package com.rest.util;

import java.util.Properties;

public class ConfigurationUtil
{
  public static String API_END_URL = "";
  public static String ENCRYPT_KEY = "";
  public static String SERVICE = "";
  public static void getDetails()
  {
    Properties param = new Properties();
    try
    {
      param = ProbUtil.getPropertiesValue();
      API_END_URL = param.getProperty("API_END_URL");
      ENCRYPT_KEY = param.getProperty("ENCRYPTION_KEY");
      SERVICE = param.getProperty("SERVICE");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
