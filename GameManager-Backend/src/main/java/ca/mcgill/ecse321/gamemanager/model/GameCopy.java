/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.gamemanager.model;

import jakarta.persistence.*;

// line 33 "model.ump"
// line 130 "model.ump"
@Entity
public class GameCopy
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameCopy Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int gameCopyId;

  //GameCopy Associations
  @ManyToOne
  @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "GAME_ID_FK"))
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public GameCopy() {}

  public GameCopy(Game aGame)
  {
    if (!setGame(aGame))
    {
      throw new RuntimeException("Unable to create GameCopy due to aGame of null type.");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getGameCopyId()
  {
    return gameCopyId;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
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
    game = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "gameCopyId" + ":" + getGameCopyId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }
}
