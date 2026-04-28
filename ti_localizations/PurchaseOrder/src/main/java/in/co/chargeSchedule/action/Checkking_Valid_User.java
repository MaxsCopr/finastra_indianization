package in.co.chargeSchedule.action;


import in.co.chargeSchedule.utility.DBConnectionUtility;
import in.co.chargeSchedule.utility.LoggableStatement;
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
   Connection them_con = null;
   ResultSet rst = null;
   try
   {
     them_con = DBConnectionUtility.getWiseConnection();
     if (them_con == null) {
       logger.info("Global Connection is Null-----------------");
     }
     String Check_Valid_User = "SELECT * FROM SS_USER WHERE USERNAME=?";
     lst = new LoggableStatement(them_con, Check_Valid_User);
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
     if (them_con != null) {
       them_con.close();
     }
   }
   return userstatus;
 }
}
