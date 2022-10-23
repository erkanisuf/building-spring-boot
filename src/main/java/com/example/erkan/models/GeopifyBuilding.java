package com.example.erkan.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeopifyBuilding {
	private String name;
	private String street;
	private String suburb;
	private String district;
	private String city;
	private String county;
	private String state;
	private String postcode;
	private String country;
	private String country_code;
	private Double lon;
	private Double lat;

}

