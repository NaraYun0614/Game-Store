package ca.mcgill.ecse321.gamemanager.dto;

import java.util.List;
import java.util.stream.Collectors;


import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.Category;
import ca.mcgill.ecse321.gamemanager.model.Game.GameStatus;
import ca.mcgill.ecse321.gamemanager.model.Game.RequestStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class GameDto {
    private int gameId;
    private String title;
    private String description;
    private String genre;
    private double price;
    private int stock;
    private int popularity;
    private double averageRating;
    private List<ReviewDto> reviews;
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    private int categoryId;
    private String categoryName;
    private String categoryDescription;
    private String imageUrl;

    // Default constructor
    public GameDto() {
    }

    // Constructor with all attributes
    public GameDto(
            int gameId,
            String title,
            String description,
            String genre,
            double price,
            int stock,
            int popularity,
            double averageRating,
            List<ReviewDto> reviews,
            GameStatus gameStatus,
            RequestStatus requestStatus,
            int categoryId,
            String categoryName,
            String categoryDescription,
            String imageUrl
    ) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.price = price;
        this.stock = stock;
        this.popularity = popularity;
        this.averageRating = averageRating;
        this.reviews = reviews;
        this.gameStatus = gameStatus;
        this.requestStatus = requestStatus;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.imageUrl = imageUrl;
    }

    // Constructor for Game entity
    public GameDto(Game game) {
        this.gameId = game.getGameId();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.genre = game.getGenre();
        this.price = game.getPrice();
        this.stock = game.getStock();
        this.popularity = game.getPopularity();
        this.averageRating = game.getAverageRating();
        this.reviews = game.getReviews().stream().map(ReviewDto::new).collect(Collectors.toList());
        this.gameStatus = game.getGameStatus();
        this.requestStatus = game.getRequestStatus();
        this.categoryId = game.getCategory() != null ? game.getCategory().getCategoryId() : 0;
        this.categoryName = game.getCategory() != null ? game.getCategory().getName() : null;
        this.categoryDescription = game.getCategory() != null ? game.getCategory().getDescription() : null;
        this.imageUrl = game.getImageUrl();
    }

    public GameDto(int i, String validTitle, String validDescription, String validGenre, double validPrice, int validStock, int i1, double v, Object o) {
    }

    // Getters and setters for all attributes
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "GameDto{" +
                "gameId=" + gameId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", popularity=" + popularity +
                ", averageRating=" + averageRating +
                ", reviews=" + reviews +
                ", gameStatus=" + gameStatus +
                ", requestStatus=" + requestStatus +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
