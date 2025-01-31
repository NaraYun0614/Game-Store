package ca.mcgill.ecse321.gamemanager.dto;

public class ReviewRequestDto {
    //private int reviewId;
    private int rating;
    private String description;
    private String reviewerEmail;
    private int reviewedGameId;

    public ReviewRequestDto(int rating, String description, String reviewerEmail, int reviewedGameId) {
        this.rating = rating;
        this.description = description;
        this.reviewerEmail = reviewerEmail;
        this.reviewedGameId = reviewedGameId;
    }
    public int getRating() {
        return this.rating;
    }
    public String getDescription() {
        return this.description;
    }
    public String getReviewerEmail() {
        return this.reviewerEmail;
    }
    public int getReviewedGameId() {
        return this.reviewedGameId;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }
    public void setReviewedGameId(int reviewedGameId) {
        this.reviewedGameId = reviewedGameId;
    }


}
