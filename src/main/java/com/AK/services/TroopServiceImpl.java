package com.AK.services;

import com.AK.exceptions.BuildingNotFoundException;
import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.exceptions.troopExceptions.AcademyDoesNotBelongToPlayerException;
import com.AK.exceptions.troopExceptions.AcademyNotFoundException;
import com.AK.exceptions.troopExceptions.BuildingIsNotAcademyException;
import com.AK.exceptions.troopExceptions.InvalidTroopTypeException;
import com.AK.exceptions.troopExceptions.InvalidTroopException;
import com.AK.exceptions.troopExceptions.TroopNotFoundException;
import com.AK.exceptions.troopExceptions.TroopParametersRequiredException;
import com.AK.models.Kingdom;
import com.AK.models.troops.CreatingTroopRequest;
import com.AK.models.troops.Troop;
import com.AK.models.troops.dto.TroopDTO;
import com.AK.models.troops.dto.TroopsListDTO;
import com.AK.repositories.TroopRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopServiceImpl implements TroopService {

  private TroopRepository troopRepository;
  private BuildingService buildingService;
  private KingdomService kingdomService;
  private TroopFactory troopFactory;

  @Autowired
  public TroopServiceImpl(
      TroopRepository troopRepository,
      BuildingService buildingService,
      KingdomService kingdomService,
      TroopFactory troopFactory) {
    this.troopRepository = troopRepository;
    this.buildingService = buildingService;
    this.kingdomService = kingdomService;
    this.troopFactory = troopFactory;
  }

  @Override
  public Troop createTroop(CreatingTroopRequest creatingTroopRequest, Long kingdomId)
      throws TroopParametersRequiredException, AcademyDoesNotBelongToPlayerException, AcademyNotFoundException,
      BuildingNotFoundException, KingdomNotFoundException, IdRequiredException, InvalidTroopTypeException,
      BuildingIsNotAcademyException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    if (creatingTroopRequest.getTroopType() == null
        || creatingTroopRequest.getTroopType().isEmpty()
        || creatingTroopRequest.getBuildingId() == null) {
      throw new TroopParametersRequiredException("Missing parameters(s): type/academy id.");
    } else if (!buildingService.isAcademyPresent(creatingTroopRequest.getBuildingId())) {
      throw new AcademyNotFoundException("Academy not found!");
    } else if (!buildingService.isBuildingAcademy(creatingTroopRequest.getBuildingId())) {
      throw new BuildingIsNotAcademyException("Building is not an academy!");
    } else if (kingdomService.getKingdomById(kingdomId).getUser() != buildingService
        .getBuildingById(creatingTroopRequest.getBuildingId()).getKingdom().getUser()) {
      throw new AcademyDoesNotBelongToPlayerException("Academy does not belong to player!");
    }

    Troop troop = troopFactory
        .createTroop(creatingTroopRequest.getTroopType(), kingdomService.getKingdomById(kingdomId));
    troopRepository.save(troop);
    return troop;
  }

  @Override
  public TroopsListDTO getKingdomTroops(Kingdom kingdom) {
    List<Troop> troops = troopRepository.findAllByKingdom(kingdom);
    List<TroopDTO> troopDTOS = troops.stream().map(Troop::toDTO)
        .collect(Collectors.toList());
    return new TroopsListDTO(troopDTOS);
  }

  @Override
  public void validateTroopOwner(Long kingdomId, Troop troop) throws InvalidTroopException {
    if (!kingdomId.equals(troop.getKingdom().getId())) {
      throw new InvalidTroopException("Forbidden Action. Troop doesnt belong to user.");
    }
  }

  @Override
  public Troop getTroopById(Long troopId) throws IdRequiredException, TroopNotFoundException {
    if (troopId == null) {
      throw new IdRequiredException("Id is required.");
    }
    Optional<Troop> troop = troopRepository.findById(troopId);
    if (troop.isEmpty()) {
      throw new TroopNotFoundException("Troop not found.");
    } else {
      return troop.get();
    }
  }

  @Override
  public Troop retrieveTroopFromKingdom(Long kingdomId, Long troopId)
      throws IdRequiredException, TroopNotFoundException, InvalidTroopException {

    Troop troop = getTroopById(troopId);
    validateTroopOwner(kingdomId, troop);

    return troop;
  }
}