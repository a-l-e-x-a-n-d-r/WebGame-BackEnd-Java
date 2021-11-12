package com.AK.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.AK.exceptions.IdRequiredException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.models.Kingdom;
import com.AK.models.User;
import com.AK.repositories.KingdomRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class KingdomServiceUnitTests {

  private KingdomServiceImpl kingdomService;
  private KingdomRepository fakeKingdomRepository;
  private ResourceService resourceService;
  private BuildingStarterPackService buildingStarterPackService;

  @BeforeEach
  void beforeStart() {
    fakeKingdomRepository = Mockito.mock(KingdomRepository.class);
    kingdomService = new KingdomServiceImpl(fakeKingdomRepository, resourceService,buildingStarterPackService);
  }

  @Test
  void tryNullId() {
    assertThrows(IdRequiredException.class, () -> kingdomService.getKingdomById(null));
  }

  @Test
  void tryEmptyKingdom() {
    Mockito.when(fakeKingdomRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(KingdomNotFoundException.class, () -> kingdomService.getKingdomById(1L));
  }

  @Test
  void tryCorrectInput() {

    Kingdom kingdom = new Kingdom(1L, "Kingdom", new User(), new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());

    Mockito.when(fakeKingdomRepository.findById(kingdom.getId())).thenReturn(Optional.of(kingdom));

    fakeKingdomRepository.findById(1L);

    assertEquals(1L, kingdom.getId());
  }
}