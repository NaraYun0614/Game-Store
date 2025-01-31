package ca.mcgill.ecse321.gamemanager.integration;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.gamemanager.dto.ErrorDto;
import ca.mcgill.ecse321.gamemanager.dto.GameCopyResponseDto;
import ca.mcgill.ecse321.gamemanager.dto.PurchaseOrderDto;
import ca.mcgill.ecse321.gamemanager.dto.PurchaseOrderRequestDto;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder.OrderStatus;
import ca.mcgill.ecse321.gamemanager.repository.GameCopyRepository;
import ca.mcgill.ecse321.gamemanager.repository.GameRepository;
import ca.mcgill.ecse321.gamemanager.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class PurchaseOrderIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCopyRepository gameCopyRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;


    private int orderId;
    private final OrderStatus VALID_STATUS = OrderStatus.ShoppingCart;
    private final OrderStatus INVALID_STATUS = null;
    private final double VALID_PRICE = 123.99;
    private final double INVALID_PRICE = -2;
    private final Date VALID_DATE = Date.valueOf(LocalDate.now());
    private final Date INVALID_DATE = null;
    private final List<GameCopyResponseDto> VALID_GAMECOPIES = new ArrayList<>();

    @BeforeAll
    @AfterAll
    public void clearDb() {
        gameCopyRepository.deleteAll();
        gameRepository.deleteAll();
        purchaseOrderRepository.deleteAll();
    }




    @Test
    @Order(1)
    public void testCreateValidOrder() {
        // Arrange
        Game game = new Game();
        GameCopy gameCopy = new GameCopy(game);
        gameRepository.save(game);
        gameCopyRepository.save(gameCopy);
        VALID_GAMECOPIES.add(new GameCopyResponseDto(gameCopy));
        PurchaseOrderRequestDto request = new PurchaseOrderRequestDto(VALID_STATUS, VALID_GAMECOPIES, VALID_PRICE);

        // Act
        ResponseEntity<PurchaseOrderDto> response = client.postForEntity("/api/orders", request, PurchaseOrderDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PurchaseOrderDto createdOrder = response.getBody();
        assertNotNull(createdOrder);
        assertEquals(VALID_PRICE, createdOrder.getPrice());
        assertEquals(VALID_STATUS, createdOrder.getOrderStatus());
        assertNotNull(createdOrder.getOrderId());
        this.orderId = createdOrder.getOrderId();
    }

    @Test
    @Order(2)
    public void testCreateInvalidOrder() {
        // Arrange
        PurchaseOrderRequestDto request = new PurchaseOrderRequestDto(VALID_STATUS, VALID_GAMECOPIES, INVALID_PRICE);

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/api/orders", request, ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Price cannot be negative.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testFindOrderById() {
        // Arrange
        String url = "/api/orders/" + this.orderId;

        // Act
        ResponseEntity<PurchaseOrderDto> response = client.getForEntity(url, PurchaseOrderDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PurchaseOrderDto createdOrder = response.getBody();
        assertNotNull(createdOrder);
        assertEquals(VALID_PRICE, createdOrder.getPrice());
        assertEquals(VALID_STATUS, createdOrder.getOrderStatus());
        assertNotNull(createdOrder.getOrderId());
        this.orderId = createdOrder.getOrderId();
    }

    @Test
    @Order(4)
    public void testUpdateValidOrder() {
        // Arrange
        String url = "/api/orders/" + this.orderId;
        PurchaseOrderDto updated = new PurchaseOrderDto(orderId, OrderStatus.ShoppingCart, 1, VALID_DATE);

        client.put(url, updated);

        // Act
        ResponseEntity<PurchaseOrderDto> response = client.getForEntity(url, PurchaseOrderDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PurchaseOrderDto updatedOrder = response.getBody();
        assertNotNull(updatedOrder);
        assertEquals(1, updatedOrder.getPrice());
        assertEquals(OrderStatus.ShoppingCart, updatedOrder.getOrderStatus());
    }

    @Test
    @Order(5)
    public void testAddGameToCart() {
        // Arrange
        Game savedGame = gameRepository.save(new Game());

        GameCopy gameCopy1 = gameCopyRepository.save(new GameCopy(savedGame));
        int game1 = gameCopy1.getGameCopyId();

        GameCopy gameCopy2 = gameCopyRepository.save(new GameCopy(savedGame));
        int game2 = gameCopy2.getGameCopyId();

        List<Integer> validGameCopyIds = List.of(game1, game2);

        HttpEntity<List<Integer>> putRequest = new HttpEntity<>(validGameCopyIds);

        String url = "/api/orders/" + this.orderId + "/cart";

        // Act
        ResponseEntity<PurchaseOrderDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                putRequest,
                new ParameterizedTypeReference<PurchaseOrderDto>() {}
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PurchaseOrderDto updatedOrder = response.getBody();
        assertNotNull(updatedOrder);
        assertEquals(this.orderId, updatedOrder.getOrderId());

        for (int i = 0; i < validGameCopyIds.size(); i++) {
            System.out.println(i);
            int expectedId = validGameCopyIds.get(i);
            int actualId = updatedOrder.getGameCopies().get(i).getGameCopyId();
            assertEquals(expectedId, actualId+1);
        }
    }

    @Test
    @Order(6)
    public void testCheckOut() {
        String url = "/api/orders/" + this.orderId;
        PurchaseOrderDto request = new PurchaseOrderDto(orderId, VALID_STATUS, VALID_PRICE, VALID_DATE);
        client.postForEntity(url, request, PurchaseOrderDto.class);

        ResponseEntity<PurchaseOrderDto> response = client.getForEntity(url, PurchaseOrderDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PurchaseOrderDto updatedOrder = response.getBody();
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.Bought, updatedOrder.getOrderStatus());
    }

    @Test
    @Order(7)
    public void testDeleteOrder() {
        String url = "/api/orders/" + this.orderId;
        client.delete(url);

        ResponseEntity<String> response = client.getForEntity(url, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // should already be deleted
    }
}
