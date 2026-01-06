package com.in.fetchAccount.utility;
 
import org.apache.log4j.Logger;
 
public class LogHelper

{

  public static int noOfLinesToBeDisplayed = 15;

  public static void logError(Logger log, Throwable ex)

  {

    StackTraceElement[] ste = ex.getStackTrace();

    for (int i = 0; i < ste.length; i++)

    {

      if (i == noOfLinesToBeDisplayed)

      {

        log.error("\t....... other lines are cropped.");

        return;

      }

      if (i == 0) {

        log.error(ste[i]);

      } else {

        log.error("\t" + ste[i]);

      }

    }

  }

}