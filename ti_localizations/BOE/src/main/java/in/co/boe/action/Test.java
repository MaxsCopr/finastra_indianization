package in.co.boe.action;

import in.co.boe.dao.bulkUpload.BOEBulkUploadDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.ValidationUtility;
import in.co.boe.vo.ClosingBalVO;
import in.co.boe.vo.TransactionBoeBlkVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
 
public class Test
{
  private static Logger logger = Logger.getLogger(BOEBulkUploadDAO.class
    .getName());
  public static void main(String[] args)
  {
    try
    {
      ClosingBalVO clBVO = new ClosingBalVO();
      logger.info("insert Excel Data Start---");
      Test t = new Test();
      t.iterateExcelData(clBVO);
      logger.info("insert Excel Data End---");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  public void iterateExcelData(ClosingBalVO clBVO)
    throws DAOException, IOException
  {
    System.out.println("Entering Method");
    int i = 1;
    int insertedRowCount = 0;
    String batchId = "";
    HSSFWorkbook workbook = null;
    ValidationUtility validation = new ValidationUtility();
    ArrayList<ClosingBalVO> closBalList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    TransactionBoeBlkVO trans = null;
    try
    {
      trans = new TransactionBoeBlkVO();
      closBalList = new ArrayList();
      File filename = clBVO.getInputFile();
      if (filename != null)
      {
        FileInputStream mbeFile = new FileInputStream(filename);
        workbook = new HSSFWorkbook(mbeFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns();
        String[] columnsArray = sequenceOfColumns.split("\\|");
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          logger.info("rows.hasNext---" + rows.hasNext());
          String date = "";
          String accNo = "";
          String closBal = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            logger.info("Excel Headers");
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              logger.info("Excel Column count mismatched");
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++)
            {
              logger.info("Column Name from Sequence---> [" + columnCount + "]" + columnsArray[columnCount]);
              logger.info("Column Name from Excel---> [" + columnCount + "]" + rowObject.getCell(columnCount));
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString()))
              {
                logger.info("Excel Column Sequence mismatch");
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            clBVO = new ClosingBalVO();
            logger.info("row---2" + rows);
            HSSFRow rowObject = (HSSFRow)rows.next();
            logger.info("row---3" + rows);
            DataFormatter formatter = new DataFormatter();
            HSSFCell sdate = rowObject.getCell(0);
            HSSFCell saccNo = rowObject.getCell(1);
            HSSFCell sclosBal = rowObject.getCell(2);

 
            logger.info("sdate---" + sdate);
            logger.info("saccNo---" + saccNo);
            logger.info("sclosBal---" + sclosBal);
            if ((sdate != null) && 
              (sdate.getCellType() != 3))
            {
              date = formatter.formatCellValue(sdate);
              date = date.trim();
            }
            if ((saccNo != null) && 
              (saccNo.getCellType() != 3))
            {
              accNo = formatter.formatCellValue(saccNo);
              accNo = accNo.trim();
            }
            if ((sclosBal != null) && 
              (sclosBal.getCellType() != 3))
            {
              closBal = 
                formatter.formatCellValue(sclosBal);
              closBal = closBal.trim();
            }
            clBVO.setDate(date);
            clBVO.setAccNo(accNo);
            clBVO.setClosBal(closBal);
            logger.info("remarks" + clBVO.getDate());
            logger.info("remarks" + clBVO.getAccNo());
            logger.info("remarks" + clBVO.getClosBal());
            closBalList.add(clBVO);
          }
          i++;
          logger.info("i  :" + i);
        }
        logger.info("boelist size---" + closBalList.size());
        logger.info("i----" + i);
        if ((closBalList != null) && (closBalList.size() > 0)) {
          batchId = insertExcelData(closBalList);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
  public String getSequenceOfColumns()
  {
    return "Date|Account_Number|Closing_Bal";
  }
  public String insertExcelData(ArrayList<ClosingBalVO> closBalList)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ppt = null;
    ClosingBalVO excelDataVO = null;
    String batchId = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      String INSERT_BOE_BULK_STG_DATA = "INSERT INTO REP_CLOSING_BAL_FINACLE (RUN_DATE, ACCOUNT_NUMBER,CLOSING_BAL)VALUES(?, ?, ?)";
      ppt = con.prepareStatement(INSERT_BOE_BULK_STG_DATA);
      for (int i = 0; i < closBalList.size(); i++)
      {
        excelDataVO = new ClosingBalVO();
        excelDataVO = (ClosingBalVO)closBalList.get(i);
        ppt.setString(1, excelDataVO.getDate());
        ppt.setString(2, excelDataVO.getAccNo());
        ppt.setString(3, excelDataVO.getClosBal());
        ppt.addBatch();
      }
      logger.info("Insert Record ");
      ppt.executeBatch();
      logger.info("Inserted Record Successfully");
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return batchId;
  }
}