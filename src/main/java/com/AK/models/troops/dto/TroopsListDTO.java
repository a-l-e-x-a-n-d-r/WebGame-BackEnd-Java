package com.AK.models.troops.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TroopsListDTO {

  private List<TroopDTO> troops;

  public TroopsListDTO(
      List<TroopDTO> troops) {
    this.troops = troops;
  }
}
