package com.misys.tiplus2.customisation.extension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
/*import org.apache.log4j.Logger;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventBOECAP;
import com.misys.tiplus2.customisation.entity.ExtEventCommissionTable;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceData;
import com.misys.tiplus2.customisation.entity.ExtEventAdvancePaymentDetails;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicelc;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.customisation.extension.EventExtension;

public class CpcoPcoc extends ConnectionMaster {
      private static final Logger LOG = LoggerFactory.getLogger(CustomValidation.class);
      // //Loggers.general().info(LOG,"eventRefNumber - "+eventRefNumber);
      Connection con, con1, localConnection, connection = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst, pes, peco, localPrepare, selectPrepare, prepared = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ress, resultSet, selectResult = null;

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
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //

                  // FCY Tax calculation
                  try {

                        // getFCCTCALCULATION();

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }
                  if (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC")) {
                        try {
                              // //Loggers.general().info(LOG,"get value for LOB");
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG, "Exception getLOBCREATE =====>" + ee.getMessage());
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }
                  }

                  // Purpose code population
                  String productyp = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  // //Loggers.general().info(LOG,"productyp " + productyp);
                  try {
                        getrbiPurcodeCode();

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in Purpose code---->" + e.getMessage());

                        }
                  }

//                try {
//                      currencyCalc();
//                } catch (Exception e) {
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());
//
//                      }
//                }

                  // cross currency conversion
                  // if (crosscurrency()) {
                  // // //Loggers.general().info(LOG,"cross currency conversion");
                  // } else {
                  // // Loggers.general().info(LOG," Else systemOutput");
                  // }
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  String rbipur = getWrapper().getOPURPOS_Name().trim();
                  if ((!rbipur.trim().equalsIgnoreCase("") || rbipur != null)
                              && (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {
                        try {
                              con = getConnection();
                              String query = "select RDRC from EXTRBIPOSE where RBICOD ='" + rbipur + "'";
                              // //Loggers.general().info(LOG,"EXTRBIPOSE-----> " + query);

                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String rbivalue = rs1.getString(1);
                                    // //Loggers.general().info(LOG,"value of count in while " +
                                    // rbivalue);
                                    getPane().setGPURDES_Name(rbivalue);
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

                  // workflow CHECKLIST
                  try {

                        EventPane pane = (EventPane) getPane();
                        pane.getExtEventBOECAPNew().setEnabled(false);
                        getPane().onRTGSOUTRMTCUSclayButton();

                        String Hyperreferel1 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTOUTRMTCUSclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMCHECKLISTOUTRMTcalyHyperlink();
                        csmreftrack2.setUrl(Hyperreferel1);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"workflow CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFERE TRACKING
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALOUTRMTCUSclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMREFRALOUTRMTcalyHyperlink();
                        csmreftrack1.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKOUTRMTCUSclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCCHECKOUTRMTcalyHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELOUTRMTCUSclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCREFRELOUTRMTcalyHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();

                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSOUTREMANULlayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSOUTWREMMAINlayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSOUTRMTCUSclayHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSOUTWBANKCanPaymeRespLayHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSOUTWBANKCanPaymeRequestLayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv6 = getPane().getCtlSFMSOUTWBANKPaymeRejReturnLayHyperlink();
                        sfmsExpAdv6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv7 = getPane().getCtlSFMSOUTWBANKCorrespondanceLayHyperlink();
                        sfmsExpAdv7.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv8 = getPane().getCtlSFMSOUTRMTcalyHyperlink();
                        sfmsExpAdv8.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv9 = getPane().getCtlSFMSOUTWREMCorrespondenceLayHyperlink();
                        sfmsExpAdv9.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv10 = getPane().getCtlSFMSOUTWREMCanPayRequestHyperlink();
                        sfmsExpAdv10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv11 = getPane().getCtlSFMSOUTWREMPayRejectionLayHyperlink();
                        sfmsExpAdv11.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv12 = getPane().getCtlSFMSOUTWREMCanPayResponseHyperlink();
                        sfmsExpAdv12.setUrl(getHyperSFMS);

                  } catch (Exception ee) {
                        // System.out.println(ee.getMessage());

                  }

                  // BOE HYPERLINK
                  try {
                        // //Loggers.general().info(LOG,"BOE LInk setting ");
                        String val = "BOE";

                        // String BoeLink = getBoeLink(val);
                        String BoeLink = getBoeLink(val);
                        // //Loggers.general().info(LOG,"BOE LInk is" + BoeLink);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlBOEOUTRMTCUSclayHyperlink();
                        dmsh.setUrl(BoeLink);
                        // Outawrd Bank Payment
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlBOEOUTRMTcalyHyperlink();
                        dmsh2.setUrl(BoeLink);
                  } catch (Exception e1) {
                        // System.out.println(e1.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTOUTRMTCUSclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTOUTWREMCorrespondenceLayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTOUTRMTcalyHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTOUTWBANKPaymeRejReturnLayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTOUTBANKMAINclayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTOUTWBANKCorrespondanceLayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTOUTWBANKCanPaymeRespLayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTOUTWREMCanPayRequestHyperlink();
                        dmsh7.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTOUTWBANKCanPaymeRequestLayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTOUTWREMPayRejectionLayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh10 = getPane().getCtlTSTOUTWREMMAINlayHyperlink();
                        dmsh10.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh11 = getPane().getCtlTSTOUTREMANULlayHyperlink();
                        dmsh11.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh12 = getPane().getCtlTSTOUTWREMCanPayResponseHyperlink();
                        dmsh12.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh13 = getPane().getCtlTSTOUTWBANKManullayHyperlink();
                        dmsh13.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        // System.out.println(ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTOUTRMTCUSclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // System.out.println(ees.getMessage());
                  }

                  // difference between shipment date and expiry date
                  // if (productyp.equalsIgnoreCase("DIR")) {
                  // try {
                  // String d = getDriverWrapper().getEventFieldAsText("CRVD", "d",
                  // "");
                  // // //Loggers.general().info(LOG," shipment values------>" + d);
                  // // String accept =
                  // // getDriverWrapper().getEventFieldAsText("FPP:XPT", "s",
                  // // "");
                  // String d1 = getWrapper().getSHTDAT();
                  // // //Loggers.general().info(LOG,"accept shipment values------>" +
                  // // d1);
                  // if (d1.length() > 0 && d.length() > 0 && d!=null && d1!=null) {
                  // // //Loggers.general().info(LOG,"date of shipment values" + d1);
                  // getPane().setNUMDAYS(difference(d, d1).substring(0, difference(d,
                  // d1).indexOf(".")));
                  // } else {
                  // // Loggers.general().info(LOG,"else part of date difference
                  // // values");
                  // }
                  // } catch (Exception e) {
                  // e.printStackTrace();
                  // }
                  // } else {
                  // // Loggers.general().info(LOG,"product type not DIR");
                  // }

                  // customer type populate

                  try {
                        String cutype = getDriverWrapper().getEventFieldAsText("PRM", "p", "cct");
                        // //Loggers.general().info(LOG,"customer type---->" + cutype);
                        getPane().setCUSTYPE(cutype);
                        // //Loggers.general().info(LOG,"customer type after set---->" +
                        // getWrapper().getCUSTYPE());

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"customer type populate--------->" +
                        // ees.getMessage());
                  }
                  String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
                  if (rtgsFlag.equalsIgnoreCase("N") && !rtgsFlag.equalsIgnoreCase("Y")) {
                        getPane().setRTGNFT("");
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
                        getPane().setUTRNO("");
                        getPane().setRTGSNEFT("");
                        getPane().setNARRTVE("");

                  }

                  String portdes = getWrapper().getPOSTDIS();
                  // //Loggers.general().info(LOG,"Port of destination Value---->" +
                  // portdes);
                  if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
                        try {
                              String portvalqury = "select trim(PODEST) from EXTPORTDESTINATION WHERE PODEST='" + portdes + "'";
                              // //Loggers.general().info(LOG,"port code destination query
                              // Value---->" +
                              // portvalqury);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(portvalqury);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);

                                    getPane().setPODESCP(hsploy);
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
                  } else {
                        // //Loggers.general().info(LOG,"port code is destination empty");
                  }

                  String portload = getWrapper().getPORLOD();
                  // //Loggers.general().info(LOG,"Port of destination Value---->" +
                  // portdes);
                  if ((!portload.equalsIgnoreCase("")) && portload != null) {
                        try {
                              String portvalqury = "select Trim(PORTDESC) from EXTPORTODLOAD where PORTLOAD='" + portload + "'";
                              // //Loggers.general().info(LOG,"port code destination query
                              // Value---->" + portvalqury);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(portvalqury);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1).trim();
                                    // //Loggers.general().info(LOG,"port code description---->"
                                    // +
                                    // hsploy);
                                    getPane().setPOLOADES(hsploy);
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
//TCS ON LRS 01-10-21               
                  if (getMajorCode().equalsIgnoreCase("CPCO") && getMinorCode().equalsIgnoreCase("PCOC")) {
                        System.out.println("TCS on LRS");

                        String masRef = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                        String eveRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
                        String prodType = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                        if (prodType.equalsIgnoreCase("64R") || prodType.equalsIgnoreCase("65R")) {
                              // remittanceLRSChargeChange(masRef, eveRef);

                        }
                  }
                  if (getMajorCode().equalsIgnoreCase("CPCO")
                              || getMajorCode().equalsIgnoreCase("CPBO") && getMinorCode().equalsIgnoreCase("PCOC")
                              || getMinorCode().equalsIgnoreCase("PBOC")) {
                        try {
                              freezeMttNumber();
                              System.out.println("INSIDE freezeMttNumber OF CREATE");
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  }

                  // if (rtgspart.equalsIgnoreCase("FULL") &&
                  // !eventStatus.equalsIgnoreCase("Completed")) {
                  // try {
                  // con = getConnection();
                  // String sql = "SELECT * from KMB_RTGS_NEFT_VIEW where MASTER_REF =
                  // '" + MasterReference
                  // + "' AND EVENT_REF = '" + eventCode + "'";
                  // // Loggers.general().info(LOG,"Query value for RTGS amount--->" +
                  // // sql);
                  // ps = con.prepareStatement(sql);
                  // rs = ps.executeQuery();
                  // while (rs.next()) {
                  // String rtgsVal = rs.getString(3);
                  // getPane().setRTGSNEFT(rtgsVal + " INR");
                  // getWrapper().setRTGSNEFT(rtgsVal + " INR");
                  //
                  // }
                  //
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"Exception update" + e.getMessage());
                  // } finally {
                  // try {
                  // if (con != null) {
                  // con.close();
                  // if (ps != null)
                  // ps.close();
                  // if (ps2 != null)
                  // ps2.close();
                  // if (rs != null)
                  // rs.close();
                  // }
                  // } catch (SQLException e) {
                  // // Loggers.general().info(LOG,"Connection Failed! Check output
                  // // console");
                  // e.printStackTrace();
                  // }
                  // }
                  // } else {
                  // Loggers.general().info(LOG,"RTGS/NEFT type is part");

                  // }

            }
            return false;
      }

      @SuppressWarnings({ "unchecked", "unused" })
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
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);
                  String productyp = getDriverWrapper().getEventFieldAsText("PTP", "s", "");

                  // FCY Tax calculation
                  if ((getMajorCode().equalsIgnoreCase("CPBO") || getMajorCode().equalsIgnoreCase("CPCO"))
                              && (getMinorCode().equalsIgnoreCase("PBOC") || getMinorCode().equalsIgnoreCase("PCOC"))) {

                        try {
                              System.out.println("inside fcct details");
                              getFCCTCALCULATION();
                              getFcctDetails(validationDetails);
                              System.out.println("outside fcct details");

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" +
                              // e.getMessage());
                              e.printStackTrace();
                        }
                  }
                  try {
                        getPane().onEXITFETCHOUTRMTCUSclayButton();
                        getPane().onRTGSOUTRMTCUSclayButton();
                        getPane().onNOSTROOUTWREMCorrespondenceLayButton();

                  } catch (Exception e2) {
                        e2.printStackTrace();
                  }

                  String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
                  if (rtgsFlag.equalsIgnoreCase("N") && !rtgsFlag.equalsIgnoreCase("Y")) {
                        getPane().setRTGNFT("");
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
                        getPane().setUTRNO("");
                        getPane().setRTGSNEFT("");
                        getPane().setNARRTVE("");

                  }

                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

                  // workflow CHECKLIST
                  try {

                        EventPane pane = (EventPane) getPane();
                        pane.getExtEventBOECAPNew().setEnabled(false);
                        getPane().onRTGSOUTRMTCUSclayButton();

                        String Hyperreferel1 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTOUTRMTCUSclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMCHECKLISTOUTRMTcalyHyperlink();
                        csmreftrack2.setUrl(Hyperreferel1);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"workflow CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFERE TRACKING
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALOUTRMTCUSclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMREFRALOUTRMTcalyHyperlink();
                        csmreftrack1.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKOUTRMTCUSclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCCHECKOUTRMTcalyHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELOUTRMTCUSclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCREFRELOUTRMTcalyHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();

                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSOUTREMANULlayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSOUTWREMMAINlayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSOUTRMTCUSclayHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSOUTWBANKCanPaymeRespLayHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSOUTWBANKCanPaymeRequestLayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv6 = getPane().getCtlSFMSOUTWBANKPaymeRejReturnLayHyperlink();
                        sfmsExpAdv6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv7 = getPane().getCtlSFMSOUTWBANKCorrespondanceLayHyperlink();
                        sfmsExpAdv7.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv8 = getPane().getCtlSFMSOUTRMTcalyHyperlink();
                        sfmsExpAdv8.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv9 = getPane().getCtlSFMSOUTWREMCorrespondenceLayHyperlink();
                        sfmsExpAdv9.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv10 = getPane().getCtlSFMSOUTWREMCanPayRequestHyperlink();
                        sfmsExpAdv10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv11 = getPane().getCtlSFMSOUTWREMPayRejectionLayHyperlink();
                        sfmsExpAdv11.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv12 = getPane().getCtlSFMSOUTWREMCanPayResponseHyperlink();
                        sfmsExpAdv12.setUrl(getHyperSFMS);

                  } catch (Exception ee) {
                        // System.out.println(ee.getMessage());

                  }

                  // BOE HYPERLINK
                  try {
                        // //Loggers.general().info(LOG,"BOE LInk setting ");
                        String val = "BOE";

                        // String BoeLink = getBoeLink(val);
                        String BoeLink = getBoeLink(val);
                        // //Loggers.general().info(LOG,"BOE LInk is" + BoeLink);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlBOEOUTRMTCUSclayHyperlink();
                        dmsh.setUrl(BoeLink);
                        // Outawrd Bank Payment
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlBOEOUTRMTcalyHyperlink();
                        dmsh2.setUrl(BoeLink);
                  } catch (Exception e1) {
                        // System.out.println(e1.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTOUTRMTCUSclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTOUTWREMCorrespondenceLayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTOUTRMTcalyHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTOUTWBANKPaymeRejReturnLayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTOUTBANKMAINclayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTOUTWBANKCorrespondanceLayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTOUTWBANKCanPaymeRespLayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTOUTWREMCanPayRequestHyperlink();
                        dmsh7.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTOUTWBANKCanPaymeRequestLayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTOUTWREMPayRejectionLayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh10 = getPane().getCtlTSTOUTWREMMAINlayHyperlink();
                        dmsh10.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh11 = getPane().getCtlTSTOUTREMANULlayHyperlink();
                        dmsh11.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh12 = getPane().getCtlTSTOUTWREMCanPayResponseHyperlink();
                        dmsh12.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // System.out.println(ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTOUTRMTCUSclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // System.out.println(ees.getMessage());
                  }

                  // New Code for Consultancy starts here
                  try {

                        if (getMajorCode().equalsIgnoreCase("CPCO") && getMinorCode().equalsIgnoreCase("PCOC")) {
                              // Comes in only if it is a Inward Remittance Create
                              String productSubType = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                              // //Loggers.general().info(LOG,"Product Sub Type is " +
                              // productSubType);
                              if (productSubType.equalsIgnoreCase("OTT")) {
                                    // Enteres only if the Sub-product Type is AIR or DIR
                                    String goodscategory = getDriverWrapper().getEventFieldAsText("PUL1", "s", "");
                                    // //Loggers.general().info(LOG,"Goods Category is " +
                                    // goodscategory);
                                    if (goodscategory.equalsIgnoreCase("Consultancy Others")
                                                || goodscategory.equalsIgnoreCase("Consultancy Infra")) {
                                          // Enteres only if the Goods Cateogory is
                                          // Consultancy
                                          // Infra or Consultancy Others
                                          String projectNo = getWrapper().getNEWPRO().trim();
                                          // Loggers.general().info(LOG,"Project Number is " +
                                          // projectNo);
                                          if (null != projectNo && !projectNo.equalsIgnoreCase("")) {
                                                int projectCount = 0;

                                                try {
                                                      // For Database Connection
                                                      localConnection = ConnectionMaster.getConnection();
                                                      String query = "SELECT COUNT(*) FROM CONSULTANCY_PROJECTS WHERE PRONAME=?";
                                                      localPrepare = localConnection.prepareStatement(query);
                                                      localPrepare.setString(1, projectNo);
                                                      resultSet = localPrepare.executeQuery();
                                                      if (resultSet.next()) {
                                                            projectCount = resultSet.getInt(1);
                                                      }
                                                      // //Loggers.general().info(LOG,"Project Count Value
                                                      // is "
                                                      // + projectCount);
                                                      if (projectCount == 0) {
                                                            // //Loggers.general().info(LOG,"Entered if
                                                            // Since
                                                            // Project Does nOt exist");
                                                            PreparedStatement insertPrepare = null;
                                                            try {
                                                                  // Inserting the new Project
                                                                  String insertQuery = "INSERT INTO CONSULTANCY_PROJECTS(PRONAME,PROTYPE) VALUES('"
                                                                              + projectNo + "','" + goodscategory + "')";
                                                                  // //Loggers.general().info(LOG,"INsert
                                                                  // Query
                                                                  // is "
                                                                  // + insertQuery);
                                                                  insertPrepare = localConnection.prepareStatement(insertQuery);
                                                                  /*
                                                                   * insertPrepare.setString(1, projectNo); insertPrepare.setString(1,
                                                                   * goodscategory);
                                                                   */
                                                                  int insetrResult = insertPrepare.executeUpdate();
                                                                  // //Loggers.general().info(LOG,"Result of
                                                                  // insertion is " + insetrResult);
                                                            } catch (Exception e) {
                                                                  // Loggers.general().info(LOG,"Exception in
                                                                  // Insert Query");

                                                                  // TODO Auto-generated catch block
                                                                  e.printStackTrace();
                                                            }
                                                      } else {
                                                            // Loggers.general().info(LOG,"Project Refernec
                                                            // Already Exist");

                                                            try {
                                                                  String selectQuery = "SELECT COUNT(*) FROM CONSULTANCY_PROJECTS WHERE PRONAME='"
                                                                              + projectNo + "' AND TRIM(PROTYPE)='" + goodscategory + "'";
                                                                  // //Loggers.general().info(LOG,"Select
                                                                  // Query
                                                                  // " +
                                                                  // selectQuery);
                                                                  selectPrepare = localConnection.prepareStatement(selectQuery);
                                                                  /*
                                                                   * selectPrepare.setString(1, projectNo); selectPrepare.setString(1,
                                                                   * goodscategory);
                                                                   */
                                                                  selectResult = selectPrepare.executeQuery();
                                                                  if (selectResult.next()) {
                                                                        if (selectResult.getInt(1) == 0) {
                                                                              validationDetails.addError(ErrorType.Other,
                                                                                          "This Project Reference Number does not match with Goods Category Selected [CM]");
                                                                        } else {
                                                                              // Loggers.general().info(LOG,"Goods
                                                                              // Code is set as Correct");
                                                                        }
                                                                  } else {
                                                                        // Loggers.general().info(LOG,"Result
                                                                        // set No value");
                                                                  }
                                                            } catch (Exception e) {
                                                                  // Loggers.general().info(LOG,"Exception in
                                                                  // validation Consultancy " +
                                                                  // e.getMessage());
                                                                  // TODO Auto-generated catch block
                                                                  e.printStackTrace();
                                                            }
                                                      }
                                                } catch (Exception e) {
                                                      // Loggers.general().info(LOG,"Exception in SQL
                                                      // Connection " + e.getMessage());
                                                }
                                          }
                                    }
                              } else {
                                    // Loggers.general().info(LOG,"Project Sub-Type is not AIR or
                                    // DIR");

                              }
                        }
                  } catch (Exception e) {
                        e.printStackTrace();
                  } finally {
                        try {

                              if (selectResult != null)
                                    selectResult.close();
                              if (selectPrepare != null)
                                    selectPrepare.close();
                              if (localConnection != null)
                                    localConnection.close();

                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  }

                  // cross currency conversion
                  // if (crosscurrency()) {
                  // // //Loggers.general().info(LOG,"cross currency conversion");
                  // } else {
                  // // Loggers.general().info(LOG," Else systemOutput");
                  // }

                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  // NOSTRO VALIDATION

                  String goodsDetails = getWrapper().getBENEFI();
                  // System.out.println( "goodsDetails population initial" +
                  // goodsDetails + "goodsDetails lengh" + goodsDetails.length());
                  if (productyp.equalsIgnoreCase("OTT") || productyp.equalsIgnoreCase("ODI")) {
                        if (goodsDetails.equalsIgnoreCase("") && goodsDetails.length() < 1) {
                              // Loggers.general().info(LOG,"goodsDetails population if loop" +
                              // goodsDetails);
                              getPane().setBENEFI("NO");

                        } else if (!goodsDetails.equalsIgnoreCase("") || goodsDetails.length() > 0) {
                              String goodsDet = getWrapper().getBENEFI();
                              // Loggers.general().info(LOG,"goodsDetails population else loop" +
                              // goodsDet);
                              getPane().setBENEFI(goodsDet);
                        }
                  }

                  try {
                        String goodsCode = getWrapper().getGOODT().trim();
                        if (!goodsCode.trim().equalsIgnoreCase("") && goodsCode != null) {
                              con = getConnection();
                              String query = "select trim(DESCRI73) from GOODSC87 where CODE79 ='" + goodsCode + "'";
                              // //Loggers.general().info(LOG,"EXTRBIPOSE-----> " + query);

                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String goodvalue = rs1.getString(1);
                                    Loggers.general().info(LOG, "Good description for goods code" + goodvalue);
                                    getPane().setDESGOODS(goodvalue);
                              }
                        } else if (goodsCode.trim().equalsIgnoreCase("") || goodsCode == null) {
                              getPane().setDESGOODS("");
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
                  String cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "no").trim();
                  try {
                        String mercht = getDriverWrapper().getEventFieldAsText("cBHW", "l", "").toString();
                        String masRefNo = getWrapper().getPREBUYRE().trim();

                        int dmT = 0;

                        String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                                    + masRefNo + "' AND mas.PRICUSTMNM ='" + cust + "'";
                        // Loggers.general().info(LOG,"Master ref no valid for Import lc" +
                        // dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Master ref number valid query" + dms);

                        }
                        con = ConnectionMaster.getConnection();
                        ps = con.prepareStatement(dms);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                              dmT = rs.getInt(1);
                              // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Master ref no valid count" + dmT);

                        }

                        if ((dmT == 0 || dmT < 1) && masRefNo.length() > 0 && mercht.equalsIgnoreCase("Y")
                                    && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                              validationDetails.addWarning(WarningType.Other,
                                          "Buyer's Credit reference number is not valid number,Kinldy check the valid reference number with CRN [CM]");

                        }

                        else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Master ref no valid in else" + dmT + "buyer credit check box" + mercht);

                              }
                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in Master ref no validation" + e.getMessage());
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
                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                  /*
                   * try {
                   *
                   * con = getConnection();
                   *
                   * String query =
                   * "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO') AND pos.ORIGAMTCCY NOT IN 'INR' AND pos.KEY97 IN (SELECT MAX(pos.KEY97) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO') AND pos.ORIGAMTCCY NOT IN 'INR' AND mas.MASTER_REF = '"
                   * + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt +
                   * "' AND bev.REFNO_SERL ='" + evvcount + "' ) AND mas.MASTER_REF = '" +
                   * MasterReference + "' AND bev.REFNO_PFIX ='" + evnt +
                   * "' AND bev.REFNO_SERL ='" + evvcount + "'"; //
                   * Loggers.general().info(LOG,"Query for NOSTRO VALIDATION" + query); ps1 =
                   * con.prepareStatement(query); rs1 = ps1.executeQuery(); while (rs1.next()) {
                   * // //Loggers.general().info(LOG,"Entered while"); String cn =
                   * rs1.getString(1).trim();
                   *
                   * String buycrd = getDriverWrapper().getEventFieldAsText("cBHW", "l", ""); if
                   * (buycrd.equalsIgnoreCase("N") && (!buycrd.equalsIgnoreCase("Y")) &&
                   * cn.equalsIgnoreCase("CN") &&
                   * (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.
                   * equalsIgnoreCase("CBS Authoriser")) ) {
                   *
                   * validationDetails.addWarning(WarningType.Other,
                   * "Pay and Receive should not have Nostro account  [CM]"); }
                   *
                   * else { // Loggers.general().info(LOG,"value of nostro else---->" + cn); }
                   *
                   * }
                   *
                   * } catch (Exception e1) { // Loggers.general().info(LOG,"Exception for Nostro"
                   * + e1.getMessage()); }
                   *
                   * finally { try { if (rs1 != null) rs1.close(); if (ps1 != null) ps1.close();
                   * if (con != null) con.close(); } catch (SQLException e) { //
                   * Loggers.general().info(LOG,"Connection Failed! Check output // console");
                   * e.printStackTrace(); } }
                   */
                  try {

                        con = getConnection();

                        String query = "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO') AND pos.ORIGAMTCCY NOT IN 'INR' AND pos.KEY97 IN (SELECT pos.KEY97 FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO') AND pos.ORIGAMTCCY NOT IN 'INR' AND mas.MASTER_REF = '"
                                    + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt + "' AND bev.REFNO_SERL ='" + evvcount
                                    + "' ) AND mas.MASTER_REF = '" + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt
                                    + "' AND bev.REFNO_SERL ='" + evvcount
                                    + "' and trim(pos.NETS_INTO) is null AND trim(pos.REPLACEDBY) is null AND pos.AMOUNT<>0 and pos.ACC_TYPE='CN'";
                        Loggers.general().info(LOG, "Query for NOSTRO VALIDATION" + query);
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              Loggers.general().info(LOG, "Entered while");
                              String cn = rs1.getString(1).trim();

                              String buycrd = getDriverWrapper().getEventFieldAsText("cBHW", "l", "");
                              String nost_debt = getDriverWrapper().getEventFieldAsText("cAPE", "l", "");
                              if (nost_debt.equalsIgnoreCase("N") && (!nost_debt.equalsIgnoreCase("Y"))
                                          && cn.equalsIgnoreCase("CN")
                                          && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Reject")
                                                      || step_csm.equalsIgnoreCase("CBS Authoriser"))) {

                                    validationDetails.addError(ErrorType.Other,
                                                "Pay and Receive should not have Nostro account  [CM]");
                              }

                              else {
                                    // Loggers.general().info(LOG,"value of nostro else---->" + cn);
                              }

                        }

                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception for Nostro" + e1.getMessage());
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
                        if (rbipur.length() > 0) {
                              String str = rbipur.substring(0, 1);
                              if (((!str.equalsIgnoreCase("S")) || (str.equalsIgnoreCase("P")))
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addError(ErrorType.Other, "RBI purpose code should start with S  [CM]");

                              } else {
                                    // Loggers.general().info(LOG,"Purpose code is S----> " + str);
                              }
                        } else {
                              // Loggers.general().info(LOG,"Purpose code is empty---->");
                        }

                        if (!rbipur.trim().equalsIgnoreCase("") || rbipur != null) {
                              try {
                                    con = getConnection();
                                    String query = "select RDRC from EXTRBIPOSE where RBICOD ='" + rbipur + "'";
                                    // //Loggers.general().info(LOG,"EXTRBIPOSE-----> " + query);

                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String rbivalue = rs1.getString(1);
                                          // //Loggers.general().info(LOG,"value of count in while " +
                                          // rbivalue);
                                          getPane().setGPURDES_Name(rbivalue);
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
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        }
                  } catch (Exception e1) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in Purpose code---->" + e1.getMessage());

                        }
                  }
                  // String ctr = getWrapper().getTableRowCount;
                  // String rowcot =
                  // EventExtension.GetTablePartEventFieldString("FXD", ctr, "SALV");
                  // Notes Populated in Summary

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

                  // -----------*********-------------
                  try {
                        String goods = getWrapper().getGOODT().trim();
                        String m = "";
                        // //Loggers.general().info(LOG,"Goods value -->" + goods);
                        if (goods.length() > 0) {
                              String str2 = goods.replaceAll("\\s", "");
                              m = str2.toUpperCase();
                        }
                        // //Loggers.general().info(LOG,"Goods value in m " + m);
                        int count = 0;
                        for (int i = 0; i < m.length(); i++) {
                              // if(m.charAt(i)==('A'|'B'|'C'|'D'|'E'|'F'|'G'|'H'|'I'|'J'|'K'|'L'|'M'|'N'|'O'|'P'|'Q'|'R'|'S'|'T'|'U'|'V'|'W'|'X'|'Y'|'Z'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'|'0'|'.')){
                              if (Character.toString(m.charAt(i)).matches("[A-Z]")
                                          || Character.toString(m.charAt(i)).matches("[0-9]") || m.charAt(i) == '.') {
                                    // //Loggers.general().info(LOG,"values---->" + m.charAt(i));
                              } else {
                                    // Loggers.general().info(LOG,"char value" + m.charAt(i));

                                    count++;

                              }
                        }
                        if (count > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                                    && (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))) {
                              validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                          "Goods Details Not allowed special character  [CM]");
                        }

                        else {
                              // Loggers.general().info(LOG,"count value is 0");
                        }
                  } catch (Exception e) {

                  }

                  // workflow error configuration
                  try {
                        int count3 = 0;

                        if (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("AdhocCSM")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CSM'";
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count3 = rs1.getInt(1);
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
                                    count3 = rs1.getInt(1);
                                    // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);

                              }
                        }
                        // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING----> " +
                        // query_wrk);

                        if (count3 < 1 && step_Input.equalsIgnoreCase("i")
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("AdhocCSM"))
                                    && (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))) {

                              validationDetails.addError(ErrorType.Other, "Workflow checklist is mandatory  [CM]");
                        }

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
                  // BOE mandatory for DIR
                  int count5 = 0;
                  if (productyp.equalsIgnoreCase("DIR")) {
                        try {
                              con = getConnection();
                              String query = "select count(*) from ETT_BOE_PAYMENT where BOE_PAYMENT_BP_PAY_REF ='"
                                          + MasterReference + "' and BOE_PAYMENT_BP_PAY_PART_REF='" + eventCode + "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Query for ETT_BOE_PAYMENT " + query);
                              }

                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count5 = rs1.getInt(1);
                                    // Loggers.general().info(LOG,"value of count in while
                                    // authorization" + count5);
                              }

                              if (count5 < 1 && (step_csm.equalsIgnoreCase("CBS Maker 1")) && step_Input.equalsIgnoreCase("i")) {

                                    validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                                "BOE link is mandatory for DIR  [CM]");
                              }

                              else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "BOE link in else" + count5);
                                    }
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
                  } else {
                        // Loggers.general().info(LOG,"BOE link not in DIR====>" + productyp);
                  }

                  try {
                        con = getConnection();
                        String query1 = "SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_PAYMENT  WHERE BOE_PAYMENT_BP_PAY_REF  ='"
                                    + MasterReference + "' AND BOE_PAYMENT_BP_PAY_PART_REF ='" + eventCode + "' AND STATUS = 'P'";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "BOE Authorization is pending Query " + query1);
                        }
                        int count6 = 0;
                        ps1 = con.prepareStatement(query1);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              count6 = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while
                              // authorization" + count5);
                              if (count6 > 0
                                          && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise"))
                                          && productyp.equalsIgnoreCase("DIR")) {

                                    validationDetails.addError(ErrorType.Other,
                                                "BOE Authorization is pending for this Entity [CM]");
                              }

                              else {
                                    // Loggers.general().info(LOG,"BOE link in else" +
                                    // count5);
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
                  // IE Code validation
                  String iecode = getDriverWrapper().getEventFieldAsText("PRM", "p", "cBBF").trim();
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "IE code is from customer details" + iecode);
                  }

                  if ((iecode.equalsIgnoreCase("") || iecode == null) && (step_Input.equalsIgnoreCase("i"))
                              && (!getMinorCode().equalsIgnoreCase("PBOC"))
                              && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
                              && (productyp.equalsIgnoreCase("DIR") || productyp.equalsIgnoreCase("AIR"))) {
                        validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                    "IE code is not Updated in the customer [CM]");

                  } else

                  {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "IE code is from customer details else" + iecode);
                        }
                  }
                  /*
                   * if ((iecode.equalsIgnoreCase("") || iecode == null) &&
                   * (step_Input.equalsIgnoreCase("i"))&&(getMinorCode().equalsIgnoreCase("PBOC"))
                   * && (step_csm.equalsIgnoreCase("CBS Maker") ||
                   * step_csm.equalsIgnoreCase("CBS Maker 1")) &&
                   * (productyp.equalsIgnoreCase("DIR") || productyp.equalsIgnoreCase("AIR"))) {
                   * validationDetails.addError(ErrorType.Other,
                   * "IE code is not Updated in the customer [CM]");
                   *
                   * } else
                   *
                   * { if (dailyval_Log.equalsIgnoreCase("YES")) {
                   * Loggers.general().info(LOG,"IE code is from customer details else" + iecode);
                   * } }
                   */

                  String branch = getDriverWrapper().getEventFieldAsText("PRM", "p", "cAIW").trim();
                  String behalf = getDriverWrapper().getEventFieldAsText("BOB", "s", "").trim();
                  // Loggers.general().info(LOG,"Customer details branch" + branch + "behalf"
                  // + behalf);
                  if (!branch.equalsIgnoreCase(behalf) && (step_Input.equalsIgnoreCase("i"))
                              && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                    "Remittance is being booked in branch different than the home branch of the Customer [CM]");

                  } else

                  {
                        // Loggers.general().info(LOG,"Remittance is being booked in branch
                        // different than the home branch of the Customer");
                  }

                  // Shipment date DATE VALIDATION
                  // try {
                  //
                  // // //Loggers.general().info(LOG,"Shipment Date Validation");
                  //
                  // String t = getWrapper().getSHTDAT();
                  // String i = getDriverWrapper().getEventFieldAsText("TDY", "d",
                  // "");
                  // // //Loggers.general().info(LOG,"value of issue date "+i);
                  // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  // Date date1 = sdf.parse(t.trim());
                  // Date date2 = sdf.parse(i.trim());
                  // if (date1.compareTo(date2) > 0 &&
                  // (step_Input.equalsIgnoreCase("i"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // //
                  // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                  // // "Shipment date cannot be future date " +t+"[CM]" );
                  // }
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"Exception " + e.getMessage());
                  // }

                  // invoice details validation

                  if ((productyp.equalsIgnoreCase("DIR") || productyp.equalsIgnoreCase("AIR"))
                              && (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))
                              && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        List<ExtEventInvoiceData> Invoic = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();
                        // //Loggers.general().info(LOG,"Invoice Validate Called");
                        if (Invoic.size() < 1 && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addError(ErrorType.Other, "Invoice details is mandatory  [CM]");
                        } else {
                              // Loggers.general().info(LOG,"Invoice data is not empty");
                        }

                  }

                  List<ExtEventInvoiceData> Invoic = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();
                  // //Loggers.general().info(LOG,"Invoice Validate Called");
                  int count1 = 0;
                  for (int k = 0; k < Invoic.size(); k++) {
                        ExtEventInvoiceData invoicedat = Invoic.get(k);
                        String invnum = String.valueOf(invoicedat.getINVNUMB());
                        String datav = invnum.toUpperCase();
                        // //Loggers.general().info(LOG,"values of invoice number---> " +
                        // datav);
                        /* while(count==0){ */
                        for (int m1 = 0; m1 < datav.length(); m1++) {
                              if (Character.toString(datav.charAt(m1)).matches("[A-Z]")
                                          || Character.toString(datav.charAt(m1)).matches("[0-9]")
                                          || Character.toString(datav.charAt(m1)).matches("[/]")) {
                                    // //Loggers.general().info(LOG,"through invoice number ");
                              } else {
                                    // Loggers.general().info(LOG,"through invoice number " +
                                    // count1);
                                    count1++;
                              }

                        }
                  }

                  // }
                  if (count1 > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                              && (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))) {
                        validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                    "Invalid invoice number format special charactor not allowed  [CM]");
                  }

                  // Purpose code population

                  try {
                        getrbiPurcodeCode();

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in purpose code--------->" + e.getMessage());
                        }
                  }

                  if (productyp.equalsIgnoreCase("EAR")) {
                        getPane().setOPURPOS_Name("S1501");
                  }
                  // MATER NO IS AVAILABLE OR NOT

                  String inwardremNO = getWrapper().getINONOS();

                  if (!inwardremNO.trim().equalsIgnoreCase("") && inwardremNO != null && inwardremNO.length() > 0) {
                        try {
                              con = ConnectionMaster.getConnection();
                              String query = "select decode(trim(mas1.MASTER_REF) ,null ,0,1)as InwardNo from master mas,BASEEVENT bev, EXTEVENT exte,master mas1,PRODTYPE prod where mas.KEY97 = bev.MASTER_KEY and  bev.KEY97  =  exte.EVENT and mas1.PRODTYPE =  prod.KEY97 and prod.CODE ='XAR' and mas1.MASTER_REF ='"
                                          + inwardremNO + "'";
                              // Loggers.general().info(LOG,"query for master ref search " +
                              // query);
                              int intcount = 0;
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();

                              if (rs1.next()) {
                                    intcount = rs1.getInt(1);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "value of count in inward remittance " + intcount);
                                    }

                              }
                              if (intcount > 0) {

                                    String USD_amt = "";
                                    String advrem = "";
                                    String rec_date = "";

                                    try {
                                          String query_adv = "SELECT mas.MASTER_REF, (ROUND(THEME_GENIUS_PKG.CONVAMT(mas.ccy,mas.AMT_O_S )* cer1.EXCH_RATE/cer2.EXCH_RATE,2) - a.util_amt)*100 balance , prt.CUS_MNM, ROUND(THEME_GENIUS_PKG.CONVAMT(cpm.pay_ccy,cpm.PAY_AMT)* cer1.EXCH_RATE/cer2.EXCH_RATE,2) AS pay_usd_amt,TO_CHAR(cpm.RCV_DATE,'DD/MM/YY') AS REV_DATE FROM master mas, (SELECT INWARD, NVL(SUM(THEME_GENIUS_PKG.CONVAMT(ccy_1,extv.AMTUTIL )),0) util_amt FROM EXTEVENTADV extv GROUP BY INWARD )a, CPAYMASTER cpm, PARTYDTLS prt, TIDATAITEM tid, CCY_EXCH_RATE_VIEW cer1, CCY_EXCH_RATE_VIEW cer2, gfpf WHERE mas.key97 = tid.MASTER_KEY AND tid.KEY97 = prt.KEY97 AND mas.KEY97 = cpm.KEY97 AND prt.ROLE = 'BEN' AND prt.CUS_MNM = gfpf.gfcus1(+) AND mas.MASTER_REF = a.INWARD AND cpm.PAY_CCY = cer1.CURR_CODE AND cer2.CURR_CODE = 'USD' AND mas.STATUS   <> 'NEW' AND mas.MASTER_REF ='"
                                                      + inwardremNO + "'";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "populated USD and customer used in advanced--->" + query_adv);
                                          }

                                          ps1 = con.prepareStatement(query_adv);
                                          rs1 = ps1.executeQuery();
                                          if (rs1.next()) {
                                                advrem = rs1.getString(2);
                                                String customer = rs1.getString(3);
                                                rec_date = rs1.getString(5);
                                                if (advrem.length() > 0) {
                                                      getPane().setADVAMNT(advrem + " " + "USD");
                                                }
                                                getPane().setADVCUT(customer);
                                                getPane().setOPURPOS_Name("S1501");
                                                getPane().setADRECDT(rec_date);

                                          }

                                          else {

                                                String own_Value = "SELECT mas.MASTER_REF, THEME_GENIUS_PKG.CONVAMT( mas.CCY,mas.AMOUNT) master_amount , mas.CCY master_ccy, THEME_GENIUS_PKG.CONVAMT(cpm.pay_ccy,cpm.PAY_AMT) pay_amt, prt.CUS_MNM, (ROUND(THEME_GENIUS_PKG.CONVAMT(cpm.pay_ccy,cpm.PAY_AMT)* cer1.EXCH_RATE/cer2.EXCH_RATE,2)*100) AS pay_usd_amt,TO_CHAR(cpm.RCV_DATE,'DD/MM/YY') AS REV_DATE FROM master mas, CPAYMASTER cpm, PARTYDTLS prt, TIDATAITEM tid, CCY_EXCH_RATE_VIEW cer1, CCY_EXCH_RATE_VIEW cer2, gfpf WHERE mas.key97 = tid.MASTER_KEY AND tid.KEY97 = prt.KEY97 AND mas.KEY97 = cpm.KEY97 AND prt.ROLE = 'BEN' AND prt.CUS_MNM = gfpf.gfcus1(+) AND cpm.PAY_CCY = cer1.CURR_CODE AND cer2.CURR_CODE = 'USD' AND mas.STATUS   <> 'NEW' AND mas.MASTER_REF ='"
                                                            + inwardremNO + "'";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "populated USD and customer in else--->" + own_Value);
                                                }
                                                ps1 = con.prepareStatement(own_Value);
                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      String USD_cust = rs1.getString(5);
                                                      USD_amt = rs1.getString(6);
                                                      rec_date = rs1.getString(7);
                                                      getPane().setOPURPOS_Name("S1501");
                                                      getPane().setADRECDT(rec_date);
                                                      getPane().setADVCUT(USD_cust);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "populated USD of XAR amount--->" + USD_amt);
                                                      }

                                                      getPane().setADVAMNT(USD_amt + " " + "USD");

                                                      try {
                                                            String sum_Value = "select *  from ETT_XAR_REFUND_VIEW where INWARD_REF='"
                                                                        + inwardremNO + "'";

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG, "Sum value of XAR query--->" + sum_Value);
                                                            }
                                                            ps1 = con.prepareStatement(sum_Value);
                                                            rs1 = ps1.executeQuery();
                                                            if (rs1.next()) {

                                                                  String sumAmt = rs1.getString(1);
                                                                  BigDecimal sumAmount = new BigDecimal(sumAmt);
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                        Loggers.general().info(LOG,
                                                                                    "populated USD of XAR amount--->" + USD_amt);
                                                                        Loggers.general().info(LOG,
                                                                                    "Sum value of XAR amount if --->" + sumAmount);
                                                                  }
                                                                  BigDecimal USD_amount = new BigDecimal(USD_amt);

                                                                  BigDecimal totalVal = USD_amount.subtract(sumAmount);
                                                                  BigDecimal hundred = new BigDecimal("100");
                                                                  BigDecimal totalValue = totalVal.divide(hundred);
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                        Loggers.general().info(LOG,
                                                                                    "Total sum of value of XAR--->" + totalValue);
                                                                  }
                                                                  DecimalFormat diff = new DecimalFormat("0.00");
                                                                  diff.setMaximumFractionDigits(2);
                                                                  String finalValue = diff.format(totalValue);
                                                                  if ((totalValue.compareTo(BigDecimal.ZERO) > 0)) {
                                                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                              Loggers.general().info(LOG,
                                                                                          "final sum of value of XAR--->" + finalValue);
                                                                        }
                                                                        getPane().setADVAMNT(finalValue + " " + "USD");
                                                                  } else {
                                                                        getPane().setADVAMNT(0 + " " + "USD");

                                                                        if (!inwardremNO.trim().equalsIgnoreCase("")
                                                                                    && getMinorCode().equalsIgnoreCase("PCOC")
                                                                                    && (step_Input.equalsIgnoreCase("i"))
                                                                                    && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                                                              String qr1 = "SELECT mas.master_ref, TO_CHAR(bev.finished,'DD/MM/YY') FROM master mas, baseevent bev, extevent ext WHERE mas.key97 =bev.master_key and bev.KEY97 = ext.event AND mas.refno_pfix IN('CPC') AND bev.status ='c' AND inonos is not null AND inonos ='"
                                                                                          + inwardremNO + "'";
                                                                              // Loggers.general().info(LOG,"query
                                                                              // for already remitance
                                                                              // out---->
                                                                              // " + qr1);
                                                                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                                    Loggers.general().info(LOG, "XAR refuned finished--->" + qr1);
                                                                              }
                                                                              ps1 = con.prepareStatement(qr1);
                                                                              rs1 = ps1.executeQuery();
                                                                              while (rs1.next()) {

                                                                                    String mas_re = rs1.getString(1);
                                                                                    String mas_date = rs1.getString(2);
                                                                                    // Loggers.general().info(LOG,"Result
                                                                                    // value already
                                                                                    // remitance
                                                                                    // out----> " + mas_re +
                                                                                    // "" + mas_date);

                                                                                    validationDetails.addError(ErrorType.Other,
                                                                                                " Inward Remittance is already refunded under Master Reference No ("
                                                                                                            + mas_re + ") on date is (" + mas_date
                                                                                                            + ")  [CM]");
                                                                                    getPane().setADVCUT("");

                                                                              }

                                                                        }

                                                                  }

                                                            } else {
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                        Loggers.general().info(LOG, "Sum value of XAR else--->" + USD_amt);
                                                                  }
                                                                  getPane().setADVAMNT(USD_amt + " " + "USD");
                                                            }

                                                      } catch (Exception e) {
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,
                                                                              "Exception in XAR_Refunded value--->" + e.getMessage());
                                                            }
                                                      }

                                                }
                                          }

                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Exception in populated  XAR value--->" + e.getMessage());
                                          }
                                    }

                              } else if ((intcount == 0 || intcount < 1) && getMinorCode().equalsIgnoreCase("PCOC")
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Invalid Inward Remittance reference no (" + inwardremNO + ")  [CM]");
                                    getPane().setADVCUT("");
                                    getPane().setADVAMNT("");
                                    getPane().setADRECDT("");
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Exception in populated and validation XAR value--->" + e.getMessage());
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
                        // Loggers.general().info(LOG,"Inward remittance in blank");

                        getPane().setADVCUT("");
                        getPane().setADVAMNT("");
                        getPane().setADRECDT("");

                  }

                  try {

                        con = ConnectionMaster.getConnection();
                        if (!inwardremNO.trim().equalsIgnoreCase("") && getMinorCode().equalsIgnoreCase("PCOC")
                                    && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                              String qr = "SELECT CASE WHEN trim(ext.grno) IS NULL THEN 0 ELSE 1 END AS grno FROM master mas, baseevent bev, extevent ext, prodtype prd WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event and mas.prodtype = prd.key97 and prd.code = 'XAR' AND bev.refno_pfix = 'PST' AND mas.master_Ref ='"
                                          + inwardremNO + "'";

                              ps1 = con.prepareStatement(qr);
                              // Loggers.general().info(LOG,"Qurey for GR no search ---> " + qr);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String k = rs1.getString(1);
                                    // //Loggers.general().info(LOG,"while of count of hscode " +
                                    // k);
                                    int ka = Integer.parseInt(k);
                                    if (ka == 0) {
                                          // //Loggers.general().info(LOG,"Goods are exported");
                                    } else {
                                          validationDetails.addError(ErrorType.Other,
                                                      " Goods are exported to the this inward remittance(" + inwardremNO + ")  [CM]");
                                          getPane().setADVCUT("");
                                          getPane().setADVAMNT("");
                                    }
                              }

                        } else {
                              // Loggers.general().info(LOG,"inward remittace blank");
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception already remitance out" +
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

                  // INVOICE VALIDATION
                  double invadob_total = 0.0;
                  double am = 0.0;
                  String payamt = "0";
                  double final_payamtlong = 0.0;

                  String mastamt = "0";
                  double final_mastamtlong = 0.0;

                  String invadobamt_String = "";
                  String paycur = "";

                  String invcurr = "";
                  String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
                  if ((getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))
                              && (productyp.equalsIgnoreCase("DIR") || productyp.equalsIgnoreCase("AIR"))
                              && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        // Loggers.general().info(LOG,"Invoice grid initial ----->");
                        List<ExtEventInvoiceData> invoice = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();

                        for (int l = 0; l < invoice.size(); l++) {
                              // Loggers.general().info(LOG,"Invoice grid initial for
                              // loop----->");
                              ExtEventInvoiceData invoicedat = invoice.get(l);
                              payamt = getDriverWrapper().getEventFieldAsText("DAMR", "v", "m");
                              mastamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                              // //Loggers.general().info(LOG,"Remittance pay amount ----->" +
                              // payamt);
                              paycur = getDriverWrapper().getEventFieldAsText("DAMR", "v", "c");
                              double invamt = invoicedat.getINAMOUT().doubleValue();
                              // Loggers.general().info(LOG,"Invoice grid initial amount ----->" +
                              // invamt);
                              invcurr = invoicedat.getINAMOUTCurrency();

                              am = am + invamt;
                              // //Loggers.general().info(LOG,"Invoice amount after add ----->" +
                              // am);

                              ConnectionMaster connectionMaster = new ConnectionMaster();
                              double divideByDecimal = connectionMaster.getDecimalforCurrency(invcurr);
                              invadob_total = am / divideByDecimal;

                              final_payamtlong = Double.parseDouble(payamt);
                              final_mastamtlong = Double.parseDouble(mastamt);

                              if ((!paycur.equalsIgnoreCase(invcurr))
                                          && (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))
                                          && (productyp.equalsIgnoreCase("DIR") || productyp.equalsIgnoreCase("OTT")
                                                      || productyp.equalsIgnoreCase("AIR"))
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "Remittance currency should be equal to Invoice grid currency  [CM]");
                              }

                              // INVOICE DATE VALIDATION

                              String deteNeg = String.valueOf(invoicedat.getINDATE());
                              // //Loggers.general().info(LOG,"Invoice date " + deteNeg);

                              if ((!deteNeg.isEmpty())
                                          && (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    if ((deteNeg.matches("(^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$)"))) {

                                          // //Loggers.general().info(LOG,"Date of invoice is valid
                                          // ");
                                    } else {
                                          // //Loggers.general().info(LOG,"Date of invoice is not
                                          // valid
                                          // ");
                                          validationDetails.addError(ErrorType.Other,
                                                      "Invalid Date format(DD/MM/YYYY) in Invoice date grid  [CM]");

                                    }
                              } else {
                                    // Loggers.general().info(LOG,"Date of invoice is empty valid
                                    // ");

                              }
                              String invdate = String.valueOf(invoicedat.getINDATE());
                              if (invdate != null || !invdate.equalsIgnoreCase("")) {
                                    try {

                                          // Loggers.general().info(LOG,"Invoice Date---> " +
                                          // invdate);
                                          // //Loggers.general().info(LOG,"1");
                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                                          Date shipbillDate = (Date) formatter1.parse(invdate);
                                          Date sysDate = formatter1.parse(systemDate);
                                          // Loggers.general().info(LOG,"sysDate Date---> " +
                                          // sysDate);
                                          // Loggers.general().info(LOG,"shipbillDate -----> " +
                                          // shipbillDate);
                                          if (shipbillDate.after(sysDate) && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                      && (getMinorCode().equalsIgnoreCase("PCOC")
                                                                  || getMinorCode().equalsIgnoreCase("PBOC"))) {
                                                /// Loggers.general().info(LOG,"Invoice date is future
                                                /// date=====");
                                                validationDetails.addError(ErrorType.Other,
                                                            "Invoice date is future date than the today date [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"Invoice date is correct
                                                // date=====");
                                          }

                                    } catch (Exception e) {
                                          Loggers.general().info(LOG, "Invoice date exception" + e.getMessage());
                                    }
                              }

                        }

                        if ((invadob_total != final_mastamtlong)) {
                              // //Loggers.general().info(LOG," invoice and master amount not
                              // equal
                              // ---->1" + invadob_total + "" + final_payamtlong);
                              if ((getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))
                                          && (productyp.equalsIgnoreCase("DIR") || productyp.equalsIgnoreCase("AIR"))
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                    validationDetails.addWarning(WarningType.Other,
                                                "Remittance amount should be equal to Invoice grid amount [CM]");
                              }
                        } else {
                              // Loggers.general().info(LOG," invoice and master amount is
                              // equal----->" + invadob_total + "" + final_mastamtlong);
                        }

                        invadobamt_String = String.valueOf(invadob_total);
                        float invadobamt_flot = Float.valueOf(invadobamt_String);
                        // //Loggers.general().info(LOG,"invoice amount in flot ----->" +
                        // invadobamt_flot);
                        long invadobamt_long = (long) invadobamt_flot;
                        // Loggers.general().info(LOG,"invoice amount in invadobamt_long ----->"
                        // + invadobamt_long);
                        String cutype = getDriverWrapper().getEventFieldAsText("PRM", "p", "cct");
                        Loggers.general().info(LOG, "Cutype==" + cutype);
                        if (cutype.equalsIgnoreCase("PF") || cutype.equalsIgnoreCase("PRO") || cutype.equalsIgnoreCase("CF")
                                    || cutype.equalsIgnoreCase("PAR") || cutype.equalsIgnoreCase("PR")
                                    || cutype.equalsIgnoreCase("HUF")) {
                              if (invcurr.equalsIgnoreCase("USD") && invadob_total > 300000
                                          && (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))
                                          && (productyp.equalsIgnoreCase("DIR") || productyp.equalsIgnoreCase("AIR"))
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                    validationDetails.addWarning(WarningType.Other,
                                                "Invoice grid amount should not greater than 300000 $. Please ensure approval is in place [CM]");
                              } else {
                                    // Loggers.general().info(LOG,"invoice amount less than 300000$
                                    // ----->");

                              }
                        }

                  }
                  // BOE validation

                  List<ExtEventBOECAP> BOECAP = (List<ExtEventBOECAP>) getWrapper().getExtEventBOECAP();
                  for (int j = 0; j < BOECAP.size(); j++) {
                        ExtEventBOECAP boeval = BOECAP.get(j);
                        String billdate = String.valueOf(boeval.getBOEDAT());
                        if (billdate != null) {
                              /// //Loggers.general().info(LOG,"BOE Date " + billdate);
                              try { // //Loggers.general().info(LOG,"1");
                                    SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                                    Date date1 = parseFormat.parse(billdate);// grid enter
                                                                                                      // SHIP
                                                                                                      // BILL
                                                                                                      // date
                                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                                    String result1 = formatter1.format(date1);
                                    Date shipbillDate = (Date) formatter1.parse(result1);
                                    Date sysDate = formatter1.parse(systemDate);
                                    if (shipbillDate.after(sysDate) && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          // //Loggers.general().info(LOG,"date1 is before Date2");
                                          validationDetails.addError(ErrorType.Other,
                                                      "BOE date is future date than the today date  [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"----date3 is after
                                          // Date2---");
                                    }
                              } catch (Exception e) {
                                    // System.out.println(e.getMessage());
                              }
                        }
                  }

                  /*
                   * // KYC DATE VALIDATION try {
                   *
                   * // //Loggers.general().info(LOG,"KYC Date Validation"); String t =
                   * getDriverWrapper().getEventFieldAsText("PRI", "p", "cBEY"); //
                   * //Loggers.general().info(LOG,"value os first date " +t); String i =
                   * getDriverWrapper().getEventFieldAsText("ISS", "d", ""); //
                   * //Loggers.general().info(LOG,"value of issue date "+i); SimpleDateFormat sdf
                   * = new SimpleDateFormat("dd/MM/yy"); Date date1 = sdf.parse(t.trim()); Date
                   * date2 = sdf.parse(i.trim()); if (date1.compareTo(date2) < 0 &&
                   * getDriverWrapper().getEventFieldAsText("CSID", "s",
                   * "").equalsIgnoreCase("input")) {
                   * validationDetails.addWarning(ValidationDetails.WarningType.Other,
                   * "KYC Expired for the Customer  [CM]"); } } catch (Exception e) {
                   * //Loggers.general().info(LOG,"Exception " + e.getMessage()); }
                   */

                  String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventRefNoNumber = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                  String stepid = getDriverWrapper().getEventFieldAsText("CSID", "s", "");

                  // IFSC code validation

                  try {
                        String Ifsc = getWrapper().getIFSCCO_Name();
                        // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                        if ((!Ifsc.equalsIgnoreCase("") || Ifsc != null) && Ifsc.length() > 0
                                    && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                              con = getConnection();
                              String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                              // //Loggers.general().info(LOG,"query " + query);
                              int count3 = 0;
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while");
                                    count3 = rs1.getInt(1);
                              }

                              if (count3 == 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                    // //Loggers.general().info(LOG,"If in IFSC");
                                    validationDetails.addError(ErrorType.Other,
                                                "Invalid Beneficiary IFSC code(" + Ifsc + ")  [CM]");
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
                              // Loggers.general().info(LOG,"Connection Failed! Check
                              // output console");
                              e.printStackTrace();
                        }
                  }

                  try {
                        if ((getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))) {
                              String HScode = getDriverWrapper().getEventFieldAsText("cACO", "s", "").trim();

                              // //Loggers.general().info(LOG,"step is information" + stepid);
                              if (!HScode.trim().equalsIgnoreCase("") || HScode != null) {
                                    String qr = "select count(*) from exthmcode where hcodee='" + HScode + "'";
                                    // //Loggers.general().info(LOG,"Query " + qr);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(qr);
                                    // //System.out.println(qr);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String k = rs1.getString(1);
                                          // //Loggers.general().info(LOG,"while of count of hscode "
                                          // +k);
                                          int ka = Integer.parseInt(k);
                                          if (ka == 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("PCOC") && HScode.length() > 0) {
                                                // //Loggers.general().info(LOG,"warning of hs code ");
                                                validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                                            "HSCODE is Invalid (" + HScode + ")  [CM]");

                                          }
                                          if (ka == 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("PBOC") && HScode.length() > 0) {
                                                // //Loggers.general().info(LOG,"warning of hs code ");
                                                validationDetails.addError(ErrorType.Other, "HSCODE is Invalid (" + HScode + ")  [CM]");

                                          }
                                    }

                              }
                        } else {
                              // Loggers.general().info(LOG,"HS code");
                        }
                  } catch (Exception e) {
                        // //Loggers.general().info(LOG,"exception caught " +e.getMessage() );
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
                  if (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC")) {
                        try {
                              // //Loggers.general().info(LOG,"get value for LOB");
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG, "Exception getLOBCREATE =====>" + ee.getMessage());
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }
                  }

                  // master amount >1000000
                  String mast = getDriverWrapper().getEventFieldAsText("DAMR", "v", "m");
                  try {
                        if (mast.length() > 0 && mast.equalsIgnoreCase("")) {
                              String mast_cur = getDriverWrapper().getEventFieldAsText("DAMR", "v", "c");
                              // //Loggers.general().info(LOG,"Master amount for new project---->
                              // " +
                              // mast);
                              String newpro1 = getWrapper().getNEWPRO();
                              float amunt1 = Float.valueOf(mast);
                              // //Loggers.general().info(LOG,"amount" + amunt1);
                              long amount01 = (long) amunt1;

                              // Double masdou = Double.valueOf(proamt);

                              // Commision amount validation 24/09/2016
                              String invcom = "";
                              String invcom1 = "";
                              double InvDou = 0.00;
                              double InvDou1 = 0.00;
                              double commAmount = 0.00;
                              // double commisionAmount = 0.00;
                              List<ExtEventCommissionTable> commTable = (List<ExtEventCommissionTable>) getWrapper()
                                          .getExtEventCommissionTable();
                              for (int l = 0; l < commTable.size(); l++) {
                                    ExtEventCommissionTable fdwarapper1 = commTable.get(l);
                                    String masterRefNu = fdwarapper1.getMASTREF();
                                    String eventRefNu = fdwarapper1.getEVNTREF();
                                    String comCurrency = fdwarapper1.getCOMAMCurrency();
                                    // //Loggers.general().info(LOG,"Commision currency " +
                                    // comCurrency);
                                    String commamot = fdwarapper1.getCOMAM().toString();
                                    // //Loggers.general().info(LOG,"Commision Amount " + commamot);
                                    ConnectionMaster connectionMaster = new ConnectionMaster();
                                    double divideByDecimal = connectionMaster.getDecimalforCurrency(comCurrency);
                                    // //Loggers.general().info(LOG,"Currency return fom method " +
                                    // divideByDecimal);
                                    double commisionAmount = Double.valueOf(commamot);
                                    // //Loggers.general().info(LOG,"commisionAmount Amount Value in
                                    // double
                                    // "
                                    // +
                                    // commisionAmount);
                                    commAmount = commAmount + commisionAmount;
                                    // //Loggers.general().info(LOG,"commisionAmount Amount Value
                                    // added"
                                    // +
                                    // commAmount);
                                    double commisionAmt = commAmount / divideByDecimal;
                                    // Loggers.general().info(LOG,"commisionAmount Amount Value
                                    // final
                                    // ---> "
                                    // + commisionAmt);
                                    String CommAmt = String.valueOf(commisionAmt);

                                    try {
                                          con = getConnection();
                                          String query = "";
                                          query = "select sum(commamt)CommAmt from  ETT_vShowInvDtls where Mas_refno='" + masterRefNu
                                                      + "' AND EVTREFNO='" + eventRefNu + "'";
                                          // Loggers.general().info(LOG,"Query 1st view ----> " +
                                          // query);
                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();

                                          while (rs1.next()) {
                                                invcom = rs1.getString(1);
                                                // //Loggers.general().info(LOG,"Commission amount in
                                                // query"
                                                // +
                                                // invcom);
                                                InvDou = Double.valueOf(invcom);
                                                // Loggers.general().info(LOG,"Commission amount in
                                                // double"
                                                // +
                                                // InvDou);
                                          }

                                          String query1 = "";
                                          query1 = "SELECT sum(SUBSTR(com.comam,1,LENGTH(com.comam)-2)) amt FROM exteventcom com, master mas, baseevent bev, extevent evt WHERE mas.key97 =bev.master_key AND bev.extfield=evt.key29 AND evt.key29 =com.fk_event and bev.status='c' and com.mastref='"
                                                      + masterRefNu + "' and com.evntref='" + eventRefNu + "'";
                                          // Loggers.general().info(LOG,"Query 2nd view ----> " +
                                          // query1);
                                          ps1 = con.prepareStatement(query1);
                                          rs1 = ps1.executeQuery();

                                          while (rs1.next()) {
                                                invcom1 = rs1.getString(1);
                                                // //Loggers.general().info(LOG,"Commission amount in
                                                // query1
                                                // " +
                                                // invcom1);
                                                InvDou1 = Double.valueOf(invcom1);
                                                // Loggers.general().info(LOG,"Commission amount in
                                                // double
                                                // 1--->
                                                // " + InvDou);
                                          }

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Exeception " +
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

                                    double finalval = InvDou - InvDou1;

                                    // //Loggers.general().info(LOG,"Final view value---> " +
                                    // finalval);
                                    // //Loggers.general().info(LOG,"Final commision value---> " +
                                    // commisionAmt);
                                    if (commisionAmt > finalval && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Agency commision amount is more than transaction amount  [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"Grid amount is less than
                                          // Invoice
                                          // commission amount");
                                    }
                              }

                              List<ExtEventCommissionTable> commison = (List<ExtEventCommissionTable>) getWrapper()
                                          .getExtEventCommissionTable();
                              for (int l = 0; l < commison.size(); l++) {
                                    ExtEventCommissionTable commison1 = commison.get(l);
                                    String comisval = commison1.getCOMAM().toString();
                                    double comisvin = Double.valueOf(comisval);
                                    String comisCUR = commison1.getCOMAMCurrency();
                                    ConnectionMaster connectionMaster = new ConnectionMaster();
                                    double divideByDecimal = connectionMaster.getDecimalforCurrency(comisCUR);
                                    // //Loggers.general().info(LOG,"Currency return fom method " +
                                    // divideByDecimal);
                                    double commisionAmt = comisvin / divideByDecimal;
                                    // //Loggers.general().info(LOG,"commisionAmount Amount Value
                                    // final
                                    // --->
                                    // "
                                    // +
                                    // commisionAmt);
                                    String CommAmt = String.valueOf(commisionAmt);
                                    float amunt2 = Float.valueOf(CommAmt);
                                    // //Loggers.general().info(LOG,"amunt2 ---" + amunt2);
                                    long comlon = (long) amunt2;
                                    String finaltab = String.valueOf(comlon);
                                    String finalcom = String.valueOf(amount01);
                                    // Loggers.general().info(LOG,"final table Amount Value final
                                    // ---> "
                                    // +
                                    // finaltab);
                                    // Loggers.general().info(LOG,"MASTER Amount Value final ---> "
                                    // +
                                    // finalcom);
                                    /*
                                     * double comisvin = Double.valueOf(comisval); //System.out.println(
                                     * "Grid commission amount---> " + comisvin); double doucom = comisvin/100; long
                                     * amount01 = (long) amunt1; String finalcom = String.valueOf(amount01);
                                     */
                                    if ((!finalcom.equalsIgnoreCase(finaltab)) && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Agency commision remittance amount is more than the balance amount to be paid  [CM]");

                                    } else {
                                          // Loggers.general().info(LOG," commission amount is
                                          // equal");
                                    }
                              }
                        }
                  } catch (Exception e) {
                  }

                  // Category code populate based on input branch
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c").trim();
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

                  // RBI guidlines population
                  try {
                        double unitdou = 0.00;
                        double ozkg = 35.27396195;
                        double grakg = 1000;
                        int kilokg = 1;
                        double conunit = 0.00;
                        double caratKg = 5000;
                        String valunit = "";
                        String gold = getDriverWrapper().getEventFieldAsText("PUO1", "s", "");
                        // //Loggers.general().info(LOG,"selected values " + gold);
                        valunit = getDriverWrapper().getEventFieldAsText("PUO2", "s", "");
                        // //Loggers.general().info(LOG,"selected values unit" + valunit);
                        String unitval = getWrapper().getWEIGHT();
                        if (unitval == null || unitval.equalsIgnoreCase("")) {
                              // //Loggers.general().info(LOG,"weight values" + unitval);

                              unitval = "0";
                        }

                        unitdou = Double.valueOf(unitval);
                        // //Loggers.general().info(LOG,"Weight values" + unitdou);

                        if (gold.equalsIgnoreCase("GOLD")) {
                              // //Loggers.general().info(LOG,"Gold is selected");

                              if (valunit.equalsIgnoreCase("GRAM")) {
                                    conunit = unitdou / grakg;
                                    // Loggers.general().info(LOG,"setconunit GRAM" + conunit);
                                    String setconunit = String.valueOf(conunit);
                                    // getWrapper().setCONWEG(setconunit);
                                    getPane().setCONWEG(setconunit);
                              } else if (valunit.equalsIgnoreCase("OZ")) {
                                    conunit = unitdou / ozkg;
                                    // //Loggers.general().info(LOG,"before convert--->" + conunit);
                                    String setconunit = String.format("%.2f", conunit);
                                    // Loggers.general().info(LOG,"setconunit OZ " + setconunit);
                                    // getWrapper().setCONWEG(setconunit);
                                    getPane().setCONWEG(setconunit);
                              } else if (valunit.equalsIgnoreCase("KILO")) {
                                    conunit = unitdou / kilokg;
                                    String setconunit = String.valueOf(conunit);
                                    // Loggers.general().info(LOG,"setconunit KILO " + setconunit);
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
                  } catch (Exception e) {
                  }

                  // difference between shipment date and expiry date
                  if (productyp.equalsIgnoreCase("DIR")
                              && (getMinorCode().equalsIgnoreCase("PCOC") || getMinorCode().equalsIgnoreCase("PBOC"))) {
                        try {
                              String d = getDriverWrapper().getEventFieldAsText("CRVD", "d", "");
                              // //Loggers.general().info(LOG," shipment values------>" + d);
                              // String accept =
                              // getDriverWrapper().getEventFieldAsText("FPP:XPT", "s",
                              // "");
                              String d1 = getWrapper().getSHTDAT();
                              // //Loggers.general().info(LOG,"accept shipment values------>" +
                              // d1);
                              if (d != null && d1 != null && d1.length() > 0 && d.length() > 0) {
                                    // //Loggers.general().info(LOG,"date of shipment values" + d1);
                                    getPane().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                              } else {
                                    // Loggers.general().info(LOG,"else part of date difference
                                    // values");
                              }
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  } else {
                        // Loggers.general().info(LOG,"product type not DIR");
                  }

                  // Loggers.general().info(LOG,"Primary customer taking ----> " + cust);
                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // Loggers.general().info(LOG,"charge account collected " + chargecol);
                  String custval = "";

                  try {

                        Connection con = getConnection();
                        if (step_csm.equalsIgnoreCase("CBS Maker 1")) {
                              String dms = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='" + masterref
                                          + "' and EVENT_REF = '" + eventREF + "'";
                              // Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE QUERY ----> " +
                              // dms);
                              PreparedStatement ps1 = con.prepareStatement(dms);

                              ResultSet rs1 = ps1.executeQuery();
                              while (rs1.next()) {

                                    custval = rs1.getString(1);

                                    if (custval.length() > 0 && chargecol.equalsIgnoreCase("Y")
                                                && (!chargecol.equalsIgnoreCase("N"))) {

                                          validationDetails.addWarning(WarningType.Other,
                                                      "Account selected in Settlement for charges does not belong to the Customer [CM]");

                                    } else {
                                          Loggers.general().info(LOG, "charge account collected matched");
                                    }

                              }
                        } else {
                              // Loggers.general().info(LOG,"charge account collected in CBS
                              // Maker");

                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"charge account collected----->" +
                        // e.getMessage());
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

//                try {
//
//                      String dms = "select * from ETTV_BOE_PENDING_VIEW where CIF='" + cust + "'";
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,"ETTV_BOE_PENDING_VIEW QUERY ----> " + dms);
//                      }
//                      con = getConnection();
//                      ps1 = con.prepareStatement(dms);
//
//                      rs1 = ps1.executeQuery();
//                      while (rs1.next()) {
//
//                            if ((step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CSM")
//                                        || step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
//
//                                  validationDetails.addWarning(WarningType.Other, "Bill of entry pending for this client [CM]");
//
//                            } else {
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Bill of entry pending for this client else ----> ");
//                                  }
//                            }
//
//                      }
//
//                } catch (Exception e) {
//                      // Loggers.general().info(LOG,"charge account collected----->" +
//                      // e.getMessage());
//                } finally {
//                      try {
//                            if (rs1 != null)
//                                  rs1.close();
//                            if (ps1 != null)
//                                  ps1.close();
//                            if (con != null)
//                                  con.close();
//                      } catch (SQLException e) {
//                            // Loggers.general().info(LOG,"Connection Failed! Check output
//                            // console");
//                            e.printStackTrace();
//                      }
//                }

                  // NPA customer

                  try {

                        String productVal = getDriverWrapper().getEventFieldAsText("PRM", "p", "cAJB").trim();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "NPA customer-----> " + productVal);
                        }
                        if (productVal.equalsIgnoreCase("Y") && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                              validationDetails.addWarning(WarningType.Other, "Customer is a NPA customer [CM]");
                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Not a NPA customer-----> " + productVal);
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception NPA customer-----> " + e.getMessage());
                        }
                  }

                  String portdes = getWrapper().getPOSTDIS();
                  // //Loggers.general().info(LOG,"Port of destination Value---->" +
                  // portdes);
                  if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
                        try {
                              String portvalqury = "select trim(PODEST) from EXTPORTDESTINATION WHERE PODEST='" + portdes + "'";
                              // //Loggers.general().info(LOG,"port code destination query
                              // Value---->" +
                              // portvalqury);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(portvalqury);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);

                                    getPane().setPODESCP(hsploy);
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
                  } else {
                        // //Loggers.general().info(LOG,"port code is destination empty");
                  }

                  String portload = getWrapper().getPORLOD();
                  // //Loggers.general().info(LOG,"Port of destination Value---->" +
                  // portdes);
                  if ((!portload.equalsIgnoreCase("")) && portload != null) {
                        try {
                              String portvalqury = "select Trim(PORTDESC) from EXTPORTODLOAD where PORTLOAD='" + portload + "'";
                              // //Loggers.general().info(LOG,"port code destination query
                              // Value---->" + portvalqury);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(portvalqury);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1).trim();
                                    // //Loggers.general().info(LOG,"port code description---->"
                                    // +
                                    // hsploy);
                                    getPane().setPOLOADES(hsploy);
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

                  /*
                   * private boolean isChargeAccountDiff(Connection con) { boolean
                   * isChargeAccountDiff = false; PreparedStatement ps = null; ResultSet rs =
                   * null;
                   *
                   * //Loggers.general().info(LOG,"isChargeAccountDiff method Entered"); try {
                   * String masterRefNumber = getDriverWrapper().getEventFieldAsText( "MST", "r",
                   * ""); String account = getDriverWrapper().getEventFieldAsText("PRI", "q",
                   * "RCA").trim(); String ar[]=account.split("-");
                   *
                   * // //Loggers.general().info(LOG,"Master Reference" + masterRefNumber); String
                   * priCustStr = getDriverWrapper().getEventFieldAsText("PRI", "p", "no"); if
                   * (priCustStr != null) { String chargeAccountCheckQuery =
                   * "select trim(p.bo_acc_no) from master m, baseevent b, relitem r, posting p where m.key97 = b.master_key and b.key97 = r.event_key and r.key97 = p.key97 and p.acc_type in ('CA', 'RB') and m.master_ref='"
                   * + masterRefNumber +
                   * "' and  trim(p.bo_acc_no) not in (select trim(bo_acctno) from account where trim(cus_mnm)='"
                   * + priCustStr + "')"; //System.out.println( "chargeAccountCheckQuery - " +
                   * chargeAccountCheckQuery); ps = con.prepareStatement(chargeAccountCheckQuery);
                   * System.out .println( "prepared statement for chargeAccountCheck - " + ps); rs
                   * = ps.executeQuery(); if (rs.next()) { if(priCustStr!=ar[2].trim()){
                   * isChargeAccountDiff = true; }
                   *
                   * }} catch (Exception e) { //System.out.println(
                   * "Exception occured in isChargeAccountDiff - " + e.getMessage()); } finally {
                   * try { rs.close(); ps.close(); } catch (Exception e) { System.out
                   * .println("Exception in finally isChargeAccountDiff - " + e.getMessage()); } }
                   * return isChargeAccountDiff; }
                   */

                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit start-------->");
                  String strPropName1 = "ADDDailyTxnLimit";
                  String dailyval1 = "";
                  AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE1 = getDriverWrapper()
                              .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName1 + "'");
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                  EXTGENCUSTPROP PROPCode1 = queryRCODE1.getUnique();
                  if (PROPCode1 != null) {

                        dailyval1 = PROPCode1.getPropval();
                        // //Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                        // PROPCode.getPropval());
                  } else {
                        // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

                  }

                  // // transaction limit checking
                  String prino = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");
                  // //Loggers.general().info(LOG,"Primary customer no-------->" + prino);
                  // String dailyper = getDriverWrapper().getEventFieldAsText("PRM",
                  // "p", "cBPG");
                  // //Loggers.general().info(LOG,"Transaction limit per customer
                  // initaily-------->" + dailyval);

                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        Loggers.general().info(LOG, "Transaction limit daily customer initaily-------->" + dailyval1);
                        Loggers.general().info(LOG, "Primary customer no daily-------->" + prino);
                  }

                  if (dailyval1.length() > 0) {
                        int dailyint = Integer.parseInt(dailyval1);
                        // //Loggers.general().info(LOG,"Transaction limit per customer
                        // initaily-------->" + dailyval);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "Transaction limit per customer initaily-------->" + dailyval);
                        }

                        int daily_Qry = 0;
                        try {

                              String query = "select trim(COUNT1),trim(CODE) from ETT_DAILY_TRNCOUNT_VIEW where PRICUSTMNM ='"
                                          + prino + "' AND CODE='" + productyp + "'";
                              // //Loggers.general().info(LOG,"Transaction customer daily
                              // query-----> " + query);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Transaction customer daily query-----> " + query);
                              }
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {

                                    daily_Qry = rs1.getInt(1) + 1;
                                    Loggers.general().info(LOG, "count value Transaction-----> " + daily_Qry);
                                    if (daily_Qry > dailyint && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CSM Reject")
                                                            || step_csm.equalsIgnoreCase("CBS Reject")
                                                            || step_csm.equalsIgnoreCase("AdhocCSM"))) {

                                          validationDetails.addWarning(WarningType.Other, "Number transaction (" + daily_Qry
                                                      + ") exceeds daily limit (" + dailyint + ")for this customer [CM]");
                                          Loggers.general().info(LOG, "Current transaction is exceeded daily limit");

                                    } else {
                                          // Loggers.general().info(LOG,"customer value daily limit
                                          // else----->");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG, "customer value daily limit else----->");
                                          }

                                    }

                              }

                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"transaction Customer daily" +
                              // e1.getMessage());

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Exception transaction Customer daily" + e1.getMessage());
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
                        // Loggers.general().info(LOG,"Transaction limit per daily customer
                        // initaily empty-------->");
                  }

                  String monthlyvalName = "ADDMonthlyTxnLimit";
                  String monthlyval = "";
                  queryRCODE = getDriverWrapper().createQuery("EXTGENCUSTPROP", "PROPNAME='" + monthlyvalName + "'");

                  EXTGENCUSTPROP propmont = queryRCODE.getUnique();
                  if (propmont != null) {

                        monthlyval = propmont.getPropval();
                        // //Loggers.general().info(LOG,"ADD month TxnLimit -------->" +
                        // propmont.getPropval());
                  } else {
                        // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

                  }
                  // String monyper = getDriverWrapper().getEventFieldAsText("PRM",
                  // "p", "cBPH");
                  // //Loggers.general().info(LOG,"Transaction limit per month customer
                  // initaily-------->" + monthlyval);
                  if (monthlyval.length() > 0) {
                        int monint = Integer.parseInt(monthlyval);
                        // //Loggers.general().info(LOG,"Transaction limit per month customer
                        // initaily-------->" + monint);
                        // //Loggers.general().info(LOG,"Primary customer no month-------->" +
                        // prino);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "Transaction limit per month customer initaily-------->" + monint);
                              Loggers.general().info(LOG, "Primary customer no month-------->" + prino);
                        }

                        int mon_Qry = 0;
                        try {

                              String query = "select trim(COUNT1),trim(CODE) from ETT_MONTHLY_TRNCOUNT_VIEW where PRICUSTMNM ='"
                                          + prino + "' AND CODE='" + productyp + "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Transaction customer month query-----> " + query);
                              }
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {

                                    mon_Qry = rs1.getInt(1) + 1;
                                    // //Loggers.general().info(LOG,"count value Transaction
                                    // month-----> " + mon_Qry);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "count value Transaction month-----> " + mon_Qry);
                                    }
                                    if (mon_Qry > monint && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CSM Reject")
                                                || step_csm.equalsIgnoreCase("CBS Reject") || step_csm.equalsIgnoreCase("AdhocCSM"))) {

                                          validationDetails.addWarning(WarningType.Other, "Number transaction (" + mon_Qry
                                                      + ") exceeds monthly limit (" + monint + ")for this customer [CM]");
                                          // //Loggers.general().info(LOG,"Current transaction is
                                          // exceeded monthly limit [CM]");

                                    } else {
                                          // Loggers.general().info(LOG,"customer value month
                                          // else----->");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG, "customer value month else----->");
                                          }

                                    }

                              }

                        } catch (Exception e1) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Exception transaction Customer month" + e1.getMessage());
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
                        // Loggers.general().info(LOG,"Transaction limit per month customer
                        // initaily empty-------->");
                  }

                  try {
                        String halfvalName = "ADDHalfYerlyTxnLimit";
                        String halfval = "";
                        queryRCODE = getDriverWrapper().createQuery("EXTGENCUSTPROP", "PROPNAME='" + halfvalName + "'");

                        EXTGENCUSTPROP propHalf = queryRCODE.getUnique();
                        if (propmont != null) {

                              halfval = propHalf.getPropval();
                              // //Loggers.general().info(LOG,"ADD month TxnLimit -------->" +
                              // propmont.getPropval());
                        } else {
                              // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

                        }
                        // String monyper =
                        // getDriverWrapper().getEventFieldAsText("PRM",
                        // "p", "cBPH");
                        // //Loggers.general().info(LOG,"Transaction limit per month customer
                        // initaily-------->" + monthlyval);
                        if (halfval.length() > 0) {
                              int halfInt = Integer.parseInt(halfval);
                              // //Loggers.general().info(LOG,"Transaction limit per month
                              // customer
                              // initaily-------->" + monint);
                              // //Loggers.general().info(LOG,"Primary customer no month-------->"
                              // +
                              // prino);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Transaction limit half yearly customer initaily===>" + halfInt);
                                    Loggers.general().info(LOG, "Primary customer half yearly===>" + prino);
                              }

                              int haly_Qry = 0;
                              try {

                                    String query = "select trim(COUNT1),trim(CODE) from ETT_HALFYEARLYLIMIT_VIEW where PRICUSTMNM ='"
                                                + prino + "' AND CODE='" + productyp + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "Transaction customer half yearly query===> " + query);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {

                                          haly_Qry = rs1.getInt(1) + 1;

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG, "count value Transaction half yearly===> " + haly_Qry);
                                          }
                                          if (haly_Qry > halfInt && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CSM Reject")
                                                                  || step_csm.equalsIgnoreCase("CBS Reject")
                                                                  || step_csm.equalsIgnoreCase("AdhocCSM"))) {

                                                validationDetails.addWarning(WarningType.Other, "Number transaction (" + haly_Qry
                                                            + ") exceeds Half yearly limit (" + halfInt + ")for this customer [CM]");

                                          } else {

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG, "Else Transaction (" + haly_Qry
                                                                  + ") exceeds monthly limit (" + halfInt + ")");
                                                }

                                          }

                                    }

                              } catch (Exception e1) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "Exception in half yearly" + e1.getMessage());
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
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        } else {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Transaction limit half yearly customer initaily blank");
                              }
                        }
                  } catch (Exception e1) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "Exception transaction Customer half yearly" + e1.getMessage());
                        }

                  }
                  // String yearlim = getDriverWrapper().getEventFieldAsText("PRM",
                  // "p", "cBPI");
                  // //Loggers.general().info(LOG,"Transaction limit year customer
                  // initaily-------->" + yearlim);
                  // if(yearlim.length()>0)
                  // {
                  // int yearint = Integer.parseInt(yearlim);
                  // //Loggers.general().info(LOG,"Transaction limit year customer
                  // initaily-------->" + yearint);
                  // //Loggers.general().info(LOG,"Primary customer no year-------->" +
                  // prino);
                  //
                  // int year_Qry = 0;
                  // try {
                  //
                  // String query = "select trim(COUNT1) from ETT_YEARLY_TRNCOUNT_VIEW
                  // where PRICUSTMNM ='" + prino + "'";
                  // //Loggers.general().info(LOG,"Transaction customer yearly query-----> " +
                  // query);
                  // con = ConnectionMaster.getConnection();
                  // ps1 = con.prepareStatement(query);
                  // rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  //
                  // year_Qry = rs1.getInt(1) + 1;
                  // //Loggers.general().info(LOG,"count value Transaction yearly-----> " +
                  // year_Qry);
                  // if (year_Qry > yearint && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  //
                  // validationDetails.addWarning(WarningType.Other, "Number
                  // transaction (" + year_Qry
                  // + ") exceeds yearly limit (" + yearint + ")for this customer
                  // [CM]");
                  // //Loggers.general().info(LOG,"Current transaction is exceeded yearly
                  // limit
                  // [CM]");
                  //
                  // } else {
                  // //Loggers.general().info(LOG,"customer value year else----->");
                  //
                  // }
                  //
                  // }
                  //
                  // } catch (Exception e1) {
                  // //Loggers.general().info(LOG,"transaction Customer yearly" +
                  // e1.getMessage());
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
                  // //Loggers.general().info(LOG,"Connection Failed! Check output console");
                  // e.printStackTrace();
                  // }
                  // }
                  // }
                  // else
                  // {
                  // //Loggers.general().info(LOG,"Transaction limit year customer initaily
                  // empty-------->");
                  // }

                  // Duplicate Master Reference

                  try {
                        int noOfRecord = 0;
                        con = getConnection();
                        String customerId = "";
                        String amt = "";
                        String currency = "";
                        String masterRef = "";
                        customerId = getDriverWrapper().getEventFieldAsText("PRM", "p", "no").trim();
                        // //Loggers.general().info(LOG,"Customer Id " + customerId);

                        String amount_am = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                        String amount = amount_am.replaceAll("[^0-9]", "");
                        // //Loggers.general().info(LOG,"Currency is " + amount);
                        currency = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        String beneaccnum = getDriverWrapper().getEventFieldAsText("BNAC", "s", "").trim();
                        Loggers.general().info(LOG, "beneaccnum is " + beneaccnum);
                        masterRef = getmasRefNo();
                        // //Loggers.general().info(LOG,"Master Reference " + masterRef);
                        // //Loggers.general().info(LOG,"Entered Validate method in CpcoPcoc
                        // class");

                        if (amount.length() > 1 && currency.length() > 0) {

                              // //Loggers.general().info(LOG,"Duplicate Record Query is " +
                              // duplicateMaster);

                              String duplicateMaster = "select COUNT(*) as COUNT  from ETT_DUPLICAT_WARNING_VIEW where PRICUSTMNM='"
                                          + customerId + "' and AMOUNT='" + amount + "' and CCY='" + currency + "' and MASTER_REF !='"
                                          + MasterReference + "' and benef_num='" + beneaccnum + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Duplicate Record Query is " + duplicateMaster);
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
                                                      || step_csm.equalsIgnoreCase("CBS Maker 1")
                                                      || step_csm.equalsIgnoreCase("AdhocCSM"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "There is an existing transaction for the Same Customer,Amount and Currency [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "Duplicate Record in else " + noOfRecord);
                                    }
                              }
                        }

                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "Exception in Duplicate master " + e.getMessage());
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

                  try {
                        int noOfRecord = 0;
                        con = getConnection();
                        String customerId = "";
                        String amt = "";
                        String currency = "";
                        String masterRef = "";
                        int count = 0;
                        customerId = getDriverWrapper().getEventFieldAsText("PRM", "p", "no").trim();
                        // //Loggers.general().info(LOG,"Customer Id " + customerId);

                        String amount_am = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                        String amount = amount_am.replaceAll("[^0-9]", "");
                        // //Loggers.general().info(LOG,"Currency is " + amount);
                        currency = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        String beneaccnum = getDriverWrapper().getEventFieldAsText("BNAC", "s", "").trim();
                        Loggers.general().info(LOG, "beneaccnum is " + beneaccnum);
                        masterRef = getmasRefNo();
                        // //Loggers.general().info(LOG,"Master Reference " + masterRef);
                        // //Loggers.general().info(LOG,"Entered Validate method in CpcoPcoc
                        // class");

                        String invnum = "";
                        List<ExtEventInvoiceData> invoice1 = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();
                        int invcount = invoice1.size();
                        Loggers.general().info(LOG, "Invoice details size-------------> " + invcount);
                        for (int l = 0; l < invoice1.size(); l++) {
                              ExtEventInvoiceData invoicedetails = invoice1.get(l);
                              // invnum=invnum+invoicedetails.getINVNUMB().trim();
                              invnum = invoicedetails.getINVNUMB().trim().toUpperCase();

                              Loggers.general().info(LOG, "Invoice details-------------> " + invnum);

                              if (amount.length() > 1 && currency.length() > 0) {

                                    // //Loggers.general().info(LOG,"Duplicate Record Query is " +
                                    // duplicateMaster);

                                    String duplicateMaster = "select COUNT(*) as COUNT  from ETT_INVOICE_DUPLICAT_VIEW where PRICUSTMNM='"
                                                + customerId + "' and AMOUNT='" + amount + "' and CCY='" + currency
                                                + "' and MASTER_REF !='" + MasterReference + "'  and benef_num='" + beneaccnum
                                                + "' and invoice_num='" + invnum + "'";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "invoice Duplicate Record Query is " + duplicateMaster);
                                    }
                                    ps1 = con.prepareStatement(duplicateMaster);
                                    rs1 = ps1.executeQuery();
                                    if (rs1.next()) {
                                          noOfRecord = rs1.getInt("COUNT");
                                          count = count + noOfRecord;
                                          Loggers.general().info(LOG, "Count " + count);
                                    }

                              }
                        }
                        // //Loggers.general().info(LOG,"No of Existing records is " +
                        // noOfRecord);
                        if ((count > 0) && (count == invcount) && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                    || step_csm.equalsIgnoreCase("CBS Reject") || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "There is an existing transaction for the Same Customer,Amount and Currency and Invoice number[CM]");
                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Duplicate Record in else " + noOfRecord);
                              }
                        }

                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "Exception in Duplicate master " + e.getMessage());
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

                  // ======================================end of duplicate

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
                  // } catch (Exception e1) {
                  // // Loggers.general().info(LOG,"Exception Sender IFSC Code" +
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
                  //
                  // } else {
                  // Loggers.general().info(LOG,"Sender IFSC Code" + seifsc);

                  // }

                  // String recifsc = getWrapper().getRECIFSC().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                  // if (recifsc.length() > 0 && (step_Input.equalsIgnoreCase("i"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // try {
                  //
                  // String query = "select count(*) from EXTIFSCC where IFSC='" +
                  // recifsc.trim() + "'";
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
                  //
                  // } else {
                  // Loggers.general().info(LOG,"Receiver IFSC Code" + recifsc);

                  // }
                  String payam = getDriverWrapper().getEventFieldAsText("DAMR", "v", "m");
                  try {
                        String dailylim = getDriverWrapper().getEventFieldAsText("PRM", "p", "cBOA").trim();

                        Loggers.general().info(LOG, "Customer daily in customer amount and usd-------->" + dailylim);
                        Loggers.general().info(LOG, "Customer daily in customer" + payam);
                        if (dailylim.length() > 0) {
                              Loggers.general().info(LOG, "Customer daily in customer amount and usd-------->" + dailylim);
                              String daily_str = dailylim.replaceAll("[^0-9]", "");
                              Loggers.general().info(LOG, "Customer daily in customer amount only------->" + daily_str);
                              BigDecimal hundred = new BigDecimal(100);
                              BigDecimal dailyamt33 = new BigDecimal(daily_str);
                              BigDecimal dailyval4 = dailyamt33.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
                              Loggers.general().info(LOG, "Customer daily after convert customer amountonly------->" + dailyval4);

                              Loggers.general().info(LOG, "Primary customer no-------->" + prino);
                              String dailyamt = "";
                              BigDecimal mon_big1 = new BigDecimal(0);

                              try {

                                    String query = "SELECT SUM(NVL(mas.AMOUNT,0)) AS AMOUNT,mas.CCY FROM master mas,BASEEVENT bas,DLYPRCCYCL dly WHERE mas.KEY97 =bas.MASTER_KEY "
                                                + " AND mas.PRICUSTMNM ='" + prino
                                                + "' AND mas.REFNO_PFIX ='CPC' AND TO_CHAR(bas.FINISHED,'DD/MM/YYYY')=TO_CHAR(dly.PROCDATE ,'DD/MM/YYYY')"
                                                + " GROUP BY MAS.CCY ";
                                    String query2 = "select (AMOUNT/100) AS AMOUNT from ETT_DLYLIMIT_VIEW where PRICUSTMNM ='"
                                                + prino + "'";
                                    Loggers.general().info(LOG, "customer dailylim query-----> " + query2);
                                    con = ConnectionMaster.getConnection();
                                    PreparedStatement ps1 = con.prepareStatement(query2);
                                    ResultSet rs1 = ps1.executeQuery();
                                    while (rs1.next()) {

                                          dailyamt = rs1.getString(1);
                                          BigDecimal priceDecimal = new BigDecimal(dailyamt);
                                          Loggers.general().info(LOG, "customer dailylim priceDecimal" + priceDecimal);
                                          BigDecimal big_tol = priceDecimal.add(new BigDecimal(payam));
                                          Loggers.general().info(LOG, "Total customer dailylim big Decimal" + big_tol);
                                          DecimalFormat df = new DecimalFormat("0.00");
                                          String val1 = String.valueOf(df.format(big_tol));
                                          Loggers.general().info(LOG, "customer dailylim after convert-----> " + val1);
                                          BigDecimal mon_big2 = new BigDecimal(val1);
                                          Loggers.general().info(LOG, "customer dailylim FINAL big1-----> " + mon_big1);
                                          Loggers.general().info(LOG, "customer dailylim FINAL-----> " + mon_big2);
                                          Loggers.general().info(LOG, "customer dailyamt" + dailyamt);
                                          Loggers.general().info(LOG,
                                                      "customer value-----> " + dailyval4 + "customer daily query value" + mon_big2);
                                          Loggers.general().info(LOG, "step id=====>" + step_csm);
                                          Loggers.general().info(LOG,
                                                      "customer value----->  comparing " + dailyval4.compareTo(mon_big2));
                                          if (dailyamt.length() > 0 && dailylim.length() > 0 && dailyval4.compareTo(mon_big2) < 0
                                                      && (step_Input.equalsIgnoreCase("i")) && ((step_csm.equalsIgnoreCase("CBS Maker"))
                                                                  || (step_csm.equalsIgnoreCase("CBS Maker 1")))) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Current transaction amount is exceeded daily limit [CM]");

                                          } else {
                                                System.out.println("customer value else-----> " + dailyval4
                                                            + "customer daily query  value else" + mon_big2);

                                          }

                                    }

                                    ps1.close();
                                    rs1.close();
                                    con.close();

                              } catch (Exception e1) {
                                    Loggers.general().info(LOG, "Customer dailylimit" + e1.getMessage());
                              }
                        } else {
                              Loggers.general().info(LOG, "Customer daily in customer amount is empty");
                        }
                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception in daily limit " + e.getMessage());
                  }

                  // ======Montly limit
                  try {

                        String dailymon = getDriverWrapper().getEventFieldAsText("PRM", "p", "cBOB").trim();
                        if (dailymon.length() > 0) {
                              Loggers.general().info(LOG, "Customer Monthly in customer amount and usd-------->" + dailymon);
                              String mon_str = dailymon.replaceAll("[^0-9]", "");
                              Loggers.general().info(LOG, "Customer monthly in customer amount only------->" + mon_str);
                              String payamt1 = getDriverWrapper().getEventFieldAsText("DAMR", "v", "m");
                              Loggers.general().info(LOG, "Customer payamt amount ---->" + payamt1);
                              BigDecimal big_val = new BigDecimal(mon_str);
                              BigDecimal hundred = new BigDecimal(100);
                              BigDecimal mon_val = big_val.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
                              String prino1 = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");
                              Loggers.general().info(LOG, "Primary customer no-------->" + prino1);
                              Loggers.general().info(LOG,
                                          "Customer Monthly after convert customer amount only------->" + mon_val);
                              BigDecimal mon_big1 = new BigDecimal(0);
                              String monamt = "";
                              try {

                                    String query = "select (AMOUNT/100) AS AMOUNT from ETT_MONTHLIMIT_VIEW where PRICUSTMNM ='"
                                                + prino1 + "'";
                                    Loggers.general().info(LOG, "customer monthly query-----> " + query);
                                    con = ConnectionMaster.getConnection();
                                    PreparedStatement ps1 = con.prepareStatement(query);
                                    ResultSet rs1 = ps1.executeQuery();
                                    while (rs1.next()) {

                                          monamt = rs1.getString(1);
                                          BigDecimal priceDecimal = new BigDecimal(monamt);
                                          Loggers.general().info(LOG, "customer Monthly priceDecimal" + priceDecimal);
                                          BigDecimal big = priceDecimal.add(new BigDecimal(payam));
                                          Loggers.general().info(LOG, "Total customer Monthly big Decimal" + big);
                                          DecimalFormat df = new DecimalFormat("0.00");
                                          String val1 = String.valueOf(df.format(big));
                                          Loggers.general().info(LOG, "customer Monthly after convert-----> " + val1);
                                          BigDecimal mon_big4 = new BigDecimal(val1);
                                          Loggers.general().info(LOG, "customer value big1-----> " + mon_big1);
                                          Loggers.general().info(LOG,
                                                      "customer value-----> " + mon_val + "customer Monthly query value" + mon_big4);
                                          Loggers.general().info(LOG, "comparing " + mon_val.compareTo(mon_big4));
                                          Loggers.general().info(LOG, "Step id====>" + step_csm);
                                          if (monamt.length() > 0 && dailymon.length() > 0 && mon_val.compareTo(mon_big4) < 0
                                                      && (step_Input.equalsIgnoreCase("i")) && ((step_csm.equalsIgnoreCase("CBS Maker"))
                                                                  || (step_csm.equalsIgnoreCase("CBS Maker 1")))) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Current transaction amount is exceeded monthly limit [CM]");

                                          } else {
                                                System.out.println("customer value else-----> " + mon_val
                                                            + "customer daily query value else" + mon_big4);

                                          }

                                    }

                                    ps1.close();
                                    rs1.close();
                                    con.close();

                              } catch (Exception e1) {
                                    Loggers.general().info(LOG, "Customer monthly" + e1.getMessage());
                              }
                        } else {
                              Loggers.general().info(LOG, "Customer monthly in customer amount is empty");
                        }
                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception in monthly limit " + e.getMessage());
                  }
                  //
                  // String dailymon = getDriverWrapper().getEventFieldAsText("PRM",
                  // "p", "cBOB");
                  // if (dailymon.length() > 0) {
                  // // //Loggers.general().info(LOG,"Customer Monthly in customer amount and
                  // // usd-------->" + dailymon);
                  // String mon_str = dailymon.replaceAll("[^0-9]", "");
                  // //Loggers.general().info(LOG,"Customer monthly in customer amount
                  // only------->" + mon_str);
                  // // String payamt = getDriverWrapper().getEventFieldAsText("DAMR",
                  // // "v", "m");
                  // // //Loggers.general().info(LOG,"Customer payamt amount ---->" + payamt);
                  // BigDecimal big_val = new BigDecimal(mon_str);
                  // BigDecimal hundred = new BigDecimal(100);
                  // BigDecimal mon_val = big_val.divide(hundred, 2,
                  // BigDecimal.ROUND_HALF_UP);
                  // // String prino = getDriverWrapper().getEventFieldAsText("PRM",
                  // "p",
                  // // "no");
                  // //Loggers.general().info(LOG,"Primary customer no-------->" + prino);
                  // // //Loggers.general().info(LOG,"Customer Monthly after convert customer
                  // // amount only------->" + mon_val);
                  // // BigDecimal mon_big1 = new BigDecimal(0);
                  // String monamt = "";
                  // try {
                  //
                  // String query = "select (AMOUNT/100) AS AMOUNT from
                  // ETT_MONTHLIMIT_VIEW where PRICUSTMNM ='" + prino
                  // + "'";
                  // //Loggers.general().info(LOG,"customer monthly query-----> " + query);
                  // con = ConnectionMaster.getConnection();
                  // PreparedStatement ps1 = con.prepareStatement(query);
                  // ResultSet rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  //
                  // monamt = rs1.getString(1);
                  // BigDecimal priceDecimal = new BigDecimal(monamt);
                  // // //Loggers.general().info(LOG,"customer Monthly priceDecimal" +
                  // // priceDecimal);
                  // BigDecimal big = priceDecimal.add(new BigDecimal(payam));
                  // // //Loggers.general().info(LOG,"Total customer Monthly big Decimal" +
                  // // big);
                  // DecimalFormat df = new DecimalFormat("0.00");
                  // String val1 = String.valueOf(df.format(big));
                  // //Loggers.general().info(LOG,"customer Monthly after convert-----> " +
                  // val1);
                  // BigDecimal mon_big1 = new BigDecimal(val1);
                  // //Loggers.general().info(LOG,"customer value-----> " + mon_val +
                  // "customer
                  // Monthly query value" + mon_big1);
                  // if (monamt.length() > 0 && dailymon.length() > 0 &&
                  // mon_val.compareTo(mon_big1) < 0
                  // && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  //
                  // validationDetails.addWarning(WarningType.Other,
                  // "Current transaction amount is exceeded monthly limit [CM]");
                  //
                  // } else {
                  // //System.out.println(
                  // "customer value else-----> " + mon_val + "customer daily query
                  // value else" + mon_big1);
                  //
                  // }
                  //
                  // }
                  //
                  // ps1.close();
                  // rs1.close();
                  // con.close();
                  //
                  // } catch (Exception e1) {
                  // //Loggers.general().info(LOG,"Customer monthly" + e1.getMessage());
                  // }
                  // } else {
                  // //Loggers.general().info(LOG,"Customer monthly in customer amount is
                  // empty");
                  // }

                  try {

                        /*
                         * String query =
                         * "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, BASEEVENT BEV, ETT_REFERRAL_TRACKING REF WHERE MAS.KEY97 = BEV.MASTER_KEY AND trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)  AND BEV.STATUS <>'a' AND TRIM(REF.MASTER_REF_NO) ='"
                         * + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode +
                         * "' AND REF.SUB_PRODUCT_CODE = '" + subproductCode +
                         * "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID"
                         * ;
                         */
                        String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, ETT_REFERRAL_TRACKING REF WHERE   trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)   AND TRIM(REF.MASTER_REF_NO) ='"
                                    + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode + "' AND REF.SUB_PRODUCT_CODE = '"
                                    + subproductCode
                                    + "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Query ETT_REFERRAL_TRACKING count===>" + query);
                        }
                        int count2 = 0;
                        String step = null;
                        String concat = null;
                        con = getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {

                              step = rs1.getString(1);

                              count2 = rs1.getInt(2);
                              Loggers.general().info(LOG, "Entered while referal step" + step + "count" + count2);
                              if (count2 > 0) {
                                    if ((step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Reject"))
                                                && !step_csm.equalsIgnoreCase("CBS Authoriser")) {

                                          String ref = null;
                                          String statusReferral = null;
                                          String warningMessage = null;
                                          String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                                                      + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                                                      + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                                                      + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
                                          Loggers.general().info(LOG, "query for referral----------------->" + query6);
                                          ps1 = con.prepareStatement(query6);

                                          rs = ps1.executeQuery();
                                          ArrayList<String> al = new ArrayList<String>();
                                          while (rs.next()) {
                                                Loggers.general().info(LOG, "referral_)status------------>" + rs.getString(2));
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

                                          validationDetails.addWarning(WarningType.Other, warningMessage);

                                    } else if (step_csm.equalsIgnoreCase("CBS Checker")
                                                || step_csm.equalsIgnoreCase("CBS Authoriser")) {
                                          Loggers.general().info(LOG, "Authoriser");
                                          String ref = null;
                                          String statusReferral = null;
                                          String warningMessage = null;
                                          String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                                                      + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                                                      + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                                                      + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "ETT_REFERRAL_TRACKING Warning Message query step2====>" + query6);
                                          }
                                          ps1 = con.prepareStatement(query6);

                                          rs = ps1.executeQuery();
                                          ArrayList<String> al = new ArrayList<String>();
                                          while (rs.next()) {
                                                Loggers.general().info(LOG, "referral_)status------------>" + rs.getString(2));
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
                        // //Loggers.general().info(LOG,"Entered while referal count out of
                        // loop"
                        // + count2);
                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception referal count" +
                        // e1.getMessage());
                  } finally {
                        try {
                              if (rs != null)
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
                                    + " AND SUB_PRODUCT_CODE= '" + subproductCode + "'" + " and INIT_AT='" + step_csm + "'"
                                    + " and MANDATORY='REJ'  and CHECKED_YN ='Y'";
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
                                          + " AND SUB_PRODUCT_CODE = '" + subproductCode + "'" + " and INIT_AT='" + step_csm + "'"
                                          + " and MANDATORY='REJ' and CHECKED_YN ='Y'";

                              pst = con.prepareStatement(query6);
                              ArrayList<String> al = new ArrayList<String>();
                              rs = pst.executeQuery();
                              while (rs.next()) {

                                    ref = " Rejections " + "\"" + rs.getString(2) + "\"" + " raised by " + rs.getString(1)
                                                + " is not resolved [CM]";
                                    al.add(ref);
                              }
                              if (al.size() > 0 && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CSM Reject")
                                          || step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CBS Reject")
                                          || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    warningMessage = StringUtils.join(al, "\n");
                                    // //Loggers.general().info(LOG,"Single Warning Message " +
                                    // warningMessage);
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
                              Loggers.general().info(LOG, "Query REFERRAL_STATUS pending" + query5);
                        }

                        con = getConnection();
                        pst = con.prepareStatement(query5);

                        rs = pst.executeQuery();
                        if (rs.next()) {
                              // int counter = getInt(1);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Pending referal status count if loop");
                              }

                              if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CSM")
                                          || step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addWarning(WarningType.Other, "Past pending deferral for same client [CM]");
                              }

                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "No pending referal status" + query5);
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "ExceptionPending referal status" + e.getMessage());
                        }
                  }

                  // RTGS/NEFT cut off time monitoring
                  String rtgs = getWrapper().getPROREMT();
                  // Loggers.general().info(LOG,"RTGS for initially-------->" + rtgs);
                  String char_val = getWrapper().getBENNAME_Name();
                  if (char_val != null && char_val.length() > 0) {
                        int counter = 0;
                        for (int i = 0; i < char_val.length(); i++) {
                              if (!Character.toString(char_val.charAt(i)).matches("^[a-zA-Z\\s]*$")) {
                                    counter++;
                              }
                        }

                        if (counter > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "Special characters should not be in Beneficiary name [CM]");
                        }
                  }

                  String ben_val = getWrapper().getBADDRE_Name();
                  if (ben_val != null && ben_val.length() > 0) {
                        int counter_val = 0;
                        for (int i = 0; i < ben_val.length(); i++) {
                              if (!Character.toString(ben_val.charAt(i)).matches("^[a-zA-Z0-9\\s]*$")) {
                                    counter_val++;
                              }
                        }

                        if (counter_val > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "Special characters should not be in Beneficiary Address [CM]");
                        }
                  }

                  String acc_val = getWrapper().getBENACC_Name();
                  if (acc_val != null && acc_val.length() > 0 && (!acc_val.matches("[a-zA-Z0-9]+"))
                              && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        validationDetails.addError(ErrorType.Other,
                                    "Special characters should not be in Beneficiary Account no [CM]");
                  }

                  // Old validation

                  // try {
                  // String NEFTHOLY = "RTGSHOLY";
                  // String NEFTTime = "";
                  // AdhocQuery<? extends
                  // com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP>
                  // queryResult = getDriverWrapper()
                  // .createQuery("EXTGENCUSTPROP", "PROPNAME='" + NEFTHOLY + "'");
                  // // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                  // EXTGENCUSTPROP querycode = queryResult.getUnique();
                  // if (querycode != null) {
                  //
                  // NEFTTime = querycode.getPropval().trim();
                  // // //Loggers.general().info(LOG,"NEFTTime time for cut off-------->"
                  // // +
                  // // querycode.getPropval());
                  // } else {
                  // // Loggers.general().info(LOG,"Today is holyday so RTGS/NEFT
                  // // empty-------->");
                  //
                  // }
                  //
                  // if (NEFTTime.equalsIgnoreCase("NO") &&
                  // (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker") ||
                  // step_csm.equalsIgnoreCase("Authoriser")
                  // || step_csm.equalsIgnoreCase("Authorise"))) {
                  // validationDetails.addError(ErrorType.Other, "Today is holiday so
                  // RTGS/NEFT will no proceed [CM]");
                  // } else {
                  // // Loggers.general().info(LOG,"Today is holyday so RTGS/NEFT will
                  // // avilable");
                  // }
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"NEFT cut off TIME Exception" +
                  // // e.getMessage());
                  // }

                  // New Validation

                  try {

                        if (rtgs.equalsIgnoreCase("NEF")) {

                              String NEFTHOLY = "NEFTWorking";
                              String NEFTTime = "";
                              AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                          .createQuery("EXTGENCUSTPROP", "PROPNAME='" + NEFTHOLY + "'");
                              // //Loggers.general().info(LOG,"ADDDailyTxnLimit
                              // initially-------->");
                              EXTGENCUSTPROP querycode = queryResult.getUnique();
                              if (querycode != null) {

                                    NEFTTime = querycode.getPropval().trim();

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "NEFT value Blank-------->" + NEFTTime);
                                    }

                              }
                              if (NEFTTime.equalsIgnoreCase("NO") && (rtgs.equalsIgnoreCase("NEF"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Authoriser")
                                                      || step_csm.equalsIgnoreCase("Authorise"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Today is holiday. No NEFT entry will proceed [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Today is not holyday so NEFT will avilable");
                                    }
                              }

                        } else if (rtgs.equalsIgnoreCase("RTG")) {

                              String RTGSHOLY = "RTGSWorking";
                              String RTGSTime = "";
                              AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                          .createQuery("EXTGENCUSTPROP", "PROPNAME='" + RTGSHOLY + "'");
                              // //Loggers.general().info(LOG,"ADDDailyTxnLimit
                              // initially-------->");
                              EXTGENCUSTPROP querycode = queryResult.getUnique();
                              if (querycode != null) {

                                    RTGSTime = querycode.getPropval().trim();

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "RTGS value Blank-------->" + RTGSTime);
                                    }

                              }
                              if (RTGSTime.equalsIgnoreCase("NO") && (rtgs.equalsIgnoreCase("RTG"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Authoriser")
                                                      || step_csm.equalsIgnoreCase("Authorise"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Today is holiday. No RTGS entry will proceed [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Today is not holyday so RTGS will avilable");
                                    }
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception RTGS/NEFT holiday" + e.getMessage());
                        }
                  }
                  String bankpay = getWrapper().getRTGNFT().trim();
                  try {
                        String RTGS = "RTGSCUTOFFTIME";
                        String RTGSTime = "";
                        AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                    .createQuery("EXTGENCUSTPROP", "PROPNAME='" + RTGS + "'");
                        // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                        EXTGENCUSTPROP querycode = queryResult.getUnique();
                        if (querycode != null) {

                              RTGSTime = querycode.getPropval();
                              // //Loggers.general().info(LOG,"RTGS time for cut off-------->" +
                              // querycode.getPropval());
                        } else {
                              // Loggers.general().info(LOG,"RTGS time for cut off is
                              // empty-------->");

                        }
                        // Loggers.general().info(LOG,"RTGS time for cut off from static
                        // table-------->" + RTGSTime);
                        // String RTGSTime = "14:30:47";
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        // //Loggers.general().info(LOG,"Current Time " +
                        // dateFormat.format(date));
                        String str1 = dateFormat.format(date);
                        Date t1 = null;
                        try {
                              t1 = new SimpleDateFormat("HH:mm:ss").parse(str1);
                        } catch (ParseException e) {
                              e.printStackTrace();
                        }
                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(t1);

                        Date d = null;
                        try {
                              d = new SimpleDateFormat("HH:mm:ss").parse(RTGSTime);
                        } catch (ParseException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                        }
                        Calendar c3 = Calendar.getInstance();
                        c3.setTime(d);
                        c3.add(Calendar.DATE, 0);

                        Date x = c3.getTime();
                        if (x.before(c1.getTime()) && rtgs.equalsIgnoreCase("RTG") && bankpay.equalsIgnoreCase("B2C")
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CSM"))) {
                              // Loggers.general().info(LOG,"RTGS cut off TIME is exceeded");
                              validationDetails.addError(ErrorType.Other, "RTGS cut-off time has been exceeded [CM]");
                        } else {
                              // Loggers.general().info(LOG,"RTGS cut off TIME is available");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"RTGS cut off TIME Exception" +
                        // e.getMessage());
                  }

                  try {
                        String NEFT = "NEFTCUTOFFTIME";
                        String NEFTTime = "";
                        AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                    .createQuery("EXTGENCUSTPROP", "PROPNAME='" + NEFT + "'");
                        // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                        EXTGENCUSTPROP querycode = queryResult.getUnique();
                        if (querycode != null) {

                              NEFTTime = querycode.getPropval();
                              // //Loggers.general().info(LOG,"NEFTTime time for cut off-------->"
                              // +
                              // querycode.getPropval());
                        } else {
                              // Loggers.general().info(LOG,"NEFTTime time for cut off is
                              // empty-------->");

                        }
                        // //Loggers.general().info(LOG,"NEFTTime time for cut off from static
                        // table-------->" + NEFTTime);
                        // String RTGSTime = "14:30:47";
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        // //Loggers.general().info(LOG,"Current Time " +
                        // dateFormat.format(date));
                        String str1 = dateFormat.format(date);
                        Date t1 = null;
                        try {
                              t1 = new SimpleDateFormat("HH:mm:ss").parse(str1);
                        } catch (ParseException e) {
                              e.printStackTrace();
                        }
                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(t1);

                        Date d = null;
                        try {
                              d = new SimpleDateFormat("HH:mm:ss").parse(NEFTTime);
                        } catch (ParseException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                        }
                        Calendar c3 = Calendar.getInstance();
                        c3.setTime(d);
                        c3.add(Calendar.DATE, 0);

                        Date x = c3.getTime();
                        if (x.before(c1.getTime()) && rtgs.equalsIgnoreCase("NEF")
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CSM"))) {
                              // //Loggers.general().info(LOG,"RTGS cut off TIME is exceeded");
                              validationDetails.addError(ErrorType.Other, "NEFT cut-off time has been exceeded [CM]");
                        } else {
                              // Loggers.general().info(LOG,"NEFT cut off TIME is avilable");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"NEFT cut off TIME Exception" +
                        // e.getMessage());
                  }

                  try {
                        String RTGS = "BANKTOBANK";
                        String RTGSTime = "";
                        AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                    .createQuery("EXTGENCUSTPROP", "PROPNAME='" + RTGS + "'");
                        // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                        EXTGENCUSTPROP querycode = queryResult.getUnique();
                        if (querycode != null) {

                              RTGSTime = querycode.getPropval();
                              // //Loggers.general().info(LOG,"RTGS time for cut off-------->" +
                              // querycode.getPropval());
                        } else {
                              // Loggers.general().info(LOG,"Bank to Bank Payment time for cut off
                              // is empty-------->");

                        }
                        // Loggers.general().info(LOG,"Bank to Bank Payment time for cut off
                        // from static table-------->" + RTGSTime);
                        // String RTGSTime = "14:30:47";
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        // //Loggers.general().info(LOG,"Current Time " +
                        // dateFormat.format(date));
                        String str1 = dateFormat.format(date);
                        Date t1 = null;
                        try {
                              t1 = new SimpleDateFormat("HH:mm:ss").parse(str1);
                        } catch (ParseException e) {
                              e.printStackTrace();
                        }
                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(t1);

                        Date d = null;
                        try {
                              d = new SimpleDateFormat("HH:mm:ss").parse(RTGSTime);
                        } catch (ParseException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                        }
                        Calendar c3 = Calendar.getInstance();
                        c3.setTime(d);
                        c3.add(Calendar.DATE, 0);

                        Date x = c3.getTime();
                        if (x.before(c1.getTime()) && bankpay.equalsIgnoreCase("B2B")
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CSM"))) {
                              // Loggers.general().info(LOG,"RTGS cut off TIME is exceeded");
                              validationDetails.addError(ErrorType.Other,
                                          "Bank to Bank Payment cut-off time has been exceeded [CM]");
                        } else {
                              // Loggers.general().info(LOG,"Bank to Bank Payment cut off TIME is
                              // available");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Bank to Bank Payment cut off TIME
                        // Exception" + e.getMessage());
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

                  String paymentType = getWrapper().getPROREMT();

                  try {
                        Loggers.general().info(LOG, "Entered into UTR ---- ");
                        getutrNoGenerated();
                  } catch (Exception ee) {
                        Loggers.general().info(LOG, "UTR Number getutrNoGenerated" + ee.getMessage());

                  }
                  try {
                        // String paymentType = getWrapper().getPROREMT();
                        String utrNum = getWrapper().getUTRNO().trim();
                        if (paymentType.equalsIgnoreCase("RTG") && utrNum.length() != 22
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CBS Maker1") || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Reject"))
                                    && rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N")) {
                              validationDetails.addError(ErrorType.Other, "Please review UTR number for RTGS[CM]");
                        } else if (paymentType.equalsIgnoreCase("NEF") && utrNum.length() != 16
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CBS Maker1") || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Reject"))
                                    && rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N")) {
                              validationDetails.addError(ErrorType.Other, "Please review UTR number for NEFT[CM]");
                        } else {
                              Loggers.general().info(LOG, "Lenth is correct");
                        }

                  } catch (Exception e) {
                        e.printStackTrace();
                  }

                  try {
                        int cnt = 0;
                        cnt = getduplicate();
                        if (cnt > 1) {
                              validationDetails.addError(ErrorType.Other,
                                          "The event reference no already exsist kindly abort and recreate the event [CM]");
                        }
                  } catch (Exception ee) {
                        Loggers.general().info(LOG, "UTR Number getutrNoGenerated" + ee.getMessage());

                  }
                  // Update value
                  String rtgspart = getWrapper().getRTGSPART();
                  // if (rtgspart.equalsIgnoreCase("FULL")) {
                  // try {
                  // con = getConnection();
                  // String sql = "SELECT * from KMB_RTGS_NEFT_VIEW where MASTER_REF =
                  // '" + MasterReference
                  // + "' AND EVENT_REF = '" + eventCode + "'";
                  // // Loggers.general().info(LOG,"Query value for RTGS amount--->" +
                  // // sql);
                  // ps = con.prepareStatement(sql);
                  // rs = ps.executeQuery();
                  // while (rs.next()) {
                  // String rtgsVal = rs.getString(3);
                  // getPane().setRTGSNEFT(rtgsVal + " INR");
                  // getWrapper().setRTGSNEFT(rtgsVal + " INR");
                  // // //Loggers.general().info(LOG,"Query value in while-->" +
                  // // rtgsVal);
                  // //
                  // // String updatequry = "UPDATE EXTEVENT EXTE SET
                  // // EXTE.RTGSNEFT ='" + rtgsVal
                  // // + "', CCY_62 = 'INR' where EXTE.event in (SELECT
                  // // BEV.KEY97 FROM MASTER MAS, BASEEVENT BEV where
                  // // mas.KEY97 = bev.MASTER_KEY and mas.master_ref = '"
                  // // + MasterReference + "' and
                  // // BEV.REFNO_PFIX||lpad(bev.refno_serl,3,0) = '" +
                  // // eventCode
                  // // + "' )";
                  // // // //Loggers.general().info(LOG,"Update--->" + updatequry);
                  // // ps2 = con.prepareStatement(updatequry);
                  // // int insetrResult = ps2.executeUpdate();
                  // // // //Loggers.general().info(LOG,"Update value
                  // // insetrResult--->" +
                  // // // insetrResult);
                  //
                  // }
                  //
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"Exception update" + e.getMessage());
                  // } finally {
                  // try {
                  // if (con != null) {
                  // con.close();
                  // if (ps != null)
                  // ps.close();
                  // if (ps2 != null)
                  // ps2.close();
                  // if (rs != null)
                  // rs.close();
                  // }
                  // } catch (SQLException e) {
                  // // Loggers.general().info(LOG,"Connection Failed! Check output
                  // // console");
                  // e.printStackTrace();
                  // }
                  // }
                  // } else {
                  // Loggers.general().info(LOG,"RTGS/NEFT type is part");

                  // }

                  // String rtgs_amt = getWrapper().getRTGSNEFT();
                  // double rtgs_lim = 200000;
                  // // //Loggers.general().info(LOG,"RTGS NEFT value and type--->" + rtgs +
                  // "and"
                  // // + rtgs_amt);
                  // if (rtgs_amt.length() > 0 && rtgs.equalsIgnoreCase("RTG")) {
                  // double rtgs_dou = Double.valueOf(rtgs_amt);
                  // double rtgs_double = rtgs_dou / 100;
                  // if (rtgs_double < rtgs_lim && step_Input.equalsIgnoreCase("i") &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // validationDetails.addError(ErrorType.Other, "RTGS amount should
                  // not less than 200000 INR [CM]");
                  // } else {
                  // //Loggers.general().info(LOG,"RTGS NEFT value and type validation
                  // part--->"
                  // + rtgs + "and" + rtgs_amt);
                  // }
                  // } else {
                  // //Loggers.general().info(LOG,"RTGS NEFT value and type else loop--->" +
                  // rtgs + "and" + rtgs_amt);
                  // }

                  // CR220 Changes Startes here
                  String subProductype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  if (getMajorCode().equalsIgnoreCase("CPCO") && (subProductype.equalsIgnoreCase("AIR")
                              || subProductype.equalsIgnoreCase("DIR") || subProductype.equalsIgnoreCase("OTT"))) {
                        Loggers.general().info(LOG, "CR-220 Validation starts here");
                        try {
                              con = getConnection();
                              String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
                                          + MasterReference + "' and BEV.REFNO_PFIX= '" + evnt + "' and BEV.REFNO_SERL = '" + evvcount
                                          + "'";
                              int count = 0;
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                              }

                              if (count > 0 && (step_csm.equalsIgnoreCase("CBS Authoriser")
                                          || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    Loggers.general().info(LOG, "CR-220 Validation setting up Error");
                                    validationDetails.addError(ErrorType.Other, "FX deal rate is outside specified tolerance [CM]");
                              } else {
                              }

                        } catch (Exception e) {
                              Loggers.general().info(LOG, "CR-220 Validation Exception occured");
                              e.printStackTrace();
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
                                    e.printStackTrace();
                              }
                        }
                        Loggers.general().info(LOG, "CR-220 Validation starts here");
                  }
                  // CR220 Changes Ends here

                  // CR 215 starts here

                  if ((getMinorCode().equalsIgnoreCase("PCOP") || getMinorCode().equalsIgnoreCase("PBOP"))
                              && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Authoriser"))) {

                        int getAccountFromTI = 0;

                        try {
                              getAccountFromTI = getAccountFromTI();

                              System.out.println("Result getAccountFromTI-->" + getAccountFromTI);

                              if (getAccountFromTI > 0) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Manual book keeping entries not allowed for these GL Accounts [CM]");
                              }

                        } catch (Exception e) {
                              System.out.println("Exception in getAcoount-->" + e.getMessage());
                              e.printStackTrace();
                        }

                  }

                  // CR 215 ends here
                  String eventStep = getDriverWrapper().getEventFieldAsText("CSTD", "s", "");

                  if ((getMajorCode().equalsIgnoreCase("CPCO")) && (getMinorCode().equalsIgnoreCase("PCOC"))
                              || (eventStep.equalsIgnoreCase("Input") || eventStep.equalsIgnoreCase("Review - final")
                                          || eventStep.equalsIgnoreCase("Post release"))) {
                        System.out.println("TCS on LRS");
                        String masRef = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                        String eveRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
                        String prodType = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                        if (prodType.equalsIgnoreCase("64R") || prodType.equalsIgnoreCase("65R")) {
                              // remittanceLRSChargeChange(masRef, eveRef);
                              TCSInProgressValidation(masRef, eveRef, validationDetails);
                        }
                        String rbipurposecode = getDriverWrapper().getEventFieldAsText("cAQL", "s", "");
//                      if (rbipurposecode.equalsIgnoreCase("S0023") || rbipurposecode.equalsIgnoreCase("S0001")
//                                  || rbipurposecode.equalsIgnoreCase("S0002") || rbipurposecode.equalsIgnoreCase("S0003")
//                                  || rbipurposecode.equalsIgnoreCase("S0004") || rbipurposecode.equalsIgnoreCase("S0005")
//                                  || rbipurposecode.equalsIgnoreCase("S0021") || rbipurposecode.equalsIgnoreCase("S0022")
//                                  || rbipurposecode.equalsIgnoreCase("S0301") || rbipurposecode.equalsIgnoreCase("S0303")
//                                  || rbipurposecode.equalsIgnoreCase("S0304") || rbipurposecode.equalsIgnoreCase("S0305")
//                                  || rbipurposecode.equalsIgnoreCase("S0306") || rbipurposecode.equalsIgnoreCase("S1301")
//                                  || rbipurposecode.equalsIgnoreCase("S1302") || rbipurposecode.equalsIgnoreCase("S1303")
//                                  || rbipurposecode.equalsIgnoreCase("S1107") || rbipurposecode.equalsIgnoreCase("S1108")
//                                  || rbipurposecode.equalsIgnoreCase("S1307") || rbipurposecode.equalsIgnoreCase("S0011")
//                                  || rbipurposecode.equalsIgnoreCase("S0603")) {
//                      }
                        if (prodType.equalsIgnoreCase("64R") || prodType.equalsIgnoreCase("65R")) {
                              try {
                                    String result_val = "";

                                    String postFXRate = "";
                                    System.out.println("In taxable amount calculation");

                                    con = getConnection();
                                    getPane().setEDTCSRAT("");
                                    getPane().setTCSRATE("");
                                    getPane().setAMTPAID("");
                                    getPane().setLRSCRAMT("");
                                    getPane().setLRSCMAMT("");
                                    getPane().setLRSINAMT("");
                                    getPane().setLRSCRINT("");
                                    String masCurr = getDriverWrapper().getEventFieldAsText("EVAM", "v", "c");
                                    // String masRef = getDriverWrapper().getEventFieldAsText("MST", "r",
                                    // "").trim();
                                    String payment = getDriverWrapper().getEventFieldAsText("EVAM", "v", "m");
                                    String utilizedAmount = getDriverWrapper().getEventFieldAsText("cAVG", "v", "m");
                                    String education = getDriverWrapper().getEventFieldAsText("cAVF", "s", "");
                                    String educationAmount = getDriverWrapper().getEventFieldAsText("cAVZ", "v", "m");
                                    String taxableExemAmount = getDriverWrapper().getEventFieldAsText("cAWF", "v", "m");
                                    String loanRemmitance = getDriverWrapper().getEventFieldAsText("cBUI", "v", "m");
                                    String currTxn = "";
                                    String fcyt = getDriverWrapper().getEventFieldAsText("FCYT", "v", "m");
////                                                  String fxcontract="SELECT MAX(fxc.FX_RATE),mas.MASTER_REF from master mas,baseevent bev,relitem rel, "+
////                                                              "fxbasedeal fx,fxcontract fxc "+
////                                                              "where fxc.key97=fx.FXCONTRACT "+
////                                                              "and fx.key97=rel.key97 "+
////                                                              "and rel.event_key=bev.key97 "+
////                                                              "and bev.master_key=mas.key97 "+
////                                                              "and mas.master_ref='"+masRef+"' "+
////                                                              "and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+eveRef+"' group by mas.master_ref";
////                                                  System.out.println(" fx contract sql "+fxcontract);
                                    ////
////                                                  ps = con.prepareStatement(fxcontract);
////                                                  rs = ps.executeQuery();
////                                                  while (rs.next()) {
////                                                        result_val = rs.getString(1).trim();
////                                                        System.out.println("Status on fxcontract rate max==>" + result_val);
////                                                  }
                                    System.out.println("fcyt AND totalAmt" + payment + " " + fcyt + " " + loanRemmitance);
                                    BigDecimal netPayAmt = new BigDecimal(0);
                                    BigDecimal taxableAmt = new BigDecimal(0);
                                    BigDecimal utilizedAmount1 = new BigDecimal(0);
                                    BigDecimal loanRem = new BigDecimal(0);
                                    BigDecimal educationloan = new BigDecimal(0);
                                    BigDecimal TaxablePersonalFunds = new BigDecimal(0);
                                    BigDecimal totalAmt = new BigDecimal(0);
                                    BigDecimal fcytb = new BigDecimal(0);
                                    BigDecimal currTxnBig = new BigDecimal(0);
                                    // BigDecimal educationAmt = new BigDecimal(educationAmount);
                                    String query = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='" + masCurr
                                                + "'";

                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          postFXRate = rs1.getString(1);
                                          // getPane().setPOSTRATE(postFXRate);

                                    }

                                    BigDecimal rate1 = new BigDecimal(postFXRate);
                                    BigDecimal advAmt = new BigDecimal(payment);

                                    netPayAmt = rate1.multiply(advAmt);

                                    // BigDecimal nonEducationAmt = netPayAmt.subtract(educationAmt);
                                    BigDecimal taxAmount = new BigDecimal(taxableExemAmount);
                                    if (!utilizedAmount.equalsIgnoreCase("") && utilizedAmount != null
                                                && !utilizedAmount.trim().isEmpty()) {
                                          utilizedAmount1 = new BigDecimal(utilizedAmount);
                                    }
                                    if (!loanRemmitance.equalsIgnoreCase("") && loanRemmitance != null
                                                && !loanRemmitance.trim().isEmpty()) {
                                          loanRem = new BigDecimal(loanRemmitance);
                                    }
                                    if (!fcyt.trim().isEmpty() && fcyt != null && !fcyt.equalsIgnoreCase("")) {
                                          fcytb = new BigDecimal(fcyt);
                                          System.out.println("utilizedAmount1 AND totalAmt in fcyt " + utilizedAmount1 + " " + fcyt
                                                      + " " + loanRem);
                                          getPane().setLRSCRAMT(fcyt + " " + "INR");
                                          currTxn = fcyt;
                                          totalAmt = fcytb.add(utilizedAmount1);
                                    } else {
                                          getPane().setLRSCRAMT(netPayAmt + " " + "INR");
                                          currTxn = netPayAmt.toString();
                                          totalAmt = netPayAmt.add(utilizedAmount1);
                                    }

                                    getPane().setLRSCMAMT(totalAmt.toString() + " " + "INR");
                                    System.out.println("utilizedAmount1 AND totalAmt" + utilizedAmount1 + " " + totalAmt + " "
                                                + netPayAmt + " " + currTxn);
                                    if (education.equalsIgnoreCase("Y")) {
                                          getPane().setEDTCSRAT("0.5%");
                                    } else if (education.equalsIgnoreCase("N")) {
                                          getPane().setTCSRATE("5%");
                                    }
                                    currTxnBig = new BigDecimal(currTxn);
                                    taxableAmt = totalAmt.subtract(taxAmount);
                                    if (taxableAmt.compareTo(BigDecimal.ZERO) == -1) {
                                          getPane().setAMTPAID("0.00 INR");
                                    } else if (taxableAmt.compareTo(currTxnBig) == 1) {
                                          getPane().setAMTPAID(currTxnBig + " " + "INR");
                                          TaxablePersonalFunds = currTxnBig.subtract(loanRem);
                                          if (TaxablePersonalFunds.compareTo(BigDecimal.ZERO) >= 0)
                                                getPane().setLRSCRINT(TaxablePersonalFunds.toString() + " " + "INR");
                                          else {
                                                getPane().setLRSCRINT("0.00 INR");
                                          }
                                    } else if (taxableAmt.compareTo(currTxnBig) == -1) {
                                          getPane().setAMTPAID(taxableAmt + " " + "INR");
                                          TaxablePersonalFunds = taxableAmt.subtract(loanRem);
                                          if (TaxablePersonalFunds.compareTo(BigDecimal.ZERO) >= 0) {
                                                getPane().setLRSCRINT(TaxablePersonalFunds.toString() + " " + "INR");
                                          } else {
                                                getPane().setLRSCRINT("0.00 INR");
                                          }
                                    }

                                    System.out.println("tAXABLE AMOUNT " + taxableAmt + " " + taxAmount + " " + loanRem);
                                    if (taxableAmt.compareTo(loanRem) == 1 || taxableAmt.compareTo(loanRem) == 0) {
                                          getPane().setLRSINAMT(loanRem.toString() + " " + "INR");

                                    } else if (loanRem.compareTo(taxableAmt) == 1 && taxableAmt.compareTo(BigDecimal.ZERO) == 1) {
                                          getPane().setLRSINAMT(taxableAmt.toString() + " " + "INR");

                                    } else if (taxableAmt.compareTo(BigDecimal.ZERO) == -1) {
                                          getPane().setLRSINAMT("0.00 INR");
                                    }

                                    /*
                                     * else if(education.equalsIgnoreCase("B")) {
                                     *
                                     * getPane().setTCSRATE("5%"); getPane().setEDTCSRAT("0.5%"); BigDecimal
                                     * educationAmt = new BigDecimal(educationAmount); BigDecimal nonEducationAmt =
                                     * netPayAmt.subtract(educationAmt); String
                                     * nonEducationAmt1=nonEducationAmt.toString();
                                     * getPane().setLRSCRINT(nonEducationAmt1+" "+"INR"); }
                                     */
                                    //
////                                                  if (taxAmount.compareTo(utilizedAmount1) == 1 && netPayAmt.compareTo(taxAmount) == 1) {
////                                                        System.out.println("rate and advamt and netpayamt" + rate + " " + advAmt + " " + netPayAmt);
////                                                        BigDecimal netAmt = taxAmount.subtract(utilizedAmount1);
////                                                        taxableAmt = netPayAmt.subtract(netAmt);
////                                                        String taxableAmt1 = taxableAmt.toString();
////                                                        System.out.println("netAmt and taxableAmt and taxableAmt1 " + netAmt + " " + taxableAmt
////                                                                    + " " + taxableAmt1);
////                                                        getPane().setAMTPAID(taxableAmt1 + " " + "INR");
////                                                  }
////                                                  if (utilizedAmount1.compareTo(taxAmount) == 1 || utilizedAmount1.compareTo(taxAmount) == 0) {
////                                                        // System.out.println("TOTAL Pay Amount Population");
                                    ////
////                                                        String taxableAmt2 = netPayAmt.toString();
////                                                        System.out.println("TOTAL Pay Amount Population" + taxableAmt2);
////                                                        getPane().setAMTPAID(taxableAmt2 + " " + "INR");
////                                                  }
////                                                  if (utilizedAmount.equalsIgnoreCase("0.00") && taxAmount.compareTo(netPayAmt) == 1) {
////                                                        System.out.println("TOTAL Pay Amount Population");
                                    ////
////                                                        String taxableAmt2 = "0.00 INR";
////                                                        System.out.println("TOTAL Pay Amount Population" + taxableAmt2);
////                                                        getPane().setAMTPAID("0.00 INR");
////                                                  }
////                                                  if (taxAmount.compareTo(totalAmt) == 1 || taxAmount.compareTo(totalAmt) == 0) {
////                                                        String taxableAmt2 = "0.00 INR";
////                                                        System.out.println("TOTAL Pay Amount Population" + taxableAmt2);
////                                                        getPane().setAMTPAID("0.00 INR");
////                                                  }
////                                                  if (taxAmount.compareTo(utilizedAmount1) == 1 && taxAmount.compareTo(netPayAmt) == 1
////                                                              && totalAmt.compareTo(taxAmount) == 1) {
////                                                        System.out.println("rate and advamt and netpayamt" + rate + " " + advAmt + " " + netPayAmt);
////                                                        BigDecimal netAmt = taxAmount.subtract(utilizedAmount1);
////                                                        taxableAmt = netPayAmt.subtract(netAmt);
////                                                        String taxableAmt1 = taxableAmt.toString();
////                                                        System.out.println(
////                                                                    " taxableAmt and taxableAmt1 " + netAmt + " " + taxableAmt + " " + taxableAmt1);
////                                                        getPane().setAMTPAID(taxableAmt1 + " " + "INR");
////                                                  }
//                                                    if (education.equalsIgnoreCase("B")) {
                                    //
//                                                          getPane().setTCSRATE("5%");
//                                                          getPane().setEDTCSRAT("0.5%");
//                                                          // BigDecimal educationAmt = new BigDecimal(educationAmount);
//                                                          // BigDecimal nonEducationAmt = netPayAmt.subtract(educationAmt);
//                                                          // String nonEducationAmt1=nonEducationAmt.toString();
//                                                          // getPane().setLRSCRINT(nonEducationAmt1+" "+"INR");
//                                                    }
                                    //
                              } catch (Exception e) {
                                    e.printStackTrace();
                              } finally {

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
                        }
                  }
                  try {
                        getPostingFxrate();
                  } catch (Exception e) {

                  }
                  if (getMajorCode().equalsIgnoreCase("CPCO") && getMinorCode().equalsIgnoreCase("PCOC")) {
                        try {
                              String customer = getDriverWrapper().getEventFieldAsText("RMPB", "s", "");
                              con = getConnection();
                              String query = "SELECT IECODE  FROM EXTCUST where CUST = '" + customer + "'";
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String iecode1 = rs1.getString(1);
                                    getPane().setIECODE(iecode1);
                              }

                        } catch (Exception e) {
                              e.printStackTrace();
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
                  if (getMajorCode().equalsIgnoreCase("CPCO") && getMinorCode().equalsIgnoreCase("PCOC")) {
                        try {
                              String applicant = getDriverWrapper().getEventFieldAsText("RMPB", "s", "").trim();
                              con = getConnection();
                              // TO_CHAR(COLM.ISSUE_DATE,'DD/MM/YYYY')
                              String query = "SELECT ext.PASSPORT,TO_CHAR(ext.ISSDATE,'DD/MM/YY'),TO_CHAR(ext.EXPRYDAT,'DD/MM/YY') FROM EXTCUST ext where CUST = '"
                                          + applicant + "'";
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String passportno = rs1.getString(1);
                                    String issudat = rs1.getString(2);
                                    String expirydat = rs1.getString(3);
                                    System.out.println("passport number, issue ,expiry: " + passportno + " " + issudat);
                                    getPane().setPASPRTNO(passportno);
                                    getPane().setISSUEDAT(issudat);
                                    getPane().setEXPRYDAT(expirydat);

                              }

                        } catch (Exception ee) {
                              ee.printStackTrace();
                              // Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());

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

                        masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                        eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                        String error = null;
                        String remark = null;
                        con = ConnectionMaster.getConnection();
                        try {
                              String payQuery = "SELECT E_OR_W,REMARKS FROM REP_CPCO_DGFT_ORM_VALDIAITON WHERE BILLREFNO='"
                                          + masterref + "' AND PAY_SERIAL_NO='" + eventCode + "'";
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
                  // lrnDetails error by Vishal G --12042022
//                                  if (getMajorCode().equalsIgnoreCase("CPCO") && getMinorCode().equalsIgnoreCase("PCOC")) {
//                                        try {
//                                              String applicant = getDriverWrapper().getEventFieldAsText("RMPB", "s", "").trim();
//                                              con = getConnection();
//                                              String rate="";
//                                              String usdRate="";
//                                              BigDecimal amtRemitted1=new BigDecimal(0);
//                                              getPane().setCRCURRAT("");
//                                              getPane().setCRCURAMT("");
//                                              BigDecimal totalRem = new BigDecimal(0);
//                                              BigDecimal payamount = new BigDecimal(0);
//                                              BigDecimal usdAmount = new BigDecimal(0);
//                                              BigDecimal fixLimit = new BigDecimal(250000);
//                                              String masCurr = getDriverWrapper().getEventFieldAsText("EVAM", "v", "c");
//                                              String payment=getDriverWrapper().getEventFieldAsText("EVAM", "v", "m");
//                                              String amtRemitted = getDriverWrapper().getEventFieldAsText("cBUY", "v", "m");
//                                              if(amtRemitted!=null && !amtRemitted.equalsIgnoreCase("")&& !amtRemitted.trim().isEmpty()) {
//                                               amtRemitted1 = new BigDecimal(amtRemitted);
//                                              }
//                                              if(!masCurr.equalsIgnoreCase("USD")) {
//                                              String query = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='"+masCurr+"'";
//                                              String query1 = "SELECT SELLEX99 FROM FXRATE86 WHERE CODE53='BLS' AND CURREN49='USD'";
//                                              ps1 = con.prepareStatement(query);
//                                              rs1 = ps1.executeQuery();
//                                              while(rs1.next()) {
//                                                     rate = rs1.getString(1);
//                                              }
//                                              ps1 = con.prepareStatement(query1);
//                                              rs1 = ps1.executeQuery();
//                                              while(rs1.next()) {
//                                                     usdRate = rs1.getString(1);
//                                                    System.out.println("USD rate: "+usdRate);
//                                              }
//                                              BigDecimal rate1= new BigDecimal(rate);
//                                              BigDecimal usdRate1=new BigDecimal(usdRate);
//                                              payamount = new BigDecimal(payment);
//                                              BigDecimal convRate = rate1.divide(usdRate1,4, RoundingMode.HALF_UP);
//                                              getPane().setCRCURRAT(convRate.toString());
//                                              usdAmount = convRate.multiply(payamount);
//                                              getPane().setCRCURAMT(usdAmount.toString()+" "+"USD");
//                                              totalRem = amtRemitted1.add(usdAmount);
//                                              }
//                                              else {
//                                                    payamount = new BigDecimal(payment);
//                                                    totalRem = amtRemitted1.add(payamount);
//                                              }
//                                              if(totalRem.compareTo(fixLimit)==1) {
//                                                    validationDetails.addError(ErrorType.Other, "For particular client remittance has already exceeded 2,50,000 USD in current ");
//                                              }
//                                        }
//                                        catch (Exception e) {
//                                              e.printStackTrace();
//                                              
//                                        }
//                                        finally {
//
//                                              try {
//                                                    if (rs1 != null)
//                                                          rs1.close();
//                                                    if (ps1 != null)
//                                                          ps1.close();
//                                                    if (con != null)
//                                                          con.close();
//                                              } catch (SQLException e) {
//                                                    // Loggers.general().info(LOG,"Connection Failed! Check output
//                                                    // console");
//                                                    e.printStackTrace();
//                                              }
//                                        
//                                            }
//                                  }
                  if (getMajorCode().equalsIgnoreCase("CPCO") && getMinorCode().equalsIgnoreCase("PCOC")) {
                        try {
                              String dfbAprroval = getDriverWrapper().getEventFieldAsText("cBUR", "s", "").trim();
                              String investee = getDriverWrapper().getEventFieldAsText("cBUV", "s", "").trim();
                              String prodsubtype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                              String rbipurposecode = getDriverWrapper().getEventFieldAsText("cAQL", "s", "");
                              String lrnDetails = getDriverWrapper().getEventFieldAsText("cBUW", "s", "").trim();
                              if (rbipurposecode.equalsIgnoreCase("S0003")
                                          || rbipurposecode.equalsIgnoreCase("S0004") && prodsubtype.equalsIgnoreCase("65R")) {
                                    if (dfbAprroval.length() != 20) {
                                          System.out.println("dfb :  error " + dfbAprroval.length());
                                          validationDetails.addError(ErrorType.Other,
                                                      " Length of DFB Approval details should be of 20  ");
                                    }
                              }
                              if (rbipurposecode.equalsIgnoreCase("S0006") && prodsubtype.equalsIgnoreCase("72R")) {

                                    if (investee.length() != 15) {
                                          validationDetails.addError(ErrorType.Other,
                                                      " Length of investee company should be of 15  ");
                                    }
                              }
                              if (rbipurposecode.equalsIgnoreCase("S0012") && prodsubtype.equalsIgnoreCase("71R")) {
                                    if (lrnDetails.length() != 15) {
                                          System.out.println("dfb :  error " + lrnDetails.length());
                                          validationDetails.addError(ErrorType.Other, " Length of LRN details should be of 15  ");
                                    }
                              }

                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                        try {
                              String prodsubtype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                              if (prodsubtype.equalsIgnoreCase("51R") || prodsubtype.equalsIgnoreCase("52R")
                                          || prodsubtype.equalsIgnoreCase("53R") || prodsubtype.equalsIgnoreCase("54R")
                                          || prodsubtype.equalsIgnoreCase("55R")) {
                                    List<ExtEventAdvancePaymentDetails> advancePayment = (List<ExtEventAdvancePaymentDetails>) getWrapper()
                                                .getExtEventAdvancePaymentDetails();
                                    System.out.println("shipping table size" + advancePayment.size());
                                    if (advancePayment.size() == 0) {
                                          validationDetails.addError(ErrorType.Other, "Advance Payment details should be filled");
                                    }
                              }
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                        try {
                              String masRef = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                              String eveRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
                              String prodType = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                              // lrnDetails error by Vishal G --12042022
                              if ((prodType.equalsIgnoreCase("64R") || prodType.equalsIgnoreCase("65R"))
                                          && (step_Input.equalsIgnoreCase("i")))

                              {
                                    System.out.println("forward total value data:");
                                    remittanceLRSTCS(masRef, eveRef, validationDetails);
                              }
                        } catch (Exception e) {
                              e.printStackTrace();
                              // System.out.println("outside BUYERS data:"+e);

                        }
                        try {
                              String pan = getPane().getPANDETAI();
                              String prodsubtype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
//                                                                if(pan.length()!=10){
//                                                                            System.out.println("PAN error");
//                                                                validationDetails.addWarning(WarningType.Other, "PAN NO SHOULD BE 10 CHARACTER");
//                                                                }
                              if (!pan.trim().equalsIgnoreCase("") && pan != null && !pan.trim().isEmpty()) {
                                    if (!Character.toString(pan.charAt(3)).equalsIgnoreCase("P")
                                                && (prodsubtype.equalsIgnoreCase("64R") && step_Input.equalsIgnoreCase("i"))) {
                                          System.out.println("PAN error of p char");
                                          validationDetails.addWarning(WarningType.Other, "Pan card is not of Individual ");

                                    }
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }

                        try {
                              getBOEWarning(validationDetails);
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  }

                  if (getMajorCode().equalsIgnoreCase("CPCO")
                              || getMajorCode().equalsIgnoreCase("CPBO") && getMinorCode().equalsIgnoreCase("PCOC")
                              || getMinorCode().equalsIgnoreCase("PBOC")) {
                        try {
                              String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventref = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String custId = getDriverWrapper().getEventFieldAsText("PRM", "p", "cu").trim();
                              String uniqueId = getDriverWrapper().getEventFieldAsText("cBUB", "s", "").trim();
                              String branch1 = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                              String eventCode1 = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                              String inputXml = "<?xml version=\"1.0\" standalone=\"yes\"?><ServiceRequest xmlns:m='urn:messages.service.ti.apps.tiplus2.misys.com' xmlns='urn:control.services.tiplus2.misys.com' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                                          + "<RequestHeader>" + "<Service>Treasury</Service>" + "<Operation>Validate</Operation>"
                                          + "<Credentials>" + "<Name>Name</Name>" + "<Password>Password</Password>"
                                          + "<Certificate>Certificate</Certificate>" + "<Digest>Digest</Digest>" + "</Credentials>"
                                          + "<ReplyFormat>FULL</ReplyFormat>" + "<NoRepair>Y</NoRepair>"
                                          + "<NoOverride>Y</NoOverride>" + "<CorrelationId>CorrelationId</CorrelationId>"
                                          + "<TransactionControl>NONE</TransactionControl>" + "</RequestHeader>"
                                          + "<TreasuryValidateRequest>" + "<MASTERREF>" + masReference + "</MASTERREF>" + "<EVENTREF>"
                                          + eventref + "</EVENTREF>" + "<CUSTID>" + custId + "</CUSTID>" + "<UNIQUEID>" + uniqueId
                                          + "</UNIQUEID>" + "<INPUTBRANCH>" + branch1 + "</INPUTBRANCH>" + "<EVENTCODE>" + eventCode1
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
                        try {
                              getForwardReference();

                              // getipbranchCode();
                              System.out.println("forward contract reference:");
                        } catch (Exception e) {
                              e.printStackTrace();
                              // System.out.println("outside BUYERS data:"+e);

                        }
                        try {
                              String inputBranch = getDriverWrapper().getEventFieldAsText("BIN", "b", "s").trim();
                              String orderingInstitute = getDriverWrapper().getEventFieldAsText("ORDI", "p", "s").trim();
                              if (!inputBranch.equalsIgnoreCase(orderingInstitute)) {
                                    System.out
                                                .println("inputBranch and orderingInstitute " + inputBranch + " " + orderingInstitute);
                                    validationDetails.addError(ErrorType.Other,
                                                "SWIFT Code should be same of input branch and Ordering Ins.");
                              }

                        } catch (Exception e) {
                              e.printStackTrace();
                              // System.out.println("outside BUYERS data:"+e);

                        }
                  }
//                                  try {
//                                        String contractReference = getDriverWrapper().getEventFieldAsText("FOCR", "s", "");
//                                  //    String pay = getDriverWrapper().getEventFieldAsText("ACT", "s", "");
//                                        if(contractReference==null || contractReference.isEmpty() || contractReference.trim().equals("")) {
//                                              System.out.println("dfb :  error "+contractReference);
//                                              validationDetails.addWarning(WarningType.Other, "Reference Contract Number/Deal ID is not entered in Settlements tab[CM]");
//                                        }
//                                  }
//                                  catch (Exception ee) {
//                                        ee.printStackTrace();
//                                        //Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());
//
//                                  }
                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in currency value population---->" + e.getMessage());

                        }
                  }
                  if (getMajorCode().equalsIgnoreCase("CPCO")
                              || getMajorCode().equalsIgnoreCase("CPBO") && getMinorCode().equalsIgnoreCase("PCOC")
                              || getMinorCode().equalsIgnoreCase("PBOC")) {
                        try {
                              freezeMttNumber();
                              mttFreeze();
//                                              unsetMTTSeqNo();
                              setMTTRefNumber();
//                                              getMttSeqNo();
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
                              getErrorPurposeCode(validationDetails);
                              getCountryRisk();
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }

//                                        try {
//                                              fxcontractValidation();
//                                        } catch (Exception e) {
//
//                                              e.printStackTrace();
//                                        }

                        try {
                              getPostingBranch(validationDetails);
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  }

            } // migration value is no

      }

      private String getString(int i) {
            // TODO Auto-generated method stub
            return null;
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