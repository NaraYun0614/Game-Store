package ca.mcgill.ecse321.gamemanager.dto;

import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder.OrderStatus;
import org.springframework.web.bind.annotation.Mapping;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseOrderDto {
    private int orderId;
    private OrderStatus orderStatus;
    private Date date;
    private double price;
    private List<GameCopyResponseDto> gameCopies;

    @SuppressWarnings("unused")
    private PurchaseOrderDto() {}

    public PurchaseOrderDto(int id, OrderStatus status, double price, Date date) {
        this.orderId = id;
        this.orderStatus = status;
        this.price = price;
        this.date = date;
    }

    public PurchaseOrderDto(PurchaseOrder purchaseOrder) {
        this.orderId = purchaseOrder.getOrderId();
        this.orderStatus = purchaseOrder.getOrderStatus();
        this.price = purchaseOrder.getTotalPrice();
        this.date = purchaseOrder.getDate();
        if (purchaseOrder.getGameCopies()!=null) {
            this.gameCopies = purchaseOrder.getGameCopies().stream()
                    .map(GameCopyResponseDto::new) // Assuming a constructor in GameCopyResponseDto
                    .collect(Collectors.toList());
        }
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {return orderStatus; }

    public void setOrderStatus(OrderStatus status) { this.orderStatus = status; }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {return price;}

    public void setPrice(double price) { this.price = price; }

    public List<GameCopyResponseDto> getGameCopies() {return gameCopies;}

    public void setGameCopies(List<GameCopyResponseDto> gameCopies) {
        this.gameCopies = gameCopies;
    }

}
