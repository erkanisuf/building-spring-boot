package com.example.erkan.exceptions;

public class NotFoundBuildingRuntime extends RuntimeException {

	public NotFoundBuildingRuntime(String key) {
		super("Building key not found : " + key);
	}

}