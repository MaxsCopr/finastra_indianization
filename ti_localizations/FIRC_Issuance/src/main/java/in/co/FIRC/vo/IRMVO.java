package in.co.FIRC.vo;

import java.util.ArrayList;

public class IRMVO
{
  private String transactionrefno;
  private String adcode;
  private String iecode;
  private String extensionInd;
  private String letterNo;
  private String letterDate;
  private String extensionDate;
  private String remarks;
  private String status;
  private String ackStatus;
  private String recordInd;
  private String reason;
  private String tempTransRefNo;
  private String serialNo;
  private String checkBoxDisabled;
  private String previousExtDate;
  ArrayList<AlertMessageVO> errorList = null;
  public String getPreviousExtDate()
  {
    return this.previousExtDate;
  }
  public void setPreviousExtDate(String previousExtDate)
  {
    this.previousExtDate = previousExtDate;
  }
  public String getCheckBoxDisabled()
  {
    return this.checkBoxDisabled;
  }
  public void setCheckBoxDisabled(String checkBoxDisabled)
  {
    this.checkBoxDisabled = checkBoxDisabled;
  }
  public String getSerialNo()
  {
    return this.serialNo;
  }
  public void setSerialNo(String serialNo)
  {
    this.serialNo = serialNo;
  }
  public String getTempTransRefNo()
  {
    return this.tempTransRefNo;
  }
  public void setTempTransRefNo(String tempTransRefNo)
  {
    this.tempTransRefNo = tempTransRefNo;
  }
  public ArrayList<AlertMessageVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessageVO> errorList)
  {
    this.errorList = errorList;
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
  public String getExtensionInd()
  {
    return this.extensionInd;
  }
  public void setExtensionInd(String extensionInd)
  {
    this.extensionInd = extensionInd;
  }
  public String getLetterNo()
  {
    return this.letterNo;
  }
  public void setLetterNo(String letterNo)
  {
    this.letterNo = letterNo;
  }
  public String getLetterDate()
  {
    return this.letterDate;
  }
  public void setLetterDate(String letterDate)
  {
    this.letterDate = letterDate;
  }
  public String getExtensionDate()
  {
    return this.extensionDate;
  }
  public void setExtensionDate(String extensionDate)
  {
    this.extensionDate = extensionDate;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
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
  public String getRecordInd()
  {
    return this.recordInd;
  }
  public void setRecordInd(String recordInd)
  {
    this.recordInd = recordInd;
  }
  public String getReason()
  {
    return this.reason;
  }
  public void setReason(String reason)
  {
    this.reason = reason;
  }
}
