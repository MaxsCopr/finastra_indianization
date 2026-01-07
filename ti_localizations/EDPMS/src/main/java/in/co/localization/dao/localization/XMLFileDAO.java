package in.co.localization.dao.localization;

import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.vo.localization.ExcelDataVO;
import in.co.localization.vo.localization.InvoiceDetailsVO;
import in.co.localization.vo.localization.ShippingDetailsVO;
import in.co.localization.vo.localization.XMLFileVO;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;

public class XMLFileDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static XMLFileDAO dao;
  private static Logger logger = Logger.getLogger(XMLFileDAO.class.getName());
  int invoicecount = 0;
  HttpSession session = ServletActionContext.getRequest().getSession();
  XMLFileVO xmlFileVO1;
 
  public static XMLFileDAO getDAO()
  {
    if (dao == null) {
      dao = new XMLFileDAO();
    }
    return dao;
  }
 
  public XMLFileVO readXMLFileData(XMLFileVO xmlFileVO)
  {
    logger.info("-------------readXMLFileData--------");
   
    File xmlFileUpload = null;
    String xmlFileName = null;
    String xmlFileFormat = null;
    String fileName = null;
    try
    {
      xmlFileUpload = xmlFileVO.getFileUpload();
      xmlFileName = xmlFileVO.getFileName();
      xmlFileFormat = FilenameUtils.getExtension(xmlFileName);
     

      logger.info("Extension format------>" + xmlFileFormat);
      if (xmlFileFormat.equalsIgnoreCase("xml"))
      {
        String fileNamewithEx = xmlFileName.substring(0, xmlFileName.lastIndexOf('.'));
       
        int pos = fileNamewithEx.lastIndexOf(".");
        if (pos > 0) {
          fileName = fileNamewithEx.substring(0, fileNamewithEx.lastIndexOf('.'));
        }
        String tempFile = "";
       
        tempFile = xmlFileName.substring(0, xmlFileName.lastIndexOf('.'));
       
        int removedot = tempFile.indexOf(".") + 1;
       
        String file = tempFile.substring(removedot, tempFile.length());
       
        int pos1 = tempFile.lastIndexOf(".");
        if (pos1 > 0) {
          fileName = tempFile.substring(0, tempFile.lastIndexOf('.'));
        } else {
          fileName = file;
        }
        fileName = fileName + file;
       
        logger.info("File name--------------------->" + fileName);
        if ((file.equalsIgnoreCase("passrodack")) || (file.equalsIgnoreCase("failrodack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadRodFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passpenack")) || (file.equalsIgnoreCase("failpenack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadPenFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passprnack")) || (file.equalsIgnoreCase("failprnack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadPrnFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if (file.equalsIgnoreCase("mdf"))
        {
          logger.info("MDF Enters-----------------------------");
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadMdfFiles(xmlFileUpload, fileName, xmlFileVO);
          logger.info("xmlfilevo" + xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passtrrack")) || (file.equalsIgnoreCase("failtrrack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadTrrFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passwsnack")) || (file.equalsIgnoreCase("failwsnack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadWsnFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passirmack")) || (file.equalsIgnoreCase("failirmack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadIRMFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passirfircack")) || (file.equalsIgnoreCase("failirfircack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadIRFIRCFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passirmexack")) || (file.equalsIgnoreCase("failirmexack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadIRMEXTFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passirclsack")) || (file.equalsIgnoreCase("failirclsack")))
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadIRMCLSFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else
        {
          xmlFileVO.setXmlFormat(file);
          xmlFileVO = xmlUploadEbrcFiles(xmlFileUpload, fileName, xmlFileVO);
        }
      }
      else
      {
        xmlFileVO.setResult("EX");
        return xmlFileVO;
      }
      logger.info("xmlFileVO.getResult() - " + xmlFileVO.getResult());
    }
    catch (Exception exception)
    {
      logger.info("exception occurred - " + exception.getMessage());
      logger.info("exception occurred - " + exception.getMessage());
    }
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadRodFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("--------------xmlUploadRodFiles-----------------");
    int rodship = 0;
    int numberbill = 0;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
     
      DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
      Date date = new Date();
      String File = "";
      LoggableStatement pst1 = new LoggableStatement(con,
        "SELECT FILE_NAME FROM ETT_ROD_ACK_FILES WHERE FILE_NAME=?");
      pst1.setString(1, fileName);
      ResultSet rs1 = pst1.executeQuery();
      logger.info("Uploaded File Name-----Query------->" + pst1.getQueryString());
      if (rs1.next()) {
        File = rs1.getString("FILE_NAME");
      }
      closeStatementResultSet(pst1, rs1);
     
      logger.info("Uploaded File Name------------>" + fileName);
      if (File.equals(fileName))
      {
        xmlFileVO.setResult("N");
      }
      else
      {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       
        Document doc = docBuilder.parse(xmlFileUpload);
       
        doc.getDocumentElement().normalize();
       
        NodeList chksum = doc.getElementsByTagName("checkSum");
       
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() == 1)
        {
          Element firstPersonElement = (Element)firstPersonNode;
         
          NodeList ShippingBills = firstPersonElement.getElementsByTagName("noOfShippingBills");
         
          Element firstNameElement = (Element)ShippingBills.item(0);
         
          NodeList noofshipbill = firstNameElement.getChildNodes();
         
          String noofshipbills = noofshipbill.item(0).getTextContent().trim();
         
          numberbill = Integer.parseInt(noofshipbills);
         
          int fiNo = 0;
         
          LoggableStatement pst2 = new LoggableStatement(con, "SELECT RODACK_SEQNO.NEXTVAL FROM DUAL");
         
          ResultSet rs2 = pst2.executeQuery();
          if (rs2.next()) {
            fiNo = rs2.getInt("nextval");
          }
          closeStatementResultSet(pst2, rs2);
         
          LoggableStatement pst3 = new LoggableStatement(con,
            "INSERT INTO ETT_ROD_ACK_FILES(FILE_NO,NO_OF_SHIPPING_BILLS,FILE_NAME,STATUS,PROCESSED_DATE) values(?,?,?,?,TO_DATE(?,'DD-MM-YYYY'))");
          pst3.setInt(1, fiNo);
          pst3.setString(2, noofshipbills);
          pst3.setString(3, fileName);
          pst3.setString(4, "Y");
          pst3.setString(5, dateFormat.format(date));
          int rsq = 0;
          rsq = pst3.executeUpdate();
          logger.info("Inserted Count---->" + rsq);
          closePreparedStatement(pst3);
         
          NodeList shbill = doc.getElementsByTagName("shippingBill");
         
          int totalshbill = shbill.getLength();
         
          int count = totalshbill;
         
          int failingcount = 0;
         
          List failBillList = new ArrayList();
          for (int y = 0; y < totalshbill; y++)
          {
            Node nNode = shbill.item(y);
            if (nNode.getNodeType() == 1)
            {
              Element eElement = (Element)nNode;
             
              String portcode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("portCode").item(0).getTextContent())
                .trim();
             
              String exporttype = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("exportType").item(0).getTextContent())
                .trim();
             
              String recordindicator = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("recordIndicator").item(0).getTextContent())
                .trim();
             
              String shipbillno = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shippingBillNo").item(0).getTextContent())
                .trim();
             
              String shipbilldate = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shippingBillDate").item(0).getTextContent())
                .trim().replace("/", "");
             
              String formno = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("formNo").item(0).getTextContent())
                .trim();
             
              String leodate = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("LEODate").item(0).getTextContent())
                .trim();
             
              String iecode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("IECode").item(0).getTextContent())
                .trim();
             
              String changedcode = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("changedIECode").item(0).getTextContent())
                .trim();
             
              String adcode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("adCode").item(0).getTextContent())
                .trim();
             
              String adexportagency = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("adExportAgency").item(0).getTextContent())
                .trim();
             
              String dispatchindicator = commonMethods.getEmptyIfNull(
                eElement.getElementsByTagName("directDispatchIndicator").item(0).getTextContent())
                .trim();
             
              String adno = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("adBillNo").item(0).getTextContent())
                .trim();
             
              String dateneg = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("dateOfNegotiation").item(0).getTextContent())
                .trim();
             
              String buyername = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("buyerName").item(0).getTextContent())
                .trim();
             
              String buyercountry = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("buyerCountry").item(0).getTextContent())
                .trim();
             
              String errorcodes = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("errorCodes").item(0).getTextContent())
                .trim();
             
              boolean alreadyExist = false;
              int setValue = 0;
              String Shipbillno = null;
              String Formno = null;
              if (!commonMethods.isNull(shipbillno)) {
                alreadyExist = isFourDataAvailable("ETT_SHP_ROD_ACK_STG", "SHIPPINGBILLNO",
                  "SHIPPINGBILLDATE", "PORTCODE", "RECORDINDICATOR", shipbillno, shipbilldate,
                  portcode, recordindicator);
              } else {
                alreadyExist = isFourDataAvailable("ETT_SHP_ROD_ACK_STG", "FORMNO", "SHIPPINGBILLDATE",
                  "PORTCODE", "RECORDINDICATOR", formno, shipbilldate, portcode, recordindicator);
              }
              if (alreadyExist)
              {
                String rodQuery = "SELECT ERRORCODES FROM ETT_SHP_ROD_ACK_STG  WHERE SHIPPINGBILLDATE=? AND PORTCODE=? AND RECORDINDICATOR=? ";
                if ((!commonMethods.isNull(shipbillno)) && (!commonMethods.isNull(formno)))
                {
                  rodQuery = rodQuery + " AND SHIPPINGBILLNO =?  AND FORMNO =?";
                  Shipbillno = shipbillno;
                  Formno = formno;
                }
                else if (!commonMethods.isNull(shipbillno))
                {
                  rodQuery = rodQuery + " AND SHIPPINGBILLNO =?";
                  Shipbillno = shipbillno;
                }
                else if (!commonMethods.isNull(formno))
                {
                  rodQuery = rodQuery + " AND FORMNO =?";
                  Formno = formno;
                }
                LoggableStatement pst4 = new LoggableStatement(con, rodQuery);
               
                pst4.setString(++setValue, shipbilldate);
                pst4.setString(++setValue, portcode);
                pst4.setString(++setValue, recordindicator);
                if (Shipbillno != null) {
                  pst4.setString(++setValue, shipbillno);
                }
                if (Formno != null) {
                  pst4.setString(++setValue, formno);
                }
                logger.info("ETT_SHP_ROD_ACK_STG Exist Data Check Query---------------" + pst4.getQueryString());
                ResultSet rs4 = pst4.executeQuery();
               
                String errorStatus = "";
                if (rs4.next())
                {
                  errorStatus = rs4.getString("ERRORCODES");
                 
                  logger.info("ETT_SHP_ROD_ACK_STG ERRORCODES Data Valeu---------------" + errorStatus);
                }
                closeStatementResultSet(pst4, rs4);
               
                logger.info("111111111111111111111111111111111111");
                if ((errorStatus != null) && (!errorStatus.equalsIgnoreCase("SUCCESS")))
                {
                  logger.info("11111111111111111111113333333333333333111");
                  int chkRod = createTempRODAckData(con, shipbillno, shipbilldate, portcode, formno);
                  logger.info("2222222222222222222222222222222");
                 

                  xmlFileVO.setResult("F");
                  String Shipno = null;
                  String Frmno = null;
                  setVal = 0;
                  if (chkRod > 1200)
                  {
                    String UPDATE_ROD_XML_DATA = "UPDATE ETT_SHP_ROD_ACK_STG SET EXPORTTYPE=?, RECORDINDICATOR=?, LEODATE=TO_DATE(?, 'DD-MM-YYYY'), IECODE=?, CHANGEDIECODE=?, ADCODE=?, ADEXPORTAGENCY=?, DIRECTDISPATCHINDICATOR=?, ADBILLNO=?, DATEOFNEGOTIATION=TO_DATE(?, 'DD-MM-YYYY'), BUYERNAME=?, BUYERCOUNTRY=?, FILE_NO=?,ERRORCODES=?  WHERE  SHIPPINGBILLDATE=? AND PORTCODE=?";
                    if (!commonMethods.isNull(shipbillno))
                    {
                      UPDATE_ROD_XML_DATA =
                        UPDATE_ROD_XML_DATA + " AND SHIPPINGBILLNO ='" + shipbillno + "'";
                      Shipno = shipbillno;
                    }
                    if (!commonMethods.isNull(formno))
                    {
                      UPDATE_ROD_XML_DATA = UPDATE_ROD_XML_DATA + " AND FORMNO ='" + formno + "'";
                      Frmno = formno;
                    }
                    LoggableStatement pst5 = new LoggableStatement(con, UPDATE_ROD_XML_DATA);
                    pst5.setString(++setVal, exporttype);
                    pst5.setString(++setVal, recordindicator);
                    pst5.setString(++setVal, leodate);
                    pst5.setString(++setVal, iecode);
                    pst5.setString(++setVal, changedcode);
                    pst5.setString(++setVal, adcode);
                    pst5.setString(++setVal, adexportagency);
                    pst5.setString(++setVal, dispatchindicator);
                    pst5.setString(++setVal, adno);
                    pst5.setString(++setVal, dateneg);
                    pst5.setString(++setVal, buyername);
                    pst5.setString(++setVal, buyercountry);
                    pst5.setInt(++setVal, fiNo);
                    pst5.setString(++setVal, errorcodes);
                    pst5.setString(++setVal, shipbilldate);
                    pst5.setString(++setVal, portcode);
                    if (Shipno != null) {
                      pst5.setString(++setVal, shipbillno);
                    }
                    if (Frmno != null) {
                      pst5.setString(++setVal, formno);
                    }
                    int rr = 0;
                    logger.info("UPDATE--------------ETT_SHP_ROD_ACK_STG-------------" + pst5.getQueryString());
                    rr = pst5.executeUpdate();
                   

                    logger.info("INSERT--------------ETT_SHP_ROD_ACK_STG---- count---------" + rr);
                    closePreparedStatement(pst5);
                   
                    rodship++;
                    numberbill -= rodship;
                    count--;
                    if (count == 0) {
                      xmlFileVO.setResult("Y");
                    }
                  }
                }
                else if ((errorStatus != null) && (errorStatus.equalsIgnoreCase("SUCCESS")))
                {
                  LoggableStatement pst6 = null;
                  try
                  {
                    logger.info("INSERT%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--");
                    String shipbillfail = shipbillno + " " + "Bill No";
                    failingcount++;
                    failBillList.add(shipbillfail);
                   

                    pst6 = new LoggableStatement(con,
                      "INSERT INTO ETT_SHP_ROD_ACK_STG(PORTCODE,EXPORTTYPE,RECORDINDICATOR,SHIPPINGBILLNO,SHIPPINGBILLDATE,FORMNO,LEODATE,IECODE,CHANGEDIECODE,ADCODE,ADEXPORTAGENCY,DIRECTDISPATCHINDICATOR,ADBILLNO,DATEOFNEGOTIATION,BUYERNAME,BUYERCOUNTRY,FILE_NO,ERRORCODES) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?)");
                    pst6.setString(1, portcode);
                    pst6.setString(2, exporttype);
                    pst6.setString(3, recordindicator);
                    pst6.setString(4, shipbillno);
                    pst6.setString(5, shipbilldate);
                    pst6.setString(6, formno);
                    pst6.setString(7, leodate);
                    pst6.setString(8, iecode);
                    pst6.setString(9, changedcode);
                    pst6.setString(10, adcode);
                    pst6.setString(11, adexportagency);
                    pst6.setString(12, dispatchindicator);
                    pst6.setString(13, adno);
                    pst6.setString(14, dateneg);
                    pst6.setString(15, buyername);
                    pst6.setString(16, buyercountry);
                    pst6.setInt(17, fiNo);
                    pst6.setString(18, "Success File Data Already Uploaded in the System ");
                   

                    int rr = 0;
                    logger.info("UPDATE--------------ETT_SHP_ROD_ACK_STG-----Already Uploaded-----quERY---" + pst6.getQueryString());
                    rr = pst6.executeUpdate();
                   
                    xmlFileVO.setResult("F");
                   
                    logger.info("INSERT--------------ETT_SHP_ROD_ACK_STG---- count---------" + rr);
                  }
                  catch (Exception e)
                  {
                    logger.info("INSERT%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%---------Exception-------" + e);
                  }
                  finally
                  {
                    if (pst6 != null) {
                      pst6.close();
                    }
                  }
                }
              }
              else
              {
                LoggableStatement pst5 = new LoggableStatement(con,
                  "INSERT INTO ETT_SHP_ROD_ACK_STG(PORTCODE,EXPORTTYPE,RECORDINDICATOR,SHIPPINGBILLNO,SHIPPINGBILLDATE,FORMNO,LEODATE,IECODE,CHANGEDIECODE,ADCODE,ADEXPORTAGENCY,DIRECTDISPATCHINDICATOR,ADBILLNO,DATEOFNEGOTIATION,BUYERNAME,BUYERCOUNTRY,FILE_NO,ERRORCODES) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?)");
                pst5.setString(1, portcode);
                pst5.setString(2, exporttype);
                pst5.setString(3, recordindicator);
                pst5.setString(4, shipbillno);
                pst5.setString(5, shipbilldate);
                pst5.setString(6, formno);
                pst5.setString(7, leodate);
                pst5.setString(8, iecode);
                pst5.setString(9, changedcode);
                pst5.setString(10, adcode);
                pst5.setString(11, adexportagency);
                pst5.setString(12, dispatchindicator);
                pst5.setString(13, adno);
                pst5.setString(14, dateneg);
                pst5.setString(15, buyername);
                pst5.setString(16, buyercountry);
                pst5.setInt(17, fiNo);
                pst5.setString(18, errorcodes);
               
                logger.info("INSERT--------------ETT_SHP_ROD_ACK_STG-------------" + pst5.getQueryString());
                int rr = 0;
                rr = pst5.executeUpdate();
               



                logger.info("INSERT--------------ETT_SHP_ROD_ACK_STG---- count---------" + rr);
               


                closePreparedStatement(pst5);
               
                rodship++;
                numberbill -= rodship;
               



                count--;
                if (count == 0) {
                  xmlFileVO.setResult("Y");
                }
              }
              xmlFileVO.setShpCount(rodship);
              logger.info("numberbill COunt------" + numberbill);
              logger.info("Inbound COunt------" + rodship);
            }
            if (failingcount > 0) {
              xmlFileVO.setResult("F");
            }
          }
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("--------------xmlUploadRodFiles----- Exception------------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadPenFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int penship = 0;
    int numberbill = 0;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
     
      con = DBConnectionUtility.getConnection();
     
      DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
     
      Date date = new Date();
     
      String File = "";
      LoggableStatement pst1 = new LoggableStatement(con,
        "SELECT FILE_NAME FROM ETT_PEN_ACK_FILES WHERE FILE_NAME=?");
      pst1.setString(1, fileName);
      ResultSet rs1 = pst1.executeQuery();
      if (rs1.next()) {
        File = rs1.getString("FILE_NAME");
      }
      closeStatementResultSet(pst1, rs1);
      if (File.equals(fileName))
      {
        xmlFileVO.setResult("N");
      }
      else
      {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       
        Document doc = docBuilder.parse(xmlFileUpload);
       
        doc.getDocumentElement().normalize();
       
        NodeList chksum = doc.getElementsByTagName("checkSum");
       
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() == 1)
        {
          Element firstPersonElement = (Element)firstPersonNode;
         
          NodeList ShippingBills = firstPersonElement.getElementsByTagName("noOfShippingBills");
         
          Element firstNameElement = (Element)ShippingBills.item(0);
         
          NodeList textFNList = firstNameElement.getChildNodes();
         
          String noofshipbills = textFNList.item(0).getTextContent().trim();
         
          numberbill = Integer.parseInt(noofshipbills);
         
          int fiNo = 0;
         
          LoggableStatement pst2 = new LoggableStatement(con, "SELECT PENACK_SEQNO.nextval FROM DUAL");
          ResultSet rs2 = pst2.executeQuery();
          if (rs2.next()) {
            fiNo = rs2.getInt("nextval");
          }
          closeStatementResultSet(pst2, rs2);
         
          LoggableStatement pst3 = new LoggableStatement(con,
            "INSERT INTO ETT_PEN_ACK_FILES(FILE_NO,NO_OF_SHIPPING_BILLS,FILE_NAME,STATUS,PROCESSED_DATE) values(?,?,?,?,TO_DATE(?,'DD-MM-YYYY'))");
          pst3.setInt(1, fiNo);
          pst3.setString(2, noofshipbills);
          pst3.setString(3, fileName);
          pst3.setString(4, "Y");
          pst3.setString(5, dateFormat.format(date));
          int rsq = 0;
          rsq = pst3.executeUpdate();
          logger.info("Inserted Count---->" + rsq);
          closePreparedStatement(pst3);
         
          NodeList shbill = doc.getElementsByTagName("shippingBill");
         
          int totalshbill = shbill.getLength();
         
          int count = totalshbill;
         
          int failingcount = 0;
         
          List failBillList = new ArrayList();
          for (int y = 0; y < totalshbill; y++)
          {
            Node firstPersonNode1 = shbill.item(y);
            if (firstPersonNode1.getNodeType() == 1)
            {
              Element eElement = (Element)firstPersonNode1;
             
              String portcode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("portCode").item(0).getTextContent())
                .trim();
             
              String exporttype = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("exportType").item(0).getTextContent())
                .trim();
             
              String recordindicator = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("recordIndicator").item(0).getTextContent())
                .trim();
             
              String shipbillno = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shippingBillNo").item(0).getTextContent())
                .trim();
             
              String shipbilldate = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shippingBillDate").item(0).getTextContent())
                .trim().replace("/", "");
             
              String formno = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("formNo").item(0).getTextContent())
                .trim();
             
              String leodate = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("LEODate").item(0).getTextContent())
                .trim();
             
              String adcode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("adCode").item(0).getTextContent())
                .trim();
             
              String iecode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("IECode").item(0).getTextContent())
                .trim();
             
              String realind = commonMethods.getEmptyIfNull(
                eElement.getElementsByTagName("realizationExtensionInd").item(0).getTextContent())
                .trim();
             
              String letterno = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("letterNo").item(0).getTextContent())
                .trim();
             
              String letterdate = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("letterDate").item(0).getTextContent())
                .trim();
             
              String exreal = commonMethods.getEmptyIfNull(
                eElement.getElementsByTagName("extendedRealizationDate").item(0).getTextContent())
                .trim();
             
              String errorcode = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("errorCodes").item(0).getTextContent())
                .trim();
             
              boolean alreadyExist = false;
              if (!commonMethods.isNull(shipbillno)) {
                alreadyExist = isFourDataAvailable("ETT_PEN_SHP_ACK_STG", "SHIPPINGBILLNO",
                  "SHIPPINGBILLDATE", "PORTCODE", "RECORDINDICATOR", shipbillno, shipbilldate,
                  portcode, recordindicator);
              } else {
                alreadyExist = isFourDataAvailable("ETT_PEN_SHP_ACK_STG", "FORMNO", "SHIPPINGBILLDATE",
                  "PORTCODE", "RECORDINDICATOR", formno, shipbilldate, portcode, recordindicator);
              }
              int setValue = 0;
              String Shipbillno = null;
              String Formno = null;
              if (alreadyExist)
              {
                String penQuery = "SELECT ERRORCODES FROM ETT_PEN_SHP_ACK_STG  WHERE SHIPPINGBILLDATE=? AND PORTCODE=? AND RECORDINDICATOR=? ";
                if (!commonMethods.isNull(shipbillno))
                {
                  penQuery = penQuery + " AND SHIPPINGBILLNO =?";
                  Shipbillno = shipbillno;
                }
                if (!commonMethods.isNull(formno))
                {
                  penQuery = penQuery + " AND FORMNO =?";
                  Formno = formno;
                }
                LoggableStatement pst4 = new LoggableStatement(con, penQuery);
                pst4.setString(++setValue, shipbilldate);
                pst4.setString(++setValue, portcode);
                pst4.setString(++setValue, recordindicator);
                if (Shipbillno != null) {
                  pst4.setString(++setValue, shipbillno);
                }
                if (Formno != null) {
                  pst4.setString(++setValue, formno);
                }
                ResultSet rs4 = pst4.executeQuery();
                String errorStatus = "";
                if (rs4.next()) {
                  errorStatus = rs4.getString("ERRORCODES");
                }
                closeStatementResultSet(pst4, rs4);
               
                String Shipno = null;
                String Frmno = null;
                int setVal = 0;
                if ((errorStatus != null) && (!errorStatus.equalsIgnoreCase("SUCCESS")))
                {
                  String UPDATE_PEN_XML_DATA = "UPDATE ETT_PEN_SHP_ACK_STG SET EXPORTTYPE=?, RECORDINDICATOR=?, FORMNO=?,LEODATE=TO_DATE(?, 'DD-MM-YYYY'), ADCODE=?, IECODE=?, REALIZATIONEXTENSIONIND=?, LETTERNO=?, LETTERDATE=TO_DATE(?, 'DD-MM-YYYY'), EXTENDEDREALIZATIONDATE=TO_DATE(?, 'DD-MM-YYYY'), FILE_NO=?,ERRORCODES=?  WHERE SHIPPINGBILLDATE=? AND PORTCODE=?";
                  if (!commonMethods.isNull(shipbillno))
                  {
                    UPDATE_PEN_XML_DATA = UPDATE_PEN_XML_DATA + " AND SHIPPINGBILLNO =?";
                    Shipno = shipbillno;
                  }
                  if (!commonMethods.isNull(formno))
                  {
                    UPDATE_PEN_XML_DATA = UPDATE_PEN_XML_DATA + " AND FORMNO =?";
                    Frmno = formno;
                  }
                  LoggableStatement pst5 = new LoggableStatement(con, UPDATE_PEN_XML_DATA);
                  pst5.setString(++setVal, exporttype);
                  pst5.setString(++setVal, recordindicator);
                  pst5.setString(++setVal, leodate);
                  pst5.setString(++setVal, adcode);
                  pst5.setString(++setVal, iecode);
                  pst5.setString(++setVal, realind);
                  pst5.setString(++setVal, letterno);
                  pst5.setString(++setVal, letterdate);
                  pst5.setString(++setVal, exreal);
                  pst5.setInt(++setVal, fiNo);
                  pst5.setString(++setVal, errorcode);
                  pst5.setString(++setVal, shipbilldate);
                  pst5.setString(++setVal, portcode);
                  if (Shipno != null) {
                    pst5.setString(++setVal, shipbillno);
                  }
                  if (Frmno != null) {
                    pst5.setString(++setVal, formno);
                  }
                  int rr = 0;
                  rr = pst5.executeUpdate();
                  closePreparedStatement(pst5);
                 
                  penship++;
                  numberbill -= penship;
                 
                  count--;
                  if (count == 0) {
                    xmlFileVO.setResult("Y");
                  }
                }
                else
                {
                  String shipbillfail = shipbillno + " " + "Bill No";
                  failingcount++;
                  failBillList.add(shipbillfail);
                }
              }
              else
              {
                LoggableStatement pst5 = new LoggableStatement(con,
                  "INSERT INTO ETT_PEN_SHP_ACK_STG(PORTCODE,EXPORTTYPE,RECORDINDICATOR,SHIPPINGBILLNO,SHIPPINGBILLDATE,FORMNO,LEODATE,ADCODE,IECODE,REALIZATIONEXTENSIONIND,LETTERNO,LETTERDATE,EXTENDEDREALIZATIONDATE,FILE_NO,ERRORCODES) values(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),TO_DATE(?, 'DD-MM-YYYY'),?,?)");
                pst5.setString(1, portcode);
                pst5.setString(2, exporttype);
                pst5.setString(3, recordindicator);
                pst5.setString(4, shipbillno);
                pst5.setString(5, shipbilldate);
                pst5.setString(6, formno);
                pst5.setString(7, leodate);
                pst5.setString(8, adcode);
                pst5.setString(9, iecode);
                pst5.setString(10, realind);
                pst5.setString(11, letterno);
                pst5.setString(12, letterdate);
                pst5.setString(13, exreal);
                pst5.setInt(14, fiNo);
                pst5.setString(15, errorcode);
                int rr = 0;
                rr = pst5.executeUpdate();
                closePreparedStatement(pst5);
               
                penship++;
                numberbill -= penship;
               
                count--;
                if (count == 0) {
                  xmlFileVO.setResult("Y");
                }
              }
            }
          }
          if (failingcount > 0) {
            xmlFileVO.setResult("F");
          }
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadPrnFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("Getting into  xmlUploadPrnFiles-------");
   
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    CommonMethods commonMethods = null;
    int invCnt = 0;
    int totalinv1 = 0;
    int numberSBill = 0;
    int prnCount = 0;
    int totalSB = 0;
   
    int invLength = 0;
    int iShpCount = 0;
    int iInvCount = 0;
    String invoiceerror = "";
    String portcode = "";
    String recordind = "";
    String shipbillno = "";
    String shipbilldate = "";
    String formno = "";
    String paySeq = "";
    String exporttype = "";
    String leodate = "";
    String adcode = "";
    String irmNumber = "";
    String fircNumber = "";
    String remAdcode = "";
    String thirdParty = "";
    String relCurrency = "";
    String realDate = "";
    String errorcodes = "";
    String invoiceserialno = "";
    String invoiceno = "";
    String invoicedate = "";
    String accountnumber = "";
    String bankCharge = "";
    String fobamt = "";
    String fobamtic = "";
    String freightamt = "";
    String freightamtic = "";
    String insuranceamt = "";
    String insuranceamtic = "";
    String remittername = "";
    String remittercountry = "";
    String closeind = "";
    String setBillErrorString = "";
    String setInvErrorString = "";
    ExcelDataVO excelDataVO = null;
    ArrayList<ExcelDataVO> alPRNTagValidation = null;
    try
    {
      commonMethods = new CommonMethods();
     
      con = DBConnectionUtility.getConnection();
     
      DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
     
      Date date = new Date();
      alPRNTagValidation = new ArrayList();
      String file = "";
      LoggableStatement pst1 = new LoggableStatement(con,
        "SELECT FILE_NAME FROM ETT_PRN_ACK_Files WHERE FILE_NAME=?");
      pst1.setString(1, fileName);
      ResultSet rs1 = pst1.executeQuery();
      if (rs1.next()) {
        file = rs1.getString("FILE_NAME");
      }
      closeStatementResultSet(pst1, rs1);
      if (file.equals(fileName))
      {
        xmlFileVO.setResult("N");
      }
      else
      {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       
        Document doc = docBuilder.parse(xmlFileUpload);
       
        doc.getDocumentElement().normalize();
       
        NodeList chksum = doc.getElementsByTagName("checkSum");
       
        Node firstPersonNode = chksum.item(0);
       
        logger.info("firstPersonNode------------->" + firstPersonNode);
        if (firstPersonNode.getNodeType() == 1)
        {
          Element firstPersonElement = (Element)firstPersonNode;
         
          NodeList ShippingBills = firstPersonElement.getElementsByTagName("noOfShippingBills");
          Element firstNameElement = (Element)ShippingBills.item(0);
         
          NodeList textFNList = firstNameElement.getChildNodes();
         
          String noofshipbills = textFNList.item(0).getTextContent().trim();
         
          totalSB = Integer.parseInt(noofshipbills);
         
          NodeList Invoices = firstPersonElement.getElementsByTagName("noOfInvoices");
         
          Element lastNameElement = (Element)Invoices.item(0);
         
          NodeList textLNList = lastNameElement.getChildNodes();
         
          String noofinvoices = textLNList.item(0).getTextContent().trim();
         
          int fiNo = 0;
         
          LoggableStatement pst2 = new LoggableStatement(con, "SELECT PRNACK_SEQNO.NEXTVAL FROM DUAL");
          ResultSet rs2 = pst2.executeQuery();
          if (rs2.next()) {
            fiNo = rs2.getInt("nextval");
          }
          closeSqlRefferance(rs2, pst2);
         
          xmlFileVO.setFileNumber(fiNo);
         
          LoggableStatement pst3 = new LoggableStatement(con,
            "INSERT INTO ETT_PRN_ACK_FILES(FILE_NO,NO_OF_SHIPPING_BILLS,NO_OF_INVOICES,FILE_NAME,STATUS,PROCESSED_DATE) values(?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'))");
          pst3.setInt(1, fiNo);
          pst3.setString(2, noofshipbills);
          pst3.setString(3, noofinvoices);
          pst3.setString(4, fileName);
          pst3.setString(5, "Y");
          pst3.setString(6, dateFormat.format(date));
          int rsq = 0;
          rsq = pst3.executeUpdate();
          closePreparedStatement(pst3);
          logger.info(rsq + " Rows Inserted for PRN ACK FILES Details");
         
          NodeList shbill = doc.getElementsByTagName("shippingBill");
         
          int totalshbill = shbill.getLength();
          for (int y = 0; y < totalshbill; y++)
          {
            excelDataVO = new ExcelDataVO();
           
            Node firstPersonNode1 = shbill.item(y);
            if (firstPersonNode1.getNodeType() == 1)
            {
              Element eElement = (Element)firstPersonNode1;
             





              NodeList n1 = eElement.getElementsByTagName("portCode");
              if (n1.getLength() < 1)
              {
                portcode = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "Port Code");
              }
              else
              {
                portcode =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("portCode").item(0).getTextContent()).trim();
              }
              NodeList n2 = eElement.getElementsByTagName("exportType");
              if (n2.getLength() < 1)
              {
                exporttype = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "exportType");
              }
              else
              {
                exporttype =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("exportType").item(0).getTextContent()).trim();
              }
              NodeList n3 = eElement.getElementsByTagName("recordIndicator");
              if (n3.getLength() < 1)
              {
                recordind = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString,
                  "recordIndicator");
              }
              else
              {
                recordind =
               
                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("recordIndicator").item(0).getTextContent()).trim();
              }
              NodeList n4 = eElement.getElementsByTagName("shippingBillNo");
              if (n4.getLength() < 1)
              {
                shipbillno = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "shippingBillNo");
              }
              else
              {
                shipbillno =
               
                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("shippingBillNo").item(0).getTextContent()).trim();
              }
              NodeList n5 = eElement.getElementsByTagName("shippingBillDate");
              if (n5.getLength() < 1)
              {
                shipbilldate = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString,
                  "shippingBillDate");
              }
              else
              {
                shipbilldate =
               
                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("shippingBillDate").item(0).getTextContent()).trim().replace("/", "");
              }
              NodeList n6 = eElement.getElementsByTagName("formNo");
              if (n6.getLength() < 1)
              {
                formno = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "formNo");
              }
              else
              {
                formno =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("formNo").item(0).getTextContent()).trim();
              }
              NodeList n7 = eElement.getElementsByTagName("LEODate");
              if (n7.getLength() < 1)
              {
                leodate = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "LEODate");
              }
              else
              {
                leodate =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("LEODate").item(0).getTextContent()).trim();
              }
              NodeList n8 = eElement.getElementsByTagName("adCode");
              if (n8.getLength() < 1)
              {
                adcode = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "adCode");
              }
              else
              {
                adcode =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("adCode").item(0).getTextContent()).trim();
              }
              NodeList n9 = eElement.getElementsByTagName("paymentSequence");
              if (n9.getLength() < 1)
              {
                paySeq = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString,
                  "paymentSequence");
              }
              else
              {
                paySeq =
               
                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("paymentSequence").item(0).getTextContent()).trim();
              }
              NodeList n10 = eElement.getElementsByTagName("IRMNumber");
              if (n10.getLength() < 1)
              {
                irmNumber = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "IRMNumber");
              }
              else
              {
                irmNumber =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("IRMNumber").item(0).getTextContent()).trim();
              }
              NodeList n11 = eElement.getElementsByTagName("FIRCNumber");
              if (n11.getLength() < 1)
              {
                fircNumber = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "FIRCNumber");
              }
              else
              {
                fircNumber =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("FIRCNumber").item(0).getTextContent()).trim();
              }
              NodeList n12 = eElement.getElementsByTagName("remittanceAdCode");
              if (n12.getLength() < 1)
              {
                remAdcode = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString,
                  "remittanceAdCode");
              }
              else
              {
                remAdcode =
               
                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("remittanceAdCode").item(0).getTextContent()).trim();
              }
              NodeList n13 = eElement.getElementsByTagName("thirdParty");
              if (n13.getLength() < 1)
              {
                thirdParty = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "thirdParty");
              }
              else
              {
                thirdParty =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("thirdParty").item(0).getTextContent()).trim();
              }
              NodeList n14 = eElement.getElementsByTagName("realizedCurrencyCode");
              if (n14.getLength() < 1)
              {
                relCurrency = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString,
                  "realizedCurrencyCode");
              }
              else
              {
                relCurrency =
               
                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("realizedCurrencyCode").item(0).getTextContent()).trim();
              }
              NodeList n15 = eElement.getElementsByTagName("realizationDate");
              if (n15.getLength() < 1)
              {
                realDate = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString,
                  "realizationDate");
              }
              else
              {
                realDate =
               
                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("realizationDate").item(0).getTextContent()).trim();
              }
              NodeList n16 = eElement.getElementsByTagName("errorCodes");
              if (n16.getLength() < 1)
              {
                errorcodes = "";
                setBillErrorString = commonMethods.setErrorString(setBillErrorString, "errorCodes");
              }
              else
              {
                errorcodes =
               

                  commonMethods.getEmptyIfNull(eElement.getElementsByTagName("errorCodes").item(0).getTextContent()).trim();
              }
              String prnQuery = "SELECT COUNT(*) AS PRN_COUNT FROM ETT_PRN_SHP_ACK_STG WHERE TO_DATE(SHIPPINGBILLDATE,'DD/MM/YYYY') = TO_DATE(?, 'DD/MM/YYYY') AND PORTCODE = ? AND RECORDINDICATOR = ?  AND PAY_SEQNO = ? AND ERRORCODES = 'SUCCESS' ";
             
              int setValue = 0;
              String Shipbillno = null;
              String Formno = null;
              if (!commonMethods.isNull(shipbillno))
              {
                prnQuery = prnQuery + " AND SHIPPINGBILLNO =?";
                Shipbillno = shipbillno;
              }
              if (!commonMethods.isNull(formno))
              {
                prnQuery = prnQuery + " AND FORMNO =?";
                Formno = formno;
              }
              LoggableStatement pst4 = new LoggableStatement(con, prnQuery);
              pst4.setString(++setValue, shipbilldate);
              pst4.setString(++setValue, portcode);
              pst4.setString(++setValue, recordind);
              pst4.setString(++setValue, paySeq);
              if (Shipbillno != null) {
                pst4.setString(++setValue, shipbillno);
              }
              if (Formno != null) {
                pst4.setString(++setValue, formno);
              }
              ResultSet rs4 = pst4.executeQuery();
             
              logger.info("shipbillno " + shipbillno);
              logger.info("shipbilldate " + shipbilldate);
              logger.info("portcode " + portcode);
              logger.info("recordind " + recordind);
              logger.info("paySeq " + paySeq);
              if (rs4.next()) {
                prnCount = rs4.getInt("PRN_COUNT");
              }
              closeStatementResultSet(pst4, rs4);
             
              NodeList inv1 = eElement.getElementsByTagName("invoice");
             

              invLength += inv1.getLength();
              logger.info("The PRN Count is : " + prnCount);
              if (prnCount == 0)
              {
                String uniqueNo = "";
               
                String prnSqnoQuery = "SELECT ETT_GR_SHP_ACK_SEQ_NO AS UNIQUE_NO FROM DUAL";
               
                LoggableStatement ps = new LoggableStatement(con, prnSqnoQuery);
                ResultSet rst = ps.executeQuery();
                if (rst.next()) {
                  uniqueNo = rst.getString("UNIQUE_NO");
                }
                closeStatementResultSet(ps, rst);
               
                LoggableStatement pst5 = new LoggableStatement(con,
                  "INSERT INTO ETT_PRN_SHP_ACK_STG(PORTCODE,EXPORTTYPE,RECORDINDICATOR,SHIPPINGBILLNO,SHIPPINGBILLDATE,FORMNO,LEODATE,ADCODE,PAY_SEQNO,INWARDNO,FIRCNO,REM_ADCODE,PAY_PARTY,REALIZATONCURR,REALIZATONDATE,FILE_NO,ERRORCODES,GR_NUMBER) values(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?)");
                pst5.setString(1, portcode);
                pst5.setString(2, exporttype);
                pst5.setString(3, recordind);
                pst5.setString(4, shipbillno);
                pst5.setString(5, shipbilldate);
                pst5.setString(6, formno);
                pst5.setString(7, leodate);
                pst5.setString(8, adcode);
                pst5.setString(9, paySeq);
                pst5.setString(10, irmNumber);
                pst5.setString(11, fircNumber);
                pst5.setString(12, remAdcode);
                pst5.setString(13, thirdParty);
                pst5.setString(14, relCurrency);
                pst5.setString(15, realDate);
                pst5.setInt(16, fiNo);
                pst5.setString(17, errorcodes);
                pst5.setString(18, uniqueNo);
                int shpCount = pst5.executeUpdate();
                closePreparedStatement(pst5);
               
                numberSBill += shpCount;
                if (shpCount > 0)
                {
                  totalinv1 = inv1.getLength();
                  if ((setBillErrorString != null) && (setBillErrorString.equalsIgnoreCase("")))
                  {
                    for (int i = 0; i < totalinv1; i++)
                    {
                      Node firstPersonNode111 = inv1.item(i);
                      if (firstPersonNode111.getNodeType() == 1)
                      {
                        Element eElementData = (Element)firstPersonNode111;
                       
                        NodeList n17 = eElementData.getElementsByTagName("invoiceSerialNo");
                        if (n17.getLength() < 1)
                        {
                          invoiceserialno = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "invoiceSerialNo");
                        }
                        else
                        {
                          invoiceserialno =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("invoiceSerialNo").item(0).getTextContent()).trim();
                        }
                        NodeList n18 = eElementData.getElementsByTagName("invoiceNo");
                        if (n18.getLength() < 1)
                        {
                          invoiceno = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "invoiceNo");
                        }
                        else
                        {
                          invoiceno =
                         
                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("invoiceNo").item(0).getTextContent()).trim();
                        }
                        NodeList n19 = eElementData.getElementsByTagName("invoiceDate");
                        if (n19.getLength() < 1)
                        {
                          invoicedate = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "invoiceDate");
                        }
                        else
                        {
                          invoicedate =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("invoiceDate").item(0).getTextContent()).trim();
                        }
                        NodeList n20 = eElementData.getElementsByTagName("accountNumber");
                        if (n20.getLength() < 1)
                        {
                          accountnumber = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "accountNumber");
                        }
                        else
                        {
                          accountnumber =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("accountNumber").item(0).getTextContent()).trim();
                        }
                        NodeList n21 = eElementData.getElementsByTagName("bankingChargesAmt");
                        if (n21.getLength() < 1)
                        {
                          bankCharge = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "bankingChargesAmt");
                        }
                        else
                        {
                          bankCharge =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("bankingChargesAmt").item(0).getTextContent()).trim();
                        }
                        NodeList n22 = eElementData.getElementsByTagName("FOBAmt");
                        if (n22.getLength() < 1)
                        {
                          fobamt = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "FOBAmt");
                        }
                        else
                        {
                          fobamt =
                         
                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("FOBAmt").item(0).getTextContent()).trim();
                        }
                        NodeList n23 = eElementData.getElementsByTagName("FOBAmtIC");
                        if (n23.getLength() < 1)
                        {
                          fobamtic = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "FOBAmtIC");
                        }
                        else
                        {
                          fobamtic =
                         
                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("FOBAmtIC").item(0).getTextContent()).trim();
                        }
                        NodeList n24 = eElementData.getElementsByTagName("freightAmt");
                        if (n24.getLength() < 1)
                        {
                          freightamt = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "freightAmt");
                        }
                        else
                        {
                          freightamt =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("freightAmt").item(0).getTextContent()).trim();
                        }
                        NodeList n25 = eElementData.getElementsByTagName("freightAmtIC");
                        if (n25.getLength() < 1)
                        {
                          freightamtic = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "freightAmtIC");
                        }
                        else
                        {
                          freightamtic =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("freightAmtIC").item(0).getTextContent()).trim();
                        }
                        NodeList n26 = eElementData.getElementsByTagName("insuranceAmt");
                        if (n26.getLength() < 1)
                        {
                          insuranceamt = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "insuranceAmt");
                        }
                        else
                        {
                          insuranceamt =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("insuranceAmt").item(0).getTextContent()).trim();
                        }
                        NodeList n27 = eElementData.getElementsByTagName("insuranceAmtIC");
                        if (n27.getLength() < 1)
                        {
                          insuranceamtic = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "insuranceAmtIC");
                        }
                        else
                        {
                          insuranceamtic =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("insuranceAmtIC").item(0).getTextContent()).trim();
                        }
                        NodeList n28 = eElementData.getElementsByTagName("remitterName");
                        if (n28.getLength() < 1)
                        {
                          remittername = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "remitterName");
                        }
                        else
                        {
                          remittername =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("remitterName").item(0).getTextContent()).trim();
                        }
                        NodeList n29 = eElementData.getElementsByTagName("remitterCountry");
                        if (n29.getLength() < 1)
                        {
                          remittercountry = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "remitterCountry");
                        }
                        else
                        {
                          remittercountry =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("remitterCountry").item(0).getTextContent()).trim();
                        }
                        NodeList n30 = eElementData
                          .getElementsByTagName("closeOfBillIndicator");
                        if (n30.getLength() < 1)
                        {
                          closeind = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "closeOfBillIndicator");
                        }
                        else
                        {
                          closeind =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("closeOfBillIndicator").item(0).getTextContent()).trim();
                        }
                        NodeList n31 = eElementData.getElementsByTagName("invoiceErrorCodes");
                        if (n31.getLength() < 1)
                        {
                          invoiceerror = "";
                          setInvErrorString = commonMethods.setErrorString(setInvErrorString,
                            "invoiceErrorCodes");
                        }
                        else
                        {
                          invoiceerror =
                         

                            commonMethods.getEmptyIfNull(eElementData.getElementsByTagName("invoiceErrorCodes").item(0).getTextContent()).trim();
                        }
                        if (commonMethods.isNull(setInvErrorString))
                        {
                          LoggableStatement pst6 = new LoggableStatement(con,
                            "INSERT INTO ETT_PRN_SHP_INV_ACK_STG(INVOICESERIALNO,INVOICENO,INVOICEDATE,ACCOUNTNUMBER,BANK_CHG,FOBAMT,FOBAMTIC,FREIGHTAMT,FREIGHTAMTIC,INSURANCEAMT,INSURANCEAMTIC,REMITTERNAME,REMITTERCOUNTRY,CLOSEOFBILLINDICATOR,FILE_NO,SHIPPINGBILLNO,SHIPPINGBILLDATE,PORTCODE,FORMNO,PAY_SEQNO,INVOICEERRORCODES,GR_SHP_NO) values(?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                          pst6.setString(1, invoiceserialno);
                          pst6.setString(2, invoiceno);
                          pst6.setString(3, invoicedate);
                          pst6.setString(4, accountnumber);
                          pst6.setString(5, bankCharge);
                          pst6.setString(6, fobamt);
                          pst6.setString(7, fobamtic);
                          pst6.setString(8, freightamt);
                          pst6.setString(9, freightamtic);
                          pst6.setString(10, insuranceamt);
                          pst6.setString(11, insuranceamtic);
                          pst6.setString(12, remittername);
                          pst6.setString(13, remittercountry);
                          pst6.setString(14, closeind);
                          pst6.setInt(15, fiNo);
                          pst6.setString(16, shipbillno);
                          pst6.setString(17, shipbilldate);
                          pst6.setString(18, portcode);
                          pst6.setString(19, formno);
                          pst6.setString(20, paySeq);
                          pst6.setString(21, invoiceerror);
                          pst6.setString(22, uniqueNo);
                          pst6.executeUpdate();
                          closePreparedStatement(pst6);
                         
                          invCnt++;
                        }
                        else
                        {
                          excelDataVO.setShipBillNo(shipbillno);
                          excelDataVO.setShipBillDate(shipbilldate);
                          excelDataVO.setPortCode(portcode);
                          excelDataVO.setFormNo(formno);
                          excelDataVO.setInvSerialNo(invoiceserialno);
                          excelDataVO.setInvNo(invoiceno);
                          excelDataVO.setInvErrorString(setInvErrorString);
                          excelDataVO.setFileNo(fiNo);
                          alPRNTagValidation.add(excelDataVO);
                        }
                      }
                    }
                  }
                  else
                  {
                    excelDataVO.setShipBillNo(shipbillno);
                    excelDataVO.setShipBillDate(shipbilldate);
                    excelDataVO.setPortCode(portcode);
                    excelDataVO.setFormNo(formno);
                    excelDataVO.setShpErrorString(setBillErrorString);
                    excelDataVO.setFileNo(fiNo);
                    alPRNTagValidation.add(excelDataVO);
                  }
                }
              }
            }
          }
          String sFileType = "PRN";
          if (((setBillErrorString != null) && (!setBillErrorString.equalsIgnoreCase(""))) || (
            (setInvErrorString != null) && (!setInvErrorString.equalsIgnoreCase(""))))
          {
            int iErrCount = insert_EDPMS_TMP_Table(con, alPRNTagValidation);
           
            String sProcName = "ETT_PRN_REC_DELETE";
            int iCnt = 0;
            CallableStatement cs = null;
            cs = con.prepareCall("{call ETT_PRN_REC_DELETE(?, ?)}");
            cs.setInt(1, fiNo);
            cs.registerOutParameter(2, 4);
            if (cs.executeUpdate() >= 0) {
              iCnt = cs.getInt(2);
            }
            String sPRNTagCountQuery = "SELECT SUM(SHPCNT+FORMNO), SUM(INVCNT) FROM (SELECT COUNT(DISTINCT SHP.SHIPPINGBILLNO) SHPCNT, COUNT(DISTINCT FORMNO) FORMNO, 0 INVCNT FROM ETT_PRN_SHP_ACK_STG SHP WHERE FILE_NO = ? UNION ALL SELECT 0 SHPCNT, 0 FORMNO, COUNT(INV.INVOICENO) FROM ETT_PRN_SHP_INV_ACK_STG INV WHERE FILE_NO = ?)";
            LoggableStatement lst1 = new LoggableStatement(con, sPRNTagCountQuery);
            lst1.setInt(1, fiNo);
            lst1.setInt(2, fiNo);
            ResultSet rst2 = lst1.executeQuery();
            if (rst2.next())
            {
              iShpCount = rst2.getInt(1);
              iInvCount = rst2.getInt(2);
            }
            if (iErrCount > 0) {
              xmlFileVO.setErrCount(iErrCount);
            }
            if (iShpCount < 1) {
              rollbackACKFileDetails(con, xmlFileVO, sFileType);
            }
            xmlFileVO.setResult("FTC");
          }
          String sShpCountQuery = "SELECT COUNT(DISTINCT(PRNSHP.SHIPPINGBILLNO)) SHPCOUNT,  COUNT(PRNINV.INVOICENO) INVCOUNT FROM ETT_PRN_SHP_ACK_STG PRNSHP, ETT_PRN_SHP_INV_ACK_STG PRNINV WHERE PRNSHP.FILE_NO   = ? AND PRNSHP.FILE_NO = PRNINV.FILE_NO AND PRNSHP.SHIPPINGBILLNO = PRNINV.SHIPPINGBILLNO";
          LoggableStatement lst = new LoggableStatement(con, sShpCountQuery);
          lst.setInt(1, fiNo);
          ResultSet rst1 = lst.executeQuery();
          if (rst1.next())
          {
            iShpCount = rst1.getInt(1);
            iInvCount = rst1.getInt(2);
          }
          rst1.close();
          lst.close();
          if (iShpCount != 0) {
            xmlFileVO.setShpCount(iShpCount);
          }
          if (iInvCount != 0) {
            xmlFileVO.setInvCount(iInvCount);
          }
          xmlFileVO.setHeaderShpCount(Integer.parseInt(noofshipbills));
          xmlFileVO.setHeaderInvCount(Integer.parseInt(noofinvoices));
          xmlFileVO.setContentShpCount(totalshbill);
          xmlFileVO.setContentInvCount(invLength);
          xmlFileVO = comparePRNCount(xmlFileVO);
          String sRetValue = xmlFileVO.getCountResult();
          if (!"S".equalsIgnoreCase(sRetValue))
          {
            rollbackACKFileDetails(con, xmlFileVO, sFileType);
           
            xmlFileVO.setResult(sRetValue);
            XMLFileVO localXMLFileVO = xmlFileVO;return localXMLFileVO;
          }
          logger.info("The numberSBill is : " + numberSBill);
          if ((numberSBill == totalSB) && (Integer.parseInt(noofinvoices) == invCnt))
          {
            xmlFileVO.setResult("Y");
          }
          else if (numberSBill == 0)
          {
            xmlFileVO.setResult("F");
          }
          else
          {
            xmlFileVO.setResult("PL");
            xmlFileVO.setInsertCount(numberSBill);
          }
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadTrrFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int trrship = 0;
    int numberbill = 0;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
     
      con = DBConnectionUtility.getConnection();
     
      DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
     
      Date date = new Date();
     
      String File = "";
      LoggableStatement pst1 = new LoggableStatement(con,
        "SELECT FILE_NAME FROM ETT_TRR_ACK_Files WHERE FILE_NAME=?");
      pst1.setString(1, fileName);
      ResultSet rs1 = pst1.executeQuery();
      if (rs1.next()) {
        File = rs1.getString("FILE_NAME");
      }
      closeStatementResultSet(pst1, rs1);
      if (File.equals(fileName))
      {
        xmlFileVO.setResult("N");
      }
      else
      {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       
        Document doc = docBuilder.parse(xmlFileUpload);
       
        doc.getDocumentElement().normalize();
       
        NodeList chksum = doc.getElementsByTagName("checkSum");
       
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() == 1)
        {
          Element firstPersonElement = (Element)firstPersonNode;
         
          NodeList ShippingBills = firstPersonElement.getElementsByTagName("noOfShippingBills");
          Element firstNameElement = (Element)ShippingBills.item(0);
         
          NodeList textFNList = firstNameElement.getChildNodes();
         
          String noofshipbill = textFNList.item(0).getTextContent().trim();
         
          numberbill = Integer.parseInt(noofshipbill);
         
          int fiNo = 0;
          LoggableStatement pst2 = new LoggableStatement(con, "SELECT TRRACK_SEQNO.NEXTVAL FROM DUAL");
          ResultSet rs2 = pst2.executeQuery();
          if (rs2.next()) {
            fiNo = rs2.getInt("nextval");
          }
          closeStatementResultSet(pst2, rs2);
         
          LoggableStatement pst3 = new LoggableStatement(con,
            "INSERT INTO ETT_TRR_ACK_FILES(FILE_NO,NO_OF_SHIPPING_BILLS,FILE_NAME,STATUS,PROCESSED_DATE) values(?,?,?,?,TO_DATE(?,'DD-MM-YYYY'))");
          pst3.setInt(1, fiNo);
          pst3.setString(2, noofshipbill);
          pst3.setString(3, fileName);
          pst3.setString(4, "Y");
          pst3.setString(5, dateFormat.format(date));
         
          int rsq = 0;
          rsq = pst3.executeUpdate();
          logger.info(rsq + " Rows Inserted for TRANSFER ACK Details");
          closePreparedStatement(pst3);
         
          NodeList shbill = doc.getElementsByTagName("shippingBill");
         
          int totalshbill = shbill.getLength();
         
          int count = totalshbill;
         
          int failingcount = 0;
         
          List failBillList = new ArrayList();
          for (int y = 0; y < totalshbill; y++)
          {
            Node firstPersonNode1 = shbill.item(y);
            if (firstPersonNode1.getNodeType() == 1)
            {
              Element eElement = (Element)firstPersonNode1;
             
              String portcode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("portCode").item(0).getTextContent())
                .trim();
             
              String exportagency = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("exportAgency").item(0).getTextContent())
                .trim();
             
              String exporttype = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("exportType").item(0).getTextContent())
                .trim();
             
              String shipbillno = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shippingBillNo").item(0).getTextContent())
                .trim();
             
              String shipbilldate = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shippingBillDate").item(0).getTextContent())
                .trim().replace("/", "");
             
              String formno = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("formNo").item(0).getTextContent())
                .trim();
             
              String iecode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("IECode").item(0).getTextContent())
                .trim();
             
              String exiecode = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("existingAdCode").item(0).getTextContent())
                .trim();
             
              String newadcode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("newAdCode").item(0).getTextContent())
                .trim();
             
              String errorcode = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("errorCodes").item(0).getTextContent())
                .trim();
             
              boolean alreadyExist = false;
              if (!commonMethods.isNull(shipbillno)) {
                alreadyExist = isDataAvailable("ETT_TRR_SHP_ACK_STG", "SHIPPINGBILLNO",
                  "SHIPPINGBILLDATE", "PORTCODE", shipbillno, shipbilldate, portcode);
              } else {
                alreadyExist = isDataAvailable("ETT_TRR_SHP_ACK_STG", "FORMNO", "SHIPPINGBILLDATE",
                  "PORTCODE", formno, shipbilldate, portcode);
              }
              int setValue = 0;
              String Shipbillno = null;
              String Formno = null;
              if (alreadyExist)
              {
                String trrQuery = "SELECT ERRORCODE FROM ETT_TRR_SHP_ACK_STG WHERE  SHIPPINGBILLDATE=? AND PORTCODE=?";
               
                LoggableStatement pst4 = new LoggableStatement(con, trrQuery);
                if (!commonMethods.isNull(shipbillno))
                {
                  trrQuery = trrQuery + " AND SHIPPINGBILLNO =?";
                  Shipbillno = shipbillno;
                }
                if (!commonMethods.isNull(formno))
                {
                  trrQuery = trrQuery + " AND FORMNO =?";
                  Formno = formno;
                }
                pst4.setString(++setValue, shipbilldate);
                pst4.setString(++setValue, portcode);
                if (Shipbillno != null) {
                  pst4.setString(++setValue, shipbillno);
                }
                if (Formno != null) {
                  pst4.setString(++setValue, formno);
                }
                ResultSet rs4 = pst4.executeQuery();
                String errorStatus = "";
                if (rs4.next()) {
                  errorStatus = rs4.getString("ERRORCODE");
                }
                closeSqlRefferance(rs4, pst4);
                if ((errorStatus != null) && (!errorStatus.equalsIgnoreCase("SUCCESS")))
                {
                  String Shipno = null;
                  String Frmno = null;
                  int setVal = 0;
                  String UPDATE_TRR_XML_DATA = "UPDATE ETT_TRR_SHP_ACK_STG SET EXPORTAGENCY=?, EXPORTTYPE=?, FORMNO=?,IECODE=?, EXISTINGADCODE=?,NEWADCODE=?,FILE_NO=?,ERRORCODE=?  WHERE SHIPPINGBILLDATE=? AND PORTCODE=?";
                  if (!commonMethods.isNull(shipbillno))
                  {
                    UPDATE_TRR_XML_DATA = UPDATE_TRR_XML_DATA + " AND SHIPPINGBILLNO =?";
                    Shipno = shipbillno;
                  }
                  if (!commonMethods.isNull(formno))
                  {
                    UPDATE_TRR_XML_DATA = UPDATE_TRR_XML_DATA + " AND FORMNO =?";
                    Frmno = formno;
                  }
                  LoggableStatement pst5 = new LoggableStatement(con, UPDATE_TRR_XML_DATA);
                  pst5.setString(++setVal, exportagency);
                  pst5.setString(++setVal, exporttype);
                  pst5.setString(++setVal, iecode);
                  pst5.setString(++setVal, exiecode);
                  pst5.setString(++setVal, newadcode);
                  pst5.setInt(++setVal, fiNo);
                  pst5.setString(++setVal, errorcode);
                  pst5.setString(++setVal, shipbilldate);
                  pst5.setString(++setVal, portcode);
                  if (Shipno != null) {
                    pst5.setString(++setVal, shipbillno);
                  }
                  if (Frmno != null) {
                    pst5.setString(++setVal, formno);
                  }
                  logger.info("Executing query : " + pst5.getQueryString());
                  int rr = 0;
                  rr = pst5.executeUpdate();
                  closePreparedStatement(pst5);
                 
                  trrship++;
                  numberbill -= trrship;
                  logger.info(rr + " Rows Updated for TRANSFER ACK SHP Details");
                 
                  count--;
                  if (count == 0) {
                    xmlFileVO.setResult("Y");
                  }
                }
                else
                {
                  String shipbillfail = shipbillno + " " + "Bill No";
                  failingcount++;
                  failBillList.add(shipbillfail);
                }
              }
              else
              {
                LoggableStatement pst5 = new LoggableStatement(con,
                  "INSERT INTO ETT_TRR_SHP_ACK_STG(PORTCODE,EXPORTAGENCY,EXPORTTYPE,SHIPPINGBILLNO,SHIPPINGBILLDATE,FORMNO,IECODE,EXISTINGADCODE,NEWADCODE,FILE_NO,ERRORCODE) values(?,?,?,?,?,?,?,?,?,?,?)");
                pst5.setString(1, portcode);
                pst5.setString(2, exportagency);
                pst5.setString(3, exporttype);
                pst5.setString(4, shipbillno);
                pst5.setString(5, shipbilldate);
                pst5.setString(6, formno);
                pst5.setString(7, iecode);
                pst5.setString(8, exiecode);
                pst5.setString(9, newadcode);
                pst5.setInt(10, fiNo);
                pst5.setString(11, errorcode);
                logger.info("Executing query : " + pst5.getQueryString());
                int rr = 0;
                rr = pst5.executeUpdate();
                closePreparedStatement(pst5);
                trrship++;
                numberbill -= trrship;
                logger.info(rr + " Rows Inserted for TRANSFER ACK SHP Details");
               
                count--;
                if (count == 0) {
                  xmlFileVO.setResult("Y");
                }
              }
            }
          }
          if (failingcount > 0) {
            xmlFileVO.setResult("F");
          }
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadIRMFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("Entering Method");
    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      commonMethods = new CommonMethods();
     
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
     
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
     
      Document doc = docBuilder.parse(xmlFileUpload);
     
      doc.getDocumentElement().normalize();
     
      NodeList chksum = doc.getElementsByTagName("checkSum");
      if (chksum.getLength() > 0)
      {
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() != 1)
        {
          DBConnectionUtility.surrenderDB(con, pst, rs);
          break label775;
        }
        Element firstPersonElement2 = (Element)firstPersonNode;
       
        NodeList numberOfIRM = firstPersonElement2.getElementsByTagName("noOfIrm");
        Element addressElement7 = (Element)numberOfIRM.item(0);
       
        NodeList numOfIRM = addressElement7.getChildNodes();
       
        String noOfIRM = null;
        if (numOfIRM.item(0) == null)
        {
          noOfIRM = "";
        }
        else
        {
          noOfIRM = numOfIRM.item(0).getTextContent().trim();
          logger.info("IRMFile " + noOfIRM);
        }
        NodeList irmRem = doc.getElementsByTagName("remittance");
        for (int temp = 0; temp < irmRem.getLength(); temp++)
        {
          Node firstPersonNode1 = irmRem.item(temp);
          if (firstPersonNode1.getNodeType() == 1)
          {
            Element eElement = (Element)firstPersonNode1;
           
            String irmNumber = eElement.getElementsByTagName("IRMNumber").item(0).getTextContent();
           
            String fircNumber = eElement.getElementsByTagName("fircNumber").item(0).getTextContent();
           
            String irmError = eElement.getElementsByTagName("errorCodes").item(0).getTextContent();
            if (irmNumber != null) {
              irmNumber = irmNumber.trim();
            }
            if (irmError != null) {
              irmError = irmError.trim();
            }
            if (fircNumber != null) {
              fircNumber = fircNumber.trim();
            }
            int i = 0;
            if (!commonMethods.isNull(irmNumber))
            {
              String irmQuery = "SELECT ACK_STATUS FROM ETT_IRM_XML_FILES_TBL WHERE TRXN_REFNO = ? ";
             
              LoggableStatement pst1 = new LoggableStatement(con, irmQuery);
             
              pst1.setString(1, irmNumber);
             
              ResultSet rs1 = pst1.executeQuery();
              String irmAckStatus = "";
              if (rs1.next()) {
                irmAckStatus = commonMethods.getEmptyIfNull(rs1.getString("ACK_STATUS")).trim();
              }
              closeSqlRefferance(rs1, pst1);
              if (!irmAckStatus.equalsIgnoreCase("SUCCESS"))
              {
                String updateQuery1 = "UPDATE  ETT_IRM_XML_FILES_TBL SET ACK_STATUS =?  WHERE TRXN_REFNO=?";
               
                PreparedStatement pst2 = con.prepareStatement(updateQuery1);
                pst2.setString(1, irmError);
                pst2.setString(2, irmNumber);
                i = pst2.executeUpdate();
                closePreparedStatement(pst2);
              }
            }
            if (!commonMethods.isNull(fircNumber))
            {
              String fircQuery = "SELECT ACK_STATUS FROM ETT_FIRC_LODGEMENT WHERE TRANS_REF_NO = ? AND FIRC_SERIAL_NO = ?";
             
              LoggableStatement pst3 = new LoggableStatement(con, fircQuery);
             
              pst3.setString(1, irmNumber);
              pst3.setString(2, fircNumber);
              ResultSet rs3 = pst3.executeQuery();
              String fircAckStatus = "";
              if (rs3.next()) {
                fircAckStatus = commonMethods.getEmptyIfNull(rs3.getString("ACK_STATUS")).trim();
              }
              closeSqlRefferance(rs3, pst3);
              if (!fircAckStatus.equalsIgnoreCase("SUCCESS"))
              {
                String updateQuery2 = "UPDATE ETT_FIRC_LODGEMENT SET ACK_STATUS =? WHERE TRANS_REF_NO =? AND FIRC_SERIAL_NO= ?";
               
                PreparedStatement pst4 = con.prepareStatement(updateQuery2);
                pst4.setString(1, irmError);
                pst4.setString(2, irmNumber);
                pst4.setString(3, fircNumber);
                pst4.executeUpdate();
                closePreparedStatement(pst4);
              }
            }
            if (i > 0) {
              xmlFileVO.setResult("Y");
            } else {
              xmlFileVO.setResult("F");
            }
          }
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    label775:
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadIRFIRCFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("Entering Method");
    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      commonMethods = new CommonMethods();
     
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
     
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
     
      Document doc = docBuilder.parse(xmlFileUpload);
     
      doc.getDocumentElement().normalize();
     
      NodeList chksum = doc.getElementsByTagName("checkSum");
      if (chksum.getLength() > 0)
      {
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() != 1)
        {
          DBConnectionUtility.surrenderDB(con, pst, rs);
          break label629;
        }
        Element firstPersonElement2 = (Element)firstPersonNode;
       
        NodeList numberOfIRM = firstPersonElement2.getElementsByTagName("noOfIrm");
        Element addressElement7 = (Element)numberOfIRM.item(0);
       
        NodeList numOfIRM = addressElement7.getChildNodes();
        String noOfIRM = null;
        if (numOfIRM.item(0) == null)
        {
          noOfIRM = "";
        }
        else
        {
          noOfIRM = numOfIRM.item(0).getTextContent().trim();
          logger.info("IRMFile " + noOfIRM);
        }
        NodeList irmRem = doc.getElementsByTagName("remittance");
        for (int temp = 0; temp < irmRem.getLength(); temp++)
        {
          Node firstPersonNode1 = irmRem.item(temp);
          if (firstPersonNode1.getNodeType() == 1)
          {
            Element eElement = (Element)firstPersonNode1;
           
            String irmNumber = eElement.getElementsByTagName("IRMNumber").item(0).getTextContent();
           
            String fircNumber = eElement.getElementsByTagName("fircNumber").item(0).getTextContent();
           
            String irmError = eElement.getElementsByTagName("errorCodes").item(0).getTextContent();
            if (irmNumber != null) {
              irmNumber = irmNumber.trim();
            }
            if (irmError != null) {
              irmError = irmError.trim();
            }
            if (fircNumber != null) {
              fircNumber = fircNumber.trim();
            }
            int i = 0;
            if (!commonMethods.isNull(fircNumber))
            {
              String fircQuery = "SELECT ACK_STATUS FROM ETT_FIRC_LODGEMENT WHERE TRANS_REF_NO = ? AND FIRC_SERIAL_NO = ?";
             
              LoggableStatement pst1 = new LoggableStatement(con, fircQuery);
             
              pst1.setString(1, irmNumber);
              pst1.setString(2, fircNumber);
              ResultSet rs1 = pst1.executeQuery();
              String fircAckStatus = "";
              if (rs1.next()) {
                fircAckStatus = commonMethods.getEmptyIfNull(rs1.getString("ACK_STATUS")).trim();
              }
              closeSqlRefferance(rs1, pst1);
              if (!fircAckStatus.equalsIgnoreCase("SUCCESS"))
              {
                String updateQuery2 = "UPDATE ETT_FIRC_LODGEMENT SET ACK_STATUS =? WHERE TRANS_REF_NO=?  AND FIRC_SERIAL_NO= ?";
               
                PreparedStatement pst2 = con.prepareStatement(updateQuery2);
                pst2.setString(1, irmError);
                pst2.setString(2, irmNumber);
                pst2.setString(3, fircNumber);
                i = pst2.executeUpdate();
               
                closePreparedStatement(pst2);
              }
            }
            if (i > 0) {
              xmlFileVO.setResult("Y");
            } else {
              xmlFileVO.setResult("F");
            }
          }
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    label629:
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadIRMEXTFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("Entering Method");
    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      commonMethods = new CommonMethods();
     
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
     
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
     
      Document doc = docBuilder.parse(xmlFileUpload);
     
      doc.getDocumentElement().normalize();
     
      NodeList chksum = doc.getElementsByTagName("checkSum");
      if (chksum.getLength() > 0)
      {
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() != 1)
        {
          DBConnectionUtility.surrenderDB(con, pst, rs);
          break label621;
        }
        Element firstPersonElement2 = (Element)firstPersonNode;
       
        NodeList numberOfIRM = firstPersonElement2.getElementsByTagName("noOfIrm");
        Element addressElement7 = (Element)numberOfIRM.item(0);
       
        NodeList numOfIRM = addressElement7.getChildNodes();
        String noOfIRM = null;
        if (numOfIRM.item(0) == null)
        {
          noOfIRM = "";
        }
        else
        {
          noOfIRM = numOfIRM.item(0).getTextContent().trim();
          logger.info("IRMFile " + noOfIRM);
        }
        NodeList irmRem = doc.getElementsByTagName("remittance");
        for (int temp = 0; temp < irmRem.getLength(); temp++)
        {
          Node firstPersonNode1 = irmRem.item(temp);
          if (firstPersonNode1.getNodeType() == 1)
          {
            Element eElement = (Element)firstPersonNode1;
           
            String irmNumber = eElement.getElementsByTagName("IRMNumber").item(0).getTextContent();
           
            String recInd = eElement.getElementsByTagName("recordIndicator").item(0).getTextContent();
           
            String irmError = eElement.getElementsByTagName("errorCodes").item(0).getTextContent();
            if (irmNumber != null) {
              irmNumber = irmNumber.trim();
            }
            if (irmError != null) {
              irmError = irmError.trim();
            }
            if (recInd != null) {
              recInd = recInd.trim();
            }
            int i = 0;
            if (!commonMethods.isNull(irmNumber))
            {
              String irmQuery = "SELECT ACK_STATUS FROM ETT_IRM_EXT_TBL WHERE MASTER_REFNO = ? ";
             
              LoggableStatement pst1 = new LoggableStatement(con, irmQuery);
             
              pst1.setString(1, irmNumber);
              ResultSet rs1 = pst1.executeQuery();
              String irmAckStatus = "";
              if (rs1.next()) {
                irmAckStatus = commonMethods.getEmptyIfNull(rs1.getString("ACK_STATUS")).trim();
              }
              closeSqlRefferance(rs1, pst1);
              if (!irmAckStatus.equalsIgnoreCase("SUCCESS"))
              {
                String updateQuery2 = "UPDATE ETT_IRM_EXT_TBL SET ACK_STATUS =? WHERE MASTER_REFNO=?  AND REC_IND= ?";
               
                PreparedStatement pst2 = con.prepareStatement(updateQuery2);
                pst2.setString(1, irmError);
                pst2.setString(2, irmNumber);
                pst2.setString(3, recInd);
                i = pst2.executeUpdate();
               
                closePreparedStatement(pst2);
              }
            }
            if (i > 0) {
              xmlFileVO.setResult("Y");
            } else {
              xmlFileVO.setResult("F");
            }
          }
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    label621:
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadWsnFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("----------xmlUploadWsnFiles-----------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int wsnship = 0;
    int wsninv = 0;
    int numberbill = 0;
    int numberinvoice = 0;
    int rs11 = 0;
    int rr = 0;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
     
      con = DBConnectionUtility.getConnection();
     
      DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
     
      Date date = new Date();
      if (con == null) {
        logger.info("---------------Connection is NUll-------------");
      }
      String file = "";
      LoggableStatement pst1 = new LoggableStatement(con,
        "SELECT FILE_NAME FROM ETT_WSN_ACK_FILES WHERE FILE_NAME=?");
      pst1.setString(1, fileName);
     
      logger.info("---------------ETT_WSN_ACK_FILES is Name Query-------------" + pst1.getQueryString());
      ResultSet rs1 = pst1.executeQuery();
      if (rs1.next())
      {
        file = rs1.getString("FILE_NAME");
       
        logger.info("---------------ETT_WSN_ACK_FILES is Name file-------------" + file);
      }
      closeStatementResultSet(pst1, rs1);
      if (file.equals(fileName))
      {
        xmlFileVO.setResult("N");
      }
      else
      {
        logger.info("------------New  WSN File------------");
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       
        Document doc = docBuilder.parse(xmlFileUpload);
       
        doc.getDocumentElement().normalize();
       
        NodeList chksum = doc.getElementsByTagName("checkSum");
       
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() == 1)
        {
          Element firstPersonElement = (Element)firstPersonNode;
         
          NodeList ShippingBills = firstPersonElement.getElementsByTagName("noOfShippingBills");
          Element firstNameElement = (Element)ShippingBills.item(0);
         
          logger.info("------------New  WSN File------------" + firstNameElement);
         
          NodeList textFNList = firstNameElement.getChildNodes();
         
          String noofshipbills = textFNList.item(0).getTextContent().trim();
         
          numberbill = Integer.parseInt(noofshipbills);
         
          NodeList Invoices = firstPersonElement.getElementsByTagName("noOfInvoices");
          Element lastNameElement = (Element)Invoices.item(0);
          logger.info("------------New  WSN File------------" + lastNameElement);
         
          NodeList textLNList = lastNameElement.getChildNodes();
         
          String noofinvoices = textLNList.item(0).getTextContent().trim();
         
          numberinvoice = Integer.parseInt(noofinvoices);
         
          int fiNo = 0;
          LoggableStatement pst2 = new LoggableStatement(con, "SELECT WSNACK_SEQNO.NEXTVAL FROM DUAL");
          ResultSet rs2 = pst2.executeQuery();
          if (rs2.next()) {
            fiNo = rs2.getInt("nextval");
          }
          closeStatementResultSet(pst2, rs2);
         
          LoggableStatement pst3 = new LoggableStatement(con,
            "INSERT INTO ETT_WSN_ACK_FILES(FILE_NO,NO_OF_SHIPPING_BILLS,NO_OF_INVOICES,FILE_NAME,STATUS,PROCESSED_DATE) values(?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'))");
          pst3.setInt(1, fiNo);
          pst3.setString(2, noofshipbills);
          pst3.setString(3, noofinvoices);
          pst3.setString(4, fileName);
          pst3.setString(5, "y");
          pst3.setString(6, dateFormat.format(date));
         
          logger.info("------------New  WSN File------------");
          int rsq = 0;
          rsq = pst3.executeUpdate();
         
          logger.info("------------New  WSN File------------");
          logger.info(rsq + " Rows Inserted for WSN ACK FILES Details");
          closePreparedStatement(pst3);
         
          NodeList shbill = doc.getElementsByTagName("shippingBill");
         
          int totalshbill = shbill.getLength();
         
          int billcount = totalshbill;
         
          int failingcount = 0;
         
          List failBillList = new ArrayList();
          for (int y = 0; y < totalshbill; y++)
          {
            Node firstPersonNode1 = shbill.item(y);
            if (firstPersonNode1.getNodeType() == 1)
            {
              Element eElement = (Element)firstPersonNode1;
             
              String portcode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("portCode").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File-----portcode-------" + portcode);
             
              String exporttype = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("exportType").item(0).getTextContent())
                .trim();
              logger.info("------------New  WSN File-------exporttype-----" + exporttype);
             
              String recordind = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("recordIndicator").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File---------recordind---" + recordind);
             
              String shipbillno = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shippingBillNo").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File-------shipbillno-----" + shipbillno);
             
              String shipbilldate = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shippingBillDate").item(0).getTextContent())
                .trim().replace("/", "");
             
              logger.info("------------New  WSN File----shipbilldate--------" + shipbilldate);
             
              String formno = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("formNo").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File------formno------" + formno);
             
              String leodate = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("LEODate").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File----leodate--------" + leodate);
             
              String adcode = commonMethods
                .getEmptyIfNull(eElement.getElementsByTagName("adCode").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File------adcode------" + adcode);
              String portDischarge = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("portOfDischarge").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File-----portDischarge-------" + portDischarge);
             
              String wsnBoeDate = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("billOfEntryDate").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File-----wsnBoeDate-------" + wsnBoeDate);
              String wsnBoeNo = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("billOfEntryNumber").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File----wsnBoeNo--------" + wsnBoeNo);
             
              String shipind = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("shipmentIndicator").item(0).getTextContent())
                .trim();
              logger.info("------------New  WSN File-----shipind-------" + shipind);
             
              String wsnWRTSeqNo = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("writeoffSequence").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File----wsnWRTSeqNo--------" + wsnWRTSeqNo);
             
              String errorcodes = commonMethods
                .getEmptyIfNull(
                eElement.getElementsByTagName("errorCodes").item(0).getTextContent())
                .trim();
             
              logger.info("------------New  WSN File-------errorcodes-----" + errorcodes);
             
              boolean alreadyExist = false;
              if (!commonMethods.isNull(shipbillno)) {
                alreadyExist = isFourDataAvailable("ETT_WSN_SHP_ACK_STG", "SHIPPINGBILLNO",
                  "SHIPPINGBILLDATE", "PORTCODE", "RECORDINDICATOR", shipbillno, shipbilldate,
                  portcode, recordind);
              } else {
                alreadyExist = isFourDataAvailable("ETT_WSN_SHP_ACK_STG", "FORMNO", "SHIPPINGBILLDATE",
                  "PORTCODE", "RECORDINDICATOR", formno, shipbilldate, portcode, recordind);
              }
              int setValue = 0;
              String Shipbillno = null;
              String Formno = null;
              if (alreadyExist)
              {
                String wsnQuery = "SELECT ERRORCODES FROM ETT_WSN_SHP_ACK_STG WHERE SHIPPINGBILLDATE=? AND PORTCODE=? AND RECORDINDICATOR=? ";
                if (!commonMethods.isNull(shipbillno))
                {
                  wsnQuery = wsnQuery + " AND SHIPPINGBILLNO =?";
                  Shipbillno = shipbillno;
                }
                if (!commonMethods.isNull(formno))
                {
                  wsnQuery = wsnQuery + " AND FORMNO =?";
                  Formno = formno;
                }
                LoggableStatement pst4 = new LoggableStatement(con, wsnQuery);
                pst4.setString(++setValue, shipbilldate);
                pst4.setString(++setValue, portcode);
                pst4.setString(++setValue, recordind);
                if (Shipbillno != null) {
                  pst4.setString(++setValue, shipbillno);
                }
                if (Formno != null) {
                  pst4.setString(++setValue, formno);
                }
                logger.info("------------New  WSN File--- ETT_WSN_SHP_ACK_STG-- ERRORCODES  Query-------" + pst4.getQueryString());
                ResultSet rs4 = pst4.executeQuery();
                String errorStatus = "";
                if (rs4.next()) {
                  errorStatus = rs4.getString("ERRORCODES");
                }
                logger.info("------------New  WSN File--- ETT_WSN_SHP_ACK_STG-- ERRORCODES  Query---errorStatus----" + errorStatus);
                closeStatementResultSet(pst4, rs4);
                if ((errorStatus != null) && (!errorStatus.equalsIgnoreCase("SUCCESS")))
                {
                  String UPDATE_WSN_XML_DATA = "UPDATE ETT_WSN_SHP_ACK_STG SET EXPORTTYPE = ?, RECORDINDICATOR = ?,LEODATE = TO_DATE(?, 'DD-MM-YYYY'),ADCODE = ?,PORTDISCHARGE = ?,BOENO = ?,BOEDATE = TO_DATE(?, 'DD-MM-YYYY'),SHIPMENTINDICATOR = ?,WRT_SEQNO = ?,FILE_NO=?,ERRORCODES=?  WHERE SHIPPINGBILLDATE = ? AND PORTCODE = ? ";
                  if (!commonMethods.isNull(shipbillno)) {
                    UPDATE_WSN_XML_DATA =
                      UPDATE_WSN_XML_DATA + " AND SHIPPINGBILLNO ='" + shipbillno + "'";
                  }
                  if (!commonMethods.isNull(formno)) {
                    UPDATE_WSN_XML_DATA = UPDATE_WSN_XML_DATA + " AND FORMNO ='" + formno + "'";
                  }
                  LoggableStatement pst5 = new LoggableStatement(con, UPDATE_WSN_XML_DATA);
                 
                  pst5.setString(1, exporttype);
                  pst5.setString(2, recordind);
                  pst5.setString(3, leodate);
                  pst5.setString(4, adcode);
                  pst5.setString(5, portDischarge);
                  pst5.setString(6, wsnBoeDate);
                  pst5.setString(7, wsnBoeNo);
                  pst5.setString(8, shipind);
                  pst5.setString(9, wsnWRTSeqNo);
                  pst5.setInt(10, fiNo);
                  pst5.setString(11, errorcodes);
                  pst5.setString(12, shipbilldate);
                  pst5.setString(13, portcode);
                 
                  logger.info("------------New  WSN File UPDATE--- ETT_WSN_SHP_ACK_STG--   -------" + pst5.getQueryString());
                  rr = pst5.executeUpdate();
                 
                  logger.info("------------New  WSN File UPDATE--- ETT_WSN_SHP_ACK_STG--   -count------" + rr);
                 
                  closePreparedStatement(pst5);
                 
                  wsnship++;
                 
                  numberbill -= wsnship;
                 
                  logger.info(rr + " Rows Inserted for WSN ACK SHP Details");
                 
                  billcount--;
                 
                  NodeList inv1 = eElement.getElementsByTagName("invoice");
                 
                  int totalinv1 = inv1.getLength();
                 
                  int count = totalinv1;
                  for (int i = 0; i < totalinv1; i++)
                  {
                    Node firstPersonNode111 = inv1.item(i);
                    if (firstPersonNode111.getNodeType() == 1)
                    {
                      Element eElementData = (Element)firstPersonNode111;
                     
                      String invoiceserialno = commonMethods.getEmptyIfNull(eElementData
                        .getElementsByTagName("invoiceSerialNo").item(0).getTextContent())
                        .trim();
                     

                      logger.info("------------New  WSN --invoiceserialno----" + invoiceserialno);
                     
                      String invoiceno = commonMethods.getEmptyIfNull(eElementData
                        .getElementsByTagName("invoiceNo").item(0).getTextContent()).trim();
                     
                      logger.info("------------New  WSN ----invoiceno--" + invoiceno);
                     
                      String invoicedate = commonMethods.getEmptyIfNull(eElementData
                        .getElementsByTagName("invoiceDate").item(0).getTextContent())
                        .trim();
                     
                      logger.info("------------New  WSN --invoicedate----" + invoicedate);
                     
                      String writeind = commonMethods.getEmptyIfNull(eElementData
                        .getElementsByTagName("writeoffIndicator").item(0).getTextContent())
                        .trim();
                     
                      logger.info("------------New  WSN --writeind----" + writeind);
                      String writeamt = commonMethods.getEmptyIfNull(eElementData
                        .getElementsByTagName("writeoffAmount").item(0).getTextContent())
                        .trim();
                     
                      logger.info("------------New  WSN ---writeamt---" + writeamt);
                     
                      String writedate = commonMethods.getEmptyIfNull(eElementData
                        .getElementsByTagName("writeoffDate").item(0).getTextContent())
                        .trim();
                     
                      logger.info("------------New  WSN --writedate----" + writedate);
                     
                      String closebill = commonMethods.getEmptyIfNull(
                        eElementData.getElementsByTagName("closeOfBillIndicator").item(0)
                        .getTextContent())
                        .trim();
                     
                      logger.info("------------New  WSN ---closebill---" + closebill);
                     
                      String invoiceerrorcodes = commonMethods.getEmptyIfNull(eElementData
                        .getElementsByTagName("invoiceErrorCodes").item(0).getTextContent())
                        .trim();
                     
                      logger.info("------------New  WSN ---invoiceerrorcodes---" + invoiceerrorcodes);
                     
                      String Shipno = null;
                      String Frmno = null;
                      int setVal = 0;
                      String UPDATE_ETT_WSN_SHP_INV_ACK_STG = "UPDATE ETT_WSN_SHP_INV_ACK_STG SET INVOICESERIALNO=?, INVOICENO=?, INVOICEDATE = TO_DATE(?, 'DD-MM-YYYY'),WRITEOFFINDICATOR=?,WRITEOFFAMOUNT=?,WRITEOFFDATE=TO_DATE(?, 'DD-MM-YYYY'), CLOSEOFBILLINDICATOR=?,FILE_NO=?,INVOICEERRORCODES=?  WHERE  SHIPPINGBILLDATE=? AND PORTCODE=?";
                      if (!commonMethods.isNull(shipbillno))
                      {
                        UPDATE_ETT_WSN_SHP_INV_ACK_STG =
                          UPDATE_ETT_WSN_SHP_INV_ACK_STG + " AND SHIPPINGBILLNO =?";
                        Shipno = shipbillno;
                      }
                      if (!commonMethods.isNull(formno))
                      {
                        UPDATE_ETT_WSN_SHP_INV_ACK_STG =
                          UPDATE_ETT_WSN_SHP_INV_ACK_STG + " AND FORMNO =?";
                        Frmno = formno;
                      }
                      LoggableStatement pst6 = new LoggableStatement(con,
                        UPDATE_ETT_WSN_SHP_INV_ACK_STG);
                      pst6.setString(++setVal, invoiceserialno);
                      pst6.setString(++setVal, invoiceno);
                      pst6.setString(++setVal, invoicedate);
                      pst6.setString(++setVal, writeind);
                      pst6.setString(++setVal, writeamt);
                      pst6.setString(++setVal, writedate);
                      pst6.setString(++setVal, closebill);
                      pst6.setInt(++setVal, fiNo);
                      pst6.setString(++setVal, invoiceerrorcodes);
                      pst6.setString(++setVal, shipbilldate);
                      pst6.setString(++setVal, portcode);
                      if (Shipno != null) {
                        pst5.setString(++setVal, shipbillno);
                      }
                      if (Frmno != null) {
                        pst5.setString(++setVal, formno);
                      }
                      logger.info("-----------  WSN --- UPDATE ETT_WSN_SHP_INV_ACK_STG-- Query -" + pst6.getQueryString());
                      rs11 = pst6.executeUpdate();
                     
                      logger.info("------------New  WSN -- UPDATE ETT_WSN_SHP_INV_ACK_STG-- Count ----" + rs11);
                      closePreparedStatement(pst6);
                     
                      wsninv++;
                     
                      numberinvoice -= wsninv;
                     
                      logger.info(rs11 + " Rows Inserted for WSN ACK INV Invoice Details");
                     
                      count--;
                      if ((count == 0) && (billcount == 0)) {
                        xmlFileVO.setResult("Y");
                      }
                    }
                  }
                }
                else
                {
                  String shipbillfail = shipbillno + " " + "Bill No Failed";
                  failingcount++;
                  failBillList.add(shipbillfail);
                }
              }
              else
              {
                LoggableStatement pst5 = new LoggableStatement(con,
                  "INSERT INTO ETT_WSN_SHP_ACK_STG(PORTCODE,EXPORTTYPE,RECORDINDICATOR,SHIPPINGBILLNO,SHIPPINGBILLDATE,FORMNO,LEODATE,ADCODE,PORTDISCHARGE,BOENO,BOEDATE,SHIPMENTINDICATOR,WRT_SEQNO,FILE_NO,ERRORCODES) values(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?)");
                pst5.setString(1, portcode);
                pst5.setString(2, exporttype);
                pst5.setString(3, recordind);
                pst5.setString(4, shipbillno);
                pst5.setString(5, shipbilldate);
                pst5.setString(6, formno);
                pst5.setString(7, leodate);
                pst5.setString(8, adcode);
                pst5.setString(9, portDischarge);
                pst5.setString(10, wsnBoeNo);
                pst5.setString(11, wsnBoeDate);
                pst5.setString(12, shipind);
                pst5.setString(13, wsnWRTSeqNo);
                pst5.setInt(14, fiNo);
                pst5.setString(15, errorcodes);
               


                logger.info("------------New  WSN ---INSERT INTO ETT_WSN_SHP_ACK_STG---" + pst5.getQueryString());
                rr = pst5.executeUpdate();
               
                logger.info("------------New  WSN --INSERT INTO ETT_WSN_SHP_ACK_STG COunt----" + rr);
                closePreparedStatement(pst5);
               
                wsnship++;
               
                numberbill -= wsnship;
               
                logger.info(rr + " Rows Inserted for WSN ACK SHP Details");
               
                billcount--;
               
                NodeList inv1 = eElement.getElementsByTagName("invoice");
               

                logger.info(" -----NodeList-----------inv1-------------------" + inv1);
               
                int totalinv1 = inv1.getLength();
               
                int count = totalinv1;
               
                int invfailingcount = 0;
                for (int i = 0; i < totalinv1; i++)
                {
                  Node firstPersonNode111 = inv1.item(i);
                  if (firstPersonNode111.getNodeType() == 1)
                  {
                    Element eElementData = (Element)firstPersonNode111;
                   
                    String invoiceserialno = commonMethods.getEmptyIfNull(eElementData
                      .getElementsByTagName("invoiceSerialNo").item(0).getTextContent())
                      .trim();
                   

                    logger.info("------------New  WSN ---invoiceserialno---" + invoiceserialno);
                   
                    String invoiceno = commonMethods.getEmptyIfNull(
                      eElementData.getElementsByTagName("invoiceNo").item(0).getTextContent())
                      .trim();
                   
                    logger.info("------------New  WSN ----invoiceno--" + invoiceno);
                    String invoicedate = commonMethods.getEmptyIfNull(eElementData
                      .getElementsByTagName("invoiceDate").item(0).getTextContent()).trim();
                   
                    logger.info("------------New  WSN ----invoicedate--" + invoicedate);
                   
                    String writeind = commonMethods.getEmptyIfNull(eElementData
                      .getElementsByTagName("writeoffIndicator").item(0).getTextContent())
                      .trim();
                   
                    logger.info("------------New  WSN writeind------" + writeind);
                   
                    String writeamt = commonMethods.getEmptyIfNull(eElementData
                      .getElementsByTagName("writeoffAmount").item(0).getTextContent())
                      .trim();
                   
                    logger.info("------------New  WSN ---writeamt---" + writeamt);
                   
                    String writedate = commonMethods.getEmptyIfNull(eElementData
                      .getElementsByTagName("writeoffDate").item(0).getTextContent()).trim();
                   
                    logger.info("------------New  WSN ---writedate---" + writedate);
                   
                    String closebill = commonMethods.getEmptyIfNull(eElementData
                      .getElementsByTagName("closeOfBillIndicator").item(0).getTextContent())
                      .trim();
                   
                    logger.info("------------New  WSN ----closebill--" + closebill);
                   
                    String invoiceerrorcodes = commonMethods.getEmptyIfNull(eElementData
                      .getElementsByTagName("invoiceErrorCodes").item(0).getTextContent())
                      .trim();
                   
                    logger.info("------------New  WSN -invoiceerrorcodes-----" + invoiceerrorcodes);
                   
                    LoggableStatement pst6 = new LoggableStatement(con,
                      "INSERT INTO ETT_WSN_SHP_INV_ACK_STG(INVOICESERIALNO,INVOICENO,INVOICEDATE,WRITEOFFINDICATOR,WRITEOFFAMOUNT,WRITEOFFDATE,CLOSEOFBILLINDICATOR,FILE_NO,SHIPPINGBILLNO,SHIPPINGBILLDATE,PORTCODE,FORMNO,INVOICEERRORCODES) values(?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?)");
                    pst6.setString(1, invoiceserialno);
                    pst6.setString(2, invoiceno);
                    pst6.setString(3, invoicedate);
                    pst6.setString(4, writeind);
                    pst6.setString(5, writeamt);
                    pst6.setString(6, writedate);
                    pst6.setString(7, closebill);
                    pst6.setInt(8, fiNo);
                    pst6.setString(9, shipbillno);
                    pst6.setString(10, shipbilldate);
                    pst6.setString(11, portcode);
                    pst6.setString(12, formno);
                    pst6.setString(13, invoiceerrorcodes);
                   

                    logger.info("------------New  WSN -- INSERT INTO ETT_WSN_SHP_INV_ACK_STG-- query--" + pst6.getQueryString());
                    rs11 = pst6.executeUpdate();
                    logger.info("------------New  WSN -- INSERT INTO ETT_WSN_SHP_INV_ACK_STG-- query insertion count--" + rs11);
                   

                    closePreparedStatement(pst6);
                   
                    wsninv++;
                   
                    numberinvoice -= wsninv;
                   
                    logger.info(rs11 + " Rows Inserted for WSN ACK INV Invoice Details");
                   
                    count--;
                    if ((count == 0) && (billcount == 0)) {
                      xmlFileVO.setResult("Y");
                    }
                  }
                }
                if (invfailingcount > 0) {
                  xmlFileVO.setResult("F");
                }
              }
              xmlFileVO.setShpCount(wsnship);
              xmlFileVO.setInvCount(wsninv);
             
              logger.info("Shipping  wsnship-----------" + wsnship);
              logger.info("Invoice  wsninv  -----------" + wsninv);
             
              logger.info("Shipping  Inbound Bill -----------" + rr);
              logger.info("Invoice  Inbound Bill -----------" + rs11);
            }
          }
          if (failingcount > 0) {
            xmlFileVO.setResult("F");
          }
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("------------New  WSN - exception-----" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("-End of   WSN-----------");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadIRMCLSFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("Entering Method");
    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      commonMethods = new CommonMethods();
     
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
     
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
     
      Document doc = docBuilder.parse(xmlFileUpload);
     
      doc.getDocumentElement().normalize();
     
      NodeList chksum = doc.getElementsByTagName("checkSum");
      if (chksum.getLength() > 0)
      {
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() != 1)
        {
          DBConnectionUtility.surrenderDB(con, pst, rs);
          break label621;
        }
        Element firstPersonElement2 = (Element)firstPersonNode;
       
        NodeList numberOfIRM = firstPersonElement2.getElementsByTagName("noOfIrm");
        Element addressElement7 = (Element)numberOfIRM.item(0);
       
        NodeList numOfIRM = addressElement7.getChildNodes();
        String noOfIRM = null;
        if (numOfIRM.item(0) == null)
        {
          noOfIRM = "";
        }
        else
        {
          noOfIRM = numOfIRM.item(0).getTextContent().trim();
          logger.info("IRMFile " + noOfIRM);
        }
        NodeList irmRem = doc.getElementsByTagName("remittance");
        for (int temp = 0; temp < irmRem.getLength(); temp++)
        {
          Node firstPersonNode1 = irmRem.item(temp);
          if (firstPersonNode1.getNodeType() == 1)
          {
            Element eElement = (Element)firstPersonNode1;
           
            String irmNumber = eElement.getElementsByTagName("IRMNumber").item(0).getTextContent();
           
            String recInd = eElement.getElementsByTagName("recordIndicator").item(0).getTextContent();
           
            String irmError = eElement.getElementsByTagName("errorCodes").item(0).getTextContent();
            if (irmNumber != null) {
              irmNumber = irmNumber.trim();
            }
            if (irmError != null) {
              irmError = irmError.trim();
            }
            if (recInd != null) {
              recInd = recInd.trim();
            }
            int i = 0;
            if (!commonMethods.isNull(irmNumber))
            {
              String irmQuery = "SELECT ACK_STATUS FROM ETT_IRM_CLOSURE_TBL WHERE MASTER_REFNO = ? ";
             
              LoggableStatement pst1 = new LoggableStatement(con, irmQuery);
             
              pst1.setString(1, irmNumber);
             
              ResultSet rs1 = pst1.executeQuery();
              String irmAckStatus = "";
              if (rs1.next()) {
                irmAckStatus = commonMethods.getEmptyIfNull(rs1.getString("ACK_STATUS")).trim();
              }
              closeSqlRefferance(rs1, pst1);
              if (!irmAckStatus.equalsIgnoreCase("SUCCESS"))
              {
                String updateQuery2 = "UPDATE ETT_IRM_CLOSURE_TBL SET ACK_STATUS =? WHERE MASTER_REFNO=?  AND REC_IND=?";
               

                PreparedStatement pst2 = con.prepareStatement(updateQuery2);
                pst2.setString(1, irmError);
                pst2.setString(2, irmNumber);
                pst2.setString(3, recInd);
                i = pst2.executeUpdate();
               
                closePreparedStatement(pst2);
              }
            }
            if (i > 0) {
              xmlFileVO.setResult("Y");
            } else {
              xmlFileVO.setResult("F");
            }
          }
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    label621:
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO xmlUploadEbrcFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("Entering Method");
    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
     
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
     
      Document doc = docBuilder.parse(xmlFileUpload);
     
      doc.getDocumentElement().normalize();
     
      NodeList chksum = doc.getElementsByTagName("EBRCACKG");
     
      NodeList ifscChksum = doc.getElementsByTagName("IFSCACKG");
      if (chksum.getLength() > 0)
      {
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() == 1)
        {
          Element firstPersonElement2 = (Element)firstPersonNode;
         
          NodeList fileName1 = firstPersonElement2.getElementsByTagName("FILENAME");
          Element addressElement7 = (Element)fileName1.item(0);
         
          NodeList ebrcfileName = addressElement7.getChildNodes();
          String ebrcFile = null;
          if (ebrcfileName.item(0) == null)
          {
            ebrcFile = "";
          }
          else
          {
            ebrcFile = ebrcfileName.item(0).getTextContent().trim();
            logger.info("ebrcFile " + ebrcFile);
          }
          NodeList fileError = firstPersonElement2.getElementsByTagName("ERRCODE");
          Element addressElement9 = (Element)fileError.item(0);
         
          NodeList filecode = addressElement9.getChildNodes();
          String fileerrorcode = null;
          if (filecode.item(0) == null)
          {
            fileerrorcode = "";
          }
          else
          {
            fileerrorcode = filecode.item(0).getTextContent().trim();
            logger.info("fileerrorcode " + fileerrorcode);
          }
          NodeList ebrc = doc.getElementsByTagName("EBRC");
          for (int temp = 0; temp < ebrc.getLength(); temp++)
          {
            Node firstPersonNode1 = ebrc.item(temp);
            if (firstPersonNode1.getNodeType() == 1)
            {
              Element eElement = (Element)firstPersonNode1;
             
              String brcNumber = eElement.getElementsByTagName("BRCNO").item(0).getTextContent();
             
              String brcerror = eElement.getElementsByTagName("ERRCODE").item(0).getTextContent();
              if (brcNumber != null) {
                brcNumber = brcNumber.trim();
              }
              if (brcerror != null) {
                brcerror = brcerror.trim();
              }
              logger.info("BRC No : " + brcNumber);
              logger.info("Error Codes : " + brcerror);
             
              String update = "UPDATE  ETT_EBRC_FILES SET STATUS=? WHERE BRCNO=? ";
              pst = con.prepareStatement(update);
              pst.setString(1, brcerror);
              pst.setString(2, brcNumber);
              int i = pst.executeUpdate();
              if (i > 0) {
                xmlFileVO.setResult("Y");
              } else {
                xmlFileVO.setResult("F");
              }
              logger.info("update 5 " + update);
            }
          }
        }
      }
      else if (ifscChksum.getLength() > 0)
      {
        Node firstPersonNode = ifscChksum.item(0);
        if (firstPersonNode.getNodeType() != 1)
        {
          DBConnectionUtility.surrenderDB(con, pst, rs);
          break label1146;
        }
        Element firstPersonElement2 = (Element)firstPersonNode;
       
        NodeList fileName1 = firstPersonElement2.getElementsByTagName("FILENAME");
        Element addressElement7 = (Element)fileName1.item(0);
       
        NodeList ebrcfileName = addressElement7.getChildNodes();
        String ebrcFile = null;
        if (ebrcfileName.item(0) == null)
        {
          ebrcFile = "";
        }
        else
        {
          ebrcFile = ebrcfileName.item(0).getTextContent().trim();
          logger.info("IfscFile " + ebrcFile);
        }
        NodeList fileError = firstPersonElement2.getElementsByTagName("ERRCODE");
        Element addressElement9 = (Element)fileError.item(0);
       
        NodeList filecode = addressElement9.getChildNodes();
        String fileerrorcode = null;
        if (filecode.item(0) == null)
        {
          fileerrorcode = "";
        }
        else
        {
          fileerrorcode = filecode.item(0).getTextContent().trim();
          logger.info("fileerrorcode " + fileerrorcode);
        }
        NodeList ifsc = doc.getElementsByTagName("IFSC");
        for (int temp = 0; temp < ifsc.getLength(); temp++)
        {
          Node firstPersonNode1 = ifsc.item(temp);
          if (firstPersonNode1.getNodeType() == 1)
          {
            Element eElement = (Element)firstPersonNode1;
           
            String ifsCode = eElement.getElementsByTagName("IFSCODE").item(0).getTextContent();
           
            String ifscError = eElement.getElementsByTagName("ERRCODE").item(0).getTextContent();
            if (ifsCode != null) {
              ifsCode = ifsCode.trim();
            }
            if (ifscError != null) {
              ifscError = ifscError.trim();
            }
            logger.info("BRC No : " + ifsCode);
            logger.info("Error Codes : " + ifscError);
           
            String update = "UPDATE  ETT_IFSC_FILES SET STATUS=?  WHERE IFSCCODE=? ";
            pst = con.prepareStatement(update);
            pst.setString(1, ifscError);
            pst.setString(2, ifsCode);
            int i = pst.executeUpdate();
            if (i > 0) {
              xmlFileVO.setResult("Y");
            } else {
              xmlFileVO.setResult("F");
            }
            logger.info("update 5 " + update);
          }
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    label1146:
    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public int createTempRODAckData(Connection con, String shippingBillNo, String shippingBillDate, String portCode, String formno)
    throws DAOException
  {
    logger.info("---------------createTempRODAckData------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    int count = 0;
    CommonMethods commonMethods = null;
    try
    {
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      commonMethods = new CommonMethods();
     
      int setValue = 0;
      String Shipbillno = null;
      String Formno = null;
      String tempRodQuery = "SELECT PORTCODE,EXPORTTYPE,RECORDINDICATOR,SHIPPINGBILLNO,SHIPPINGBILLDATE, FORMNO,TO_CHAR(TO_DATE(LEODATE,'DD-MM-YY'),'DD-MM-YY') AS LEODATE,IECODE,CHANGEDIECODE,ADCODE,ADEXPORTAGENCY,DIRECTDISPATCHINDICATOR,ADBILLNO, TO_CHAR(TO_DATE(DATEOFNEGOTIATION,'DD-MM-YY'),'DD-MM-YY') AS DATEOFNEGOTIATION,BUYERNAME,BUYERCOUNTRY,FILE_NO,ERRORCODES FROM ETT_SHP_ROD_ACK_STG  WHERE SHIPPINGBILLDATE=? AND PORTCODE=? ";
      if (!commonMethods.isNull(shippingBillNo))
      {
        tempRodQuery = tempRodQuery + " AND SHIPPINGBILLNO =?";
        Shipbillno = shippingBillNo;
      }
      if (!commonMethods.isNull(formno))
      {
        tempRodQuery = tempRodQuery + " AND FORMNO =?";
        Formno = formno;
      }
      pst = new LoggableStatement(con, tempRodQuery);
      pst.setString(++setValue, shippingBillDate);
      pst.setString(++setValue, portCode);
      if (Shipbillno != null) {
        pst.setString(++setValue, shippingBillNo);
      }
      if (Formno != null) {
        pst.setString(++setValue, formno);
      }
      logger.info("---------------createTempRODAckData------ query--- ---" + pst.getQueryString());
      rs = pst.executeQuery();
      if (rs.next())
      {
        String shipBillNo = commonMethods.getEmptyIfNull(rs.getString("SHIPPINGBILLNO")).trim();
        String shipBillDate = commonMethods.getEmptyIfNull(rs.getString("SHIPPINGBILLDATE")).trim();
        String prtCode = commonMethods.getEmptyIfNull(rs.getString("PORTCODE")).trim();
        String recInd = commonMethods.getEmptyIfNull(rs.getString("RECORDINDICATOR")).trim();
        String expType = commonMethods.getEmptyIfNull(rs.getString("EXPORTTYPE")).trim();
        String formNo = commonMethods.getEmptyIfNull(rs.getString("FORMNO")).trim();
        String leoDate = commonMethods.getEmptyIfNull(rs.getString("LEODATE")).trim();
        String ieCode = commonMethods.getEmptyIfNull(rs.getString("IECODE")).trim();
        String changedIecode = commonMethods.getEmptyIfNull(rs.getString("CHANGEDIECODE")).trim();
        String adcode = commonMethods.getEmptyIfNull(rs.getString("ADCODE")).trim();
        String adExportAgency = commonMethods.getEmptyIfNull(rs.getString("ADEXPORTAGENCY")).trim();
        String directDispatchInd = commonMethods.getEmptyIfNull(rs.getString("DIRECTDISPATCHINDICATOR")).trim();
        String dateOfNgo = commonMethods.getEmptyIfNull(rs.getString("DATEOFNEGOTIATION")).trim();
        String adBillNo = commonMethods.getEmptyIfNull(rs.getString("ADBILLNO")).trim();
        String buyerName = commonMethods.getEmptyIfNull(rs.getString("BUYERNAME")).trim();
        String buyerCountry = commonMethods.getEmptyIfNull(rs.getString("BUYERCOUNTRY")).trim();
        String fileNo = commonMethods.getEmptyIfNull(rs.getString("FILE_NO")).trim();
        String errorCode = commonMethods.getEmptyIfNull(rs.getString("ERRORCODES")).trim();
       
        LoggableStatement pst1 = new LoggableStatement(con,
          "INSERT INTO ETT_SHP_ROD_ACK_STG(PORTCODE,EXPORTTYPE,RECORDINDICATOR,SHIPPINGBILLNO,SHIPPINGBILLDATE,FORMNO,LEODATE,IECODE,CHANGEDIECODE,ADCODE,ADEXPORTAGENCY,DIRECTDISPATCHINDICATOR,ADBILLNO,DATEOFNEGOTIATION,BUYERNAME,BUYERCOUNTRY,FILE_NO,ERRORCODES) VALUES(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?)");
        pst1.setString(1, prtCode);
        pst1.setString(2, expType);
        pst1.setString(3, recInd);
        pst1.setString(4, shipBillNo);
        pst1.setString(5, shipBillDate);
        pst1.setString(6, formNo);
        pst1.setString(7, leoDate);
        pst1.setString(8, ieCode);
        pst1.setString(9, changedIecode);
        pst1.setString(10, adcode);
        pst1.setString(11, adExportAgency);
        pst1.setString(12, directDispatchInd);
        pst1.setString(13, adBillNo);
        pst1.setString(14, dateOfNgo);
        pst1.setString(15, buyerName);
        pst1.setString(16, buyerCountry);
        pst1.setString(17, fileNo);
        pst1.setString(18, errorCode);
        logger.info(pst1.getQueryString());
        logger.info("Temp ROD Table Insert Query-->" + pst1.getQueryString());
        count = pst1.executeUpdate();
        closePreparedStatement(pst1);
      }
      closeStatementResultSet(pst, rs);
    }
    catch (Exception exception)
    {
      logger.info("---------------createTempRODAckData------ Exception--- ---" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
 
  public XMLFileVO xmlUploadMdfFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("--------------xmlUploadMdfFiles---------------------");
   
    int ij = 0;
   

    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int inboundship = 0;
    int inbound_upd = 0;
    int invLength = 0;
    int iShpCount = 0;
    int iShpInvCount = 0;
    int iSofCount = 0;
    int iSofInvCount = 0;
    int iErrCount = 0;
    int toatl_inv = 0;
    boolean error_status = false;
    ShippingDetailsVO shippingVO = null;
    InvoiceDetailsVO invoiceVO = null;
    CommonMethods commonMethods = null;
    ArrayList invoiceList = null;
    ArrayList xmlErrorList = null;
    ExcelDataVO excelDataVO = null;
    int staging = 0;
    try
    {
      commonMethods = new CommonMethods();
      xmlErrorList = new ArrayList();
     
      con = DBConnectionUtility.getConnection();
     
      DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
      Date date = new Date();
      logger.info("Connection Created - " + con);
      logger.info("Connection Created - " + con);
      String sFile = "";
      LoggableStatement p1 = new LoggableStatement(con, "SELECT FILENAME FROM ETT_EDPMS_FILES WHERE FILENAME =?");
      p1.setString(1, fileName);
      ResultSet rs1 = p1.executeQuery();
     
      logger.info("File Name Query Checking--------------------------->" + p1.getQueryString());
      if (rs1.next()) {
        sFile = rs1.getString("FILENAME");
      }
      closeStatementResultSet(p1, rs1);
     

      logger.info("sFile-------file name chekcing---------File Name-------" + sFile);
     


      XMLFileVO xmlFileVO1 = validateMDFXMLTags(xmlFileUpload, fileName);
     

      logger.info("The Return String Value is : " + xmlFileVO1.getStatusRes());
      XMLFileVO localXMLFileVO1;
      if (xmlFileVO1.getStatusRes().trim().equalsIgnoreCase("false"))
      {
        xmlFileVO.setResult("XMLE");
        xmlFileVO.setXmlErrTagName(xmlFileVO1.getTagName());
        localXMLFileVO1 = xmlFileVO;return localXMLFileVO1;
      }
      logger.info("Uploading File Name--------->" + fileName);
     
      logger.info("DBFile Name------------------->" + sFile);
      if (sFile.equals(fileName))
      {
        xmlFileVO.setResult("N");
      }
      else
      {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       
        Document doc = docBuilder.parse(xmlFileUpload);
       
        doc.getDocumentElement().normalize();
        NodeList chksum = doc.getElementsByTagName("checkSum");
        Node firstPersonNode = chksum.item(0);
        if (firstPersonNode.getNodeType() == 1)
        {
          Element firstPersonElement = (Element)firstPersonNode;
          NodeList ShippingBills = firstPersonElement.getElementsByTagName("noOfShippingBills");
          Element firstNameElement = (Element)ShippingBills.item(0);
         

          NodeList noofshipbill = firstNameElement.getChildNodes();
         
          String noofshipbills = noofshipbill.item(0).getTextContent().trim();
         
          NodeList Invoices = firstPersonElement.getElementsByTagName("noOfInvoices");
          Element lastNameElement = (Element)Invoices.item(0);
         
          NodeList noofinvoice = lastNameElement.getChildNodes();
          String noofinvoices = noofinvoice.item(0).getTextContent().trim();
          logger.info("Sequence-----");
          int FINO = 0;
          LoggableStatement p2 = new LoggableStatement(con, "SELECT EDPMS_SEQNO.nextval FROM DUAL");
          ResultSet rs2 = p2.executeQuery();
          if (rs2.next()) {
            FINO = rs2.getInt("nextval");
          }
          closeStatementResultSet(p2, rs2);
          logger.info("file no" + FINO);
          logger.info("Insert into edpms files-----");
          LoggableStatement ps1 = new LoggableStatement(con,
            "INSERT INTO ETT_EDPMS_FILES(FILENO,NOOFSHIPBILLS,NOOFINV,FILENAME,STATUS,PROCESSDATE,CREATEDON_DATE) values(?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'),SYSTIMESTAMP)");
         
          ps1.setInt(1, FINO);
          ps1.setString(2, noofshipbills);
          ps1.setString(3, noofinvoices);
          ps1.setString(4, fileName);
          ps1.setString(5, "Y");
          ps1.setString(6, dateFormat.format(date));
         

          int rsq = 0;
          rsq = ps1.executeUpdate();
          closeStatementResultSet(ps1, null);
         
          logger.info("ETT_EDPMS_FILES----------Inserted Values Query--------" + ps1.getQueryString());
         
          logger.info(rsq + " Rows Inserted for EDPMS FILES Details");
         

          NodeList shBills = doc.getElementsByTagName("shippingBills");
         
          NodeList shbill = doc.getElementsByTagName("shippingBill");
         
          int totalshbill = shbill.getLength();
          for (int y = 0; y < totalshbill; y++)
          {
            String shipErrorString = "";
            shippingVO = new ShippingDetailsVO();
            invoiceList = new ArrayList();
           
            Node firstPersonNode1 = shbill.item(y);
            if (firstPersonNode1.getNodeType() == 1)
            {
              Element firstPersonElement2 = (Element)firstPersonNode1;
             
              shippingVO.setShippingBillNo(commonMethods.getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("shippingBillNo").item(0).getTextContent())
                .trim());
             
              String shippingbilldate = firstPersonElement2.getElementsByTagName("shippingBillDate")
                .item(0).getTextContent();
             
              shippingVO.setShippingBillDate(
                commonMethods.getEmptyIfNull(shippingbilldate).trim().replace("/", ""));
             
              shippingVO.setLeoDate(commonMethods.getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("LEODate").item(0).getTextContent())
                .trim());
             
              shippingVO.setCustSerialNo(commonMethods
                .getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("custNo").item(0).getTextContent())
                .trim());
             
              shippingVO.setFormNo(commonMethods
                .getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("formNo").item(0).getTextContent())
                .trim());
             
              shippingVO.setIeCode(commonMethods
                .getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("IECode").item(0).getTextContent())
                .trim());
             
              shippingVO.setAdCode(commonMethods
                .getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("adCode").item(0).getTextContent())
                .trim());
             
              shippingVO.setPortCode(commonMethods.getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("portCode").item(0).getTextContent())
                .trim());
             
              shippingVO.setExportAgency(commonMethods.getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("exportAgency").item(0).getTextContent())
                .trim());
             
              shippingVO.setExportType(commonMethods.getEmptyIfNull(
                firstPersonElement2.getElementsByTagName("exportType").item(0).getTextContent())
                .trim());
             
              shippingVO.setRecInd(commonMethods.getEmptyIfNull(firstPersonElement2
                .getElementsByTagName("recordIndicator").item(0).getTextContent()).trim());
             
              shippingVO.setCountryOfDest(commonMethods.getEmptyIfNull(firstPersonElement2
                .getElementsByTagName("countryOfDestination").item(0).getTextContent()).trim());
              if ((!commonMethods.isNull(shippingVO.getExportType())) &&
                (!shippingVO.getExportType().equalsIgnoreCase("2")) &&
                (commonMethods.isNull(shippingVO.getShippingBillNo()))) {
                shipErrorString = commonMethods.setErrorString(shipErrorString,
                  "Shipping Bill Number is Empty");
              }
              if (commonMethods.isNull(shippingVO.getShippingBillDate())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString,
                  "Shipping Bill Date is Empty");
              }
              if (commonMethods.isNull(shippingVO.getLeoDate())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString, "Leo Date is Empty");
              }
              if ((!commonMethods.isNull(shippingVO.getExportType())) &&
                (shippingVO.getExportType().equalsIgnoreCase("2")) &&
                (commonMethods.isNull(shippingVO.getFormNo()))) {
                shipErrorString = commonMethods.setErrorString(shipErrorString, "Form Number is Empty");
              }
              if (commonMethods.isNull(shippingVO.getIeCode())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString, "IECode is Empty");
              }
              if (commonMethods.isNull(shippingVO.getAdCode())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString, "ADCode is Empty");
              }
              if (commonMethods.isNull(shippingVO.getPortCode())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString, "Portcode is Empty");
              }
              if (commonMethods.isNull(shippingVO.getExportType())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString, "Export Type is Empty");
              }
              if (commonMethods.isNull(shippingVO.getExportAgency())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString,
                  "Export Agency is Empty");
              }
              if (commonMethods.isNull(shippingVO.getRecInd())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString,
                  "Record Indicator is Empty");
              }
              if (commonMethods.isNull(shippingVO.getCountryOfDest())) {
                shipErrorString = commonMethods.setErrorString(shipErrorString,
                  "Country Of Destination is Empty");
              }
              shippingVO.setFileNo(FINO);
             

              shippingVO.setErrorString(shipErrorString);
             

              logger.info("Error String ---------------" + shipErrorString);
             

              NodeList inv1 = firstPersonElement2.getElementsByTagName("invoice");
             
              int totalinv1 = inv1.getLength();
              invLength += inv1.getLength();
             
              logger.info("totalinv1 in the XML Tag - " + totalinv1);
              for (int i = 0; i < totalinv1; i++)
              {
                invoiceVO = new InvoiceDetailsVO();
               
                Node firstPersonNode111 = inv1.item(i);
                if (firstPersonNode111.getNodeType() == 1)
                {
                  Element firstPersonElement211 = (Element)firstPersonNode111;
                 
                  invoiceVO.setInvoiceSerialNo(commonMethods
                    .getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("invoiceSerialNo").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO
                    .setInvoiceNo(commonMethods
                    .getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("invoiceNo").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO.setInvoiceDate(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("invoiceDate").item(0).getTextContent()).trim());
                 
                  invoiceVO.setFobCurr(commonMethods
                    .getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("FOBCurrencyCode").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO.setFobAmt(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("FOBAmt").item(0).getTextContent()).trim());
                 
                  invoiceVO.setFreightCurr(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("freightCurrencyCode").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO.setFreightAmt(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("freightAmt").item(0).getTextContent()).trim());
                 
                  invoiceVO.setInsuranceCurr(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("insuranceCurrencyCode").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO.setInsuranceAmt(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("insuranceAmt").item(0).getTextContent()).trim());
                 
                  invoiceVO.setCommissionCurr(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("commissionCurrencyCode").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO.setCommissionAmt(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("commissionAmt").item(0).getTextContent()).trim());
                 
                  invoiceVO.setDiscountCurr(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("discountCurrencyCode").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO.setDiscountAmt(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("discountAmt").item(0).getTextContent()).trim());
                 
                  invoiceVO.setDeductionCurr(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("deductionsCurrencyCode").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO.setDeductionAmt(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("deductionsAmt").item(0).getTextContent()).trim());
                 
                  invoiceVO.setPackagingCurr(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("packagingCurrencyCode").item(0).getTextContent())
                    .trim());
                 
                  invoiceVO.setPackagingAmt(commonMethods.getEmptyIfNull(firstPersonElement211
                    .getElementsByTagName("packagingChargesAmt").item(0).getTextContent())
                    .trim());
                 
                  invoiceList.add(invoiceVO);
                }
              }
            }
            logger.info("shippingVO.getErrorString()-----------------------" + shippingVO.getErrorString());
            logger.info("commonMethods.isNull(shippingVO.getRecInd())-------------------" + commonMethods.isNull(shippingVO.getRecInd()));
            staging = insertStaging(con, shippingVO);
            if (commonMethods.isNull(shippingVO.getErrorString()))
            {
              if ((!shippingVO.getShippingBillNo().equalsIgnoreCase("")) && (shippingVO.getExportType().equalsIgnoreCase("1")) && (shippingVO.getFormNo().equalsIgnoreCase("")))
              {
                LoggableStatement ship_pst = new LoggableStatement(con,
               
                  "SELECT RECIND  AS RECIND,SHIPBILLNO,MIG_SOURCE FROM ETT_EDPMS_SHP WHERE SHIPBILLNO=? AND SHIPBILLDATE =? AND PORTCODE=?");
                ship_pst.setString(1, shippingVO.getShippingBillNo());
                ship_pst.setString(2, shippingVO.getShippingBillDate());
                ship_pst.setString(3, shippingVO.getPortCode());
                logger.info("ship_pst query - " + ship_pst.getQueryString());
                ResultSet ship_rs = ship_pst.executeQuery();
                String recInd = null;
                String shpno = null;
                String migSrc = null;
                logger.info("ETT_EDPMS_SHP Record Indicator---------Query------" + ship_pst.getQueryString());
                if (ship_rs.next())
                {
                  recInd = ship_rs.getString("RECIND");
                  shpno = ship_rs.getString("SHIPBILLNO");
                  migSrc = ship_rs.getString("MIG_SOURCE");
                }
                ship_rs.close();
                ship_pst.close();
                logger.info("shipbillno---------------------------" + shpno);
                logger.info("recInd----------------------------" + recInd);
                if (recInd != null)
                {
                  if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("1")))
                  {
                    logger.info("insert_ExistData_Error-------------------------");
                    shippingVO = insert_ExistData_Error(con, shippingVO, invoiceList);
                  }
                  else if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("2")))
                  {
                    logger.info("updateEDPMS_EDIData-------------------------");
                   
                    shippingVO = updateEDPMS_EDIData(con, shippingVO, invoiceList);
                   


                    inboundship++;
                   
                    xmlErrorList.addAll(shippingVO.getXmlErrorList());
                  }
                  if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("3"))) {
                    shippingVO = existData_Record_Indicator_3(con, shippingVO);
                  }
                }
                else if ((shpno != null) && (migSrc != null) && (migSrc.equalsIgnoreCase("FINACLE")))
                {
                  logger.info("Migrated data----------------");
                  shippingVO = insert_ExistData_Error_mig(con, shippingVO, invoiceList);
                }
                else if (recInd == null)
                {
                  logger.info("insertEDPMS_EDIData--------EDI-----------------");
                 
                  shippingVO = insertEDPMS_EDIData(con, shippingVO, invoiceList);
                  inboundship++;
                  ij++;
                  toatl_inv += this.invoicecount;
                 

                  xmlErrorList.addAll(shippingVO.getXmlErrorList());
                }
                String sShpCountQuery = "SELECT COUNT(DISTINCT(SHP.SHIPBILLNO)) SHPCOUNT, COUNT(INV.INVNO) INVCOUNT FROM ETT_EDPMS_SHP SHP, ETT_EDPMS_SHP_INV INV WHERE SHP.FILENO = ? AND SHP.FILENO = INV.FILENO AND SHP.SHIPBILLNO = INV.SHIPBILLNO";
               
                logger.info("----------------Counting Shipping Bills-------------------");
               
                LoggableStatement lst = new LoggableStatement(con, sShpCountQuery);
                lst.setInt(1, shippingVO.getFileNo());
                ResultSet rst1 = lst.executeQuery();
               
                logger.info("----------------Counting Shipping Bills------------Query-------" + lst.getQueryString());
                if (rst1.next())
                {
                  iShpCount = rst1.getInt(1);
                  iShpInvCount = rst1.getInt(2);
                }
                rst1.close();
                lst.close();
              }
              else if ((!shippingVO.getFormNo().equalsIgnoreCase("")) &&
                (shippingVO.getExportType().equalsIgnoreCase("2")) &&
                (shippingVO.getShippingBillNo().equalsIgnoreCase("")))
              {
                LoggableStatement softex_pst = new LoggableStatement(con,
                  "SELECT NVL(RECIND,'1') AS RECIND,formno,MIG_SOURCE FROM ETT_EDPMS_SHP_SOFTEX WHERE SHIPBILLDATE =? AND PORTCODE=? AND FORMNO=?");
                softex_pst.setString(1, shippingVO.getShippingBillDate());
                softex_pst.setString(2, shippingVO.getPortCode());
                softex_pst.setString(3, shippingVO.getFormNo());
                ResultSet softex_rs = softex_pst.executeQuery();
               
                logger.info("Softex Query Count Record Indicator---------Query" + softex_pst.getQueryString());
                String recInd = null;
                String frmno = null;
                String migSrc = null;
                if (softex_rs.next())
                {
                  recInd = softex_rs.getString("RECIND");
                  frmno = softex_rs.getString("formno");
                  migSrc = softex_rs.getString("MIG_SOURCE");
                }
                softex_rs.close();
                softex_pst.close();
                if (recInd != null)
                {
                  if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("1"))) {
                    shippingVO = insert_ExistData_Error(con, shippingVO, invoiceList);
                  } else if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("2"))) {
                    shippingVO = updateEDPMS_SoftexData(con, shippingVO, invoiceList);
                  } else if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("3"))) {
                    shippingVO = existData_Record_Indicator_3(con, shippingVO);
                  }
                }
                else if ((frmno != null) && (migSrc != null) && (migSrc.equalsIgnoreCase("FINACLE")))
                {
                  logger.info("Migrated data----------------");
                  shippingVO = insert_ExistData_Error_mig(con, shippingVO, invoiceList);
                }
                else if (recInd == null)
                {
                  shippingVO = insertEDPMS_SoftexData(con, shippingVO, invoiceList);
                  ij++;
                 
                  inboundship++;
                  toatl_inv += this.invoicecount;
                 
                  xmlErrorList.addAll(shippingVO.getXmlErrorList());
                }
                String sSoftCountQuery = "SELECT COUNT(DISTINCT(SOFT.FORMNO)) FORMNO, COUNT(INVNO) INVNO FROM ETT_EDPMS_SHP_SOFTEX SOFT, ETT_EDPMS_SHP_INV_SOFTEX INVSOFT WHERE SOFT.FILENO = ? AND SOFT.FILENO = INVSOFT.FILENO AND SOFT.FORMNO = INVSOFT.FORMNO";
                LoggableStatement lst = new LoggableStatement(con, sSoftCountQuery);
                lst.setInt(1, shippingVO.getFileNo());
                ResultSet rst1 = lst.executeQuery();
               
                logger.info("COUNT_SOFT_STATUS----------------------Query---" + lst.getQueryString());
                if (rst1.next())
                {
                  iSofCount = rst1.getInt(1);
                 
                  logger.info("iSofCount---------------" + iSofCount);
                  iSofInvCount = rst1.getInt(2);
                 
                  logger.info("iSofInvCount---------------" + iSofInvCount);
                }
                rst1.close();
                lst.close();
              }
              else if ((!shippingVO.getShippingBillNo().equalsIgnoreCase("")) && (shippingVO.getExportType().equalsIgnoreCase("1")) && (!shippingVO.getFormNo().equalsIgnoreCase("")))
              {
                logger.info("Inside export type 1----");
                LoggableStatement ship_pst = new LoggableStatement(con,
               
                  "SELECT RECIND  AS RECIND, MIG_SOURCE FROM ETT_EDPMS_SHP WHERE SHIPBILLNO=? AND SHIPBILLDATE =? AND PORTCODE=? AND FORMNO=?");
                ship_pst.setString(1, shippingVO.getShippingBillNo());
                ship_pst.setString(2, shippingVO.getShippingBillDate());
                ship_pst.setString(3, shippingVO.getPortCode());
                ship_pst.setString(4, shippingVO.getFormNo());
                logger.info("ship_pst query - " + ship_pst.getQueryString());
                ResultSet ship_rs = ship_pst.executeQuery();
                String recInd = null;
               
                logger.info("ETT_EDPMS_SHP Record Indicator---------Query------" + ship_pst.getQueryString());
                if (ship_rs.next()) {
                  recInd = ship_rs.getString("RECIND");
                }
                ship_rs.close();
                ship_pst.close();
               
                logger.info("recInd----------------------------" + recInd);
                if (recInd != null)
                {
                  if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("1")))
                  {
                    logger.info("insert_ExistData_Error-------------------------");
                    shippingVO = insert_ExistData_Error(con, shippingVO, invoiceList);
                  }
                  else if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("2")))
                  {
                    logger.info("updateEDPMS_EDIData-------------------------");
                   
                    shippingVO = updateEDPMS_EDIData_SEZ(con, shippingVO, invoiceList);
                   


                    inboundship++;
                   
                    xmlErrorList.addAll(shippingVO.getXmlErrorList());
                  }
                  if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("3"))) {
                    shippingVO = existData_Record_Indicator_3(con, shippingVO);
                  }
                }
                else if (recInd == null)
                {
                  LoggableStatement ship_pst1 = new LoggableStatement(con,
                 
                    "SELECT RECIND  AS RECIND FROM ETT_EDPMS_SHP WHERE SHIPBILLNO=? AND SHIPBILLDATE =? AND PORTCODE=?");
                  ship_pst1.setString(1, shippingVO.getShippingBillNo());
                  ship_pst1.setString(2, shippingVO.getShippingBillDate());
                  ship_pst1.setString(3, shippingVO.getPortCode());
                 
                  logger.info("ship_pst query - " + ship_pst1.getQueryString());
                  ResultSet ship_rs1 = ship_pst1.executeQuery();
                  String recInd1 = null;
                 
                  logger.info("ETT_EDPMS_SHP Record Indicator---------Query------" + ship_pst1.getQueryString());
                  if (ship_rs1.next()) {
                    recInd1 = ship_rs1.getString("RECIND");
                  }
                  ship_rs1.close();
                  ship_pst1.close();
                  logger.info("insertEDPMS_EDIData--------EDI-----------------");
                  if (recInd1 == null)
                  {
                    shippingVO = insertEDPMS_EDIData(con, shippingVO, invoiceList);
                    inboundship++;
                    ij++;
                    toatl_inv += this.invoicecount;
                   

                    xmlErrorList.addAll(shippingVO.getXmlErrorList());
                  }
                }
                String sShpCountQuery = "SELECT COUNT(DISTINCT(SHP.SHIPBILLNO)) SHPCOUNT, COUNT(INV.INVNO) INVCOUNT FROM ETT_EDPMS_SHP SHP, ETT_EDPMS_SHP_INV INV WHERE SHP.FILENO = ? AND SHP.FILENO = INV.FILENO AND SHP.SHIPBILLNO = INV.SHIPBILLNO";
               
                logger.info("----------------Counting Shipping Bills-------------------");
               
                LoggableStatement lst = new LoggableStatement(con, sShpCountQuery);
                lst.setInt(1, shippingVO.getFileNo());
                ResultSet rst1 = lst.executeQuery();
               
                logger.info("----------------Counting Shipping Bills------------Query-------" + lst.getQueryString());
                if (rst1.next())
                {
                  iShpCount = rst1.getInt(1);
                  iShpInvCount = rst1.getInt(2);
                }
                rst1.close();
                lst.close();
              }
              else if ((!shippingVO.getFormNo().equalsIgnoreCase("")) &&
                (shippingVO.getExportType().equalsIgnoreCase("2")) &&
                (!shippingVO.getShippingBillNo().equalsIgnoreCase("")))
              {
                logger.info("Inside export type 2----");
                LoggableStatement softex_pst = new LoggableStatement(con,
                  "SELECT NVL(RECIND,'1') AS RECIND FROM ETT_EDPMS_SHP_SOFTEX WHERE SHIPBILLDATE =? AND PORTCODE=? AND FORMNO=? AND SHIPBILLNO=?");
                softex_pst.setString(1, shippingVO.getShippingBillDate());
                softex_pst.setString(2, shippingVO.getPortCode());
                softex_pst.setString(3, shippingVO.getFormNo());
                softex_pst.setString(4, shippingVO.getShippingBillNo());
                ResultSet softex_rs = softex_pst.executeQuery();
               
                logger.info("Softex Query Count Record Indicator---------Query" + softex_pst.getQueryString());
                String recInd = null;
                if (softex_rs.next()) {
                  recInd = softex_rs.getString("RECIND");
                }
                softex_rs.close();
                softex_pst.close();
                if (recInd != null)
                {
                  if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("1"))) {
                    shippingVO = insert_ExistData_Error(con, shippingVO, invoiceList);
                  } else if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("2"))) {
                    shippingVO = updateEDPMS_SoftexData_SEZ(con, shippingVO, invoiceList);
                  } else if ((shippingVO.getRecInd() != null) && (shippingVO.getRecInd().equalsIgnoreCase("3"))) {
                    shippingVO = existData_Record_Indicator_3(con, shippingVO);
                  }
                }
                else if (recInd == null)
                {
                  LoggableStatement softex_pst1 = new LoggableStatement(con,
                    "SELECT NVL(RECIND,'1') AS RECIND FROM ETT_EDPMS_SHP_SOFTEX WHERE SHIPBILLDATE =? AND PORTCODE=? AND FORMNO=? ");
                  softex_pst1.setString(1, shippingVO.getShippingBillDate());
                  softex_pst1.setString(2, shippingVO.getPortCode());
                  softex_pst1.setString(3, shippingVO.getFormNo());
                 
                  ResultSet softex_rs1 = softex_pst1.executeQuery();
                 
                  logger.info("Softex Query Count Record Indicator---------Query" + softex_pst1.getQueryString());
                  String recInd1 = null;
                  if (softex_rs1.next()) {
                    recInd1 = softex_rs1.getString("RECIND");
                  }
                  softex_rs1.close();
                  softex_pst1.close();
                  if (recInd1 == null)
                  {
                    shippingVO = insertEDPMS_SoftexData(con, shippingVO, invoiceList);
                    ij++;
                   
                    inboundship++;
                    toatl_inv += this.invoicecount;
                   
                    xmlErrorList.addAll(shippingVO.getXmlErrorList());
                  }
                }
                String sSoftCountQuery = "SELECT COUNT(DISTINCT(SOFT.FORMNO)) FORMNO, COUNT(INVNO) INVNO FROM ETT_EDPMS_SHP_SOFTEX SOFT, ETT_EDPMS_SHP_INV_SOFTEX INVSOFT WHERE SOFT.FILENO = ? AND SOFT.FILENO = INVSOFT.FILENO AND SOFT.FORMNO = INVSOFT.FORMNO";
                LoggableStatement lst = new LoggableStatement(con, sSoftCountQuery);
                lst.setInt(1, shippingVO.getFileNo());
                ResultSet rst1 = lst.executeQuery();
               
                logger.info("COUNT_SOFT_STATUS----------------------Query---" + lst.getQueryString());
                if (rst1.next())
                {
                  iSofCount = rst1.getInt(1);
                 
                  logger.info("iSofCount---------------" + iSofCount);
                  iSofInvCount = rst1.getInt(2);
                 
                  logger.info("iSofInvCount---------------" + iSofInvCount);
                }
                rst1.close();
                lst.close();
              }
            }
            else
            {
              logger.info("Errors in MDF File ");
             
              error_status = true;
             
              String err_msg = shippingVO.getErrorString();
              if ((err_msg == null) || (err_msg.equalsIgnoreCase(""))) {
                err_msg = "Error in MDF Data";
              }
              excelDataVO = new ExcelDataVO();
              excelDataVO.setShipBillNo(shippingVO.getShippingBillNo());
              excelDataVO.setShipBillDate(shippingVO.getShippingBillDate());
              excelDataVO.setPortCode(shippingVO.getPortCode());
              excelDataVO.setFormNo(shippingVO.getFormNo());
              excelDataVO.setShpErrorString(shippingVO.getErrorString());
              excelDataVO.setFileNo(shippingVO.getFileNo());
              xmlErrorList.add(excelDataVO);
            }
          }
          if (xmlErrorList.size() > 0)
          {
            logger.info("----------------inserting the Error Information--------------");
           

            iErrCount = insert_EDPMS_TMP_Table(con, xmlErrorList);
           
            logger.info("EDI---iErrCount----------------------" + iErrCount);
          }
          logger.info("The Number is : totalshbill : " + totalshbill);
          logger.info("The Number is : inboundship : " + inboundship);
         
          logger.info("The Update Inbound Shipping BIlls : " + inbound_upd);
         
          logger.info("INsetion Count-----------------------------------" + ij);
         





          String sFileType = "MDF";
         
          iShpCount += iSofCount;
          xmlFileVO.setShpCount(inboundship);
         


          iShpInvCount += iSofInvCount;
          xmlFileVO.setInvCount(toatl_inv);
          if (iErrCount != 0) {
            xmlFileVO.setErrCount(iErrCount);
          }
          shippingVO.setHeaderShpCount(Integer.parseInt(noofshipbills));
          shippingVO.setHeaderInvCount(Integer.parseInt(noofinvoices));
          shippingVO.setContentShpCount(totalshbill);
          shippingVO.setContentInvCount(invLength);
          shippingVO = compareBOECount(shippingVO);
          String sRetValue = shippingVO.getCountResult();
          if (!"S".equalsIgnoreCase(sRetValue))
          {
            rollbackMDFFileDetails(con, shippingVO, sFileType);
            xmlFileVO.setResult(shippingVO.getCountResult());
            localXMLFileVO1 = xmlFileVO;return localXMLFileVO1;
          }
          logger.info("totalshbill--------------------" + totalshbill);
         
          logger.info("inboundship--------------------" + inboundship);
          if ((commonMethods.isNull(shippingVO.getErrorString())) &&
            (toatl_inv != Integer.valueOf(noofinvoices).intValue()))
          {
            logger.info(
              "-------------Partial File Uploaded--------------");
           



            xmlFileVO.setResult("PL");
            xmlFileVO.setInsertCount(inboundship);
            xmlFileVO.setXmlErrorList(xmlErrorList);
          }
          if ((commonMethods.isNull(shippingVO.getErrorString())) && (totalshbill == inboundship) &&
            (toatl_inv == Integer.valueOf(noofinvoices).intValue()))
          {
            xmlFileVO.setResult("Y");
          }
          else if (totalshbill != inboundship)
          {
            logger.info("No Invoices from XML -----------" + noofinvoices);
            logger.info("No Invoices are inserted in tables -----------" + toatl_inv);
            logger.info("error_status-------------" + error_status);
            if ((commonMethods.isNull(shippingVO.getErrorString())) &&
              (totalshbill != inboundship))
            {
              xmlFileVO.setResult("F");
             
              xmlFileVO.setXmlErrorList(xmlErrorList);
            }
            else if (!commonMethods.isNull(shippingVO.getErrorString()))
            {
              logger.info("----Error in XML---");
             
              xmlFileVO.setResult("ERR");
            }
          }
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception------------XMLFILE DAO--------------------" + exception);
     
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("mdf xmlfilevo" + xmlFileVO);
    return xmlFileVO;
  }
 
  public ShippingDetailsVO insertEDPMS_EDIData(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("insertEDPMS_EDIData");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    int staging = 0;
    int icount = 0;
    try
    {
      commonMethods = new CommonMethods();
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      pst = new LoggableStatement(con,
        "INSERT INTO ETT_EDPMS_SHP(SHIPBILLNO,SHIPBILLDATE,PORTCODE,LEODATE,CUSTNO,FORMNO,IECODE,ADCODE,EXPORTAGENCY,EXPORTTYPE,RECIND,COUNTRYDEST,FILENO,CREATEDON_DATE)values(?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?, SYSTIMESTAMP)");
     
      pst.setString(1, shipingVO.getShippingBillNo());
      pst.setString(2, shipingVO.getShippingBillDate());
      pst.setString(3, shipingVO.getPortCode());
      pst.setString(4, shipingVO.getLeoDate());
      pst.setString(5, shipingVO.getCustSerialNo());
      pst.setString(6, shipingVO.getFormNo());
      pst.setString(7, shipingVO.getIeCode());
      pst.setString(8, shipingVO.getAdCode());
      pst.setString(9, shipingVO.getExportAgency());
      pst.setString(10, shipingVO.getExportType());
      pst.setString(11, shipingVO.getRecInd());
      pst.setString(12, shipingVO.getCountryOfDest());
      pst.setInt(13, shipingVO.getFileNo());
     
      logger.info("file no" + shipingVO.getFileNo());
      noShipBill = pst.executeUpdate();
      logger.info("ETT_EDPMS_SHP-------------Query---------------" + pst.getQueryString());
     
      logger.info("NoShipBill - " + noShipBill);
     
      pst.close();
      icount++;
      logger.info("icount=>" + icount);
      if ((noShipBill > 0) && (invoiceList != null)) {
        for (int i = 0; i < invoiceList.size(); i++)
        {
          InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
         
          String invErrorString = "";
         
          LoggableStatement pst2 = new LoggableStatement(con,
            "SELECT COUNT(*) AS COUNT FROM ETT_EDPMS_SHP_INV WHERE  SHIPBILLNO=? AND SHIPBILLDATE =? AND PORTCODE=? AND INVSERIALNO=? AND INVNO=? ");
          pst2.setString(1, shipingVO.getShippingBillNo());
          pst2.setString(2, shipingVO.getShippingBillDate());
          pst2.setString(3, shipingVO.getPortCode());
          pst2.setString(4, invoiceVO.getInvoiceSerialNo());
          pst2.setString(5, invoiceVO.getInvoiceNo());
          ResultSet edi_rs = pst2.executeQuery();
         
          logger.info(
            "Checking Existing Invoice Information------------Query---------" + pst2.getQueryString());
          int k = 0;
          if (edi_rs.next()) {
            k = edi_rs.getInt("COUNT");
          }
          logger.info("Checking Existing Invoice Information--------K----Query-------count--" + k);
          edi_rs.close();
          pst2.close();
          if (k == 0)
          {
            if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice Serial No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobCurr())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Currency is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobAmt())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Amount is Empty");
            }
            logger.info("invErrorString--------Status----------" + invErrorString);
            if (commonMethods.isNull(invErrorString))
            {
              LoggableStatement pst1 = new LoggableStatement(con,
                "INSERT INTO ETT_EDPMS_SHP_INV(SHIPBILLNO,SHIPBILLDATE,PORTCODE,FILENO,INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE,INSAMT,COMMCURRCODE,COMMAMT,DISCURRCODE,DISAMT,DEDCURRCODE,DEDAMT,PKGCURRCODE,PKGAMT,FORMNO,CREATEDON_DATE) values(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP )");
             
              pst1.setString(1, shipingVO.getShippingBillNo());
              pst1.setString(2, shipingVO.getShippingBillDate());
              pst1.setString(3, shipingVO.getPortCode());
              pst1.setInt(4, shipingVO.getFileNo());
              pst1.setString(5, invoiceVO.getInvoiceSerialNo());
              pst1.setString(6, invoiceVO.getInvoiceNo());
              pst1.setString(7, invoiceVO.getInvoiceDate());
              pst1.setString(8, invoiceVO.getFobCurr());
              pst1.setString(9, invoiceVO.getFobAmt());
              pst1.setString(10, invoiceVO.getFreightCurr());
              pst1.setString(11, invoiceVO.getFreightAmt());
              pst1.setString(12, invoiceVO.getInsuranceCurr());
              pst1.setString(13, invoiceVO.getInsuranceAmt());
              pst1.setString(14, invoiceVO.getCommissionCurr());
              pst1.setString(15, invoiceVO.getCommissionAmt());
              pst1.setString(16, invoiceVO.getDiscountCurr());
              pst1.setString(17, invoiceVO.getDiscountAmt());
              pst1.setString(18, invoiceVO.getDeductionCurr());
              pst1.setString(19, invoiceVO.getDeductionAmt());
              pst1.setString(20, invoiceVO.getPackagingCurr());
              pst1.setString(21, invoiceVO.getPackagingAmt());
             
              logger.info("FORMNO=============>" + shipingVO.getFormNo());
              pst1.setString(22, shipingVO.getFormNo());
             


              int iCount = 0;
              iCount = pst1.executeUpdate();
              if (iCount == 0) {
                logger.info("Duplicate data found" + invoiceVO.getInvoiceNo());
              }
              logger.info("INSERT  ETT_EDPMS_SHP_INV-------Query-------------" + pst1.getQueryString());
              invCount += iCount;
             
              this.invoicecount = invCount;
              logger.info("insert invoice count" + this.invoicecount);
              logger.info("INSERT  Count in ETT_EDPMS_SHP_INV-->" + invCount);
             
              pst1.close();
            }
          }
          else
          {
            logger.info("--Duplicate Data Executed--");
           
            excelDataVO = new ExcelDataVO();
           
            invErrorString = commonMethods.setErrorString(invErrorString, "Duplicate Data");
           
            excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
            excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
            excelDataVO.setPortCode(shipingVO.getPortCode());
            excelDataVO.setFormNo(shipingVO.getFormNo());
            excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
            excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
            excelDataVO.setShpErrorString(invErrorString);
            excelDataVO.setFileNo(shipingVO.getFileNo());
           
            xmlErrorList.add(excelDataVO);
          }
        }
      }
      logger.info("--------" + shipingVO.getXmlErrorList());
      if (xmlErrorList.size() > 0)
      {
        deleteMDFErrorRecords(con, xmlErrorList);
        noShipBill = 0;
      }
      if ((noShipBill > 0) && (invCount > 0)) {
        shipingVO.setNoShipBillNo(noShipBill);
      }
      shipingVO.setXmlErrorList(xmlErrorList);
    }
    catch (Exception exception)
    {
      logger.info("insertEDPMS_EDIData-----Exception----------" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  private int insertStaging(Connection con, ShippingDetailsVO shipingVO)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int staging = 0;
    try
    {
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      logger.info("--Staging---");
      pst = new LoggableStatement(con,
        "INSERT INTO ETT_EDPMS_STAGING(SHIPBILLNO,SHIPBILLDATE,PORTCODE,FORMNO,FILENO,uploadtime)values(?,?,?,?,?,SYSTIMESTAMP)");
      pst.setString(1, shipingVO.getShippingBillNo());
      pst.setString(2, shipingVO.getShippingBillDate());
      pst.setString(3, shipingVO.getPortCode());
      pst.setString(4, shipingVO.getFormNo());
      pst.setInt(5, shipingVO.getFileNo());
      staging = pst.executeUpdate();
      pst.close();
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return staging;
  }
 
  public ShippingDetailsVO insert_ExistData_Error(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    int staging = 0;
    int icount = 0;
    try
    {
      logger.info("insert_ExistData_Record_Indicator_O");
      String sQuery = null;
      LoggableStatement ps = null;
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      con = DBConnectionUtility.getConnection();
      sQuery = "INSERT INTO ETT_EDPMS_ERR_TMP_TBL(SHIPBILLNO, SHIPBILLDATE, PORTCODE, FORMNO, SHP_ERROR_MSG,  FILENO) VALUES(?, ?, ?, ?, ?, ?)";
     



      logger.info("shipingVO.getShippingBillNo()---------" + shipingVO.getShippingBillNo());
      logger.info("shipingVO.getShippingBillDate()---------" + shipingVO.getShippingBillDate());
      logger.info("shipingVO.getPortCode()---------" + shipingVO.getPortCode());
      logger.info("shipingVO.getFormNo()---------" + shipingVO.getFormNo());
      logger.info("shipingVO.getFileNo()---------" + shipingVO.getFileNo());
     

      ps = new LoggableStatement(con, sQuery);
     
      logger.info("----------------Inside Method-----------");
      ps.setString(1, shipingVO.getShippingBillNo());
      ps.setString(2, shipingVO.getShippingBillDate());
      ps.setString(3, shipingVO.getPortCode());
      ps.setString(4, shipingVO.getFormNo());
      ps.setString(5, " Data Already Exist in TI+");
      ps.setInt(6, shipingVO.getFileNo());
     
      logger.info(" insertRet Record 3 Query-----------------" + ps.getQueryString());
     
      int insertRet = ps.executeUpdate();
     
      logger.info(" insertRet Record Count shipping bill no" + insertRet);
     


      DBConnectionUtility.surrenderDB(con, ps, null);
    }
    catch (Exception exception)
    {
      logger.info("------------------insert_ExistData_Record_Indicator_O---" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public ShippingDetailsVO insert_ExistData_Error_mig(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    int staging = 0;
    int icount = 0;
    try
    {
      logger.info("insert_ExistData_Record_Indicator_O");
      String sQuery = null;
      LoggableStatement ps = null;
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      con = DBConnectionUtility.getConnection();
      sQuery = "INSERT INTO ETT_EDPMS_ERR_TMP_TBL(SHIPBILLNO, SHIPBILLDATE, PORTCODE, FORMNO, SHP_ERROR_MSG,  FILENO) VALUES(?, ?, ?, ?, ?, ?)";
     



      logger.info("shipingVO.getShippingBillNo()---------" + shipingVO.getShippingBillNo());
      logger.info("shipingVO.getShippingBillDate()---------" + shipingVO.getShippingBillDate());
      logger.info("shipingVO.getPortCode()---------" + shipingVO.getPortCode());
      logger.info("shipingVO.getFormNo()---------" + shipingVO.getFormNo());
      logger.info("shipingVO.getFileNo()---------" + shipingVO.getFileNo());
     

      ps = new LoggableStatement(con, sQuery);
     
      logger.info("----------------Inside Method-----------");
      ps.setString(1, shipingVO.getShippingBillNo());
      ps.setString(2, shipingVO.getShippingBillDate());
      ps.setString(3, shipingVO.getPortCode());
      ps.setString(4, shipingVO.getFormNo());
      ps.setString(5, " Migrated Data Already Exist in TI+");
      ps.setInt(6, shipingVO.getFileNo());
     
      logger.info(" insertRet Record 3 Query-----------------" + ps.getQueryString());
     
      int insertRet = ps.executeUpdate();
     
      logger.info(" insertRet Record Count shipping bill no" + insertRet);
     


      DBConnectionUtility.surrenderDB(con, ps, null);
    }
    catch (Exception exception)
    {
      logger.info("------------------insert_ExistData_Record_Indicator_O---" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public ShippingDetailsVO insert_ExistData_Record_Indicator_O(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    int staging = 0;
    int icount = 0;
    try
    {
      logger.info("insert_ExistData_Record_Indicator_O");
      String sQuery = null;
      LoggableStatement ps = null;
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      con = DBConnectionUtility.getConnection();
      sQuery = "INSERT INTO ETT_EDPMS_ERR_TMP_TBL(SHIPBILLNO, SHIPBILLDATE, PORTCODE, FORMNO, SHP_ERROR_MSG,  FILENO) VALUES(?, ?, ?, ?, ?, ?)";
     



      logger.info("shipingVO.getShippingBillNo()---------" + shipingVO.getShippingBillNo());
      logger.info("shipingVO.getShippingBillDate()---------" + shipingVO.getShippingBillDate());
      logger.info("shipingVO.getPortCode()---------" + shipingVO.getPortCode());
      logger.info("shipingVO.getFormNo()---------" + shipingVO.getFormNo());
      logger.info("shipingVO.getFileNo()---------" + shipingVO.getFileNo());
     

      ps = new LoggableStatement(con, sQuery);
     
      logger.info("----------------Inside Method-----------");
      ps.setString(1, shipingVO.getShippingBillNo());
      ps.setString(2, shipingVO.getShippingBillDate());
      ps.setString(3, shipingVO.getPortCode());
      ps.setString(4, shipingVO.getFormNo());
      ps.setString(5, " Record Indicator 3 Not Allowed to upload in TI+");
      ps.setInt(6, shipingVO.getFileNo());
     
      logger.info(" insertRet Record 3 Query-----------------" + ps.getQueryString());
     
      int insertRet = ps.executeUpdate();
     
      logger.info(" insertRet Record Count shipping bill no" + insertRet);
      ps.close();
     
      DBConnectionUtility.surrenderDB(con, ps, null);
    }
    catch (Exception exception)
    {
      logger.info("------------------insert_ExistData_Record_Indicator_O---" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public ShippingDetailsVO already_utilized_bills(Connection con, ShippingDetailsVO shipingVO)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    int staging = 0;
    int icount = 0;
    try
    {
      logger.info("insert_ExistData_Record_Indicator_O");
      String sQuery = null;
      LoggableStatement ps = null;
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      con = DBConnectionUtility.getConnection();
      sQuery = "INSERT INTO ETT_EDPMS_ERR_TMP_TBL(SHIPBILLNO, SHIPBILLDATE, PORTCODE, FORMNO, SHP_ERROR_MSG,  FILENO) VALUES(?, ?, ?, ?, ?, ?)";
     
      logger.info("shipingVO.getShippingBillNo()---------" + shipingVO.getShippingBillNo());
      logger.info("shipingVO.getShippingBillDate()---------" + shipingVO.getShippingBillDate());
      logger.info("shipingVO.getPortCode()---------" + shipingVO.getPortCode());
      logger.info("shipingVO.getFormNo()---------" + shipingVO.getFormNo());
      logger.info("shipingVO.getFileNo()---------" + shipingVO.getFileNo());
     

      ps = new LoggableStatement(con, sQuery);
     
      logger.info("----------------Inside Method-----------");
      ps.setString(1, shipingVO.getShippingBillNo());
      ps.setString(2, shipingVO.getShippingBillDate());
      ps.setString(3, shipingVO.getPortCode());
      ps.setString(4, shipingVO.getFormNo());
      ps.setString(5, " SB Already utilized");
      ps.setInt(6, shipingVO.getFileNo());
     
      logger.info(" insertRet Record 3 Query-----------------" + ps.getQueryString());
     
      int insertRet = ps.executeUpdate();
     
      logger.info(" insertRet Record Count shipping bill no" + insertRet);
      ps.close();
     
      DBConnectionUtility.surrenderDB(con, ps, null);
    }
    catch (Exception exception)
    {
      logger.info("------------------insert_ExistData_Record_Indicator_O---" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  private ShippingDetailsVO existData_Record_Indicator_3(Connection con, ShippingDetailsVO shipingVO)
    throws DAOException
  {
    logger.info("Entering Method");
    CommonMethods commonMethods = null;
    LoggableStatement ps = null;
    ResultSet rs = null;
    LoggableStatement ps1 = null;
    ResultSet rs1 = null;
    LoggableStatement ps2 = null;
    ResultSet rs2 = null;
    LoggableStatement ps3 = null;
    ResultSet rs3 = null;
    int count = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("ddmmyyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-mm-yy");
    String sbdate = null;
    try
    {
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      logger.info("existData_Record_Indicator_3");
      String sQuery = null;
      String sQuery1 = null;
      String sQuery2 = null;
      String sQuery3 = null;
      commonMethods = new CommonMethods();
      if (!commonMethods.isNull(shipingVO.getShippingBillDate())) {
        try
        {
          sbdate = sdf1.format(sdf.parse(shipingVO.getShippingBillDate()));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      logger.info(sbdate);
      if ((commonMethods.isNull(shipingVO.getShippingBillNo())) || (shipingVO.getShippingBillNo().trim().equals("")) ||
        (commonMethods.isNull(shipingVO.getFormNo())) || (shipingVO.getFormNo().trim().equals("")))
      {
        logger.info("EDI/EDF/SOFTEX");
       

        sQuery = "SELECT COUNT(1) FROM exteventspd spd,extevent ext,baseevent bev,master mas WHERE mas.key97 =BEV.MASTER_KEY AND bev.key97=ext.event and SPD.FK_EVENT=EXT.KEY29 and BEV.REFNO_PFIX IN ('CRE') and trim(spd.sdbillno)=? and trim(spd.sdbildat)=? and trim(spd.sdprtcde)=?";
       



        ps = new LoggableStatement(con, sQuery);
        ps.setString(1, shipingVO.getShippingBillNo());
        ps.setString(2, sbdate);
        ps.setString(3, shipingVO.getPortCode());
        logger.info(ps.getQueryString());
        rs = ps.executeQuery();
        while (rs.next()) {
          count += rs.getInt(1);
        }
        sQuery1 = "SELECT COUNT(1) FROM exteventslc spd,extevent ext,baseevent bev,master mas WHERE mas.key97 =BEV.MASTER_KEY AND bev.key97=ext.event and SPD.FK_EVENT=EXT.KEY29 and BEV.REFNO_PFIX IN ('DPR') and trim(spd.lcbillno)=? and trim(spd.lcbildat)=? and trim(spd.lcprtcde)=?";
       



        ps1 = new LoggableStatement(con, sQuery1);
        ps1.setString(1, shipingVO.getShippingBillNo());
        ps1.setString(2, sbdate);
        ps1.setString(3, shipingVO.getPortCode());
        logger.info(ps1.getQueryString());
        rs1 = ps1.executeQuery();
        while (rs1.next()) {
          count += rs1.getInt(1);
        }
        sQuery2 = "SELECT COUNT(1) FROM exteventspd spd,extevent ext,baseevent bev,master mas WHERE mas.key97 =BEV.MASTER_KEY AND bev.key97=ext.event and SPD.FK_EVENT=EXT.KEY29 and BEV.REFNO_PFIX IN ('CRE') and trim(spd.sdformno)=? and trim(spd.sdbildat)=? and trim(spd.sdprtcde)=?";
       



        ps2 = new LoggableStatement(con, sQuery2);
        ps2.setString(1, shipingVO.getFormNo());
        ps2.setString(2, sbdate);
        ps2.setString(3, shipingVO.getPortCode());
        logger.info(ps2.getQueryString());
        rs2 = ps2.executeQuery();
        while (rs2.next()) {
          count += rs2.getInt(1);
        }
        sQuery3 = "SELECT COUNT(1) FROM exteventslc spd,extevent ext,baseevent bev,master mas WHERE mas.key97 =BEV.MASTER_KEY AND bev.key97=ext.event and SPD.FK_EVENT=EXT.KEY29 and BEV.REFNO_PFIX IN ('DPR') and trim(spd.lcformno)=? and trim(spd.lcbildat)=? and trim(spd.lcprtcde)=?";
       



        ps3 = new LoggableStatement(con, sQuery3);
        ps3.setString(1, shipingVO.getFormNo());
        ps3.setString(2, sbdate);
        ps3.setString(3, shipingVO.getPortCode());
        logger.info(ps3.getQueryString());
        rs3 = ps3.executeQuery();
        while (rs3.next()) {
          count += rs3.getInt(1);
        }
        logger.info("count - " + count);
       
        DBConnectionUtility.surrenderDB(null, ps, rs);
        DBConnectionUtility.surrenderDB(null, ps1, rs1);
        DBConnectionUtility.surrenderDB(null, ps2, rs2);
        DBConnectionUtility.surrenderDB(null, ps3, rs3);
      }
      else if ((!commonMethods.isNull(shipingVO.getShippingBillNo())) && (!shipingVO.getShippingBillNo().trim().equals("")) &&
        (!commonMethods.isNull(shipingVO.getFormNo())) && (!shipingVO.getFormNo().trim().equals("")))
      {
        logger.info("SEZ");
       

        sQuery = "SELECT COUNT(1) FROM exteventspd spd,extevent ext,baseevent bev,master mas WHERE mas.key97 =BEV.MASTER_KEY AND bev.key97=ext.event and SPD.FK_EVENT=EXT.KEY29 and BEV.REFNO_PFIX IN ('CRE') and trim(spd.sdbillno)=? and trim(spd.sdbildat)=? and trim(spd.sdprtcde)=? and trim(spd.sdformno)=?";
       



        ps = new LoggableStatement(con, sQuery);
        ps.setString(1, shipingVO.getShippingBillNo());
        ps.setString(2, sbdate);
        ps.setString(3, shipingVO.getPortCode());
        ps.setString(4, shipingVO.getFormNo());
        logger.info(ps.getQueryString());
        rs = ps.executeQuery();
        while (rs.next()) {
          count += rs.getInt(1);
        }
        sQuery1 = "SELECT COUNT(1) FROM exteventslc spd,extevent ext,baseevent bev,master mas WHERE mas.key97 =BEV.MASTER_KEY AND bev.key97=ext.event and SPD.FK_EVENT=EXT.KEY29 and BEV.REFNO_PFIX IN ('DPR') and trim(spd.lcbillno)=? and trim(spd.lcbildat)=? and trim(spd.lcprtcde)=? and trim(spd.lcformno)=?";
       



        ps1 = new LoggableStatement(con, sQuery1);
        ps1.setString(1, shipingVO.getShippingBillNo());
        ps1.setString(2, sbdate);
        ps1.setString(3, shipingVO.getPortCode());
        ps1.setString(4, shipingVO.getFormNo());
        logger.info(ps1.getQueryString());
        rs1 = ps1.executeQuery();
        while (rs1.next()) {
          count += rs1.getInt(1);
        }
        logger.info("count - " + count);
       
        DBConnectionUtility.surrenderDB(null, ps, rs);
        DBConnectionUtility.surrenderDB(null, ps1, rs1);
        DBConnectionUtility.surrenderDB(null, ps2, rs2);
      }
      logger.info("SB Utilized count - " + count);
      if (count > 0)
      {
        logger.info("SB Already Utilized error");
       
        shipingVO = already_utilized_bills(con, shipingVO);
      }
      else if (count == 0)
      {
        logger.info("SB not utilized and deleted");
       
        sQuery = "DELETE FROM ETT_EDPMS_SHP WHERE TRIM(SHIPBILLNO)=? AND TRIM(SHIPBILLDATE)=? AND TRIM(PORTCODE)=?";
       
        LoggableStatement ps4 = new LoggableStatement(con, sQuery);
        ps4.setString(1, shipingVO.getShippingBillNo());
        ps4.setString(2, shipingVO.getShippingBillDate());
        ps4.setString(3, shipingVO.getPortCode());
        ps4.executeUpdate();
        ps4.close();
       
        sQuery2 = "DELETE FROM ETT_EDPMS_SHP_SOFTEX WHERE TRIM(FORMNO)=? AND TRIM(SHIPBILLDATE)=? AND TRIM(PORTCODE)=?";
       
        LoggableStatement ps6 = new LoggableStatement(con, sQuery2);
        ps6.setString(1, shipingVO.getFormNo());
        ps6.setString(2, shipingVO.getShippingBillDate());
        ps6.setString(3, shipingVO.getPortCode());
        ps6.executeUpdate();
        ps6.close();
       
        sQuery1 = "DELETE FROM ETT_EDPMS_SHP_INV WHERE TRIM(SHIPBILLNO)=? AND TRIM(SHIPBILLDATE)=? AND TRIM(PORTCODE)=?";
       
        LoggableStatement ps5 = new LoggableStatement(con, sQuery1);
        ps5.setString(1, shipingVO.getShippingBillNo());
        ps5.setString(2, shipingVO.getShippingBillDate());
        ps5.setString(3, shipingVO.getPortCode());
        ps5.executeUpdate();
        ps5.close();
       
        sQuery3 = "DELETE FROM ETT_EDPMS_SHP_INV_SOFTEX WHERE TRIM(FORMNO)=? AND TRIM(SHIPBILLDATE)=? AND TRIM(PORTCODE)=?";
       
        LoggableStatement ps7 = new LoggableStatement(con, sQuery3);
        ps7.setString(1, shipingVO.getFormNo());
        ps7.setString(2, shipingVO.getShippingBillDate());
        ps7.setString(3, shipingVO.getPortCode());
        ps7.executeUpdate();
        ps7.close();
      }
    }
    catch (Exception exception)
    {
      logger.info("------------------insert_ExistData_Record_Indicator_O---" + exception);
      exception.printStackTrace();
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, ps, rs);
      DBConnectionUtility.surrenderDB(null, ps1, rs1);
      DBConnectionUtility.surrenderDB(null, ps2, rs2);
      DBConnectionUtility.surrenderDB(null, ps3, rs3);
    }
    return shipingVO;
  }
 
  public int createTempEDPMS_EDIData(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int insertCount = 0;
    try
    {
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      pst = new LoggableStatement(con,
        "INSERT INTO ETT_TEMP_EDPMS_SHP(SHIPBILLNO,SHIPBILLDATE,LEODATE,CUSTNO,FORMNO,IECODE,ADCODE,PORTCODE,EXPORTAGENCY,EXPORTTYPE,RECIND,COUNTRYDEST,FILENO)SELECT SHIPBILLNO,SHIPBILLDATE,LEODATE,CUSTNO,FORMNO,IECODE,ADCODE,PORTCODE,EXPORTAGENCY,EXPORTTYPE,RECIND,COUNTRYDEST,FILENO FROM ETT_EDPMS_SHP WHERE  SHIPBILLNO=? AND SHIPBILLDATE=? AND PORTCODE=?");
      pst.setString(1, shipingVO.getShippingBillNo());
      pst.setString(2, shipingVO.getShippingBillDate());
      pst.setString(3, shipingVO.getPortCode());
      logger.info("TEMP_SHP_QUERY-->" + pst.getQueryString());
      insertCount = pst.executeUpdate();
      pst.close();
      if ((insertCount > 0) && (invoiceList != null)) {
        for (int i = 0; i < invoiceList.size(); i++)
        {
          InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
         
          LoggableStatement pst1 = new LoggableStatement(con,
            "INSERT INTO ETT_TEMP_EDPMS_SHP_INV(SHIPBILLNO,SHIPBILLDATE,PORTCODE,FILENO, INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE, INSAMT,COMMCURRCODE,COMMAMT,DISCURRCODE,DISAMT,DEDCURRCODE,DEDAMT,PKGCURRCODE,PKGAMT)  SELECT SHIPBILLNO,SHIPBILLDATE,PORTCODE,FILENO, INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE, INSAMT,COMMCURRCODE,COMMAMT,DISCURRCODE,DISAMT,DEDCURRCODE,DEDAMT,PKGCURRCODE,PKGAMT FROM ETT_EDPMS_SHP_INV WHERE  SHIPBILLNO=? AND SHIPBILLDATE=? AND PORTCODE=? AND INVSERIALNO=? AND INVNO=? ");
          pst1.setString(1, shipingVO.getShippingBillNo());
          pst1.setString(2, shipingVO.getShippingBillDate());
          pst1.setString(3, shipingVO.getPortCode());
          pst1.setString(4, invoiceVO.getInvoiceSerialNo());
          pst1.setString(5, invoiceVO.getInvoiceNo());
          logger.info("TEMP_INV_QUERY-->" + pst1.getQueryString());
          int insShpCount = pst1.executeUpdate();
          logger.info(Integer.valueOf(insShpCount));
          pst1.close();
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return insertCount;
  }
 
  public ShippingDetailsVO updateEDPMS_EDIData(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    try
    {
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      logger.info("shipn umber----------------" + shipingVO.getShippingBillNo());
      logger.info("shipn umber----------------" + shipingVO.getShippingBillDate());
      logger.info("shipn umber----------------" + shipingVO.getPortCode());
      if ((shipingVO != null) && (shipingVO.getRecInd().equalsIgnoreCase("3")))
      {
        logger.info("Record Indiactor is 3 So Updating Datas---------------------------");
        logger.info("Record Indiactor is 3 So Updating Datas---------------------------");
        pst = new LoggableStatement(con,
          "UPDATE ETT_EDPMS_SHP SET RECIND=?,FILENO=? WHERE  SHIPBILLNO=? AND SHIPBILLDATE=? AND PORTCODE=? ");
        pst.setString(1, shipingVO.getRecInd());
        pst.setInt(2, shipingVO.getFileNo());
        pst.setString(3, shipingVO.getShippingBillNo());
        pst.setString(4, shipingVO.getShippingBillDate());
        pst.setString(5, shipingVO.getPortCode());
        noShipBill = pst.executeUpdate();
        logger.info("ETT_EDPMS_SHP-------Update Query-----------" + pst.getQueryString());
        logger.info("ETT_EDPMS_SHP-------Update Query-----------" + pst.getQueryString());
        logger.info("NoShipBill - " + noShipBill);
        logger.info("NoShipBill - " + noShipBill);
        shipingVO.setNoShipBillNo(noShipBill);
        pst.close();
      }
      else
      {
        logger.info("Record Indiactor is not 3  So Updating Datas---------------------------");
        logger.info("Record Indiactor is 3 So Updating Datas---------------------------");
        pst = new LoggableStatement(con,
          "UPDATE ETT_EDPMS_SHP SET LEODATE=TO_DATE(?, 'DD-MM-YYYY'), CUSTNO=?,FORMNO=?, IECODE=?,ADCODE=?,EXPORTAGENCY=?,EXPORTTYPE=?,RECIND=?,COUNTRYDEST=?,FILENO=? WHERE SHIPBILLNO=? AND SHIPBILLDATE=? AND PORTCODE=?");
        pst.setString(1, shipingVO.getLeoDate());
        pst.setString(2, shipingVO.getCustSerialNo());
        pst.setString(3, shipingVO.getFormNo());
        pst.setString(4, shipingVO.getIeCode());
        pst.setString(5, shipingVO.getAdCode());
        pst.setString(6, shipingVO.getExportAgency());
        pst.setString(7, shipingVO.getExportType());
        pst.setString(8, shipingVO.getRecInd());
        pst.setString(9, shipingVO.getCountryOfDest());
        pst.setInt(10, shipingVO.getFileNo());
        pst.setString(11, shipingVO.getShippingBillNo());
        pst.setString(12, shipingVO.getShippingBillDate());
        pst.setString(13, shipingVO.getPortCode());
        noShipBill = pst.executeUpdate();
        logger.info("ETT_EDPMS_SHP-------Update Query-----------" + pst.getQueryString());
        logger.info("ETT_EDPMS_SHP-------Update Query-----------" + pst.getQueryString());
        logger.info("NoShipBill - " + noShipBill);
        pst.close();
        if ((noShipBill > 0) && (invoiceList != null)) {
          for (int i = 0; i < invoiceList.size(); i++)
          {
            InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
           
            String invErrorString = "";
            if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice Serial No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobCurr())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Currency is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobAmt())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Amount is Empty");
            }
            if (commonMethods.isNull(invErrorString))
            {
              LoggableStatement pst1 = new LoggableStatement(con,
                "UPDATE ETT_EDPMS_SHP_INV SET INVDATE=TO_DATE(?, 'DD-MM-YYYY'), FOBCURRCODE=?,FOBAMT=?,FRIEGHTCURRCODE=?,FRIEGHTAMT=?,INSCURRCODE=?,INSAMT=?, COMMCURRCODE=?,COMMAMT=?,DISCURRCODE=?,DISAMT=?,DEDCURRCODE=?,DEDAMT=?,PKGCURRCODE=?,PKGAMT=?,FILENO=? WHERE  SHIPBILLNO=? AND SHIPBILLDATE=? AND PORTCODE=? AND INVSERIALNO=? AND INVNO=? ");
              pst1.setString(1, invoiceVO.getInvoiceDate());
              pst1.setString(2, invoiceVO.getFobCurr());
              pst1.setString(3, invoiceVO.getFobAmt());
              pst1.setString(4, invoiceVO.getFreightCurr());
              pst1.setString(5, invoiceVO.getFreightAmt());
              pst1.setString(6, invoiceVO.getInsuranceCurr());
              pst1.setString(7, invoiceVO.getInsuranceAmt());
              pst1.setString(8, invoiceVO.getCommissionCurr());
              pst1.setString(9, invoiceVO.getCommissionAmt());
              pst1.setString(10, invoiceVO.getDiscountCurr());
              pst1.setString(11, invoiceVO.getDiscountAmt());
              pst1.setString(12, invoiceVO.getDeductionCurr());
              pst1.setString(13, invoiceVO.getDeductionAmt());
              pst1.setString(14, invoiceVO.getPackagingCurr());
              pst1.setString(15, invoiceVO.getPackagingAmt());
              pst1.setInt(16, shipingVO.getFileNo());
              pst1.setString(17, shipingVO.getShippingBillNo());
              pst1.setString(18, shipingVO.getShippingBillDate());
              pst1.setString(19, shipingVO.getPortCode());
              pst1.setString(20, invoiceVO.getInvoiceSerialNo());
              pst1.setString(21, invoiceVO.getInvoiceNo());
             
              int iCount = pst1.executeUpdate();
              invCount += iCount;
              logger.info("ETT_EDPMS_SHP_INV--Update------Query" + pst1.getQueryString());
              logger.info("ETT_EDPMS_SHP_INV--Update------Query" + pst1.getQueryString());
              logger.info("Invoice Update -->" + invCount);
              pst1.close();
            }
            else
            {
              excelDataVO = new ExcelDataVO();
              excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
              excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
              excelDataVO.setPortCode(shipingVO.getPortCode());
              excelDataVO.setFormNo(shipingVO.getFormNo());
              excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
              excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
              excelDataVO.setShpErrorString(invErrorString);
              excelDataVO.setFileNo(shipingVO.getFileNo());
              xmlErrorList.add(excelDataVO);
            }
          }
        }
        if ((noShipBill > 0) && (invCount > 0)) {
          shipingVO.setNoShipBillNo(noShipBill);
        }
      }
      shipingVO.setXmlErrorList(xmlErrorList);
    }
    catch (Exception exception)
    {
      logger.info("Exception----" + exception.getMessage());
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public ShippingDetailsVO updateEDPMS_EDIData_SEZ(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    try
    {
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      logger.info("shipn umber----------------" + shipingVO.getShippingBillNo());
      logger.info("shipn umber----------------" + shipingVO.getShippingBillDate());
      logger.info("shipn umber----------------" + shipingVO.getPortCode());
      if ((shipingVO != null) && (shipingVO.getRecInd().equalsIgnoreCase("3")))
      {
        logger.info("Record Indiactor is 3 So Updating Datas---------------------------");
        logger.info("Record Indiactor is 3 So Updating Datas---------------------------");
        pst = new LoggableStatement(con,
          "UPDATE ETT_EDPMS_SHP SET RECIND=?,FILENO=? WHERE  SHIPBILLNO=? AND SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=?");
        pst.setString(1, shipingVO.getRecInd());
        pst.setInt(2, shipingVO.getFileNo());
        pst.setString(3, shipingVO.getShippingBillNo());
        pst.setString(4, shipingVO.getShippingBillDate());
        pst.setString(5, shipingVO.getPortCode());
        pst.setString(6, shipingVO.getFormNo());
        noShipBill = pst.executeUpdate();
        logger.info("ETT_EDPMS_SHP-------Update Query-----------" + pst.getQueryString());
        logger.info("ETT_EDPMS_SHP-------Update Query-----------" + pst.getQueryString());
        logger.info("NoShipBill - " + noShipBill);
        logger.info("NoShipBill - " + noShipBill);
        shipingVO.setNoShipBillNo(noShipBill);
        pst.close();
      }
      else
      {
        logger.info("Record Indiactor is not 3  So Updating Datas---------------------------");
        logger.info("Record Indiactor is 3 So Updating Datas---------------------------");
        pst = new LoggableStatement(con,
          "UPDATE ETT_EDPMS_SHP SET LEODATE=TO_DATE(?, 'DD-MM-YYYY'), CUSTNO=?, IECODE=?,ADCODE=?,EXPORTAGENCY=?,EXPORTTYPE=?,RECIND=?,COUNTRYDEST=?,FILENO=? WHERE SHIPBILLNO=? AND SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=?");
        pst.setString(1, shipingVO.getLeoDate());
        pst.setString(2, shipingVO.getCustSerialNo());
        pst.setString(3, shipingVO.getIeCode());
        pst.setString(4, shipingVO.getAdCode());
        pst.setString(5, shipingVO.getExportAgency());
        pst.setString(6, shipingVO.getExportType());
        pst.setString(7, shipingVO.getRecInd());
        pst.setString(8, shipingVO.getCountryOfDest());
        pst.setInt(9, shipingVO.getFileNo());
        pst.setString(10, shipingVO.getShippingBillNo());
        pst.setString(11, shipingVO.getShippingBillDate());
        pst.setString(12, shipingVO.getPortCode());
        pst.setString(13, shipingVO.getFormNo());
        noShipBill = pst.executeUpdate();
        logger.info("ETT_EDPMS_SHP-------Update Query-----------" + pst.getQueryString());
        logger.info("ETT_EDPMS_SHP-------Update Query-----------" + pst.getQueryString());
        logger.info("NoShipBill - " + noShipBill);
        pst.close();
        if ((noShipBill > 0) && (invoiceList != null)) {
          for (int i = 0; i < invoiceList.size(); i++)
          {
            InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
           
            String invErrorString = "";
            if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice Serial No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobCurr())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Currency is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobAmt())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Amount is Empty");
            }
            if (commonMethods.isNull(invErrorString))
            {
              LoggableStatement pst1 = new LoggableStatement(con,
                "UPDATE ETT_EDPMS_SHP_INV SET INVDATE=TO_DATE(?, 'DD-MM-YYYY'), FOBCURRCODE=?,FOBAMT=?,FRIEGHTCURRCODE=?,FRIEGHTAMT=?,INSCURRCODE=?,INSAMT=?, COMMCURRCODE=?,COMMAMT=?,DISCURRCODE=?,DISAMT=?,DEDCURRCODE=?,DEDAMT=?,PKGCURRCODE=?,PKGAMT=?,FILENO=? WHERE  SHIPBILLNO=? AND SHIPBILLDATE=? AND PORTCODE=? AND INVSERIALNO=? AND INVNO=? AND FORMNO=?");
              pst1.setString(1, invoiceVO.getInvoiceDate());
              pst1.setString(2, invoiceVO.getFobCurr());
              pst1.setString(3, invoiceVO.getFobAmt());
              pst1.setString(4, invoiceVO.getFreightCurr());
              pst1.setString(5, invoiceVO.getFreightAmt());
              pst1.setString(6, invoiceVO.getInsuranceCurr());
              pst1.setString(7, invoiceVO.getInsuranceAmt());
              pst1.setString(8, invoiceVO.getCommissionCurr());
              pst1.setString(9, invoiceVO.getCommissionAmt());
              pst1.setString(10, invoiceVO.getDiscountCurr());
              pst1.setString(11, invoiceVO.getDiscountAmt());
              pst1.setString(12, invoiceVO.getDeductionCurr());
              pst1.setString(13, invoiceVO.getDeductionAmt());
              pst1.setString(14, invoiceVO.getPackagingCurr());
              pst1.setString(15, invoiceVO.getPackagingAmt());
              pst1.setInt(16, shipingVO.getFileNo());
              pst1.setString(17, shipingVO.getShippingBillNo());
              pst1.setString(18, shipingVO.getShippingBillDate());
              pst1.setString(19, shipingVO.getPortCode());
              pst1.setString(20, invoiceVO.getInvoiceSerialNo());
              pst1.setString(21, invoiceVO.getInvoiceNo());
              pst1.setString(22, invoiceVO.getFormNo());
             
              int iCount = pst1.executeUpdate();
              invCount += iCount;
              logger.info("ETT_EDPMS_SHP_INV--Update------Query" + pst1.getQueryString());
              logger.info("ETT_EDPMS_SHP_INV--Update------Query" + pst1.getQueryString());
              logger.info("Invoice Update -->" + invCount);
              pst1.close();
            }
            else
            {
              excelDataVO = new ExcelDataVO();
              excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
              excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
              excelDataVO.setPortCode(shipingVO.getPortCode());
              excelDataVO.setFormNo(shipingVO.getFormNo());
              excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
              excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
              excelDataVO.setShpErrorString(invErrorString);
              excelDataVO.setFileNo(shipingVO.getFileNo());
              xmlErrorList.add(excelDataVO);
            }
          }
        }
        if ((noShipBill > 0) && (invCount > 0)) {
          shipingVO.setNoShipBillNo(noShipBill);
        }
      }
      shipingVO.setXmlErrorList(xmlErrorList);
    }
    catch (Exception exception)
    {
      logger.info("Exception----" + exception.getMessage());
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public ShippingDetailsVO insertEDPMS_EDI_INV_Data(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    try
    {
      logger.info("Insert_EDPMS_EDI_Data");
     
      commonMethods = new CommonMethods();
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      if (invoiceList != null) {
        for (int i = 0; i < invoiceList.size(); i++)
        {
          InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
         
          String invErrorString = "";
         
          LoggableStatement pst2 = new LoggableStatement(con,
            "SELECT COUNT(*) AS COUNT FROM ETT_EDPMS_SHP_INV WHERE  SHIPBILLNO=? AND SHIPBILLDATE =? AND PORTCODE=? AND INVSERIALNO=? AND INVNO=? ");
          pst2.setString(1, shipingVO.getShippingBillNo());
          pst2.setString(2, shipingVO.getShippingBillDate());
          pst2.setString(3, shipingVO.getPortCode());
          pst2.setString(4, invoiceVO.getInvoiceSerialNo());
          pst2.setString(5, invoiceVO.getInvoiceNo());
          ResultSet edi_rs = pst2.executeQuery();
          int k = 0;
          if (edi_rs.next()) {
            k = edi_rs.getInt("COUNT");
          }
          edi_rs.close();
          pst2.close();
          if (k == 0)
          {
            if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice Serial No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobCurr())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Currency is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobAmt())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Amount is Empty");
            }
            if (commonMethods.isNull(invErrorString))
            {
              LoggableStatement pst1 = new LoggableStatement(con,
                "INSERT INTO ETT_EDPMS_SHP_INV(SHIPBILLNO,SHIPBILLDATE,PORTCODE,FILENO,INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE,INSAMT,COMMCURRCODE,COMMAMT,DISCURRCODE,DISAMT,DEDCURRCODE,DEDAMT,PKGCURRCODE,PKGAMT) values(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             
              pst1.setString(1, shipingVO.getShippingBillNo());
              pst1.setString(2, shipingVO.getShippingBillDate());
              pst1.setString(3, shipingVO.getPortCode());
              pst1.setInt(4, shipingVO.getFileNo());
              pst1.setString(5, invoiceVO.getInvoiceSerialNo());
              pst1.setString(6, invoiceVO.getInvoiceNo());
              pst1.setString(7, invoiceVO.getInvoiceDate());
              pst1.setString(8, invoiceVO.getFobCurr());
              pst1.setString(9, invoiceVO.getFobAmt());
              pst1.setString(10, invoiceVO.getFreightCurr());
              pst1.setString(11, invoiceVO.getFreightAmt());
              pst1.setString(12, invoiceVO.getInsuranceCurr());
              pst1.setString(13, invoiceVO.getInsuranceAmt());
              pst1.setString(14, invoiceVO.getCommissionCurr());
              pst1.setString(15, invoiceVO.getCommissionAmt());
              pst1.setString(16, invoiceVO.getDiscountCurr());
              pst1.setString(17, invoiceVO.getDiscountAmt());
              pst1.setString(18, invoiceVO.getDeductionCurr());
              pst1.setString(19, invoiceVO.getDeductionAmt());
              pst1.setString(20, invoiceVO.getPackagingCurr());
              pst1.setString(21, invoiceVO.getPackagingAmt());
              int iCount = pst1.executeUpdate();
              invCount += iCount;
              this.invoicecount = invCount;
              pst1.close();
            }
            else
            {
              excelDataVO = new ExcelDataVO();
              excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
              excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
              excelDataVO.setPortCode(shipingVO.getPortCode());
              excelDataVO.setFormNo(shipingVO.getFormNo());
              excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
              excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
              excelDataVO.setShpErrorString(invErrorString);
              excelDataVO.setFileNo(shipingVO.getFileNo());
              xmlErrorList.add(excelDataVO);
            }
          }
          else
          {
            excelDataVO = new ExcelDataVO();
            logger.info("duplicate");
            invErrorString = commonMethods.setErrorString(invErrorString, "Duplicate Data");
           
            excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
            excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
            excelDataVO.setPortCode(shipingVO.getPortCode());
            excelDataVO.setFormNo(shipingVO.getFormNo());
            excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
            excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
            excelDataVO.setShpErrorString(invErrorString);
            excelDataVO.setFileNo(shipingVO.getFileNo());
            xmlErrorList.add(excelDataVO);
          }
        }
      }
      if (invCount > 0)
      {
        noShipBill = 1;
        shipingVO.setNoShipBillNo(noShipBill);
      }
      logger.info("xmlerrorlist" + shipingVO.getXmlErrorList());
      if (xmlErrorList.size() > 0) {
        deleteMDFErrorRecords(con, xmlErrorList);
      }
      shipingVO.setXmlErrorList(xmlErrorList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public ShippingDetailsVO insertEDPMS_SoftexData(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    int staging = 0;
    try
    {
      logger.info("Insert_EDPMS_Softex_Data");
     
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
     
      logger.info("staging---" + staging);
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      pst = new LoggableStatement(con,
        "INSERT INTO ETT_EDPMS_SHP_SOFTEX(SHIPBILLDATE,PORTCODE,LEODATE,CUSTNO,FORMNO,IECODE,ADCODE,EXPORTAGENCY,EXPORTTYPE,RECIND,COUNTRYDEST,FILENO,SHIPBILLNO,CREATEDON_DATE)values(?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,?, SYSTIMESTAMP)");
     
      pst.setString(1, shipingVO.getShippingBillDate());
      pst.setString(2, shipingVO.getPortCode());
      pst.setString(3, shipingVO.getLeoDate());
      pst.setString(4, shipingVO.getCustSerialNo());
      pst.setString(5, shipingVO.getFormNo());
      pst.setString(6, shipingVO.getIeCode());
      pst.setString(7, shipingVO.getAdCode());
      pst.setString(8, shipingVO.getExportAgency());
      pst.setString(9, shipingVO.getExportType());
      pst.setString(10, shipingVO.getRecInd());
      pst.setString(11, shipingVO.getCountryOfDest());
      pst.setInt(12, shipingVO.getFileNo());
      pst.setString(13, shipingVO.getShippingBillNo());
     
      noShipBill = pst.executeUpdate();
     
      logger.info("ETT_EDPMS_SHP_SOFTEX-----Inserted --------------" + pst.getQueryString());
      logger.info("ETT_EDPMS_SHP_SOFTEX-----Inserted --------------" + pst.getQueryString());
      logger.info("ETT_EDPMS_SHP_SOFTEX NoShipBill - " + noShipBill);
      logger.info("ETT_EDPMS_SHP_SOFTEX NoShipBill - " + noShipBill);
      pst.close();
      if ((noShipBill > 0) && (invoiceList != null)) {
        for (int i = 0; i < invoiceList.size(); i++)
        {
          InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
         
          String invErrorString = "";
         
          LoggableStatement pst2 = new LoggableStatement(con,
            "SELECT COUNT(*) AS COUNT FROM ETT_EDPMS_SHP_INV_SOFTEX WHERE SHIPBILLDATE =? AND PORTCODE=? AND FORMNO=? AND INVSERIALNO=? AND INVNO=? AND SHIPBILLNO=?");
          pst2.setString(1, shipingVO.getShippingBillDate());
          pst2.setString(2, shipingVO.getPortCode());
          pst2.setString(3, shipingVO.getFormNo());
          pst2.setString(4, invoiceVO.getInvoiceSerialNo());
          pst2.setString(5, invoiceVO.getInvoiceNo());
          pst2.setString(6, invoiceVO.getShippingBillNO());
          ResultSet rs = pst2.executeQuery();
         
          logger.info("ETT_EDPMS_SHP_INV_SOFTEX------------------Query------" + pst2.getQueryString());
          logger.info("ETT_EDPMS_SHP_INV_SOFTEX------------------Query------" + pst2.getQueryString());
          int k = 0;
          if (rs.next()) {
            k = rs.getInt("COUNT");
          }
          closeStatementResultSet(pst2, rs);
         
          logger.info("ETT_EDPMS_SHP_INV_SOFTEX-----------k--COUNT-----------" + k);
          logger.info("-------IF K IS 0 It gives error or else it provides Duplicate Records ");
          logger.info("ETT_EDPMS_SHP_INV_SOFTEX-----------k----COUNT---------" + k);
          if (k == 0)
          {
            if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice Serial No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobCurr())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Currency is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobAmt())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Amount is Empty");
            }
            if (commonMethods.isNull(invErrorString))
            {
              LoggableStatement pst1 = new LoggableStatement(con,
                "INSERT INTO ETT_EDPMS_SHP_INV_SOFTEX(SHIPBILLDATE,PORTCODE,FORMNO,FILENO,INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE,INSAMT,COMMCURRCODE,COMMAMT,DISCURRCODE,DISAMT,DEDCURRCODE,DEDAMT,PKGCURRCODE,PKGAMT,SHIPBILLNO,CREATEDON_DATE) values(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP)");
             
              pst1.setString(1, shipingVO.getShippingBillDate());
              pst1.setString(2, shipingVO.getPortCode());
              pst1.setString(3, shipingVO.getFormNo());
              pst1.setInt(4, shipingVO.getFileNo());
              pst1.setString(5, invoiceVO.getInvoiceSerialNo());
              pst1.setString(6, invoiceVO.getInvoiceNo());
              pst1.setString(7, invoiceVO.getInvoiceDate());
              pst1.setString(8, invoiceVO.getFobCurr());
              pst1.setString(9, invoiceVO.getFobAmt());
              pst1.setString(10, invoiceVO.getFreightCurr());
              pst1.setString(11, invoiceVO.getFreightAmt());
              pst1.setString(12, invoiceVO.getInsuranceCurr());
              pst1.setString(13, invoiceVO.getInsuranceAmt());
              pst1.setString(14, invoiceVO.getCommissionCurr());
              pst1.setString(15, invoiceVO.getCommissionAmt());
              pst1.setString(16, invoiceVO.getDiscountCurr());
              pst1.setString(17, invoiceVO.getDiscountAmt());
              pst1.setString(18, invoiceVO.getDeductionCurr());
              pst1.setString(19, invoiceVO.getDeductionAmt());
              pst1.setString(20, invoiceVO.getPackagingCurr());
              pst1.setString(21, invoiceVO.getPackagingAmt());
              pst1.setString(22, shipingVO.getShippingBillNo());
             

              int iCount = pst1.executeUpdate();
              invCount += iCount;
             
              this.invoicecount = invCount;
             
              logger.info("ETT_EDPMS_SHP_INV_SOFTEX---------Insert Query--------------" +
                pst1.getQueryString());
              logger.info("ETT_EDPMS_SHP_INV_SOFTEX---------Insert Query--------------" +
                pst1.getQueryString());
              logger.info("Invoice Update -->" + invCount);
              logger.info("Invoice Update -->" + invCount);
              pst1.close();
            }
            else
            {
              excelDataVO = new ExcelDataVO();
              excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
              excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
              excelDataVO.setPortCode(shipingVO.getPortCode());
              excelDataVO.setFormNo(shipingVO.getFormNo());
              excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
              excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
              excelDataVO.setShpErrorString(invErrorString);
              excelDataVO.setFileNo(shipingVO.getFileNo());
             

              xmlErrorList.add(excelDataVO);
            }
          }
          else
          {
            excelDataVO = new ExcelDataVO();
            invErrorString = commonMethods.setErrorString(invErrorString, "Duplicate Data");
            excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
            excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
            excelDataVO.setPortCode(shipingVO.getPortCode());
            excelDataVO.setFormNo(shipingVO.getFormNo());
            excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
            excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
            excelDataVO.setShpErrorString(invErrorString);
            excelDataVO.setFileNo(shipingVO.getFileNo());
           

            xmlErrorList.add(excelDataVO);
          }
        }
      }
      if (xmlErrorList.size() > 0)
      {
        deleteSOFTEXErrorRecords(con, xmlErrorList);
        noShipBill = 0;
      }
      if ((noShipBill > 0) && (invCount > 0)) {
        shipingVO.setNoShipBillNo(noShipBill);
      }
      shipingVO.setXmlErrorList(xmlErrorList);
    }
    catch (Exception exception)
    {
      logger.info("insertEDPMS_SoftexData---------Exception-------" + exception);
      logger.info("insertEDPMS_SoftexData---------Exception-------" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public int createTempEDPMS_SoftexData(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int insertCount = 0;
    try
    {
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      pst = new LoggableStatement(con,
        "INSERT INTO ETT_TEMP_EDPMS_SHP_SOFTEX(SHIPBILLDATE,LEODATE,CUSTNO,FORMNO,IECODE,ADCODE,PORTCODE,EXPORTAGENCY,EXPORTTYPE,RECIND,COUNTRYDEST,FILENO)SELECT SHIPBILLDATE,LEODATE,CUSTNO,FORMNO,IECODE,ADCODE,PORTCODE,EXPORTAGENCY,EXPORTTYPE,RECIND,COUNTRYDEST,FILENO FROM ETT_EDPMS_SHP_SOFTEX WHERE  SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=?");
      pst.setString(1, shipingVO.getShippingBillDate());
      pst.setString(2, shipingVO.getPortCode());
      pst.setString(3, shipingVO.getFormNo());
      insertCount = pst.executeUpdate();
      pst.close();
      if ((insertCount > 0) && (invoiceList != null)) {
        for (int i = 0; i < invoiceList.size(); i++)
        {
          InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
         
          LoggableStatement pst1 = new LoggableStatement(con,
            "INSERT INTO ETT_TEMP_EDPMS_SHP_INV_SOFTEX(SHIPBILLDATE,PORTCODE,FORMNO,FILENO, INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE, INSAMT,COMMCURRCODE,COMMAMT,DISCURRCODE,DISAMT,DEDCURRCODE,DEDAMT,PKGCURRCODE,PKGAMT)  SELECT SHIPBILLDATE,PORTCODE,FORMNO,FILENO, INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE, INSAMT,COMMCURRCODE,COMMAMT,DISCURRCODE,DISAMT,DEDCURRCODE,DEDAMT,PKGCURRCODE,PKGAMT FROM ETT_EDPMS_SHP_INV_SOFTEX WHERE  SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=? AND INVSERIALNO=? AND INVNO=? ");
          pst1.setString(1, shipingVO.getShippingBillDate());
          pst1.setString(2, shipingVO.getPortCode());
          pst1.setString(3, shipingVO.getFormNo());
          pst1.setString(4, invoiceVO.getInvoiceSerialNo());
          pst1.setString(5, invoiceVO.getInvoiceNo());
          int insShpCount = pst1.executeUpdate();
          logger.info("Inserted Count--->" + insShpCount);
          pst1.close();
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return insertCount;
  }
 
  public ShippingDetailsVO updateEDPMS_SoftexData_SEZ(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    try
    {
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      if ((shipingVO != null) && (shipingVO.getShippingBillNo().equalsIgnoreCase("3")))
      {
        pst = new LoggableStatement(con,
          "UPDATE ETT_EDPMS_SHP_SOFTEX SET RECIND=?,FILENO=? WHERE  SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=? AND SHIPBILLNO=? ");
        pst.setString(1, shipingVO.getRecInd());
        pst.setInt(2, shipingVO.getFileNo());
        pst.setString(3, shipingVO.getShippingBillDate());
        pst.setString(4, shipingVO.getPortCode());
        pst.setString(5, shipingVO.getFormNo());
        pst.setString(5, shipingVO.getShippingBillNo());
        noShipBill = pst.executeUpdate();
       
        logger.info("Softex Update Query------------------------------->" + pst.getQueryString());
        logger.info("Softex Update Query------------------------------->" + pst.getQueryString());
       
        logger.info("Softex Update NoShipBill - -----" + noShipBill);
        logger.info(" Softex UpdateNoShipBill ------- " + noShipBill);
       
        shipingVO.setNoShipBillNo(noShipBill);
        pst.close();
      }
      else
      {
        pst = new LoggableStatement(con,
          "UPDATE ETT_EDPMS_SHP_SOFTEX SET LEODATE=TO_DATE(?, 'DD-MM-YYYY'), CUSTNO=?, IECODE=?,ADCODE=?,EXPORTAGENCY=?,EXPORTTYPE=?,RECIND=?,COUNTRYDEST=?,FILENO=? WHERE  SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=? AND SHIPBILLNO=?");
        pst.setString(1, shipingVO.getLeoDate());
        pst.setString(2, shipingVO.getCustSerialNo());
        pst.setString(3, shipingVO.getIeCode());
        pst.setString(4, shipingVO.getAdCode());
        pst.setString(5, shipingVO.getExportAgency());
        pst.setString(6, shipingVO.getExportType());
        pst.setString(7, shipingVO.getRecInd());
        pst.setString(8, shipingVO.getCountryOfDest());
        pst.setInt(9, shipingVO.getFileNo());
        pst.setString(10, shipingVO.getShippingBillDate());
        pst.setString(11, shipingVO.getPortCode());
        pst.setString(12, shipingVO.getFormNo());
        pst.setString(13, shipingVO.getShippingBillNo());
        noShipBill = pst.executeUpdate();
        logger.info("Update ETT_EDPMS_SHP_SOFTEX Query---------------------->" + pst.getQueryString());
        logger.info("Update ETT_EDPMS_SHP_SOFTEX Query---------------------->" + pst.getQueryString());
        logger.info("NoShipBill - " + noShipBill);
        pst.close();
        if ((noShipBill > 0) && (invoiceList != null)) {
          for (int i = 0; i < invoiceList.size(); i++)
          {
            InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
           
            String invErrorString = "";
            if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice Serial No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobCurr())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Currency is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobAmt())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Amount is Empty");
            }
            if (commonMethods.isNull(invErrorString))
            {
              LoggableStatement pst1 = new LoggableStatement(con,
                "UPDATE ETT_EDPMS_SHP_INV_SOFTEX SET INVDATE=TO_DATE(?, 'DD-MM-YYYY'), FOBCURRCODE=?,FOBAMT=?,FRIEGHTCURRCODE=?,FRIEGHTAMT=?,INSCURRCODE=?,INSAMT=?, COMMCURRCODE=?,COMMAMT=?,DISCURRCODE=?,DISAMT=?,DEDCURRCODE=?,DEDAMT=?,PKGCURRCODE=?,PKGAMT=?,FILENO=? WHERE  SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=? AND INVSERIALNO=? AND INVNO=? AND SHIPBILLNO=?");
              pst1.setString(1, invoiceVO.getInvoiceDate());
              pst1.setString(2, invoiceVO.getFobCurr());
              pst1.setString(3, invoiceVO.getFobAmt());
              pst1.setString(4, invoiceVO.getFreightCurr());
              pst1.setString(5, invoiceVO.getFreightAmt());
              pst1.setString(6, invoiceVO.getInsuranceCurr());
              pst1.setString(7, invoiceVO.getInsuranceAmt());
              pst1.setString(8, invoiceVO.getCommissionCurr());
              pst1.setString(9, invoiceVO.getCommissionAmt());
              pst1.setString(10, invoiceVO.getDiscountCurr());
              pst1.setString(11, invoiceVO.getDiscountAmt());
              pst1.setString(12, invoiceVO.getDeductionCurr());
              pst1.setString(13, invoiceVO.getDeductionAmt());
              pst1.setString(14, invoiceVO.getPackagingCurr());
              pst1.setString(15, invoiceVO.getPackagingAmt());
              pst1.setInt(16, shipingVO.getFileNo());
              pst1.setString(17, shipingVO.getShippingBillDate());
              pst1.setString(18, shipingVO.getPortCode());
              pst1.setString(19, shipingVO.getFormNo());
              pst1.setString(20, invoiceVO.getInvoiceSerialNo());
              pst1.setString(21, invoiceVO.getInvoiceNo());
              pst1.setString(21, invoiceVO.getShippingBillNO());
             
              logger.info("ETT_EDPMS_SHP_INV_SOFTEX---------------Update---Query---------" +
                pst1.getQueryString());
              logger.info("ETT_EDPMS_SHP_INV_SOFTEX---------------Update---Query---------" +
                pst1.getQueryString());
             
              int iCount = pst1.executeUpdate();
              invCount += iCount;
              logger.info("ETT_EDPMS_SHP_INV_SOFTEX ----------Invoice Update count-->" + invCount);
             
              pst1.close();
            }
            else
            {
              excelDataVO = new ExcelDataVO();
              excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
              excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
              excelDataVO.setPortCode(shipingVO.getPortCode());
              excelDataVO.setFormNo(shipingVO.getFormNo());
              excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
              excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
              excelDataVO.setShpErrorString(invErrorString);
              excelDataVO.setFileNo(shipingVO.getFileNo());
             

              xmlErrorList.add(excelDataVO);
            }
          }
        }
        if ((noShipBill > 0) && (invCount > 0)) {
          shipingVO.setNoShipBillNo(noShipBill);
        }
      }
      shipingVO.setXmlErrorList(xmlErrorList);
    }
    catch (Exception exception)
    {
      logger.info("updateEDPMS_SoftexData--------count------------" + exception);
      logger.info("updateEDPMS_SoftexData----------------count----" + exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public ShippingDetailsVO updateEDPMS_SoftexData(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    try
    {
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      if ((shipingVO != null) && (shipingVO.getShippingBillNo().equalsIgnoreCase("3")))
      {
        pst = new LoggableStatement(con,
          "UPDATE ETT_EDPMS_SHP_SOFTEX SET RECIND=?,FILENO=? WHERE  SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=? ");
        pst.setString(1, shipingVO.getRecInd());
        pst.setInt(2, shipingVO.getFileNo());
        pst.setString(3, shipingVO.getShippingBillDate());
        pst.setString(4, shipingVO.getPortCode());
        pst.setString(5, shipingVO.getFormNo());
       

        logger.info("Softex Update Query------------------------------->" + pst.getQueryString());
        logger.info("Softex Update Query------------------------------->" + pst.getQueryString());
       
        logger.info("Softex Update NoShipBill - -----" + noShipBill);
        logger.info(" Softex UpdateNoShipBill ------- " + noShipBill);
       
        shipingVO.setNoShipBillNo(noShipBill);
        pst.close();
      }
      else
      {
        pst = new LoggableStatement(con,
          "UPDATE ETT_EDPMS_SHP_SOFTEX SET LEODATE=TO_DATE(?, 'DD-MM-YYYY'), CUSTNO=?, IECODE=?,ADCODE=?,EXPORTAGENCY=?,EXPORTTYPE=?,RECIND=?,COUNTRYDEST=?,FILENO=? WHERE  SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=?");
        pst.setString(1, shipingVO.getLeoDate());
        pst.setString(2, shipingVO.getCustSerialNo());
        pst.setString(3, shipingVO.getIeCode());
        pst.setString(4, shipingVO.getAdCode());
        pst.setString(5, shipingVO.getExportAgency());
        pst.setString(6, shipingVO.getExportType());
        pst.setString(7, shipingVO.getRecInd());
        pst.setString(8, shipingVO.getCountryOfDest());
        pst.setInt(9, shipingVO.getFileNo());
        pst.setString(10, shipingVO.getShippingBillDate());
        pst.setString(11, shipingVO.getPortCode());
        pst.setString(12, shipingVO.getFormNo());
       
        logger.info("Update ETT_EDPMS_SHP_SOFTEX Query---------------------->" + pst.getQueryString());
        logger.info("Update ETT_EDPMS_SHP_SOFTEX Query---------------------->" + pst.getQueryString());
        logger.info("NoShipBill - " + noShipBill);
        pst.close();
        if ((noShipBill > 0) && (invoiceList != null)) {
          for (int i = 0; i < invoiceList.size(); i++)
          {
            InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
           
            String invErrorString = "";
            if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice Serial No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobCurr())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Currency is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobAmt())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Amount is Empty");
            }
            if (commonMethods.isNull(invErrorString))
            {
              LoggableStatement pst1 = new LoggableStatement(con,
                "UPDATE ETT_EDPMS_SHP_INV_SOFTEX SET INVDATE=TO_DATE(?, 'DD-MM-YYYY'), FOBCURRCODE=?,FOBAMT=?,FRIEGHTCURRCODE=?,FRIEGHTAMT=?,INSCURRCODE=?,INSAMT=?, COMMCURRCODE=?,COMMAMT=?,DISCURRCODE=?,DISAMT=?,DEDCURRCODE=?,DEDAMT=?,PKGCURRCODE=?,PKGAMT=?,FILENO=? WHERE  SHIPBILLDATE=? AND PORTCODE=? AND FORMNO=? AND INVSERIALNO=? AND INVNO=? ");
              pst1.setString(1, invoiceVO.getInvoiceDate());
              pst1.setString(2, invoiceVO.getFobCurr());
              pst1.setString(3, invoiceVO.getFobAmt());
              pst1.setString(4, invoiceVO.getFreightCurr());
              pst1.setString(5, invoiceVO.getFreightAmt());
              pst1.setString(6, invoiceVO.getInsuranceCurr());
              pst1.setString(7, invoiceVO.getInsuranceAmt());
              pst1.setString(8, invoiceVO.getCommissionCurr());
              pst1.setString(9, invoiceVO.getCommissionAmt());
              pst1.setString(10, invoiceVO.getDiscountCurr());
              pst1.setString(11, invoiceVO.getDiscountAmt());
              pst1.setString(12, invoiceVO.getDeductionCurr());
              pst1.setString(13, invoiceVO.getDeductionAmt());
              pst1.setString(14, invoiceVO.getPackagingCurr());
              pst1.setString(15, invoiceVO.getPackagingAmt());
              pst1.setInt(16, shipingVO.getFileNo());
              pst1.setString(17, shipingVO.getShippingBillDate());
              pst1.setString(18, shipingVO.getPortCode());
              pst1.setString(19, shipingVO.getFormNo());
              pst1.setString(20, invoiceVO.getInvoiceSerialNo());
              pst1.setString(21, invoiceVO.getInvoiceNo());
             
              logger.info("ETT_EDPMS_SHP_INV_SOFTEX---------------Update---Query---------" +
                pst1.getQueryString());
              logger.info("ETT_EDPMS_SHP_INV_SOFTEX---------------Update---Query---------" +
                pst1.getQueryString());
              int iCount = 0;
             
              invCount += iCount;
              logger.info("ETT_EDPMS_SHP_INV_SOFTEX ----------Invoice Update count-->" + invCount);
             
              pst1.close();
            }
            else
            {
              excelDataVO = new ExcelDataVO();
              excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
              excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
              excelDataVO.setPortCode(shipingVO.getPortCode());
              excelDataVO.setFormNo(shipingVO.getFormNo());
              excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
              excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
              excelDataVO.setShpErrorString(invErrorString);
              excelDataVO.setFileNo(shipingVO.getFileNo());
             

              xmlErrorList.add(excelDataVO);
            }
          }
        }
        if ((noShipBill > 0) && (invCount > 0)) {
          shipingVO.setNoShipBillNo(noShipBill);
        }
      }
      shipingVO.setXmlErrorList(xmlErrorList);
    }
    catch (Exception exception)
    {
      logger.info("updateEDPMS_SoftexData--------count------------" + exception);
      logger.info("updateEDPMS_SoftexData----------------count----" + exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public ShippingDetailsVO insertEDPMS_Softex_Inv_Data(Connection con, ShippingDetailsVO shipingVO, ArrayList<InvoiceDetailsVO> invoiceList)
    throws DAOException
  {
    logger.info("Entering Method");
    int noShipBill = 0;
    int invCount = 0;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    ArrayList xmlErrorList = null;
    try
    {
      logger.info("Insert_EDPMS_Softex_Inv_Data");
     
      commonMethods = new CommonMethods();
     
      xmlErrorList = new ArrayList();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      if (invoiceList != null) {
        for (int i = 0; i < invoiceList.size(); i++)
        {
          InvoiceDetailsVO invoiceVO = (InvoiceDetailsVO)invoiceList.get(i);
         
          String invErrorString = "";
         
          LoggableStatement pst2 = new LoggableStatement(con,
            "SELECT COUNT(*) AS COUNT FROM ETT_EDPMS_SHP_INV_SOFTEX WHERE SHIPBILLDATE =? AND PORTCODE=? AND FORMNO=? AND INVSERIALNO=? AND INVNO=? ");
          pst2.setString(1, shipingVO.getShippingBillDate());
          pst2.setString(2, shipingVO.getPortCode());
          pst2.setString(3, shipingVO.getFormNo());
          pst2.setString(4, invoiceVO.getInvoiceSerialNo());
          pst2.setString(5, invoiceVO.getInvoiceNo());
          ResultSet rs = pst2.executeQuery();
          int k = 0;
          if (rs.next()) {
            k = rs.getInt("COUNT");
          }
          closeStatementResultSet(pst2, rs);
          if (k == 0)
          {
            if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice Serial No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "Invoice No is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobCurr())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Currency is Empty");
            }
            if (commonMethods.isNull(invoiceVO.getFobAmt())) {
              invErrorString = commonMethods.setErrorString(invErrorString, "FOB Amount is Empty");
            }
            if (commonMethods.isNull(invErrorString))
            {
              LoggableStatement pst1 = new LoggableStatement(con,
                "INSERT INTO ETT_EDPMS_SHP_INV_SOFTEX(SHIPBILLDATE,PORTCODE,FORMNO,FILENO,INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE,INSAMT,COMMCURRCODE,COMMAMT,DISCURRCODE,DISAMT,DEDCURRCODE,DEDAMT,PKGCURRCODE,PKGAMT) values(?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
              pst1.setString(1, shipingVO.getShippingBillDate());
              pst1.setString(2, shipingVO.getPortCode());
              pst1.setString(3, shipingVO.getFormNo());
              pst1.setInt(4, shipingVO.getFileNo());
              pst1.setString(5, invoiceVO.getInvoiceSerialNo());
              pst1.setString(6, invoiceVO.getInvoiceNo());
              pst1.setString(7, invoiceVO.getInvoiceDate());
              pst1.setString(8, invoiceVO.getFobCurr());
              pst1.setString(9, invoiceVO.getFobAmt());
              pst1.setString(10, invoiceVO.getFreightCurr());
              pst1.setString(11, invoiceVO.getFreightAmt());
              pst1.setString(12, invoiceVO.getInsuranceCurr());
              pst1.setString(13, invoiceVO.getInsuranceAmt());
              pst1.setString(14, invoiceVO.getCommissionCurr());
              pst1.setString(15, invoiceVO.getCommissionAmt());
              pst1.setString(16, invoiceVO.getDiscountCurr());
              pst1.setString(17, invoiceVO.getDiscountAmt());
              pst1.setString(18, invoiceVO.getDeductionCurr());
              pst1.setString(19, invoiceVO.getDeductionAmt());
              pst1.setString(20, invoiceVO.getPackagingCurr());
              pst1.setString(21, invoiceVO.getPackagingAmt());
              int iCount = pst1.executeUpdate();
              invCount += iCount;
              this.invoicecount = invCount;
             
              pst1.close();
            }
            else
            {
              excelDataVO = new ExcelDataVO();
              excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
              excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
              excelDataVO.setPortCode(shipingVO.getPortCode());
              excelDataVO.setFormNo(shipingVO.getFormNo());
              excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
              excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
              excelDataVO.setShpErrorString(invErrorString);
              excelDataVO.setFileNo(shipingVO.getFileNo());
             

              xmlErrorList.add(excelDataVO);
            }
          }
          else
          {
            excelDataVO = new ExcelDataVO();
           
            invErrorString = commonMethods.setErrorString(invErrorString, "Duplicate Data");
           
            excelDataVO.setShipBillNo(shipingVO.getShippingBillNo());
            excelDataVO.setShipBillDate(shipingVO.getShippingBillDate());
            excelDataVO.setPortCode(shipingVO.getPortCode());
            excelDataVO.setFormNo(shipingVO.getFormNo());
            excelDataVO.setInvSerialNo(invoiceVO.getInvoiceSerialNo());
            excelDataVO.setInvNo(invoiceVO.getInvoiceNo());
            excelDataVO.setShpErrorString(invErrorString);
            excelDataVO.setFileNo(shipingVO.getFileNo());
           

            xmlErrorList.add(excelDataVO);
          }
        }
      }
      if (invCount > 0)
      {
        noShipBill = 1;
        shipingVO.setNoShipBillNo(noShipBill);
      }
      shipingVO.setXmlErrorList(xmlErrorList);
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return shipingVO;
  }
 
  public XMLFileVO getErrorListDownload(XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("----------getErrorListDownload-------------");
    int iCnt = 0;
    String fileName1 = null;
    String sQuery = null;
    ArrayList<Object> xmlErrorList = null;
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet rs = null;
   
    CommonMethods commonMethods = null;
    try
    {
      xmlErrorList = new ArrayList();
      commonMethods = new CommonMethods();
     
      con = DBConnectionUtility.getConnection();
      fileName1 = xmlFileVO.getFileName1();
      logger.info("The file name is : " + fileName1);
     
      String sExt = fileName1.replaceAll("[^a-z]", "");
      if (((sExt != null) && (sExt.equalsIgnoreCase("passrodack"))) || (sExt.equalsIgnoreCase("failrodack")))
      {
        iCnt = checkFileExistanceROD(fileName1);
       

        commonMethods = new CommonMethods();
        con = DBConnectionUtility.getConnection();
        if (!commonMethods.isNull(fileName1))
        {
          logger.info("The file name is ---------------- " + fileName1);
         

          String sProcName = "SELECT DISTINCT FL.FILE_NO AS FILE_NO, FL.FILE_NAME AS FILENAME FROM ETT_ROD_ACK_FILES FL ,ETT_SHP_ROD_ACK_STG STG  WHERE TRIM(FL.FILE_NO) =TRIM(STG.FILE_NO) AND TRIM(FL.FILE_NAME) =? ";
         


          LoggableStatement lst1 = new LoggableStatement(con, sProcName);
          lst1.setString(1, fileName1);
          ResultSet rs1 = lst1.executeQuery();
          while (rs1.next())
          {
            fileName1 = String.valueOf(rs1.getInt("FILE_NO"));
           
            logger.info("File Name---------------- " + rs1.getInt("FILE_NO"));
            if (fileName1 != null) {
              iCnt = 1024;
            }
          }
          DBConnectionUtility.surrenderDB(null, lst1, rs1);
        }
      }
      else if (!sExt.equalsIgnoreCase("passprnack"))
      {
        iCnt = checkFileExistance(fileName1);
      }
      else
      {
        iCnt = checkPRNFileExistance(fileName1);
      }
      logger.info("The file count is : Error Download method : " + iCnt);
     
      logger.info("File Name---Extension--------------- " + sExt);
      logger.info("File Name---------------- " + fileName1);
     
      xmlFileVO.setFileCount(iCnt);
      if ((iCnt == 2) && (!sExt.equalsIgnoreCase("passprnack")))
      {
        sQuery = "SELECT BILLNO, BILLDATE, PCODE, FORMNO, INVSERNO, INVNO, SHP||','||INV ERROR_CODE FROM(SELECT TMP.SHIPBILLNO BILLNO, TMP.SHIPBILLDATE BILLDATE, TMP.PORTCODE PCODE, TMP.FORMNO FORMNO, TMP.INVSERIALNO INVSERNO, TMP.INVNO INVNO, TMP.SHP_ERROR_MSG SHP, TMP.INV_ERROR_MSG INV FROM ETT_EDPMS_ERR_TMP_TBL TMP, ETT_EDPMS_FILES F WHERE TMP.FILENO   = F.FILENO AND F.FILENAME     = ? AND SHP_ERROR_MSG IS NOT NULL )";
      }
      else if (((iCnt == 1024) && (sExt != null) && (sExt.equalsIgnoreCase("passrodack"))) || (sExt.equalsIgnoreCase("failrodack")))
      {
        logger.info("-----------ROD------------- ");
        sQuery = "SELECT  STG.SHIPPINGBILLNO AS BILLNO ,STG.SHIPPINGBILLDATE AS BILLDATE,STG.PORTCODE  AS PCODE ,STG.FORMNO AS FORMNO,  '' AS INVSERNO,'' AS INVNO, ERRORCODES  AS ERROR_CODE FROM ETT_SHP_ROD_ACK_STG STG,ETT_ROD_ACK_FILES FL  WHERE  TRIM(FL.FILE_NO) =TRIM(STG.FILE_NO)  and TRIM(STG.FILE_NO)=?";
      }
      else
      {
        sQuery = "SELECT BILLNO, BILLDATE, PCODE, FORMNO, INVSERNO, INVNO, SHP ||',' ||INV ERROR_CODE FROM(SELECT TMP.SHIPBILLNO BILLNO, TMP.SHIPBILLDATE BILLDATE, TMP.PORTCODE PCODE, TMP.FORMNO FORMNO, TMP.INVSERIALNO INVSERNO, TMP.INVNO INVNO,TMP.SHP_ERROR_MSG SHP,TMP.INV_ERROR_MSG INV FROM ETT_EDPMS_ERR_TMP_TBL TMP,   ETT_PRN_ACK_FILES F WHERE TMP.FILENO   = F.FILE_NO AND F.FILE_NAME     = ? AND SHP_ERROR_MSG IS NOT NULL)";
      }
      ps = new LoggableStatement(con, sQuery);
      ps.setString(1, fileName1);
     
      logger.info("SELECT_ERROR_LIS QUery :---------- " + ps.getQueryString());
      rs = ps.executeQuery();
     

      String datas = null;
      while (rs.next())
      {
        ExcelDataVO excelDataVO = new ExcelDataVO();
        excelDataVO.setShipBillNo(rs.getString("BILLNO"));
        datas = rs.getString("BILLNO");
        logger.info("Error Bill -------- " + rs.getString("BILLNO"));
        excelDataVO.setShipBillDate(rs.getString("BILLDATE"));
        excelDataVO.setPortCode(rs.getString("PCODE"));
        excelDataVO.setFormNo(rs.getString("FORMNO"));
        excelDataVO.setInvSerialNo(rs.getString("INVSERNO"));
        excelDataVO.setInvNo(rs.getString("INVNO"));
        excelDataVO.setShpErrorString(rs.getString("ERROR_CODE"));
        xmlErrorList.add(excelDataVO);
       
        xmlFileVO.setXmlErrorList(xmlErrorList);
      }
      if (datas == null)
      {
        logger.info("Error List % NOt % Added------- ");
        return xmlFileVO;
      }
    }
    catch (Exception exception)
    {
      logger.info("----------getErrorListDownload------- Exception------" + exception);
     
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    DBConnectionUtility.surrenderDB(con, ps, rs);
   

    logger.info("Exiting Method");
    return xmlFileVO;
  }
 
  public XMLFileVO getXMLFileList(XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("----------------------getXMLFileList----------");
    ArrayList<XMLFileVO> fileList = null;
    String sQuery = "";
    String sCountQuery = null;
    String fileName1 = null;
    int iCnt = 0;
   
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet rs = null;
   
    CommonMethods commonMethods = null;
    String fileName = null;
    try
    {
      commonMethods = new CommonMethods();
      fileList = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      fileName1 = xmlFileVO.getFileName1();
     
      String sExt = fileName1.replaceAll("[^a-z]", "");
      if (!sExt.equalsIgnoreCase("passprnack"))
      {
        sCountQuery = "SELECT COUNT(1) AS CNT FROM ETT_EDPMS_FILES WHERE FILENAME LIKE '%'||?||'%'";
        fileName = fileName1;
      }
      else
      {
        sCountQuery = "SELECT COUNT(1) AS CNT FROM ETT_PRN_ACK_FILES WHERE FILE_NAME LIKE '%'||?||'%'";
        fileName = fileName1;
      }
      ps = new LoggableStatement(con, sCountQuery);
      ps.setString(1, fileName);
      logger.info("----------------------getXMLFileList---ETT_EDPMS_FILES- qUERY------" + ps.getQueryString());
     
      rs = ps.executeQuery();
      if (rs.next())
      {
        iCnt = rs.getInt("CNT");
       
        logger.info("----------------------getXMLFileList---ETT_EDPMS_FILES- qUERY- iCnt Count Value-----" + iCnt);
      }
      logger.info("-------------fileName1------" + sExt);
      if (((iCnt == 0) && (sExt != null) && (sExt.equalsIgnoreCase("passrodack"))) || (sExt.equalsIgnoreCase("failrodack")))
      {
        logger.info("-------------fileName1---99999999---");
       

        sQuery = "SELECT  DISTINCT FL.FILE_NO AS FILENO, FL.FILE_NAME AS FILENAME FROM ETT_ROD_ACK_FILES FL ,ETT_SHP_ROD_ACK_STG STG WHERE TRIM(FL.FILE_NO) =TRIM(STG.FILE_NO) AND TRIM(FILE_NAME) =?";
       
        fileName = fileName1;
      }
      else if (iCnt != 0)
      {
        if (!sExt.equalsIgnoreCase("passprnack"))
        {
          sQuery = "SELECT FILENO, FILENAME FROM ETT_EDPMS_FILES WHERE FILENAME LIKE '%'||?||'%' ORDER BY FILENAME ASC";
          fileName = fileName1;
        }
        else
        {
          sQuery = "SELECT FILE_NO AS FILENO, FILE_NAME AS FILENAME FROM ETT_PRN_ACK_FILES WHERE FILE_NAME LIKE '%'||?||'%' ORDER BY FILE_NAME ASC";
          fileName = fileName1;
        }
      }
      LoggableStatement ps1 = new LoggableStatement(con, sQuery);
      ps1.setString(1, fileName);
      logger.info("----------------------getXMLFileList---ETT_EDPMS_FILES- Name and Number Query---" + ps1.getQueryString());
     
      ResultSet rs1 = ps1.executeQuery();
      while (rs1.next())
      {
        logger.info("@@@@@@@@@@@@---------Results-----------!!!!!!!!");
        XMLFileVO xmlFileVO1 = new XMLFileVO();
        xmlFileVO1.setFileNumber(rs1.getInt("FILENO"));
       
        iCnt = rs1.getInt("FILENO");
       
        logger.info("----------------------getXMLFileList---ETT_EDPMS_FILES- Name and Number Query FILENO---" + rs1.getInt("FILENO"));
        xmlFileVO1.setFileName1(rs1.getString("FILENAME"));
        logger.info("----------------------getXMLFileList---ETT_EDPMS_FILES- Name and Number Query FILENAME---" + rs1.getString("FILENAME"));
        fileList.add(xmlFileVO1);
      }
      xmlFileVO.setFileList(fileList);
     
      ps1.close();
     


      xmlFileVO.setFileCount(iCnt);
    }
    catch (Exception exception)
    {
      logger.info("----------------------getXMLFileList---exception-------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
   
    return xmlFileVO;
  }
 
  public int checkFileExistance(String fileName)
    throws DAOException
  {
    logger.info("Entering Method");
    int iRet = 0;
    String sProcName = null;
    CallableStatement cs = null;
    Connection con = null;
   
    CommonMethods commonMethods = null;
    try
    {
      logger.info("This is Inside of Check method : " + fileName);
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (!commonMethods.isNull(fileName))
      {
        sProcName = "{call ETT_GET_EDPMS_FILE_COUNT(?, ?)}";
        cs = con.prepareCall(sProcName);
       
        cs.setString(1, commonMethods.getEmptyIfNull(fileName).trim());
        cs.registerOutParameter(2, 4);
        if (cs.executeUpdate() >= 0)
        {
          iRet = cs.getInt(2);
          logger.info("This procedure return value is : " + iRet);
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return iRet;
  }
 
  public int checkFileExistanceROD(String fileName)
    throws DAOException
  {
    logger.info("ROD ---------Ack File Status-------------");
    int iRet = 0;
    String sProcName = null;
    CallableStatement cs = null;
    Connection con = null;
   
    CommonMethods commonMethods = null;
    try
    {
      logger.info("This is Inside of Check method : " + fileName);
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (!commonMethods.isNull(fileName))
      {
        sProcName = "SELECT FILE_NO, FILE_NAME AS FILENAME FROM ETT_ROD_ACK_FILES WHERE FILE_NAME =?";
       

        LoggableStatement lst1 = new LoggableStatement(con, sProcName);
        lst1.setString(1, fileName);
        ResultSet rs1 = lst1.executeQuery();
        while (rs1.next())
        {
          String a = rs1.getString("FILENAME");
          if (a != null) {
            iRet = 2;
          }
        }
        DBConnectionUtility.surrenderDB(con, lst1, rs1);
      }
    }
    catch (Exception exception)
    {
      logger.info("ROD ---------Ack File Status-exception------------" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return iRet;
  }
 
  public int checkPRNFileExistance(String fileName)
    throws DAOException
  {
    logger.info("Entering Method");
    int iRet = 0;
    String sProcName = null;
    CallableStatement cs = null;
    Connection con = null;
   
    CommonMethods commonMethods = null;
    try
    {
      logger.info("This is Inside of Check method : " + fileName);
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (!commonMethods.isNull(fileName))
      {
        sProcName = "{call ETT_GET_EDPMS_PRN_FILE_COUNT(?, ?)}";
        cs = con.prepareCall(sProcName);
       
        cs.setString(1, commonMethods.getEmptyIfNull(fileName).trim());
        cs.registerOutParameter(2, 4);
        if (cs.executeUpdate() >= 0)
        {
          iRet = cs.getInt(2);
          logger.info("This procedure return value is : " + iRet);
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return iRet;
  }
 
  public int insert_EDPMS_TMP_Table(Connection con, ArrayList<ExcelDataVO> alExcelDataVO)
    throws DAOException
  {
    logger.info("Entering Method");
    int iRet = 0;
    String sQuery = null;
   

    LoggableStatement ps = null;
    LoggableStatement ps1 = null;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      sQuery = "INSERT INTO ETT_EDPMS_ERR_TMP_TBL(SHIPBILLNO, SHIPBILLDATE, PORTCODE, FORMNO, INVNO, INVSERIALNO, SHP_ERROR_MSG, INV_ERROR_MSG, FILENO) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      logger.info("The insert query is : " + sQuery);
     
      logger.info("The insert query is : ArrayList : " + alExcelDataVO.size());
      logger.info("excel data size" + alExcelDataVO.size());
      for (int i = 0; i < alExcelDataVO.size(); i++)
      {
        excelDataVO = (ExcelDataVO)alExcelDataVO.get(i);
        ps = new LoggableStatement(con, sQuery);
       
        ps.setString(1, excelDataVO.getShipBillNo());
        ps.setString(2, excelDataVO.getShipBillDate());
        ps.setString(3, excelDataVO.getPortCode());
        ps.setString(4, excelDataVO.getFormNo());
        ps.setString(5, excelDataVO.getInvNo());
        ps.setString(6, excelDataVO.getInvSerialNo());
        ps.setString(7, excelDataVO.getShpErrorString());
        ps.setString(8, excelDataVO.getInvErrorString());
        ps.setInt(9, excelDataVO.getFileNo());
        logger.info("shipping bill no" + excelDataVO.getShipBillNo());
        int insertRet = ps.executeUpdate();
        ps.close();
      }
      String sCountDeletedQuery = "SELECT SUM(BILLCOUNT+FORMCOUNT) AS CNT FROM (SELECT COUNT(DISTINCT SHIPBILLNO) AS BILLCOUNT, 0 AS FORMCOUNT FROM ETT_EDPMS_ERR_TMP_TBL WHERE FILENO = ? UNION SELECT 0 AS BILLCOUNT, COUNT(DISTINCT FORMNO) AS FORMCOUNT FROM ETT_EDPMS_ERR_TMP_TBL WHERE FILENO = ?)";
      ps1 = new LoggableStatement(con, sCountDeletedQuery);
      ps1.setInt(1, excelDataVO.getFileNo());
      ps1.setInt(2, excelDataVO.getFileNo());
      ResultSet rs = ps1.executeQuery();
      if (rs.next()) {
        iRet = rs.getInt(1);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
    return iRet;
  }
 
  public ShippingDetailsVO compareBOECount(ShippingDetailsVO shippingVO1)
  {
    logger.info("Entering Method");
   
    int iHeaderSHPCount = 0;
    int iHeaderINVCount = 0;
    int iContentSHPCount = 0;
    int iContentINVCount = 0;
    String errMsg = null;
   
    iHeaderSHPCount = shippingVO1.getHeaderShpCount();
    iHeaderINVCount = shippingVO1.getHeaderInvCount();
    iContentSHPCount = shippingVO1.getContentShpCount();
    iContentINVCount = shippingVO1.getContentInvCount();
   
    logger.info("SHP : " + iHeaderSHPCount + " : INV : " + iHeaderINVCount + " : CSHP : " + iContentSHPCount +
      " : CINV : " + iContentINVCount);
    if (iHeaderSHPCount == iContentSHPCount)
    {
      if (iHeaderINVCount == iContentINVCount) {
        shippingVO1.setCountResult("S");
      } else {
        shippingVO1.setCountResult("INV");
      }
    }
    else
    {
      if (iHeaderINVCount == iContentINVCount) {
        errMsg = "SHP";
      } else {
        errMsg = "BOTH";
      }
      shippingVO1.setCountResult(errMsg);
    }
    logger.info("Exiting Method");
    return shippingVO1;
  }
 
  public XMLFileVO validateMDFXMLTags(File xmlFileUpload, String fileName)
  {
    logger.info("Entering Method");
    try
    {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
     
      DefaultHandler handler = new XMLFileDAO.1(this);
     































      saxParser.parse(xmlFileUpload, handler);
      this.xmlFileVO1.setStatusRes("true");
    }
    catch (Exception e)
    {
      logger.info("This is Error Page : " + this.xmlFileVO1.getTagName());
      this.xmlFileVO1.setStatusRes("false");
      e.printStackTrace();
      return this.xmlFileVO1;
    }
    logger.info("Exiting Method");
    return this.xmlFileVO1;
  }
 
  public int rollbackMDFFileDetails(Connection con, ShippingDetailsVO shippingVO, String sFileType)
    throws DAOException
  {
    logger.info("Entering Method");
    int iRet = 0;
   
    String sProcName = null;
    CallableStatement cs = null;
    try
    {
      sProcName = "{call ROLLBACK_MDF_FILES(?, ?)}";
      cs = con.prepareCall(sProcName);
     
      cs.setInt(1, shippingVO.getFileNo());
      cs.setString(2, sFileType);
      if (cs.executeUpdate() >= 0)
      {
        iRet = 1;
        logger.info("This procedure return value is : MDF : " + iRet);
        return iRet;
      }
    }
    catch (Exception ex)
    {
      throwDAOException(ex);
     
      logger.info("Exiting Method");
    }
    return iRet;
  }
 
  public XMLFileVO comparePRNCount(XMLFileVO xmlFileVO1)
  {
    logger.info("Entering Method");
   
    int iHeaderSHPCount = 0;
    int iHeaderINVCount = 0;
    int iContentSHPCount = 0;
    int iContentINVCount = 0;
    String errMsg = null;
   
    iHeaderSHPCount = xmlFileVO1.getHeaderShpCount();
    iHeaderINVCount = xmlFileVO1.getHeaderInvCount();
    iContentSHPCount = xmlFileVO1.getContentShpCount();
    iContentINVCount = xmlFileVO1.getContentInvCount();
   
    logger.info("SHP : " + iHeaderSHPCount + " : INV : " + iHeaderINVCount + " : CSHP : " + iContentSHPCount +
      " : CINV : " + iContentINVCount);
    if (iHeaderSHPCount == iContentSHPCount)
    {
      if (iHeaderINVCount == iContentINVCount) {
        xmlFileVO1.setCountResult("S");
      } else {
        xmlFileVO1.setCountResult("INV");
      }
    }
    else
    {
      if (iHeaderINVCount == iContentINVCount) {
        errMsg = "SHP";
      } else {
        errMsg = "BOTH";
      }
      xmlFileVO1.setCountResult(errMsg);
    }
    logger.info("Exiting Method");
    return xmlFileVO1;
  }
 
  public int rollbackACKFileDetails(Connection con, XMLFileVO xmlFileVO, String sFileType)
    throws DAOException
  {
    logger.info("Entering Method");
    int iRet = 0;
   
    String sProcName = null;
    CallableStatement cs = null;
    try
    {
      sProcName = "{call ROLLBACK_MDF_FILES(?, ?)}";
      cs = con.prepareCall(sProcName);
     
      cs.setInt(1, xmlFileVO.getFileNumber());
      cs.setString(2, sFileType);
      if (cs.executeUpdate() >= 0)
      {
        iRet = 1;
        logger.info("This procedure return value is : PRN : " + iRet);
        logger.info("This procedure return value is : PRN : " + iRet);
        return iRet;
      }
    }
    catch (Exception ex)
    {
      logger.info("rollbackACKFileDetails-----------" + ex.getMessage());
      logger.info("rollbackACKFileDetails-----------" + ex.getMessage());
     
      logger.info("Exiting Method");
    }
    return iRet;
  }
 
  public boolean deleteMDFErrorRecords(Connection con, ArrayList xmlErrorList)
    throws DAOException
  {
    logger.info("Entering Method");
   
    String sShpQuery = null;
    String sInvQuery = null;
    LoggableStatement ps = null;
    LoggableStatement ps1 = null;
    LoggableStatement ps2 = null;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      excelDataVO = (ExcelDataVO)xmlErrorList.get(0);
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      sShpQuery = "DELETE FROM ETT_EDPMS_SHP WHERE SHIPBILLNO = ? AND TO_DATE(SHIPBILLDATE,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ? AND FILENO = ?";
      sInvQuery = "DELETE FROM ETT_EDPMS_SHP_INV WHERE SHIPBILLNO = ? AND TO_DATE(SHIPBILLDATE,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ? AND FILENO = ?";
     
      commonMethods = new CommonMethods();
      ps = new LoggableStatement(con, sShpQuery);
      logger.info("getShipBillNo---------------" + excelDataVO.getShipBillNo());
      logger.info("getShipBillDate-------------" + excelDataVO.getShipBillDate());
      logger.info("getPortCode-----------------" + excelDataVO.getPortCode());
      logger.info("getFileNo-------------------" + excelDataVO.getFileNo());
     
      logger.info("getShipBillNo---------------" + excelDataVO.getShipBillNo());
      logger.info("getShipBillDate-------------" + excelDataVO.getShipBillDate());
      logger.info("getPortCode-----------------" + excelDataVO.getPortCode());
      logger.info("getFileNo-------------------" + excelDataVO.getFileNo());
      try
      {
        ps.setString(1, excelDataVO.getShipBillNo());
        ps.setString(2, excelDataVO.getShipBillDate());
        ps.setString(3, excelDataVO.getPortCode());
        ps.setInt(4, excelDataVO.getFileNo());
        ps.executeUpdate();
       
        ps.close();
      }
      catch (Exception e)
      {
        logger.info("Exception-------------ae---------------" + e);
        logger.info("Exception--------------ae---------------" + e);
      }
      try
      {
        ps1 = new LoggableStatement(con, sInvQuery);
       
        ps1.setString(1, excelDataVO.getShipBillNo());
        ps1.setString(2, excelDataVO.getShipBillDate());
        ps1.setString(3, excelDataVO.getPortCode());
        ps1.setInt(4, excelDataVO.getFileNo());
        ps1.executeUpdate();
      }
      catch (Exception e)
      {
        logger.info("Exception--------------be---------------" + e);
        logger.info("Exception--------------be---------------" + e);
      }
      logger.info("Exiting Method");
    }
    catch (Exception ex)
    {
      logger.info("deleteMDFErrorRecords--------------------" + ex.getMessage());
      logger.info("deleteMDFErrorRecords--------------------" + ex.getMessage());
      throwDAOException(ex);
    }
    return false;
  }
 
  public boolean deleteSOFTEXErrorRecords(Connection con, ArrayList xmlErrorList)
    throws DAOException
  {
    logger.info("Entering Method");
   
    String sShpQuery = null;
    String sInvQuery = null;
    LoggableStatement ps = null;
    LoggableStatement ps1 = null;
    LoggableStatement ps2 = null;
    CommonMethods commonMethods = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      excelDataVO = (ExcelDataVO)xmlErrorList.get(0);
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      sShpQuery = "DELETE FROM ETT_EDPMS_SHP_SOFTEX WHERE TO_DATE(SHIPBILLDATE,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ? AND FORMNO = ? AND FILENO = ?";
      sInvQuery = "DELETE FROM ETT_EDPMS_SHP_INV_SOFTEX WHERE TO_DATE(SHIPBILLDATE,'DD/MM/YYYY') = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE=? AND FORMNO=? AND FILENO = ?";
     
      commonMethods = new CommonMethods();
      ps = new LoggableStatement(con, sShpQuery);
     
      ps.setString(1, excelDataVO.getShipBillDate());
      ps.setString(2, excelDataVO.getPortCode());
      ps.setString(3, excelDataVO.getFormNo());
      ps.setInt(4, excelDataVO.getFileNo());
      ps.executeUpdate();
     
      ps.close();
     
      ps1 = new LoggableStatement(con, sInvQuery);
     
      ps1.setString(1, excelDataVO.getShipBillDate());
      ps1.setString(2, excelDataVO.getPortCode());
      ps1.setString(3, excelDataVO.getFormNo());
      ps1.setInt(4, excelDataVO.getFileNo());
      ps1.executeUpdate();
    }
    catch (Exception ex)
    {
      throwDAOException(ex);
    }
    logger.info("Exiting Method");
    return false;
  }
}