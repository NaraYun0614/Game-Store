package ca.mcgill.ecse321.gamemanager.service;

import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Category;
import ca.mcgill.ecse321.gamemanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Transactional
    public Category getCategoryById(int id) {
        return categoryRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new GameManagerException(HttpStatus.NOT_FOUND, "Category with ID " + id + " not found"));
    }

    @Transactional
    public Category createCategory(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Category name cannot be empty");
        }
        if (category.getDescription() == null || category.getDescription().isBlank()) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Category description cannot be empty");
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(int id, Category category) {
        Category existingCategory = categoryRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new GameManagerException(HttpStatus.NOT_FOUND, "Category with ID " + id + " not found"));

        if (category.getName() != null && !category.getName().isBlank()) {
            existingCategory.setName(category.getName());
        }
        if (category.getDescription() != null && !category.getDescription().isBlank()) {
            existingCategory.setDescription(category.getDescription());
        }

        return categoryRepository.save(existingCategory);
    }

    @Transactional
    public void deleteCategory(int id) {
        if (!categoryRepository.existsById(String.valueOf(id))) {
            throw new GameManagerException(HttpStatus.NOT_FOUND, "Category with ID " + id + " not found");
        }
        categoryRepository.deleteById(String.valueOf(id));
    }
}
