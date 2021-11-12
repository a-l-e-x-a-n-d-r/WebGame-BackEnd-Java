package com.AK.services;

import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
import com.AK.models.buildings.Building;
import com.AK.repositories.BuildingRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BuildingStarterPackServiceImpl implements BuildingStarterPackService{

  private final BuildingFactory buildingFactory;
  private final TimeService timeService;
  private final BuildingRepository buildingRepository;

  public BuildingStarterPackServiceImpl(
      BuildingFactory buildingFactory,
      TimeService timeService,
      BuildingRepository buildingRepository) {
    this.buildingFactory = buildingFactory;
    this.timeService = timeService;
    this.buildingRepository = buildingRepository;
  }

  @Override
  public void setBuildingStarterPack(Kingdom kingdom)
      throws BuildingTypeRequiredException, KingdomNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    if (kingdom == null) {
      throw new KingdomNotFoundException("Kingdom is not found.");
    }

    Building townhall = buildingFactory.createBuilding("townhall", kingdom);
    townhall.setFinishedAt(timeService.getTime());
    buildingRepository.save(townhall);
    Building farm = buildingFactory.createBuilding("farm", kingdom);
    farm.setFinishedAt(timeService.getTime());
    buildingRepository.save(farm);
    Building mine = buildingFactory.createBuilding("mine", kingdom);
    mine.setFinishedAt(timeService.getTime());
    buildingRepository.save(mine);
    Building academy = buildingFactory.createBuilding("academy", kingdom);
    academy.setFinishedAt(timeService.getTime());
    buildingRepository.save(academy);

    List<Building> buildingList = new ArrayList<>(List.of(townhall, farm, mine, academy));
    kingdom.setBuildings(buildingList);
  }
}
