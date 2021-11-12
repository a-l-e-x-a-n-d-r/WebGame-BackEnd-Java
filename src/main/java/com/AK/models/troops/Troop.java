package com.AK.models.troops;

import com.AK.models.Kingdom;
import com.AK.models.troops.dto.TroopDTO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Troop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String type;
  private int level;
  private int hp;
  private int attack;
  private int defence;
  private Long startedAt;
  private Long finishedAt;
  private boolean inArmy;

  @ManyToOne
  @JoinColumn(name = "kingdom_id")
  private Kingdom kingdom;

  public TroopDTO toDTO() {
    return new TroopDTO(id, type, level, hp, attack, defence, startedAt, finishedAt);
  }
}