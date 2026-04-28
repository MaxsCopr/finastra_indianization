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

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventPrePurchaseOrder;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class OdcFEC extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(OdcFEC.class);
      Connection con, con1 = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst, ship_prepare,shp = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ship_result,shpr = null;

      public boolean onPostInitialise() {

            // GETTING LOB
            // Double dmsst=0.00;
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
                  
                  // Arunkumar M 14-07-2021
                  if (getMajorCode().equalsIgnoreCase("FEC")) {
                        Connection conc = null;
                        ResultSet result = null;
                        PreparedStatement pst = null;
                        String subprodcode = "";
                        try {
                              String mainMasterReference = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                              conc = ConnectionMaster.getConnection();
                              String query = "SELECT PT.CODE FROM MASTER MAS, PRODTYPE PT WHERE MAS.PRODTYPE = PT.KEY97 AND TRIM(MAS.MASTER_REF)='" + mainMasterReference + "'";
                              System.out.println(query);
                              pst = conc.prepareStatement(query);
                              result = pst.executeQuery();
                              while(result.next()) {
                                    subprodcode = result.getString(1);
                              }
                        } catch(Exception e) {
                              e.printStackTrace();
                        } finally {
                              ConnectionMaster.surrenderDB(conc, pst, result);
                        }
                        getPane().setSPRODTYP(subprodcode);
                  }
                  // Arunkumar M 14-07-2021
                  
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
                  // // SFMS
                  try {

                        // String val = "SFMS";
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSFSAADJclayHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                        // ISB
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSFSAAMDclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSFSACREclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSFSAREPclayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  // // Purchase order
                  try {

                        // String val = "SFMS";
                        String getPurchase = getPurchaseDetails();
                        
                        
                        ExtendedHyperlinkControlWrapper purchase = getPane().getCtlPURCHASEFSACREclayHyperlink();
                        purchase.setUrl(getPurchase);
                        
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTFSAAMDclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMCHECKLISTFSACREclayHyperlink();
//                      csmreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMCHECKLISTFSAADJclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }


                  //For CR-143 Limit Nodes
                  
                  try{
                        String HyperLimitNode = getLimitNode().trim();
//                      ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesFSACREclayHyperlink();
//                      limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesFSAADJclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                  }catch(Exception e){
                        System.out.println("For Limit Node"+e.getMessage());
                        
                  }
                  
                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALFSACREclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMREFRALFSAAMDclayHyperlink();
                        csmreftrack2.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMREFRALFSAADJclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKFSAAMDclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCCHECKFSACREclayHyperlink();
//                      cpcreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCCHECKFSAADJclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
//                      ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELFSACREclayHyperlink();
//                      cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCREFERELFSAAMDclayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCREFERELFSAADJclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // TST
                  try {

                        // //Loggers.general().info(LOG,"STP else--->" + stpcode);
                        String value = "";
                        String TSTHyper = Their(value);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTEXPCOLFECclayHyperlink();
                        dmsh.setUrl(TSTHyper);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTFSAADJclayHyperlink();
                        dmsh1.setUrl(TSTHyper);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTFSAAMDclayHyperlink();
                        dmsh2.setUrl(TSTHyper);
//                      ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTFSACREclayHyperlink();
//                      dmsh3.setUrl(TSTHyper);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTFSAREPclayHyperlink();
                        dmsh4.setUrl(TSTHyper);

                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTFSAPASTDUElayHyperlink();
                        dmsh5.setUrl(TSTHyper);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTFSAMAINlayHyperlink();
                        dmsh6.setUrl(TSTHyper);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTFSAMANULlayHyperlink();
                        dmsh7.setUrl(TSTHyper);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  try {

                        String Preshipment = getHyperPreshipment();
                        // //Loggers.general().info(LOG,"Preshipment URL - "+Preshipment);

                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentEXPCOLFECclayHyperlink();
                        dmsh.setUrl(Preshipment);
                        // //Loggers.general().info(LOG,"URL Set done");
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Catched exception in OdcFEC - " +
                        // e.getMessage());
                  }
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

//                      try {
//                            currencyCalc();
//                      } catch (Exception e) {
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());
//
//                            }
//                      }


                        // fxrate

                        if (getMinorCode().equalsIgnoreCase("FEC")) {
                              try {

                                    String Mast = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                                    String Evnt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                                    // String strPSID = post.valueString();

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"MasterReference-------->" + Mast);
                                          Loggers.general().info(LOG,"their Reference-------->" + Evnt);
                                          // Loggers.general().info(LOG,"PSID----------->" + strPSID);
                                    }
                                    con = getConnection();
                                    String query1 = "SELECT DISTINCT SELLEX99 FROM MASTER MAS,BASEEVENT BEV,BASEEVENT BEV1, RELITEM  REL,POSTING POS,POSTRULES POSR,FXRATE86 F86"
                                                + " WHERE MAS.KEY97=BEV.MASTER_KEY" + " AND BEV.KEY97=BEV1.ATTACHD_EV"
                                                + " AND BEV1.KEY97=REL.EVENT_KEY" + " AND REL.KEY97=POS.KEY97"
                                                + " AND POS.POSTINGTYP =  POSR.POSTINGTYP" + " AND  BEV1.EXEMPLAR  =  POSR.EV_TYPE"
                                                + " AND POSR.FX_RATECOD = F86.CODE53" + " AND MAS.CCY  = F86.CURREN49"
                                                + " AND MASTER_REF='" + Mast + "'" + " AND BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) ='" + Evnt + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Query Result for FX_Rate----------> " + query1);

                                    }

                                    Loggers.general().info(LOG,"Query Result for FX_Rate ------->" + query1);

                                    ps1 = con.prepareStatement(query1);

                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {

                                          String fx = rs1.getString(1);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Entering While Loop" + getMinorCode().equalsIgnoreCase("FEC"));
                                                Loggers.general().info(LOG,"FXrate in FEC" + fx);

                                          }

                                          getPane().setFX_RATE(fx);
                                    }

                              } catch (Exception e) {
                                    e.printStackTrace();
                                    Loggers.general().info(LOG,"Exception FX_Rate===>" + e.getMessage());

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
                        if (getMinorCode().equalsIgnoreCase("FEC")) {

                              String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                              String mainrefNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                              String mastcur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                              String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                              String billAmt = "";
                              String finAmt = "";
                              String marginAmt = "";
                              String balanceccy = "";

                              BigDecimal billAmount = new BigDecimal(0);
                              BigDecimal finAmount = new BigDecimal(0);
                              BigDecimal marginAmount = new BigDecimal(0);
                              try {

                                    String margin = getWrapper().getPMARGIN();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Margin percentage " + margin);

                                    }

                                    if (margin == null || margin.equalsIgnoreCase("")) {
                                          String finalVal = "0";
                                          try {
                                                con = getConnection();
                                                String queryResult = "SELECT BILLAMOUNT,TOTAL_FINANCE_AMOUNT FROM ETT_FINANCED_AMOUNT_VIEW WHERE MASTER_REF='"
                                                            + mainrefNumber + "'";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Query Result for Total financed amount " + queryResult);

                                                }
                                                ps1 = con.prepareStatement(queryResult);
                                                rs1 = ps1.executeQuery();
                                                while (rs1.next()) {

                                                      billAmt = rs1.getString(1);

                                                      finAmt = rs1.getString(2);

                                                }

                                                String query = "SELECT MARAMT,CURRENCY FROM ETT_MARGIN_AMOUNT_VIEW WHERE MASTER_REF='"
                                                            + mainrefNumber + "'";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Query Result for Total margin amount " + query);

                                                }
                                                ps1 = con.prepareStatement(query);
                                                rs1 = ps1.executeQuery();
                                                while (rs1.next()) {
                                                      marginAmt = rs1.getString(1);
                                                      balanceccy = rs1.getString(2);

                                                }

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Query Total bill amount====>" + queryResult);
                                                      Loggers.general().info(LOG,"Query Total financed amount====>" + queryResult);
                                                      Loggers.general().info(LOG,"Query Total Margin amount====>" + queryResult);

                                                }

                                                try {
                                                      billAmount = new BigDecimal(billAmt);
                                                } catch (Exception e) {
                                                      billAmount = new BigDecimal(0);
                                                }

                                                try {
                                                      finAmount = new BigDecimal(finAmt);
                                                } catch (Exception e) {
                                                      finAmount = new BigDecimal(0);

                                                }

                                                try {
                                                      marginAmount = new BigDecimal(marginAmt);
                                                } catch (Exception e) {
                                                      marginAmount = new BigDecimal(0);

                                                }

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"BigDecimal Total bill amount====>" + billAmount);
                                                      Loggers.general().info(LOG,"BigDecimal Total financed amount====>" + finAmount);
                                                      Loggers.general().info(LOG,"BigDecimal Total Margin amount====>" + marginAmount);

                                                }

                                                BigDecimal totalFinace = billAmount.subtract(finAmount);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Total value finace minus margin amount====>" + totalFinace);

                                                }
                                                BigDecimal totalAvailable = totalFinace.subtract(marginAmount);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Total net available amount====>" + totalAvailable);

                                                }
                                                ConnectionMaster connectionMaster = new ConnectionMaster();
                                                double divideByDecimal = connectionMaster.getDecimalforCurrency(mastcur);
                                                BigDecimal divideByBig = new BigDecimal(divideByDecimal);

                                                BigDecimal ValueBig = totalAvailable.divide(divideByBig);
                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);
                                                finalVal = diff.format(ValueBig);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Final value of net finance amount " + finalVal);

                                                }

                                                if ((marginAmount.compareTo(BigDecimal.ZERO) > 0)) {
                                                      getPane().setBALAMT(finalVal + "" + balanceccy);
                                                } else {
                                                      getPane().setBALAMT("");
                                                }

                                          } catch (Exception ex) {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Exception in net finance amount" + ex.getMessage());

                                                }

                                          }
                                    } else {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Margin percentage else" + margin);

                                          }
                                    }

                              } catch (Exception ex) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Net finance and drawee fields population" + ex.getMessage());

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

                              String facid = getWrapper().getFACLTYID().trim();

                              if (facid == null || facid.equalsIgnoreCase("")) {

                                    getPane().setINTERDET("");
                                    getPane().setTENO("");
                                    getPane().setOURS("");
                                    getPane().setLIBORAT("");
                              }
                        }

                        // Drawee name population
                        if (getMinorCode().equalsIgnoreCase("FEC")) {

                              String subrefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();

                              try {

                                    con = getConnection();
                                    //Modified By : ETT
                                    //Modified Date : 18-July-2020
                                    //Purpose : Rectified the production issue.
                                    //Change Description : Query changed to take date from master level instead of create event
                                    //Start changes- Remedy #INC000004023921
                                    //Old code
                                    /*String query1 = "select TO_CHAR(ext.SIGVALDT,'DD/MM/YY') from master mas,baseevent bas,extevent ext where mas.KEY97=bas.MASTER_KEY and bas.KEY97=ext.EVENT AND ext.SIGVALDT is not null and mas.MASTER_REF ='"
                                                + subrefNumber + "' and bas.REFNO_PFIX='CRE'";*/
                                    //New code
                                    String query1 = "select TO_CHAR(extm.SIGVALDT,'DD/MM/YY') from master mas ,extmaster extm where mas.key97=extm.master AND mas.MASTER_REF ='"+subrefNumber+"'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Notional due date population in finace exsiting collection " + query1);

                                    }
                                    ps1 = con.prepareStatement(query1);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment due
                                          // date
                                          // query");
                                          String paydt = rs1.getString(1);
                                          getPane().setSIGVALDT(paydt);
                                    }

                                    con = getConnection();
                                    String query = "select trim(part.ADDRESS1) from master mas,COLLMASTER col,PARTYDTLS part,BASEEVENT bas where mas.KEY97=col.KEY97 and mas.KEY97=bas.MASTER_KEY and col.DRAWEE_PTY = part.KEY97 and mas.MASTER_REF='"
                                                + subrefNumber + "' and bas.REFNO_PFIX='CRE'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Drawee name population in finace exsiting collection" + query);

                                    }
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {

                                          String draweeName = rs1.getString(1);
                                          getPane().setDRAWNAM(draweeName);
                                    }
                              }

                              catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Notional due date and drawee in finace exsiting collection " + e.getMessage());

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
                        }
                        
                        
                        //For CR_209 Starts
                        
                        if ((getMajorCode().equalsIgnoreCase("FSA")) && (getMinorCode().equalsIgnoreCase("PSA"))) {
                              System.out.println("PurchaseOrder Validation onpostinitialise --->");
                              
                                    
                                    List<ExtEventPrePurchaseOrder> liste = (List<ExtEventPrePurchaseOrder>) getWrapper()
                                                .getExtEventPrePurchaseOrder();
                                    if(liste.size()==0){
                                          System.out.println("Inside No");
                                          getPane().setPOATT("NO");
                                    }else if(liste.size()>0){
                                          System.out.println("Inside yes");
                                          getPane().setPOATT("YES");
                                    }
                                    
                        }
                        //For CR_209 Ends

                        if (getMinorCode().equalsIgnoreCase("CSA") || getMinorCode().equalsIgnoreCase("ASA")) {
                              getcustomerName();
                        }

                        try {
                              getPane().onRTGSEXPCOLFECclayButton();
                              getPane().ondisplayvalFSACREclayButton();
                              getPane().ondisplayvalFSAAMDclayButton();

                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,"LOB Catch----->" + ee.getMessage());

                        }

                        try {
                              if (getMajorCode().equalsIgnoreCase("FSA")) {
                                    getLobFSA();
                              } else if (!getMajorCode().equalsIgnoreCase("ODC")) {
                                    getLob();
                              }

                              if (getMinorCode().equalsIgnoreCase("FEC")) {
                                    getLOBCREATE();
                              }

                        } catch (Exception ee) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in lob code===>" + ee.getMessage());
                              }
                        }

                        String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                        String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                        String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                        String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                        String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");

                        // 27-12-2016----------------------------------------------------------------------

                        // getSubvention

                        try {
                              // //Loggers.general().info(LOG,"get value for Subvention");
                              getSubvention();
                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"getSubvention");
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }

                        // if (subproductCode.equalsIgnoreCase("PCR") ||
                        // subproductCode.equalsIgnoreCase("INA")
                        // || subproductCode.equalsIgnoreCase("HCA")) {
                        //
                        // if (getMajorCode().equalsIgnoreCase("FSA") &&
                        // getMinorCode().equalsIgnoreCase("CSA")) {
                        // getPane().onSUBVENFSACREclayButton();
                        // }
                        //
                        // if (getMajorCode().equalsIgnoreCase("FSA") &&
                        // getMinorCode().equalsIgnoreCase("JSA")) {
                        // getPane().onSUBVENFSAADJclayButton();
                        // }
                        // if (getMajorCode().equalsIgnoreCase("FSA") &&
                        // getMinorCode().equalsIgnoreCase("ASA")) {
                        // getPane().onSUBVENFSAAMDclayButton();
                        // }
                        // if (getMajorCode().equalsIgnoreCase("FSA") &&
                        // getMinorCode().equalsIgnoreCase("RSA")) {
                        // getPane().onSUBVENFSAREPclayButton();
                        // }
                        //
                        // }

                        // check list table disabled

                        String procode = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
                        // Margin amount and margin percentage validation
                        if (procode.equalsIgnoreCase("FSA") && (!procode.equalsIgnoreCase("ODC"))) {
                              // //Loggers.general().info(LOG,"Product type only in FSA--->" +
                              // procode);
                              try {
                                    String s = "";
                                    String Sa = "";
                                    String c = "";
                                    s = getDriverWrapper().getEventFieldAsText("B+PC", "s", "");
                                    Sa = getDriverWrapper().getEventFieldAsText("B+AN", "v", "m");
                                    c = getDriverWrapper().getEventFieldAsText("B+AN", "v", "c");
                                    if (Sa != null && Sa.length() > 0) {
                                          // //Loggers.general().info(LOG,"Sa " + Sa);
                                          // //Loggers.general().info(LOG,"currency------>>>>" + c);

                                          String t = s.substring(0, s.length() - 1);
                                          double d = 100.0 - Double.parseDouble(t);
                                          double k = Double.parseDouble(new DecimalFormat("##.##").format(d));
                                          if (k > 0 && Sa != null) {
                                                // //Loggers.general().info(LOG,"Margin val in
                                                // double------>>"
                                                // +
                                                // k);
                                                long k1 = (long) (k);
                                                String k2 = String.valueOf(k1);
                                                // //Loggers.general().info(LOG,"Margin val in
                                                // string------>>"
                                                // +
                                                // k2);
                                                getPane().setSEPERCN(k2);
                                                // float fullamtfol = Float.valueOf(Sa);
                                                // Float obj = new Float(fullamtfol);
                                                // //Loggers.general().info(LOG,"String value float--->"
                                                // +
                                                // obj);
                                                // long longval = obj.longValue();
                                                // //Loggers.general().info(LOG,"String value
                                                // longval--->" +
                                                // longval);
                                                // String finalval = String.valueOf(longval);

                                                // String stringLitersOfPetrol = "123.50";
                                                Float litersOfPetrol = Float.parseFloat(Sa);
                                                // //Loggers.general().info(LOG,litersOfPetrol);
                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);
                                                String LitersOf = diff.format(litersOfPetrol);
                                                // //Loggers.general().info(LOG,"liters of petrol before
                                                // putting
                                                // in editor : " + LitersOf);

                                                // //Loggers.general().info(LOG,"String finalval--->" +
                                                // LitersOf);
                                                getPane().setSEPRMAR(LitersOf + " " + c);
                                                getWrapper().setSEPRMAR(LitersOf + " " + c);
                                                // //Loggers.general().info(LOG,"margin amount
                                                // ----------------->>>" + LitersOf + c);
                                                /*
                                                 * getWrapper().setMARAMT(Sa);
                                                 * getWrapper().setMARAMTCurrency(c);
                                                 */
                                          }

                                    } else {
                                          // Loggers.general().info(LOG,"margin amount and percentage
                                          // is
                                          // blank");
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }

                        } else {
                              // Loggers.general().info(LOG,"Product type not FSA");
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
                        if (getMinorCode().equalsIgnoreCase("FEC")) {
                              try {

                                    getpenalRateFEC();
                              } catch (Exception ee) {
                                    Loggers.general().info(LOG,"get Penal Rate" + ee.getMessage());

                              }
                        }
                        try {

                              int penal=getpenalRate();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"get Penal Rate" + ee.getMessage());

                        }

                        String portval = getWrapper().getPORTCOD_Name();
                        // //Loggers.general().info(LOG,"Port Value---->" + portval);
                        if ((!portval.equalsIgnoreCase("")) && portval != null) {
                              try {
                                    // //Loggers.general().info(LOG,"hscode Value in try---->" +
                                    // hscodeval);
                                    String hyperValue = "SELECT trim(PNAME) FROM EXTPORTCO WHERE PCODE='" + portval + "'";
                                    // //Loggers.general().info(LOG,"port code query Value---->" +
                                    // hyperValue);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(hyperValue);
                                    rs = ps1.executeQuery();
                                    while (rs.next()) {
                                          String hsploy = rs.getString(1);
                                          // //Loggers.general().info(LOG,"port code description---->"
                                          // +
                                          // hsploy);
                                          getPane().setPORTDESC(hsploy);
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
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        } else {
                              // Loggers.general().info(LOG,"port code is empty");
                        }
                        try {
                              String rtgs = getWrapper().getPROREMT();
                              // String rtgspart = getWrapper().getRTGSPART();
                              if (getMajorCode().equalsIgnoreCase("FSA")
                                          && (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))
                                          && (subproductCode.equalsIgnoreCase("CBD") || subproductCode.equalsIgnoreCase("IVD")
                                                      || subproductCode.equalsIgnoreCase("FAC") || subproductCode.equalsIgnoreCase("RVF")
                                                      || subproductCode.equalsIgnoreCase("TDF"))) {

                                    getrtgsNeftNet();

                              } else {
                                    if (getMajorCode().equalsIgnoreCase("FSA")) {
                                          Loggers.general().info(LOG,"FSA subproductCode" + subproductCode);
                                    }
                              }
                        } catch (Exception e) {
                              if (getMajorCode().equalsIgnoreCase("FSA")) {
                                    Loggers.general().info(LOG,"Exception in getrtgsNeftNet" + e.getMessage());
                              }
                        }

                        if (getMajorCode().equalsIgnoreCase("ODC")) {
                              getPane().getExtEventLoanDetailsUp().setEnabled(false);
                              getPane().getExtEventLoanDetailsDown().setEnabled(false);
                              getPane().getExtEventLoanDetailsNew().setEnabled(false);
                              getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                              getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                        }
                  }

            }
            // //Loggers.general().info(LOG,"onPostInitialise called in OdcFEC end");
            return false;

      }

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
                  // try {
                  // // //Loggers.general().info(LOG,"Calling Button Action in validate");
                  // // getPane().onpurchasedetailsFSACREclayButton();
                  // // getPane().onpurchaseorderFSAADJclayButton();

                  // FCY Tax calculation
                  try {

                        getFCCTCALCULATION();

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }

                  if (step_csm.equalsIgnoreCase("CBS Authoriser")) {
                        getPane().onFETCHLOANEXPCOLFECclayButton();
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

                  // // SFMS
                  try {

                        getPane().onRTGSEXPCOLFECclayButton();
                        getPane().ondisplayvalFSACREclayButton();
                        getPane().ondisplayvalFSAAMDclayButton();
                        getPane().onFETCHLOANEXPCOLFECclayButton();

                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSFSAADJclayHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                        // ISB
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSFSAAMDclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSFSACREclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSFSAREPclayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTFSAAMDclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMCHECKLISTFSACREclayHyperlink();
//                      csmreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMCHECKLISTFSAADJclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  //For CR-143 Limit Nodes
                  
                  try{
                        String HyperLimitNode = getLimitNode().trim();
//                      ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesFSACREclayHyperlink();
//                      limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesFSAADJclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                  }catch(Exception e){
                        System.out.println("For Limit Node"+e.getMessage());
                        
                  }
                  
                  
                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
//                      ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALFSACREclayHyperlink();
//                      csmreftrack.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMREFRALFSAAMDclayHyperlink();
                        csmreftrack2.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMREFRALFSAADJclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKFSAAMDclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCCHECKFSACREclayHyperlink();
//                      cpcreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCCHECKFSAADJclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
//                      ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELFSACREclayHyperlink();
//                      cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCREFERELFSAAMDclayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCREFERELFSAADJclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  if (getMinorCode().equalsIgnoreCase("FEC")) {

                        String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                        String mainrefNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                        String mastcur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                        String billAmt = "";
                        String finAmt = "";
                        String marginAmt = "";
                        String balanceccy = "";

                        BigDecimal billAmount = new BigDecimal(0);
                        BigDecimal finAmount = new BigDecimal(0);
                        BigDecimal marginAmount = new BigDecimal(0);
                        try {

                              try {
                                    double marginD1 = 0;
                                    double marginD2 = 0;
                                    double hundVal = 100;
                                    String marginP1 = getWrapper().getPMARGIN();
                                    if (marginP1 != null && marginP1.length() > 0) {
                                          marginD1 = Double.valueOf(marginP1);
                                    }
                                    String marginval = getDriverWrapper().getEventFieldAsText("B+PC", "s", "").trim();

                                    if (marginval.length() > 0) {
                                          String marginP2 = marginval.replaceAll("%", "");
                                          marginD2 = Double.valueOf(marginP2);
                                    }
                                    double totalMargin = marginD1 + marginD2;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"CIF margin value" + marginP1);
                                          Loggers.general().info(LOG,"Percentage value" + marginD2);
                                          Loggers.general().info(LOG,"Total margin value" + totalMargin);
                                          Loggers.general().info(LOG,"Margin value in hundred" + hundVal);

                                    }

                                    if (marginD1 > 0 && totalMargin > hundVal && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Sum of finance percentage and margin percentage should not exceed 100 % [CM]");
                                    }

                              } catch (Exception ex) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception in Finance percentage and CIF maring exceed" + ex.getMessage());

                                    }

                              }

                              String margin = getWrapper().getPMARGIN();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Margin percentage " + margin);

                              }

                              if (margin == null || margin.equalsIgnoreCase("")) {
                                    String finalVal = "0";
                                    try {
                                          con = getConnection();
                                          String queryResult = "SELECT BILLAMOUNT,TOTAL_FINANCE_AMOUNT FROM ETT_FINANCED_AMOUNT_VIEW WHERE MASTER_REF='"
                                                      + mainrefNumber + "'";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Query Result for Total financed amount " + queryResult);

                                          }
                                          ps1 = con.prepareStatement(queryResult);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {

                                                billAmt = rs1.getString(1);

                                                finAmt = rs1.getString(2);

                                          }

                                          String query = "SELECT MARAMT,CURRENCY FROM ETT_MARGIN_AMOUNT_VIEW WHERE MASTER_REF='"
                                                      + mainrefNumber + "'";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Query Result for Total margin amount " + query);

                                          }
                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                marginAmt = rs1.getString(1);
                                                balanceccy = rs1.getString(2);

                                          }

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Query Total bill amount====>" + queryResult);
                                                Loggers.general().info(LOG,"Query Total financed amount====>" + queryResult);
                                                Loggers.general().info(LOG,"Query Total Margin amount====>" + queryResult);

                                          }

                                          try {
                                                billAmount = new BigDecimal(billAmt);
                                          } catch (Exception e) {
                                                billAmount = new BigDecimal(0);
                                          }

                                          try {
                                                finAmount = new BigDecimal(finAmt);
                                          } catch (Exception e) {
                                                finAmount = new BigDecimal(0);

                                          }

                                          try {
                                                marginAmount = new BigDecimal(marginAmt);
                                          } catch (Exception e) {
                                                marginAmount = new BigDecimal(0);

                                          }

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"BigDecimal Total bill amount====>" + billAmount);
                                                Loggers.general().info(LOG,"BigDecimal Total financed amount====>" + finAmount);
                                                Loggers.general().info(LOG,"BigDecimal Total Margin amount====>" + marginAmount);

                                          }

                                          BigDecimal totalFinace = billAmount.subtract(finAmount);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Total value finace minus margin amount====>" + totalFinace);

                                          }
                                          BigDecimal totalAvailable = totalFinace.subtract(marginAmount);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Total net available amount====>" + totalAvailable);

                                          }
                                          ConnectionMaster connectionMaster = new ConnectionMaster();
                                          double divideByDecimal = connectionMaster.getDecimalforCurrency(mastcur);
                                          BigDecimal divideByBig = new BigDecimal(divideByDecimal);

                                          BigDecimal ValueBig = totalAvailable.divide(divideByBig);
                                          DecimalFormat diff = new DecimalFormat("0.00");
                                          diff.setMaximumFractionDigits(2);
                                          finalVal = diff.format(ValueBig);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Final value of net finance amount " + finalVal);

                                          }

                                          if ((marginAmount.compareTo(BigDecimal.ZERO) > 0)) {
                                                getPane().setBALAMT(finalVal + "" + balanceccy);
                                          } else {
                                                getPane().setBALAMT("");
                                          }

                                    } catch (Exception ex) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Exception in net finance amount" + ex.getMessage());

                                          }

                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Margin percentage else" + margin);

                                    }
                              }

                        } catch (Exception ex) {
                              Loggers.general().info(LOG,"Net finance and drawee fields population" + ex.getMessage());
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

                        String facid = getWrapper().getFACLTYID().trim();

                        if (facid == null || facid.equalsIgnoreCase("")) {

                              getPane().setINTERDET("");
                              getPane().setTENO("");
                              getPane().setOURS("");
                              getPane().setLIBORAT("");
                        }
                  }

                  if (getMinorCode().equalsIgnoreCase("FEC") || getMajorCode().equalsIgnoreCase("FSA")) {
                        try {

                              String customera = "";
                              if (getMajorCode().equalsIgnoreCase("FSA")) {
                                    customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no").trim();
                              } else if (getMinorCode().equalsIgnoreCase("FEC")) {
                                    customera = getDriverWrapper().getEventFieldAsText("B+DB", "p", "no").trim();
                              }

                              String tenor1 = getDriverWrapper().getEventFieldAsText("PTN", "s", "");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Tenor value====> " + tenor1);

                                    Loggers.general().info(LOG,"Product level customer====> " + customera);
                              }

                              try {
                                    String facid = getWrapper().getFACLTYID().trim();
                                    con = getConnection();
                                    String query = "select trim(TENOR) from CUSTOMERMARGIN WHERE CIF='" + customera
                                                + "' and FACILITY='" + facid + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"custom tenor value===>" + query);
                                    }
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment due
                                          // date
                                          // query");
                                          String tenor2 = rs1.getString(1).trim();
                                          double tenorval = Double.valueOf(tenor1);

                                          double tenorval2 = Double.valueOf(tenor2);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Tenor value in double ====> " + tenorval);
                                                Loggers.general().info(LOG,"custom tenor value double===>" + tenorval2);
                                          }

                                          if (facid.length() > 0 && tenor1.length() > 0 && tenorval > tenorval2
                                                      && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Tenor Period greater than the customer facility tenor [CM]");
                                          } else {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Tenor Period and customer facility tenor else===>" + tenorval
                                                                  + "tenorval2" + tenorval2);
                                                }
                                          }

                                    }

                              } catch (Exception ex) {
                                    Loggers.general().info(LOG,"Exception Custom tenor value===>" + ex.getMessage());
                              }

                              try {

                                    double actualval = 0;
                                    double spread1 = 0;
                                    double base1 = 0;
                                    double teno1 = 0;
                                    double penal1 = 0;
                                    try {
                                          String actual = getDriverWrapper().getEventFieldAsText("B+ACTR", "s", "");
                                          actualval = Double.valueOf(actual);
                                    } catch (Exception e) {
                                          actualval = 0;
                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Actual interest rate===>" + actualval);
                                    }

                                    try {
                                          String spread = getWrapper().getINTERDET();
                                          spread1 = Double.valueOf(spread);
                                    } catch (Exception e) {
                                          spread1 = 0;
                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Spread rate===>" + spread1);
                                    }

                                    try {
                                          String base = getWrapper().getLIBORAT();
                                          base1 = Double.valueOf(base);
                                    } catch (Exception e) {
                                          base1 = 0;
                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"base rate===>" + base1);
                                    }

                                    try {
                                          String teno = getWrapper().getTENO();
                                          teno1 = Double.valueOf(teno);
                                    } catch (Exception e) {
                                          teno1 = 0;
                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"penal rate===>" + teno1);
                                    }

                                    try {
                                          String penal = getWrapper().getPENALRAT();
                                          penal1 = Double.valueOf(penal);
                                    } catch (Exception e) {
                                          penal1 = 0;
                                    }
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Actual penal rate===>" + penal1);
                                    }

                                    if (penal1 > 0 && teno1 > 0 && teno1 != penal1 && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "CIF margin penal Interest is equal to Actual/Penal Rate [CM]");
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"CIF margin penal Interest is equal to Actual/Penal Rate else===>"
                                                            + penal1 + "tenorval2" + teno1);
                                          }
                                    }

                                    double totalVal = base1 + spread1;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Total value of spread and base===>" + totalVal);
                                    }
                                    if (totalVal > 0 && actualval > 0 && totalVal != penal1 && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "CIF margin spread and base rate should be equal to Actual rate [CM]");
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"CIF margin spread and base rate is equal to Actual rate else===>"
                                                            + totalVal + "actualval" + actualval);
                                          }
                                    }

                              } catch (Exception ex) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception Custom interest and Penal validation===>" + ex.getMessage());
                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception Customer Tenor validation " + e.getMessage());
                              }
                        }
                  }
                  // Drawee name population
                  if (getMinorCode().equalsIgnoreCase("FEC")) {

                        String subrefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();

                        try {

                              con = getConnection();
                              /*String query1 = "select TO_CHAR(ext.SIGVALDT,'DD/MM/YY') from master mas,baseevent bas,extevent ext where mas.KEY97=bas.MASTER_KEY and bas.KEY97=ext.EVENT AND ext.SIGVALDT is not null and mas.MASTER_REF ='"
                                          + subrefNumber + "' and bas.REFNO_PFIX='CRE'";
*/
                              String query1 = "select TO_CHAR(extm.SIGVALDT,'DD/MM/YY') from master mas ,extmaster extm where mas.key97=extm.master AND mas.MASTER_REF ='"+subrefNumber+"'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Notional due date population in finace exsiting collection " + query1);

                              }
                              ps1 = con.prepareStatement(query1);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while Payment due
                                    // date
                                    // query");
                                    String paydt = rs1.getString(1);
                                    getPane().setSIGVALDT(paydt);
                              }

                              con = getConnection();
                              String query = "select trim(part.ADDRESS1) from master mas,COLLMASTER col,PARTYDTLS part,BASEEVENT bas where mas.KEY97=col.KEY97 and mas.KEY97=bas.MASTER_KEY and col.DRAWEE_PTY = part.KEY97 and mas.MASTER_REF='"
                                          + subrefNumber + "' and bas.REFNO_PFIX='CRE'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Drawee name population in finace exsiting collection" + query);

                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {

                                    String draweeName = rs1.getString(1);
                                    getPane().setDRAWNAM(draweeName);
                              }
                        }

                        catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Notional due date and drawee in finace exsiting collection " + e.getMessage());

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
                  }
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  if (getMinorCode().equalsIgnoreCase("CSA") || getMinorCode().equalsIgnoreCase("ASA")) {
                        getcustomerName();

                        if (subproductCode.equalsIgnoreCase("CBD") || subproductCode.equalsIgnoreCase("IVD")
                                    || subproductCode.equalsIgnoreCase("FAC") || subproductCode.equalsIgnoreCase("RVF")
                                    || subproductCode.equalsIgnoreCase("TDF")) {
                              try {
                                    double graceVal = 0;
                                    double ContractVal = 0;
                                    try {
                                          String grace = getWrapper().getGRACEPER();
                                          graceVal = Double.valueOf(grace);
                                    } catch (Exception e) {
                                          graceVal = 0;
                                    }

                                    try {
                                          String Contract = getWrapper().getINTPERCE();
                                          ContractVal = Double.valueOf(Contract);
                                    } catch (Exception e) {
                                          ContractVal = 0;
                                    }
                                    double totalval = graceVal + ContractVal;
                                    String tenor = getDriverWrapper().getEventFieldAsText("PTN", "s", "");
                                    double tenorVal = Double.valueOf(tenor);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,"Grace period===> " +
                                          // graceVal);
                                          // Loggers.general().info(LOG,"Contract period===> " +
                                          // ContractVal);
                                          Loggers.general().info(LOG,"Contract+Grace period===> " + totalval);

                                          Loggers.general().info(LOG,"Tenor period===> " + tenorVal);

                                    }

                                    if (totalval > 0 && tenor.length() > 0 && totalval != tenorVal
                                                && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Entered Period is not equal to the sum of grace and Contract period [CM]");
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"tenor period===> " + tenorVal + "total value" + totalval);
                                          }
                                    }

                                    try {
                                          String grace = getWrapper().getGRACEPER();
                                          String dueDat = null;
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"grace period value===> " + grace);
                                          }
                                          if (getMajorCode().equalsIgnoreCase("FSA") && getMinorCode().equalsIgnoreCase("CSA")) {
                                                dueDat = getDriverWrapper().getEventFieldAsText("B+PD", "d", "");
                                          } else if (getMajorCode().equalsIgnoreCase("FSA")
                                                      && getMinorCode().equalsIgnoreCase("ASA")) {

                                                dueDat = getDriverWrapper().getEventFieldAsText("AMI:B+MD", "d", "");
                                          }
                                          if (grace != null && grace.length() > 0 && dueDat != null) {

                                                String graveVal = "-" + grace;
                                                int graveValue = Integer.parseInt(graveVal);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"grace period int===> " + graveValue);
                                                }
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(sdf.parse(dueDat));
                                                c.add(Calendar.DATE, graveValue);
                                                // //Loggers.general().info(LOG,"DATE 1"+ c);
                                                String output = sdf.format(c.getTime());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"grace period minus due date===> " + output);
                                                }
                                                //getPane().setACTDUE(output);

                                          } else {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"grace period ===> " + grace + "due date" + dueDat);
                                                }
                                          }
                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Actual due date===> " + e.getMessage());
                                          }
                                    }

                              } catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception Grace period and float rate " + e.getMessage());

                                    }
                              }
                        }

                  }

                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001

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
                        if (count < 1 && step_Input.equalsIgnoreCase("i")
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("AdhocCSM"))
                                    && (getMinorCode().equalsIgnoreCase("ASA") || getMinorCode().equalsIgnoreCase("CSA")
                                                || getMinorCode().equalsIgnoreCase("JSA"))
                                    && !getMinorCode().equalsIgnoreCase("FEC") && !evnt.equalsIgnoreCase("FEC")) {

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
                  // Setting the PreShipemnt LInk

                  try {

                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentEXPCOLFECclayHyperlink();
                        dmsh.setUrl(Preshipment);
                        // //Loggers.general().info(LOG,"URL Set done");

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception in setting the PreShipment Link
                        // " + e.getMessage());
                  }

                  try {

                        try {
                              double marginD1 = 0;
                              double marginD2 = 0;
                              double hundVal = 100;
                              String marginP1 = getWrapper().getPMARGIN();
                              if (marginP1 != null && marginP1.length() > 0) {
                                    marginD1 = Double.valueOf(marginP1);
                              }
                              String marginval = getDriverWrapper().getEventFieldAsText("B+PC", "s", "");

                              if (marginval.length() > 0) {
                                    String marginP2 = marginval.replaceAll("%", "");
                                    marginD2 = Double.valueOf(marginP2);
                              }
                              double totalMargin = marginD1 + marginD2;
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"CIF margin value" + marginP1);
                                    Loggers.general().info(LOG,"Percentage value" + marginD2);
                                    Loggers.general().info(LOG,"Total margin value" + totalMargin);
                                    Loggers.general().info(LOG,"Margin value in hundred" + hundVal);

                              }

                              if ((subproductCode.equalsIgnoreCase("CBD") || subproductCode.equalsIgnoreCase("IVD")
                                          || subproductCode.equalsIgnoreCase("FAC") || subproductCode.equalsIgnoreCase("RCF")
                                          || subproductCode.equalsIgnoreCase("TDF") || subproductCode.equalsIgnoreCase("POF"))
                                          && getMinorCode().equalsIgnoreCase("CSA")) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Margin value in FSA create" + getMinorCode().equalsIgnoreCase("CSA"));

                                    }

                                    if (marginD1 > 0 && totalMargin > hundVal && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addError(ErrorType.Other,
                                                      "Sum of finance percentage and margin percentage should not exceed 100 % [CM]");
                                    }
                              } else if ((subproductCode.equalsIgnoreCase("CBD") || subproductCode.equalsIgnoreCase("IVD")
                                          || subproductCode.equalsIgnoreCase("FAC") || subproductCode.equalsIgnoreCase("RCF")
                                          || subproductCode.equalsIgnoreCase("TDF") || subproductCode.equalsIgnoreCase("POF"))
                                          && getMinorCode().equalsIgnoreCase("ASA")) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Margin value in FSA amend" + getMinorCode().equalsIgnoreCase("ASA"));

                                    }
                                    double marginDAmd = 0;
                                    try {
                                          String marginvalue = getDriverWrapper().getEventFieldAsText("AMM:B+PC", "s", "");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Margin value in FSA marginvalue" + marginvalue);

                                          }
                                          if (marginvalue.length() > 0) {
                                                String marginAmd = marginvalue.replaceAll("%", "");
                                                marginDAmd = Double.valueOf(marginAmd);
                                          }
                                    } catch (Exception e) {
                                          marginDAmd = 0;
                                    }

                                    double totalMargin1 = marginD1 + marginDAmd;

                                    if (marginD1 > 0 && totalMargin1 > hundVal && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addError(ErrorType.Other,
                                                      "Sum of finance percentage and margin percentage should not exceed 100 % [CM]");
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Margin value in FSA marginvalue in else" + totalMargin1);

                                          }
                                    }

                              } else {
                                    if (marginD1 > 0 && totalMargin > hundVal && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Sum of finance percentage and margin percentage should not exceed 100 % [CM]");
                                    }

                              }

                        } catch (Exception ex) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in Finance percentage and CIF margin exceed" + ex.getMessage());

                              }

                        }

                        String facid = getWrapper().getFACLTYID().trim();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Facility id for FSA" + facid);
                        }
                        if (facid == null || facid.equalsIgnoreCase("")) {

                              getPane().setINTERDET("");
                              getPane().setTENO("");
                              getPane().setOURS("");
                              getPane().setLIBORAT("");
                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in Finance percentage and CIF margin FSA" + e.getMessage());
                        }
                  }

                  String portval = getWrapper().getPORTCOD_Name();
                  // //Loggers.general().info(LOG,"Port Value---->" + portval);
                  if ((!portval.equalsIgnoreCase("")) && portval != null) {
                        try {
                              // //Loggers.general().info(LOG,"hscode Value in try---->" +
                              // hscodeval);
                              String hyperValue = "SELECT trim(PNAME) FROM EXTPORTCO WHERE PCODE='" + portval + "'";
                              // //Loggers.general().info(LOG,"port code query Value---->" +
                              // hyperValue);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(hyperValue);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);
                                    // //Loggers.general().info(LOG,"port code description---->"
                                    // +
                                    // hsploy);
                                    getPane().setPORTDESC(hsploy);
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

                  // Advance Table Validations
                  String customera = getDriverWrapper().getEventFieldAsText("DBT", "p", "no").trim();
                  if (getMajorCode().equalsIgnoreCase("FSA") && (getMinorCode().equalsIgnoreCase("CSA")||getMinorCode().equalsIgnoreCase("JSA"))||getMinorCode().equalsIgnoreCase("PSA")) {
                        Loggers.general().info(LOG,"PurchaseOrder Validation --->");
                        try {
                              String cusNo = "";
                              List<ExtEventPrePurchaseOrder> liste = (List<ExtEventPrePurchaseOrder>) getWrapper()
                                          .getExtEventPrePurchaseOrder();
                              //For CR_209 Starts
                              if(getMinorCode().equalsIgnoreCase("PSA")){
                                    
                                    if(liste.size()==0){
                                          getPane().setPOATT("NO");
                                          getWrapper().setPOATT("NO");
                                    }else if(liste.size()>0){
                                          getPane().setPOATT("YES");
                                          getWrapper().setPOATT("YES");
                                    }
                                    
                              }
                              //For CR_209 Ends
                              
                              for (int a = 0; a < liste.size(); a++) {
                                    ExtEventPrePurchaseOrder adve = liste.get(a);
                                    cusNo = adve.getCRNO().trim();
                                    String ponumner = adve.getPON().trim();
                                    // //Loggers.general().info(LOG,"PurchaseOrder Validation in
                                    // customer--->" + cusNo + "" + customera);
                                    if (liste.size() > 0 && (!cusNo.equalsIgnoreCase(customera)) && (!cusNo.equalsIgnoreCase(""))
                                                && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          
                                          //For CR_209 Starts
                                          if(getMinorCode().equalsIgnoreCase("PSA")){
                                                validationDetails.addError(ErrorType.Other,
                                                            "Purchase Order CIF ID is not same as the Customer [CM]");
                                          }else
                                          {validationDetails.addWarning(WarningType.Other,
                                                      "Purchase Order CIF ID is not same as the Customer [CM]");
                                          }
                                          
                                          //For CR_209 Ends

                                    }

                                    String query2 = "select count(*) from ett_export_order WHERE EXPORT_ORDER_NUMBER='" + ponumner
                                                + "'";
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query2);
                                    // Loggers.general().info(LOG,"Query value for current currency"
                                    // +
                                    // query1);
                                    rs = ps.executeQuery();
                                    int count1 = 0;
                                    // //Loggers.general().info(LOG,"result of query "+rs2);
                                    while (rs.next()) {
                                          count1 = rs.getInt(1);
                                          if ((liste.size() > 0) && (count1 == 0 || count1 < 1)
                                                      && getMinorCode().equalsIgnoreCase("CSA") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Please enter Valid Purchase Order number (" + ponumner + ") [CM]");
                                          }

                                    }

                                    String query3 = "select count(*) from ett_export_order WHERE EXPORT_ORDER_NUMBER='" + ponumner
                                                + "' AND STATUS='A' ";
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query3);
                                    // Loggers.general().info(LOG,"Query value for current currency"
                                    // +
                                    // query1);
                                    rs = ps.executeQuery();
                                    int count2 = 0;
                                    // //Loggers.general().info(LOG,"result of query "+rs2);
                                    while (rs.next()) {
                                          count2 = rs.getInt(1);
                                          if ((liste.size() > 0) && (count2 == 0 || count2 < 1)
                                                      && getMinorCode().equalsIgnoreCase("CSA") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Please enter Authorized Purchase Order number (" + ponumner + ") [CM]");
                                          }

                                    }

                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception is PurchaseOrder Validation
                              // in customer" + e.getMessage());
                        }
                  } else {
                        // Loggers.general().info(LOG,"PurchaseOrder create getMajorCode()--->"
                        // + getMajorCode());
                  }

                  // port code population
                  String stpId = getDriverWrapper().getEventFieldAsText("CSID", "s", "");

                  String protype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  String running = getDriverWrapper().getEventFieldAsText("PUO1", "s", "");
                  // PCF,PCR,POF
                  // PUO3

                  // purchase order validation
                  try {

                        DecimalFormat diff = new DecimalFormat("0.00");
                        diff.setMaximumFractionDigits(2);

                        BigDecimal finAmt = new BigDecimal(0);
                        BigDecimal utlizationDividedAmount = new BigDecimal("0");
                        BigDecimal currentCurAmount = new BigDecimal("0");
                        BigDecimal utlizationtotalVal = new BigDecimal("0");
                        BigDecimal currentCurValue = new BigDecimal("0");
                        BigDecimal utlizationDividedValue = new BigDecimal("0");
                        BigDecimal finAmount = new BigDecimal("0");

                        try {
                              String amount_str = getDriverWrapper().getEventFieldAsText("B+AF", "v", "m");

                              finAmt = new BigDecimal(amount_str);
                              finAmount = finAmt.setScale(2, RoundingMode.HALF_UP);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("Finance amount String===>" + amount_str);
                                    Loggers.general().info(LOG,"Finance amount Bigdecimal===>" + finAmount);
                              }
                        } catch (Exception e) {
                              finAmt = new BigDecimal(0);
                        }

                        try {

                              List<ExtEventPrePurchaseOrder> ExtEventFixedeef = (List) getWrapper().getExtEventPrePurchaseOrder();
                              for (int j = 0; j < ExtEventFixedeef.size(); j++) {
                                    ExtEventPrePurchaseOrder eventFixedefg = (ExtEventPrePurchaseOrder) ExtEventFixedeef.get(j);

                                    String utlizamt = eventFixedefg.getUFINAMT().toString();
                                    
                                    if((Integer.parseInt(utlizamt)<0)){
                                          validationDetails.addError(ErrorType.Other,
                                                      "PO Finance amount is negative [CM]");
                                    }else{
                                    BigDecimal utlizAmount = new BigDecimal(utlizamt);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Utilization amount String===>" + utlizamt);
                                    }
                                    String utlizcur = eventFixedefg.getUFINAMTCurrency();
                                    ConnectionMaster connectionMaster = new ConnectionMaster();
                                    double divideByDecimal = connectionMaster.getDecimalforCurrency(utlizcur);
                                    BigDecimal divideInvcurBig = new BigDecimal(divideByDecimal);
                                    BigDecimal utlizationDividedAmt = utlizAmount.divide(divideInvcurBig);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Utilization amount after divid===>" + utlizationDividedAmt);
                                    }
                                    utlizationDividedAmount = utlizationDividedAmount.add(utlizationDividedAmt);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("utlizationDividedAmount amount in final===>" + utlizationDividedAmount);
                                    }
                                    utlizationDividedValue = utlizationDividedAmount.setScale(2, RoundingMode.HALF_UP);
                                    String curnvamt = eventFixedefg.getCURRFAMT().toString();
                                    BigDecimal curnvamount = new BigDecimal(curnvamt);
                                    String currentcurr = eventFixedefg.getCURRFAMTCurrency();
                                    // //Loggers.general().info(LOG,"Current amount initially----> "
                                    // +
                                    // curnvamt);

                                    // amm = new BigDecimal(am);
                                    // Loggers.general().info(LOG,"finance amount after get
                                    // original----> " + am);
                                    double dividecurrentcurr = connectionMaster.getDecimalforCurrency(currentcurr);
                                    BigDecimal dividecurrentCur = new BigDecimal(dividecurrentcurr);
                                    BigDecimal currentCurAmt = curnvamount.divide(dividecurrentCur);
                                    currentCurAmount = currentCurAmount.add(currentCurAmt);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Current amount in final===>" + currentCurAmount);
                                    }

                                    String query = "select trim(SPOTRATE) as SPOTRATE from spotrate where currency='" + utlizcur
                                                + "'" +" and BRANCH='MBWW'";
                                    
                                          System.out.println("Utilization amount query===>" + query);
                                    
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query);

                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          double currency = rs.getDouble(1);
                                          BigDecimal currencyVal = new BigDecimal(currency);
                                          BigDecimal utlizationAmount = utlizationDividedAmount.multiply(currencyVal);
                                          
                                                System.out.println("Utilization amount query value===>" + utlizationAmount);
                                          
                                          utlizationtotalVal = utlizationAmount.setScale(2, RoundingMode.HALF_UP);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Utilization amount query in final===>" + utlizationtotalVal);
                                          }
                                    }
                                    // Loggers.general().info(LOG,"Final finace amount for
                                    // validation-----> " + finamt);

                                    String query1 = "select trim(SPOTRATE) as SPOTRATE from spotrate where currency='" + currentcurr
                                                + "'" +" and BRANCH='MBWW'";
                                    
                                          System.out.println("Current amount query===>" + query1);
                                    
                                    ps = con.prepareStatement(query1);

                                    rs = ps.executeQuery();
                                    // //Loggers.general().info(LOG,"result of query "+rs2);
                                    while (rs.next()) {
                                          double currentCurency = rs.getDouble(1);
                                          BigDecimal curencyValue = new BigDecimal(currentCurency);
                                          BigDecimal currentCurVal = currentCurAmount.multiply(curencyValue);
                                          
                                                System.out.println("current amount query value===>" + currentCurVal);
                                          
                                          currentCurValue = currentCurVal.setScale(2, RoundingMode.HALF_UP);
                              
                                          System.out.println("current amount query in final===>" + currentCurValue);
                                          
                                    }

                                    }
                              }

                              if ((protype.equalsIgnoreCase("PCF") || protype.equalsIgnoreCase("PCR")
                                          || protype.equalsIgnoreCase("POF"))) {
                                    if ((ExtEventFixedeef.size() == 0) && getMinorCode().equalsIgnoreCase("CSA")
                                                && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                && (running.equalsIgnoreCase("No"))) {
                                          validationDetails.addError(ErrorType.Other, "Purchase Order Details is mandatory [CM]");
                                    } else if (ExtEventFixedeef.size() > 0 && (running.equalsIgnoreCase("Yes"))
                                                && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Running flag is Yes,Purchase Order Details should not be enter [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"Purchase Order
                                          // Details"+running);
                                    }

                              } else {
                                    // Loggers.general().info(LOG,"protype---> " + protype);
                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("Comparison of master amount and finace amount" + finAmount + ">>>>>>"
                                                + utlizationtotalVal);
                              }
                              if (ExtEventFixedeef.size() > 0 && (utlizationDividedValue.compareTo(BigDecimal.ZERO) > 0)
                                          && (finAmt.compareTo(BigDecimal.ZERO) > 0)
                                          && (utlizationDividedValue.compareTo(finAmount) != 0) && (step_Input.equalsIgnoreCase("i"))) {
                                    // //Loggers.general().info(LOG,"Comparison of master amount in
                                    // if
                                    // loop");
                                    validationDetails.addError(ErrorType.Other,
                                                "Purchase order Finance amount should be equal to Finance amount [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Comparison of master amount and finace amount else" + finAmount
                                                      + ">>>>>>" + utlizationtotalVal);
                                    }
                              }
                              
                                    System.out.println("Comparison of currentCurValue amount and utlizationtotalVal amount"
                                                + currentCurValue + ">>>>>>" + utlizationtotalVal);
                              
                              if (ExtEventFixedeef.size() > 0 && (utlizationtotalVal.compareTo(BigDecimal.ZERO) > 0)
                                          && (currentCurValue.compareTo(BigDecimal.ZERO) > 0)
                                          && (utlizationtotalVal.compareTo(currentCurValue) > 0) && (step_Input.equalsIgnoreCase("i"))) {

                                    validationDetails.addError(ErrorType.Other,
                                                "Purchase order Finance amount cannot be greater than Current Finance Eligible Amount [CM]");
                              } else {
                                    
                                          System.out.println("Comparison of currentCurValue amount and utlizationtotalVal amount else"
                                                      + currentCurValue + ">>>>>>" + utlizationtotalVal);
                                    
                              }

                        } catch (Exception e) {
                              e.printStackTrace();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception Purchase order validation" + e.getMessage());
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

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Purchase order Finance" + e.getMessage());
                        }
                  }
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

                  String procode = getDriverWrapper().getEventFieldAsText("PCO", "s", "");

                  if (procode.equalsIgnoreCase("FSA") && (!procode.equalsIgnoreCase("ODC"))) {
                        // //Loggers.general().info(LOG,"Product type only in FSA--->" +
                        // procode);
                        try {
                              String s = "";
                              String Sa = "";
                              String c = "";
                              s = getDriverWrapper().getEventFieldAsText("B+PC", "s", "");
                              Sa = getDriverWrapper().getEventFieldAsText("B+AN", "v", "m");
                              c = getDriverWrapper().getEventFieldAsText("B+AN", "v", "c");
                              if (Sa != null && Sa.length() > 0) {
                                    // //Loggers.general().info(LOG,"Sa " + Sa);
                                    // //Loggers.general().info(LOG,"currency------>>>>" + c);

                                    String t = s.substring(0, s.length() - 1);
                                    double d = 100.0 - Double.parseDouble(t);
                                    double k = Double.parseDouble(new DecimalFormat("##.##").format(d));
                                    if (k > 0 && Sa != null) {
                                          // //Loggers.general().info(LOG,"Margin val in
                                          // double------>>"
                                          // +
                                          // k);
                                          long k1 = (long) (k);
                                          String k2 = String.valueOf(k1);
                                          // //Loggers.general().info(LOG,"Margin val in
                                          // string------>>"
                                          // +
                                          // k2);
                                          getPane().setSEPERCN(k2);
                                          
                                          Float litersOfPetrol = Float.parseFloat(Sa);
                                          // //Loggers.general().info(LOG,litersOfPetrol);
                                          DecimalFormat diff = new DecimalFormat("0.00");
                                          diff.setMaximumFractionDigits(2);
                                          String LitersOf = diff.format(litersOfPetrol);
                                          // //Loggers.general().info(LOG,"liters of petrol before
                                          // putting
                                          // in editor : " + LitersOf);

                                          // //Loggers.general().info(LOG,"String finalval--->" +
                                          // LitersOf);
                                          getPane().setSEPRMAR(LitersOf + " " + c);
                                          getWrapper().setSEPRMAR(LitersOf + " " + c);
                                          // //Loggers.general().info(LOG,"margin amount
                                          // ----------------->>>" + LitersOf + c);
                                          /*
                                           * getWrapper().setMARAMT(Sa);
                                           * getWrapper().setMARAMTCurrency(c);
                                           */
                                    }

                              } else {
                                    // Loggers.general().info(LOG,"margin amount and percentage is
                                    // blank");
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,e.getMessage());
                        }

                  } else {
                        // Loggers.general().info(LOG,"Product type not FSA");
                  }

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                        }
                  }           

                  // interest subvention validation
                  String elisub = getWrapper().getELISUB();
                  // //Loggers.general().info(LOG,"Eligible value in string " + elisub);
                  String subv = getWrapper().getINTPERE();
                  // //Loggers.general().info(LOG,"Interest percentage value " + subv);
                  if (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub.trim().equalsIgnoreCase("NO")) {
                        if ((!(subv.trim().equalsIgnoreCase(""))) && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "Subvention Eligible is Blank or No then Interest percentage should be blank  [CM]");
                        } else {
                              // Loggers.general().info(LOG,"Subvention Eligible is YES");
                        }
                  } else {
                        // Loggers.general().info(LOG,"Subvention Eligible is YES");
                  }

                  String Ifsc = getWrapper().getIFSCCO_Name().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);
                  if (Ifsc.trim().equalsIgnoreCase("") || Ifsc == null) {
                        // validationDetails.addWarning(WarningType.Other, "Receiver
                        // IFSC
                        // code should not be blank");
                  } else {
                        if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
                              try {
                                    con = getConnection();
                                    String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                                    // //Loggers.general().info(LOG,"query " + query);
                                    int count = 0;
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while");
                                          count = rs1.getInt(1);
                                          // //Loggers.general().info(LOG,"value of count in while
                                          // "+count);
                                    }

                                    if (count == 0 && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                          // //Loggers.general().info(LOG,"If in IFSC");
                                          validationDetails.addError(ErrorType.Other,
                                                      "Invali Beneficiary IFSC code(" + Ifsc + ")  [CM]");
                                    }
                                    // con.close();
                                    // ps1.close();
                                    // rs1.close();

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
                        }

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

                  // Charge Account Validation
                  String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD

                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  // CODE
                  // ILC

                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // //Loggers.general().info(LOG,"charge account collected " + chargecol);
                  String custval = "";
                  String cust = "";

                  if (prodtype.trim().equalsIgnoreCase("FSA") && (subproductCode.equalsIgnoreCase("CBD")
                              || subproductCode.equalsIgnoreCase("IVD") || subproductCode.equalsIgnoreCase("FAC")
                              || subproductCode.equalsIgnoreCase("RVF") || subproductCode.equalsIgnoreCase("TDF"))) {
                        cust = getDriverWrapper().getEventFieldAsText("B+FT", "p", "no").trim();// party
                        if (step_csm.equalsIgnoreCase("CBS Maker 1")) {
                              try {

                                    con = getConnection();

                                    String query = "select * from ETT_CUS_ACCT_SETTLE_FSA where MASTER_REF ='" + masterref
                                                + "' and EVENT_REF = '" + eventREF + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Account selected in Settlement query" + query);
                                    }
                                    dmsp = con.prepareStatement(query);

                                    dmsr = dmsp.executeQuery();

                                    while (dmsr.next()) {

                                          custval = dmsr.getString(4);

                                          if (custval.length() > 0 && chargecol.equalsIgnoreCase("Y")
                                                      && (!chargecol.equalsIgnoreCase("N"))) {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Account selected in Settlement if loop");
                                                }

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Account selected in Settlement does not belong to the customer (" + custval
                                                                        + ") [CM]");
                                          } else {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Account selected in Settlement else" + custval);
                                                }
                                          }

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
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        }

                  } else if ((prodtype.trim().equalsIgnoreCase("FSA") || getMinorCode().equalsIgnoreCase("FEC"))
                              && !(subproductCode.equalsIgnoreCase("CBD") || subproductCode.equalsIgnoreCase("IVD")
                                          || subproductCode.equalsIgnoreCase("FAC") || subproductCode.equalsIgnoreCase("RVF")
                                          || subproductCode.equalsIgnoreCase("TDF"))) {
                        cust = getDriverWrapper().getEventFieldAsText("DBT", "p", "no").trim();// party

                        try {
                              if (step_csm.equalsIgnoreCase("CBS Maker 1")) {

                                    con = getConnection();

                                    String query = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='" + masterref
                                                + "' and EVENT_REF = '" + eventREF + "'";
                                    dmsp = con.prepareStatement(query);

                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {

                                          custval = dmsr.getString(1);

                                          if (custval.length() > 0 && chargecol.equalsIgnoreCase("Y")
                                                      && (!chargecol.equalsIgnoreCase("N"))) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Account selected in Settlement does not belong to the customer [CM]");
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
                  }

                  if (prodtype.trim().equalsIgnoreCase("FSA") && (subproductCode.equalsIgnoreCase("CBD")
                              || subproductCode.equalsIgnoreCase("IVD") || subproductCode.equalsIgnoreCase("FAC")
                              || subproductCode.equalsIgnoreCase("RVF") || subproductCode.equalsIgnoreCase("TDF"))) {

                        try {
                              String custerVal = getDriverWrapper().getEventFieldAsText("DBT", "p", "f").trim();// debit
                                                                                                                                                            // party
                              String custerValue = getDriverWrapper().getEventFieldAsText("B+FT", "p", "f").trim();// finance

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Debit party in FSA===>" + custerVal);
                                    Loggers.general().info(LOG,"Finance party in FSA===>" + custerValue);
                              }

                              // party
                              String custerBuyer = getWrapper().getBUYERN_Name().trim();// party
                              String custerSell = getWrapper().getSELLERN_Name().trim();// party

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Buyer party in FSA===>" + custerBuyer);
                                    Loggers.general().info(LOG,"Seller party in FSA===>" + custerSell);
                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                              }
                              if (custerVal.length() > 0 && custerBuyer.length() > 0 && custerSell.length() > 0
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    if (!custerVal.equalsIgnoreCase(custerBuyer) && !custerVal.equalsIgnoreCase(custerSell)) {

                                          validationDetails.addWarning(WarningType.Other,
                                                      "Selected Debit party does not belong to Buyer name/Seller name [CM]");

                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Debit party does not belong to Buyer name/Seller name else");
                                          }
                                    }
                              }
                              if (custerValue.length() > 0 && custerBuyer.length() > 0 && custerSell.length() > 0
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {

                                    if (!custerValue.equalsIgnoreCase(custerBuyer) && !custerValue.equalsIgnoreCase(custerSell)) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Selected Finance party does not belong to Buyer name/Seller name [CM]");
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Finance party does not belong to Buyer name/Seller name else");
                                          }
                                    }

                              }

                              if ((custerValue.length() > 0 && custerBuyer.length() > 0)
                                          && (custerValue.equalsIgnoreCase(custerBuyer))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    validationDetails.addWarning(WarningType.Other, "Finance is Credited to Buyer [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Finance party not equal to Buyer name else");
                                    }
                              }
                        } catch (Exception e) {
                              Loggers.general().info(LOG,"charge account collected----->" + e.getMessage());
                        }
                  }

                  // NPA customer

                  try {

                        String productVal = "N";

                        if (prodtype.trim().equalsIgnoreCase("FSA")) {
                              // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              productVal = getDriverWrapper().getEventFieldAsText("DBT", "p", "cAJB").trim();// party
                              // id
                        } else if (prodtype.trim().equalsIgnoreCase("ODC")) {
                              // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              productVal = getDriverWrapper().getEventFieldAsText("DRW", "p", "cAJB").trim();// party
                              // id
                        }

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

                  try {
                        List<ExtEventLoanDetails> preship = (List<ExtEventLoanDetails>) getWrapper().getExtEventLoanDetails();
                        String currency = "";
                        String amtt = "";
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

                  // getSubvention

                  try {
                        // //Loggers.general().info(LOG,"get value for Subvention");
                        getSubvention();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
                        // //Loggers.general().info(LOG,"getSubvention");
                  } finally {
                        // //Loggers.general().info(LOG,"finally LOB ");
                  }
                  try {
                        if (getMajorCode().equalsIgnoreCase("FSA")) {
                              getLobFSA();
                        } else if (!getMajorCode().equalsIgnoreCase("ODC")) {
                              getLob();
                        }
                        if (getMinorCode().equalsIgnoreCase("FEC")) {
                              getLOBCREATE();
                        }
                  } catch (Exception ee) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in lob code===>" + ee.getMessage());
                        }
                  }

                  try {
                        String finaceCreate = getDriverWrapper().getEventFieldAsText("RCFA", "l", "");
                        if (finaceCreate.equalsIgnoreCase("Y") && !finaceCreate.equalsIgnoreCase("N")
                                    && getMinorCode().equalsIgnoreCase("FEC")) {
                              String facility = getWrapper().getLIMITID().trim();

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"ODC finance limit id validation===>" + facility);
                                    Loggers.general().info(LOG,"ODC LOB code finance created validation=======>" + finaceCreate);
                              }

//                            if (finaceCreate.equalsIgnoreCase("Y") && (facility == null || facility.equalsIgnoreCase(""))
//                                        && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
//                                  validationDetails.addError(ErrorType.Other,
//                                              "Please input the LOB limit id which is attached in credit facility [CM]");
//                            }
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
                                                if ((count == 0 || count < 1) && (step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                                      validationDetails.addWarning(WarningType.Other,
                                                                  "Please enter valid LOB limit id [CM]");
                                                }
*/
                                          } catch (Exception e) {
                                                Loggers.general().info(LOG,"Exception ODC finance limit id validation===>" + e.getMessage());
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
                        Loggers.general().info(LOG,"Exception FEC validation===>" + e.getMessage());
                  }

                  try {

                        String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, ETT_REFERRAL_TRACKING REF WHERE trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)   AND TRIM(REF.MASTER_REF_NO) ='"
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
                              Loggers.general().info(LOG,"Entered while referal step" + step + " count" + count);
                              
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
                                                Loggers.general().info(LOG,"Query ETT_REFERRAL_TRACKING CBS Authoriser 3rd===>" + query6);
                                          }
                                          ps1 = con.prepareStatement(query6);
                                          
                                          rs = ps1.executeQuery();
                                          ArrayList<String> al = new ArrayList<String>();
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
                                          // Loggers.general().info(LOG,"Single Warning Message " +
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
                        // + count);
                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception referal count" +
                        // e1.getMessage());
                  } finally {
                        try {
                              if (rs1!= null)
                                    rs1.close();
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
                  String prd_code = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                  // rejection validation
                  try {
                        int counter1 = 0;
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
                              counter1 = Integer.valueOf(rs.getString(1));
                        }

                        // //Loggers.general().info(LOG,"Rejection list count===>" + counter);

                        if (counter1 > 0) {
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
                  }

                  String char_val = getWrapper().getBENNAME_Name();
                  if (char_val != null && char_val.length() > 0) {
                        int counter1 = 0;
                        for (int i = 0; i < char_val.length(); i++) {
                              if (!Character.toString(char_val.charAt(i)).matches("^[a-zA-Z\\s]*$")) {
                                    counter1++;
                              }
                        }

                        if (counter1 > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
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
                  String rtgs = getWrapper().getPROREMT();
                  String rtgspart = getWrapper().getRTGSPART();

                  // //Loggers.general().info(LOG,"RTGS for initially-------->" + rtgs);

                  // unbalanced posting error
                  try {
                        if ((step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise"))
                                    && (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))
                                    && rtgspart.equalsIgnoreCase("FULL") && (subproductCode.equalsIgnoreCase("PCF")
                                                || subproductCode.equalsIgnoreCase("PCR") || subproductCode.equalsIgnoreCase("POF"))) {
                              // Loggers.general().info(LOG,"try Unbalanced posting valid");
                              try {
                                    con = getConnection();
                                    String query = "SELECT * FROM KMB_RTGS_NEFT_ACC_VALID_VIEW WHERE MASTER_REF = '"
                                                + MasterReference + "' AND REFNO_PFIX = '" + evnt + "' AND REFNO_SERL = '" + evvcount
                                                + "'";
                                    // Loggers.general().info(LOG,"Unbalanced posting error" +
                                    // query);
                                    // int count = 0;
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    if (rs1.next()) {
                                          // Loggers.general().info(LOG,"Unbalanced posting valid in
                                          // if loop");
                                    } else if ((step_csm.equalsIgnoreCase("CBS Authoriser")
                                                || step_csm.equalsIgnoreCase("Authorise"))
                                                && (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))) {

                                          validationDetails.addError(ErrorType.Other, "Please enter RTGS account number [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"Unbalanced posting valid in
                                          // else loop");
                                    }
                              } catch (Exception e1) {
                                    // Loggers.general().info(LOG,"Exception Unbalanced posting
                                    // error" + e1.getMessage());
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
                                          // output console");
                                          e.printStackTrace();
                                    }
                              }
                        } else {
                              // Loggers.general().info(LOG,"Please enter RTGS account in else");

                        }
                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception Unbalanced posting===>" +
                        // e1.getMessage());
                  }

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
                                          Loggers.general().info(LOG,"NEFT value Blank-------->" + NEFTTime);
                                    }

                              }
                              if (NEFTTime.equalsIgnoreCase("NO") && (rtgs.equalsIgnoreCase("NEF"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Authoriser")
                                                      || step_csm.equalsIgnoreCase("Authorise"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Today is holiday. No NEFT entry will proceed [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Today is not holyday so NEFT will avilable");
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
                                          Loggers.general().info(LOG,"RTGS value Blank-------->" + RTGSTime);
                                    }

                              }
                              if (RTGSTime.equalsIgnoreCase("NO") && (rtgs.equalsIgnoreCase("RTG"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Authoriser")
                                                      || step_csm.equalsIgnoreCase("Authorise"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Today is holiday. No RTGS entry will proceed [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Today is not holyday so RTGS will avilable");
                                    }
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception RTGS/NEFT holiday" + e.getMessage());
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
                        if (x.before(c1.getTime()) && rtgs.equalsIgnoreCase("RTG")&& bankpay.equalsIgnoreCase("B2C")
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

                  try {

                        getutrNoGenerated();
                  } catch (Exception ee) {
                        Loggers.general().info(LOG,"UTR Number getutrNoGenerated" + ee.getMessage());

                  }
                  if (getMinorCode().equalsIgnoreCase("FEC")) {
                        try {

                              getpenalRateFEC();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"get Penal Rate" + ee.getMessage());

                        }
                  }
                  //Start changes -Remedy #INC000004065584
                  try {

                        int penal=getpenalRate();
                        Loggers.general () .info(LOG,"penal" + penal);

                        if (penal==1&&(step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject")))
                        {                       Loggers.general () .info(LOG,"Condition true" + penal);

                              validationDetails.addError(ErrorType.Other,
                                          "Actual penal rate is not updated please click on validate[CM]");
                        }
                  } catch (Exception ee) {
                        Loggers.general () .info(LOG,"get Penal Rate" + ee.getMessage());

                  }
                  //-----------------FORCE DEBIT start----------------------------
                  
                  if((step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Reject")||step_csm.equalsIgnoreCase("CBS Authoriser")) && getMajorCode().equalsIgnoreCase("ODC") &&  (getMinorCode().equalsIgnoreCase("FEC"))) {
                              
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
                              if(getMinorCode().equalsIgnoreCase("FEC"))
                                    validationDetails.addError(ErrorType.Other,
                                          "Force Debit Flag should be ticked in Finance Existing collection event [CM]");
                        
                        }

                  }
                  
                  
                  //-----------------FORCE DEBIT end----------------------------

                  try {

                        if (getMajorCode().equalsIgnoreCase("FSA")
                                    && (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))
                                    && (subproductCode.equalsIgnoreCase("CBD") || subproductCode.equalsIgnoreCase("IVD")
                                                || subproductCode.equalsIgnoreCase("FAC") || subproductCode.equalsIgnoreCase("RVF")
                                                || subproductCode.equalsIgnoreCase("TDF"))) {

                              getrtgsNeftNet();

                        } else {
                              if (getMajorCode().equalsIgnoreCase("FSA")) {
                                    Loggers.general().info(LOG,"FSA subproductCode" + subproductCode);
                              }
                        }
                  } catch (Exception e) {
                        if (getMajorCode().equalsIgnoreCase("FSA")) {
                              Loggers.general().info(LOG,"Exception in getrtgsNeftNet" + e.getMessage());
                        }
                  }
                  if (getMinorCode().equalsIgnoreCase("FEC") && subproductCode.equalsIgnoreCase("FCA")) {

                        // PostingCustom post = null;
                        // if(step_csm.equalsIgnoreCase("CSM Maker 1"))
                        // String strPSID =
                        // getDriverWrapper().getPostingFieldAsText("PSID", "").trim();
                        try {

                              String Mast = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                              String Evnt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                              // String strPSID = post.valueString();

                              // Loggers.general().info(LOG,"PSID--------->"+strPSID);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"MasterReference-------->" + Mast);
                                    Loggers.general().info(LOG,"their Reference-------->" + Evnt);
                                    // Loggers.general().info(LOG,"PSID----------->" + strPSID);
                              }
                              con = getConnection();
                              String query1 = "SELECT DISTINCT SELLEX99 FROM MASTER MAS,BASEEVENT BEV,BASEEVENT BEV1, RELITEM  REL,POSTING POS,POSTRULES POSR,FXRATE86 F86"
                                          + " WHERE MAS.KEY97=BEV.MASTER_KEY" + " AND BEV.KEY97=BEV1.ATTACHD_EV"
                                          + " AND BEV1.KEY97=REL.EVENT_KEY" + " AND REL.KEY97=POS.KEY97"
                                          + " AND POS.POSTINGTYP =  POSR.POSTINGTYP" + " AND  BEV1.EXEMPLAR  =  POSR.EV_TYPE"
                                          + " AND POSR.FX_RATECOD = F86.CODE53" + " AND MAS.CCY  = F86.CURREN49" + " AND MASTER_REF='"
                                          + Mast + "'" + " AND BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) ='" + Evnt + "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Query Result for FX_Rate----------> " + query1);

                              }

                              ps1 = con.prepareStatement(query1);

                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {

                                    String fx = rs1.getString(1);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Entering While Loopfor FEC===>" + getMinorCode().equalsIgnoreCase("FEC"));
                                          Loggers.general().info(LOG,"Entering While Loopfor FEC===>" + fx);

                                    }
                                    getPane().setFX_RATE(fx);

                              }

                              String fx_rate = getWrapper().getFX_RATE();
                              Loggers.general().info(LOG,"fx_rate in FEC---" + fx_rate);
                              String interestType = getDriverWrapper().getEventFieldAsText("B+IS", "l", "");

                              

                              if ((fx_rate == null || fx_rate.equalsIgnoreCase("")) && interestType.equalsIgnoreCase("Y")
                                          && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                      || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "FX rate should not be blank,Please enquiry finance screen[CM]");
                              }
                        } catch (Exception e) {
                              e.printStackTrace();
                              Loggers.general().info(LOG,"Exception FX_Rate===>" + e.getMessage());

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

//-------------------------Start of Preshipment--------------
                  
                  if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("FEC")
                              && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                        try{
                              String strPropName1 = "PRESHIPMENTLINK";
                              String preship = "";
                              AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE1 = getDriverWrapper()
                                          .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName1 + "'");
                              // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                              EXTGENCUSTPROP PROPCode1 = queryRCODE1.getUnique();
                              if (PROPCode1 != null) {

                                    preship = PROPCode1.getPropval();
                                    // //Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                                    // PROPCode.getPropval());
                              } else {
                                    // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

                              }
                              
                        Loggers.general().info(LOG,"PRESHIPMENTLINK----------> " + preship);
                        List<ExtEventLoanDetails> loanDetails = (List<ExtEventLoanDetails>) getWrapper().getExtEventLoanDetails();
                        int loanCount=loanDetails.size();
                        int count=0;
                        String master_Ref="";
                        Loggers.general().info(LOG,"Master Reference from FOC----------> " + masterref);
                        Loggers.general().info(LOG,"Loan details grid size----------> " + loanCount);
                        if(loanCount>0){
                        
                        con = getConnection();
                        String query1 = "select masref from ett_preshipment_apiserver where masref='"+masterref+"'";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query Result for Preshipment----------> " + query1);

                        }

                        ps1 = con.prepareStatement(query1);

                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {

                              master_Ref=rs1.getString(1);  
                              count++;
                              Loggers.general().info(LOG,"Master Reference from FOC inside query----------> " + master_Ref);
                        

                  }
                        Loggers.general().info(LOG,"Final count of Loan details master reference----------> " + count);
                        if(count==0&&preship.equalsIgnoreCase("E"))
                        {
                              validationDetails.addError(ErrorType.Other,
                                          "Master Reference and Preshipment Master Reference is different Kindly do the preshipment again [CM]");
                        }
                        else if(count==0&&preship.equalsIgnoreCase("W"))
                        {
                              validationDetails.addWarning(WarningType.Other,
                                          "Master Reference and Preshipment Master Reference is different Kindly do the preshipment again [CM]");
                        }
                        }
                        }
                        catch (Exception e) {
                              e.printStackTrace();
                              Loggers.general().info(LOG,"Exception third party check===>" + e.getMessage());

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
                  if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("FEC")
                              && (step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser")||step_csm.equalsIgnoreCase("CBS Reject")) ){
                        String loanRef="";
                        BigDecimal Amount=null;
                        String ccy="";
                        int loancount=0;
                        try{
                              Loggers.general().info(LOG,"Invoice details size-------------> ");
                        List<ExtEventLoanDetails> LoanDet = (List<ExtEventLoanDetails>) getWrapper().getExtEventLoanDetails();
                         loancount=LoanDet.size();
                        Loggers.general().info(LOG,"Invoice details size-------------> "+LoanDet);
                        Loggers.general().info(LOG,"Invoice details size-------------> "+loancount);
                        for (int l = 0; l < LoanDet.size(); l++) {
                              ExtEventLoanDetails loandetails = LoanDet.get(l);     
                              BigDecimal pre_out=null;
                              BigDecimal pre_out1=null;
                              String master=null;
                              // invnum=invnum+invoicedetails.getINVNUMB().trim();
                              loanRef=loandetails.getDEALREF().trim();
                              Amount=loandetails.getREAMOUNT();
                              
                              ccy=loandetails.getREAMOUNTCurrency();
                              Loggers.general().info(LOG,"Dealreference-------------> "+loanRef);     
                              Loggers.general().info(LOG,"Loan Amount-------------> "+Amount);  
                              Loggers.general().info(LOG,"Loan Amount currency-------------> "+ccy);  
                              con = getConnection();

                              String checkOut="select AMT_O_S from master,C8PF c8"  
                                      + " where C8.C8CCY= MASTER.CCY AND master_ref='"+loanRef+"' and refno_pfix<>'NEW'";
                              Loggers.general().info(LOG,"Loan Amount currency query-------------> "+checkOut);   
                              ps1 = con.prepareStatement(checkOut);
                              rs1 = ps1.executeQuery();
                              if (rs1.next()) {
                                    pre_out=rs1.getBigDecimal(1);
                                    Loggers.general().info(LOG,"Amount in query "+pre_out);     
                              }
                                    int res=0;
                                    res=pre_out.compareTo(Amount);
                                    if(res == -1)
                                    {
                                          Loggers.general().info(LOG,"Amount less than outstanding");       
                        validationDetails.addError(ErrorType.Other, "Preshipment knock of amount is greater than the outstanding amount[CM]");
                                    }
                                    String checkOut1="SELECT ETT.LOAN_REF, SUM(ETT.REPAYAMT) AS OUT_AMT FROM ETT_PRESHIPMENT_APISERVER ETT ,MASTER MAS,"
                                    + "  BASEEVENT BEV, (SELECT BEV.KEY97 AS BEV_KEY FROM UBZONE.MASTER MAS, BASEEVENT BEV, EVENTSTEP EVS, ORCH_MAP ORM WHERE MAS.KEY97   = BEV.MASTER_KEY"
                                +" AND BEV.KEY97     = EVS.EVENT_KEY AND EVS.ORCH_MAP  = ORM.KEY97 AND ORM.ORCH_STEP = 16 AND BEV.STATUS   <>'c')fp WHERE TRIM(ETT.MASREF)=TRIM(MAS.MASTER_REF) AND TRIM(ETT.EVENTREF)=TRIM(BEV.REFNO_PFIX)||LPAD(BEV.REFNO_SERL,3,0) AND MAS.KEY97  =BEV.MASTER_KEY"
                                    +" AND bev.key97   = fp.bev_key(+) AND BEV.STATUS ='i' AND fp.bev_key   IS NULL  AND ETT.LOAN_REF= '"+loanRef+"' GROUP BY ETT.LOAN_REF";
                        
                                                Loggers.general().info(LOG,"Loan Amount currency query11-------------> "+checkOut1);      
                                                ps = con.prepareStatement(checkOut1);
                                                rs = ps.executeQuery();
                                                if (rs.next()) {
                                                      pre_out1=rs.getBigDecimal(2);
                                                      Loggers.general().info(LOG,"Amount in query11 "+pre_out1);  
                                                }
                                                      int res1=0;
                                                      res1=pre_out.compareTo(pre_out1);
                                                      if(res1 == -1)
                                                      {
                                                            
                                                            Loggers.general().info(LOG,"Amount less than outstanding"); 
                                                            String query="SELECT ETT.MASREF FROM UBZONE.ETT_PRESHIPMENT_APISERVER ETT ,UBZONE.MASTER MAS,UBZONE.BASEEVENT BEV, (SELECT BEV.KEY97 AS BEV_KEY FROM UBZONE.MASTER MAS, BASEEVENT BEV, EVENTSTEP EVS, ORCH_MAP ORM WHERE MAS.KEY97   = BEV.MASTER_KEY"
                                                +" AND BEV.KEY97     = EVS.EVENT_KEY AND EVS.ORCH_MAP  = ORM.KEY97 AND ORM.ORCH_STEP = 16 AND BEV.STATUS   <>'c')fp"
                                                                        +" WHERE TRIM(ETT.MASREF)=TRIM(MAS.MASTER_REF)"
                                                                        +" AND TRIM(ETT.EVENTREF)=TRIM(BEV.REFNO_PFIX)||LPAD(BEV.REFNO_SERL,3,0) "
                                                                        +" AND MAS.KEY97=BEV.MASTER_KEY AND bev.key97   = fp.bev_key(+) AND BEV.STATUS ='i' AND fp.bev_key   IS NULL  AND ETT.LOAN_REF= '"+loanRef+"' and ETT.MASREF!='"+masterref+"'" ;
                                                            Loggers.general().info(LOG,"Loan Amount currency query-------------> "+query);            
                                                            ps2 = con.prepareStatement(query);
                                                            rs2 = ps2.executeQuery();
                                                            while (rs2.next()) {
                                                                  /*String master=rs2.getString("MASREF");
                                                                   mst=mst + " " +master;*/
                                                                  
                                                                  if(master ==null || master.isEmpty())
                                                                        master=rs2.getString("MASREF");
                                                                  else
                                                                        master=master + " " +rs2.getString("MASREF");
                                                            }
                                                            validationDetails.addError(ErrorType.Other, "Pre shipment knocked off reference number already fetched in" +master+ ". Kindly check the outstanding amount [CM]");
                                                      }     
                                                      else
                                                      {
                                                            Loggers.general().info(LOG,"Amount less than outstanding failure");     
                                                      }           
                              
                        }
                        }
                        catch(Exception e)
                        {
                              e.printStackTrace();
                              Loggers.general().info(LOG,"Exception in preshipment subproduct===>" + e.getMessage());
                        }
                        finally {
                              try {
                                    if (rs!= null)
                                          rs.close();
                                    if (ps2!= null)
                                          ps2.close();
                                    if (rs2 != null)
                                          rs2.close();
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
                  
                  if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("FEC")
                              && (step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser")||step_csm.equalsIgnoreCase("CBS Reject")) ){
                  


                        


                        
                        int cnt=0;
                        int count=0;
                        int query_count=0;
                        try{
                              /*con = getConnection();
                              String query = "select masref from ett_preshipment_apiserver where masref='"+masterref+"' and eventref='"+eventREF+ "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Query Result for Preshipment1----------> " + query);

                              }

                              ps = con.prepareStatement(query);

                              rs = ps.executeQuery();
                              if (rs.next()) {*/
                              
                              Loggers.general().info(LOG,"Invoice details size1-------------> ");
                              con = getConnection();
                              String query1 = "select count(*) from ett_preshipment_apiserver where masref='"+masterref+"'and eventref='"+eventREF+ "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Query Result for Preshipment1----------> " + query1);

                              }

                              ps1 = con.prepareStatement(query1);

                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    query_count=rs1.getInt(1);
                              }
                              Loggers.general().info(LOG,"Query Result for Preshipment1----------> " + query_count);
                        List<ExtEventLoanDetails> LoanDet = (List<ExtEventLoanDetails>) getWrapper().getExtEventLoanDetails();
                         //loancount=LoanDet.size();
                  
                        for (int l = 0; l < LoanDet.size(); l++) {
                              
                              count=count+1;
                              
                                    }
                        Loggers.general().info(LOG,"Query Result for Preshipment1----------> " + count);

                              if(query_count!=count)
                              {
                                    Loggers.general().info(LOG,"Query Result for Preshipment1----------> " + count);
                                    validationDetails.addError(ErrorType.Other, "Please fetch the preshipment details[CM] ");
                              }
                              try{
                                    if(ps !=null)
                                          ps.close();
                                    if(rs !=null)
                                          rs.close();
                              }catch(Exception e){
                                    Loggers.general().info(LOG,"close shp---->"+e.getMessage());
                              }
                        
                              
                              for (int l = 0; l < LoanDet.size(); l++) {
                                    ExtEventLoanDetails preshipment = LoanDet.get(l);
                                    double amt=0.0;
                                    String cur=null;
                                    String loanref=null;
                                    int result;
                                   amt=preshipment.getREAMOUNT().doubleValue();
                                     cur=preshipment.getREAMOUNTCurrency();
                                     loanref=preshipment.getDEALREF();
                                    
                                     Loggers.general().info(LOG,"Amount for Preshipment1----------> " + amt);
                                     Loggers.general().info(LOG,"Currency for Preshipment1----------> " + cur);
                                     Loggers.general().info(LOG,"Loan Reference for Preshipment1----------> " + loanref);
                                     con = getConnection();
                                     String query2 = "select count(*) from ett_preshipment_apiserver where masref='"+masterref+"'and eventref='"+eventREF+"' and LOAN_REF ='"+loanref+"' and REPAYAMT='"+amt+"' and CURR='"+cur+"'";
                              
                                                Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + query2);

                                          ps = con.prepareStatement(query2);

                                          rs = ps.executeQuery();
                                          if (rs.next()) {
                                                result=rs.getInt(1);
                                                if(result==1)
                                                cnt=cnt+1;
                                                Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                          }
                              }
                              
                              if (cnt!=count){
                                    Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                    validationDetails.addError(ErrorType.Other, "Please fetch the preshipment details[CM] ");
                              }
                                    
                              //}
                        }
                        
                        catch(Exception e)
                        {
                              e.printStackTrace();
                              Loggers.general().info(LOG,"Exception in preshipment error===>" + e.getMessage());
                        }
                        finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                                    if (rs != null)
                                          rs.close();
                                    if (ps != null)
                                          ps.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,"Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                        
                  
                  
                  
                  }
                  //String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
                  try{
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
                  
                  
                  if (getMajorCode().equalsIgnoreCase("FSA") && getMinorCode().equalsIgnoreCase("RSA"))
                  {
                        try{
                              int count=0;
                              String Masterref=getDriverWrapper().getEventFieldAsText("MST","r","");
                              String EventSerial=getDriverWrapper().getEventFieldAsText("ESQ","i","");
                              Loggers.general().info(LOG,"Event count===>"+EventSerial);
                              Loggers.general().info(LOG,"Event count===>"+Masterref);
                              con = getConnection();
                              String query1 = "SELECT COUNT(*) FROM master mas,baseevent bev,(SELECT BEV.KEY97 AS BEV_KEY FROM UBZONE.MASTER MAS,BASEEVENT BEV,EVENTSTEP EVS,ORCH_MAP ORM WHERE MAS.KEY97   = BEV.MASTER_KEY AND BEV.KEY97     = EVS.EVENT_KEY AND EVS.ORCH_MAP  = ORM.KEY97 AND ORM.ORCH_STEP = 16 AND BEV.STATUS   <>'c' )fp WHERE mas.KEY97          =bev.MASTER_KEY AND bev.key97            = fp.bev_key(+) AND mas.MASTER_REF       ='"+masterref+"' AND trim(bev.refno_pfix) ='RFS' AND trim(bev.REFNO_SERL) <'"+ EventSerial +"' AND bev.status           ='i' AND fp.bev_key          IS NULL ";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Query Result for REFERENCE----------> " + query1);

                              }

                              ps1 = con.prepareStatement(query1);

                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count=rs1.getInt(1);
                                    Loggers.general().info(LOG,"COUNT for reference --------> " + query1);
                              }
                              if(count!=0)
                              {
                                    validationDetails.addError(ErrorType.Other, "Already repayment event in process, please complete or abort earlier event to continue this event[CM]");
                              }
                        }
                        catch(Exception e){
                              
                        }
                        finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                                    if (rs != null)
                                          rs.close();
                                    if (ps != null)
                                          ps.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,""Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                        
                              try{
                                    BigDecimal pre_out=null;
                                    BigDecimal pre_out1=null;
                                    String Masterref=getDriverWrapper().getEventFieldAsText("MST","r","");
                                    String Eventref=getDriverWrapper().getEventFieldAsText("EVR","r","");
                                    Loggers.general().info(LOG,"Event count06===>"+Eventref);
                                    Loggers.general().info(LOG,"Event count06===>"+Masterref);
                                    con = getConnection();
                                    String checkOut="select AMT_O_S from master,C8PF c8"  
                                  + " where C8.C8CCY= MASTER.CCY AND master_ref='"+Masterref+"' and refno_pfix<>'NEW'";
                        Loggers.general().info(LOG,"Loan Amount currency query1-------------> "+checkOut);  
                        ps1 = con.prepareStatement(checkOut);
                        rs1 = ps1.executeQuery();
                        if (rs1.next()) {
                              pre_out=rs1.getBigDecimal(1);
                              Loggers.general().info(LOG,"Amount in query06 "+pre_out);   
                        }
                        con = getConnection();
                        String checkOut1="SELECT ci.BALANC_AMT,mas.master_ref,bev.REFNO_PFIX,bev.REFNO_SERL"
                                    + "    FROM master mas, baseevent bev, extevent ext,tidataitem ti, CURRENTINT ci"
                                    + "    WHERE mas.key97  =bev.masteR_key AND bev.key97 =ext.event   AND bev.key97 =ti.event_key"
                                    + "    and ti.key97=ci.key97 AND TRIM(mas.master_Ref)='"+Masterref+"' and TRIM(BEV.REFNO_PFIX)"
                                    + "        ||LPAD(BEV.REFNO_SERL,3,0)='"+Eventref+"'";
            
                                    Loggers.general().info(LOG,"Loan Amount currency query06-------------> "+checkOut1);      
                                    ps = con.prepareStatement(checkOut1);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                          pre_out1=rs.getBigDecimal(1);
                                          Loggers.general().info(LOG,"Amount in query06 "+pre_out1);  
                                    }     
                                    int res1=pre_out.compareTo(pre_out1);
                                    if(res1 !=0)
                                    {Loggers.general().info(LOG,"Amount in query done entered06");    
                                          validationDetails.addError(ErrorType.Other, "Please abort the transaction and create again[CM]");
                                    }
                              }
                              catch(Exception e){
                                    
                              }
                  
                  }
                  
                  
                  if (getMajorCode().equalsIgnoreCase("FSA") && (getMinorCode().equalsIgnoreCase("ASA")||getMinorCode().equalsIgnoreCase("JSA")))
                  {
                        try{
                              int count=0;
                              String Masterref=getDriverWrapper().getEventFieldAsText("MST","r","");
                              String EventREF=getDriverWrapper().getEventFieldAsText("EPF","s","");
                              Loggers.general().info(LOG,"Event count===>"+EventREF);
                              Loggers.general().info(LOG,"Event count===>"+Masterref);
                              con = getConnection();
                              String query1 = "SELECT count(*) FROM MASTER MAS, BASEEVENT BSV WHERE MAS.KEY97=BSV.MASTER_KEY AND BSV.REFNO_PFIX NOT IN('TRC') AND BSV.TIMESTART> (select bsv1.TIMESTART from BASEEVENT bsv1"
                             +" where mas.KEY97=bsv1.MASTER_KEY and bsv1.refno_pfix='"+EventREF+"' and bsv1.status='i' AND MAS.MASTER_REF='"+Masterref+"')"
                                    +" AND BSV.STATUS='c' AND MAS.MASTER_REF='"+Masterref+"'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Query Result for REFERENCE----------> " + query1);

                              }

                              ps1 = con.prepareStatement(query1);

                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count=rs1.getInt(1);
                                    Loggers.general().info(LOG,"COUNT for reference --------> " + query1);
                              }
                              if(count!=0)
                              {
                                    validationDetails.addError(ErrorType.Other, "Please abort the transaction and create again after this event various events has been completed[CM]");
                              }
                        }
                        catch(Exception e){
                              
                        }
                        finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                                    if (rs != null)
                                          rs.close();
                                    if (ps != null)
                                          ps.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,""Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
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
                  //CR 140
                  if(getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("FEC"))
                  {
                        try {
                        int cnt = 0;
                        Loggers.general () .info(LOG,"PCR/PCF--");
                        cnt = preshipWar();
                        Loggers.general () .info(LOG,"count" + cnt);
                        String subProductype = getDriverWrapper().getEventFieldAsText("PTP",
                                    "s", "");
                        Loggers.general () .info(LOG,"subProductype in odc/fec==>" + subProductype);
                        if (cnt == 1 && (subProductype.equalsIgnoreCase("OCF")||
                                    subProductype.equalsIgnoreCase("OCI")||subProductype.equalsIgnoreCase("NBI")
                                    )) {
                              //Loggers.general () .info(LOG,"duplicate reference");
                              validationDetails.addWarning(WarningType.Other,"Packing credits are outstanding for the customer. Please check if this payment is to be used to knock off exsisting PCR/PCF [CM]");             
                              }
                  } catch (Exception ee) {
                        //ee.printStackTrace();
                        Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());

                  }
                  try {
                              getFcctDetails(validationDetails);
                              System.out.println("fcct details:");
                        }
                        catch (Exception e) {
                        e.printStackTrace();
                        //    System.out.println("fcct data:"+e);

                        }
                  }
                  //CR 140
                  
                  if(getMajorCode().equalsIgnoreCase("FSA") && (getMinorCode().equalsIgnoreCase("CSA")||getMinorCode().equalsIgnoreCase("JSA")||getMinorCode().equalsIgnoreCase("ASA"))
                              &&(step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Maker 1")
                                          ||step_csm.equalsIgnoreCase("CBS Authoriser")||step_csm.equalsIgnoreCase("CBS Reject")))
                  {
                        String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                        String mainEvntRef = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                        try{
                              int cnt=0,result=0;
                               String  intcon = getDriverWrapper().getEventFieldAsText("B+AM", "l", " ").trim();
                                 String party=getDriverWrapper().getEventFieldAsText("B+DP", "p", "no").trim();
                              
                                 if(intcon.equalsIgnoreCase("Y"))
                                 {
                               con = getConnection();
                               String query2 = "SELECT count(*) FROM GVPF WHERE  GVCUS1='"+party+"' "
                                    +" and GVCCY='INR' and GVPRF='R' and GVMVT='I'";
                        
                                          Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + query2);

                                    ps = con.prepareStatement(query2);

                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                          result=rs.getInt(1);
                                          if(result==1)
                                          cnt=cnt+1;
                                          
                                          Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                    }
                                    
                                    if (cnt==0)
                                    {
                                           con = getConnection();
                                           String query3 ="SELECT count(*) FROM UBZONE.MASTER MAS, UBZONE.TIDATAITEM TID , UBZONE.BASEEVENT BEV, UBZONE.MSTRSETTLE MSTR, UBZONE.PARTYDTLS PTY WHERE MAS.KEY97 = BEV.MASTER_KEY"
                                                       +" AND BEV.KEY97 = TID.EVENT_KEY AND TID.KEY97 = MSTR.KEY97 AND MSTR.SETTLE_PTY = PTY.KEY97 AND MSTR.PAY_REC = 'R' AND MSTR.CCY = 'INR' AND MSTR.MOVEMENT = 'I' and TID.DELETED='N'"
                                                       +" AND BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0)='"+mainEvntRef+"' AND MAS.MASTER_REF = '"+mainMasterRef+"' AND trim(PTY.CUS_MNM) = '"+party+"'";
                                          
                                                      Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + query3);

                                                ps1 = con.prepareStatement(query3);

                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      result=rs1.getInt(1);
                                                      if(result==1)
                                                      cnt=cnt+1;
                                                      
                                                      Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                                }
                                    }
                                    if(cnt==0 &&(step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject")))
                                    {
                                          Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                          validationDetails.addWarning(WarningType.Other, "Settlement instruction(interest) is mandatory for the interest consolidation.Please pend and reopen the transaction for settlememt instruction to be updated to database[CM]");
                                    
                                    }
                                    if(cnt==0 &&(step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser")))
                                    {
                                          Loggers.general().info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                          validationDetails.addError(ErrorType.Other, "Settlement instruction(interest) is mandatory for the interest consolidation.Please pend and reopen the transaction for settlememt instruction to be updated to database[CM]");
                                    
                                    }
                        }
                        }
                        catch(Exception e)
                        {
                              
                        }
                        
                        finally{
                              surrenderDB(con,ps1,rs1);
                        }
                  }//CR140 ends
                  
                  // CR220 Changes Startes here

                  String subProductype = getDriverWrapper().getEventFieldAsText("PTP",
                              "s", "");
                  
                  if (getMajorCode().equalsIgnoreCase("FSA")&&subProductype.equalsIgnoreCase("PCF")) {
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
                              System.out.println("CR-220 Validation Exception occured");
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

                  if ((getMinorCode().equalsIgnoreCase("KSA"))
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
                  
                  try
                  {
                  getPostingFxrate();
                  }
                  catch(Exception e)
                  {
                        
                  }
                  //Done by vishal
                  if(getMajorCode().equalsIgnoreCase("FSA") && getMinorCode().equalsIgnoreCase("CSA")){
                        try{
            //          String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
            //        String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
                        String finAmt = getDriverWrapper().getEventFieldAsText("B+FD", "v", "m");
                        String financeParty = getDriverWrapper().getEventFieldAsText("B+FT", "p", "cu").trim();
                        List<ExtEventPrePurchaseOrder> shipTable = (List<ExtEventPrePurchaseOrder>) getWrapper()
                                                .getExtEventPrePurchaseOrder();
                        System.out.println("inside pre purchse order" );
                                    // //Loggers.general().info(LOG,"shipping table for notional
                                    // rate---->" + shipTable.size());
                                    for (int i = 0; i < shipTable.size(); i++) {

                                          ExtEventPrePurchaseOrder ship = shipTable.get(i);
                                    System.out.println("inside for loop" );
                        
                        //String currAmt=total.subtract(finAmt);
                                    BigDecimal x=new BigDecimal(finAmt);
                                    BigDecimal y=new BigDecimal(100);
                                    BigDecimal fincAmt=x.multiply(y);
                                          BigDecimal totAmt=ship.getTOFAMT();
                                          String crno=ship.getCRNO();
                                          BigDecimal elgAmt=totAmt.subtract(fincAmt);
                                          System.out.println("subtract amount "+x+" "+totAmt+" "+elgAmt+" "+fincAmt+" "+crno );
                                          ship.setCURRFAMT(elgAmt);
                                          ship.setUFINAMT(x);
                                          System.out.println("subtract amount over" );
                                          if(!financeParty.equalsIgnoreCase(crno)){
                                                
                                                validationDetails.addError(ErrorType.Other,
                                                            "CIF ID and finance party are not same");
                  System.out.println("Exception in purchase Order:");
                  }
                        //                String queryUpdate= "update exteventppo set currfamt = '"+elgAmt+ "'where xkey ='"+key+"'";
                        //            System.out.println("subtract amount query"+queryUpdate);
                        //                    dmsp= con.prepareStatement(queryUpdate);
                        //                            dmsp.executeUpdate();
                        //                            System.out.println("Finance amount calculation");
                                          
                                          }
                                    
                                          }
                                    
                         catch (Exception e) {
                                          e.printStackTrace();
                                                System.out.println("Exception in purchase Order:"+e);

                                          }
                        finally {
                              surrenderDB(con, dmsp, dmsr);
            }}
                  
                        //code by nidhi
                        if ((getMajorCode().equalsIgnoreCase("FSA")) && (getMinorCode().equalsIgnoreCase("CSA"))) {
                              
                              try {
                                    getFcctDetails(validationDetails);
                                    System.out.println("fcct details:");
                              }
                              catch (Exception e) {
                              e.printStackTrace();
                              //    System.out.println("fcct data:"+e);

                              }
                              String tenor = getDriverWrapper().getEventFieldAsText("PTN", "s", "");
                              String importOperating = getDriverWrapper().getEventFieldAsText("SEL", "p", "cBYH");
                        String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                        int imptrade=0;
                        int tenorperiod=0;
                        
                        
                              try {
                                    
                                    // Loggers.general().info(LOG,"query " + query);
                                    System.out.println(" EXPTRADE query: " +tenor+" "+subproCode);
                                    
                                    if(!importOperating.equals("")&&!importOperating.isEmpty() &&!tenor.equals("")&& !tenor.isEmpty()) {
                                    imptrade=Integer.parseInt(importOperating);
                                    tenorperiod=Integer.parseInt(tenor);
                                    }
                                    if (tenorperiod>imptrade) {
                                          System.out.println(" tenor period and import trade value "+tenorperiod+" "+imptrade);
                                          validationDetails.addWarning(WarningType.Other,"Tenor Period is greater than Customer export preshipment Operating Trade Cycle "+imptrade+" days");
                                    }
                              }catch (Exception e) {
                                          System.out.println("Exception update of tenor" + e.getMessage());
                                    }
                              finally {
                               ConnectionMaster.surrenderDB(con, ps, rs);
                                  
                              }
                        }
                        //CODE BY NIDHI
                  if ((getMajorCode().equalsIgnoreCase("FSA")) && (getMinorCode().equalsIgnoreCase("RSA"))) {
                              
                              try {
                                    getFcctDetails(validationDetails);
                                    System.out.println("fcct details:");
                              }
                              catch (Exception e) {
                              e.printStackTrace();
                              //    System.out.println("fcct data:"+e);

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

      public boolean isvalidPurchaseOrder(String orderNo) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            boolean result = false;
            try {
                  connection = ConnectionMaster.getConnection();
                  String query = "select COUNT(1) from ETT_EXPORT_ORDER WHERE TRIM(STATUS)='A' AND TRIM(EXPORT_ORDER_NUMBER)=?";
                  preparedStatement = con.prepareStatement(query);
                  preparedStatement.setString(1, orderNo.trim());
                  resultSet = preparedStatement.executeQuery();
                  if (resultSet.next()) {
                        if (resultSet.getInt(1) > 0) {
                              result = true;
                        }
                  }
            } catch (Exception e) {
                  Loggers.general().info(LOG,"Exception in isvalidPurchaseOrder" + e.getMessage());
            } finally {
                  ConnectionMaster.surrenderDB(connection, preparedStatement, resultSet);
            }
            return result;
      }

      //

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
      // // Loggers.general().info(LOG,"Exception occured in isChargeAccountDiff - "
      // // + e.getMessage());
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
      // return isChargeAccountDiff;
      // }

}