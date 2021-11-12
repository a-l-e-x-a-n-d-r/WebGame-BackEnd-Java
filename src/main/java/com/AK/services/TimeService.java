package com.AK.services;

public interface TimeService {

  long getTime();

  long getTimeAfter(int sec);

  int getTimeBetween(long from, long to);
}
