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
@DiscriminatorValue("Archer")
public class Archer extends Troop {

  public Archer(Long startedAt, Long finishAt, Kingdom kingdom)
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    super(null, "Archer", 1, getDefaultValueInt("troopArcherHP"),
        getDefaultValueInt("troopArcherAttack"), getDefaultValueInt("troopArcherDefence"),
        startedAt, finishAt, false, kingdom);
  }
}
