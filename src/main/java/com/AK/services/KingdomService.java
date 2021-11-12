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
import java.io.IOException;

public interface KingdomService {

  Kingdom createNewKingdom(String name)
      throws KingdomHasNoResourcesException, KingdomNotFoundException, BuildingTypeRequiredException, IOException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException;

  Kingdom getKingdomById(Long id) throws KingdomNotFoundException, IdRequiredException;
}
