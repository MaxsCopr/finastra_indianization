package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;

import in.co.localization.businessdelegate.exception.BusinessException;

import in.co.localization.dao.localization.XMLFileDAO;

import in.co.localization.vo.localization.XMLFileVO;

import java.util.ArrayList;

import org.apache.log4j.Logger;
 
public class XMLFileBD

  extends BaseBusinessDelegate

{

  private static Logger logger = Logger.getLogger(XMLFileBD.class

    .getName());

  static XMLFileBD bd;

  public static XMLFileBD getBD()

  {

    if (bd == null) {

      bd = new XMLFileBD();

    }

    return bd;

  }

  public XMLFileVO readXMLFileData(XMLFileVO xmlFileVO)

    throws BusinessException

  {

    logger.info("--Entering Method---readXMLFileData----");

    XMLFileDAO dao = null;

    try

    {

      dao = XMLFileDAO.getDAO();

      xmlFileVO = dao.readXMLFileData(xmlFileVO);

    }

    catch (Exception exception)

    {

      logger.info("Reading XML File Exception-------------->" + exception.getMessage());

      throwBDException(exception);

    }

    logger.info("Exiting readXMLFileData ethod");

    return xmlFileVO;

  }

  public XMLFileVO getErrorListDownload(XMLFileVO xmlFileVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    XMLFileDAO dao = null;

    try

    {

      dao = XMLFileDAO.getDAO();

      xmlFileVO = dao.getErrorListDownload(xmlFileVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return xmlFileVO;

  }

  public XMLFileVO getXMLFileList(XMLFileVO xmlFileVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    ArrayList<XMLFileVO> fileList = null;

    XMLFileDAO dao = null;

    try

    {

      dao = XMLFileDAO.getDAO();

      xmlFileVO = dao.getXMLFileList(xmlFileVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return xmlFileVO;

  }

}