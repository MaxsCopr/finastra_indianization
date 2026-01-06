package in.co.stp.dao;

import in.co.stp.utility.CommonMethods;
import in.co.stp.utility.DBConnectionUtility;
import in.co.stp.utility.LoggableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ODCUpdate
  extends AbstractDAO
{
  private static Logger logger = LogManager.getLogger(ODCUpdate.class.getName());
 
  public String Updateexteventshc(String pri_ref, String portcode, String FORM_NO, String shipbillno, String updatedate)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String key = null;
    String updateexteventshc = null;
    int flagform = 0;
    int flagshp = 0;
    int flagformshp = 0;
    String getvalueexteventshc = "select shc.xkey as keyvalue from master mas, baseevent bev, exteventshc shc where  mas.key97=bev.MASTER_KEY  and shc.FK_EVENT=bev.EXTFIELD  and trim(mas.PRI_REF)= ?  and trim(shc.cportco)=? and shc.cbillda is null ";
    if ((!CommonMethods.isNullValue(FORM_NO)) && (!CommonMethods.isNullValue(shipbillno)))
    {
      getvalueexteventshc = getvalueexteventshc + " and  trim(shc.CFORMN)=? and trim(shc.cbillnum)=? ";
      flagformshp++;
    }
    else if (!CommonMethods.isNullValue(FORM_NO))
    {
      getvalueexteventshc = getvalueexteventshc + " and trim(shc.CFORMN)=? ";
      flagform++;
    }
    else if (!CommonMethods.isNullValue(shipbillno))
    {
      getvalueexteventshc = getvalueexteventshc + " and trim(shc.cbillnum)=? ";
      flagshp++;
    }
    try
    {
      con = DBConnectionUtility.getConnection();
      ps = con.prepareStatement(getvalueexteventshc);
      ps.setString(1, pri_ref);
      ps.setString(2, portcode);
      if (flagform > 0) {
        ps.setString(3, FORM_NO);
      }
      if (flagshp > 0) {
        ps.setString(3, shipbillno);
      }
      if (flagformshp > 0)
      {
        ps.setString(3, FORM_NO);
        ps.setString(4, shipbillno);
      }
      rs = ps.executeQuery();
      while (rs.next()) {
        key = rs.getString("keyvalue");
      }
      updateexteventshc = "update exteventshc set cbillda=TO_DATE('" + updatedate + "','YYYY-MM-DD') where xkey=" + key + " ";
      logger.info("|||||||||||||||||||||" + updateexteventshc);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("Error in catching ***************" + e.getMessage());
    }
    finally
    {
      closeResultSet(rs);
      closePreparedStatement(ps);
     
      closeConnection(con);
    }
    return updateexteventshc;
  }
 
  public String getvalueexteventspd(String pri_ref, String portcode, String FORM_NO, String shipbillno, String updatedate, String leodate)
  {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    LoggableStatement ls = null;
    String key = null;
    int flagform = 0;
    int flagshp = 0;
    int flagformshp = 0;
   
    String updateexteventspd = "";
    try
    {
      String getvalueexteventspd = "select spd.xkey as keyvalue from master mas, baseevent bev, EXTEVENTSPD spd where  mas.key97=bev.MASTER_KEY   and spd.FK_EVENT=bev.EXTFIELD   and trim(mas.PRI_REF)=?  and trim(spd.sdprtcde)=?  and spd.sdbildat is null  ";
      if ((!CommonMethods.isNullValue(FORM_NO)) && (!CommonMethods.isNullValue(shipbillno)))
      {
        getvalueexteventspd = getvalueexteventspd + " and trim(spd.SDFORMNO)=? and trim(spd.sdbillno)=?";
        flagformshp++;
        logger.info("*------Inside -FORM_NO --shipbillno--*");
      }
      else if (!CommonMethods.isNullValue(FORM_NO))
      {
        getvalueexteventspd = getvalueexteventspd + " and  trim(spd.SDFORMNO)=? ";
        flagform++;
        logger.info("*------Inside ---FORM_NO ----*");
      }
      else if (!CommonMethods.isNullValue(shipbillno))
      {
        getvalueexteventspd = getvalueexteventspd + " and  trim(spd.sdbillno)=? ";
        flagshp++;
        logger.info("*------Inside --- --shipbillno--*");
      }
      try
      {
        con = DBConnectionUtility.getConnection();
        ps = con.prepareStatement(getvalueexteventspd);
        ps.setString(1, pri_ref);
        ps.setString(2, portcode);
        if (flagform > 0) {
          ps.setString(3, FORM_NO);
        }
        if (flagshp > 0) {
          ps.setString(3, shipbillno);
        }
        if (flagformshp > 0)
        {
          ps.setString(3, FORM_NO);
          ps.setString(4, shipbillno);
        }
        rs = ps.executeQuery();
        while (rs.next())
        {
          key = rs.getString("keyvalue").trim();
          logger.info("--------key------" + key);
        }
        if ((!CommonMethods.isNullValue(updatedate)) && (CommonMethods.isNullValue(leodate)))
        {
          updateexteventspd = "update exteventspd set  sdbildat= to_date('" + updatedate + "','YYYY-MM-DD') where xkey= " + key + " ";
          logger.info("!!!!!!!!!111111111!!!!!!!!!" + updateexteventspd);
        }
        else if ((CommonMethods.isNullValue(updatedate)) && (!CommonMethods.isNullValue(leodate)))
        {
          updateexteventspd = "update exteventspd set SDLEODAT=to_date('" + leodate + "','YYYY-MM-DD') where xkey= " + key + " ";
          logger.info("!!!!!!!!!!222222222222!!!!!!!!" + updateexteventspd);
        }
        else
        {
          updateexteventspd = "update exteventspd set  sdbildat= to_date('" + updatedate + "','YYYY-MM-DD') , SDLEODAT=to_date('" + leodate + "','YYYY-MM-DD') where xkey= " + key + " ";
          logger.info("!!!!!!!!!333333333333!!!!!!!!!" + updateexteventspd);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
        logger.info("Error in catching ***************" + e.getMessage());
      }
      finally
      {
        closeResultSet(rs);
        closePreparedStatement(ps);
        closeLoggableStatement(ls);
        closeConnection(con);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return updateexteventspd;
  }
 
  public String getupdateexteventinv(String pri_ref, String SHP_INVOICE_NO, String FORM_NO, String shipbillno, String updatedate)
  {
    String key = null;
    String updateexteventinv = null;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    LoggableStatement ls = null;
    int flagform = 0;
    int flagshp = 0;
    int flagformshp = 0;
    try
    {
      String getexteventinvedi = "select inv.xkey as keyvalue from master mas, baseevent bev, EXTEVENTINV inv where  mas.key97=bev.MASTER_KEY   and inv.FK_EVENT=bev.EXTFIELD  and trim(mas.PRI_REF)=?   and inv.invdate is null ";
      if ((!CommonMethods.isNullValue(FORM_NO)) && (!CommonMethods.isNullValue(shipbillno)))
      {
        getexteventinvedi = getexteventinvedi + " and trim(inv.IFORNO)=? and trim(inv.ishpbill)=?";
        flagformshp++;
      }
      else if (!CommonMethods.isNullValue(FORM_NO))
      {
        getexteventinvedi = getexteventinvedi + " and  trim(inv.IFORNO)=? ";
        flagform++;
      }
      else if (!CommonMethods.isNullValue(shipbillno))
      {
        getexteventinvedi = getexteventinvedi + " and  trim(inv.ishpbill)=? ";
        flagshp++;
      }
      try
      {
        con = DBConnectionUtility.getConnection();
        ps = con.prepareStatement(getexteventinvedi);
        ps.setString(1, pri_ref);
        if (flagform > 0) {
          ps.setString(2, FORM_NO);
        }
        if (flagshp > 0) {
          ps.setString(2, shipbillno);
        }
        if (flagformshp > 0)
        {
          ps.setString(2, FORM_NO);
          ps.setString(3, shipbillno);
        }
        rs = ps.executeQuery();
        while (rs.next()) {
          key = rs.getString("keyvalue").trim();
        }
        updateexteventinv = " update EXTEVENTINV set invdate=to_date('" + updatedate + "','RRRR-mm-dd') where xkey=" + key + " ";
        logger.info("@@@@@@@@@@@@@@@@@@@" + updateexteventinv);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        logger.info("Error in catching ***************" + e.getMessage());
      }
      finally
      {
        closeResultSet(rs);
        closePreparedStatement(ps);
        closeLoggableStatement(ls);
        closeConnection(con);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return updateexteventinv;
  }
 
  public String getgetremittance(String pri_ref, String cust, String updatedate)
    throws Exception
  {
    String key = null;
    String updateRemittance = null;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    LoggableStatement ls = null;
    try
    {
      String getremittance = "select  adv.XKEY as keyvalue from master mas, baseevent bev, EXTEVENTADV adv where  mas.key97=bev.MASTER_KEY  and adv.FK_EVENT=bev.EXTFIELD  and trim(mas.PRI_REF)= ?   and adv.datrem is null ";
     


      con = DBConnectionUtility.getConnection();
      ps = con.prepareStatement(getremittance);
      ps.setString(1, pri_ref);
     
      logger.info("getremittance" + getremittance);
      rs = ps.executeQuery();
      while (rs.next()) {
        key = rs.getString("keyvalue").trim();
      }
      updateRemittance = "update EXTEVENTADV set datrem= to_date('" + updatedate + "','YYYY-MM-DD')  where xkey=" + key + " ";
      logger.info("###################" + updateRemittance);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("Error in catching ***************" + e.getMessage());
    }
    finally
    {
      closeResultSet(rs);
      closePreparedStatement(ps);
      closeLoggableStatement(ls);
      closeConnection(con);
    }
    return updateRemittance;
  }
 
  public int insert_ODCUPDATE_PANE(String PRI_REF, String updateQuery, String status, String Shpbillno, String portcde, String formno, String updatedate, String leodate, String invno, String invdate, String cust, String remitNum, String firc, String remitName, String remitDate, String formtype)
    throws Exception
  {
    String insertEventpane = "insert into ETT_ODC_UPDATE_PANE (THEIRREF,UPDATED_QUERY,STATUS,SHIPBILLNO,PORTCODE,FORMNO,SHIPDATE,LEODATE,INVOICENO,INVOICEDATE,CUST,REMITTANCENUMBER,FIRCNUMBER,REMITTER_NAME,REMITTERDATE,FORMTYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    Connection con = null;
    LoggableStatement ls = null;
    int insertCount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      ls = new LoggableStatement(con, insertEventpane);
      logger.info("-------------********updateQuery====In pane********-----" + updateQuery);
      ls.setString(1, PRI_REF);
      ls.setString(2, updateQuery);
      ls.setString(3, status);
      ls.setString(4, Shpbillno);
      ls.setString(5, portcde);
      ls.setString(6, formno);
      ls.setString(7, updatedate);
      ls.setString(8, leodate);
      ls.setString(9, invno);
      ls.setString(10, invdate);
      ls.setString(11, cust);
      ls.setString(12, remitNum);
      ls.setString(13, firc);
      ls.setString(14, remitName);
      ls.setString(15, remitDate);
      ls.setString(16, formtype);
      insertCount = ls.executeUpdate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      closeLoggableStatement(ls);
      closeConnection(con);
    }
    return insertCount;
  }
 
  public void update_ODCUPDATE_PANE()
    throws Exception
  {
    String toupdateMultipleData = null;
    String singleUpdateQuery = null;
    String ourReference = null;
    int checkWhile = 0;
    Connection con = null;
    LoggableStatement ls = null;
    ResultSet rs = null;
   
    LoggableStatement lss = null;
    LoggableStatement lss1 = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      con.setAutoCommit(false);
      ls = new LoggableStatement(con, "SELECT THEIRREF , UPDATED_QUERY FROM ETT_ODC_UPDATE_PANE WHERE STATUS='P' ");logger.info("*-------getDataTOUPDATE----------*SELECT THEIRREF , UPDATED_QUERY FROM ETT_ODC_UPDATE_PANE WHERE STATUS='P' ");
      rs = ls.executeQuery();
      logger.info("#-----Result set count--------#" + rs.getRow());
      while (rs.next())
      {
        logger.info("#-----Check While-----#" + checkWhile);
        ourReference = rs.getString("THEIRREF");
        logger.info("*--------ourReference--------------*" + ourReference);
        toupdateMultipleData = rs.getString("UPDATED_QUERY");
        logger.info("*--------toupdateMultipleData--------------*" + toupdateMultipleData);
        if (!CommonMethods.isNullValue(toupdateMultipleData))
        {
          String[] toupdateSingledata = toupdateMultipleData.split(":");
          logger.info("*-------Total Length ----------*" + toupdateSingledata.length);
          for (int i = 0; i < toupdateSingledata.length; i++)
          {
            logger.info("-------i count-----" + i);
            singleUpdateQuery = toupdateSingledata[i];logger.info("*--------singleUpdateQuery--------------*" + singleUpdateQuery);
            try
            {
              lss = new LoggableStatement(con, singleUpdateQuery);
              logger.info("*----Inside try----singleUpdateQuery--------------*" + singleUpdateQuery);
              lss.executeUpdate();
              con.commit();
            }
            catch (Exception e)
            {
              e.printStackTrace();
              logger.info("Error in catching ***************" + e.getMessage());
            }
          }
        }
        try
        {
          lss1 = new LoggableStatement(con, "UPDATE ETT_ODC_UPDATE_PANE SET STATUS='A' WHERE THEIRREF=? ");
          logger.info("*-------updateStatus----------*UPDATE ETT_ODC_UPDATE_PANE SET STATUS='A' WHERE THEIRREF=? ");
          lss1.setString(1, ourReference);
          lss1.executeUpdate();
        }
        catch (Exception e)
        {
          e.printStackTrace();
          logger.info("Error in catching ***************" + e.getMessage());
        }
        checkWhile++;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      closeResultSet(rs);
      closeLoggableStatement(ls);
      closeLoggableStatement(lss);
      closeLoggableStatement(lss1);
      closeConnection(con);
    }
  }
}
