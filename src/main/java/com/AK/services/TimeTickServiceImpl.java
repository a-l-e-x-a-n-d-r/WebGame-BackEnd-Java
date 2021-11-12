package com.AK.services;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class TimeTickServiceImpl implements TimeTickService {

  private int tickCounter;

  @Scheduled(fixedRateString = "${TRIBES_GAMETICK_LEN}000")
  @Override
  public void timeTick() {
    System.out.println("Game tick occurred.");
    tickCounter++;
  }

  public int getTickCounter() {
    return tickCounter;
  }
}
