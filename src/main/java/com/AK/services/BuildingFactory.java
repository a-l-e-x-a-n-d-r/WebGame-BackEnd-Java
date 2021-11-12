package com.AK.services;

import static com.AK.configuration.DefaultValues.getDefaultValueInt;

import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
import com.AK.models.buildings.Academy;
import com.AK.models.buildings.Building;
import com.AK.models.buildings.Farm;
import com.AK.models.buildings.Mine;
import com.AK.models.buildings.TownHall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingFactory {

  private TimeService timeService;

  @Autowired
  public BuildingFactory(TimeService timeService) {
    this.timeService = timeService;
  }

  public Building createBuilding(String newBuildingType, Kingdom kingdom)
      throws BuildingTypeRequiredException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {

    switch (newBuildingType) {
      case "townhall":
        return new TownHall(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("buildingTownHallTimeToBuild")), kingdom);
      case "farm":
        return new Farm(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("buildingFarmTimeToBuild")), kingdom);
      case "mine":
        return new Mine(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("buildingMineTimeToBuild")), kingdom);
      case "academy":
        return new Academy(timeService.getTime(),
            timeService.getTimeAfter(getDefaultValueInt("buildingAcademyTimeToBuild")), kingdom);
      default:
        throw new BuildingTypeRequiredException(
            "Invalid building type.");
    }
  }
}