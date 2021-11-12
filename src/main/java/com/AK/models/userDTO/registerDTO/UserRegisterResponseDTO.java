package com.AK.models.userDTO.registerDTO;

import com.AK.models.Kingdom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterResponseDTO {

  private Long id;
  private String username;
  private Long kingdomId;
  private String avatar;
  private Integer points;

  public UserRegisterResponseDTO(Long id, String username, Kingdom kingdom) {
    this.id = id;
    this.username = username;
    this.kingdomId = kingdom.getId();
    this.avatar = "Avatar";
    this.points = 0;
  }
}
