package com.AK.exceptions.globalExceptionHandling;

import com.AK.exceptions.BuildingDoesNotBelongToKingdomException;
import com.AK.exceptions.BuildingIsAlreadyOnMaxLevelException;
import com.AK.exceptions.BuildingIsNotFinishedException;
import com.AK.exceptions.InvalidBuildingException;
import com.AK.exceptions.BuildingNotFoundException;
import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.KingdomAlreadyHasTownhallException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomHasNotEnoughResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.TownHallNotFoundException;
import com.AK.exceptions.UpgradingIsRestrictedWhileTownhallIsUpgradedException;
import com.AK.exceptions.troopExceptions.AcademyDoesNotBelongToPlayerException;
import com.AK.exceptions.troopExceptions.AcademyNotFoundException;
import com.AK.exceptions.troopExceptions.BuildingIsNotAcademyException;
import com.AK.models.userDTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BuildingGlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BuildingIsNotAcademyException.class)
  public ResponseEntity<Object> buildingIsNotAcademy(BuildingIsNotAcademyException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BuildingNotFoundException.class)
  public ResponseEntity<Object> buildingNotFound(BuildingNotFoundException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AcademyNotFoundException.class)
  public ResponseEntity<Object> academyNotFound(AcademyNotFoundException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AcademyDoesNotBelongToPlayerException.class)
  public ResponseEntity<Object> academyDoesNotBelongToPlayer(
      AcademyDoesNotBelongToPlayerException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(KingdomHasNoResourcesException.class)
  public ResponseEntity<Object> kingdomHasNoResources(KingdomHasNoResourcesException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(KingdomNotFoundException.class)
  public ResponseEntity<Object> kingdomNotFound(KingdomNotFoundException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TownHallNotFoundException.class)
  public ResponseEntity<Object> townHallNotFound(TownHallNotFoundException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(BuildingTypeRequiredException.class)
  public ResponseEntity<Object> buildingTypeRequired(BuildingTypeRequiredException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(KingdomAlreadyHasTownhallException.class)
  public ResponseEntity<Object> kingdomAlreadyHasTownhall(KingdomAlreadyHasTownhallException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(BuildingIsAlreadyOnMaxLevelException.class)
  public ResponseEntity<Object> buildingOnMaxLevel(BuildingIsAlreadyOnMaxLevelException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BuildingDoesNotBelongToKingdomException.class)
  public ResponseEntity<Object> buildingDoesNotBelongToKingdom(
      BuildingDoesNotBelongToKingdomException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BuildingIsNotFinishedException.class)
  public ResponseEntity<Object> buildingIsNotFinished(BuildingIsNotFinishedException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(UpgradingIsRestrictedWhileTownhallIsUpgradedException.class)
  public ResponseEntity<Object> upgradingIsRestrictedWhileTownhallIsUpgraded(
      UpgradingIsRestrictedWhileTownhallIsUpgradedException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(KingdomHasNotEnoughResourcesException.class)
  public ResponseEntity<Object> kingdomHasNotEnoughResources(
      KingdomHasNotEnoughResourcesException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InvalidBuildingException.class)
  public ResponseEntity<Object> buildingDoesntBelongToUser(InvalidBuildingException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.FORBIDDEN);
  }
}