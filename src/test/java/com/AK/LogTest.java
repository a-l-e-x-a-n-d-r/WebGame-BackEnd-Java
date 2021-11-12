package com.AK;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.AK.models.userDTO.registerDTO.UserRegisterRequestDTO;
import com.AK.repositories.UserRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LogTest {

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
  public void logFileLengthGrows() throws Exception {

    int lengthBefore = 0;
    try (BufferedReader reader = new BufferedReader(new FileReader("logs.log"))) {
      while (reader.readLine() != null) {
        lengthBefore++;
      }

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

      int lengthAfter = 0;
      try (BufferedReader readerAfter = new BufferedReader(new FileReader("logs.log"))) {
        while (readerAfter.readLine() != null) {
          lengthAfter++;
        }

        Assertions.assertNotEquals(lengthBefore, lengthAfter);
      }
    }
  }
}
