package com.AK.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class Config {

  @Bean
  public BCryptPasswordEncoder encrypt() {
    return new BCryptPasswordEncoder();
  }

}
