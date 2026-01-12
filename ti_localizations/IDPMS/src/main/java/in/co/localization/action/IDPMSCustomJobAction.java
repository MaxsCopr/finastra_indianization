package in.co.localization.action;
 
import in.co.localization.businessdelegate.localization.AcknowledgementBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.CommonMethods;

import in.co.localization.utility.DBConnectionUtility;

import in.co.localization.utility.LoggableStatement;

import in.co.localization.utility.ServiceLoggingUtil;

import in.co.localization.vo.localization.AcknowledgementVO;

import in.co.localization.vo.localization.EodDownloadVO;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.PrintStream;

import java.io.PrintWriter;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.ResultSetMetaData;

import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;

import org.apache.struts2.ServletActionContext;

import org.w3c.dom.Document;

import org.w3c.dom.Element;
 
public class IDPMSCustomJobAction

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(IDPMSCustomJobAction.class.getName());

  private static final long serialVersionUID = 1L;

  private InputStream inputStream;

  private String fileName;

  private long contentLength;

  String idpmsFileName = null;

  String idpmsFileType = null;

  String eodDate = null;

  private EodDownloadVO vo;

  public EodDownloadVO getVo()

  {

    return this.vo;

  }

  public void setVo(EodDownloadVO vo)

  {

    this.vo = vo;

  }

  public InputStream getInputStream()

  {

    return this.inputStream;

  }

  public void setInputStream(InputStream inputStream)

  {

    this.inputStream = inputStream;

  }

  public String getFileName()

  {

    return this.fileName;

  }

  public void setFileName(String fileName)

  {

    this.fileName = fileName;

  }

  public long getContentLength()

  {

    return this.contentLength;

  }

  public void setContentLength(long contentLength)

  {

    this.contentLength = contentLength;

  }

  public String getIdpmsFileName()

  {

    return this.idpmsFileName;

  }

  public void setIdpmsFileName(String idpmsFileName)

  {

    this.idpmsFileName = idpmsFileName;

  }

  public String getIdpmsFileType()

  {

    return this.idpmsFileType;

  }

  public void setIdpmsFileType(String idpmsFileType)

  {

    this.idpmsFileType = idpmsFileType;

  }

  public String getEodDate()

  {

    return this.eodDate;

  }

  public void setEodDate(String eodDate)

  {

    this.eodDate = eodDate;

  }

  public String execute()

    throws ApplicationException

  {

    try

    {

      isSessionAvailable();

      this.vo = new EodDownloadVO();

    }

    catch (Exception exception)

    {

      exception.printStackTrace();

    }

    return "success";

  }

  public String landingPage()

    throws IOException

  {

    String sessionUserName = null;

    AcknowledgementVO ackVO = null;

    AcknowledgementBD bd = null;

    String target = null;

    try

    {

      ackVO = new AcknowledgementVO();

      isSessionAvailable();

      HttpSession session = ServletActionContext.getRequest().getSession();

      sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("Maker sessionUserName  : " + sessionUserName);

      if (sessionUserName != null)

      {

        logger.info("Inside outer session name");

        bd = new AcknowledgementBD();

        logger.info("sessionUserName--->" + sessionUserName);

        ackVO.setSessionUserName(sessionUserName);

        ackVO.setPageType("IDPMS TEAM DFB");

        int count = bd.checkLoginedUserType(ackVO);

        logger.info("Count Session Name for Checker Eligiblity");

        if (count > 0) {

          target = "success";

        } else {

          target = "fail";

        }

        logger.info("target-------------" + target);

      }

      else

      {

        target = "fail";

      }

    }

    catch (Exception e)

    {

      logger.info("IDPMS FILE Upload Exception------------>" + e);

    }

    return target;

  }

  public int seqGeneration(String sequence)

  {

    int seq = 0;

    Connection con = null;

    LoggableStatement pst = null;

    ResultSet rs = null;

    String seq_query = null;

    try

    {

      con = DBConnectionUtility.getConnection();

      seq_query = "SELECT " + sequence + ".NEXTVAL AS SEQNO FROM DUAL";

      pst = new LoggableStatement(con, seq_query);

      logger.info(pst.getQueryString());

      rs = pst.executeQuery();

      while (rs.next())

      {

        seq = rs.getInt("SEQNO");

        logger.info("Sequence Number----->" + seq);

      }

    }

    catch (Exception e)

    {

      logger.info("Excpetion in SeqGeneration Method" + e.getMessage());

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, pst, rs);

    }

    return seq;

  }

  public String landingPage1()

    throws IOException

  {

    try

    {

      isSessionAvailable();

    }

    catch (Exception e)

    {

      logger.info("File Download Landing Exception------------>" + e);

    }

    return "success";

  }

  public String downloadORMFile()

  {

    logger.info("----------IDPMS ------ORMDWLD--- [downloadORMFile]- ------------------- idpmsFileType :: " + this.idpmsFileType + " :: idpmsFileName :: " + this.idpmsFileName + " :: eodDate :: " + this.eodDate);

    Connection con = null;

    LoggableStatement ps = null;

    LoggableStatement pst = null;

    ResultSet res = null;

    int noOfOrm = 0;

    int xmlCount = 0;

    String result = "success";

    String fileName1 = "";

    String myFilePath = "";

    try

    {

      con = DBConnectionUtility.getConnection();
      String proQuery = "SELECT TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'YYYYMMDD') AS PROCDATE," + 

        "ORMXML_SEQNO.NEXTVAL AS ORM_SEQNO,TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'DD-MM-YY') AS EODFINISHEDDATE FROM DUAL";

      PreparedStatement pps1 = con.prepareStatement(proQuery);

      ResultSet rs1 = pps1.executeQuery();

      String prodate = null;

      String eodFinishedDate = null;

      int seq = 0;

      if (rs1.next())

      {

        prodate = rs1.getString("PROCDATE");

        seq = rs1.getInt("ORM_SEQNO");

        eodFinishedDate = rs1.getString("EODFINISHEDDATE");

      }

      closeSqlRefferance(rs1, pps1);

      logger.info("----------IDPMS ------ORMDWLD--- [downloadORMFile]- ------------------- prodate :: " + prodate + " :: seq :: " + seq + " :: eodFinishedDate :: " + eodFinishedDate);

      if (this.idpmsFileType.equalsIgnoreCase("XML"))

      {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        Element root = document.createElement("bank");

        document.appendChild(root);

        Element rocdoc = document.createElement("checkSum");

        root.appendChild(rocdoc);

        Element rocdoc2 = document.createElement("outwardReferences");

        root.appendChild(rocdoc2);

        String query = "SELECT TRIM(OUTWARDREFERENCENUMBER) AS OUTWARDREFERENCENUMBER,TRIM(ADCODE) AS ADCODE,TRIM(AMOUNT) AS AMOUNT,TRIM(CURRENCYCODE) AS CURRENCYCODE,TO_CHAR(PAYMENTDATE,'DD/MM/YYYY') AS PAYMENTDATE ,TRIM(IECODE) AS IECODE,TRIM(IENAME) AS IENAME,TRIM(IEADDRESS) AS IEADDRESS,TRIM(PANNUMBER) AS PANNUMBER,TRIM(ISCAPITALGOODS) AS ISCAPITALGOODS,TRIM(BENEFICIARYNAME) AS BENEFICIARYNAME,TRIM(BENEFICIARYACCOUNTNUMBER) AS BENEFICIARYACCOUNTNUMBER,TRIM(BENEFICIARYCOUNTRY) AS BENEFICIARYCOUNTRY,TRIM(SWIFTMESSAGE) AS SWIFTMESSAGE, TRIM(PURPOSECODE) AS PURPOSECODE, TRIM(REMARKS) AS REMARKS,TRIM(PAYMENTTERMS) AS PAYMENTTERMS FROM ETT_IDPMS_EOD_DATA WHERE EOD_DATE = '" + 

 
 
          eodFinishedDate + "'";

        ps = new LoggableStatement(con, query);

        logger.info("----------IDPMS ------ORMDWLD--- [downloadORMFile]- ---------XML_DATA_QUERY----------" + query);

 
 
        res = ps.executeQuery();

        while (res.next())

        {

          String outwardRefNo1 = setValue(res.getString("OUTWARDREFERENCENUMBER"));

          String outwardRefNo = setValue(res.getString("OUTWARDREFERENCENUMBER"));

          outwardRefNo = outwardRefNo.replaceAll("[^A-Za-z0-9 ]", "");

          String adCode = setValue(res.getString("ADCODE"));

          String amount = setValue(res.getString("AMOUNT"));

          String currencyCode = setValue(res.getString("CURRENCYCODE"));

          String paymentDate = setValue(res.getString("PAYMENTDATE"));

          String ieCode = setValue(res.getString("IECODE"));

          String ieName = setValue(res.getString("IENAME"));

          if (ieName != null) {

            ieName = ieName.replaceAll("[^A-Za-z0-9 ]", "");

          }

          String ieAddress = setValue(res.getString("IEADDRESS"));

          if (ieAddress != null) {

            ieAddress = ieAddress.replaceAll("[^A-Za-z0-9 ]", "");

          }

          String panNo = setValue(res.getString("PANNUMBER"));

          String isCaptial = setValue(res.getString("ISCAPITALGOODS"));

          String benefName = setValue(res.getString("BENEFICIARYNAME"));

          if (benefName != null) {

            benefName = benefName.replaceAll("[^A-Za-z0-9 ]", "");

          }

          String benfAccNo = setValue(res.getString("BENEFICIARYACCOUNTNUMBER"));

          String benefCty = setValue(res.getString("BENEFICIARYCOUNTRY"));

          String swiftMsg = setValue(res.getString("SWIFTMESSAGE"));

          String purpose = setValue(res.getString("PURPOSECODE"));

          String remarks = setValue(res.getString("REMARKS"));

          String payTerms = setValue(res.getString("PAYMENTTERMS"));

          Element rocdoc1 = document.createElement("outwardReference");

          rocdoc2.appendChild(rocdoc1);

          Element pc = document.createElement("outwardReferenceNumber");

          pc.appendChild(document.createTextNode(outwardRefNo));

          rocdoc1.appendChild(pc);

          Element ri = document.createElement("ADCode");

          ri.appendChild(document.createTextNode(adCode));

          rocdoc1.appendChild(ri);

          Element et = document.createElement("Amount");

          et.appendChild(document.createTextNode(amount));

          rocdoc1.appendChild(et);

          Element sbn = document.createElement("currencyCode");

          sbn.appendChild(document.createTextNode(currencyCode));

          rocdoc1.appendChild(sbn);

          Element sbd = document.createElement("paymentDate");

          sbd.appendChild(document.createTextNode(paymentDate));

          rocdoc1.appendChild(sbd);

          Element fn = document.createElement("IECode");

          fn.appendChild(document.createTextNode(ieCode));

          rocdoc1.appendChild(fn);

          Element ld = document.createElement("IEName");

          ld.appendChild(document.createTextNode(ieName));

          rocdoc1.appendChild(ld);

          Element ic = document.createElement("IEAddress");

          ic.appendChild(document.createTextNode(ieAddress));

          rocdoc1.appendChild(ic);

          Element cic = document.createElement("IEPANNumber");

          cic.appendChild(document.createTextNode(panNo));

          rocdoc1.appendChild(cic);

          Element ac = document.createElement("isCapitalGoods");

          ac.appendChild(document.createTextNode(isCaptial));

          rocdoc1.appendChild(ac);

          Element aea = document.createElement("beneficiaryName");

          aea.appendChild(document.createTextNode(benefName));

          rocdoc1.appendChild(aea);

          Element dd = document.createElement("beneficiaryAccountNumber");

          dd.appendChild(document.createTextNode(benfAccNo));

          rocdoc1.appendChild(dd);

          Element abn = document.createElement("beneficiaryCountry");

          abn.appendChild(document.createTextNode(benefCty));

          rocdoc1.appendChild(abn);

          Element dn = document.createElement("SWIFT");

          dn.appendChild(document.createTextNode(swiftMsg));

          rocdoc1.appendChild(dn);

          Element bn = document.createElement("purposeCode");

          bn.appendChild(document.createTextNode(purpose));

          rocdoc1.appendChild(bn);

 
 
 
          Element remark = document.createElement("remarks");

          remark.appendChild(document.createTextNode(remarks));

          rocdoc1.appendChild(remark);

          Element term = document.createElement("paymentTerms");

          term.appendChild(document.createTextNode(payTerms));

          rocdoc1.appendChild(term);

          noOfOrm++;

          xmlCount++;

 
          TransformerFactory transformerFactory = TransformerFactory.newInstance();

          Transformer transformer = transformerFactory.newTransformer();

          DOMSource domSource = new DOMSource(document);

 
 
          String fileLoc1 = "";

          fileLoc1 = ServiceLoggingUtil.getBridgePropertyValue("ORM1");

          fileLoc1 = fileLoc1 + prodate + "/";

          File f = new File(fileLoc1);

          if (!f.exists()) {

            f.mkdir();

          }

          fileName1 = "RBI_UBI" + prodate + seq + ".orm";

          myFilePath = fileLoc1 + fileName1 + ".xml";

          StreamResult streamResult = new StreamResult(new File(myFilePath));

          transformer.transform(domSource, streamResult);

        }

      }

      if (this.idpmsFileType.equalsIgnoreCase("CSV"))

      {

        String queryCsvbill = "SELECT TRIM(OUTWARDREFERENCENUMBER) AS OUTWARDREFERENCENUMBER,TRIM(ADCODE) AS ADCODE,TRIM(AMOUNT) AS AMOUNT,TRIM(CURRENCYCODE) AS CURRENCYCODE,TO_CHAR(PAYMENTDATE,'DD/MM/YYYY') AS PAYMENTDATE ,TRIM(IECODE) AS IECODE,TRIM(IENAME) AS IENAME,TRIM(IEADDRESS) AS IEADDRESS,TRIM(PANNUMBER) AS PANNUMBER,TRIM(ISCAPITALGOODS) AS ISCAPITALGOODS,TRIM(BENEFICIARYNAME) AS BENEFICIARYNAME,TRIM(BENEFICIARYACCOUNTNUMBER) AS BENEFICIARYACCOUNTNUMBER,TRIM(BENEFICIARYCOUNTRY) AS BENEFICIARYCOUNTRY,TRIM(SWIFTMESSAGE) AS SWIFTMESSAGE, TRIM(PURPOSECODE) AS PURPOSECODE, TRIM(REMARKS) AS REMARKS,TRIM(PAYMENTTERMS) AS PAYMENTTERMS,' ' as seqno FROM ETT_IDPMS_EOD_DATA WHERE EOD_DATE = '" + 

 
 
          eodFinishedDate + "'";

        String queryCsvadv = "SELECT TRIM(OUTWARDREFERENCENUMBER) AS OUTWARDREFERENCENUMBER,TRIM(ADCODE) AS ADCODE,TRIM(AMOUNT) AS AMOUNT,TRIM(CURRENCYCODE) AS CURRENCYCODE,TO_CHAR(PAYMENTDATE,'DD/MM/YYYY') AS PAYMENTDATE ,TRIM(IECODE) AS IECODE,TRIM(IENAME) AS IENAME,TRIM(IEADDRESS) AS IEADDRESS,TRIM(PANNUMBER) AS PANNUMBER,TRIM(ISCAPITALGOODS) AS ISCAPITALGOODS,TRIM(BENEFICIARYNAME) AS BENEFICIARYNAME,TRIM(BENEFICIARYACCOUNTNUMBER) AS BENEFICIARYACCOUNTNUMBER,TRIM(BENEFICIARYCOUNTRY) AS BENEFICIARYCOUNTRY,TRIM(SWIFTMESSAGE) AS SWIFTMESSAGE, TRIM(PURPOSECODE) AS PURPOSECODE, TRIM(REMARKS) AS REMARKS,TRIM(PAYMENTTERMS) AS PAYMENTTERMS,' ' as seqno FROM ETT_IDPMS_EOD_DATA WHERE STATUS = 'E' and PURPOSECODE IN ('S0103','S0103','S0109') ";

 
 
 
        String fileLoc1 = "";
        fileLoc1 = ServiceLoggingUtil.getBridgePropertyValue("ORM1");

        logger.info("QueryCSV: " + queryCsvbill);

        fileLoc1 = fileLoc1 + prodate + "/";

        File f = new File(fileLoc1);

        if (!f.exists()) {

          f.mkdir();

        }

        fileName1 = "RBI_UBI" + prodate + seq + ".orm";

        myFilePath = fileLoc1 + fileName1 + ".csv";

        noOfOrm = generateManualORMCSVFile(queryCsvbill, myFilePath);

      }

      if (noOfOrm > 0)

      {

        downloadFiles(myFilePath);

        addActionMessage("File (" + fileName1 + ") Downloaded Successfully..");

      }

      else

      {

        result = "nodatafound";

        addActionError("No Data Found");

      }

    }

    catch (Exception e)

    {

      System.out.println("Exception " + e);

      logger.info("----------IDPMS ------ORMDWLD--- [downloadORMFile]- ---exception----------------" + e);

      result = "error";

      e.printStackTrace();

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, ps, res);

      logger.info("Finally Occurred in saveDetail");

    }

    return result;

  }

  public String downloadBESFile()

  {

    Connection con = null;

    int noOfBES = 0;

    int noofBOEBills = 0;

    int invoiceCount = 0;

    String result = "success";

    String fileName = "";

    String myFilePath = "";

    try

    {

      con = DBConnectionUtility.getConnection();

      if (con == null) {

        logger.info(

          "----------IDPMS ------BESDWLD--- [processBESjob]-connection not available--------------");

      }

      String proQuery = "SELECT TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'YYYYMMDD') AS PROCDATE," + 

        "ORMXML_SEQNO.NEXTVAL AS BES_SEQNO,TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'DD-MM-YY') AS EODFINISHEDDATE FROM DUAL";

      PreparedStatement pps1 = con.prepareStatement(proQuery);

      ResultSet rs1 = pps1.executeQuery();

      String prodate = null;

      String eodFinishedDate = null;

      int seq = 0;

      if (rs1.next())

      {

        prodate = rs1.getString("PROCDATE");

        seq = rs1.getInt("BES_SEQNO");

        eodFinishedDate = rs1.getString("EODFINISHEDDATE");

      }

      closeSqlRefferance(rs1, pps1);

      if (this.idpmsFileType.equalsIgnoreCase("XML"))

      {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        document.setXmlStandalone(true);

        Element root = document.createElement("bank");

        document.appendChild(root);

        Element rocdoc = document.createElement("checkSum");

        root.appendChild(rocdoc);

        Element rocdoc2 = document.createElement("billOfEntrys");

        root.appendChild(rocdoc2);

        String query = "SELECT PORTOFDISCHARGE,BILLOFENTRYNUMBER,BILLOFENTRYDATE,IECODE,CHANGEIECODE,ADCODE,RECORDINDICATOR,PAYMENTPARTY,PAYMENT_REFNO,OUTWARDREFERENCENUMBER,EVENTREFNO,OUTWARDREFERENCEADCODE,REMITTANCECURRENCY,THIRD_PARTY,INV_SNO,INV_NO,REAL_AMT,REAL_ORM_AMT,BOE_CLOSURE_IND FROM ETT_BES_EOD_FILE_DATA WHERE LASTUPDATE = '" + 

          eodFinishedDate + "' ";

        LoggableStatement pst = new LoggableStatement(con, query);

 
 
        ResultSet res = pst.executeQuery();

        while (res.next())

        {

          String portOfDischarge = setValue(res.getString("PORTOFDISCHARGE"));

          String billOfEntryNumber = setValue(res.getString("BILLOFENTRYNUMBER"));

          String billOfEntryDate = setValue(res.getString("BILLOFENTRYDATE"));

 
 
          String ieCode = setValue(res.getString("IECODE"));

          String changedIeCode = setValue(res.getString("CHANGEIECODE"));

          String adCode = setValue(res.getString("ADCODE"));

          String recordIndicator = setValue(res.getString("RECORDINDICATOR"));

          String paymentParty = setValue(res.getString("THIRD_PARTY"));

          String paymentRefNo = setValue(res.getString("PAYMENT_REFNO"));

          String outwardRefNo = setValue(res.getString("OUTWARDREFERENCENUMBER"));

          String eventRefNo = setValue(res.getString("EVENTREFNO"));

          String outwardAdcode = setValue(res.getString("OUTWARDREFERENCEADCODE"));

          String remittanceCurrency = setValue(res.getString("REMITTANCECURRENCY"));

          String invSno = setValue(res.getString("INV_SNO"));

          String invNo = setValue(res.getString("INV_NO"));

          String realAmt = setValue(res.getString("REAL_AMT"));

          String realAmtIC = setValue(res.getString("REAL_ORM_AMT"));

          Element rocdoc1 = document.createElement("billOfEntry");

          rocdoc2.appendChild(rocdoc1);

          Element pd = document.createElement("portOfDischarge");

          pd.appendChild(document.createTextNode(portOfDischarge));

          rocdoc1.appendChild(pd);

          Element bno = document.createElement("billOfEntryNumber");

          bno.appendChild(document.createTextNode(billOfEntryNumber));

          rocdoc1.appendChild(bno);

          Element bda = document.createElement("billOfEntryDate");

          bda.appendChild(document.createTextNode(billOfEntryDate));

          rocdoc1.appendChild(bda);

          Element ie = document.createElement("IECode");

          ie.appendChild(document.createTextNode(ieCode));

          rocdoc1.appendChild(ie);

          Element cie = document.createElement("changeIECode");

          cie.appendChild(document.createTextNode(changedIeCode));

          rocdoc1.appendChild(cie);

          Element ad = document.createElement("ADCode");

          ad.appendChild(document.createTextNode(adCode));

          rocdoc1.appendChild(ad);

          Element ri = document.createElement("recordIndicator");

          ri.appendChild(document.createTextNode(recordIndicator));

          rocdoc1.appendChild(ri);

          Element pp = document.createElement("paymentParty");

          pp.appendChild(document.createTextNode(paymentParty));

          rocdoc1.appendChild(pp);

          Element pN = document.createElement("paymentReferenceNumber");

          pN.appendChild(document.createTextNode(paymentRefNo));

          rocdoc1.appendChild(pN);

          Element oN = document.createElement("outwardReferenceNumber");

          oN.appendChild(document.createTextNode(outwardRefNo));

          rocdoc1.appendChild(oN);

          Element oA = document.createElement("outwardReferenceADCode");

          oA.appendChild(document.createTextNode(outwardAdcode));

          rocdoc1.appendChild(oA);

          Element rC = document.createElement("remittanceCurrency");

          rC.appendChild(document.createTextNode(remittanceCurrency));

          rocdoc1.appendChild(rC);

          int boeCloseInd = Integer.parseInt(setValue(res.getString("BOE_CLOSURE_IND")));

          Element bI = document.createElement("billClosureIndicator");

          bI.appendChild(document.createTextNode(String.valueOf(boeCloseInd)));

          rocdoc1.appendChild(bI);

          noofBOEBills++;
          Element Inv = document.createElement("invoices");

          rocdoc1.appendChild(Inv);

          Element Invc = document.createElement("invoice");

          Inv.appendChild(Invc);

          Element Insrlno = document.createElement("invoiceSerialNo");

          Insrlno.appendChild(document.createTextNode(invSno));

          Invc.appendChild(Insrlno);

          Element Invno = document.createElement("invoiceNo");

          Invno.appendChild(document.createTextNode(invNo));

          Invc.appendChild(Invno);

          logger.info("----------IDPMS ------BESDWLD--- realAmt-----" + realAmt);

          Element Invamt = document.createElement("invoiceAmt");

          Invamt.appendChild(document.createTextNode(realAmt));

          Invc.appendChild(Invamt);

          logger.info("----------IDPMS ------BESDWLD--- realAmtIC-----" + realAmtIC);

          Element InvamtIc = document.createElement("invoiceAmtIc");

          InvamtIc.appendChild(document.createTextNode(realAmtIC));

          Invc.appendChild(InvamtIc);

          invoiceCount++;

          noOfBES++;

        }

        res.close();

        pst.close();

        Element rocdoc3 = document.createElement("noOfbillOfEntry");

        rocdoc3.appendChild(document.createTextNode(String.valueOf(noofBOEBills)));

        rocdoc.appendChild(rocdoc3);

        Element rocdocInvCount = document.createElement("noOfInvoices");

        rocdocInvCount.appendChild(document.createTextNode(String.valueOf(invoiceCount)));

        rocdoc.appendChild(rocdocInvCount);

 
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        DOMSource domSource = new DOMSource(document);

        String fileLoc = "";

        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("BES1");

        logger.info("File Location is : " + fileLoc);

        fileLoc = fileLoc + prodate + "/";

        File f = new File(fileLoc);

        if (!f.exists())

        {

          f.mkdir();

          System.out.println(f.mkdir());

        }

        fileName = "RBI" + prodate + seq + ".bes";

        myFilePath = fileLoc + fileName + ".xml";

 
        StreamResult streamResult = new StreamResult(new File(myFilePath));

        transformer.transform(domSource, streamResult);

      }

      if (this.idpmsFileType.equalsIgnoreCase("CSV"))

      {

        String queryCsv = "SELECT BILLOFENTRYNUMBER,TO_CHAR(TO_DATE(BILLOFENTRYDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BILLOFENTRYDATE,PORTOFDISCHARGE,IECODE,CHANGEIECODE,ADCODE,INV_SNO AS INVOICESERIALNO,INV_NO AS INVOICENO,THIRD_PARTY AS PAYMENT_PARTY,PAYMENT_REFNO AS PAYMENTREFRENCENUMBER,OUTWARDREFERENCENUMBER AS OUTWARDREFERENCENUMBER,OUTWARDREFERENCEADCODE,REAL_AMT AS REMITTANCE_AMT, RECORDINDICATOR AS INVOICECLOSEBILLIND,TO_CHAR(TO_DATE(BILLOFENTRYDATE,'DD-MM-YY'),'DD/MM/YYYY') AS OUTWARDREALZDATE,REMITTANCECURRENCY AS REALZCURRENCYCODE FROM ETT_BES_EOD_FILE_DATA WHERE LASTUPDATE='" + 

 
          eodFinishedDate + "' ORDER BY TO_NUMBER(PAYMENT_REFNO) ";

        String fileLoc = "";

        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("BES1");

        logger.info("File Location is : " + fileLoc);

        fileLoc = fileLoc + prodate + "/";

        File f = new File(fileLoc);

        if (!f.exists())

        {

          f.mkdir();

          System.out.println(f.mkdir());

        }

        fileName = "RBI" + prodate + seq + ".bes";

        myFilePath = fileLoc + fileName + ".csv";

        noOfBES = generateManualBESCSVFile(queryCsv, myFilePath);

      }

      if (noOfBES > 0)

      {

        downloadFiles(myFilePath);

        addActionMessage("File (" + fileName + ") Downloaded Successfully..");

      }

      else

      {

        result = "nodatafound";

        addActionError("No Data Found");

      }

    }

    catch (Exception e)

    {

      System.out.println("Exception " + e);

      result = "error";

      e.printStackTrace();

      logger.info("----------IDPMS ------BESDWLD--- [processBESjob]- GENERATION exception-eee----" + e);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, null, null);

      logger.info("Finally Occurred in saveDetail");

    }

    return result;

  }

  public String downloadBESCANFile()

  {

    Connection con = null;

    int noOfBESCanCount = 0;

    int noofBOEBills = 0;

    int invoiceCount = 0;

    String result = "success";

    String fileName = "";

    String myFilePath = "";

    try

    {

      con = DBConnectionUtility.getConnection();

      if (con == null) {

        logger.info(

          "----------IDPMS ------BESCANDWLD--- [processBESCANjob]-connection not available--------------");

      }

      String proQuery = "SELECT TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'YYYYMMDD') AS PROCDATE," + 

        "ORMXML_SEQNO.NEXTVAL AS BESCAN_SEQNO,TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'DD-MM-YY') AS EODFINISHEDDATE FROM DUAL";

      PreparedStatement pps1 = con.prepareStatement(proQuery);

      ResultSet rs1 = pps1.executeQuery();

      String prodate = null;

      String eodFinishedDate = null;

      int seq = 0;

      if (rs1.next())

      {

        prodate = rs1.getString("PROCDATE");

        seq = rs1.getInt("BESCAN_SEQNO");

        eodFinishedDate = rs1.getString("EODFINISHEDDATE");

      }

      closeSqlRefferance(rs1, pps1);

      if (this.idpmsFileType.equalsIgnoreCase("XML"))

      {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        document.setXmlStandalone(true);

        Element root = document.createElement("bank");

        document.appendChild(root);

        Element rocdoc = document.createElement("checkSum");

        root.appendChild(rocdoc);

        Element rocdoc2 = document.createElement("billOfEntrys");

        root.appendChild(rocdoc2);

        String query = "SELECT PORTOFDISCHARGE AS PORTOFDISCHARGE,BOENUMBER AS BILLOFENTRYNUMBER,TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BILLOFENTRYDATE,IECODE AS IECODE,CHANGEIECODE AS CHANGEIECODE,ADCODE AS ADCODE,'3' AS RECORDINDICATOR,PAYMENTPARTY AS PAYMENTPARTY,PAYMENTREFNUMBER AS PAYMENT_REFNO,ORNUMBER AS OUTWARDREFERENCENUMBER,ORADCODE AS OUTWARDREFERENCEADCODE,REMITTCUR AS REMITTANCECURRENCY,BILLCLOSUREINDI AS BILLCLOSUREINDI,INVOICESERIALNO AS INV_SNO,INVOICENO AS INV_NO,INVOICEAMTIC AS INVOICEAMTIC,INVOICEAMT AS INVOICEAMT FROM ETT_BES_ACK_CANCEL WHERE EOD_FINISHED_TIME='" + 

 
          eodFinishedDate + "'";

        LoggableStatement pst = new LoggableStatement(con, query);

 
 
        ResultSet res = pst.executeQuery();

        while (res.next())

        {

          String portOfDischarge = setValue(res.getString("PORTOFDISCHARGE"));

          String billOfEntryNumber = setValue(res.getString("BILLOFENTRYNUMBER"));

          String billOfEntryDate = setValue(res.getString("BILLOFENTRYDATE"));
          String ieCode = setValue(res.getString("IECODE"));

          String changedIeCode = setValue(res.getString("CHANGEIECODE"));

          String adCode = setValue(res.getString("ADCODE"));

          String recordIndicator = setValue(res.getString("RECORDINDICATOR"));

          String paymentParty = setValue(res.getString("PAYMENTPARTY"));

          String paymentRefNo = setValue(res.getString("PAYMENT_REFNO"));

          String outwardRefNo = setValue(res.getString("OUTWARDREFERENCENUMBER"));

          String outwardAdcode = setValue(res.getString("OUTWARDREFERENCEADCODE"));

          String remittanceCurrency = setValue(res.getString("REMITTANCECURRENCY"));

          int boeCloseInd = Integer.parseInt(setValue(res.getString("BILLCLOSUREINDI")));

          String invSno = setValue(res.getString("INV_SNO"));

          String invNo = setValue(res.getString("INV_NO"));

          String INVOICEAMT = setValue(res.getString("INVOICEAMT"));

          String INVOICEAMTIC = setValue(res.getString("INVOICEAMTIC"));

 
          Element rocdoc1 = document.createElement("billOfEntry");

          rocdoc2.appendChild(rocdoc1);

          Element pd = document.createElement("portOfDischarge");

          pd.appendChild(document.createTextNode(portOfDischarge));

          rocdoc1.appendChild(pd);

          Element bno = document.createElement("billOfEntryNumber");

          bno.appendChild(document.createTextNode(billOfEntryNumber));

          rocdoc1.appendChild(bno);

          Element bda = document.createElement("billOfEntryDate");

          bda.appendChild(document.createTextNode(billOfEntryDate));

          rocdoc1.appendChild(bda);

          Element ie = document.createElement("IECode");

          ie.appendChild(document.createTextNode(ieCode));

          rocdoc1.appendChild(ie);

          Element cie = document.createElement("changeIECode");

          cie.appendChild(document.createTextNode(changedIeCode));

          rocdoc1.appendChild(cie);

          Element ad = document.createElement("ADCode");

          ad.appendChild(document.createTextNode(adCode));

          rocdoc1.appendChild(ad);

          Element ri = document.createElement("recordIndicator");

          ri.appendChild(document.createTextNode(recordIndicator));

          rocdoc1.appendChild(ri);

          Element pp = document.createElement("paymentParty");

          pp.appendChild(document.createTextNode(paymentParty));

          rocdoc1.appendChild(pp);

          Element pN = document.createElement("paymentReferenceNumber");

          pN.appendChild(document.createTextNode(paymentRefNo));

          rocdoc1.appendChild(pN);

          Element oN = document.createElement("outwardReferenceNumber");

          oN.appendChild(document.createTextNode(outwardRefNo));

          rocdoc1.appendChild(oN);

          Element oA = document.createElement("outwardReferenceADCode");

          oA.appendChild(document.createTextNode(outwardAdcode));

          rocdoc1.appendChild(oA);

          Element rC = document.createElement("remittanceCurrency");

          rC.appendChild(document.createTextNode(remittanceCurrency));

          rocdoc1.appendChild(rC);

          Element bI = document.createElement("billClosureIndicator");

          bI.appendChild(document.createTextNode(String.valueOf(boeCloseInd)));

          rocdoc1.appendChild(bI);

          noofBOEBills++;

          Element Inv = document.createElement("invoices");

          rocdoc1.appendChild(Inv);

          Element Invc = document.createElement("invoice");

          Inv.appendChild(Invc);

          Element Insrlno = document.createElement("invoiceSerialNo");

          Insrlno.appendChild(document.createTextNode(invSno));

          Invc.appendChild(Insrlno);

          Element Invno = document.createElement("invoiceNo");

          Invno.appendChild(document.createTextNode(invNo));

          Invc.appendChild(Invno);

          Element Invamt = document.createElement("invoiceAmt");

          Invamt.appendChild(document.createTextNode(INVOICEAMT));

          Invc.appendChild(Invamt);

          Element InvamtIc = document.createElement("invoiceAmtIc");

          InvamtIc.appendChild(document.createTextNode(INVOICEAMTIC));

          Invc.appendChild(InvamtIc);

          invoiceCount++;

          noOfBESCanCount++;

        }

        res.close();

        pst.close();

        Element rocdoc3 = document.createElement("noOfbillOfEntry");

        rocdoc3.appendChild(document.createTextNode(String.valueOf(noofBOEBills)));

        rocdoc.appendChild(rocdoc3);

        Element rocdocInvCount = document.createElement("noOfInvoices");

        rocdocInvCount.appendChild(document.createTextNode(String.valueOf(invoiceCount)));

        rocdoc.appendChild(rocdocInvCount);

 
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        DOMSource domSource = new DOMSource(document);

        String fileLoc = "";

        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("BES1");

        logger.info("File Location is : " + fileLoc);

        fileLoc = fileLoc + prodate + "/";

        File f = new File(fileLoc);

        if (!f.exists())

        {

          f.mkdir();

          System.out.println(f.mkdir());

        }

        fileName = "RBICAN" + prodate + seq + ".bes";

        myFilePath = fileLoc + fileName + ".xml";

 
        StreamResult streamResult = new StreamResult(new File(myFilePath));

        transformer.transform(domSource, streamResult);

      }

      if (this.idpmsFileType.equalsIgnoreCase("CSV"))

      {

        String queryCsv = "SELECT BOENUMBER AS BILLOFENTRYNUMBER,TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BILLOFENTRYDATE,PORTOFDISCHARGE AS PORTOFDISCHARGE,IECODE AS IECODE,CHANGEIECODE AS CHANGEIECODE,ADCODE AS ADCODE,'3' AS RECORDINDICATOR,INVOICESERIALNO AS INVOICESERIALNO,INVOICENO AS INVOICENO,PAYMENTPARTY AS PAYMENTPARTY,PAYMENTREFNUMBER AS PAYMENTREFERENCENUMBER,ORNUMBER AS OUTWARDREFERENCENUMBER,ORADCODE AS OUTWARDREFERENCEADCODE,INVOICEAMT AS REMITTANCEAMOUNT, RECORDINDICATOR AS INVOICECLOSEBILLIND,TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS OUTWARDREALZDATE,REMITTCUR AS REALZCURRENCYCODE FROM ETT_BES_ACK_CANCEL WHERE EOD_FINISHED_TIME='" + 

 
          eodFinishedDate + "' ORDER BY TO_NUMBER(PAYMENTREFNUMBER)";

        String fileLoc = "";

        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("BES1");

        logger.info("File Location is : " + fileLoc);

        fileLoc = fileLoc + prodate + "/";

        File f = new File(fileLoc);

        if (!f.exists())

        {

          f.mkdir();

          System.out.println(f.mkdir());

        }

        fileName = "RBICAN" + prodate + seq + ".bes";

        myFilePath = fileLoc + fileName + ".csv";

        noOfBESCanCount = generateBESCANCSVFile(queryCsv, myFilePath);

      }

      if (noOfBESCanCount > 0)

      {

        downloadFiles(myFilePath);

        addActionMessage("File (" + fileName + ") Downloaded Successfully..");

      }

      else

      {

        result = "nodatafound";

        addActionError("No Data Found");

      }

    }

    catch (Exception e)

    {

      System.out.println("Exception " + e);

      result = "error";

      e.printStackTrace();

      logger.info("----------IDPMS ------BESCANDWLD--- [processBESCANjob]- GENERATION exception-eee----" + e);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, null, null);

      logger.info("Finally Occurred in saveDetail");

    }

    return result;

  }

  public String downloadOBBFile()

  {

    logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]-----------");

    Connection con = null;

    String result = "success";

    int obbCnt = 0;

 
    String COMMA_DELIMITER = "|";

    String myFilePath = "";

    String fileName1 = "";

    String FILE_HEADER = "PortOfDischarge|ImportAgency|BillOfEntryNumber|BillOfEntryDate|ADCode|IECode|IEName";

    PrintWriter csvWriter = null;

    try

    {

      con = DBConnectionUtility.getConnection();
      String proQuery = "SELECT TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'YYYYMMDD') AS PROCDATE,TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1, " + 

        "OBB_RBI_REQ_REQ.NEXTVAL AS OBB_SEQNO FROM DLYPRCCYCL";

      LoggableStatement pps1 = new LoggableStatement(con, proQuery);

      logger.info(

        "----------IDPMS ------OBBDWLD--- [processOBBjob]-----query------" + pps1.getQueryString());

      ResultSet rs1 = pps1.executeQuery();

      String prodate = null;

      String prodate1 = null;

      int seq = 0;

      if (rs1.next())

      {

        prodate = rs1.getString("PROCDATE");

        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]-----prodate------" + prodate);

        prodate1 = rs1.getString("PROCDATE1");

        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]-----prodate1------" + prodate1);

        seq = rs1.getInt("OBB_SEQNO");

        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]-----seq------" + seq);

      }

      closeSqlRefferance(rs1, pps1);

      if (this.idpmsFileType.equalsIgnoreCase("XML")) {

        addActionError("XML File Not available for OBB");

      }

      if (this.idpmsFileType.equalsIgnoreCase("CSV"))

      {

        String fileLoc = "";

        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("OBB1");

 
        fileLoc = fileLoc + prodate + "/";

        File f = new File(fileLoc);

        if (!f.exists()) {

          f.mkdir();

        }

        fileName1 = "RBI" + prodate + seq + ".ibe";

        myFilePath = fileLoc + fileName1 + ".csv";

 
 
 
 
        csvWriter = new PrintWriter(new File(myFilePath));

        csvWriter.println("PortOfDischarge|ImportAgency|BillOfEntryNumber|BillOfEntryDate|ADCode|IECode|IEName".toString());

        String query = " SELECT PORTCODE AS PORTCODE,BOE_IMP_AGENCY AS ImportAgency,BOENUM AS BOENUM,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,ADCODE AS ADCode,IE_CODE AS IECode, IENAME AS IEName FROM ETT_BOE_OBB_DETAILS WHERE EODDATE='" + 

          prodate1 + "'";

        LoggableStatement pst = new LoggableStatement(con, query);

        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]---ETT_BOE_OBB_DETAILS--query------" + 

          pst.getQueryString());

        ResultSet res = pst.executeQuery();

        while (res.next())

        {

          obbCnt++;

          String sBOENum = setValue(res.getString("BOENUM"));

          String sBOEDate = setValue(res.getString("BOEDATE"));

          String sPortCode = setValue(res.getString("PORTCODE"));

          String sBOEImpAgency = setValue(res.getString("ImportAgency"));

          String sBOEADCode = setValue(res.getString("ADCode"));

          String sBOEIECode = setValue(res.getString("IECode"));

          String sBOEIEName = setValue(res.getString("IEName"));

          StringBuffer sContent = new StringBuffer();

          sContent.append(sPortCode);

          sContent.append("|");

          sContent.append(sBOEImpAgency);

          sContent.append("|");

          sContent.append(sBOENum);

          sContent.append("|");

          sContent.append(sBOEDate);

          sContent.append("|");

          sContent.append(sBOEADCode);

          sContent.append("|");

          sContent.append(sBOEIECode);

          sContent.append("|");

          sContent.append(sBOEIEName);

          csvWriter.println(sContent.toString());

          logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]---sContent.toString()------" + sContent.toString());

        }

        csvWriter.close();

        res.close();

        pst.close();

      }

      if (obbCnt == 0)

      {

        result = "nodatafound";

        addActionError("No Data Found");

      }

      if (obbCnt > 0)

      {

        downloadFiles(myFilePath);

        addActionMessage("File (" + fileName1 + ") Downloaded Successfully..");

      }

    }

    catch (Exception e)

    {

      System.out.println("Exception " + e);

      result = "error";

      e.printStackTrace();

      logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]---exception---" + e);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, null, null);

    }

    return result;

  }

  public String downloadBEEFile()

  {

    Connection con = null;

    int noOfBoe = 0;

    int xmlCount = 0;

    String result = "success";

    String myFilePath = "";

    String fileName = "";

    try

    {

      con = DBConnectionUtility.getConnection();

      if (con == null) {

        logger.info("----------IDPMS ------BEEDWLD--- [processBEEjob]---connection Issue-------------");

      }

      String proQuery = "SELECT TO_CHAR(TO_DATE('" + this.eodDate + "','DD-MM-YY'),'YYYYMMDD') AS PROCDATE," + 

        " BOE_EXTN_SEQ.NEXTVAL AS BEE_SEQNO,TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'DD-MM-YY') AS EODDATE FROM DLYPRCCYCL";

      PreparedStatement pps1 = con.prepareStatement(proQuery);

      ResultSet rs1 = pps1.executeQuery();

      String prodate = null;

      String eodDate = "";

      int seq = 0;

      if (rs1.next())

      {

        prodate = rs1.getString("PROCDATE");

        seq = rs1.getInt("BEE_SEQNO");

        eodDate = rs1.getString("EODDATE");

      }

      closeSqlRefferance(rs1, pps1);

      if (this.idpmsFileType.equalsIgnoreCase("XML"))

      {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        document.setXmlStandalone(true);

        Element root = document.createElement("bank");

        document.appendChild(root);

        Element rocdoc = document.createElement("checkSum");

        root.appendChild(rocdoc);

        Element rocdoc2 = document.createElement("billOfEntries");

        root.appendChild(rocdoc2);
        String query = "SELECT boeext.BOENUMBER,TO_CHAR(TO_DATE(boeext.BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,boeext.IECODE AS IECODE, boeext.BOE_IE_NAME AS IENAME,boeext.ADCODE AS ADCODE,boeext.PORTOFDISCHARGE AS PORTOFDISCHARGE, CASE WHEN boeext.IMPORT_AGENCY ='1' THEN 'Customs' ELSE 'SEZ' END AS IMPORT_AGENCY,boeext.STATUS AS STATUS,boeext.EXT_INDICATOR AS EXT_INDICATOR, boeext.EXTENSIONBY AS EXTENSIONBY,TO_CHAR(TO_DATE(boeext.EXTENSIONDATE,'DD-MM-YY'),'DD/MM/YYYY') AS EXTENSIONDATE,boeext.LETTERNO,TO_CHAR(TO_DATE(boeext.LETTERDATE,'DD-MM-YY'),'DD/MM/YYYY') AS LETTERDATE,boeext.REMARKS AS REMARKS FROM ETT_BEE_EOD_FILE_DATA  boeext where TO_DATE(boeext.EODDATE,'DD/MM/YY')='" + 

          
          
          eodDate + "'";

        LoggableStatement pst = new LoggableStatement(con, query);

        ResultSet res = pst.executeQuery();

        while (res.next())

        {

          String portOfDischarge = setValue(res.getString("PORTOFDISCHARGE"));

          String billOfEntryNumber = setValue(res.getString("BOENUMBER"));

          String billOfEntryDate = setValue(res.getString("BOEDATE"));

          String ieCode = setValue(res.getString("IECODE"));

          String adCode = setValue(res.getString("ADCODE"));

          String recordIndicator = setValue(res.getString("EXT_INDICATOR"));

          String extensionBy = setValue(res.getString("EXTENSIONBY"));

          String letterNo = setValue(res.getString("LETTERNO"));

          String letterDate = setValue(res.getString("LETTERDATE"));

          String extensionDate = setValue(res.getString("EXTENSIONDATE"));

          String remarks = setValue(res.getString("REMARKS"));

          String ieName = setValue(res.getString("IENAME"));

          String importAgency = setValue(res.getString("IMPORT_AGENCY"));

          String status = setValue(res.getString("STATUS"));

          Element rocdoc1 = document.createElement("billOfEntry");

          rocdoc2.appendChild(rocdoc1);

          Element pd = document.createElement("portOfDischarge");

          pd.appendChild(document.createTextNode(portOfDischarge));

          rocdoc1.appendChild(pd);

          Element bno = document.createElement("billOfEntryNumber");

          bno.appendChild(document.createTextNode(billOfEntryNumber));

          rocdoc1.appendChild(bno);

          Element bda = document.createElement("billOfEntryDate");

          bda.appendChild(document.createTextNode(billOfEntryDate));

          rocdoc1.appendChild(bda);

          Element ie = document.createElement("IECode");

          ie.appendChild(document.createTextNode(ieCode));

          rocdoc1.appendChild(ie);

          Element ad = document.createElement("ADCode");

          ad.appendChild(document.createTextNode(adCode));

          rocdoc1.appendChild(ad);

          Element ri = document.createElement("recordIndicator");

          ri.appendChild(document.createTextNode(recordIndicator));

          rocdoc1.appendChild(ri);

          Element eb = document.createElement("extenstionBy");

          eb.appendChild(document.createTextNode(extensionBy));

          rocdoc1.appendChild(eb);

          Element ln = document.createElement("letterNumber");

          ln.appendChild(document.createTextNode(letterNo));

          rocdoc1.appendChild(ln);

          Element ld = document.createElement("letterDate");

          ld.appendChild(document.createTextNode(letterDate));

          rocdoc1.appendChild(ld);

          Element ed = document.createElement("extensionDate");

          ed.appendChild(document.createTextNode(extensionDate));

          rocdoc1.appendChild(ed);

          Element rm = document.createElement("remarks");

          rm.appendChild(document.createTextNode(remarks));

          rocdoc1.appendChild(rm);

          noOfBoe++;

          xmlCount++;

        }

        res.close();

        pst.close();

        Element rocdoc3 = document.createElement("noOfBOE");

        rocdoc3.appendChild(document.createTextNode(String.valueOf(noOfBoe)));

        rocdoc.appendChild(rocdoc3);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        DOMSource domSource = new DOMSource(document);

        String fileLoc = "";

        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("BEE1");

        fileLoc = String.valueOf(fileLoc) + prodate + "/";

        File f = new File(fileLoc);

        if (!f.exists()) {

          System.out.print(f.mkdir());

        }

        fileName = "RBI" + prodate + seq + ".bee";

        myFilePath = String.valueOf(fileLoc) + fileName + ".xml";

        StreamResult streamResult = new StreamResult(new File(myFilePath));

        transformer.transform(domSource, streamResult);

      }

      if (this.idpmsFileType.equalsIgnoreCase("CSV"))

      {

        String queryCsv = "SELECT boeext.BOENUMBER,TO_CHAR(TO_DATE(boeext.BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,boeext.IECODE AS IECODE,boeext.BOE_IE_NAME AS IENAME,boeext.ADCODE AS ADCODE,boeext.PORTOFDISCHARGE AS PORTOFDISCHARGE, boeext.IMPORT_AGENCY AS IMPORT_AGENCY,boeext.STATUS AS STATUS,boeext.EXTENSIONBY AS EXTENSIONBY,TO_CHAR(TO_DATE(boeext.EXTENSIONDATE,'DD-MM-YY'),'DD/MM/YYYY') AS EXTENSIONDATE,boeext.LETTERNO,TO_CHAR(TO_DATE(boeext.LETTERDATE,'DD-MM-YY'),'DD/MM/YYYY') AS LETTERDATE  FROM ETT_BEE_EOD_FILE_DATA  boeext where TO_DATE(boeext.EODDATE,'DD/MM/YY')='" + 

 
 
          eodDate + "' ORDER BY BOEEXT.EXTENSION_SNO";

        String fileLoc = "";

        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("BEE1");

        fileLoc = String.valueOf(fileLoc) + prodate + "/";

        File f = new File(fileLoc);

        if (!f.exists()) {

          System.out.print(f.mkdir());

        }

        fileName = "RBI" + prodate + seq + ".bee";

        myFilePath = String.valueOf(fileLoc) + fileName + ".csv";

        noOfBoe = generateManualBEECSVFile(queryCsv, myFilePath);

      }

      if (noOfBoe > 0)

      {

        downloadFiles(myFilePath);

        addActionMessage("File (" + fileName + ") Downloaded Successfully..");

      }

      if (noOfBoe == 0)

      {

        result = "nodatafound";

        addActionError("No Data Found");

      }

    }

    catch (Exception e)

    {

      logger.info("----------IDPMS ------BEEDWLD--- [processBEEjob]-exception------" + e);

      result = "error";

      e.printStackTrace();

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, null, null);

      logger.info("Finally Occurred in saveDetail");

    }

    return result;

  }

  public String downloadORMCLFile()

  {
	  logger.info("----------IDPMS ------ORMCLDWLD--- [downloadORMCLFile]- ------------------- idpmsFileType :: " + this.idpmsFileType + " :: idpmsFileName :: " + this.idpmsFileName + " :: eodDate :: " + this.eodDate);

	    Connection con = null;

	    int xmlCount = 0;

	    String result = "success";

	    String fileName = "";

	    String myFilePath = "";

	    try

	    {

	      con = DBConnectionUtility.getConnection();

	      if (con == null) {

	        logger.info(

	          "----------IDPMS ------ORMCLDWLD--- [processORMCLjob]- ----Connection Issue----------");

	      }

	      String proQuery = "SELECT TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'YYYYMMDD') AS PROCDATE," + 

	        "ORMXML_SEQNO.NEXTVAL AS ORM_SEQNO,TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'DD-MM-YY') AS EODFINISHEDDATE FROM DUAL";

	 
	      PreparedStatement pps1 = con.prepareStatement(proQuery);

	      ResultSet rs1 = pps1.executeQuery();

	      String prodate = null;

	      int seq = 0;

	      String eodFinishedDate = null;

	      if (rs1.next())

	      {

	        prodate = rs1.getString("PROCDATE");

	        seq = rs1.getInt("ORM_SEQNO");

	        eodFinishedDate = rs1.getString("EODFINISHEDDATE");

	      }

	      closeSqlRefferance(rs1, pps1);

	      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

	      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

	      Document document = documentBuilder.newDocument();

	      document.setXmlStandalone(true);

	      Element root = document.createElement("bank");

	      document.appendChild(root);

	      Element rocdoc = document.createElement("checkSum");

	      root.appendChild(rocdoc);

	      Element rocdoc2 = document.createElement("outwardReferences");

	      root.appendChild(rocdoc2);

	      if (this.idpmsFileType.equalsIgnoreCase("XML"))

	      {

	        String query = "SELECT ETTEC.ORMID,ETTEC.REFERENCENO,ETTEC.ADCODE,ETTEC.IECODE,ETTEC.IENAME,ETTEC.REMTTAMOUNT,ETTEC.CURRENCYCODE,TO_CHAR(ETTEC.PAYMENTDATE,'DD/MM/YYYY') AS PAYMENTDATE,ETTEC.AMTUNUTILIZED,ETTEC.STATUS,ETTEC.ADJAMT, TO_CHAR(ETTEC.ADJDATE,'DD/MM/YYYY') AS ADJDATE,ETTEC.ADJREFNO,ETTEC.ADJREASON,ETTEC.DOCNUM,ETTEC.DOCDATE,ETTEC.PORTCODE,ETTEC.APPROVEDBY,ETTEC.LETTERNO,TO_CHAR(ETTEC.LETTERDAT,'DD/MM/YYYY') AS LETTERDAT,ETTEC.REMARKS FROM ETT_ORMCL_EOD_FILE_DATA ETTEC WHERE TO_DATE(EODDATE,'DD/MM/YY') = '" + 

	 
	 
	 
	          eodFinishedDate + "'";

	        String fileLoc = "";

	        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("ORA1");

	 
	 
	        fileLoc = fileLoc + prodate + "/";

	        File f = new File(fileLoc);

	        if (!f.exists())

	        {

	          f.mkdir();

	          System.out.println(f.mkdir());

	        }

	        fileName = "RBI" + prodate + seq + ".orc";

	        myFilePath = fileLoc + fileName + ".xml";

	        LoggableStatement pst = new LoggableStatement(con, query);

	        ResultSet res = pst.executeQuery();

	        logger.info(

	          "----------IDPMS ------ORMCLDWLD--- [processORMCLjob]- --ETT_IDPMS_DATA_EC------query------" + 

	          pst.getQueryString());

	        while (res.next())

	        {

	          String ormId = setValue(res.getString("ORMID"));

	          String referenceNo = setValue(res.getString("REFERENCENO"));

	          String adcode = setValue(res.getString("ADCODE"));

	          String iecode = setValue(res.getString("IECODE"));

	          String iename = setValue(res.getString("IENAME"));

	          String remttamount = setValue(res.getString("REMTTAMOUNT"));

	          String currencyCode = setValue(res.getString("CURRENCYCODE"));

	          String PaymentDate = setValue(res.getString("PAYMENTDATE"));

	          String amtunutilized = setValue(res.getString("AMTUNUTILIZED"));

	          String status = setValue(res.getString("STATUS"));

	          String adjamt = setValue(res.getString("ADJAMT"));

	          String adjdate = setValue(res.getString("ADJDATE"));

	          String adjrefno = setValue(res.getString("ADJREFNO"));

	          String adjreason = setValue(res.getString("ADJREASON"));

	          String docnum = setValue(res.getString("DOCNUM"));

	          String docdate = setValue(res.getString("DOCDATE"));

	          String portcode = setValue(res.getString("PORTCODE"));

	          String approvedBy = setValue(res.getString("APPROVEDBY"));

	          String letterno = setValue(res.getString("LETTERNO"));

	          String letterdat = setValue(res.getString("LETTERDAT"));

	          String remarks = setValue(res.getString("REMARKS"));

	          Element rocdoc1 = document.createElement("outwardReference");

	          rocdoc2.appendChild(rocdoc1);

	          Element orefid = document.createElement("outwardReferenceNumber");

	          orefid.appendChild(document.createTextNode(ormId));

	          rocdoc1.appendChild(orefid);

	          Element orefno = document.createElement("outwardReferenceNumber");

	          orefno.appendChild(document.createTextNode(referenceNo));

	          rocdoc1.appendChild(orefno);

	          Element oadcd = document.createElement("ADCode");

	          oadcd.appendChild(document.createTextNode(adcode));

	          rocdoc1.appendChild(oadcd);

	          Element Oiecod = document.createElement("IECode");

	          Oiecod.appendChild(document.createTextNode(iecode));

	          rocdoc1.appendChild(Oiecod);

	          Element Oiename = document.createElement("IEName");

	          Oiename.appendChild(document.createTextNode(iename));

	          rocdoc1.appendChild(Oiename);

	          Element Oremamt = document.createElement("remittanceAmount");

	          Oremamt.appendChild(document.createTextNode(remttamount));

	          rocdoc1.appendChild(Oremamt);

	          Element Occode = document.createElement("remittanceCurrency");

	          Occode.appendChild(document.createTextNode(currencyCode));

	          rocdoc1.appendChild(Occode);

	          Element Opaydate = document.createElement("paymentDate");

	          Opaydate.appendChild(document.createTextNode(PaymentDate));

	          rocdoc1.appendChild(Opaydate);

	          Element Oamtunut = document.createElement("paymentDate");

	          Oamtunut.appendChild(document.createTextNode(amtunutilized));

	          rocdoc1.appendChild(Oamtunut);

	          Element Ostatus = document.createElement("adjustedAmount");

	          Ostatus.appendChild(document.createTextNode(status));

	          rocdoc1.appendChild(Ostatus);

	          Element Oant = document.createElement("adjustedAmount");

	          Oant.appendChild(document.createTextNode(adjamt));

	          rocdoc1.appendChild(Oant);

	          Element Odat = document.createElement("adjustedDate");

	          Odat.appendChild(document.createTextNode(adjdate));

	          rocdoc1.appendChild(Odat);

	          Element oadjref = document.createElement("adjustmentReferenceNumber");

	          oadjref.appendChild(document.createTextNode(adjrefno));

	          rocdoc1.appendChild(oadjref);

	          Element Oadjreason = document.createElement("adjustmentReason");

	          Oadjreason.appendChild(document.createTextNode(adjreason));

	          rocdoc1.appendChild(Oadjreason);

	          Element Odocno = document.createElement("documentNumber");

	          Odocno.appendChild(document.createTextNode(docnum));

	          rocdoc1.appendChild(Odocno);

	          Element Odocdat = document.createElement("documentDate");

	          Odocdat.appendChild(document.createTextNode(docdate));

	          rocdoc1.appendChild(Odocdat);

	          Element Oportdis = document.createElement("portofDischarge");

	          Oportdis.appendChild(document.createTextNode(portcode));

	          rocdoc1.appendChild(Oportdis);

	          Element Oappby = document.createElement("approvedBy");

	          Oappby.appendChild(document.createTextNode(approvedBy));

	          rocdoc1.appendChild(Oappby);

	          Element Olettrno = document.createElement("letterNumber");

	          Olettrno.appendChild(document.createTextNode(letterno));

	          rocdoc1.appendChild(Olettrno);

	          Element OlettrDate = document.createElement("letterDate");

	          OlettrDate.appendChild(document.createTextNode(letterdat));

	          rocdoc1.appendChild(OlettrDate);

	          Element Orm = document.createElement("remark");

	          Orm.appendChild(document.createTextNode(remarks));

	          rocdoc1.appendChild(Orm);

	 
	          xmlCount++;

	        }

	        res.close();

	        pst.close();
	        Element rocdoc3 = document.createElement("noOfORM");

	        rocdoc3.appendChild(document.createTextNode(String.valueOf(xmlCount)));

	        rocdoc.appendChild(rocdoc3);

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();

	        Transformer transformer = transformerFactory.newTransformer();

	        DOMSource domSource = new DOMSource(document);

	        StreamResult streamResult = new StreamResult(new File(myFilePath));

	        transformer.transform(domSource, streamResult);

	      }

	      if (this.idpmsFileType.equalsIgnoreCase("CSV"))

	      {

	        String queryCsv = "SELECT ROWNUM as NUM,'' AS ORMID,ETTEC.REFERENCENO,ETTEC.ADCODE,ETTEC.IECODE,ETTEC.IENAME,ETTEC.REMTTAMOUNT,ETTEC.CURRENCYCODE,TO_CHAR(ETTEC.PAYMENTDATE,'DD/MM/YYYY') AS PAYMENTDATE,ETTEC.AMTUNUTILIZED,ETTEC.STATUS,ETTEC.ADJAMT, TO_CHAR(ETTEC.ADJDATE,'DD/MM/YYYY') AS ADJDATE,ETTEC.ADJREFNO,ETTEC.ADJREASON,ETTEC.DOCNUM,ETTEC.DOCDATE,ETTEC.PORTCODE,ETTEC.APPROVEDBY,ETTEC.LETTERNO,TO_CHAR(ETTEC.LETTERDAT,'DD/MM/YYYY') AS LETTERDAT,ETTEC.REMARKS FROM ETT_ORMCL_EOD_FILE_DATA ETTEC WHERE TO_DATE(EODDATE,'DD/MM/YY') = '" + 

	 
	 
	 
	          eodFinishedDate + "'";

	        String fileLoc = "";

	        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("ORA1");

	        System.out.println(queryCsv);

	        fileLoc = fileLoc + prodate + "/";

	        File f = new File(fileLoc);

	        if (!f.exists())

	        {

	          f.mkdir();

	          System.out.println(f.mkdir());

	        }

	        fileName = "RBI" + prodate + seq + ".orc";

	        myFilePath = fileLoc + fileName + ".csv";

	        xmlCount = generateManualORCCSVFile(queryCsv, myFilePath);

	      }

	      if (xmlCount > 0)

	      {

	        downloadFiles(myFilePath);

	        addActionMessage("File (" + fileName + ") Downloaded Successfully..");

	      }

	      if (xmlCount == 0)

	      {

	        result = "nodatafound";

	        addActionError("No Data Found");

	      }

	    }

	    catch (Exception e)

	    {

	      logger.info("----------IDPMS ------ORMCLDWLD--- [processORMCLjob]-  exception------c------" + e);

	      result = "error";

	      e.printStackTrace();

	    }

	    finally

	    {

	      DBConnectionUtility.surrenderDB(con, null, null);

	      logger.info("Finally Occurred in saveDetail");

	    }

	    return result;

	  }

	  public String downloadBEAFile()

	  {

	    Connection con = null;

	    int noOfBEA = 0;

	    int boeCount = 0;

	    int invoiceCount = 0;

	    String result = "success";

	    String fileName = "";

	    String myFilePath = "";

	    try

	    {

	      con = DBConnectionUtility.getConnection();

	      if (con == null) {

	        logger.info(

	          "----------IDPMS ------BEADWLD--- [processBEAjob]-connection not available--------------");

	      }

	      String proQuery = "SELECT TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'YYYYMMDD') AS PROCDATE," + 

	        "ORMXML_SEQNO.NEXTVAL AS BEA_SEQNO,TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'DD-MM-YY') AS EODFINISHEDDATE FROM DUAL";

	      PreparedStatement pps1 = con.prepareStatement(proQuery);

	      ResultSet rs1 = pps1.executeQuery();

	      String prodate = null;

	      String eodFinishedDate = null;

	      int seq = 0;

	      if (rs1.next())

	      {

	        prodate = rs1.getString("PROCDATE");

	        seq = rs1.getInt("BEA_SEQNO");

	        eodFinishedDate = rs1.getString("EODFINISHEDDATE");

	      }

	      closeSqlRefferance(rs1, pps1);

	      if (this.idpmsFileType.equalsIgnoreCase("XML"))

	      {

	        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

	        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

	        Document document = documentBuilder.newDocument();

	        document.setXmlStandalone(true);

	        Element root = document.createElement("bank");

	        document.appendChild(root);

	        Element rocdoc = document.createElement("checkSum");

	        root.appendChild(rocdoc);

	        Element rocdoc2 = document.createElement("billOfEntrys");

	        root.appendChild(rocdoc2);

	        String query = "SELECT BOENUMBER,BOEDATE,BOE_IMPORT_AGENCY,PORTOFDISCHARGE,IECODE,BOE_IE_NAME,BOE_AD_CODE,BOE_DETAIL_ID,INV_NO,INV_SNO,INVOICE_TERMS_OF_INVOICE,STATUS,ADJUSTMENT_REFNO,ADJ_DATE,ADJ_INVAMT,BEA_IND,ADJ_IND,DOCUMENT_NO,DOCUMENT_DATE,INVOICEPORTOFDISCHARGE,APPROVEDBY,LETTERNO,LETTERDATE,REMARKS,ADJUSTMENT_REFNO FROM ETT_BEA_EOD_FILE_DATA WHERE TO_DATE(EODDATE,'DD/MM/YY') = '" + 

	          eodFinishedDate + "' ";

	        LoggableStatement pst = new LoggableStatement(con, query);

	 
	 
	        ResultSet res = pst.executeQuery();

	        while (res.next())

	        {

	          String portOfDischarge = setValue(res.getString("PORTOFDISCHARGE"));

	          String billOfEntryNumber = setValue(res.getString("BOENUMBER"));

	          String billOfEntryDate = setValue(res.getString("BOEDATE"));

	          String ieCode = setValue(res.getString("IECODE"));

	          String adCode = setValue(res.getString("BOE_AD_CODE"));

	          String recordIndicator = setValue(res.getString("BEA_IND"));

	          String adj_Date = setValue(res.getString("ADJ_DATE"));

	          String adj_Ind = setValue(res.getString("ADJ_IND"));

	          String doc_No = setValue(res.getString("DOCUMENT_NO"));

	          String doc_Date = setValue(res.getString("DOCUMENT_DATE"));

	          String doc_Port = setValue(res.getString("INVOICEPORTOFDISCHARGE"));

	          String approvedBy = setValue(res.getString("APPROVEDBY"));

	          String letterNo = setValue(res.getString("LETTERNO"));

	          String letterDate = setValue(res.getString("LETTERDATE"));

	          String rInvSeqNo = setValue(res.getString("ADJUSTMENT_REFNO"));

	          String remarks = setValue(res.getString("REMARKS"));

	          String invSno = setValue(res.getString("INV_SNO"));

	          String invNo = setValue(res.getString("INV_NO"));

	          String adjAmt = setValue(res.getString("ADJ_INVAMT"));

	          String importAgency = setValue(res.getString("BOE_IMPORT_AGENCY"));

	          String ieName = setValue(res.getString("BOE_IE_NAME"));

	          String terms_Of_Invoice = setValue(res.getString("INVOICE_TERMS_OF_INVOICE"));

	          String status = setValue(res.getString("STATUS"));

	 
	 
	          Element rocdoc1 = document.createElement("billOfEntry");

	          rocdoc2.appendChild(rocdoc1);

	          Element pd = document.createElement("portOfDischarge");

	          pd.appendChild(document.createTextNode(portOfDischarge));

	          rocdoc1.appendChild(pd);

	          Element bno = document.createElement("billOfEntryNumber");

	          bno.appendChild(document.createTextNode(billOfEntryNumber));

	          rocdoc1.appendChild(bno);

	          Element bda = document.createElement("billOfEntryDate");

	          bda.appendChild(document.createTextNode(billOfEntryDate));

	          rocdoc1.appendChild(bda);

	          Element ie = document.createElement("IECode");

	          ie.appendChild(document.createTextNode(ieCode));

	          rocdoc1.appendChild(ie);

	          Element ad = document.createElement("ADCode");

	          ad.appendChild(document.createTextNode(adCode));

	          rocdoc1.appendChild(ad);

	          Element ri = document.createElement("recordIndicator");

	          ri.appendChild(document.createTextNode(recordIndicator));

	          rocdoc1.appendChild(ri);

	          Element aRn = document.createElement("adjustmentReferenceNumber");

	          aRn.appendChild(document.createTextNode(rInvSeqNo));

	          rocdoc1.appendChild(aRn);

	          Element cBi = document.createElement("closeofBillIndicator");

	          cBi.appendChild(document.createTextNode(String.valueOf(recordIndicator)));

	          rocdoc1.appendChild(cBi);

	          Element adjDate = document.createElement("adjustmentDate");

	          adjDate.appendChild(document.createTextNode(adj_Date));

	          rocdoc1.appendChild(adjDate);

	          Element adjInd = document.createElement("adjustmentIndicator");

	          adjInd.appendChild(document.createTextNode(adj_Ind));

	          rocdoc1.appendChild(adjInd);

	          Element docNo = document.createElement("documentNumber");

	          docNo.appendChild(document.createTextNode(doc_No));

	          rocdoc1.appendChild(docNo);

	          Element docDate = document.createElement("documentDate");

	          docDate.appendChild(document.createTextNode(doc_Date));

	          rocdoc1.appendChild(docDate);

	          Element docPort = document.createElement("documentPort");

	          docPort.appendChild(document.createTextNode(doc_Port));

	          rocdoc1.appendChild(docPort);

	          Element eb = document.createElement("approvedBy");

	          eb.appendChild(document.createTextNode(approvedBy));

	          rocdoc1.appendChild(eb);

	          Element ln = document.createElement("letterNumber");

	          ln.appendChild(document.createTextNode(letterNo));

	          rocdoc1.appendChild(ln);

	          Element ld = document.createElement("letterDate");

	          ld.appendChild(document.createTextNode(letterDate));

	          rocdoc1.appendChild(ld);

	          Element rm = document.createElement("Remark");

	          rm.appendChild(document.createTextNode(remarks));

	          rocdoc1.appendChild(rm);

	          boeCount++;
	          Element invoices = document.createElement("invoices");

	          rocdoc1.appendChild(invoices);

	          Element invoice = document.createElement("invoice");

	          invoices.appendChild(invoice);

	          Element invoiceSerial = document.createElement("invoiceSerialNo");

	          invoiceSerial.appendChild(document.createTextNode(invSno));

	          invoice.appendChild(invoiceSerial);

	          Element invoiceNum = document.createElement("invoiceNo");

	          invoiceNum.appendChild(document.createTextNode(invNo));

	          invoice.appendChild(invoiceNum);

	          Element adjustedFOBValueIC = document.createElement("adjustedInvoiceValueIC");

	          adjustedFOBValueIC.appendChild(document.createTextNode(adjAmt));

	          invoice.appendChild(adjustedFOBValueIC);

	          invoiceCount++;

	          noOfBEA++;

	        }

	        res.close();

	        pst.close();

	        Element rocdoc3 = document.createElement("noOfBOE");

	        rocdoc3.appendChild(document.createTextNode(String.valueOf(boeCount)));

	        rocdoc.appendChild(rocdoc3);

	        Element rocdocInvCount = document.createElement("noOfInvoices");

	        rocdocInvCount.appendChild(document.createTextNode(String.valueOf(invoiceCount)));

	        rocdoc.appendChild(rocdocInvCount);

	 
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();

	        Transformer transformer = transformerFactory.newTransformer();

	        DOMSource domSource = new DOMSource(document);

	        String fileLoc = "";

	        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("BEA1");

	        logger.info("File Location is : " + fileLoc);

	        fileLoc = fileLoc + prodate + "/";

	        File f = new File(fileLoc);

	        if (!f.exists())

	        {

	          f.mkdir();

	          System.out.println(f.mkdir());

	        }

	        fileName = "RBI" + prodate + seq + ".bec";

	        myFilePath = fileLoc + fileName + ".xml";

	 
	        StreamResult streamResult = new StreamResult(new File(myFilePath));

	        transformer.transform(domSource, streamResult);

	      }

	      if (this.idpmsFileType.equalsIgnoreCase("CSV"))

	      {

	        String queryCsv = "SELECT BOENUMBER,BOEDATE,BOE_IMPORT_AGENCY,PORTOFDISCHARGE,IECODE,BOE_IE_NAME,BOE_AD_CODE,BOE_DETAIL_ID,INV_NO,INV_SNO,INVOICE_TERMS_OF_INVOICE,STATUS,ADJUSTMENT_REFNO,ADJ_DATE,ADJ_INVAMT,BEA_IND,ADJ_IND,DOCUMENT_NO,DOCUMENT_DATE,INVOICEPORTOFDISCHARGE,APPROVEDBY,LETTERNO,LETTERDATE,REMARKS FROM ETT_BEA_EOD_FILE_DATA WHERE TO_DATE(EODDATE,'DD/MM/YY') = '" + 

	          eodFinishedDate + "' ";

	        String fileLoc = "";

	        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("BES1");

	        logger.info("File Location is : " + fileLoc);

	        fileLoc = fileLoc + prodate + "/";

	        File f = new File(fileLoc);

	        if (!f.exists())

	        {

	          f.mkdir();

	          System.out.println(f.mkdir());

	        }

	        fileName = "RBI" + prodate + seq + ".bec";

	        myFilePath = fileLoc + fileName + ".csv";

	        noOfBEA = generateManualBEACSVFile(queryCsv, myFilePath);

	      }

	      if (noOfBEA > 0)

	      {

	        downloadFiles(myFilePath);

	        addActionMessage("File (" + fileName + ") Downloaded Successfully..");

	      }

	      else

	      {

	        result = "nodatafound";

	        addActionError("No Data Found");

	      }

	    }

	    catch (Exception e)

	    {

	      System.out.println("Exception " + e);

	      result = "error";

	      e.printStackTrace();

	      logger.info("----------IDPMS ------BEADWLD--- [processBEAjob]- GENERATION exception-eee----" + e);

	    }

	    finally

	    {

	      DBConnectionUtility.surrenderDB(con, null, null);

	      logger.info("Finally Occurred in saveDetail");

	    }

	    return result;

	  }

	  public String downloadMBEFile()

	  {

	    logger.info("----------IDPMS ------MBEDWLD--- [processMBEjob]-----------");

	    Connection con = null;

	    String result = "success";

	    int obbCnt = 0;

	    String myFilePath = "";

	    String fileName1 = "";

	    try

	    {

	      con = DBConnectionUtility.getConnection();

	 
	      String proQuery = "SELECT TO_CHAR(TO_DATE('" + this.eodDate + "','DD-MM-YY'),'YYYYMMDD') AS PROCDATE,TO_CHAR(TO_DATE('" + this.eodDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1, " + 

	        " ETT_MANUAL_BOE_CSV_SEQ.NEXTVAL AS MBE_SEQNO FROM DUAL";

	      LoggableStatement pps1 = new LoggableStatement(con, proQuery);

	      logger.info(

	        "----------IDPMS ------MBEDWLD--- [processMBEjob]-----query------" + pps1.getQueryString());

	      ResultSet rs1 = pps1.executeQuery();

	      String prodate = null;

	      String prodate1 = null;

	      int seq = 0;

	      if (rs1.next())

	      {

	        prodate = rs1.getString("PROCDATE");

	        logger.info("----------IDPMS ------MBEDWLD--- [processMBEjob]-----prodate------" + prodate);

	        prodate1 = rs1.getString("PROCDATE1");

	        logger.info("----------IDPMS ------MBEDWLD--- [processMBEjob]-----prodate1------" + prodate1);

	        seq = rs1.getInt("MBE_SEQNO");

	        logger.info("----------IDPMS ------MBEDWLD--- [processMBEjob]-----seq------" + seq);

	      }

	      closeSqlRefferance(rs1, pps1);

	      if (this.idpmsFileType.equalsIgnoreCase("XML")) {

	        addActionError("XML File Not available for MBE");

	      }

	      if (this.idpmsFileType.equalsIgnoreCase("CSV"))

	      {

	        String fileLoc = "";

	        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("MBE1");

	 
	        fileLoc = fileLoc + prodate + "/";

	        File f = new File(fileLoc);

	        if (!f.exists()) {

	          f.mkdir();

	        }

	        fileName1 = "RBI" + prodate + seq + ".mbe";

	        myFilePath = fileLoc + fileName1 + ".csv";

	        String newCsvGenerationQuery = "SELECT BOE.BOE_PORT_OF_DISCHARGE AS \"portofDischarge\",BOE.BOE_IMPORT_AGENCY AS \"importAgency\", BOE.BOE_NUMBER AS \"billOfEntryNumber\",TO_CHAR(BOE.BOE_DATE,'DD/MM/YYYY') AS \"billOfEntryDate\",BOE.BOE_AD_CODE AS \"ADCode\", BOE.BOE_GP AS \"G-P\",BOE.BOE_IE_CODE AS \"IECode\",BOE.BOE_IE_NAME AS \"IEName\",BOE.BOE_IE_ADDRESS AS \"IEAddress\", BOE.BOE_IE_PANNUMBER AS \"IEPANNumber\",BOE.BOE_PORT_OF_SHIPMENT AS \"portOfShipment\",BOE.BOE_RECORD_INDICATOR AS \"recordIndicator\", BOE.BOE_IGMNUMBER AS \"IGMNumber\",TO_CHAR(BOE.BOE_IGMDATE,'DD/MM/YYYY') AS \"IGMDate\", BOE.BOE_MAWB_MBLNUMBER AS \"MAWB-MBLNumber\",TO_CHAR(BOE.BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS \"MAWB-MBLDate\", BOE.BOE_HAWB_HBLNUMBER  AS \"HAWB-HBLNumber\",TO_CHAR(BOE.BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS \"HAWB-HBLDate\", INV.INVOICE_SERIAL_NUMBER AS \"invoiceSerialNo\", INV.INVOICE_NUMBER AS \"invoiceNo\",INV.INVOICE_TERMS_OF_INVOICE AS \"termsOfInvoice\",INV.INVOICE_SUPPLIER_NAME AS \"supplierName\", INV.INVOICE_SUPPLIER_ADDRESS AS \"supplierAddress\",INV.INVOICE_SUPPLIER_COUNTRY AS \"supplierCountry\",INV.INVOICE_SELLER_NAME AS \"sellerName\", INV.INVOICE_SELLER_ADDRESS AS \"sellerAddress\",INV.INVOICE_SELLER_COUNTRY AS \"sellerCountry\",INV.INVOICE_FOBAMOUNT AS \"invoiceAmount\", INV.INVOICE_FOBCURRENCY AS \"invoiceCurrency\",INV.INVOICE_FRIEGHTAMOUNT AS \"freightAmount\",INV.INVOICE_FRIEGHTCURRENCYCODE AS \"freightCurrencyCode\", INV.INVOICE_INSURANCEAMOUNT AS \"insuranceAmount\",INV.INVOICE_INSURANCECURRENCY_CODE AS \"insuranceCurrencyCode\", INV.INVOICE_AGENCY_COMMISSION AS \"agencyCommission\",INV.INVOICE_AGENCY_CURRENCY AS \"agencyCurrency\", INV.INVOICE_DISCOUNT_CHARGES AS \"discountCharges\",INV.INVOICE_DISCOUNT_CURRENCY AS \"discountCurrency\", INV.INVOICE_MISCELLANEOUS_CHARGES AS \"miscellaneousCharges\",INV.INVOICE_MISCELLANEOUS_CURRENCY AS \"miscellaneousCurrency\", INV.INVOICE_THIRDPARTY_NAME AS \"thirdPartyName\",INV.INVOICE_THIRDPARTY_ADDRESS AS \"thirdPartyAddress\", INV.INVOICE_THIRDPARTY_COUNTRY AS \"thirdPartyCountry\", MBE_DATA_SEQ.NEXTVAL AS \"billId\" FROM ETT_BOE_DETAILS BOE,ETT_INVOICE_DETAILS INV WHERE BOE.BOE_NUMBER              =   INV.BOE_NUMBER AND BOE.BOE_DATE                  =   INV.BOE_DATE AND BOE.BOE_PORT_OF_DISCHARGE     =   INV.BOE_PORT_OF_DISCHARGE AND BOE.BOE_TYPE                  =   'M' AND TO_CHAR(BOE.EOD_FINISHED_TIME,'DD-MM-YY') = '" + 

	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	          prodate1 + "'";

	        obbCnt = generateManualBOEFile(newCsvGenerationQuery, myFilePath);

	        logger.info("----------IDPMS ------MBEDWLD--- [processMBEjob]--After CSV Genereate---obbCnt------" + obbCnt);

	      }

	      if (obbCnt == 0)

	      {

	        result = "nodatafound";

	        addActionError("No Data Found");

	      }

	      if (obbCnt > 0)

	      {

	        downloadFiles(myFilePath);

	        addActionMessage("File (" + fileName1 + ") Downloaded Successfully..");

	      }

	    }

	    catch (Exception e)
	    {

	        System.out.println("Exception " + e);

	        result = "error";

	        e.printStackTrace();

	        logger.info("----------IDPMS ------MBEDWLD--- [processMBEjob]---exception---" + e);

	      }

	      finally

	      {

	        DBConnectionUtility.surrenderDB(con, null, null);

	      }

	      return result;

	    }

	    public void downloadFiles(String filePath)

	    {

	      try

	      {

	        logger.info("File Path----->" + filePath);

	        File fileToDownload = new File(filePath);

	        this.inputStream = new FileInputStream(fileToDownload);

	        this.fileName = fileToDownload.getName();

	        this.contentLength = fileToDownload.length();

	      }

	      catch (Exception e)

	      {

	        logger.info("Exception in download Files--->" + e.getMessage());

	      }

	    }

	    public String setDate(Connection con)

	    {

	      PreparedStatement pst = null;

	      ResultSet rs = null;

	      CommonMethods commonMethods = null;

	      String dateValue = null;

	      try

	      {

	        commonMethods = new CommonMethods();

	        if (con == null) {

	          con = DBConnectionUtility.getConnection();

	        }

	        String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE FROM dlyprccycl";

	        pst = con.prepareStatement(query);

	        rs = pst.executeQuery();

	        if (rs.next()) {

	          dateValue = commonMethods.getEmptyIfNull(rs.getString("PROCDATE")).trim();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	      }

	      finally

	      {

	        DBConnectionUtility.surrenderDB(null, pst, rs);

	      }

	      return dateValue;

	    }

	    public void updateAuditTrail(String table, String fileName, String fileLoc, String sequence, int seqNo)

	    {

	      Connection con = null;

	      PreparedStatement pst = null;

	      String update_query = null;

	      int updateCount = 0;

	      try

	      {

	        con = DBConnectionUtility.getConnection();

	        update_query = "UPDATE " + table + " SET FILENAME=?,FILEPATH=? WHERE " + sequence + "=?";

	        pst = con.prepareStatement(update_query);

	        pst.setString(1, fileName);

	        pst.setString(2, fileLoc);

	        pst.setInt(3, seqNo);

	        updateCount = pst.executeUpdate();

	        logger.info("update count for irm----->" + updateCount);

	      }

	      catch (Exception e)

	      {

	        logger.info("Excpetion in updateTrial Method" + e.getMessage());

	      }

	      finally

	      {

	        DBConnectionUtility.surrenderDB(con, pst, null);

	      }

	    }

	    public static int generateManualORMCSVFile(String sqlQuery, String fileNameWithPath)

	    {

	      int result = 0;

	      Connection connection = null;

	      Statement stmt = null;

	      ResultSet resultSet = null;

	      boolean isheaderExists = false;

	      System.out.println("generateManualCSVFile");

	      int noOfRecords = 0;

	      try

	      {

	        connection = DBConnectionUtility.getConnection();

	        PrintWriter csvWriter = null;

	        stmt = connection.createStatement();

	        resultSet = stmt.executeQuery(sqlQuery);

	   
	        ResultSetMetaData meta = resultSet.getMetaData();

	        int numberOfColumns = meta.getColumnCount();

	        String dataHeaders = "outwardReferenceNumber|ADCode|Amount|currencyCode|paymentDate|IECode|IEName|IEAddress|IEPANNumber|isCapitalGoods|beneficiaryName|beneficiaryAccountNumber|beneficiaryCountry|SWIFT|purposeCode|Remarks|PaymentTerms|seqNo";

	        while (resultSet.next())

	        {

	          String row = resultSet.getString(1);

	          for (int i = 2; i < numberOfColumns + 1; i++)

	          {

	            String data = returnSpaceifNull(resultSet.getString(i));

	            data = data.replaceAll(",", " ");

	            if (i <= numberOfColumns) {

	              row = row + "|" + data;

	            } else {

	              row = row + "|" + data + "|";

	            }

	          }

	          if (!isheaderExists)

	          {

	            csvWriter = new PrintWriter(new File(fileNameWithPath));

	            csvWriter.println(dataHeaders);

	            isheaderExists = true;

	          }

	          csvWriter.println(row);

	   
	   
	   
	   
	   
	   
	          noOfRecords++;

	        }

	        if (csvWriter != null) {

	          csvWriter.close();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	        noOfRecords = 0;

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      finally

	      {

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      return noOfRecords;

	    }

	    public static boolean generateManualORMADVCSVFile(String sqlQuery, String fileNameWithPath)

	    {

	      boolean result = true;

	      Connection connection = null;

	      Statement stmt = null;

	      ResultSet resultSet1 = null;

	      boolean isheaderExists = false;

	      try

	      {

	        connection = DBConnectionUtility.getConnection();

	        PrintWriter csvWriter = null;

	        stmt = connection.createStatement();

	        resultSet1 = stmt.executeQuery(sqlQuery);

	   
	        ResultSetMetaData meta = resultSet1.getMetaData();

	        int numberOfColumns = meta.getColumnCount();

	        String dataHeaders = meta.getColumnName(1);

	        for (int i = 2; i < numberOfColumns + 1; i++) {

	          dataHeaders = dataHeaders + "|" + meta.getColumnName(i);

	        }

	        while (resultSet1.next())

	        {

	          String row = resultSet1.getString(1);

	   
	   
	          System.out.println("inside while of adv");

	          for (int i = 2; i < numberOfColumns + 1; i++)

	          {

	            String data = returnSpaceifNull(resultSet1.getString(i));

	            if (i <= numberOfColumns)

	            {

	              row = row + "|" + data;

	              System.out.println("if");

	            }

	            else

	            {

	              row = row + "|";

	              System.out.println("in else of adv");
	            }

	          }

	          if (!isheaderExists)

	          {

	            csvWriter = new PrintWriter(new File(fileNameWithPath));

	            csvWriter.println(dataHeaders);

	            isheaderExists = true;

	          }

	          csvWriter.println(row);

	        }

	        if (csvWriter != null) {

	          csvWriter.close();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	        result = false;

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet1.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      finally

	      {

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet1.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      return result;

	    }

	    public static int generateManualBESCSVFile(String sqlQuery, String fileNameWithPath)

	    {

	      Connection connection = null;

	      Statement stmt = null;

	      ResultSet resultSet = null;

	      boolean isheaderExists = false;

	      int noOfBES = 0;

	      System.out.println("generateManualCSVFile");

	      try

	      {

	        connection = DBConnectionUtility.getConnection();

	        PrintWriter csvWriter = null;

	        stmt = connection.createStatement();

	        resultSet = stmt.executeQuery(sqlQuery);

	   
	        ResultSetMetaData meta = resultSet.getMetaData();

	        int numberOfColumns = meta.getColumnCount();

	        String dataHeaders = "BILLOFENTRYNUMBER| BILLOFENTRYDATE| PORTOFDISCHARGE| IECODE| CHANGEIECODE| ADCODE| INVOICESERIALNO| INVOICENO| PAYMENTPARTY| PAYMENTREFERENCENUMBER| OUTWARDREFERENCENUMBER| OUTWARDREFERENCEADCODE|REMITTANCEAMOUNT|INVOICECLOSEOFBILLID |OUTWARDREALZDATE| REALZCURRENCYCODE";

	        while (resultSet.next())

	        {

	          System.out.println("generating:");

	          String row = resultSet.getString(1);

	          String portCode = row;

	          String no = "";

	          String Date = "";

	          for (int i = 2; i < numberOfColumns + 1; i++)

	          {

	            String data = returnSpaceifNull(resultSet.getString(i));

	            if (i == 3) {

	              no = data;

	            } else if (i == 4) {

	              Date = data;

	            }

	            data = data.replaceAll(",", " ");

	            row = row + "|" + data;

	          }

	          if (!isheaderExists)

	          {

	            csvWriter = new PrintWriter(new File(fileNameWithPath));

	            csvWriter.println(dataHeaders);

	            isheaderExists = true;

	          }

	          csvWriter.println(row);

	   
	   
	   
	   
	   
	   
	          noOfBES++;

	        }

	        if (csvWriter != null) {

	          csvWriter.close();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	        noOfBES = 0;

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      finally

	      {

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      return noOfBES;

	    }

	    public static int generateBESCANCSVFile(String sqlQuery, String fileNameWithPath)

	    {

	      int besCanCount = 0;

	      Connection connection = null;

	      Statement stmt = null;

	      ResultSet resultSet = null;

	      boolean isheaderExists = false;

	      System.out.println("generateManualCSVFile");

	      try

	      {

	        connection = DBConnectionUtility.getConnection();

	        PrintWriter csvWriter = null;

	        stmt = connection.createStatement();

	        resultSet = stmt.executeQuery(sqlQuery);

	   
	        ResultSetMetaData meta = resultSet.getMetaData();

	        int numberOfColumns = meta.getColumnCount();

	        String dataHeaders = "BILLOFENTRYNUMBER| BILLOFENTRYDATE| PORTOFDISCHARGE| IECODE| CHANGEIECODE| ADCODE| RECORDINDICATOR| INVOICESERIALNO| INVOICENO| PAYMENTPARTY| PAYMENTREFERENCENUMBER| OUTWARDREFERENCENUMBER| OUTWARDREFERENCEADCODE|REMITTANCEAMOUNT|INVOICECLOSEOFBILLID |OUTWARDREALZDATE| REALZCURRENCYCODE";

	        while (resultSet.next())

	        {

	          System.out.println("generating:");

	          String row = resultSet.getString(1);

	          String portCode = row;

	          String no = "";

	          String Date = "";

	          for (int i = 2; i < numberOfColumns + 1; i++)

	          {

	            String data = returnSpaceifNull(resultSet.getString(i));

	            if (i == 3) {

	              no = data;

	            } else if (i == 4) {

	              Date = data;

	            }

	            data = data.replaceAll(",", " ");

	            row = row + "|" + data;

	          }

	          if (!isheaderExists)

	          {

	            csvWriter = new PrintWriter(new File(fileNameWithPath));

	            csvWriter.println(dataHeaders);

	            isheaderExists = true;

	          }

	          csvWriter.println(row);

	          besCanCount++;
	        }

	        if (csvWriter != null) {

	          csvWriter.close();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	        besCanCount = 0;

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      finally

	      {

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      return besCanCount;

	    }

	    public static int generateManualBEACSVFile(String sqlQuery, String fileNameWithPath)

	    {

	      boolean result = true;

	      Connection connection = null;

	      Statement stmt = null;

	      ResultSet resultSet = null;

	      boolean isheaderExists = false;

	      int noOfBEA = 0;

	      System.out.println("generateManualCSVFile");

	      try

	      {

	        System.out.println("generating1:");

	        connection = DBConnectionUtility.getConnection();

	        PrintWriter csvWriter = null;

	        stmt = connection.createStatement();

	        resultSet = stmt.executeQuery(sqlQuery);

	        ResultSetMetaData meta = resultSet.getMetaData();

	        int numberOfColumns = meta.getColumnCount();

	        String dataHeaders = "BOENUMBER|BOEDATE|IMPORTAGENCY|PORTOFDISCHARGE|IECODE|IENAME|ADCODE|BOEDETAILID|INVOICENO|INVOICESERIALNO|TERMSOFINVOICE|STATUS|ADJUSTMENTREFNO|ADJUSTMENTDATE|ADJUSTMENTAMOUNT|CLOSEOFBILLINDICATOR|ADJUSTMENTINDICATOR|DOCUMENTNUMBER|DOCUMENTDATE|INVOICEPORTOFDISCHARGE|APPROVEDBY|LETTERNUMBER|LETTERDATE|REMARKS";

	        while (resultSet.next())

	        {

	          System.out.println("generating:");

	          String row = resultSet.getString(1);

	          String portCode = row;

	          String no = "";

	          String Date = "";

	          for (int i = 2; i < numberOfColumns + 1; i++)

	          {

	            String data = returnSpaceifNull(resultSet.getString(i));

	            if (i == 3) {

	              no = data;

	            } else if (i == 4) {

	              Date = data;

	            }

	            data = data.replaceAll(",", " ");

	            row = String.valueOf(row) + "|" + data;

	          }

	          if (!isheaderExists)

	          {

	            csvWriter = new PrintWriter(new File(fileNameWithPath));

	            csvWriter.println(dataHeaders);

	            isheaderExists = true;

	          }

	          csvWriter.println(row);

	          noOfBEA++;

	        }

	        if (csvWriter != null) {

	          csvWriter.close();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	        noOfBEA = 0;

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      finally

	      {

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      return noOfBEA;

	    }

	    public static int generateManualBEECSVFile(String sqlQuery, String fileNameWithPath)

	    {

	      int noOfBoe = 0;

	      Connection connection = null;

	      Statement stmt = null;

	      ResultSet resultSet = null;

	      boolean isheaderExists = false;

	      System.out.println("generateManualCSVFile");

	      try

	      {

	        connection = DBConnectionUtility.getConnection();

	        PrintWriter csvWriter = null;

	        stmt = connection.createStatement();

	        resultSet = stmt.executeQuery(sqlQuery);

	   
	        ResultSetMetaData meta = resultSet.getMetaData();

	        int numberOfColumns = meta.getColumnCount();

	        String dataHeaders = "BILLOFENTRYNUMBER|BILLOFENTRYDATE|IECODE|IENAME|ADCODE|PORTOFDISCHARGE|IMPORTAGENCY|STATUS|EXTENSIONBY|EXTENSIONDATE|LETTERNUMBER|LETTERDATE";

	        while (resultSet.next())

	        {

	          System.out.println("generating:");

	          String row = resultSet.getString(1);

	          String portCode = row;

	          String no = "";

	          String Date = "";

	          for (int i = 2; i < numberOfColumns + 1; i++)

	          {

	            noOfBoe++;

	            String data = returnSpaceifNull(resultSet.getString(i));

	            if (i == 3) {

	              no = data;

	            } else if (i == 4) {

	              Date = data;

	            }

	            data = data.replaceAll(",", " ");

	            row = row + "|" + data;

	          }

	          if (!isheaderExists)

	          {

	            csvWriter = new PrintWriter(new File(fileNameWithPath));

	            csvWriter.println(dataHeaders);

	            isheaderExists = true;

	          }

	          csvWriter.println(row);

	        }

	        if (csvWriter != null) {

	          csvWriter.close();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	        noOfBoe = 0;

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      finally

	      {

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      return noOfBoe;

	    }

	    public static int generateManualORCCSVFile(String sqlQuery, String fileNameWithPath)

	    {

	      boolean result = true;

	      Connection connection = null;

	      Statement stmt = null;

	      ResultSet resultSet = null;

	      boolean isheaderExists = false;

	      int noOfRecords = 0;

	      System.out.println("generateManualCSVFile");

	      try

	      {

	        System.out.println("generating1:");

	        connection = DBConnectionUtility.getConnection();

	        PrintWriter csvWriter = null;

	        stmt = connection.createStatement();

	        resultSet = stmt.executeQuery(sqlQuery);

	   
	        ResultSetMetaData meta = resultSet.getMetaData();

	        int numberOfColumns = meta.getColumnCount();
	        String dataHeaders = "SERIALNO|ORMID|ORMNUMBER|ADCODE|IECODE|IENAME|REMITTANCEAMOUNT|ORMCURRENCY|ORMDATE|AMOUNTUNUTILIZED|STATUS|ADJUSTMENTAMOUNT|ADJUSTMENTDATE|ADJUSTMENTREFNUMBER|REASONFORADJUSTMENT|DOCNUMBER|DOCDATE|PORTOFDISCHARGE|APPROVEDBY|LETTERNUMBER|LETTERDATE|REMARKS ";

	        while (resultSet.next())

	        {

	          System.out.println("generating:");

	          String row = resultSet.getString(1);

	          String portCode = row;

	          String no = "";

	          String Date = "";

	          for (int i = 2; i < numberOfColumns + 1; i++)

	          {

	            noOfRecords++;

	            String data = returnSpaceifNull(resultSet.getString(i));

	            if (i == 3) {

	              no = data;

	            } else if (i == 4) {

	              Date = data;

	            }

	            data = data.replaceAll(",", " ");

	            row = row + "|" + data;

	          }

	          if (!isheaderExists)

	          {

	            csvWriter = new PrintWriter(new File(fileNameWithPath));

	            csvWriter.println(dataHeaders);

	            isheaderExists = true;

	          }

	          csvWriter.println(row);

	        }

	        if (csvWriter != null) {

	          csvWriter.close();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	        result = false;

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      finally

	      {

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      return noOfRecords;

	    }

	    public static int generateManualBOEFile(String sqlQuery, String fileNameWithPath)

	    {

	      logger.info("----------IDPMS ------MBEDWLD--- [generateManualBOEFile]- ");

	      boolean result = true;

	      Connection connection = null;

	      Statement stmt = null;

	      ResultSet resultSet = null;

	      boolean isheaderExists = false;

	      int noOfBoe = 0;

	      try

	      {

	        connection = DBConnectionUtility.getConnection();

	        PrintWriter csvWriter = null;

	        stmt = connection.createStatement();

	        resultSet = stmt.executeQuery(sqlQuery);

	        ResultSetMetaData meta = resultSet.getMetaData();

	        int numberOfColumns = meta.getColumnCount();

	        String dataHeaders = meta.getColumnName(1);

	        for (int i = 2; i < numberOfColumns + 1; i++) {

	          dataHeaders = dataHeaders + "|" + meta.getColumnName(i);

	        }

	        while (resultSet.next())

	        {

	          String row = resultSet.getString(1);

	          String portCode = row;

	          String boeNo = "";

	          String boeDate = "";

	          for (int i = 2; i < numberOfColumns + 1; i++)

	          {

	            String data = returnSpaceifNull(resultSet.getString(i));

	            if (i == 3) {

	              boeNo = data;

	            } else if (i == 4) {

	              boeDate = data;

	            }

	            data = data.replaceAll(",", " ");

	            row = row + "|" + data;

	          }

	          if (!isheaderExists)

	          {

	            csvWriter = new PrintWriter(new File(fileNameWithPath));

	            csvWriter.println(dataHeaders);

	            isheaderExists = true;

	          }

	          csvWriter.println(row);

	          noOfBoe++;

	        }

	        if (csvWriter != null) {

	          csvWriter.close();

	        }

	      }

	      catch (Exception e)

	      {

	        logger.info("----------IDPMS ------MBEDWLD--- [generateManualBOEFile] ----------------------Exception-- " + e);

	        logger.info("MBE FILE GENERATION---------generateManualBOEFile------>" + e.getMessage());

	        e.printStackTrace();

	        noOfBoe = 0;

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      finally

	      {

	        try

	        {

	          connection.close();

	          stmt.close();

	          resultSet.close();

	        }

	        catch (Exception e)

	        {

	          logger.info(e.getMessage());

	        }

	      }

	      return noOfBoe;

	    }

	    public static String returnSpaceifNull(String value)

	    {

	      try

	      {

	        if (value == null) {

	          value = " ";

	        } else if (value.trim().equalsIgnoreCase("")) {

	          value = " ";

	        } else {

	          value = value.trim();

	        }

	      }

	      catch (Exception e)

	      {

	        e.printStackTrace();

	      }

	      return value;

	    }

	    public static String setValue(String val)

	    {

	      if (val == null) {

	        val = "";

	      } else {

	        val = val.trim();

	      }

	      return val;

	    }

	    public void closePreparedStatement(PreparedStatement preparedStatement)

	    {

	      try

	      {

	        if (preparedStatement != null) {

	          preparedStatement.close();

	        }

	      }

	      catch (Exception excepConnection)

	      {

	        logger.info(excepConnection.fillInStackTrace());

	      }

	    }

	    public void closeResultSet(ResultSet resultSet)

	    {

	      try

	      {

	        if (resultSet != null) {

	          resultSet.close();

	        }

	      }

	      catch (Exception excepConnection)

	      {

	        logger.info(excepConnection.fillInStackTrace());

	      }

	    }

	    public void closeSqlRefferance(ResultSet result, PreparedStatement preparedStatement)

	    {

	      closeResultSet(result);

	      closePreparedStatement(preparedStatement);

	    }

	  }
 