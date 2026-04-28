package in.co.localization.action;

import in.co.localization.dao.exception.ApplicationException;
import in.co.localization.utility.ActionConstants;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.vo.localization.EodDownloadVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.w3c.dom.Node;

public class EDPMSCustomJobAction
  extends LocalizationBaseAction
{
  private static Logger logger = Logger.getLogger(EDPMSCustomJobAction.class.getName());
  private static final long serialVersionUID = 1L;
  private InputStream inputStream;
  private String fileName;
  private long contentLength;
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
 
  public String processRODjob()
  {
    logger.info("----------EDPMS ------RODDWLD--- [processRODjob]- stpContext3----------------");
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet res = null;
    String noofshipbills = "0";
    int xmlCount = 0;
    String result = "success";
    int rodSeq = 0;
    boolean refNof = false;
    boolean billNof = false;
    boolean billDatef = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      if (con == null) {
        logger.info("----------EDPMS ------RODDWLD--- [processRODjob]- Connection Null----------------");
      }
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
     








      rodSeq = seqGeneration("ROD_SEQNO_GEN");
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("shippingBills");
      root.appendChild(rocdoc2);
      String query = "SELECT SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE,PORTCODE,SHIPPINGBILLDATE AS TEMP_SHIPPINGBILLDATE,RECORDINDICATOR, EXPORTTYPE, FORMNO,TO_CHAR(TO_DATE(LEODATE, 'dd/mm/yy'),'dd/mm/yyyy') AS LEODATE,IECODE, CHANGEDIECODE,ADCODE, ADEXPORTAGENCY, DIRECTDISPATCHINDICATOR, TRIM(ADBILLNO) AS ADBILLNO,TO_CHAR(TO_DATE(DATEOFNEGOTIATION, 'dd/mm/yy'),'dd/mm/yyyy') AS DATEOFNEGOTIATION,BUYERNAME,BUYERCOUNTRY FROM ETTV_ROD_FILES WHERE 1=1";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        query = query + " AND TRIM(ADBILLNO) = ?";
        refNof = true;
      }
      if ((this.vo.getShipBillNo() != null) && (this.vo.getShipBillNo().trim().length() > 0))
      {
        query = query + " AND TRIM(SHIPPINGBILLNO) = ?";
        billNof = true;
      }
      if ((this.vo.getShipBillDate() != null) && (this.vo.getShipBillDate().trim().length() > 0))
      {
        query = query + " AND TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy') = TO_DATE(?, 'DD/MM/YYYY')";
        billDatef = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        query = query + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = con.prepareStatement(query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (billNof) {
        ps.setString(++filtercount, this.vo.getShipBillNo().trim());
      }
      if (billDatef) {
        ps.setString(++filtercount, this.vo.getShipBillDate());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      res = ps.executeQuery();
      while (res.next())
      {
        String shipbillno = res.getString("SHIPPINGBILLNO");
        String shipbilldate = res.getString("SHIPPINGBILLDATE");
        String temp_shipbilldate = res.getString("TEMP_SHIPPINGBILLDATE");
        String portcode = res.getString("PORTCODE");
        String record = res.getString("RECORDINDICATOR");
        String exporttype = res.getString("EXPORTTYPE");
        String formno = res.getString("FORMNO");
        String leodate = res.getString("LEODATE");
        String iecode = res.getString("IECODE");
        String ciecode = res.getString("CHANGEDIECODE");
        String adcode = res.getString("ADCODE");
        String adexport = res.getString("ADEXPORTAGENCY");
        String ddindicator = res.getString("DIRECTDISPATCHINDICATOR");
        String adbillno = res.getString("ADBILLNO");
        String dateneg = res.getString("DATEOFNEGOTIATION");
        String buyername = res.getString("BUYERNAME");
        if (buyername != null) {
          buyername = buyername.replaceAll("[^A-Za-z0-9 ]", "");
        }
        String buyercountry = res.getString("BUYERCOUNTRY");
       
        String sql = " SELECT RODSTATUS FROM ETT_GR_SHP_TBL WHERE SHIPBILLDATE = '" + temp_shipbilldate +
          "' AND PORTCODE = '" + portcode + "'";
        if ((shipbillno != null) && (shipbillno.trim().length() > 0)) {
          sql = sql + " AND TRIM(SHIPBILLNO) = '" + shipbillno + "'";
        }
        if ((formno != null) && (formno.trim().length() > 0)) {
          sql = sql + " AND TRIM(FORMNO) = '" + formno + "'";
        }
        LoggableStatement pss = new LoggableStatement(con, sql);
        logger.info("----------EDPMS ------RODDWLD--- [processRODjob]- stpContext3----------------" +
          pss.getQueryString());
        ResultSet rst = pss.executeQuery();
        String status = null;
        if (rst.next()) {
          status = rst.getString("RODSTATUS");
        }
        rst.close();
        pss.close();
        if ((status != null) && ("Pending".equalsIgnoreCase(status)))
        {
          Element rocdoc1 = document.createElement("shippingBill");
          rocdoc2.appendChild(rocdoc1);
          if (portcode == null)
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(pc);
          }
          else
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(portcode));
            rocdoc1.appendChild(pc);
          }
          if (record == null)
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ri);
          }
          else
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(record));
            rocdoc1.appendChild(ri);
          }
          if (exporttype == null)
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(et);
          }
          else
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(exporttype));
            rocdoc1.appendChild(et);
          }
          if (shipbillno == null)
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbn);
          }
          else
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(shipbillno));
            rocdoc1.appendChild(sbn);
          }
          if (shipbilldate == null)
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbd);
          }
          else
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(shipbilldate));
            rocdoc1.appendChild(sbd);
          }
          if (formno == null)
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(fn);
          }
          else
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(formno));
            rocdoc1.appendChild(fn);
          }
          if (leodate == null)
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ld);
          }
          else
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(leodate));
            rocdoc1.appendChild(ld);
          }
          if (iecode == null)
          {
            Element ic = document.createElement("IECode");
            ic.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ic);
          }
          else
          {
            Element ic = document.createElement("IECode");
            ic.appendChild(document.createTextNode(iecode));
            rocdoc1.appendChild(ic);
          }
          if (ciecode == null)
          {
            Element cic = document.createElement("changedIECode");
            cic.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(cic);
          }
          else
          {
            Element cic = document.createElement("changedIECode");
            cic.appendChild(document.createTextNode(ciecode));
            rocdoc1.appendChild(cic);
          }
          if (adcode == null)
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(adcode));
            rocdoc1.appendChild(ac);
          }
          if (adexport == null)
          {
            Element aea = document.createElement("adExportAgency");
            aea.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(aea);
          }
          else
          {
            Element aea = document.createElement("adExportAgency");
            aea.appendChild(document.createTextNode(adexport));
            rocdoc1.appendChild(aea);
          }
          if (ddindicator == null)
          {
            Element dd = document.createElement("directDispatchIndicator");
            dd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(dd);
          }
          else
          {
            Element dd = document.createElement("directDispatchIndicator");
            dd.appendChild(document.createTextNode(ddindicator));
            rocdoc1.appendChild(dd);
          }
          if (adbillno == null)
          {
            Element abn = document.createElement("adBillNo");
            abn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(abn);
          }
          else
          {
            Element abn = document.createElement("adBillNo");
            abn.appendChild(document.createTextNode(adbillno));
            rocdoc1.appendChild(abn);
          }
          if (dateneg == null)
          {
            Element dn = document.createElement("dateOfNegotiation");
            dn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(dn);
          }
          else
          {
            Element dn = document.createElement("dateOfNegotiation");
            dn.appendChild(document.createTextNode(dateneg));
            rocdoc1.appendChild(dn);
          }
          if (buyername == null)
          {
            Element bn = document.createElement("buyerName");
            bn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("buyerName");
            bn.appendChild(document.createTextNode(buyername));
            rocdoc1.appendChild(bn);
          }
          if (buyercountry == null)
          {
            Element bc = document.createElement("buyerCountry");
            bc.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(bc);
          }
          else
          {
            Element bc = document.createElement("buyerCountry");
            bc.appendChild(document.createTextNode(buyercountry));
            rocdoc1.appendChild(bc);
          }
          int nobill = Integer.parseInt(noofshipbills);
          nobill++;
          noofshipbills = Integer.toString(nobill);
         
          String sq = "UPDATE ETT_GR_SHP_TBL SET RODSENT=?,RODSTATUS=? WHERE  TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) =? ";
          if ((shipbillno != null) && (shipbillno.trim().length() > 0)) {
            sq = sq + " AND TRIM(SHIPBILLNO) = '" + shipbillno + "'";
          }
          if ((formno != null) && (formno.trim().length() > 0)) {
            sq = sq + " AND TRIM(FORMNO) = '" + formno + "'";
          }
          LoggableStatement pps = new LoggableStatement(con, sq);
          pps.setString(1, "Y");
          pps.setString(2, "Completed");
          pps.setString(3, temp_shipbilldate);
          pps.setString(4, portcode);
         
          logger.info(
            "----------EDPMS ------RODDWLD--- [processRODjob]- UPDATE--- ETT_GR_SHP_TBL-------------" +
            pss.getQueryString());
         


          pps.close();
         
          insertRODAudit(shipbillno, shipbilldate, portcode, record, exporttype, formno, leodate, iecode, ciecode,
            adcode, adexport, ddindicator, adbillno, dateneg, buyername, userName, rodSeq);
          xmlCount++;
        }
        else
        {
          logger.info("Nothing to print");
        }
      }
      Element nob = document.createElement("noOfShippingBills");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,RODXML_SEQNO.NEXTVAL AS ROD_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("ROD_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       


        fileLoc = ActionConstants.ROD_ONLINE;
       


        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".rod";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_ROD_AUDIT_TABLE", fileName, fileLoc, "ROD_SEQ_NO", rodSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      result = "error";
      logger.info("----------EDPMS ------RODDWLD--- [processRODjob]- exception-------" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String processIRMjob()
  {
    logger.info("----------EDPMS ------IRMDWLD--- [processIRMjob]- ---");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    CommonMethods commonMethods = null;
    String noofshipbills = "0";
    int xmlCount = 0;
    String result = "success";
    int irmSeq = 0;
    boolean refNof = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      if (con == null) {
        logger.info("----------EDPMS ------IRMDWLD--- [processIRMjob]-Connection -Is null--");
      }
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
     
      irmSeq = seqGeneration("IRM_SEQNO_GEN");
     
      commonMethods = new CommonMethods();
     
      String getIRMData = "{call ETT_INS_IRM_TBL}";
      CallableStatement callableStatement = con.prepareCall(getIRMData);
      callableStatement.executeUpdate();
      if (callableStatement != null) {
        callableStatement.close();
      }
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("remittances");
      root.appendChild(rocdoc2);
      String query = "SELECT TRXN_REFNO,MASTER_REFNO,EVENT_REFNO,REM_AMOUNT, TO_CHAR(TO_DATE(REM_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS IRM_RECDATE, ADCODE,FIRC_FLAG,FIRC_NO,TO_CHAR(TO_DATE(FIRC_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS FIRC_DATE,FUND_FLAG,IFSCCODE,FIRCAMOUNT, CURRENCY AS IRM_RECCURR,IECODE,IENAME,REM_NAME,REM_ADDR,REM_COUNTRY,REM_BANKNAME, REM_BANKCOUNTRY,SWIFT_REFNO,PURPOSECODE,REC_IND,REMARKS FROM ETT_IRM_XML_FILES_TBL WHERE STATUS='Pending' ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        query = query + " AND TRIM(MASTER_REFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        query = query + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = new LoggableStatement(con, query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      logger.info("----------EDPMS ------IRMDWLD--- [processIRMjob]- -ETT_IRM_XML_FILES_TBL Query--" +
        ps.getQueryString());
      res = ps.executeQuery();
      while (res.next())
      {
        String transRefNo = commonMethods.getEmptyIfNull(res.getString("TRXN_REFNO")).trim();
        transRefNo = transRefNo.replaceAll("[^A-Za-z0-9 ]", "");
        String masterRefNo = commonMethods.getEmptyIfNull(res.getString("MASTER_REFNO")).trim();
        String irmRecAmt = commonMethods.getEmptyIfNull(res.getString("REM_AMOUNT")).trim();
        String irmRecDate = commonMethods.getEmptyIfNull(res.getString("IRM_RECDATE")).trim();
        String adCode = commonMethods.getEmptyIfNull(res.getString("ADCODE")).trim();
        String fircFlag = commonMethods.getEmptyIfNull(res.getString("FIRC_FLAG")).trim();
        String fircNo = commonMethods.getEmptyIfNull(res.getString("FIRC_NO")).trim();
        String fircDate = commonMethods.getEmptyIfNull(res.getString("FIRC_DATE")).trim();
        String fircAmt = commonMethods.getEmptyIfNull(res.getString("FIRCAMOUNT")).trim();
        String irmRecCurr = commonMethods.getEmptyIfNull(res.getString("IRM_RECCURR")).trim();
        String ieCode = commonMethods.getEmptyIfNull(res.getString("IECODE")).trim();
        String ieName = commonMethods.getEmptyIfNull(res.getString("IENAME")).trim();
        ieName = ieName.replaceAll("[^A-Za-z0-9 ]", "");
        String remName = commonMethods.getEmptyIfNull(res.getString("REM_NAME")).trim();
        remName = remName.replaceAll("[^A-Za-z0-9 ]", "");
        String remAddr = commonMethods.getEmptyIfNull(res.getString("REM_ADDR")).trim();
        remAddr = remAddr.replaceAll("[^A-Za-z0-9 ]", "");
        String remCountry = commonMethods.getEmptyIfNull(res.getString("REM_COUNTRY")).trim();
        String remBankName = commonMethods.getEmptyIfNull(res.getString("REM_BANKNAME")).trim();
        remBankName = remBankName.replaceAll("[^A-Za-z0-9 ]", "");
        String remBankCountry = commonMethods.getEmptyIfNull(res.getString("REM_BANKCOUNTRY")).trim();
        String swiftRefNo = commonMethods.getEmptyIfNull(res.getString("SWIFT_REFNO")).trim();
        swiftRefNo = swiftRefNo.replaceAll("[^A-Za-z0-9 ]", "");
        String purposeCode = commonMethods.getEmptyIfNull(res.getString("PURPOSECODE")).trim();
        String recInd = commonMethods.getEmptyIfNull(res.getString("REC_IND")).trim();
        String remarks = commonMethods.getEmptyIfNull(res.getString("REMARKS")).trim();
       
        Element rocdoc1 = document.createElement("remittance");
        rocdoc2.appendChild(rocdoc1);
        if (masterRefNo == null)
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(transRefNo));
          rocdoc1.appendChild(pc);
        }
        if (irmRecAmt == null)
        {
          Element ri = document.createElement("remittanceAmount");
          ri.appendChild(document.createTextNode("0"));
          rocdoc1.appendChild(ri);
        }
        else
        {
          Element ri = document.createElement("remittanceAmount");
          ri.appendChild(document.createTextNode(irmRecAmt));
          rocdoc1.appendChild(ri);
        }
        if (irmRecDate == null)
        {
          Element et = document.createElement("remittanceDate");
          et.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(et);
        }
        else
        {
          Element et = document.createElement("remittanceDate");
          et.appendChild(document.createTextNode(irmRecDate));
          rocdoc1.appendChild(et);
        }
        if (adCode == null)
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(adCode));
          rocdoc1.appendChild(sbn);
        }
        if (fircFlag == null)
        {
          Element sbd = document.createElement("fircFlag");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("fircFlag");
          sbd.appendChild(document.createTextNode(fircFlag));
          rocdoc1.appendChild(sbd);
        }
        if (fircNo == null)
        {
          Element sbd = document.createElement("fircNumber");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("fircNumber");
          sbd.appendChild(document.createTextNode(fircNo));
          rocdoc1.appendChild(sbd);
        }
        if (fircDate == null)
        {
          Element sbd = document.createElement("fircIssueDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("fircIssueDate");
          sbd.appendChild(document.createTextNode(fircDate));
          rocdoc1.appendChild(sbd);
        }
        if (fircAmt == null)
        {
          Element sbd = document.createElement("fircAmount");
          sbd.appendChild(document.createTextNode("0"));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("fircAmount");
          sbd.appendChild(document.createTextNode(fircAmt));
          rocdoc1.appendChild(sbd);
        }
        if (irmRecCurr == null)
        {
          Element fn = document.createElement("currency");
          fn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(fn);
        }
        else
        {
          Element fn = document.createElement("currency");
          fn.appendChild(document.createTextNode(irmRecCurr));
          rocdoc1.appendChild(fn);
        }
        if (ieCode == null)
        {
          Element ld = document.createElement("ieCode");
          ld.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ld);
        }
        else
        {
          Element ld = document.createElement("ieCode");
          ld.appendChild(document.createTextNode(ieCode));
          rocdoc1.appendChild(ld);
        }
        if (ieName == null)
        {
          Element ic = document.createElement("ieName");
          ic.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ic);
        }
        else
        {
          Element ic = document.createElement("ieName");
          ic.appendChild(document.createTextNode(ieName));
          rocdoc1.appendChild(ic);
        }
        if (remName == null)
        {
          Element cic = document.createElement("remitterName");
          cic.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(cic);
        }
        else
        {
          Element cic = document.createElement("remitterName");
          cic.appendChild(document.createTextNode(remName));
          rocdoc1.appendChild(cic);
        }
        if (remAddr == null)
        {
          Element cic = document.createElement("remitterAddress");
          cic.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(cic);
        }
        else
        {
          Element cic = document.createElement("remitterAddress");
          cic.appendChild(document.createTextNode(remAddr));
          rocdoc1.appendChild(cic);
        }
        if (remCountry == null)
        {
          Element ac = document.createElement("remitterCountry");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("remitterCountry");
          ac.appendChild(document.createTextNode(remCountry));
          rocdoc1.appendChild(ac);
        }
        if (remBankName == null)
        {
          Element aea = document.createElement("remitterBankName");
          aea.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(aea);
        }
        else
        {
          Element aea = document.createElement("remitterBankName");
          aea.appendChild(document.createTextNode(remBankName));
          rocdoc1.appendChild(aea);
        }
        if (remBankCountry == null)
        {
          Element dd = document.createElement("remitterBankCountry");
          dd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dd);
        }
        else
        {
          Element dd = document.createElement("remitterBankCountry");
          dd.appendChild(document.createTextNode(remBankCountry));
          rocdoc1.appendChild(dd);
        }
        if (swiftRefNo == null)
        {
          Element abn = document.createElement("swiftOtherBankRefNumber");
          abn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(abn);
        }
        else
        {
          Element abn = document.createElement("swiftOtherBankRefNumber");
          abn.appendChild(document.createTextNode(swiftRefNo));
          rocdoc1.appendChild(abn);
        }
        if (purposeCode == null)
        {
          Element abn = document.createElement("purposeOfRemittance");
          abn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(abn);
        }
        else
        {
          Element abn = document.createElement("purposeOfRemittance");
          abn.appendChild(document.createTextNode(purposeCode));
          rocdoc1.appendChild(abn);
        }
        if (recInd == null)
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(recInd));
          rocdoc1.appendChild(dn);
        }
        if (remarks == null)
        {
          Element abn = document.createElement("remarks");
          abn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(abn);
        }
        else
        {
          Element abn = document.createElement("remarks");
          abn.appendChild(document.createTextNode(remarks));
          rocdoc1.appendChild(abn);
        }
        int nobill = Integer.parseInt(noofshipbills);
        nobill++;
        noofshipbills = Integer.toString(nobill);
       
        String sq = "UPDATE ETT_IRM_XML_FILES_TBL SET STATUS='Completed' WHERE TRXN_REFNO ='" + transRefNo +
          "'";
        PreparedStatement pps = con.prepareStatement(sq);
       





        pps.close();
        if (!commonMethods.isNull(fircNo))
        {
          String sq1 = "UPDATE ETT_FIRC_LODGEMENT SET IRMSTATUS='Completed' WHERE TRANS_REF_NO='" +
            masterRefNo + "'" + " AND FIRC_SERIAL_NO='" + fircNo + "'";
         
          LoggableStatement pps1 = new LoggableStatement(con, sq1);
         




          pps1.close();
        }
        insertIRMAudit(transRefNo, masterRefNo, irmRecAmt, irmRecDate, adCode, fircFlag, fircNo, fircDate,
          fircAmt, irmRecCurr, ieCode, ieName, remName, remAddr, remCountry, remBankName, remBankCountry,
          swiftRefNo, purposeCode, recInd, remarks, userName, irmSeq);
        xmlCount++;
      }
      Element nob = document.createElement("noOfIrm");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,IRMXML_SEQNO.NEXTVAL AS IRM_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("IRM_SEQNO");
        }
        DBConnectionUtility.surrenderDB(null, pps1, rs1);
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.IRM_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".irm";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_IRM_AUDIT_TABLE", fileName, fileLoc, "IRM_SEQ_NO", irmSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------IRMDWLD--- [processIRMjob]- Exception-" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
  }
 
  public String processPRN()
  {
    String result = "success";
    String cancellationReport = "success";
    String amendmentReport = "success";
    String status = "success";
    try
    {
      result = processPRNjob();
      logger.info("----------EDPMS ------PRNDWLD--- [processPRN]- result---" + result);
     
      cancellationReport = processPRNCancelledJob();
     
      logger.info(
        "----------EDPMS ------PRNDWLD--- [processPRN]- cancellationReport---" + cancellationReport);
     
      amendmentReport = processPRNAmendJob();
      logger.info("----------EDPMS ------PRNDWLD--- [processPRN]- amendmentReport---" + amendmentReport);
      if ((result.equals("success")) || (cancellationReport.equals("success")) || (amendmentReport.equals("success"))) {
        status = "success";
      } else if ((result.equals("error")) || (cancellationReport.equals("error")) || (amendmentReport.equals("error"))) {
        status = "error";
      } else if ((result.equals("nodatafound")) || (cancellationReport.equals("nodatafound")) || (amendmentReport.equals("nodatafound"))) {
        status = "nodatafound";
      }
      logger.info("----------EDPMS ------PRNDWLD--- [processPRN]  (-_-)- amendmentReport---" + status);
    }
    catch (Exception e)
    {
      status = "error";
      e.printStackTrace();
    }
    return status;
  }
 
  public String processPRNjob()
  {
    logger.info("----------EDPMS ------PRNDWLD--- [processPRNjob]- --------------");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    String noofshipbills = "0";
    String noofinvoices = "0";
    int xmlCount = 0;
    String result = "success";
    int prnSeq = 0;
    boolean refNof = false;
    boolean billNof = false;
    boolean billDatef = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      if (con == null) {
        logger.info("----------EDPMS ------PRNDWLD--- [processPRNjob]- ------Connection Null--------");
      }
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
     










      prnSeq = seqGeneration("PRN_SEQNO_GEN");
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
      document.setXmlStandalone(true);
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("shippingBills");
      root.appendChild(rocdoc2);
     

















      String Query = "SELECT BRANCHCODE AS BRANCHCODE,TRANSDATE AS TRANSDATE ,BILLREFNO AS BILLREFNO,EVENTREFNO AS EVENTREFNO,PAY_SERIAL_NO AS PAY_SERIAL_NO, SHIPPINGBILLNO AS SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE, PORTCODE AS PORTCODE,SHIPPINGBILLDATE AS TEMP_SHIPPINGBILLDATE,FORMNO AS FORMNO,TO_CHAR(TO_DATE(LEODATE, 'dd/mm/yy'),'dd/mm/yyyy') AS LEODATE, ADCODE AS ADCODE,DECODE(EXPORTTYPE,'EDI',1,'EDF',1,'SOFTEX',2,EXPORTTYPE) AS EXPORTTYPE,RECORDINDICATOR AS RECORDINDICATOR,REALIZATIONCURR AS REALIZATIONCURR, TO_CHAR(TO_DATE(REALIZATONDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS REALIZATONDATE,PAYMENT_SEQNO AS PAYMENT_SEQNO, INWARDNO AS INWARDNO,FIRCNO AS FIRCNO,REM_ADCODE AS REM_ADCODE,PAY_PARTY AS PAY_PARTY,REALIZATIONAMOUNT AS REALIZATIONAMOUNT,PRN_FILE_STATUS FROM ETTV_PRN_SHP_DETAILS WHERE PRN_FILE_STATUS = 'N'";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        Query = Query + " AND TRIM(BILLREFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getShipBillNo() != null) && (this.vo.getShipBillNo().trim().length() > 0))
      {
        Query = Query + " AND TRIM(SHIPPINGBILLNO) = ?";
        billNof = true;
      }
      if ((this.vo.getShipBillDate() != null) && (this.vo.getShipBillDate().trim().length() > 0))
      {
        Query = Query + " AND TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy') = TO_DATE(?, 'DD/MM/YYYY')";
        billDatef = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        Query = Query + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = new LoggableStatement(con, Query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (billNof) {
        ps.setString(++filtercount, this.vo.getShipBillNo().trim());
      }
      if (billDatef) {
        ps.setString(++filtercount, this.vo.getShipBillDate());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      logger.info(
        "----------EDPMS ------PRNDWLD--- [processPRNjob]- ------ETTV_PRN_SHP_DETAILS Query--------" +
        ps.getQueryString());
     
      ResultSet results = ps.executeQuery();
      int i = 0;
      while (results.next())
      {
        logger.info("---------PRN ----processPRNjob INSIDE--data -Count------------------" + i++);
        String shipbillno = results.getString("SHIPPINGBILLNO");
        String shipbilldate = results.getString("SHIPPINGBILLDATE");
        String temp_shipbilldate = results.getString("TEMP_SHIPPINGBILLDATE");
        String portcode = results.getString("PORTCODE");
        String formno = results.getString("FORMNO");
        String leodate = results.getString("LEODATE");
        String adcode = results.getString("ADCODE");
        String exporttype = results.getString("EXPORTTYPE");
        String record = results.getString("RECORDINDICATOR");
        String billRefNo = results.getString("BILLREFNO");
        String eventRefNo = results.getString("EVENTREFNO");
        String payRefNo = results.getString("PAY_SERIAL_NO");
        String inwardNo = results.getString("INWARDNO");
        String fircNo = results.getString("FIRCNO");
        String remAdcode = results.getString("REM_ADCODE");
        String payParty = results.getString("PAY_PARTY");
        String realCurr = results.getString("REALIZATIONCURR");
        String realDate = results.getString("REALIZATONDATE");
        String ebrcNo = results.getString("PAYMENT_SEQNO");
       



        String prnStatus = results.getString("PRN_FILE_STATUS");
        if ((prnStatus != null) && (prnStatus.equalsIgnoreCase("N")))
        {
          Element rocdoc1 = document.createElement("shippingBill");
          rocdoc2.appendChild(rocdoc1);
          if (portcode == null)
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(pc);
          }
          else
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(portcode));
            rocdoc1.appendChild(pc);
          }
          if (exporttype == null)
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(et);
          }
          else
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(exporttype));
            rocdoc1.appendChild(et);
          }
          if (record == null)
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ri);
          }
          else
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(record));
            rocdoc1.appendChild(ri);
          }
          if (shipbillno == null)
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbn);
          }
          else
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(shipbillno));
            rocdoc1.appendChild(sbn);
          }
          if (shipbilldate == null)
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbd);
          }
          else
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(shipbilldate));
            rocdoc1.appendChild(sbd);
          }
          if (formno == null)
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(fn);
          }
          else
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(formno));
            rocdoc1.appendChild(fn);
          }
          if (leodate == null)
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ld);
          }
          else
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(leodate));
            rocdoc1.appendChild(ld);
          }
          if (adcode == null)
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(adcode));
            rocdoc1.appendChild(ac);
          }
          if (ebrcNo == null)
          {
            Element ac = document.createElement("paymentSequence");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("paymentSequence");
            ac.appendChild(document.createTextNode(ebrcNo));
            rocdoc1.appendChild(ac);
          }
          if (inwardNo == null)
          {
            Element ac = document.createElement("IRMNumber");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("IRMNumber");
            ac.appendChild(document.createTextNode(inwardNo));
            rocdoc1.appendChild(ac);
          }
          if ((inwardNo != null) || (fircNo == null))
          {
            Element ac = document.createElement("FIRCNumber");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("FIRCNumber");
            ac.appendChild(document.createTextNode(fircNo));
            rocdoc1.appendChild(ac);
          }
          if (remAdcode == null)
          {
            Element ac = document.createElement("remittanceAdCode");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("remittanceAdCode");
            ac.appendChild(document.createTextNode(remAdcode));
            rocdoc1.appendChild(ac);
          }
          if (payParty == null)
          {
            Element ac = document.createElement("thirdParty");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("thirdParty");
            ac.appendChild(document.createTextNode(payParty));
            rocdoc1.appendChild(ac);
          }
          if (realCurr == null)
          {
            Element ac = document.createElement("realizedCurrencyCode");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("realizedCurrencyCode");
            ac.appendChild(document.createTextNode(realCurr));
            rocdoc1.appendChild(ac);
          }
          if (realDate == null)
          {
            Element ac = document.createElement("realizationDate");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("realizationDate");
            ac.appendChild(document.createTextNode(realDate));
            rocdoc1.appendChild(ac);
          }
          int nobill = Integer.parseInt(noofshipbills);
          nobill++;
          noofshipbills = Integer.toString(nobill);
         
          Element rocdoc3 = document.createElement("invoices");
          rocdoc1.appendChild(rocdoc3);
         
          String query = "SELECT TRIM(INVSERIALNO) AS INVSERIALNO ,TRIM(INVNO) AS INVNO,TO_CHAR(TO_DATE(INVDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS INVDATE,  TRIM(REALIZATIONCURRCODE) AS REALIZATIONCURRCODE,TRIM(ACCOUNTNO) AS ACCOUNTNO,NVL(TRIM(BANKCHARGEAMT),0) AS BANKCHARGEAMT,  NVL(TRIM(FOBAMT),0) AS FOBAMT,TRIM(FOBCURRCODE) AS FOBCURRCODE,NVL(TRIM(FRIEGHTAMT),0) AS FRIEGHTAMT,TRIM(FRIEGHTCURRCODE) AS FRIEGHTCURRCODE,\t NVL(TRIM(INSAMT),0) AS INSAMT,TRIM(INSCURRCODE) AS INSCURRCODE,  TRIM(REMITTER_NAME) AS REMITTER_NAME, TRIM(REMITTER_COUNTRY) AS REMITTER_COUNTRY ,TRIM(CLOSEOFBILLIND) AS CLOSEOFBILLIND FROM ETTV_PRN_INV_DETAILS   WHERE BILLREFNO = '" +
         



            billRefNo + "' AND EVENTREFNO ='" + eventRefNo + "'" +
            "  AND PAY_SERIAL_NO ='" + payRefNo + "'" + "  AND SHIPPINGBILLDATE ='" +
            temp_shipbilldate + "'" + "  AND PORTCODE ='" + portcode + "'";
          if ((shipbillno != null) && (!shipbillno.equals("0"))) {
            query = query + " AND TRIM(SHIPPINGBILLNO) = '" + shipbillno + "'";
          }
          if ((formno != null) && (!formno.equals("0"))) {
            query = query + " AND TRIM(FORMNO) = '" + formno + "'";
          }
          if ((inwardNo != null) && (!inwardNo.equals("0"))) {
            query = query + " AND INWARDNO = '" + inwardNo + "'";
          }
          if ((fircNo != null) && (!fircNo.equals("0"))) {
            query = query + " AND FIRCNO ='" + fircNo + "'";
          }
          query = query + " ORDER BY CLOSEOFBILLIND ASC ";
         
          LoggableStatement ps1 = new LoggableStatement(con, query);
         
          logger.info(
            "----------EDPMS ------PRNDWLD--- [processPRNjob]- ------ETT_GR_REL_INV_TBL Query--------" +
            ps1.getQueryString());
         
          ResultSet results1 = ps1.executeQuery();
          while (results1.next())
          {
            String invserialno = results1.getString("INVSERIALNO");
            String invno = results1.getString("INVNO");
            String invdate = results1.getString("INVDATE");
            String accno = results1.getString("ACCOUNTNO");
            String bankingChargeAmt = results1.getString("BANKCHARGEAMT");
            String fobamt = results1.getString("FOBAMT");
            String fobcurcode = results1.getString("FOBCURRCODE");
            String friamt = results1.getString("FRIEGHTAMT");
            String fricurcode = results1.getString("FRIEGHTCURRCODE");
            String insamt = results1.getString("INSAMT");
            String inscurcode = results1.getString("INSCURRCODE");
            String remname = results1.getString("REMITTER_NAME");
            if (remname != null) {
              remname = remname.replaceAll("[^A-Za-z0-9 ]", "");
            }
            String remcountry = results1.getString("REMITTER_COUNTRY");
            String closebill = results1.getString("CLOSEOFBILLIND");
           
            Element rocdoc4 = document.createElement("invoice");
            rocdoc3.appendChild(rocdoc4);
            if (invserialno == null)
            {
              Element pc1 = document.createElement("invoiceSerialNo");
              pc1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(pc1);
            }
            else
            {
              Element pc1 = document.createElement("invoiceSerialNo");
              pc1.appendChild(document.createTextNode(invserialno));
              rocdoc4.appendChild(pc1);
            }
            if (invno == null)
            {
              Element et1 = document.createElement("invoiceNo");
              et1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(et1);
            }
            else
            {
              Element et1 = document.createElement("invoiceNo");
              Node cdata = document.createCDATASection(invno);
              et1.appendChild(cdata);
              rocdoc4.appendChild(et1);
            }
            if (invdate == null)
            {
              Element ri1 = document.createElement("invoiceDate");
              ri1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ri1);
            }
            else
            {
              Element ri1 = document.createElement("invoiceDate");
              ri1.appendChild(document.createTextNode(invdate));
              rocdoc4.appendChild(ri1);
            }
            if (accno == null)
            {
              Element ld1 = document.createElement("accountNumber");
              ld1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("accountNumber");
              ld1.appendChild(document.createTextNode(accno));
              rocdoc4.appendChild(ld1);
            }
            if (bankingChargeAmt == null)
            {
              Element fn1 = document.createElement("bankingChargesAmt");
              fn1.appendChild(document.createTextNode("0"));
              rocdoc4.appendChild(fn1);
            }
            else
            {
              Element fn1 = document.createElement("bankingChargesAmt");
              fn1.appendChild(document.createTextNode(bankingChargeAmt));
              rocdoc4.appendChild(fn1);
            }
            if (fobamt == null)
            {
              Element ld1 = document.createElement("FOBAmt");
              ld1.appendChild(document.createTextNode("0"));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("FOBAmt");
              ld1.appendChild(document.createTextNode(fobamt));
              rocdoc4.appendChild(ld1);
            }
            if (fobcurcode == null)
            {
              Element ld1 = document.createElement("FOBAmtIC");
              ld1.appendChild(document.createTextNode("0"));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("FOBAmtIC");
              ld1.appendChild(document.createTextNode(fobcurcode));
              rocdoc4.appendChild(ld1);
            }
            if (friamt == null)
            {
              Element ld1 = document.createElement("freightAmt");
              ld1.appendChild(document.createTextNode("0"));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("freightAmt");
              ld1.appendChild(document.createTextNode(friamt));
              rocdoc4.appendChild(ld1);
            }
            if (fricurcode == null)
            {
              Element ld1 = document.createElement("freightAmtIC");
              ld1.appendChild(document.createTextNode("0"));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("freightAmtIC");
              ld1.appendChild(document.createTextNode(fricurcode));
              rocdoc4.appendChild(ld1);
            }
            if (insamt == null)
            {
              Element ld1 = document.createElement("insuranceAmt");
              ld1.appendChild(document.createTextNode("0"));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("insuranceAmt");
              ld1.appendChild(document.createTextNode(insamt));
              rocdoc4.appendChild(ld1);
            }
            if (inscurcode == null)
            {
              Element ld1 = document.createElement("insuranceAmtIC");
              ld1.appendChild(document.createTextNode("0"));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("insuranceAmtIC");
              ld1.appendChild(document.createTextNode(inscurcode));
              rocdoc4.appendChild(ld1);
            }
            if (remname == null)
            {
              Element ac1 = document.createElement("remitterName");
              ac1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ac1);
            }
            else
            {
              Element ac1 = document.createElement("remitterName");
              ac1.appendChild(document.createTextNode(remname));
              rocdoc4.appendChild(ac1);
            }
            if (remcountry == null)
            {
              Element ac1 = document.createElement("remitterCountry");
              ac1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ac1);
            }
            else
            {
              Element ac1 = document.createElement("remitterCountry");
              ac1.appendChild(document.createTextNode(remcountry));
              rocdoc4.appendChild(ac1);
            }
            if (closebill == null)
            {
              Element ac1 = document.createElement("closeOfBillIndicator");
              ac1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ac1);
            }
            else
            {
              Element ac1 = document.createElement("closeOfBillIndicator");
              ac1.appendChild(document.createTextNode(closebill));
              rocdoc4.appendChild(ac1);
            }
            int noinv = Integer.parseInt(noofinvoices);
            noinv++;
            noofinvoices = Integer.toString(noinv);
           
            String s1 = "UPDATE ETT_GR_SHP_TBL SET PRNSENT=?,RSTATUS=? WHERE BILLREFNO=? AND EVENTREFNO = ? AND TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) =? ";
            if ((shipbillno != null) && (shipbillno.trim().length() > 0)) {
              s1 = s1 + " AND TRIM(SHIPBILLNO) ='" + shipbillno + "'";
            }
            if ((formno != null) && (formno.trim().length() > 0)) {
              s1 = s1 + " AND FORMNO ='" + formno + "'";
            }
            LoggableStatement pps1 = new LoggableStatement(con, s1);
            logger.info(
              "----------EDPMS ------PRNDWLD--- [processPRNjob]- ------ETT_GR_SHP_TBL Query--------" +
              pps1.getQueryString());
            pps1.setString(1, "Y");
            pps1.setString(2, "Y");
            pps1.setString(3, billRefNo);
            pps1.setString(4, eventRefNo);
            pps1.setString(5, temp_shipbilldate);
            pps1.setString(6, portcode);
           


            pps1.close();
           
            String s2 = "UPDATE ETT_GR_INV_TBL SET RSTATUS=? WHERE BILLREFNO=? AND EVENTREFNO = ? AND TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) =?  ";
            if ((shipbillno != null) && (shipbillno.trim().length() > 0)) {
              s2 = s2 + " AND TRIM(SHIPBILLNO) ='" + shipbillno + "'";
            }
            if ((formno != null) && (formno.trim().length() > 0)) {
              s2 = s2 + " AND FORMNO ='" + formno + "'";
            }
            LoggableStatement pps2 = new LoggableStatement(con, s2);
            pps2.setString(1, "Y");
            pps2.setString(2, billRefNo);
            pps2.setString(3, eventRefNo);
            pps2.setString(4, temp_shipbilldate);
            pps2.setString(5, portcode);
           
            logger.info(
              "----------EDPMS ------PRNDWLD--- [processPRNjob]- ------ETT_GR_INV_TBL Query---update query-----" +
              pps2.getQueryString());
           



            pps2.close();
           
            String s3 = "UPDATE ETT_GR_REL_SHP_TBL SET RSTATUS=? WHERE BILLREFNO = ? AND EVENTREFNO = ? AND PAY_SERIAL_NO = ? AND TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) = ? ";
            if ((shipbillno != null) && (shipbillno != "0")) {
              s3 = s3 + " AND SHIPBILLNO ='" + shipbillno + "'";
            }
            if ((formno != null) && (formno != "0")) {
              s3 = s3 + " AND FORMNO ='" + formno + "'";
            }
            if ((inwardNo != null) && (inwardNo != "0")) {
              s3 = s3 + " AND IRMNUMBER ='" + inwardNo + "'";
            }
            if ((fircNo != null) && (fircNo != "0")) {
              s3 = s3 + " AND FIRCNO ='" + fircNo + "'";
            }
            LoggableStatement pps3 = new LoggableStatement(con, s3);
            pps3.setString(1, "Y");
            pps3.setString(2, billRefNo);
            pps3.setString(3, eventRefNo);
            pps3.setString(4, payRefNo);
            pps3.setString(5, temp_shipbilldate);
            pps3.setString(6, portcode);
           
            logger.info(
              "----------EDPMS ------PRNDWLD--- [processPRNjob]- ---UPDATE---ETT_GR_REL_SHP_TBL Query---query---" +
              pps3.getQueryString());
           


            pps3.close();
           
            String s4 = "UPDATE ETT_GR_REL_INV_TBL SET RSTATUS=? WHERE BILLREFNO=? AND EVENTREFNO = ? AND PAY_SERIAL_NO=? AND TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) =? ";
            if (shipbillno != null) {
              s4 = s4 + " AND SHIPBILLNO ='" + shipbillno + "'";
            }
            if (formno != null) {
              s4 = s4 + " AND FORMNO ='" + formno + "'";
            }
            if (inwardNo != null) {
              s4 = s4 + " AND IRMNUMBER ='" + inwardNo + "'";
            }
            if (fircNo != null) {
              s4 = s4 + " AND FIRCNO ='" + fircNo + "'";
            }
            LoggableStatement pps4 = new LoggableStatement(con, s4);
            pps4.setString(1, "Y");
            pps4.setString(2, billRefNo);
            pps4.setString(3, eventRefNo);
            pps4.setString(4, payRefNo);
            pps4.setString(5, temp_shipbilldate);
            pps4.setString(6, portcode);
           



            pps4.close();
           
            insertPRNAudit(shipbillno, shipbilldate, portcode, formno, leodate, adcode, exporttype, record,
              billRefNo, eventRefNo, payRefNo, inwardNo, fircNo, remAdcode, payParty, realCurr,
              realDate, ebrcNo, invserialno, invno, invdate,
              accno, bankingChargeAmt, fobamt, fobcurcode, friamt, fricurcode, insamt, inscurcode,
              remname, userName, prnSeq);
          }
          results1.close();
          ps1.close();
          xmlCount++;
        }
        else
        {
          logger.info("Nothing to print");
        }
      }
      Element nob1 = document.createElement("noOfInvoices");
      nob1.appendChild(document.createTextNode(noofinvoices));
      rocdoc.appendChild(nob1);
     
      Element nob = document.createElement("noOfShippingBills");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("method", "html");
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,PRNXML_SEQNO.NEXTVAL AS PRN_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("PRN_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.PRN_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".prn";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StringWriter writer = new StringWriter();
        transformer.transform(domSource, new StreamResult(writer));
        String output = writer.toString();
       
        File file = new File(myFilePath);
       
        file.createNewFile();
       
        FileWriter writer1 = new FileWriter(file);
       
        writer1.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + output);
        writer1.flush();
        writer1.close();
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_PRN_AUDIT_TABLE", fileName, fileLoc, "PRN_SEQ_NO", prnSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------PRNDWLD--- [processPRNjob]- --exception---" + e);
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
 
  public String processPRNCancelledJob()
  {
    logger.info("----------EDPMS ------PRNDWLD--- [processPRNCancelledJob]- ---");
   
    String result = "success";
    String noofshipbills = "0";
    String noofinvoices = "0";
    int xmlCount = 0;
   
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
   
    Connection connection1 = null;
    LoggableStatement preparedStatement1 = null;
    ResultSet resultSet1 = null;
    int prnSeq = 0;
    try
    {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
      document.setXmlStandalone(true);
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("shippingBills");
      root.appendChild(rocdoc2);
     
      connection = DBConnectionUtility.getConnection();
      if (connection == null) {
        logger.info("----------EDPMS ------PRNDWLD--- [processPRNCancelledJob]- -Connection Null--");
      }
      prnSeq = seqGeneration("PRN_SEQNO_GEN");
     
      String shipQuery = "SELECT GR_NUMBER,SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') SHIPPINGBILLDATE,PORTCODE,FORMNO,TO_CHAR(TO_DATE(LEODATE, 'dd/mm/yy'),'dd/mm/yyyy') LEODATE,ADCODE,DECODE(EXPORTTYPE,'EDI',1,'EDF',1,'SOFTEX',2,EXPORTTYPE) AS EXPORTTYPE,'3' RECORDINDICATOR,INWARDNO,FIRCNO,REM_ADCODE,PAY_PARTY,REALIZATONCURR,TO_CHAR(TO_DATE(REALIZATONDATE, 'dd/mm/yy'),'dd/mm/yyyy') REALIZATONDATE,PAY_SEQNO FROM ETT_PRN_SHP_ACK_STG WHERE UPPER(TRIM(STATUS)) = 'C' AND TRIM(EODSTATUS) IS NULL ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0)) {
        shipQuery = shipQuery + " AND TRIM(GR_NUMBER) = ?";
      }
      preparedStatement = new LoggableStatement(connection, shipQuery);
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0)) {
        preparedStatement.setString(1, this.vo.getBillRefNo());
      }
      logger.info(
        "----------EDPMS ------PRNDWLD--- [processPRNCancelledJob]- -ETT_PRN_SHP_ACK_STG -----------" +
        preparedStatement.getQueryString());
     
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        String grNumber = resultSet.getString("GR_NUMBER");
        String shipbillno = resultSet.getString("SHIPPINGBILLNO");
        String shipbilldate = resultSet.getString("SHIPPINGBILLDATE");
        String portcode = resultSet.getString("PORTCODE");
        String formno = resultSet.getString("FORMNO");
        String leodate = resultSet.getString("LEODATE");
        String adcode = resultSet.getString("ADCODE");
        String exporttype = resultSet.getString("EXPORTTYPE");
        String record = resultSet.getString("RECORDINDICATOR");
        String inwardNo = resultSet.getString("INWARDNO");
        String fircNo = resultSet.getString("FIRCNO");
        String remAdcode = resultSet.getString("REM_ADCODE");
        String payParty = resultSet.getString("PAY_PARTY");
        String realCurr = resultSet.getString("REALIZATONCURR");
        String realDate = resultSet.getString("REALIZATONDATE");
        String ebrcNo = resultSet.getString("PAY_SEQNO");
       

        Element rocdoc1 = document.createElement("shippingBill");
        rocdoc2.appendChild(rocdoc1);
        if (portcode == null)
        {
          Element pc = document.createElement("portCode");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("portCode");
          pc.appendChild(document.createTextNode(portcode));
          rocdoc1.appendChild(pc);
        }
        if (exporttype == null)
        {
          Element et = document.createElement("exportType");
          et.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(et);
        }
        else
        {
          Element et = document.createElement("exportType");
          et.appendChild(document.createTextNode(exporttype));
          rocdoc1.appendChild(et);
        }
        if (record == null)
        {
          Element ri = document.createElement("recordIndicator");
          ri.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ri);
        }
        else
        {
          Element ri = document.createElement("recordIndicator");
          ri.appendChild(document.createTextNode(record));
          rocdoc1.appendChild(ri);
        }
        if (shipbillno == null)
        {
          Element sbn = document.createElement("shippingBillNo");
          sbn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("shippingBillNo");
          sbn.appendChild(document.createTextNode(shipbillno));
          rocdoc1.appendChild(sbn);
        }
        if (shipbilldate == null)
        {
          Element sbd = document.createElement("shippingBillDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("shippingBillDate");
          sbd.appendChild(document.createTextNode(shipbilldate));
          rocdoc1.appendChild(sbd);
        }
        if (formno == null)
        {
          Element fn = document.createElement("formNo");
          fn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(fn);
        }
        else
        {
          Element fn = document.createElement("formNo");
          fn.appendChild(document.createTextNode(formno));
          rocdoc1.appendChild(fn);
        }
        if (leodate == null)
        {
          Element ld = document.createElement("LEODate");
          ld.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ld);
        }
        else
        {
          Element ld = document.createElement("LEODate");
          ld.appendChild(document.createTextNode(leodate));
          rocdoc1.appendChild(ld);
        }
        if (adcode == null)
        {
          Element ac = document.createElement("adCode");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("adCode");
          ac.appendChild(document.createTextNode(adcode));
          rocdoc1.appendChild(ac);
        }
        if (ebrcNo == null)
        {
          Element ac = document.createElement("paymentSequence");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("paymentSequence");
          ac.appendChild(document.createTextNode(ebrcNo));
          rocdoc1.appendChild(ac);
        }
        if (inwardNo == null)
        {
          Element ac = document.createElement("IRMNumber");
          ac.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("IRMNumber");
          ac.appendChild(document.createTextNode(inwardNo));
          rocdoc1.appendChild(ac);
        }
        if ((inwardNo != null) || (fircNo == null))
        {
          Element ac = document.createElement("FIRCNumber");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("FIRCNumber");
          ac.appendChild(document.createTextNode(fircNo));
          rocdoc1.appendChild(ac);
        }
        if (remAdcode == null)
        {
          Element ac = document.createElement("remittanceAdCode");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("remittanceAdCode");
          ac.appendChild(document.createTextNode(remAdcode));
          rocdoc1.appendChild(ac);
        }
        if (payParty == null)
        {
          Element ac = document.createElement("thirdParty");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("thirdParty");
          ac.appendChild(document.createTextNode(payParty));
          rocdoc1.appendChild(ac);
        }
        if (realCurr == null)
        {
          Element ac = document.createElement("realizedCurrencyCode");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("realizedCurrencyCode");
          ac.appendChild(document.createTextNode(realCurr));
          rocdoc1.appendChild(ac);
        }
        if (realDate == null)
        {
          Element ac = document.createElement("realizationDate");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("realizationDate");
          ac.appendChild(document.createTextNode(realDate));
          rocdoc1.appendChild(ac);
        }
        connection1 = DBConnectionUtility.getConnection();
        String invQuery = "SELECT INVOICESERIALNO,INVOICENO,TO_CHAR(TO_DATE(INVOICEDATE, 'dd/mm/yy'),'dd/mm/yyyy') INVOICEDATE,ACCOUNTNUMBER,NVL(BANK_CHG,0) AS BANK_CHG,NVL(FOBAMT,0) AS FOBAMT,NVL(FOBAMTIC,0) AS FOBAMTIC,NVL(FREIGHTAMT,0) AS FREIGHTAMT,NVL(FREIGHTAMTIC,0) AS FREIGHTAMTIC,NVL(INSURANCEAMT,0) AS INSURANCEAMT,NVL(INSURANCEAMTIC,0) AS INSURANCEAMTIC,REMITTERNAME,REMITTERCOUNTRY,CLOSEOFBILLINDICATOR FROM ETT_PRN_SHP_INV_ACK_STG WHERE GR_SHP_NO = ? ";
       


        preparedStatement1 = new LoggableStatement(connection, invQuery);
        logger.info(
          "----------EDPMS ------PRNDWLD--- [processPRNCancelledJob]- -ETT_PRN_SHP_INV_ACK_STG -----------" +
          preparedStatement1.getQueryString());
        preparedStatement1.setString(1, grNumber.trim());
        resultSet1 = preparedStatement1.executeQuery();
        while (resultSet1.next())
        {
          String invserialno = resultSet1.getString("INVOICESERIALNO");
          String invno = resultSet1.getString("INVOICENO");
          String invdate = resultSet1.getString("INVOICEDATE");
          String accno = resultSet1.getString("ACCOUNTNUMBER");
          String bankingChargeAmt = resultSet1.getString("BANK_CHG");
          String fobamt = resultSet1.getString("FOBAMT");
          String fobcurcode = resultSet1.getString("FOBAMTIC");
          String friamt = resultSet1.getString("FREIGHTAMT");
          String fricurcode = resultSet1.getString("FREIGHTAMTIC");
          String insamt = resultSet1.getString("INSURANCEAMT");
          String inscurcode = resultSet1.getString("INSURANCEAMTIC");
          String remname = resultSet1.getString("REMITTERNAME");
          if (remname != null) {
            remname = remname.replaceAll("[^A-Za-z0-9 ]", "");
          }
          String remcountry = resultSet1.getString("REMITTERCOUNTRY");
          String closebill = resultSet1.getString("CLOSEOFBILLINDICATOR");
         
          int nobill = Integer.parseInt(noofshipbills);
          nobill++;
          noofshipbills = Integer.toString(nobill);
         
          Element rocdoc3 = document.createElement("invoices");
          rocdoc1.appendChild(rocdoc3);
         
          Element rocdoc4 = document.createElement("invoice");
          rocdoc3.appendChild(rocdoc4);
          if (invserialno == null)
          {
            Element pc1 = document.createElement("invoiceSerialNo");
            pc1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(pc1);
          }
          else
          {
            Element pc1 = document.createElement("invoiceSerialNo");
            pc1.appendChild(document.createTextNode(invserialno));
            rocdoc4.appendChild(pc1);
          }
          if (invno == null)
          {
            Element et1 = document.createElement("invoiceNo");
            et1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(et1);
          }
          else
          {
            Element et1 = document.createElement("invoiceNo");
            Node cdata = document.createCDATASection(invno);
            et1.appendChild(cdata);
            rocdoc4.appendChild(et1);
          }
          if (invdate == null)
          {
            Element ri1 = document.createElement("invoiceDate");
            ri1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ri1);
          }
          else
          {
            Element ri1 = document.createElement("invoiceDate");
            ri1.appendChild(document.createTextNode(invdate));
            rocdoc4.appendChild(ri1);
          }
          if (accno == null)
          {
            Element ld1 = document.createElement("accountNumber");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("accountNumber");
            ld1.appendChild(document.createTextNode(accno));
            rocdoc4.appendChild(ld1);
          }
          if (bankingChargeAmt == null)
          {
            Element fn1 = document.createElement("bankingChargesAmt");
            fn1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(fn1);
          }
          else
          {
            Element fn1 = document.createElement("bankingChargesAmt");
            fn1.appendChild(document.createTextNode(bankingChargeAmt));
            rocdoc4.appendChild(fn1);
          }
          if (fobamt == null)
          {
            Element ld1 = document.createElement("FOBAmt");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("FOBAmt");
            ld1.appendChild(document.createTextNode(fobamt));
            rocdoc4.appendChild(ld1);
          }
          if (fobcurcode == null)
          {
            Element ld1 = document.createElement("FOBAmtIC");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("FOBAmtIC");
            ld1.appendChild(document.createTextNode(fobcurcode));
            rocdoc4.appendChild(ld1);
          }
          if (friamt == null)
          {
            Element ld1 = document.createElement("freightAmt");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("freightAmt");
            ld1.appendChild(document.createTextNode(friamt));
            rocdoc4.appendChild(ld1);
          }
          if (fricurcode == null)
          {
            Element ld1 = document.createElement("freightAmtIC");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("freightAmtIC");
            ld1.appendChild(document.createTextNode(fricurcode));
            rocdoc4.appendChild(ld1);
          }
          if (insamt == null)
          {
            Element ld1 = document.createElement("insuranceAmt");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("insuranceAmt");
            ld1.appendChild(document.createTextNode(insamt));
            rocdoc4.appendChild(ld1);
          }
          if (inscurcode == null)
          {
            Element ld1 = document.createElement("insuranceAmtIC");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("insuranceAmtIC");
            ld1.appendChild(document.createTextNode(inscurcode));
            rocdoc4.appendChild(ld1);
          }
          if (remname == null)
          {
            Element ac1 = document.createElement("remitterName");
            ac1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ac1);
          }
          else
          {
            Element ac1 = document.createElement("remitterName");
            ac1.appendChild(document.createTextNode(remname));
            rocdoc4.appendChild(ac1);
          }
          if (remcountry == null)
          {
            Element ac1 = document.createElement("remitterCountry");
            ac1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ac1);
          }
          else
          {
            Element ac1 = document.createElement("remitterCountry");
            ac1.appendChild(document.createTextNode(remcountry));
            rocdoc4.appendChild(ac1);
          }
          if (closebill == null)
          {
            Element ac1 = document.createElement("closeOfBillIndicator");
            ac1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ac1);
          }
          else
          {
            Element ac1 = document.createElement("closeOfBillIndicator");
            ac1.appendChild(document.createTextNode(closebill));
            rocdoc4.appendChild(ac1);
          }
          int noinv = Integer.parseInt(noofinvoices);
          noinv++;
          noofinvoices = Integer.toString(noinv);
         
          insertPRNCancelAudit(grNumber, shipbillno, shipbilldate, portcode, formno, leodate, adcode,
            exporttype, record, inwardNo, fircNo, remAdcode, payParty, realCurr, realDate, ebrcNo,
            invserialno, invno, invdate, accno, bankingChargeAmt, fobamt, fobcurcode, friamt,
            fricurcode, insamt, inscurcode, remname, userName, prnSeq);
        }
        xmlCount++;
       
        String s1 = "UPDATE ETT_PRN_SHP_ACK_STG SET EODSTATUS = ?  WHERE GR_NUMBER = ? ";
       
        LoggableStatement pps1 = new LoggableStatement(connection, s1);
        pps1.setString(1, "C");
        pps1.setString(2, grNumber.trim());
       
        logger.info(
          "----------EDPMS ------PRNDWLD--- [processPRNCancelledJob]- -ETT_PRN_SHP_ACK_STG -----------" +
          pps1.getQueryString());
       

        pps1.close();
      }
      Element nob1 = document.createElement("noOfInvoices");
      nob1.appendChild(document.createTextNode(noofinvoices));
      rocdoc.appendChild(nob1);
     
      Element nob = document.createElement("noOfShippingBills");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("method", "html");
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,PRNXML_SEQNO.NEXTVAL AS PRN_SEQNO FROM DLYPRCCYCL";
       

        PreparedStatement pps1 = connection.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("PRN_SEQNO");
        }
        logger.info("==================>>> prodate : " + prodate + " : seq : " + seq);
        rs1.close();
        pps1.close();
        logger.info("Milestone 06 EDPMS-PRN completed..!");
        String fileLoc = "";
       

        fileLoc = ActionConstants.PRN_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        logger.info("==================>>> fileLoc : " + fileLoc);
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".prn";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StringWriter writer = new StringWriter();
        transformer.transform(domSource, new StreamResult(writer));
        String output = writer.toString();
       
        File file = new File(myFilePath);
       
        file.createNewFile();
       
        FileWriter writer1 = new FileWriter(file);
       
        writer1.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + output);
        writer1.flush();
        writer1.close();
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_PRN_CANCEL_AUDIT_TABLE", fileName, fileLoc, "PRN_SEQ_NO", prnSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      result = "error";
      logger.info("----------EDPMS ------PRNDWLD--- [processPRNCancelledJob]- -exception -----------" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, preparedStatement, resultSet);
      DBConnectionUtility.surrenderDB(connection1, preparedStatement1, resultSet1);
    }
    return result;
  }
 
  public String processPRNAmendJob()
  {
    logger.info("----------EDPMS ------PRNDWLD--- [processPRNAmendJob]- amendmentReport---");
   
    String result = "success";
    String noofshipbills = "0";
    String noofinvoices = "0";
    int xmlCount = 0;
   
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
   
    Connection connection1 = null;
    LoggableStatement preparedStatement1 = null;
    ResultSet resultSet1 = null;
    int prnSeq = 0;
    try
    {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
      document.setXmlStandalone(true);
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("shippingBills");
      root.appendChild(rocdoc2);
     
      connection = DBConnectionUtility.getConnection();
      if (connection == null) {
        logger.info("----------EDPMS ------PRNDWLD--- [processPRNAmendJob]- Connection null---");
      }
      prnSeq = seqGeneration("PRN_SEQNO_GEN");
     
      String shipQuery = "SELECT GR_NUMBER,SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') SHIPPINGBILLDATE,PORTCODE,FORMNO,TO_CHAR(TO_DATE(LEODATE, 'dd/mm/yy'),'dd/mm/yyyy') LEODATE,ADCODE,DECODE(EXPORTTYPE,'EDI',1,'EDF',1,'SOFTEX',2,EXPORTTYPE) AS EXPORTTYPE,'2' RECORDINDICATOR,INWARDNO,FIRCNO,REM_ADCODE,PAY_PARTY,REALIZATONCURR,TO_CHAR(TO_DATE(REALIZATONDATE, 'dd/mm/yy'),'dd/mm/yyyy') REALIZATONDATE,PAY_SEQNO FROM ETT_PRN_SHP_ACK_STG WHERE UPPER(TRIM(STATUS)) = 'A' AND TRIM(EODSTATUS) IS NULL ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0)) {
        shipQuery = shipQuery + " AND TRIM(GR_NUMBER) = ?";
      }
      preparedStatement = new LoggableStatement(connection, shipQuery);
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0)) {
        preparedStatement.setString(1, this.vo.getBillRefNo());
      }
      logger.info("----------EDPMS ------PRNDWLD--- [processPRNAmendJob]- shipQuery -----------" +
        preparedStatement.getQueryString());
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        String grNumber = resultSet.getString("GR_NUMBER");
        String shipbillno = resultSet.getString("SHIPPINGBILLNO");
        String shipbilldate = resultSet.getString("SHIPPINGBILLDATE");
        String portcode = resultSet.getString("PORTCODE");
        String formno = resultSet.getString("FORMNO");
        String leodate = resultSet.getString("LEODATE");
        String adcode = resultSet.getString("ADCODE");
        String exporttype = resultSet.getString("EXPORTTYPE");
        String record = resultSet.getString("RECORDINDICATOR");
        String inwardNo = resultSet.getString("INWARDNO");
        String fircNo = resultSet.getString("FIRCNO");
        String remAdcode = resultSet.getString("REM_ADCODE");
        String payParty = resultSet.getString("PAY_PARTY");
        String realCurr = resultSet.getString("REALIZATONCURR");
        String realDate = resultSet.getString("REALIZATONDATE");
        String ebrcNo = resultSet.getString("PAY_SEQNO");
       

        Element rocdoc1 = document.createElement("shippingBill");
        rocdoc2.appendChild(rocdoc1);
        if (portcode == null)
        {
          Element pc = document.createElement("portCode");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("portCode");
          pc.appendChild(document.createTextNode(portcode));
          rocdoc1.appendChild(pc);
        }
        if (exporttype == null)
        {
          Element et = document.createElement("exportType");
          et.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(et);
        }
        else
        {
          Element et = document.createElement("exportType");
          et.appendChild(document.createTextNode(exporttype));
          rocdoc1.appendChild(et);
        }
        if (record == null)
        {
          Element ri = document.createElement("recordIndicator");
          ri.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ri);
        }
        else
        {
          Element ri = document.createElement("recordIndicator");
          ri.appendChild(document.createTextNode(record));
          rocdoc1.appendChild(ri);
        }
        if (shipbillno == null)
        {
          Element sbn = document.createElement("shippingBillNo");
          sbn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("shippingBillNo");
          sbn.appendChild(document.createTextNode(shipbillno));
          rocdoc1.appendChild(sbn);
        }
        if (shipbilldate == null)
        {
          Element sbd = document.createElement("shippingBillDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("shippingBillDate");
          sbd.appendChild(document.createTextNode(shipbilldate));
          rocdoc1.appendChild(sbd);
        }
        if (formno == null)
        {
          Element fn = document.createElement("formNo");
          fn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(fn);
        }
        else
        {
          Element fn = document.createElement("formNo");
          fn.appendChild(document.createTextNode(formno));
          rocdoc1.appendChild(fn);
        }
        if (leodate == null)
        {
          Element ld = document.createElement("LEODate");
          ld.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ld);
        }
        else
        {
          Element ld = document.createElement("LEODate");
          ld.appendChild(document.createTextNode(leodate));
          rocdoc1.appendChild(ld);
        }
        if (adcode == null)
        {
          Element ac = document.createElement("adCode");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("adCode");
          ac.appendChild(document.createTextNode(adcode));
          rocdoc1.appendChild(ac);
        }
        if (ebrcNo == null)
        {
          Element ac = document.createElement("paymentSequence");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("paymentSequence");
          ac.appendChild(document.createTextNode(ebrcNo));
          rocdoc1.appendChild(ac);
        }
        if (inwardNo == null)
        {
          Element ac = document.createElement("IRMNumber");
          ac.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("IRMNumber");
          ac.appendChild(document.createTextNode(inwardNo));
          rocdoc1.appendChild(ac);
        }
        if ((inwardNo != null) || (fircNo == null))
        {
          Element ac = document.createElement("FIRCNumber");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("FIRCNumber");
          ac.appendChild(document.createTextNode(fircNo));
          rocdoc1.appendChild(ac);
        }
        if (remAdcode == null)
        {
          Element ac = document.createElement("remittanceAdCode");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("remittanceAdCode");
          ac.appendChild(document.createTextNode(remAdcode));
          rocdoc1.appendChild(ac);
        }
        if (payParty == null)
        {
          Element ac = document.createElement("thirdParty");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("thirdParty");
          ac.appendChild(document.createTextNode(payParty));
          rocdoc1.appendChild(ac);
        }
        if (realCurr == null)
        {
          Element ac = document.createElement("realizedCurrencyCode");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("realizedCurrencyCode");
          ac.appendChild(document.createTextNode(realCurr));
          rocdoc1.appendChild(ac);
        }
        if (realDate == null)
        {
          Element ac = document.createElement("realizationDate");
          ac.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("realizationDate");
          ac.appendChild(document.createTextNode(realDate));
          rocdoc1.appendChild(ac);
        }
        connection1 = DBConnectionUtility.getConnection();
        if (connection1 == null) {
          logger.info(
            "----------EDPMS ------PRNDWLD--- [processPRNAmendJob]-Connection Null ----------");
        }
        String invQuery = "SELECT INVOICESERIALNO,INVOICENO,TO_CHAR(TO_DATE(INVOICEDATE, 'dd/mm/yy'),'dd/mm/yyyy') INVOICEDATE,ACCOUNTNUMBER,NVL(BANK_CHG,0) AS BANK_CHG,NVL(FOBAMT,0) AS FOBAMT,NVL(FOBAMTIC,0) AS FOBAMTIC,NVL(FREIGHTAMT,0) AS FREIGHTAMT,NVL(FREIGHTAMTIC,0) AS FREIGHTAMTIC,NVL(INSURANCEAMT,0) AS INSURANCEAMT,NVL(INSURANCEAMTIC,0) AS INSURANCEAMTIC,REMITTERNAME,REMITTERCOUNTRY,CLOSEOFBILLINDICATOR FROM ETT_PRN_SHP_INV_ACK_STG WHERE GR_SHP_NO = ? ";
       



        preparedStatement1 = new LoggableStatement(connection1, invQuery);
        preparedStatement1.setString(1, grNumber.trim());
       
        logger.info(
          "----------EDPMS ------PRNDWLD--- [processPRNAmendJob]- ETT_PRN_SHP_INV_ACK_STG -----------" +
          preparedStatement1.getQueryString());
        resultSet1 = preparedStatement1.executeQuery();
        while (resultSet1.next())
        {
          String invserialno = resultSet1.getString("INVOICESERIALNO");
          String invno = resultSet1.getString("INVOICENO");
          String invdate = resultSet1.getString("INVOICEDATE");
          String accno = resultSet1.getString("ACCOUNTNUMBER");
          String bankingChargeAmt = resultSet1.getString("BANK_CHG");
          String fobamt = resultSet1.getString("FOBAMT");
          String fobcurcode = resultSet1.getString("FOBAMTIC");
          String friamt = resultSet1.getString("FREIGHTAMT");
          String fricurcode = resultSet1.getString("FREIGHTAMTIC");
          String insamt = resultSet1.getString("INSURANCEAMT");
          String inscurcode = resultSet1.getString("INSURANCEAMTIC");
          String remname = resultSet1.getString("REMITTERNAME");
          if (remname != null) {
            remname = remname.replaceAll("[^A-Za-z0-9 ]", "");
          }
          String remcountry = resultSet1.getString("REMITTERCOUNTRY");
          String closebill = resultSet1.getString("CLOSEOFBILLINDICATOR");
         
          int nobill = Integer.parseInt(noofshipbills);
          nobill++;
          noofshipbills = Integer.toString(nobill);
         
          Element rocdoc3 = document.createElement("invoices");
          rocdoc1.appendChild(rocdoc3);
         
          Element rocdoc4 = document.createElement("invoice");
          rocdoc3.appendChild(rocdoc4);
          if (invserialno == null)
          {
            Element pc1 = document.createElement("invoiceSerialNo");
            pc1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(pc1);
          }
          else
          {
            Element pc1 = document.createElement("invoiceSerialNo");
            pc1.appendChild(document.createTextNode(invserialno));
            rocdoc4.appendChild(pc1);
          }
          if (invno == null)
          {
            Element et1 = document.createElement("invoiceNo");
            et1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(et1);
          }
          else
          {
            Element et1 = document.createElement("invoiceNo");
            Node cdata = document.createCDATASection(invno);
            et1.appendChild(cdata);
            rocdoc4.appendChild(et1);
          }
          if (invdate == null)
          {
            Element ri1 = document.createElement("invoiceDate");
            ri1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ri1);
          }
          else
          {
            Element ri1 = document.createElement("invoiceDate");
            ri1.appendChild(document.createTextNode(invdate));
            rocdoc4.appendChild(ri1);
          }
          if (accno == null)
          {
            Element ld1 = document.createElement("accountNumber");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("accountNumber");
            ld1.appendChild(document.createTextNode(accno));
            rocdoc4.appendChild(ld1);
          }
          if (bankingChargeAmt == null)
          {
            Element fn1 = document.createElement("bankingChargesAmt");
            fn1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(fn1);
          }
          else
          {
            Element fn1 = document.createElement("bankingChargesAmt");
            fn1.appendChild(document.createTextNode(bankingChargeAmt));
            rocdoc4.appendChild(fn1);
          }
          if (fobamt == null)
          {
            Element ld1 = document.createElement("FOBAmt");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("FOBAmt");
            ld1.appendChild(document.createTextNode(fobamt));
            rocdoc4.appendChild(ld1);
          }
          if (fobcurcode == null)
          {
            Element ld1 = document.createElement("FOBAmtIC");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("FOBAmtIC");
            ld1.appendChild(document.createTextNode(fobcurcode));
            rocdoc4.appendChild(ld1);
          }
          if (friamt == null)
          {
            Element ld1 = document.createElement("freightAmt");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("freightAmt");
            ld1.appendChild(document.createTextNode(friamt));
            rocdoc4.appendChild(ld1);
          }
          if (fricurcode == null)
          {
            Element ld1 = document.createElement("freightAmtIC");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("freightAmtIC");
            ld1.appendChild(document.createTextNode(fricurcode));
            rocdoc4.appendChild(ld1);
          }
          if (insamt == null)
          {
            Element ld1 = document.createElement("insuranceAmt");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("insuranceAmt");
            ld1.appendChild(document.createTextNode(insamt));
            rocdoc4.appendChild(ld1);
          }
          if (inscurcode == null)
          {
            Element ld1 = document.createElement("insuranceAmtIC");
            ld1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ld1);
          }
          else
          {
            Element ld1 = document.createElement("insuranceAmtIC");
            ld1.appendChild(document.createTextNode(inscurcode));
            rocdoc4.appendChild(ld1);
          }
          if (remname == null)
          {
            Element ac1 = document.createElement("remitterName");
            ac1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ac1);
          }
          else
          {
            Element ac1 = document.createElement("remitterName");
            ac1.appendChild(document.createTextNode(remname));
            rocdoc4.appendChild(ac1);
          }
          if (remcountry == null)
          {
            Element ac1 = document.createElement("remitterCountry");
            ac1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ac1);
          }
          else
          {
            Element ac1 = document.createElement("remitterCountry");
            ac1.appendChild(document.createTextNode(remcountry));
            rocdoc4.appendChild(ac1);
          }
          if (closebill == null)
          {
            Element ac1 = document.createElement("closeOfBillIndicator");
            ac1.appendChild(document.createTextNode(""));
            rocdoc4.appendChild(ac1);
          }
          else
          {
            Element ac1 = document.createElement("closeOfBillIndicator");
            ac1.appendChild(document.createTextNode(closebill));
            rocdoc4.appendChild(ac1);
          }
          int noinv = Integer.parseInt(noofinvoices);
          noinv++;
          noofinvoices = Integer.toString(noinv);
         
          insertPRNAmendAudit(grNumber, shipbillno, shipbilldate, portcode, formno, leodate, adcode,
            exporttype, record, inwardNo, fircNo, remAdcode, payParty, realCurr, realDate, ebrcNo,
            invserialno, invno, invdate, accno, bankingChargeAmt, fobamt, fobcurcode, friamt,
            fricurcode, insamt, inscurcode, remname, userName, prnSeq);
        }
        xmlCount++;
       
        String s1 = "UPDATE ETT_PRN_SHP_ACK_STG SET EODSTATUS = ?  WHERE GR_NUMBER = ? ";
       

        LoggableStatement pps1 = new LoggableStatement(connection, s1);
        logger.info(
          "----------EDPMS ------PRNDWLD--- [processPRNAmendJob]- ETT_PRN_SHP_INV_ACK_STG ---query--------" +
          preparedStatement1.getQueryString());
        pps1.setString(1, "C");
        pps1.setString(2, grNumber.trim());
       

        pps1.close();
      }
      Element nob1 = document.createElement("noOfInvoices");
      nob1.appendChild(document.createTextNode(noofinvoices));
      rocdoc.appendChild(nob1);
     
      Element nob = document.createElement("noOfShippingBills");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("method", "html");
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,PRNXML_SEQNO.NEXTVAL AS PRN_SEQNO FROM DLYPRCCYCL";
       

        PreparedStatement pps1 = connection.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("PRN_SEQNO");
        }
        logger.info("==================>>> prodate : " + prodate + " : seq : " + seq);
        rs1.close();
        pps1.close();
        logger.info("Milestone 06 EDPMS-PRN completed..!");
        String fileLoc = "";
       

        fileLoc = ActionConstants.PRN_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        logger.info("==================>>> fileLoc : " + fileLoc);
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".prn";
        String myFilePath = fileLoc + fileName + ".xml";
       
        logger.info("==================>>> myFilePath : Amend : " + myFilePath);
       
        StringWriter writer = new StringWriter();
        transformer.transform(domSource, new StreamResult(writer));
        String output = writer.toString();
       
        File file = new File(myFilePath);
       
        file.createNewFile();
       
        FileWriter writer1 = new FileWriter(file);
       
        writer1.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + output);
        writer1.flush();
        writer1.close();
       
        logger.info("File has been successfully saved Amend");
       
        updateAuditTrail("ETT_PRN_AMEND_AUDIT_TABLE", fileName, fileLoc, "PRN_SEQ_NO", prnSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      result = "error";
      logger.info(
        "----------EDPMS ------PRNDWLD--- [processPRNAmendJob]- ETT_PRN_SHP_INV_ACK_STG ------Exception-----" +
        e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, preparedStatement, resultSet);
      DBConnectionUtility.surrenderDB(connection1, preparedStatement1, resultSet1);
    }
    return result;
  }
 
  public String processWSN()
  {
    String result1 = "success";
    String result2 = "success";
    String status = "success";
    try
    {
      result1 = processWSNJobProcess1();
     

      result2 = processWSNJobProcess2();
      if ((result1.equals("success")) || (result2.equals("success"))) {
        status = "success";
      } else if ((result1.equals("error")) || (result2.equals("error"))) {
        status = "error";
      } else if ((result1.equals("nodatafound")) || (result2.equals("nodatafound"))) {
        status = "nodatafound";
      }
      logger.info("Milestone 04A EDPMS-WSN (-_-) " + status);
    }
    catch (Exception e)
    {
      status = "error";
      e.printStackTrace();
    }
    return status;
  }
 
  public String processWSNJobProcess1()
  {
    Connection con = null;
    String noofshipbills = "0";
    String noofinvoices = "0";
    int xmlCount = 0;
    String result = "success";
    int wsnSeq = 0;
    boolean refNof = false;
    boolean billNof = false;
    boolean billDatef = false;
    int filtercount = 0;
    try
    {
      logger.info("-----------------*********processWSNJobProcess1*******-----------------------");
     
      con = DBConnectionUtility.getConnection();
     
      wsnSeq = seqGeneration("WSN_SEQNO_GEN");
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("shippingBills");
      root.appendChild(rocdoc2);
     
      String query = "SELECT BILLREFNO,PAY_SERIAL_NO,SHIPPINGBILLNO,SHIPPINGBILLDATE as TEMP_SHIPPINGBILLDATE,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE,PORTCODE,FORMNO,TO_CHAR(TO_DATE(LEODATE, 'dd/mm/yy'),'dd/mm/yyyy') AS LEODATE,ADCODE,EXPORTTYPE,RECORDINDICATOR,PAYMENT_SEQNO,BOENO,TO_CHAR(TO_DATE(BOEDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS BOEDATE,PORTOFDISCHARGE,SHIPMENTIND FROM ETTV_WSN_FILES WHERE RECORDINDICATOR = '1' ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        query = query + " AND TRIM(BILLREFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getShipBillNo() != null) && (this.vo.getShipBillNo().trim().length() > 0))
      {
        query = query + " AND TRIM(SHIPPINGBILLNO) = ?";
        billNof = true;
      }
      if ((this.vo.getShipBillDate() != null) && (this.vo.getShipBillDate().trim().length() > 0))
      {
        query = query + " AND TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy') = TO_DATE(?, 'DD/MM/YYYY')";
        billDatef = true;
      }
      LoggableStatement ps = new LoggableStatement(con, query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (billNof) {
        ps.setString(++filtercount, this.vo.getShipBillNo().trim());
      }
      if (billDatef) {
        ps.setString(++filtercount, this.vo.getShipBillDate());
      }
      ResultSet results = ps.executeQuery();
      while (results.next())
      {
        String shipbillno = results.getString("SHIPPINGBILLNO");
        String shipbilldate = results.getString("SHIPPINGBILLDATE");
        String temp_shipbilldate = results.getString("TEMP_SHIPPINGBILLDATE");
        String portcode = results.getString("PORTCODE");
        String formno = results.getString("FORMNO");
        String leodate = results.getString("LEODATE");
        String adcode = results.getString("ADCODE");
        String exporttype = results.getString("EXPORTTYPE");
        String record = results.getString("RECORDINDICATOR");
        String billRefNo = results.getString("BILLREFNO");
        String payRefNo = results.getString("PAY_SERIAL_NO");
        String writeOffSeqNo = results.getString("PAYMENT_SEQNO");
        String shpInd = results.getString("SHIPMENTIND");
        String boeNo = results.getString("BOENO");
        String boeDate = results.getString("BOEDATE");
        String portDischarge = results.getString("PORTOFDISCHARGE");
       
        String sql = "SELECT WRITEOFFSTATUS FROM ETT_GR_REL_INV_TBL WHERE BILLREFNO = '" + billRefNo +
          "' AND PAY_SERIAL_NO ='" + payRefNo + "'" + " AND TRIM(SHIPBILLDATE) = TO_CHAR(TO_DATE('" +
          temp_shipbilldate + "', 'dd/mm/yy'),'ddmmyyyy')" + " AND TRIM(PORTCODE) ='" + portcode + "'";
        if (shipbillno != null) {
          sql = sql + "AND TRIM(SHIPBILLNO) = TRIM('" + shipbillno + "')";
        }
        if (formno != null) {
          sql = sql + "AND TRIM(FORMNO) = TRIM('" + formno + "')";
        }
        PreparedStatement p = con.prepareStatement(sql);
        ResultSet rst = p.executeQuery();
        String status = null;
        if (rst.next()) {
          status = rst.getString("WRITEOFFSTATUS");
        }
        if ((status != null) && (status.equalsIgnoreCase("N")))
        {
          Element rocdoc1 = document.createElement("shippingBill");
          rocdoc2.appendChild(rocdoc1);
          if (portcode == null)
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(pc);
          }
          else
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(portcode));
            rocdoc1.appendChild(pc);
          }
          if (exporttype == null)
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(et);
          }
          else
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(exporttype));
            rocdoc1.appendChild(et);
          }
          if (record == null)
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ri);
          }
          else
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(record));
            rocdoc1.appendChild(ri);
          }
          if (shipbillno == null)
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbn);
          }
          else
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(shipbillno));
            rocdoc1.appendChild(sbn);
          }
          if (shipbilldate == null)
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbd);
          }
          else
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(shipbilldate));
            rocdoc1.appendChild(sbd);
          }
          if (formno == null)
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(fn);
          }
          else
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(formno));
            rocdoc1.appendChild(fn);
          }
          if (leodate == null)
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ld);
          }
          else
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(leodate));
            rocdoc1.appendChild(ld);
          }
          if (adcode == null)
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(adcode));
            rocdoc1.appendChild(ac);
          }
          if (writeOffSeqNo == null)
          {
            Element wr = document.createElement("writeoffSequence");
            wr.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(wr);
          }
          else
          {
            Element wr = document.createElement("writeoffSequence");
            wr.appendChild(document.createTextNode(writeOffSeqNo));
            rocdoc1.appendChild(wr);
          }
          if (boeNo == null)
          {
            Element bo = document.createElement("billOfEntryNumber");
            bo.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(bo);
          }
          else
          {
            Element bo = document.createElement("billOfEntryNumber");
            bo.appendChild(document.createTextNode(boeNo));
            rocdoc1.appendChild(bo);
          }
          if (boeDate == null)
          {
            Element bd = document.createElement("billOfEntryDate");
            bd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(bd);
          }
          else
          {
            Element bd = document.createElement("billOfEntryDate");
            bd.appendChild(document.createTextNode(boeDate));
            rocdoc1.appendChild(bd);
          }
          if (portDischarge == null)
          {
            Element pd = document.createElement("portOfDischarge");
            pd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(pd);
          }
          else
          {
            Element pd = document.createElement("portOfDischarge");
            pd.appendChild(document.createTextNode(portDischarge));
            rocdoc1.appendChild(pd);
          }
          if (shpInd == null)
          {
            Element sbn1 = document.createElement("shipmentIndicator");
            sbn1.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbn1);
          }
          else
          {
            Element sbn1 = document.createElement("shipmentIndicator");
            sbn1.appendChild(document.createTextNode(shpInd));
            rocdoc1.appendChild(sbn1);
          }
          int nobill = Integer.parseInt(noofshipbills);
          nobill++;
          noofshipbills = Integer.toString(nobill);
         
          Element rocdoc3 = document.createElement("invoices");
          rocdoc1.appendChild(rocdoc3);
         
          String inv_query = "SELECT INVSERIALNO,INVNO,TO_CHAR(TO_DATE(INVDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS INVDATE,  WRITEOFIND,WRITEOFINDAMOUNT,  TO_CHAR(TO_DATE(WRITEOFDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS WRITEOFDATE,CLOSEOFBILLIND FROM ETTV_WSN_FILES   WHERE BILLREFNO = '" +
         

            billRefNo + "'" + "  AND PAY_SERIAL_NO = '" + payRefNo + "'" +
            "  AND SHIPPINGBILLDATE = '" + temp_shipbilldate + "'" + "  AND PORTCODE = '" + portcode +
            "'";
          if (shipbillno != null) {
            inv_query = inv_query + " AND SHIPPINGBILLNO = '" + shipbillno + "'";
          }
          if (formno != null) {
            inv_query = inv_query + " AND FORMNO = '" + formno + "'";
          }
          PreparedStatement ps1 = con.prepareStatement(inv_query);
          ResultSet results1 = ps1.executeQuery();
          while (results1.next())
          {
            String invserialno = results1.getString("INVSERIALNO");
            String invno = results1.getString("INVNO");
            String invdate = results1.getString("INVDATE");
            String writeind = results1.getString("WRITEOFIND");
            String writeamt = results1.getString("WRITEOFINDAMOUNT");
            String writedate = results1.getString("WRITEOFDATE");
            String closebill = results1.getString("CLOSEOFBILLIND");
           
            Element rocdoc4 = document.createElement("invoice");
            rocdoc3.appendChild(rocdoc4);
            if (invserialno == null)
            {
              Element pc1 = document.createElement("invoiceSerialNo");
              pc1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(pc1);
            }
            else
            {
              Element pc1 = document.createElement("invoiceSerialNo");
              pc1.appendChild(document.createTextNode(invserialno));
              rocdoc4.appendChild(pc1);
            }
            if (invno == null)
            {
              Element et1 = document.createElement("invoiceNo");
              et1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(et1);
            }
            else
            {
              Element et1 = document.createElement("invoiceNo");
              et1.appendChild(document.createTextNode(invno));
              rocdoc4.appendChild(et1);
            }
            if (invdate == null)
            {
              Element ri1 = document.createElement("invoiceDate");
              ri1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ri1);
            }
            else
            {
              Element ri1 = document.createElement("invoiceDate");
              ri1.appendChild(document.createTextNode(invdate));
              rocdoc4.appendChild(ri1);
            }
            if (writeind == null)
            {
              Element sbd1 = document.createElement("writeoffIndicator");
              sbd1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(sbd1);
            }
            else
            {
              Element sbd1 = document.createElement("writeoffIndicator");
              sbd1.appendChild(document.createTextNode(writeind));
              rocdoc4.appendChild(sbd1);
            }
            if (writeamt == null)
            {
              Element fn1 = document.createElement("writeoffAmount");
              fn1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(fn1);
            }
            else
            {
              Element fn1 = document.createElement("writeoffAmount");
              fn1.appendChild(document.createTextNode(writeamt));
              rocdoc4.appendChild(fn1);
            }
            if (writedate == null)
            {
              Element ld1 = document.createElement("writeoffDate");
              ld1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("writeoffDate");
              ld1.appendChild(document.createTextNode(writedate));
              rocdoc4.appendChild(ld1);
            }
            if (closebill == null)
            {
              Element ac1 = document.createElement("closeOfBillIndicator");
              ac1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ac1);
            }
            else
            {
              Element ac1 = document.createElement("closeOfBillIndicator");
              ac1.appendChild(document.createTextNode(closebill));
              rocdoc4.appendChild(ac1);
            }
            int noinv = Integer.parseInt(noofinvoices);
            noinv++;
            noofinvoices = Integer.toString(noinv);
           
            String s1 = "UPDATE ETT_GR_SHP_TBL SET WSNSENT=? WHERE TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) = ? ";
            if (shipbillno != null) {
              s1 = s1 + " AND SHIPBILLNO = '" + shipbillno + "'";
            }
            if (formno != null) {
              s1 = s1 + " AND FORMNO = '" + formno + "'";
            }
            PreparedStatement pps1 = con.prepareStatement(s1);
            pps1.setString(1, "Y");
            pps1.setString(2, temp_shipbilldate);
            pps1.setString(3, portcode);
           
            pps1.close();
           
            String sq1 = "UPDATE ETT_GR_REL_INV_TBL SET WRITEOFFSTATUS = ?  WHERE BILLREFNO = ? AND PAY_SERIAL_NO = ?  AND TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) = ? ";
            if (shipbillno != null) {
              sq1 = sq1 + " AND SHIPBILLNO = '" + shipbillno + "'";
            }
            if (formno != null) {
              sq1 = sq1 + " AND FORMNO = '" + formno + "'";
            }
            PreparedStatement pps2 = con.prepareStatement(sq1);
            pps2.setString(1, "Y");
            pps2.setString(2, billRefNo);
            pps2.setString(3, payRefNo);
            pps2.setString(4, temp_shipbilldate);
            pps2.setString(5, portcode);
           
            pps2.close();
           
            insertWSN1Audit(shipbillno, shipbilldate, portcode, formno, leodate, adcode, exporttype, record,
              billRefNo, payRefNo, writeOffSeqNo, shpInd, boeNo, boeDate, portDischarge, invserialno,
              invno, invdate, writeind, writeamt, writedate, closebill, userName, wsnSeq);
          }
          results1.close();
          ps1.close();
          xmlCount++;
        }
        else
        {
          logger.info("Nothing to print");
        }
      }
      results.close();
      ps.close();
     
      Element nob1 = document.createElement("noOfInvoices");
      nob1.appendChild(document.createTextNode(noofinvoices));
      rocdoc.appendChild(nob1);
      logger.info("Total number of Invoices for WSN1: " + noofinvoices);
     
      Element nob = document.createElement("noOfShippingBills");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      logger.info("Total number of Shipping Bills for WSN1: " + noofshipbills);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("method", "html");
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,WSNXML_SEQNO.NEXTVAL AS WSN_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("WSN_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.WSN_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".wsn";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StringWriter writer = new StringWriter();
        transformer.transform(domSource, new StreamResult(writer));
        String output = writer.toString();
       
        File file = new File(myFilePath);
       
        file.createNewFile();
       
        FileWriter writer1 = new FileWriter(file);
       
        writer1.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + output);
        writer1.flush();
        writer1.close();
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_WSN_AUDIT_TABLE", fileName, fileLoc, "WSN_SEQ_NO", wsnSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
      logger.info("-----------------------------> Result is True (WSN1)");
    }
    catch (Exception e)
    {
      result = "error";
      logger.info("-----------------------------> Result is False (WSN1)");
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, null, null);
      logger.info("Finally Block Occurred in saveDetail (WSN1)");
    }
    return result;
  }
 
  public String processWSNJobProcess2()
  {
    Connection con = null;
    String noofshipbills = "0";
    String noofinvoices = "0";
    int xmlCount = 0;
    String result = "success";
    int wsnSeq = 0;
    boolean refNof = false;
    boolean billNof = false;
    boolean billDatef = false;
    int filtercount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
     
      wsnSeq = seqGeneration("WSN_SEQNO_GEN");
     
      logger.info("-----------------*********processWSNJobProcess2*******-----------------------");
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("shippingBills");
      root.appendChild(rocdoc2);
     
      String query = "SELECT BILLREFNO,PAY_SERIAL_NO,SHIPPINGBILLNO,SHIPPINGBILLDATE as TEMP_SHIPPINGBILLDATE,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE,PORTCODE,FORMNO,TO_CHAR(TO_DATE(LEODATE, 'dd/mm/yy'),'dd/mm/yyyy') AS LEODATE,ADCODE,EXPORTTYPE,RECORDINDICATOR,PAYMENT_SEQNO,BOENO,TO_CHAR(TO_DATE(BOEDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS BOEDATE,PORTOFDISCHARGE,SHIPMENTIND FROM ETTV_WSN_FILES WHERE RECORDINDICATOR = '2' ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        query = query + " AND TRIM(BILLREFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getShipBillNo() != null) && (this.vo.getShipBillNo().trim().length() > 0))
      {
        query = query + " AND TRIM(SHIPPINGBILLNO) = ?";
        billNof = true;
      }
      if ((this.vo.getShipBillDate() != null) && (this.vo.getShipBillDate().trim().length() > 0))
      {
        query = query + " AND TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy') = TO_DATE(?, 'DD/MM/YYYY')";
        billDatef = true;
      }
      LoggableStatement ps = new LoggableStatement(con, query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (billNof) {
        ps.setString(++filtercount, this.vo.getShipBillNo().trim());
      }
      if (billDatef) {
        ps.setString(++filtercount, this.vo.getShipBillDate());
      }
      ResultSet results = ps.executeQuery();
      while (results.next())
      {
        String shipbillno = results.getString("SHIPPINGBILLNO");
        String shipbilldate = results.getString("SHIPPINGBILLDATE");
        String temp_shipbilldate = results.getString("TEMP_SHIPPINGBILLDATE");
        String portcode = results.getString("PORTCODE");
        String formno = results.getString("FORMNO");
        String leodate = results.getString("LEODATE");
        String adcode = results.getString("ADCODE");
        String exporttype = results.getString("EXPORTTYPE");
        String record = results.getString("RECORDINDICATOR");
        String billRefNo = results.getString("BILLREFNO");
        String payRefNo = results.getString("PAY_SERIAL_NO");
        String writeOffSeqNo = results.getString("PAYMENT_SEQNO");
        String shpInd = results.getString("SHIPMENTIND");
        String boeNo = results.getString("BOENO");
        String boeDate = results.getString("BOEDATE");
        String portDischarge = results.getString("PORTOFDISCHARGE");
       
        String sql = "SELECT WRITEOFFSTATUS FROM ETT_GR_REL_INV_TBL WHERE BILLREFNO = '" + billRefNo +
          "' AND PAY_SERIAL_NO ='" + payRefNo + "'" + " AND TRIM(SHIPBILLDATE) = TO_CHAR(TO_DATE('" +
          temp_shipbilldate + "', 'dd/mm/yy'),'ddmmyyyy')" + " AND TRIM(PORTCODE) ='" + portcode + "'";
        if (shipbillno != null) {
          sql = sql + "AND TRIM(SHIPBILLNO) = TRIM('" + shipbillno + "')";
        }
        if (formno != null) {
          sql = sql + "AND TRIM(FORMNO) = TRIM('" + formno + "')";
        }
        PreparedStatement p = con.prepareStatement(sql);
        ResultSet rst = p.executeQuery();
        String status = null;
        if (rst.next()) {
          status = rst.getString("WRITEOFFSTATUS");
        }
        if ((status != null) && (status.equalsIgnoreCase("N")))
        {
          Element rocdoc1 = document.createElement("shippingBill");
          rocdoc2.appendChild(rocdoc1);
          if (portcode == null)
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(pc);
          }
          else
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(portcode));
            rocdoc1.appendChild(pc);
          }
          if (exporttype == null)
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(et);
          }
          else
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(exporttype));
            rocdoc1.appendChild(et);
          }
          if (record == null)
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ri);
          }
          else
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(record));
            rocdoc1.appendChild(ri);
          }
          if (shipbillno == null)
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbn);
          }
          else
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(shipbillno));
            rocdoc1.appendChild(sbn);
          }
          if (shipbilldate == null)
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbd);
          }
          else
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(shipbilldate));
            rocdoc1.appendChild(sbd);
          }
          if (formno == null)
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(fn);
          }
          else
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(formno));
            rocdoc1.appendChild(fn);
          }
          if (leodate == null)
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ld);
          }
          else
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(leodate));
            rocdoc1.appendChild(ld);
          }
          if (adcode == null)
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(adcode));
            rocdoc1.appendChild(ac);
          }
          if (writeOffSeqNo == null)
          {
            Element wr = document.createElement("writeoffSequence");
            wr.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(wr);
          }
          else
          {
            Element wr = document.createElement("writeoffSequence");
            wr.appendChild(document.createTextNode(writeOffSeqNo));
            rocdoc1.appendChild(wr);
          }
          if (boeNo == null)
          {
            Element bo = document.createElement("billOfEntryNumber");
            bo.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(bo);
          }
          else
          {
            Element bo = document.createElement("billOfEntryNumber");
            bo.appendChild(document.createTextNode(boeNo));
            rocdoc1.appendChild(bo);
          }
          if (boeDate == null)
          {
            Element bd = document.createElement("billOfEntryDate");
            bd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(bd);
          }
          else
          {
            Element bd = document.createElement("billOfEntryDate");
            bd.appendChild(document.createTextNode(boeDate));
            rocdoc1.appendChild(bd);
          }
          if (portDischarge == null)
          {
            Element pd = document.createElement("portOfDischarge");
            pd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(pd);
          }
          else
          {
            Element pd = document.createElement("portOfDischarge");
            pd.appendChild(document.createTextNode(portDischarge));
            rocdoc1.appendChild(pd);
          }
          if (shpInd == null)
          {
            Element sbn1 = document.createElement("shipmentIndicator");
            sbn1.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbn1);
          }
          else
          {
            Element sbn1 = document.createElement("shipmentIndicator");
            sbn1.appendChild(document.createTextNode(shpInd));
            rocdoc1.appendChild(sbn1);
          }
          int nobill = Integer.parseInt(noofshipbills);
          nobill++;
          noofshipbills = Integer.toString(nobill);
         
          Element rocdoc3 = document.createElement("invoices");
          rocdoc1.appendChild(rocdoc3);
         
          String inv_query = "SELECT INVSERIALNO,INVNO,TO_CHAR(TO_DATE(INVDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS INVDATE,  WRITEOFIND,WRITEOFINDAMOUNT,  TO_CHAR(TO_DATE(WRITEOFDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS WRITEOFDATE,CLOSEOFBILLIND FROM ETTV_WSN_FILES   WHERE BILLREFNO = '" +
         

            billRefNo + "'" + "  AND PAY_SERIAL_NO = '" + payRefNo + "'" +
            "  AND SHIPPINGBILLDATE = '" + temp_shipbilldate + "'" + "  AND PORTCODE = '" + portcode +
            "'";
          if (shipbillno != null) {
            inv_query = inv_query + " AND SHIPPINGBILLNO = '" + shipbillno + "'";
          }
          if (formno != null) {
            inv_query = inv_query + " AND FORMNO = '" + formno + "'";
          }
          PreparedStatement ps1 = con.prepareStatement(inv_query);
          ResultSet results1 = ps1.executeQuery();
          while (results1.next())
          {
            String invserialno = results1.getString("INVSERIALNO");
            String invno = results1.getString("INVNO");
            String invdate = results1.getString("INVDATE");
            String writeind = results1.getString("WRITEOFIND");
            String writeamt = results1.getString("WRITEOFINDAMOUNT");
            String writedate = results1.getString("WRITEOFDATE");
            String closebill = results1.getString("CLOSEOFBILLIND");
           
            Element rocdoc4 = document.createElement("invoice");
            rocdoc3.appendChild(rocdoc4);
            if (invserialno == null)
            {
              Element pc1 = document.createElement("invoiceSerialNo");
              pc1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(pc1);
            }
            else
            {
              Element pc1 = document.createElement("invoiceSerialNo");
              pc1.appendChild(document.createTextNode(invserialno));
              rocdoc4.appendChild(pc1);
            }
            if (invno == null)
            {
              Element et1 = document.createElement("invoiceNo");
              et1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(et1);
            }
            else
            {
              Element et1 = document.createElement("invoiceNo");
              et1.appendChild(document.createTextNode(invno));
              rocdoc4.appendChild(et1);
            }
            if (invdate == null)
            {
              Element ri1 = document.createElement("invoiceDate");
              ri1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ri1);
            }
            else
            {
              Element ri1 = document.createElement("invoiceDate");
              ri1.appendChild(document.createTextNode(invdate));
              rocdoc4.appendChild(ri1);
            }
            if (writeind == null)
            {
              Element sbd1 = document.createElement("writeoffIndicator");
              sbd1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(sbd1);
            }
            else
            {
              Element sbd1 = document.createElement("writeoffIndicator");
              sbd1.appendChild(document.createTextNode(writeind));
              rocdoc4.appendChild(sbd1);
            }
            if (writeamt == null)
            {
              Element fn1 = document.createElement("writeoffAmount");
              fn1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(fn1);
            }
            else
            {
              Element fn1 = document.createElement("writeoffAmount");
              fn1.appendChild(document.createTextNode(writeamt));
              rocdoc4.appendChild(fn1);
            }
            if (writedate == null)
            {
              Element ld1 = document.createElement("writeoffDate");
              ld1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ld1);
            }
            else
            {
              Element ld1 = document.createElement("writeoffDate");
              ld1.appendChild(document.createTextNode(writedate));
              rocdoc4.appendChild(ld1);
            }
            if (closebill == null)
            {
              Element ac1 = document.createElement("closeOfBillIndicator");
              ac1.appendChild(document.createTextNode(""));
              rocdoc4.appendChild(ac1);
            }
            else
            {
              Element ac1 = document.createElement("closeOfBillIndicator");
              ac1.appendChild(document.createTextNode(closebill));
              rocdoc4.appendChild(ac1);
            }
            int noinv = Integer.parseInt(noofinvoices);
            noinv++;
            noofinvoices = Integer.toString(noinv);
           
            String s1 = "UPDATE ETT_GR_SHP_TBL SET WSNSENT=? WHERE TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) = ? ";
            if (shipbillno != null) {
              s1 = s1 + " AND SHIPBILLNO = '" + shipbillno + "'";
            }
            if (formno != null) {
              s1 = s1 + " AND FORMNO = '" + formno + "'";
            }
            PreparedStatement pps1 = con.prepareStatement(s1);
            pps1.setString(1, "Y");
            pps1.setString(2, temp_shipbilldate);
            pps1.setString(3, portcode);
           
            pps1.close();
           
            String sq1 = "UPDATE ETT_GR_REL_INV_TBL SET WRITEOFFSTATUS = ?  WHERE BILLREFNO = ? AND PAY_SERIAL_NO = ?  AND TRIM(SHIPBILLDATE) = ? AND TRIM(PORTCODE) = ? ";
            if (shipbillno != null) {
              sq1 = sq1 + " AND SHIPBILLNO = '" + shipbillno + "'";
            }
            if (formno != null) {
              sq1 = sq1 + " AND FORMNO = '" + formno + "'";
            }
            PreparedStatement pps2 = con.prepareStatement(sq1);
            pps2.setString(1, "Y");
            pps2.setString(2, billRefNo);
            pps2.setString(3, payRefNo);
            pps2.setString(4, temp_shipbilldate);
            pps2.setString(5, portcode);
           
            pps2.close();
           
            insertWSN2Audit(shipbillno, shipbilldate, portcode, formno, leodate, adcode, exporttype, record,
              billRefNo, payRefNo, writeOffSeqNo, shpInd, boeNo, boeDate, portDischarge, invserialno,
              invno, invdate, writeind, writeamt, writedate, closebill, userName, wsnSeq);
          }
          results1.close();
          ps1.close();
          xmlCount++;
        }
        else
        {
          logger.info("Nothing to print------>No record has the right WRITEOFF status");
        }
      }
      results.close();
      ps.close();
     
      Element nob1 = document.createElement("noOfInvoices");
      nob1.appendChild(document.createTextNode(noofinvoices));
      rocdoc.appendChild(nob1);
      logger.info("Total number of Invoices for WSN2: " + noofinvoices);
     
      Element nob = document.createElement("noOfShippingBills");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      logger.info("Total number of Shipping Bills for WSN2: " + noofshipbills);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("method", "html");
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,WSNXML_SEQNO.NEXTVAL AS WSN_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("WSN_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.WSN_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".wsn";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StringWriter writer = new StringWriter();
        transformer.transform(domSource, new StreamResult(writer));
        String output = writer.toString();
       
        File file = new File(myFilePath);
       
        file.createNewFile();
       
        FileWriter writer1 = new FileWriter(file);
       
        writer1.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + output);
        writer1.flush();
        writer1.close();
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_WSN_AUDIT_TABLE", fileName, fileLoc, "WSN_SEQ_NO", wsnSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
      logger.info("-----------------------------> Result is True (WSN2)");
    }
    catch (Exception e)
    {
      result = "error";
      logger.info("-----------------------------> Result is False (WSN2)");
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, null, null);
      logger.info("Finally Block Occurred in saveDetail (WSN2)");
    }
    return result;
  }
 
  public String processPENjob()
  {
    logger.info("----------EDPMS ------PENDWLD--- [processPENjob]----");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    String noofshipbills = "0";
    int xmlCount = 0;
    String result = "success";
    int penSeq = 0;
    boolean refNof = false;
    boolean billNof = false;
    boolean billDatef = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
      if (con == null) {
        logger.info("----------EDPMS ------PENDWLD--- [processPENjob]--Connection Null--");
      }
      penSeq = seqGeneration("PEN_SEQNO_GEN");
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("shippingBills");
      root.appendChild(rocdoc2);
     
      String query = "SELECT SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE,SHIPPINGBILLDATE AS TEMP_SHIPPINGBILLDATE, PORTCODE,FORMNO,TO_CHAR(TO_DATE(LEODATE, 'dd/mm/yy'),'dd/mm/yyyy')AS LEODATE,IECODE,ADCODE,LETTERNO,TO_CHAR(TO_DATE(LETTERDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS LETTERDATE,EXPORTTYPE,RECORDINDICATOR,REALIZATIONEXTENSIONIND ,TO_CHAR(TO_DATE(EXTENDEDREALIZATIONDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS EXTENDEDREALIZATIONDATE FROM ETTV_PEN_FILES WHERE 1=1 ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        query = query + " AND TRIM(MASTER_REF) = ?";
        refNof = true;
      }
      if ((this.vo.getShipBillNo() != null) && (this.vo.getShipBillNo().trim().length() > 0))
      {
        query = query + " AND TRIM(SHIPPINGBILLNO) = ?";
        billNof = true;
      }
      if ((this.vo.getShipBillDate() != null) && (this.vo.getShipBillDate().trim().length() > 0))
      {
        query = query + " AND TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy') = TO_DATE(?, 'DD/MM/YYYY')";
        billDatef = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        query = query + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = new LoggableStatement(con, query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (billNof) {
        ps.setString(++filtercount, this.vo.getShipBillNo().trim());
      }
      if (billDatef) {
        ps.setString(++filtercount, this.vo.getShipBillDate());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      logger.info("----------EDPMS ------PENDWLD--- [processPENjob]--ETTV_PEN_FILES     Query--------------" +
        ps.getQueryString());
      res = ps.executeQuery();
      while (res.next())
      {
        String shipbillno = res.getString("SHIPPINGBILLNO");
        String shipbilldate = res.getString("SHIPPINGBILLDATE");
        String temp_shipbilldate = res.getString("TEMP_SHIPPINGBILLDATE");
        String portcode = res.getString("portcode");
        String formno = res.getString("FORMNO");
        String leodate = res.getString("LEODATE");
        String iecode = res.getString("IECODE");
        String adcode = res.getString("ADCODE");
        String letterno = res.getString("LETTERNO");
        String letterdate = res.getString("LETTERDATE");
        String exporttype = res.getString("EXPORTTYPE");
        String record = res.getString("RECORDINDICATOR");
        String ind = res.getString("REALIZATIONEXTENSIONIND");
        String inddate = res.getString("EXTENDEDREALIZATIONDATE");
       
        String sql = "SELECT PENSENT FROM ETT_GR_SHP_TBL WHERE  TRIM(SHIPBILLDATE) = '" + temp_shipbilldate +
          "' AND TRIM(PORTCODE) ='" + portcode + "'";
        if ((shipbillno != null) && (shipbillno.trim().length() > 0)) {
          sql = sql + " AND TRIM(SHIPBILLNO) = '" + shipbillno + "'";
        }
        if ((formno != null) && (formno.trim().length() > 0)) {
          sql = sql + " AND TRIM(FORMNO) = '" + formno + "'";
        }
        LoggableStatement pss = new LoggableStatement(con, sql);
        ResultSet rst = pss.executeQuery();
        logger.info(
          "----------EDPMS ------PENDWLD--- [processPENjob]--ETT_GR_SHP_TBL     Query--------------" +
          pss.getQueryString());
        String status = null;
        if (rst.next())
        {
          status = rst.getString("PENSENT");
         
          logger.info(
            "----------EDPMS ------PENDWLD--- [processPENjob]--ETT_GR_SHP_TBL     status--------------" +
            status);
        }
        rst.close();
        pss.close();
        if ((status == null) || (status.equalsIgnoreCase("")))
        {
          Element rocdoc1 = document.createElement("shippingBill");
          rocdoc2.appendChild(rocdoc1);
          if (portcode == null)
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(pc);
          }
          else
          {
            Element pc = document.createElement("portCode");
            pc.appendChild(document.createTextNode(portcode));
            rocdoc1.appendChild(pc);
          }
          if (exporttype == null)
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(et);
          }
          else
          {
            Element et = document.createElement("exportType");
            et.appendChild(document.createTextNode(exporttype));
            rocdoc1.appendChild(et);
          }
          if (record == null)
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ri);
          }
          else
          {
            Element ri = document.createElement("recordIndicator");
            ri.appendChild(document.createTextNode(record));
            rocdoc1.appendChild(ri);
          }
          if (shipbillno == null)
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbn);
          }
          else
          {
            Element sbn = document.createElement("shippingBillNo");
            sbn.appendChild(document.createTextNode(shipbillno));
            rocdoc1.appendChild(sbn);
          }
          if (shipbilldate == null)
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(sbd);
          }
          else
          {
            Element sbd = document.createElement("shippingBillDate");
            sbd.appendChild(document.createTextNode(shipbilldate));
            rocdoc1.appendChild(sbd);
          }
          if (formno == null)
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(fn);
          }
          else
          {
            Element fn = document.createElement("formNo");
            fn.appendChild(document.createTextNode(formno));
            rocdoc1.appendChild(fn);
          }
          if (leodate == null)
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ld);
          }
          else
          {
            Element ld = document.createElement("LEODate");
            ld.appendChild(document.createTextNode(leodate));
            rocdoc1.appendChild(ld);
          }
          if (adcode == null)
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("adCode");
            ac.appendChild(document.createTextNode(adcode));
            rocdoc1.appendChild(ac);
          }
          if (iecode == null)
          {
            Element ic = document.createElement("IECode");
            ic.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(ic);
          }
          else
          {
            Element ic = document.createElement("IECode");
            ic.appendChild(document.createTextNode(iecode));
            rocdoc1.appendChild(ic);
          }
          if (ind == null)
          {
            Element aea = document.createElement("realizationExtensionInd");
            aea.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(aea);
          }
          else
          {
            Element aea = document.createElement("realizationExtensionInd");
            aea.appendChild(document.createTextNode(ind));
            rocdoc1.appendChild(aea);
          }
          if (letterno == null)
          {
            Element dd = document.createElement("letterNo");
            dd.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(dd);
          }
          else
          {
            Element dd = document.createElement("letterNo");
            dd.appendChild(document.createTextNode(letterno));
            rocdoc1.appendChild(dd);
          }
          if (letterdate == null)
          {
            Element abn = document.createElement("letterDate");
            abn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(abn);
          }
          else
          {
            Element abn = document.createElement("letterDate");
            abn.appendChild(document.createTextNode(letterdate));
            rocdoc1.appendChild(abn);
          }
          if (inddate == null)
          {
            Element dn = document.createElement("extendedRealizationDate");
            dn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(dn);
          }
          else
          {
            Element dn = document.createElement("extendedRealizationDate");
            dn.appendChild(document.createTextNode(inddate));
            rocdoc1.appendChild(dn);
          }
          int nobill = Integer.parseInt(noofshipbills);
          nobill++;
          noofshipbills = Integer.toString(nobill);
         
          String s1 = "UPDATE ETT_GR_SHP_TBL SET PENSENT=? WHERE TRIM(SHIPBILLDATE) =? AND TRIM(PORTCODE) =? ";
          if ((shipbillno != null) && (shipbillno.trim().length() > 0)) {
            s1 = s1 + " AND TRIM(SHIPBILLNO) = '" + shipbillno + "'";
          }
          if ((formno != null) && (formno.trim().length() > 0)) {
            s1 = s1 + " AND TRIM(FORMNO) = '" + formno + "'";
          }
          LoggableStatement pps1 = new LoggableStatement(con, s1);
          pps1.setString(1, "Y");
          pps1.setString(2, temp_shipbilldate);
          pps1.setString(3, portcode);
          logger.info(
            "----------EDPMS ------PENDWLD--- [processPENjob]-------------UPDATE--------ETT_GR_SHP_TBL status--------------" +
            pps1.getQueryString());
         

          pps1.close();
         
          insertPENAudit(shipbillno, shipbilldate, portcode, formno, leodate, iecode, adcode, letterno,
            letterdate, exporttype, record, ind, inddate, userName, penSeq);
          xmlCount++;
        }
        else
        {
          logger.info("Nothing to print");
        }
      }
      Element nob = document.createElement("noOfShippingBills");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,PENXML_SEQNO.NEXTVAL AS PEN_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("PEN_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.PEN_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".pen";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_PEN_AUDIT_TABLE", fileName, fileLoc, "PEN_SEQ_NO", penSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------PENDWLD--- [processPENjob]----Exception----------" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
  }
 
  public String processTRRjob()
  {
    logger.info("----------EDPMS ------TRRDWLD--- processTRRjob-- result-");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    String noofshipbills = "0";
    int xmlCount = 0;
    CommonMethods commonMethods = null;
    String result = "success";
    int trrSeq = 0;
    boolean billNof = false;
    boolean billDatef = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
      if (con == null) {
        logger.info("----------EDPMS ------TRRDWLD--- Connection Issues------------");
      }
      trrSeq = seqGeneration("TRR_SEQNO_GEN");
     
      commonMethods = new CommonMethods();
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("shippingBills");
      root.appendChild(rocdoc2);
     
      String querys = "SELECT SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE,SHIPPINGBILLDATE AS TEMP_SHIPPINGBILLDATE,PORTCODE,IECODE,FORMNO,EXPORTTYPE,EXPORTAGENCY,NEWADCODE,EXISTINGADCODE,TRANSFERSTATUS FROM ETTV_TRR_FILES WHERE 1=1 ";
      if ((this.vo.getShipBillNo() != null) && (this.vo.getShipBillNo().trim().length() > 0))
      {
        querys = querys + " AND TRIM(SHIPPINGBILLNO) = ?";
        billNof = true;
      }
      if ((this.vo.getShipBillDate() != null) && (this.vo.getShipBillDate().trim().length() > 0))
      {
        querys = querys + " AND TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy') = TO_DATE(?, 'DD/MM/YYYY')";
        billDatef = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        querys = querys + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = new LoggableStatement(con, querys);
      if (billNof) {
        ps.setString(++filtercount, this.vo.getShipBillNo().trim());
      }
      if (billDatef) {
        ps.setString(++filtercount, this.vo.getShipBillDate());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      res = ps.executeQuery();
     
      logger.info("----------EDPMS ------TRRDWLD--- processTRRjob-- result-------ps" + ps.getQueryString());
      while (res.next())
      {
        String shipbillno = commonMethods.getEmptyIfNull(res.getString("SHIPPINGBILLNO")).trim();
        String shipbilldate = commonMethods.getEmptyIfNull(res.getString("SHIPPINGBILLDATE")).trim();
        String temp_shipbilldate = commonMethods.getEmptyIfNull(res.getString("TEMP_SHIPPINGBILLDATE")).trim();
        String portcode = commonMethods.getEmptyIfNull(res.getString("PORTCODE")).trim();
        String iecode = commonMethods.getEmptyIfNull(res.getString("IECODE")).trim();
        String formno = commonMethods.getEmptyIfNull(res.getString("FORMNO")).trim();
        String exporttype = commonMethods.getEmptyIfNull(res.getString("EXPORTTYPE")).trim();
        String exportagency = commonMethods.getEmptyIfNull(res.getString("EXPORTAGENCY")).trim();
        String newadcode = commonMethods.getEmptyIfNull(res.getString("NEWADCODE")).trim();
        String exadcode = commonMethods.getEmptyIfNull(res.getString("EXISTINGADCODE")).trim();
       
        Element rocdoc1 = document.createElement("shippingBill");
        rocdoc2.appendChild(rocdoc1);
        if (portcode == null)
        {
          Element pc = document.createElement("portCode");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("portCode");
          pc.appendChild(document.createTextNode(portcode));
          rocdoc1.appendChild(pc);
        }
        if (exportagency == null)
        {
          Element ri = document.createElement("exportAgency");
          ri.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ri);
        }
        else
        {
          Element ri = document.createElement("exportAgency");
          ri.appendChild(document.createTextNode(exportagency));
          rocdoc1.appendChild(ri);
        }
        if (exporttype == null)
        {
          Element et = document.createElement("exportType");
          et.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(et);
        }
        else
        {
          Element et = document.createElement("exportType");
          et.appendChild(document.createTextNode(exporttype));
          rocdoc1.appendChild(et);
        }
        if (shipbillno == null)
        {
          Element sbn = document.createElement("shippingBillNo");
          sbn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("shippingBillNo");
          sbn.appendChild(document.createTextNode(shipbillno));
          rocdoc1.appendChild(sbn);
        }
        if (shipbilldate == null)
        {
          Element sbd = document.createElement("shippingBillDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("shippingBillDate");
          sbd.appendChild(document.createTextNode(shipbilldate));
          rocdoc1.appendChild(sbd);
        }
        if (formno == null)
        {
          Element fn = document.createElement("formNo");
          fn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(fn);
        }
        else
        {
          Element fn = document.createElement("formNo");
          fn.appendChild(document.createTextNode(formno));
          rocdoc1.appendChild(fn);
        }
        if (iecode == null)
        {
          Element ic = document.createElement("IECode");
          ic.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(ic);
        }
        else
        {
          Element ic = document.createElement("IECode");
          ic.appendChild(document.createTextNode(iecode));
          rocdoc1.appendChild(ic);
        }
        if (exadcode == null)
        {
          Element aea = document.createElement("existingAdCode");
          aea.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(aea);
        }
        else
        {
          Element aea = document.createElement("existingAdCode");
          aea.appendChild(document.createTextNode(exadcode));
          rocdoc1.appendChild(aea);
        }
        if (newadcode == null)
        {
          Element dd = document.createElement("newAdCode");
          dd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dd);
        }
        else
        {
          Element dd = document.createElement("newAdCode");
          dd.appendChild(document.createTextNode(newadcode));
          rocdoc1.appendChild(dd);
        }
        int nobill = Integer.parseInt(noofshipbills);
        nobill++;
        noofshipbills = Integer.toString(nobill);
        if (((exporttype != null) && (exporttype.equalsIgnoreCase("1"))) || (
          (exporttype != null) && (exporttype.equalsIgnoreCase("3"))))
        {
          String sq = "UPDATE ETT_TRR_SHP_STG SET TRANSFERSTATUS=? WHERE TRIM(SHIPPINGBILLNO) =? AND TRIM(SHIPPINGBILLDATE) = ? AND TRIM(PORTCODE) = ? ";
         

          LoggableStatement pps = new LoggableStatement(con, sq);
          pps.setString(1, "Completed");
          pps.setString(2, shipbillno);
          pps.setString(3, temp_shipbilldate);
          pps.setString(4, portcode);
         
          logger.info(
            "----------EDPMS ------TRRDWLD--- processTRRjob-UPDATE -ETT_TRR_SHP_STG query-------ps" +
            pps.getQueryString());
         


          pps.close();
        }
        else if ((exporttype != null) && (exporttype.equalsIgnoreCase("2")))
        {
          String sq = "UPDATE ETT_TRR_SHP_STG SET TRANSFERSTATUS=? WHERE TRIM(FORMNO) =? AND TRIM(SHIPPINGBILLDATE) = ? AND TRIM(PORTCODE) = ? ";
         

          LoggableStatement pps = new LoggableStatement(con, sq);
          pps.setString(1, "Completed");
          pps.setString(2, formno);
          pps.setString(3, temp_shipbilldate);
          pps.setString(4, portcode);
          logger.info(
            "----------EDPMS ------TRRDWLD--- processTRRjob-UPDATE -ETT_TRR_SHP_STG query-------ps" +
            pps.getQueryString());
         
          pps.close();
        }
        insertTRRAudit(shipbillno, shipbilldate, portcode, iecode, formno, exporttype,
          exportagency, newadcode, exadcode, userName, trrSeq);
        xmlCount++;
      }
      Element nob = document.createElement("noOfShippingBills");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,TRRXML_SEQNO.NEXTVAL AS TRR_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("TRR_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.TRR_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".trr";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_TRR_AUDIT_TABLE", fileName, fileLoc, "TRR_SEQ_NO", trrSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------TRRDWLD--- processTRRjob-exception-------ps" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
  }
 
  public String processEBRC()
  {
    String status = "success";
    String result1 = "success";
    String result2 = "success";
    try
    {
      result1 = processCancelEBRCjob();
     
      logger.info("----------EDPMS ------EBRCDWLD--- [process]-  result--111111-" + result1);
     

      result2 = processEBRCjob();
     
      logger.info("----------EDPMS ------EBRCDWLD--- [process]-  result-2222222222--" + result2);
      if ((result1.equals("success")) || (result2.equals("success"))) {
        status = "success";
      } else if ((result1.equals("error")) || (result2.equals("error"))) {
        status = "error";
      } else if ((result1.equals("nodatafound")) || (result2.equals("nodatafound"))) {
        status = "nodatafound";
      }
      logger.info("----------EDPMS ------PRNDWLD--- [processPRN]  (-_-)- amendmentReport---" + status);
    }
    catch (Exception e)
    {
      status = "error";
      e.printStackTrace();
    }
    return status;
  }
 
  public String processCancelEBRCjob()
  {
    logger.info("----------EDPMS ------EBRCDWLD--- [processCancelEBRCjob]-  processCancelEBRCjob---");
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet res = null;
    int xmlCount = 0;
    String result = "success";
    int ebrcSeq = 0;
    boolean refNof = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
      con = DBConnectionUtility.getConnection();
      if (con == null) {
        logger.info("----------EDPMS ------EBRCDWLD--- [processCancelEBRCjob]- Connection Is Null--");
      }
      ebrcSeq = seqGeneration("EBRC_SEQNO_GEN");
     
      String fileName = "";
     
      DateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
     
      Date date1 = new Date();
     
      String prodate = dateFormat1.format(date1);
     
      logger.info("----------EDPMS ------EBRCDWLD--- [processCancelEBRCjob]- prodate-----" + prodate);
     
      String sq = "SELECT LPAD(EBRC_SEQNO.NEXTVAL,3,'0')ID FROM DUAL";
      PreparedStatement pps = con.prepareStatement(sq);
      ResultSet rst = pps.executeQuery();
      int seq = 0;
      if (rst.next())
      {
        seq = rst.getInt(1);
        logger.info("----------EDPMS ------EBRCDWLD--- [processCancelEBRCjob]- seq-----" + seq);
      }
      rst.close();
      pps.close();
     
      fileName = "KKBK0000000" + prodate + String.format("%03d", new Object[] { Integer.valueOf(seq) });
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("BRCDATA");
      document.appendChild(root);
     
      Element fname = document.createElement("FILENAME");
      fname.appendChild(document.createTextNode(fileName));
      root.appendChild(fname);
     
      Element rocdoc2 = document.createElement("ENVELOPE");
      root.appendChild(rocdoc2);
     
      String sqlQuery11 = "select NVL(TRIM(BRCNO), ' ') AS BRCNO, TO_CHAR(TO_DATE(BRCDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS BRCDATE, NVL(TRIM(STATUS), ' ') AS STATUS, NVL(TRIM(IEC), ' ') AS IEC, NVL(TRIM(EXPNAME), ' ') AS EXPNAME, NVL(TRIM(IFSC), ' ') AS IFSC, NVL(TRIM(BILLID), ' ') AS BILLID, NVL(TRIM(SNO), ' ') AS SNO, NVL(TRIM(SPORT), ' ') AS SPORT, TO_CHAR(TO_DATE(SDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS SDATE, NVL(TRIM(SCC), ' ') AS SCC, NVL(TRIM(SVALUE), ' ') AS SVALUE, NVL(TRIM(RCC), ' ') AS RCC, NVL(TRIM(RVALUE), ' ') AS RVALUE, TO_CHAR(TO_DATE(RDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS RDATE, NVL(TRIM(RVALUEINR), ' ') AS RVALUEINR, NVL(TRIM(RMTBANK), ' ') AS RMTBANK, NVL(TRIM(RMTCITY), ' ') AS RMTCITY, NVL(TRIM(RMTCTRY), ' ') AS RMTCTRY, NVL(TRIM(FILENAME), ' ') AS FILENAME, NVL(TRIM(FILE_ERRCODE), ' ') AS FILE_ERRCODE, NVL(TRIM(BRC_ERRORCODE), ' ') AS BRC_ERRORCODE FROM ETT_EBRC_FILES WHERE STATUS IN ('C')";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        sqlQuery11 = sqlQuery11 + " AND TRIM(BILL_REFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        sqlQuery11 = sqlQuery11 + " AND TRIM(IEC) = ?";
        iecodef = true;
      }
      LoggableStatement ps2 = new LoggableStatement(con, sqlQuery11);
      if (refNof) {
        ps2.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (iecodef) {
        ps2.setString(++filtercount, this.vo.getIecode().trim());
      }
      logger.info("----------EDPMS ------EBRCDWLD--- [processCancelEBRCjob]- ETT_EBRC_FILES-- query---" +
        ps2.getQueryString());
      ResultSet res1 = ps2.executeQuery();
      while (res1.next())
      {
        String brcNo = res1.getString("BRCNO");
        String brcDate = res1.getString("BRCDATE");
        String status = res1.getString("STATUS");
        String ieCode = res1.getString("IEC");
        String exportName = res1.getString("EXPNAME");
        String codeIFSC = res1.getString("IFSC");
        String billID = res1.getString("BILLID");
        String shipNo = res1.getString("SNO");
        String shipPort = res1.getString("SPORT");
        String shipDate = res1.getString("SDATE");
        String shipCurr = res1.getString("SCC");
        String shipValue = res1.getString("SVALUE");
        String realCurr = res1.getString("RCC");
        String realValue = res1.getString("RVALUE");
        String realDate = res1.getString("RDATE");
       
        Element rocdoc1 = document.createElement("EBRC");
        rocdoc2.appendChild(rocdoc1);
        if (brcNo == null)
        {
          Element pc = document.createElement("BRCNO");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("BRCNO");
          pc.appendChild(document.createTextNode(brcNo));
          rocdoc1.appendChild(pc);
        }
        if (brcDate == null)
        {
          Element ri = document.createElement("BRCDATE");
          ri.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ri);
        }
        else
        {
          Element ri = document.createElement("BRCDATE");
          ri.appendChild(document.createTextNode(brcDate));
          rocdoc1.appendChild(ri);
        }
        if (status == null)
        {
          Element et = document.createElement("STATUS");
          et.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(et);
        }
        else
        {
          Element et = document.createElement("STATUS");
          et.appendChild(document.createTextNode(status));
          rocdoc1.appendChild(et);
        }
        if (ieCode == null)
        {
          Element sbn = document.createElement("IEC");
          sbn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("IEC");
          sbn.appendChild(document.createTextNode(ieCode));
          rocdoc1.appendChild(sbn);
        }
        if (exportName == null)
        {
          Element sbd = document.createElement("EXPNAME");
          sbd.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("EXPNAME");
          sbd.appendChild(document.createTextNode(exportName));
          rocdoc1.appendChild(sbd);
        }
        if (codeIFSC == null)
        {
          Element fn = document.createElement("IFSC");
          fn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(fn);
        }
        else
        {
          Element fn = document.createElement("IFSC");
          fn.appendChild(document.createTextNode(codeIFSC));
          rocdoc1.appendChild(fn);
        }
        if (billID == null)
        {
          Element ld = document.createElement("BILLID");
          ld.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ld);
        }
        else
        {
          Element ld = document.createElement("BILLID");
          ld.appendChild(document.createTextNode(billID));
          rocdoc1.appendChild(ld);
        }
        if (shipNo == null)
        {
          Element ic = document.createElement("SNO");
          ic.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ic);
        }
        else
        {
          Element ic = document.createElement("SNO");
          ic.appendChild(document.createTextNode(shipNo));
          rocdoc1.appendChild(ic);
        }
        if (shipPort == null)
        {
          Element cic = document.createElement("SPORT");
          cic.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(cic);
        }
        else
        {
          Element cic = document.createElement("SPORT");
          cic.appendChild(document.createTextNode(shipPort));
          rocdoc1.appendChild(cic);
        }
        if (shipDate == null)
        {
          Element ac = document.createElement("SDATE");
          ac.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("SDATE");
          ac.appendChild(document.createTextNode(shipDate));
          rocdoc1.appendChild(ac);
        }
        if (shipCurr == null)
        {
          Element aea = document.createElement("SCC");
          aea.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(aea);
        }
        else
        {
          Element aea = document.createElement("SCC");
          aea.appendChild(document.createTextNode(shipCurr));
          rocdoc1.appendChild(aea);
        }
        if (shipValue == null)
        {
          Element dd = document.createElement("SVALUE");
          dd.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(dd);
        }
        else
        {
          Element dd = document.createElement("SVALUE");
          dd.appendChild(document.createTextNode(shipValue));
          rocdoc1.appendChild(dd);
        }
        if (realCurr == null)
        {
          Element abn = document.createElement("RCC");
          abn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(abn);
        }
        else
        {
          Element abn = document.createElement("RCC");
          abn.appendChild(document.createTextNode(realCurr));
          rocdoc1.appendChild(abn);
        }
        if (realValue == null)
        {
          Element dn = document.createElement("RVALUE");
          dn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("RVALUE");
          dn.appendChild(document.createTextNode(realValue));
          rocdoc1.appendChild(dn);
        }
        if ((realDate == null) || (realDate == ""))
        {
          Element bn = document.createElement("RDATE");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("RDATE");
          bn.appendChild(document.createTextNode(realDate));
          rocdoc1.appendChild(bn);
        }
        String updateStatus = "UPDATE ETT_EBRC_FILES SET STATUS='CD' , FILENAME ='" + fileName +
          "' where BRCNO='" + brcNo + "'";
       
        LoggableStatement ps3 = new LoggableStatement(con, updateStatus);
        logger.info(
          "----------EDPMS ------EBRCDWLD--- [processCancelEBRCjob]- vUPDATE ETT_EBRC_FILES-- query---" +
          ps3.getQueryString());
       

        ps3.close();
       
        insertEBRCCancelAudit(brcNo, brcDate, status, ieCode, exportName, codeIFSC, billID, shipNo, shipPort,
          shipDate, shipCurr, shipValue, realCurr, realValue, realDate, userName, ebrcSeq);
        xmlCount++;
      }
      res1.close();
      ps2.close();
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String eodDate = null;
        if (rs1.next()) {
          eodDate = rs1.getString("PROCDATE");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.CANCEL_EBRC_ONLINE;
       

        fileLoc = fileLoc + eodDate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_EBRC_CANCEL_AUDIT_TABLE", fileName, fileLoc, "EBRC_SEQ_NO", ebrcSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------EBRCDWLD--- [processCancelEBRCjob]- exception---" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
  }
 
  public String processEBRCjob()
  {
    logger.info("----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  result--111111-");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    CommonMethods commonMethods = null;
    int xmlCount = 0;
    String result = "success";
    int ebrcSeq = 0;
    boolean refNof = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
      con = DBConnectionUtility.getConnection();
      if (con == null) {
        logger.info("----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  Connection is NUll--111111-");
      }
      commonMethods = new CommonMethods();
     
      ebrcSeq = seqGeneration("EBRC_SEQNO_GEN");
     
      String fileName = "";
     
      SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
     
      SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
     
      DateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
     
      Date date1 = new Date();
     
      String prodate = dateFormat1.format(date1);
     
      String processeddate = setDate(con);
     
      String sqlQuery = "SELECT * FROM ETTV_EBRC_LODGEMENT_VW WHERE 1=1";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        sqlQuery = sqlQuery + " AND TRIM(MASTERREF) = ?";
        refNof = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        sqlQuery = sqlQuery + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = new LoggableStatement(con, sqlQuery);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      logger.info("----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  exception-------------" +
        ps.getQueryString());
      res = ps.executeQuery();
      while (res.next())
      {
        String brcDate = processeddate;
        String status = "F";
        String rbiRefNo = commonMethods.getEmptyIfNull(res.getString("BILLID"));
        String billRef = commonMethods.getEmptyIfNull(res.getString("MASTERREF"));
        String eventRef = commonMethods.getEmptyIfNull(res.getString("EVENTREF"));
        String ieCode = commonMethods.getEmptyIfNull(res.getString("IECODE"));
        String exportName = commonMethods.getEmptyIfNull(res.getString("EXPORTERNAME"));
        exportName = exportName.replaceAll("[^A-Za-z0-9 ]", "");
        String codeIFSC = commonMethods.getEmptyIfNull(res.getString("BANKCODE"));
        String shipNo = commonMethods.getEmptyIfNull(res.getString("BILLNO"));
        String shipPort = commonMethods.getEmptyIfNull(res.getString("PORTCODE"));
        String shipDate = commonMethods.getEmptyIfNull(res.getString("SHIP_DATE"));
        Date shippingDate = format1.parse(shipDate);
        String shipCurr = commonMethods.getEmptyIfNull(res.getString("SHIP_CURR"));
        String shipValue = commonMethods.getEmptyIfNull(res.getString("BILL_AMT"));
        String realCurr = commonMethods.getEmptyIfNull(res.getString("CURRENCYOFREALIZATION"));
        String realValue = commonMethods.getEmptyIfNull(res.getString("TotalREALIZED"));
        String realDate = commonMethods.getEmptyIfNull(res.getString("date of realization"));
        String totalReal = commonMethods.getEmptyIfNull(res.getString("Total realised Value in INR"));
        String rmtBank = commonMethods.getEmptyIfNull(res.getString("REMITTER_NAME"));
        String rmtCity = commonMethods.getEmptyIfNull(res.getString("REMITTER_ADDRESS"));
        String rmtCountry = commonMethods.getEmptyIfNull(res.getString("REMITTER_COUNTRY"));
       
        String temp_rmtBank = rmtBank.replaceAll("[^A-Za-z0-9 ]", "");
        String temp_rmtCity = rmtCity.replaceAll("[^A-Za-z0-9 ]", "");
       
        String tempDate = format2.format(shippingDate);
        if ((!billRef.equalsIgnoreCase("")) && (!eventRef.equalsIgnoreCase("")) && (!shipNo.equalsIgnoreCase("")) &&
          (!tempDate.equalsIgnoreCase("")))
        {
          boolean isAlreadyExits = checkingData(con, billRef, eventRef, shipNo, tempDate, shipPort);
          if (!isAlreadyExits)
          {
            String brcNo = null;
            if (codeIFSC != null)
            {
              String squ = "SELECT ETT_GET_BRC_NO('" + codeIFSC.trim() + "') AS brcNo FROM DUAL";
              PreparedStatement pps = con.prepareStatement(squ);
              ResultSet rst2 = pps.executeQuery();
              if (rst2.next()) {
                brcNo = rst2.getString("brcNo");
              }
              rst2.close();
              pps.close();
            }
            Date realDate1 = format1.parse(realDate);
           
            String sqlInsertQuery = "INSERT INTO ETT_EBRC_FILES(BRCNO,BRCDATE,STATUS,BILL_REFNO,EVENT_REFNO,IEC,EXPNAME,IFSC,BILLID,SNO,SPORT,SDATE,SCC,SVALUE,RCC,RVALUE,RDATE,RVALUEINR,RMTBANK,RMTCITY,RMTCTRY,CREATED_DATE) VALUES (?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,SYSTIMESTAMP)";
           




            LoggableStatement ps1 = new LoggableStatement(con, sqlInsertQuery);
           
            ps1.setString(1, commonMethods.getEmptyIfNull(brcNo).trim());
            ps1.setString(2, commonMethods.getEmptyIfNull(brcDate).trim());
            ps1.setString(3, commonMethods.getEmptyIfNull(status).trim());
            ps1.setString(4, commonMethods.getEmptyIfNull(billRef).trim());
            ps1.setString(5, commonMethods.getEmptyIfNull(eventRef).trim());
            ps1.setString(6, commonMethods.getEmptyIfNull(ieCode).trim());
            ps1.setString(7, commonMethods.getEmptyIfNull(exportName).toUpperCase().trim());
            ps1.setString(8, commonMethods.getEmptyIfNull(codeIFSC).trim());
            ps1.setString(9, commonMethods.getEmptyIfNull(rbiRefNo).trim());
            ps1.setString(10, commonMethods.getEmptyIfNull(shipNo).trim());
            ps1.setString(11, commonMethods.getEmptyIfNull(shipPort).trim());
            ps1.setString(12, format2.format(shippingDate));
            ps1.setString(13, commonMethods.getEmptyIfNull(shipCurr).trim());
            ps1.setString(14, commonMethods.getEmptyIfNull(shipValue).trim());
            ps1.setString(15, commonMethods.getEmptyIfNull(realCurr).trim());
            ps1.setString(16, commonMethods.getEmptyIfNull(realValue).trim());
            ps1.setString(17, format2.format(realDate1));
            ps1.setString(18, commonMethods.getEmptyIfNull(totalReal).trim());
            ps1.setString(19,
              commonMethods.getEmptyIfNull(temp_rmtBank).toUpperCase().trim().replace(",", " "));
            ps1.setString(20,
              commonMethods.getEmptyIfNull(temp_rmtCity).toUpperCase().trim().replace(",", " "));
            ps1.setString(21, commonMethods.getEmptyIfNull(rmtCountry).toUpperCase().trim());
           
            logger.info(
              "----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  INSERT ETT_EBRC_FILES-------------" +
              ps1.getQueryString());
           
            int a = ps1.executeUpdate();
            logger.info("----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  a---INSERT ETT_EBRC_FILES-----Insert  count------" + a);
            ps1.close();
          }
        }
      }
      String sq = "SELECT LPAD(EBRC_SEQNO.NEXTVAL,3,'0')ID FROM DUAL";
      PreparedStatement pps = con.prepareStatement(sq);
      ResultSet rst = pps.executeQuery();
      int seq = 0;
      if (rst.next()) {
        seq = rst.getInt(1);
      }
      rst.close();
      pps.close();
     
      fileName = "KKBK0000000" + prodate + String.format("%03d", new Object[] { Integer.valueOf(seq) });
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("BRCDATA");
      document.appendChild(root);
     
      Element fname = document.createElement("FILENAME");
      fname.appendChild(document.createTextNode(fileName));
      root.appendChild(fname);
     
      Element rocdoc2 = document.createElement("ENVELOPE");
      root.appendChild(rocdoc2);
     
      String sqlQuery11 = "select NVL(TRIM(BRCNO), ' ') AS BRCNO, TO_CHAR(TO_DATE(BRCDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS BRCDATE, NVL(TRIM(STATUS), ' ') AS STATUS, NVL(TRIM(IEC), ' ') AS IEC, NVL(TRIM(EXPNAME), ' ') AS EXPNAME, NVL(TRIM(IFSC), ' ') AS IFSC, NVL(TRIM(BILLID), ' ') AS BILLID, NVL(TRIM(SNO), ' ') AS SNO, NVL(TRIM(SPORT), ' ') AS SPORT, TO_CHAR(TO_DATE(SDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS SDATE, NVL(TRIM(SCC), ' ') AS SCC, NVL(TRIM(SVALUE), ' ') AS SVALUE, NVL(TRIM(RCC), ' ') AS RCC, NVL(TRIM(RVALUE), ' ') AS RVALUE, TO_CHAR(TO_DATE(RDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS RDATE, NVL(TRIM(RVALUEINR), ' ') AS RVALUEINR, NVL(TRIM(RMTBANK), ' ') AS RMTBANK, NVL(TRIM(RMTCITY), ' ') AS RMTCITY, NVL(TRIM(RMTCTRY), ' ') AS RMTCTRY, NVL(TRIM(FILENAME), ' ') AS FILENAME, NVL(TRIM(FILE_ERRCODE), ' ') AS FILE_ERRCODE, NVL(TRIM(BRC_ERRORCODE), ' ') AS BRC_ERRORCODE FROM ETT_EBRC_FILES WHERE STATUS IN ('F')";
     









      filtercount = 0;
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        sqlQuery11 = sqlQuery11 + " AND TRIM(BILL_REFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        sqlQuery11 = sqlQuery11 + " AND TRIM(IEC) = ?";
        iecodef = true;
      }
      LoggableStatement ps2 = new LoggableStatement(con, sqlQuery11);
      if (refNof) {
        ps2.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (iecodef) {
        ps2.setString(++filtercount, this.vo.getIecode().trim());
      }
      logger.info(
        "----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  a---INSERT ETT_EBRC_FILES-----ETT_EBRC_FILES Select Query------" +
        ps2.getQueryString());
      ResultSet res1 = ps2.executeQuery();
      while (res1.next())
      {
        String brcNo = res1.getString("BRCNO");
        String brcDate = res1.getString("BRCDATE");
        String status = res1.getString("STATUS");
        String ieCode = res1.getString("IEC");
        String exportName = res1.getString("EXPNAME");
        String codeIFSC = res1.getString("IFSC");
        String billID = res1.getString("BILLID");
        String shipNo = res1.getString("SNO");
        String shipPort = res1.getString("SPORT");
        String shipDate = res1.getString("SDATE");
        String shipCurr = res1.getString("SCC");
        String shipValue = res1.getString("SVALUE");
        String realCurr = res1.getString("RCC");
        String realValue = res1.getString("RVALUE");
        String realDate = res1.getString("RDATE");
       
        Element rocdoc1 = document.createElement("EBRC");
        rocdoc2.appendChild(rocdoc1);
        if (brcNo == null)
        {
          Element pc = document.createElement("BRCNO");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("BRCNO");
          pc.appendChild(document.createTextNode(brcNo));
          rocdoc1.appendChild(pc);
        }
        if (brcDate == null)
        {
          Element ri = document.createElement("BRCDATE");
          ri.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ri);
        }
        else
        {
          Element ri = document.createElement("BRCDATE");
          ri.appendChild(document.createTextNode(brcDate));
          rocdoc1.appendChild(ri);
        }
        if (status == null)
        {
          Element et = document.createElement("STATUS");
          et.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(et);
        }
        else
        {
          Element et = document.createElement("STATUS");
          et.appendChild(document.createTextNode(status));
          rocdoc1.appendChild(et);
        }
        if (ieCode == null)
        {
          Element sbn = document.createElement("IEC");
          sbn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("IEC");
          sbn.appendChild(document.createTextNode(ieCode));
          rocdoc1.appendChild(sbn);
        }
        if (exportName == null)
        {
          Element sbd = document.createElement("EXPNAME");
          sbd.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("EXPNAME");
          sbd.appendChild(document.createTextNode(exportName));
          rocdoc1.appendChild(sbd);
        }
        if (codeIFSC == null)
        {
          Element fn = document.createElement("IFSC");
          fn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(fn);
        }
        else
        {
          Element fn = document.createElement("IFSC");
          fn.appendChild(document.createTextNode(codeIFSC));
          rocdoc1.appendChild(fn);
        }
        if (billID == null)
        {
          Element ld = document.createElement("BILLID");
          ld.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ld);
        }
        else
        {
          Element ld = document.createElement("BILLID");
          ld.appendChild(document.createTextNode(billID));
          rocdoc1.appendChild(ld);
        }
        if (shipNo == null)
        {
          Element ic = document.createElement("SNO");
          ic.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ic);
        }
        else
        {
          Element ic = document.createElement("SNO");
          ic.appendChild(document.createTextNode(shipNo));
          rocdoc1.appendChild(ic);
        }
        if (shipPort == null)
        {
          Element cic = document.createElement("SPORT");
          cic.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(cic);
        }
        else
        {
          Element cic = document.createElement("SPORT");
          cic.appendChild(document.createTextNode(shipPort));
          rocdoc1.appendChild(cic);
        }
        if (shipDate == null)
        {
          Element ac = document.createElement("SDATE");
          ac.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("SDATE");
          ac.appendChild(document.createTextNode(shipDate));
          rocdoc1.appendChild(ac);
        }
        if (shipCurr == null)
        {
          Element aea = document.createElement("SCC");
          aea.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(aea);
        }
        else
        {
          Element aea = document.createElement("SCC");
          aea.appendChild(document.createTextNode(shipCurr));
          rocdoc1.appendChild(aea);
        }
        if (shipValue == null)
        {
          Element dd = document.createElement("SVALUE");
          dd.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(dd);
        }
        else
        {
          Element dd = document.createElement("SVALUE");
          dd.appendChild(document.createTextNode(shipValue));
          rocdoc1.appendChild(dd);
        }
        if (realCurr == null)
        {
          Element abn = document.createElement("RCC");
          abn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(abn);
        }
        else
        {
          Element abn = document.createElement("RCC");
          abn.appendChild(document.createTextNode(realCurr));
          rocdoc1.appendChild(abn);
        }
        if (realValue == null)
        {
          Element dn = document.createElement("RVALUE");
          dn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("RVALUE");
          dn.appendChild(document.createTextNode(realValue));
          rocdoc1.appendChild(dn);
        }
        if ((realDate == null) || (realDate == ""))
        {
          Element bn = document.createElement("RDATE");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("RDATE");
          bn.appendChild(document.createTextNode(realDate));
          rocdoc1.appendChild(bn);
        }
        String updateStatus = "UPDATE ETT_EBRC_FILES SET STATUS='Downloaded' , FILENAME ='" + fileName +
          "' where BRCNO='" + brcNo + "'";
       
        LoggableStatement ps3 = new LoggableStatement(con, updateStatus);
        logger.info(
          "----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  a---INSERT ETT_EBRC_FILES-- UPDATE---ETT_EBRC_FILES ------" +
          ps3.getQueryString());
       



        ps3.close();
       
        insertEBRCAudit(brcNo, brcDate, status, ieCode, exportName, codeIFSC, billID, shipNo, shipPort,
          shipDate, shipCurr, shipValue, realCurr, realValue, realDate, userName, ebrcSeq);
        xmlCount++;
      }
      res1.close();
      ps2.close();
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String eodDate = null;
        if (rs1.next()) {
          eodDate = rs1.getString("PROCDATE");
        }
        logger.info("eodDate-----------------------------" + eodDate);
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.EBRC_ONLINE;
        logger.info("fileLoc-----------------------------" + fileLoc);
       

        fileLoc = fileLoc + eodDate + "/";
        File f = new File(fileLoc);
        if (!f.exists())
        {
          f.mkdir();
         
          logger.info("File Created--------------------------" + fileLoc);
        }
        else
        {
          logger.info("fileLoc--Already Exst---------------------------" + fileLoc);
        }
        String myFilePath = fileLoc + fileName + ".xml";
        logger.info("fileLoc----------fileLoc-------------------" + fileLoc);
        logger.info("fileName----------fileName-------------------" + fileName);
        logger.info("domSource----------domSource-------------------" + domSource);
        logger.info("myFilePath----------myFilePath-------------------" + myFilePath);
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  exception-------------");
       
        updateAuditTrail("ETT_EBRC_AUDIT_TABLE", fileName, fileLoc, "EBRC_SEQ_NO", ebrcSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------EBRCDWLD--- [processEBRCjob]-  exception-------------" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
  }
 
  public String processIRFIRCjob()
  {
    logger.info("----------EDPMS ------IRFIRCDWLD--- [processIRFIRCjob]- ---");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    CommonMethods commonMethods = null;
    String noofshipbills = "0";
    int xmlCount = 0;
    String result = "success";
    int irfircSeq = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
      con = DBConnectionUtility.getConnection();
      if (con == null) {
        logger.info("----------EDPMS ------IRFIRCDWLD--- [processIRFIRCjob]- -COnneciont Issue Occured--");
      }
      irfircSeq = seqGeneration("IRFIRC_SEQNO_GEN");
     
      commonMethods = new CommonMethods();
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("remittances");
      root.appendChild(rocdoc2);
      String query = "SELECT TRXN_REFERENCE,BANK_ADCODE,FIRC_FLAG,FIRC_NUMBER, TO_CHAR(TO_DATE(FIRC_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS FIRC_DATE,FIRC_AMT, RECIND FROM ETTV_IRM_FIRC_FILES ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0)) {
        query = query + " WHERE TRIM(TRXN_REFERENCE) = ?";
      }
      ps = new LoggableStatement(con, query);
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0)) {
        ps.setString(1, this.vo.getBillRefNo());
      }
      logger.info("----------EDPMS ------IRFIRCDWLD--- [processIRFIRCjob]- -ETTV_IRM_FIRC_FILES Query-" +
        ps.getQueryString());
      res = ps.executeQuery();
      while (res.next())
      {
        String masterRefNo = commonMethods.getEmptyIfNull(res.getString("TRXN_REFERENCE")).trim();
        String adCode = commonMethods.getEmptyIfNull(res.getString("BANK_ADCODE")).trim();
        String fircFlag = commonMethods.getEmptyIfNull(res.getString("FIRC_FLAG")).trim();
        String fircNo = commonMethods.getEmptyIfNull(res.getString("FIRC_NUMBER")).trim();
        String fircDate = commonMethods.getEmptyIfNull(res.getString("FIRC_DATE")).trim();
        String fircAmt = commonMethods.getEmptyIfNull(res.getString("FIRC_AMT")).trim();
        String recInd = commonMethods.getEmptyIfNull(res.getString("RECIND")).trim();
       
        Element rocdoc1 = document.createElement("remittance");
        rocdoc2.appendChild(rocdoc1);
        if (masterRefNo == null)
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(masterRefNo));
          rocdoc1.appendChild(pc);
        }
        if (adCode == null)
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(adCode));
          rocdoc1.appendChild(sbn);
        }
        if (fircFlag == null)
        {
          Element sbd = document.createElement("fircFlag");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("fircFlag");
          sbd.appendChild(document.createTextNode(fircFlag));
          rocdoc1.appendChild(sbd);
        }
        if (fircNo == null)
        {
          Element sbd = document.createElement("fircNumber");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("fircNumber");
          sbd.appendChild(document.createTextNode(fircNo));
          rocdoc1.appendChild(sbd);
        }
        if (fircDate == null)
        {
          Element sbd = document.createElement("fircIssueDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("fircIssueDate");
          sbd.appendChild(document.createTextNode(fircDate));
          rocdoc1.appendChild(sbd);
        }
        if (fircAmt == null)
        {
          Element sbd = document.createElement("fircAmount");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("fircAmount");
          sbd.appendChild(document.createTextNode(fircAmt));
          rocdoc1.appendChild(sbd);
        }
        if (recInd == null)
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(recInd));
          rocdoc1.appendChild(dn);
        }
        int nobill = Integer.parseInt(noofshipbills);
        nobill++;
        noofshipbills = Integer.toString(nobill);
       
        String sq1 = "UPDATE ETT_FIRC_LODGEMENT SET IRMSTATUS='Completed' WHERE TRANS_REF_NO='" + masterRefNo +
          "'" + " AND FIRC_SERIAL_NO='" + fircNo + "'";
        LoggableStatement pps1 = new LoggableStatement(con, sq1);
       
        logger.info(
          "----------EDPMS ------IRFIRCDWLD--- [processIRFIRCjob]- ETT_FIRC_LODGEMENT-UPDATE ETT_FIRC_LODGEMENT Query-" +
          pps1.getQueryString());
       

        pps1.close();
       
        String sql1234 = "UPDATE ETT_INW_REMITTANCE   SET EOD_STATUS='Completed'  WHERE REMIT_FIRC_NO='" +
          fircNo + "'";
       
        LoggableStatement pps2 = new LoggableStatement(con, sql1234);
        logger.info(
          "----------EDPMS ------IRFIRCDWLD--- [processIRFIRCjob]- ETT_INW_REMITTANCE-UPDATE ETT_FIRC_LODGEMENT Query-" +
          pps2.getQueryString());
       


        pps2.close();
       
        insertIRFIRCAudit(masterRefNo, adCode, fircFlag, fircNo, fircDate, fircAmt, recInd, userName, irfircSeq);
        xmlCount++;
      }
      Element nob = document.createElement("noOfIrm");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,IRMFIRCXML_SEQNO.NEXTVAL AS IRFIRC_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("IRFIRC_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.IRFIRC_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".irfirc";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_IRFIRC_AUDIT_TABLE", fileName, fileLoc, "IRFIRC_SEQ_NO", irfircSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------IRFIRCDWLD--- [processIRFIRCjob]- -exception--" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
  }
 
  public String processIRMEXjob()
  {
    logger.info("----------EDPMS ------IRMEXTDWLD--- [processIRMEXjob]- -result-");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    CommonMethods commonMethods = null;
    String noofshipbills = "0";
    int xmlCount = 0;
    String result = "success";
    int irextSeq = 0;
    boolean refNof = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
      con = DBConnectionUtility.getConnection();
      if (con == null) {
        logger.info("----------EDPMS ------IRMEXTDWLD--- [processIRMEXjob]-ConnectionIssue ----");
      }
      irextSeq = seqGeneration("IREXT_SEQNO_GEN");
     
      commonMethods = new CommonMethods();
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("remittances");
      root.appendChild(rocdoc2);
     
      String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,EXT_IND,TO_CHAR(TO_DATE(EXT_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS EXT_DATE,LETTER_NO,TO_CHAR(TO_DATE(LETTER_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS LETTER_DATE,REC_IND,REASON FROM ETT_IRM_EXT_TBL WHERE  STATUS = 'A' AND EXTSTATUS = 'Pending' ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        query = query + " AND TRIM(MASTER_REFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        query = query + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = new LoggableStatement(con, query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      logger.info("----------EDPMS ------IRMEXTDWLD--- [processIRMEXjob]-ETT_IRM_EXT_TBL Query ----" +
        ps.getQueryString());
      res = ps.executeQuery();
      while (res.next())
      {
        String serialNo = commonMethods.getEmptyIfNull(res.getString("S_NO")).trim();
        String masterRefNo = commonMethods.getEmptyIfNull(res.getString("MASTER_REFNO")).trim();
        String adCode = commonMethods.getEmptyIfNull(res.getString("ADCODE")).trim();
        String ieCode = commonMethods.getEmptyIfNull(res.getString("IECODE")).trim();
        String extInd = commonMethods.getEmptyIfNull(res.getString("EXT_IND")).trim();
        String extDate = commonMethods.getEmptyIfNull(res.getString("EXT_DATE")).trim();
        String letterNo = commonMethods.getEmptyIfNull(res.getString("LETTER_NO")).trim();
        String letterDate = commonMethods.getEmptyIfNull(res.getString("LETTER_DATE")).trim();
        String recInd = commonMethods.getEmptyIfNull(res.getString("REC_IND")).trim();
        String reason = commonMethods.getEmptyIfNull(res.getString("REASON")).trim();
       
        Element rocdoc1 = document.createElement("remittance");
        rocdoc2.appendChild(rocdoc1);
        if (masterRefNo == null)
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(masterRefNo));
          rocdoc1.appendChild(pc);
        }
        if (adCode == null)
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(adCode));
          rocdoc1.appendChild(sbn);
        }
        if (ieCode == null)
        {
          Element sbd = document.createElement("ieCode");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("ieCode");
          sbd.appendChild(document.createTextNode(ieCode));
          rocdoc1.appendChild(sbd);
        }
        if (letterNo == null)
        {
          Element sbd = document.createElement("letterNumber");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("letterNumber");
          sbd.appendChild(document.createTextNode(letterNo));
          rocdoc1.appendChild(sbd);
        }
        if (letterDate == null)
        {
          Element sbd = document.createElement("letterDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("letterDate");
          sbd.appendChild(document.createTextNode(letterDate));
          rocdoc1.appendChild(sbd);
        }
        if (extDate == null)
        {
          Element sbd = document.createElement("extensionDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("extensionDate");
          sbd.appendChild(document.createTextNode(extDate));
          rocdoc1.appendChild(sbd);
        }
        if (recInd == null)
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(recInd));
          rocdoc1.appendChild(dn);
        }
        if (extInd == null)
        {
          Element sbd = document.createElement("extensionInd");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("extensionInd");
          sbd.appendChild(document.createTextNode(extInd));
          rocdoc1.appendChild(sbd);
        }
        if (reason == null)
        {
          Element dn = document.createElement("remarks");
          dn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("remarks");
          dn.appendChild(document.createTextNode(reason));
          rocdoc1.appendChild(dn);
        }
        int nobill = Integer.parseInt(noofshipbills);
        nobill++;
        noofshipbills = Integer.toString(nobill);
       
        String sq1 = "UPDATE ETT_IRM_EXT_TBL SET EXTSTATUS='Completed' WHERE MASTER_REFNO = '" + masterRefNo +
          "'" + " AND S_NO = '" + serialNo + "'";
        logger.info(sq1);
       
        LoggableStatement pps1 = new LoggableStatement(con, sq1);
        logger.info(
          "----------EDPMS ------IRMEXTDWLD--- [processIRMEXjob]-UPDATE Query ETT_IRM_EXT_TBL----" +
          pps1.getQueryString());
       

        con.commit();
        pps1.close();
       
        insertIREXTAudit(serialNo, masterRefNo, adCode, ieCode, extInd, extDate, letterNo, letterDate, recInd,
          reason, userName, irextSeq);
        xmlCount++;
      }
      Element nob = document.createElement("noOfIrm");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,IRMEXTXML_SEQNO.NEXTVAL AS IRMEX_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("IRMEX_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.IREXT_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".irmex";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_IREXT_AUDIT_TABLE", fileName, fileLoc, "IREXT_SEQ_NO", irextSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------IRMEXTDWLD--- [processIRMEXjob]-Exception----" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
  }
 
  public String processIRMCL()
  {
    String result1 = "success";
    String result2 = "success";
    String status = "success";
    try
    {
      result1 = processCancelIRMCLjob();
     
      logger.info(
        "----------EDPMS ------IRMCLDWLD--- [processIRMCL]- -processCancelIRMCLjob-------result------" +
        result1);
     

      result2 = processIRMCLjob();
     
      logger.info(
        "----------EDPMS ------IRMCLDWLD--- [processIRMCL]- -processIRMCLjob-------result------" + result2);
      if ((result1.equals("success")) || (result2.equals("success"))) {
        status = "success";
      } else if ((result1.equals("error")) || (result2.equals("error"))) {
        status = "error";
      } else if ((result1.equals("nodatafound")) || (result2.equals("nodatafound"))) {
        status = "nodatafound";
      }
      logger.info("Milestone 04A EDPMS-IRMCLS (-_-) " + status);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return status;
  }
 
  public String processCancelIRMCLjob()
  {
    logger.info("----------EDPMS ------IRMCLDWLD--- [processCancelIRMCLjob]- -");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    CommonMethods commonMethods = null;
    String noofshipbills = "0";
    int xmlCount = 0;
    String result = "success";
    int irmclSeq = 0;
    boolean refNof = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
      if (con == null) {
        logger.info(
          "----------EDPMS ------IRMCLDWLD--- [processCancelIRMCLjob]- Connection is Null-----------");
      }
      irmclSeq = seqGeneration("IRCLS_SEQNO_GEN");
     
      con = DBConnectionUtility.getConnection();
     
      commonMethods = new CommonMethods();
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("remittances");
      root.appendChild(rocdoc2);
     
      String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVED_BY,LETTER_NO,TO_CHAR(TO_DATE(ADJ_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS ADJ_DATE,REASON,DOC_NO,TO_CHAR(TO_DATE(DOC_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS DOC_DATE,DOC_PORT,REC_IND,CLOSE_IND,REMARKS FROM ETT_IRM_CLOSURE_TBL WHERE CLOSURE_STATUS = 'Pending' AND STATUS = 'C' ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        query = query + " AND TRIM(MASTER_REFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        query = query + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = new LoggableStatement(con, query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      logger.info(
        "----------EDPMS ------IRMCLDWLD--- [processCancelIRMCLjob]- ETT_IRM_CLOSURE_TBL is Query-----------" +
        ps.getQueryString());
     
      res = ps.executeQuery();
      while (res.next())
      {
        String serialNo = commonMethods.getEmptyIfNull(res.getString("S_NO")).trim();
        String masterRefNo = commonMethods.getEmptyIfNull(res.getString("MASTER_REFNO")).trim();
        String adCode = commonMethods.getEmptyIfNull(res.getString("ADCODE")).trim();
        String ieCode = commonMethods.getEmptyIfNull(res.getString("IECODE")).trim();
        String currency = commonMethods.getEmptyIfNull(res.getString("CURRENCY")).trim();
        String amount = commonMethods.getEmptyIfNull(res.getString("AMOUNT")).trim();
        String approvedBy = commonMethods.getEmptyIfNull(res.getString("APPROVED_BY")).trim();
        String letterNo = commonMethods.getEmptyIfNull(res.getString("LETTER_NO")).trim();
        String adjDate = commonMethods.getEmptyIfNull(res.getString("ADJ_DATE")).trim();
        String reason = commonMethods.getEmptyIfNull(res.getString("REASON")).trim();
        String docNo = commonMethods.getEmptyIfNull(res.getString("DOC_NO")).trim();
        String docDate = commonMethods.getEmptyIfNull(res.getString("DOC_DATE")).trim();
        String docPort = commonMethods.getEmptyIfNull(res.getString("DOC_PORT")).trim();
        String recInd = commonMethods.getEmptyIfNull(res.getString("REC_IND")).trim();
        String remarks = commonMethods.getEmptyIfNull(res.getString("REMARKS")).trim();
       
        Element rocdoc1 = document.createElement("remittance");
        rocdoc2.appendChild(rocdoc1);
        if (masterRefNo == null)
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(masterRefNo));
          rocdoc1.appendChild(pc);
        }
        if (adCode == null)
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(adCode));
          rocdoc1.appendChild(sbn);
        }
        if (ieCode == null)
        {
          Element sbd = document.createElement("ieCode");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("ieCode");
          sbd.appendChild(document.createTextNode(ieCode));
          rocdoc1.appendChild(sbd);
        }
        if (currency == null)
        {
          Element sbd = document.createElement("currency");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("currency");
          sbd.appendChild(document.createTextNode(currency));
          rocdoc1.appendChild(sbd);
        }
        if (amount == null)
        {
          Element sbd = document.createElement("adjustedAmount");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("adjustedAmount");
          sbd.appendChild(document.createTextNode(amount));
          rocdoc1.appendChild(sbd);
        }
        if (approvedBy == null)
        {
          Element sbd = document.createElement("approvalBy");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("approvalBy");
          sbd.appendChild(document.createTextNode(approvedBy));
          rocdoc1.appendChild(sbd);
        }
        if (letterNo == null)
        {
          Element sbd = document.createElement("letterNo");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("letterNo");
          sbd.appendChild(document.createTextNode(letterNo));
          rocdoc1.appendChild(sbd);
        }
        if (adjDate == null)
        {
          Element sbd = document.createElement("adjustmentDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("adjustmentDate");
          sbd.appendChild(document.createTextNode(adjDate));
          rocdoc1.appendChild(sbd);
        }
        if (reason == null)
        {
          Element sbd = document.createElement("reasonForAdjustment");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("reasonForAdjustment");
          sbd.appendChild(document.createTextNode(reason));
          rocdoc1.appendChild(sbd);
        }
        if (serialNo == null)
        {
          Element sbd = document.createElement("closureSequenceNo");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("closureSequenceNo");
          sbd.appendChild(document.createTextNode(serialNo));
          rocdoc1.appendChild(sbd);
        }
        if (recInd == null)
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(recInd));
          rocdoc1.appendChild(dn);
        }
        if (docNo == null)
        {
          Element sbd = document.createElement("docNumber");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("docNumber");
          sbd.appendChild(document.createTextNode(docNo));
          rocdoc1.appendChild(sbd);
        }
        if (docDate == null)
        {
          Element sbd = document.createElement("docDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("docDate");
          sbd.appendChild(document.createTextNode(docDate));
          rocdoc1.appendChild(sbd);
        }
        if (docPort == null)
        {
          Element sbd = document.createElement("docPort");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("docPort");
          sbd.appendChild(document.createTextNode(docPort));
          rocdoc1.appendChild(sbd);
        }
        if (remarks == null)
        {
          Element dn = document.createElement("remark");
          dn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("remark");
          dn.appendChild(document.createTextNode(remarks));
          rocdoc1.appendChild(dn);
        }
        int nobill = Integer.parseInt(noofshipbills);
        nobill++;
        noofshipbills = Integer.toString(nobill);
       
        String sq1 = "UPDATE ETT_IRM_CLOSURE_TBL SET CLOSURE_STATUS='Completed' WHERE MASTER_REFNO = '" +
          masterRefNo + "'" + " AND S_NO = '" + serialNo + "'";
       
        LoggableStatement pps1 = new LoggableStatement(con, sq1);
       
        logger.info(
          "----------EDPMS ------IRMCLDWLD--- [processCancelIRMCLjob]- UPDATE ETT_IRM_CLOSURE_TBL is Query-----------" +
          pps1.getQueryString());
       

        pps1.close();
       
        insertIRCLSCancelAudit(serialNo, masterRefNo, adCode, ieCode, currency, amount, approvedBy, letterNo,
          adjDate, reason, docNo, docDate, docPort, recInd, remarks, userName, irmclSeq);
        xmlCount++;
      }
      Element nob = document.createElement("noOfIrm");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,IRMADJCLOSUREXML_SEQNO.NEXTVAL AS IRCL_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("IRCL_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.CANCEL_IRCLS_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".ircls";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        logger.info("File has been successfully saved");
       
        updateAuditTrail("ETT_IRCLS_CANCEL_AUDIT_TABLE", fileName, fileLoc, "IRCLS_SEQ_NO", irmclSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------IRMCLDWLD--- [processCancelIRMCLjob]- Exception---------" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
  }
 
  public String processIRMCLjob()
  {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet res = null;
    CommonMethods commonMethods = null;
    String noofshipbills = "0";
    int xmlCount = 0;
    String result = "success";
    int irmclSeq = 0;
    boolean refNof = false;
    boolean iecodef = false;
    int filtercount = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userName = (String)session.getAttribute("loginedUserName");
      con = DBConnectionUtility.getConnection();
     
      commonMethods = new CommonMethods();
     
      irmclSeq = seqGeneration("IRCLS_SEQNO_GEN");
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("bank");
      document.appendChild(root);
     
      Element rocdoc = document.createElement("checkSum");
      root.appendChild(rocdoc);
     
      Element rocdoc2 = document.createElement("remittances");
      root.appendChild(rocdoc2);
     
      String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVED_BY,LETTER_NO,TO_CHAR(TO_DATE(ADJ_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS ADJ_DATE,REASON,DOC_NO,TO_CHAR(TO_DATE(DOC_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS DOC_DATE,DOC_PORT,REC_IND,CLOSE_IND,REMARKS FROM ETT_IRM_CLOSURE_TBL WHERE  STATUS = 'A' AND CLOSURE_STATUS = 'Pending' ";
      if ((this.vo.getBillRefNo() != null) && (this.vo.getBillRefNo().trim().length() > 0))
      {
        query = query + " AND TRIM(MASTER_REFNO) = ?";
        refNof = true;
      }
      if ((this.vo.getIecode() != null) && (this.vo.getIecode().trim().length() > 0))
      {
        query = query + " AND TRIM(IECODE) = ?";
        iecodef = true;
      }
      ps = new LoggableStatement(con, query);
      if (refNof) {
        ps.setString(++filtercount, this.vo.getBillRefNo().trim());
      }
      if (iecodef) {
        ps.setString(++filtercount, this.vo.getIecode().trim());
      }
      res = ps.executeQuery();
      while (res.next())
      {
        String serialNo = commonMethods.getEmptyIfNull(res.getString("S_NO")).trim();
        String masterRefNo = commonMethods.getEmptyIfNull(res.getString("MASTER_REFNO")).trim();
        String adCode = commonMethods.getEmptyIfNull(res.getString("ADCODE")).trim();
        String ieCode = commonMethods.getEmptyIfNull(res.getString("IECODE")).trim();
        String currency = commonMethods.getEmptyIfNull(res.getString("CURRENCY")).trim();
        String amount = commonMethods.getEmptyIfNull(res.getString("AMOUNT")).trim();
        String approvedBy = commonMethods.getEmptyIfNull(res.getString("APPROVED_BY")).trim();
        String letterNo = commonMethods.getEmptyIfNull(res.getString("LETTER_NO")).trim();
        String adjDate = commonMethods.getEmptyIfNull(res.getString("ADJ_DATE")).trim();
        String reason = commonMethods.getEmptyIfNull(res.getString("REASON")).trim();
        String docNo = commonMethods.getEmptyIfNull(res.getString("DOC_NO")).trim();
        String docDate = commonMethods.getEmptyIfNull(res.getString("DOC_DATE")).trim();
        String docPort = commonMethods.getEmptyIfNull(res.getString("DOC_PORT")).trim();
        String recInd = commonMethods.getEmptyIfNull(res.getString("REC_IND")).trim();
        String remarks = commonMethods.getEmptyIfNull(res.getString("REMARKS")).trim();
       
        Element rocdoc1 = document.createElement("remittance");
        rocdoc2.appendChild(rocdoc1);
        if (masterRefNo == null)
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("IRMNumber");
          pc.appendChild(document.createTextNode(masterRefNo));
          rocdoc1.appendChild(pc);
        }
        if (adCode == null)
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("adCode");
          sbn.appendChild(document.createTextNode(adCode));
          rocdoc1.appendChild(sbn);
        }
        if (ieCode == null)
        {
          Element sbd = document.createElement("ieCode");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("ieCode");
          sbd.appendChild(document.createTextNode(ieCode));
          rocdoc1.appendChild(sbd);
        }
        if (currency == null)
        {
          Element sbd = document.createElement("currency");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("currency");
          sbd.appendChild(document.createTextNode(currency));
          rocdoc1.appendChild(sbd);
        }
        if (amount == null)
        {
          Element sbd = document.createElement("adjustedAmount");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("adjustedAmount");
          sbd.appendChild(document.createTextNode(amount));
          rocdoc1.appendChild(sbd);
        }
        if (approvedBy == null)
        {
          Element sbd = document.createElement("approvalBy");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("approvalBy");
          sbd.appendChild(document.createTextNode(approvedBy));
          rocdoc1.appendChild(sbd);
        }
        if (letterNo == null)
        {
          Element sbd = document.createElement("letterNo");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("letterNo");
          sbd.appendChild(document.createTextNode(letterNo));
          rocdoc1.appendChild(sbd);
        }
        if (adjDate == null)
        {
          Element sbd = document.createElement("adjustmentDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("adjustmentDate");
          sbd.appendChild(document.createTextNode(adjDate));
          rocdoc1.appendChild(sbd);
        }
        if (reason == null)
        {
          Element sbd = document.createElement("reasonForAdjustment");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("reasonForAdjustment");
          sbd.appendChild(document.createTextNode(reason));
          rocdoc1.appendChild(sbd);
        }
        if (serialNo == null)
        {
          Element sbd = document.createElement("closureSequenceNo");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("closureSequenceNo");
          sbd.appendChild(document.createTextNode(serialNo));
          rocdoc1.appendChild(sbd);
        }
        if (recInd == null)
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("recordIndicator");
          dn.appendChild(document.createTextNode(recInd));
          rocdoc1.appendChild(dn);
        }
        if (docNo == null)
        {
          Element sbd = document.createElement("docNumber");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("docNumber");
          sbd.appendChild(document.createTextNode(docNo));
          rocdoc1.appendChild(sbd);
        }
        if (docDate == null)
        {
          Element sbd = document.createElement("docDate");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("docDate");
          sbd.appendChild(document.createTextNode(docDate));
          rocdoc1.appendChild(sbd);
        }
        if (docPort == null)
        {
          Element sbd = document.createElement("docPort");
          sbd.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("docPort");
          sbd.appendChild(document.createTextNode(docPort));
          rocdoc1.appendChild(sbd);
        }
        if (remarks == null)
        {
          Element dn = document.createElement("remark");
          dn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("remark");
          dn.appendChild(document.createTextNode(remarks));
          rocdoc1.appendChild(dn);
        }
        int nobill = Integer.parseInt(noofshipbills);
        nobill++;
        noofshipbills = Integer.toString(nobill);
       
        String sq1 = "UPDATE ETT_IRM_CLOSURE_TBL SET CLOSURE_STATUS='Completed' WHERE MASTER_REFNO = '" +
          masterRefNo + "'" + " AND S_NO = '" + serialNo + "'";
        PreparedStatement pps1 = con.prepareStatement(sq1);
       
        pps1.close();
       
        insertIRCLSAudit(serialNo, masterRefNo, adCode, ieCode, currency, amount, approvedBy, letterNo, adjDate,
          reason, docNo, docDate, docPort, recInd, remarks, userName, irmclSeq);
        xmlCount++;
      }
      Element nob = document.createElement("noOfIrm");
      nob.appendChild(document.createTextNode(noofshipbills));
      rocdoc.appendChild(nob);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
       
        String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,IRMADJCLOSUREXML_SEQNO.NEXTVAL AS IRCL_SEQNO FROM DLYPRCCYCL";
       
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        String prodate = null;
        int seq = 0;
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          seq = rs1.getInt("IRCL_SEQNO");
        }
        rs1.close();
        pps1.close();
       
        String fileLoc = "";
       

        fileLoc = ActionConstants.IRCLS_ONLINE;
       

        fileLoc = fileLoc + prodate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String fileName = "RBI" + prodate + seq + ".ircls";
        String myFilePath = fileLoc + fileName + ".xml";
       
        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
       
        updateAuditTrail("ETT_IRCLS_AUDIT_TABLE", fileName, fileLoc, "IRCLS_SEQ_NO", irmclSeq);
       
        downloadFiles(myFilePath);
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    return result;
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
 
  public boolean checkingData(Connection con, String billRef, String eventRef, String shipNo, String shipDate, String portCode)
  {
    if (con == null) {
      logger.info("----------EDPMS ------EBRCDWLD--- [checkingData]- Conneciton Null");
    }
    logger.info("----------EDPMS ------EBRCDWLD--- [checkingData]- billRef---" + billRef);
   
    logger.info("----------EDPMS ------EBRCDWLD--- [checkingData]- eventRef---" + eventRef);
    logger.info("----------EDPMS ------EBRCDWLD--- [checkingData]- shipNo---" + shipNo);
    logger.info("----------EDPMS ------EBRCDWLD--- [checkingData]- shipDate---" + shipDate);
    logger.info("----------EDPMS ------EBRCDWLD--- [checkingData]- portCode---" + portCode);
   
    LoggableStatement ps = null;
    ResultSet rs = null;
    boolean result = false;
    try
    {
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      String sq = "SELECT COUNT(1) FROM  ETT_EBRC_FILES  WHERE BILL_REFNO LIKE '%" + billRef.trim() +
        "%' AND EVENT_REFNO ='" + eventRef.trim() + "'" + " AND TO_DATE(SDATE, 'dd-mm-yy')='" +
        shipDate.trim() + "'" + " AND SNO ='" + shipNo.trim() + "'";
     
      ps = new LoggableStatement(con, sq);
     
      logger.info(
        "----------EDPMS ------EBRCDWLD--- [checkingData]- ETT_EBRC_FILES Query---" + ps.getQueryString());
      rs = ps.executeQuery();
      rs.next();
      if (rs.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception e)
    {
      logger.info("----------EDPMS ------EBRCDWLD--- [checkingData]- exception---" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, ps, rs);
    }
    return result;
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
 
  public void insertRODAudit(String shipbillno, String shipbilldate, String portcode, String record, String exporttype, String formno, String leodate, String iecode, String ciecode, String adcode, String adexport, String ddindicator, String adbillno, String dateneg, String buyername, String userName, int rodSeq)
  {
    Connection con = null;
    LoggableStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_ROD_AUDIT_TABLE (SHPBILLNO,SHPBILLDATE,PORTCODE,RECORD,EXPTYPE,FORMNO,LOEDATE,IECODE,CIECODE,ADCODE,ADEXPORT,DDINDICATOR,ADBILLNO,DATENEG,BUYERNAME,USERNAME,ROD_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,SYSTIMESTAMP)";
      pst = new LoggableStatement(con, insert_query);
      pst.setString(1, shipbillno);
      pst.setString(2, shipbilldate);
      pst.setString(3, portcode);
      pst.setString(4, record);
      pst.setString(5, exporttype);
      pst.setString(6, formno);
      pst.setString(7, leodate);
      pst.setString(8, iecode);
      pst.setString(9, ciecode);
      pst.setString(10, adcode);
      pst.setString(11, adexport);
      pst.setString(12, ddindicator);
      pst.setString(13, adbillno);
      pst.setString(14, dateneg);
      pst.setString(15, buyername);
      pst.setString(16, userName);
      pst.setInt(17, rodSeq);
      logger.info("Insert Query for IRM Audit Trial---->" + pst.getQueryString());
      pst.executeUpdate();
     
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertIRMAudit(String transRefNo, String masterRefNo, String irmRecAmt, String irmRecDate, String adCode, String fircFlag, String fircNo, String fircDate, String fircAmt, String irmRecCurr, String ieCode, String ieName, String remName, String remAddr, String remCountry, String remBankName, String remBankCountry, String swiftRefNo, String purposeCode, String recInd, String remarks, String userName, int irmSeq)
  {
    Connection con = null;
    LoggableStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_IRM_AUDIT_TABLE (TRXN_REFNO,MASTER_REFNO,REM_AMOUNT,REM_DATE,ADCODE,FIRC_FLAG,FIRC_NO,FIRC_DATE,FIRCAMOUNT,CURRENCY,IECODE,IENAME,REM_NAME,REM_ADDR,REM_COUNTRY,REM_BANKNAME,REM_BANKCOUNTRY,SWIFT_REFNO,PURPOSECODE,REC_IND,REMARKS,USERNAME,IRM_SEQ_NO,CREATE_DATE) VALUES (?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = new LoggableStatement(con, insert_query);
      pst.setString(1, transRefNo);
      pst.setString(2, masterRefNo);
      pst.setString(3, irmRecAmt);
      pst.setString(4, irmRecDate);
      pst.setString(5, adCode);
      pst.setString(6, fircFlag);
      pst.setString(7, fircNo);
      pst.setString(8, fircDate);
      pst.setString(9, fircAmt);
      pst.setString(10, irmRecCurr);
      pst.setString(11, ieCode);
      pst.setString(12, ieName);
      pst.setString(13, remName);
      pst.setString(14, remAddr);
      pst.setString(15, remCountry);
      pst.setString(16, remBankName);
      pst.setString(17, remBankCountry);
      pst.setString(18, swiftRefNo);
      pst.setString(19, purposeCode);
      pst.setString(20, recInd);
      pst.setString(21, remarks);
      pst.setString(22, userName);
      pst.setInt(23, irmSeq);
      logger.info("Insert Query for IRM Audit Trial---->" + pst.getQueryString());
      pst.executeUpdate();
     
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertPRNAudit(String shipbillno, String shipbilldate, String portcode, String formno, String leodate, String adcode, String exporttype, String record, String billRefNo, String eventRefNo, String payRefNo, String inwardNo, String fircNo, String remAdcode, String payParty, String realCurr, String realDate, String ebrcNo, String invserialno, String invno, String invdate, String accno, String bankingChargeAmt, String fobamt, String fobcurcode, String friamt, String fricurcode, String insamt, String inscurcode, String remname, String userName, int prnSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_PRN_AUDIT_TABLE (SHPBILLNO,SHPBILLDATE,PORTCODE,FORMNO,LOEDATE,ADCODE,EXPTYPERECORD,BILLREFNO,EVENTREFNO,PAYREFNO,INWARDNO,FIRCNO,REMADCODE,PAYPARTY,REALCURR,REALDATE,EBRCNO,INVSERNO,INVNO,INVDATE,ACCNO,BANKCHGAMT,FOBAMT,FOBCURCODE,FRIAMT,FRICURCODE,INSAMT,INSCURCODE,RENAME,USERNAME,PRN_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, shipbillno);
      pst.setString(2, shipbilldate);
      pst.setString(3, portcode);
      pst.setString(4, formno);
      pst.setString(5, leodate);
      pst.setString(6, adcode);
      pst.setString(7, exporttype);
      pst.setString(8, record);
      pst.setString(9, billRefNo);
      pst.setString(10, eventRefNo);
      pst.setString(11, payRefNo);
      pst.setString(12, inwardNo);
      pst.setString(13, fircNo);
      pst.setString(14, remAdcode);
      pst.setString(15, payParty);
      pst.setString(16, realCurr);
      pst.setString(17, realDate);
      pst.setString(18, ebrcNo);
      pst.setString(19, invserialno);
      pst.setString(20, invno);
      pst.setString(21, invdate);
      pst.setString(22, accno);
      pst.setString(23, bankingChargeAmt);
      pst.setString(24, fobamt);
      pst.setString(25, fobcurcode);
      pst.setString(26, friamt);
      pst.setString(27, fricurcode);
      pst.setString(28, insamt);
      pst.setString(29, inscurcode);
      pst.setString(30, remname);
      pst.setString(31, userName);
      pst.setInt(32, prnSeq);
      pst.executeUpdate();
     
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertPRNCancelAudit(String grNumber, String shipbillno, String shipbilldate, String portcode, String formno, String leodate, String adcode, String exporttype, String record, String inwardNo, String fircNo, String remAdcode, String payParty, String realCurr, String realDate, String ebrcNo, String invserialno, String invno, String invdate, String accno, String bankingChargeAmt, String fobamt, String fobcurcode, String friamt, String fricurcode, String insamt, String inscurcode, String remname, String userName, int prnSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_PRN_CANCEL_AUDIT_TABLE (SHPBILLNO,SHPBILLDATE,PORTCODE,FORMNO,LOEDATE,ADCODE,EXPTYPE,RECORD,INWARDNO,FIRCNO,REMADCODE,PAYPARTY,REALCURR,REALDATE,EBRCNO,INVSERNO,INVNO,INVDATE,ACCNO,BANKCHGAMT,FOBAMT,FOBCURCODE,FRIAMT,FRICURCODE,INSAMT,INSCURCODE,RENAME,USERNAME,PRN_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, grNumber);
      pst.setString(2, shipbillno);
      pst.setString(2, shipbilldate);
      pst.setString(3, portcode);
      pst.setString(4, formno);
      pst.setString(5, leodate);
      pst.setString(6, adcode);
      pst.setString(7, exporttype);
      pst.setString(8, record);
      pst.setString(9, inwardNo);
      pst.setString(10, fircNo);
      pst.setString(11, remAdcode);
      pst.setString(12, payParty);
      pst.setString(13, realCurr);
      pst.setString(14, realDate);
      pst.setString(15, ebrcNo);
      pst.setString(16, invserialno);
      pst.setString(17, invno);
      pst.setString(18, invdate);
      pst.setString(19, accno);
      pst.setString(20, bankingChargeAmt);
      pst.setString(21, fobamt);
      pst.setString(22, fobcurcode);
      pst.setString(23, friamt);
      pst.setString(24, fricurcode);
      pst.setString(25, insamt);
      pst.setString(26, inscurcode);
      pst.setString(27, remname);
      pst.setString(28, userName);
      pst.setInt(29, prnSeq);
      pst.executeUpdate();
     
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertPRNAmendAudit(String grNumber, String shipbillno, String shipbilldate, String portcode, String formno, String leodate, String adcode, String exporttype, String record, String inwardNo, String fircNo, String remAdcode, String payParty, String realCurr, String realDate, String ebrcNo, String invserialno, String invno, String invdate, String accno, String bankingChargeAmt, String fobamt, String fobcurcode, String friamt, String fricurcode, String insamt, String inscurcode, String remname, String userName, int prnSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_PRN_AMEND_AUDIT_TABLE (GRNO,SHPBILLNO,SHIPBILLDATE,PORTCODE,FORMNO,LOEDATE,ADCODE,EXPTYPE,RECORD,INWARDNO,FIRCNO,REMADCODE,PAYPARTY,REALCURR,REALDATE,EBRCNO,INVSERNO,INVNO,INVDATE,ACCNO,BANKCHGAMT,FOBAMT,FOBCURCODE,FRIAMT,FRICURCODE,INSAMT,INSCURCODE,RENAME,USERNAME,PRN_SEQ_NO,CREATE_DATE) VALUES (?,?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, grNumber);
      pst.setString(2, shipbillno);
      pst.setString(3, shipbilldate);
      pst.setString(4, portcode);
      pst.setString(5, formno);
      pst.setString(6, leodate);
      pst.setString(7, adcode);
      pst.setString(8, exporttype);
      pst.setString(9, record);
      pst.setString(10, inwardNo);
      pst.setString(11, fircNo);
      pst.setString(12, remAdcode);
      pst.setString(13, payParty);
      pst.setString(14, realCurr);
      pst.setString(15, realDate);
      pst.setString(16, ebrcNo);
      pst.setString(17, invserialno);
      pst.setString(18, invno);
      pst.setString(19, invdate);
      pst.setString(20, accno);
      pst.setString(21, bankingChargeAmt);
      pst.setString(22, fobamt);
      pst.setString(23, fobcurcode);
      pst.setString(24, friamt);
      pst.setString(25, fricurcode);
      pst.setString(26, insamt);
      pst.setString(27, inscurcode);
      pst.setString(28, remname);
      pst.setString(29, userName);
      pst.setInt(30, prnSeq);
      pst.executeUpdate();
     
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertWSN1Audit(String shipbillno, String shipbilldate, String portcode, String formno, String leodate, String adcode, String exporttype, String record, String billRefNo, String payRefNo, String writeOffSeqNo, String shpInd, String boeNo, String boeDate, String portDischarge, String invserialno, String invno, String invdate, String writeind, String writeamt, String writedate, String closebill, String userName, int wsnSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_WSN1_AUDIT_TABLE (SHPBILLNO,SHIPBILLDATE,PORTCODE,FORMNO,LOEDATE,ADCODE,EXPTYPE,RECORD,BILLREFNO,PAYREFNO,WRITEOFFSEQNO,SHPIND,BOENO,BOEDATE,PORTDISCHG,INVSERNO,INVNO,INVDATE,WRITEIND,WRITEAMT,WRITEDATE,CLOSEBILL,USERNAME,WSN_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, shipbillno);
      pst.setString(2, shipbilldate);
      pst.setString(3, portcode);
      pst.setString(4, formno);
      pst.setString(5, leodate);
      pst.setString(6, adcode);
      pst.setString(7, exporttype);
      pst.setString(8, record);
      pst.setString(9, billRefNo);
      pst.setString(10, payRefNo);
      pst.setString(11, writeOffSeqNo);
      pst.setString(12, shpInd);
      pst.setString(13, boeNo);
      pst.setString(14, boeDate);
      pst.setString(15, portDischarge);
      pst.setString(16, invserialno);
      pst.setString(17, invno);
      pst.setString(18, invdate);
      pst.setString(19, writeind);
      pst.setString(20, writeamt);
      pst.setString(21, writedate);
      pst.setString(22, closebill);
      pst.setString(23, userName);
      pst.setInt(24, wsnSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertWSN2Audit(String shipbillno, String shipbilldate, String portcode, String formno, String leodate, String adcode, String exporttype, String record, String billRefNo, String payRefNo, String writeOffSeqNo, String shpInd, String boeNo, String boeDate, String portDischarge, String invserialno, String invno, String invdate, String writeind, String writeamt, String writedate, String closebill, String userName, int wsnSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_WSN2_AUDIT_TABLE (SHPBILLNO,SHIPBILLDATE,PORTCODE,FORMNO,LOEDATE,ADCODE,EXPTYPE,RECORD,BILLREFNO,PAYREFNO,WRITEOFFSEQNO,SHPIND,BOENO,BOEDATE,PORTDISCHG,INVSERNO,INVNO,INVDATE,WRITEIND,WRITEAMT,WRITEDATE,CLOSEBILL,USERNAME,WSN_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, shipbillno);
      pst.setString(2, shipbilldate);
      pst.setString(3, portcode);
      pst.setString(4, formno);
      pst.setString(5, leodate);
      pst.setString(6, adcode);
      pst.setString(7, exporttype);
      pst.setString(8, record);
      pst.setString(9, billRefNo);
      pst.setString(10, payRefNo);
      pst.setString(11, writeOffSeqNo);
      pst.setString(12, shpInd);
      pst.setString(13, boeNo);
      pst.setString(14, boeDate);
      pst.setString(15, portDischarge);
      pst.setString(16, invserialno);
      pst.setString(17, invno);
      pst.setString(18, invdate);
      pst.setString(19, writeind);
      pst.setString(20, writeamt);
      pst.setString(21, writedate);
      pst.setString(22, closebill);
      pst.setString(23, userName);
      pst.setInt(24, wsnSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertPENAudit(String shipbillno, String shipbilldate, String portcode, String formno, String leodate, String iecode, String adcode, String letterno, String letterdate, String exporttype, String record, String ind, String inddate, String userName, int penSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_PEN_AUDIT_TABLE (SHPBILLNO,SHIPBILLDATE,PORTCODE,FORMNO,LOEDATE,IECODE,ADCODE,LETTERNO,LETTERDATE,EXPTYPE,RECORD,IND,INDDATE,USERNAME,PEN_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, shipbillno);
      pst.setString(2, shipbilldate);
      pst.setString(3, portcode);
      pst.setString(4, formno);
      pst.setString(5, leodate);
      pst.setString(6, iecode);
      pst.setString(7, adcode);
      pst.setString(8, letterno);
      pst.setString(9, letterdate);
      pst.setString(10, exporttype);
      pst.setString(11, record);
      pst.setString(12, ind);
      pst.setString(13, inddate);
      pst.setString(14, userName);
      pst.setInt(15, penSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertTRRAudit(String shipbillno, String shipbilldate, String portcode, String iecode, String formno, String exporttype, String exportagency, String newadcode, String exadcode, String userName, int trrSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_TRR_AUDIT_TABLE (SHPBILLNO,SHIPBILLDATE,PORTCODE,IECODE,FORMNO,EXPTYPE,EXPAGNCY,NEWADCODE,EXADCODE,USERNAME,TRR_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, shipbillno);
      pst.setString(2, shipbilldate);
      pst.setString(3, portcode);
      pst.setString(4, iecode);
      pst.setString(5, formno);
      pst.setString(6, exporttype);
      pst.setString(7, exportagency);
      pst.setString(8, newadcode);
      pst.setString(9, exadcode);
      pst.setString(10, userName);
      pst.setInt(11, trrSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertEBRCCancelAudit(String brcNo, String brcDate, String status, String ieCode, String exportName, String codeIFSC, String billID, String shipNo, String shipPort, String shipDate, String shipCurr, String shipValue, String realCurr, String realValue, String realDate, String userName, int ebrcSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_EBRC_CANCEL_AUDIT_TABLE (BRCNO,BRCDATE,STATUS,IECODE,EXPNAME,CODEIFSC,BILLID,SHIPNO,SHIPPORT,SHIPDATE,SHIPCURR,SHIPVALUE,REALCURR,REALVALUE,REALDATE,USERNAME,EBRC_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, brcNo);
      pst.setString(2, brcDate);
      pst.setString(3, status);
      pst.setString(4, ieCode);
      pst.setString(5, exportName);
      pst.setString(6, codeIFSC);
      pst.setString(7, billID);
      pst.setString(8, shipNo);
      pst.setString(9, shipPort);
      pst.setString(10, shipDate);
      pst.setString(11, shipCurr);
      pst.setString(9, shipValue);
      pst.setString(10, realCurr);
      pst.setString(11, realValue);
      pst.setString(11, realDate);
      pst.setString(12, userName);
      pst.setInt(13, ebrcSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertEBRCAudit(String brcNo, String brcDate, String status, String ieCode, String exportName, String codeIFSC, String billID, String shipNo, String shipPort, String shipDate, String shipCurr, String shipValue, String realCurr, String realValue, String realDate, String userName, int ebrcSeq)
  {
    Connection con = null;
    LoggableStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_EBRC_AUDIT_TABLE (BRCNO,BRCDATE,STATUS,IECODE,EXPNAME,CODEIFSC,BILLID,SHIPNO,SHIPPORT,SHIPDATE,SHIPCURR,SHIPVALUE,REALCURR,REALVALUE,REALDATE,USERNAME,EBRC_SEQ_NO,CREATE_DATE) VALUES (?,TO_DATE(?,'YYYY-MM-DD'),?,?,?,?,?,?,?,TO_DATE(?,'YYYY-MM-DD'),?,?,?,?,TO_DATE(?,'YYYY-MM-DD'),?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = new LoggableStatement(con, insert_query);
      pst.setString(1, brcNo);
      pst.setString(2, brcDate);
      pst.setString(3, status);
      pst.setString(4, ieCode);
      pst.setString(5, exportName);
      pst.setString(6, codeIFSC);
      pst.setString(7, billID);
      pst.setString(8, shipNo);
      pst.setString(9, shipPort);
      pst.setString(10, shipDate);
      pst.setString(11, shipCurr);
      pst.setString(12, shipValue);
      pst.setString(13, realCurr);
      pst.setString(14, realValue);
      pst.setString(15, realDate);
      pst.setString(16, userName);
      pst.setInt(17, ebrcSeq);
      logger.info("Insert Query for IRM Audit Trial---->" + pst.getQueryString());
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertIRFIRCAudit(String masterRefNo, String adCode, String fircFlag, String fircNo, String fircDate, String fircAmt, String recInd, String userName, int irfircSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_IRFIRC_AUDIT_TABLE (MASTERREFNO,ADCODE,FIRCFLAG,FIRCNO,FIRCDATE,FIRCAMT,RECIND,USERNAME,IRFIRC_SEQ_NO,CREATE_DATE) VALUES (?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, masterRefNo);
      pst.setString(2, adCode);
      pst.setString(3, fircFlag);
      pst.setString(4, fircNo);
      pst.setString(5, fircDate);
      pst.setString(6, fircAmt);
      pst.setString(7, recInd);
      pst.setString(8, userName);
      pst.setInt(9, irfircSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertIREXTAudit(String serialNo, String masterRefNo, String adCode, String ieCode, String extInd, String extDate, String letterNo, String letterDate, String recInd, String reason, String userName, int irextSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_IREXT_AUDIT_TABLE (SERIALNO,MASTERREFNO,ADCODE,IECODE,EXTIND,EXTDATE,LETTERNO,LETTERDATE,RECIND,REASON,USERNAME,IREXT_SEQ_NO,CREATE_DATE) VALUES (?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, serialNo);
      pst.setString(2, masterRefNo);
      pst.setString(3, adCode);
      pst.setString(4, ieCode);
      pst.setString(5, extInd);
      pst.setString(6, extDate);
      pst.setString(7, letterNo);
      pst.setString(5, letterDate);
      pst.setString(6, recInd);
      pst.setString(7, reason);
      pst.setString(8, userName);
      pst.setInt(9, irextSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertIRCLSCancelAudit(String serialNo, String masterRefNo, String adCode, String ieCode, String currency, String amount, String approvedBy, String letterNo, String adjDate, String reason, String docNo, String docDate, String docPort, String recInd, String remarks, String userName, int irmclSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_IRCLS_CANCEL_AUDIT_TABLE (SERIALNO,MASTERREFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVEDBY,LETTERNO,ADJDATE,REASON,DOCNO,DOCDATE,DOCPORT,RECIND,REMARKS,USERNAME,IRCLS_SEQ_NO,CREATE_DATE) VALUES (?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, serialNo);
      pst.setString(2, masterRefNo);
      pst.setString(3, adCode);
      pst.setString(4, ieCode);
      pst.setString(5, currency);
      pst.setString(6, amount);
      pst.setString(7, approvedBy);
      pst.setString(5, letterNo);
      pst.setString(6, adjDate);
      pst.setString(7, reason);
      pst.setString(6, docNo);
      pst.setString(7, docDate);
      pst.setString(5, docPort);
      pst.setString(6, recInd);
      pst.setString(7, remarks);
      pst.setString(8, userName);
      pst.setInt(9, irmclSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public void insertIRCLSAudit(String serialNo, String masterRefNo, String adCode, String ieCode, String currency, String amount, String approvedBy, String letterNo, String adjDate, String reason, String docNo, String docDate, String docPort, String recInd, String remarks, String userName, int irmclSeq)
  {
    Connection con = null;
    PreparedStatement pst = null;
    String insert_query = null;
    try
    {
      logger.info("Entering insertAuditTrialTable Method");
      con = DBConnectionUtility.getConnection();
      insert_query = "INSERT INTO ETT_IRCLS_AUDIT_TABLE (SERIALNO,MASTERREFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVEDBY,LETTERNO,ADJDATE,REASON,DOCNO,DOCDATE,DOCPORT,RECIND,REMARKS,USERNAME,IRCLS_SEQ_NO,CREATE_DATE) VALUES (?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,SYSTIMESTAMP)";
      logger.info("Insert Query for IRM Audit Trial---->" + insert_query);
      pst = con.prepareStatement(insert_query);
      pst.setString(1, serialNo);
      pst.setString(2, masterRefNo);
      pst.setString(3, adCode);
      pst.setString(4, ieCode);
      pst.setString(5, currency);
      pst.setString(6, amount);
      pst.setString(7, approvedBy);
      pst.setString(5, letterNo);
      pst.setString(6, adjDate);
      pst.setString(7, reason);
      pst.setString(6, docNo);
      pst.setString(7, docDate);
      pst.setString(5, docPort);
      pst.setString(6, recInd);
      pst.setString(7, remarks);
      pst.setString(8, userName);
      pst.setInt(9, irmclSeq);
      pst.executeUpdate();
      logger.info("Exiting insertAuditTrialTable Method");
    }
    catch (Exception e)
    {
      logger.info("Exception in InsertAuditTrialTable" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
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
}