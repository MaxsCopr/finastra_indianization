package in.co.localization.vo.localization;
 
import java.io.File;

import java.util.ArrayList;
 
public class XMLFileVO

{

  String fileName;

  String fileFormat;

  String filePath;

  File fileUpload;

  String filePat;

  private int insertCount;

  private String result;

  private String fileExt;

  private String overridStatus;

  ArrayList<Object> xmlErrorList;

  private String fileName1;

  private int fileNumber;

  private int fileCount;

  public String getFileName1()

  {

    return this.fileName1;

  }

  public void setFileName1(String fileName1)

  {

    this.fileName1 = fileName1;

  }

  private ArrayList<XMLFileVO> fileList = null;

  private static int boeCount;

  private static int Outref;

  private static int outrefFailedCount;

  private static int invCount;

  private int errCount;

  private String errDesc;

  private String tagName;

  private String statusRes;

  private String xmlErrTagName;

  public String fil;

  private static int n;

  private static String[] record;

  private static int x;

  ArrayList<Object> xmlOrmAckErrorList;

  public String getErrDesc()

  {

    return this.errDesc;

  }

  public void setErrDesc(String errDesc)

  {

    this.errDesc = errDesc;

  }

  public int getErrCount()

  {

    return this.errCount;

  }

  public void setErrCount(int errCount)

  {

    this.errCount = errCount;

  }

  public String getTagName()

  {

    return this.tagName;

  }

  public void setTagName(String tagName)

  {

    this.tagName = tagName;

  }

  public String getStatusRes()

  {

    return this.statusRes;

  }

  public void setStatusRes(String statusRes)

  {

    this.statusRes = statusRes;

  }

  public String getXmlErrTagName()

  {

    return this.xmlErrTagName;

  }

  public void setXmlErrTagName(String xmlErrTagName)

  {

    this.xmlErrTagName = xmlErrTagName;

  }

  public int getBoeCount()

  {

    return boeCount;

  }

  public void setBoeCount(int boeCount)

  {

    boeCount = boeCount;

  }

  public int getOutref()

  {

    return Outref;

  }

  public void setOutref(int Outref)

  {

    Outref = Outref;

  }

  public int getOutrefFailedCount()

  {

    return outrefFailedCount;

  }

  public void setOutrefFailedCount(int outrefFailedCount)

  {

    outrefFailedCount = outrefFailedCount;

  }

  public String getFilePat()

  {

    return this.filePat;

  }

  public void setFilePat(String filePat)

  {

    this.filePat = filePat;

  }

  public String getfileName()

  {

    return this.fileName;

  }

  public static void setfileName(String fileName)

  {

    fileName = fileName;

  }

  public int getInvCount()

  {

    return invCount;

  }

  public static void setInvCount(int invCount)

  {

    invCount = invCount;

  }

  public ArrayList<XMLFileVO> getFileList()

  {

    return this.fileList;

  }

  public void setFileList(ArrayList<XMLFileVO> fileList)

  {

    this.fileList = fileList;

  }

  public int getFileCount()

  {

    return this.fileCount;

  }

  public void setFileCount(int fileCount)

  {

    this.fileCount = fileCount;

  }

  public int getFileNumber()

  {

    return this.fileNumber;

  }

  public void setFileNumber(int fileNumber)

  {

    this.fileNumber = fileNumber;

  }

  public String getFileExt()

  {

    return this.fileExt;

  }

  public void setFileExt(String fileExt)

  {

    this.fileExt = fileExt;

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

  public int setInsertCount(int insertCount)

  {

    return this.insertCount = insertCount;

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

  public String getOverridStatus()

  {

    return this.overridStatus;

  }

  public void setOverridStatus(String overridStatus)

  {

    this.overridStatus = overridStatus;

  }

  public static void setCountry(int n)

  {

    n = n;

  }

  public static int getCountry()

  {

    return n;

  }

  public static void setRecord(String insertQuery)

  {

    while (x < n)

    {

      record[x] = insertQuery;

      x += 1;

    }

  }

  public static String[] getRecord()

  {

    return record;

  }

  public ArrayList<Object> getXmlOrmAckErrorList()

  {

    return this.xmlOrmAckErrorList;

  }

  public void setXmlOrmAckErrorList(ArrayList<Object> xmlOrmAckErrorList)

  {

    this.xmlOrmAckErrorList = xmlOrmAckErrorList;

  }

  String sessionUserName = null;

  String pageType = null;

  public String getPageType()

  {

    return this.pageType;

  }

  public void setPageType(String pageType)

  {

    this.pageType = pageType;

  }

  public String getSessionUserName()

  {

    return this.sessionUserName;

  }

  public void setSessionUserName(String sessionUserName)

  {

    this.sessionUserName = sessionUserName;

  }

}