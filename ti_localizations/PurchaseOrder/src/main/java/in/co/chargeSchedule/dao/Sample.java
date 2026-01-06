package in.co.chargeSchedule.dao;

import in.co.chargeSchedule.utility.DBConnectionUtility;
import in.co.chargeSchedule.utility.LoggableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
 
public class Sample
{
  private static Logger logger = Logger.getLogger(Sample.class.getName());
  public static void main(String[] args)
  {
    String s1 = "1200.03 inr";
    String noOnly = s1.replaceAll("[^0-9.]", "");
    String alphaOnly = s1.replaceAll("[^A-Za-z]+", "");

 
    logger.info("Characters " + noOnly);
  }
  public boolean executeGenericQuery(String query, String parameters)
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    try
    {
      int parameterCount = 1;
      connection = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(connection, query);
      if (((parameters instanceof String)) && (!parameters.trim().equalsIgnoreCase(""))) {
        for (String invidualParameters : parameters.split("\\|"))
        {
          logger.info("individual Parameter" + invidualParameters);
          loggableStatement.setString(parameterCount, invidualParameters);
          parameterCount++;
        }
      }
      logger.info("Query " + loggableStatement.getQueryString());
      resultSet = loggableStatement.executeQuery();
      while (resultSet.next()) {
        if (resultSet.getInt(1) > 0) {
          return true;
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, resultSet);
    }
    return false;
  }
}
