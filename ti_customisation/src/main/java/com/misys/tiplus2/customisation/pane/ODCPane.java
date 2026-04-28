package com.misys.tiplus2.customisation.pane;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.log4j.Logger;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventFircDetails;
import com.misys.tiplus2.customisation.entity.ExtEventFircDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceDetails;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventPrePurchaseOrder;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTax;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTaxEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingCollections;
import com.misys.tiplus2.customisation.entity.ExtEventShippingCollectionsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetails;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventOrmDetails;
import com.misys.tiplus2.customisation.entity.ExtEventOrmDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisation;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisationEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.customisation.extension.OdcFEC;
import com.misys.tiplus2.customisation.services.UidSelectUtil;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.pane.ExtensionViewPaneMode;
import com.misys.tiplus2.enigma.customisation.pane.ExtensionViewWebPane;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationTexts;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.enigma.lang.EnigmaException;
import com.misys.tiplus2.enigma.lang.EnigmaExceptionCode;
import com.misys.tiplus2.enigma.lang.control.EnigmaControl;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import com.misys.tiplus2.enigma.lang.util.PaneManager;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
import com.misys.tiplus2.customisation.pane.EventPane;

public class ODCPane extends EventPane {

      private static final String ODC_CREATE = "EventEXPBILLclay";
      private ExtensionViewWebPane extensionViewWebPane = null;
      // private static final Logger logger =
      // Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(ODCPane.class);
      Connection con, con1 = null;
      PreparedStatement dmsp, dmsp1, ps, ps1, ps2, psd, ps3, ps4, pst, prepare1, prepare2, shp, account = null;
      ResultSet dmsr, dmsr1, rs, rs1, rs2, rst, rs3, rs4, result, result2, shpr, rst2, rst3, rst1 = null;
      CallableStatement stmt = null;
      ConnectionMaster cm = new ConnectionMaster();
      ValidationDetails validationDetails = new ValidationDetails();

      public void onFETCHIFSCINWCOLCREclayButton() {
            // Loggers.general().info(LOG,"IFSC SFMS called in Inward collection Create");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      public void onFETCHIFSCINWDOCCOLADJclayButton() {
            // Loggers.general().info(LOG,"IFSC SFMS called in Inward collection Adjust");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      public void onFETCHIFSCINWDOCCOLAMDclayButton() {
            // Loggers.general().info(LOG,"IFSC SFMS called in Inward collection Amend");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      public void onFETCHIFSCEXPBILLclayButton() {
            // Loggers.general().info(LOG,"IFSC SFMS called in outward collection Create");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      public void onFETCHIFSCOUTDOCCOLADJclayButton() {
            // Loggers.general().info(LOG,"IFSC SFMS called in outward collection Adjust");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      public void onFETCHIFSCOUTDOCCOLAMDclayButton() {
            // Loggers.general().info(LOG,"IFSC SFMS called in outward collection Amend");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      public void onMERCHANTINWDOCCOLADJclayButton() {
            // String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r",
            // "");
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
            String mercht = getDriverWrapper().getEventFieldAsText("cARQ", "l", "").toString();
            String relrefno = getREMERREF();
            String adremno = getADOUTREM();

            int dmT = 0;
            try {
                  // //Loggers.general().info(LOG,"enter into try");
                  con = ConnectionMaster.getConnection();
                  String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                              + relrefno + "'";
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Master ref no valid for Import lc" + dms);
                  }

                  ps = con.prepareStatement(dms);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {
                                    // //Loggers.general().info(LOG,"enter into try");

                                    String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                + relrefno + "'";
                                    // Loggers.general().info(LOG,"values fetching Import lc" +
                                    // query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "values fetching Import lc" + query_dms);
                                    }
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String merdate = rs.getString(1);
                                          // setMERDUET(merdate);
                                          // Loggers.general().info(LOG,"AFTER GET THE VALUE Import
                                          // lc" + dmT);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "AFTER GET THE VALUE Import lc" + dmT);
                                          }
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }

                        } else {
                              Loggers.general().info(LOG,
                                          "Merchant trade is not tickec or master not valide Import lc --->" + dmT + mercht);
                        }

                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details Import--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Merchanting details Import--->" + e.getMessage());
                  }
            }

            finally {
                  try {
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

            try {
                  // inward renittance
                  String query_adv = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS ADOUTREM FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND prod.CODE ='AIR' AND mas.MASTER_REF ='"
                              + adremno + "'";
                  // Loggers.general().info(LOG,"Advance rem no valid for Export lc" +
                  // query_adv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Advance rem no valid for Export lc" + query_adv);
                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(query_adv);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"query_adv AFTER GET THE VALUE " + dmT);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "query_adv AFTER GET THE VALUE " + dmT);
                        }
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {

                                    String query_dms = "SELECT TO_CHAR(cpm.RCV_DATE,'DD/MM/YY') AS REV_DATE FROM master mas, CPAYMASTER cpm, PARTYDTLS prt, TIDATAITEM tid WHERE mas.key97 = tid.MASTER_KEY AND tid.KEY97 = prt.KEY97 AND mas.KEY97 = cpm.KEY97 AND prt.ROLE = 'BEN' AND mas.master_ref ='"
                                                + adremno + "'";
                                    // Loggers.general().info(LOG,"Advance rem no values fetching
                                    // Import lc" + query_dms);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Advance rem no values fetching Import lc" + query_dms);
                                    }
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String recdate = rs.getString(1);
                                          // Loggers.general().info(LOG,"Outward received date
                                          // fetching " + recdate);

                                          // Loggers.general().info(LOG,"systemDate date --->" +
                                          // recdate);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Outward received date fetching " + recdate);

                                                Loggers.general().info(LOG, "systemDate date --->" + recdate);
                                          }
                                          String accept = getDriverWrapper().getEventFieldAsText("FCO:sROS", "s", "");
                                          // Loggers.general().info(LOG,"Payment is acceptance or
                                          // sight --->" + accept);
                                          // tenor FCO:sTNR
                                          if (accept.equalsIgnoreCase("Acceptance")) {
                                                String tenor = "0";
                                                tenor = getDriverWrapper().getEventFieldAsText("FCO:sTNR", "i", "");
                                                // Loggers.general().info(LOG,"tenor befor converting "
                                                // + tenor);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "tenor befor converting " + tenor);
                                                }
                                                if (tenor == null || tenor.isEmpty()) {
                                                      tenor = "0";

                                                }
                                                int tnr = Integer.parseInt(tenor);
                                                String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                                Calendar cal = Calendar.getInstance();
                                                int gr = 120;
                                                int gra = tnr + gr;
                                                // Loggers.general().info(LOG,"tenor days and 90 daye "
                                                // + gra);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "tenor days and 90 daye " + gra);
                                                }

                                                try {
                                                      cal.setTime(sdf.parse(systemDate));
                                                      // //Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      cal.add(Calendar.DATE, gra);
                                                      String output = sdf.format(cal.getTime());
                                                      // Loggers.general().info(LOG,"output----->" +
                                                      // output);
                                                      // setMERDUET(output);

                                                } catch (Exception e) {
                                                      // Loggers.general().info(LOG,"Sight value date
                                                      // --->" + e.getMessage());
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "Sight value date --->" + e.getMessage());
                                                      }
                                                }
                                          } else {

                                                String systemDate = getDASHIP_Name();
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                                Calendar cal = Calendar.getInstance();
                                                int gra = 270;
                                                // int gra = tnr + gr;
                                                // //Loggers.general().info(LOG,"tenor days and 90 daye
                                                // "
                                                // + gra);

                                                try {
                                                      cal.setTime(sdf.parse(systemDate));
                                                      // //Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      cal.add(Calendar.DATE, gra);
                                                      String output = sdf.format(cal.getTime());
                                                      // Loggers.general().info(LOG,"output----->" +
                                                      // output);
                                                      // setMERDUET(output);

                                                } catch (Exception e) {
                                                      // Loggers.general().info(LOG,"Sight value date
                                                      // --->" + e.getMessage());
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "Sight value date --->" + e.getMessage());
                                                      }
                                                }

                                          }
                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }

                        } else {
                              // Loggers.general().info(LOG,"Merchant trade is not ticked or
                              // master not valide Import --->" + dmT + mercht);
                        }

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details Import--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Merchanting details Import --->" + e.getMessage());
                  }
            } finally {
                  try {
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
      }

      public void onMERCHATINWCOLCREclayButton() {
            // String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r",
            // "");
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

            String mercht = getDriverWrapper().getEventFieldAsText("cARQ", "l", "").toString();
            String relrefno = getREMERREF();
            String adremno = getADOUTREM();

            int dmT = 0;
            try {
                  // //Loggers.general().info(LOG,"enter into try");

                  String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                              + relrefno + "'";
                  // Loggers.general().info(LOG,"Master ref no valid for Import lc" + dms);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Master ref no valid for Import lc" + dms);
                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(dms);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {
                                    // //Loggers.general().info(LOG,"enter into try");

                                    String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                + relrefno + "'";
                                    // Loggers.general().info(LOG,"values fetching Import lc" +
                                    // query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "values fetching Import lc" + query_dms);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String merdate = rs.getString(1);
                                          // setMERDUET(merdate);
                                          // Loggers.general().info(LOG,"AFTER GET THE VALUE Import
                                          // lc" + dmT);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "AFTER GET THE VALUE Import lc" + dmT);
                                          }
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }

                        } else {
                              Loggers.general().info(LOG,
                                          "Merchant trade is not tickec or master not valide Import lc --->" + dmT + mercht);
                        }

                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details Import--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Merchanting details Import--->" + e.getMessage());
                  }
            }
            try {
                  // inward renittance
                  String query_adv = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS ADOUTREM FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND prod.CODE ='AIR' AND mas.MASTER_REF ='"
                              + adremno + "'";
                  // Loggers.general().info(LOG,"Advance rem no valid for Export lc" +
                  // query_adv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Advance rem no valid for Export lc" + query_adv);
                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(query_adv);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"query_adv AFTER GET THE VALUE " + dmT);
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {

                                    String query_dms = "SELECT TO_CHAR(cpm.RCV_DATE,'DD/MM/YY') AS REV_DATE FROM master mas, CPAYMASTER cpm, PARTYDTLS prt, TIDATAITEM tid WHERE mas.key97 = tid.MASTER_KEY AND tid.KEY97 = prt.KEY97 AND mas.KEY97 = cpm.KEY97 AND prt.ROLE = 'BEN' AND mas.master_ref ='"
                                                + adremno + "'";
                                    // Loggers.general().info(LOG,"Advance rem no values fetching
                                    // Import lc" + query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Advance rem no values fetching Import lc" + query_dms);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String recdate = rs.getString(1);
                                          // Loggers.general().info(LOG,"Outward received date
                                          // fetching " + recdate);

                                          // Loggers.general().info(LOG,"systemDate date --->" +
                                          // recdate);
                                          String accept = getDriverWrapper().getEventFieldAsText("FCO:sROS", "s", "");
                                          // Loggers.general().info(LOG,"Payment is acceptance or
                                          // sight --->" + accept);
                                          // tenor FCO:sTNR
                                          if (accept.equalsIgnoreCase("Acceptance")) {
                                                String tenor = "0";
                                                tenor = getDriverWrapper().getEventFieldAsText("FCO:sTNR", "i", "");
                                                // Loggers.general().info(LOG,"tenor befor converting "
                                                // + tenor);
                                                if (tenor == null || tenor.isEmpty()) {
                                                      tenor = "0";

                                                }
                                                int tnr = Integer.parseInt(tenor);
                                                String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                                Calendar cal = Calendar.getInstance();
                                                int gr = 120;
                                                int gra = tnr + gr;
                                                // Loggers.general().info(LOG,"tenor days and 90 daye "
                                                // + gra);

                                                try {
                                                      cal.setTime(sdf.parse(systemDate));
                                                      // //Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      cal.add(Calendar.DATE, gra);
                                                      String output = sdf.format(cal.getTime());
                                                      // Loggers.general().info(LOG,"output----->" +
                                                      // output);
                                                      // setMERDUET(output);

                                                } catch (Exception e) {
                                                      // Loggers.general().info(LOG,"Sight value date
                                                      // --->" + e.getMessage());
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "Sight value date --->" + e.getMessage());
                                                      }
                                                }
                                          } else {
                                                // Loggers.general().info(LOG,"Payment is sight --->" +
                                                // accept);

                                                // String tenor = "0";
                                                // tenor =
                                                // getDriverWrapper().getEventFieldAsText("FCO:sTNR",
                                                // "i", "");
                                                // //Loggers.general().info(LOG,"tenor befor converting
                                                // "
                                                // + tenor);
                                                // if (tenor == null || tenor.isEmpty()) {
                                                // tenor = "0";
                                                //
                                                // }
                                                // int tnr = Integer.parseInt(tenor);
                                                String systemDate = getDASHIP_Name();
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                                Calendar cal = Calendar.getInstance();
                                                int gra = 270;
                                                // int gra = tnr + gr;
                                                // //Loggers.general().info(LOG,"tenor days and 90 daye
                                                // "
                                                // + gra);

                                                try {
                                                      cal.setTime(sdf.parse(systemDate));
                                                      // //Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      cal.add(Calendar.DATE, gra);
                                                      String output = sdf.format(cal.getTime());
                                                      // Loggers.general().info(LOG,"output----->" +
                                                      // output);
                                                      // setMERDUET(output);

                                                } catch (Exception e) {
                                                      // Loggers.general().info(LOG,"Sight value date
                                                      // --->" + e.getMessage());
                                                }

                                          }
                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }

                        } else {
                              // Loggers.general().info(LOG,"Merchant trade is not ticked or
                              // master not valide Import --->" + dmT + mercht);
                        }

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details Import--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Merchanting details Import--->" + e.getMessage());
                  }
            } finally {
                  try {
                        if (con != null) {
                              con.close();
                              if (dmsp != null)
                                    dmsp.close();
                              if (dmsr != null)
                                    dmsr.close();
                        }
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void onMERCHATOUTDOCCOLADJclayButton() {
            String mercht = getDriverWrapper().getEventFieldAsText("cARQ", "l", "").toString();
            String relrefno = getREMERREF();

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

            int dmT = 0;
            try {
                  // //Loggers.general().info(LOG,"enter into try");

                  String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                              + relrefno + "'";
                  // Loggers.general().info(LOG,"Master ref no valid for ODC" + dms);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Master ref no valid for ODC" + dms);
                  }
                  Connection con = ConnectionMaster.getConnection();
                  PreparedStatement ps = con.prepareStatement(dms);
                  ResultSet rs = ps.executeQuery();
                  while (rs.next()) {
                        // Loggers.general().info(LOG,"while--->");
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {
                                    // //Loggers.general().info(LOG,"enter into try");

                                    String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                + relrefno + "'";
                                    // Loggers.general().info(LOG,"values fetching ODC" +
                                    // query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "values fetching ODC" + query_dms);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String merdate = rs.getString(1);
                                          // setMERDUET(merdate);
                                          // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }

                        } else {
                              // Loggers.general().info(LOG,"Merchant trade is not tickec or
                              // master not valide ODC collection--->" + dmT + mercht);
                        }

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details in ODC--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Merchanting details in ODC--->" + e.getMessage());
                  }
            }

      }

      public void onBUYERVALINWDOCCOLPAYclayButton() {

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

            String mas = getPREBUYRE();
            int dmT = 0;
            try {
                  // //Loggers.general().info(LOG,"enter into try");

                  String dms = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS PREBUYRE FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND prod.CODE ='BCR' AND mas.MASTER_REF ='"
                              + mas + "'";
                  // Loggers.general().info(LOG,"Master ref no valid " + dms);

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Master ref no valid " + dms);
                  }
                  Connection con = ConnectionMaster.getConnection();
                  PreparedStatement ps = con.prepareStatement(dms);
                  ResultSet rs = ps.executeQuery();
                  while (rs.next()) {
                        // Loggers.general().info(LOG,"while--->");
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                        if (dmT > 0) {
                              try {

                                    String query_dms = "SELECT TRIM(mas.NPRNAME_L1),TRIM(mas.AMOUNT),mas.CCY,TO_CHAR(ext.CLIMEXPD,'DD/MM/YY') AS CLIMEXPD,ext.INTAMT,ext.CCY_3,ext.TENDAYS,ext.FUNDBAN FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('ISS','TIG') AND mas. MASTER_REF='"
                                                + mas + "'";
                                    // Loggers.general().info(LOG,"values fetching
                                    // BUYERVALINWDOCCOLPAYclayButton " + query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "values fetching BUYERVALINWDOCCOLPAYclayButton " + query_dms);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          // Loggers.general().info(LOG,"BUYERVALINWDOCCOLPAYclayButton
                                          // into while");
                                          String advise = rs.getString(1);
                                          String amt = rs.getString(2);
                                          // Loggers.general().info(LOG,"values fetching BUYER credit
                                          // amount " + amt);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "values fetching BUYER credit amount " + amt);
                                          }
                                          BigDecimal hundred = new BigDecimal(100);
                                          BigDecimal amt_val = new BigDecimal(amt);
                                          BigDecimal amt_big = amt_val.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);

                                          // Loggers.general().info(LOG,"values fetching BUYER credit
                                          // amount amt_big" + amt_big);
                                          String ccy = rs.getString(3);
                                          String clmate = rs.getString(4);
                                          String int_amt = rs.getString(5);

                                          String int_ccy = rs.getString(6);
                                          String tenday = rs.getString(7);
                                          String fundchrg = rs.getString(8);

                                          setFUNDBAN(advise);
                                          setCREAMT(amt_big + " " + ccy);
                                          // Loggers.general().info(LOG,"fetching values BUYER credit
                                          // after set" + getCREAMT());
                                          setDUEDAT(clmate);
                                          if (int_amt.length() > 0) {
                                                BigDecimal int_val = new BigDecimal(int_amt);
                                                BigDecimal int_big = int_val.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
                                                setINTAMT(int_big + " " + int_ccy);
                                          }
                                          setTENO(tenday);
                                          if (fundchrg.length() > 0) {
                                                // Loggers.general().info(LOG,"Funding bank charges" +
                                                // fundchrg);
                                                String fundchrg_val = fundchrg.replaceAll("[^0-9]", "");
                                                // Loggers.general().info(LOG,"Funding bank charges
                                                // amount only" + fundchrg_val);
                                                BigDecimal fundchrg_big = new BigDecimal(fundchrg_val);
                                                // Loggers.general().info(LOG,"Funding bank charges
                                                // amount only fundchrg_big" + fundchrg_big);
                                                BigDecimal fundchrg_total = fundchrg_big.multiply(hundred);
                                                String luCur = fundchrg.replaceAll("[^A-Za-z]+", "");
                                                // Loggers.general().info(LOG,"Funding bank charges
                                                // currency" + luCur);
                                                setCHABUY(fundchrg_total + " " + luCur);
                                          }

                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"values fetching
                                    // BUYERVALINWDOCCOLPAYclayButton" + e.getMessage());
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "values fetching BUYERVALINWDOCCOLPAYclayButton" + e.getMessage());
                                    }
                              }

                        } else {
                              // Loggers.general().info(LOG,"master no is not valide IDC
                              // collection--->" + dmT);
                        }

                  }
                  // Loggers.general().info(LOG,"master no empty--->" + dmT);

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Master IDC no not valid count 0--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Master IDC no not valid count 0--->" + e.getMessage());
                  }
            }
      }

      public void onNOSTROEXPCOLSETTclayButton() {

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
//        String nostref_MT103102 = getNOSTMT().trim();
//        String nostref_MT940950 = getNOSTRM().trim();
//        String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
//        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
//        String Credit_AmountMT103202 = "";
//        String Credit_curMT103202 = "";
//        String ValuedateMT103202 = "";
//        String Credit_AmountMT940950 = "";
//        String Credit_curMT940950 = "";
//        String CreditAccountMT940950 = "";
//        String Nostro_key = "";
//        String msg940950_1 = "";
//        String msg940950_2 = "";
//        String msg940950_concat = "";
//        String swift_type = "";
//        con = ConnectionMaster.getConnection();
//        if (nostref_MT103102.length() > 0) {
//          try {
//
//              String query = "SELECT MT103_MT202_AMOUNT AS CreditMT103202, MT103_MT202_CURRENCY_CODE AS CreditCurMT103202, TO_CHAR(MT103_MT202_VALUE_DATE,'DD/MM/YY') AS ValuedateMT103202, MT940_MT950_AMOUNT AS CreditAmountMT940950, MT940_MT950_CURRENCY_CODE AS CreditCurMT940950, MT940_MT950_ACCOUNT_NO AS CreditAccountMT940950, MT940_MT950_IND_TEXT, MT940_MT950_ACC_OWNER_INFO,Trim(SWIFT_TYPE),MT940_MT950_REFERENCE_NUMBER ,NOSTRO_KEY FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
//                    + nostref_MT103102 + "'";
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG, "Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
//              }
//
//              ps = con.prepareStatement(query);
//              rs = ps.executeQuery();
//              if (rs.next()) {
//
//                Credit_AmountMT103202 = rs.getString(1);
//                Credit_curMT103202 = rs.getString(2);
//
//                // setNOSTAMT(Credit_AmountMT103202 + " " + Credit_curMT103202);
//                setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
//                ValuedateMT103202 = rs.getString(3);
//                setNOSTDAT(ValuedateMT103202);
//                Credit_AmountMT940950 = rs.getString(4);
//                Credit_curMT940950 = rs.getString(5);
//
//                // setPOOLAMT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                CreditAccountMT940950 = rs.getString(6);
//
//                setNOSTACC(CreditAccountMT940950);
//
//                Nostro_key = rs.getString(11);
//                Loggers.general().info(LOG, "Nostro Key-=====================-->" + Nostro_key);
//                setNOSTROKE(Nostro_key);
//
//                // ========todo on mar 9
//                BigDecimal totalVal1 = new BigDecimal(Credit_AmountMT103202);
//
//                ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                double divideByDecimal1 = connectionMaster1.getDecimalforCurrency(Credit_curMT103202);
//                BigDecimal divideByBig1 = new BigDecimal(divideByDecimal1);
//                BigDecimal totalValue1 = totalVal1.divide(divideByBig1);
//
//                // BigDecimal totalValue1 = new BigDecimal(Credit_AmountMT103202);
//
//                BigDecimal totalVal2 = new BigDecimal(Credit_AmountMT940950);
//
//                // ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                double divideByDecimal2 = connectionMaster1.getDecimalforCurrency(Credit_curMT940950);
//                BigDecimal divideByBig2 = new BigDecimal(divideByDecimal2);
//                BigDecimal totalValue2 = totalVal2.divide(divideByBig2);
//
//                // =======================
//
//                // =====================
//
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG,
//                          "Total Nostro Credit amount-=====================-->" + totalValue1);
//                    Loggers.general().info(LOG, "poollll amount-=====================-->" + totalValue2);
//
//                }
//                String finalVal1 = String.format("%.2f", totalValue1);
//                String finalVal2 = String.format("%.2f", totalValue2);
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG, "Final Nostro Credit amount===========--->" + finalVal1);
//                    Loggers.general().info(LOG, "Final poolll amount===========--->" + finalVal2);
//
//                }
//                setNOSTAMT(finalVal1 + " " + Credit_curMT103202);
//
//                setPOOLAMT(finalVal2 + " " + Credit_curMT940950);
//
//                // ================================
//                /*
//                 * msg940950_1 = rs.getString(7); msg940950_2 = rs.getString(8);
//                 *
//                 * msg940950_concat = msg940950_1 + " \n " + msg940950_2;
//                 * setMTMESG(msg940950_concat);
//                 */
//                swift_type = rs.getString(9);
//                String nostref_MT940 = rs.getString(10);
//                setNOSTRM(nostref_MT940);
//
//                // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT,
//                // mas.MASTER_REF FROM master mas, BASEEVENT bas,
//                // extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND
//                // bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                // (mas.MASTER_REF ='"
//                // + masReference + "' AND trim(bas.REFNO_PFIX
//                // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                // + "') AND ext.NOSTMT ='" + nostref_MT103102
//                // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                // master mas, BASEEVENT bas, extevent ext WHERE
//                // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT
//                // AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                // + masReference + "' AND trim(bas.REFNO_PFIX
//                // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                // + "') AND ext.NOSTMT ='" + nostref_MT103102
//                // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                      + "') AND ext.NOSTMT ='" + nostref_MT103102
//                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                      + "') AND ext.NOSTMT ='" + nostref_MT103102
//                      + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventCode
//                      + "') AND ext.NOSTMT ='" + nostref_MT103102
//                      + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";
//
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG, "Nostro outsanding amount--->" + dms);
//
//                }
//                ps = con.prepareStatement(dms);
//                rs = ps.executeQuery();
//                while (rs.next()) {
//                    String nostOut = rs.getString(1).trim();
//                    BigDecimal nostOutBig = new BigDecimal(nostOut);
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);
//
//                    }
//                    BigDecimal CreditBig = new BigDecimal(Credit_AmountMT940950);
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro Credit amount--->" + CreditBig);
//
//                    }
//                    BigDecimal totalVal = CreditBig.subtract(nostOutBig);
//
//                    ConnectionMaster connectionMaster = new ConnectionMaster();
//                    double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT940950);
//                    BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                    BigDecimal totalValue = totalVal.divide(divideByBig);
//
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);
//
//                    }
//                    String finalVal = String.format("%.2f", totalValue);
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);
//
//                    }
//                    if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                      setNOSTOUT(finalVal + " " + Credit_curMT940950);
//                    } else {
//                      finalVal = "0";
//                      setNOSTOUT(finalVal + " " + Credit_curMT940950);
//
//                    }
//
//                }
//                String query_940 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT,TRIM('TAG 86:'||ACC_OWNER_INFO) AS ACC_OWNER_INFO FROM ETT_NOSTRO_UTILITY_MT940 where IND_NOS_CUS_REFNO='"
//                      + nostref_MT940 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940 + "'";
//                //// Loggers.general().info(LOG,"Nostro swift_type is
//                //// query_103
//                //// " + query_103);
//                ps3 = con.prepareStatement(query_940);
//                rs3 = ps3.executeQuery();
//                while (rs3.next()) {
//                    String msg1 = rs3.getString(1);
//                    String msg2 = rs3.getString(2);
//                    String msg3 = rs3.getString(3);
//                    String fullmsg = msg1 + " \n " + msg2 + " \n " + msg3;
//                    setMTMESG(fullmsg);
//                }
//                String query_950 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT FROM ETT_NOSTRO_UTILITY_MT950 where IND_NOS_CUS_REFNO='"
//                      + nostref_MT940 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940 + "'";
//                //// Loggers.general().info(LOG,"Nostro swift_type is
//                //// query_103
//                //// " + query_103);
//                ps4 = con.prepareStatement(query_950);
//                rs4 = ps4.executeQuery();
//                while (rs4.next()) {
//                    String msg1 = rs4.getString(1);
//                    String msg2 = rs4.getString(2);
//
//                    String fullmsg = msg1 + " \n " + msg2;
//                    setMTMESG(fullmsg);
//                }
//
//                if (swift_type.equalsIgnoreCase("103")) {
//                    String query_103 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT103 WHERE REFERENCE_NUMBER ='"
//                          + nostref_MT103102 + "'";
//                    //// Loggers.general().info(LOG,"Nostro swift_type is
//                    //// query_103
//                    //// " + query_103);
//                    ps = con.prepareStatement(query_103);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                      String swift = rs.getString(1);
//                      setINWMSG(swift);
//                    }
//                } else if (swift_type.equalsIgnoreCase("202")) {
//                    String query_202 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT202 WHERE REFERENCE_NUMBER ='"
//                          + nostref_MT103102 + "'";
//                    //// Loggers.general().info(LOG,"Nostro swift_type is
//                    //// query_202
//                    //// " + query_202);
//
//                    ps = con.prepareStatement(query_202);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                      String swift = rs.getString(1);
//                      setINWMSG(swift);
//                    }
//                } else {
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro swift_type is empty " + swift_type);
//                    }
//                    setINWMSG("");
//
//                }
//
//              } else {
//
//                setPOOLAMT("");
//                setNOSTACC("");
//                setMTMESG("");
//
//                /*
//                 * String queryVal =
//                 * "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA from ETT_NOSTRO_UTILITY_MT103 M where trim(M.REFERENCE_NUMBER)='"
//                 * + nostref_MT103102 + "'";
//                 */
//                String queryVal = "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA,M.MT103_SL_NO from ETT_NOSTRO_UTILITY_MT103 M where QUEUE_TYPE='CLSQ' AND RESPQ_STATUS='A' AND trim(M.REFERENCE_NUMBER)='"
//                      + nostref_MT103102 + "'";
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG,
//                          "Nostro ETT_NOSTRO_UTILITY_MT103 value query else===> " + queryVal);
//                }
//                ps = con.prepareStatement(queryVal);
//                rs = ps.executeQuery();
//
//                // while (rs.next()) { changed to add mt202
//                if (rs.next()) {
//                    Credit_AmountMT103202 = rs.getString(1);
//                    BigDecimal Credit_Amount = new BigDecimal(Credit_AmountMT103202);
//
//                    Credit_curMT103202 = rs.getString(2);
//                    String Credit_AmountMT103 = String.format("%.2f", Credit_Amount);
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      System.out.println(
//                            "Nostro ETT_NOSTRO_UTILITY_MT103 value==1==> " + Credit_AmountMT103202);
//                      Loggers.general().info(LOG,
//                            "Nostro ETT_NOSTRO_UTILITY_MT103 value==2==> " + Credit_AmountMT103);
//                    }
//                    setNOSTAMT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                    setNOSTOUT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                    ValuedateMT103202 = rs.getString(3);
//                    setNOSTDAT(ValuedateMT103202);
//
//                    msg940950_concat = rs.getString(4);
//                    setINWMSG(msg940950_concat);
//                    Nostro_key = rs.getString(5);
//                    Loggers.general().info(LOG, "Nostro Key-=====================-->" + Nostro_key);
//                    setNOSTROKE(Nostro_key);
//
//                    // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                    // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS
//                    // NOSUTLAMT, mas.MASTER_REF FROM master mas,
//                    // BASEEVENT bas, extevent ext WHERE mas.KEY97
//                    // =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND
//                    // bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                    // + masReference + "' AND trim(bas.REFNO_PFIX
//                    // ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                    // + eventCode + "') AND ext.NOSTMT ='" +
//                    // nostref_MT103102
//                    // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                    // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                    // master mas, BASEEVENT bas, extevent ext WHERE
//                    // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97
//                    // =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                    // (mas.MASTER_REF !='"
//                    // + masReference + "' AND trim(bas.REFNO_PFIX
//                    // ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                    // + eventCode + "') AND ext.NOSTMT ='" +
//                    // nostref_MT103102
//                    // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                    String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                          + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                          + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                          + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
//                          + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                          + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                          + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
//                          + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
//                          + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                          + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";
//
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro outsanding amount else--->" + dms);
//
//                    }
//                    ps = con.prepareStatement(dms);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                      String nostOut = rs.getString(1).trim();
//                      BigDecimal nostOutBig = new BigDecimal(nostOut);
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);
//
//                      }
//                      BigDecimal CreditBig = new BigDecimal(Credit_AmountMT103202);
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Nostro Credit amount for MT103--->" + CreditBig);
//
//                      }
//                      ConnectionMaster connectionMaster = new ConnectionMaster();
//                      double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT103202);
//                      BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                      BigDecimal creditAmount = CreditBig.multiply(divideByBig);
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG,
//                                "Nostro Credit amount for MT103 after multifly===>" + CreditBig);
//
//                      }
//                      BigDecimal totalVal = creditAmount.subtract(nostOutBig);
//
//                      BigDecimal totalValue = totalVal.divide(divideByBig);
//
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);
//
//                      }
//                      String finalVal = String.format("%.2f", totalValue);
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);
//
//                      }
//                      if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                          setNOSTOUT(finalVal + " " + Credit_curMT103202);
//                      } else {
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,
//                                  "Final Nostro Credit amount ZERO in else--->" + finalVal);
//
//                          }
//                          finalVal = "0";
//                          setNOSTOUT(finalVal + " " + Credit_curMT103202);
//
//                      }
//
//                    }
//
//                }
//                // MT202 START
//                else {
//                    setPOOLAMT("");
//                    setNOSTACC("");
//                    setMTMESG("");
//
//                    /*
//                     * String queryVal2 =
//                     * "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA from ETT_NOSTRO_UTILITY_MT202 M where trim(M.REFERENCE_NUMBER)='"
//                     * + nostref_MT103102 + "'";
//                     */
//                    String queryVal2 = "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA from ETT_NOSTRO_UTILITY_MT202 M where QUEUE_TYPE='CLSQ' AND RESPQ_STATUS='A' AND trim(M.REFERENCE_NUMBER)='"
//                          + nostref_MT103102 + "'";
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG,
//                            "Nostro ETT_NOSTRO_UTILITY_MT202 value query else===> " + queryVal2);
//                    }
//                    ps = con.prepareStatement(queryVal2);
//                    rs = ps.executeQuery();
//
//                    // while (rs.next()) {
//                    if (rs.next()) {
//                      Credit_AmountMT103202 = rs.getString(1);
//                      BigDecimal Credit_Amount = new BigDecimal(Credit_AmountMT103202);
//
//                      Credit_curMT103202 = rs.getString(2);
//                      String Credit_AmountMT103 = String.format("%.2f", Credit_Amount);
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          System.out.println(
//                                "Nostro ETT_NOSTRO_UTILITY_MT202 value==1==> " + Credit_AmountMT103202);
//                          System.out.println(
//                                "Nostro ETT_NOSTRO_UTILITY_MT202 value==2==> " + Credit_AmountMT103);
//                      }
//                      setNOSTAMT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                      setNOSTOUT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                      ValuedateMT103202 = rs.getString(3);
//                      setNOSTDAT(ValuedateMT103202);
//
//                      msg940950_concat = rs.getString(4);
//                      setINWMSG(msg940950_concat);
//
//                      // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                      // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS
//                      // NOSUTLAMT, mas.MASTER_REF FROM master mas,
//                      // BASEEVENT bas, extevent ext WHERE mas.KEY97
//                      // =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND
//                      // bas.STATUS IN ('i','c') AND (mas.MASTER_REF
//                      // ='"
//                      // + masReference + "' AND trim(bas.REFNO_PFIX
//                      // ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                      // + eventCode + "') AND ext.NOSTMT ='" +
//                      // nostref_MT103102
//                      // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                      // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF
//                      // FROM
//                      // master mas, BASEEVENT bas, extevent ext WHERE
//                      // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97
//                      // =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                      // (mas.MASTER_REF !='"
//                      // + masReference + "' AND trim(bas.REFNO_PFIX
//                      // ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                      // + eventCode + "') AND ext.NOSTMT ='" +
//                      // nostref_MT103102
//                      // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                      String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                            + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                            + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                            + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
//                            + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                            + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";
//
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Nostro outsanding amount else--->" + dms);
//
//                      }
//                      ps = con.prepareStatement(dms);
//                      rs = ps.executeQuery();
//                      while (rs.next()) {
//                          String nostOut = rs.getString(1).trim();
//                          BigDecimal nostOutBig = new BigDecimal(nostOut);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,
//                                  "Result of Nostro outsanding amount--->" + nostOutBig);
//
//                          }
//                          BigDecimal CreditBig = new BigDecimal(Credit_AmountMT103202);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Nostro Credit amount for MT202--->" + CreditBig);
//
//                          }
//                          ConnectionMaster connectionMaster = new ConnectionMaster();
//                          double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT103202);
//                          BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                          BigDecimal creditAmount = CreditBig.multiply(divideByBig);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            System.out.println(
//                                  "Nostro Credit amount for MT202 after multifly===>" + CreditBig);
//
//                          }
//                          BigDecimal totalVal = creditAmount.subtract(nostOutBig);
//
//                          BigDecimal totalValue = totalVal.divide(divideByBig);
//
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);
//
//                          }
//                          String finalVal = String.format("%.2f", totalValue);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);
//
//                          }
//                          if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                            setNOSTOUT(finalVal + " " + Credit_curMT103202);
//                          } else {
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                System.out
//                                      .println("Final Nostro Credit amount ZERO in else--->" + finalVal);
//
//                            }
//                            finalVal = "0";
//                            setNOSTOUT(finalVal + " " + Credit_curMT103202);
//
//                          }
//
//                      }
//
//                    }
//                }
//                // MT202 END
//
//              }
//
//          } catch (Exception ee) {
//              Loggers.general().info(LOG, "Exception Inward reference 103" + ee.getMessage());
//          }
//        } else if (nostref_MT940950.length() > 0) {
//
//          //// Loggers.general().info(LOG,"the Nostro MT103/202 reference number
//          //// is
//          //// empty");
//
//          try {
//
//              String query = "SELECT MT103_MT202_AMOUNT AS CreditMT103202, MT103_MT202_CURRENCY_CODE AS CreditCurMT103202, TO_CHAR(MT103_MT202_VALUE_DATE,'DD/MM/YY') AS ValuedateMT103202, MT940_MT950_AMOUNT AS CreditAmountMT940950, MT940_MT950_CURRENCY_CODE AS CreditCurMT940950, MT940_MT950_ACCOUNT_NO AS CreditAccountMT940950, MT940_MT950_IND_TEXT, MT940_MT950_ACC_OWNER_INFO,Trim(SWIFT_TYPE),MT103_MT202_REFERENCE_NUMBER,NOSTRO_KEY FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
//                    + nostref_MT940950 + "'";
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG, "Nostro MT940950 value query " + query);
//              }
//              ps = con.prepareStatement(query);
//              rs = ps.executeQuery();
//              while (rs.next()) {
//
//                Credit_AmountMT103202 = rs.getString(1);
//                Credit_curMT103202 = rs.getString(2);
//                //// Loggers.general().info(LOG,"Nostro MT103202 credit amount
//                //// and
//                //// currency" + Credit_AmountMT103202 + " " +
//                //// Credit_curMT103202);
//                // setNOSTAMT(Credit_AmountMT103202 + " " + Credit_curMT103202);
//
//                ValuedateMT103202 = rs.getString(3);
//                setNOSTDAT(ValuedateMT103202);
//                Credit_AmountMT940950 = rs.getString(4);
//                Credit_curMT940950 = rs.getString(5);
//                //// Loggers.general().info(LOG,"Nostro MT940950 credit amount
//                //// and
//                //// currency" + Credit_AmountMT940950 + " " +
//                //// Credit_curMT940950);
//                // setPOOLAMT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                setNOSTOUT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                CreditAccountMT940950 = rs.getString(6);
//
//                //// Loggers.general().info(LOG,"setNOSTACC------------>" +
//                //// getNOSTACC());
//                setNOSTACC(CreditAccountMT940950);
//                /*
//                 * msg940950_1 = rs.getString(7); msg940950_2 = rs.getString(8);
//                 *
//                 * msg940950_concat = msg940950_1 + " \n " + msg940950_2;
//                 * setMTMESG(msg940950_concat);
//                 */
//                swift_type = rs.getString(9);
//                String nostref_MT103 = rs.getString(10);
//                setNOSTMT(nostref_MT103);
//                Nostro_key = rs.getString(11);
//                Loggers.general().info(LOG, "Nostro Key-=====================-->" + Nostro_key);
//                setNOSTROKE(Nostro_key);
////=====================================================
//                BigDecimal totalVal1 = new BigDecimal(Credit_AmountMT103202);
//
//                ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                double divideByDecimal1 = connectionMaster1.getDecimalforCurrency(Credit_curMT103202);
//                BigDecimal divideByBig1 = new BigDecimal(divideByDecimal1);
//                BigDecimal totalValue1 = totalVal1.divide(divideByBig1);
//
//                // BigDecimal totalValue1 = new BigDecimal(Credit_AmountMT103202);
//
//                BigDecimal totalVal2 = new BigDecimal(Credit_AmountMT940950);
//
//                // ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                double divideByDecimal2 = connectionMaster1.getDecimalforCurrency(Credit_curMT940950);
//                BigDecimal divideByBig2 = new BigDecimal(divideByDecimal2);
//                BigDecimal totalValue2 = totalVal2.divide(divideByBig2);
//
//                // =======================
//
//                // =====================
//
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG,
//                          "Total Nostro Credit amount-940=====================-->" + totalValue1);
//                    Loggers.general().info(LOG, "poollll amount-====940=================-->" + totalValue2);
//
//                }
//                String finalVal1 = String.format("%.2f", totalValue1);
//                String finalVal2 = String.format("%.2f", totalValue2);
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG, "Final Nostro Credit amount==940=========--->" + finalVal1);
//                    Loggers.general().info(LOG, "Final poolll amount===940========--->" + finalVal2);
//
//                }
//                setNOSTAMT(finalVal1 + " " + Credit_curMT103202);
//
//                setPOOLAMT(finalVal2 + " " + Credit_curMT940950);
//
//                // ===============================
//                // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT,
//                // mas.MASTER_REF FROM master mas, BASEEVENT bas,
//                // extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND
//                // bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                // (mas.MASTER_REF ='"
//                // + masReference + "' AND trim(bas.REFNO_PFIX
//                // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                // + "') AND ext.NOSTRM ='" + nostref_MT940950
//                // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                // master mas, BASEEVENT bas, extevent ext WHERE
//                // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT
//                // AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                // + masReference + "' AND trim(bas.REFNO_PFIX
//                // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                // + "') AND ext.NOSTRM ='" + nostref_MT940950
//                // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                      + "') AND ext.NOSTRM ='" + nostref_MT940950
//                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                      + "') AND ext.NOSTRM ='" + nostref_MT940950
//                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventCode
//                      + "') AND ext.NOSTRM ='" + nostref_MT940950
//                      + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG, "Nostro outsanding amount--->" + dms);
//
//                }
//                ps1 = con.prepareStatement(dms);
//                rs = ps1.executeQuery();
//                while (rs.next()) {
//                    String nostOut = rs.getString(1).trim();
//                    BigDecimal nostOutBig = new BigDecimal(nostOut);
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);
//
//                    }
//                    BigDecimal CreditBig = new BigDecimal(Credit_AmountMT940950);
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro Credit amount--->" + CreditBig);
//
//                    }
//                    BigDecimal totalVal = CreditBig.subtract(nostOutBig);
//
//                    ConnectionMaster connectionMaster = new ConnectionMaster();
//                    double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT940950);
//                    BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                    BigDecimal totalValue = totalVal.divide(divideByBig);
//
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);
//
//                    }
//                    String finalVal = String.format("%.2f", totalValue);
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);
//
//                    }
//                    if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                      setNOSTOUT(finalVal + " " + Credit_curMT940950);
//                    } else {
//                      finalVal = "0";
//                      setNOSTOUT(finalVal + " " + Credit_curMT940950);
//
//                    }
//
//                }
//
//                String query_940 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT,TRIM('TAG 86:'||ACC_OWNER_INFO) AS ACC_OWNER_INFO FROM ETT_NOSTRO_UTILITY_MT940 where IND_NOS_CUS_REFNO='"
//                      + nostref_MT940950 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940950 + "'";
//                //// Loggers.general().info(LOG,"Nostro swift_type is
//                //// query_103
//                //// " + query_103);
//                ps3 = con.prepareStatement(query_940);
//                rs3 = ps3.executeQuery();
//                while (rs3.next()) {
//                    String msg1 = rs3.getString(1);
//                    String msg2 = rs3.getString(2);
//                    String msg3 = rs3.getString(3);
//                    String fullmsg = msg1 + " \n " + msg2 + " \n " + msg3;
//                    setMTMESG(fullmsg);
//                }
//                String query_950 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT FROM ETT_NOSTRO_UTILITY_MT950 where IND_NOS_CUS_REFNO='"
//                      + nostref_MT940950 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940950 + "'";
//                //// Loggers.general().info(LOG,"Nostro swift_type is
//                //// query_103
//                //// " + query_103);
//                ps4 = con.prepareStatement(query_950);
//                rs4 = ps4.executeQuery();
//                while (rs4.next()) {
//                    String msg1 = rs4.getString(1);
//                    String msg2 = rs4.getString(2);
//
//                    String fullmsg = msg1 + " \n " + msg2;
//                    setMTMESG(fullmsg);
//                }
//
//                if (swift_type.equalsIgnoreCase("103")) {
//                    String query_103 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT103 WHERE REFERENCE_NUMBER ='"
//                          + nostref_MT103 + "'";
//                    //// Loggers.general().info(LOG,"Nostro swift_type is
//                    //// query_103
//                    //// " + query_103);
//                    ps = con.prepareStatement(query_103);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                      String swift = rs.getString(1);
//                      setINWMSG(swift);
//                    }
//                } else if (swift_type.equalsIgnoreCase("202")) {
//                    String query_202 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT202 WHERE REFERENCE_NUMBER ='"
//                          + nostref_MT103 + "'";
//                    //// Loggers.general().info(LOG,"Nostro swift_type is
//                    //// query_202
//                    //// " + query_202);
//
//                    ps = con.prepareStatement(query_202);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                      String swift = rs.getString(1);
//                      setINWMSG(swift);
//                    }
//                } else {
//                    //// Loggers.general().info(LOG,"Nostro swift_type is empty
//                    //// " +
//                    //// swift_type);
//
//                    setINWMSG("");
//
//                }
//
//              }
//
//          } catch (Exception ee) {
//              Loggers.general().info(LOG, "Exception Inward reference 940" + ee.getMessage());
//          }
//
//        } else {
//          setNOSTAMT("");
//          setNOSTDAT("");
//          setPOOLAMT("");
//          setNOSTACC("");
//          setMTMESG("");
//          setINWMSG("");
//          setNOSTOUT("");
//
//        }
//
//        try {
//
//          String query = "";
//          if (dailyval_Log.equalsIgnoreCase("YES")) {
//              Loggers.general().info(LOG, "Nostro ref no nostref_MT103102" + nostref_MT103102);
//          }
//          // String nostref_MT940950 = getWrapper().getNOSTRM().trim();
//
//          if (nostref_MT103102.length() > 0) {
//
//              query = "SELECT count(*) as COUNT FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
//                    + nostref_MT103102 + "'";
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG, "Nostro ref no ETTV_NOSTRO_MT103_MT202_TBL query" + query);
//              }
//              con = ConnectionMaster.getConnection();
//              ps = con.prepareStatement(query);
//              rs = ps.executeQuery();
//              int val = 0;
//              if (rs.next()) {
//                val = rs.getInt(1);
//
//              }
//
//              query = "select count(*) from ETT_NOSTRO_UTILITY_MT103 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
//                    + nostref_MT103102 + "'";
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG, "Nostro ref no ETT_NOSTRO_UTILITY_MT103 query" + query);
//              }
//              con = ConnectionMaster.getConnection();
//              ps = con.prepareStatement(query);
//              rs = ps.executeQuery();
//              int value103 = 0;
//              if (rs.next()) {
//                value103 = rs.getInt(1);
//
//              }
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG,
//                      "Nostro ref no nostref_MT103102 count" + val + "value103" + value103);
//              }
//              // MT202 start
//              query = "select count(*) from ETT_NOSTRO_UTILITY_MT202 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
//                    + nostref_MT103102 + "'";
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG, "Nostro ref no ETT_NOSTRO_UTILITY_MT202 query" + query);
//              }
//              con = ConnectionMaster.getConnection();
//              ps = con.prepareStatement(query);
//              rs = ps.executeQuery();
//              int value202 = 0;
//              if (rs.next()) {
//                value202 = rs.getInt(1);
//
//              }
//
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG,
//                      "Nostro ref no nostref_MT103102 count" + val + "value202" + value202);
//              }
//              // MT202 end
//              if ((nostref_MT103102.length() > 0)
//                    && ((val == 0 && value103 == 0) && (val == 0 && value202 == 0))) {
//                Loggers.general().info(LOG, "MT202/103 EMPTY");
//                setNOSTAMT("");
//                setNOSTDAT("");
//                setPOOLAMT("");
//                setNOSTACC("");
//                setMTMESG("");
//                setINWMSG("");
//                setNOSTOUT("");
//              } else {
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG, "MT103 Nostro Reference else" + val);
//                }
//              }
//
//          } else if (nostref_MT940950.length() > 0) {
//              query = "SELECT count(*) FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
//                    + nostref_MT940950 + "'";
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG, "Nostro ref no nostref_MT940 query" + query);
//              }
//              ps = con.prepareStatement(query);
//              rs = ps.executeQuery();
//              int val = 0;
//              while (rs.next()) {
//                val = rs.getInt(1);
//              }
//              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                Loggers.general().info(LOG, "Nostro ref no nostref_MT940 count" + val);
//              }
//              if ((nostref_MT940950.length() > 0) && val == 0) {
//                Loggers.general().info(LOG, "MT940 EMPTY");
//                setNOSTAMT("");
//                setNOSTDAT("");
//                setPOOLAMT("");
//                setNOSTACC("");
//                setMTMESG("");
//                setINWMSG("");
//                setNOSTOUT("");
//
//              } else {
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG, "MT940 Nostro Reference else" + val);
//                }
//              }
//          }
//
//        } catch (Exception e1) {
//          if (dailyval_Log.equalsIgnoreCase("YES")) {
//              Loggers.general().info(LOG, "Exception Nostro ref no validation" + e1.getMessage());
//          }
//        }
//
                  String forwardUid = getFDTRSUID().trim();
                  String nostro_Reference_no1 = "";
                  String nostro_Reference_no2 = "";
                  String nostro_value_date = "";
                  con = ConnectionMaster.getConnection();
                  String Credit_Amount = "";
                  String Credit_currency = "";
                  String CreditAccount = "";
                  String available_Amount = "";

                  String query = "SELECT AMOUNT AS Credit_Amount, CCY AS Credit_currency, TO_CHAR(VALUE_DATE,'DD/MM/YY') AS nostro_value_date,  NOSTRO AS CreditAccount,INST_REF_NUM,OWNER_REF_NUM FROM UID_CREDIT_DAILY_TBL WHERE UNIQUE_ID='"
                              + forwardUid + "'";
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
                  }

                  ps = con.prepareStatement(query);
                  rs = ps.executeQuery();
                  if (rs.next()) {
                        // setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
                        nostro_value_date = rs.getString(3);

                        Credit_Amount = rs.getString(1);
                        Credit_currency = rs.getString(2);
                        nostro_Reference_no1 = rs.getString(6);
                        nostro_Reference_no2 = rs.getString(5);
                        CreditAccount = rs.getString(4);
                        System.out.println("Nostro values  details===>" + Credit_Amount + " " + nostro_Reference_no2 + " "
                                    + CreditAccount);
                        setNOSTMT(nostro_Reference_no1);
                        setNOSTRM(nostro_Reference_no2);
                        setNOSTDAT(nostro_value_date);
                        setPOOLAMT(Credit_Amount + " " + Credit_currency);
                        setNOSTACC(CreditAccount);
                  }
//          String amountquery = "SELECT UID_AVAILABLE_AMOUNT||' '||ccy as availableAmount from REP_UID_AVAILABLE_AMOUNT where UNIQUE_ID='"+ forwardUid + "'";
//          ps1 = con.prepareStatement(amountquery);
//          rs1 = ps1.executeQuery();
//          if (rs1.next()) {
//                available_Amount=rs1.getString(1);
//                System.out.println("aVAILABLE AMOUNT "+available_Amount);
//                setNOSTAMT(available_Amount);
//          }
            } catch (Exception e) {
                  e.printStackTrace();
            }

            finally {
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
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
      }

      public void onNOSTROEXPBILLclayButton() {
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

                  String forwardUid = getFDTRSUID().trim();
                  String nostro_Reference_no1 = "";
                  String nostro_Reference_no2 = "";
                  String nostro_value_date = "";
                  con = ConnectionMaster.getConnection();
                  String Credit_Amount = "";
                  String Credit_currency = "";
                  String CreditAccount = "";
                  String available_Amount = "";

                  String query = "SELECT AMOUNT AS Credit_Amount, CCY AS Credit_currency, TO_CHAR(VALUE_DATE,'DD/MM/YY') AS nostro_value_date,  NOSTRO AS CreditAccount,INST_REF_NUM,OWNER_REF_NUM FROM UID_CREDIT_DAILY_TBL WHERE UNIQUE_ID='"
                              + forwardUid + "'";
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
                  }

                  ps = con.prepareStatement(query);
                  rs = ps.executeQuery();
                  if (rs.next()) {
                        // setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
                        nostro_value_date = rs.getString(3);

                        Credit_Amount = rs.getString(1);
                        Credit_currency = rs.getString(2);
                        nostro_Reference_no1 = rs.getString(6);
                        nostro_Reference_no2 = rs.getString(5);
                        CreditAccount = rs.getString(4);
                        System.out.println("Nostro values  details===>" + Credit_Amount + " " + nostro_Reference_no2 + " "
                                    + CreditAccount);
                        setNOSTMT(nostro_Reference_no1);
                        setNOSTRM(nostro_Reference_no2);
                        setNOSTDAT(nostro_value_date);
                        setPOOLAMT(Credit_Amount + " " + Credit_currency);
                        setNOSTACC(CreditAccount);
                  }
                  String amountquery = "SELECT AVBL_AMOUNT||' '||CCY as availableAmount from UID_CREDIT_DAILY_TRAN_TBL where UNIQUE_ID='"
                              + forwardUid + "' ORDER BY ROW_SEQ DESC fetch first 1 row only";
                  ps1 = con.prepareStatement(amountquery);
                  rs1 = ps1.executeQuery();
                  if (rs1.next()) {
                        available_Amount = rs1.getString(1);
                        System.out.println("aVAILABLE AMOUNT " + available_Amount);
                        setNOSTAMT(available_Amount);
                  }
            } catch (Exception e) {
                  e.printStackTrace();

            }

            finally {
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
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
      }
      // public void setNostroOutstandAmountForMT940(String MT_Ref, String CCY,
      // String creditAmt) {
      // // Loggers.general().info(LOG,"setNostroOutstandAmountForMT940 Occurred");
      // try {
      // double creditAmount = 0.00;
      // double nostroOutstandAmt = 0.00;
      // double currAmt = 0.00;
      //
      // // Loggers.general().info(LOG,"creditAmt length - " + creditAmt.length());
      // if (creditAmt.length() > 0) {
      // creditAmount = Double.parseDouble(creditAmt);
      // }
      //
      // con = ConnectionMaster.getConnection();
      // String query = "select total_utilised_amt from
      // ETTV_MT940_OUT_UTILISED_AMT where NOSTRO_REF='" + MT_Ref
      // + "'";
      // // Loggers.general().info(LOG,"query - " + query);
      // PreparedStatement ps = con.prepareStatement(query);
      //
      // ResultSet rs = ps.executeQuery();
      //
      // while (rs.next()) {
      // currAmt = rs.getDouble(1);
      // }
      //
      // if (currAmt > 0) {
      // // Loggers.general().info(LOG,"entered if in
      // // setNostroOutstandAmountForMT940");
      // nostroOutstandAmt = creditAmount - currAmt;
      // } else {
      // // Loggers.general().info(LOG,"entered else in
      // // setNostroOutstandAmountForMT940");
      // nostroOutstandAmt = creditAmount;
      // }
      // // Loggers.general().info(LOG,"Nostro outstanding value - " +
      // // nostroOutstandAmt);
      // String nostroOutAmt = String.valueOf(nostroOutstandAmt);
      //
      // setNOSTOUT(nostroOutAmt + " " + CCY);
      //
      // rs.close();
      // ps.close();
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"Exception occurred in
      // // setNostroOutstandAmountForMT940 - " + e.getMessage());
      // } finally {
      // // Loggers.general().info(LOG,"setNostroOutstandAmountForMT940 Exiting");
      // }
      // }

      /*
       * @Override public void onFetchPreshipEXPCOLSETTclayButton(){ try{
       * Loggers.general().info(LOG,"New PreShipment method is Called");
       * EnigmaArray<ExtEventLoanDetailsEntityWrapper>
       * loanDetails=getExtEventLoanDetailsData();
       * for(ExtEventLoanDetailsEntityWrapper detailsEntityWrapper : loanDetails){
       * removeExtEventLoanDetails(detailsEntityWrapper); }
       *
       * } catch(Exception e){ e.printStackTrace(); Loggers.general().info(LOG,
       * "Exception in PreShip Fetch Test"); } }
       */

      public boolean deleteLoanDetails() {
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
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "New PreShipment method is Called");
                  }

                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> loanDetails = getExtEventLoanDetailsData();
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "PreShipment Grid details  " + loanDetails.getSize());

                  }
                  for (ExtEventLoanDetailsEntityWrapper detailsEntityWrapper : loanDetails) {
                        removeExtEventLoanDetails(detailsEntityWrapper);
                  }
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "PreShipment Grid details after delete " + loanDetails.getSize());

                  }
                  return true;
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception in PreShip Fetch Test" + e.getMessage());
                  }
                  return false;
            }
      }

      public boolean deleteGridDetails() {
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
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Advance Table grid is clear in ODC");
                  }
                  EnigmaArray<ExtEventAdvanceTableEntityWrapper> loanDetails = getExtEventAdvanceTableData();

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Advance Grid size" + loanDetails.getSize());
                  }
                  for (ExtEventAdvanceTableEntityWrapper detailsEntityWrapper : loanDetails) {
                        removeExtEventAdvanceTable(detailsEntityWrapper);
                  }
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Advance Grid details after delete " + loanDetails.getSize());

                  }
                  return true;
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception in Grid details delete" + e.getMessage());
                  }
                  return false;
            }
      }

      public void onDELETEEXPCOLSETTclayButton() {
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
            String step_input = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
            if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")
                        && (step_input.equalsIgnoreCase("CSM") || step_input.equalsIgnoreCase("CBS Maker"))) {

                  try {
                        String refNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");

                        String query = "select step.status from baseevent ev, master m, stephist step, eventstep evstep where ev.master_key = m.key97 and step.event_key = ev.key97 and step.eventstep = evstep.key97 and m.master_ref = '"
                                    + refNumber + "' order by step.timestart desc";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Step ID query in ODC pane===> " + query);
                        }
                        con = ConnectionMaster.getConnection();
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                              String stepID = rs.getString(1).trim();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Custom value step id===> " + stepID);
                              }
                              if (stepID.length() > 0 && !stepID.equalsIgnoreCase("P")) {
                                    deleteGridDetails();
                                    deleteLoanDetails();
                              }
                              getBtnDELETEEXPCOLSETTclay().setEnabled(false);
                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception Custom value cleared===> " + e.getMessage());
                        }

                  } finally {
                        try {
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

            }

      }

      private Statement ConnectionMaster() {
            // TODO Auto-generated method stub
            return null;
      }

      public void onCIFMARGINEXPCOLFECclayButton() {

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
                  String facid = getFACLTYID().trim();

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Facility id initially Finance exisiting collection--->" + facid);

                  }
                  BigDecimal hundred = new BigDecimal(100);
                  DecimalFormat diff = new DecimalFormat("0.00");
                  diff.setMaximumFractionDigits(2);
                  // BigDecimal notionalRate = new BigDecimal(1);
                  BigDecimal financeAmount = new BigDecimal(0);
                  BigDecimal margin_Amt = new BigDecimal(0);
                  // String magn = "";
                  String billAmt = "";
                  String financeAmt = "";
                  String curr = "";
                  // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                  try {
                        billAmt = getDriverWrapper().getEventFieldAsText("B+FD", "v", "m");

                        margin_Amt = new BigDecimal(billAmt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Bill amount--->" + margin_Amt);

                        }
                        // margin_Amount = margin_Amt.divide(hundred);
                        // String marginAmount = diff.format(margin_Amount);
                        curr = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");
                        financeAmt = getDriverWrapper().getEventFieldAsText("B+AF", "v", "m");
                        financeAmount = new BigDecimal(financeAmt);
                        // financeVal = margin_Amt.divide(financeAmount);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Finance amount--->" + financeAmount);
                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception Finance amount" + e.getMessage());
                        }
                  }

                  if (!facid.equalsIgnoreCase("") && facid != null) {
                        String query = "select MARGIN,INTEREST,PENINTEREST,PLR,BASERATE from customermargin where facility='"
                                    + facid + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Query resultcustomer margin " + query);
                        }
                        con = ConnectionMaster.getConnection();
                        dmsp = con.prepareStatement(query);
                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {

                              String magVal = dmsr.getString(1);
                              String interest = dmsr.getString(2);
                              String penalInt = dmsr.getString(3);
                              String plr = dmsr.getString(4);
                              String baseRate = dmsr.getString(5);
                              // Loggers.general().info(LOG,"magn--->" + magn);
                              BigDecimal magValue = new BigDecimal(magVal);
                              // Loggers.general().info(LOG,"Margin " + magn);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG, "Final Bill amount--->" + margin_Amt);
                                    Loggers.general().info(LOG, "Final Finance amount--->" + financeAmount);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Total margin amount--->" + totalVal);
                              }
                              setPMARGIN(magVal);

                              setINTERDET(interest);
                              setTENO(penalInt);
                              setOURS(plr);
                              setLIBORAT(baseRate);

                              setMARAMT(totalVal + curr);
                              // setPRMARAMT(ks + "" + curr);
                              BigDecimal financeValue = financeAmount.add(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Total value of finance and margin amount--->" + financeValue);
                              }
                              BigDecimal totalValue = margin_Amt.subtract(financeValue);
                              String finalVal = diff.format(totalValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Final value of finance and margin amount====>" + finalVal);
                              }
                              if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
                                    setBALAMT(finalVal + curr);
                              } else {
                                    finalVal = "0";
                                    setBALAMT(finalVal + curr);
                              }
                        }

                  } else if (facid.equalsIgnoreCase("") || facid == null) {

                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");

                        String magVal = getPMARGIN();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Margin percentage--->" + magVal);
                        }
                        if ((magVal != null || !magVal.equalsIgnoreCase("")) && magVal.length() > 0) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Facility id blank and Margin percentage====>" + magVal);
                              }
                              BigDecimal magValue = new BigDecimal(magVal);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG, "Final Bill amount--->" + margin_Amt);
                                    Loggers.general().info(LOG, "Final Finance amount--->" + financeAmount);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Total margin amount--->" + totalVal);
                              }
                              setMARAMT(totalVal + curr);
                              // setPRMARAMT(ks + "" + curr);
                              BigDecimal financeValue = financeAmount.add(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Total value of finance and margin amount--->" + financeValue);
                              }
                              BigDecimal totalValue = margin_Amt.subtract(financeValue);
                              String finalVal = diff.format(totalValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Final value of finance and margin amount====>" + finalVal);
                              }
                              if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
                                    setBALAMT(finalVal + curr);
                              } else {
                                    finalVal = "0";
                                    setBALAMT(finalVal + curr);
                              }
                        } else {
                              setPMARGIN("");
                              setMARAMT("");
                              setINTERDET("");
                              setTENO("");
                              setOURS("");
                              setLIBORAT("");
                        }

                  }

                  else {

                        setPMARGIN("");
                        setMARAMT("");
                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");
                        //// setPRMARAMT(0.0 + "" + curr);

                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception in margin calcultion " + e.getMessage());
                  }
            }
      }

      @SuppressWarnings("unused")
      public void onFetchPreshipEXPCOLSETTclayButton() {
            String strLog = "Log";
            String dailyval_Log = "";
            // String Subproduct="";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            // Loggers.general().info(LOG,"Enetered onFetchpreShipEXPCOLFECclayButton");
            Connection con = null;
            PreparedStatement prepareStmt = null;

            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";
            // int count = 0;
            // int k = 0;
            String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };
            int temp_count = 0;
            String startCurr = "";
            double sumAmount = 0;
            ArrayList<String> sumAmountInAllCurrencies;
            int sequence = 0;
            int count = 0;
            int cc = 0;
            String loanType = "";// This will fetch the Loan Type from the grid
            ArrayList<String> loans = new ArrayList<String>();
            try {

                  deleteLoanDetails();

                  sumAmountInAllCurrencies = new ArrayList<String>();
                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  /*
                   * Subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                   * Loggers.general().info(LOG,"Preshipment subproduct"+Subproduct);
                   */
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  // Loggers.general().info(LOG,"temp value " + temp_count);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "temp value " + temp_count);
                  }
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
                        // Loggers.general().info(LOG,"loan query is " + loan_query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "loan query is " + loan_query);
                        }
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {

                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              loanType = ress.getString("TYPE");
                              // Loggers.general().info(LOG,"eneteerd while");

                        }
                        // Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        for (int i = 0; i < loans.size() / 3; i++) {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              /*
                               * setPRESHIP(Subproduct); Loggers.general().info(LOG,"Preshipment subproduct"+
                               * getPRESHIP());
                               */
                              // Loggers.general().info(LOG,"va " + cc);
                        }
                        //// setLOANTYPE(loanType);
                        try {
                              // Loggers.general().info(LOG,"PreShipment new Changes");
                              sumAmountInAllCurrencies = new ArrayList<String>();

                              for (int a = 0; a < currencies.length; a++) {
                                    try {
                                          String query = "SELECT NVL(SUM(REPAYAMT),'0') FROM ETT_PRESHIPMENT_APISERVER WHERE CURR='"
                                                      + currencies[a] + "' AND masref='" + masref + "' and eventref='" + eventRef
                                                      + "' ORDER BY CURR DESC";
                                          // Loggers.general().info(LOG,"PreShipment Sum query " +
                                          // query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "PreShipment Sum query " + query);
                                          }
                                          PreparedStatement prep = con.prepareStatement(query);
                                          ResultSet result = prep.executeQuery();
                                          if (result.next()) {
                                                double amount = 0;
                                                double dob_amt = result.getDouble(1);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + dob_amt);
                                                String amt = result.getString(1);
                                                BigDecimal amt_dec = new BigDecimal(amt);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in String " + amt + " Bigdecimal" +
                                                // amt_dec);
                                                if (currencies[a].equalsIgnoreCase("EUR")) {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in if loop" + amount);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "PRESHIPMENT Value of Amount in if loop" + amount);
                                                      }

                                                } else {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in else loop" + amount);
                                                }
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + amount);
                                                // -----------DECIMAL
                                                // ISSUE-----------------------------------------------------------

                                                if (currencies[a].equalsIgnoreCase("USD")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {

                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("INR")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("JPY")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 1;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("GBP")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                // -------------------------------------------------------------------------------------
                                                // 18-aug-19 decimal to bigdecimal for exponential in amount
                                                BigDecimal b_amt = new BigDecimal(amount);
                                                Currency cur = Currency.getInstance(currencies[a]);
                                                int precision = cur.getDefaultFractionDigits();
                                                RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
                                                BigDecimal roundoffvalue = null;
                                                roundoffvalue = b_amt.setScale(precision, DEFAULT_ROUNDING);
                                                sumAmountInAllCurrencies.add(roundoffvalue + " " + currencies[a]);
//                                              //sumAmountInAllCurrencies.add(amount + " " + currencies[a]);
                                          } else {
                                                // Loggers.general().info(LOG,"ENtered else since the
                                                // Resultset is empty");
                                          }
                                          result.close();
                                          prep.close();

                                    } catch (SQLException sqlexception) {
                                          sqlexception.printStackTrace();
                                    }

                              }
                              // Loggers.general().info(LOG," sumAmountInAllCurrencies Size " +
                              // sumAmountInAllCurrencies.size());
                              for (int b = 0; b < sumAmountInAllCurrencies.size(); b++) {
                                    // Loggers.general().info(LOG,"Inside For Loop " + b);
                                    if (b == 0) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTUSD(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 1) {
                                          setTOTJPY(sumAmountInAllCurrencies.get(b));
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Value of b " + b);
                                          }
                                    }
                                    if (b == 2) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTINR(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 3) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTGBP(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 4) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Value of b " + b);
                                          }
                                          setTOTEUR(sumAmountInAllCurrencies.get(b));
                                    }
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in try inside try" +
                              // e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Exception in try inside try" + e.getMessage());
                              }
                        }

                  }
                  getExtEventLoanDetailsNew().setEnabled(false);
                  getExtEventLoanDetailsDelete().setEnabled(false);
                  getExtEventLoanDetailsUpdate().setEnabled(false);
                  getExtEventLoanDetailsUp().setEnabled(false);
                  getExtEventLoanDetailsDown().setEnabled(false);

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception int preshipment fetch " +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception int preshipment fetch " + e.getMessage());
                  }
            } finally {
                  ConnectionMaster.surrenderDB(con, prepareStmt, result_loan_emplty);
            }

      }

      public void onFetchpreShipEXPCOLFECclayButton() {
            String strLog = "Log";
            String dailyval_Log = "";
            // String Subproduct="";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            // Loggers.general().info(LOG,"Enetered onFetchpreShipEXPCOLFECclayButton");
            Connection con = null;
            PreparedStatement prepareStmt = null;

            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";
            // int count = 0;
            // int k = 0;
            String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };
            int temp_count = 0;
            String startCurr = "";
            double sumAmount = 0;
            ArrayList<String> sumAmountInAllCurrencies;
            int sequence = 0;
            int count = 0;
            int cc = 0;
            String loanType = "";// This will fetch the Loan Type from the grid
            ArrayList<String> loans = new ArrayList<String>();
            try {
                  deleteLoanDetails();

                  sumAmountInAllCurrencies = new ArrayList<String>();
                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  /*
                   * Subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                   * Loggers.general().info(LOG,"Preshipment subproduct"+Subproduct);
                   */
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  // Loggers.general().info(LOG,"temp value " + temp_count);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "temp value " + temp_count);
                  }
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
                        Loggers.general().info(LOG, "loan query for Exixting collection " + loan_query);
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {

                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              loanType = ress.getString("TYPE");
                              // Loggers.general().info(LOG,"eneteerd while");

                        }
                        // Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Loan Type Value is " + loanType);
                        }
                        for (int i = 0; i < loans.size() / 3; i++) {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              /*
                               * setPRESHIP(Subproduct); Loggers.general().info(LOG,"Preshipment subproduct"+
                               * getPRESHIP());
                               */
                              // Loggers.general().info(LOG,"va " + cc);
                        }
                        //// setLOANTYPE(loanType);
                        try {
                              // Loggers.general().info(LOG,"PreShipment new Changes");
                              sumAmountInAllCurrencies = new ArrayList<String>();

                              for (int a = 0; a < currencies.length; a++) {
                                    try {
                                          String query = "SELECT NVL(SUM(REPAYAMT),'0') FROM ETT_PRESHIPMENT_APISERVER WHERE CURR='"
                                                      + currencies[a] + "' AND masref='" + masref + "' and eventref='" + eventRef
                                                      + "' ORDER BY CURR DESC";
                                          Loggers.general().info(LOG, "PreShipment Sum query for FEC " + query);
                                          PreparedStatement prep = con.prepareStatement(query);
                                          ResultSet result = prep.executeQuery();
                                          if (result.next()) {
                                                double amount = 0;
                                                double dob_amt = result.getDouble(1);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + dob_amt);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,
                                                                  "Entered if and the Value of Amount in double" + dob_amt);
                                                }
                                                String amt = result.getString(1);
                                                BigDecimal amt_dec = new BigDecimal(amt);
                                                Loggers.general().info(LOG, "Entered if and the REPAYAMT in  Bigdecimal" + amt_dec);
                                                if (currencies[a].equalsIgnoreCase("EUR")) {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in if loop" + amount);

                                                } else {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in else loop" + amount);
                                                }
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + amount);
                                                // -----------DECIMAL
                                                // ISSUE-----------------------------------------------------------

                                                if (currencies[a].equalsIgnoreCase("USD")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "PRESHIPMENT Value of Amount in before" + amount);
                                                      }
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {

                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("INR")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "PRESHIPMENT Value of Amount in before" + amount);
                                                      }
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("JPY")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "PRESHIPMENT Value of Amount in before" + amount);
                                                      }
                                                      amount = result.getDouble(1) / 1;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("GBP")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                // -------------------------------------------------------------------------------------

//                                        sumAmountInAllCurrencies.add(amount + " " + currencies[a]);
                                                // 18-aug-19 decimal to bigdecimal for exponential in amount
                                                BigDecimal b_amt = new BigDecimal(amount);
                                                Currency cur = Currency.getInstance(currencies[a]);
                                                int precision = cur.getDefaultFractionDigits();
                                                RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
                                                BigDecimal roundoffvalue = null;
                                                roundoffvalue = b_amt.setScale(precision, DEFAULT_ROUNDING);
                                                sumAmountInAllCurrencies.add(roundoffvalue + " " + currencies[a]);
                                          } else {
                                                // Loggers.general().info(LOG,"ENtered else since the
                                                // Resultset is empty");
                                          }
                                          result.close();
                                          prep.close();

                                    } catch (Exception e) {
                                          Loggers.general().info(LOG, "Exception ETT_PRESHIPMENT_APISERVER EFC" + e.getMessage());
                                    }

                              }
                              Loggers.general().info(LOG, " sumAmountInAllCurrencies Size " + sumAmountInAllCurrencies);
                              for (int b = 0; b < sumAmountInAllCurrencies.size(); b++) {
                                    // Loggers.general().info(LOG,"Inside For Loop " + b);
                                    if (b == 0) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTUSD(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 1) {
                                          setTOTJPY(sumAmountInAllCurrencies.get(b));
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                    }
                                    if (b == 2) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTINR(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 3) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTGBP(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 4) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTEUR(sumAmountInAllCurrencies.get(b));
                                    }
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in try inside try" +
                              // e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Exception in try inside try" + e.getMessage());
                              }
                        }

                  }
                  getExtEventLoanDetailsNew().setEnabled(false);
                  getExtEventLoanDetailsDelete().setEnabled(false);
                  getExtEventLoanDetailsUpdate().setEnabled(false);
                  getExtEventLoanDetailsUp().setEnabled(false);
                  getExtEventLoanDetailsDown().setEnabled(false);

            } catch (Exception e) {
                  Loggers.general().info(LOG, "Exception int preshipment fetch FEC" + e.getMessage());
            } finally {
                  ConnectionMaster.surrenderDB(con, prepareStmt, result_loan_emplty);
            }

      }

      // public void ontotalcalcEXPCOLFECclayButton() {
      // // Loggers.general().debug(LOG, "on{}Button:{}", "totalcalcOUTDOCFECL",
      // // ValidationTexts.METHOD_NOT_IMPLEMENTED);
      // try {
      // //Loggers.general().info(LOG,"enter into INVOICE CALCULTAE");
      // double totINVcamt = 0.00;
      // double testdouble = 0.00;
      // String billCurr =
      // String.valueOf(getDriverWrapper().getEventFieldAsText("AMT", "v", "c"));
      // EnigmaArray<ExtEventInvoiceCollectionsEntityWrapper> liste =
      // getExtEventInvoiceCollectionsData();
      // if (liste.getSize() > 0) {
      // Iterator<ExtEventInvoiceCollectionsEntityWrapper> iterator1 =
      // liste.iterator();
      // while (iterator1.hasNext()) {
      // ExtEventInvoiceCollectionsEntityWrapper
      // ExtEventInvoiceCollectionsEntityWrapper1 =
      // (ExtEventInvoiceCollectionsEntityWrapper) iterator1
      // .next();
      // String testData =
      // ExtEventInvoiceCollectionsEntityWrapper1.getCINVOAMTAmount();
      // //Loggers.general().info(LOG,"INVOICE Amount ------->" + testData);
      // testdouble = Double.parseDouble(testData);
      // totINVcamt = totINVcamt + testdouble;
      // StringBuilder str = new StringBuilder(String.format("%.2f", (totINVcamt /
      // 100)));
      // //Loggers.general().info(LOG,"value of amount + currency" +
      // str.append(billCurr).toString());
      // /*
      // * String str=String.format("%.2f",(totINVcamt/100));
      // * //Loggers.general().info(LOG,"Str is "+str); str=str+billCurr;
      // * //Loggers.general().info(LOG,"Final STr is "+str);
      // */
      // setTRAMT(str.toString());
      // }
      // }
      // } catch (Exception e) {
      // //Loggers.general().info(LOG,"Exeception" + e.getMessage());
      // }
      // }

      // public void ontotalcalcOUTDOCCOLACCclayButton() {
      // // Loggers.general().debug(LOG, "on{}Button:{}",
      // // "totalcalcOITDOCACPTLAY", ValidationTexts.METHOD_NOT_IMPLEMENTED);
      // try {
      // //Loggers.general().info(LOG,"enter into INVOICE CALCULTAE");
      // double totINVcamt = 0.00;
      // double testdouble = 0.00;
      // String billCurr =
      // String.valueOf(getDriverWrapper().getEventFieldAsText("AMT", "v", "c"));
      // EnigmaArray<ExtEventInvoiceCollectionsEntityWrapper> liste =
      // getExtEventInvoiceCollectionsData();
      // if (liste.getSize() > 0) {
      // Iterator<ExtEventInvoiceCollectionsEntityWrapper> iterator1 =
      // liste.iterator();
      // while (iterator1.hasNext()) {
      // ExtEventInvoiceCollectionsEntityWrapper
      // ExtEventInvoiceCollectionsEntityWrapper1 =
      // (ExtEventInvoiceCollectionsEntityWrapper) iterator1
      // .next();
      // String testData =
      // ExtEventInvoiceCollectionsEntityWrapper1.getCINVOAMTAmount();
      // //Loggers.general().info(LOG,"INVOICE Amount ------->" + testData);
      // testdouble = Double.parseDouble(testData);
      // totINVcamt = totINVcamt + testdouble;
      // StringBuilder str = new StringBuilder(String.format("%.2f", (totINVcamt /
      // 100)));
      // //Loggers.general().info(LOG,"value of amount + currency" +
      // str.append(billCurr).toString());
      // /*
      // * String str=String.format("%.2f",(totINVcamt/100));
      // * //Loggers.general().info(LOG,"Str is "+str); str=str+billCurr;
      // * //Loggers.general().info(LOG,"Final STr is "+str);
      // */
      // setTRAMT(str.toString());
      // }
      // }
      // } catch (Exception e) {
      // //Loggers.general().info(LOG,"Exeception" + e.getMessage());
      // }
      // }

      /*
       * public void ononcalculateEXPBILLclayButton() { //
       * Loggers.general().debug(LOG, "on{}Button:{}", "totalcalcELCDOCLAYOUT",
       * ValidationTexts.METHOD_NOT_IMPLEMENTED); try { //Loggers.general().info(LOG,
       * "enter into INVOICE CALCULTAE"); double totINVcamt = 0.00; double
       * testdouble=0.00; String billCurr
       * =String.valueOf(getDriverWrapper().getEventFieldAsText("AMT", "v", "c"));
       * EnigmaArray<ExtEventInvoiceCollectionsEntityWrapper> liste =
       * getExtEventInvoiceCollectionsData(); if(liste.getSize()>0){
       * Iterator<ExtEventInvoiceCollectionsEntityWrapper> iterator1 =
       * liste.iterator(); while (iterator1.hasNext()) {
       * ExtEventInvoiceCollectionsEntityWrapper
       * ExtEventInvoiceCollectionsEntityWrapper1 =
       * (ExtEventInvoiceCollectionsEntityWrapper) iterator1.next(); String testData =
       * ExtEventInvoiceCollectionsEntityWrapper1.getCINVOAMTAmount();
       * //Loggers.general().info(LOG,"INVOICE Amount ------->"+testData); testdouble=
       * Double.parseDouble(testData); totINVcamt=totINVcamt+testdouble;
       * //setTRAMT(String.valueOf(totINVcamt/100));
       * //totINVcamt=totINVcamt+testdouble; StringBuilder str = new
       * StringBuilder(String.format("%.2f",(totINVcamt/100)));
       * //Loggers.general().info(LOG,"value of amount + currency"
       * +str.append(billCurr).toString() ); setTRAMT(str.toString());
       * ////Loggers.general().info(LOG,"totIfircamt in loop " + totINVcamt); String
       * query=
       * "SELECT COD.* FROM MASTER MAS,COLLDRAFT COD, TIDATAITEM TID WHERE trim(MAS.KEY97)=trim(TID.MASTER_KEY) AND trim(TID.KEY97) =trim(COD.KEY97) AND trim(MAS.MASTER_REF)='"
       * +cm.getmasRefNo()+"'"; //Loggers.general().info(LOG,"query value" + query); }
       * }
       *
       * } catch(Exception e) {//Loggers.general().info(LOG,"Exeception" +
       * e.getMessage()); }
       *
       * }
       */

      /*
       * public void ononcalculateOUTDOCCOLAMDclayButton() { //
       * Loggers.general().debug(LOG, "on{}Button:{}", "totalcalcEXPLCCOLLAMENDL",
       * ValidationTexts.METHOD_NOT_IMPLEMENTED); try { //Loggers.general().info(LOG,
       * "enter into INVOICE CALCULTAE"); double totINVcamt = 0.00; double
       * testdouble=0.00; String billCurr
       * =String.valueOf(getDriverWrapper().getEventFieldAsText("AMT", "v", "c"));
       * EnigmaArray<ExtEventInvoiceCollectionsEntityWrapper> liste =
       * getExtEventInvoiceCollectionsData(); if(liste.getSize()>0){
       * Iterator<ExtEventInvoiceCollectionsEntityWrapper> iterator1 =
       * liste.iterator(); while (iterator1.hasNext()) {
       * ExtEventInvoiceCollectionsEntityWrapper
       * ExtEventInvoiceCollectionsEntityWrapper1 =
       * (ExtEventInvoiceCollectionsEntityWrapper) iterator1.next(); String testData =
       * ExtEventInvoiceCollectionsEntityWrapper1.getCINVOAMTAmount();
       * //Loggers.general().info(LOG,"INVOICE Amount ------->"+testData); testdouble=
       * Double.parseDouble(testData); totINVcamt=totINVcamt+testdouble;
       * //setTRAMT(String.valueOf(totINVcamt/100));
       * //totINVcamt=totINVcamt+testdouble; StringBuilder str = new
       * StringBuilder(String.format("%.2f",(totINVcamt/100)));
       * //Loggers.general().info(LOG,"value of amount + currency"
       * +str.append(billCurr).toString() ); setTRAMT(str.toString());
       * ////Loggers.general().info(LOG,"totIfircamt in loop " + totINVcamt); } }
       *
       * } catch(Exception e) {//Loggers.general().info(LOG,"Exeception" +
       * e.getMessage()); }
       *
       * }
       */

      /*
       * public void ononcalculateOUTDOCCOLADJclayButton() { //
       * Loggers.general().debug(LOG, "on{}Button:{}", "totalcalcOUTDOADJT",
       * ValidationTexts.METHOD_NOT_IMPLEMENTED); try { //Loggers.general().info(LOG,
       * "enter into INVOICE CALCULTAE"); double totINVcamt = 0.00; double
       * testdouble=0.00; String billCurr
       * =String.valueOf(getDriverWrapper().getEventFieldAsText("AMT", "v", "c"));
       * EnigmaArray<ExtEventInvoiceCollectionsEntityWrapper> liste =
       * getExtEventInvoiceCollectionsData(); if(liste.getSize()>0){
       * Iterator<ExtEventInvoiceCollectionsEntityWrapper> iterator1 =
       * liste.iterator(); while (iterator1.hasNext()) {
       * ExtEventInvoiceCollectionsEntityWrapper
       * ExtEventInvoiceCollectionsEntityWrapper1 =
       * (ExtEventInvoiceCollectionsEntityWrapper) iterator1.next(); String testData =
       * ExtEventInvoiceCollectionsEntityWrapper1.getCINVOAMTAmount();
       * //Loggers.general().info(LOG,"INVOICE Amount ------->"+testData); testdouble=
       * Double.parseDouble(testData); totINVcamt=totINVcamt+testdouble;
       * //setTRAMT(String.valueOf(totINVcamt/100));
       * //totINVcamt=totINVcamt+testdouble; StringBuilder str = new
       * StringBuilder(String.format("%.2f",(totINVcamt/100)));
       * //Loggers.general().info(LOG,"value of amount + currency"
       * +str.append(billCurr).toString() ); setTRAMT(str.toString());
       * ////Loggers.general().info(LOG,"totIfircamt in loop " + totINVcamt); } }
       *
       * } catch(Exception e) {//Loggers.general().info(LOG,"Exeception" +
       * e.getMessage()); }
       *
       * }
       */
      public void ononfetchEXPCOLSETTclayButton() {
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

                  String inwnum = "";
                  double totalAmt = 0;
                  String balanceValCurrency = ""; // String
                  String balance = "0.0";
                  String creditcur = "";
                  String bank_ADCODE = "";
                  long creditAmount = 0;
                  long balanceAmt = 0;
                  String cif_no = "";
                  String utilize = "";
                  String utilizeCcy = "";
                  BigDecimal utilizeamt = new BigDecimal(0);
                  BigDecimal hundred = new BigDecimal(100);
                  BigDecimal availamt = new BigDecimal(0);
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  String fircNumber = "";
                  String tempcountry = "";
                  String amtutil = "0.0";
                  long utilamt = 0;
                  String rbiCode = "";
                  String merchantTrading = getMACHTD().trim();
                  setBALAMT("");
                  try {

                        con = ConnectionMaster.getConnection();
                        EnigmaArray<ExtEventAdvanceTableEntityWrapper> liste = getExtEventAdvanceTableData();

                        for (int i = 0; i < liste.getSize().intValue(); i++) {
                              Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
                              while (iterator1.hasNext()) {
                                    ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
                                                .next();
                                    inwnum = fdwarapper1.getINWARD().trim();

                                    fircNumber = fdwarapper1.getFINUMB().trim();
                                    Loggers.general().info(LOG, "Firc number======>" + fircNumber);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Inward Remittance Number " + inwnum + " " + merchantTrading);
                                          Loggers.general().info(LOG, "FIRC Number " + fircNumber);
                                    }
//                      if (!inwnum.equalsIgnoreCase("")) {
//                          String rbiquery = "SELECT PURCODE FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
//                                + inwnum + "'";
//                          pst = con.prepareStatement(rbiquery);
//                          rs1 = pst.executeQuery();
//                          if (rs1.next()) {
//                             System.out.println("RBI Purpose code " + rs1.getString(1));
//                            rbiCode=rs1.getString(1);
//                          }
//                      }
                                    if (merchantTrading.equalsIgnoreCase("YES")) {
                                          if (!inwnum.equalsIgnoreCase("")) {
                                                String inwardDetails = "SELECT TRIM(ORDCUS_CST), TO_CHAR(TO_DATE(value_date,'dd-mm-yy'),'yyyy-mm-dd'), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE),PURCODE FROM ETTV_INWARD_REMITTANCE_AMOUNT  WHERE MAS_REF='"
                                                            + inwnum + "' AND TRIM(PURCODE)='P0108'";
                                                System.out.println(
                                                            "query for getting all fields in inward Remittance grid " + inwardDetails);
                                                pst = con.prepareStatement(inwardDetails);
                                                rs1 = pst.executeQuery();
                                                fdwarapper1.setINWARD(inwnum);// added as the user inputed value is encountered with
                                                                                                // space

                                                if (rs1.next()) {
                                                      fdwarapper1.setNAMREM(rs1.getString(1));
                                                      fdwarapper1.setDATREM(rs1.getString(2));
                                                      fdwarapper1.setCOUNREM(rs1.getString(3));
                                                      creditAmount = rs1.getLong(4);
                                                      creditcur = rs1.getString(5);
                                                      cif_no = rs1.getString(6);
                                                      System.out.println("name and date " + rs1.getString(1) + " " + rs1.getString(2)
                                                                  + " " + rs1.getString(3) + " " + rs1.getString(7));
                                                      fdwarapper1.setCUSCIFNO(cif_no);
                                                      bank_ADCODE = rs1.getString(7);
                                                      fdwarapper1.setADVRECB(bank_ADCODE);

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "Credit AMount " + creditAmount);
                                                      }
                                                } else {
                                                      //// Loggers.general().info(LOG,"entered else since
                                                      //// result set value returned nothing");
                                                }
                                                utilize = fdwarapper1.getAMTUTIL().trim();
                                                System.out.println("utilizeamt amount " + utilize);
                                                utilizeCcy = fdwarapper1.getAMTUTILCurrency();
                                                // System.out.println("utilizeamt amount " +utilizeCcy);
                                                int spacePosition1 = utilize.indexOf(" ");
                                                utilize = utilize.trim().substring(0, spacePosition1);
                                                utilize = utilize.replaceAll("[^0-9]", "");
                                                System.out.println("utilizeamt amount converted " + utilize);
                                                utilizeamt = new BigDecimal(utilize);
                                                utilizeamt = utilizeamt.divide(hundred);
                                                availamt = availamt.add(utilizeamt);

                                                // String BalAmtQuery = "SELECT
                                                // ext.ccy_1,NVL(SUM(ext.amtutil),0) AS TOTAL_AMT"
                                                // + " FROM exteventadv ext, BASEEVENT bev, MASTER
                                                // mas WHERE ext.FK_EVENT = bev.EXTFIELD "
                                                // + " AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS
                                                // IN ('i','c') AND ext.inward ='"
                                                // + inwnum + "' AND mas.MASTER_REF !='" +
                                                // masReference + "' GROUP BY ext.ccy_1 ";

                                                String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
                                                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                            + eventCode + "') AND ext.INWARD ='" + inwnum
                                                            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                            + eventCode + "') AND ext.INWARD ='" + inwnum
                                                            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
                                                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
                                                            + eventCode + "') AND ext.INWARD ='" + inwnum
                                                            + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,
                                                                  "Query for getting Inward Utilized Amount===>" + BalAmtQuery);
                                                }
                                                pst = con.prepareStatement(BalAmtQuery);
                                                rs1 = pst.executeQuery();
                                                if (rs1.next()) {

                                                      totalAmt = rs1.getDouble(1);

                                                      balanceValCurrency = creditcur;

                                                      double irmAmt = 0;
                                                      String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                                  + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                                  + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            System.out.println("IRM Clourse Query ---" + closureQuery);
                                                      }
                                                      pst = con.prepareStatement(closureQuery);
                                                      rs1 = pst.executeQuery();
                                                      if (rs1.next()) {
                                                            irmAmt = rs1.getDouble("IRMAMT");
                                                      } else {
                                                            irmAmt = 0;
                                                      }

                                                      totalAmt = totalAmt + irmAmt;

                                                      balanceAmt = (long) (creditAmount - totalAmt);

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            System.out.println("Balance Credit Amount-->" + balanceAmt);
                                                      }
                                                      // fdwarapper1.setCUSCIFNO(cif_no);
                                                      if (balanceAmt > 0) {

                                                            balance = String.valueOf(balanceAmt);
                                                            fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);

                                                      } else {
                                                            fdwarapper1.setBALANCE(0 + " " + balanceValCurrency);
                                                      }
                                                } else {

                                                      double irmAmt = 0;
                                                      String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                                  + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                                  + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "IRM Clourse Query --->" + closureQuery);
                                                      }
                                                      pst = con.prepareStatement(closureQuery);
                                                      rs1 = pst.executeQuery();
                                                      if (rs1.next()) {
                                                            irmAmt = rs1.getDouble("IRMAMT");
                                                      } else {
                                                            irmAmt = 0;
                                                      }

                                                      long balan_cret = (long) (creditAmount - irmAmt);

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "Balance Credit Amount-->" + balan_cret);
                                                      }

                                                      if (balan_cret > 0) {
                                                            String balan_Str = String.valueOf(balan_cret);
                                                            fdwarapper1.setBALANCE(balan_Str + " " + creditcur);

                                                      } else {
                                                            fdwarapper1.setBALANCE(0 + " " + creditcur);
                                                      }

                                                      // fdwarapper1.setCUSCIFNO(cif_no);

                                                }
                                          } else if (!fircNumber.equalsIgnoreCase("")) {
                                                String fircDetails = "SELECT ORDER_CUSTOMER AS REMITTER_NAME,trim(TO_CHAR(FIRC_DATE,'DD/MM/YY')) as remittance_date,"
                                                            + "trim(REM_COUNTRY) AS REMITTER_COUNTRY ,CIF_NO AS CIF_NUMBER,AVAILABLE_AMOUNT AS AVAILABLE_AMOUNT,PAID_AMOUNT AS UTILIZATION_AMOUNT,currency"
                                                            + " FROM ETT_FIRC_LODGEMENT where FIRC_SERIAL_NO='" + fircNumber + "'";

                                                Loggers.general().info(LOG,
                                                            "query for getting all fields in firc details " + fircDetails);
                                                pst = con.prepareStatement(fircDetails);
                                                rs1 = pst.executeQuery();
                                                if (rs1.next()) {
                                                      fdwarapper1.setNAMREM(rs1.getString(1));
                                                      fdwarapper1.setDATREM(rs1.getString(2));
                                                      Loggers.general().info(LOG, "Date of firc" + fdwarapper1.getDATREM());
                                                      tempcountry = rs1.getString(3);
                                                      // fdwarapper1.setCOUNREM(rs1.getString(3));
                                                      // creditAmount = rs1.getLong(4);
                                                      // creditcur = rs1.getString(5);
                                                      Loggers.general().info(LOG, "tempcountry" + tempcountry);
                                                      cif_no = rs1.getString(4);
                                                      fdwarapper1.setCUSCIFNO(cif_no);
                                                      balanceAmt = rs1.getLong(5);
                                                      Loggers.general().info(LOG, "balance amount firc" + balanceAmt);
                                                      balance = String.valueOf(balanceAmt);
                                                      // fdwarapper1.setBALANCE(balance);
                                                      Loggers.general().info(LOG, "balance firc" + balance);
                                                      utilamt = rs1.getLong(6);
                                                      Loggers.general().info(LOG, "util amount firc" + utilamt);
                                                      amtutil = String.valueOf(utilamt);
                                                      // fdwarapper1.setAMTUTIL(amtutil);
                                                      Loggers.general().info(LOG, "util " + amtutil);
                                                      balanceValCurrency = rs1.getString(7);

                                                } else {
                                                      //// Loggers.general().info(LOG,"entered else since
                                                      //// result set value returned nothing");
                                                }

                                                String firccurrency = "select trim(c7cna) as country from c7pf where trim(C7CNM)='"
                                                            + tempcountry + "'";
                                                Loggers.general().info(LOG,
                                                            "query for getting all fields in firc currency" + firccurrency);
                                                pst = con.prepareStatement(firccurrency);
                                                rs1 = pst.executeQuery();
                                                if (rs1.next()) {

                                                      fdwarapper1.setCOUNREM(rs1.getString(1));
                                                }
                                                Loggers.general().info(LOG, "Currency firc" + fdwarapper1.getCOUNREM());

                                                String tempavailamt = "SELECT E.Available_Amount*power(10,c.c8ced) as available_amount,e.currency"
                                                            + " FROM ETT_FIRC_LODGEMENT E,c8pf c WHERE e.CURRENCY =c.c8ccy "
                                                            + " AND e.FIRC_SERIAL_NO='" + fircNumber + "'";
                                                Loggers.general().info(LOG,
                                                            "query for getting all fields in firc available amount" + tempavailamt);
pst = con.prepareStatement(tempavailamt);
rs1 = pst.executeQuery();
if (rs1.next()) {
      balanceAmt = rs1.getLong(1);
      Loggers.general().info(LOG, "balance amount firc====>" + balanceAmt);
      balance = String.valueOf(balanceAmt);
      Loggers.general().info(LOG, "balance firc=====>" + balance);

}

fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);
Loggers.general().info(LOG, "bal with currency" + fdwarapper1.getBALANCE());
// fdwarapper1.setAMTUTIL(amtutil+ "
// "+balanceValCurrency);
Loggers.general().info(LOG, "utilbal with currency" + fdwarapper1.getAMTUTIL());

}

else {
//// Loggers.general().info(LOG,"entered else since there is
//// no Inward remittance no ");/ System

}
} else {
if (!inwnum.equalsIgnoreCase("")) {
String inwardDetails = "SELECT TRIM(ORDCUS_CST), TO_CHAR(TO_DATE(value_date,'dd-mm-yy'),'yyyy-mm-dd'), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE),PURCODE FROM ETTV_INWARD_REMITTANCE_AMOUNT  WHERE MAS_REF='"
            + inwnum + "' AND TRIM(PURCODE)='P0103'";
System.out.println(
            "query for getting all fields in inward Remittance grid " + inwardDetails);
pst = con.prepareStatement(inwardDetails);
rs1 = pst.executeQuery();
fdwarapper1.setINWARD(inwnum);// added as the user inputed value is encountered with
                                                // space

if (rs1.next()) {
      fdwarapper1.setNAMREM(rs1.getString(1));
      fdwarapper1.setDATREM(rs1.getString(2));
      fdwarapper1.setCOUNREM(rs1.getString(3));
      creditAmount = rs1.getLong(4);
      creditcur = rs1.getString(5);
      cif_no = rs1.getString(6);
      System.out.println("name and date " + rs1.getString(1) + " " + rs1.getString(2)
                  + " " + rs1.getString(3) + " " + rs1.getString(7));
      fdwarapper1.setCUSCIFNO(cif_no);
      bank_ADCODE = rs1.getString(7);
      fdwarapper1.setADVRECB(bank_ADCODE);

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "Credit AMount " + creditAmount);
      }
} else {
      //// Loggers.general().info(LOG,"entered else since
      //// result set value returned nothing");
}

// String BalAmtQuery = "SELECT
// ext.ccy_1,NVL(SUM(ext.amtutil),0) AS TOTAL_AMT"
// + " FROM exteventadv ext, BASEEVENT bev, MASTER
// mas WHERE ext.FK_EVENT = bev.EXTFIELD "
// + " AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS
// IN ('i','c') AND ext.inward ='"
// + inwnum + "' AND mas.MASTER_REF !='" +
// masReference + "' GROUP BY ext.ccy_1 ";
utilize = fdwarapper1.getAMTUTIL().trim();
System.out.println("utilizeamt amount " + utilize);
utilizeCcy = fdwarapper1.getAMTUTILCurrency();
// System.out.println("utilizeamt amount " +utilizeCcy);
int spacePosition1 = utilize.indexOf(" ");
utilize = utilize.trim().substring(0, spacePosition1);
utilize = utilize.replaceAll("[^0-9]", "");
System.out.println("utilizeamt amount converted " + utilize);
utilizeamt = new BigDecimal(utilize);
utilizeamt = utilizeamt.divide(hundred);
availamt = availamt.add(utilizeamt);
String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
            + eventCode + "') AND ext.INWARD ='" + inwnum
            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
            + eventCode + "') AND ext.INWARD ='" + inwnum
            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
            + eventCode + "') AND ext.INWARD ='" + inwnum
            + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";

if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG,
                  "Query for getting Inward Utilized Amount===>" + BalAmtQuery);
}
pst = con.prepareStatement(BalAmtQuery);
rs1 = pst.executeQuery();
if (rs1.next()) {

      totalAmt = rs1.getDouble(1);

      balanceValCurrency = creditcur;

      double irmAmt = 0;
      String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                  + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                  + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            System.out.println("IRM Clourse Query ---" + closureQuery);
      }
      pst = con.prepareStatement(closureQuery);
      rs1 = pst.executeQuery();
      if (rs1.next()) {
            irmAmt = rs1.getDouble("IRMAMT");
      } else {
            irmAmt = 0;
      }

      totalAmt = totalAmt + irmAmt;

      balanceAmt = (long) (creditAmount - totalAmt);

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            System.out.println("Balance Credit Amount-->" + balanceAmt);
      }
      // fdwarapper1.setCUSCIFNO(cif_no);
      if (balanceAmt > 0) {

            balance = String.valueOf(balanceAmt);
            fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);

      } else {
            fdwarapper1.setBALANCE(0 + " " + balanceValCurrency);
      }
} else {

      double irmAmt = 0;
      String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                  + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                  + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "IRM Clourse Query --->" + closureQuery);
      }
      pst = con.prepareStatement(closureQuery);
      rs1 = pst.executeQuery();
      if (rs1.next()) {
            irmAmt = rs1.getDouble("IRMAMT");
      } else {
            irmAmt = 0;
      }

      long balan_cret = (long) (creditAmount - irmAmt);

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "Balance Credit Amount-->" + balan_cret);
      }

      if (balan_cret > 0) {
            String balan_Str = String.valueOf(balan_cret);
            fdwarapper1.setBALANCE(balan_Str + " " + creditcur);

      } else {
            fdwarapper1.setBALANCE(0 + " " + creditcur);
      }

      // fdwarapper1.setCUSCIFNO(cif_no);

}
} else if (!fircNumber.equalsIgnoreCase("")) {
String fircDetails = "SELECT ORDER_CUSTOMER AS REMITTER_NAME,trim(TO_CHAR(FIRC_DATE,'DD/MM/YY')) as remittance_date,"
            + "trim(REM_COUNTRY) AS REMITTER_COUNTRY ,CIF_NO AS CIF_NUMBER,AVAILABLE_AMOUNT AS AVAILABLE_AMOUNT,PAID_AMOUNT AS UTILIZATION_AMOUNT,currency"
            + " FROM ETT_FIRC_LODGEMENT where FIRC_SERIAL_NO='" + fircNumber + "'";

Loggers.general().info(LOG,
            "query for getting all fields in firc details " + fircDetails);
pst = con.prepareStatement(fircDetails);
rs1 = pst.executeQuery();
if (rs1.next()) {
      fdwarapper1.setNAMREM(rs1.getString(1));
      fdwarapper1.setDATREM(rs1.getString(2));
      Loggers.general().info(LOG, "Date of firc" + fdwarapper1.getDATREM());
      tempcountry = rs1.getString(3);
      // fdwarapper1.setCOUNREM(rs1.getString(3));
      // creditAmount = rs1.getLong(4);
      // creditcur = rs1.getString(5);
      Loggers.general().info(LOG, "tempcountry" + tempcountry);
      cif_no = rs1.getString(4);
      fdwarapper1.setCUSCIFNO(cif_no);
      balanceAmt = rs1.getLong(5);
      Loggers.general().info(LOG, "balance amount firc" + balanceAmt);
      balance = String.valueOf(balanceAmt);
      // fdwarapper1.setBALANCE(balance);
      Loggers.general().info(LOG, "balance firc" + balance);
      utilamt = rs1.getLong(6);
      Loggers.general().info(LOG, "util amount firc" + utilamt);
      amtutil = String.valueOf(utilamt);
      // fdwarapper1.setAMTUTIL(amtutil);
      Loggers.general().info(LOG, "util " + amtutil);
      balanceValCurrency = rs1.getString(7);

} else {
      //// Loggers.general().info(LOG,"entered else since
      //// result set value returned nothing");
}

String firccurrency = "select trim(c7cna) as country from c7pf where trim(C7CNM)='"
            + tempcountry + "'";
Loggers.general().info(LOG,
            "query for getting all fields in firc currency" + firccurrency);
pst = con.prepareStatement(firccurrency);
rs1 = pst.executeQuery();
if (rs1.next()) {

      fdwarapper1.setCOUNREM(rs1.getString(1));
}
Loggers.general().info(LOG, "Currency firc" + fdwarapper1.getCOUNREM());

String tempavailamt = "SELECT E.Available_Amount*power(10,c.c8ced) as available_amount,e.currency"
            + " FROM ETT_FIRC_LODGEMENT E,c8pf c WHERE e.CURRENCY =c.c8ccy "
            + " AND e.FIRC_SERIAL_NO='" + fircNumber + "'";
Loggers.general().info(LOG,
            "query for getting all fields in firc available amount" + tempavailamt);
pst = con.prepareStatement(tempavailamt);
rs1 = pst.executeQuery();
if (rs1.next()) {
      balanceAmt = rs1.getLong(1);
      Loggers.general().info(LOG, "balance amount firc====>" + balanceAmt);
      balance = String.valueOf(balanceAmt);
      Loggers.general().info(LOG, "balance firc=====>" + balance);

}

fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);
Loggers.general().info(LOG, "bal with currency" + fdwarapper1.getBALANCE());
// fdwarapper1.setAMTUTIL(amtutil+ "
// "+balanceValCurrency);
Loggers.general().info(LOG, "utilbal with currency" + fdwarapper1.getAMTUTIL());

}

else {
//// Loggers.general().info(LOG,"entered else since there is
//// no Inward remittance no ");/ System

}
}
}
}
if (utilizeCcy != null && !utilizeCcy.trim().equals("") && !utilizeCcy.trim().isEmpty()) {
availamt = availamt.setScale(2, BigDecimal.ROUND_HALF_EVEN);
setBALAMT(availamt.toString() + " " + utilizeCcy);
} else {
setBALAMT("");
}
//for (int i = 0; i < liste.getSize().intValue(); i++) {
//Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
//while (iterator1.hasNext()) {
//ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
//.next();
//utilize = fdwarapper1.getAMTUTIL();
//utilizeamt=new BigDecimal(utilize);
//String      utilizeCcy = fdwarapper1.getAMTUTILCurrency();
//utilizeamt=utilizeamt.divide(hundred);
//System.out.println("utilizeamt amount " +utilizeamt+" "+utilizeCcy );
//availamt = availamt.add(utilizeamt);
//setBALAMT(availamt.toString() + " " + utilizeCcy);
//}
//}

} catch (Exception e) {
e.printStackTrace();
//// Loggers.general().info(LOG,"Inward remittance excepton" +
//// e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception Inward remittance" + e.getMessage());
}

}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception in Inward remittance ODCPAYLAYOUTButton" + e.getMessage());
}
}

finally {
try {
if (rs1 != null)
rs1.close();
if (pst != null)
pst.close();
if (con != null)
con.close();
} catch (SQLException e) {
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}
}

public void ononfetchEXPBILLclayButton() {
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

String inwnum = "";
double totalAmt = 0;
String balanceValCurrency = ""; // String
String balance = "0.0";
String creditcur = "";
String bank_ADCODE = "";
long creditAmount = 0;
long balanceAmt = 0;
String cif_no = "";
String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String fircNumber = "";
String tempcountry = "";
String amtutil = "0.0";
long utilamt = 0;

try {
con = ConnectionMaster.getConnection();
EnigmaArray<ExtEventAdvanceTableEntityWrapper> liste = getExtEventAdvanceTableData();
for (int i = 0; i < liste.getSize().intValue(); i++) {
Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
while (iterator1.hasNext()) {
ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
.next();
inwnum = fdwarapper1.getINWARD().trim();

fircNumber = fdwarapper1.getFINUMB().trim();
Loggers.general().info(LOG, "Firc number======>" + fircNumber);

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Inward Remittance Number " + inwnum);
Loggers.general().info(LOG, "FIRC Number " + fircNumber);
}

if (!inwnum.equalsIgnoreCase("")) {
String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'yyyy-mm-dd')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
      + inwnum + "'";
Loggers.general().info(LOG,
      "query for getting all fields in inward Remittance grid " + inwardDetails);
pst = con.prepareStatement(inwardDetails);
rs1 = pst.executeQuery();
fdwarapper1.setINWARD(inwnum);// added as the user inputed value is encountered with space

if (rs1.next()) {
fdwarapper1.setNAMREM(rs1.getString(1));
fdwarapper1.setDATREM(rs1.getString(2));
Loggers.general().info(LOG, "Date of remeitt" + fdwarapper1.getDATREM());
fdwarapper1.setCOUNREM(rs1.getString(3));
creditAmount = rs1.getLong(4);
creditcur = rs1.getString(5);
cif_no = rs1.getString(6);

fdwarapper1.setCUSCIFNO(cif_no);
bank_ADCODE = rs1.getString(7);
fdwarapper1.setADVRECB(bank_ADCODE);

if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Credit AMount " + creditAmount);
}
} else {
//// Loggers.general().info(LOG,"entered else since
//// result set value returned nothing");
}

// String BalAmtQuery = "SELECT
// ext.ccy_1,NVL(SUM(ext.amtutil),0) AS TOTAL_AMT"
// + " FROM exteventadv ext, BASEEVENT bev, MASTER
// mas WHERE ext.FK_EVENT = bev.EXTFIELD "
// + " AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS
// IN ('i','c') AND ext.inward ='"
// + inwnum + "' AND mas.MASTER_REF !='" +
// masReference + "' GROUP BY ext.ccy_1 ";

String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
      + eventCode + "') AND ext.INWARD ='" + inwnum
      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
      + eventCode + "') AND ext.INWARD ='" + inwnum
      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
      + eventCode + "') AND ext.INWARD ='" + inwnum
      + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,
            "Query for getting Inward Utilized Amount===>" + BalAmtQuery);
}
pst = con.prepareStatement(BalAmtQuery);
rs1 = pst.executeQuery();
if (rs1.next()) {

totalAmt = rs1.getDouble(1);

balanceValCurrency = creditcur;

double irmAmt = 0;
String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "IRM Clourse Query ---" + closureQuery);
}
pst = con.prepareStatement(closureQuery);
rs1 = pst.executeQuery();
if (rs1.next()) {
      irmAmt = rs1.getDouble("IRMAMT");
} else {
      irmAmt = 0;
}

totalAmt = totalAmt + irmAmt;

balanceAmt = (long) (creditAmount - totalAmt);

if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Balance Credit Amount-->" + balanceAmt);
}
fdwarapper1.setCUSCIFNO(cif_no);
if (balanceAmt > 0) {

      balance = String.valueOf(balanceAmt);
      fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);

} else {
      fdwarapper1.setBALANCE(0 + " " + balanceValCurrency);
}
} else {

double irmAmt = 0;
String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "IRM Clourse Query --->" + closureQuery);
}
pst = con.prepareStatement(closureQuery);
rs1 = pst.executeQuery();
if (rs1.next()) {
      irmAmt = rs1.getDouble("IRMAMT");
} else {
      irmAmt = 0;
}

long balan_cret = (long) (creditAmount - irmAmt);

if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Balance Credit Amount-->" + balan_cret);
}

if (balan_cret > 0) {
      String balan_Str = String.valueOf(balan_cret);
      fdwarapper1.setBALANCE(balan_Str + " " + creditcur);

} else {
      fdwarapper1.setBALANCE(0 + " " + creditcur);
}

fdwarapper1.setCUSCIFNO(cif_no);

}
} else if (!fircNumber.equalsIgnoreCase("")) {
String fircDetails = "SELECT ORDER_CUSTOMER AS REMITTER_NAME,trim(TO_CHAR(FIRC_DATE,'yyyy-mm-dd')) as remittance_date,"
      + "trim(REM_COUNTRY) AS REMITTER_COUNTRY ,CIF_NO AS CIF_NUMBER,AVAILABLE_AMOUNT AS AVAILABLE_AMOUNT,PAID_AMOUNT AS UTILIZATION_AMOUNT,currency"
      + " FROM ETT_FIRC_LODGEMENT where FIRC_SERIAL_NO='" + fircNumber + "'";

Loggers.general().info(LOG, "query for getting all fields in firc details " + fircDetails);
pst = con.prepareStatement(fircDetails);
rs1 = pst.executeQuery();
if (rs1.next()) {
fdwarapper1.setNAMREM(rs1.getString(1));
fdwarapper1.setDATREM(rs1.getString(2));
Loggers.general().info(LOG, "Date of firc" + fdwarapper1.getDATREM());
tempcountry = rs1.getString(3);
// fdwarapper1.setCOUNREM(rs1.getString(3));
// creditAmount = rs1.getLong(4);
// creditcur = rs1.getString(5);
Loggers.general().info(LOG, "tempcountry" + tempcountry);
cif_no = rs1.getString(4);
fdwarapper1.setCUSCIFNO(cif_no);
balanceAmt = rs1.getLong(5);
Loggers.general().info(LOG, "balance amount firc" + balanceAmt);
balance = String.valueOf(balanceAmt);
// fdwarapper1.setBALANCE(balance);
Loggers.general().info(LOG, "balance firc" + balance);
utilamt = rs1.getLong(6);
Loggers.general().info(LOG, "util amount firc" + utilamt);
amtutil = String.valueOf(utilamt);
// fdwarapper1.setAMTUTIL(amtutil);
Loggers.general().info(LOG, "util " + amtutil);
balanceValCurrency = rs1.getString(7);

} else {
//// Loggers.general().info(LOG,"entered else since
//// result set value returned nothing");
}

String firccurrency = "select trim(c7cna) as country from c7pf where trim(C7CNM)='"
      + tempcountry + "'";
Loggers.general().info(LOG, "query for getting all fields in firc currency" + firccurrency);
pst = con.prepareStatement(firccurrency);
rs1 = pst.executeQuery();
if (rs1.next()) {

fdwarapper1.setCOUNREM(rs1.getString(1));
}
Loggers.general().info(LOG, "Currency firc" + fdwarapper1.getCOUNREM());

String tempavailamt = "SELECT E.Available_Amount*power(10,c.c8ced) as available_amount,e.currency"
      + " FROM ETT_FIRC_LODGEMENT E,c8pf c WHERE e.CURRENCY =c.c8ccy "
      + " AND e.FIRC_SERIAL_NO='" + fircNumber + "'";
Loggers.general().info(LOG,
      "query for getting all fields in firc available amount" + tempavailamt);
pst = con.prepareStatement(tempavailamt);
rs1 = pst.executeQuery();
if (rs1.next()) {
balanceAmt = rs1.getLong(1);
Loggers.general().info(LOG, "balance amount firc====>" + balanceAmt);
balance = String.valueOf(balanceAmt);
Loggers.general().info(LOG, "balance firc=====>" + balance);

}

fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);
Loggers.general().info(LOG, "bal with currency" + fdwarapper1.getBALANCE());
// fdwarapper1.setAMTUTIL(amtutil+ "
// "+balanceValCurrency);
Loggers.general().info(LOG, "utilbal with currency" + fdwarapper1.getAMTUTIL());

}

else {
//// Loggers.general().info(LOG,"entered else since there is
//// no Inward remittance no ");/ System

}

}
}

} catch (Exception e) {

//// Loggers.general().info(LOG,"Inward remittance excepton" +
//// e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception Inward remittance" + e.getMessage());
}

}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception in Inward remittance ODCCreatelayOUTButton" + e.getMessage());
}
}

finally {
try {
if (rs1 != null)
rs1.close();
if (pst != null)
pst.close();
if (con != null)
con.close();
} catch (SQLException e) {
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}
}

// public void onGetFircEXPCOLSETTclayButton() {
// // Loggers.general().debug(LOG, "on{}Button:{}", "GetFircOUTCL_EXCOLEC",
// // ValidationTexts.METHOD_NOT_IMPLEMENTED);
// //Loggers.general().info(LOG,"FIRCButton");
// if (FIRCFECTH()) {
// //Loggers.general().info(LOG,"systemOutput");
// } else {
// //Loggers.general().info(LOG,"Else systemOutput");
// }
// }

// public boolean FIRCFECTH() {
// //Loggers.general().info(LOG,"Boolean Method called");
// Connection con = null;
// PreparedStatement dmsp = null;
// ResultSet dmsr = null;
// String masterref = getDriverWrapper().getEventFieldAsText("MST", "r",
// "");
// String eventref = getDriverWrapper().getEventFieldAsText("MEVR", "r",
// "");
// //Loggers.general().info(LOG,"MasterRef = " + masterref + "Eventref = " +
// eventref);
// ArrayList<String> al = new ArrayList<String>();
// boolean value = false;
// //Loggers.general().info(LOG,"Size firc " +
// getExtEventFircDetailsData().getSize());
// String RBIREFNO = "Number";
// EnigmaArray<ExtEventFircDetailsEntityWrapper> liste =
// getExtEventFircDetailsData();
// Iterator<ExtEventFircDetailsEntityWrapper> iterator1 = liste.iterator();
// //Loggers.general().info(LOG,"iterator1.hasNext() - " + iterator1.hasNext());
// while (iterator1.hasNext()) {
// //Loggers.general().info(LOG,"While Started in firc button");
// ExtEventFircDetailsEntityWrapper ExtEventFircDetailsEntityWrapper1 =
// (ExtEventFircDetailsEntityWrapper) iterator1
// .next();
// String firccs = ExtEventFircDetailsEntityWrapper1.getFRNUMB();
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getFIRCCU());
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getFIRCDT());
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getFRNUMB());
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getSequence());
// al.add(firccs);
// }
// int si = liste.getSize();
// int i = 0, sik = 0;
// Collection<ExtEventFircDetails> collect = new
// ArrayList<ExtEventFircDetails>();
// SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yy");
// Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
// Date date = new Date();
// String[] arr = new String[64];
// String[] arra = new String[64];
//
// try {
// //Loggers.general().info(LOG,"TRy 1 ok");
// con = ConnectionMaster.getConnection();
// String dms = "select
// FIRC_ADJUST_FIRC,FIRC_UTIL_DATE,FIRC_BILL_AMOUNT,FIRC_UTILSATION_CURR
// from ETT_FIRC_UTILISATION WHERE trim(FIRC_BILL_REF_NO)='"
// + masterref.trim() + "'";
// dmsp = con.prepareStatement(dms);
// //Loggers.general().info(LOG,"MAster in firc button" + masterref.trim());
// //Loggers.general().info(LOG,dms);
// dmsr = dmsp.executeQuery();
// //Loggers.general().info(LOG,"ChecKing.....");
// while (dmsr.next()) {
// //Loggers.general().info(LOG,"while 2 for firc button");
// //Loggers.general().info(LOG,"Entering looping.....");
// String fircno = dmsr.getString(1).trim();
// //Loggers.general().info(LOG,"validate date inside");
// String fircdate = dmsr.getString(2).trim().replace("-", "/");
// //Loggers.general().info(LOG,"validate date outside" + fircdate);
// String fircamt = dmsr.getString(3).trim();
// String fircccy = dmsr.getString(4).trim();
// //Loggers.general().info(LOG,"Printing all the above strings fircno fircamt
// fircccy");
// //Loggers.general().info(LOG,fircno + "" + fircamt + "" + fircccy);
// String output = fircno + "][" + fircdate + "][" + fircamt + "][" +
// fircccy;
// arr[i] = output;
// i++;
// }
//
// //Loggers.general().info(LOG,"Size of table:" + i);
// } catch (Exception e) {
// //Loggers.general().info(LOG,"error is catched" + e.getMessage());
// }
// // compare of two thing
// try {
// //Loggers.general().info(LOG,"entered try2 in firc button");
// for (int j = 0; j < i; j++) {
// StringTokenizer sts = new StringTokenizer(arr[j], "][");
// String output = "";
// String firc = "";
// while (sts.hasMoreTokens()) {
// //Loggers.general().info(LOG,"Entered while 3 in firc button");
// firc = sts.nextToken();
// String fircdate = sts.nextToken();
// String fircamt = sts.nextToken();
// String fircccy = sts.nextToken();
// output = firc + "][" + fircdate + "][" + fircamt + "][" + fircccy;
// }
// //Loggers.general().info(LOG,"FIRC -->" + firc);
// //Loggers.general().info(LOG,"a1" + al + "a1 size" + al.size());
// if (null != al && al.size() > 0) {
// //Loggers.general().info(LOG,"null != al && al.size()>0");
// if (!al.contains(firc)) {
// //Loggers.general().info(LOG,"al.contains(firc)");
// arra[j] = output;
// sik = sik + 1;
// }
// } else {
// //Loggers.general().info(LOG,"else1 in firc button");
// arra[j] = output;
// sik = sik + 1;
// }
//
// } /// end of while
//
// } // end of for loop
// catch (Exception ekl) {
// //Loggers.general().info(LOG,"Sysout" + ekl.getMessage());
// ekl.getStackTrace();
// ekl.printStackTrace();
// }
// try {
// //Loggers.general().info(LOG,"final try in firc button");
// for (int j = 0; j < sik; j++) {
// //Loggers.general().info(LOG,"Checking table data ----------->" + sik);
// StringTokenizer st = new StringTokenizer(arra[j], "][");
// while (st.hasMoreTokens()) {
// //Loggers.general().info(LOG,"while 3 in firc button");
// String firc = st.nextToken();
// String fircdate = st.nextToken();
// String fircamt = st.nextToken();
// String fircccy = st.nextToken();
// ExtEventFircDetails wFIRC2 = new ExtEventFircDetails();
// wFIRC2.setColumn("FRNUMB", firc);
// wFIRC2.setColumn("FIRCDT", fircdate);
// wFIRC2.setColumn("FRCUTA", fircamt);
// wFIRC2.setColumn("FIRCCU", fircccy);
// wFIRC2.setNewKey();
// wFIRC2.setFk(fkey);
// //Loggers.general().info(LOG,"Forign Key" + fkey);
// wFIRC2.setSequence(si);
// si = si + 1;
// //Loggers.general().info(LOG,"Keddy2" + wFIRC2.toString());
// collect.add(wFIRC2);
// }
// //Loggers.general().info(LOG,"printing array" + arr[j]);
//
// }
// } catch (Exception ece) {
// //Loggers.general().info(LOG,"Exeception in button" + ece.getMessage());
// ece.getStackTrace();
// ece.printStackTrace();
// }
//
//
// Iterator<ExtEventFircDetails> iterator = collect.iterator();
// while (iterator.hasNext()) {
// //Loggers.general().info(LOG,"Final while");
// ExtEventFircDetails extEventFircDetails = (ExtEventFircDetails)
// iterator.next();
// ExtEventFircDetailsEntityWrapper wrpp = new
// ExtEventFircDetailsEntityWrapper(extEventFircDetails,
// getDriverWrapper());
// wrpp.getEntity().get$key();
// wrpp.getSequence();
// wrpp.getEntity().getFk();
// wrpp.getFIRCCU();
// wrpp.getFIRCDT();
// wrpp.getFRCUTA();
// wrpp.getFRNUMB();
// // wrpp.getEntity()
// // fire the insert
// //Loggers.general().info(LOG,"collect.size()>0" + collect.size());
// if (collect.size() > 0) {
// //Loggers.general().info(LOG,"collect.size()>0");
// loadExtEventFircDetailsViewPane(ExtensionViewPaneMode.NEW, wrpp);
// }
// //Loggers.general().info(LOG,"After Load");
// //Loggers.general().info(LOG,"One Report wrpp");
//
// }
// // value = true;
//
// /*
// * // available amt - FIRC value = total firs utilization amount try {
// * //Loggers.general().info(LOG,"enter into try to fetch firc"); Double
// * totIfircamt = 0.00; String firccurrency = null; Double available
// * =Double.valueOf(getDriverWrapper().getEventFieldAsText("ORA", "v",
// * "m")); //Loggers.general().info(LOG,"available" + available);
// * EnigmaArray<ExtEventFircDetailsEntityWrapper> lisgte =
// * getExtEventFircDetailsData();
// * Iterator<ExtEventFircDetailsEntityWrapper> itegrator1 =
// * lisgte.iterator(); //Loggers.general().info(LOG,"itegrator1 " +
// itegrator1);
// * while (iterator1.hasNext()) { //Loggers.general().info(LOG,
// * "enter into while to fetch firc"); ExtEventFircDetailsEntityWrapper
// * ExtEventFircDetailsEntityWrapper1 =
// * (ExtEventFircDetailsEntityWrapper) itegrator1.next();
// *
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getFIRCCU());
// *
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getFIRCDT());
// *
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getFRNUMB());
// *
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getFRCUTA());
// *
// //Loggers.general().info(LOG,ExtEventFircDetailsEntityWrapper1.getSequence());
// * totIfircamt = totIfircamt +
// * Double.valueOf(ExtEventFircDetailsEntityWrapper1.getFRCUTA());
// * firccurrency =
// * String.valueOf(ExtEventFircDetailsEntityWrapper1.getFIRCCU());
// * //Loggers.general().info(LOG,"firccurrency " + firccurrency); }
// if(totIfircamt
// * != 0 && firccurrency != null) { //Loggers.general().info(LOG,totIfircamt +
// * "totIfircamt"); Double present = available - totIfircamt;
// * //Loggers.general().info(LOG,"totIfircamt " + totIfircamt);
// * setTOLUL(String.valueOf(totIfircamt)); setFIRCU(firccurrency); }
// *
// * value = true; }
// *
// * catch (Exception e) { // TODO Auto-generated catch block
// * e.printStackTrace(); value = false; }
// */
// return value;
// }

public void onPRESHIPFINEXPBILLclayButton() {

String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");

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

con1 = ConnectionMaster.getConnection();
String dms = "SELECT exte.MARAMT, exte.CCY_1 FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT and exte.MARAMT is not null and trim(exte.CCY_1) is not null AND mas.MASTER_REF = '"
+ MasterReference + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL =" + evvcount + "";

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Finance Collection margin query---->" + dms);
}
ps1 = con1.prepareStatement(dms);
rs = ps1.executeQuery();
if (rs.next()) {
String margin = rs.getString(1);
String ccy = rs.getString(2);
if (margin != null && margin.length() > 0 && ccy.length() > 1) {
setMARAMT(margin + "" + ccy);
} else {
setMARAMT("");
}

}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception Margin finance amount" + e.getMessage());
}
} finally {
try {
if (rs != null)
rs.close();
if (ps1 != null)
ps1.close();
if (con1 != null)
con1.close();
} catch (SQLException e) {
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}

try {
getExtEventLoanDetailsNew().setEnabled(false);
getExtEventLoanDetailsDelete().setEnabled(false);
getExtEventLoanDetailsUpdate().setEnabled(false);
getExtEventLoanDetailsUp().setEnabled(false);
getExtEventLoanDetailsDown().setEnabled(false);
EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
int count = 0;
// Iterator<ExtEventLoanDetailsEntityWrapper> iterator =
// liste.iterator();

if (liste.getSize() < 1) {
String query = "SELECT trim(exte.DEALREF),exte.REAMOUNT,exte.CCY,TO_CHAR(exte.VALDATE,'yyyy-mm-dd') FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENTLRT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.EXTFIELD = exte.FK_EVENT AND mas.MASTER_REF = '"
+ MasterReference + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL =" + evvcount
+ "";
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "DPR Loan Details" + query);
}

psd = con1.prepareStatement(query);
rst = psd.executeQuery();
//// Loggers.general().info(LOG,"executeQuery statement ");
while (rst.next()) {
//// Loggers.general().info(LOG,"Enter into while");
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
loanvalues.setColumn("DEALREF", rst.getString(1));
loanvalues.setColumn("REAMOUNT", rst.getString(2) + "" + rst.getString(3));
// loanvalues.setColumn("CCY", rst.getString(3));
loanvalues.setColumn("VALDATE", rst.getString(4));
loanvalues.setNewKey();
loanvalues.setFk(fkey);
loanvalues.setSequence(count);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Loan details" + rst.getString(1));
Loggers.general().info(LOG, "Repayment amount" + rst.getString(2));
Loggers.general().info(LOG, "Value date" + rst.getString(3));
}
getExtEventLoanDetailsNew().setEnabled(false);
getExtEventLoanDetailsDelete().setEnabled(false);
getExtEventLoanDetailsUpdate().setEnabled(false);
getExtEventLoanDetailsUp().setEnabled(false);
getExtEventLoanDetailsDown().setEnabled(false);
getBtnFetchPreShipFINEXPLCCREclay().setEnabled(false);

ExtEventLoanDetailsEntityWrapper projectdetchk = new ExtEventLoanDetailsEntityWrapper(loanvalues,
getDriverWrapper());
addNewExtEventLoanDetails(projectdetchk);

count++;
}
}
} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception LoanDetails" + e.getMessage());
}

} finally {
try {
if (rst != null)
rst.close();
if (psd != null)
psd.close();
if (con1 != null)
con1.close();
} catch (SQLException e) {
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}

}

public void ondisplayvalEXPCOLSETTclayButton() {
// Loggers.general().info(LOG,"IFSCFECTH");
if (IFSCFECTH()) {
// Loggers.general().info(LOG," IFSCFECTH BUTTON");
} else {
// Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
}
}

public void onBENIFSCINWDOCCOLPAYclayButton() {

// Loggers.general().info(LOG,"IFSCFECTH");
if (IFSCFECTH()) {
// Loggers.general().info(LOG," IFSCFECTH BUTTON");
} else {
// Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
}

}

//public void onRTGSEXPCOLFECclayButton() {
//
//// Loggers.general().info(LOG,"IFSCFECTH");
//if (IFSCFECTH()) {
//// Loggers.general().info(LOG," IFSCFECTH BUTTON");
//} else {
//// Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
//}
//
//}

//public void ondisplayvalEXPBILLclayButton() {
//
//// Loggers.general().info(LOG,"IFSCFECTH");
//if (IFSCFECTH()) {
//// Loggers.general().info(LOG," IFSCFECTH BUTTON");
//} else {
//// Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
//}
//
//}

//public void onRTGSEXPCOLSETTclayButton() {
//
//// Loggers.general().info(LOG,"IFSCFECTH");
//if (IFSCFECTH()) {
//// Loggers.general().info(LOG," IFSCFECTH BUTTON");
//} else {
//// Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
//}
//
//}

public boolean IFSCFECTH() {
boolean value = false;
// Loggers.general().info(LOG,"Exp lc button for POD");
String benname = "";
String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String amountcoll = getDriverWrapper().getEventFieldAsText("LPAY", "v", "m");
String Advcur = getDriverWrapper().getEventFieldAsText("LPAY", "v", "c");
con = ConnectionMaster.getConnection();
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
//setBENNAME_Name(benname);
try {
String query = "SELECT pos.BO_ACC_NO, POS.ORIGAMOUNT/100,POS.ORIGAMTCCY "
+ "FROM  MASTER MAS,BASEEVENT BEV,RELITEM REL ,POSTING POS "
+ "WHERE MAS.KEY97=BEV.MASTER_KEY   AND BEV.KEY97=REL.EVENT_KEY " + " AND REL.KEY97=POS.KEY97"
+ " AND MAS.MASTER_REF='" + masRefNo + "' " + " AND (BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))='"
+ eventRefNo + "' " + " and (BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))=pos.eventref "
+ " AND POS.DR_CR_FLG='C' " + " AND POS.ACC_TYPE='OA' " + " AND POS.SP_CODE='SP734' ";
ps = con.prepareStatement(query);
rs = ps.executeQuery();
if (rs.next()) {
String dracct = rs.getString(1).trim();
//String amount=rs.getString(2).trim();
//String ccy=rs.getString(3).trim();
System.out.println("RTGS NEFT QUERY " + query + " " + amountcoll);
setDRINTACC(dracct);

}
setRTGSNEFT(amountcoll + " " + Advcur);
String rtgsType = getPROREMT();
if (rtgsType.trim().equalsIgnoreCase("RTG")) {

setCRPOOLAC("504505120004000");

}

if (rtgsType.trim().equalsIgnoreCase("NEF")) {

// String debtacc="1980050000";
setCRPOOLAC("473802480015000");

}
setSENDBNCD("026");
String proceed = getRTGNFT();
if (proceed.equalsIgnoreCase("B2B")) {
benname = getDriverWrapper().getEventFieldAsText("CDT", "p", "f");
setBENNAME_Name(benname);
}
if (proceed.equalsIgnoreCase("B2C")) {
benname = getDriverWrapper().getEventFieldAsText("DRW", "p", "f");
setBENNAME_Name(benname);
}
//setBENNAME_Name(benname);

} catch (Exception e) {
e.printStackTrace();
// Loggers.general().info(LOG,"event catch");
}

finally {
try {
if (con != null) {
con.close();
if (rs != null)
rs.close();
if (dmsr1 != null)
dmsr1.close();
if (ps != null)
ps.close();
}
} catch (SQLException e) {
// Loggers.general().info(LOG,"Connection Failed! Check output
// console");
e.printStackTrace();
}
}

return value;
}

public boolean FETCHIFSC() {
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
try {

String behalfHalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
String amountCurrency = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
String subProductType = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
String query = "";
String senderIfsc = "";
if ((subProductType.equalsIgnoreCase("OCI") || subProductType.equalsIgnoreCase("ICI"))
&& amountCurrency.equalsIgnoreCase("INR")) {
query = "select trim(IFSC) from extbramas where BCODE='" + behalfHalfBranch + "' ";
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
senderIfsc = rs1.getString(1);
}
// Loggers.general().info(LOG,"Sender IFSC code----->" + senderIfsc);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Sender IFSC code----->" + senderIfsc);

}
setSENIFSC(senderIfsc);
}
} catch (Exception e) {
// Loggers.general().info(LOG,"Exeception of recifsc " + e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exeception of recifsc " + e.getMessage());

}

} finally {

try {
if (con != null) {
con.close();
if (ps1 != null)
ps1.close();
if (rs1 != null)
rs1.close();
}
} catch (SQLException e) {
// Loggers.general().info(LOG,"Connection Failed! Check output
// console");
e.printStackTrace();
}

}
return value;
}

// public boolean FetchIFSCIncoming() {
// boolean value = false;
// String masterRefNumber = getDriverWrapper().getEventFieldAsText("THE",
// "r", "").trim();
// Connection con = null;
// PreparedStatement ps1 = null;
// ResultSet rs1 = null;
// try {
// // Code changed by Sreedhar
// String query = "select TRIM(SENDERIFSC),TRIM(RECEIVERIFSC) from
// EXTSFMSCUSTMAP where MASTERREFERENCE ='"
// + masterRefNumber + "' and SENDERIFSC is not null and RECEIVERIFSC is not
// null";
//
// // Loggers.general().info(LOG,"Sender ifsc code=====>" + query);
// con = ConnectionMaster.getConnection();
// ps1 = con.prepareStatement(query);
// rs1 = ps1.executeQuery();
//
// while (rs1.next()) {
// String senifsc = rs1.getString(1);
// // Loggers.general().info(LOG,"Sender ifsc code=====>" + senifsc);
// String recifsc = rs1.getString(2);
// // Loggers.general().info(LOG,"Sender ifsc code=====>" + recifsc);
// setSNIFSCIN(senifsc);
// //setRCIFSCIN(recifsc);
// }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Exeception of incoming recifsc " +
// // e.getMessage());
//
// } finally {
//
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
//
// }
//
// return value;
// }
// -----------***--------------

protected void loadExtEventFircDetailsViewPane(ExtensionViewPaneMode mode, ExtEventFircDetailsEntityWrapper item) {
EnigmaArray<ExtEventFircDetailsEntityWrapper> listes = getExtEventFircDetailsData();

Loggers.general().info(LOG, "loasdksiskdikdi");

PaneManager.preExecute();
try {
ExtensionViewWebPane pane = createExtEventFircDetailsViewPane(getLayoutName());
// pane.getco
pane.initialise(this, "ExtEventFircDetails", mode, item, getDriverWrapper());
PaneManager.execute(pane);
// Loggers.general().info(LOG,"PaneManager");
} catch (EnigmaException ex) {
PaneManager.undoPreExecute();
throw ex;
} catch (Exception ex) {
PaneManager.undoPreExecute();
throw new EnigmaException(new EnigmaExceptionCode("CUST", 100), "Cannot load view pane", ex);
}

}

public void onFetchShipDetEXPBILLclayButton() {
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

EnigmaArray<ExtEventShippingCollectionsEntityWrapper> liste = getExtEventShippingCollectionsData();
EnigmaArray<ExtEventShippingdetailsEntityWrapper> listef = getExtEventShippingdetailsData();

String iecode = getDriverWrapper().getEventFieldAsText("DRW", "p", "cBBF").trim();
System.out.println("IE code is from customer" + iecode);
Iterator<ExtEventShippingCollectionsEntityWrapper> iterator = liste.iterator();
Iterator<ExtEventShippingdetailsEntityWrapper> iterator1 = listef.iterator();

for (ExtEventShippingdetailsEntityWrapper extship : listef) {
System.out.println("Entered into for all");
removeExtEventShippingdetails(extship);
}
int j = 0;
int count = 0;
String query = "";
System.out.println("ExtEventShippingCollections size==>" + liste.getSize().intValue());

Connection connection = null;
ResultSet rst = null;
PreparedStatement ps = null;

for (int i = 0; i < liste.getSize().intValue(); i++) {

if (connection == null) {
connection = ConnectionMaster.getConnection();
}

while (iterator.hasNext()) {

ExtEventShippingCollectionsEntityWrapper sdwrapper = (ExtEventShippingCollectionsEntityWrapper) iterator
.next();
ExtEventShippingdetailsEntityWrapper sdwrapper1 = (ExtEventShippingdetailsEntityWrapper) iterator1
.next();

String billnum = "";
String formNO = "";
String portcode = "";
String shipamt = "";

String add_lie = "";

try {
billnum = sdwrapper.getCBILLNUM().trim();

System.out.println("billnum num and other details" + billnum);
} catch (Exception e) {
System.out.println("Exception billnum num and other details" + e.getMessage());

}

try {
formNO = sdwrapper.getCFORMN().trim();
System.out.println("Form num and other details" + formNO);
} catch (Exception e) {
System.out.println("Exception Form num and other details" + e.getMessage());

}

portcode = sdwrapper.getCPORTCO();
shipamt = sdwrapper.getCSHPAMT();
if (!shipamt.equalsIgnoreCase("")) {
add_lie = shipamt.replaceAll("[^0-9]", "");
}
String shidate = sdwrapper.getCBILLDA();
System.out.println("bill num and other details" + billnum + " " + portcode + " " + shipamt);
System.out.println("Shipping bill num" + billnum);
System.out.println("Shipping form num" + formNO);

sdwrapper.setCIECOD(iecode);

if (add_lie.length() > 0) {
String shipcur = sdwrapper.getCSHPAMTCurrency();
BigDecimal priceDecimal = new BigDecimal(add_lie);
System.out.println("Bigdecimal value for priceDecimal" + priceDecimal);

String ship_final = String.valueOf(priceDecimal);

sdwrapper.setCEQUBILL(ship_final + "" + shipcur);

sdwrapper.setCOUTSAMT(ship_final + "" + shipcur);
sdwrapper.setCSHCOLAM(0 + "" + shipcur);
} else {
Loggers.general().info(LOG, "Shipping bill amount is empty");
}
j = j + 1;
int count2 = 0;
if (billnum.length() > 0 && formNO.length() > 0) {
Loggers.general().info(LOG, "entered  if 16/1/19");
System.out.println("billnum and formNO entered");

String query2 = "SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
+ " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDBILLNO) ='"
+ billnum + "'" + "   and trim(elm.sdformno)='" + formNO
+ "' AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('" + shidate
+ "','DD-MM-YY')) AND trim(elm.sdprtcde) ='" + portcode + "'";

System.out.println("Query2 formNO EXTEVENTSPD---------->" + query2);

try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;

} catch (Exception e) {
System.out.println("Exception Query2 formNO EXTEVENTSPD---------->" + e.getMessage());
}
ps = connection.prepareStatement(query2);
rst = ps.executeQuery();
while (rst.next()) {
count2 = rst.getInt(1);
System.out.println("Count value for shipping formNO---------->" + count2);
}

// ELC form no checking
int count_elc = 0;

String query2_elc = "SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
+ " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcBILLNO) ='"
+ billnum + "'" + " and trim(elm.LCFORMNO)='" + formNO
+ "' AND TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('" + shidate
+ "','DD-MM-YY')) AND trim(elm.lcprtcde) ='" + portcode + "'";

System.out.println("Query2 form no EXTEVENTSLC-------->" + query2_elc);

try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;

} catch (Exception e) {
System.out.println("Exception Query2 form no EXTEVENTSLC-------->" + e.getMessage());
}
ps = connection.prepareStatement(query2_elc);
rst = ps.executeQuery();
while (rst.next()) {
count_elc = rst.getInt(1);
System.out.println("count_elc Count value for shipping form no---------->" + count_elc);

}

if (count_elc < 1 && count2 < 1) {
Loggers.general().info(LOG, "start of export 1---");
String queryexport1 = "select DISTINCT shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS SDATE,portcode,TO_CHAR(leodate,'yyyy-mm-dd') AS LDATE,custno,exportagency,exporttype,countrydest,iecode,adcode,formno,RECIND from ett_edpms_shp where shipbillno='"
      + billnum + "' AND IECODE='" + iecode + "' AND SHIPBILLDATE = to_char(to_date('"
      + shidate + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "' AND FORMNO='"
      + formNO + "'";

System.out.println("Shipping bill queryexport1-------->" + queryexport1);

try {
if (ps != null)
      ps.close();
if (rst != null)
      rst.close();
rst = null;

} catch (Exception e) {
System.out.println(" Exception Shipping bill queryexport1-------->" + e.getMessage());
}
ps = connection.prepareStatement(queryexport1);
rst = ps.executeQuery();
while (rst.next()) {
System.out.println("Entered rs");
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventShippingdetails shippingdetails = new ExtEventShippingdetails();

shippingdetails.setColumn("SDBILLNO", rst.getString(1));

shippingdetails.setColumn("SDBILDAT", rst.getString(2));
Loggers.general().info(LOG, "billdate---" + shippingdetails.getColumn("SDBILDAT"));
Loggers.general().info(LOG, "date" + rst.getString(2));
shippingdetails.setColumn("SDPRTCDE", rst.getString(3));
// Loggers.general().info(LOG,"PORTCODE" +
// rst.getString(3));
shippingdetails.setColumn("SDLEODAT", rst.getString(4));
Loggers.general().info(LOG, "leodate---" + shippingdetails.getColumn("SDLEODAT"));
Loggers.general().info(LOG, "date" + rst.getString(4));
shippingdetails.setColumn("SDCUSTNO", rst.getString(5));
shippingdetails.setColumn("SDEXAGNC", rst.getString(6));
shippingdetails.setColumn("SDEXPTYP", rst.getString(7));
shippingdetails.setColumn("SDDESCON", rst.getString(8));
shippingdetails.setColumn("SDIECOD", rst.getString(9));
shippingdetails.setColumn("SDADCOD", rst.getString(10));
shippingdetails.setColumn("SDFORMNO", rst.getString(11));
shippingdetails.setColumn("CREIND", rst.getString(12));
shippingdetails.setSERIALN(String.valueOf(j));
getBtnFetchInvDetEXPBILLclay().setEnabled(true);

System.out.println("After button in while");
shippingdetails.setNewKey();
shippingdetails.setFk(fkey);
shippingdetails.setSequence(count);

ExtEventShippingdetailsEntityWrapper shippingdwrapper = new ExtEventShippingdetailsEntityWrapper(
            shippingdetails, getDriverWrapper());
addNewExtEventShippingdetails(shippingdwrapper);

count++;
getBtnFetchShipDetEXPBILLclay().setEnabled(false);

}
Loggers.general().info(LOG, "start of export 2-----");
String queryexport2 = "SELECT DISTINCT fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS sofdate, portcode, TO_CHAR(leodate,'yyyy-mm-dd') AS LEODATE, custno, exportagency, exporttype, countrydest, iecode, adcode, formno,RECIND FROM ett_edpms_shp_softex WHERE formno    ='"
      + formNO + "' AND IECODE='" + iecode + "' AND SHIPBILLDATE = to_char(to_date('"
      + shidate + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode
      + "' AND SHIPBILLNO='" + billnum + "'";

System.out.println("From number result-------->" + queryexport2);

try {
if (ps != null)
      ps.close();
if (rst != null)
      rst.close();
rst = null;

} catch (Exception e) {
System.out.println("Exception From number result-------->" + e.getMessage());
}
ps = connection.prepareStatement(queryexport2);
rst = ps.executeQuery();
while (rst.next()) {
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventShippingdetails shippingdetails = new ExtEventShippingdetails();

shippingdetails.setColumn("SDBILLNO", rst.getString(1));

shippingdetails.setColumn("SDBILDAT", rst.getString(2));
Loggers.general().info(LOG,
            "billdate---------" + shippingdetails.getColumn("SDBILDAT"));
Loggers.general().info(LOG, "date------" + rst.getString(2));
shippingdetails.setColumn("SDPRTCDE", rst.getString(3));

shippingdetails.setColumn("SDLEODAT", rst.getString(4));
Loggers.general().info(LOG, "LEOdate---" + shippingdetails.getColumn("SDLEODAT"));
Loggers.general().info(LOG, "date====" + rst.getString(4));
shippingdetails.setColumn("SDCUSTNO", rst.getString(5));
shippingdetails.setColumn("SDEXAGNC", rst.getString(6));
shippingdetails.setColumn("SDEXPTYP", rst.getString(7));
shippingdetails.setColumn("SDDESCON", rst.getString(8));
shippingdetails.setColumn("SDIECOD", rst.getString(9));
shippingdetails.setColumn("SDADCOD", rst.getString(10));
shippingdetails.setColumn("SDFORMNO", rst.getString(11));
shippingdetails.setColumn("CREIND", rst.getString(12));
shippingdetails.setSERIALN(String.valueOf(j));
getBtnFetchInvDetEXPBILLclay().setEnabled(true);

shippingdetails.setNewKey();
shippingdetails.setFk(fkey);
shippingdetails.setSequence(count);

ExtEventShippingdetailsEntityWrapper shippingdwrapper = new ExtEventShippingdetailsEntityWrapper(
            shippingdetails, getDriverWrapper());
addNewExtEventShippingdetails(shippingdwrapper);

count++;

}

}

else {
Loggers.general().info(LOG,
      "Count value for shipping form NO in else------->" + count2 + "" + count_elc);

}
if (listef.getSize().intValue() == 0) {
getBtnFetchInvDetEXPBILLclay().setEnabled(false);
} else if (listef.getSize().intValue() > 0) {

getBtnFetchInvDetEXPBILLclay().setEnabled(true);
}
} else if (billnum.length() > 0 && formNO.length() <= 0) {
System.out.println("billnum entered---------->" + billnum);

String query2 = "SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
+ " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDBILLNO) ='"
+ billnum + "'" + " AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('" + shidate
+ "','DD-MM-YY')) AND trim(elm.sdprtcde) ='" + portcode + "'";

System.out.println("Query2 billnum EXTEVENTSPD---------->" + query2);

try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;

} catch (Exception e) {
System.out.println("Exception Query2 billnum EXTEVENTSPD---------->" + e.getMessage());
}
ps = connection.prepareStatement(query2);
rst = ps.executeQuery();
while (rst.next()) {
count2 = rst.getInt(1);
System.out.println("Query2 billnum count2---------->" + count2);
}

// ELC bill no checking
int count_elc = 0;

String query_elc = "SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
+ " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcBILLNO) ='"
+ billnum + "'" + " AND TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('" + shidate
+ "','DD-MM-YY')) AND trim(elm.lcprtcde) ='" + portcode + "'";

System.out.println("Query_elc EXTEVENTSLC-------->" + query_elc);

try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;

} catch (Exception e) {
System.out.println("Exception Query_elc EXTEVENTSLC-------->" + e.getMessage());
}

ps = connection.prepareStatement(query_elc);
rst = ps.executeQuery();
while (rst.next()) {
count_elc = rst.getInt(1);
System.out.println("Query_elc checking Count bill no---------->" + count_elc);
}

if (count_elc < 1 && count2 < 1) {
try {
query = "select DISTINCT shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS SDATE,portcode,TO_CHAR(leodate,'yyyy-mm-dd') AS LDATE,custno,exportagency,exporttype,countrydest,iecode,adcode,formno,RECIND from ett_edpms_shp where shipbillno='"
            + billnum + "' AND IECODE='" + iecode + "' AND SHIPBILLDATE = to_char(to_date('"
            + shidate + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "'";
System.out.println("inside shipment query" + query);
System.out.println("Shipping bill result-------->" + query);

try {
      if (ps != null)
            ps.close();
      if (rst != null)
            rst.close();
      rst = null;

} catch (Exception e) {
      System.out.println("Exception Shipping bill result-------->" + e.getMessage());
}
ps = connection.prepareStatement(query);
rst = ps.executeQuery();
while (rst.next()) {
      Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
      ExtEventShippingdetails shippingdetails = new ExtEventShippingdetails();
      if (billnum.length() > 0) {
            shippingdetails.setColumn("SDBILLNO", rst.getString(1));
      } else {
            shippingdetails.setColumn("SDBILLNO", "");
      }
      System.out.println("inside shipment fetch button" + query);
      shippingdetails.setColumn("SDBILDAT", rst.getString(2));
      Loggers.general().info(LOG,
                  "billdate---------" + shippingdetails.getColumn("SDBILDAT"));
      Loggers.general().info(LOG, "date" + rst.getString(2));
      Loggers.general().info(LOG, "Date==>" + rst.getString(2));
      Loggers.general().info(LOG,
                  "getting date==>" + shippingdetails.getColumn("SDBILDAT"));
      shippingdetails.setColumn("SDPRTCDE", rst.getString(3));
      // Loggers.general().info(LOG,"PORTCODE" +
      // rst.getString(3));
      shippingdetails.setColumn("SDLEODAT", rst.getString(4));
      Loggers.general().info(LOG,
                  "leodate---------" + shippingdetails.getColumn("SDLEODAT"));
      Loggers.general().info(LOG, "date" + rst.getString(4));
      shippingdetails.setColumn("SDCUSTNO", rst.getString(5));
      shippingdetails.setColumn("SDEXAGNC", rst.getString(6));
      shippingdetails.setColumn("SDEXPTYP", rst.getString(7));
      shippingdetails.setColumn("SDDESCON", rst.getString(8));
      shippingdetails.setColumn("SDIECOD", rst.getString(9));
      shippingdetails.setColumn("SDADCOD", rst.getString(10));
      shippingdetails.setColumn("SDFORMNO", rst.getString(11));
      shippingdetails.setColumn("CREIND", rst.getString(12));
      shippingdetails.setSERIALN(String.valueOf(j));
      getBtnFetchInvDetEXPBILLclay().setEnabled(true);

      shippingdetails.setNewKey();
      shippingdetails.setFk(fkey);
      shippingdetails.setSequence(count);

      ExtEventShippingdetailsEntityWrapper shippingdwrapper = new ExtEventShippingdetailsEntityWrapper(
                  shippingdetails, getDriverWrapper());
      addNewExtEventShippingdetails(shippingdwrapper);

      count++;

}

} catch (Exception e) {
System.out.println("Exception in getting ship bill if loop --" + e);
}
} else {
Loggers.general().info(LOG, "Count value for shipping bill in else------->" + count2
      + "ELC query result" + count_elc);

}

if (listef.getSize().intValue() == 0) {
getBtnFetchInvDetEXPBILLclay().setEnabled(false);
} else if (listef.getSize().intValue() > 0) {

getBtnFetchInvDetEXPBILLclay().setEnabled(true);
}
} else if (formNO.length() > 0 && billnum.length() <= 0) {
System.out.println("entered formNO=====>");

String query2 = "SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
+ " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDFORMNO)  ='"
+ formNO + "'" + " AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('" + shidate
+ "','DD-MM-YY')) AND trim(elm.sdprtcde) ='" + portcode + "'";

System.out.println("Query2 formNO EXTEVENTSPD---------->" + query2);

try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;

} catch (Exception e) {
System.out.println("Query2 formNO EXTEVENTSPD exception" + e.getMessage());
e.getMessage();
}
ps = connection.prepareStatement(query2);
rst = ps.executeQuery();
while (rst.next()) {
count2 = rst.getInt(1);
System.out.println("Count value for shipping formNO---------->" + count2);
}

// ELC form no checking
int count_elc = 0;

String query2_elc = "SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
+ " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcformno) ='"
+ formNO + "'" + " AND TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('" + shidate
+ "','DD-MM-YY')) AND trim(elm.lcprtcde) ='" + portcode + "'";

System.out.println("Query2 form no EXTEVENTSLC-------->" + query2_elc);

try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
} catch (Exception e) {
System.out.println("Exception Query2 form no EXTEVENTSLC-------->" + e.getMessage());
}
ps = connection.prepareStatement(query2_elc);
rst = ps.executeQuery();
while (rst.next()) {
count_elc = rst.getInt(1);

System.out.println("count_elc Count value for shipping form no---------->" + count_elc);

}

if (count_elc < 1 && count2 < 1) {

query = "SELECT DISTINCT fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS sofdate, portcode, TO_CHAR(leodate,'yyyy-mm-dd') AS LEODATE, custno, exportagency, exporttype, countrydest, iecode, adcode, formno,RECIND FROM ett_edpms_shp_softex WHERE formno    ='"
      + formNO + "' AND IECODE='" + iecode + "' AND SHIPBILLDATE = to_char(to_date('"
      + shidate + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "'";
System.out.println("fetch button last query" + query);

try {
if (ps != null)
      ps.close();
if (rst != null)
      rst.close();
rst = null;

} catch (Exception e) {
System.out.println("Exception fetch button last query------>" + e.getMessage());
}
ps = connection.prepareStatement(query);
rst = ps.executeQuery();

while (rst.next()) {
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventShippingdetails shippingdetails = new ExtEventShippingdetails();
if (billnum.length() > 0) {
      shippingdetails.setColumn("SDBILLNO", rst.getString(1));
} else {
      shippingdetails.setColumn("SDBILLNO", "");
}
shippingdetails.setColumn("SDBILDAT", rst.getString(2));
Loggers.general().info(LOG, "billdate---" + shippingdetails.getColumn("SDBILDAT"));
Loggers.general().info(LOG, "date" + rst.getString(2));
shippingdetails.setColumn("SDPRTCDE", rst.getString(3));

shippingdetails.setColumn("SDLEODAT", rst.getString(4));
shippingdetails.setColumn("SDCUSTNO", rst.getString(5));
shippingdetails.setColumn("SDEXAGNC", rst.getString(6));
shippingdetails.setColumn("SDEXPTYP", rst.getString(7));
shippingdetails.setColumn("SDDESCON", rst.getString(8));
shippingdetails.setColumn("SDIECOD", rst.getString(9));
shippingdetails.setColumn("SDADCOD", rst.getString(10));
shippingdetails.setColumn("SDFORMNO", rst.getString(11));
shippingdetails.setColumn("CREIND", rst.getString(12));
shippingdetails.setSERIALN(String.valueOf(j));
getBtnFetchInvDetEXPBILLclay().setEnabled(true);

shippingdetails.setNewKey();
shippingdetails.setFk(fkey);
shippingdetails.setSequence(count);

ExtEventShippingdetailsEntityWrapper shippingdwrapper = new ExtEventShippingdetailsEntityWrapper(
            shippingdetails, getDriverWrapper());
addNewExtEventShippingdetails(shippingdwrapper);

count++;

}

}

else {
Loggers.general().info(LOG,
      "Count value for shipping form NO in else------->" + count2 + "" + count_elc);

}
if (listef.getSize().intValue() == 0) {
getBtnFetchInvDetEXPBILLclay().setEnabled(false);
} else if (listef.getSize().intValue() > 0) {

getBtnFetchInvDetEXPBILLclay().setEnabled(true);
}
} else {
Loggers.general().info(LOG, "Not in any if");
}

}
try {
if (connection != null)
connection.close();
} catch (Exception e) {
e.getMessage();
}

}
/* Changes on 05032022 */
try {
String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
String shpquery = "SELECT SHP.CSHPAMT,SHP.CREPAY,SHP.COUTSAMT,SHP.XKEY FROM MASTER MAS,"
+ "BASEEVENT BEV,EXTEVENT EXT,EXTEVENTSHC SHP "
+ "WHERE MAS.KEY97=BEV.MASTER_KEY AND EXT.EVENT=BEV.KEY97 AND "
+ "SHP.FK_EVENT=EXT.KEY29 AND MAS.MASTER_REF='" + masRefNo
+ "' and BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) = '" + eventRef + "'";
System.out.println("Shipping Bill Outstanding changes: " + shpquery);
String shpamount = "";
String repayamt = "";
String outstamt = "";
String xkey = "";
con = ConnectionMaster.getConnection();
shp = con.prepareStatement(shpquery);
shpr = shp.executeQuery();
while (shpr.next()) {
shpamount = shpr.getString(1).trim();
repayamt = shpr.getString(2).trim();
outstamt = shpr.getString(3).trim();
xkey = shpr.getString(4).trim();

BigDecimal bshp, brep, bout, brep1;
bshp = new BigDecimal(shpamount);
brep = new BigDecimal(repayamt);
bout = new BigDecimal(outstamt);
bout = bshp.subtract(brep);
brep1 = new BigDecimal(0.00);

String shpupdate = "UPDATE EXTEVENTSHC SET COUTSAMT='" + bout + "' WHERE XKEY='" + xkey + "'";

shp = con.prepareStatement(shpupdate);
shp.executeUpdate();

System.out.println("Shipping Billing Query Update:" + shpupdate);

System.out.println("Shipping Billing Amount after fetching:" + bout);

System.out.println("Shipping Billing Amount after fetching:" + bout);

}
} catch (Exception e) {
e.printStackTrace();
System.out.println("Exception Shipping Billing Amount" + e.getMessage());
} finally {
ConnectionMaster.surrenderDB(con, shp, shpr);
}

} catch (Exception e) {

System.out.println("Exception for shipping population--->" + e.getMessage());

e.printStackTrace();
} finally {

try {
if (rst != null)
rst.close();
if (pst != null)
pst.close();
if (rs != null)
rs.close();
if (ps != null)
ps.close();
if (con != null)
con.close();
} catch (SQLException e) {

e.printStackTrace();
}

}

}

public void onFetchInvDetEXPBILLclayButton() {

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

EnigmaArray<ExtEventShippingCollectionsEntityWrapper> liste = getExtEventShippingCollectionsData();
EnigmaArray<ExtEventInvoiceDetailsEntityWrapper> listef = getExtEventInvoiceDetailsData();
EnigmaArray<ExtEventShippingdetailsEntityWrapper> listes = getExtEventShippingdetailsData();
Iterator<ExtEventShippingCollectionsEntityWrapper> iterator = liste.iterator();
Iterator<ExtEventInvoiceDetailsEntityWrapper> iterator1 = listef.iterator();
Iterator<ExtEventShippingdetailsEntityWrapper> iterator2 = listes.iterator();

for (ExtEventInvoiceDetailsEntityWrapper extship : listef) {
Loggers.general().info(LOG, "Entered into for all");
removeExtEventInvoiceDetails(extship);
}
int j = 0;
int count = 0;
String query = "";
Connection connection = null;
ResultSet rst = null;

PreparedStatement ps = null;
for (int i = 0; i < listes.getSize().intValue(); i++) {
if (connection == null) {
connection = ConnectionMaster.getConnection();
}

while (iterator.hasNext()) {

ExtEventShippingCollectionsEntityWrapper sdwrapper = (ExtEventShippingCollectionsEntityWrapper) iterator
.next();
ExtEventInvoiceDetailsEntityWrapper sdwrapper1 = (ExtEventInvoiceDetailsEntityWrapper) iterator1
.next();
ExtEventShippingdetailsEntityWrapper sdwrapper2 = (ExtEventShippingdetailsEntityWrapper) iterator2
.next();

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG, "bill number" + sdwrapper.getCBILLNUM());

}

String billnum = "";
String formNO = "";
String portcode = "";
String shipamt = "";

String add_lie = "";

try {
billnum = sdwrapper.getCBILLNUM().trim();

System.out.println("billnum num and other details" + billnum);
} catch (Exception e) {
System.out.println("Exception billnum num and other details" + e.getMessage());

}

try {
formNO = sdwrapper.getCFORMN().trim();
System.out.println("Form num and other details" + formNO);
} catch (Exception e) {
System.out.println("Exception Form num and other details" + e.getMessage());

}

portcode = sdwrapper.getCPORTCO();
shipamt = sdwrapper.getCSHPAMT();
if (!shipamt.equalsIgnoreCase("")) {
add_lie = shipamt.replaceAll("[^0-9]", "");
}
// String shidate = sdwrapper.getCBILLDA();
System.out.println("bill num and other details" + billnum + " " + portcode + " " + shipamt);
System.out.println("Shipping bill num" + billnum);
System.out.println("Shipping form num" + formNO);
j = j + 1;
int count2 = 0;
if (billnum.length() > 0 && formNO.length() > 0) {

String formNO_b = sdwrapper2.getSDFORMNO().trim();
String billnum_b = sdwrapper2.getSDBILLNO().trim();
String portcode_b = sdwrapper2.getSDPORTCODE();
String shidate = sdwrapper2.getSDBILDAT();
System.out.println("portcode billnum and form no" + portcode_b);
System.out.println("billnum_b billnum and form no" + billnum_b);
System.out.println("start of invoice export1");
String queryexport1 = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate),'yyyy-mm-dd') AS invdate, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY') AS SHIPBILLDATE, portcode, shipbillno, formno FROM ett_edpms_shp_inv WHERE formno='"
+ formNO_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
+ "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "' AND SHIPBILLNO='"
+ billnum_b + "'";
System.out.println("Query value Invoice billnum and form no" + queryexport1);
try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;
} catch (Exception e) {
System.out.println("Exception Query value Invoice form no" + e.getMessage());
}
ps = connection.prepareStatement(queryexport1);
rst = ps.executeQuery();

while (rst.next()) {
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventInvoiceDetails iinvoicedetails = new ExtEventInvoiceDetails();
iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
iinvoicedetails.setColumn("INVNO", rst.getString(2));
iinvoicedetails.setColumn("INVDATE", rst.getString(3));
System.out.println("invdate---" + iinvoicedetails.getColumn("INVDATE"));
System.out.println("date" + rst.getString(3));

iinvoicedetails.setColumn("IFOBAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5), "T")
                  + rst.getString(5));
iinvoicedetails.setColumn("CCY", rst.getString(5));
iinvoicedetails.setColumn("INVFRAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7), "T")
                  + rst.getString(7));
iinvoicedetails.setColumn("CCY_1", rst.getString(7));
iinvoicedetails.setColumn("INSUAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9), "T")
                  + rst.getString(9));
iinvoicedetails.setColumn("CCY_2", rst.getString(9));
iinvoicedetails.setColumn("ICOMMAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11), "T")
                  + rst.getString(11));
iinvoicedetails.setColumn("CCY_3", rst.getString(11));
iinvoicedetails.setColumn("IDISCAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13), "T")
                  + rst.getString(13));
iinvoicedetails.setColumn("CCY_4", rst.getString(13));
iinvoicedetails.setColumn("IDEDUAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15), "T")
                  + rst.getString(15));
iinvoicedetails.setColumn("CCY_5", rst.getString(15));
iinvoicedetails.setColumn("IPKGAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17), "T")
                  + rst.getString(17));
iinvoicedetails.setColumn("CCY_6", rst.getString(17));

iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));
if (billnum.length() > 0) {
iinvoicedetails.setColumn("ISHPBILL", rst.getString(21));
} else {
iinvoicedetails.setColumn("ISHPBILL", "");
}
iinvoicedetails.setColumn("IFORNO", rst.getString(22));

iinvoicedetails.setNewKey();
iinvoicedetails.setFk(fkey);
iinvoicedetails.setSequence(count);

ExtEventInvoiceDetailsEntityWrapper invoicewrapper = new ExtEventInvoiceDetailsEntityWrapper(
      iinvoicedetails, getDriverWrapper());
addNewExtEventInvoiceDetails(invoicewrapper);

count++;
if (liste.getSize().intValue() > 0)

getBtnFetchinvdetEXPCOLSETTclay().setEnabled(false);
getBtnFetchInvDetEXPBILLclay().setEnabled(false);

}
Loggers.general().info(LOG, "start of invoice export 2");
String queryexport2 = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate),'yyyy-mm-dd') AS invdate, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY') AS SHIPBILLDATE, portcode, shipbillno, formno FROM ett_edpms_shp_inv_softex WHERE formno='"
+ formNO_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
+ "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "' AND SHIPBILLNO='"
+ billnum_b + "'";
System.out.println("Query value Invoice form no" + queryexport2);
try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;
} catch (Exception e) {
System.out.println("Exception Query value Invoice form no" + e.getMessage());
}
ps = connection.prepareStatement(queryexport2);
rst = ps.executeQuery();
while (rst.next()) {
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventInvoiceDetails iinvoicedetails = new ExtEventInvoiceDetails();
iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
iinvoicedetails.setColumn("INVNO", rst.getString(2));
iinvoicedetails.setColumn("INVDATE", rst.getString(3));
System.out.println("invdate---" + iinvoicedetails.getColumn("INVDATE"));
System.out.println("date" + rst.getString(3));

iinvoicedetails.setColumn("IFOBAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5), "T")
                  + rst.getString(5));
iinvoicedetails.setColumn("CCY", rst.getString(5));
iinvoicedetails.setColumn("INVFRAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7), "T")
                  + rst.getString(7));
iinvoicedetails.setColumn("CCY_1", rst.getString(7));
iinvoicedetails.setColumn("INSUAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9), "T")
                  + rst.getString(9));
iinvoicedetails.setColumn("CCY_2", rst.getString(9));
iinvoicedetails.setColumn("ICOMMAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11), "T")
                  + rst.getString(11));
iinvoicedetails.setColumn("CCY_3", rst.getString(11));
// Loggers.general().info(LOG,"7");
iinvoicedetails.setColumn("IDISCAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13), "T")
                  + rst.getString(13));
iinvoicedetails.setColumn("CCY_4", rst.getString(13));
// Loggers.general().info(LOG,"8");
iinvoicedetails.setColumn("IDEDUAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15), "T")
                  + rst.getString(15));
// Loggers.general().info(LOG,"PORTCODE" +
// rst.getString(3));
iinvoicedetails.setColumn("CCY_5", rst.getString(15));
// Loggers.general().info(LOG,"9");
iinvoicedetails.setColumn("IPKGAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17), "T")
                  + rst.getString(17));
iinvoicedetails.setColumn("CCY_6", rst.getString(17));

iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));
if (billnum.length() > 0) {
iinvoicedetails.setColumn("ISHPBILL", rst.getString(21));
} else {
iinvoicedetails.setColumn("ISHPBILL", "");
}
iinvoicedetails.setColumn("IFORNO", rst.getString(22));
iinvoicedetails.setSERNO(String.valueOf(j));
iinvoicedetails.setNewKey();
iinvoicedetails.setFk(fkey);
iinvoicedetails.setSequence(count);

ExtEventInvoiceDetailsEntityWrapper invoicewrapper = new ExtEventInvoiceDetailsEntityWrapper(
      iinvoicedetails, getDriverWrapper());
addNewExtEventInvoiceDetails(invoicewrapper);

count++;
if (liste.getSize().intValue() > 0)

getBtnFetchinvdetEXPCOLSETTclay().setEnabled(false);
getBtnFetchInvDetEXPBILLclay().setEnabled(false);

}

} else if (billnum.length() > 0 && formNO.length() <= 0) {
System.out.println("billnum entered in invoice" + billnum);
String billnum_b = sdwrapper2.getSDBILLNO().trim();
String portcode_b = sdwrapper2.getSDPORTCODE();
String shidate = sdwrapper2.getSDBILDAT();
System.out.println("portcode invoice billnum no" + portcode_b);
System.out.println("billnum_b invoice" + billnum_b);
query = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate,'dd-mm-yyyy'),'yyyy-mm-dd') AS INDATE, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), shipbillno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY'), portcode, shipbillno, fileno FROM ett_edpms_shp_inv WHERE shipbillno='"
+ billnum_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
+ "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "'";

System.out.println("query value Invoice bill" + query);

try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;
} catch (Exception e) {
System.out.println("Exception Query value Invoice form no" + e.getMessage());
}
ResultSet rst2 = null;
ps = connection.prepareStatement(query);
rst2 = ps.executeQuery();
while (rst2.next()) {
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventInvoiceDetails iinvoicedetails = new ExtEventInvoiceDetails();
iinvoicedetails.setColumn("INVSRNO", rst2.getString(1));
// Loggers.general().info(LOG,"1");
iinvoicedetails.setColumn("INVNO", rst2.getString(2));
// Loggers.general().info(LOG,"2");
iinvoicedetails.setColumn("INVDATE", rst2.getString(3));
// Loggers.general().info(LOG,"3");
System.out.println("invdate---" + iinvoicedetails.getColumn("INVDATE"));
System.out.println("invoice number " + rst2.getString(2));

iinvoicedetails.setColumn("IFOBAMT",
      getDriverWrapper().convertFromToDBFormat(rst2.getString(4), rst2.getString(5), "T")
                  + rst2.getString(5));
iinvoicedetails.setColumn("CCY", rst2.getString(5));
// Loggers.general().info(LOG,"4");
iinvoicedetails.setColumn("INVFRAMT",
      getDriverWrapper().convertFromToDBFormat(rst2.getString(6), rst2.getString(7), "T")
                  + rst2.getString(7));
iinvoicedetails.setColumn("CCY_1", rst2.getString(7));
// Loggers.general().info(LOG,"5");
iinvoicedetails.setColumn("INSUAMT",
      getDriverWrapper().convertFromToDBFormat(rst2.getString(8), rst2.getString(9), "T")
                  + rst2.getString(9));
iinvoicedetails.setColumn("CCY_2", rst2.getString(9));
// Loggers.general().info(LOG,"6");
iinvoicedetails.setColumn("ICOMMAMT",
      getDriverWrapper().convertFromToDBFormat(rst2.getString(10), rst2.getString(11),
                  "T") + rst2.getString(11));
iinvoicedetails.setColumn("CCY_3", rst2.getString(11));
// Loggers.general().info(LOG,"7");
iinvoicedetails.setColumn("IDISCAMT",
      getDriverWrapper().convertFromToDBFormat(rst2.getString(12), rst2.getString(13),
                  "T") + rst2.getString(13));
iinvoicedetails.setColumn("CCY_4", rst2.getString(13));
// Loggers.general().info(LOG,"8");
iinvoicedetails.setColumn("IDEDUAMT",
      getDriverWrapper().convertFromToDBFormat(rst2.getString(14), rst2.getString(15),
                  "T") + rst2.getString(15));
// Loggers.general().info(LOG,"PORTCODE" +
// rst.getString(3));
iinvoicedetails.setColumn("CCY_5", rst2.getString(15));
System.out.println("9 " + rst2.getString(15));
iinvoicedetails.setColumn("IPKGAMT",
      getDriverWrapper().convertFromToDBFormat(rst2.getString(16), rst2.getString(17),
                  "T") + rst2.getString(17));
iinvoicedetails.setColumn("CCY_6", rst2.getString(17));

iinvoicedetails.setColumn("INVPRTCD", rst2.getString(20));
if (billnum.length() > 0) {
iinvoicedetails.setColumn("ISHPBILL", rst2.getString(21));
} else {
iinvoicedetails.setColumn("ISHPBILL", "");
}
System.out.println(" ISHPBILL " + rst2.getString(21));
iinvoicedetails.setSERNO(String.valueOf(j));
iinvoicedetails.setNewKey();
iinvoicedetails.setFk(fkey);
iinvoicedetails.setSequence(count);

ExtEventInvoiceDetailsEntityWrapper invoicewrapper = new ExtEventInvoiceDetailsEntityWrapper(
      iinvoicedetails, getDriverWrapper());
addNewExtEventInvoiceDetails(invoicewrapper);

count++;
if (liste.getSize().intValue() > 0)

getBtnFetchinvdetEXPCOLSETTclay().setEnabled(false);
getBtnFetchInvDetEXPBILLclay().setEnabled(false);
System.out.println("end of while loop ");
}

} else if (formNO.length() > 0 && billnum.length() <= 0) {
System.out.println("Invoice formNO" + formNO);

String portcode_f = sdwrapper2.getSDPORTCODE();
String formNO_f = sdwrapper2.getSDFORMNO().trim();
String shidate = sdwrapper2.getSDBILDAT();
System.out.println("Invoice formNO portcode" + portcode_f);
System.out.println("Invoice" + formNO_f);
query = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate),'yyyy-mm-dd') AS invdate, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY') AS SHIPBILLDATE, portcode, shipbillno, formno FROM ett_edpms_shp_inv_softex WHERE formno='"
+ formNO_f + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
+ "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_f + "'";
System.out.println("Query value Invoice form no" + query);
try {
if (ps != null)
ps.close();
if (rst != null)
rst.close();
rst = null;
} catch (Exception e) {
System.out.println(" Exception Invoice form no------>" + e.getMessage());
}
ps = connection.prepareStatement(query);
rst = ps.executeQuery();
while (rst.next()) {
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventInvoiceDetails iinvoicedetails = new ExtEventInvoiceDetails();
iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
// Loggers.general().info(LOG,"1");
iinvoicedetails.setColumn("INVNO", rst.getString(2));
// Loggers.general().info(LOG,"2");
iinvoicedetails.setColumn("INVDATE", rst.getString(3));
// Loggers.general().info(LOG,"3");
System.out.println("invdate---" + iinvoicedetails.getColumn("INVDATE"));
System.out.println("date" + rst.getString(3));

iinvoicedetails.setColumn("IFOBAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5), "T")
                  + rst.getString(5));
iinvoicedetails.setColumn("CCY", rst.getString(5));
// Loggers.general().info(LOG,"4");
iinvoicedetails.setColumn("INVFRAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7), "T")
                  + rst.getString(7));
iinvoicedetails.setColumn("CCY_1", rst.getString(7));
// Loggers.general().info(LOG,"5");
iinvoicedetails.setColumn("INSUAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9), "T")
                  + rst.getString(9));
iinvoicedetails.setColumn("CCY_2", rst.getString(9));
// Loggers.general().info(LOG,"6");
iinvoicedetails.setColumn("ICOMMAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11), "T")
                  + rst.getString(11));
iinvoicedetails.setColumn("CCY_3", rst.getString(11));
// Loggers.general().info(LOG,"7");
iinvoicedetails.setColumn("IDISCAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13), "T")
                  + rst.getString(13));
iinvoicedetails.setColumn("CCY_4", rst.getString(13));
// Loggers.general().info(LOG,"8");
iinvoicedetails.setColumn("IDEDUAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15), "T")
                  + rst.getString(15));
// Loggers.general().info(LOG,"PORTCODE" +
// rst.getString(3));
iinvoicedetails.setColumn("CCY_5", rst.getString(15));
// Loggers.general().info(LOG,"9");
iinvoicedetails.setColumn("IPKGAMT",
      getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17), "T")
                  + rst.getString(17));
iinvoicedetails.setColumn("CCY_6", rst.getString(17));

iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));
if (billnum.length() > 0) {
iinvoicedetails.setColumn("ISHPBILL", rst.getString(21));
} else {
iinvoicedetails.setColumn("ISHPBILL", "");
}
iinvoicedetails.setColumn("IFORNO", rst.getString(22));
iinvoicedetails.setSERNO(String.valueOf(j));
iinvoicedetails.setNewKey();
iinvoicedetails.setFk(fkey);
iinvoicedetails.setSequence(count);

ExtEventInvoiceDetailsEntityWrapper invoicewrapper = new ExtEventInvoiceDetailsEntityWrapper(
      iinvoicedetails, getDriverWrapper());
addNewExtEventInvoiceDetails(invoicewrapper);

count++;
if (liste.getSize().intValue() > 0)

getBtnFetchinvdetEXPCOLSETTclay().setEnabled(false);
getBtnFetchInvDetEXPBILLclay().setEnabled(false);

}

} else {
System.out.println("Not in any else=====>");
}

}

try {
if (connection != null)
connection.close();
} catch (Exception e) {
e.getMessage();
}

}

} catch (Exception e) {
e.printStackTrace();

System.out.println("Exception invoicedetails" + e.getMessage());

} finally {

try {
if (rst != null)
rst.close();
if (pst != null)
pst.close();
if (rst2 != null)
rst2.close();
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

}

/*
//Button to fetch shipping  bill details in odc added by Vishal G
public void onFetchShippingDetailsEXPBILLclayButton() {
Loggers.general().debug(LOG, "###onFetchShippingDetailsEXPBILLclayButton...");
// Loggers.general()
String customer = getDriverWrapper().getEventFieldAsText("RCV", "p", "cu");
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
EnigmaArray<ExtEventShippingCollectionsEntityWrapper> liste = getExtEventShippingCollectionsData();
Iterator<ExtEventShippingCollectionsEntityWrapper> iterator = liste.iterator();
int count = 0;
int j = 0;
String iecode = "";
String query = "";

Set<String> shp_bill = new HashSet<String>();
for (int i = 0; i < liste.getSize().intValue(); i++) {
while (iterator.hasNext()) {
ExtEventShippingCollectionsEntityWrapper val1 = (ExtEventShippingCollectionsEntityWrapper) iterator
.next();
String shippingBillno = val1.getCBILLNUM();
shp_bill.add(shippingBillno);
}
}

con = ConnectionMaster.getConnection();
for (int i = 0; i < liste.getSize().intValue(); i++) {
while (iterator.hasNext()) {

ExtEventShippingCollectionsEntityWrapper val1 = (ExtEventShippingCollectionsEntityWrapper) iterator
.next();
String shippingBillno = val1.getCBILLNUM();
String formNumber = val1.getCFORMN();
String notional = val1.getCNOTIONL();
BigDecimal sbAmtTotal = new BigDecimal(0);
BigDecimal fobamtbd = new BigDecimal(0);
BigDecimal freightbdbd = new BigDecimal(0);
BigDecimal insubd = new BigDecimal(0);
BigDecimal notionalbd = new BigDecimal(notional);
String freightcurr = "";
String insucurr = "";
String fobamt = "";
String freight = "";
String insurance = "";

if (shp_bill != null && !shp_bill.isEmpty()) {
if (shp_bill.contains(shippingBillno)) {
shp_bill.remove(shippingBillno);
System.out
      .println("SHIPPING bill no :" + shippingBillno + " " + formNumber + " " + notional);
String query1 = "SELECT IECODE  FROM EXTCUST where CUST = '" + customer + "'";

ps1 = con.prepareStatement(query1);
rs1 = ps1.executeQuery();
while (rs1.next()) {
iecode = rs1.getString(1);
val1.setCIECOD(iecode);
}
if (shippingBillno != null && !shippingBillno.equalsIgnoreCase("")) {
query = "SELECT shp.shipbillno,TO_CHAR(TO_DATE(shp.shipbilldate,'ddmmyyyy'),'yy-mm-dd') AS shipbilldate,shp.portcode, shp.formno,shp.iecode,"
            + " TO_CHAR(sum(inv.fobamt) , 'fm99999999999990.00'),inv.FOBCURRCODE,TO_CHAR(sum(inv.FRIEGHTAMT) , 'fm99999999999990.00'), "
            + " TO_CHAR(sum(inv.INSAMT) , 'fm99999999999990.00'),  "
            + "INV.FRIEGHTCURRCODE,INV.INSCURRCODE "
            + "FROM ett_edpms_shp shp,ett_edpms_shp_inv inv WHERE "
            + "inv.shipbillno = shp.shipbillno " + "and shp.PORTCODE=inv.PORTCODE "
            + "AND shp.shipbillno ='" + shippingBillno
            + "' group by  INV.FRIEGHTCURRCODE,INV.INSCURRCODE,inv.FOBCURRCODE, "
            + "shp.shipbillno,shp.shipbilldate,shp.portcode, shp.formno,shp.iecode";
}

if (formNumber != null && !formNumber.equalsIgnoreCase("")) {
query = "SELECT ESP.shipbillno,TO_CHAR(TO_DATE(ESP.shipbilldate,'ddmmyyyy'),'yy-mm-dd') AS shipbilldate,ESP.portcode, ESP.formno,ESP.iecode, "
            + " TO_CHAR(sum(ein.fobamt ), 'fm99999999999990.00'),ein.FOBCURRCODE,TO_CHAR(sum(ein.FRIEGHTAMT) , 'fm99999999999990.00'), "
            + " TO_CHAR(sum(ein.INSAMT) , 'fm99999999999990.00'),  "
            + " ein.FRIEGHTCURRCODE,ein.INSCURRCODE "
            + "FROM ETT_EDPMS_FILES EEF,ETT_EDPMS_SHP_SOFTEX ESP,ETT_EDPMS_SHP_INV_SOFTEX EIN "
            + "WHERE EEF.FILENO      = ESP.FILENO " + "AND ESP.FORMNO        = EIN.FORMNO "
            + "AND ESP.SHIPBILLDATE  = EIN.SHIPBILLDATE "
            + "AND ESP.PORTCODE      = EIN.PORTCODE "
            + "AND TRIM(ESP.FORMNO)||TRIM(ESP.SHIPBILLDATE)||TRIM(ESP.PORTCODE) "
            + "NOT IN (SELECT TRIM(EDT.FORMNO)||TRIM(EDT.SHIPBILLDATE)||TRIM(EDT.PORTCODE) "
            + "FROM ETT_GR_SHP_TBL EDT WHERE TRIM(EDT.SHIPBILLNO) IS NULL) AND ESP.FORMNO ='"
            + formNumber
            + "' group by  ein.FRIEGHTCURRCODE,ein.INSCURRCODE,ein.FOBCURRCODE, "
            + " ESP.shipbillno,esp.shipbilldate,ESP.portcode, ESP.formno,ESP.iecode";
}
// + "AND shp.iecode ='"+iecode+"' " ;
System.out.println("SQL QUERY: " + query);
// con = ConnectionMaster.getConnection();
pst = con.prepareStatement(query);
rst = pst.executeQuery();
j = j + 1;
while (rst.next()) {
// Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
// ExtEventShippingCollections shippingdetails = new
// ExtEventShippingCollections();
if (iecode.equalsIgnoreCase(rst.getString(5))) {
      val1.setCBILLDA(rst.getString(2));
      val1.setCPORTCO(rst.getString(3));
      // val1.setCFORMN(rst.getString(4));
      val1.setCREPAY("0.00" + " " + rst.getString(7));
      val1.setCSHCOLAM("0.00" + " " + rst.getString(7));
      val1.setSERIALNO(String.valueOf(j));
      fobamt = rst.getString(6);
      freight = rst.getString(8);
      insurance = rst.getString(9);

      fobamtbd = new BigDecimal(fobamt);
      freightbdbd = new BigDecimal(freight);
      insubd = new BigDecimal(insurance);
      sbAmtTotal = fobamtbd;
      freightcurr = rst.getString(10);
      insucurr = rst.getString(11);
      System.out.println("inside while SHIPPING bill no :" + fobamt + " " + freight + " "
                  + insurance + " " + fobamtbd + " " + insubd);
      if (freight != null && !freight.equalsIgnoreCase("") && freightcurr != null
                  && !freightcurr.equalsIgnoreCase("")) {
            if (freightcurr.equalsIgnoreCase("INR")) {
                  System.out.println("inside of freight currency :" + freightcurr + " "
                              + freight + " " + insurance + " " + fobamtbd + " " + freightbdbd);
                  freightbdbd = freightbdbd.divide(notionalbd, 4, RoundingMode.HALF_UP);
            } else {
                  freightbdbd = freightbdbd;
            }

            sbAmtTotal = sbAmtTotal.add(freightbdbd);
      }
      if (insurance != null && !insurance.equalsIgnoreCase("") && insucurr != null
                  && !insucurr.equalsIgnoreCase("")) {
            if (insucurr.equalsIgnoreCase("INR")) {
                  insubd = insubd.divide(notionalbd, 4, RoundingMode.HALF_UP);

            } else {
                  insubd = insubd;
            }
            sbAmtTotal = (sbAmtTotal.add(insubd).setScale(2, BigDecimal.ROUND_HALF_EVEN));
      }
//if (  rst.getString(13) != null && !rst.getString(13).equalsIgnoreCase("")) {
//sbAmtTotal = sbAmtTotal.add(new BigDecimal(rst.getString(13)));
//}
//if ( rst.getString(10) != null  &&  !rst.getString(10).equalsIgnoreCase("") ) {
//sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(10)));
//}
//if ( rst.getString(11) != null  &&  !rst.getString(11).equalsIgnoreCase("") ) {
//sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(11)));
//}
//if ( rst.getString(12) != null  &&  !rst.getString(12).equalsIgnoreCase("") ) {
//sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(12)));
//}
      // sbAmtTotal = sbAmtTotal.multiply(new BigDecimal(100));
      val1.setCSHPAMT((sbAmtTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN)) + " "
                  + rst.getString(7));

      // val1.setCIECOD(rst.getString(5));
      System.out.println(
                  "SHIPPING GRID DETAILS " + String.valueOf(j) + " " + sbAmtTotal + " ");
      // shippingdetails.setNewKey();
      // shippingdetails.setFk(fkey);
      // shippingdetails.setSequence(count);

      // ExtEventShippingCollectionsEntityWrapper invoicewrapper = new
      // ExtEventShippingCollectionsEntityWrapper(
      // shippingdetails, getDriverWrapper());
      // addNewExtEventShippingCollections(invoicewrapper);

      // count++;
      // if (liste.getSize().intValue() > 0)

      // getBtnFetchShippingDetailsEXPBILLclay().setEnabled(false);
}
}

}
}
}
}
} catch (SQLException e) {
e.printStackTrace();
} finally {

try {
if (rst != null)
rst.close();
if (pst != null)
pst.close();
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
try {
String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
String shpquery = "SELECT SHP.CSHPAMT,SHP.CREPAY,SHP.COUTSAMT,SHP.XKEY FROM MASTER MAS,"
+ "BASEEVENT BEV,EXTEVENT EXT,EXTEVENTSHC SHP "
+ "WHERE MAS.KEY97=BEV.MASTER_KEY AND EXT.EVENT=BEV.KEY97 AND "
+ "SHP.FK_EVENT=EXT.KEY29 AND MAS.MASTER_REF='" + masRefNo
+ "' and BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) = '" + eventRef + "'";
System.out.println("Shipping Bill Outstanding changes: " + shpquery);
String shpamount = "";
String repayamt = "";
String outstamt = "";
String xkey = "";
con = ConnectionMaster.getConnection();
shp = con.prepareStatement(shpquery);
shpr = shp.executeQuery();
while (shpr.next()) {
shpamount = shpr.getString(1).trim();
repayamt = shpr.getString(2).trim();
outstamt = shpr.getString(3).trim();
xkey = shpr.getString(4).trim();

BigDecimal bshp, brep, bout, brep1;
bshp = new BigDecimal(shpamount);
brep = new BigDecimal(repayamt);
bout = new BigDecimal(outstamt);
bout = bshp.subtract(brep);
brep1 = new BigDecimal(0.00);

String shpupdate = "UPDATE EXTEVENTSHC SET COUTSAMT='" + bout + "' WHERE XKEY='" + xkey + "'";

// String spupdate1="UPDATE EXTEVENTSHC SET CREPAY ='"+brep1+"' WHERE
// XKEY='"+xkey+"'";

shp = con.prepareStatement(shpupdate);
shp.executeUpdate();

System.out.println("Shipping Billing Query Update:" + shpupdate);

System.out.println("Shipping Billing Amount after fetching:" + bout);

// System.out.println("Shipping Billing Amount after fetching:"+bout);

}
} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, shp, shpr);
}
}

*/

//[18-Apr 2025] Re-aligned to production code [Aldryl]  
//Button to fetch shipping  bill details in odc added by Vishal G
public void onFetchShippingDetailsEXPBILLclayButton() {
Loggers.general().debug(LOG,"###onFetchShippingDetailsEXPBILLclayButton...");
//Loggers.general()
String customer = getDriverWrapper().getEventFieldAsText("RCV", "p", "cu");
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
EnigmaArray<ExtEventShippingCollectionsEntityWrapper> liste = getExtEventShippingCollectionsData();
Iterator<ExtEventShippingCollectionsEntityWrapper> iterator = liste.iterator();
int count = 0;
int j = 0;
String iecode = "";
String query = "";
con = ConnectionMaster.getConnection();
for (int i = 0; i < liste.getSize().intValue(); i++) {
while (iterator.hasNext()) {

ExtEventShippingCollectionsEntityWrapper val1 = (ExtEventShippingCollectionsEntityWrapper) iterator
.next();
String shippingBillno = val1.getCBILLNUM();
String formNumber = val1.getCFORMN();
String notional =val1.getCNOTIONL();
BigDecimal sbAmtTotal = new BigDecimal(0);
BigDecimal fobamtbd = new BigDecimal(0);
BigDecimal freightbdbd = new BigDecimal(0);
BigDecimal insubd = new BigDecimal(0);
BigDecimal notionalbd = new BigDecimal(notional);
String freightcurr="";
String insucurr="";
String fobamt="";
String freight="";
String insurance="";
// String iecode = val1.getCIECOD();
System.out.println("SHIPPING bill no :" + shippingBillno + " " + formNumber+" "+notional);
String query1 = "SELECT IECODE  FROM EXTCUST where CUST = '" + customer + "'";

ps1 = con.prepareStatement(query1);
rs1 = ps1.executeQuery();
while (rs1.next()) {
iecode = rs1.getString(1);
val1.setCIECOD(iecode);
}
if (shippingBillno != null && !shippingBillno.equalsIgnoreCase("")) {
query = "SELECT shp.shipbillno,TO_CHAR(TO_DATE(shp.shipbilldate,'ddmmyyyy'),'yy-mm-dd') AS shipbilldate,shp.portcode, shp.formno,shp.iecode,"
+ " TO_CHAR(sum(inv.fobamt) , 'fm99999999999990.00'),inv.FOBCURRCODE,TO_CHAR(sum(inv.FRIEGHTAMT) , 'fm99999999999990.00'), "
+ " TO_CHAR(sum(inv.INSAMT) , 'fm99999999999990.00'),  "
+ "INV.FRIEGHTCURRCODE,INV.INSCURRCODE "
+ "FROM ett_edpms_shp shp,ett_edpms_shp_inv inv WHERE "
+ "inv.shipbillno = shp.shipbillno " + "and shp.PORTCODE=inv.PORTCODE "
+ "AND shp.shipbillno ='" + shippingBillno + "' group by  INV.FRIEGHTCURRCODE,INV.INSCURRCODE,inv.FOBCURRCODE, "
+"shp.shipbillno,shp.shipbilldate,shp.portcode, shp.formno,shp.iecode";
}
if (formNumber != null && !formNumber.equalsIgnoreCase("")) {
query = "SELECT ESP.shipbillno,TO_CHAR(TO_DATE(ESP.shipbilldate,'ddmmyyyy'),'yy-mm-dd') AS shipbilldate,ESP.portcode, ESP.formno,ESP.iecode, "
+ " TO_CHAR(sum(ein.fobamt ), 'fm99999999999990.00'),ein.FOBCURRCODE,TO_CHAR(sum(ein.FRIEGHTAMT) , 'fm99999999999990.00'), "
+ " TO_CHAR(sum(ein.INSAMT) , 'fm99999999999990.00'),  "
+ " ein.FRIEGHTCURRCODE,ein.INSCURRCODE "
+ "FROM ETT_EDPMS_FILES EEF,ETT_EDPMS_SHP_SOFTEX ESP,ETT_EDPMS_SHP_INV_SOFTEX EIN "
+ "WHERE EEF.FILENO      = ESP.FILENO " + "AND ESP.FORMNO        = EIN.FORMNO "
+ "AND ESP.SHIPBILLDATE  = EIN.SHIPBILLDATE " + "AND ESP.PORTCODE      = EIN.PORTCODE "
+ "AND TRIM(ESP.FORMNO)||TRIM(ESP.SHIPBILLDATE)||TRIM(ESP.PORTCODE) "
+ "NOT IN (SELECT TRIM(EDT.FORMNO)||TRIM(EDT.SHIPBILLDATE)||TRIM(EDT.PORTCODE) "
+ "FROM ETT_GR_SHP_TBL EDT WHERE TRIM(EDT.SHIPBILLNO) IS NULL) AND ESP.FORMNO ='"
+ formNumber + "' group by  ein.FRIEGHTCURRCODE,ein.INSCURRCODE,ein.FOBCURRCODE, "
+" ESP.shipbillno,esp.shipbilldate,ESP.portcode, ESP.formno,ESP.iecode";
}
// + "AND shp.iecode ='"+iecode+"' " ;
System.out.println("SQL QUERY: " + query);
//    con = ConnectionMaster.getConnection();
pst = con.prepareStatement(query);
rst = pst.executeQuery();
j = j + 1;
while (rst.next()) {
// Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
// ExtEventShippingCollections shippingdetails = new
// ExtEventShippingCollections();
if (iecode.equalsIgnoreCase(rst.getString(5))) {
val1.setCBILLDA(rst.getString(2));
val1.setCPORTCO(rst.getString(3));
// val1.setCFORMN(rst.getString(4));
val1.setCREPAY("0.00" + " " + rst.getString(7));
val1.setCSHCOLAM("0.00" + " " + rst.getString(7));
val1.setSERIALNO(String.valueOf(j));
fobamt=rst.getString(6);
freight=rst.getString(8);
insurance=rst.getString(9);

fobamtbd = new BigDecimal(fobamt);
freightbdbd = new BigDecimal(freight);
insubd = new BigDecimal(insurance);
sbAmtTotal=fobamtbd;
freightcurr=rst.getString(10);
insucurr=rst.getString(11);
System.out.println("inside while SHIPPING bill no :" + fobamt + " " + freight+" "+insurance+" "+fobamtbd+" "+insubd);
if (  freight != null &&!freight.equalsIgnoreCase("")&& freightcurr != null && !freightcurr.equalsIgnoreCase("") ) {
if(freightcurr.equalsIgnoreCase("INR")) {
System.out.println("inside of freight currency :" + freightcurr + " " + freight+" "+insurance+" "+fobamtbd+" "+freightbdbd);
freightbdbd=freightbdbd.divide(notionalbd,4, RoundingMode.HALF_UP);
}
else {
freightbdbd=freightbdbd;
}

sbAmtTotal = sbAmtTotal.add(freightbdbd);
}
if ( insurance != null &&!insurance.equalsIgnoreCase("") &&  insucurr != null && !insucurr.equalsIgnoreCase("") ) {
if(insucurr.equalsIgnoreCase("INR")) {
insubd=insubd.divide(notionalbd,4, RoundingMode.HALF_UP);
            
}
else {
insubd=insubd;
}
sbAmtTotal = (sbAmtTotal.add(insubd).setScale(2,BigDecimal.ROUND_HALF_EVEN));
}
//if (  rst.getString(13) != null && !rst.getString(13).equalsIgnoreCase("")) {
//sbAmtTotal = sbAmtTotal.add(new BigDecimal(rst.getString(13)));
//}
//if ( rst.getString(10) != null  &&  !rst.getString(10).equalsIgnoreCase("") ) {
//sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(10)));
//}
//if ( rst.getString(11) != null  &&  !rst.getString(11).equalsIgnoreCase("") ) {
//sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(11)));
//}
//if ( rst.getString(12) != null  &&  !rst.getString(12).equalsIgnoreCase("") ) {
//sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(12)));
//}
//                sbAmtTotal = sbAmtTotal.multiply(new BigDecimal(100));
val1.setCSHPAMT((sbAmtTotal.setScale(2,BigDecimal.ROUND_HALF_EVEN)) + " "+ rst.getString(7));

// val1.setCIECOD(rst.getString(5));
System.out.println("SHIPPING GRID DETAILS " + String.valueOf(j) + " " + sbAmtTotal + " ");
// shippingdetails.setNewKey();
// shippingdetails.setFk(fkey);
// shippingdetails.setSequence(count);

// ExtEventShippingCollectionsEntityWrapper invoicewrapper = new
// ExtEventShippingCollectionsEntityWrapper(
// shippingdetails, getDriverWrapper());
// addNewExtEventShippingCollections(invoicewrapper);

// count++;
// if (liste.getSize().intValue() > 0)

// getBtnFetchShippingDetailsEXPBILLclay().setEnabled(false);
}
}

}
}
} catch (SQLException e) {
e.printStackTrace();
} finally {

try {
if (rst != null)
rst.close();
if (pst != null)
pst.close();
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
try {
String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
String shpquery = "SELECT SHP.CSHPAMT,SHP.CREPAY,SHP.COUTSAMT,SHP.XKEY FROM MASTER MAS,"
+ "BASEEVENT BEV,EXTEVENT EXT,EXTEVENTSHC SHP "
+ "WHERE MAS.KEY97=BEV.MASTER_KEY AND EXT.EVENT=BEV.KEY97 AND "
+ "SHP.FK_EVENT=EXT.KEY29 AND MAS.MASTER_REF='" + masRefNo
+ "' and BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) = '" + eventRef + "'";
System.out.println("Shipping Bill Outstanding changes: " + shpquery);
String shpamount = "";
String repayamt = "";
String outstamt = "";
String xkey = "";
con = ConnectionMaster.getConnection();
shp = con.prepareStatement(shpquery);
shpr = shp.executeQuery();
while (shpr.next()) {
shpamount = shpr.getString(1).trim();
repayamt = shpr.getString(2).trim();
outstamt = shpr.getString(3).trim();
xkey = shpr.getString(4).trim();

BigDecimal bshp, brep, bout, brep1;
bshp = new BigDecimal(shpamount);
brep = new BigDecimal(repayamt);
bout = new BigDecimal(outstamt);
bout = bshp.subtract(brep);
brep1 = new BigDecimal(0.00);

String shpupdate = "UPDATE EXTEVENTSHC SET COUTSAMT='" + bout + "' WHERE XKEY='" + xkey + "'";

// String spupdate1="UPDATE EXTEVENTSHC SET CREPAY ='"+brep1+"' WHERE
// XKEY='"+xkey+"'";

shp = con.prepareStatement(shpupdate);
shp.executeUpdate();

System.out.println("Shipping Billing Query Update:" + shpupdate);

System.out.println("Shipping Billing Amount after fetching:" + bout);

// System.out.println("Shipping Billing Amount after fetching:"+bout);

}
} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, shp, shpr);
}
}
//[18-Apr 2025] Re-aligned to production code [Aldryl]  

// Button to display harmonised code and details added by Vishal G
public void onHSFETCHINWCOLCREclayButton() {
String hscodeval = getHMON().trim();
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
if ((!hscodeval.equalsIgnoreCase("")) && hscodeval != null) {
try {

String hyperValue = "SELECT trim(HSPOY),trim(HDESC) FROM EXTHMCODE WHERE HCODEE='" + hscodeval + "'";
// Loggers.general().info(LOG,"Hs code query Value---->" + hyperValue);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Hs code query Value---->" + hyperValue);

}
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(hyperValue);
rs = ps.executeQuery();
while (rs.next()) {
String hsploy = rs.getString(1);
String hsdesc = rs.getString(2);
// Loggers.general().info(LOG,"Policy value---->" + hsploy);
setHSPOLY(hsploy);
setHDESC(hsdesc);
}

} catch (Exception e) {
// Loggers.general().info(LOG,"exceptio is " + e);
}

finally {

try {
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
// Loggers.general().info(LOG,"HS code is empty for policy ");
}
}

public void onUIDFETCHINWDOCCOLPAYclayButton() {
System.out.println("UID: Fetch button click!!");
String date = getDriverWrapper().getEventFieldAsText("cBFG", "d", "");
String subType = getDriverWrapper().getEventFieldAsText("cBFF", "s", "");
String uid = getDriverWrapper().getEventFieldAsText("cBES", "s", "");
String refno = getDriverWrapper().getEventFieldAsText("cBFE", "s", "");
Map<String, String> uidResponseTokens = null;
if (date != null && !date.equalsIgnoreCase("") && subType != null && !subType.equalsIgnoreCase("")
&& uid != null && !uid.equalsIgnoreCase("") && refno != null && !refno.equalsIgnoreCase("")) {
System.out.println("UID: Calling API");
uidResponseTokens = UidSelectUtil.getUidDetailsFromAPI(date, subType, uid, refno);
String status = uidResponseTokens.get("Status");
System.out.println(" Status " + status);
if (status.equalsIgnoreCase("S")) {
String resultantSubType = uidResponseTokens.get("TranSubType");
String uidccy = uidResponseTokens.get("UidCcy");
System.out.println("resultantSubType: " + resultantSubType);
System.out.println("uidccy: " + uidccy);
}
}

}

// Button to display pan no. and iecode added by Vishal G
public void onPANNUMINWCOLCREclayButton() {
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
String customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");
try {
con = ConnectionMaster.getConnection();
String query = "select panno,IECODE from extcust where cust= '" + customer + "'";
// Loggers.general().info(LOG,"query " + query);
System.out.println(" panno query: " + query + " " + customer);
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
String panno = rs.getString(1);
String iecode = rs.getString(2);
// Loggers.general().info(LOG,"Customer" + cust);
System.out.println(" panno data: " + panno + " " + iecode);
setPANDETAI(panno);
setIECODE(iecode);
}
} catch (Exception e) {
e.printStackTrace();
// Loggers.general().info(LOG,"exception caught");
} finally {
ConnectionMaster.surrenderDB(con, ps, rs);
}
}

// Button to display pan no. and iecode added by Vishal G
public void onPANNUMICCCREButton() {
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
String customer = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu");
try {
con = ConnectionMaster.getConnection();
String query = "select panno,IECODE from extcust where cust= '" + customer + "'";
// Loggers.general().info(LOG,"query " + query);
System.out.println(" panno query: " + query + " " + customer);
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
String panno = rs.getString(1);
String iecode = rs.getString(2);
// Loggers.general().info(LOG,"Customer" + cust);
System.out.println(" panno data: " + panno + " " + iecode);
setPANDETAI(panno);
setIECODE(iecode);
}
} catch (Exception e) {
e.printStackTrace();
// Loggers.general().info(LOG,"exception caught");
} finally {
ConnectionMaster.surrenderDB(con, ps, rs);
}
}

// Button to display pan no. and iecode added by Vishal G
public void onGETPANoutcleancashcreclayButton() {
String customer = getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
try {
con = ConnectionMaster.getConnection();
String query = "select panno,IECODE from extcust where cust= '" + customer + "'";
System.out.println("panno query: " + query);
ps = con.prepareStatement(query);
rs = ps.executeQuery();
while (rs.next()) {
String panno = rs.getString(1);
String iecode = rs.getString(2);
System.out.println("panno data: " + panno + " " + iecode);
setPANDETAI(panno);
setIECODE(iecode);
}
} catch (Exception e) {
e.printStackTrace();
} finally {
ConnectionMaster.surrenderDB(con, ps, rs);
}
}

public void onFETCHACCOUNTEXPBILLclayButton() {
account();
}

public void onFETCHACCOUNTEXPCOLSETTclayButton() {
account();
}

// Fetch button to get pca account details in export product added by Vishal G
public void account() {
try {
EnigmaArray<ExtEventAccountRealisationEntityWrapper> liste = getExtEventAccountRealisationData();
con = ConnectionMaster.getConnection();
String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String key29 = "";
int count = 1;
//String query="Select bev.extfield from master mas,baseevent bev "+
//"where mas.key97=bev.master_key "+
//"and mas.MASTER_REF='"+masReference.trim()+"'"+
//" and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+eventCode+"'"+
//" and bev.status not in ('a','c')";
//System.out.println("query for account code button:"+query+" "+masReference+" "+eventCode);
//dmsp = con.prepareStatement(query);
//// //Loggers.general().info(LOG,"DMS Query " + dms);
//dmsr = dmsp.executeQuery();   
//while (dmsr.next()) {
//key29 = dmsr.getString(1);
//// dmsurl = dmsstr + "&dirAmount=" + payamt;
//
//}
String accountQuery = "Select xkey,seqn,fk_event,raccount,raccttyp,ramount,rpercent,response,drcr from exteventaccount "
+ " where fk_event=(" + "Select bev.extfield from master mas,baseevent bev "
+ "where mas.key97=bev.master_key " + "and mas.MASTER_REF='" + masReference + "'"
+ " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='" + eventCode + "'"
+ " and bev.status not in ('a','c')" + ")";
account = con.prepareStatement(accountQuery);
rst1 = account.executeQuery();
System.out.println("account query fetch" + accountQuery);
while (rst1.next()) {
Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
ExtEventAccountRealisation account = new ExtEventAccountRealisation();

account.setRACCOUNT(rst1.getString(4));
account.setRACCTTYP(rst1.getString(5));
account.setRAMOUNT(rst1.getString(6));
account.setRPERCENT(rst1.getString(7));
System.out
.println("inside while loop of account" + rst1.getString(3) + " ramount " + rst1.getString(5));
account.setRESPONSE(rst1.getString(8));
account.setDRCR(rst1.getString(9));
account.setNewKey();
account.setFk(fkey);
account.setSequence(count);
System.out.println("last of while foreign " + fkey);
ExtEventAccountRealisationEntityWrapper accountwrapper = new ExtEventAccountRealisationEntityWrapper(
account, getDriverWrapper());
addNewExtEventAccountRealisation(accountwrapper);
count++;
//if (liste.getSize().intValue() > 0)
//{
//getBtnFETCHACCOUNTEXPCOLSETTclay().setEnabled(false);
//getBtnFETCHACCOUNTEXPBILLclay().setEnabled(false);
//}

System.out.println("last of while  " + count);
}
} catch (Exception e) {
e.printStackTrace();
System.out.println("Exception update" + e.getMessage());
} finally {
ConnectionMaster.surrenderDB(con, account, rst1);
ConnectionMaster.surrenderDB(con, dmsp, dmsr);
}
}

public void onORMFETCHINWCOLCREclayButton() {
getOrmDetails();
}

public void onORMFETCHINWDOCCOLPAYclayButton() {
getOrmDetails();
}

// Fetch button to get orm details in import product added by Vishal G
public void getOrmDetails() {

try {
String outward = "";
double totalAmt = 0;
long balanceAmt = 0;
String balance = "0.0";
String creditamt = "0";
String creditccy = "";
String date = "";
String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String custId = getDriverWrapper().getEventFieldAsText("DRE", "p", "cu").trim();
con = ConnectionMaster.getConnection();
EnigmaArray<ExtEventOrmDetailsEntityWrapper> liste = getExtEventOrmDetailsData();
Iterator<ExtEventOrmDetailsEntityWrapper> iterator1 = liste.iterator();
System.out.println("orm button  " + masReference + " " + eventCode + " " + custId);
for (int i = 0; i < liste.getSize().intValue(); i++) {
while (iterator1.hasNext()) {
ExtEventOrmDetailsEntityWrapper fdwarapper1 = (ExtEventOrmDetailsEntityWrapper) iterator1.next();
outward = fdwarapper1.getOUTWARD();
if (!outward.equalsIgnoreCase("")) {
//String outwardQuery="select CREDIT_AMOUNT,CREDIT_CURRENCY,to_char(to_date(VALUE_DATE,'dd-mm-yy'),'yyyy-mm-dd') from ETTV_OUTWARD_REMITTANCE_AMOUNT where mas_ref='"+outward+"' and CIF_NO='"+custId+"'";
//System.out.println("query for getting all fields in outward Remittance grid " + outwardQuery);
//pst = con.prepareStatement(outwardQuery);
//rs1 = pst.executeQuery();
//if (rs1.next()) {
//fdwarapper1.setREMAMT(rs1.getString(1)+" "+rs1.getString(2));
//fdwarapper1.setREMDATE(rs1.getString(3));
//
//}
//String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.UTILIZED,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventorm ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//+ masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//+ eventCode + "') AND ext.OUTWARD ='" + outward
//+ "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.UTILIZED,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventorm ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//+ masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//+ eventCode + "') AND ext.OUTWARD ='" + outward
//+ "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.UTILIZED,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventorm ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
//+ masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
//+ eventCode + "') AND ext.OUTWARD ='" + outward
//+ "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";
//System.out.println( "query for previous orm sum " + BalAmtQuery+" "+rs1.getString(1)+" "+rs1.getString(2)+" "+rs1.getString(3));
//account = con.prepareStatement(BalAmtQuery);
//rst3 = account.executeQuery();
//if (rst3.next()) {
//totalAmt=rst3.getDouble(1);
//}
System.out.println("inside if of orm button  :" + outward);
String sql = "{call ETT_ORM_HISTORY(?,?,?,?,?,?,?,?)}";

stmt = con.prepareCall(sql);
stmt.setString(1, outward);
stmt.setString(2, custId);
stmt.setString(3, masReference);
stmt.setString(4, eventCode);
stmt.registerOutParameter(5, Types.VARCHAR);
stmt.registerOutParameter(6, Types.VARCHAR);
stmt.registerOutParameter(7, Types.VARCHAR);
stmt.registerOutParameter(8, Types.DOUBLE);

stmt.execute();
creditamt = stmt.getString(5);
creditccy = stmt.getString(6);
date = stmt.getString(7);
totalAmt = stmt.getDouble(8);

fdwarapper1.setREMAMT(creditamt + " " + creditccy);
fdwarapper1.setREMDATE(date);
System.out.println("quer orm procedure " + creditamt + " " + date + " " + totalAmt);
}
balanceAmt = (long) (Double.valueOf(creditamt) - totalAmt);
if (balanceAmt > 0) {

balance = String.valueOf(balanceAmt);
fdwarapper1.setAVAILABL(balance + " " + creditccy);

} else {
fdwarapper1.setAVAILABL(0 + " " + creditccy);
}
}

}

}

catch (Exception e) {
e.printStackTrace();
System.out.println("Exception update in outward grid" + e.getMessage());
} finally {
if (stmt != null)
try {
stmt.close();
} catch (SQLException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
if (con != null)
try {
con.close();
} catch (SQLException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}

}
}