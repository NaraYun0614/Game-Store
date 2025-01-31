package ca.mcgill.ecse321.gamemanager.integration;

import ca.mcgill.ecse321.gamemanager.dto.*;
import ca.mcgill.ecse321.gamemanager.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Define the order to conduct the tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// Tell the springBoot to store everything I modified?
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class EmployeeIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeAll
    @AfterAll
    public void cleanUp() {
        employeeRepository.deleteAll();
    }

    public final String VALID_NAME = "Doe";
    public final String VALID_NEW_NAME = "John";
    public final String EMPTY_NAME = "";
    public final String VALID_EMAIL = "doe@example.com";
    public final String NEW_EMAIL = "john@example.com";
    public final String VALID_PASSWORD = "123456789";
    public final String INVALID_PASSWORD = "";
    public final String VALID_NEW_PASSWORD = "987654321";



    @Test
    @Order(1)
    public void testAndCreateValidEmployee() {
        EmployeeRequestDto requestDto = new EmployeeRequestDto(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

        ResponseEntity<EmployeeResponseDto> response = client.postForEntity("/api/employees", requestDto, EmployeeResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        EmployeeResponseDto responseBody = response.getBody();
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_EMAIL, responseBody.getEmail());
    }

    @Test
    @Order(2)
    public void testAndCreateEmployeeWithExistingEmail() {
        EmployeeRequestDto requestDto = new EmployeeRequestDto(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

        ResponseEntity<ErrorDto> response = client.postForEntity("/api/employees", requestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @Order(3)
    public void testAndCreateEmployeeWithInvalidPassword() {
        EmployeeRequestDto requestDto = new EmployeeRequestDto(VALID_NAME, NEW_EMAIL, INVALID_PASSWORD);

        ResponseEntity<ErrorDto> response = client.postForEntity("/api/employees", requestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    @Order(4)
    public void testAndFindEmployeeByEmail() {
        ResponseEntity<EmployeeResponseDto> response = client.getForEntity("/api/employees/" + VALID_EMAIL, EmployeeResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        EmployeeResponseDto responseBody = response.getBody();
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_EMAIL, responseBody.getEmail());

    }


    @Test
    @Order(5)
    public void testAndFindEmployeeByInvalidEmail() {
        ResponseEntity<EmployeeResponseDto> response = client.getForEntity("/api/employees/" + NEW_EMAIL, EmployeeResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }


    @Test
    @Order(6)
    public void testAndUpdateValidEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_NEW_NAME, VALID_EMAIL, VALID_NEW_PASSWORD);
        String url = "/api/employees/" + VALID_EMAIL;

        ResponseEntity<EmployeeResponseDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                EmployeeResponseDto.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        EmployeeResponseDto createdEmployee = response.getBody();
        assertEquals(VALID_NEW_NAME, createdEmployee.getName());
    }
    @Test
    @Order(7)
    public void testAndUpdateNotExistingEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_NEW_NAME, NEW_EMAIL, VALID_NEW_PASSWORD);
        String url = "/api/employees/" + NEW_EMAIL;

        ResponseEntity<EmployeeResponseDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                EmployeeResponseDto.class
        );
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testAndUpdateEmployeeWithInvalidName() {
        EmployeeRequestDto request = new EmployeeRequestDto(EMPTY_NAME, VALID_EMAIL, VALID_NEW_PASSWORD);
        String url = "/api/employees/" + this.VALID_EMAIL;

        ResponseEntity<EmployeeResponseDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                EmployeeResponseDto.class
        );
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(9)
    public void testAndUpdateEmployeeWithInvalidPassword() {
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_NEW_NAME, VALID_EMAIL, INVALID_PASSWORD);
        String url = "/api/employees/" + this.VALID_EMAIL;

        ResponseEntity<EmployeeResponseDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                EmployeeResponseDto.class
        );
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(10)
    public void testAndDeleteEmployeeWithInvalidEmail() {
        String url = "/api/employees/" + NEW_EMAIL;
        client.delete(url);
        ResponseEntity<String> response = client.getForEntity(url,String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(11)
    public void testAndDeleteValidEmployee() {

        String url = "/api/employees/" + VALID_EMAIL;
        client.delete(url);
        ResponseEntity<String> response = client.getForEntity(url,String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }


}
