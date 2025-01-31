package ca.mcgill.ecse321.gamemanager.repository;

import ca.mcgill.ecse321.gamemanager.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repo;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){ repo.deleteAll();}

    @Test
    public void testCreateAndReadCategory() {

        // creating a new category
        String name = "Test Category";
        String description = "Test Description";

        Category jTestCategory = new Category(name, description);

        // saving and retrieving from the database
        jTestCategory = repo.save(jTestCategory);
        Category CategoryFromDB = repo.findCategoryByCategoryId(jTestCategory.getCategoryId());

        // assertions
        assertNotNull(CategoryFromDB);
        assertEquals(name, CategoryFromDB.getName());
        assertEquals(description, CategoryFromDB.getDescription());
    }

}