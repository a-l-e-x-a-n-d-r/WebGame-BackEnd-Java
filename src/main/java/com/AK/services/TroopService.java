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
import com.AK.models.troops.dto.TroopsListDTO;

public interface TroopService {

  Troop createTroop(CreatingTroopRequest creatingTroopRequest, Long kingdomId)
      throws TroopParametersRequiredException, AcademyDoesNotBelongToPlayerException,
      AcademyNotFoundException, IdRequiredException, BuildingNotFoundException, KingdomNotFoundException,
      InvalidTroopTypeException, BuildingIsNotAcademyException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException;

  TroopsListDTO getKingdomTroops (Kingdom kingdom);

  void validateTroopOwner(Long id,Troop troop) throws InvalidTroopException;

  Troop getTroopById(Long troopId) throws IdRequiredException, TroopNotFoundException;

  Troop retrieveTroopFromKingdom(Long kingdomId, Long troopId)
      throws IdRequiredException, TroopNotFoundException, InvalidTroopException;

}

