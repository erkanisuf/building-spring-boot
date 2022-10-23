package com.example.erkan.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GeopifyResponse {
	private List<GeopifyBuilding> results;
	private HashMap query;
}
