package in.co.stp.utility;

import in.co.stp.action.BaseAction;
import in.co.stp.dao.exception.ApplicationException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class DataGridUtility
  extends BaseAction
{
  public static final long serialVersionUID = -4523852525797859925L;
  public int sEcho;
  public int iTotalRecords;
  public int iTotalDisplayRecords;
  public ArrayList<Object> aaData;
  public int iDisplayLength;
  public int iDisplayStart;
  public ArrayList<String> addOnScripts = new ArrayList();
  public ArrayList<String> addOnCss = new ArrayList();
  private static Logger logger = LogManager.getLogger(DataGridUtility.class
    .getName());
  public BaseAction baseAction = null;
  public void throwApplicationException(Exception exception)
    throws ApplicationException
  {
    logger.error(exception.fillInStackTrace());
    LogHelper.logError(logger, exception);
    throw new ApplicationException(exception.getMessage());
  }
  public int getsEcho()
  {
    return this.sEcho;
  }
  public void setSEcho(int sEcho)
  {
    this.sEcho = sEcho;
  }
  public int getiTotalRecords()
  {
    return this.iTotalRecords;
  }
  public void setITotalRecords(int iTotalRecords)
  {
    this.iTotalRecords = iTotalRecords;
  }
  public int getiTotalDisplayRecords()
  {
    return this.iTotalDisplayRecords;
  }
  public void setITotalDisplayRecords(int iTotalDisplayRecords)
  {
    this.iTotalDisplayRecords = iTotalDisplayRecords;
  }
  public ArrayList<Object> getAaData()
  {
    return this.aaData;
  }
  public void setAaData(ArrayList<Object> aaData)
  {
    this.aaData = aaData;
  }
  public int getiDisplayLength()
  {
    return this.iDisplayLength;
  }
  public void setIDisplayLength(int iDisplayLength)
  {
    this.iDisplayLength = iDisplayLength;
  }
  public int getiDisplayStart()
  {
    return this.iDisplayStart;
  }
  public void setIDisplayStart(int iDisplayStart)
  {
    this.iDisplayStart = iDisplayStart;
  }
}