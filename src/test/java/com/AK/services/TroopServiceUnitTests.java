package com.AK.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import com.AK.exceptions.BuildingNotFoundException;
import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.exceptions.troopExceptions.AcademyDoesNotBelongToPlayerException;
import com.AK.exceptions.troopExceptions.AcademyNotFoundException;
import com.AK.exceptions.troopExceptions.BuildingIsNotAcademyException;
import com.AK.exceptions.troopExceptions.InvalidTroopTypeException;
import com.AK.exceptions.troopExceptions.TroopNotFoundException;
import com.AK.exceptions.troopExceptions.TroopParametersRequiredException;
import com.AK.models.Kingdom;
import com.AK.models.User;
import com.AK.models.buildings.Academy;
import com.AK.models.buildings.Building;
import com.AK.models.troops.Archer;
import com.AK.models.troops.CreatingTroopRequest;
import com.AK.models.troops.Pikeman;
import com.AK.models.troops.Troop;
import com.AK.repositories.BuildingRepository;
import com.AK.repositories.TroopRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TroopServiceUnitTests {

  private TroopServiceImpl troopService;
  private TroopRepository fakeTroopRepository;
  private BuildingService fakeBuildingService;
  private KingdomService fakeKingdomService;
  private TroopFactory fakeTroopFactory;
  private BuildingRepository fakeBuildingRepository;

  @BeforeEach
  void beforeStart() {
    fakeTroopFactory = Mockito.mock(TroopFactory.class);
    fakeBuildingService = Mockito.mock(BuildingService.class);
    fakeKingdomService = Mockito.mock(KingdomService.class);
    fakeTroopRepository = Mockito.mock(TroopRepository.class);
    fakeBuildingRepository = Mockito.mock(BuildingRepository.class);
    troopService = new TroopServiceImpl(fakeTroopRepository, fakeBuildingService,
        fakeKingdomService, fakeTroopFactory);
  }

  @Test
  void createTroopOk()
      throws IdRequiredException, BuildingNotFoundException, AcademyNotFoundException, InvalidTroopTypeException,
      BuildingIsNotAcademyException, KingdomNotFoundException, AcademyDoesNotBelongToPlayerException,
      TroopParametersRequiredException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, "archer");
    Kingdom kingdom = new Kingdom("kingdom");
    Building academy = new Academy(123L, 123L, kingdom);
    kingdom.setBuildings(new ArrayList<>(List.of(academy)));

    Mockito.when(fakeBuildingService.isBuildingAcademy(any())).thenReturn(true);
    Mockito.when(fakeBuildingService.isAcademyPresent(any())).thenReturn(true);
    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(academy);
    Mockito.when(fakeTroopFactory.createTroop("archer", kingdom))
        .thenReturn(new Archer(1L, 1L, kingdom));

    Troop result = troopService.createTroop(creatingTroopRequest, kingdom.getId());

    assertEquals("Archer", result.getType());
  }

  @Test
  void createTroopReturnBuildingIsNotAcademyException()
      throws IdRequiredException, BuildingNotFoundException, KingdomNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, "archer");
    Kingdom kingdom = new Kingdom("kingdom");
    Building academy = new Academy(123L, 123L, kingdom);
    kingdom.setBuildings(new ArrayList<>(List.of(academy)));

    Mockito.when(fakeBuildingService.isBuildingAcademy(any())).thenReturn(false);
    Mockito.when(fakeBuildingService.isAcademyPresent(any())).thenReturn(true);
    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(academy);

    Exception e = assertThrows(BuildingIsNotAcademyException.class,
        () -> troopService.createTroop(creatingTroopRequest, kingdom.getId()));

    assertEquals("Building is not an academy!", e.getMessage());
  }

  @Test
  void createTroopReturnTroopParametersRequired()
      throws IdRequiredException, BuildingNotFoundException, KingdomNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, "");
    Kingdom kingdom = new Kingdom("kingdom");
    Building academy = new Academy(123L, 123L, kingdom);
    kingdom.setBuildings(new ArrayList<>(List.of(academy)));

    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(academy);

    Exception e = assertThrows(TroopParametersRequiredException.class,
        () -> troopService.createTroop(creatingTroopRequest, kingdom.getId()));

    assertEquals("Missing parameters(s): type/academy id.", e.getMessage());
  }

  @Test
  void createTroopReturnAcademyNotFound()
      throws InvalidTroopTypeException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(1L, "archer");
    Kingdom kingdom = new Kingdom("kingdom");

    Mockito.when(fakeTroopFactory.createTroop("archer", kingdom))
        .thenReturn(new Archer(1L, 1L, kingdom));

    Exception e = assertThrows(AcademyNotFoundException.class,
        () -> troopService.createTroop(creatingTroopRequest, kingdom.getId()));

    assertEquals("Academy not found!", e.getMessage());
  }

  @Test
  void createTroopReturnAcademyDoesNotBelongToPlayer()
      throws IdRequiredException, BuildingNotFoundException, KingdomNotFoundException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    CreatingTroopRequest creatingTroopRequest = new CreatingTroopRequest(2L, "archer");

    Kingdom kingdom = new Kingdom("kingdom");
    User user = new User("Tom", "12345678", kingdom);
    kingdom.setUser(user);
    Building academy = new Academy(123L, 123L, kingdom);
    kingdom.setBuildings(new ArrayList<>(List.of(academy)));

    Kingdom wrongKingdom = new Kingdom("kralovstvi");
    User wrongUser = new User("Tomas", "12345678", wrongKingdom);
    wrongKingdom.setUser(wrongUser);
    Building wrongAcademy = new Academy(123L, 123L, wrongKingdom);
    wrongKingdom.setBuildings(new ArrayList<>(List.of(wrongAcademy)));

    Mockito.when(fakeBuildingService.isAcademyPresent(any())).thenReturn(true);
    Mockito.when(fakeBuildingService.isBuildingAcademy(any())).thenReturn(true);
    Mockito.when(fakeKingdomService.getKingdomById(any())).thenReturn(kingdom);
    Mockito.when(fakeBuildingService.getBuildingById(any())).thenReturn(wrongAcademy);

    Exception e = assertThrows(AcademyDoesNotBelongToPlayerException.class,
        () -> troopService.createTroop(creatingTroopRequest, kingdom.getId()));

    assertEquals("Academy does not belong to player!", e.getMessage());
  }

  @Test
  void getKingdomTroopsReturnsListOfTroops()
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    Kingdom kingdom = new Kingdom("kingdom");
    Troop pikeman = new Pikeman(10L, 70L, kingdom);
    Troop archer = new Archer(10L, 70L, kingdom);

    kingdom.getTroops().add(pikeman);
    kingdom.getTroops().add(archer);

    Mockito.when(fakeTroopRepository.findAllByKingdom(kingdom))
        .thenReturn(kingdom.getTroops());
    assertEquals("Pikeman", troopService.getKingdomTroops(kingdom).getTroops().get(0).getType());
    assertEquals("Archer", troopService.getKingdomTroops(kingdom).getTroops().get(1).getType());
    assertEquals(2,
        troopService.getKingdomTroops(kingdom).getTroops().size());
  }

  @Test
  void getKingdomTroopsReturnsEmptyList()
      throws DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {
    Kingdom kingdom = new Kingdom("kingdom");

    Mockito.when(fakeTroopRepository.findAllByKingdom(kingdom))
        .thenReturn(kingdom.getTroops());
    assertEquals(0, troopService.getKingdomTroops(kingdom).getTroops().size());
    assertTrue(troopService.getKingdomTroops(kingdom).getTroops().isEmpty());
  }

    @Test
    void TryNullId () {
      assertThrows(IdRequiredException.class, () -> troopService.getTroopById(null));
    }

    @Test
    void tryEmptyTroop () {
      Mockito.when(fakeTroopRepository.findById(any())).thenReturn(Optional.empty());
      assertThrows(TroopNotFoundException.class, () -> troopService.getTroopById(1L));
    }

    @Test
    void tryCorrectInput ()
      throws
    DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException
    {
      List buildingList = new ArrayList();
      Kingdom kingdom = new Kingdom(1L, "Kingdom", new User(), buildingList, new ArrayList<>(),
          new ArrayList<>());

      Building academy = new Academy(102L, 102L, kingdom);
      buildingList.add(academy);

      Troop archer = new Archer(102L, 102L, kingdom);
      archer.setId(1L);

      Mockito.when(fakeTroopRepository.findById(archer.getId())).thenReturn(Optional.of(archer));

      fakeTroopRepository.findById(1L);

      assertEquals(1L, archer.getId());
    }
  }