package in.co.FIRC.vo;

import java.util.ArrayList;

public class IRMClosureVO
{
  private String transactionrefno;
  private String adcode;
  private String iecode;
  private String currency;
  private String amount;
  private String approvedBy;
  private String letterNo;
  private String adjDate;
  private String reason;
  private String docNo;
  private String docDate;
  private String docPort;
  private String recordInd;
  private String closeInd;
  private String remarks;
  private String checkerRemarks;
  private String status;
  private String ackStatus;
  private String tempTransRefNo;
  private String serialNo;
  private String checkBoxDisabled;
  ArrayList<AlertMessageVO> errorList = null;
  public String getCheckBoxDisabled()
  {
    return this.checkBoxDisabled;
  }
  public void setCheckBoxDisabled(String checkBoxDisabled)
  {
    this.checkBoxDisabled = checkBoxDisabled;
  }
  public String getTransactionrefno()
  {
    return this.transactionrefno;
  }
  public void setTransactionrefno(String transactionrefno)
  {
    this.transactionrefno = transactionrefno;
  }
  public String getAdcode()
  {
    return this.adcode;
  }
  public void setAdcode(String adcode)
  {
    this.adcode = adcode;
  }
  public String getIecode()
  {
    return this.iecode;
  }
  public void setIecode(String iecode)
  {
    this.iecode = iecode;
  }
  public String getCurrency()
  {
    return this.currency;
  }
  public void setCurrency(String currency)
  {
    this.currency = currency;
  }
  public String getAmount()
  {
    return this.amount;
  }
  public void setAmount(String amount)
  {
    this.amount = amount;
  }
  public String getApprovedBy()
  {
    return this.approvedBy;
  }
  public void setApprovedBy(String approvedBy)
  {
    this.approvedBy = approvedBy;
  }
  public String getLetterNo()
  {
    return this.letterNo;
  }
  public void setLetterNo(String letterNo)
  {
    this.letterNo = letterNo;
  }
  public String getAdjDate()
  {
    return this.adjDate;
  }
  public void setAdjDate(String adjDate)
  {
    this.adjDate = adjDate;
  }
  public String getReason()
  {
    return this.reason;
  }
  public void setReason(String reason)
  {
    this.reason = reason;
  }
  public String getDocNo()
  {
    return this.docNo;
  }
  public void setDocNo(String docNo)
  {
    this.docNo = docNo;
  }
  public String getDocDate()
  {
    return this.docDate;
  }
  public void setDocDate(String docDate)
  {
    this.docDate = docDate;
  }
  public String getDocPort()
  {
    return this.docPort;
  }
  public void setDocPort(String docPort)
  {
    this.docPort = docPort;
  }
  public String getRecordInd()
  {
    return this.recordInd;
  }
  public void setRecordInd(String recordInd)
  {
    this.recordInd = recordInd;
  }
  public String getCloseInd()
  {
    return this.closeInd;
  }
  public void setCloseInd(String closeInd)
  {
    this.closeInd = closeInd;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String getCheckerRemarks()
  {
    return this.checkerRemarks;
  }
  public void setCheckerRemarks(String checkerRemarks)
  {
    this.checkerRemarks = checkerRemarks;
  }
  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public String getAckStatus()
  {
    return this.ackStatus;
  }
  public void setAckStatus(String ackStatus)
  {
    this.ackStatus = ackStatus;
  }
  public String getTempTransRefNo()
  {
    return this.tempTransRefNo;
  }
  public void setTempTransRefNo(String tempTransRefNo)
  {
    this.tempTransRefNo = tempTransRefNo;
  }
  public String getSerialNo()
  {
    return this.serialNo;
  }
  public void setSerialNo(String serialNo)
  {
    this.serialNo = serialNo;
  }
  public ArrayList<AlertMessageVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessageVO> errorList)
  {
    this.errorList = errorList;
  }
}
