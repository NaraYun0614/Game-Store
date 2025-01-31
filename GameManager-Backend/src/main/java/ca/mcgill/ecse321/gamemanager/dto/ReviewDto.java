package ca.mcgill.ecse321.gamemanager.dto;

import java.sql.Date;

import ca.mcgill.ecse321.gamemanager.model.Review;

public class ReviewDto {

    private int reviewId;
    private String content;
    private int rating;
    private Date date;
    private int gameId;
    private String customerName;

    // Constructors
    public ReviewDto(Review review) {
        this.reviewId = review.getReviewId();
        this.content = review.getDescription();
        this.rating = review.getRating();
        this.date = review.getDate();
        this.gameId = review.getGame().getGameId();
        this.customerName = review.getCreated().getName();
    }

    public ReviewDto(int reviewId, String content, int rating, Date date, int gameId, String customerName) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.date = date;
        this.gameId = gameId;
        this.customerName = customerName;
    }

    public ReviewDto(int reviewId, String content, int rating) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
    }

    // Getters
    public int getReviewId() {
        return reviewId;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }

    public Date getDate() {
        return date;
    }

    public int getGameId() {
        return gameId;
    }

    public String getCustomerName() {
        return customerName;
    }

    // Setters (optional, only if necessary)
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
