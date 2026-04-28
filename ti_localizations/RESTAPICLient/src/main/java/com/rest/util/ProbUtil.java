package com.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
 
public class ProbUtil
{
  public static Properties getPropertiesValue()
    throws IOException
  {
    InputStream input = null;
    Properties prop = new Properties();
    try
    {
      input = ProbUtil.class.getClassLoader().getResourceAsStream("com/rest/util/API.properties");
      prop.load(input);
    }
    catch (Exception e)
    {
      System.out.println("---------------------------------------------");
      e.printStackTrace();
    }
    finally
    {
      if (input != null) {
        input.close();
      }
    }
    return prop;
  }
}
