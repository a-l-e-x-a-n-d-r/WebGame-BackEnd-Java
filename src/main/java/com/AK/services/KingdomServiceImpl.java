package com.AK.services;

import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
import com.AK.repositories.KingdomRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomServiceImpl implements KingdomService {

  private final KingdomRepository kingdomRepository;
  private final ResourceService resourceService;
  private final BuildingStarterPackService buildingStarterPackService;

  @Autowired
  public KingdomServiceImpl(
      KingdomRepository kingdomRepository,
      ResourceService resourceService,
      BuildingStarterPackService buildingStarterPackService) {
    this.kingdomRepository = kingdomRepository;
    this.resourceService = resourceService;
    this.buildingStarterPackService = buildingStarterPackService;
  }

  @Override
  public Kingdom createNewKingdom(String name)
      throws KingdomHasNoResourcesException, KingdomNotFoundException, BuildingTypeRequiredException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {
    Kingdom kingdom = new Kingdom(name);
    kingdomRepository.save(kingdom);
    setStarterPack(kingdom);
    return kingdom;
  }

  @Override
  public Kingdom getKingdomById(Long id) throws KingdomNotFoundException, IdRequiredException {

    if (id == null) {
      throw new IdRequiredException("Id is required.");
    }
    Optional<Kingdom> kingdom = kingdomRepository.findById(id);
    if (kingdom.isEmpty()) {
      throw new KingdomNotFoundException("Kingdom not found.");
    } else {
      return kingdom.get();
    }
  }

  private void setStarterPack(Kingdom kingdom)
      throws KingdomHasNoResourcesException, KingdomNotFoundException, BuildingTypeRequiredException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {

    if (kingdom == null) {
      throw new KingdomNotFoundException("Kingdom is not found.");
    }

    buildingStarterPackService.setBuildingStarterPack(kingdom);
    resourceService.setStarterResources(kingdom);
  }
}