package com.AK.models.troops.dto;

import com.AK.models.troops.Troop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TroopDTO {

  private Long id;
  private String type;
  private int level;
  private int hp;
  private int attack;
  private int defence;
  private Long startedAt;
  private Long finishedAt;

  public TroopDTO(Troop troop) {
    this.id = troop.getId();
    this.type = troop.getType();
    this.level = troop.getLevel();
    this.hp = troop.getHp();
    this.attack = troop.getAttack();
    this.defence = troop.getDefence();
    this.startedAt = troop.getStartedAt();
    this.finishedAt = troop.getFinishedAt();
  }
}