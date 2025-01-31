package ca.mcgill.ecse321.gamemanager.repository;

import ca.mcgill.ecse321.gamemanager.model.Customer;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class PurchaseOrderRepositoryTests {

    @Autowired
    private PurchaseOrderRepository repo;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadOrder() {
        // Create customer
        Customer customer = new Customer("123", "Bob", "bob@gmail.com");
        customer = customerRepository.save(customer);

        // Create order
        OrderStatus status = OrderStatus.Bought;
        double price = 34.23;
        Date date = Date.valueOf("2024-10-10");

        PurchaseOrder test1 = new PurchaseOrder(status, price, date);

        // Save order in the database
        test1 = repo.save(test1);
        int test1Id = test1.getOrderId();

        // Retrieve order from the database
        PurchaseOrder orderFromDb = repo.findByOrderId(test1Id);

        // Assertions
        assertNotNull(orderFromDb);  // Ensure order is not null
        assertEquals(test1Id, orderFromDb.getOrderId());  // Check order ID
        assertEquals(status, orderFromDb.getOrderStatus());  // Check order status
        assertEquals(price, orderFromDb.getTotalPrice());  // Check total price
        assertEquals(date, orderFromDb.getDate());  // Check date
        //assertEquals(customer.getEmail(), orderFromDb.getCustomer().getEmail());  // Check customer association
    }
}
