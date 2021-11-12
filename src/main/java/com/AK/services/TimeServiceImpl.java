package com.AK.services;

import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  @Override
  public long getTime() {
    return System.currentTimeMillis() / 1000;
  }

  @Override
  public long getTimeAfter(int sec) {
    return (System.currentTimeMillis() / 1000) + sec;
  }

  @Override
  public int getTimeBetween(long from, long to) {
    return (int) (to - from);
  }
}
