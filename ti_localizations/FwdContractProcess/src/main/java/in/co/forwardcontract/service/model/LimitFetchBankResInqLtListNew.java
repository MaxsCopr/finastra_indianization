package in.co.forwardcontract.service.model;

public class LimitFetchBankResInqLtListNew
{
  private LimitFetchBankResCustLimitDtls customerLimitDetails;
  private LimitFetchBankResHeaderDtls limitHeaderDetails;
  private LimitFetchBankResLimitInq LimitInquire;
  public LimitFetchBankResHeaderDtls getLimitHeaderDetails()
  {
    return this.limitHeaderDetails;
  }
  public void setLimitHeaderDetails(LimitFetchBankResHeaderDtls limitHeaderDetails)
  {
    this.limitHeaderDetails = limitHeaderDetails;
  }
  public LimitFetchBankResLimitInq getLimitInquire()
  {
    return this.LimitInquire;
  }
  public void setLimitInquire(LimitFetchBankResLimitInq limitInquire)
  {
    this.LimitInquire = limitInquire;
  }
  public LimitFetchBankResCustLimitDtls getCustomerLimitDetails()
  {
    return this.customerLimitDetails;
  }
  public void setCustomerLimitDetails(LimitFetchBankResCustLimitDtls customerLimitDetails)
  {
    this.customerLimitDetails = customerLimitDetails;
  }
}
