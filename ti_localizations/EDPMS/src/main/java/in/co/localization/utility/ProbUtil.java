package in.co.localization.utility;
 
import java.io.InputStream;

import java.util.Properties;
 
public class ProbUtil

{

  public static Properties getPropertiesValue()

  {

    InputStream input = null;

    Properties prop = new Properties();

    try

    {

      input = ProbUtil.class.getClassLoader().getResourceAsStream("in/co/localization/resources/EDPMS.properties");

      prop.load(input);

    }

    catch (Exception e)

    {

      e.printStackTrace();

      try

      {

        if (input != null) {

          input.close();

        }

      }

      catch (Exception e)

      {

        e.printStackTrace();

      }

    }

    finally

    {

      try

      {

        if (input != null) {

          input.close();

        }

      }

      catch (Exception e)

      {

        e.printStackTrace();

      }

    }

    return prop;

  }

}