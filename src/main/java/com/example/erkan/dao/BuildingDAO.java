package com.example.erkan.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Builder
@AllArgsConstructor
@Entity
@Data
@Table(name = "buildings")
@NoArgsConstructor
@Getter
@Setter
public class BuildingDAO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank
	@NotEmpty
	@NotNull
	private String key;
	private String name;
	@NotBlank
	@NotEmpty
	@NotNull
	private String street;
	@NotBlank
	@NotEmpty
	@NotNull
	private String streetNumber;
	@NotBlank
	@NotEmpty
	@NotNull
	private String postalCode;
	@NotBlank
	@NotEmpty
	@NotNull
	private String city;
	@NotBlank
	@NotEmpty
	@NotNull
	private String country;
	private String description;
	private Double lat;
	private Double lon;
	private Point location;
}
