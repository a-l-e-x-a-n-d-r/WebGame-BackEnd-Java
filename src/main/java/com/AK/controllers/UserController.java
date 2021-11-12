package com.AK.controllers;

import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.exceptions.userExceptions.PasswordRequiredException;
import com.AK.exceptions.userExceptions.UsernameAndPasswordRequiredException;
import com.AK.exceptions.userExceptions.UsernameRequiredException;
import com.AK.exceptions.userExceptions.userLoginExceptions.IncorrectPasswordException;
import com.AK.exceptions.userExceptions.userLoginExceptions.UserNotFoundException;
import com.AK.exceptions.userExceptions.userRegisterExceptions.KingdomNameRequiredException;
import com.AK.exceptions.userExceptions.userRegisterExceptions.PasswordMinCharactersException;
import com.AK.exceptions.userExceptions.userRegisterExceptions.UsernameIsTakenException;
import com.AK.models.User;
import com.AK.models.authentication.AuthenticationRequest;
import com.AK.models.userDTO.authentication.AuthenticateResponse;
import com.AK.models.userDTO.registerDTO.UserRegisterRequestDTO;
import com.AK.models.userDTO.registerDTO.UserRegisterResponseDTO;
import com.AK.services.UserService;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;
  private final Logger logger;

  @Autowired
  public UserController(UserService userService, Logger logger) {
    this.userService = userService;
    this.logger = logger;
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginAuthenticate(
      @RequestBody AuthenticationRequest authenticationRequest)
      throws UsernameRequiredException, PasswordRequiredException, UserNotFoundException,
      IncorrectPasswordException, UsernameAndPasswordRequiredException {

    String username = authenticationRequest.getUsername();
    String password = authenticationRequest.getPassword();

    String jwt = userService.login(username, password);

    logger.info("METHOD POST : /login and logged in succesfully");

    return ResponseEntity.status(HttpStatus.OK).body(new AuthenticateResponse(jwt, "ok"));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody(required = false) UserRegisterRequestDTO req)
      throws PasswordMinCharactersException, UsernameRequiredException, PasswordRequiredException,
      UsernameAndPasswordRequiredException, KingdomNameRequiredException, UsernameIsTakenException, KingdomHasNoResourcesException, KingdomNotFoundException, BuildingTypeRequiredException, IOException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {
    User user = userService.register(req);

    logger.info("METHOD POST : /register and registered succesfully");

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new UserRegisterResponseDTO(user.getId(), user.getUsername(), user.getKingdom()));
  }
}
