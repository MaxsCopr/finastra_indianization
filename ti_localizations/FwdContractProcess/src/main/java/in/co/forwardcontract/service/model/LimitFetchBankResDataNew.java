package in.co.forwardcontract.service.model;

public class LimitFetchBankResDataNew
{
  private String Status;
  private String StatusDesc;
  private LimitFetchBankResInqLtListNew InquireLimitList;
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
  public LimitFetchBankResInqLtListNew getInquireLimitList()
  {
    return this.InquireLimitList;
  }
  public void setInquireLimitList(LimitFetchBankResInqLtListNew inquireLimitList)
  {
    this.InquireLimitList = inquireLimitList;
  }
}