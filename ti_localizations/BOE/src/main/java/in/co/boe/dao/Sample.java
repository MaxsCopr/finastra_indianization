package in.co.boe.dao;

import in.co.boe.utility.DBConnectionUtility;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
 
public class Sample
{
  private static Logger logger = Logger.getLogger(Sample.class.getName());
  public static void main(String[] args)
  {
    try
    {
      Sample s1 = new Sample();

 
      s1.onFetchPreshipEXPCOLSETTclayButton();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  public int pushToExtDataBase(String masterRef, String eventRef, String loanRef)
    throws SQLException
  {
    int insertedRowCount = 0;
    ResultSet aResultSet = null;
    Connection aConnection = null;
    CallableStatement aCallableStatement = null;
    try
    {
      aConnection = DBConnectionUtility.getConnection();
      if (aConnection != null)
      {
        String procedureQuery = "{call ETT_PRESHIPMENT_CLOSURE(?,?,?)}";
        aCallableStatement = aConnection.prepareCall(procedureQuery);
        aCallableStatement.setString(1, masterRef);
        aCallableStatement.setString(2, eventRef);
        aCallableStatement.setString(3, loanRef);
        insertedRowCount = aCallableStatement.executeUpdate();
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, aResultSet);
    }
    return insertedRowCount;
  }
  public void onFetchPreshipEXPCOLSETTclayButton()
  {
    Connection con = null;
    PreparedStatement prepareStmt = null;
    ResultSet result_loan_emplty = null;
    String masref = "0958OCF170201108";
    String eventRef = "PAY001";

 
    String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };
    int temp_count = 0;
    String startCurr = "";
    double sumAmount = 0.0D;
    int sequence = 0;
    int count = 0;
    int cc = 0;
    String loanType = "";
    ArrayList<String> loans = new ArrayList();
    try
    {
      ArrayList<String> sumAmountInAllCurrencies = new ArrayList();
      masref = "0958OCF170201108";
      eventRef = "PAY001";
      temp_count = 0;
      if (temp_count == 0)
      {
        logger.info("inside if");
        con = DBConnectionUtility.getConnection();
        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='" + 
          masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
        logger.info("loan query is " + loan_query);
        PreparedStatement ps = con.prepareStatement(loan_query);
        ResultSet ress = ps.executeQuery();
        while (ress.next())
        {
          loans.add(ress.getString("LOAN"));
          loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " " + 
            ress.getString("CURR").trim());
          loans.add(ress.getString("VDATE"));
          loanType = ress.getString("TYPE");
        }
        logger.info("Loan size " + loans.size());
        for (int i = 0; i < loans.size() / 3; i++)
        {
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

 
          logger.info("DEALREF" + (String)loans.get(cc));
          logger.info("REAMOUNT" + (String)loans.get(cc + 1));
          Date date1 = format.parse((String)loans.get(cc + 2));
          logger.info("VAL Date" + date1);
        }
        int a;
        String query;
        PreparedStatement prep;
        ResultSet result;
        double amount;
        int b;
        return;
      }
    }
    catch (Exception localException1) {}finally
    {
      DBConnectionUtility.surrenderDB(con, prepareStmt, result_loan_emplty);
    }
  }
}
