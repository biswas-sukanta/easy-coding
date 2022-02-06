package com.sukanta.easycoding.addressservice.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import com.sukanta.easycoding.addressservice.entity.AddressEntity;
import com.sukanta.easycoding.addressservice.model.Address;

@Mapper
public interface AddressMapper {
	AddressEntity addressToEntity(Address address);

	Address entityToAddress(AddressEntity addressEntity);

	Set<AddressEntity> addressToEntitySet(Set<Address> address);

	Set<Address> entityToAddressSet(Set<AddressEntity> addressEntity);
}
