package in.co.stp.action;

import com.misys.tiplus2.service.access.UnrecognisedServiceName;
import com.misys.tiplus2.service.access.ejb.EnigmaServiceAccess;
import com.misys.tiplus2.service.access.ejb.EnigmaServiceAccessHome;
import in.co.stp.dao.DBPropertiesLoader;
import in.co.stp.utility.DBConnectionUtility;
import in.co.stp.utility.LoggableStatement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;
import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class TIPlusEJBClient
{
  private static final Logger logger = LoggerFactory.getLogger(TIPlusEJBClient.class);
  public static String process(String message, int start)
  {
    result = "Unable to Process";
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs1 = null;
    String ejbClientUrl = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID='BOOTSTRAPURL' AND ACTIVE='Y'";
      log = new LoggableStatement(con, query);
      rs1 = log.executeQuery();
      if (rs1.next())
      {
        logger.info("value " + rs1.getString("VALUE1"));
        ejbClientUrl = rs1.getString("VALUE1");
        logger.info("ETT_PARAMETER_TBL EJB URL-----" + ejbClientUrl);
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception in bootsrap url--------" + exception.getMessage());
      logger.info("Exception in bootsrap url--------" + exception.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs1);
    }
    logger.info("EjbclientUrl------" + ejbClientUrl);

 
    logger.info("Connecting : " + ejbClientUrl);
    try
    {
      System.getProperties().put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
      logger.debug("Step 1");
      System.getProperties().put("java.naming.provider.url", ejbClientUrl);
      logger.debug("Step 2");
      Context ctx = new InitialContext();
      logger.debug("Step 3");
      Object ejbObject = ctx.lookup("ejb/EnigmaServiceAccess");
      logger.debug("Step 4");
      EnigmaServiceAccessHome accessBeanHome = (EnigmaServiceAccessHome)PortableRemoteObject.narrow(ejbObject, EnigmaServiceAccessHome.class);
      logger.debug("Step 5");
      try
      {
        EnigmaServiceAccess accessB = accessBeanHome.create();
        result = accessB.process(message);
        logger.info("Response from EJB CLient -> \n" + result);
      }
      catch (RemoteException e)
      {
        logger.error("RemoteException " + e.getMessage());
        e.printStackTrace();
      }
      catch (CreateException e)
      {
        logger.error("CreateException " + e.getMessage());
        e.printStackTrace();
      }
      catch (IOException ex)
      {
        logger.error("IOException " + ex.getMessage());
        ex.printStackTrace();
      }
      catch (UnrecognisedServiceName e)
      {
        logger.error("UnrecognisedServiceName " + e.getMessage());
        e.printStackTrace();
      }
      catch (Exception e)
      {
        logger.error("Exception " + e.getMessage());
        e.printStackTrace();
      }
      return result;
    }
    catch (NamingException ex)
    {
      logger.info("TIPlusEJBClient---Exception----------" + ex.getMessage());
      ex.printStackTrace();
    }
  }
  public static void createFile(Exception e, String result, int start, String message) {}
  public static void threadProcess(String input)
  {
    logger.info("Running..............." + input);
  }
  public static String readFile(String file)
    throws IOException
  {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line = null;
    StringBuilder stringBuilder = new StringBuilder();
    String ls = System.getProperty("line.separator");
    while ((line = reader.readLine()) != null)
    {
      stringBuilder.append(line);
      stringBuilder.append(ls);
    }
    return stringBuilder.toString();
  }
  public static void main(String[] args)
  {
    DBPropertiesLoader.initialize("sourcedb.properties");
    process("terte", 0);
    logger.info(" done ..");
  }
}
