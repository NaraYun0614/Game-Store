package ca.mcgill.ecse321.gamemanager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Category;
import ca.mcgill.ecse321.gamemanager.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testGetAllCategories() {
        Category category1 = new Category();
        category1.setCategoryId(1);
        category1.setName("Adventure");
        category1.setDescription("Exciting adventure games");

        Category category2 = new Category();
        category2.setCategoryId(2);
        category2.setName("Puzzle");
        category2.setDescription("Challenging puzzle games");

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);
        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Adventure", result.get(0).getName());
        assertEquals("Puzzle", result.get(1).getName());
    }

    @Test
    public void testGetCategoryById_ValidId() {
        int categoryId = 1;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setName("Adventure");
        category.setDescription("Exciting adventure games");

        when(categoryRepository.findById(String.valueOf(categoryId))).thenReturn(Optional.of(category));
        Category result = categoryService.getCategoryById(categoryId);
        assertNotNull(result);
        assertEquals("Adventure", result.getName());
        assertEquals("Exciting adventure games", result.getDescription());
    }

    @Test
    public void testGetCategoryById_InvalidId() {
        int invalidId = 99;
        when(categoryRepository.findById(String.valueOf(invalidId))).thenReturn(Optional.empty());
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            categoryService.getCategoryById(invalidId);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Category with ID " + invalidId + " not found", exception.getMessage());
    }

    @Test
    public void testCreateCategory_ValidCategory() {
        Category category = new Category();
        category.setName("Adventure");
        category.setDescription("Exciting adventure games");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        Category result = categoryService.createCategory(category);
        assertNotNull(result);
        assertEquals("Adventure", result.getName());
        assertEquals("Exciting adventure games", result.getDescription());
    }

    @Test
    public void testCreateCategory_InvalidCategory_NoName() {
        Category category = new Category();
        category.setDescription("Exciting adventure games");
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            categoryService.createCategory(category);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Category name cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateCategory_InvalidCategory_NoDescription() {
        Category category = new Category();
        category.setName("Adventure");
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            categoryService.createCategory(category);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Category description cannot be empty", exception.getMessage());
    }

    @Test
    public void testUpdateCategory_ValidId() {
        int categoryId = 1;
        Category existingCategory = new Category();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setName("Adventure");
        existingCategory.setDescription("Old description");

        Category updatedCategory = new Category();
        updatedCategory.setName("Adventure Updated");
        updatedCategory.setDescription("Updated description");

        when(categoryRepository.findById(String.valueOf(categoryId))).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        Category result = categoryService.updateCategory(categoryId, updatedCategory);
        assertNotNull(result);
        assertEquals("Adventure Updated", result.getName());
        assertEquals("Updated description", result.getDescription());
    }

    @Test
    public void testUpdateCategory_InvalidId() {
        int invalidId = 99;
        Category updatedCategory = new Category();
        updatedCategory.setName("New Name");
        updatedCategory.setDescription("New description");

        when(categoryRepository.findById(String.valueOf(invalidId))).thenReturn(Optional.empty());
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            categoryService.updateCategory(invalidId, updatedCategory);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Category with ID " + invalidId + " not found", exception.getMessage());
    }

    @Test
    public void testDeleteCategory_ValidId() {
        int categoryId = 1;
        when(categoryRepository.existsById(String.valueOf(categoryId))).thenReturn(true);
        categoryService.deleteCategory(categoryId);
        verify(categoryRepository, times(1)).deleteById(String.valueOf(categoryId));
    }

    @Test
    public void testDeleteCategory_InvalidId() {
        int invalidId = 99;
        when(categoryRepository.existsById(String.valueOf(invalidId))).thenReturn(false);
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            categoryService.deleteCategory(invalidId);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Category with ID " + invalidId + " not found", exception.getMessage());
    }
}
