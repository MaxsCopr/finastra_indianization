package in.co.ebrc.dao.customername;

import com.opensymphony.xwork2.ActionContext;
import com.sun.jmx.snmp.Timestamp;
import in.co.ebrc.dao.AbstractDAO;
import in.co.ebrc.dao.exception.DAOException;
import in.co.ebrc.utility.ActionConstants;
import in.co.ebrc.utility.ActionConstantsQuery;
import in.co.ebrc.utility.CommonMethods;
import in.co.ebrc.utility.DBConnectionUtility;
import in.co.ebrc.utility.LoggableStatement;
import in.co.ebrc.vo.EbrcDataProcessVO;
import in.co.ebrc.vo.EbrcProcessVO;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
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

public class EbrcDataProcessDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static EbrcDataProcessDAO dao;
  private static Logger logger = Logger.getLogger(EbrcDataProcessDAO.class.getName());
 
  public static EbrcDataProcessDAO getDAO()
  {
    if (dao == null) {
      dao = new EbrcDataProcessDAO();
    }
    return dao;
  }
 
  ArrayList<EbrcProcessVO> list = null;
  ArrayList<EbrcProcessVO> list1 = null;
  EbrcProcessVO ebrcVO;
  EbrcDataProcessVO ebrcDataVO;
 
  public EbrcProcessVO getEbrcVO()
  {
    return this.ebrcVO;
  }
 
  public void setEbrcVO(EbrcProcessVO ebrcVO)
  {
    this.ebrcVO = ebrcVO;
  }
 
  public EbrcDataProcessVO getEbrcDataVO()
  {
    return this.ebrcDataVO;
  }
 
  public void setEbrcDataVO(EbrcDataProcessVO ebrcDataVO)
  {
    this.ebrcDataVO = ebrcDataVO;
  }
 
  public EbrcProcessVO getEbrcDetails(EbrcProcessVO ebrcVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    EbrcProcessVO ebrcData = null;
    CommonMethods commonMethods = null;
    this.list = new ArrayList();
    commonMethods = new CommonMethods();
    String brcNum = commonMethods.getEmptyIfNull(ebrcVO.getBrcNumber()).trim();
    String brcStatus = commonMethods.getEmptyIfNull(ebrcVO.getEbrcStatus()).trim();
    String importCode = commonMethods.getEmptyIfNull(ebrcVO.getEbrcIECode()).trim();
    String billRef = commonMethods.getEmptyIfNull(ebrcVO.getBillRefNumber()).trim();
    String eventRef = commonMethods.getEmptyIfNull(ebrcVO.getEventRefNumber()).trim();
    String fromDate = commonMethods.getEmptyIfNull(ebrcVO.getFromDate()).trim();
    String tobrcDate = commonMethods.getEmptyIfNull(ebrcVO.getToDate()).trim();
   
    int setvalue = 0;
    boolean checkbillRef = false;
    boolean checkbrcNum = false;
    boolean checkeventRef = false;
    boolean checkimportCode = false;
    boolean checkfromDate = false;
    boolean checktobrcDate = false;
    boolean checkbrcStatus = false;
    try
    {
      con = DBConnectionUtility.getConnection();
      logger.info("before query---");
      sqlQuery = "select NVL(TRIM(BILL_REFNO), ' ') AS BILL_REFNO,NVL(TRIM(BRCNO), ' ') AS BRCNO,  NVL(TRIM(EVENT_REFNO), ' ') AS EVENT_REFNO,TO_CHAR(TO_DATE(BRCDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS BRCDATE,  NVL(TRIM(STATUS), ' ') AS STATUS, NVL(TRIM(IEC), ' ') AS IEC, NVL(TRIM(EXPNAME), ' ') AS EXPNAME,  NVL(TRIM(IFSC), ' ') AS IFSC, NVL(TRIM(BILLID), ' ') AS BILLID, NVL(TRIM(SNO), ' ') AS SNO, NVL(TRIM(SPORT), ' ') AS SPORT, TO_CHAR(TO_DATE(SDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS SDATE, NVL(TRIM(SCC), ' ') AS SCC, NVL(TRIM(SVALUE), ' ') AS SVALUE, NVL(TRIM(RCC), ' ') AS RCC, NVL(TRIM(RVALUE), ' ') AS RVALUE, TO_CHAR(TO_DATE(RDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS RDATE,  NVL(TRIM(RVALUEINR), ' ') AS RVALUEINR, NVL(TRIM(RMTBANK), ' ') AS RMTBANK, NVL(TRIM(RMTCITY), ' ') AS RMTCITY, NVL(TRIM(RMTCTRY), ' ') AS RMTCTRY, NVL(TRIM(FILENAME), ' ') AS FILENAME, NVL(TRIM(FILE_ERRCODE), ' ') AS FILE_ERRCODE,  NVL(TRIM(BRC_ERRORCODE), ' ') AS BRC_ERRORCODE FROM ETT_EBRC_FILES WHERE BRCNO = BRCNO ";
     








      logger.info(" query---" + sqlQuery);
      if (!CommonMethods.isNullValue(billRef))
      {
        sqlQuery = sqlQuery + " AND BILL_REFNO =? ";
        checkbillRef = true;
      }
      if (!CommonMethods.isNullValue(brcNum))
      {
        sqlQuery = sqlQuery + " AND BRCNO =?";
        checkbrcNum = true;
      }
      if (!brcStatus.equalsIgnoreCase("-1")) {
        if (brcStatus.equalsIgnoreCase("E"))
        {
          sqlQuery = sqlQuery + " AND STATUS NOT IN ('F','C','Downloaded','000','CU','CD','CS')";
        }
        else
        {
          sqlQuery = sqlQuery + " AND STATUS =?";
          checkbrcStatus = true;
        }
      }
      if (!CommonMethods.isNullValue(eventRef))
      {
        sqlQuery = sqlQuery + " AND EVENT_REFNO= ? ";
        checkeventRef = true;
      }
      if (!CommonMethods.isNullValue(importCode))
      {
        sqlQuery = sqlQuery + " AND IEC=? ";
        checkimportCode = true;
      }
      if ((!commonMethods.isNull(fromDate)) && (!commonMethods.isNull(tobrcDate)))
      {
        sqlQuery = sqlQuery + " AND TO_DATE(BRCDATE,'DD-MM-YY') BETWEEN TO_DATE(?,'YYYY-MM-DD')  AND TO_DATE( ? ,'YYYY-MM-DD')";
       
        checkfromDate = true;
        checktobrcDate = true;
      }
      else
      {
        if (!commonMethods.isNull(fromDate))
        {
          sqlQuery = sqlQuery + " AND TO_DATE(BRCDATE,'DD-MM-YY') >= TO_DATE( ? ,'YYYY-MM-DD')";
          checkfromDate = true;
        }
        if (!commonMethods.isNull(tobrcDate))
        {
          sqlQuery = sqlQuery + " AND TO_DATE(BRCDATE,'DD-MM-YY') <= TO_DATE( ? ,'YYYY-MM-DD')";
          checktobrcDate = true;
        }
      }
      logger.info("query" + sqlQuery);
      ppt = new LoggableStatement(con, sqlQuery);
      if (checkbillRef)
      {
        logger.info("------Billref-----------" + billRef.trim());
        ppt.setString(++setvalue, billRef.trim());
      }
      if (checkbrcNum) {
        ppt.setString(++setvalue, brcNum.trim());
      }
      if (checkbrcStatus) {
        ppt.setString(++setvalue, brcStatus);
      }
      if (checkeventRef) {
        ppt.setString(++setvalue, eventRef.trim());
      }
      if (checkimportCode) {
        ppt.setString(++setvalue, importCode.trim());
      }
      if (checkfromDate) {
        ppt.setString(++setvalue, fromDate.trim());
      }
      if (checktobrcDate) {
        ppt.setString(++setvalue, tobrcDate);
      }
      logger.info("Get EBRC Values from DB " + ppt.getQueryString());
     
      rs = ppt.executeQuery();
      logger.info("rs---" + rs.getRow());
      logger.info("before while");
      while (rs.next())
      {
        logger.info("inside while");
        ebrcData = new EbrcProcessVO();
        ebrcData.setBillRefNumber(commonMethods.getEmptyIfNull(rs.getString("BILL_REFNO")).trim());
        ebrcData.setEventRefNumber(commonMethods.getEmptyIfNull(rs.getString("EVENT_REFNO")).trim());
        ebrcData.setBrcNo(commonMethods.getEmptyIfNull(rs.getString("BRCNO")).trim());
        ebrcData.setBrcDate(commonMethods.getEmptyIfNull(rs.getString("BRCDATE")).trim());
        String sta = commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim();
        if (sta.equalsIgnoreCase("000")) {
          sta = "Succeeded";
        } else if (sta.equalsIgnoreCase("F")) {
          sta = "Fresh";
        } else if (sta.equalsIgnoreCase("C")) {
          sta = "Cancelled";
        } else if (sta.equalsIgnoreCase("Downloaded")) {
          sta = "Downloaded";
        } else if (sta.equalsIgnoreCase("CU")) {
          sta = "Reject";
        } else if (sta.equalsIgnoreCase("CD")) {
          sta = "Cancel Downloaded";
        } else if (sta.equalsIgnoreCase("CS")) {
          sta = "Cancel Succeeded";
        } else {
          sta = "Error";
        }
        ebrcData.setStatus(sta);
        ebrcData.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IEC")).trim());
        ebrcData.setExportName(commonMethods.getEmptyIfNull(rs.getString("EXPNAME")).trim());
        ebrcData.setCodeIFSC(commonMethods.getEmptyIfNull(rs.getString("IFSC")).trim());
        ebrcData.setBillID(commonMethods.getEmptyIfNull(rs.getString("BILLID")).trim());
        ebrcData.setShipNo(commonMethods.getEmptyIfNull(rs.getString("SNO")).trim());
        ebrcData.setShipPort(commonMethods.getEmptyIfNull(rs.getString("SPORT")).trim());
        ebrcData.setShipDate(commonMethods.getEmptyIfNull(rs.getString("SDATE")).trim());
        ebrcData.setShipCurr(commonMethods.getEmptyIfNull(rs.getString("SCC")).trim());
        ebrcData.setShipValue(commonMethods.getEmptyIfNull(rs.getString("SVALUE")).trim());
        ebrcData.setRealCurr(commonMethods.getEmptyIfNull(rs.getString("RCC")).trim());
        ebrcData.setRealValue(commonMethods.getEmptyIfNull(rs.getString("RVALUE")).trim());
        ebrcData.setRealDate(commonMethods.getEmptyIfNull(rs.getString("RDATE")).trim());
        ebrcData.setTotalRealValue(commonMethods.getEmptyIfNull(rs.getString("RVALUEINR")).trim());
        ebrcData.setRmtBank(commonMethods.getEmptyIfNull(rs.getString("RMTBANK")).trim());
        ebrcData.setRmtCity(commonMethods.getEmptyIfNull(rs.getString("RMTCITY")).trim());
        ebrcData.setRmtCountry(commonMethods.getEmptyIfNull(rs.getString("RMTCTRY")).trim());
        this.list.add(ebrcData);
      }
      if (!this.list.isEmpty()) {
        ebrcVO.setList(this.list);
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return ebrcVO;
  }
 
  public EbrcProcessVO getGridDetails(EbrcProcessVO ebrcVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    this.list1 = new ArrayList();
    String brcNo = ebrcVO.getBrcNo1();
    if (brcNo != null) {
      brcNo = brcNo.trim();
    }
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      sqlQuery = "select NVL(TRIM(BRCNO), ' ') AS BRCNO, TO_CHAR(TO_DATE(BRCDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS BRCDATE, NVL(TRIM(STATUS), ' ') AS STATUS, NVL(TRIM(IEC), ' ') AS IEC, NVL(TRIM(EXPNAME), ' ') AS EXPNAME, NVL(TRIM(IFSC), ' ') AS IFSC, NVL(TRIM(BILLID), ' ') AS BILLID, NVL(TRIM(SNO), ' ') AS SNO, NVL(TRIM(SPORT), ' ') AS SPORT, TO_CHAR(TO_DATE(SDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS SDATE, NVL(TRIM(SCC), ' ') AS SCC, NVL(TRIM(SVALUE), ' ') AS SVALUE, NVL(TRIM(RCC), ' ') AS RCC, NVL(TRIM(RVALUE), ' ') AS RVALUE, TO_CHAR(TO_DATE(RDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS RDATE, NVL(TRIM(RVALUEINR), ' ') AS RVALUEINR, NVL(TRIM(RMTBANK), ' ') AS RMTBANK, NVL(TRIM(RMTCITY), ' ') AS RMTCITY, NVL(TRIM(RMTCTRY), ' ') AS RMTCTRY, NVL(TRIM(FILENAME), ' ') AS FILENAME, NVL(TRIM(FILE_ERRCODE), ' ') AS FILE_ERRCODE, NVL(TRIM(BRC_ERRORCODE), ' ') AS BRC_ERRORCODE,NVL(TRIM(BILL_REFNO), ' ') AS BILL_REF,NVL(TRIM(EVENT_REFNO), ' ') AS EVENT_REF FROM ETT_EBRC_FILES WHERE BRCNO=?";
      con = DBConnectionUtility.getConnection();
     
      ppt = new LoggableStatement(con, sqlQuery);
      ppt.setString(1, brcNo);
      logger.info("Get Grid Values " + ppt.getQueryString());
      rs = ppt.executeQuery();
      if (rs != null)
      {
        while (rs.next())
        {
          ebrcVO = new EbrcProcessVO();
          ebrcVO.setBrcNo(commonMethods.getEmptyIfNull(rs.getString("BRCNO")).trim());
          ebrcVO.setBrcDate(commonMethods.getEmptyIfNull(rs.getString("BRCDATE")).trim());
          String sta = commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim();
          if (sta.equalsIgnoreCase("000"))
          {
            sta = "000";
          }
          else if (sta.equalsIgnoreCase("F"))
          {
            sta = "F";
          }
          else if (sta.equalsIgnoreCase("C"))
          {
            sta = "C";
          }
          else if (sta.equalsIgnoreCase("Downloaded"))
          {
            sta = "Downloaded";
          }
          else if (sta.equalsIgnoreCase("CU"))
          {
            sta = "CU";
          }
          else if (sta.equalsIgnoreCase("CD"))
          {
            sta = "CD";
          }
          else if (sta.equalsIgnoreCase("CS"))
          {
            sta = "CS";
          }
          else
          {
            if (sta != null)
            {
              String errorDesc = getErrorDesc(sta);
              ebrcVO.setErrorDesc(commonMethods.getEmptyIfNull(errorDesc).trim());
            }
            sta = "E";
          }
          HttpSession session = ServletActionContext.getRequest()
            .getSession();
          if (sta == "000") {
            session.setAttribute("count", "0");
          } else {
            session.setAttribute("count", "1");
          }
          ebrcVO.setStatus(sta);
          ebrcVO.setRbiRefNo(commonMethods.getEmptyIfNull(rs.getString("BILLID")).trim());
          ebrcVO.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IEC")).trim());
          ebrcVO.setExportName(commonMethods.getEmptyIfNull(rs.getString("EXPNAME")).trim());
          ebrcVO.setCodeIFSC(commonMethods.getEmptyIfNull(rs.getString("IFSC")).trim());
          ebrcVO.setBillID(commonMethods.getEmptyIfNull(rs.getString("BILLID")).trim());
          ebrcVO.setShipNo(commonMethods.getEmptyIfNull(rs.getString("SNO")).trim());
          ebrcVO.setShipPort(commonMethods.getEmptyIfNull(rs.getString("SPORT")).trim());
          ebrcVO.setShipDate(commonMethods.getEmptyIfNull(rs.getString("SDATE")).trim());
          ebrcVO.setShipCurr(commonMethods.getEmptyIfNull(rs.getString("SCC")).trim());
          ebrcVO.setShipValue(commonMethods.getEmptyIfNull(rs.getString("SVALUE")).trim());
          ebrcVO.setRealCurr(commonMethods.getEmptyIfNull(rs.getString("RCC")).trim());
          ebrcVO.setRealValue(commonMethods.getEmptyIfNull(rs.getString("RVALUE")).trim());
          ebrcVO.setRealDate(commonMethods.getEmptyIfNull(rs.getString("RDATE")).trim());
          ebrcVO.setTotalRealValue(commonMethods.getEmptyIfNull(rs.getString("RVALUEINR")).trim());
          ebrcVO.setRmtBank(commonMethods.getEmptyIfNull(rs.getString("RMTBANK")).trim());
          ebrcVO.setRmtCity(commonMethods.getEmptyIfNull(rs.getString("RMTCITY")).trim());
          ebrcVO.setRmtCountry(commonMethods.getEmptyIfNull(rs.getString("RMTCTRY")).trim());
          ebrcVO.setBillRefNo(commonMethods.getEmptyIfNull(rs.getString("BILL_REF")).trim());
          ebrcVO.setEventRefNo(commonMethods.getEmptyIfNull(rs.getString("EVENT_REF")).trim());
        }
        if (!this.list.isEmpty()) {
          ebrcVO.setList(this.list);
        }
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return ebrcVO;
  }
 
  public ArrayList<EbrcProcessVO> fetchEbrcValue(EbrcProcessVO ebrcVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    this.list1 = new ArrayList();
    commonMethods = new CommonMethods();
    String brcNum = commonMethods.getEmptyIfNull(ebrcVO.getBrcNumber()).trim();
    String brcStatus = commonMethods.getEmptyIfNull(ebrcVO.getEbrcStatus()).trim();
    String importCode = commonMethods.getEmptyIfNull(ebrcVO.getEbrcIECode()).trim();
    String billRef = commonMethods.getEmptyIfNull(ebrcVO.getBillRefNumber()).trim();
    String eventRef = commonMethods.getEmptyIfNull(ebrcVO.getEventRefNumber()).trim();
    String fromDate = commonMethods.getEmptyIfNull(ebrcVO.getFromDate()).trim();
    String tobrcDate = commonMethods.getEmptyIfNull(ebrcVO.getToDate()).trim();
   
    boolean checkbillRef = false;
    boolean checkbrcNum = false;
    boolean checkbrcStatus = false;
    boolean checkeventRef = false;
    boolean checkimportCode = false;
    boolean checkfromDate = false;
    boolean checktobrcDate = false;
    int checkValue = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "select NVL(TRIM(BILL_REFNO), ' ') AS BILL_REFNO,NVL(TRIM(BRCNO), ' ') AS BRCNO,  NVL(TRIM(EVENT_REFNO), ' ') AS EVENT_REFNO,TO_CHAR(TO_DATE(BRCDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS BRCDATE,  NVL(TRIM(STATUS), ' ') AS STATUS, NVL(TRIM(IEC), ' ') AS IEC, NVL(TRIM(EXPNAME), ' ') AS EXPNAME,  NVL(TRIM(IFSC), ' ') AS IFSC, NVL(TRIM(BILLID), ' ') AS BILLID, NVL(TRIM(SNO), ' ') AS SNO, NVL(TRIM(SPORT), ' ') AS SPORT, TO_CHAR(TO_DATE(SDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS SDATE, NVL(TRIM(SCC), ' ') AS SCC, NVL(TRIM(SVALUE), ' ') AS SVALUE, NVL(TRIM(RCC), ' ') AS RCC, NVL(TRIM(RVALUE), ' ') AS RVALUE, TO_CHAR(TO_DATE(RDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS RDATE,  NVL(TRIM(RVALUEINR), ' ') AS RVALUEINR, NVL(TRIM(RMTBANK), ' ') AS RMTBANK, NVL(TRIM(RMTCITY), ' ') AS RMTCITY, NVL(TRIM(RMTCTRY), ' ') AS RMTCTRY, NVL(TRIM(FILENAME), ' ') AS FILENAME, NVL(TRIM(FILE_ERRCODE), ' ') AS FILE_ERRCODE,  NVL(TRIM(BRC_ERRORCODE), ' ') AS BRC_ERRORCODE FROM ETT_EBRC_FILES WHERE BRCNO = BRCNO ";
      if (!CommonMethods.isNullValue(billRef))
      {
        sqlQuery = sqlQuery + " AND BILL_REFNO = ? ";
        checkbillRef = true;
      }
      if (!CommonMethods.isNullValue(brcNum))
      {
        sqlQuery = sqlQuery + " AND BRCNO = ? ";
        checkbrcNum = true;
      }
      if (!brcStatus.equalsIgnoreCase("-1")) {
        if (brcStatus.equalsIgnoreCase("E"))
        {
          sqlQuery = sqlQuery + " AND STATUS NOT IN ('F','C','Downloaded','000','CU','CD','CS')";
        }
        else
        {
          sqlQuery = sqlQuery + " AND STATUS = ? ";
          checkbrcStatus = true;
        }
      }
      if (!CommonMethods.isNullValue(eventRef))
      {
        sqlQuery = sqlQuery + " AND EVENT_REFNO= ? ";
        checkeventRef = true;
      }
      if (!CommonMethods.isNullValue(importCode))
      {
        sqlQuery = sqlQuery + " AND IEC= ? ";
        checkimportCode = true;
      }
      if ((!commonMethods.isNull(fromDate)) && (!commonMethods.isNull(tobrcDate)))
      {
        sqlQuery = sqlQuery + " AND TO_DATE(BRCDATE,'DD-MM-YY') BETWEEN TO_DATE( ? ,'YYYY-MM-DD')  AND TO_DATE( ? ,'YYYY-MM-DD')";
       
        checkfromDate = true;
        checktobrcDate = true;
      }
      else
      {
        if (!commonMethods.isNull(fromDate))
        {
          sqlQuery = sqlQuery + " AND TO_DATE(BRCDATE,'DD-MM-YY') >= TO_DATE( ? ,'YYYY-MM-DD')";
          checkfromDate = true;
        }
        if (!commonMethods.isNull(tobrcDate))
        {
          sqlQuery = sqlQuery + " AND TO_DATE(BRCDATE,'DD-MM-YY') <= TO_DATE( ? ,'YYYY-MM-DD')";
          checktobrcDate = true;
        }
      }
      ppt = new LoggableStatement(con, sqlQuery);
      if (checkbillRef) {
        ppt.setString(++checkValue, billRef.trim());
      }
      if (checkbrcNum) {
        ppt.setString(++checkValue, brcNum.trim());
      }
      if (checkbrcStatus) {
        ppt.setString(++checkValue, brcStatus);
      }
      if (checkeventRef) {
        ppt.setString(++checkValue, eventRef.trim());
      }
      if (checkimportCode) {
        ppt.setString(++checkValue, importCode.trim());
      }
      if (checkfromDate) {
        ppt.setString(++checkValue, fromDate.trim());
      }
      if (checktobrcDate) {
        ppt.setString(++checkValue, tobrcDate);
      }
      logger.info("Get EBRC Values from DB " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next())
      {
        ebrcVO = new EbrcProcessVO();
        ebrcVO.setBillRefNumber(commonMethods.getEmptyIfNull(rs.getString("BILL_REFNO")).trim());
        ebrcVO.setEventRefNumber(commonMethods.getEmptyIfNull(rs.getString("EVENT_REFNO")).trim());
        ebrcVO.setBrcNo(commonMethods.getEmptyIfNull(rs.getString("BRCNO")).trim());
        ebrcVO.setBrcDate(commonMethods.getEmptyIfNull(rs.getString("BRCDATE")).trim());
        String sta = commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim();
        if (sta.equalsIgnoreCase("000")) {
          sta = "Succeeded";
        } else if (sta.equalsIgnoreCase("F")) {
          sta = "Fresh";
        } else if (sta.equalsIgnoreCase("C")) {
          sta = "Cancelled";
        } else if (sta.equalsIgnoreCase("Downloaded")) {
          sta = "Downloaded";
        } else if (sta.equalsIgnoreCase("CU")) {
          sta = "Reject";
        } else if (sta.equalsIgnoreCase("CD")) {
          sta = "Cancel Downloaded";
        } else if (sta.equalsIgnoreCase("CS")) {
          sta = "Cancel Succeeded";
        } else {
          sta = "Error";
        }
        ebrcVO.setStatus(sta);
        ebrcVO.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IEC")).trim());
        ebrcVO.setExportName(commonMethods.getEmptyIfNull(rs.getString("EXPNAME")).trim());
        ebrcVO.setCodeIFSC(commonMethods.getEmptyIfNull(rs.getString("IFSC")).trim());
        ebrcVO.setBillID(commonMethods.getEmptyIfNull(rs.getString("BILLID")).trim());
        ebrcVO.setShipNo(commonMethods.getEmptyIfNull(rs.getString("SNO")).trim());
        ebrcVO.setShipPort(commonMethods.getEmptyIfNull(rs.getString("SPORT")).trim());
        ebrcVO.setShipDate(commonMethods.getEmptyIfNull(rs.getString("SDATE")).trim());
        ebrcVO.setShipCurr(commonMethods.getEmptyIfNull(rs.getString("SCC")).trim());
        ebrcVO.setShipValue(commonMethods.getEmptyIfNull(rs.getString("SVALUE")).trim());
        ebrcVO.setRealCurr(commonMethods.getEmptyIfNull(rs.getString("RCC")).trim());
        ebrcVO.setRealValue(commonMethods.getEmptyIfNull(rs.getString("RVALUE")).trim());
        ebrcVO.setRealDate(commonMethods.getEmptyIfNull(rs.getString("RDATE")).trim());
        ebrcVO.setTotalRealValue(commonMethods.getEmptyIfNull(rs.getString("RVALUEINR")).trim());
        ebrcVO.setRmtBank(commonMethods.getEmptyIfNull(rs.getString("RMTBANK")).trim());
        ebrcVO.setRmtCity(commonMethods.getEmptyIfNull(rs.getString("RMTCITY")).trim());
        ebrcVO.setRmtCountry(commonMethods.getEmptyIfNull(rs.getString("RMTCTRY")).trim());
        this.list1.add(ebrcVO);
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return this.list1;
  }
 
  public void storeEbrcDetails(EbrcProcessVO ebrcVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rst = null;
    int count = 0;
    String brcNumber = ebrcVO.getBrcNo();
    Date date = new Date();
    SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");
   
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (brcNumber != null)
      {
        String sqlQuery = "select count(*) FROM ETT_EBRC_FILES WHERE BRCNO ='" + brcNumber.trim() + "'";
        pst = new LoggableStatement(con, sqlQuery);
        rst = pst.executeQuery();
        if (rst.next()) {
          count = rst.getInt(1);
        }
        if ((count > 0) && (!ebrcVO.getTempStatus().equalsIgnoreCase("-1")))
        {
          if (ebrcVO.getTempStatus().equalsIgnoreCase("E"))
          {
            String sqlUpdate = "UPDATE ETT_EBRC_FILES SET STATUS=?,IEC=?,EXPNAME=?,SPORT=?, RMTBANK=?,RMTCITY=?,RMTCTRY=?,UPDATED_DATE=? WHERE BRCNO=?";
           
            LoggableStatement pst1 = new LoggableStatement(con, sqlUpdate);
            pst1.setString(1, "F");
            pst1.setString(2, commonMethods.getEmptyIfNull(ebrcVO.getIeCode()).trim());
            pst1.setString(3, commonMethods.getEmptyIfNull(ebrcVO.getExportName()).trim());
            pst1.setString(4, commonMethods.getEmptyIfNull(ebrcVO.getShipPort()).trim());
            pst1.setString(5, commonMethods.getEmptyIfNull(ebrcVO.getRmtBank()).trim());
            pst1.setString(6, commonMethods.getEmptyIfNull(ebrcVO.getRmtCity()).trim());
            pst1.setString(7, commonMethods.getEmptyIfNull(ebrcVO.getRmtCountry()).trim());
            pst1.setString(8, FORMATTER.format(date));
            pst1.setString(9, ebrcVO.getBrcNo());
            logger.info("Update Query " + pst1.getQueryString());
            int i = pst1.executeUpdate();
            if (i == 0)
            {
              con.commit();
              pst1.close();
            }
          }
          if ((ebrcVO.getTempStatus().equalsIgnoreCase("C")) ||
            (ebrcVO.getTempStatus().equalsIgnoreCase("CS")))
          {
            String sqlQuery1 = "select count(*) FROM ETT_EBRC_FILES WHERE BRCNO ='" + brcNumber.trim() + "' " +
              " AND STATUS ='" + ebrcVO.getTempStatus() + "'";
            LoggableStatement pst2 = new LoggableStatement(con, sqlQuery1);
            ResultSet rst1 = pst2.executeQuery();
            if (rst1.next()) {
              count = rst1.getInt(1);
            }
            closeSqlRefferance(rst1, pst2);
            if (count > 0)
            {
              String sqlInsertQuery = "INSERT INTO ETT_EBRC_FILES(BRCNO, BRCDATE,STATUS,BILL_REFNO,EVENT_REFNO,IEC,EXPNAME,IFSC,BILLID, SNO,SPORT,SDATE,SCC,SVALUE,RCC,RVALUE,RDATE,RVALUEINR,RMTBANK,RMTCITY, RMTCTRY,CREATED_DATE) VALUES (?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,to_timestamp(?, 'DD-MM-YYYY HH12:MI:SS'))";
             




              SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
              SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
             

              HttpSession session = ServletActionContext.getRequest()
                .getSession();
              String processeddate = (String)session.getAttribute("processDate");
             
              Date shippingDate = format1.parse(ebrcVO.getShipDate());
             
              Date realDate1 = format1.parse(ebrcVO.getRealDate());
             
              String brcDate = processeddate;
             
              SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
              String createdDate = df.format(Long.valueOf(new Timestamp().getDateTime()));
             
              String codeIFSC = ebrcVO.getCodeIFSC();
             
              String brcNo = null;
              if (codeIFSC != null)
              {
                String squ = "SELECT ETT_GET_BRC_NO('" + codeIFSC.trim() + "') AS brcNo FROM DUAL";
                PreparedStatement pps = con.prepareStatement(squ);
                ResultSet rst2 = pps.executeQuery();
                logger.info("Get ETT_GET_BRC_NO Values " + squ);
                if (rst2.next()) {
                  brcNo = rst2.getString("brcNo");
                }
                closeSqlRefferance(rst2, pps);
              }
              LoggableStatement pst1 = new LoggableStatement(con, sqlInsertQuery);
              pst1.setString(1, brcNo);
              pst1.setString(2, brcDate);
              pst1.setString(3, "F");
              pst1.setString(4, commonMethods.getEmptyIfNull(ebrcVO.getBillRefNo()).trim());
              pst1.setString(5, commonMethods.getEmptyIfNull(ebrcVO.getEventRefNo()).trim());
              pst1.setString(6, commonMethods.getEmptyIfNull(ebrcVO.getIeCode()).trim());
              pst1.setString(7, commonMethods.getEmptyIfNull(ebrcVO.getExportName()).trim());
              pst1.setString(8, commonMethods.getEmptyIfNull(ebrcVO.getCodeIFSC()).trim());
              pst1.setString(9, commonMethods.getEmptyIfNull(ebrcVO.getRbiRefNo()).trim());
              pst1.setString(10, commonMethods.getEmptyIfNull(ebrcVO.getShipNo()).trim());
              pst1.setString(11, commonMethods.getEmptyIfNull(ebrcVO.getShipPort()).trim());
              pst1.setString(12, format2.format(shippingDate));
              pst1.setString(13, commonMethods.getEmptyIfNull(ebrcVO.getShipCurr()).trim());
              pst1.setString(14, commonMethods.getEmptyIfNull(ebrcVO.getShipValue()).trim());
              pst1.setString(15, commonMethods.getEmptyIfNull(ebrcVO.getRealCurr()).trim());
              pst1.setString(16, commonMethods.getEmptyIfNull(ebrcVO.getRealValue()).trim());
              pst1.setString(17, commonMethods.getEmptyIfNull(format2.format(realDate1)).trim());
              pst1.setString(18, commonMethods.getEmptyIfNull(ebrcVO.getTotalRealValue()).trim());
              pst1.setString(19, commonMethods.getEmptyIfNull(ebrcVO.getRmtBank()).trim());
              pst1.setString(20, commonMethods.getEmptyIfNull(ebrcVO.getRmtCity()).trim());
              pst1.setString(21, commonMethods.getEmptyIfNull(ebrcVO.getRmtCountry()).trim());
              pst1.setString(22, createdDate);
              logger.info(pst1.getQueryString());
              int i = pst1.executeUpdate();
              if (i > 0)
              {
                String sqlUpdate = "UPDATE ETT_EBRC_FILES SET STATUS=? WHERE BRCNO=?";
                LoggableStatement pst3 = new LoggableStatement(con, sqlUpdate);
                pst3.setString(1, "CU");
                pst3.setString(2, ebrcVO.getBrcNo());
                logger.info("Update ETT_EBRC_FILES Values " + pst3.getQueryString());
                pst3.executeUpdate();
                con.commit();
                pst3.close();
              }
              closePreparedStatement(pst1);
            }
            else
            {
              String sqlUpdate = "UPDATE ETT_EBRC_FILES SET STATUS=?,UPDATED_DATE=? WHERE BRCNO=?";
              LoggableStatement pst1 = new LoggableStatement(con, sqlUpdate);
              pst1.setString(1, ebrcVO.getTempStatus());
              pst1.setString(2, FORMATTER.format(date));
              pst1.setString(3, ebrcVO.getBrcNo());
              logger.info("Update ETT_EBRC_FILES Values-----> " + pst1.getQueryString());
              pst1.executeUpdate();
              con.commit();
              closePreparedStatement(pst1);
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
      logger.info("Exiting Method");
      closeSqlRefferance(rst, pst, con);
    }
  }
 
  public String cancelXmlGenerate()
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    int xmlCount = 0;
    String fileName = "";
    String result = null;
    try
    {
      logger.info("cancel");
      con = DBConnectionUtility.getConnection();
     


      SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
     
      Date date = new Date();
      logger.info("Current Date " + new Timestamp(date.getTime()));
     
      Date realiDate = null;
     
      DateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
     
      Date date1 = new Date();
     
      String prodate = dateFormat1.format(date1);
     
      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      String processeddate = (String)session.getAttribute("processDate");
     

      logger.info("processed date:" + processeddate);
     
      String sq = "SELECT LPAD(EBRC_SEQNO.NEXTVAL,3,'0')ID FROM DUAL";
      PreparedStatement pps = con.prepareStatement(sq);
      ResultSet rst = pps.executeQuery();
     
      int seq = 0;
      if (rst.next()) {
        seq = rst.getInt(1);
      }
      closeSqlRefferance(rst, pps);
     
      logger.info("File SEQUENCE" + String.format("%03d", new Object[] { Integer.valueOf(seq) }));
     
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
     








      logger.info("sqlquery" + sqlQuery11);
      logger.info("Query " + sqlQuery11);
      ps = new LoggableStatement(con, sqlQuery11);
      logger.info("cancelXmlGenerate  ETT_EBRC_FILES Values " + ps.getQueryString());
      res = ps.executeQuery();
      while (res.next())
      {
        String brcNo = res.getString("BRCNO");
        String brcDate = res.getString("BRCDATE");
        String status = res.getString("STATUS");
        String ieCode = res.getString("IEC");
        String exportName = res.getString("EXPNAME");
        String codeIFSC = res.getString("IFSC");
        String billID = res.getString("BILLID");
        String shipNo = res.getString("SNO");
        String shipPort = res.getString("SPORT");
        String shipDate = res.getString("SDATE");
        String shipCurr = res.getString("SCC");
        String shipValue = res.getString("SVALUE");
        String realCurr = res.getString("RCC");
        String realValue = res.getString("RVALUE");
        String realDate = res.getString("RDATE");
       
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
          realiDate = format1.parse(realDate);
          logger.info(format2.format(realiDate));
          Element bn = document.createElement("RDATE");
          bn.appendChild(document.createTextNode(realDate));
          rocdoc1.appendChild(bn);
        }
        String updateStatus = " UPDATE ETT_EBRC_FILES SET STATUS='CD' , FILENAME =?  where BRCNO= ? ";
        LoggableStatement ps2 = new LoggableStatement(con, updateStatus);
        ps2.setString(1, fileName);
        ps2.setString(2, brcNo);
        logger.info("updateStatus---->" + ps2.getQueryString());
        ps2.executeUpdate();
        closePreparedStatement(ps2);
       
        xmlCount++;
      }
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
       
        Transformer transformer = transformerFactory.newTransformer();
       
        DOMSource domSource = new DOMSource(document);
       



        String myFilePath = ActionConstants.FileLocationEBRC + fileName + ".xml";
       

        StreamResult streamResult = new StreamResult(new File(myFilePath));
       
        transformer.transform(domSource, streamResult);
        logger.info("filename" + fileName);
        result = fileName;
        logger.info("File has been successfully saved");
      }
    }
    catch (Exception e)
    {
      logger.info("Exception!!!" + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String xmlGenerate()
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    CommonMethods commonMethods = null;
    int xmlCount = 0;
    String fileName = "";
    String result = null;
    try
    {
      logger.info("xml");
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     


      SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
     
      Date date = new Date();
      logger.info("Current Date " + new Timestamp(date.getTime()));
     

      Date realiDate = null;
     
      DateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
     
      SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
      String createdDate = df.format(Long.valueOf(new Timestamp().getDateTime()));
     

      Date date1 = new Date();
     
      String prodate = dateFormat1.format(date1);
     
      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      String processeddate = (String)session.getAttribute("processDate");
     

      String sqlQuery = "SELECT * FROM ETTV_EBRC_LODGEMENT_VW";
     
      logger.info("Query " + sqlQuery);
     
      ps = new LoggableStatement(con, sqlQuery);
      res = ps.executeQuery();
      logger.info("res value" + res.getRow());
      while (res.next())
      {
        String brcDate = processeddate;
        logger.info("brcDate" + brcDate);
        String status = "F";
        String rbiRefNo = commonMethods.getEmptyIfNull(res.getString("BILLID"));
        String billRef = commonMethods.getEmptyIfNull(res.getString("MASTERREF")).trim();
        String eventRef = commonMethods.getEmptyIfNull(res.getString("EVENTREF")).trim();
        String ieCode = commonMethods.getEmptyIfNull(res.getString("IECODE"));
        String exportName = commonMethods.getEmptyIfNull(res.getString("EXPORTERNAME"));
        String codeIFSC = commonMethods.getEmptyIfNull(res.getString("BANKCODE"));
        String shipNo = commonMethods.getEmptyIfNull(res.getString("BILLNO"));
        String shipPort = commonMethods.getEmptyIfNull(res.getString("PORTCODE"));
        String shipDate = commonMethods.getEmptyIfNull(res.getString("SHIP_DATE"));
        logger.info("shipdate" + shipDate);
        Date shippingDate = null;
        String tempDate = null;
        if ((shipDate != null) && (!shipDate.equalsIgnoreCase("")))
        {
          logger.info("Inside Loop---------shipDate---------" + shipDate);
          try
          {
            shippingDate = format1.parse(shipDate);
            logger.info("date--" + shippingDate);
           
            tempDate = format2.format(shippingDate);
          }
          catch (Exception e)
          {
            logger.info("exceoption" + e.getMessage());
          }
        }
        String shipCurr = commonMethods.getEmptyIfNull(res.getString("SHIP_CURR"));
        String shipValue = commonMethods.getEmptyIfNull(res.getString("BILL_AMT"));
        String realCurr = commonMethods.getEmptyIfNull(res.getString("CURRENCYOFREALIZATION"));
        String realValue = commonMethods.getEmptyIfNull(res.getString("TotalREALIZED"));
        String realDate = commonMethods.getEmptyIfNull(res.getString("date of realization"));
        String totalReal = commonMethods.getEmptyIfNull(res.getString("Total realised Value in INR"));
        String rmtBank = commonMethods.getEmptyIfNull(res.getString("REMITTER_NAME"));
        String rmtCity = commonMethods.getEmptyIfNull(res.getString("REMITTER_ADDRESS"));
        String rmtCountry = commonMethods.getEmptyIfNull(res.getString("REMITTER_COUNTRY"));
       
        String temp_rmtBank = rmtBank.replaceAll("[^A-Za-z ]", "");
        String temp_rmtCity = rmtCity.replaceAll("[^A-Za-z ]", "");
       

        logger.info("date-temp-" + tempDate);
        if ((!billRef.equalsIgnoreCase("")) && (!eventRef.equalsIgnoreCase("")) && (!shipNo.equalsIgnoreCase("")) &&
          (tempDate != null) && (!tempDate.equalsIgnoreCase("")))
        {
          logger.info("Bill Reference No:" + billRef);
         


          boolean isAlreadyExits = checkingData(con, billRef, eventRef, shipNo, tempDate, shipPort);
         
          logger.info("Bill Reference Status:" + isAlreadyExits);
          if (!isAlreadyExits)
          {
            String brcNo = null;
            if (codeIFSC != null)
            {
              String squ = "SELECT ETT_GET_BRC_NO('" + codeIFSC.trim() + "') AS brcNo FROM DUAL";
              PreparedStatement pps = con.prepareStatement(squ);
              logger.info("ETT_GET_BRC_NO---->" + squ);
              ResultSet rst2 = pps.executeQuery();
              if (rst2.next()) {
                brcNo = rst2.getString("brcNo");
              }
              closeSqlRefferance(rst2, pps);
            }
            String fircDate = getFircDate(con, billRef, eventRef, shipNo, shipPort);
            Date realDate1;
            Date realDate1;
            if (!commonMethods.isNull(fircDate)) {
              realDate1 = format1.parse(fircDate);
            } else {
              realDate1 = format1.parse(realDate);
            }
            String sqlInsertQuery = "INSERT INTO ETT_EBRC_FILES(BRCNO,BRCDATE,STATUS,BILL_REFNO,EVENT_REFNO,IEC,EXPNAME,IFSC,BILLID,SNO,SPORT,SDATE,SCC,SVALUE,RCC,RVALUE,RDATE,RVALUEINR,RMTBANK,RMTCITY,RMTCTRY,CREATED_DATE) VALUES (?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,to_timestamp(?, 'DD-MM-YYYY HH12:MI:SS'))";
           





















            logger.info("Query " + sqlInsertQuery);
            LoggableStatement pst1 = new LoggableStatement(con, sqlInsertQuery);
            pst1.setString(1, commonMethods.getEmptyIfNull(brcNo).trim());
            pst1.setString(2, commonMethods.getEmptyIfNull(brcDate).trim());
            pst1.setString(3, commonMethods.getEmptyIfNull(status).trim());
            pst1.setString(4, commonMethods.getEmptyIfNull(billRef).trim());
            pst1.setString(5, commonMethods.getEmptyIfNull(eventRef).trim());
            pst1.setString(6, commonMethods.getEmptyIfNull(ieCode).trim());
            pst1.setString(7, commonMethods.getEmptyIfNull(exportName).toUpperCase().trim());
            pst1.setString(8, commonMethods.getEmptyIfNull(codeIFSC).trim());
            pst1.setString(9, commonMethods.getEmptyIfNull(rbiRefNo).trim());
            pst1.setString(10, commonMethods.getEmptyIfNull(shipNo).trim());
            pst1.setString(11, commonMethods.getEmptyIfNull(shipPort).trim());
            pst1.setString(12, format2.format(shippingDate));
            pst1.setString(13, commonMethods.getEmptyIfNull(shipCurr).trim());
            pst1.setString(14, commonMethods.getEmptyIfNull(shipValue).trim());
            pst1.setString(15, commonMethods.getEmptyIfNull(realCurr).trim());
            pst1.setString(16, commonMethods.getEmptyIfNull(realValue).trim());
            pst1.setString(17, format2.format(realDate1));
            pst1.setString(18, commonMethods.getEmptyIfNull(totalReal).trim());
            pst1.setString(19, commonMethods.getEmptyIfNull(temp_rmtBank).toUpperCase().trim().replace(",", " "));
            pst1.setString(20, commonMethods.getEmptyIfNull(temp_rmtCity).toUpperCase().trim().replace(",", " "));
            pst1.setString(21, commonMethods.getEmptyIfNull(rmtCountry).toUpperCase().trim());
            pst1.setString(22, createdDate);
            logger.info("EBRC FILES ->>>>>>>>>>>>>>>" + pst1.getQueryString());
            pst1.executeUpdate();
            con.commit();
            closePreparedStatement(pst1);
          }
        }
      }
      logger.info("processed date:" + processeddate);
     
      String sq = "SELECT LPAD(EBRC_SEQNO.NEXTVAL,3,'0')ID FROM DUAL";
      PreparedStatement pps = con.prepareStatement(sq);
      ResultSet rst = pps.executeQuery();
     
      int seq = 0;
      if (rst.next()) {
        seq = rst.getInt(1);
      }
      closeSqlRefferance(rst, pps);
     



      logger.info("File SEQUENCE" + String.format("%03d", new Object[] { Integer.valueOf(seq) }));
     

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
     
      String sqlQuery1 = "select NVL(TRIM(BRCNO), ' ') AS BRCNO, TO_CHAR(TO_DATE(BRCDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS BRCDATE, NVL(TRIM(STATUS), ' ') AS STATUS, NVL(TRIM(IEC), ' ') AS IEC, NVL(TRIM(EXPNAME), ' ') AS EXPNAME, NVL(TRIM(IFSC), ' ') AS IFSC, NVL(TRIM(BILLID), ' ') AS BILLID, NVL(TRIM(SNO), ' ') AS SNO, NVL(TRIM(SPORT), ' ') AS SPORT, TO_CHAR(TO_DATE(SDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS SDATE, NVL(TRIM(SCC), ' ') AS SCC, NVL(TRIM(SVALUE), ' ') AS SVALUE, NVL(TRIM(RCC), ' ') AS RCC, NVL(TRIM(RVALUE), ' ') AS RVALUE, TO_CHAR(TO_DATE(RDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS RDATE, NVL(TRIM(RVALUEINR), ' ') AS RVALUEINR, NVL(TRIM(RMTBANK), ' ') AS RMTBANK, NVL(TRIM(RMTCITY), ' ') AS RMTCITY, NVL(TRIM(RMTCTRY), ' ') AS RMTCTRY, NVL(TRIM(FILENAME), ' ') AS FILENAME, NVL(TRIM(FILE_ERRCODE), ' ') AS FILE_ERRCODE, NVL(TRIM(BRC_ERRORCODE), ' ') AS BRC_ERRORCODE FROM ETT_EBRC_FILES where status='F'";
     










      logger.info("Query " + sqlQuery1);
     
      LoggableStatement pps1 = new LoggableStatement(con, sqlQuery1);
      ResultSet res1 = pps1.executeQuery();
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
          realiDate = format1.parse(realDate);
          logger.info(format2.format(realiDate));
          Element bn = document.createElement("RDATE");
          bn.appendChild(document.createTextNode(realDate));
          rocdoc1.appendChild(bn);
        }
        String updateStatus = "UPDATE ETT_EBRC_FILES SET STATUS='Downloaded' , FILENAME =?  where BRCNO= ? ";
        LoggableStatement ps2 = new LoggableStatement(con, updateStatus);
        ps2.setString(1, fileName);
        ps2.setString(2, brcNo);
        logger.info("updateStatus---->" + ps2.getQueryString());
        ps2.executeUpdate();
        closePreparedStatement(ps2);
       
        xmlCount++;
      }
      closeSqlRefferance(res1, pps1);
      logger.info("xmlcouhnt" + xmlCount);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
       
        Transformer transformer = transformerFactory.newTransformer();
       
        DOMSource domSource = new DOMSource(document);
       



        String myFilePath = ActionConstants.FileLocationEBRC + fileName + ".xml";
       

        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
        result = fileName;
        logger.info("resultfile--" + result);
        logger.info("File has been successfully saved");
      }
    }
    catch (Exception e)
    {
      logger.info("Exception!!!" + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in saveDetail");
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean checkingData(Connection con, String billRef, String eventRef, String shipNo, String shipDate, String portCode)
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean result = false;
   
    boolean checkshipNo = false;
    try
    {
      con = DBConnectionUtility.getConnection();
      String sq = "SELECT COUNT(1) FROM  ETT_EBRC_FILES  WHERE BILL_REFNO LIKE '%'|| ? ||'%' AND EVENT_REFNO = ?  AND TO_DATE(SDATE, 'dd-mm-yy')= ? ";
      if ((portCode != null) && (!portCode.equalsIgnoreCase("OTHERS")))
      {
        sq = sq + " AND SNO = ? ";
        checkshipNo = true;
      }
      ps = con.prepareStatement(sq);
      ps.setString(1, billRef.trim());
      ps.setString(2, eventRef.trim());
      ps.setString(3, shipDate.trim());
      if (checkshipNo) {
        ps.setString(4, shipNo.trim());
      }
      logger.info("checkingData---->" + sq);
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
      e.printStackTrace();
    }
    finally
    {
      closeSqlRefferance(rs, ps);
    }
    return result;
  }
 
  public void setDate()
    throws DAOException
  {
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      Map<String, Object> session = ActionContext.getContext().getSession();
      con = DBConnectionUtility.getConnection();
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE FROM dlyprccycl";
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      while (rs.next())
      {
        String dateValue = commonMethods.getEmptyIfNull(rs.getString("PROCDATE")).trim();
        session.put("processDate", dateValue);
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
  }
 
  public String getErrorDesc(String errorCode)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    String errorMsg = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      query = "SELECT ERROR_MSG FROM AD_ERROR_CODES WHERE ERROR_CODES = ? ";
      pst = new LoggableStatement(con, query);
      pst.setString(1, errorCode);
      rs = pst.executeQuery();
      while (rs.next()) {
        errorMsg = rs.getString("ERROR_MSG");
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
    return errorMsg;
  }
 
  public void ifscXMLGenerate()
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet res = null;
    CommonMethods commonMethods = null;
    int xmlCount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     
      String fileName = "";
     
      DateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
     
      Date date1 = new Date();
     
      String prodate = dateFormat1.format(date1);
     

      String sqlQuery = "SELECT * FROM ETTV_IFSC_CODE_VIEW";
     
      logger.info("Query " + sqlQuery);
      ps = new LoggableStatement(con, sqlQuery);
      res = ps.executeQuery();
      while (res.next())
      {
        String ifscCode = commonMethods.getEmptyIfNull(res.getString("IFSCCODE")).trim();
        String bankName = commonMethods.getEmptyIfNull(res.getString("BANK_NAME")).trim().replace(",", "");
        String branch = commonMethods.getEmptyIfNull(res.getString("BANK_BRANCH")).trim();
        String address = commonMethods.getEmptyIfNull(res.getString("BANK_ADDRESS")).trim();
        String city = commonMethods.getEmptyIfNull(res.getString("CITY")).trim();
        String state = commonMethods.getEmptyIfNull(res.getString("STATE")).trim();
        String dealForeign = commonMethods.getEmptyIfNull(res.getString("DEALFE")).trim();
        if (!ifscCode.equalsIgnoreCase(""))
        {
          logger.info("IFSC CODE:" + ifscCode);
         
          boolean isAlreadyExits = checkingIFSCData(con, ifscCode);
          logger.info("Bill Reference Status:" + isAlreadyExits);
          if (!isAlreadyExits)
          {
            String sqlInsertQuery = "INSERT INTO ETT_IFSC_FILES(IFSCCODE,BANK_NAME,BANK_BRANCH,BANK_ADDRESS,CITY,STATE,BANK_STATUS,FOREIGN_EX,DATASTAT,STATUS) VALUES (?,?,?,?,?,?,?,?,?,?)";
           








            logger.info("Query " + sqlInsertQuery);
            LoggableStatement pst = new LoggableStatement(con, sqlInsertQuery);
           
            pst.setString(1, commonMethods.getEmptyIfNull(ifscCode).trim());
            pst.setString(2, commonMethods.getEmptyIfNull(bankName).toUpperCase().trim());
            pst.setString(3, commonMethods.getEmptyIfNull(branch).toUpperCase().trim());
            pst.setString(4, commonMethods.getEmptyIfNull(address).toUpperCase().trim());
            pst.setString(5, commonMethods.getEmptyIfNull(city).toUpperCase().trim());
            pst.setString(6, commonMethods.getEmptyIfNull(state).toUpperCase().trim());
            pst.setString(7, "O");
            pst.setString(8, commonMethods.getEmptyIfNull(dealForeign).toUpperCase().trim());
            pst.setString(9, "F");
            pst.setString(10, "P");
           
            logger.info("IFSC FILES ->>>>>>>>>>>>>>>" + ps.getQueryString());
            pst.executeUpdate();
            con.commit();
            closePreparedStatement(pst);
          }
        }
      }
      fileName = "IFSCDATAIDFB" + prodate;
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
     
      Element root = document.createElement("IFSCDATA");
      document.appendChild(root);
     

      Element fname = document.createElement("FILENAME");
      fname.appendChild(document.createTextNode(fileName));
      root.appendChild(fname);
     
      Element rocdoc2 = document.createElement("ENVELOPE");
      root.appendChild(rocdoc2);
     
      String sqlQuery11 = "select NVL(TRIM(IFSCCODE), ' ') AS IFSCCODE,  NVL(TRIM(BANK_NAME), ' ') AS BANK_NAME, NVL(TRIM(BANK_BRANCH), ' ') AS BANK_BRANCH, NVL(TRIM(BANK_ADDRESS), ' ') AS BANK_ADDRESS, NVL(TRIM(CITY), ' ') AS CITY, NVL(TRIM(STATE), ' ') AS STATE, NVL(TRIM(BANK_STATUS), ' ') AS BANK_STATUS, NVL(TRIM(FOREIGN_EX), ' ') AS FOREIGN_EX, NVL(TRIM(DATASTAT), ' ') AS DATASTAT  FROM ETT_IFSC_FILES WHERE STATUS IN ('P')";
     




      logger.info("Query " + sqlQuery11);
      LoggableStatement ps1 = new LoggableStatement(con, sqlQuery11);
      ResultSet res1 = ps1.executeQuery();
      while (res1.next())
      {
        String ifscCode = res1.getString("IFSCCODE");
        String bankName = res1.getString("BANK_NAME");
        String branch = res1.getString("BANK_BRANCH");
        String address = res1.getString("BANK_ADDRESS");
        String city = res1.getString("CITY");
        String state = res1.getString("STATE");
        String bank_status = res1.getString("BANK_STATUS");
        String foreignEx = res1.getString("FOREIGN_EX");
        String dataStat = res1.getString("DATASTAT");
       
        Element rocdoc1 = document.createElement("IFSC");
        rocdoc2.appendChild(rocdoc1);
        if (ifscCode == null)
        {
          Element pc = document.createElement("IFSCODE");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("IFSCODE");
          pc.appendChild(document.createTextNode(ifscCode));
          rocdoc1.appendChild(pc);
        }
        if (bankName == null)
        {
          Element ri = document.createElement("BNAME");
          ri.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ri);
        }
        else
        {
          Element ri = document.createElement("BNAME");
          ri.appendChild(document.createTextNode(bankName));
          rocdoc1.appendChild(ri);
        }
        if (branch == null)
        {
          Element et = document.createElement("BRANCH");
          et.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(et);
        }
        else
        {
          Element et = document.createElement("BRANCH");
          et.appendChild(document.createTextNode(branch));
          rocdoc1.appendChild(et);
        }
        if (address == null)
        {
          Element sbn = document.createElement("ADDR");
          sbn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("ADDR");
          sbn.appendChild(document.createTextNode(address));
          rocdoc1.appendChild(sbn);
        }
        if (city == null)
        {
          Element sbd = document.createElement("CITY");
          sbd.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("CITY");
          sbd.appendChild(document.createTextNode(city));
          rocdoc1.appendChild(sbd);
        }
        if (state == null)
        {
          Element fn = document.createElement("STATE");
          fn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(fn);
        }
        else
        {
          Element fn = document.createElement("STATE");
          fn.appendChild(document.createTextNode(state));
          rocdoc1.appendChild(fn);
        }
        if (bank_status == null)
        {
          Element ld = document.createElement("BRANSTAT");
          ld.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ld);
        }
        else
        {
          Element ld = document.createElement("BRANSTAT");
          ld.appendChild(document.createTextNode(bank_status));
          rocdoc1.appendChild(ld);
        }
        if (foreignEx == null)
        {
          Element ic = document.createElement("DEALFE");
          ic.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ic);
        }
        else
        {
          Element ic = document.createElement("DEALFE");
          ic.appendChild(document.createTextNode(foreignEx));
          rocdoc1.appendChild(ic);
        }
        if (dataStat == null)
        {
          Element cic = document.createElement("DATASTAT");
          cic.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(cic);
        }
        else
        {
          Element cic = document.createElement("DATASTAT");
          cic.appendChild(document.createTextNode(dataStat));
          rocdoc1.appendChild(cic);
        }
        String updateStatus = "UPDATE ETT_IFSC_FILES SET STATUS='Downloaded' , FILENAME =?  where IFSCCODE = ? ";
        LoggableStatement ps2 = new LoggableStatement(con, updateStatus);
        ps2.setString(1, fileName);
        ps2.setString(2, ifscCode);
        logger.info("updateStatus " + ps2.getQueryString());
        ps2.executeUpdate();
        ps2.close();
       
        xmlCount++;
      }
      closeSqlRefferance(res1, ps1);
      if (xmlCount > 0)
      {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
       
        Transformer transformer = transformerFactory.newTransformer();
       
        DOMSource domSource = new DOMSource(document);
       



        String myFilePath = ActionConstants.FileLocation + fileName + ".xml";
       


        StreamResult streamResult = new StreamResult(new File(myFilePath));
        transformer.transform(domSource, streamResult);
      }
    }
    catch (Exception e)
    {
      logger.info("Exception!!!" + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
    }
    logger.info("Exiting Method");
  }
 
  public boolean checkingIFSCData(Connection con, String ifscCode)
  {
    logger.info("Entering Method");
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean result = false;
    try
    {
      con = DBConnectionUtility.getConnection();
      String sq = "SELECT COUNT(1) FROM  ETT_IFSC_FILES  WHERE IFSCCODE = ? ";
     
      ps = con.prepareStatement(sq);
      ps.setString(1, ifscCode.trim());
      logger.info("checkingIFSCData " + sq);
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
      logger.info("Exception!!!" + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      closeSqlRefferance(rs, ps);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String getFircDate(Connection con, String billRefNo, String transEventRefNo, String shipNo, String portCode)
    throws DAOException, SQLException
  {
    logger.info("Entering Method");
    String fircDate = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    boolean shipnoFlag = false;
    int setValue = 0;
    try
    {
      String GET_FIRC_DETAILS = "SELECT  TO_CHAR(TO_DATE(REALIZATIONDATE,'DD-MM-YY'),'YYYY-MM-DD') AS FIRC_DATE  FROM ETT_GR_REL_SHP_TBL WHERE TRIM(BILLREFNO) = TRIM(?)  AND TRIM(PAY_SERIAL_NO) = TRIM(?) ";
      if ((portCode != null) && (!portCode.equalsIgnoreCase("OTHERS")))
      {
        GET_FIRC_DETAILS = GET_FIRC_DETAILS + " AND TRIM(SHIPBILLNO) = TRIM(?)";
        shipnoFlag = true;
      }
      else
      {
        GET_FIRC_DETAILS = GET_FIRC_DETAILS + " AND TRIM(FORMNO) = TRIM(?)";
        shipnoFlag = true;
      }
      pst = new LoggableStatement(con, GET_FIRC_DETAILS);
      pst.setString(++setValue, billRefNo);
      pst.setString(++setValue, transEventRefNo);
      logger.info("getFircDate " + pst.getQueryString());
      if (shipnoFlag) {
        pst.setString(++setValue, shipNo);
      }
      rs = pst.executeQuery();
      if (rs.next()) {
        fircDate = rs.getString("FIRC_DATE");
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      if (pst != null) {
        pst.close();
      }
      if (rs != null) {
        rs.close();
      }
    }
    logger.info("Exiting Method");
    return fircDate;
  }
}
