package in.co.localization.action;
 
import in.co.localization.boe.businessdelegate.BoeBD;

import in.co.localization.businessdelegate.localization.SearchRecordsBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.vo.localization.BOEDataSearchVO;

import in.co.localization.vo.localization.BoeVO;

import in.co.localization.vo.localization.IdpmsOrmVO;

import in.co.localization.vo.localization.InvoiceDetailsVO;

import java.util.ArrayList;

import java.util.Map;

import org.apache.log4j.Logger;
 
public class SearchRecordsAction

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(SearchRecordsAction.class

    .getName());

  private static final long serialVersionUID = 1L;

  BOEDataSearchVO boeSearchVO;

  ArrayList<BoeVO> boeList = null;

  Map<String, String> boeStatus;

  BoeVO boevo;

  ArrayList<InvoiceDetailsVO> invoiceList;

  Map<String, String> appGivenByList;

  Map<String, String> indicatorList;

  Map<String, String> clTransType;

  ArrayList<IdpmsOrmVO> idpmsOrmVOList;

  public Map<String, String> getClTransType()

  {

    return ActionConstants.BOE_CL_TYPE;

  }

  public void setClTransType(Map<String, String> clTransType)

  {

    this.clTransType = clTransType;

  }

  public ArrayList<IdpmsOrmVO> getIdpmsOrmVOList()

  {

    return this.idpmsOrmVOList;

  }

  public void setIdpmsOrmVOList(ArrayList<IdpmsOrmVO> idpmsOrmVOList)

  {

    this.idpmsOrmVOList = idpmsOrmVOList;

  }

  public Map<String, String> getAppGivenByList()

  {

    return ActionConstants.APP_BY;

  }

  public void setAppGivenByList(Map<String, String> appGivenByList)

  {

    this.appGivenByList = appGivenByList;

  }

  public Map<String, String> getIndicatorList()

  {

    logger.info("getIndicatorList() =[=================>>>");

    return ActionConstants.ADJ_CLOSURE_IND;

  }

  public void setIndicatorList(Map<String, String> indicatorList)

  {

    logger.info("setAdjCloInd() +================>>> " + indicatorList);

    this.indicatorList = indicatorList;

  }

  public BoeVO getBoevo()

  {

    return this.boevo;

  }

  public void setBoevo(BoeVO boevo)

  {

    this.boevo = boevo;

  }

  public ArrayList<InvoiceDetailsVO> getInvoiceList()

  {

    return this.invoiceList;

  }

  public void setInvoiceList(ArrayList<InvoiceDetailsVO> invoiceList)

  {

    this.invoiceList = invoiceList;

  }

  public Map<String, String> getBoeStatus()

  {

    return ActionConstants.BOE_STATUS;

  }

  public void setBoeStatus(Map<String, String> boeStatus)

  {

    this.boeStatus = boeStatus;

  }

  public ArrayList<BoeVO> getBoeList()

  {

    return this.boeList;

  }

  public void setBoeList(ArrayList<BoeVO> boeList)

  {

    this.boeList = boeList;

  }

  public BOEDataSearchVO getBoeSearchVO()

  {

    return this.boeSearchVO;

  }

  public void setBoeSearchVO(BOEDataSearchVO boeSearchVO)

  {

    this.boeSearchVO = boeSearchVO;

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

  public String boeExSearch()

    throws ApplicationException

  {

    logger.info("----------boeExSearch-----------");

    SearchRecordsBD bd = null;

    try

    {

      bd = SearchRecordsBD.getBD();

      if (this.boeSearchVO != null) {

        this.boeList = bd.boeExSearch(this.boeSearchVO);

      }

    }

    catch (Exception e)

    {

      logger.info("boeExSearch-----------Exception" + e);

      throwApplicationException(e);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String boeClSearch()

    throws ApplicationException

  {

    logger.info("-------------boeClSearch---exception----------");

    SearchRecordsBD bd = null;

    try

    {

      bd = SearchRecordsBD.getBD();

      if (this.boeSearchVO != null) {

        this.boeList = bd.boeClSearch(this.boeSearchVO);

      }

    }

    catch (Exception e)

    {

      logger.info("-------------boeClSearch---exception----------" + e);

      throwApplicationException(e);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String boeClSearchView()

    throws ApplicationException

  {

    logger.info("Entering Method");

    BoeBD bd = null;

    try

    {

      bd = BoeBD.getBD();

      this.boevo = bd.boeView(this.boeSearchVO);

      if (this.boevo != null) {

        this.invoiceList = this.boevo.getInvoiceList();

      }

    }

    catch (Exception e)

    {

      throwApplicationException(e);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String ormClSearch()

    throws ApplicationException

  {

    logger.info("Entering Method");

    SearchRecordsBD bd = null;

    try

    {

      bd = SearchRecordsBD.getBD();

      if (this.boeSearchVO != null) {

        this.idpmsOrmVOList = bd.ormClSearch(this.boeSearchVO);

      }

    }

    catch (Exception e)

    {

      logger.info("ormClSearch------------------------" + e);

      throwApplicationException(e);

    }

    logger.info("Exiting Method");

    return "success";

  }

}