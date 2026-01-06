package in.co.ebrc.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.ebrc.businessdelegate.pricereftomanybill.EbrcCertificateProcess;
import in.co.ebrc.utility.DBConnectionUtility;
import in.co.ebrc.utility.LoggableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
 
public class EbrcHomeAction
  extends InvoiceBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(EbrcHomeAction.class
    .getName());
  Map<String, String> ebrcStatus;
  Map<String, String> status;
  public Map<String, String> getEbrcStatus()
  {
    return this.ebrcStatus;
  }
  public void setEbrcStatus(Map<String, String> ebrcStatus)
  {
    this.ebrcStatus = REC_IND;
  }
  public Map<String, String> getstatus()
  {
    return this.status;
  }
  public void setstatus(Map<String, String> status)
  {
    this.status = REC;
  }
  public String ebrcProcess()
  {
    logger.info("Entering Method");
    EbrcCertificateProcess invProcess = null;
    try
    {
      invProcess = 
        EbrcCertificateProcess.getBD();
      setEbrcStatus(REC_IND);
      setstatus(REC);
      invProcess.setDate();
      Map<String, Object> session = ActionContext.getContext().getSession();
      session.remove("key");
      session.remove("id");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String closeWindow()
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs1 = null;
    String closeUrl = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='closeURL'";
      log = new LoggableStatement(con, query);
      rs1 = log.executeQuery();
      if (rs1.next())
      {
        logger.info("value" + rs1.getString("VALUE1"));
        closeUrl = rs1.getString("VALUE1");
        logger.info("-----" + closeUrl);
      }
      HttpServletResponse response = (HttpServletResponse)
        ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
      response.sendRedirect(closeUrl);
    }
    catch (Exception exception)
    {
      logger.info("Exception in Close Window Method--------" + exception.getMessage());
      throwApplicationException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs1);
    }
    logger.info("Exiting Method");
    return "none";
  }
}
