package com.example.simplecrmlite.controller;

import com.example.simplecrmlite.model.Customer;
import com.example.simplecrmlite.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for CustomerController
 * Tests the GET /customers endpoint
 */
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerRepository customerRepository;

    /**
     * Test: GET /customers should return list of customers
     */
    @Test
    void testGetAllCustomers() throws Exception {
        // Arrange - Create test data
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setEmail("john@example.com");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setEmail("jane@example.com");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        // Mock the repository to return test data
        when(customerRepository.findAll()).thenReturn(customers);

        // Act & Assert - Make request and verify response
        mockMvc.perform(get("/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }
}
