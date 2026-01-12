package in.co.localization.vo.localization;
 
import java.util.ArrayList;
 
public class TransactionClosVO
{
  private String boeNo;
  private String boeType;
  private String boeDate;
  private String portCode;
  private String approvedBy;
  private String docNo;
  private String docDate;
  private String docPortCode;
  private String letterNo;
  private String letterDate;
  private String adjClsInd;
  private String adjClsDate;
  private String invoiceSerNo;
  private String invoiceNo;
  private String adjInvoiceAmtIC;
  private String exRate;
  private String insExRate;
  private String frExRate;
  private String remarks;
  private String errDesc;
  private String batchId;
  private String errorStatus;
  private String closureType;
  private String status;
  private ArrayList<TransactionClosVO> boeList;
  private ArrayList<TransactionClosVO> errorCodeDesc;
  private ArrayList<BOEClosBulkUploadVO> boeClsvoList;
  private ArrayList<String> errorList;
  private String invamnt;
  private String invcurr;
  private String outamnt;
  public ArrayList<BOEClosBulkUploadVO> getBoeClsvoList()
  {
    return this.boeClsvoList;
  }
  public void setBoeClsvoList(ArrayList<BOEClosBulkUploadVO> boeClsvoList)
  {
    this.boeClsvoList = boeClsvoList;
  }
  public ArrayList<String> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<String> errorList)
  {
    this.errorList = errorList;
  }
  public String getInvamnt()
  {
    return this.invamnt;
  }
  public void setInvamnt(String invamnt)
  {
    this.invamnt = invamnt;
  }
  public String getInvcurr()
  {
    return this.invcurr;
  }
  public void setInvcurr(String invcurr)
  {
    this.invcurr = invcurr;
  }
  public String getOutamnt()
  {
    return this.outamnt;
  }
  public void setOutamnt(String outamnt)
  {
    this.outamnt = outamnt;
  }
  public ArrayList<TransactionClosVO> getErrorCodeDesc()
  {
    return this.errorCodeDesc;
  }
  public void setErrorCodeDesc(ArrayList<TransactionClosVO> errorCodeDesc)
  {
    this.errorCodeDesc = errorCodeDesc;
  }
  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public String getClosureType()
  {
    return this.closureType;
  }
  public void setClosureType(String closureType)
  {
    this.closureType = closureType;
  }
  public ArrayList<TransactionClosVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<TransactionClosVO> boeList)
  {
    this.boeList = boeList;
  }
  public String getErrorStatus()
  {
    return this.errorStatus;
  }
  public void setErrorStatus(String errorStatus)
  {
    this.errorStatus = errorStatus;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
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
  public String getApprovedBy()
  {
    return this.approvedBy;
  }
  public void setApprovedBy(String approvedBy)
  {
    this.approvedBy = approvedBy;
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
  public String getDocPortCode()
  {
    return this.docPortCode;
  }
  public void setDocPortCode(String docPortCode)
  {
    this.docPortCode = docPortCode;
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
  public String getAdjClsInd()
  {
    return this.adjClsInd;
  }
  public void setAdjClsInd(String adjClsInd)
  {
    this.adjClsInd = adjClsInd;
  }
  public String getAdjClsDate()
  {
    return this.adjClsDate;
  }
  public void setAdjClsDate(String adjClsDate)
  {
    this.adjClsDate = adjClsDate;
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
  public String getAdjInvoiceAmtIC()
  {
    return this.adjInvoiceAmtIC;
  }
  public void setAdjInvoiceAmtIC(String adjInvoiceAmtIC)
  {
    this.adjInvoiceAmtIC = adjInvoiceAmtIC;
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
}