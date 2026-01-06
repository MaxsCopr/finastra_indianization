package in.co.boe.action;

import in.co.boe.businessdelegate.boe_viewer.BOEViwerBD;
import in.co.boe.dao.exception.ApplicationException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.vo.BOEViewerVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;
 
public class BOEViewerAction
  extends BoeBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(BOEViewerAction.class
    .getName());
  BOEViewerVO viewerVO;
  ArrayList<TransactionVO> boeList = null;
  ArrayList<BoeVO> custList = null;
  BoeVO boeDetailsVO;
  Map<String, String> bankname;
  public Map<String, String> getBankname()
  {
    return ActionConstants.BANK_NAME;
  }
  public void setBankname(Map<String, String> bankname)
  {
    this.bankname = bankname;
  }
  public ArrayList<BoeVO> getCustList()
  {
    return this.custList;
  }
  public void setCustList(ArrayList<BoeVO> custList)
  {
    this.custList = custList;
  }
  public BoeVO getBoeDetailsVO()
  {
    return this.boeDetailsVO;
  }
  public void setBoeDetailsVO(BoeVO boeDetailsVO)
  {
    this.boeDetailsVO = boeDetailsVO;
  }
  public BOEViewerVO getViewerVO()
  {
    return this.viewerVO;
  }
  public void setViewerVO(BOEViewerVO viewerVO)
  {
    this.viewerVO = viewerVO;
  }
  public ArrayList<TransactionVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<TransactionVO> boeList)
  {
    this.boeList = boeList;
  }
  public String landingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      logger.info("BOE Viewer Action");
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String viewerProcess()
    throws ApplicationException
  {
    logger.info("Entering Method");
    BOEViwerBD bd = null;
    try
    {
      bd = new BOEViwerBD();
      if (this.viewerVO == null) {
        this.viewerVO = new BOEViewerVO();
      }
      this.viewerVO = bd.getBOEIssuance(this.viewerVO);
      if (this.viewerVO != null) {
        this.boeList = this.viewerVO.getBoeList();
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
}