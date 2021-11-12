package com.AK.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TimeTickServiceIntegrationTest {

  @Autowired
  private TimeTickService timeTickService;

  @Test
  public void testTimeTick() {

    timeTickService.timeTick();
    assertTrue(timeTickService.getTickCounter() > 0);
  }
}