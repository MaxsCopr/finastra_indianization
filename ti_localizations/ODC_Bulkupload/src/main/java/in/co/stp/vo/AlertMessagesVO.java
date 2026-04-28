package in.co.stp.vo;

public class AlertMessagesVO
{
  private int rollNo;
  private String errorId;
  private String errorCode;
  private String errorDesc;
  private String errorMsg;
  private String errorDetails;
  private String type;
  private String changeDesc;
  public String getType()
  {
    return this.type;
  }
  public void setType(String type)
  {
    this.type = type;
  }
  public String getChangeDesc()
  {
    return this.changeDesc;
  }
  public void setChangeDesc(String changeDesc)
  {
    this.changeDesc = changeDesc;
  }
  public int getRollNo()
  {
    return this.rollNo;
  }
  public void setRollNo(int rollNo)
  {
    this.rollNo = rollNo;
  }
  public String getErrorId()
  {
    return this.errorId;
  }
  public void setErrorId(String errorId)
  {
    this.errorId = errorId;
  }
  public String getErrorCode()
  {
    return this.errorCode;
  }
  public void setErrorCode(String errorCode)
  {
    this.errorCode = errorCode;
  }
  public String getErrorDesc()
  {
    return this.errorDesc;
  }
  public void setErrorDesc(String errorDesc)
  {
    this.errorDesc = errorDesc;
  }
  public String getErrorMsg()
  {
    return this.errorMsg;
  }
  public void setErrorMsg(String errorMsg)
  {
    this.errorMsg = errorMsg;
  }
  public String getErrorDetails()
  {
    return this.errorDetails;
  }
  public void setErrorDetails(String errorDetails)
  {
    this.errorDetails = errorDetails;
  }
}
