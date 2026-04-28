package in.co.forwardcontract.service.model;

import java.util.List;

public class LimitFetchBankResInqLtList
{
  private List<LimitFetchBankResCustLimitDtls> customerLimitDetails;
  private LimitFetchBankResHeaderDtls limitHeaderDetails;
  private LimitFetchBankResLimitInq LimitInquire;
  public List<LimitFetchBankResCustLimitDtls> getCustomerLimitDetails()
  {
    return this.customerLimitDetails;
  }
  public void setCustomerLimitDetails(List<LimitFetchBankResCustLimitDtls> customerLimitDetails)
  {
    this.customerLimitDetails = customerLimitDetails;
  }
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
}
