package in.co.ebrc.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import org.apache.log4j.Logger;
 
public class UtilityGenerateCard
{
  private static Logger logger = Logger.getLogger(UtilityGenerateCard.class
    .getName());
  public String generateCard(int prefix)
  {
    logger.info("Entering Method");
    Random rand = new Random();
    int number = rand.nextInt(10000000) + 10000000;
    String num = String.valueOf(number);
    num = prefix + num + 2;
    int length = num.length();
    int credit = generateCheckSum(num);
    num = num.substring(0, length - 1);
    logger.info("Exiting Method");
    return num + credit;
  }
  private static String getDigitsOnly(String s)
  {
    logger.info("Entering Method");
    StringBuffer digitsOnly = new StringBuffer();
    for (int j = 0; j < s.length(); j++)
    {
      char c = s.charAt(j);
      if (Character.isDigit(c)) {
        digitsOnly.append(c);
      }
    }
    logger.info("Exiting Method");
    return digitsOnly.toString();
  }
  private static int generateCheckSum(String num)
  {
    logger.info("Entering Method");
    String digitsOnly = getDigitsOnly(num);
    int sum = 0;
    int digit = 0;
    int addend = 0;
    boolean timesTwo = false;
    for (int k = digitsOnly.length() - 1; k >= 0; k--)
    {
      digit = Integer.parseInt(digitsOnly.substring(k, k + 1));
      if (timesTwo)
      {
        addend = digit * 2;
        if (addend > 9) {
          addend -= 9;
        }
      }
      else
      {
        addend = digit;
      }
      sum += addend;
      timesTwo = !timesTwo;
    }
    sum -= 2;
    int modulus = sum % 10;
    logger.info("Exiting Method");
    if (modulus != 0) {
      return 10 - modulus;
    }
    return 0;
  }
  public static String getErrorDesc(String errorCD, String screenId)
  {
    String errorMsg = "";
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT ERROR_MSG FROM ETT_ERROR_CODES WHERE ERROR_CODE=? AND SCREEN_ID=?";
      pst = con.prepareStatement(query);
      pst.setString(1, errorCD);
      pst.setString(2, screenId);
      rs = pst.executeQuery();
      if (rs.next()) {
        errorMsg = rs.getString(1);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return errorMsg;
  }
}