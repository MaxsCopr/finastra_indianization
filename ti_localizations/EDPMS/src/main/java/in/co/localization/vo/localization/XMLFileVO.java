package in.co.localization.vo.localization;
 
import java.io.File;

import java.util.ArrayList;
 
public class XMLFileVO

{

  String fileName;

  String fileFormat;

  String filePath;

  File fileUpload;

  private int insertCount;

  private String result;

  private String xmlFormat;

  ArrayList<Object> xmlErrorList;

  private String fileName1;

  private int fileNumber;

  private int fileCount;

  private ArrayList<XMLFileVO> fileList = null;

  private int shpCount;

  private int invCount;

  private int errCount;

  private String tagName;

  private String statusRes;

  private String xmlErrTagName;

  private int headerShpCount;

  private int headerInvCount;

  private int contentShpCount;

  private int contentInvCount;

  private String countResult;

  private String xmlTagCount;

  public String getXmlTagCount()

  {

    return this.xmlTagCount;

  }

  public void setXmlTagCount(String xmlTagCount)

  {

    this.xmlTagCount = xmlTagCount;

  }

  public int getErrCount()

  {

    return this.errCount;

  }

  public void setErrCount(int errCount)

  {

    this.errCount = errCount;

  }

  public int getHeaderShpCount()

  {

    return this.headerShpCount;

  }

  public void setHeaderShpCount(int headerShpCount)

  {

    this.headerShpCount = headerShpCount;

  }

  public int getHeaderInvCount()

  {

    return this.headerInvCount;

  }

  public void setHeaderInvCount(int headerInvCount)

  {

    this.headerInvCount = headerInvCount;

  }

  public int getContentShpCount()

  {

    return this.contentShpCount;

  }

  public void setContentShpCount(int contentShpCount)

  {

    this.contentShpCount = contentShpCount;

  }

  public int getContentInvCount()

  {

    return this.contentInvCount;

  }

  public void setContentInvCount(int contentInvCount)

  {

    this.contentInvCount = contentInvCount;

  }

  public String getCountResult()

  {

    return this.countResult;

  }

  public void setCountResult(String countResult)

  {

    this.countResult = countResult;

  }

  public String getXmlErrTagName()

  {

    return this.xmlErrTagName;

  }

  public void setXmlErrTagName(String xmlErrTagName)

  {

    this.xmlErrTagName = xmlErrTagName;

  }

  public String getStatusRes()

  {

    return this.statusRes;

  }

  public void setStatusRes(String statusRes)

  {

    this.statusRes = statusRes;

  }

  public String getTagName()

  {

    return this.tagName;

  }

  public void setTagName(String tagName)

  {

    this.tagName = tagName;

  }

  public int getShpCount()

  {

    return this.shpCount;

  }

  public void setShpCount(int shpCount)

  {

    this.shpCount = shpCount;

  }

  public int getInvCount()

  {

    return this.invCount;

  }

  public void setInvCount(int invCount)

  {

    this.invCount = invCount;

  }

  public String getFileName1()

  {

    return this.fileName1;

  }

  public void setFileName1(String fileName1)

  {

    this.fileName1 = fileName1;

  }

  public int getFileNumber()

  {

    return this.fileNumber;

  }

  public void setFileNumber(int fileNumber)

  {

    this.fileNumber = fileNumber;

  }

  public int getFileCount()

  {

    return this.fileCount;

  }

  public void setFileCount(int fileCount)

  {

    this.fileCount = fileCount;

  }

  public ArrayList<XMLFileVO> getFileList()

  {

    return this.fileList;

  }

  public void setFileList(ArrayList<XMLFileVO> fileList)

  {

    this.fileList = fileList;

  }

  public String getXmlFormat()

  {

    return this.xmlFormat;

  }

  public void setXmlFormat(String xmlFormat)

  {

    this.xmlFormat = xmlFormat;

  }

  public ArrayList<Object> getXmlErrorList()

  {

    return this.xmlErrorList;

  }

  public void setXmlErrorList(ArrayList<Object> xmlErrorList)

  {

    this.xmlErrorList = xmlErrorList;

  }

  public int getInsertCount()

  {

    return this.insertCount;

  }

  public void setInsertCount(int insertCount)

  {

    this.insertCount = insertCount;

  }

  public String getResult()

  {

    return this.result;

  }

  public void setResult(String result)

  {

    this.result = result;

  }

  public File getFileUpload()

  {

    return this.fileUpload;

  }

  public void setFileUpload(File fileUpload)

  {

    this.fileUpload = fileUpload;

  }

  public String getFileName()

  {

    return this.fileName;

  }

  public void setFileName(String fileName)

  {

    this.fileName = fileName;

  }

  public String getFileFormat()

  {

    return this.fileFormat;

  }

  public void setFileFormat(String fileFormat)

  {

    this.fileFormat = fileFormat;

  }

  public String getFilePath()

  {

    return this.filePath;

  }

  public void setFilePath(String filePath)

  {

    this.filePath = filePath;

  }

}