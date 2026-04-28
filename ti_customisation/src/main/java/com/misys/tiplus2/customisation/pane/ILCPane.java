package com.misys.tiplus2.customisation.pane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Wrapper;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventCounterGuranteeEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarking;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarkingDAO;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarkingEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventPrePurchaseOrderEntityWrapper;
//import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.ExtEventPreshipmentEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTax;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTaxEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingTable;
import com.misys.tiplus2.customisation.entity.ExtEventShippingTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetails;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventOrmDetails;
import com.misys.tiplus2.customisation.entity.ExtEventOrmDetailsEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.customisation.extension.EventExtension;
import com.misys.tiplus2.customisation.extension.OdcFEC;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
//import com.misys.tiplus2.customisation.extension.ServiceTAXCALC;
import com.misys.tiplus2.apps.ti.kernel.extpm.pane.ExtEventExtensionDriverPWrapper;
import com.misys.tiplus2.enigma.lang.control.EnigmaControl;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.pane.ExtensionViewPaneMode;
import com.misys.tiplus2.enigma.customisation.validation.ValidationTexts;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
//import com.bs.theme.transport.client.ThemeTransportClient;
import com.bs.theme.transport.client.ThemeTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class ILCPane extends EventPane {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(ILCPane.class);
      Connection con, con1 = null;
      PreparedStatement peco, ps1, ps, dmsp, ttRatePS, psd,account,pst = null;
      ResultSet dmsr1, rs1, rs, dmsr, ttRateRS, rst,rst3 = null;
      CallableStatement stmt=null;
      String swachhCharge = "";
      String serviceTax = "";

      public static String randomCorrelationId() {
            // Loggers.general().info(LOG,"randomCorrelationId generate");
            return UUID.randomUUID().toString();
      }

      // public void onFETCHIFSCIMPLCclayButton() {
      // // Loggers.general().info(LOG,"ILC ISSUE IFSC FETCH");
      // FETCHIFSC();
      // }
      //
      // public void onFETCHIFSCILCAMDclayButton() {
      // // Loggers.general().info(LOG,"ILC AMEND IFSC FETCH");
      // FETCHIFSC();
      // // Loggers.general().debug(LOG, "on{}Button:{}", "FETCHIFSCILCAMDclay",
      // // ValidationTexts.METHOD_NOT_IMPLEMENTED);
      // }
      //
      // public void onFETCHIFSCIMPADVclayButton() {
      // // Loggers.general().info(LOG,"ILC Adjust IFSC FETCH");
      // FETCHIFSC();
      // }
      //
      // public void onFETCHIFSCOGTISSclayButton() {
      // FETCHIFSC();
      // }

      // public void onFETCHIFSCIMPGUAADJclayButton() {
      // FETCHIFSC();
      // }
      //
      // public void onFETCHIFSCIMPGUAAMDclayButton() {
      // FETCHIFSC();
      // }
      public void onFETCHIFSCIMPSTDLCISSclayButton() {
            FETCHIFSC();
      };

      public void onFETCHIFSCIMPSTDLCAMDclayButton() {
            FETCHIFSC();
      };

      public void onFETCHIFSCIMPSTDLCADJclayButton() {
            FETCHIFSC();
      }

      public void onHSFETCHIMPLCclayButton() {

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
                              Loggers.general().info(LOG,"Hs code query Value---->" + hyperValue);

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

      // public boolean FETCHIFSC() {
      // boolean value = false;
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
      // String behalfHalfBranch = getDriverWrapper().getEventFieldAsText("BOB",
      // "s", "");
      // String amountCurrency = getDriverWrapper().getEventFieldAsText("ORA",
      // "v", "c");
      // String subProductType = getDriverWrapper().getEventFieldAsText("PTP",
      // "s", "");
      // String query = "";
      // String senderIfsc = "";
      // if (subProductType.equalsIgnoreCase("ILD") &&
      // amountCurrency.equalsIgnoreCase("INR")) {
      // query = "select trim(IFSC) from extbramas where BCODE='" +
      // behalfHalfBranch + "' ";
      //
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"IFSC code fetch query----->" + query);
      //
      // }
      // con = ConnectionMaster.getConnection();
      // ps1 = con.prepareStatement(query);
      // rs1 = ps1.executeQuery();
      // while (rs1.next()) {
      // senderIfsc = rs1.getString(1);
      // }
      // // Loggers.general().info(LOG,"Sender IFSC code----->" + senderIfsc);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Sender IFSC code----->" + senderIfsc);
      //
      // }
      // setSENIFSC(senderIfsc);
      // }
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"Exeception of recifsc " + e.getMessage());
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Exeception of recifsc " + e.getMessage());
      //
      // }
      //
      // } finally {
      //
      // try {
      // if (rs1 != null)
      // rs1.close();
      // if (ps1 != null)
      // ps1.close();
      // if (con != null)
      // con.close();
      // } catch (SQLException e) {
      // // Loggers.general().info(LOG,"Connection Failed! Check output
      // // console");
      // e.printStackTrace();
      // }
      //
      // }
      // return value;
      // }

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

                  String subProductType = "";
                  String behalfHalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
                  String amountCurrency = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
                  if (getMajorCode().equalsIgnoreCase("ISB")) {
                        subProductType = getDriverWrapper().getEventFieldAsText("PUO3", "s", "");
                  }
                  String query = "";
                  String senderIfsc = "";
                  if (subProductType.equalsIgnoreCase("INL") && amountCurrency.equalsIgnoreCase("INR")) {
                        query = "select trim(IFSC) from extbramas where BCODE='" + behalfHalfBranch + "' ";
                        con = ConnectionMaster.getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              senderIfsc = rs1.getString(1);
                        }
                        // Loggers.general().info(LOG,"Sender IFSC code----->" + senderIfsc);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"Sender IFSC code----->" + senderIfsc);

                        }
                        setSENIFSC(senderIfsc);
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exeception of recifsc " + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        Loggers.general().info(LOG,"Exeception of recifsc " + e.getMessage());

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
            return value;
      }

      public void onMERCHANTIMPLCCLAIMRECclayButton() {
            // String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r",
            // "");
            String mercht = getDriverWrapper().getEventFieldAsText("cARQ", "l", "").toString();
            String relrefno = getREMERREF();
            String adremno = getADOUTREM();

            int dmT = 0;

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
                  // //Loggers.general().info(LOG,"enter into try");

                  String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                              + relrefno + "'";
                  // Loggers.general().info(LOG,"Master ref no valid for Import lc" + dms);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Master ref no valid for Import lc" + dms);

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
                                    String merdate = "";
                                    String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                + relrefno + "' and MERDUET is not null";
                                    // Loggers.general().info(LOG,"values fetching Import lc" +
                                    // query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"values fetching Import lc" + query_dms);

                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          merdate = rs.getString(1);
      //                                  setMERDUET(merdate);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "AFTER GET THE VALUE Import lc MERDUET" + merdate + "" + merdate.length());

                                          }
                                    }
                                    // Loggers.general().info(LOG,"Merchant trade length import lc
                                    // out of loop------>" + merdate.length());
      //                            String merch = getMERDUET();
                                    // Loggers.general().info(LOG,"Merchant trade import lc------>"
                                    // + merch);
//                                  if (merdate.length() < 1 && merch.length() < 1 && getMajorCode().equalsIgnoreCase("ILC")) {
//                                        String recdate = getDASHIP_Name();
//                                        // Loggers.general().info(LOG,"systemDate date --->" +
//                                        // recdate);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"systemDate date --->" + recdate);
//
//                                        }
//                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//                                        Calendar cal = Calendar.getInstance();
//                                        int gra = 270;
//
//                                        try {
//                                              cal.setTime(sdf.parse(recdate));
//                                              cal.add(Calendar.DATE, gra);
//                                              String output = sdf.format(cal.getTime());
//                                              // Loggers.general().info(LOG,"output Merchant
//                                              // trade----->" + output);
//                //                            setMERDUET(output);
//
//                                        } catch (Exception e) {
//                                              // Loggers.general().info(LOG,"Import lc value date
//                                              // --->" + e.getMessage());
//                                              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                                    Loggers.general().info(LOG,"Import lc value date--->" + e.getMessage());
//
//                                              }
//
//                                        }
//
//                                  } else {
//                                        // Loggers.general().info(LOG,"Else port Import lc");
//
//                                  }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Merchant trade is not tickec or
                                    // master not valide Import lc exception--->" +
                                    // e.getMessage());

                              }

                        } else {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Merchant trade is not tickec or master not valide Import lc --->" + dmT + mercht);

                              }
                        }

                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details lc--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Merchanting details lc--->" + e.getMessage());

                  }
            }
            try {
                  // inward renittance
                  String query_adv = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS ADOUTREM FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND prod.CODE ='AIR' AND mas.MASTER_REF ='"
                              + adremno + "'";
                  // Loggers.general().info(LOG,"Advance rem no valid for Export lc" +
                  // query_adv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Advance rem no valid for Export lc" + query_adv);
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
                                          Loggers.general().info(LOG,"Advance rem no values fetching Import lc" + query_dms);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String recdate = rs.getString(1);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Outward received date fetching " + recdate);

                                                Loggers.general().info(LOG,"systemDate date --->" + recdate);
                                          }
                                          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                          Calendar cal = Calendar.getInstance();
                                          int gra = 120;

                                          try {
                                                cal.setTime(sdf.parse(recdate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                String output = sdf.format(cal.getTime());
                                                // Loggers.general().info(LOG,"output----->" + output);
                        //                      setMERDUET(output);

                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"Sight value date --->" +
                                                // e.getMessage());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Sight value date --->" + e.getMessage());
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
                  // Loggers.general().info(LOG,"Merchanting details outward--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Merchanting details outward--->" + e.getMessage());
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

      public void onMERCHATIMPLCclayButton() {
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
                        Loggers.general().info(LOG,"Master ref no valid for Import lc" + dms);
                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(dms);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                        }
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {
                                    // //Loggers.general().info(LOG,"enter into try");

                                    String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                + relrefno + "'";
                                    // Loggers.general().info(LOG,"values fetching Import lc" +
                                    // query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"values fetching Import lc" + query_dms);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String merdate = rs.getString(1);
                        //                setMERDUET(merdate);
                                          // Loggers.general().info(LOG,"AFTER GET THE VALUE Import
                                          // lc" + dmT);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }

                        } else {
                              Loggers.general().info(LOG,"Merchant trade is not tickec or master not valide Import lc --->" + dmT + mercht);
                        }

                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details lc--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Merchanting details lc--->" + e.getMessage());
                  }
            }
            try {
                  // inward renittance
                  String query_adv = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS ADOUTREM FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND prod.CODE ='AIR' AND mas.MASTER_REF ='"
                              + adremno + "'";
                  // Loggers.general().info(LOG,"Advance rem no valid for Export lc" +
                  // query_adv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Advance rem no valid for Export lc" + query_adv);
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
                                          Loggers.general().info(LOG,"Advance rem no values fetching Import lc" + query_dms);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String recdate = rs.getString(1);
                                          // Loggers.general().info(LOG,"systemDate date --->" +
                                          // recdate);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Outward received date fetching " + recdate);

                                                Loggers.general().info(LOG,"systemDate date --->" + recdate);
                                          }
                                          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                          Calendar cal = Calendar.getInstance();
                                          int gra = 120;

                                          try {
                                                cal.setTime(sdf.parse(recdate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                String output = sdf.format(cal.getTime());
                                                // Loggers.general().info(LOG,"output----->" + output);
                        //                      setMERDUET(output);

                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"Sight value date --->" +
                                                // e.getMessage());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Sight value date --->" + e.getMessage());
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
                  // Loggers.general().info(LOG,"Merchanting details outward--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Merchanting details outward--->" + e.getMessage());
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

      public void onMERCHATIMPADVclayButton() {

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
                        Loggers.general().info(LOG,"Master ref no valid for Import lc" + dms);
                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(dms);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                        }
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {
                                    // //Loggers.general().info(LOG,"enter into try");

                                    String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                + relrefno + "'";
                                    // Loggers.general().info(LOG,"values fetching Import lc" +
                                    // query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"values fetching Import lc" + query_dms);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String merdate = rs.getString(1);
                        //                setMERDUET(merdate);
                                          // Loggers.general().info(LOG,"AFTER GET THE VALUE Import
                                          // lc" + dmT);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"AFTER GET THE VALUE Import lc" + dmT);
                                          }
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }

                        } else {
                              Loggers.general().info(LOG,"Merchant trade is not tickec or master not valide Import lc --->" + dmT + mercht);
                        }

                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details lc--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Merchanting details lc--->" + e.getMessage());
                  }
            }
            try {
                  // inward renittance
                  String query_adv = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS ADOUTREM FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND prod.CODE ='AIR' AND mas.MASTER_REF ='"
                              + adremno + "'";
                  // Loggers.general().info(LOG,"Advance rem no valid for Export lc" +
                  // query_adv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Advance rem no valid for Export lc" + query_adv);
                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(query_adv);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"query_adv AFTER GET THE VALUE " + dmT);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"query_adv AFTER GET THE VALUE " + dmT);
                        }
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {

                                    String query_dms = "SELECT TO_CHAR(cpm.RCV_DATE,'DD/MM/YY') AS REV_DATE FROM master mas, CPAYMASTER cpm, PARTYDTLS prt, TIDATAITEM tid WHERE mas.key97 = tid.MASTER_KEY AND tid.KEY97 = prt.KEY97 AND mas.KEY97 = cpm.KEY97 AND prt.ROLE = 'BEN' AND mas.master_ref ='"
                                                + adremno + "'";
                                    // Loggers.general().info(LOG,"Advance rem no values fetching
                                    // Import lc" + query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Advance rem no values fetching Import lc" + query_dms);
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
                                          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                          Calendar cal = Calendar.getInstance();
                                          int gra = 120;

                                          try {
                                                cal.setTime(sdf.parse(recdate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                String output = sdf.format(cal.getTime());
                                                // Loggers.general().info(LOG,"output----->" + output);
                              //                setMERDUET(output);

                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"Sight value date --->" +
                                                // e.getMessage());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Sight value date --->" + e.getMessage());
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
                  // Loggers.general().info(LOG,"Merchanting details outward--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Merchanting details outward--->" + e.getMessage());
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

      public void onINSURENCEIMPLCclayButton() {

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

            String fullamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
            String fullcur = getDriverWrapper().getEventFieldAsText("FOA", "v", "c");
            String Sight_Payment = getDriverWrapper().getEventFieldAsText("AVBY", "s", "");
            // Loggers.general().info(LOG,"Available by payment--->" + Sight_Payment);
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"Available by payment--->" + Sight_Payment);
            }
            Loggers.general().info(LOG,"Available by payment--->" + Sight_Payment);
            Loggers.general().info(LOG,"Available by fullamt--->" + fullamt);
            Loggers.general().info(LOG,"Available by fullcur--->" + fullcur);

            try {
                  String intamount = getINTAMT();
                  Loggers.general().info(LOG,"Available by intamount--->" + intamount);
                  

                  if (!fullamt.equalsIgnoreCase("") && fullamt.length() > 0) {

                        BigDecimal lcAmount = new BigDecimal(fullamt);
                        BigDecimal Amount110 = new BigDecimal("110");
                        BigDecimal Amount100 = new BigDecimal("100");
                        BigDecimal Amount36500 = new BigDecimal("36500");
                        BigDecimal Amount36000 = new BigDecimal("36000");

                        Loggers.general().info(LOG,"Full amount and currency " + lcAmount);

                        ConnectionMaster connectionMaster = new ConnectionMaster();

                        try {
                              BigDecimal totalLcAmt = lcAmount.multiply(Amount110);

                              String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);

                              if (divideByDecimal.equalsIgnoreCase("1")) {

                                    DecimalFormat diff = new DecimalFormat("0");
                                    diff.setMaximumFractionDigits(1);
                                    BigDecimal totalLcAmount100 = totalLcAmt.divide(Amount100);
                                    String totalLcAmount = diff.format(totalLcAmount100);
                                    String finalval = String.valueOf(totalLcAmount);

                                    Loggers.general().info(LOG,"finalval totalLcAmount" + totalLcAmount );
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"finalval insurance amount<====1===>" + finalval + fullcur);

                                    }
                                    setINSPERAM(finalval + " " + fullcur);
                              } else if (divideByDecimal.equalsIgnoreCase("1000")) {

                                    DecimalFormat diff = new DecimalFormat("0.000");
                                    diff.setMaximumFractionDigits(3);
                                    BigDecimal totalLcAmount100 = totalLcAmt.divide(Amount100);
                                    String totalLcAmount = diff.format(totalLcAmount100);
                                    String finalval = String.valueOf(totalLcAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"finalval insurance amount<====3===>" + finalval + fullcur);

                                    }
                                    setINSPERAM(finalval + " " + fullcur);

                              }

                              else {

                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    BigDecimal totalLcAmount100 = totalLcAmt.divide(Amount100);
                                    String totalLcAmount = diff.format(totalLcAmount100);
                                    String finalval = String.valueOf(totalLcAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"finalval insurance amount<====2===>" + finalval + fullcur);

                                    }
                                    setINSPERAM(finalval + " " + fullcur);

                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,e.getMessage());
                        }

                        if (Sight_Payment.equalsIgnoreCase("Mixed Payment")) {

                              try {
                                    setINTPERCE("");
                                    setINTAMT("");
                                    String usanceAmt = getDriverWrapper().getEventFieldAsText("cADP", "v", "m");
                                    // Loggers.general().info(LOG,"usanceAmt initially" +
                                    // usanceAmt);
                                    if (usanceAmt != null || !usanceAmt.equalsIgnoreCase("")) {

                                          BigDecimal usanceAmount = new BigDecimal(usanceAmt);
                                          BigDecimal totalUsance = lcAmount.add(usanceAmount);
                                          String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);
                                          if (divideByDecimal.equalsIgnoreCase("1")) {
                                                DecimalFormat diff = new DecimalFormat("0");
                                                diff.setMaximumFractionDigits(1);
                                                String totalUsanceAmt = diff.format(totalUsance);
                                                // Loggers.general().info(LOG,"Final usanse amount====>
                                                // " +
                                                // totalUsanceAmt);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Final usanse amount==1==> " + totalUsanceAmt);
                                                }
                                                String finalUsanceAmt = String.valueOf(totalUsanceAmt);
                                                setTOTLOAM(finalUsanceAmt + " " + fullcur);
                                          } else if (divideByDecimal.equalsIgnoreCase("1000")) {

                                                DecimalFormat diff = new DecimalFormat("0.000");
                                                diff.setMaximumFractionDigits(3);
                                                String totalUsanceAmt = diff.format(totalUsance);
                                                // Loggers.general().info(LOG,"Final usanse amount====>
                                                // " +
                                                // totalUsanceAmt);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Final usanse amount==3==> " + totalUsanceAmt);
                                                }
                                                String finalUsanceAmt = String.valueOf(totalUsanceAmt);
                                                setTOTLOAM(finalUsanceAmt + " " + fullcur);

                                          } else {

                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);
                                                String totalUsanceAmt = diff.format(totalUsance);
                                                // Loggers.general().info(LOG,"Final usanse amount====>
                                                // " +
                                                // totalUsanceAmt);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Final usanse amount==2==> " + totalUsanceAmt);
                                                }
                                                String finalUsanceAmt = String.valueOf(totalUsanceAmt);
                                                setTOTLOAM(finalUsanceAmt + " " + fullcur);

                                          }
                                    } else {
                                          setTOTLOAM(fullamt + " " + fullcur);
                                    }
                              } catch (Exception e) {
                                    setTOTLOAM(fullamt + " " + fullcur);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception Usance amount====>" + e.getMessage());
                                    }
                              }
                        } else if (Sight_Payment.equalsIgnoreCase("Acceptance")) {
                              String inper = getINTPERCE();

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Initial interest percentage====>" + inper);
                              }
                              setUSMIXAMT("");
                              try {

                                    String tendays = getDriverWrapper().getEventFieldAsText("TNRD", "i", "");
                                    // Loggers.general().info(LOG,"tendays value check---->" +
                                    // tendays);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"tendays value check---->" + tendays);
                                    }

                                    BigDecimal tendayBig = new BigDecimal(1);
                                    try {

                                          tendayBig = new BigDecimal(tendays);

                                    } catch (Exception e) {
                                          tendayBig = new BigDecimal(1);
                                    }

                                    // Loggers.general().info(LOG,"tendays value BigDecimal---->" +
                                    // tendayBig);
                                    if ((!inper.equalsIgnoreCase("") && inper != null) && inper.length() > 0) {
                                          // Loggers.general().info(LOG,"Interest percentage
                                          // length----->"
                                          // + inper);
                                          BigDecimal inperBig = new BigDecimal(1);
                                          try {

                                                inperBig = new BigDecimal(inper);

                                          } catch (Exception e) {
                                                inperBig = new BigDecimal(1);
                                          }

                                          // Loggers.general().info(LOG,"tendays value length---->" +
                                          // tendayBig);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Bigdecimal interest percentage====>" + inperBig);
                                          }

                                          BigDecimal inperAndTenor = inperBig.multiply(tendayBig);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Bigdecimal interest percentage====>" + inperAndTenor);
                                          }

                                          BigDecimal lcAmt_Tenor = lcAmount.multiply(inperAndTenor);
                                          // Loggers.general().info(LOG,"LC tenor value---->" +
                                          // lcAmt_Tenor);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Total LC and tenor % value---->" + lcAmt_Tenor);
                                          }
                                          try {

                                                if (fullcur.equalsIgnoreCase("INR") || fullcur.equalsIgnoreCase("GBP")) {
                                                      // Loggers.general().info(LOG,"Total interest amount
                                                      // if-------->");
                                                      BigDecimal total_amt = lcAmt_Tenor.divide(Amount36500, RoundingMode.HALF_UP);
                                                      // Loggers.general().info(LOG,"Total interest
                                                      // amount--------> " + total_amt);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"Total interest amount INR and GBP--------> " + total_amt);
                                                      }
                                                      String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);
                                                      // Loggers.general().info(LOG,"Currency return fom
                                                      // method " + divideByDecimal);
                                                      if (divideByDecimal.equalsIgnoreCase("1")) {
                                                            DecimalFormat diff = new DecimalFormat("0");
                                                            diff.setMaximumFractionDigits(1);
                                                            String finallib = diff.format(total_amt);
                                                            // Loggers.general().info(LOG,"interest amount
                                                            // in
                                                            // finallib 1--------> " + finallib);
                                                            String finalval = String.valueOf(finallib);
                                                            setINTAMT(finalval + " " + fullcur);
                                                            String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                                            BigDecimal intamtBig = new BigDecimal(inamt);

                                                            //BigDecimal total_val = lcAmount.add(intamtBig); // commented for Jira 5332
                                                            BigDecimal total_val = lcAmount;

                                                            String finalValue = diff.format(total_val);
                                                            // Loggers.general().info(LOG,"Total Liability
                                                            // amount finalVal 1---->" + finalVal);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 1 finalVal---->" + finalValue);
                                                            }
                                                            setTOTLOAM(finalValue + " " + fullcur);
                                                      } else if (divideByDecimal.equalsIgnoreCase("1000")) {
                                                            DecimalFormat diff = new DecimalFormat("0.000");
                                                            diff.setMaximumFractionDigits(3);
                                                            String finallib = diff.format(total_amt);
                                                            // Loggers.general().info(LOG,"interest amount
                                                            // in
                                                            // finallib 3--------> " + finallib);
                                                            String finalval = String.valueOf(finallib);
                                                            setINTAMT(finalval + " " + fullcur);
                                                            String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                                            BigDecimal intamtBig = new BigDecimal(inamt);

                                                            //BigDecimal total_val = lcAmount.add(intamtBig);
                                                            BigDecimal total_val = lcAmount;

                                                            String finalValue = diff.format(total_val);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 1000 finalVal---->" + finalValue);
                                                            }

                                                            setTOTLOAM(finalValue + " " + fullcur);
                                                      } else {
                                                            DecimalFormat diff = new DecimalFormat("0.00");
                                                            diff.setMaximumFractionDigits(2);
                                                            String finallib = diff.format(total_amt);
                                                            // Loggers.general().info(LOG,"interest amount
                                                            // in
                                                            // finallib 2--------> " + finallib);

                                                            String finalval = String.valueOf(finallib);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 100 finalVal---->" + finalval);
                                                            }

                                                            setINTAMT(finalval + " " + fullcur);
                                                            String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                                            BigDecimal intamtBig = new BigDecimal(inamt);

                                                            //BigDecimal total_val = lcAmount.add(intamtBig);
                                                            BigDecimal total_val = lcAmount;

                                                            String finalValue = diff.format(total_val);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 100 finalVal---->" + finalValue);
                                                            }
                                                            setTOTLOAM(finalValue + " " + fullcur);
                                                      }
                                                } else {
                                                      BigDecimal total_amt = lcAmt_Tenor.divide(Amount36000, RoundingMode.HALF_UP);
                                                      // Loggers.general().info(LOG,"Total interest
                                                      // amount--------> " + total_amt);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"Total interest amount 2 digit--------> " + total_amt);
                                                      }
                                                      String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);
                                                      // Loggers.general().info(LOG,"Currency return fom
                                                      // method " + divideByDecimal);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,
                                                                        "Currency return fom method 2 digit currency" + divideByDecimal);
                                                      }
                                                      if (divideByDecimal.equalsIgnoreCase("1")) {
                                                            DecimalFormat diff = new DecimalFormat("0");
                                                            diff.setMaximumFractionDigits(1);
                                                            String finallib = diff.format(total_amt);
                                                            // Loggers.general().info(LOG,"interest amount
                                                            // in
                                                            // finallib 1--------> " + finallib);
                                                            String finalval = String.valueOf(finallib);

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 1 totalVal---->" + finalval);
                                                            }

                                                            setINTAMT(finalval + " " + fullcur);
                                                            String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                                            BigDecimal intamtBig = new BigDecimal(inamt);

                                                            //BigDecimal total_val = lcAmount.add(intamtBig);
                                                            BigDecimal total_val = lcAmount;

                                                            String finalValue = diff.format(total_val);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 1 finalValue---->" + finalValue);
                                                            }
                                                            setTOTLOAM(finalValue + " " + fullcur);
                                                      } else if (divideByDecimal.equalsIgnoreCase("1000")) {
                                                            DecimalFormat diff = new DecimalFormat("0.000");
                                                            diff.setMaximumFractionDigits(3);
                                                            String finallib = diff.format(total_amt);

                                                            String finalval = String.valueOf(finallib);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 1000 totalVal---->" + finalval);
                                                            }
                                                            setINTAMT(finalval + " " + fullcur);
                                                            String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                                            BigDecimal intamtBig = new BigDecimal(inamt);

                                                            //BigDecimal total_val = lcAmount.add(intamtBig);
                                                            BigDecimal total_val = lcAmount;

                                                            String finalValue = diff.format(total_val);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,
                                                                              "Total Liability amount 1000 finalValue---->" + finalValue);
                                                            }
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 3 finalVal ---->" + finalValue);
                                                            }

                                                            setTOTLOAM(finalValue + " " + fullcur);
                                                      } else {
                                                            DecimalFormat diff = new DecimalFormat("0.00");
                                                            diff.setMaximumFractionDigits(2);
                                                            String finallib = diff.format(total_amt);
                                                            // Loggers.general().info(LOG,"interest amount
                                                            // in
                                                            // finallib 2--------> " + finallib);
                                                            String finalval = String.valueOf(finallib);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 100 totalVal---->" + finalval);
                                                            }
                                                            setINTAMT(finalval + " " + fullcur);
                                                            String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                                            BigDecimal intamtBig = new BigDecimal(inamt);

                                                            //BigDecimal total_val = lcAmount.add(intamtBig);
                                                            BigDecimal total_val = lcAmount;

                                                            String finalValue = diff.format(total_val);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Total Liability amount 100 finalValue---->" + finalValue);
                                                            }
                                                            setTOTLOAM(finalValue + " " + fullcur);
                                                      }
                                                }

                                          } catch (Exception e) {
                                                Loggers.general().info(LOG,"Exception Interest percentage----->" + e.getMessage());
                                          }

                                    } else if ((inper.equalsIgnoreCase("") || inper == null) && inper.length() < 1
                                                && ((intamount != null || !intamount.equalsIgnoreCase("")) && intamount.length() > 0)) {

                                          String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);
                                          // Loggers.general().info(LOG,"Interest amount for float
                                          // charges" + intamount);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Interest amount for float charges" + intamount);
                                          }

                                          if (divideByDecimal.equalsIgnoreCase("1")) {
                                                DecimalFormat diff = new DecimalFormat("0");
                                                diff.setMaximumFractionDigits(1);
                                                String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");

                                                BigDecimal inperBig = new BigDecimal(1);
                                                try {

                                                      inperBig = new BigDecimal(inamt);

                                                } catch (Exception e) {
                                                      inperBig = new BigDecimal(1);
                                                }

                                                //BigDecimal total_val = lcAmount.add(inperBig);
                                                BigDecimal total_val = lcAmount;

                                                String finalVal = diff.format(total_val);
                                                // Loggers.general().info(LOG,"Total Liability amount
                                                // finalVal float charges 1--->" + finalVal);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Total Liability amount finalVal float charges 1--->" + finalVal);
                                                }
                                                setTOTLOAM(finalVal + " " + fullcur);
                                          } else if (divideByDecimal.equalsIgnoreCase("1000")) {
                                                DecimalFormat diff = new DecimalFormat("0.000");
                                                diff.setMaximumFractionDigits(3);

                                                String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                                BigDecimal inperBig = new BigDecimal(1);
                                                try {

                                                      inperBig = new BigDecimal(inamt);

                                                } catch (Exception e) {
                                                      inperBig = new BigDecimal(1);
                                                }
                                                //BigDecimal total_val = lcAmount.add(inperBig);
                                                BigDecimal total_val = lcAmount;

                                                String finalVal = diff.format(total_val);
                                                // Loggers.general().info(LOG,"Total Liability amount
                                                // finalVal float charges 3--->" + finalVal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Total Liability amount finalVal float charges 3--->" + finalVal);
                                                }

                                                setTOTLOAM(finalVal + " " + fullcur);
                                          } else {
                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);

                                                String inamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                                BigDecimal inperBig = new BigDecimal(1);
                                                try {

                                                      inperBig = new BigDecimal(inamt);

                                                } catch (Exception e) {
                                                      inperBig = new BigDecimal(1);
                                                }
                                                //BigDecimal total_val = lcAmount.add(inperBig);
                                                BigDecimal total_val = lcAmount;

                                                String finalVal = diff.format(total_val);
                                                // Loggers.general().info(LOG,"Total Liability amount
                                                // finalVal float charges 2--->" + finalVal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Total Liability amount finalVal float charges 2--->" + finalVal);
                                                }

                                                setTOTLOAM(finalVal + " " + fullcur);
                                          }

                                    }

                                    else {
                                          setTOTLOAM(fullamt + " " + fullcur);
                                    }
                              } catch (Exception e) {
                                    Loggers.general().info(LOG,"Exception Acceptance====>" + e.getMessage());
                              }
                        } else if (!Sight_Payment.equalsIgnoreCase("Mixed Payment")
                                    && !Sight_Payment.equalsIgnoreCase("Acceptance")) {

                              setUSMIXAMT("");
                              setINTPERCE("");
                              setINTAMT("");
                              setTOTLOAM(fullamt + " " + fullcur);
                        }
                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception for Total liability amount====>" + e.getMessage());
                  }
            }

            // -------------------------------------------------------//
            try {
                  if (Sight_Payment.equalsIgnoreCase("Acceptance")) {
                        try {

                              String maxAmt = getDriverWrapper().getEventFieldAsText("MAX", "v", "m");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," Initial full Liablity maxAmount" + maxAmt);
                              }
                              String maxCur = getDriverWrapper().getEventFieldAsText("FOA", "v", "c");
                              BigDecimal maxAmount = new BigDecimal(maxAmt);
                              BigDecimal addAmount = new BigDecimal(0);
                              BigDecimal intamount = new BigDecimal(0);

                              try {
                                    String addAmt = getDriverWrapper().getEventFieldAsText("ADA", "v", "m");
                                    addAmount = new BigDecimal(addAmt);

                              } catch (Exception e) {
                                    addAmount = new BigDecimal(0);

                              }

                              try {
                                    String intamt = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                                    intamount = new BigDecimal(intamt);

                              } catch (Exception e) {
                                    intamount = new BigDecimal(0);
                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Liablity maxAmount" + maxAmount + " addAmount" + addAmount + " intamount" + intamount);
                              }

                              BigDecimal maxAddAmount = addAmount.add(maxAmount);

                              //BigDecimal totAmount = maxAddAmount.add(intamount);
                              BigDecimal totAmount = maxAddAmount;

                              ConnectionMaster connectionMaster = new ConnectionMaster();
                              String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);
                              // Loggers.general().info(LOG,"Interest amount for float
                              // charges" + intamount);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total amount for liablity" + totAmount);
                              }

                              if (divideByDecimal.equalsIgnoreCase("1")) {
                                    DecimalFormat diff = new DecimalFormat("0");
                                    diff.setMaximumFractionDigits(1);

                                    String finalVal = diff.format(totAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Final amount for liablity 1 digit" + totAmount);
                                    }

                                    setFULTOAM(finalVal + " " + fullcur);
                              } else if (divideByDecimal.equalsIgnoreCase("1000")) {
                                    DecimalFormat diff = new DecimalFormat("0.000");
                                    diff.setMaximumFractionDigits(3);

                                    String finalVal = diff.format(totAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Final amount for liablity 3 digit" + totAmount);
                                    }

                                    setFULTOAM(finalVal + " " + fullcur);
                              } else {
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);

                                    String finalVal = diff.format(totAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Final amount for liablity 2 digit" + totAmount);
                                    }

                                    setFULTOAM(finalVal + " " + fullcur);
                              }

                              if (getMinorCode().equalsIgnoreCase("NAMI") || getMinorCode().equalsIgnoreCase("NADI")) {
                                    try {
                                          String liaAmt = getDriverWrapper().getEventFieldAsText("LIA", "v", "m");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Initial Total Liablity Amount " + liaAmt);
                                          }
                                          BigDecimal liaAmountInc = new BigDecimal(0);
                                          BigDecimal liaAmoutDec = new BigDecimal(0);
                                          BigDecimal liaTotal = new BigDecimal(0);
                                          try {
                                                //String liaAmtInc = getDriverWrapper().getEventFieldAsText("IAM", "v", "m");
                                                String liaAmtInc = getDriverWrapper().getEventFieldAsText("IML", "v", "m");
                                                liaAmountInc = new BigDecimal(liaAmtInc);
                                          } catch (Exception e) {
                                                liaAmountInc = new BigDecimal(0);
                                          }

                                          try {
                                          //    String liaAmtDec = getDriverWrapper().getEventFieldAsText("DAM", "v", "m");
                                                String liaAmtDec = getDriverWrapper().getEventFieldAsText("DML", "v", "m");
                                                liaAmoutDec = new BigDecimal(liaAmtDec);
                                          } catch (Exception e) {
                                                liaAmoutDec = new BigDecimal(0);
                                          }

                                          BigDecimal liaAmount = new BigDecimal(liaAmt);
                                          if (liaAmountInc.compareTo(BigDecimal.ZERO) > 0) {
                                                liaTotal = liaAmount.add(liaAmountInc);
                                          } else {
                                                liaTotal = liaAmount.subtract(liaAmoutDec);
                                          }

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Total Liablity Amount increase/dec" + liaTotal);
                                          }

                                          if (divideByDecimal.equalsIgnoreCase("1")) {
                                                DecimalFormat diff = new DecimalFormat("0");
                                                diff.setMaximumFractionDigits(1);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===1===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);
                                          } else if (divideByDecimal.equalsIgnoreCase("1000")) {

                                                DecimalFormat diff = new DecimalFormat("0.000");
                                                diff.setMaximumFractionDigits(3);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===3===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);

                                          } else {

                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===2===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);

                                          }
                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Exception Total Liablity Amount " + e.getMessage());
                                          }
                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception for Full liability amount acceptance====>" + e.getMessage());
                              }
                        }
                  } else if (Sight_Payment.equalsIgnoreCase("Mixed Payment")) {

                        try {

                              String maxAmt = getDriverWrapper().getEventFieldAsText("MAX", "v", "m");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," Initial Liablity maxAmount Mixed Payment" + maxAmt);
                              }
                              String maxCur = getDriverWrapper().getEventFieldAsText("FOA", "v", "c");
                              BigDecimal maxAmount = new BigDecimal(maxAmt);
                              BigDecimal addAmount = new BigDecimal(0);
                              BigDecimal intamount = new BigDecimal(0);

                              try {
                                    String addAmt = getDriverWrapper().getEventFieldAsText("ADA", "v", "m");
                                    addAmount = new BigDecimal(addAmt);

                              } catch (Exception e) {
                                    addAmount = new BigDecimal(0);

                              }

                              try {
                                    String intamt = getDriverWrapper().getEventFieldAsText("cADP", "v", "m");
                                    intamount = new BigDecimal(intamt);

                              } catch (Exception e) {
                                    intamount = new BigDecimal(0);
                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Liablity maxAmount Mixed Payment" + maxAmount + " addAmount" + addAmount
                                                + " intamount" + intamount);
                              }

                              BigDecimal maxAddAmount = addAmount.add(maxAmount);

                              
                              
                              //BigDecimal totAmount = maxAddAmount.add(intamount);
                              BigDecimal totAmount = maxAddAmount;

                              ConnectionMaster connectionMaster = new ConnectionMaster();
                              String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);
                              // Loggers.general().info(LOG,"Interest amount for float
                              // charges" + intamount);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total amount for liablity" + totAmount);
                              }

                              if (divideByDecimal.equalsIgnoreCase("1")) {
                                    DecimalFormat diff = new DecimalFormat("0");
                                    diff.setMaximumFractionDigits(1);

                                    String finalVal = diff.format(totAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Final amount for liablity 1 digit" + totAmount);
                                    }

                                    setFULTOAM(finalVal + " " + fullcur);
                              } else if (divideByDecimal.equalsIgnoreCase("1000")) {
                                    DecimalFormat diff = new DecimalFormat("0.000");
                                    diff.setMaximumFractionDigits(3);

                                    String finalVal = diff.format(totAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Final amount for liablity 3 digit" + totAmount);
                                    }

                                    setFULTOAM(finalVal + " " + fullcur);
                              } else {
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);

                                    String finalVal = diff.format(totAmount);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Final amount for liablity 2 digit" + totAmount);
                                    }

                                    setFULTOAM(finalVal + " " + fullcur);
                              }

                              if (getMinorCode().equalsIgnoreCase("NAMI") || getMinorCode().equalsIgnoreCase("NADI")) {
                                    try {
                                          String liaAmt = getDriverWrapper().getEventFieldAsText("LIA", "v", "m");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Initial Total Liablity Amount " + liaAmt);
                                          }
                                          BigDecimal liaAmountInc = new BigDecimal(0);
                                          BigDecimal liaAmoutDec = new BigDecimal(0);
                                          BigDecimal liaTotal = new BigDecimal(0);
                                          try {
                                          //    String liaAmtInc = getDriverWrapper().getEventFieldAsText("IAM", "v", "m");
                                                String liaAmtInc = getDriverWrapper().getEventFieldAsText("IML", "v", "m");
                                                liaAmountInc = new BigDecimal(liaAmtInc);
                                          } catch (Exception e) {
                                                liaAmountInc = new BigDecimal(0);
                                          }

                                          try {
                                          //    String liaAmtDec = getDriverWrapper().getEventFieldAsText("DAM", "v", "m");
                                                String liaAmtDec = getDriverWrapper().getEventFieldAsText("DML", "v", "m");
                                                liaAmoutDec = new BigDecimal(liaAmtDec);
                                          } catch (Exception e) {
                                                liaAmoutDec = new BigDecimal(0);
                                          }

                                          BigDecimal liaAmount = new BigDecimal(liaAmt);
                                          if (liaAmountInc.compareTo(BigDecimal.ZERO) > 0) {
                                                liaTotal = liaAmount.add(liaAmountInc);
                                          } else {
                                                liaTotal = liaAmount.subtract(liaAmoutDec);
                                          }

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Total Liablity Amount increase/dec" + liaTotal);
                                          }

                                          if (divideByDecimal.equalsIgnoreCase("1")) {
                                                DecimalFormat diff = new DecimalFormat("0");
                                                diff.setMaximumFractionDigits(1);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===1===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);
                                          } else if (divideByDecimal.equalsIgnoreCase("1000")) {

                                                DecimalFormat diff = new DecimalFormat("0.000");
                                                diff.setMaximumFractionDigits(2);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===3===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);

                                          } else {

                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===2===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);

                                          }
                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Exception Total Liablity Amount " + e.getMessage());
                                          }
                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception for Full liability amount Mixed Payment====>" + e.getMessage());
                              }
                        }

                  } else {

                        try {

                              String maxAmt = getDriverWrapper().getEventFieldAsText("MAX", "v", "m");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," Initial full Liablity maxAmount  others" + maxAmt);
                              }
                              String maxCur = getDriverWrapper().getEventFieldAsText("FOA", "v", "c");
                              BigDecimal maxAmount = new BigDecimal(maxAmt);
                              BigDecimal addAmount = new BigDecimal(0);

                              try {
                                    String addAmt = getDriverWrapper().getEventFieldAsText("ADA", "v", "m");
                                    addAmount = new BigDecimal(addAmt);

                              } catch (Exception e) {
                                    addAmount = new BigDecimal(0);

                              }

                              BigDecimal maxAddAmount = addAmount.add(maxAmount);

                              // DecimalFormat diff = new DecimalFormat("0.00");
                              // diff.setMaximumFractionDigits(2);
                              //
                              // String finalVal = diff.format(maxAddAmount);
                              //
                              // if (dailyval_Log.equalsIgnoreCase("YES")) {
                              // Loggers.general().info(LOG,"Final amount for liablity 2 digit" +
                              // finalVal);
                              // }
                              //
                              // setFULTOAM(finalVal + " " + maxCur);

                              ConnectionMaster connectionMaster = new ConnectionMaster();
                              String divideByDecimal = connectionMaster.getDecimalforCur(fullcur);

                              if (divideByDecimal.equalsIgnoreCase("1")) {
                                    DecimalFormat difVal = new DecimalFormat("0");
                                    difVal.setMaximumFractionDigits(1);

                                    String finalVal = difVal.format(maxAddAmount);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Amend Total Liablity Amount final<===1===>" + finalVal);
                                    }
                                    setFULTOAM(finalVal + " " + maxCur);
                              } else if (divideByDecimal.equalsIgnoreCase("1000")) {

                                    DecimalFormat difVal = new DecimalFormat("0.000");
                                    difVal.setMaximumFractionDigits(3);

                                    String finalVal = difVal.format(maxAddAmount);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Amend Total Liablity Amount final<===3===>" + finalVal);
                                    }
                                    setFULTOAM(finalVal + " " + maxCur);

                              } else {

                                    DecimalFormat difVal = new DecimalFormat("0.00");
                                    difVal.setMaximumFractionDigits(2);

                                    String finalVal = difVal.format(maxAddAmount);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Amend Total Liablity Amount final<===2===>" + finalVal);
                                    }
                                    setFULTOAM(finalVal + " " + maxCur);

                              }

                              if (getMinorCode().equalsIgnoreCase("NAMI") || getMinorCode().equalsIgnoreCase("NADI")) {
                                    try {
                                          String liaAmt = getDriverWrapper().getEventFieldAsText("LIA", "v", "m");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Initial Total Liablity Amount " + liaAmt);
                                          }
                                          BigDecimal liaAmountInc = new BigDecimal(0);
                                          BigDecimal liaAmoutDec = new BigDecimal(0);
                                          BigDecimal liaTotal = new BigDecimal(0);
                                          try {
                                                //String liaAmtInc = getDriverWrapper().getEventFieldAsText("IAM", "v", "m");
                                                String liaAmtInc = getDriverWrapper().getEventFieldAsText("IML", "v", "m");
                                                liaAmountInc = new BigDecimal(liaAmtInc);
                                          } catch (Exception e) {
                                                liaAmountInc = new BigDecimal(0);
                                          }

                                          try {
                                          //    String liaAmtDec = getDriverWrapper().getEventFieldAsText("DAM", "v", "m");
                                                String liaAmtDec = getDriverWrapper().getEventFieldAsText("DML", "v", "m");
                                                liaAmoutDec = new BigDecimal(liaAmtDec);
                                          } catch (Exception e) {
                                                liaAmoutDec = new BigDecimal(0);
                                          }

                                          BigDecimal liaAmount = new BigDecimal(liaAmt);
                                          if (liaAmountInc.compareTo(BigDecimal.ZERO) > 0) {
                                                liaTotal = liaAmount.add(liaAmountInc);
                                          } else {
                                                liaTotal = liaAmount.subtract(liaAmoutDec);
                                          }

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Total Liablity Amount increase/dec" + liaTotal);
                                          }

                                          if (divideByDecimal.equalsIgnoreCase("1")) {
                                                DecimalFormat diff = new DecimalFormat("0");
                                                diff.setMaximumFractionDigits(1);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===1===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);
                                          } else if (divideByDecimal.equalsIgnoreCase("1000")) {

                                                DecimalFormat diff = new DecimalFormat("0.000");
                                                diff.setMaximumFractionDigits(3);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===3===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);

                                          } else {

                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);

                                                String liablityAmt = diff.format(liaTotal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Amend Total Liablity Amount final<===2===>" + liablityAmt);
                                                }
                                                setTOTLOAM(liablityAmt + " " + maxCur);

                                          }
                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Exception Total Liablity Amount " + e.getMessage());
                                          }
                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Final amount for liablity others" + e.getMessage());
                              }

                        }

                  }
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception for Full liability amount====>" + e.getMessage());
                  }
            }
      }

//    public void onILCSETTLEBUYERILCSETTclayButton() {
//
//          String strLog = "Log";
//          String dailyval_Log = "";
//          @SuppressWarnings("unchecked")
//          AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
//                      .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
//          EXTGENCUSTPROP CodeLog = queryLog.getUnique();
//          if (CodeLog != null) {
//
//                dailyval_Log = CodeLog.getPropval();
//          } else {
//                // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
//
//          }
//          String mas = getPREBUYRE().trim();
//          int dmT = 0;
//          try {
//                // Loggers.general().info(LOG,"enter into try for Buyer credit");
//
//                String dms = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS PREBUYRE FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND prod.CODE ='BCR' AND mas.MASTER_REF ='"
//                            + mas + "'";
//                // Loggers.general().info(LOG,"Master ref no valid " + dms);
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG,"Master ref no valid " + dms);
//                }
//
//                Connection con = ConnectionMaster.getConnection();
//                PreparedStatement ps = con.prepareStatement(dms);
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                      dmT = rs.getInt(1);
//                      // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
//                      if (dmT > 0) {
//                            try {
//
//                                  String query_dms = "SELECT TRIM(mas.NPRNAME_L1),TRIM(mas.AMOUNT),mas.CCY,TO_CHAR(ext.CLIMEXPD,'DD/MM/YY') AS CLIMEXPD,ext.INTAMT,ext.CCY_3,ext.TENDAYS,ext.FUNDBAN FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('ISS','TIG') AND mas. MASTER_REF='"
//                                              + mas + "'";
//                                  // Loggers.general().info(LOG,"Values fetching Buyer credit
//                                  // value in ILC sett " + query_dms);
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Values fetching Buyer credit value in ILC sett " + query_dms);
//                                  }
//
//                                  con = ConnectionMaster.getConnection();
//                                  ps = con.prepareStatement(query_dms);
//                                  rs = ps.executeQuery();
//                                  while (rs.next()) {
//                                        String advise = rs.getString(1);
//                                        String amt = rs.getString(2);
//                                        // Loggers.general().info(LOG,"values fetching BUYER credit
//                                        // amount " + amt);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"values fetching BUYER credit amount " + amt);
//                                        }
//                                        BigDecimal hundred = new BigDecimal(100);
//                                        BigDecimal amt_val = new BigDecimal(amt);
//                                        BigDecimal amt_big = amt_val.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
//
//                                        // Loggers.general().info(LOG,"values fetching BUYER credit
//                                        // amount amt_big" + amt_big);
//                                        String ccy = rs.getString(3);
//                                        String clmate = rs.getString(4);
//                                        String int_amt = rs.getString(5);
//
//                                        String int_ccy = rs.getString(6);
//                                        String tenday = rs.getString(7);
//                                        String fundchrg = rs.getString(8);
//
//                                        setFUNDBAN(advise);
//                                        setCREAMT(amt_big + " " + ccy);
//                                        // Loggers.general().info(LOG,"fetching values BUYER credit
//                                        // after set" + getCREAMT());
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"fetching values BUYER credit after set" + getCREAMT());
//                                        }
//                                        setDUEDATE(clmate);
//                                        if (int_amt.length() > 0) {
//                                              BigDecimal int_val = new BigDecimal(int_amt);
//                                              BigDecimal int_big = int_val.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
//                                              setINTAMT(int_big + " " + int_ccy);
//                                        }
//                                        setTENO(tenday);
//                                        if (fundchrg.length() > 0) {
//                                              // Loggers.general().info(LOG,"Funding bank charges" +
//                                              // fundchrg);
//                                              String fundchrg_val = fundchrg.replaceAll("[^0-9]", "");
//                                              // Loggers.general().info(LOG,"Funding bank charges
//                                              // amount only" + fundchrg_val);
//                                              BigDecimal fundchrg_big = new BigDecimal(fundchrg_val);
//                                              // Loggers.general().info(LOG,"Funding bank charges
//                                              // amount only fundchrg_big" + fundchrg_big);
//                                              BigDecimal fundchrg_total = fundchrg_big.multiply(hundred);
//                                              String luCur = fundchrg.replaceAll("[^A-Za-z]+", "");
//                                              // Loggers.general().info(LOG,"Funding bank charges
//                                              // currency" + luCur);
//                                              setCHABUY(fundchrg_total + " " + luCur);
//                                        }
//
//                                  }
//
//                            } catch (Exception e) {
//                                  // Loggers.general().info(LOG,"Exception fetching BUYERVALILC" +
//                                  // e.getMessage());
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Exception fetching BUYERVALILC" + e.getMessage());
//                                  }
//                            }
//
//                      } else {
//                            Loggers.general().info(LOG,"master no is not valide ILC --->" + dmT);
//                      }
//
//                }
//
//          } catch (Exception e) {
//                // Loggers.general().info(LOG,"Master IDC no not valid count 0--->" +
//                // e.getMessage());
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG,"Master IDC no not valid count 0--->" + e.getMessage());
//                }
//          }
//
//          finally {
//                try {
//                      if (rs != null)
//                            rs.close();
//                      if (ps != null)
//                            ps.close();
//                      if (con != null)
//                            con.close();
//                } catch (SQLException e) {
//                      // Loggers.general().info(LOG,"Connection Failed! Check output
//                      // console");
//                      e.printStackTrace();
//                }
//          }
//
//    }

      // public void onSERVICEIMPSTDLCISSclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEIMPSTDLCCLRECclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEIMPSTDLCAMDclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEIMPSTDLCADJclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEIMPSTALCOUTCLAclayButton() {
      // }

      // public void onSERVICEIMPSTDLCREPclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEILCCANButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEIMPLCCLAIMRECclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEILCEXPButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEILCISSTAKEclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEIMPLCclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEILCMAINLayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEIMPADVclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEILCAMDclayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICEILCSETTclayButton() {
      // }

      // public boolean SERVICFECTH() {
      // boolean value = false;
      // getExtEventServiceTaxNew().setEnabled(false);
      // getExtEventServiceTaxDelete().setEnabled(false);
      // getExtEventServiceTaxUpdate().setEnabled(false);
      // getExtEventServiceTaxUp().setEnabled(false);
      // getExtEventServiceTaxDown().setEnabled(false);
      // getExtEventServiceTaxView().setEnabled(false);
      // String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
      // "r", "");
      //
      // String eventPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s",
      // "");
      // String eventPrefixSerialNo =
      // getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
      // Integer eventPrefixNo = Integer.parseInt(eventPrefixSerialNo);
      // EnigmaArray<ExtEventServiceTaxEntityWrapper> liste =
      // getExtEventServiceTaxData();
      // int count = 0;
      // Iterator<ExtEventServiceTaxEntityWrapper> iterator = liste.iterator();
      // // for (int i = 0; i < seivce.size(); i++) {
      // // ExtEventServiceTax serviceTax = seivce.get(i);
      // // }
      //
      // if (liste.getSize() < 1) {
      // try {
      // String query = "SELECT
      // TRIM(DESCR),TRIM(CHARGE_AMT),TRIM(SERVICE_TAX),TRIM(EDU_CESS),TRIM(KRISHI_CESS)
      // FROM ETTV_SERVICETAX_SWACH_CALC WHERE REFNO_PFIX='"
      // + eventPrefix + "' AND REFNO_SERL=" + eventPrefixNo + " AND MASTER_REF='"
      // + masterRefNumber
      // + "'";
      //
      // // Loggers.general().info(LOG,"Service tax query - " + query);
      // con = ConnectionMaster.getConnection();
      // PreparedStatement ps1 = con.prepareStatement(query);
      // ResultSet rs1 = ps1.executeQuery();
      //
      // while (rs1.next()) {
      // Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
      // ExtEventServiceTax serviceTax = new ExtEventServiceTax();
      // serviceTax.setColumn("DESCR", rs1.getString(1));
      // serviceTax.setColumn("CHRGAMT", rs1.getString(2) + " " + "INR");
      // serviceTax.setColumn("SERVTAX", rs1.getString(3) + " " + "INR");
      // serviceTax.setColumn("EDUCES", rs1.getString(4) + " " + "INR");
      // serviceTax.setColumn("KRISH", rs1.getString(5) + " " + "INR");
      // serviceTax.setNewKey();
      // serviceTax.setFk(fkey);
      // serviceTax.setSequence(count);
      //
      // ExtEventServiceTaxEntityWrapper projectdetchk = new
      // ExtEventServiceTaxEntityWrapper(serviceTax,
      // getDriverWrapper());
      // addNewExtEventServiceTax(projectdetchk);
      //
      // count++;
      //
      // getExtEventServiceTaxNew().setEnabled(false);
      // getExtEventServiceTaxDelete().setEnabled(false);
      // getExtEventServiceTaxUpdate().setEnabled(false);
      // getExtEventServiceTaxUp().setEnabled(false);
      // getExtEventServiceTaxDown().setEnabled(false);
      // getExtEventServiceTaxView().setEnabled(false);
      // }
      //
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"Exeception of service tax- " +
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
      //
      // } else {
      // // Loggers.general().info(LOG,"Service tax grid value greater then 1 ");
      // }
      // return value;
      // }

      // public void onCHECKLISTIMPLCclayButton() {
      //
      // try {
      // String subcode = "0";
      // getExtEventChecklistNew().setEnabled(false);
      // getExtEventChecklistDelete().setEnabled(false);
      // //Loggers.general().info(LOG,"Enter into try Import gur after code change----->
      // ");
      // EnigmaArray<ExtEventChecklistEntityWrapper> liste =
      // getExtEventChecklistData();
      // int count = 0;
      // Iterator<ExtEventChecklistEntityWrapper> iterator = liste.iterator();
      // String prodCode = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
      // //Loggers.general().info(LOG,"prodCode for chacklist------------>" + prodCode);
      // String eventCod = getDriverWrapper().getEventFieldAsText("EVCD", "s",
      // "");
      // //Loggers.general().info(LOG,"eventCod for chacklist------------>" + eventCod);
      // subcode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
      // if (subcode.equalsIgnoreCase("")) {
      // subcode = "0";
      // }
      // //Loggers.general().info(LOG,"SUB_PRODUCT_CODE for chacklist------------>" +
      // subcode);
      // String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
      // //Loggers.general().info(LOG,"prodCode for chacklist------------>" + prodCode);
      //
      // String query = "select TRIM(CHECKLIST_DESCR),TRIM(SEQ_NO) from
      // ETT_WF_CHKLST_MST WHERE TRIM(PROD_CODE) ='"
      // + prodCode + "' AND TRIM(EVENT_CODE)='" + eventCod + "' AND
      // TRIM(SUB_PRODUCT_CODE) ='" + subcode
      // + "' AND TRIM(INIT_AT)='" + stepID + "'";
      // //Loggers.general().info(LOG,"Query for checklist------------>" + query);
      // con1 = ConnectionMaster.getConnection();
      // psd = con1.prepareStatement(query);
      // rst = psd.executeQuery();
      // //Loggers.general().info(LOG,"executeQuery statement ");
      // while (rst.next()) {
      // //Loggers.general().info(LOG,"Enter into while");
      // Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
      // ExtEventChecklist checkdet = new ExtEventChecklist();
      // //Loggers.general().info(LOG,"liste size -----> " + fkey);
      // checkdet.setColumn("CHEKCOL", rst.getString(1));
      // checkdet.setColumn("CSMSNO", rst.getString(2));
      // checkdet.setNewKey();
      // checkdet.setFk(fkey);
      // checkdet.setSequence(count);
      //
      // ExtEventChecklistEntityWrapper projectdetchk = new
      // ExtEventChecklistEntityWrapper(checkdet,
      // getDriverWrapper());
      // addNewExtEventChecklist(projectdetchk);
      //
      // count++;
      // }
      // rst.close();
      // psd.close();
      // con1.close();
      // if (liste.getSize().intValue() > 0) {
      // //Loggers.general().info(LOG,"liste size -----> " + liste.getSize());
      // getBtnCHECKLISTIMPLCclay().setEnabled(false);
      // getExtEventChecklistNew().setEnabled(false);
      // getExtEventChecklistDelete().setEnabled(false);
      // getExtEventChecklistUp().setEnabled(false);
      // getExtEventChecklistDown().setEnabled(false);
      // getExtEventChecklistView().setEnabled(false);
      //
      // }
      // } catch (Exception e) {
      // //Loggers.general().info(LOG,"catch result" + e.getMessage());
      //
      // }
      // }

      // @Override
      // protected void loadExtEventChecklistViewPane(ExtensionViewPaneMode mode,
      // ExtEventChecklistEntityWrapper item) {
      // // TODO Auto-generated method stub
      // super.loadExtEventChecklistViewPane(mode, item);
      // //Loggers.general().info(LOG,"loadExtEventChecklistViewPane ----> " + item);
      // //Loggers.general().info(LOG,"Mode ----> " + mode);
      // ExtensionViewPaneMode modeval = mode;
      // //Loggers.general().info(LOG,"modeval.name() - " + modeval.name());
      // if (modeval.name().equalsIgnoreCase("UPDATE")) {
      // //Loggers.general().info(LOG,"Mode UPDATE----> ");
      // getExtEventChecklistDelete().setEnabled(false);
      // } else {
      // //Loggers.general().info(LOG,"modeval.name() in esle ----> " + modeval.name());
      // }
      // }

      public boolean MarginCAL() {
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

                  double notionalRate = 0.0;
                  double permaradub = 0.0;
                  String facid = getFACLTYID().trim();
                  String permar = getPMARGIN();
                  String permaradd = getPADDMARG();
                  if (!facid.equalsIgnoreCase("")) {
                        if (permaradd == null || permaradd.isEmpty()) {
                              permaradd = "0";
                        }
                        permaradub = Double.valueOf(permaradd);
                        // String magn = "";
                        int magne = 0;
                        String ttRateString = "";
                        // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                        String mastamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
                        // Loggers.general().info(LOG,"Master and tolerant amount--->" +
                        // mastamt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Master and tolerant amount--->" + mastamt);
                        }
                        if (mastamt.isEmpty() || mastamt == null) {
                              // Loggers.general().info(LOG,"Master and tolerant amount is
                              // empty--->" + mastamt);
                              mastamt = "0";
                        }
                        float amunt1 = Float.valueOf(mastamt);
                        // Loggers.general().info(LOG,"amount" + amunt1);
                        long amount01 = (long) amunt1;
                        // Loggers.general().info(LOG,"current Value ----" + amount01);
                        String curr = getDriverWrapper().getEventFieldAsText("AMT", "v", "c").trim();
                        String customerNo = getDriverWrapper().getEventFieldAsText("APP", "p", "no");

                        // Loggers.general().info(LOG,"Currency " + curr);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Currency " + curr);
                        }

                        con = ConnectionMaster.getConnection();
                        if (permar.equalsIgnoreCase("") || permar == null) {
                              String queryfac = "select margin from customermargin where facility='" + facid + "' and CIF ='"
                                          + customerNo + "'"; // Loggers.general().info(LOG,"queryfac
                                                                        // " + queryfac);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"queryfac" + queryfac);
                              }

                              dmsp = con.prepareStatement(queryfac);
                              // Loggers.general().info(LOG,"dmsp-----> ");
                              dmsr = dmsp.executeQuery();
                              // Loggers.general().info(LOG,"executeQuery-----> ");
                              Boolean facilityFound = false;
                              while (dmsr.next()) {
                                    // Loggers.general().info(LOG,"while margin-----> ");
                                    facilityFound = true;
                                    // Loggers.general().info(LOG,"magn--->");
                                    String magn = dmsr.getString(1).trim();
                                    // Loggers.general().info(LOG,"magn--->" + magn);
                                    magne = Integer.parseInt(magn);
                                    // Loggers.general().info(LOG,"Margin " + magn);
                                    // Loggers.general().info(LOG,"Adding amount---- " + (magne +
                                    // permaradub));
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Margin " + magn);
                                          Loggers.general().info(LOG,"Adding amount---- " + (magne + permaradub));
                                    }

                                    if (curr.equalsIgnoreCase("INR")) {
                                          notionalRate = 1;
                                    } else {
                                          String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                          // Loggers.general().info(LOG,"ttRateQuery - " +
                                          // ttRateQuery);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"ttRateQuery - " + ttRateQuery);
                                          }

                                          ttRatePS = con.prepareStatement(ttRateQuery);
                                          ttRateRS = ttRatePS.executeQuery();
                                          while (ttRateRS.next()) {
                                                String ttRate_String = ttRateRS.getString(1).trim();
                                                notionalRate = Double.parseDouble(ttRate_String);
                                          }
                                    }
                                    // Loggers.general().info(LOG,"notionalRate - " + notionalRate);
                                    long k = (long) (((amount01 * (magne + permaradub))) * notionalRate);
                                    String ks = String.valueOf(k);
                                    // Loggers.general().info(LOG,"Margin amount in final for set if
                                    // stmt " + ks + "" + "INR");
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Margin amount in final for set if stmt " + ks + "" + "INR");
                                    }
                                    setPMARGIN(magn);
                                    // setNOTRATE(String.valueOf(notionalRate));
                                    setMARAMT(ks + " " + "INR");
                                    //// setPRMARAMT(ks + " " + "INR");
                              }
                              if (facilityFound == false) {
                                    setPMARGIN("");
                                    // setNOTRATE("");
                                    setMARAMT("");
                                    // setPRMARAMT("");
                              }
                        } else {
                              permar = getPMARGIN();
                              // Loggers.general().info(LOG,"permar value in string else--->" +
                              // permar);
                              float permarflo = Float.valueOf(permar);
                              long magndou = (long) permarflo;
                              // Loggers.general().info(LOG,"magndou value in long else--->" +
                              // magndou);
                              // Loggers.general().info(LOG,"float value in else--->" + magndou);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"magndou value in long else--->" + magndou);
                                    Loggers.general().info(LOG,"float value in else--->" + magndou);
                              }
                              if (curr.equalsIgnoreCase("INR")) {
                                    notionalRate = 1;
                              } else {
                                    // Loggers.general().info(LOG,"margin lc in else");
                                    String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                    // Loggers.general().info(LOG,"ttRateQuery else - " +
                                    // ttRateQuery);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"ttRateQuery else - " + ttRateQuery);
                                    }
                                    ttRatePS = con.prepareStatement(ttRateQuery);
                                    ttRateRS = ttRatePS.executeQuery();
                                    while (ttRateRS.next()) {
                                          String ttRate_String = ttRateRS.getString(1).trim();
                                          notionalRate = Double.parseDouble(ttRate_String);
                                    }
                              }
                              // Loggers.general().info(LOG,"Adding amount in else---- " +
                              // (magndou + permaradub));
                              long k = (long) (((amount01 * (magndou + permaradub))) * notionalRate);
                              // Loggers.general().info(LOG,"Expected margin amount in else---> -
                              // " + k);
                              String ks = String.valueOf(k);
                              // Loggers.general().info(LOG,"Margin amount in final for set else
                              // stmt " + ks + "" + "INR");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Margin amount in final for set else stmt " + ks + "" + "INR");
                              }
                              setPMARGIN(permar);
                              // setNOTRATE(String.valueOf(notionalRate));
                              setMARAMT(ks + " " + "INR");
                              //// setPRMARAMT(ks + " " + "INR");
                        }
                  } else {

                        // Loggers.general().info(LOG,"Facility id is blank");
                        setPMARGIN("");
                        // setNOTRATE("");
                        setMARAMT("");
                        setPADDMARG("");
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"exception caught on margin button validation
                  // " + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"exception caught on margin button validation " + e.getMessage());
                  }
            } finally {
                  try {
                        if (ttRateRS != null)
                              ttRateRS.close();
                        if (ttRatePS != null)
                              ttRatePS.close();
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

            return value;

      }

      // Margin validations
      public void oncalcmarginIMPLCclayButton() {
            MarginCAL();
      }

      @Override
      public void oncalcmarginIMPADVclayButton() {
            MarginCAL();
      }

      @Override
      public void oncalcmarginILCAMDclayButton() {
            MarginCAL();
      }

      @Override
      public void oncalcmarginILCSETTclayButton() {
            MarginCAL();

      }

      @Override
      public void oncalcmarginIMPLCCLAIMRECclayButton() {
            MarginCAL();

      }

      public void oncalcmarginIMPSTDLCISSclayButton() {
            MarginCAL();

      }

      public void oncalcmarginIMPSTDLCCLRECclayButton() {
            MarginCAL();

      }

      public void oncalcmarginIMPSTDLCAMDclayButton() {
            MarginCAL();

      }

      public void oncalcmarginIMPSTDLCADJclayButton() {
            MarginCAL();

      }

      public void oncalcmarginIMPSTALCOUTCLAclayButton() {
            MarginCAL();

      }

      // lien marking for import standby lc

      // implc standby issue
      public void onfetchlienIMPSTDLCISSclayButton() {
            lienMark();
      }

      // implc standby amend

      public void onfetchlienIMPSTDLCAMDclayButton() {
            lienMark();
      }

      public void onfetchlienIMPSTDLCADJclayButton() {
            lienMark();
      }

      public void onGURANTEEIMPSTDLCISSclayButton() {
            counterGuarantee();
      }

      public void onGURANTEEIMPSTDLCAMDclayButton() {
            counterGuarantee();
      }

      public void onGURANTEEIMPSTDLCADJclayButton() {
            counterGuarantee();

      }

      public boolean counterGuarantee() {
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

            BigDecimal Amount100 = new BigDecimal("100");
            BigDecimal Amount1000 = new BigDecimal("1000");
            BigDecimal eligibleVal = new BigDecimal("0");
            String eligibleValule = "";
            String mamt = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            try {

                  // Getting the Table Structure and Size
                  EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list = getExtEventCounterGuranteeData();
                  for (int i = 0; i < list.getSize().intValue(); i++) {
                        Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 = list.iterator();
                        // Getting the table Values
                        while (iterator1.hasNext()) {
                              ExtEventCounterGuranteeEntityWrapper fdwrapper = (ExtEventCounterGuranteeEntityWrapper) iterator1
                                          .next();
                              con = ConnectionMaster.getConnection();
                              String guaranteeNumber = fdwrapper.getGURNO().trim();
                              String query = "select trim(mas.amount), mas.ccy , to_char( mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
                                          + guaranteeNumber + "'";
                              // Loggers.general().info(LOG,"Master amount in query " + query);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Counter guarantee Master amount in query " + query);
                              }
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              if (rs.next()) {
                                    String eligible_amt = rs.getString(1);
                                    BigDecimal eligible_Amount = new BigDecimal(eligible_amt);
                                    String getced = "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'";
                                    ps = con.prepareStatement(getced);
                                    // Loggers.general().info(LOG,"sql" + getced);
                                    rs = ps.executeQuery();
                                    int a = 0;
                                    while (rs.next()) {
                                          a = rs.getInt(1);
                                    }
                                    if (a == 2) {

                                          eligibleVal = eligible_Amount.divide(Amount100);
                                          DecimalFormat diff = new DecimalFormat("0.00");
                                          diff.setMaximumFractionDigits(2);
                                          eligibleValule = diff.format(eligibleVal);
                                    } else if (a == 3) {
                                          DecimalFormat diff = new DecimalFormat("0.000");
                                          diff.setMaximumFractionDigits(3);
                                          eligibleVal = eligible_Amount.divide(Amount1000);
                                          eligibleValule = diff.format(eligibleVal);
                                    } else {
                                          DecimalFormat diff = new DecimalFormat("0");
                                          diff.setMaximumFractionDigits(0);
                                          eligibleValule = diff.format(eligibleVal);
                                    }
                                    // Loggers.general().info(LOG,"Total eligible_amt--->" +
                                    // eligible_amt);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Total eligible_amt--->" + eligibleVal);

                                    }

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Final counter value--->" + eligibleValule + "" + rs.getString(2));

                                    }
                                    fdwrapper.setELIAMT(eligibleValule + " " + rs.getString(2));

                                    fdwrapper.setEXDATE(rs.getString(3));

                              }

                        }
                  }
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in Counter guarantee Master amount " + e.getMessage());
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
                        //// Loggers.general().info(LOG,"Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }
            return true;

      }
      //Button to fetch rtgs details added by Vishal G
      public void onBENIFSCILCSETTclayButton() {

            // Loggers.general().info(LOG,"ImpLc button for POD");
            String benname="";
            String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
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
      //    setBENNAME_Name(benname);
            try {
                  String query="SELECT pos.BO_ACC_NO, POS.ORIGAMOUNT/100,POS.ORIGAMTCCY "+
                              "FROM  MASTER MAS,BASEEVENT BEV,RELITEM REL ,POSTING POS "+
                              "WHERE MAS.KEY97=BEV.MASTER_KEY   AND BEV.KEY97=REL.EVENT_KEY "+
                              " AND REL.KEY97=POS.KEY97"+
                              " AND MAS.MASTER_REF='"+masRefNo+"' "+
                              " AND (BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))='"+eventRefNo+"' "+
                              " and (BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))=pos.eventref "+
                              " AND POS.DR_CR_FLG='C' "+
                              " AND POS.ACC_TYPE='OA' "+
                              " AND POS.SP_CODE='SP734' ";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                  String dracct=rs.getString(1).trim();
                  String amount=rs.getString(2).trim();
                  String ccy=rs.getString(3).trim();
                  System.out.println("RTGS NEFT QUERY "+query+" "+amount);
                  setDRINTACC(dracct);
                  setRTGSNEFT(amount+" "+ccy);
            }
            String rtgsType=getPROREMT();
            if (rtgsType.trim().equalsIgnoreCase("RTG"))  {
                    
                                    setCRPOOLAC("504505120004000");
                                    
                              }
                              
                              if (rtgsType.trim().equalsIgnoreCase("NEF"))  {
                                    
                              //    String      debtacc="1980050000";
                                    setCRPOOLAC("473802480015000");
                                    
                              }
            setSENDBNCD("026");
            String proceed=getRTGNFT();
            if(proceed.equalsIgnoreCase("B2B")) {
             benname = getDriverWrapper().getEventFieldAsText("PRB", "p", "f");
             setBENNAME_Name(benname);
             }
            if(proceed.equalsIgnoreCase("B2C")) {
                   benname = getDriverWrapper().getEventFieldAsText("SEL", "p", "f");
                   setBENNAME_Name(benname);
                   }
            
            }catch (Exception e) {
                  e.printStackTrace();
            } finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        //// Loggers.general().info(LOG,"Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }
            }

      public void onfetchlienIMPLCclayButton() {
            lienMark();
      }

      public void onfetchlienILCAMDclayButton() {
            lienMark();
      }
      
      

      public void onREVERSELIENIMPSTDLCISSclayButton() {
            lienReverse();
      }

      public void onReverselienIMPSTDLCADJclayButton() {
            lienReverse();
      }

      public void onREVERSELIENIMPSTDLCAMDclayButton() {
            lienReverse();
      }

      public boolean lienReverse() {

            boolean value = false;

            String releaseacc = getLIENBAL().trim();
            String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
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
            // Loggers.general().info(LOG,"Lien reversal text box=====>" + releaseacc);
            if (releaseacc.equalsIgnoreCase("") || releaseacc == null) {

                  try {
                        // Loggers.general().info(LOG,"Reverse service method calling started
                        // IGT Adjust in if loop");
                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienRemoval</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienremoval xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienremoval></ServiceRequest>";
                        String gridXml = "";
                        String lienStatus = "";
                        String lienbox = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              // //Loggers.general().info(LOG,"11112212121");
                              String marginAmt = fdwrapper.getMARGAMT();
                              String marginAcc = fdwrapper.getDEPOSTNO().trim();
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String lien_id = fdwrapper.getLIENID();
                              lienStatus = fdwrapper.getLINEST().trim();
                              lienbox = lienbox + lienStatus;
                              // Loggers.general().info(LOG,"Lien Status " + lienbox);
                              if ((!lienbox.equalsIgnoreCase("") || lienbox != null)
                                          && lienStatus.equalsIgnoreCase("MARK SUCCEEDED")) {

                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>lienRev_id</LienId><Amount>Amount_Ccy</Amount><Currency>Lc_Ccy</Currency><Remarks>masRefNo</Remarks></FDRow>";
                                    // String endXml = "</fdlien></ServiceRequest>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("lienRev_id", lien_id);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("Lc_Ccy", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("masRefNo", masRefNo);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien status is blank");
                              }

                        }
                        // //Loggers.general().info(LOG,"gridXml - "+gridXml);
                        // //Loggers.general().info(LOG,"gridXml final ----> "+gridXml);
                        // Final formation of request
                        char j = '"';
                        String val = Character.toString(j);
                        // Loggers.general().info(LOG,"char value - " + j);
                        headerReq = headerReq.replace("$", val);
                        // Replace correlation id
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        // //Loggers.general().info(LOG,"headerReq replaced with quotes - " +
                        // headerReq);

                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml -----> " +
                        // finalXml);

                        // sending to themebridge
                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String resquestXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        // Loggers.general().info(LOG,"Themebridge resquestXML" + resquestXML);

                        // String line = "12312~500~SUCCEEDED~Lien test
                        // reversal,222450~600~SUCCEEDED~Lien
                        // reversal,33333~700~FAILED~Lien
                        // failed";
                        String line = resquestXML;
                        // String reverse = "Reversal";
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");
                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    String currentDepositNum = res_sp_line[0];
                                    String depositNum = str.getDEPOSTNO().trim();
                                    String lienst = str.getLINEST().trim();
                                    // Loggers.general().info(LOG,"currentDepositNum - " +
                                    // currentDepositNum);
                                    // Loggers.general().info(LOG,"depositNum - " + depositNum);
                                    if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                          if (lienst.equalsIgnoreCase("MARK SUCCEEDED")
                                                      && (!lienbox.equalsIgnoreCase("") || lienbox != null)) {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // blank");
                                                str.setLINEST(res_sp_line[2]);
                                                // Loggers.general().info(LOG,"Lien id " +
                                                // res_sp_line[1]);
                                                str.setLIENID(res_sp_line[1]);
                                                str.setLIENREM(res_sp_line[3]);
                                                // getBtnReverselienIMPLCCLAIMRECclay().setEnabled(false);
                                          } else if (lienst.equalsIgnoreCase("REVERSAL SUCCEEDED")) {

                                          } else {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // FAILED");
                                                // str.setLIENID(res_sp_line[1]);
                                                // str.setLINEST(res_sp_line[2]);
                                                // str.setLIENREM(res_sp_line[3]);
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"depositNum is not equal
                                          // currentDepositNum in reversal");
                                    }

                              }
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            } else {
                  // Loggers.general().info(LOG,"Reverse service method calling started IGT
                  // Adjust in else====>");

                  try {
                        // Loggers.general().info(LOG,"Reverse service method calling started
                        // IGT Adjust");
                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienRemoval</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienremoval xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienremoval></ServiceRequest>";
                        String gridXml = "";
                        String lienStatus = "";
                        String lienbox = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              // //Loggers.general().info(LOG,"11112212121");
                              String marginAmt = fdwrapper.getMARGAMT();
                              String DepositNum = fdwrapper.getDEPOSTNO().trim();
                              String marginAcc = getLIENBAL().trim();
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String lien_id = fdwrapper.getLIENID();
                              lienStatus = fdwrapper.getLINEST();
                              lienbox = lienbox + lienStatus;
                              // Loggers.general().info(LOG,"Lien Status " + lienbox);
                              if ((!lienbox.equalsIgnoreCase("") || lienbox != null)
                                          && (!marginAcc.equalsIgnoreCase("") || marginAcc != null)
                                          && marginAcc.equalsIgnoreCase(DepositNum)
                                          && lienStatus.equalsIgnoreCase("MARK SUCCEEDED")) {
                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>lienRev_id</LienId><Amount>Amount_Ccy</Amount><Currency>Lc_Ccy</Currency><Remarks>masRefNo</Remarks></FDRow>";
                                    // String endXml = "</fdlien></ServiceRequest>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("lienRev_id", lien_id);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("Lc_Ccy", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("masRefNo", masRefNo);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien status and lien deposit no
                                    // is blank" + DepositNum + "and====>" + marginAcc);
                              }
                        }
                        // //Loggers.general().info(LOG,"gridXml - "+gridXml);
                        // //Loggers.general().info(LOG,"gridXml final ----> "+gridXml);
                        // Final formation of request
                        char j = '"';
                        String val = Character.toString(j);
                        // Loggers.general().info(LOG,"char value - " + j);
                        headerReq = headerReq.replace("$", val);
                        // Replace correlation id
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        // //Loggers.general().info(LOG,"headerReq replaced with quotes - " +
                        // headerReq);

                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml -----> " +
                        // finalXml);

                        // sending to themebridge
                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String resquestXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        // Loggers.general().info(LOG,"Themebridge resquestXML" + resquestXML);

                        // String line = "12312~500~SUCCEEDED~Lien test
                        // reversal,222450~600~SUCCEEDED~Lien
                        // reversal,33333~700~FAILED~Lien
                        // failed";
                        String line = resquestXML;
                        // String reverse = "Reversal";
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");

                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    // String currentDepositNum = res_sp_line[0].trim();
                                    String currentDepositNum = str.getDEPOSTNO().trim();
                                    String depositNum = getLIENBAL().trim();
                                    String lienst = str.getLINEST().trim();
                                    // Loggers.general().info(LOG,"currentDepositNum - " +
                                    // currentDepositNum);
                                    // Loggers.general().info(LOG,"depositNum - " + depositNum);
                                    if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                          if (lienst.equalsIgnoreCase("MARK SUCCEEDED")
                                                      && (!lienbox.equalsIgnoreCase("") || lienbox != null)) {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // blank");
                                                str.setLINEST(res_sp_line[2]);
                                                // Loggers.general().info(LOG,"Lien id " +
                                                // res_sp_line[1]);
                                                str.setLIENID(res_sp_line[1]);
                                                str.setLIENREM(res_sp_line[3]);
                                                // getBtnReverselienIMPLCCLAIMRECclay().setEnabled(false);
                                          } else if (lienst.equalsIgnoreCase("REVERSAL SUCCEEDED")) {
                                                // Loggers.general().info(LOG,"REVERSAL update=====>");

                                          } else {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // FAILED");
                                                // str.setLIENID(res_sp_line[1]);
                                                // str.setLINEST(res_sp_line[2]);
                                                // str.setLIENREM(res_sp_line[3]);
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"depositNum is not equal
                                          // currentDepositNum in reversal");
                                    }

                              }

                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            }

            return value;
      }

      public void onReverselienIMPADVclayButton() {
            lienReverse();
      }

      public void onREVERSELIENILCAMDclayButton() {
            lienReverse();
      }
      
      public void onREVERSELIENILCSETTclayButton() {
            lienReverse();
      }
      
      public void onREVERSELIENIMPSTALCOUTCLAclayButton() {
            lienReverse();
      }

      public void onREVERSELIENIMPLCclayButton() {
            lienReverse();
      }

      public void onfetchlienIMPADVclayButton() {
            lienMark();
      }

      public boolean lienMark() {
            boolean value = false;

            String Margamt = "0";
            String gridamtcur = "";
            String result = "";
            String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
            String AcnoRem = "Check Space or Spl charactor should not be there";
            String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "").trim();
            getExtEventLienMarkingDelete().setEnabled(false);
            Margamt = getMARAMT().toString();
            String facility = getFACLTYID();
            int count_R = 0;
            int count_M = 0;
            if (Margamt.equalsIgnoreCase("") || Margamt == null) {
                  Margamt = "0";

            }
            BigDecimal margindec = new BigDecimal("0");
            BigDecimal margindecadd = new BigDecimal("0");
            BigDecimal marginbig = new BigDecimal("0");
            BigDecimal one = new BigDecimal(1);
            BigDecimal thousand = new BigDecimal(1000);
            BigDecimal hundred = new BigDecimal(100);
            String add_lie = Margamt.replaceAll("[^0-9]", "");
            // Loggers.general().info(LOG," MarginAmt in number----> " + add_lie);
            // Margamtdob = Double.valueOf(add_lie);
            BigDecimal MargamtNum = new BigDecimal(add_lie);
            BigDecimal Margamtdob = MargamtNum.divide(hundred);

            // Loggers.general().info(LOG,"Grid BigDecimal amount--->" + Margamtdob);

            getExtEventLienMarkingDelete().setEnabled(false);
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

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"Grid BigDecimal amount--->" + Margamtdob);

            }

            EnigmaArray<ExtEventLienMarkingEntityWrapper> liste2 = getExtEventLienMarkingData();

            if (liste2.getSize() > 0) {
                  Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = liste2.iterator();

                  while (iterator1.hasNext()) {
                        ExtEventLienMarkingEntityWrapper ExtEventLienMark = (ExtEventLienMarkingEntityWrapper) iterator1.next();

                        String marginAmt = ExtEventLienMark.getMARGAMTAmount().toString();
                        // Loggers.general().info(LOG,"Amount marginAmt String-----> " +
                        // marginAmt);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Amount marginAmt String-----> " + marginAmt);

                        }
                        marginbig = new BigDecimal(marginAmt);

                        gridamtcur = ExtEventLienMark.getMARGAMTCurrency().toString();
                        // Loggers.general().info(LOG,"Amount marginAmt-----> " + marginAmt);

                        // ConnectionMaster connectionMaster = new ConnectionMaster();
                        // double divideByDecimal =
                        // connectionMaster.getDecimalforCurrency(gridamtcur);
                        // gridadded = valamtdob / divideByDecimal;
                        // Loggers.general().info(LOG,"Grid divided amount--->" + gridadded);

                        // gridamount = gridamount + gridadded;
                        //
                        // gridamount += gridadded;
                        // Loggers.general().info(LOG,"Amount marginAmt double in while-----> "
                        // + gridamount);
                        if (gridamtcur.equalsIgnoreCase("BHD") || gridamtcur.equalsIgnoreCase("KWD")
                                    || gridamtcur.equalsIgnoreCase("OMR")) {
                              margindec = marginbig.divide(thousand);
                        } else if (gridamtcur.equalsIgnoreCase("JPY") || gridamtcur.equalsIgnoreCase("KRW")) {

                              margindec = marginbig.divide(one);
                        } else {
                              margindec = marginbig.divide(hundred);
                        }
                        // Loggers.general().info(LOG,"Amount marginAmt bigdecimal in
                        // while-----> " + margindec);

                        margindecadd = margindecadd.add(margindec);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Margin add amount-----> " + margindecadd);

                        }

                        String deptno = ExtEventLienMark.getDEPOSTNO().trim();
                        String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();

                        // try {
                        // if (!deptno.equalsIgnoreCase("")) {
                        // String qr = "SELECT COUNT(*),LIEN,ACCOUNTID,MASTERREFERENCE
                        // FROM THEMEBRIDGE.LIENDETAILS WHERE ACCOUNTID ='"
                        // + deptno + "' AND MASTERREFERENCE ='" + masRefNo + "' AND
                        // EVENTREFERENCE ='" + eventCode
                        // + "' GROUP BY LIEN,ACCOUNTID,MASTERREFERENCE";
                        // Loggers.general().info(LOG,"Query for LIENDETAILS====>" + qr);
                        // con = ConnectionMaster.getThemeBridgeConnection();
                        // ps = con.prepareStatement(qr);
                        // // //Loggers.general().info(LOG,qr);
                        // rs = ps.executeQuery();
                        // while (rs.next()) {
                        // if (rs.getString(2).equalsIgnoreCase("R")) {
                        // count_R++;
                        // Loggers.general().info(LOG,"LIENDETAILS count_Reversal====>" +
                        // count_R);
                        // } else if (rs.getString(2).equalsIgnoreCase("M")) {
                        // count_M++;
                        // Loggers.general().info(LOG,"LIENDETAILS count_Mark====>" + count_M);
                        // }
                        //
                        // }
                        // }
                        //
                        // } catch (Exception e) {
                        // //Loggers.general().info(LOG,"exception caught " +e.getMessage()
                        // );
                        // } finally {
                        // try {
                        // if (con != null) {
                        // con.close();
                        // if (ps != null)
                        // ps.close();
                        // if (rs != null)
                        // rs.close();
                        // }
                        // } catch (SQLException e) {
                        // // Loggers.general().info(LOG,"Connection Failed! Check output
                        // // console");
                        // e.printStackTrace();
                        // }
                        // }
                  }

            }
            // Loggers.general().info(LOG,"count_Reversal====>" + count_R +
            // "count_Mark=====>" + count_M);

            // if (count_R >= count_M) {
            // Loggers.general().info(LOG,"If loop ==== > Account no is added new");
            if (facility.length() > 0) {
                  if (Margamtdob.compareTo(margindecadd) >= 0) {

                        try {

                              String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienAdd</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienadd xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                              String endXml = "</fdlienadd></ServiceRequest>";
                              String gridXml = "";
                              String startDate = "";
                              String endDate = "";
                              EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                                    String marginAmt = fdwrapper.getMARGAMT();
                                    String marginAcc = fdwrapper.getDEPOSTNO().trim();
                                    String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                                    String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                                    String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                                    String mas_evnt = masRefNo;
                                    String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                                    DateFormat userDateFormat = new SimpleDateFormat("dd/mm/yy");
                                    DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-mm-dd");

                                    Date date;
                                    Date date1;

                                    try {
                                          date = (Date) userDateFormat.parse(issueDate);
                                          startDate = dateFormatNeeded.format(date);
                                          date1 = (Date) userDateFormat.parse(expiryDate);
                                          endDate = dateFormatNeeded.format(date1);

                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");

                                          Date sysdate = (Date) formatter1.parse(systemDate);
                                          // Loggers.general().info(LOG,"systemDate Date lien----- " +
                                          // systemDate);
                                          result = formatter1.format(sysdate);
                                          // Loggers.general().info(LOG,"result Date ----- " +
                                          // result);

                                    } catch (ParseException e) {
                                          // Loggers.general().info(LOG,"Date Exception ");
                                    }
                                    String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                                    String mas_evntRef = masRefNo;
                                    String lienbox = fdwrapper.getLINEST();
                                    if (lienbox.equalsIgnoreCase("") || lienbox == null) {
                                          // Loggers.general().info(LOG,"Lien Status is blank");
                                          String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><Amount>Amount_Ccy</Amount><Currency>LC_Cur</Currency><ReasonCode>TRD</ReasonCode><Remarks>mas_evnt</Remarks><LienStartDate>Issue_Date</LienStartDate><LienEndDate>Expiry_Date</LienEndDate></FDRow>";
                                          fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                          fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                          fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                          fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                          fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                          fdDefaultXml = fdDefaultXml.replace("LC_Cur", luCur);
                                          fdDefaultXml = fdDefaultXml.replace("mas_evnt", mas_evntRef);
                                          fdDefaultXml = fdDefaultXml.replace("Issue_Date", startDate);
                                          fdDefaultXml = fdDefaultXml.replace("Expiry_Date", endDate);
                                          gridXml = gridXml + fdDefaultXml;
                                    } else {
                                          // Loggers.general().info(LOG,"Lien Status is not
                                          // blank");
                                    }

                              }

                              char j = '"';
                              String val = Character.toString(j);
                              headerReq = headerReq.replace("$", val);
                              String correlationId = randomCorrelationId();
                              // Loggers.general().info(LOG,"correlationId " + correlationId);
                              headerReq = headerReq.replace("Correlation_Id", correlationId);
                              String finalXml = headerReq + gridXml + endXml;
                              // Loggers.general().info(LOG,"Themebridge finalXml " +
                              // finalXml);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Themebridge finalXml " + finalXml);

                              }
                              ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                              String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Final Themebridge responseXML==========>" + responseXML);

                              }

                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              String line = responseXML;
                              String[] sp_line = line.split(",");
                              for (int k = 0; k < sp_line.length; k++) {
                                    String[] res_sp_line = sp_line[k].split("~");
                                    Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = leanDataList.iterator();

                                    for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                          String depositNum = str.getDEPOSTNO().trim();

                                          String currentDepositNum = res_sp_line[0].trim();
                                          // //Loggers.general().info(LOG,"res_sp_line[0]==>"+res_sp_line[0]);
                                          // String lienStr = str.getMARGAMT();
                                          // double lienAmt = Double.valueOf(lienStr);
                                          String lienbox = str.getLINEST();
                                          // //Loggers.general().info(LOG,"currentDepositNum -
                                          // "+currentDepositNum);
                                          // //Loggers.general().info(LOG,"depositNum -
                                          // "+depositNum);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Lien grid depositNum" + depositNum);
                                                Loggers.general().info(LOG,"Lien response currentDepositNum" + currentDepositNum);

                                          }
                                          if (depositNum.matches("[0-9]+") && depositNum.length() > 1) {
                                                if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                                      if (res_sp_line[2].equalsIgnoreCase("MARK SUCCEEDED")
                                                                  && (lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien
                                                            // SUCCEEDED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINEST(res_sp_line[2]);
                                                            str.setLIENID(res_sp_line[1]);
                                                            str.setLIENREM(res_sp_line[3]);
                                                            //str.setLIENDAT(result);
                                                            getExtEventLienMarkingUpdate().setEnabled(false);
                                                            getExtEventLienMarkingDelete().setEnabled(false);
                                                      } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien FAILED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINEST(res_sp_line[2]);
                                                            str.setLIENID(res_sp_line[1]);
                                                            str.setLIENREM(res_sp_line[3]);
                                                            //str.setLIENDAT(result);
                                                            getExtEventLienMarkingUpdate().setEnabled(true);
                                                            getExtEventLienMarkingDelete().setEnabled(false);
                                                      }

                                                      else {

                                                            // Loggers.general().info(LOG,"lien marked
                                                            // depositNum is not equal to
                                                            // currentDepositNum");
                                                      }
                                                } else {
                                                      // Loggers.general().info(LOG,"lien marked
                                                      // status is
                                                      // not blank");
                                                }
                                          } else {
                                                str.setLIENREM(AcnoRem);
                                                // Loggers.general().info(LOG,"Special charactor");
                                          }
                                    }

                              }

                              // }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"ThemeTransportClient Exceptions!
                              // " +
                              // e.getMessage());
                        }

                  }

                  else {
                        // Loggers.general().info(LOG,"Lien mark amount is greater than the
                        // margin amount");

                  }
            } else {
                  // Loggers.general().info(LOG,"Facility id is blank");

                  try {

                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienAdd</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienadd xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienadd></ServiceRequest>";
                        String gridXml = "";
                        String startDate = "";
                        String endDate = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              String marginAmt = fdwrapper.getMARGAMT();
                              String marginAcc = fdwrapper.getDEPOSTNO().trim();
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                              String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                              String mas_evnt = masRefNo;
                              String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                              DateFormat userDateFormat = new SimpleDateFormat("dd/mm/yy");
                              DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-mm-dd");
                              Date date;
                              Date date1;
                              try {
                                    date = (Date) userDateFormat.parse(issueDate);
                                    startDate = dateFormatNeeded.format(date);
                                    date1 = (Date) userDateFormat.parse(expiryDate);
                                    endDate = dateFormatNeeded.format(date1);

                                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");

                                    Date sysdate = (Date) formatter1.parse(systemDate);
                                    // Loggers.general().info(LOG,"systemDate Date lien----- " +
                                    // systemDate);
                                    result = formatter1.format(sysdate);
                                    // Loggers.general().info(LOG,"result Date ----- " + result);

                              } catch (ParseException e) {
                                    // Loggers.general().info(LOG,"Date Exception ");
                              }
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String mas_evntRef = masRefNo;
                              String lienbox = fdwrapper.getLINEST();
                              if (lienbox.equalsIgnoreCase("") || lienbox == null) {
                                    // Loggers.general().info(LOG,"Lien Status is blank");
                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><Amount>Amount_Ccy</Amount><Currency>LC_Cur</Currency><ReasonCode>TRD</ReasonCode><Remarks>mas_evnt</Remarks><LienStartDate>Issue_Date</LienStartDate><LienEndDate>Expiry_Date</LienEndDate></FDRow>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("LC_Cur", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("mas_evnt", mas_evntRef);
                                    fdDefaultXml = fdDefaultXml.replace("Issue_Date", startDate);
                                    fdDefaultXml = fdDefaultXml.replace("Expiry_Date", endDate);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien Status is not blank");
                              }

                        }

                        char j = '"';
                        String val = Character.toString(j);
                        headerReq = headerReq.replace("$", val);
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml " + finalXml);

                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Final Themebridge responseXML==========>" + responseXML);

                        }

                        EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                        String line = responseXML;
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");
                              Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = leanDataList.iterator();

                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    String depositNum = str.getDEPOSTNO().trim();

                                    String currentDepositNum = res_sp_line[0];
                                    // //Loggers.general().info(LOG,"res_sp_line[0]==>"+res_sp_line[0]);

                                    String lienbox = str.getLINEST();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Lien grid depositNum" + depositNum);
                                          Loggers.general().info(LOG,"Lien response currentDepositNum" + currentDepositNum);

                                    }
                                    if (depositNum.matches("[0-9]+") && depositNum.length() > 1) {
                                          if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                                if (res_sp_line[2].equalsIgnoreCase("MARK SUCCEEDED")
                                                            && (lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien SUCCEEDED
                                                      // status" + res_sp_line[2]);
                                                      str.setLINEST(res_sp_line[2]);
                                                      str.setLIENID(res_sp_line[1]);
                                                      str.setLIENREM(res_sp_line[3]);
                                                      //str.setLIENDAT(result);
                                                      getExtEventLienMarkingUpdate().setEnabled(false);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien FAILED
                                                      // status" +
                                                      // res_sp_line[2]);
                                                      str.setLINEST(res_sp_line[2]);
                                                      str.setLIENID(res_sp_line[1]);
                                                      str.setLIENREM(res_sp_line[3]);
                                                      //str.setLIENDAT(result);
                                                      getExtEventLienMarkingUpdate().setEnabled(true);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                }

                                                else {

                                                      // Loggers.general().info(LOG,"lien marked
                                                      // depositNum is not equal to
                                                      // currentDepositNum");
                                                }
                                          } else {
                                                // Loggers.general().info(LOG,"lien marked status is
                                                // not
                                                // blank");
                                          }
                                    } else {
                                          str.setLIENREM(AcnoRem);
                                          // Loggers.general().info(LOG,"Special charactor");
                                    }
                              }

                        }

                        // }
                  } catch (Exception e) {
                        Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        e.getMessage());
                  }

            }
            // } else {
            // getExtEventLienMarkingDelete().setEnabled(false);
            // // Loggers.general().info(LOG,"else ==== > Account no is already exit");
            // String sameAcc = "Same account no should not add for same master
            // reference no,Please modify existing account no";
            // EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList =
            // getExtEventLienMarkingData();
            //
            // Iterator<ExtEventLienMarkingEntityWrapper> iterator1 =
            // leanDataList.iterator();
            //
            // for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
            // String lienremark = str.getLIENREM().trim();
            // if (lienremark.equalsIgnoreCase("") || lienremark == null) {
            // str.setLIENREM(sameAcc);
            // getExtEventLienMarkingDelete().setEnabled(false);
            // }
            //
            // }
            // }

            return value;
      }
      
      
      public void ondisplayvalIMPSTALCOUTCLAclayButton() {
            // Loggers.general().info(LOG,"Imp gur button for POD");
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
            String Ifsc = getIFSCCO_Name().trim();

            if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                        // Loggers.general().info(LOG,"query for IFSC button" + query);
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                        }

                        if (count == 0) {

                              // Loggers.general().info(LOG,"IFSC Code is Not valid ");
                              String ban = getBENBAK();
                              String bran = getBENBRN();
                              String cit = getBENCITY();
                              ban = "";
                              bran = "";
                              bran = "";
                              setBENBAK(ban);
                              setBENBRN(bran);
                              setBENCITY(bran);

                        } else {
                              try {
                                    // Loggers.general().info(LOG,"IFSC Code is valid");
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"IFSC code is " + spq);
                                    }
                                    ps1 = con.prepareStatement(spq);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          setBENBAK((rs1.getString(1).trim()));
                                          setBENBRN(rs1.getString(2).trim());
                                          setBENCITY((rs1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
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
                  // Loggers.general().info(LOG,"IFSC code is blak");
            }

      }
      
      //Button to display pan no. and iecode added by Vishal G
      public void onPANNUMIMPLCclayButton() {
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
            String customer = getDriverWrapper().getEventFieldAsText("APP", "p", "cu");
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
      
      public void onORMFETCHIMPLCCLAIMRECclayButton() {
            getOrmDetails();
      }
      public void onORMFETCHILCSETTclayButton() {
            getOrmDetails();
      }
      
        //Fetch button to get orm details in import product added by Vishal G
      public void getOrmDetails() {
      try {
            String outward = "";
            double totalAmt = 0;
            long balanceAmt = 0;
            String balance = "0.0";
            String creditamt="0";
              String creditccy="";
              String date="";
          String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
          String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
          String custId = getDriverWrapper().getEventFieldAsText("APP", "p", "cu").trim();
            con = ConnectionMaster.getConnection();
                  EnigmaArray<ExtEventOrmDetailsEntityWrapper> liste = getExtEventOrmDetailsData();
                  Iterator<ExtEventOrmDetailsEntityWrapper> iterator1 = liste.iterator();
                  for (int i = 0; i < liste.getSize().intValue(); i++) {
                      while (iterator1.hasNext()) {
                        ExtEventOrmDetailsEntityWrapper fdwarapper1 = (ExtEventOrmDetailsEntityWrapper) iterator1
                              .next();
                        outward = fdwarapper1.getOUTWARD();
                        if (!outward.equalsIgnoreCase("")) {
//                            String outwardQuery="select CREDIT_AMOUNT,CREDIT_CURRENCY,to_char(to_date(VALUE_DATE,'dd-mm-yy'),'yyyy-mm-dd') from ETTV_OUTWARD_REMITTANCE_AMOUNT where mas_ref='"+outward+"' and CIF_NO='"+custId+"'";
//                            System.out.println("query for getting all fields in outward Remittance grid " + outwardQuery);
//                                pst = con.prepareStatement(outwardQuery);
//                                rs1 = pst.executeQuery();
//                                if (rs1.next()) {
//                                  fdwarapper1.setREMAMT(rs1.getString(1)+" "+rs1.getString(2));
//                                        fdwarapper1.setREMDATE(rs1.getString(3));
//                                        
//                                }
//                                String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.UTILIZED,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventorm ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                                            + eventCode + "') AND ext.OUTWARD ='" + outward
//                                            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.UTILIZED,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventorm ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                                            + eventCode + "') AND ext.OUTWARD ='" + outward
//                                            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.UTILIZED,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventorm ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
//                                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
//                                            + eventCode + "') AND ext.OUTWARD ='" + outward
//                                            + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";
//                                System.out.println( "query for previous orm sum " + BalAmtQuery+" "+rs1.getString(1)+" "+rs1.getString(2)+" "+rs1.getString(3));
//                                account = con.prepareStatement(BalAmtQuery);
//                                rst3 = account.executeQuery();
//                                if (rst3.next()) {
//                                  totalAmt=rst3.getDouble(1);
//                                }
                              String sql="{call orm_procedure(?,?,?,?,?,?,?,?)}";
                              
                              stmt=con.prepareCall(sql);
                              stmt.setString(1, outward);
                              stmt.setString(2, custId);
                              stmt.setString(3, masReference);
                              stmt.setString(4, eventCode);
                              stmt.registerOutParameter(5,Types.VARCHAR);
                              stmt.registerOutParameter(6,Types.VARCHAR);
                              stmt.registerOutParameter(7,Types.VARCHAR);
                              stmt.registerOutParameter(8,Types.DOUBLE);
                              
                              stmt.execute();
                              creditamt=stmt.getString(5);
                              creditccy=stmt.getString(6);
                              date=stmt.getString(7);
                              totalAmt=stmt.getDouble(8);
                              
                              fdwarapper1.setREMAMT(creditamt+" "+creditccy);
                              fdwarapper1.setREMDATE(date);
                              System.out.println("quer orm procedure "+creditamt+" "+date+" "+totalAmt);
                        }
                                  balanceAmt = (long) (rs1.getLong(1) - totalAmt);
                                  if (balanceAmt > 0) {

                                     balance  = String.valueOf(balanceAmt);
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
               }
      finally {
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