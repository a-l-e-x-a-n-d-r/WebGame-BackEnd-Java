package com.AK.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import com.AK.exceptions.BuildingDoesNotBelongToKingdomException;
import com.AK.exceptions.BuildingIsAlreadyOnMaxLevelException;
import com.AK.exceptions.BuildingIsNotFinishedException;
import com.AK.exceptions.InvalidBuildingException;
import com.AK.exceptions.BuildingNotFoundException;
import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.KingdomAlreadyHasTownhallException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomHasNotEnoughResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.TownHallNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.models.Kingdom;
import com.AK.models.buildings.Building;
import com.AK.models.buildings.CreateBuildingDTO;
import com.AK.models.buildings.Farm;
import com.AK.models.buildings.TownHall;
import com.AK.repositories.BuildingRepository;
import com.AK.repositories.UserRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BuildingServiceUnitTests {

  private BuildingService fakeBuildingService;
  private BuildingService buildingService;
  private KingdomService fakeKingdomService;
  private BuildingFactory fakeBuildingFactory;
  private BuildingRepository fakeBuildingRepository;
  private TimeService timeService;
  private PurchaseService purchaseService;
  private Kingdom kingdom;
  private UserRepository fakeUserRepository;

  @BeforeEach
  void beforeStart() {

    fakeKingdomService = Mockito.mock(KingdomService.class);
    fakeBuildingFactory = Mockito.mock(BuildingFactory.class);
    fakeBuildingRepository = Mockito.mock(BuildingRepository.class);
    fakeBuildingService = Mockito.mock(BuildingService.class);
    timeService = Mockito.mock(TimeService.class);
    buildingService = new BuildingServiceImpl(fakeBuildingRepository, fakeKingdomService,
        fakeBuildingFactory, timeService, purchaseService);
    kingdom = new Kingdom(1L, "myKingdom", null, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    fakeUserRepository = Mockito.mock(UserRepository.class);
  }

  @Test
  void returnCreateBuildingWhenCreatingBuilding()
      throws BuildingTypeRequiredException, TownHallNotFoundException, IdRequiredException, KingdomNotFoundException, KingdomAlreadyHasTownhallException, DefaultValuesFileNotFoundException, IOException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {

    Building townhall = new TownHall(10L, 70L,
        kingdom);
    kingdom.getBuildings().add(townhall);
    Mockito.when(fakeKingdomService.getKingdomById(kingdom.getId())).thenReturn(kingdom);
    Mockito.when(fakeBuildingFactory.createBuilding("farm", kingdom))
        .thenReturn(new Farm(10L, 100L, kingdom));

    int listSizeBefore = kingdom.getBuildings().size();

    Building result = buildingService
        .createBuilding(new CreateBuildingDTO("farm"), kingdom.getId());

    int listSizeAfter = kingdom.getBuildings().size();

    assertEquals(kingdom, result.getKingdom());
    assertTrue(listSizeBefore < listSizeAfter);
  }

  @Test
  void throwBuildingTypeRequiredExceptionWhenCreatingBuilding()
      throws BuildingTypeRequiredException, IdRequiredException, KingdomNotFoundException, DefaultValuesFileNotFoundException, DefaultValueNotFoundException, DefaultValueInvalidInputException, DefaultValuesZeroValueException {

    Building townhall = new TownHall(10L, 70L, kingdom);
    kingdom.addBuilding(townhall);
    Mockito.when(fakeKingdomService.getKingdomById(kingdom.getId())).thenReturn(kingdom);
    Mockito.when(fakeBuildingFactory.createBuilding("farm", kingdom))
        .thenReturn(new Farm(10L, 100L, kingdom));

    assertThrows(BuildingTypeRequiredException.class,
        () -> buildingService.createBuilding(null, kingdom.getId()));
  }

  @Test
  void throwTownHallNotFoundExceptionWhenCreatingBuilding()
      throws IdRequiredException, KingdomNotFoundException {

    Mockito.when(fakeKingdomService.getKingdomById(kingdom.getId())).thenReturn(kingdom);

    assertThrows(TownHallNotFoundException.class,
        () -> buildingService.createBuilding(new CreateBuildingDTO("farm"), kingdom.getId()));
  }

  @Test
  void getKingdomBuildingsReturnsListOfBuildings()
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Building townhall = new TownHall(10L, 70L, kingdom);
    Building farm = new Farm(10L, 70L, kingdom);

    fakeBuildingRepository.save(townhall);
    fakeBuildingRepository.save(farm);

    kingdom.addBuilding(townhall);
    kingdom.addBuilding(farm);

    Mockito.when(fakeBuildingRepository.findAllByKingdom(kingdom))
        .thenReturn(kingdom.getBuildings());

    assertEquals("townhall", kingdom.getBuildings().get(0).getType());
    assertEquals("farm", kingdom.getBuildings().get(1).getType());
    assertEquals(2,
        buildingService.getKingdomBuildings(kingdom).getBuildings().size());
  }

  @Test
  void upgradeBuildingOk()
      throws KingdomHasNoResourcesException, IdRequiredException, KingdomHasNotEnoughResourcesException, BuildingNotFoundException, KingdomNotFoundException, BuildingIsAlreadyOnMaxLevelException, BuildingDoesNotBelongToKingdomException, TownHallNotFoundException, BuildingIsNotFinishedException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    kingdom.setId(1L);

    Building townhall = new TownHall(1L, 2L, kingdom);
    kingdom.addBuilding(townhall);
    townhall.setFinishedAt(10L);
    fakeBuildingRepository.save(townhall);

    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(townhall);
    Mockito.when(fakeBuildingRepository.findById(any())).thenReturn(Optional.of(townhall));
    Mockito.when(timeService.getTime()).thenReturn(20L);

    Building result = buildingService.upgradeBuilding(1L, 1L);

    assertEquals(2, result.getLevel());
  }

  @Test
  void upgradingReturnsTownhallNotFound()
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException, KingdomHasNoResourcesException, KingdomHasNotEnoughResourcesException, BuildingIsAlreadyOnMaxLevelException, BuildingDoesNotBelongToKingdomException, TownHallNotFoundException, BuildingIsNotFinishedException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    kingdom.setId(1L);

    Building townhall = new TownHall(1L, 2L, kingdom);
    townhall.setFinishedAt(10L);
    fakeBuildingRepository.save(townhall);

    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(townhall);
    Mockito.when(fakeBuildingRepository.findById(any())).thenReturn(Optional.of(townhall));

    Exception e = assertThrows(TownHallNotFoundException.class,
        () -> buildingService.upgradeBuilding(1L, 1L));

    assertEquals("This kingdom doesn't have a Townhall.", e.getMessage());
  }

  @Test
  void upgradingReturnsBuildingDoesNotBelongToKingdom()
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    kingdom.setId(1L);

    Building townhall = new TownHall(1L, 2L, kingdom);
    fakeBuildingRepository.save(townhall);
    kingdom.addBuilding(townhall);

    Kingdom secondKingdom = new Kingdom("Second Kingdom");
    secondKingdom.setId(2L);
    Building secondTownhall = new TownHall(1L, 2L, secondKingdom);
    secondKingdom.addBuilding(secondTownhall);
    fakeBuildingRepository.save(secondTownhall);

    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(secondTownhall);
    Mockito.when(fakeBuildingRepository.findById(any())).thenReturn(Optional.of(secondTownhall));

    Exception e = assertThrows(BuildingDoesNotBelongToKingdomException.class,
        () -> buildingService.upgradeBuilding(2L, 1L));

    assertEquals("Provided Id must belong to the kingdom.", e.getMessage());
  }

  @Test
  void upgradingReturnsBuildingIsAlreadyOnMaxLevelException()
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    kingdom.setId(1L);

    Building townhall = new TownHall(1L, 2L, kingdom);
    kingdom.addBuilding(townhall);
    townhall.setLevel(20);
    fakeBuildingRepository.save(townhall);

    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(townhall);
    Mockito.when(fakeBuildingRepository.findById(any())).thenReturn(Optional.of(townhall));

    Exception e = assertThrows(BuildingIsAlreadyOnMaxLevelException.class,
        () -> buildingService.upgradeBuilding(1L, 1L));

    assertEquals("Maximum level is 20.", e.getMessage());
  }

  @Test
  void upgradingReturnsLevelIsHigherThanTheTownhallsLevel()
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    kingdom.setId(1L);

    Building townhall = new TownHall(1L, 2L, kingdom);
    kingdom.addBuilding(townhall);
    townhall.setLevel(2);
    townhall.setId(1L);
    fakeBuildingRepository.save(townhall);

    Building farm = new Farm(1L, 2L, kingdom);
    kingdom.addBuilding(farm);
    farm.setLevel(2);
    farm.setId(2L);
    fakeBuildingRepository.save(farm);

    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(farm);
    Mockito.when(fakeBuildingRepository.findById(any())).thenReturn(Optional.of(farm));
    Mockito.when(timeService.getTime()).thenReturn(20L);

    Exception e = assertThrows(BuildingIsAlreadyOnMaxLevelException.class,
        () -> buildingService.upgradeBuilding(2L, 1L));

    assertEquals("Level is higher than the Townhall's level", e.getMessage());
  }

  @Test
  void upgradingReturnsOnlyFinishedBuildingsCanBeUpgraded()
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    kingdom.setId(1L);

    Building townhall = new TownHall(1L, 30L, kingdom);
    kingdom.addBuilding(townhall);
    townhall.setLevel(2);
    townhall.setId(1L);
    fakeBuildingRepository.save(townhall);

    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(townhall);
    Mockito.when(fakeBuildingRepository.findById(any())).thenReturn(Optional.of(townhall));
    Mockito.when(timeService.getTime()).thenReturn(20L);

    Exception e = assertThrows(BuildingIsNotFinishedException.class,
        () -> buildingService.upgradeBuilding(2L, 1L));

    assertEquals("Only already finished buildings can be upgraded.", e.getMessage());
  }

  @Test
  void upgradingReturnsTownhallIsBeingUpgraded()
      throws IdRequiredException, KingdomNotFoundException, BuildingNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    kingdom.setId(1L);

    Building townhall = new TownHall(1L, 30L, kingdom);
    kingdom.addBuilding(townhall);
    townhall.setLevel(2);
    townhall.setId(1L);
    fakeBuildingRepository.save(townhall);

    Building farm = new Farm(1L, 2L, kingdom);
    kingdom.addBuilding(farm);
    farm.setLevel(1);
    farm.setId(2L);
    fakeBuildingRepository.save(farm);

    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(farm);
    Mockito.when(fakeBuildingRepository.findById(any())).thenReturn(Optional.of(farm));
    Mockito.when(timeService.getTime()).thenReturn(20L);

    Exception e = assertThrows(BuildingIsNotFinishedException.class,
        () -> buildingService.upgradeBuilding(2L, 1L));

    assertEquals("Upgrading is restricted while townhall is being upgraded.", e.getMessage());
  }

@Test
  void belongBuildingToUser()
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    Long id = kingdom.getId();
    Building townhall = new TownHall(10L, 70L, kingdom);

    assertEquals(id, townhall.getKingdom().getId());
  }

  @Test
  void throwNotBelongBuildingToUser()
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Long id = kingdom.getId();

    Kingdom kingdom1 = new Kingdom("apolo");
    Building townhall = new TownHall(10L, 70L, kingdom1);

    assertThrows(InvalidBuildingException.class,
        () -> buildingService.validateBuildingOwner(id, townhall));
  }
}

