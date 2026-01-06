package in.co.stp.action;

import in.co.stp.businessdelegate.CheckerHomeBD;
import in.co.stp.dao.exception.ApplicationException;
import in.co.stp.utility.DataGridUtility;
import in.co.stp.utility.DataGridVO;
import in.co.stp.vo.CheckerHomeVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class CheckerHomeJSONAction
  extends DataGridUtility
{
  private static Logger logger = LogManager.getLogger(CheckerHomeJSONAction.class.getName());
  private static final long serialVersionUID = 1L;
  DataGridVO daVO = null;
  private String invoiceAjaxListval;
  CheckerHomeVO checkervo = null;
  String jsonResponse;
  public String getInvoiceAjaxListval()
  {
    return this.invoiceAjaxListval;
  }
  public void setInvoiceAjaxListval(String invoiceAjaxListval)
  {
    this.invoiceAjaxListval = invoiceAjaxListval;
  }
  public CheckerHomeVO getCheckervo()
  {
    return this.checkervo;
  }
  public void setCheckervo(CheckerHomeVO checkervo)
  {
    this.checkervo = checkervo;
  }
  public DataGridVO getDaVO()
  {
    return this.daVO;
  }
  public void setDaVO(DataGridVO daVO)
  {
    this.daVO = daVO;
  }
  public String getJsonResponse()
  {
    return this.jsonResponse;
  }
  public void setJsonResponse(String jsonResponse)
  {
    this.jsonResponse = jsonResponse;
  }
  public String execute()
    throws ApplicationException
  {
    logger.info("Entering Method");
    CheckerHomeBD bd = null;
    try
    {
      bd = CheckerHomeBD.getBD();
      this.daVO = new DataGridVO();
      this.daVO.setiDisplayStart(this.iDisplayStart);
      this.daVO.setiDisplayLength(this.iDisplayLength);
      this.daVO = bd.fetchInvoiceList(this.invoiceAjaxListval, this.checkervo, this.daVO);

 
      this.aaData = this.daVO.getAaData();
      this.iTotalRecords = this.daVO.getiTotalRecords();
      this.iTotalDisplayRecords = this.daVO.getiTotalDisplayRecords();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "json";
  }
}
