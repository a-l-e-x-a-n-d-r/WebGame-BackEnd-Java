package com.AK.controllers;

import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.models.Kingdom;
import com.AK.models.kingdomDTO.KingdomDTO;
import com.AK.services.JwtService;
import com.AK.services.KingdomService;
import com.AK.services.ResourceService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KingdomController {

  private final ResourceService resourceService;
  private final KingdomService kingdomService;
  private final JwtService jwtService;
  private final Logger logger;

  @Autowired
  public KingdomController(
      ResourceService resourceService,
      KingdomService kingdomService,
      JwtService jwtService, Logger logger) {
    this.resourceService = resourceService;
    this.kingdomService = kingdomService;
    this.jwtService = jwtService;
    this.logger = logger;
  }

  @GetMapping("/kingdom/resources")
  public ResponseEntity<?> getResources(@RequestHeader("Authorization") String token)
      throws IdRequiredException, KingdomNotFoundException, KingdomHasNoResourcesException {

    Long kingdomId = jwtService.extractKingdomId(token);
    Kingdom kingdom = kingdomService.getKingdomById(kingdomId);

    Long userId = jwtService.extractUserId(token);

    logger.info("METHOD GET : /kingdom/resources User : " + userId + " KingdomId : " + kingdomId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(resourceService.getKingdomsResources(kingdom));
  }

  @GetMapping("/kingdom/{id}")
  public ResponseEntity getKingdomById(@PathVariable Long id)
      throws IdRequiredException, KingdomNotFoundException {

    Kingdom kingdom = kingdomService.getKingdomById(id);

    logger.info("METHOD GET : /kingdom/id ");

    return ResponseEntity.status(HttpStatus.OK).body(new KingdomDTO(kingdom));
  }
}
