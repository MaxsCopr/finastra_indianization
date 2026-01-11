package in.co.chargeSchedule.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.chargeSchedule.businessdelegate.ChargeScheduleBD;
import in.co.chargeSchedule.businessdelegate.exception.BusinessException;
import in.co.chargeSchedule.dao.ChargeScheduleDAO;
import in.co.chargeSchedule.dao.exception.ApplicationException;
import in.co.chargeSchedule.utility.ActionConstants;
import in.co.chargeSchedule.utility.CommonMethods;
import in.co.chargeSchedule.utility.DBConnectionUtility;
import in.co.chargeSchedule.utility.LoggableStatement;
import in.co.chargeSchedule.vo.ChargeScheduleVO;
import in.co.chargeSchedule.vo.ChargeSelectionVO;
import in.co.chargeSchedule.vo.CustomerDataVO;
import in.co.chargeSchedule.vo.ProductSelectionVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class ChargeScheduleAction
  extends ChargeScheduleBaseAction
{
  private static Logger logger = LogManager.getLogger("appLog");
  private static final long serialVersionUID = 1L;
  ChargeScheduleVO chargeVO;
  CustomerDataVO cusDataVo;
  ProductSelectionVO productVo;
  ChargeSelectionVO chargeSelectionVO;
  ArrayList<CustomerDataVO> customerList;
  ArrayList<CustomerDataVO> goodsList;
  ArrayList<CustomerDataVO> inwardDocList;
  ArrayList<ChargeSelectionVO> chargeList;
  ArrayList<ProductSelectionVO> productList;
  ArrayList<ChargeScheduleVO> chargeScheduleList;
  ArrayList<ChargeScheduleVO> multiPaymentReferenceList = null;
  String[] chkList = null;
  String remarks = null;
  String check = null;
  String enquiryDatas = null;
  String poNocifID = null;
  ArrayList<String> poListVal = null;
  ArrayList<ChargeScheduleVO> enquiryList;
 
  public ArrayList<String> getPoListVal()
  {
    return this.poListVal;
  }
 
  public void setPoListVal(ArrayList<String> poListVal)
  {
    this.poListVal = poListVal;
  }
 
  public ArrayList<CustomerDataVO> getInwardDocList()
  {
    return this.inwardDocList;
  }
 
  public void setInwardDocList(ArrayList<CustomerDataVO> inwardDocList)
  {
    this.inwardDocList = inwardDocList;
  }
 
  public ArrayList<CustomerDataVO> getGoodsList()
  {
    return this.goodsList;
  }
 
  public void setGoodsList(ArrayList<CustomerDataVO> goodsList)
  {
    this.goodsList = goodsList;
  }
 
  public String getPoNocifID()
  {
    return this.poNocifID;
  }
 
  public void setPoNocifID(String poNocifID)
  {
    this.poNocifID = poNocifID;
  }
 
  public String getEnquiryDatas()
  {
    return this.enquiryDatas;
  }
 
  public void setEnquiryDatas(String enquiryDatas)
  {
    this.enquiryDatas = enquiryDatas;
  }
 
  public ArrayList<ChargeScheduleVO> getEnquiryList()
  {
    return this.enquiryList;
  }
 
  public void setEnquiryList(ArrayList<ChargeScheduleVO> enquiryList)
  {
    this.enquiryList = enquiryList;
  }
 
  Map<String, String> statusList = null;
  String msg;
 
  public String getCheck()
  {
    return this.check;
  }
 
  public Map<String, String> getStatusList()
  {
    return ActionConstants.REC3;
  }
 
  public void setStatusList(Map<String, String> statusList)
  {
    this.statusList = statusList;
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
 
  public ArrayList<ChargeScheduleVO> getMultiPaymentReferenceList()
  {
    return this.multiPaymentReferenceList;
  }
 
  public void setMultiPaymentReferenceList(ArrayList<ChargeScheduleVO> multiPaymentReferenceList)
  {
    this.multiPaymentReferenceList = multiPaymentReferenceList;
  }
 
  public String getMsg()
  {
    return this.msg;
  }
 
  public void setMsg(String msg)
  {
    this.msg = msg;
  }
 
  public ChargeScheduleVO getChargeVO()
  {
    return this.chargeVO;
  }
 
  public void setChargeVO(ChargeScheduleVO chargeVO)
  {
    this.chargeVO = chargeVO;
  }
 
  public CustomerDataVO getCusDataVo()
  {
    return this.cusDataVo;
  }
 
  public void setCusDataVo(CustomerDataVO cusDataVo)
  {
    this.cusDataVo = cusDataVo;
  }
 
  public ArrayList<CustomerDataVO> getCustomerList()
  {
    return this.customerList;
  }
 
  public void setCustomerList(ArrayList<CustomerDataVO> customerList)
  {
    this.customerList = customerList;
  }
 
  public ChargeSelectionVO getChargeSelectionVO()
  {
    return this.chargeSelectionVO;
  }
 
  public void setChargeSelectionVO(ChargeSelectionVO chargeSelectionVO)
  {
    this.chargeSelectionVO = chargeSelectionVO;
  }
 
  public ArrayList<ChargeSelectionVO> getChargeList()
  {
    return this.chargeList;
  }
 
  public void setChargeList(ArrayList<ChargeSelectionVO> chargeList)
  {
    this.chargeList = chargeList;
  }
 
  public ProductSelectionVO getProductVo()
  {
    return this.productVo;
  }
 
  public void setProductVo(ProductSelectionVO productVo)
  {
    this.productVo = productVo;
  }
 
  public ArrayList<ProductSelectionVO> getProductList()
  {
    return this.productList;
  }
 
  public void setProductList(ArrayList<ProductSelectionVO> productList)
  {
    this.productList = productList;
  }
 
  public ArrayList<ChargeScheduleVO> getChargeScheduleList()
  {
    return this.chargeScheduleList;
  }
 
  public void setChargeScheduleList(ArrayList<ChargeScheduleVO> chargeScheduleList)
  {
    this.chargeScheduleList = chargeScheduleList;
  }
 
  public String landingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String result = null;
    String count = null;
    String target = null;
    ChargeScheduleBD chargBD = null;
    ChargeScheduleVO chargVO = null;
    ChargeScheduleBD bd = null;
    Checkking_Valid_User usr = new Checkking_Valid_User();
    String userName = null;
    try
    {
      bd = ChargeScheduleBD.getBD();
      bd.setProcessDate();
      isSessionAvailable();
      chargVO = new ChargeScheduleVO();
     


















































      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
      userName = (String)session.getAttribute("loginedUserName");
     
      logger.info("----------------loginedUserName-----------------" + userName);
      if (!usr.isValidTIUser(userName))
      {
        logger.info("----------False Connection-----1111111111--");
       
        logger.info("------------Valid User Status-------" + usr.isValidTIUser(userName));
        userName = null;
      }
      if (userName == null)
      {
        userName = (String)session.getAttribute("loginedUserName");
        logger.info("loginedUserName------------------" + userName);
        if (!usr.isValidTIUser(userName))
        {
          logger.info("----loginedUserName------False Connection-----1111111111--");
         
          logger.info("-----loginedUserName-------Valid User Status-------" + usr.isValidTIUser(userName));
          userName = null;
        }
      }
      if (userName == null)
      {
        userName = request.getRemoteUser();
        logger.info("getRemoteUser[------------------" + userName);
        if (!usr.isValidTIUser(userName))
        {
          logger.info("-----getRemoteUser-----False Connection-----1111111111--");
         
          logger.info("-------getRemoteUser-----Valid User Status-------" + usr.isValidTIUser(userName));
          userName = null;
        }
      }
      if (userName == null)
      {
        Connection them_con = null;
        them_con = DBConnectionUtility.getWiseConnection();
       
        userName = request.getRequestedSessionId();
       
        String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
       

        LoggableStatement lst = new LoggableStatement(them_con, get_User_ID);
        lst.setString(1, userName);
        logger.info("Getting Session Value Query------------" + lst.getQueryString());
       
        ResultSet rst = lst.executeQuery();
        while (rst.next())
        {
          userName = rst.getString("USER_ID");
          logger.info("Getting Session Value Query-- user id value----------" + userName);
        }
        DBConnectionUtility.surrenderDB(them_con, lst, rst);
        logger.info("userName-----------" + userName);
      }
      chargBD = new ChargeScheduleBD();
     
      logger.info("sessionUserName-------------------------------------->" + userName);
     
      chargVO.setSessionUserName(userName);
     
      count = chargBD.checkLoginedUserType(userName);
      logger.info("get sessionUserName count" + count);
      session.setAttribute("count", count);
      if ((count != null) && (count.equalsIgnoreCase("1"))) {
        target = "maker";
      } else if ((count != null) && (count.equalsIgnoreCase("2"))) {
        target = "both";
      } else if ((count != null) && (count.equalsIgnoreCase("3"))) {
        target = "both";
      } else {
        target = "fail";
      }
      logger.info("User Status---------------->" + target);
    }
    catch (Exception exception)
    {
      logger.info("User landingPage-- Exception-------------->" + exception);
     
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
   


    return target;
  }
 
  public String loadMultiPaymentReferenceData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    ChargeScheduleBD chargBD = null;
    String value = null;
    try
    {
      logger.info("cOMING");
      isSessionAvailable();
      chargBD = ChargeScheduleBD.getBD();
      logger.info("Mohan 1");
     


      this.multiPaymentReferenceList = chargBD.loadMultiPaymentReferenceData(this.chargeVO);
     
      setMultiPaymentReferenceList(this.multiPaymentReferenceList);
      logger.info("eXITING");
    }
    catch (BusinessException exception)
    {
      logger.info("Exception is" + exception.getMessage());
      exception.printStackTrace();
     
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String loadEnquiryProcessData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    ChargeScheduleBD chargBD = null;
    try
    {
      chargBD = ChargeScheduleBD.getBD();
     

      this.multiPaymentReferenceList = chargBD.loadEnquiryProcess(this.chargeVO);
     
      setMultiPaymentReferenceList(this.multiPaymentReferenceList);
      logger.info("eXITING");
    }
    catch (BusinessException exception)
    {
      logger.info("Exception is" + exception.getMessage());
      exception.printStackTrace();
     
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String showEnquiryRecordDetails()
    throws ApplicationException
  {
    ChargeScheduleBD chargBD = null;
    ArrayList<ChargeScheduleVO> list1 = null;
    try
    {
      isSessionAvailable();
      list1 = new ArrayList();
      if (this.chargeVO != null)
      {
        logger.info("Entered show Enquiry Record Details & Fin details");
        chargBD = new ChargeScheduleBD();
        String[] temp = this.enquiryDatas.split(":");
        String poNo = temp[0];
        String cifId = temp[1];
        this.chargeVO = chargBD.getEnquiryProcess(poNo, cifId);
       


        this.multiPaymentReferenceList = chargBD.loadFinanceProcess(poNo);
        setMultiPaymentReferenceList(this.multiPaymentReferenceList);
      }
      else
      {
        logger.info("boeSearchVO is null");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String showCheckerRecordDetails()
    throws ApplicationException
  {
    ChargeScheduleBD chargBD = null;
    ArrayList<ChargeScheduleVO> list1 = null;
    try
    {
      isSessionAvailable();
      list1 = new ArrayList();
      if (this.chargeVO != null)
      {
        logger.info(" show Checker Record Details");
        chargBD = new ChargeScheduleBD();
        String[] temp = this.poNocifID.split(":");
        String poNo = temp[0];
        String cifId = temp[1];
        this.chargeVO = chargBD.getCheckerProcess(poNo, cifId);
      }
      else
      {
        logger.info("boeSearchVO is null");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String approveSinglePO()
    throws ApplicationException
  {
    ChargeScheduleBD chargBD = null;
    try
    {
      isSessionAvailable();
      if (this.chargeVO != null)
      {
        logger.info(" show Checker Record Details");
        chargBD = new ChargeScheduleBD();
        this.chargeVO = chargBD.approveSinglePO(this.chargeVO);
      }
      else
      {
        logger.info("boeSearchVO is null");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String rejectSinglePO()
    throws ApplicationException
  {
    ChargeScheduleBD chargBD = null;
    try
    {
      isSessionAvailable();
      if (this.chargeVO != null)
      {
        logger.info(" show Checker Record Details");
        chargBD = new ChargeScheduleBD();
        this.chargeVO = chargBD.rejectSinglePO(this.chargeVO);
      }
      else
      {
        logger.info("boeSearchVO is null");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String execute()
    throws Exception
  {
    logger.info("Entering Method");
    HttpServletRequest request = ServletActionContext.getRequest();
    ChargeScheduleBD bd = null;
    String userName = null;
    String userID = null;
    try
    {
      bd = new ChargeScheduleBD();
      HttpServletRequest uRequest = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      userName = uRequest.getRemoteUser();
      if (userName == null) {
        userName = "SUPERVISOR";
      }
      logger.info(userName);
      userID = bd.getSessionUser(userName);
      HttpSession httpSession = request.getSession();
      logger.info("USERID------>" + userID);
      httpSession.setAttribute("USERID", userID);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String customerCifCode()
    throws Exception
  {
    logger.info("Entering Method");
    this.cusDataVo = null;
    try
    {
      this.cusDataVo = new CustomerDataVO();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String incoTerms()
    throws Exception
  {
    logger.info("Entering Method");
    this.cusDataVo = null;
    try
    {
      this.cusDataVo = new CustomerDataVO();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String calculate()
  {
    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      this.chargeVO = bd.fetchvalues(this.chargeVO);
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    return "success";
  }
 
  public String fetchBenName()
  {
    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      this.chargeVO = bd.fetchBeneficiary(this.chargeVO);
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    return "success";
  }
 
  public void Errors()
    throws Exception
  {
    logger.info("Entering Method");
    ChargeScheduleBD bd = null;
    String errorCode = "";
    try
    {
      bd = new ChargeScheduleBD();
      if (this.chargeVO != null) {
        bd.getErrors(errorCode, this.chargeVO);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
  }
 
  public String fetchCustomer()
    throws Exception
  {
    logger.info("Entering Method");
   

    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      this.customerList = new ArrayList();
      if (this.cusDataVo != null) {
        if ((CommonMethods.isNull(this.cusDataVo.getCifID())) &&
          (CommonMethods.isNull(this.cusDataVo.getBeneficiaryName())))
        {
          this.customerList = bd.getCustomerList(this.customerList);
        }
        else
        {
          logger.info("CIF ID IS" + this.cusDataVo.getCifID());
          this.customerList = bd.filterCustomer(this.cusDataVo, this.customerList);
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
 
  public String fetchIncoTerms()
    throws Exception
  {
    logger.info("Entering Method");
   

    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      this.customerList = new ArrayList();
      if (this.cusDataVo != null) {
        if (CommonMethods.isNull(this.cusDataVo.getIncoTerms())) {
          this.customerList = bd.fetchIncoTerms(this.customerList);
        } else {
          this.customerList = bd.filterIncoTerms(this.cusDataVo, this.customerList);
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
 
  public String gotoHome()
    throws Exception
  {
    logger.info("Entering Method");
    try
    {
      if (this.chargeVO != null)
      {
        logger.info("CIF ID==>" + this.chargeVO.getCifID());
        this.chargeVO.setCifID(this.chargeVO.getCifID());
        this.chargeVO.setBeneficiaryName(this.chargeVO.getBeneficiaryName());
        logger.info("chargeVO.setBeneficiaryName==>" + this.chargeVO.getBeneficiaryName());
        this.chargeVO.setMarginPercentage(this.chargeVO.getMarginPercentage());
        logger.info("chargeVO.setMarginPercentage==>" + this.chargeVO.getMarginPercentage());
        this.chargeVO.setIncoTerms(this.chargeVO.getIncoTerms());
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    return "success";
  }
 
  public String chargeType()
    throws Exception
  {
    logger.info("Entering Method");
    this.chargeSelectionVO = null;
    try
    {
      this.chargeSelectionVO = new ChargeSelectionVO();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchChargeList()
    throws Exception
  {
    logger.info("Entering Method");
    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      this.chargeList = new ArrayList();
      if (this.chargeSelectionVO != null) {
        if ((CommonMethods.isNull(this.chargeSelectionVO.getFilterChargeCode())) &&
          (CommonMethods.isNull(this.chargeSelectionVO.getFilterChargeDesc()))) {
          this.chargeList = bd.getChargeList(this.chargeList);
        } else {
          this.chargeList = bd.filterChargeList(this.chargeSelectionVO, this.chargeList);
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
 
  public String productType()
    throws Exception
  {
    logger.info("Entering Method");
    this.productVo = null;
    try
    {
      this.productVo = new ProductSelectionVO();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchProductList()
    throws Exception
  {
    logger.info("Entering Method");
    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      this.productList = new ArrayList();
      if (this.productVo != null) {
        if ((CommonMethods.isNull(this.productVo.getFilterProductCode())) &&
          (CommonMethods.isNull(this.productVo.getFilterProductDesc()))) {
          this.productList = bd.getProductList(this.productList);
        } else {
          this.productList = bd.filterProductList(this.productVo, this.productList);
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
 
  public String fetchChargeScheduleList()
    throws Exception
  {
    logger.info("Entering Method");
    ChargeScheduleBD bd = null;
    ChargeScheduleDAO dao = null;
    try
    {
      bd = new ChargeScheduleBD();
      this.chargeScheduleList = new ArrayList();
      if (this.chargeVO != null) {
        if ((CommonMethods.isNull(this.chargeVO.getChargeType())) && (CommonMethods.isNull(this.chargeVO.getCustomerCif()))) {
          this.chargeScheduleList = bd.getChargeScheduleList(this.chargeScheduleList);
        } else {
          this.chargeScheduleList = bd.filterChargeScheduleList(this.chargeVO, this.chargeScheduleList);
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
 
  public String newChargeSchedule()
    throws Exception
  {
    logger.info("Entering Method");
    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      if (this.chargeVO != null) {
        this.chargeScheduleList = bd.createChargeSchedule(this.chargeVO, this.chargeScheduleList);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String updateChargeSchedule()
    throws Exception
  {
    logger.info("Entering Method");
    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      if (this.chargeVO != null) {
        this.chargeScheduleList = bd.updateChargeSchedule(this.chargeVO, this.chargeScheduleList);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String deleteChargeSchedule()
    throws Exception
  {
    logger.info("Entering Method");
    ChargeScheduleBD bd = null;
    try
    {
      bd = new ChargeScheduleBD();
      if (this.chargeVO != null) {
        this.chargeScheduleList = bd.delChargeSchedule(this.chargeVO, this.chargeScheduleList);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String updateStatusAction()
    throws ApplicationException
  {
    logger.info("Entering Method");
    ChargeScheduleBD chargBD = null;
    try
    {
      logger.info("Coming");
      chargBD = ChargeScheduleBD.getBD();
      if ((this.chkList != null) && (this.check != null)) {
        chargBD.updateStatus(this.chkList, this.check, this.remarks);
      }
      this.multiPaymentReferenceList = chargBD.loadMultiPaymentReferenceData(this.chargeVO);
     
      setMultiPaymentReferenceList(this.multiPaymentReferenceList);
    }
    catch (BusinessException exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    this.remarks = "";
    return "success";
  }
 
  public String closeWindow()
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String closeUrl = "";
    try
    {
      con = DBConnectionUtility.getConnection();
     




      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = 'closeURL'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        closeUrl = rs.getString("VALUE1");
      }
      HttpServletResponse response = (HttpServletResponse)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
      HttpSession ses = request.getSession(false);
      if (ses != null)
      {
        String sesId = ses.getId();
        ses.invalidate();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
          if (sesId.equalsIgnoreCase(cookie.getValue()))
          {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setDomain(request.getServerName());
            cookie.setPath(request.getServletContext().getContextPath() + "/");
            cookie.setSecure(request.isSecure());
            response.addCookie(cookie);
            break;
          }
        }
      }
      response.sendRedirect(closeUrl);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return "none";
  }
 
  public String insertExport()
    throws Exception
  {
    ChargeScheduleBD bd = null;
    if (this.chargeVO != null) {
      bd = new ChargeScheduleBD();
    }
    try
    {
      this.chargeVO = bd.insertExport(this.chargeVO);
      if (this.chargeVO.getCount() > 0) {
        this.chargeVO = new ChargeScheduleVO();
      }
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return "success";
  }
 
  public String purchaseOrderValidations()
    throws Exception
  {
    ChargeScheduleBD bd = null;
    if (this.chargeVO != null) {
      bd = new ChargeScheduleBD();
    }
    try
    {
      this.chargeVO = bd.purchaseOrderValidations(this.chargeVO);
      if (this.chargeVO.getCount() > 0) {
        this.chargeVO = new ChargeScheduleVO();
      }
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return "success";
  }
 
  public String searchPurchaseOrder()
    throws Exception
  {
    ChargeScheduleBD bd = null;
    try
    {
      if (this.chargeVO != null)
      {
        bd = new ChargeScheduleBD();
        this.chargeVO = bd.searchPurchaseDetails(this.chargeVO);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
 
  public String fetchPurchaseOrder()
    throws Exception
  {
    ChargeScheduleBD bd = null;
    if (this.chargeVO != null) {
      bd = new ChargeScheduleBD();
    }
    try
    {
      this.chargeVO = bd.fetchPurchaseOrder(this.chargeVO);
      this.chargeVO.setPurchaseOrderList(new ArrayList());
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return "success";
  }
 
  public String resetval()
  {
    try
    {
      this.chargeVO = new ChargeScheduleVO();
    }
    catch (Exception e)
    {
      logger.info("Exception in resetval " + e.getMessage());
    }
    return "success";
  }
 
  public String fetchGoodsCode()
  {
    return "success";
  }
 
  public String searchGoodsCode()
    throws ApplicationException
  {
    ChargeScheduleBD bd = null;
    try
    {
      bd = ChargeScheduleBD.getBD();
      this.goodsList = bd.searchGoodsCode(this.cusDataVo);
      logger.info("Goods List Size" + this.goodsList.size());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
 
  public String setGoodsCodeValue()
    throws ApplicationException
  {
    try
    {
      this.chargeVO.setGoodsCode(this.cusDataVo.getGoodsCode());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
 
  public String goToInwardPage()
    throws ApplicationException
  {
    try
    {
      if ((this.chargeVO != null) && ((this.chargeVO.getCifID() instanceof String)))
      {
        this.cusDataVo = new CustomerDataVO();
        this.cusDataVo.setCustNo(this.chargeVO.getCifID());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
 
  public String fetchInwardDetails()
    throws ApplicationException
  {
    ChargeScheduleBD bd = null;
    try
    {
      bd = ChargeScheduleBD.getBD();
      this.inwardDocList = bd.fetchInwardDetails(this.cusDataVo);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
 
  public String setMasterDetails()
    throws ApplicationException
  {
    ChargeScheduleBD bd = null;
    String masRef = "";
    try
    {
      masRef = this.cusDataVo.getMaster();
     
      bd = ChargeScheduleBD.getBD();
      this.chargeVO = bd.setMasterDetails(this.chargeVO, masRef);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
 
  public String gotoPurchaseOrderScreen()
    throws ApplicationException
  {
    try
    {
      this.chargeVO.setExportOrderNumber(this.chargeVO.getExportOrderNumber());
      this.chargeVO.setCifID(this.chargeVO.getCifID());
      fetchPurchaseOrder();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
 
  public String copyPurchaseOrderDetails()
  {
    logger.info("Value " + this.chargeVO.getCopyVal());
    try
    {
      if (((this.chargeVO.getCopyVal() instanceof String)) && (!this.chargeVO.getCopyVal().equalsIgnoreCase("")))
      {
        this.chargeVO.setExportOrderNumber(this.chargeVO.getCopyVal());
        fetchPurchaseOrder();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
}
