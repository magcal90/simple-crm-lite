package com.example.simplecrmlite.controller;

import com.example.simplecrmlite.model.Customer;
import com.example.simplecrmlite.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        logger.info("Retrieved {} customers", customers.size());
        return customers;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        ResponseEntity<Customer> response = customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        logger.info("Customer lookup for id={} returned status={}", id, response.getStatusCode());
        return response;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer saved = customerRepository.save(customer);
        logger.info("Created customer");
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,
                                                     @RequestBody Customer updatedCustomer) {
        return customerRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(updatedCustomer.getFirstName());
                    existing.setLastName(updatedCustomer.getLastName());
                    existing.setEmail(updatedCustomer.getEmail());
                    existing.setContactNo(updatedCustomer.getContactNo());
                    existing.setJobTitle(updatedCustomer.getJobTitle());
                    existing.setYearOfBirth(updatedCustomer.getYearOfBirth());
                    Customer saved = customerRepository.save(existing);
                    logger.info("Updated customer with id={}", id);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> {
                    logger.info("Customer update requested for id={}, but no customer was found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            logger.info("Customer deletion requested for id={}, but no customer was found", id);
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        logger.info("Deleted customer with id={}", id);
        return ResponseEntity.noContent().build();
    }
}
