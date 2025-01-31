package ca.mcgill.ecse321.gamemanager.controller;

import ca.mcgill.ecse321.gamemanager.dto.GameRequestDto;
import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.gamemanager.dto.GameDto;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    // Add an exception handler for IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Create a new game.
     */

    @PostMapping
    public ResponseEntity<GameDto> createGame(@RequestBody GameRequestDto gameRequestDto) {
        Game createdGame = gameService.createGame(
                gameRequestDto.getTitle(),
                gameRequestDto.getDescription(),
                gameRequestDto.getGenre(),
                gameRequestDto.getPrice(),
                gameRequestDto.getStock(),
                gameRequestDto.getGameStatus(),
                gameRequestDto.getRequestStatus(),
                gameRequestDto.getCategoryId(), // Use the provided category ID
                gameRequestDto.getImageUrl()
        );

        GameDto createdGameDto = new GameDto(createdGame);
        return new ResponseEntity<GameDto>(createdGameDto, HttpStatus.CREATED);
    }

    /**
     * Update an existing game by ID.
     */
    @PutMapping("/{id}")
    public GameDto updateGame(@PathVariable int id, @RequestBody GameRequestDto gameRequestDto) {
        Game updatedGame = gameService.updateGame(
                id,
                gameRequestDto.getTitle(),
                gameRequestDto.getDescription(),
                gameRequestDto.getGenre(),
                gameRequestDto.getPrice(),
                gameRequestDto.getStock(),
                gameRequestDto.getGameStatus(),
                gameRequestDto.getRequestStatus(),
                gameRequestDto.getCategoryId(),  // Pass categoryId instead of the Category object
                gameRequestDto.getImageUrl()
        );
        return new GameDto(updatedGame);
    }

    /**
     * Get a game by ID.
     */
    @GetMapping("/{id}")
    public GameDto findByGameId(@PathVariable int id) {
        Game game = gameService.findByGameId(id);
        return new GameDto(game);
    }

    /**
     * Get all games.
     */
    @GetMapping
    public List<GameDto> getAllGames() {
        Iterable<Game> games = gameService.findAllGames();
        List<GameDto> gameDtos = new ArrayList<>();
        for (Game game : games) {
            gameDtos.add(new GameDto(game));
        }
        return gameDtos;
    }

    /**
     * Delete a game by ID.
     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteGame(@PathVariable int id) {
//        gameService.deleteGame(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable int id) {
        try {
            gameService.deleteGame(id);
            return ResponseEntity.ok("Game deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete game");
        }
    }

    /**
     * Search games by keyword or category with sorting options.
     */
    @GetMapping("/search")
    public List<GameDto> searchGames(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "popularity") String sortBy
    ) {
        List<Game> games = gameService.searchGames(keyword, category, sortBy);
        return games.stream().map(GameDto::new).collect(Collectors.toList());
    }

    /**
     * Get detailed game information, including reviews.
     */
    @GetMapping("/{id}/details")
    public GameDto getGameDetails(@PathVariable int id) {
        return gameService.getGameDetails(id);
    }

    /**
     * Handle invalid search input with suggestions.
     */
    @GetMapping("/search/handleInvalid")
    public String handleInvalidSearch(@RequestParam String keyword) {
        return gameService.handleInvalidSearch(keyword);
    }
    @GetMapping("/pending")
    public List<GameDto> getPendingGames() {
        List<Game> pendingGames = gameService.findPendingGames();
        return pendingGames.stream().map(GameDto::new).collect(Collectors.toList());
    }

    /**
     * Approve a game addition.
     */
    @PutMapping("/{id}/approve")
    public GameDto approveGame(@PathVariable int id) {
        Game approvedGame = gameService.approveGameAddition(id);
        return new GameDto(approvedGame);
    }

    /**
     * Reject a game addition.
     */
    @PutMapping("/{id}/reject")
    public GameDto rejectGame(@PathVariable int id) {
        Game rejectedGame = gameService.rejectGameAddition(id);
        return new GameDto(rejectedGame);
    }

    @PutMapping("/{id}/status")
    public GameDto updateGameStatus(@PathVariable int id, @RequestParam Game.GameStatus newStatus) {
        Game updatedGame = gameService.updateGameStatus(id, newStatus);
        return new GameDto(updatedGame);
    }

    @PostMapping("/direct-add")
    public ResponseEntity<GameDto> addGameDirectly(@RequestBody GameRequestDto gameRequestDto) {
        Game createdGame = gameService.createGame(
                gameRequestDto.getTitle(),
                gameRequestDto.getDescription(),
                gameRequestDto.getGenre(),
                gameRequestDto.getPrice(),
                gameRequestDto.getStock(),
                Game.GameStatus.Available,
                Game.RequestStatus.Approved,
                gameRequestDto.getCategoryId(),
                gameRequestDto.getImageUrl()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new GameDto(createdGame));
    }

    //    @DeleteMapping("/direct-remove/{id}")
//    public ResponseEntity<Void> removeGameDirectly(@PathVariable int id) {
//        gameService.deleteGame(id);
//        return ResponseEntity.noContent().build();
//    }
    @DeleteMapping("/direct-remove/{id}")
    public ResponseEntity<Void> removeGameDirectly(@PathVariable int id) {
        try {
            gameService.deleteGame(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("{gId}/addReview/{rId}")
    public GameDto addReview(@PathVariable int gId, @PathVariable int rId) {
        Game game = gameService.addReview(gId, rId);
        return new GameDto(game);
    }
}