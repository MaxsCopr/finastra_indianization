package in.co.localization.utility;
 
import java.util.List;
import org.apache.log4j.Logger;
 
public class ValidationsUtil
{
  private static Logger logger = Logger.getLogger(ValidationsUtil.class.getName());
  public static boolean isValidString(String checkValue)
  {
    boolean result = false;
    if ((checkValue != null) && (!checkValue.trim().isEmpty()) && (!checkValue.trim().equalsIgnoreCase("null"))) {
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
      e.printStackTrace();
      return false;
    }
    return true;
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
  public static boolean isValidNumber2(String str)
  {
    boolean b = str.matches("^[+-]?(?=.)\\d*(\\.\\d+)?$");
    return b;
  }
  public static boolean isValidList(List<?> list)
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
  public static boolean isAmountValid(Double amountValue)
  {
    if ((isValidObject(amountValue)) && (amountValue.doubleValue() == 0.0D)) {
      return true;
    }
    return false;
  }
  public static String checkIsNull(String value)
  {
    try
    {
      value = value.trim();
      if ((value.equals(null)) || (value.equals("null")) || (value.length() < 1)) {
        return "";
      }
      return value;
    }
    catch (NullPointerException e) {}
    return "";
  }
  public static void main(String[] args)
  {
    String str = "-122A";
    boolean b = isValidNumber(str);
    logger.info(Boolean.valueOf(b));
    boolean b2 = isValidNumber2(str);
    logger.info(Boolean.valueOf(b2));
  }
}