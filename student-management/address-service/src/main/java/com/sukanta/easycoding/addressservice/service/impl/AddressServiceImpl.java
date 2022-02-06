package com.sukanta.easycoding.addressservice.service.impl;

import java.util.Set;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.sukanta.easycoding.addressservice.exception.ItemNotFoundException;
import com.sukanta.easycoding.addressservice.mapper.AddressMapper;
import com.sukanta.easycoding.addressservice.model.Address;
import com.sukanta.easycoding.addressservice.repository.AddressRepository;
import com.sukanta.easycoding.addressservice.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	AddressRepository addressRepository;

	private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);

	Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

	@Override
	public Address addAddress(@RequestBody Address address) {
		return addressMapper.entityToAddress(addressRepository.saveAndFlush(addressMapper.addressToEntity(address)));
	}

	@Override
	public Set<Address> findAddressByEmail(String studentEmail) {
		logger.info("inside AddressController-findAddressByEmail()");
		return addressMapper.entityToAddressSet(addressRepository.findByStudentEmail(studentEmail)
				.orElseThrow(() -> new ItemNotFoundException("Item not found : " + studentEmail)));
	}
}