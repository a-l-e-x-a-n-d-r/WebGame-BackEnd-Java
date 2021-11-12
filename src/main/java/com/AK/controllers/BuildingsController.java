package com.AK.controllers;

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
import com.AK.models.buildings.BuildingDTO;
import com.AK.models.buildings.CreateBuildingDTO;
import com.AK.services.BuildingService;
import com.AK.services.JwtService;
import com.AK.services.KingdomService;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildingsController {

  private BuildingService buildingService;
  private JwtService jwtService;
  private KingdomService kingdomService;
  private final Logger logger;

  @Autowired
  public BuildingsController(
      BuildingService buildingService,
      JwtService jwtService,
      KingdomService kingdomService, Logger logger) {
    this.buildingService = buildingService;
    this.jwtService = jwtService;
    this.kingdomService = kingdomService;
    this.logger = logger;
  }

  @PostMapping("/kingdom/buildings")
  public ResponseEntity<?> buildBuilding(
      @RequestBody(required = false) CreateBuildingDTO buildingDTO,
      @RequestHeader("Authorization") String token)
      throws IdRequiredException, KingdomNotFoundException, BuildingTypeRequiredException, TownHallNotFoundException, KingdomAlreadyHasTownhallException, DefaultValuesFileNotFoundException, IOException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {

    Long kingdomId = jwtService.extractKingdomId(token);
    Building building = buildingService.createBuilding(buildingDTO, kingdomId);

    Long userId = jwtService.extractUserId(token);

    logger
        .info("METHOD POST : /kingdom/buildings User : " + userId + " KingdomId : " + kingdomId);

    return ResponseEntity.status(HttpStatus.OK).body(new BuildingDTO(building));
  }

  @GetMapping("/kingdom/buildings")
  public ResponseEntity<?> getBuildings(
      @RequestHeader("Authorization") String token)
      throws IdRequiredException, KingdomNotFoundException {

    Long kingdomId = jwtService.extractKingdomId(token);
    Kingdom kingdom = kingdomService.getKingdomById(kingdomId);

    Long userId = jwtService.extractUserId(token);

    logger.info("METHOD GET : /kingdom/buildings User : " + userId + " KingdomId : " + kingdomId);

    return ResponseEntity.status(HttpStatus.OK).body(buildingService.getKingdomBuildings(kingdom));
  }

  @PutMapping("/kingdom/buildings/{buildingId}")
  public ResponseEntity<?> upgradeBuilding(@RequestHeader("Authorization") String token,
      @PathVariable Long buildingId)
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException,
      TownHallNotFoundException, BuildingIsAlreadyOnMaxLevelException,
      KingdomHasNoResourcesException, KingdomHasNotEnoughResourcesException, BuildingDoesNotBelongToKingdomException,
      BuildingIsNotFinishedException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Long kingdomId = jwtService.extractKingdomId(token);
    Building building = buildingService.upgradeBuilding(buildingId, kingdomId);

    Long userId = jwtService.extractUserId(token);

    logger.info(
        "METHOD PUT : /kingdom/buildings/id User : " + userId + " KingdomId : " + kingdomId);

    return ResponseEntity.status(HttpStatus.OK).body(new BuildingDTO(building));
  }

  @GetMapping("/kingdom/buildings/{id}")
  public ResponseEntity getBuildingById(@PathVariable Long id,
      @RequestHeader("Authorization") String token)
      throws IdRequiredException, BuildingNotFoundException, InvalidBuildingException {
    Long kingdomId = jwtService.extractKingdomId(token);

    Building building = buildingService.retrieveBuildingFromKingdom(kingdomId, id);

    Long userId = jwtService.extractUserId(token);

    logger.info(
        "METHOD GET : /kingdom/buildings/id User : " + userId + " KingdomId : " + kingdomId);

    return ResponseEntity.status(HttpStatus.OK).body(new BuildingDTO(building));
  }
}
