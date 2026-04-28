package in.co.forwardcontract.service.model;

public class TreasUpdateBankReq
{
  private String requestType;
  private String msgid;
  private TreasUpdateBankReqData data;
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
  public TreasUpdateBankReqData getData()
  {
    return this.data;
  }
  public void setData(TreasUpdateBankReqData data)
  {
    this.data = data;
  }
}
