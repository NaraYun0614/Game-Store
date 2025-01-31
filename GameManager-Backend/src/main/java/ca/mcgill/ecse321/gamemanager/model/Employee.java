/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.gamemanager.model;

import java.util.*;
import jakarta.persistence.*;

// line 21 "model.ump"
// line 112 "model.ump"
@Entity
public class Employee extends Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------
  @SuppressWarnings("unused")
  public Employee(){}
  public Employee(String aPassword, String aName, String aEmail)
  {
    super(aPassword, aName, aEmail);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}