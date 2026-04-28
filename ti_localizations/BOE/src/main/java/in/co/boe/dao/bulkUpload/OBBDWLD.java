package in.co.boe.dao.bulkUpload;

import in.co.boe.dao.boe_checker.BoeCheckerDAO;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
 
public class OBBDWLD
{
  private static Logger logger = Logger.getLogger(BoeCheckerDAO.class
    .getName());
  public boolean processOBBjob()
  {
    logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]-----------");
    Connection con = null;
    int iQueryStatus = 0;
    boolean result = false;
    String sOBBUpdateQuery = "";

 
 
 
    String COMMA_DELIMITER = "|";
    String NEW_LINE_SEPARATOR = "\n";
    String FILE_HEADER = "billOfEntryNumber|billOfEntryDate|portOfDischarge";
    try
    {
      con = DBConnectionUtility.getConnection();
      String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE,TO_CHAR(PROCDATE,'DD/MM/YYYY') AS PROCDATE1, LPAD(OBB_RBI_REQ_REQ.NEXTVAL,2,0) AS OBB_SEQNO FROM DLYPRCCYCL";

 
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
      rs1.close();
      pps1.close();
      String fileLoc = "";

 
 
      fileLoc = ActionConstants.obbcsvfilepath;
      logger.info("obbcsvfilepath----" + fileLoc);
      fileLoc = fileLoc + "/" + prodate + "/";
      File f = new File(fileLoc);
      if (!f.exists())
      {
        f.mkdir();
      }
      else
      {
        recursiveDelete(f);
        f.mkdir();
      }
      String fileName = "IDIS_OTHERBANKBILLOFENTRY_" + prodate + seq + ".obb";
      String myFilePath = fileLoc + fileName + ".csv";
      logger.info(myFilePath);
      FileWriter fw = new FileWriter(myFilePath, true);
      BufferedWriter bw = new BufferedWriter(fw);

 
      fw.append("billOfEntryNumber|billOfEntryDate|portOfDischarge".toString());
      sOBBUpdateQuery = "UPDATE ETT_BOE_OBB_DETAILS SET STATUS = 'U' WHERE STATUS = 'R' AND BOENUM = ? AND BOEDATE = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ?";
      String query = "SELECT BOENUM, TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE, PORTCODE FROM ETT_BOE_OBB_DETAILS WHERE STATUS = 'R'AND TRIM(REC_DATE) = TO_DATE(?,'DD/MM/YYYY')";

 
      LoggableStatement pst = new LoggableStatement(con, query);
      pst.setString(1, prodate1);
      logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]---ETT_BOE_OBB_DETAILS--query------" + 
        pst.getQueryString());
      ResultSet res = pst.executeQuery();
      while (res.next())
      {
        String sBOENum = setValue(res.getString("BOENUM"));
        String sBOEDate = setValue(res.getString("BOEDATE"));
        String sPortCode = setValue(res.getString("PORTCODE"));
        StringBuffer sContent = new StringBuffer();
        sContent.append("\n");
        sContent.append(sBOENum);
        sContent.append("|");
        sContent.append(sBOEDate);
        sContent.append("|");
        sContent.append(sPortCode);
        bw.write(sContent.toString());
        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]---sBOENum------" + sBOENum);
        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]---sBOEDate------" + sBOEDate);
        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]---sPortCode------" + sPortCode);
        LoggableStatement ps = new LoggableStatement(con, sOBBUpdateQuery);
        logger.info(
          "----------IDPMS ------OBBDWLD--- [processOBBjob]---UPDATE --- ETT_BOE_OBB_DETAILS query---" + 
          pst.getQueryString());
        ps.setString(1, sBOENum);
        ps.setString(2, sBOEDate);
        ps.setString(3, sPortCode);
        iQueryStatus = ps.executeUpdate();
        ps.close();
        logger.info(
          "----------IDPMS ------OBBDWLD--- [processOBBjob]---UPDATE --- ETT_BOE_OBB_DETAILS query-- count-" + 
          iQueryStatus);
      }
      bw.close();
      fw.close();
      res.close();
      pst.close();
      result = true;
    }
    catch (Exception e)
    {
      logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]---exception---" + e);
      result = false;
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, null, null);
    }
    return result;
  }
  public String obbAckSequence()
  {
    Connection con = null;
    LoggableStatement pps1 = null;
    ResultSet rs1 = null;
    String prodate = null;
    int seq = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      String proQuery = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE, LPAD(OBB_RBI_REQ_REQ.NEXTVAL,2,0) AS OBB_SEQNO FROM DLYPRCCYCL";
      pps1 = new LoggableStatement(con, proQuery);
      logger.info(
        "----------IDPMS ------OBBDWLD--- [processOBBjob]-----query------" + pps1.getQueryString());
      rs1 = pps1.executeQuery();
      if (rs1.next())
      {
        prodate = rs1.getString("PROCDATE");
        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]-----prodate------" + prodate);
        seq = rs1.getInt("OBB_SEQNO");
        logger.info("----------IDPMS ------OBBDWLD--- [processOBBjob]-----seq------" + seq);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pps1, rs1);
    }
    return prodate + seq;
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
  public static void recursiveDelete(File file)
  {
    if (!file.exists()) {
      return;
    }
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        recursiveDelete(f);
      }
    }
    file.delete();
    logger.info("Deleted file/folder: " + file.getAbsolutePath());
  }
}