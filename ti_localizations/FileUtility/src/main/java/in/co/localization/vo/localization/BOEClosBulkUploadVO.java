package in.co.localization.vo.localization;
 
import java.io.File;

import java.util.ArrayList;
 
public class BOEClosBulkUploadVO

{

  private String boeNo;

  private String boeDate;

  private String portCode;

  private String closType;

  private String adjClsInd;

  private String adjClsDate;

  private String approvedBy;

  private String docNo;

  private String docDate;

  private String letterNo;

  private String letterDate;

  private String docPort;

  private String remarks;

  private String invoiceSerNo;

  private String invoiceNo;

  private String adjInvAmtIC;

  private String exRate;

  private String insExRate;

  private String frExRate;

  private File inputFile;

  private String overridStatus;

  private String invamnt;

  private String invcurr;

  private String outamnt;

  private String errorDesc;

  private ArrayList<BOEClosBulkUploadVO> boeClsvoList;

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

  public ArrayList<BOEClosBulkUploadVO> getBoeClsvoList()

  {

    return this.boeClsvoList;

  }

  public void setBoeClsvoList(ArrayList<BOEClosBulkUploadVO> boeClsvoList)

  {

    this.boeClsvoList = boeClsvoList;

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

  public String getClosType()

  {

    return this.closType;

  }

  public void setClosType(String closType)

  {

    this.closType = closType;

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

  public String getDocPort()

  {

    return this.docPort;

  }

  public void setDocPort(String docPort)

  {

    this.docPort = docPort;

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

  public String getAdjInvAmtIC()

  {

    return this.adjInvAmtIC;

  }

  public void setAdjInvAmtIC(String adjInvAmtIC)

  {

    this.adjInvAmtIC = adjInvAmtIC;

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

}