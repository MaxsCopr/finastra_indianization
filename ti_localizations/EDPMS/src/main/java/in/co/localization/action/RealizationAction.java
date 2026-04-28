package in.co.localization.action;
 
import in.co.localization.businessdelegate.localization.RealizationBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.utility.CommonMethods;

import in.co.localization.vo.localization.AcknowledgementListVO;

import in.co.localization.vo.localization.AcknowledgementVO;

import in.co.localization.vo.localization.AlertMessagesVO;

import in.co.localization.vo.localization.InvoiceDetailsVO;

import in.co.localization.vo.localization.ShippingDetailsVO;

import java.util.ArrayList;

import java.util.Map;

import org.apache.log4j.Logger;
 
public class RealizationAction

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(RealizationAction.class

    .getName());

  private static final long serialVersionUID = 1L;

  AcknowledgementListVO ackListVO;

  ArrayList<AcknowledgementVO> prnAckShippingList = null;

  ShippingDetailsVO shippingVO = null;

  Map<String, String> payStatus = null;

  Map<String, String> chPayStatus = null;

  Map<String, String> payStatusAmd = null;

  ArrayList<InvoiceDetailsVO> invoiceList = null;

  private ArrayList<AlertMessagesVO> alertMsgArray = null;

  public Map<String, String> getChPayStatus()

  {

    return ActionConstants.CH_PAY_STATUS;

  }

  public void setChPayStatus(Map<String, String> chPayStatus)

  {

    this.chPayStatus = chPayStatus;

  }

  public Map<String, String> getPayStatusAmd()

  {

    return ActionConstants.PAY_STATUS_AMD;

  }

  public void setPayStatusAmd(Map<String, String> payStatusAmd)

  {

    this.payStatusAmd = payStatusAmd;

  }

  public ArrayList<AlertMessagesVO> getAlertMsgArray()

  {

    return this.alertMsgArray;

  }

  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)

  {

    this.alertMsgArray = alertMsgArray;

  }

  public ArrayList<InvoiceDetailsVO> getInvoiceList()

  {

    return this.invoiceList;

  }

  public void setInvoiceList(ArrayList<InvoiceDetailsVO> invoiceList)

  {

    this.invoiceList = invoiceList;

  }

  public Map<String, String> getPayStatus()

  {

    return ActionConstants.PAY_STATUS;

  }

  public void setPayStaus(Map<String, String> payStatus)

  {

    this.payStatus = payStatus;

  }

  public ShippingDetailsVO getShippingVO()

  {

    return this.shippingVO;

  }

  public void setShippingVO(ShippingDetailsVO shippingVO)

  {

    this.shippingVO = shippingVO;

  }

  public AcknowledgementListVO getAckListVO()

  {

    return this.ackListVO;

  }

  public void setAckListVO(AcknowledgementListVO ackListVO)

  {

    this.ackListVO = ackListVO;

  }

  public ArrayList<AcknowledgementVO> getPrnAckShippingList()

  {

    return this.prnAckShippingList;

  }

  public void setPrnAckShippingList(ArrayList<AcknowledgementVO> prnAckShippingList)

  {

    this.prnAckShippingList = prnAckShippingList;

  }

  public String execute()

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      isSessionAvailable();

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchPRNAckData()

    throws ApplicationException

  {

    logger.info("Entering---fetchPRNAckData-- Method");

    RealizationBD bd = null;

    try

    {

      bd = RealizationBD.getBD();

      if (this.ackListVO != null) {

        this.prnAckShippingList = bd.getPRNAckData(this.ackListVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering---fetchPRNAckData-- exception----" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String prnAckDataView()

    throws ApplicationException

  {

    logger.info("Entering Method");

    RealizationBD bd = null;

    try

    {

      bd = RealizationBD.getBD();

      if (this.ackListVO != null)

      {

        this.shippingVO = bd.prnAckDataView(this.ackListVO);

        if (this.shippingVO.getInvoiceList() != null) {

          this.invoiceList = this.shippingVO.getInvoiceList();

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

  public String storeCancelPRNData()

    throws ApplicationException

  {

    logger.info("Entering ------storeCancelPRNData--------Method");

    RealizationBD bd = null;

    String target = null;

    try

    {

      bd = RealizationBD.getBD();

      if (this.ackListVO != null)

      {

        validateData(this.shippingVO);

        if (this.alertMsgArray.size() > 0)

        {

          prnAckDataView();

          target = "error";

        }

        else

        {

          int iCount = bd.storeCancelPRNData(this.ackListVO, this.shippingVO);

          logger.info("PRN Count--->" + iCount);

          target = "success";

        }

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering ------storeCancelPRNData------exception" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return target;

  }

  public void validateData(ShippingDetailsVO shippingVO)

    throws ApplicationException

  {

    logger.info("Entering Method");

    CommonMethods commonMethods = null;

    try

    {

      this.alertMsgArray = new ArrayList();

      commonMethods = new CommonMethods();

      if ((this.alertMsgArray != null) && 

        (this.alertMsgArray.size() > 0)) {

        this.alertMsgArray.clear();

      }

      if (commonMethods.isNull(shippingVO.getPayStatus()))

      {

        String errorcode = "Please Select Status";

        Object[] arg = { "0", "E", errorcode, "Input" };

        setErrorvalues(arg);

      }

    }

    catch (Exception exception)

    {

      exception.printStackTrace();

    }

  }

  public void setErrorvalues(Object[] arg)

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      CommonMethods commonMethods = new CommonMethods();

      AlertMessagesVO altMsg = new AlertMessagesVO();

      altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1])

        .equalsIgnoreCase("W") ? "Warning" : 

        "Error");

      altMsg.setErrorDesc("General");

      altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));

      altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));

      altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1])

        .equalsIgnoreCase("W") ? "N" : 

        "");

      this.alertMsgArray.add(altMsg);

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

  }

}