package in.co.forwardcontract.service.model;

public class PostingRes
{
  private String Status;
  private String TransactionDt;
  private String ResponseDesc;
  private String TransactionId;
  private String Message;
  private String ErrorDesc;
  public String getStatus()
  {
    return this.Status;
  }
  public void setStatus(String status)
  {
    this.Status = status;
  }
  public String getTransactionDt()
  {
    return this.TransactionDt;
  }
  public void setTransactionDt(String transactionDt)
  {
    this.TransactionDt = transactionDt;
  }
  public String getResponseDesc()
  {
    return this.ResponseDesc;
  }
  public void setResponseDesc(String responseDesc)
  {
    this.ResponseDesc = responseDesc;
  }
  public String getTransactionId()
  {
    return this.TransactionId;
  }
  public void setTransactionId(String transactionId)
  {
    this.TransactionId = transactionId;
  }
  public String getMessage()
  {
    return this.Message;
  }
  public void setMessage(String message)
  {
    this.Message = message;
  }
  public String getErrorDesc()
  {
    return this.ErrorDesc;
  }
  public void setErrorDesc(String errorDesc)
  {
    this.ErrorDesc = errorDesc;
  }
}

