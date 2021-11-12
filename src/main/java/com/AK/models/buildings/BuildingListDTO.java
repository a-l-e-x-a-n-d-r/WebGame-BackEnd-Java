package com.AK.models.buildings;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuildingListDTO {

  private List<BuildingDTO> buildings;

  public BuildingListDTO(
      List<BuildingDTO> buildings) {
    this.buildings = buildings;
  }
}
