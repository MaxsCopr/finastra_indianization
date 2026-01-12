package in.co.prishipment.util;
 
import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.util.UUID;
 
public class PreshipUtil

{

  public static void main(String[] args) {}

  public static String readFile(InputStream anInputStream)

  {

    StringBuilder stringBuilder = new StringBuilder();

    BufferedReader reader = null;

    try

    {

      reader = new BufferedReader(new InputStreamReader(anInputStream));

      String line = null;

      String ls = System.getProperty("line.separator");

      while ((line = reader.readLine()) != null)

      {

        stringBuilder.append(line);

        stringBuilder.append(ls);

      }

    }

    catch (IOException e)

    {

      e.printStackTrace();

      if (reader != null) {

        try

        {

          reader.close();

        }

        catch (IOException e)

        {

          e.printStackTrace();

        }

      }

    }

    finally

    {

      if (reader != null) {

        try

        {

          reader.close();

        }

        catch (IOException e)

        {

          e.printStackTrace();

        }

      }

    }

    return stringBuilder.toString();

  }

  public static String randomCorrelationId()

  {

    return UUID.randomUUID().toString();

  }

}