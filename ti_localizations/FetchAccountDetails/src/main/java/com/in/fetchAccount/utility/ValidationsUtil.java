package com.in.fetchAccount.utility;

import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

public class ValidationsUtil
{
  private static final Logger logger = Logger.getLogger(ValidationsUtil.class.getName());
 
  public static void main(String[] args)
  {
    System.out.println(">>" + isValidString(null));
  }
 
  public static String checkString(String value)
  {
    logger.debug("checkStringE : " + value);
    String str = "";
    try
    {
      if ((value == null) || (value.isEmpty())) {
        str = "";
      } else {
        str = value;
      }
    }
    catch (Exception e)
    {
      logger.debug("Exceptions! " + e.getMessage());
      e.printStackTrace();
    }
    logger.debug("checkStringExit : " + str);
    return str;
  }
 
  public static boolean isValidString(String checkValue)
  {
    boolean result = false;
    if ((checkValue != null) && (!checkValue.trim().isEmpty())) {
      result = true;
    }
    return result;
  }
 
  public static boolean isValidObject(Object object)
  {
    if (object == null) {
      return false;
    }
    return true;
  }
 
  public static String checkIsNull(String value)
  {
    try
    {
      value = value.trim();
      if ((value.equals(null)) || (value == null) || (value.length() < 1)) {
        return "";
      }
      return value;
    }
    catch (NullPointerException e) {}
    return "";
  }
 
  public static boolean isValidNumber(String str)
  {
    String s = str;
    for (int i = 0; i < s.length(); i++) {
      if (!Character.isDigit(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }
 
  public static boolean isNumeric(String str)
  {
    try
    {
      for (char c : str.toCharArray()) {
        if (!Character.isDigit(c)) {
          return false;
        }
      }
    }
    catch (Exception e)
    {
      logger.error("isNumeric ? exception..!! " + e.getMessage());
      e.printStackTrace();
      return false;
    }
    return true;
  }
 
  public static boolean isValidNumber2(String str)
  {
    boolean b = str.matches("^[+-]?(?=.)\\d*(\\.\\d+)?$");
    return b;
  }
 
  public static boolean isValidList(List list)
  {
    if ((isValidObject(list)) && (list.size() > 0)) {
      return true;
    }
    return false;
  }
 
  public static boolean isValidAmount(Double amountValue)
  {
    if ((isValidObject(amountValue)) && (amountValue.doubleValue() > 0.0D)) {
      return true;
    }
    return false;
  }
 
  public static Object getValidObject(Object objectToCheck)
  {
    return isValidObject(objectToCheck) ? objectToCheck : null;
  }
 
  public static String getValidStringValue(String stringToCheck)
  {
    return isValidString(stringToCheck) ? stringToCheck.trim() : "";
  }
 
  public static String getValidStringValue(String stringToCheck, String stringToAssign)
  {
    return isValidString(stringToCheck) ? stringToCheck.trim() :
      getValidStringValue(stringToAssign);
  }
 
  public static boolean isAmountValid(Double amountValue)
  {
    if ((isValidObject(amountValue)) && (amountValue.doubleValue() == 0.0D)) {
      return true;
    }
    return false;
  }
 
  public static String mapValueToStringOrEmpty(HashMap<?, ?> hashMap, String key)
  {
    Object value = hashMap.get(key);
    return value == null ? "" : value.toString();
  }
 
  public static Timestamp getSqlLocalDateTime()
  {
    Date date = new Date();
    long t = date.getTime();
    Timestamp sqlTimestamp = new Timestamp(t);
    return sqlTimestamp;
  }
}