package com.AK.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TimeServiceImplTest {

  @Test
  void getTimeBetween() {
    TimeServiceImpl timeService = new TimeServiceImpl();
    int result = timeService.getTimeBetween(10L, 20L);
    assertEquals(10, result);
  }
}