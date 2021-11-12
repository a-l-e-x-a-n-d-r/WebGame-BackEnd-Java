package com.AK.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceUnitTest {

  private UserServiceImpl userService;
  private JwtService jwtService;
  private UserRepository fakeUserRepository;
  private AuthenticationManager authenticationManager;
  private UserDetailsService userDetailsService;
  private BCryptPasswordEncoder fakeBCryptPasswordEncoder;

  @BeforeEach
  void beforeStart() {
    jwtService = new JwtService();
    fakeUserRepository = Mockito.mock(UserRepository.class);
    KingdomService fakeKingdomService = Mockito.mock(KingdomService.class);
    fakeBCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    authenticationManager = Mockito.mock(AuthenticationManager.class);
    userDetailsService = Mockito.mock(UserDetailsService.class);
    userService = new UserServiceImpl(fakeUserRepository, fakeUserRepository, fakeKingdomService,
        fakeBCryptPasswordEncoder, jwtService, authenticationManager, userDetailsService);
  }

  @Test
  void findUserandReturnToken()
      throws UserNotFoundException, IncorrectPasswordException, UsernameRequiredException, PasswordRequiredException, UsernameAndPasswordRequiredException {

    String username = "hana";
    String password = "12345678";
    Kingdom kingdom = new Kingdom("polo");
    User user = new User(username, password, kingdom);

    when(fakeUserRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User(username, password, kingdom)));

    when(fakeBCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);

    Mockito.when(fakeUserRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(user));

    Mockito.when(fakeBCryptPasswordEncoder.matches(any(), any())).thenReturn(true);

    Mockito.when(userDetailsService.loadUserByUsername(any()))
        .thenReturn(new CustomUserDetails(user));

    String jwt = jwtService.generateToken(new CustomUserDetails(user));
    String result = userService.login(user.getUsername(), user.getPassword());

    assertEquals(jwt, result);
  }

  @Test
  void loginUsernameAndPasswordRequiredException() {
    Exception e = assertThrows(UsernameAndPasswordRequiredException.class,
        () -> userService.login("", ""));
    assertEquals("Username and password are required.", e.getMessage());
  }

  @Test
  void loginUsernameRequired() {
    Exception e = assertThrows(UsernameRequiredException.class, () -> userService.login("", "123"));
    assertEquals("Username is required.", e.getMessage());
  }

  @Test
  void loginPasswordRequired() {
    Exception e = assertThrows(PasswordRequiredException.class,
        () -> userService.login("hana", ""));
    assertEquals("Password is required.", e.getMessage());
  }

  @Test
  void loginIncorrectPasswordException() {
    Kingdom kingdom = new Kingdom("polo");

    when(fakeUserRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User("hana", "1234", kingdom)));

    Exception e = assertThrows(IncorrectPasswordException.class,
        () -> userService.login("hana", "123"));
    assertEquals("Password or username is incorrect.", e.getMessage());
  }

  @Test
  void loginUserNotFound() {
    when(fakeUserRepository.findByUsername(any())).thenReturn(Optional.empty());

    Exception e = assertThrows(UserNotFoundException.class, () -> userService.login("hana", "123"));
    assertEquals("User not found.", e.getMessage());
  }

  @Test
  public void registerSuccess() throws Exception {
    UserRegisterRequestDTO temp = new UserRegisterRequestDTO("hana",
        "12345678",
        "polo");
    Kingdom kingdom = new Kingdom("polo");
    User user = new User(temp.getUsername(), temp.getPassword(), kingdom);

    when(fakeUserRepository.save(any())).thenReturn(user);

    User result = userService.register(temp);

    assertEquals("hana", result.getUsername());
    assertEquals("polo", result.getKingdom().getName());
  }

  @Test
  public void registerUsernameAndPasswordRequiredException() {

    Exception e = assertThrows(UsernameAndPasswordRequiredException.class,
        () -> userService.register(new UserRegisterRequestDTO("",
            "", "Tirol")));
    assertEquals("Username and password are required.", e.getMessage());
  }

  @Test
  public void registerUsernameRequiredException() {
    Exception e = assertThrows(UsernameRequiredException.class,
        () -> userService.register(new UserRegisterRequestDTO("",
            "12345678", "Tirol")));
    assertEquals("Username is required.", e.getMessage());
  }

  @Test
  public void registerPasswordRequiredException() {
    Exception e = assertThrows(PasswordRequiredException.class,
        () -> userService.register(new UserRegisterRequestDTO("Anton aus Tirol",
            "", "Tirol")));
    assertEquals("Password is required.", e.getMessage());
  }

  @Test
  public void registerUsernameIsTakenException() {
    UserRegisterRequestDTO temp = new UserRegisterRequestDTO("Anton aus Tirol",
        "12345678",
        "Tirol");
    when(fakeUserRepository.findByUsername(any())).thenReturn(
        Optional.of(new User("Anton aus Tirol", null, null)));

    Exception e = assertThrows(UsernameIsTakenException.class,
        () -> userService.register(temp));
    assertEquals("Username is already taken.", e.getMessage());
  }

  @Test
  public void registerPasswordMinCharactersException() {
    Exception e = assertThrows(PasswordMinCharactersException.class,
        () -> userService.register(new UserRegisterRequestDTO("Anton aus Tirol",
            "1234567", "Tirol")));
    assertEquals("Password must be at least 8 characters.", e.getMessage());
  }

  @Test
  public void registerKingdomNameRequiredException() {
    Exception e = assertThrows(KingdomNameRequiredException.class,
        () -> userService.register(new UserRegisterRequestDTO("Anton aus Tirol",
            "12345678", "")));
    assertEquals("Kingdom name is required.", e.getMessage());
  }
}
