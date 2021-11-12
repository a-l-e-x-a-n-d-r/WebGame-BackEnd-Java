package com.AK.models.buildings;

import static com.AK.configuration.DefaultValues.getDefaultValueInt;

import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue("townhall")
public class TownHall extends Building {

  public TownHall(Long startedAt, Long finishedAt, Kingdom kingdom)
      throws DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {
    super(null, "townhall", 1, getDefaultValueInt("buildingTownHallHP"), startedAt, finishedAt,
        kingdom);
  }

  @Override
  public void levelUp(Long time)
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    super.levelUp(time);
    hp = level * getDefaultValueInt("upgradingTownHallHPLevelN");
    finishedAt = time + ((long) level * getDefaultValueInt("upgradingTownHallTimeToUpgradeLvlN"));
  }
}