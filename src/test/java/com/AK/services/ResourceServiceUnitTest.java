package com.AK.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.models.Kingdom;
import com.AK.models.resources.Food;
import com.AK.models.resources.Gold;
import com.AK.models.resources.Resource;
import com.AK.repositories.KingdomRepository;
import com.AK.repositories.ResourceRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ResourceServiceUnitTest {

  private ResourceService resourceService;
  private TimeService timeService;
  private ResourceRepository resourceRepository;
  private Kingdom kingdom;
  private List<Resource> resources;
  private KingdomRepository kingdomRepository;
  private KingdomService kingdomService;
  private BuildingStarterPackService buildingStarterPackService;

  @BeforeEach
  void beforeStart() {
    resourceRepository = Mockito.mock(ResourceRepository.class);
    kingdomRepository = Mockito.mock(KingdomRepository.class);
    kingdomService = new KingdomServiceImpl(kingdomRepository, resourceService, buildingStarterPackService);
    timeService = new TimeServiceImpl();
    resourceService = new ResourceServiceImpl(resourceRepository, timeService);
    kingdom = new Kingdom("polo");
    resources = new ArrayList<>();
  }

  @Test
  void returnKingdomWithResources()
      throws KingdomHasNoResourcesException, KingdomNotFoundException {
    Resource food = new Food(50, 1, timeService.getTime(), kingdom);
    Resource gold = new Gold(100, 1, timeService.getTime(), kingdom);

    resourceRepository.save(food);
    resourceRepository.save(gold);

    Mockito.when(resourceRepository.findAllByKingdom(kingdom))
        .thenReturn(resources = List.of(food, gold));

    resourceService.setStarterResources(kingdom);

    assertEquals(50, kingdom.getResources().get(0).getAmount());
    assertEquals(100, kingdom.getResources().get(1).getAmount());
  }

  @Test
  void throwKingdomNotFoundException() {
    Resource food = new Food(50, 1, timeService.getTime(), null);
    Resource gold = new Gold(100, 1, timeService.getTime(), null);

    resourceRepository.save(food);
    resourceRepository.save(gold);

    Mockito.when(resourceRepository.findAllByKingdom(null))
        .thenReturn(resources = List.of(food, gold));
    Mockito.when(kingdomRepository.findByName(any())).thenReturn(null);

    assertThrows(KingdomNotFoundException.class, () -> resourceService.setStarterResources(null));
  }
}
