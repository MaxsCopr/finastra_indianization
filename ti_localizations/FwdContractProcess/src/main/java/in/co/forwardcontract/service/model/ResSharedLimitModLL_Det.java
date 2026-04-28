package in.co.forwardcontract.service.model;

public class ResSharedLimitModLL_Det
{
  private String primaryCustomer;
  private String activeFlg;
  private ResLimitCategory limitCategoryCode;
  private ResSharedLimSerialNum key;
  private String cifId;
  public String getprimaryCustomer()
  {
    return this.primaryCustomer;
  }
  public void setprimaryCustomer(String primaryCustomer)
  {
    this.primaryCustomer = primaryCustomer;
  }
  public String getactiveFlg()
  {
    return this.activeFlg;
  }
  public void setactiveFlg(String activeFlg)
  {
    this.activeFlg = activeFlg;
  }
  public ResLimitCategory getlimitCategoryCode()
  {
    return this.limitCategoryCode;
  }
  public void setlimitCategoryCode(ResLimitCategory limitCategoryCode)
  {
    this.limitCategoryCode = limitCategoryCode;
  }
  public ResSharedLimSerialNum getkey()
  {
    return this.key;
  }
  public void setkey(ResSharedLimSerialNum key)
  {
    this.key = key;
  }
  public String getcifId()
  {
    return this.cifId;
  }
  public void setcifId(String cifId)
  {
    this.cifId = cifId;
  }
}
