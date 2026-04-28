package in.co.ebrc.vo;

import java.util.ArrayList;
import java.util.Date;
 
public class InvMatchingVO
{
  private String progId;
  private String debitParty;
  private String counterParty;
  private String valueDate;
  private String payAmount;
  private String masterRef;
  private String invNumber;
  private String disburseDate;
  private String dueDate;
  private String loanAmount;
  private String outAmount;
  private String repayAmount;
  private Date valDate;
  private String key;
  private String value;
  private String key1;
  private String value1;
  ArrayList<InvMatchingVO> debitList = null;
  ArrayList<InvMatchingVO> pgmList = null;
  ArrayList<InvMatchingDataVO> invList = null;
  ArrayList<InvMatchingVO> counterList = null;
  ArrayList<AlertMessagesVO> errorList = null;
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
  }
  public Date getValDate()
  {
    return this.valDate;
  }
  public void setValDate(Date valDate)
  {
    this.valDate = valDate;
  }
  public String getKey()
  {
    return this.key;
  }
  public String getValue()
  {
    return this.value;
  }
  public String getKey1()
  {
    return this.key1;
  }
  public void setKey1(String key1)
  {
    this.key1 = key1;
  }
  public String getValue1()
  {
    return this.value1;
  }
  public void setValue1(String value1)
  {
    this.value1 = value1;
  }
  public ArrayList<InvMatchingVO> getPgmList()
  {
    return this.pgmList;
  }
  public void setKey(String key)
  {
    this.key = key;
  }
  public void setValue(String value)
  {
    this.value = value;
  }
  public void setPgmList(ArrayList<InvMatchingVO> pgmList)
  {
    this.pgmList = pgmList;
  }
  public ArrayList<InvMatchingVO> getDebitList()
  {
    return this.debitList;
  }
  public ArrayList<InvMatchingDataVO> getInvList()
  {
    return this.invList;
  }
  public void setDebitList(ArrayList<InvMatchingVO> debitList)
  {
    this.debitList = debitList;
  }
  public void setInvList(ArrayList<InvMatchingDataVO> invList)
  {
    this.invList = invList;
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
  public void setProgId(String progId)
  {
    this.progId = progId;
  }
  public String getDebitParty()
  {
    return this.debitParty;
  }
  public void setDebitParty(String debitParty)
  {
    this.debitParty = debitParty;
  }
  public String getCounterParty()
  {
    return this.counterParty;
  }
  public void setCounterParty(String counterParty)
  {
    this.counterParty = counterParty;
  }
  public String getValueDate()
  {
    return this.valueDate;
  }
  public void setValueDate(String valueDate)
  {
    this.valueDate = valueDate;
  }
  public String getPayAmount()
  {
    return this.payAmount;
  }
  public void setPayAmount(String payAmount)
  {
    this.payAmount = payAmount;
  }
  public String getMasterRef()
  {
    return this.masterRef;
  }
  public void setMasterRef(String masterRef)
  {
    this.masterRef = masterRef;
  }
  public String getInvNumber()
  {
    return this.invNumber;
  }
  public void setInvNumber(String invNumber)
  {
    this.invNumber = invNumber;
  }
  public String getDisburseDate()
  {
    return this.disburseDate;
  }
  public void setDisburseDate(String disburseDate)
  {
    this.disburseDate = disburseDate;
  }
  public String getDueDate()
  {
    return this.dueDate;
  }
  public void setDueDate(String dueDate)
  {
    this.dueDate = dueDate;
  }
  public String getLoanAmount()
  {
    return this.loanAmount;
  }
  public void setLoanAmount(String loanAmount)
  {
    this.loanAmount = loanAmount;
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
}
