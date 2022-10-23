package com.example.erkan.repository;

import com.example.erkan.dao.BuildingDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuildingRepositoryTest {
	@Mock
	BuildingRepository buildingRepository;

	@Test
	void findByKey() {
		Optional<BuildingDAO> buildingDAO = Optional.of(new BuildingDAO());
		buildingDAO.get().setName("Harmotie");
		buildingDAO.get().setKey("exKey");
		buildingDAO.get().setStreet("Harmotie");
		buildingDAO.get().setCity("Vantaa");
		when(buildingRepository.findByKey("exKey")).thenReturn(buildingDAO);
		Optional<BuildingDAO> res = buildingRepository.findByKey("exKey");
		assertEquals(res, buildingDAO);
		Optional<BuildingDAO> empty = buildingRepository.findByKey("gg");
		assertNotEquals(empty, buildingDAO);

	}
}