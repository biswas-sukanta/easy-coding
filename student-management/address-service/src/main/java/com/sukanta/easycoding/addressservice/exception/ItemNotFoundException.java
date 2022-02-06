package com.sukanta.easycoding.addressservice.exception;

public class ItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6380505176952105463L;

	public ItemNotFoundException(String message) {
		super(message);
	}

}
