package in.co.forwardcontract.service.model;

public class LimitFetchBankResNew
{
  private LimitFetchBankResDataNew data;
  private String msgrrn;
  private String msgtime;
  private String msgid;
  private String channelName;
  private String http_statusCode;
  private String status;
  public LimitFetchBankResDataNew getData()
  {
    return this.data;
  }
  public void setData(LimitFetchBankResDataNew data)
  {
    this.data = data;
  }
  public String getMsgrrn()
  {
    return this.msgrrn;
  }
  public void setMsgrrn(String msgrrn)
  {
    this.msgrrn = msgrrn;
  }
  public String getMsgtime()
  {
    return this.msgtime;
  }
  public void setMsgtime(String msgtime)
  {
    this.msgtime = msgtime;
  }
  public String getMsgid()
  {
    return this.msgid;
  }
  public void setMsgid(String msgid)
  {
    this.msgid = msgid;
  }
  public String getChannelName()
  {
    return this.channelName;
  }
  public void setChannelName(String channelName)
  {
    this.channelName = channelName;
  }
  public String getHttp_statusCode()
  {
    return this.http_statusCode;
  }
  public void setHttp_statusCode(String http_statusCode)
  {
    this.http_statusCode = http_statusCode;
  }
  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
}