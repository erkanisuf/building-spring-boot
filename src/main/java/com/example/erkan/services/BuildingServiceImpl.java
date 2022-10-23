package com.example.erkan.services;

import com.example.erkan.dao.BuildingDAO;
import com.example.erkan.dto.BuildingDTO;
import com.example.erkan.exceptions.NotFoundBuildingRuntime;
import com.example.erkan.models.GeopifyResponse;
import com.example.erkan.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
	@Value("${geopify.key}")
	private String API_KEY;
	private Integer DEFAULT_OFFSET = 0;
	private Integer DEFAULT_PAGE_SIZE = 10;
	private String DEFAULT_SORT_BY = "name";
	private final BuildingRepository buildingRepository;
	private final ModelMapper modelMapper;
	private final RestTemplate restTemplate;
	Logger logger = LoggerFactory.getLogger(BuildingServiceImpl.class);


	public String createUrl(String street, String streetNumber, String country, String postalCode, String city) {
		return "https://api.geoapify.com/v1/geocode/search?" + "street=" + street + "&housenumber=" + streetNumber + "&city=" + city + "&country=" + country + "&postcode=" + postalCode + "&limit=20&format=json&apiKey=" + API_KEY;
	}

	public BuildingDAO getBuildingLocation(BuildingDAO buildingDAO) {
		BuildingDAO newBuilding = buildingDAO;
		String url = this.createUrl(buildingDAO.getStreet(), buildingDAO.getStreetNumber(), buildingDAO.getCountry(), buildingDAO.getPostalCode(), buildingDAO.getCity());

		GeopifyResponse geopifyResponse = restTemplate.getForObject(url, GeopifyResponse.class);
		if (geopifyResponse.getResults() != null && !geopifyResponse.getResults().isEmpty()) {
			Double lat = geopifyResponse.getResults().get(0).getLat();
			Double lon = geopifyResponse.getResults().get(0).getLon();
			if (lat != null && lon != null) {
				logger.info("Location for address: {} has been found. Proceeding to add coordinates", buildingDAO.getStreet() + ", " + buildingDAO.getStreetNumber());
				newBuilding.setLat(lat);
				newBuilding.setLon(lon);
				newBuilding.setLocation(new GeometryFactory().createPoint(new Coordinate(lon, lat)));
			} else {
				logger.warn("Location was not find for address: {}. Proceeding to add building with no coordinates", buildingDAO.getStreet() + ", " + buildingDAO.getStreetNumber());
			}
		} else {
			logger.warn("Could not find any results with url: {}. Proceeding to add building with no coordinates ", url);
		}
		return newBuilding;
	}

	@Override
	@Async
	public CompletableFuture<List<BuildingDTO>> addBuildings(List<BuildingDTO> buildings) {
		List<BuildingDAO> newBuildings = buildings.stream().map(b -> modelMapper.map(b, BuildingDAO.class)).map(b -> this.getBuildingLocation(b)).collect(Collectors.toList());
		buildingRepository.saveAll(newBuildings);
		List<BuildingDTO> savedBuildings = newBuildings.stream().map(b -> modelMapper.map(b, BuildingDTO.class)).collect(Collectors.toList());
		return CompletableFuture.completedFuture(savedBuildings);
	}


	public Page<BuildingDTO> getBuildings(String sortBy, Integer offset, Integer amount, Boolean isASC) {
		Integer offsetPage = offset != null ? offset : DEFAULT_OFFSET;
		Integer amountPage = amount != null ? amount : DEFAULT_PAGE_SIZE;
		Sort sortPage = sortBy != null ? Sort.by(sortBy) : Sort.by(DEFAULT_SORT_BY);

		if (isASC != null && isASC) {
			sortPage = sortPage.ascending();
		} else {
			sortPage = sortPage.descending();
		}

		Page<BuildingDAO> buildingDAOPage = buildingRepository.findAll(PageRequest.of(offsetPage, amountPage, sortPage));
		Page<BuildingDTO> buildingDTOS = buildingDAOPage.map(buildingDTO -> modelMapper.map(buildingDTO, BuildingDTO.class));
		return buildingDTOS;
	}


	@Override
	public Optional<BuildingDTO> editBuilding(String key, BuildingDTO buildingDTO) {
		Optional<BuildingDAO> building = buildingRepository.findByKey(key);
		if (building.isEmpty()) {
			logger.error("Editing is not possible! Could not find building with key: {} .", key);
			throw new NotFoundBuildingRuntime(key);
		}

		BuildingDAO editedBuilding = building.get();
		editedBuilding.setKey(key);
		editedBuilding.setStreet(buildingDTO.getStreet());
		editedBuilding.setStreetNumber(buildingDTO.getStreetNumber());
		editedBuilding.setCity(buildingDTO.getCity());
		editedBuilding.setCountry(buildingDTO.getCountry());
		editedBuilding.setPostalCode(buildingDTO.getCountry());
		editedBuilding.setDescription(buildingDTO.getDescription());

		if (buildingDTO.getName() != null) {
			editedBuilding.setName(buildingDTO.getName());
		}

		if (buildingDTO.getDescription() != null) {
			editedBuilding.setDescription(buildingDTO.getDescription());
		}

		if (buildingDTO.getLat() != null && buildingDTO.getLon() != null) {
			logger.info("Changing location with provided coordinates by user lat: {} , lon: {} .", buildingDTO.getLat(), buildingDTO.getLon());
			editedBuilding.setLat(buildingDTO.getLat());
			editedBuilding.setLon(buildingDTO.getLon());
			editedBuilding.setLocation(new GeometryFactory().createPoint(new Coordinate(buildingDTO.getLon(), buildingDTO.getLat())));
		} else {

			BuildingDAO locationFromGeopify = this.getBuildingLocation(editedBuilding);
			if (locationFromGeopify.getLat() != null && locationFromGeopify.getLon() != null) {
				logger.info("Changing location with  coordinates by Geopify lat: {} , lon: {} .", locationFromGeopify.getLat(), locationFromGeopify.getLon());
				editedBuilding.setLat(locationFromGeopify.getLat());
				editedBuilding.setLon(locationFromGeopify.getLon());
				editedBuilding.setLocation(new GeometryFactory().createPoint(new Coordinate(locationFromGeopify.getLon(), locationFromGeopify.getLat())));
			} else {
				logger.info("Could not find location from Geopify of address: {} .", editedBuilding.getStreet() + ", " + editedBuilding.getStreetNumber());
			}
		}

		buildingRepository.save(editedBuilding);
		return Optional.of(modelMapper.map(editedBuilding, BuildingDTO.class));
	}


	@Override
	public ResponseEntity deleteBuilding(String key) {
		Optional<BuildingDAO> building = buildingRepository.findByKey(key);
		if (building.isEmpty()) {
			logger.error("Deletion is not possible! Could not find building with key: {} .", key);
			throw new NotFoundBuildingRuntime(key);
		}

		buildingRepository.deleteById(building.get().getId());
		logger.info("Building has been deleted with key: {} .", key);
		return ResponseEntity.ok().build();

	}
}
