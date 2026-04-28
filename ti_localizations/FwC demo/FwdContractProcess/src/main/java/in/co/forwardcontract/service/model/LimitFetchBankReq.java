package in.co.forwardcontract.service.model;

public class LimitFetchBankReq
{
  private String requestType;
  private String msgid;
  private LimitFetchCustomerReq data;
  public String getRequestType()
  {
    return this.requestType;
  }
  public void setRequestType(String requestType)
  {
    this.requestType = requestType;
  }
  public String getMsgid()
  {
    return this.msgid;
  }
  public void setMsgid(String msgid)
  {
    this.msgid = msgid;
  }
  public LimitFetchCustomerReq getData()
  {
    return this.data;
  }
  public void setData(LimitFetchCustomerReq data)
  {
    this.data = data;
  }
}