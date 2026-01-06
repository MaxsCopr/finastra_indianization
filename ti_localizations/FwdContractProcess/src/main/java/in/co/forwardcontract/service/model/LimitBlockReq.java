package in.co.forwardcontract.service.model;

public class LimitBlockReq
{
  private ReqModifyLimitNodeInputVO ModifyLimitNodeInputVO;
  private String msgid;
  public ReqModifyLimitNodeInputVO getModifyLimitNodeInputVO()
  {
    return this.ModifyLimitNodeInputVO;
  }
  public void setModifyLimitNodeInputVO(ReqModifyLimitNodeInputVO modifyLimitNodeInputVO)
  {
    this.ModifyLimitNodeInputVO = modifyLimitNodeInputVO;
  }
  public String getmsgid()
  {
    return this.msgid;
  }
  public void setmsgid(String msgid)
  {
    this.msgid = msgid;
  }
}
