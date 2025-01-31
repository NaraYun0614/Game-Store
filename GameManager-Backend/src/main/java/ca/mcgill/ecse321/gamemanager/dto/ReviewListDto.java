package ca.mcgill.ecse321.gamemanager.dto;
import ca.mcgill.ecse321.gamemanager.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewListDto {

    private List<ReviewResponseDto> reviews;

    public ReviewListDto() {
        this.reviews = new ArrayList<>();
    }

    public ReviewListDto(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }
    public List<ReviewResponseDto> getReviews() {
        return reviews;
    }
    public void setReviews(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }


}
