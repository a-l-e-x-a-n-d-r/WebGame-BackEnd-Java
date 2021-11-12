package com.AK.models.resources.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorMessageDTO {

  private String message;

  public ErrorMessageDTO(String message) {
    this.message = message;
  }
}