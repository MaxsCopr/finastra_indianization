package com.misys.tiplus2.customisation.extension;

//com.misys.tiplus2.customisation.extension.Service
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisation;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTable;
import com.misys.tiplus2.customisation.entity.ExtEventBillReference;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceData;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceDetails;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
//import com.misys.tiplus2.customisation.entity.ExtEventFircDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingCollections;
import com.misys.tiplus2.customisation.entity.ExtEventShippingTable;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetails;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class IlcPoc extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(IlcPoc.class);
      private static final DateFormat localDate = null;
      // @Override
      PreparedStatement ps1, ps, ps2, dmsp, pst, ship_prepare = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ship_result = null;
      private ValidationDetails validationDetails;

      public boolean onPostInitialise() {
            // GETTING LOB
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
            String collAmtCcy = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
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
                  // Standing instruction link

                  // try {
                  // String Standing = getStandinglink().trim();
                  // ExtendedHyperlinkControlWrapper Standing1 =
                  // getPane().getCtlSTANDINGEXPCOLSETTclayHyperlink();
                  // Standing1.setUrl(Standing);
                  //
                  // String nostroAccountNum = getPane().getNOSTACC();
                  // String nostroCreditCCY = getWrapper().getPOOLAMTCurrency();
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"Account number for SI Availability check - "
                  // + nostroAccountNum
                  // + " And Credit CCY - " + nostroCreditCCY);
                  // }
                  // Standing1.setVisible(isSIAvailable(nostroAccountNum,
                  // nostroCreditCCY));
                  // } catch (Exception ees) {
                  // // Loggers.general().info(LOG,"Standing in ELC--------->" +
                  // // ees.getMessage());
                  // }
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (getMinorCode().equalsIgnoreCase("CLP")
                              && (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {
                        getPane().onNOSTROEXPCOLSETTclayButton();
                  }
                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane()
                                    .getCtlCSMCHECKLISTIMPSTALCOUTCLAclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMCHECKLISTEXPCOLSETTclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALIMPSTALCOUTCLAclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);
//                      ExtendedHyperlinkControlWrapper csmreftrack4 = getPane().getCtlCSMREFRALEXPCOLSETTclayHyperlink();
//                      csmreftrack4.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKIMPSTALCOUTCLAclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCPCCHECKEXPCOLSETTclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELIMPSTALCOUTCLAclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCPCREFRALEXPCOLSETTclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSEXPCOLSETTclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTILCSETTclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTIMPSTALCOUTCLAclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
//                      ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTEXPCOLSETTclayHyperlink();
//                      dmsh2.setUrl(TSTHyperlink);
//                      ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTELCSETTclayHyperlink();
//                      dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTEXPGRNTEEOPclayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // boe
                  try {
                        String val = "BOE";
                        String BoeLink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlBOEILCSETTclayHyperlink();
                        dmsh.setUrl(BoeLink);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                  }
                  // PRESHIPMENT
                  try {

                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlPreshipmentEXPCOLSETTclayHyperlink();
                        dmsh1.setUrl(Preshipment);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                  }

                  // InwardLink
                  try {
                        String HyperInward = getINWARDREM();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlINWARDEXPCOLSETTclayHyperlink();
                        InwardLink.setUrl(HyperInward);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"InwardLink exception----->" +//
                        // ees.getMessage());
                  }
                  // Get Packing Credit A/c Outstanding
                                    try {
                                          String Account = getACCOUNT();
                                          ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlACCOUNTEXPCOLSETTclayHyperlink();
                                          InwardLink.setUrl(Account);

                                    } catch (Exception ees) {
                                          // Loggers.general().info(LOG,"InwardLink exception----->" +//
                                          // ees.getMessage());
                                    }
                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                        String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //

                        // FCY Tax calculation
                        try {

                              // getFCCTCALCULATION();

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" +
                              // e.getMessage());
                        }

                        // commented by MISYS - 5351
                        // try {
                        // // //Loggers.general().info(LOG,"get value for LOB");
                        // getLob();
                        // } catch (Exception ee) {
                        // // Loggers.general().info(LOG,ee.getMessage());
                        // // //Loggers.general().info(LOG,"LOB Catch");
                        // } finally {
                        // // //Loggers.general().info(LOG,"finally LOB ");
                        // }
                        String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                        String step_Id = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();

                        // USD EQUIVALENT
//                      try {
//                            currencyCalc();
//                      } catch (Exception e) {
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());
//
//                            }
//                      }

                        if (getMajorCode().equalsIgnoreCase("ODC") && (prd_typ.equalsIgnoreCase("OCI"))) {
                              // Loggers.general().info(LOG,"Else systemOutput");
                              getPane().setUSDAMT("");
                              getPane().setCRAMT_Name("");
                              getPane().setINRAMT("");
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
                  //          getPane().setRTGSNEFT("");
                              getPane().setNARRTVE("");

                        }

                        if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")) {
                              EventPane pane = (EventPane) getPane();
                  //          getPane().onRTGSEXPCOLSETTclayButton();
                              pane.getBtnFetchshipdetEXPCOLSETTclay().setEnabled(false);
                              pane.getBtnFetchinvdetEXPCOLSETTclay().setEnabled(false);
                              pane.getExtEventShippingCollectionsNew().setEnabled(false);
                              pane.getExtEventShippingCollectionsDelete().setEnabled(false);
                              pane.getExtEventShippingdetailsNew().setEnabled(false);
                              pane.getExtEventShippingdetailsDelete().setEnabled(false);
                              pane.getExtEventShippingdetailsUpdate().setEnabled(false);
                              pane.getExtEventInvoiceDetailsNew().setEnabled(false);
                              pane.getExtEventInvoiceDetailsUpdate().setEnabled(false);
                              pane.getExtEventInvoiceDetailsDelete().setEnabled(false);
                        }

                        if ((prd_typ.equalsIgnoreCase("OCI")) && getMinorCode().equalsIgnoreCase("CLP")) {
                              EventPane pane = (EventPane) getPane();

                              pane.getExtEventAdvanceTableNew().setEnabled(false);
                              pane.getExtEventAdvanceTableDelete().setEnabled(false);
                              pane.getExtEventAdvanceTableUpdate().setEnabled(false);
                              pane.getBtnonfetchEXPBILLclay().setEnabled(false);
                              pane.getBtnonfetchEXPCOLSETTclay().setEnabled(false);

                        }

                        else {
                              // //Loggers.general().info(LOG,"Product type is forign");
                              EventPane pane = (EventPane) getPane();
                              pane.getExtEventAdvanceTableNew().setEnabled(true);
                              pane.getExtEventAdvanceTableDelete().setEnabled(true);
                              pane.getExtEventAdvanceTableUpdate().setEnabled(true);
                              pane.getBtnonfetchEXPBILLclay().setEnabled(true);
                              pane.getBtnonfetchEXPCOLSETTclay().setEnabled(true);
                        }

                        List<ExtEventShippingCollections> shipcol = (List<ExtEventShippingCollections>) getWrapper()
                                    .getExtEventShippingCollections();
                        if (shipcol.size() > 0) {
                              // //Loggers.general().info(LOG,"shipdetails --->" + shipcol);
                              EventPane pane = (EventPane) getPane();
                              pane.getExtEventShippingCollectionsDelete().setEnabled(false);
                        }

                        String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "").trim();
                        // //Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

                        // //Loggers.general().info(LOG,"step id check for CSM ----->" +
                        // step_Id);
                        String step_log = getDriverWrapper().getEventFieldAsText("ECOI", "s", "").trim();
                        String gateway = getDriverWrapper().getEventFieldAsText("FRGI", "l", "");

                        if (gateway.equalsIgnoreCase("Y")) {
                              getPane().setPERADV("");
                              getPane().setADVREC("");
                              getPane().setNETRECIV("");
                              getPane().setBILLPAY("");
                        }

                        if (step_Id.equalsIgnoreCase("CSM") && getMinorCode().equalsIgnoreCase("CLP")) {

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
            //                getPane().setRTGSNEFT("");
                              getPane().setUTRNO("");
                              getPane().setNARRTVE("");
                              getPane().setBADDRE_Name("");
                              getPane().setRTGSPART("");

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

                              //getPane().setRATECOV("");
                              //getPane().setRATEDET("");

                              getPane().setREPAYAMT("");
                              getPane().setTOTUSD(0 + " USD");
                              getPane().setTOTINR(0 + " INR");
                              getPane().setTOTEUR(0 + " EUR");
                              getPane().setTOTJPY(0 + " JPY");
                              getPane().setTOTGBP(0 + " GBP");

                        } else {
                              // Loggers.general().info(LOG,"Step id for else step---->" +
                              // step_log);
                        }

                        if (step_Id.equalsIgnoreCase("CSM") && getMinorCode().equalsIgnoreCase("CLP")
                                    && !gateway.equalsIgnoreCase("Y")) {
                              /*getPane().setNOSAMT("");
                              getPane().setNOSTOUT("");
                              */getPane().setEBRCFLAG("");

                              /*getPane().setNOSTRM("");
                              getPane().setNOSTMT("");
                              getPane().setNOSTAMT("");
                              getPane().setNOSTDAT("");

                              getPane().setPOOLAMT("");
                              getPane().setNOSTACC("");
                              getPane().setINWMSG("");
                              getPane().setMTMESG("");
*/                      }

                        String tranType = getWrapper().getTRANTYP();
                        // Loggers.general().info(LOG,"Write of Type---->" + tranType +
                        // "tranType---->" + tranType.length());
                        if (tranType.equalsIgnoreCase("WRITEOFF") && (prd_typ.equalsIgnoreCase("OCF"))
                                    && !step_Id.equalsIgnoreCase("CSM")) {
                              getPane().setEBRCFLAG("N");
                        } else if ((tranType.equalsIgnoreCase("") || !tranType.equalsIgnoreCase("WRITEOFF"))
                                    && (prd_typ.equalsIgnoreCase("OCF")) && !step_Id.equalsIgnoreCase("CSM")) {
                              getPane().setEBRCFLAG("Y");
                        } else if (step_Id.equalsIgnoreCase("CSM")) {
                              getPane().setEBRCFLAG("");
                        }

                        if ((step_log.equalsIgnoreCase("Log")
                                    || step_Id.equalsIgnoreCase("CSM") && getMinorCode().equalsIgnoreCase("CLP"))
                                    && (!gateway.equalsIgnoreCase("Y"))) {
                              getPane().setEBRCFLAG("");

                        }

                        // Code for
                        // Notional--------------------------------------------------------------
                        if (step_Input.equalsIgnoreCase("i")) {
                              try {
                                    List<ExtEventShippingCollections> shipTable = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();
                                    // //Loggers.general().info(LOG,"shipping table for notional
                                    // rate---->" + shipTable.size());
                                    for (int i = 0; i < shipTable.size(); i++) {

                                          ExtEventShippingCollections ship = shipTable.get(i);

                                          String not_str = ship.getCNOTIONL().toString();
                                          BigDecimal rAmt = ship.getCREPAY();
                                          String shiamtcuy = ship.getCSHPAMTCurrency();
                                          String reyamtcuy = ship.getCREPAYCurrency();

                                          double repay_Amount = 0.0;

                                          double notional = 1;
                                          try {
                                                con = getConnection();
                                                String query = "SELECT ETT_SPOTRATE_CAL('" + shiamtcuy + "','" + reyamtcuy
                                                            + "') FROM DUAL";
                                                // Loggers.general().info(LOG,"Notional rate function "
                                                // +
                                                // query);

                                                ps1 = con.prepareStatement(query);
                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      notional = rs1.getDouble(1);
                                                      // ship.setCNOTIONL(new
                                                      // BigDecimal(notional));
                                                }

                                                String temp_notRate = String.valueOf(notional);

                                                if (null != not_str && not_str.equalsIgnoreCase("1")) {
                                                      ship.setCNOTIONL(new BigDecimal(temp_notRate));
                                                      // Loggers.general().info(LOG,"shipping table
                                                      // notional
                                                      // length if loop====>" +
                                                      // ship.getCNOTIONL());

                                                      BigDecimal notional_big = new BigDecimal(notional);
                                                      BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                      // Loggers.general().info(LOG,"Notional rate +
                                                      // Repament
                                                      // amount in big decimal POD---->" +
                                                      // equi_bill);
                                                      ship.setCEQUBILL(equi_bill);
                                                      ship.setCEQUBILLCurrency(ship.getCSHPAMTCurrency());
                                                } else if (null != not_str && !temp_notRate.equalsIgnoreCase(not_str)) {
                                                      ship.setCNOTIONL(new BigDecimal(not_str));
                                                      // Loggers.general().info(LOG,"shipping table
                                                      // notional
                                                      // length else if loop====>" +
                                                      // ship.getCNOTIONL());
                                                      BigDecimal notional_big = new BigDecimal(not_str);
                                                      BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                      // Loggers.general().info(LOG,"Notional rate +
                                                      // Repament
                                                      // amount in big decimal POD---->" +
                                                      // equi_bill);
                                                      ship.setCEQUBILL(equi_bill);
                                                      ship.setCEQUBILLCurrency(ship.getCSHPAMTCurrency());

                                                } else {
                                                      // Loggers.general().info(LOG,"Notional rate is
                                                      // blank---->");
                                                }

                                          } catch (Exception e1) {
                                                // Loggers.general().info(LOG,"Exception" +
                                                // e1.getMessage());
                                          } finally {
                                                try {
                                                      if (rs1 != null)
                                                            rs1.close();
                                                      if (ps1 != null)
                                                            ps1.close();
                                                      if (con != null)
                                                            con.close();
                                                } catch (SQLException e) {
                                                      // Loggers.general().info(LOG,"Connection Failed!
                                                      // Check
                                                      // output console");
                                                      e.printStackTrace();
                                                }
                                          }

                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception in validating for
                                    // notional
                                    // rate--->" + e.getMessage());
                              }
                        }

                        List<ExtEventShippingdetails> shipdetails = (List<ExtEventShippingdetails>) getWrapper()
                                    .getExtEventShippingdetails();
                        if (shipdetails.size() == 0) {
                              // //Loggers.general().info(LOG,"shipdetails --->" + shipdetails);
                              EventPane pane = (EventPane) getPane();
                              pane.getBtnFetchshipdetEXPCOLSETTclay().setEnabled(true);
                        }

                        List<ExtEventInvoiceDetails> invodetails = (List<ExtEventInvoiceDetails>) getWrapper()
                                    .getExtEventInvoiceDetails();
                        if (invodetails.size() == 0) {
                              // //Loggers.general().info(LOG,"invodetails --->" + invodetails);
                              EventPane pane = (EventPane) getPane();
                              pane.getBtnFetchinvdetEXPCOLSETTclay().setEnabled(true);
                        }

                        // hs code population

                        String hscodeval = getWrapper().getHMON().trim();
                        // //Loggers.general().info(LOG,"hscode Value---->" + hscodeval);
                        if ((!hscodeval.equalsIgnoreCase("")) && hscodeval != null) {
                              try {
                                    // //Loggers.general().info(LOG,"hscode Value in try---->" +
                                    // hscodeval);
                                    String hyperValue = "SELECT trim(HSEXPPOY) FROM EXTHMCODE WHERE HCODEE='" + hscodeval + "'";
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
                                    // Loggers.general().info(LOG,"exceptio is hscode" +
                                    // e.getMessage());
                              }

                              finally {
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
                              // Loggers.general().info(LOG,"HS code is empty for policy ");
                        }

                        String mas = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                        // String = "0172ELFX0003716";
                        String evt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                        // String str ="";
                        try {
                        Loggers.general().info(LOG,"enter into bill generate no");
                              String str = mas.substring(4, 16);
                              // //Loggers.general().info(LOG,"str---->" + str);
                              String strevt = evt.substring(0, 1);
                              // //Loggers.general().info(LOG,"strevt ---->" + strevt);

                              String str11 = evt.substring(3, 6);
                              // //Loggers.general().info(LOG,"str11 ---->" + str11);
                              String val = str + strevt + str11;
                              // //Loggers.general().info(LOG,"Total ---->" + val);
                              getWrapper().setBLLREFNO(mas);
                              getPane().setBLLREFNO(mas);
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"exception" + e);
                        }

                        // ------------------------

                        String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                        String eventPrefix = getDriverWrapper().getEventFieldAsText("EVCD", "s", "");
                        String csty_id = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");

                        String trns_TYPE = getWrapper().getTRANTYP();

                        try {

                              String shipbillnum = "x";
                              String SHIPBILLDATE = "";
                              String PORTCODE = "";
                              String FORMNO = "";
                              String OUTSTANDINGAMOUNT = "";
                              String outStandingAmount = "";
                              String currency = "USD";
                              String ShipAmount = "";
                              double outAmount = 0.0;
                              String outstr = "";
                              ArrayList<String> shippValues = new ArrayList<String>();
                              List<ExtEventShippingCollections> shipingDetails = (List<ExtEventShippingCollections>) getWrapper()
                                          .getExtEventShippingCollections();

                              for (int i = 0; i < shipingDetails.size(); i++) {
                                    if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                                          ExtEventShippingCollections ship_Obj = shipingDetails.get(i);
                                          SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");
                                          ShipAmount = String.valueOf(ship_Obj.getCSHPAMT()) + " " + ship_Obj.getCSHPAMTCurrency();
                                          OUTSTANDINGAMOUNT = ShipAmount;
                                          shipbillnum = ship_Obj.getCBILLNUM().trim();
                                          shipbillnum = setxxforparameters(shipbillnum.trim());
                                          // //Loggers.general().info(LOG,"Shipping Bill Number " +
                                          // shipbillnum);
                                          shippValues.add(shipbillnum);
                                          SHIPBILLDATE = format.format(ship_Obj.getCBILLDA());
                                          // //Loggers.general().info(LOG,"Shipping Bill Date " +
                                          // SHIPBILLDATE);
                                          SHIPBILLDATE = setxxforparameters(SHIPBILLDATE.trim());
                                          // //Loggers.general().info(LOG,"Shipping Bill Date " +
                                          // SHIPBILLDATE);
                                          shippValues.add(SHIPBILLDATE);
                                          if (shipbillnum.length() > 1) {
                                                // //Loggers.general().info(LOG,"Shipping Bill Number "
                                                // +
                                                // shipbillnum);

                                                String outStanding_Amnt = "SELECT OUTSAMT ,SHIPCCY FROM ETT_EDPMS_SHP_OUTSAMT WHERE SHIPBILLNUM='"
                                                            + shipbillnum + "' and SHIPBILLDATE='" + SHIPBILLDATE + "'";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,
                                                                  "Query is ETT_EDPMS_SHP_OUTSAMT if loop-----> " + outStanding_Amnt);

                                                }
                                                con = ConnectionMaster.getConnection();
                                                ship_prepare = con.prepareStatement(outStanding_Amnt);
                                                ship_result = ship_prepare.executeQuery();
                                                // //Loggers.general().info(LOG,"Query executeQuery");
                                                while (ship_result.next()) {

                                                      outAmount = ship_result.getDouble("OUTSAMT");
                                                      outstr = ship_result.getString("OUTSAMT");
                                                      // Loggers.general().info(LOG,"while for outstanding
                                                      // fin
                                                      // String" + outstr);

                                                      if (outAmount > 0) {

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"Outstanding amount if loop------>" + outstr);

                                                            }
                                                            currency = ship_result.getString(2);
                                                            // //Loggers.general().info(LOG,"currency is " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            // amt=new BigDecimal(outAmount/10);
                                                            OUTSTANDINGAMOUNT = outstr + " " + currency;

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"Final Outstanding amount------>" + OUTSTANDINGAMOUNT);

                                                            }

                                                            ship_Obj.setColumn("COUTSAMT", OUTSTANDINGAMOUNT);

                                                      }

                                                      else {

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"else Outstanding amount------>" + outAmount);

                                                            }

                                                            currency = ship_result.getString(2);
                                                            // //Loggers.general().info(LOG,"currency is " +
                                                            // currency);

                                                            OUTSTANDINGAMOUNT = 0 + " " + currency;
                                                            // Loggers.general().info(LOG,"Out Amount
                                                            // else---->
                                                            // " + OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("COUTSAMT", OUTSTANDINGAMOUNT);

                                                      }

                                                }

                                          } else {

                                                FORMNO = ship_Obj.getCFORMN();
                                                // //Loggers.general().info(LOG,"Form No" + FORMNO);
                                                FORMNO = setxxforparameters(FORMNO.trim());
                                                // //Loggers.general().info(LOG,"Form No" + FORMNO);
                                                shippValues.add(FORMNO);

                                                PORTCODE = ship_Obj.getCPORTCO();
                                                // //Loggers.general().info(LOG,"Port Code " +
                                                // PORTCODE);
                                                PORTCODE = setxxforparameters(PORTCODE.trim());
                                                // //Loggers.general().info(LOG,"Port Code " +
                                                // PORTCODE);
                                                shippValues.add(PORTCODE);
                                                String outStanding_Amnt = "SELECT OUTSAMT ,SHIPCCY FROM ETT_EDPMS_SHP_OUTSAMT WHERE SHIPBILLNUM='"
                                                            + shipbillnum + "' and SHIPBILLDATE='" + SHIPBILLDATE + "' AND PORTCODE='"
                                                            + PORTCODE + "' AND FORMNO='" + FORMNO + "'";
                                                // Loggers.general().info(LOG,"Query is
                                                // ETT_EDPMS_SHP_OUTSAMT else loop-----> " +
                                                // outStanding_Amnt);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,
                                                                  "Query is ETT_EDPMS_SHP_OUTSAMT for form number-----> " + outStanding_Amnt);

                                                }
                                                con = ConnectionMaster.getConnection();
                                                ship_prepare = con.prepareStatement(outStanding_Amnt);
                                                ship_result = ship_prepare.executeQuery();
                                                while (ship_result.next()) {

                                                      // //Loggers.general().info(LOG,"eneterd while for
                                                      // outstanding
                                                      // amount else loop------>");

                                                      outAmount = ship_result.getDouble("OUTSAMT");
                                                      outstr = ship_result.getString("OUTSAMT");
                                                      if (outAmount > 0) {

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"outAmount is form number" + outAmount);

                                                            }
                                                            currency = ship_result.getString(2);
                                                            // //Loggers.general().info(LOG,"currency is " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = outstr + " " + currency;
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"Final outAmount is form number" + OUTSTANDINGAMOUNT);

                                                            }
                                                            ship_Obj.setColumn("COUTSAMT", OUTSTANDINGAMOUNT);
                                                      }

                                                      else {

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"outAmount is else loop" + outAmount);

                                                            }

                                                            currency = ship_result.getString(2);
                                                            // //Loggers.general().info(LOG,"currency is " +
                                                            // currency);

                                                            OUTSTANDINGAMOUNT = 0 + " " + currency;
                                                            // //Loggers.general().info(LOG,"Out Amount
                                                            // else---->
                                                            // " + OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("COUTSAMT", OUTSTANDINGAMOUNT);

                                                      }

                                                }
                                          }

                                    } else {
                                          // Loggers.general().info(LOG,"Event Status is Completed");
                                    }
                              }

                        } catch (Exception e) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Exception is Outstanding amount" + e.getMessage());

                              }
                        }

                        finally {
                              try {

                                    if (ship_result != null)
                                          ship_result.close();
                                    if (ship_prepare != null)
                                          ship_prepare.close();
                                    if (con != null)
                                          con.close();

                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,"Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }

                        // Own lc validation 261116
                        String mast_ref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                        try {
                              String own_Value = "SELECT mas.MASTER_REF, THEME_GENIUS_PKG.CONVAMT( mas.CCY,mas.AMOUNT) master_amount , mas.CCY master_ccy, THEME_GENIUS_PKG.CONVAMT(cpm.pay_ccy,cpm.PAY_AMT) pay_amt, PAY_CCY pay_ccy, round(THEME_GENIUS_PKG.CONVAMT(cpm.pay_ccy,cpm.PAY_AMT)* cer1.EXCH_RATE/cer2.EXCH_RATE,2) AS pay_usd_amt FROM master mas, PRODTYPE prod, CPAYMASTER cpm, PARTYDTLS prt, CCY_EXCH_RATE_VIEW cer1, CCY_EXCH_RATE_VIEW cer2, gfpf WHERE mas.PRODTYPE = prod.KEY97 AND mas.key97 = cpm.KEY97 AND cpm.REMIT_PTY = prt.KEY97 AND prt.CUS_MNM = gfpf.gfcus1 AND cpm.PAY_CCY = cer1.CURR_CODE and cer2.CURR_CODE = 'USD' AND mas.MASTER_REF ='"
                                          + mast_ref + "'";
                              // //Loggers.general().info(LOG,"OWN LC VALUE SHOW USD--->" +
                              // own_Value);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(own_Value);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String USD_amt = rs1.getString(6);
                                    // //Loggers.general().info(LOG,"USD_amt value after getting in
                                    // query---->" + USD_amt);
                                    getPane().setUSDAMT(USD_amt + " " + "USD");
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"OWN LC VALUE SHOW " + e);
                        }

                        finally {
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
                        String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");

                        try {
                              getrbiPurcodeCode();

                        } catch (Exception e) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception in Purpose code---->" + e.getMessage());

                                    }
                              }
                        }

                        try {

                              Connection conne = null;
                              PreparedStatement pres = null;
                              ResultSet resSet = null;
                              String master_refNo = "";
                              master_refNo = getmasRefNo();
                              String EventRefNo = "";
                              EventRefNo = geteventRefNo();
                              // //Loggers.general().info(LOG,"Master is " + master_refNo + "Event
                              // Ref
                              // No is " + EventRefNo);
                              // //Loggers.general().info(LOG,"Resetting the Outstanding Amount
                              // and
                              // Repayment Amount");
                              List<ExtEventShippingCollections> liste = (List<ExtEventShippingCollections>) getWrapper()
                                          .getExtEventShippingCollections();
                              // //Loggers.general().info(LOG,"Shipping Table Size " +
                              // liste.size());
                              for (int i = 0; i < liste.size(); i++) {
                                    if (!eventStatus.equalsIgnoreCase("Completed")) {
                                          conne = ConnectionMaster.getConnection();
                                          double outStandingAmt = 0;
                                          double repAmt = 0;
                                          String outStandingAmtCurr = "";
                                          ExtEventShippingCollections ship = liste.get(i);
                                          String ShipBillNo = "";
                                          ShipBillNo = ship.getCBILLNUM();
                                          outStandingAmt = ship.getCOUTSAMT().doubleValue();
                                          repAmt = ship.getCREPAY().doubleValue();
                                          String outStanding_str = String.valueOf(outStandingAmt);
                                          // Loggers.general().info(LOG,"Outstanding Amount " +
                                          // outStandingAmt);
                                          String outStanding = ship.getCOUTSAMT().toString();
                                          outStandingAmtCurr = ship.getCOUTSAMTCurrency();
                                          // //Loggers.general().info(LOG,"Shipping Bill Number is " +
                                          // ShipBillNo);
                                          Date ShipDate = ship.getCBILLDA();
                                          String billDate = "";
                                          SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy");
                                          if (ShipDate != null) {
                                                billDate = date.format(ShipDate);
                                          }
                                          String formNo = "";
                                          formNo = ship.getCFORMN();
                                          // //Loggers.general().info(LOG,"Form nO is " + formNo);
                                          String portCode = "";
                                          portCode = ship.getCPORTCO();
                                          // //Loggers.general().info(LOG,"Port Code is " + portCode);
                                          ShipBillNo = setxxforparameters(ShipBillNo);
                                          billDate = setxxforparameters(billDate);
                                          formNo = setxxforparameters(formNo);
                                          portCode = setxxforparameters(portCode);
                                          // //Loggers.general().info(LOG,"Ship Bill Amount " +
                                          // ShipBillNo
                                          // +
                                          // "Bill Date " + billDate + "Form No " + formNo +
                                          // "portCode
                                          // " + portCode);
                                          String query = "select billamt AS BILL,billequcurr AS BCURR,repayamt AS REP,repaycurr AS RCURR,REPAYTYPE as TYPE,SHORTAMT as SHORT,SHORTCURR as SHORTCURR,REASON as REASON,NOTIONAL AS NOTIONAL from ETT_EDPMS_ALLSHP_REPAYAMT where master_ref='"
                                                      + master_refNo + "' and eventrefno='" + EventRefNo + "' and shipbillnum='"
                                                      + ShipBillNo + "' AND shipbilldate='" + billDate + "' and portcode='" + portCode
                                                      + "' and formno='" + formNo + "'";
                                          System.out.println("Shipping Bill Query: "+query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,"Resetting amount Query is=====>" + query);

                                          }
                                          pres = conne.prepareStatement(query);
                                          resSet = pres.executeQuery();
                                          if (resSet.next()) {
                                                System.out.println("Query has data");
                                                // //Loggers.general().info(LOG,"Resetting amount value
                                                // in
                                                // while loop");
                                                // resetting------------------->>>>");
                                                ship.setCEQUBILL(new BigDecimal(resSet.getString("BILL")));
                                                ship.setCEQUBILLCurrency(resSet.getString("BCURR"));

                                                if (resSet.getDouble("REP") > 0) {
                                                      // Loggers.general().info(LOG,"Repayment value in
                                                      // resetting if loop====>" +
                                                      // resSet.getString("REP"));

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,
                                                                        "Repayment value in resetting if loop====>" + resSet.getString("REP"));

                                                      }

                                    //                ship.setCREPAY(new BigDecimal(resSet.getString("REP")));
                                    //                ship.setCREPAYCurrency(resSet.getString("RCURR"));

                                                } else if (repAmt < 1) {
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"Repayment value in resetting else if loop====>" + repAmt);

                                                      }
                                                      BigDecimal repayAmt = ship.getCREPAY();
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"Repayment value in resetting from grid ====>" + repayAmt);

                                                      }
                                                      String resSetCCY = ship.getCREPAYCurrency().toString();
                                                      // Loggers.general().info(LOG,"Repayment double
                                                      // value in
                                                      // resetting else loop====>" + repAmt + "" +
                                                      // resSetCCY);
                                    //                ship.setCREPAY(repayAmt);
                                    //                ship.setCREPAYCurrency(resSetCCY);
                                                }

                                                // ship.setREPTYP(resSet.getString("TYPE"));
                                                ship.setCRSNASHF(resSet.getString("REASON"));
                                                // ship.setCSHCOLAM(new
                                                // BigDecimal(resSet.getString("SHORT")));
                                                // ship.setCSHCOLAMCurrency(resSet.getString("SHORTCURR"));

                                          } else {

                                                // Loggers.general().info(LOG,"Resetting amount value
                                                // while
                                                // in the else and if loop===>");
                                                System.out.println("Query has no data");
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Repayment value in resetting in  else loop====>" + repAmt);
                                                      Loggers.general().info(LOG,"Repayment value in resetting in  outStanding====>" + outStanding);

                                                }
                                    //          ship.setCREPAY(new BigDecimal(outStanding));
                                    //          ship.setCREPAYCurrency(outStandingAmtCurr);

                                          }

                                          if (outStandingAmt == 0 || outStandingAmt < 1) {
                                                System.out.println("Step001");
                                                String reystr = "0";
                                    //          ship.setCREPAY(new BigDecimal(Double.valueOf(reystr)));
                                    //          ship.setCREPAYCurrency(outStandingAmtCurr);
                                          }

                                    } else {
                                          // Loggers.general().info(LOG,"Event is already Completed");
                                    }
                              }

                        } catch (Exception e) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Exception for Resetting=====>" + e.getMessage());

                              }
                        }

                        if ((step_Id.equalsIgnoreCase("CSM") || step_Id.equalsIgnoreCase("CBS Maker"))
                                    && getMinorCode().equalsIgnoreCase("CLP")) {
                              try {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Custom fields are clear method");
                                    }
                        //          getcustomValueDelete();
                                    getPane().onDELETEEXPCOLSETTclayButton();
                              }

                              catch (Exception e) {
                                    e.getStackTrace();
                              }
                        }

                        if (getMinorCode().equalsIgnoreCase("CLP") && step_Id.equalsIgnoreCase("CSM")) {

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
                                          // Loggers.general().info(LOG,"shipping table repayment in
                                          // CSM
                                          // step if====>" + outStandAmt);

                              //          ship.setCREPAY(outStandAmt);
                              //          ship.setCREPAYCurrency(outStandccy);
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
                                    // Loggers.general().info(LOG,"Exception e " + e.getMessage());
                              }
                        } else {
                              // Loggers.general().info(LOG,"shipping table repayment in CSM step
                              // else====>");
                        }

                        // Update value
                        String rtgspart = getWrapper().getRTGSPART();

                        String portdes = getWrapper().getPORTDES().trim();
                        // //Loggers.general().info(LOG,"Port of destination Value---->" +
                        // portdes);
                        if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
                              try {
                                    String portvalqury = "select trim(PODESPN),trim(COUNTRY) from EXTPORTDESTINATION WHERE PODEST='"
                                                + portdes + "'";
                                    // //Loggers.general().info(LOG,"port code destination query
                                    // Value---->" + portvalqury);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(portvalqury);
                                    rs = ps1.executeQuery();
                                    while (rs.next()) {
                                          String hsploy = rs.getString(1);
                                          String COUNTRY = rs.getString(2);
                                          // //Loggers.general().info(LOG,"port code
                                          // description---->"
                                          // +
                                          // hsploy);
                                          getPane().setPODESCP(hsploy);
                                          getPane().setPODESCON(COUNTRY);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exception is " +
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
                                    rs = ps1.executeQuery();
                                    while (rs.next()) {
                                          String hsploy = rs.getString(1);
                                          String country = rs.getString(2);
                                          // //Loggers.general().info(LOG,"port code
                                          // description---->"
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
if (getMajorCode().equalsIgnoreCase("ODC")) {
                              
                              
                              getPane().getExtEventLoanDetailsNew().setEnabled(false);
                              getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                              getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                              getPane().getExtEventLoanDetailsUp().setEnabled(false);
                              getPane().getExtEventLoanDetailsDown().setEnabled(false);
                        }
                        
                        /*if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP"))
                        {
                              String outstanding=getDriverWrapper().getEventFieldAsText("FCO:sON","v", "m");
                              String outccy=getDriverWrapper().getEventFieldAsText("FCO:sON","v", "c");
                              Loggers.general().info(LOG,"outstanding=>" + outstanding);
                              Loggers.general().info(LOG,"outccy=>"+outccy);
                        
                              try{
                                    List<ExtEventShippingCollections> shipingDetails = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();
                                    
                                    for (int i = 0; i < shipingDetails.size(); i++) {
                                          ExtEventShippingCollections ship_Obj = shipingDetails.get(i);
                                          String shipbillnum = outstanding+" "+outccy;
                                          ship_Obj.setColumn("COUTSAMT", shipbillnum);
                                          Loggers.general().info(LOG,"coutsamt=>"+ship_Obj.getCOUTSAMT());
                                    }
                                    
                              }
                              catch(Exception e)
                              {
                                    Loggers.general().info(LOG,"Exception in ilcpoc =<"+e.getMessage());
                              }
                        }
                  */

if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")) {
      try {
            System.out.println("Inside Try block of MTT freezeMttNumber");
            freezeMttNumber();
            
      } catch (Exception e) {
            e.printStackTrace();
      }
      }
                        try {

                              getcountryCode();

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" +
                              // e.getMessage());
                        }

                        
                        try {
                              int cnt = 0;
                              cnt = getRepayProb();
                              Loggers.general().info(LOG,"Repayment in subsidery" +cnt );
                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"Repayment in subsidery" + ee.getMessage());

                        }
                        //INC4431622 starts
                        try {
                               Loggers.general () .info(LOG,"Entered method");
                              removeSpace();

                        } catch (Exception e) {
                               Loggers.general () .info(LOG,"Exception update" +e.getMessage());
                        }
                        //INC4431622 ends
                        // if (rtgspart.equalsIgnoreCase("FULL")) {
                        // try {
                        // con = getConnection();
                        // String sql = "SELECT * from KMB_RTGS_NEFT_VIEW where
                        // MASTER_REF = '" + MasterReference
                        // + "' AND EVENT_REF = '" + eventCode + "'";
                        // // Loggers.general().info(LOG,"Query value for RTGS amount--->"
                        // // +
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
                        // // Loggers.general().info(LOG,"Exception update" +
                        // // e.getMessage());
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
                        // // Loggers.general().info(LOG,"Connection Failed! Check
                        // // output
                        // // console");
                        // e.printStackTrace();
                        // }
                        // }
                        // } else {
                        // Loggers.general().info(LOG,"RTGS/NEFT type is part");
                        //
                        // }

                  }
            }
            return false;
      }

      @SuppressWarnings("deprecation")
      @Override
      public void onValidate(ValidationDetails validationDetails) {

            // WORKFLOW CHECKLIST
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
                  // Loggers.general().info(LOG,"On validation calling with no");
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // //Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);
                  if (getMinorCode().equalsIgnoreCase("CLP")
                              && (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {
                        getPane().onNOSTROEXPCOLSETTclayButton();
            //          getPane().onRTGSEXPCOLSETTclayButton();
                  }
                  if (step_csm.equalsIgnoreCase("CSM") && getMinorCode().equalsIgnoreCase("CLP")) {

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
      //                getPane().setRTGSNEFT("");
                        getPane().setUTRNO("");
                        getPane().setNARRTVE("");
                        getPane().setBADDRE_Name("");
                        getPane().setRTGSPART("");

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
                        getPane().setREPAYAMT("");
                        getPane().setTOTUSD(0 + " USD");
                        getPane().setTOTINR(0 + " INR");
                        getPane().setTOTEUR(0 + " EUR");
                        getPane().setTOTJPY(0 + " JPY");
                        getPane().setTOTGBP(0 + " GBP");

                  } else {
                        // Loggers.general().info(LOG,"Step id for else step---->" +
                        // step_log);
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
            //          getPane().setRTGSNEFT("");
                        getPane().setNARRTVE("");

                  }

                  String gateway = getDriverWrapper().getEventFieldAsText("FRGI", "l", "");
                  if (step_csm.equalsIgnoreCase("CSM") && getMinorCode().equalsIgnoreCase("CLP")
                              && !gateway.equalsIgnoreCase("Y")) {
                        Loggers.general().info(LOG,"todo on====>");
                        /*getPane().setNOSAMT("");
                        getPane().setNOSTOUT("");*/
                        getPane().setEBRCFLAG("");

                        /*getPane().setNOSTRM("");
                        getPane().setNOSTMT("");
                        getPane().setNOSTAMT("");
                        getPane().setNOSTDAT("");

                        getPane().setPOOLAMT("");
                        getPane().setNOSTACC("");
                        getPane().setINWMSG("");
                        getPane().setMTMESG("");*/
                  }

                  // FCY Tax calculation
                  try {

                        getFCCTCALCULATION();
                        getFcctDetails(validationDetails);
                        System.out.println("fcct calculation Payment !!!");

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }

                  if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")) {
                        EventPane pane = (EventPane) getPane();

                        pane.getBtnDELETEEXPCOLSETTclay().setEnabled(false);
                        pane.getBtnFetchshipdetEXPCOLSETTclay().setEnabled(false);
                        pane.getBtnFetchinvdetEXPCOLSETTclay().setEnabled(false);
                        pane.getExtEventShippingCollectionsNew().setEnabled(false);
                        pane.getExtEventShippingCollectionsDelete().setEnabled(false);
                        pane.getExtEventShippingdetailsNew().setEnabled(false);
                        pane.getExtEventShippingdetailsDelete().setEnabled(false);
                        pane.getExtEventShippingdetailsUpdate().setEnabled(false);
                        pane.getExtEventInvoiceDetailsNew().setEnabled(false);
                        pane.getExtEventInvoiceDetailsUpdate().setEnabled(false);
                        pane.getExtEventInvoiceDetailsDelete().setEnabled(false);
                  }

                  if (getMinorCode().equalsIgnoreCase("CLP")
                              && (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {
            //          getPane().onRTGSEXPCOLSETTclayButton();
                  }

                  if (getMinorCode().equalsIgnoreCase("CLP") && step_csm.equalsIgnoreCase("CSM")
                              && (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {

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
                                    // Loggers.general().info(LOG,"shipping table repayment in CSM
                                    // step if====>" + outStandAmt);

                        //          ship.setCREPAY(outStandAmt);
                        //          ship.setCREPAYCurrency(outStandccy);
                                    String shipcoll = "0";
                                    ship.setCSHCOLAM(new BigDecimal(Double.valueOf(shipcoll)));
                                    ship.setCSHCOLAMCurrency(outStandccy);

                                    ship.setCNOTIONL(new BigDecimal(Double.valueOf(notionalrate)));
                                    // Loggers.general().info(LOG,"Notional rate in CSM step
                                    // if====>" + ship.getCNOTIONL());
                                    if (repval_new.equalsIgnoreCase("2") || repval_new.equalsIgnoreCase("")
                                                && (!repval_new.equalsIgnoreCase("1") || !repval_new.equalsIgnoreCase("Part"))) {
                                          // Loggers.general().info(LOG,"Repayment type in CSM step
                                          // if====>" + repval_new);
                                          ship.setREPTYP(repval_new);
                                    } else {
                                          // Loggers.general().info(LOG,"Repayment type in CSM step
                                          // else loop====>" + repval_new);
                                    }
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception e " + e.getMessage());
                        }
                  } else {
                        // Loggers.general().info(LOG,"shipping table repayment in CSM step
                        // else====>");
                  }
                  // Standing instruction link

                  // try {
                  // String Standing = getStandinglink().trim();
                  // ExtendedHyperlinkControlWrapper Standing1 =
                  // getPane().getCtlSTANDINGEXPCOLSETTclayHyperlink();
                  // Standing1.setUrl(Standing);
                  // String nostroAccountNum = getPane().getNOSTACC();
                  // String nostroCreditCCY = getWrapper().getPOOLAMTCurrency();
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"Account number for SI Availability check - "
                  // + nostroAccountNum
                  // + " And Credit CCY - " + nostroCreditCCY);
                  // }
                  // Standing1.setVisible(isSIAvailable(nostroAccountNum,
                  // nostroCreditCCY));
                  // } catch (Exception ees) {
                  // // Loggers.general().info(LOG,"Standing in ELC--------->" +
                  // // ees.getMessage());
                  // }

                  // InwardLink
                  try {
                        String HyperInward = getINWARDREM();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlINWARDEXPCOLSETTclayHyperlink();
                        InwardLink.setUrl(HyperInward);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"InwardLink exception----->" +//
                        // ees.getMessage());
                  }

                  String mas = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  // String = "0172ELFX0003716";
                  String evt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                  // String str ="";
                  try {
                        Loggers.general().info(LOG,"enter into bill generate no");
                        String str = mas.substring(4, 16);
                        // //Loggers.general().info(LOG,"str---->" + str);
                        String strevt = evt.substring(0, 1);
                        // //Loggers.general().info(LOG,"strevt ---->" + strevt);

                        String str11 = evt.substring(3, 6);
                        // //Loggers.general().info(LOG,"str11 ---->" + str11);
                        String val = str + strevt + str11;
                        // //Loggers.general().info(LOG,"Total ---->" + val);
                        getWrapper().setBLLREFNO(mas);
                        getPane().setBLLREFNO(mas);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exception" + e);
                  }

                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSEXPCOLSETTclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane()
                                    .getCtlCSMCHECKLISTIMPSTALCOUTCLAclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMCHECKLISTEXPCOLSETTclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALIMPSTALCOUTCLAclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);
//                      ExtendedHyperlinkControlWrapper csmreftrack4 = getPane().getCtlCSMREFRALEXPCOLSETTclayHyperlink();
//                      csmreftrack4.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKIMPSTALCOUTCLAclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCPCCHECKEXPCOLSETTclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELIMPSTALCOUTCLAclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCPCREFRALEXPCOLSETTclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

                  // Setting the PreShipment Link
                  try {
                        // //Loggers.general().info(LOG,"Setting the PreShipment link in
                        // validate");
                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlPreshipmentEXPCOLSETTclayHyperlink();
                        dmsh1.setUrl(Preshipment);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                  }

                  String cur = getDriverWrapper().getEventFieldAsText("AMTP", "v", "c").trim();
                  List<ExtEventShippingCollections> shipcol = (List<ExtEventShippingCollections>) getWrapper()
                              .getExtEventShippingCollections();
                  if (shipcol.size() > 0) {
                        // //Loggers.general().info(LOG,"shipdetails --->" + shipcol);
                        EventPane pane = (EventPane) getPane();
                        pane.getExtEventShippingCollectionsDelete().setEnabled(false);
                  }
                  String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                  // USD EQUIVALENT
                  

                  if (getMajorCode().equalsIgnoreCase("ODC") && (prd_typ.equalsIgnoreCase("OCI"))) {
                        // Loggers.general().info(LOG,"Else systemOutput");
                        getPane().setUSDAMT("");
                        getPane().setCRAMT_Name("");
                        getPane().setINRAMT("");
                  }

                  String tranType = getWrapper().getTRANTYP();
                   Loggers.general().info(LOG,"Write of Type---->" + tranType +"tranType---->" + tranType.length());
                  if (tranType.equalsIgnoreCase("WRITEOFF") && (prd_typ.equalsIgnoreCase("OCF"))
                              && !step_csm.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("N");
                  } else if ((tranType.equalsIgnoreCase("") || !tranType.equalsIgnoreCase("WRITEOFF"))
                              && (prd_typ.equalsIgnoreCase("OCF")) && !step_csm.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("Y");
                  } else if (step_csm.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("");
                  }

                  if ((prd_typ.equalsIgnoreCase("OCI")) && getMinorCode().equalsIgnoreCase("CLP")) {
                        EventPane pane = (EventPane) getPane();

                        pane.getExtEventAdvanceTableNew().setEnabled(false);
                        pane.getExtEventAdvanceTableDelete().setEnabled(false);
                        pane.getExtEventAdvanceTableUpdate().setEnabled(false);
                        pane.getBtnonfetchEXPBILLclay().setEnabled(false);
                        pane.getBtnonfetchEXPCOLSETTclay().setEnabled(false);

                  }

                  else {
                        // //Loggers.general().info(LOG,"Product type is forign");
                        EventPane pane = (EventPane) getPane();
                        pane.getExtEventAdvanceTableNew().setEnabled(true);
                        pane.getExtEventAdvanceTableDelete().setEnabled(true);
                        pane.getExtEventAdvanceTableUpdate().setEnabled(true);
                        pane.getBtnonfetchEXPBILLclay().setEnabled(true);
                        pane.getBtnonfetchEXPCOLSETTclay().setEnabled(true);
                  }

                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001

                  // Shipping bill date validation
                  double am_rey = 0;
                  double repay_total = 0;
                  double short_totvalue = 0;
                  double shortValue = 0;
                  
                  

                  // Advance Table Validations
                  String customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no").trim();
                  
                  
                  String formtyp = getWrapper().getFORTYP();
                  // Shipping bill date checking
                  double datedouble = 0;
                  String paymentExt = "PAYEXT";
                  String paymentVal = "";
                  double paymentValue = 0;
                  AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryValue = getDriverWrapper()
                              .createQuery("EXTGENCUSTPROP", "PROPNAME='" + paymentExt + "'");
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                  EXTGENCUSTPROP val = queryValue.getUnique();
                  if (val != null) {

                        paymentVal = val.getPropval();

                        paymentValue = Double.valueOf(paymentVal);
                        // Loggers.general().info(LOG,"payment Value date ===>" + paymentValue);
                  }
                  String tenorExt = getWrapper().getTENEXT();
                  

                  // Advance table date checking
                  

                  // Notes Populated in Summary

                  
                  // commented by MISYS - 5351
                  // try {
                  // // //Loggers.general().info(LOG,"get value for LOB");
                  // getLob();
                  // } catch (Exception ee) {
                  // // Loggers.general().info(LOG,ee.getMessage());
                  // // //Loggers.general().info(LOG,"LOB Catch");
                  // } finally {
                  // // //Loggers.general().info(LOG,"finally LOB ");
                  // }

                  // try {
                  // getrealizationamount(validationDetails);
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"error in realization value" +
                  // // e.getMessage());
                  // }

                  // USD EQUIVALENT
                  if (currencyCalc()) {
                        // //Loggers.general().info(LOG," Currency systemOutput");
                  } else {
                        // //Loggers.general().info(LOG," Else systemOutput");
                  }

                  String Ifsc = getWrapper().getIFSCCO_Name().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);
                  
                  String portdes = getWrapper().getPORTDES().trim();
                  // //Loggers.general().info(LOG,"Port of destination Value---->" +
                  // portdes);
                  

                  // port of loading description

                  String portload = getWrapper().getPORLOD().trim();
                  // //Loggers.general().info(LOG,"Port of destination Value---->" +
                  // portdes);
                  
                  try {

                        getcountryCode();

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }

                  // ------------*********--------------
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                        con = ConnectionMaster.getConnection();
                        if (!(BranchCode.length() == 0)) {
                              String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
                              // //Loggers.general().info(LOG,"BranchCode Query - " + sql6);
                              PreparedStatement ps1 = con.prepareStatement(sql6);
                              ResultSet rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String inmt = rs.getString(1);
                                    // //Loggers.general().info(LOG,"category code - " + inmt);
                                    getWrapper().setIMBRCODE(inmt);
                                    getPane().setIMBRCODE(inmt);
                              }

                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception caught on branch code
                        // validation......" + e.getMessage());
                  }

                  finally {
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

                  
                  
                  

                  // Charge Account Validation
                  String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                                                                                                                                    // CODE
                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); // ILC
                  String cust = "";

                  if (prodtype.trim().equalsIgnoreCase("EGT")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("BEN", "p", "no").trim();
                        ;// party
                              // id
                  } else if (prodtype.trim().equalsIgnoreCase("ISB")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("APP", "p", "no").trim();
                        ;// party
                              // id
                  } else if (prodtype.trim().equalsIgnoreCase("ODC")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("DRW", "p", "no").trim();
                        ;// party
                              // id
                  }

                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // //Loggers.general().info(LOG,"charge account collected " + chargecol);
                  String custval = "";
                  

                  // NPA customer

                  
                  // LIEN VALIDATION
                  String productp = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();

                  // Own lc validation 261116
                  String mast_ref = getDriverWrapper().getEventFieldAsText("MST", "r", "");


                  String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  // //Loggers.general().info(LOG,"event code --->" + getMinorCode());
                  //
                  // if (getMinorCode().equalsIgnoreCase("CLP") &&
                  // (!prd_typ.equalsIgnoreCase("OCI"))) {
                  //
                  // String query_fin = "SELECT count(*) FROM master mas, FNCEMASTER
                  // fin, BASEEVENT bev, BASEEVENT bev1, master mas1 WHERE mas.KEY97 =
                  // fin.KEY97 AND mas.KEY97 = bev.MASTER_KEY AND bev.ATTACHD_EV =
                  // bev1.key97 AND bev1.MASTER_KEY = mas1.KEY97 and mas1.MASTER_REF =
                  // '"
                  // + masRefNo + "'";
                  // // //Loggers.general().info(LOG,"RBI purpose code query_fin --->" +
                  // // query_fin);
                  // int count = 0;
                  // try {
                  // con = ConnectionMaster.getConnection();
                  // ps1 = con.prepareStatement(query_fin);
                  // rs = ps1.executeQuery();
                  // while (rs.next()) {
                  // count = rs.getInt(1);
                  // // //Loggers.general().info(LOG,"RBI purpose if resss --->" +
                  // // resss);
                  //
                  // }
                  //
                  // if (count > 0) {
                  // // //Loggers.general().info(LOG,"RBI purpose if count greater
                  // // resss
                  // // --->" + resss);
                  // getPane().setOPURPOS_Name("P0103");
                  // } else {
                  // // //Loggers.general().info(LOG,"RBI purpose else resss --->" +
                  // // resss);
                  // getPane().setOPURPOS_Name("P0102");
                  // }
                  // } catch (SQLException e) {
                  // e.printStackTrace();
                  // }
                  //
                  // finally {
                  // try {
                  // if (con != null) {
                  // con.close();
                  // if (ps1 != null)
                  // ps1.close();
                  // if (rs != null)
                  // rs.close();
                  // }
                  // } catch (SQLException e) {
                  // // Loggers.general().info(LOG,"Connection Failed! Check output
                  // // console");
                  // e.printStackTrace();
                  // }
                  // }
                  //
                  // }

                  // else {
                  // // Loggers.general().info(LOG,"RBI purpose code in else");
                  // getPane().setOPURPOS_Name("");
                  // }

                  
                  try {
                        List<ExtEventLoanDetails> preship = (List<ExtEventLoanDetails>) getWrapper().getExtEventLoanDetails();
                        String currency = "";
                        String amt = "";
                        boolean usdCheck = false;
                        boolean inrCheck = false;
                        boolean eurCheck = false;
                        boolean gbpcheck = false;
                        boolean jpyCheck = false;
                        // //Loggers.general().info(LOG,"Preshipment value checking====> 1" +
                        // inrCheck);

                        for (int l = 0; l < preship.size(); l++) {
                              ExtEventLoanDetails preshipment = preship.get(l);
                              if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("INR")) {
                                    inrCheck = true;
                              }
                              if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("USD")) {
                                    usdCheck = true;
                              }
                              if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("EUR")) {
                                    eurCheck = true;
                              }
                              if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("GBP")) {
                                    gbpcheck = true;
                              }
                              if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("JPY")) {
                                    jpyCheck = true;
                              }

                        }
                        // //Loggers.general().info(LOG,"Preshipment value checking====> 2" +
                        // inrCheck);
                        if (!inrCheck) {
                              getPane().setTOTINR("0.0 INR");
                        }
                        // //Loggers.general().info(LOG,"Preshipment value checking====> 3" +
                        // inrCheck);
                        if (!usdCheck) {
                              getPane().setTOTUSD("0.0 USD");
                        }
                        if (!eurCheck) {
                              getPane().setTOTEUR("0.0 EUR");
                        }
                        if (!gbpcheck) {
                              getPane().setTOTGBP("0.0 GBP");
                        }
                        if (!jpyCheck) {
                              getPane().setTOTJPY("0.0 JPY");
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception in Prsehipment Code" +
                        // e.getMessage());
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
                  // }
                  //
                  // finally {
                  // try {
                  // if (con != null) {
                  // con.close();
                  // if (ps1 != null)
                  // ps1.close();
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
                  // // Loggers.general().info(LOG,"Receiver IFSC Code" + recifsc);
                  //
                  // }

                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                  String bulkUpload = getDriverWrapper().getEventFieldAsText("cAOM", "l", "");
                  // Loggers.general().info(LOG,"Event originated from Gateway message" +
                  // gateway + "bulkUpload===>" + bulkUpload);

                  if (gateway.equalsIgnoreCase("Y")) {
                        getPane().setPERADV("");
                        getPane().setADVREC("");
                        getPane().setNETRECIV("");
                        getPane().setBILLPAY("");
                  }

                  // Loggers.general().info(LOG,"Event originated subproductCode-----" +
                  // subproductCode);
                  // Loggers.general().info(LOG,"Event originated productcode-----" +
                  // productcode);
                  // Loggers.general().info(LOG,"Event originated eventCode-----" +
                  // eventCode);

                  // workflow error configuration
                  
                  if (step_Input.equalsIgnoreCase("i")) {
                        try {
                              List<ExtEventShippingCollections> shipTable = (List<ExtEventShippingCollections>) getWrapper()
                                          .getExtEventShippingCollections();
                              for (int i = 0; i < shipTable.size(); i++) {

                                    ExtEventShippingCollections ship = shipTable.get(i);

                                    String not_str = ship.getCNOTIONL().toString();
                                    double notional = 1;
                                    // //Loggers.general().info(LOG,"shipping table for notional
                                    // length====>" + not_str.length());
                                    BigDecimal rAmt = ship.getCREPAY();
                                    String shiamtcuy = ship.getCSHPAMTCurrency();
                                    String reyamtcuy = ship.getCREPAYCurrency();

                                    double repay_Amount = 0.0;

                                    double notionalDouble = ship.getCNOTIONL().doubleValue();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"Notional rate value---->" + notionalDouble);
                                    }
                                    if ((notionalDouble == 0.0 || notionalDouble == 0 || notionalDouble < 1)
                                                && getMinorCode().equalsIgnoreCase("CLP") && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Notional rate should not be '0' in shipping details grid [CM]");
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,"Notional rate value in else---->" + notionalDouble);
                                          }
                                    }
                                    try {
                                          con = getConnection();
                                          String query = "SELECT ETT_SPOTRATE_CAL('" + shiamtcuy + "','" + reyamtcuy + "') FROM DUAL";
                                          // Loggers.general().info(LOG,"Notional rate function " +
                                          // query);

                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          if (rs1.next()) {
                                                notional = rs1.getDouble(1);
                                                // ship.setCNOTIONL(new BigDecimal(notional));
                                          }

                                          String temp_notRate = String.valueOf(notional);

                                          if (null != not_str && not_str.equalsIgnoreCase("1")) {
                                                ship.setCNOTIONL(new BigDecimal(temp_notRate));
                                                //// Loggers.general().info(LOG,"shipping table notional
                                                //// length if loop====>" + ship.getCNOTIONL());

                                                BigDecimal notional_big = new BigDecimal(notional);
                                                BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                //// Loggers.general().info(LOG,"Notional rate +
                                                //// Repament
                                                //// amount in big decimal POD---->" +
                                                //// equi_bill);
                                                ship.setCEQUBILL(equi_bill);
                                                ship.setCEQUBILLCurrency(ship.getCSHPAMTCurrency());
                                          } else if (null != not_str && !temp_notRate.equalsIgnoreCase(not_str)) {
                                                ship.setCNOTIONL(new BigDecimal(not_str));
                                                //// Loggers.general().info(LOG,"shipping table notional
                                                //// length else if loop====>" +
                                                //// ship.getCNOTIONL());
                                                BigDecimal notional_big = new BigDecimal(not_str);
                                                BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                // Loggers.general().info(LOG,"Notional rate + Repament
                                                // amount in big decimal POD---->" + equi_bill);
                                                ship.setCEQUBILL(equi_bill);
                                                ship.setCEQUBILLCurrency(ship.getCSHPAMTCurrency());

                                          } else {
                                                // Loggers.general().info(LOG,"Notional rate is
                                                // blank---->");
                                          }

                                    } catch (Exception e1) {
                                          // Loggers.general().info(LOG,"Exception" +
                                          // e1.getMessage());
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

                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in validating for notional
                              // rate--->" + e.getMessage());
                        }
                  }

                  // ---------------------------------------------------------------------------

                  String rtgs = getWrapper().getPROREMT();
                  String rtgspart = getWrapper().getRTGSPART();

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

                  

            

                  

                  // try {
                  // int evtno = Integer.parseInt(evvcount);
                  // List<ExtEventSERVICETAXLC> servicetax =
                  // (List<ExtEventSERVICETAXLC>) getWrapper()
                  // .getExtEventSERVICETAXLC();
                  // if (servicetax.size() > 0 && (step_Input.equalsIgnoreCase("i"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker")) && evtno != 1) {
                  // if ((getMajorCode().equalsIgnoreCase("ODC") &&
                  // getMinorCode().equalsIgnoreCase("CLP"))) {
                  // validationDetails.addWarning(WarningType.Other,
                  // "If Service tax values are change in this transaction Kindly
                  // delete the service tax record in CBS Maker step and the changed
                  // lastest value will populate in Authorized step [CM]");
                  // }
                  // } else {
                  // // Loggers.general().info(LOG,"Service tax lc value" +
                  // // servicetax.size());
                  // }
                  // } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception Service tax lc value");
                  // }

                  
                  
                  // Update value

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
                  //
                  // } else {
                  // Loggers.general().info(LOG,"RTGS/NEFT type is part");

                  // }

                  

                  
                  // rejection validation
                  

      
                  

Loggers.general().info(LOG,"Todo on ODC payment");
                        if (getMajorCode().equalsIgnoreCase("ODC")) {
                              try {
                                    getnetAmountOdcPay();
                                    Loggers.general().info(LOG,"After calling getnetamountodcpay===>");
                              } catch (Exception e) {
                        
                              }
                        }
                        
                        
                  
                        //-------------------------Start of Preshipment--------------
                        
                        ///error configured for duplicate events
                        
                        
                        //CR 37 starts
                        
                        //CR 37 ends
                        
                        
                        /*if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP"))
                        {
            try{
                  List<ExtEventLoanDetails> loanDetails = (List<ExtEventLoanDetails>) getWrapper().getExtEventLoanDetails();
                  int loanCount=loanDetails.size();
                  String subProd=getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  String preSub=getPane().getPRESHIP();
                  Loggers.general().info(LOG,"Current subproduct----------> " + subProd);
                  Loggers.general().info(LOG,"Preshipment subproduct----------> " + preSub);
                  
                  if(preSub!=null&&loanCount>0)
                  {
                  if (!subProd.equalsIgnoreCase(preSub))
                  {
                        validationDetails.addError(ErrorType.Other, "You have changed the subproduct type so kindly reverse back and delete the preshipment in both grid and link [CM]");
                  }
                  }
                  
            }
            catch(Exception e)
            {
                  e.printStackTrace();
                  Loggers.general().info(LOG,"Exception in preshipment subproduct===>" + e.getMessage());

            }
                        }*/
                        /*if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP"))
                        {
                              String outstanding=getDriverWrapper().getEventFieldAsText("FCO:sON","v", "m");
                              String outccy=getDriverWrapper().getEventFieldAsText("FCO:sON","v", "c");
                              Loggers.general().info(LOG,"outstanding=>" + outstanding);
                              Loggers.general().info(LOG,"outccy=>"+outccy);
                        
                              try{
                                    List<ExtEventShippingCollections> shipingDetails = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();
                                    
                                    for (int i = 0; i < shipingDetails.size(); i++) {
                                          ExtEventShippingCollections ship_Obj = shipingDetails.get(i);
                                          String shipbillnum = outstanding+" "+outccy;
                                          ship_Obj.setColumn("COUTSAMT", shipbillnum);
                                          Loggers.general().info(LOG,"coutsamt=>"+ship_Obj.getCOUTSAMT());
                                    }
                                    
                              }
                              catch(Exception e)
                              {
                                    Loggers.general().info(LOG,"Exception in ilcpoc =<"+e.getMessage());
                              }
                        }
                  */
                        // CR220 Changes Startes here
//                      if (getMajorCode().equalsIgnoreCase("ODC")&&(getMinorCode().equalsIgnoreCase("CLP"))) {
//                            System.out.println("CR-220 Validation starts here");
//                            try {
//                                  con = getConnection();
//                                  String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
//                                              + MasterReference
//                                              + "' and BEV.REFNO_PFIX= '"
//                                              + evnt
//                                              + "' and BEV.REFNO_SERL = '"
//                                              + evvcount
//                                              + "'";
//                                  int count = 0;
//                                  ps1 = con.prepareStatement(query);
//                                  rs1 = ps1.executeQuery();
//                                  while (rs1.next()) {
//                                        count = rs1.getInt(1);
//                                  }
//
//                                  if (count > 0
//                                              && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm
//                                                          .equalsIgnoreCase("CBS Maker 1"))) {
//                                        System.out
//                                                    .println("CR-220 Validation setting up Error");
//                                        validationDetails
//                                                    .addError(ErrorType.Other,
//                                                                "FX deal rate is outside specified tolerance [CM]");
//                                  } else {
//                                  }
//
//                            } catch (Exception e) {
//                                  System.out.println("CR-220 Validation Exception occured");
//                                  e.printStackTrace();
//                            }
//
//                            finally {
//                                  try {
//                                        if (rs1 != null)
//                                              rs1.close();
//                                        if (ps1 != null)
//                                              ps1.close();
//                                        if (con != null)
//                                              con.close();
//                                  } catch (SQLException e) {
//                                        e.printStackTrace();
//                                  }
//                            }
//                            System.out.println("CR-220 Validation starts here");
//                      }
                        // CR220 Changes Ends here    
                        //INC4431622 starts
                        try {
                               Loggers.general () .info(LOG,"Entered method");
                              removeSpace();

                        } catch (Exception e) {
                               Loggers.general () .info(LOG,"Exception update" +e.getMessage());
                        }
                        //INC4431622 ends
                        try {
                              getPostingFxrate();
                        }
                        catch (Exception e){
                              e.printStackTrace();
                        }
                        if (getMajorCode().equalsIgnoreCase("ODC")&&(getMinorCode().equalsIgnoreCase("CLP"))) {
                        try {
                              DecimalFormat df = new DecimalFormat("##,##,##0.##");
                              BigDecimal colAmt=new BigDecimal(0);
                              String collAmtStr = getDriverWrapper().getEventFieldAsText("ORA", "v", "m");
                              String collAmtCcy = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
                              String AdvAmtStr = getDriverWrapper().getEventFieldAsText("cARK", "v", "m").trim();
                              String AdvAmtStrCcy = getDriverWrapper().getEventFieldAsText("cARK", "v", "c");
                              String AdvRcv = getDriverWrapper().getEventFieldAsText("cARL", "s", "");
                              if(AdvRcv.equalsIgnoreCase("Part")&&AdvAmtStr != null && !AdvAmtStr.trim().isEmpty()) {
                               colAmt = new BigDecimal(collAmtStr);
                              BigDecimal AdvAmt = new BigDecimal(AdvAmtStr);
                              BigDecimal netAmt = new BigDecimal(0);
                              getPane().setNETRECIV("");
                              if (colAmt.compareTo(AdvAmt) == 1 && collAmtCcy.trim().equals(AdvAmtStrCcy)) {
                                    System.out.println("Amount to be collected");
                              netAmt = colAmt.subtract(AdvAmt);
                              String value1 = df.format(netAmt) + " " + AdvAmtStrCcy;
                              getPane().setNETRECIV(value1);
                              }
                               }
                              else if(AdvRcv.equalsIgnoreCase("Full")){
                                     colAmt = new BigDecimal(collAmtStr);
                                    String value2 = df.format(colAmt) + " " + collAmtCcy;
                                    getPane().setNETRECIV("0 "+collAmtCcy);
                                    getPane().setADVREC(value2);
                              }
                              else {
                                     colAmt = new BigDecimal(collAmtStr);
                                    String value2 = df.format(colAmt) + " " + collAmtCcy;
                                    getPane().setNETRECIV(value2);
                                    getPane().setADVREC("");
                              }
                              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                              Calendar c = Calendar.getInstance();
                              int t = 0;
                              String rvdDate = getDriverWrapper().getEventFieldAsText("RVD","d","");
                              String tenorPeriod = getDriverWrapper().getEventFieldAsText("cAEP","s","");
                              System.out.println("getNotionalDueDate: "+rvdDate);
                              if(!tenorPeriod.equalsIgnoreCase("") && tenorPeriod != null && !tenorPeriod.trim().isEmpty()) {
                                    t = Integer.parseInt(tenorPeriod);
                              }
                              System.out.println("tenorPeriod" +t);
                              if (t>0) {
                                    c.setTime(sdf.parse(rvdDate));
                                    c.add(Calendar.DATE, t);
                                    System.out.println("inside notional IF: " +sdf.format(c.getTime()));
                                    getPane().setSIGVALDT(sdf.format(c.getTime()));
                              }
                              
                        }catch (Exception e){
                              e.printStackTrace();
                        }
                  /*    try {
                              System.out.println("getWarningShipping");
                              con = getConnection();
                               masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                               eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
                        //    String prodType = getDriverWrapper().getEventFieldAsText("SLN", "s", "").trim();
                              System.out.println("Shipping_Warning: "+masterref+", "+eventREF);
                              
                                    
                              String query = "SELECT TRIM(EXTS.CBILLNUM),TRIM(EXTS.CBILLDA),TRIM(EXTS.CFORMN) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTSHC EXTS" +
                                    " WHERE MAS.KEY97 = BEV.MASTER_KEY" +
                                    " AND BEV.KEY97 = EXT.EVENT" +
                                    " AND EXTS.FK_EVENT = EXT.KEY29" +
                                    " AND MAS.MASTER_REF = '"+masterref+"'" +
                                    " AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='"+eventREF+"'";
                              System.out.println(query);
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        if (!rs1.next()) {
                              validationDetails.addWarning(WarningType.Other, "Shipping details should be filled");
                        }
                        else {
                              System.out.println("Shipping grid  has data");
                        }
                        
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                        finally {
                              surrenderDB(con, ps1, rs1);
                        }*/
                        
                        
                        try {
                            String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                            String eventref = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                            String custId = getDriverWrapper().getEventFieldAsText("PRM", "p", "cu").trim();
                            String uniqueId = getDriverWrapper().getEventFieldAsText("cBUB", "s", "").trim();
                            String branch = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
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
                                  + "</UNIQUEID>" + "<INPUTBRANCH>" + branch + "</INPUTBRANCH>" + "<EVENTCODE>" + eventCode1
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
                        //Shipping Grid Amount Calculation - 01-03-2023
                      try {
                              if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")) {
                                    shippingAmountSum();
                                    getCountryRisk();
                              }
                              }catch (Exception e) {
                                    e.printStackTrace();
                              }
                        try {
                              currencyCalc();
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                              }
                        }/*changes on 10-03-2022*/
                        
                         if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")) {
                                    try {
                                          System.out.println("Inside Try block of MTT");
                                          freezeMttNumber();
                                          mttFreeze();
//                                        unsetMTTSeqNo();
                                          setMTTRefNumber();
      //                                  getMttSeqNo();
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
//                                        List<ExtEventAdvanceTable> advanced = getWrapper().getExtEventAdvanceTable().stream()
//                                                    .sorted(Comparator.comparing(ExtEventAdvanceTable::getBALANCE)).collect(Collectors.toList());
                                          
                                          List<ExtEventAdvanceTable> advanced = (List<ExtEventAdvanceTable>) getWrapper().getExtEventAdvanceTable();
                                          if (advanced.size()>0) {
                                          Collections.sort(advanced, new Comparator<ExtEventAdvanceTable>() {

                                                @Override
                                                public int compare(ExtEventAdvanceTable o1, ExtEventAdvanceTable o2) {
                                                      // TODO Auto-generated method stub
                                                      return o1.getBALANCE().compareTo(o2.getBALANCE());
                                                }
                                          });
                                          
//                                        System.out.println("Available balance : " + advanced.get(0).getBALANCE() +" and Utilization amount entered: " + advanced.get(0).getAMTUTIL());
//                                        
                                          if (advanced.get(0).getAMTUTIL().compareTo(advanced.get(0).getBALANCE()) == 1) {
                                          //    Loggers.general().error(LOG, "Insufficient balance");
                                                System.out.println("Insufficient balance");
                                                validationDetails.addError(ErrorType.Other, "Utilization Balance entered shouldn't be greater than Available Balance");
                                          }
                                          }                             
                                    } catch(Exception e){
                                          e.printStackTrace();
                                    }
                                    try {
                                          System.out.println("inside advance table validation");
                                          getAdvanceTableValidation(validationDetails);
                                    }   catch (Exception e) {
                                        e.printStackTrace();
                                    //    System.out.println("outside BUYERS data:"+e);

                                    }
                                    try {
                                          getRapayAmt(validationDetails);
                                          }catch (Exception e) {
                                                e.printStackTrace();
                                          }
                                    
                                     try{
                                
                                 getUidWarning(validationDetails);
                                 System.out.println("inside uid warning");
                                 }
                              catch (Exception e)
                              {
                                 System.out.println("Exception in uid warning-->" + e.getMessage());
                              }
                                     try{
                                           getRealisationCharges();
                                           }
                                           catch (Exception e)
                                    {
                                                 e.printStackTrace();
                                       System.out.println("Exception in realisation-->" + e.getMessage());
                                    }
                                     try {
                                                getPostingBranch(validationDetails);
                                                }catch (Exception e) {
                                                      e.printStackTrace();
                                                }
                              }
                        
            }     
      }
      

      private boolean checkAvailblityOfMarginForCIF(Connection con) {
            // TODO Auto-generated method stub
            return false;
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
      // rs.close();
      // ps.close();
      // } catch (Exception e) {
      // //Loggers.general().info(LOG,"Exception in finally isChargeAccountDiff - " +
      // e.getMessage());
      // }
      // }
      // return isChargeAccountDiff;
      // }

      private String setValueTOString(double d1) {
            DecimalFormat df = new DecimalFormat("#.##");
            BigDecimal dValue = new BigDecimal(df.format(d1)).setScale(2, RoundingMode.HALF_UP);
            return String.valueOf(dValue);
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