package ca.mcgill.ecse321.gamemanager.dto;

import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder;

import java.util.List;

public class PurchaseOrderRequestDto {
    private PurchaseOrder.OrderStatus orderStatus;
    private List<GameCopyResponseDto> games;
    private double price;

    public PurchaseOrderRequestDto(PurchaseOrder.OrderStatus status, List<GameCopyResponseDto> games, double price) {
        this.orderStatus = status;
        this.games = games;
        this.price = price;
    }

    public PurchaseOrder.OrderStatus getOrderStatus() { return orderStatus; }
    public List<GameCopyResponseDto> getGames() {
        return this.games;
    }
    public double getPrice() {return price;}

}
