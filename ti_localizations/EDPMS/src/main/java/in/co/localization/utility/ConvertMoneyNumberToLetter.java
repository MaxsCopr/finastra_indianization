package in.co.localization.utility;
 
public class ConvertMoneyNumberToLetter

{

  public String convertRupeesToWords(String amt)

  {

    String str2 = "";

    NumToWords w = new NumToWords();

    int rupees = Integer.parseInt(amt.split("\\.")[0]);

    String str1 = w.convert(rupees);

    str1 = str1 + " Rupees ";

    int paise = Integer.parseInt(amt.split("\\.")[1]);

    if (paise != 0)

    {

      str2 = str2 + " and";

      str2 = w.convert(paise);

      str2 = str2 + " Paise";

    }

    return str1 + str2 + " Only";

  }

}