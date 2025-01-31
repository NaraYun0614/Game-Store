/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.gamemanager.model;


import java.util.*;
import jakarta.persistence.*;


// line 4 "model.ump"
// line 92 "model.ump"
@MappedSuperclass
public abstract class Person
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Person Attributes
  private String password;
  private String name;
  @Id
  @Column(unique = true, nullable = false)
  private String email;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  @SuppressWarnings("unused")
  protected Person(){}
  public Person(String aPassword, String aName, String aEmail)
  {
    password = aPassword;
    name = aName;
    email = aEmail;
    /*if (!setEmail(aEmail))
    {
      throw new RuntimeException("Cannot create due to duplicate email. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }*/
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;

    email = aEmail;

    wasSet = true;

    return wasSet;
  }

  public String getPassword()
  {
    return password;
  }

  public String getName()
  {
    return name;
  }

  public String getEmail()
  {
    return email;
  }


  public void delete()
  {

  }


  public String toString()
  {
    return super.toString() + "["+
            "password" + ":" + getPassword()+ "," +
            "name" + ":" + getName()+ "," +
            "email" + ":" + getEmail()+ "]";
  }
  @Override
  public boolean equals(Object other){
    if (!(other instanceof Person)) {
      return false;
    }
    Person that = (Person) other;
    return  this.password.equals(that.password)
            && this.name.equals(that.name)
            && this.email.equals(that.email);

  }
}