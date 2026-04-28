package in.co.FIRC.utility;

import in.co.FIRC.dao.FIRCOurBankDAO;

class AmountToWordConverter
{
  static final String[] tensNames = {
    "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", 
    "Eighty", "Ninrty" };
  static final String[] onesNames = {
    "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", 
    "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", 
    "Sixteen", "Seventeen", "Eighteen", "Nineteen" };
  public String converter(String aStrAmount)
    throws Exception
  {
    String output = "";
    FIRCOurBankDAO dao = null;
    try
    {
      String totalAmount = aStrAmount;
      int index = totalAmount.indexOf(".");
      String beforeDecimal = totalAmount;
      if (index > -1) {
        beforeDecimal = totalAmount.substring(0, index);
      }
      dao = new FIRCOurBankDAO();
      output = dao.evaluateAmount(beforeDecimal);
      if (index > -1)
      {
        String afterDecimal = totalAmount.substring(index + 1);
        if (!afterDecimal.equals("00")) {
          output = output + " and  " + evaluate(afterDecimal) + " Cents ";
        }
      }
    }
    catch (Exception exception)
    {
      throw exception;
    }
    return output;
  }
  public String converter(int iAmount)
    throws Exception
  {
    String output = "";
    Integer i = null;
    try
    {
      i = new Integer(iAmount);
      output = converter(i.toString());
    }
    catch (Exception exception)
    {
      throw exception;
    }
    return output;
  }
  private String evaluate(String text)
    throws Exception
  {
    long number = 0L;
    try
    {
      number = Long.parseLong(text);
    }
    catch (NumberFormatException eNumberFormatException)
    {
      throw eNumberFormatException;
    }
    return evaluate(number);
  }
  private String evaluate(long number)
  {
    long temp = number;
    long billion = temp / 1000000000L;
    temp %= 1000000000L;
    long crore = temp / 10000000L;
    temp %= 10000000L;
    long lakh = temp / 100000L;
    temp %= 100000L;
    long thousands = temp / 1000L;
    temp %= 1000L;
    long hundreds = temp / 100L;
    temp %= 100L;
    StringBuffer result = new StringBuffer(30);
    if (billion > 0L) {
      result.append(evaluate(billion) + " Billion ");
    }
    if (crore > 0L) {
      result.append(evaluate(crore) + " Million ");
    }
    if (lakh > 0L) {
      result.append(evaluate(lakh) + " LAKH ");
    }
    if (thousands > 0L) {
      result.append(evaluate(thousands) + " THOUSAND ");
    }
    if (hundreds > 0L) {
      result.append(evaluate(hundreds) + " HUNDRED ");
    }
    if (temp != 0L)
    {
      if (number >= 100L) {
        result.append(" and ");
      }
      if ((0L < temp) && (temp <= 19L))
      {
        result.append(onesNames[((int)temp)]);
      }
      else
      {
        long tens = temp / 10L;
        long ones = temp % 10L;
        result.append(tensNames[((int)tens)] + " ");
        result.append(onesNames[((int)ones)]);
      }
    }
    if (result.toString().trim().equals("")) {
      result.append(" ");
    }
    return result.toString();
  }
}