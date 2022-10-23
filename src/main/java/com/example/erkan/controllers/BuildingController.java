package com.example.erkan.controllers;

import com.example.erkan.dto.BuildingDTO;
import com.example.erkan.exceptions.NotFoundBuildingRuntime;
import com.example.erkan.services.BuildingServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Validated
public class BuildingController {
	private BuildingServiceImpl buildingService;


	public BuildingController(BuildingServiceImpl buildingService) {
		this.buildingService = buildingService;
	}

	@GetMapping("api/v1/buildings")
	public Page<BuildingDTO> getBuildings(@RequestParam(required = false) String sortBy,
	                                      @RequestParam(required = false) Integer offset,
	                                      @RequestParam(required = false) Integer amount,
	                                      @RequestParam(required = false) Boolean isAsc) {
		return buildingService.getBuildings(sortBy, offset, amount, isAsc);
	}

	@PostMapping("api/v1/buildings")
	public CompletableFuture<List<BuildingDTO>> addNewBuildings(@RequestBody @Valid List<BuildingDTO> newBuildings) {
		return buildingService.addBuildings(newBuildings);
	}

	@DeleteMapping("api/v1/building/{key}")
	public ResponseEntity deleteBuilding(@PathVariable String key) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(buildingService.deleteBuilding(key));
		} catch (NotFoundBuildingRuntime ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Could not find building for deletion with key: %s", key));
		}

	}

	@PutMapping("api/v1/building/{key}")
	public ResponseEntity editBuilding(@PathVariable String key, @RequestBody BuildingDTO buildingDTO) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(buildingService.editBuilding(key, buildingDTO));
		} catch (NotFoundBuildingRuntime ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Could not find building to edit with key: %s", key));
		}
	}
}
