/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.gamemanager.model;

import jakarta.persistence.*;
import java.util.*;

// line 72 "model.ump"
// line 136 "model.ump"
@Entity
public class Game
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum GameStatus { Onsale, Available, Archived, PendingApproval }
  public enum RequestStatus { PendingApproval, Approved, PendingArchived, Archived }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int gameId;
  private String title;
  private String description;
  private String genre;
  private double price;
  private int stock;
  private String imageUrl;
  //@Enumerated(EnumType.STRING)
  private GameStatus gameStatus;
  //@Enumerated(EnumType.STRING)
  private RequestStatus requestStatus;
  private int popularity;
  private double averageRating;

  //Game Associations
  @ManyToOne
  @JoinColumn(name = "category", foreignKey = @ForeignKey(name = "CATEGORY_ID_FK"))
  private Category category;

  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews = new ArrayList<>();

  //------------------------
  // CONSTRUCTOR
  //------------------------

  @SuppressWarnings("unused")
  public Game() {
  }
  
  public Game(String aTitle, String aDescription, String aGenre, double aPrice, int aStock, GameStatus aGameStatus, RequestStatus aRequestStatus, Category aCategory, String aImageUrl) {
    title = aTitle;
    description = aDescription;
    genre = aGenre;
    price = aPrice;
    stock = aStock;
    gameStatus = aGameStatus;
    requestStatus = aRequestStatus;
    imageUrl = aImageUrl; // Initialize the imageUrl
    popularity = 0;
    averageRating = 0.0;
    if (!setCategory(aCategory)) {
      throw new RuntimeException("Unable to create Game due to aCategory. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

// New method to add a review
    public void addReview(Review review) {
        if (!reviews.contains(review)) {
            reviews.add(review);
            review.setGame(this); // Set the game in Review as well
        }
    }

    // Getter for reviews
    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }


  public boolean setGameId(int aGameId)
  {
    boolean wasSet = false;
    gameId = aGameId;
    wasSet = true;
    return wasSet;
  }

  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setGenre(String aGenre)
  {
    boolean wasSet = false;
    genre = aGenre;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(double aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setStock(int aStock)
  {
    boolean wasSet = false;
    stock = aStock;
    wasSet = true;
    return wasSet;
  }

  public boolean setGameStatus(GameStatus aGameStatus)
  {
    boolean wasSet = false;
    gameStatus = aGameStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setRequestStatus(RequestStatus aRequestStatus)
  {
    boolean wasSet = false;
    requestStatus = aRequestStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setImageUrl(String aImageUrl) {
    imageUrl = aImageUrl;
    return true;
  }

  public int getGameId()
  {
    return gameId;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public String getGenre()
  {
    return genre;
  }

  public double getPrice()
  {
    return price;
  }

  public int getStock()
  {
    return stock;
  }

  public GameStatus getGameStatus()
  {
    return gameStatus;
  }

  public RequestStatus getRequestStatus()
  {
    return requestStatus;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public int getPopularity() {
    return popularity;
  }

  public double getAverageRating() {
    return averageRating;
  }

  /* Code from template association_GetOne */
  public Category getCategory()
  {
    return category;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCategory(Category aNewCategory)
  {
    boolean wasSet = false;
    if (aNewCategory != null)
    {
      category = aNewCategory;
      wasSet = true;
    }
    return wasSet;
  }

  /**
   * Increments the popularity by 1.
   */
  public void incrementPopularity() {
    popularity++;
  }

  /**
  * Updates the average rating based on a new rating.
  *
  * @param newRating The new rating to consider in average calculation.
  */
 public void updateAverageRating(double newRating) {
   if (newRating < 0 || newRating > 5) {
     throw new IllegalArgumentException("Rating must be between 0 and 5.");
   }
   
   // Calculate the new average
   if (popularity == 0) {
     averageRating = newRating;
   } else {
     averageRating = (averageRating * popularity + newRating) / (popularity + 1);
   }

   incrementPopularity();  // Update popularity since the game was rated
 }

  public void delete()
  {
    category = null;
  }

  @Override
  public String toString() {
    return super.toString() + "[" +
            "gameId" + ":" + getGameId() + "," +
            "title" + ":" + getTitle() + "," +
            "description" + ":" + getDescription() + "," +
            "genre" + ":" + getGenre() + "," +
            "price" + ":" + getPrice() + "," +
            "stock" + ":" + getStock() + "," +
            "popularity" + ":" + getPopularity() + "," +
            "averageRating" + ":" + getAverageRating() + "," +
            "imageUrl" + ":" + getImageUrl() + "]" + 
            System.getProperties().getProperty("line.separator") +
            "  " + "gameStatus" + "=" + (getGameStatus() != null ? getGameStatus() : "null") + 
            System.getProperties().getProperty("line.separator") +
            "  " + "requestStatus" + "=" + (getRequestStatus() != null ? getRequestStatus() : "null") +
            System.getProperties().getProperty("line.separator") +
            "  " + "category = " + (getCategory() != null ? Integer.toHexString(System.identityHashCode(getCategory())) : "null");
  }

  @Override
  public boolean equals(Object other){
    if (!(other instanceof Game)) {
      return false;
    }
    Game that = (Game) other;
    return this.getGameId() == that.getGameId()
            && this.getTitle().equals(that.getTitle())
            && this.getDescription().equals(that.getDescription())
            && this.getGenre().equals(that.getGenre())
            && this.getPrice() == that.getPrice()
            && this.getStock() == that.getStock()
            && this.getPopularity() == that.getPopularity()
            && this.getAverageRating() == that.getAverageRating()
            && this.getCategory() == that.getCategory()
            && this.getRequestStatus() == that.getRequestStatus()
            && this.getGameStatus() == that.getGameStatus()
            && this.getCategory().equals(that.getCategory())
            && this.getImageUrl().equals(that.getImageUrl());

  }

}