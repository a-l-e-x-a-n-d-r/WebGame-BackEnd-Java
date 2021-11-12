package com.AK.models.buildings;

import static com.AK.configuration.DefaultValues.getDefaultValueInt;

import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("mine")
public class Mine extends Building {

  public Mine(Long startedAt, Long finishedAt, Kingdom kingdom)
      throws DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {
    super(null, "mine", 1, getDefaultValueInt("buildingMineHP"), startedAt, finishedAt, kingdom);
  }

  @Override
  public void levelUp(Long time)
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    super.levelUp(time);
    hp = level * getDefaultValueInt("upgradingMineHPLevelN");
    finishedAt = time + ((long) level * getDefaultValueInt("upgradingMineTimeToUpgradeLvlN"));
  }
}
