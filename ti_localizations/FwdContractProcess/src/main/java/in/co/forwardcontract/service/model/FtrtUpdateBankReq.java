package in.co.forwardcontract.service.model;

public class FtrtUpdateBankReq
{
  private String requestType;
  private String msgid;
  private FtrtUpdateBankReqData data;
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
  public FtrtUpdateBankReqData getData()
  {
    return this.data;
  }
  public void setData(FtrtUpdateBankReqData data)
  {
    this.data = data;
  }
}
