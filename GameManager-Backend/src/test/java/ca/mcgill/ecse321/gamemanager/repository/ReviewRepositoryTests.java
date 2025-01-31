package ca.mcgill.ecse321.gamemanager.repository;

import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.Customer;
import ca.mcgill.ecse321.gamemanager.model.Review;
import ca.mcgill.ecse321.gamemanager.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void testCreateAndLoadReview() {
        // Initialize Category
        Category category = new Category("FPS","First Person Shooting Game");
        // save Category
        category = categoryRepository.save(category);
        String imageUrl = "https://example.com/test-game-image.png";

        // Initialize Game
        Game game = new Game("Test Game", "A great game", "genre", 59.99, 3, Game.GameStatus.Available,Game.RequestStatus.Approved,category, imageUrl);

        game.setImageUrl(imageUrl); 
        
        // Save Game
        game = gameRepository.save(game);

        // Initialize Customer
        Customer customer = new Customer("password123", "Test Customer", "Test Customer");
        // Save Customer
        customer = customerRepository.save(customer);

        // Initialize Review
        Date date = Date.valueOf("2024-01-01");
        Review review = new Review(5, "Excellent game!", date, customer, game);
        // Save
        review = reviewRepository.save(review);

        // Fetch Review from Database
        Review reviewFromDb = reviewRepository.findById(review.getReviewId()).orElse(null);

        assertNotNull(reviewFromDb);
        assertEquals(5, reviewFromDb.getRating());
        assertEquals("Excellent game!", reviewFromDb.getDescription());
        assertEquals("2024-01-01", reviewFromDb.getDate().toString());
        assertEquals(game.getGameId(), reviewFromDb.getGame().getGameId());
        assertEquals(customer.getEmail(), reviewFromDb.getCreated().getEmail());
    }
}