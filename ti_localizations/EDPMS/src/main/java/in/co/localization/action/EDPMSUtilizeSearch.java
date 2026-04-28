package in.co.localization.action;
 
import in.co.localization.businessdelegate.localization.EDPMSUtilizeSearchBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.vo.localization.AcknowledgementListVO;

import in.co.localization.vo.localization.InvoiceDetailsVO;

import in.co.localization.vo.localization.ShippingDetailsVO;

import java.util.ArrayList;

import java.util.Map;

import org.apache.log4j.Logger;
 
public class EDPMSUtilizeSearch

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(EDPMSUtilizeSearch.class

    .getName());

  private static final long serialVersionUID = 1L;

  ShippingDetailsVO shpVO;

  AcknowledgementListVO ackListVO;

  ArrayList<ShippingDetailsVO> shippingList = null;

  ArrayList<InvoiceDetailsVO> invoiceList = null;

  ArrayList<AcknowledgementListVO> custList = null;

  ArrayList<ShippingDetailsVO> sbUnUtilizeList = null;

  ArrayList<InvoiceDetailsVO> mdfUnUtilizeList = null;

  Map<String, String> bankname;

  public Map<String, String> getBankname()

  {

    return ActionConstants.BANK_NAME;

  }

  public void setBankname(Map<String, String> bankname)

  {

    this.bankname = bankname;

  }

  public ArrayList<InvoiceDetailsVO> getMdfUnUtilizeList()

  {

    return this.mdfUnUtilizeList;

  }

  public void setMdfUnUtilizeList(ArrayList<InvoiceDetailsVO> mdfUnUtilizeList)

  {

    this.mdfUnUtilizeList = mdfUnUtilizeList;

  }

  public ArrayList<ShippingDetailsVO> getSbUnUtilizeList()

  {

    return this.sbUnUtilizeList;

  }

  public void setSbUnUtilizeList(ArrayList<ShippingDetailsVO> sbUnUtilizeList)

  {

    this.sbUnUtilizeList = sbUnUtilizeList;

  }

  public ArrayList<AcknowledgementListVO> getCustList()

  {

    return this.custList;

  }

  public void setCustList(ArrayList<AcknowledgementListVO> custList)

  {

    this.custList = custList;

  }

  public ArrayList<ShippingDetailsVO> getShippingList()

  {

    return this.shippingList;

  }

  public void setShippingList(ArrayList<ShippingDetailsVO> shippingList)

  {

    this.shippingList = shippingList;

  }

  public ArrayList<InvoiceDetailsVO> getInvoiceList()

  {

    return this.invoiceList;

  }

  public void setInvoiceList(ArrayList<InvoiceDetailsVO> invoiceList)

  {

    this.invoiceList = invoiceList;

  }

  public ShippingDetailsVO getShpVO()

  {

    return this.shpVO;

  }

  public void setShpVO(ShippingDetailsVO shpVO)

  {

    this.shpVO = shpVO;

  }

  public AcknowledgementListVO getAckListVO()

  {

    return this.ackListVO;

  }

  public void setAckListVO(AcknowledgementListVO ackListVO)

  {

    this.ackListVO = ackListVO;

  }

  public String custData()

    throws ApplicationException

  {

    logger.info("Entering Method");

    EDPMSUtilizeSearchBD bd = null;

    try

    {

      bd = new EDPMSUtilizeSearchBD();

      this.custList = bd.custData(this.ackListVO);

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

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

      exception.printStackTrace();

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchGRData()

    throws ApplicationException

  {

    logger.info("--------fetchGRData--------Entering Method");

    EDPMSUtilizeSearchBD bd = null;

    try

    {

      bd = new EDPMSUtilizeSearchBD();

      if (this.ackListVO == null) {

        this.ackListVO = new AcknowledgementListVO();

      }

      this.ackListVO = bd.fetchGRData(this.ackListVO);

      if (this.ackListVO != null) {

        setVOToForm(this.ackListVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("--------fetchGRData-------exception" + exception);

      exception.printStackTrace();

    }

    logger.info("Exiting Method");

    return "success";

  }

  private void setVOToForm(AcknowledgementListVO ackListVO)

    throws ApplicationException

  {

    try

    {

      this.shippingList = ackListVO.getShippingList();

      this.invoiceList = ackListVO.getInvoiceList();

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

  }

  public String fetchSBUnUtilizeBills()

    throws ApplicationException

  {

    logger.info("Entering fetchSBUnUtilizeBills-----------Method");

    EDPMSUtilizeSearchBD bd = null;

    try

    {

      bd = new EDPMSUtilizeSearchBD();

      if (this.ackListVO != null) {

        this.sbUnUtilizeList = bd.fetchSBUnUtilizeBills(this.ackListVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering fetchSBUnUtilizeBills---exception--------Method" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String resetSBUnUntilize()

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      if (this.ackListVO != null) {

        this.ackListVO = new AcknowledgementListVO();

      }

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchMDFUnUtilizeBills()

    throws ApplicationException

  {

    logger.info("Entering---------fetchMDFUnUtilizeBills-------- Method");

    EDPMSUtilizeSearchBD bd = null;

    try

    {

      bd = new EDPMSUtilizeSearchBD();

      if (this.ackListVO != null) {

        this.mdfUnUtilizeList = bd.fetchMDFUnUtilizeBills(this.ackListVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering---------fetchMDFUnUtilizeBills-------- exception" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

}