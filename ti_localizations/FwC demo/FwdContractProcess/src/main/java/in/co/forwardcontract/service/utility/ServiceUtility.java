package in.co.forwardcontract.service.utility;

import com.infrasoft.kiya.security.EncryptionDecryptionImpl;
import in.co.forwardcontract.utility.DBConnectionUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
 
public class ServiceUtility
{
  private static Logger logger = Logger.getLogger(ServiceUtility.class.getName());
  static EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
  public static LinkedHashMap<String, String> TBProperties = new LinkedHashMap();
  String reqdata;
  String msgid;
  public ServiceUtility(String reqData, String msgId)
  {
    this.msgid = msgId;
    this.reqdata = reqData;
  }
  public static String getBankFinResponse(String bankEncReq, String url)
  {
    String encResponse = null;
    PostMethod post = new PostMethod(url);
    logger.info("Entering getBankFinResponse");
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
      logger.info("Encrypted Response From Bank-->\n" + encResponse);
      logger.info("Exiting getBankFinResponse");
    }
    catch (Exception e)
    {
      logger.info("Exception in getBankFinResponse:- " + e);
      e.printStackTrace();
    }
    finally
    {
      post.releaseConnection();
    }
    return encResponse.trim();
  }
  public static void getProperties()
  {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    logger.info(" Entering getProperties ");
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      String query = "SELECT * FROM Bridgeproperties ";
      pst = con.prepareStatement(query);
      rs = pst.executeQuery();
      while (rs.next()) {
        TBProperties.put(rs.getString("key").trim(), rs.getString("value").trim());
      }
      logger.info(" Size of Bridgeproperties From DB ---->" + TBProperties.size());
      logger.info(" Entering getProperties ");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
  }
  public static String generateEncryptBankRequest(String bankRequestJson, String key)
  {
    String encMes = null;
    logger.info(" Entering generateEncryptBankRequest ");
    try
    {
      encMes = obj.encryptMessage(bankRequestJson, key);
      logger.info(" Exiting generateEncryptBankRequest ");
    }
    catch (Exception e)
    {
      logger.info(" Error in  generateEncryptBankRequest --->" + e.getMessage());
      e.printStackTrace();
    }
    return encMes;
  }
  public static String generateDecryptBankResponse(String bankEncRes, String key)
  {
    String decMes = null;
    logger.info(" Entering generateDecryptBankResponse ");
    try
    {
      decMes = obj.decryptMessage(bankEncRes, key);
      logger.info(" Exiting generateDecryptBankResponse ");
    }
    catch (Exception e)
    {
      logger.info(" Error in  generateDecryptBankResponse --->" + e.getMessage());
      e.printStackTrace();
    }
    return decMes;
  }
  public static Timestamp getSqlLocalDateTime()
  {
    Date date = new Date();
    long t = date.getTime();
    Timestamp sqlTimestamp = new Timestamp(t);
    return sqlTimestamp;
  }
  public static String getBridgePropertyValue(String key)
  {
    logger.info("FWC Getting File Path Method ");
    String value = "";
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      if (con != null)
      {
        String bridgePropQuery = "SELECT ID, ZONE, BRANCH, KEY, VALUE, CATEGORY FROM BRIDGEPROPERTIES WHERE KEY = ? ";
        logger.info("FWC BridgePropQuery : " + bridgePropQuery + " Params[" + key + "]");
        ps = con.prepareStatement(bridgePropQuery);
        ps.setString(1, key);
        rs = ps.executeQuery();
        while (rs.next()) {
          value = rs.getString("VALUE");
        }
        logger.info(" FWC-----------File Location------------JOB Name : VALUE ---->>> " + key + 
          " Location File Saved---------: " + value);
      }
    }
    catch (Exception ex)
    {
      logger.info(
        " FWC-----------File Location----- getBridgePropertyValue--- Exception is :" + ex.getMessage());
      ex.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return value;
  }
}
