package com.innovx.gestionrh.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    // Getters and setters
    private String accessToken;
  private String tokenType = "Bearer"; // Fixed field name and initialization
  private Long id;
  private String lastname;
  private String firstname;
  private String email;
  private String title;
  private List<String> userRole; // Changed to use List<String>

  public JwtResponse(String accessToken, Long id, String lastname, String firstname, String email, String title, List<String> userRole) {
    this.accessToken = accessToken;
    this.id = id;
    this.lastname = lastname;
    this.firstname =  firstname; // Construct username from lastname and firstname
    this.email = email;
    this.title = title;
    this.userRole = userRole;

  }


}
