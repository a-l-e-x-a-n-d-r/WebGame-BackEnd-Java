package com.AK.services;

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
import com.AK.models.Kingdom;
import com.AK.models.User;
import com.AK.models.userDTO.registerDTO.UserRegisterRequestDTO;
import java.io.IOException;

public interface UserService {

  String login(String username, String password)
      throws PasswordRequiredException, UsernameRequiredException, IncorrectPasswordException, UserNotFoundException, UsernameAndPasswordRequiredException;

  User register(UserRegisterRequestDTO req)
      throws UsernameAndPasswordRequiredException, PasswordMinCharactersException, KingdomNameRequiredException, UsernameIsTakenException, UsernameRequiredException, PasswordRequiredException, KingdomHasNoResourcesException, KingdomNotFoundException, BuildingTypeRequiredException, IOException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException;

  boolean isUserPresent(String name);

  User createNewUser(String username, String password, Kingdom kingdom);
}
