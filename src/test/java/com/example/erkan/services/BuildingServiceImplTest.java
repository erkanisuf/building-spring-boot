package com.example.erkan.services;

import com.example.erkan.dto.BuildingDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@ExtendWith(MockitoExtension.class)
class BuildingServiceImplTest {
	@Value("${geopify.key}")
	private String FAKE_API_KEY;

	@Mock
	private BuildingServiceImpl buildingService;


	@Test
	void createUrl() {
		String street = "Harmotie";
		String streetNumber = "5";
		String country = "FI";
		String postalCode = "01200";
		String city = "Vantaa";

		String result = buildingService.createUrl(street, streetNumber, country, postalCode, city);
		assertEquals(result, "https://api.geoapify.com/v1/geocode/search?" + "street=" + street + "&housenumber=" + streetNumber + "&city=" + city + "&country=" + country + "&postcode=" + postalCode + "&limit=20&format=json&apiKey=" + FAKE_API_KEY);

	}

	@Test
	void addBuildings() throws ExecutionException, InterruptedException {
		BuildingDTO buildingDTO = BuildingDTO.builder().name("Harmotie").street("Harmotie").city("Vantaa").country("Finland").lon(55.55).lon(55.55).build();
		List<BuildingDTO> buildingsDTO = Arrays.asList(buildingDTO);
		when(buildingService.addBuildings(any())).thenReturn(CompletableFuture.completedFuture(buildingsDTO));
		CompletableFuture<List<BuildingDTO>> result = buildingService.addBuildings(buildingsDTO);
		Assertions.assertEquals(result.get(), buildingsDTO);
	}

	@Test
	void editBuilding() {
		BuildingDTO buildingDTO = BuildingDTO.builder().key("keyName").name("Harmotie").street("Harmotie").city("Vantaa").country("Finland").lon(55.55).lon(55.55).build();
		when(buildingService.editBuilding("damn", buildingDTO)).thenReturn(Optional.ofNullable(buildingDTO));
		Optional<BuildingDTO> result = buildingService.editBuilding("damn", buildingDTO);
		Assertions.assertEquals(result.get(), buildingDTO);
	}

}