package com.example.api.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Customer;
import com.example.api.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private CustomerService service;

	@Autowired
	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@GetMapping
	public List<Customer> findAll(@RequestParam Optional<Integer> page) {
		
		if(page.isPresent()) {
			Pageable pageable = PageRequest.of(page.get(), 3, Sort.by("name").ascending());
			Page<Customer> customersPage = service.findAll(pageable);
			
			return customersPage.getContent();
		}
		else {
			return service.findAll();
		}
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
	}
	
	@PostMapping
	public void add(@RequestBody Customer newCustomer, @RequestParam Optional<String> cep) {
		
		if(service.validateCustomer(newCustomer)) {
			if(cep.isPresent()) {
				String address = service.getAddressFromCep(cep.get());
				List<String> addresses = new ArrayList<String>();
				addresses.add(address);
				newCustomer.setAddresses(addresses);
			}
			service.addCustomer(newCustomer);
		}
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.deleteById(id);
	}
	
	@PatchMapping("/{id}")
	public Customer update(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
		
		if(service.validateCustomer(updatedCustomer)) {
			return service.updateById(id, updatedCustomer)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
		}
		
		return null;
	}
}
