package com.AK.models.userDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

  private String status;
  private String message;

  public ErrorDTO(String message) {
    this.status = "error";
    this.message = message;
  }
}
