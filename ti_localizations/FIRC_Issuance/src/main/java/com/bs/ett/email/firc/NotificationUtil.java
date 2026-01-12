package com.bs.ett.email.firc;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class NotificationUtil
{
  private static Logger logger = LogManager.getLogger(NotificationUtil.class.getName());
  public static String CONFIG_KEY_COLUMN = "KEY";
  public static String CONFIG_VALUE_COLUMN = "VALUE";
  public static String CONFIG_SELECT_QUERY = "SELECT * FROM BRIDGEPROPERTIES ";
  public static final String EMAIL = "EMAIL";
  public static final String ZONE = "ZONE1";
  public static final String SOURCE_SYSTEM = "ZONE1";
  public static final String TARGET_SYSTEM = "SMTP";
  public static String SMTP_HOST = "EmailHost";
  public static String SMTP_PORT = "EmailPort";
  public static String EMAIL_USER = "EmailUser";
  public static String EMAIL_PASSWORD = "EmailPassword";
  public static HashMap<String, String> getBridgePropertiesConfigMap()
  {
    Statement statement = null;
    ResultSet resultSet = null;
    Connection connection = null;
    HashMap<String, String> map = new HashMap();
    try
    {
      connection = DatabaseUtility.getWiseConnection();
      statement = connection.createStatement();
      resultSet = statement.executeQuery(CONFIG_SELECT_QUERY);
      while (resultSet.next()) {
        map.put(resultSet.getString(CONFIG_KEY_COLUMN), resultSet.getString(CONFIG_VALUE_COLUMN));
      }
    }
    catch (SQLException e)
    {
      logger.error("FIRC Bridgeproperties error " + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DatabaseUtility.surrenderConnection(connection, statement, resultSet);
    }
    return map;
  }
  public static String getCurrentHost()
  {
    String hostName = null;
    try
    {
      InetAddress inet = InetAddress.getLocalHost();
      hostName = inet.getHostName();
    }
    catch (UnknownHostException e)
    {
      logger.error("FIRC UnknownHost Exception! " + e.getMessage());
      e.printStackTrace();
    }
    return hostName;
  }
  public static Timestamp getSqlLocalTimestamp()
  {
    Calendar calendar = Calendar.getInstance();
    Date now = calendar.getTime();
    Timestamp currentTimestamp = new Timestamp(now.getTime());
    return currentTimestamp;
  }
  public static boolean isValidEmailAddress(String email)
  {
    boolean result = true;
    try
    {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    }
    catch (AddressException ex)
    {
      result = false;
    }
    return result;
  }
  public static String openFileToString(byte[] bytes)
  {
    String file_string = "";
    for (int i = 0; i < bytes.length; i++) {
      file_string = file_string + (char)bytes[i];
    }
    return file_string;
  }
  public static void BlobPDF()
  {
    try
    {
      if (new File("D://Blob.pdf").exists())
      {
        Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler D://Blob.pdf");
        p.waitFor();
      }
      else
      {
        logger.info("File is not exists");
      }
      logger.info("Done");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  public static Date getLocalDate()
  {
    return new Date();
  }
  public static void main(String[] args) {}
}
