package com.AK.models.buildings;

import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
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
public abstract class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;
  protected String type;
  protected Integer level;
  protected Integer hp;
  protected Long startedAt;
  protected Long finishedAt;

  @ManyToOne
  @JoinColumn(name = "kingdom_id")
  protected Kingdom kingdom;

  public BuildingDTO toDTO() {
    return new BuildingDTO(id, type, level, hp, startedAt, finishedAt);
  }

  public void levelUp(Long time)
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    level += 1;
    startedAt = time;
  }
}
