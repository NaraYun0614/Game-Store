package ca.mcgill.ecse321.gamemanager.service;


import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Customer;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.Review;
import ca.mcgill.ecse321.gamemanager.repository.CustomerRepository;
import ca.mcgill.ecse321.gamemanager.repository.GameRepository;
import ca.mcgill.ecse321.gamemanager.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public Review createReview(int rating, String description, String customerEmail, int gameId) {
        if (rating > 5 || rating < 1) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST,"Review rating out of range");
        }
        if (description.length()>1000) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST,"Review description out of range");
        }
        if (description.isEmpty()) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Review description is empty");
        }
        Date now = Date.valueOf(LocalDate.now());
        Customer customer = customerRepository.findCustomerByEmail(customerEmail);
        Game game = gameRepository.findByGameId(gameId);
        Review review = new Review(rating, description, now, customer, game);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(int id,int rating, String description) {
        Review review = reviewRepository.findReviewByReviewId(id);
        if (review == null) {
            throw new GameManagerException(HttpStatus.NOT_FOUND,"Review not found");
        }
        if (rating > 5 || rating < 1) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST,"Review rating out of range");
        }
        if (description.length()>1000) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST,"Review description out of range");
        }
        if (description.isEmpty()) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST,"Review description is empty");

        }
        Date now = Date.valueOf(LocalDate.now());
        review.setRating(rating);
        review.setDescription(description);
        review.setDate(now);


        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(int id) {
        Review review = reviewRepository.findReviewByReviewId(id);
        if (review == null) {
            throw new GameManagerException(HttpStatus.NOT_FOUND,"Review not found");
        }
        reviewRepository.delete(review);
    }


    @Transactional
    public Review findReviewById(int id) {
        Review review = reviewRepository.findReviewByReviewId(id);
        if (review == null) {
            throw new GameManagerException(HttpStatus.NOT_FOUND,String.format("There is no review with ID %d.", id));
        }
        return review;
    }


    public List<Review> findAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    @Transactional
    public List<Review> findReviewsByCustomerEmail(String customerEmail) {
        Customer customer = customerRepository.findCustomerByEmail(customerEmail);
        List<Review> review = reviewRepository.findReviewByCreated(customer);
        if (review.isEmpty()) {
            throw new GameManagerException(HttpStatus.NOT_FOUND,String.format("There is no review from customer %s.", customerEmail));
        }
        return review;
    }

    @Transactional
    public List<Review> findReviewsByGameId(int gameId) {
        Game game = gameRepository.findByGameId(gameId);

        List<Review> review = reviewRepository.findReviewByGame(game);
        if (review.isEmpty()) {
            throw new GameManagerException(HttpStatus.NOT_FOUND,String.format("There is no review for game %d.", gameId));
        }

        return review;
    }

    @Transactional
    public List<Review> findReviewsByGameIdDescendingRating(int gameId) {
        Game game = gameRepository.findByGameId(gameId);
        List<Review> review = reviewRepository.findReviewByGame(game);
        if (review.isEmpty()) {
            throw new GameManagerException(HttpStatus.NOT_FOUND,String.format("There is no review for game %d.", gameId));
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            //        String.format("There is no review for game %d.", gameId));
        }
        review.sort((r1, r2) -> Integer.compare(r2.getRating(), r1.getRating()));
        return review;
    }


}
