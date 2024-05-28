package com.innovx.gestionrh.security.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovx.gestionrh.Entity.User;
@Getter
public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;
  private Long id;

  private String lastname;

  private String firstname;// This could be the nickname or any non-email identifier.
  private String email;

  private String title;
  private String username;
  @JsonIgnore
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(Long id, String lastname, String firstname, String email, String password,
                         String title, Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;  // username is now the nickname or first name
    this.email = email;
    this.title= title;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(User user) {
    GrantedAuthority authority = new SimpleGrantedAuthority(user.getUserRole().name());
    return new UserDetailsImpl(
            user.getId(),
            user.getLastName(),
            user.getFirstName() ,
            user.getEmail(),
            user.getPassword(),
            user.getTitle(),
            Collections.singletonList(authority));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }



    public String getUserRole() {
    return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
