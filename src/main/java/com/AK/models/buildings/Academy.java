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
@DiscriminatorValue("academy")
public class Academy extends Building {

  public Academy(Long startedAt, Long finishedAt, Kingdom kingdom)
      throws DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {
    super(null, "academy", 1, getDefaultValueInt("buildingAcademyHP"), startedAt, finishedAt,
        kingdom);
  }

  @Override
  public void levelUp(Long time)
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    super.levelUp(time);
    hp = level * getDefaultValueInt("upgradingAcademyHPLevelN");
    finishedAt = time + ((long) level * getDefaultValueInt("upgradingAcademyTimeToUpgradeLvlN"));
  }
}