package com.AK.configuration;

import com.AK.exceptions.defaultValuesExceptions.DefaultValueInvalidInputException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValueNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesFileNotFoundException;
import com.AK.exceptions.defaultValuesExceptions.DefaultValuesZeroValueException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultValues {

  private static DefaultValues instance;
  private final Map<String, String> defaultVariableValues;

  private DefaultValues(String filePath) throws DefaultValuesFileNotFoundException {
    defaultVariableValues = readFile(filePath);
  }

  public static DefaultValues getInstance() throws DefaultValuesFileNotFoundException {
    if (instance == null) {
      instance = new DefaultValues(
          System.getenv("CONFIG_FILE_PATH"));
    }
    return instance;
  }

  private Map<String, String> readFile(String filepath)
      throws DefaultValuesFileNotFoundException {
    HashMap<String, String> map = new HashMap<>();
    String line;
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filepath));
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":", 2);
        if (parts.length == 2) {
          String key = parts[0];
          String value = parts[1];
          map.put(key, value);
        }
      }
      reader.close();
    } catch (IOException e) {
      throw new DefaultValuesFileNotFoundException("Default values file not found");
    }
    return map;
  }

  private Map<String, String> getDefaultVariableValues() {
    return defaultVariableValues;
  }

  public static Integer getDefaultValueInt(String key)
      throws DefaultValueNotFoundException, DefaultValuesFileNotFoundException, DefaultValuesZeroValueException, DefaultValueInvalidInputException {
    String value = getInstance().getDefaultVariableValues().get(key);
    if (value == null) {
      throw new DefaultValueNotFoundException("Key " + key + " not found.");
    }
    if (value.isEmpty()) {
      throw new DefaultValueNotFoundException("Default value for " + key + " is empty.");
    }
    if (value.equals("0")) {
      throw new DefaultValuesZeroValueException(
          "Default value for " + key + " must not be equal to zero");
    }
    if (!value.matches("^[-+]?\\d*$")) {
      throw new DefaultValueInvalidInputException("Value for " + key + " must be a number.");
    }
    if (!value.matches("^\\d*$")) {
      throw new DefaultValueInvalidInputException(
          "Value for " + key + " must be a positive number.");
    }
    if (!value.matches("^[1-9]\\d*$")) {
      throw new DefaultValueInvalidInputException(
          "Numeric value for " + key + " must not start with zero.");
    }
    return Integer.parseInt(value);
  }
}
