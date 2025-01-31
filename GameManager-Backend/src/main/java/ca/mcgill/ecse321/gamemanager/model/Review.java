/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.gamemanager.model;
import jakarta.persistence.*;
import java.sql.Date;

// line 49 "model.ump"
// line 125 "model.ump"
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "game_id"}))
public class Review
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Review Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int reviewId;
  private int rating;
  private String description;
  private Date date;

  //Review Associations
  @ManyToOne
  @JoinColumn(
          name = "rGame_id",
          foreignKey = @ForeignKey(name = "GAME_ID_FK")
  )
  private Game game;
  @ManyToOne
  @JoinColumn(
          name = "rCustomer_id",
          foreignKey = @ForeignKey(name = "CUSTOMER_ID_FK")
  )
  private Customer created;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  @SuppressWarnings("unused")
  public Review(){}
  public Review(int aRating, String aDescription, Date aDate, Customer aCreated, Game aGame)
  {
    rating = aRating;
    description = aDescription;
    date = aDate;
    if (!setCreated(aCreated))
    {
      throw new RuntimeException("Unable to create Review due to aCreated. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setGame(aGame))
    {
      throw new RuntimeException("Unable to create Review due to aGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setReviewId(int aReviewId)
  {
    boolean wasSet = false;
    reviewId = aReviewId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRating(int aRating)
  {
    boolean wasSet = false;
    rating = aRating;
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

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public int getReviewId()
  {
    return reviewId;
  }

  public int getRating()
  {
    return rating;
  }

  public String getDescription()
  {
    return description;
  }

  public Date getDate()
  {
    return date;
  }
  /* Code from template association_GetOne */
  public Customer getCreated()
  {
    return created;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCreated(Customer aNewCreated)
  {
    boolean wasSet = false;
    if (aNewCreated != null)
    {
      created = aNewCreated;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setGame(Game aNewGame)
  {
    boolean wasSet = false;
    if (aNewGame != null)
    {
      game = aNewGame;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    created = null;
    game = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "reviewId" + ":" + getReviewId()+ "," +
            "rating" + ":" + getRating()+ "," +
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "created = "+(getCreated()!=null?Integer.toHexString(System.identityHashCode(getCreated())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }

  @Override
  public boolean equals(Object other){
    if (!(other instanceof Review)) {
      return false;
    }
    Review that = (Review) other;
    return this.reviewId == that.reviewId
            && this.rating == that.rating
            && this.description.equals(that.description)
            && this.date.equals(that.date);
            //&& this.created.equals(that.created)
            //&& this.game.equals(that.game);
  }

}

