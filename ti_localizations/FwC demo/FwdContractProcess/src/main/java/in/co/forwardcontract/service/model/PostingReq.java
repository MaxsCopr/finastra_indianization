package in.co.forwardcontract.service.model;

public class PostingReq
{
  private PostingReqTrnAddRequest XferTrnAddRequest;
  private String msgid;
  public PostingReqTrnAddRequest getXferTrnAddRequest()
  {
    return this.XferTrnAddRequest;
  }
  public void setXferTrnAddRequest(PostingReqTrnAddRequest xferTrnAddRequest)
  {
    this.XferTrnAddRequest = xferTrnAddRequest;
  }
  public String getMsgid()
  {
    return this.msgid;
  }
  public void setMsgid(String msgid)
  {
    this.msgid = msgid;
  }
}
