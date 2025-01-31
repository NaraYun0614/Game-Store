package ca.mcgill.ecse321.gamemanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.gamemanager.model.*;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateCustomer() {
        Customer customer = new Customer("password123", "John Doe", "john@example.com");
        Customer savedCustomer = customerRepository.save(customer);

        // assertions of created customer
        assertNotNull(savedCustomer.getEmail());
        assertEquals("John Doe", savedCustomer.getName());
        assertEquals("john@example.com", savedCustomer.getEmail());
    }

    @Test
    @Transactional
    public void testReadCustomer() {
        Customer customer = new Customer("password123", "Jane Doe", "jane@example.com");
        Customer savedCustomer = customerRepository.save(customer);

        Customer retrievedCustomer = customerRepository.findCustomerByEmail(savedCustomer.getEmail());

        // assertions of customer retrieved from database
        assertNotNull(retrievedCustomer);
        assertEquals("Jane Doe", retrievedCustomer.getName());
        assertEquals("jane@example.com", retrievedCustomer.getEmail());
    }
}
