package in.co.localization.action;
 
import in.co.localization.businessdelegate.localization.AdTransferBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.utility.CommonMethods;

import in.co.localization.vo.localization.EDMPSProcessVO;

import in.co.localization.vo.localization.EodDownloadVO;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.util.ArrayList;

import java.util.List;

import java.util.Map;

import java.util.zip.ZipEntry;

import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
 
public class EodDownloadAction

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(EodDownloadAction.class

    .getName());

  private static final long serialVersionUID = 1L;

  List<String> filesListInDir = new ArrayList();

  Map<String, String> eodList = null;

  EodDownloadVO eodDownloadVO;

  ArrayList<EDMPSProcessVO> eodFileList = null;

  private HttpServletResponse response;

  private InputStream inputStream;

  private String fileName;

  private long contentLength;

  public String getFileName()

  {

    return this.fileName;

  }

  public void setFileName(String fileName)

  {

    this.fileName = fileName;

  }

  public InputStream getInputStream()

  {

    return this.inputStream;

  }

  public void setInputStream(InputStream inputStream)

  {

    this.inputStream = inputStream;

  }

  public long getContentLength()

  {

    return this.contentLength;

  }

  public void setContentLength(long contentLength)

  {

    this.contentLength = contentLength;

  }

  public EodDownloadVO getEodDownloadVO()

  {

    return this.eodDownloadVO;

  }

  public void setEodDownloadVO(EodDownloadVO eodDownloadVO)

  {

    this.eodDownloadVO = eodDownloadVO;

  }

  public Map<String, String> getEodList()

  {

    return ActionConstants.EDPMS_IDPMS;

  }

  public void setEodList(Map<String, String> eodList)

  {

    this.eodList = eodList;

  }

  public HttpServletResponse getResponse()

  {

    return this.response;

  }

  public void setResponse(HttpServletResponse response)

  {

    this.response = response;

  }

  public ArrayList<EDMPSProcessVO> getEodFileList()

  {

    return this.eodFileList;

  }

  public void setEodFileList(ArrayList<EDMPSProcessVO> eodFileList)

  {

    this.eodFileList = eodFileList;

  }

  public String execute()

    throws ApplicationException

  {

    logger.info("Entering Method");

    AdTransferBD bd = null;

    try

    {

      isSessionAvailable();

      bd = AdTransferBD.getBD();

      logger.info("This is Up of the EDPMS/IDPMS EOD file ===>");

      this.eodFileList = bd.fetchEODFileList(this.eodDownloadVO);

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchEodFileType()

    throws ApplicationException

  {

    logger.info("Entering Method");

    AdTransferBD bd = null;

    try

    {

      bd = AdTransferBD.getBD();

      this.eodFileList = bd.fetchEODFileList(this.eodDownloadVO);

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String downloadEodFiles()

    throws ApplicationException

  {

    logger.info("Entering Method");

    AdTransferBD bd = null;

    CommonMethods commonMethods = null;

    try

    {

      bd = AdTransferBD.getBD();

      commonMethods = new CommonMethods();

      this.eodFileList = bd.fetchEODFileList(this.eodDownloadVO);

      String eodDate = commonMethods.getEmptyIfNull(this.eodDownloadVO.getEodDate()).replace("-", "");

      String eodFileType = commonMethods.getEmptyIfNull(this.eodDownloadVO.getEodFileType()).trim();

      String eodFileName = commonMethods.getEmptyIfNull(this.eodDownloadVO.getEodFileName()).trim();

 
 
      String fileLoc = ActionConstants.EODFILEPATH;

      String zipFileName = fileLoc + eodFileType + "/" + eodFileName + "/" + eodDate + "/" + eodDate + "_" + eodFileName + ".zip";

      fileLoc = fileLoc + eodFileType + "/" + eodFileName + "/" + eodDate;

      logger.info("zipFileName--------------123456---------" + zipFileName);

      File isAlreadyExists = new File(zipFileName);

      if (!isAlreadyExists.exists())

      {

        File f = new File(fileLoc);

        if (f.exists())

        {

          zipDirectory(f, zipFileName);

          File fileToDownload = new File(zipFileName);

          if (fileToDownload.exists())

          {

            this.inputStream = new FileInputStream(fileToDownload);

            this.fileName = fileToDownload.getName();

            this.contentLength = fileToDownload.length();

          }

        }

        else

        {

          addActionError(eodFileName + " " + "XML file is not found");

          return "error";

        }

      }

      else if (isAlreadyExists.exists())

      {

        this.inputStream = new FileInputStream(isAlreadyExists);

        this.fileName = isAlreadyExists.getName();

        this.contentLength = isAlreadyExists.length();

      }

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  private void zipDirectory(File dir, String zipDirName)

  {

    try

    {

      populateFilesList(dir);

      FileOutputStream fos = new FileOutputStream(zipDirName);

      ZipOutputStream zos = new ZipOutputStream(fos);

      for (String filePath : this.filesListInDir)

      {

        ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));

        zos.putNextEntry(ze);

        FileInputStream fis = new FileInputStream(filePath);

        byte[] buffer = new byte[1024];

        int len;

        while ((len = fis.read(buffer)) > 0)

        {

          int len;

          zos.write(buffer, 0, len);

        }

        zos.closeEntry();

        fis.close();

      }

      zos.close();

      fos.close();

    }

    catch (IOException e)

    {

      e.printStackTrace();

    }

  }

  private void populateFilesList(File dir)

    throws IOException

  {

    File[] files = dir.listFiles();

    for (File file : files) {

      if (file.isFile()) {

        this.filesListInDir.add(file.getAbsolutePath());

      } else {

        populateFilesList(file);

      }

    }

  }

}