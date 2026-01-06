package in.co.ebrc.action;

import in.co.ebrc.dao.exception.ApplicationException;
import in.co.ebrc.utility.CommonMethods;
import in.co.ebrc.vo.AlertMessagesVO;
import in.co.ebrc.vo.InvMatchingDataVO;
import in.co.ebrc.vo.InvMatchingVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
 
public class InvoiceCustomerAction
  extends InvoiceBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(InvoiceCustomerAction.class.getName());
  ArrayList<InvMatchingDataVO> tiList = null;
  ArrayList<InvMatchingDataVO> storeInvData = null;
  List<InvMatchingVO> getdebitParty = null;
  List<InvMatchingVO> getcounterParty = null;
  private ArrayList<AlertMessagesVO> alertMsgArray = new ArrayList();
  ArrayList<InvMatchingVO> eventList = null;
  ArrayList<AlertMessagesVO> errorList = null;
  InvMatchingVO invoiceVO = null;
  String mode;
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
  }
  public String getMode()
  {
    return this.mode;
  }
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  public ArrayList<InvMatchingVO> getEventList()
  {
    return this.eventList;
  }
  public void setEventList(ArrayList<InvMatchingVO> eventList)
  {
    this.eventList = eventList;
  }
  public List<InvMatchingVO> getGetdebitParty()
  {
    return this.getdebitParty;
  }
  public void setGetdebitParty(List<InvMatchingVO> getdebitParty)
  {
    this.getdebitParty = getdebitParty;
  }
  public List<InvMatchingVO> getGetcounterParty()
  {
    return this.getcounterParty;
  }
  public void setGetcounterParty(List<InvMatchingVO> getcounterParty)
  {
    this.getcounterParty = getcounterParty;
  }
  public ArrayList<InvMatchingDataVO> getStoreInvData()
  {
    return this.storeInvData;
  }
  public void setStoreInvData(ArrayList<InvMatchingDataVO> storeInvData)
  {
    this.storeInvData = storeInvData;
  }
  InvMatchingVO invMatchingVO = null;
  public ArrayList<InvMatchingDataVO> getTiList()
  {
    return this.tiList;
  }
  public void setTiList(ArrayList<InvMatchingDataVO> tiList)
  {
    this.tiList = tiList;
  }
  public InvMatchingVO getInvMatchingVO()
  {
    return this.invMatchingVO;
  }
  public void setInvMatchingVO(InvMatchingVO invMatchingVO)
  {
    this.invMatchingVO = invMatchingVO;
  }
  public String landingPage()
  {
    logger.info("Entering Method");
    logger.info("Exiting Method");
    return "success";
  }
  public String fetchDebitParty()
    throws ApplicationException, IOException
  {
    return this.mode;
  }
  public String fetchCounterParty()
    throws ApplicationException, IOException
  {
    return this.mode;
  }
  public String getInvoiceMatchingValue()
    throws ApplicationException
  {
    return this.mode;
  }
  public String storeInvoiceMatchingData()
    throws ApplicationException
  {
    return this.mode;
  }
  public String validateInvoiceMatchingData()
    throws ApplicationException
  {
    return this.mode;
  }
  public void setErrorvalues(Object[] arg)
  {
    CommonMethods commonMethods = new CommonMethods();
    AlertMessagesVO altMsg = new AlertMessagesVO();
    altMsg.setErrorId("Error");
    altMsg.setErrorDesc("General");
    altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
    altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
    altMsg.setErrorMsg("");
    this.alertMsgArray.add(altMsg);
  }
}