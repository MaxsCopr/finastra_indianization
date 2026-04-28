package com.misys.tiplus2.customisation.extension;

//com.misys.tiplus2.customisation.extension.ExportLcPane
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.lang.StringUtils;
/*import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
*/
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisation;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTable;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceDetails;
import com.misys.tiplus2.customisation.entity.ExtEventShippingCollections;
import com.misys.tiplus2.customisation.entity.ExtEventShippingTable;
//import com.misys.tiplus2.customisation.entity.ExtEventInvoiveDetails;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetails;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
/*import com.misys.tiplus2.customisation.extension.PostingExtension;*/
//import com.misys.tiplus2.customisation.extension.PostingCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class OdcCreate extends ConnectionMaster{
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(OdcCreate.class);
      Connection con, con1 = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst, ship_prepare = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ship_result = null;

      public boolean onPostInitialise() {

            Loggers.general().debug(LOG, "###onPostInitialise..");
            System.out.println("###onPostInitialise..");
            // GETTING LOB
            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  System.out.println("###PROPcode is not null..");
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  // PROPCode.getPropval());
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
            String gateway = getDriverWrapper().getEventFieldAsText("FRGI", "l", "");
            if (dailyval.equalsIgnoreCase("NO")) {

                  System.out.println("###dailyVal = NO..");
                  String strLog = "Log";
                  String dailyval_Log = "";
                  @SuppressWarnings("unchecked")
                  AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                              .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
                  EXTGENCUSTPROP CodeLog = queryLog.getUnique();
                  if (CodeLog != null) {

                        System.out.println("###CodeLog is not null..");
                        dailyval_Log = CodeLog.getPropval();
                  } else {
                        // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

                  } // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSEXPBILLclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSEXPCOLACCPTlayHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp1 = getPane().getCtlSFMSEXPCOLNONACCPTlayHyperlink();
                        sfmsExp1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp2 = getPane().getCtlSFMSOUTDOCORlayHyperlink();
                        sfmsExp2.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  try {
                        String Hyperreferel12 = getWorkflow().trim();
//                      ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTEXPBILLclayHyperlink();
//                      csmreftrack1.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALEXPBILLclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
//                      ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKEXPBILLclayHyperlink();
//                      cpcreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
//                      ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELEXPBILLclayHyperlink();
//                      cpcreftrack.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
//                      ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTEXPBILLclayHyperlink();
//                      dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTOUTDOCORlayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTEXPCOLACCPTlayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTEXPCOLNONACCPTlayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTOUTCOLMAINlayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTOUTCOLMANULlayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }


                  // InwardLink
                  try {
                        String HyperInward = getINWARDREM();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlINWARDEXPBILLclayHyperlink();
                        InwardLink.setUrl(HyperInward);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"InwardLink exception----->" +//
                        // ees.getMessage());
                  }
                  // Get Packing Credit A/c Outstanding
                  try {
                        String HyperInward = getACCOUNT();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlACCOUNTEXPBILLclayHyperlink();
                        InwardLink.setUrl(HyperInward);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"InwardLink exception----->" +//
                        // ees.getMessage());
                  }

                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (gateway.equalsIgnoreCase("N") && !gateway.equalsIgnoreCase("Y")) {
                        if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                              if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")) {
                                    try {
                                          // //Loggers.general().info(LOG,"get value for LOB");
                                          System.out.println("###getting value for LOB..");
                                          getLOBCREATE();
                                    } catch (Exception ee) {
                                          Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                                    } finally {
                                          // //Loggers.general().info(LOG,"finally LOB ");
                                    }
                              }

                              String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                              if ((prd_typ.equalsIgnoreCase("OCI")) && getMinorCode().equalsIgnoreCase("CRE")) {
                                    Loggers.general().debug(LOG, "###OCI & CRE..");
                                    System.out.println("###Prodtype: OCI Event Create..");
                                    EventPane pane = (EventPane) getPane();
                                    pane.getBtnFetchShipDetEXPBILLclay().setEnabled(false);
                                    pane.getBtnFetchInvDetEXPBILLclay().setEnabled(false);

                                    pane.getExtEventShippingCollectionsNew().setEnabled(false);
                                    pane.getExtEventShippingCollectionsDelete().setEnabled(false);
                                    pane.getExtEventShippingdetailsNew().setEnabled(false);
                                    pane.getExtEventShippingdetailsDelete().setEnabled(false);
                                    pane.getExtEventShippingdetailsUpdate().setEnabled(false);
                                    pane.getExtEventInvoiceDetailsNew().setEnabled(false);
                                    pane.getExtEventInvoiceDetailsUpdate().setEnabled(false);
                                    pane.getExtEventInvoiceDetailsDelete().setEnabled(false);

                                    pane.getExtEventAdvanceTableNew().setEnabled(false);
                                    pane.getExtEventAdvanceTableDelete().setEnabled(false);
                                    pane.getExtEventAdvanceTableUpdate().setEnabled(false);
                                    pane.getBtnonfetchEXPBILLclay().setEnabled(false);

                              }

                              else {
                                    // //Loggers.general().info(LOG,"Product type is forign");
                                    System.out.println("###Product type is foreign..");
                                    
                                    //should we enable here?
                                    System.out.println("###To check if enable..");
                                    EventPane pane = (EventPane) getPane();
                                    pane.getExtEventAdvanceTableNew().setEnabled(true);
                                    pane.getExtEventAdvanceTableDelete().setEnabled(true);
                                    pane.getExtEventAdvanceTableUpdate().setEnabled(true);
                                    pane.getBtnonfetchEXPBILLclay().setEnabled(true);
                              }

//                            try {
//                                  currencyCalc();
//                            } catch (Exception e) {
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());
//
//                                  }
//                            }
                              if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")&& stepID.equalsIgnoreCase("SCRUTINIZER1")) {
                                    Loggers.general().debug(LOG, "### ODC CRE1");
                                    try {
                                          if(prd_typ.equalsIgnoreCase("01E")){
                                                getPane().setNATTRPRD("25");
                                          }
                                    }
                                    catch(Exception e){
                                          e.printStackTrace();
                                    }
                                    }
            
                              if (getMajorCode().equalsIgnoreCase("ODC") && (prd_typ.equalsIgnoreCase("OCI"))) {
                                    // Loggers.general().info(LOG,"Else systemOutput");
                                    getPane().setUSDAMT("");
                                    getPane().setCRAMT_Name("");
                                    getPane().setINRAMT("");
                              }

                              if (prd_typ.equalsIgnoreCase("MBI")) {
                                    getPane().setPERADV("Full");
                              }

                              String mas = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              Loggers.general().info(LOG,"Master Reference==>"+mas);
                              // String = "0172ELFX0003716";
                              String evt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                              // String str ="";
                              try {
                                    // //Loggers.general().info(LOG,"enter into bill generate no");
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
                                    Loggers.general().info(LOG,"Master Reference bllrefno==>"+getPane().getBLLREFNO());
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exception" + e);
                              }

                              if (getMajorCode().equalsIgnoreCase("ODC")) {
                                    getPane().onPRESHIPFINEXPBILLclayButton();

                                    getPane().getExtEventLoanDetailsNew().setEnabled(false);
                                    getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                                    getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                                    getPane().getExtEventLoanDetailsUp().setEnabled(false);
                                    getPane().getExtEventLoanDetailsDown().setEnabled(false);
                              }

                              // WORKFLOW CHECKLIST
                              if (stepID.equalsIgnoreCase("CSM")
                                          || stepID.equalsIgnoreCase("CBS Maker") && prd_typ.equalsIgnoreCase("OCF")) {

                                    try {
                                          Loggers.general().debug(LOG, "###WORKFLOW CHECK..");
                                          List<ExtEventShippingCollections> shipTable = (List<ExtEventShippingCollections>) getWrapper()
                                                      .getExtEventShippingCollections();
                                          // //Loggers.general().info(LOG,"shipTable.size - " +
                                          // shipTable.size());
                                          for (int i = 0; i < shipTable.size(); i++) {

                                                ExtEventShippingCollections ship = shipTable.get(i);
                                                if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")) {
                                                      Loggers.general().debug(LOG, "### ODC CRE2");
                                                      BigDecimal shipAmt = new BigDecimal("0");
                                                      BigDecimal notVal = new BigDecimal("1");
                                                      ship.setCREPAY(shipAmt);
                                                      ship.setCREPAYCurrency(ship.getCSHPAMTCurrency());
                                                      ship.setCSHCOLAM(shipAmt);
                                                      ship.setCSHCOLAMCurrency(ship.getCSHPAMTCurrency());
                                                      ship.setCNOTIONL(notVal);
                                                }
                                                double notional = 1;

                                                notional = ship.getCNOTIONL().doubleValue();
                                                // double ship_Amount = 0.0;

                                                String ship_str = ship.getCSHPAMT().toString();
                                                BigDecimal shipamt_str = new BigDecimal(ship_str);
                                                // ship_Amount =
                                                // ship.getCSHPAMT().doubleValue();
                                                // double shipamt = (notional * ship_Amount);
                                                BigDecimal notional_big = new BigDecimal(notional);
                                                BigDecimal equi_bill = notional_big.multiply(shipamt_str);

                                                ship.setCEQUBILL(equi_bill);
                                                ship.setCEQUBILLCurrency(ship.getCSHPAMTCurrency());
                                                ship.setCOUTSAMT(shipamt_str);
                                                ship.setCOUTSAMTCurrency(ship.getCSHPAMTCurrency());

                                          }

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Exception in validating for
                                          // notional
                                          // rate
                                          // - " + e.getMessage());
                                    }
                              }

                              if (getMinorCode().equalsIgnoreCase("CRE")) {
                                    try {
                                          getrtgsNEFT();

                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Exception in purpose
                                          // code--------->"
                                          // + e.getMessage());
                                    }
                              }

                              try {
                                    // //Loggers.general().info(LOG,"Notional due date is calling");
                                    getNotionalDueDate();
                              } catch (Exception ee) {
                                    // Loggers.general().info(LOG,"Notional due date is calling--->"
                                    // +
                                    // ee.getMessage());
                                    // //Loggers.general().info(LOG,"LOB Catch");
                              }

                              // P0105 auto populated
                              try {
                                    getrbiPurcodeCode();

                              } catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception in Purpose code---->" + e.getMessage());

                                    }
                              }

                              if (getMajorCode().equalsIgnoreCase("ODC")) {
                                    try {

                                          getactualPenalRate();
                                          Loggers.general().debug(LOG, "### ODC CRE3");
                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"Exception update" +
                                          // e.getMessage());
                                    }
                              }
                              // check list table disabled

                              // //Loggers.general().info(LOG,"prodCode for
                              // chacklist------------>" +
                              // stepID);

                              EventPane pane = (EventPane) getPane();
                              // //Loggers.general().info(LOG,"Disable pane------------>");

                              // hs code population

                              String hscodeval = getWrapper().getHMON();
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
                                          // Loggers.general().info(LOG,"exceptio is " + e);
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

                              // Shipment value toUpperCase();

                              String shipfrom = getWrapper().getSHIPFTO_Name();
                              String shipto = getWrapper().getSHIPTOP_Name();
                              try {
                                    String fromVal = shipfrom.toUpperCase();
                                    String toval = shipto.toUpperCase();
                                    if (shipfrom.length() > 0 || shipto.length() > 0) {
                                          getPane().setSHIPFTO_Name(fromVal);
                                          getPane().setSHIPTOP_Name(toval);
                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Shipment value --->" +
                                    // e.getMessage());
                              }

                              // port of destination description

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
                                          String portvalqury = "select Trim(PORTDESC),Trim(COUNTRY) from EXTPORTODLOAD where trim(PORTLOAD)=trim('"
                                                      + portload + "')";
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
                              if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")) {
                                    try {
                                          Loggers.general().debug(LOG, "### ODC CRE5");
                                          freezeMttNumber();
                                          System.out.println("INSIDE freezeMttNumber OF  odc CREATE");
                                    }
                                    catch(Exception e){
                                          e.printStackTrace();
                                    }
                                    }
                              try {

                                    getcountryCode();

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception update" +
                                    // e.getMessage());
                              }
                              //INC4431622 starts
                              try {
                                     Loggers.general () .info(LOG,"Entered method");
                                    removeSpace();

                              } catch (Exception e) {
                                     Loggers.general () .info(LOG,"Exception update" +e.getMessage());
                              }
                              //INC4431622 ends

                        }
                  }
            }
            return false;
      }

      // code of repayment amount

      @Override
      public void onValidate(ValidationDetails validationDetails) {
            
            
            System.out.println("###onValidete..");
            EventPane pane1 = (EventPane)getPane();
            boolean isPaneEnabled = pane1.getExtEventShippingCollectionsNew().isEnabled();
            System.out.println(isPaneEnabled);
            
            String strPropName = "MigrationDone";
            String dailyval = "";
            @SuppressWarnings("unchecked")
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

            if (dailyval.equalsIgnoreCase("NO")) {

                  //INC4431622 ends
                  System.out.println("INSIDE ON VALIDATE");
                  try {
                        getPostingFxrate();
                  }
                  catch (Exception e){
                        e.printStackTrace();
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
                  String gateway = getDriverWrapper().getEventFieldAsText("FRGI", "l", "");
                  if (gateway.equalsIgnoreCase("N") && !gateway.equalsIgnoreCase("Y")) {

                        String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");

                        String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();

                        if ((prd_typ.equalsIgnoreCase("OCI")) && getMinorCode().equalsIgnoreCase("CRE")) {
                              Loggers.general().debug(LOG, "###GATEWAY..");
                              EventPane pane = (EventPane) getPane();
                              pane.getBtnFetchShipDetEXPBILLclay().setEnabled(false);
                              pane.getBtnFetchInvDetEXPBILLclay().setEnabled(false);

                              pane.getExtEventShippingCollectionsNew().setEnabled(false);
                              pane.getExtEventShippingCollectionsDelete().setEnabled(false);
                              pane.getExtEventShippingdetailsNew().setEnabled(false);
                              pane.getExtEventShippingdetailsDelete().setEnabled(false);
                              pane.getExtEventShippingdetailsUpdate().setEnabled(false);
                              pane.getExtEventInvoiceDetailsNew().setEnabled(false);
                              pane.getExtEventInvoiceDetailsUpdate().setEnabled(false);
                              pane.getExtEventInvoiceDetailsDelete().setEnabled(false);

                              pane.getExtEventAdvanceTableNew().setEnabled(false);
                              pane.getExtEventAdvanceTableDelete().setEnabled(false);
                              pane.getExtEventAdvanceTableUpdate().setEnabled(false);
                              pane.getBtnonfetchEXPBILLclay().setEnabled(false);

                        }

                        else {

                              EventPane pane = (EventPane) getPane();
                              pane.getExtEventAdvanceTableNew().setEnabled(true);
                              pane.getExtEventAdvanceTableDelete().setEnabled(true);
                              pane.getExtEventAdvanceTableUpdate().setEnabled(true);
                              pane.getBtnonfetchEXPBILLclay().setEnabled(true);

                              pane.getExtEventShippingCollectionsNew().setEnabled(true);
                              pane.getExtEventShippingCollectionsDelete().setEnabled(true);
                              pane.getExtEventShippingdetailsNew().setEnabled(true);
                              pane.getExtEventShippingdetailsDelete().setEnabled(true);
                              pane.getExtEventShippingdetailsUpdate().setEnabled(true);
                              pane.getExtEventInvoiceDetailsNew().setEnabled(true);
                              pane.getExtEventInvoiceDetailsUpdate().setEnabled(true);
                              pane.getExtEventInvoiceDetailsDelete().setEnabled(true);

                              pane.getBtnFetchShipDetEXPBILLclay().setEnabled(true);
                              pane.getBtnFetchInvDetEXPBILLclay().setEnabled(true);
                        }

                        // SFMS
                        try {
                              String getHyperSFMS = getIFSCSEARCH();
                              ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSEXPBILLclayHyperlink();
                              sfmsExpAdv.setUrl(getHyperSFMS);
                              ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSEXPCOLACCPTlayHyperlink();
                              sfmsExp.setUrl(getHyperSFMS);
                              ExtendedHyperlinkControlWrapper sfmsExp1 = getPane().getCtlSFMSEXPCOLNONACCPTlayHyperlink();
                              sfmsExp1.setUrl(getHyperSFMS);
                              ExtendedHyperlinkControlWrapper sfmsExp2 = getPane().getCtlSFMSOUTDOCORlayHyperlink();
                              sfmsExp2.setUrl(getHyperSFMS);
                        } catch (Exception ees) {
                              // Loggers.general().info(LOG,ees.getMessage());
                        }
                        // WORKFLOW CHECKLIST

                        try {
                              String Hyperreferel12 = getWorkflow().trim();
//                            ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTEXPBILLclayHyperlink();
//                            csmreftrack1.setUrl(Hyperreferel12);

                        } catch (Exception ees) {
                              // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                              // ees.getMessage());
                        }

                        // REFEREL TRACKING
                        try {

                              String Hyperreferel3 = getReferealtracking().trim();
//                            ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALEXPBILLclayHyperlink();
//                            csmreftrack.setUrl(Hyperreferel3);

                        } catch (Exception ees) {
                              // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                              // ees.getMessage());
                        }

                        // Workflow CBS
                        try {
                              String Hyperreferel12 = getWorkflowCBS().trim();
//                            ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKEXPBILLclayHyperlink();
//                            cpcreftrack1.setUrl(Hyperreferel12);
                        } catch (Exception ees) {
                              // Loggers.general().info(LOG,"WORKFLOW CHECKLIST
                              // WorkflowCBS--------->"
                              // + ees.getMessage());
                        }

                        // Refereal CBS
                        try {
                              String Hyperreferel12 = getReferealtrackingCBS().trim();
//                            ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELEXPBILLclayHyperlink();
//                            cpcreftrack.setUrl(Hyperreferel12);

                        } catch (Exception ees) {
                              // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->"
                              // +
                              // ees.getMessage());
                        }

                        // InwardLink
                        try {
                              String HyperInward = getINWARDREM();
                              ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlINWARDEXPBILLclayHyperlink();
                              InwardLink.setUrl(HyperInward);

                        } catch (Exception ees) {
                              // Loggers.general().info(LOG,"InwardLink exception----->" +//
                              // ees.getMessage());
                        }

                        if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")) {
                              try {
                                    // //Loggers.general().info(LOG,"get value for LOB");
                                    Loggers.general().debug(LOG, "### ODC CRE6");
                                    getLOBCREATE();
                              } catch (Exception ee) {
                                    Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                              } finally {
                                    // //Loggers.general().info(LOG,"finally LOB ");
                              }
                        }

                        // FCY Tax calculation
                        try {

                              getFCCTCALCULATION();

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" +
                              // e.getMessage());
                        }
                        
                        if(getMajorCode().equalsIgnoreCase("ODC")&&getMinorCode().equalsIgnoreCase("CRE")){
                        
                              Loggers.general().info(LOG,"Only for odc create");
                        try{
                              
                              String recbank=getDriverWrapper().getEventFieldAsText("RCV","p","cc").trim();
                              String sendto=getDriverWrapper().getEventFieldAsText("SND","p","cc").trim();
                              String drawee=getDriverWrapper().getEventFieldAsText("DRE","p","cc").trim();
                              Loggers.general().info(LOG,"Received bank empty=========>"+recbank);
                              Loggers.general().info(LOG,"send to empty=========>"+sendto);
                              Loggers.general().info(LOG,"drawee bank empty=========>"+drawee);
                              
                              if(recbank.equalsIgnoreCase("")||recbank==null)
                              {
                                    Loggers.general().info(LOG,"Received bank empty");
                                    validationDetails.addError(ErrorType.Other, "Received bank country should be entered  [CM]");
                                    
                              }
                              if(sendto.equalsIgnoreCase("")||sendto==null)
                              {
                                    Loggers.general().info(LOG,"send to bank empty");
                                    validationDetails.addError(ErrorType.Other, "Send to bank country should be entered  [CM]");
                                    
                              }
                              if(drawee.equalsIgnoreCase("")||drawee==null)
                              {
                                    Loggers.general().info(LOG,"drawee bank empty");
                                    validationDetails.addError(ErrorType.Other, "Drawee bank country should be entered  [CM]");
                                    
                              }
                              
                              
                              
                        }
                        catch(Exception e)
                        {
                              Loggers.general().info(LOG,"Exception in country===>"+e.getMessage());
                        }
                        }

                        if (getMinorCode().equalsIgnoreCase("CRE")) {
                              try {
                                    getrtgsNEFT();

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception in purpose
                                    // code--------->"
                                    // + e.getMessage());
                              }
                        }

                        String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                        // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

                        List<ExtEventShippingdetails> shipdetails = (List<ExtEventShippingdetails>) getWrapper()
                                    .getExtEventShippingdetails();
                        if (shipdetails.size() == 0) {
                              // //Loggers.general().info(LOG,"shipdetails --->" + shipdetails);
                              EventPane pane = (EventPane) getPane();
                              pane.getBtnFetchShipDetEXPBILLclay().setEnabled(true);
                        }

                        List<ExtEventInvoiceDetails> invodetails = (List<ExtEventInvoiceDetails>) getWrapper()
                                    .getExtEventInvoiceDetails();
                        if (invodetails.size() == 0) {
                              // //Loggers.general().info(LOG,"invodetails --->" + invodetails);
                              EventPane pane = (EventPane) getPane();
                              pane.getBtnFetchInvDetEXPBILLclay().setEnabled(true);
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

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING CSM query" + query_wrk);
                                    }
                                    con = getConnection();
                                    ps1 = con.prepareStatement(query_wrk);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          count = rs1.getInt(1);
                                          // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in
                                          // while
                                          // loop----> " + count3);

                                    }
                              } else if (step_csm.equalsIgnoreCase("CBS Maker")) {
                                    String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                                + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                                + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CBS Maker'";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING CBS Maker query" + query_wrk);
                                    }
                                    con = getConnection();
                                    ps1 = con.prepareStatement(query_wrk);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          count = rs1.getInt(1);
                                          // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in
                                          // while
                                          // loop----> " + count3);

                                    }
                              }
                              if (count < 1 && step_Input.equalsIgnoreCase("i")
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CSM")
                                                      || step_csm.equalsIgnoreCase("AdhocCSM"))
                                          && (getMinorCode().equalsIgnoreCase("CRE")) && !getMinorCode().equalsIgnoreCase("FEC")
                                          && !evnt.equalsIgnoreCase("FEC") && (!gateway.equalsIgnoreCase("Y"))) {

                                    validationDetails.addError(ErrorType.Other, "Workflow checklist is mandatory  [CM]");                             }

                              else {
                                    // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in else
                                    // loop----> " + count);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in else");
                                    }
                              }

                        } catch (Exception e1) {
                              //

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Exception ETT_WF_CHKLST_TRACKING" + e1.getMessage());
                              }
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
                        
                        
                        
                        
                  
                        

                        if (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("CBS Maker 1")) {
                              try {
                                    con = getConnection();
                                    String query = "select wm.MESSAGE from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'MIXFX' and MAS.MASTER_REF = '"
                                                + MasterReference + "' and BEV.REFNO_PFIX= '" + evnt + "' and BEV.REFNO_SERL = '"
                                                + evvcount + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"FX deal message in authorization " + query);
                                    }
                                    String fxDeal = "";
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while");
                                          String fxAmount = rs1.getString(1);
                                          // Loggers.general().info(LOG,"FX deal message===>" +
                                          // fxAmount);
                                          fxDeal = fxAmount.substring(4, 6);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,"FX deal message converted===>" + fxDeal);

                                          }
                                    }

                                    if (fxDeal.equalsIgnoreCase("FX") && (step_csm.equalsIgnoreCase("CBS Authoriser")
                                                || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          // Loggers.general().info(LOG,"warning message in
                                          // authorization
                                          // if
                                          // loop" + count3 + " and step_Id" + step_csm);
                                          validationDetails.addError(ErrorType.Other,
                                                      "FX deal amount more than transaction amount [CM]");
                                    }

                                    else {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,"FX deal amount more than else===>" + step_csm);

                                          }
                                    }

                              } catch (Exception e1) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"Exception FX deal amount more than===>" + e1.getMessage());

                                    }
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
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        }
                        
                        // Notes Populated in Summary

                        try {
                              con = getConnection();
                              String query = "select * from master mas,NOTE, TIDATAITEM tid WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 AND (NOTE.TYPE NOT IN (3,129,1089) or NOTE.TYPE is null) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
                                          + MasterReference + "' ";
                              //// Loggers.general().info(LOG,"Notes Populated in Summary
                              //// query====> "
                              //// + query);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Notes Populated in Summary query====> " + query);

                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String notes = rs1.getString(1);
                                    if (notes.length() > 0 && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Notes Populated in Summary. Kindly check [CM]");
                                    }

                              }

                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception" + e1.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Notes Populated in Summary query====> " + e1.getMessage());

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

                        // P0105 auto populated

                        try {
                              getrbiPurcodeCode();

                        } catch (Exception e) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Exception in purpose code===>" + e.getMessage());

                              }
                        }

                        if (getMajorCode().equalsIgnoreCase("ODC")) {
                              getPane().onPRESHIPFINEXPBILLclayButton();
                              getPane().getExtEventLoanDetailsNew().setEnabled(false);
                              getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                              getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                              getPane().getExtEventLoanDetailsUp().setEnabled(false);
                              getPane().getExtEventLoanDetailsDown().setEnabled(false);
                        }
                        if (getMajorCode().equalsIgnoreCase("ODC")) {
                              try {

                                    getactualPenalRate();

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception update" +
                                    // e.getMessage());
                              }
                        }

                        List<ExtEventAdvanceTable> advanced = (List<ExtEventAdvanceTable>) getWrapper()
                                    .getExtEventAdvanceTable();
                        // //Loggers.general().info(LOG,"Product type is--->" + prd_typ);

                        if (prd_typ.equalsIgnoreCase("MBI")) {
                              getPane().setPERADV("Full");
                        }

                        String advrec = getWrapper().getPERADV();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"Advance received field" + advrec + "advanced size" + advanced.size());
                        }
                        if ((!advrec.equalsIgnoreCase(""))
                                    && (advrec.equalsIgnoreCase("Full") || advrec.equalsIgnoreCase("Part"))) {

                              if ((advanced.size() == 0 || advanced.size() < 1) && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker")) && prd_typ.equalsIgnoreCase("MBI")) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Advance Received selection is selected,please input Advance Remittance Grid [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"Advance Remittance advanced" + advanced);
                                    }
                              }
                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Advance Remittance Grid" + advrec);
                              }
                        }

                        // -----------------shipping valiadtion
                        // start-----------------------

                        String mert = getDriverWrapper().getEventFieldAsText("cARQ", "l", "");
                        // //Loggers.general().info(LOG,"Merchanting trade--->" + mert);
                        String deemed = getDriverWrapper().getEventFieldAsText("cAGM", "l", "").trim();
                        // //Loggers.general().info(LOG,"Deemed Exports--->" + deemed);
                        int shippingCount = 0;
                        int shippingCount1 = 0;
                        if (null != mert && mert.equalsIgnoreCase("N")) {
                              // //Loggers.general().info(LOG,"Merchanting check Box is Not
                              // checked" +
                              // mert);
                              shippingCount++;
                        } else {
                              // Loggers.general().info(LOG,"Merchant Tick box is checked");
                        }
                        if (null != deemed && deemed.equalsIgnoreCase("N")) {
                              // //Loggers.general().info(LOG,"Deemed is not checked" + deemed);
                              shippingCount1++;
                        } else {

                              // Loggers.general().info(LOG,"Deemed is checked");
                        }
                        String formtyp = getWrapper().getFORTYP();

                        String sezCust = getDriverWrapper().getEventFieldAsText("PRM", "p", "cBBI").trim();
                        // Loggers.general().info(LOG,"Form type for SEZ" + formtyp + " SEZ
                        // customer" + sezCust);
                        if (sezCust.equalsIgnoreCase("SEZ") && getMinorCode().equalsIgnoreCase("CRE")
                                    && (!prd_typ.equalsIgnoreCase("OCI"))
                                    && (prd_typ.equalsIgnoreCase("OCF") || prd_typ.equalsIgnoreCase("MBI"))) {
                              if (!formtyp.equalsIgnoreCase("EXEMPTED") && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Please select Form type as EXEMPTED as Exporter category is SEZ [CM]");
                              } else {

                              }

                        }

                        List<ExtEventShippingCollections> shipgrid = (List<ExtEventShippingCollections>) getWrapper()
                                    .getExtEventShippingCollections();

                        if ((prd_typ.equalsIgnoreCase("OCF") || prd_typ.equalsIgnoreCase("MBI"))
                                    && formtyp.equalsIgnoreCase("EXEMPTED")) {
                              Loggers.general().debug(LOG, "###EXEMPTED..");
                              if (shipgrid.size() > 0 && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject")) && getMinorCode().equalsIgnoreCase("CRE")) {

                                    validationDetails.addError(ErrorType.Other,
                                                "Form type is selected as 'EXEMPTED' Kindly delete the shipping bill details [CM]");
                              }
                        }

                        if (!prd_typ.equalsIgnoreCase("OCI")
                                    && (prd_typ.equalsIgnoreCase("OCF") || prd_typ.equalsIgnoreCase("MBI"))
                                    && (shippingCount > 0 && shippingCount1 > 0) && (!formtyp.equalsIgnoreCase("EXEMPTED"))) {

                              // //Loggers.general().info(LOG,"Merchanting trade--->" + mert +
                              // "<<<<>>>"
                              // + "Deemed Exports--->" + deemed);
                              try {

                                    // //Loggers.general().info(LOG,"Form type for shipping--->" +
                                    // formtyp);
                                    if (formtyp.equalsIgnoreCase("") && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject")) && getMinorCode().equalsIgnoreCase("CRE")
                                                && getMajorCode().equalsIgnoreCase("ODC")) {
                                          validationDetails.addError(ErrorType.Other, "Form type is mandatory [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"Form type for shipping--->" +
                                          // formtyp);
                                    }

                                    List<ExtEventShippingCollections> shiplcd = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();

                                    // //Loggers.general().info(LOG,"size shipping grid " +
                                    // shiplcd.size());
                                    String AmountS = getDriverWrapper().getEventFieldAsText("AMT", "v", "m").trim();
                                    String cur1 = getDriverWrapper().getEventFieldAsText("AMT", "v", "c").trim();
                                    double amountint = Double.parseDouble(AmountS);
                                    int counter = 0;
                                    double am = 0.0;

                                    if (shiplcd.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject")) && getMinorCode().equalsIgnoreCase("CRE")) {

                                          // //Loggers.general().info(LOG,"size of grid inside if loop
                                          // " +
                                          // shiplcd.size());
                                          validationDetails.addError(ErrorType.Other, "Shipping Bill details is mandatory [CM]");
                                    }

                                    else {

                                          List<ExtEventShippingdetails> shiplc = (List<ExtEventShippingdetails>) getWrapper()
                                                      .getExtEventShippingdetails();
                                          List<ExtEventInvoiceDetails> invlc = (List<ExtEventInvoiceDetails>) getWrapper()
                                                      .getExtEventInvoiceDetails();

                                          if ((formtyp.trim().equalsIgnoreCase("EDI") || formtyp.trim().equalsIgnoreCase("SOFTEX"))
                                                      && shiplc.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {

                                                validationDetails.addError(ErrorType.Other,
                                                            "Please click Fetch button to get the Shipping bill details [CM]");
                                          } else if (formtyp.trim().equalsIgnoreCase("EDF") && shiplc.size() == 0
                                                      && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {

                                                validationDetails.addError(ErrorType.Other,
                                                            "Kindly input shipping bill details manually as Form Type is 'EDF' [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"Form type is" + formtyp);
                                          }

                                          if ((formtyp.trim().equalsIgnoreCase("EDI") || formtyp.trim().equalsIgnoreCase("SOFTEX"))
                                                      && invlc.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {

                                                validationDetails.addError(ErrorType.Other,
                                                            "Please click Fetch button to get the Invoice details [CM]");
                                          } else if (formtyp.trim().equalsIgnoreCase("EDF") && invlc.size() == 0
                                                      && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {

                                                validationDetails.addError(ErrorType.Other,
                                                            "Kindly input Invoice bill details manually as Form Type is 'EDF' [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"Form type is" + formtyp);
                                          }

                                          String billrow = shiplcd.get(0).getCBILLNUM();
                                          // //Loggers.general().info(LOG,"Shipping Number billrow
                                          // softax--->" + billrow);
                                          String formNo = shiplcd.get(0).getCFORMN();
                                          // //Loggers.general().info(LOG,"Form Number softax--->" +
                                          // formNo);
                                          // String formtyp = getWrapper().getFORTYP();
                                          // //Loggers.general().info(LOG,"Form type for shipping--->"
                                          // +
                                          // formtyp);

                                          if (formtyp.trim().equalsIgnoreCase("EDI") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {
                                                if ((billrow.trim().equalsIgnoreCase(""))
                                                            || billrow.trim().isEmpty() && getMinorCode().equalsIgnoreCase("CRE")) {
                                                      // //Loggers.general().info(LOG,"Shipping Number
                                                      // blank
                                                      // softax--->");
                                                      validationDetails.addError(ErrorType.Other,
                                                                  "Form Type: EDI,Shipping Bill No is Mandatory in shipping details pane :[CM]");
                                                }
                                          }

                                          if (formtyp.trim().equalsIgnoreCase("SOFTEX") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {
                                                if ((formNo.trim().equalsIgnoreCase(""))
                                                            || formNo.trim().isEmpty() && getMinorCode().equalsIgnoreCase("CRE")) {
                                                      validationDetails.addError(ErrorType.Other,
                                                                  "Form Type:SOFTEX,Form No is Mandatory in shipping details pane :[CM]");
                                                }
                                          }

                                          if (shiplcd.size() > 0) {

                                                // Loggers.general().info(LOG,"Shipping Bill details
                                                // validation in else>>>");

                                                double shipamt = 0.0;
                                                double am_rey = 0;
                                                double rep_total = 0.0;
                                                double repay_amt = 0.0;
                                                double invadob_total = 0.0;
                                                double equal_tot = 0.0;
                                                double equal_total = 0.0;
                                                double equal_amt = 0.0;
                                                double notioanl = 1;
                                                double shpamt = 0.0;
                                                double equal_Val = 0.0;
                                                String shipstr = "";
                                                ExtEventShippingCollections shipinglc = null;
                                                for (int l = 0; l < shiplcd.size(); l++) {

                                                      shipinglc = shiplcd.get(l);
                                                      shipamt = shipinglc.getCSHPAMT().doubleValue();
                                                      // //Loggers.general().info(LOG,"Shipping bill
                                                      // amount
                                                      // ---->" + shipamt);
                                                      double shipamount = shipinglc.getCSHPAMT().doubleValue();
                                                      if (shipamount < 0 && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                                  && getMinorCode().equalsIgnoreCase("CRE")) {
                                                            // //Loggers.general().info(LOG,"Amount of
                                                            // shipping"
                                                            // +
                                                            // shpamt);
                                                            validationDetails.addError(ErrorType.Other,
                                                                        "Shipping bill amount should not be negative [CM]");
                                                      } else {
                                                            // Loggers.general().info(LOG,"Amount of
                                                            // shipping in
                                                            // else" + shipamount);
                                                      }

                                                      try {
                                                            String billrow1 = shipinglc.getCBILLNUM();
                                                            String formNo1 = shipinglc.getCFORMN();

                                                            if (formtyp.trim().equalsIgnoreCase("EDF") && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                                        && getMinorCode().equalsIgnoreCase("CRE")) {
                                                                  if (((billrow1.equalsIgnoreCase("") || billrow1 == null)
                                                                              && (formNo1.equalsIgnoreCase("") || formNo1 == null))
                                                                              && getMinorCode().equalsIgnoreCase("CRE")) {
                                                                        validationDetails.addError(ErrorType.Other,
                                                                                    "Form Type:EDF,Form No or Shipping Bill No is Mandatory in shipping details pane :[CM]");
                                                                  } else {
                                                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                              Loggers.general().info(LOG,"Shipping bill number" + billrow1);
                                                                              Loggers.general().info(LOG,"Form number" + formNo1);
                                                                        }
                                                                  }
                                                            }
                                                      } catch (Exception e) {
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Exception ODC Shiping bill number and form number--->"
                                                                              + e.getMessage());
                                                            }
                                                      }

                                                      try {
                                                            double repayAmt = 0;
                                                            repayAmt = shipinglc.getCREPAY().doubleValue();
                                                            if (repayAmt > 0 && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                                        && !prd_typ.equalsIgnoreCase("MBI")) {
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                        Loggers.general().info(LOG,"Shipping repayAmt if loop===>" + repayAmt);
                                                                  }
                                                                  validationDetails.addError(ErrorType.Other,
                                                                              "Repayment amount should not greater than '0' OCF product type [CM]");
                                                            } else if ((repayAmt == 0 || repayAmt < 1) && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                                        && prd_typ.equalsIgnoreCase("MBI")) {
                                                                  validationDetails.addError(ErrorType.Other,
                                                                              "Repayment amount should greater than '0' MBI product type [CM]");
                                                            } else {
                                                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                        Loggers.general().info(LOG,"Repayment amount in else" + repayAmt);
                                                                  }
                                                            }
                                                            BigDecimal repayAmount = shipinglc.getCREPAY();
                                                            BigDecimal shortAmount = shipinglc.getCSHCOLAM();
                                                            BigDecimal outAmount = shipinglc.getCOUTSAMT();
                                                            BigDecimal repayShortAmount = repayAmount.add(shortAmount);
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Repayment and short collection amount"
                                                                              + repayShortAmount + "Outstanding Amount" + outAmount);
                                                            }
                                                            if ((repayShortAmount.compareTo(outAmount) > 0)
                                                                        && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                                                                        && prd_typ.equalsIgnoreCase("MBI")) {
                                                                  validationDetails.addError(ErrorType.Other,
                                                                              "Repayment and short collection amount should not greater than Outstanding Amount [CM]");
                                                            }

                                                      } catch (Exception e) {
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,"Repayment amount is blank" + e.getMessage());
                                                            }
                                                      }
                                                      String shipamtcurr = shipinglc.getCSHPAMTCurrency().trim();

                                                      ConnectionMaster connectionMaster = new ConnectionMaster();
                                                      double divideByDecimal = connectionMaster.getDecimalforCurrency(shipamtcurr);
                                                      invadob_total = shipamt / divideByDecimal;
                                                      am = am + invadob_total;

                                                      // //Loggers.general().info(LOG,"amount of
                                                      // Collection "
                                                      // +
                                                      // am);
                                                      // //Loggers.general().info(LOG,"amount of shipping
                                                      // " +
                                                      // amountint);
                                                      if (!cur1.equalsIgnoreCase(shipamtcurr)) {
                                                            counter++;
                                                      }

                                                      equal_amt = shipinglc.getCEQUBILL().doubleValue();
                                                      String equal_cur = shipinglc.getCEQUBILLCurrency();
                                                      // //Loggers.general().info(LOG,"Shipping
                                                      // equaivalent
                                                      // amount " + equal_amt);
                                                      double deci = connectionMaster.getDecimalforCurrency(equal_cur);
                                                      equal_tot = equal_amt / deci;
                                                      equal_total = equal_total + equal_tot;
                                                      DecimalFormat df = new DecimalFormat("#.##");
                                                      equal_Val = Double.valueOf(df.format(equal_total));
                                                      // //Loggers.general().info(LOG,"Shipping
                                                      // equaivalent
                                                      // amount after convert" + equal_total);

                                                }
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Shipping equaivalent amount " + equal_Val
                                                                  + "Bill lodgement amount" + amountint);
                                                }
                                                if (equal_Val != amountint && (step_Input.equalsIgnoreCase("i"))
                                                            && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                        || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                            && getMinorCode().equalsIgnoreCase("CRE") && advanced.size() < 1) {

                                                      validationDetails.addWarning(WarningType.Other,
                                                                  "Sum of shipping bill amount should be equal to Bill lodgement amount [CM]");

                                                } else {
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"Shipping equaivalent amount else" + equal_Val
                                                                        + "Bill lodgement amount" + amountint);
                                                      }
                                                }

                                                if (counter > 0 && (step_Input.equalsIgnoreCase("i"))
                                                            && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                                      // //Loggers.general().info(LOG,"counter VALUE ---->
                                                      // " +
                                                      // counter);
                                                      // validationDetails.addWarning(WarningType.Other,
                                                      // "
                                                      // shipping bill currency is differ from
                                                      // collection
                                                      // currency [CM]");
                                                      notioanl = shipinglc.getCNOTIONL().doubleValue();
                                                      // //Loggers.general().info(LOG,"Notional rate is
                                                      // ---->
                                                      // "
                                                      // + notioanl);
                                                      if (notioanl < 2 && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                              || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase("CRE")) {
                                                            // //Loggers.general().info(LOG,"Notional rate
                                                            // is
                                                            // empty " + notioanl);
                                                            validationDetails.addWarning(WarningType.Other,
                                                                        "Shipping bill currency is different from collection currency, Notional rate is mandatory [CM]");
                                                      } else {

                                                            // Loggers.general().info(LOG,"Notional rate is
                                                            // greater than 0 values----> ");
                                                      }
                                                }
                                          }

                                          // FOB + fright+insurance validation
                                          List<ExtEventInvoiceDetails> invoice_shiplc = (List<ExtEventInvoiceDetails>) getWrapper()
                                                      .getExtEventInvoiceDetails();
                                          try {
                                                double fobAmt = 0.0;
                                                double insAmt = 0.0;
                                                double friAmt = 0.0;
                                                double comm = 0.0;
                                                double pack = 0.0;
                                                double discount = 0.0;
                                                double dedut = 0.0;
                                                double discal = 0.0;
                                                double toFobamt = 0.0;
                                                double toFobamt_tot = 0.0;
                                                
                                                double inscuramt=0.0;                           
                                                BigDecimal inscuramtBig = new BigDecimal(0);
                                                BigDecimal insconamt =new BigDecimal(0);
                                                
                                                double fricuramt=0.0;                           
                                                BigDecimal fricuramtBig = new BigDecimal(0);
                                                BigDecimal friconamt =new BigDecimal(0);
                                                
                                                double commcuramt=0.0;                          
                                                BigDecimal commcuramtBig = new BigDecimal(0);
                                                BigDecimal commconamt =new BigDecimal(0);
                                                
                                                double packcuramt=0.0;                          
                                                BigDecimal packcuramtBig = new BigDecimal(0);
                                                BigDecimal packconamt =new BigDecimal(0);
                                                for (int l = 0; l < invoice_shiplc.size(); l++) {

                                                      ExtEventInvoiceDetails invoice_ship = invoice_shiplc.get(l);
                                                      fobAmt = fobAmt + invoice_ship.getIFOBAMT().doubleValue();
                                                      Loggers.general().info(LOG,"FOBAmount ---> " + fobAmt);
                                                      String fobcur = invoice_ship.getIFOBAMTCurrency();
                                                      
                                                      insAmt = insAmt + invoice_ship.getINSUAMT().doubleValue();
                                                      Loggers.general().info(LOG,"INSAmount ----> " + insAmt);
                                                      String insccy = invoice_ship.getINSUAMTCurrency();
                                                      
                                                      friAmt = friAmt + invoice_ship.getINVFRAMT().doubleValue();
                                                      Loggers.general().info(LOG,"friAmtAmount ----> " + friAmt);
                                                      String friccy = invoice_ship.getINVFRAMTCurrency();
                                                      
                                                      comm = comm + invoice_ship.getICOMMAMT().doubleValue();
                                                    Loggers.general().info(LOG,"commision amount ----> " + comm);
                                                    String commccy = invoice_ship.getICOMMAMTCurrency();
                                                
                                                      pack = pack + invoice_ship.getIPKGAMT().doubleValue();
                                                      Loggers.general().info(LOG,"Paking credit ----> " +    pack);
                                                      String packccy = invoice_ship.getIPKGAMTCurrency();

                                                      discount = discount + invoice_ship.getIDISCAMT().doubleValue();
                                                    Loggers.general().info(LOG,"Discount amount ----> " + discount);
                                                      dedut = dedut + invoice_ship.getIDEDUAMT().doubleValue();
                                                      Loggers.general().info(LOG,"Deduction amount ----> " +      dedut);

                                                      
                                                      discal = discount + dedut;
                                                      Loggers.general().info(LOG,"deduction and discount discal----> " + discal);
                                                      
                                    try {
                                                con = getConnection();
                                                String query = "SELECT ETT_SPOTRATE_CAL('" + fobcur + "','" + insccy + "') FROM DUAL";
                                                Loggers.general().info(LOG,"insurance amount " + query);

                                                ps1 = con.prepareStatement(query);
                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      inscuramt = rs1.getDouble(1);
                                                      inscuramtBig = new BigDecimal(inscuramt);                               
                                                    insconamt =new BigDecimal(insAmt);
                                                      insconamt=inscuramtBig.multiply(insconamt);
                                                      
                                                      }
                                                      
                                                 String query1 = "SELECT ETT_SPOTRATE_CAL('" + fobcur + "','" + friccy + "') FROM DUAL";
                                                 Loggers.general().info(LOG,"freight amount " + query1);

                                                ps1 = con.prepareStatement(query1);
                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      fricuramt = rs1.getDouble(1);
                                                      fricuramtBig = new BigDecimal(fricuramt);                               
                                                    friconamt =new BigDecimal(friAmt);
                                                      friconamt=fricuramtBig.multiply(friconamt);
                                                      
                                                      }
                                                      
                                                 String query2 = "SELECT ETT_SPOTRATE_CAL('" + fobcur + "','" + commccy + "') FROM DUAL";
                                                 Loggers.general().info(LOG,"commission amount " + query2);

                                                ps1 = con.prepareStatement(query2);
                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      commcuramt = rs1.getDouble(1);
                                                      commcuramtBig = new BigDecimal(commcuramt);                                   
                                                    commconamt =new BigDecimal(comm);
                                                      commconamt=commcuramtBig.multiply(commconamt);
                                                      
                                                      }
                                                      
                                                 String query3 = "SELECT ETT_SPOTRATE_CAL('" + fobcur + "','" + packccy + "') FROM DUAL";
                                                 Loggers.general().info(LOG,"package amount " + query3);

                                                ps1 = con.prepareStatement(query3);
                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      packcuramt = rs1.getDouble(1);
                                                      packcuramtBig = new BigDecimal(packcuramt);                                   
                                                    packconamt =new BigDecimal(pack);
                                                      packconamt=packcuramtBig.multiply(packconamt);
                                                      
                                                      }
                                                      
                                                inscuramt=insconamt.doubleValue();
                                                fricuramt=friconamt.doubleValue();
                                                commcuramt=commconamt.doubleValue();
                                                packcuramt=packconamt.doubleValue();
                                                Loggers.general().info(LOG,"inscuramt"+inscuramt);
                                                Loggers.general().info(LOG,"fricuramt"+fricuramt);
                                                Loggers.general().info(LOG,"commcuramt"+commcuramt);
                                                Loggers.general().info(LOG,"packcuramt"+packcuramt);
                                    }
                                    catch(Exception e)
                                    {
                                          Loggers.general().info(LOG,"Exception in sum of fob"+e.getMessage());
                                    }
                                    finally{
                                          surrenderDB(con, ps1, rs1);
                                    }
                                    
                                    Double totalInvoiceAmt = fobAmt + inscuramt + fricuramt + commcuramt + packcuramt;
                                    Loggers.general().info(LOG,"totalInvoiceAmt"+totalInvoiceAmt);
                                    ConnectionMaster connectionMaster = new ConnectionMaster();
                                    double divideByDecimal = connectionMaster.getDecimalforCurrency(fobcur);
                                    Loggers.general().info(LOG,"divideByDecimal"+divideByDecimal);
                                    toFobamt_tot = totalInvoiceAmt / divideByDecimal;
                                    
                                    Loggers.general().info(LOG,"toFobamt_tot"+toFobamt_tot);
                                          
                                    
                                    toFobamt = (toFobamt_tot - (discal/ divideByDecimal));
                                    Loggers.general().info(LOG,"toFobamt"+toFobamt);            
                                    
                                          
                                    

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"Total Fobamt for validation===> " + toFobamt);
                                                      }
                                                }

                                                if (invoice_shiplc.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                                            && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                            && getMinorCode().equalsIgnoreCase("CRE")) {

                                                      // //Loggers.general().info(LOG,"size of grid inside
                                                      // if
                                                      // loop
                                                      // "
                                                      // + invoice_shiplc.size());
                                                      validationDetails.addError(ErrorType.Other, "Invoice details is mandatory [CM]");
                                                } else {
                                                      // Loggers.general().info(LOG,"toFobamt for compare
                                                      // ----> "
                                                      // + toFobamt);
                                                      // Loggers.general().info(LOG,"shipping bill amount
                                                      // for
                                                      // compare ----> " + am);
                                                      // String finval = String.format("%.2f",
                                                      // am);
                                                /*    DecimalFormat df = new DecimalFormat("#.##");
                                                      double finvalue = Double.valueOf(df.format(am));*/
                                                      Loggers.general().info(LOG,"am==="+am);
                                                      String finval = String.format("%.2f", am);
                                                      double finvalue = Double.valueOf(finval);
                                                      Loggers.general().info(LOG,"finvalue"+finvalue);
                                                      
                                                      String totval = String.format("%.2f", toFobamt);
                                                      double totvalue = Double.valueOf(totval);
                                                      
                                                      
                                                      Loggers.general().info(LOG,"toFobamt"+toFobamt);
                                                      Loggers.general().info(LOG,"totvalue"+totvalue);
                  

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"Sum of FOB amount minus (Discount + deduction amt)("
                                                                        + totvalue + ") should be equal to shipping bill amount (" + finvalue
                                                                        + ")");
                                                      }
                                                      if (totvalue > 0 && totvalue != finvalue && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                              || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase("CRE")) {
                                                            validationDetails.addWarning(WarningType.Other,
                                                                        "Sum of FOB + freight+ insurance+commission+package amount minus (Discount + deduction amt)("
                                                                                    + totvalue + ") should be equal to shipping bill amount ("
                                                                                    + finvalue + ") [CM]");

                                                      } else {

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"Sum of FOB amount minus (Discount + deduction amt)("
                                                                              + totvalue + ") should be equal to shipping bill amount else part("
                                                                              + finvalue + ")");
                                                            }

                                                      }
                                                }
                                          } catch (Exception e) {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Exception in Sum of FOB amount" + e.getMessage());
                                                }
                                          }
                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Shipping validation error " +
                                    // e.getMessage());
                              }

                              try {

                                    List<ExtEventShippingCollections> shiplcd = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();
                                    Loggers.general().debug(LOG, "###shiplcd..");
                                    int counterv = 0;
                                    String rep1 = "";
                                    for (int l = 0; l < shiplcd.size(); l++) {

                                          ExtEventShippingCollections shipinglc = shiplcd.get(l);

                                          String billnoo = shipinglc.getCBILLNUM();

                                          String formNO = shipinglc.getCFORMN();
                                          String colbillno = shipinglc.getCBILLNUM();
                                          String port = shipinglc.getCPORTCO();
                                          String dateresult = "";
                                          String billdate = shipinglc.getCBILLDA().toString();
                                          // //Loggers.general().info(LOG,"Shipping bill date for
                                          // checking
                                          // valid" + billdate);

                                          SimpleDateFormat parseFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                                          SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");

                                          if (billdate.length() > 0 && billdate != null || !billdate.equalsIgnoreCase("")) {
                                                if (billdate.length() > 10) {
                                                      dateresult = formatter1.format(parseFormat1.parse(billdate));
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"Shipping Bill date after result1 if----->" + dateresult);
                                                      }
                                                } else {
                                                      dateresult = formatter1.format(parseFormat.parse(billdate));
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"Shipping Bill date after result1 else----->" + dateresult);
                                                      }

                                                }
                                          }

                                          int count2 = 0;
                                          String query2 = "select count(*) from ett_edpms_shp where SHIPBILLNO ='" + colbillno + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Invalide shipping bill no Query2---------->" + query2);
                                          }
                                          con = ConnectionMaster.getConnection();
                                          ps = con.prepareStatement(query2);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                count2 = rs.getInt(1);
                                                // //Loggers.general().info(LOG,"Count value for
                                                // shipping
                                                // bill
                                                // dup for collection---------->" + count2);
                                          }

                                          // Port,bill date,bill no combination validation
                                          int count4 = 0;
                                          String query4 = "SELECT count(*) FROM ett_edpms_shp WHERE shipbillno ='" + billnoo
                                                      + "' AND SHIPBILLDATE = TO_CHAR(to_date('" + dateresult
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE ='" + port + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Port,bill date,bill no combination validation====>" + query4);
                                          }
                                          //con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query4);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count4 = rs1.getInt(1);

                                          }

                                          int count3 = 0;
                                          String query3 = "select count(*) from ett_edpms_shp_softex where FORMNO ='" + formNO + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"softax query3---------->" + query3);
                                          }
                                          Connection con1 = ConnectionMaster.getConnection();
                                          PreparedStatement ps1 = con1.prepareStatement(query3);
                                          ResultSet rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count3 = rs1.getInt(1);
                                                // //Loggers.general().info(LOG,"Count value for softax
                                                // Collection bill dup---------->" + count2);
                                          }

                                          // Port,bill date,form no combination validation
                                          int count5 = 0;
                                          String query5 = "SELECT count(*) FROM ett_edpms_shp_inv_softex A WHERE FORMNO ='" + formNO
                                                      + "' AND SHIPBILLDATE = TO_CHAR(to_date('" + dateresult
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE ='" + port + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Port,bill date,form no combination validation====>" + query5);
                                          }
                                    //    con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query5);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count5 = rs1.getInt(1);

                                          }
                                          String formType=getPane().getFORTYP().trim();
                                          Loggers.general().info(LOG,"Form type===>"+formType);

                                          if (count2 == 0 && (step_Input.equalsIgnoreCase("i")) && (!colbillno.equalsIgnoreCase(""))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")&&!formType.equalsIgnoreCase("EDF")) {

                                                // validationDetails.addWarning(WarningType.Other,
                                                // "Invalid shipping bill no ("+colbillno+")
                                                // [CM]");
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Shipping bill no (" + billnoo + ") not found in MDF file  [CM]");
                                          }

                                          if (count3 == 0 && (!formNO.equalsIgnoreCase("")&&billnoo.equalsIgnoreCase("")) && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Shipping Form no (" + formNO + ") not found in MDF file  [CM]");

                                          }

                                          // Port,bill date,bill no combination validation

                                          if ((count4 < 1 || count4 == 0) && (!billnoo.equalsIgnoreCase(""))
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")&&!formType.equalsIgnoreCase("EDF")) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Kindly enter valid Shipping bill no,bill date,port code  [CM]");

                                          } else {
                                                // Loggers.general().info(LOG,"valid Bill no---------->"
                                                // +
                                                // count4);
                                          }

                                          // Port,bill date,form no combination validation

                                          if ((count5 < 1 || count5 == 0) && (!formNO.equalsIgnoreCase("")&&billnoo.equalsIgnoreCase(""))
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Kindly enter valid Form no,bill date,port code  [CM]");

                                          } else {
                                                // Loggers.general().info(LOG,"valid Form no---------->"
                                                // +
                                                // count5);
                                          }

                                    }
                              } catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Valid bill no and form no exception" + e.getMessage());
                                    }
                              } finally {
                                    try {
                                          if (rs1 != null)
                                                rs1.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();
                                          if (con1 != null)
                                                con1.close();
                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }

                              try {
                                    List<ExtEventShippingCollections> shipcol = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();
                                    Loggers.general().debug(LOG, "###shipcol..");
                                    for (int j = 0; j < shipcol.size(); j++) {
                                          ExtEventShippingCollections type = shipcol.get(j);

                                          if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")
                                                      && prd_typ.equalsIgnoreCase("OCF")) {
                                                BigDecimal shipAmt = new BigDecimal("0");
                                                BigDecimal notVal = new BigDecimal("1");
                                                type.setCREPAY(shipAmt);
                                                type.setCREPAYCurrency(type.getCSHPAMTCurrency());
                                                type.setCSHCOLAM(shipAmt);
                                                type.setCSHCOLAMCurrency(type.getCSHPAMTCurrency());
                                                type.setCNOTIONL(notVal);
                                          }

                                          double shpamt = type.getCSHPAMT().doubleValue();
                                          String shpamtccy = type.getCSHPAMTCurrency();

                                          if (shpamt < 0 && getMinorCode().equalsIgnoreCase("CRE")
                                                      && getMajorCode().equalsIgnoreCase("ODC") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Shipping bill amount should not be negative [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"New shipment for not
                                                // negative---->" + shpamt);
                                          }

                                    }
                              }

                              catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception New shipment for not
                                    // negative" + e.getMessage());
                              }

                              // shipping bill validation
                              try {
                                    // Loggers.general().info(LOG,"Shipping validation Started");
                                    List<ExtEventShippingCollections> shiplcd = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();
                                    Loggers.general().debug(LOG, "###shiplcd1..");
                                    int countv = 0;
                                    int countv1 = 0;
                                    int countelc = 0;
                                    int countv1elc = 0;
                                    String formNo = "";
                                    String shipBilNo = "";
                                    String shipBilldate = "";
                                    String portCode = "";
                                    for (int l = 0; l < shiplcd.size(); l++) {
                                          
                                          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                                          ExtEventShippingCollections shipinglc = shiplcd.get(l);
                                          shipBilNo = shipinglc.getCBILLNUM().trim();
                                          shipBilldate = format.format(shipinglc.getCBILLDA());
                                          portCode = shipinglc.getCPORTCO();
                                          // Loggers.general().info(LOG,"Shpping Bill date " +
                                          // shipBilldate + "and " + portCode);
                                          formNo = shipinglc.getCFORMN();
                                          con = ConnectionMaster.getConnection();
                                          String query = "select count(*) from  exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield and (bev.STATUS = 'c' or (mas.STATUS  = 'LIV' and bev.STATUS = 'i')) and shc.CBILLNUM='"
                                                      + shipBilNo + "' AND  TO_CHAR(shc.CBILLDA,'DD/MM/YY')='" + shipBilldate
                                                      + "' and shc.CPORTCO='" + portCode + "'";
                                          try{
                                                if(ps1 !=null)
                                                      ps1.close();
                                                if(rs1 !=null)
                                                      rs1.close();
                                          }catch(Exception e){
                                                Loggers.general().info(LOG,"close shp------>"+e.getMessage());
                                          }

                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                countv = rs1.getInt(1);
                                          }
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Shpping bill no query odc" + query + "countv===" + countv);
                                          }
                                          String query1 = "select count(*) from  exteventshc shc, baseevent bev,master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield and (bev.STATUS = 'c' or (mas.STATUS  = 'LIV' and bev.STATUS = 'i')) and shc.CFORMN='"
                                                      + formNo + "' AND  TO_CHAR(shc.CBILLDA,'DD/MM/YY')='" + shipBilldate
                                                      + "' and shc.CPORTCO='" + portCode + "' ";
                                          try{
                                                if(ps1 !=null)
                                                      ps1.close();
                                                if(rs1 !=null)
                                                      rs1.close();
                                          }catch(Exception e){
                                                Loggers.general().info(LOG,"close shp------>"+e.getMessage());
                                          }
                                          //con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query1);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //Loggers.general().info(LOG,"Entered while");
                                                countv1 = rs1.getInt(1);
                                                // //Loggers.general().info(LOG,"value of count in while
                                                // " +
                                                // countv);
                                          }
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Shpping form no query odc" + query1 + "countv1===" + countv1);
                                          }
                                          String query2 = "select count(*) from  exteventsht shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield and (bev.STATUS = 'c' or (mas.STATUS  = 'LIV' and bev.STATUS = 'i')) and shc.BILLNUM='"
                                                      + shipBilNo + "' AND  TO_CHAR(shc.BILLDAT,'DD/MM/YY')='" + shipBilldate
                                                      + "' and shc.PORTCODD='" + portCode + "' ";
                                          //con = ConnectionMaster.getConnection();
                                          try{
                                                if(ps1 !=null)
                                                      ps1.close();
                                                if(rs1 !=null)
                                                      rs1.close();
                                          }catch(Exception e){
                                                Loggers.general().info(LOG,"close shp------>"+e.getMessage());
                                          }
                                          ps1 = con.prepareStatement(query2);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //Loggers.general().info(LOG,"Entered while");
                                                countelc = rs1.getInt(1);

                                          }
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"ODC Shipping bill no is used in ELC---->" + countelc); //

                                          }

                                          String query3 = "select count(*) from  exteventsht shc, baseevent bev,  master mas where  mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield and (bev.STATUS = 'c' or (mas.STATUS  = 'LIV' and bev.STATUS = 'i')) and shc.FORMNUM='"
                                                      + formNo + "' and TO_CHAR(shc.BILLDAT,'DD/MM/YY')='" + shipBilldate
                                                      + "' and shc.PORTCODD='" + portCode + "' ";
                                          // //Loggers.general().info(LOG,"Shipping bill Query
                                          // authorised" +
                                          // query);
                                          //con = ConnectionMaster.getConnection();
                                          try{
                                                if(ps1 !=null)
                                                      ps1.close();
                                                if(rs1 !=null)
                                                      rs1.close();
                                          }catch(Exception e){
                                                Loggers.general().info(LOG,"close shp------>"+e.getMessage());
                                          }
                                          ps1 = con.prepareStatement(query3);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                countv1elc = rs1.getInt(1);

                                          }
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Shpping form no query elc" + query3 + "countv1 elc===" + countv1elc);
                                          }
                                          if (countv > 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Entered shipping bill number already used (" + shipBilNo + ") [CM]");
                                          }

                                          if (countv1 > 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Entered shipping Form number already used (" + formNo + ") [CM]");
                                          }

                                          if (countelc > 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Entered shipping bill number already used (" + shipBilNo + ") [CM]");
                                          }

                                          if (countv1elc > 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("CRE")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Entered shipping Form number already used (" + formNo + ") [CM]");
                                          }

                                    }

                              } catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception in Shipping validations" + e.getMessage());
                                    }
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
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }

                        } else

                        {
                              EventPane pane = (EventPane) getPane();
                              pane.getBtnFetchShipDetEXPBILLclay().setEnabled(false);
                              pane.getBtnFetchInvDetEXPBILLclay().setEnabled(false);
                              // //Loggers.general().info(LOG,"Product type is not a Local cur " +
                              // shippingCount + "Product is " + prd_typ);
                        }
                        // -------------------------------Shipping validation
                        // end--------------------------------

                        // //Loggers.general().info(LOG,"Shipping bill ELC Query======>");

                        if (step_Input.equalsIgnoreCase("i") && prd_typ.equalsIgnoreCase("OCF"))

                        {
                              try {
                                    List<ExtEventShippingCollections> shipTable = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();
                                    // //Loggers.general().info(LOG,"shipTable.size - " +
                                    // shipTable.size());
                                    Loggers.general().debug(LOG, "###shiptable..");
                                    for (int i = 0; i < shipTable.size(); i++) {

                                          ExtEventShippingCollections ship = shipTable.get(i);

                                          double notional = 1;

                                          notional = ship.getCNOTIONL().doubleValue();
                                          // double ship_Amount = 0.0;

                                          String ship_str = ship.getCSHPAMT().toString();
                                          BigDecimal shipamt_str = new BigDecimal(ship_str);
                                          // ship_Amount = ship.getCSHPAMT().doubleValue();
                                          // double shipamt = (notional * ship_Amount);
                                          BigDecimal notional_big = new BigDecimal(notional);
                                          BigDecimal equi_bill = notional_big.multiply(shipamt_str);

                                          ship.setCEQUBILL(equi_bill);
                                          ship.setCEQUBILLCurrency(ship.getCSHPAMTCurrency());
                                          ship.setCOUTSAMT(shipamt_str);
                                          ship.setCOUTSAMTCurrency(ship.getCSHPAMTCurrency());
                                    }

                              } catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception in validating for notional rate - " + e.getMessage());
                                    }
                              }
                        }
                        if (step_Input.equalsIgnoreCase("i") && prd_typ.equalsIgnoreCase("MBI"))

                        {
                              try {
                                    List<ExtEventShippingCollections> shipTable = (List<ExtEventShippingCollections>) getWrapper()
                                                .getExtEventShippingCollections();
                                    // //Loggers.general().info(LOG,"shipTable.size - " +
                                    // shipTable.size());
                                    Loggers.general().debug(LOG, "###shiptable2..");
                                    BigDecimal shortTotalAmt = new BigDecimal("0");

                                    BigDecimal shortTotalAmount = new BigDecimal("0");
                                    for (int i = 0; i < shipTable.size(); i++) {

                                          ExtEventShippingCollections ship = shipTable.get(i);

                                          double notional = 1;

                                          notional = ship.getCNOTIONL().doubleValue();
                                          // double ship_Amount = 0.0;
                                          String not_str = ship.getCNOTIONL().toString();
                                          String ship_str = ship.getCSHPAMT().toString();
                                          String shiamtcuy = ship.getCSHPAMTCurrency();
                                          BigDecimal shipamt_str = new BigDecimal(ship_str);
                                          BigDecimal rAmt = ship.getCREPAY();
                                          String reyamtcuy = ship.getCREPAYCurrency();

                                          try {
                                                con = getConnection();
                                                String query = "SELECT ETT_SPOTRATE_CAL('" + shiamtcuy + "','" + reyamtcuy
                                                            + "') FROM DUAL";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Notional rate function " + query);

                                                }
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
                                                      //// Loggers.general().info(LOG,"shipping table
                                                      //// notional
                                                      //// length if loop====>" +
                                                      //// ship.getCNOTIONL());
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,
                                                                        "shipping table notional length if loop====>" + ship.getCNOTIONL());

                                                      }

                                                      BigDecimal notional_big = new BigDecimal(notional);
                                                      BigDecimal equi_bill = notional_big.multiply(rAmt);

                                                      ship.setCEQUBILL(equi_bill);
                                                      ship.setCEQUBILLCurrency(ship.getCSHPAMTCurrency());
                                                      ship.setCOUTSAMT(shipamt_str);
                                                      ship.setCOUTSAMTCurrency(ship.getCSHPAMTCurrency());

                                                } else if (null != not_str && !temp_notRate.equalsIgnoreCase(not_str)) {
                                                      ship.setCNOTIONL(new BigDecimal(not_str));
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"shipping table notional length else if loop====>"
                                                                        + ship.getCNOTIONL());

                                                      }
                                                      BigDecimal notional_big = new BigDecimal(not_str);
                                                      BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                      // Loggers.general().info(LOG,"Notional rate +
                                                      // Repament
                                                      // amount in big decimal POD---->" +
                                                      // equi_bill);

                                                      ship.setCEQUBILL(equi_bill);
                                                      ship.setCEQUBILLCurrency(ship.getCSHPAMTCurrency());
                                                      ship.setCOUTSAMT(shipamt_str);
                                                      ship.setCOUTSAMTCurrency(ship.getCSHPAMTCurrency());

                                                } else {
                                                      // Loggers.general().info(LOG,"Notional rate is
                                                      // blank---->");
                                                }

                                          } catch (Exception e) {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Exception in validating for notional rate" + e.getMessage());

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
                                                      // Loggers.general().info(LOG,"Connection Failed!
                                                      // Check
                                                      // output console");
                                                      e.printStackTrace();
                                                }
                                          }

                                          try {

                                                String shortCol = ship.getCSHCOLAM().toString();
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Short collection amount in MBIL" + shortCol);
                                                }
                                                String shortCur = ship.getCSHCOLAMCurrency();
                                                BigDecimal shortCollection = new BigDecimal(shortCol);
                                                ConnectionMaster connectionMaster = new ConnectionMaster();
                                                double divideByDecimal = connectionMaster.getDecimalforCurrency(shortCur);
                                                BigDecimal shortBigdeci = new BigDecimal(divideByDecimal);
                                                BigDecimal shortTotal = shortCollection.divide(shortBigdeci);

                                                shortTotalAmt = shortTotalAmt.add(shortTotal);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Short collection amount in MBIL after divided" + shortCol);
                                                      Loggers.general().info(LOG,"Total Short collection amount in MBIL" + shortTotalAmt);
                                                }

                                          } catch (Exception e) {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Exception Short collection amount in MBIL" + e.getMessage());
                                                }
                                          }

                                    }

                                    try {

                                          String colAmount = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                                          // String colAmountCur =
                                          // getDriverWrapper().getEventFieldAsText("AMT",
                                          // "v", "c");
                                          // ConnectionMaster connectionMaster = new
                                          // ConnectionMaster();
                                          // double divideByCol =
                                          // connectionMaster.getDecimalforCurrency(colAmountCur);
                                          // BigDecimal divideByCollection = new
                                          // BigDecimal(divideByCol);
                                          BigDecimal colAmountVal = new BigDecimal(colAmount);
                                          // BigDecimal colAmountValue =
                                          // colAmountVal.divide(divideByCollection);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Collection amount in MBIL initial" + colAmountVal);
                                          }

                                          String recAmount = getDriverWrapper().getEventFieldAsText("cARK", "v", "m");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Advanced amount in MBIL initial" + recAmount);
                                          }
                                          // String recCur =
                                          // getDriverWrapper().getEventFieldAsText("cARK",
                                          // "v", "c");
                                          // BigDecimal recTotalAmt = new
                                          // BigDecimal(recAmount);
                                          // double divideBy =
                                          // connectionMaster.getDecimalforCurrency(recCur);
                                          BigDecimal receivedAmount = new BigDecimal(recAmount);
                                          // BigDecimal recTotalAmount =
                                          // recTotalAmt.divide(receivedAmount);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Advanced amount in MBIL" + receivedAmount);
                                          }
                                          BigDecimal totalVal = shortTotalAmt.add(receivedAmount);

                                          shortTotalAmount = totalVal.setScale(2, RoundingMode.HALF_UP);
                                          BigDecimal totalValue = colAmountVal.setScale(2, RoundingMode.HALF_UP);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Total Short collection and advance amount MBIL" + totalVal);
                                                Loggers.general().info(LOG,"Total Short collection and advance amount in MBIL in comparition"
                                                            + shortTotalAmount);
                                                Loggers.general().info(LOG,"Collection amount in MBIL in comparition" + totalValue);
                                          }

                                          if (shortTotalAmount.compareTo(BigDecimal.ZERO) > 0
                                                      && totalValue.compareTo(BigDecimal.ZERO) > 0
                                                      && (shortTotalAmount.compareTo(totalValue) != 0)
                                                      && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Advance Received selected is Full advance,Advance Received Amount + short collection amount should be same with Collection amount [CM]");
                                          } else {

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Collection amount in MBIL in comparision in else" + totalValue
                                                                  + "" + shortTotalAmount);
                                                }

                                          }

                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Exception Short collection and Collection amount in MBIL" + e.getMessage());
                                          }
                                    }

                              } catch (Exception e) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"Exception in Equalent and Outstanding amount" + e.getMessage());

                                    }
                              }
                        }

                        // Shipping bill need to delete
                        if (step_csm.equalsIgnoreCase("CBS Maker 1") && getMajorCode().equalsIgnoreCase("ODC")
                                    && getMinorCode().equalsIgnoreCase("CRE")) {
                              try {

                                    List<ExtEventInvoiceDetails> invoiceDet = (List<ExtEventInvoiceDetails>) getWrapper()

                                                .getExtEventInvoiceDetails();
                                    for (int l = 0; l < invoiceDet.size(); l++) {

                                          ExtEventInvoiceDetails invoice_ship = invoiceDet.get(l);
                                          try {

                                                String invShipBill = invoice_ship.getISHPBILL().trim();

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Invoice no shipping bill" + invShipBill);

                                                }
                                                if ((invShipBill != null && !invShipBill.equalsIgnoreCase(""))
                                                            && invShipBill.length() > 0) {
                                                      int count = 0;
                                                      String query = "SELECT count(*),mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL,shc.CBILLNUM FROM exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield and shc.CBILLNUM='"
                                                                  + invShipBill
                                                                  + "' GROUP BY mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL,shc.CBILLNUM ";

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"Invoice no shipping bill query" + query);

                                                      }
                                                      con = ConnectionMaster.getConnection();
                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            // //Loggers.general().info(LOG,"Entered
                                                            // while");
                                                            count = rs1.getInt(1);

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"Invoice bill count if loop" + count);

                                                            }
                                                      }

                                                      else if ((step_Input.equalsIgnoreCase("i")) && !prd_typ.equalsIgnoreCase("OCI")
                                                                  && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                                            Loggers.general().info(LOG,"Invoice bill count else if loop" + count);
                                                            validationDetails.addError(ErrorType.Other,
                                                                        "Please delete the Invoice number row (" + invShipBill
                                                                                    + ") which is not available in 1st Shipping grid [CM]");
                                                      } else {

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"Shipping bill for comparation invShipBill else===>"
                                                                              + invShipBill + "And count" + count);

                                                            }

                                                      }
                                                } else {

                                                }
                                          } catch (Exception e) {

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Exception in Invoice number" + e.getMessage());

                                                }
                                          }
                                          try {
                                                String invFormBill = invoice_ship.getIFORNO().trim();

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Invoice Form number" + invFormBill);

                                                }
                                                if ((invFormBill != null && !invFormBill.equalsIgnoreCase(""))
                                                            && invFormBill.length() > 0) {
                                                      int count = 0;
                                                      String query = "SELECT count(*),mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL,shc.CFORMN FROM exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield and shc.CFORMN='"
                                                                  + invFormBill
                                                                  + "' GROUP BY mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL,shc.CFORMN";
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"Invoice Form number query" + query);

                                                      }
                                                      con = ConnectionMaster.getConnection();
                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            // //Loggers.general().info(LOG,"Entered
                                                            // while");
                                                            count = rs1.getInt(1);

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,"Invoice form number count if loop" + count);

                                                            }
                                                      }

                                                      else if ((step_Input.equalsIgnoreCase("i")) && !prd_typ.equalsIgnoreCase("OCI")
                                                                  && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                                            validationDetails.addError(ErrorType.Other,
                                                                        "Please delete the Invoice form number row (" + invFormBill
                                                                                    + ") which is not available in 1st Shipping grid [CM]");
                                                      } else {

                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                                  Loggers.general().info(LOG,
                                                                              "Shipping bill for comparation Invoice form number else===>"
                                                                                          + invFormBill + "And count" + count);

                                                            }

                                                      }
                                                } else {

                                                }
                                          } catch (Exception e) {

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"Exception in Invoice Form number" + e.getMessage());

                                                }
                                          }

                                    }

                              } catch (Exception e) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"Exception in Invoice details" + e.getMessage());

                                    }
                              }

                              // Shipping bill need to delete

                              try {
                                    List<ExtEventShippingdetails> shipDet = (List<ExtEventShippingdetails>) getWrapper()
                                                .getExtEventShippingdetails();
                                    for (int l = 0; l < shipDet.size(); l++) {

                                          ExtEventShippingdetails invoice_ship = shipDet.get(l);
                                          try {

                                                String invShipBill = invoice_ship.getSDBILLNO().trim();

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG,"2nd shipping bill" + invShipBill);

                                                }
                                                if ((invShipBill != null && !invShipBill.equalsIgnoreCase(""))
                                                            && invShipBill.length() > 0) {
                                                      int count = 0;
                                                      String query = "SELECT count(*),mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL,shc.CBILLNUM FROM exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield and shc.CBILLNUM='"
                                                                  + invShipBill
                                                                  + "' GROUP BY mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL,shc.CBILLNUM ";

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                            Loggers.general().info(LOG,"2nd shipping bill query" + query);

                                                      }
                                                      con = ConnectionMaster.getConnection();
                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            // //Loggers.general().info(LOG,"Entered
                                                            // while");
                                                            count = rs1.getInt(1);

                                                      }

                                                      else if ((step_Input.equalsIgnoreCase("i")) && !prd_typ.equalsIgnoreCase("OCI")
                                                                  && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {
    validationDetails.addError(ErrorType.Other,
        "Please delete the Shipping bill number row (" + invShipBill
                    + ") which is not available in 1st Shipping grid [CM]");
} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {

  Loggers.general().info(LOG,"Shipping bill for comparation invShipBill else===>"
              + invShipBill + "And count" + count);

}

}
} else {

}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,"Exception in 2nd shipping bill" + e.getMessage());

}
}
try {
String invFrom = invoice_ship.getSDFORMNO().trim();

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,"2nd shipping form number" + invFrom);

}
if ((invFrom != null && !invFrom.equalsIgnoreCase("")) && invFrom.length() > 0) {
int count = 0;
String query = "SELECT count(*),mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL,shc.CFORMN FROM exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield and shc.CFORMN='"
  + invFrom
  + "' GROUP BY mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL,shc.CFORMN ";

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,"2nd shipping form number query" + query);

}
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
if (rs1.next()) {
// //Loggers.general().info(LOG,"Entered
// while");
count = rs1.getInt(1);

if (dailyval_Log.equalsIgnoreCase("YES")) {

  Loggers.general().info(LOG,"2nd Shipping from number count if loop" + count);

}
}

else if ((step_Input.equalsIgnoreCase("i")) && !prd_typ.equalsIgnoreCase("OCI")
  && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addError(ErrorType.Other,
        "Please delete the 2nd Shipping from number row (" + invFrom
                    + ") which is not available in 1st Shipping grid [CM]");
} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {

  Loggers.general().info(LOG,
              "2nd Shipping from number for comparation 2nd Shipping from else===>"
                          + invFrom + "And count" + count);

}

}
} else {

}
} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,"Exception 2nd shipping form number" + e.getMessage());

}
}
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,"Exception in Shipping details" + e.getMessage());

}
}
}

if (getMajorCode().equalsIgnoreCase("ODC")) {
try {
//getnetAmount();
Loggers.general().info(LOG,"Odc  create==>");
getnetAmountOdcPay();
} catch (Exception e) {

}
}

if (prd_typ.equalsIgnoreCase("OCF") || prd_typ.equalsIgnoreCase("MBI")) {
try {
List<ExtEventShippingCollections> shipTable = (List<ExtEventShippingCollections>) getWrapper()
.getExtEventShippingCollections();
// //Loggers.general().info(LOG,"shipTable.size - " +
// shipTable.size());
Loggers.general().debug(LOG, "###shiotable4..");
for (int i = 0; i < shipTable.size(); i++) {

ExtEventShippingCollections ship = shipTable.get(i);

double notional = 1;

notional = ship.getCNOTIONL().doubleValue();
// double ship_Amount = 0.0;

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,"Notional rate value---->" + notional);
}
if ((notional == 0.0 || notional == 0 || notional < 1) && shipTable.size() > 0
&& (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker")
  || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(WarningType.Other,
"Notional rate should not be '0' in shipping details grid [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,"Notional rate value in else---->" + notional);
}
}

}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in validating for
// notional
// rate - " + e.getMessage());
}
}

try {
String finaceCreate = getDriverWrapper().getEventFieldAsText("BFC", "l", "");
if (finaceCreate.equalsIgnoreCase("Y") && !finaceCreate.equalsIgnoreCase("N")) {
String facility = getWrapper().getLIMITID().trim();

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"ODC finance limit id validation===>" + facility);
Loggers.general().info(LOG,"ODC LOB code finance created validation=======>" + finaceCreate);
}

/*if (finaceCreate.equalsIgnoreCase("Y") && (facility == null || facility.equalsIgnoreCase(""))
&& (step_csm.equalsIgnoreCase("CBS Maker")
|| step_csm.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(ValidationDetails.WarningType.Other,
"Please input the LOB limit id which is attached in credit facility [CM]");
}*/
if (facility != null && !facility.equalsIgnoreCase("")) {
try {
try {
int count = 0;
String query = "select count(*) from XMLAPISTO where FACILITYID='" + facility + "'";
con = getConnection();
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
if (rs1.next()) {
count = rs1.getInt(1);

}
/*
if ((count == 0 || count < 1) && (step_csm.equalsIgnoreCase("CBS Authoriser")))
   {
validationDetails.addWarning(ValidationDetails.WarningType.Other,
        "Please enter valid LOB limit id [CM]");
}
*/
} catch (Exception e) {
Loggers.general().info(LOG,"Exception ODC finance limit id validation===>" + e.getMessage());
}

try {
String lobCreate = getWrapper().getLOB().trim();
String lobfinance = "";
String query = "SELECT exte.LOB FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT AND trim(exte.LOB) IS NOT NULL AND mas.MASTER_REF = '"
  + MasterReference + "'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Attched finance LOB code===>" + query);
}
con = getConnection();
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
if (rs1.next()) {
lobfinance = rs1.getString(1).trim();

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Attched finance LOB code===>" + lobfinance + "lobCreate==>"
        + lobCreate);
}


} catch (Exception e) {
Loggers.general().info(LOG,"Exception ODC authorized validation===>" + e.getMessage());
}

} catch (Exception e) {
Loggers.general().info(LOG,"Exception in connection closed===>" + e.getMessage());
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
// Check output
// console");
e.printStackTrace();
}
}
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"ODC finance limit id blank===>");
}
}

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"ODC finance not created===>" + finaceCreate);
}
}

} catch (Exception e) {
Loggers.general().info(LOG,"Exception ODC finance validation===>" + e.getMessage());
}

// port code population
String portval = getWrapper().getPORTCOD_Name().trim();
// //Loggers.general().info(LOG,"Port Value---->" + portval);
if ((!portval.equalsIgnoreCase("")) && portval != null)

{
try {
// //Loggers.general().info(LOG,"hscode Value in try---->" +
// hscodeval);
String hyperValue = "SELECT trim(PNAME),trim(COUNTRY) FROM EXTPORTCO WHERE PCODE='" + portval
+ "'";
// //Loggers.general().info(LOG,"port code query Value---->" +
// hyperValue);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(hyperValue);
rs = ps1.executeQuery();
while (rs.next()) {
String hsploy = rs.getString(1);
String poname = rs.getString(2);
// //Loggers.general().info(LOG,"port code description---->"
// +
// hsploy);
getPane().setPORTDESC(hsploy);
getPane().setPORTDECO(poname);
}

} catch (Exception e) {
// Loggers.general().info(LOG,"exception is " + e);
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
} else

{
// Loggers.general().info(LOG,"port code is empty");
}

String stepid = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
// HS Code Validation
try

{
String HScode = getWrapper().getHMON().trim();
// //Loggers.general().info(LOG,"step is information" + stepid);
if ((!HScode.trim().equalsIgnoreCase("")) || HScode != null && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker")) && HScode.length() > 0) {
String qr = "select count(*) from exthmcode where hcodee='" + HScode + "'";
// //Loggers.general().info(LOG,"Query " + qr);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(qr);
// //Loggers.general().info(LOG,qr);
rs = ps1.executeQuery();
while (rs.next()) {
String k = rs.getString(1);
// //Loggers.general().info(LOG,"while of count of hscode "
// +k);
int ka = Integer.parseInt(k);
//if (ka == 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker")
//|| step_csm.equalsIgnoreCase("CBS Maker 1"))) {
//// //Loggers.general().info(LOG,"warning of hs code ");
//validationDetails.addWarning(ValidationDetails.WarningType.Other,
//"HS Code is Invalid (" + HScode + ")" + "[CM]");
//
//}
}

} else {
// Loggers.general().info(LOG,"HS code");
}
} catch (

Exception e)

{
// //Loggers.general().info(LOG,"exception caught " +e.getMessage()
// );
}

finally

{
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

// Shipment value toUpperCase();

// Shipment value toUpperCase();

String shipfrom = getWrapper().getSHIPFTO_Name();
String shipto = getWrapper().getSHIPTOP_Name();
try

{
String fromVal = shipfrom.toUpperCase();
String toval = shipto.toUpperCase();
if (shipfrom.length() > 0 || shipto.length() > 0) {
getPane().setSHIPFTO_Name(fromVal);
getPane().setSHIPTOP_Name(toval);
}
} catch (

Exception e)

{
// //Loggers.general().info(LOG,"Shipment value --->" +
// e.getMessage());
}

// IECODE Validation

try {
// //Loggers.general().info(LOG,"IE code validation ");
List<ExtEventShippingdetails> shpcol5 = (List<ExtEventShippingdetails>) getWrapper()
.getExtEventShippingdetails();

if (shpcol5.size() > 0) {
int count = 0;
for (int l = 0; l < shpcol5.size(); l++) {
ExtEventShippingdetails shipCol = shpcol5.get(l);

String recind = shipCol.getCREIND().trim();
String shipbill = shipCol.getSDBILLNO();
// Loggers.general().info(LOG,"Record indicator is===>" +
// recind);

if (recind.equalsIgnoreCase("3") && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
"Shipping bill is already cancelled (" + shipbill + ")  [CM]");
} else {
// Loggers.general().info(LOG,"recind is not 3===>" +
// recind);
}

String ie = shipCol.getSDIECODE().trim();

if (getDriverWrapper().getEventFieldAsText("PRI", "p", "cBBF").trim()
.equalsIgnoreCase(ie)) {
// //Loggers.general().info(LOG,"count" + count);

} else {
count++;
// //Loggers.general().info(LOG,"else part of ie code
// validation");
}

}
if (count > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker")
|| step_csm.equalsIgnoreCase("CBS Maker 1"))) {
// validationDetails.addWarning(WarningType.Other,
// "IE
// code Mismatched [CM]");
}

}

} catch (Exception e)

{
// Loggers.general().info(LOG,"IE CODE exception " +
// e.getMessage());
}

// Account realization warning
try

{
List<ExtEventAccountRealisation> Acr = (List<ExtEventAccountRealisation>) getWrapper()
.getExtEventAccountRealisation();
if (Acr.size() > 0
&& getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim().substring(0, 2) == "PAY"
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(ValidationDetails.WarningType.Other,
"Disposal instruction exist  [CM]");
}
} catch (

Exception e)

{
// Loggers.general().info(LOG,e.getMessage());
}

// for EDI

try

{

String IEcodec = getDriverWrapper().getEventFieldAsText("DRW", "p", "cBBF");
// //Loggers.general().info(LOG,"IEcodecode" + IEcodec);
List<ExtEventShippingCollections> shpcol5 = (List<ExtEventShippingCollections>) getWrapper()
.getExtEventShippingCollections();

String sizeship = getWrapper().getNOOFBLLS();
if (!(shpcol5.size() == (Integer.valueOf(sizeship))) && getMinorCode().equalsIgnoreCase("CRE")
&& (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
// //Loggers.general().info(LOG,"Shipping details is not equal
// to
// table
// size");
validationDetails.addError(ErrorType.Other, "Number of Shipping bills(" + sizeship
+ ")is not equal to ShippingDetails grid(" + shpcol5.size() + ")  [CM]");
}

} catch (Exception e)

{
// Loggers.general().info(LOG,"Exception in EDI" + e.getMessage());
}

try {
currencyCalc();
} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

}
}

if (getMajorCode().equalsIgnoreCase("ODC") && (prd_typ.equalsIgnoreCase("OCI"))) {
// Loggers.general().info(LOG,"Else systemOutput");
getPane().setUSDAMT("");
getPane().setCRAMT_Name("");
getPane().setINRAMT("");
}

String mas = getDriverWrapper().getEventFieldAsText("MST", "r", "");
Loggers.general().info(LOG,"Master Reference on validate==>"+mas);
// String = "0172ELFX0003716";
String evt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
// String str ="";
try {
// //Loggers.general().info(LOG,"enter into bill generate no");
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
Loggers.general().info(LOG,"Master Reference==>"+getPane().getBLLREFNO());
} catch (Exception e) {
// Loggers.general().info(LOG,"exception" + e);
}

// Category code populate based on input branch
try {
String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
con = ConnectionMaster.getConnection();
if (!(BranchCode.length() == 0)) {
String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
// //Loggers.general().info(LOG,"BranchCode Query - " + sql6);
ps1 = con.prepareStatement(sql6);
rs = ps1.executeQuery();
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

String cust = getDriverWrapper().getEventFieldAsText("DRW", "p", "no").trim();
// //Loggers.general().info(LOG,"Primary customer taking ----> " +
// cust);
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
// //Loggers.general().info(LOG,"charge account collected " +
// chargecol);
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

// Loggers.general().info(LOG,"custoemr number in query"
// +
// custval);
// Loggers.general().info(LOG,"custoemr number in
// transaction" + cust);
validationDetails.addWarning(WarningType.Other,
"Account selected in Settlement for charges does not belong to the Customer  [CM]");
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

try {

String productVal = getDriverWrapper().getEventFieldAsText("DRW", "p", "cAJB").trim();
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

// Advance Table Validations
String customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no").trim();
if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")) {
// //Loggers.general().info(LOG,"Advance Table Validation for
// collection--->");
try {
String inwnum = "";
String master=null;
double totalAmt = 0;
String balanceValCurrency = ""; // String
double balance = 0.0;
String creditcur = "";
long creditAmount = 0;
long balanceAmt = 0;
String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
//    String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String cusNo = "";
double balan = 0.0;
double amtutl = 0.0;
String balancur = "";
String amtutlcur = "";
List<ExtEventAdvanceTable> liste = (List<ExtEventAdvanceTable>) getWrapper()
.getExtEventAdvanceTable();
for (int a = 0; a < liste.size(); a++) {
ExtEventAdvanceTable adve = liste.get(a);
cusNo = adve.getCUSCIFNO().trim();
balan = adve.getBALANCE().doubleValue();
amtutl = adve.getAMTUTIL().doubleValue();
balancur = adve.getBALANCECurrency();
amtutlcur = adve.getAMTUTILCurrency();
inwnum = adve.getINWARD().trim();
// //Loggers.general().info(LOG,"Advance Table Validation in
// customer in odc--->" + cusNo + "" + customera);

Loggers.general().info(LOG,"balance  AMount " + balan);
if ((!cusNo.equalsIgnoreCase(customera)) && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker")
  || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(WarningType.Other,
"Remittance Customer is not same as the Drawer in advance table [CM]");

}
if (amtutl > balan && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker")
  || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addError(ErrorType.Other,
"Utillization of amount should not greater than available amount in advance table [CM]");
}
if (!balancur.equalsIgnoreCase(amtutlcur) && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker")
  || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(WarningType.Other,
"Utillization of amount currency should equal to available amount currency in advance table [CM]");
}

if (!inwnum.equalsIgnoreCase("")) {

String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'DD/MM/YY')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE trim(MAS_REF)='"
+ inwnum + "'";
Loggers.general().info(LOG,"query for getting all fields in inward Remittance grid22 " + inwardDetails);
con = getConnection();
pst = con.prepareStatement(inwardDetails);

rs1 = pst.executeQuery();
if (rs1.next()) {

creditAmount = rs1.getLong(4);
creditcur = rs1.getString(5);


Loggers.general().info(LOG,"Credit AMount22 " + creditAmount);


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


Loggers.general().info(LOG,"Query for getting Inward Utilized Amount===>" + BalAmtQuery);

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
Loggers.general().info(LOG,"IRM Clourse Query ---" + closureQuery);
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
Loggers.general().info(LOG,"Balance Credit Amount22-->" + balanceAmt);
}
//fdwarapper1.setCUSCIFNO(cif_no);
if (balanceAmt > 0) {

balance = Double.valueOf(balanceAmt);
Loggers.general().info(LOG,"Balance Credit Amount221-->" + balance);

} else {
balance=0.0;
}
} else {

double irmAmt = 0;
String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
  + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
  + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"IRM Clourse Query --->" + closureQuery);
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
Loggers.general().info(LOG,"Balance Credit Amount22-->" + balan_cret);
}

if (balan_cret > 0) {

balance = Double.valueOf(balan_cret);


} else {
balance =0.0;
}



}
BigDecimal divideByBig1 = new BigDecimal(balance);
BigDecimal divideByBig2 = new BigDecimal(amtutl);
int res1=0;
res1=divideByBig1.compareTo(divideByBig2);

Loggers.general().info(LOG,"Balance Credit Amount221-->" + balance+"bal" + amtutl );
if ((res1==-1)&& (step_csm.equalsIgnoreCase("CBS Authoriser")
|| step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))){
Loggers.general().info(LOG,"Amount less than outstanding12");     

String query="SELECT mas.MASTER_REF FROM UBZONE.master mas,UBZONE.BASEEVENT bas,"
+" UBZONE.exteventadv ext WHERE mas.KEY97      =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS  IN ('i','c')"
+" and ext.INWARD='"+inwnum+"' and mas.master_ref!='"+masReference+"'";
Loggers.general().info(LOG,"Loan Amount currency query12-------------> "+query);    
ps2 = con.prepareStatement(query);
rs2 = ps2.executeQuery();
while (rs2.next()) {
/*String master=rs2.getString("MASREF");
mst=mst + " " +master;*/

if(master ==null || master.isEmpty())
  master=rs2.getString("MASTER_REF");
else
  master=master + " " +rs2.getString("MASTER_REF");
}

Loggers.general().info(LOG,"Balance Credit Amount221-->" + balance);
validationDetails.addError(ErrorType.Other,
  "Available amount for XAR "+inwnum+" is used in "+master+" so available amount exceeds please check  [CM]");      
}
}

}


}
} catch (Exception e) {
// Loggers.general().info(LOG,"Exception is Advance Table
// Validation
// in customer" + e.getMessage());
}
} else {
// Loggers.general().info(LOG,"Advance Table Validation not
// collection
// create--->");
}

getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
getPane().getExtEventLoanDetailsNew().setEnabled(false);
getPane().getExtEventLoanDetailsUp().setEnabled(false);
getPane().getExtEventLoanDetailsDown().setEnabled(false);
// Advance remittance Grid Validation
// If utilization Currency differes
try {
// int count = 0;
// String first_curr = "";
// ArrayList<String> Currency = new ArrayList<String>();
BigDecimal utilizationAmount = new BigDecimal(0);
List<ExtEventAdvanceTable> advance = (List<ExtEventAdvanceTable>) getWrapper()
.getExtEventAdvanceTable();
for (int l = 0; l < advance.size(); l++) {
ExtEventAdvanceTable advance_obj = advance.get(l);
// Currency.add(advance_obj.getAMTUTILCurrency().trim());

String firc = advance_obj.getFINUMB().trim();
String inwRem = advance_obj.getINWARD().trim();
String cusNum = advance_obj.getCUSCIFNO();
// Loggers.general().info(LOG,"cusNum no---------->" + cusNum);
String RemName = advance_obj.getNAMREM();
// Loggers.general().info(LOG,"RemName no---------->" +
// RemName);
String advrem = advance_obj.getADVRECB();
// Loggers.general().info(LOG,"advrem no---------->" + advrem);
String RemCunntry = advance_obj.getCOUNREM();
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"FIRC Number===>" + firc + "Inward remittance Number" + inwRem);

}

if ((firc.equalsIgnoreCase("") || firc == null)
&& (inwRem.equalsIgnoreCase("") || inwRem == null)) {
if (step_Input.equalsIgnoreCase("i") && step_csm.equalsIgnoreCase("CBS Maker")) {

validationDetails.addError(ErrorType.Other,
"FIRC Number/Inward remittance Number should Mandatory in Advance grid [CM]");
}
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"FIRC Number else===>" + firc + "Inward remittance Number" + inwRem);

}
}

if (!firc.equalsIgnoreCase("") || firc.length() > 0) {
if ((!inwRem.equalsIgnoreCase("") || inwRem.length() > 0)
&& (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

validationDetails.addError(ErrorType.Other,
"FIRC No is inputted, Please remove the Inward remittance no [CM]");
}
}

if (!inwRem.equalsIgnoreCase("") || inwRem.length() > 0) {
if ((!firc.equalsIgnoreCase("") || firc.length() > 0) && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker"))) {

validationDetails.addError(ErrorType.Other,
"Inward remittance no is inputted, Please remove the FIRC No [CM]");
}
}

if (!inwRem.equalsIgnoreCase("") || inwRem.length() > 0) {
if (cusNum.equalsIgnoreCase("") && RemName.equalsIgnoreCase("")
&& (RemCunntry.equalsIgnoreCase("")) && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker")
  || step_csm.equalsIgnoreCase("CBS Maker 1"))) {

if (prd_typ.equalsIgnoreCase("MBI")) {
validationDetails.addError(ErrorType.Other,
  "Please fetch the advance details table [CM]");
} else {
validationDetails.addWarning(WarningType.Other,
  "Please fetch the advance details table [CM]");
}

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Remittance name else===>" + RemName
  + "Customer name  and country" + cusNum + RemCunntry);

}
}
} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Inward remittance no---------->" + inwRem);

}
}

if (!firc.equalsIgnoreCase("") || firc.length() > 0) {
if (cusNum.equalsIgnoreCase("") && RemName.equalsIgnoreCase("")
&& advrem.equalsIgnoreCase("") && RemCunntry.equalsIgnoreCase("")
&& (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
"Please Input Remittance AdCode,Remitter Name,Remittance Date,Remittance country,Customer CIF No,Available amount [CM]");

} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Fiels value cusNu--------->" + cusNum + "RemName" + RemName
  + "advrem" + advrem + "RemCunntry" + RemCunntry);

}
}
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Firc number--->" + firc);
}
}

BigDecimal utilizationAmt = advance_obj.getAMTUTIL();
// Loggers.general().info(LOG,"utilization Amount initial--->" +
// utilizationAmt);
String currency = advance_obj.getAMTUTILCurrency();
ConnectionMaster connectionMaster = new ConnectionMaster();
double dividemixcur = connectionMaster.getDecimalforCurrency(currency);
BigDecimal divideMix = new BigDecimal(dividemixcur);
BigDecimal utilAmount = utilizationAmt.divide(divideMix);
// Loggers.general().info(LOG,"utilization Amount after
// convert--->"
// + utilAmount);

utilizationAmount = utilizationAmount.add(utilAmount);

}

String colAmount = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
BigDecimal collectionAmount = new BigDecimal(colAmount);
// Loggers.general().info(LOG,"Collection Amount--->" +
// collectionAmount);

if (advance.size() > 0 && (collectionAmount.compareTo(utilizationAmount) != 0)
&& (step_Input.equalsIgnoreCase("i")) && step_csm.equalsIgnoreCase("CBS Maker")) {
validationDetails.addWarning(WarningType.Other,
"Utilized Remittance amount in Advance grid and Collection amount should be always same [CM]");
} else {
Loggers.general().info(LOG,"Total utilization Amount else loop--->" + utilizationAmount + "colAmount"
+ collectionAmount);

}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in utilization" +
// e.getMessage());
}

List<ExtEventAdvanceTable> listval = (List<ExtEventAdvanceTable>) getWrapper()
.getExtEventAdvanceTable();

if ((!advrec.equalsIgnoreCase(""))
&& (advrec.equalsIgnoreCase("Full") || advrec.equalsIgnoreCase("Part"))) {

if ((listval.size() == 0 || listval.size() < 1) && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
"Advance Received selection is selected,please input Advance Remittance Grid [CM]");
}

}

if ((listval.size() > 0) && (step_Input.equalsIgnoreCase("i"))
&& step_csm.equalsIgnoreCase("CBS Maker")) {
if (advrec.equalsIgnoreCase("") || advrec.length() < 1) {
validationDetails.addError(ErrorType.Other,
"Advance Remittance Grid entered,Please select Advance Received selection [CM]");
}
}

if ((step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
try {
// ------------------*********************************------------------------------

String resultamt = "";
String shippingAmt = "";
String repay = "0.00";
Double shipping = 0.00;
double utlizationdou = 0.00;
String finalrepay = "";
String advan = "";
double totalAdv = 0;
String adv = "";
double utlizat = 0.0;
double utlization_convert = 0.0;
String utlizationdouStr = "";
double totalamt = 0.00;
double amount_double = 0.00;
advan = getDriverWrapper().getEventFieldAsText("cARK", "v", "m");
// //Loggers.general().info(LOG,"advance amount " + advan);

List<ExtEventShippingCollections> shpcoll = (List<ExtEventShippingCollections>) getWrapper()
.getExtEventShippingCollections();

for (int k = 0; k < shpcoll.size(); k++) {// LOOPS
                                      // STARTS
ExtEventShippingCollections shippCol = shpcoll.get(k);
String Currency = String.valueOf(shippCol.getCSHPAMTCurrency());

Double billAmt = Double.valueOf(String.valueOf(shippCol.getCSHPAMT()));
shipping = shipping + billAmt;
Double shiptol = decimalval(shipping, Currency);
shippingAmt = String.valueOf(shiptol);
// //Loggers.general().info(LOG,"Fianl shippingAmt " +
// shippingAmt);
String colAmt = getDriverWrapper().getEventFieldAsText("ORA", "v", "m");
String colc = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
// //Loggers.general().info(LOG,"Colletion amount " +
// colAmt);

if (!(advan == null || advan.length() == 1)) {
// //Loggers.general().info(LOG,"rate of Refund value
// for
// subvention" + advan + "length" +
// advan.length());

totalAdv = Double.valueOf(advan);

}
// advance = advan.replaceAll("^0-9", "");
// //Loggers.general().info(LOG,"Advance received Amount " +
// totalAdv);
Double totalCol = NullPoint(colAmt);
// //Loggers.general().info(LOG,"Collection value " +
// totalCol);
// Double totalAdv=Double.valueOf(advan);
// //Loggers.general().info(LOG,"advanced value " +
// totalAdv);
totalamt = totalCol;// + totalAdv;
resultamt = String.valueOf(totalamt);// --BJO
String amountcol = getDriverWrapper().getEventFieldAsText("cBJO", "v", "m");
// //Loggers.general().info(LOG,"Amount to collected value "
// +
// amountcol);
amount_double = Double.valueOf(amountcol);
// //Loggers.general().info(LOG,"Amount to collected in
// double---->" +
// amount_double);
// //Loggers.general().info(LOG,"total added value " +
// resultamt);
adv = getWrapper().getPERADV();
// //Loggers.general().info(LOG,"Advance received" + adv);

BigDecimal collectionAmt = shippCol.getCREPAY();
// //Loggers.general().info(LOG,"collectionAmt---->" +
// collectionAmt);
if (!(collectionAmt == null)) {
repay = collectionAmt.toString();
// //Loggers.general().info(LOG,"repay---->" + repay +
// "repay
// Length--->" + repay);
}

// //Loggers.general().info(LOG,"Repay Amount for advance" +
// repay);
}

for (int k = 0; k < advanced.size(); k++) {// LOOPS
                                      // STARTS
ExtEventAdvanceTable advancedval = advanced.get(k);

String utlization = advancedval.getAMTUTIL().toString();
String utlizationcuy = advancedval.getAMTUTILCurrency().toString();
// //Loggers.general().info(LOG,"utlization---->" +
// utlization);
if (utlization == null || utlization.equalsIgnoreCase("")) {
utlization = "0";
}
utlizat = Double.valueOf(utlization);
// //Loggers.general().info(LOG,"utlization in double---->"
// +
// utlizat);

ConnectionMaster connectionMaster = new ConnectionMaster();
double divideByDecimal = connectionMaster.getDecimalforCurrency(utlizationcuy);
utlization_convert = utlizat / divideByDecimal;

utlizationdou = utlizationdou + (utlization_convert + amount_double);
// //Loggers.general().info(LOG,"Total utlizationdou amount
// ---->"
// +
// utlizationdou);
// //Loggers.general().info(LOG,"Total utlizat amount ---->"
// +
// utlizat);
utlizationdouStr = String.valueOf(utlizationdou);
// //Loggers.general().info(LOG,"final totalamt amount
// ---->" +
// utlizationdouStr);

}

// //Loggers.general().info(LOG,"total shipping amount
// final------>"
// +
// shippingAmt);
// //Loggers.general().info(LOG,"total value final---------->" +
// resultamt);
if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,
"utlization amount ---->" + utlizationdouStr + "shippingAmt" + shippingAmt);
}
if ((!utlizationdouStr.equalsIgnoreCase(shippingAmt)) && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
&& utlization_convert > 0) {
// //Loggers.general().info(LOG,"enter into shipping
// validation");
validationDetails.addWarning(WarningType.Other,
"Remittance Utilization amount should be equal to SB amount [CM]");
}

} catch (Exception e) {
// Loggers.general().info(LOG,"message" + e.getMessage());
}
}

// repayment validation of shipping grid
try {
Double shiptol1 = 0.00;

Double repayamt = 0.00;
String advan1 = "";
String billAmt1 = "0.00";
Double billAmt = 0.00;
Double totalAdv1 = 0.0;
String fnlrepay = "";
String fnladv = "";
String advan = "";
String reptype = "";
advan = getDriverWrapper().getEventFieldAsText("cAIX", "v", "m");
advan1 = getDriverWrapper().getEventFieldAsText("cAIX", "v", "m");
// //Loggers.general().info(LOG,"advance amount " + advan);

List<ExtEventShippingCollections> sh1 = (List<ExtEventShippingCollections>) getWrapper()
.getExtEventShippingCollections();
if (advan1.trim().length() > 0) {
for (int k = 0; k < sh1.size(); k++) { // LOOP STARTS
ExtEventShippingCollections val = sh1.get(k);

String Currency1 = String.valueOf(val.getCSHPAMTCurrency());
BigDecimal amt = val.getCREPAY();

if (!(amt == null)) {
// //Loggers.general().info(LOG,"Shipping amount" + amt
// +
// "length");
billAmt1 = amt.toString();
}

billAmt = Double.valueOf(billAmt1);
if (!(amt == null)) {
repayamt = repayamt + billAmt;
shiptol1 = decimalval(repayamt, Currency1);
// //Loggers.general().info(LOG,"Fianl repayamount in
// double
// "
// +
// shiptol1);
fnlrepay = String.valueOf(shiptol1);
// //Loggers.general().info(LOG,"Fianl repayamount " +
// fnlrepay);
}
} // LOOP ENDS

if (!(advan1 == null || advan1.length() == 1)) {
// //Loggers.general().info(LOG,"rate of Refund value for
// subvention"
// + advan1 + "length" + advan.length());

totalAdv1 = Double.valueOf(advan1);

}
fnladv = String.valueOf(totalAdv1);
// //Loggers.general().info(LOG,"advanced value final------>" +
// totalAdv1);
// //Loggers.general().info(LOG,"shiptol1 value
// final---------->" +
// shiptol1);
}
/*
* int s=shiptol1.compareTo(totalAdv1);
* //Loggers.general().info(LOG, "print s value" + s); if((s==1) ||
* (s==-1))
*/
if (!fnlrepay.equalsIgnoreCase(fnladv) && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
// //Loggers.general().info(LOG,"enter into shipping validation
// 1");
/*
* validationDetails .addError(ErrorType.Other,
* "Sum of the Repayment Amount should be equal to Advanced Received amount"
* );
*/
validationDetails.addWarning(WarningType.Other,
"Sum of the Repayment Amount should be equal to Advanced Received amount  [CM]");
}

} catch (Exception e) {
// Loggers.general().info(LOG,"error in repayment amt validation " +
// e.getMessage());
}

// ================================================

try {
// //Loggers.general().info(LOG,"Notional due date is calling");
getNotionalDueDate();
} catch (Exception ee) {
// Loggers.general().info(LOG,"Notional due date is calling--->" +
// ee.getMessage());
// //Loggers.general().info(LOG,"LOB Catch");
}

try {
int noOfRecord = 0;
con = getConnection();
String customerId = "";
String amount_am = "";
String currency = "";
String masterRef = "";
customerId = getDriverWrapper().getEventFieldAsText("DRW", "p", "no").trim();
// //Loggers.general().info(LOG,"Customer Id " + customerId);
amount_am = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
// //Loggers.general().info(LOG,"Currency is " + amount_am);
String amount = amount_am.replaceAll("[^0-9]", "");
currency = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
// //Loggers.general().info(LOG,"Currency is " + currency);
masterRef = getmasRefNo();
// //Loggers.general().info(LOG,"Master Reference " + masterRef);
// //Loggers.general().info(LOG,"Entered Validate method in CpcoPcoc
// class");
if (amount_am.length() > 0 && currency.length() > 0) {

// //Loggers.general().info(LOG,"Duplicate Record Query is " +
// duplicateMaster);

String duplicateMaster = "select COUNT(*) as COUNT from ETT_DUPLICAT_WARNING_VIEW where PRICUSTMNM='"
+ customerId + "' and AMOUNT='" + amount + "' and CCY='" + currency
+ "' and MASTER_REF !='" + MasterReference + "'";
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
}
} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG,"Exception in Duplicate master " + e.getMessage());
}
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

String portdes = getWrapper().getPORTDES().trim();
// //Loggers.general().info(LOG,"Port of destination Value---->" +
// portdes);
if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
try {
String portvalqury = "select trim(PODESPN),trim(COUNTRY) from EXTPORTDESTINATION WHERE PODEST='"
+ portdes.trim() + "'";
// //Loggers.general().info(LOG,"port code destination query
// Value---->" + portvalqury);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(portvalqury);
rs = ps1.executeQuery();
while (rs.next()) {
String hsploy = rs.getString(1);
String COUNTRY = rs.getString(2);
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
String portvalqury = "select Trim(PORTDESC),Trim(COUNTRY) from EXTPORTODLOAD where trim(PORTLOAD)=trim('"
+ portload + "')";
Loggers.general().info(LOG,"port code destination queryValue---->" + portvalqury);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(portvalqury);
rs = ps1.executeQuery();
while (rs.next()) {
String hsploy = rs.getString(1);
String country = rs.getString(2);
Loggers.general().info(LOG,"port code description---->"+ hsploy);
Loggers.general().info(LOG,"port code country---->"+ country);
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

try {

getcountryCode();

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception update" +
// e.getMessage());
}

try {

int count = 0;
String step = null;
/*String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, BASEEVENT BEV, ETT_REFERRAL_TRACKING REF WHERE MAS.KEY97 = BEV.MASTER_KEY AND trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)  AND BEV.STATUS <>'a' AND TRIM(REF.MASTER_REF_NO) ='"
+ MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode
+ "' AND REF.SUB_PRODUCT_CODE = '" + subproductCode
+ "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID";*/
String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, ETT_REFERRAL_TRACKING REF WHERE  trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)   AND TRIM(REF.MASTER_REF_NO) ='"
+ MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode
+ "' AND REF.SUB_PRODUCT_CODE = '" + subproductCode
+ "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID";

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Query ETT_REFERRAL_TRACKING count===>" + query);
}


String concat = null;
con = getConnection();
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();

while (rs1.next()) {

step = rs1.getString(1);

count = rs1.getInt(2);
Loggers.general().info(LOG,"Entered while referal step" + step+"count" + count);

if (count > 0) {
if ((step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Reject"))
&& !step_csm.equalsIgnoreCase("CBS Authoriser")) {

String ref = null;
String statusReferral = null;
String warningMessage = null;
String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
+ " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
+ " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
+ subproductCode + "'"
+ " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Query ETT_REFERRAL_TRACKING CBS Maker 2nd===>" + query6);
}
ps1 = con.prepareStatement(query6);

rs = ps1.executeQuery();
ArrayList<String> al = new ArrayList<String>();
while (rs.next()) {
if (rs.getString(2).equals("PEND")) {
Loggers.general().info(LOG,"referral_)status------------>" +rs.getString(2));
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

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Single Warning Message 2nd" + warningMessage);
}

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
+ subproductCode + "'"
+ " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Query ETT_REFERRAL_TRACKING CBS Authoriser 3rd===>" + query6);
}
ps1 = con.prepareStatement(query6);

rs = ps1.executeQuery();
ArrayList<String> al = new ArrayList<String>();
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
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"warningMessage ETT_REFERRAL_TRACKING CBS Authoriser 3rd===>"
  + warningMessage);
}

validationDetails.addError(ErrorType.Other, warningMessage);

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG," ETT_REFERRAL_TRACKING CSM===>");
}
}
} else {
Loggers.general().info(LOG," ETT_REFERRAL_TRACKING count value===>" + count);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG," ETT_REFERRAL_TRACKING count value===>" + count);
}

}

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG," ETT_REFERRAL_TRACKING count value===>" + count);
}
}

} catch (Exception e1) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Exception ETT_REFERRAL_TRACKING=====>" + e1.getMessage());
}
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
String query5 = "SELECT  count(*) FROM ETT_WF_CHKLST_TRACKING  WHERE  MASTER_REF ='"
+ MasterReference + "'" + " AND EVENTREF= '" + eventCode + "'" + " and PROD_CODE='"
+ prd_code + "'" + " AND SUB_PRODUCT_CODE= '" + subproductCode + "'"
+ " and INIT_AT IN ('CSM','CBS Maker') and MANDATORY='REJ'  and CHECKED_YN ='Y'";
// Loggers.general().info(LOG,"ArrayList Warning Message rejection
// 1st"
// + query5);
con = getConnection();
pst = con.prepareStatement(query5);

rs = pst.executeQuery();
while (rs.next()) {
counter = Integer.valueOf(rs.getString(1));
}

// //Loggers.general().info(LOG,"Rejection list count===>" +
// counter);

if (counter > 0) {
String ref = null;
String statusReferral = null;
String warningMessage = null;
String query6 = "SELECT  INIT_AT,CHECKLIST_SHRT_DESCR "
+ " FROM ETT_WF_CHKLST_TRACKING  WHERE  MASTER_REF ='" + MasterReference + "'"
+ " AND EVENTREF = '" + eventCode + "'" + " and PROD_CODE='" + prd_code + "'"
+ " AND SUB_PRODUCT_CODE = '" + subproductCode + "'"
+ " and INIT_AT IN ('CSM','CBS Maker') and MANDATORY='REJ' and CHECKED_YN ='Y'";
// Loggers.general().info(LOG,"ArrayList Warning Message
// rejection
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
if (al.size() > 0 && (step_csm.equalsIgnoreCase("CSM")
|| step_csm.equalsIgnoreCase("CSM Reject") || step_csm.equalsIgnoreCase("AdhocCSM"))) {
// Loggers.general().info(LOG,"ArrayList Single Warning CSM
// 2nd"
// + warningMessage);
validationDetails.addWarning(WarningType.Other, warningMessage);
}

else if (al.size() > 0 && (step_csm.equalsIgnoreCase("CBS Reject")
|| step_csm.equalsIgnoreCase("CBS Maker"))) {
// Loggers.general().info(LOG,"ArrayList Single Warning CBS
// 2nd"
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
validationDetails.addWarning(WarningType.Other,
"Past pending deferral for same client [CM]");
}

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"No pending referal status");
}

}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"ExceptionPending referal status" + e.getMessage());
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

if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker")
|| step_csm.equalsIgnoreCase("CBS Maker 1"))) {
// //Loggers.general().info(LOG,"Over due bill exists
// for
// this
// customer in if loop " + cust);
validationDetails.addWarning(WarningType.Other,
"Over due bill exists for this customer (" + cust + ") [CM]");
}

else {
// //Loggers.general().info(LOG,"Over due bill exists
// for
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
// Loggers.general().info(LOG,"Connection Failed! Check
// output
// console");
e.printStackTrace();
}
}
} else {
// Loggers.general().info(LOG,"Step id not CMS Maker");
}


//-----------------FORCE DEBIT start----------------------------

if((step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Reject")||step_csm.equalsIgnoreCase("CBS Authoriser")) && getMajorCode().equalsIgnoreCase("ODC") &&  (getMinorCode().equalsIgnoreCase("CRE"))) {

//String forEvent = getFORCEDEBIT();
String forFin = getFORCEDEBITFIN();

String TransForceDebit=getDriverWrapper().getEventFieldAsText("cAHW", "l","").trim();
//String TransForceDebitFinance=getDriverWrapper().getEventFieldAsText("cAHW", "l","").trim();
Loggers.general().info(LOG,"Force Debit in main event "+TransForceDebit);
Loggers.general().info(LOG,"Force Debit in Finance "+forFin);

/*    if (forFin.equalsIgnoreCase("N")&&TransForceDebit.equalsIgnoreCase("Y")) {

validationDetails.addError(ErrorType.Other,
"Force Debit Flag should be ticked in subsidiary event [CM]");

} else*/ if (TransForceDebit.equalsIgnoreCase("N") && forFin.equalsIgnoreCase("Y"))
{
if(getMinorCode().equalsIgnoreCase("CRE"))
validationDetails.addError(ErrorType.Other,
"Force Debit Flag should be ticked in create event [CM]");

}

}


//-----------------FORCE DEBIT end----------------------------
//merchant trade
String merchanting=getDriverWrapper().getEventFieldAsText("cARQ","l","");
try{
//String merchanting=getDriverWrapper().getEventFieldAsText("cARQ","l","");
String merchref=getWrapper().getREMERREF();
Loggers.general().info(LOG,"Merchanting------ "+merchanting);
Loggers.general().info(LOG,"Merchanting Ref No-----"+merchref);
//String merchref=getWrapper().getREMERREF();
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

if (getMajorCode().equalsIgnoreCase("ODC")&&merchanting.equalsIgnoreCase("Y"))

{
try{
String advpaymentreceived=getDriverWrapper().getEventFieldAsText("cAJR","l", "");
String dateofremitt = getWrapper().getDATEREM();
String remittrefnoadv = getWrapper().getREMREFAP();

//String advpaymentmade = getWrapper().getADVPYMTR().toString();
String shipdate = getWrapper().getDASHIP_Name();


Loggers.general().info(LOG,"Advance Payment"+advpaymentreceived);
Loggers.general().info(LOG,"Date of remittance"+dateofremitt);
Loggers.general().info(LOG,"Remittance Ref No"+remittrefnoadv);
Loggers.general().info(LOG,"Shipment Date"+shipdate);

SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
Calendar c = Calendar.getInstance();
//Loggers.general().info(LOG,"Advance Payment--"+advpaymentreceived);

if(advpaymentreceived.equalsIgnoreCase("Y")){


if(dateofremitt==null||dateofremitt.equalsIgnoreCase(""))
{
validationDetails.addError(ErrorType.Other,
"Please enter date of remittance [CM]");
}
}

if(advpaymentreceived.equalsIgnoreCase("Y"))
{
if(remittrefnoadv==null||remittrefnoadv.equalsIgnoreCase(""))
{
validationDetails.addError(ErrorType.Other,
"Please enter remittance reference number for advance payment [CM]");
}
}


if(advpaymentreceived.equalsIgnoreCase("Y")){

int gra = 90;
try {

c.setTime(sdf.parse(dateofremitt));
// //Loggers.general().info(LOG,"expdate in issue-------> ");
Loggers.general().info(LOG,"Shippment Date in odc 3m"+dateofremitt);
c.add(Calendar.DATE, gra);
// //Loggers.general().info(LOG,"DATE 1"+ c);
String output = sdf.format(c.getTime());
// //Loggers.general().info(LOG,output);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG," Merchanting Completion date for 3m ODC---->" + output);
}
//                      getPane().setMERTCOMD(output);

}
catch(Exception e)
{
Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 3m"+e.getMessage());
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
Loggers.general().info(LOG," Merchanting Completion date for 6m odc---->" + output);
}
//                      getPane().setMERTCOMD(output);

}
catch(Exception e)
{
Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 6m"+e.getMessage());
}


}
}
catch(Exception e)
{
Loggers.general().info(LOG,"Exception in  Merchanting Completion date"+e.getMessage());

}

}
try{
String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
String paymentType = getWrapper().getPROREMT();
String utrNum = getWrapper().getUTRNO().trim();
if (paymentType.equalsIgnoreCase("RTG") && utrNum.length() != 22
&& (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")||step_csm.equalsIgnoreCase("CBS Maker1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
&& rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N"))
{
validationDetails.addError(ErrorType.Other, "Please review UTR number for RTGS[CM]");
}else if (paymentType.equalsIgnoreCase("NEF") && utrNum.length() != 16
&& (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")||step_csm.equalsIgnoreCase("CBS Maker1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
&& rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N"))
{
validationDetails.addError(ErrorType.Other, "Please review UTR number for NEFT[CM]");
}
else
{
Loggers.general().info(LOG,"Lenth is correct");
}

}
catch(Exception e)
{
e.printStackTrace();
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
//CR 140
if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE"))
{try {
int cnt = 0;
Loggers.general () .info(LOG,"PCR/PCF--");
cnt = preshipWar();
Loggers.general () .info(LOG,"count" + cnt);

String subProductype = getDriverWrapper().getEventFieldAsText("PTP",
"s", "");
Loggers.general () .info(LOG,"subProductype in odc/cre==>" + subProductype);

if (cnt == 1 && (subProductype.equalsIgnoreCase("OCF")||
subProductype.equalsIgnoreCase("OCI")||subProductype.equalsIgnoreCase("NBI")
)) {
//Loggers.general () .info(LOG,"duplicate reference");
validationDetails.addWarning(WarningType.Other,"Packing credits are outstanding for the customer. Please check if this payment is to be used to knock off exsisting PCR/PCF [CM]");             
}
} catch (Exception ee) {
//ee.printStackTrace();
Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());

}}

//CR 140

// CR220 Changes Startes here

if (getMajorCode().equalsIgnoreCase("ODC")&&(getMinorCode().equalsIgnoreCase("CRE"))) {
System.out.println("CR-220 Validation starts here");
try {
con = getConnection();
String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
+ MasterReference
+ "' and BEV.REFNO_PFIX= '"
+ evnt
+ "' and BEV.REFNO_SERL = '"
+ evvcount
+ "'";
int count = 0;
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
count = rs1.getInt(1);
}

if (count > 0
&& (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm
.equalsIgnoreCase("CBS Maker 1"))) {
System.out
.println("CR-220 Validation setting up Error");
validationDetails
.addError(ErrorType.Other,
  "FX deal rate is outside specified tolerance [CM]");
} else {
}

} catch (Exception e) {
System.out
.println("CR-220 Validation Exception occured");
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
System.out.println("CR-220 Validation starts here");
}

// CR220 Changes Ends here


// CR 215 starts here

if ((getMinorCode().equalsIgnoreCase("MBC"))
&& (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm
.equalsIgnoreCase("CBS Authoriser"))) {

int getAccountFromTI = 0;

try {
getAccountFromTI = getAccountFromTI();

System.out.println("Result getAccountFromTI-->"
+ getAccountFromTI);

if (getAccountFromTI > 0) {
validationDetails
.addError(ErrorType.Other,
  "Manual book keeping entries not allowed for these GL Accounts [CM]");
}

} catch (Exception e) {
System.out.println("Exception in getAcoount-->"
+ e.getMessage());
e.printStackTrace();
}

}

//CR 215 ends here
//INC4431622 starts
try {
Loggers.general () .info(LOG,"Entered method");
removeSpace();

} catch (Exception e) {
Loggers.general () .info(LOG,"Exception update" +e.getMessage());
}

}
if ((getMajorCode().equalsIgnoreCase("ODC"))&&(getMinorCode().equalsIgnoreCase("CRE"))) {
try {
DecimalFormat df = new DecimalFormat("##,##,##0.##");
String collAmtStr = getDriverWrapper().getEventFieldAsText("ORA", "v", "m");
String collAmtCcy = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
String AdvAmtStr = getDriverWrapper().getEventFieldAsText("cARK", "v", "m");
String AdvAmtStrCcy = getDriverWrapper().getEventFieldAsText("cARK", "v", "c");
String AdvRcv = getDriverWrapper().getEventFieldAsText("cARL", "s", "");
if(AdvRcv.equalsIgnoreCase("Part")) {
BigDecimal colAmt = new BigDecimal(collAmtStr);
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
BigDecimal colAmt = new BigDecimal(collAmtStr);
String value2 = df.format(colAmt) + " " + collAmtCcy;
getPane().setNETRECIV("0 "+collAmtCcy);
getPane().setADVREC(value2);
}
else {
BigDecimal colAmt = new BigDecimal(collAmtStr);
String value2 = df.format(colAmt) + " " + collAmtCcy;
getPane().setNETRECIV(value2);
getPane().setADVREC("0 "+collAmtCcy);
}
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Calendar c = Calendar.getInstance();
String rvdDate = getDriverWrapper().getEventFieldAsText("RVD","d","");
String tenorPeriod = getDriverWrapper().getEventFieldAsText("cAEP","s","");
String tenorPeriod1 = getDriverWrapper().getEventFieldAsText("cBFL","s","");
System.out.println("getNotionalDueDate: "+rvdDate);
if((tenorPeriod != null && !tenorPeriod.trim().isEmpty())||(tenorPeriod1 != null && !tenorPeriod1.trim().isEmpty()))
{
if(tenorPeriod != null && !tenorPeriod.trim().isEmpty()) {
int t = Integer.parseInt(tenorPeriod);
System.out.println("tenorPeriod" +t);

if (t>0) {
c.setTime(sdf.parse(rvdDate));
c.add(Calendar.DATE, t);
System.out.println("inside notional IF: " +sdf.format(c.getTime()));
getPane().setSIGVALDT(sdf.format(c.getTime()));
}
}
if(tenorPeriod1 != null && !tenorPeriod1.trim().isEmpty()) {
int t1 = Integer.parseInt(tenorPeriod1);
System.out.println("tenorPeriod" +t1);
if (t1>0) {
c.setTime(sdf.parse(rvdDate));
c.add(Calendar.DATE, t1);
System.out.println("inside notional IF DUE DATE: " +sdf.format(c.getTime()));
getPane().setPODESCP(sdf.format(c.getTime()));
}
}

}
}catch (Exception e) {
e.printStackTrace();
}
}

String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
//Goods Code and HS code comparison 
if ((getMajorCode().equalsIgnoreCase("ODC"))&&(getMinorCode().equalsIgnoreCase("CRE")||(step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker")
|| step_csm.equalsIgnoreCase("CBS Maker 1")))) {
try {
getNostroDetails();
System.out.println("outside nostro details");
}
catch (Exception e) {
e.printStackTrace();
}
try {
String goodsCode = getDriverWrapper().getEventFieldAsText("GDC", "s", "");
String hmCode = getDriverWrapper().getEventFieldAsText("cACO", "s", "");
String lastFour ="";
String firstFour ="";
if(goodsCode.length()>4){
lastFour = goodsCode.substring(goodsCode.length()-4);
System.out.println("Last four:"+lastFour);
}
else{
lastFour = goodsCode;
}

if(hmCode.length()>4){
firstFour = hmCode.substring(0,4);
System.out.println("First four:"+firstFour);
}
else{
firstFour = hmCode;
}

if(!lastFour.equals(firstFour)){
System.out.println("Goodscode&HScode not equal");
validationDetails.addWarning(WarningType.Other,"Goodscode and HSCode are not proper");
}
}
catch (Exception e) {
e.printStackTrace();
}
}

//swiftsfms
/*    if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE"))
{
try{
int v;
String Branch=getDriverWrapper().getEventFieldAsText("CBK", "p", "ss").trim();
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
Loggers.general().info(LOG,"Same currency fx conversion" + e.getMessage());

}
}*/


if (getMajorCode().equalsIgnoreCase("ODC") && (getMinorCode().equalsIgnoreCase("CRE"))) {

/*    String formtype=getDriverWrapper().getEventFieldAsText("cARJ", "s", "").trim();
if(formtype==null)
{
validationDetails
.addError(ErrorType.Other,
"Form Type is mandatory");
}*/
// For shipping details warning
//try {
//System.out.println("getWarningShipping");
//con = getConnection();
//String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
//String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
////    String prodType = getDriverWrapper().getEventFieldAsText("SLN", "s", "").trim();
//System.out.println("Shipping_Warning: "+masterref+", "+eventREF);
//
//
//String query = "SELECT TRIM(EXTS.CBILLNUM),TRIM(EXTS.CBILLDA),TRIM(EXTS.CFORMN) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTSHC EXTS" +
//" WHERE MAS.KEY97 = BEV.MASTER_KEY" +
//" AND BEV.KEY97 = EXT.EVENT" +
//" AND EXTS.FK_EVENT = EXT.KEY29" +
//" AND MAS.MASTER_REF = '"+masterref+"'" +
//" AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='"+eventREF+"'";
//System.out.println(query);
//ps1 = con.prepareStatement(query);
//rs1 = ps1.executeQuery();
//if (!rs1.next()) {
//validationDetails.addWarning(WarningType.Other, "Shipping details should be filled");
//}
//else {
//System.out.println("Shipping grid  has data");
//}
//
//} catch (Exception e) {
//e.printStackTrace();
//}
try {
System.out.println("getWarningShipping");
con = getConnection();
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
String merchantTrading = "";
String formType="";
//    String prodType = getDriverWrapper().getEventFieldAsText("SLN", "s", "").trim();


List<ExtEventShippingCollections> shipcount = (List<ExtEventShippingCollections>) getWrapper()
.getExtEventShippingCollections();
List<ExtEventShippingdetails> shipdetails = (List<ExtEventShippingdetails>) getWrapper()
.getExtEventShippingdetails();
List<ExtEventInvoiceDetails> invoiceDetails = (List<ExtEventInvoiceDetails>) getWrapper()
.getExtEventInvoiceDetails();
System.out.println("shipping table size"+shipcount.size()+" "+shipdetails.size()+" "+invoiceDetails.size());
/*    String query = "SELECT TRIM(EXTS.CBILLNUM),TRIM(EXTS.CBILLDA),TRIM(EXTS.CFORMN) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTSHC EXTS" +
" WHERE MAS.KEY97 = BEV.MASTER_KEY" +
" AND BEV.KEY97 = EXT.EVENT" +
" AND EXTS.FK_EVENT = EXT.KEY29" +
" AND MAS.MASTER_REF = '"+masterref+"'" +
" AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='"+eventREF+"'";
System.out.println(query);
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();*/
merchantTrading = getPane().getMACHTD().trim();
formType = getPane().getFORTYP().trim();
if ((subproCode.equalsIgnoreCase("01E") || subproCode.equalsIgnoreCase("02E")
|| subproCode.equalsIgnoreCase("04E") || subproCode.equalsIgnoreCase("05E")
|| subproCode.equalsIgnoreCase("06E") || subproCode.equalsIgnoreCase("07E")
|| subproCode.equalsIgnoreCase("08E") || subproCode.equalsIgnoreCase("09E")
|| subproCode.equalsIgnoreCase("41F") || subproCode.equalsIgnoreCase("42F")
|| subproCode.equalsIgnoreCase("44F") || subproCode.equalsIgnoreCase("51F")
|| subproCode.equalsIgnoreCase("52F") || subproCode.equalsIgnoreCase("54F")
|| subproCode.equalsIgnoreCase("61F")
|| subproCode.equalsIgnoreCase("62F")) && (!merchantTrading.equalsIgnoreCase("YES"))
&& (!formType.equalsIgnoreCase("EXEMPTED"))) {
System.out.println("Shipping_Warning: "+merchantTrading+", "+formType);
if (shipcount.size()==0) {
validationDetails.addError(ErrorType.Other, "Shipping details should be filled");
}
else {
System.out.println("Shipping grid  has data");
}
if(shipdetails.size()==0||invoiceDetails.size()==0) {
validationDetails.addError(ErrorType.Other, "Shipping and invoice details must be filled");
}
}
} catch (Exception e) {
e.printStackTrace();
}
finally {
surrenderDB(con, ps1, rs1);
}

try {
System.out.println("getWarningRemmitance");
con = getConnection();
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
//    String prodType = getDriverWrapper().getEventFieldAsText("SLN", "s", "").trim();
String AdvRcv = getDriverWrapper().getEventFieldAsText("cARL", "s", "");
System.out.println("Remmitance_Warning: "+masterref+", "+eventREF+" ,"+AdvRcv);

if((AdvRcv.equalsIgnoreCase("Part"))||(AdvRcv.equalsIgnoreCase("Full")))
{     
List<ExtEventAdvanceTable> liste = (List<ExtEventAdvanceTable>) getWrapper()
.getExtEventAdvanceTable();
System.out.println("invoice data size"+liste.size());


}
} catch (Exception e) {
e.printStackTrace();
}
finally {
surrenderDB(con, ps1, rs1);
}

}

if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")) {

try{
List<ExtEventShippingCollections> shipcount = (List<ExtEventShippingCollections>) getWrapper()
.getExtEventShippingCollections();
int shipcountSize = shipcount.size();
System.out.println("shipcountSize: "+shipcountSize);
String value = String.valueOf(shipcountSize);
getPane().setNOOFBLLS(value);
getPane().setNOOFBILL(value);
}catch (Exception e) {
e.printStackTrace();
}
}

try {

List<ExtEventShippingCollections> liste = (List<ExtEventShippingCollections>) getWrapper()
.getExtEventShippingCollections();
System.out.println("Enters into collection amount:");

String colamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m").trim();
String colcurr="";
System.out.println("Collection Amount:"+colamt);
BigDecimal colamtbg=new BigDecimal(colamt);
System.out.println("Collection amount Decimal:"+colamtbg);
BigDecimal hundred=new BigDecimal(100);
BigDecimal colamtct=new BigDecimal(0.00);
for (int k = 0; k < liste.size(); k++) {
ExtEventShippingCollections val1 = liste.get(k);
BigDecimal shipamt =val1.getCSHPAMT();
colcurr=val1.getCSHPAMTCurrency();
System.out.println("shipamt:"+shipamt+" "+colcurr);
colamtct=colamtct.add(shipamt);
System.out.println("colamtct:"+colamtct);

}
colamtct=colamtct.divide(hundred);
colamtct=colamtct.setScale(2,BigDecimal.ROUND_HALF_EVEN);
if(colcurr != null && !colcurr.trim().equals("")) {
getPane().setTOTSBAMT(colamtct.toString() + " " + colcurr);
}
System.out.println(colamtct); 

}
catch(Exception e)
{
e.printStackTrace();
}

try {
List<ExtEventShippingCollections> liste = (List<ExtEventShippingCollections>) getWrapper()
.getExtEventShippingCollections();
for (int k = 0; k < liste.size(); k++) {
ExtEventShippingCollections val1 = liste.get(k);
String shippingBill=val1.getCBILLNUM();
String formno=val1.getCFORMN();
System.out.println(" form number and shipping bill "+shippingBill+" "+formno);
if ((formno.trim().equalsIgnoreCase("")
|| formno.trim().isEmpty()||formno==null)&&(shippingBill.trim().equalsIgnoreCase("")
|| shippingBill.trim().isEmpty()||shippingBill==null)) {
System.out.println("shippingBill validation:"+formno+" "+shippingBill);
validationDetails.addWarning(WarningType.Other, "Kindly fill shipping bill or form number");
}
if ((!formno.trim().equalsIgnoreCase("")
|| !formno.trim().isEmpty())&&(!shippingBill.trim().equalsIgnoreCase("")
|| !shippingBill.trim().isEmpty())) {
System.out.println("shippingBill and formno validation:"+formno+" "+shippingBill);
validationDetails.addWarning(WarningType.Other, "Kindly remove shipping bill or form number");
}
}
}
catch(Exception e)
{
e.printStackTrace();
}

//Merchant Trade-19/12/22           

if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")) {
try {
freezeMttNumber();
mttFreeze();
//unsetMTTSeqNo();System.out.println("colamtct:"+colamtct);
setMTTRefNumber();
//                getMttSeqNo();
validateClosedMttNumber(validationDetails);
validateMttRefNumber (validationDetails);
unsetMTTFields();
newMttNumber();
invalidMTTNumber(validationDetails);
}
catch(Exception e){
e.printStackTrace();
}
try {
mttcommenceDate(validationDetails);
mttforeignExchange(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
try {
System.out.println(" inside warning ShippingCollections ");
getShiipingWarning(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
}

if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CRE")) {
//cAHR
String tranportDate = getWrapper().getTRADOCDT().trim();
//          String eventStartdate=getDriverWrapper().getEventFieldAsText("RVD", "d", "");
String received=getDriverWrapper().getEventFieldAsText("RVD", "d", "");
String noofbills=getDriverWrapper().getEventFieldAsText("cANT", "s", "");
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Calendar cal = Calendar.getInstance();
try {
cal.setTime(sdf.parse(tranportDate));
cal.add(Calendar.DATE, 21);
getPane().setLOANDATE(sdf.format(cal.getTime()));
System.out.println("Transport Document date: " +sdf.format(cal.getTime()));

String output = sdf.format(cal.getTime());
java.util.Date tranDate = sdf.parse(output);
java.util.Date startDate = sdf.parse(received);
java.util.Date receivedDate = sdf.parse(received);
//System.out.println("date Remmitance: " + remitDate);
if(startDate.after(tranDate)) {
getPane().setLATEDPR("YES");
}
else {
getPane().setLATEDPR("");
}

long timeDiff=Math.abs(receivedDate.getTime()-tranDate.getTime());
long daysDiff=TimeUnit.DAYS.convert(timeDiff,TimeUnit.MILLISECONDS);
System.out.println("The of days between dates "+daysDiff);

BigDecimal daysDiffdb=new BigDecimal(daysDiff);
BigDecimal noofbillsbd=new BigDecimal(noofbills);
BigDecimal value=new BigDecimal(91.5);
BigDecimal quarter=daysDiffdb.divide(value,RoundingMode.UP);
BigDecimal charges= (quarter.multiply(noofbillsbd));
getPane().setDELAYEDS(charges.toString());
System.out.println("The of days quarter "+quarter+" "+charges);

}
catch (Exception e) {
e.printStackTrace();
}
try {
getFcctDetails(validationDetails);
System.out.println("fcct details:");
getCountryRisk();
}
catch (Exception e) {
e.printStackTrace();
//    System.out.println("fcct data:"+e);

}
try {
getUidWarning(validationDetails);
}
catch (Exception e) {
e.printStackTrace();
//    System.out.println("fcct data:"+e);

}
try {
getRapayAmt(validationDetails);
}catch (Exception e) {
e.printStackTrace();
}

try {
getPostingBranch(validationDetails);
}catch (Exception e) {
e.printStackTrace();
}
}

}
}



private String getString(int i) {
// TODO Auto-generated method stub
return null;
}

private int getInt(int i) {
// TODO Auto-generated method stub
return 0;
}

public BigDecimal divideAmountWithCurrencyval(BigDecimal val, String Currency) {
String convertedAmount = "0";
try {
convertedAmount = getDriverWrapper().convertFromToDBFormat(val.toString(), Currency, "F");
// Loggers.general().info(LOG,"Converted Amount After Decimal values " +
// convertedAmount);
val = new BigDecimal(convertedAmount);
// Loggers.general().info(LOG,"Converted Amount After Decimal values in
// BigDecimal " + val);
} catch (Exception e) {
Loggers.general().info(LOG,e.getMessage());
e.printStackTrace();
}
return val;
}


private double getdraftAmt(String getdraft1Val) {
double value = 0.0;
// //Loggers.general().info(LOG,"getdraft1Val" + getdraft1Val);
if (getdraft1Val.equals("") || getdraft1Val == null || getdraft1Val.length() == 0
|| getdraft1Val.length() == 3) {
// //Loggers.general().info(LOG,getdraft1Val + "getdraft1Val");

value = 0.0;
} else {
String getrvalue = (getdraft1Val.substring(0, getdraft1Val.length() - 3)).replace(",", "");
value = Double.parseDouble(getrvalue);
}

return (value);

}
                                                      }