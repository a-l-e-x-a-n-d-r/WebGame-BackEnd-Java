package com.AK.controllers;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.AK.models.CustomUserDetails;
import com.AK.models.Kingdom;
import com.AK.models.User;
import com.AK.models.buildings.Academy;
import com.AK.models.buildings.Building;
import com.AK.models.buildings.Farm;
import com.AK.models.troops.CreatingTroopRequest;
import com.AK.models.troops.Troop;
import com.AK.models.troops.dto.TroopDTO;
import com.AK.repositories.BuildingRepository;
import com.AK.repositories.KingdomRepository;
import com.AK.repositories.TroopRepository;
import com.AK.repositories.UserRepository;
import com.AK.services.JwtService;
import com.AK.services.TroopFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
class TroopControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private KingdomRepository kingdomRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TroopRepository troopRepository;

  @Autowired
  private TroopFactory troopFactory;

  @Autowired
  private BuildingRepository buildingRepository;

  private Kingdom kingdom;
  private User user;
  private Building building;
  private String token;

  @BeforeEach
  void beforeStart() {
    kingdom = new Kingdom("kingdom");
    kingdomRepository.save(kingdom);

    user = new User("Tomas", "12345678", kingdom);
    userRepository.save(user);

    token = jwtService.generateToken(new CustomUserDetails(user));
  }

  @Test
  void POSTShouldReturnCorrectResponse() throws Exception {
    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, "archer");

    building = new Academy(1L, 1L, kingdom);
    buildingRepository.save(building);

    mockMvc.perform(post("/kingdom/troops").header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(creatingTroopRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.type", is("Archer")))
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(100)));
  }

  @Test
  void POSTShouldReturnAcademyDoesNotBelongToPlayerException() throws Exception {
    building = new Academy(1L, 1L, kingdom);
    buildingRepository.save(building);

    Kingdom kingdom2 = new Kingdom("Kingdom2");
    kingdomRepository.save(kingdom2);

    User user2 = new User("Pavel", "12345678", kingdom2);
    userRepository.save(user2);

    kingdom2.setUser(user2);
    kingdomRepository.save(kingdom2);

    Building academy = new Academy(1L, 1L, kingdom2);
    academy.setKingdom(kingdom2);

    List<Building> buildings = new ArrayList<>(List.of(academy));
    kingdom2.setBuildings(buildings);
    kingdomRepository.save(kingdom2);

    buildingRepository.save(academy);

    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(2L, "archer");

    mockMvc.perform(post("/kingdom/troops").header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(creatingTroopRequest)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Academy does not belong to player!")))
        .andDo(print());
  }

  @Test
  void POSTShouldReturnTroopParametersRequiredException() throws Exception {
    building = new Academy(1L, 1L, kingdom);
    buildingRepository.save(building);

    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, "");

    mockMvc.perform(post("/kingdom/troops").header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(creatingTroopRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameters(s): type/academy id.")))
        .andDo(print());
  }

  @Test
  void POSTShouldReturnInvalidTroopTypeException() throws Exception {
    building = new Academy(1L, 1L, kingdom);
    buildingRepository.save(building);

    String invalidTroopType = "abc";

    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, invalidTroopType);

    mockMvc.perform(post("/kingdom/troops").header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(creatingTroopRequest)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Invalid troop type: " + invalidTroopType)))
        .andDo(print());
  }

  @Test
  void POSTShouldReturnBuildingIsNotAcademyException() throws Exception {
    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, "archer");

    building = new Farm(1L, 1L, kingdom);
    buildingRepository.save(building);

    mockMvc.perform(post("/kingdom/troops").header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(creatingTroopRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Building is not an academy!")))
        .andDo(print());
  }

  @Test
  void POSTShouldReturnAcademyNotFoundException() throws Exception {
    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, "archer");

    mockMvc.perform(post("/kingdom/troops").header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(creatingTroopRequest)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Academy not found!")))
        .andDo(print());
  }

  @Test
  void getTroopsSuccessReturnsListOfTroops() throws Exception {
    Troop archer = troopFactory.createTroop("archer", kingdom);
    kingdom.addTroops(archer);
    troopRepository.save(archer);

    Troop pikeman = troopFactory.createTroop("pikeman", kingdom);
    kingdom.addTroops(pikeman);
    troopRepository.save(pikeman);

    mockMvc.perform(get("/kingdom/troops").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.troops[0].id", is(1)))
        .andExpect(jsonPath("$.troops[1].id", is(2)))
        .andExpect(jsonPath("$.troops[0].type", is("Archer")))
        .andExpect(jsonPath("$.troops[1].type", is("Pikeman")))
        .andExpect(jsonPath("$.troops[0].level", is(1)))
        .andExpect(jsonPath("$.troops[1].level", is(1)))
        .andExpect(jsonPath("$.troops[0].hp", is(100)))
        .andExpect(jsonPath("$.troops[1].hp", is(100)));
  }

  @Test
  void GETShouldReturnTroopDTO() throws Exception {

    Troop archer = troopFactory.createTroop("archer", kingdom);
    archer.setId(1L);
    troopRepository.save(archer);

    TroopDTO troopDTO = new TroopDTO(archer);

    mockMvc.perform(get("/kingdom/troops/1").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.type", is("Archer")))
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(100)))
        .andDo(print());
  }
}