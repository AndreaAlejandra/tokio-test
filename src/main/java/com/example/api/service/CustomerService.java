package com.example.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.domain.Customer;
import com.example.api.repository.CustomerRepository;

@Service
public class CustomerService {

	private CustomerRepository repository;

	@Autowired
	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public List<Customer> findAll() {
		return repository.findAllByOrderByNameAsc();
	}
	
	public Optional<Customer> findById(Long id) {
		return repository.findById(id);
	}
	
	public void addCustomer(Customer newCustomer) {
		repository.save(newCustomer);
	}
	
	public void deleteById(long id) {
		repository.deleteById(id);
	}

	public Optional<Customer> updateById(long id, Customer updatedCustomer) {
		Optional<Customer> customer = this.findById(id);
		
		if(customer.isPresent()) {
			customer.get().setName(updatedCustomer.getName());
			customer.get().setEmail(updatedCustomer.getEmail());
			repository.save(customer.get());
		}
		
		return customer;
	}
}
