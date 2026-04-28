package com.misys.tiplus2.customisation.extension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventBOECAP;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceData;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class IdcCreate extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(IdcCreate.class);
      Connection con, con1 = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst, ship_prepare = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ship_result = null;

      public boolean onPostInitialise() {

            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  // PROPCode.getPropval());
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
                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTINWCOLCREclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTINWCOLMAINlayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                  }

                  catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSINWCOLCREclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSINWCOLMAINlayHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  // BOE
                  try {
                        String val = "BOE";
                        String BoeLink = getBoeLink(val);
                        // Loggers.general().info(LOG,"BOE LInk is " + BoeLink);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlBOEINWCOLCREclayHyperlink();
                        dmsh.setUrl(BoeLink);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                  }

                  // 27-12-2016----------------------------------------------------------------------

                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTINWCOLCREclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALINWCOLCREclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKINWCOLCREclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELINWCOLCREclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CRE")) {
                        try {
                              freezeMttNumber();
                              System.out.println("INSIDE freezeMttNumber OF CREATE");
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  }

                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CRE")) {
                              try {
                                    // //Loggers.general().info(LOG,"get value for LOB");
                                    getLOBCREATE();
                              } catch (Exception ee) {
                                    Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                              }
                        }
                        try {
                              // //Loggers.general().info(LOG,"Notional due date is calling");
                              getGoodsCategory();
                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,"Notional due date is calling--->"
                              // +
                              // ee.getMessage());

                        }
                        try {
                              currencyCalc();
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                              }
                        }

                        try {
                              getrbiPurcodeCode();

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in Purpose code---->" + e.getMessage());

                              }
                        }
                        try {
                              // //Loggers.general().info(LOG,"Notional due date is calling");
                              getNotionalDueDateIDC();
                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,"Notional due date is calling--->"
                              // +
                              // ee.getMessage());

                        }
                        // Shipment value toUpperCase();

                        String shipfrom = getWrapper().getSHIPFTO_Name();
                        String shipto = getWrapper().getSHIPTOP_Name();
                        if (shipfrom.length() > 0 && shipto.length() > 0) {
                              try {
                                    String fromVal = shipfrom.toUpperCase();
                                    String toval = shipto.toUpperCase();
                                    if (shipfrom.length() > 0 || shipto.length() > 0) {
                                          getPane().setSHIPFTO_Name(fromVal);
                                          getPane().setSHIPTOP_Name(toval);
                                    }
                              } catch (Exception e) {
                                    // //Loggers.general().info(LOG,"Shipment value --->" +
                                    // e.getMessage());
                              }
                        }

                        String portval = getWrapper().getPORTCOD_Name();
                        // //Loggers.general().info(LOG,"Port Value---->" + portval);
                        if ((!portval.equalsIgnoreCase("")) && portval != null) {
                              try {
                                    // //Loggers.general().info(LOG,"hscode Value in try---->" +
                                    // hscodeval);
                                    String hyperValue = "SELECT trim(PNAME),trim(COUNTRY) FROM EXTPORTCO WHERE PCODE='" + portval
                                                + "'";
                                    // //Loggers.general().info(LOG,"port code query Value---->" +
                                    // hyperValue);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(hyperValue);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String hsploy = rs1.getString(1);
                                          String poname = rs1.getString(2);
                                          // //Loggers.general().info(LOG,"port code description---->"
                                          // +
                                          // hsploy);
                                          getPane().setPORTDESC(hsploy);
                                          getPane().setPORTDECO(poname);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exception is " + e);
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
                              // Loggers.general().info(LOG,"port code is empty");
                        }

                        // port of destination description

                        String portdes = getWrapper().getPORTDES().trim();
                        // //Loggers.general().info(LOG,"Port of destination Value---->" +
                        // portdes);
                        if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
                              try {
                                    String portvalqury = "select trim(PODEST),trim(COUNTRY) from EXTPORTDESTINATION WHERE PODEST='"
                                                + portdes + "'";
                                    // //Loggers.general().info(LOG,"port code destination query
                                    // Value---->" + portvalqury);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(portvalqury);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String hsploy = rs1.getString(1);
                                          String COUNTRY = rs1.getString(2);
                                          // //Loggers.general().info(LOG,"port code description---->"
                                          // +
                                          // hsploy);
                                          getPane().setPODESCP(hsploy);
                                          getPane().setPODESCON(COUNTRY);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exception is " + e.getMessage());
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
                              // Loggers.general().info(LOG,"port code is destination empty");
                        }

                        // port of loading description

                        String portload = getWrapper().getPORLOD().trim();
                        // //Loggers.general().info(LOG,"Port of destination Value---->" +
                        // portdes);
                        if ((!portload.equalsIgnoreCase("")) && portload != null) {
                              try {
                                    String portvalqury = "select Trim(PORTDESC),Trim(COUNTRY) from EXTPORTODLOAD where PORTLOAD='"
                                                + portload + "'";
                                    // //Loggers.general().info(LOG,"port code destination query
                                    // Value---->" + portvalqury);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(portvalqury);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String hsploy = rs1.getString(1);
                                          String country = rs1.getString(2);
                                          // //Loggers.general().info(LOG,"port code description---->"
                                          // +
                                          // hsploy);
                                          getPane().setPOLOADES(hsploy);
                                          getPane().setPOLDCON(country);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"portload exception is " +
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
                              // Loggers.general().info(LOG,"port loading empty");
                        }

                        try {

                              getcountryCode();

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" +
                              // e.getMessage());
                        }

                        // 27-12-2016----------------------------------------------------------------------

                        // hs code population

                        String hscodeval = getWrapper().getHMON().trim();
                        // //Loggers.general().info(LOG,"hscode Value---->" + hscodeval);
                        if ((!hscodeval.equalsIgnoreCase("")) && hscodeval != null) {
                              try {
                                    // //Loggers.general().info(LOG,"hscode Value in try---->" +
                                    // hscodeval);
                                    String hyperValue = "SELECT trim(HSPOY) FROM EXTHMCODE WHERE HCODEE='" + hscodeval + "'";
                                    // //Loggers.general().info(LOG,"Hs code query Value---->" +
                                    // hyperValue);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(hyperValue);
                                    rs = ps1.executeQuery();
                                    while (rs.next()) {
                                          String hsploy = rs.getString(1);
                                          // //Loggers.general().info(LOG,"Policy value---->" +
                                          // hsploy);
                                          getPane().setHSPOLY(hsploy);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exceptio is " + e);
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
                        } else {
                              // //Loggers.general().info(LOG,"HS code is empty for policy ");
                        }

                        // shipment date and bill of lading population

                        String shipdate = getWrapper().getDASHIP_Name();
                        // //Loggers.general().info(LOG,"Shipment date ---> " + shipdate);
                        if (shipdate != null) {
                              SimpleDateFormat shp = new SimpleDateFormat("dd/MM/yy");
                              Date date;
                              try {
                                    date = shp.parse(shipdate);
                                    String output = shp.format(date);
                                    
                              } catch (ParseException e) {

                                    // Loggers.general().info(LOG," output of Shipment date" +
                                    // e.getMessage());
                              }
                        }
                        
                        if (getMajorCode().equalsIgnoreCase("IDC")) {
                              try {
                              getPane().getExtEventBOECAPUpdate().setEnabled(false);
                              getPane().getExtEventBOECAPNew().setEnabled(false);
                              getPane().getExtEventBOECAPDelete().setEnabled(false);
                              getPane().getExtEventBOECAPUp().setEnabled(false);
                              getPane().getExtEventBOECAPDown().setEnabled(false);
                              }
                              catch (Exception e) {
                                    e.printStackTrace();
                                    // System.out.println("outside BUYERS data:"+e);

                              }
                        }
                  }
            }
            return false;
      }

      @Override
      public void onValidate(ValidationDetails validationDetails) {
            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  // PROPCode.getPropval());
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
                  String step_Id = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();

                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSINWCOLCREclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSINWCOLMAINlayHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  //kalpana Ghorpade add new trigger point for IDC cac :30/01/25
                  if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CAC")) {
                        try {
                              getTenordays();
                              System.out.println("Inside Tendor Days Method");
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                        try {
                              getLimitError(validationDetails);
                              System.out.println("Inside limit utilisation error Days Method");
                        } catch (Exception e) {
                              e.printStackTrace();
                        }

                              // TODO: handle exception
                        }

                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTINWCOLCREclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALINWCOLCREclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKINWCOLCREclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELINWCOLCREclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  // FCY Tax calculation
                  try {

                        getFCCTCALCULATION();
                        getFcctDetails(validationDetails);
                        System.out.println("fcct calculation Create !!!");
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }
                  try {
                        // //Loggers.general().info(LOG,"Notional due date is calling");
                        getGoodsCategory();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,"Notional due date is calling--->"
                        // +
                        // ee.getMessage());

                  }

                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  //// Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

                  if ((step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                              && getMinorCode().equalsIgnoreCase("CRE")) {
                        // //Loggers.general().info(LOG,"Invoice Validate Called");
                        List<ExtEventInvoiceData> Invoic = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();
                        if (Invoic.size() < 1) {
                              validationDetails.addError(ErrorType.Other, "Invoice details is mandatory  [CM]");
                        }

                        else {
                              // Loggers.general().info(LOG,"Invoice data is not empty");
                        }
                  }
                  try {
                        getrbiPurcodeCode();

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in Purpose code---->" + e.getMessage());

                        }
                  }
                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                        }
                  }

                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                  // workflow error configuration
                  try {
                        int count = 0;

                        if (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("AdhocCSM")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CSM'";
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                                    // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);

                              }
                        } else if (step_csm.equalsIgnoreCase("CBS Maker")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CBS Maker'";
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                                    // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);

                              }
                        }
                        if (count < 1
                                    && step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("AdhocCSM"))
                                    && (getMinorCode().equalsIgnoreCase("CRE"))) {

                              validationDetails.addError(ErrorType.Other, "Workflow checklist is mandatory  [CM]");                       }

                        else {
                              // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in else
                              // loop----> " + count);
                        }

                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception ETT_WF_CHKLST_TRACKING" +
                        // e1.getMessage());
                  }

                  finally {
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

                  try {
                        String rbipur = getWrapper().getOPURPOS_Name().trim();
                        if (rbipur.equalsIgnoreCase("") || rbipur.length() > 0) {
                              String str = rbipur.substring(0, 1);
                              if (((!str.equalsIgnoreCase("S")) || str.equalsIgnoreCase("P"))
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addWarning(WarningType.Other, "RBI purpose code should start with S  [CM]");
                                    //Changed By pravin on 30-11-2021 as advised by bank team. It should be a warning instead of error in IDC create
                                    
                                    

                              } else {
                                    // Loggers.general().info(LOG,"Purpose code is P----> " + str);
                              }
                        }
                  } catch (Exception e) {
                        Loggers.general().info(LOG,"Exception in purpose code" + e.getMessage());
                  }

                  if (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("CBS Maker 1")) {
                        try {
                              con = getConnection();
                              String query = "select wm.MESSAGE from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'MIXFX' and MAS.MASTER_REF = '"
                                          + MasterReference + "' and BEV.REFNO_PFIX= '" + evnt + "' and BEV.REFNO_SERL = '" + evvcount
                                          + "'";
                              // Loggers.general().info(LOG,"FX deal message in authorization " +
                              // query);
                              String fxDeal = "";
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while");
                                    String fxAmount = rs1.getString(1);
                                    // Loggers.general().info(LOG,"FX deal message===>" + fxAmount);
                                    fxDeal = fxAmount.substring(4, 6);
                                    // Loggers.general().info(LOG,"FX deal message converted===>" +
                                    // fxDeal);
                              }

                              if (fxDeal.equalsIgnoreCase("FX") && (step_csm.equalsIgnoreCase("CBS Authoriser")
                                          || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    // Loggers.general().info(LOG,"warning message in authorization
                                    // if
                                    // loop" + count3 + " and step_Id" + step_csm);
                                    validationDetails.addError(ErrorType.Other, "FX deal amount more than transaction amount [CM]");
                              }

                              else {
                                    // Loggers.general().info(LOG,"warning message in authorization
                                    // else" + count3 + " and step_Id" + step_csm);
                              }

                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception" + e1.getMessage());
                        }

                        finally {
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

                  try {
                        con = getConnection();
                        String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
                                    + MasterReference + "' and BEV.REFNO_PFIX= '" + evnt + "' and BEV.REFNO_SERL = '" + evvcount
                                    + "'";
                        // Loggers.general().info(LOG,"Query FX deal rate is outside specified
                        // tolerance " + query);
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // //Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // //Loggers.general().info(LOG,"value of count in while
                              // authorization" + count);
                        }

                        if (count > 0
                                    && (step_Id.equalsIgnoreCase("CBS Authoriser") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                              // //Loggers.general().info(LOG,"warning message in authorization if
                              // loop" + count + " and step_Id" + step_Id);
                              validationDetails.addError(ErrorType.Other, "FX deal rate is outside specified tolerance [CM]");
                        }

                        else {
                              // //Loggers.general().info(LOG,"warning message in authorization
                              // else" + count + " and step_Id" + step_Id);
                        }

                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception" + e1.getMessage());
                  }

                  finally {
                        try {
                              if (rs1 != null)
                                    rs1.close();
                              if (ps1 != null)
                                    ps1.close();
                              if (con != null)
                                    con.close();
                        } catch (SQLException e) {
                              //// Loggers.general().info(LOG,"Connection Failed! Check output
                              //// console");
                              e.printStackTrace();
                        }
                  }

                  // port of loading description

                  String portload = getWrapper().getPORLOD().trim();
                  // //Loggers.general().info(LOG,"Port of destination Value---->" + portdes);
                  if ((!portload.equalsIgnoreCase("")) && portload != null) {
                        try {
                              String portvalqury = "select Trim(PORTDESC),Trim(COUNTRY) from EXTPORTODLOAD where PORTLOAD='"
                                          + portload + "'";
                              // //Loggers.general().info(LOG,"port code destination query
                              // Value---->" + portvalqury);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(portvalqury);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);
                                    String country = rs.getString(2);
                                    // //Loggers.general().info(LOG,"port code description---->" +
                                    // hsploy);
                                    getPane().setPOLOADES(hsploy);
                                    getPane().setPOLDCON(country);
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"portload exception is " +
                              // e.getMessage());
                        } finally {
                              try {
                                    if (rs != null)
                                          rs.close();
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
                        // Loggers.general().info(LOG,"port loading empty");
                  }

                  String portval = getWrapper().getPORTCOD_Name().trim();
                  // //Loggers.general().info(LOG,"Port Value---->" + portval);
                  if ((!portval.equalsIgnoreCase("")) && portval != null) {
                        try {
                              // //Loggers.general().info(LOG,"hscode Value in try---->" +
                              // hscodeval);
                              String hyperValue = "SELECT trim(PNAME),trim(COUNTRY) FROM EXTPORTCO WHERE PCODE='" + portval + "'";
                              // //Loggers.general().info(LOG,"port code query Value---->" +
                              // hyperValue);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(hyperValue);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);
                                    String poname = rs.getString(2);
                                    // //Loggers.general().info(LOG,"port code description---->" +
                                    // hsploy);
                                    getPane().setPORTDESC(hsploy);
                                    getPane().setPORTDECO(poname);
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"exception is " + e);
                        } finally {
                              try {
                                    if (rs != null)
                                          rs.close();
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
                        // Loggers.general().info(LOG,"port code is empty");
                  }

                  try {

                        getcountryCode();

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }

                  // port of destination description

                  String portdes = getWrapper().getPORTDES().trim();
                  // //Loggers.general().info(LOG,"Port of destination Value---->" + portdes);
                  if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
                        try {
                              String portvalqury = "select trim(PODEST),trim(COUNTRY) from EXTPORTDESTINATION WHERE PODEST='"
                                          + portdes + "'";
                              // //Loggers.general().info(LOG,"port code destination query
                              // Value---->" + portvalqury);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(portvalqury);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);
                                    String COUNTRY = rs.getString(2);
                                    // //Loggers.general().info(LOG,"port code description---->" +
                                    // hsploy);
                                    getPane().setPODESCP(hsploy);
                                    getPane().setPODESCON(COUNTRY);
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
                                    // Loggers.general().info(LOG,"Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {
                        // Loggers.general().info(LOG,"port code is destination empty");
                  }

                  try {
                        // //Loggers.general().info(LOG,"Notional due date is calling");
                        getNotionalDueDateIDC();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,"Notional due date is calling--->" +
                        // ee.getMessage());
                        // //Loggers.general().info(LOG,"LOB Catch");
                  }
                  String productyp = getDriverWrapper().getEventFieldAsText("PTP", "s", "");

                  try {
                        // IE Code validation
                        String iecode = getDriverWrapper().getEventFieldAsText("DRE", "p", "cBBF").trim();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"IE code is from customer details" + iecode);
                        }

                        if ((iecode.equalsIgnoreCase("") || iecode == null) && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                    && (productyp.equalsIgnoreCase("ICF"))) {
                              validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                          "IE code is not Updated in the customer [CM]");

                        } else

                        {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"IE code is from customer details else" + iecode);
                              }
                        }
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
                  // NOSTRO VALIDATION
                  // if (getMajorCode().equalsIgnoreCase("IDC")) {
                  // String buycrd = getDriverWrapper().getEventFieldAsText("cBHW",
                  // "l", "");
                  // try {
                  //
                  // con = getConnection();
                  // String query = "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW
                  // pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG
                  // ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97
                  // AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN
                  // ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND
                  // pos.KEY97 IN (SELECT MAX(pos.KEY97) FROM POSTING_BASE_VIEW pos,
                  // BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D'
                  // AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND
                  // mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN
                  // ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND
                  // mas.MASTER_REF = '"
                  // + MasterReference + "' ) AND mas.MASTER_REF = '" +
                  // MasterReference + "'";
                  // // Loggers.general().info(LOG,"Query for warning message in NOSTRO
                  // // VALIDATION " + query);
                  // ps1 = con.prepareStatement(query);
                  // rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  // // //Loggers.general().info(LOG,"Entered while");
                  // String cn = rs1.getString(1).trim();
                  //
                  // if (cn.equalsIgnoreCase("CN") && (step_csm.equalsIgnoreCase("CBS
                  // Authoriser"))
                  // && buycrd.equalsIgnoreCase("N") &&
                  // (!buycrd.equalsIgnoreCase("Y"))) {
                  //
                  // validationDetails.addError(ErrorType.Other,
                  // "Pay and Receive should not have Nostro account [CM]");
                  // }
                  //
                  // else {
                  // // Loggers.general().info(LOG,"value of nostro else--->" +
                  // // cn);
                  // }
                  //
                  // }
                  //
                  // } catch (Exception e1) {
                  // // Loggers.general().info(LOG,"Exception for Nostro" +
                  // // e1.getMessage());
                  // }
                  //
                  // finally {
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
                  // }

                  // Notes Populated in Summary

                  try {
                        con = getConnection();
                        String query = "select * from master mas,NOTE, TIDATAITEM tid WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 AND (NOTE.TYPE NOT IN (3,129,1089) or NOTE.TYPE is null) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
                                    + MasterReference + "' ";
                        //// Loggers.general().info(LOG,"Notes Populated in Summary query====> "
                        //// + query);

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

                  // ----------------------

                  String pamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                  // //Loggers.general().info(LOG,"Duplicate Collection amount ----->" +
                  // pamt);
                  String pcur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                  // String rbipor = getDriverWrapper().getEventFieldAsText("AQL",
                  // "s",
                  // "");
                  String prodty = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
                  // //Loggers.general().info(LOG,"Product type Duplicate----->" + prodty);

                  ConnectionMaster connectionMaster = new ConnectionMaster();
                  // String customera = connectionMaster.getproduct(prodty);

                  // ======================================14/12/2016

                  String customera = "";

                  if (prodty.trim().equalsIgnoreCase("IDC")) {
                        // //Loggers.general().info(LOG,"Major Code>>>>>>>" + getMajorCode());
                        customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");// party
                        // //Loggers.general().info(LOG,"Customer no for duplicate---->" +
                        // customera); // id
                  }

                  else {
                        // Loggers.general().info(LOG,">>>>>>>>>>>custa" + customera);
                  }

                  if (pamt.length() > 0 && pcur.length() > 0) {
                        try {
                              String amount = pamt.replaceAll("[^0-9]", "");

                              int noOfRecord = 0;
                              con = ConnectionMaster.getConnection();

                              String duplicateMaster = "select COUNT(*) as COUNT from ETT_DUPLICAT_WARNING_VIEW where PRICUSTMNM='"
                                          + customera + "' and AMOUNT='" + amount + "' and CCY='" + pcur + "' and MASTER_REF !='"
                                          + MasterReference + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Duplicate Record Query is " + duplicateMaster);
                              }
                              ps1 = con.prepareStatement(duplicateMaster);
                              rs1 = ps1.executeQuery();
                              if (rs1.next()) {
                                    noOfRecord = rs1.getInt("COUNT");
                              }
                              // //Loggers.general().info(LOG,"No of Existing records is " +
                              // noOfRecord);
                              if ((noOfRecord == 1 || noOfRecord > 0) && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker")
                                                      || step_csm.equalsIgnoreCase("CSM Reject")
                                                      || step_csm.equalsIgnoreCase("CBS Reject")
                                                      || step_csm.equalsIgnoreCase("AdhocCSM"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "There is an existing transaction for the Same Customer,Amount and Currency [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"Duplicate Record in else " + noOfRecord);
                                    }
                              }

                        } catch (Exception e) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Exception in Duplicate master " + e.getMessage());
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
                  }
                  //

                  // invoice details validation
                  // String productyp = getDriverWrapper().getEventFieldAsText("PTP",
                  // "s",
                  // "");

                  // invoice details validation
                  // String productyp = getDriverWrapper().getEventFieldAsText("PTP",
                  // "s",
                  // "");
                  // if(productyp.equalsIgnoreCase("AIR")&&getDriverWrapper().getEventFieldAsText("CSID","s",
                  // "").equalsIgnoreCase("Input")&&
                  // getMinorCode().equalsIgnoreCase("PCOC")){
                  // List<ExtEventInvoiceData> Invoic = (List<ExtEventInvoiceData>)
                  // getWrapper().getExtEventInvoiceData();
                  // //Loggers.general().info(LOG,"Invoice Validate Called");
                  // int count1 = 0;
                  //
                  // for (int k = 0; k < Invoic.size(); k++) {
                  // ExtEventInvoiceData invoicedat = Invoic.get(k);
                  // String datav = String.valueOf(invoicedat.getINVNUMB());
                  // // //Loggers.general().info(LOG,"values od data v " + datav);
                  // /* while(count==0){ */
                  // for (int m1 = 0; m1 < datav.length(); m1++) {
                  // if (datav.charAt(0) == '/' ||
                  // Character.toString(datav.charAt(m1)).matches("[A-Z]")
                  // || Character.toString(datav.charAt(m1)).matches("[0-9]")) {
                  // // //Loggers.general().info(LOG,"through invoice number ");
                  // } else {
                  //
                  // count1++;
                  // }
                  //
                  // }
                  // }
                  //
                  // // }
                  // if (count1 > 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // validationDetails.addError(ErrorType.Other, "Invalid invoice
                  // number format [CM]");
                  // }
                  // //Loggers.general().info(LOG,"onValidate Called");

                  // BOE validation

                  String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
                  // //Loggers.general().info(LOG,"systemDate " + systemDate);
                  List<ExtEventBOECAP> BOECAP = (List<ExtEventBOECAP>) getWrapper().getExtEventBOECAP();
                  for (int j = 0; j < BOECAP.size(); j++) {
                        ExtEventBOECAP boeval = BOECAP.get(j);
                        String billdate = String.valueOf(boeval.getBOEDAT());
                        // //Loggers.general().info(LOG,"BOE Date " + billdate);
                        try {
                              // Loggers.general().info(LOG,"1");
                              SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                              Date date1 = (Date) parseFormat.parse(billdate);// grid
                                                                                                      // enter
                                                                                                      // SHIP BILL
                                                                                                      // date
                              SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                              String result1 = formatter1.format(date1);
                              Date shipbillDate = (Date) formatter1.parse(result1);
                              Date sysDate = (Date) formatter1.parse(systemDate);
                              if (shipbillDate.after(sysDate) && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    // //Loggers.general().info(LOG,"date1 is before Date2");
                                    validationDetails.addError(ErrorType.Other, "BOE date is future date than the today date [CM]");
                              } else {
                                    // Loggers.general().info(LOG,"----date3 is after Date2---");
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,e.getMessage());
                        }
                  }

                  String stpId = getDriverWrapper().getEventFieldAsText("CSID", "s", "");

                  // INVOICE VALIDATION
                  double invadob_total = 0.0;
                  double am = 0.0;
                  String payamt = "0";
                  double final_payamtlong = 0.0;
                  if (getMinorCode().equalsIgnoreCase("CRE") && getMajorCode().equalsIgnoreCase("IDC")) {

                        List<ExtEventInvoiceData> invoice = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();

                        for (int l = 0; l < invoice.size(); l++) {
                              ExtEventInvoiceData invoicedat = invoice.get(l);
                              payamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                              // //Loggers.general().info(LOG,"Collection amount ----->" +
                              // payamt);
                              String paycur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              double invamt = invoicedat.getINAMOUT().doubleValue();
                              // //Loggers.general().info(LOG,"Invoice grid initial amount ----->"
                              // +
                              // invamt);
                              String invcurr = invoicedat.getINAMOUTCurrency();

                              am = am + invamt;
                              // //Loggers.general().info(LOG,"Invoice amount after add ----->" +
                              // am);

                              connectionMaster = new ConnectionMaster();
                              double divideByDecimal = connectionMaster.getDecimalforCurrency(invcurr);
                              invadob_total = am / divideByDecimal;

                              final_payamtlong = Double.parseDouble(payamt);
                              // //Loggers.general().info(LOG,"payamtflot for conpare ----->" +
                              // final_payamtlong + "<<");
                              // //Loggers.general().info(LOG,"invoice final amount for conpare
                              // ----->" + invadob_total + "<<");

                              if ((!paycur.equalsIgnoreCase(invcurr)) && getMinorCode().equalsIgnoreCase("CRE")
                                          && getMajorCode().equalsIgnoreCase("IDC") && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Collection amount currency should be equal to Invoice grid currency  [CM]");
                              }

                        }

                        if ((invadob_total != final_payamtlong)) {
                              // //Loggers.general().info(LOG," invoice and master amount not
                              // equal
                              // ---->1" + invadob_total + "" + final_payamtlong);
                              if (getMinorCode().equalsIgnoreCase("CRE") && getMajorCode().equalsIgnoreCase("IDC")
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                    validationDetails.addWarning(WarningType.Other,
                                                "Collection amount should be equal to Invoice grid amount [CM]");

                                    // validationDetails.addError(ErrorType.Other,"Collection
                                    // amount should be equal to Invoice grid amount [CM]");
                              }
                        } else {
                              // Loggers.general().info(LOG," invoice and master amount is
                              // equal----->" + invadob_total + "" + final_payamtlong);
                        }

                  }

                  List<ExtEventInvoiceData> invoice = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();
                  for (int j = 0; j < invoice.size(); j++) {
                        ExtEventInvoiceData invoicedat = invoice.get(j);
                        String deteNeg = String.valueOf(invoicedat.getINDATE());
                        Loggers.general().info(LOG,"Invoice date " + deteNeg);

                        if (deteNeg != null && deteNeg.length() > 0 && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              if ((deteNeg.matches("(^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$)"))) {

                                    // //Loggers.general().info(LOG,"Date of invoice is valid ");
                              } else {
                                    // //Loggers.general().info(LOG,"Date of invoice is not valid
                                    // ");
                                    validationDetails.addError(ErrorType.Other,
                                                "Invalid Date format(DD/MM/YYYY) in Invoice date grid  [CM]");

                              }
                        } else {
                              // Loggers.general().info(LOG,"Date of invoice is empty valid ");

                        }
                        String invdate = String.valueOf(invoicedat.getINDATE());
                        // //Loggers.general().info(LOG,"Invoice Date---> " + invdate);
                        if (invdate != null || !invdate.equalsIgnoreCase("")) {
                              try {
                                    // Loggers.general().info(LOG,"1");
                                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                                    Date shipbillDate = (Date) formatter1.parse(invdate);
                                    Date sysDate = formatter1.parse(systemDate);
                                    // Loggers.general().info(LOG,"sysDate Date---> " + sysDate);
                                    // Loggers.general().info(LOG,"shipbillDate -----> " +
                                    // shipbillDate);
                                    if (shipbillDate.after(sysDate) && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          // Loggers.general().info(LOG,"Invoice date is after");
                                          validationDetails.addError(ErrorType.Other,
                                                      "Invoice date is future date than the today date [CM]");
                                    } else {
                                          Loggers.general().info(LOG,"Invoice date is correct");
                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }
                        } else {
                        }

                  }
                  // difference between shipment date and expiry date
                  // difference between shipment date and expiry date
                  String accept = getDriverWrapper().getEventFieldAsText("FCO:sROS", "s", "");

                  if ((!accept.equalsIgnoreCase("") && accept != null) && accept.equalsIgnoreCase("Acceptance")
                              && step_csm.equalsIgnoreCase("CBS Maker")) {
                        try {
                              String d = getDriverWrapper().getEventFieldAsText("FCO:sMD", "d", "");

                              String d1 = getWrapper().getDASHIP_Name();

                              if ((d != null && (!d.equalsIgnoreCase(""))) && (d1 != null && (!d1.equalsIgnoreCase("")))) {
                                    // //Loggers.general().info(LOG,"date of shipment values" + d1);
                                    getPane().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                                    getWrapper().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                              } else {
                                    // Loggers.general().info(LOG,"else part of date difference
                                    // values");
                              }
                        } catch (Exception e) {
                              Loggers.general().info(LOG,"Number of days calculation " + e.getMessage());
                        }
                  }

                  // Shipment value toUpperCase();
                  try {
                        String shipfrom = getWrapper().getSHIPFTO_Name();
                        String shipto = getWrapper().getSHIPTOP_Name();
                        if (shipfrom.length() > 0 && shipto.length() > 0) {
                              try {
                                    String fromVal = shipfrom.toUpperCase();
                                    String toval = shipto.toUpperCase();
                                    if (shipfrom.length() > 0 || shipto.length() > 0) {
                                          getPane().setSHIPFTO_Name(fromVal);
                                          getPane().setSHIPTOP_Name(toval);
                                    }
                              } catch (Exception e) {
                                    // //Loggers.general().info(LOG,"Shipment value --->" +
                                    // e.getMessage());
                              }
                        }
                  } catch (Exception e) {
                        // //Loggers.general().info(LOG,"Shipment value --->" +
                        // e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"Exception Shipment value --->" + e.getMessage());
                        }
                  }
                  /*
                   * // KYC DATE VALIDATION try {
                   *
                   * //Loggers.general().info(LOG,"KYC Date Validation"); String t =
                   * getDriverWrapper().getEventFieldAsText("PRI", "p", "cBEY");
                   * //Loggers.general().info(LOG,"value os first date " + t); String i =
                   * getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                   * //Loggers.general().info(LOG,"value of issue date " + i);
                   * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                   * java.util.Date date1 = sdf.parse(t.trim()); java.util.Date date2
                   * = sdf.parse(i.trim()); if (date1.compareTo(date2) < 0 &&
                   * getDriverWrapper().getEventFieldAsText("CSID", "s",
                   * "").equalsIgnoreCase("input")) {
                   * validationDetails.addWarning(ValidationDetails.WarningType.Other,
                   * "KYC Expired for the Customer  [CM]"); } } catch (Exception e) {
                   * //Loggers.general().info(LOG,"Exception " + e.getMessage()); }
                   *
                   */

                  if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CRE")) {
                        try {
                              // //Loggers.general().info(LOG,"get value for LOB");
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                        }
                  }

                  // hs code population

                  String hscodeval = getWrapper().getHMON().trim();
                  // //Loggers.general().info(LOG,"hscode Value---->" + hscodeval);
                  if ((!hscodeval.equalsIgnoreCase("")) && hscodeval != null) {
                        try {
                              // //Loggers.general().info(LOG,"hscode Value in try---->" +
                              // hscodeval);
                              String hyperValue = "SELECT trim(HSPOY) FROM EXTHMCODE WHERE HCODEE='" + hscodeval + "'";
                              // //Loggers.general().info(LOG,"Hs code query Value---->" +
                              // hyperValue);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(hyperValue);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);
                                    // //Loggers.general().info(LOG,"Policy value---->" + hsploy);
                                    getPane().setHSPOLY(hsploy);
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"exceptio is " + e);
                        } finally {
                              try {
                                    if (rs != null)
                                          rs.close();
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
                        // //Loggers.general().info(LOG,"HS code is empty for policy ");
                  }

                  /*
                   * try {
                   *
                   * String Explc="0"; ////Loggers.general().info(LOG,"getting connection");
                   * con = ConnectionMaster.getConnection(); String sql = (
                   * "SELECT ETT_ELC_BILL_SEQNO.NEXTVAl FROM DUAL");
                   * //Loggers.general().info(LOG, "sequence query"+sql); PreparedStatement
                   * ps=con.prepareStatement(sql); ResultSet rs=ps.executeQuery();
                   * while(rs.next()) { Explc=rs.getString(1); //Loggers.general().info(LOG,
                   * "Explc no" +Explc); getWrapper().setBLLREFNO(Explc); }
                   * con.commit(); rs.close(); ps.close(); con.close(); }
                   * catch(Exception e) { //Loggers.general().info(LOG,"exeception"+e); }
                   */
                  // Category code populate based on input branch
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                        con = ConnectionMaster.getConnection();
                        if (!(BranchCode.length() == 0)) {
                              String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
                              // //Loggers.general().info(LOG,"BranchCode Query - " + sql6);
                              ps1 = con.prepareStatement(sql6);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String inmt = rs1.getString(1);
                                    // //Loggers.general().info(LOG,"category code - " + inmt);
                                    getWrapper().setIMBRCODE(inmt);
                                    getPane().setIMBRCODE(inmt);
                              }

                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception caught on branch code
                        // validation......" + e.getMessage());
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

                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  String cust = getDriverWrapper().getEventFieldAsText("DRE", "p", "no").trim();
                  // //Loggers.general().info(LOG,"Primary customer taking ----> " + cust);

                  try {
                        String mercht = getDriverWrapper().getEventFieldAsText("cBHW", "l", "").toString();
                        String masRefNo = getWrapper().getPREBUYRE().trim();

                        int dmT = 0;

                        String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                                    + masRefNo + "' AND mas.PRICUSTMNM ='" + cust + "'";
                        // Loggers.general().info(LOG,"Master ref no valid for Import lc" +
                        // dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Master ref number valid query" + dms);

                        }
                        con = ConnectionMaster.getConnection();
                        ps = con.prepareStatement(dms);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                              dmT = rs.getInt(1);
                              // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Master ref no valid count" + dmT);

                        }

                        if ((dmT == 0 || dmT < 1) && masRefNo.length() > 0 && mercht.equalsIgnoreCase("Y")
                                    && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                              validationDetails.addWarning(WarningType.Other,
                                          "Buyer's Credit reference number is not valid number,Kinldy check the valid reference number with CRN [CM]");

                        }

                        else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Master ref no valid in else" + dmT + "buyer credit check box" + mercht);

                              }
                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in Master ref no validation" + e.getMessage());
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

                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // //Loggers.general().info(LOG,"charge account collected " + chargecol);
                  String custval = "";

                  try {
                        if (step_csm.equalsIgnoreCase("CBS Maker 1")) {

                              con = getConnection();

                              String dmstlmt = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='" + masterref
                                          + "' and EVENT_REF = '" + eventREF + "'";
                              dmsp = con.prepareStatement(dmstlmt);

                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {

                                    custval = dmsr.getString(1);

                                    if (custval.length() > 0 && chargecol.equalsIgnoreCase("Y")
                                                && (!chargecol.equalsIgnoreCase("N"))) {

                                          // Loggers.general().info(LOG,"custoemr number in query" +
                                          // custval);
                                          // Loggers.general().info(LOG,"custoemr number in
                                          // transaction" + cust);
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Account selected in Settlement for charges does not belong to the Applicant  [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"charge account collected
                                          // matched");
                                    }

                              }
                        } else {
                              // Loggers.general().info(LOG,"Not a CBS Maker step");
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"charge account collected----->" +
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
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }

                  // try {
                  //
                  // String dms = "select * from ETTV_BOE_PENDING_VIEW where CIF='" +
                  // cust + "'";
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"ETTV_BOE_PENDING_VIEW QUERY ----> " + dms);
                  // }
                  // con = getConnection();
                  // ps1 = con.prepareStatement(dms);
                  //
                  // rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  //
                  // if ((step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CSM")
                  // || step_csm.equalsIgnoreCase("CBS Maker") ||
                  // step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                  //
                  // validationDetails.addWarning(WarningType.Other, "Bill of entry
                  // pending for this client [CM]");
                  //
                  // } else {
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"Bill of entry pending for this client else
                  // ----> ");
                  // }
                  // }
                  //
                  // }
                  //
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"charge account collected----->" +
                  // // e.getMessage());
                  // } finally {
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
                  // }

                  // NPA customer

                  try {

                        String productVal = getDriverWrapper().getEventFieldAsText("DRE", "p", "cAJB").trim();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"NPA customer-----> " + productVal);
                        }
                        if (productVal.equalsIgnoreCase("Y") && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                              validationDetails.addWarning(WarningType.Other, "Customer is a NPA customer [CM]");
                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Not a NPA customer-----> " + productVal);
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception NPA customer-----> " + e.getMessage());
                        }
                  }

                  // shipment date and bill of lading population
                  try {
                        String shipdate = getWrapper().getDASHIP_Name();
                        // //Loggers.general().info(LOG,"Shipment date ---> " + shipdate);
                        if (shipdate != null) {
                              SimpleDateFormat shp = new SimpleDateFormat("dd/MM/yy");
                              Date date;
                              try {
                                    date = shp.parse(shipdate);
                                    String output = shp.format(date);
                                    
                              } catch (Exception e) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG," output of Shipment date" + e.getMessage());

                                    }
                              }

                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in Shipment date" + e.getMessage());

                        }
                  }
                  // String seifsc = getWrapper().getSENIFSC().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                  // if (seifsc.length() > 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // try {
                  //
                  // String query = "select count(*) from EXTIFSCC where IFSC='" +
                  // seifsc + "'";
                  // // //Loggers.general().info(LOG,"query " + query);
                  // int count3 = 0;
                  // con = getConnection();
                  // ps1 = con.prepareStatement(query);
                  // rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  // // //Loggers.general().info(LOG,"Entered while");
                  // count3 = rs1.getInt(1);
                  // }
                  //
                  // if (count3 == 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  //
                  // validationDetails.addError(ErrorType.Other, "Invalid Sender IFSC
                  // code(" + seifsc + ") [CM]");
                  // } else {
                  // // Loggers.general().info(LOG,"valid Sender IFSC code");
                  // }
                  //
                  // } catch (Exception e1) {
                  // // Loggers.general().info(LOG,"Exception Sender IFSC Code" +
                  // // e1.getMessage());
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
                  // } else {
                  // Loggers.general().info(LOG,"Sender IFSC Code" + seifsc);

                  // }

                  // String recifsc = getWrapper().getRECIFSC().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                  // if (recifsc.length() > 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // try {
                  //
                  // String query = "select count(*) from EXTIFSCC where IFSC='" +
                  // recifsc + "'";
                  // // //Loggers.general().info(LOG,"query " + query);
                  // int count3 = 0;
                  // con = getConnection();
                  // ps1 = con.prepareStatement(query);
                  // rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  // // //Loggers.general().info(LOG,"Entered while");
                  // count3 = rs1.getInt(1);
                  // }
                  //
                  // if (count3 == 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  //
                  // validationDetails.addError(ErrorType.Other,
                  // "Invalid Receiver IFSC code(" + recifsc + ") [CM]");
                  // } else {
                  // // Loggers.general().info(LOG,"valid Receiver IFSC code");
                  // }
                  //
                  // } catch (Exception e1) {
                  // // Loggers.general().info(LOG,"Exception Receiver IFSC Code" +
                  // // e1.getMessage());
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
                  // } else {
                  // Loggers.general().info(LOG,"Receiver IFSC Code" + recifsc);

                  // }

                  // Base rate validation in IDC

                  String mulcheck = getDriverWrapper().getEventFieldAsText("MPT", "l", "");
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select TO_CHAR(cold.BASEDATE,'DD/MM/YYYY') from master mas, TIDATAITEM tid, COLLDRAFT cold   where mas.KEY97 = tid.MASTER_KEY and tid.KEY97 = cold.KEY97 and cold.DRAFT_TYP !='P' and mas.MASTER_REF  = '"
                                    + masterref + "'"; // and cold.BASEDATE is not null";
                        //// Loggers.general().info(LOG,"BASEDATE value in Query---> " + query);
                        String dateString = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
                        // //Loggers.general().info(LOG,"Today date for dateString2---> " +
                        // dateString);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date extendDate1 = new Date();
                        Date extendDate2 = new Date();
                        if (dateString.length() > 0) {
                              extendDate2 = dateFormat.parse(dateString);
                        }
                        // Loggers.general().info(LOG,"Today date for extendDate2---> " +
                        // extendDate2);

                        // String dateString2 = sdf.format(output);
                        // //Loggers.general().info(LOG,"BASEDATE output--->" + dateString2);
                        String dateString1 = "";
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {

                              dateString1 = rs1.getString(1);
                              // //Loggers.general().info(LOG,"BASEDATE output in query--->" +
                              // dateString1);
                              if (dateString1 == null && mulcheck.equalsIgnoreCase("Y")) {
                                    // //Loggers.general().info(LOG,"BASEDATE output is null--->" +
                                    // dateString1);
                                    if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                && mulcheck.equalsIgnoreCase("Y")) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Base date should not blank in Draft details pane [CM]");
                                    }
                              } else {

                                    // Loggers.general().info(LOG,"BASEDATE value in Query in while
                                    // loop dateString1" + dateString1);
                                    if (dateString1.length() > 0) {
                                          extendDate1 = dateFormat.parse(dateString1);
                                    }
                                    // //Loggers.general().info(LOG,"BASEDATE value in Query in
                                    // while
                                    // loop extendDate1" + extendDate1);
                                    if (dateString1.length() > 0 && extendDate1.compareTo(extendDate2) > 0
                                                && (step_Input.equalsIgnoreCase("i")) && mulcheck.equalsIgnoreCase("Y")) {

                                          // //Loggers.general().info(LOG,"extendDate1 is Greater than
                                          // extendDate2");
                                          validationDetails.addError(ErrorType.Other, " Base Date is Later than Contract Date [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"extendDate1 is less than
                                          // extendDate2");
                                    }
                              }
                        }

                        // //Loggers.general().info(LOG,"BASEDATE value in Query in out of while
                        // loop dateString1" + dateString1);
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
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }

                  try {

                        /*String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, BASEEVENT BEV, ETT_REFERRAL_TRACKING REF WHERE MAS.KEY97 = BEV.MASTER_KEY AND trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)  AND BEV.STATUS <>'a' AND TRIM(REF.MASTER_REF_NO) ='"
                                    + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode + "' AND REF.SUB_PRODUCT_CODE = '"
                                    + subproductCode
                                    + "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID";
                        */
                        String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS,  ETT_REFERRAL_TRACKING REF WHERE  trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)   AND TRIM(REF.MASTER_REF_NO) ='"
                                    + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode + "' AND REF.SUB_PRODUCT_CODE = '"
                                    + subproductCode
                                    + "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query ETT_REFERRAL_TRACKING count===>" + query);
                        }
                        int count = 0;
                        String step = null;
                        String concat = null;
                        con = getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {

                              step = rs1.getString(1);

                              count = rs1.getInt(2);
                              Loggers.general().info(LOG,"Entered while referal step" + step+" count" + count);
                              if (count > 0) {
                                    if ((step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Reject"))
                                                && !step_csm.equalsIgnoreCase("CBS Authoriser")) {
                                    
                                          String ref = null;
                                          String statusReferral = null;
                                          String warningMessage = null;
                                          String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                                                      + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                                                      + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                                                      + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"ETT_REFERRAL_TRACKING Warning Message query step2====>" + query6);
                                          }
                                          ps1 = con.prepareStatement(query6);
                                          ArrayList<String> al = new ArrayList<String>();
                                          rs = ps1.executeQuery();
                                          while (rs.next()) {
                                                Loggers.general().info(LOG,"referral_)status------------>" +rs.getString(2));
                                                if (rs.getString(2).equals("PEND")) {
                                                      
                                                      statusReferral = "Pending";
                                                      ref = rs.getString(3) + "'s Referral " + "\"" + rs.getString(1) + "\"" + " is "
                                                                  + statusReferral + " for approval ";

                                                } else {
                                                      statusReferral = "Rejected";
                                                      ref = rs.getString(3) + "'s Referral " + "\"" + rs.getString(1) + "\"" + " is "
                                                                  + statusReferral + "by approver";

                                                }
                                                al.add(ref);
                                          }
                                          warningMessage = StringUtils.join(al, "\n");
                                          // //Loggers.general().info(LOG,"Single Warning Message " +
                                          // warningMessage);

                                          validationDetails.addWarning(WarningType.Other, warningMessage);

                                    } else if (step_csm.equalsIgnoreCase("CBS Checker")
                                                || step_csm.equalsIgnoreCase("CBS Authoriser")) {
                                          Loggers.general().info(LOG,"Authoriser");
                                          String ref = null;
                                          String statusReferral = null;
                                          String warningMessage = null;
                                          String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                                                      + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                                                      + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                                                      + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
                                          Loggers.general().info(LOG,"query for referral----------------->" +query6);
                                          ps1 = con.prepareStatement(query6);
                                          ArrayList<String> al = new ArrayList<String>();
                                          rs = ps1.executeQuery();
                                          while (rs.next()) {
                                                Loggers.general().info(LOG,"referral_)status------------>" +rs.getString(2));
                                                if (rs.getString(2).equals("PEND")) {
                                                      
                                                      statusReferral = "Pending";
                                                      ref = " Referral " + rs.getString(1) + " is " + statusReferral + " with "
                                                                  + rs.getString(3);

                                                } else {
                                                      statusReferral = "Rejected";
                                                      ref = " Referral " + rs.getString(1) + " has been " + statusReferral + " by "
                                                                  + rs.getString(3);
                                                }
                                                al.add(ref);
                                          }
                                          warningMessage = StringUtils.join(al, "\n");
                                          // //Loggers.general().info(LOG,"Single Warning Message " +
                                          // warningMessage);

                                          validationDetails.addError(ErrorType.Other, warningMessage);

                                    } else {
                                          // Loggers.general().info(LOG,"referal step in step" +
                                          // step_csm);
                                    }
                              } else {

                                    
                        
                                    // Loggers.general().info(LOG,"referal step in step" +
                                    // step_csm);
                              }
                        }
                        // Loggers.general().info(LOG,"Entered while referal count out of loop"
                        // + count);
                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception referal count" +
                        // e1.getMessage());
                  } finally {
                        try {
                              if (rs!= null)
                                    rs.close();
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
                  String prd_code = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                  // rejection validation
                  try {
                        int counter = 0;
                        String query5 = "SELECT  count(*) FROM ETT_WF_CHKLST_TRACKING  WHERE  MASTER_REF ='" + MasterReference
                                    + "'" + " AND EVENTREF= '" + eventCode + "'" + " and PROD_CODE='" + prd_code + "'"
                                    + " AND SUB_PRODUCT_CODE= '" + subproductCode + "'"
                                    + " and INIT_AT IN ('CSM','CBS Maker') and MANDATORY='REJ'  and CHECKED_YN ='Y'";
                        // Loggers.general().info(LOG,"ArrayList Warning Message rejection 1st"
                        // + query5);
                        con = getConnection();
                        pst = con.prepareStatement(query5);

                        rs = pst.executeQuery();
                        while (rs.next()) {
                              counter = Integer.valueOf(rs.getString(1));
                        }

                        // //Loggers.general().info(LOG,"Rejection list count===>" + counter);

                        if (counter > 0) {
                              String ref = null;
                              String statusReferral = null;
                              String warningMessage = null;
                              String query6 = "SELECT  INIT_AT,CHECKLIST_SHRT_DESCR "
                                          + " FROM ETT_WF_CHKLST_TRACKING  WHERE  MASTER_REF ='" + MasterReference + "'"
                                          + " AND EVENTREF = '" + eventCode + "'" + " and PROD_CODE='" + prd_code + "'"
                                          + " AND SUB_PRODUCT_CODE = '" + subproductCode + "'"
                                          + " and INIT_AT IN ('CSM','CBS Maker') and MANDATORY='REJ' and CHECKED_YN ='Y'";
                              // Loggers.general().info(LOG,"ArrayList Warning Message rejection
                              // 2nd" + query6);
                              pst = con.prepareStatement(query6);
                              ArrayList<String> al = new ArrayList<String>();
                              rs = pst.executeQuery();
                              while (rs.next()) {

                                    ref = " Rejections " + "\"" + rs.getString(2) + "\"" + " raised by " + rs.getString(1)
                                                + " is not resolved [CM]";
                                    al.add(ref);
                              }

                              warningMessage = StringUtils.join(al, "\n");
                              if (al.size() > 0 && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CSM Reject")
                                          || step_csm.equalsIgnoreCase("AdhocCSM"))) {
                                    // Loggers.general().info(LOG,"ArrayList Single Warning CSM 2nd"
                                    // + warningMessage);
                                    validationDetails.addWarning(WarningType.Other, warningMessage);
                              }

                              else if (al.size() > 0
                                          && (step_csm.equalsIgnoreCase("CBS Reject") || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    // Loggers.general().info(LOG,"ArrayList Single Warning CBS 2nd"
                                    // + warningMessage);
                                    validationDetails.addError(ErrorType.Other, warningMessage);
                              } else if (al.size() > 0 && step_csm.equalsIgnoreCase("CBS Authoriser")) {
                                    validationDetails.addError(ErrorType.Other, warningMessage);
                              }
                        }

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  } finally {
                        try {

                              if (rs != null)
                                    rs.close();
                              if (pst != null)
                                    pst.close();
                              if (con != null)
                                    con.close();

                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }
                  try {

                        String query5 = "select count(*),trim(mas.PRICUSTMNM),ref.STATUS,ref.REFERRAL_STATUS from MASTER mas,ETT_REFERRAL_TRACKING ref where trim(mas.MASTER_REF)=ref.MASTER_REF_NO and mas.MASTER_REF='"
                                    + MasterReference
                                    + "' and ref.REFERRAL_STATUS='PEND' group by mas.PRICUSTMNM,ref.STATUS,ref.REFERRAL_STATUS";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query REFERRAL_STATUS pending" + query5);
                        }

                        con = getConnection();
                        pst = con.prepareStatement(query5);

                        rs = pst.executeQuery();
                        if (rs.next()) {
                              // int counter = getInt(1);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Pending referal status count if loop");
                              }

                              if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CSM")
                                          || step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addWarning(WarningType.Other, "Past pending deferral for same client [CM]");
                              }

                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"No pending referal status" + query5);
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"ExceptionPending referal status" + e.getMessage());
                        }
                  } finally {
                        try {

                              if (rs != null)
                                    rs.close();
                              if (pst != null)
                                    pst.close();
                              if (con != null)
                                    con.close();

                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }
                  
                  //mechant trade
                  String merchanting=getDriverWrapper().getEventFieldAsText("cARQ","l","");
                  
                  try{
                        //String merchanting=getWrapper().getMERTRA().toString();
                        
                        String merchref=getWrapper().getREMERREF();
                        Loggers.general().info(LOG,"Merchanting------ "+merchanting);
                        Loggers.general().info(LOG,"Merchanting Ref No-----"+merchref);
                        if(merchanting.equalsIgnoreCase("Y"))
                        {
                              if(merchref==null||merchref.equalsIgnoreCase(""))
                              {
                              validationDetails.addError(ErrorType.Other,"Enter the merchant trade reference number");
                              }
                        }
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in Merchant Trade"+e.getMessage());
                  }
                  
                  String advpaymentmade=getDriverWrapper().getEventFieldAsText("cAJO","l", "");
                  String dateofremitt = getWrapper().getDATEREM();                        
                  String remittrefnoadv = getWrapper().getREMREFAP();
                  String shipdate = getWrapper().getDASHIP_Name();
                  
                  if(merchanting.equalsIgnoreCase("Y"))
                  {
                        try{
                        if(advpaymentmade.equalsIgnoreCase("Y")){
                              
                              
                              if(dateofremitt==null||dateofremitt.equalsIgnoreCase(""))
                              {
                                    Loggers.general().info(LOG,"Date of remitt inside Y"+dateofremitt);
                        validationDetails.addError(ErrorType.Other,
                                    "Please enter date of remittance [CM]");
                              }
                        
                  
                        if(remittrefnoadv==null||remittrefnoadv.equalsIgnoreCase(""))
                        {
                              Loggers.general().info(LOG,"remitt ref no inside Y"+remittrefnoadv);
                  validationDetails.addError(ErrorType.Other,
                              "Please enter remittance reference number for advance payment [CM]");
                        
                        }
            }
                        
                        
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                        Calendar c = Calendar.getInstance();
                        Loggers.general().info(LOG,"Advance Payment"+advpaymentmade);
                        Loggers.general().info(LOG,"Date of remittance"+dateofremitt);
                        Loggers.general().info(LOG,"Remittance Ref No"+remittrefnoadv);
                        Loggers.general().info(LOG,"Shipment Date"+shipdate);
                  
                        if(advpaymentmade.equalsIgnoreCase("Y")){
                              
                              int gra = 90;
                              try {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Merchanting Shipdate date 3m---->" + dateofremitt);
                                    }
                                    c.setTime(sdf.parse(dateofremitt));
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Merchanting Completion date added with 90days---->");
                                    }
                                    c.add(Calendar.DATE, gra);
                                    // //Loggers.general().info(LOG,"DAE 1"+ c);
                                    String output = sdf.format(c.getTime());
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Merchanting Completion date 3m---->" + output);
                                    }
            //                      getPane().setMERTCOMD(output);
                              
                        }
                        catch(Exception e)
                        {
                              Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 3m==>"+e.getMessage());
                        }
                        }
                        else{
                              
                    int gra = 180;
                              try {

                                    c.setTime(sdf.parse(shipdate));
                                    // //Loggers.general().info(LOG,"expdate in issue-------> ");
                                    c.add(Calendar.DATE, gra);
                                    // //Loggers.general().info(LOG,"DATE 1"+ c);
                                    String output = sdf.format(c.getTime());
                                    // //Loggers.general().info(LOG,output);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG," Merchanting Completion date 6m---->" + output);
                                    }
                        //          getPane().setMERTCOMD(output);
                              
                        }
                        catch(Exception e)
                        {
                              Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 6m===>"+e.getMessage());
                        }
                        
                        
                        }
                        }
                        catch(Exception e)
                        {
                              Loggers.general().info(LOG,"Exception in advance payment"+e.getMessage());
                        }
                  }
                  
                  
                  

                  // Over due bill exists for this customer
                  if (step_csm.equalsIgnoreCase("CBS Maker")) {
                        try {
                              con = getConnection();
                              String query = "select * from ETT_OVERDUE_BILCUS_EOD where CUSTOMER_ID= '" + cust + "'";

                              // Loggers.general().info(LOG,"Over due bill exists for this
                              // customer " + query);
                              // int count = 0;
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while Over due bill
                                    // exists
                                    // for this customer");

                                    if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          // //Loggers.general().info(LOG,"Over due bill exists for
                                          // this
                                          // customer in if loop " + cust);
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Over due bill exists for this customer (" + cust + ")  [CM]");
                                    }

                                    else {
                                          // //Loggers.general().info(LOG,"Over due bill exists for
                                          // this
                                          // customer in else " + cust);
                                    }
                              }

                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception Over due bill" +
                              // e1.getMessage());
                        }

                        finally {
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
                        // Loggers.general().info(LOG,"Step id not CMS Maker");
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
                  
                  try {
                        getPostingFxrate();
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
                  //Difference between shipment date and maturity date - Vaisak 17-11-21
                  try {
                        if((getMajorCode().equalsIgnoreCase("IDC")) && (getMinorCode().equalsIgnoreCase("CRE"))) {
                        
                        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String maturityDate = getDriverWrapper().getEventFieldAsText("FCO:sMD", "d", "");
                        String shipDate = getDriverWrapper().getEventFieldAsText("cAJT", "d", "");//Shipment Date
                        LocalDate date1 = LocalDate.parse(maturityDate,formatters);
                        LocalDate date2 = LocalDate.parse(shipDate,formatters);
                        long no = ChronoUnit.DAYS.between(date2, date1);
                        getPane().setDIFSHPMA(String.valueOf(no));
                        
                        
                        }
                  } catch (Exception e) {
                        
                  }
                  //Purpose Code starts with S for IDC
                  try {
                        if((getMajorCode().equalsIgnoreCase("IDC")) && (getMinorCode().equalsIgnoreCase("CRE"))) {
                              System.out.println("INSIDE error purpose code OF CREATE");
                        getErrorPurposeCode(validationDetails);
                        
                        // getWarningBOE(validationDetails);
                        System.out.println("INSIDE INVOICE DATA WARNING OF CREATE");
                        getWarningData(validationDetails);
                  }
                  }catch (Exception e) {
                        
                  }
                  if((getMajorCode().equalsIgnoreCase("IDC")) && (getMinorCode().equalsIgnoreCase("CRE"))) {
                        try {
                              getPane().getExtEventBOECAPUpdate().setEnabled(false);
                              getPane().getExtEventBOECAPNew().setEnabled(false);
                              getPane().getExtEventBOECAPDelete().setEnabled(false);
                              getPane().getExtEventBOECAPUp().setEnabled(false);
                              getPane().getExtEventBOECAPDown().setEnabled(false);
                              }
                              catch (Exception e) {
                                    e.printStackTrace();
                                    // System.out.println("outside BUYERS data:"+e);

                              }
                  }

      //Merchant Trade-19/12/22           
                  if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CRE")) {
                        try {
                              freezeMttNumber();
                              mttFreeze();
//                            unsetMTTSeqNo();
                              setMTTRefNumber();
//                            getMttSeqNo();
                              validateClosedMttNumber(validationDetails);
                              validateMttRefNumber(validationDetails);
                              unsetMTTFields();
                              newMttNumber();
                              invalidMTTNumber(validationDetails);
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                        try {
                              mttcommenceDate(validationDetails);
                              mttforeignExchange(validationDetails);
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                        try {
                              getStampDuty();
                              getCountryRisk();
                              }
                              catch (Exception e) {
                                    e.printStackTrace();
                              }
                        
                        try {
                              getBOEWarning(validationDetails);
                              }
                              catch (Exception e) {
                                    e.printStackTrace();
                              }
                        try {
                              getPostingBranch(validationDetails);
                              getDateDiff(validationDetails);
                              }catch (Exception e) {
                                    e.printStackTrace();
                              }
                  }

            /*    //swiftsfms
                  if(getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CRE"))
                  { int v;
                  try{
                        String Branch=getDriverWrapper().getEventFieldAsText("PRI", "p", "ss").trim();
                        Loggers.general().info(LOG,"Branch code ---->"+Branch);
                        v=getSWIFTSFMS(Branch);
                        if (v==1)
                        {
                              validationDetails.addError(ErrorType.Other, "Please select SFMS in Swift/Sfms [CM]");
                        }
                        if(v==2)
                        {
                              validationDetails.addError(ErrorType.Other, "Please select SWIFT in Swift/Sfms [CM]");
                        }
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"SwiftSfms--->" + e.getMessage());

                  }
                  }*/
                  
                  
                  }
      }

      private String getString(int i) {
            // TODO Auto-generated method stub
            return null;
      }

      // private boolean isChargeAccountDiff(Connection con) {
      // boolean isChargeAccountDiff = false;
      // PreparedStatement ps = null;
      // ResultSet rs = null;
      //
      // // //Loggers.general().info(LOG,"isChargeAccountDiff method Entered");
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
      // //Loggers.general().info(LOG,"Exception occured in isChargeAccountDiff - " +
      // e.getMessage());
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
      // //Loggers.general().info(LOG,"Connection Failed! Check output console");
      // e.printStackTrace();
      // }
      // }
      // return isChargeAccountDiff;
      // }

      
      public void getStampDuty() {
             try {
                   String tenor = getDriverWrapper().getEventFieldAsText("CDT", "s", "");
                  
                  String amount = getDriverWrapper().getEventFieldAsText("ORA", "v", "m");
                  String postrate = getDriverWrapper().getEventFieldAsText("cASE", "s", "");    
                  System.out.println("tenor amount "+tenor+" "+amount);
                  String stampDuty="0";
            if ((tenor != null && !tenor.equalsIgnoreCase("")&& !tenor.trim().isEmpty())&&(postrate != null && !postrate.equalsIgnoreCase("")&& !postrate.trim().isEmpty())) {
                  tenor = tenor.replaceAll("[^0-9]", "");
                  int t = Integer.parseInt(tenor);
                  BigDecimal amt = new BigDecimal(amount);
                  BigDecimal postrt = new BigDecimal(postrate);
                  amt=amt.multiply(postrt);
                  BigDecimal amt500 = new BigDecimal(500);
                  BigDecimal amt1000 = new BigDecimal(1000);
                  BigDecimal stamp = new BigDecimal(stampDuty);
                  BigDecimal rem = new BigDecimal(0);
                  
                  if(t<=90) {
                        if(amt.compareTo(amt500)==-1 || amt.compareTo(amt500)==0) {
                              getPane().setSTAMPDTY("1.25");
                              System.out.println("inside less than 500 "+stamp);
                        }else if (amt.compareTo(amt500)==1 && (amt.compareTo(amt1000)==-1||amt.compareTo(amt1000)==0)) {
                              getPane().setSTAMPDTY("2.50");
                              System.out.println("above 500 "+stamp);
                        }else if (amt.compareTo(amt1000)==1) {
                              stamp=amt.divide(amt1000);
                              rem=stamp.remainder(BigDecimal.ONE);
                              System.out.println("Reminder "+rem);
                              stamp=stamp.setScale(0,BigDecimal.ROUND_DOWN);                    
                              
                              if(rem.compareTo(BigDecimal.ZERO)==1) {
                                    stamp=stamp.add(BigDecimal.ONE);
                                    
                              }
                              stamp=stamp.multiply(new BigDecimal(2.50));
                              getPane().setSTAMPDTY(stamp.toString());
                              
                        }
                        }
                        if(t>90&&t<=180) {
                              if(amt.compareTo(amt500)==-1 || amt.compareTo(amt500)==0) {
                                    getPane().setSTAMPDTY("2.50");
                                    System.out.println("inside less than 500 "+stamp);
                              }else if (amt.compareTo(amt500)==1 && (amt.compareTo(amt1000)==-1||amt.compareTo(amt1000)==0)) {
                                    getPane().setSTAMPDTY("5.00");
                                    System.out.println("above 500 "+stamp);
                              }else if (amt.compareTo(amt1000)==1) {
                                    stamp=amt.divide(amt1000);
                                    rem=stamp.remainder(BigDecimal.ONE);
                                    System.out.println("Reminder "+rem);
                                    stamp=stamp.setScale(0,BigDecimal.ROUND_DOWN);                    
                                    
                                    if(rem.compareTo(BigDecimal.ZERO)==1) {
                                          stamp=stamp.add(BigDecimal.ONE);
                                          
                                    }
                                    stamp=stamp.multiply(new BigDecimal(5.00));
                                    getPane().setSTAMPDTY(stamp.toString());
                                    
                              }
                              }
                              if(t>180&&t<=270) {
                                    if(amt.compareTo(amt500)==-1 || amt.compareTo(amt500)==0) {
                                          getPane().setSTAMPDTY("3.75");
                                          System.out.println("inside less than 500 "+stamp);
                                    }else if (amt.compareTo(amt500)==1 && (amt.compareTo(amt1000)==-1||amt.compareTo(amt1000)==0)) {
                                          getPane().setSTAMPDTY("7.50");
                                          System.out.println("above 500 "+stamp);
                                    }else if (amt.compareTo(amt1000)==1) {
                                          stamp=amt.divide(amt1000);
                                          rem=stamp.remainder(BigDecimal.ONE);
                                          System.out.println("Reminder ");
                                          stamp=stamp.setScale(0,BigDecimal.ROUND_DOWN);                    
                                          
                                          if(rem.compareTo(BigDecimal.ZERO)==1) {
                                                stamp=stamp.add(BigDecimal.ONE);
                                                
                                          }
                                          stamp=stamp.multiply(new BigDecimal(7.50));
                                          getPane().setSTAMPDTY(stamp.toString());
                                          
                                    }
                                    }
                                    if(t>270&&t<=365) {
                                          if(amt.compareTo(amt500)==-1 || amt.compareTo(amt500)==0) {
                                                getPane().setSTAMPDTY("5.00");
                                                System.out.println(stamp);
                                          }else if (amt.compareTo(amt500)==1 && (amt.compareTo(amt1000)==-1||amt.compareTo(amt1000)==0)) {
                                                getPane().setSTAMPDTY("10.00");
                                                System.out.println(stamp);
                                          }else if (amt.compareTo(amt1000)==1) {
                                                stamp=amt.divide(amt1000);
                                                rem=stamp.remainder(BigDecimal.ONE);
                                                System.out.println("Reminder ");
                                                stamp=stamp.setScale(0,BigDecimal.ROUND_DOWN);                    
                                                
                                                if(rem.compareTo(BigDecimal.ZERO)==1) {
                                                      stamp=stamp.add(BigDecimal.ONE);
                                                      
                                                }
                                                stamp=stamp.multiply(new BigDecimal(10.00));
                                                getPane().setSTAMPDTY(stamp.toString());
                                                
                                          }

                                  }
                                    if(t>365) {
                                          if(amt.compareTo(amt500)==-1 || amt.compareTo(amt500)==0) {
                                                getPane().setSTAMPDTY("10.00");
                                                System.out.println(stamp);
                                          }else if (amt.compareTo(amt500)==1 && (amt.compareTo(amt1000)==-1||amt.compareTo(amt1000)==0)) {
                                                getPane().setSTAMPDTY("20.00");
                                                System.out.println(stamp);
                                          }else if (amt.compareTo(amt1000)==1) {
                                                stamp=amt.divide(amt1000);
                                                rem=stamp.remainder(BigDecimal.ONE);
                                                System.out.println("Reminder ");
                                                stamp=stamp.setScale(0,BigDecimal.ROUND_DOWN);                    
                                                
                                                if(rem.compareTo(BigDecimal.ZERO)==1) {
                                                      stamp=stamp.add(BigDecimal.ONE);
                                                      
                                                }
                                                stamp=stamp.multiply(new BigDecimal(20.00));
                                                getPane().setSTAMPDTY(stamp.toString());
                                                
                                          }

                                  }
                              }
            }
            
             catch(Exception e)
                  {
                   e.printStackTrace();

                  }
       }
      private int getInt(int i) {
            // TODO Auto-generated method stub
            return 0;
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

}