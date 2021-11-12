package com.AK.models.buildings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDTO {

  private Long id;
  private String type;
  private Integer level;
  private Integer hp;
  private Long startedAt;
  private Long finishedAt;

  public BuildingDTO(Building building) {
    this.id = building.getId();
    this.type = building.getType();
    this.level = building.getLevel();
    this.hp = building.getHp();
    this.startedAt = building.getStartedAt();
    this.finishedAt = building.getFinishedAt();
  }
}
