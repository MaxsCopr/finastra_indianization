package in.co.boe.vo;

import java.util.ArrayList;

public class BOEViewerVO
{
  private String boeRefNo;
  private String boeEventNo;
  private String cifNo;
  private String boeNo;
  private String boeDate;
  private String boeType;
  private String boeCurr;
  private String portCode;
  private String harCode;
  ArrayList<TransactionVO> boeList;
  public ArrayList<TransactionVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<TransactionVO> boeList)
  {
    this.boeList = boeList;
  }
  public String getPortCode()
  {
    return this.portCode;
  }
  public void setPortCode(String portCode)
  {
    this.portCode = portCode;
  }
  public String getHarCode()
  {
    return this.harCode;
  }
  public void setHarCode(String harCode)
  {
    this.harCode = harCode;
  }
  public String getBoeRefNo()
  {
    return this.boeRefNo;
  }
  public void setBoeRefNo(String boeRefNo)
  {
    this.boeRefNo = boeRefNo;
  }
  public String getBoeEventNo()
  {
    return this.boeEventNo;
  }
  public void setBoeEventNo(String boeEventNo)
  {
    this.boeEventNo = boeEventNo;
  }
  public String getCifNo()
  {
    return this.cifNo;
  }
  public void setCifNo(String cifNo)
  {
    this.cifNo = cifNo;
  }
  public String getBoeNo()
  {
    return this.boeNo;
  }
  public void setBoeNo(String boeNo)
  {
    this.boeNo = boeNo;
  }
  public String getBoeDate()
  {
    return this.boeDate;
  }
  public void setBoeDate(String boeDate)
  {
    this.boeDate = boeDate;
  }
  public String getBoeType()
  {
    return this.boeType;
  }
  public void setBoeType(String boeType)
  {
    this.boeType = boeType;
  }
  public String getBoeCurr()
  {
    return this.boeCurr;
  }
  public void setBoeCurr(String boeCurr)
  {
    this.boeCurr = boeCurr;
  }
}