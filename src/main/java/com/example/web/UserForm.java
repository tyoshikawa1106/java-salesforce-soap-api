package com.example.web;

import lombok.Data;

@Data
public class UserForm {
	private String userId;
	private String password;
	private String authEndpoint;
}