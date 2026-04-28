package com.misys.tiplus2.customisation.extension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bs.theme.transport.client.ThemeTransportClient;
//import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation;
//import org.slf4j.Logger;
//import org.apache.log4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.LoggerFactory;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.EXTGOODS;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTable;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventOrmDetails;
import com.misys.tiplus2.customisation.entity.ExtEventShippingCollections;
import com.misys.tiplus2.customisation.entity.ExtEventShippingTable;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceData;
import com.misys.tiplus2.customisation.entity.ExtEventBOECAP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.foundations.lang.logging.Loggers;

public class ConnectionMaster extends EventExtension {

private static final Logger LOG = LoggerFactory.getLogger(ConnectionMaster.class);

private ValidationDetails validationDetails;
      Connection con, conn = null;
      PreparedStatement ps1, ps, ps2, dmsp, ccyd, pes, pre, ps3 = null;
      ResultSet rs1, rs, rs2, dmsr, rccy3, result, res, rs3 = null;
      String dmsstr = null;
      CallableStatement stmt = null;
      DecimalFormat df1 = new DecimalFormat("#0.00");// df1.format()
      // String output = "";
      // String tempexp = "";
      // String flagstat="";
      // String flagclmdate="";
      static ConnectionMaster cONNECTIONmASTER;

      private static SecretKeySpec secretKey;
      private static byte[] key;
      private static final String ALGORITHM = "AES";

      public ConnectionMaster() {
            // //Loggers.general().info(LOG,"ConnectionMaster started!");
      }

      public static Connection getConnection() {
            Connection connection = null;
            try {
//                Properties param = new Properties();
//                param.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
//                param.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
                  Context initialContext = new InitialContext();
                  DataSource dataSource = (DataSource) initialContext.lookup("jdbc/zone");
                  connection = dataSource.getConnection();
            } catch (NamingException e) {
                  e.printStackTrace();
                  LOG.info("Exception Websphere connection NamingException Error" + e.getMessage());
            } catch (SQLException e) {
                  e.printStackTrace();
                  LOG.info("Exception Websphere connection SQLException Error" + e.getMessage());
            }
            return connection;
      }

      public static Connection getThemeBridgeConnection() {
            Connection connection = null;
            // Loggers.general().info(LOG,"Themebridge Connection ----->");
            try {
                  Properties param = new Properties();
                  param.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
//                param.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
                  Context initialContext = new InitialContext(param);
                  // Loggers.general().info(LOG,"Themebridge Connection Start----->");
                  DataSource dataSource = (DataSource) initialContext.lookup("jdbc/themebridge");
                  connection = dataSource.getConnection();
                  // Loggers.general().info(LOG,"Themebridge Connection getconnection----->");
            } catch (NamingException e) {
                  e.printStackTrace();
                  // Loggers.general().info(LOG,"Error is " + e.getMessage());
                  // Loggers.general().info(LOG,"Connection Catch");
            } catch (SQLException e) {
                  e.printStackTrace();
                  // Loggers.general().info(LOG,"SQLException is " + e.getMessage());
                  // Loggers.general().info(LOG,"Connection Catch");
            }

            return connection;

      }

      public static Connection getNeftRtgsConnection() {
            Connection connection = null;
            try {
                  Properties param = new Properties();
                  param.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
//                param.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
                  Context initialContext = new InitialContext(param);
                  DataSource dataSource = (DataSource) initialContext.lookup("jdbc/neftrtgs");
                  connection = dataSource.getConnection();
            } catch (NamingException e) {
                  e.printStackTrace();
                  LOG.info("Error is " + e.getMessage());
                  // Loggers.general().info(LOG,"Connection Catch");
            } catch (SQLException e) {
                  e.printStackTrace();
                  LOG.info("SQLException is " + e.getMessage());
                  LOG.info("Connection Catch");
            }
            return connection;
      }

      public static void surrenderDB(Connection con, PreparedStatement stmt, ResultSet res) {
            try {
                  if (null != res) {
                        res.close();
                  }
                  if (null != stmt) {
                        stmt.close();
                  }
                  if (null != con) {
                        con.close();
                  }

            } catch (SQLException e) {
                  // Loggers.general().info(LOG,"Connection Failed! Check output console");
                  e.printStackTrace();
            }

      }

      public static void surrenderDB1(ResultSet res, PreparedStatement stmt, Connection con) {
            try {
                  if (null != res) {
                        res.close();
                  }
                  if (null != stmt) {
                        stmt.close();
                  }
                  if (null != con) {
                        con.close();
                  }

            } catch (SQLException e) {
                  // Loggers.general().info(LOG,"Connection Failed! Check output console");
                  e.printStackTrace();
            }

      }

      public static void surrenderDBConnection(Connection con, Statement stmt, ResultSet res) {
            try {
                  if (null != res) {
                        res.close();
                  }
                  if (null != stmt) {
                        stmt.close();
                  }
                  if (null != con) {
                        con.close();
                  }

            } catch (SQLException e) {
                  // Loggers.general().info(LOG,"Connection Failed! Check output console");
                  e.printStackTrace();
            }

      }

      public String gettheReference() {

            String theReference = getDriverWrapper().getEventFieldAsText("THE", "r", "");
            // //Loggers.general().info(LOG,"theReference " + gettheReference());
            return theReference;
      }

      public String getmasRefNo() {
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            // //Loggers.general().info(LOG,masRefNo);
            return masRefNo;
      }

      public String geteventRefNo() {
            String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
            return eventRefNo;
      }

      public String getStepPhaseCode() {
            String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
            return step_csm;
      }

      // Moses Daniel S 21-08-2021

      public String getGoodsDescription(String goodsCode) {
            String result = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGOODS> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGOODS", "GCODE='" + goodsCode + "'");

            EXTGOODS PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  result = PROPCode.getGdesp();
                  LOG.info("Goods Description for goods Code " + goodsCode + "-" + result);

            }
            return result;
      }

      public void getpenalRateFEC() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {
                  String masRefNo = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                  con = getConnection();
                  String query = "SELECT exte.PENALRAT FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT and exte.PENALRAT is NOT null AND mas.MASTER_REF = '"
                              + masRefNo + "'";
                  // //Loggers.general().info(LOG,"Payment value date query " +
                  // query);

                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        LOG.info("FEC collection Actual total penal rate" + query);
                  }
                  ps1 = con.prepareStatement(query);
                  rs1 = ps1.executeQuery();
                  if (rs1.next()) {
                        String penalRate = rs1.getString(1);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("FEC collection Actual final" + penalRate);
                        }
                        if (penalRate != null && penalRate.length() > 0) {
                              getPane().setPENALRAT(penalRate);
                        } else {
                              getPane().setPENALRAT("");
                        }

                  }
            }

            catch (Exception e) {

                  LOG.info("Exception FEC collection Actual final" + e.getMessage());

            } finally {
                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public int getpenalRate() {
            int flag = 0;
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general () .info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            if (getMajorCode().equalsIgnoreCase("FSA") || getMajorCode().equalsIgnoreCase("FEL")
                        || getMajorCode().equalsIgnoreCase("FOC")) {
                  try {
                        String penalR1 = getDriverWrapper().getEventFieldAsText("INAR", "s", "");// penalty rate
                        double penalD1 = 0;
                        double penalD2 = 0;
                        double penalD3 = 0;
                        double totalPenal = 0;
                        try {
                              penalD1 = Double.valueOf(penalR1);
                        } catch (Exception e) {
                              penalD1 = 0;
                              // Loggers.general () .info(LOG,"Exception Penal value 1" +
                              // e.getMessage());
                        }
                        String penalR2 = getDriverWrapper().getEventFieldAsText("INPA", "s", "");// actual penal rate
                        try {
                              penalD2 = Double.valueOf(penalR2);
                        } catch (Exception e) {
                              penalD2 = 0;
                              // Loggers.general () .info(LOG,"Exception Penal value 2" +
                              // e.getMessage());
                        }
                        String penalR3 = getDriverWrapper().getEventFieldAsText("INPR", "s", "");// rate code penal rate
                        try {
                              penalD3 = Double.valueOf(penalR3);
                        } catch (Exception e) {
                              penalD3 = 0;
                              // Loggers.general () .info(LOG,"Exception Penal value 3" +
                              // e.getMessage());
                        }

                        LOG.info("Penal1" + penalD1);
                        LOG.info("Penal2" + penalD2);
                        LOG.info("Penal3" + penalD3);

                        String pastDue = getDriverWrapper().getEventFieldAsText("B+PSE", "l", "").trim();
                        LOG.info("Past Due completed" + pastDue);

                        String todayDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "").trim();
                        LOG.info("Today system date===" + todayDate);

                        String dueDate = getDriverWrapper().getEventFieldAsText("B+PD", "d", "").trim();
                        LOG.info("Today Due date===" + dueDate);
                        int countDate = 0;

                        LOG.info("countDate===" + countDate);

                        /*
                         * try { Date start = new SimpleDateFormat("dd/mm/yy").parse(todayDate); Date
                         * end = new SimpleDateFormat("dd/mm/yy").parse(dueDate);
                         *
                         * Loggers.general () .info(LOG,"start date==>"+start); Loggers.general ()
                         * .info(LOG,"due date====>"+end);
                         *
                         * if(start.before(end)||start.equals(end)) { countDate=countDate+1;
                         * Loggers.general () .info(LOG,"date before or equals"+countDate);
                         *
                         * } else if(start.after(end)) { countDate=0; Loggers.general ()
                         * .info(LOG,"date after"+countDate); }
                         *
                         *
                         * } catch (ParseException e) { Loggers.general ()
                         * .info(LOG,"Exception in penal rate date comparison"+e.getMessage()); }
                         */
                        try {
                              LOG.info("Entering penalrate");
                              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                              Date date1 = sdf.parse(todayDate);
                              Date date2 = sdf.parse(dueDate);

                              LOG.info("start date==>" + date1);
                              LOG.info("due date====>" + date2);

                              if (date1.compareTo(date2) > 0) {
                                    countDate = countDate + 1;
                                    LOG.info("system Date is greater than due date" + countDate);

                              } else if (date1.compareTo(date2) < 0) {

                                    LOG.info("dueDate is greater than system date" + countDate);
                              } else if (date1.compareTo(date2) == 0) {
                                    countDate = countDate + 1;
                                    LOG.info("system date is equal to due date" + countDate);

                              }
                        } catch (Exception e) {
                              LOG.info("Exception in System date comparison" + e.getMessage());
                        }

                        if (getMajorCode().equalsIgnoreCase("FSA") && getMinorCode().equalsIgnoreCase("ASA")) {
                              if (pastDue.equalsIgnoreCase("Y") && (countDate == 1)) {
                                    totalPenal = penalD1;
                                    LOG.info("total penal inside if==>" + totalPenal);
                                    // totalPenal=totalPenal+penalD2+penalD3;
                                    // Loggers.general () .info(LOG,"Total penal after adding penal2"+totalPenal);
                              } else {
                                    totalPenal = penalD1 + penalD2 + penalD3;
                                    LOG.info("total penal inside else of pastdue==>" + totalPenal);
                              }
                        }

                        else {

                              totalPenal = penalD1 + penalD2 + penalD3;
                              LOG.info("total penal inside else==>" + totalPenal);
                        }

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Actual total Penal for finance===>" + totalPenal);
                        }

                        if (totalPenal > 0) {
                              String finval = String.format("%.2f", totalPenal);
                              String penalValue = String.valueOf(finval);
                              getPane().setPENALRAT(penalValue);
                              // added for comparion (error)_07-JUL-2020
                              String ActPenal1 = getDriverWrapper().getEventFieldAsText("cAPF", "s", "");
                              Double d = Double.parseDouble(ActPenal1);
                              LOG.info("ActPenal1===" + ActPenal1);
                              String ActPenal = String.format("%.2f", d);
                              LOG.info("ActPenal===" + ActPenal);

                              if ((penalValue != null && !penalValue.equalsIgnoreCase(""))
                                          && (!penalValue.contentEquals(ActPenal))) {
                                    flag = 1;
                                    LOG.info("flag===" + flag);

                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Actual total penal rate" + penalValue);
                                    LOG.info("penal rate====" + getPane().getPENALRAT());
                              }
                        } else {
                              getPane().setPENALRAT("");
                              LOG.info("penal rate else====" + getPane().getPENALRAT());

                        }

                  } catch (Exception e) {

                        LOG.info("Exception Penal rate" + e.getMessage());

                  }

            }
            return flag;
      }

      public void getrtgsNEFT() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {
                  String masRefNo = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                  String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");
                  con = getConnection();
                  /*
                   * String query =
                   * "select RTGSN,RTGNFT,NETSCF,PROREMT,PURCODE,BENTYP,BENACC,BENNAME,IFSCCO,BENBAK,BENBRN,BENCITY,UTRNO,RTGSNEFT,CCY_62,RTGSPART,BADDRE,NARRTVE,MASTER_REF from ETTV_RTGSNEFT_VIEW where MASTER_REF='"
                   * + masRefNo + "' and REFNO_PFIX='" + evnt + "' and REFNO_SERL='" + evvcount +
                   * "'";
                   */

                  String query = "select RTGSN,RTGNFT,NETSCF,PROREMT,PURCODE,BENTYP,BENACC,BENNAME,IFSCCO,BENBAK,BENBRN,BENCITY,UTRNO,case when c8ced=0 then rtgsneft when c8ced=2 then rtgsneft/power(10,2) when c8ced=3 then rtgsneft/power(10,3) end as RTGSNEFT,"
                              + "CCY_62,RTGSPART,BADDRE,NARRTVE,MASTER_REF from ETTV_RTGSNEFT_VIEW e,c8pf c "
                              + " where c.c8ccy=e.ccy_62 and MASTER_REF='" + masRefNo + "' and REFNO_PFIX='" + evnt
                              + "' and REFNO_SERL='" + evvcount + "'";

                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        LOG.info("RTGS/NEFT fields values in master" + query);
                  }
                  ps1 = con.prepareStatement(query);
                  rs1 = ps1.executeQuery();
                  if (rs1.next()) {
                        String RTGSN = rs1.getString(1).trim();
                        String RTGNFT = rs1.getString(2).trim();
                        String NETSCF = rs1.getString(3).trim();
                        String PROREMT = rs1.getString(4).trim();
                        String PURCODE = rs1.getString(5).trim();
                        String BENTYP = rs1.getString(6).trim();
                        String BENACC = rs1.getString(7).trim();
                        String BENNAME = rs1.getString(8).trim();
                        String IFSCCO = rs1.getString(9).trim();
                        String BENBAK = rs1.getString(10).trim();
                        String BENBRN = rs1.getString(11).trim();
                        String BENCITY = rs1.getString(12).trim();
                        String UTRNO = rs1.getString(13).trim();
                        String RTGSNEFT = rs1.getString(14).trim();
                        String CCY_62 = rs1.getString(15).trim();
                        String RTGSPART = rs1.getString(16).trim();
                        String BADDRE = rs1.getString(17).trim();
                        String NARRTVE = rs1.getString(18).trim();

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("RTGS/NEFT fields values RTGSN" + RTGSN);
                              LOG.info("RTGS/NEFT fields values RTGNFT" + RTGNFT);
                              LOG.info("RTGS/NEFT fields values NETSCF" + NETSCF);
                              LOG.info("RTGS/NEFT fields values PROREMT" + PROREMT);
                              LOG.info("RTGS/NEFT fields values PURCODE" + PURCODE);
                              LOG.info("RTGS/NEFT fields values BENTYP" + BENTYP);
                              LOG.info("RTGS/NEFT fields values BENACC" + BENACC);
                              LOG.info("RTGS/NEFT fields values BENNAME" + BENNAME);
                              LOG.info("RTGS/NEFT fields values IFSCCO" + IFSCCO);
                              LOG.info("RTGS/NEFT fields values BENBAK" + BENBAK);
                              LOG.info("RTGS/NEFT fields values BENBRN" + BENBRN);
                              LOG.info("RTGS/NEFT fields values BENCITY" + BENCITY);
                              LOG.info("RTGS/NEFT fields values UTRNO" + UTRNO);
                              LOG.info("RTGS/NEFT fields values RTGSNEFT" + RTGSNEFT);
                              LOG.info("RTGS/NEFT fields values CCY_62" + CCY_62);
                              LOG.info("RTGS/NEFT fields values RTGSPART" + RTGSPART);
                              LOG.info("RTGS/NEFT fields values BADDRE" + BADDRE);
                              LOG.info("RTGS/NEFT fields values NARRTVE" + NARRTVE);
                        }
                        getPane().setFINRTGS(true);

                        if (RTGSN.equalsIgnoreCase("Y") && RTGSN.length() > 0) {
                              getPane().setRTGSN(true);
                        }
                        if (!RTGNFT.equalsIgnoreCase("") && RTGNFT.length() > 0) {
                              getPane().setRTGNFT(RTGNFT);
                        }
                        if (!NETSCF.equalsIgnoreCase("") && NETSCF.length() > 0) {
                              getPane().setNETSCF(NETSCF);
                        }
                        if (!PROREMT.equalsIgnoreCase("") && PROREMT.length() > 0) {
                              getPane().setPROREMT(PROREMT);
                        }
                        if (!PURCODE.equalsIgnoreCase("") && PURCODE.length() > 0) {
                              getPane().setPURCODE(PURCODE);
                        }
                        if (!BENTYP.equalsIgnoreCase("") && BENTYP.length() > 0) {
                              getPane().setBENTYP(BENTYP);
                        }
                        if (!BENACC.equalsIgnoreCase("") && BENACC.length() > 0) {
                              getPane().setBENACC_Name(BENACC);
                        }
                        if (!BENNAME.equalsIgnoreCase("") && BENNAME.length() > 0) {
                              getPane().setBENNAME_Name(BENNAME);
                        }
                        if (!IFSCCO.equalsIgnoreCase("") && IFSCCO.length() > 0) {
                              getPane().setIFSCCO_Name(IFSCCO);
                        }
                        if (!BENBAK.equalsIgnoreCase("") && BENBAK.length() > 0) {
                              getPane().setBENBAK(BENBAK);
                        }
                        if (!BENBRN.equalsIgnoreCase("") && BENBRN.length() > 0) {
                              getPane().setBENBRN(BENBRN);
                        }
                        if (!BENCITY.equalsIgnoreCase("") && BENCITY.length() > 0) {
                              getPane().setBENCITY(BENCITY);
                        }
                        if (!UTRNO.equalsIgnoreCase("") && UTRNO.length() > 0) {
                              getPane().setUTRNO(UTRNO);
                        }
                        if (!RTGSNEFT.equalsIgnoreCase("") && RTGSNEFT.length() > 0 && CCY_62.length() > 1) {
                              getPane().setRTGSNEFT(RTGSNEFT + "" + CCY_62);
                        }
                        if (!RTGSPART.equalsIgnoreCase("") && RTGSPART.length() > 0) {
                              getPane().setRTGSPART(RTGSPART);
                        }
                        if (!BADDRE.equalsIgnoreCase("") && BADDRE.length() > 0) {
                              getPane().setBADDRE_Name(BADDRE);
                        }
                        if (!NARRTVE.equalsIgnoreCase("") && NARRTVE.length() > 0) {
                              getPane().setNARRTVE(NARRTVE);
                        }
                        getPane().setFINRTGS(true);
                  } else {

                        getPane().setFINRTGS(false);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("RTGS/NEFT fields values in else");
                        }
                  }

            } catch (Exception e) {

                  LOG.info("Exception RTGS/NEFT fields values" + e.getMessage());

            } finally {
                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void getrtgsNeftNet() {
            // Net posting amount popuklate for RTGS/NEFT
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            if (getMajorCode().equalsIgnoreCase("FSA")) {
                  String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String evntRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");

                  try {
                        String query = "SELECT * FROM (SELECT trim(MAS.MASTER_REF) AS MASTER_REF, trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0)) AS EVENT_REF, sum(case when prod.code in ('ELD','OCI') and pos.TRAN_CODE in (226) then 0 when POS.DR_CR_FLG = 'D' then (POS.AMOUNT /100)*-1 else (POS.AMOUNT/100) end) as AMOUNT, SUM(DECODE(POS.DR_CR_FLG, 'D', (POS.AMOUNT /100)*-1, (POS.AMOUNT/100))) AS AMOUNTOLD, SUM(DECODE(POS.DR_CR_FLG, 'D', (POS.AMOUNT) *-1, (POS.AMOUNT))) AS UPDAMOUNT, TRIM(POS.CCY) AS CURRENCY FROM MASTER MAS, BASEEVENT BEV, RELITEM REL, POSTING POS, PRODTYPE prod WHERE MAS.KEY97 = BEV.MASTER_KEY AND BEV.KEY97 = REL.EVENT_KEY AND REL.KEY97 = POS.KEY97 AND POS.POSTED_AS IS NULL AND POS.ACC_TYPE = 'RTGS' and MAS.PRODTYPE = PROD.KEY97 GROUP BY POS.CCY , trim(MAS.MASTER_REF), trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0)), TRIM(POS.CCY) UNION ALL SELECT trim(MAS.MASTER_REF) AS MASTER_REF, trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0)) EVENT_REF , sum(case when prod.code in ('ELD','OCI') and pos.TRAN_CODE in (226) then 0 when POS.DR_CR_FLG = 'D' then (POS.AMOUNT /100)*-1 else (POS.AMOUNT/100) end) as AMOUNT, SUM(DECODE(POS.DR_CR_FLG, 'D', (POS.AMOUNT /100)*-1, (POS.AMOUNT/100))) AS AMOUNTOLD, SUM(DECODE(POS.DR_CR_FLG, 'D', (POS.AMOUNT) *-1, (POS.AMOUNT))) AS UPDAMOUNT , TRIM(POS.CCY) AS CURRENCY FROM MASTER MAS, BASEEVENT BEV, BASEEVENT BEV1, MASTER MAS1, RELITEM REL, POSTING POS, PRODTYPE prod WHERE MAS.KEY97 = BEV.MASTER_KEY AND BEV.ATTACHD_EV = BEV1.KEY97 AND BEV1.MASTER_KEY = MAS1.KEY97 AND BEV1.KEY97 = REL.EVENT_KEY AND REL.KEY97 = POS.KEY97 AND POS.ACC_TYPE = 'RTGS' AND POS.POSTED_AS IS NULL and MAS.PRODTYPE = PROD.KEY97 GROUP BY trim(MAS.MASTER_REF), trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0)), TRIM(POS.CCY) ) A WHERE trim(A.MASTER_REF) = '"
                                    + masRefNo + "' AND trim(A.EVENT_REF) = '" + evntRefNo + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Net amount of RTGS/NEFT query" + query);
                        }
                        con = getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        if (rs1.next()) {

                              String rtgsAmt = rs1.getString(3);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Net amount of RTGS/NEFT value" + rtgsAmt);
                              }
                              if (rtgsAmt != null && rtgsAmt.length() > 0) {
                                    BigDecimal rtgsAmount = new BigDecimal(rtgsAmt);

                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String finalVal = diff.format(rtgsAmount);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Net amount of RTGS/NEFT value" + finalVal + " " + "INR");
                                    }
                                    getPane().setREALAMT(finalVal + " " + "INR");
                              } else {
                                    getPane().setREALAMT("");
                              }

                        } else {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Net amount of RTGS/NEFT query else");
                              }
                        }

                  } catch (Exception e) {
                        LOG.info("Exception Net amount of RTGS/NEFT in connection master" + e.getMessage());
                  } finally {
                        try {
                              if (rs1 != null)
                                    rs1.close();
                              if (ps1 != null)
                                    ps1.close();
                              if (con != null)
                                    con.close();

                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check
                              // output
                              // console");
                              e.printStackTrace();
                        }
                  }

            } else {

            }
      }

      public void getGoodsCategory() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
            String sql = "";
            try {
                  // RBI guidlines population
                  double unitdou = 0.00;
                  double ozkg = 35.27396195;
                  double grakg = 1000;
                  int kilokg = 1;
                  double caratKg = 5000;
                  double conunit = 0.00;
                  // weight
                  String gold = getDriverWrapper().getEventFieldAsText("PUO1", "s", ""); // IDC
                  String goldILC = getWrapper().getGOODCAT(); // ILC
                  String valunit = getWrapper().getWEIUNIT();
                  if (gold.equalsIgnoreCase("GOLD") && getMajorCode().equalsIgnoreCase("IDC")) {
                        // grams or weight
                        // unit
                        // Loggers.general().info(LOG,"getGoodsCategory selected");

                        try {

                              if (evnt.equalsIgnoreCase("CRE") && getMajorCode().equalsIgnoreCase("IDC")) {
                                    String unitval = getWrapper().getWEIGHT();
                                    if (unitval == null || unitval.equalsIgnoreCase("")) {

                                          unitval = "0";
                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("weight values fro IDC" + unitval);
                                    }
                                    unitdou = Double.valueOf(unitval);
                                    // //Loggers.general().info(LOG,"Weight values" + unitdou);

                                    if (gold.equalsIgnoreCase("GOLD")) {
                                          // //Loggers.general().info(LOG,"Gold is selected");

                                          if (valunit.equalsIgnoreCase("GRAM")) {
                                                conunit = unitdou / grakg;
                                                // Loggers.general().info(LOG,"setconunit GRAM" +
                                                // conunit);
                                                String setconunit = String.valueOf(conunit);
                                                // getWrapper().setCONWEG(setconunit);
                                                getPane().setCONWEG(setconunit);
                                          } else if (valunit.equalsIgnoreCase("OZ")) {
                                                conunit = unitdou / ozkg;
                                                LOG.info("before convert--->" + conunit);
                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);
                                                String setconunit = diff.format(conunit);
                                                // String setconunit = String.format("%.2f",
                                                // conunit);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("setconunit OZ " + setconunit);
                                                }
                                                // Loggers.general().info(LOG,"setconunit OZ " +
                                                // setconunit);
                                                // getWrapper().setCONWEG(setconunit);
                                                getPane().setCONWEG(setconunit);
                                          } else if (valunit.equalsIgnoreCase("KILO")) {
                                                conunit = unitdou / kilokg;
                                                String setconunit = String.valueOf(conunit);
                                                // Loggers.general().info(LOG,"setconunit KILO " +
                                                // setconunit);
                                                // getWrapper().setCONWEG(setconunit);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("setconunit KILO " + setconunit);
                                                }
                                                getPane().setCONWEG(setconunit);
                                          }

                                    }

                                    else {
                                          // //Loggers.general().info(LOG,"Not in Gold");
                                          String convertval = getWrapper().getCONWEG();
                                          convertval = "";
                                          getPane().setCONWEG(convertval);

                                    }

                              } else if (evnt.equalsIgnoreCase("AMD") && getMajorCode().equalsIgnoreCase("IDC")) {
                                    sql = "SELECT MAS.MASTER_REF, trim(USOP3.LONGNAME),BAS.REFNO_PFIX,BAS.REFNO_SERL FROM MASTER MAS, BASEEVENT BAS, EXTEVENT EXT, USEROPTN USOP3 WHERE MAS.KEY97 =BAS.MASTER_KEY AND BAS.KEY97=EXT.EVENT AND MAS.USEROPTN1=USOP3.KEY29 AND BAS.REFNO_PFIX='CRE' AND MAS.MASTER_REF ='"
                                                + masRefNo + "'";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Query value for get Goods Category Issue--->" + sql);
                                    }
                                    // Loggers.general().info(LOG,"Query value for get Goods
                                    // Category Issue--->" + sql);
                                    con = getConnection();
                                    ps = con.prepareStatement(sql);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String userCode = rs.getString(2).trim();
                                          // Loggers.general().info(LOG,"userCode value===>" +
                                          // userCode);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                LOG.info("userCode value===>" + userCode);
                                          }
                                          getPane().setGOODCAT(userCode);
                                    }
                                    String unitval = getWrapper().getWEIGHT();
                                    if (unitval == null || unitval.isEmpty()) {
                                          // //Loggers.general().info(LOG,"weight values" +
                                          // unitval);

                                          unitval = "0";
                                    }

                                    unitdou = Double.valueOf(unitval);
                                    // //Loggers.general().info(LOG,"Weight values" + unitdou);
                                    String goldStr = getWrapper().getGOODCAT();
                                    if (goldStr.equalsIgnoreCase("GOLD")) {
                                          // //Loggers.general().info(LOG,"Gold is selected");

                                          if (valunit.equalsIgnoreCase("GRAM")) {
                                                conunit = unitdou / grakg;
                                                // Loggers.general().info(LOG,"setconunit GRAM" +
                                                // conunit);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("setconunit GRAM" + conunit);
                                                }
                                                String setconunit = String.valueOf(conunit);
                                                // getWrapper().setCONWEG(setconunit);
                                                getPane().setCONWEG(setconunit);
                                          } else if (valunit.equalsIgnoreCase("OZ")) {
                                                conunit = unitdou / ozkg;
                                                // //Loggers.general().info(LOG,"before convert--->"
                                                // +
                                                // conunit);
                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);
                                                String setconunit = diff.format(conunit);
                                                // String setconunit = String.format("%.2f",
                                                // conunit);
                                                // Loggers.general().info(LOG,"setconunit OZ " +
                                                // setconunit);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("setconunit OZ " + setconunit);
                                                }
                                                // getWrapper().setCONWEG(setconunit);
                                                getPane().setCONWEG(setconunit);
                                          } else if (valunit.equalsIgnoreCase("KILO")) {
                                                conunit = unitdou / kilokg;
                                                String setconunit = String.valueOf(conunit);
                                                // Loggers.general().info(LOG,"setconunit KILO " +
                                                // setconunit);
                                                // getWrapper().setCONWEG(setconunit);
                                                getPane().setCONWEG(setconunit);
                                          }

                                    }

                                    else {
                                          // //Loggers.general().info(LOG,"Not in Gold");
                                          String convertval = getWrapper().getCONWEG();
                                          convertval = "";
                                          getPane().setCONWEG(convertval);

                                    }

                              } else if (getMinorCode().equalsIgnoreCase("CLP") && getMajorCode().equalsIgnoreCase("IDC")) {
                                    sql = "SELECT MAS.MASTER_REF, trim(USOP3.LONGNAME),BAS.REFNO_PFIX,BAS.REFNO_SERL FROM MASTER MAS, BASEEVENT BAS, EXTEVENT EXT, USEROPTN USOP3 WHERE MAS.KEY97 =BAS.MASTER_KEY AND BAS.KEY97=EXT.EVENT AND MAS.USEROPTN1=USOP3.KEY29 AND BAS.REFNO_PFIX IN ('CRE','AMD') AND MAS.MASTER_REF ='"
                                                + masRefNo + "'";
                                    // Loggers.general().info(LOG,"Query value for get Goods
                                    // Category amend--->" + sql);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Query value for get Goods Category amend--->" + sql);
                                    }
                                    con = getConnection();
                                    ps = con.prepareStatement(sql);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String userCode = rs.getString(2).trim();
                                          // Loggers.general().info(LOG,"userCode value===>" +
                                          // userCode);
                                          getPane().setGOODCAT(userCode);

                                    }
                                    String unitval = getWrapper().getWEIGHT();
                                    if (unitval == null || unitval.isEmpty()) {
                                          // //Loggers.general().info(LOG,"weight values" +
                                          // unitval);

                                          unitval = "0";
                                    }

                                    unitdou = Double.valueOf(unitval);
                                    // //Loggers.general().info(LOG,"Weight values" + unitdou);
                                    if (gold.equalsIgnoreCase("GOLD")) {
                                          // //Loggers.general().info(LOG,"Gold is selected");

                                          if (valunit.equalsIgnoreCase("GRAM")) {
                                                conunit = unitdou / grakg;
                                                // Loggers.general().info(LOG,"setconunit GRAM" +
                                                // conunit);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("setconunit GRAM" + conunit);
                                                }
                                                String setconunit = String.valueOf(conunit);
                                                // getWrapper().setCONWEG(setconunit);
                                                getPane().setCONWEG(setconunit);
                                          } else if (valunit.equalsIgnoreCase("OZ")) {
                                                conunit = unitdou / ozkg;
                                                // //Loggers.general().info(LOG,"before convert--->"
                                                // +
                                                // conunit);
                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);
                                                String setconunit = diff.format(conunit);
                                                // String setconunit = String.format("%.2f",
                                                // conunit);
                                                // Loggers.general().info(LOG,"setconunit OZ " +
                                                // setconunit);
                                                // getWrapper().setCONWEG(setconunit);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("setconunit OZ " + setconunit);
                                                }
                                                getPane().setCONWEG(setconunit);
                                          } else if (valunit.equalsIgnoreCase("KILO")) {
                                                conunit = unitdou / kilokg;
                                                String setconunit = String.valueOf(conunit);
                                                // Loggers.general().info(LOG,"setconunit KILO " +
                                                // setconunit);
                                                // getWrapper().setCONWEG(setconunit);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("setconunit KILO " + setconunit);
                                                }
                                                getPane().setCONWEG(setconunit);
                                          }

                                    }

                                    else {
                                          // //Loggers.general().info(LOG,"Not in Gold");
                                          String convertval = getWrapper().getCONWEG();
                                          convertval = "";
                                          getPane().setCONWEG(convertval);

                                    }
                              }

                        } catch (Exception e)

                        {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Exception for Gold weight" + e.getMessage());
                              }

                        } finally

                        {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    //// Loggers.general().info(LOG,"Connection Failed! Check
                                    //// output
                                    //// console");
                                    e.printStackTrace();
                              }
                        }
                  }

                  else if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("CRC")
                              && goldILC.equalsIgnoreCase("GOLD")) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Goods value calculated in ILC CRC event");
                        }

                        String unitval = getWrapper().getWEIGHT();
                        if (unitval == null || unitval.isEmpty()) {
                              // //Loggers.general().info(LOG,"weight values" + unitval);

                              unitval = "0";
                        }

                        unitdou = Double.valueOf(unitval);
                        // //Loggers.general().info(LOG,"Weight values" + unitdou);

                        if (goldILC.equalsIgnoreCase("GOLD")) {
                              // //Loggers.general().info(LOG,"Gold is selected");

                              if (valunit.equalsIgnoreCase("GRAM")) {
                                    conunit = unitdou / grakg;
                                    // Loggers.general().info(LOG,"setconunit GRAM" + conunit);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("setconunit GRAM" + conunit);
                                    }

                                    String setconunit = String.valueOf(conunit);
                                    // getWrapper().setCONWEG(setconunit);
                                    getPane().setCONWEG(setconunit);
                              } else if (valunit.equalsIgnoreCase("OZ")) {
                                    conunit = unitdou / ozkg;
                                    // //Loggers.general().info(LOG,"before convert--->" +
                                    // conunit);
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String setconunit = diff.format(conunit);
                                    // String setconunit = String.format("%.2f", conunit);
                                    // Loggers.general().info(LOG,"setconunit OZ " +
                                    // setconunit);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("setconunit OZ " + setconunit);
                                    }
                                    // getWrapper().setCONWEG(setconunit);
                                    getPane().setCONWEG(setconunit);
                              } else if (valunit.equalsIgnoreCase("KILO")) {
                                    conunit = unitdou / kilokg;
                                    String setconunit = String.valueOf(conunit);
                                    // Loggers.general().info(LOG,"setconunit KILO " +
                                    // setconunit);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("setconunit KILO " + setconunit);
                                    }
                                    // getWrapper().setCONWEG(setconunit);
                                    getPane().setCONWEG(setconunit);
                              }

                        }

                        else {
                              // //Loggers.general().info(LOG,"Not in Gold");
                              String convertval = getWrapper().getCONWEG();
                              convertval = "";
                              getPane().setCONWEG(convertval);

                        }
                  } else {
                        String convertval = getWrapper().getCONWEG();
                        convertval = "";
                        getPane().setCONWEG(convertval);
                  }

            } catch (Exception e)

            {

                  LOG.info("Exception for Gold Category" + e.getMessage());

            }

      }

      public void getChargeBasis() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            // todo after days changed to 10
            String strLogdays = "CHARGEDAYS";
            String days = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog1 = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLogdays + "'");
            EXTGENCUSTPROP CodeLog1 = queryLog1.getUnique();
            if (CodeLog1 != null) {

                  days = CodeLog1.getPropval();
                  LOG.info("days in extgencust" + days);
            } else {
                  LOG.info("days in extgencust in else-------->");

            }

            LOG.info("days in extgencust======>" + days);

            // -------------------------------------------

            String Sight_Payment = getDriverWrapper().getEventFieldAsText("AVBY", "s", "");
            String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
            // Loggers.general().info(LOG,"Additional expiry date initial" + expiryDate);

            if (dailyval_Log.equalsIgnoreCase("YES")) {

                  LOG.info("Payment type" + Sight_Payment);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            Calendar c = Calendar.getInstance();
            if (Sight_Payment.equalsIgnoreCase("Sight Payment") || Sight_Payment.equalsIgnoreCase("Mixed Payment")) {

                  int gra = 0;
                  int addVal = 1;
                  // String days = "15";
                  try {
                        String graceday = getWrapper().getGRACEPER();
                        // Loggers.general().info(LOG,"Additional expiry days" + graceday);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Additional expiry days" + graceday);
                        }

                        if (graceday.equalsIgnoreCase("") || graceday.equalsIgnoreCase("0")) {
                              // getPane().setGRACEPER(days);
                              String grace = getWrapper().getGRACEPER();
                              gra = Integer.parseInt(grace);
                              // Loggers.general().info(LOG,"Additional expiry days in if loop" +
                              // grace);
                        } else if (!graceday.equalsIgnoreCase("") && !graceday.equalsIgnoreCase("0")) {
                              String grace = getWrapper().getGRACEPER();
                              // Loggers.general().info(LOG,"Additional expiry days in else loop"
                              // +
                              // grace);
                              gra = Integer.parseInt(grace);
                        }
                        int graceVal = gra + addVal;
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Total Additional grace days" + graceVal);
                        }
                        c.setTime(sdf.parse(expiryDate));
                        c.add(Calendar.DATE, graceVal);
                        // //Loggers.general().info(LOG,"DATE 1"+ c);
                        String output = sdf.format(c.getTime());
                        // Loggers.general().info(LOG,"Additional expiry date" + output);
                        getPane().setCHAENDED(output);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exeception" + e);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Exeception Additional expiry days" + e.getMessage());
                        }
                  }
            } else {

                  String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                  String tenorDays = getDriverWrapper().getEventFieldAsText("TNRD", "i", "");
                  int tenor = Integer.parseInt(tenorDays);
                  try {
                        c.setTime(sdf.parse(issueDate));
                        c.add(Calendar.DATE, tenor);
                        String output = sdf.format(c.getTime());
                        getPane().setCHAENDED(output);
                        // getPane().setGRACEPER("");
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Exeception chargeDate in else" + e.getMessage());
                        }
                  }

            }

      }

      public void getnetAmount() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {

                  String Advamt = getDriverWrapper().getEventFieldAsText("cARK", "v", "m");
                  String Advcur = getDriverWrapper().getEventFieldAsText("cARK", "v", "c");
                  String amountcur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                  String amountCol = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                  // //Loggers.general().info(LOG,"advance amount --->" + Advamt + ""
                  // +
                  // amountcur);
                  LOG.info("advamt" + Advamt);
                  LOG.info("Advcur" + Advcur);
                  LOG.info("amountcur " + amountcur);
                  LOG.info("amountCol" + amountCol);

                  BigDecimal amountCollection = new BigDecimal(0);
                  if (getMajorCode().equalsIgnoreCase("ODC")) {

                        try {
                              amountCollection = new BigDecimal(amountCol);
                              LOG.info("amount collection" + amountCollection);
                        } catch (Exception e) {
                              amountCollection = new BigDecimal(0);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Exception Initial Master amount Collection" + e.getMessage());

                              }
                        }

                  } else if (getMajorCode().equalsIgnoreCase("ELC")) {

                        double addAmountDub = 0;
                        try {
                              String addAmount = getDriverWrapper().getEventFieldAsText("AAC", "v", "m");

                              try {
                                    addAmountDub = Double.valueOf(addAmount);

                              } catch (Exception e) {
                                    addAmountDub = 0;

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,
                                                      "Exception Initial Master addtional amount ELC" + e.getMessage());

                                    }
                              }

                              if (addAmountDub > 0) {
                                    String claimAmount = getDriverWrapper().getEventFieldAsText("AMC", "v", "m");

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Master amount and addtional amount ELC" + claimAmount);

                                    }
                                    amountCollection = new BigDecimal(claimAmount);
                              }

                              else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Master amount ELC" + amountCol);

                                    }
                                    amountCollection = new BigDecimal(amountCol);
                              }
                        } catch (Exception e) {
                              amountCollection = new BigDecimal(0);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Exception Initial Master amount ELC" + e.getMessage());

                              }
                        }

                  }

                  BigDecimal advanceAmt = new BigDecimal(0);
                  try {
                        advanceAmt = new BigDecimal(Advamt);
                        LOG.info("advance amt" + advanceAmt);
                  } catch (Exception e) {
                        advanceAmt = new BigDecimal(0);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Advance amount Collection" + e.getMessage());

                        }
                  }

                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        LOG.info("Master amount Collection " + amountCollection);
                        LOG.info("advanced amount Collection " + advanceAmt);

                  }

                  double advadob = 0.0;
                  try {

                        advadob = Double.valueOf(Advamt);
                        LOG.info("advadob" + advadob);
                        // //Loggers.general().info(LOG,"tendays in try--->" + tenddou);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Advance ant in catch" +
                        // e.getMessage());
                  }

                  if (advadob > 0) {

                        double notional = 1;
                        BigDecimal notionalBig = new BigDecimal(1);
                        BigDecimal totalAmount = new BigDecimal(0);
                        String finalAmount = "0";
                        try {
                              con = getConnection();
                              String query = "SELECT ETT_SPOTRATE_CAL('" + amountcur + "','" + Advcur + "') FROM DUAL";
                              // Loggers.general().info(LOG,"Notional rate function " +
                              // query);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("advanced amount Collection Notional rate function " + query);

                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              if (rs1.next()) {
                                    notional = rs1.getDouble(1);
                                    notionalBig = new BigDecimal(notional);
                                    BigDecimal totaladvamt = notionalBig.multiply(advanceAmt);
                                    // double totalamt = notional * advadob;
                                    // Loggers.general().info(LOG,"Notional rate totalamount
                                    // " +
                                    // totalamount + "notional" + notional);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Notional rate total advance amount " + totaladvamt + "Notional rate"
                                                      + notionalBig);

                                    }

                                    if (amountcur.equalsIgnoreCase("BHD") || amountcur.equalsIgnoreCase("KWD")
                                                || amountcur.equalsIgnoreCase("OMR")) {
                                          totalAmount = amountCollection.subtract(totaladvamt);
                                          DecimalFormat diff = new DecimalFormat("0.000");
                                          diff.setMaximumFractionDigits(2);
                                          finalAmount = diff.format(totalAmount);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                LOG.info("Final Amount Collection 3 digit" + finalAmount);

                                          }

                                    } else if (amountcur.equalsIgnoreCase("JPY") || amountcur.equalsIgnoreCase("KRW")) {

                                          totalAmount = amountCollection.subtract(totaladvamt);
                                          DecimalFormat diff = new DecimalFormat("0.0");
                                          diff.setMaximumFractionDigits(2);
                                          finalAmount = diff.format(totalAmount);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                LOG.info("Final Amount Collection 1 digit" + finalAmount);

                                          }
                                    } else {
                                          totalAmount = amountCollection.subtract(totaladvamt);
                                          DecimalFormat diff = new DecimalFormat("0.00");
                                          diff.setMaximumFractionDigits(2);
                                          finalAmount = diff.format(totalAmount);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                LOG.info("Final Amount Collection 2 digit" + finalAmount);

                                          }
                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Final Amount Collection===>" + finalAmount);

                                    }

                                    getPane().setNETRECIV(finalAmount + " " + amountcur);
                              }
                        } catch (Exception e) {
                              // // Loggers.general().info(LOG,"Exception Notional rate
                              // // function" + e1.getMessage());
                              // }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Exception Notional rate Amount to be collect==>" + e.getMessage());

                              }

                        }
                  } else {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,
                                          "Final Amount to be collect in else part ---->" + amountCollection + " " + amountcur);

                        }

                        getPane().setNETRECIV(amountCollection + " " + amountcur);
                  }
            } catch (Exception e) {

                  LOG.info("Exception Amount to be collect---->" + e.getMessage());

            }

      }

      // ----------------------------ODC Payment----

      public void getnetAmountOdcPay() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {

                  String Advamt = getDriverWrapper().getEventFieldAsText("cARK", "v", "m");
                  String Advcur = getDriverWrapper().getEventFieldAsText("cARK", "v", "c");
                  String amountcur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                  String amountCol = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                  // //Loggers.general().info(LOG,"advance amount --->" + Advamt + ""
                  // +
                  // amountcur);
                  LOG.info("advamt=====" + Advamt);
                  LOG.info("Advcur=======" + Advcur);
                  LOG.info("amountcur ========" + amountcur);
                  LOG.info("amountCol============" + amountCol);

                  BigDecimal amountCollection = new BigDecimal(0);
                  if (getMajorCode().equalsIgnoreCase("ODC")) {

                        try {
                              amountCollection = new BigDecimal(amountCol);
                              LOG.info("amount collection===================>" + amountCollection);
                        } catch (Exception e) {
                              amountCollection = new BigDecimal(0);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,
                                                "Exception Initial Master amount Collection=======================>" + e.getMessage());

                              }
                        }

                  } else if (getMajorCode().equalsIgnoreCase("ELC")) {

                        double addAmountDub = 0;
                        try {
                              String addAmount = getDriverWrapper().getEventFieldAsText("AAC", "v", "m");
                              LOG.info("Addamount===>" + addAmount);

                              try {
                                    addAmountDub = Double.valueOf(addAmount);
                                    LOG.info("addAmountDub===>" + addAmountDub);

                              } catch (Exception e) {
                                    addAmountDub = 0;

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,
                                                      "Exception Initial Master addtional amount ELC" + e.getMessage());

                                    }
                              }

                              if (addAmountDub > 0) {
                                    String claimAmount = getDriverWrapper().getEventFieldAsText("AMC", "v", "m");
                                    LOG.info("claimAmount===>" + claimAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Master amount and addtional amount ELC" + claimAmount);

                                    }
                                    amountCollection = new BigDecimal(claimAmount);
                                    LOG.info("amountCollection===>" + amountCollection);
                              }

                              else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Master amount ELC" + amountCol);

                                    }
                                    amountCollection = new BigDecimal(amountCol);
                              }
                        } catch (Exception e) {
                              amountCollection = new BigDecimal(0);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Exception Initial Master amount ELC" + e.getMessage());

                              }
                        }

                  }

                  BigDecimal advanceAmt = new BigDecimal(0);
                  try {
                        advanceAmt = new BigDecimal(Advamt);
                        LOG.info("advance amt==============" + advanceAmt);
                  } catch (Exception e) {
                        advanceAmt = new BigDecimal(0);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Advance amount Collection===========" + e.getMessage());

                        }
                  }

                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        LOG.info("Master amount Collection============== " + amountCollection);
                        LOG.info("advanced amount Collection================== " + advanceAmt);

                  }

                  double advadob = 0.0;
                  try {

                        advadob = Double.valueOf(Advamt);
                        LOG.info("advadob===================" + advadob);
                        // //Loggers.general().info(LOG,"tendays in try--->" + tenddou);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Advance ant in catch" +
                        // e.getMessage());
                  }

                  if (advadob > 0) {

                        double notional = 1;
                        BigDecimal notionalBig = new BigDecimal(1);
                        BigDecimal totalAmount = new BigDecimal(0);
                        String finalAmount = "0";
                        try {
                              con = getConnection();
                              String query = "SELECT ETT_SPOTRATE_CAL('" + amountcur + "','" + Advcur + "') FROM DUAL";
                              // Loggers.general().info(LOG,"Notional rate function " +
                              // query);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,
                                                "advanced amount Collection Notional rate function============== " + query);

                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              if (rs1.next()) {
                                    notional = rs1.getDouble(1);
                                    notionalBig = new BigDecimal(notional);
                                    BigDecimal totaladvamt = amountCollection.subtract(notionalBig.multiply(advanceAmt));
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    finalAmount = diff.format(totaladvamt);
                                    // double totalamt = notional * advadob;
                                    // Loggers.general().info(LOG,"Notional rate totalamount
                                    // " +
                                    // totalamount + "notional" + notional);
                                    LOG.info("totaladvamt" + totaladvamt);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Notional rate total advance amount======= " + finalAmount + "Notional rate"
                                                      + notionalBig);

                                    }

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Final Amount Collection===================>" + finalAmount);

                                    }

                                    getPane().setNETRECIV(finalAmount + " " + amountcur);
                                    LOG.info("Amount to be collected======" + getPane().getNETRECIV());
                              }
                        } catch (Exception e) {
                              // // Loggers.general().info(LOG,"Exception Notional rate
                              // // function" + e1.getMessage());
                              // }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Exception Notional rate Amount to be collect==>" + e.getMessage());

                              }

                        }
                  } else {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,
                                          "Final Amount to be collect in else part ---->" + amountCollection + " " + amountcur);

                        }

                        getPane().setNETRECIV(amountCollection + " " + amountcur);
                  }
            } catch (Exception e) {

                  LOG.info("Exception Amount to be collect---=====->" + e.getMessage());

            }

      }

      // -----------------------------End of odc payment---

      // public void getChargeGur() {
      // String strLog = "Log";
      // String dailyval_Log = "";
      // @SuppressWarnings("unchecked")
      // AdhocQuery<? extends
      // com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
      // getDriverWrapper()
      // .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
      // EXTGENCUSTPROP CodeLog = queryLog.getUnique();
      // if (CodeLog != null) {
      //
      // dailyval_Log = CodeLog.getPropval();
      // } else {
      // // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
      //
      // }
      // try {
      //
      // int garceInt = 0;
      // try {
      //
      // String graceda = getWrapper().getGRACEPER();
      //
      // garceInt = Integer.parseInt(graceda);
      // } catch (Exception e) {
      // garceInt = 0;
      //
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Exception in Grace days--->" + e.getMessage());
      // }
      // }
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"IGT grace days---->" + garceInt);
      // }
      // String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
      // // 12/10/16
      // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
      // Calendar c = Calendar.getInstance();
      // int gra = 1 + garceInt;
      // if ((getMajorCode().equalsIgnoreCase("IGT") &&
      // getMinorCode().equalsIgnoreCase("IIG"))) {
      // // String expdate = getWrapper().getCLIMEXPD();
      //
      // // //Loggers.general().info(LOG,"expdate in issue-------> " + expdate);
      //
      // try {
      //
      // c.setTime(sdf.parse(expdate));
      // // //Loggers.general().info(LOG,"expdate in issue-------> ");
      // c.add(Calendar.DATE, gra);
      // // //Loggers.general().info(LOG,"DATE 1"+ c);
      // String output = sdf.format(c.getTime());
      // // //Loggers.general().info(LOG,output);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Charge end date IGT---->" + output);
      // }
      // getPane().setCHAENDED(output);
      //
      // } catch (Exception e) {
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      //
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"IGT Charge end date---->" + e.getMessage());
      // }
      // }
      // }
      // } else {
      //
      // // //Loggers.general().info(LOG,"expdate in issue-------> " + expdate);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"New claim date IGT--->" + expdate);
      // }
      //
      // try {
      //
      // c.setTime(sdf.parse(expdate));
      // // //Loggers.general().info(LOG,"expdate in issue-------> ");
      // c.add(Calendar.DATE, gra);
      // // //Loggers.general().info(LOG,"DATE 1"+ c);
      // String output = sdf.format(c.getTime());
      // // //Loggers.general().info(LOG,output);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"New claim end date---->" + output);
      // }
      // getPane().setCHAENDED(output);
      //
      // } catch (Exception e) {
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      //
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Exeception New Charge end date---->" +
      // e.getMessage());
      // }
      // }
      // }
      // }
      // } catch (Exception e) {
      // Loggers.general().info(LOG,"Exeception in charge end date" + e.getMessage());
      //
      // }
      // }

      // Todo on Sep 18 Statutory Claim Expiry Date
      /*
       * public void getStatutoryChargeGur() { String strLog = "Log"; String
       * dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { // Loggers.general().info(LOG,
       * "ADDDailyTxnLimit is empty-------->");
       *
       * } try {
       *
       * int garceInt = 0;
       *
       * if (getMajorCode().equalsIgnoreCase("IGT") &&
       * (getMinorCode().equalsIgnoreCase("IIG") ||
       * getMinorCode().equalsIgnoreCase("NAIG") ||
       * getMinorCode().equalsIgnoreCase("NJIG"))) {
       *
       * String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
       * Loggers.general().info(LOG,"expiry date"+expdate);
       *
       * String claimdate = getDriverWrapper().getEventFieldAsText("cAIT", "d", "");
       * Loggers.general().info(LOG,"claim expiry date"+claimdate);
       *
       * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); Calendar c =
       * Calendar.getInstance();
       *
       * String type = getDriverWrapper().getEventFieldAsText("PUL2", "s", "");
       * Loggers.general().info(LOG,"type of stat"+type);
       *
       * if (expdate != null && !expdate.equalsIgnoreCase("")) {
       *
       * if(type!=null &&!type.equalsIgnoreCase("")) {
       * if(type.equalsIgnoreCase("Inland")){
       *
       * try {
       *
       * c.setTime(sdf.parse(expdate));
       *
       * c.add(Calendar.YEAR, 1);
       *
       * String output = sdf.format(c.getTime()); Loggers.general().info(LOG,"output"
       * + output);
       *
       * if (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * " statutory claim expiry date---->" + output); }
       *
       * getPane().setSTACLAIM(output);
       * Loggers.general().info(LOG,"output"+getPane().getSTACLAIM()); }
       * catch(Exception e)
       *
       * { Loggers.general().info(LOG,"Exception in inland"+e.getMessage()); } } else
       * { getPane().setSTACLAIM(claimdate);
       * Loggers.general().info(LOG,"output in else" +getPane().getSTACLAIM()); } }
       * String stadate = getWrapper().getSTACLAIM(); }
       * Loggers.general().info(LOG,"stadate" + stadate); if (stadate != null &&
       * !stadate.equalsIgnoreCase("")&&getMinorCode().equalsIgnoreCase("IIG")) { if
       * (stadate.equals(output)) {
       *
       * getPane().setSTACLAIM(output); Loggers.general().info(LOG,"STACLAIM output" +
       * getPane().getSTACLAIM()); } else {
       *
       * getPane().setSTACLAIM(stadate); Loggers.general().info(LOG,"STACLAIM stadate"
       * + getPane().getSTACLAIM()); } } else {
       *
       * getPane().setSTACLAIM(output); Loggers.general().info(LOG,"STACLAIM" +
       * getPane().getSTACLAIM()); }
       *
       * String tempsta=getPane().getSTACLAIM(); Loggers.general().info(LOG,
       * "Statutory Temp"+tempsta);
       *
       * try {
       *
       * int statInt = 0;
       *
       * int garceday = 0; try {
       *
       * String graceda = getWrapper().getGRACEPER();
       *
       * garceday = Integer.parseInt(graceda); } catch (Exception e) { garceday = 0;
       *
       * }
       *
       * // 12/10/16
       *
       * if (type.equalsIgnoreCase("Inland")) {
       *
       * try { statInt = 1; // If INL adding 1 day + 365 // with expiry date
       * c.setTime(sdf.parse(tempsta)); // //Loggers.general().info(LOG,"expdate in //
       * issue-------> "); c.add(Calendar.DATE, statInt); // //LOG.info("DATE 1"+ c);
       * String outputVal = sdf.format(c.getTime()); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * LOG.info("Charge end date IGT for Inland---->" + outputVal); }
       * getPane().setCHAENDED(outputVal);
       *
       * } catch (Exception e) { if (dailyval_Log.equalsIgnoreCase("YES")) {
       *
       * if (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * "IGT Charge end date for Inland---->" + e.getMessage()); } } }
       *
       * } else {
       *
       * try { statInt = 1 + garceday; // If For adding 1 // day + grace date // with
       * expiry date c.setTime(sdf.parse(expdate)); // //Loggers.general().info(LOG,
       * "expdate in // issue-------> "); c.add(Calendar.DATE, statInt); //
       * //Loggers.general().info(LOG,"DATE 1"+ c); String outputVal =
       * sdf.format(c.getTime()); // //Loggers.general().info(LOG,output); if
       * (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * "Charge end date IGT for foreign---->" + outputVal); }
       * getPane().setCHAENDED(outputVal);
       *
       * } catch (Exception e) { if (dailyval_Log.equalsIgnoreCase("YES")) {
       *
       * if (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * "IGT Charge end date for foreign---->" + e.getMessage()); } } }
       *
       * }
       *
       *
       *
       * }catch (Exception e) { if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Exception charge end date value population--->" +
       * e.getMessage()); } }
       *
       * } } }catch (Exception e) {
       *
       * if (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * " Exception statutory claim expiry date---->" + e.getMessage()); }
       *
       * } }
       */

      /*
       * catch (Exception e) { Loggers.general().info(LOG,
       * "Exeception in statutory claim expiry date---->" + e.getMessage());
       *
       * } }
       */
      public void getStatutoryChargeGur() {
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            // String tempexpdate = getPane().getTEMEXPIR().trim();
            // LOG.info(" temperovary Expiry date in statutory claim" + tempexpdate);
//          LOG.info("length" + tempexpdate.length());

            String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
            LOG.info("Expiry date in statutory claim" + expdate);

            String tempcldate = getPane().getCLIMEXPD();
            LOG.info("Pane claim expiry date" + tempcldate);

            String type = getDriverWrapper().getEventFieldAsText("PUL2", "s", ""); // 12/10/16
            LOG.info("Inland/Foreign Type in statutory claim" + type);

            String tempstatdate = getPane().getSTACLAIM();
            LOG.info("statuatory date ======= 1" + tempstatdate);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            Calendar c = Calendar.getInstance();

//          if ((tempexpdate != null) && (!tempexpdate.equalsIgnoreCase("")) && (!tempexpdate.equalsIgnoreCase(expdate))) {
//                getPane().setSTACLAIM("");
//                LOG.info("Clearing staclaim" + getPane().getSTACLAIM());
//                tempstatdate = "";
//                LOG.info("Clearing tempstadate" + tempstatdate);
//          }

            try {
                  if (getMajorCode().equalsIgnoreCase("IGT")) {
                        if (expdate != null && !expdate.equalsIgnoreCase("")) {

                              if (type != null && !type.equalsIgnoreCase("")) {

                                    if (type.equalsIgnoreCase("Inland")) {

                                          LOG.info("Inland=========>" + type);
                                          if ((tempstatdate == null) || (tempstatdate.equalsIgnoreCase(""))) {

                                                c.setTime(sdf.parse(expdate));
                                                c.add(Calendar.YEAR, 1);
                                                String outputdate = sdf.format(c.getTime());
                                                LOG.info("outputdate" + outputdate);
                                                getPane().setSTACLAIM(outputdate);
                                                Loggers.general().info(LOG,
                                                            "statutory claim expiry date inland in if" + getPane().getSTACLAIM());
                                          } else if ((tempstatdate != null) && (!tempstatdate.equalsIgnoreCase(""))) {
                                                LOG.info("tempstatdate" + tempstatdate);
                                                getPane().setSTACLAIM(tempstatdate);
                                                Loggers.general().info(LOG,
                                                            "statutory claim expiry date inland in else if" + getPane().getSTACLAIM());
                                          }

                                    }

                                    else if (type.equalsIgnoreCase("Foreign")) {
                                          LOG.info("Foreign=========>" + type);
                                          if ((tempstatdate == null) || (tempstatdate.equalsIgnoreCase(""))) {
                                                LOG.info("tempcldate" + tempcldate);
                                                getPane().setSTACLAIM(tempcldate);
                                                Loggers.general().info(LOG,
                                                            "statutory claim expiry date foreign in if" + getPane().getSTACLAIM());
                                          } else if ((tempstatdate != null) && (!tempstatdate.equalsIgnoreCase(""))) {
                                                LOG.info("tempstatdate" + tempstatdate);
                                                getPane().setSTACLAIM(tempstatdate);
                                                Loggers.general().info(LOG,
                                                            "statutory claim expiry date foreign in else if" + getPane().getSTACLAIM());
                                          }
                                    }
                              }
                        }

                  }
            } catch (Exception e) {
                  LOG.info("Exception in statutory Claim expiry date" + e.getMessage());
            }

            try {
                  String statdate = getPane().getSTACLAIM();
                  int gradate = 1;

                  String type1 = getDriverWrapper().getEventFieldAsText("PUL2", "s", ""); // 12/10/16
                  LOG.info("Inland/Foreign in statutory claim in second try" + type1);

                  String newclmdate = getPane().getNEWCLMDT();
                  LOG.info("New claim expiry date" + newclmdate);

                  LOG.info("statutory claim in second try" + statdate);

                  if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("IIG"))) {

                        try {

                              c.setTime(sdf.parse(statdate));

                              c.add(Calendar.DATE, gradate);

                              String outputVal = sdf.format(c.getTime());
                              LOG.info(outputVal);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Charge end date IGT for foreign and inland---->" + outputVal);
                              }
                              getPane().setCHAENDED(outputVal);
                              LOG.info("outputVal" + outputVal);

                        } catch (Exception e) {
                              LOG.info("exception in date second catch" + e.getMessage());

                        }

                  }

                  else {

                        if (type1.equalsIgnoreCase("Foreign")) {

                              try {

                                    c.setTime(sdf.parse(newclmdate));

                                    c.add(Calendar.DATE, gradate);

                                    String outputVal = sdf.format(c.getTime());
                                    LOG.info(outputVal);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Charge end date IGT for foreign and inland for amend adjust---->" + outputVal);
                                    }
                                    getPane().setCHAENDED(outputVal);
                                    LOG.info("outputVal" + outputVal);

                              } catch (Exception e) {
                                    LOG.info("exception in date second catch amend adjust" + e.getMessage());

                              }
                        }

                        else {

                              try {

                                    c.setTime(sdf.parse(statdate));

                                    c.add(Calendar.DATE, gradate);

                                    String outputVal = sdf.format(c.getTime());
                                    LOG.info(outputVal);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Charge end date IGT for foreign and inland for adj amd---->" + outputVal);
                                    }
                                    getPane().setCHAENDED(outputVal);
                                    LOG.info("outputVal" + outputVal);

                              } catch (Exception e) {
                                    LOG.info("exception in date second catch" + e.getMessage());

                              }

                        }
                  }

            } catch (Exception e) {
                  LOG.info("exception in second catch charge basis end date" + e.getMessage());
            }
            /*
             * getPane().setTEMEXPIR(expdate);
             * Loggers.general().info(LOG,"Temperavory Expiry date"+tempexpdate);
             */
            // LOG.info("Temperavory expiry =========>" + getPane().getTEMEXPIR());

      }

      /*
       * public void getStatutoryChargeGur() { String strLog = "Log"; String
       * dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { //
       * Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
       *
       * } String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); //
       * 12/10/16 Loggers.general().info(LOG,"Expiry date in statutory claim" +
       * expdate); String tempcldate = getPane().getCLIMEXPD();
       * Loggers.general().info(LOG,"Pane claim expiry date" + tempcldate);
       *
       * //String flagstat=""; try {
       *
       * if (getMajorCode().equalsIgnoreCase("IGT") &&
       * (getMinorCode().equalsIgnoreCase("IIG") ||
       * getMinorCode().equalsIgnoreCase("NAIG") ||
       * getMinorCode().equalsIgnoreCase("NJIG"))) {
       *
       * // String clmdate = // getDriverWrapper().getEventFieldAsText("cAIT", "d",
       * ""); // // 12/10/16 // Loggers.general().info(LOG,"Claim expiry date in
       * statutory // claim"+clmdate);
       *
       *
       *
       * String type = getDriverWrapper().getEventFieldAsText("PUL2", "s", ""); //
       * 12/10/16 Loggers.general().info(LOG,"Inland/Foreign Type in statutory claim"
       * + type);
       *
       * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); Calendar c =
       * Calendar.getInstance();
       *
       * String tempstatdate = getPane().getSTACLAIM();
       * Loggers.general().info(LOG,"statuatory date ======= 1" + tempstatdate);
       *
       *
       *
       * tempstatdate = tempstatdate.trim();
       *
       * if ((tempstatdate != null) && (!tempstatdate.equalsIgnoreCase(""))) { try {
       * c.setTime(sdf.parse(tempstatdate)); tempstatdate = sdf.format(c.getTime());
       * // tempstatdate = tempstatdate.trim();
       * Loggers.general().info(LOG,"tempstatdate========= 2" + tempstatdate); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG," tempstatdate---->3" + tempstatdate); }
       *
       * Loggers.general().info(LOG,"tempstatdate getpane======4" +
       * getPane().getSTACLAIM()); } catch (Exception e) {
       * Loggers.general().info(LOG,"Exception in tempstatdate statutory claim" +
       * e.getMessage()); }
       *
       * }
       *
       * output=output.trim(); Loggers.general().info(LOG,"output==============>" +
       * output);
       *
       * if(((tempexp != null) &&
       * (!tempexp.equalsIgnoreCase("")))&&(!tempexp.equalsIgnoreCase(expdate))){
       * output= ""; tempstatdate=""; flagstat=""; getPane().setSTACLAIM(""); }
       *
       * if(((flagclmdate != null) &&
       * (!flagclmdate.equalsIgnoreCase("")))&&(!flagclmdate.equalsIgnoreCase(
       * tempcldate))&&(type.equalsIgnoreCase("Foreign"))){ output= "";
       * tempstatdate=""; //flagstat=""; getPane().setSTACLAIM(""); }
       *
       * Loggers.general().info(LOG,"flagstat<<<<<<<<<<<<"+flagstat);
       * Loggers.general().info(LOG,"tempstatdate<<<<<<<<"+tempstatdate);
       * Loggers.general().info(LOG,"output<<<<<<<<<"+output);
       * Loggers.general().info(LOG,"getstaclaim<<<<<<<<<"+getPane().getSTACLAIM());
       *
       *
       *
       * if (expdate != null && !expdate.equalsIgnoreCase("") ) {
       *
       * if (type != null && !type.equalsIgnoreCase("")) {
       *
       * if (type.equalsIgnoreCase("Inland")) {
       *
       * Loggers.general().info(LOG,"INland=========>"+type);
       *
       * if (((tempstatdate != null) && (!tempstatdate.equalsIgnoreCase(""))) &&
       * ((output != null) && (!output.equalsIgnoreCase(""))) &&
       * (!output.equalsIgnoreCase(tempstatdate))) {
       * getPane().setSTACLAIM(tempstatdate); flagstat=getPane().getSTACLAIM();
       * Loggers.general().info(LOG,"flagstat"+flagstat); Loggers.general().info(LOG,
       * "inside inland output not equal to tempstatdate=====1" +
       * getPane().getSTACLAIM()); } else {
       *
       * try {
       *
       * c.setTime(sdf.parse(expdate));
       *
       * c.add(Calendar.YEAR, 1);
       *
       * output = sdf.format(c.getTime());
       *
       * Loggers.general().info(LOG,"output" + output); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG," statutory claim expiry date---->" + output); }
       *
       * getPane().setSTACLAIM(output); flagstat=getPane().getSTACLAIM();
       * Loggers.general().info(LOG,"flagstat"+flagstat);
       * Loggers.general().info(LOG,"getting staclaim in inland=====2" +
       * getPane().getSTACLAIM());
       *
       * }
       *
       * catch (Exception e) {
       * Loggers.general().info(LOG,"Exception in inland statutory claim======" +
       * e.getMessage()); } } } else {
       *
       * if (type.equalsIgnoreCase("Foreign")) {
       * Loggers.general().info(LOG,"Foreign======>"+type);
       *
       * if (((tempstatdate != null) && (!tempstatdate.equalsIgnoreCase("")))) { if
       * ((output != null) && (!output.equalsIgnoreCase(""))) {
       *
       * if (!tempcldate.equalsIgnoreCase(tempstatdate)&&(flagstat.equalsIgnoreCase(
       * tempstatdate))) { Loggers.general().info(LOG,"claim date staclaim in foreign"
       * + tempcldate); getPane().setSTACLAIM(tempcldate);
       * flagstat=getPane().getSTACLAIM();
       * Loggers.general().info(LOG,"flagstat====1"+flagstat);
       *
       *
       * Loggers.general().info(LOG,"getting staclaim in foreign=====1" +
       * getPane().getSTACLAIM()); } else {
       *
       * getPane().setSTACLAIM(tempstatdate); flagstat=getPane().getSTACLAIM();
       * Loggers.general().info(LOG,"flagstat===2"+flagstat);
       *
       * Loggers.general().info(
       * LOG,"inside inland output not equal to tempstatdate=====2" +
       * getPane().getSTACLAIM()); }
       *
       * // if(!tempcldate.equalsIgnoreCase(tempstatdate))
       *
       * } else {
       *
       * getPane().setSTACLAIM(tempstatdate); flagstat=getPane().getSTACLAIM();
       * Loggers.general().info(LOG,"flagstat====3"+flagstat);
       *
       * Loggers.general().info(
       * LOG,"inside inland output not equal to tempstatdate======3" +
       * getPane().getSTACLAIM()); }
       *
       * } else { Loggers.general().info(LOG,"claim date staclaim in foreign====4" +
       * tempcldate); getPane().setSTACLAIM(tempcldate);
       * flagstat=getPane().getSTACLAIM();
       * Loggers.general().info(LOG,"flagstat======4"+flagstat);
       *
       * Loggers.general().info(LOG,"getting staclaim in foreign" +
       * getPane().getSTACLAIM()); } } } } } }
       *
       * } catch (Exception e) {
       * Loggers.general().info(LOG,"Exception in main try statutory claim" +
       * e.getMessage()); } //
       * =============================================================================
       * ==================
       *
       * try { String statdate = getPane().getSTACLAIM(); int gradate = 1;
       *
       * String type1 = getDriverWrapper().getEventFieldAsText("PUL2", "s", ""); //
       * 12/10/16
       * Loggers.general().info(LOG,"Inland/Foreign in statutory claim in second try"
       * + type1);
       *
       * String newclmdate = getPane().getNEWCLMDT();
       * Loggers.general().info(LOG,"New claim expiry date" + newclmdate);
       *
       * Loggers.general().info(LOG,"statutory claim in second try" + statdate);
       *
       * if ((getMajorCode().equalsIgnoreCase("IGT")) &&
       * (getMinorCode().equalsIgnoreCase("IIG"))) {
       *
       * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); Calendar c =
       * Calendar.getInstance();
       *
       * try {
       *
       * c.setTime(sdf.parse(statdate));
       *
       * c.add(Calendar.DATE, gradate);
       *
       * String outputVal = sdf.format(c.getTime());
       * Loggers.general().info(LOG,outputVal); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Charge end date IGT for foreign and inland---->"
       * + outputVal); } getPane().setCHAENDED(outputVal);
       * Loggers.general().info(LOG,"outputVal" + outputVal);
       *
       * } catch (Exception e) {
       * Loggers.general().info(LOG,"exception in date second catch" +
       * e.getMessage());
       *
       * }
       *
       * }
       *
       * else {
       *
       * if (type1.equalsIgnoreCase("Foreign")) { SimpleDateFormat sdf = new
       * SimpleDateFormat("dd/MM/yy"); Calendar c = Calendar.getInstance();
       *
       * try {
       *
       * c.setTime(sdf.parse(newclmdate));
       *
       * c.add(Calendar.DATE, gradate);
       *
       * String outputVal = sdf.format(c.getTime());
       * Loggers.general().info(LOG,outputVal); if
       * (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * "Charge end date IGT for foreign and inland for amend adjust---->" +
       * outputVal); } getPane().setCHAENDED(outputVal);
       * Loggers.general().info(LOG,"outputVal" + outputVal);
       *
       * } catch (Exception e) {
       * Loggers.general().info(LOG,"exception in date second catch amend adjust" +
       * e.getMessage());
       *
       * } }
       *
       * else { SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); Calendar c =
       * Calendar.getInstance();
       *
       * try {
       *
       * c.setTime(sdf.parse(statdate));
       *
       * c.add(Calendar.DATE, gradate);
       *
       * String outputVal = sdf.format(c.getTime());
       * Loggers.general().info(LOG,outputVal); if
       * (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(
       * LOG,"Charge end date IGT for foreign and inland for adj amd---->" +
       * outputVal); } getPane().setCHAENDED(outputVal);
       * Loggers.general().info(LOG,"outputVal" + outputVal);
       *
       * } catch (Exception e) {
       * Loggers.general().info(LOG,"exception in date second catch" +
       * e.getMessage());
       *
       * }
       *
       * } }
       *
       * } catch (Exception e) {
       * Loggers.general().info(LOG,"exception in second catch charge basis end date"
       * + e.getMessage()); }
       *
       * tempexp = expdate; Loggers.general().info(LOG,"tempexp====>" + tempexp);
       * flagclmdate=tempcldate;
       * Loggers.general().info(LOG,"Temp claim days"+flagclmdate);
       *
       * }
       */

      /*
       * public void getStatutoryChargeGur() { String strLog = "Log"; String
       * dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { // Loggers.general().info(LOG,
       * "ADDDailyTxnLimit is empty-------->");
       *
       * } //----------------------------------------------------------- start of
       * inland / foreign (Statutory Claim Expiry date)
       * --------------------------------------- try {
       *
       * if (getMajorCode().equalsIgnoreCase("IGT") &&
       * (getMinorCode().equalsIgnoreCase("IIG") ||
       * getMinorCode().equalsIgnoreCase("NAIG") ||
       * getMinorCode().equalsIgnoreCase("NJIG"))) {
       *
       * String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); //
       * 12/10/16 Loggers.general().info(LOG,"Expiry date in statutory claim" +
       * expdate);
       *
       * //String clmdate = getDriverWrapper().getEventFieldAsText("cAIT", "d", "");
       * // 12/10/16 //Loggers.general().info(LOG,
       * "Claim expiry date in statutory claim" + clmdate);
       *
       * String tempcldate = getPane().getCLIMEXPD(); Loggers.general().info(LOG,
       * "Pane claim expiry date" + tempcldate);
       *
       * String type = getDriverWrapper().getEventFieldAsText("PUL2", "s", ""); //
       * 12/10/16 Loggers.general().info(LOG,"Inland/Foreign in statutory claim" +
       * type);
       *
       * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); Calendar c =
       * Calendar.getInstance();
       *
       * String tempstatdate = getPane().getSTACLAIM(); Loggers.general().info(LOG,
       * "tempervory stat date" + tempstatdate);
       *
       * try { c.setTime(sdf.parse(tempstatdate)); tempstatdate =
       * sdf.format(c.getTime()); tempstatdate=tempstatdate.trim();
       * Loggers.general().info(LOG,"tempstatdate" + tempstatdate); if
       * (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * " tempstatdate---->" + tempstatdate); }
       *
       * Loggers.general().info(LOG,"tempstatdate getpane" + getPane().getSTACLAIM());
       * } catch (Exception e) { Loggers.general().info(LOG,
       * "Exception in tempstatdate statutory claim" + e.getMessage()); }
       *
       *
       *
       *
       *
       *
       * if (expdate != null && !expdate.equalsIgnoreCase("")) {
       *
       * if (type != null && !type.equalsIgnoreCase("")) { Loggers.general().info(LOG,
       * "Output Date >>>>>>>>>>>>>>>>" + output);
       *
       * if (((output.equalsIgnoreCase("")) || (output == null)) &&
       * (type.equalsIgnoreCase("Inland"))) { Loggers.general().info(LOG,
       * "Condition Check method one inland"); try { c.setTime(sdf.parse(expdate));
       * c.add(Calendar.YEAR, 1); output = sdf.format(c.getTime());
       * output=output.trim(); Loggers.general().info(LOG,"output" + output); if
       * (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * " statutory claim expiry date---->" + output); }
       * getPane().setSTACLAIM(output); Loggers.general().info(LOG,
       * "getting staclaim in inland" + getPane().getSTACLAIM()); } catch (Exception
       * e) { Loggers.general().info(LOG,"Exception in inland statutory claim" +
       * e.getMessage()); } }
       *
       * else if (((!output.equalsIgnoreCase("")) && (output != null)) &&
       * (type.equalsIgnoreCase("Inland"))) { Loggers.general().info(LOG,
       * "Condition Check method two inland"); Loggers.general().info(LOG,
       * "Output Date >>>>>>>>>>>>>>>>" + output+">>>>>>>");
       * LOG.info("tempstatdate >>>>>>>>>>>>>>>>" + tempstatdate);
       * LOG.info("Output Date length >>>>>>>>>>>>>>>>" + output.length()+">>>>>>>");
       * Loggers.general().info(LOG,"tempstatdate length>>>>>>>>>>>>>>>>" +
       * tempstatdate.length());
       *
       * if (((!tempstatdate.equalsIgnoreCase(""))&&(tempstatdate!=null))&&
       * (!output.equalsIgnoreCase(tempstatdate))) {
       * getPane().setSTACLAIM(tempstatdate); Loggers.general().info(LOG,
       * "inside inland output not equal to tempstatdate" + getPane().getSTACLAIM());
       * } else { try {
       *
       * c.setTime(sdf.parse(expdate));
       *
       * c.add(Calendar.YEAR, 1);
       *
       * output = sdf.format(c.getTime()); output=output.trim();
       *
       * Loggers.general().info(LOG,"output" + output); if
       * (dailyval_Log.equalsIgnoreCase("YES")) { System.out .println(
       * " statutory claim expiry date output not null and equal---->" + output); }
       *
       * getPane().setSTACLAIM(output); Loggers.general().info(LOG,
       * "getting staclaim in inland output not null and equal" +
       * getPane().getSTACLAIM());
       *
       * } catch (Exception e) { Loggers.general().info(LOG,
       * "Exception in inland statutory claim output not null and equal" +
       * e.getMessage()); } } } else if (((output.equalsIgnoreCase("")) || (output ==
       * null)) && (type.equalsIgnoreCase("Foreign"))) { Loggers.general().info(LOG,
       * "claim date staclaim in foreign" + tempcldate);
       * getPane().setSTACLAIM(tempcldate); Loggers.general().info(LOG,
       * "getting staclaim in foreign" + getPane().getSTACLAIM()); } else if
       * (((!output.equalsIgnoreCase("")) && (output != null)) &&
       * type.equalsIgnoreCase("Foreign")) { Loggers.general().info(LOG,
       * "Condition Check method two in foregin"); Loggers.general().info(LOG,
       * "Output Date >>>>>>>>>>>>>>>>" + output); Loggers.general().info(LOG,
       * "tempstatdate >>>>>>>>>>>>>>>>" + tempstatdate);
       *
       * Loggers.general().info(LOG,"Output Date length >>>>>>>>>>>>>>>>" +
       * output.length()); Loggers.general().info(LOG,
       * "tempstatdate length>>>>>>>>>>>>>>>>" + tempstatdate.length());
       * Loggers.general().info(LOG,"True or false in foreign>>>>>>>>>>>>>>>>" +
       * (output != tempstatdate)); Loggers.general().info(LOG,
       * "True or false in foreign>>>>>>>>>>>>>>>>" +
       * (output.equalsIgnoreCase(tempstatdate)));
       *
       * if (((!tempstatdate.equalsIgnoreCase(""))&&(tempstatdate!=null))&&
       * (!output.equalsIgnoreCase(tempstatdate))) {
       * getPane().setSTACLAIM(tempstatdate); Loggers.general().info(LOG,
       * "getting staclaim in foreign output not null======>" +
       * getPane().getSTACLAIM());
       *
       * } else { getPane().setSTACLAIM(tempcldate); Loggers.general().info(LOG,
       * "getting staclaim in foreign output not null" + getPane().getSTACLAIM()); }
       * }else { Loggers.general().info(LOG,
       * "Nither any condition returned true >>>>>>>>>>>>>>>>>>>>>>>"); } } } }
       *
       * } catch (Exception e) { Loggers.general().info(LOG,
       * "Exception in main try statutory claim" + e.getMessage()); }
       *
       * //----------------------------------------------------------- end of inland /
       * foreign (Statutory Claim Expiry date) ---------------------------------------
       *
       *
       * //--------------------------------------------------------start Charge basis
       * end date --------------------------------------------------------- try {
       * String statdate = getPane().getSTACLAIM(); int gradate = 1;
       *
       * String type1 = getDriverWrapper().getEventFieldAsText("PUL2", "s", ""); //
       * 12/10/16 Loggers.general().info(LOG,
       * "Inland/Foreign in statutory claim in second try" + type1);
       *
       * String newclmdate = getPane().getNEWCLMDT(); Loggers.general().info(LOG,
       * "New claim expiry date" + newclmdate);
       *
       * Loggers.general().info(LOG,"statutory claim in second try" + statdate);
       *
       * if ((getMajorCode().equalsIgnoreCase("IGT")) &&
       * (getMinorCode().equalsIgnoreCase("IIG"))) {
       *
       * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); Calendar c =
       * Calendar.getInstance();
       *
       * try {
       *
       * c.setTime(sdf.parse(statdate));
       *
       * c.add(Calendar.DATE, gradate);
       *
       * String outputVal = sdf.format(c.getTime());
       * Loggers.general().info(LOG,outputVal); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Charge end date IGT for foreign and inland---->"
       * + outputVal); } getPane().setCHAENDED(outputVal);
       * Loggers.general().info(LOG,"outputVal" + outputVal);
       *
       * } catch (Exception e) { Loggers.general().info(LOG,
       * "exception in date second catch" + e.getMessage());
       *
       * }
       *
       * }
       *
       * else {
       *
       * if (type1.equalsIgnoreCase("Foreign")) { SimpleDateFormat sdf = new
       * SimpleDateFormat("dd/MM/yy"); Calendar c = Calendar.getInstance();
       *
       * try {
       *
       * c.setTime(sdf.parse(newclmdate));
       *
       * c.add(Calendar.DATE, gradate);
       *
       * String outputVal = sdf.format(c.getTime());
       * Loggers.general().info(LOG,outputVal); if
       * (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(LOG,
       * "Charge end date IGT for foreign and inland for amend adjust---->" +
       * outputVal); } getPane().setCHAENDED(outputVal);
       * Loggers.general().info(LOG,"outputVal" + outputVal);
       *
       * } catch (Exception e) { Loggers.general().info(LOG,
       * "exception in date second catch amend adjust" + e.getMessage());
       *
       * } }
       *
       * else { SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); Calendar c =
       * Calendar.getInstance();
       *
       * try {
       *
       * c.setTime(sdf.parse(statdate));
       *
       * c.add(Calendar.DATE, gradate);
       *
       * String outputVal = sdf.format(c.getTime());
       * Loggers.general().info(LOG,outputVal); if
       * (dailyval_Log.equalsIgnoreCase("YES")) { System.out .println(
       * "Charge end date IGT for foreign and inland for adj amd---->" + outputVal); }
       * getPane().setCHAENDED(outputVal); Loggers.general().info(LOG,"outputVal" +
       * outputVal);
       *
       * } catch (Exception e) { Loggers.general().info(LOG,
       * "exception in date second catch" + e.getMessage());
       *
       * }
       *
       * } }
       *
       * } catch (Exception e) {
       * Loggers.general().info(LOG,"exception in second catch" + e.getMessage()); }
       *
       *
       * //--------------------------------------------------------End Charge basis
       * end date ---------------------------------------------------------
       *
       *
       * }
       */
      // public void getChargeEndDateGur() {
      // String strLog = "Log";
      // String dailyval_Log = "";
      // @SuppressWarnings("unchecked")
      // AdhocQuery<? extends
      // com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
      // getDriverWrapper()
      // .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
      // EXTGENCUSTPROP CodeLog = queryLog.getUnique();
      // if (CodeLog != null) {
      //
      // dailyval_Log = CodeLog.getPropval();
      // } else {
      // // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
      //
      // }
      // try {
      //
      // int statInt = 0;
      //
      // String statutorydate = getWrapper().getSTACLAIM();
      // String claimdate = getWrapper().getCLIMEXPD();
      //
      // String type = getDriverWrapper().getEventFieldAsText("PU02", "s", ""); //
      // 12/10/16
      // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
      // Calendar c = Calendar.getInstance();
      //
      // if ((getMajorCode().equalsIgnoreCase("IGT") &&
      // getMinorCode().equalsIgnoreCase("IIG"))) {
      // // String expdate = getWrapper().getCLIMEXPD();
      //
      // if (type.equalsIgnoreCase("INL")) { // //Loggers.general().info(LOG,"expdate
      // // in issue-------> " +
      // // expdate);
      //
      // try {
      // statInt = statInt + 1;
      // c.setTime(sdf.parse(statutorydate));
      // // //Loggers.general().info(LOG,"expdate in issue-------> ");
      // c.add(Calendar.DATE, statInt);
      // // //Loggers.general().info(LOG,"DATE 1"+ c);
      // String output = sdf.format(c.getTime());
      // // //Loggers.general().info(LOG,output);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Charge end date IGT for foreign---->" + output);
      // }
      // getPane().setCHAENDED(output);
      //
      // } catch (Exception e) {
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      //
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"IGT Charge end date for foreign---->" +
      // e.getMessage());
      // }
      // }
      // }
      // }
      // } else {
      //
      // // //Loggers.general().info(LOG,"expdate in issue-------> " + expdate);
      //
      // try {
      // statInt = statInt + 1;
      // c.setTime(sdf.parse(claimdate));
      // // //Loggers.general().info(LOG,"expdate in issue-------> ");
      // c.add(Calendar.DATE, statInt);
      // // //Loggers.general().info(LOG,"DATE 1"+ c);
      // String output = sdf.format(c.getTime());
      // // //Loggers.general().info(LOG,output);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Charge end date IGT for Inland---->" + output);
      // }
      // getPane().setCHAENDED(output);
      //
      // } catch (Exception e) {
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      //
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Exeception New Charge end date for Inland---->" +
      // e.getMessage());
      // }
      // }
      // }
      // }
      // } catch (Exception e) {
      // Loggers.general().info(LOG,"Exeception in charge end date" + e.getMessage());
      //
      // }
      // }

      public void getChargeGurISB() {
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {

                  int garceInt = 0;
                  try {

                        String graceda = getWrapper().getGRACEPER();

                        garceInt = Integer.parseInt(graceda);
                  } catch (Exception e) {
                        garceInt = 0;

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Exception in Grace days--->" + e.getMessage());
                        }
                  }
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("ISB grace days---->" + garceInt);
                  }
                  String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
                  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  Calendar c = Calendar.getInstance();
                  int gra = 1 + garceInt;
                  if ((getMajorCode().equalsIgnoreCase("ISB") && getMinorCode().equalsIgnoreCase("IIS"))) {
                        // String expdate = getWrapper().getCLIMEXPD();

                        // //Loggers.general().info(LOG,"expdate in issue-------> " + expdate);

                        try {

                              c.setTime(sdf.parse(expdate));
                              // //Loggers.general().info(LOG,"expdate in issue-------> ");
                              c.add(Calendar.DATE, gra);
                              // //Loggers.general().info(LOG,"DATE 1"+ c);
                              String output = sdf.format(c.getTime());
                              // //Loggers.general().info(LOG,output);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Charge end date IGT---->" + output);
                              }
                              getPane().setCHAENDED(output);

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Exception ISB Charge end date---->" + e.getMessage());
                                    }
                              }
                        }
                  } else {

                        // //Loggers.general().info(LOG,"expdate in issue-------> " + expdate);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("New claim date ISB else--->" + expdate);
                        }

                        try {

                              c.setTime(sdf.parse(expdate));
                              // //Loggers.general().info(LOG,"expdate in issue-------> ");
                              c.add(Calendar.DATE, gra);
                              // //Loggers.general().info(LOG,"DATE 1"+ c);
                              String output = sdf.format(c.getTime());
                              // //Loggers.general().info(LOG,output);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("New claim end date---->" + output);
                              }
                              getPane().setCHAENDED(output);

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Exception New Charge end date---->" + e.getMessage());
                                    }
                              }
                        }
                  }
            } catch (Exception e) {
                  LOG.info("Exeception in charge end date ISB" + e.getMessage());

            }
      }

      public void getclaimExpiryDateISB() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String product = getDriverWrapper().getEventFieldAsText("PCO", "s", "");

            String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            Calendar c = Calendar.getInstance();
            try {

                  // //Loggers.general().info(LOG,"Expiry date" + expdate);
                  String graceda = "0";
                  graceda = getWrapper().getGRACEPER();

                  int gra = 0;

                  if (((!graceda.equalsIgnoreCase("")) || graceda != null) && graceda.length() > 0) {
                        // //Loggers.general().info(LOG,"claimdate is blank");
                        try {
                              if (graceda.length() > 0
                                          && (getMajorCode().equalsIgnoreCase("ISB") || getMajorCode().equalsIgnoreCase("EGT"))
                                          && (getMinorCode().equalsIgnoreCase("IIS") || getMinorCode().equalsIgnoreCase("VEG"))) {
                                    gra = Integer.parseInt(graceda);
                                    c.setTime(sdf.parse(expdate));
                                    // //Loggers.general().info(LOG,"expdate in issue------->
                                    // ");
                                    c.add(Calendar.DATE, gra);
                                    // //Loggers.general().info(LOG,"DATE 1"+ c);
                                    String output = sdf.format(c.getTime());
                                    // Loggers.general().info(LOG,output);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("ISB claim date---->" + output);
                                    }
                                    getPane().setCLIMEXPD(output);
                                    getWrapper().setCLIMEXPD(output);

                              } else {
                                    // //Loggers.general().info(LOG,"New claim expiry date enter
                                    // ------->
                                    // ");
                                    if (graceda.length() > 0
                                                && (getMinorCode().equalsIgnoreCase("NAIS") || getMinorCode().equalsIgnoreCase("NAEG"))
                                                && expdate.length() > 0) {
                                          gra = Integer.parseInt(graceda);
                                          c.setTime(sdf.parse(expdate));
                                          // //Loggers.general().info(LOG,"expdate in adjust and
                                          // amend-------> ");
                                          c.add(Calendar.DATE, gra);
                                          // //Loggers.general().info(LOG,"DATE 1"+ c);
                                          String output = sdf.format(c.getTime());
                                          // Loggers.general().info(LOG,output);
                                          // getWrapper().setNEWCLMDT(output);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("ISB New claim date---->" + output);
                                          }
                                          getPane().setNEWCLMDT(output);
                                          getWrapper().setNEWCLMDT(output);
                                    } else {
                                          // Loggers.general().info(LOG,"else part of grace
                                          // days");
                                    }
                              }
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Exception IGT claim and new claim expiry date---->" + e.getMessage());
                              }
                        }

                  } else {

                        // gra = Integer.parseInt(graceda);
                        try {
                              if (expdate.length() > 0) {
                                    c.setTime(sdf.parse(expdate));

                                    c.add(Calendar.DATE, gra);
                                    // //Loggers.general().info(LOG,"DATE 1"+ c);
                                    String output = sdf.format(c.getTime());

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("ISB claim date else---->" + output);
                                    }
                                    getPane().setCLIMEXPD(output);
                                    getWrapper().setCLIMEXPD(output);
                              } else {
                                    // Loggers.general().info(LOG,"expdate is blank" + expdate);
                              }
                        } catch (ParseException e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Exception ISB grace days---->" + e.getMessage());
                              }
                        }

                  }
            } catch (Exception e) {
                  LOG.info("Exception ISB Claim expiry date---->" + e.getMessage());

            }

            String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
            String effct = getWrapper().getEFFDATE();
            // //Loggers.general().info(LOG,"effct date --->" + effct);
            if (effct == null) {

                  try {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                        Calendar cal = Calendar.getInstance();
                        int val = 0;
                        cal.setTime(sdf1.parse(systemDate));
                        // //Loggers.general().info(LOG,"effct in issue-------> ");
                        cal.add(Calendar.DATE, val);
                        String output = sdf1.format(cal.getTime());
                        // Loggers.general().info(LOG,"output----->" + output);
                        getPane().setEFFDATE(output);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"EFFECTIVE date --->" +
                        // e.getMessage());
                  }
            } else {
                  String effctive = getWrapper().getEFFDATE();
                  // //Loggers.general().info(LOG,"effctive date in else --->" +
                  // effctive);
                  try {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                        Calendar cal = Calendar.getInstance();
                        int val = 0;
                        cal.setTime(sdf1.parse(effctive));
                        // //Loggers.general().info(LOG,"effct in issue-------> ");
                        cal.add(Calendar.DATE, val);
                        String output = sdf1.format(cal.getTime());
                        // Loggers.general().info(LOG,"output in else----->" + output);
                        getPane().setEFFDATE(output);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"EFFECTIVE date in else--->" +
                        // e.getMessage());
                  }
            }

      }

      @SuppressWarnings("unused")
      public void totalLiaAmount() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {

                  // INTEREST AMOUNT CALCULATION
                  String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  String fullcur = getDriverWrapper().getEventFieldAsText("FOA", "v", "c");
                  if (subproCode.equalsIgnoreCase("BCR")
                              && (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {

                        DecimalFormat diff = new DecimalFormat("0.00");
                        diff.setMaximumFractionDigits(2);
                        if (getMinorCode().equalsIgnoreCase("IIG")) {

                              String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Interest amount initialy" + inamt);
                              }
                              String inamtcur = getDriverWrapper().getEventFieldAsText("cAKB", "v", "c");
                              String mastamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
                              String indcateval = getWrapper().getINDNDDT();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Master amount initialy" + mastamt);
                              }

                              if ((!inamt.equalsIgnoreCase("")) || (inamt != null)) {

                                    try {

                                          BigDecimal priceDecimal = new BigDecimal(mastamt);
                                          // //Loggers.general().info(LOG,"Master amount value" +
                                          // priceDecimal);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("Master amount value" + priceDecimal);
                                          }

                                          BigDecimal total = priceDecimal.add(new BigDecimal(inamt));
                                          String totalVal = diff.format(total);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("FOA amount and Interest amount IGT===>" + totalVal);
                                          }
                                          String finalvalue = String.valueOf(totalVal);
                                          getPane().setTOTLOAM(finalvalue + " " + fullcur);

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,e.getMessage());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("Exception in issue event---> " + e.getMessage());
                                          }
                                    }

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Interset amount else---> " + inamt);
                                    }
                                    String liabiltyamt = getDriverWrapper().getEventFieldAsText("LIA", "v", "m");
                                    BigDecimal liabiltyDecimal = new BigDecimal(liabiltyamt);
                                    String totalLiabilty = diff.format(liabiltyDecimal);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Interest amount is blank and Total liability amount---> " + totalLiabilty);
                                    }
                                    getPane().setTOTLOAM(totalLiabilty + " " + fullcur);
                              }

                        } else if (getMinorCode().equalsIgnoreCase("NAIG")) {
                              String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Amend Interest amount---> " + inamt);
                              }
                              String inamtcur = getDriverWrapper().getEventFieldAsText("cAKB", "v", "c");
                              String mastamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Amend liability amount---> " + mastamt);
                              }
                              String indcateval = getWrapper().getAMDIND();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Amend indicator field---> " + indcateval + "length--->" + indcateval.length());
                              }

                              if ((!inamt.equalsIgnoreCase("")) || (inamt != null)) {

                                    try {

                                          BigDecimal priceDecimal = new BigDecimal(mastamt);
                                          BigDecimal total = priceDecimal.add(new BigDecimal(inamt));
                                          String totalVal = diff.format(total);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("Total Amend liability INCREASE amount---> " + totalVal);
                                          }
                                          String finalvalue = String.valueOf(totalVal);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("Amend liability INCREASE amount---> " + finalvalue);
                                          }
                                          getPane().setTOTLOAM(finalvalue + " " + fullcur);

                                          // // //Loggers.general().info(LOG,"Master amount value" +
                                          // // priceDecimal);
                                          // if (indcateval.equalsIgnoreCase("INC")) {
                                          // BigDecimal priceDecimal = new
                                          // BigDecimal(mastamt);
                                          // BigDecimal total = priceDecimal.add(new
                                          // BigDecimal(inamt));
                                          // String totalVal = diff.format(total);
                                          // if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,"Total Amend liability
                                          // INCREASE amount---> " + totalVal);
                                          // }
                                          // String finalvalue = String.valueOf(totalVal);
                                          // if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,"Amend liability INCREASE
                                          // amount---> " + finalvalue);
                                          // }
                                          // getPane().setTOTLOAM(finalvalue + " " + fullcur);
                                          // } else if (indcateval.equalsIgnoreCase("DEC")) {
                                          // BigDecimal priceDecimal = new
                                          // BigDecimal(mastamt);
                                          // BigDecimal total = priceDecimal.subtract(new
                                          // BigDecimal(inamt));
                                          //
                                          // String totalVal = diff.format(total);
                                          // if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,"Total Amend liability
                                          // DECREASE amount---> " + totalVal);
                                          // }
                                          // String finalvalue = String.valueOf(totalVal);
                                          // if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,"Amend liability DECREASE
                                          // amount---> " + finalvalue);
                                          // }
                                          // getPane().setTOTLOAM(finalvalue + " " + fullcur);
                                          // } else if (indcateval.length() == 0 ||
                                          // indcateval.length() < 0) {
                                          //
                                          // if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,"Indicator value is blank and
                                          // Liability amount---> " + mastamt);
                                          // }
                                          //
                                          // try {
                                          // String intAmt =
                                          // getDriverWrapper().getEventFieldAsText("cAKB",
                                          // "v", "m");
                                          // String intCuurecy =
                                          // getDriverWrapper().getEventFieldAsText("cAKB",
                                          // "v", "c");
                                          // BigDecimal intAmtDecimal = new
                                          // BigDecimal(intAmt);
                                          // String totalintAmount =
                                          // diff.format(intAmtDecimal);
                                          // if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,
                                          // "Indicator value is blank and interest amount--->
                                          // " + totalintAmount);
                                          // }
                                          // getPane().setINTAMT(totalintAmount + " " +
                                          // intCuurecy);
                                          // } catch (Exception e) {
                                          // if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,
                                          // "Exception Indicator value is blank amand---> " +
                                          // e.getMessage());
                                          // }
                                          // }
                                          // String liabiltyamt =
                                          // getDriverWrapper().getEventFieldAsText("cACP",
                                          // "v", "m");
                                          // BigDecimal liabiltyDecimal = new
                                          // BigDecimal(liabiltyamt);
                                          // String totalLiabilty =
                                          // diff.format(liabiltyDecimal);
                                          // if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,
                                          // "Indicator value is blank and Total liability
                                          // amount---> " + totalLiabilty);
                                          // }
                                          // getPane().setTOTLOAM(totalLiabilty + " " +
                                          // fullcur);
                                          // }

                                    } catch (Exception e) {

                                          // Loggers.general().info(LOG,e.getMessage());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Exception in amand and adjust event---> " + e.getMessage());
                                          }

                                    }

                              } else {
                                    // //Loggers.general().info(LOG,"Interest percentage
                                    // empty----->" +
                                    // inamt);
                                    String liabiltyamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
                                    BigDecimal liabiltyDecimal = new BigDecimal(liabiltyamt);
                                    String totalLiabilty = diff.format(liabiltyDecimal);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Interest amount is blank and Total liability amount---> " + totalLiabilty);
                                    }
                                    getPane().setTOTLOAM(totalLiabilty + " " + fullcur);
                              }
                        }

                  } else {
                        // Loggers.general().info(LOG,"Product type is not a buyer credit");
                  }
            } catch (Exception e) {
                  LOG.info("Exception in Total liability amount calculation---> " + e.getMessage());

            }

      }

      public void getPerdChgEndDate() {
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            if (getMajorCode().equalsIgnoreCase("IGT") || getMajorCode().equalsIgnoreCase("ILC")
                        || getMajorCode().equalsIgnoreCase("ISB")) {
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  try {
                        con = getConnection();
                        String dms = "SELECT PERIOD,END_DATE from ETT_PERIODIC_CHARGE_VIEW where MASTER_REF = '"
                                    + MasterReference + "'";
                        ps1 = con.prepareStatement(dms);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Periodic charge end date query---->" + dms);
                        }

                        rs = ps1.executeQuery();
                        if (rs.next()) {
                              String perdChg = rs.getString(1).trim();
                              double periodicVal = Double.valueOf(perdChg);
                              double periodicDouble = 200;
                              String periodicDate = rs.getString(2).trim();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Periodic charge value---->" + periodicVal);
                              }

                              if (periodicVal >= periodicDouble) {

                                    // String perdic_val = getWrapper().getCHAENDED();

                                    String chargeDate = getDriverWrapper().getEventFieldAsText("cBKZ", "d", ""); // Charge
                                                                                                                                                            // end
                                                                                                                                                            // date
                                                                                                                                                            // master
                                                                                                                                                            // value
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Master Event Charge end date ---->" + chargeDate);
                                    }

                                    int gra = 0;
                                    try {
                                          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                          Calendar c = Calendar.getInstance();
                                          c.setTime(sdf.parse(chargeDate));
                                          c.add(Calendar.DATE, gra);
                                          // //Loggers.general().info(LOG,"DATE 1"+ c);
                                          String output = sdf.format(c.getTime());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("Periodic charge end date if---->" + output);
                                          }
                                          getPane().setVALDAT(output);
                                    } catch (Exception e) {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                LOG.info("Exeception Periodic charge query" + e.getMessage());
                                          }
                                    }

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Periodic charge end date else in period---->" + periodicDate);
                                    }
                                    getPane().setVALDAT(periodicDate);
                              }

                        } else {

                              String chargeDate = getDriverWrapper().getEventFieldAsText("cBKZ", "d", ""); // Charge
                                                                                                                                                      // end
                                                                                                                                                      // date
                                                                                                                                                      // master
                                                                                                                                                      // value
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Master Event Charge end date else---->" + chargeDate);
                              }

                              int gra = 0;
                              try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(sdf.parse(chargeDate));
                                    c.add(Calendar.DATE, gra);
                                    // //Loggers.general().info(LOG,"DATE 1"+ c);
                                    String output = sdf.format(c.getTime());
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Periodic charge end date else---->" + output);
                                    }
                                    getPane().setVALDAT(output);
                              } catch (Exception e) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Exeception Periodic charge query else" + e.getMessage());
                                    }
                              }

                        }
                  } catch (Exception e) {

                        LOG.info("Exeception periodic Charge end date " + e.getMessage());

                  } finally {
                        try {
                              if (rs1 != null)
                                    rs1.close();
                              if (ps1 != null)
                                    ps1.close();
                              if (con != null)
                                    con.close();
                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }

            }
      }

      public void getactualPenalRate() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String refNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
            String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
            // //Loggers.general().info(LOG,"event refNumber ----->" + evnt);
            String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
            try {
                  con = getConnection();
                  String query = "SELECT exte.PENALRAT, mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, bev1.REFNO_PFIX, bev1.REFNO_SERL FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.key97 = exte.EVENT AND mas.MASTER_REF = '"
                              + refNumber + "' AND bev.REFNO_PFIX='" + evnt + "' AND bev.REFNO_SERL=" + evvcount + "";

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("Penal rate query " + query);
                  }

                  ps1 = con.prepareStatement(query);
                  rs1 = ps1.executeQuery();
                  while (rs1.next()) {
                        String penalRate = rs1.getString(1);

                        BigDecimal penalRateCode = new BigDecimal(penalRate);
                        DecimalFormat diff = new DecimalFormat("0.00");
                        diff.setMaximumFractionDigits(4);
                        String penalRateVal = diff.format(penalRateCode);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Penal rate value " + penalRateVal);
                        }
                        getPane().setPENALRAT(penalRateVal);
                  }
            }

            catch (Exception e) {

                  LOG.info("Penal rate query " + e.getMessage());

            } finally {
                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void getcustomerName() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String buyid = getWrapper().getBUYID();
            String selid = getWrapper().getSELID();

            try {
                  con = getConnection();
                  String query = "select trim(GFCUN) from gfpf where GFCUS1='" + buyid + "'";

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("Customer buyer id " + query);
                  }

                  ps1 = con.prepareStatement(query);
                  rs1 = ps1.executeQuery();
                  while (rs1.next()) {
                        String buyVal = rs1.getString(1);

                        getPane().setBUYERN_Name(buyVal);
                  }

                  String query1 = "select trim(GFCUN) from gfpf where GFCUS1='" + selid + "'";

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("Customer seller id " + query1);
                  }

                  ps1 = con.prepareStatement(query1);
                  rs1 = ps1.executeQuery();
                  while (rs1.next()) {
                        String selVal = rs1.getString(1);

                        getPane().setSELLERN_Name(selVal);
                  }

            }

            catch (Exception e) {

                  LOG.info("Exception Buyer/Seller id " + e.getMessage());

            } finally {
                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      /*
       * public void getbillNumber() {
       *
       * String strLog = "Log"; String dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { //
       * Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
       *
       * }
       *
       * try { String refNumber = getDriverWrapper().getEventFieldAsText("MST", "r",
       * ""); String eventNumber = getDriverWrapper().getEventFieldAsText("EVR", "r",
       * "");
       *
       * con = getConnection(); String query =
       * "select trim(bill.BILLREF) as BILLREF, bill.MASTERREF, bill.EVENTREF, swi.SWIMIR, BEV.CREATNMTHD from SWIPF swi, master mas, BASEEVENT BEV, ETT_SWIFTBILLREFMAP bill where swi.SWIKEY = mas.KEY97 AND swi.SWIEKY = BEV.KEY97 and swimir = bill.MSGINPUTREF and BEV.CREATNMTHD ='S' and mas.MASTER_REF = '"
       * + refNumber + "' and BEV.REFNO_PFIX||lpad(bev.REFNO_SERL,3,0) = '" +
       * eventNumber + "'";
       *
       * if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Bill reference number===> " + query); }
       *
       * ps1 = con.prepareStatement(query); rs1 = ps1.executeQuery(); while
       * (rs1.next()) { String billVal = rs1.getString(1).trim();
       *
       * if (billVal.length() > 0&& billVal.length()>10&&
       * !billVal.equalsIgnoreCase("NONREF")) {
       *
       * getPane().setBLLREFNO(billVal); } if(billVal.equalsIgnoreCase("NONREF")||
       * (billVal.length() > 0&&billVal.length()<10)) {
       * Loggers.general().info(LOG,"Bill reference number in while1===> " +
       * billVal.length()); getPane().setBLLREFNO(refNumber); }
       *
       * if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Bill reference number in while===> " + billVal);
       * } }
       *
       * }
       *
       * catch (Exception e) {
       *
       * Loggers.general().info(LOG,"Exception Bill reference number " +
       * e.getMessage());
       *
       * } finally { try { if (rs1 != null) rs1.close(); if (ps1 != null) ps1.close();
       * if (con != null) con.close();
       *
       * } catch (SQLException e) { // Loggers.general().info(LOG,"Connection Failed!
       * Check // output // console"); e.printStackTrace(); } }
       *
       * }
       */

      public void getbillNumber() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {
                  String refNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventNumber = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  String claimRef = getDriverWrapper().getEventFieldAsText("CLM", "r", "");
                  con = getConnection();
                  String query = "select trim(bill.BILLREF) as BILLREF, bill.MASTERREF, bill.EVENTREF, swi.SWIMIR, BEV.CREATNMTHD from SWIPF swi, master mas, BASEEVENT BEV, ETT_SWIFTBILLREFMAP bill where swi.SWIKEY = mas.KEY97 AND swi.SWIEKY = BEV.KEY97 and swimir = bill.MSGINPUTREF and BEV.CREATNMTHD ='S' and mas.MASTER_REF = '"
                              + refNumber + "' and BEV.REFNO_PFIX||lpad(bev.REFNO_SERL,3,0) = '" + eventNumber + "'";

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("Bill reference number===> " + query);
                  }

                  ps1 = con.prepareStatement(query);
                  rs1 = ps1.executeQuery();
                  while (rs1.next()) {
                        String billVal = rs1.getString(1).trim();
                        LOG.info("Bill reference number in while===> " + billVal.length());

                        if (billVal.length() > 0 && billVal.length() > 10 && !billVal.equalsIgnoreCase("NONREF")) {

                              getPane().setBLLREFNO(billVal);
                        }
                        if (billVal.equalsIgnoreCase("NONREF") || (billVal.length() > 0 && billVal.length() < 10)) {
                              String query1 = "SELECT trim(ext.BLLREFNO)  FROM UBZONE.master mas,  UBZONE.baseevent bas,  UBZONE.extevent ext WHERE mas.KEY97    =bas.MASTER_KEY AND bas.KEY97      =ext.EVENT AND mas.MASTER_REF ='"
                                          + refNumber + "' and bas.REFNO_PFIX||lpad(bas.REFNO_SERL,3,0) = '" + claimRef + "'";
                              ps = con.prepareStatement(query1);
                              rs = ps.executeQuery();
                              LOG.info("Query1===> " + billVal);
                              while (rs.next()) {
                                    String billVal1 = rs.getString(1).trim();
                                    LOG.info("Bill reference number in while1===> " + billVal1);
                                    getPane().setBLLREFNO(billVal1);
                              }
                        }

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Bill reference number in while===> " + billVal);
                        }
                  }

            }

            catch (Exception e) {

                  LOG.info("Exception Bill reference number " + e.getMessage());

            } finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void getcountryCode() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {

                  con = getConnection();
                  try {

                        String shipForm = getWrapper().getSHIPFTO_Name();
                        if (!shipForm.equalsIgnoreCase("") && shipForm.length() > 0) {
                              String query = "select TRIM(C7CNM) from c7pf where C7CNA='" + shipForm + "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Shipment from===> " + shipForm);
                                    LOG.info("Shipment from description===> " + query);
                              }

                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String countVal = rs1.getString(1).trim();

                                    if (countVal.length() > 0) {
                                          getPane().setSFROMDES(countVal);
                                    }

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Shipment from description in while===> " + countVal);
                                    }
                              }

                        }

                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Exception Shipment from description" + e.getMessage());
                        }
                  }

                  try {

                        String shipTO = getWrapper().getSHIPTOP_Name();
                        if (!shipTO.equalsIgnoreCase("") && shipTO.length() > 0) {
                              String query = "select TRIM(C7CNM) from c7pf where C7CNA='" + shipTO + "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Shipment to===> " + shipTO);
                                    LOG.info("Shipment to description===> " + query);
                              }

                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String countVal = rs1.getString(1).trim();

                                    if (countVal.length() > 0) {
                                          getPane().setSTODESP(countVal);
                                    }

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Shipment to description in while===> " + countVal);
                                    }
                              }
                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Exception Shipment to description " + e.getMessage());
                        }
                  }

                  try {

                        String orginCon = getWrapper().getORGIN_Name();
                        if (!orginCon.equalsIgnoreCase("") && orginCon.length() > 0) {
                              String query = "select TRIM(C7CNM) from c7pf where C7CNA='" + orginCon + "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Country of orgin===> " + orginCon);
                                    LOG.info("Country of orgin description===> " + query);
                              }

                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String countVal = rs1.getString(1).trim();

                                    if (countVal.length() > 0) {
                                          getPane().setCONDESP(countVal);
                                    }

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Country of orgin description in while===> " + countVal);
                                    }
                              }
                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Exception Country of orgin description" + e.getMessage());
                        }
                  }

            } catch (Exception e)

            {

                  LOG.info("Exception in country codes" + e.getMessage());

            } finally

            {
                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void getNotionalDueDate() {
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {
                  // Start of Notional due date calculation new
                  String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                  String systemDate = getDriverWrapper().getEventFieldAsText("RVD", "d", "");
                  String accpt = getDriverWrapper().getEventFieldAsText("RELS", "s", "");
                  LOG.info("ACCEPTANCE VALUE --->" + accpt);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        LOG.info("ACCEPTANCE VALUE --->" + accpt);
                  }
                  if (accpt.equalsIgnoreCase("Payment")) {

                        // //Loggers.general().info(LOG,"systemDate date --->" + systemDate);
                        if (prd_typ.equalsIgnoreCase("OCF")) {
                              // //Loggers.general().info(LOG,"Product type OCF and payment --->"
                              // +
                              // accpt);
                              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                              Calendar cal = Calendar.getInstance();
                              getPane().setSIGVALDT("");

                              String addnatt = getWrapper().getADDNATT().trim();
                              // //Loggers.general().info(LOG,"Additional notional transit
                              // date--->" +
                              // addnatt);
                              // Loggers.general().info(LOG,"OCFF");
                              String indcateval = getWrapper().getINDNDDT().trim();
                              // Loggers.general().info(LOG,"indcateval---->"+indcateval.length()+"---->indcateval--->"+indcateval);
                              // //Loggers.general().info(LOG,"Notional incator value--->" +
                              // indcateval);
                              getPane().setNATTRPRD("25");

                              Integer gr = 0;
                              Integer gr1 = 0;
                              String natt_set1 = getWrapper().getNATTRPRD().trim();
                              String natt_set = getPane().getNATTRPRD().trim();
                              LOG.info("Notional trasit period--->" + natt_set1);
                              LOG.info("Notional trasit period--->" + natt_set);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Notional trasit period--->" + natt_set);
                              }
                              if (natt_set.length() > 0) {
                                    gr = Integer.parseInt(natt_set);
                              }
                              if (addnatt.length() > 0) {
                                    gr1 = Integer.parseInt(addnatt);
                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Notional integer--->" + gr1);
                              }

                              if (indcateval.equalsIgnoreCase("INCREASE")) {

                                    try {
                                          Integer gra = gr + gr1;
                                          // //Loggers.general().info(LOG,"Notional trasit period for
                                          // OCF
                                          // payment increase INCREASE--->" + gra);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional trasit period for OCF payment increase INCREASE--->" + gra);
                                          }
                                          cal.setTime(sdf.parse(systemDate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");

                                          cal.add(Calendar.DATE, gra);
                                          String output = sdf.format(cal.getTime());
                                          // //Loggers.general().info(LOG,"output----->" + output);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional trasit period for OCF payment increase INCREASE output--->" + output);
                                          }
                                          getPane().setSIGVALDT(output);

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Notional value date OCF and
                                          // payment--->" + e.getMessage());
                                    }
                              } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                    Integer gra = gr - gr1;
                                    // //Loggers.general().info(LOG,"Notional trasit period for OCF
                                    // payment decrease--->" + gra);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Notional trasit period for OCF payment decrease--->" + gra);
                                    }
                                    try {
                                          cal.setTime(sdf.parse(systemDate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          cal.add(Calendar.DATE, gra);
                                          String output = sdf.format(cal.getTime());
                                          // //Loggers.general().info(LOG,"output----->" + output);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional trasit period for OCF payment decrease--->" + output);
                                          }
                                          getPane().setSIGVALDT(output);

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Notional value date OCF and
                                          // payment DECREASE--->" + e.getMessage());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional value date OCF and payment DECREASE--->" + e.getMessage());
                                          }
                                    }

                              }

                        } else if ((prd_typ.equalsIgnoreCase("OCI"))) {
                              // //Loggers.general().info(LOG,"Product type OCI and payment --->"
                              // +
                              // accpt);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Product type OCI and payment --->" + accpt);
                              }

                              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                              Calendar cal = Calendar.getInstance();
                              getPane().setSIGVALDT("");

                              String addnatt = getWrapper().getADDNATT().trim();
                              // //Loggers.general().info(LOG,"Additional notional transit
                              // date--->" +
                              // addnatt);
                              // Loggers.general().info(LOG,"OCI");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Additional notional transit date--->" + addnatt);
                              }
                              String indcateval = getWrapper().getINDNDDT().trim();
                              // Loggers.general().info(LOG,"indcateval---->"+indcateval.length()+"---->indcateval--->"+indcateval);
                              // //Loggers.general().info(LOG,"Notional incator value--->" +
                              // indcateval);
                              getPane().setNATTRPRD("10");

                              Integer gr = 0;
                              Integer gr1 = 0;
                              String natt_set1 = getWrapper().getNATTRPRD().trim();
                              String natt_set = getPane().getNATTRPRD().trim();
                              LOG.info("Notional trasit period OCI and payment--->" + natt_set1);
                              LOG.info("Notional trasit period OCI and payment--->" + natt_set);
                              if (natt_set.length() > 0) {
                                    gr = Integer.parseInt(natt_set);
                              }
                              if (addnatt.length() > 0) {
                                    gr1 = Integer.parseInt(addnatt);
                              }

                              if (indcateval.equalsIgnoreCase("INCREASE")) {

                                    try {
                                          Integer gra = gr + gr1;
                                          // //Loggers.general().info(LOG,"Notional trasit period for
                                          // OCI
                                          // and payment increase--->" + gra);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional trasit period for OCI and payment increase--->" + gra);
                                          }
                                          cal.setTime(sdf.parse(systemDate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          cal.add(Calendar.DATE, gra);
                                          String output = sdf.format(cal.getTime());
                                          // //Loggers.general().info(LOG,"output----->" + output);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional trasit period for OCI and payment increase--->" + output);
                                          }
                                          getPane().setSIGVALDT(output);

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Notional value date OCI and
                                          // payment INCREASE--->" + e.getMessage());
                                    }
                              } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                    Integer gra = gr - gr1;
                                    // //Loggers.general().info(LOG,"Notional trasit period for OCI
                                    // and
                                    // payment decrease--->" + gra);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,
                                                      "Notional trasit period for OCI and payment decrease--->" + gra);
                                    }
                                    try {
                                          cal.setTime(sdf.parse(systemDate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          cal.add(Calendar.DATE, gra);
                                          String output = sdf.format(cal.getTime());
                                          // //Loggers.general().info(LOG,"output----->" + output);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional trasit period for OCI and payment decrease--->" + output);
                                          }
                                          getPane().setSIGVALDT(output);

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Notional value date OCI and
                                          // payment DECREASE--->" + e.getMessage());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional value date OCI and payment DECREASE--->" + e.getMessage());
                                          }
                                    }

                              }

                        }

                  } else if (accpt.equalsIgnoreCase("Acceptance")) {

                        String tenorStart = getDriverWrapper().getEventFieldAsText("FCO:sFRM", "s", "").toString();
                        String notionalDate = getDriverWrapper().getEventFieldAsText("FCO:sMD", "d", "");
                        // //Loggers.general().info(LOG,"Product type OCF and Acceptance --->" +
                        // notionalDate);
                        String output = null;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                        Calendar cal = Calendar.getInstance();
                        if (prd_typ.equalsIgnoreCase("OCF")) {
                              // //Loggers.general().info(LOG,"Product type OCF and Acceptance
                              // --->" +
                              // accpt);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Product type OCF and Acceptance --->" + accpt);
                              }
                              getPane().setSIGVALDT("");

                              String addnatt = getWrapper().getADDNATT().trim();
                              // //Loggers.general().info(LOG,"Additional notional transit
                              // date--->" +
                              // addnatt);
                              // Loggers.general().info(LOG,"OCF Second one");
                              String indcateval = getWrapper().getINDNDDT().trim();
                              // Loggers.general().info(LOG,"indcateval---->"+indcateval.length()+"---->indcateval--->"+indcateval);
                              // //Loggers.general().info(LOG,"Notional incator value--->" +
                              // indcateval);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "indcateval---->" + indcateval.length() + "---->indcateval--->" + indcateval);
                              }
                              getPane().setNATTRPRD("0");

                              Integer gr = 0;
                              Integer gr1 = 0;
                              String natt_set1 = getWrapper().getNATTRPRD().trim();
                              String natt_set = getPane().getNATTRPRD().trim();
                              LOG.info("Notional => " + natt_set1);
                              LOG.info("Notional  1 => " + natt_set);
                              if (natt_set.length() > 0) {
                                    gr = Integer.parseInt(natt_set);
                              }
                              if (addnatt.length() > 0) {
                                    gr1 = Integer.parseInt(addnatt);
                              }
                              if (indcateval.equalsIgnoreCase("INCREASE")) {
                                    try {
                                          Integer gra = gr + gr1;
                                          cal.setTime(sdf.parse(notionalDate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          cal.add(Calendar.DATE, gra);
                                          output = sdf.format(cal.getTime());
                                          // //Loggers.general().info(LOG,"output----->" + output);
                                          // getPane().setSIGVALDT(output);

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Notional value date OCF and
                                          // Acceptance INCREASE--->" + e.getMessage());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional value date OCF and Acceptance INCREASE--->" + e.getMessage());
                                          }
                                    }
                              } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                    try {
                                          Integer gra = gr - gr1;
                                          cal.setTime(sdf.parse(notionalDate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          cal.add(Calendar.DATE, gra);
                                          output = sdf.format(cal.getTime());
                                          // //Loggers.general().info(LOG,"output----->" + output);
                                          // getPane().setSIGVALDT(output);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Notional value date OCF and Acceptance INCREASE output--->" + output);
                                          }

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Notional value date OCF and
                                          // Acceptance DECREASE--->" + e.getMessage());
                                    }
                              }
                              // //Loggers.general().info(LOG,"Value of selected tenorStart " +
                              // tenorStart);

                              if (tenorStart.trim().equalsIgnoreCase("Air waybill")) {

                                    getPane().setSIGVALDT(output);

                              } else if (tenorStart.trim().equalsIgnoreCase("Bill of Exchange")) {

                                    getPane().setSIGVALDT(output);

                              } else if (tenorStart.trim().equalsIgnoreCase("Bill of Lading")) {

                                    getPane().setSIGVALDT(output);

                              } else if (tenorStart.trim().equalsIgnoreCase("Invoice")) {

                                    getPane().setSIGVALDT(output);

                              } else if (tenorStart.trim().equalsIgnoreCase("Shipment Date")) {

                                    getPane().setSIGVALDT(output);

                              }

                              else if (tenorStart.trim().equalsIgnoreCase("Arrival of goods")
                                          || tenorStart.trim().equalsIgnoreCase("O") || tenorStart.trim().equalsIgnoreCase("Sight")) {
                                    // //Loggers.general().info(LOG,"Product type OCF and Acceptance
                                    // --->"
                                    // + accpt);
                                    String output_notional = null;
                                    getPane().setSIGVALDT("");
                                    String addnotional = getWrapper().getADDNATT().trim();
                                    // //Loggers.general().info(LOG,"Additional notional transit
                                    // date--->"
                                    // + addnotional);
                                    String indcat = getWrapper().getINDNDDT().trim();
                                    // //Loggers.general().info(LOG,"Notional incator value--->" +
                                    // indcat);
                                    getPane().setNATTRPRD("25");

                                    Integer grr = 0;
                                    Integer grr1 = 0;

                                    String notionalperd1 = getWrapper().getNATTRPRD().trim();

                                    String notionalperd = getPane().getNATTRPRD().trim();
                                    LOG.info("notionalperd " + notionalperd);
                                    LOG.info("notionalperd  1  " + notionalperd1);
                                    if (notionalperd.length() > 0) {
                                          grr = Integer.parseInt(notionalperd);
                                    }
                                    if (addnotional.length() > 0) {
                                          grr1 = Integer.parseInt(addnotional);
                                    }
                                    if (indcat.equalsIgnoreCase("INCREASE")) {

                                          try {
                                                Integer graa = grr + grr1;
                                                // //Loggers.general().info(LOG,"Notional trasit period
                                                // Finally--->" + graa);
                                                cal.setTime(sdf.parse(notionalDate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, graa);
                                                output_notional = sdf.format(cal.getTime());
                                                // //Loggers.general().info(LOG,"output_notional----->"
                                                // +
                                                // output_notional);
                                                getPane().setSIGVALDT(output_notional);

                                          } catch (Exception e) {

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,
                                                                  "Notional value date OCF and Acceptance INCREASE 3--->" + e.getMessage());
                                                }
                                          }
                                    } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                          try {
                                                Integer graa = grr - grr1;
                                                // //Loggers.general().info(LOG,"Notional trasit period
                                                // Finally--->" + graa);
                                                cal.setTime(sdf.parse(notionalDate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, graa);
                                                output_notional = sdf.format(cal.getTime());
                                                // //Loggers.general().info(LOG,"output_notional----->"
                                                // +
                                                // output_notional);
                                                getPane().setSIGVALDT(output_notional);

                                          } catch (Exception e) {

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,
                                                                  "Notional value date OCF and Acceptance DECREASE 3--->" + e.getMessage());
                                                }
                                          }

                                    }
                              }

                        } else if (prd_typ.equalsIgnoreCase("OCI")) {
                              // //Loggers.general().info(LOG,"Product type OCI and Acceptance
                              // --->" +
                              // accpt);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Product type OCI and Acceptance --->" + accpt);
                              }
                              getPane().setSIGVALDT("");
                              String addnotional = getWrapper().getADDNATT().trim();
                              // //Loggers.general().info(LOG,"Additional notional transit
                              // date--->" +
                              // addnotional);
                              String indcat = getWrapper().getINDNDDT().trim();
                              // //Loggers.general().info(LOG,"Notional incator value--->" +
                              // indcat);
                              getPane().setNATTRPRD("0");

                              Integer grr = 0;
                              Integer grr1 = 0;

                              String notionalperd1 = getWrapper().getNATTRPRD().trim();
                              String notionalperd = getPane().getNATTRPRD().trim();
                              LOG.info("notionalperd1======== " + notionalperd1);
                              LOG.info("notionalperd ============" + notionalperd);
                              if (notionalperd.length() > 0) {
                                    grr = Integer.parseInt(notionalperd);
                              }
                              if (addnotional.length() > 0) {
                                    grr1 = Integer.parseInt(addnotional);
                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    LOG.info("Product type OCI and Integer --->" + grr1);
                              }
                              if (indcat.equalsIgnoreCase("INCREASE")) {
                                    try {
                                          Integer gra = grr + grr1;
                                          // //Loggers.general().info(LOG,"Notional trasit period
                                          // Finally--->" + gra);
                                          cal.setTime(sdf.parse(notionalDate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          cal.add(Calendar.DATE, gra);
                                          output = sdf.format(cal.getTime());
                                          // //Loggers.general().info(LOG,"output----->" + output);
                                          // getPane().setSIGVALDT(output);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                LOG.info("Product type OCI output --->" + output);
                                          }

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Notional value date OCI and
                                          // Acceptance INCREASE--->" + e.getMessage());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional value date OCI and Acceptance INCREASE--->" + e.getMessage());
                                          }
                                    }

                              } else if (indcat.equalsIgnoreCase("DECREASE")) {

                                    try {
                                          Integer gra = grr - grr1;
                                          // //Loggers.general().info(LOG,"Notional trasit period
                                          // Finally--->" + gra);
                                          cal.setTime(sdf.parse(notionalDate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          cal.add(Calendar.DATE, gra);
                                          output = sdf.format(cal.getTime());
                                          // //Loggers.general().info(LOG,"output----->" + output);
                                          // getPane().setSIGVALDT(output);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                LOG.info("Product type OCI DECREASE output --->" + output);
                                          }

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Notional value date OCI and
                                          // Acceptance DECREASE--->" + e.getMessage());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,
                                                            "Notional value date OCI and Acceptance DECREASE--->" + e.getMessage());
                                          }
                                    }

                              }

                              // //Loggers.general().info(LOG,"Value of selected tenorStart " +
                              // tenorStart);

                              if (tenorStart.trim().equalsIgnoreCase("Air waybill")) {

                                    getPane().setSIGVALDT(output);

                              } else if (tenorStart.trim().equalsIgnoreCase("Bill of Exchange")) {

                                    getPane().setSIGVALDT(output);

                              } else if (tenorStart.trim().equalsIgnoreCase("Bill of Lading")) {

                                    getPane().setSIGVALDT(output);

                              } else if (tenorStart.trim().equalsIgnoreCase("Invoice")) {

                                    getPane().setSIGVALDT(output);

                              } else if (tenorStart.trim().equalsIgnoreCase("Shipment Date")) {

                                    getPane().setSIGVALDT(output);

                              }

                              else if (tenorStart.trim().equalsIgnoreCase("Arrival of goods")
                                          || tenorStart.trim().equalsIgnoreCase("O") || tenorStart.trim().equalsIgnoreCase("Sight")) {
                                    String output_notional = null;
                                    // //Loggers.general().info(LOG,"Product type OCI and Acceptance
                                    // in
                                    // others--->" + accpt);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Product type OCI and Acceptance in others--->" + accpt);
                                    }
                                    getPane().setSIGVALDT("");
                                    String addnatt = getWrapper().getADDNATT().trim();
                                    // //Loggers.general().info(LOG,"Additional notional transit
                                    // date--->"
                                    // + addnatt);
                                    // Loggers.general().info(LOG,"Arrival of goods");
                                    String indcateval = getWrapper().getINDNDDT().trim();
                                    // Loggers.general().info(LOG,"indcateval---->"+indcateval.length()+"---->indcateval--->"+indcateval);
                                    // //Loggers.general().info(LOG,"Notional incator value--->" +
                                    // indcateval);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,
                                                      "indcateval---->" + indcateval.length() + "---->indcateval--->" + indcateval);
                                    }
                                    getPane().setNATTRPRD("10");

                                    Integer gr = 0;
                                    Integer gr1 = 0;
                                    String natt_set1 = getWrapper().getNATTRPRD().trim();
                                    String natt_set = getPane().getNATTRPRD().trim();
                                    LOG.info("natt_set1   ========" + natt_set1);
                                    LOG.info("natt_set   ========" + natt_set);
                                    if (natt_set.length() > 0) {
                                          gr = Integer.parseInt(natt_set);
                                    }
                                    if (addnatt.length() > 0) {
                                          gr1 = Integer.parseInt(addnatt);
                                    }
                                    if (indcateval.equalsIgnoreCase("INCREASE")) {
                                          try {
                                                Integer gra = gr + gr1;
                                                cal.setTime(sdf.parse(notionalDate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                output = sdf.format(cal.getTime());
                                                // //Loggers.general().info(LOG,"output----->" +
                                                // output);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,
                                                                  "Product type OCI and Acceptance in INCREASE--->" + output);
                                                }
                                                getPane().setSIGVALDT(output);

                                          } catch (Exception e) {
                                                Loggers.general().info(LOG,
                                                            "Notional value date OCI and Acceptance INCREASE 3--->" + e.getMessage());
                                          }
                                    } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                          try {
                                                Integer gra = gr - gr1;
                                                cal.setTime(sdf.parse(notionalDate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                output = sdf.format(cal.getTime());
                                                // //Loggers.general().info(LOG,"output----->" +
                                                // output);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,
                                                                  "Product type OCI and Acceptance in DECREASE--->" + output);
                                                }
                                                getPane().setSIGVALDT(output);

                                          } catch (Exception e) {

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,
                                                                  "Notional value date OCI and Acceptance DECREASE 3--->" + e.getMessage());
                                                }
                                          }
                                    }

                              }

                        }

                  } else {
                        // Loggers.general().info(LOG,"Payment action is others --->" + accpt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("Payment action is others --->" + accpt);
                        }
                  }
            } catch (Exception e) {

                  LOG.info("Exception in notional rate in ODC" + e.getMessage());

            }

      }

      public void getNotionalDueDateIDC() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            // //Loggers.general().info(LOG,"Product type OCF and payment --->" +
            // accpt);
            try {
                  String mixpay_fco = getDriverWrapper().getEventFieldAsText("FCO:sROS", "s", "");
                  String accpt = getDriverWrapper().getEventFieldAsText("RELS", "s", "");
                  String mixedpay = getDriverWrapper().getEventFieldAsText("MPT", "l", "");
                  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  Calendar cal = Calendar.getInstance();
                  getPane().setSIGVALDT("");

                  // //Loggers.general().info(LOG,"ACCEPTANCE VALUE --->" + accpt);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        LOG.info("ACCEPTANCE VALUE IDC--->" + accpt);
                  }
                  if (accpt.equalsIgnoreCase("Payment")) {

                        String systemDate = getDriverWrapper().getEventFieldAsText("RVD", "d", "");

                        String addnatt = getWrapper().getADDNATT().trim();
                        // //Loggers.general().info(LOG,"Additional notional transit date--->" +
                        // addnatt);
                        // Loggers.general().info(LOG,"Payment");
                        String indcateval = getWrapper().getINDNDDT().trim();
                        // Loggers.general().info(LOG,"indcateval---->"+indcateval.length()+"---->indcateval--->"+indcateval);
                        // //Loggers.general().info(LOG,"Notional incator value--->" +
                        // indcateval);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,
                                          "indcateval IDC---->" + indcateval.length() + "---->indcateval--->" + indcateval);

                        }
                        getPane().setNATTRPRD("");

                        Integer gr = 0;
                        Integer gr1 = 0;
                        String natt_set1 = getWrapper().getNATTRPRD().trim();
                        String natt_set = getPane().getNATTRPRD().trim();
                        // //Loggers.general().info(LOG,"Notional trasit period--->" +
                        // natt_set);
                        if (natt_set.length() > 0) {
                              gr = Integer.parseInt(natt_set);
                        }
                        if (addnatt.length() > 0) {
                              gr1 = Integer.parseInt(addnatt);
                        }

                        if (indcateval.equalsIgnoreCase("INCREASE")) {

                              try {
                                    Integer gra = gr + gr1;
                                    // //Loggers.general().info(LOG,"Notional trasit period for OCF
                                    // payment increase INCREASE--->" + gra);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Notional trasit period for IDC payment increase INCREASE--->" + gra);

                                    }
                                    cal.setTime(sdf.parse(systemDate));
                                    // //Loggers.general().info(LOG,"expdate in issue-------> ");
                                    cal.add(Calendar.DATE, gra);
                                    String output = sdf.format(cal.getTime());
                                    // //Loggers.general().info(LOG,"output----->" + output);
                                    getPane().setSIGVALDT(output);

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Notional value date OCF and
                                    // payment--->" + e.getMessage());
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Notional value date IDC and payment--->" + e.getMessage());

                                    }
                              }
                        } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                              Integer gra = gr - gr1;
                              // //Loggers.general().info(LOG,"Notional trasit period for OCF
                              // payment decrease--->" + gra);
                              try {
                                    cal.setTime(sdf.parse(systemDate));
                                    // //Loggers.general().info(LOG,"expdate in issue-------> ");
                                    cal.add(Calendar.DATE, gra);
                                    String output = sdf.format(cal.getTime());
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Notional trasit period for IDC payment increase DECREASE--->" + output);

                                    }
                                    getPane().setSIGVALDT(output);

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Notional value date OCF and
                                    // payment
                                    // DECREASE--->" + e.getMessage());
                              }

                        }
                  } else if (mixedpay.equalsIgnoreCase("Y")) {
                        String systemDate = getDriverWrapper().getEventFieldAsText("RVD", "d", "");
                        String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                        try {
                              con = ConnectionMaster.getConnection();
                              String query = "select cold.DRAFT_TYP from master mas, TIDATAITEM tid, COLLDRAFT cold where mas.KEY97 = tid.MASTER_KEY and tid.KEY97 = cold.KEY97 and cold.DRAFT_TYP <> 'A' and mas.MASTER_REF = '"
                                          + masterref + "' group by cold.DRAFT_TYP";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Notional due date for IDC--->" + query);

                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              if (rs1.next()) {
                                    String addnatt = getWrapper().getADDNATT().trim();
                                    // //Loggers.general().info(LOG,"Additional notional transit
                                    // date--->" +
                                    // addnatt);
                                    // Loggers.general().info(LOG,"Payment");
                                    String indcateval = getWrapper().getINDNDDT().trim();
                                    // Loggers.general().info(LOG,"indcateval---->"+indcateval.length()+"---->indcateval--->"+indcateval);
                                    // //Loggers.general().info(LOG,"Notional incator value--->" +
                                    // indcateval);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "indcateval---->" + indcateval.length() + "---->indcateval--->" + indcateval);

                                    }
                                    getPane().setNATTRPRD("");

                                    Integer gr = 0;
                                    Integer gr1 = 0;
                                    String natt_set1 = getWrapper().getNATTRPRD().trim();
                                    String natt_set = getPane().getNATTRPRD().trim();

                                    // //Loggers.general().info(LOG,"Notional trasit period--->" +
                                    // natt_set);
                                    if (natt_set.length() > 0) {
                                          gr = Integer.parseInt(natt_set);
                                    }
                                    if (addnatt.length() > 0) {
                                          gr1 = Integer.parseInt(addnatt);
                                    }

                                    if (indcateval.equalsIgnoreCase("INCREASE")) {

                                          try {
                                                Integer gra = gr + gr1;
                                                // //Loggers.general().info(LOG,"Notional trasit period
                                                // for
                                                // OCF
                                                // payment increase INCREASE--->" + gra);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,
                                                                  "Notional trasit period for IDC payment increase INCREASE--->" + gra);

                                                }
                                                cal.setTime(sdf.parse(systemDate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                String output = sdf.format(cal.getTime());
                                                // //Loggers.general().info(LOG,"output----->" +
                                                // output);
                                                getPane().setSIGVALDT(output);
                                                getWrapper().setSIGVALDT(output);

                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"Notional value date OCF
                                                // and
                                                // payment--->" + e.getMessage());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,
                                                                  "Notional value date IDC and payment--->" + e.getMessage());

                                                }
                                          }
                                    } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                          Integer gra = gr - gr1;
                                          // //Loggers.general().info(LOG,"Notional trasit period for
                                          // OCF
                                          // payment decrease--->" + gra);
                                          try {
                                                cal.setTime(sdf.parse(systemDate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                String output = sdf.format(cal.getTime());
                                                // //Loggers.general().info(LOG,"output----->" +
                                                // output);
                                                getPane().setSIGVALDT(output);

                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"Notional value date IDC
                                                // and
                                                // payment
                                                // DECREASE--->" + e.getMessage());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,
                                                                  "Notional value date IDC and payment DECREASE--->" + e.getMessage());

                                                }
                                          }

                                    }

                              }

                              else {
                                    getPane().setSIGVALDT("");
                                    getWrapper().setSIGVALDT("");
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception of BASEDATE " +
                              // e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Exception of BASEDATE " + e.getMessage());

                              }
                        } finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,"Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {

                        getPane().setSIGVALDT("");
                        getWrapper().setSIGVALDT("");

                  }

            } catch (Exception e) {

                  LOG.info("Exception in notional rate in IDC" + e.getMessage());

            }
      }

      // Notional due date elc new
      public void getNotionalDueDateELC() {
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {
                  if (getMajorCode().equalsIgnoreCase("ELC")) {
                        String sight = getDriverWrapper().getEventFieldAsText("FPP:XPTS", "s", "");
                        String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                        String mixedpay = getDriverWrapper().getEventFieldAsText("MIX", "l", "");
                        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yy");
                        Calendar call = Calendar.getInstance();
                        Integer gra = 0;
                        String addnatt = getWrapper().getADDNATT().trim();
                        String indcateval = getWrapper().getINDNDDT().trim();
                        String natt_set1 = getWrapper().getNATTRPRD().trim();
                        String natt_set = getPane().getNATTRPRD().trim();

                        Integer gr = 0;
                        Integer gr1 = 0;

                        // //Loggers.general().info(LOG,"Payment details sight --->" + sight);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Payment details sight --->" + sight);

                        }
                        if (sight.equalsIgnoreCase("Sight")) {
                              String payval = getDriverWrapper().getEventFieldAsText("PRD", "d", "");

                              if (getMajorCode().equalsIgnoreCase("ELC") && prd_typ.equalsIgnoreCase("ELF")) {

                                    getPane().setNATTRPRD("25");

                                    if (natt_set.length() > 0) {
                                          gr = Integer.parseInt(natt_set);
                                    }
                                    if (addnatt.length() > 0) {
                                          gr1 = Integer.parseInt(addnatt);
                                    }

                                    if (indcateval.equalsIgnoreCase("INCREASE")) {
                                          if (payval.length() > 0 && payval != null && getMinorCode().equalsIgnoreCase("DOP")) {
                                                try {
                                                      gra = gr + gr1;
                                                      call.setTime(sdf3.parse(payval));
                                                      // //Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      call.add(Calendar.DATE, gra);
                                                      String output = sdf3.format(call.getTime());
                                                      /// //Loggers.general().info(LOG,"Notional output
                                                      /// Sight
                                                      /// ELF----->" + output);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,
                                                                        "Notional output Sight ELF INCREASE----->" + output);

                                                      }
                                                      getPane().setSIGVALDT(output);
                                                      getWrapper().setSIGVALDT(output);

                                                } catch (Exception e) {

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,
                                                                        "Exception Notional output Sight ELF INCREASE----->" + e.getMessage());

                                                      }

                                                }
                                          } else {
                                                // //Loggers.general().info(LOG,"Notional transit
                                                // period--->" +
                                                // natt + "payval" + payval);

                                          }
                                    } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                          if (payval.length() > 0 && payval != null && getMinorCode().equalsIgnoreCase("DOP")) {

                                                try {
                                                      gra = gr - gr1;
                                                      call.setTime(sdf3.parse(payval));
                                                      // //Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      call.add(Calendar.DATE, gra);
                                                      String output = sdf3.format(call.getTime());
                                                      /// //Loggers.general().info(LOG,"Notional output
                                                      /// Sight
                                                      /// ELF----->" + output);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,
                                                                        "Notional output Sight ELF DECREASE----->" + output);

                                                      }
                                                      getPane().setSIGVALDT(output);
                                                      getWrapper().setSIGVALDT(output);

                                                } catch (Exception e) {
                                                      // Loggers.general().info(LOG,"Notional value date
                                                      // OCF
                                                      // and
                                                      // payment
                                                      // DECREASE--->" + e.getMessage());
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,
                                                                        "Notional value date OCF and payment DECREASE--->" + e.getMessage());

                                                      }
                                                }

                                          }
                                    }
                              } else if (getMajorCode().equalsIgnoreCase("ELC") && prd_typ.equalsIgnoreCase("ELD")) {

                                    getPane().setNATTRPRD("10");

                                    if (natt_set.length() > 0) {
                                          gr = Integer.parseInt(natt_set);
                                    }
                                    if (addnatt.length() > 0) {
                                          gr1 = Integer.parseInt(addnatt);
                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Notional Sight ELD DECREASE----->" + gr1);

                                    }
                                    if (indcateval.equalsIgnoreCase("INCREASE")) {
                                          if (payval.length() > 0 && payval != null && getMinorCode().equalsIgnoreCase("DOP")) {
                                                try {
                                                      gra = gr + gr1;
                                                      call.setTime(sdf3.parse(payval));
                                                      // //Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      call.add(Calendar.DATE, gra);
                                                      String output = sdf3.format(call.getTime());
                                                      getPane().setSIGVALDT(output);
                                                      getWrapper().setSIGVALDT(output);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            LOG.info("Notional Sight ELD INCREASE----->" + output);

                                                      }
                                                } catch (Exception e) {
                                                      // Loggers.general().info(LOG,"Notional value date
                                                      // --->"
                                                      // +
                                                      // e.getMessage());
                                                }
                                          } else {

                                          }
                                    } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                          if (payval.length() > 0 && payval != null && getMinorCode().equalsIgnoreCase("DOP")) {

                                                try {
                                                      gra = gr - gr1;
                                                      call.setTime(sdf3.parse(payval));
                                                      // //Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      call.add(Calendar.DATE, gra);
                                                      String output = sdf3.format(call.getTime());
                                                      /// ELF----->" + output);
                                                      getPane().setSIGVALDT(output);
                                                      getWrapper().setSIGVALDT(output);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            LOG.info("Notional Sight ELD DECREASE----->" + output);

                                                      }
                                                } catch (Exception e) {

                                                }

                                          } else {
                                                //// Loggers.general().info(LOG,"Notional due date is
                                                //// not
                                                //// ELC");
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      LOG.info("Notional due date is not ELC");

                                                }
                                          }
                                    }
                              }

                        } else if (mixedpay.equalsIgnoreCase("Y")) {
                              String payval = getDriverWrapper().getEventFieldAsText("PRD", "d", "");
                              String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                              String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                              try {
                                    con = ConnectionMaster.getConnection();
                                    String query = "SELECT pap.TYPE FROM master mas, BASEEVENT bev, LCPAYMENT lcp , PARTPAYMNT pap WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = lcp.KEY97 AND lcp.KEY97 = pap.PAYEV_KEY and pap.TYPE <> 'A' AND mas.MASTER_REF = '"
                                                + masterref + "' and bev.REFNO_PFIX = 'DPR' and bev.REFNO_SERL = '" + evvcount
                                                + "' group by pap.TYPE";
                                    // Loggers.general().info(LOG,"Value date in Mixed lc---> " +
                                    // query);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Value date in Mixed lc---> " + query);

                                    }

                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    if (rs1.next()) {

                                          if (getMajorCode().equalsIgnoreCase("ELC") && prd_typ.equalsIgnoreCase("ELF")) {

                                                getPane().setNATTRPRD("25");

                                                if (natt_set.length() > 0) {
                                                      gr = Integer.parseInt(natt_set);
                                                }
                                                if (addnatt.length() > 0) {
                                                      gr1 = Integer.parseInt(addnatt);
                                                }

                                                if (indcateval.equalsIgnoreCase("INCREASE")) {
                                                      if (payval.length() > 0 && payval != null
                                                                  && getMinorCode().equalsIgnoreCase("DOP")) {
                                                            try {
                                                                  gra = gr + gr1;
                                                                  call.setTime(sdf3.parse(payval));
                                                                  // //Loggers.general().info(LOG,"expdate in
                                                                  // issue------->
                                                                  // ");
                                                                  call.add(Calendar.DATE, gra);
                                                                  String output = sdf3.format(call.getTime());
                                                                  /// //Loggers.general().info(LOG,"Notional
                                                                  /// output
                                                                  /// Sight
                                                                  /// ELF----->" + output);
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                        Loggers.general().info(LOG,
                                                                                    "Mixed lc Notional output Sight ELF INCREASE----->" + output);

                                                                  }
                                                                  getPane().setSIGVALDT(output);
                                                                  getWrapper().setSIGVALDT(output);

                                                            } catch (Exception e) {
                                                                  // Loggers.general().info(LOG,"Notional
                                                                  // value
                                                                  // date
                                                                  // --->" +
                                                                  // e.getMessage());
                                                            }
                                                      } else {
                                                            // //Loggers.general().info(LOG,"Notional
                                                            // transit
                                                            // period--->" +
                                                            // natt + "payval" + payval);

                                                      }
                                                } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                                      if (payval.length() > 0 && payval != null
                                                                  && getMinorCode().equalsIgnoreCase("DOP")) {

                                                            try {
                                                                  gra = gr - gr1;
                                                                  call.setTime(sdf3.parse(payval));
                                                                  // //Loggers.general().info(LOG,"expdate in
                                                                  // issue------->
                                                                  // ");
                                                                  call.add(Calendar.DATE, gra);
                                                                  String output = sdf3.format(call.getTime());
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                        Loggers.general().info(LOG,
                                                                                    "Mixed lc Notional output Sight ELF DECREASE----->" + output);

                                                                  }
                                                                  getPane().setSIGVALDT(output);
                                                                  getWrapper().setSIGVALDT(output);

                                                            } catch (Exception e) {
                                                                  // Loggers.general().info(LOG,"Notional
                                                                  // value
                                                                  // date
                                                                  // OCF and
                                                                  // payment
                                                                  // DECREASE--->" + e.getMessage());
                                                            }

                                                      }
                                                }
                                          } else if (getMajorCode().equalsIgnoreCase("ELC") && prd_typ.equalsIgnoreCase("ELD")) {

                                                getPane().setNATTRPRD("10");

                                                if (natt_set.length() > 0) {
                                                      gr = Integer.parseInt(natt_set);
                                                }
                                                if (addnatt.length() > 0) {
                                                      gr1 = Integer.parseInt(addnatt);
                                                }

                                                if (indcateval.equalsIgnoreCase("INCREASE")) {
                                                      if (payval.length() > 0 && payval != null
                                                                  && getMinorCode().equalsIgnoreCase("DOP")) {
                                                            try {
                                                                  gra = gr + gr1;
                                                                  call.setTime(sdf3.parse(payval));
                                                                  // //Loggers.general().info(LOG,"expdate in
                                                                  // issue------->
                                                                  // ");
                                                                  call.add(Calendar.DATE, gra);
                                                                  String output = sdf3.format(call.getTime());
                                                                  getPane().setSIGVALDT(output);
                                                                  getWrapper().setSIGVALDT(output);
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                        Loggers.general().info(LOG,
                                                                                    "Mixed lc Notional output Sight ELD INCREASE----->" + output);

                                                                  }
                                                            } catch (Exception e) {
                                                                  // Loggers.general().info(LOG,"Notional
                                                                  // value
                                                                  // date
                                                                  // --->" +
                                                                  // e.getMessage());
                                                            }
                                                      } else {

                                                      }
                                                } else if (indcateval.equalsIgnoreCase("DECREASE")) {
                                                      if (payval.length() > 0 && payval != null
                                                                  && getMinorCode().equalsIgnoreCase("DOP")) {

                                                            try {
                                                                  gra = gr - gr1;
                                                                  call.setTime(sdf3.parse(payval));
                                                                  // //Loggers.general().info(LOG,"expdate in
                                                                  // issue------->
                                                                  // ");
                                                                  call.add(Calendar.DATE, gra);
                                                                  String output = sdf3.format(call.getTime());
                                                                  /// ELF----->" + output);
                                                                  getPane().setSIGVALDT(output);
                                                                  getWrapper().setSIGVALDT(output);
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                        Loggers.general().info(LOG,
                                                                                    "Mixed lc Notional output Sight ELD DECREASE----->" + output);

                                                                  }

                                                            } catch (Exception e) {

                                                            }

                                                      } else {
                                                            //// Loggers.general().info(LOG,"Notional due
                                                            //// date
                                                            //// is
                                                            //// not ELC");
                                                      }
                                                }
                                          }

                                    } else {
                                          getPane().setSIGVALDT("");
                                          getWrapper().setSIGVALDT("");
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception of BASEDATE " +
                                    // e.getMessage());
                              } finally {
                                    try {
                                          if (rs1 != null)
                                                rs1.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();
                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }

                        } else {
                              getPane().setSIGVALDT("");
                              getWrapper().setSIGVALDT("");
                        }
                  }
            } catch (Exception e) {

                  LOG.info("Exception in notional rate in ELC" + e.getMessage());

            }
      }

      // Notional due date ILC new
      public void getNotionalDueDateILC() {
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {
                  if (getMajorCode().equalsIgnoreCase("ILC")) {
                        String sight = getDriverWrapper().getEventFieldAsText("FPP:XPTS", "s", "");
                        String mixedpay = getDriverWrapper().getEventFieldAsText("MIX", "l", "");

                        // Notional due date ILC new
                        if (sight.equalsIgnoreCase("Sight")) {

                              String payval = getDriverWrapper().getEventFieldAsText("PRD", "d", "");

                              if (payval.length() > 0 && payval != null && getMajorCode().equalsIgnoreCase("ILC")
                                          && getMinorCode().equalsIgnoreCase("CRC")) {
                                    // //Loggers.general().info(LOG,"Payment value date --->" +
                                    // payval);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Sight ILC ---->" + sight);

                                    }
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yy");
                                    Calendar call = Calendar.getInstance();
                                    getPane().setNATTRPRD("7");
                                    Integer gra = 0;
                                    String addnatt = getWrapper().getADDNATT().trim();
                                    String indcateval = getWrapper().getINDNDDT().trim();
                                    String natt_set1 = getWrapper().getNATTRPRD().trim();
                                    String natt_set = getPane().getNATTRPRD().trim();

                                    Integer gr = 0;
                                    Integer gr1 = 0;
                                    if (natt_set.length() > 0) {
                                          gr = Integer.parseInt(natt_set);
                                    }
                                    if (addnatt.length() > 0) {
                                          gr1 = Integer.parseInt(addnatt);
                                    }
                                    if (indcateval.equalsIgnoreCase("INCREASE")) {

                                          try {
                                                gra = gr + gr1;
                                                call.setTime(sdf3.parse(payval));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                call.add(Calendar.DATE, gra);
                                                String output = sdf3.format(call.getTime());
                                                // //Loggers.general().info(LOG,"Notional output Sight
                                                // ILC----->"
                                                // + output);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("Notional output Sight ILC----->" + output);

                                                }
                                                getPane().setSIGVALDT(output);
                                                getWrapper().setSIGVALDT(output);

                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"Notional value date --->"
                                                // +
                                                // e.getMessage());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("Notional Sight ILC INCREASE----->" + e.getMessage());

                                                }
                                          }
                                    } else if (indcateval.equalsIgnoreCase("DECREASE")) {

                                          try {
                                                gra = gr - gr1;
                                                call.setTime(sdf3.parse(payval));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                call.add(Calendar.DATE, gra);
                                                String output = sdf3.format(call.getTime());
                                                // //Loggers.general().info(LOG,"Notional output Sight
                                                // ILC----->"
                                                // + output);
                                                getPane().setSIGVALDT(output);
                                                getWrapper().setSIGVALDT(output);

                                          } catch (Exception e) {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("Notional Sight ILC DECREASE----->" + e.getMessage());

                                                }
                                          }
                                    }
                              } else {
                                    //// Loggers.general().info(LOG,"Notional transit period--->" +
                                    //// "payval"
                                    //// + payval);

                              }

                        }

                        else if (mixedpay.equalsIgnoreCase("Y")) {
                              String payval = getDriverWrapper().getEventFieldAsText("PRD", "d", "");
                              String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                              String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                              try {
                                    con = ConnectionMaster.getConnection();
                                    String query = "SELECT pap.TYPE FROM master mas, BASEEVENT bev, LCPAYMENT lcp , PARTPAYMNT pap WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = lcp.KEY97 AND lcp.KEY97 = pap.PAYEV_KEY and pap.TYPE <> 'A' AND mas.MASTER_REF = '"
                                                + masterref + "' and bev.REFNO_PFIX = 'CLM' and bev.REFNO_SERL = '" + evvcount
                                                + "' group by pap.TYPE";
                                    // Loggers.general().info(LOG,"Value date in Mixed lc---> " +
                                    // query);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          LOG.info("Value date in Mixed lc---> " + query);

                                    }

                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    if (rs1.next()) {

                                          if (payval != null && payval.length() > 0 && getMajorCode().equalsIgnoreCase("ILC")
                                                      && getMinorCode().equalsIgnoreCase("CRC")) {
                                                // //Loggers.general().info(LOG,"Payment value date
                                                // --->" +
                                                // payval);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      LOG.info("Mixed lc payval---> " + payval);

                                                }

                                                SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yy");
                                                Calendar call = Calendar.getInstance();
                                                getPane().setNATTRPRD("7");
                                                Integer gra = 0;
                                                String addnatt = getWrapper().getADDNATT().trim();
                                                String indcateval = getWrapper().getINDNDDT().trim();
                                                String natt_set1 = getWrapper().getNATTRPRD().trim();
                                                String natt_set = getPane().getNATTRPRD().trim();

                                                Integer gr = 0;
                                                Integer gr1 = 0;
                                                if (natt_set.length() > 0) {
                                                      gr = Integer.parseInt(natt_set);
                                                }
                                                if (addnatt.length() > 0) {
                                                      gr1 = Integer.parseInt(addnatt);
                                                }
                                                if (indcateval.equalsIgnoreCase("INCREASE")) {

                                                      try {
                                                            gra = gr + gr1;
                                                            call.setTime(sdf3.parse(payval));
                                                            // //Loggers.general().info(LOG,"expdate in
                                                            // issue-------> ");
                                                            call.add(Calendar.DATE, gra);
                                                            String output = sdf3.format(call.getTime());
                                                            // //Loggers.general().info(LOG,"Notional output
                                                            // Sight
                                                            // ILC----->"
                                                            // + output);

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  LOG.info("Notional output Sight ILC----->" + output);

                                                            }
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,
                                                                              "Notional output Sight ILC INCREASE----->" + output);

                                                            }
                                                            getPane().setSIGVALDT(output);
                                                            getWrapper().setSIGVALDT(output);

                                                      } catch (Exception e) {
                                                            // Loggers.general().info(LOG,"Notional value
                                                            // date
                                                            // --->"
                                                            // +
                                                            // e.getMessage());
                                                      }
                                                } else if (indcateval.equalsIgnoreCase("DECREASE")) {

                                                      try {
                                                            gra = gr - gr1;
                                                            call.setTime(sdf3.parse(payval));
                                                            // //Loggers.general().info(LOG,"expdate in
                                                            // issue-------> ");
                                                            call.add(Calendar.DATE, gra);
                                                            String output = sdf3.format(call.getTime());
                                                            // //Loggers.general().info(LOG,"Notional output
                                                            // Sight
                                                            // ILC----->"
                                                            // + output);
                                                            getPane().setSIGVALDT(output);
                                                            getWrapper().setSIGVALDT(output);

                                                      } catch (Exception e) {
                                                            // Loggers.general().info(LOG,"Notional value
                                                            // date
                                                            // --->"
                                                            // +
                                                            // e.getMessage());
                                                      }
                                                }
                                          } else {
                                                //// Loggers.general().info(LOG,"Notional transit
                                                //// period--->" +
                                                //// "payval"
                                                //// + payval);

                                          }

                                    } else {
                                          getPane().setSIGVALDT("");
                                          getWrapper().setSIGVALDT("");
                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception of BASEDATE " +
                                    // e.getMessage());
                              } finally {
                                    try {
                                          if (rs1 != null)
                                                rs1.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();
                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        } else {
                              getPane().setSIGVALDT("");
                              getWrapper().setSIGVALDT("");

                        }
                  }

            } catch (Exception e) {

                  LOG.info("Exception in notional rate in ILC" + e.getMessage());

            }
      }

      public void getfinaceNotionalDate() {
            // Notional due date population

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            if (getMajorCode().equalsIgnoreCase("FEL") || getMajorCode().equalsIgnoreCase("FOC")
                        || getMajorCode().equalsIgnoreCase("FIC") || getMajorCode().equalsIgnoreCase("FIL")) {
                  if (getMinorCode().equalsIgnoreCase("CSA4") || getMinorCode().equalsIgnoreCase("CSA2")
                              || getMinorCode().equalsIgnoreCase("CSA3") || getMinorCode().equalsIgnoreCase("CSA1")) {
                        String refNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();

                        String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        // //Loggers.general().info(LOG,"event refNumber ----->" +
                        // evnt);
                        String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");
                        // //Loggers.general().info(LOG,"Event count for posting----->"
                        // +
                        // evvcount);
                        try {
                              con = getConnection();
                              String query = "select TO_CHAR(ext.SIGVALDT,'DD/MM/YY') from master mas,baseevent bas,extevent ext where mas.KEY97=bas.MASTER_KEY and bas.KEY97=ext.EVENT AND ext.SIGVALDT is not null and mas.MASTER_REF ='"
                                          + refNumber + "' and bas.REFNO_PFIX='" + evnt + "' and bas.REFNO_SERL='" + evvcount + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Notional due date query " + query);
                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              if (rs1.next()) {

                                    String paydt = rs1.getString(1);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Notional due date " + paydt);
                                    }
                                    getPane().setSIGVALDT(paydt);
                              } else {
                                    getPane().setSIGVALDT("");
                              }
                        }

                        catch (Exception e) {
                              LOG.info("Exception Notional due date in finance" + e.getMessage());

                        } finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,"Connection Failed! Check
                                    // output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {

                        String refNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();

                        try {
                              con = getConnection();
                              String query = "select TO_CHAR(ext.SIGVALDT,'DD/MM/YY') from master mas,baseevent bas,extevent ext where mas.KEY97=bas.MASTER_KEY and bas.KEY97=ext.EVENT AND ext.SIGVALDT is not null and mas.MASTER_REF ='"
                                          + refNumber + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Notional due date query for amend" + query);
                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {

                                    String paydt = rs1.getString(1);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Notional due date amend" + paydt);
                                    }
                                    getPane().setSIGVALDT(paydt);
                              }
                        }

                        catch (Exception e) {
                              LOG.info("Exception Notional due date in finance amend" + e.getMessage());

                        } finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,"Connection Failed! Check
                                    // output
                                    // console");
                                    e.printStackTrace();
                              }
                        }

                  }
            } else {
                  // Loggers.general().info(LOG,"Payment due date major code" +
                  // getMajorCode());
                  getPane().setSIGVALDT("");
            }

            if (getMajorCode().equalsIgnoreCase("FEL")) {

                  String refNumber = getDriverWrapper().getEventFieldAsText("ORR", "r", "").trim();

                  try {

                        String query = "SELECT TO_CHAR(pap.VALUE_DAT,'DD/MM/YY'),bev.REFNO_PFIX FROM master mas, BASEEVENT bev, LCPAYMENT lcp , PARTPAYMNT pap WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = lcp.KEY97 AND lcp.KEY97 = pap.PAYEV_KEY AND mas.MASTER_REF = '"
                                    + refNumber + "' AND pap.TYPE ='A' AND bev.REFNO_PFIX = 'DPR'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("value due date query for amend" + query);
                        }
                        con = getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {

                              String paydt = rs1.getString(1);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Notional due date amend" + paydt);
                              }
                              getPane().setVALDAT(paydt);
                        }
                  }

                  catch (Exception e) {
                        LOG.info("Exception Value date in finance amend" + e.getMessage());

                  } finally {
                        try {
                              if (rs1 != null)
                                    rs1.close();
                              if (ps1 != null)
                                    ps1.close();
                              if (con != null)
                                    con.close();
                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check
                              // output
                              // console");
                              e.printStackTrace();
                        }
                  }

            }

      }

      public void getcustomValueDelete() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {
                  String refNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");

                  con = getConnection();
                  String query = "select step.status from baseevent ev, master m, stephist step, eventstep evstep where ev.master_key = m.key97 and step.event_key = ev.key97 and step.eventstep = evstep.key97 and m.master_ref = '"
                              + refNumber + "' order by step.timestart desc";

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("Step ID query===>" + query);
                  }

                  ps1 = con.prepareStatement(query);
                  rs1 = ps1.executeQuery();
                  if (rs1.next()) {
                        String stepID = rs1.getString(1).trim();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Custom value step id===> " + stepID);
                        }
                        if (stepID.length() > 0 && !stepID.equalsIgnoreCase("P")) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Custom value cleared===>");
                              }
                              System.out.println("RTGS amount null");
                              LOG.info("inside if 21/01/2019" + stepID);
                              getPane().setRTGNFT("");
                              getPane().setRTGSN(false);
                              getPane().setNETSCF("");
                              getPane().setPROREMT("");
                              getPane().setPURCODE("");
                              getPane().setBENTYP("");
                              getPane().setBENACC_Name("");
                              getPane().setBENNAME_Name("");
                              getPane().setIFSCCO_Name("");
                              getPane().setBENBAK("");
                              getPane().setBENBRN("");
                              getPane().setBENCITY("");
                              getPane().setRTGSNEFT("");
                              getPane().setUTRNO("");
                              getPane().setNARRTVE("");
                              getPane().setBADDRE_Name("");
                              getPane().setRTGSPART("");
                              getPane().setRTGSNEFT("");
                              getPane().setTENEXT("");
                              getPane().setTENRSN("");
                              getPane().setLETNUM("");
                              getPane().setLETDAT("");
                              getPane().setREEXTIN("");
                              getPane().setEXTDAT("");
                              getPane().setWRAMT("");
                              getPane().setWRAMCU("");
                              getPane().setWRITDATE("");
                              getPane().setWRITNDI("");
                              getPane().setTRANTYP("");
                              getPane().setREALTYP("");
                              getPane().setBOENO("");
                              getPane().setBOEDATE("");
                              getPane().setPOSTDIS("");
                              // getPane().setRATECOV("");
                              // getPane().setRATEDET("");

                              getPane().setREPAYAMT("");
                              getPane().setTOTUSD(0 + " USD");
                              getPane().setTOTINR(0 + " INR");
                              getPane().setTOTEUR(0 + " EUR");
                              getPane().setTOTJPY(0 + " JPY");
                              getPane().setTOTGBP(0 + " GBP");
                              LOG.info("Inside empty function forlim" + getPane().getFORLIM());
                              getPane().setFORLIM(false);
                              LOG.info("Inside empty function forlim after empty" + getPane().getFORLIM());

                              String gateway = getDriverWrapper().getEventFieldAsText("FRGI", "l", "");

                              if (!gateway.equalsIgnoreCase("Y")) {
                                    /*
                                     * getPane().setNOSAMT(""); getPane().setNOSTOUT("");
                                     */
                                    getPane().setEBRCFLAG("");
                                    /*
                                     * getPane().setNOSTRM(""); getPane().setNOSTMT(""); getPane().setNOSTAMT("");
                                     * getPane().setNOSTDAT(""); getPane().setPOOLAMT(""); getPane().setNOSTACC("");
                                     * getPane().setINWMSG(""); getPane().setMTMESG("");
                                     */
                              }
                              if (gateway.equalsIgnoreCase("Y")) {
                                    getPane().setPERADV("");
                                    getPane().setADVREC("");
                                    getPane().setNETRECIV("");
                                    getPane().setBILLPAY("");
                              }
                              String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                              if (getMinorCode().equalsIgnoreCase("CLP") && prd_typ.equalsIgnoreCase("OCF")) {

                                    try {
                                          List<ExtEventShippingCollections> shipTab = (List<ExtEventShippingCollections>) getWrapper()
                                                      .getExtEventShippingCollections();
                                          // //Loggers.general().info(LOG,"shipping table for notional
                                          // rate---->" + shipTable.size());
                                          String repval_new = "2";
                                          String notionalrate = "1";
                                          for (int i = 0; i < shipTab.size(); i++) {

                                                ExtEventShippingCollections ship = shipTab.get(i);
                                                BigDecimal outStandAmt = ship.getCOUTSAMT();
                                                String outStandccy = ship.getCOUTSAMTCurrency().toString();

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      LOG.info("ShippingCollection step if====>" + outStandAmt);
                                                }

                                                ship.setCRSNASHF("");
                                                ship.setCREPAY(outStandAmt);
                                                ship.setCREPAYCurrency(outStandccy);
                                                ship.setCNOTIONL(new BigDecimal(Double.valueOf(notionalrate)));
                                                // Loggers.general().info(LOG,"Notional rate in CSM step
                                                // if====>" + ship.getCNOTIONL());
                                                String shipcoll = "0";
                                                ship.setCSHCOLAM(new BigDecimal(Double.valueOf(shipcoll)));
                                                ship.setCSHCOLAMCurrency(outStandccy);
                                                if (repval_new.equalsIgnoreCase("2")
                                                            || repval_new.equalsIgnoreCase("") && (!repval_new.equalsIgnoreCase("1"))) {
                                                      // Loggers.general().info(LOG,"Repayment type in CSM
                                                      // step
                                                      // if====>" + repval_new);
                                                      ship.setREPTYP(repval_new);
                                                } else {
                                                      // Loggers.general().info(LOG,"Repayment type in CSM
                                                      // step
                                                      // else loop====>" + repval_new);
                                                }
                                          }
                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("Exception ShippingCollection clear " + e.getMessage());
                                          }
                                    }
                              } else {
                                    // Loggers.general().info(LOG,"shipping table repayment in CSM
                                    // step
                                    // else====>");
                              }

                              if (getMinorCode().equalsIgnoreCase("POD") && prd_typ.equalsIgnoreCase("ELF")) {
                                    try {
                                          List<ExtEventShippingTable> shipTab = (List<ExtEventShippingTable>) getWrapper()
                                                      .getExtEventShippingTable();
                                          // //Loggers.general().info(LOG,"shipping table for notional
                                          // rate---->" + shipTable.size());
                                          String repval_new = "2";
                                          String notionalrate = "1";
                                          for (int i = 0; i < shipTab.size(); i++) {

                                                ExtEventShippingTable ship = shipTab.get(i);
                                                BigDecimal outStandAmt = ship.getLOUTSAMT();
                                                String outStandccy = ship.getLOUTSAMTCurrency().toString();
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      LOG.info("ShippingTable step if====>" + outStandAmt);
                                                }
                                                ship.setREASHRF("");
                                                ship.setREPAYAM(outStandAmt);
                                                ship.setREPAYAMCurrency(outStandccy);
                                                ship.setNOTIONAL(new BigDecimal(Double.valueOf(notionalrate)));
                                                //// Loggers.general().info(LOG,"Notional rate in CSM
                                                //// step
                                                //// if====>" + ship.getNOTIONAL());
                                                String shipcol = "0";
                                                ship.setSHCOLAM(new BigDecimal(Double.valueOf(shipcol)));
                                                ship.setSHCOLAMCurrency(outStandccy);

                                                if (repval_new.equalsIgnoreCase("2")
                                                            || repval_new.equalsIgnoreCase("") && (!repval_new.equalsIgnoreCase("1")
                                                                        || !repval_new.equalsIgnoreCase("Part"))) {
                                                      //// Loggers.general().info(LOG,"Repayment type in
                                                      //// CSM
                                                      //// step
                                                      //// if====>" + repval_new);
                                                      ship.setREPTYPE(repval_new);
                                                } else {
                                                      //// Loggers.general().info(LOG,"Repayment type in
                                                      //// CSM
                                                      //// step
                                                      //// else loop====>" + repval_new);
                                                }
                                          }
                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("Exception ShippingTable clear " + e.getMessage());
                                          }
                                    }
                              } else {
                                    //// Loggers.general().info(LOG,"shipping repayment in CSM step
                                    //// else====>" + payaction);
                              }

                        }

                  } else {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Custom value step id else===> ");
                        }
                  }

            }

            catch (Exception e) {

                  LOG.info("Exception Custom value clear " + e.getMessage());

            } finally {
                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      // Bill reference no least date checking
      public static Date compareAndReturnDate(List<Date> list) {

            Date returndate = null;
            try {
                  // //Loggers.general().info(LOG,"Bill referen date List Size" +
                  // list.size());
                  returndate = list.get(0);
                  for (int a = 0; a < list.size(); a++) {
                        if (list.get(a).compareTo(returndate) < 1) {
                              returndate = list.get(a);
                        }
                  }
                  // //Loggers.general().info(LOG," Bill referen dateFinal value " +
                  // returndate);
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return returndate;
      }

      // Shipping bill date greater date checking
      public static Date compareAndGreaterDate(List<Date> list) {
            Date returndate = null;
            try {
                  // //Loggers.general().info(LOG,"Bill referen date List Size" +
                  // list.size());
                  returndate = list.get(0);
                  for (int a = 0; a < list.size(); a++) {
                        if (list.get(a).compareTo(returndate) > 1) {
                              returndate = list.get(a);
                        }
                  }
                  // //Loggers.general().info(LOG," Bill referen dateFinal value " +
                  // returndate);
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return returndate;
      }

      public double NullPoint(String advan) {
            double totalAdv = 0.0;
            if (!(advan == null || advan.length() == 1)) {
                  // //Loggers.general().info(LOG,"Null Checker--->" + advan + "length" +
                  // advan.length());
                  totalAdv = 0.0;
                  totalAdv = Double.valueOf(advan);
                  // usanceconv = Double.valueOf(maximum_amount);
            }
            return totalAdv;
      }

      public void getSubvention() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
            String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
            if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                  // PCR //PTP
                  String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                  // //Loggers.general().info(LOG,"SubproCode-----> " + subproCode);
                  String customer = "";
                  if (getMajorCode().equalsIgnoreCase("FOC")) {
                        customer = getDriverWrapper().getEventFieldAsText("B+FT", "p", "no");

                  } else {
                        customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");
                  }
                  // //Loggers.general().info(LOG,"customer " + customer);
                  if (!customer.isEmpty()) {
                        try {
                              // String dmT = "";
                              // String cusper = "";

                              // //Loggers.general().info(LOG,"enter into try");
                              con = getConnection();
                              String dms = "select TRIM(SUBELB),TRIM(CUSINPER) from extcust where cust='" + customer.trim() + "'";
                              dmsp = con.prepareStatement(dms);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("Subvention query===>" + dms);

                              }
                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {
                                    String dmT = getEmptyIfNull(dmsr.getString(1)).trim();
                                    String cusper = getEmptyIfNull(dmsr.getString(2)).trim();
                                    String eligible = getWrapper().getELISUB();

                                    System.out.println("Subvention eligible value===>" + dmT);
                                    LOG.info("Subvention percentage value===>" + cusper);

                                    if (eligible.equalsIgnoreCase("") || eligible == null) {
                                          getPane().setINTPERE(cusper);
                                          getPane().setELISUB(dmT);
                                    } else if (!eligible.equalsIgnoreCase("") && eligible != null) {

                                          System.out.println("Subvention eligible value else if===>" + dmT + " " + eligible);
                                          LOG.info("Subvention percentage value else if===>" + cusper);

                                          getPane().setELISUB(eligible);
                                          getPane().setINTPERE(cusper);
                                    }
                                    if (eligible.equalsIgnoreCase("") || eligible == null || eligible.equalsIgnoreCase("NO")) {
                                          System.out.println("Subvention eligible value else if subvention===>" + dmT);
                                          getPane().setINTPERE("");
                                    } else if (!eligible.equalsIgnoreCase("") && eligible != null
                                                && eligible.equalsIgnoreCase("YES")) {
                                          getPane().setINTPERE(cusper);
                                    }

                              }

                        } catch (Exception e) {

                              e.printStackTrace();

                        } finally {
                              try {
                                    if (dmsr != null)
                                          dmsr.close();
                                    if (dmsp != null)
                                          dmsp.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,"Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  }

//                if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
//                            || subproCode.equalsIgnoreCase("HCA")) {
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            LOG.info("Subvention subproduct Code===>" + subproCode);
//
//                      }
//                }
//                } else {
//
//                      getPane().setELISUB("");
//                      getPane().setINTPERE("");
//                      getPane().setECSECE("");
//                      getPane().setSUBVRBI("0.00 INR");
//                      getPane().setSUBVCRD("0.00 INR");
//                      getPane().setSUBVPAY("0.00 INR");
//                      getPane().setSUBVDEB("0.00 INR");
//
//                }
            }
      }

      @SuppressWarnings("deprecation")
      public int Currentyear(String date) {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            int cyear = 1;
            try {
                  // //Loggers.general().info(LOG,"date " + dateFormat1.parse(date));
                  Calendar calendar = Calendar.getInstance();
                  // //Loggers.general().info(LOG,"Date" + dateFormat1.parse(date).getDate() +
                  // "Month "+ dateFormat1.parse(date).getMonth() + "Year " +
                  // dateFormat1.parse(date).getYear());
                  Date date11 = new Date(dateFormat1.parse(date).getYear(), dateFormat1.parse(date).getMonth(),
                              dateFormat1.parse(date).getDate());
                  // calendar.setTime(dateFormat1.parse(date));
                  calendar.setTime(date11);
                  cyear = calendar.get(Calendar.DAY_OF_YEAR);
                  // //Loggers.general().info(LOG,"Day_of_Year" + cyear);
            } catch (Exception e) {
                  // Loggers.general().info(LOG,e.getMessage());
            }
            return cyear;
      }

      @SuppressWarnings("deprecation")
      public String Countyear(String dates) {
            // For Country
            // Declaration
            // CustomValidation objs = new CustomValidation();
            String cvdiy = "Null";
            int expiryd = 0;
            int curexp = 0;
            String Value = "A";
            String daysArr[] = new String[366];
            String brcd = this.getDriverWrapper().getEventFieldAsText("BIN", "s", "");
            String brcn = this.getDriverWrapper().getEventFieldAsText("BCN", "s", "");
            String brcd1 = this.getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
            // //Loggers.general().info(LOG,"BIN" + brcd);
            // //Loggers.general().info(LOG,"BCN" + brcn);
            // //Loggers.general().info(LOG,"MCCY" + brcd1);

            SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
            try {
                  con = getConnection();
                  expiryd = format.parse(dates).getYear();
                  String sglccy = "SELECT TRIM(cvdiy) FROM cvpf where cvccy = '" + brcd1 + "' and cvyoc = '" + expiryd + "'";
                  // //Loggers.general().info(LOG,sglccy);
                  ccyd = con.prepareStatement(sglccy);
                  rccy3 = ccyd.executeQuery();
                  while (rccy3.next()) {
                        cvdiy = rccy3.getString(1);
                        daysArr = cvdiy.split("");
                        curexp = Currentyear(dates);
                        // //Loggers.general().info(LOG,"curexp" + curexp);
                        // //Loggers.general().info(LOG,cvdiy);
                        Value = daysArr[curexp].trim();
                  }
                  // rccy3.close();
                  // ccyd.close();
                  // con.close();
            } catch (Exception e) {
                  // Loggers.general().info(LOG,e.getMessage());
            } finally {
                  try {

                        if (rccy3 != null)
                              rccy3.close();
                        if (ccyd != null)
                              ccyd.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

            return Value;// giving the Y or N

      }

      @SuppressWarnings("deprecation")
      public String Brancyear(String dates) {
            // For Country
            String cvdiy = "Null";
            int expiryd = 0;
            int curexp = 0;
            String Value = "A";
            String daysArr[] = new String[366];
            String brcd = this.getDriverWrapper().getEventFieldAsText("BIN", "s", "");
            String brcn = this.getDriverWrapper().getEventFieldAsText("BCN", "s", "");
            String brcd1 = this.getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
            // //Loggers.general().info(LOG,"BIN" + brcd);
            // //Loggers.general().info(LOG,"BCN" + brcn);
            // //Loggers.general().info(LOG,"MCCY" + brcd1);

            SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
            try {
                  con = getConnection();
                  expiryd = format.parse(dates).getYear();
                  String sglccy = "select trim(cxdiy) from cxpf where cxyoc = '" + expiryd + "' and cxmnm ='" + brcd + "'";
                  // //Loggers.general().info(LOG,sglccy);
                  ccyd = con.prepareStatement(sglccy);
                  rccy3 = ccyd.executeQuery();
                  while (rccy3.next()) {
                        cvdiy = rccy3.getString(1);
                        daysArr = cvdiy.split("");
                        curexp = Currentyear(dates);
                        // //Loggers.general().info(LOG,"curexp" + curexp);
                        // Loggers.general().info(LOG,cvdiy);
                        Value = daysArr[curexp].trim();
                  }

                  // rccy3.close();
                  // ccyd.close();
                  // con.close();
            } catch (Exception e) {
                  // Loggers.general().info(LOG,e.getMessage());
            }

            finally {
                  try {

                        if (rccy3 != null)
                              rccy3.close();
                        if (ccyd != null)
                              ccyd.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
            return Value;// giving the Y or N
      }

      public boolean preshipTableValidation() {
            boolean result = true;
            double gridrepayAmt = 0.0;
            double preshpTableRepayAmt = 0.0;
            String masref = "";
            String evenref = "";
            Connection con = null;
            PreparedStatement pre = null;
            ResultSet res = null;
            String repayAmtCurrency = "";
            double dividVal = 0.0;
            try {
                  // //Loggers.general().info(LOG,"entered preshipTableValidation ");
                  masref = getmasRefNo();
                  // //Loggers.general().info(LOG,"Master Reference Number is " + masref);
                  evenref = geteventRefNo();
                  // //Loggers.general().info(LOG,"Event Reference Number is " + evenref);
                  List<ExtEventLoanDetails> adv = (List<ExtEventLoanDetails>) getWrapper().getExtEventLoanDetails();
                  for (int i = 0; i < adv.size(); i++) {
                        ExtEventLoanDetails lotbl = adv.get(i);
                        // repayAmtCurrency=lotbl.getREAMOUNTCurrency().trim();
                        // dividVal=getDecimalforCurrency(repayAmtCurrency);
                        gridrepayAmt = gridrepayAmt + (lotbl.getREAMOUNT().doubleValue());

                  }
                  // //Loggers.general().info(LOG,"Value of Repay Amount in Customization grid
                  // is " + gridrepayAmt);
                  con = getConnection();
                  String getSumAmountquery = "select sum(nvl(repayamt,0)) from ett_preshipment_apiserver where masref='"
                              + masref + "' AND eventref='" + evenref + "'";
                  // //Loggers.general().info(LOG,"Valie of Query is " + getSumAmountquery);
                  pre = con.prepareStatement(getSumAmountquery);
                  res = pre.executeQuery();
                  if (res.next()) {
                        preshpTableRepayAmt = res.getDouble(1);
                  }
                  // //Loggers.general().info(LOG,"Value of Repay Amount in Localization grid
                  // is
                  // " + preshpTableRepayAmt);
                  if (gridrepayAmt != gridrepayAmt) {
                        result = false;
                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception in preshipTableValidation " +
                  // e.getMessage());
            }

            finally {
                  try {

                        if (res != null)
                              res.close();
                        if (pre != null)
                              pre.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
            return result;
      }

      public String getHyperPref() {

            String url = "";
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {
                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
                  String subProductCode = getDriverWrapper().getEventFieldAsText("EVCD", "s", "");
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String EventReference = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  String hyperValue = "select url from extapp where ID='RefTrack'";
                  // //Loggers.general().info(LOG,"hyperValue---->" + hyperValue);
                  con = ConnectionMaster.getConnection();
                  ps1 = con.prepareStatement(hyperValue);
                  rs1 = ps1.executeQuery();
                  while (rs1.next()) {
                        url = rs1.getString(1).trim();
                        // //Loggers.general().info(LOG,"url---->" + url);
                  }
                  // http://localhost:8080/ReferralTracking/makerProcess?tradeRef=Master_REF_NO&tradeRef1=ILC&masRef=EVENT_REF_NO&masRef1=SUB_PRODUCT_CODE

                  url = url + "?tradeRef=" + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                              + "&masRef1=" + subProductCode;

            } catch (Exception e) {

                  LOG.info("Exception ReferralTracking " + e.getMessage());
            } finally {
                  try {

                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
            return url;

      }

      @SuppressWarnings("deprecation")
      public String Counyyear(String dates) {
            // For Country
            String cvdiy = "Null";
            int expiryd = 0;
            int curexp = 0;
            String Value = "A";
            String daysArr[] = new String[366];
            String brcd = this.getDriverWrapper().getEventFieldAsText("BIN", "s", "");
            String brcn = this.getDriverWrapper().getEventFieldAsText("BCN", "s", "");
            String brcd1 = this.getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
            // //Loggers.general().info(LOG,"BIN" + brcd);
            // //Loggers.general().info(LOG,"BCN" + brcn);
            // //Loggers.general().info(LOG,"MCCY" + brcd1);

            SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
            try {
                  con = getConnection();
                  expiryd = format.parse(dates).getYear();
                  String sglccy = "select trim(cvdiy) from cvpf where cvcna= '" + brcn + "' and cvyoc='" + expiryd + "'";
                  // //Loggers.general().info(LOG,sglccy);
                  ccyd = con.prepareStatement(sglccy);
                  rccy3 = ccyd.executeQuery();
                  while (rccy3.next()) {
                        cvdiy = rccy3.getString(1);
                        daysArr = cvdiy.split("");
                        curexp = Currentyear(dates);
                        // //Loggers.general().info(LOG,"curexp" + curexp);
                        // Loggers.general().info(LOG,cvdiy);
                        Value = daysArr[curexp].trim();
                  }

                  // rccy3.close();
                  // ccyd.close();
                  // con.close();
            } catch (Exception e) {
                  // Loggers.general().info(LOG,e.getMessage());
            } finally {
                  try {

                        if (rccy3 != null)
                              rccy3.close();
                        if (ccyd != null)
                              ccyd.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

            return Value;// giving the Y or N

      }

      public static Timestamp getLocalDateTime() {
            Date date = new Date();
            long t = date.getTime();
            Timestamp sqltime = new Timestamp(t);
            return sqltime;
      }

      public String getBoeLink(String val) {
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            // //Loggers.general().info(LOG,"BOE NEW");
            // String tradepr1 = getPane().getTRADEPRO_Name();
            String payamt = "";
            try {
                  payamt = getDriverWrapper().getEventFieldAsText("DAMR", "v", "m");
            } catch (Exception e) {
                  payamt = "0";
            }
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  LOG.info("DIR amount for BOE" + payamt);
            }
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");

            String dmsstr = "", dmsurl = "";
            String stepCode = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
            String boeVal = "";

            if ((stepCode.equalsIgnoreCase("CSM") || stepCode.equalsIgnoreCase("CBS Maker")
                        || stepCode.equalsIgnoreCase("CBS Maker 1")) && !stepCode.equalsIgnoreCase("CBS Authoriser")) {
                  boeVal = "BOE";
            } else if (stepCode.equalsIgnoreCase("CBS Authoriser")) {
                  boeVal = "BOE1";
            }

            try {

                  con = getConnection();
                  String dms = "select value1 from ett_parameter_tbl where parameter_id='" + boeVal + "' and active = 'Y'";
                  dmsp = con.prepareStatement(dms);
                  // //Loggers.general().info(LOG,"DMS Query " + dms);
                  dmsr = dmsp.executeQuery();
                  while (dmsr.next()) {
                        dmsstr = dmsr.getString(1);
                        // dmsurl = dmsstr + "&dirAmount=" + payamt;
                        dmsurl = dmsstr + "&dirAmount=" + payamt + "&mstRefNumber=" + masRefNo + "&evtRefNumber=" + eventRefNo;
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("BOE Final Url" + dmsurl);
                        }

                  }

            } catch (Exception e) {
                  LOG.info("Exception BOE Url" + e.getMessage());

            } finally {
                  try {

                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
            return dmsurl;
      }

      // Standing instruction link
      public String getStandinglink() {
            LOG.info("getStandinglink Entered");
            String returns = "";
            String strPropName = "Standing";
            String displayVal = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {
                  // Loggers.general().info(LOG,"Standing URL is not empty-------->");
                  displayVal = PROPCode.getPropval();
            } else {
                  // Loggers.general().info(LOG,"Standing URL is empty-------->");

            }

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            // Loggers.general().info(LOG,"Account no is for URL-------->" + accountno);
            try {
                  String accountno = getWrapper().getACTNUM();
                  LOG.info("accountno - " + accountno);
                  returns = displayVal + accountno;

            } catch (Exception e) {
                  LOG.info("Standing exception----->" + e.getMessage());
            }
            // //Loggers.general().info(LOG,"Standing URL finally----->" + returns);
            return returns;

      }

      public String getIFSCSEARCH() {
            System.out.println("Inside getIFSCSEARCH()");
            String strPropName = "IFSCCODE";
            String displayVal = "";
            String returns = "";

            try {

                  AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                              .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                  EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
                  if (PROPCode != null) {
                        displayVal = PROPCode.getPropval();
                        // //Loggers.general().info(LOG,"WorkflowCSM URL getting -------->"
                        // +displayVal);
                  } else {
                        // Loggers.general().info(LOG,"WorkflowCSM URL is empty-------->");

                  }
                  System.out.println("IFSC Code Output " + displayVal);
                  String strLog = "Log";
                  String dailyval_Log = "";
                  @SuppressWarnings("unchecked")
                  AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                              .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
                  EXTGENCUSTPROP CodeLog = queryLog.getUnique();
                  if (CodeLog != null) {

                        dailyval_Log = CodeLog.getPropval();
                  } else {
                        // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
                  }
                  // String IFSCCODE = getWrapper().getRECIFSC().trim();
                  // Loggers.general().info(LOG,"IFSCCODE -------->"+IFSCCODE);
                  String IFSCCODE = getDriverWrapper().getEventFieldAsText("cBYQ", "s", "");
                  LOG.info("IFSCCODE event field on hyperlink-------->" + IFSCCODE);
                  returns = displayVal + "" + IFSCCODE;

            } catch (Exception e) {
                  System.out.println("Exception getIFSCSEARCH() " + e.getMessage());
                  e.printStackTrace();
            }
            return returns;
      }

      // InwardLink
      public String getINWARDREM() {
            String strPropName = "INWARDNO";
            String displayVal = "";
            String returns = "";
            String customer = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"WorkflowCSM URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"WorkflowCSM URL is empty-------->");

            }
            String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
            if (getMajorCode().equalsIgnoreCase("ELC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());

                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                        // id
            } else if (getMajorCode().equalsIgnoreCase("ODC")) {

                  customer = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                        // id
            }

            // Loggers.general().info(LOG,"Major code and customer no-------->" + customer);

            returns = displayVal + "" + customer + "&masterReference=" + masReference + "&eventReference=" + eventCode;

            return returns;

      }

      // Get Packing Credit A/c Outstanding //210823 vishal g
      public String getACCOUNT() {

            String strPropName = "ACCOUNT";
            String displayVal = "";
            String returns = "";
            String customer = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"WorkflowCSM URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"WorkflowCSM URL is empty-------->");

            }
            try {
                  con = getConnection();
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  String stepPhase = getDriverWrapper().getEventFieldAsText("SPHC", "s", "");
                  if (getMajorCode().equalsIgnoreCase("ELC") || getMajorCode().equalsIgnoreCase("CPCI")
                              || getMajorCode().equalsIgnoreCase("CPBI")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());

                        customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
                  } else if (getMajorCode().equalsIgnoreCase("ODC")) {

                        customer = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                              // id
                  }
//                String query="Select bev.extfield from master mas,baseevent bev "+
//                             "where mas.key97=bev.master_key "+
//                             "and mas.MASTER_REF='"+masReference.trim()+"'"+
//                            " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+eventCode+"'"+
//                            " and bev.status not in ('a','c')";
//                System.out.println("query for account code :"+query+" "+masReference+" "+eventCode);
//                dmsp = con.prepareStatement(query);
//                // //Loggers.general().info(LOG,"DMS Query " + dms);
//                dmsr = dmsp.executeQuery();   
//                while (dmsr.next()) {
//                      key29 = dmsr.getString(1);
//                      // dmsurl = dmsstr + "&dirAmount=" + payamt;
//                      
//                      }
                  // Loggers.general().info(LOG,"Major code and customer no-------->" + customer);

                  returns = displayVal + "" + customer + "&masterReference=" + masReference + "&eventReference=" + eventCode
                              + "&stepPhase=" + stepPhase;
            } catch (Exception e) {

                  System.out.println("Setting the URL preship exception- " + e.getMessage());
                  e.printStackTrace();

            }

            finally {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
            return returns;

      }

      // FORWARD LINK // VISHAL G //120523
      public String getForward() {
            String strPropName = "FORWARD";
            String displayVal = "";
            String returns = "";

            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"WorkflowCSM URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"WorkflowCSM URL is empty-------->");

            }

            System.out.println("Major code and customer no-------->" + displayVal);

            returns = displayVal;

            return returns;

      }

      // Purchase Order LINK // VISHAL G //120623
      public String getPurchaseDetails() {

            String strPropName = "PURCHASE";
            String displayVal = "";
            String returns = "";
            String customer = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"WorkflowCSM URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"WorkflowCSM URL is empty-------->");

            }
            try {

                  customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");

                  // Loggers.general().info(LOG,"Major code and customer no-------->" + customer);

                  returns = displayVal + "" + customer;
            } catch (Exception e) {

                  System.out.println("Setting the URL purchase exception- " + e.getMessage());
                  e.printStackTrace();

            }

            return returns;
      }

      public String getWorkflow() {
            String strPropName = "WorkflowCSM";
            String displayVal = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"WorkflowCSM URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"WorkflowCSM URL is empty-------->");

            }

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            // //Loggers.general().info(LOG,"Started getWorkflow URL");
            String returns = "";
            String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", ""); // ILC
            String eventCode = getDriverWrapper().getEventFieldAsText("EVCD", "s", ""); // ISI
            String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", ""); // ILF
            String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", ""); //
            String EventReference = getDriverWrapper().getEventFieldAsText("EVR", "r", ""); // ISS001
            String stepcsm = "";
            String stepCode = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
            LOG.info("stepID for URL CSM getWorkflow---->" + stepCode);
            String stepID = "CSM";
            // AdhocCSM
            if (stepCode.equalsIgnoreCase("CSM Reject")) {
                  stepcsm = "CSM";
            } else if (stepCode.equalsIgnoreCase("AdhocCSM")) {
                  stepcsm = "CSM";
            } else if (stepCode.equalsIgnoreCase("CSM")) {
                  stepcsm = stepCode;
            }
            // //Loggers.general().info(LOG,"stepID for URL---->" + stepID);

            // UAT 2 URL
            if (getMinorCode().equalsIgnoreCase("LSC")) {

                  String sub_Productcode = getDriverWrapper().getEventFieldAsText("S:PCO", "s", ""); //
                  String sub_EventCode = getDriverWrapper().getEventFieldAsText("S:EVCD", "s", ""); //
                  String sub_subProductCode = getDriverWrapper().getEventFieldAsText("S:PTP", "s", ""); //
                  String sub_MasterReference = getDriverWrapper().getEventFieldAsText("S:MST", "r", "").trim(); //
                  String sub_EventReference = getDriverWrapper().getEventFieldAsText("S:EVR", "r", "").trim(); // CRE001
                  if (stepcsm.equalsIgnoreCase("CSM")) {
                        returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                    + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventcode=" + sub_EventCode
                                    + "&editable=yes" + "&stepId=" + stepID;
                  } else if (stepcsm.equalsIgnoreCase("CBS Maker")) {
                        returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                    + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventcode=" + sub_EventCode
                                    + "&editable=no" + "&stepId=" + stepID;
                  } else if (stepcsm.equalsIgnoreCase("CBS Authoriser")) {

                        returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                    + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventcode=" + sub_EventCode
                                    + "&editable=no" + "&stepId=" + stepID;
                  } else {
                        returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                    + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventcode=" + sub_EventCode
                                    + "&editable=no" + "&stepId=" + stepID;
                  }
            } else {
                  if (stepcsm.equalsIgnoreCase("CSM")) {
                        returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                    + "&masRef1=" + subproductCode + "&eventcode=" + eventCode + "&editable=yes" + "&stepId="
                                    + stepID;
                  } else if (stepcsm.equalsIgnoreCase("CBS Maker")) {
                        returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                    + "&masRef1=" + subproductCode + "&eventcode=" + eventCode + "&editable=no" + "&stepId="
                                    + stepID;
                  } else if (stepcsm.equalsIgnoreCase("CBS Authoriser")) {

                        returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                    + "&masRef1=" + subproductCode + "&eventcode=" + eventCode + "&editable=no" + "&stepId="
                                    + stepID;
                  } else {
                        returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                    + "&masRef1=" + subproductCode + "&eventcode=" + eventCode + "&editable=no" + "&stepId="
                                    + stepID;
                  }
            }

            LOG.info("getWorkflow returns---->" + returns);

            return returns;

      }

      public String getReferealtracking() {
            // //Loggers.general().info(LOG,"Started getReferealtracking URL");
            String strPropName = "ReferralURL";
            String displayVal = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ReferralURL URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"ReferralURL URL is empty-------->");

            }

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String returns = "";
            String amount = "0";
            String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", ""); // ILC
            String eventCode = getDriverWrapper().getEventFieldAsText("EVCD", "s", ""); // ISI
            String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", ""); // ILF
            String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", ""); //
            String EventReference = getDriverWrapper().getEventFieldAsText("EVR", "r", ""); // ISS001

            // select VALUE1 from ETT_PARAMETER_TBL where PARAMETER_ID='REFINITIAT'
            // and ACTIVE = 'Y';
            if (getMinorCode().equalsIgnoreCase("LSC")) {
                  String sub_Productcode = getDriverWrapper().getEventFieldAsText("S:PCO", "s", ""); //
                  String sub_EventCode = getDriverWrapper().getEventFieldAsText("S:EVCD", "s", ""); //
                  String sub_subProductCode = getDriverWrapper().getEventFieldAsText("S:PTP", "s", ""); //
                  String sub_MasterReference = getDriverWrapper().getEventFieldAsText("S:MST", "r", "").trim(); //
                  String sub_EventReference = getDriverWrapper().getEventFieldAsText("S:EVR", "r", "").trim(); // CRE001

                  amount = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                  if (!amount.equalsIgnoreCase("") && amount != null) {

                        // //Loggers.general().info(LOG,"Referealtracking initial amount" +
                        // amount);
                        float fullamt = Float.valueOf(amount);
                        // //Loggers.general().info(LOG,"Referealtracking master amount" +
                        // amount);
                        long longamout = (long) fullamt;
                        // //Loggers.general().info(LOG,"Referealtracking master longamout" +
                        // longamout);
                        String cur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        String cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "f");
                        String cutno = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");
                        // //Loggers.general().info(LOG,"Referealtracking primary cust" + cust);

                        String stepedit = "";
                        String stepCode = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                        // Loggers.general().info(LOG,"stepID for referal URL CSM---->" +
                        // stepCode);
                        String stepID = "CSM";
                        if (stepCode.equalsIgnoreCase("CSM Reject")) {
                              stepedit = "CSM";
                        } else if (stepCode.equalsIgnoreCase("AdhocCSM")) {
                              stepedit = "CSM";
                        } else if (stepCode.equalsIgnoreCase("CSM")) {
                              stepedit = stepCode;
                        }

                        // UAT2 URL
                        if (stepedit.equalsIgnoreCase("CSM")) {
                              returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                          + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventCode=" + sub_EventCode
                                          + "&stepId=" + stepID + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur
                                          + "&applicantNo=" + cutno + "&editable=yes";
                        } else if (stepedit.equalsIgnoreCase("CBS Maker")) {
                              returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                          + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventCode=" + sub_EventCode
                                          + "&stepId=" + stepID + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur
                                          + "&applicantNo=" + cutno + "&editable=no";
                        }

                        else if (stepedit.equalsIgnoreCase("CBS Authoriser")) {
                              returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                          + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventCode=" + sub_EventCode
                                          + "&stepId=" + stepID + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur
                                          + "&applicantNo=" + cutno + "&editable=no";
                        } else {
                              returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                          + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventCode=" + sub_EventCode
                                          + "&stepId=" + stepID + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur
                                          + "&applicantNo=" + cutno + "&editable=no";
                        }

                  } else {
                        // Loggers.general().info(LOG,"Referealtracking amount empty " +
                        // amount);
                  }
            } else {

                  amount = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                  if (!amount.equalsIgnoreCase("") && amount != null) {

                        // //Loggers.general().info(LOG,"Referealtracking initial amount" +
                        // amount);
                        float fullamt = Float.valueOf(amount);
                        // //Loggers.general().info(LOG,"Referealtracking master amount" +
                        // amount);
                        long longamout = (long) fullamt;
                        // //Loggers.general().info(LOG,"Referealtracking master longamout" +
                        // longamout);
                        String cur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        String cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "f");
                        String cutno = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");
                        // //Loggers.general().info(LOG,"Referealtracking primary cust" + cust);
                        String stepedit = "";
                        String stepCode = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                        // Loggers.general().info(LOG,"stepID for referal URL CSM---->" +
                        // stepCode);
                        String stepID = "CSM";

                        if (stepCode.equalsIgnoreCase("CSM Reject")) {
                              stepedit = "CSM";
                        } else if (stepCode.equalsIgnoreCase("AdhocCSM")) {
                              stepedit = "CSM";
                        } else if (stepCode.equalsIgnoreCase("CSM")) {
                              stepedit = stepCode;
                        }

                        // UAT2 URL
                        if (stepedit.equalsIgnoreCase("CSM")) {
                              returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                          + "&masRef1=" + subproductCode + "&eventCode=" + eventCode + "&stepId=" + stepID
                                          + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur + "&applicantNo="
                                          + cutno + "&editable=yes";
                        } else if (stepedit.equalsIgnoreCase("CBS Maker")) {
                              returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                          + "&masRef1=" + subproductCode + "&eventCode=" + eventCode + "&stepId=" + stepID
                                          + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur + "&applicantNo="
                                          + cutno + "&editable=no";
                        }

                        else if (stepedit.equalsIgnoreCase("CBS Authoriser")) {
                              returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                          + "&masRef1=" + subproductCode + "&eventCode=" + eventCode + "&stepId=" + stepID
                                          + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur + "&applicantNo="
                                          + cutno + "&editable=no";
                        } else {
                              returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                          + "&masRef1=" + subproductCode + "&eventCode=" + eventCode + "&stepId=" + stepID
                                          + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur + "&applicantNo="
                                          + cutno + "&editable=no";
                        }

                  } else {
                        // Loggers.general().info(LOG,"Referealtracking amount empty " +
                        // amount);
                  }

            }

            return returns;

      }

      public String getWorkflowCBS() {
            String strPropName = "WorkflowCSM";
            String displayVal = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"WorkflowCSM URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"WorkflowCSM URL is empty-------->");

            }

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String returns = "";
            String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", ""); // ILC
            String eventCode = getDriverWrapper().getEventFieldAsText("EVCD", "s", ""); // ISI
            String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", ""); // ILF
            String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", ""); //
            String EventReference = getDriverWrapper().getEventFieldAsText("EVR", "r", ""); // ISS001

            String stepID = "CBS Maker";

            String stepcsm = "";
            String stepCode = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
            // Loggers.general().info(LOG,"stepID for URL CBS Maker---->" + stepCode);

            if (stepCode.equalsIgnoreCase("CBS Reject")) {
                  stepcsm = "CBS Maker";
            } else if (stepCode.equalsIgnoreCase("CBS Maker")) {
                  stepcsm = stepCode;
            }
            // //Loggers.general().info(LOG,"stepID for URL---->" + stepID);
            if (getMinorCode().equalsIgnoreCase("LSC")) {

                  String sub_Productcode = getDriverWrapper().getEventFieldAsText("S:PCO", "s", ""); //
                  String sub_EventCode = getDriverWrapper().getEventFieldAsText("S:EVCD", "s", ""); //
                  String sub_subProductCode = getDriverWrapper().getEventFieldAsText("S:PTP", "s", ""); //
                  String sub_MasterReference = getDriverWrapper().getEventFieldAsText("S:MST", "r", "").trim(); //
                  String sub_EventReference = getDriverWrapper().getEventFieldAsText("S:EVR", "r", "").trim(); // CRE001

                  // UAT 2 URL
                  if (stepcsm.equalsIgnoreCase("CBS Maker")) {
                        returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                    + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventcode=" + sub_EventCode
                                    + "&editable=yes" + "&stepId=" + stepID;
                  } else if (stepcsm.equalsIgnoreCase("CSM")) {
                        returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                    + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventcode=" + sub_EventCode
                                    + "&editable=no" + "&stepId=" + stepID;
                  } else if (stepcsm.equalsIgnoreCase("CBS Authoriser")) {
                        returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                    + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventcode=" + sub_EventCode
                                    + "&editable=no" + "&stepId=" + stepID;
                  } else {
                        returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                    + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventcode=" + sub_EventCode
                                    + "&editable=no" + "&stepId=" + stepID;
                  }
            } else {

                  // UAT 2 URL
                  if (stepcsm.equalsIgnoreCase("CBS Maker")) {
                        returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                    + "&masRef1=" + subproductCode + "&eventcode=" + eventCode + "&editable=yes" + "&stepId="
                                    + stepID;
                  } else if (stepcsm.equalsIgnoreCase("CSM")) {
                        returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                    + "&masRef1=" + subproductCode + "&eventcode=" + eventCode + "&editable=no" + "&stepId="
                                    + stepID;
                  } else if (stepcsm.equalsIgnoreCase("CBS Authoriser")) {
                        returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                    + "&masRef1=" + subproductCode + "&eventcode=" + eventCode + "&editable=no" + "&stepId="
                                    + stepID;
                  } else {
                        returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                    + "&masRef1=" + subproductCode + "&eventcode=" + eventCode + "&editable=no" + "&stepId="
                                    + stepID;
                  }

            }

            return returns;

      }

      public String getReferealtrackingCBS() {
            // //Loggers.general().info(LOG,"Started getReferealtracking URL");

            String strPropName = "ReferralURL";
            String displayVal = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ReferralURL URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"ReferralURL URL is empty-------->");

            }

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String returns = "";
            String amount = "0";
            String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", ""); // ILC
            String eventCode = getDriverWrapper().getEventFieldAsText("EVCD", "s", ""); // ISI
            String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", ""); // ILF
            String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", ""); //
            String EventReference = getDriverWrapper().getEventFieldAsText("EVR", "r", ""); // ISS001

            // select VALUE1 from ETT_PARAMETER_TBL where PARAMETER_ID='REFINITIAT'
            // and ACTIVE = 'Y';
            if (getMinorCode().equalsIgnoreCase("LSC")) {

                  String sub_Productcode = getDriverWrapper().getEventFieldAsText("S:PCO", "s", ""); //
                  String sub_EventCode = getDriverWrapper().getEventFieldAsText("S:EVCD", "s", ""); //
                  String sub_subProductCode = getDriverWrapper().getEventFieldAsText("S:PTP", "s", ""); //
                  String sub_MasterReference = getDriverWrapper().getEventFieldAsText("S:MST", "r", "").trim(); //
                  String sub_EventReference = getDriverWrapper().getEventFieldAsText("S:EVR", "r", "").trim(); // CRE001
                  amount = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                  if (!amount.equalsIgnoreCase("") && amount != null) {

                        // //Loggers.general().info(LOG,"Referealtracking initial amount" +
                        // amount);
                        float fullamt = Float.valueOf(amount);
                        // //Loggers.general().info(LOG,"Referealtracking master amount" +
                        // amount);
                        long longamout = (long) fullamt;
                        // //Loggers.general().info(LOG,"Referealtracking master longamout" +
                        // longamout);
                        String cur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        String cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "f");
                        String cutno = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");
                        // //Loggers.general().info(LOG,"Referealtracking primary cust" + cust);

                        // UAT2 URL

                        String stepedit = "";
                        String stepCode = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                        // Loggers.general().info(LOG,"stepID for referal URL CBS Maker---->" +
                        // stepCode);
                        String stepID = "CBS Maker";
                        if (stepCode.equalsIgnoreCase("CBS Reject")) {
                              stepedit = "CBS Maker";
                        } else if (stepCode.equalsIgnoreCase("CBS Maker")) {
                              stepedit = stepCode;
                        }

                        if (stepedit.equalsIgnoreCase("CSM")) {
                              returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                          + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventCode=" + sub_EventCode
                                          + "&stepId=" + stepID + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur
                                          + "&applicantNo=" + cutno + "&editable=no";

                        } else if (stepedit.equalsIgnoreCase("CBS Maker")) {
                              returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                          + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventCode=" + sub_EventCode
                                          + "&stepId=" + stepID + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur
                                          + "&applicantNo=" + cutno + "&editable=yes";

                        } else if (stepedit.equalsIgnoreCase("CBS Authoriser")) {
                              returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                          + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventCode=" + sub_EventCode
                                          + "&stepId=" + stepID + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur
                                          + "&applicantNo=" + cutno + "&editable=no";

                        } else {
                              returns = displayVal + sub_MasterReference + "&tradeRef1=" + sub_Productcode + "&masRef="
                                          + sub_EventReference + "&masRef1=" + sub_subProductCode + "&eventCode=" + sub_EventCode
                                          + "&stepId=" + stepID + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur
                                          + "&applicantNo=" + cutno + "&editable=no";
                        }

                  } else {
                        // Loggers.general().info(LOG,"Referealtracking amount empty " +
                        // amount);
                  }
            } else {

                  amount = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                  if (!amount.equalsIgnoreCase("") && amount != null) {

                        // //Loggers.general().info(LOG,"Referealtracking initial amount" +
                        // amount);
                        float fullamt = Float.valueOf(amount);
                        // //Loggers.general().info(LOG,"Referealtracking master amount" +
                        // amount);
                        long longamout = (long) fullamt;
                        // //Loggers.general().info(LOG,"Referealtracking master longamout" +
                        // longamout);
                        String cur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        String cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "f");
                        String cutno = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");
                        // //Loggers.general().info(LOG,"Referealtracking primary cust" + cust);

                        String stepedit = "";
                        String stepCode = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                        // Loggers.general().info(LOG,"stepID for referal URL CBS Maker---->" +
                        // stepCode);
                        String stepID = "CBS Maker";
                        if (stepCode.equalsIgnoreCase("CBS Reject")) {
                              stepedit = "CBS Maker";
                        } else if (stepCode.equalsIgnoreCase("CBS Maker")) {
                              stepedit = stepCode;
                        }

                        if (stepedit.equalsIgnoreCase("CSM")) {
                              returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                          + "&masRef1=" + subproductCode + "&eventCode=" + eventCode + "&stepId=" + stepID
                                          + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur + "&applicantNo="
                                          + cutno + "&editable=no";

                        } else if (stepedit.equalsIgnoreCase("CBS Maker")) {
                              returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                          + "&masRef1=" + subproductCode + "&eventCode=" + eventCode + "&stepId=" + stepID
                                          + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur + "&applicantNo="
                                          + cutno + "&editable=yes";

                        } else if (stepedit.equalsIgnoreCase("CBS Authoriser")) {
                              returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                          + "&masRef1=" + subproductCode + "&eventCode=" + eventCode + "&stepId=" + stepID
                                          + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur + "&applicantNo="
                                          + cutno + "&editable=no";

                        } else {
                              returns = displayVal + MasterReference + "&tradeRef1=" + productcode + "&masRef=" + EventReference
                                          + "&masRef1=" + subproductCode + "&eventCode=" + eventCode + "&stepId=" + stepID
                                          + "&applicant=" + cust + "&amount=" + longamout + "&currency=" + cur + "&applicantNo="
                                          + cutno + "&editable=no";
                        }

                  } else {
                        // Loggers.general().info(LOG,"Referealtracking amount empty " +
                        // amount);
                  }

            }

            return returns;

      }

      public String FDenquiry() {
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
            String customerNo = "";
            if (getMajorCode().equalsIgnoreCase("SHG")) {
                  customerNo = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");

            } else {
                  customerNo = getDriverWrapper().getEventFieldAsText("APP", "p", "no");

            }
            String returns = "";

            String strPropName = "FDENQUIRY";
            String displayVal = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ThemeBridge URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"ThemeBridge URL is empty-------->");

            }

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            // Loggers.general().info(LOG,"FDENQUIRY URL getting ==========>" +displayVal);

            returns = displayVal + "customerCIF=" + customerNo + "&masRefNo=" + masRefNo + "&eventRefNo=" + eventRefNo;

            return returns;
      }

      // Link
      public String Link(String val) {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
            String returns = "";
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  LOG.info("Transaction link called in connection master");
            }

            String strPropName = "ThemeBridge";
            String displayVal = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  LOG.info("ThemeBridge URL getting -------->" + displayVal);
            } else {
                  LOG.info("ThemeBridge URL is empty-------->");

            }
            LOG.info("Inside the link method=================>");
            returns = displayVal + "?masRefNo=" + masRefNo.toString().trim() + "&eventRefNo="
                        + eventRefNo.toString().trim();
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  LOG.info("Transaction link return====> " + returns);
            }
            return returns;

      }

      // scf their ref no passing

      // Link
      public String Their(String value) {
            String strPropName = "ThemeBridge";
            String displayVal = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  displayVal = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ThemeBridge URL getting -------->"
                  // +displayVal);
            } else {
                  // Loggers.general().info(LOG,"ThemeBridge URL is empty-------->");

            }

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String returns = "";
            // //Loggers.general().info(LOG,"ThemeBridge URL getting finally-------->"
            // +displayVal);
            try {
                  String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  // String theirRefNo ="";

                  String theirRefNo = getDriverWrapper().getEventFieldAsText("THE", "r", "").trim();

                  LOG.info("Product type FSA and their ref no" + theirRefNo);
                  LOG.info("Product type FSA and their ref no length" + theirRefNo.length());

                  if (theirRefNo != null && !theirRefNo.equalsIgnoreCase("") && theirRefNo.length() > 0) {
                        theirRefNo = theirRefNo;
                        LOG.info("Their reference of THE" + theirRefNo);
                  } else {
                        theirRefNo = getDriverWrapper().getEventFieldAsText("B+FR", "r", "").trim();
                        LOG.info("Their reference of B+FR" + theirRefNo);
                  }
                  String[] temp;
                  String delimiter = "_";

                  temp = theirRefNo.split(delimiter);
                  String the_name = temp[0];
                  LOG.info("their ref no" + the_name);
                  String stp = getDriverWrapper().getEventFieldAsText("PUO3", "s", "");
                  LOG.info("stp value" + stp);
                  LOG.info("Event reference added=============>");

                  if (stp.equalsIgnoreCase("YES") && getMajorCode().equalsIgnoreCase("FSA")) {

                        // UAT2 URL
                        // returns =
                        // "https://misyswebappuat:8884/ThemeBridge/transactionStatusServlet"
                        // + "?masRefNo=" + the_name;
                        returns = displayVal + "?masRefNo=" + the_name + "&eventRefNo=" + eventRefNo;
                        LOG.info("Return inside yes " + returns);

                  } else {

                        // UAT2 URL
                        returns = displayVal + "?masRefNo=" + masRefNo + "&eventRefNo=" + eventRefNo;
                        LOG.info("Return inside else " + returns);

                  }

            } catch (Exception e) {
                  LOG.info("Exception themebridge for their reference number" + e.getMessage());
            }

            return returns;
      }

      // Preshipment
      public String getHyperPreshipment() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
            String behalfOf = getDriverWrapper().getEventFieldAsText("BOB", "s", "");

            /*
             * if (getMajorCode().equalsIgnoreCase("FEL") &&
             * getMinorCode().equalsIgnoreCase("CSA4")) {
             */
            if ((getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4"))
                        || (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("CSA1"))

            ) {

                  LOG.info("Finance Export Lc Create Event " + eventRefNo);
                  eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
            }

            String customer = "customer", customera = "customerid";
            String prodtype = "prodtype", prodtypea = "prodtype";
            prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                                                                                                                  // CODE
                                                                                                                  // ILC
            prodtypea = getDriverWrapper().getEventFieldAsText("PLN", "s", "");// pROD
                                                                                                                  // Name
                                                                                                                  // Import
                                                                                                                  // lc

            // Cutomer for export lc
            if (prodtype.trim().equalsIgnoreCase("ILC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                              // id
            } // end of ILC
            else if (prodtype.trim().equalsIgnoreCase("ELC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("IDC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("DRE", "p", "no");// party
                                                                                                                              // id
            } // end of ELC
            else if (prodtype.trim().equalsIgnoreCase("IGT")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("ISB")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("ODC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  String eventRefPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s", "");// Event
                                                                                                                                          // reference
                                                                                                                                          // prefix
                  // //Loggers.general().info(LOG,"eventRefPrefix" + eventRefPrefix);
                  if (eventRefPrefix.trim().equalsIgnoreCase("FEC")) {
                        customer = getDriverWrapper().getEventFieldAsText("B+DB", "p", "cu");// party
                                                                                                                                    // name
                        customera = getDriverWrapper().getEventFieldAsText("B+DB", "p", "no");// party
                                                                                                                                    // id
                  } else {
                        customer = getDriverWrapper().getEventFieldAsText("DRW", "p", "cu");// party
                                                                                                                              // name
                        customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                                    // id
                  }
            } else if (prodtype.trim().equalsIgnoreCase("OCC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("DRW", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                              // id
            } // end of odc
            else if (prodtype.trim().equalsIgnoreCase("IGT")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                              // id
            } // end of import gurantee
            else if (prodtype.trim().equalsIgnoreCase("EGT")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
            } // end of Export Gura
            else if (prodtype.trim().equalsIgnoreCase("ICC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("DRE", "p", "no");// party
                                                                                                                              // id
            } // end of Shipping Gur
            else if (prodtype.trim().equalsIgnoreCase("FSA")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("DBT", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("ESB")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("RMB")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("ISS", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("ISS", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("FEL")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("B+FT", "p", "cu");// party
                                                                                                                              // name
                  customera = getDriverWrapper().getEventFieldAsText("B+FT", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("CPCI")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
            } else {
                  // //Loggers.general().info(LOG,"Check in Else Part");
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode() + "and Minor
                  // Code" + getMinorCode());
                  customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");// party
                                                                                                                              // id
            }

            if (dailyval_Log.equalsIgnoreCase("YES")) {

                  LOG.info("Preshipment ODC payment customer" + customera);
            }

            // //Loggers.general().info(LOG,"function is continuing");
            String returns = "";

            // Commented from 1269 to 1284 for validate jira
            String repayAmt = "";
            try {
                  repayAmt = getPane().getREPAYAMT();
                  if ((!repayAmt.equalsIgnoreCase("")) && repayAmt != null) {
                        // //Loggers.general().info(LOG,"value of repayAmt" + repayAmt);
                        repayAmt = repayAmt.trim();
                        String repayAmtOnly = repayAmt.replaceAll("[\\D.]", "");
                        /*
                         * repayAmtOnly=repayAmtOnly.substring(0, repayAmtOnly.length()-2);
                         */
                        // //Loggers.general().info(LOG,"value of
                        // repayAmt--------------------------------->> " +
                        // repayAmtOnly);
                        String repayCurr = repayAmt.replaceAll("[^A-Z]", "");
                        // //Loggers.general().info(LOG,"value of " +
                        // "--------------------------------->>
                        // " + repayCurr);
                        repayAmt = repayAmtOnly + repayCurr;

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              LOG.info("URL preshipment Repayment amount - " + repayAmt);
                        }
                  }
            } catch (Exception e) {

                  LOG.info("Preship exception repayment " + e.getMessage());

            }
            // OPVD
            String Valuedate = null;
            if (prodtype.trim().equalsIgnoreCase("CPCI")) {
                  Valuedate = getDriverWrapper().getEventFieldAsText("OPVD", "d", "");
                  // //Loggers.general().info(LOG,"value date for Inward remittance--->" +
                  // Valuedate);
            } else if (prodtype.trim().equalsIgnoreCase("ODC")) {
                  Valuedate = getDriverWrapper().getEventFieldAsText("PVD", "d", "");
                  // //Loggers.general().info(LOG,"value date for Explc--->" + Valuedate);
                  if (getMinorCode().equalsIgnoreCase("FEC")) {
                        Valuedate = getDriverWrapper().getEventFieldAsText("B+SD", "d", "");
                        // //Loggers.general().info(LOG,"value date for Explc--->" +
                        // Valuedate);
                  }
            } else if (prodtype.trim().equalsIgnoreCase("FSA")) {
                  Valuedate = getDriverWrapper().getEventFieldAsText("B+MD", "d", "");
                  // //Loggers.general().info(LOG,"value date for Fiance standalone--->" +
                  // Valuedate);
            } else if (prodtype.trim().equalsIgnoreCase("FOC")) {
                  Valuedate = getDriverWrapper().getEventFieldAsText("B+DR", "d", "");
                  // //Loggers.general().info(LOG,"value date for Fiance standalone--->" +
                  // Valuedate);
            }

            else if (prodtype.trim().equalsIgnoreCase("ELC")) {
                  Valuedate = getDriverWrapper().getEventFieldAsText("PDD", "d", "");
                  // //Loggers.general().info(LOG,"value date for Fiance standalone--->" +
                  // Valuedate);
            } else if (prodtype.trim().equalsIgnoreCase("IDC")) {
                  Valuedate = getDriverWrapper().getEventFieldAsText("PVD", "d", "");
                  // //Loggers.general().info(LOG,"value date for Explc--->" + Valuedate);
            } else if (prodtype.trim().equalsIgnoreCase("FEL")) {
                  Valuedate = getDriverWrapper().getEventFieldAsText("B+DR", "d", "");
                  // //Loggers.general().info(LOG,"value date for Explc--->" + Valuedate);
            }

            try {

                  // //Loggers.general().info(LOG,"Entered Try block");
                  con = getConnection();
                  String dms = "select value1 from ett_parameter_tbl where parameter_id='PRESHP' and active ='Y'";
                  dmsp = con.prepareStatement(dms);
                  // //Loggers.general().info(LOG,dms);
                  dmsr = dmsp.executeQuery();
                  while (dmsr.next()) {
                        // //Loggers.general().info(LOG,"Entering into while1");
                        String dmsstr = dmsr.getString(1);

                        returns = dmsstr + "?masterRef=" + masRefNo + "&eventRef=" + eventRefNo + "&cifCode=" + customera.trim()
                                    + "&repayAmt=" + repayAmt + "&behalfOfBranch=" + behalfOf + "&valueDate=" + Valuedate;

                        // //Loggers.general().info(LOG,"Setting the URL preship - " +
                        // returns);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              // Loggers.general().info(LOG,"Setting the URL preship - " +
                              // returns);
                        }

                  }
                  // dmsr.close();
                  // dmsp.close();
                  // con.close();
            } catch (Exception e) {

                  LOG.info("Setting the URL preship exception- " + e.getMessage());
                  e.printStackTrace();

            }

            finally {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
            // } else {
            // // Loggers.general().info(LOG,"value of repayAmt is empty" + repayAmt);
            // }

            // //Loggers.general().info(LOG,"Setting the URL preship return - " + returns);
            return returns;
      }

      public String getLimitNode() {

//          System.out.println("Inside getLimitNode Method");

            String strLog = "Log";
            String dailyval_Log = "";
            String strPropName = "LimitNode";
            String displayVal = "";
            String returns = "";
            String subproductCode = "";
            String transactionCurrency = "";
            String user = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            }

            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");

            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {
                  displayVal = PROPCode.getPropval();
            }
//          System.out.println("Main Classesss");
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");

            String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");

            String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();

            String subproductCode1 = getDriverWrapper().getEventFieldAsText("PTP", "s", "");

            if (subproductCode1 != null && !subproductCode1.isEmpty()) {
                  subproductCode = subproductCode1;
            }

            String transactionCurrency1 = "INR";
            System.out.println("transactionCurrency==>" + transactionCurrency1);

            if (transactionCurrency1 != null && !transactionCurrency1.isEmpty()) {
                  transactionCurrency = transactionCurrency1;
            }
            String user1 = getDriverWrapper().getEventFieldAsText("USER", "s", "");

            if (user1 != null && !user1.isEmpty()) {
                  user = user1;
            }
            String zoneid = "ZONE1";

            /*
             * if (getMajorCode().equalsIgnoreCase("FEL") &&
             * getMinorCode().equalsIgnoreCase("CSA4")) {
             */
            if ((getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4"))
                        || (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("CSA1"))) {

//                System.out.println("Finance Export Lc Create Event " + eventRefNo);
                  eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
            }

            String customer = "";
            String customera = "";
            String prodtype = "prodtype", prodtypea = "prodtype";
            prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                                                                                                                  // CODE
                                                                                                                  // ILC
            prodtypea = getDriverWrapper().getEventFieldAsText("PLN", "s", "");// pROD
                                                                                                                  // Name
                                                                                                                  // Import
                                                                                                                  // lc
            // Cutomer for export lc
            if (getMajorCode().equalsIgnoreCase("ILC")) {
                  // //System.out.println("Major Code" + getMajorCode());
                  System.out.println("Inside ILC");
                  customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } // end of ILC
            else if (getMajorCode().equalsIgnoreCase("ELC")) {
//                System.out.println("Inside ELC");
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                  System.out.println("Customer===>" + customera); // id
            } else if (getMajorCode().equalsIgnoreCase("IDC")) {
                  System.out.println("Inside IDC");
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("DRE", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } // end of ELC
            else if (getMajorCode().equalsIgnoreCase("IGT")) {
//                System.out.println("Inside IGT");
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party

//                System.out.println("Customer===>" + customera);
            } else if (getMajorCode().equalsIgnoreCase("ISB")) {
//                System.out.println("Inside ISB");
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } else if (getMajorCode().equalsIgnoreCase("ODC")) {
//                System.out.println("Inside ODC");
                  // //System.out.println("Major Code" + getMajorCode());
                  String eventRefPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s", "");// Event
                                                                                                                                          // reference
//                System.out.println("Customer===>" + customera); // prefix
                  // //System.out.println("eventRefPrefix" + eventRefPrefix);
                  if (getMajorCode().equalsIgnoreCase("FEC")) {
//                      System.out.println("Inside FEC");
                        customer = getDriverWrapper().getEventFieldAsText("B+DB", "p", "cu");// party
                                                                                                                                    // name
                        customera = getDriverWrapper().getEventFieldAsText("B+DB", "p", "no");// party
//                      System.out.println("Customer===>" + customera); // id
                  } else {
                        customer = getDriverWrapper().getEventFieldAsText("DRW", "p", "cu");// party
                                                                                                                              // name
                        customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
//                      System.out.println("Customer===>" + customera); // id
                  }
            } else if (getMajorCode().equalsIgnoreCase("OCC")) {
//                System.out.println("Inside OCC");
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("DRW", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } // end of odc
            /*
             * else if (getMajorCode().equalsIgnoreCase("IGT")) { //
             * //System.out.println("Major Code" + getMajorCode()); customer =
             * getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party // name
             * customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
             * System.out.println("Customer===>"+customera); // id }
             */// end of import gurantee
            else if (getMajorCode().equalsIgnoreCase("EGT")) {
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } // end of Export Gura
            else if (getMajorCode().equalsIgnoreCase("ICC")) {
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("DRE", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } // end of Shipping Gur
            else if (getMajorCode().equalsIgnoreCase("FSA")) {
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("DBT", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } else if (prodtype.trim().equalsIgnoreCase("ESB")) {
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } else if (getMajorCode().equalsIgnoreCase("RMB")) {
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("ISS", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("ISS", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } else if (getMajorCode().equalsIgnoreCase("FEL")) {
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("B+FT", "p", "cu");// party
                                                                                                                              // name
                  customera = getDriverWrapper().getEventFieldAsText("B+FT", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } else if (getMajorCode().equalsIgnoreCase("CPCI")) {
                  // //System.out.println("Major Code" + getMajorCode());
                  customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            } else {

                  customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "");// party
                                                                                                                        // name
                  customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");// party
//                System.out.println("Customer===>" + customera); // id
            }

            if (dailyval_Log.equalsIgnoreCase("YES")) {

//                System.out.println("Preshipment ODC payment customer" + customera);
            }
            final String secretKey = "secrete";

            customera = encrypt(customera, secretKey);
//          System.out.println("customencrypt--->" + customera);
            user = encrypt(user, secretKey);
//          System.out.println("userencrypt--->" + user);
            returns = displayVal + "?masrefno=" + masRefNo + "&eventrefno=" + eventRefNo + "&productcode=" + productcode
                        + "&subproductcode=" + subproductCode + "&transactioncurrency=" + transactionCurrency + "&user=" + user
                        + "&zoneid=" + zoneid + "&customer=" + customera;
//          System.out.println("The url of return " + returns);
            return returns;

      }

      // For cr 143 Starts

      public void prepareSecreteKey(String myKey) {
            MessageDigest sha = null;
            try {
                  key = myKey.getBytes(StandardCharsets.UTF_8);
                  sha = MessageDigest.getInstance("SHA-1");
                  key = sha.digest(key);
                  key = Arrays.copyOf(key, 16);
                  secretKey = new SecretKeySpec(key, ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                  e.printStackTrace();
            }
      }

      public String encrypt(String strToEncrypt, String secret) {
            try {
                  prepareSecreteKey(secret);
                  Cipher cipher = Cipher.getInstance(ALGORITHM);
                  cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                  return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            } catch (Exception e) {
                  System.out.println("Error while encrypting: " + e.toString());
            }
            return "";
      }

      public String getproduct(String prodty) {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String customera = "";
            // //Loggers.general().info(LOG,"Product type initially -----> " + prodty);
            // Cutomer for export lc
            String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
            // //Loggers.general().info(LOG,"get product type---> " + prodtype);
            if (prodtype.trim().equalsIgnoreCase("ILC")) {
                  // //Loggers.general().info(LOG,"get Major Code for ILC -----> " +
                  // getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                              // id
            } // end of ILC
            else if (prodtype.trim().equalsIgnoreCase("ELC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("IDC")) {
                  // //Loggers.general().info(LOG,"Major Code>>>>>>>" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");// party
                  // //Loggers.general().info(LOG,"Customer no for duplicate---->" +
                  // customera);
                  // // id
            } // end of ELC
            else if (prodtype.trim().equalsIgnoreCase("IGT")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("ISB")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("ODC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  String eventRefPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s", "");// Event
                                                                                                                                          // reference
                                                                                                                                          // prefix
                  // //Loggers.general().info(LOG,"eventRefPrefix" + eventRefPrefix);
                  if (eventRefPrefix.trim().equalsIgnoreCase("FEC")) {
                        customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
                                                                                                                                    // id
                  } else {
                        customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                                    // id
                  }
            } else if (prodtype.trim().equalsIgnoreCase("OCC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                              // id
            } // end of odc
            else if (prodtype.trim().equalsIgnoreCase("IGT")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                              // id
            } // end of import gurantee
            else if (prodtype.trim().equalsIgnoreCase("EGT")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
            } // end of Export Gura
            else if (prodtype.trim().equalsIgnoreCase("ICC")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("DRE", "p", "no");// party
                                                                                                                              // id
            } // end of Shipping Gur
            else if (prodtype.trim().equalsIgnoreCase("FSA")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("ESB")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("RMB")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("ISS", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("FEL")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("CPCI")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                              // id
            } else if (prodtype.trim().equalsIgnoreCase("CPCO")) {
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");// party
                                                                                                                              // id
            } else {
                  // //Loggers.general().info(LOG,"Check in Else Part");
                  // //Loggers.general().info(LOG,"Major Code" + getMajorCode() + "and Minor
                  // Code" + getMinorCode());
                  customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");// party
                                                                                                                              // id
            }
            return customera;

      }

      public String getLob() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String customera = "";
            // TOdo 31-08-2018
            // String customerb = "";

            String maincustomer = "";
            String localcustomer = "";

            try {

                  String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                                                                                                                                    // CODE
                  String finace_event = getDriverWrapper().getEventFieldAsText("BFC", "l", "");
                  LOG.info("ELC finance event create" + finace_event);
                  // ILC
                  if (prodtype.trim().equalsIgnoreCase("ELC")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        maincustomer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cct");
                        LOG.info("MainCustomerLOB BEN" + maincustomer);
                        localcustomer = getDriverWrapper().getEventFieldAsText("APP", "p", "cct");
                        LOG.info("MainCustomerLOB APP" + localcustomer);

                        if (maincustomer.equalsIgnoreCase("CO")) {
                              customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                                          // id
                              LOG.info("MainCustomerofBEN" + customera);
                        } else if (localcustomer.equalsIgnoreCase("CO")) {
                              customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");
                              LOG.info("MainCustomerofAPP" + customera);
                        } else {

                              customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");
                              LOG.info("MainCustomerofBENinelse" + customera);
                        }

                  }

                  String lob1 = "";
                  String lob2 = "";

                  String lobFacility = "";

                  if (getMinorCode().equalsIgnoreCase("ADE") && finace_event.equalsIgnoreCase("N")) {

                        // Changing the LOB code order for JIRA - 5345 as per sign off
                        // by
                        // Raisa
                        try {
                              String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim() + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code from customer level--->" + lob1);
                              }
                              con = getConnection();
                              dmsp = con.prepareStatement(dms);
                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {
                                    lob1 = dmsr.getString(1);
                                    lob2 = dmsr.getString(2);

                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code 1 population--->" + lob1);
                                    LOG.info("LOB code 2 population--->" + lob2);
                              }

                        } catch (Exception e) {
                              LOG.info("Exception LOB code 1 and 2 population--->" + e.getMessage());
                        }

                  } else if ((getMinorCode().equalsIgnoreCase("DOP") || getMinorCode().equalsIgnoreCase("POD"))
                              && finace_event.equalsIgnoreCase("Y")) {

                        try {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code Customer id with finance--->" + customera);
                              }
                              String facility = getWrapper().getLIMITID().trim();

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Limit id from transaction for LOB code===>" + facility);
                              }

                              if (facility != null && facility.length() > 0) {

                                    String dmsQuery = "select TRIM(LOBCODE) from CUSTOMERMARGIN where CIF ='" + customera.trim()
                                                + "' and FACILITY='" + facility.trim() + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Facilityt id for LOB code Query===>" + dmsQuery);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dmsQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lobFacility = dmsr.getString(1);

                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Exception in Facility id for LOB code" + e.getMessage());
                              }
                        }

                        // Changing the LOB code order for JIRA - 5345 as per sign off
                        // by
                        // Raisa
                        try {
                              String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim() + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code from customer level--->" + lob1);
                              }
                              con = getConnection();
                              dmsp = con.prepareStatement(dms);
                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {
                                    lob1 = dmsr.getString(1);
                                    lob2 = dmsr.getString(2);

                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code 1 population--->" + lob1);
                                    LOG.info("LOB code 2 population--->" + lob2);
                              }

                        } catch (Exception e) {
                              LOG.info("Exception LOB code 1 and 2 population--->" + e.getMessage());
                        }

                  }

                  String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        if (lobFacility != null && !lobFacility.equalsIgnoreCase("")) {

                              getPane().setLOB(lobFacility);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code populated from facility--->" + getPane().getLOB());
                              }

                        } else if (lob1 != null && !lob1.equalsIgnoreCase("")) {
                              getPane().setLOB(lob1);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code populated from TI+--->" + getPane().getLOB());
                              }
                        } else if (lob2 != null && !lob2.equalsIgnoreCase("")) {
                              getPane().setLOB(lob2);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code populated from BCIF--->" + getPane().getLOB());
                              }
                        }

                  } else {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("LOB code else for event status--->" + eventStatus);
                        }
                  }
            } catch (Exception e)

            {
                  LOG.info("Exception in LOB code customer master" + e.getMessage());
                  e.printStackTrace();

            }

            finally

            {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

            return customera;

      }

      // For CR_36 Starts here

      public String getLOBFreeNeg() {

            System.out.println("Inside getLOBFreeNeg method");
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // System.out.println("ADDDailyTxnLimit is empty-------->");

            }
            String customera = "";
            // TOdo 31-08-2018
            // String customerb = "";

            String maincustomer = "";
            String localcustomer = "";

            try {

                  String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                                                                                                                                    // CODE
                  String finace_event = getDriverWrapper().getEventFieldAsText("BFC", "l", "");
                  System.out.println("FreeNelgLC event create" + finace_event);
                  // ILC
                  if (prodtype.trim().equalsIgnoreCase("FRN")) {
                        // //System.out.println("Major Code" + getMajorCode());
                        maincustomer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cct");
                        System.out.println("MainCustomerLOB BEN" + maincustomer);
                        localcustomer = getDriverWrapper().getEventFieldAsText("APP", "p", "cct");
                        System.out.println("MainCustomerLOB APP" + localcustomer);

                        if (maincustomer.equalsIgnoreCase("CO")) {
                              customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                                          // id
                              System.out.println("MainCustomerofBEN" + customera);
                        } else if (localcustomer.equalsIgnoreCase("CO")) {
                              customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");
                              System.out.println("MainCustomerofAPP" + customera);
                        } else {

                              customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");
                              System.out.println("MainCustomerofBENinelse" + customera);
                        }

                  }

                  String lob1 = "";
                  String lob2 = "";

                  String lobFacility = "";

                  // Changing the LOB code order for JIRA - 5345 as per sign off
                  // by
                  // Raisa
                  try {
                        System.out.println("Inside Minor Code value for freely Neg LC");

                        String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim() + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              System.out.println("LOB code from customer level--->" + lob1);
                        }
                        con = getConnection();
                        dmsp = con.prepareStatement(dms);
                        dmsr = dmsp.executeQuery();
                        while (dmsr.next()) {
                              lob1 = dmsr.getString(1);
                              lob2 = dmsr.getString(2);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              System.out.println("LOB code 1 population--->" + lob1);
                              System.out.println("LOB code 2 population--->" + lob2);
                        }

                  } catch (Exception e) {
                        System.out.println("Exception LOB code 1 and 2 population--->" + e.getMessage());
                  }

                  if ((getMinorCode().equalsIgnoreCase("FNF") || getMinorCode().equalsIgnoreCase("POF"))
                              && finace_event.equalsIgnoreCase("Y")) {

                        try {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("LOB code Customer id with finance--->" + customera);
                              }
                              String facility = getWrapper().getLIMITID().trim();

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("Limit id from transaction for LOB code===>" + facility);
                              }

                              if (facility != null && facility.length() > 0) {

                                    String dmsQuery = "select TRIM(LOBCODE) from CUSTOMERMARGIN where CIF ='" + customera.trim()
                                                + "' and FACILITY='" + facility.trim() + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Facilityt id for LOB code Query===>" + dmsQuery);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dmsQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lobFacility = dmsr.getString(1);

                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("Exception in Facility id for LOB code" + e.getMessage());
                              }
                        }

                        // Changing the LOB code order for JIRA - 5345 as per sign off
                        // by
                        // Raisa
                        try {
                              String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim() + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("LOB code from customer level--->" + lob1);
                              }
                              con = getConnection();
                              dmsp = con.prepareStatement(dms);
                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {
                                    lob1 = dmsr.getString(1);
                                    lob2 = dmsr.getString(2);

                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("LOB code 1 population--->" + lob1);
                                    System.out.println("LOB code 2 population--->" + lob2);
                              }

                        } catch (Exception e) {
                              System.out.println("Exception LOB code 1 and 2 population--->" + e.getMessage());
                        }

                  }

                  String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        if (lobFacility != null && !lobFacility.equalsIgnoreCase("")) {

                              getPane().setLOB(lobFacility);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("LOB code populated from facility--->" + getPane().getLOB());
                              }

                        } else if (lob1 != null && !lob1.equalsIgnoreCase("")) {
                              getPane().setLOB(lob1);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("LOB code populated from TI+--->" + getPane().getLOB());
                              }
                        } else if (lob2 != null && !lob2.equalsIgnoreCase("")) {
                              getPane().setLOB(lob2);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("LOB code populated from BCIF--->" + getPane().getLOB());
                              }
                        }

                  } else {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              System.out.println("LOB code else for event status--->" + eventStatus);
                        }
                  }
            } catch (Exception e)

            {
                  System.out.println("Exception in LOB code customer master" + e.getMessage());
                  e.printStackTrace();

            }

            finally

            {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // System.out.println("Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

            return customera;

      }

      public void getLOBCREATE() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String customera = "";
            try {

                  String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                                                                                                                                    // CODE
                                                                                                                                    // ILC
                  if (prodtype.trim().equalsIgnoreCase("IDC")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        customera = getDriverWrapper().getEventFieldAsText("DRE", "p", "no");// party
                                                                                                                                    // id
                  } else if (prodtype.trim().equalsIgnoreCase("ODC")) {

                        customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                                    // id
                  } else if (prodtype.trim().equalsIgnoreCase("OCC")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                                    // id
                  } else if (prodtype.trim().equalsIgnoreCase("CPCI") || prodtype.trim().equalsIgnoreCase("CPBI")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                                    // id
                  } else if (prodtype.trim().equalsIgnoreCase("CPCO") || prodtype.trim().equalsIgnoreCase("CPBO")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");// party
                                                                                                                                    // id
                  } else if (prodtype.trim().equalsIgnoreCase("SHG")) {
                        customera = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");

                  }

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("LOB code Customer id for create event--->" + customera);
                        LOG.info("Get major code for create event--->" + prodtype);
                  }
                  String lobFacility = "";
                  String lob1 = "";
                  String lob2 = "";

                  try {
                        String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim() + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("LOB code from create customer level--->" + dms);
                        }
                        con = getConnection();
                        dmsp = con.prepareStatement(dms);
                        dmsr = dmsp.executeQuery();
                        while (dmsr.next()) {
                              lob1 = dmsr.getString(1);
                              lob2 = dmsr.getString(2);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("LOB code 1 population--->" + lob1);
                              LOG.info("LOB code 2 population--->" + lob2);
                        }

                  } catch (Exception e) {
                        LOG.info("Exception LOB code 1 and 2 population--->" + e.getMessage());
                  }

                  String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  String finaceCreate = getDriverWrapper().getEventFieldAsText("BFC", "l", "");
                  String finaceCollection = getDriverWrapper().getEventFieldAsText("RCFA", "l", "");

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("ODC LOB code finance created=======>" + finaceCreate);
                  }

                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        if ((getMinorCode().equalsIgnoreCase("CRE") && finaceCreate.equalsIgnoreCase("N"))
                                    || getMinorCode().equalsIgnoreCase("PBIC") || getMinorCode().equalsIgnoreCase("PCIC")
                                    || getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC")) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("getMinorCode() for LOB code=======>" + getMinorCode());
                              }
                              if (lob1 != null && !lob1.equalsIgnoreCase("")) {
                                    getPane().setLOB(lob1);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from TI+--->" + getPane().getLOB());
                                    }
                              } else if (lob2 != null && !lob2.equalsIgnoreCase("")) {
                                    getPane().setLOB(lob2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from BCIF--->" + getPane().getLOB());
                                    }
                              }
                        } else if (getMajorCode().equalsIgnoreCase("FOC")) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("getMinorCode() for LOB code=======>" + getMinorCode());
                              }
                              String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                              try {
                                    con = getConnection();
                                    String query = "select e.LOB from master m,BASEEVENT b,extevent e where m.KEY97=b.MASTER_KEY and b.KEY97=e.EVENT and b.REFNO_PFIX='CRE' and b.REFNO_SERL =1 and m.MASTER_REF='"
                                                + mainMasterRef + "'";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code update for finance FOC" + query);
                                    }
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String lobVal = rs1.getString(1);
                                          getPane().setLOB(lobVal);

                                    }
                              }

                              catch (Exception e) {

                                    LOG.info("Exception LOB code update for finance FOC" + e.getMessage());

                              } finally {
                                    try {
                                          if (rs1 != null)
                                                rs1.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();
                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }

                        } else if ((getMajorCode().equalsIgnoreCase("ODC") || getMajorCode().equalsIgnoreCase("IDC"))
                                    && getMinorCode().equalsIgnoreCase("CRE") && finaceCreate.equalsIgnoreCase("Y")) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "getMinorCode() for LOB code finance created=======>" + getMinorCode());
                              }

                              String facility = getWrapper().getLIMITID().trim();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("ODC finance limit id===>" + facility);
                              }
                              if (facility != null && facility.length() > 0) {

                                    String dmsQuery = "select TRIM(LOBCODE) from CUSTOMERMARGIN where CIF ='" + customera.trim()
                                                + "' and FACILITY='" + facility.trim() + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("ODC finance Facility id for LOB code Query===>" + dmsQuery);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dmsQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lobFacility = dmsr.getString(1);

                                    }
                              }

                              try {
                                    String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim()
                                                + "'";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code from customer level CREATE--->" + dms);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dms);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lob1 = dmsr.getString(1);
                                          lob2 = dmsr.getString(2);

                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code 1 population--->" + lob1);
                                          LOG.info("LOB code 2 population--->" + lob2);
                                    }

                              } catch (Exception e) {
                                    LOG.info("Exception LOB code 1 and 2 population--->" + e.getMessage());
                              }

                              if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                                    if (lobFacility != null && !lobFacility.equalsIgnoreCase("")) {

                                          getPane().setLOB(lobFacility);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "LOB code populated from facility--->" + getPane().getLOB());
                                          }

                                    } else if (lob1 != null && !lob1.equalsIgnoreCase("")) {
                                          getPane().setLOB(lob1);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("LOB code populated from TI+--->" + getPane().getLOB());
                                          }
                                    } else if (lob2 != null && !lob2.equalsIgnoreCase("")) {
                                          getPane().setLOB(lob2);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("LOB code populated from BCIF--->" + getPane().getLOB());
                                          }
                                    }

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code else for event status--->" + eventStatus);
                                    }
                              }
                        } else if (getMajorCode().equalsIgnoreCase("SHG") && getMinorCode().equalsIgnoreCase("SCR")) {
                              String facility = getWrapper().getLIMITID().trim();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Shipping guarantee limit id===>" + facility);
                              }
                              if (facility != null && facility.length() > 0) {

                                    String dmsQuery = "select TRIM(LOBCODE) from CUSTOMERMARGIN where CIF ='" + customera.trim()
                                                + "' and FACILITY='" + facility.trim() + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Shipping guarantee Facility id for LOB code Query===>" + dmsQuery);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dmsQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lobFacility = dmsr.getString(1);

                                    }
                              }
                              try {
                                    String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim()
                                                + "'";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Shipping guarantee LOB code from create customer level--->" + dms);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dms);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lob1 = dmsr.getString(1);
                                          lob2 = dmsr.getString(2);

                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code 1 population--->" + lob1);
                                          LOG.info("LOB code 2 population--->" + lob2);
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG,
                                                "Exception LOB code 1 and 2 Shipping guarantee--->" + e.getMessage());
                              }

                              if (lobFacility != null && !lobFacility.equalsIgnoreCase("")) {

                                    getPane().setLOB(lobFacility);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from facility--->" + getPane().getLOB());
                                    }

                              } else if (lob1 != null && !lob1.equalsIgnoreCase("")) {
                                    getPane().setLOB(lob1);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from TI+--->" + getPane().getLOB());
                                    }
                              } else if (lob2 != null && !lob2.equalsIgnoreCase("")) {
                                    getPane().setLOB(lob2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from BCIF--->" + getPane().getLOB());
                                    }
                              }

                        } else if ((getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("FEC")
                                    && finaceCollection.equalsIgnoreCase("Y"))) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "getMinorCode() for LOB code finance created=======>" + getMinorCode());
                              }

                              String facility = getWrapper().getLIMITID().trim();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("ODC finance limit id===>" + facility);
                              }
                              if (facility != null && facility.length() > 0) {

                                    String dmsQuery = "select TRIM(LOBCODE) from CUSTOMERMARGIN where CIF ='" + customera.trim()
                                                + "' and FACILITY='" + facility.trim() + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("ODC finance Facility id for LOB code Query===>" + dmsQuery);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dmsQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lobFacility = dmsr.getString(1);

                                    }
                              }

                              try {
                                    String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim()
                                                + "'";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code from customer level CREATE--->" + dms);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dms);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lob1 = dmsr.getString(1);
                                          lob2 = dmsr.getString(2);

                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code 1 population--->" + lob1);
                                          LOG.info("LOB code 2 population--->" + lob2);
                                    }

                              } catch (Exception e) {
                                    LOG.info("Exception LOB code 1 and 2 population--->" + e.getMessage());
                              }

                              if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                                    if (lobFacility != null && !lobFacility.equalsIgnoreCase("")) {

                                          getPane().setLOB(lobFacility);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "LOB code populated from facility--->" + getPane().getLOB());
                                          }

                                    } else if (lob1 != null && !lob1.equalsIgnoreCase("")) {
                                          getPane().setLOB(lob1);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("LOB code populated from TI+--->" + getPane().getLOB());
                                          }
                                    } else if (lob2 != null && !lob2.equalsIgnoreCase("")) {
                                          getPane().setLOB(lob2);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                LOG.info("LOB code populated from BCIF--->" + getPane().getLOB());
                                          }
                                    }

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code else for event status--->" + eventStatus);
                                    }
                              }

                        }

                  }

            } catch (Exception e)

            {
                  LOG.info("Exception in LOB code customer master" + e.getMessage());
                  e.printStackTrace();

            }

            finally

            {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void getLOBISSUE() {
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");

            String customera = "";
            try {

                  // Cutomer for export lc
                  if ((getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("ISI"))
                              || (getMajorCode().equalsIgnoreCase("IGT") && getMinorCode().equalsIgnoreCase("IIG"))
                              || (getMajorCode().equalsIgnoreCase("ISB") && getMinorCode().equalsIgnoreCase("IIS"))) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("get Major Code for ILC or IGT -----> " + getMajorCode());
                        }
                        customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                                    // id

                        String lob1 = "";
                        String lob2 = "";

                        String lobFacility = "";
                        try {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code Customer id--->" + customera);
                              }
                              String facility = "";
                              try {

                                    String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                                    String dmsQuery = "select lim1.mas_key ,xsa.FACILITYID from (SELECT mas.KEY97 mas_key , mas.master_ref mas_ref, MAX(xsa.KEY29) lim_key FROM master mas, TIDATAITEM tid, MIDXMLMAP xms, XMLAPISTO xsa WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = xms.KEY97 AND xms.APIXML = xsa.KEY29 and mas.MASTER_REF = '"
                                                + MasterReference
                                                + "' GROUP BY mas.KEY97, mas.master_ref )lim1, XMLAPISTO xsa WHERE lim1.lim_key = xsa.KEY29(+)";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Facilityt id for LOB code Query===>" + dmsQuery);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dmsQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          facility = dmsr.getString(2);

                                    }
                              } catch (Exception e) {
                                    LOG.info("Exception Limit id from transaction===>" + e.getMessage());
                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Limit id from transaction for LOB code===>" + facility);
                              }

                              if (facility != null && facility.length() > 0) {

                                    String dmsQuery = "select TRIM(LOBCODE) from CUSTOMERMARGIN where CIF ='" + customera.trim()
                                                + "' and FACILITY='" + facility.trim() + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Facilityt id for LOB code Query===>" + dmsQuery);
                                    }
                                    con = getConnection();
                                    dmsp = con.prepareStatement(dmsQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          lobFacility = dmsr.getString(1);

                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Exception in Facility id for LOB code" + e.getMessage());
                              }
                        }

                        // Changing the LOB code order for JIRA - 5345 as per sign
                        // off
                        // by
                        // Raisa
                        try {
                              String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim() + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code from customer level--->" + lob1);
                              }
                              con = getConnection();
                              dmsp = con.prepareStatement(dms);
                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {
                                    lob1 = dmsr.getString(1);
                                    lob2 = dmsr.getString(2);

                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code 1 population--->" + lob1);
                                    LOG.info("LOB code 2 population--->" + lob2);
                              }

                        } catch (Exception e) {
                              LOG.info("Exception LOB code 1 and 2 population--->" + e.getMessage());
                        }

                        String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
                        String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                        if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                              if (lobFacility != null && !lobFacility.equalsIgnoreCase("")) {

                                    getPane().setLOB(lobFacility);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from facility--->" + getPane().getLOB());
                                    }

                              } else if (lob1 != null && !lob1.equalsIgnoreCase("")) {
                                    getPane().setLOB(lob1);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from TI+--->" + getPane().getLOB());
                                    }
                              } else if (lob2 != null && !lob2.equalsIgnoreCase("")) {
                                    getPane().setLOB(lob2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from BCIF--->" + getPane().getLOB());
                                    }
                              }

                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code else for event status--->" + eventStatus);
                              }
                        }
                  } else if (getMajorCode().equalsIgnoreCase("FIL")) {

                        String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                        String evnt = getDriverWrapper().getEventFieldAsText("EVR", "s", "");
                        try {
                              con = getConnection();
                              String query = "select e.LOB from master m,BASEEVENT b,extevent e where m.KEY97=b.MASTER_KEY and b.KEY97=e.EVENT and b.REFNO_PFIX IN ('ISS','TIG','TIL') and b.REFNO_SERL =1 and m.MASTER_REF='"
                                          + mainMasterRef + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("LOB code update for finance LIC" + query);
                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String lobVal = rs1.getString(1);
                                    getPane().setLOB(lobVal);
                                    LOG.info("Lob in FIL" + getPane().getLOB());

                              }
                              LOG.info("Lob in FIL outside while==========>" + getPane().getLOB());
                        }

                        catch (Exception e) {

                              LOG.info("Exception LOB code update for finance LIC" + e.getMessage());

                        } finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,"Connection Failed! Check
                                    // output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  }
            } catch (Exception e)

            {
                  LOG.info("Exception in LOB code for ILC and IGT" + e.getMessage());
                  e.printStackTrace();

            }

            finally

            {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void getLobFSA() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {
                  String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
                  String customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
                                                                                                                                    // id

                  String lob1 = "";
                  String lob2 = "";

                  String lobFacility = "";
                  try {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("LOB code Customer id for FSA--->" + customera);
                        }
                        String facility = "";
                        try {

                              String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                              String dmsQuery = "select lim1.mas_key ,xsa.FACILITYID from (SELECT mas.KEY97 mas_key , mas.master_ref mas_ref, MAX(xsa.KEY29) lim_key FROM master mas, TIDATAITEM tid, MIDXMLMAP xms, XMLAPISTO xsa WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = xms.KEY97 AND xms.APIXML = xsa.KEY29 and mas.MASTER_REF = '"
                                          + MasterReference
                                          + "' GROUP BY mas.KEY97, mas.master_ref )lim1, XMLAPISTO xsa WHERE lim1.lim_key = xsa.KEY29(+)";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Facilityt id for LOB code Query for FSA===>" + dmsQuery);
                              }
                              con = getConnection();
                              dmsp = con.prepareStatement(dmsQuery);
                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {
                                    facility = dmsr.getString(2);

                              }
                        } catch (Exception e) {
                              LOG.info("Exception Limit id from transaction for FSA===>" + e.getMessage());
                        }

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Limit id from transaction for LOB code FSA===>" + facility);
                        }

                        if (facility != null && facility.length() > 0) {

                              String dmsQuery = "select TRIM(LOBCODE) from CUSTOMERMARGIN where CIF ='" + customera.trim()
                                          + "' and FACILITY='" + facility.trim() + "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Facilityt id for LOB code Query FSA===>" + dmsQuery);
                              }
                              con = getConnection();
                              dmsp = con.prepareStatement(dmsQuery);
                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {
                                    lobFacility = dmsr.getString(1);

                              }
                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("Exception in Facility id for LOB code FSA" + e.getMessage());
                        }
                  }

                  try {
                        String dms = "select TRIM(LOB),TRIM(LOBBCIF) from extcust where cust='" + customera.trim() + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("LOB code from customer level FSA--->" + dms);
                        }
                        con = getConnection();
                        dmsp = con.prepareStatement(dms);
                        dmsr = dmsp.executeQuery();
                        while (dmsr.next()) {
                              lob1 = dmsr.getString(1);
                              lob2 = dmsr.getString(2);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("LOB code 1 population FSA--->" + lob1);
                              LOG.info("LOB code 2 population FSA--->" + lob2);
                        }

                  } catch (Exception e) {
                        LOG.info("Exception LOB code 1 and 2 population FSA--->" + e.getMessage());
                  }

                  String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  String lob_value = getWrapper().getLOB().trim();
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        if (getMinorCode().equalsIgnoreCase("CSA")) {
                              if (lobFacility != null && !lobFacility.equalsIgnoreCase("")) {

                                    getPane().setLOB(lobFacility);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from facility--->" + getPane().getLOB());
                                    }

                              } else if (lob1 != null && !lob1.equalsIgnoreCase("")) {
                                    getPane().setLOB(lob1);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from TI+--->" + getPane().getLOB());
                                    }
                              } else if (lob2 != null && !lob2.equalsIgnoreCase("")) {
                                    getPane().setLOB(lob2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("LOB code populated from BCIF--->" + getPane().getLOB());
                                    }
                              }
                        } else if ((getMinorCode().equalsIgnoreCase("JSA") || getMinorCode().equalsIgnoreCase("ASA")
                                    || getMinorCode().equalsIgnoreCase("SSA") || getMinorCode().equalsIgnoreCase("RSA"))
                                    && getMajorCode().equalsIgnoreCase("FSA")) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    LOG.info("Get the LOB for FSA else if loop---->" + getMinorCode());
                              }
                              String fin_lob = getWrapper().getFINLOB().trim();
                              if (fin_lob == null || fin_lob.equalsIgnoreCase("")) {
                                    // String lob = getWrapper().getLOB().trim();
                                    String lobVal = getDriverWrapper().getEventFieldAsText("cARS", "s", "").trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          LOG.info("Get the lob from master LOB details---->" + lobVal);
                                    }
                                    if (!lobVal.equalsIgnoreCase("") && lobVal.length() > 0) {
                                          getPane().setFINLOB(lobVal);
                                    }

                              } else if (fin_lob != null && fin_lob.length() > 0) {
                                    getPane().setFINLOB(fin_lob);
                              }

                        }
                  } else {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("LOB code else for event status FSA--->" + eventStatus);
                        }

                  }
            } catch (Exception e) {
                  LOG.info("Exception in LOB for FSA---->" + e.getMessage());
            } finally

            {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void getPeriodicAdv() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {

                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  /*
                   * String dmsQuery =
                   * "SELECT CSHC.ADV_ARR, CSHC.WHEN_COLL, CSHC.PERIODIC, BEV.REFNO_PFIX,BEV.REFNO_SERL,MAS.MASTER_REF FROM MASTER MAS,BASEEVENT BEV,RELITEM rel ,eventchg ECHR,CHGSCHED CSHC,CHGSCHTBAS CBAS WHERE MAS.KEY97=BEV.MASTER_KEY AND BEV.KEY97=rel.EVENT_KEY AND REL.KEY97=ECHR.KEY97 AND ECHR.CHG_SCH=CSHC.KEY97 AND CSHC.KEY97=CBAS.KEY97 AND CSHC.PERIODIC ='Y' AND MAS.MASTER_REF='"
                   * + MasterReference +
                   * "' AND BEV.REFNO_PFIX='ISS' AND MAS.STATUS='LIV' AND BEV.STATUS in ('c','i')"
                   */;

                  String dmsQuery = "SELECT CSHC.ADV_ARR, CSHC.WHEN_COLL, CSHC.PERIODIC, BEV.REFNO_PFIX,BEV.REFNO_SERL,MAS.MASTER_REF FROM MASTER MAS,BASEEVENT BEV,RELITEM rel ,eventchg ECHR,CHGSCHED CSHC,CHGSCHTBAS CBAS"
                              + " WHERE MAS.KEY97=BEV.MASTER_KEY AND BEV.KEY97=rel.EVENT_KEY AND REL.KEY97=ECHR.KEY97 "
                              + " AND ECHR.CHG_SCH=CSHC.KEY97 AND CSHC.KEY97=CBAS.KEY97 AND CSHC.PERIODIC ='Y' AND MAS.MASTER_REF='"
                              + MasterReference + "'"
                              + " AND BEV.REFNO_PFIX IN ('TIL','TIG','ISS') AND MAS.STATUS='LIV' AND BEV.STATUS in ('c','i') ";

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("Periodic Advance/Arrears ===>" + dmsQuery);
                  }
                  String adv_Arr = "";
                  con = getConnection();
                  dmsp = con.prepareStatement(dmsQuery);
                  dmsr = dmsp.executeQuery();
                  while (dmsr.next()) {
                        adv_Arr = dmsr.getString(1).trim();

                  }
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("Periodic Advance/Arrears value===>" + adv_Arr);
}
if (adv_Arr.equalsIgnoreCase("E")) {
      getPane().setLOBCODE("Arrears");
} else if (adv_Arr.equalsIgnoreCase("S")) {
      getPane().setLOBCODE("Advance");
}

} catch (Exception e) {
LOG.info("Exception Periodic Advance/Arrears===>" + e.getMessage());
} finally {
try {
      if (dmsr != null)
            dmsr.close();
      if (dmsp != null)
            dmsp.close();
      if (con != null)
            con.close();
} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}
}

// Arunkumar M 14-07-2021
public void getChargeBasisAmountSplit() { // Charge basis amount splittation
if (((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IGT")
      || getMajorCode().equalsIgnoreCase("IDC")))
      && ((getMinorCode().equalsIgnoreCase("ISI") || getMinorCode().equalsIgnoreCase("NADI")
                  || getMinorCode().equalsIgnoreCase("NAMI") || getMinorCode().equalsIgnoreCase("IIG")
                  || getMinorCode().equalsIgnoreCase("NJIG") || getMinorCode().equalsIgnoreCase("NAIG")
                  || getMinorCode().equalsIgnoreCase("CAC")))) {
//if(((getMajorCode().equalsIgnoreCase("ILC")||getMajorCode().equalsIgnoreCase("IGT"))) && ((getMinorCode().equalsIgnoreCase("ISI")||getMinorCode().equalsIgnoreCase("NADI")||getMinorCode().equalsIgnoreCase("NAMI")||getMinorCode().equalsIgnoreCase("IIG")||getMinorCode().equalsIgnoreCase("NJIG")||getMinorCode().equalsIgnoreCase("NAIG")))) {
Connection conc1 = null;
ResultSet result1 = null;
PreparedStatement pst1 = null;
BigDecimal fxRate = new BigDecimal(0);
DecimalFormat df = new DecimalFormat("##,##,##0.##");
String lcAmt = getDriverWrapper().getEventFieldAsText("MAL", "v", "m");
String lcAmtCcy = getDriverWrapper().getEventFieldAsText("MAL", "v", "c");
String oldlcAmt = getDriverWrapper().getEventFieldAsText("oMAL", "v", "m");
String oldlcAmtCcy = getDriverWrapper().getEventFieldAsText("oMAL", "v", "c");
String incrOrDcr = getDriverWrapper().getEventFieldAsText("DELD", "s", "");
String lcCcy = "INR";
BigDecimal lcChargeBasisAmount = new BigDecimal(0);
BigDecimal lcAmount = new BigDecimal(0);
BigDecimal oldlcChargeBasisAmount = new BigDecimal(0);
BigDecimal oldlcAmount = new BigDecimal(0);
BigDecimal uptoFivCr = new BigDecimal(0);
BigDecimal fivToTenCr = new BigDecimal(0);
BigDecimal aboveTenCr = new BigDecimal(0);
BigDecimal oldUptoFivCr = new BigDecimal(0);
BigDecimal oldFivToTenCr = new BigDecimal(0);
BigDecimal oldAboveTenCr = new BigDecimal(0);
BigDecimal incUptoFivCr = new BigDecimal(0);
BigDecimal incFivToTenCr = new BigDecimal(0);
BigDecimal incAboveTenCr = new BigDecimal(0);

try {
      LOG.info("Inside ChargeBasisAmountSplit");
      conc1 = ConnectionMaster.getConnection();
      String query = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='" + lcAmtCcy + "'";
//    System.out.println(query);
      pst1 = conc1.prepareStatement(query);
      result1 = pst1.executeQuery();
      while (result1.next()) {
            fxRate = new BigDecimal(result1.getString(1));
      }
} catch (Exception e) {
      e.printStackTrace();
} finally {
      ConnectionMaster.surrenderDB(conc1, pst1, result1);
}

if (lcAmtCcy != null && lcAmtCcy.trim().equals(lcCcy)) {
      lcAmount = new BigDecimal(lcAmt); // NO FX FOR INR CURRENCY
} else {
      if (lcAmt != null && !lcAmt.trim().equals("")) {
            lcChargeBasisAmount = new BigDecimal(lcAmt);

            lcAmount = lcChargeBasisAmount.multiply(fxRate);
      }
}

BigDecimal fiveCr = new BigDecimal(50000000);
BigDecimal tenCr = new BigDecimal(100000000);

if (lcAmount.compareTo(fiveCr) == 0 || fiveCr.compareTo(lcAmount) == 1) { // IF LC AMOUNT IS EQUAL OR NOT
                                                                                                                  // GREATER THAN 5 Cr
      String value = df.format(lcAmount) + " " + lcCcy;
      getPane().setUPTO5CR(value);
      getPane().setFIVT10CR("");
      getPane().setABOV10CR("");
      uptoFivCr = lcAmount;
} else if (fiveCr.compareTo(lcAmount) == -1) { // IF LC AMOUNT IS GREATER THAN 5 Cr

      BigDecimal balance = lcAmount.subtract(fiveCr);

      if (fiveCr.compareTo(balance) == 1) { // IF LC AMOUNT IS NOT GREATER THAN 10 Cr
            String value = df.format(fiveCr) + " " + lcCcy;
            getPane().setUPTO5CR(value);
            String bal = df.format(balance) + " " + lcCcy;
            getPane().setFIVT10CR(bal);
            uptoFivCr = fiveCr;
            fivToTenCr = balance;
      } else if (tenCr.compareTo(lcAmount) == 0) { // IF LC AMOUNT IS EQUAL TO 10 Cr
            String value1 = df.format(fiveCr) + " " + lcCcy;
            getPane().setUPTO5CR(value1);
            getPane().setFIVT10CR(value1);
            getPane().setABOV10CR("");
            uptoFivCr = fiveCr;
            fivToTenCr = fiveCr;
      } else if (tenCr.compareTo(lcAmount) == -1) { // IF LC AMOUNT IS GREATER THAN 10 Cr
            String value1 = df.format(fiveCr) + " " + lcCcy;
            getPane().setUPTO5CR(value1);
            getPane().setFIVT10CR(value1);
            balance = lcAmount.subtract(tenCr);
            String bal = df.format(balance) + " " + lcCcy;
            getPane().setABOV10CR(bal);
            uptoFivCr = fiveCr;
            fivToTenCr = fiveCr;
            aboveTenCr = balance;
      }
}
// Increase&Old Charge basis amount Calculation - Vaisak 240921
if (((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IGT")))
            && ((getMinorCode().equalsIgnoreCase("NADI") || getMinorCode().equalsIgnoreCase("NAMI")
                        || getMinorCode().equalsIgnoreCase("NJIG") || getMinorCode().equalsIgnoreCase("NAIG")))) {
      System.out.println("first IF");
      if (incrOrDcr.equalsIgnoreCase("Increase")) {
            System.out.println("Increase");
            try {
                  System.out.println("Inside ChargeBasisAmountSplit - Increase Calculation");
                  LOG.info("Inside ChargeBasisAmountSplit - Increase Calculation");
                  conc1 = ConnectionMaster.getConnection();
                  String query = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='" + oldlcAmtCcy
                              + "'";
//          System.out.println(query);
                  pst1 = conc1.prepareStatement(query);
                  result1 = pst1.executeQuery();
                  while (result1.next()) {
                        fxRate = new BigDecimal(result1.getString(1));
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            } finally {
                  ConnectionMaster.surrenderDB(conc1, pst1, result1);
            }
            if (oldlcAmtCcy != null && oldlcAmtCcy.trim().equals(lcCcy)) {
                  oldlcAmount = new BigDecimal(oldlcAmt); // NO FX FOR INR CURRENCY
            } else {
                  if (oldlcAmt != null && !oldlcAmt.trim().equals("")) {
                        oldlcChargeBasisAmount = new BigDecimal(oldlcAmt);
                        oldlcAmount = oldlcChargeBasisAmount.multiply(fxRate);
                  }
            }
            if (oldlcAmount.compareTo(fiveCr) == 0 || fiveCr.compareTo(oldlcAmount) == 1) { // IF LC AMOUNT IS
                                                                                                                                    // EQUAL OR LESSER
                                                                                                                                    // THAN 5 Cr
                  oldUptoFivCr = oldlcAmount;
                  String value1 = df.format(oldUptoFivCr) + " " + lcCcy;
                  getPane().setOLDUP5CR(value1);

            } else if (fiveCr.compareTo(oldlcAmount) == -1) { // IF LC AMOUNT IS GREATER THAN 5 Cr

                  BigDecimal balance = oldlcAmount.subtract(fiveCr);

                  if (fiveCr.compareTo(balance) == 1) { // IF LC AMOUNT IS NOT GREATER THAN 10 Cr
                        oldUptoFivCr = fiveCr;
                        oldFivToTenCr = balance;
                        String value1 = df.format(oldUptoFivCr) + " " + lcCcy;
                        String value2 = df.format(oldFivToTenCr) + " " + lcCcy;
                        getPane().setOLDUP5CR(value1);
                        getPane().setOLD5TO10(value2);

                  } else if (tenCr.compareTo(oldlcAmount) == 0) { // IF LC AMOUNT IS EQUAL TO 10 Cr
                        oldUptoFivCr = fiveCr;
                        oldFivToTenCr = fiveCr;
                        String value1 = df.format(oldUptoFivCr) + " " + lcCcy;
                        getPane().setOLDUP5CR(value1);
                        getPane().setOLD5TO10(value1);

                  } else if (tenCr.compareTo(oldlcAmount) == -1) { // IF LC AMOUNT IS GREATER THAN 10 Cr
                        oldUptoFivCr = fiveCr;
                        oldFivToTenCr = fiveCr;
                        balance = oldlcAmount.subtract(tenCr);
                        oldAboveTenCr = balance;
                        String value1 = df.format(oldUptoFivCr) + " " + lcCcy;
                        String value2 = df.format(oldAboveTenCr) + " " + lcCcy;
                        getPane().setOLDUP5CR(value1);
                        getPane().setOLD5TO10(value1);
                        getPane().setOLDABV10(value2);
                  }
            }
            incUptoFivCr = uptoFivCr.subtract(oldUptoFivCr);
            String value1 = df.format(incUptoFivCr) + " " + lcCcy;
            getPane().setINCUPTO5(value1);
            incFivToTenCr = fivToTenCr.subtract(oldFivToTenCr);
            String value2 = df.format(incFivToTenCr) + " " + lcCcy;
            getPane().setINC5TO10(value2);
            incAboveTenCr = aboveTenCr.subtract(oldAboveTenCr);
            String value3 = df.format(incAboveTenCr) + " " + lcCcy;
            getPane().setINABOV10(value3);
      }
//    else if(incrOrDcr.equalsIgnoreCase("Decrease")){
      else {
            getPane().setINCUPTO5("");
            getPane().setINC5TO10("");
            getPane().setINABOV10("");

      }
}
}
}
// Arunkumar M 14-07-2021

public void getrbiPurcodeCode() {

String strLog = "Log";
String dailyval_Log = "";
@SuppressWarnings("unchecked")
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
      .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
EXTGENCUSTPROP CodeLog = queryLog.getUnique();
if (CodeLog != null) {

dailyval_Log = CodeLog.getPropval();
} else {
// Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

}
try {

String partyDetail = "IN";
String rbiCode = getWrapper().getOPURPOS_Name().trim();
String proType = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
String pay_finance = getDriverWrapper().getEventFieldAsText("BFRC", "l", "");
String mertchnat = getDriverWrapper().getEventFieldAsText("cARQ", "l", "");
String mertch = getDriverWrapper().getEventFieldAsText("cAXS", "l", "");

if (getMajorCode().equalsIgnoreCase("FOC") || getMajorCode().equalsIgnoreCase("FEL")
            || getMinorCode().equalsIgnoreCase("FEC")) {
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code FEL" + rbiCode);
      }
      partyDetail = getDriverWrapper().getEventFieldAsText("B+FT", "p", "cc");
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("P0101");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }
} else if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")
            && pay_finance.equalsIgnoreCase("N"))

{
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code ODC" + rbiCode);
      }
      partyDetail = getDriverWrapper().getEventFieldAsText("DRW", "p", "cc");
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("P0102");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }
}

else if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")
            && pay_finance.equalsIgnoreCase("N")) {
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code ELC" + rbiCode);
      }
      partyDetail = getDriverWrapper().getEventFieldAsText("PRM", "p", "cc");

      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("P0102");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

}

else if (getMajorCode().equalsIgnoreCase("CPCI") && proType.equalsIgnoreCase("XAR")) {
      partyDetail = getDriverWrapper().getEventFieldAsText("BEN", "p", "cc");
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code CPCI" + rbiCode);
      }
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("P0103");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

} else if (getMajorCode().equalsIgnoreCase("CPCO") && proType.equalsIgnoreCase("AIR")) {
      partyDetail = getDriverWrapper().getEventFieldAsText("RMIT", "p", "cc");
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code CPCO" + rbiCode);
      }
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("S0101");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

}

else if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("DOP")
            && mertch.equalsIgnoreCase("Y") && !mertch.equalsIgnoreCase("N")) {
      partyDetail = getDriverWrapper().getEventFieldAsText("PRM", "p", "cc");
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code ELC" + rbiCode);
      }
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("P0108");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

}

else if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")
            && mertchnat.equalsIgnoreCase("Y") && !mertchnat.equalsIgnoreCase("N")) {
      partyDetail = getDriverWrapper().getEventFieldAsText("DRW", "p", "cc");
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code ODC" + rbiCode);
      }
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("P0108");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

}

else if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("POC")) {
      partyDetail = getDriverWrapper().getEventFieldAsText("APP", "p", "cc");
      {
            LOG.info("Purpose code ILC" + rbiCode);
      }
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("S0102");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

}

else if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CLP")) {
      partyDetail = getDriverWrapper().getEventFieldAsText("DRE", "p", "cc");
      {
            LOG.info("Purpose code IDC" + rbiCode);
      }
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("S0102");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

}

else if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("CRC")
            && mertch.equalsIgnoreCase("Y") && !mertch.equalsIgnoreCase("N")) {
      partyDetail = getDriverWrapper().getEventFieldAsText("BEN", "p", "cc");
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code ILC" + rbiCode);
      }
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("S0108");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

} else if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CRE")
            && mertchnat.equalsIgnoreCase("Y") && !mertchnat.equalsIgnoreCase("N")) {
      partyDetail = getDriverWrapper().getEventFieldAsText("DRE", "p", "cc");
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code IDC" + rbiCode);
      }
      if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && !partyDetail.equalsIgnoreCase("BT")
                  && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name("S0108");
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0)
                  && !partyDetail.equalsIgnoreCase("BT") && !partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      } else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && partyDetail.equalsIgnoreCase("BT")
                  && partyDetail.equalsIgnoreCase("NP")) {
            getPane().setOPURPOS_Name(rbiCode);
      }

}

else if ((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1) && proType.equalsIgnoreCase("DIR")
            && (getMajorCode().equalsIgnoreCase("CPBO") || getMajorCode().equalsIgnoreCase("CPCO"))) {
      // //Loggers.general().info(LOG,"productyp AIR---> " + productyp);
      getPane().setOPURPOS_Name("S0102");
}

else if ((!rbiCode.equalsIgnoreCase("") || rbiCode.length() > 0) && proType.equalsIgnoreCase("DIR")
            && (getMajorCode().equalsIgnoreCase("CPBO") || getMajorCode().equalsIgnoreCase("CPCO"))) { // //Loggers.general().info(LOG,"productyp
      rbiCode = getWrapper().getOPURPOS_Name().trim();
      getPane().setOPURPOS_Name(rbiCode);
}

else {
      String rbiCod = getWrapper().getOPURPOS_Name().trim();
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Purpose code else" + rbiCod);
      }
      getPane().setOPURPOS_Name(rbiCod);
}

/*
 * else
 * if(getMajorCode().equalsIgnoreCase("ILC")||getMajorCode().equalsIgnoreCase(
 * "IDC")) { if((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1)) {
 * getPane().setOPURPOS_Name("S0105");
 * Loggers.general().info(LOG,"Purpose code for ILC and IDC"+getPane().
 * getOPURPOS_Name()); } else { getPane().setOPURPOS_Name(rbiCode);
 * Loggers.general().info(LOG,"Purpose code for ILC and IDC in else"+getPane().
 * getOPURPOS_Name()); } } else
 * if(getMajorCode().equalsIgnoreCase("ELC")||getMajorCode().equalsIgnoreCase(
 * "ODC")) { if((rbiCode.equalsIgnoreCase("") || rbiCode.length() < 1)) {
 * getPane().setOPURPOS_Name("P0105");
 * Loggers.general().info(LOG,"Purpose code for ELC and ODC"+getPane().
 * getOPURPOS_Name()); } else { getPane().setOPURPOS_Name(rbiCode);
 * Loggers.general().info(LOG,"Purpose code for ELC and ODC in else"+getPane().
 * getOPURPOS_Name()); } } else { String rbiCod =
 * getWrapper().getOPURPOS_Name().trim(); if
 * (dailyval_Log.equalsIgnoreCase("YES")) {
 * Loggers.general().info(LOG,"Purpose code else" + rbiCod); }
 * getPane().setOPURPOS_Name(rbiCod); }
 */
} catch (Exception e) {
LOG.info("Exception Purpose code" + e.getMessage());

}

}

// Shipping Table
// public boolean GRREGSHP() {
// boolean value = false;
// String returns = "true";
// try {
//
// con = getConnection();
// String dms = "select GRNUMBER from ETT_GR_SHP_TBL where BILLREFNO='" +
// getmasRefNo() + "' and eventRefNo='"
// + geteventRefNo() + "'";
// dmsp = con.prepareStatement(dms);
//
// dmsr = dmsp.executeQuery();
// while (dmsr.next()) {
// String dmsstr = dmsr.getString(1);
// // //Loggers.general().info(LOG,dms);
// if (!(dmsstr.trim().equalsIgnoreCase(null))) {
// returns = dmsstr;
// value = true;
// }
// }
//
// // con.close();
// // dmsp.close();
// // dmsr.close();
// } catch (Exception e) {
// // Loggers.general().info(LOG,e.getMessage());
// // //Loggers.general().info(LOG,"GR Number Ending");
// }
//
// finally {
// try {
// if (dmsr != null)
// dmsr.close();
// if (dmsp != null)
// dmsp.close();
// if (con != null)
// con.close();
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check output
// // console");
// e.printStackTrace();
// }
// }
// // //Loggers.general().info(LOG,"GR Number ETT_GR_SHP_TBL" + returns+ value);
//
// return value;
// }

// IFSCC CODE VALIDATION
public void getIFSC() {
String ifsc = getDriverWrapper().getEventFieldAsText("cBYQ", "s", "");
getPane().setDUMMYBIC("");
try {
con = ConnectionMaster.getConnection();
String query1 = "select TIIFSC from ETTIFSCMAP where REALIFSC= '" + ifsc + "'";
System.out.println(" ifsc query: " + query1 + " " + ifsc);
ps1 = con.prepareStatement(query1);
rs1 = ps1.executeQuery();
while (rs1.next()) {
      String biccode = rs1.getString(1);

      // Loggers.general().info(LOG,"Customer" + cust);
      System.out.println(" ifsc data: " + biccode);
      getPane().setDUMMYBIC(biccode);

}
} catch (Exception e) {
e.printStackTrace();
// Loggers.general().info(LOG,"exception caught");
} finally {
ConnectionMaster.surrenderDB(con, ps, rs);
ConnectionMaster.surrenderDB(con, ps1, rs1);
}

try {
getPane().setBICCODE("");
con = ConnectionMaster.getConnection();
String ifsc1 = getDriverWrapper().getEventFieldAsText("cBYS", "s", "");
String query1 = "select TIIFSC from ETTIFSCMAP where REALIFSC= '" + ifsc1 + "'";
System.out.println(" ifsc query OF ADVISE THROUGH: " + query1 + " " + ifsc1);
ps1 = con.prepareStatement(query1);
rs1 = ps1.executeQuery();
while (rs1.next()) {
      String biccode = rs1.getString(1);

      // Loggers.general().info(LOG,"Customer" + cust);
      System.out.println(" ifsc data ADVISE THROUGH: " + biccode);
      getPane().setBICCODE(biccode);

}
} catch (Exception e) {
e.printStackTrace();
// Loggers.general().info(LOG,"exception caught");
} finally {
ConnectionMaster.surrenderDB(con, ps, rs);
ConnectionMaster.surrenderDB(con, ps1, rs1);
}

}

// public boolean validateIFSC(ValidationDetails validationDetails) {
// String senIFSC = "";
// PreparedStatement ps1 = null;
// ResultSet rs1 = null;
// if (!(getMajorCode().equalsIgnoreCase("FEL") ||
// getMajorCode().equalsIgnoreCase("FIC")
// || getMajorCode().equalsIgnoreCase("FIL") ||
// getMajorCode().equalsIgnoreCase("FOC")
// || getMajorCode().equalsIgnoreCase("FSA") ||
// getMajorCode().equalsIgnoreCase("FEL")
// || getMinorCode().equalsIgnoreCase("NCAJ") ||
// getMinorCode().equalsIgnoreCase("EXC")
// || getMinorCode().equalsIgnoreCase("NCAJ") ||
// getMinorCode().equalsIgnoreCase("NCAM")
// || getMinorCode().equalsIgnoreCase("EXP") ||
// getMinorCode().equalsIgnoreCase("FLC")
// || getMinorCode().equalsIgnoreCase("OEG") ||
// getMinorCode().equalsIgnoreCase("MIG")
// || getMinorCode().equalsIgnoreCase("XIG") ||
// getMinorCode().equalsIgnoreCase("AOGL")
// || getMinorCode().equalsIgnoreCase("COCL"))) {
// try {
// con = getConnection();
// String Bcode = getDriverWrapper().getEventFieldAsText("BOB", "b", "c");
// // //Loggers.general().info(LOG,"the branch code" + Bcode);
// String query = "select trim(ifsc) from EXTBRAMAS where trim(BCODE)='" +
// Bcode.trim() + "'";
// //Loggers.general().info(LOG,"query " + query);
// ps1 = con.prepareStatement(query);
// rs1 = ps1.executeQuery();
//
// while (rs1.next()) {
// // //Loggers.general().info(LOG,"Entered while");
// senIFSC = rs1.getString(1);
// }
// getWrapper().setSENIFSC(senIFSC);
//
// con.close();
// ps1.close();
// rs1.close();
//
// }
//
// catch (Exception e) {
// //Loggers.general().info(LOG,"exception in sender ifsc code" +
// e.getMessage());
//
// } finally {
// surrenderDB(con, ps1, rs1);
// }
//
// }
// // Receiver IFSC code validation
// String recIfsc = getWrapper().getRECIFSC();
// // String recIfsc1=recIfsc.toUpperCase().substring(0, 4);//Splitting
// // first 4 characters
// // String recIfsc2=recIfsc.substring(4, recIfsc.length());//getting the
// // other number after the 4 characters
// if (recIfsc.trim().equalsIgnoreCase("") || recIfsc == null) {
//
// } else {
// if (!recIfsc.trim().equalsIgnoreCase("") || recIfsc != null) {
// try {
// con = getConnection();
// String query = "select count(*) from ETT_IFSC_TBL where IFSC='" +
// recIfsc.trim() + "'";
// //Loggers.general().info(LOG,"query " + query);
// int count = 0;
// ps1 = con.prepareStatement(query);
// rs1 = ps1.executeQuery();
// while (rs1.next()) {
// // //Loggers.general().info(LOG,"Entered while");
// count = rs1.getInt(1);
// // //Loggers.general().info(LOG,"value of count in while "+count);
// }
//
// if (count == 0) {
// // //Loggers.general().info(LOG,"if in IFSC");
// }
// con.close();
// ps1.close();
// rs1.close();
//
// } catch (Exception e1) {
// //Loggers.general().info(LOG,"Exception" + e1.getMessage());
// } finally {
// surrenderDB(con, ps1, rs1);
// }
// }
//
// }
//
// try { // checking Proceed Remittant(Code)
// String procrem = getDriverWrapper().getEventFieldAsText("PUL1", "s", "");
// String procrem2 = procrem.substring(0, 4);
// // //Loggers.general().info(LOG,"checking Proceed Remittant(Code)"+procrem);
// //Loggers.general().info(LOG,"checking Proceed Remittant(Code) substring" +
// procrem2);
// String ifsccode = getWrapper().getIFSCCO_Name();
//
// // //Loggers.general().info(LOG,"Value of the IFSC Code entered "+ifsccode);
// String ifsccode1 = ifsccode.toUpperCase().substring(0, 4);
// //Loggers.general().info(LOG,"Checking RTGS or NEFT " + ifsccode1);
// String ifsccode2 = ifsccode.toUpperCase().substring(4,
// ifsccode.length());
// //Loggers.general().info(LOG,"Checking RTGS or NEFT " + ifsccode2);
//
// if (ifsccode1.equalsIgnoreCase(procrem2)) {
// try {
// Connection con = getConnection();
// // //Loggers.general().info(LOG,"trim value of
// // ifsccode2"+ifsccode2.trim());
// String query = "select count(*) from ETT_IFSC_TBL where IFSC='" +
// ifsccode2.trim() + "'";
// int count = 0;
// //Loggers.general().info(LOG,"query - " + query);
// ps1 = con.prepareStatement(query);
// rs1 = ps1.executeQuery();
// PreparedStatement ps2 = con.prepareStatement(query);
// ResultSet rs2 = ps2.executeQuery();
// //Loggers.general().info(LOG,"rs2.next() - " + rs2.next());
// // //Loggers.general().info(LOG,"Before entering into while");
// while (rs1.next()) {
// // //Loggers.general().info(LOG,"Entered while");
// count = rs1.getInt(1);
// // //Loggers.general().info(LOG,"value of count in while "+count);
// }
//
// if (count == 0) {
//
// }
//
// con.close();
// ps1.close();
// rs1.close();
// } catch (Exception ee) {
// //Loggers.general().info(LOG,"Error in try in side try" + ee.getMessage());
// }
// } else {
//
// }
// } finally {
// surrenderDB(con, ps2, rs2);
// // //Loggers.general().info(LOG,"Finally for validating the IFSCC code");
// }
// return true;
// }

// currency validation 24/09/16

public double getDecimalforCurrency(String curr) {
double deci = 0.0;
Connection conn = null;
PreparedStatement pre = null;
ResultSet res = null;
try {
conn = ConnectionMaster.getConnection();
String query = "select power(10,C8CED) from C8PF where c8ccy='" + curr + "'";
LOG.info("Qurey is " + query);
pre = conn.prepareStatement(query);
res = pre.executeQuery();
if (res.next()) {
      deci = res.getDouble(1);
}

} catch (Exception e) {
LOG.info("Exception is power value" + e.getMessage());
}

finally {
try {

      if (res != null)
            res.close();
      if (pre != null)
            pre.close();
      if (conn != null)
            conn.close();

} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}
return deci;
}

public String getDecimalforCur(String fullcur) {
String deci = "";
Connection conn = null;
PreparedStatement pre = null;
ResultSet res = null;
try {

// //Loggers.general().info(LOG,"get connection ----------->");
conn = ConnectionMaster.getConnection();
String query = "select power(10,C8CED) from C8PF where c8ccy='" + fullcur + "'";
// //Loggers.general().info(LOG,"Qurey is in sample" + query);
pre = conn.prepareStatement(query);
res = pre.executeQuery();
if (res.next()) {
      deci = res.getString(1);
}

// conn.close();
// pre.close();
// res.close();
} catch (Exception e) {
LOG.info("Exception is power value" + e.getMessage());
} finally {
try {

      if (res != null)
            res.close();
      if (pre != null)
            pre.close();
      if (conn != null)
            conn.close();

} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}

return deci;
}

// FCY Tax calculation
public void getFCCTCALCULATION() {
try {

String strLog = "Log";
String dailyval_Log = "";
@SuppressWarnings("unchecked")
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
            .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
EXTGENCUSTPROP CodeLog = queryLog.getUnique();
if (CodeLog != null) {

      dailyval_Log = CodeLog.getPropval();
} else {
      // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

}
String proType = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
LOG.info("Product option for FCCT" + proType);
if (getMajorCode().equalsIgnoreCase("FIL") || proType.equalsIgnoreCase("CRY")) {
      LOG.info("FCCT Calculation " + getMajorCode() + " " + getMinorCode() + " " + getmasRefNo() + " "
                  + geteventRefNo());

      String fcct_Cal = getDriverWrapper().getEventFieldAsText("B+DA", "v", "m").trim();
      String fcct_CalCcy = getDriverWrapper().getEventFieldAsText("B+DA", "v", "c").trim();
      // Loggers.general().info(LOG,"FCCT Amount initial FIL===> " +
      // fcct_Cal);

      if (!fcct_Cal.equalsIgnoreCase("") && fcct_Cal != null && fcct_Cal.length() > 0) {

            con = getConnection();
            String query = "SELECT ETT_FCCT_CALCULATION('" + fcct_Cal + "') FROM DUAL";
            LOG.info("ETT_FCCT_CALCULATION function FIL or CRY===> " + query);
            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                  String fcct_val = rs1.getString(1);
                  // Loggers.general().info(LOG,"FCCT Amount in string===> " +
                  // fcct_val);
                  BigDecimal finalvalue = new BigDecimal(fcct_val);
                  DecimalFormat diff = new DecimalFormat("0.00");
                  diff.setMaximumFractionDigits(2);
                  String finval = diff.format(finalvalue);

                  LOG.info("FCCT Amount in Bigdecimal===> " + finval);

                  getPane().setFXSVTX(finval + " INR");

                  String fxrate = getDriverWrapper().getEventFieldAsText("cAPD", "v", "m");

                  // Loggers.general().info(LOG,"fxrate Amount in string===> " +
                  // fxrate);
                  String fcctval = " Calculation details " + "\n"
                              + "1.Upto 100,000 INR Apply 1% (minimum 250 INR) " + "\n" + "2.Upto 10,00,000 INR "
                              + "\n" + "Apply 1000 INR + 0.5% on the value exceeding 100,000 INR " + "\n"
                              + "3.Above 10,00,000 INR " + "\n"
                              + "Apply 5500 INR + 0.1% on the value exceeding 10,00,000 INR (maximum 60,000 INR) "
                              + "\n" + "\n" + "For Example" + "\n"
                              + "INR equivalent amount to calculation FCY conversion tax = " + fcct_Cal + " INR"
                              + "\n" + "Up to 100,000.00 x 1% (min 250 INR) = 1,000.00 INR" + "\n"
                              + "Up to 321,900.00 x 0.5% =1,609.50 INR" + "\n" + "Total charge amount = " + fxrate
                              + " INR" + "";
                  getPane().setFCCTDETS(fcctval);

            } else {
                  getPane().setFCCTDETS("");
                  getPane().setFXSVTX("");
            }

      } else {

            getPane().setFCCTDETS("");
            getPane().setFXSVTX("");

      }
} else {
      String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
      String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
      String fcct_Cal = getDriverWrapper().getEventFieldAsText("FCYT", "v", "m").trim();

      // Loggers.general().info(LOG,"FCCT Amount initial===> " + fcct_Cal);
      LOG.info("FCCT Calculation " + getMajorCode() + " " + getMinorCode() + " " + getmasRefNo() + " "
                  + geteventRefNo());

      if (!fcct_Cal.equalsIgnoreCase("") && fcct_Cal != null && fcct_Cal.length() > 0) {

            con = getConnection();
            String query = "SELECT ETT_FCCT_CALCULATION('" + fcct_Cal + "') FROM DUAL";

            LOG.info("ETT_FCCT_CALCULATION function " + query);

            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                  String fcct_val = rs1.getString(1);
                  // Loggers.general().info(LOG,"FCCT Amount in string===> " +
                  // fcct_val);
                  System.out.println(fcct_val);
                  BigDecimal finalvalue = new BigDecimal(fcct_val);
                  DecimalFormat diff = new DecimalFormat("0.00");
                  diff.setMaximumFractionDigits(2);
                  System.out.println(diff);
                  String finval = diff.format(finalvalue);
                  System.out.println("finval:" + finval);

                  LOG.info("FCCT Amount in Bigdecimal===> " + finval);

                  getPane().setFXSVTX(finval + " INR");

                  String fxrate = getDriverWrapper().getEventFieldAsText("cAPD", "v", "m");
                  System.out.println(fxrate);
                  // Loggers.general().info(LOG,"fxrate Amount in string===> " +
                  // fxrate);
                  String fcctval = " Calculation details " + "\n"
                              + "1.Upto 100,000 INR Apply 1% (minimum 250 INR) " + "\n" + "2.Upto 10,00,000 INR "
                              + "\n" + "Apply 1000 INR + 0.5% on the value exceeding       100,000 INR " + "\n"
                              + "3.Above 10,00,000 INR " + "\n"
                              + "Apply 5500 INR + 0.1% on the value exceeding 10,00,000 INR (maximum 60,000 INR) "
                              + "\n" + "\n" + "For Example" + "\n"
                              + "INR equivalent amount to calculation FCY conversion tax = " + fcct_Cal + " INR"
                              + "\n" + "Up to 100,000.00 x 1% (min 250 INR) = 1,000.00 INR" + "\n"
                              + "Up to 321,900.00 x 0.5% =1,609.50 INR" + "\n" + "Total charge amount = " + fxrate
                              + " INR" + "";
                  getPane().setFCCTDETS(fcctval);

            } else {
                  getPane().setFCCTDETS("");
                  getPane().setFXSVTX("");

            }

      } else {

            getPane().setFCCTDETS("");
            getPane().setFXSVTX("");

      }

}

} catch (Exception e1) {
// Loggers.general().info(LOG,"Exception ETT_FCCT_CALCULATION blank" +
// e1.getMessage());
getPane().setFCCTDETS("");
getPane().setFXSVTX("");
e1.printStackTrace();
} finally {
try {
      if (rs1 != null)
            rs1.close();
      if (ps1 != null)
            ps1.close();
      if (con != null)
            con.close();
} catch (SQLException e) {
      //// Loggers.general().info(LOG,"Connection Failed! Check
      //// output
      //// console");
      e.printStackTrace();
}
}

}
// cross currency conversion

// public boolean crosscurrency() {
//
// boolean value = false;
// if ((!getMinorCode().equalsIgnoreCase("POC") ||
// !getMinorCode().equalsIgnoreCase("POD"))
// && !getMajorCode().equalsIgnoreCase("IDC")) {
// // if ((!getMajorCode().equalsIgnoreCase("ILC")) &&
// // (!getMinorCode().equalsIgnoreCase("POC"))) {
// String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s",
// "").trim();
// String subproductcode = getDriverWrapper().getEventFieldAsText("PTP",
// "s", "").trim();
// // //Loggers.general().info(LOG,"cross currency conversion and product type
// // ---->"
// // + productcode + "subproductcode--->"+ subproductcode);
// // only for foreign currency
//
// String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
// // //Loggers.general().info(LOG,"Event reference for posting----> " + evnt);
// String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
// // //Loggers.general().info(LOG,"Event count for posting----->" + evvcount);
// String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
// "r", "");
// String camt = getDriverWrapper().getEventFieldAsText("AMT", "v",
// "m").toString().trim();
//
// String strLog = "Log";
// String dailyval_Log = "";
// AdhocQuery<? extends
// com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
// getDriverWrapper()
// .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
// EXTGENCUSTPROP CodeLog = queryLog.getUnique();
// if (CodeLog != null) {
//
// dailyval_Log = CodeLog.getPropval();
// } else {
// // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
//
// }
//
// if (camt.length() > 0) {
// double mastdoub = Double.valueOf(camt);
// long without_con = 0;
// double outamt_dou = 0.0;
//
// if (productcode.equalsIgnoreCase("CPCI") ||
// productcode.equalsIgnoreCase("ODC")
// || productcode.equalsIgnoreCase("ELC") ||
// productcode.equalsIgnoreCase("FSA")
// || productcode.equalsIgnoreCase("ESB") ||
// productcode.equalsIgnoreCase("OCL")
// || productcode.equalsIgnoreCase("FRN") ||
// productcode.equalsIgnoreCase("ICC")
// || productcode.equalsIgnoreCase("ICL") ||
// productcode.equalsIgnoreCase("FEL")
// || productcode.equalsIgnoreCase("FOC"))
//
// {
// // //Loggers.general().info(LOG,"Product type is Expot---->" +
// // productcode);
//
// try {
// // //Loggers.general().info(LOG,"With converstion Expot----> ");
// String query_with = "select TRIM(WITH_CONV),TRIM(WITH_CONV_CCY) from
// ETT_VSELL_FCYTAX_WITHCONV where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"query_with converstion Expot---->" + query_with);
// //
// Export
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_with);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
//
// String conamt = res.getString(1).trim();
// String conccy = res.getString(2).trim();
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out
// .println("With converstion amount and currency Expot----> " +
// res.getString(1));
// }
// getPane().setFXSVTX(conamt + " INR");
// getWrapper().setFXSVTX(conamt + " INR");
// String rtgspart = getWrapper().getRTGSPART();
// // if (rtgspart.equalsIgnoreCase("FULL")) {
// // getPane().setRTGSNEFT(conamt + " INR");
// // getWrapper().setRTGSNEFT(conamt + " INR");
// // }
// // //Loggers.general().info(LOG,"With converstion amount and
// // currency
// // Expot----> " + getWrapper().getFXSVTX());
//
// } else {
// // Loggers.general().info(LOG,"With converstion amount in
// // else");
// getPane().setFXSVTX("");
// getWrapper().setFXSVTX("");
// }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"With converstion Expot----> " +
// // e.getMessage());
// } finally {
// try {
// if (conn != null) {
// conn.close();
// if (pre != null)
// pre.close();
// if (res != null)
// res.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check
// // output console");
// e.printStackTrace();
// }
// }
//
// try {
//
// // //Loggers.general().info(LOG,"Cross currency converstion
// // Expot----->");
// String query_cross = "select TRIM(INR_EQV_AMT),XCCY_CONV_CCY from
// ETT_VSELL_FCYTAX_XCCYCONV where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"query_cross converstion Expot----> " +
// query_cross);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_cross);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
//
// String crossamt = res.getString(1).trim();
// String crosscur = res.getString(2).trim();
// ConnectionMaster connectionMaster = new ConnectionMaster();
// String divideByStr = connectionMaster.getDecimalforCur(crosscur);
// BigDecimal divideByDecimal = new BigDecimal(divideByStr);
// // //LOG.info("Cross currency converstion
// // amount divideByDecimal---> " + divideByDecimal);
// BigDecimal priceDec = new BigDecimal(crossamt);
// BigDecimal priceDecimal = priceDec.divide(divideByDecimal);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Cross currency converstion amount ---> " +
// priceDecimal);
// }
//
// DecimalFormat diff = new DecimalFormat("0.00");
// diff.setMaximumFractionDigits(2);
// String LitersOf = diff.format(priceDecimal);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Cross currency converstion amount ---> " +
// LitersOf);
// }
// getPane().setCCSVTX(LitersOf + " INR");
// getWrapper().setCCSVTX(LitersOf + " INR");
//
// } else {
// // Loggers.general().info(LOG,"Cross converstion amount in
// // else");
// getPane().setCCSVTX("");
// getWrapper().setCCSVTX("");
// }
//
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Cross currency converstion
// // Expot----->" + e.getMessage());
// } finally {
// try {
// if (conn != null) {
// conn.close();
// if (pre != null)
// pre.close();
// if (res != null)
// res.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check
// // output console");
// e.printStackTrace();
// }
// }
//
// try {
//
// String outccy = "";
// double spot_dou = 0.0;
// // //Loggers.general().info(LOG,"Without converstion
// // Import----->");
// String query_without = "select TRIM(WITHOUT_CONV),TRIM(WITHOUT_CONV_CCY)
// from ETT_VSELL_FCYTAX_WOCONV where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"query_without converstion Expot----> " +
// query_without);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_without);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
//
// String outamt = res.getString(1).trim();
//
// outccy = res.getString(2).trim();
// // //Loggers.general().info(LOG,"Without converstion amount
// // and
// // currency Import----> " + res.getString(1)+
// // res.getString(2));
// outamt_dou = Double.valueOf(outamt);
// ConnectionMaster connectionMaster = new ConnectionMaster();
// double divideByDecimal = connectionMaster.getDecimalforCurrency(outccy);
// // invadob_total = outamt_dou / divideByDecimal;
// long without = (long) (mastdoub - (outamt_dou / divideByDecimal));
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Without converstion amount in without long " +
// without);
// }
// without_con = without * 100;
// // //Loggers.general().info(LOG,"Without converstion amount
// // in
// // without_con long after mul " + without_con);
//
// long other_long = 0;
// String other_CCY = "";
// String query_other = "select OTH_CHRG_AMT,OTH_CHRG_AMT_CCY from
// ETT_VBS_FCYTAX_OTHCHRG where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Other bank converstion import----> " +
// query_other);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_other);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
// String other = res.getString(1).trim();
// other_CCY = res.getString(1).trim();
// other_long = Long.parseLong(other);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Other bank value in query---->" + other_long);
// }
// }
// double divideByccy = connectionMaster.getDecimalforCurrency(other_CCY);
// long other_total = (long) (without_con + (other_long / divideByccy));
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Without converstion amount in other_total " +
// other_total);
// }
// String final_val = String.valueOf(other_total);
// // //Loggers.general().info(LOG,"Without converstion amount
// // in
// // without_con " + final_val);
// if (other_total > 0) {
// getPane().setNOCONV(final_val + " " + outccy);
// getWrapper().setNOCONV(final_val + " " + outccy);
// } else {
// getPane().setNOCONV(0 + " USD");
// getWrapper().setNOCONV(0 + " USD");
// }
//
// }
//
// else {
// long other_long = 0;
// String other_curr = "";
// String query_other = "select OTH_CHRG_AMT,OTH_CHRG_AMT_CCY from
// ETT_VBS_FCYTAX_OTHCHRG where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Other bank converstion import----> " +
// query_other);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_other);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
// String other = res.getString(1).trim();
// other_curr = res.getString(2).trim();
// other_long = Long.parseLong(other);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Other bank value in query---->" + other_long);
// }
// }
//
// ConnectionMaster connectionMaster = new ConnectionMaster();
// double divideByDecimal =
// connectionMaster.getDecimalforCurrency(other_curr);
// long other_total = (long) (mastdoub + (other_long / divideByDecimal)) *
// 100;
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Without converstion amount in other_total " +
// other_total);
// }
// String final_val = String.valueOf(other_total);
// // //Loggers.general().info(LOG,"Without converstion amount
// // in
// // without_con " + final_val);
// if (other_long > 0) {
// if (other_total > 0) {
// getPane().setNOCONV(final_val + " " + other_curr);
// getWrapper().setNOCONV(final_val + " " + other_curr);
// } else {
//
// getPane().setNOCONV(0 + " USD");
// getWrapper().setNOCONV(0 + " USD");
//
// }
// } else {
// // Loggers.general().info(LOG,"Without converstion
// // amount in else");
// getPane().setNOCONV("");
// getWrapper().setNOCONV("");
// }
// }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Without converstion Expot-----> "
// // + e.getMessage());
// } finally {
// try {
// if (conn != null) {
// conn.close();
// if (pre != null)
// pre.close();
// if (res != null)
// res.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check
// // output console");
// e.printStackTrace();
// }
// }
//
// }
//
// else
//
// {
// // //Loggers.general().info(LOG,"Product type is Import---->" +
// // productcode);
//
// try {
// // //Loggers.general().info(LOG,"With converstion Import---->
// // ");
// String query_with = "select TRIM(WITH_CONV),TRIM(WITH_CONV_CCY) from
// ETT_VBUY_FCYTAX_WITHCONV where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"query_with converstion Import----> " +
// query_with);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_with);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
//
// String conamt = res.getString(1).trim();
// String conccy = res.getString(2).trim();
// // //LOG.info("With converstion amount
// // and
// // currency
// // Import----> " + res.getString(1) +
// // res.getString(2));
// getPane().setFXSVTX(conamt + " INR");
// getWrapper().setFXSVTX(conamt + " INR");
// // String rtgspart = getWrapper().getRTGSPART();
// // if (rtgspart.equalsIgnoreCase("FULL")) {
// // getPane().setRTGSNEFT(conamt + " INR");
// // getWrapper().setRTGSNEFT(conamt + " INR");
// // }
// // //Loggers.general().info(LOG,"With converstion amount and
// // currency
// // Import----> " + getWrapper().getFXSVTX());
//
// }
//
// else {
// // //Loggers.general().info(LOG,"With converstion amount in
// // else");
// getPane().setFXSVTX("");
// getWrapper().setFXSVTX("");
// }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"With converstion Import----> " +
// // e.getMessage());
// } finally {
// try {
// if (conn != null) {
// conn.close();
// if (pre != null)
// pre.close();
// if (res != null)
// res.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check
// // output console");
// e.printStackTrace();
// }
// }
//
// try {
//
// // //Loggers.general().info(LOG,"Cross currency converstion
// // Import----->");
// String query_cross = "select TRIM(INR_EQV_AMT),XCCY_CONV_CCY from
// ETT_VBUY_FCYTAX_XCCYCONV where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"query_cross converstion Import----> " +
// query_cross);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_cross);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
//
// String crossamt = res.getString(1).trim();
// String crosscur = res.getString(2).trim();
// ConnectionMaster connectionMaster = new ConnectionMaster();
// String divideByStr = connectionMaster.getDecimalforCur(crosscur);
// BigDecimal divideByDecimal = new BigDecimal(divideByStr);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,
// "Cross currency converstion amount divideByDecimal---> " +
// divideByDecimal);
// }
// BigDecimal priceDec = new BigDecimal(crossamt);
// BigDecimal priceDecimal = priceDec.divide(divideByDecimal);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Cross currency converstion amount ---> " +
// priceDecimal);
// }
// DecimalFormat diff = new DecimalFormat("0.00");
// diff.setMaximumFractionDigits(2);
// String LitersOf = diff.format(priceDecimal);
// // //Loggers.general().info(LOG,"Cross currency converstion
// // amount ---> " + LitersOf);
// getPane().setCCSVTX(LitersOf + " INR");
// getWrapper().setCCSVTX(LitersOf + " INR");
//
// } else {
// // //Loggers.general().info(LOG,"With converstion amount in
// // else");
// getPane().setCCSVTX("");
// getWrapper().setCCSVTX("");
// }
//
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Cross currency converstion
// // Import----->" + e.getMessage());
// }
//
// finally {
// try {
// if (conn != null) {
// conn.close();
// if (pre != null)
// pre.close();
// if (res != null)
// res.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check
// // output console");
// e.printStackTrace();
// }
// }
//
// try {
// String outccy = "";
// double spot_dou = 0.0;
// // //Loggers.general().info(LOG,"Without converstion
// // Import----->");
// String query_without = "select TRIM(WITHOUT_CONV),TRIM(WITHOUT_CONV_CCY)
// from ETT_VBUY_FCYTAX_WOCONV where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"query_without converstion import----> " +
// query_without);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_without);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
//
// String outamt = res.getString(1).trim();
//
// outccy = res.getString(2).trim();
// // //Loggers.general().info(LOG,"Without converstion amount
// // and
// // currency Import----> " + res.getString(1)+
// // res.getString(2));
// outamt_dou = Double.valueOf(outamt);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Without converstion amount in outamt_dou" +
// outamt_dou);
// }
// ConnectionMaster connectionMaster = new ConnectionMaster();
// double divideByDecimal = connectionMaster.getDecimalforCurrency(outccy);
// // invadob_total = outamt_dou / divideByDecimal;
// long without = (long) (mastdoub - (outamt_dou / divideByDecimal));
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Without converstion amount in without long " +
// without);
// }
// without_con = without * 100;
// // //Loggers.general().info(LOG,"Without converstion amount
// // in
// // without_con long after mul " + without_con);
//
// long other_long = 0;
// String other_CCY = "";
// String query_other = "select OTH_CHRG_AMT,OTH_CHRG_AMT_CCY from
// ETT_VBS_FCYTAX_OTHCHRG where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Other bank converstion import----> " +
// query_other);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_other);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
// String other = res.getString(1).trim();
// other_CCY = res.getString(1).trim();
// other_long = Long.parseLong(other);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Other bank value in query---->" + other_long);
// }
// }
// double divideByccy = connectionMaster.getDecimalforCurrency(other_CCY);
// long other_total = (long) (without_con + (other_long / divideByccy));
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Without converstion amount in other_total " +
// other_total);
// }
//
// if (other_total > 0) {
// String final_val = String.valueOf(other_total);
// // //Loggers.general().info(LOG,"Without converstion
// // amount in without_con " + final_val);
// getPane().setNOCONV(final_val + " " + outccy);
// getWrapper().setNOCONV(final_val + " " + outccy);
// } else {
// getPane().setNOCONV(0 + " USD");
// getWrapper().setNOCONV(0 + " USD");
// }
//
// }
//
// else {
// long other_long = 0;
// String other_curr = "";
// String query_other = "select OTH_CHRG_AMT,OTH_CHRG_AMT_CCY from
// ETT_VBS_FCYTAX_OTHCHRG where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + evnt + "' AND REFNO_SERL='" +
// evvcount
// + "'";
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Other bank converstion import----> " +
// query_other);
// }
// conn = ConnectionMaster.getConnection();
// pre = conn.prepareStatement(query_other);
// res = pre.executeQuery();
// // //Loggers.general().info(LOG,"result of query "+rs2);
// if (res.next()) {
// String other = res.getString(1).trim();
// other_curr = res.getString(2).trim();
// other_long = Long.parseLong(other);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Other bank value in query---->" + other_long);
// }
// }
// ConnectionMaster connectionMaster = new ConnectionMaster();
// double divideByDecimal =
// connectionMaster.getDecimalforCurrency(other_curr);
// long other_total = (long) (mastdoub + (other_long / divideByDecimal)) *
// 100;
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Without converstion amount in other_total " +
// other_total);
// }
// String final_val = String.valueOf(other_total);
// // //Loggers.general().info(LOG,"Without converstion amount
// // in
// // without_con " + final_val);
// if (other_long > 0) {
// if (other_total > 0) {
// getPane().setNOCONV(final_val + " " + other_curr);
// getWrapper().setNOCONV(final_val + " " + other_curr);
// } else {
// getPane().setNOCONV(0 + " USD");
// getWrapper().setNOCONV(0 + " USD");
// }
// } else {
// // //Loggers.general().info(LOG,"Without converstion
// // amount in else");
// getPane().setNOCONV("");
// getWrapper().setNOCONV("");
// }
// }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Without converstion Import----->
// // " + e.getMessage());
// } finally {
// try {
// if (conn != null) {
// conn.close();
// if (pre != null)
// pre.close();
// if (res != null)
// res.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check
// // output console");
// e.printStackTrace();
// }
// }
//
// }
// } else {
// // Loggers.general().info(LOG,"Master amount is empty");
// }
// }
//
// else {
// // Loggers.general().info(LOG,"Product type is import lc and poc ");
// }
// return value;
//
// }

// USD EQUIVALENT
public boolean currencyCalc() {
boolean value = false;

String strLog = "Log";
String dailyval_Log = "";
@SuppressWarnings("unchecked")
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
      .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
EXTGENCUSTPROP CodeLog = queryLog.getUnique();
if (CodeLog != null) {

dailyval_Log = CodeLog.getPropval();
} else {
// Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

}
String amtval = "";
String camt = "";
try {

amtval = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");

} catch (Exception e) {
amtval = "0";
if (dailyval_Log.equalsIgnoreCase("YES")) {
      LOG.info("Exception in master amount calculation---> " + e.getMessage());
}
}

String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
String newpro = getWrapper().getNEWPRO().trim();

if (amtval.length() > 1
      && (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {
try {
      // double amountval = Double.parseDouble(amtval);
      BigDecimal amtbig = new BigDecimal(amtval);
      // //Loggers.general().info(LOG,"Double value of amount" + amtval +
      // "amtbig" + amtbig);
      camt = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
      con = getConnection();
      String query = "select trim(SPOTRATE) from spotrate where currency='" + camt + "'";

      ps = con.prepareStatement(query);
      rs3 = ps.executeQuery();
      while (rs3.next()) {
            String i = "INR";
            String fxrate = rs3.getString(1);
            BigDecimal fxratbig = new BigDecimal(fxrate);
            BigDecimal dailyval = amtbig.multiply(fxratbig);
            DecimalFormat diff = new DecimalFormat("0.00");
            diff.setMaximumFractionDigits(2);
            String val1 = diff.format(dailyval);

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  LOG.info("INR amount in Big decimal final " + val1);
            }
            getPane().setINRAMT(val1 + " " + i);

            try {

                  String fxDeal = getDriverWrapper().getEventFieldAsText("FXD", "l", "");
                  String rtgspart = getWrapper().getRTGSPART();

                  if (rtgspart.equalsIgnoreCase("FULL")
                              && (!stepID.equalsIgnoreCase("Final print")
                                          && !eventStatus.equalsIgnoreCase("Completed"))
                              && fxDeal.equalsIgnoreCase("N")) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("RTGS/NEFT Deal not taken" + fxDeal);
                        }
                        getPane().setRTGSNEFT(val1 + " INR");

                  } else if (rtgspart.equalsIgnoreCase("FULL")
                              && (!stepID.equalsIgnoreCase("Final print")
                                          && !eventStatus.equalsIgnoreCase("Completed"))
                              && fxDeal.equalsIgnoreCase("Y")) {
                        String fcct_val = getDriverWrapper().getEventFieldAsText("FCYT", "v", "m");
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,
                                          "RTGS/NEFT Deal not taken and fcct value" + fxDeal + fcct_val);
                        }
                        BigDecimal finalvalue = new BigDecimal(fcct_val);
                        diff.setMaximumFractionDigits(2);
                        String finval = diff.format(finalvalue);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              LOG.info("RTGS/NEFT Deal taken amount" + finval);
                        }
                        getPane().setRTGSNEFT(finval + " INR");

                  }

            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,
                                    "Exception in RTGS/NEFT and INR amount calculation" + e.getMessage());
                  }
            }

            try {
                  // String query1 = "select trim(SPOTRATE) from spotrate where currency='USD'";
                  // String query1 = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND
                  // CURREN49='USD'";
                  // con = getConnection();
                  // ps = con.prepareStatement(query1);
                  // rs = ps.executeQuery();

                  if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IGT")
                              || getMajorCode().equalsIgnoreCase("IDC") || getMajorCode().equalsIgnoreCase("SHG")
                              || getMajorCode().equalsIgnoreCase("ELC") || getMajorCode().equalsIgnoreCase("ODC")
                              || getMajorCode().equalsIgnoreCase("EGT") || getMajorCode().equalsIgnoreCase("ICC")
                              || getMajorCode().equalsIgnoreCase("OCC") || getMajorCode().equalsIgnoreCase("CPCO")
                              || getMajorCode().equalsIgnoreCase("CPCI") || getMajorCode().equalsIgnoreCase("CPBO")
                              || getMajorCode().equalsIgnoreCase("CPBI") || getMajorCode().equalsIgnoreCase("FEL")
                              || getMajorCode().equalsIgnoreCase("FOC") || getMajorCode().equalsIgnoreCase("FSA"))
                              && (getMinorCode().equalsIgnoreCase("ISI") || getMinorCode().equalsIgnoreCase("CRE")
                                          || getMinorCode().equalsIgnoreCase("IIG")
                                          || getMinorCode().equalsIgnoreCase("VEG")
                                          || getMinorCode().equalsIgnoreCase("ADE")
                                          || getMinorCode().equalsIgnoreCase("CSA4")
                                          || getMinorCode().equalsIgnoreCase("CSA")
                                          || getMinorCode().equalsIgnoreCase("CSA1")
                                          || getMinorCode().equalsIgnoreCase("SCR")
                                          || getMinorCode().equalsIgnoreCase("PBIC")
                                          || getMinorCode().equalsIgnoreCase("PBOC")
                                          || getMinorCode().equalsIgnoreCase("PCIC")
                                          || getMinorCode().equalsIgnoreCase("PCOC"))) {

                        System.out.println("In Usd calculation");
                        // con = getConnection();
                        // masCurr = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
                        // String masRef = getDriverWrapper().getEventFieldAsText("MST", "r",
                        // "").trim();
                        String query1 = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='USD'";

                        if ((getMajorCode().equalsIgnoreCase("ELC") || getMajorCode().equalsIgnoreCase("ODC")
                                    || getMajorCode().equalsIgnoreCase("EGT")
                                    || getMajorCode().equalsIgnoreCase("OCC"))) {
                              query1 = "SELECT BUYEXC03 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='USD'";
                        }
                        System.out.println("USD  rate " + query1);
                        con = getConnection();
                        ps = con.prepareStatement(query1);
                        rs3 = ps.executeQuery();
                        while (rs3.next()) {

                              String u = "USD";
                              String usdrate = rs3.getString(1);
                              BigDecimal usdrat = new BigDecimal(usdrate);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("the value of USD in Bigdecimal spotrate" + usdrat);
                              }
                              String usdrat1 = String.valueOf(usdrate);
                              getPane().setCRAMT_Name(usdrat1);

                              BigDecimal total = dailyval.divide(usdrat, RoundingMode.HALF_UP);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("The USD Equivalent is Bigdecimal" + total);
                              }
                              diff.setMaximumFractionDigits(2);
                              String total2 = diff.format(total);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("The USD Equivalent is finally Bigdecimal" + total2);
                              }
                              getPane().setUSDAMT(total2 + " " + u);
                              if (getMajorCode().equalsIgnoreCase("CPCO") && newpro.length() > 0) {
                                    getPane().setTOTUSD(total2 + " " + u);
                              } else if (getMajorCode().equalsIgnoreCase("CPCO")
                                          && (newpro.length() == 0 || newpro.equalsIgnoreCase(""))) {
                                    getPane().setTOTUSD("");
                              }

                        }

                  }
            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        LOG.info("Exception in USD amount calculation---> " + e.getMessage());
                  }
            }
      }

} catch (Exception e) {

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Exception in currency calculation---> " + e.getMessage());
            e.printStackTrace();
      }
} finally {
      try {
            if (rs3 != null)
                  rs3.close();
            if (ps != null)
                  ps.close();
            if (con != null)
                  con.close();
      } catch (SQLException e) {
            // Loggers.general().info(LOG,"Connection Failed! Check output
            // console");
            e.printStackTrace();
      }
}
} else {
// Loggers.general().info(LOG,"Amount value is empty" + amtval);
}

try {

String currency = "USD";
try {

      currency = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");

} catch (Exception e) {
      currency = "USD";
      LOG.info("Exception in equvalent currency blank ===> " + e.getMessage());

}

String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");// ILF

if (getMajorCode().equalsIgnoreCase("FSA") && currency.equalsIgnoreCase("INR")
            && (subproductCode.equalsIgnoreCase("CBD") || subproductCode.equalsIgnoreCase("IVD")
                        || subproductCode.equalsIgnoreCase("FAC") || subproductCode.equalsIgnoreCase("RVF")
                        || subproductCode.equalsIgnoreCase("TDF")))

{
      getPane().setUSDAMT("");
      getPane().setCRAMT_Name("");
      getPane().setINRAMT("");

}
} catch (Exception e) {
LOG.info("Exception in equvalent amount not populate in INR===> " + e.getMessage());
}

// // Own lc validation 261116
// String mast_ref = getDriverWrapper().getEventFieldAsText("MST", "r",
// "");
// try {
// String own_Value = "SELECT mas.MASTER_REF, THEME_GENIUS_PKG.CONVAMT(
// mas.CCY,mas.AMOUNT) master_amount , mas.CCY master_ccy,
// THEME_GENIUS_PKG.CONVAMT(cpm.pay_ccy,cpm.PAY_AMT) pay_amt, PAY_CCY
// pay_ccy, round(THEME_GENIUS_PKG.CONVAMT(cpm.pay_ccy,cpm.PAY_AMT)*
// cer1.EXCH_RATE/cer2.EXCH_RATE,2) AS pay_usd_amt FROM master
// mas,CPAYMASTER cpm, PARTYDTLS prt, CCY_EXCH_RATE_VIEW cer1,
// CCY_EXCH_RATE_VIEW cer2, gfpf WHERE mas.key97 = cpm.KEY97 AND
// cpm.REMIT_PTY = prt.KEY97 AND prt.CUS_MNM = gfpf.gfcus1 AND
// cpm.PAY_CCY = cer1.CURR_CODE and cer2.CURR_CODE = 'USD' AND
// mas.MASTER_REF='"
// + mast_ref + "'";
// //Loggers.general().info(LOG,"OWN LC VALUE SHOW USD--->" + own_Value);
// con = ConnectionMaster.getConnection();
// ps1 = con.prepareStatement(own_Value);
// rs = ps1.executeQuery();
// while (rs.next()) {
// String USD_amt = rs1.getString(6);
// //Loggers.general().info(LOG,"USD_amt value after getting in query---->" +
// USD_amt);
// getPane().setUSDAMT(USD_amt + " " + "USD");
// }
// con.close();
// ps1.close();
// rs.close();
// } catch (Exception e) {
// //Loggers.general().info(LOG,"OWN LC VALUE SHOW " + e);
// }

return value;
}

// public boolean onFETCHLOANELCDP() {
// boolean value = false;
// // public void onFETCHLOANELCDPclayButton() {
//
// String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
// "r", "");
// String eventPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s",
// "");
// String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
// String productType = getDriverWrapper().getEventFieldAsText("FPP:XFPTP",
// "s", ""); // FCA
// String crystal = getDriverWrapper().getEventFieldAsText("cBOJ", "l", "");
// String billAmt = getDriverWrapper().getEventFieldAsText("cBOH", "v",
// "m");
// // Loggers.general().info(LOG,"Discounted Bill Amount----> " + billAmt);
//
// if (productType.equalsIgnoreCase("FCA") && crystal.equalsIgnoreCase("N")
// && getMinorCode().equalsIgnoreCase("DOP")) {
// try {
// // //Loggers.general().info(LOG,"With converstion Expot----> ");
// String query_with = "select TRIM(WITH_CONV),TRIM(WITH_CONV_CCY) from
// ETT_VSELL_FCYTAX_WITHCONV where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + eventPrefix + "' AND
// REFNO_SERL='" + evvcount + "'";
//
// con = ConnectionMaster.getConnection();
// ps1 = con.prepareStatement(query_with);
// rs1 = ps1.executeQuery();
// // Loggers.general().info(LOG,"Discounted Bill Amount query " +
// // query_with);
// if (rs1.next()) {
//
// String conamt = rs1.getString(1).trim();
// String conccy = rs1.getString(2).trim();
// Loggers.general().info(LOG,"With converstion amount and currency Expot----> "
// +
// rs1.getString(1));
// getPane().setBILRELAM(conamt + " INR");
//
// }
// // else
// // {
// // setBILRELAM("");
// // }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"With converstion Expot----> " +
// // e.getMessage());
// } finally {
// try {
// if (con != null) {
// con.close();
// if (ps1 != null)
// ps1.close();
// if (rs1 != null)
// rs1.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check output
// // console");
// e.printStackTrace();
// }
// }
// } else if ((productType.equalsIgnoreCase("INA")) &&
// crystal.equalsIgnoreCase("N")
// && getMinorCode().equalsIgnoreCase("DOP")) {
// String dealAmt = getDriverWrapper().getEventFieldAsText("FPP:XB+DA", "v",
// "m");
// // Loggers.general().info(LOG,"Finance deal amount =====>" + dealAmt);
// getPane().setBILRELAM(dealAmt + " INR");
//
// }
//
// // else if (crystal.equalsIgnoreCase("Y")) {
// // String cryloanAmt = getDriverWrapper().getEventFieldAsText("cBOL",
// // "v", "m");
// // Loggers.general().info(LOG,"Crystallization Amount----> " + cryloanAmt);
// // String billAmount = getDriverWrapper().getEventFieldAsText("cBOH",
// // "v", "m");
// // Loggers.general().info(LOG,"Discounted Bill Amount =====>" + billAmount);
// // BigDecimal dealAmt_Big = new BigDecimal(cryloanAmt);
// // BigDecimal billAmount_Big = new BigDecimal(billAmount);
// // BigDecimal totalAmt = dealAmt_Big.subtract(billAmount_Big);
// // Loggers.general().info(LOG,"Total deal amount =====>" + totalAmt);
// // String finalvalue = String.format("%.2f", totalAmt);
// // Loggers.general().info(LOG,"Final deal amount =====>" + finalvalue);
// // setPRFT(finalvalue + " INR");
// // } else if (!productType.equalsIgnoreCase("FCA") ||
// // !productType.equalsIgnoreCase("INA")) {
// // setBILRELAM("");
// // setPRFT("");
// // setCYRLOAN(false);
// // }
//
// // }
// return value;
//
// }

// public boolean onFETCHLOANELCSETT() {
// boolean value = false;
//
// // public void onFETCHLOANELCSETTclayButton() {
//
// String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
// "r", "");
// String eventPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s",
// "");
// String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
// String productType = getDriverWrapper().getEventFieldAsText("FPP:XFPTP",
// "s", ""); // FCA
// // Loggers.general().info(LOG,"productType----> " + productType);
// String crystal = getDriverWrapper().getEventFieldAsText("cBOJ", "l", "");
// String billAmt = getDriverWrapper().getEventFieldAsText("cBOH", "v",
// "m");
// // Loggers.general().info(LOG,"Discounted Bill Amount----> " + billAmt);
//
// if (productType.equalsIgnoreCase("FCA") && crystal.equalsIgnoreCase("N")
// && getMinorCode().equalsIgnoreCase("POD")) {
// try {
// Loggers.general().info(LOG,"Discounted Bill Amount query call if loop");
// String query_with = "select TRIM(WITH_CONV),TRIM(WITH_CONV_CCY) from
// ETT_VSELL_FCYTAX_WITHCONV where MASTER_REF = '"
// + masterRefNumber + "' AND REFNO_PFIX ='" + eventPrefix + "' AND
// REFNO_SERL='" + evvcount + "'";
//
// con = ConnectionMaster.getConnection();
// ps1 = con.prepareStatement(query_with);
// rs1 = ps1.executeQuery();
// Loggers.general().info(LOG,"Discounted Bill Amount query " + query_with);
// if (rs1.next()) {
//
// String conamt = rs1.getString(1).trim();
// String conccy = rs1.getString(2).trim();
// Loggers.general().info(LOG,"With converstion amount and currency Expot----> "
// +
// rs1.getString(1));
// getPane().setBILRELAM(conamt + " INR");
//
// }
// // else
// // {
// // setBILRELAM("");
// // }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"With converstion Expot----> " +
// // e.getMessage());
// } finally {
// try {
// if (con != null) {
// con.close();
// if (ps1 != null)
// ps1.close();
// if (rs1 != null)
// rs1.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check output
// // console");
// e.printStackTrace();
// }
// }
// } else if ((productType.equalsIgnoreCase("INA")) &&
// crystal.equalsIgnoreCase("N")
// && getMinorCode().equalsIgnoreCase("POD")) {
// // Loggers.general().info(LOG,"productType INA----> " + productType);
// String dealAmt = getDriverWrapper().getEventFieldAsText("FPP:XB+DA", "v",
// "m");
// // Loggers.general().info(LOG,"Finance deal amount =====>" + dealAmt);
// getPane().setBILRELAM(dealAmt + " INR");
//
// }
//
// // else if (crystal.equalsIgnoreCase("Y")) {
// // Loggers.general().info(LOG,"productType INA----> " + productType);
// // String cryloanAmt = getDriverWrapper().getEventFieldAsText("cBOL",
// // "v", "m");
// // Loggers.general().info(LOG,"Crystallization Amount----> " + cryloanAmt);
// //
// // String billAmount = getDriverWrapper().getEventFieldAsText("cBOH",
// // "v", "m");
// // Loggers.general().info(LOG,"Discounted Bill Amount =====>" + billAmount);
// // BigDecimal dealAmt_Big = new BigDecimal(cryloanAmt);
// // BigDecimal billAmount_Big = new BigDecimal(billAmount);
// // BigDecimal totalAmt = dealAmt_Big.subtract(billAmount_Big);
// // Loggers.general().info(LOG,"Total deal amount =====>" + totalAmt);
// // String finalvalue = String.format("%.2f", totalAmt);
// // Loggers.general().info(LOG,"Final deal amount =====>" + finalvalue);
// // setPRFT(finalvalue + " INR");
// // } else if (!productType.equalsIgnoreCase("FCA") ||
// // !productType.equalsIgnoreCase("INA")) {
// // Loggers.general().info(LOG,"productType clear----> " + productType);
// // setBILRELAM("");
// // setPRFT("");
// // setCYRLOAN(false);
// // } else {
// // Loggers.general().info(LOG,"Else part----> " + productType + "billAmt===>"
// +
// // billAmt);
// // }
//
// // }
// return value;
//
// }

// URR no generated

public boolean getutrNoGenerated() {

boolean value = false;

String paymentType = getWrapper().getPROREMT();
String utrNum = getWrapper().getUTRNO();
String strLog = "Log";
String dailyval_Log = "";
@SuppressWarnings("unchecked")
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
      .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
EXTGENCUSTPROP CodeLog = queryLog.getUnique();
if (CodeLog != null) {

dailyval_Log = CodeLog.getPropval();
} else {
LOG.info("ADDDailyTxnLimit is empty-------->");

}
LOG.info("UTR Number length" + utrNum.length() + "paymentType" + paymentType);
String step_Id = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
if (paymentType.equalsIgnoreCase("RTG") && utrNum.length() != 22
            && (!step_Id.equalsIgnoreCase("CBS Authoriser") || !step_Id.equalsIgnoreCase("Authorise"))
            && rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N")) {
      String Parameter2 = "5";
      String paymentRTNEFT = "";
      if (paymentType.equalsIgnoreCase("RTG")) {
            paymentRTNEFT = "RTGS";

      }

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("RTGS/NEFT for UTR number generation--->" + paymentRTNEFT);
      }
      ConnectionMaster connectionMaster = new ConnectionMaster();
      if (null != connectionMaster) {
            LOG.info("Entered into utr rtgs============>");
            String urtNumber = connectionMaster.getUtrNoFromPaymentHubDb(paymentRTNEFT, Parameter2);

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  LOG.info("UTR Number for RTGS--->" + urtNumber);
            }

            getPane().setUTRNO(urtNumber);
      }
} else if (paymentType.equalsIgnoreCase("NEF") && utrNum.length() != 16
            && (!step_Id.equalsIgnoreCase("CBS Authoriser") || !step_Id.equalsIgnoreCase("Authorise"))
            && rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N")) {

      String Parameter2 = "5";
      String paymentRTNEFT = "";
      if (paymentType.equalsIgnoreCase("NEF")) {
            paymentRTNEFT = "NEFT";
      }
      // Loggers.general().info(LOG,"RTGS/NEFT for UTR number generation--->"
      // +
      // paymentRTNEFT);
      ConnectionMaster connectionMaster = new ConnectionMaster();
      if (null != connectionMaster) {
            LOG.info("Entered into utr neft============>");
            String urtNumber = connectionMaster.getUtrNoFromPaymentHubDb(paymentRTNEFT, Parameter2);

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  LOG.info("UTR Number for NEFT--->" + urtNumber);
            }

            getPane().setUTRNO(urtNumber);
      }
} else if (rtgsFlag.equalsIgnoreCase("N") && !rtgsFlag.equalsIgnoreCase("Y")) {
      getPane().setUTRNO("");

} else {

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("UTR Number for RTGS/NEFT in else--->" + utrNum);
      }
}
}

return value;

}

public static String getUtrNoFromPaymentHubDb(String paymentType, String channelId) {

LOG.info("UTR number in connection=====>");
String utrNumber = "";
Connection con = null;
CallableStatement callableStmt = null;
int channelIdNumber = Integer.parseInt(channelId);
try {
// getting connection
con = getNeftRtgsConnection();

LOG.info("before execute the procedure=====>");

// Call Oracle Database Function
callableStmt = con.prepareCall("{ ? = call Ep.F_Gen_Neft_Rtgs_Utr_No(?, ?)}");
// Database function will return OUT parameter as String
callableStmt.registerOutParameter(1, java.sql.Types.VARCHAR);

// Database function IN parameter as 'VARCHAR' and 'NUMBER' to
// values of Stored procedure
callableStmt.setString(2, paymentType);
callableStmt.setInt(3, channelIdNumber);
LOG.info("before execute the procedure=====>");
// Execute database Function
callableStmt.executeUpdate();

// Then retrieve values returned by method using using get methods.
utrNumber = callableStmt.getString(1);

LOG.info("UTR Number from procedure--->" + utrNumber);

} catch (SQLException sqlexc) {

sqlexc.printStackTrace();
LOG.info("Exception in UTR -------------> " + sqlexc.getMessage());

} finally {
try {

      if (con != null)
            con.close(); // close connection
      if (callableStmt != null)
            callableStmt.close(); // close CallableStatement

} catch (SQLException e) {
      e.printStackTrace();
}
}

return utrNumber;
}

// -----------------------------* FORCE DEBIT START
// *-------------------------------------------
public String getFORCEDEBITFIN() {
String returns = "";
String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();

try {

con = getConnection();
String dms = "SELECT EXT1.FORCDEBT from MASTER MAS, BASEEVENT BEV, BASEEVENT BEV1, EXTEVENT EXT1 "
            + " WHERE MAS.KEY97 = BEV.MASTER_KEY AND BEV.KEY97 = BEV1.ATTACHD_EV(+) "
            + " AND BEV1.KEY97 = EXT1.EVENT(+) AND trim(MAS.MASTER_REF) ='" + masRefNo + "'"
            + " AND trim(BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))='" + eventRef + "'";
dmsp = con.prepareStatement(dms);
LOG.info("Forcelimit  Query " + dms);
dmsr = dmsp.executeQuery();
while (dmsr.next()) {
      returns = dmsr.getString(1).trim();

}

} catch (Exception e) {
LOG.info("Exception FORCE LIMIT finance " + e.getMessage());

} finally {
try {

      if (dmsr != null)
            dmsr.close();
      if (dmsp != null)
            dmsp.close();
      if (con != null)
            con.close();
} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}
LOG.info("Force limit finance " + returns);
return returns;
}

public String getFORCEDEBIT() {
String returns = "";
String masRefNo = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
String eventRef = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

try {

con = getConnection();
String dms = "select EXT.forcdebt from master mas,baseevent bev,extevent ext WHERE MAS.KEY97 = BEV.MASTER_KEY "
            + " and EXT.EVENT = BEV.KEY97 and trim(mas.master_ref)='" + masRefNo + "'"
            + " AND trim(BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))='" + eventRef + "'";
dmsp = con.prepareStatement(dms);
LOG.info("Forcelimit  Query " + dms);
dmsr = dmsp.executeQuery();
while (dmsr.next()) {
      returns = dmsr.getString(1).trim();

}

} catch (Exception e) {
LOG.info("Exception FORCE LIMIT " + e.getMessage());

} finally {
try {

      if (dmsr != null)
            dmsr.close();
      if (dmsp != null)
            dmsp.close();
      if (con != null)
            con.close();
} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}
LOG.info("Force limit------->  " + returns);
return returns;
}

// Changes for CR 215 starts here

public static int getAccountStatusFromStatic(String accountFromTI) {
int count = 0;
Connection con = null;
PreparedStatement ps1 = null;
ResultSet rs1 = null;
System.out.println("Inside getAccountStatusFromStatic");
try {
con = ConnectionMaster.getConnection();
String query = "SELECT COUNT(*) FROM EXTGLACCOUNTS EXE where BLOCK = 'YES' and TRIM(GLACCNO) = '"
            + accountFromTI + "' ";

System.out.println("Query Result for getAccountStatusFromStatic Validation----------> " + query);

ps1 = con.prepareStatement(query);

rs1 = ps1.executeQuery();
while (rs1.next()) {
      count = rs1.getInt(1);
      System.out.println("Count from getAccountStatusFromStatic--->" + count);
}
} catch (Exception e) {
System.out.println("Exception in getAccountStatusFromStatic" + e.getMessage());
e.printStackTrace();
} finally {
surrenderDB(con, ps1, rs1);
}
return count;
}

public int getAccountFromTI() {

ResultSet res = null;
Connection con = null;
PreparedStatement pst = null;
String emailAlertStatus = "";
int getAccountStatusFromStatic = 0;

try {
String mainMasterRef = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String mainEvntRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
con = ConnectionMaster.getConnection();
String query3 = "SELECT SUBSTR(trim(ACCOUNT_NO), -8) FROM KMB_GL_BLOCK_CUST_VIEW" + " WHERE MASTER_REF ='"
            + mainMasterRef.trim() + "' " + " AND EVENT_REF ='" + mainEvntRef + "' ";

pst = con.prepareStatement(query3);
res = pst.executeQuery();
System.out.println("getEmailAlertStatusQuery--->" + query3);
while (res.next()) {
      String accountFromTi = getEmptyIfNull(res.getString(1));

      getAccountStatusFromStatic = getAccountStatusFromStatic(accountFromTi);
      System.out
                  .println("getAccountStatusFromStatic--->" + getAccountStatusFromStatic + "--" + accountFromTi);
}

} catch (Exception e) {
System.out.println("Exception in getAccountFromTI method---> " + e.getMessage());
} finally {
surrenderDB(con, pst, res);
}
return getAccountStatusFromStatic;
}

// Changes for CR 215 Ends here

// -----------------------------* FORCE DEBIT END
// *-------------------------------------------

// public boolean totalLiability() {
// boolean value = false;
// long fildou = 0;
// String fullcur = "";
// if (getMajorCode().equalsIgnoreCase("ILC")) {
// String fullamt = "0";
// fullamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
// if (fullamt.isEmpty() || fullamt == null) {
// fullamt = "0";
// }
// float fullamtfol = Float.valueOf(fullamt);
// // //Loggers.general().info(LOG,"Initial amount" + fullamtfol);
// fildou = (long) fullamtfol;
// // double fildoudd = Double.valueOf(fildou);
// // //Loggers.general().info(LOG,"Full amount in long" + fildou);
// fullcur = getDriverWrapper().getEventFieldAsText("FOA", "v", "c");
// // //Loggers.general().info(LOG,"Full amount currency " + fullcur);
// String tendays = getDriverWrapper().getEventFieldAsText("TNRD", "i", "");
// // //Loggers.general().info(LOG,"tendays value check---->" + tendays);
// if (tendays.isEmpty() || tendays.equalsIgnoreCase("")) {
//
// tendays = "0";
// // //Loggers.general().info(LOG,"tendays in if --->" + tendays);
// }
// // //Loggers.general().info(LOG,"tendays--->" + tendays);
// double tenddou = 0.0;
// try {
//
// tenddou = Double.valueOf(tendays);
// // //Loggers.general().info(LOG,"tendays in try--->" + tenddou);
// } catch (Exception e) {
// // Loggers.general().info(LOG,"tendays in catch" + e.getMessage());
// }
//
// // //Loggers.general().info(LOG,"Tenor days" + tenddou);
// String inper = getWrapper().getINTPERCE();
// // //Loggers.general().info(LOG,"Interest percentage ----->" + inper);
// if (inper != null && inper.length() > 0) {
// // //Loggers.general().info(LOG,"Interest percentage----->" + inper);
// // inper = "0";
//
// double inperdou = Double.valueOf(inper);
// double totalamt = 0.00;
// try {
// totalamt = (fildou * tenddou * inperdou) / 36500;
// // Loggers.general().info(LOG,"Total amount double" + totalamt);
// // long fildoulon = (long) totalamt;
// ConnectionMaster connectionMaster = new ConnectionMaster();
// String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);
// // Loggers.general().info(LOG,"Currency return fom method=====>" +
// // divideByDecimal);
// if (divideByDecimal.equalsIgnoreCase("1")) {
// // //Loggers.general().info(LOG,"Currency return fom method is
// // one1111111111--------> ");
// BigDecimal k = new BigDecimal(totalamt);
//
// // long k = (long) (totalamt);
// // Loggers.general().info(LOG,"interest amount in BigDecimal if
// // loop-------->"+k);
// // ");
// String finalval = String.valueOf(k);
// getPane().setINTAMT(k + " " + fullcur);
// String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
// // //Loggers.general().info(LOG,"customised Interest amount
// // ----->" +
// // inamt);
// double inamtlandou = Double.valueOf(inamt);
// double libamot = fildou + inamtlandou;
// // Loggers.general().info(LOG,"Total Liability amount in double"
// // + libamot);
// // long tolibal = (long) (libamot);
// BigDecimal tolibal = new BigDecimal(libamot);
// // Loggers.general().info(LOG,"Total Liability amount in
// // BigDecimal" + tolibal);
// String finalli = String.valueOf(tolibal);
// // Loggers.general().info(LOG,"Total Liability amount in String"
// // + finalli);
//
// DecimalFormat diff = new DecimalFormat("0.00");
// diff.setMaximumFractionDigits(2);
// String finallib = diff.format(finalli);
//
// // String finallib = String.format("%.2f", finalli);
// getPane().setTOTLOAM(finallib + " " + fullcur);
// } else
//
// {
// BigDecimal k = new BigDecimal(totalamt);
// // Loggers.general().info(LOG,"interest amount in BigDecimal
// // else-------->"+k);
// String finalval = String.valueOf(k);
// // Loggers.general().info(LOG,"interest amount in finalval
// // else-------->"+finalval);
// getPane().setINTAMT(finalval + " " + fullcur);
// String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
// double inamtlandou = Double.valueOf(inamt);
// // //Loggers.general().info(LOG,"Total interest amount" +
// // inamtlandou);
// double libamot = fildou + inamtlandou;
// // Loggers.general().info(LOG,"Total Liability amount double in
// // else" + libamot);
// BigDecimal tolibal = new BigDecimal(libamot);
// // long tolibal = (long) (libamot);
// // Loggers.general().info(LOG,"Total Liability amount in
// // BigDecimal else" + tolibal);
//
// String finalli = String.valueOf(tolibal);
// // Loggers.general().info(LOG,"Total Liability amount in string
// // else" + finalli);
// DecimalFormat diff = new DecimalFormat("0.00");
// diff.setMaximumFractionDigits(2);
// String finallib = diff.format(finalli);
//
// // String finallib = String.format("%.2f", finalli);
//
// getPane().setTOTLOAM(finallib + " " + fullcur);
// }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Exception of Total Liability amount"
// // +e.getMessage());
// }
//
// } else {
// // //Loggers.general().info(LOG,"Interest percentage empty----->" +
// // inper);
// getPane().setTOTLOAM("");
// getPane().setINTAMT("");
// }
// } else {
// // Loggers.general().info(LOG,"getMajorCode() ILC------->" +
// // getMajorCode());
//
// }
// return value;
// }

// public boolean getrealizationamount(ValidationDetails validationDetails)
// {
// String presentcur = getDriverWrapper().getEventFieldAsText("AMPR", "v",
// "c").toString();
// // //Loggers.general().info(LOG,"the presentation amount currency" +
// presentcur
// // );
// String presentamt = getDriverWrapper().getEventFieldAsText("AMPR", "v",
// "i").toString();
// // //Loggers.general().info(LOG,"the presentation amount " + presentamt );
// String repayamt = getWrapper().getREPAYAMT();
// // //Loggers.general().info(LOG," repayment Amount" + repayamt);
// String repaycur = getWrapper().getREPAYAMTCurrency();
// // //Loggers.general().info(LOG,"repayment amount currency " + repaycur);
// String transamt = getWrapper().getTRANAMT();
// // //Loggers.general().info(LOG,"transaction Amount" + transamt);
// String transcur = getWrapper().getTRANAMTCurrency();
// // //Loggers.general().info(LOG,"transaction amount currency " + transcur);
//
// if (!presentamt.equalsIgnoreCase("") || !presentamt.isEmpty()) {
// if (presentcur.equalsIgnoreCase(repaycur) &&
// presentcur.equalsIgnoreCase(transcur)) {
// try {
// double repayamt1 = Double.parseDouble(repayamt);
// // //Loggers.general().info(LOG,"double value of repayment amount "
// // +
// // repayamt1);
// double transamt1 = Double.parseDouble(transamt);
// // //Loggers.general().info(LOG," double value of transaction amout"
// // +
// // transamt1);
//
// if (repayamt.equalsIgnoreCase("") || transamt.equalsIgnoreCase("")) {
// repayamt1 = 0.00;
// transamt1 = 0.00;
//
// }
// double total = repayamt1 + repayamt1;
// // //Loggers.general().info(LOG,"the total of 3 values" + total);
// double presentamt1 = Double.parseDouble(presentamt);
// // //Loggers.general().info(LOG,"double value of presentation
// // amount"
// // + presentamt1);
// double realamt = presentamt1 - total;
// // //Loggers.general().info(LOG,"realization Amount" + realamt);
// String real = String.format("%.2f", realamt);
// getWrapper().setREALAMT(real);
// // //Loggers.general().info(LOG,"final value" +real );
// } catch (Exception msg) {
// // Loggers.general().info(LOG,"exception in realization amount
// // validation" + msg.getMessage());
// }
// } else {
// // Loggers.general().info(LOG,"the presentation currency and given 3
// // currencies should be same ");
//
// }
// } else {
// // Loggers.general().info(LOG,"presentation amount is empty");
// }
// return true;
// }

// public boolean Inwardreference(ValidationDetails validationDetails) {
//
// String presentamt = getDriverWrapper().getEventFieldAsText("AMPR", "v",
// "C");
// // //Loggers.general().info(LOG,"The presentation Amount value" +
// presentamt);
// double presentamt1 = Double.parseDouble(presentamt);
// // Charge basis ended population
// String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s",
// "");
// // //Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
// String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s",
// "");
// // //Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);
//
// String inwnum = getWrapper().getINONOS();
// // //Loggers.general().info(LOG,"the customized inward remittance number in
// // outward remittance layout" + inwnum );
// double iamount = 0.00;
// double oamount = 0.00;
// double oamount1 = 0.00;
// double total = 0.00;
//
// if (!inwnum.equalsIgnoreCase("") || !inwnum.isEmpty()) {
// try {
// con = getConnection();
// String query = "select A.MASTER_REF,b.rcv_amt,b.rcv_ccy from MASTER
// A,CPAYMASTER B WHERE A.KEY97=B.KEY97 AND REFNO_PFIX='CPC' and
// A.MASTER_REF='"
// + getmasRefNo() + "'";
// //Loggers.general().info(LOG,"query " + query);
// ps = con.prepareStatement(query);
// rs = ps.executeQuery();
//
// while (rs.next()) {
// // //Loggers.general().info(LOG,"Entered 1st while");
// iamount = rs.getDouble(1);
// ////Loggers.general().info(LOG,"the value of inward transaction amount
// transaction
// amount" + iamount);
//
// }
//
// } catch (Exception ee) {
// //Loggers.general().info(LOG,"catch in inward reference" + ee.getMessage());
// }
//
// finally {
// try {
// if (con != null) {
// con.close();
// if (ps != null)
// ps.close();
// if (rs != null)
// rs.close();
// }
// } catch (SQLException e) {
// //Loggers.general().info(LOG,"Connection Failed! Check output console");
// e.printStackTrace();
// }
// }
//// try {
//// con = getConnection();
//// String query1 = "select A.MASTER_REF,b.pay_amt,b.pay_ccy from MASTER
// A,CPAYMASTER B WHERE A.KEY97=B.KEY97 AND REFNO_PFIX='CPC' and
// A.MASTER_REF='"
//// + getmasRefNo() + "'";
//// ////Loggers.general().info(LOG,"the query value" + query1);
//// ps1 = con.prepareStatement(query1);
//// rs1 = ps1.executeQuery();
//// while (rs1.next()) {
//// // //Loggers.general().info(LOG,"Entered 2nd while");
//// oamount += rs1.getDouble(1);
//// oamount1 = oamount1 + oamount;
//// // //Loggers.general().info(LOG,"the value of outward transaction
//// // amount transaction amount" + oamount);
//// }
////
//// total = oamount1 + presentamt1;
////
//// if ((total > iamount) && (step_Input.equalsIgnoreCase("i")) &&
// (!step_csm.equalsIgnoreCase("CSM"))) {
//// // //Loggers.general().info(LOG,"if condition for inward amount
//// // greater than outward amount");
//// validationDetails.addWarning(WarningType.Other,
//// "outward Remittance transaction amount is greater than inward
// remittance transaction [CM]");
////
//// }
//// } catch (Exception e1) {
// //Loggers.general().info(LOG,"exception catched in outward reference payment"
// +
// e1.getMessage());
//// } finally {
//// try {
//// if (con != null) {
//// con.close();
//// if (ps1 != null)
//// ps1.close();
//// if (rs1 != null)
//// rs1.close();
//// }
//// } catch (SQLException e) {
//// //Loggers.general().info(LOG,"Connection Failed! Check output console");
//// e.printStackTrace();
//// }
//// }
// } else {
// //Loggers.general().info(LOG,"inward reference number is not given");
// }
//
// return true;
//
// }

public boolean nostrovalidate(String presentamt) {
boolean res = false;

// //Loggers.general().info(LOG,"The presentation Amount value" + presentamt);
double presentamt1 = Double.parseDouble(presentamt);
// //Loggers.general().info(LOG,"DOUBLE VALUE OF PRESENTATION" + presentamt1);
String elcamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
// //Loggers.general().info(LOG,"elc amount value" + elcamt );
double elcamt1 = Double.parseDouble(elcamt);
String nostref = getWrapper().getNOSTMT();// nostro ref number from user
// //Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
double amount = 0.00;
double amt202 = 0.00;
double amtpres = 0.00;
String Amountval = "";
String msg = "";
String MTref = "";
if (!nostref.equalsIgnoreCase("") || !nostref.isEmpty()) {
try {
      con = getConnection();
      String query = "SELECT trim(mt940.opening_amount),trim(mt103.MESSAGE_DATA),trim(mt940.reference_number) FROM ett_nostro_utility_match MAT,ett_nostro_utility_mt103 MT103,ett_nostro_utility_mt940 MT940 WHERE MAT.SWIFT_MATCH_REFERENCE = mt940.reference_number AND mt103.reference_number = MAT.SWIFT_MATCH_REFERENCE AND MAT.SWIFT_TYPE_REFERENCE='"
                  + getmasRefNo() + "'";
      // //Loggers.general().info(LOG,"query " + query);
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
            // //Loggers.general().info(LOG,"Entered 1st while");
            amount = rs.getDouble(1);
            Amountval = String.valueOf(amount);
            getWrapper().setNOSTAMT(Amountval);
            // //Loggers.general().info(LOG,"the amount in MT940 " + amount);
            msg = rs.getString(2);
            getWrapper().setINWMSG(msg);
            // //Loggers.general().info(LOG,"the inward message" + msg);
            MTref = rs.getString(3);
            getWrapper().setNOSTRM(MTref);
            // //Loggers.general().info(LOG,"the MT940 reference No" + MTref);
            // //Loggers.general().info(LOG,"the value of inward transaction
            // amount transaction amount");
            String query1 = "select nvl(sum(amount),0) from extevent ext,baseevent bev where ext.event=bev.key97 and bev.status='c' and ext.nostmt='"
                        + nostref + "'";
            // //Loggers.general().info(LOG,"the amount of mt202 " + query1);
            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                  // //Loggers.general().info(LOG,"entering 2nd while");
                  double amount202 = rs1.getDouble(1);
                  amt202 = amt202 + amount202; // sum value of MT202
                                                                  // transaction with same
                                                                  // reference number

            }
            // con.close();
            // ps1.close();
            // rs1.close();
            {
                  if (getMajorCode().equalsIgnoreCase("ODC") || getMajorCode().equalsIgnoreCase("CPCI")) {
                        amtpres = amt202 + presentamt1; // sum of
                                                                        // presentation and
                                                                        // Mt202 total
                                                                        // transaction
                                                                        // amount-
                        // //Loggers.general().info(LOG,"odc amount" + amtpres);
                  } else {
                        amtpres = amt202 + elcamt1;
                        // Loggers.general().info(LOG,"elc amount" + amtpres);
                  }
            }
      }

      if (amtpres > amount) {
            // //Loggers.general().info(LOG,"Error in nostro validation");

            res = true;
      } else {
            // Loggers.general().info(LOG,"Else is executed ");
      }
      // con.close();
      // ps.close();
      // rs.close();

}

catch (Exception ee) {
      // Loggers.general().info(LOG,"catch in inward reference" +
      // ee.getMessage());
}

finally {
      try {
            if (rs1 != null)
                  rs1.close();
            if (ps1 != null)
                  ps1.close();
            if (rs != null)
                  rs.close();
            if (ps != null)
                  ps.close();
            if (con != null)
                  con.close();

      } catch (SQLException e) {
            // Loggers.general().info(LOG,"Connection Failed! Check output
            // console");
            e.printStackTrace();
      }
}
} else {
// Loggers.general().info(LOG,"the Nostro MT103/202 reference number is
// empty");
}

return res;
}

public double decimalval(double val, String Currency) {

try {
String getced1 = "select c8ced from c8pf where c8ccy='" + Currency + "'";
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(getced1);
// //Loggers.general().info(LOG,"sql" + getced1);
rs1 = ps1.executeQuery();
int a1 = 0;
while (rs1.next()) {
      a1 = rs1.getInt(1);

}
if (a1 == 2) {
      val = val / 100;
}
if (a1 == 3) {
      val = val / 1000;
}
// con.close();
// ps1.close();
// rs1.close();

} catch (Exception e) {
// Loggers.general().info(LOG,"Decimal value exception" + e.getMessage());
} finally {
try {
      if (rs1 != null)
            rs1.close();
      if (ps1 != null)
            ps1.close();
      if (con != null)
            con.close();
} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}
return val;

}

public static void surrenderDB(Connection con, Statement stmt, ResultSet res) {
try {
if (res != null)
      res.close();
if (stmt != null)
      stmt.close();
if (con != null)
      con.close();
} catch (SQLException e) {
// Loggers.general().info(LOG,"Connection Failed! Check output console");
e.printStackTrace();
}
}

public int getShippingDetails(Connection con, String master, String formno, String billdate, String billno,
String portcode) {
int count = 0;
pes = null;
result = null;
try {
con = ConnectionMaster.getConnection();
String query = "SELECT COUNT(*) FROM EXTEVENTSHC EXE, baseevent BEV, MASTER MAS WHERE mas.key97=bev.master_key AND exe.fk_event=bev.extfield AND bev.refno_pfix IN ('DPR','CRE') AND MAS.MASTER_REF ='"
            + master + "' AND nvl(trim(exe.cbillnum),'xx')='" + billno
            + "' AND to_char(nvl(exe.cbillda,'01-01-87'),'dd-mm-yy') ='" + billdate
            + "' AND nvl(trim(exe.cformn),'xx') ='" + formno + "' AND nvl(trim(exe.cportco),'xx') ='" + portcode
            + "' AND bev.status ='c'";
// //Loggers.general().info(LOG,"query is " + query);
pes = con.prepareStatement(query);
result = pes.executeQuery();
if (result.next()) {
      count = result.getInt(1);
}

// con.close();
// pes.close();
// result.close();
} catch (Exception e) {
// Loggers.general().info(LOG,"Exception is " + e.getMessage());
}

finally {
try {
      if (result != null)
            result.close();
      if (pes != null)
            pes.close();
      if (con != null)
            con.close();

} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}
return count;
}

// public boolean checkNostroBalance() {
// boolean result = false;
// try {
//
// // Loggers.general().info(LOG,"enetered Nostro Oustanding Amount Validation
// // ");
// Connection connection = ConnectionMaster.getConnection();
// String query = "";
// String MT940_ref = "";
// String Nostro_Credit = "";
// Double NostroCredit = 0.0;
// String nostro_out = "";
// String nostro_util = "0.0";
// Double nos_out = 0.0;
// Double nos_util = 0.0;
// // Nostro_Credit = getWrapper().getNOSTAMT();
// Nostro_Credit = getDriverWrapper().getEventFieldAsText("cANE", "v", "m");
// if (!Nostro_Credit.equalsIgnoreCase("")) {
// NostroCredit = Double.valueOf(Nostro_Credit);
// }
// MT940_ref = getWrapper().getNOSTRM().trim();
// // //Loggers.general().info(LOG,"values are " + Nostro_Credit + " and " +
// // MT940_ref);
// nos_out = getOutStandingAmount(MT940_ref, NostroCredit);
//
// // nostro_util = getWrapper().getNOSAMT();
// nostro_util = getDriverWrapper().getEventFieldAsText("cAOU", "v",
// "m").replaceAll("[^\\d.]", "");
// // //Loggers.general().info(LOG,"util Amt is " + nostro_util);
// nos_util = Double.valueOf(nostro_util);
// if (nos_util > nos_out) {
// // //Loggers.general().info(LOG,"enetered if in Nostro Validation");
// result = true;
// } else {
// // Loggers.general().info(LOG,"value of Else is called");
// }
//
// } catch (Exception e) {
// // Loggers.general().info(LOG,"exception in checkNostroBalance " +
// // e.getMessage());
// }
// return result;
// }

// public double getOutStandingAmount(String MT940Ref, Double creditAmt) {
// double oustAmt = 0.0;
// double total_util_Amt = 0.0;
// Connection connection = null;
// PreparedStatement pes = null;
// ResultSet result = null;
// try {
// // //Loggers.general().info(LOG,"getOutStandingAmount is Called");
// connection = ConnectionMaster.getConnection();
// String query = "select total_utilised_amt AS TOTAL_AMT from
// ETTV_MT940_OUT_UTILISED_AMT where NOSTRO_REF='"
// + MT940Ref + "'";
// // Loggers.general().info(LOG,"query is " + query);
// pes = connection.prepareStatement(query);
// result = pes.executeQuery();
// while (result.next()) {
// // //Loggers.general().info(LOG,"enetered while");
// total_util_Amt = result.getDouble("TOTAL_AMT");
// }
// // //Loggers.general().info(LOG,"val is " + total_util_Amt);
// if (total_util_Amt >= 0) {
//
// oustAmt = creditAmt - total_util_Amt;
// }
// // //Loggers.general().info(LOG,"value of Oustatnding Amt is " + oustAmt);
//
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Exception in getOutStandingAmount " +
// // e.getMessage());
// } finally {
// try {
// if (connection != null) {
// connection.close();
// if (pes != null)
// pes.close();
// if (result != null)
// result.close();
// }
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check output
// // console");
// e.printStackTrace();
// }
// }
// return oustAmt;
// }

public String setxxforparameters(String Value) {
try {
if (Value.equalsIgnoreCase("") || Value == null) {
      Value = "x";
}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in setxxforparameters " +
// e.getMessage());
}
return Value;
}

// public Boolean isSIAvailableOld(String accountNum, String creditCCY) {
// Boolean isSIAvailable = false;
// String isSIAvailableQuery = "Select count(*) as COUNT From
// ETTV_NOSTRO_SI_MAINTENANCE Where SIACCOUNTNO = '"
// + accountNum + "' And (NVL(ALL_CURRENCIES, 'N') = 'Y' Or CURRENCY = '" +
// creditCCY + "')";
//
// // String strLog = "Log";
// // String dailyval_Log = "";
// // @SuppressWarnings("unchecked")
// // AdhocQuery<? extends
// // com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
// // getDriverWrapper()
// // .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
// // EXTGENCUSTPROP CodeLog = queryLog.getUnique();
// // if (CodeLog != null) {
// //
// // dailyval_Log = CodeLog.getPropval();
// // } else {
// // // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
// //
// // }
//
// // if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"SI AvailableQuery for Masterref - " +
// getmasRefNo() +
// " is " + isSIAvailableQuery);
// // }
// try {
// con = getConnection();
// ps = con.prepareStatement(isSIAvailableQuery);
// rs = ps.executeQuery();
// while (rs.next()) {
// int count = rs.getInt("COUNT");
// if (count == 1)
// isSIAvailable = true;
// }
// // if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Result for SI Availablity for Masterref - " +
// getmasRefNo() + " is " + isSIAvailable);
// // }
// } catch (Exception e) {
// // if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Exception in SI Available - " + e.getMessage());
// // }
// } finally {
// try {
// if (rs != null)
// rs.close();
// if (ps != null)
// ps.close();
// if (con != null)
// con.close();
//
// } catch (SQLException e) {
// // Loggers.general().info(LOG,"Connection Failed! Check output
// // console");
// e.printStackTrace();
// }
// }
// return isSIAvailable;
// }

public Boolean isSIAvailable(String accountNum, String creditCCY) {
Boolean isSIAvailable = false;
String isSIAvailableQuery = "Select count(*) as COUNT From ETTV_NOSTRO_SI_MAINTENANCE Where SIACCOUNTNO = '"
      + accountNum + "' And (NVL(ALL_CURRENCIES, 'N') = 'Y' Or CURRENCY = '" + creditCCY + "')";
LOG.info("SI AvailableQuery - " + isSIAvailableQuery);
Connection connect = null;
try {
connect = getConnection();
ps = connect.prepareStatement(isSIAvailableQuery);
rs = ps.executeQuery();
while (rs.next()) {
      int count = rs.getInt("COUNT");
      if (count == 1)
            isSIAvailable = true;
      LOG.info("ISAVAILABLE " + isSIAvailable);
}
} catch (Exception e) {
LOG.info("Exception in SI Available - " + e.getMessage());
e.printStackTrace();
} finally {
try {
      if (rs != null)
            rs.close();
      if (ps != null)
            ps.close();
      if (connect != null)
            connect.close();
} catch (SQLException e) {
      LOG.info("Connection Failed! Check output console");
      e.printStackTrace();
}
}
return isSIAvailable;
}

public void PeriodicUpfront() {
String perichargval = getPane().getPERCHRAD().trim();
LOG.info("Periodic charge in upfront " + perichargval);

if ((getMajorCode().equalsIgnoreCase("ILC")) && (getMinorCode().equalsIgnoreCase("ISI"))) {
try {

      String d = getDriverWrapper().getEventFieldAsText("ISS", "d", "").trim();
      String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();

      LOG.info("Issue date issue" + d);
      LOG.info("Charge basis end date issue " + d1);

      getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
      LOG.info("charge - issue date days issue" + getPane().getCHRPERAM());

} catch (Exception e) {
      LOG.info("Exception in calculating days issue " + e.getMessage());
}
}
if ((getMajorCode().equalsIgnoreCase("ILC")) && (getMinorCode().equalsIgnoreCase("NADI"))) {
try {

      String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "").trim();
      String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();

      LOG.info("Issue date adjust" + d);
      LOG.info("Charge basis end date adjust " + d1);

      getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
      LOG.info("charge - issue date days adjust" + getPane().getCHRPERAM());

} catch (Exception e) {
      LOG.info("Exception in calculating days adjust " + e.getMessage());
}
}

if (perichargval.equalsIgnoreCase("Yes")) {

if ((getMajorCode().equalsIgnoreCase("ILC")) && (getMinorCode().equalsIgnoreCase("NAMI"))) {

      String imlAmount = getDriverWrapper().getEventFieldAsText("IML", "v", "m").trim();
      String n = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();
      String m = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "").trim();
      LOG.info("iml amount amend" + imlAmount);
      LOG.info("charge end end mirror " + m);
      LOG.info("charge end end  " + n);

      try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

            Date date1 = sdf1.parse(n);
            Date date2 = sdf1.parse(m);

            LOG.info("date1.compareTo(date2) " + date1.compareTo(date2));
            if (date1.compareTo(date2) > 0) {
                  try {

                        String d = getDriverWrapper().getEventFieldAsText("occBKY", "d", "");
                        String d1 = getDriverWrapper().getEventFieldAsText("nccBKY", "d", "");

                        LOG.info("Issue date amend" + d);
                        LOG.info("Charge basis end date amend " + d1);

                        getPane().setCHRTENIN(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                        LOG.info("charge - issue date days amend" + getPane().getCHRTENIN());

                  } catch (Exception e) {
                        LOG.info("Exception in calculating days amend " + e.getMessage());
                  }
            }

            if (imlAmount != null && !imlAmount.equalsIgnoreCase("")) {

                  try {

                        String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "");
                        String d1 = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "");

                        LOG.info("Issue date amend" + d);
                        LOG.info("Charge basis end date amend " + d1);

                  } catch (Exception e) {
                        LOG.info("Exception in calculating days amend " + e.getMessage());
                  }
            }

      } catch (Exception e) {
            LOG.info("Exception in date format" + e.getMessage());
      }

}
}
}

public static String difference(String a, String b) throws ParseException {
SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
Date date1 = format.parse(a);
Date date2 = format.parse(b);
long diff = date2.getTime() - date1.getTime();
double z = Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
// //Loggers.general().info(LOG,"values " + Math.abs(TimeUnit.DAYS.convert(diff,
// TimeUnit.MILLISECONDS)));
return Double.toString(z);

}

public int getduplicate() {
int cnt = 0;
String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eveRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
try {

// //Loggers.general().info(LOG,masRefNo);
String query_103 = "SELECT count(*) FROM UBZONE.master mas, UBZONE.BASEEVENT bas WHERE mas.KEY97      =bas.MASTER_KEY AND bas.STATUS  IN ('i','c') and mas.master_ref='"
            + masRefNo + "' " + " and bas.refno_pfix||lpad(trim(bas.refno_serl), 3,'0')='" + eveRefNo + "'";
LOG.info("Master Reference====>" + masRefNo);
LOG.info("Event Reference====>" + eveRefNo);
LOG.info("Query====>" + query_103);
// + query_103);
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(query_103);
rs = ps.executeQuery();
while (rs.next()) {
      cnt = rs.getInt(1);
      LOG.info("cnt====>" + cnt);
}
} catch (Exception e) {
e.printStackTrace();
}
return cnt;
}

public int getLimitMatch() {
int match = 0;
try {

con = ConnectionMaster.getConnection();
String relevnt = null;
String prelimit = null;
String Mast = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
String Evnt = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
String Limit = getDriverWrapper().getEventFieldAsText("cBNR", "s", "").trim();
LOG.info("Master " + Mast);
LOG.info("Evnt " + Evnt);
LOG.info("Limit " + Limit);
String closureQuery = "SELECT MAS.MASTER_REF AS PREVIOUS_MASTER, BEV.REFNO_PFIX|| LPAD(BEV.REFNO_SERL,3,0) AS PREVIOUS_EVENT"
            + " FROM MASTER MAS,BASEEVENT BEV, BASEEVENT BEV1, LCPAYMENT LCP  WHERE MAS.KEY97    = BEV.MASTER_KEY"
            + "   AND BEV.KEY97      = LCP.KEY97      AND LCP.NEXTOUT_EV = BEV1.KEY97     AND BEV1.REFNO_PFIX || LPAD(BEV1.REFNO_SERL,3,0)= '"
            + Evnt + "'" + " AND MAS.MASTER_REF            = '" + Mast + "'";
LOG.info("query " + closureQuery);
ps = con.prepareStatement(closureQuery);
rs = ps.executeQuery();
while (rs.next()) {
      relevnt = rs.getString("PREVIOUS_EVENT");
      LOG.info("relevnt " + relevnt);
}
if (relevnt != null || (!relevnt.equalsIgnoreCase(""))) {
      String query = "SELECT row_number() over (ORDER BY BEV.KEY97 ASC) AS ROWN, MAS.MASTER_REF, MAS.KEY97 mas_key,"
                  + " BEV.KEY97 bev_key, BEV.REFNO_PFIX||lpad(BEV.REFNO_SERL,3,0) bev_ref,LIMBLK FROM MASTER MAS, BASEEVENT BEV,"
                  + " EXTEVENT EXT WHERE MAS.KEY97   =BEV.MASTER_KEY AND BEV.KEY97     =EXT.EVENT AND BEV.status   <>'a' AND trim(LIMBLK) IS NOT NULL"
                  + " AND MAS.MASTER_REF='" + Mast + "' and trim(BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0))='"
                  + relevnt + "' ORDER BY BEV.KEY97";
      LOG.info("query11 " + query);
      ps1 = con.prepareStatement(query);

      rs1 = ps1.executeQuery();
      while (rs1.next()) {
            prelimit = rs1.getString("LIMBLK");
            LOG.info("prelimit " + prelimit);
      }
}
if (prelimit.equalsIgnoreCase("1") || prelimit.equalsIgnoreCase("2")) {

      if (!Limit.equalsIgnoreCase(prelimit)) {
            match = 1;
      }
}

} catch (Exception e) {

}
return match;
}

public int getRepayProb() {
int match = 0;
if ((getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP"))
      || (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD"))) {
try {

      String RepayAmt = null;
      String Outstamt = null;
      String RepayAmtccy = "INR";
      BigDecimal Repamt = null;
      BigDecimal EvntNomAmt = null;
      BigDecimal Outstamtdec = null;
      String masReference = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
      String EventReference = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
      String FinanceMas = getDriverWrapper().getEventFieldAsText("FMST", "l", "");
      String EvntNom = getDriverWrapper().getEventFieldAsText("EVAM", "v", "m");
      LOG.info("masReference --->" + masReference);
      LOG.info("EventReference --->" + EventReference);
      LOG.info("EvntNom --->" + EvntNom);
      LOG.info("FinanceMas --->" + FinanceMas);
      con = getConnection();
      String Query = "  SELECT TRIM(MAS1.MASTER_REF) AS MASTER_REF,BEV1.REFNO_PFIX || LPAD(BEV1.REFNO_SERL,3,0) AS EVENT_REF,PRI_PAID/(SELECT power(10,c8.c8ced) FROM c8pf c8 WHERE c8.c8ccy= PRIPAIDCCY)                  AS PRI_PAID_AMT,"
                  + " REP_FIN.PRIPAIDCCY AS PRI_PAID_CCY, MAS1.AMT_O_S       AS OUTSTANDING_AMT  FROM MASTER MAS, BASEEVENT BEV, BASEEVENT BEV1, MASTER MAS1, FINREPAY REP_FIN WHERE MAS.KEY97     = BEV.MASTER_KEY AND BEV.KEY97       = BEV1.ATTACHD_EV"
                  + " AND BEV1.MASTER_KEY = MAS1.KEY97 AND BEV1.KEY97      = REP_FIN.KEY97 AND MAS.MASTER_REF  = '"
                  + masReference + "' AND BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) = '" + EventReference + "'";

      LOG.info("Repay event amount Query --->" + Query);

      ps = con.prepareStatement(Query);
      rs = ps.executeQuery();
      if (rs.next()) {
            RepayAmt = rs.getString("PRI_PAID_AMT");
            Outstamt = rs.getString("OUTSTANDING_AMT");
            RepayAmtccy = rs.getString("PRI_PAID_CCY");
            LOG.info("RepayAmt --->" + RepayAmt);
            LOG.info("Outstamt --->" + Outstamt);
      } else {
            RepayAmt = "0";
            Outstamt = "0";
      }
      LOG.info("RepayAmt --->" + RepayAmt);
      LOG.info("Outstamt --->" + Outstamt);
      getPane().setLIAINCDE(RepayAmt + " " + RepayAmtccy);
      if (RepayAmt != null && !RepayAmt.isEmpty()) {
            Repamt = new BigDecimal(RepayAmt);
      }
      if (EvntNom != null && !EvntNom.isEmpty()) {
            EvntNomAmt = new BigDecimal(EvntNom);
      }
      if (Outstamt != null && !Outstamt.isEmpty()) {
            Outstamtdec = new BigDecimal(Outstamt);
      }

      LOG.info("Compare --->" + Repamt.compareTo(EvntNomAmt));
      LOG.info("Compare --->" + Outstamtdec.compareTo(Repamt));
      if (FinanceMas.equalsIgnoreCase("Y") && (Repamt.compareTo(EvntNomAmt) != 0)
                  && (Outstamtdec.compareTo(Repamt) == 1)) {
            LOG.info("Entered 11/03/20 --->" + Outstamtdec);
            match = 1;
      }
} catch (Exception e) {

}
}
return match;
}

// CR 140 starts
public int preshipWar() {
int count = 0, cnt = 0;
String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
if (stepID.equalsIgnoreCase("CBS Maker1") || stepID.equalsIgnoreCase("CBS Maker")
      || stepID.equalsIgnoreCase("CBS Reject")) {

try {

      String customera = null;
      String prodtype = null;
      prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                                                                                                            // CODE
                                                                                                            // ILC
      // prodtypea = getDriverWrapper().getEventFieldAsText("PLN", "s", "");// pROD
      // Name
      // Import
      // lc

      // Cutomer for export lc
      if (prodtype.trim().equalsIgnoreCase("ILC")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                        // id
      } // end of ILC
      else if (prodtype.trim().equalsIgnoreCase("ELC")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                        // id
      } else if (prodtype.trim().equalsIgnoreCase("IDC")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("DRE", "p", "no");// party
                                                                                                                        // id
      } // end of ELC
      else if (prodtype.trim().equalsIgnoreCase("IGT")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                        // id
      } else if (prodtype.trim().equalsIgnoreCase("ISB")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                        // id
      } else if (prodtype.trim().equalsIgnoreCase("ODC")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            String eventRefPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s", "");// Event
                                                                                                                                    // reference
                                                                                                                                    // prefix
            // //Loggers.general () .info(LOG,"eventRefPrefix" + eventRefPrefix);
            if (eventRefPrefix.trim().equalsIgnoreCase("FEC")) {
                  // customer = getDriverWrapper().getEventFieldAsText("B+DB", "p", "cu");// party
                  // name
                  customera = getDriverWrapper().getEventFieldAsText("B+DB", "p", "no");// party
                                                                                                                              // id
            } else {
                  // customer = getDriverWrapper().getEventFieldAsText("DRW", "p", "cu");// party
                  // name
                  customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                              // id
            }
      } else if (prodtype.trim().equalsIgnoreCase("OCC")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("DRW", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                        // id
      } // end of odc
      else if (prodtype.trim().equalsIgnoreCase("IGT")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("APP", "p", "no");// party
                                                                                                                        // id
      } // end of import gurantee
      else if (prodtype.trim().equalsIgnoreCase("EGT")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                        // id
      } // end of Export Gura
      else if (prodtype.trim().equalsIgnoreCase("ICC")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("DRE", "p", "no");// party
                                                                                                                        // id
      } // end of Shipping Gur
      else if (prodtype.trim().equalsIgnoreCase("FSA")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("DBT", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
                                                                                                                        // id
      } else if (prodtype.trim().equalsIgnoreCase("ESB")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                        // id
      } else if (prodtype.trim().equalsIgnoreCase("RMB")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("ISS", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("ISS", "p", "no");// party
                                                                                                                        // id
      } else if (prodtype.trim().equalsIgnoreCase("FEL")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("B+FT", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("B+FT", "p", "no");// party
                                                                                                                        // id
      } else if (prodtype.trim().equalsIgnoreCase("CPCI")) {
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode());
            // customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                        // id
      } else {
            // //Loggers.general () .info(LOG,"Check in Else Part");
            // //Loggers.general () .info(LOG,"Major Code" + getMajorCode() + "and Minor
            // Code" + getMinorCode());
            // customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "");// party
            // name
            customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");// party
                                                                                                                        // id
      }

      LOG.info("Preshipment ODC payment customer" + customera);
      String cust = customera.trim();
      con = getConnection();
      if (!cust.equalsIgnoreCase("") && cust != null && cust.length() > 0) {
            String query1 = "SELECT count(*) FROM UBZONE.PRESHIPMENT_FIN_BASE_VIEW WHERE PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS IN ('LIV')"
                        + " AND PRODUCT_CODE  ='FSA' AND PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S   > 0 AND PRESHIPMENT_FIN_BASE_VIEW.APPCUSTEMERID='"
                        + cust + "'";

            // if (dailyval_Log.equalsIgnoreCase("YES")) {
            LOG.info("Query Result for customera----------> " + query1);
            LOG.info("Query Result for customera----------> " + cust);

            // }

            ps1 = con.prepareStatement(query1);

            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                  count = rs1.getInt(1);
                  LOG.info("COUNT for reference --------> " + count);
            }
      }
      if (count != 0) {
            LOG.info("Setting error pre");

            cnt = 1;
      }
} catch (Exception e) {

} finally {
      try {
            if (rs != null)
                  rs.close();
            if (ps != null)
                  ps.close();
            if (con != null)
                  con.close();
      } catch (SQLException e) {
            // Loggers.general () .info(LOG,"Connection Failed! Check output
            // console");
            e.printStackTrace();
      }
}
}
return cnt;
}

//CR 140 ends
// CR 37 starts
public String getMstrSttle() {
String mstrSetAccount = "";

LOG.info("Inside MasterSettle");
String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
String mainEvntRef = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
try {
con = getConnection();
/*
 * String query3
 * ="SELECT TRIM(MSTR.ACCT_NO) FROM MASTER MAS,BASEEVENT BEV ,TIDATAITEM TID , MSTRSETTLE MSTR "
 * + "WHERE MAS.KEY97=BEV.MASTER_KEY " +
 * "AND  TRIM(BEV.KEY97)       = TRIM(TID.EVENT_KEY)" +
 * "AND TRIM(TID.KEY97)       = TRIM(MSTR.KEY97)" +
 * "AND MAS.MASTER_REF='"+mainMasterRef+"'" +
 * "AND (BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0))='"
 * +mainEvntRef+"' AND TRIM(MSTR.MOVEMENT)='P'";
 */

String query3 = "SELECT TRIM(MSTR.ACCT_NO) FROM MASTER MAS,BASEEVENT BEV ,TIDATAITEM TID , MSTRSETTLE MSTR "
            + "WHERE MAS.KEY97=BEV.MASTER_KEY " + "AND  BEV.KEY97      = TID.EVENT_KEY "
            + "AND TID.KEY97      = MSTR.KEY97 " + "AND MAS.MASTER_REF='" + mainMasterRef + "' "
            + "AND (BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0))='" + mainEvntRef
            + "' AND TRIM(MSTR.MOVEMENT)='P'";

LOG.info("Query Result for Account Validation----------> " + query3);

ps1 = con.prepareStatement(query3);

rs1 = ps1.executeQuery();
if (rs1.next()) {
      mstrSetAccount = getEmptyIfNull(rs1.getString(1));

      LOG.info("Master Settlement Account----------> " + mstrSetAccount);
}

} catch (Exception e) {
LOG.info("Exception in getmasterSettle");
} finally {
surrenderDB(con, ps1, rs1);
}
return mstrSetAccount;
}

public ArrayList<String> getPayAccount() {
LOG.info("Inside GetPayAccount");

ArrayList<String> settlementAccountList = new ArrayList<String>();
String party = "";
String settlementAccount = "";
int result = 0;

String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
String mainEvntRef = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
try {
con = getConnection();
String query3 = "SELECT trim(pos.ACCT_NUM) FROM MASTER MAS,BASEEVENT BEV,RELITEM REL ,POSTING POS"
            + " WHERE TRIM(MAS.KEY97)=TRIM(BEV.MASTER_KEY)" + " AND TRIM(BEV.KEY97)=TRIM(REL.EVENT_KEY)"
            + "AND TRIM(REL.KEY97)=TRIM(POS.KEY97)" + " AND MAS.MASTER_REF='" + mainMasterRef + "'"
            + "AND (BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))='" + mainEvntRef + "'  "
            + "AND  POS.POSTED_AS IS NOT NULL " + "AND POS.SETTINSTRS IS NOT NULL "
            + "AND trim(POS.SP_CODE) IS NULL AND POS.DR_CR_FLG='C'";
LOG.info("Query Result for Account Validation----------> " + query3);

ps1 = con.prepareStatement(query3);

rs1 = ps1.executeQuery();
if (rs1.next()) {
      settlementAccount = getEmptyIfNull(rs1.getString(1));
      settlementAccountList.add(settlementAccount);

      LOG.info("Master Settlement Account----------> " + settlementAccount);
}

} catch (Exception e) {
LOG.info("Exception in getmasterSettle");
} finally {
surrenderDB(con, ps1, rs1);
}
return settlementAccountList;
}

public boolean isNull(Object obj) {
if (obj == null) {
return true;
} else if (obj instanceof String) {
return (((String) obj).trim().length() == 0);
} else if (obj instanceof Collection) {
return (((Collection) obj).size() == 0);
} else {
return false;
}
}

public String convertIfNull(Object sourceStr, Object toConvert) {
return isNull(sourceStr) ? toConvert.toString() : sourceStr.toString();
}

public String getEmptyIfNull(Object sourceStr) {
return convertIfNull(sourceStr, "");
}

// CR 37 ends
// LIMIT ISSUE -05Sep2020 starts
public int amountError() {
int count = 0;

try {

double payamt = 0.0;
String Masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
String eventname = getDriverWrapper().getEventFieldAsText("CLM", "r", "").trim();
String Payact = getDriverWrapper().getEventFieldAsText("PYAD", "s", "").trim();// FPP:XPTS
String Paytype = getDriverWrapper().getEventFieldAsText("FPP:XPTS", "s", "").trim();
String clmamt = getDriverWrapper().getEventFieldAsText("FPP:XAM", "v", "u").trim();
String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
String ownlc = getDriverWrapper().getEventFieldAsText("cBKR", "l", "").trim();
String Mixpay = getDriverWrapper().getEventFieldAsText("MIX", "l", "").trim();

// Loggers.general () .info(LOG,"Event count===>"+EventSerial);
LOG.info("Masterref count===>" + Masterref);
LOG.info("subproduct count===>" + subproduct);
LOG.info("eventname count===>" + eventname);
LOG.info("Payact count===>" + Payact);
LOG.info("Paytype count===>" + Paytype);
LOG.info("clmamt count===>" + clmamt);
LOG.info("ownlc count===>" + ownlc);
LOG.info("Mixpay count===>" + Mixpay);
con = getConnection();
String query1 = "SELECT ext.PAY_AMT FROM UBZONE.master mas, UBZONE.BASEEVENT bas, UBZONE.PARTPAYMNT ext WHERE"
            + " mas.KEY97      =bas.MASTER_KEY and bas.KEY97=ext.PAYEV_KEY and mas.master_ref='" + Masterref
            + "' and trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventname + "'";

// if (dailyval_Log.equalsIgnoreCase("YES")) {
LOG.info("Query for pay amount----------> " + query1);

// }

ps1 = con.prepareStatement(query1);

rs1 = ps1.executeQuery();
while (rs1.next()) {
      payamt = rs1.getDouble(1);
      LOG.info("payamt from db --------> " + payamt);
}
BigDecimal divideByBig1 = new BigDecimal(clmamt);
BigDecimal divideByBig2 = new BigDecimal(payamt);
LOG.info("divideByBig1 for pay amount----------> " + divideByBig1);
LOG.info("divideByBig2 for pay amount----------> " + divideByBig2);

int res1 = 0;
res1 = divideByBig1.compareTo(divideByBig2);
LOG.info("result for comp --------> " + res1);

if (res1 != 0
            && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Maker")
                        || step_csm.equalsIgnoreCase("CBS Reject"))
            && getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")
            && subproduct.equalsIgnoreCase("ELD") && !Payact.equalsIgnoreCase("AMD")
            && ownlc.equalsIgnoreCase("N") && Mixpay.equalsIgnoreCase("N")
            && (Paytype.equalsIgnoreCase("Acceptance") || Paytype.equalsIgnoreCase("Deferred"))) {
      LOG.info("Setting error");
      count = 1;
}

} catch (Exception e) {

} finally {
surrenderDB(con, ps1, rs1);
}

return count;
}

// LIMIT ISSUE -05Sep2020 ends
// INC4431622 starts
public void removeSpace() {
try {
String Billnum = null;
// Date ShipDate=null;
String Portcode = null;
String formno = null;
LOG.info("Billnum from db inital --------> " + Billnum);
// Loggers.general () .info(LOG,"ShipDate from db inital--------> " + ShipDate);
LOG.info("Portcode from db inital--------> " + Portcode);
LOG.info("formno from db inital--------> " + formno);
if (getMajorCode().equalsIgnoreCase("ELC")) {
      List<ExtEventShippingTable> shipTab = (List<ExtEventShippingTable>) getWrapper()
                  .getExtEventShippingTable();
      LOG.info("shipping table for notional" + shipTab.size());

      for (int i = 0; i < shipTab.size(); i++) {
            LOG.info("shipping table itteration" + i);
            ExtEventShippingTable ship = shipTab.get(i);
            Billnum = ship.getBILLNUM().trim();
            // ShipDate=ship.getBILLDAT().trim();
            Portcode = ship.getPORTCODDD().trim();
            formno = ship.getFORMNUM().trim();
            LOG.info("Billnum from db --------> " + Billnum);
            // Loggers.general () .info(LOG,"ShipDate from db --------> " + ShipDate);
            LOG.info("Portcode from db --------> " + Portcode);
            LOG.info("formno from db --------> " + formno);
            ship.setBILLNUM(Billnum);
            // ship.setBILLDAT(ShipDate);
            ship.setPORTCODDD(Portcode);
            ship.setFORMNUM(formno);

      }
}
if (getMajorCode().equalsIgnoreCase("ODC")) {
      System.out.println("###shipping Collection iteration");
      List<ExtEventShippingCollections> shipTab1 = (List<ExtEventShippingCollections>) getWrapper()
                  .getExtEventShippingCollections();
      LOG.info("shipping Collection iteration" + shipTab1);
      for (int i = 0; i < shipTab1.size(); i++) {
            LOG.info("shipping Collection iteration" + i);
            ExtEventShippingCollections ship = shipTab1.get(i);
            Billnum = ship.getCBILLNUM().trim();
            // ShipDate=ship.getCBILLDA().trim();
            Portcode = ship.getCPORTCO().trim();
            formno = ship.getCFORMN().trim();
            LOG.info("Billnum from db1 --------> " + Billnum);
            // Loggers.general () .info(LOG,"ShipDate from db1 --------> " + ShipDate);
            LOG.info("Portcode from db1 --------> " + Portcode);
            LOG.info("formno from db1 --------> " + formno);
            ship.setCBILLNUM(Billnum);
            // ship.setCBILLDA(ShipDate);
            ship.setCPORTCO(Portcode);
            ship.setCFORMN(formno);

      }
}
} catch (Exception e) {
LOG.info("Exception update" + e.getMessage());
}
}
// INC4431622 ends
// swiftsfms

public void checkGoodsDesc() {
// Goods Description auto population in custom test area - Moses Daniel S -
// 21-08-2021

if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equals("ISI")) {
String goodsCode = "";
String goodsDescription = "";
try {
      goodsCode = getDriverWrapper().getEventFieldAsText("GDC", "s", "");
      LOG.info("Goods Code Selected in TIPLUS - ILC - ISI " + getmasRefNo() + "-" + geteventRefNo() + " - "
                  + goodsCode);
      if (!goodsCode.isEmpty() && goodsCode.trim().length() != 0) {
            // goodsDescription=getDriverWrapper().getEventFieldAsText("GDCD", "s", "");
            goodsDescription = getGoodsDescription(goodsCode);
            if (!goodsDescription.isEmpty() && goodsDescription.trim().length() != 0) {
                  getPane().setGDESC(goodsDescription);
            }
      }
} catch (Exception e) {
      e.printStackTrace();
}
}
}

//Posting FX Rate Population - 08/09/2021 by Vaisak   
public void getPostingFxrate() {
String masCurr = "";
String postFXRate = "";
int count = 0;
getPane().setPOSTRATE("");
if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IGT")
      || getMajorCode().equalsIgnoreCase("IDC") || getMajorCode().equalsIgnoreCase("SHG")
      || getMajorCode().equalsIgnoreCase("ELC") || getMajorCode().equalsIgnoreCase("ODC")
      || getMajorCode().equalsIgnoreCase("EGT") || getMajorCode().equalsIgnoreCase("ICC")
      || getMajorCode().equalsIgnoreCase("OCC") || getMajorCode().equalsIgnoreCase("CPCO")
      || getMajorCode().equalsIgnoreCase("CPCI") || getMajorCode().equalsIgnoreCase("CPBO")
      || getMajorCode().equalsIgnoreCase("CPBI") || getMajorCode().equalsIgnoreCase("FEL")
      || getMajorCode().equalsIgnoreCase("FOC") || getMajorCode().equalsIgnoreCase("FSA")
      || getMajorCode().equalsIgnoreCase("FIL"))
      && (getMinorCode().equalsIgnoreCase("ISI") || getMinorCode().equalsIgnoreCase("CRE")
                  || getMinorCode().equalsIgnoreCase("IIG") || getMinorCode().equalsIgnoreCase("VEG")
                  || getMinorCode().equalsIgnoreCase("ADE") || getMinorCode().equalsIgnoreCase("CSA4")
                  || getMinorCode().equalsIgnoreCase("CSA") || getMinorCode().equalsIgnoreCase("CSA1")
                  || getMinorCode().equalsIgnoreCase("SCR") || getMinorCode().equalsIgnoreCase("PBIC")
                  || getMinorCode().equalsIgnoreCase("PBOC") || getMinorCode().equalsIgnoreCase("PCIC")
                  || getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("CSA3")
                  || getMinorCode().equalsIgnoreCase("CSA2"))) {
/** RSA1 added by Saurabh Bittu on 16-11-2024 **/
try {
      System.out.println("In getPostingFxrate");
      con = getConnection();
      masCurr = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
      String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
      String masRef = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
      String query = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='" + masCurr + "'";

      if ((getMajorCode().equalsIgnoreCase("ELC") || getMajorCode().equalsIgnoreCase("ODC")
                  || getMajorCode().equalsIgnoreCase("EGT") || getMajorCode().equalsIgnoreCase("OCC"))) {
            query = "SELECT BUYEXC03 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='" + masCurr + "'";
      }
      System.out.println("posting fx rate " + query);
      ps1 = con.prepareStatement(query);
      rs1 = ps1.executeQuery();
      while (rs1.next()) {
            postFXRate = rs1.getString(1);
            getPane().setPOSTRATE(postFXRate);

      }
//LOG.info("Posing FX Rate for " + masRef + " : " + postFXRate);
      System.out.println("Posing FX Rate for " + masRef + " : " + postFXRate);
      String query1 = "SELECT COUNT(*) FROM ETTFXRATE WHERE MASTER_REF = '" + masRef + "'";
      ps1 = con.prepareStatement(query1);
      rs1 = ps1.executeQuery();
      while (rs1.next()) {
            count = rs1.getInt(1);
      }
      if (count == 0) {
            String queryInsert = "INSERT INTO ETTFXRATE VALUES ('" + masRef.trim() + "','" + postFXRate.trim()
                        + "')";
            ps1 = con.prepareStatement(queryInsert);
            ps1.executeUpdate();
            System.out.println("ETTFXRATE update FX Rate for " + queryInsert);
      } else if (count > 0) {
            String queryUpdate = "UPDATE ETTFXRATE SET FXRATE= " + postFXRate.trim() + " WHERE MASTER_REF = '"
                        + masRef.trim() + "'";
            ps1 = con.prepareStatement(queryUpdate);
            ps1.executeUpdate();
            System.out.println("ETTFXRATE update FX Rate for " + queryUpdate);
      }
      String extevent = "update extevent set postrate='" + postFXRate.trim() + "' where key29=("
                  + "SELECT exte.KEY29 FROM master mas, baseevent bev, extevent exte "
                  + "where mas.KEY97 = bev.MASTER_KEY " + "and bev.KEY97 = exte.EVENT " + "and mas.MASTER_REF='"
                  + masRef.trim() + "'" + " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='" + eventCode + "')";
      System.out.println("extevent update FX Rate for " + extevent);
      ps1 = con.prepareStatement(extevent);
      ps1.executeUpdate();

} catch (Exception e) {

      LOG.info("Posing FX Rate: " + e.getMessage());

} finally {
      surrenderDB(con, ps1, rs1);
}
}
}

//TCS of LRS Transaction 01-10-21   

public void remittanceLRSChargeChange(String masRef, String eveRef) {

PreparedStatement ps3 = null;
ResultSet rs3 = null;
String sys = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
String masterAmt = getDriverWrapper().getEventFieldAsText("DRAM", "v", "m").trim();
String masterCcy = getDriverWrapper().getEventFieldAsText("DRAM", "v", "c").trim();
String currPurCode = getDriverWrapper().getEventFieldAsText("cAQL", "s", "c").trim();
String custId = getDriverWrapper().getEventFieldAsText("RMIT", "p", "cu").trim();
String panNo = getPane().getPANDETAI().trim();
String srcOfFund = getPane().getLRSTCSYN();
String exRatUSD = "";
String totIntAmt = "";
String currIntAmt = "";
String totComplAmt = "";
String totCurrAmt = "";
String totIntAmtCumula = "";
BigDecimal masAmt = BigDecimal.ZERO;
BigDecimal currIntAmtBig = BigDecimal.ZERO;
BigDecimal totIntAmtCumulaBig = BigDecimal.ZERO;
BigDecimal totCurrAmtBig = BigDecimal.ZERO;

Connection conn = null;
try {
conn = ConnectionMaster.getConnection();
String from = null;
String to = null;
String sys1 = null;
String year = null;
int month = 3;
int y = 0;
sys1 = sys.substring(3, 5);
year = sys.substring(6, 10);
int mon = Integer.parseInt(sys1);
y = Integer.parseInt(year);
System.out.println("In remittanceLRSChargeChange");
if (mon <= month) {
      from = "01/04/" + (y - 1);
      to = sys;
} else {
      from = "01/04/" + y;
      to = sys;
}

// FOR TOTAL AMOUNT & INTEREST AMOUNT

String query = "SELECT SUM(THEME_GENIUS_PKG.CONVAMT(EXT.CCY_61,EXT.LRSCRAMT)) AS TOTAMT, "
            + " SUM(THEME_GENIUS_PKG.CONVAMT(EXT.CCY_66,EXT.LRSCRINT)) AS TOTINTAMT "
            + " FROM MASTER MAS,BASEEVENT BEV,EXTEVENT EXT,EXEMPL30 PROD,TIDATAITEM TID,"
            + " PARTYDTLS PRD,PRODTYPE SPROD  " + " WHERE MAS.KEY97 = BEV.MASTER_KEY "
            + " AND BEV.KEY97   = EXT.EVENT " + " AND MAS.EXEMPLAR    = PROD.KEY97 "
            + " AND MAS.KEY97      = TID.MASTER_KEY " + " AND TID.KEY97      = PRD.KEY97 "
            + " AND MAS.PRODTYPE      = SPROD.KEY97 " + " AND PROD.CODE79 IN ('CPCO','CPHO') "
            + " AND BEV.REFNO_PFIX      = 'CRE' " + " AND SPROD.CODE IN ('LRS','DDI') "
            + " AND BEV.STATUS = 'c' " + " AND PRD.ROLE       = 'RFP' " + " AND PRD.CUS_MNM    = '" + custId
            + "' " + " AND EXT.OPURPOS IN ('S0001', 'S0002', 'S0003', 'S0004', 'S0005', 'S0021', 'S0022', "
            + " 'S0023', 'S0301','S0303', 'S0304', 'S0305', 'S0306', 'S1107', 'S1108', "
            + " 'S1301', 'S1302', 'S1303', 'S1307', 'S0011', 'S0603') " + " AND EXT.PANDETAI  = '" + panNo
            + "' " + " AND BEV.FINISHED BETWEEN TO_DATE('" + from + "','DD-MM-YY') " + " AND TO_DATE('" + to
            + "','DD-MM-YY') GROUP BY PRD.CUS_MNM ";
System.out.println("query1--->" + query);
ps3 = conn.prepareStatement(query);
rs3 = ps3.executeQuery();
while (rs3.next()) {
      totComplAmt = rs3.getString(1);
      totIntAmt = rs3.getString(2);
}

if (totComplAmt == null || totComplAmt.trim().equals(""))
      totComplAmt = "0.00";
if (totIntAmt == null || totIntAmt.trim().equals(""))
      totIntAmt = "0.00";
BigDecimal totIntAmtBig = new BigDecimal(totIntAmt);
ConnectionMaster.surrenderDB(null, ps3, rs3);

// FOR TAKING CURRENT AMOUNT WITH FXRATE OR EXCHANGERATE

if (currPurCode.equals("S0001") || currPurCode.equals("S0002") || currPurCode.equals("S0003")
            || currPurCode.equals("S0004") || currPurCode.equals("S0005") || currPurCode.equals("S0021")
            || currPurCode.equals("S0022") || currPurCode.equals("S0023") || currPurCode.equals("S0301")
            || currPurCode.equals("S0303") || currPurCode.equals("S0304") || currPurCode.equals("S0305")
            || currPurCode.equals("S0306") || currPurCode.equals("S1107") || currPurCode.equals("S1108")
            || currPurCode.equals("S1301") || currPurCode.equals("S1302") || currPurCode.equals("S1303")
            || currPurCode.equals("S1307") || currPurCode.equals("S0011") || currPurCode.equals("S0603")) {

      if (!masterCcy.equals("INR")) {
            query = "SELECT DISTINCT NVL(TRIM(EXCH_RATE),'') AS FXRATE "
                        + " FROM MASTER MAS,BASEEVENT BEV,RELITEM REL,POSTING POS,FXBASEDEAL FXD, "
                        + " EXEMPL30 PROD WHERE MAS.KEY97         = BEV.MASTER_KEY "
                        + " AND Bev.Key97           = Rel.Event_Key " + " AND Rel.Key97           = Pos.Key97 "
                        + " AND Pos.Fx_Key          = Fxd.Key97 " + " AND MAS.EXEMPLAR          = PROD.KEY97 "
                        + " AND PROD.CODE79 IN ('CPCO','CPHO') " + " AND BEV.REFNO_PFIX      = 'CRE'"
                        + " AND MAS.MASTER_REF ='" + masRef + "' "
                        + " AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0) ='" + eveRef + "' ";
            ps3 = conn.prepareStatement(query);
            rs3 = ps3.executeQuery();
            while (rs3.next()) {
                  exRatUSD = rs3.getString(1);
            }

            if (exRatUSD == null || exRatUSD.equals("")) {
                  System.out.println("Exchange Rate query no value");
//          exRatUSD = getPane().getEXRATE();
                  exRatUSD = getDriverWrapper().getEventFieldAsText("FXD", "t", "RATE");
            }
            System.out.println("ExchangeRate: " + exRatUSD);
      }
      if (exRatUSD != null && !exRatUSD.trim().equals("")) {
            masAmt = new BigDecimal(masterAmt);
            BigDecimal exR = new BigDecimal(exRatUSD);
            totCurrAmtBig = masAmt.multiply(exR);
            totCurrAmt = totCurrAmtBig.setScale(2, BigDecimal.ROUND_HALF_EVEN) + " INR";
      } else {
            masAmt = new BigDecimal(masterAmt);
            totCurrAmtBig = masAmt.multiply(BigDecimal.ONE);
            totCurrAmt = masterAmt + " INR";
      }

}

// FOR TOTAL CUMULATIVE AMOUNT

BigDecimal amount1 = new BigDecimal(totComplAmt);

BigDecimal totalAmt = amount1.add(totCurrAmtBig);

BigDecimal comTCStotalAmt = new BigDecimal(totComplAmt); // Completed
BigDecimal newTCStotalAmt = amount1.add(totCurrAmtBig); // Completed
                                                                                    // and New

String amount = totalAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN) + " INR";

// START : NEW LOGIC DEVELOPED ON 290920
if (amount1.compareTo(new BigDecimal(1000000)) == 1) {  //TCS CHANGES NEED TO MODIFY AS PER BANK REQUEST CHANGES MADE BY PRIYANKA YADAV
      if (currPurCode.equals("S0301") || currPurCode.equals("S0303") || currPurCode.equals("S0304")
                  || currPurCode.equals("S0306")) {
            currIntAmtBig = totCurrAmtBig.multiply(new BigDecimal(0.05));
            currIntAmt = currIntAmtBig.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";
      } else if ((currPurCode.equals("S1107") || currPurCode.equals("S0305"))
                  && srcOfFund.trim().equals("Y")) {
            currIntAmtBig = totCurrAmtBig.multiply(new BigDecimal(0.005));
            currIntAmt = currIntAmtBig.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";
      } else {
            currIntAmtBig = totCurrAmtBig.multiply(new BigDecimal(0.05));
            currIntAmt = currIntAmtBig.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";
      }
} else {
      if (currPurCode.equals("S0301") || currPurCode.equals("S0303") || currPurCode.equals("S0304")
                  || currPurCode.equals("S0306")) {
            currIntAmtBig = totCurrAmtBig.multiply(new BigDecimal(0.05));
            currIntAmt = currIntAmtBig.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";
      } else {
            if (totalAmt.compareTo(new BigDecimal(1000000)) == 1) {
                  totalAmt = totalAmt.subtract(new BigDecimal(1000000));
                  if ((currPurCode.equals("S1107") || currPurCode.equals("S0305"))
                              && srcOfFund.trim().equals("Y")) {
                        currIntAmtBig = totalAmt.multiply(new BigDecimal(0.005));
                        currIntAmt = currIntAmtBig.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";
                  } else {
                        currIntAmtBig = totalAmt.multiply(new BigDecimal(0.05));
                        currIntAmt = currIntAmtBig.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";
                  }

            }
      }
}
// END

// System.out.println("currPurCode--->" + currPurCode);
// if (currPurCode.equals("S0301")|| currPurCode.equals("S0303")
// || currPurCode.equals("S0304") || currPurCode.equals("S0306")) {
// // FOR TAKING 5% CURRENT INTEREST AMOUNT
// System.out.println("totalAmt--->" + totalAmt);
// // currIntAmtBig = totalAmt.multiply(new BigDecimal(0.05));
// // currIntAmtBig = currIntAmtBig.subtract(totIntAmtBig);
// // System.out.println("totIntAmtBig--->" + totIntAmtBig);
// // currIntAmt = currIntAmtBig.setScale(2,
// // BigDecimal.ROUND_HALF_EVEN) + " INR";
// currIntAmtBig = totCurrAmtBig.multiply(new BigDecimal(0.05));
// currIntAmt = currIntAmtBig.setScale(2,
// BigDecimal.ROUND_HALF_EVEN) + " INR";
// System.out.println("currIntAmt--->" + currIntAmt);
// }else{
// // if (!currPurCode.equals("S0306")&&!currPurCode.equals("S0306")
// // &&!currPurCode.equals("S0306")&&!currPurCode.equals("S0306")
// // &&!currPurCode.equals("S0306")) {
// if (totalAmt.compareTo(new BigDecimal(700000)) == 1) {
// // if (!currPurCode.equals("S0306"))
// totalAmt = totalAmt.subtract(new BigDecimal(700000));
// System.out.println("totalAmt--->" + totalAmt);
// if ((currPurCode.equals("S1107") || currPurCode
// .equals("S0305")) && srcOfFund.trim().equals("Y"))
// // FOR TAKING 0.5% CURRENT INTEREST AMOUNT
// currIntAmtBig = totalAmt
// .multiply(new BigDecimal(0.005));
// else
// // FOR TAKING 5% CURRENT INTEREST AMOUNT
// currIntAmtBig = totalAmt.multiply(new BigDecimal(0.05));
// currIntAmtBig = currIntAmtBig.subtract(totIntAmtBig);
// System.out.println("totIntAmtBig--->" + totIntAmtBig);
// currIntAmt = currIntAmtBig.setScale(2,
// BigDecimal.ROUND_HALF_EVEN) + " INR";
// }
// }

// FOR TAKING 5% INTEREST CUMULATIVE AMOUNT
// commented the logic as per the email from Mr.Dilip Parmar on 09/07/2021 by
// Arunkumar D

//totIntAmtCumulaBig = currIntAmtBig.add(totIntAmtBig);
//System.out.println("totIntAmtCumulaBig--->" + totIntAmtCumulaBig);
//totIntAmtCumula = totIntAmtCumulaBig.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";

getPane().setLRSCPAMT(totComplAmt + " INR");
BigDecimal tmp = new BigDecimal(totIntAmt);
totIntAmt = tmp.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";
getPane().setLRSINAMT(totIntAmt);//
getPane().setLRSCRAMT(totCurrAmt);
getPane().setLRSCMAMT(amount);

// commented the logic as per the email from Mr.Dilip Parmar on 09/07/2021 by
// Arunkumar D
//getPane().setLRSCRINT(currIntAmt);//
//getPane().setLRSINTCM(totIntAmtCumula);//

// TCS LRS calculation and logics are added by Arunkumar D
// on 23/06/2021
try {
//    PreparedStatement ps = null;
//    ResultSet rs = null;
//    Connection con = null;

      String LRSTCSYN = getPane().getLRSTCSYN();

      BigDecimal hundred = new BigDecimal("100");

      System.out.println("totalAmt from the query===>" + totalAmt);

      System.out.println("totCurrAmtBig=========>" + totCurrAmtBig);

      System.out.println("newTCStotalAmt===>" + newTCStotalAmt);
      System.out.println("comTCStotalAmt===>" + comTCStotalAmt);
      BigDecimal newtotalAmt = new BigDecimal("0");

      getPane().setTCSRATE("");
      getWrapper().setTAXAMNT(0 + "" + "INR");
      getPane().setTAXAMNT(0 + "" + "INR");

      String SP206AB = getDriverWrapper().getEventFieldAsText("cAWE", "s", "");// Specified Person u/s 206AB
      BigDecimal totalVal = BigDecimal.ZERO;
      String finval = ""; // 206CCA

      // 1st condition
      if ((currPurCode.equals("S0301") || currPurCode.equals("S0303") || currPurCode.equals("S0304")
                  || currPurCode.equals("S0306")) && SP206AB.equalsIgnoreCase("NO")) {
            BigDecimal five = new BigDecimal("5");
            BigDecimal rem_amount = totCurrAmtBig.multiply(five);
            totalVal = rem_amount.divide(hundred);

            finval = totalVal.setScale(0, BigDecimal.ROUND_HALF_EVEN) + "INR";
            System.out.println("1st finvalue" + finval);
            getPane().setTCSRATE("5");
            getPane().setTAXAMNT(finval);
            getWrapper().setTAXAMNT(finval);
            // getWrapper().setTAXAMNTCurrency("INR");

      }
      // 2nd condition
      else if ((currPurCode.equals("S0301") || currPurCode.equals("S0303") || currPurCode.equals("S0304")
                  || currPurCode.equals("S0306")) && SP206AB.equalsIgnoreCase("YES")) {

            BigDecimal ten = new BigDecimal("10");
            BigDecimal rem_amount = totCurrAmtBig.multiply(ten);
            totalVal = rem_amount.divide(hundred);
            finval = totalVal.setScale(0, BigDecimal.ROUND_HALF_EVEN) + "INR";
            System.out.println("2nd finvalue" + finval);
            getPane().setTCSRATE("10");
            getPane().setTAXAMNT(finval);
            getWrapper().setTAXAMNT(finval);

      }

      if ((comTCStotalAmt.compareTo(new BigDecimal(1000000)) == 1)
                  || (currPurCode.equals("S0301") || currPurCode.equals("S0303") || currPurCode.equals("S0304")
                              || currPurCode.equals("S0306") && (SP206AB.equalsIgnoreCase("YES")))
                  || ((currPurCode.equals("S0301") || currPurCode.equals("S0303") || currPurCode.equals("S0304")
                              || currPurCode.equals("S0306")) && SP206AB.equalsIgnoreCase("YES"))) {
            newtotalAmt = totCurrAmtBig;// already completed amount--
                                                      // tax already calculated
            System.out.println("comTCStotalAmt if loop=1==>" + newtotalAmt);

      }

      else if ((comTCStotalAmt.compareTo(new BigDecimal(1000000)) == 1)
                  || (currPurCode.equals("S0301") || currPurCode.equals("S0303") || currPurCode.equals("S0304")
                              || currPurCode.equals("S0306") && (SP206AB.equalsIgnoreCase("YES")))
                  || ((currPurCode.equals("S0301") || currPurCode.equals("S0303") || currPurCode.equals("S0304")
                              || currPurCode.equals("S0306")) && SP206AB.equalsIgnoreCase("NO"))) {
            newtotalAmt = totCurrAmtBig;// already completed amount--
                                                      // tax already calculated
            System.out.println("newtotalAmt else if loop=2==>" + newtotalAmt);

      } else if (newTCStotalAmt.compareTo(new BigDecimal(1000000)) == 1) {
            newtotalAmt = newTCStotalAmt.subtract(new BigDecimal(1000000));// amount exceeds
                                                                                                            // 7 lakh with
                                                                                                            // current
                                                                                                            // tnx and
            System.out.println("newtotalAmt else if loop=3==>" + newtotalAmt);

      }
      // need to subtract and calculate tax

      // FO Balance amount

      String setValue = newtotalAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN) + "INR";
      System.out.println("setValue=====>" + setValue);
      getWrapper().setAMTPAID(setValue);
      getPane().setAMTPAID(setValue);
      // getWrapper().setAMTPAIDCurrency("INR");

      // 3rd condition
      if ((currPurCode.equals("S0001") || currPurCode.equals("S0002") || currPurCode.equals("S0003")
                  || currPurCode.equals("S0004") || currPurCode.equals("S0005") || currPurCode.equals("S0011")
                  || currPurCode.equals("S0021") || currPurCode.equals("S0022") || currPurCode.equals("S0023")
                  || currPurCode.equals("S0603") || currPurCode.equals("S1108") || currPurCode.equals("S1301")
                  || currPurCode.equals("S1302") || currPurCode.equals("S1303") || currPurCode.equals("S1307"))
                  && SP206AB.equalsIgnoreCase("NO")) {
            BigDecimal fivep = new BigDecimal("5");
            BigDecimal rem_amount = newtotalAmt.multiply(fivep);
            totalVal = rem_amount.divide(hundred);
            finval = totalVal.setScale(0, BigDecimal.ROUND_HALF_EVEN) + "INR";
            System.out.println("3rd finvalue" + finval);
            getPane().setTCSRATE("5");
            getPane().setTAXAMNT(finval);
            getWrapper().setTAXAMNT(finval);
      }
      // 4th condition
      else if ((currPurCode.equals("S0001") || currPurCode.equals("S0002") || currPurCode.equals("S0003")
                  || currPurCode.equals("S0004") || currPurCode.equals("S0005") || currPurCode.equals("S0011")
                  || currPurCode.equals("S0021") || currPurCode.equals("S0022") || currPurCode.equals("S0023")
                  || currPurCode.equals("S0603") || currPurCode.equals("S1108") || currPurCode.equals("S1301")
                  || currPurCode.equals("S1302") || currPurCode.equals("S1303") || currPurCode.equals("S1307"))
                  && SP206AB.equalsIgnoreCase("YES")) {
            BigDecimal five = new BigDecimal("10");
            BigDecimal rem_amount = newtotalAmt.multiply(five);
            totalVal = rem_amount.divide(hundred);
            finval = totalVal.setScale(0, BigDecimal.ROUND_HALF_EVEN) + "INR";
            System.out.println("4th finvalue" + finval);
            getPane().setTCSRATE("10");
            getPane().setTAXAMNT(finval);
            getWrapper().setTAXAMNT(finval);
      }
      // 5th condition
      else if ((currPurCode.equals("S1107") || currPurCode.equals("S0305")) && LRSTCSYN.equalsIgnoreCase("Y")
                  && SP206AB.equalsIgnoreCase("NO")) {
            BigDecimal fivep = new BigDecimal("0.5");
            BigDecimal rem_amount = newtotalAmt.multiply(fivep);
            totalVal = rem_amount.divide(hundred);
            finval = totalVal.setScale(0, BigDecimal.ROUND_HALF_EVEN) + "INR";
            System.out.println("5th finvalue" + finval);
            getPane().setTCSRATE("0.5");
            getPane().setTAXAMNT(finval);
            getWrapper().setTAXAMNT(finval);
      }
      // 6th condition
      else if ((currPurCode.equals("S1107") || currPurCode.equals("S0305")) && LRSTCSYN.equalsIgnoreCase("N")
                  && SP206AB.equalsIgnoreCase("NO")) {
            BigDecimal five = new BigDecimal("5");
            BigDecimal rem_amount = newtotalAmt.multiply(five);
            totalVal = rem_amount.divide(hundred);
            finval = totalVal.setScale(0, BigDecimal.ROUND_HALF_EVEN) + "INR";
            System.out.println("6th finvalue" + finval);
            getPane().setTCSRATE("5");
            getPane().setTAXAMNT(finval);
            getWrapper().setTAXAMNT(finval);
      }
      // 7th condition
      else if ((currPurCode.equals("S1107") || currPurCode.equals("S0305")) && LRSTCSYN.equalsIgnoreCase("Y")
                  && SP206AB.equalsIgnoreCase("YES")) {
            BigDecimal ten = new BigDecimal("5");
            BigDecimal rem_amount = newtotalAmt.multiply(ten);
            totalVal = rem_amount.divide(hundred);
            finval = totalVal.setScale(0, BigDecimal.ROUND_HALF_EVEN) + "INR";
            System.out.println("7th finvalue" + finval);
            getPane().setTCSRATE("5");
            getPane().setTAXAMNT(finval);
            getWrapper().setTAXAMNT(finval);
      }
      // 8th condition
      else if ((currPurCode.equals("S1107") || currPurCode.equals("S0305")) && LRSTCSYN.equalsIgnoreCase("N")
                  && SP206AB.equalsIgnoreCase("YES")) {
            BigDecimal five = new BigDecimal("10");
            BigDecimal rem_amount = newtotalAmt.multiply(five);
            totalVal = rem_amount.divide(hundred);
            finval = totalVal.setScale(0, BigDecimal.ROUND_HALF_EVEN) + "INR";
            System.out.println("8th finvalue" + finval);
            getPane().setTCSRATE("10");
            getPane().setTAXAMNT(finval);
            getWrapper().setTAXAMNT(finval);
      }

      // Add the logic as per the email from Mr.Dilip Parmar on 09/07/2021 by
      // Arunkumar D
      totIntAmtCumulaBig = totalVal.add(totIntAmtBig);
      System.out.println("totIntAmtCumulaBig after new change====>" + totIntAmtCumulaBig);
      totIntAmtCumula = totIntAmtCumulaBig.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " INR";
      System.out.println("finval after new change====>" + finval);
      getPane().setLRSCRINT(finval);//
      getPane().setLRSINTCM(totIntAmtCumula);//

} catch (Exception e) {
      e.printStackTrace();
      System.out.println("TCS LRS exception ====>" + e.getMessage());
}

} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(conn, ps3, rs3);
}

}

public void TCSInProgressValidation(String masRef, String eveRef, ValidationDetails validationDetails) {
Connection conn = null;
PreparedStatement pst = null;
ResultSet rs = null;
String query = "";
String masReferences = "";
String panCard = getPane().getPANDETAI();
String custId = getDriverWrapper().getEventFieldAsText("RMIT", "p", "cu").trim();
Loggers.general().info(LOG, "TCSInProgressValidation (Logger)");
System.out.println("TCSInProgressValidation");
try {
query = "SELECT LISTAGG(TRIM(MAS.MASTER_REF),',') WITHIN GROUP (ORDER BY MAS.MASTER_REF) AS MAS_REF "
            + " FROM MASTER MAS,BASEEVENT BEV,EXTEVENT EXT,EXEMPL30 PROD,"
            + " TIDATAITEM TID,PARTYDTLS PRD,PRODTYPE SPROD " + " WHERE MAS.KEY97      = BEV.MASTER_KEY  "
            + " AND BEV.KEY97        = EXT.EVENT " + " AND MAS.EXEMPLAR     = PROD.KEY97 "
            + " AND MAS.KEY97      = TID.MASTER_KEY " + " AND TID.KEY97      = PRD.KEY97 "
            + " AND MAS.PRODTYPE      = SPROD.KEY97 " + " AND PROD.CODE79     IN ('CPCO','CPHO') "
            + " AND BEV.REFNO_PFIX   = 'CRE' " + " AND SPROD.CODE IN ('65R','64R') "
            + " AND BEV.STATUS       = 'i' " + " AND PRD.ROLE       = 'RFP' " + " AND PRD.CUS_MNM    = '"
            + custId + "' " + " AND EXT.PANDETAI      = '" + panCard + "' "
            + " AND EXT.OPURPOS    IN ('S0001', 'S0002', 'S0003', 'S0004', 'S0005', "
            + " 'S0021', 'S0022', 'S0023', 'S0301','S0303', 'S0304', "
            + " 'S0305', 'S0306', 'S1107', 'S1108','S1301', 'S1302'," + " 'S1303', 'S1307', 'S0011', 'S0603') "
            + " AND MAS.MASTER_REF <> '<Unallocated>' " + " AND (MAS.MASTER_REF <> '" + masRef + "' "
            + " OR BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) <> '" + eveRef + "')";
conn = getConnection();
pst = conn.prepareStatement(query);
rs = pst.executeQuery();
while (rs.next()) {
      masReferences = rs.getString(1);
}
if (masReferences != null && !masReferences.trim().equals("")) {
      validationDetails.addWarning(WarningType.Other,
                  "Already Events are work In progress in [" + masReferences + "] for this Pan No.[CE]");
}
} catch (Exception e) {
//e.printStackTrace();
} finally {
surrenderDB(conn, pst, rs);
}
}//// TCS of LRS Transaction 01-10-21 Ends

//Net Payable amount Population 12-11-2021

public void getNetPayableAmt() {
try {
System.out.println("getNetPayableAmt");
DecimalFormat df = new DecimalFormat("##,##,##0.##");

String amtPaidStr = getDriverWrapper().getEventFieldAsText("AMP", "v", "m");
String amtPaidCcy = getDriverWrapper().getEventFieldAsText("AMP", "v", "c");

String discAmtStr = getDriverWrapper().getEventFieldAsText("cBCH", "v", "m");
String discAmtStrCcy = getDriverWrapper().getEventFieldAsText("cBCH", "v", "c");
if ((amtPaidStr != null && !amtPaidStr.trim().isEmpty())
            && (discAmtStr != null && !discAmtStr.trim().isEmpty() && !discAmtStr.trim().equals(""))) {
      System.out.println("DISCOUNT AMOUNT" + discAmtStr + " " + amtPaidStr);
      BigDecimal amtPaid = new BigDecimal(amtPaidStr);
      BigDecimal discAmt = new BigDecimal(discAmtStr);
      BigDecimal netAmt = new BigDecimal(0);
      getPane().setNTPAYAMT("");
      if (amtPaid.compareTo(discAmt) == 1 && amtPaidCcy.trim().equals(discAmtStrCcy)) {
            System.out.println("Net Pay Amount Population");
            netAmt = amtPaid.subtract(discAmt);
            String value1 = df.format(netAmt) + " " + discAmtStrCcy;
            getPane().setNTPAYAMT(value1);
      }
}
} catch (Exception e) {
e.printStackTrace();
}
}

public void getDateDiff(ValidationDetails validationDetails) {
System.out.println("In Date Diff");
DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String shipDate = getDriverWrapper().getEventFieldAsText("cAJT", "d", "");
String payDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
String dueDate = getDriverWrapper().getEventFieldAsText("FPD:sMD", "d", "");

if (shipDate != null && !shipDate.trim().equals("") && payDate != null && !payDate.trim().equals("")
      && dueDate != null && !dueDate.trim().equals("")) {
LocalDate date1 = LocalDate.parse(shipDate, formatters);
LocalDate date2 = LocalDate.parse(payDate, formatters);
LocalDate date3 = LocalDate.parse(dueDate, formatters);
long compare = date2.compareTo(date1);
long compare2 = date2.compareTo(date3);
System.out.println("Date Compare:" + compare + " " + compare2);
if (compare >= 0) {
      long no = ChronoUnit.DAYS.between(date1, date2);
      getPane().setDIFSHPMT(String.valueOf(no));
} else if (compare < 0) {
      validationDetails.addWarning(WarningType.Other, "Payment before due date");
}
if (compare2 >= 0) {
      long no = ChronoUnit.DAYS.between(date3, date2);
      System.out.println("Date DIFFRENCE: " + no);
      getPane().setDFDUEDAT(String.valueOf(no));
}
}
}

public void getWarningBOE(ValidationDetails validationDetails) {
//try {
//System.out.println("getWarningBOE");
//con = getConnection();
//String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
//String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
//String prodType = getDriverWrapper().getEventFieldAsText("SLN", "s", "").trim();
//System.out.println("BOE_Warning: "+masterref+", "+eventREF+", "+prodType);
//if(prodType == "Direct Coll Foreign" || prodType.trim().equals("Direct Coll Foreign") || prodType == "Coll Usance Domestic" || prodType.trim().equals("Coll Usance Domestic") || prodType == "Coll Usance Foreign" || prodType.trim().equals("Coll Usance Foreign")) {
//    
//String query = "SELECT TRIM(EXTB.BOENUM) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTBOE EXTB" +
//    " WHERE MAS.KEY97 = BEV.MASTER_KEY" +
//    " AND BEV.KEY97 = EXT.EVENT" +
//    " AND EXTB.FK_EVENT = EXT.KEY29" +
//    " AND MAS.MASTER_REF = '"+masterref+"'" +
//    " AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='"+eventREF+"'";
//System.out.println(query);
//ps1 = con.prepareStatement(query);
//rs1 = ps1.executeQuery();
//if (!rs1.next()) {
//validationDetails.addWarning(WarningType.Other,"BOE has not entered");
//}
//else {
//System.out.println("BOE has data");
//}
//}
//} catch (Exception e) {
////e.printStackTrace();
//}
try {
System.out.println("getWarningBOE");
con = getConnection();
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
String prodType = getDriverWrapper().getEventFieldAsText("SLN", "s", "").trim();
System.out.println("BOE_Warning: " + masterref + ", " + eventREF + ", " + prodType);
if (prodType == "Direct Coll Foreign" || prodType.trim().equals("Direct Coll Domestic")
            || prodType == "Coll Usance Domestic" || prodType.trim().equals("Usance Doc LC-Foreign")
            || prodType == "Coll Usance Foreign" || prodType.trim().equals("Usance Clean LC-Foreign")
            || prodType.trim().equals("Usance Merchant LC-Foreign")
            || prodType.trim().equals("Usance Physical LC-Foreign")
            || prodType.trim().equals("Usance Doc LC-Domestic")
            || prodType.trim().equals("Usance Clean LC-Domestic")
            || prodType.trim().equals("Usance Merchant LC-Domestic")
            || prodType.trim().equals("Usance Physical LC-Domestic")) {

      /*
       * String query =
       * "SELECT TRIM(EXTB.BOENUM) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTBOE EXTB"
       * + " WHERE MAS.KEY97 = BEV.MASTER_KEY" + " AND BEV.KEY97 = EXT.EVENT" +
       * " AND EXTB.FK_EVENT = EXT.KEY29" + " AND MAS.MASTER_REF = '"+masterref+"'" +
       * " AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='"+eventREF+"'";
       * System.out.println(query); ps1 = con.prepareStatement(query); rs1 =
       * ps1.executeQuery();
       */
      List<ExtEventBOECAP> shipTable = (List<ExtEventBOECAP>) getWrapper().getExtEventBOECAP();
      // System.out.println("inside invoice data" );
      // int size=shipTable.size();
      System.out.println("BOE data size" + shipTable.size());
      if (shipTable.size() == 0) {
            validationDetails.addWarning(WarningType.Other, "BOE has not entered");
      } else {
            System.out.println("BOE has data");
      }
}
} catch (Exception e) {
e.printStackTrace();
} finally {
surrenderDB(con, ps1, rs1);
}
}

// Purpose Code starts with S for IDC
public void getErrorPurposeCode(ValidationDetails validationDetails) {
try {
String purCode = getDriverWrapper().getEventFieldAsText("cAQL", "s", "");
char firstLetter = purCode.charAt(0);
if ((getMajorCode().equalsIgnoreCase("IDC") || getMajorCode().equalsIgnoreCase("ILC")
            || getMajorCode().equalsIgnoreCase("CPCO") || getMajorCode().equalsIgnoreCase("CPBO"))
            && (getMinorCode().equalsIgnoreCase("CRE") || getMinorCode().equalsIgnoreCase("CLP")
                        || getMinorCode().equalsIgnoreCase("ISI") || getMinorCode().equalsIgnoreCase("POC")
                        || getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))) {

      System.out.println("Purpose Code: " + purCode + ", and first letter: " + firstLetter);
      if (!Character.toString(firstLetter).equalsIgnoreCase("S")) {
            validationDetails.addError(ErrorType.Other, "The purpose code should start with 'S'");
      }
}
if ((getMajorCode().equalsIgnoreCase("CPCI") || getMajorCode().equalsIgnoreCase("CPBI"))
            && (getMinorCode().equalsIgnoreCase("PCIC") || getMinorCode().equalsIgnoreCase("PBIC"))) {
      System.out.println("Purpose Code: " + purCode + ", and first letter: " + firstLetter);
      if (!Character.toString(firstLetter).equalsIgnoreCase("P")) {
            validationDetails.addError(ErrorType.Other, "The purpose code should start with 'P'");
      }
}
} catch (Exception e) {
e.printStackTrace();
}

}

// Invoice grid warning by Vishal G
public void getWarningData(ValidationDetails validationDetails) {
/*
* try { System.out.println("getWarningdata"); con = getConnection(); String
* masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
* String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r",
* "").trim(); // String prodType =
* getDriverWrapper().getEventFieldAsText("SLN", "s", "").trim();
* System.out.println("BOE_Warning: "+masterref+", "+eventREF);
*
*
* String query =
* "SELECT TRIM(EXTN.INVNUMB),TRIM(EXTN.INDATE),TRIM(EXTN.INAMOUT) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTIND EXTN"
* + " WHERE MAS.KEY97 = BEV.MASTER_KEY" + " AND BEV.KEY97 = EXT.EVENT" +
* " AND EXTN.FK_EVENT = EXT.KEY29" + " AND MAS.MASTER_REF = '"+masterref+"'" +
* " AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='"+eventREF+"'";
* System.out.println(query); ps1 = con.prepareStatement(query); rs1 =
* ps1.executeQuery(); if (!rs1.next()) {
* validationDetails.addWarning(WarningType.
* Other," Invoice Details should be entered."); } else {
* System.out.println("Invoice has data"); }
*
* } catch (Exception e) { e.printStackTrace(); }
*/
try {

// String finAmt = getDriverWrapper().getEventFieldAsText("B+FD", "v", "m");
// String financeParty = getDriverWrapper().getEventFieldAsText("B+FT", "p",
// "cu").trim();

List<ExtEventInvoiceData> shipTable = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();
// System.out.println("inside invoice data" );
// int size=shipTable.size();
System.out.println("invoice data size" + shipTable.size());

if (shipTable.size() == 0) {

      validationDetails.addWarning(WarningType.Other, " Invoice Details should be entered.");
      System.out.println("inside invoice warning:");
}
// else {
// System.out.println("Invoice has data");
//}

// }

}

catch (Exception e) {
e.printStackTrace();
System.out.println("outside invoice data:" + e);

}

finally {
surrenderDB(con, ps1, rs1);
}
}

public void getRtgsNeftAmt() {
try {
getPane().setRTGSNEFT("");
System.out.println("getRtgsNeftAmt");
String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
System.out.println("Prodcode " + subproCode);
if ((getMajorCode().equalsIgnoreCase("IDC")) && (getMinorCode().equalsIgnoreCase("CLP"))) {
      if (subproCode.equalsIgnoreCase("31D") || subproCode.equalsIgnoreCase("32D")
                  || subproCode.equalsIgnoreCase("33D") || subproCode.equalsIgnoreCase("34D")) {
            System.out.println("IN idc IF of getRtgsNeftAmt");
            String collAmt = getDriverWrapper().getEventFieldAsText("ORA", "v", "m");
            String collAmtCurr = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
            getPane().setRTGSNEFT(collAmt + " " + collAmtCurr);
      }
} else if ((getMajorCode().equalsIgnoreCase("ILC")) && (getMinorCode().equalsIgnoreCase("POC"))) {
      if (subproCode.equalsIgnoreCase("01D") || subproCode.equalsIgnoreCase("02D")
                  || subproCode.equalsIgnoreCase("03D") || subproCode.equalsIgnoreCase("04D")
                  || subproCode.equalsIgnoreCase("11D") || subproCode.equalsIgnoreCase("12D")
                  || subproCode.equalsIgnoreCase("13D") || subproCode.equalsIgnoreCase("14D")
                  || subproCode.equalsIgnoreCase("21D")) {
            System.out.println("IN ilc IF of getRtgsNeftAmt");
            String collAmt = getDriverWrapper().getEventFieldAsText("FPP:XPAM", "v", "m");
            String collAmtCurr = getDriverWrapper().getEventFieldAsText("FPP:XPAM", "v", "c");
            if ((collAmt != null && !collAmt.isEmpty()) && (collAmtCurr != null && !collAmtCurr.isEmpty())) {
                  getPane().setRTGSNEFT(collAmt + " " + collAmtCurr);
            }
      }
}

} catch (Exception e) {

}

}

public void getTotalPayAmt() {
System.out.println("getTotalPayableAmt");
DecimalFormat df = new DecimalFormat("##,##,##0.##");

String amtCollectStr = getDriverWrapper().getEventFieldAsText("AMC", "v", "m");
String amtCollectCcy = getDriverWrapper().getEventFieldAsText("AMC", "v", "c");

String advAmtStr = getDriverWrapper().getEventFieldAsText("cBMC", "v", "m");
String advAmtStrCcy = getDriverWrapper().getEventFieldAsText("cBMC", "v", "c");
if ((advAmtStr != null && !advAmtStr.isEmpty()) || (amtCollectStr != null && !amtCollectStr.isEmpty())) {
BigDecimal amtCollect = new BigDecimal(amtCollectStr);
BigDecimal advAmt = new BigDecimal(advAmtStr);
BigDecimal netPayAmt = new BigDecimal(0);
getPane().setNTPAYAMT("");
if (amtCollect.compareTo(advAmt) == 1 && amtCollectCcy.trim().equals(advAmtStrCcy)) {
      System.out.println("TOTAL Pay Amount Population");
      netPayAmt = amtCollect.subtract(advAmt);
      String value1 = df.format(netPayAmt) + " " + advAmtStrCcy;
      getPane().setNTPAYAMT(value1);
}
}
}

// CommitEndDate and Notional Duedate done by vishal G
public void getCommitEndDate() {
try {
//if((getMajorCode().equalsIgnoreCase("ILC")) && (getMinorCode().equalsIgnoreCase("NAMI"))) {
//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//Calendar c = Calendar.getInstance();
//DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//String expDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
////    String amdDate = getDriverWrapper().getEventFieldAsText("AMD", "d", "");
//String issDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
//String tenorPeriod = getDriverWrapper().getEventFieldAsText("cBLM", "s", "");
//System.out.println("getCommitEndDate: "+expDate+","+","+issDate);
//LocalDate exp = LocalDate.parse(expDate,formatters);
//LocalDate iss = LocalDate.parse(issDate,formatters);
//long l = ChronoUnit.DAYS.between(iss, exp);
//int t =Integer.parseInt(tenorPeriod);
//System.out.println("getCommitEndDate iss&exp date diff: "+l);
//if(l < 90) {
//    c.setTime(sdf.parse(issDate));
//    c.add(Calendar.DATE, 90);
//    getPane().setCOMENDDT(sdf.format(c.getTime()));
//}
//else {
//    getPane().setCOMENDDT("");
//}
//if(t <=90 ) {
//    c.setTime(sdf.parse(issDate));
//    c.add(Calendar.DATE, 90);
//    getPane().setUSENDDAT(sdf.format(c.getTime()));
//}
//else {
//    getPane().setUSENDDAT("");
//}
//
//}
if ((getMajorCode().equalsIgnoreCase("OCC")) && (getMinorCode().equalsIgnoreCase("CLP"))) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      Calendar c = Calendar.getInstance();
      String rvdDate = getDriverWrapper().getEventFieldAsText("RVD", "d", "");
      String tenorPeriod = getDriverWrapper().getEventFieldAsText("cAEP", "s", "");
      System.out.println("getNotionalDueDate: " + rvdDate);
      if (tenorPeriod.trim() != null && !tenorPeriod.trim().equalsIgnoreCase("") && !tenorPeriod.isEmpty()) {
            int t = Integer.parseInt(tenorPeriod);
            System.out.println("tenorPeriod" + t);
            if (t > 0) {
                  c.setTime(sdf.parse(rvdDate));
                  c.add(Calendar.DATE, t);
                  System.out.println("inside notional IF: " + sdf.format(c.getTime()));
                  getPane().setSIGVALDT(sdf.format(c.getTime()));

            }
      }
}
} catch (Exception e) {
e.printStackTrace();
}
}

//To calculate diffrence between Issue date and expiry date && Issue Date tenor days addition  
public void getTenordays() {
try {
if ((getMajorCode().equalsIgnoreCase("ILC"))
            && (getMinorCode().equalsIgnoreCase("ISI") || getMinorCode().equalsIgnoreCase("NAMI"))) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      Calendar c = Calendar.getInstance();
      getPane().setCHRGBADT("");
      getPane().setEXPIREDT("");
      DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      String expDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
      // String amdDate = getDriverWrapper().getEventFieldAsText("AMD", "d", "");
      String issDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");

      System.out.println("getCHARGEBASISEndDate: " + expDate + "," + "," + issDate);
      LocalDate exp = LocalDate.parse(expDate, formatters);
      LocalDate iss = LocalDate.parse(issDate, formatters);
      long l = ChronoUnit.DAYS.between(iss, exp);
      int ln = (int) l;
      // int t = Integer.parseInt(tenorPeriod);
      System.out.println("getCHARGEBASISEndDate iss&exp date diff: " + l);
      if (l <= 90) {
            c.setTime(sdf.parse(issDate));
            c.add(Calendar.DATE, 90);
            getPane().setEXPIREDT(sdf.format(c.getTime()));
      } else {
            c.setTime(sdf.parse(issDate));
            c.add(Calendar.DATE, ln); // change t to l
            getPane().setEXPIREDT(sdf.format(c.getTime()));
      }
      String tenorPeriod = getDriverWrapper().getEventFieldAsText("TNRD", "i", "").trim();
      String mixtenorPeriod = getDriverWrapper().getEventFieldAsText("cBLM", "s", "").trim();
      if (tenorPeriod.trim() != null && !tenorPeriod.trim().equalsIgnoreCase("") && !tenorPeriod.isEmpty()) {
            int t = Integer.parseInt(tenorPeriod);
            if (t <= 90) {
                  c.setTime(sdf.parse(issDate));
                  c.add(Calendar.DATE, 90);
                  getPane().setCHRGBADT(sdf.format(c.getTime()));
            } else {
                  c.setTime(sdf.parse(issDate));
                  c.add(Calendar.DATE, t);
                  getPane().setCHRGBADT(sdf.format(c.getTime()));
            }
      } else if (mixtenorPeriod != null && !mixtenorPeriod.equalsIgnoreCase("")) {
            int t2 = Integer.parseInt(mixtenorPeriod);
            System.out.println("mixed tenor: " + mixtenorPeriod + " " + t2);
            if (t2 <= 90) {
                  c.setTime(sdf.parse(issDate));
                  c.add(Calendar.DATE, 90);
                  getPane().setCHRGBADT(sdf.format(c.getTime()));
            } else {
                  c.setTime(sdf.parse(issDate));
                  c.add(Calendar.DATE, t2);
                  getPane().setCHRGBADT(sdf.format(c.getTime()));
            }
      }

}
} catch (Exception e) {
e.printStackTrace();
}

}

public static String getTagValue(String xml, String tagName) {
String extractValue = null;
try {
extractValue = xml.split("<" + tagName + ">")[1].split("</" + tagName + ">")[0];

} catch (Exception e) {
}

if (extractValue == null || extractValue.isEmpty()) {
try {
      extractValue = xml.split("&lt;" + tagName + "&gt;")[1].split("&lt;/" + tagName + "&gt;")[0];
} catch (Exception e) {
}
}

if (extractValue == null || extractValue.isEmpty()) {
return "";
} else {
return extractValue;
}
}

public void getNostroDetails() {
try {
if ((getMajorCode().equalsIgnoreCase("ODC") || getMajorCode().equalsIgnoreCase("ELC")
            || getMajorCode().equalsIgnoreCase("IGT"))
            && (getMinorCode().equalsIgnoreCase("CRE") || getMinorCode().equalsIgnoreCase("DOP")
                        || getMinorCode().equalsIgnoreCase("IIG"))) {
      String bicSwift = getDriverWrapper().getEventFieldAsText("cBRT", "s", "").trim();
      if ((!bicSwift.equalsIgnoreCase("")) && bicSwift != null) {
            try {
                  String portvalqury = "select trim(BICSWIFT),trim(ACCNUM),trim(BNKNM),BNKNMIC,ACCNSTUT,CURRENCY from EXTNOSTRODETAILS WHERE BICSWIFT='"
                              + bicSwift + "'";
                  // //Loggers.general().info(LOG,"port code destination query
                  // Value---->" +
                  // portvalqury);
                  con = ConnectionMaster.getConnection();
                  ps1 = con.prepareStatement(portvalqury);
                  rs = ps1.executeQuery();
                  while (rs.next()) {
                        String accnum = rs.getString(2);
                        String bankName = rs.getString(3);
                        String bankMnimonic = rs.getString(4);
                        String nostroutilization = rs.getString(5);
                        String currency = rs.getString(6);
                        System.out.println("Account number and bank is :" + accnum + ", " + bankName);
                        // //Loggers.general().info(LOG,"port code description---->"
                        // +
                        // accnum);
                        getPane().setNOSTACTN(accnum);
                        getPane().setNOSTBNKN(bankName);
                        getPane().setBNKNMIC(bankMnimonic);
                        getPane().setACCNSTUT(nostroutilization);
                        getPane().setCURRENCY(currency);
                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"exception is " + e.getMessage());
            } finally {
                  try {

                        if (rs != null)
                              rs.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // console");
                        e.printStackTrace();
                  }
            }
      }
}
} catch (Exception e) {
e.printStackTrace();
}
}

public void getPurposeAdv() {
try {
String purAdv = getDriverWrapper().getEventFieldAsText("cBRW", "s", "").trim();
if ((!purAdv.equalsIgnoreCase("")) && purAdv != null) {
      try {
            String portvalqury = "select trim(PURADVCD),trim(DESCRIPT) from EXTPURPOSEADV WHERE PURADVCD='"
                        + purAdv + "'";
            // //Loggers.general().info(LOG,"port code destination query
            // Value---->" +
            // portvalqury);
            con = ConnectionMaster.getConnection();
            ps1 = con.prepareStatement(portvalqury);
            rs = ps1.executeQuery();
            while (rs.next()) {
                  String purposeAdv = rs.getString(1);
                  String descptn = rs.getString(2);
                  System.out.println("Purpose adv and decription is :" + purposeAdv + ", " + descptn);
                  // //Loggers.general().info(LOG,"port code description---->"
                  // +
                  // purposeAdv);

                  getPane().setDESCRIPT(descptn);
            }

      } catch (Exception e) {
            // Loggers.general().info(LOG,"exception is " + e.getMessage());
      } finally {
            try {

                  if (rs != null)
                        rs.close();
                  if (ps1 != null)
                        ps1.close();
                  if (con != null)
                        con.close();

            } catch (SQLException e) {
                  // Loggers.general().info(LOG,"Connection Failed! Check
                  // output
                  // console");
                  e.printStackTrace();
            }
      }
}
} catch (Exception e) {
e.printStackTrace();
}
}

public void getFcctDetails(ValidationDetails validationDetails) {
try {
String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
String fcct_Cal = getDriverWrapper().getEventFieldAsText("FCYT", "v", "m").trim();
String acct = "";
String charge = "";
String wave = "";
String fcctAccount = "";
con = getConnection();

if ((!fcct_Cal.equalsIgnoreCase("")) && fcct_Cal != null) {
      BigDecimal fcctAmt = new BigDecimal(fcct_Cal);
      BigDecimal fcctAmt1 = new BigDecimal(fcct_Cal);
      fcctAmt = fcctAmt.setScale(0, RoundingMode.HALF_UP);
      Loggers.general().info(LOG, "FCCT Amount initial===> " + fcct_Cal);
      LOG.info("FCCT Calculation " + getMajorCode() + " " + getMinorCode() + " " + getmasRefNo() + " "
                  + geteventRefNo());

      if (!fcct_Cal.equalsIgnoreCase("") && fcct_Cal != null && fcct_Cal.length() > 0) {

            String query = "SELECT ETT_FCCT_CALCULATION('" + fcct_Cal + "') FROM DUAL";
            LOG.info("ETT_FCCT_CALCULATION function " + query);

            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                  String accountQuery = "SELECT trim(ACC.BO_ACCTNO) As BO_ACCTNO "
                              + "FROM MASTER MAS, BASEEVENT BEV, RELITEM REL, NETTEDFM NET ,ACCOUNT ACC,GFPF "
                              + "WHERE MAS.KEY97   = BEV.MASTER_KEY AND BEV.KEY97 = REL.EVENT_KEY "
                              + "AND REL.KEY97     = NET.KEY97 " + "AND NET.STTLACCT1 =ACC.ACCT_KEY "
                              + "and ACC_TYPE IN('CA','CAA','CADDA','CAGEN','CCA','CCGEN','CCUTR','ODA','DDAEH','SBA','ODGEN','PCDIS','RPC','FB004','FBA', "
                              + "'FDULC','ODBDNLC','ODBNLCD','RTGS','DL023','SA','SB','BDNLC','CCCTT','CCENT','CCGSL','CDFFP','ODSTF','PCA') "
                              + " AND CURRENCY='INR' " + "AND MAS.MASTER_REF='" + masRefNo + "'"
                              + " AND BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0)='" + eventRef + "'"
                              + " AND  NET.AMOUNT>0 AND ROWNUM=1 ";
                  ps1 = con.prepareStatement(accountQuery);
                  rs1 = ps1.executeQuery();
                  if (rs1.next()) {
                        acct = rs1.getString("BO_ACCTNO").trim();
                        System.out.println(acct);
                  }

                  String fcct_CalCcy = getDriverWrapper().getEventFieldAsText("CHM", "v", "c").trim();
                  String fcct_CalAmt = getDriverWrapper().getEventFieldAsText("CHM", "v", "m").trim();
                  System.out.println(fcct_Cal + " " + fcct_CalAmt);
                  BigDecimal amount = new BigDecimal(fcct_CalAmt);
                  BigDecimal netPayAmt = new BigDecimal(0);
                  netPayAmt = fcctAmt1.divide(amount, 4, RoundingMode.HALF_UP);
                  getPane().setFCCTREFN("1stCrncyVal:" + fcctAmt);
                  getPane().setFCCTREM1("1ST CRNCY: INR 2ND CRNCY: " + fcct_CalCcy);
                  System.out.println(acct + " " + "@" + netPayAmt + " " + fcct_CalAmt);
                  getPane().setFCCTPART(acct + " " + "@" + netPayAmt + " " + fcct_CalAmt);
//    String fcctquery="SELECT bch.action,substr(BCH.SHORT_TEXT,2,4) as SHORT_TEXT from MASTER MAS, BASEEVENT BEV, relitem REL, BASECHARGE BCH "+
//          "WHERE MAS.KEY97=BEV.MASTER_KEY "+
//          "AND bev.key97 = rel.event_key "+
//          "AND rel.key97=bch.key97 "+
//          "AND MAS.MASTER_REF='" + masRefNo + "'"+
//          " AND BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0)='" + eventRef + "'"+
//          " AND substr(BCH.SHORT_TEXT,2,4)='FCCT'";
//  ps2 = con.prepareStatement(fcctquery);
//    rs2 = ps2.executeQuery();
//    if (rs2.next()) {
//        wave = rs2.getString("ACTION").trim();
//        charge=rs2.getString("SHORT_TEXT").trim();
//        System.out.println(charge+" "+wave);
//    }
//    if(charge.equalsIgnoreCase("FCCT") &&!wave.equalsIgnoreCase("W")) {
////        String fcctAccount=getPane().getFCCTPART().trim();
//          String extevent="SELECT exte.FCCTPART FROM master mas, baseevent bev, extevent exte "+
//                "where mas.KEY97 = bev.MASTER_KEY "+
//                "and bev.KEY97 = exte.EVENT "+
//                "and mas.MASTER_REF='"+masRefNo.trim()+"'"+
//                " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+eventRef+"'";
//      System.out.println("FCCTPART query: " + extevent);
//      ps1 = con.prepareStatement(extevent);
//          rs1 = ps1.executeQuery();
//          while (rs1.next()) {
//                fcctAccount = rs1.getString(1).trim();
//          }
//          
//    if ((fcctAccount.substring(0,1).equalsIgnoreCase("@")) || fcctAccount == null) {
//          System.out.println("Fcct val :"+fcctAccount+ " "+fcctAccount.substring(0,1));
//        validationDetails.addWarning(WarningType.Other,
//              "FCCT TRANSACTION Particulars incorrect. The transaction will fail");
//    }
//    }
                  if ((getMajorCode().equalsIgnoreCase("ODC")) || getMajorCode().equalsIgnoreCase("ELC")
                              || getMajorCode().equalsIgnoreCase("OCC") || getMajorCode().equalsIgnoreCase("ESB")
                              || getMajorCode().equalsIgnoreCase("CPCI")
                              || getMajorCode().equalsIgnoreCase("CPBI") && (getMinorCode().equalsIgnoreCase("CLP")
                                          || getMinorCode().equalsIgnoreCase("POD")
                                          || getMinorCode().equalsIgnoreCase("PBIC")
                                          || getMinorCode().equalsIgnoreCase("PCIC")
                                          || getMinorCode().equalsIgnoreCase("DOP")
                                          || getMinorCode().equalsIgnoreCase("OES")
                                          || getMinorCode().equalsIgnoreCase("FEC")
                                          || getMinorCode().equalsIgnoreCase("CRE"))) {

                        getPane().setFCCTREFN("1stCrncyVal:" + fcctAmt);
                        getPane().setFCCTREM1("1ST CRNCY:" + fcct_CalCcy + " " + "2ND CRNCY: INR");
                        System.out.println(acct + " " + "@" + netPayAmt + " " + fcct_CalAmt);
                        getPane().setFCCTPART(acct + " " + "@" + netPayAmt + " " + fcct_CalAmt);

//        if(charge.equalsIgnoreCase("FCCT") &&!wave.equalsIgnoreCase("W")) {
////              String fcctAccount=getPane().getFCCTPART().trim();
//          
//          String extevent="SELECT exte.FCCTPART FROM master mas, baseevent bev, extevent exte "+
//                      "where mas.KEY97 = bev.MASTER_KEY "+
//                      "and bev.KEY97 = exte.EVENT "+
//                      "and mas.MASTER_REF='"+masRefNo.trim()+"'"+
//                      " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+eventRef+"'";
//            System.out.println("FCCTPART query: " + extevent);
//            ps1 = con.prepareStatement(extevent);
//                rs1 = ps1.executeQuery();
//                while (rs1.next()) {
//                      fcctAccount = rs1.getString(1).trim();
//                }
//                
//          if ((fcctAccount.substring(0,1).equalsIgnoreCase("@")) || fcctAccount == null) {
//                System.out.println("Fcct val :"+fcctAccount+ " "+fcctAccount.substring(0,1));
//              validationDetails.addWarning(WarningType.Other,
//                    "FCCT TRANSACTION Particulars incorrect. The transaction will fail");
//          }
//          }
                  }

            } else {
                  getPane().setFCCTREFN("");

            }

      } else {
            getPane().setFCCTREFN("");
      }
}

} catch (Exception e) {
e.printStackTrace();
} finally {
try {

      if (rs1 != null)
            rs1.close();
      if (ps1 != null)
            ps1.close();
      if (rs2 != null)
            rs2.close();
      if (ps2 != null)
            ps2.close();
      if (con != null)
            con.close();

} catch (SQLException e) {

      e.printStackTrace();
}
}
}

// Merchant Trade Validations
public void freezeMttNumber() {
String mttStatus = "";
String merchantTrading = "";
String mttnum = "";
try {
mttStatus = getPane().getMTTSTUS();
mttnum = getPane().getMTTNUM();

merchantTrading = getPane().getMACHTD();
System.out.println("Merchant Trading---> mttnum" + merchantTrading + " " + mttnum);

if (merchantTrading != null && !merchantTrading.equalsIgnoreCase("")
            && merchantTrading.equalsIgnoreCase("YES")) {
      if (mttStatus != null && !mttStatus.equalsIgnoreCase("") && mttStatus.equalsIgnoreCase("N")
                  && mttnum != null && !mttnum.equalsIgnoreCase("")) {
            getPane().getCtlMTTNUM().setEnabled(false);
      }
}

} catch (Exception e) {
e.printStackTrace();
}
}

public void mttFreeze() {
String merchantTrading = getEmptyIfNull(getPane().getMACHTD()).trim();
try {
System.out.println("Merchant Trading ---->" + merchantTrading);
if (merchantTrading != null && !merchantTrading.equalsIgnoreCase("")
            && merchantTrading.equalsIgnoreCase("NO")) {
      getPane().getCtlMTTCLOSE().setEnabled(false);
      getPane().setMTTCLOSE(false);
      getPane().getCtlMTTNUM().setEnabled(false);
      getPane().setMTTNUM("");
      getPane().getCtlMTTSTUS().setEnabled(false);
      getPane().setMTTSTUS("");
      getPane().getCtlMTTREMAR().setEnabled(false);
      getPane().setMTTREMAR("");

} else {
      getPane().getCtlMTTCLOSE().setEnabled(true);
      getPane().getCtlMTTNUM().setEnabled(true);
      getPane().getCtlMTTSTUS().setEnabled(true);
      getPane().getCtlMTTREMAR().setEnabled(true);
}

} catch (Exception e) {
e.printStackTrace();
}
}

public void unsetMTTSeqNo() {
try {
System.out.println("Entering unsetMTTSeqNo Method");
String newMTT = getEmptyIfNull(getPane().getMACHTD()).trim();
String mttStatus = getEmptyIfNull(getPane().getMTTSTUS()).trim();
if (newMTT != null && !newMTT.equalsIgnoreCase("") && newMTT.equalsIgnoreCase("YES")) {
      if (mttStatus != null && !mttStatus.equalsIgnoreCase("") && mttStatus.equalsIgnoreCase("E")) {
            getPane().setMTTNUM("");
            getPane().getCtlMTTNUM().setEnabled(true);
      }
}
} catch (Exception e) {
e.printStackTrace();
}
}

public void setMTTRefNumber() {
System.out.println("Entering setMTTRefNumber Method");
String mttStatus = "";
int mttYear = 0;
String mttSeqNo = "";
String mttRefNumber = "";
String merchantTrading = "";

try {

mttYear = Calendar.getInstance().get(Calendar.YEAR);
System.out.println("MTT Year--->" + mttYear);
mttStatus = getPane().getMTTSTUS();
System.out.println("MTT Status--->" + mttStatus);
mttRefNumber = getPane().getMTTNUM();
System.out.println("MTT Ref Number--->" + mttRefNumber);
merchantTrading = getPane().getMACHTD();
System.out.println("Merchant Trading--->" + merchantTrading);

if (merchantTrading != null && !merchantTrading.equalsIgnoreCase("")
            && merchantTrading.equalsIgnoreCase("YES")) {
      if (mttRefNumber == null || mttRefNumber.equalsIgnoreCase("")) {
            System.out.println("MTT Sequence Number From Query--->" + mttSeqNo);

            if (mttStatus != null && !mttStatus.equalsIgnoreCase("") && mttStatus.equalsIgnoreCase("N")) {
                  mttSeqNo = getMttSeqNo();
                  mttSeqNo = mttYear + mttSeqNo;
                  System.out.println("Final MTT Number---->" + mttSeqNo);
                  getPane().setMTTNUM(mttSeqNo);
                  getPane().getCtlMTTNUM().setEnabled(false);
            }
      }
}
} catch (Exception e) {
e.printStackTrace();
}
}

public String getMttSeqNo() {
String seqNo = "";
Connection con = null;
PreparedStatement pst = null;
ResultSet rs = null;
try {
con = ConnectionMaster.getConnection();
String seqNoQuery = "SELECT LPAD(MTT_SEQ_NO.NEXTVAL,6,0) AS SEQNO FROM DUAL";
pst = con.prepareStatement(seqNoQuery);
rs = pst.executeQuery();
while (rs.next()) {
      seqNo = rs.getString("SEQNO");
}

System.out.println("Sequence Number--->" + seqNo);

} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, pst, rs);
}

return seqNo;
}

public void unsetMTTFields() {
try {
String newMTT = getEmptyIfNull(getPane().getMACHTD()).trim();
if (!newMTT.equalsIgnoreCase("YES")) {
      getPane().setMTTSTUS("");
      getPane().setMTTCLOSE(false);
      getPane().setMTTNUM("");
      getPane().setMTTREMAR("");

}
} catch (Exception e) {
e.printStackTrace();
}
}

public void newMttNumber() {
String mttStatus = "";
String merchantTrading = "";
String mttRefNumber = "";
try {
mttStatus = getPane().getMTTSTUS();
System.out.println("MTT Status--->" + mttStatus);
merchantTrading = getPane().getMACHTD();
System.out.println("Merchant Trading--->" + merchantTrading);
mttRefNumber = getPane().getMTTNUM();
System.out.println("MTT Ref Number--->" + mttRefNumber);

if (merchantTrading != null && !merchantTrading.equalsIgnoreCase("")
            && merchantTrading.equalsIgnoreCase("YES")) {

      if (mttStatus != null && !mttStatus.equalsIgnoreCase("") && mttStatus.equalsIgnoreCase("N")) {
            if (mttRefNumber != null && !mttRefNumber.equalsIgnoreCase("")) {
                  getPane().getCtlMTTNUM().setEnabled(false);
            }
      } else {
            getPane().getCtlMTTNUM().setEnabled(true);
      }

}

} catch (Exception e) {
e.printStackTrace();
}
}

public void validateClosedMttNumber(ValidationDetails validationDetails) {
System.out.println("Entering validateClosedMttNumber Method");
String mttStatus = "";
String mttRefNumber = "";
String query = "";
Connection con = null;
PreparedStatement pst = null;
ResultSet rs = null;
int count = 0;
try {
con = ConnectionMaster.getConnection();
mttStatus = getPane().getMTTSTUS();

String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventref = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String finalRef = masterref + eventref;
System.out.println("mttClosedStatus-->" + mttStatus);
if (mttStatus != null && !mttStatus.equalsIgnoreCase("") && mttStatus.equalsIgnoreCase("E")) {
      mttRefNumber = getPane().getMTTNUM();
      System.out.println("MTT Reference Number---->" + mttRefNumber);

//query = "SELECT COUNT(*) FROM ETT_MTT_ENQUIRY WHERE MTT_CLOSE_STATUS ='Y' AND MTTNUMBER= ? AND MASTER_REFERENCE <> ?";
      query = "SELECT COUNT(*) FROM ETT_MTT_ENQUIRY WHERE MTT_CLOSE_STATUS ='Y' AND MTTNUMBER= ? ";
      pst = con.prepareStatement(query);
      pst.setInt(1, Integer.parseInt(mttRefNumber.trim()));
//pst.setString(2, finalRef.trim());
      rs = pst.executeQuery();
      while (rs.next()) {
            count = rs.getInt(1);
      }
      System.out.println("Count--->" + count);

      if (count > 0) {
            System.out.println("The MTT Number is closed");
            validationDetails.addError(ErrorType.Other, "The MTT Number is closed ");
      }
}

} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, pst, rs);
}

}

public void validateMttRefNumber(ValidationDetails validationDetails) {
System.out.println("Entering validateMttRefNumber Method");
String mttStatus = "";
String customerID = "";
String customerName = "";
String custToCheck = "";
String query = "";
String mttNumber = "";
String masterReference = "";
Connection con = null;
PreparedStatement ps2 = null;
PreparedStatement pst = null;
ResultSet rs2 = null;
ResultSet rs = null;
int count = 0;
int count1 = 0;
try {
con = ConnectionMaster.getConnection();
mttStatus = getPane().getMTTSTUS();
mttNumber = getPane().getMTTNUM();
//System.out.println("MTT Status--->" + mttStatus);
//System.out.println("MTT Number--->" + mttNumber);
if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IDC")
            || getMajorCode().equalsIgnoreCase("CPCO"))
            && (getMinorCode().equalsIgnoreCase("CLP") || getMinorCode().equalsIgnoreCase("POC")
                        || getMinorCode().equalsIgnoreCase("CRE") || getMinorCode().equalsIgnoreCase("CRC")
                        || getMinorCode().equalsIgnoreCase("PCOC"))) {
      customerID = getEmptyIfNull(getDriverWrapper().getEventFieldAsText("BUY", "p", "cu")).trim();
      System.out.println("Customer ID---->" + customerID);
} else {
      customerID = getEmptyIfNull(getDriverWrapper().getEventFieldAsText("SEL", "p", "cu")).trim();
      System.out.println("Customer ID---->" + customerID);
}
//customerName = getEmptyIfNull(getDriverWrapper().getEventFieldAsText("DRW", "p", "f")).trim();
//System.out.println("Customer ID---->" + customerName);
//String[] splited = customerName.split("\\s+");
//System.out.println("Splitted--->" + splited);
//masterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
//System.out.println("Master Reference--->" + masterReference);

//if (customerID == null || customerID.equalsIgnoreCase("")) {
//custToCheck = splited[0].toUpperCase();
//} else {
//custToCheck = customerID;
//}
System.out.println("Final Customer to Check--->" + custToCheck + " " + customerID);

if (mttStatus != null && !mttStatus.equalsIgnoreCase("") && mttStatus.equalsIgnoreCase("E")) {
//if (customerID == null || customerID.equalsIgnoreCase("")) {
      query = "SELECT CUSTOMER_CODE FROM ETT_MTT_ENQUIRY WHERE MTTNUMBER ='" + mttNumber + "'";
//} else {
//  query = "SELECT COUNT(*) AS COUNT FROM ETT_MTT_ENQUIRY WHERE CUSTOMER_CODE = ? AND MTTNUMBER = ? AND MASTER_REF <> ? ";
//}

      pst = con.prepareStatement(query);
//pst.setString(1, custToCheck);
//pst.setString(2, mttNumber.trim());
//pst.setString(3, masterReference);
      rs = pst.executeQuery();
      while (rs.next()) {
            custToCheck = rs.getString(1).trim();
      }
      System.out.println("Customer to check MTT Number --->" + custToCheck + " " + query);
      if (!custToCheck.equalsIgnoreCase(customerID)) {
            System.out.println(" inside warning of cif id");
            // createErrorAndWarnings(validationDetails,"MTT001");
            validationDetails.addError(ErrorType.Other, "Mtt number is already used by another customer");
      }

}
//else if (mttStatus != null && !mttStatus.equalsIgnoreCase("") && mttStatus.equalsIgnoreCase("N")) {

//if (mttNumber != null && !mttNumber.equalsIgnoreCase("")) {
//  query = "SELECT COUNT(*) AS COUNT FROM ETT_MTT_ENQUIRY WHERE MTTNUMBER = ?  AND TRIM(MASTER_REF) <> ?";
//  pst = con.prepareStatement(query);
//  pst.setString(1, mttNumber.trim());
//  pst.setString(2, masterReference.trim());
//  rs = pst.executeQuery();
//
//  while (rs.next()) {
//    count1 = rs.getInt(1);
//  }
//
//  System.out.println("Count of New MTT Number--->" + count1);
//
//  if (count1 > 0) {
//    // createErrorAndWarnings(validationDetails,"MTT004");
//    validationDetails.addWarning(WarningType.Other, "MTT004");
//  }
//}
//
//}
//}
} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, pst, rs);
}
// added on 14/09/2022 by Sushmita
try {
String mttNum = "";
con = getConnection();
//    ps2 = con.prepareStatement(query);
//    rs2 = ps2.executeQuery();
mttNum = getPane().getMTTNUM();
mttStatus = getPane().getMTTSTUS();
//    query="select MTT_CLOSE_STATUS,MTTNUMBER from ETT_MTT_ENQUIRY where trim(MTTNUMBER)='"+mttNum+"'";
//    ps2 = con.prepareStatement(query);
//    rs2 = ps2.executeQuery();

if (mttStatus.equalsIgnoreCase("E")) {
      query = "select MTT_CLOSE_STATUS,MTTNUMBER from ETT_MTT_ENQUIRY where MTTNUMBER='" + mttNum + "'";
      ps2 = con.prepareStatement(query);
      rs2 = ps2.executeQuery();
      System.out.println("MTT Number exists query" + query);
      if (rs2.next()) {
            System.out.println("MTT Number exists" + mttNum);
      } else {
            System.out.println("MTT Number does not exist");
            validationDetails.addError(ErrorType.Other, "The MTT Number does not exsist ");
      }
}
} catch (Exception e) {
e.printStackTrace();
} finally {
try {
      if (rs2 != null)
            rs2.close();
      if (ps2 != null)
            ps2.close();
      if (con != null)
            con.close();
} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}

}

public void invalidMTTNumber(ValidationDetails validationDetails) {
int mttYear = 0;
String mttRefNumber = "";
String mttStatus = "";
String merchantTrading = "";
try {
mttYear = Calendar.getInstance().get(Calendar.YEAR);
System.out.println("MTT Year--->" + mttYear);

mttStatus = getPane().getMTTSTUS();
System.out.println("MTT Status--->" + mttStatus);
merchantTrading = getPane().getMACHTD();
System.out.println("Merchant Trading--->" + merchantTrading);
mttRefNumber = getPane().getMTTNUM();
System.out.println("MTT Ref Number--->" + mttRefNumber);

if (mttRefNumber != null && !mttRefNumber.equalsIgnoreCase("")) {
      if (merchantTrading != null && !merchantTrading.equalsIgnoreCase("")
                  && merchantTrading.equalsIgnoreCase("YES")) {
            if (mttStatus != null && !mttStatus.equalsIgnoreCase("") && mttStatus.equalsIgnoreCase("N")) {
                  mttRefNumber = mttRefNumber.substring(0, 4);
                  System.out.println("Year in the MTT Number--->" + mttRefNumber);
                  int mttNumYear = Integer.parseInt(mttRefNumber);
                  System.out.println("Year in the MTT Number in Int--->" + mttNumYear);

                  if (mttYear != mttNumYear) {
                        // createErrorAndWarnings(validationDetails, "MTT006");
                        validationDetails.addWarning(WarningType.Other, "MTT006");
                  }

            }
      }
}
} catch (Exception e) {
e.printStackTrace();
}

}

public void mttcommenceDate(ValidationDetails validationDetails) {
try {
con = ConnectionMaster.getConnection();
String mttRefNumber = "";
String merchantTrading = "";
int ageing = 0;
// String todaysDate = "";
DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yy");
mttRefNumber = getPane().getMTTNUM();
merchantTrading = getPane().getMACHTD();
System.out.println("MTT Ref Number--->" + mttRefNumber);
if (merchantTrading.equalsIgnoreCase("YES")) {
      String dateQuery = "select AGEING from MTT_ANNEXURE_II_MASTER_TBL where  MTT_REF='" + mttRefNumber
                  + "' and rownum=1";
      System.out.println(dateQuery);
      ps1 = con.prepareStatement(dateQuery);
      rs1 = ps1.executeQuery();
      if (rs1.next()) {
            ageing = rs1.getInt(1);
            System.out.println("ageing value " + ageing);
      }
      if ((mttRefNumber != null && !mttRefNumber.equalsIgnoreCase("")) && (ageing != 0)) {
//LocalDate commenceDt= LocalDate.parse(commencDate, formatters);
//LocalDate todaysDt= LocalDate.parse(todaysDate, formatters);
//long no = ChronoUnit.DAYS.between(commenceDt, todaysDt);
//System.out.println("Inside COMMENCEMENT_DATE Validation condition "+no+" "+ commenceDt +" "+ todaysDt);
            if (ageing > 270) {
                  validationDetails.addWarning(WarningType.Other, "MTT Exceeding 270 days");
                  System.out.println("Inside if of Validation condition ");
            }

      }
}
} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, ps1, rs1);
}
}

public void mttforeignExchange(ValidationDetails validationDetails) {
try {
con = ConnectionMaster.getConnection();
String mttRefNumber = "";
String merchantTrading = "";
boolean mttclose = false;
String importAmtPaid = "";
String exportAmtReal = "";
int transDt = 0;
double converRate = 0.0;
BigDecimal masAmt = BigDecimal.ZERO;
BigDecimal multiplyAmt = new BigDecimal(0.05);
String tranamt = "";
String TranCcy = "";
String flag = "";
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventref = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
BigDecimal importAmtPd = BigDecimal.ZERO;
BigDecimal exportAmtRl = BigDecimal.ZERO;
//String sys = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
//DateTimeFormatter formatters= DateTimeFormatter.ofPattern("dd/MM/yy");
mttRefNumber = getPane().getMTTNUM();
merchantTrading = getPane().getMACHTD();
mttclose = getPane().getMTTCLOSE();
System.out.println("MTT Ref Number--->" + mttRefNumber + " " + merchantTrading + " " + mttclose);
if (merchantTrading.equalsIgnoreCase("YES")) {
      // String dateQuery="SELECT sum(IMPORT_AMT_PAID), sum(EXPORT_AMT_REALISED) FROM
      // MTT_ANNEXURE_II_MASTER_VIEW where MTT_REF='"+mttRefNumber+"'";
      String dateQuery2 = "SELECT FOREIGN_EXCHANGE_OVERLAY FROM MTT_ANNEXURE_II_MASTER_TBL where MTT_REF='"
                  + mttRefNumber + "'";

      ps2 = con.prepareStatement(dateQuery2);
      rs2 = ps2.executeQuery();
      if (rs2.next()) {
            transDt = rs2.getInt(1);
            System.out.println("transDt " + transDt);
      }
      if (transDt > 120) {
            System.out.println("inside four months validation");
            validationDetails.addWarning(WarningType.Other, "Foreign Exchange Outlay Exceed 120 Days.");

      }

      if (mttclose) {
            String dateQuery = "SELECT sum(IMPORT_AMT_PAID), sum(EXPORT_AMT_REALISED) FROM MTT_ANNEXURE_II_MASTER_TBL where MTT_REF='"
                        + mttRefNumber + "'";
            System.out.println(dateQuery);
            ps1 = con.prepareStatement(dateQuery);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                  importAmtPaid = rs1.getString(1).trim();
                  exportAmtReal = rs1.getString(2).trim();
                  System.out.println(importAmtPaid + " " + exportAmtReal);
            }
            if ((mttRefNumber != null && !mttRefNumber.equalsIgnoreCase(""))
                        && (importAmtPaid != null && !importAmtPaid.equalsIgnoreCase(""))
                        && (exportAmtReal != null && !exportAmtReal.equalsIgnoreCase(""))) {

                  importAmtPd = new BigDecimal(importAmtPaid);
                  exportAmtRl = new BigDecimal(exportAmtReal);
//    BigDecimal resulAmt =importAmtPd.subtract(exportAmtRl) ;
//    BigDecimal percentAmt =importAmtPd.multiply(multiplyAmt);
//    percentAmt=percentAmt.setScale(2,BigDecimal.ROUND_HALF_EVEN);
                  // LocalDate transDate= LocalDate.parse(transDt, formatters);
                  // LocalDate system= LocalDate.parse(sys, formatters);
                  // long no = ChronoUnit.DAYS.between(system, transDate);
                  System.out.println(
                              "merchant trade amount fields: " + importAmtPd + " " + exportAmtRl + " " + transDt);
                  String amountQuery = "SELECT  net.PAYRECFLG,(NET.AMOUNT/100) AS AMOUNT,NET.AMT_CCY FROM MASTER MAS, BASEEVENT BEV, RELITEM REL, NETTEDFM NET ,ACCOUNT ACC"
                              + " WHERE MAS.KEY97   = BEV.MASTER_KEY AND BEV.KEY97 = REL.EVENT_KEY "
                              + " AND REL.KEY97     = NET.KEY97" + " AND  NET.STTLACCT1 =ACC.ACCT_KEY"
                              + " and ACC_TYPE IN('CN')" + " AND MAS.MASTER_REF='" + masterref + "'"
                              + " AND BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0)='" + eventref + "'"
                              + " AND net.AMOUNT IS NOT NULL";
                  ps3 = con.prepareStatement(amountQuery);
                  rs3 = ps3.executeQuery();
                  if (rs3.next()) {
                        flag = rs3.getString(1).trim();
                        tranamt = rs3.getString(2).trim();
                        TranCcy = rs3.getString(3).trim();
                        System.out.println(tranamt + " " + TranCcy + " " + flag);
                  }
                  converRate = getCurrencyRate(TranCcy);
                  BigDecimal conversionRate = new BigDecimal(converRate);
                  masAmt = new BigDecimal(tranamt);
                  BigDecimal usdAmt = masAmt.multiply(conversionRate);
                  usdAmt = usdAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                  if (flag.equalsIgnoreCase("P")) {
                        importAmtPd = importAmtPd.add(usdAmt);
                        System.out.println("merchant trade amount fields of import: " + importAmtPd + " " + usdAmt);
                  } else {
                        exportAmtRl = exportAmtRl.add(usdAmt);
                        System.out.println("merchant trade amount fields of export: " + exportAmtRl + " " + usdAmt);
                  }
                  if (importAmtPd.compareTo(exportAmtRl) == 1) {
                        System.out.println("export amount validation");
                        validationDetails.addWarning(WarningType.Other, "MTT is closing with Loss");
                  }
//    if(  importAmtPd.compareTo(masAmt)==0 && exportAmtRl.compareTo(masAmt)==1) {
//          System.out.println("import amount validation");
//          validationDetails.addWarning(WarningType.Other, "Export amount into EEFC account");
//    }
//    if(  importAmtPd.compareTo(exportAmtRl)==1&& resulAmt.compareTo(percentAmt)==1){
//          System.out.println("5 percent validation");
//          validationDetails.addWarning(WarningType.Other, "Merchant traders with outstanding of 5% or more of their annual export earnings shall be liable for caution listing.");
//    }
            }
      }
}
} catch (Exception e) {
e.printStackTrace();
} finally {
try {
      if (rs2 != null)
            rs2.close();
      if (ps2 != null)
            ps2.close();
      if (rs1 != null)
            rs1.close();
      if (ps1 != null)
            ps1.close();
      if (rs3 != null)
            rs3.close();
      if (ps3 != null)
            ps3.close();
      if (con != null)
            con.close();

} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}
}

// Nostro validation based on uid added by Vishal G
public void getNostroValidation(ValidationDetails validationDetails) {
try {
String masReference = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
String eventref = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
String custId = getDriverWrapper().getEventFieldAsText("PRM", "p", "cu").trim();
String uniqueId = getDriverWrapper().getEventFieldAsText("cBUB", "s", "").trim();
String branch = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
String eventCode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
String inputXml = "<?xml version=\"1.0\" standalone=\"yes\"?><ServiceRequest xmlns:m='urn:messages.service.ti.apps.tiplus2.misys.com' xmlns='urn:control.services.tiplus2.misys.com' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
            + "<RequestHeader>" + "<Service>Treasury</Service>" + "<Operation>Validate</Operation>"
            + "<Credentials>" + "<Name>Name</Name>" + "<Password>Password</Password>"
            + "<Certificate>Certificate</Certificate>" + "<Digest>Digest</Digest>" + "</Credentials>"
            + "<ReplyFormat>FULL</ReplyFormat>" + "<NoRepair>Y</NoRepair>" + "<NoOverride>Y</NoOverride>"
            + "<CorrelationId>CorrelationId</CorrelationId>" + "<TransactionControl>NONE</TransactionControl>"
            + "</RequestHeader>" + "<TreasuryValidateRequest>" + "<MASTERREF>" + masReference + "</MASTERREF>"
            + "<EVENTREF>" + eventref + "</EVENTREF>" + "<CUSTID>" + custId + "</CUSTID>" + "<UNIQUEID>"
            + uniqueId + "</UNIQUEID>" + "<INPUTBRANCH>" + branch + "</INPUTBRANCH>" + "<EVENTCODE>" + eventCode
            + "</EVENTCODE>" + "<VALIDATIONTYPE>BOVERIFY</VALIDATIONTYPE>"
            + "</TreasuryValidateRequest></ServiceRequest>";

ThemeTransportClient aClient = new ThemeTransportClient();
String resultXml = aClient.invoke("Treasury", "Validate", inputXml);

String errMsg = getTagValue(resultXml, "ERRORMSG");
String warningMsg = getTagValue(resultXml, "WARNMSG");
if (errMsg != null && !errMsg.trim().isEmpty()) {
      {
            validationDetails.addError(ErrorType.Other, errMsg);
      }
} else if (warningMsg != null && !warningMsg.trim().isEmpty()) {
      {
            validationDetails.addWarning(WarningType.Other, warningMsg);
      }
}
} catch (Exception e) {
e.printStackTrace();
}
}

// LRS Calculation validation for 2500000 usd added by Vishal G
public void remittanceLRSTCS(String masRef, String eveRef, ValidationDetails validationDetails)
throws SQLException {

PreparedStatement ps1 = null;
ResultSet rs1 = null;
String sys = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
String masterAmt = getDriverWrapper().getEventFieldAsText("LPAY", "v", "m").trim();
String masterCcy = getDriverWrapper().getEventFieldAsText("LPAY", "v", "c").trim();
//String currPurCode = getDriverWrapper().getEventFieldAsText("cAQL", "s", "c").trim();
String custId = getDriverWrapper().getEventFieldAsText("RMIT", "p", "cu").trim();
double amount1 = 0.0;
double totComplAmt = 0.0;
String totComplAmtCcy = "";
Connection conn = null;
try {
conn = ConnectionMaster.getConnection();
String from = null;
String to = null;
String sys1 = null;
String year = null;
int month = 3;
int y = 0;
sys1 = sys.substring(3, 5);
year = sys.substring(6, 10);
int mon = Integer.parseInt(sys1);
y = Integer.parseInt(year);
System.out.println("In remittanceLRSChargeChange " + masterAmt + " " + masterCcy + " " + sys);
if (mon <= month) {
      from = "01/04/" + (y - 1);
      to = sys;
} else {
      from = "01/04/" + y;
      to = sys;
}

// FOR TOTAL AMOUNT & INTEREST AMOUNT

String query = "SELECT MAS.AMOUNT/100 AS TOTAMT ,MAS.CCY FROM MASTER MAS,BASEEVENT BEV,EXTEVENT EXT,EXEMPL30 PROD,TIDATAITEM TID, "
            + "PARTYDTLS PRD,PRODTYPE SPROD WHERE MAS.KEY97 = BEV.MASTER_KEY AND BEV.KEY97 = EXT.EVENT "
            + "AND MAS.EXEMPLAR = PROD.KEY97 AND MAS.KEY97 = TID.MASTER_KEY AND TID.KEY97 = PRD.KEY97 "
            + "AND MAS.PRODTYPE = SPROD.KEY97 AND PROD.CODE79 IN ('CPCO') AND BEV.REFNO_PFIX = 'CRE' "
            + "AND BEV.STATUS = 'c'  AND PRD.CUS_MNM='" + custId + "'" + " AND BEV.FINISHED BETWEEN TO_DATE('"
            + from + "','DD-MM-YY') " + " AND TO_DATE('" + to + "','DD-MM-YY')";
System.out.println("query1--->" + query);
//ps3 = conn.prepareStatement(query);
//rs3 = ps3.executeQuery();
//while (rs3.next()) {
//totComplAmt = rs3.getString(1);
//totIntAmt = rs3.getString(2);
//}
//
//if (totComplAmt == null || totComplAmt.trim().equals(""))
//totComplAmt = "0.00";
//if (totIntAmt == null || totIntAmt.trim().equals(""))
//totIntAmt = "0.00";
//BigDecimal totIntAmtBig = new BigDecimal(totIntAmt);
//ConnectionMaster.surrenderDB(null, ps3, rs3);
//
//BigDecimal amount1 = new BigDecimal(totComplAmt);
//
//totalAmt = totalAmt.add(amount1);
//
//BigDecimal amount2 = new BigDecimal(masterAmt);
//totalAmt = totalAmt.add(amount2);
//
//System.out.println("Total Amount " +totalAmt +" "+amount1);
//
//// BigDecimal comTCStotalAmt = new BigDecimal(totComplAmt);
//// BigDecimal newTCStotalAmt = amount1.add(totCurrAmtBig);
//
//
//if (totalAmt.compareTo(new BigDecimal(250000)) == 1) {
//if ((currPurCode.equals("S1107") || currPurCode.equals("S1108")) )
//  System.out.println("Total Amount Comp " +totalAmt +" "+amount2);
//{
//  validationDetails.addWarning(WarningType.Other, "Transaction exceeds more than 2500000 limit");
//}
//}
//
//
//
//}
ps1 = conn.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
      totComplAmtCcy = rs1.getString(2);
      String Amount = rs1.getString(1);
      if (!totComplAmtCcy.equalsIgnoreCase("USD")) {
            double converRate = getCurrencyRate(totComplAmtCcy);
            totComplAmt = totComplAmt + (Double.parseDouble(Amount) * converRate);
            System.out.println("inside if of while " + totComplAmt + " " + totComplAmtCcy + " " + converRate);
      } else {
            totComplAmt = totComplAmt + Double.parseDouble(Amount);
      }

}
if (!masterCcy.equalsIgnoreCase("USD")) {
      double converRate = getCurrencyRate(totComplAmtCcy);
      amount1 = Double.parseDouble(masterAmt) * converRate;

} else {
      amount1 = Double.parseDouble(masterAmt);
}

double totalAmt = totComplAmt + amount1;

System.out.println("Total Amount " + totalAmt + " " + amount1);

// BigDecimal comTCStotalAmt = new BigDecimal(totComplAmt);
// BigDecimal newTCStotalAmt = amount1.add(totCurrAmtBig);

if (totalAmt > 250000) {
      System.out.println("Total Amount Comp " + totalAmt + " " + totComplAmt + " " + amount1);
      validationDetails.addWarning(WarningType.Other, "Transaction exceeds more than 250000 limit");

}

} catch (Exception e) {
e.printStackTrace();
} finally {
surrenderDB(conn, ps1, rs1);
}
}

// Account no. and behalf of branch validation added by Vishal G
public void getaccountCode() {
Statement st = null;
try {
String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String branch = getDriverWrapper().getEventFieldAsText("BOB", "b", "c");
con = ConnectionMaster.getConnection();

String account = "";
String query = "SELECT trim(ACC.BO_ACCTNO) As BO_ACCTNO ,trim(ACC.ext_ACCTNO),acc.category "
            + "FROM MASTER MAS, BASEEVENT BEV, RELITEM REL, NETTEDFM NET ,ACCOUNT ACC "
            + "WHERE MAS.KEY97   = BEV.MASTER_KEY AND BEV.KEY97 = REL.EVENT_KEY "
            + "AND REL.KEY97     = NET.KEY97   AND NET.STTLACCT1 =ACC.ACCT_KEY " + "AND MAS.MASTER_REF='"
            + masReference + "' " + " and acc.category='28500030'";
st = con.createStatement();
// ps = con.prepareStatement(query);
rs = st.executeQuery(query);
while (rs.next()) {
      account = rs.getString("BO_ACCTNO");

}
if (account != null && !account.trim().isEmpty()) {
      String acct = account.substring(0, 5);
      if (!branch.equalsIgnoreCase(acct)) {
            System.out.println("inside warning " + acct + " " + branch);
            validationDetails.addWarning(WarningType.Other,
                        account + " Account number  should belong to Behalf of Branch " + branch
                                    + ". The transaction may fail");
      }
}
}

catch (Exception e) {
e.printStackTrace();
}

finally {
ConnectionMaster.surrenderDBConnection(con, st, rs);
}
}

// Double shiiping utilisation validation added by Vishal G
public void getShiipingWarning(ValidationDetails validationDetails) {
try {
System.out.println("###getShiipingWarning");
List<ExtEventShippingCollections> shcodc = (List<ExtEventShippingCollections>) getWrapper()
            .getExtEventShippingCollections();
List<ExtEventShippingTable> shiplcd = (List<ExtEventShippingTable>) getWrapper().getExtEventShippingTable();

String queryodc = "";
System.out.println("ExtEventShippingCollections size==>" + shcodc.size());

Connection con = null;
ResultSet rs1 = null;
PreparedStatement ps1 = null;
String billnum = "";
String shipBilldate = "";
String portcode = "";
String master = "";
if ((getMajorCode().equalsIgnoreCase("ODC")) && (getMinorCode().equalsIgnoreCase("CRE"))) {
      for (int i = 0; i < shcodc.size(); i++) {
            ExtEventShippingCollections val1 = shcodc.get(i);

            try {
                  billnum = val1.getCBILLNUM().trim();
            } catch (Exception e) {
                  System.out.println("Exception billnum num and other details" + e.getMessage());

            }

            portcode = val1.getCPORTCO();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

            shipBilldate = format.format(val1.getCBILLDA());

            System.out.println("bill num and other details" + billnum + " " + portcode);

            int count_odc = 0;
            con = ConnectionMaster.getConnection();
            queryodc = "select count(*) ,mas.master_ref from  exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('CRE') then 'c' else 'i' end )) and shc.CBILLNUM='"
                        + billnum + "' AND  TO_CHAR(shc.CBILLDA,'DD/MM/YY')='" + shipBilldate
                        + "' and shc.CPORTCO='" + portcode + "' " + "group by mas.master_ref";

            System.out.println("Query2 billnum EXTEVENTSHC---------->" + queryodc);
            ps1 = con.prepareStatement(queryodc);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                  count_odc = rs1.getInt(1);
                  master = rs1.getString(2);
                  System.out.println("Query2 billnum count_odc---------->" + count_odc + " " + master);
            }

//ELC bill no checking
            int count_elc = 0;

            String query_elc = "select count(*) ,mas.master_ref from  exteventsht sht, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and sht.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('CRE') then 'c' else 'i' end )) and sht.BILLNUM='"
                        + billnum + "' AND  TO_CHAR(sht.BILLDAT,'DD/MM/YY')='" + shipBilldate
                        + "' and sht.PORTCODD='" + portcode + "' " + "group by mas.master_ref";

            System.out.println("Query_elc EXTEVENTSHT-------->" + query_elc);

            ps1 = con.prepareStatement(query_elc);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                  count_elc = rs1.getInt(1);
                  master = rs1.getString(2);
                  System.out.println("Query_elc checking Count bill no---------->" + count_elc + " " + master);
            }
            if ((count_odc > 0 || count_elc > 0) && getMinorCode().equalsIgnoreCase("CRE")) {
                  validationDetails.addWarning(WarningType.Other,
                              "Entered shipping bill number already used (" + billnum + ")in " + master + "[CM]");
            }

      }
}
if ((getMajorCode().equalsIgnoreCase("ELC")) && (getMinorCode().equalsIgnoreCase("DOP"))) {
      int countv = 0;
      int countvodc = 0;
      String shipBilNo = "";
      String portCode = "";

      for (int l = 0; l < shiplcd.size(); l++) {
            ExtEventShippingTable shipinglc = shiplcd.get(l);
            shipBilNo = shipinglc.getBILLNUM().trim();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

            shipBilldate = format.format(shipinglc.getBILLDAT());
            portCode = shipinglc.getPORTCODDD();
            // Loggers.general().info(LOG,"Shpping Bill date " +
            // shipBilldate + "and " + portCode);
            System.out.println(
                        "shipBilNo==>" + shipBilNo + ",shipBilldate==>" + shipBilldate + ",portCode==>" + portCode);
            con = ConnectionMaster.getConnection();
            String query = "select count(*),mas.master_ref from  exteventsht sht, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and sht.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and sht.BILLNUM='"
                        + shipBilNo + "' AND  TO_CHAR(sht.BILLDAT,'DD/MM/YY')='" + shipBilldate
                        + "' and sht.PORTCODD='" + portCode + "' " + "group by mas.master_ref";
            System.out.println("CR104 : query==>" + query);
            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                  countv = rs1.getInt(1);
                  master = rs1.getString(2);
            }

            String query2 = "select count(*),mas.master_ref from  exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.CBILLNUM='"
                        + shipBilNo + "' AND  TO_CHAR(shc.CBILLDA,'DD/MM/YY')='" + shipBilldate
                        + "' and shc.CPORTCO='" + portCode + "' " + "group by mas.master_ref";

            ps1 = con.prepareStatement(query2);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                  countvodc = rs1.getInt(1);
                  master = rs1.getString(2);
            }
            System.out.println("CR104 : query2==>" + query2);

            // elc validation
            if ((countv > 0 || countvodc > 0) && getMinorCode().equalsIgnoreCase("DOP")) {
                  validationDetails.addWarning(WarningType.Other,
                              "Entered shipping bill number already used (" + shipBilNo + ")in " + master + "[CM]");
            }
      }
}
} catch (Exception e) {
e.printStackTrace();
System.out.println("Exception Query_elc EXTEVENTSHT-------->" + e.getMessage());
} finally {
surrenderDB(con, ps1, rs1);
}
}

// Vaisak -Shipping Collection Grid - Amount Calculation - 24-02-23
public void shippingAmountSum() {
try {
System.out.println("###shippingAmountSum");
List<ExtEventShippingCollections> shipTab = (List<ExtEventShippingCollections>) getWrapper()
            .getExtEventShippingCollections();
List<ExtEventShippingTable> shipTable = (List<ExtEventShippingTable>) getWrapper()
            .getExtEventShippingTable();

BigDecimal sbTotalAmtTotal = new BigDecimal(0), repayAmtTotal = new BigDecimal(0),
            shortCollAmtTotal = new BigDecimal(0);
BigDecimal sbAmt = null, repayAmt = null, shortCollAmt = null, divideBy100 = new BigDecimal(100);
String sbCurr = null, repayCurr = null, shortCurr = null;
if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")) {
      for (int i = 0; i < shipTab.size(); i++) {
            ExtEventShippingCollections ship = shipTab.get(i);
            sbAmt = ship.getCSHPAMT();
            repayAmt = ship.getCREPAY();
            shortCollAmt = ship.getCSHCOLAM();
            sbCurr = ship.getCSHPAMTCurrency();
            repayCurr = ship.getCREPAYCurrency();
            shortCurr = ship.getCSHCOLAMCurrency();

            if (!sbTotalAmtTotal.equals(null) && !sbCurr.equals(null) && !sbCurr.equals("")
                        && !sbAmt.equals(null)) {
                  sbTotalAmtTotal = sbTotalAmtTotal.add(sbAmt);
            }
            if (!repayAmtTotal.equals(null) && !repayCurr.equals(null) && !repayCurr.equals("")
                        && !repayAmt.equals(null)) {
                  repayAmtTotal = repayAmtTotal.add(repayAmt);
            }
            if (!shortCollAmtTotal.equals(null) && !shortCurr.equals(null) && !shortCurr.equals("")
                        && !shortCollAmt.equals(null)) {
                  shortCollAmtTotal = shortCollAmtTotal.add(shortCollAmt);
            }
      }
      sbTotalAmtTotal = sbTotalAmtTotal.divide(divideBy100);
      repayAmtTotal = repayAmtTotal.divide(divideBy100);
      shortCollAmtTotal = shortCollAmtTotal.divide(divideBy100);
      System.out.println(sbTotalAmtTotal.toString() + " " + sbCurr + "__" + repayAmtTotal.toString() + " "
                  + repayCurr + "___" + shortCollAmtTotal.toString() + " " + shortCurr);
      getPane().setTOTSBAMT(sbTotalAmtTotal.toString() + " " + sbCurr);
      getPane().setOTHBAN(repayAmtTotal.toString() + " " + repayCurr);
      getPane().setSUBVDEB(shortCollAmtTotal.toString() + " " + shortCurr);

}
if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")) {

      for (int i = 0; i < shipTable.size(); i++) {
            ExtEventShippingTable shpTable = shipTable.get(i);
            sbAmt = shpTable.getSHPAMT();
            repayAmt = shpTable.getREPAYAM();
            shortCollAmt = shpTable.getSHCOLAM();
            sbCurr = shpTable.getSHPAMTCurrency();
            repayCurr = shpTable.getREPAYAMCurrency();
            shortCurr = shpTable.getSHCOLAMCurrency();

            if (!sbTotalAmtTotal.equals(null) && !sbCurr.equals(null) && !sbCurr.equals("")) {
                  sbTotalAmtTotal = sbTotalAmtTotal.add(sbAmt);
            }
            if (!repayAmtTotal.equals(null) && !repayCurr.equals(null) && !repayCurr.equals("")) {
                  repayAmtTotal = repayAmtTotal.add(repayAmt);
            }
            if (!shortCollAmtTotal.equals(null) && !shortCurr.equals(null) && !shortCurr.equals("")) {
                  shortCollAmtTotal = shortCollAmtTotal.add(shortCollAmt);
            }

      }
      sbTotalAmtTotal = sbTotalAmtTotal.divide(divideBy100);
      sbTotalAmtTotal = sbTotalAmtTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      repayAmtTotal = repayAmtTotal.divide(divideBy100);
      repayAmtTotal = repayAmtTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      shortCollAmtTotal = shortCollAmtTotal.divide(divideBy100);
      shortCollAmtTotal = shortCollAmtTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      System.out.println(sbTotalAmtTotal.toString() + " " + sbCurr + "__" + repayAmtTotal.toString() + " "
                  + repayCurr + "___" + shortCollAmtTotal.toString() + " " + shortCurr);
      getPane().setTOTSBAMT(sbTotalAmtTotal.toString() + " " + sbCurr);
      getPane().setOTHBAN(repayAmtTotal.toString() + " " + repayCurr);
      getPane().setSUBVDEB(shortCollAmtTotal.toString() + " " + shortCurr);
}

} catch (Exception e) {
e.printStackTrace();
System.out.println("Shipping Amount Sum Calculation Error: " + e.getMessage());
}
}

// Vishal G -Forward reference fields - 24-02-23
public void getForwardReference() {

String amount = "";
String ccy = "";
String reference = "";
String mindate = "";
String maxdate = "";
int count = 0;
String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String evntRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
try {
con = ConnectionMaster.getConnection();

//  String query = "SELECT CONTRACT_REF,UTILIZED_AMOUNT,UTILIZED_CURRENCY ,min(TO_CHAR(DEAL_VALID_FROM,'DD/MM/YY')),min(TO_CHAR(DEAL_VALID_TO,'DD/MM/YY')) FROM REP_FWC_UTIL_FRONTEND where" +
//                 " MASTER_REF_NO='"+masReference+"-"+evntRefNo+"' "+" group by CONTRACT_REF,UTILIZED_AMOUNT,UTILIZED_CURRENCY";
//  System.out.println("Forward reference QUERY: " + query);
//    ps3 = con.prepareStatement(query);
//    rs3 = ps3.executeQuery();
//    while (rs3.next()) {
// reference = rs3.getString(1);
// amount = rs3.getString(2);
// ccy=rs3.getString(3);

String query = "call ETT_FWC_FROM_TO_DATE_PROC(?,?,?,?)";
stmt = con.prepareCall(query);
//       count=count+1;
//       System.out.println("Forward reference result: " + reference+" "+amount+" "+count);
//       if (count==1){
//             getPane().setFWCREF1(reference);
//             getPane().setUTILZAM1(amount+" "+ccy);
//       }
//       if (count==2){
//             getPane().setFWCREF2(reference);
//             getPane().setUTILZAM2(amount+" "+ccy);
//       }
//       if (count==3){
//             getPane().setFWCREF3(reference);
//             getPane().setUTILZAM3(amount+" "+ccy);
//       }
//       if (count==4){
//             getPane().setFWCREF4(reference);
//             getPane().setUTILZAM4(amount+" "+ccy);
//       }
////     if (count==5){
////           getPane().setFWCREF5(reference);
////           getPane().setUTILZAM5(amount+" "+ccy);
////     }
stmt.setString(1, masReference);
stmt.setString(2, evntRefNo);
stmt.registerOutParameter(3, Types.VARCHAR);
stmt.registerOutParameter(4, Types.VARCHAR);

stmt.execute();
mindate = stmt.getString(3);
maxdate = stmt.getString(4);
getPane().setDACOMT(mindate);
getPane().setDATCOM(maxdate);
//    }

} catch (Exception e) {
e.printStackTrace();
} finally {
try {
      if (stmt != null)
            stmt.close();
      if (con != null)
            con.close();

} catch (SQLException e) {
      // Loggers.general().info(LOG,"Connection Failed! Check output
      // console");
      e.printStackTrace();
}
}

}

// Advance table validation for export products added by Vishal G
public void getAdvanceTableValidation(ValidationDetails validationDetails) {
try {
con = getConnection();
ResultSet rs1 = null;
PreparedStatement ps1 = null;

String remitterName = "";
String thirdParty = "";
String masteref = "";
String cifId = "";
//String repayamt="";
BigDecimal utilizeamt = new BigDecimal(0);
BigDecimal hundred = new BigDecimal(100);
BigDecimal availamt = new BigDecimal(0);
String shipDate = "";
String remitDate = "";
String rbiCode = "";
String balanceCcy = "";
String utilizeCcy = "";
shipDate = getDriverWrapper().getEventFieldAsText("cAJT", "d", "");
String drawee = getDriverWrapper().getEventFieldAsText("DRE", "p", "n");
String drawer = getDriverWrapper().getEventFieldAsText("DRW", "p", "cu");
String productType = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
String thirdPartyDetails = getDriverWrapper().getEventFieldAsText("TPT", "p", "n");
String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
String colamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m").trim();
BigDecimal colamtct = new BigDecimal(colamt);

DateTimeFormatter formatters1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
List<ExtEventAdvanceTable> liste = (List<ExtEventAdvanceTable>) getWrapper().getExtEventAdvanceTable();
if (liste.size() == 0) {
      System.out.println("Remmitance not has data");
      validationDetails.addWarning(WarningType.Other, " Remittance fields not entered.");
} else {
      System.out.println("Remmitance has data");
}
for (int k = 0; k < liste.size(); k++) {
      ExtEventAdvanceTable val1 = liste.get(k);
      remitterName = val1.getNAMREM();
      thirdParty = val1.getTHRDPART();
      masteref = val1.getINWARD();

      // remitDate = val1.getDATREM();
      remitDate = new SimpleDateFormat("dd/MM/yyyy").format(val1.getDATREM());
      cifId = val1.getCUSCIFNO();
      utilizeamt = val1.getAMTUTIL();
      // String utilccy=val1.getAMTUTILCurrency();
      balanceCcy = val1.getBALANCECurrency();
      utilizeCcy = val1.getAMTUTILCurrency();
      utilizeamt = utilizeamt.divide(hundred);
      System.out.println("utilizeamt amount " + utilizeamt);
      availamt = availamt.add(utilizeamt);
//if (shipDate != null && !shipDate.trim().isEmpty()) {
//LocalDate ship = LocalDate.parse(shipDate, formatters1);
//LocalDate remit = LocalDate.parse(remitDate, formatters1);
//  System.out.println("date Remmitance: " + remitDate+" "+remit);
//  System.out.println("date Ship: " + shipDate+" "+ship);
//  if (remit.isAfter(ship)) {
//    System.out.println("date Ship: " + shipDate);
//    validationDetails.addWarning(WarningType.Other,
//          "Shipment date should be greater than Remmitance date");
//  }
//  }
      String rbiquery = "SELECT PURCODE FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='" + masteref + "'";

      ps1 = con.prepareStatement(rbiquery);
      rs1 = ps1.executeQuery();
      if (rs1.next()) {
            System.out.println("RBI Purpose code " + rs1.getString(1));
            rbiCode = rs1.getString(1);
      }

//  if(!rbiCode.equalsIgnoreCase("P0103"))
//   {
//    System.out.println("Purpose Code is not P0103");
//    validationDetails.addWarning(WarningType.Other,
//                      "Purpose Code is not matching with P0103 for Inward Remittance "+ masteref);
//              } 

      // getPane().setREMDAT(date.toString());

      if (remitterName != null && !remitterName.trim().equals("") && drawee != null
                  && !drawee.trim().equals("")) {

//if (!drawee.equalsIgnoreCase(remitterName)) {
//  System.out.println("Drawee is not equal remitter " + drawee + " " + remitterName);
//  validationDetails.addWarning(WarningType.Other,
//        "Drawee and Remitter are not same (Select Y, If Third Party Payment under Advance Payment Details Pane)");
//  if (thirdParty.equalsIgnoreCase("Y")
//        && (thirdPartyDetails.trim().equals("") || thirdPartyDetails == null)) {
//    System.out.println("thirdparty:" + thirdParty);
//    validationDetails.addWarning(WarningType.Other,
//          "Third Party Payment-Enter details under Third party single payment pane");
//  }
//
//}
            if (!cifId.equalsIgnoreCase(drawer)) {
                  validationDetails.addWarning(WarningType.Other, "CIF Id mismatch in advance payment.");
            }
      }
}
//            String query = "select mas.master_ref,prod.code from master  mas, PRODTYPE prod where" +
//                            " mas.prodtype=prod.key97 and "+
//                            " mas.MASTER_REF='"+masteref+"'";
//          String      prodcode="";
//                ps1 = con.prepareStatement(query);
//                rs1 = ps1.executeQuery();
//                while (rs1.next()) {
//                      System.out.println("PROD QUERY: "+query);
//                      prodcode = rs1.getString(2);
//                      getPane().setPURPCODE(prodcode);
//                }
String masterref1 = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventREF1 = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

//String query = "SELECT TRIM(EXTA.CCY),TRIM(EXTA.CCY_1),TRIM(EXTA.FINUMB) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTADV EXTA"
//  + " WHERE MAS.KEY97 = BEV.MASTER_KEY" + " AND BEV.KEY97 = EXT.EVENT"
//  + " AND EXTA.FK_EVENT = EXT.KEY29" + " AND MAS.MASTER_REF = '" + masterref1 + "'"
//  + " AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='" + eventREF1 + "'";
//// getPane().setREMDAT(remitDate.toString());
//ps1 = con.prepareStatement(query);
//rs1 = ps1.executeQuery();
//while (rs1.next()) {
//System.out.println("PROD QUERY: " + query);
//balanceCcy = rs1.getString(1);
//utilizeCcy = rs1.getString(2);

//                 String avail=availamt.toString().trim();
//                int spacePosition1=     avail.indexOf(" ");
//                String utilize=utilizeamt.toString().trim();
//                int spacePosition2=     utilize.indexOf(" ");

// boolean isCcySame = avail.trim().split("
// ")[1].equalsIgnoreCase(utilize.trim().split(" ")[1]) ? true : false;

// avail=avail.trim().substring(spacePosition1+1, spacePosition1+4);
// utilize=utilize.trim().substring(spacePosition2+1, spacePosition2+4);
if (balanceCcy != null && !balanceCcy.trim().equals("") && utilizeCcy != null
            && !utilizeCcy.trim().equals("")) {
      if (!balanceCcy.equalsIgnoreCase(utilizeCcy)) {
            System.out.println("Available: " + balanceCcy + " " + utilizeCcy);
            validationDetails.addWarning(WarningType.Other,
                        "Available Amount  and utilized Amount Currency Mismatch.");
      }
}
// availamt=availamt.divide(hundred);
if (utilizeCcy != null && !utilizeCcy.trim().equals("")) {
      getPane().setBALAMT(availamt.toString() + " " + utilizeCcy);
}

if (availamt.compareTo(colamtct) == 1) {
      System.out.println(" total amount of remittance is higher than the collection amount");
      validationDetails.addWarning(WarningType.Other,
                  "  total amount of remittance is higher than the collection amount ");
}

/*
 * LocalDate ship = LocalDate.parse(shipDate, formatters1); LocalDate remit =
 * LocalDate.parse(remitDate, formatters1);
 * System.out.println("date Remmitance: " + remitDate+" "+remit);
 * System.out.println("date Ship: " + shipDate+" "+ship); if
 * (remit.isAfter(ship)) { System.out.println("date Ship: " + shipDate);
 * validationDetails.addWarning(WarningType.Other,
 * "Shipment date should be greater than Remmitance date"); }
 */

} catch (Exception e) {
e.printStackTrace();
// System.out.println("outside BUYERS data:"+e);

} finally {
surrenderDB(con, ps1, rs1);
}

}

// Orm validation for import products added by Vishal G
public void getOrmDetailsValidation(ValidationDetails validationDetails) {

String balanceCcy = "";
String utilizeCcy = "";
BigDecimal utilizeamt = new BigDecimal(0);
BigDecimal availamt = new BigDecimal(0);
List<ExtEventOrmDetails> liste = (List<ExtEventOrmDetails>) getWrapper().getExtEventOrmDetails();

for (int k = 0; k < liste.size(); k++) {
ExtEventOrmDetails val1 = liste.get(k);
utilizeamt = val1.getUTILIZED();
availamt = val1.getAVAILABL();
utilizeCcy = val1.getUTILIZEDCurrency();
balanceCcy = val1.getAVAILABLCurrency();
if (utilizeamt.compareTo(availamt) == 1) {
      System.out.println(" inside available amount validation in idc /ilc");
      validationDetails.addWarning(WarningType.Other,
                  "Advance payment utillization  amount should not greater than available amount in advance table [CM]");
}
if (balanceCcy != null && !balanceCcy.trim().equals("") && utilizeCcy != null
            && !utilizeCcy.trim().equals("")) {
      if (!balanceCcy.equalsIgnoreCase(utilizeCcy)) {
            System.out.println("Available: " + balanceCcy + " " + utilizeCcy);
            validationDetails.addWarning(WarningType.Other,
                        "Available Amount  and utilized Amount Currency Mismatch.");
      }
}
}
}

// Currency calculation added by Vishal G for tenorperiodwarning //170523
public double getCurrencyRate(String masCurr) {
double rate = 0.0;
double usdRate = 0.0;
double converRate = 0.0;
try {
con = ConnectionMaster.getConnection();
String query = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='" + masCurr + "'";
String query1 = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='USD'";
ps2 = con.prepareStatement(query);
rs2 = ps2.executeQuery();
while (rs2.next()) {
      rate = Double.parseDouble(rs2.getString(1));
}
ps3 = con.prepareStatement(query1);
rs3 = ps3.executeQuery();
while (rs3.next()) {
      usdRate = Double.parseDouble(rs3.getString(1));
      System.out.println("USD rate: " + usdRate);
}
converRate = rate / usdRate;
} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, ps2, rs2);
ConnectionMaster.surrenderDB(con, ps3, rs3);
}
return converRate;
}

// Tenor warning added by Vishal G for tenorperiodwarning //170523
public void getTenorWarning(ValidationDetails validationDetails) {

String tenor = getDriverWrapper().getEventFieldAsText("TNRD", "i", "");
String importOperating = getDriverWrapper().getEventFieldAsText("APP", "p", "cBYF");
String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
int imptrade = 0;
int tenorperiod = 0;

if ((subproCode.equalsIgnoreCase("01F") || subproCode.equalsIgnoreCase("02F")
      || subproCode.equalsIgnoreCase("03F") || subproCode.equalsIgnoreCase("04F")
      || subproCode.equalsIgnoreCase("11F") || subproCode.equalsIgnoreCase("12F")
      || subproCode.equalsIgnoreCase("13F") || subproCode.equalsIgnoreCase("14F")
      || subproCode.equalsIgnoreCase("21F") || subproCode.equalsIgnoreCase("22F"))) {
try {

      // Loggers.general().info(LOG,"query " + query);
      System.out.println(" IMPTRADE query: " + tenor + " " + subproCode + " " + importOperating);

      if (!importOperating.equals("") && !importOperating.isEmpty() && !tenor.equals("")
                  && !tenor.isEmpty()) {
            imptrade = Integer.parseInt(importOperating);
            tenorperiod = Integer.parseInt(tenor);
      }
      if (tenorperiod > imptrade) {
            System.out.println(" tenor period and import trade value " + tenorperiod + " " + imptrade);
            validationDetails.addWarning(WarningType.Other,
                        "Tenor Period is greater than Customer Import Operating Trade Cycle " + imptrade
                                    + " days (CM)");
      }
} catch (Exception e) {
      System.out.println("Exception update of tenor" + e.getMessage());
} finally {
      ConnectionMaster.surrenderDB(con, ps, rs);

}
}
}

//risk country added by Vishal G for risk country //120723
public void getCountryRisk() {
String risk = "";
String Country = "";
if (getMajorCode().equalsIgnoreCase("ODC") || getMajorCode().equalsIgnoreCase("CPCI")
      || getMajorCode().equalsIgnoreCase("CPBI")) {
Country = getDriverWrapper().getEventFieldAsText("BUY", "p", "cc");
}
if (getMajorCode().equalsIgnoreCase("IDC")) {
Country = getDriverWrapper().getEventFieldAsText("SEL", "p", "cc");
}
if (getMajorCode().equalsIgnoreCase("ELC")) {
Country = getDriverWrapper().getEventFieldAsText("APP", "p", "cc");
}
if (getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("CPCO")
      || getMajorCode().equalsIgnoreCase("CPBO") || getMajorCode().equalsIgnoreCase("IGT")) {
Country = getDriverWrapper().getEventFieldAsText("BEN", "p", "cc");
}
try {
con = getConnection();
String coutryrisk = "select RISKCODE,COUNTRY from extcountry where  COUNTRY='" + Country + "'";
System.out.println("coutryrisk query " + coutryrisk);

ps = con.prepareStatement(coutryrisk);

rs = ps.executeQuery();
while (rs.next()) {
      risk = rs.getString(1);
      System.out.println("Status on validate reservation==>" + risk);
      getPane().setOTHERPAR(risk);
}
} catch (Exception e) {
System.out.println("Exception update in country" + e.getMessage());
} finally {
ConnectionMaster.surrenderDB(con, ps, rs);
ConnectionMaster.surrenderDB(con, ps1, rs1);
ConnectionMaster.surrenderDB(con, ps2, rs2);

}
}

// Rapay amount validation 04/10/2023 By Vishal G
public void getRapayAmt(ValidationDetails validationDetails) {
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String error = null;
String remark = null;
con = ConnectionMaster.getConnection();
try {
String payQuery = "SELECT E_OR_W,REMARKS FROM REP_SB_REPAY_VALIDATION WHERE BILLREFNO='" + masterref
            + "' AND PAY_SERIAL_NO='" + eventCode + "'";
ps1 = con.prepareStatement(payQuery);
rs1 = ps1.executeQuery();
if (rs1.next()) {
      error = rs1.getString(1);
      remark = rs1.getString(2);
      System.out.println("Rapay amt error warning :" + payQuery + " " + error + " " + remark);

}
if (error.trim().equalsIgnoreCase("E")) {
      validationDetails.addError(ErrorType.Other, "" + remark);
} else if (error.trim().equalsIgnoreCase("W")) {
      validationDetails.addWarning(WarningType.Other, "" + remark);
}
} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, ps1, rs1);

}

}

// boe pending warning added by Vishal G for //170723
public void getBOEWarning(ValidationDetails validationDetails) {

String Customer = "";
String noofbills = "";
String amount = "";
String oldestbillorm = "";
String date = "";
String largestbillorm = "";
String amountininr = "";
if (getMajorCode().equalsIgnoreCase("IDC")) {
Customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");
}

if (getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IGT")) {
Customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");
}
if (getMajorCode().equalsIgnoreCase("CPCO") || getMajorCode().equalsIgnoreCase("CPBO")) {
Customer = getDriverWrapper().getEventFieldAsText("RMIT", "p", "cu");
}
try {
con = getConnection();
String boequery = "select BOE_OVDU_COUNT,BOE_OVDU_SUM_INR,REF_WHERE_MIN_DATE,TO_CHAR(MIN_IMPORT_BILL_REMITTANCE_PAYMENT_DATE,'YY-MM-DD'), "
            + " REF_WHERE_MAX_AMT ,MAX_IMP_BILL_REMIT_AMT_INR from REP_BOE_OVERDUE_CONSOL_TBL  where  CUSTOMER_ID='"
            + Customer + "'";
System.out.println("boewarning query " + boequery);

ps = con.prepareStatement(boequery);

rs = ps.executeQuery();
while (rs.next()) {
      noofbills = rs.getString(1);
      amount = rs.getString(2);
      oldestbillorm = rs.getString(3);
      date = rs.getString(4);
      largestbillorm = rs.getString(5);
      amountininr = rs.getString(6);
      System.out.println("Status on validate reservation==>" + noofbills + " " + amount + " " + oldestbillorm
                  + " " + date + " " + amountininr);
      validationDetails.addWarning(WarningType.Other,
                  "BOE OVERDUE STATUS Within Finastra \n" + "No. of Bills/ORM -" + noofbills
                              + " Total Amount in INR-" + amount + " \n" + "Oldest Bill/ORM-" + oldestbillorm
                              + " Dated-" + date + " \n" + "Largest Bll/ORM-" + largestbillorm + " Amount in INR-"
                              + amountininr);
}
} catch (Exception e) {
e.printStackTrace();
System.out.println("Exception update in boe warning" + e.getMessage());
} finally {
ConnectionMaster.surrenderDB(con, ps, rs);

}
}

// uid available amount warning added by Vishal G for //250823
public void getUidWarning(ValidationDetails validationDetails) {
String availamount = "";
String uid = getDriverWrapper().getEventFieldAsText("cBUB", "s", "");
String tranamt = "";
con = ConnectionMaster.getConnection();
BigDecimal availamountbd = new BigDecimal(0);
BigDecimal tranamtbd = new BigDecimal(0);
if (!uid.trim().isEmpty() && !uid.equalsIgnoreCase("na") && uid != null) {
try {
      String amountquery = "SELECT AVBL_AMOUNT,CCY as availableAmount from UID_CREDIT_DAILY_TRAN_TBL where UNIQUE_ID='"
                  + uid + "' ORDER BY ROW_SEQ DESC fetch first 1 row only";
      ps1 = con.prepareStatement(amountquery);
      rs1 = ps1.executeQuery();
      if (rs1.next()) {
            availamount = rs1.getString(1);
            System.out.println("aVAILABLE AMOUNT " + availamount);

      }
} catch (Exception e) {
      e.printStackTrace();
} finally {
      ConnectionMaster.surrenderDB(con, ps1, rs1);

}
if (getMajorCode().equalsIgnoreCase("ELC") || getMajorCode().equalsIgnoreCase("ODC")) {
      tranamt = getDriverWrapper().getEventFieldAsText("cABI", "v", "m");
}
if (getMajorCode().equalsIgnoreCase("CPCI") || getMajorCode().equalsIgnoreCase("CPBI")) {
      tranamt = getDriverWrapper().getEventFieldAsText("DRAM", "v", "m");

}
}
if (!tranamt.trim().isEmpty() && tranamt != null && !availamount.trim().isEmpty() && availamount != null) {
availamountbd = new BigDecimal(availamount);
tranamtbd = new BigDecimal(tranamt);
System.out.println("uid validation values " + availamount + " " + availamountbd + " " + tranamtbd);
if (tranamtbd.compareTo(availamountbd) == 1) {
      validationDetails.addError(ErrorType.Other, "Transaction amount(" + tranamtbd
                  + ") is gretaer than available amount(" + availamountbd + ")");
}
}
}

// Limit overutilisation warning added by Vishal G //170823
public void getLimitError(ValidationDetails validationDetails) {
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String todayDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
String tranamount = getDriverWrapper().getEventFieldAsText("MAL", "v", "m");
String tranCurrency = getDriverWrapper().getEventFieldAsText("MAL", "v", "c");
String amdamount = getDriverWrapper().getEventFieldAsText("IML", "v", "m");
String amdCurrency = getDriverWrapper().getEventFieldAsText("IML", "v", "c");
String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
String postrate = getDriverWrapper().getEventFieldAsText("cASE", "s", "");
String event = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
String principal = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
String principalfORiDC = getDriverWrapper().getEventFieldAsText("PRM", "p", "cu");
String stepPhase = getDriverWrapper().getEventFieldAsText("SPHC", "s", "");
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
String rcfa = getDriverWrapper().getEventFieldAsText("RCFA", "l", "");
// POC AMOUNT
String pocamount = getDriverWrapper().getEventFieldAsText("FEA", "v", "m").trim();
//exposure amount
String expAmount = getDriverWrapper().getEventFieldAsText("OMLI", "v", "m");

// NEW values for poc
String poc1 = getDriverWrapper().getEventFieldAsText("ALIA", "v", "m").trim();
String poc2 = getDriverWrapper().getEventFieldAsText("BLIA", "v", "m").trim();
String amount = null;
String facility = null;
String facilityiss = null;
String expiry = null;
String result = null;
String currency = null;
String subfacility = null;
String customer = null;
BigDecimal tranamt = BigDecimal.ZERO;
BigDecimal amdamountbd = BigDecimal.ZERO;
BigDecimal tranamountbd = BigDecimal.ZERO;
BigDecimal pocamountbd = BigDecimal.ZERO;
BigDecimal pocbd1 = BigDecimal.ZERO;
BigDecimal pocbd2 = BigDecimal.ZERO;
int count1 = 2;
int count2 = 2;

Connection connection = null;
PreparedStatement preparedStatement = null;
ResultSet resultSet = null;
try {
conn = getConnection();
String sql1 = "{call LIMIT_PROCEDURE(?,?,?,?,?,?,?,?,?,?,?,?,?)}";

CallableStatement stmt1 = conn.prepareCall(sql1);

stmt1.setString(1, masterref);
stmt1.setString(2, eventCode);
stmt1.setString(3, subproCode);
stmt1.registerOutParameter(4, Types.VARCHAR);
stmt1.registerOutParameter(5, Types.VARCHAR);
stmt1.registerOutParameter(6, Types.VARCHAR);
stmt1.registerOutParameter(7, Types.VARCHAR);
stmt1.registerOutParameter(8, Types.VARCHAR);
stmt1.registerOutParameter(9, Types.VARCHAR);
stmt1.registerOutParameter(10, Types.VARCHAR);
stmt1.registerOutParameter(11, Types.VARCHAR);
stmt1.registerOutParameter(12, Types.INTEGER);
stmt1.registerOutParameter(13, Types.INTEGER);

stmt1.execute();
facility = stmt1.getString(4);
facilityiss = stmt1.getString(5);
amount = stmt1.getString(6);
expiry = stmt1.getString(7);
currency = stmt1.getString(8);
customer = stmt1.getString(9);
subfacility = stmt1.getString(10);
result = stmt1.getString(11);
count1 = stmt1.getInt(12);
count2 = stmt1.getInt(13);

System.out.println("quer limit procedure " + facility + " " + amount + " " + expiry + " " + count1 + " "
            + count2 + " " + facilityiss);

// Kalpana Ghorpade added lines as per bank requirement to check table weather
// master ref is present in table or not
connection = getConnection();
String query = "select MASREF AS MASTERREFERENCE from UBZONE.EXTMASEXCL where MASREF LIKE '%" + masterref
            + "%'";
preparedStatement = connection.prepareStatement(query);
//String param="%"+masterref+"%";
//preparedStatement.setString(1, param);
resultSet = preparedStatement.executeQuery();
String masterReference = null;
while (resultSet.next()) {
      masterReference = resultSet.getString("MASTERREFERENCE");
      System.out.println("masterRef from UBZONE.EXTMASEXCL:::::::::" + masterReference);
}
if (event.equalsIgnoreCase("AMD") || event.equalsIgnoreCase("POC")) {// || event.equalsIgnoreCase("POC") ||
                                                                                                            // event.equalsIgnoreCase("CRE")
//          || event.equalsIgnoreCase("CAC")) {
      System.out.println("rcfa::::::" + rcfa);
      if (rcfa.equalsIgnoreCase("Y")) {
            System.out.println("rcfa::::::" + rcfa);
//    amdamountbd = new BigDecimal(amdamount);
//    System.out.println("amdamountbd" + amdamountbd);
//    if (amdamountbd.compareTo(BigDecimal.ZERO) == 1) {
            if (facility != null && !facility.trim().isEmpty()) {// &&facilityiss!=null
                                                                                                // &&!facilityiss.trim().isEmpty()
                  // condition exposure amount in debit then attach limit error

                  if (!facility.equalsIgnoreCase(facilityiss)) {
                        System.out.println("facility of issue and amend " + facilityiss + " " + facility);
                        if (result.equalsIgnoreCase("E")) {

//                            System.out.println(
//                                        "error warning result " + result + " " + postrate + " " + principal);
//                            validationDetails.addError(ErrorType.Other, "Limit Node should not be changed ");

                              if (masterReference == null) {
                                    System.out.println("masterRef" + masterReference);

                                    System.out.println(
                                                "error warning result " + result + " " + postrate + " " + principal);
                                    validationDetails.addError(ErrorType.Other, "Limit Node should not be changed ");
                              } else {
                                    System.out.println(
                                                "error warning result " + result + " " + postrate + " " + principal);
                                    validationDetails.addWarning(WarningType.Other,
                                                "Limit Node should not be changed ");
                              }
                        }

                        if (result.equalsIgnoreCase("W")) {
                              validationDetails.addWarning(WarningType.Other, "Limit Node should not be changed ");
                        }

                  }
            }
      }
}
//          else {
if (event.equalsIgnoreCase("AMD")) {
      if (amdamount == null || amdamount.isEmpty()) {
            amdamount = "0";
      }
      amdamountbd = new BigDecimal(amdamount);
      System.out.println("amdamountbd" + amdamountbd);
      if (amdamountbd.compareTo(BigDecimal.ZERO) == 1) {
            if (facility == null || facility.trim().isEmpty()) {// &&facilityiss==null
                                                                                          // &&facilityiss.trim().isEmpty()
                  if (result.equalsIgnoreCase("E")) {
                        // newly added shubham pathak
                        if (masterReference == null) {
                              System.out.println("masterRef" + masterReference);
                              System.out.println("error warning result " + result + " " + postrate + " " + principal);
                              validationDetails.addError(ErrorType.Other, "Please attach limit for the transaction ");
                        } else {
                              System.out.println("error warning result " + result + " " + postrate + " " + principal);
                              validationDetails.addWarning(WarningType.Other,
                                          "Please attach limit for the transaction ");
                        }
//                      System.out.println("error warning result " + result + " " + postrate + " " + principal);
//                      validationDetails.addError(ErrorType.Other, "Please attach limit for the transaction");
                  }

                  if (result.equalsIgnoreCase("W")) {
                        validationDetails.addWarning(WarningType.Other, "Please attach limit for the transaction");
                  }

            }
      }
}
//    }
//
//}

java.util.Date tranDate = sdf.parse(todayDate);
java.util.Date expiryDate = sdf.parse(expiry);

if (expiryDate.after(tranDate)) {

} else {
      long timeDiff = Math.abs(tranDate.getTime() - expiryDate.getTime());
      long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
      System.out.println(
                  "The of days between dates " + daysDiff + " " + tranDate + " " + expiryDate + " " + result);
      if (daysDiff == 180) {

            System.out.println("Limit has been expired but limit not renewed.");
      }
      if (daysDiff > 180) {
            if (result.equalsIgnoreCase("E")) {
                  // newly added shubham pathak

                  if (masterReference == null) {
                        System.out.println("masterRef" + masterReference);
                        System.out.println("error warning result " + result + " " + postrate + " " + principal);
                        validationDetails.addError(ErrorType.Other, "Limit has not renewed for more than 180days ");
                  } else {
                        System.out.println("error warning result " + result + " " + postrate + " " + principal);
                        validationDetails.addWarning(WarningType.Other,
                                    "Limit has not renewed for more than 180days ");
                  }
                  // validationDetails.addError(ErrorType.Other, "Limit has not renewed for more
                  // than 180days");
            }

            if (result.equalsIgnoreCase("W")) {
                  validationDetails.addWarning(WarningType.Other, "Limit has not renewed for more than 180days");
            }

      } else {
            validationDetails.addWarning(WarningType.Other, "Limit has been expired but limit not renewed.");

      }
}
//if (event.equalsIgnoreCase("AMD")) {

if (event.equalsIgnoreCase("AMD")) {
//    if (amdCurrency.equalsIgnoreCase("JPY")) {
//          System.out.println("amdCurrency::::::"+amdCurrency);
//          amdamountbd = amdamountbd.multiply(new BigDecimal("100"));
//          System.out.println("amdamountbd:::::::"+amdamountbd);
//    }
      // handling null pointer exception
      if (amdamount == null || amdamount.isEmpty()) {
            amdamount = "0";
      }
      amdamountbd = new BigDecimal(amdamount);
      tranamt = amdamountbd.multiply(new BigDecimal(postrate));
} else if (event.equalsIgnoreCase("POC") || event.equalsIgnoreCase("CAC")) {
      System.out.println("exposure amount for poc :::::" + pocamount);
      if (poc1 == null || poc1.isEmpty()) {
            poc1 = "0";
      }
      if (poc2 == null || poc2.isEmpty()) {
            poc2 = "0";
      }
      pocbd1 = new BigDecimal(poc1);
      pocbd2 = new BigDecimal(poc2);
      pocamountbd = pocbd2.subtract(pocbd1);
      if (pocamountbd.compareTo(BigDecimal.ZERO) == 1) {
            tranamt = pocamountbd.multiply(new BigDecimal(postrate));
      } else {
            tranamt = new BigDecimal(0);
            tranamt = pocamountbd.multiply(new BigDecimal(postrate));
      }
//    tranamt=new BigDecimal(pocamount);

} else {
      if (expAmount == null || expAmount.isEmpty()) {
            expAmount = "0";
      }
      tranamountbd = new BigDecimal(expAmount);
      System.out.println("exposure amount:::::" + tranamountbd);
//    if (tranCurrency.equalsIgnoreCase("JPY")) {
//          System.out.println("tranCurrency:::::"+tranCurrency);
//          tranamountbd = tranamountbd.multiply(new BigDecimal("100"));
//          System.out.println("tranamountbd:::::"+tranamountbd);
//    }
      tranamt = tranamountbd.multiply(new BigDecimal(postrate));
}
//if (event.equalsIgnoreCase("AMD")||(event.equalsIgnoreCase("POC"))) {
//    tranamt = amdamountbd.multiply(new BigDecimal(postrate));
//} else {
//    tranamt = new BigDecimal(tranamount).multiply(new BigDecimal(postrate));
//}
System.out.println("limit_amount::::::" + amount);
BigDecimal limitavailamt = new BigDecimal(amount);

System.out.println("tranamt :: " + tranamt);
System.out.println("limitavailamt :: " + limitavailamt);
tranamt = tranamt.setScale(3, RoundingMode.DOWN);
limitavailamt = limitavailamt.setScale(3, RoundingMode.DOWN);
if (tranamt.stripTrailingZeros().compareTo(limitavailamt) == 1) {
      if (result.equalsIgnoreCase("E")) {
            // newly added shubham pathak
            if (masterReference == null) {
                  System.out.println("masterRef" + masterReference);
                  System.out.println("error warning result " + result + " " + postrate + " " + principal);
                  validationDetails.addError(ErrorType.Other,
                              "Transaction amount is greater than available limit ");
            } else {
                  System.out.println("error warning result " + result + " " + postrate + " " + principal);
                  validationDetails.addWarning(WarningType.Other,
                              "Transaction amount is greater than available limit ");
            }

            // validationDetails.addError(ErrorType.Other, "Transaction amount is greater
            // than available limit ");
      }

      if (result.equalsIgnoreCase("W")) {
            validationDetails.addWarning(WarningType.Other,
                        "Transaction amount is greater than available limit ");
      }

}
if (event.equalsIgnoreCase("ISS") || event.equalsIgnoreCase("CRE") || event.equalsIgnoreCase("CAC")) {
      if (!currency.equalsIgnoreCase("INR")) {
            if (result.equalsIgnoreCase("E")) {
                  // newly added shubham pathak
                  if (masterReference == null) {
                        System.out.println("masterRef" + masterReference);
                        System.out.println("error warning result " + result + " " + postrate + " " + principal);
                        validationDetails.addError(ErrorType.Other, "Facility Id Currency should be INR ");
                  } else {
                        System.out.println("error warning result " + result + " " + postrate + " " + principal);
                        validationDetails.addWarning(WarningType.Other, "Facility Id Currency should be INR ");
                  }
                  // validationDetails.addError(ErrorType.Other, "Facility Id Currency should be
                  // INR");
            }

            if (result.equalsIgnoreCase("W")) {
                  validationDetails.addWarning(WarningType.Other, "Facility Id Currency should be INR");

            }

      }
}
//To be applicable only for Issue Event (first 3 letters of EventReference as ISS)
// || event.equalsIgnoreCase("AMD") remove as :18-01-2025
if (event.equalsIgnoreCase("ISS") || event.equalsIgnoreCase("CRE") || event.equalsIgnoreCase("CAC")) {
      if (event.equalsIgnoreCase("CAC")) {
            System.out.println("principalfORiDC:::::" + principalfORiDC);
            if (!principalfORiDC.equalsIgnoreCase(customer)) {
                  validationDetails.addError(ErrorType.Other,
                              "Principal party and Limit party are not same.(CM)");
            }
      } else {
            if (!principal.equalsIgnoreCase(customer)) {
                  validationDetails.addError(ErrorType.Other,
                              "Principal party and Limit party are not same.(CM)");
            }
      }
      if (count1 > 0) {
            System.out.println("sub facility code :" + subfacility + " ");

            if (count2 < 1) {

                  if (masterReference == null) {
                        System.out.println("masterRef" + masterReference);

                        System.out.println("error warning result " + result + " " + postrate + " " + principal);
                        validationDetails.addError(ErrorType.Other, "Facility id is not valid for Product Type. ");
                  } else {
                        System.out.println("error warning result " + result + " " + postrate + " " + principal);
                        validationDetails.addWarning(WarningType.Other,
                                    "Facility id is not valid for Product Type. ");
                  }
//          validationDetails.addError(ErrorType.Other, "Facility id is not valid for Product Type.");
            }

      }
}

stmt1.close();
conn.close();

} catch (Exception e) {
System.out.println("Exception limit code in country" + e.getMessage());
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, ps, rs);
ConnectionMaster.surrenderDB(con, ps2, rs2);
ConnectionMaster.surrenderDB(con, ps1, rs1);
ConnectionMaster.surrenderDB(con, ps3, rs3);
ConnectionMaster.surrenderDB(con, pes, res);
ConnectionMaster.surrenderDB(con, dmsp, dmsr);
ConnectionMaster.surrenderDB(connection, preparedStatement, resultSet);
}
}

// Delayed Shipping Bill Realisation method added by Vishal G //130923
public void getRealisationCharges() {
try {
System.out.println("###getRealisationCharges");
String issue = getDriverWrapper().getEventFieldAsText("ISS", "d", "");

String draftDue = getDriverWrapper().getEventFieldAsText("FCO:sMD", "d", "");

String todays = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
String nostro = getDriverWrapper().getEventFieldAsText("cBCJ", "d", "");
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
String maxRealisationDate = null;
String dueDate = null;
java.util.Date nostDate = null;
java.util.Date due = null;
java.util.Date maxRealisation = null;
java.util.Date todayDate = null;
Calendar cal = Calendar.getInstance();
BigDecimal value = new BigDecimal(91.5);
int count = 0;

BigDecimal repaytyp = BigDecimal.ZERO;
List<ExtEventShippingCollections> shipTable = (List<ExtEventShippingCollections>) getWrapper()
            .getExtEventShippingCollections();
if (draftDue != null && !draftDue.trim().equalsIgnoreCase("") && !draftDue.isEmpty()) {
      cal.setTime(sdf.parse(draftDue));
      cal.add(Calendar.DATE, 30);
      maxRealisationDate = sdf.format(cal.getTime());
      due = sdf.parse(draftDue);
      maxRealisation = sdf.parse(maxRealisationDate);
      System.out.println("inside if of realisation " + due + " " + maxRealisation);
} else {
      cal.setTime(sdf.parse(issue));
      cal.add(Calendar.DATE, 25);
      dueDate = sdf.format(cal.getTime());
      due = sdf.parse(dueDate);
      cal.setTime(sdf.parse(dueDate));
      cal.add(Calendar.DATE, 30);
      maxRealisationDate = sdf.format(cal.getTime());
      maxRealisation = sdf.parse(maxRealisationDate);
      System.out.println("inside else of realisation " + due + " " + maxRealisation);
}

if (!nostro.trim().equalsIgnoreCase("") && !nostro.isEmpty() && nostro != null) {
      nostDate = sdf.parse(nostro);

} else {
      nostDate = sdf.parse(todays);
}
//    for (int i = 0; i < shipTable.size(); i++) {
//          ExtEventShippingCollections ship = shipTable.get(i);
//          
//    BigDecimal repayAmt=ship.getCREPAY();
//    String repayType=ship.getREPTYP();
//    repaytyp=new BigDecimal(repayType);
//    if(!repayAmt.equals("")&&!repayAmt.equals(null)){
//    if (repayAmt.compareTo(BigDecimal.ZERO) > 0
//                && repaytyp.compareTo(new BigDecimal(2)) == 0) {
//          count=count+1;
//    }
//    }
//    }
long timeDiff = Math.abs(nostDate.getTime() - due.getTime());
long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
System.out.println("The of days between dates " + daysDiff + " " + count + " " + repaytyp);

BigDecimal daysDiffdb = new BigDecimal(daysDiff);
// BigDecimal noofbillsbd=new BigDecimal(count);

BigDecimal quarter = daysDiffdb.divide(value, RoundingMode.UP);
todayDate = sdf.parse(todays);
if (todayDate.after(maxRealisation)) {
      BigDecimal charges = quarter;
      System.out.println("max charges " + quarter + " " + charges);
      // getPane().setDELAYEDR(charges.toString());
}

} catch (Exception e) {
e.printStackTrace();
// System.out.println("fcct data:"+e);

}
}

// Behalf of branch validation 15/09/2023 By Vishal G
public void getPostingBranch(ValidationDetails validationDetails) {
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String branch = null;
con = ConnectionMaster.getConnection();
try {
String branchQuery = "SELECT branch FROM ETT_POSTING_BRANCH_VIEW WHERE MASTER_REF='" + masterref
            + "' AND EVENTREFNO='" + eventCode + "'";
ps1 = con.prepareStatement(branchQuery);
rs1 = ps1.executeQuery();
if (rs1.next()) {
      branch = rs1.getString(1);
      System.out.println("Behalf of branch :" + branchQuery + " " + branch);

}
if (!branch.trim().equalsIgnoreCase("") && !branch.isEmpty() && branch != null) {
      validationDetails.addError(ErrorType.Other,
                  "The Office Account/Customer Account utilised does not belong to Behalf of Branch");
}
} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, ps1, rs1);

}

}
//public void fxcontractValidation() {
//try {
//String result_val = null;
//int count = 0;
//String matched="";
//con = getConnection();
//getPane().setNOOFINVS("");
//String mas = getDriverWrapper().getEventFieldAsText("MST", "r", "");
//System.out.println("Master Reference on validate==>" + mas);
//String evt1 = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
//String fxcontract="select distinct(fxc.REFERENCE),mas.MASTER_REF from master mas,baseevent bev,relitem rel, "+
//          "fxbasedeal fx,fxcontract fxc "+
//          "where fxc.key97=fx.FXCONTRACT "+
//          "and fx.key97=rel.key97 "+
//          "and rel.event_key=bev.key97 "+
//          "and bev.master_key=mas.key97 "+
//          "and substr(fxc.reference,7,1)='N' "+
//          "and mas.master_ref='"+mas+"' "+
//          "and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+evt1+"'";
//System.out.println(" fx contract sql "+fxcontract);
//
//ps = con.prepareStatement(fxcontract);
//
//rs = ps.executeQuery();
//while (rs.next()) {
//    result_val = rs.getString(1);
//    System.out.println("Status on fxcontract reservation==>" + result_val);
//
//
//String transactionType="select TRANSACTION_TYPE from UBIFT.CUST_NPI_DATA@PROD_FINTRDB WHERE NPI_REF_NUM = '"+result_val+"'"
//                        + " and status = 'ACCEPTED'"  ;
//
//System.out.println("transaction type "+transactionType);
//
//    ps1 = con.prepareStatement(transactionType);
//
//    rs1 = ps1.executeQuery();
//    if (rs1.next()) {
//          String type = rs1.getString(1);
//          System.out.println("Status on fxcontract transaction==>" + type);
//    
//    if ((getMajorCode().equalsIgnoreCase("IDC") || getMajorCode().equalsIgnoreCase("ILC")
//          || getMajorCode().equalsIgnoreCase("CPCO") || getMajorCode().equalsIgnoreCase("CPBO")
//          || getMajorCode().equalsIgnoreCase("IGT"))) {
//          
//          if(!type.equalsIgnoreCase("NIRT")) {
//                
//                getPane().setNOOFINVS("UNMATCHED");
//          }
//    }
//    else if ((getMajorCode().equalsIgnoreCase("ODC") || getMajorCode().equalsIgnoreCase("ELC")
//          || getMajorCode().equalsIgnoreCase("CPCI") || getMajorCode().equalsIgnoreCase("CPBI"))) {
//          
//          if(!type.equalsIgnoreCase("NORT")) {
//                
//                getPane().setNOOFINVS("UNMATCHED");
//          }
//    }
//    }
//    
//    
//    matched=getPane().getNOOFINVS();
//    if (!matched.equalsIgnoreCase("") && matched.equalsIgnoreCase("UNMATCHED")) {
//          System.out.println("Status on fxcontract transaction status==>" + matched);
//          break;
//          }
//
//}
//}
//catch(Exception e){
//System.out.println("Exception update" + e.getMessage());
//}
//finally {
//ConnectionMaster.surrenderDB(con, ps, rs);
//ConnectionMaster.surrenderDB(con, ps1, rs1);
//
//  
//}
//}

/*
* public int getSWIFTSFMS(String Branch) { //ValidationDetails
* validationDetails=new ValidationDetails(); int value=0; try{ String
* Swiftsfms=getPane().getSWIFSFMS(); String Branchcode = Branch.substring(4,
* 6); Loggers.general().info(LOG,"Substring code"+Branchcode); String
* TFM=getDriverWrapper().getEventFieldAsText("TFM", "s", "").trim();
* Loggers.general().info(LOG,"TFM---------"+TFM); if(TFM.equalsIgnoreCase("4"))
* { if(Branchcode!=null && Swiftsfms!=null) {
* if(Branchcode.equalsIgnoreCase("XZ")||Branchcode.equalsIgnoreCase("XY")) {
* Loggers.general().info(LOG,"sfms"); if(!Swiftsfms.equalsIgnoreCase("SFMS")) {
* Loggers.general().info(LOG,"Substring code1");
* //validationDetails.addError(ErrorType.Other,
* "Please select SFMS in Swift/Sfms"); value=1; } } else{
* Loggers.general().info(LOG,"swift"); if(!Swiftsfms.equalsIgnoreCase("SWIFT"))
*
* { Loggers.general().info(LOG,"Substring code2");
* //validationDetails.addError(ErrorType.Other,
* "Please select SWIFT in Swift/Sfms"); value=2; }
*
* } } } } catch(Exception e) {
* Loggers.general().info(LOG,"Exception in swift sfms"+e.getMessage()); }
* return value; }
*/
/*
* public int getPreshipment(){ int value=0; String loanRef=""; BigDecimal
* Amount=null; String ccy="";
* Loggers.general().info(LOG,"Invoice details size-------------> "+value); try{
* Loggers.general().info(LOG,"Invoice details size-------------> "+value);
* List<ExtEventLoanDetails> LoanDet = (List<ExtEventLoanDetails>)
* getWrapper().getExtEventLoanDetails();
* Loggers.general().info(LOG,"LoadDetails"+ LoanDet); int
* loancount=LoanDet.size();
* Loggers.general().info(LOG,"Invoice details size-------------> "+loancount);
* for (int l = 0; l < LoanDet.size(); l++) { ExtEventLoanDetails loandetails =
* LoanDet.get(l); BigDecimal pre_out=null; //
* invnum=invnum+invoicedetails.getINVNUMB().trim();
* loanRef=loandetails.getDEALREF().trim(); Amount=loandetails.getREAMOUNT();
*
* ccy=loandetails.getREAMOUNTCurrency();
* Loggers.general().info(LOG,"Dealreference-------------> "+loanRef);
* Loggers.general().info(LOG,"Loan Amount-------------> "+Amount);
* Loggers.general().info(LOG,"Loan Amount currency-------------> "+ccy); String
* checkOut="select AMT_O_S from master,C8PF c8" +
* " where C8.C8CCY= MASTER.CCY AND master_ref='"
* +loanRef+"' and refno_pfix<>'NEW'";
* Loggers.general().info(LOG,"Loan Amount outstanding query-------------> "
* +checkOut); ps1 = con.prepareStatement(checkOut); rs1 = ps1.executeQuery();
* if (rs1.next()) { pre_out=rs1.getBigDecimal(1);
* Loggers.general().info(LOG,"Amount in query "+pre_out); } int res=0;
* res=pre_out.compareTo(Amount); if(res == -1) {
* Loggers.general().info(LOG,"Amount less than outstanding"); value=1; }
*
*
* } } catch(Exception e) { e.printStackTrace();
* Loggers.general().info(LOG,"Exception in preshipment subproduct===>" +
* e.getMessage()); } finally { try { if (rs1 != null) rs1.close(); if (ps1 !=
* null) ps1.close(); if (con != null) con.close(); } catch (SQLException e) {
* // Loggers.general().info(LOG,"Connection Failed! Check output // console");
* e.printStackTrace(); } } return value; }
*/

// Standing DMS link
public String getDmslink() {
LOG.info("DMSStaticLink");
System.out.println("DMSStaticLink");
String returns = "";
String strPropName = "DMSStaticLink";
String displayVal = "";
@SuppressWarnings("unchecked")
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
      .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
// //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
if (PROPCode != null) {
System.out.println("DMSStaticLink is not empty" + strPropName);
displayVal = PROPCode.getPropval();
} else {
System.out.println("DMSStaticLink is empty" + strPropName);

}

System.out.println("fetching DMSrefNumber ");
try {
String refNumber = getWrapper().getDMSREFNO_Name();
System.out.println("fetching DMSrefNumber " + refNumber);
LOG.info("DMSrefNumber - " + refNumber);
System.out.println("DMSrefNumber - " + refNumber);
returns = displayVal + refNumber;

} catch (Exception e) {
System.out.println("DMSrefNumber exception----->" + e.getMessage());
LOG.info(" DMSrefNumber exception----->" + e.getMessage());
}

return returns;

}

// Standing Repo link
public String getRepolink() {
LOG.info("DMSCustRepoLink");
System.out.println("DMSCustRepoLink");
String returns = "";
String strPropName = "DMSCustRepoLink";
String displayVal = "";
@SuppressWarnings("unchecked")
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
      .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
// //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
if (PROPCode != null) {
System.out.println("DMSCustRepoLink is not empty" + strPropName);
displayVal = PROPCode.getPropval();
} else {
System.out.println("DMSCustRepoLink is empty" + strPropName);

}

System.out.println("fetching CUSTID ");
try {
String custId = getDriverWrapper().getEventFieldAsText("PRM", "p", "cu").trim();
System.out.println("fetching custid " + custId);
LOG.info("custid - " + custId);
System.out.println("custid - " + custId);
returns = displayVal + custId;

} catch (Exception e) {
System.out.println("custId exception----->" + e.getMessage());
LOG.info(" custId exception----->" + e.getMessage());
}

return returns;

}

//----------------------------------------------------------------Yathish EBG---------------------------

public Double getStateWiseStampDuty(String stateCode) {

double stampValue = 0.00;
con = getConnection();
try {
String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
System.out.println("eventRef-> " + eventRef);
String fullamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");

System.out.println("fullamt-> " + fullamt);

String query = "SELECT COALESCE(STAMPDUTY,'Empty') AS STAMPDUTY  from EXTSTATE WHERE STATCOD = '"
            + stateCode.trim() + "'";
System.out.println("query :: -> " + query);
System.out.println("connection::: ->");
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
System.out.println("rs1 ->" + rs1);
while (rs1.next()) {

      String StampDuty = rs1.getString(1);

      System.out.println("StampDuty :::: " + StampDuty);
      if (StampDuty.equalsIgnoreCase("Empty")) {

            stampValue = getStampDutyAmount(stateCode, StampDuty, fullamt, eventRef);
            System.out.println("stampValue ::  " + stampValue);

      } else {
            stampValue = Double.parseDouble(StampDuty);
            System.out.println("stampValue ::  " + stampValue);
      }

}
} catch (Exception e) {
e.printStackTrace();
System.out.println("error::  " + e);
} finally {
ConnectionMaster.surrenderDB(con, ps1, rs1);

System.out.println("inside Finally block");
}

return stampValue;
}

public static Double getStampDutyAmount(String stateCode, String StampDuty, String amount, String eventRef) {
System.out.println("getStampDutyAmount");
double stampDuty = 0.00;

if (StampDuty.equalsIgnoreCase("Empty")) {
if (stateCode.trim().equalsIgnoreCase("MP")) {
      double percentage = 0.25;
      double findStampDuty = findStampDuty(amount, percentage);
      System.out.println("inside MP for testing -->> " + findStampDuty);
      if (findStampDuty >= 25000) {
            stampDuty = 25000.00;
      } else {
            stampDuty = findStampDuty;
      }

} else if (stateCode.trim().equalsIgnoreCase("RJ")) {
      if (eventRef.substring(0, 3).equalsIgnoreCase("ISS")) {
            System.out.println("----------ISS event in RJ------------");
            double percentage = 0.25;
            double findStampDuty = findStampDuty(amount, percentage);
            System.out.println("AMOUNT CONNECTIONMASTER" +amount);
            System.out.println("Value OF findStampDuty in ISS RJ=" +findStampDuty);
            if (findStampDuty >= 25000) {
                  System.out.println("ISS IF RJ");
                  stampDuty = 25000.00;
            } else {
                  System.out.println("ISS else RJ");
                  stampDuty = findStampDuty;
            }

      } else if (eventRef.substring(0, 3).equalsIgnoreCase("AMD")) {
            System.out.println("----------AMD event in RJ------------");
            double percentage = 0.25;
            double findStampDuty = findStampDuty(amount, percentage);
            System.out.println("AMOUNT CONNECTIONMASTER" +amount);
            System.out.println("Value OF findStampDuty in AMD RJ=" +findStampDuty);
            if (findStampDuty >= 1000) {
                  System.out.println("AMD IF RJ");
                  stampDuty = 1000.00;
            } else {
                  System.out.println("AMD else RJ");
                  stampDuty = findStampDuty;
            }

      }
      System.out.println("BEFORE StampDuty=" +stampDuty);
      double surcharge = Math.ceil(stampDuty*0.1);
      System.out.println("StampDuty=" +stampDuty);
      System.out.println("Value of surcharge=" +surcharge);
      surcharge = surcharge*3;
      System.out.println("Value of surcharge after multiplying by 3=" +surcharge);
      stampDuty = stampDuty + surcharge;
      System.out.println("Final stampDuty of RJ" +stampDuty);
      
} else if (stateCode.trim().equalsIgnoreCase("UP")) {
      double percentage = 0.5;
      double findStampDuty = findStampDuty(amount, percentage);
      if (findStampDuty >= 10000) {
            stampDuty = 10000.00;
      } else {
            stampDuty = findStampDuty;
      }

} else if (stateCode.trim().equalsIgnoreCase("UK")) {
      double percentage = 0.5;
      double findStampDuty = findStampDuty(amount, percentage);
      System.out.println("AMOUNT CONNECTIONMASTER" +amount);
      System.out.println("Value OF findStampDuty in UK=" +findStampDuty);
      if (findStampDuty >= 10000) {
            System.out.println("IF VAlue of stampDuty:" +findStampDuty);
            stampDuty = 10000.00;
      } else {
            System.out.println("ELSE VAlue of stampDuty:" +findStampDuty);
            stampDuty = findStampDuty;
      }

} else if (stateCode.trim().equalsIgnoreCase("MH")) {

      double baseAmount = Double.valueOf(amount);

      if (baseAmount < 1000000) {

            double percentage = 0.1;
            double findStampDuty = findStampDuty(amount, percentage);
            if (findStampDuty <= 100) {
                  stampDuty = 100.00;
            } else {
                  stampDuty = findStampDuty;
            }

      } else {
            double percentage = 0.2;
            double findStampDuty = findStampDuty(amount, percentage);
            stampDuty = findStampDuty;
      }

}

}
return stampDuty;

}

public static Double findStampDuty(String amount, double percentage) {

System.out.println("findStampDuty :: " + amount + " percentage :: " + percentage);

double baseValue = Double.valueOf(amount);
System.out.println(" baseValue :: " + baseValue);
double result = Math.ceil(((percentage / 100) * baseValue));
result = Double.parseDouble(String.format("%.2f", result));
System.out.println("0.25% of " + baseValue + " is: " + result);

System.out.println("ends findStampDuty" + result);
return result;
}            

}