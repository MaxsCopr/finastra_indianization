package com.in.fetchAccount.action;
 
import com.in.fetchAccount.bean.FetchAccountRequestBean;

import com.in.fetchAccount.bean.FetchAccountResponseBean;

import com.in.fetchAccount.dao.exception.ApplicationException;

import com.in.fetchAccount.dao.services.FetchAccountDetailsDaoImpl;

import com.in.fetchAccount.utility.ValidationsUtil;

import com.opensymphony.xwork2.ActionContext;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts2.ServletActionContext;
 
public class FetchAccountDetailsAction

  extends FetchAccountDetailsBaseAction

{

  private static Logger logger = Logger.getLogger(FetchAccountDetailsAction.class.getName());

  private static final long serialVersionUID = 1L;

  private FetchAccountRequestBean fetchAccountRequestBean = null;

  private List<FetchAccountResponseBean> finalAccountResponseList;

  public FetchAccountRequestBean getFetchAccountRequestBean()

  {

    return this.fetchAccountRequestBean;

  }

  public void setFetchAccountRequestBean(FetchAccountRequestBean fetchAccountRequestBean)

  {

    this.fetchAccountRequestBean = fetchAccountRequestBean;

  }

  public List<FetchAccountResponseBean> getFinalAccountResponseList()

  {

    return this.finalAccountResponseList;

  }

  public void setFinalAccountResponseList(List<FetchAccountResponseBean> finalAccountResponseList)

  {

    this.finalAccountResponseList = finalAccountResponseList;

  }

  public String landingPage()

    throws ApplicationException

  {

    logger.info("FetchAccountDetailsAction : landingPage : Started");

    String sessionUserName = "";

    try

    {

      this.fetchAccountRequestBean = new FetchAccountRequestBean();

      isSessionAvailable();

 
      HttpSession session = ServletActionContext.getRequest().getSession();

      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()

        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");

 
      sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("FetchAccountDetailsAction : landingPage : Request SessionUserName------------------>" + sessionUserName);

      if (sessionUserName == null)

      {

        sessionUserName = request.getRemoteUser();

        logger.info("FetchAccountDetailsAction : landingPage : getRemoteUser------------------>" + sessionUserName);

        if (sessionUserName == null)

        {

          sessionUserName = request.getRequestedSessionId();

          sessionUserName = FetchAccountDetailsDaoImpl.getSessionUserId(sessionUserName);

          session.setAttribute("loginedUserName", sessionUserName);

          session.setAttribute("loginedUserId", sessionUserName);

        }

      }

      logger.info("FetchAccountDetailsAction : landingPage : Fianl SessionUserName------------------>" + sessionUserName);

      String sessionUserVal = ValidationsUtil.isValidString(sessionUserName) ? sessionUserName.trim() : "";

      String masterReference = ValidationsUtil.isValidString(request.getParameter("masterReference")) ? request.getParameter("masterReference").trim() : "";

      String eventReference = ValidationsUtil.isValidString(request.getParameter("eventReference")) ? request.getParameter("eventReference").trim() : "";

      String customerId = ValidationsUtil.isValidString(request.getParameter("cifNo")) ? request.getParameter("cifNo").trim() : "";

      String btnStatus = ValidationsUtil.isValidString(request.getParameter("stepPhase")) ? request.getParameter("stepPhase").trim() : "";

      logger.info("FetchAccountDetailsAction : landingPage : btnStatus------------------>" + btnStatus);

      this.fetchAccountRequestBean.setSessionUserName(sessionUserVal);

      this.fetchAccountRequestBean.setMasterRef(masterReference);

      this.fetchAccountRequestBean.setEventRef(eventReference);

      this.fetchAccountRequestBean.setCustomerId(customerId);

      this.fetchAccountRequestBean.setBtnStatus(btnStatus);

    }

    catch (Exception e)

    {

      logger.info("FetchAccountDetailsAction : landingPage : Exception :" + e.getMessage());

      logger.error(e);

      throwApplicationException(e);

    }

    logger.info("FetchAccountDetailsAction : landingPage : Ended");

    return "success";

  }

  public String submitAccountDetails()

  {

    logger.info("FetchAccountDetailsAction : submitAccountDetails : Started");

    List<FetchAccountResponseBean> fetchAccountResponseList = new ArrayList();

    List<String> accountList = new ArrayList();

    String customerId = ValidationsUtil.isValidString(this.fetchAccountRequestBean.getCustomerId()) ? this.fetchAccountRequestBean.getCustomerId().trim() : "";

    String sessionUserVal = ValidationsUtil.isValidString(this.fetchAccountRequestBean.getSessionUserName()) ? this.fetchAccountRequestBean.getSessionUserName().trim() : "";

    String masterReference = ValidationsUtil.isValidString(this.fetchAccountRequestBean.getMasterRef()) ? this.fetchAccountRequestBean.getMasterRef().trim() : "";

    String eventReference = ValidationsUtil.isValidString(this.fetchAccountRequestBean.getEventRef()) ? this.fetchAccountRequestBean.getEventRef().trim() : "";

    String keyVal = FetchAccountDetailsDaoImpl.getDbKeyValue(masterReference, eventReference);

 
    String keyValue = ValidationsUtil.isValidString(keyVal) ? keyVal.trim() : "";

    String btnStatus = ValidationsUtil.isValidString(this.fetchAccountRequestBean.getBtnStatus()) ? this.fetchAccountRequestBean.getBtnStatus().trim() : "";

    logger.info("sessionUserVal :" + sessionUserVal + " customerId :" + customerId + " masterReference :" + masterReference + " eventReference :" + eventReference + " keyValue :" + keyValue + " btnStatus :" + btnStatus);

    this.fetchAccountRequestBean.setSessionUserName(sessionUserVal);

    this.fetchAccountRequestBean.setCustomerId(customerId);

    this.fetchAccountRequestBean.setMasterRef(masterReference);

    this.fetchAccountRequestBean.setEventRef(eventReference);

    this.fetchAccountRequestBean.setDbKeyValue(keyValue);

    this.fetchAccountRequestBean.setBtnStatus(btnStatus);

    try

    {

      accountList = FetchAccountDetailsDaoImpl.getAccountBasedOnCustId(customerId);

      logger.info("FetchAccountDetailsAction : submitAccountDetails : accountList Size :" + accountList.size());

      if (accountList.size() > 0)

      {

        for (String accountNumber : accountList)

        {

          FetchAccountResponseBean fetchAccountResponseBean = AccountAvailBalAction.process(accountNumber, customerId);

          fetchAccountResponseList.add(fetchAccountResponseBean);

        }

        logger.info("FetchAccountDetailsAction : submitAccountDetails : fetchAccountResponseList Size :" + fetchAccountResponseList.size());

        if (fetchAccountResponseList.size() > 0)

        {

          logger.info("FetchAccountDetailsAction : submitAccountDetails : inside if");

          this.finalAccountResponseList = fetchAccountResponseList;

          FetchAccountDetailsDaoImpl.updateDataInTiScreen(this.finalAccountResponseList, keyValue);

        }

        logger.info("FetchAccountDetailsAction : submitAccountDetails : finalAccountResponseList Size :" + this.finalAccountResponseList.size());

      }

      else

      {

        addActionError("There Is No PCA Account Is Avaliable For This Customer");

        return "error";

      }

    }

    catch (Exception e)

    {

      logger.info("FetchAccountDetailsAction : submitAccountDetails : Exception :" + e.getMessage());

      logger.error(e);

    }

    return "success";

  }

}