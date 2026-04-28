package in.co.boe.dao.bulkUpload;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.utility.ValidationUtility;
import in.co.boe.vo.OBBBulkUploadVO;
import in.co.boe.vo.TransactionVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class OBBBulkUploadDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = Logger.getLogger(OBBBulkUploadDAO.class.getName());
  static OBBBulkUploadDAO dao;
  public static OBBBulkUploadDAO getDAO()
  {
    if (dao == null) {
      dao = new OBBBulkUploadDAO();
    }
    return dao;
  }
  public String getSequenceOfColumns()
  {
    return "portOfDischarge|importAgency|billOfEntryNumber|billOfEntryDate|ADCode|IECode|IEName";
  }
  public TransactionVO getExcelValidate(OBBBulkUploadVO bulkVO)
    throws DAOException, IOException
  {
    logger.info("Entering Method");
    int insertedRowCount = 0;
    String batchId = "";
    ValidationUtility validation = new ValidationUtility();
    ArrayList<OBBBulkUploadVO> OBBList = null;
    TransactionVO trans = null;
    String fileNameRef = "";
    int result = 0;
    try
    {
      trans = new TransactionVO();
      OBBList = new ArrayList();
      File file = bulkVO.getInputFile();
      if (file != null)
      {
        fileNameRef = bulkVO.getFileNameRef();
        String Fileext = fileNameRef.substring(fileNameRef.length() - 3, fileNameRef.length());
        String s = bulkVO.getInputFile().getAbsolutePath();
        logger.info("s :: " + s);
        BufferedReader br = null;
        String line = null;
        String splitby = "\\|";
        int i = 1;
        String sequenceOfColumns = getSequenceOfColumns();
        logger.info("sequenceOfColumns :: " + sequenceOfColumns);
        String[] columnsArray = sequenceOfColumns.split(splitby);

 
        String boeNo = "";String boeDate = "";String portDischarge = "";String impAgency = "";String adCode = "";
        String ieCode = "";String ieName = "";
        if (Fileext.equalsIgnoreCase("csv"))
        {
          result = 1;
          br = new BufferedReader(new FileReader(s));
          Map<String, Integer> map = new HashMap();
          try
          {
            while ((line = br.readLine()) != null) {
              if (i == 1)
              {
                String[] text = line.split(",");
                for (int j = 0; j < 7; j++) {
                  map.put(text[j], Integer.valueOf(j));
                }
                i++;
                logger.info("inside  if :: " + i);
                if (columnsArray.length != text.length)
                {
                  result = 2;
                  break;
                }
                for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
                  if (!columnsArray[columnCount].toString().equalsIgnoreCase(text[columnCount].toString())) {
                    result = 3;
                  } else {
                    if (result == 3) {
                      break;
                    }
                  }
                }
              }
              else
              {
                if (result != 1) {
                  break;
                }
                bulkVO = new OBBBulkUploadVO();
                String[] text = line.split(",", -1);
                portDischarge = text[((Integer)map.get("portOfDischarge")).intValue()];
                impAgency = text[((Integer)map.get("importAgency")).intValue()];
                boeNo = text[((Integer)map.get("billOfEntryNumber")).intValue()];
                boeDate = text[((Integer)map.get("billOfEntryDate")).intValue()];
                adCode = text[((Integer)map.get("ADCode")).intValue()];
                ieCode = text[((Integer)map.get("IECode")).intValue()];
                ieName = text[((Integer)map.get("IEName")).intValue()];
                i++;
                logger.info("inside  else i :: " + i);
                bulkVO.setBoeNo(boeNo.trim());
                bulkVO.setBoeDate(boeDate.trim());
                bulkVO.setPortCode(portDischarge.trim());
                bulkVO.setImAgency(impAgency.trim());
                bulkVO.setAdCode(adCode.trim());
                bulkVO.setIeCode(ieCode.trim());
                bulkVO.setIeName(ieName.trim());
                OBBList.add(bulkVO);
              }
            }
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
          br.close();
        }
      }
      logger.info("Result - " + result);
      if ((OBBList != null) && 
        (OBBList.size() > 0))
      {
        ArrayList<TransactionVO> errorData = validation.obbValBulkUpload(OBBList);
        if ((errorData == null) || (errorData.size() == 0))
        {
          batchId = insertExcelData(OBBList);
          insertedRowCount = validateExcel(batchId);
          if (insertedRowCount > 0)
          {
            ArrayList<TransactionVO> boeList = getBulkStgData(batchId);
            trans.setBoeList(boeList);
            trans.setBatchId(batchId);
          }
        }
        else
        {
          trans.setErrorCodeDesc(errorData);
          logger.info("OBBList size ===>" + OBBList.size());
          trans.setObbvoList(OBBList);
          logger.info("trans.getObbvoList().size()" + trans.getObbvoList().size());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return trans;
  }
  private int validateExcel(String batchId)
  {
    logger.info("Entering Method");
    int insertedRowCount = 0;
    ResultSet aResultSet = null;
    Connection aConnection = null;
    CallableStatement aCallableStatement = null;
    try
    {
      aConnection = DBConnectionUtility.getConnection();
      if (aConnection != null)
      {
        String procedureQuery = "{call ETT_BULK_OBB_BOE_VAL_DATA(?)}";
        aCallableStatement = aConnection.prepareCall(procedureQuery);
        aCallableStatement.setString(1, batchId);
        insertedRowCount = aCallableStatement.executeUpdate();
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, aResultSet);
    }
    logger.info("Exiting Method");
    return insertedRowCount;
  }
  public String insertExcelData(ArrayList<OBBBulkUploadVO> OBBList)
		    throws DAOException
		  {
		    logger.info("Entering Method");
		    String sAdCode = "";
		    String sIeCode = "";
		    String sIeName = "";
		    String sImAgency = "";
		    Connection con = null;
		    ResultSet rs = null;
		    PreparedStatement ppt = null;
		    OBBBulkUploadVO excelDataVO = null;
		    String batchId = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      commonMethods = new CommonMethods();
		      HttpSession session = ServletActionContext.getRequest().getSession();
		      String userId = (String)session.getAttribute("loginedUserId");

		 
		 
		      String query = "SELECT ETT_BULK_OBB_BOE_SEQ_NO_FN AS BATCHID FROM DUAL";
		      LoggableStatement pst = new LoggableStatement(con, query);
		      ResultSet rs1 = pst.executeQuery();
		      if (rs1.next()) {
		        batchId = rs1.getString("BATCHID");
		      }
		      pst.close();
		      ppt = con.prepareStatement("INSERT INTO ETT_BULK_OBB_BOE_STG_DAT(BOENUM, BOEDATE, PORTCODE, BOE_IMPORT_AGENCY ,ADCODE, IE_CODE, IE_NAME, BATCHID, USERID) VALUES(?, TO_DATE(?,'DD/MM/YYYY'), ?, ?, ?, ?, ?, ?, ?)");
		      for (int i = 0; i < OBBList.size(); i++)
		      {
		        excelDataVO = new OBBBulkUploadVO();
		        excelDataVO = (OBBBulkUploadVO)OBBList.get(i);
		        if ((!commonMethods.isNull(excelDataVO.getBoeNo())) && (!commonMethods.isNull(excelDataVO.getBoeDate())) && 
		          (!commonMethods.isNull(excelDataVO.getPortCode())))
		        {
		          excelDataVO = fetchCIFDetails(excelDataVO);
		          if (!excelDataVO.getAdCode().isEmpty()) {
		            sAdCode = excelDataVO.getAdCode();
		          }
		          if (!excelDataVO.getIeCode().isEmpty()) {
		            sIeCode = excelDataVO.getIeCode();
		          }
		          if (!excelDataVO.getIeName().isEmpty()) {
		            sIeName = excelDataVO.getIeName();
		          }
		          if (!excelDataVO.getImAgency().isEmpty()) {
		            sImAgency = excelDataVO.getImAgency();
		          }
		          ppt.setString(1, excelDataVO.getBoeNo());
		          ppt.setString(2, excelDataVO.getBoeDate());
		          ppt.setString(3, excelDataVO.getPortCode());
		          if (!sImAgency.equals(""))
		          {
		            int imAgency = (int)Double.parseDouble(sImAgency);
		            ppt.setString(4, String.valueOf(imAgency));
		          }
		          else
		          {
		            ppt.setString(4, sImAgency);
		          }
		          ppt.setString(5, sAdCode);
		          ppt.setString(6, sIeCode);
		          ppt.setString(7, sIeName);
		          ppt.setString(8, batchId);
		          ppt.setString(9, userId);
		          ppt.addBatch();
		        }
		      }
		      logger.info("Insert Record ");
		      ppt.executeBatch();
		      logger.info("Inserted Record Successfully");
		    }
		    catch (Exception exception)
		    {
		      exception.printStackTrace();
		      throwDAOException(exception);
		    }
		    finally
		    {
		      closeSqlRefferance(rs, ppt, con);
		    }
		    logger.info("Exiting Method");
		    return batchId;
		  }
		  public OBBBulkUploadVO fetchCIFDetails(OBBBulkUploadVO boeVO)
		    throws DAOException
		  {
		    logger.info("Entering Method");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    String sqlQuery = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      commonMethods = new CommonMethods();
		      con = DBConnectionUtility.getConnection();
		      sqlQuery = "SELECT TRIM(EXT.CUST) AS CUST,TRIM(GF.GFCUN) AS CUST_NAME,TRIM(SX.SVNA1) AS IE_ADDR1,TRIM(SX.SVNA2) AS IE_ADDR2,TRIM(SX.SVNA3) AS IE_ADDR3,TRIM(SX.SVNA4) AS IE_ADDR4,TRIM(SX.SVNA5) AS IE_ADDR5,TRIM(EXT.PANNO) AS PANNO FROM EXTCUST EXT,GFPF GF,SX20LF SX WHERE EXT.CUST = SX.SXCUS1 AND EXT.CUST = GF.GFCUS1 AND EXT.IECODE = '" + 

		 
		        commonMethods.getEmptyIfNull(boeVO.getIeCode()).trim() + "'";
		      pst = new LoggableStatement(con, sqlQuery);
		      rs = pst.executeQuery();
		      if (rs.next())
		      {
		        boeVO.setIename(rs.getString("CUST_NAME"));
		        boeVO.setIepan(rs.getString("PANNO"));
		      }
		    }
		    catch (SQLException e)
		    {
		      e.printStackTrace();
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst, con);
		    }
		    logger.info("Exiting Method");
		    return boeVO;
		  }
		  public ArrayList<TransactionVO> getBulkStgData(String batchId)
		    throws DAOException
		  {
		    logger.info("Entering Method");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    String sqlQuery = null;
		    CommonMethods commonMethods = null;
		    ArrayList<TransactionVO> invoiceList = null;
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    try
		    {
		      commonMethods = new CommonMethods();
		      invoiceList = new ArrayList();
		      con = DBConnectionUtility.getConnection();
		      sqlQuery = "select * from ETT_BULK_OBB_BOE_STG_DAT where BATCHID =?";
		      pst = new LoggableStatement(con, sqlQuery);
		      pst.setString(1, batchId);
		      rs = pst.executeQuery();
		      while (rs.next())
		      {
		        TransactionVO boe = new TransactionVO();
		        logger.info("BOENUM : =====================>>> : " + rs.getString("BOENUM"));
		        logger.info("BOEDATE : =====================>>> : " + sdf.format(rs.getDate("BOEDATE")));
		        logger.info("PORTCODE : =====================>>> : " + rs.getString("PORTCODE"));
		        logger.info("BOE_IMPORT_AGENCY : =====================>>> : " + rs.getString("BOE_IMPORT_AGENCY"));
		        logger.info("ADCODE : =====================>>> : " + rs.getString("ADCODE"));
		        logger.info("IE_CODE : =====================>>> : " + rs.getString("IE_CODE"));
		        logger.info("IE_NAME : =====================>>> : " + rs.getString("IE_NAME"));
		        boe.setBoeNo(rs.getString("BOENUM"));
		        boe.setBoeDate(sdf.format(rs.getDate("BOEDATE")));
		        boe.setPortCode(rs.getString("PORTCODE"));
		        boe.setImpagc(rs.getString("BOE_IMPORT_AGENCY"));
		        boe.setAdcode(rs.getString("ADCODE"));
		        boe.setIeCode(rs.getString("IE_CODE"));
		        boe.setIename(rs.getString("IE_NAME"));
		        boe.setErrDesc(rs.getString("ERRORDTLS"));
		        if (commonMethods.isNull(boe.getErrDesc())) {
		          boe.setStatus("SUCCESS");
		        } else {
		          boe.setStatus("FAIL");
		        }
		        invoiceList.add(boe);
		      }
		    }
		    catch (SQLException e)
		    {
		      e.printStackTrace();
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst, con);
		    }
		    logger.info("Exiting Method");
		    return invoiceList;
		  }
		  public int rejectData(String batchId)
		    throws DAOException
		  {
		    logger.info("Entering Method");
		    Connection con = null;
		    LoggableStatement pst = null;
		    int delCount = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      String delQuery = "DELETE FROM ETT_BULK_OBB_BOE_STG_DAT where BATCHID =?";
		      LoggableStatement pst1 = new LoggableStatement(con, delQuery);
		      pst1.setString(1, batchId);
		      delCount = pst1.executeUpdate();
		      logger.info("Delete Count --->" + delCount);
		      closePreparedStatement(pst1);
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		      throwDAOException(e);
		    }
		    finally
		    {
		      closeSqlRefferance(pst, con);
		    }
		    logger.info("Exiting Method");
		    return delCount;
		  }
		  public int uploadDataToTable(String batchId)
				    throws SQLException
				  {
				    logger.info("Entering Method");
				    ResultSet aResultSet = null;
				    Connection aConnection = null;
				    CallableStatement aCallableStatement = null;
				    LoggableStatement pst = null;
				    int count = 0;
				    try
				    {
				      aConnection = DBConnectionUtility.getConnection();
				      if (aConnection != null)
				      {
				        String sqlQuery = "SELECT COUNT(*) FROM ETT_BULK_OBB_BOE_STG_DAT WHERE BATCHID = ? AND ERRORDTLS IS NULL";
				        pst = new LoggableStatement(aConnection, sqlQuery);
				        pst.setString(1, batchId);
				        aResultSet = pst.executeQuery();
				        if (aResultSet.next()) {
				          count = aResultSet.getInt(1);
				        }
				        if (count > 0)
				        {
				          String procedureQuery = "{call ETT_BULK_OBB_BOE_INS_TBL(?)}";
				          aCallableStatement = aConnection.prepareCall(procedureQuery);
				          aCallableStatement.setString(1, batchId);
				          aCallableStatement.executeUpdate();
				        }
				      }
				    }
				    catch (SQLException ex)
				    {
				      ex.printStackTrace();
				    }
				    catch (Exception ex)
				    {
				      ex.printStackTrace();
				    }
				    finally
				    {
				      DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, aResultSet);
				      pst.close();
				    }
				    logger.info("Exiting Method");
				    return count;
				  }
				}