package ca.mcgill.ecse321.gamemanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.gamemanager.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class GameRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private GameRepository gameRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        gameRepo.deleteAll();
        categoryRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadGameVal() {
        // Create and save category
        Category category = new Category("FPS", "First Person Shooting Game");
        category = categoryRepo.save(category);

        // Game attributes
        String title = "Valorant";
        String description = "An FPS game made by Riot";
        String imageUrl = "https://example.com/valorant-image.png";
        double price = 19.99;
        Game.GameStatus gameStatus = Game.GameStatus.Available;
        String genre = "Tactical Shooter";
        int stock = 3;
        Game.RequestStatus requestStatus = Game.RequestStatus.Approved;

        // Create and save game
        Game game = new Game(title, description, genre, price, stock, gameStatus, requestStatus, category, imageUrl);
        game.setImageUrl(imageUrl);
        game = gameRepo.save(game);

        // Retrieve game
        Game gameFromDb = gameRepo.findByGameId(game.getGameId());

        // Assert initial properties
        assertNotNull(gameFromDb);
        assertEquals(title, gameFromDb.getTitle());
        assertEquals(description, gameFromDb.getDescription());
        assertEquals(imageUrl, gameFromDb.getImageUrl());
        assertEquals(price, gameFromDb.getPrice());
        assertEquals(stock, gameFromDb.getStock());
        assertEquals(gameStatus, gameFromDb.getGameStatus());
        assertEquals(requestStatus, gameFromDb.getRequestStatus());
        assertEquals(category.getName(), gameFromDb.getCategory().getName());

        // Assert initial popularity and average rating
        assertEquals(0, gameFromDb.getPopularity(), "Initial popularity should be 0.");
        assertEquals(0.0, gameFromDb.getAverageRating(), "Initial average rating should be 0.0.");

        // Test incrementPopularity and updateAverageRating
        gameFromDb.updateAverageRating(4.0); // Has increment popularity function
        gameRepo.save(gameFromDb); // Save the updated game back to the database

        // Update the imageUrl and save
        String updatedImageUrl = "https://example.com/updated-valorant-image.png";
        gameFromDb.setImageUrl(updatedImageUrl);
        gameRepo.save(gameFromDb);

        // Retrieve the updated game
        Game updatedGameFromDb = gameRepo.findByGameId(game.getGameId());

        // Assert updated popularity and average rating
        assertEquals(1, updatedGameFromDb.getPopularity(), "Popularity should be 1 after one increment.");
        assertEquals(4.0, updatedGameFromDb.getAverageRating(), "Average rating should be 4.0 after one rating of 4.0.");

        // Add another rating and verify the updated average rating
        updatedGameFromDb.updateAverageRating(5.0); // Adding a second rating
        gameRepo.save(updatedGameFromDb); // Save changes to the database

        // Retrieve the game again and check the new average rating
        Game finalGameFromDb = gameRepo.findByGameId(game.getGameId());
        assertEquals(2, finalGameFromDb.getPopularity(), "Popularity should be 2 after two interactions.");
        assertEquals(4.5, finalGameFromDb.getAverageRating(), "Average rating should be 4.5 after ratings of 4.0 and 5.0.");
        assertEquals(updatedImageUrl, updatedGameFromDb.getImageUrl(), "Image URL should be updated correctly.");
    }
}
