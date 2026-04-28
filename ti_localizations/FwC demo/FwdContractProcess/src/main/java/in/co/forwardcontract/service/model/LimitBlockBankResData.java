package in.co.forwardcontract.service.model;

public class LimitBlockBankResData
{
  private String Status;
  private String StatusDesc;
  private LimitBlockBankResLtBlockList LimitblockList;
  private LimitBlockBankResLtBlockList LimitUnblockList;
  public String getStatus()
  {
    return this.Status;
  }
  public void setStatus(String status)
  {
    this.Status = status;
  }
  public String getStatusDesc()
  {
    return this.StatusDesc;
  }
  public void setStatusDesc(String statusDesc)
  {
    this.StatusDesc = statusDesc;
  }
  public LimitBlockBankResLtBlockList getLimitblockList()
  {
    return this.LimitblockList;
  }
  public void setLimitblockList(LimitBlockBankResLtBlockList limitblockList)
  {
    this.LimitblockList = limitblockList;
  }
  public LimitBlockBankResLtBlockList getLimitUnblockList()
  {
    return this.LimitUnblockList;
  }
  public void setLimitUnblockList(LimitBlockBankResLtBlockList limitUnblockList)
  {
    this.LimitUnblockList = limitUnblockList;
  }
}
