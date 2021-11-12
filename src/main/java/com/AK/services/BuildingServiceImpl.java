package com.AK.services;

import com.AK.exceptions.BuildingDoesNotBelongToKingdomException;
import com.AK.exceptions.BuildingIsAlreadyOnMaxLevelException;
import com.AK.exceptions.BuildingIsNotFinishedException;
import com.AK.exceptions.InvalidBuildingException;
import com.AK.exceptions.BuildingNotFoundException;
import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.KingdomAlreadyHasTownhallException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.TownHallNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
import com.AK.models.buildings.Building;
import com.AK.models.buildings.BuildingDTO;
import com.AK.models.buildings.BuildingListDTO;
import com.AK.models.buildings.CreateBuildingDTO;
import com.AK.repositories.BuildingRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {

  private BuildingRepository buildingRepository;
  private KingdomService kingdomService;
  private BuildingFactory buildingFactory;
  private TimeService timeService;
  private PurchaseService purchaseService;

  @Autowired
  public BuildingServiceImpl(
      BuildingRepository buildingRepository,
      KingdomService kingdomService,
      BuildingFactory buildingFactory,
      TimeService timeService,
      PurchaseService purchaseService) {
    this.buildingRepository = buildingRepository;
    this.kingdomService = kingdomService;
    this.buildingFactory = buildingFactory;
    this.timeService = timeService;
    this.purchaseService = purchaseService;
  }

  @Override
  public Building createBuilding(CreateBuildingDTO buildingDTO, Long kingdomId)
      throws BuildingTypeRequiredException, TownHallNotFoundException, IdRequiredException, KingdomNotFoundException, KingdomAlreadyHasTownhallException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {

    Kingdom kingdom = kingdomService.getKingdomById(kingdomId);
    Optional<Building> optionalBuilding = kingdom.getBuildings().stream()
        .filter(b -> b.getType().equals("townhall"))
        .findFirst();

    if (buildingDTO == null || buildingDTO.getType() == null || buildingDTO.getType().isEmpty()) {
      throw new BuildingTypeRequiredException("Missing parameter(s): type.");
    } else if (buildingDTO.getType().equals("townhall") && optionalBuilding.isPresent()) {
      throw new KingdomAlreadyHasTownhallException("This kingdom already has a townhall.");
    } else if (buildingDTO.getType().equals("townhall")) {
      Building building = buildingFactory.createBuilding(buildingDTO.getType(), kingdom);
      kingdom.addBuilding(building);
      buildingRepository.save(building);
      return building;
    } else if (optionalBuilding.isEmpty()) {
      throw new TownHallNotFoundException(
          "This kingdom doesn't have a Townhall.");
    }

    Building building = buildingFactory.createBuilding(buildingDTO.getType(), kingdom);
    kingdom.addBuilding(building);
    buildingRepository.save(building);
    return building;
  }

  @Override
  public BuildingListDTO getKingdomBuildings(Kingdom kingdom) {
    List<Building> buildings = buildingRepository.findAllByKingdom(kingdom);
    List<BuildingDTO> buildingDTOS = buildings.stream().map(Building::toDTO)
        .collect(Collectors.toList());
    return new BuildingListDTO(buildingDTOS);
  }

  @Override
  public Building getBuildingById(Long id)
      throws IdRequiredException, BuildingNotFoundException {
    if (id == null) {
      throw new IdRequiredException("Id is required.");
    }
    Optional<Building> building = buildingRepository.findById(id);
    if (building.isEmpty()) {
      throw new BuildingNotFoundException("Building not found.");
    } else {
      return building.get();
    }
  }

  @Override
  public void validateBuildingOwner(Long kingdomId, Building building)
      throws InvalidBuildingException {
    if (!kingdomId.equals(building.getKingdom().getId())) {
      throw new InvalidBuildingException("Forbidden Action. Building doesnt belong to user.");
    }
  }

  @Override
  public Building retrieveBuildingFromKingdom(Long kingdomId, Long buildingId)
      throws IdRequiredException, BuildingNotFoundException, InvalidBuildingException {

    Building building = getBuildingById(buildingId);
    validateBuildingOwner(kingdomId, building);

    return building;
  }

  @Override
  public boolean isAcademyPresent(Long buildingId) {
    var optionalAcademy = buildingRepository.findById(buildingId);
    return optionalAcademy.isPresent();
  }

  @Override
  public boolean isBuildingAcademy(Long buildingId)
      throws IdRequiredException, BuildingNotFoundException {
    return getBuildingById(buildingId).getType().equals("academy");
  }

  @Override
  public Building upgradeBuilding(Long buildingId, Long kingdomId)
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException,
      TownHallNotFoundException, BuildingIsAlreadyOnMaxLevelException,
      BuildingDoesNotBelongToKingdomException,
      BuildingIsNotFinishedException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Building building = getBuildingById(buildingId);
    Kingdom kingdom = kingdomService.getKingdomById(kingdomId);
    Optional<Building> optionalTownhall = kingdom.getBuildings().stream()
        .filter(b -> b.getType().equals("townhall"))
        .findFirst();

    if (optionalTownhall.isEmpty()) {
      throw new TownHallNotFoundException(
          "This kingdom doesn't have a Townhall.");
    }

    Building townhall = optionalTownhall.get();

    if (kingdom != building.getKingdom()) {
      throw new BuildingDoesNotBelongToKingdomException("Provided Id must belong to the kingdom.");
    } else if (building.getLevel() == 20) {
      throw new BuildingIsAlreadyOnMaxLevelException("Maximum level is 20.");
    } else if (building.getLevel() >= townhall.getLevel() && !building.getType()
        .equals("townhall")) {
      throw new BuildingIsAlreadyOnMaxLevelException(
          "Level is higher than the Townhall's level");
    } else if (building.getFinishedAt() > timeService.getTime()) {
      throw new BuildingIsNotFinishedException("Only already finished buildings can be upgraded.");
    } else if (townhall.getFinishedAt() > timeService.getTime()) {
      throw new BuildingIsNotFinishedException(
          "Upgrading is restricted while townhall is being upgraded.");
    }

    building.levelUp(timeService.getTime());
    buildingRepository.save(building);

    return building;
  }
}
