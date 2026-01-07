package in.co.localization.action;
 
import in.co.localization.businessdelegate.localization.AcknowledgementBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.vo.localization.AcknowledgementListVO;

import in.co.localization.vo.localization.AcknowledgementVO;

import java.util.ArrayList;

import org.apache.log4j.Logger;
 
public class AcknowledgementAction

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(AcknowledgementAction.class

    .getName());

  private static final long serialVersionUID = 1L;

  AcknowledgementVO ackVO;

  AcknowledgementListVO ackListVO;

  ArrayList<AcknowledgementVO> rodAckList = null;

  ArrayList<AcknowledgementVO> penAckList = null;

  ArrayList<AcknowledgementVO> trrAckList = null;

  ArrayList<AcknowledgementVO> irmAckList = null;

  ArrayList<AcknowledgementVO> irFircAckList = null;

  ArrayList<AcknowledgementVO> wsnAckShippingList = null;

  ArrayList<AcknowledgementVO> wsnAckInvoiceList = null;

  ArrayList<AcknowledgementVO> prnAckShippingList = null;

  ArrayList<AcknowledgementVO> prnAckInvoiceList = null;

  public ArrayList<AcknowledgementVO> getIrFircAckList()

  {

    return this.irFircAckList;

  }

  public void setIrFircAckList(ArrayList<AcknowledgementVO> irFircAckList)

  {

    this.irFircAckList = irFircAckList;

  }

  public ArrayList<AcknowledgementVO> getIrmAckList()

  {

    return this.irmAckList;

  }

  public void setIrmAckList(ArrayList<AcknowledgementVO> irmAckList)

  {

    this.irmAckList = irmAckList;

  }

  public AcknowledgementListVO getAckListVO()

  {

    return this.ackListVO;

  }

  public void setAckListVO(AcknowledgementListVO ackListVO)

  {

    this.ackListVO = ackListVO;

  }

  public AcknowledgementVO getAckVO()

  {

    return this.ackVO;

  }

  public void setAckVO(AcknowledgementVO ackVO)

  {

    this.ackVO = ackVO;

  }

  public ArrayList<AcknowledgementVO> getTrrAckList()

  {

    return this.trrAckList;

  }

  public void setTrrAckList(ArrayList<AcknowledgementVO> trrAckList)

  {

    this.trrAckList = trrAckList;

  }

  public ArrayList<AcknowledgementVO> getWsnAckShippingList()

  {

    return this.wsnAckShippingList;

  }

  public void setWsnAckShippingList(ArrayList<AcknowledgementVO> wsnAckShippingList)

  {

    this.wsnAckShippingList = wsnAckShippingList;

  }

  public ArrayList<AcknowledgementVO> getWsnAckInvoiceList()

  {

    return this.wsnAckInvoiceList;

  }

  public void setWsnAckInvoiceList(ArrayList<AcknowledgementVO> wsnAckInvoiceList)

  {

    this.wsnAckInvoiceList = wsnAckInvoiceList;

  }

  public ArrayList<AcknowledgementVO> getPrnAckShippingList()

  {

    return this.prnAckShippingList;

  }

  public void setPrnAckShippingList(ArrayList<AcknowledgementVO> prnAckShippingList)

  {

    this.prnAckShippingList = prnAckShippingList;

  }

  public ArrayList<AcknowledgementVO> getPrnAckInvoiceList()

  {

    return this.prnAckInvoiceList;

  }

  public void setPrnAckInvoiceList(ArrayList<AcknowledgementVO> prnAckInvoiceList)

  {

    this.prnAckInvoiceList = prnAckInvoiceList;

  }

  public ArrayList<AcknowledgementVO> getPenAckList()

  {

    return this.penAckList;

  }

  public void setPenAckList(ArrayList<AcknowledgementVO> penAckList)

  {

    this.penAckList = penAckList;

  }

  public ArrayList<AcknowledgementVO> getRodAckList()

  {

    return this.rodAckList;

  }

  public void setRodAckList(ArrayList<AcknowledgementVO> rodAckList)

  {

    this.rodAckList = rodAckList;

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

  public String fetchRodData()

    throws ApplicationException

  {

    logger.info("Entering Method");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackVO != null) {

        this.rodAckList = bd.fetchRodData(this.ackVO);

      }

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchPenData()

    throws ApplicationException

  {

    logger.info("Entering--fetchPenData-- Method");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackVO != null) {

        this.penAckList = bd.fetchPenData(this.ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering--fetchPenData-exception- Method" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchTrrData()

    throws ApplicationException

  {

    logger.info("Entering--fetchTrrData- Method");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackVO != null) {

        this.trrAckList = bd.fetchTrrData(this.ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering--fetchTrrData- exception" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchIrmData()

    throws ApplicationException

  {

    logger.info("Entering-----fetchIrmData Method");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackVO != null) {

        this.irmAckList = bd.fetchIrmData(this.ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering-----fetchIrmData exception" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchIRFIRcData()

    throws ApplicationException

  {

    logger.info("Entering----fetchIRFIRcData- Method");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackVO != null) {

        this.irFircAckList = bd.fetchIRFIRcData(this.ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering----fetchIRFIRcData- exception" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchPrnData()

    throws ApplicationException

  {

    logger.info("Entering---fetchPrnData-- Method");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackListVO != null) {

        this.ackListVO = bd.getAckPRNReportList(this.ackListVO);

      }

      if (this.ackListVO != null) {

        setVOToForm(this.ackListVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering---fetchPrnData-exception- Method" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchWsnData()

    throws ApplicationException

  {

    logger.info("Entering--fetchWsnData-- Method");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackListVO != null) {

        this.ackListVO = bd.getAckWSNReportList(this.ackListVO);

      }

      if (this.ackListVO != null) {

        setVOToForm(this.ackListVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering--fetchWsnData--exception" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  private void setVOToForm(AcknowledgementListVO ackListVO)

    throws ApplicationException

  {

    try

    {

      this.prnAckShippingList = ackListVO.getPrnShippingList();

      this.prnAckInvoiceList = ackListVO.getPrnInvoiceList();

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

  }

}