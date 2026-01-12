package in.co.localization.dao.localization;
 
import com.opensymphony.xwork2.ActionContext;
import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.ApplicationException;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.vo.localization.BOEDetailsVO;
import in.co.localization.vo.localization.ExcelDataVO;
import in.co.localization.vo.localization.OBEInvoiceDetailsVO;
import in.co.localization.vo.localization.XMLFileVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class XMLFileDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static XMLFileDAO dao;
  private static Logger logger = Logger.getLogger(XMLFileDAO.class.getName());
  XMLFileVO xmlFileVO1;
  public static XMLFileDAO getDAO()
  {
    if (dao == null) {
      dao = new XMLFileDAO();
    }
    return dao;
  }
  
  public XMLFileVO readXMLFileData(XMLFileVO xmlFileVO)
    throws DAOException
  {
    logger.info("The Control getting inside of readXMLFileData()");
    File xmlFileUpload = null;
    String xmlFileName = null;
    String xmlFileFormat = null;
    String fileName = null;
    try
    {
      xmlFileUpload = xmlFileVO.getFileUpload();
      logger.info("xmlFileUpload : " + xmlFileUpload);
      xmlFileName = xmlFileVO.getFileName();
      logger.info("xmlFileName : " + xmlFileName);
      xmlFileFormat = FilenameUtils.getExtension(xmlFileName);
      logger.info("xmlFileFormat : " + xmlFileFormat);
      logger.info("xmlFileUpload.getAbsolutePath() : ================>>> " + xmlFileUpload.getAbsolutePath());
      if (xmlFileFormat.equalsIgnoreCase("xml"))
      {
        String fileNamewithEx = xmlFileName.substring(0, xmlFileName.lastIndexOf('.'));
        logger.info("The File Extension is : fileNamewithEx : " + fileNamewithEx);
        int pos = fileNamewithEx.lastIndexOf(".");
        logger.info("The File Position is : " + pos);
        if (pos > 0) {
          fileName = fileNamewithEx.substring(0, fileNamewithEx.lastIndexOf('.'));
        }
        String tempFile = "";
        tempFile = xmlFileName.substring(0, xmlFileName.lastIndexOf('.'));
        int removedot = tempFile.indexOf(".") + 1;
        String file = tempFile.substring(removedot, tempFile.length());
        logger.info("File is------------------- " + file);
        xmlFileVO.setFilePat(file);
        int pos1 = tempFile.lastIndexOf(".");
        if (pos1 > 0) {
          fileName = tempFile.substring(0, tempFile.lastIndexOf('.'));
        } else {
          fileName = file;
        }
        fileName = fileName + file;
        logger.info("fileName--------------:" + fileName);
        if ((file.equalsIgnoreCase("obe")) || (file.equalsIgnoreCase("obback")))
        {
          xmlFileVO = xmlUploadIDPMSFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("failormAck")) || (file.equalsIgnoreCase("passormAck")))
        {
          xmlFileVO = xmlUploadIdpmsAckFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("failbesAck")) || (file.equalsIgnoreCase("passbesAck")))
        {
          logger.info("------------METHOD----------BES ACK FILE-------------");
          xmlFileVO = xmlUploadIdpmsBESAckFiles(xmlFileUpload, fileName, xmlFileVO, file);
        }
        else if ((file.equalsIgnoreCase("failbeeAck")) || (file.equalsIgnoreCase("passbeeAck")))
        {
          logger.info("------------METHOD----------BEE ACK FILE-------------");
          xmlFileVO = xmlUploadIdpmsBEEAckFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("failbeaAck")) || (file.equalsIgnoreCase("passbeaAck")))
        {
          xmlFileVO = xmlUploadIdpmsBEAAckFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if ((file.equalsIgnoreCase("passoraAck")) || (file.equalsIgnoreCase("failoraAck")))
        {
          logger.info("------------METHOD----------passoraAck FILE---------file----" + file);
          xmlFileVO = xmlUploadIdpmsORAAckFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else if (file.equalsIgnoreCase("btt"))
        {
          logger.info("------------METHOD----------BTT FILE---------file----" + file);
          xmlFileVO = xmlUploadIdpmsBTTAckFiles(xmlFileUpload, fileName, xmlFileVO);
        }
        else
        {
          xmlFileVO.setResult("INF");
          return xmlFileVO;
        }
      }
      else if (xmlFileFormat.equalsIgnoreCase("csv"))
      {
        int firstDotPlcae = xmlFileName.indexOf(".");
        String actualFileName = xmlFileName.substring(0, firstDotPlcae);
        int lastDotPlace = xmlFileName.lastIndexOf(".");
        String fileExt = xmlFileName.substring(firstDotPlcae + 1, lastDotPlace);
        logger.info("FileExt:" + fileExt);
        actualFileName = actualFileName + fileExt;
        logger.info("ActualFileName:" + actualFileName);
        xmlFileVO.setFilePat(fileExt);
        if ((fileExt.equalsIgnoreCase("passmbeAck")) || (fileExt.equalsIgnoreCase("failmbeAck")))
        {
          logger.info("----------------------------------File Format Matches(Inside First If Clause)---------------------------------");
          if (checkFile(actualFileName) == 0)
          {
            logger.info("----------------------------------File Format Matches(Inside First If Clause)---------------------------------");
            xmlFileVO = xmlUploadIdpmsMBEckFiles(xmlFileUpload, actualFileName, xmlFileVO);
          }
          else
          {
            xmlFileVO.setResult("N");
          }
        }
        else
        {
          xmlFileVO.setResult("INF");
          return xmlFileVO;
        }
      }
      else
      {
        xmlFileVO.setResult("INF");
        return xmlFileVO;
      }
    }
    catch (Exception exception)
    {
      logger.info("------readXMLFileData-----exception------------" + exception);
      exception.printStackTrace();
      throwDAOException(exception);
      logger.info("Exiting Method");
    }
    return xmlFileVO;
  }
  
  private int checkFile(String fileName)
    throws DAOException
  {
    logger.info("Inside of checkFile() ==============================>>> " + fileName);
    logger.info("Inside of checkFile() ==============================>>> " + fileName);
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, "SELECT FILENAME FROM ETT_MBE_ACK_FILES WHERE TRIM(FILENAME) = TRIM(?)");
      logger.info("-----------After Checkfilename---------");
      pst.setString(1, fileName);
      logger.info("-----------After SetString()---------" + pst);
      rs = pst.executeQuery();
      logger.info("-----------After executeQuery()---------" + rs);
      logger.info("-----------Next in RS---------" + rs.next());
      logger.info("-----------Next in RS---------" + rs.next());
      String query1 = "SELECT COUNT(1) as Excel FROM ETT_MBE_ACK_FILES WHERE TRIM(FILENAME) = TRIM(?)";
      logger.info("Count............................." + query1);
      logger.info("Count............................." + query1);
      LoggableStatement pst2 = new LoggableStatement(con, "SELECT COUNT(1) as Excel FROM ETT_MBE_ACK_FILES WHERE TRIM(FILENAME) = TRIM(?)");
      pst2.setString(1, fileName);
      ResultSet rs2 = pst2.executeQuery();
      if (rs2.next())
      {
        count = rs2.getInt(1);
        logger.info("Count............................." + count);
      }
      closeStatementResultSet(pst2, rs2);
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return count;
  }
  private XMLFileVO xmlUploadIdpmsMBEckFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)

		    throws ParserConfigurationException, SAXException, IOException, SQLException

		  {

		    logger.info("Inside of xmlUploadIdpmsMBEckFiles() ==============================>>> " + fileName);

		    logger.info("Entering Method");

		    Connection con = null;

		    LoggableStatement insertStatement = null;

		    BufferedReader br = null;

		    String line = "";

		    int a = 0;

		    String columns = "";

		    String rows = "";

		    String firstHalfQuery = "";

		    String insertQuerydup = "";

		    int mbeAckCount = 0;

		    int mbeInsertFailedCount = 0;

		    String[] temp1 = null;

		    String[] temp2 = null;

		    String[] tempBOEArr = null;

		    List<String> boe = new ArrayList();

		    List<String> inv = new ArrayList();

		    Set<String> boe1 = null;

		    Set<String> inv1 = null;

		    HttpSession session = ServletActionContext.getRequest()

		      .getSession();

		    String userId = (String)session.getAttribute("loginedUserId");

		    logger.info("Inside of xmlUploadIdpmsMBEckFiles() ==================userId============>>> " + userId);

		    try

		    {

		      con = DBConnectionUtility.getConnection();

		      br = new BufferedReader(new FileReader(xmlFileUpload));

		      int c = 0;

		      int q = 0;

		      int p = 0;

		      String invoice;

		      while ((line = br.readLine()) != null)

		      {

		        String insertQuery = null;

		        String[] country = line.split("\\|");

		 
		 
		        int n = country.length;

		 
		        logger.info("Value of n=============================== " + n);

		        if (a == 0)

		        {

		          for (int i = 0; i < n; i++) {

		            if (i != n - 1) {

		              columns = columns + country[i] + ",";

		            } else {

		              columns = columns + country[i];

		            }

		          }

		          columns = columns.replaceAll("-", "");

		          logger.info("Columns_____" + columns);

		          firstHalfQuery = "INSERT INTO ETT_MANUAL_BOE_ACK(" + columns + ") VALUES(";

		 
		          a++;

		        }

		        else

		        {

		          insertQuery = firstHalfQuery;

		          for (int j = 0; j < n; j++) {

		            if (j != n - 1)

		            {

		              insertQuery = insertQuery + "'" + country[j] + "',";

		            }

		            else

		            {

		              insertQuery = insertQuery + "'" + country[j] + "')";

		              logger.info(insertQuery);

		            }

		          }

		        }

		        logger.info("The time while loop executes====================>>>>>>>>" + c);

		        if (a != 1) {

		          try

		          {

		            insertStatement = new LoggableStatement(con, insertQuery);

		            mbeAckCount = insertStatement.executeUpdate();

		            insertStatement.close();

		          }

		          catch (SQLException se)

		          {

		            logger.info("Exception-----" + se);

		            mbeInsertFailedCount++;

		            tempBOEArr = line.split("\\|", 4);

		            logger.info("TEMP1---------BOE------------------->" + tempBOEArr[2]);

		            logger.info("TEMP1---------AD------------------->" + tempBOEArr[4]);

		            logger.info("TEMP1---------IE------------------->" + tempBOEArr[6]);

		            String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";

		            LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);

		            pst_err_query.setString(1, tempBOEArr[2]);

		            pst_err_query.setString(2, tempBOEArr[4]);

		            pst_err_query.setString(3, tempBOEArr[6]);

		            pst_err_query.setString(4, fileName);

		            pst_err_query.setString(5, se.getMessage());

		            pst_err_query.setString(6, "MBE");

		            pst_err_query.executeUpdate();

		            closePreparedStatement(pst_err_query);

		          }

		        }

		        a++;

		        insertQuerydup = insertQuery;

		 
		        temp1 = line.split("\\|", 4);

		        logger.info("TEMP1---------------------------->" + temp1[2]);

		        while (q == c)

		        {

		          q = c;

		          String billofentry = temp1[2];

		          logger.info(billofentry);

		          if (Arrays.asList(new List[] { boe }).contains(billofentry)) {

		            break;

		          }

		          boe.add(billofentry);

		          q++;

		        }

		        temp2 = line.split("\\|", 21);

		        logger.info("TEMP2---------------------------->" + temp2[19]);

		        logger.info("TEMP2---------------------------->" + temp2[19]);

		        while (p == c)

		        {

		          p = c;

		          invoice = temp2[19];

		          logger.info(invoice);

		          if (Arrays.asList(new List[] { inv }).contains(invoice)) {

		            break;

		          }

		          inv.add(invoice);

		          p++;

		        }

		        c++;

		      }

		      logger.info("TEMP1---------------------------->" + temp1[2]);

		      logger.info("BOE---------------------------->" + boe);

		      boe1 = new LinkedHashSet(boe);

		 
		      logger.info("TEMP2---------------------------->" + temp2[19]);

		      logger.info("INV---------------------------->" + inv);

		 
		 
		      logger.info(inv);

		 
		 
		      logger.info("INV-------------fileName----------------------->" + fileName);

		      logger.info("INV-------------userId----------------------->" + userId);

		      if (mbeAckCount > 0)

		      {

		        if (mbeInsertFailedCount == 0)

		        {

		          LoggableStatement ps1 = new LoggableStatement(con, "INSERT INTO ETT_MBE_ACK_FILES(FILENO,FILENAME,CREATED_BY) VALUES (MBE_ACK_SEQ.NEXTVAL,?,?)");

		          ps1.setString(1, fileName);

		          ps1.setString(2, userId);

		          ps1.executeUpdate();

		          closeStatementResultSet(ps1, null);

		          xmlFileVO.setResult("Y");

		        }

		        else

		        {

		          xmlFileVO.setErrCount(mbeInsertFailedCount);

		          xmlFileVO.setResult("PL");

		        }

		      }

		      else {

		        xmlFileVO.setResult("ACKFAIL");

		      }

		      logger.info("Firsthalf Query---------------------    " + firstHalfQuery);

		 
		 
		 
		 
		      int bcount = -1;

		      int icount = -1;

		      for (String str : boe1)

		      {

		        bcount++;

		        logger.info("BOE count  +++***********++++++++   ====  " + str);

		      }

		      for (String str : inv)

		      {

		        icount++;

		        logger.info("INVOICE count  +++***********++++++++   ====  " + str);

		      }

		      xmlFileVO.setBoeCount(bcount);

		      XMLFileVO.setInvCount(icount);

		    }

		    catch (Exception e)

		    {

		      logger.info("----xmlUploadIdpmsMBEckFiles-------Exception1--------    " + e);

		      e.printStackTrace();

		      try

		      {

		        br.close();

		        insertStatement.close();

		        con.close();

		      }

		      catch (Exception e)

		      {

		        e.printStackTrace();

		      }

		    }

		    finally

		    {

		      try

		      {

		        br.close();

		        insertStatement.close();

		        con.close();

		      }

		      catch (Exception e)

		      {

		        e.printStackTrace();

		      }

		    }

		    logger.info("Exiting Method");

		    return xmlFileVO;

		  }
  
  public XMLFileVO xmlUploadIdpmsBEAAckFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)

		    throws DAOException

		  {

		    logger.info("Inside of xmlUploadIdpmsBEAAckFiles() ==============================>>> " + fileName);

		    logger.info("Entering Method");

		    Connection con = null;

		    int beaInsertCount = 0;

		    int beaInsertFailedCount = 0;

		    CommonMethods commonMethods = null;

		    try

		    {

		      con = DBConnectionUtility.getConnection();

		      commonMethods = new CommonMethods();

		      HttpSession session = ServletActionContext.getRequest().getSession();

		      String userId = (String)session.getAttribute("loginedUserId");

		      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		      Document doc = docBuilder.parse(xmlFileUpload);

		      doc.getDocumentElement().normalize();

		      LoggableStatement pst = new LoggableStatement(con, "SELECT FILENAME FROM ETT_BEA_ACK_FILES WHERE TRIM(FILENAME) = TRIM(?)");

		      pst.setString(1, fileName);

		      ResultSet rs = pst.executeQuery();

		      if (rs.next())

		      {

		        xmlFileVO.setResult("N");

		      }

		      else

		      {

		        NodeList nList = doc.getElementsByTagName("billOfEntry");

		        for (int temp = 0; temp < nList.getLength(); temp++)

		        {

		          Node nNode = nList.item(temp);

		          if (nNode.getNodeType() == 1)

		          {

		            Element eElement = (Element)nNode;

		            String portOfDischarge = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("portOfDischarge").item(0).getTextContent());

		            String billOfEntryNumber = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("billOfEntryNumber").item(0).getTextContent()).trim();

		            String billOfEntryDate = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("billOfEntryDate").item(0).getTextContent()).trim();

		            String ieCode = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("IECode").item(0).getTextContent()).trim();

		            String adCode = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("ADCode").item(0).getTextContent()).trim();

		            String recordIndicator = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("recordIndicator").item(0).getTextContent()).trim();

		            String adjNo = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("adjustmentReferenceNumber").item(0).getTextContent()).trim();

		            String closeInd = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("closeofBillIndicator").item(0).getTextContent()).trim();

		            String adjDate = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("adjustmentDate").item(0).getTextContent()).trim();

		            String adjInd = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("adjustmentIndicator").item(0).getTextContent()).trim();

		            String docNo = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("documentNumber").item(0).getTextContent()).trim();

		            String docDate = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("documentDate").item(0).getTextContent()).trim();

		            String docPort = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("documentPort").item(0).getTextContent()).trim();

		            String apporvedBy = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("approvedBy").item(0).getTextContent()).trim();

		            String letterNo = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("letterNumber").item(0).getTextContent()).trim();

		            String letterDate = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("letterDate").item(0).getTextContent()).trim();

		            String remarks = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("Remark").item(0).getTextContent()).trim();

		            String errorCode = commonMethods.getEmptyIfNull(eElement

		              .getElementsByTagName("errorCode").item(0).getTextContent()).trim();

		            NodeList nList2 = eElement.getElementsByTagName("invoice");

		            for (int temp1 = 0; temp1 < nList2.getLength(); temp1++)

		            {

		              Node nNode1 = nList2.item(temp1);

		              if (nNode1.getNodeType() == 1)

		              {

		                Element eElement1 = (Element)nNode1;

		                String inSno = commonMethods.getEmptyIfNull(eElement1

		                  .getElementsByTagName("invoiceSerialNo").item(0).getTextContent()).trim();

		                String inNo = commonMethods.getEmptyIfNull(eElement1

		                  .getElementsByTagName("invoiceNo").item(0).getTextContent()).trim();

		                String adjInvoiceIC = commonMethods.getEmptyIfNull(eElement1

		                  .getElementsByTagName("adjustedInvoiceValueIC").item(0).getTextContent()).trim();

		                String invErrorCode = commonMethods.getEmptyIfNull(eElement1

		                  .getElementsByTagName("errorCode").item(0).getTextContent()).trim();

		                String xmlErrorCode = "";

		                if ((errorCode.equalsIgnoreCase("SUCCESS")) && (

		                  (commonMethods.isNull(invErrorCode)) || (invErrorCode.equalsIgnoreCase("SUCCESS")))) {

		                  xmlErrorCode = "SUCCESS";

		                } else {

		                  xmlErrorCode = errorCode + invErrorCode;

		                }

		                try

		                {

		                  int beaCount = 0;

		                  String query = "SELECT COUNT(*) AS BEA_COUNT FROM ETT_BEA_ACK WHERE ADJ_NO =? AND RECORDINDICATOR = ? AND ERRORCODES  = 'SUCCESS' ";

		 
		                  LoggableStatement pst2 = new LoggableStatement(con, query);

		                  pst2.setString(1, adjNo);

		                  pst2.setString(2, recordIndicator);

		                  ResultSet rs2 = pst2.executeQuery();

		                  if (rs2.next()) {

		                    beaCount = rs2.getInt("BEA_COUNT");

		                  }

		                  closeStatementResultSet(pst2, rs2);

		                  if (beaCount == 0)

		                  {

		                    try

		                    {

		                      LoggableStatement loggableStatement = new LoggableStatement(con, "INSERT INTO ETT_BEA_ACK (BOENUMBER,BOEDATE,PORTOFDISCHARGE,IECODE,ADCODE,RECORDINDICATOR,ADJ_NO,CLOSE_IND,ADJ_DATE,ADJ_IND,DOCUMENT_NO,DOCUMENT_DATE,DOCUMENT_PORT,APPROVEDBY,LETTERNO,LETTERDATE,REMARKS,INV_SNO,INV_NO,ADJ_INVAMT,ERRORCODES,FILENAME) VALUES (?,TO_DATE(?,'DD/MM/YY'),?,?,?,?,?,?,TO_DATE(?,'DD/MM/YY'),?,?,TO_DATE(?,'DD/MM/YY'),?,?,?,TO_DATE(?,'DD/MM/YY'),?,?,?,?,?,?)");

		                      loggableStatement.setString(1, billOfEntryNumber);

		                      loggableStatement.setString(2, billOfEntryDate);

		                      loggableStatement.setString(3, portOfDischarge);

		                      loggableStatement.setString(4, ieCode);

		                      loggableStatement.setString(5, adCode);

		                      loggableStatement.setString(6, recordIndicator);

		                      loggableStatement.setString(7, adjNo);

		                      loggableStatement.setString(8, closeInd);

		                      loggableStatement.setString(9, adjDate);

		                      loggableStatement.setString(10, adjInd);

		                      loggableStatement.setString(11, docNo);

		                      loggableStatement.setString(12, docDate);

		                      loggableStatement.setString(13, docPort);

		                      loggableStatement.setString(14, apporvedBy);

		                      loggableStatement.setString(15, letterNo);

		                      loggableStatement.setString(16, letterDate);

		                      loggableStatement.setString(17, remarks);

		                      loggableStatement.setString(18, inSno);

		                      loggableStatement.setString(19, inNo);

		                      loggableStatement.setString(20, adjInvoiceIC);

		                      loggableStatement.setString(21, xmlErrorCode);

		                      loggableStatement.setString(22, fileName);

		                      beaInsertCount += loggableStatement.executeUpdate();

		                      loggableStatement.close();

		                    }
		                    catch (SQLException se)
		                    {
		                      logger.info("------Exception------" + se.getMessage());
		                      beaInsertFailedCount++;
		                      String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                      LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                      pst_err_query.setString(1, billOfEntryNumber);
		                      pst_err_query.setString(2, adCode);
		                      pst_err_query.setString(3, ieCode);
		                      pst_err_query.setString(4, fileName);
		                      pst_err_query.setString(5, se.getMessage());
		                      pst_err_query.setString(6, "BEA");
		                      pst_err_query.executeUpdate();
		                      closePreparedStatement(pst_err_query);
		                    }
		                  }
		                  else
		                  {
		                    beaInsertFailedCount++;
		                    String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                    LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                    pst_err_query.setString(1, billOfEntryNumber);
		                    pst_err_query.setString(2, adCode);
		                    pst_err_query.setString(3, ieCode);
		                    pst_err_query.setString(4, fileName);
		                    pst_err_query.setString(5, "Adjustment Number & RecordIndicator already Exist");
		                    pst_err_query.setString(6, "BEA");
		                    pst_err_query.executeUpdate();
		                    closePreparedStatement(pst_err_query);
		                  }
		                }
		                catch (Exception e)
		                {
		                  e.printStackTrace();
		                }
		              }
		            }
		          }
		        }
		        xmlFileVO.setInsertCount(beaInsertCount);
		        if (beaInsertCount > 0)
		        {
		          if (beaInsertFailedCount == 0)
		          {
		            LoggableStatement ps1 = new LoggableStatement(con, "INSERT INTO ETT_BEA_ACK_FILES(FILENO,FILENAME,CREATED_BY) VALUES (BEA_ACK_SEQ.NEXTVAL,?,?)");
		            ps1.setString(1, fileName);
		            ps1.setString(2, userId);
		            ps1.executeUpdate();
		            closeStatementResultSet(ps1, null);
		            xmlFileVO.setResult("Y");
		            xmlFileVO.setBoeCount(beaInsertCount);
		          }
		          else
		          {
		            xmlFileVO.setBoeCount(beaInsertCount);
		            xmlFileVO.setErrCount(beaInsertFailedCount);
		            xmlFileVO.setResult("PL");
		          }
		        }
		        else {
		          xmlFileVO.setResult("ACKFAIL");
		        }
		      }
		      closeStatementResultSet(pst, rs);
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		    finally
		    {
		      closeConnection(con);
		    }
		    logger.info("Exiting Method");
		    return xmlFileVO;
		  }
  public XMLFileVO xmlUploadIdpmsBESAckFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO, String file)
		  {
		    logger.info("-------------xmlUploadIdpmsBESAckFiles---------------");
		    LoggableStatement pst1 = null;
		    Connection con = null;
		    CommonMethods commonMethods = null;
		    int besInsertCount = 0;
		    int besInsertFailedCount = 0;
		    int outref = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      commonMethods = new CommonMethods();
		      HttpSession session = ServletActionContext.getRequest().getSession();
		      String userId = (String)session.getAttribute("loginedUserId");
		      logger.info("-------------xmlUploadIdpmsBESAckFiles--- -userId-----------" + userId);
		      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		      Document doc = docBuilder.parse(xmlFileUpload);
		      doc.getDocumentElement().normalize();
		      pst1 = new LoggableStatement(con, "SELECT FILENAME FROM ETT_BES_ACK_FILES WHERE TRIM(FILENAME) = TRIM(?) ");
		      pst1.setString(1, fileName);
		      logger.info("-------------xmlUploadIdpmsBESAckFiles--- -FILENAME------ QUERY-----" + pst1.getQueryString());
		      logger.info("------query Execution Started------");
		      ResultSet rs1 = pst1.executeQuery();
		      logger.info("------query Execution Ended------");
		      if (rs1.next())
		      {
		        xmlFileVO.setResult("N");
		        logger.info("----------File Name Already Exisit------");
		      }
		      else
		      {
		        logger.info("---------New File-----");
		        NodeList nList = doc.getElementsByTagName("billOfEntry");
		        for (int temp = 0; temp < nList.getLength(); temp++)
		        {
		          Node nNode = nList.item(temp);
		          if (nNode.getNodeType() == 1)
		          {
		            logger.info("--------Loop- Count----" + temp);
		            Element eElement = (Element)nNode;
		            String portCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("portOfDischarge").item(0).getTextContent()).trim();

		       
		                  String boeNo = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("billOfEntryNumber").item(0).getTextContent()).trim();
		                  String boeDate = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("billOfEntryDate").item(0).getTextContent()).trim();
		                  String ieCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("IECode").item(0).getTextContent()).trim();
		                  String changedIeCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("changeIECode").item(0).getTextContent()).trim();
		                  String adCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("ADCode").item(0).getTextContent()).trim();
		                  String recInd = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("recordIndicator").item(0).getTextContent()).trim();
		                  String payParty = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("paymentParty").item(0).getTextContent()).trim();
		                  String payPartyRefNo = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("paymentReferenceNumber").item(0).getTextContent()).trim();
		                  String outRefNo = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("outwardReferenceNumber").item(0).getTextContent()).trim();
		                  String outRefAdcode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("outwardReferenceADCode").item(0).getTextContent()).trim();
		                  String remCurrency = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("remittanceCurrency").item(0).getTextContent()).trim();
		                  String boeInd = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("billClosureIndicator").item(0).getTextContent()).trim();

		       
		                  String errorCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("errorCode").item(0).getTextContent()).trim();

		       
		                  NodeList nList2 = eElement.getElementsByTagName("invoice");
		                  for (int temp1 = 0; temp1 < nList2.getLength(); temp1++)
		                  {
		                    Node nNode1 = nList2.item(temp1);
		                    if (nNode1.getNodeType() == 1)
		                    {
		                      Element eElement1 = (Element)nNode1;
		                      String inSno = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("invoiceSerialNo").item(0).getTextContent()).trim();

		       
		                      String inNo = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("invoiceNo").item(0).getTextContent()).trim();
		                      String invoiceAmt = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("invoiceAmt").item(0).getTextContent()).trim();
		                      String invoiceAmtIc = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("invoiceAmtIc").item(0).getTextContent()).trim();
		                      String invErrorCode = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("errorCode").item(0).getTextContent()).trim();

		       
		                      String xmlErrorCode = "";
		                      if ((errorCode.equalsIgnoreCase("SUCCESS")) && (
		                        (commonMethods.isNull(invErrorCode)) || (invErrorCode.equalsIgnoreCase("SUCCESS")))) {
		                        xmlErrorCode = "SUCCESS";
		                      } else {
		                        xmlErrorCode = errorCode + invErrorCode;
		                      }
		                      if ((!outRefNo.substring(5, 7).equalsIgnoreCase("TT")) && (!outRefNo.substring(5, 7).equalsIgnoreCase("NU")) && (!outRefNo.substring(5, 8).equalsIgnoreCase("PAD")))
		                      {
		                        try
		                        {
		                          int besCount = 0;
		                          String query = "SELECT COUNT(*) AS BES_COUNT FROM ETT_BES_ACK WHERE TRIM(PAYMENTREFNUMBER) = ? AND RECORDINDICATOR = ? AND ERRORCODES  = 'SUCCESS' ";

		       
		                          LoggableStatement pst2 = new LoggableStatement(con, query);
		                          pst2.setString(1, payPartyRefNo);
		                          pst2.setString(2, recInd);
		                          logger.info("ETT_BES_ACK--------------------" + pst2.getQueryString());
		                          logger.info("------query Execution Started------");
		                          ResultSet rs2 = pst2.executeQuery();
		                          logger.info("------query Execution Ended------");
		                          if (rs2.next())
		                          {
		                            besCount = rs2.getInt("BES_COUNT");
		                            logger.info("ETT_BES_ACK--------besCount------------" + besCount);
		                          }
		                          closeStatementResultSet(pst2, rs2);
		                          if (besCount == 0)
		                          {
		                            try
		                            {
		                              LoggableStatement loggableStatement = new LoggableStatement(con, "INSERT INTO ETT_BES_ACK (PORTOFDISCHARGE,BOENUMBER,BOEDATE,IECODE,CHANGEIECODE,ADCODE,RECORDINDICATOR,PAYMENTPARTY,PAYMENTREFNUMBER,ORNUMBER,ORADCODE,REMITTCUR,BILLCLOSUREINDI,INVOICESERIALNO,INVOICENO,INVOICEAMT,INVOICEAMTIC,ERRORCODES,FILENAME)VALUES(?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		                              loggableStatement.setString(1, portCode);
		                              loggableStatement.setString(2, boeNo);
		                              loggableStatement.setString(3, boeDate);
		                              loggableStatement.setString(4, ieCode);
		                              loggableStatement.setString(5, changedIeCode);
		                              loggableStatement.setString(6, adCode);
		                              loggableStatement.setString(7, recInd);
		                              loggableStatement.setString(8, payParty);
		                              loggableStatement.setString(9, payPartyRefNo);
		                              loggableStatement.setString(10, outRefNo);
		                              loggableStatement.setString(11, outRefAdcode);
		                              loggableStatement.setString(12, remCurrency);
		                              loggableStatement.setString(13, boeInd);
		                              loggableStatement.setString(14, inSno);
		                              loggableStatement.setString(15, inNo);
		                              loggableStatement.setString(16, invoiceAmt);
		                              loggableStatement.setString(17, invoiceAmtIc);
		                              loggableStatement.setString(18, xmlErrorCode);
		                              loggableStatement.setString(19, fileName);
		                              logger.info("ETT_BES_ACK Insert--------------------" + loggableStatement.getQueryString());
		                              logger.info("------query Execution Started------");
		                              besInsertCount += loggableStatement.executeUpdate();
		                              logger.info("------query Execution Ended------");
		                              loggableStatement.close();
		                              outref++;
		                              if ((!file.equalsIgnoreCase("passbesAck")) || (fileName.contains("RBICAN"))) {
		                                continue;
		                              }
		                              String paymentRef = "";
		                              String paymentSlNo = "";
		                              int autoKey = 0;
		                              int fx_event = 0;
		                              int seqNo = 0;
		                              paymentRef = outRefNo.substring(0, outRefNo.length() - 6);
		                              paymentSlNo = outRefNo.substring(outRefNo.length() - 6, outRefNo.length());
		                              String getAutoKey = "SELECT HIGHVAL FROM AUTOKEYS WHERE BASETAB LIKE '%EXTEVENTBOE%'";
		                              LoggableStatement pstAUTOKEYS = new LoggableStatement(con, getAutoKey);
		                              logger.info("AUTOKEYS --------------------" + pstAUTOKEYS.getQueryString());
		                              logger.info("------query Execution Started------");
		                              ResultSet rsAUTOKEYS = pstAUTOKEYS.executeQuery();
		                              logger.info("------query Execution Ended------");
		                              if (rsAUTOKEYS.next())
		                              {
		                                autoKey = rsAUTOKEYS.getInt("HIGHVAL");
		                                logger.info("autoKey after query---> " + autoKey);
		                              }
		                              closeStatementResultSet(pstAUTOKEYS, rsAUTOKEYS);
		                              try
		                              {
		                                Statement st = null;
		                                String getExField = "SELECT BEV.EXTFIELD as EXTFIELD FROM MASTER MAS,BASEEVENT BEV  WHERE MAS.KEY97=BEV.MASTER_KEY AND MAS.MASTER_REF='" + 
		                                  paymentRef + "'" + 
		                                  " AND (BEV.REFNO_PFIX||lPAD(BEV.REFNO_SERL,3,0))='" + paymentSlNo + "'";
		                                logger.info("EXTFIELD Query --->" + getExField);
		                                st = con.createStatement();
		                                logger.info("getExField --------------------" + getExField);
		                                logger.info("------query Execution Started------");
		                                ResultSet rsEXTFIELD = st.executeQuery(getExField);
		                                logger.info("------query Execution Ended------");
		                                while (rsEXTFIELD.next()) {
		                                  fx_event = rsEXTFIELD.getInt("EXTFIELD");
		                                }
		                                logger.info("fx_event after query--->" + fx_event);
		                                closeStatement(st);
		                                closeResultSet(rsEXTFIELD);
		                              }
		                              catch (SQLException se)
		                              {
		                                logger.info("Inside Exception --->" + se.getMessage());
		                                se.printStackTrace();
		                              }
		                              String getCount = "SELECT count(*) as Count FROM EXTEVENTBOE WHERE FK_EVENT =?";
		                              LoggableStatement pstgetCount = new LoggableStatement(con, getCount);
		                              pstgetCount.setInt(1, fx_event);
		                              logger.info("getExField --------------------" + pstgetCount.getQueryString());
		                              logger.info("------query Execution Started------");
		                              ResultSet rsgetCount = pstgetCount.executeQuery();
		                              logger.info("------query Execution Ended------");
		                              if (rsgetCount.next())
		                              {
		                                int c = rsgetCount.getInt("Count");
		                                logger.info("c after query---> " + c);
		                                if (c > 0)
		                                {
		                                  String getSeqNo = "SELECT MAX(SEQN)+1 as SEQNO FROM EXTEVENTBOE WHERE FK_EVENT =?";
		                                  LoggableStatement pstgetSeqNo = new LoggableStatement(con, getSeqNo);
		                                  pstgetSeqNo.setInt(1, fx_event);
		                                  logger.info("getSeqNo --------------------" + pstgetSeqNo.getQueryString());
		                                  logger.info("------query Execution Started------");
		                                  ResultSet rsgetSeqNo = pstgetSeqNo.executeQuery();
		                                  logger.info("------query Execution Ended------");
		                                  if (rsgetSeqNo.next())
		                                  {
		                                    seqNo = rsgetSeqNo.getInt("SEQNO");
		                                    logger.info("seqNo after query---> " + seqNo);
		                                  }
		                                  closeStatementResultSet(pstgetSeqNo, rsgetSeqNo);
		                                }
		                                else
		                                {
		                                  seqNo = 0;
		                                }
		                              }
		                              closeStatementResultSet(pstgetCount, rsgetCount);

		       
		       
		                              String sqlQueryinsertEXTEVENTBOE = "INSERT INTO EXTEVENTBOE(XKEY, SEQN,FK_EVENT,BOENUM,BOECUR,BOEDAT,BOTYP,BOENDA)  VALUES (?,?,?,?,?,TO_DATE(?,'dd/mm/yy'),?,?)";

		       
		       
		                              LoggableStatement pst5 = new LoggableStatement(con, sqlQueryinsertEXTEVENTBOE);
		                              pst5.setInt(1, autoKey);
		                              pst5.setInt(2, seqNo);
		                              pst5.setInt(3, fx_event);
		                              pst5.setString(4, boeNo);
		                              pst5.setString(5, remCurrency);
		                              pst5.setString(6, boeDate);
		                              pst5.setString(7, portCode);
		                              double endAmt = Double.parseDouble(invoiceAmtIc);
		                              pst5.setDouble(8, endAmt);
		                              logger.info("sqlQueryinsertEXTEVENTBOE --------------------" + pst5.getQueryString());
		                              logger.info("------query Execution Started------");
		                              int k = pst5.executeUpdate();
		                              logger.info("------query Execution Ended------");
		                              closePreparedStatement(pst5);
		                              String getBOEChecking = "SELECT BOE_PAYMENT_BP_PAY_FC_AMT,SUM(BOE_PAYMENT_BP_PAY_EDS_FC_AMT) AS BOE_PAYMENT_BP_PAY_EDS_FC_AMT  FROM ETT_BOE_PAYMENT where BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF =? GROUP BY BOE_PAYMENT_BP_PAY_FC_AMT";

		       
		       
		                              LoggableStatement pst6 = new LoggableStatement(con, getBOEChecking);
		                              pst6.setString(1, paymentRef);
		                              pst6.setString(2, paymentSlNo);
		                              logger.info("getBOEChecking --------------------" + pst6.getQueryString());
		                              logger.info("------query Execution Started------");
		                              ResultSet rs6 = pst6.executeQuery();
		                              logger.info("------query Execution Ended------");
		                              if (rs6.next())
		                              {
		                                String temp_fc_amt = commonMethods.toDouble1(rs6.getString("BOE_PAYMENT_BP_PAY_FC_AMT")).trim();
		                                String temp_eds_fc_amt = commonMethods.toDouble1(rs6.getString("BOE_PAYMENT_BP_PAY_EDS_FC_AMT")).trim();
		                                double fc_amt = Double.parseDouble(temp_fc_amt);
		                                double eds_fc_amt = Double.parseDouble(temp_eds_fc_amt);
		                                if (fc_amt == eds_fc_amt)
		                                {
		                                  String upQuery = "UPDATE EXTEVENT SET BOSUM= 'Y' WHERE KEY29 =?";
		                                  LoggableStatement pst7 = new LoggableStatement(con, upQuery);
		                                  pst7.setInt(1, fx_event);
		                                  logger.info("upQuery --------------------" + pst7.getQueryString());
		                                  logger.info("------query Execution Started------");
		                                  int checkUpdate = pst7.executeUpdate();
		                                  logger.info("------query Execution Ended------");
		                                  logger.info("Check Update--->" + checkUpdate);
		                                  closePreparedStatement(pst7);
		                                }
		                              }
		                              closeStatementResultSet(pst6, rs6);
		                              if (k <= 0) {
		                                continue;
		                              }
		                              autoKey++;
		                              String autoUpQuery = "UPDATE AUTOKEYS SET HIGHVAL =? WHERE BASETAB ='EXTEVENTBOE'";
		                              LoggableStatement pst8 = new LoggableStatement(con, autoUpQuery);
		                              pst8.setInt(1, autoKey);
		                              logger.info("autoUpQuery --------------------" + pst8.getQueryString());
		                              logger.info("------query Execution Started------");
		                              pst8.executeUpdate();
		                              logger.info("------query Execution Ended------");
		                              closePreparedStatement(pst8);
		                            }
		                            catch (SQLException se)
		                            {
		                              logger.info("Exception-----" + se);
		                              besInsertFailedCount++;
		                              String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                              LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                              pst_err_query.setString(1, outRefNo);
		                              pst_err_query.setString(2, adCode);
		                              pst_err_query.setString(3, ieCode);
		                              pst_err_query.setString(4, fileName);
		                              pst_err_query.setString(5, se.getMessage());
		                              pst_err_query.setString(6, "BES");
		                              pst_err_query.executeUpdate();
		                              closePreparedStatement(pst_err_query);
		                            }
		                            catch (Exception e)
		                            {
		                              logger.info("Exception-----" + e);
		                              besInsertFailedCount++;
		                              String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                              LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                              pst_err_query.setString(1, outRefNo);
		                              pst_err_query.setString(2, adCode);
		                              pst_err_query.setString(3, ieCode);
		                              pst_err_query.setString(4, fileName);
		                              pst_err_query.setString(5, e.getMessage());
		                              pst_err_query.setString(6, "BES");
		                              pst_err_query.executeUpdate();
		                              closePreparedStatement(pst_err_query);
		                            }
		                          }
		                          else
		                          {
		                            besInsertFailedCount++;
		                            String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                            LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                            pst_err_query.setString(1, outRefNo);
		                            pst_err_query.setString(2, adCode);
		                            pst_err_query.setString(3, ieCode);
		                            pst_err_query.setString(4, fileName);
		                            pst_err_query.setString(5, "PAYMENT REF NUMBER & RecordIndicator already Exist");
		                            pst_err_query.setString(6, "BES");
		                            pst_err_query.executeUpdate();
		                            closePreparedStatement(pst_err_query);
		                          }
		                        }
		                        catch (Exception e)
		                        {
		                          logger.info("Exception-----" + e);
		                          besInsertFailedCount++;
		                          String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                          LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                          pst_err_query.setString(1, outRefNo);
		                          pst_err_query.setString(2, adCode);
		                          pst_err_query.setString(3, ieCode);
		                          pst_err_query.setString(4, fileName);
		                          pst_err_query.setString(5, e.getMessage());
		                          pst_err_query.setString(6, "BES");
		                          pst_err_query.executeUpdate();
		                          closePreparedStatement(pst_err_query);
		                        }
		                      }
		                      else
		                      {
		                        besInsertFailedCount++;
		                        String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                        LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                        pst_err_query.setString(1, outRefNo);
		                        pst_err_query.setString(2, adCode);
		                        pst_err_query.setString(3, ieCode);
		                        pst_err_query.setString(4, fileName);
		                        pst_err_query.setString(5, "INVALID ORM NUMBER");
		                        pst_err_query.setString(6, "BES");
		                        pst_err_query.executeUpdate();
		                        closePreparedStatement(pst_err_query);
		                      }
		                    }
		                  }
		                }
		              }
		              int i = xmlFileVO.setInsertCount(besInsertCount);
		              logger.info("No.of BOEs ---------------------------------------- :" + i);
		              logger.info("Insertion Count-----------------" + besInsertCount);

		       
		              xmlFileVO.setBoeCount(besInsertCount);
		              if (besInsertCount > 0)
		              {
		                if (besInsertFailedCount == 0)
		                {
		                  logger.info("Insertion Count----Record Inserted-------------" + besInsertCount);
		                  LoggableStatement ps1 = new LoggableStatement(con, "INSERT INTO ETT_BES_ACK_FILES(FILENO,FILENAME,CREATED_BY) VALUES (BES_ACK_SEQ.NEXTVAL,?,?)");
		                  ps1.setString(1, fileName);
		                  ps1.setString(2, userId);
		                  logger.info("Exceotin---- INS_ETT_BES_ACK_FILESQuery-" + ps1.getQueryString());
		                  int a = ps1.executeUpdate();
		                  logger.info("Exceotin---- INS_ETT_BES_ACK_FILESQuery----------executeUpdate-" + a);
		                  closeStatementResultSet(ps1, null);
		                  xmlFileVO.setBoeCount(outref);
		                  xmlFileVO.setResult("Y");
		                }
		                else
		                {
		                  xmlFileVO.setBoeCount(outref);
		                  xmlFileVO.setErrCount(besInsertFailedCount);
		                  xmlFileVO.setResult("PL");
		                }
		              }
		              else
		              {
		                logger.info("Insertion Count----Record Not Inserted-------------");
		                xmlFileVO.setResult("ACKFAIL");
		              }
		            }
		            rs1.close();
		          }
		          catch (Exception exception)
		          {
		            logger.info("-------------xmlUploadIdpmsBESAckFiles--- -exception-----------" + exception);
		            exception.printStackTrace();
		          }
		          finally
		          {
		            closeSqlRefferance(pst1, con);
		          }
		          logger.info("Exiting Method");
		          return xmlFileVO;
		        }
  
  public XMLFileVO xmlUploadIdpmsAckFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
		          throws ApplicationException
		        {
		          logger.info("Entering Method");
		          LoggableStatement pst1 = null;
		          Connection con = null;
		          int ormInsertCount = 0;
		          int ormInsertFailedCount = 0;
		          CommonMethods commonMethods = null;
		          String result = null;
		          String target = "";
		          String sXMLFileName = "";
		          int outref = 0;
		          try
		          {
		            logger.info("------xmlUploadIdpmsAckFiles--:: Entering Try Block ");
		            con = DBConnectionUtility.getConnection();
		            commonMethods = new CommonMethods();
		            HttpSession session = ServletActionContext.getRequest()
		              .getSession();
		            String userId = (String)session.getAttribute("loginedUserId");
		            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		            Document doc = docBuilder.parse(xmlFileUpload);
		            doc.getDocumentElement().normalize();
		            pst1 = new LoggableStatement(con, "SELECT FILENAME FROM ETT_ORM_ACK_FILES WHERE TRIM(FILENAME) = TRIM(?) ");
		            pst1.setString(1, fileName);
		            ResultSet rs1 = pst1.executeQuery();
		            if (rs1.next())
		            {
		              xmlFileVO.setResult("N");
		            }
		            else
		            {
		              logger.info("------xmlUploadIdpmsAckFiles--:: Entering Else Block ");
		              NodeList nList = doc.getElementsByTagName("outwardReference");
		              for (int temp = 0; temp < nList.getLength(); temp++)
		              {
		                Node nNode = nList.item(temp);
		                if (nNode.getNodeType() == 1)
		                {
		                  Element eElement = (Element)nNode;
		                  String ormNo = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("outwardReferenceNumber").item(0).getTextContent()).trim();
		                  String adCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("ADCode").item(0).getTextContent()).trim();
		                  String amount = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("Amount").item(0).getTextContent()).trim();
		                  String currency = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("currencyCode").item(0).getTextContent()).trim();
		                  String payDate = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("paymentDate").item(0).getTextContent()).trim();
		                  String ieCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("IECode").item(0).getTextContent()).trim();
		                  String ieName = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("IEName").item(0).getTextContent()).trim();
		                  String ieAddr = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("IEAddress").item(0).getTextContent()).trim();
		                  String iePANNo = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("IEPANNumber").item(0).getTextContent()).trim();
		                  String isGoods = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("isCapitalGoods").item(0).getTextContent()).trim();
		                  String benName = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("beneficiaryName").item(0).getTextContent()).trim();
		                  String benAccNo = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("beneficiaryAccountNumber").item(0).getTextContent()).trim();
		                  String benCty = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("beneficiaryCountry").item(0).getTextContent()).trim();
		                  String swiftMsg = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("SWIFT").item(0).getTextContent()).trim();
		                  String purpCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("purposeCode").item(0).getTextContent()).trim();
		                  String recInd = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("recordIndicator").item(0).getTextContent()).trim();
		                  String remarks = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("remarks").item(0).getTextContent()).trim();
		                  String payTerms = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("paymentTerms").item(0).getTextContent()).trim();
		                  String errorCode = commonMethods.getEmptyIfNull(eElement
		                    .getElementsByTagName("errorCode").item(0).getTextContent()).trim();
		                  int ormMasterCount = 0;
		                  String queryMaster = "SELECT COUNT(*) AS ORM_MASTER_COUNT FROM MASTER M, BASEEVENT BE WHERE M.KEY97 = BE.MASTER_KEY and TRIM(M.MASTER_REF)||TRIM(BE.REFNO_PFIX)||TRIM(LPAD(BE.REFNO_SERL,3,0))='" + ormNo + "'";
		                  LoggableStatement pstMaster = new LoggableStatement(con, queryMaster);
		                  ResultSet rsMaster = pstMaster.executeQuery();
		                  if (rsMaster.next())
		                  {
		                    ormMasterCount = rsMaster.getInt("ORM_MASTER_COUNT");
		                    logger.info("------ormCount------" + ormMasterCount);
		                  }
		                  closeStatementResultSet(pstMaster, rsMaster);
		                  if (ormMasterCount > 0)
		                  {
		                    int ormCount = 0;

		       
		                    String query = "SELECT COUNT(*) AS ORM_COUNT FROM ETT_ORM_ACK WHERE TRIM(OUTWARDREFERNECNO) = '" + ormNo + "'" + 
		                      " AND RECORDINDICATOR = '" + recInd + "' AND ERRORCODES  = 'SUCCESS' ";
		                    LoggableStatement pst2 = new LoggableStatement(con, query);
		                    ResultSet rs2 = pst2.executeQuery();
		                    if (rs2.next())
		                    {
		                      ormCount = rs2.getInt("ORM_COUNT");
		                      logger.info("------ormCount------" + ormCount);
		                    }
		                    closeStatementResultSet(pst2, rs2);
		                    if (ormCount == 0)
		                    {
		                      try
		                      {
		                        String insert_query = "INSERT INTO ETT_ORM_ACK(OUTWARDREFERNECNO,ADCODE,AMOUNT,CURR,PAYDATE,IECODE,IENAME,IEADDRESS,IEPANNO,ISCAPITAL,BENNAME,BENACCNO,BENCOUNTRY,SWIFT,PURPCODE,RECORDINDICATOR,REMARKS,PAYTERMS,ERRORCODES,FILENAME) VALUES (?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		       
		       
		                        LoggableStatement pst = new LoggableStatement(con, insert_query);
		                        pst.setString(1, ormNo);
		                        pst.setString(2, adCode);
		                        pst.setString(3, amount);
		                        pst.setString(4, currency);
		                        pst.setString(5, payDate);
		                        pst.setString(6, ieCode);
		                        pst.setString(7, ieName);
		                        pst.setString(8, ieAddr);
		                        pst.setString(9, iePANNo);
		                        pst.setString(10, isGoods);
		                        pst.setString(11, benName);
		                        pst.setString(12, benAccNo);
		                        pst.setString(13, benCty);
		                        pst.setString(14, swiftMsg);
		                        pst.setString(15, purpCode);
		                        pst.setString(16, recInd);
		                        pst.setString(17, remarks);
		                        pst.setString(18, payTerms);
		                        pst.setString(19, errorCode);
		                        pst.setString(20, fileName);
		                        ormInsertCount += pst.executeUpdate();
		                        closePreparedStatement(pst);
		                        outref++;
		                      }
		                      catch (SQLException se)
		                      {
		                        logger.info("------xmlUploadIdpmsAckFiles--:: Exception occured for uploading ormNo : " + ormNo);
		                        logger.info("------Exception------" + se.getMessage());
		                        ormInsertFailedCount++;
		                        String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                        LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                        pst_err_query.setString(1, ormNo);
		                        pst_err_query.setString(2, adCode);
		                        pst_err_query.setString(3, ieCode);
		                        pst_err_query.setString(4, fileName);
		                        pst_err_query.setString(5, se.getMessage());
		                        pst_err_query.setString(6, "ORM");
		                        pst_err_query.executeUpdate();
		                        closePreparedStatement(pst_err_query);
		                      }
		                    }
		                    else
		                    {
		                      ormInsertFailedCount++;
		                      String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                      LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                      pst_err_query.setString(1, ormNo);
		                      pst_err_query.setString(2, adCode);
		                      pst_err_query.setString(3, ieCode);
		                      pst_err_query.setString(4, fileName);
		                      pst_err_query.setString(5, "ORM NUMBER & RecordIndicator already Exist");
		                      pst_err_query.setString(6, "ORA");
		                      pst_err_query.executeUpdate();
		                      closePreparedStatement(pst_err_query);
		                    }
		                  }
		                  else
		                  {
		                    logger.info("------ORM NOT FOUND------");
		                    ormInsertFailedCount++;
		                    String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                    LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                    pst_err_query.setString(1, ormNo);
		                    pst_err_query.setString(2, adCode);
		                    pst_err_query.setString(3, ieCode);
		                    pst_err_query.setString(4, fileName);
		                    pst_err_query.setString(5, "INVALID ORM NUMBER");
		                    pst_err_query.setString(6, "ORM");
		                    pst_err_query.executeUpdate();
		                    closePreparedStatement(pst_err_query);
		                  }
		                }
		              }
		              if (ormInsertCount > 0)
		              {
		                if (ormInsertFailedCount == 0)
		                {
		                  LoggableStatement ps1 = new LoggableStatement(con, "INSERT INTO ETT_ORM_ACK_FILES(FILENO,FILENAME,CREATED_BY) VALUES (ORM_ACK_SEQ.NEXTVAL,?,?)");
		                  ps1.setString(1, fileName);
		                  ps1.setString(2, userId);
		                  ps1.executeUpdate();
		                  closeStatementResultSet(ps1, null);

		       
		                  xmlFileVO.setOutref(outref);
		                  xmlFileVO.setResult("Y");
		                }
		                else
		                {
		                  xmlFileVO.setOutref(outref);
		                  xmlFileVO.setOutrefFailedCount(ormInsertFailedCount);
		                  xmlFileVO.setResult("PL");
		                }
		              }
		              else {
		                xmlFileVO.setResult("ACKFAIL");
		              }
		              logger.info("------xmlUploadIdpmsAckFiles--:: Exiting Else Block ");
		            }
		            rs1.close();
		            logger.info("------xmlUploadIdpmsAckFiles--:: Exiting Try Block ");
		          }
		          catch (Exception exception)
		          {
		            exception.printStackTrace();
		          }
		          finally
		          {
		            closeSqlRefferance(pst1, con);
		          }
		          logger.info("Exiting Method");
		          return xmlFileVO;
		        }
  
  public XMLFileVO xmlUploadIDPMSFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
		          throws DAOException
		        {
		          logger.info("Entering Method");
		          Connection con = null;
		          LoggableStatement pst1 = null;
		          ResultSet rs = null;
		          BOEDetailsVO boeVO = null;
		          CommonMethods commonMethods = null;
		          int invLength = 0;
		          ArrayList<Object> xmlErrorList = null;
		          ArrayList<BOEDetailsVO> boeVOList = null;
		          ArrayList<OBEInvoiceDetailsVO> invoiceVOList = null;
		          try
		          {
		            logger.info("------xmlUploadIDPMSFiles-----------");
		            boeVOList = new ArrayList();
		            invoiceVOList = new ArrayList();
		            HttpSession session = ServletActionContext.getRequest()
		              .getSession();
		            String userId = (String)session.getAttribute("loginedUserId");
		            logger.info("userId--------getAttribute--------------" + userId);
		            if (userId == null)
		            {
		              HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
		                .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
		              userId = request.getRemoteUser();
		              logger.info("sessionUserName--------getRemoteUser--------------" + userId);
		              if (userId == null) {
		                userId = "SUPERVISOR";
		              }
		            }
		            commonMethods = new CommonMethods();
		            xmlErrorList = new ArrayList();
		            con = DBConnectionUtility.getConnection();

		       
		       
		            this.xmlFileVO1 = validateMDFXMLTags(xmlFileUpload, fileName);
		            logger.info(this.xmlFileVO1.getStatusRes());
		            logger.info("The Return String Value is : " + this.xmlFileVO1.getStatusRes());
		            XMLFileVO localXMLFileVO;
		            if (this.xmlFileVO1.getStatusRes().trim().equalsIgnoreCase("false"))
		            {
		              xmlFileVO.setResult("XMLE");
		              xmlFileVO.setXmlErrTagName(this.xmlFileVO1.getTagName());
		              localXMLFileVO = xmlFileVO;return localXMLFileVO;
		            }
		            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		            Document doc = docBuilder.parse(xmlFileUpload);
		            doc.getDocumentElement().normalize();
		            pst1 = new LoggableStatement(con, "SELECT FILENAME FROM ETT_IDPMS_FILES WHERE TRIM(FILENAME)=TRIM(?)");
		            pst1.setString(1, fileName);
		            rs = pst1.executeQuery();
		            if (rs.next())
		            {
		              xmlFileVO.setResult("N");
		            }
		            else
		            {
		              NodeList chksum = doc.getElementsByTagName("checkSum");
		              Node firstPersonNode = chksum.item(0);
		              if (firstPersonNode.getNodeType() == 1)
		              {
		                Element firstPersonElement = (Element)firstPersonNode;
		                String noOfBOE = commonMethods.getEmptyIfNull(firstPersonElement
		                  .getElementsByTagName("noOfbillOfEntry").item(0).getTextContent()).trim();
		                String noOfInvoice = commonMethods.getEmptyIfNull(firstPersonElement
		                        .getElementsByTagName("noOfInvoices").item(0).getTextContent()).trim();
		                      int fileNo = 0;
		                      LoggableStatement p2 = new LoggableStatement(con, "SELECT IDPMS_SEQNO.NEXTVAL FROM DUAL");
		                      ResultSet rs2 = p2.executeQuery();
		                      if (rs2.next()) {
		                        fileNo = rs2.getInt("nextval");
		                      }
		                      closeStatementResultSet(p2, rs2);
		                      LoggableStatement ps1 = new LoggableStatement(con, "INSERT INTO ETT_IDPMS_FILES(FILENO,NOOFBOE,NOOFINV,FILENAME,CREATED_BY) VALUES (?,?,?,?,?)");
		                      ps1.setInt(1, fileNo);
		                      ps1.setString(2, noOfBOE);
		                      ps1.setString(3, noOfInvoice);
		                      ps1.setString(4, fileName);
		                      ps1.setString(5, userId);
		                      ps1.executeUpdate();
		                      closeStatementResultSet(ps1, null);
		                      logger.info("The total no of BOE is : ---------------------->>> " + noOfBOE);
		                      logger.info("The total no of Invoice is : ---------------------->>> " + noOfInvoice);
		                      double totalBoeCount = commonMethods.toDouble(noOfBOE);
		                      double totalInvoiceCount = commonMethods.toDouble(noOfInvoice);
		                      if (totalBoeCount > totalInvoiceCount)
		                      {
		                        xmlFileVO.setResult("IFD");
		                      }
		                      else
		                      {
		                        NodeList nList = doc.getElementsByTagName("billOfEntry");

		             
		                        int totalBOE = nList.getLength();
		                        logger.info("The Total BOE List is : =====================>>> totalBOE: " + totalBOE);
		                        int noBOE = 0;
		                        for (int temp = 0; temp < nList.getLength(); temp++)
		                        {
		                          String boeErrorString = "";
		                          String boeInvErrorString = "";
		                          boeVO = new BOEDetailsVO();
		                          Node nNode = nList.item(temp);
		                          if (nNode.getNodeType() == 1)
		                          {
		                            Element eElement = (Element)nNode;
		                            boeVO.setPortCode(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("portOfDischarge").item(0).getTextContent()).trim());
		                            boeVO.setImportAgency(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("importAgency").item(0).getTextContent()).trim());
		                            boeVO.setBoeNo(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("billOfEntryNumber").item(0).getTextContent()).trim());
		                            boeVO.setBoeDate(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("billOfEntryDate").item(0).getTextContent()).trim());
		                            boeVO.setAdCode(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("ADCode").item(0).getTextContent()).trim());
		                            boeVO.setGpFirm(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("G-P").item(0).getTextContent()).trim());
		                            boeVO.setIeCode(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("IECode").item(0).getTextContent()).trim());
		                            boeVO.setIeName(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("IEName").item(0).getTextContent()).trim());
		                            boeVO.setIeAddress(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("IEAddress").item(0).getTextContent()).trim());
		                            boeVO.setIePanNo(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("IEPANNumber").item(0).getTextContent()).trim());
		                            boeVO.setPortShipment(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("portOfShipment").item(0).getTextContent()).trim());
		                            boeVO.setIgmNo(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("IGMNumber").item(0).getTextContent()).trim());
		                            boeVO.setIgmDate(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("IGMDate").item(0).getTextContent()).trim());
		                            boeVO.setMblNo(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("MAWB-MBLNumber").item(0).getTextContent()).trim());
		                            boeVO.setMblDate(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("MAWB-MBLDate").item(0).getTextContent()).trim());
		                            boeVO.setHblNo(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("HAWB-HBLNumber").item(0).getTextContent()).trim());
		                            boeVO.setHblDate(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("HAWB-HBLDate").item(0).getTextContent()).trim());
		                            boeVO.setRecIndicator(commonMethods.getEmptyIfNull(eElement
		                              .getElementsByTagName("recordIndicator").item(0).getTextContent()).trim());
		                            if (commonMethods.isNull(boeVO.getPortCode())) {
		                              boeErrorString = commonMethods.setErrorString(boeErrorString, "Port Of Discharge is Empty");
		                            }
		                            if (commonMethods.isNull(boeVO.getImportAgency())) {
		                              boeErrorString = commonMethods.setErrorString(boeErrorString, "Import Agency is Empty");
		                            }
		                            if (commonMethods.isNull(boeVO.getBoeNo())) {
		                              boeErrorString = commonMethods.setErrorString(boeErrorString, "BOE Number is Empty");
		                            }
		                            if (commonMethods.isNull(boeVO.getBoeDate())) {
		                              boeErrorString = commonMethods.setErrorString(boeErrorString, "BOE Date is Empty");
		                            }
		                            if (commonMethods.isNull(boeVO.getAdCode())) {
		                              boeErrorString = commonMethods.setErrorString(boeErrorString, "ADCode is Empty");
		                            }
		                            if (commonMethods.isNull(boeVO.getIeCode())) {
		                              boeErrorString = commonMethods.setErrorString(boeErrorString, "IECode is Empty");
		                            }
		                            if (commonMethods.isNull(boeVO.getPortShipment())) {
		                              boeErrorString = commonMethods.setErrorString(boeErrorString, "Port Of Shipment is Empty");
		                            }
		                            if (commonMethods.isNull(boeVO.getRecIndicator())) {
		                              boeErrorString = commonMethods.setErrorString(boeErrorString, "Record Indicator is Empty");
		                            }
		                            boeVO.setFileNo(fileNo);
		                            NodeList nList1 = eElement.getElementsByTagName("invoice");
		                            invLength += nList1.getLength();
		                            for (int temp1 = 0; temp1 < nList1.getLength(); temp1++)
		                            {
		                              OBEInvoiceDetailsVO invoiceVO = new OBEInvoiceDetailsVO();
		                              Node nNode1 = nList1.item(temp1);
		                              if (nNode1.getNodeType() == 1)
		                              {
		                                Element eElement1 = (Element)nNode1;
		                                invoiceVO.setInvoiceSerialNo(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("invoiceSerialNo").item(0).getTextContent()).trim());
		                                invoiceVO.setInvoiceNo(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("invoiceNo").item(0).getTextContent()).trim());
		                                invoiceVO.setInvoiceTerms(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("termsOfInvoice").item(0).getTextContent()).trim());
		                                invoiceVO.setSupplierName(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("supplierName").item(0).getTextContent()).trim());
		                                invoiceVO.setSupplierAddr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("supplierAddress").item(0).getTextContent()).trim());
		                                invoiceVO.setSupplierCountry(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("supplierCountry").item(0).getTextContent()).trim());
		                                invoiceVO.setSellerName(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("sellerName").item(0).getTextContent()).trim());
		                                invoiceVO.setSellerAddr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("sellerAddress").item(0).getTextContent()).trim());
		                                invoiceVO.setSellerCountry(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("sellerCountry").item(0).getTextContent()).trim());
		                                invoiceVO.setInvoiceAmt(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("invoiceAmount").item(0).getTextContent()).trim());
		                                invoiceVO.setInvoiceCurr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("invoiceCurrency").item(0).getTextContent()).trim());
		                                invoiceVO.setFreightAmt(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("freightAmount").item(0).getTextContent()).trim());
		                                invoiceVO.setFreightCurr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("freightCurrencyCode").item(0).getTextContent()).trim());
		                                invoiceVO.setInsAmt(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("insuranceAmount").item(0).getTextContent()).trim());
		                                invoiceVO.setInsCurr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("insuranceCurrencyCode").item(0).getTextContent()).trim());
		                                invoiceVO.setAgencyComm(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("agencyCommission").item(0).getTextContent()).trim());
		                                invoiceVO.setAgencyCurr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("agencyCurrency").item(0).getTextContent()).trim());
		                                invoiceVO.setDiscountAmt(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("discountCharges").item(0).getTextContent()).trim());
		                                invoiceVO.setDiscountCurr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("discountCurrency").item(0).getTextContent()).trim());
		                                invoiceVO.setMiscAmt(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("miscellaneousCharges").item(0).getTextContent()).trim());
		                                invoiceVO.setMiscCurr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("miscellaneousCurrency").item(0).getTextContent()).trim());
		                                invoiceVO.setThirdPartyName(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("thirdPartyName").item(0).getTextContent()).trim());
		                                invoiceVO.setThirdPartyAddr(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("thirdPartyAddress").item(0).getTextContent()).trim());
		                                invoiceVO.setThirdPartyCountry(commonMethods.getEmptyIfNull(eElement1
		                                  .getElementsByTagName("thirdPartyCountry").item(0).getTextContent()).trim());
		                                if (commonMethods.isNull(invoiceVO.getInvoiceSerialNo())) {
		                                  boeInvErrorString = commonMethods.setErrorString(boeErrorString, "Invoice Serial No is Empty");
		                                }
		                                if (commonMethods.isNull(invoiceVO.getInvoiceNo())) {
		                                  boeInvErrorString = commonMethods.setErrorString(boeErrorString, "Invoice No is Empty");
		                                }
		                                if (commonMethods.isNull(invoiceVO.getInvoiceTerms())) {
		                                  boeInvErrorString = commonMethods.setErrorString(boeErrorString, "Invoice Terms is Empty");
		                                }
		                                if (commonMethods.isNull(invoiceVO.getInvoiceCurr())) {
		                                  boeInvErrorString = commonMethods.setErrorString(boeErrorString, "Invoice Currency is Empty");
		                                }
		                                if (commonMethods.isNull(invoiceVO.getInvoiceAmt())) {
		                                  boeInvErrorString = commonMethods.setErrorString(boeErrorString, "Invoice Amount is Empty");
		                                }
		                                boeErrorString = boeErrorString + boeInvErrorString;
		                                boeInvErrorString = "";
		                                boeVO.setErrorString(boeErrorString);
		                                insertOBE_TEMP_STG_Data(con, boeVO, invoiceVO);
		                                boeErrorString = "";
		                              }
		                            }
		                          }
		                        }
		                        boeVO.setHeaderBOECount(Integer.parseInt(noOfBOE));
		                        boeVO.setHeaderInvCount(Integer.parseInt(noOfInvoice));
		                        boeVO.setContentBOECount(totalBOE);
		                        boeVO.setContentInvCount(invLength);
		                        boeVO = compareBOECount(boeVO);
		                        String sRetValue = boeVO.getCountResult();
		                        if (!"S".equalsIgnoreCase(sRetValue))
		                        {
		                          xmlFileVO.setResult(boeVO.getCountResult());
		                          localXMLFileVO = xmlFileVO;return localXMLFileVO;
		                        }
		                        String getOBEData = "{call ETT_IDPMS_OBE_DATA(?)}";
		                        CallableStatement callableStatement = con.prepareCall(getOBEData);
		                        callableStatement.setInt(1, fileNo);
		                        callableStatement.executeUpdate();
		                        if (callableStatement != null) {
		                          callableStatement.close();
		                        }
		                        String countQuery = "SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_DETAILS WHERE FILENO = ? ";
		                        LoggableStatement ps3 = new LoggableStatement(con, countQuery);
		                        ps3.setInt(1, fileNo);
		                        ResultSet rs3 = ps3.executeQuery();
		                        if (rs3.next()) {
		                          noBOE = rs3.getInt("BOE_COUNT");
		                        }
		                        closeStatementResultSet(ps3, rs3);

		             
		             
		                        int iBOECount = 0;
		                        int iINVCount = 0;
		                        String sOBEStatusCountQuery = "SELECT COUNT(DISTINCT BOE.BOE_NUMBER) AS BOECNT, COUNT(INV.INVOICE_NUMBER) AS INVCNT FROM ETT_BOE_DETAILS BOE, ETT_INVOICE_DETAILS INV WHERE BOE.BOE_NUMBER = INV.BOE_NUMBER AND BOE.BOE_DATE = INV.BOE_DATE AND BOE.BOE_PORT_OF_DISCHARGE = INV.BOE_PORT_OF_DISCHARGE AND BOE.FILENO = INV.FILENO AND BOE.FILENO = ?";
		                        LoggableStatement ps5 = new LoggableStatement(con, sOBEStatusCountQuery);
		                        ps5.setInt(1, fileNo);
		                        ResultSet rs5 = ps5.executeQuery();
		                        if (rs5.next())
		                        {
		                          iBOECount = rs5.getInt("BOECNT");
		                          iINVCount = rs5.getInt("INVCNT");
		                        }
		                        closeStatementResultSet(ps5, rs5);
		                        if (totalBOE == noBOE)
		                        {
		                          xmlFileVO.setResult("Y");
		                          xmlFileVO.setBoeCount(iBOECount);
		                          XMLFileVO.setInvCount(iINVCount);
		                        }
		                        else if (totalBOE != noBOE)
		                        {
		                          int boeErrorCode = 0;
		                          String getErrorData = "SELECT COUNT(DISTINCT BOE_NUMBER) AS CNT FROM ETT_BOE_DETAILS_TEMP_STG\tWHERE FILENO = ? AND ERROR_CODE IS NOT NULL";

		             
		                          LoggableStatement ps4 = new LoggableStatement(con, getErrorData);
		                          ps4.setInt(1, fileNo);
		                          ResultSet rs4 = ps4.executeQuery();
		                          if (rs4.next()) {
		                            boeErrorCode = rs4.getInt("CNT");
		                          }
		                          closeStatementResultSet(ps4, rs4);
		                          if (boeErrorCode == 0)
		                          {
		                            xmlFileVO.setResult("Y");
		                            xmlFileVO.setBoeCount(iBOECount);
		                            XMLFileVO.setInvCount(iINVCount);
		                          }
		                          else if (noBOE == 0)
		                          {
		                            xmlFileVO.setResult("F");
		                            xmlFileVO.setBoeCount(iBOECount);
		                            XMLFileVO.setInvCount(iINVCount);
		                          }
		                          else
		                          {
		                            xmlFileVO.setResult("PL");
		                            xmlFileVO.setBoeCount(iBOECount);
		                            XMLFileVO.setInvCount(iINVCount);
		                            xmlFileVO.setErrCount(boeErrorCode);
		                          }
		                        }
		                      }
		                    }
		                  }
		                }
		                catch (Exception exception)
		                {
		                  exception.printStackTrace();
		                  xmlFileVO.setResult("FILEFAILED");
		                  throwDAOException(exception);
		                }
		                finally
		                {
		                  DBConnectionUtility.surrenderDB(con, pst1, rs);
		                }
		                logger.info("Exiting Method");
		                return xmlFileVO;
		        		}

  //xmlUploadIdpmsORAAckFiles
  public XMLFileVO xmlUploadIdpmsORAAckFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
		    throws DAOException
		  {
		    logger.info("----------xmlUploadIdpmsORAAckFiles----------------------");
		    Connection con = null;
		    CommonMethods commonMethods = null;
		    int oraInsertCount = 0;
		    int oraInsertFailedCount = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      commonMethods = new CommonMethods();
		      HttpSession session = ServletActionContext.getRequest().getSession();
		      String userId = (String)session.getAttribute("loginedUserId");
		      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		      Document doc = docBuilder.parse(xmlFileUpload);
		      doc.getDocumentElement().normalize();
		      LoggableStatement pst1 = new LoggableStatement(con, "SELECT FILENAME FROM ETT_ORA_ACK_FILES WHERE TRIM(FILENAME) = TRIM(?) ");
		      pst1.setString(1, fileName);
		      ResultSet rs1 = pst1.executeQuery();
		      if (rs1.next())
		      {
		        xmlFileVO.setResult("N");
		      }
		      else
		      {
		        NodeList nList = doc.getElementsByTagName("outwardReference");
		        for (int temp = 0; temp < nList.getLength(); temp++)
		        {
		          Node nNode = nList.item(temp);
		          if (nNode.getNodeType() == 1)
		          {
		            Element eElement = (Element)nNode;
		            String ormNo = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("outwardReferenceNumber").item(0).getTextContent()).trim();
		            String adCode = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("ADCode").item(0).getTextContent()).trim();
		            String currency = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("remittanceCurrency").item(0).getTextContent()).trim();
		            String amount = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("adjustedAmount").item(0).getTextContent()).trim();
		            String ieCode = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("IECode").item(0).getTextContent()).trim();
		            String swiftMsg = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("SWIFT").item(0).getTextContent()).trim();
		            String adjInd = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("adjustmentIndicator").item(0).getTextContent()).trim();
		            String adjSeqNo = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("adjustmentSeqNumber").item(0).getTextContent()).trim();
		            String appBy = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("approvedBy").item(0).getTextContent()).trim();
		            String letterNo = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("letterNumber").item(0).getTextContent()).trim();
		            String letDate = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("letterDate").item(0).getTextContent()).trim();
		            String docNo = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("documentNumber").item(0).getTextContent()).trim();
		            String docDate = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("documentDate").item(0).getTextContent()).trim();
		            String portCode = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("portofDischarge").item(0).getTextContent()).trim();
		            String recInd = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("recordIndicator").item(0).getTextContent()).trim();
		            String remark = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("remark").item(0).getTextContent()).trim();
		            String errorCode = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("errorCode").item(0).getTextContent()).trim();
		            int ormMasterCount = 0;
		            String queryMaster = "SELECT COUNT(*) AS ORM_MASTER_COUNT FROM MASTER M, BASEEVENT BE WHERE M.KEY97 = BE.MASTER_KEY and TRIM(M.MASTER_REF)||TRIM(BE.REFNO_PFIX)||TRIM(LPAD(BE.REFNO_SERL,3,0))='" + ormNo + "'";
		            LoggableStatement pstMaster = new LoggableStatement(con, queryMaster);
		            ResultSet rsMaster = pstMaster.executeQuery();		    
		            if (rsMaster.next())
		            {
		              ormMasterCount = rsMaster.getInt("ORM_MASTER_COUNT");
		              logger.info("------ormCount------" + ormMasterCount);
		            }
		            closeStatementResultSet(pstMaster, rsMaster);
		            if (ormMasterCount > 0)
		            {
		              int oraCount = 0;
		              String query = "SELECT COUNT(*) AS ORM_COUNT FROM ETT_ORA_ACK WHERE ADJ_SEQNO = ? AND RECORDINDICATOR = ? AND ERRORCODES  = 'SUCCESS' ";

		 
		              LoggableStatement pst2 = new LoggableStatement(con, query);
		              pst2.setString(1, adjSeqNo);
		              pst2.setString(2, recInd);
		              logger.info("----------ETT_ORA_ACK------QUery-- --------------" + pst2.getQueryString());
		              ResultSet rs2 = pst2.executeQuery();
		              if (rs2.next()) {
		                oraCount = rs2.getInt("ORM_COUNT");
		              }
		              logger.info("----------ETT_ORA_ACK------ORM_COUNT----------------" + oraCount);
		              closeStatementResultSet(pst2, rs2);
		              if (oraCount == 0)
		              {
		                try
		                {
		                  String insert_query = "INSERT INTO ETT_ORA_ACK(OUTWARDREFERENCENUMBER,ADCODE,CURRENCYCODE,CLOSAMT,IECODE,SWIFTMESSAGE,RECORDINDICATOR,REMARKS,LETTERNO,LETTERDAT,ADJ_SEQNO,DOCNUM,DOCDAT,PORTCODE,ADJCLOINDI,APPROVEDBY,ERRORCODES,FILENAME) VALUES (?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?)";

		 
		 
		                  LoggableStatement pst = new LoggableStatement(con, insert_query);
		                  pst.setString(1, ormNo);
		                  pst.setString(2, adCode);
		                  pst.setString(3, currency);
		                  pst.setString(4, amount);
		                  pst.setString(5, ieCode);
		                  pst.setString(6, swiftMsg);
		                  pst.setString(7, recInd);
		                  pst.setString(8, remark);
		                  pst.setString(9, letterNo);
		                  pst.setString(10, letDate);
		                  pst.setString(11, adjSeqNo);
		                  pst.setString(12, docNo);
		                  pst.setString(13, docDate);
		                  pst.setString(14, portCode);
		                  pst.setString(15, adjInd);
		                  pst.setString(16, appBy);
		                  pst.setString(17, errorCode);
		                  pst.setString(18, fileName);
		                  logger.info("----------xmlUploadIdpmsORAAckFiles------insert_query----------------" + pst.getQueryString());
		                  oraInsertCount += pst.executeUpdate();

		 
		 
		                  logger.info("----------xmlUploadIdpmsORAAckFiles------insert_query---------oraInsertCount-------" + oraInsertCount);
		                  closePreparedStatement(pst);
		                }
		                catch (SQLException se)
		                {
		                  logger.info("------Exception------" + se.getMessage());
		                  oraInsertFailedCount++;
		                  String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                  LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                  pst_err_query.setString(1, ormNo);
		                  pst_err_query.setString(2, adCode);
		                  pst_err_query.setString(3, ieCode);
		                  pst_err_query.setString(4, fileName);
		                  pst_err_query.setString(5, se.getMessage());
		                  pst_err_query.setString(6, "ORA");
		                  pst_err_query.executeUpdate();
		                  closePreparedStatement(pst_err_query);
		                }
		              }
		              else
		              {
		                oraInsertFailedCount++;
		                String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                pst_err_query.setString(1, ormNo);
		                pst_err_query.setString(2, adCode);
		                pst_err_query.setString(3, ieCode);
		                pst_err_query.setString(4, fileName);
		                pst_err_query.setString(5, "AdjustmentSeqNumber & RecordIndicator already Exist");
		                pst_err_query.setString(6, "ORA");
		                pst_err_query.executeUpdate();
		                closePreparedStatement(pst_err_query);
		              }
		            }
		            else
		            {
		              oraInsertFailedCount++;
		              String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		              LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		              pst_err_query.setString(1, ormNo);
		              pst_err_query.setString(2, adCode);
		              pst_err_query.setString(3, ieCode);
		              pst_err_query.setString(4, fileName);
		              pst_err_query.setString(5, "INVALID ORM NUMBER");
		              pst_err_query.setString(6, "ORA");
		              pst_err_query.executeUpdate();
		              closePreparedStatement(pst_err_query);
		            }
		          }
		        }
		        xmlFileVO.setInsertCount(oraInsertCount);
		        xmlFileVO.setBoeCount(oraInsertCount);
		        logger.info("The file gets here only =================>>> " + fileName);
		        if (oraInsertCount > 0)
		        {
		          if (oraInsertFailedCount == 0)
		          {
		            LoggableStatement ps1 = new LoggableStatement(con, "INSERT INTO ETT_ORA_ACK_FILES(FILENO,FILENAME,CREATED_BY) VALUES (ORA_ACK_SEQ.NEXTVAL,?,?)");
		            ps1.setString(1, fileName);
		            ps1.setString(2, userId);
		            logger.info("----------INS_ETT_ORA_ACK_FILES------insert_query------------" + oraInsertCount);
		            int a = ps1.executeUpdate();
		            logger.info("----------INS_ETT_ORA_ACK_FILES------insert_count------------" + a);
		            closeStatementResultSet(ps1, null);
		            xmlFileVO.setBoeCount(oraInsertCount);
		            xmlFileVO.setResult("Y");
		          }
		          else
		          {
		            xmlFileVO.setBoeCount(oraInsertCount);
		            xmlFileVO.setErrCount(oraInsertFailedCount);
		            xmlFileVO.setResult("PL");
		          }
		        }
		        else {
		          xmlFileVO.setResult("ACKFAIL");
		        }
		      }
		      closeStatementResultSet(pst1, rs1);
		    }
		    catch (Exception e)
		    {
		      logger.info("----------xmlUploadIdpmsORAAckFiles--------Exception--------------" + e);
		      throwDAOException(e);
		    }
		    finally
		    {
		      closeConnection(con);
		    }
		    logger.info("Exiting Method");
		    return xmlFileVO;
		  }
		  
		        
		        //xmlUploadIdpmsORAAckFiles
		        
		  public XMLFileVO xmlUploadIdpmsBTTAckFiles(File xmlFileUpload, String fileName, XMLFileVO xmlFileVO)
		  {
		    logger.info("-------------xmlUploadIdpmsBESAckFiles---------------");
		    LoggableStatement pst1 = null;
		    Connection con = null;
		    CommonMethods commonMethods = null;
		    int bttInsertCount = 0;
		    int bttInsertFailedCount = 0;
		    int outref = 0;
		    LoggableStatement loggableStatement = null;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      commonMethods = new CommonMethods();
		      HttpSession session = ServletActionContext.getRequest().getSession();
		      String userId = (String)session.getAttribute("loginedUserId");
		      logger.info("-------------xmlUploadIdpmsBESAckFiles--- -userId-----------" + userId);
		      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		      Document doc = docBuilder.parse(xmlFileUpload);
		      doc.getDocumentElement().normalize();
		      pst1 = new LoggableStatement(con, "SELECT FILENAME FROM ETT_BTT_ACK_FILES WHERE TRIM(FILENAME) = TRIM(?) ");
		      pst1.setString(1, fileName);
		      logger.info("-------------xmlUploadIdpmsBESAckFiles--- -FILENAME------ QUERY-----" + pst1.getQueryString());
		      ResultSet rs1 = pst1.executeQuery();
		      if (rs1.next())
		      {
		        xmlFileVO.setResult("N");
		        logger.info("----------File Name Already Exisit------");
		      }
		      else
		      {
		        logger.info("---------New File-----");
		        NodeList nList = doc.getElementsByTagName("billOfEntry");
		        for (int temp = 0; temp < nList.getLength(); temp++)
		        {
		          Node nNode = nList.item(temp);
		          if (nNode.getNodeType() == 1)
		          {
		            logger.info("--------Loop- Count----" + temp);
		            Element eElement = (Element)nNode;
		            String portCode = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("portOfDischarge").item(0).getTextContent()).trim();

		 
		            String boeNo = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("billOfEntryNumber").item(0).getTextContent()).trim();
		            String boeDate = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("billOfEntryDate").item(0).getTextContent()).trim();
		            String adCode = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("ADCode").item(0).getTextContent()).trim();
		            String gp = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("G-P").item(0).getTextContent()).trim();
		            String ieCode = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("IECode").item(0).getTextContent()).trim();
		            String IeName = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("IEName").item(0).getTextContent()).trim();
		            String ieAddress = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("IEAddress").item(0).getTextContent()).trim();
		            String IEPANNumber = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("IEPANNumber").item(0).getTextContent()).trim();
		            String portOfShipment = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("portOfShipment").item(0).getTextContent()).trim();
		            String igmNumber = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("IGMNumber").item(0).getTextContent()).trim();
		            String igmDate = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("IGMDate").item(0).getTextContent()).trim();
		            String mblNumber = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("MAWB-MBLNumber").item(0).getTextContent()).trim();
		            String mblDate = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("MAWB-MBLDate").item(0).getTextContent()).trim();
		            String hblNumber = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("HAWB-HBLNumber").item(0).getTextContent()).trim();
		            String hblDate = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("HAWB-HBLDate").item(0).getTextContent()).trim();
		            String recordIndicator = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("recordIndicator").item(0).getTextContent()).trim();
		            String noOfDays = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("noOfDays").item(0).getTextContent()).trim();
		            String totalInvoiceAmtIc = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("totalInvoiceAmtIc").item(0).getTextContent()).trim();
		            String realizedInvoiceAmtIc = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("realizedInvoiceAmtIc").item(0).getTextContent()).trim();
		            String unRealizedInvoiceAmtIc = commonMethods.getEmptyIfNull(eElement
		              .getElementsByTagName("unRealizedInvoiceAmtIc").item(0).getTextContent()).trim();

		 
		            NodeList nList2 = eElement.getElementsByTagName("invoice");
		            for (int temp1 = 0; temp1 < nList2.getLength(); temp1++)
		            {
		              Node nNode1 = nList2.item(temp1);
		              if (nNode1.getNodeType() == 1)
		              {
		                Element eElement1 = (Element)nNode1;
		                String inSno = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("invoiceSerialNo").item(0).getTextContent()).trim();
		                String inNo = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("invoiceNo").item(0).getTextContent()).trim();
		                String termsOfInvoice = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("termsOfInvoice").item(0).getTextContent()).trim();
		                String realizedInvoiceAmtIc1 = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("realizedInvoiceAmtIc").item(0).getTextContent()).trim();
		                String supplierName = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("supplierName").item(0).getTextContent()).trim();
		                String supplierAddress = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("supplierAddress").item(0).getTextContent()).trim();
		                String supplierCountry = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("supplierCountry").item(0).getTextContent()).trim();
		                String sellerName = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("sellerName").item(0).getTextContent()).trim();
		                String sellerAddress = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("sellerAddress").item(0).getTextContent()).trim();
		                String sellerCountry = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("sellerCountry").item(0).getTextContent()).trim();
		                String invoiceAmount = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("invoiceAmount").item(0).getTextContent()).trim();
		                String invoiceCurrency = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("invoiceCurrency").item(0).getTextContent()).trim();
		                String freightAmount = commonMethods.getEmptyIfNull(eElement1
		                  .getElementsByTagName("freightAmount").item(0).getTextContent()).trim();
		                String freightCurrencyCode = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("freightCurrencyCode").item(0).getTextContent()).trim();
		                      String insuranceAmount = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("insuranceAmount").item(0).getTextContent()).trim();
		                      String insuranceCurrencyCode = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("insuranceCurrencyCode").item(0).getTextContent()).trim();
		                      String agencyCommission = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("agencyCommission").item(0).getTextContent()).trim();
		                      String agencyCurrency = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("agencyCurrency").item(0).getTextContent()).trim();
		                      String discountCharges = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("discountCharges").item(0).getTextContent()).trim();
		                      String discountCurrency = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("discountCurrency").item(0).getTextContent()).trim();
		                      String miscellaneousCharges = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("miscellaneousCharges").item(0).getTextContent()).trim();
		                      String miscellaneousCurrency = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("miscellaneousCurrency").item(0).getTextContent()).trim();
		                      String thirdPartyName = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("thirdPartyName").item(0).getTextContent()).trim();
		                      String thirdPartyAddress = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("thirdPartyAddress").item(0).getTextContent()).trim();
		                      String thirdPartyCountry = commonMethods.getEmptyIfNull(eElement1
		                        .getElementsByTagName("thirdPartyCountry").item(0).getTextContent()).trim();

		       
		       
		                      int boeCount = 0;
		                      String queryBoe = "SELECT count(*) AS BOE_COUNT FROM ETT_BOE_DETAILS WHERE TRIM(BOE_NUMBER) = '" + 
		                        boeNo.trim() + "' AND TO_DATE(BOE_DATE) = TO_DATE('" + boeDate.trim() + "','DD/MM/YYYY') AND TRIM(BOE_PORT_OF_DISCHARGE) = '" + portCode.trim() + "'";
		                      LoggableStatement pstBoe = new LoggableStatement(con, queryBoe);
		                      ResultSet rsBoe = pstBoe.executeQuery();
		                      if (rsBoe.next())
		                      {
		                        boeCount = rsBoe.getInt("BOE_COUNT");
		                        logger.info("------boeCount------" + boeCount);
		                      }
		                      closeStatementResultSet(pstBoe, rsBoe);
		                      if (boeCount > 0)
		                      {
		                        try
		                        {
		                          int bttCount = 0;
		                          String query = "SELECT COUNT(*) AS BTT_COUNT FROM ETT_BTT_ACK WHERE TRIM(BOENO) = ? AND TRIM(RECORDINDICATOR) = ?  AND TRIM(TOTALINVOICEAMTIC) = ? AND TRIM(REALIZEDINVOICEAMTIC)= ? AND TRIM(UNREALIZEDINVOICEAMTIC) = ? AND INNO=? ";

		       
		                          LoggableStatement pst2 = new LoggableStatement(con, query);
		                          pst2.setString(1, boeNo);
		                          pst2.setString(2, recordIndicator);
		                          pst2.setString(3, totalInvoiceAmtIc);
		                          pst2.setString(4, realizedInvoiceAmtIc);
		                          pst2.setString(5, unRealizedInvoiceAmtIc);
		                          pst2.setString(6, inNo);
		                          logger.info("ETT_BTT_ACK--------------------" + pst2.getQueryString());
		                          ResultSet rs2 = pst2.executeQuery();
		                          if (rs2.next())
		                          {
		                            bttCount = rs2.getInt("BTT_COUNT");
		                            logger.info("ETT_BTT_ACK--------bttCount------------" + bttCount);
		                          }
		                          closeStatementResultSet(pst2, rs2);
		                          if (bttCount == 0)
		                          {
		                            try
		                            {
		                              loggableStatement = new LoggableStatement(con, "INSERT INTO ETT_BTT_ACK (PORTCODE,BOENO,BOEDATE,ADCODE,GP,IECODE,IENAME,IEADDRESS,IEPANNUMBER,PORTOFSHIPMENT,IGMNUMBER,IGMDATE,MBLNUMBER,MBLDATE,HBLNUMBER,HBLDATE, RECORDINDICATOR,NOOFDAYS,TOTALINVOICEAMTIC,REALIZEDINVOICEAMTIC,UNREALIZEDINVOICEAMTIC,INSNO,INNO,TERMSOFINVOICE,REALIZEDINVOICEAMTIC1, SUPPLIERNAME,SUPPLIERADDRESS,SUPPLIERCOUNTRY,SELLERNAME,SELLERADDRESS,SELLERCOUNTRY,INVOICEAMOUNT,INVOICECURRENCY,  FREIGHTAMOUNT,FREIGHTCURRENCYCODE,INSURANCEAMOUNT,INSURANCECURRENCYCODE,AGENCYCOMMISSION,AGENCYCURRENCY,DISCOUNTCHARGES,DISCOUNTCURRENCY,  MISCELLANEOUSCHARGES,MISCELLANEOUSCURRENCY,THIRDPARTYNAME,THIRDPARTYADDRESS,THIRDPARTYCOUNTRY,FILENAME) VALUES(?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,TO_DATE(?,'DD-MM-YY'),?,TO_DATE(?,'DD-MM-YY'), ?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, ?,?,?,?,?,?)");
		                              loggableStatement.setString(1, portCode);
		                              loggableStatement.setString(2, boeNo);
		                              loggableStatement.setString(3, boeDate);
		                              loggableStatement.setString(4, adCode);
		                              loggableStatement.setString(5, gp);
		                              loggableStatement.setString(6, ieCode);
		                              loggableStatement.setString(7, IeName);
		                              loggableStatement.setString(8, ieAddress);
		                              loggableStatement.setString(9, IEPANNumber);
		                              loggableStatement.setString(10, portOfShipment);
		                              loggableStatement.setString(11, igmNumber);
		                              loggableStatement.setString(12, igmDate);
		                              loggableStatement.setString(13, mblNumber);
		                              loggableStatement.setString(14, mblDate);
		                              loggableStatement.setString(15, hblNumber);
		                              loggableStatement.setString(16, hblDate);
		                              loggableStatement.setString(17, recordIndicator);
		                              loggableStatement.setString(18, noOfDays);
		                              loggableStatement.setString(19, totalInvoiceAmtIc);
		                              loggableStatement.setString(20, realizedInvoiceAmtIc);
		                              loggableStatement.setString(21, unRealizedInvoiceAmtIc);
		                              loggableStatement.setString(22, inSno);
		                              loggableStatement.setString(23, inNo);
		                              loggableStatement.setString(24, termsOfInvoice);
		                              loggableStatement.setString(25, realizedInvoiceAmtIc1);
		                              loggableStatement.setString(26, supplierName);
		                              loggableStatement.setString(27, supplierAddress);
		                              loggableStatement.setString(28, supplierCountry);
		                              loggableStatement.setString(29, sellerName);
		                              loggableStatement.setString(30, sellerAddress);
		                              loggableStatement.setString(31, sellerCountry);
		                              loggableStatement.setString(32, invoiceAmount);
		                              loggableStatement.setString(33, invoiceCurrency);
		                              loggableStatement.setString(34, freightAmount);
		                              loggableStatement.setString(35, freightCurrencyCode);
		                              loggableStatement.setString(36, insuranceAmount);
		                              loggableStatement.setString(37, insuranceCurrencyCode);
		                              loggableStatement.setString(38, agencyCommission);
		                              loggableStatement.setString(39, agencyCurrency);
		                              loggableStatement.setString(40, discountCharges);
		                              loggableStatement.setString(41, discountCurrency);
		                              loggableStatement.setString(42, miscellaneousCharges);
		                              loggableStatement.setString(43, miscellaneousCurrency);
		                              loggableStatement.setString(44, thirdPartyName);
		                              loggableStatement.setString(45, thirdPartyAddress);
		                              loggableStatement.setString(46, thirdPartyCountry);
		                              loggableStatement.setString(47, fileName);
		                              logger.info("ETT_BTT_ACK INSERT QUERY--------------------" + loggableStatement.getQueryString());
		                              bttInsertCount += loggableStatement.executeUpdate();
		                              loggableStatement.close();
		                              outref++;
		                              if ((!realizedInvoiceAmtIc.equals("")) || (!realizedInvoiceAmtIc.isEmpty()))
		                              {
		                                logger.info("BEFORE SELECT  ETT_BOE_INV_PAYMENT :: ");
		                                double real_amt = 0.0D;
		                                double real_orm_amt = 0.0D;
		                                String paymentRef = "";
		                                String eventRef = "";
		                                ResultSet rsrRealAmountQuery = null;
		                                String selectRealAmountQuery = " SELECT REAL_AMT,REAL_ORM_AMT,PAYMENT_REF,EVENT_REF FROM ETT_BOE_INV_PAYMENT  WHERE BOE_NO=? AND TO_DATE(BOE_DATE) = TO_DATE(?,'DD/MM/YYYY') AND TRIM(PORTCODE)=?  AND INV_NO=?";

		                                
		                                
		                                LoggableStatement loggableStatementRealAmountQuery = new LoggableStatement(con, selectRealAmountQuery);
		                                loggableStatementRealAmountQuery.setString(1, boeNo.trim());
		                                loggableStatementRealAmountQuery.setString(2, boeDate.trim());
		                                loggableStatementRealAmountQuery.setString(3, portCode.trim());
		                                loggableStatementRealAmountQuery.setString(4, inNo.trim());
		                                rsrRealAmountQuery = loggableStatementRealAmountQuery.executeQuery();
		                                if (rsrRealAmountQuery.next())
		                                {
		                                  real_amt = Double.parseDouble(rsrRealAmountQuery.getString("REAL_AMT"));
		                                  real_orm_amt = Double.parseDouble(rsrRealAmountQuery.getString("REAL_ORM_AMT"));
		                                  paymentRef = rsrRealAmountQuery.getString("PAYMENT_REF");
		                                  eventRef = rsrRealAmountQuery.getString("EVENT_REF");
		                                }
		                                logger.info("AFTER SELECT   real_amt :: " + real_amt);
		                                logger.info("AFTER SELECT   realORM_amt :: " + real_orm_amt);

		       
		                                closeSqlRefferance(rsrRealAmountQuery, loggableStatementRealAmountQuery, null);

		       
		                                logger.info("BEFORE SELECT  ETT_BOE_PAYMENT :: ");
		                                double ends_amt = 0.0D;
		                                double os_amt = 0.0D;
		                                double ex_Rate = 0.0D;
		                                ResultSet rsAmountQuery = null;
		                                String selectAmountQuery = " SELECT BOE_PAYMENT_BP_PAY_ENDORSE_AMT,BOE_PAYMENT_BP_PAY_OS_FC_AMT,EXCHANGE_RATE FROM ETT_BOE_PAYMENT  WHERE TRIM(BOE_PAYMENT_BP_BOE_NO) = ? AND TO_DATE(BOE_PAYMENT_BP_BOE_DT) = TO_DATE(?,'DD/MM/YYYY')  AND TRIM(PORT_CODE) = ? AND TRIM(BOE_PAYMENT_BP_PAY_REF || BOE_PAYMENT_BP_PAY_PART_REF) = ?";

		       
		       
		                                LoggableStatement loggableStatementAmountQuery = new LoggableStatement(con, selectAmountQuery);
		                                loggableStatementAmountQuery.setString(1, boeNo.trim());
		                                loggableStatementAmountQuery.setString(2, boeDate.trim());
		                                loggableStatementAmountQuery.setString(3, portCode.trim());
		                                loggableStatementAmountQuery.setString(4, paymentRef.trim() + eventRef.trim());
		                                rsAmountQuery = loggableStatementAmountQuery.executeQuery();
		                                if (rsAmountQuery.next())
		                                {
		                                  ends_amt = Double.parseDouble(rsAmountQuery.getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT"));
		                                  os_amt = Double.parseDouble(rsAmountQuery.getString("BOE_PAYMENT_BP_PAY_OS_FC_AMT"));
		                                  ex_Rate = Double.parseDouble(rsAmountQuery.getString("EXCHANGE_RATE"));
		                                }
		                                logger.info("AFTER SELECT   ends_amt :: " + ends_amt);
		                                logger.info("AFTER SELECT   os_amt :: " + os_amt);
		                                logger.info("AFTER SELECT   ex_Rate :: " + ex_Rate);
		                                closeSqlRefferance(rsAmountQuery, loggableStatementAmountQuery, null);
		                                NumberFormat format = NumberFormat.getInstance(Locale.US);
		                                Number number = format.parse(realizedInvoiceAmtIc);
		                                double Inv_amt = number.doubleValue();
		                                logger.info("AFTER parse Inv_amt :: " + Inv_amt);
		                                double update_OS_AMT = os_amt - Inv_amt * ex_Rate;
		                                double update_ENDS_AMT = ends_amt + Inv_amt * ex_Rate;

		       
		                                double update_Real_AMT = real_amt + Double.parseDouble(realizedInvoiceAmtIc);
		                                double update_RealOrm_AMT = real_orm_amt + Double.parseDouble(realizedInvoiceAmtIc);
		                                LoggableStatement pst = null;
		                                String updateQuery = " UPDATE ETT_BOE_PAYMENT SET BOE_PAYMENT_BP_PAY_ENDORSE_AMT='" + update_ENDS_AMT + "', BOE_PAYMENT_BP_PAY_OS_FC_AMT='" + update_OS_AMT + "' " + 
		                                  " WHERE TRIM(BOE_PAYMENT_BP_BOE_NO) = '" + boeNo.trim() + "' AND TO_DATE(BOE_PAYMENT_BP_BOE_DT) = TO_DATE('" + boeDate.trim() + "','DD/MM/YYYY') " + 
		                                  " AND TRIM(PORT_CODE) = '" + portCode.trim() + "' AND TRIM(BOE_PAYMENT_BP_PAY_REF || BOE_PAYMENT_BP_PAY_PART_REF) = '" + paymentRef.trim() + eventRef.trim() + "'";
		                                pst = new LoggableStatement(con, updateQuery);
		                                logger.info("--ETT_BOE_PAYMENT UPDATE QUERY----------------" + pst.getQueryString());
		                                int a = pst.executeUpdate();
		                                logger.info("---ETT_IDPMS_EOD_DATA UPDATE QUERY RESULT----------------" + a);
		                                pst.close();
		                                LoggableStatement pstETT_BOE_INV_PAYMENT = null;
		                                String updateQuery1 = " UPDATE ETT_BOE_INV_PAYMENT SET REAL_AMT='" + update_Real_AMT + "', REAL_ORM_AMT='" + update_RealOrm_AMT + "' " + 
		                                  " WHERE TRIM(BOE_NO) = '" + boeNo.trim() + "' AND TO_DATE(BOE_DATE) = TO_DATE('" + boeDate.trim() + "','DD/MM/YYYY') " + 
		                                  " AND TRIM(PORTCODE) = '" + portCode.trim() + "' AND INV_NO = '" + inNo.trim() + "'";
		                                pstETT_BOE_INV_PAYMENT = new LoggableStatement(con, updateQuery1);
		                                logger.info("--ETT_BOE_INV_PAYMENT UPDATE QUERY----------------" + pstETT_BOE_INV_PAYMENT.getQueryString());
		                                int a1 = pstETT_BOE_INV_PAYMENT.executeUpdate();
		                                logger.info("---ETT_IDPMS_EOD_DATA UPDATE QUERY RESULT----------------" + a1);
		                                pstETT_BOE_INV_PAYMENT.close();
		                              }
		                            }
		                            catch (SQLException se)
		                            {
		                              logger.info("Exception-----" + se);
		                              bttInsertFailedCount++;
		                              String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                              LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                              pst_err_query.setString(1, boeNo);
		                              pst_err_query.setString(2, adCode);
		                              pst_err_query.setString(3, ieCode);
		                              pst_err_query.setString(4, fileName);
		                              pst_err_query.setString(5, se.getMessage());
		                              pst_err_query.setString(6, "BTT");
		                              pst_err_query.executeUpdate();
		                              closePreparedStatement(pst_err_query);
		                            }
		                            catch (Exception e)
		                            {
		                                logger.info("Exception-----" + e);
		                                bttInsertFailedCount++;
		                                String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                                LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                                pst_err_query.setString(1, boeNo);
		                                pst_err_query.setString(2, adCode);
		                                pst_err_query.setString(3, ieCode);
		                                pst_err_query.setString(4, fileName);
		                                pst_err_query.setString(5, e.getMessage());
		                                pst_err_query.setString(6, "BTT");
		                                pst_err_query.executeUpdate();
		                                closePreparedStatement(pst_err_query);
		                              }
		                              finally
		                              {
		                                if (loggableStatement != null) {
		                                  loggableStatement.close();
		                                }
		                              }
		                            }
		                            else
		                            {
		                              bttInsertFailedCount++;
		                              String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                              LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                              pst_err_query.setString(1, boeNo);
		                              pst_err_query.setString(2, adCode);
		                              pst_err_query.setString(3, ieCode);
		                              pst_err_query.setString(4, fileName);
		                              pst_err_query.setString(5, "BOE NUMBER & RecordIndicator already Exist");
		                              pst_err_query.setString(6, "BTT");
		                              pst_err_query.executeUpdate();
		                              closePreparedStatement(pst_err_query);
		                            }
		                          }
		                          catch (Exception e)
		                          {
		                            logger.info("Exception-----" + e);
		                            bttInsertFailedCount++;
		                            String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                            LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                            pst_err_query.setString(1, boeNo);
		                            pst_err_query.setString(2, adCode);
		                            pst_err_query.setString(3, ieCode);
		                            pst_err_query.setString(4, fileName);
		                            pst_err_query.setString(5, e.getMessage());
		                            pst_err_query.setString(6, "BTT");
		                            pst_err_query.executeUpdate();
		                            closePreparedStatement(pst_err_query);
		                          }
		                        }
		                        else
		                        {
		                          bttInsertFailedCount++;
		                          String insert_Err_query = "INSERT INTO ETT_ORM_BES_BEE_ERROR_DETAILS(OUTWARDREFERNECNO,ADCODE,IECODE,FILENAME,ERROR_DESC,ERR_FILE_TYPE) VALUES (?,?,?,?,?,?)";
		                          LoggableStatement pst_err_query = new LoggableStatement(con, insert_Err_query);
		                          pst_err_query.setString(1, boeNo);
		                          pst_err_query.setString(2, adCode);
		                          pst_err_query.setString(3, ieCode);
		                          pst_err_query.setString(4, fileName);
		                          pst_err_query.setString(5, "Invalid BOE Number");
		                          pst_err_query.setString(6, "BES");
		                          pst_err_query.executeUpdate();
		                          closePreparedStatement(pst_err_query);
		                        }
		                      }
		                    }
		                  }
		                }
		                int i = xmlFileVO.setInsertCount(bttInsertCount);
		                logger.info("No.of BOEs ---------------------------------------- :" + i);
		                logger.info("Insertion Count-----------------" + bttInsertCount);
		                xmlFileVO.setBoeCount(bttInsertCount);
		                if (bttInsertCount > 0)
		                {
		                  if (bttInsertFailedCount == 0)
		                  {
		                    logger.info("Insertion Count----Record Inserted-------------" + bttInsertCount);
		                    LoggableStatement ps1 = new LoggableStatement(con, "INSERT INTO ETT_BTT_ACK_FILES(FILENO,FILENAME,CREATED_BY) VALUES (BES_ACK_SEQ.NEXTVAL,?,?)");
		                    ps1.setString(1, fileName);
		                    ps1.setString(2, userId);
		                    logger.info(" INS_ETT_BES_ACK_FILESQuery-" + ps1.getQueryString());
		                    int a = ps1.executeUpdate();
		                    logger.info("INS_ETT_BES_ACK_FILESQuery----------executeUpdate-" + a);
		                    closeStatementResultSet(ps1, null);
		                    xmlFileVO.setBoeCount(outref);
		                    xmlFileVO.setResult("Y");
		                  }
		                  else
		                  {

		                      xmlFileVO.setBoeCount(outref);

		                      xmlFileVO.setErrCount(bttInsertFailedCount);

		                      xmlFileVO.setResult("PL");

		                    }

		                  }

		                  else

		                  {

		                    logger.info("Insertion Count----Record Not Inserted-------------");

		                    xmlFileVO.setResult("ACKFAIL");

		                  }

		                }

		                rs1.close();

		              }

		              catch (Exception exception)

		              {

		                logger.info("-------------xmlUploadIdpmsBESAckFiles--- -exception-----------" + exception);

		                exception.printStackTrace();

		              }

		              finally

		              {

		                closeSqlRefferance(pst1, con);

		              }

		              logger.info("Exiting Method");

		              return xmlFileVO;

		            }

		            public void insertOBE_TEMP_STG_Data(Connection con, BOEDetailsVO boeVO, OBEInvoiceDetailsVO invoiceVO) throws DAOException

		            {

		              logger.info("Entering Method");

		              LoggableStatement pst = null;

		              int noBOE = 0;

		              try

		              {

		                logger.info("Insert ETT_BOE_DETAILS_TEMP_STG");

		                if (con == null) {

		                  con = DBConnectionUtility.getConnection();

		                }

		                pst = new LoggableStatement(con, "INSERT INTO ETT_BOE_DETAILS_TEMP_STG(BOE_NUMBER,BOE_DATE,BOE_PORT_OF_DISCHARGE,BOE_IMPORT_AGENCY,BOE_AD_CODE,BOE_GP,BOE_IE_CODE,BOE_IE_NAME,BOE_IE_ADDRESS,BOE_IE_PANNUMBER,BOE_PORT_OF_SHIPMENT,BOE_RECORD_INDICATOR,BOE_MAWB_MBLNUMBER,BOE_MAWB_MBLDATE,BOE_HAWB_HBLNUMBER,BOE_HAWB_HBLDATE,BOE_IGMNUMBER,BOE_IGMDATE,INVOICE_SERIAL_NUMBER,INVOICE_NUMBER,INVOICE_TERMS_OF_INVOICE,INVOICE_SUPPLIER_NAME,INVOICE_SUPPLIER_ADDRESS,INVOICE_SUPPLIER_COUNTRY,INVOICE_SELLER_NAME,INVOICE_SELLER_ADDRESS,INVOICE_SELLER_COUNTRY,INVOICE_FOBAMOUNT,INVOICE_FOBCURRENCY,INVOICE_FRIEGHTAMOUNT,INVOICE_FRIEGHTCURRENCYCODE,INVOICE_INSURANCEAMOUNT,INVOICE_INSURANCECURRENCY_CODE,INVOICE_AGENCY_COMMISSION,INVOICE_AGENCY_CURRENCY,INVOICE_DISCOUNT_CHARGES,INVOICE_DISCOUNT_CURRENCY,INVOICE_MISCELLANEOUS_CHARGES,INVOICE_MISCELLANEOUS_CURRENCY,INVOICE_THIRDPARTY_NAME,INVOICE_THIRDPARTY_ADDRESS,INVOICE_THIRDPARTY_COUNTRY,FILENO,ERROR_CODE)VALUES (?,TO_DATE(?,'DD/MM/YY'),?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YY'),?,TO_DATE(?,'DD/MM/YY'),?,TO_DATE(?,'DD/MM/YY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		                pst.setString(1, boeVO.getBoeNo());

		                pst.setString(2, boeVO.getBoeDate());

		                pst.setString(3, boeVO.getPortCode());

		                pst.setString(4, boeVO.getImportAgency());

		                pst.setString(5, boeVO.getAdCode());

		                pst.setString(6, boeVO.getGpFirm());

		                pst.setString(7, boeVO.getIeCode());

		                pst.setString(8, boeVO.getIeName());

		                pst.setString(9, boeVO.getIeAddress());

		                pst.setString(10, boeVO.getIePanNo());

		                pst.setString(11, boeVO.getPortShipment());

		                pst.setString(12, boeVO.getRecIndicator());

		                pst.setString(13, boeVO.getMblNo());

		                pst.setString(14, boeVO.getMblDate());

		                pst.setString(15, boeVO.getHblNo());

		                pst.setString(16, boeVO.getHblDate());

		                pst.setString(17, boeVO.getIgmNo());

		                pst.setString(18, boeVO.getIgmDate());

		                pst.setString(19, invoiceVO.getInvoiceSerialNo());

		                pst.setString(20, invoiceVO.getInvoiceNo());

		                pst.setString(21, invoiceVO.getInvoiceTerms());

		                pst.setString(22, invoiceVO.getSupplierName());

		                pst.setString(23, invoiceVO.getSupplierAddr());

		                pst.setString(24, invoiceVO.getSupplierCountry());

		                pst.setString(25, invoiceVO.getSellerName());

		                pst.setString(26, invoiceVO.getSellerAddr());

		                pst.setString(27, invoiceVO.getSellerCountry());

		                pst.setString(28, invoiceVO.getInvoiceAmt());

		                pst.setString(29, invoiceVO.getInvoiceCurr());

		                pst.setString(30, invoiceVO.getFreightAmt());

		                pst.setString(31, invoiceVO.getFreightCurr());

		                pst.setString(32, invoiceVO.getInsAmt());

		                pst.setString(33, invoiceVO.getInsCurr());

		                pst.setString(34, invoiceVO.getAgencyComm());

		                pst.setString(35, invoiceVO.getAgencyCurr());

		                pst.setString(36, invoiceVO.getDiscountAmt());

		                pst.setString(37, invoiceVO.getDiscountCurr());

		                pst.setString(38, invoiceVO.getMiscAmt());

		                pst.setString(39, invoiceVO.getMiscCurr());

		                pst.setString(40, invoiceVO.getThirdPartyName());

		                pst.setString(41, invoiceVO.getThirdPartyAddr());

		                pst.setString(42, invoiceVO.getThirdPartyCountry());

		                pst.setInt(43, boeVO.getFileNo());

		                pst.setString(44, boeVO.getErrorString());

		           
		           
		                noBOE = pst.executeUpdate();

		                pst.close();

		                logger.info("NoOfBOE - " + boeVO.getBoeNo());

		              }

		              catch (Exception exception)

		              {

		                throwDAOException(exception);

		              }

		              logger.info("Exiting Method");

		            }

		            public XMLFileVO getErrorListDownload(XMLFileVO xmlFileVO)

		              throws DAOException

		            {

		              logger.info("----------getErrorListDownload-----------------");

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

		                int iCnt = checkFileExistance(fileName1);

		                xmlFileVO.setFileCount(iCnt);

		                if (iCnt == 2)

		                {

		                  sQuery = "SELECT TMP.BOE_NUMBER,TO_CHAR(TMP.BOE_DATE,'DD/MM/YYYY') AS BOE_DATE,TMP.BOE_PORT_OF_DISCHARGE, TMP.INVOICE_SERIAL_NUMBER,TMP.INVOICE_NUMBER,TMP.ERROR_CODE FROM ETT_BOE_DETAILS_TEMP_STG TMP, ETT_IDPMS_FILES F WHERE TMP.FILENO = F.FILENO AND F.FILENAME = ? AND ERROR_CODE IS NOT NULL";

		           
		                  ps = new LoggableStatement(con, sQuery);

		                  ps.setString(1, fileName1);
		                  logger.info("----------getErrorListDownload-----query------------" + ps.getQueryString());
		                  rs = ps.executeQuery();
		                  while (rs.next())
		                  {
		                    ExcelDataVO excelDataVO = new ExcelDataVO();
		                    excelDataVO.setBoeNo(rs.getString("BOE_NUMBER"));
		                    excelDataVO.setBoeDate(rs.getString("BOE_DATE"));
		                    excelDataVO.setPortCode(rs.getString("BOE_PORT_OF_DISCHARGE"));
		                    excelDataVO.setInvSerialNo(rs.getString("INVOICE_SERIAL_NUMBER"));
		                    excelDataVO.setInvNo(rs.getString("INVOICE_NUMBER"));
		                    excelDataVO.setBoeErrorString(rs.getString("ERROR_CODE"));
		                    xmlErrorList.add(excelDataVO);
		                  }
		                  xmlFileVO.setXmlErrorList(xmlErrorList);
		                }
		                else
		                {
		                  return xmlFileVO;
		                }
		              }
		              catch (Exception exception)
		              {
		                logger.info("----------getErrorListDownload-----Exctption------------" + exception);
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
		              logger.info("Entering Method");
		              ArrayList<XMLFileVO> fileList = null;
		              String sQuery = "";
		              String sCountQuery = null;
		              String fileName1 = null;
		              int iCnt = 0;
		              Connection con = null;
		              LoggableStatement ps = null;
		              ResultSet rs = null;
		              String sCountQuery2 = null;
		              LoggableStatement ps2 = null;
		              LoggableStatement ps1 = null;
		              LoggableStatement ps3 = null;
		              ResultSet rs2 = null;
		              ResultSet rs1 = null;
		              ResultSet rs3 = null;
		              CommonMethods commonMethods = null;
		              try
		              {
		                commonMethods = new CommonMethods();
		                fileList = new ArrayList();
		                con = DBConnectionUtility.getConnection();
		                fileName1 = xmlFileVO.getFileName1();

		           
		           
		                sCountQuery = "SELECT COUNT(1) AS CNT FROM ETT_IDPMS_FILES WHERE FILENAME =?";
		                ps = new LoggableStatement(con, sCountQuery);
		                ps.setString(1, fileName1);
		                logger.info("---------------Count Query--------" + ps.getQueryString());
		                rs = ps.executeQuery();
		                if (rs.next())
		                {
		                  iCnt = rs.getInt("CNT");
		                  logger.info("---------------Count Query---Count-----" + iCnt);
		                }
		                if (iCnt != 0)
		                {
		                  sQuery = "SELECT FILENO, FILENAME FROM ETT_IDPMS_FILES WHERE FILENAME=? ORDER BY FILENAME ASC";

		           
		                  ps1 = new LoggableStatement(con, sQuery);
		                  ps1.setString(1, fileName1);
		                  logger.info("---------------Count Query---q-----" + ps1.getQueryString());
		                  rs1 = ps1.executeQuery();
		                  while (rs1.next())
		                  {
		                    XMLFileVO xmlFileVO1 = new XMLFileVO();
		                    xmlFileVO1.setFileNumber(rs1.getInt("FILENO"));
		                    logger.info("------FILENAME---------Count Query---q-----" + rs1.getInt("FILENO"));
		                    xmlFileVO1.setFileName1(rs1.getString("FILENAME"));
		                    xmlFileVO1.setErrDesc("");
		                    logger.info("-----------FILENO----Count Query---q-----" + rs1.getString("FILENAME"));
		                    fileList.add(xmlFileVO1);
		                  }
		                  xmlFileVO.setFileList(fileList);
		                }
		                else
		                {
		                  sCountQuery2 = "select COUNT(1) AS CNT1 from ETT_ORM_BES_BEE_ERROR_DETAILS where FILENAME=?";
		                  ps2 = new LoggableStatement(con, sCountQuery2);
		                  ps2.setString(1, fileName1);
		                  logger.info("---------------Count Query--------" + ps2.getQueryString());
		                  rs2 = ps2.executeQuery();
		                  if (rs2.next())
		                  {
		                    iCnt = rs2.getInt("CNT1");
		                    logger.info("---------------Count Query---Count-----" + iCnt);
		                  }
		                  if (iCnt != 0)
		                  {
		                    sQuery = "SELECT ROW_NUMBER() OVER (PARTITION BY 1 ORDER BY OUTWARDREFERNECNO DESC) as FILENO, FILENAME,OUTWARDREFERNECNO||'-'||ERROR_DESC as ERROR_DESC FROM ETT_ORM_BES_BEE_ERROR_DETAILS WHERE FILENAME like ? ORDER BY FILENAME ASC";

		           
		                    ps3 = new LoggableStatement(con, sQuery);
		                    ps3.setString(1, fileName1 + "%");
		                    logger.info("---------------Count Query---q-----" + ps3.getQueryString());
		                    rs3 = ps3.executeQuery();
		                    while (rs3.next())
		                    {
		                      XMLFileVO xmlFileVO1 = new XMLFileVO();
		                      xmlFileVO1.setFileNumber(rs3.getInt("FILENO"));
		                      xmlFileVO1.setFileName1(rs3.getString("FILENAME"));
		                      xmlFileVO1.setErrDesc(rs3.getString("ERROR_DESC"));
		                      fileList.add(xmlFileVO1);
		                    }
		                    xmlFileVO.setFileList(fileList);
		                  }
		                }
		                xmlFileVO.setFileCount(iCnt);
		              }
		              catch (Exception exception)
		              {
		                logger.info("-----------FILENO----Exception----" + exception);
		                throwDAOException(exception);
		              }
		              finally
		              {
		                DBConnectionUtility.surrenderDB(con, ps, rs);
		                DBConnectionUtility.surrenderDB(con, ps1, rs1);
		                DBConnectionUtility.surrenderDB(con, ps2, rs2);
		                DBConnectionUtility.surrenderDB(con, ps3, rs3);
		              }
		              logger.info("Exiting Method");
		              return xmlFileVO;
		            }
		            public int checkFileExistance(String fileName)
		              throws DAOException
		            {
		              logger.info("Entering Method");
		              int iCnt = 0;
		              String sQuery = null;
		              String sProcName = null;
		              String sFileCount = "";
		              CallableStatement cs = null;
		              Connection con = null;
		              LoggableStatement ps = null;
		              ResultSet rs = null;
		              CommonMethods commonMethods = null;
		              try
		              {
		                commonMethods = new CommonMethods();
		                con = DBConnectionUtility.getConnection();
		                if (!commonMethods.isNull(fileName))
		                {
		                  sProcName = "{call ETT_GET_FILE_COUNT(?, ?)}";
		                  cs = con.prepareCall(sProcName);
		                  cs.setString(1, commonMethods.getEmptyIfNull(fileName).trim());
		                  cs.registerOutParameter(2, 4);
		                  if (cs.executeUpdate() >= 0) {
		                    iCnt = cs.getInt(2);
		                  }
		                }
		              }
		              catch (Exception exception)
		              {
		                throwDAOException(exception);
		              }
		              finally
		              {
		                DBConnectionUtility.surrenderDB(con, cs, null);
		              }
		              logger.info("Exiting Method");
		              return iCnt;
		            }
		            public BOEDetailsVO compareBOECount(BOEDetailsVO boeVO1)
		            {
		              logger.info("Entering Method");
		              int iHeaderBOECount = 0;
		              int iHeaderINVCount = 0;
		              int iContentBOECount = 0;
		              int iContentINVCount = 0;
		              String errMsg = null;
		              iHeaderBOECount = boeVO1.getHeaderBOECount();
		              iHeaderINVCount = boeVO1.getHeaderInvCount();
		              iContentBOECount = boeVO1.getContentBOECount();
		              iContentINVCount = boeVO1.getContentInvCount();
		              logger.info("BOE : " + iHeaderBOECount + " : INV : " + iHeaderINVCount + " : CBOE : " + iContentBOECount + " : CINV : " + iContentINVCount);
		              if (iHeaderBOECount == iContentBOECount)
		              {
		                if (iHeaderINVCount == iContentINVCount) {
		                  boeVO1.setCountResult("S");
		                } else {
		                  boeVO1.setCountResult("INV");
		                }
		              }
		              else
		              {
		                if (iHeaderINVCount == iContentINVCount) {
		                  errMsg = "BOE";
		                } else {
		                  errMsg = "BOTH";
		                }
		                boeVO1.setCountResult(errMsg);
		              }
		              logger.info("Exiting Method");
		              return boeVO1;
		            }
		            public XMLFileVO validateMDFXMLTags(File xmlFileUpload, String fileName)
		            {
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
		              logger.info("xmlfile" + this.xmlFileVO1);
		              return this.xmlFileVO1;
		            }
		            public XMLFileVO getORMACKErrorListDownload(XMLFileVO xmlFileVO)
		              throws DAOException
		            {
		              logger.info("----------getORMACKErrorListDownload-----------------");
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
		                logger.info("----------getORMACKErrorListDownload-----Filename------------" + fileName1);
		                sQuery = "select * from ETT_ORM_BES_BEE_ERROR_DETAILS where FILENAME=?";
		                ps = new LoggableStatement(con, sQuery);
		                ps.setString(1, fileName1);
		                logger.info("----------getORMACKErrorListDownload-----query------------" + ps.getQueryString());
		                rs = ps.executeQuery();
		                while (rs.next())
		                {
		                  ExcelDataVO excelDataVO = new ExcelDataVO();
		                  excelDataVO.setOUTWARDREFERNECNO(rs.getString("OUTWARDREFERNECNO"));
		                  excelDataVO.setADCODE(rs.getString("ADCODE"));
		                  excelDataVO.setIECODE(rs.getString("IECODE"));
		                  excelDataVO.setFILENAME(rs.getString("FILENAME"));
		                  excelDataVO.setERROR_DESC(rs.getString("ERROR_DESC"));
		                  xmlErrorList.add(excelDataVO);
		                }
		                xmlFileVO.setXmlOrmAckErrorList(xmlErrorList);
		              }
		              catch (Exception exception)
		              {
		                logger.info("----------getORMACKErrorListDownload-----Exctption------------" + exception);
		                throwDAOException(exception);
		              }
		              finally
		              {
		                closeSqlRefferance(rs, ps, con);
		              }
		              logger.info("Exiting Method");
		              return xmlFileVO;
		            }
		            public int checkLoginedUserType(XMLFileVO xmlFileVO)
		              throws DAOException
		            {
		              logger.info("Entering Method");
		              Connection con = null;
		              LoggableStatement loggableStatement = null;
		              ResultSet rs = null;
		              int count = 0;
		              String sqlQuery = null;
		              CommonMethods commonMethods = null;
		              try
		              {
		                commonMethods = new CommonMethods();
		                con = DBConnectionUtility.getConnection();
		                if (!commonMethods.isNull(xmlFileVO.getSessionUserName()))
		                {
		                  sqlQuery = 
		                    "SELECT COUNT(*) AS LOGIN_COUNT FROM SECAGE88 U,TEAMUSRMAP T WHERE T.USERKEY = U.SKEY80 AND TRIM(UPPER(U.NAME85))  = TRIM(UPPER('" + xmlFileVO.getSessionUserName() + "'))" + " AND TRIM(UPPER(T.TEAMKEY)) = TRIM(UPPER('" + xmlFileVO.getPageType() + "'))";
		                  loggableStatement = new LoggableStatement(con, sqlQuery);
		                  logger.info("Checker Stage QUery--------------->" + loggableStatement.getQueryString());
		                  rs = loggableStatement.executeQuery();
		                  if (rs.next()) {
		                    count = rs.getInt("LOGIN_COUNT");
		                  }
		                  logger.info("Checker Stage QUery-----Count---------->" + count);
		                }
		              }
		              catch (Exception exception)
		              {
		                throwDAOException(exception);
		              }
		              finally
		              {
		                closeSqlRefferance(rs, loggableStatement, con);
		              }
		              return count;
		            }
		          }