package in.co.forwardcontract.service.model;

public class PostingReqCount
{
 private String CreditCount;
 private String DebitCount;
 private String TotalXferCount;
 public String getCreditCount()
 {
   return this.CreditCount;
 }
 public void setCreditCount(String creditCount)
 {
   this.CreditCount = creditCount;
 }
 public String getDebitCount()
 {
   return this.DebitCount;
 }
 public void setDebitCount(String debitCount)
 {
   this.DebitCount = debitCount;
 }
 public String getTotalXferCount()
 {
   return this.TotalXferCount;
 }
 public void setTotalXferCount(String totalXferCount)
 {
   this.TotalXferCount = totalXferCount;
 }
}