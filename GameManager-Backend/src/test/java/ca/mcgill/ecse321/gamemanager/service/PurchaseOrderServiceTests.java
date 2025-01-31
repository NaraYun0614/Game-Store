package ca.mcgill.ecse321.gamemanager.service;

import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder.OrderStatus;
import ca.mcgill.ecse321.gamemanager.repository.GameCopyRepository;
import org.junit.jupiter.api.Test;

import ca.mcgill.ecse321.gamemanager.repository.PurchaseOrderRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class PurchaseOrderServiceTests {
    @Mock
    private PurchaseOrderRepository repo;
    @Mock
    private GameCopyRepository gameCopyRepository;

    @InjectMocks
    private PurchaseOrderService service;
    @InjectMocks
    private GameCopyService gameCopyService;

    @SuppressWarnings("null")
    @Test
    public void testCreateValidOrder() {
        // Arrange
        OrderStatus status = OrderStatus.Bought;
        double totalPrice = 53.2;
        Date date = Date.valueOf(LocalDate.now());

        PurchaseOrder order = new PurchaseOrder(status, totalPrice, date);
        when(repo.save(any(PurchaseOrder.class))).thenReturn(order);

        // Act
        PurchaseOrder createdOrder = service.createOrder(status, null, totalPrice);

        // Assert
        assertNotNull(createdOrder);
        assertEquals(status, createdOrder.getOrderStatus());
        assertEquals(totalPrice, createdOrder.getTotalPrice());
        assertEquals(date, createdOrder.getDate());
        verify(repo, times(1)).save(any(PurchaseOrder.class));
    }

    @Test
    public void testCreateInvalidOrderPrice() {
        // Arrange
        OrderStatus status = OrderStatus.Bought;
        double totalPrice = -3;

        // Act and assert
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            service.createOrder(status, null, totalPrice);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Price cannot be negative.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidOrderStatus() {
        // Arrange
        OrderStatus status = null;
        double totalPrice = 50;

        // Act and assert
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            service.createOrder(status, null, totalPrice);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Status cannot be null.", exception.getMessage());
    }

    @Test
    public void testGetAllOrders() {
        // Arrange
        PurchaseOrder order1 =
                new PurchaseOrder(OrderStatus.Bought, 1234.0, Date.valueOf(LocalDate.now()));
        order1.setOrderId(0);

        PurchaseOrder order2 =
                new PurchaseOrder(OrderStatus.ShoppingCart, 0.99, Date.valueOf(LocalDate.now()));
        order2.setOrderId(1);

        // grouping together to check equality later
        List<PurchaseOrder> orders = Arrays.asList(order1, order2);
        when(repo.findAll()).thenReturn(orders);

        // Act
        List<PurchaseOrder> result = service.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateOrder() {
        // Arrange
        int id = 0;
        OrderStatus oldStatus = OrderStatus.Bought;
        double oldPrice = 1234;
        Date oldDate = Date.valueOf(LocalDate.now());

        PurchaseOrder order = new PurchaseOrder(oldStatus, oldPrice, oldDate);
        order.setOrderId(id);
        when(repo.findByOrderId(id)).thenReturn(order);

        when(repo.findById(id)).thenReturn(Optional.of(order));
        when(repo.save(any(PurchaseOrder.class))).thenReturn(order);

        // new info for updating
        OrderStatus newStatus = OrderStatus.ShoppingCart;
        double newPrice = 4321.99;
        Date newDate = Date.valueOf(LocalDate.now());

        // Act
        PurchaseOrder result = service.updateOrder(id, newStatus, newPrice);
        when(repo.findByOrderId(id)).thenReturn(result);

        // Assert
        assertNotNull(result);
        assertEquals(newPrice, result.getTotalPrice());
        assertEquals(newStatus, result.getOrderStatus());
        assertEquals(newDate, result.getDate());
    }

    @Test
    public void testUpdateInvalidOrderPrice() {
        // Arrange
        int id = 0;
        OrderStatus oldStatus = OrderStatus.Bought;
        double oldPrice = 1234;
        Date oldDate = Date.valueOf(LocalDate.now());

        PurchaseOrder order = new PurchaseOrder(oldStatus, oldPrice, oldDate);
        order.setOrderId(id);
        when(repo.findByOrderId(id)).thenReturn(order);

        // new info for updating
        OrderStatus newStatus = OrderStatus.ShoppingCart;
        double newPrice = -5;

        when(repo.findById(id)).thenReturn(Optional.empty());
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            service.updateOrder(id, newStatus, newPrice);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no order with ID " + id + ".", exception.getMessage());
    }

    @Test
    public void testUpdateInvalidOrderStatus() {
        // Arrange
        int id = 0;
        OrderStatus oldStatus = OrderStatus.Bought;
        double oldPrice = 1234;
        Date oldDate = Date.valueOf(LocalDate.now());

        PurchaseOrder order = new PurchaseOrder(oldStatus, oldPrice, oldDate);
        order.setOrderId(id);
        when(repo.findByOrderId(id)).thenReturn(order);

        // new info for updating
        OrderStatus newStatus = null;
        double newPrice = 2;

        when(repo.findById(id)).thenReturn(Optional.empty());
        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            service.updateOrder(id, newStatus, newPrice);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no order with ID " + id + ".", exception.getMessage());
    }

    @Test
    public void testFindOrderByValidId() {
        // Arrange
        int id = 1;
        PurchaseOrder order =
                new PurchaseOrder(OrderStatus.ShoppingCart, 23.45, Date.valueOf(LocalDate.now()));
        order.setOrderId(id);

        when(repo.findById(id)).thenReturn(Optional.of(order));

        // Act
        PurchaseOrder foundOrder = service.findOrderById(id);

        // Assert
        assertNotNull(foundOrder);
        assertEquals(OrderStatus.ShoppingCart, foundOrder.getOrderStatus());
        assertEquals(23.45, foundOrder.getTotalPrice());
        assertEquals(Date.valueOf(LocalDate.now()), foundOrder.getDate());
    }

    @Test
    public void testFindOrderByInvalidId() {
        // Arrange
        int invalidId = 1;
        when(repo.findById(invalidId)).thenReturn(Optional.empty());

        GameManagerException exception = assertThrows(GameManagerException.class, () -> {
            service.findOrderById(invalidId);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no order with ID " + invalidId + ".", exception.getMessage());
    }

    @Test
    public void testDeleteOrderByValidId() {
        // Arrange
        int id = 1;

        PurchaseOrder order =
                new PurchaseOrder(OrderStatus.ShoppingCart, 23.45, Date.valueOf(LocalDate.now()));
        order.setOrderId(id);

        // Act
        when(repo.existsById(id)).thenReturn(true);
        service.deleteOrder(id);

        // Assert
        verify(repo, times(1)).deleteById(id);
    }

    @Test
    public void testAddGameToCartBy_ValidOrderId_validGameCopies() {
        // Arrange
        int id = 1;
        PurchaseOrder order = new PurchaseOrder(OrderStatus.ShoppingCart, 23.45, Date.valueOf(LocalDate.now()));
        order.setOrderId(id);

        GameCopy gameCopy = new GameCopy();
        int gameCopyId = gameCopy.getGameCopyId();
        ArrayList<Integer> gameCopyIds = new ArrayList<>();
        gameCopyIds.add(gameCopyId);

        when(repo.findByOrderId(id)).thenReturn(order);
        when(repo.save(order)).thenReturn(order);
        when(gameCopyRepository.findGameCopyByGameCopyId(gameCopyId)).thenReturn(gameCopy);

        // Act
        PurchaseOrder updatedPurchaseOrder = service.addGameToCart(id, gameCopyIds);

        // Assert
        assertEquals(updatedPurchaseOrder.getOrderId(), id);
        assertEquals(updatedPurchaseOrder.getGameCopy(0).getGameCopyId(), gameCopyId);
        verify(repo, times(1)).save(order);
    }

    @Test
    public void testAddGameToCartBy_InvalidOrderId_ValidGameCopies() {
        // Arrange
        int InvalidId = 1;
        PurchaseOrder order = new PurchaseOrder(OrderStatus.ShoppingCart, 0.1, Date.valueOf(LocalDate.now()));
        order.setOrderId(InvalidId);

        GameCopy gameCopy = new GameCopy();
        int gameCopyId = gameCopy.getGameCopyId();
        ArrayList<Integer> gameCopyIds = new ArrayList<>();
        gameCopyIds.add(gameCopyId);

        when(repo.findByOrderId(InvalidId)).thenReturn(null);

        // Act and assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.addGameToCart(InvalidId, gameCopyIds));
        assertEquals("Invalid order Id.", e.getMessage());
    }

    @Test
    public void testAddGameToCartBy_ValidOrderId_EmptyGameCopies() {
        // Arrange
        int id = 1;
        PurchaseOrder order = new PurchaseOrder(OrderStatus.ShoppingCart, 0.1, Date.valueOf(LocalDate.now()));
        order.setOrderId(id);

        ArrayList<Integer> gameCopyIds = new ArrayList<>();

        when(repo.findByOrderId(id)).thenReturn(order);
        when(repo.save(order)).thenReturn(order);

        // Act and assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.addGameToCart(id, gameCopyIds));
        assertEquals("Can not add empty Game in cart.", e.getMessage());
    }

    @Test
    public void testAddGameToCartBy_ValidOrderId_InvalidGameCopies() {
        // Arrange
        int InvalidId = 1;
        PurchaseOrder order = new PurchaseOrder(OrderStatus.ShoppingCart, 0.1, Date.valueOf(LocalDate.now()));
        order.setOrderId(InvalidId);

        ArrayList<Integer> gameCopyIds = new ArrayList<>();
        gameCopyIds.add(1);

        when(repo.findByOrderId(InvalidId)).thenReturn(order);
        when(gameCopyRepository.findGameCopyByGameCopyId(1)).thenReturn(null);

        // Act and assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.addGameToCart(InvalidId, gameCopyIds));
        assertEquals("Invalid gameCopy id contained at 0th id.", e.getMessage());
    }

    @Test
    public void testCheckOutBy_ValidOrderId_ValidGameCopies(){
        // Arrange
        int ValidId = 1;
        PurchaseOrder order = new PurchaseOrder(OrderStatus.ShoppingCart, 0.1, Date.valueOf(LocalDate.now()));
        order.setOrderId(ValidId);

        GameCopy gameCopy = new GameCopy();
        order.addGameCopy(gameCopy);

        when(repo.findByOrderId(ValidId)).thenReturn(order);
        when(repo.save(order)).thenReturn(order);

        // Act
        PurchaseOrder updatedPurchaseOrder = service.checkOut(ValidId);

        // Assert
        assertEquals(updatedPurchaseOrder.getOrderId(), ValidId);
        assertEquals(updatedPurchaseOrder.getTotalPrice(), 0.1);
        assertEquals(updatedPurchaseOrder.getOrderStatus(), OrderStatus.Bought);
        verify(repo, times(1)).save(order);
    }

    @Test
    public void testCheckOutBy_InvalidOrderId_ValidGameCopies(){
        // Arrange
        int InvalidId = 1;
        when(repo.findByOrderId(InvalidId)).thenReturn(null);

        // Act and assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.checkOut(InvalidId));
        assertEquals("Invalid order Id.", e.getMessage());
    }

    @Test
    public void testCheckOutBy_ValidOrderId_InvalidGameCopies(){
        // Arrange
        int ValidId = 1;
        PurchaseOrder order = new PurchaseOrder(OrderStatus.ShoppingCart, 0.1, Date.valueOf(LocalDate.now()));
        order.setOrderId(ValidId);

        when(repo.findByOrderId(ValidId)).thenReturn(order);

        // Act and assert
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.checkOut(ValidId));
        assertEquals("Can not check out without game in the order.", e.getMessage());
    }
}