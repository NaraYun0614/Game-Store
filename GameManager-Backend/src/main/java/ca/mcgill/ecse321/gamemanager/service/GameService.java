package ca.mcgill.ecse321.gamemanager.service;


import ca.mcgill.ecse321.gamemanager.dto.GameDto;
import ca.mcgill.ecse321.gamemanager.dto.ReviewDto;
import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Category;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.Review;
import ca.mcgill.ecse321.gamemanager.repository.GameRepository;
import ca.mcgill.ecse321.gamemanager.repository.CategoryRepository;

import ca.mcgill.ecse321.gamemanager.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepo;

        /*public static Game findByGameId(int id) {
            Game game = gameRepo.findByGameId(id);
            if (game == null) {
                throw new IllegalArgumentException("There is no game with ID " + id + ".");
            }
            return game;
        }*/

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Retrieve a game by ID, throwing an exception if not found.
     *
     * @param id The ID of the game.
     * @return The game with the specified ID.
     */
    public Game findByGameId(int id) {
        Game game = gameRepository.findByGameId(id);
        if (game == null) {
            throw new IllegalArgumentException("There is no game with ID " + id + ".");
        }
        return game;
    }

    /**
     * Create a new game with the provided attributes and save it in the repository.
     *
     * @param title        The title of the game.
     * @param description  The description of the game.
     * @param genre        The genre of the game.
     * @param price        The price of the game.
     * @param stock        The stock of the game.
     * @param gameStatus   The status of the game.
     * @param requestStatus The request status of the game.
     * @param categoryId    The category of the game.
     * @param imageUrl      The URL of the game's image.
     * @return The created game.
     */

     @Transactional
     public Game createGame(String title, String description, String genre, double price, int stock,
                            Game.GameStatus gameStatus, Game.RequestStatus requestStatus, Integer categoryId, String imageUrl) {
         // Fetch the category by ID from the database
         Category category = categoryRepository.findCategoryByCategoryId(categoryId);
         if (category == null) {
             throw new GameManagerException(HttpStatus.BAD_REQUEST, "There is no category with ID " + categoryId + ".");
         }
 
         // Create the Game with the associated Category and imageUrl
         Game gameToCreate = new Game(title, description, genre, price, stock, gameStatus, requestStatus, category, imageUrl);
         return gameRepository.save(gameToCreate);
     }

    /**
     * Search games by keyword or category and sort by popularity, rating, and relevance.
     *
     * @param keyword The search keyword (optional).
     * @param category The category to filter by (optional).
     * @param sortBy The sorting criteria, e.g., "popularity", "rating", "releaseDate".
     * @return A list of games matching the search criteria.
     */
    @Transactional
    public List<Game> searchGames(String keyword, String category, String sortBy) {
        List<Game> games;

        // If no keyword and no category is provided, return popular categories or a message
        if ((keyword == null || keyword.isBlank()) && (category == null || category.isBlank())) {
            throw new IllegalArgumentException("Please enter a keyword or select a category.");
        }

        // Search games by keyword and category
        if (keyword != null && !keyword.isBlank()) {
            games = gameRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        } else {
            games = gameRepository.findByCategoryName(category);
        }

        // Sort games based on the selected sorting criteria
        games = sortGames(games, sortBy);

        // Return games if found, otherwise throw an exception
        if (games.isEmpty()) {
            throw new IllegalArgumentException("Game not found");
        }
        return games;
    }

    @Transactional
    public Game updateGame(int id, String title, String description, String genre, double price, int stock,
                           Game.GameStatus gameStatus, Game.RequestStatus requestStatus, int categoryId, String imageUrl) {
        Game game = gameRepository.findByGameId(id);
        if (game == null) {
            throw new IllegalArgumentException("There is no game with ID " + id + ".");
        }

        // Retrieve the category using the categoryId
        Category category = categoryRepository.findCategoryByCategoryId(categoryId);
        if (category == null) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "There is no category with ID " + categoryId + ".");
        }

        // Update the game’s attributes
        game.setTitle(title);
        game.setDescription(description);
        game.setGenre(genre);
        game.setPrice(price);
        game.setStock(stock);
        game.setGameStatus(gameStatus);
        game.setRequestStatus(requestStatus);
        game.setCategory(category);
        game.setImageUrl(imageUrl); // Update the image URL

        return gameRepository.save(game);
    }

    @Transactional
    public void deleteGame(int id) {
        Game game = gameRepo.findByGameId(id);
        if (game ==null){
            throw new IllegalArgumentException("There is no game with ID " + id + ".");
        }
        gameRepo.delete(game);
    }

    public Iterable<Game> findAllGames() {
        return gameRepo.findAll();
    }

    /**
     * Helper method to sort games based on different criteria.
     *
     * @param games  The list of games to sort.
     * @param sortBy The sorting criteria.
     * @return A sorted list of games.
     */

    private List<Game> sortGames(List<Game> games, String sortBy) {
        if ("popularity".equalsIgnoreCase(sortBy)) {
            return games.stream().sorted(Comparator.comparingInt(Game::getPopularity).reversed()).collect(Collectors.toList());
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            return games.stream().sorted(Comparator.comparingDouble(Game::getAverageRating).reversed()).collect(Collectors.toList());
        }
        return games;  // Default, unsorted
    }

    /**
     * Retrieve game details, including description, images, and reviews.
     *
     * @param gameId The ID of the game.
     * @return The game with detailed information.
     */
    @Transactional
    public GameDto getGameDetails(int gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        Game game = optionalGame.orElseThrow(() -> new IllegalArgumentException("Game not found"));

        // Convert Game entity to GameDto
        return convertToDto(game);
    }

    // Helper method to convert Game entity to GameDto
    private GameDto convertToDto(Game game) {
        List<ReviewDto> reviewDtos = game.getReviews().stream()
                .map(review -> new ReviewDto(review.getReviewId(), review.getDescription(), review.getRating()))
                .collect(Collectors.toList());

        return new GameDto(
                game.getGameId(),
                game.getTitle(),
                game.getDescription(),
                game.getGenre(),
                game.getPrice(),
                game.getStock(),
                game.getPopularity(),
                game.getAverageRating(),
                reviewDtos,
                game.getGameStatus(),
                game.getRequestStatus(),
                game.getCategory() != null ? game.getCategory().getCategoryId() : 0,
                game.getCategory() != null ? game.getCategory().getName() : null,
                game.getCategory() != null ? game.getCategory().getDescription() : null,
                game.getImageUrl()
        );
    }


    /**
     * Handle an invalid search input or no search results found.
     *
     * @param keyword The invalid search keyword entered by the user.
     * @return Suggested keywords or popular categories to help the customer.
     */
    public String handleInvalidSearch(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return "Please enter a valid keyword or select a popular category.";
        }
        return "No results found for \"" + keyword + "\". Please try again or check the spelling.";
    }

    @Transactional
    public List<Game> findPendingGames() {
        return gameRepository.findByRequestStatus(Game.RequestStatus.PendingApproval);
    }

    @Transactional
    public Game approveGameAddition(int gameId) {
        Game game = gameRepository.findByGameId(gameId);
        if (game == null) {
            throw new IllegalArgumentException("There is no game with ID " + gameId + ".");
        }
        if (game.getRequestStatus() != Game.RequestStatus.PendingApproval) {
            throw new IllegalStateException("Game with ID " + gameId + " is not pending approval.");
        }

        game.setRequestStatus(Game.RequestStatus.Approved);
        game.setGameStatus(Game.GameStatus.Available); // Make the game available for purchase
        return gameRepository.save(game);
    }

    @Transactional
    public Game rejectGameAddition(int gameId) {
        Game game = gameRepository.findByGameId(gameId);
        if (game == null) {
            throw new IllegalArgumentException("There is no game with ID " + gameId + ".");
        }
        if (game.getRequestStatus() != Game.RequestStatus.PendingApproval) {
            throw new IllegalStateException("Game with ID " + gameId + " is not pending approval.");
        }

        game.setRequestStatus(Game.RequestStatus.Archived); // Mark game as archived
        game.setGameStatus(Game.GameStatus.Archived); // Update the status
        return gameRepository.save(game);
    }

    @Transactional
    public Game updateGameStatus(int gameId, Game.GameStatus newStatus) {
        Game game = findByGameId(gameId);
        game.setGameStatus(newStatus);
        return gameRepository.save(game);
    }

    @Transactional
    public Game addReview(int gameId, int reiewId) {
        Game game = gameRepository.findByGameId(gameId);
        if (game == null) {
            throw new IllegalArgumentException("There is no game with ID " + gameId + ".");
        }
        Review review = reviewRepository.findReviewByReviewId(reiewId);
        if (review == null) {
            throw new IllegalArgumentException("There is no review with ID " + reiewId + ".");
        }
        game.addReview(review);
        game.updateAverageRating(review.getRating());
        return gameRepository.save(game);
    }



}