package com.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHandler {
  private static final String resource = "application.properties";

  public static String getProperties(String key) {
    Properties properties = new Properties();
    try {
      InputStream reader = PropertyHandler.class.getClassLoader().getResourceAsStream(resource);
      properties.load(reader);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties.getProperty(key);
  }
}
