package in.co.forwardcontract.service.model;

import java.util.List;

public class ReqModifyLimitNodeInputVO
{
  private String limitPrefix;
  private String limitSuffix;
  private List<ReqUserMaintLiabModLL> userMaintLiabModLL;
  public List<ReqUserMaintLiabModLL> getUserMaintLiabModLL()
  {
    return this.userMaintLiabModLL;
  }
  public void setUserMaintLiabModLL(List<ReqUserMaintLiabModLL> userMaintLiabModLL)
  {
    this.userMaintLiabModLL = userMaintLiabModLL;
  }
  public String getlimitPrefix()
  {
    return this.limitPrefix;
  }
  public void setlimitPrefix(String limitPrefix)
  {
    this.limitPrefix = limitPrefix;
  }
  public String getlimitSuffix()
  {
    return this.limitSuffix;
  }
  public void setlimitSuffix(String limitSuffix)
  {
    this.limitSuffix = limitSuffix;
  }
}
