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
import com.AK.models.CustomUserDetails;
import com.AK.models.Kingdom;
import com.AK.models.User;
import com.AK.models.userDTO.registerDTO.UserRegisterRequestDTO;
import com.AK.repositories.UserRepository;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final KingdomService kingdomService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;

  @Autowired
  public UserServiceImpl(UserRepository repository,
      UserRepository userRepository, KingdomService kingdomService,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      JwtService jwtService,
      AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.kingdomService = kingdomService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public String login(String username, String password) throws PasswordRequiredException,
      UsernameRequiredException, IncorrectPasswordException, UserNotFoundException, UsernameAndPasswordRequiredException {

    if ((username == null || username.isEmpty()) && (password == null || password.isEmpty())) {
      throw new UsernameAndPasswordRequiredException("Username and password are required.");
    }
    if (username == null || username.isEmpty()) {
      throw new UsernameRequiredException("Username is required.");
    } else if (password == null || password.isEmpty()) {
      throw new PasswordRequiredException("Password is required.");
    }

    Optional<User> optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException("User not found.");
    }
    User user = optionalUser.get();
    if (!(bCryptPasswordEncoder.matches(password, user.getPassword())) || !username
        .equals(user.getUsername())) {
      throw new IncorrectPasswordException("Password or username is incorrect.");
    }
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(username);

    return jwtService.generateToken((CustomUserDetails) userDetails);
  }

  @Override
  public User register(UserRegisterRequestDTO req)
      throws UsernameAndPasswordRequiredException, UsernameRequiredException,
      PasswordRequiredException, UsernameIsTakenException,
      PasswordMinCharactersException, KingdomNameRequiredException, KingdomHasNoResourcesException, KingdomNotFoundException, BuildingTypeRequiredException, IOException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {
    if (req.getUsername().isEmpty() && req.getPassword().isEmpty()) {
      throw new UsernameAndPasswordRequiredException("Username and password are required.");
    } else if (req.getUsername().isEmpty()) {
      throw new UsernameRequiredException("Username is required.");
    } else if (req.getPassword().isEmpty()) {
      throw new PasswordRequiredException("Password is required.");
    } else if (isUserPresent(req.getUsername())) {
      throw new UsernameIsTakenException("Username is already taken.");
    } else if (req.getPassword().length() < 8) {
      throw new PasswordMinCharactersException("Password must be at least 8 characters.");
    } else if (req.getKingdomName().isEmpty()) {
      throw new KingdomNameRequiredException("Kingdom name is required.");
    }
    return createNewUser(req.getUsername(), req.getPassword(),
        kingdomService.createNewKingdom(req.getKingdomName()));
  }

  @Override
  public boolean isUserPresent(String name) {
    var optionalUser = userRepository.findByUsername(name);
    return optionalUser.isPresent();
  }

  @Override
  public User createNewUser(String username, String password, Kingdom kingdom) {
    User user = new User(username, bCryptPasswordEncoder.encode(password),
        kingdom);
    return userRepository.save(user);
  }
}
