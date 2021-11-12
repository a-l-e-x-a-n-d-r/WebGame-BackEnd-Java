package com.AK.exceptions.globalExceptionHandling;

import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.userExceptions.PasswordRequiredException;
import com.AK.exceptions.userExceptions.UsernameAndPasswordRequiredException;
import com.AK.exceptions.userExceptions.UsernameRequiredException;
import com.AK.exceptions.userExceptions.userLoginExceptions.IncorrectPasswordException;
import com.AK.exceptions.userExceptions.userLoginExceptions.UserNotFoundException;
import com.AK.exceptions.userExceptions.userRegisterExceptions.KingdomNameRequiredException;
import com.AK.exceptions.userExceptions.userRegisterExceptions.PasswordMinCharactersException;
import com.AK.exceptions.userExceptions.userRegisterExceptions.UsernameIsTakenException;
import com.AK.models.userDTO.ErrorDTO;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final Logger logger;

  public UserGlobalExceptionHandler(Logger logger) {
    this.logger = logger;
  }

  @ExceptionHandler(UsernameAndPasswordRequiredException.class)
  public ResponseEntity<Object> usernameAndPasswordRequired(
      UsernameAndPasswordRequiredException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UsernameIsTakenException.class)
  public ResponseEntity<Object> usernameTaken(UsernameIsTakenException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(KingdomNameRequiredException.class)
  public ResponseEntity<Object> kingdomNameRequired(KingdomNameRequiredException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UsernameRequiredException.class)
  public ResponseEntity<Object> usernameRequired(UsernameRequiredException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PasswordRequiredException.class)
  public ResponseEntity<Object> passwordRequiredException(PasswordRequiredException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PasswordMinCharactersException.class)
  public ResponseEntity<Object> passwordMinCharacters(PasswordMinCharactersException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(IncorrectPasswordException.class)
  public ResponseEntity<Object> incorrectPassword(IncorrectPasswordException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Object> userNotFound(UserNotFoundException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IdRequiredException.class)
  public ResponseEntity<Object> idRequired(IdRequiredException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }
}