package in.co.boe.vo;

import java.util.ArrayList;

public class TransactionBoeBlkVO
{
  private String paymentRefNo;
  private String eventRefNo;
  private String boeNo;
  private String boeType;
  private String boeDate;
  private String portCode;
  private String changeIeCode;
  private String invoiceSerNo;
  private String invoiceNo;
  private String invRealAmt;
  private String exRate;
  private String insExRate;
  private String frExRate;
  private String remarks;
  private String errDesc;
  private String batchId;
  private String errorStatus;
  private String status;
  private ArrayList<TransactionBoeBlkVO> boeList;
  private ArrayList<TransactionBoeBlkVO> errorCodeDesc;
  private String count;
  private int successCount;
  private String paymentAmnt;
  private String payAmntCurr;
  private String boeAmnt;
  private String boeAmntCurr;
  private String boeAmntEndorse;
  private String boeAllocAmnt;
  private String besRecInd;
  private ArrayList<String> errorList;
  private ArrayList<BOEBulkUploadVO> boevoList;
  public ArrayList<String> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<String> errorList)
  {
    this.errorList = errorList;
  }
  public ArrayList<BOEBulkUploadVO> getBoevoList()
  {
    return this.boevoList;
  }
  public void setBoevoList(ArrayList<BOEBulkUploadVO> boevoList)
  {
    this.boevoList = boevoList;
  }
  public int getSuccessCount()
  {
    return this.successCount;
  }
  public void setSuccessCount(int successCount)
  {
    this.successCount = successCount;
  }
  public String getCount()
  {
    return this.count;
  }
  public void setCount(String count)
  {
    this.count = count;
  }
  public String getPaymentRefNo()
  {
    return this.paymentRefNo;
  }
  public void setPaymentRefNo(String paymentRefNo)
  {
    this.paymentRefNo = paymentRefNo;
  }
  public String getEventRefNo()
  {
    return this.eventRefNo;
  }
  public void setEventRefNo(String eventRefNo)
  {
    this.eventRefNo = eventRefNo;
  }
  public String getBoeNo()
  {
    return this.boeNo;
  }
  public void setBoeNo(String boeNo)
  {
    this.boeNo = boeNo;
  }
  public String getBoeType()
  {
    return this.boeType;
  }
  public void setBoeType(String boeType)
  {
    this.boeType = boeType;
  }
  public String getBoeDate()
  {
    return this.boeDate;
  }
  public void setBoeDate(String boeDate)
  {
    this.boeDate = boeDate;
  }
  public String getPortCode()
  {
    return this.portCode;
  }
  public void setPortCode(String portCode)
  {
    this.portCode = portCode;
  }
  public String getChangeIeCode()
  {
    return this.changeIeCode;
  }
  public void setChangeIeCode(String changeIeCode)
  {
    this.changeIeCode = changeIeCode;
  }
  public String getInvoiceSerNo()
  {
    return this.invoiceSerNo;
  }
  public void setInvoiceSerNo(String invoiceSerNo)
  {
    this.invoiceSerNo = invoiceSerNo;
  }
  public String getInvoiceNo()
  {
    return this.invoiceNo;
  }
  public void setInvoiceNo(String invoiceNo)
  {
    this.invoiceNo = invoiceNo;
  }
  public String getInvRealAmt()
  {
    return this.invRealAmt;
  }
  public void setInvRealAmt(String invRealAmt)
  {
    this.invRealAmt = invRealAmt;
  }
  public String getExRate()
  {
    return this.exRate;
  }
  public void setExRate(String exRate)
  {
    this.exRate = exRate;
  }
  public String getInsExRate()
  {
    return this.insExRate;
  }
  public void setInsExRate(String insExRate)
  {
    this.insExRate = insExRate;
  }
  public String getFrExRate()
  {
    return this.frExRate;
  }
  public void setFrExRate(String frExRate)
  {
    this.frExRate = frExRate;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String getErrDesc()
  {
    return this.errDesc;
  }
  public void setErrDesc(String errDesc)
  {
    this.errDesc = errDesc;
  }
  public String getBatchId()
  {
    return this.batchId;
  }
  public void setBatchId(String batchId)
  {
    this.batchId = batchId;
  }
  public String getErrorStatus()
  {
    return this.errorStatus;
  }
  public void setErrorStatus(String errorStatus)
  {
    this.errorStatus = errorStatus;
  }
  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public ArrayList<TransactionBoeBlkVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<TransactionBoeBlkVO> boeList)
  {
    this.boeList = boeList;
  }
  public ArrayList<TransactionBoeBlkVO> getErrorCodeDesc()
  {
    return this.errorCodeDesc;
  }
  public void setErrorCodeDesc(ArrayList<TransactionBoeBlkVO> errorCodeDesc)
  {
    this.errorCodeDesc = errorCodeDesc;
  }
  public String getPaymentAmnt()
  {
    return this.paymentAmnt;
  }
  public void setPaymentAmnt(String paymentAmnt)
  {
    this.paymentAmnt = paymentAmnt;
  }
  public String getPayAmntCurr()
  {
    return this.payAmntCurr;
  }
  public void setPayAmntCurr(String payAmntCurr)
  {
    this.payAmntCurr = payAmntCurr;
  }
  public String getBoeAmnt()
  {
    return this.boeAmnt;
  }
  public void setBoeAmnt(String boeAmnt)
  {
    this.boeAmnt = boeAmnt;
  }
  public String getBoeAmntCurr()
  {
    return this.boeAmntCurr;
  }
  public void setBoeAmntCurr(String boeAmntCurr)
  {
    this.boeAmntCurr = boeAmntCurr;
  }
  public String getBoeAmntEndorse()
  {
    return this.boeAmntEndorse;
  }
  public void setBoeAmntEndorse(String boeAmntEndorse)
  {
    this.boeAmntEndorse = boeAmntEndorse;
  }
  public String getBoeAllocAmnt()
  {
    return this.boeAllocAmnt;
  }
  public void setBoeAllocAmnt(String boeAllocAmnt)
  {
    this.boeAllocAmnt = boeAllocAmnt;
  }
  public String getBesRecInd()
  {
    return this.besRecInd;
  }
  public void setBesRecInd(String besRecInd)
  {
    this.besRecInd = besRecInd;
  }
}