package in.co.prishipment.businessdelegate;
 
import in.co.prishipment.businessdelegate.exception.BusinessException;

import in.co.prishipment.dao.PreShipmentDAO;

import in.co.prishipment.vo.PreShipmentVO;

import java.io.PrintStream;

import org.apache.log4j.Logger;
 
public class PreShipmentBD

  extends BaseBusinessDelegate

{

  private static Logger logger = Logger.getLogger(PreShipmentBD.class

    .getName());

  static PreShipmentBD bd = null;

  String sessionId;

  public static PreShipmentBD getBD()

  {

    if (bd == null) {

      bd = new PreShipmentBD();

    }

    return bd;

  }

  public String getSessionUser(String sessionUserName)

    throws BusinessException

  {

    logger.info("Entering Method");

    PreShipmentDAO dao = null;

    try

    {

      dao = PreShipmentDAO.getDAO();

      this.sessionId = dao.getSessionUserID(sessionUserName);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return this.sessionId;

  }

  public String getValDate()

  {

    PreShipmentDAO dao = null;

    String vDate = "";

    try

    {

      dao = PreShipmentDAO.getDAO();

      vDate = dao.getValueDate();

    }

    catch (Exception e)

    {

      logger.info("exception in getValDate " + e.getMessage());

    }

    return vDate;

  }

  public PreShipmentVO fetchShipment(PreShipmentVO preShipVo)

    throws BusinessException

  {

    PreShipmentDAO dao = null;

    try

    {

      System.out.println("PreshipmentBD: fetchShipment ");

      logger.info("Enetered DAO");

      dao = PreShipmentDAO.getDAO();

      preShipVo = dao.fetchPreShipment(preShipVo);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return preShipVo;

  }

  public PreShipmentVO processRepayment(PreShipmentVO preShipVo)

    throws BusinessException

  {

    PreShipmentDAO dao = null;

    try

    {

      dao = PreShipmentDAO.getDAO();

      preShipVo = dao.repayProcess(preShipVo);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return preShipVo;

  }

  public void getTiDate()

    throws BusinessException

  {

    logger.info("This is Inside of get Ti Date ================>>> getTiDate()");

    PreShipmentDAO dao = null;

    try

    {

      dao = PreShipmentDAO.getDAO();

      dao.getTIDate();

    }

    catch (Exception exception)

    {

      exception.printStackTrace();

    }

    logger.info("Exiting Method");

  }

  public double getDecimalPoints(String currency)

  {

    double val = 0.0D;

    PreShipmentDAO dao = null;

    try

    {

      dao = PreShipmentDAO.getDAO();

      val = dao.getDecimalforCurrency(currency);

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    return val;

  }

  public PreShipmentVO fetchdeletePreship(PreShipmentVO preShipVo)

    throws BusinessException

  {

    PreShipmentDAO dao = null;

    try

    {

      dao = PreShipmentDAO.getDAO();

      preShipVo = dao.fetchdeletePreship(preShipVo);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return preShipVo;

  }

  public PreShipmentVO deletefetchPreship(PreShipmentVO preShipVo)

    throws BusinessException

  {

    PreShipmentDAO dao = null;

    try

    {

      dao = PreShipmentDAO.getDAO();

      preShipVo = dao.deletefetchPreship(preShipVo);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return preShipVo;

  }

}