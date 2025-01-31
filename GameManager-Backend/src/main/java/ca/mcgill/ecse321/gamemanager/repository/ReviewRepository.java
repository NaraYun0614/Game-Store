package ca.mcgill.ecse321.gamemanager.repository;

import ca.mcgill.ecse321.gamemanager.model.Customer;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository <Review, Integer> {
    public Review findReviewByReviewId(int reviewId);

    public List<Review> findReviewByGame(Game game);

    public List<Review> findReviewByCreated(Customer customer);
}
