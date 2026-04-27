package in.co.forwardcontract.service.model;

public class LimitEnquiryResponseDataA
{
  private String Status;
  private String StatusDesc;
  private LimitEnquiryResponseLimitListA UserMaintainedLimitInquiryList;
  public String getStatus()
  {
    return this.Status;
  }
  public String getStatusDesc()
  {
    return this.StatusDesc;
  }
  public void setStatus(String status)
  {
    this.Status = status;
  }
  public void setStatusDesc(String statusDesc)
  {
    this.StatusDesc = statusDesc;
  }
  public LimitEnquiryResponseLimitListA getUserMaintainedLimitInquiryList()
  {
    return this.UserMaintainedLimitInquiryList;
  }
  public void setUserMaintainedLimitInquiryList(LimitEnquiryResponseLimitListA userMaintainedLimitInquiryList)
  {
    this.UserMaintainedLimitInquiryList = userMaintainedLimitInquiryList;
  }
}
