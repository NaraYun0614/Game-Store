package ca.mcgill.ecse321.gamemanager.dto;
import ca.mcgill.ecse321.gamemanager.model.Customer;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import ca.mcgill.ecse321.gamemanager.model.PurchaseOrder;

import java.util.ArrayList;
import java.util.List;

//for request

public class CustomerResponseDto {
    private String name;
    private String email;
    private String password;
    private List<PurchaseOrder> purchaseOrders;
    private List<GameCopy> inWishlist;
    private List<GameCopy> inCart;

    public CustomerResponseDto() {}

    public CustomerResponseDto(Customer customer) {
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.password = customer.getPassword() ;
        purchaseOrders = customer.getPurchaseOrders();
        inWishlist = customer.getInWishlist();
        inCart = customer.getInCart();

    }

    public String getName() {
        return name;
    }

    /*public void setName(String name) {
        this.name = name;
    }*/

    public String getEmail() {
        return email;
    }

    /*public void setEmail(String email) {
        this.email = email;
    }*/

    public String getPassword() {return password;}

   /* public void setPassword(String password) {this.password = password;}*/

    public List<PurchaseOrder> getPurchaseOrders() {return purchaseOrders;}
    public List<GameCopy> getInWishlist() {return inWishlist;}
    public List<GameCopy> getInCart() {return inCart;}

}
