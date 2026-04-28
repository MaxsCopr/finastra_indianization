package in.co.boe.action;

import in.co.boe.businessdelegate.boe_checker.BoeCheckerBD;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.exception.ApplicationException;
import in.co.boe.vo.BOEDataVO;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.TransactionVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BoeCheckerAction
  extends BoeBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(BoeCheckerAction.class.getName());
  ArrayList<TransactionVO> multiPaymentReferenceList = null;
  ArrayList<TransactionVO> multiBillNumbersList = null;
  String check = null;
  String[] chkList = null;
  String remarks = null;
  String boeData = null;
  BOEDataVO boeVO = null;
  BOESearchVO boeSearchVO = null;
  ArrayList<TransactionVO> invoiceList1;
  public ArrayList<TransactionVO> getInvoiceList1()
  {
    return this.invoiceList1;
  }
  public void setInvoiceList1(ArrayList<TransactionVO> invoiceList1)
  {
    this.invoiceList1 = invoiceList1;
  }
  public BOESearchVO getBoeSearchVO()
  {
    return this.boeSearchVO;
  }
  public void setBoeSearchVO(BOESearchVO boeSearchVO)
  {
    this.boeSearchVO = boeSearchVO;
  }
  public BOEDataVO getBoeVO()
  {
    return this.boeVO;
  }
  public void setBoeVO(BOEDataVO boeVO)
  {
    this.boeVO = boeVO;
  }
  public String getBoeData()
  {
    return this.boeData;
  }
  public void setBoeData(String boeData)
  {
    this.boeData = boeData;
  }
  public String getCheck()
  {
    return this.check;
  }
  public void setCheck(String check)
  {
    this.check = check;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String[] getChkList()
  {
    return this.chkList;
  }
  public void setChkList(String[] chkList)
  {
    this.chkList = chkList;
  }
  public ArrayList<TransactionVO> getMultiBillNumbersList()
  {
    return this.multiBillNumbersList;
  }
  public void setMultiBillNumbersList(ArrayList<TransactionVO> multiBillNumbersList)
  {
    this.multiBillNumbersList = multiBillNumbersList;
  }
  public ArrayList<TransactionVO> getMultiPaymentReferenceList()
  {
    return this.multiPaymentReferenceList;
  }
  public void setMultiPaymentReferenceList(ArrayList<TransactionVO> multiPaymentReferenceList)
  {
    this.multiPaymentReferenceList = multiPaymentReferenceList;
  }
  public String loadMultiPaymentReferenceData()
    throws ApplicationException
  {
    logger.info("Welcome to Inside of loadMultiPaymentReferenceData() ===========>>> ACTION ");
    logger.info("Entering Method");
    BoeCheckerBD boeCheckerBD = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      logger.info("The Session value is of login Type is : 1 : " + session.getAttribute("loginType"));
      logger.info("The Session value is of login Type is : 2 : " + session.getAttribute("xMstRefNum"));
      boeCheckerBD = BoeCheckerBD.getBD();
      logger.info("boeSearchVO" + this.boeSearchVO);
      if (this.boeSearchVO != null)
      {
        this.multiPaymentReferenceList = boeCheckerBD.loadMultiPaymentReferenceData(this.boeSearchVO);
        logger.info("This is Reference List : Top : " + this.multiPaymentReferenceList.size());
        setMultiPaymentReferenceList(this.multiPaymentReferenceList);
        logger.info("This is Reference List : Down : " + this.multiPaymentReferenceList.size());
      }
    }
    catch (BusinessException exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String updateStatus()
    throws ApplicationException
  {
    logger.info("Entering Method");
    BoeCheckerBD boeCheckerBD = null;
    try
    {
      boeCheckerBD = BoeCheckerBD.getBD();
      if ((this.chkList != null) && (this.check != null)) {
        boeCheckerBD.updateStatus(this.chkList, this.check, this.remarks);
      }
      if (this.boeSearchVO != null)
      {
        this.multiPaymentReferenceList = boeCheckerBD.loadMultiPaymentReferenceData(this.boeSearchVO);
        setMultiPaymentReferenceList(this.multiPaymentReferenceList);
      }
    }
    catch (BusinessException exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    this.remarks = "";
    return "success";
  }
  public String boeDataView()
    throws ApplicationException
  {
    logger.info("Entering Method");
    BoeCheckerBD boeCheckerBD = null;
    try
    {
      String[] temp = this.boeData.split(":");
      String boeNo = temp[0];
      String boeDate = temp[1];
      String portCode = temp[2];
      String paymentRef = temp[3];
      String paymentSlNo = temp[4];
      boeCheckerBD = BoeCheckerBD.getBD();
      this.boeVO = boeCheckerBD.boeDataView(boeNo, boeDate, portCode, paymentRef, paymentSlNo);
      this.invoiceList1 = boeCheckerBD.fetchInvoiecListChecker(boeNo, boeDate, portCode, paymentRef, paymentSlNo);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String resetBOEDetails()
    throws ApplicationException
  {
    logger.info("Entering Method");
    if (this.boeSearchVO != null) {
      this.boeSearchVO = new BOESearchVO();
    }
    logger.info("Exiting Method");
    return "success";
  }
}
