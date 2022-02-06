package com.sukanta.easycoding.addressservice.service;

import java.util.Set;

import com.sukanta.easycoding.addressservice.model.Address;

public interface AddressService {
	Address addAddress(Address address);

	Set<Address> findAddressByEmail(String studentEmail);
}
