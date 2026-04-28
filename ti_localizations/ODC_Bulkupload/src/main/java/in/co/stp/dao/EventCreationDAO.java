package in.co.stp.dao;

import in.co.stp.utility.ActionConstants;
import in.co.stp.utility.CommonMethods;
import in.co.stp.utility.DBConnectionUtility;
import in.co.stp.utility.LoggableStatement;
import in.co.stp.vo.ExcelDataVO;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
public class EventCreationDAO
  implements ActionConstants
{
  static EventCreationDAO dao;
  private static Logger logger = LogManager.getLogger(EventCreationDAO.class.getName());
  public static EventCreationDAO getDAO()
  {
    if (dao == null) {
      dao = new EventCreationDAO();
    }
    return dao;
  }
  public String generateXmlTFOutwColNew(ArrayList<ExcelDataVO> transactionList)
    throws ParserConfigurationException
  {
    logger.info("Entering Method");
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();
    String xmlStr = null;
    String xmlStatus = null;
    ExcelDataVO excelDataVO = null;
    CommonMethods commonMethods = null;
    int i = 0;
    try
    {
      commonMethods = new CommonMethods();
      String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns4:ServiceRequest xmlns=\"urn:common.service.ti.apps.tiplus2.misys.com\" xmlns:ns2=\"urn:messages.service.ti.apps.tiplus2.misys.com\" xmlns:ns3=\"urn:custom.service.ti.apps.tiplus2.misys.com\" xmlns:ns4=\"urn:control.services.tiplus2.misys.com\"><ns4:RequestHeader><ns4:Service>TIBulk</ns4:Service><ns4:Operation>Item</ns4:Operation><ns4:Credentials><ns4:Name>IDFCBANK</ns4:Name></ns4:Credentials><ns4:ReplyFormat>FULL</ns4:ReplyFormat><ns4:NoRepair>N</ns4:NoRepair><ns4:NoOverride>N</ns4:NoOverride><ns4:CorrelationId>CorrelationId</ns4:CorrelationId></ns4:RequestHeader><ns2:ItemRequest><ns4:ServiceRequest><ns4:RequestHeader><ns4:Service>TI</ns4:Service><ns4:Operation>TFCOLNEW</ns4:Operation><ns4:Credentials><ns4:Name>IDFCBANK</ns4:Name></ns4:Credentials><ns4:ReplyFormat>FULL</ns4:ReplyFormat><ns4:NoRepair>N</ns4:NoRepair><ns4:NoOverride>N</ns4:NoOverride></ns4:RequestHeader>";

 
 
 
      String xml2 = "</ns4:ServiceRequest></ns2:ItemRequest></ns4:ServiceRequest>";
      for (i = 0; i < transactionList.size(); i++)
      {
        excelDataVO = (ExcelDataVO)transactionList.get(i);
        if (excelDataVO != null)
        {
          Element rocdoc01 = document.createElement("ns2:TFCOLNEW");
          document.appendChild(rocdoc01);
          Element rocdoc02 = document.createElement("ns2:Context");
          rocdoc01.appendChild(rocdoc02);
        }
      }
      xmlStr = convertDocumentToString(document);
      logger.info("XML File-------->" + xmlStr);
      xmlStr = xmlStr.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
      String finalXml = xml1 + xmlStr + xml2;
      logger.info("Final XML File-------->" + finalXml);
      logger.info("final xml-->" + finalXml);
      xmlStatus = commonMethods.fetchEJBResponse(finalXml);
    }
    catch (Exception e)
    {
      logger.info("Error in Element tag" + e.getMessage());
    }
    logger.info("Exiting Method");
    return xmlStatus;
  }
  public static String convertDocumentToString(Document doc)
  {
    logger.info("Entering Method");
    TransformerFactory tf = TransformerFactory.newInstance();
    try
    {
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty("omit-xml-declaration", "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      return writer.getBuffer().toString();
    }
    catch (TransformerException e)
    {
      e.printStackTrace();
      logger.info("Exiting Method");
    }
    return null;
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
  public String pushToAPISERVER(String xmlToPost, String operation)
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    String lowRange = getSequenceAPISERVER();
    try
    {
      con = DBConnectionUtility.getConnection();
      String insert_apiserver = "insert into apiserver(SEQUENCE,VERSION,SERVICE,OPERATION,REQUEST,STATUS) VALUES(?,(CAST(SYSDATE AS TIMESTAMP)),'TI',?,?,'WAITING')";
      ps = new LoggableStatement(con, insert_apiserver);
      ps.setString(1, lowRange);
      ps.setString(2, operation);
      ps.setString(3, xmlToPost);
      logger.info("Query " + ps.getQueryString());
      ps.executeUpdate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return lowRange;
  }
  public int getSequenceAPISERVER()
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    int sequenceNumber = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, "SELECT MAX(TO_NUMBER(SEQUENCE)) FROM APISERVER");
      rs = ps.executeQuery();
      if (rs.next()) {
        sequenceNumber = rs.getInt(1);
      }
      sequenceNumber++;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return sequenceNumber;
  }
  public String checkStatus(String sequence)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String status = null;
    String response = null;
    try
    {
      for (;;)
      {
        con = DBConnectionUtility.getConnection();
        String query = "SELECT STATUS,RESPONSE FROM APISERVER WHERE SEQUENCE=?";
        pst = new LoggableStatement(con, query);
        pst.setString(1, sequence);
        logger.info(pst.getQueryString());
        rs = pst.executeQuery();
        if (!rs.next()) {
          break;
        }
        status = rs.getString("STATUS");
        response = rs.getString("RESPONSE");
        logger.info("RESPONSE--->" + response);
        if ((status.equalsIgnoreCase("SUCCEEDED")) || (status.equalsIgnoreCase("FAILED")))
        {
          logger.info("STATUS--->" + status);
          break;
        }
        Thread.sleep(5000L);
      }
      logger.info("FINAL STATUS--->" + status);
    }
    catch (Exception e)
    {
      logger.info("Error in settosdf" + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return status;
  }
}
