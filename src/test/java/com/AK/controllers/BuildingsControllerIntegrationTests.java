package com.AK.controllers;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.AK.models.CustomUserDetails;
import com.AK.models.Kingdom;
import com.AK.models.User;
import com.AK.models.buildings.Building;
import com.AK.models.buildings.BuildingDTO;
import com.AK.models.buildings.CreateBuildingDTO;
import com.AK.models.buildings.Farm;
import com.AK.models.buildings.TownHall;
import com.AK.repositories.BuildingRepository;
import com.AK.repositories.KingdomRepository;
import com.AK.repositories.UserRepository;
import com.AK.services.BuildingFactory;
import com.AK.services.BuildingService;
import com.AK.services.JwtService;
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
class BuildingsControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BuildingRepository buildingRepository;

  @Autowired
  private KingdomRepository kingdomRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private BuildingService buildingService;

  @Autowired
  private BuildingFactory buildingFactory;

  private Kingdom kingdom;
  private User user;

  @BeforeEach
  void beforeStart() {
    kingdom = new Kingdom("myKingdom");
    user = new User("Matyas", "12345678", kingdom);
    kingdomRepository.save(kingdom);
    userRepository.save(user);
  }

  @Test
  void createBuildingSuccess() throws Exception {

    Building townhall = buildingFactory.createBuilding("townhall", kingdom);
    townhall.setLevel(10);
    townhall.setKingdom(kingdom);
    buildingRepository.save(townhall);

    kingdom.getBuildings().add(townhall);
    kingdomRepository.save(kingdom);

    CreateBuildingDTO buildingDTO = new CreateBuildingDTO("farm");
    List<Building> buildingList = new ArrayList<>(List.of(townhall));
    kingdom.setBuildings(buildingList);
    String token = jwtService.generateToken(new CustomUserDetails(user));

    mockMvc.perform(
        post("/kingdom/buildings").header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(buildingDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.type", is("farm")))
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(100)));
  }

  @Test
  void createBuildingFailureThrowsBuildingTypeRequiredException() throws Exception {

    String token = jwtService.generateToken(new CustomUserDetails(user));

    mockMvc.perform(
        post("/kingdom/buildings").header("Authorization", "Bearer " + token)
            .contentType("application/json"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): type.")));
  }

  @Test
  void createBuildingFailureThrowsBuildingValidTypeRequiredException() throws Exception {

    Building townhall = buildingFactory.createBuilding("townhall", kingdom);
    townhall.setLevel(10);
    townhall.setKingdom(kingdom);
    buildingRepository.save(townhall);

    String token = jwtService.generateToken(new CustomUserDetails(user));

    mockMvc.perform(
        post("/kingdom/buildings").header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString("matyas")))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Invalid building type.")))
        .andDo(print());
  }

  @Test
  void createBuildingFailureThrowsTownHallNotFoundException() throws Exception {

    CreateBuildingDTO buildingDTO = new CreateBuildingDTO("farm");
    String token = jwtService.generateToken(new CustomUserDetails(user));

    mockMvc.perform(
        post("/kingdom/buildings").header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(buildingDTO)))
        .andExpect(status().isNotAcceptable())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message",
            is("This kingdom doesn't have a Townhall.")))
        .andDo(print());
  }

  @Test
  void createBuildingFailureThrowsThisKingdomAlreadyHasTownhallException() throws Exception {

    Building townhall = buildingFactory.createBuilding("townhall", kingdom);
    townhall.setLevel(10);
    townhall.setKingdom(kingdom);
    buildingRepository.save(townhall);

    String token = jwtService.generateToken(new CustomUserDetails(user));

    mockMvc.perform(
        post("/kingdom/buildings").header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString("townhall")))
        .andExpect(status().isNotAcceptable())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("This kingdom already has a townhall.")))
        .andDo(print());
  }

  @Test
  void getBuildingsSuccessReturnsListOfBuildings() throws Exception {

    String token = jwtService.generateToken(new CustomUserDetails(user));

    Building townhall = buildingFactory.createBuilding("townhall", kingdom);
    townhall.setKingdom(kingdom);
    buildingRepository.save(townhall);

    Building farm = buildingFactory.createBuilding("farm", kingdom);
    farm.setKingdom(kingdom);
    buildingRepository.save(farm);

    mockMvc.perform(get("/kingdom/buildings").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.buildings[0].id", is(1)))
        .andExpect(jsonPath("$.buildings[1].id", is(2)))
        .andExpect(jsonPath("$.buildings[0].type", is("townhall")))
        .andExpect(jsonPath("$.buildings[1].type", is("farm")))
        .andExpect(jsonPath("$.buildings[0].level", is(1)))
        .andExpect(jsonPath("$.buildings[1].level", is(1)))
        .andExpect(jsonPath("$.buildings[0].hp", is(1000)))
        .andExpect(jsonPath("$.buildings[1].hp", is(100)))
        .andDo(print());
  }

  @Test
  void PUTUpgradeBuildingOK() throws Exception {

    String token = jwtService.generateToken(new CustomUserDetails(user));

    Building townhall = new TownHall(1L, 2L, kingdom);
    townhall.setKingdom(kingdom);
    buildingRepository.save(townhall);

    mockMvc.perform(put("/kingdom/buildings/1").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.type", is("townhall")))
        .andExpect(jsonPath("$.level", is(2)))
        .andDo(print());
  }

  @Test
  void PUTReturnsBuildingDoesNotBelongToKingdomException() throws Exception {

    String token = jwtService.generateToken(new CustomUserDetails(user));

    Kingdom secondKingdom = new Kingdom("Second Kingdom");
    User secondUser = new User("Tomas", "123456789", secondKingdom);
    Building townhall = new TownHall(1L, 2L, kingdom);
    buildingRepository.save(townhall);

    kingdom.setUser(secondUser);
    kingdomRepository.save(secondKingdom);

    Building secondTownhall = new TownHall(1L, 2L, secondKingdom);
    secondTownhall.setKingdom(secondKingdom);
    buildingRepository.save(secondTownhall);

    mockMvc.perform(put("/kingdom/buildings/2").header("Authorization", "Bearer " + token))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.message", is("Provided Id must belong to the kingdom.")))
        .andDo(print());
  }

  @Test
  void PUTReturnsBuildingIsAlreadyOnMaxLevelException() throws Exception {

    String token = jwtService.generateToken(new CustomUserDetails(user));

    Building townhall = new TownHall(1L, 2L, kingdom);
    townhall.setLevel(20);
    buildingRepository.save(townhall);

    mockMvc.perform(put("/kingdom/buildings/1").header("Authorization", "Bearer " + token))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.message", is("Maximum level is 20.")))
        .andDo(print());
  }

  @Test
  void PUTReturnsBuildingIsAlreadyOnMaxLevelException2() throws Exception {
    String token = jwtService.generateToken(new CustomUserDetails(user));

    Building townhall = new TownHall(1L, 2L, kingdom);
    Building farm = new Farm(1L, 2L, kingdom);
    buildingRepository.save(townhall);
    buildingRepository.save(farm);

    mockMvc.perform(put("/kingdom/buildings/2").header("Authorization", "Bearer " + token))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.message", is("Level is higher than the Townhall's level")))
        .andDo(print());
  }

  @Test
  void PUTBuildingIsNotFinishedException() throws Exception {
    String token = jwtService.generateToken(new CustomUserDetails(user));

    Building townhall = new TownHall(1L, 9632578763L, kingdom);
    buildingRepository.save(townhall);

    mockMvc.perform(put("/kingdom/buildings/1").header("Authorization", "Bearer " + token))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.message", is("Only already finished buildings can be upgraded.")))
        .andDo(print());
  }

  @Test
  void PUTBuildingIsNotFinishedException2() throws Exception {
    String token = jwtService.generateToken(new CustomUserDetails(user));

    Building townhall = new TownHall(1L, 9632578763L, kingdom);
    Building farm = new Farm(1L, 2L, kingdom);
    townhall.setLevel(2);
    buildingRepository.save(townhall);
    buildingRepository.save(farm);

    mockMvc.perform(put("/kingdom/buildings/2").header("Authorization", "Bearer " + token))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().contentType("application/json"))
        .andExpect(
            jsonPath("$.message", is("Upgrading is restricted while townhall is being upgraded.")))
        .andDo(print());
  }

  @Test
  void getShouldReturnBuildingDTO()
      throws Exception {

    String token = jwtService.generateToken(new CustomUserDetails(user));

    Building townhall = buildingFactory.createBuilding("townhall", kingdom);
    townhall.setKingdom(kingdom);
    buildingRepository.save(townhall);

    BuildingDTO buildingDTO = new BuildingDTO(townhall);

    mockMvc.perform(get("/kingdom/buildings/1").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.type", is("townhall")))
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(1000)))
        .andDo(print());
  }
}

