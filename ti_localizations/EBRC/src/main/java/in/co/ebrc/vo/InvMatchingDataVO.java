package in.co.ebrc.vo;

import java.util.ArrayList;

public class InvMatchingDataVO
{
  private String custName;
  private String payCurr;
  private String payAmount;
  private String custCIF;
  private String prgName;
  private String masRef;
  private String invNumber;
  private String loanStartDate;
  private String loanCurr;
  private String loanVal;
  private String outAmount;
  private String repayAmount;
  private String balAmount;
  private String progId;
  private String debitParty;
  private String valueDate;
  private String masterRef;
  private String disburseDate;
  private String dueDate;
  private String loanAmount;
  ArrayList<InvMatchingVO> debitList = null;
  ArrayList<InvMatchingVO> counterList = null;
  public ArrayList<InvMatchingVO> getDebitList()
  {
    return this.debitList;
  }
  public void setDebitList(ArrayList<InvMatchingVO> debitList)
  {
    this.debitList = debitList;
  }
  public ArrayList<InvMatchingVO> getCounterList()
  {
    return this.counterList;
  }
  public void setCounterList(ArrayList<InvMatchingVO> counterList)
  {
    this.counterList = counterList;
  }
  public String getProgId()
  {
    return this.progId;
  }
  public String getDebitParty()
  {
    return this.debitParty;
  }
  public String getValueDate()
  {
    return this.valueDate;
  }
  public String getMasterRef()
  {
    return this.masterRef;
  }
  public String getDisburseDate()
  {
    return this.disburseDate;
  }
  public String getDueDate()
  {
    return this.dueDate;
  }
  public String getLoanAmount()
  {
    return this.loanAmount;
  }
  public void setProgId(String progId)
  {
    this.progId = progId;
  }
  public void setDebitParty(String debitParty)
  {
    this.debitParty = debitParty;
  }
  public void setValueDate(String valueDate)
  {
    this.valueDate = valueDate;
  }
  public void setMasterRef(String masterRef)
  {
    this.masterRef = masterRef;
  }
  public void setDisburseDate(String disburseDate)
  {
    this.disburseDate = disburseDate;
  }
  public void setDueDate(String dueDate)
  {
    this.dueDate = dueDate;
  }
  public void setLoanAmount(String loanAmount)
  {
    this.loanAmount = loanAmount;
  }
  public String getCustName()
  {
    return this.custName;
  }
  public void setCustName(String custName)
  {
    this.custName = custName;
  }
  public String getPayCurr()
  {
    return this.payCurr;
  }
  public void setPayCurr(String payCurr)
  {
    this.payCurr = payCurr;
  }
  public String getPayAmount()
  {
    return this.payAmount;
  }
  public void setPayAmount(String payAmount)
  {
    this.payAmount = payAmount;
  }
  public String getCustCIF()
  {
    return this.custCIF;
  }
  public void setCustCIF(String custCIF)
  {
    this.custCIF = custCIF;
  }
  public String getPrgName()
  {
    return this.prgName;
  }
  public void setPrgName(String prgName)
  {
    this.prgName = prgName;
  }
  public String getMasRef()
  {
    return this.masRef;
  }
  public void setMasRef(String masRef)
  {
    this.masRef = masRef;
  }
  public String getInvNumber()
  {
    return this.invNumber;
  }
  public void setInvNumber(String invNumber)
  {
    this.invNumber = invNumber;
  }
  public String getLoanStartDate()
  {
    return this.loanStartDate;
  }
  public void setLoanStartDate(String loanStartDate)
  {
    this.loanStartDate = loanStartDate;
  }
  public String getLoanCurr()
  {
    return this.loanCurr;
  }
  public void setLoanCurr(String loanCurr)
  {
    this.loanCurr = loanCurr;
  }
  public String getLoanVal()
  {
    return this.loanVal;
  }
  public void setLoanVal(String loanVal)
  {
    this.loanVal = loanVal;
  }
  public String getOutAmount()
  {
    return this.outAmount;
  }
  public void setOutAmount(String outAmount)
  {
    this.outAmount = outAmount;
  }
  public String getRepayAmount()
  {
    return this.repayAmount;
  }
  public void setRepayAmount(String repayAmount)
  {
    this.repayAmount = repayAmount;
  }
  public String getBalAmount()
  {
    return this.balAmount;
  }
  public void setBalAmount(String balAmount)
  {
    this.balAmount = balAmount;
  }
}
