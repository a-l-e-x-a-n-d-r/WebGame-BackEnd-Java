package com.AK.services;

import static com.AK.configuration.DefaultValues.getDefaultValueInt;

import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.exceptions.troopExceptions.InvalidTroopTypeException;
import com.AK.models.Kingdom;
import com.AK.models.troops.Archer;
import com.AK.models.troops.Axeman;
import com.AK.models.troops.Horseman;
import com.AK.models.troops.Pikeman;
import com.AK.models.troops.Swordsman;
import com.AK.models.troops.Troop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopFactory {

  private TimeService timeService;

  @Autowired
  public TroopFactory(TimeService timeService) {
    this.timeService = timeService;
  }

  public Troop createTroop(String type, Kingdom kingdom)
      throws InvalidTroopTypeException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    switch (type) {
      case "archer":
        return new Archer(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("troopArcherTimeToBuild")), kingdom);
      case "swordsman":
        return new Swordsman(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("troopSwordsmanTimeToBuild")), kingdom);
      case "pikeman":
        return new Pikeman(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("troopPikemanTimeToBuild")), kingdom);
      case "axeman":
        return new Axeman(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("troopAxemanTimeToBuild")), kingdom);
      case "horseman":
        return new Horseman(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("troopHorsemanTimeToBuild")), kingdom);
      default:
        throw new InvalidTroopTypeException("Invalid troop type: " + type);
    }
  }
}