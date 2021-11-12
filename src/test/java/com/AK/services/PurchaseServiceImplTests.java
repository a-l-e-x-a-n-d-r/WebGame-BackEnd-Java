package com.AK.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.AK.exceptions.BuildingTypeRequiredException;
import com.AK.exceptions.BuildingValidTypeRequiredException;
import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.NotEnoughGoldException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import com.AK.exceptions.troopExceptions.InvalidTroopTypeException;
import com.AK.models.Kingdom;
import com.AK.models.buildings.Building;
import com.AK.models.buildings.Farm;
import com.AK.models.buildings.TownHall;
import com.AK.models.resources.Gold;
import com.AK.models.resources.Resource;
import com.AK.models.troops.Troop;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PurchaseServiceImplTests {

  private PurchaseService purchaseService;
  private ResourceService resourceService;
  private Kingdom kingdom;

  @BeforeEach
  void beforeStart() {

    resourceService = Mockito.mock(ResourceService.class);
    purchaseService = new PurchaseServiceImpl(resourceService);
    kingdom = new Kingdom("myKingdom");
    kingdom.setId(1L);

  }

  @Test
  void purchaseNewBuildingSuccess()
      throws NotEnoughGoldException, BuildingTypeRequiredException, KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Resource gold = new Gold(10000, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    purchaseService.purchaseNewBuilding(kingdom, "farm");

    int result = gold.getAmount();

    assertEquals(9900, result);
  }

  @Test
  void purchaseNewBuildingFailureThrowsNotEnoughGoldForBuildingException()
      throws KingdomHasNoResourcesException {

    Resource gold = new Gold(0, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    assertThrows(NotEnoughGoldException.class,
        () -> purchaseService.purchaseNewBuilding(kingdom, "farm"));
  }

  @Test
  void purchaseNewBuildingFailureThrowsBuildingTypeRequiredException()
      throws KingdomHasNoResourcesException {

    Resource gold = new Gold(0, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    assertThrows(BuildingTypeRequiredException.class,
        () -> purchaseService.purchaseNewBuilding(kingdom, ""));
  }

  @Test
  void purchaseUpgradeFarmSuccess()
      throws BuildingTypeRequiredException, KingdomHasNoResourcesException, NotEnoughGoldException, BuildingValidTypeRequiredException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Resource gold = new Gold(10000, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Building farm = new Farm(1L, 1L, kingdom);

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    purchaseService.purchaseBuildingUpgrade(kingdom, farm);

    int result = gold.getAmount();

    assertEquals(9800, result);
  }

  @Test
  void purchaseForUpgradeTownhallSuccess()
      throws KingdomHasNoResourcesException, NotEnoughGoldException, BuildingTypeRequiredException, BuildingValidTypeRequiredException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Resource gold = new Gold(10000, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Building townhall = new TownHall(1L, 1L, kingdom);

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    purchaseService.purchaseBuildingUpgrade(kingdom, townhall);

    int result = gold.getAmount();

    assertEquals(9600, result);
  }

  @Test
  void purchaseUpgradeBuildingFailureThrowsNotEnoughGoldForBuildingException()
      throws KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Resource gold = new Gold(0, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Building farm = new Farm(1L, 1L, kingdom);

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    assertThrows(NotEnoughGoldException.class,
        () -> purchaseService.purchaseBuildingUpgrade(kingdom, farm));
  }

  @Test
  void purchaseUpgradeBuildingFailureThrowsBuildingTypeRequiredException()
      throws KingdomHasNoResourcesException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Resource gold = new Gold(10000, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Building farm = new Farm(1L, 1L, kingdom);
    farm.setType("notValidType");

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    assertThrows(BuildingTypeRequiredException.class,
        () -> purchaseService.purchaseBuildingUpgrade(kingdom, farm));
  }

  @Test
  void purchaseNewTroopSuccess()
      throws KingdomHasNoResourcesException, InvalidTroopTypeException, NotEnoughGoldException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Resource gold = new Gold(10000, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Troop archer = new Troop(1L, "archer", 1, 1, 1, 1, 1L, 1L, false, kingdom) {
    };

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    purchaseService.purchaseNewTroop(kingdom, archer.getType());

    int result = gold.getAmount();

    assertEquals(9975, result);
  }

  @Test
  void purchaseNewTroopFailureThrowsNotEnoughGoldForTroopException()
      throws KingdomHasNoResourcesException {

    Resource gold = new Gold(0, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Troop archer = new Troop(1L, "archer", 1, 1, 1, 1, 1L, 1L, false, kingdom) {
    };

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    assertThrows(NotEnoughGoldException.class,
        () -> purchaseService.purchaseNewTroop(kingdom, archer.getType()));
  }

  @Test
  void purchaseNewTroopFailureThrowsInvalidTroopTypeException()
      throws KingdomHasNoResourcesException {

    Resource gold = new Gold(0, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Troop archer = new Troop(1L, "", 1, 1, 1, 1, 1L, 1L, false, kingdom) {
    };

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    assertThrows(InvalidTroopTypeException.class,
        () -> purchaseService.purchaseNewTroop(kingdom, archer.getType()));
  }

  @Test
  void purchaseTroopUpgradeSuccess()
      throws KingdomHasNoResourcesException, NotEnoughGoldException, InvalidTroopTypeException, DefaultValueInvalidInputException, DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException {

    Resource gold = new Gold(10000, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Troop archer = new Troop(1L, "archer", 1, 1, 1, 1, 1L, 1L, false, kingdom) {
    };

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    purchaseService.purchaseTroopUpgrade(kingdom, archer);

    int result = gold.getAmount();

    assertEquals(9950, result);
  }

  @Test
  void purchaseTroopUpgradeFailureThrowsNotEnoughGoldForTroopException()
      throws KingdomHasNoResourcesException {

    Resource gold = new Gold(0, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Troop archer = new Troop(1L, "archer", 1, 1, 1, 1, 1L, 1L, false, kingdom) {
    };

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    assertThrows(NotEnoughGoldException.class,
        () -> purchaseService.purchaseTroopUpgrade(kingdom, archer));
  }

  @Test
  void purchaseTroopUpgradeFailureThrowsInvalidTroopTypeException()
      throws KingdomHasNoResourcesException {

    Resource gold = new Gold(0, 1, 1, kingdom);
    kingdom.setResources(List.of(gold));
    Troop archer = new Troop(1L, "", 1, 1, 1, 1, 1L, 1L, false, kingdom) {
    };

    Mockito.when(resourceService.findAllByKingdomAndType(any(), any())).thenReturn(gold);

    assertThrows(InvalidTroopTypeException.class,
        () -> purchaseService.purchaseTroopUpgrade(kingdom, archer));
  }
}
