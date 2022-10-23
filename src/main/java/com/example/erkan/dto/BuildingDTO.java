package com.example.erkan.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDTO {
	private String name;
	@NotNull
	@NotEmpty
	private String key;
	@NotNull
	@NotEmpty
	private String street;
	@NotNull
	@NotEmpty
	private String streetNumber;
	@NotNull
	@NotEmpty
	private String postalCode;

	@NotNull
	@NotEmpty
	private String city;

	@NotNull
	@NotEmpty
	private String country;
	private Double lat;
	private Double lon;
	private String description;
}
