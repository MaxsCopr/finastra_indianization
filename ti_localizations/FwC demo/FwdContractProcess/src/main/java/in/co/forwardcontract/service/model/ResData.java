package in.co.forwardcontract.service.model;


public class ResData
{
 private String Status;
 private String Remarks;
 private String accountNumber;
 public String getStatus()
 {
   return this.Status;
 }
 public void setStatus(String status)
 {
   this.Status = status;
 }
 public String getRemarks()
 {
   return this.Remarks;
 }
 public void setRemarks(String remarks)
 {
   this.Remarks = remarks;
 }
 public String getAccountNumber()
 {
   return this.accountNumber;
 }
 public void setAccountNumber(String accountNumber)
 {
   this.accountNumber = accountNumber;
 }
}