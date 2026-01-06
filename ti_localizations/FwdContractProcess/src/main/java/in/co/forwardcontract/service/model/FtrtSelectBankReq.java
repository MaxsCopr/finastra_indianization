package in.co.forwardcontract.service.model;

public class FtrtSelectBankReq
{
  private String requestType;
  private String msgid;
  private FtrtSelectBankReqData data;
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
  public FtrtSelectBankReqData getData()
  {
    return this.data;
  }
  public void setData(FtrtSelectBankReqData data)
  {
    this.data = data;
  }
}
