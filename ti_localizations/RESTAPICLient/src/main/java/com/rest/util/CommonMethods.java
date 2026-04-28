package com.rest.util;

import com.infrasoft.kiya.security.EncryptionDecryptionImpl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.UUID;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class CommonMethods
{
  static EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
  public static LinkedHashMap<String, String> TBProperties = new LinkedHashMap();
 
  public static String generateEncryptBankRequest(String bankRequestJson, String key)
  {
    String encMes = null;
    System.out.println(" Entering generateEncryptBankRequest ");
    try
    {
      encMes = obj.encryptMessage(bankRequestJson, key);
      System.out.println(" Exiting generateEncryptBankRequest ");
    }
    catch (Exception e)
    {
      System.out.println(" Error in  generateEncryptBankRequest --->" + e.getMessage());
      e.printStackTrace();
    }
    return encMes;
  }
 
  public static String generateDecryptBankResponse(String bankEncRes, String key)
  {
    String decMes = null;
    System.out.println(" Entering generateDecryptBankResponse ");
    try
    {
      decMes = obj.decryptMessage(bankEncRes, key);
      System.out.println(" Exiting generateDecryptBankResponse ");
    }
    catch (Exception e)
    {
      System.out.println(" Error in  generateDecryptBankResponse --->" + e.getMessage());
      e.printStackTrace();
    }
    return decMes;
  }
 
  public static void getProperties()
  {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    System.out.println(" Entering getProperties ");
    try
    {
      con = getWiseConnection();
      String query = "SELECT * FROM Bridgeproperties ";
      pst = con.prepareStatement(query);
      rs = pst.executeQuery();
      while (rs.next()) {
        TBProperties.put(rs.getString("key").trim(), rs.getString("value").trim());
      }
      System.out.println(" Exiting getProperties ");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      surrenderConnection(con, pst, rs);
    }
  }
 
  public static String getBankFinResponse(String bankEncReq, String url)
  {
    String encResponse = null;
    StringBuffer buffer = new StringBuffer();
    PostMethod post = new PostMethod(url);
    System.out.println("Entering getBankFinResponse");
    try
    {
      StringRequestEntity requestEntity = new StringRequestEntity(bankEncReq, "application/json", "utf-8");
      post.setRequestEntity(requestEntity);
      HttpClient httpclient = new HttpClient();
     
      int result = httpclient.executeMethod(post);
      if (result != 200) {
        throw new Exception("Server returned code " + result);
      }
      encResponse = post.getResponseBodyAsString();
      System.out.println("Encrypted Response From Bank-->\n" + encResponse);
     
      System.out.println("Exiting getBankFinResponse");
    }
    catch (Exception e)
    {
      System.out.println("Exception in getBankFinResponse:- " + e);
      e.printStackTrace();
    }
    finally
    {
      post.releaseConnection();
    }
    return encResponse.trim();
  }
 
  public static String returnEmptyIfNull(String Value)
  {
    if (Value == null) {
      Value = "";
    }
    return Value;
  }
 
  public static String returnZeroIfEmpty(String Value)
  {
    if (Value == "") {
      Value = "0.00";
    }
    return Value;
  }
 
  public static String getTiDateFormat(String date)
    throws ParseException
  {
    SimpleDateFormat tiFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat jsonFormat = new SimpleDateFormat("dd-MM-yyyy");
    if ((date != null) && (!date.equalsIgnoreCase("")))
    {
      Date d1 = jsonFormat.parse(date);
      date = tiFormat.format(d1);
    }
    else
    {
      date = "";
    }
    return date;
  }
 
  public static Connection getWiseConnection()
  {
    Connection connection = null;
    try
    {
      Properties param = new Properties();
     

      param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
     


      Context initialContext = new InitialContext(param);
      DataSource dataSource = (DataSource)initialContext.lookup("jdbc/wiseconnect");
      connection = dataSource.getConnection();
    }
    catch (NamingException e)
    {
      System.out.println("UBI Suppport -ThemeBridge JNDI NamingException! " + e.getMessage());
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      System.out.println("UBI Suppport -ThemeBridge JNDI SQLException! " + e.getMessage());
      e.printStackTrace();
    }
    return connection;
  }
 
  public static Connection getTizoneConnection()
  {
    Connection connection = null;
    try
    {
      Properties param = new Properties();
      param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
     
      Context initialContext = new InitialContext(param);
      DataSource dataSource = (DataSource)initialContext.lookup("jdbc/zone");
      connection = dataSource.getConnection();
    }
    catch (NamingException e)
    {
      System.out.println("TIZONE JNDI NamingException! " + e.getMessage());
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      System.out.println("TIZONE JNDI SQLException! " + e.getMessage());
      e.printStackTrace();
    }
    return connection;
  }
 
  public static void surrenderConnection(Connection conn, Statement stmt, ResultSet res)
  {
    try
    {
      if (isValidObject(res)) {
        res.close();
      }
    }
    catch (SQLException e)
    {
      System.out.println("Close Resultset Failed! " + e.getMessage());
      e.printStackTrace();
    }
    try
    {
      if (isValidObject(stmt)) {
        stmt.close();
      }
    }
    catch (SQLException e)
    {
      System.out.println("Close Statement Failed! " + e.getMessage());
      e.printStackTrace();
    }
    try
    {
      if (isValidObject(conn)) {
        conn.close();
      }
    }
    catch (SQLException e)
    {
      System.out.println("Close Connection Failed! " + e.getMessage());
      e.printStackTrace();
    }
  }
 
  public static boolean isValidObject(Object object)
  {
    if (object == null) {
      return false;
    }
    return true;
  }
 
  public static Integer StringtoInt(String name)
  {
    if (name == null) {
      return null;
    }
    Integer aint = Integer.valueOf(0);
    try
    {
      aint = new Integer(name);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return aint;
  }
 
  public static boolean writeFile(String filePath, String textContent)
  {
    boolean isSucceed = false;
    Writer output = null;
    File file = null;
    try
    {
      file = new File(filePath);
      output = new BufferedWriter(new FileWriter(file));
      if (isValidString(textContent)) {
        output.write(textContent);
      }
      isSucceed = true;
    }
    catch (Exception ex)
    {
      System.out.println("Exception " + ex.getMessage());
      ex.printStackTrace();
      isSucceed = false;
      try
      {
        output.close();
      }
      catch (IOException e)
      {
        System.out.println("Exception " + e.getMessage());
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        output.close();
      }
      catch (IOException e)
      {
        System.out.println("Exception " + e.getMessage());
        e.printStackTrace();
      }
    }
    return isSucceed;
  }
 
  public static boolean isValidString(String checkValue)
  {
    boolean result = false;
    if ((checkValue != null) && (!checkValue.trim().isEmpty())) {
      result = true;
    }
    return result;
  }
 
  public static Timestamp getSqlLocalDateTime()
  {
    Date date = new Date();
    long t = date.getTime();
    Timestamp sqlTimestamp = new Timestamp(t);
    return sqlTimestamp;
  }
 
  public static String readFileInpStream(InputStream inpStream)
    throws IOException
  {
    String result = "";
    BufferedReader buffReader = null;
    try
    {
      String line = null;
      StringBuilder strBuilder = new StringBuilder();
      String newLineSeparator = System.getProperty("line.separator");
      buffReader = new BufferedReader(new InputStreamReader(inpStream));
     
      int lineCount = 1;
      while ((line = buffReader.readLine()) != null)
      {
        if (lineCount > 1) {
          strBuilder.append(newLineSeparator);
        }
        strBuilder.append(line);
        lineCount++;
      }
      result = strBuilder.toString();
    }
    catch (IOException ex)
    {
      System.out.println("IOException " + ex.getMessage());
      ex.printStackTrace();
      try
      {
        if (buffReader != null) {
          buffReader.close();
        }
      }
      catch (IOException ex)
      {
        System.out.println("BufferedReader close exception! " + ex.getMessage());
        ex.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (buffReader != null) {
          buffReader.close();
        }
      }
      catch (IOException ex)
      {
        System.out.println("BufferedReader close exception! " + ex.getMessage());
        ex.printStackTrace();
      }
    }
    return result;
  }
 
  public static String randomCorrelationId()
  {
    return UUID.randomUUID().toString();
  }
 
  public static String readFile(String filePath)
    throws Exception
  {
    String result = "";
    System.out.println("Read Filepath: " + filePath);
   
    String line = null;
    FileReader fileReader = null;
    BufferedReader buffReader = null;
    StringBuilder strBuilder = new StringBuilder();
    String newLineSeparator = System.getProperty("line.separator");
    try
    {
      fileReader = new FileReader(filePath);
      buffReader = new BufferedReader(fileReader);
     
      int lineCount = 1;
      while ((line = buffReader.readLine()) != null)
      {
        if (lineCount > 1) {
          strBuilder.append(newLineSeparator);
        }
        strBuilder.append(line);
        lineCount++;
      }
      result = strBuilder.toString();
    }
    catch (Exception ex)
    {
      System.out.println("FileReader exception " + ex.getMessage());
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (fileReader != null) {
          fileReader.close();
        }
      }
      catch (Exception ex1)
      {
        System.out.println("FileReader close exception! " + ex1.getMessage());
        ex1.printStackTrace();
      }
      try
      {
        if (buffReader != null) {
          buffReader.close();
        }
      }
      catch (Exception ex2)
      {
        System.out.println("BufferedReader close exception! " + ex2.getMessage());
        ex2.printStackTrace();
      }
    }
    return result;
  }
}
