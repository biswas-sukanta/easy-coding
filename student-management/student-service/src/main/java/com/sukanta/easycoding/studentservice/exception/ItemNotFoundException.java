package com.sukanta.easycoding.studentservice.exception;

public class ItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 960237675805351437L;

	public ItemNotFoundException(String message) {
		super(message);
	}

}
