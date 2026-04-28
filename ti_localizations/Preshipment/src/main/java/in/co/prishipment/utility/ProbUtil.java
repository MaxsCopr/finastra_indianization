package in.co.prishipment.utility;
 
import java.io.IOException;

import java.io.InputStream;

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

      input = ProbUtil.class.getClassLoader().getResourceAsStream("in/co/prishipment/resources/PreShipment.properties");

      prop.load(input);

    }

    catch (Exception e)

    {

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