package in.co.prishipment.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.prishipment.businessdelegate.PreShipmentBD;
import in.co.prishipment.utility.ActionConstants;
import in.co.prishipment.utility.CommonMethods;
import in.co.prishipment.utility.DBConnectionUtility;
import in.co.prishipment.utility.LoggableStatement;
import in.co.prishipment.vo.AlertMessagesVO;
import in.co.prishipment.vo.CustomerDataVO;
import in.co.prishipment.vo.PreShipDeleteVO;
import in.co.prishipment.vo.PreShipmentVO;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class PreShipmentAction
  extends PreShipmentBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(PreShipmentAction.class.getName());
  private PreShipmentVO preShipVo = null;
  private PreShipDeleteVO preShipDelVO = null;
  ArrayList<AlertMessagesVO> errorList;
  PreShipmentVO productlist;
  CustomerDataVO cusDataVo;
  String sessionId;
  String buttonEnables;
  ArrayList<CustomerDataVO> customerList;
  public boolean preshipbool;
  Map<String, String> prolist;
  Map<String, String> faclist;
  String[] chkList = null;
  String check = null;
 
  public String getCheck()
  {
    return this.check;
  }
 
  public void setCheck(String check)
  {
    this.check = check;
  }
 
  public String[] getChkList()
  {
    return this.chkList;
  }
 
  public void setChkList(String[] chkList)
  {
    this.chkList = chkList;
  }
 
  public String getButtonEnables()
  {
    return this.buttonEnables;
  }
 
  public void setButtonEnables(String buttonEnables)
  {
    this.buttonEnables = buttonEnables;
  }
 
  public Map<String, String> getProlist()
  {
    return ActionConstants.REC;
  }
 
  public void setProlist(Map<String, String> prolist)
  {
    this.prolist = prolist;
  }
 
  public Map<String, String> getFaclist()
  {
    return ActionConstants.PROREC;
  }
 
  public void setFaclist(Map<String, String> faclist)
  {
    this.faclist = faclist;
  }
 
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
 
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
  }
 
  public PreShipmentVO getPreShipVo()
  {
    return this.preShipVo;
  }
 
  public void setPreShipVo(PreShipmentVO preShipVo)
  {
    this.preShipVo = preShipVo;
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
 
  public PreShipmentVO getProductlist()
  {
    return this.productlist;
  }
 
  public void setProductlist(PreShipmentVO productlist)
  {
    this.productlist = productlist;
  }
 
  public String execute()
    throws Exception
  {
    LoggableStatement ppt = null;
    Connection con = null;
    ResultSet rs = null;
    String Query = null;
    this.preshipbool = true;
    String Amtonly = "";
    String curr = "";
   
    logger.info("Entering Method");
    PreShipmentBD bd = null;
    double divide_val = 0.0D;
    try
    {
      bd = PreShipmentBD.getBD();
      bd.getTiDate();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
      String cif = CommonMethods.nullAndTrimString(request.getParameter("cifCode"));
      String masterRef = CommonMethods.nullAndTrimString(request.getParameter("masterRef"));
      String eventRef = CommonMethods.nullAndTrimString(request.getParameter("eventRef"));
      String amtWithCurr = CommonMethods.nullAndTrimString(request.getParameter("repayAmt"));
      String behalfBranch = CommonMethods.nullAndTrimString(request.getParameter("behalfOfBranch"));
      String preship_value_date = CommonMethods.nullAndTrimString(request.getParameter("valueDate"));
     


























      logger.info("Preshipment Date--preship_value_date--------" + preship_value_date);
      amtWithCurr = amtWithCurr.toUpperCase();
      this.sessionId = CommonMethods.nullAndTrimString(request.getSession().getId());
     
      logger.info("cif \t\t: " + cif);
      logger.info("masterRef \t: " + masterRef);
      logger.info("eventRef \t: " + eventRef);
      logger.info("amtWithCurr: " + amtWithCurr);
      logger.info("behalfBranch : " + behalfBranch);
      logger.info("preship_value_date : " + preship_value_date);
      logger.info("amtWithCurr: " + amtWithCurr);
     
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      preship_value_date = new SimpleDateFormat("dd/MM/yy").format(format.parse(preship_value_date));
     
      logger.info("After Changes--------------------->" + preship_value_date);
     
      Amtonly = amtWithCurr.replaceAll("[\\D.]", "");
     
      logger.info("Amoutn val is----------------> " + Amtonly);
      curr = amtWithCurr.replaceAll("[^A-Z]", "");
     
      Double val = Double.valueOf(Amtonly);
      logger.info("Value in Double--------------> " + val);
      divide_val = bd.getDecimalPoints(curr);
     
      logger.info("Divided Value---------------------" + divide_val);
     
      val = Double.valueOf(val.doubleValue() / divide_val);
     


      BigDecimal value1 = new BigDecimal(val.doubleValue());
     
      Currency ccyNameCode = Currency.getInstance(curr);
      int precision = ccyNameCode.getDefaultFractionDigits();
      RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
     

      BigDecimal roundOffValue = null;
      roundOffValue = value1.setScale(precision, DEFAULT_ROUNDING);
     

      Amtonly = String.valueOf(roundOffValue);
     
      amtWithCurr = Amtonly + ' ' + curr;
      this.preShipVo = new PreShipmentVO();
      this.preShipVo.setCifCode(cif);
      this.preShipVo.setCurrency(curr);
      this.preShipVo.setMasterRef(masterRef);
     
      logger.info("GetEventRef Before: " + this.preShipVo.getEventRef());
      this.preShipVo.setEventRef(eventRef);
      logger.info("GetEventRef After: " + this.preShipVo.getEventRef());
     
      this.preShipVo.setAmtWithCurr(amtWithCurr);
      this.preShipVo.setValueDate(preship_value_date);
      this.preShipVo.setBehalfBranch(behalfBranch);
     
      con = DBConnectionUtility.getConnection();
      Query = "SELECT bev.status FROM master mas,BASEEVENT bev where mas.KEY97=bev.MASTER_KEY and trim(mas.MASTER_REF)= ? and TRIM(BEV.REFNO_PFIX) ||lPAD(TRIM(BEV.REFNO_SERL),3,0)=? ";
     


      ppt = new LoggableStatement(con, Query);
      ppt.setString(1, masterRef.trim());
      ppt.setString(2, eventRef.trim());
      rs = ppt.executeQuery();
      logger.info("Query " + ppt.getQueryString());
      logger.info(" " + ppt.getQueryString());
      if (rs.next()) {
        this.preShipVo.setBevstatus(rs.getString(1));
      } else {
        this.preShipVo.setBevstatus("i");
      }
      Query = "SELECT trim(STEPNAME) FROM (SELECT TRIM(M.MASTER_REF) AS MASTER_REF, TRIM(BE.REFNO_PFIX||lpad(BE.REFNO_SERL, 3,0)) AS EVENT_REF, TRIM(OS.ID) AS STEPNAME, TRIM(SH.STATUS) AS STEPSTATUS,TRIM(OS.FINALRVIEW), SH.TIMESTART ,TRIM(SH.TYPE) FROM MASTER M, BASEEVENT BE, STEPHIST SH, ORCH_MAP OM, ORCH_STEP OS  WHERE M.KEY97 = BE.MASTER_KEY AND BE.KEY97     = SH.EVENT_KEY  AND SH.ORCH_MAP  = OM.KEY97 AND OM.ORCH_STEP = OS.KEY97  AND trim(M.MASTER_REF) = ? AND trim(BE.REFNO_PFIX)||LPAD(trim(BE.REFNO_SERL),3,0) =? ORDER BY SH.TIMESTART DESC )  WHERE ROWNUM = 1";
     





      logger.info("Query ---->" + Query);
     
      ppt = new LoggableStatement(con, Query);
      ppt.setString(1, masterRef.trim());
      ppt.setString(2, eventRef.trim());
      rs = ppt.executeQuery();
      logger.info("Query " + ppt.getQueryString());
      String StepID = null;
      if (rs.next())
      {
        StepID = rs.getString(1).trim();
        this.preShipVo.setBevstatus("i");
      }
      else
      {
        this.preShipVo.setBevstatus("i");
      }
      logger.info("StepID------> " + StepID);
      if ((StepID.equalsIgnoreCase("Complete")) || (StepID.equalsIgnoreCase("Final print")) || (StepID.equalsIgnoreCase("Release")) ||
        (StepID.equalsIgnoreCase("Authorise")) || (StepID.equalsIgnoreCase("CBS Authoriser")) || (StepID.equalsIgnoreCase("Release pending")) ||
        (StepID.equalsIgnoreCase("Final limit chk")) || (StepID.equalsIgnoreCase("Watch list chk")) || (StepID.equalsIgnoreCase("Review")) ||
        (StepID.equalsIgnoreCase("Rate fixing")) || (StepID.equalsIgnoreCase("Fix auth")) || (StepID.equalsIgnoreCase("Scrutinizer 1")) ||
        (StepID.equalsIgnoreCase("Scrutinizer-1")) || (StepID.equalsIgnoreCase("Scrutinizer 2")) || (StepID.equalsIgnoreCase("CBS Checker"))) {
        this.preShipVo.setBevstatus("c");
      }
      logger.info("Base Event Status: " + this.preShipVo.getBevstatus());
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("landing Page Exception--------------->" + exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchPreShip()
    throws Exception
  {
    PreShipmentBD bd = null;
    System.out.println("PreshipmentAction : fetchPreShip()");
    try
    {
      if (this.preShipVo != null)
      {
        bd = new PreShipmentBD();
        this.preShipVo = bd.fetchShipment(this.preShipVo);
      }
      if (this.preShipVo != null) {
        this.preShipVo.getErrorList();
      }
    }
    catch (Exception e)
    {
      throwApplicationException(e);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String processPreship()
    throws Exception
  {
    logger.info("Entering Method");
    PreShipmentBD bd = null;
    try
    {
      if (this.preShipVo != null)
      {
        if ((this.preShipVo.getPreShipList() != null) && (this.preShipVo.getPreShipList().size() > 0))
        {
          bd = new PreShipmentBD();
          this.preShipVo = bd.processRepayment(this.preShipVo);
        }
        if ((this.preShipVo != null) &&
          ((this.preShipVo.getIsSubmitted() instanceof String)) &&
          (this.preShipVo.getIsSubmitted().trim().equalsIgnoreCase("Y"))) {
          setButtonEnables("Y");
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String deletePreship()
    throws Exception
  {
    logger.info("Entering Method");
    PreShipmentBD bd = null;
   
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchdeletePreship()
    throws Exception
  {
    logger.info("Entering Method");
    PreShipmentBD bd = null;
    try
    {
      bd = new PreShipmentBD();
      this.preShipVo = bd.fetchdeletePreship(this.preShipVo);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String deletefetchPreship()
    throws Exception
  {
    logger.info("Entering Method");
    PreShipmentBD bd = null;
    try
    {
      bd = new PreShipmentBD();
     
      this.preShipVo = bd.deletefetchPreship(this.preShipVo);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String closeWindow()
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs1 = null;
    String closeUrl = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='closeURL'";
     
      log = new LoggableStatement(con, query);
      rs1 = log.executeQuery();
      if (rs1.next()) {
        closeUrl = rs1.getString("VALUE1");
      }
      HttpServletResponse response = (HttpServletResponse)
        ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
      response.sendRedirect(closeUrl);
    }
    catch (Exception exception)
    {
      logger.info("Exception in Close Window Method--------" + exception.getMessage());
     
      throwApplicationException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs1);
    }
    logger.info("Exiting Method");
   
    return "none";
  }
}