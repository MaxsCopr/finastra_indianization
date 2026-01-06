package in.co.forwardcontract.service.model;

public class LimitBlockBankReq
{
  private String requestType;
  private String msgid;
  private LimitBlockBankReqData data;
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
  public LimitBlockBankReqData getData()
  {
    return this.data;
  }
  public void setData(LimitBlockBankReqData data)
  {
    this.data = data;
  }
}
