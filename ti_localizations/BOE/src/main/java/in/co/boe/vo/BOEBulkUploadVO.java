package in.co.boe.vo;

import java.io.File;
import java.util.ArrayList;
 
public class BOEBulkUploadVO
{
  private String paymentRefNo;
  private String eventRefNo;
  private String boeNo;
  private String boeDate;
  private String portCode;
  private String changeIeCode;
  private String invoiceSerNo;
  private String invoiceNo;
  private String invRelAmt;
  private String exRate;
  private String insExRate;
  private String frExRate;
  private String remarks;
  private File inputFile;
  private String overridStatus;
  private String paymentAmnt;
  private String payAmntCurr;
  private String boeAmnt;
  private String boeAmntCurr;
  private String boeAmntEndorse;
  private String boeAllocAmnt;
  private String besRecInd;
  private String errorDesc;
  private ArrayList<BOEBulkUploadVO> boevoList;
  public String getErrorDesc()
  {
    return this.errorDesc;
  }
  public void setErrorDesc(String errorDesc)
  {
    this.errorDesc = errorDesc;
  }
  public String getOverridStatus()
  {
    return this.overridStatus;
  }
  public void setOverridStatus(String overridStatus)
  {
    this.overridStatus = overridStatus;
  }
  public ArrayList<BOEBulkUploadVO> getBoevoList()
  {
    return this.boevoList;
  }
  public void setBoevoList(ArrayList<BOEBulkUploadVO> boevoList)
  {
    this.boevoList = boevoList;
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
  public File getInputFile()
  {
    return this.inputFile;
  }
  public void setInputFile(File inputFile)
  {
    this.inputFile = inputFile;
  }
  public String getInvoiceNo()
  {
    return this.invoiceNo;
  }
  public void setInvoiceNo(String invoiceNo)
  {
    this.invoiceNo = invoiceNo;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String getInvoiceSerNo()
  {
    return this.invoiceSerNo;
  }
  public void setInvoiceSerNo(String invoiceSerNo)
  {
    this.invoiceSerNo = invoiceSerNo;
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
  public String getChangeIeCode()
  {
    return this.changeIeCode;
  }
  public void setChangeIeCode(String changeIeCode)
  {
    this.changeIeCode = changeIeCode;
  }
  public String getInvRelAmt()
  {
    return this.invRelAmt;
  }
  public void setInvRelAmt(String invRelAmt)
  {
    this.invRelAmt = invRelAmt;
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