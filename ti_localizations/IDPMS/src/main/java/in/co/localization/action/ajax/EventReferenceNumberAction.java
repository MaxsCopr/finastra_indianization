package in.co.localization.action.ajax;
 
import in.co.localization.action.LocalizationBaseAction;
import in.co.localization.vo.localization.EDMPSProcessVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class EventReferenceNumberAction
  extends LocalizationBaseAction
{
  private static Logger logger = Logger.getLogger(EventReferenceNumberAction.class
    .getName());
  private static final long serialVersionUID = 1L;
  ArrayList<EDMPSProcessVO> eventList = null;
  String billRefNo = null;
  public ArrayList<EDMPSProcessVO> getEventList()
  {
    return this.eventList;
  }
  public void setEventList(ArrayList<EDMPSProcessVO> eventList)
  {
    this.eventList = eventList;
  }
  public String getBillRefNo()
  {
    return this.billRefNo;
  }
  public void setBillRefNo(String billRefNo)
  {
    this.billRefNo = billRefNo;
  }
}