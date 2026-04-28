package in.co.forwardcontract.service.model;

public class LimitEnquiryRequestData
{
  private String custId;
  private String SUFFIX;
  public String getSUFFIX()
  {
    return this.SUFFIX;
  }
  public void setSUFFIX(String sUFFIX)
  {
    this.SUFFIX = sUFFIX;
  }
  public String getCustId()
  {
    return this.custId;
  }
  public void setCustId(String custId)
  {
    this.custId = custId;
  }
}
