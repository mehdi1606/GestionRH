package com.innovx.gestionrh.payload.response;

import java.util.List;

public class JwtResponse {
  private String accessToken;
  private String tokenType = "Bearer"; // Fixed field name and initialization
  private Long id;
  private String lastname;
  private String firstname;
  private String username;
  private String email;
  private List<String> userRole; // Changed to use List<String>

  public JwtResponse(String accessToken, Long id, String lastname, String firstname, String email, String role, List<String> userRole) {
    this.accessToken = accessToken;
    this.id = id;
    this.lastname = lastname;
    this.firstname =  firstname; // Construct username from lastname and firstname
    this.email = email;
    this.userRole = userRole;
  }



  // Getters and setters
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<String> getUserRole() {
    return userRole;
  }

  public void setUserRole(List<String> userRole) {
    this.userRole = userRole;
  }
}
