package in.co.prishipment.vo;
 
import java.util.ArrayList;
 
public class PreShipDeleteVO

{

  private String masref;

  private String startDate;

  private String duedate;

  private String loanAmt;

  private String outStandAmt;

  private String repayAmount;

  private String status;

  ArrayList<PreShipDeleteVO> preShipdelList;

  public ArrayList<PreShipDeleteVO> getPreShipdelList()

  {

    return this.preShipdelList;

  }

  public void setPreShipdelList(ArrayList<PreShipDeleteVO> preShipdelList)

  {

    this.preShipdelList = preShipdelList;

  }

  public String getMasref()

  {

    return this.masref;

  }

  public void setMasref(String masref)

  {

    this.masref = masref;

  }

  public String getStartDate()

  {

    return this.startDate;

  }

  public void setStartDate(String startDate)

  {

    this.startDate = startDate;

  }

  public String getDuedate()

  {

    return this.duedate;

  }

  public void setDuedate(String duedate)

  {

    this.duedate = duedate;

  }

  public String getLoanAmt()

  {

    return this.loanAmt;

  }

  public void setLoanAmt(String loanAmt)

  {

    this.loanAmt = loanAmt;

  }

  public String getOutStandAmt()

  {

    return this.outStandAmt;

  }

  public void setOutStandAmt(String outStandAmt)

  {

    this.outStandAmt = outStandAmt;

  }

  public String getRepayAmount()

  {

    return this.repayAmount;

  }

  public void setRepayAmount(String repayAmount)

  {

    this.repayAmount = repayAmount;

  }

  public String getStatus()

  {

    return this.status;

  }

  public void setStatus(String status)

  {

    this.status = status;

  }

}