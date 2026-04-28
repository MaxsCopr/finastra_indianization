package in.co.boe.vo;

import java.io.File;
import java.util.ArrayList;
 
public class OBBBulkUploadVO
{
  private String boeNo;
  private String boeDate;
  private String portCode;
  private String govprv;
  private String ieCode;
  private String iename = "";
  private String ieadd;
  private String iepan;
  private String imAgency = "";
  private String adCode;
  private String ieName;
  private String pos;
  private String igmNo;
  private File inputFile;
  private String overridStatus;
  private String fileNameRef;
  private ArrayList<OBBBulkUploadVO> obbvoList;
  private String errorDesc;
  public String getFileNameRef()
  {
    return this.fileNameRef;
  }
  public void setFileNameRef(String fileNameRef)
  {
    this.fileNameRef = fileNameRef;
  }
  public ArrayList<OBBBulkUploadVO> getObbvoList()
  {
    return this.obbvoList;
  }
  public void setObbvoList(ArrayList<OBBBulkUploadVO> obbvoList)
  {
    this.obbvoList = obbvoList;
  }
  public String getErrorDesc()
  {
    return this.errorDesc;
  }
  public void setErrorDesc(String errorDesc)
  {
    this.errorDesc = errorDesc;
  }
  public String getIeName()
  {
    return this.ieName;
  }
  public void setIeName(String ieName)
  {
    this.ieName = ieName;
  }
  public String getOverridStatus()
  {
    return this.overridStatus;
  }
  public void setOverridStatus(String overridStatus)
  {
    this.overridStatus = overridStatus;
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
  public String getGovprv()
  {
    return this.govprv;
  }
  public void setGovprv(String govprv)
  {
    this.govprv = govprv;
  }
  public String getIeCode()
  {
    return this.ieCode;
  }
  public void setIeCode(String ieCode)
  {
    this.ieCode = ieCode;
  }
  public String getIename()
  {
    return this.iename;
  }
  public void setIename(String iename)
  {
    this.iename = iename;
  }
  public String getIeadd()
  {
    return this.ieadd;
  }
  public void setIeadd(String ieadd)
  {
    this.ieadd = ieadd;
  }
  public String getIepan()
  {
    return this.iepan;
  }
  public void setIepan(String iepan)
  {
    this.iepan = iepan;
  }
  public String getImAgency()
  {
    return this.imAgency;
  }
  public void setImAgency(String imAgency)
  {
    this.imAgency = imAgency;
  }
  public String getAdCode()
  {
    return this.adCode;
  }
  public void setAdCode(String adCode)
  {
    this.adCode = adCode;
  }
  public String getPos()
  {
    return this.pos;
  }
  public void setPos(String pos)
  {
    this.pos = pos;
  }
  public String getIgmNo()
  {
    return this.igmNo;
  }
  public void setIgmNo(String igmNo)
  {
    this.igmNo = igmNo;
  }
  public File getInputFile()
  {
    return this.inputFile;
  }
  public void setInputFile(File inputFile)
  {
    this.inputFile = inputFile;
  }
}
