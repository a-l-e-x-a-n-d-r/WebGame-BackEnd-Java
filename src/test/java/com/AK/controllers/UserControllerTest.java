package com.AK.controllers;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.AK.models.CustomUserDetails;
import com.AK.models.Kingdom;
import com.AK.models.User;
import com.AK.models.authentication.AuthenticationRequest;
import com.AK.repositories.UserRepository;
import com.AK.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private JwtService jwtService;

  @MockBean
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @MockBean
  private UserRepository userRepository;

  private Kingdom kingdom;

  @BeforeEach
  void beforeStart() {
    kingdom = new Kingdom("polo");
    objectMapper = new ObjectMapper();
  }

  @Test
  void loginSuccesfull() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest("hana", "123");
    String username = request.getUsername();
    String password = request.getPassword();

    User user = new User(username, password, kingdom);

    when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(user));
    when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);

    Mockito.when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);

    Mockito.when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(user));

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.jwt",
            is(jwtService.generateToken(new CustomUserDetails(user)))))
        .andExpect(jsonPath("$.status", is("ok")))
        .andDo(print());
  }

  @Test
  void noParameters() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest("", "");
    String username = request.getUsername();
    String password = request.getPassword();

    Mockito.when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User(username, password, kingdom)));

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username and password are required.")));
  }

  @Test
  void usernameRequired() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest("", "123");
    String username = request.getUsername();
    String password = request.getPassword();

    Mockito.when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User(username, password, kingdom)));

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is required.")));
  }

  @Test
  void passwordRequired() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest("hana", "");
    String username = request.getUsername();
    String password = request.getPassword();

    Mockito.when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(new User(username, password, kingdom)));

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password is required.")));
  }

  @Test
  void notCorrectParameters() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest("hana", "123");
    String username = "dana";
    String password = "1234";

    User user = new User(username, password, kingdom);
    when(userRepository.findByUsername(any())).thenReturn(
        java.util.Optional.of(user));
    when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

    mockMvc.perform(post("/login").contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password or username is incorrect.")));
  }
}
