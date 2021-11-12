package com.AK.controllers;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.AK.models.User;
import com.AK.models.userDTO.registerDTO.UserRegisterRequestDTO;
import com.AK.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerRegisterIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void beforeStart() {
    objectMapper = new ObjectMapper();
  }

  @Test
  @Order(1)
  void registrationSuccessful() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("Sauron", "12345678", "Mordor");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.username", is("Sauron")))
        .andExpect(jsonPath("$.kingdomId", is(1)))
        .andExpect(jsonPath("$.avatar", is("Avatar")))
        .andExpect(jsonPath("$.points", is(0)))
        .andDo(print());
  }

  @Test
  @Order(2)
  void registrationUsernameAndPasswordRequired() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("", "", "Mordor");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username and password are required.")))
        .andDo(print());
  }

  @Test
  @Order(3)
  void registrationUsernameRequired() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("", "12345678", "Mordor");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is required.")))
        .andDo(print());
  }

  @Test
  @Order(4)
  void registrationPasswordRequired() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("Sauron", "", "Mordor");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password is required.")))
        .andDo(print());
  }

  @Test
  @Order(5)
  void registrationUsernameIsTaken() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("Sauron", "12345678", "Mordor");
    User firstUser = new User("Sauron", "12345678", null);
    userRepository.save(firstUser);

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is already taken.")))
        .andDo(print());
  }

  @Test
  @Order(6)
  void registrationPasswordMinCharacters() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("Sauron", "1234567", "Mordor");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password must be at least 8 characters.")))
        .andDo(print());
  }

  @Test
  @Order(7)
  void registrationKingdomNameRequired() throws Exception {

    UserRegisterRequestDTO req = new UserRegisterRequestDTO("Sauron", "12345678", "");

    mockMvc.perform(post("/register")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Kingdom name is required.")))
        .andDo(print());
  }
}
