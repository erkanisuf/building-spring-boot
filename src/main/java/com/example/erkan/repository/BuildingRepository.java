package com.example.erkan.repository;

import com.example.erkan.dao.BuildingDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildingRepository extends PagingAndSortingRepository<BuildingDAO, Long> {
	@Query(
				value = "SELECT * FROM buildings b WHERE b.key = ?1",
				nativeQuery = true)
	Optional<BuildingDAO> findByKey(String key);


}
