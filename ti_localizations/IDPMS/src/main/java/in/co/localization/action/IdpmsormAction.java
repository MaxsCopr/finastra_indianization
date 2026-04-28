package in.co.localization.action;
 
import in.co.localization.businessdelegate.localization.AdTransferBD;
import in.co.localization.businessdelegate.localization.IdpmsOrmBD;
import in.co.localization.dao.exception.ApplicationException;
import in.co.localization.utility.ActionConstants;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.ValidationUtility;
import in.co.localization.vo.localization.AlertMessagesVO;
import in.co.localization.vo.localization.BOEDataSearchVO;
import in.co.localization.vo.localization.IdpmsOrmVO;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class IdpmsormAction
  extends LocalizationBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(IdpmsormAction.class
    .getName());
  IdpmsOrmVO idvo;
  String[] checkList;
  String tempRef;
  private ArrayList<AlertMessagesVO> alertMsgArray = null;
  Map<String, String> adjCloInd;
  Map<String, String> approvedBy;
  Map<String, String> recordIndicator;
  String ormStatus = null;
  ArrayList<IdpmsOrmVO> idpmsOrmVOList;
  BOEDataSearchVO boeSearchVO;
  String[] ormChkList;
  String remarks;
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String[] getOrmChkList()
  {
    return this.ormChkList;
  }
  public void setOrmChkList(String[] ormChkList)
  {
    this.ormChkList = ormChkList;
  }
  public BOEDataSearchVO getBoeSearchVO()
  {
    return this.boeSearchVO;
  }
  public void setBoeSearchVO(BOEDataSearchVO boeSearchVO)
  {
    this.boeSearchVO = boeSearchVO;
  }
  public String getOrmStatus()
  {
    return this.ormStatus;
  }
  public void setOrmStatus(String ormStatus)
  {
    this.ormStatus = ormStatus;
  }
  public IdpmsOrmVO getIdvo()
  {
    return this.idvo;
  }
  public void setIdvo(IdpmsOrmVO idvo)
  {
    this.idvo = idvo;
  }
  public ArrayList<IdpmsOrmVO> getIdpmsOrmVOList()
  {
    return this.idpmsOrmVOList;
  }
  public void setIdpmsOrmVOList(ArrayList<IdpmsOrmVO> idpmsOrmVOList)
  {
    this.idpmsOrmVOList = idpmsOrmVOList;
  }
  public String[] getCheckList()
  {
    return this.checkList;
  }
  public void setCheckList(String[] checkList)
  {
    this.checkList = checkList;
  }
  public String getTempRef()
  {
    return this.tempRef;
  }
  public void setTempRef(String tempRef)
  {
    this.tempRef = tempRef;
  }
  public ArrayList<AlertMessagesVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
  public Map<String, String> getAdjCloInd()
  {
    return ActionConstants.ADJ_CLOSURE_IND;
  }
  public void setAdjCloInd(Map<String, String> adjCloInd)
  {
    this.adjCloInd = adjCloInd;
  }
  public Map<String, String> getApprovedBy()
  {
    return ActionConstants.APP_BY;
  }
  public void setApprovedBy(Map<String, String> approvedBy)
  {
    this.approvedBy = approvedBy;
  }
  public Map<String, String> getRecordIndicator()
  {
    return ActionConstants.REC_IND;
  }
  public void setRecordIndicator(Map<String, String> recordIndicator)
  {
    this.recordIndicator = recordIndicator;
  }
  public String execute()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String target = null;
    AdTransferBD bd = null;
    try
    {
      isSessionAvailable();
      bd = new AdTransferBD();
      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      String sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("IDPMS sessionUserName--------------------|" + sessionUserName);
      logger.info("sessionUserName--->" + sessionUserName);

      String pageType = "BOEMaker";

      int count = bd.checkLoginedUserType(sessionUserName, pageType);

      if (count > 0) {

        target = "success";

      } else {

        target = "fail";

      }

      if ((this.ormStatus != null) && (this.ormStatus.equalsIgnoreCase("1"))) {

        addActionMessage("ORM Closure details are successfully submitted");

      } else if ((this.ormStatus != null) && (this.ormStatus.equalsIgnoreCase("2"))) {

        addActionError("Error in ORM Closure details");

      }

    }

    catch (Exception exception)

    {

      logger.info("ORM Maker Exception-------------" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return target;

  }

  public String fetchorm()

    throws ApplicationException

  {

    logger.info("Entering Method");

    IdpmsOrmBD bd = null;

    try

    {

      bd = new IdpmsOrmBD();

      if (this.idvo != null) {

        this.idvo = bd.fetchormData(this.idvo);

      }

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String insertOrm()

    throws ApplicationException

  {

    logger.info("Entering Method");

    IdpmsOrmBD bd = null;

    int result = 0;

    String target = null;

    try

    {

      bd = new IdpmsOrmBD();

      validateForm();

      if (this.alertMsgArray.size() == 0)

      {

        result = bd.insertOrm(this.idvo);

        if (result == 1) {

          this.ormStatus = "1";

        } else {

          this.ormStatus = "2";

        }

        target = "success";

      }

      else if ((this.alertMsgArray.size() == 1) || (this.alertMsgArray.size() == 2))

      {

        String errorMsg = ((AlertMessagesVO)this.alertMsgArray.get(0)).getErrorMsg();

        if ((errorMsg != null) && 

          (errorMsg.equalsIgnoreCase("Y")))

        {

          result = bd.insertOrm(this.idvo);

          if (result == 1) {

            this.ormStatus = "1";

          } else {

            this.ormStatus = "2";

          }

          target = "success";

        }

        else

        {

          target = "Error";

        }

      }

      else

      {

        logger.info("ORM Closure Error Page");

        target = "Error";

      }

    }

    catch (Exception e)

    {

      throwApplicationException(e);

    }

    logger.info("Entering Method");

    return target;

  }

  public void validateForm()

    throws ApplicationException

  {

    IdpmsOrmBD bd = null;

    CommonMethods commonMethods = null;

    try

    {

      this.alertMsgArray = new ArrayList();

      bd = new IdpmsOrmBD();

      commonMethods = new CommonMethods();

      if ((this.alertMsgArray != null) && 

        (this.alertMsgArray.size() > 0)) {

        this.alertMsgArray.clear();

      }

      if (!commonMethods.isNull(this.idvo.getOcprtcde()))

      {

        boolean bPortCodeStatus = ValidationUtility.isValiedPortCode(this.idvo.getOcprtcde());

        if (!bPortCodeStatus)

        {

          String errorcode = "Invalid Port Code";

          Object[] arg = { "0", "E", errorcode, "Input" };

          setErrorvalues(arg);

        }

      }

      if (commonMethods.isNull(this.idvo.getOclosdat()))

      {

        String errorcode = commonMethods.getErrorDesc("ORM001", "N100");

        Object[] arg = { "0", "E", errorcode, "Input" };

        setErrorvalues(arg);

      }

      if (commonMethods.isNull(this.idvo.getOadjcloind()))

      {

        String errorcode = commonMethods.getErrorDesc("ORM003", "N100");

        Object[] arg = { "0", "E", errorcode, "Input" };

        setErrorvalues(arg);

      }

      else if (this.idvo.getOadjcloind().equalsIgnoreCase("2"))

      {

        if (commonMethods.isNull(this.idvo.getOdocnum()))

        {

          String errorcode = commonMethods.getErrorDesc("ORM007", "N100");

          Object[] arg = { "0", "E", errorcode, "Input" };

          setErrorvalues(arg);

        }

        if (commonMethods.isNull(this.idvo.getOdocdate()))

        {

          String errorcode = commonMethods.getErrorDesc("ORM008", "N100");

          Object[] arg = { "0", "E", errorcode, "Input" };

          setErrorvalues(arg);

        }

      }

      if (commonMethods.isNull(this.idvo.getApprovedBy()))

      {

        String errorcode = commonMethods.getErrorDesc("ORM004", "N100");

        Object[] arg = { "0", "E", errorcode, "Input" };

        setErrorvalues(arg);

      }

      else if (this.idvo.getApprovedBy().equalsIgnoreCase("1"))

      {

        if (commonMethods.isNull(this.idvo.getOcltrno()))

        {

          String errorcode = commonMethods.getErrorDesc("ORM005", "N100");

          Object[] arg = { "0", "E", errorcode, "Input" };

          setErrorvalues(arg);

        }

        if (commonMethods.isNull(this.idvo.getOltrdate()))

        {

          String errorcode = commonMethods.getErrorDesc("ORM006", "N100");

          Object[] arg = { "0", "E", errorcode, "Input" };

          setErrorvalues(arg);
        }

      }

      if (!commonMethods.isNull(this.idvo.getOclosamt()))

      {

        double closuerAmt = Double.valueOf(this.idvo.getOclosamt()).doubleValue();

        double payAmt = Double.valueOf(this.idvo.getOamt()).doubleValue();

        double calcAmt = 0.0D;

        if (closuerAmt > 0.0D)

        {

          calcAmt = 5.0D * payAmt / 100.0D;

          if (closuerAmt > calcAmt)

          {

            String errorcode = commonMethods.getErrorDesc("ORM009", "N100");

            Object[] arg = { "0", "W", errorcode, "Input" };

            setErrorvalues(arg);

          }

        }

        else if (closuerAmt == 0.0D)

        {

          String errorcode = commonMethods.getErrorDesc("ORM012", "N100");

          Object[] arg = { "0", "E", errorcode, "Input" };

          setErrorvalues(arg);

        }

      }

      else if (commonMethods.isNull(this.idvo.getOclosamt()))

      {

        String errorcode = commonMethods.getErrorDesc("ORM012", "N100");

        Object[] arg = { "0", "E", errorcode, "Input" };

        setErrorvalues(arg);

      }

      if (commonMethods.isNull(this.idvo.getRecInd()))

      {

        String errorcode = commonMethods.getErrorDesc("ORM002", "N100");

        Object[] arg = { "0", "E", errorcode, "Input" };

        setErrorvalues(arg);

      }

      if ((!commonMethods.isNull(this.idvo.getOrefNum())) && (!commonMethods.isNull(this.idvo.getOclosamt()))) {

        if (checkOrmOutstandingAmount(this.idvo.getOrefNum().trim(), this.idvo.getOclosamt()))

        {

          String errorcode = commonMethods.getErrorDesc("ORM011", "N100");

          Object[] arg = { "0", "E", errorcode, "Input" };

          setErrorvalues(arg);

        }

      }

      int ormCount = bd.getORMCount(this.idvo);

      if (ormCount > 0)

      {

        String errorcode = commonMethods.getErrorDesc("ORM013", "N100");

        Object[] arg = { "0", "E", errorcode, "Input" };

        setErrorvalues(arg);

      }

      int ormACKCount = bd.getORMAckCount(this.idvo);

      if (ormACKCount == -1)

      {

        String errorcode = commonMethods.getErrorDesc("ORM014", "N100");

        Object[] arg = { "0", "W", errorcode, "Input" };

        setErrorvalues(arg);

      }

      if (ormACKCount == 0)

      {

        String errorcode = commonMethods.getErrorDesc("ORM014", "N100");

        Object[] arg = { "0", "E", errorcode, "Input" };

        setErrorvalues(arg);

      }

      for (int a = 0; a < this.alertMsgArray.size(); a++) {

        if (((AlertMessagesVO)this.alertMsgArray.get(a)).getErrorMsg().equalsIgnoreCase("N")) {

          if ((this.idvo.getOverridStatus() != null) && 

            (this.idvo.getOverridStatus().equalsIgnoreCase("Y")))

          {

            ((AlertMessagesVO)this.alertMsgArray.get(a)).setErrorMsg("Y");

            this.idvo.setOverridStatus("Y");

          }

          else if ((this.idvo.getOverridStatus() != null) && 

            (this.idvo.getOverridStatus().equalsIgnoreCase("N")))

          {

            ((AlertMessagesVO)this.alertMsgArray.get(a)).setErrorMsg("N");

            this.idvo.setOverridStatus("N");

          }

        }

      }

    }

    catch (Exception e)

    {

      e.printStackTrace();

      throwApplicationException(e);

    }

  }

  public boolean checkOrmOutstandingAmount(String ormNo, String closureAmt)

    throws ApplicationException

  {

    IdpmsOrmBD bd = null;

    boolean result = true;

    try

    {

      bd = IdpmsOrmBD.getBD();

      result = bd.checkOrmOutstandingAmount(ormNo, closureAmt);

    }

    catch (Exception e)

    {

      throwApplicationException(e);

    }

    return result;

  }

  public String updateWarning()

    throws ApplicationException

  {

    try

    {

      validateForm();

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    return "success";

  }

  public void setErrorvalues(Object[] arg)

  {

    logger.info("Entering Method");

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

      "Error");

    this.alertMsgArray.add(altMsg);

    logger.info("Exiting Method");

  }

  public String ormChecker()

    throws ApplicationException

  {

    logger.info("Entering Method");

    IdpmsOrmBD bd = null;

    String target = null;

    AdTransferBD adBD = null;

    try

    {

      isSessionAvailable();

 
 
      adBD = new AdTransferBD();

      bd = new IdpmsOrmBD();

      HttpSession session = ServletActionContext.getRequest().getSession();

      String sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("ormChecker sessionUserName--->" + sessionUserName);
      String pageType = "BOEChecker";

      int count = adBD.checkLoginedUserType(sessionUserName, pageType);

      logger.info("ormChecker checkLoginedUserType count--->" + count);

      if (count > 0)

      {

        if (this.boeSearchVO != null) {

          this.idpmsOrmVOList = bd.idpmsOrmList(this.boeSearchVO);

        }

        target = "success";

      }

      else

      {

        target = "fail";

      }

    }

    catch (Exception e)

    {

      logger.info("ormChecker exceptionerror values of action " + e.getMessage());

    }

    return target;

  }

  public String ormAuthorize()

    throws ApplicationException

  {

    logger.info("--------------ormAuthorize--------------");

    IdpmsOrmBD bd = null;

    try

    {

      bd = new IdpmsOrmBD();

      if (this.boeSearchVO != null) {

        this.idpmsOrmVOList = bd.insertOrm(this.ormChkList, this.ormStatus, this.remarks, this.boeSearchVO);

      }

      this.remarks = "";

    }

    catch (Exception e)

    {

      logger.info("error values of action " + e.getMessage());

    }

    return "success";

  }

  public String ormView()

    throws ApplicationException

  {

    logger.info("Entering Method");

    IdpmsOrmBD bd = null;

    try

    {

      bd = new IdpmsOrmBD();

      if (this.boeSearchVO != null) {

        this.idvo = bd.ormVIEW(this.boeSearchVO);

      }

    }

    catch (Exception e)

    {

      logger.info("error values of action " + e.getMessage());

    }

    return "success";

  }

}