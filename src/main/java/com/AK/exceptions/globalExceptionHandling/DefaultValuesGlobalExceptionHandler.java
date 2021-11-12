package com.AK.exceptions.globalExceptionHandling;

import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.userDTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultValuesGlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(DefaultValuesFileNotFoundException.class)
  public ResponseEntity<Object> DefaultValuesFileNotFound(DefaultValuesFileNotFoundException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DefaultValueNotFoundException.class)
  public ResponseEntity<Object> DefaultValuesFileNotFound(DefaultValueNotFoundException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DefaultValuesZeroValueException.class)
  public ResponseEntity<Object> DefaultValuesZeroValue(DefaultValuesZeroValueException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(DefaultValueInvalidInputException.class)
  public ResponseEntity<Object> DefaultValueInvalidInput(DefaultValueInvalidInputException e) {
    logger.error(e.getStackTrace());
    return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }
}
