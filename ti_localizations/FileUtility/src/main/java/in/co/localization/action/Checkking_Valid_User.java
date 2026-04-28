package in.co.localization.action;
 
import java.sql.Connection;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
 
public class Checkking_Valid_User
{
  private static Logger logger = Logger.getLogger(Checkking_Valid_User.class.getName());
  public boolean isValidTIUser(String username)
    throws Exception
  {
    boolean userstatus = false;
    LoggableStatement lst = null;
    Connection wise_con = null;
    ResultSet rst = null;
    try
    {
    	wise_con = DBConnectionUtility.getGlobalWiseConnection();
      if (wise_con == null) {
        logger.info("Global Connection is Null-----------------");
      }
      String Check_Valid_User = "SELECT * FROM SS_USER WHERE USERNAME=?";
      lst = new LoggableStatement(wise_con, Check_Valid_User);
      lst.setString(1, username);

 
      rst = lst.executeQuery();
      logger.info("Validating User Query------------------" + lst.getQueryString());
      if (rst.next())
      {
        logger.info("Validating User ID VALUE------------------" + rst.getString("USERNAME"));
        userstatus = true;
      }
      logger.info("User Status-----------" + userstatus);
    }
    catch (Exception e)
    {
      logger.info("isValidTIUser Exception -----------------" + e);
    }
    finally
    {
      if (lst != null) {
        lst.close();
      }
      if (rst != null) {
        rst.close();
      }
      if (wise_con != null) {
    	  wise_con.close();
      }
    }
    return userstatus;
  }
}