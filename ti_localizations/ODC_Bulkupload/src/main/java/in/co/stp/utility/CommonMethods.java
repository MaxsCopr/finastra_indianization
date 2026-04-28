package in.co.stp.utility;

import com.misys.tiplus2.service.access.ejb.EnigmaServiceAccess;
import com.misys.tiplus2.service.access.ejb.EnigmaServiceAccessHome;
import in.co.clf.util.SystemPropertiesUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CommonMethods
{
  private static Logger logger = LogManager.getLogger(CommonMethods.class
    .getName());
 
  public boolean moveFile(String source, String destination)
    throws InterruptedException
  {
    InputStream inStream = null;
    OutputStream outStream = null;
    boolean result = false;
    try
    {
      File afile = new File(source);
      File bfile = new File(destination);
      if ((afile != null) && (afile.exists()))
      {
        inStream = new FileInputStream(afile);
        outStream = new FileOutputStream(bfile);
       
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inStream.read(buffer)) > 0)
        {
          int length;
          outStream.write(buffer, 0, length);
        }
        inStream.close();
        outStream.close();
       
        afile.setWritable(true);
       
        System.gc();
        Thread.sleep(2000L);
        afile.delete();
       

        result = true;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return result;
  }
 
  public static String nullAndTrimString(String value)
  {
    if (value == null)
    {
      value = "";
      return value;
    }
    return value.trim();
  }
 
  public String setErrorString(String errorString, String currentError)
  {
    if (errorString.length() > 0) {
      errorString = errorString + ",";
    }
    errorString = errorString + currentError;
    return errorString;
  }
 
  public String checkForNullvalue(String value)
  {
    if (value != null) {
      return value;
    }
    return "";
  }
 
  public int retrieveNoOfErrors(String errorString)
  {
    StringTokenizer stringTokenizer = new StringTokenizer(errorString, ",");
    int n = stringTokenizer.countTokens();
    return n;
  }
 
  public String doubleComma(String amount)
  {
    String output = null;
    String[] splitString = null;
    String amountProcess = "";
    String backUpString = "";
    String minus = "";
    if (amount != null)
    {
      if (amount.length() > 0)
      {
        if (amount.charAt(0) == '-')
        {
          amount = amount.substring(1);
          minus = "-";
        }
        splitString = amount.split("\\.");
        if (splitString.length == 2)
        {
          amountProcess = splitString[0];
          backUpString = "." + splitString[1];
        }
        else
        {
          amountProcess = splitString[0];
          backUpString = ".00";
        }
        String number = amountProcess;
        int len = number.length();
        output = number;
        if (len > 3) {
          for (int i = len; i > 0; i -= 2) {
            if (i == len)
            {
              i -= 3;
              String temp = output.substring(0, i);
              String temp1 = output.substring(i);
              output = temp + "," + temp1;
            }
            else
            {
              String temp = output.substring(0, i);
              String temp1 = output.substring(i);
              output = temp + "," + temp1;
            }
          }
        }
      }
      return minus + output + backUpString;
    }
    return "0";
  }
 
  public static String findRequestUrl(String sentence)
  {
    String retval = null;
    String[] tokens = null;
    String splitPattern = "/ats";
    tokens = sentence.split(splitPattern);
    retval = tokens[0];
    return retval;
  }
 
  public String getEmptyIfNull(Object sourceStr)
  {
    return convertIfNull(sourceStr, "");
  }
 
  public static String convertIfNull(Object sourceStr, Object toConvert)
  {
    return isNullValue(sourceStr) ? toConvert.toString() : sourceStr
      .toString();
  }
 
  public static boolean isNullValue(Object obj)
  {
    if (obj == null) {
      return true;
    }
    if ((obj instanceof String)) {
      return ((String)obj).trim().length() == 0;
    }
    if ((obj instanceof Collection)) {
      return ((Collection)obj).size() == 0;
    }
    return false;
  }
 
  public boolean isNull(String value)
  {
    boolean result = false;
    if ((value == null) || (value.equalsIgnoreCase(""))) {
      result = true;
    }
    return result;
  }
 
  public String dateConvert(String D)
  {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
    Date date = null;
    try
    {
      date = format1.parse(D);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    String dateString = format2.format(date);
    dateString = dateString.replace("-", " ");
    logger.info(dateString);
    return dateString;
  }
 
  public String dateConvert_1(String D)
  {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try
    {
      date = format1.parse(D);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    String dateString = format2.format(date);
   
    logger.info(dateString);
    return dateString;
  }
 
  public boolean isValidFormat(String format, String value)
  {
    Date date = null;
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      date = sdf.parse(value);
      if (!value.equals(sdf.format(date))) {
        date = null;
      }
    }
    catch (ParseException ex)
    {
      ex.printStackTrace();
    }
    return date != null;
  }
 
  public String getDateFormat(String tempDate)
  {
    String delimiter = "-";
    try
    {
      if (tempDate != null)
      {
        String str = tempDate;
        String[] temp = str.split(delimiter);
        if (temp != null)
        {
          int len = temp.length;
          if (len == 3)
          {
            String year = temp[2];
            String month = temp[1];
            String date = temp[0];
            tempDate = year + "-" + month + "-" + date;
          }
          else
          {
            tempDate = null;
          }
        }
      }
    }
    catch (Exception e)
    {
      tempDate = null;
    }
    return tempDate;
  }
 
  public String comma(String amount)
  {
    String output = null;
    if (amount != null)
    {
      String number = amount;
      int len = number.length();
      output = number;
      if (len > 3) {
        for (int i = len; i > 0; i -= 2) {
          if (i == len)
          {
            i -= 3;
            String temp = output.substring(0, i);
            String temp1 = output.substring(i);
            output = temp + "," + temp1;
          }
          else
          {
            String temp = output.substring(0, i);
            String temp1 = output.substring(i);
            output = temp + "," + temp1;
          }
        }
      }
      return output;
    }
    return "0";
  }
 
  public boolean isNumeric(String quantity)
  {
    try
    {
      Double.parseDouble(quantity);
    }
    catch (NumberFormatException e)
    {
      return false;
    }
    return true;
  }
 
  public boolean isBigDecimal(String quantity)
  {
    try
    {
      BigDecimal localBigDecimal = new BigDecimal(quantity);
    }
    catch (NumberFormatException e)
    {
      return false;
    }
    return true;
  }
 
  public static String convertTimestampToUIDateFormat(Timestamp timestamp)
  {
    if (timestamp != null)
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
     
      return dateFormat.format(timestamp);
    }
    return null;
  }
 
  public String getDataSign(Object sourceStr)
  {
    return convertSign(sourceStr, "-");
  }
 
  public static String convertSign(Object sourceStr, Object toConvert)
  {
    return signValue(sourceStr) ? toConvert.toString() : sourceStr
      .toString();
  }
 
  public static boolean signValue(Object obj)
  {
    if (obj == null) {
      return true;
    }
    if ((obj instanceof String)) {
      return ((String)obj).trim().length() == 0;
    }
    if ((obj instanceof Collection)) {
      return ((Collection)obj).size() == 0;
    }
    return false;
  }
 
  public double toDouble(Object str)
  {
    double tempVal = 0.0D;
    if (isNullValue(str)) {
      return tempVal;
    }
    try
    {
      tempVal = Double.parseDouble(getEmptyIfNull(str).trim());
    }
    catch (NumberFormatException e)
    {
      tempVal = 0.0D;
    }
    return tempVal;
  }
 
  public String fetchEJBResponse(String xmlToPost)
    throws Exception
  {
    logger.info("Entering Method");
    String result = null;
    String ejbUrl = SystemPropertiesUtil.getEJBURL();
    try
    {
      System.getProperties().put("java.naming.factory.initial",
        "com.ibm.websphere.naming.WsnInitialContextFactory");
      System.getProperties().put("java.naming.provider.url", ejbUrl);
      Context ctx = new InitialContext();
      Object ejbObject = ctx.lookup("ejb/EnigmaServiceAccess");
      EnigmaServiceAccessHome accessBeanHome = (EnigmaServiceAccessHome)
        PortableRemoteObject.narrow(ejbObject, EnigmaServiceAccessHome.class);
      EnigmaServiceAccess accessB = accessBeanHome.create();
      result = accessB.process(xmlToPost);
      logger.info("Debugmessage:: EJB Response inside method-->" + result);
    }
    catch (Exception e)
    {
      logger.info("EJB Exception----->" + e.getMessage());
      throw e;
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String updateAPISERVER(String response, String sequence)
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    String query = null;
    String status = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      DocumentBuilder builder = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder();
      InputSource src = new InputSource();
      src.setCharacterStream(new StringReader(response));
      Document doc = builder.parse(src);
      status = doc.getElementsByTagName("Status").item(0)
        .getTextContent();
     
      response = response.replaceAll("'", "\"");
     
      logger.info("EJB Status for APISequence- " + sequence + "----->" + status);
      query = "UPDATE ETT_CLF_APISERVER SET STATUS=?,RESPONSE=?,UPLOADED=SYSDATE WHERE SEQUENCE=?";
      logger.info("EJB Update Query--->" + query);
      pst = new LoggableStatement(con, query);
      pst.setString(1, status);
      pst.setString(2, response);
      pst.setString(3, sequence);
      pst.executeUpdate();
    }
    catch (Exception e)
    {
      logger.info("EJB catching status Exception----->" + e.getMessage());
      throw e;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
    logger.info("Exiting Method");
    return status;
  }
 
  public String updateInvoiceAudit(String response, String sequence, String invoiceNumber, String batchID)
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    String query = null;
    String status = null;
    String lowRange = getSequenceInvoice();
    try
    {
      con = DBConnectionUtility.getConnection();
      DocumentBuilder builder = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder();
      InputSource src = new InputSource();
      src.setCharacterStream(new StringReader(response));
      Document doc = builder.parse(src);
      status = doc.getElementsByTagName("Status").item(0)
        .getTextContent();
     
      response = response.replaceAll("'", "\"");
     
      logger.info("EJB Status for APISequence- " + sequence + "----->" + status);
      query = "INSERT INTO ETT_INVOICES_AUDIT (SEQUENCE,UPLOADED,INV_SEQUENCE,BATCHID,INVOICE_INDENT_NO,STATUS) VALUES(?,SYSDATE,?,?,?,?)";
     
      logger.info("Invoice Update Query--->" + query);
      pst = new LoggableStatement(con, query);
      pst.setString(1, lowRange);
      pst.setString(2, sequence);
      pst.setString(3, batchID);
      pst.setString(4, invoiceNumber);
      pst.setString(5, status);
      pst.executeUpdate();
    }
    catch (Exception e)
    {
      logger.info("EJB catching status Exception----->" + e.getMessage());
      throw e;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
    logger.info("Exiting Method");
    return status;
  }
 
  public String updateFinanceAudit(String response, String sequence, String invoiceNumber, String batchID)
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    String query = null;
    String status = null;
    String lowRange = getSequenceFinance();
    try
    {
      con = DBConnectionUtility.getConnection();
      DocumentBuilder builder = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder();
      InputSource src = new InputSource();
      src.setCharacterStream(new StringReader(response));
      Document doc = builder.parse(src);
      status = doc.getElementsByTagName("Status").item(0)
        .getTextContent();
     
      response = response.replaceAll("'", "\"");
     
      logger.info("EJB Status for APISequence- " + sequence + "----->" + status);
      query = "INSERT INTO ETT_FINANCES_AUDIT (SEQUENCE,UPLOADED,FIN_SEQUENCE,BATCHID,INVOICE_INDENT_NO,STATUS) VALUES(?,SYSDATE,?,?,?,?)";
     
      logger.info("Finance Update Query--->" + query);
      pst = new LoggableStatement(con, query);
      pst.setString(1, lowRange);
      pst.setString(2, sequence);
      pst.setString(3, batchID);
      pst.setString(4, invoiceNumber);
      pst.setString(5, status);
      pst.executeUpdate();
    }
    catch (Exception e)
    {
      logger.info("Finance catching status Exception----->" + e.getMessage());
      throw e;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
    logger.info("Exiting Method");
    return status;
  }
 
  public int getSequenceInvoice()
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    int sequenceNumber = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, "SELECT ETT_INVOICES_SEQUENCE.NEXTVAL FROM DUAL");
      rs = ps.executeQuery();
      if (rs.next()) {
        sequenceNumber = rs.getInt(1);
      }
    }
    catch (Exception exception)
    {
      throw exception;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return sequenceNumber;
  }
 
  public int getSequenceFinance()
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    int sequenceNumber = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, "SELECT ETT_FINANCES_SEQUENCE.NEXTVAL FROM DUAL");
      rs = ps.executeQuery();
      if (rs.next()) {
        sequenceNumber = rs.getInt(1);
      }
    }
    catch (Exception exception)
    {
      throw exception;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return sequenceNumber;
  }
 
  public static String getUtilizationSum(String r_status, String theirref, String batch)
  {
    String utilization_amount = "";
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    String getUtilizationAMT_query = "SELECT * FROM (SELECT   sum(NVL(STG.UTILIZATION_AMOUNT,' ')) AS UTILIZATION_AMOUNT FROM UBZONE.master mas,  UBZONE.ETTDM_BULKUPLOAD_STAGING STG,   UBZONE.BASEEVENT bev,   UBZONE.EXTEVENT exte,   UBZONE.CPAYMASTER cpm,   UBZONE.EXEMPL30 exe,   UBZONE.MSTRSETTLE mst1,   UBZONE.EXTBRAMAS extbr,   UBZONE.c8pf c82  WHERE mas.KEY97         = bev.MASTER_KEY  AND mas.KEY97           = cpm.KEY97  AND mas.STATUS          = 'LIV'  AND bev.REFNO_PFIX      = 'CRE'  AND mas.EXEMPLAR        = exe.KEY97  AND cpm.PAY_SI          = mst1.KEY97  AND exe.CODE79         IN ('CPCI','CPBI','CPHI')  AND bev.KEY97           = exte.EVENT  AND trim(mas.BHALF_BRN) = extbr.BCODE  AND trim(cpm.RCV_CCY)   = trim(C82.C8CCY)  AND mas.MASTER_REF      = '" +
   

















      r_status + "' " +
      " AND STG.THEIRREF        ='" + theirref + "' " +
      " AND STG.BATCH_NO        = '" + batch + "' " +
      " AND STG.STATUS          ='SUCCESS' " +
      " AND STG.REMITTANCE_NUM  ='" + r_status + "' " +
      " group by STG.REMITTANCE_NUM " +
      " UNION ALL " +
      " SELECT " +
      "  sum(NVL(STG.UTILIZATION_AMOUNT,' ')  )        AS UTILIZATION_AMOUNT " +
      " FROM UBZONE.ETTDM_BULKUPLOAD_STAGING STG, " +
      "  UBZONE.ETT_INW_REMITTANCE REM, " +
      "  UBZONE.EXTBRAMAS EXTBR, " +
      "  UBZONE.c8pf c82 " +
      " WHERE TRIM(REM.REMIT_BRANCH) = EXTBR.BCODE " +
      " AND C82.C8CCY                =REM.REMIT_CURR " +
      " AND REM.REMIT_NO             ='" + r_status + "' " +
      " AND STG.THEIRREF             ='" + theirref + "' " +
      " AND STG.BATCH_NO             ='" + batch + "' " +
      " AND STG.STATUS               ='SUCCESS' " +
      " AND STG.REMITTANCE_NUM       = '" + r_status + "' " +
      " group by STG.REMITTANCE_NUM " +
      " ) ";
    try
    {
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, getUtilizationAMT_query);
      logger.info("Utilization AMount Query----->" + getUtilizationAMT_query);
      rs = ps.executeQuery();
      while (rs.next())
      {
        utilization_amount = rs.getString("UTILIZATION_AMOUNT");
        logger.info("Utilization AMount---->" + utilization_amount);
      }
      logger.info("Utilization AMount---->" + utilization_amount);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return utilization_amount;
  }
 
  public static String getCurrentHost()
  {
    String hostName = null;
    String hostIpAddr = null;
    try
    {
      InetAddress inet = InetAddress.getLocalHost();
      hostName = inet.getHostName();
      hostIpAddr = inet.getHostAddress();
    }
    catch (UnknownHostException e)
    {
      logger.error("UnknownHost Exception! " + e.getMessage());
      e.printStackTrace();
    }
    return hostName;
  }
 
  public static Timestamp getSqlLocalTimestamp()
  {
    Calendar calendar = Calendar.getInstance();
    Date now = calendar.getTime();
    Timestamp currentTimestamp = new Timestamp(now.getTime());
   
    return currentTimestamp;
  }
 
  public static int insertBulkAPIFiles(String batchId, String theirref, String request)
  {
    int upload_Status = 0;
    Connection con = null;
    PreparedStatement pst = null;
    String insertBulkAPIserver = "insert into ett_odc_bulkupload_apiserver (Batch,THEIRREF,REQUEST,status,Upload_Time,node) values (?,?,?,?,?,?) ";
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = con.prepareStatement(insertBulkAPIserver);
      pst.setString(1, batchId);
      pst.setString(2, theirref);
      pst.setString(3, request);
      pst.setString(4, "QUEUED");
      pst.setTimestamp(5, getSqlLocalTimestamp());
      pst.setString(6, getCurrentHost());
      upload_Status = pst.executeUpdate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("EXception--->" + e.getMessage());
      upload_Status = 0;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
    logger.info("Insert bulk upload API server file status--->" + upload_Status);
    return upload_Status;
  }
 
  public static int updateAPIServer(String batchId, String pri_ref, String response, String status)
  {
    Connection con = null;
    LoggableStatement lst = null;
   
    int updateStatus = 0;
    String updateAPIserver = "UPDATE ETT_ODC_BULKUPLOAD_APISERVER SET RESPONSE=? ,STATUS=?,RESPONSE_TIME=? WHERE BATCH=? AND THEIRREF=?";
    try
    {
      con = DBConnectionUtility.getConnection();
      lst = new LoggableStatement(con, updateAPIserver);
      lst.setString(1, response);
      lst.setString(2, status);
      lst.setTimestamp(3, getSqlLocalTimestamp());
      lst.setString(4, batchId);
      lst.setString(5, pri_ref);
      updateStatus = lst.executeUpdate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("EXception--->" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, lst, null);
    }
    logger.info("Updated Status---->" + updateStatus);
    return updateStatus;
  }
 
  public static void main(String[] args)
  {
    insertBulkAPIFiles("7909", "20108471800111", "XML request");
  }
}
