package com.example.customerapi.service;

import com.example.customerapi.model.Customer;
import com.example.customerapi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(LocalDate.now());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));

        if (!existingCustomer.getEmail().equals(customerDetails.getEmail()) &&
                customerRepository.existsByEmail(customerDetails.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setLastName(customerDetails.getLastName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setDateOfBirth(customerDetails.getDateOfBirth());
        existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> getCustomersBetweenAges(int minAge, int maxAge) {
        LocalDate today = LocalDate.now();
        LocalDate maxDateOfBirth = today.minusYears(minAge);
        LocalDate minDateOfBirth = today.minusYears(maxAge);
        return customerRepository.findByDateOfBirthBetween(minDateOfBirth, maxDateOfBirth);
    }

    public double getAverageAge() {
        return customerRepository.findAverageAge().orElse(0.0);
    }
}