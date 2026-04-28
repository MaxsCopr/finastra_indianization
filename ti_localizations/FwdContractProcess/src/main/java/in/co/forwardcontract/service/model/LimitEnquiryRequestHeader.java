package in.co.forwardcontract.service.model;

public class LimitEnquiryRequestHeader
{
  private String requestType;
  private String msgid;
  private LimitEnquiryRequestData data;
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
  public LimitEnquiryRequestData getData()
  {
    return this.data;
  }
  public void setData(LimitEnquiryRequestData data)
  {
    this.data = data;
  }
}
