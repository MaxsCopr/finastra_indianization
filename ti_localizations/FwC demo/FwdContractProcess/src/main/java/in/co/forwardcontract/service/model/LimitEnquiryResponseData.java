package in.co.forwardcontract.service.model;


public class LimitEnquiryResponseData
{
 private String Status;
 private String StatusDesc;
 private LimitEnquiryResponseLimitList UserMaintainedLimitInquiryList;
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
 public LimitEnquiryResponseLimitList getUserMaintainedLimitInquiryList()
 {
   return this.UserMaintainedLimitInquiryList;
 }
 public void setUserMaintainedLimitInquiryList(LimitEnquiryResponseLimitList userMaintainedLimitInquiryList)
 {
   this.UserMaintainedLimitInquiryList = userMaintainedLimitInquiryList;
 }
}