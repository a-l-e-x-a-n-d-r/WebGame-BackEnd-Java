package com.AK.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String avatar;
  private Integer points;
  private String email;

  @OneToOne
  @JoinColumn(name = "kingdom_id", referencedColumnName = "id")
  private Kingdom kingdom;

  public User(String username, String password, Kingdom kingdom) {
    this.username = username;
    this.password = password;
    this.kingdom = kingdom;
  }
}
