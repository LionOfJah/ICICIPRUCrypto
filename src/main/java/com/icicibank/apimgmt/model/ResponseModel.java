package com.icicibank.apimgmt.model;

import org.springframework.stereotype.Component;

@Component
public class ResponseModel {

	private String response;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public ResponseModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
