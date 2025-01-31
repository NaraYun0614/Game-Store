package ca.mcgill.ecse321.gamemanager.service;


import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Customer;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.Review;
import ca.mcgill.ecse321.gamemanager.repository.CustomerRepository;
import ca.mcgill.ecse321.gamemanager.repository.GameRepository;
import ca.mcgill.ecse321.gamemanager.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class ReviewServiceTests {

    @Mock
    private ReviewRepository mockReviewRepository;

    @Mock
    private GameRepository mockGameRepository;

    @Mock
    private CustomerRepository mockCustomerRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    @AfterEach
    public void deleteDb(){
        mockGameRepository.deleteAll();
        mockCustomerRepository.deleteAll();
        mockReviewRepository.deleteAll();
    }

    private static final int VALID_RATING = 5;
    private static final int INVALID_LOW_RATING = -1;
    private static final int INVALID_HIGH_RATING = 6;

    private static final String INVALID_EMPTY_COMMENT  = "";
    private static final String VALID_COMMENT = "The game is excellent";
    private static final String INVALID_LONG_COMMENT = "This is a comment designed to be slightly longer than 1000 characters. It includes some filler text to reach the necessary length and demonstrate how one might handle comments of this size within the constraints of the Steam platform. When you write comments on a game, it's easy to get carried away describing the features, graphics, storyline, and gameplay mechanics. For instance, let's talk about the combat system—it's fluid and dynamic, allowing for seamless transitions between moves, and the AI opponents are challenging without feeling unfair. Additionally, the graphics are stunning, with high-resolution textures and beautiful landscapes that make exploration enjoyable. The developers have done a fantastic job in creating an immersive world that keeps you engaged for hours. As you progress, you can unlock new abilities and upgrades, which add depth and replay value. Not to mention, the storyline is compelling and filled with unexpected twists and turns, making you eager to see what happens next. Overall, this game is a masterclass in design and storytelling, and I highly recommend it. I'm excited to see how the game evolves with future updates, as the potential for even more content is enormous. Let's keep supporting such dedicated developers!\n";

    @Test
    public void testAndCreateValidReview() {
        int gameId = 1;
        String customerEmail = "example1@example.com";
        String password = "123456";
        Customer customer = new Customer(password,"John",customerEmail);
        Game game = new Game();
        game.setGameId(gameId);
        Date date = Date.valueOf(LocalDate.now());


        Review review = new Review(VALID_RATING, VALID_COMMENT,date, customer, game );
        when(mockReviewRepository.save(any(Review.class))).thenReturn(review);
        when(mockGameRepository.findByGameId(any(int.class))).thenReturn(game);
        when(mockCustomerRepository.findCustomerByEmail(any(String.class))).thenReturn(customer);


        Review createdReview = reviewService.createReview(VALID_RATING,VALID_COMMENT,customerEmail,gameId);

        assertNotNull(createdReview);
        assertEquals(VALID_RATING, createdReview.getRating());
        assertEquals(VALID_COMMENT,createdReview.getDescription());
        assertEquals(gameId, createdReview.getGame().getGameId());
        assertEquals(customerEmail,createdReview.getCreated().getEmail());

        verify(mockReviewRepository, times(1)).save(createdReview);
    }
    @Test
    public void testAndCreateInvalidReviewWithEmptyComment() {
        int gameId = 1;
        String customerEmail = "example@example.com";

        GameManagerException e = assertThrows(GameManagerException.class, () -> reviewService.createReview(VALID_RATING, INVALID_EMPTY_COMMENT, customerEmail, gameId));
        //  IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.findPersonById(invalidId));

        assertEquals("Review description is empty", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());

    }

    @Test
    public void testAndCreateInvalidReviewWithLongComment() {
        int gameId = 1;
        String customerEmail = "example@example.com";

        GameManagerException e = assertThrows(GameManagerException.class, () -> reviewService.createReview(VALID_RATING, INVALID_LONG_COMMENT, customerEmail, gameId));
        assertEquals("Review description out of range", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void testAndCreateValidReviewWithLowRating() {
        int gameId = 1;
        String customerEmail = "example@example.com";

        GameManagerException e = assertThrows(GameManagerException.class, () -> reviewService.createReview(INVALID_LOW_RATING, VALID_COMMENT, customerEmail, gameId));
        assertEquals("Review rating out of range", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());
    }

    @Test
    public void testAndCreateValidReviewWithHighRating() {
        int gameId = 1;
        String customerEmail = "example@example.com";
        GameManagerException e = assertThrows(GameManagerException.class, () -> reviewService.createReview(INVALID_HIGH_RATING, VALID_COMMENT, customerEmail, gameId));
        assertEquals("Review rating out of range", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void testGetReviewByInvalidReviewId() {
        int reviewId = 11;
        when(mockReviewRepository.findReviewByReviewId(reviewId)).thenReturn(null);
        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.findReviewById(reviewId));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("There is no review with ID 11.", ex.getMessage());

    }
    @Test
    public void testGetReviewByValidReviewId() {
        int reviewId = 11;
        int gameId = 1;
        String customerEmail = "example2@example.com";
        String password = "123456";
        Customer customer = new Customer(password,"John",customerEmail);
        Game game = new Game();
        customer.setEmail(customerEmail);
        game.setGameId(gameId);
        Date date = Date.valueOf(LocalDate.now());


        Review review = new Review(VALID_RATING, VALID_COMMENT,date, customer, game );
        when(mockReviewRepository.findReviewByReviewId(reviewId)).thenReturn(review);


        Review gotReview = reviewService.findReviewById(reviewId);

        assertNotNull(gotReview);
        assertEquals(VALID_RATING, gotReview.getRating());
        assertEquals(VALID_COMMENT,gotReview.getDescription());
        assertEquals(gameId, gotReview.getGame().getGameId());
        assertEquals(customerEmail,gotReview.getCreated().getEmail());

        verify(mockReviewRepository, times(1)).findReviewByReviewId(reviewId);

    }

    @Test
    public void testDeleteReviewByValidReviewId(){
        int reviewId = 11;
        int gameId = 1;
        String customerEmail = "example@example.com";
        Customer customer = new Customer();
        Game game = new Game();
        customer.setEmail(customerEmail);
        game.setGameId(gameId);
        Date date = Date.valueOf(LocalDate.now());
        Review review = new Review(VALID_RATING, VALID_COMMENT,date, customer, game );
        when(mockReviewRepository.findReviewByReviewId(any(int.class))).thenReturn(review);


        reviewService.deleteReview(reviewId);
        verify(mockReviewRepository, times(1)).delete(review);
    }

    @Test
    public void testDeleteReviewByInvalidCustomerId() {
        int reviewId = 11;
        when(mockReviewRepository.findReviewByReviewId(any(int.class))).thenReturn(null);
        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.deleteReview(reviewId));

        assertEquals("Review not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND,ex.getStatus());

    }

    @Test
    public void testUpdateReviewWithEverythingValid(){
        int reviewId = 11;
        int gameId = 1;
        String customerEmail = "example@example.com";
        Customer customer = new Customer();
        Game game = new Game();
        customer.setEmail(customerEmail);
        game.setGameId(gameId);
        Date date = Date.valueOf(LocalDate.now());

        Review review = new Review(VALID_RATING, VALID_COMMENT,date, customer, game );
        when(mockReviewRepository.findReviewByReviewId(reviewId)).thenReturn(review);

        int updatedRating = 1;
        String updatedDescription = "The game is awful";
        Review review2 = new Review(updatedRating, updatedDescription, date, customer, game );
        when(mockReviewRepository.save(any(Review.class))).thenReturn(review2);

        Review updatedReview = reviewService.updateReview(reviewId, updatedRating, updatedDescription);


        assertNotNull(updatedReview);
        assertEquals(updatedRating, updatedReview.getRating());
        assertEquals(updatedDescription, updatedReview.getDescription());
        assertEquals(Date.valueOf(LocalDate.now()), updatedReview.getDate());
        verify(mockReviewRepository, times(1)).findReviewByReviewId(reviewId);
    }

    @Test
    public void testUpdateReviewWithInvalidReviewId(){
        int reviewId = 11;
        int validRating = 1;
        String validDescription = "The game is awful";

        when(mockReviewRepository.findReviewByReviewId(any(int.class))).thenReturn(null);
        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.updateReview(reviewId,validRating,validDescription));
        assertEquals("Review not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND,ex.getStatus());
    }

    @Test
    public void testUpdateReviewWithInvalidEmptyDescription(){
        int reviewId = 11;
        int gameId = 1;
        String customerEmail = "example@example.com";
        Customer customer = new Customer();
        Game game = new Game();
        customer.setEmail(customerEmail);
        game.setGameId(gameId);
        Date date = Date.valueOf(LocalDate.now());
        Review review = new Review(VALID_RATING, VALID_COMMENT,date, customer, game );

        when(mockReviewRepository.findReviewByReviewId(any(int.class))).thenReturn(review);

        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.updateReview(reviewId,VALID_RATING,INVALID_EMPTY_COMMENT));
        assertEquals("Review description is empty", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,ex.getStatus());
    }

    @Test
    public void testUpdateReviewWithInvalidLongDescription(){
        int reviewId = 11;
        int gameId = 1;
        String customerEmail = "example@example.com";
        Customer customer = new Customer();
        Game game = new Game();
        customer.setEmail(customerEmail);
        game.setGameId(gameId);
        Date date = Date.valueOf(LocalDate.now());
        Review review = new Review(VALID_RATING, VALID_COMMENT,date, customer, game );

        when(mockReviewRepository.findReviewByReviewId(any(int.class))).thenReturn(review);

        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.updateReview(reviewId,VALID_RATING,INVALID_LONG_COMMENT));
        assertEquals("Review description out of range", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,ex.getStatus());

    }

    @Test
    public void testUpdateReviewWithLowRating(){
        int reviewId = 11;
        int gameId = 1;
        String customerEmail = "example@example.com";
        Customer customer = new Customer();
        Game game = new Game();
        customer.setEmail(customerEmail);
        game.setGameId(gameId);
        Date date = Date.valueOf(LocalDate.now());
        Review review = new Review(VALID_RATING, VALID_COMMENT,date, customer, game );


        when(mockReviewRepository.findReviewByReviewId(any(int.class))).thenReturn(review);

        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.updateReview(reviewId,INVALID_LOW_RATING,VALID_COMMENT));
        assertEquals("Review rating out of range", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,ex.getStatus());

    }
    @Test
    public void testUpdateReviewWithHighRating(){

        int reviewId = 11;
        int gameId = 1;
        String customerEmail = "example@example.com";
        Customer customer = new Customer();
        Game game = new Game();
        customer.setEmail(customerEmail);
        game.setGameId(gameId);
        Date date = Date.valueOf(LocalDate.now());
        Review review = new Review(VALID_RATING, VALID_COMMENT,date, customer, game );


        when(mockReviewRepository.findReviewByReviewId(any(int.class))).thenReturn(review);

        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.updateReview(reviewId,INVALID_HIGH_RATING,VALID_COMMENT));
        assertEquals("Review rating out of range", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());

    }

    @Test
    public void testFindAllReviews(){
        Game game = new Game();
        Customer customer = new Customer();
        Review review1 = new Review(1,"1",Date.valueOf(LocalDate.now()),customer, game );
        Review review2 = new Review(2,"2",Date.valueOf(LocalDate.now()),customer, game );
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);

        when(mockReviewRepository.findAll()).thenReturn(reviewList);
        List<Review> gotReviewList = reviewService.findAllReviews();
        assertNotNull(gotReviewList);
        assertEquals(2, gotReviewList.size());
        assertEquals(review1, gotReviewList.get(0));
        assertEquals(review2, gotReviewList.get(1));
        assertTrue(reviewList.contains(review1));
        assertTrue(reviewList.contains(review2));

    }

    @Test
    public void testFindReviewByCustomerEmailSuccessfully(){
        Game game = new Game();
        //Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        //customer1.setEmail("example1@example.com");
        customer2.setEmail("example@example.com");
        //Review Review1 = new Review(1,"1",Date.valueOf(LocalDate.now()),customer1, game );
        Review Review2 = new Review(2,"2",Date.valueOf(LocalDate.now()),customer2, game );
        Review Review3 = new Review(3,"3",Date.valueOf(LocalDate.now()),customer2, game );
        List<Review> reviewList = new ArrayList<>();
        //reviewList.add(Review1);
        reviewList.add(Review2);
        reviewList.add(Review3);

        when(mockCustomerRepository.findCustomerByEmail(any(String.class))).thenReturn(customer2);
        when(mockReviewRepository.findReviewByCreated(any(Customer.class))).thenReturn(reviewList);
        //List<Review> gotReviewList = reviewService.findReviewsByCustomerEmail(customer2.getEmail());
        List<Review> gotReviewList = reviewService.findReviewsByCustomerEmail("example@example.com");
        assertNotNull(gotReviewList);
        assertEquals(2, gotReviewList.size());
        assertEquals(Review2, gotReviewList.get(0));
        assertEquals(Review3, gotReviewList.get(1));
        assertTrue(reviewList.contains(Review2));
        assertTrue(reviewList.contains(Review3));
    }

    @Test
    public void testFindReviewByCustomerEmailUnsuccessfully(){
        String customerEmail = "example@example.com";
        Customer customer = new Customer();
        List<Review> listReview= new ArrayList<>();

        when(mockCustomerRepository.findCustomerByEmail(any(String.class))).thenReturn(customer);
        when(mockReviewRepository.findReviewByCreated(any(Customer.class))).thenReturn(listReview);

        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.findReviewsByCustomerEmail(customerEmail));
        assertEquals("There is no review from customer example@example.com.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());

    }

    @Test
    public void testFindReviewByGameIdSuccessfully(){
        Customer customer = new Customer();
        Game game = new Game();
        game.setGameId(1);

        //Review Review1 = new Review(1,"1",Date.valueOf(LocalDate.now()),customer, game);
        Review Review2 = new Review(2,"2",Date.valueOf(LocalDate.now()),customer, game);
        Review Review3 = new Review(3,"3",Date.valueOf(LocalDate.now()),customer, game);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(Review2);
        reviewList.add(Review3);

        when(mockGameRepository.findByGameId(any(int.class))).thenReturn(game);
        when(mockReviewRepository.findReviewByGame(any(Game.class))).thenReturn(reviewList);
        List<Review> gotReviewList = reviewService.findReviewsByGameId(1);
        assertNotNull(gotReviewList);
        assertEquals(2, gotReviewList.size());
        assertEquals(Review2, gotReviewList.get(0));
        assertEquals(Review3, gotReviewList.get(1));
        assertTrue(reviewList.contains(Review2));
        assertTrue(reviewList.contains(Review3));

    }

    @Test
    public void testFindReviewByGameIdUnsuccessfully(){
        int gameId = 1;
        Game game = new Game();

        when(mockGameRepository.findByGameId(any(int.class))).thenReturn(game);
        when(mockReviewRepository.findReviewByCreated(any(Customer.class))).thenReturn(null);

        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.findReviewsByGameId(gameId));
        assertEquals("There is no review for game 1.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testFindReviewByGameIdDescendingSuccessfully(){
        Customer customer = new Customer();
        Game game = new Game();
        game.setGameId(1);

        //Review Review1 = new Review(1,"1",Date.valueOf(LocalDate.now()),customer, game);
        Review Review2 = new Review(2,"2",Date.valueOf(LocalDate.now()),customer, game);
        Review Review3 = new Review(3,"3",Date.valueOf(LocalDate.now()),customer, game);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(Review2);
        reviewList.add(Review3);

        when(mockGameRepository.findByGameId(any(int.class))).thenReturn(game);
        when(mockReviewRepository.findReviewByGame(any(Game.class))).thenReturn(reviewList);
        List<Review> gotReviewList = reviewService.findReviewsByGameIdDescendingRating(1);
        assertNotNull(gotReviewList);
        assertEquals(2, gotReviewList.size());
        assertEquals(Review3, gotReviewList.get(0));
        assertEquals(Review2, gotReviewList.get(1));
        assertTrue(reviewList.contains(Review2));
        assertTrue(reviewList.contains(Review3));
    }

    @Test
    public void testFindReviewByGameIdDescendingUnsuccessfully(){
        int gameId = 1;
        Game game = new Game();

        when(mockGameRepository.findByGameId(any(int.class))).thenReturn(game);
        when(mockReviewRepository.findReviewByCreated(any(Customer.class))).thenReturn(null);

        GameManagerException ex = assertThrows(GameManagerException.class, () -> reviewService.findReviewsByGameIdDescendingRating(gameId));
        assertEquals("There is no review for game 1.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

}
