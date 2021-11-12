package com.AK.exceptions.globalExceptionHandling;

import com.AK.exceptions.troopExceptions.InvalidTroopTypeException;
import com.AK.exceptions.troopExceptions.InvalidTroopException;
import com.AK.exceptions.troopExceptions.TroopNotFoundException;
import com.AK.exceptions.troopExceptions.TroopParametersRequiredException;
import com.AK.models.userDTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TroopGlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidTroopTypeException.class)
  public ResponseEntity<Object> invalidTroopType(InvalidTroopTypeException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.CONFLICT);
  }

  public ResponseEntity<Object> missingTroopsParameter(InvalidTroopTypeException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(TroopParametersRequiredException.class)
  public ResponseEntity<Object> missingTroopsParameter(TroopParametersRequiredException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidTroopException.class)
  public ResponseEntity<Object> troopNotBelongToUser(InvalidTroopException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(TroopNotFoundException.class)
  public ResponseEntity<Object> troopNotFound(TroopNotFoundException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
  }
}