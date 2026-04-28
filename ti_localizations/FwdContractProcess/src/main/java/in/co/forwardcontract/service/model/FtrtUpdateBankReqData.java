package in.co.forwardcontract.service.model;

public class FtrtUpdateBankReqData
{
  private String option;
  private String status;
  private String utilizedAmount;
  private String trRefNum;
  public String getOption()
  {
    return this.option;
  }
  public void setOption(String option)
  {
    this.option = option;
  }
  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public String getUtilizedAmount()
  {
    return this.utilizedAmount;
  }
  public void setUtilizedAmount(String utilizedAmount)
  {
    this.utilizedAmount = utilizedAmount;
  }
  public String getTrRefNum()
  {
    return this.trRefNum;
  }
  public void setTrRefNum(String trRefNum)
  {
    this.trRefNum = trRefNum;
  }
}