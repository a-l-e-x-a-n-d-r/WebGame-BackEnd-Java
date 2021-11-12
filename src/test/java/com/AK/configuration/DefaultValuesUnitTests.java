package com.AK.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import org.junit.jupiter.api.Test;

class DefaultValuesUnitTests {

  @Test
  void defaultValueIsNull() {
    String key = "The one value not to be found";
    Exception e = assertThrows(DefaultValueNotFoundException.class,
        () -> DefaultValues.getDefaultValueInt(key));
    assertEquals("Key " + key + " not found.", e.getMessage());
  }

  @Test
  void defaultValueIsEmpty() {
    String key = "ValueMissing";
    Exception e = assertThrows(DefaultValueNotFoundException.class,
        () -> DefaultValues.getDefaultValueInt(key));
    assertEquals("Default value for " + key + " is empty.", e.getMessage());
  }

  @Test
  void success() throws Exception {
    assertEquals(123, DefaultValues.getDefaultValueInt("TEST"));
  }

  @Test
  void notZero() throws Exception {
    String key = "ZeroValue";
    Exception e = assertThrows(DefaultValuesZeroValueException.class,
        () -> DefaultValues.getDefaultValueInt(key));
    assertEquals("Default value for " + key + " must not be equal to zero", e.getMessage());
  }

  @Test
  void isNumber() throws Exception {
    String key = "NotANumber";
    Exception e = assertThrows(DefaultValueInvalidInputException.class,
        () -> DefaultValues.getDefaultValueInt(key));
    assertEquals("Value for " + key + " must be a number.", e.getMessage());
  }

  @Test
  void numberIsPositive() throws Exception {
    String key = "NegativeNumber";
    Exception e = assertThrows(DefaultValueInvalidInputException.class,
        () -> DefaultValues.getDefaultValueInt(key));
    assertEquals("Value for " + key + " must be a positive number.", e.getMessage());
  }

  @Test
  void numberDoesNotBeginWithZero() throws Exception {
    String key = "NumberStartsWithZero";
    Exception e = assertThrows(DefaultValueInvalidInputException.class,
        () -> DefaultValues.getDefaultValueInt(key));
    assertEquals("Numeric value for " + key + " must not start with zero.", e.getMessage());
  }

  @Test
  void doIGetTheSameInstance() throws Exception {
    DefaultValues instance = DefaultValues.getInstance();
    assertEquals(DefaultValues.getInstance(), instance);
  }
}