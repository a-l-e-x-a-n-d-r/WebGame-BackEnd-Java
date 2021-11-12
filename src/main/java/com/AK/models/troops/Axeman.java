package com.AK.models.troops;

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
@Entity
@NoArgsConstructor
@DiscriminatorValue("Axeman")
public class Axeman extends Troop {

  public Axeman(Long startedAt, Long finishAt, Kingdom kingdom)
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    super(null, "Axeman", 1, getDefaultValueInt("troopAxemanHP"),
        getDefaultValueInt("troopAxemanAttack"), getDefaultValueInt("troopAxemanDefence"),
        startedAt, finishAt, false, kingdom);
  }
}