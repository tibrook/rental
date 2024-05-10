package com.chatop.rental.exception;


public class AuthenticationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public AuthenticationException(String message) {
		super(message);
	}
	
	
}
