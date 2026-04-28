package in.co.localization.action;
 
import com.opensymphony.xwork2.Action;

import com.opensymphony.xwork2.ActionContext;

import in.co.localization.businessdelegate.localization.AcknowledgementBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.utility.ActionConstantsQuery;

import in.co.localization.utility.DBConnectionUtility;

import in.co.localization.utility.LoggableStatement;

import in.co.localization.vo.localization.AcknowledgementListVO;

import in.co.localization.vo.localization.AcknowledgementVO;

import java.io.IOException;

import java.sql.Connection;

import java.sql.ResultSet;

import java.util.ArrayList;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts2.ServletActionContext;
 
public class AcknowledgementAction

  extends LocalizationBaseAction

  implements ActionConstantsQuery, Action

{

  private static Logger logger = Logger.getLogger(AcknowledgementAction.class

    .getName());

  private static final long serialVersionUID = 1L;

  AcknowledgementVO ackVO;

  AcknowledgementListVO ackListVO;

  Map<String, String> ackStatusList;

  Map<String, String> obbAckStatusList;

  ArrayList<AcknowledgementVO> ormAckList = null;

  ArrayList<AcknowledgementVO> besAckList = null;

  ArrayList<AcknowledgementVO> beeAckList = null;

  ArrayList<AcknowledgementVO> beaAckList = null;

  ArrayList<AcknowledgementVO> mbeAckList = null;

  ArrayList<AcknowledgementVO> oraAckList = null;

  ArrayList<AcknowledgementVO> obbAckList = null;

  ArrayList<AcknowledgementVO> bttAckList = null;

  public ArrayList<AcknowledgementVO> getBttAckList()

  {

    return this.bttAckList;

  }

  public void setBttAckList(ArrayList<AcknowledgementVO> bttAckList)

  {

    this.bttAckList = bttAckList;

  }

  String[] chkList = null;

  String check = null;

  String remarks = null;

  public String[] getChkList()

  {

    return this.chkList;

  }

  public void setChkList(String[] chkList)

  {

    this.chkList = chkList;

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

  String[] chkList1 = null;

  String check1 = null;

  String remarks1 = null;

  public String[] getChkList1()

  {

    return this.chkList1;

  }

  public void setChkList1(String[] chkList1)

  {

    this.chkList1 = chkList1;

  }

  public String getCheck1()

  {

    return this.check1;

  }

  public void setCheck1(String check1)

  {

    this.check1 = check1;

  }

  public String getRemarks1()

  {

    return this.remarks1;

  }

  public void setRemarks1(String remarks1)

  {

    this.remarks1 = remarks1;

  }

  private String successCount = "0";

  private String failCount = "0";

  private String errorDesc = "0";

  private String totalCount = "0";

  private String recCount = "0";

  private String uploadCount = "0";

  public String getRecCount()

  {

    return this.recCount;

  }

  public void setRecCount(String recCount)

  {

    this.recCount = recCount;

  }

  public String getUploadCount()

  {

    return this.uploadCount;

  }

  public void setUploadCount(String uploadCount)

  {

    this.uploadCount = uploadCount;

  }

  public Map<String, String> getObbAckStatusList()

  {

    return ActionConstants.OBB_ACK_STATUS;

  }

  public void setObbAckStatusList(Map<String, String> obbAckStatusList)

  {

    this.obbAckStatusList = obbAckStatusList;

  }

  public ArrayList<AcknowledgementVO> getObbAckList()

  {

    return this.obbAckList;

  }

  public void setObbAckList(ArrayList<AcknowledgementVO> obbAckList)

  {

    this.obbAckList = obbAckList;

  }

  public String getSuccessCount()

  {

    return this.successCount;

  }

  public void setSuccessCount(String successCount)

  {

    this.successCount = successCount;

  }

  public String getFailCount()

  {

    return this.failCount;

  }

  public void setFailCount(String failCount)

  {

    this.failCount = failCount;

  }

  public String getErrorDesc()

  {

    return this.errorDesc;

  }

  public void setErrorDesc(String errorDesc)

  {

    this.errorDesc = errorDesc;

  }

  public String getTotalCount()

  {

    return this.totalCount;

  }

  public void setTotalCount(String totalCount)

  {

    this.totalCount = totalCount;

  }

  public Map<String, String> getAckStatusList()

  {

    return ActionConstants.ACK_STATUS;

  }

  public void setAckStatusList(Map<String, String> ackStatusList)

  {

    this.ackStatusList = ackStatusList;

  }

  public ArrayList<AcknowledgementVO> getBeaAckList()

  {

    return this.beaAckList;

  }

  public void setBeaAckList(ArrayList<AcknowledgementVO> beaAckList)

  {

    this.beaAckList = beaAckList;

  }

  public ArrayList<AcknowledgementVO> getOraAckList()

  {

    return this.oraAckList;

  }

  public void setOraAckList(ArrayList<AcknowledgementVO> oraAckList)

  {

    this.oraAckList = oraAckList;

  }

  public ArrayList<AcknowledgementVO> getMbeAckList()

  {

    return this.mbeAckList;

  }

  public void setMbeAckList(ArrayList<AcknowledgementVO> mbeAckList)

  {

    this.mbeAckList = mbeAckList;

  }

  public ArrayList<AcknowledgementVO> getBeeAckList()

  {

    return this.beeAckList;

  }

  public void setBeeAckList(ArrayList<AcknowledgementVO> beeAckList)

  {

    this.beeAckList = beeAckList;

  }

  public ArrayList<AcknowledgementVO> getBesAckList()

  {

    return this.besAckList;

  }

  public void setBesAckList(ArrayList<AcknowledgementVO> besAckList)

  {

    this.besAckList = besAckList;

  }

  public ArrayList<AcknowledgementVO> getOrmAckList()

  {

    return this.ormAckList;

  }

  public void setOrmAckList(ArrayList<AcknowledgementVO> ormAckList)

  {

    this.ormAckList = ormAckList;

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

  public String landingPage()

    throws IOException

  {

    String sessionUserName = null;

    AcknowledgementVO ackVO = null;

    AcknowledgementBD bd = null;

    String target = null;

    try

    {

      ackVO = new AcknowledgementVO();

      isSessionAvailable();

      HttpSession session = ServletActionContext.getRequest().getSession();

      sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("Maker sessionUserName  : " + sessionUserName);

      if (sessionUserName != null)

      {

        logger.info("Inside outer session name");

        bd = new AcknowledgementBD();

        logger.info("sessionUserName--->" + sessionUserName);

        ackVO.setSessionUserName(sessionUserName);

        ackVO.setPageType("IDPMS TEAM DFB");

        int count = bd.checkLoginedUserType(ackVO);

        logger.info("Count Session Name for Checker Eligiblity");

        if (count > 0) {

          target = "success";

        } else {

          target = "fail";

        }

        logger.info("target-------------" + target);

      }

      else

      {

        target = "fail";

      }

    }

    catch (Exception e)

    {

      logger.info("IDPMS FILE Upload Exception------------>" + e);

    }

    return target;

  }

  public String fetchOrmData()

    throws ApplicationException

  {

    logger.info("-----------------------fetchOrmData-----------------------");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackVO != null)

      {

        this.ackVO = bd.fetchOrmData(this.ackVO);

        if ((this.ackVO.getOrmAckValues() != null) && 

          (!this.ackVO.getOrmAckValues().isEmpty()))

        {

          this.ormAckList = this.ackVO.getOrmAckValues();

          this.totalCount = this.ackVO.getTotalCount();

          this.successCount = this.ackVO.getSuccessCount();

          this.failCount = this.ackVO.getFailCount();

        }

      }

    }

    catch (Exception exception)

    {

      logger.info("-----------------------fetchOrmData------------exception-----------" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchBeeData()

    throws ApplicationException

  {

    logger.info("-------fetchBeeData-----------");

    AcknowledgementBD bd = null;

    try

    {

      bd = new AcknowledgementBD();

      if (this.ackVO != null)

      {

        this.ackVO = bd.fetchBeeData(this.ackVO);

        if ((this.ackVO.getBeeAckValues() != null) && 

          (!this.ackVO.getBeeAckValues().isEmpty()))

        {

          this.beeAckList = this.ackVO.getBeeAckValues();

          this.totalCount = this.ackVO.getTotalCount();

          this.successCount = this.ackVO.getSuccessCount();

          this.failCount = this.ackVO.getFailCount();

        }

      }

    }

    catch (Exception exception)

    {

      logger.info("-------fetchBeeData-------exception----" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchBeaData()

		    throws ApplicationException

		  {

		    logger.info("--------------------fetchBeaData--------------------");

		    AcknowledgementBD bd = null;

		    try

		    {

		      bd = new AcknowledgementBD();

		      if (this.ackVO != null)

		      {

		        this.ackVO = bd.fetchBeaData(this.ackVO);

		        if ((this.ackVO.getBeaAckValues() != null) && 

		          (!this.ackVO.getBeaAckValues().isEmpty()))

		        {

		          this.beaAckList = this.ackVO.getBeaAckValues();

		          this.totalCount = this.ackVO.getTotalCount();

		          this.successCount = this.ackVO.getSuccessCount();

		          this.failCount = this.ackVO.getFailCount();

		        }

		      }

		    }

		    catch (Exception exception)

		    {

		      logger.info("--------------------fetchBeaData-----------exception---------" + exception);

		      throwApplicationException(exception);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String fetchBesData()

		    throws ApplicationException

		  {

		    logger.info("Entering Method");

		    AcknowledgementBD bd = null;

		    try

		    {

		      bd = new AcknowledgementBD();

		      if (this.ackVO != null)

		      {

		        this.ackVO = bd.fetchBesData(this.ackVO);

		        if ((this.ackVO.getBesAckValues() != null) && 

		          (!this.ackVO.getBesAckValues().isEmpty()))

		        {

		          this.besAckList = this.ackVO.getBesAckValues();

		          this.totalCount = this.ackVO.getTotalCount();

		          this.successCount = this.ackVO.getSuccessCount();

		          this.failCount = this.ackVO.getFailCount();

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

		  public String fetchMbeData()

		    throws ApplicationException

		  {

		    logger.info("------------fetchMbeData---------------");

		    AcknowledgementBD bd = null;

		    try

		    {

		      bd = new AcknowledgementBD();

		      if (this.ackVO != null)

		      {

		        this.ackVO = bd.fetchMbeData(this.ackVO);

		        if ((this.ackVO.getMbeAckValues() != null) && 

		          (!this.ackVO.getMbeAckValues().isEmpty()))

		        {

		          this.mbeAckList = this.ackVO.getMbeAckValues();

		          this.totalCount = this.ackVO.getTotalCount();

		          this.successCount = this.ackVO.getSuccessCount();

		          this.failCount = this.ackVO.getFailCount();

		        }

		      }

		    }

		    catch (Exception exception)

		    {

		      logger.info("------------fetchMbeData-----exception----------" + exception);

		      throwApplicationException(exception);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String fetchOraData()

		    throws ApplicationException

		  {

		    logger.info("-----------------fetchOraData-----------------");

		    AcknowledgementBD bd = null;

		    try

		    {

		      bd = new AcknowledgementBD();

		      if (this.ackVO != null)

		      {

		        this.ackVO = bd.fetchOraData(this.ackVO);

		        if ((this.ackVO.getOraAckValues() != null) && 

		          (!this.ackVO.getOraAckValues().isEmpty()))

		        {

		          this.oraAckList = this.ackVO.getOraAckValues();

		          this.totalCount = this.ackVO.getTotalCount();

		          this.successCount = this.ackVO.getSuccessCount();

		          this.failCount = this.ackVO.getFailCount();

		          this.ackVO = null;

		        }

		      }

		    }

		    catch (Exception exception)

		    {

		      logger.info("-----------------fetchOraData----------exception-------" + exception);

		      throwApplicationException(exception);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String fetchObbData()

		    throws ApplicationException

		  {

		    logger.info("Entering Method");

		    AcknowledgementBD bd = null;

		    try

		    {

		      bd = new AcknowledgementBD();

		      if (this.ackVO != null)

		      {

		        this.ackVO = bd.fetchObbData(this.ackVO);

		        if ((this.ackVO.getObbAckValues() != null) && 

		          (!this.ackVO.getObbAckValues().isEmpty()))

		        {

		          this.obbAckList = this.ackVO.getObbAckValues();

		          this.totalCount = this.ackVO.getTotalCount();

		          this.successCount = this.ackVO.getSuccessCount();

		          this.recCount = this.ackVO.getRecCount();

		          this.uploadCount = this.ackVO.getUploadCount();

		          this.ackVO = null;

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
		  public String fetchBttData()

				    throws ApplicationException

				  {

				    logger.info("Entering Method");

				    AcknowledgementBD bd = null;

				    try

				    {

				      bd = new AcknowledgementBD();

				      if (this.ackVO != null)

				      {

				        this.ackVO = bd.fetchBttData(this.ackVO);

				        if ((this.ackVO.getBttAckValues() != null) && 

				          (!this.ackVO.getBttAckValues().isEmpty()))

				        {

				          this.bttAckList = this.ackVO.getBttAckValues();

				          this.totalCount = this.ackVO.getTotalCount();

				 
				 
				          this.ackVO = null;

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

				  public String besCancellationProcess()

				    throws ApplicationException

				  {

				    logger.info("Entering Method");

				    String sessionUserName = "";

				    try

				    {

				      HttpSession session = ServletActionContext.getRequest().getSession();

				      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");

				 
				      sessionUserName = (String)session.getAttribute("loginedUserName");

				      logger.info("loginedUserName------------------>" + sessionUserName);

				      if (sessionUserName == null)

				      {

				        sessionUserName = request.getRemoteUser();

				 
				        logger.info("getRemoteUser------------------>" + sessionUserName);

				        if (sessionUserName == null)

				        {

				          Connection globalConnection = null;

				          globalConnection = DBConnectionUtility.getGlobalConnection();

				          sessionUserName = request.getRequestedSessionId();

				          logger.info("sessionUserName------------------>" + sessionUserName);

				          String get_User_ID = 

				            "SELECT SCT.USERNAME AS USER_ID FROM TIGLOBAL.CENTRAL_SESSION_DETAILS SCT,TIGLOBAL.LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";

				 
				          LoggableStatement lst = new LoggableStatement(globalConnection, get_User_ID);

				          lst.setString(1, sessionUserName);

				          logger.info("Getting Session Value Query------------" + 

				            lst.getQueryString());

				          ResultSet rst = lst.executeQuery();

				          while (rst.next())

				          {

				            sessionUserName = rst.getString("USER_ID");

				            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);

				          }

				          session.setAttribute("loginedUserName", sessionUserName);

				          session.setAttribute("loginedUserId", sessionUserName);

				          DBConnectionUtility.surrenderDB(globalConnection, lst, rst);

				          logger.info("userName-----------" + sessionUserName);

				        }

				        session.setAttribute("loginedUserName", sessionUserName);

				        session.setAttribute("loginedUserId", sessionUserName);

				      }

				    }

				    catch (Exception e)

				    {

				      logger.error("Exception In makercheckerlandingPage", e);

				      e.printStackTrace();

				    }

				    AcknowledgementVO ackVO = null;

				    AcknowledgementBD bd = null;

				    String target = null;

				    try

				    {

				      ackVO = new AcknowledgementVO();

				      isSessionAvailable();

				      logger.info("Maker sessionUserName  : " + sessionUserName);

				      if (sessionUserName != null)

				      {

				        logger.info("Inside outer session name");

				        bd = new AcknowledgementBD();

				        logger.info("sessionUserName--->" + sessionUserName);

				        ackVO.setSessionUserName(sessionUserName);

				        ackVO.setPageType("BES Cancel");

				        int count = bd.checkLoginedUserType(ackVO);

				        logger.info("Count Session Name for Checker Eligiblity");

				        if (count > 0) {

				          target = "success";

				        } else {

				          target = "fail";

				        }

				        logger.info("target-------------" + target);

				      }

				      else

				      {

				        target = "fail";

				      }

				    }

				    catch (Exception e)

				    {

				      logger.info("IDPMS FILE Upload Exception------------>" + e);

				    }

				    logger.info("Exiting Method");

				    return target;

				  }

				  public String fetchBesMakerData()

				    throws ApplicationException

				  {

				    logger.info("Entering Method");

				    AcknowledgementBD bd = null;

				    try

				    {

				      bd = new AcknowledgementBD();

				      if (this.ackVO != null)

				      {

				        this.ackVO = bd.fetchBesDataForMaker(this.ackVO);

				        if ((this.ackVO.getBesAckValues() != null) && 

				          (!this.ackVO.getBesAckValues().isEmpty()))

				        {

				          this.besAckList = this.ackVO.getBesAckValues();

				          this.totalCount = this.ackVO.getTotalCount();

				          this.successCount = this.ackVO.getSuccessCount();

				          this.failCount = this.ackVO.getFailCount();

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

				  public String deleteBesData()

				    throws ApplicationException

				  {

				    logger.info("Entering MethodStart deleteBesData Action");

				    AcknowledgementBD bd = null;

				    try

				    {

				      bd = new AcknowledgementBD();

				      if (this.ackVO != null)

				      {

				        if (this.chkList != null)

				        {

				          logger.info("inside checkList not null");

				          this.ackVO = bd.deleteBesData(this.chkList, this.remarks, this.ackVO);

				        }

				        if ((this.ackVO.getBesAckValues() != null) && 

				          (!this.ackVO.getBesAckValues().isEmpty()))

				        {

				          this.besAckList = this.ackVO.getBesAckValues();

				          this.totalCount = this.ackVO.getTotalCount();

				          this.successCount = this.ackVO.getSuccessCount();

				          this.failCount = this.ackVO.getFailCount();

				        }

				      }

				    }

				    catch (Exception exception)

				    {

				      throwApplicationException(exception);

				    }

				    logger.info("Exiting MethodEnd deleteBesData Action");

				    return "success";

				  }
				  public String fetchbesCheckerData()

						    throws ApplicationException

						  {

						    logger.info("Entering Method");

						    AcknowledgementBD bd = null;

						    try

						    {

						      bd = new AcknowledgementBD();

						      if (this.ackVO != null)

						      {

						        this.ackVO = bd.fetchBesCheckerData(this.ackVO);

						        if ((this.ackVO.getBesAckValues() != null) && 

						          (!this.ackVO.getBesAckValues().isEmpty()))

						        {

						          this.besAckList = this.ackVO.getBesAckValues();

						          this.totalCount = this.ackVO.getTotalCount();

						          this.successCount = this.ackVO.getSuccessCount();

						          this.failCount = this.ackVO.getFailCount();

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

						  public String updateBESCheckerData()

						    throws ApplicationException

						  {

						    logger.info("Entering Method");

						    AcknowledgementBD bd = null;

						    try

						    {

						      bd = new AcknowledgementBD();

						      if (this.chkList1 != null) {

						        bd.updateBesCheckerData(this.chkList1, this.check1, this.remarks1);

						      }

						      if (this.ackVO != null)

						      {

						        this.ackVO = bd.fetchBesCheckerData(this.ackVO);

						        if ((this.ackVO.getBesAckValues() != null) && 

						          (!this.ackVO.getBesAckValues().isEmpty()))

						        {

						          this.besAckList = this.ackVO.getBesAckValues();

						          this.totalCount = this.ackVO.getTotalCount();

						          this.successCount = this.ackVO.getSuccessCount();

						          this.failCount = this.ackVO.getFailCount();

						        }

						      }

						    }

						    catch (Exception exception)

						    {

						      exception.printStackTrace();

						    }

						    logger.info("Exiting Method");

						    return "success";

						  }

						}
						 