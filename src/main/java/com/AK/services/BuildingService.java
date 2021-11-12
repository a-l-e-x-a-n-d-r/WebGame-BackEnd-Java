package com.AK.services;

import com.AK.exceptions.BuildingDoesNotBelongToKingdomException;
import com.AK.exceptions.BuildingIsAlreadyOnMaxLevelException;
import com.AK.exceptions.BuildingIsNotFinishedException;
import com.AK.exceptions.InvalidBuildingException;
import com.AK.exceptions.BuildingNotFoundException;
import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.KingdomAlreadyHasTownhallException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomHasNotEnoughResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.TownHallNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
import com.AK.models.buildings.Building;
import com.AK.models.buildings.BuildingListDTO;
import com.AK.models.buildings.CreateBuildingDTO;
import java.io.IOException;

public interface BuildingService {

  Building createBuilding(CreateBuildingDTO buildingDTO, Long kingdomId)
      throws BuildingTypeRequiredException, TownHallNotFoundException, IdRequiredException, KingdomNotFoundException, KingdomAlreadyHasTownhallException, IOException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException;

  BuildingListDTO getKingdomBuildings(Kingdom kingdom);

  Building getBuildingById(Long buildingId) throws IdRequiredException, BuildingNotFoundException;

  void validateBuildingOwner(Long id, Building building) throws InvalidBuildingException;

  Building retrieveBuildingFromKingdom(Long kingdomId, Long buildingId)
      throws IdRequiredException, BuildingNotFoundException, InvalidBuildingException;

  boolean isBuildingAcademy(Long buildingId)
      throws IdRequiredException, BuildingNotFoundException;

  boolean isAcademyPresent(Long buildingId);

  Building upgradeBuilding(Long buildingId, Long kingdomId)
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException, TownHallNotFoundException,
      BuildingIsAlreadyOnMaxLevelException, KingdomHasNoResourcesException,
      KingdomHasNotEnoughResourcesException, BuildingDoesNotBelongToKingdomException, BuildingIsNotFinishedException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException;
}
