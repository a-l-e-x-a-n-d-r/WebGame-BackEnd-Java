package com.AK.controllers;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.AK.models.CustomUserDetails;
import com.AK.models.Kingdom;
import com.AK.models.User;
import com.AK.models.kingdomDTO.KingdomDTO;
import com.AK.models.resources.Food;
import com.AK.models.resources.Gold;
import com.AK.models.resources.Resource;
import com.AK.repositories.KingdomRepository;
import com.AK.repositories.ResourceRepository;
import com.AK.repositories.UserRepository;
import com.AK.services.JwtService;
import java.util.ArrayList;
import java.util.List;
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
class KingdomControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private KingdomRepository kingdomRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ResourceRepository resourceRepository;

  @Test
  void GETShouldReturnCorrectResources() throws Exception {

    Kingdom kingdom = new Kingdom("kingdom");
    kingdomRepository.save(kingdom);

    User user = new User("Tomas", "12345678", kingdom);
    userRepository.save(user);

    Resource resourceFood = new Food(12, 1, 156L, kingdom);
    Resource resourceGold = new Gold(156, 1, 586L, kingdom);
    resourceRepository.save(resourceFood);
    resourceRepository.save(resourceGold);

    List<Resource> resourceList = new ArrayList<>(List.of(resourceFood, resourceGold));
    kingdom.setResources(resourceList);

    String token = jwtService.generateToken(new CustomUserDetails(user));

    mockMvc.perform(get("/kingdom/resources").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.resources[0].type", is("Food")))
        .andExpect(jsonPath("$.resources[1].type", is("Gold")))
        .andExpect(jsonPath("$.resources[0].amount", is(12)))
        .andExpect(jsonPath("$.resources[1].amount", is(156)))
        .andExpect(jsonPath("$.resources[0].generation", is(1)))
        .andExpect(jsonPath("$.resources[1].generation", is(1)))
        .andExpect(jsonPath("$.resources[0].updatedAt", is(156)))
        .andExpect(jsonPath("$.resources[1].updatedAt", is(586)))
        .andDo(print());
  }

  @Test
  void GETShouldReturnKingdomHasNoResourcesException() throws Exception {
    Kingdom kingdom = new Kingdom();
    kingdomRepository.save(kingdom);

    User user = new User("Tomas", "12345678", kingdom);
    userRepository.save(user);

    String token = jwtService.generateToken(new CustomUserDetails(user));

    mockMvc.perform(get("/kingdom/resources").header("Authorization", "Bearer " + token))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.message", is("Kingdom has no resources.")))
        .andDo(print());
  }

  @Test
  void GETShouldReturnKingdomNotFoundException() throws Exception {

    Kingdom kingdom = new Kingdom("kingdom");
    kingdomRepository.save(kingdom);

    User user = new User("Tomas", "12345678", kingdom);
    userRepository.save(user);

    String token = jwtService.generateToken(new CustomUserDetails(user));

    user.setKingdom(null);
    userRepository.save(user);
    kingdom.setUser(null);
    kingdomRepository.save(kingdom);
    kingdomRepository.delete(kingdom);

    mockMvc.perform(get("/kingdom/resources").header("Authorization", "Bearer " + token))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.message", is("Kingdom not found.")))
        .andDo(print());
  }

  @Test
  void getShouldreturnKingdomDTO() throws Exception {

    Kingdom kingdom = new Kingdom(1L, "apolo", null, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    kingdomRepository.save(kingdom);

    User user = new User("Tomas", "12345678", kingdom);
    userRepository.save(user);

    kingdom.setUser(user);
    kingdomRepository.save(kingdom);

    String token = jwtService.generateToken(new CustomUserDetails(user));

    KingdomDTO kingdomDTO = new KingdomDTO(kingdom);

    mockMvc.perform(get("/kingdom/1").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.name", is("apolo")))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.userId", is(1)))
        .andDo(print());
  }

  @Test
  void getShouldthrowIdRequiredException() throws Exception {

    Kingdom kingdom = new Kingdom(1L, "apolo", null, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    kingdomRepository.save(kingdom);

    User user = new User("Tomas", "12345678", kingdom);
    userRepository.save(user);

    kingdom.setUser(user);
    kingdomRepository.save(kingdom);

    String token = jwtService.generateToken(new CustomUserDetails(user));

    mockMvc.perform(get("/kingdom/").header("Authorization", "Bearer " + token))
        .andExpect(status().isNotFound())
        .andDo(print());
  }
}
