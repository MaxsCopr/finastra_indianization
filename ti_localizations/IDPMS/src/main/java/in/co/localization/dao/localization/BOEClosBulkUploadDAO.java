package in.co.localization.dao.localization;
 
import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstants;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.utility.ValidationUtility;
import in.co.localization.vo.localization.BOEClosBulkUploadVO;
import in.co.localization.vo.localization.TransactionClosVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
 
public class BOEClosBulkUploadDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = Logger.getLogger(BOEClosBulkUploadDAO.class
    .getName());
  static BOEClosBulkUploadDAO dao;
  public static BOEClosBulkUploadDAO getDAO()
  {
    if (dao == null) {
      dao = new BOEClosBulkUploadDAO();
    }
    return dao;
  }
  public TransactionClosVO getExcelValidate(BOEClosBulkUploadVO bulkVO)
    throws DAOException, IOException
  {
    logger.info("Entering Method");
    int i = 1;
    int insertedRowCount = 0;
    String batchId = "";
    HSSFWorkbook workbook = null;
    ValidationUtility validation = new ValidationUtility();
    ArrayList<BOEClosBulkUploadVO> boeClosList = null;
    TransactionClosVO trans = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      trans = new TransactionClosVO();
      boeClosList = new ArrayList();
      File filename = bulkVO.getInputFile();
      if (filename != null)
      {
        FileInputStream mbeFile = new FileInputStream(filename);
        workbook = new HSSFWorkbook(mbeFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns();
        String[] columnsArray = sequenceOfColumns.split("\\|");
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        String excelError = "";
        while (rows.hasNext())
        {
          String boeNo = "";String boeDate = "";String portDischarge = "";String closType = "";String adjClsInd = "";String adjClsDate = "";String approvedBy = "";String docNo = "";String docDate = "";String letterNo = "";String letterDate = "";String docPort = "";String remarks = "";String invoiceSerNo = "";String invoiceNo = "";
          String invamnt = "";String invcurr = "";String outamnt = "";String adjInvAmtIC = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            logger.info("Excel Headers");
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              logger.info("Excel Column count mismatched");
              excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString()))
              {
                logger.info("Column Name from Sequence---> [" + columnCount + "]" + columnsArray[columnCount]);
                logger.info("Column Name from Excel---> [" + columnCount + "]" + rowObject.getCell(columnCount));
                logger.info("Excel Column Sequence mismatch");
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            bulkVO = new BOEClosBulkUploadVO();
            HSSFRow rowObject = (HSSFRow)rows.next();
            DataFormatter formatter = new DataFormatter();

 
            HSSFCell billEntryNo = rowObject.getCell(0);
            HSSFCell billEntryDate = rowObject.getCell(1);
            HSSFCell portCode = rowObject.getCell(2);
            HSSFCell sClosType = rowObject.getCell(3);
            HSSFCell sAdjClsInd = rowObject.getCell(4);
            HSSFCell sAdjClsDate = rowObject.getCell(5);
            HSSFCell sApprovedBy = rowObject.getCell(6);
            HSSFCell sDocNo = rowObject.getCell(7);
            HSSFCell sDocDate = rowObject.getCell(8);
            HSSFCell sLetterNo = rowObject.getCell(9);
            HSSFCell sLetterDate = rowObject.getCell(10);
            HSSFCell sDocPort = rowObject.getCell(11);
            HSSFCell sRemarks = rowObject.getCell(12);
            HSSFCell sInvoiceSerNo = rowObject.getCell(13);
            HSSFCell sInvoiceNo = rowObject.getCell(14);
            HSSFCell sInvoiceAmnt = rowObject.getCell(15);
            HSSFCell sInvoiceCurr = rowObject.getCell(16);
            HSSFCell sOutAmnt = rowObject.getCell(17);
            HSSFCell sAdjInvAmtIC = rowObject.getCell(18);
            if ((billEntryNo != null) && 
              (billEntryNo.getCellTypeEnum() != CellType.BLANK)) {
              boeNo = formatter.formatCellValue(billEntryNo).trim();
            }
            if ((billEntryDate != null) && 
              (billEntryDate.getCellTypeEnum() != CellType.BLANK)) {
              boeDate = billEntryDate.toString().trim();
            }
            if ((portCode != null) && 
              (portCode.getCellTypeEnum() != CellType.BLANK)) {
              portDischarge = formatter.formatCellValue(portCode).trim();
            }
            if ((sClosType != null) && 
              (sClosType.getCellTypeEnum() != CellType.BLANK)) {
              closType = formatter.formatCellValue(sClosType).trim();
            }
            if ((sAdjClsInd != null) && 
              (sAdjClsInd.getCellTypeEnum() != CellType.BLANK)) {
              adjClsInd = formatter.formatCellValue(sAdjClsInd).trim();
            }
            if ((sAdjClsDate != null) && 
              (sAdjClsDate.getCellTypeEnum() != CellType.BLANK)) {
              adjClsDate = formatter.formatCellValue(sAdjClsDate).trim();
            }
            if ((sApprovedBy != null) && 
              (sApprovedBy.getCellTypeEnum() != CellType.BLANK)) {
              approvedBy = formatter.formatCellValue(sApprovedBy).trim();
            }
            if ((sDocNo != null) && 
              (sDocNo.getCellTypeEnum() != CellType.BLANK)) {
              docNo = formatter.formatCellValue(sDocNo).trim();
            }
            if ((sDocDate != null) && 
              (sDocDate.getCellTypeEnum() != CellType.BLANK)) {
              docDate = formatter.formatCellValue(sDocDate).trim();
            }
            if ((sLetterNo != null) && 
              (sLetterNo.getCellTypeEnum() != CellType.BLANK)) {
              letterNo = formatter.formatCellValue(sLetterNo).trim();
            }
            if ((sLetterDate != null) && 
              (sLetterDate.getCellTypeEnum() != CellType.BLANK)) {
              letterDate = sLetterDate.toString().trim();
            }
            if ((sDocPort != null) && 
                    (sDocPort.getCellTypeEnum() != CellType.BLANK)) {
                    docPort = formatter.formatCellValue(sDocPort).trim();
                  }
                  if ((sRemarks != null) && 
                    (sRemarks.getCellTypeEnum() != CellType.BLANK)) {
                    remarks = formatter.formatCellValue(sRemarks).trim();
                  }
                  if ((sInvoiceSerNo != null) && 
                    (sInvoiceSerNo.getCellTypeEnum() != CellType.BLANK)) {
                    invoiceSerNo = 
                      formatter.formatCellValue(sInvoiceSerNo).trim();
                  }
                  if ((sInvoiceAmnt != null) && 
                    (sInvoiceAmnt.getCellTypeEnum() != CellType.BLANK)) {
                    invamnt = formatter.formatCellValue(sInvoiceAmnt).trim();
                  }
                  if ((sInvoiceCurr != null) && 
                    (sInvoiceCurr.getCellTypeEnum() != CellType.BLANK)) {
                    invcurr = formatter.formatCellValue(sInvoiceCurr).trim();
                  }
                  if ((sOutAmnt != null) && 
                    (sOutAmnt.getCellTypeEnum() != CellType.BLANK)) {
                    outamnt = formatter.formatCellValue(sOutAmnt).trim();
                  }
                  if ((sInvoiceNo != null) && 
                    (sInvoiceNo.getCellTypeEnum() != CellType.BLANK)) {
                    invoiceNo = formatter.formatCellValue(sInvoiceNo).trim();
                  }
                  if ((sAdjInvAmtIC != null) && 
                    (sAdjInvAmtIC.getCellTypeEnum() != CellType.BLANK)) {
                    adjInvAmtIC = 
                      formatter.formatCellValue(sAdjInvAmtIC).trim();
                  }
                  bulkVO.setBoeNo(boeNo);
                  bulkVO.setBoeDate(boeDate);
                  bulkVO.setPortCode(portDischarge);
                  bulkVO.setClosType(closType);
                  bulkVO.setAdjClsInd(adjClsInd);
                  bulkVO.setAdjClsDate(adjClsDate);
                  bulkVO.setApprovedBy(approvedBy);
                  bulkVO.setDocNo(docNo);
                  bulkVO.setDocDate(docDate);
                  bulkVO.setLetterNo(letterNo);
                  bulkVO.setLetterDate(letterDate);
                  bulkVO.setDocPort(docPort);
                  bulkVO.setRemarks(remarks);
                  bulkVO.setInvoiceSerNo(invoiceSerNo);
                  bulkVO.setInvoiceNo(invoiceNo);
                  bulkVO.setInvamnt(invamnt);
                  bulkVO.setInvcurr(invcurr);
                  bulkVO.setOutamnt(outamnt);
                  bulkVO.setAdjInvAmtIC(adjInvAmtIC);

       
       
       
                  boeClosList.add(bulkVO);
                }
                i++;
              }
              if ((boeClosList != null) && (boeClosList.size() > 0))
              {
                ArrayList<TransactionClosVO> errorData = validation
                  .valClosBulkUpload(boeClosList);
                if ((errorData == null) || (errorData.size() == 0))
                {
                  batchId = insertExcelData(boeClosList);
                  insertedRowCount = validateExcel(batchId);
                  if (insertedRowCount > 0)
                  {
                    ArrayList<TransactionClosVO> boeList = getBulkStgData(batchId);
                    trans.setBoeList(boeList);
                    trans.setBatchId(batchId);
                  }
                }
                else
                {
                  trans.setErrorCodeDesc(errorData);
                  trans.setBoeClsvoList(boeClosList);
                  logger.info("trans.getBoeClsvoList()" + 
                    trans.getBoeClsvoList().size());
                }
              }
            }
            if ((excelErrorList != null) && (excelErrorList.size() > 0)) {
              trans.setErrorList(excelErrorList);
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
        private String getSequenceOfColumns()
        {
          return "BOENUMBER|BOEDATE|PORTCODE|CLOS_TYPE|ADJ_CLS_IND|ADJ_CLS_DATE|APPROVED_BY|DOC_NO|DOC_DATE|LETTER_NO|LETTER_DATE|DOC_PORT|REMARKS|INV_SER_NO|INV_NO|INV_AMNT|INV_CURR|OUTSTANDING_AMNT|ADJ_INV_AMT_IC";
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
              String procedureQuery = "{call ETT_BULK_CLOS_BOE_VAL_DATA(?)}";
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
            DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, 
              aResultSet);
          }
          logger.info("Exiting Method");
          return insertedRowCount;
        }
        public String insertExcelData(ArrayList<BOEClosBulkUploadVO> BOEClosList)
          throws DAOException
        {
          logger.info("Entering Method");
          Connection con = null;
          ResultSet rs = null;
          PreparedStatement ppt = null;
          BOEClosBulkUploadVO excelDataVO = null;
          String batchId = null;
          CommonMethods commonMethods = null;
          try
          {
            con = DBConnectionUtility.getConnection();
            commonMethods = new CommonMethods();
            HttpSession session = ServletActionContext.getRequest()
              .getSession();
            String userId = (String)session.getAttribute("loginedUserId");
            String query = "SELECT ETT_BULK_BOE_CLOS_SEQ_NO AS BATCHID FROM DUAL";
            LoggableStatement pst = new LoggableStatement(con, query);
            ResultSet rs1 = pst.executeQuery();
            if (rs1.next()) {
              batchId = rs1.getString("BATCHID");
            }
            pst.close();
            rs1.close();
            ppt = con.prepareStatement("INSERT INTO ETT_BULK_CLOSE_BOE_STG_DAT(BOENUMBER, BOEDATE, PORTCODE, CLOS_TYPE, ADJ_CLS_IND, ADJ_CLS_DATE, DOC_NO, DOC_DATE, DOC_PORT, LETTER_NO, LETTER_DATE, INV_SER_NO, INV_NO, INV_AMNT,INV_CURR,OUTSTANDING_AMNT,APPROVED_BY, ADJ_INV_AMT_IC, REMARKS, BATCHID, USERID)VALUES( ?, TO_DATE(?, 'DD/MM/YYYY'), ?, ?, ?, TO_DATE(?,'DD/MM/YYYY'), ?, TO_DATE(?, 'DD/MM/YYYY'), ?, ?, TO_DATE(?, 'DD/MM/YYYY'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < BOEClosList.size(); i++)
            {
              excelDataVO = new BOEClosBulkUploadVO();
              excelDataVO = (BOEClosBulkUploadVO)BOEClosList.get(i);
              if ((!commonMethods.isNull(excelDataVO.getBoeNo())) && 
                (!commonMethods.isNull(excelDataVO.getBoeDate())) && 
                (!commonMethods.isNull(excelDataVO.getPortCode())) && 
                (!commonMethods.isNull(excelDataVO.getInvoiceNo())))
              {
                ppt.setString(1, excelDataVO.getBoeNo());
                ppt.setString(2, excelDataVO.getBoeDate());
                ppt.setString(3, excelDataVO.getPortCode());
                ppt.setString(4, excelDataVO.getClosType());
                ppt.setString(5, excelDataVO.getAdjClsInd());
                ppt.setString(6, excelDataVO.getAdjClsDate());
                ppt.setString(7, excelDataVO.getDocNo());
                ppt.setString(8, excelDataVO.getDocDate());
                ppt.setString(9, excelDataVO.getDocPort());
                ppt.setString(10, excelDataVO.getLetterNo());
                ppt.setString(11, excelDataVO.getLetterDate());
                ppt.setString(12, excelDataVO.getInvoiceSerNo());
                ppt.setString(13, excelDataVO.getInvoiceNo());
                ppt.setString(14, excelDataVO.getInvamnt());
                ppt.setString(15, excelDataVO.getInvcurr());
                ppt.setString(16, excelDataVO.getOutamnt());
                ppt.setString(17, excelDataVO.getApprovedBy());
                ppt.setString(18, excelDataVO.getAdjInvAmtIC());

       
       
       
                ppt.setString(19, excelDataVO.getRemarks());
                ppt.setString(20, batchId);
                ppt.setString(21, userId);
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
        public ArrayList<TransactionClosVO> getBulkStgData(String batchId)
          throws DAOException
        {
          logger.info("Entering Method");
          Connection con = null;
          LoggableStatement pst = null;
          ResultSet rs = null;
          String sqlQuery = null;
          CommonMethods commonMethods = null;
          ArrayList<TransactionClosVO> boeList = null;
          try
          {
            commonMethods = new CommonMethods();
            boeList = new ArrayList();
            con = DBConnectionUtility.getConnection();
            sqlQuery = "SELECT BOENUMBER, TO_CHAR(BOEDATE,'DD/MM/YYYY') BOEDATE, PORTCODE, DECODE(CLOS_TYPE,'C','Close', 'O','Open','') CLOS_TYPE, DECODE(ADJ_CLS_IND,'1','Destroy of Goods', '2','Sort Shipment','3','Quality Issue','4','Re-import','5','Re-export','6','Set-off/Net off','7','Others','8','BOE Prior To 10/10/2016','') ADJ_CLS_IND, TO_CHAR(ADJ_CLS_DATE,'DD/MM/YYYY') ADJ_CLS_DATE, DOC_NO, TO_DATE(DOC_DATE,'DD/MM/YYYY') DOC_DATE, DOC_PORT, LETTER_NO,  TO_CHAR(LETTER_DATE,'DD/MM/YYYY') LETTER_DATE, INV_SER_NO, INV_NO,INV_AMNT,INV_CURR,OUTSTANDING_AMNT, DECODE(APPROVED_BY, '1','RBI','2','AD Bank','3','Other','') APPRIVED_BY, ADJ_INV_AMT_IC,   REMARKS, ERRORDTLS FROM ETT_BULK_CLOSE_BOE_STG_DAT WHERE BATCHID = ?";
            pst = new LoggableStatement(con, sqlQuery);
            pst.setString(1, batchId);
            rs = pst.executeQuery();
            while (rs.next())
            {
              TransactionClosVO boe = new TransactionClosVO();
              boe.setBoeNo(rs.getString("BOENUMBER"));
              boe.setBoeDate(rs.getString("BOEDATE"));
              boe.setPortCode(rs.getString("PORTCODE"));
              boe.setClosureType(rs.getString("CLOS_TYPE"));
              boe.setAdjClsInd(rs.getString("ADJ_CLS_IND"));
              boe.setAdjClsDate(rs.getString("ADJ_CLS_DATE"));
              boe.setDocNo(rs.getString("DOC_NO"));
              boe.setDocDate(rs.getString("DOC_DATE"));
              boe.setDocPortCode(rs.getString("DOC_PORT"));
              boe.setLetterNo(rs.getString("LETTER_NO"));
              boe.setLetterDate(rs.getString("LETTER_DATE"));
              boe.setInvoiceSerNo(rs.getString("INV_SER_NO"));
              boe.setInvoiceNo(rs.getString("INV_NO"));
              boe.setInvamnt(rs.getString("INV_AMNT"));
              boe.setInvcurr(rs.getString("INV_CURR"));
              boe.setOutamnt(rs.getString("OUTSTANDING_AMNT"));
              boe.setApprovedBy(rs.getString("APPRIVED_BY"));
              boe.setAdjInvoiceAmtIC(rs.getString("ADJ_INV_AMT_IC"));

       
       
       
              boe.setRemarks(rs.getString("REMARKS"));
              boe.setErrDesc(rs.getString("ERRORDTLS"));
              if (commonMethods.isNull(boe.getErrDesc())) {
                boe.setStatus("SUCCESS");
              } else {
                boe.setStatus("FAIL");
              }
              boeList.add(boe);
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
          return boeList;
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
            String delQuery = "DELETE FROM ETT_BULK_CLOSE_BOE_STG_DAT where BATCHID =?";
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
              String sqlQuery = "SELECT COUNT(*) FROM ETT_BULK_CLOSE_BOE_STG_DAT WHERE BATCHID = ? AND ERRORDTLS IS NULL";
              pst = new LoggableStatement(aConnection, sqlQuery);
              pst.setString(1, batchId);
              aResultSet = pst.executeQuery();
              if (aResultSet.next()) {
                count = aResultSet.getInt(1);
              }
              if (count > 0)
              {
                String procedureQuery = "{call ETT_BULK_BOE_CLOSE_INS_TBL(?)}";
                aCallableStatement = aConnection
                  .prepareCall(procedureQuery);
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
            DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, 
              aResultSet);
            pst.close();
          }
          logger.info("Exiting Method");
          return count;
        }
      }