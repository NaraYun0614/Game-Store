package ca.mcgill.ecse321.gamemanager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import ca.mcgill.ecse321.gamemanager.repository.GameCopyRepository;
import ca.mcgill.ecse321.gamemanager.repository.GameRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class GameCopyServiceTests {

    @Mock
    private GameCopyRepository gameCopyRepo;

    @Mock
    private GameRepository gameRepo;

    @InjectMocks
    private GameCopyService gameCopyService;

    @Test
    public void testFindGameCopyByGameCopyId_ValidId() {
        int gameCopyId = 1;
        GameCopy gameCopy = new GameCopy();
        ReflectionTestUtils.setField(gameCopy, "gameCopyId", gameCopyId);

        when(gameCopyRepo.findGameCopyByGameCopyId(gameCopyId)).thenReturn(gameCopy);

        GameCopy foundGameCopy = gameCopyService.findGameCopyByGameCopyId(gameCopyId);

        assertNotNull(foundGameCopy);
        assertEquals(gameCopyId, foundGameCopy.getGameCopyId());
    }

    @Test
    public void testFindGameCopyByGameCopyId_InvalidId() {
        int invalidId = 99;
        when(gameCopyRepo.findGameCopyByGameCopyId(invalidId)).thenReturn(null);

        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            gameCopyService.findGameCopyByGameCopyId(invalidId);
        });
        assertEquals("Invalid GameCopy ID.", exception.getMessage());
    }

    @Test
    public void testFindGameCopiesByGame_ValidId() {
        int gameId = 1;
        Game game = new Game();
        game.setGameId(gameId);

        GameCopy gameCopy = new GameCopy(game);
        List<GameCopy> gameCopies = Collections.singletonList(gameCopy);

        when(gameRepo.findByGameId(gameId)).thenReturn(game);
        when(gameCopyRepo.findGameCopiesByGame(game)).thenReturn(gameCopies);

        List<GameCopy> result = gameCopyService.findGameCopiesByGame(gameId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(gameCopy, result.get(0));
    }

    @Test
    public void testFindGameCopiesByGame_InvalidId() {
        int invalidGameId = 99;
        when(gameRepo.findByGameId(invalidGameId)).thenReturn(null);

        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            gameCopyService.findGameCopiesByGame(invalidGameId);
        });
        assertEquals("Invalid Game ID.", exception.getMessage());
    }

    @Test
    public void testCountGameCopyOfGame_ValidId() {
        int gameId = 1;
        Game game = new Game();
        game.setGameId(gameId);
        long expectedCount = 5;

        when(gameRepo.findByGameId(gameId)).thenReturn(game);
        when(gameCopyRepo.countByGame(game)).thenReturn(expectedCount);

        long result = gameCopyService.countGameCopyOfGame(gameId);

        assertEquals(expectedCount, result);
    }

    @Test
    public void testCountGameCopyOfGame_InvalidId() {
        int invalidGameId = 99;
        when(gameRepo.findByGameId(invalidGameId)).thenReturn(null);

        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            gameCopyService.countGameCopyOfGame(invalidGameId);
        });
        assertEquals("Invalid Game ID.", exception.getMessage());
    }

    @Test
    public void testCreateGameCopy_ValidGameId_WithStock() {
        int gameId = 1;
        Game game = new Game();
        game.setGameId(gameId);
        game.setStock(10); // Ensure stock is greater than 0

        GameCopy newGameCopy = new GameCopy(game);

        when(gameRepo.findByGameId(gameId)).thenReturn(game);
        when(gameCopyRepo.save(any(GameCopy.class))).thenReturn(newGameCopy); // Use any(GameCopy.class) here

        GameCopy createdGameCopy = gameCopyService.createGameCopy(gameId);

        assertNotNull(createdGameCopy);
        assertEquals(game.getGameId(), createdGameCopy.getGame().getGameId());
        assertEquals(game.getStock(), createdGameCopy.getGame().getStock());
        verify(gameRepo, times(1)).save(game);
        verify(gameCopyRepo, times(1)).save(any(GameCopy.class)); // Use any(GameCopy.class) for verification
    }

    @Test
    public void testCreateGameCopy_InvalidGameId() {
        int invalidGameId = 99;
        when(gameRepo.findByGameId(invalidGameId)).thenReturn(null);

        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            gameCopyService.createGameCopy(invalidGameId);
        });
        assertEquals("Invalid Game ID.", exception.getMessage());
    }

    @Test
    public void testCreateGameCopy_OutOfStock() {
        int gameId = 1;
        Game game = new Game();
        game.setGameId(gameId);
        game.setStock(0); // Set stock to 0 to simulate out-of-stock condition
        game.setTitle("Example Game");

        when(gameRepo.findByGameId(gameId)).thenReturn(game);

        // Attempt to create a game copy and expect an IllegalStateException
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            gameCopyService.createGameCopy(gameId);
        });
        assertEquals("Example Game with Game ID " + gameId + " is currently out of stock.", exception.getMessage());
    }


    @Test
    public void testReturnGameCopy_ValidId() {
        int gameCopyId = 1;
        Game game = new Game();
        game.setGameId(1);
        game.setStock(5);

        GameCopy gameCopy = new GameCopy(game);
        ReflectionTestUtils.setField(gameCopy, "gameCopyId", gameCopyId);

        when(gameCopyRepo.findGameCopyByGameCopyId(gameCopyId)).thenReturn(gameCopy);
        when(gameRepo.findByGameId(game.getGameId())).thenReturn(game);

        gameCopyService.returnGameCopy(gameCopyId);

        assertEquals(6, game.getStock()); // Stock should be incremented
        verify(gameCopyRepo, times(1)).delete(gameCopy);
    }

    @Test
    public void testReturnGameCopy_InvalidGameCopyId() {
        int invalidGameCopyId = 99;
        when(gameCopyRepo.findGameCopyByGameCopyId(invalidGameCopyId)).thenReturn(null);

        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            gameCopyService.returnGameCopy(invalidGameCopyId);
        });
        assertEquals("Invalid GameCopy ID.", exception.getMessage());
    }
}