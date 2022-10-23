package com.example.erkan.services;

import com.example.erkan.dto.BuildingDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface BuildingService {
	@Async
	CompletableFuture<List<BuildingDTO>> addBuildings(List<BuildingDTO> buildings);

	Page<BuildingDTO> getBuildings(String sortBy, Integer offset, Integer amount, Boolean isAsc);

	Optional<BuildingDTO> editBuilding(String key, BuildingDTO buildingDTO);

	ResponseEntity deleteBuilding(String key);

}
