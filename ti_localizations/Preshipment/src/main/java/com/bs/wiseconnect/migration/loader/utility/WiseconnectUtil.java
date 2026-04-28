package com.bs.wiseconnect.migration.loader.utility;

import com.misys.tiplus2.services.control.ServiceResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;

public class WiseconnectUtil
{
  public static Logger logger = Logger.getLogger("WiseconnectUtil");
  public static Properties property = null;
 
  public static boolean isValidObject(Object object)
  {
    if (object == null) {
      return false;
    }
    return true;
  }
 
  public static boolean isValidString(String string)
  {
    if ((string == null) || ("".equalsIgnoreCase(string))) {
      return false;
    }
    return true;
  }
 
  public static boolean isValidList(List list)
  {
    if ((isValidObject(list)) && (list.size() > 0)) {
      return true;
    }
    return false;
  }
 
  public static java.util.Date getLocalTime()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new java.util.Date());
    return cal.getTime();
  }
 
  public static String getLocalTimeInSwiftFormat()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    return sdf.format(new java.util.Date());
  }
 
  public static Timestamp getLocalDateTime()
  {
    java.util.Date date = new java.util.Date();
    long t = date.getTime();
    Timestamp sqlTimestamp = new Timestamp(t);
    return sqlTimestamp;
  }
 
  public static java.util.Date getLocalDate()
  {
    return new java.util.Date();
  }
 
  public static XMLGregorianCalendar getLocalDateInXMLGregorian()
    throws Exception
  {
    XMLGregorianCalendar result = null;
    try
    {
      java.util.Date dateTime = getLocalDate();
      Calendar cal = Calendar.getInstance();
      cal.setTime(dateTime);
      int year = cal.get(1);
      int month = cal.get(2);
      int date = cal.get(5);
      int hour = cal.get(10);
      int minute = cal.get(12);
      int second = cal.get(13);
     
      result = DatatypeFactory.newInstance().newXMLGregorianCalendar(
        year, month + 1, date, hour, minute, second,
        -2147483648,
        -2147483648);
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static XMLGregorianCalendar getDateInXMLGregorian(java.util.Date dateToBeConvert)
    throws Exception
  {
    XMLGregorianCalendar result = null;
    try
    {
      Calendar cal = Calendar.getInstance();
      cal.setTime(dateToBeConvert);
      int year = cal.get(1);
      int month = cal.get(2);
      int date = cal.get(5);
      int hour = cal.get(10);
      int minute = cal.get(12);
      int second = cal.get(13);
     
      result = DatatypeFactory.newInstance().newXMLGregorianCalendar(
        year, month + 1, date, hour, minute, second,
        -2147483648,
        -2147483648);
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static XMLGregorianCalendar getDateStringInXMLGregorian(String dateString, String dateInFormat)
    throws Exception
  {
    XMLGregorianCalendar result = null;
    try
    {
      java.util.Date dateToBeConvert = getDateByDateAndFormat(dateString, dateInFormat);
      result = getDateInXMLGregorian(dateToBeConvert);
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static java.sql.Date getSQLLocalDate()
  {
    java.util.Date date = new java.util.Date();
    long t = date.getTime();
    java.sql.Date sqlDate = new java.sql.Date(t);
    return sqlDate;
  }
 
  public static java.util.Date getDateByDateAndFormat(String dateStr, String dateFormat)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    java.util.Date date = null;
    try
    {
      date = sdf.parse(dateStr);
    }
    catch (ParseException e)
    {
      logger.error(e.getMessage(), e);
    }
    return date;
  }
 
  public static java.sql.Date getSqlDateByDateAndFormat(String dateStr, String dateFormat)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    java.util.Date date = null;
    java.sql.Date sqlDate = null;
    try
    {
      date = sdf.parse(dateStr);
      long t = date.getTime();
      sqlDate = new java.sql.Date(t);
    }
    catch (ParseException e)
    {
      logger.error(e.getMessage(), e);
    }
    return sqlDate;
  }
 
  public static Timestamp getTimeStampByDateAndFormat(String dateStr, String dateFormat)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    java.util.Date date = null;
    Timestamp sqlTimestamp = null;
    try
    {
      date = sdf.parse(dateStr);
      long t = date.getTime();
      sqlTimestamp = new Timestamp(t);
    }
    catch (ParseException e)
    {
      logger.error(e.getMessage(), e);
    }
    return sqlTimestamp;
  }
 
  public static String readFile(String filePath)
    throws IOException
  {
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    String line = null;
    StringBuilder stringBuilder = new StringBuilder();
    String ls = System.getProperty("line.separator");
    while ((line = reader.readLine()) != null)
    {
      stringBuilder.append(line);
      stringBuilder.append(ls);
    }
    return stringBuilder.toString();
  }
 
  public static String randomCorrelationId()
  {
    return UUID.randomUUID().toString();
  }
 
  public static String randomLimitRequestId()
  {
    String result = UUID.randomUUID().toString().replace('-', '0');
    result = result.substring(0, 18);
    return result;
  }
 
  public static String getReSubmitCount(String countVal)
  {
    String result = "";
    if (isValidString(countVal))
    {
      Integer count = new Integer(countVal);
      if (isValidObject(count)) {
        result = count.intValue() + 1;
      }
    }
    else
    {
      result = "1";
    }
    return result;
  }
 
  public static String getFileProperties(String keyName)
  {
    if (!isValidObject(property))
    {
      property = new Properties();
      InputStream inputStream = WiseconnectUtil.class.getClassLoader()
        .getResourceAsStream("dummyFile.properties");
      try
      {
        property.load(inputStream);
      }
      catch (IOException e)
      {
        logger.error(e.getMessage(), e);
      }
      catch (NullPointerException e)
      {
        logger.error(e.getMessage(), e);
      }
      catch (Exception e)
      {
        logger.error(e.getMessage(), e);
      }
    }
    return property.getProperty(keyName);
  }
 
  public static String convertResponseXMLToString(ServiceResponse serviceResponse)
  {
    String result = "";
    JAXBContext context = null;
    try
    {
      context = JAXBInstanceInitialiser.getServiceResponseContext();
      result = JAXBTransformUtil.doMarshalling(context, serviceResponse);
    }
    catch (Exception exp)
    {
      logger.error(exp.getMessage(), exp);
    }
    return result;
  }
 
  public static String doMarshalling(JAXBContext context, Object toDoObject)
  {
    String result = "";
    if (isValidObject(toDoObject))
    {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      try
      {
        Marshaller jaxbMarshaller = context.createMarshaller();
        jaxbMarshaller.setProperty("jaxb.formatted.output",
          Boolean.valueOf(true));
        jaxbMarshaller.marshal(toDoObject, outStream);
        result = outStream.toString();
      }
      catch (Exception exp)
      {
        logger.error(exp);
      }
    }
    else
    {
      logger.debug("Marshalling object is not valid");
    }
    return result;
  }
 
  public static boolean writeFile(String filePath, String messageToBeWrite)
  {
    boolean isSucceed = false;
    Writer output = null;
    File file = null;
    try
    {
      file = new File(filePath);
      output = new BufferedWriter(new FileWriter(file));
      if (isValidString(messageToBeWrite)) {
        output.write(messageToBeWrite);
      }
      isSucceed = true;
      logger.debug("Swift file stored in " + filePath);
    }
    catch (Exception ex)
    {
      logger.error(ex.getMessage(), ex);
      isSucceed = false;
      try
      {
        output.close();
      }
      catch (IOException e)
      {
        logger.error(e.getMessage(), e);
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
        logger.error(e.getMessage(), e);
      }
    }
    return isSucceed;
  }
 
  public static String[] getAccountAbodeArray(String accountAbodeString)
  {
    String[] accAbode = null;
    if (isValidString(accountAbodeString)) {
      accAbode = accountAbodeString.split("\\|");
    }
    return accAbode;
  }
 
  public static boolean checkT24PostingStatus(String statusCode)
  {
    boolean isSucceed = false;
    if (isValidString(statusCode)) {
      if (statusCode.indexOf("1") != -1) {
        isSucceed = false;
      } else {
        isSucceed = true;
      }
    }
    return isSucceed;
  }
 
  public static XMLGregorianCalendar dateToXMLGregorianCalendarDate(java.util.Date dateTime)
    throws ParseException, DatatypeConfigurationException
  {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(dateTime);
    XMLGregorianCalendar xmlGC = DatatypeFactory.newInstance()
      .newXMLGregorianCalendarDate(cal.get(1),
      cal.get(2) + 1,
      cal.get(5),
      -2147483648);
    return xmlGC;
  }
 
  public static String getSubStringData(String value, int fromIndex, int toIndex)
  {
    String result = "";
    if (isValidString(value)) {
      result = value.substring(fromIndex, toIndex);
    }
    return result;
  }
 
  public static void replaceTokenByValue(String text, String token, String value)
  {
    if ((isValidString(text)) &&
      (isValidString(value)))
    {
      logger.info(token);
      logger.info(value);
      text = text.replaceAll(token, value);
    }
  }
 
  public static void main(String[] args)
  {
    try
    {
      String aVal = randomLimitRequestId().replace('-', '0');
      logger.info(aVal.substring(0, 18));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}