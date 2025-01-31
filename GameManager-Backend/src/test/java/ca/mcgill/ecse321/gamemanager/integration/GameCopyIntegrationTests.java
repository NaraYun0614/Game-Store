package ca.mcgill.ecse321.gamemanager.integration;


import ca.mcgill.ecse321.gamemanager.dto.ErrorDto;
import ca.mcgill.ecse321.gamemanager.dto.GameCopyRequestDto;
import ca.mcgill.ecse321.gamemanager.dto.GameCopyResponseDto;
import ca.mcgill.ecse321.gamemanager.dto.GameDto;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.repository.GameCopyRepository;
import ca.mcgill.ecse321.gamemanager.repository.GameRepository;

import org.apache.tomcat.util.json.JSONParser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class GameCopyIntegrationTests {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCopyRepository gameCopyRepository;

    @Autowired
    private TestRestTemplate client;
    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        gameRepository.deleteAll();
        gameCopyRepository.deleteAll();
    }

    private final String TITLE = "MineCraft";
    private final double PRICE = 0.1;
    private final int STOCK = 10;
    private final int INVALID_GAME_COPY_ID = -1;
    private final int INVALID_GAME_ID = -1;

    private int validGameCopyId;
    private int validGameId;

    @BeforeAll
    @AfterAll
    public void clearDb() {
        gameCopyRepository.deleteAll();
        gameRepository.deleteAll();
    }


    @Test
    @Order(1)
    @Commit
    public void testCreateValidGameCopy() {
        // Arrange
        Game game = new Game();
        game.setStock(STOCK);
        game.setPrice(PRICE);
        game.setTitle(TITLE);
        gameRepository.save(game);
        GameDto gameDto = new GameDto(game);
        GameCopyRequestDto request = new GameCopyRequestDto(game);
        System.out.println(request);
        // Act
        ResponseEntity<GameCopyResponseDto> response = client.postForEntity("/game-copy", request, GameCopyResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        GameCopyResponseDto createdGameCopyDto = response.getBody();
        assertNotNull(createdGameCopyDto);
        GameDto responsedGameDto = createdGameCopyDto.getGameDto();

        assertEquals(gameDto.getGameId(), responsedGameDto.getGameId());
        assertEquals(TITLE, responsedGameDto.getTitle());
        assertEquals(PRICE, responsedGameDto.getPrice());
        assertEquals(STOCK-1, responsedGameDto.getStock());
        assertTrue(createdGameCopyDto.getGameCopyId() > 0, "GameCopyResponse should have a positive ID.");
        assertTrue(responsedGameDto.getGameId() > 0, "GameResponse should have a positive ID.");
        this.validGameCopyId = createdGameCopyDto.getGameCopyId();
        this.validGameId = responsedGameDto.getGameId();
    }

    @Test
    @Order(2)
    public void testCreateInvalidGameCopy() {
        // Arrange
        Game game = new Game();
        game.setGameId(INVALID_GAME_ID);
        GameDto gameDto = new GameDto(game);
        GameCopyRequestDto request = new GameCopyRequestDto(game);

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/game-copy", request, ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Invalid Game ID.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testFindGameCopyByValidGameCopyId() {
        // Arrange
        Game game = new Game();
        game.setStock(STOCK);
        game.setPrice(PRICE);
        game.setTitle(TITLE);
        gameRepository.save(game);
        GameDto gameDto = new GameDto(game);
        GameCopyRequestDto request = new GameCopyRequestDto(game);
        int id = request.getGameId();
        ResponseEntity<GameCopyResponseDto> postResponse = client.postForEntity("/game-copy", request, GameCopyResponseDto.class);
        validGameCopyId = postResponse.getBody().getGameCopyId();
        validGameId = postResponse.getBody().getGameDto().getGameId();
        String url = "/game-copy/" + this.validGameCopyId;

        // Act
        ResponseEntity<GameCopyResponseDto> response = client.getForEntity(url, GameCopyResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameCopyResponseDto gameCopyResponseDto = response.getBody();
        assertNotNull(gameCopyResponseDto);
        assertEquals(validGameCopyId, gameCopyResponseDto.getGameCopyId());
        assertEquals(validGameId, gameCopyResponseDto.getGameDto().getGameId());
        assertEquals(STOCK-1, gameCopyResponseDto.getGameDto().getStock());
        assertEquals(PRICE, gameCopyResponseDto.getGameDto().getPrice());
    }



    @Test
    @Order(4)
    public void testFindGameCopyByInvalidGameCopyId(){
        // Arrange
        String url = "/game-copy/" + this.INVALID_GAME_COPY_ID;

        // Act
        ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Invalid GameCopy ID.", body.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testFindGameCopyByValidGameId() {
        // Arrange
        Game game = new Game();
        game.setStock(STOCK);
        game.setPrice(PRICE);
        game.setTitle(TITLE);
        Game savedGame = gameRepository.save(game);
        GameCopyRequestDto request = new GameCopyRequestDto(game);
        ResponseEntity<GameCopyResponseDto> postResponse = client.postForEntity("/game-copy", request, GameCopyResponseDto.class);
        validGameCopyId = postResponse.getBody().getGameCopyId();
        validGameId = postResponse.getBody().getGameDto().getGameId();
        String url = "/game/"+ savedGame.getGameId() + "/game-copies";

        // Act
        ResponseEntity<List<GameCopyResponseDto>> response = client.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GameCopyResponseDto>>() {}
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameCopyResponseDto> gameCopyResponseDtos = response.getBody();
        GameCopyResponseDto gameCopyResponseDto = gameCopyResponseDtos.get(0);
        assertNotNull(gameCopyResponseDtos);
        assertTrue(gameCopyResponseDtos.size()>0, "The number of GameCopy found should be positive.");
        assertEquals(validGameCopyId, gameCopyResponseDto.getGameCopyId());
        assertEquals(validGameId, gameCopyResponseDto.getGameDto().getGameId());
        assertEquals(STOCK-1, gameCopyResponseDto.getGameDto().getStock());
        assertEquals(PRICE, gameCopyResponseDto.getGameDto().getPrice());
    }

    @Test
    @Order(6)
    public void testFindGameCopyByInvalidGameId() {
        // Arrange
        String url = "/game/" + INVALID_GAME_ID + "/game-copies";

        // Act
        ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Invalid Game ID.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testCountGameCopyOfGame() {
        // Arrange
        Game game = new Game();
        game.setStock(STOCK);
        game.setPrice(PRICE);
        game.setTitle(TITLE);
        gameRepository.save(game);
        GameDto gameDto = new GameDto(game);
        GameCopyRequestDto request = new GameCopyRequestDto(game);
        ResponseEntity<GameCopyResponseDto> postResponse = client.postForEntity("/game-copy", request, GameCopyResponseDto.class);
        validGameCopyId = postResponse.getBody().getGameCopyId();
        validGameId = postResponse.getBody().getGameDto().getGameId();
        String url = "/game/" + validGameId + "/game-copies/count";

        // Act
        ResponseEntity<Integer> response = client.getForEntity(url, Integer.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());

    }

    @Test
    @Order(8)
    public void testCountGameCopyOfGameWithoutGameCopy() {
        // Arrange
        Game game = new Game();
        game.setStock(STOCK);
        game.setPrice(PRICE);
        game.setTitle(TITLE);
        Game savedGame = gameRepository.save(game);
        validGameId = savedGame.getGameId();
        String url = "/game/" + validGameId + "/game-copies/count";

        // Act
        ResponseEntity<Integer> response = client.getForEntity(url, Integer.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    @Test
    @Order(8)
    public void testDeleteValidGameCopy() {
        // Arrange
        Game game = new Game();
        game.setStock(STOCK);
        game.setPrice(PRICE);
        game.setTitle(TITLE);
        gameRepository.save(game);
        GameDto gameDto = new GameDto(game);
        GameCopyRequestDto request = new GameCopyRequestDto(game);
        ResponseEntity<GameCopyResponseDto> postResponse = client.postForEntity("/game-copy", request, GameCopyResponseDto.class);
        validGameCopyId = postResponse.getBody().getGameCopyId();
        String url = "/game-copy/" + validGameCopyId;

        // Act
        client.delete(url, GameCopyResponseDto.class);
        ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid GameCopy ID.", response.getBody().getErrors().get(0));
    }

    @Test
    @Order(9)
    public void testDeleteInvalidGameCopy() {
        // Arrange
        Game game = new Game();
        game.setStock(STOCK);
        game.setPrice(PRICE);
        game.setTitle(TITLE);
        gameRepository.save(game);
        GameDto gameDto = new GameDto(game);
        GameCopyRequestDto request = new GameCopyRequestDto(game);
        ResponseEntity<GameCopyResponseDto> postResponse = client.postForEntity("/game-copy", request, GameCopyResponseDto.class);
        validGameCopyId = postResponse.getBody().getGameCopyId();
        validGameId = postResponse.getBody().getGameDto().getGameId();
        String deleteUrl = "/game-copy/" + INVALID_GAME_COPY_ID;
        String findUrl = "/game-copy/" + validGameCopyId;

        // Act
        client.delete(deleteUrl, GameCopyResponseDto.class);
        ResponseEntity<GameCopyResponseDto> response = client.getForEntity(findUrl, GameCopyResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameCopyResponseDto gameCopyResponseDto = response.getBody();
        assertNotNull(gameCopyResponseDto);
        assertEquals(validGameCopyId, gameCopyResponseDto.getGameCopyId());
        GameDto respondseGameDto = gameCopyResponseDto.getGameDto();
        assertNotNull(respondseGameDto);
        assertEquals(validGameId, respondseGameDto.getGameId());
        assertEquals(STOCK-1, respondseGameDto.getStock());
        assertEquals(TITLE, respondseGameDto.getTitle());
        assertEquals(PRICE, respondseGameDto.getPrice());
    }
}
