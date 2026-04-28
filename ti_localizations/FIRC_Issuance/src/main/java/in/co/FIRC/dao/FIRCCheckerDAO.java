package in.co.FIRC.dao;

import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.utility.ActionConstantsQuery;
import in.co.FIRC.utility.CommonMethods;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.vo.ourBank.IssuanceVO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class FIRCCheckerDAO
  extends DBConnectionUtility
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = LogManager.getLogger(FIRCCheckerDAO.class.getName());
  static FIRCCheckerDAO dao;
  public static FIRCCheckerDAO getDAO()
  {
    if (dao == null) {
      dao = new FIRCCheckerDAO();
    }
    return dao;
  }
  public String getIssuanceList(IssuanceVO issuanceVO)
  {
    CommonMethods commonMethods = null;
    String query = "SELECT ISSUING_BANK_NAME,ISSUING_BNK_BRANCH,FIRC_SERIAL_NO,TRANS_REF_NO, BENFICIARY_ADDRESS,PUR_CODE,ORDER_CUSTOMER, AMOUNT,CURRENCY,EXCHANGE_RATE,AVAILABLE_AMOUNT,PAID_AMOUNT, PAID_CURRENCY,INWARD_TYPE,STATUS,REMARKS,USERID FROM ETT_FIRC_LODGEMENT WHERE STATUS = 'P' ";

 
 
 
    commonMethods = new CommonMethods();
    String fircNo = commonMethods.getEmptyIfNull(issuanceVO.getFircno()).trim();
    if (!CommonMethods.isNull(fircNo)) {
      query = query + " AND FIRC_SERIAL_NO ='" + fircNo + "'";
    }
    String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
    if (!CommonMethods.isNull(transRefNo)) {
      query = query + " AND TRANS_REF_NO ='" + transRefNo + "'";
    }
    return query;
  }
  public ArrayList<OurBankVO> fetchPendingOurBankDetails(IssuanceVO issuanceVO)
    throws DAOException
  {
    logger.info("--------------------fetchPendingOurBankDetails--------------");
    ArrayList<OurBankVO> issuanceList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    OurBankVO ackVO = null;
    CommonMethods commonMethods = null;
    String loginedUserId = null;
    try
    {
      commonMethods = new CommonMethods();
      HttpSession session = ServletActionContext.getRequest().getSession();
      loginedUserId = (String)session.getAttribute("loginedUserName");
      issuanceList = new ArrayList();
      con = DBConnectionUtility.getConnection();

 
 
 
 
      query = "SELECT ISSUING_BANK_NAME,ISSUING_BNK_BRANCH,FIRC_SERIAL_NO,TRANS_REF_NO, BENFICIARY_ADDRESS,PUR_CODE,ORDER_CUSTOMER, AMOUNT,CURRENCY,EXCHANGE_RATE,AVAILABLE_AMOUNT,PAID_AMOUNT, PAID_CURRENCY,INWARD_TYPE,STATUS,REMARKS,USERID FROM ETT_FIRC_LODGEMENT WHERE STATUS = 'P' ";

 
 
 
      commonMethods = new CommonMethods();
      String fircNo = commonMethods.getEmptyIfNull(issuanceVO.getFircno()).trim();
      if (!CommonMethods.isNull(fircNo))
      {
        query = query + " AND FIRC_SERIAL_NO = ? ";
        pst = new LoggableStatement(con, query);
        pst.setString(1, fircNo);
      }
      String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
      if (!CommonMethods.isNull(transRefNo))
      {
        query = query + " AND TRANS_REF_NO = ? ";
        pst = new LoggableStatement(con, query);
        pst.setString(1, transRefNo);
      }
      logger.info("--------------------fetchPendingOurBankDetails-----query---------" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ackVO = new OurBankVO();
        ackVO.setIssuingBank(commonMethods.getEmptyIfNull(rs.getString("ISSUING_BANK_NAME")).trim());
        ackVO.setIssunibranch(commonMethods.getEmptyIfNull(rs.getString("ISSUING_BNK_BRANCH")).trim());
        ackVO.setTransrefno(commonMethods.getEmptyIfNull(rs.getString("TRANS_REF_NO")).trim());
        ackVO.setFircno(commonMethods.getEmptyIfNull(rs.getString("FIRC_SERIAL_NO")).trim());
        ackVO.setBenificiarydetails(commonMethods.getEmptyIfNull(rs.getString("BENFICIARY_ADDRESS")).trim());
        ackVO.setPurposecode(commonMethods.getEmptyIfNull(rs.getString("PUR_CODE")).trim());
        ackVO.setOrderingcustomer(commonMethods.getEmptyIfNull(rs.getString("ORDER_CUSTOMER")).trim());
        ackVO.setAmount(commonMethods.getEmptyIfNull(rs.getString("AMOUNT")).trim());
        ackVO.setCurrency(commonMethods.getEmptyIfNull(rs.getString("CURRENCY")).trim());
        ackVO.setExchange_rate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
        ackVO.setAvailable_amt(commonMethods.getEmptyIfNull(rs.getString("AVAILABLE_AMOUNT")).trim());
        ackVO.setPaidamount(commonMethods.getEmptyIfNull(rs.getString("PAID_AMOUNT")).trim());
        ackVO.setPaidcurrency(commonMethods.getEmptyIfNull(rs.getString("PAID_CURRENCY")).trim());
        ackVO.setInwardType(commonMethods.getEmptyIfNull(rs.getString("INWARD_TYPE")).trim());
        ackVO.setRemarks(commonMethods.getEmptyIfNull(rs.getString("REMARKS")).trim());
        ackVO.setStatus(commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim());
        String makerUserId = commonMethods.getEmptyIfNull(rs.getString("USERID")).trim();
        if ((loginedUserId != null) && (loginedUserId.equalsIgnoreCase(makerUserId))) {
          ackVO.setCheckBoxDisabled("true");
        } else {
          ackVO.setCheckBoxDisabled("false");
        }
        issuanceList.add(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("--------------------fetchPendingOurBankDetails---------exception-----" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return issuanceList;
  }
  public String updateStatus(String[] chkList, String check, String remarks)
    throws DAOException
  {
    logger.info("------------updateStatus-----------");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = "fail";
    int count = 0;
    String loginedUserId = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      loginedUserId = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserId - " + loginedUserId);
      if ((chkList != null) && (check != null)) {
        for (int i = 0; i < chkList.length; i++)
        {
          String fircNo = chkList[i];
          if (check.equalsIgnoreCase("approve"))
          {
            String query = "UPDATE ETT_FIRC_LODGEMENT SET STATUS ='A',UPDATED_BY=?,CHECKER_REMARKS=?, LASTUPDATE=SYSTIMESTAMP WHERE FIRC_SERIAL_NO =?";
            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks);
            loggableStatement.setString(3, fircNo);
            logger.info("UPDATE count Query------APPROVE-------- - " + loggableStatement.getQueryString());
            count = loggableStatement.executeUpdate();
            logger.info("count - " + count);
          }
          else if (check.equalsIgnoreCase("reject"))
          {
            String query = "UPDATE ETT_FIRC_LODGEMENT SET STATUS ='R',UPDATED_BY=?,CHECKER_REMARKS=?, LASTUPDATE=SYSTIMESTAMP WHERE FIRC_SERIAL_NO =?";
            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks);
            loggableStatement.setString(3, fircNo);
            logger.info("UPDATE Query---------REJECT----- - " + loggableStatement.getQueryString());
            count = loggableStatement.executeUpdate();
            logger.info("count - " + count);
          }
        }
      }
      if (count > 0) {
        result = "success";
      }
      logger.info("update counts------------: " + count);
    }
    catch (Exception e)
    {
      logger.info("Exception in updatestatus of DAO - " + e.getMessage());
      e.getMessage();
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return result;
  }
}