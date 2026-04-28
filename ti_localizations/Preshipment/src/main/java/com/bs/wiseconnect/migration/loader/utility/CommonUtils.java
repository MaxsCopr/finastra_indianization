package com.bs.wiseconnect.migration.loader.utility;

import com.bs.wiseconnect.migration.loader.db.connection.WiseconnectDB;
import com.misys.tiplus2.apps.ti.service.common.EnigmaBoolean;
import com.misys.tiplus2.apps.ti.service.common.GWRAdditionalData;
import com.misys.tiplus2.apps.ti.service.common.GWRAdditionalData.DataItem;
import com.misys.tiplus2.apps.ti.service.common.GWRParty;
import com.misys.tiplus2.apps.ti.service.common.ObjectFactory;
import com.prowidesoftware.swift.model.field.Field57A;
import com.prowidesoftware.swift.model.field.Field57D;
import com.prowidesoftware.swift.model.field.Field58A;
import com.prowidesoftware.swift.model.field.Field58D;
import com.prowidesoftware.swift.model.mt.mt7xx.MT754;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;

public class CommonUtils
{
  private static Logger logger = Logger.getLogger(CommonUtils.class.getName());
 
  public static java.util.Date StringtoDate(String name)
  {
    java.sql.Date convertedDate = null;
    try
    {
      SimpleDateFormat aSdfWithoutTimestamp = new SimpleDateFormat(
        "dd/MM/yyyy");
      convertedDate = new java.sql.Date(aSdfWithoutTimestamp.parse(name).getTime());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return convertedDate;
  }
 
  public static Double StringtoDecimal(String name)
  {
    try
    {
      aDouble = new Double(name);
    }
    catch (Exception e)
    {
      Double aDouble;
      return Double.valueOf(0.0D);
    }
    Double aDouble;
    return aDouble;
  }
 
  public static String RemoveDecimal(String name)
  {
    double o_s_amt = Double.parseDouble(name);
   
    return String.valueOf(o_s_amt / 1.0D);
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
 
  public static String convertEnigma(String EnigmaValue)
  {
    if (EnigmaValue.equals("0")) {
      return "false";
    }
    return "true";
  }
 
  public static Long StringtoLong(String name)
  {
    if (name == null) {
      return null;
    }
    Long aint = Long.valueOf(0L);
    try
    {
      aint = Long.valueOf(Long.parseLong(name));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return aint;
  }
 
  public static BigDecimal StringtoBigDecimal(String name)
  {
    if ((name == null) || (name.equals(""))) {
      return null;
    }
    BigDecimal bigDecimal = null;
    try
    {
      bigDecimal = new BigDecimal(name);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return bigDecimal;
  }
 
  public static String LongtoString(Long value)
  {
    String result = String.valueOf(value);
   
    return result;
  }
 
  public static String getCurrentDate()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    java.util.Date date = new java.util.Date();
   
    return dateFormat.format(date);
  }
 
  public static String getCurrentDateTime()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    java.util.Date date = new java.util.Date();
   
    return dateFormat.format(date);
  }
 
  public static java.util.Date getDate()
  {
    java.util.Date date = new java.util.Date();
   
    return date;
  }
 
  public static String ChartoString(char ch)
  {
    String stringValue = Character.toString(ch);
    return stringValue;
  }
 
  public static XMLGregorianCalendar getGregorianDate(String dateString)
  {
    DateFormat formatter = null;
    java.util.Date date = null;
    DatatypeFactory df = null;
    GregorianCalendar gc = new GregorianCalendar();
    if (dateString.charAt(4) == '|') {
      formatter = new SimpleDateFormat("yyyy|MMM|E");
    } else if (dateString.charAt(4) == '/') {
      formatter = new SimpleDateFormat("yyyy/MM/dd");
    } else if (dateString.charAt(2) == '/') {
      formatter = new SimpleDateFormat("dd/MM/yyyy");
    } else {
      formatter = new SimpleDateFormat("yy|MM|dd");
    }
    try
    {
      date = formatter.parse(dateString);
      df = DatatypeFactory.newInstance();
      gc.setTimeInMillis(date.getTime());
    }
    catch (DatatypeConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    System.out.print("Calender : " + df.newXMLGregorianCalendar(gc));
    return df.newXMLGregorianCalendar(gc);
  }
 
  public static XMLGregorianCalendar getXmlGregorianDate(String dateString)
  {
    XMLGregorianCalendar resultGC = null;
    if ((dateString == null) || (dateString.equals(""))) {
      return null;
    }
    DateFormat formatter = null;
    java.util.Date date = null;
    DatatypeFactory df = null;
    GregorianCalendar gc = new GregorianCalendar();
    if (dateString.charAt(4) == '-')
    {
      if (dateString.length() > 10) {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
      }
      formatter = new SimpleDateFormat("yyyy-MM-dd");
    }
    else if (dateString.charAt(4) == '|')
    {
      formatter = new SimpleDateFormat("yyyy|MMM|E");
    }
    else if (dateString.charAt(4) == '/')
    {
      formatter = new SimpleDateFormat("yyyy/MM/dd");
    }
    else if (dateString.charAt(2) == '/')
    {
      formatter = new SimpleDateFormat("dd/MM/yyyy");
    }
    else if (dateString.charAt(2) == '/')
    {
      formatter = new SimpleDateFormat("MM/dd/yyyy");
    }
    else if (dateString.charAt(2) == '-')
    {
      formatter = new SimpleDateFormat("dd-MM-yyyy");
    }
    else
    {
      formatter = new SimpleDateFormat("yyyyMMdd");
    }
    try
    {
      date = formatter.parse(dateString);
     
      df = DatatypeFactory.newInstance();
     
      gc.setTimeInMillis(date.getTime());
     


      resultGC = df.newXMLGregorianCalendar(gc);
     
      resultGC.setTime(-2147483648, -2147483648, -2147483648);
    }
    catch (DatatypeConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return resultGC;
  }
 
  public static String getStringDateInFormat(String date, String dateFormat, String expectedFormat)
  {
    SimpleDateFormat sdf = null;
    String result = null;
    if ((date != null) && (!date.equals(""))) {
      try
      {
        sdf = new SimpleDateFormat(dateFormat);
        java.util.Date aDate = sdf.parse(date);
        sdf = new SimpleDateFormat(expectedFormat);
        result = sdf.format(aDate);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return result;
  }
 
  public static int getStringDecimal(String name)
  {
    int aint = 0;
    if ((name == null) || (name == "")) {
      return aint;
    }
    try
    {
      BigDecimal bd = new BigDecimal(name);
      aint = bd.intValue();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return aint;
  }
 
  public static String getNString(String name)
  {
    String nvalue = null;
    try
    {
      nvalue = name.substring(0, Math.min(name.length(), 2));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return nvalue;
  }
 
  public static String StringTrim(String name)
  {
    String nvalue = null;
    try
    {
      int nameLength = name.length();
      if (nameLength > 35) {
        nvalue = name.substring(0, name.length() - 2);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return nvalue;
  }
 
  public static boolean isBetween(int value, int min, int max)
  {
    return (value > min) && (value < max);
  }
 
  public static <T> JAXBElement<T> nullHandler(JAXBElement<T> value)
  {
    if (value.isNil()) {
      return null;
    }
    return value;
  }
 
  public static EnigmaBoolean EnigmaBooleanHandler(String booleanValue)
  {
    EnigmaBoolean enigmaBoolean = null;
    if ((booleanValue != null) && (!booleanValue.equals(""))) {
      enigmaBoolean = EnigmaBoolean.fromValue(booleanValue);
    }
    return enigmaBoolean;
  }
 
  public static String multiValueHandler(String multiValue)
  {
    if ((multiValue != null) && (!multiValue.equals(""))) {
      multiValue = multiValue.replaceAll("@@", "\n");
    }
    return multiValue;
  }
 
  public static GWRAdditionalData objectHandler(GWRAdditionalData.DataItem dataItem, GWRAdditionalData data)
  {
    for (Field f : dataItem.getClass().getFields()) {
      f.setAccessible(true);
    }
    return data;
  }
 
  public static void printFileds(Object obj)
  {
    Field[] fields = obj.getClass().getFields();
    for (Field field : fields) {
      logger.info("Field Name = " + field.getName());
    }
  }
 
  public static String TIMandatory(String v)
  {
    if ((v == null) || (v.equals(""))) {
      return null;
    }
    return v;
  }
 
  public static String getReconciledAmount(String amount, String currency)
  {
    if ((amount != null) && (!amount.equals("")))
    {
      Connection connection = null;
      PreparedStatement preparedStatement = null;
      ResultSet resultSet = null;
      String decimal = "0";
      BigDecimal bi = null;
      try
      {
        connection = WiseconnectDB.getTIPLUSConnection();
        preparedStatement = connection.prepareCall("SELECT C8CED FROM C8PF WHERE C8CCY=?");
        preparedStatement.setString(1, currency);
       

        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          decimal = resultSet.getString("C8CED");
        }
        logger.info("Amount ->" + amount);
        logger.info("Currency from T24->" + currency);
        logger.info("Decimal for currency ->" + decimal);
        bi = new BigDecimal(amount);
       
        BigDecimal bi1 = new BigDecimal(Math.pow(10.0D, StringtoInt(decimal).intValue()));
       
        bi = bi.divide(bi1);
        amount = bi.toString();
      }
      catch (SQLException ex)
      {
        ex.printStackTrace();
      }
      finally
      {
        MigrationUtil.surrenderConnection(connection, resultSet, preparedStatement);
      }
    }
    return amount;
  }
 
  public static ReconciledAmountBean getReconciledAmountForClaims(String amount, String currency)
  {
    ReconciledAmountBean reconciledAmountBean = new ReconciledAmountBean();
    if ((amount != null) && (!amount.equals("")))
    {
      Connection connection = null;
      PreparedStatement preparedStatement = null;
      ResultSet resultSet = null;
      String decimal = "0";
      BigDecimal bi = null;
      try
      {
        connection = WiseconnectDB.getTIPLUSConnection();
        preparedStatement = connection.prepareCall("SELECT C8CED FROM C8PF WHERE C8CCY=?");
        preparedStatement.setString(1, currency);
       

        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          decimal = resultSet.getString("C8CED");
        }
        bi = new BigDecimal(amount);
       
        BigDecimal bi1 = new BigDecimal(Math.pow(10.0D, StringtoInt(decimal).intValue()));
       
        bi = bi.divide(bi1);
        amount = bi.toString();
       

        reconciledAmountBean.setAmount(amount);
        reconciledAmountBean.setCurrency(currency);
        reconciledAmountBean.setCurrencyDecimal(decimal);
      }
      catch (SQLException ex)
      {
        ex.printStackTrace();
      }
      finally
      {
        MigrationUtil.surrenderConnection(connection, resultSet, preparedStatement);
      }
    }
    return reconciledAmountBean;
  }
 
  public static GWRParty getValueFromIdentification(String input, GWRParty gWRParty)
  {
    ObjectFactory factory = new ObjectFactory();
    if ((input != null) && (!input.equals(""))) {
      if (input.matches("(.*)SW-(.*)")) {
        gWRParty.setSwiftAddress(factory.createGWRPartySwiftAddress(input.substring(3)));
      } else if (input.matches("(.*)CU-(.*)")) {
        gWRParty.setCustomer(factory.createGWRPartyCustomer(input.substring(3)));
      } else if (input.matches("(.*)AD-(.*)")) {
        gWRParty.setNameAddress(multiValueHandler(input.substring(3)));
      }
    }
    return gWRParty;
  }
 
  public static void get58tagFromIdentification(String input, MT754 m)
  {
    if ((input != null) && (!input.equals(""))) {
      if (input.matches("(.*)SW-(.*)"))
      {
        input = input.substring(3);
       
        m.addField(new Field58A(input));
      }
      else if (input.matches("(.*)CU-(.*)"))
      {
        input = input.substring(3);
        m.addField(new Field58D(input));
      }
    }
  }
 
  public static void get57tagFromIdentification(String input, MT754 m)
  {
    if ((input != null) && (!input.equals(""))) {
      if (input.matches("(.*)SW-(.*)"))
      {
        input = input.substring(3);
       
        m.addField(new Field57A(input));
      }
      else if (input.matches("(.*)CU-(.*)"))
      {
        input = input.substring(3);
        m.addField(new Field57D(input));
      }
    }
  }
 
  public static void main(String[] args)
  {
    DBPropertiesLoader.initialize("sourcedb.properties");
   
    logger.info("Amount -> " + getReconciledAmount("720000000056565", "AED"));
  }
}