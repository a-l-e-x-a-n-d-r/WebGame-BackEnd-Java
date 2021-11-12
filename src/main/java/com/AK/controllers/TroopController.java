package com.AK.controllers;

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
import com.AK.services.JwtService;
import com.AK.services.KingdomService;
import com.AK.services.TroopService;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kingdom/troops")
public class TroopController {

  private final TroopService troopService;
  private final JwtService jwtService;
  private final KingdomService kingdomService;
  private final Logger logger;

  public TroopController(TroopService troopService,
      JwtService jwtService,
      KingdomService kingdomService, Logger logger) {
    this.troopService = troopService;
    this.jwtService = jwtService;
    this.kingdomService = kingdomService;
    this.logger = logger;
  }


  @PostMapping
  public ResponseEntity<?> createTroop(@RequestHeader("Authorization") String token,
      @RequestBody CreatingTroopRequest creatingTroopRequest)
      throws AcademyDoesNotBelongToPlayerException, TroopParametersRequiredException, AcademyNotFoundException,
      IdRequiredException, BuildingNotFoundException, KingdomNotFoundException, InvalidTroopTypeException,
      BuildingIsNotAcademyException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Long kingdomId = jwtService.extractKingdomId(token);
    Troop troop = troopService.createTroop(creatingTroopRequest, kingdomId);

    Long userId = jwtService.extractUserId(token);

    logger.info("METHOD POST : /kingdom/troops User : " + userId + " KingdomId : " + kingdomId);

    return ResponseEntity.status(200).body(new TroopDTO(troop));
  }

  @GetMapping
  public ResponseEntity<?> getTroops(@RequestHeader("Authorization") String token)
      throws IdRequiredException, KingdomNotFoundException {
    Long kingdomId = jwtService.extractKingdomId(token);
    Kingdom kingdom = kingdomService.getKingdomById(kingdomId);

    Long userId = jwtService.extractUserId(token);

    logger.info("METHOD GET : /kingdom/troops User : " + userId + " KingdomId : " + kingdomId);

    return ResponseEntity.status(HttpStatus.OK).body(troopService.getKingdomTroops(kingdom));
  }

  @GetMapping("/{id}")
  public ResponseEntity getTroopById(@PathVariable Long id,
      @RequestHeader("Authorization") String token)
      throws InvalidTroopException, IdRequiredException, TroopNotFoundException {
    Long kingdomId = jwtService.extractKingdomId(token);

    Troop troop = troopService.retrieveTroopFromKingdom(kingdomId, id);

    Long userId = jwtService.extractUserId(token);

    logger.info("METHOD GET : /kingdom/troops/id User : " + userId + " KingdomId : " + kingdomId);

    return ResponseEntity.status(HttpStatus.OK).body(new TroopDTO(troop));
  }
}