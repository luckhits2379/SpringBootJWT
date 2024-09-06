package com.ng.springboot.jwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ng.springboot.jwt.configurations.JWTUtility;

@RestController
public class MyRestController {

	@Autowired
	JWTUtility jWTUtility;

	@GetMapping("/protected/")
	public ResponseEntity<String> getProtectedResponse() {

		UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		System.out.println("loggedInUser using bearer token: " + loggedInUser);

		return ResponseEntity.ok("I am protected response");

	}

	@GetMapping("/public/")
	public ResponseEntity<String> getPublicResponse() {

		return ResponseEntity.ok("I am public response");

	}

	@GetMapping("/generateToken/")
	public ResponseEntity<Token> generateToken() {

		UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String token = this.jWTUtility.generateToken(loggedInUser);

		Token genratedToken = new Token();
		genratedToken.setToken(token);

		return new ResponseEntity<>(genratedToken, HttpStatus.OK);

	}

	class Token {

		String Token;

		public String getToken() {
			return Token;
		}

		public void setToken(String token) {
			Token = token;
		}

	}
}
