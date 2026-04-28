package in.co.forwardcontract.service.model;

public class AccountStatusReq

{

  private String requestType;

  private String msgid;

  private ReqAccStatusData data;

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

  public ReqAccStatusData getData()

  {

    return this.data;

  }

  public void setData(ReqAccStatusData data)

  {

    this.data = data;

  }

}

 
