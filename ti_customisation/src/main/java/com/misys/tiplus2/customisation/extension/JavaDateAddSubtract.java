package com.misys.tiplus2.customisation.extension;

//com.misys.tiplus2.customisation.extension.JavaDateAddSubtract;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class JavaDateAddSubtract extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(JavaDateAddSubtract.class);
      public boolean onPostInitialise() {
            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            //// Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  //// Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  //// PROPCode.getPropval());
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
            if (dailyval.equalsIgnoreCase("NO") && !eventStatus.equalsIgnoreCase("Completed")) {

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
                  // GETTING LOB
                  try {
                        //// Loggers.general().info(LOG,"get value for LOB");
                        getLob();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage() );
                        // //Loggers.general().info(LOG,"LOB Catch");
                  } finally {
                        //// Loggers.general().info(LOG,"finally LOB ");
                  }

                  String customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();// party
                  //// Loggers.general().info(LOG,"customera value for subvention " +
                  //// customera);

                  try {

                        con = ConnectionMaster.getConnection();
                        String dms = "select COLNG from extcust where cust ='" + customera + "'";
                        ps1 = con.prepareStatement(dms);
                        // Loggers.general().info(LOG,"Query" + dms);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              dmsstr = rs1.getString(1);
                              if (dmsstr != null) {
                                    dmsstr = dmsstr.trim();
                              }
                              getWrapper().setCOOLNG(dmsstr);
                              getPane().setCOOLNG(dmsstr);
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
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

                  // provitional due date calculation
                  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  Calendar c = Calendar.getInstance();
                  String todayd = getDriverWrapper().getEventFieldAsText("TDY", "d", ""); // 12/10/16
                  //// Loggers.general().info(LOG,"today date" + todayd);
                  String cooling = getWrapper().getCOOLNG();
                  // graceda="0";
                  int gra = 0;
                  // Loggers.general().info(LOG,"cooling value in int ----> " + gra);
                  try {
                        if (cooling.length() > 0) {
                              gra = Integer.parseInt(cooling);
                              c.setTime(sdf.parse(todayd));
                              //// Loggers.general().info(LOG,"todayd -------> ");
                              c.add(Calendar.DATE, gra);
                              // //Loggers.general().info(LOG,"DATE 1"+ c);
                              String output = sdf.format(c.getTime());
                              // Loggers.general().info(LOG,output);
                              // getWrapper().setPRVDUEDT(output);
                              getPane().setPRVDUEDT(output);
                        } else {
                              // Loggers.general().info(LOG,"else part of grace days");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exeception" + e);
                  }

                  // Actual due date
                  SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                  Calendar c1 = Calendar.getInstance();
                  String graceda = "0";
                  graceda = getWrapper().getGRACEPER();
                  //// Loggers.general().info(LOG,"Greace value in int ----> " + graceda);
                  int gra1 = 0;
                  try {
                        if (graceda.length() > 0) {
                              gra1 = Integer.parseInt(graceda);
                              String prodate = getWrapper().getPRVDUEDT(); // 12/10/16
                              //// Loggers.general().info(LOG,"Provisional Due date" + prodate);
                              c1.setTime(sdf1.parse(prodate));
                              //// Loggers.general().info(LOG,"prodate -------> ");
                              c1.add(Calendar.DATE, gra1);
                              // //Loggers.general().info(LOG,"DATE 1"+ c);
                              String output1 = sdf1.format(c1.getTime());
                              // Loggers.general().info(LOG,output1);
                              //getWrapper().setACTDUE(output1);
                              //getPane().setACTDUE(output1);
                        } else {
                              // Loggers.general().info(LOG,"else part of grace days");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exeception" + e);
                  }

            }

            return false;
      }

      public void onValidate(ValidationDetails validationDetails) {
            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            //// Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  //// Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  //// PROPCode.getPropval());
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
            if (dailyval.equalsIgnoreCase("NO")) {

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
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

                  // // KYC DATE VALIDATION
                  // try{
                  //
                  // //Loggers.general().info(LOG,"KYC Date Validation");
                  // String t= getDriverWrapper().getEventFieldAsText("PRI", "p",
                  // "cBEY");
                  // //Loggers.general().info(LOG,"value os first date " +t);
                  // String i = getDriverWrapper().getEventFieldAsText("ISS", "d",
                  // "");
                  // //Loggers.general().info(LOG,"value of issue date "+i);
                  // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  // java.util.Date date1 = sdf.parse(t.trim());
                  // java.util.Date date2 = sdf.parse(i.trim());
                  // if(date1.compareTo(date2)<0 && (step_Input.equalsIgnoreCase("i"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker"))){
                  // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                  // "KYC Expired for the Customer [CM]" );
                  // }
                  // }catch(Exception e){
                  // //Loggers.general().info(LOG,"Exception " + e.getMessage());
                  // }

                  // GETTING LOB
                  try {
                        // //Loggers.general().info(LOG,"get value for LOB");
                        getLob();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage() );
                        // //Loggers.general().info(LOG,"LOB Catch");
                  } finally {
                        //// Loggers.general().info(LOG,"finally LOB ");
                  }

                  // Actual due date
                  SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                  Calendar c1 = Calendar.getInstance();
                  String graceda = "0";
                  graceda = getWrapper().getGRACEPER();
                  // //Loggers.general().info(LOG,"Greace value in int ----> " + graceda);
                  int gra1 = 0;
                  try {
                        if (graceda.length() > 0) {
                              gra1 = Integer.parseInt(graceda);
                              String prodate = getWrapper().getPRVDUEDT(); // 12/10/16
                              //// Loggers.general().info(LOG,"Provisional Due date" + prodate);
                              c1.setTime(sdf1.parse(prodate));
                              //// Loggers.general().info(LOG,"prodate -------> ");
                              c1.add(Calendar.DATE, gra1);
                              // //Loggers.general().info(LOG,"DATE 1"+ c);
                              String output1 = sdf1.format(c1.getTime());
                              //// Loggers.general().info(LOG,output1);
                              //getWrapper().setACTDUE(output1);
                              //getPane().setACTDUE(output1);
                        } else {
                              // Loggers.general().info(LOG,"else part of grace days");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exeception" + e);
                  }

                  //
                  // try{
                  // String dt = getDriverWrapper().getEventFieldAsText("TDY","d","");
                  // // Start date
                  // ////Loggers.general().info(LOG,dt + " TI dt Date");
                  // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  // Calendar ca = Calendar.getInstance();
                  // ca.setTime(sdf.parse(dt));
                  // ca.add(Calendar.DATE, 25); // number of days to add
                  // dt = sdf.format(ca.getTime()); // dt is now the new date
                  // ////Loggers.general().info(LOG,dt);
                  // getWrapper().setODCGR(dt);
                  //
                  // }
                  // catch (Exception e) {
                  // //Loggers.general().info(LOG,"Exception is "+e.getMessage());
                  // ////Loggers.general().info(LOG,"Exception is error calender");
                  //
                  // }

                  // Notes Populated in Summary
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //

                  try {
                        con = getConnection();
                        String query = "select * from master mas,NOTE, TIDATAITEM tid WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 AND (NOTE.TYPE NOT IN (3,129,1089) or NOTE.TYPE is null) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
                                    + MasterReference + "' ";
                        // Loggers.general().info(LOG,"Notes Populated in Summary query====> " +
                        // query);

                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              String notes = rs1.getString(1);
                              if (notes.length() > 0 && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "Notes Populated in Summary. Kindly check [CM]");
                              }

                        }

                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception" + e1.getMessage());
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

                  // Category code populate based on input branch
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                        con = ConnectionMaster.getConnection();
                        if (!(BranchCode.length() == 0)) {
                              String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
                              // Loggers.general().info(LOG,"BranchCode Query - " + sql6);
                              ps1 = con.prepareStatement(sql6);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String inmt = rs1.getString(1);
                                    //// Loggers.general().info(LOG,"category code - " + inmt);
                                    getWrapper().setIMBRCODE(inmt);
                                    getPane().setIMBRCODE(inmt);
                              }

                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception caught on branch code
                        // validation......"+e.getMessage());
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

                  ///error configured for duplicate events
                  try {
                   int cnt=0;
                   Loggers.general().info(LOG,"duplicate reference");
                              cnt= getduplicate();
                               Loggers.general().info(LOG,"count"+cnt);
                              if(cnt>1)
                              {
                                     Loggers.general().info(LOG,"duplicate reference");
                                    validationDetails.addError(ErrorType.Other,
                                                "The event reference no already exsist kindly abort and recreate the event [CM]");
                              }
                        } catch (Exception ee) {
                              ee.printStackTrace();
                              Loggers.general().info(LOG,"duplicate reference" + ee.getMessage());

                        }
                  // // Charge Account Validation
                  // String prodtype = getDriverWrapper().getEventFieldAsText("PCO",
                  // "s", "");//pROD CODE ILC
                  // String priCustStr = "";
                  // try {
                  // if(prodtype.trim().equalsIgnoreCase("OCC"))
                  // { // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                  // priCustStr = getDriverWrapper().getEventFieldAsText("DRW","p",
                  // "no");//party id
                  // }
                  // String account = getDriverWrapper().getEventFieldAsText("PRI",
                  // "q", "RCA").trim();
                  // String ar[]=account.split("-");
                  // ////Loggers.general().info(LOG,"Splitted values"+ar[2]);
                  // String chargecol =
                  // getDriverWrapper().getEventFieldAsText("BOTC","l", "");
                  // ////Loggers.general().info(LOG,"charge account collected "+chargecol);
                  // if (priCustStr != null && ar[2].length()>0 &&
                  // chargecol.equalsIgnoreCase("Y") &&
                  // (!chargecol.equalsIgnoreCase("N")) ) {
                  //
                  // if((!priCustStr.trim().equalsIgnoreCase(ar[2].trim()))
                  // && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))){
                  // //Loggers.general().info(LOG,"custoemr number "+priCustStr.trim());
                  // //Loggers.general().info(LOG,"charge acc no" +ar[2].trim());
                  // validationDetails.addWarning(WarningType.Other, "Account selected
                  // for charges does not belong to the Applicant [CM]");
                  //
                  // }
                  //
                  // }} catch (Exception e) {
                  // //Loggers.general().info(LOG,"Exception occured in isChargeAccountDiff -
                  // "
                  // + e.getMessage());
                  // }

                  /*
                   * // provitional due date calculation SimpleDateFormat sdf = new
                   * SimpleDateFormat("dd/MM/yy"); Calendar c =
                   * Calendar.getInstance(); String tdydate
                   * =getDriverWrapper().getEventFieldAsText("TDY", "d","");
                   * //12/10/16 //Loggers.general().info(LOG,"Today date"+tdydate); String
                   * graceda = getWrapper().getGRACEPER(); int gra =
                   * Integer.parseInt(graceda); //Loggers.general().info(LOG,
                   * "Greace value in int ----> " + gra); try {
                   * c.setTime(sdf.parse(tdydate)); //Loggers.general().info(LOG,
                   * "tdydate -------> "); c.add(Calendar.DATE, gra);
                   * ////Loggers.general().info(LOG,"DATE 1"+ c); String output =
                   * sdf.format(c.getTime()); //Loggers.general().info(LOG,output);
                   * //getWrapper().setPRVDUEDT(output);
                   * getPane().setPRVDUEDT(output);
                   *
                   * } catch(Exception e) { //Loggers.general().info(LOG,"exeception"+e); }
                   *
                   * // Actual due date SimpleDateFormat simpDate = new
                   * SimpleDateFormat("dd/MM/yy"); Calendar c2 =
                   * Calendar.getInstance(); String prodate
                   * =getWrapper().getPRVDUEDT(); //12/10/16 //Loggers.general().info(LOG,
                   * "Provisional Due date"+prodate); String colingprd =
                   * getWrapper().getCOOLNG(); int coling =
                   * Integer.parseInt(colingprd); //Loggers.general().info(LOG,
                   * "Cooling period in int ----> " + coling); try {
                   * c2.setTime(simpDate.parse(prodate)); //Loggers.general().info(LOG,
                   * "prodate -------> "); c2.add(Calendar.DATE, coling);
                   * ////Loggers.general().info(LOG,"DATE 1"+ c); String output =
                   * simpDate.format(c2.getTime()); //Loggers.general().info(LOG,output);
                   * //getWrapper().setPRVDUEDT(output); getPane().setACTDUE(output);
                   *
                   * } catch(Exception e) { //Loggers.general().info(LOG,"exeception"+e); }
                   */

            }
      }

      // private boolean isChargeAccountDiff(Connection con) {
      // boolean isChargeAccountDiff = false;
      // PreparedStatement ps = null;
      // ResultSet rs = null;
      //
      // // Loggers.general().info(LOG,"isChargeAccountDiff method Entered");
      // try {
      // String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
      // "r", "");
      // String account = getDriverWrapper().getEventFieldAsText("PRI", "q",
      // "RCA").trim();
      // String ar[] = account.split("-");
      //
      // // //Loggers.general().info(LOG,"Master Reference" + masterRefNumber);
      // String priCustStr = getDriverWrapper().getEventFieldAsText("PRI", "p",
      // "no");
      // if (priCustStr != null) {
      // /*
      // * String chargeAccountCheckQuery =
      // * "select trim(p.bo_acc_no) from master m, baseevent b, relitem r,
      // posting p where m.key97 = b.master_key and b.key97 = r.event_key and
      // r.key97 = p.key97 and p.acc_type in ('CA', 'RB') and m.master_ref='"
      // * + masterRefNumber +
      // * "' and trim(p.bo_acc_no) not in (select trim(bo_acctno) from account
      // where trim(cus_mnm)='"
      // * + priCustStr + "')"; //Loggers.general().info(LOG,
      // * "chargeAccountCheckQuery - " + chargeAccountCheckQuery); ps =
      // * con.prepareStatement(chargeAccountCheckQuery); System.out
      // * .println("prepared statement for chargeAccountCheck - " +
      // * ps); rs = ps.executeQuery(); if (rs.next()) {
      // */
      // if (priCustStr != ar[2].trim()) {
      // isChargeAccountDiff = true;
      // }
      //
      // }
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"Exception occured in isChargeAccountDiff - "
      // // + e.getMessage());
      // }
      // return isChargeAccountDiff;
      // }

}