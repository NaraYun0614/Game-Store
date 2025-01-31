package ca.mcgill.ecse321.gamemanager.integration;

import ca.mcgill.ecse321.gamemanager.dto.CategoryDto;
import ca.mcgill.ecse321.gamemanager.dto.ErrorDto;
import ca.mcgill.ecse321.gamemanager.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class CategoryIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CategoryRepository categoryRepository;

    private int validCategoryId;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        categoryRepository.deleteAll();
    }

    // Test for creating a valid category
    @Test
    @Order(1)
    public void testCreateValidCategory() {
        CategoryDto request = new CategoryDto(0, "Adventure", "Exciting adventure games", null);
        ResponseEntity<CategoryDto> response = client.postForEntity("/categories", request, CategoryDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CategoryDto createdCategory = response.getBody();
        assertNotNull(createdCategory);
        assertEquals("Adventure", createdCategory.getName());
        assertEquals("Exciting adventure games", createdCategory.getDescription());
        assertNotNull(createdCategory.getCategoryId());

        this.validCategoryId = createdCategory.getCategoryId();
    }

    @Test
    @Order(2)
    public void testCreateCategoryWithInvalidData() {
        CategoryDto request = new CategoryDto(0, "", "No name", null);
        ResponseEntity<ErrorDto> response = client.postForEntity("/categories", request, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto errorBody = response.getBody();
        assertNotNull(errorBody);
        assertTrue(errorBody.getErrors().contains("Category name cannot be empty"));
    }

    @Test
    @Order(3)
    public void testGetCategoryByValidId() {
        String url = "/categories/" + this.validCategoryId;

        ResponseEntity<CategoryDto> response = client.getForEntity(url, CategoryDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CategoryDto category = response.getBody();
        assertNotNull(category);
        assertEquals("Adventure", category.getName());
        assertEquals("Exciting adventure games", category.getDescription());
        assertEquals(this.validCategoryId, category.getCategoryId());
    }

    @Test
    @Order(4)
    public void testGetCategoryByInvalidId() {
        String url = "/categories/999";

        ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto errorBody = response.getBody();
        assertNotNull(errorBody);
        assertEquals(1, errorBody.getErrors().size());
        assertEquals("Category with ID 999 not found", errorBody.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testUpdateCategory() {
        String url = "/categories/" + this.validCategoryId;
        CategoryDto updatedRequest = new CategoryDto(this.validCategoryId, "Adventure Updated", "Updated adventure games", null);

        client.put(url, updatedRequest);

        ResponseEntity<CategoryDto> response = client.getForEntity(url, CategoryDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CategoryDto updatedCategory = response.getBody();
        assertNotNull(updatedCategory);
        assertEquals("Adventure Updated", updatedCategory.getName());
        assertEquals("Updated adventure games", updatedCategory.getDescription());
    }

    @Test
    @Order(6)
    public void testUpdateCategoryWithInvalidId() {
        String url = "/categories/999";
        CategoryDto updatedRequest = new CategoryDto(999, "Nonexistent Update", "Should not exist", null);

        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(updatedRequest), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto errorBody = response.getBody();
        assertNotNull(errorBody);
        assertTrue(errorBody.getErrors().contains("Category with ID 999 not found"));
    }

    @Test
    @Order(7)
    public void testDeleteCategory() {
        String url = "/categories/" + this.validCategoryId;
        client.delete(url);

        ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getErrors().contains("Category with ID " + this.validCategoryId + " not found"));
    }

    @Test
    @Order(8)
    public void testDeleteCategoryWithInvalidId() {
        String url = "/categories/999";
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.DELETE, null, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto errorBody = response.getBody();
        assertNotNull(errorBody);
        assertTrue(errorBody.getErrors().contains("Category with ID 999 not found"));
    }
}
