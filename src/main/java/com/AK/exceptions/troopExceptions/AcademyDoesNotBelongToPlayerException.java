package com.AK.exceptions.troopExceptions;

public class AcademyDoesNotBelongToPlayerException extends Exception {

  public AcademyDoesNotBelongToPlayerException(String message) {
    super(message);
  }
}