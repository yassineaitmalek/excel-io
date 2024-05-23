package com.test.config;

import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

  @Bean
  public DateTimeFormatter getDateTimeFormatter() {
    return DateTimeFormatter.ofPattern("yyyyMMdd");
  }

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    return modelMapper;
  }

  @Bean
  public Random getRandom() {
    return new SecureRandom();
  }

}
