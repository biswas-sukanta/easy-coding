package com.sukanta.easycoding.studentservice.feignclients;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sukanta.easycoding.studentservice.model.Address;

@FeignClient(value = "api-gateway-service")
public interface AddressFeignClient {
	@GetMapping("/address-service/findAddressByEmail/{studentFirstName}")
	ResponseEntity<Set<Address>> findAddressByEmail(@PathVariable String studentFirstName);

	@PostMapping("/address-service/addAddress")
	ResponseEntity<Address> addAddress(@RequestBody Address address);
}
