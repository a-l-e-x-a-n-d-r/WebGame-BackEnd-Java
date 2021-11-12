package com.AK.services;

import com.AK.exceptions.BuildingValidTypeRequiredException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.NotEnoughGoldException;
import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.exceptions.troopExceptions.InvalidTroopTypeException;
import com.AK.models.Kingdom;
import com.AK.models.buildings.Building;
import com.AK.models.troops.Troop;

public interface PurchaseService {

  void purchaseNewBuilding(Kingdom kingdom, String type)
      throws NotEnoughGoldException, BuildingTypeRequiredException, KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException;

  void purchaseBuildingUpgrade(Kingdom kingdom, Building building)
      throws BuildingTypeRequiredException, BuildingValidTypeRequiredException, NotEnoughGoldException, KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException;

  void purchaseNewTroop(Kingdom kingdom, String type)
      throws NotEnoughGoldException, InvalidTroopTypeException, KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException;

  void purchaseTroopUpgrade(Kingdom kingdom,
      Troop troop)
      throws NotEnoughGoldException, InvalidTroopTypeException, KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException;

}
