package in.co.localization.vo.localization;
 
import in.co.localization.action.DBConnectionUtility;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
 
public class ShippingBillReportsView

{

  public static List<Object[]> getSHBViewDetails(String number)

  {

    List<Object[]> DetailsList = new ArrayList();

    System.out.println("inside ShippingBillReportsView");

    Connection conn = null;

    PreparedStatement ps = null;

    ResultSet resultSet = null;

    try

    {

      conn = DBConnectionUtility.getConnection();

      String query = "  SELECT DATE_OF_LODGEMENT, MASTER_REF, EVENT_REF, CCY, BILL_AMOUNT,  SHIPPING_BILL_NO, SHIPPING_BILL_DATE, FORM_NO, PORT_CODE, SB_CCY, SB_AMOUNT, IECODE  FROM REP_SHB_VB_VIEW WHERE FORM_NO = '" + 

        number.trim() + "' or SHIPPING_BILL_NO = '" + number.trim() + "'";

 
      System.out.println("Query -> " + query);

      ps = conn.prepareStatement(query);

      resultSet = ps.executeQuery();

      while (resultSet.next())

      {

        Object[] rowData = new Object[12];

        rowData[0] = resultSet.getDate("DATE_OF_LODGEMENT");

        rowData[1] = resultSet.getString("MASTER_REF");

        rowData[2] = resultSet.getString("EVENT_REF");

        rowData[3] = resultSet.getString("CCY");

        rowData[4] = resultSet.getString("BILL_AMOUNT");

        rowData[5] = resultSet.getString("SHIPPING_BILL_NO");

        rowData[6] = resultSet.getString("SHIPPING_BILL_DATE");

        rowData[7] = resultSet.getString("FORM_NO");

        rowData[8] = resultSet.getString("PORT_CODE");

        rowData[9] = resultSet.getString("SB_CCY");

        rowData[10] = resultSet.getString("SB_AMOUNT");

        rowData[11] = resultSet.getString("IECODE");

        DetailsList.add(rowData);

      }

    }

    catch (Exception e)

    {

      System.out.println(": Exception :" + e.getMessage());

    }

    finally

    {

      DatabaseUtility.surrenderConnection(conn, ps, resultSet);

    }

    System.out.println("DetailsList-> " + DetailsList);

    return DetailsList;

  }

  public static List<Object[]> getForwardContractDetails(String number)

  {

    List<Object[]> DetailsList = new ArrayList();

    System.out.println("inside ShippingBillReportsView");

    Connection conn = null;

    PreparedStatement ps = null;

    ResultSet resultSet = null;

    try

    {

      conn = DatabaseUtility.getTizoneConnection();

      String query = "SELECT  REFERENCE_NUM, FWC_REF_NUM,FWC_REF_NUM, FBO_ID_NUM, HOST_DEAL_CATEGORY, HOST_TRAN_DATE, START_DATE, END_DATE, RECORD_STATUS, BUY_OR_SELL, DEAL_AMOUNT, DEAL_AMOUNT_CCY, BUY_AMOUNT, BUY_AMOUNT_CCY, SELL_AMOUNT, SELL_AMOUNT_CCY, FWD_CONTRACT_RATE, COUNTERPARTY_STRING, SOL_ID, BILL_ID, RATE_CODE, NARRATIVE, ADDITIONAL_TEXT_9, HOST_DEAL_SUB_CATEGORY from ubift.host_deal_data@prod_fintrdb where REFERENCE_NUM = '" + 

 
 
        number.trim() + "' or BILL_ID = '" + number.trim() + "' or FWC_REF_NUM = '" + number.trim() + "'";

      System.out.println("Query -> " + query);

      ps = conn.prepareStatement(query);

      resultSet = ps.executeQuery();

      while (resultSet.next())

      {

        Object[] rowData = new Object[23];

        rowData[0] = resultSet.getString("REFERENCE_NUM");

        rowData[1] = resultSet.getString("FWC_REF_NUM");

        rowData[2] = resultSet.getString("FBO_ID_NUM");

        rowData[3] = resultSet.getString("HOST_DEAL_CATEGORY");

        rowData[4] = resultSet.getString("HOST_TRAN_DATE");

        rowData[5] = resultSet.getString("START_DATE");

        rowData[6] = resultSet.getString("END_DATE");

        rowData[7] = resultSet.getString("RECORD_STATUS");

        rowData[8] = resultSet.getString("BUY_OR_SELL");

        rowData[9] = resultSet.getString("DEAL_AMOUNT");

        rowData[10] = resultSet.getString("DEAL_AMOUNT_CCY");

        rowData[11] = resultSet.getString("BUY_AMOUNT");

        rowData[12] = resultSet.getString("BUY_AMOUNT_CCY");

        rowData[13] = resultSet.getString("SELL_AMOUNT");

        rowData[14] = resultSet.getString("SELL_AMOUNT_CCY");

        rowData[15] = resultSet.getString("FWD_CONTRACT_RATE");

        rowData[16] = resultSet.getString("COUNTERPARTY_STRING");

        rowData[17] = resultSet.getString("SOL_ID");

        rowData[18] = resultSet.getString("BILL_ID");

        rowData[19] = resultSet.getString("RATE_CODE");

        rowData[20] = resultSet.getString("NARRATIVE");

        rowData[21] = resultSet.getString("ADDITIONAL_TEXT_9");

        rowData[22] = resultSet.getString("HOST_DEAL_SUB_CATEGORY");

        DetailsList.add(rowData);

      }

    }

    catch (Exception e)

    {

      System.out.println("Exception :" + e.getMessage());

    }

    finally

    {

      DatabaseUtility.surrenderConnection(conn, ps, resultSet);

    }

    System.out.println("DetailsList-> " + DetailsList.toString());

    return DetailsList;

  }

}