package in.co.boe.businessdelegate.boe_viewer;

package in.co.boe.businessdelegate.boe_viewer;

import in.co.boe.businessdelegate.BaseBusinessDelegate;

import in.co.boe.businessdelegate.exception.BusinessException;

import in.co.boe.dao.boe_viewer.BOEViwerDAO;

import in.co.boe.vo.BOEViewerVO;

import org.apache.log4j.Logger;
 
public class BOEViwerBD

  extends BaseBusinessDelegate

{

  private static Logger logger = Logger.getLogger(BOEViwerBD.class

    .getName());

  static BOEViwerBD bd;

  public static BOEViwerBD getBD()

  {

    if (bd == null) {

      bd = new BOEViwerBD();

    }

    return bd;

  }

  public BOEViewerVO getBOEIssuance(BOEViewerVO viewerVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    BOEViwerDAO dao = null;

    try

    {

      dao = BOEViwerDAO.getBD();

      viewerVO = dao.getBOEIssuance(viewerVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return viewerVO;

  }

}
 
