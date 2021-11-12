package com.AK.models;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private final String username;
  private final String password;
  private final Long id;
  private final Kingdom kingdom;

  public CustomUserDetails(User user) {
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.id = user.getId();
    this.kingdom = user.getKingdom();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public Long getId() {
    return id;
  }

  public String getKingdomName() {
    return kingdom.getName();
  }

  public Long getKingdomId() {
    return kingdom.getId();
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
}
