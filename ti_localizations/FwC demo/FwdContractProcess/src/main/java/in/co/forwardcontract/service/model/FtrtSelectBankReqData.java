package in.co.forwardcontract.service.model;

public class FtrtSelectBankReqData
{
  private String option;
  private String trRefNum;
  private String cifId;
  public String getOption()
  {
    return this.option;
  }
  public void setOption(String option)
  {
    this.option = option;
  }
  public String getTrRefNum()
  {
    return this.trRefNum;
  }
  public void setTrRefNum(String trRefNum)
  {
    this.trRefNum = trRefNum;
  }
  public String getCifId()
  {
    return this.cifId;
  }
  public void setCifId(String cifId)
  {
    this.cifId = cifId;
  }
}