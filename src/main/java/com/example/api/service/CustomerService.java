package com.example.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.api.domain.Customer;
import com.example.api.repository.CustomerRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
	
	public Page<Customer> findAll(Pageable page) {
		return repository.findAll(page);
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
			customer.get().setAddresses(updatedCustomer.getAddresses());
			repository.save(customer.get());
		}
		
		return customer;
	}
	
	public Boolean validateCustomer(Customer customer) {
		
		Boolean isValid = customer != null && !customer.getName().trim().isEmpty() && !customer.getEmail().trim().isEmpty() && customer.getAddresses() != null;
		return isValid;
	}
	
	public String getAddressFromCep(String cep) {
		
		String url = String.format("http://viacep.com.br/ws/%s/json", cep);
		RestTemplate restTemplate = new RestTemplate();
		
		String result = restTemplate.getForObject(url, String.class);
		
		JsonObject resultJson = new Gson().fromJson(result, JsonObject.class);
		String address = resultJson.get("logradouro").toString();
		
		address = address.replaceAll("^\"|\"$", "");
		
		return address;
		
	}
}
