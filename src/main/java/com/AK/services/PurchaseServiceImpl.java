package com.AK.services;

import static com.AK.configuration.DefaultValues.getDefaultValueInt;

import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.NotEnoughGoldException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.exceptions.troopExceptions.InvalidTroopTypeException;
import com.AK.models.Kingdom;
import com.AK.models.buildings.Building;
import com.AK.models.resources.Resource;
import com.AK.models.troops.Troop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService {

  private ResourceService resourceService;

  @Autowired
  public PurchaseServiceImpl(
      ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @Override
  public void purchaseNewBuilding(Kingdom kingdom, String type)
      throws NotEnoughGoldException, BuildingTypeRequiredException, KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    switch (type) {
      case "farm":
        deductResourcesNew(getDefaultValueInt("buildingFarmPrice"), kingdom);
        break;
      case "mine":
        deductResourcesNew(getDefaultValueInt("buildingMinePrice"), kingdom);
        break;
      case "academy":
        deductResourcesNew(getDefaultValueInt("buildingAcademyPrice"), kingdom);
        break;
      default:
        throw new BuildingTypeRequiredException("Invalid building type.");
    }
  }

  @Override
  public void purchaseBuildingUpgrade(Kingdom kingdom, Building building)
      throws NotEnoughGoldException, KingdomHasNoResourcesException, BuildingTypeRequiredException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    switch (building.getType()) {
      case "townhall":
        deductResourcesUpgradeBuilding(getDefaultValueInt("buildingTownhallPrice"), kingdom,
            building);
        break;
      case "farm":
        deductResourcesUpgradeBuilding(getDefaultValueInt("buildingFarmPrice"), kingdom, building);
        break;
      case "mine":
        deductResourcesUpgradeBuilding(getDefaultValueInt("buildingMinePrice"), kingdom, building);
        break;
      case "academy":
        deductResourcesUpgradeBuilding(getDefaultValueInt("buildingAcademyPrice"), kingdom,
            building);
        break;
      default:
        throw new BuildingTypeRequiredException("Invalid building type.");
    }
  }

  @Override
  public void purchaseNewTroop(Kingdom kingdom, String type)
      throws
      NotEnoughGoldException, InvalidTroopTypeException, KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    switch (type) {
      case "archer":
        deductResourcesNew(getDefaultValueInt("troopArcherPrice"), kingdom);
        break;
      case "swordsman":
        deductResourcesNew(getDefaultValueInt("troopSwordsmanPrice"), kingdom);
        break;
      case "pikeman":
        deductResourcesNew(getDefaultValueInt("troopPikemanPrice"), kingdom);
        break;
      case "axeman":
        deductResourcesNew(getDefaultValueInt("troopAxemanPrice"), kingdom);
        break;
      case "horseman":
        deductResourcesNew(getDefaultValueInt("troopHorsemanPrice"), kingdom);
        break;
      default:
        throw new InvalidTroopTypeException("Invalid troop type.");
    }
  }

  @Override
  public void purchaseTroopUpgrade(Kingdom kingdom, Troop troop)
      throws
      NotEnoughGoldException, InvalidTroopTypeException, KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    switch (troop.getType()) {
      case "archer":
        deductResourcesUpgradeTroop(getDefaultValueInt("troopArcherPrice"), kingdom, troop);
        break;
      case "swordsman":
        deductResourcesUpgradeTroop(getDefaultValueInt("troopSwordsmanPrice"), kingdom, troop);
        break;
      case "pikeman":
        deductResourcesUpgradeTroop(getDefaultValueInt("troopPikemanPrice"), kingdom, troop);
        break;
      case "axeman":
        deductResourcesUpgradeTroop(getDefaultValueInt("troopAxemanPrice"), kingdom, troop);
        break;
      case "horseman":
        deductResourcesUpgradeTroop(getDefaultValueInt("troopHorsemanPrice"), kingdom, troop);
        break;
      default:
        throw new InvalidTroopTypeException("Invalid troop type.");
    }
  }

  private void deductResourcesNew(int price, Kingdom kingdom)
      throws KingdomHasNoResourcesException, NotEnoughGoldException {
    Resource gold = resourceService.findAllByKingdomAndType(kingdom, "gold");
    int goldAmount = gold.getAmount();
    if ((goldAmount - price) >= 0) {
      gold.setAmount(goldAmount - price);
      resourceService.save(gold);
    } else {
      throw new NotEnoughGoldException("You don't have enough gold to purchase this building.");
    }
  }

  private void deductResourcesUpgradeBuilding(int price, Kingdom kingdom, Building building)
      throws KingdomHasNoResourcesException, NotEnoughGoldException {
    Resource gold = resourceService.findAllByKingdomAndType(kingdom, "gold");
    int goldAmount = gold.getAmount();
    int levelToBeUpgradedTo = building.getLevel() + 1;
    if ((goldAmount - price * levelToBeUpgradedTo) >= 0) {
      gold.setAmount(goldAmount - price * levelToBeUpgradedTo);
      resourceService.save(gold);
    } else {
      throw new NotEnoughGoldException("You don't have enough gold to upgrade this building.");
    }
  }

  private void deductResourcesUpgradeTroop(int price, Kingdom kingdom, Troop troop)
      throws KingdomHasNoResourcesException, NotEnoughGoldException {
    Resource gold = resourceService.findAllByKingdomAndType(kingdom, "gold");
    int goldAmount = gold.getAmount();
    int levelToBeUpgradedTo = troop.getLevel() + 1;
    if ((goldAmount - price * levelToBeUpgradedTo) >= 0) {
      gold.setAmount(goldAmount - price * levelToBeUpgradedTo);
      resourceService.save(gold);
    } else {
      throw new NotEnoughGoldException("You don't have enough gold to upgrade this troop.");
    }
  }
}
