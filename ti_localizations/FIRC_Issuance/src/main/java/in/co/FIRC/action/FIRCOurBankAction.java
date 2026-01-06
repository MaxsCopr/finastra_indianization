package in.co.FIRC.action;

import in.co.FIRC.businessdelegate.FIRCOurBankBD;
import in.co.FIRC.dao.exception.ApplicationException;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.vo.AlertMessageVO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import in.co.FIRC.vo.ourBank.PrintOurBankVO;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
 
public class FIRCOurBankAction
  extends FIRCBaseAction
  implements ServletResponseAware
{
  private static Logger logger = LogManager.getLogger(FIRCOurBankAction.class.getName());
  private static final long serialVersionUID = 1L;
  OurBankVO ourBankVO;
  Map<String, String> inwardType;
  ArrayList<AlertMessageVO> errorList = null;
  ArrayList<PrintOurBankVO> printDetails = null;
  PrintOurBankVO printOurBankVO;
  private HttpServletResponse response;
  public Map<String, String> getInwardType()
  {
    return ActionConstants.INWARD_TYPE;
  }
  public void setInwardType(Map<String, String> inwardType)
  {
    this.inwardType = inwardType;
  }
  public PrintOurBankVO getPrintOurBankVO()
  {
    return this.printOurBankVO;
  }
  public void setPrintOurBankVO(PrintOurBankVO printOurBankVO)
  {
    this.printOurBankVO = printOurBankVO;
  }
  public ArrayList<PrintOurBankVO> getPrintDetails()
  {
    return this.printDetails;
  }
  public void setPrintDetails(ArrayList<PrintOurBankVO> printDetails)
  {
    this.printDetails = printDetails;
  }
  public ArrayList<AlertMessageVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessageVO> errorList)
  {
    this.errorList = errorList;
  }
  public OurBankVO getOurBankVO()
  {
    return this.ourBankVO;
  }
  public void setOurBankVO(OurBankVO ourBankVO)
  {
    this.ourBankVO = ourBankVO;
  }
  public HttpServletResponse getResponse()
  {
    return this.response;
  }
  public void setResponse(HttpServletResponse response)
  {
    this.response = response;
  }
  public String execute()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      isSessionAvailable();
      if (this.ourBankVO != null) {
        this.ourBankVO = new OurBankVO();
      }
    }
    catch (Exception exception)
    {
      logger.info("execute---------------fircOurBank-----exception" + exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String fetchOurBankDetails()
    throws ApplicationException
  {
    logger.info("Entering Method");
    FIRCOurBankBD bd = null;
    bd = new FIRCOurBankBD();
    try
    {
      if (this.ourBankVO != null)
      {
        this.ourBankVO.setMode("fetch");
        this.ourBankVO = bd.validate(this.ourBankVO);
        this.errorList = this.ourBankVO.getErrorList();
        if (this.errorList == null) {
          this.ourBankVO = bd.fetchOurBankList(this.ourBankVO);
        }
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String validateOurBankDetails()
    throws ApplicationException
  {
    logger.info("------------validateOurBankDetails--------");
    FIRCOurBankBD bd = null;
    bd = new FIRCOurBankBD();
    try
    {
      if (this.ourBankVO != null)
      {
        this.ourBankVO.setMode("validate");
        this.ourBankVO = bd.validate(this.ourBankVO);
        this.errorList = this.ourBankVO.getErrorList();
      }
    }
    catch (Exception exception)
    {
      logger.info("validateOurBankDetails----------------Exception" + exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String insertOurBankDetails()
    throws ApplicationException
  {
    logger.info("---------------insertOurBankDetails---------------");
    FIRCOurBankBD bd = null;
    bd = new FIRCOurBankBD();
    try
    {
      if (this.ourBankVO != null)
      {
        this.ourBankVO.setMode("validate");
        this.ourBankVO = bd.validate(this.ourBankVO);
        this.errorList = this.ourBankVO.getErrorList();
        if (this.errorList == null)
        {
          this.ourBankVO = bd.insertOurBankList(this.ourBankVO);
          String irmType = this.ourBankVO.getInwardType();
          if ((this.ourBankVO.getStatus() != null) && 
            (this.ourBankVO.getStatus().equalsIgnoreCase("success"))) {
            addActionMessage(irmType + " " + "ISSUANCE FORM IS SUCCESSFULLY SUBMITTED");
          } else {
            addActionError(irmType + " " + "ISSUANCE FORM IS FAILED");
          }
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("---------------insertOurBankDetails-------exception--------" + exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
}
