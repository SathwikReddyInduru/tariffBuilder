package com.xius.TariffBuilder.Dao;

import lombok.Data;

@Data
public class LoginResponse {
	
	
	    public LoginResponse(String message, String token) {
		super();
		this.message = message;
		this.token = token;
	}
		private String message;
	    private String token;
	}