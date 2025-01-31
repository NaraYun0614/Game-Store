package ca.mcgill.ecse321.gamemanager.integration;


import ca.mcgill.ecse321.gamemanager.dto.CustomerResponseDto;
import ca.mcgill.ecse321.gamemanager.dto.CustomerRequestDto;
import ca.mcgill.ecse321.gamemanager.dto.ErrorDto;
import ca.mcgill.ecse321.gamemanager.dto.ReviewResponseDto;
import ca.mcgill.ecse321.gamemanager.repository.CustomerRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class CustomerIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeAll
    @AfterAll
    public void cleanUp() {

        customerRepository.deleteAll();

    }

    private final String aPassword = "12345678";
    private final String aUsername = "Ang";
    private final String email = "ang@example.com";
    private final String updatedUsername = "Ang1";
    private final String updatedPassword = "23456789";
    private  String aEmail ;



    @Test
    @Order(1)

    public void testCreateValid(){
        //set
        CustomerRequestDto request = new CustomerRequestDto( aUsername,email,aPassword);

        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customers", request, CustomerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());


        CustomerResponseDto createdCustomerResponseDto = response.getBody();
        aEmail = createdCustomerResponseDto.getEmail();
        assertEquals(aUsername, createdCustomerResponseDto.getName());
        assertEquals(email, createdCustomerResponseDto.getEmail());
    }

    @Test
    @Order(2)
    public void testUpdateNameValid(){


        CustomerRequestDto request = new CustomerRequestDto( updatedUsername,email,updatedPassword);

        String url = "/customers/" + this.email;



        ResponseEntity<CustomerResponseDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                CustomerResponseDto.class
        );

        assertNotNull(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        CustomerResponseDto createdCustomerResponseDto = response.getBody();
        assertEquals(updatedUsername, createdCustomerResponseDto.getName());
        assertEquals(updatedPassword, createdCustomerResponseDto.getPassword());

    }
    @Test
    @Order(3)
    public void testCreateInValidEmail(){
        //set

        CustomerRequestDto request = new CustomerRequestDto( aUsername,email,aPassword);

        ResponseEntity<String> response = client.postForEntity("/customers", request, String.class);

        assertNotNull(response);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }


    @Test
    @Order(4)
    public void testCreateInValidPassword(){
        //set
        String inValidPassword = "123";
        String newEmail = "ang123@example.com";
        CustomerRequestDto request = new CustomerRequestDto( aUsername,newEmail,inValidPassword);
        ResponseEntity<String> response = client.postForEntity("/customers", request, String.class);


        assertNotNull(response);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @Order(5)
    public void testUpdateInValidEmail(){

        String inValidEmail = "Ang1234@example.com";
        CustomerRequestDto request = new CustomerRequestDto( updatedUsername,inValidEmail,updatedPassword);
        String url = "/customers/" + inValidEmail;

        ResponseEntity<ErrorDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ErrorDto.class
        );
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


    }
    @Test
    @Order(6)
    public void testUpdateInValidName(){
        String inValidName = null;
        CustomerRequestDto request = new CustomerRequestDto( inValidName,email,updatedPassword);
        String url = "/customers/" + this.email;
        ResponseEntity<ErrorDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ErrorDto.class
        );
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void testUpdateInValidPassword(){
        String inValidPassword = "123";
        CustomerRequestDto request = new CustomerRequestDto( updatedUsername,email,inValidPassword);
        String url = "/customers/" + this.email;
        ResponseEntity<ErrorDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ErrorDto.class
        );
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testDeleteEmailInValid(){

        String inValidEmail1 = "Ang12345@example.com";
        //CustomerRequestDto request = new CustomerRequestDto( aUsername,inValidEmail1,aPassword);
        String url="/customers/" + inValidEmail1;

        client.delete(url);
        ResponseEntity<String> response = client.getForEntity(url, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    @Order(9)
    public void testDeleteEmailValid(){

        String url="/customers/" + this.email;

        client.delete(url);
        ResponseEntity<String> response = client.getForEntity(url, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }



}
