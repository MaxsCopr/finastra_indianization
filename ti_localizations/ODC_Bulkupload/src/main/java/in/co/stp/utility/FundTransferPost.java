package in.co.stp.utility;

import in.co.clf.util.SystemPropertiesUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
 
public class FundTransferPost
{
  static Logger logger = LogManager.getLogger(FundTransferPost.class);
  public static void main(String[] args)
  {
    fundPosting("10000317990", "100", "CUST01", "");
  }
  public static String fundPosting(String limitNumber, String amount, String customer, String batchId)
  {
    String finalStatus = null;
    try
    {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd:HH:mm:ss");
      Date date = new Date();
      String CurrentDate = dateFormat.format(date);
      String CurrentDateTime = dateFormat1.format(date);
      long randomVal = generateRandom(12);

 
 
      logger.info(SystemPropertiesUtil.getFundTransfer());
      URL url = new URL(SystemPropertiesUtil.getFundTransfer());

 
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("PUT");
      conn.setRequestProperty("Content-Type", "application/json");
      String accountNumber = SystemPropertiesUtil.getAccountNumber();
      String input = "{\"InitiateGenericFundTransferRequest\":{\"msgHdr\":{\"msgId\":\"569919387390414\",\"cnvId\":\"553129893347477\",\"bizObjId\":\"PCFC" + 
        randomVal + "\",\"appId\":\"TF\"," + 
        "\"timestamp\":\"" + CurrentDateTime + "\"},\"msgBdy\":{\"initiateOrderReq\":{\"txnId\":\"PCFC" + randomVal + "\"," + 
        "\"dbtrAcctId\":\"" + limitNumber + "\",\"cdtrAcctId\":\"" + accountNumber + "\",\"amt\":\"" + amount + "\",\"ccy\":\"INR\"," + 
        "\"txnTp\":\"IFT\",\"pmtDesc\":\"" + batchId + "\"," + 
        "\"cstId\":\"" + customer + "\",\"onDt\":\"" + CurrentDate + "\"}}}}";

 
 
      logger.info("Input Json" + input);
      OutputStream os = conn.getOutputStream();
      os.write(input.getBytes());
      os.flush();
      conn.getResponseCode();

 
 
      BufferedReader br = new BufferedReader(new InputStreamReader(
        conn.getInputStream()));

 
      StringBuffer totalOutput = new StringBuffer();
      logger.info("Output from Server .... \n");
      String output;
      while ((output = br.readLine()) != null)
      {
        String output;
        logger.info(output);
        totalOutput.append(output);
      }
      String result = totalOutput.toString();
      logger.info("Result" + result);
      finalStatus = json2Object(result);
      conn.disconnect();
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (Exception e1)
    {
      logger.info("Exception----------->" + e1.getMessage());
      e1.printStackTrace();
    }
    return finalStatus;
  }
  public static String json2Object(String input)
  {
    JSONObject object = (JSONObject)JSONValue.parse(input);
    Object headvalue = object.get("InitiateGenericFundTransferResponse");
    object = (JSONObject)JSONValue.parse(headvalue.toString());
    Object bodyValue = object.get("msgHdr");
    object = (JSONObject)JSONValue.parse(bodyValue.toString());
    String stsValue = (String)object.get("rslt");

 
 
 
    logger.info(stsValue);
    return stsValue;
  }
  public static long generateRandom(int length)
  {
    Random random = new Random();
    char[] digits = new char[length];
    digits[0] = ((char)(random.nextInt(9) + 49));
    for (int i = 1; i < length; i++) {
      digits[i] = ((char)(random.nextInt(10) + 48));
    }
    return Long.parseLong(new String(digits));
  }
}
