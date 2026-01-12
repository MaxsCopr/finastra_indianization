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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceData;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class CpciPcic extends ConnectionMaster {
//    private static final Logger logger = Logger.getLogger(CpciPcic.class.getName());

private static final Logger LOG = LoggerFactory.getLogger(CpciPcic.class);
      Connection con, con1 = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst, ship_prepare = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ship_result = null;

      public boolean onPostInitialise() {
            // getting LOB post initialise
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
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //

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

                  if (getMinorCode().equalsIgnoreCase("PCIC")) {
                        try {
                              // //Loggers.general().info(LOG,"get value for LOB");
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }
                  }

                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "").trim();
                  // //Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                  String step_Id = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
                  // //Loggers.general().info(LOG,"step id check for CSM ----->" + step_Id);

                  // if (step_Id.equalsIgnoreCase("CSM") ||
                  // step_Id.equalsIgnoreCase("Log")
                  // || step_Input.equalsIgnoreCase("l") &&
                  // !eventStatus.equalsIgnoreCase("Completed")) {
                  // // //Loggers.general().info(LOG,"Step id for log Create if for log
                  // // step---->" + step_Id);
                  //
                  // getPane().setRTGNFT("");
                  // getPane().setRTGSN(false);
                  // getPane().setNETSCF("");
                  // getPane().setPROREMT("");
                  // getPane().setPURCODE("");
                  // getPane().setBENTYP("");
                  // getPane().setBENACC_Name("");
                  // getPane().setBENNAME_Name("");
                  // getPane().setIFSCCO_Name("");
                  // getPane().setBENBAK("");
                  // getPane().setBENBRN("");
                  // getPane().setBENCITY("");
                  // getPane().setUTRNO("");
                  // getPane().setRTGSNEFT("");
                  // getPane().setNARRTVE("");
                  //
                  // getPane().setNOSAMT("");
                  // getPane().setNOSTOUT("");
                  // getPane().setEBRCFLAG("");
                  // } else {
                  // Loggers.general().info(LOG,"Step id for else step---->" + step_Id);
                  // }
//                if (!step_Id.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
//                //    getPane().onNOSTROINWRMTCUSclayButton();
//
//                      getPane().onNOSTROOUTWREMCorrespondenceLayButton();
//                }

                  // Purpose code population
                  String productyp = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  try {
                        getrbiPurcodeCode();

                  } catch (Exception e) {
                        Loggers.general().info(LOG,"Exception in purpose code====>" + e.getMessage());

                  }

                  EventPane pane = (EventPane) getPane();
                  // List<ExtEventLoanDetails> loandet = (List<ExtEventLoanDetails>)
                  // getWrapper().getExtEventLoanDetails();
                  // if (loandet.size() > 0) {
                  //
                  // pane.getExtEventLoanDetailsNew().setEnabled(false);
                  // pane.getExtEventLoanDetailsDelete().setEnabled(false);
                  // } else {
                  //
                  // pane.getExtEventLoanDetailsNew().setEnabled(false);
                  // pane.getExtEventLoanDetailsDelete().setEnabled(false);
                  // }

                  // Standing instruction link

                  try {
                        getPane().onRTGSINWRMTCUSclayButton();

                        String Standing = getStandinglink().trim();
                        ExtendedHyperlinkControlWrapper Standing1 = getPane().getCtlSTANDINGINWRMTCUSclayHyperlink();
                        Standing1.setUrl(Standing);
                        String nostroAccountNum = getPane().getACTNUM();
                        String mt103Currency = getDriverWrapper().getEventFieldAsText("EVAM", "v", "c");
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Account number for SI Availability check - " + nostroAccountNum
                                          + " And Credit CCY - " + mt103Currency);
                        }
                        Standing1.setVisible(isSIAvailable(nostroAccountNum, mt103Currency));
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"Standing in Inward--------->" +
                        // ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTINWRMTCUSclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTINWRDCUSPAYCONTclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTINWREMCORRlayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTINWREMPaymentRejectionLayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTINWREMLINKCORRSlayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTINWREMMAINHyperlink();
                        dmsh5.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTINWREMMANULayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTINWREMCanPayRespLayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // REFERE TRACKING
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALINWRMTCUSclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel);
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMREFRALINWRDCUSPAYCONTclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKINWRMTCUSclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCCHECKINWRDCUSPAYCONTclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFRELINWRMTCUSclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCREFERELINWRDCUSPAYCONTclayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  pane.getExtEventBOECAPNew().setEnabled(false);
                  // check list table disabled

                  // Preshipemnt
                  try {

                        String Preshipment = getHyperPreshipment();

                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentELCSETTclayHyperlink();
                        dmsh.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlPreshipmentINWRMTCUSclayHyperlink();
                        dmsh5.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlPreshipmentINWRDCUSPAYCONTclayHyperlink();
                        dmsh6.setUrl(Preshipment);

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());

                  }
                  // Get Packing Credit A/c Outstanding //200523 //vishal g
                  try {

                        String HyperInward = getACCOUNT();
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlACCOUNTINWRMTCUSclayHyperlink();
                        dmsh5.setUrl(HyperInward);                            
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());

                  }
                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();

                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSINWRDCUSPAYCONTclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSINWREMMAINHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSINWREMMANULayHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSINWRMTCUSclayHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSINWREMCanPayRespLayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv6 = getPane().getCtlSFMSINWREMPaymentRejectionLayHyperlink();
                        sfmsExpAdv6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv7 = getPane().getCtlSFMSINWREMCORRlayHyperlink();
                        sfmsExpAdv7.setUrl(getHyperSFMS);
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());

                  }
                  // FIRC
                  try {

                        String val = "FIRC";
                        String FIRCHyperLINK = Link(val);

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
                  }
                  
                  
                  
            

                  // Repay Balance Amount Calaculation 01/07/2016
                  // try {
                  // String EVAM_Amount =
                  // getDriverWrapper().getEventFieldAsText("EVAM", "v", "m");
                  // String Bal_Amount =
                  // getDriverWrapper().getEventFieldAsText("cAQY", "v", "m");
                  // String Currency = getDriverWrapper().getEventFieldAsText("EVAM",
                  // "v", "c");
                  // // //Loggers.general().info(LOG,"Evam Amont" + EVAM_Amount);
                  // // //Loggers.general().info(LOG,"BALA AMOUNT" + Bal_Amount);
                  // // //Loggers.general().info(LOG,"Currency--->" + Currency);
                  // double dbEVAM_Amount = 0.00;
                  // double dbBal_Amount = 0.00;
                  //
                  // if (!(EVAM_Amount == null || EVAM_Amount.length() == 1)) {
                  // // //Loggers.general().info(LOG,"EVAM_Amount" + EVAM_Amount +
                  // // "length"
                  // // + EVAM_Amount.length());
                  // dbEVAM_Amount = Double.valueOf(EVAM_Amount);
                  //
                  // }
                  // if (!(Bal_Amount == null || Bal_Amount.length() == 1)) {
                  // // //Loggers.general().info(LOG,"Bal_Amount" + Bal_Amount + "length"
                  // // +
                  // // Bal_Amount.length());
                  // dbBal_Amount = Double.valueOf(Bal_Amount);
                  //
                  // }
                  // double balance_amt = dbEVAM_Amount - dbBal_Amount;
                  // // //Loggers.general().info(LOG,"balance_amt---->" + balance_amt);
                  //
                  // getPane().setREPBALAT(setValueTOString(balance_amt) + Currency);
                  // } catch (Exception ee) {
                  // Loggers.general().info(LOG,"Error in setting Value" +
                  // ee.getMessage());
                  // }

                  // CR 99 starts
                  try {

                        

                        String tipay_method = getDriverWrapper().getEventFieldAsText(
                        "PYSB", "s", "");

                        String  prev_payAccount = getDriverWrapper().getEventFieldAsText(
                        "cAPH", "s", "");
                        if(prev_payAccount==null)
                            prev_payAccount ="";
                        String  tipayAccount  =  getDriverWrapper().getEventFieldAsText(
                        "PAYA", "a", "u");
                        if(tipayAccount==null)
                        tipayAccount="";
                        String accountType = getDriverWrapper().getEventFieldAsText(
                        "PAYA", "a", "t");

                        Loggers.general () .info(LOG,"Pay funds via account transfer "
                        + tipay_method);
                        Loggers.general () .info(LOG,"Account type onvalidate" + accountType);
                        String rbiPurCode = getPane().getOPURPOS_Name().trim();
                        Loggers.general () .info(LOG,"rbi purpose code onvali---> " + rbiPurCode);

                        if (getMajorCode().equalsIgnoreCase("CPCI")
                        && getMinorCode().equalsIgnoreCase("PCIC")&&step_Input.equalsIgnoreCase("i")) {

                        if (tipay_method.equalsIgnoreCase("T")) {    // set the purpose code if the Pay Funds is  Account Transfer
                        if(!tipayAccount.equalsIgnoreCase(prev_payAccount)&&(!tipayAccount.equalsIgnoreCase(""))) { // condition to check if the account number is changed
                        getPane().setPAYACC(tipayAccount);
                        getWrapper().setPAYACC(tipayAccount);
                        if (accountType.equalsIgnoreCase("SBASBNRE")) {  // condition to check if the account type of the changed account is SBASBNRE
                        getPane().setOPURPOS_Name("P0014");
                        Loggers.general () .info(LOG,"Inside SBASBNRE for account "+tipayAccount);

                        } else {
            //          getPane().setOPURPOS_Name("");
                        Loggers.general () .info(LOG,"Inside NON-NRE account for account " + tipayAccount);
                        }      
                        }
                        }

                        else {  // reset the purpose code if the Pay Funds is not Account Transfer
                        getPane().setPAYACC("");
                        //getPane().setOPURPOS_Name("");
                        }
                           }
                        
                  
            

                  } catch (Exception e) {
                        Loggers.general () .info(LOG,"Inside NON-NRE account for account " + e.getMessage());
                  }
                                    // CR 99 ends
                  
                  String rbipur = getWrapper().getOPURPOS_Name().trim();
                  if ((!rbipur.trim().equalsIgnoreCase("") || rbipur != null)
                              && (!step_Id.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {
                        try {
                              con = getConnection();
                              String query = "select trim(RDRC) from EXTRBIPOSE where RBICOD ='" + rbipur + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"EXTRBIPOSE code population-----> " + query);
                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String rbivalue = rs1.getString(1);
                                    // //Loggers.general().info(LOG,"value of count in while " +
                                    // rbivalue);
                                    getPane().setGPURDES_Name(rbivalue);
                              }

                        } catch (Exception e1) {

                              Loggers.general().info(LOG,"Exception EXTRBIPOSE code population-----> " + e1.getMessage());

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

                  // RBI
                  /*
                   * if(getWrapper().getRBIREFNO().trim().length()<=1) {
                   * //Loggers.general().info(LOG,"RBI Called"); String RBIrefe = RBIREFNO();
                   * //Loggers.general().info(LOG,"RBi reference" + RBIrefe);
                   * StackTraceElement[] e = Thread.currentThread().getStackTrace();
                   * //Loggers.general().info(LOG,e); for(int i=0 ;
                   * i<StackTraceElement.class.getMethods().length;i++ ) {
                   * //Loggers.general().info(LOG," StackTraceElement Class Name [" +
                   * e[i].getClassName()+" MethodName "+e[i].getMethodName()+
                   * " FileName " +e[i].getFileName() + "Looping Times "+ i+" ]");
                   * //getWrapper().setRBIREFNO(RBIrefe);
                   *
                   * } }
                   */

//                try {
//                      currencyCalc();
//                } catch (Exception e) {
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());
//
//                      }
//                }

                  // SFMS
                  // try {
                  // String val = "SFMS";
                  // String getHyperSFMS = getHyperSFMS(val);
                  // ExtendedHyperlinkControlWrapper sfmsExpAdv1 =
                  // getPane().getCtlSFMSINWRDCUSPAYCONTclayHyperlink();
                  // sfmsExpAdv1.setUrl(getHyperSFMS);
                  // } catch (Exception ee) {
                  // // Loggers.general().info(LOG,ee.getMessage());
                  //
                  // }
                  // FIRC
                  try {

                        String val = "FIRC";
                        String FIRCHyperLINK = Link(val);
                        // ExtendedHyperlinkControlWrapper dmsh =
                        // getPane().getCtlFIRCINWRMTCUSclayHyperlink();
                        // dmsh.setUrl(FIRCHyperLINK);
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
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

                  // Update value
                  String rtgspart = getWrapper().getRTGSPART();
                  String paymentType = getWrapper().getPROREMT();
                  try {

                        // getutrNoGenerated();
                  } catch (Exception ee) {
                        Loggers.general().info(LOG,"UTR Number getutrNoGenerated" + ee.getMessage());

                  }
                  if (getMajorCode().equalsIgnoreCase("CPCI")) {
                        
                        getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                        getPane().getExtEventLoanDetailsNew().setEnabled(false);
                        getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                        getPane().getExtEventLoanDetailsUp().setEnabled(false);
                        getPane().getExtEventLoanDetailsDown().setEnabled(false);
                        
                  }

                  if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC")||getMinorCode().equalsIgnoreCase("PCIN")) {
                        try {
                              freezeMttNumber();
                              System.out.println("INSIDE freezeMttNumber OF CREATE");
                        } catch (Exception e) {
                              e.printStackTrace();
                        }}
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

            }
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
//                if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
//                      getPane().onNOSTROINWRMTCUSclayButton();
//                      getPane().onNOSTROOUTWREMCorrespondenceLayButton();
//                }

                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

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

                  // Standing instruction link

                  try {
                        getPane().onRTGSINWRMTCUSclayButton();

                        String Standing = getStandinglink().trim();
                        ExtendedHyperlinkControlWrapper Standing1 = getPane().getCtlSTANDINGINWRMTCUSclayHyperlink();
                        Standing1.setUrl(Standing);
                        String nostroAccountNum = getPane().getACTNUM();
                        String mt103Currency = getDriverWrapper().getEventFieldAsText("EVAM", "v", "c");
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Account number for SI Availability check - " + nostroAccountNum
                                          + " And Credit CCY - " + mt103Currency);
                        }
                        Standing1.setVisible(isSIAvailable(nostroAccountNum, mt103Currency));
                  } catch (Exception ees) {
                        Loggers.general().info(LOG,"Eception Standing in Inward==>" + ees.getMessage());

                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTINWRMTCUSclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTINWRDCUSPAYCONTclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTINWREMCORRlayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTINWREMPaymentRejectionLayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTINWREMLINKCORRSlayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTINWREMMAINHyperlink();
                        dmsh5.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTINWREMMANULayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTINWREMCanPayRespLayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // REFERE TRACKING
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALINWRMTCUSclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel);
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMREFRALINWRDCUSPAYCONTclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKINWRMTCUSclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCCHECKINWRDCUSPAYCONTclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFRELINWRMTCUSclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCREFERELINWRDCUSPAYCONTclayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  EventPane pane = (EventPane) getPane();
                  pane.getExtEventBOECAPNew().setEnabled(false);
                  // check list table disabled

                  // Preshipemnt
                  try {

                        String Preshipment = getHyperPreshipment();

                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentELCSETTclayHyperlink();
                        dmsh.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlPreshipmentINWRMTCUSclayHyperlink();
                        dmsh5.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlPreshipmentINWRDCUSPAYCONTclayHyperlink();
                        dmsh6.setUrl(Preshipment);

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());

                  }
                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();

                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSINWRDCUSPAYCONTclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSINWREMMAINHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSINWREMMANULayHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSINWRMTCUSclayHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSINWREMCanPayRespLayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv6 = getPane().getCtlSFMSINWREMPaymentRejectionLayHyperlink();
                        sfmsExpAdv6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv7 = getPane().getCtlSFMSINWREMCORRlayHyperlink();
                        sfmsExpAdv7.setUrl(getHyperSFMS);
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());

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
                  try {
                        getrbiPurcodeCode();

                  } catch (Exception e) {
                        Loggers.general().info(LOG,"Exception in Purpose code---->" + e.getMessage());

                  }

                  // ----------------------

                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");

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
                              Loggers.general().info(LOG,"Exception FX deal amount more than transaction" + e1.getMessage());
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
                  
                  
                  //Workflow checklist
                  // workflow error configuration
                                    try {
                                          int count3 = 0;

                                           if (step_csm.equalsIgnoreCase("CBS Maker")) {
                                                String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                                            + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                                            + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CBS Maker'";
                                                Loggers.general().info(LOG,"Query for checklist==>"+query_wrk);
                                                con = getConnection();
                                                ps1 = con.prepareStatement(query_wrk);
                                                rs1 = ps1.executeQuery();
                                                while (rs1.next()) {
                                                      count3 = rs1.getInt(1);
                                                      Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while loop----> " + count3);

                                                }
                                          }
                                    //     Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING----> " + query_wrk);

                                          if (count3 < 1 && step_Input.equalsIgnoreCase("i")
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))
                                                      &&(subproductCode.equalsIgnoreCase("EEC")||subproductCode.equalsIgnoreCase("FIC"))
                                                      && (getMinorCode().equalsIgnoreCase("PCIC") || getMinorCode().equalsIgnoreCase("PCIN"))) {

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

                  
                  
                  
                  
                  //End of workflow checklist


                  // NOSTRO VALIDATION

                  try {

                        con = getConnection();
                        String query = "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='C' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCI') AND pos.ORIGAMTCCY NOT IN 'INR' AND pos.KEY97 IN (SELECT MAX(pos.KEY97) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='C' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCI') AND pos.ORIGAMTCCY NOT IN 'INR' AND mas.MASTER_REF = '"
                                    + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt + "' AND bev.REFNO_SERL ='" + evvcount
                                    + "' ) AND mas.MASTER_REF = '" + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt
                                    + "' AND bev.REFNO_SERL ='" + evvcount + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query for warning message in NOSTRO VALIDATION " + query);
                        }
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // //Loggers.general().info(LOG,"Entered while");
                              String cn = rs1.getString(1).trim();

                              if (cn.equalsIgnoreCase("CN") && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                          || step_csm.equalsIgnoreCase("CBS Authoriser"))) {

                                    validationDetails.addWarning(WarningType.Other,
                                                "Pay and Receive should not have Nostro account  [CM]");
                              }

                              else {
                                    // Loggers.general().info(LOG,"value of nostro else--->" + cn);
                              }

                        }

                  } catch (Exception e1) {
                        Loggers.general().info(LOG,"Exception Pay and Receive should not have Nostro" + e1.getMessage());
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

                        String reference = getDriverWrapper().getEventFieldAsText("THE", "r", "").trim();
                        String nostref_MT103102 = getWrapper().getNOSTMT().trim();
                        String nostref_MT940 = getWrapper().getNOSTRM().trim();
                        String query = "";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Nostro ref no nostref_MT103102" + nostref_MT103102);
                        }
                        // String nostref_MT940950 = getWrapper().getNOSTRM().trim();

                        if (nostref_MT103102.length() > 0) {

                              if ((!reference.equalsIgnoreCase(nostref_MT103102)) && (step_Input.equalsIgnoreCase("i"))
                                          && step_csm.equalsIgnoreCase("CBS Maker")) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "Nostro Reference number and Received reference number should be same [CM]");

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Nostro ref no nostref_MT103102 else" + nostref_MT103102);
                                    }
                              }

                              query = "SELECT count(*) as COUNT FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
                                          + nostref_MT103102 + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Nostro ref no nostref_MT103102 query" + query);
                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              int value = 0;
                              if (rs.next()) {
                                    value = rs.getInt(1);

                              }
                              query = "select count(*) from ETT_NOSTRO_UTILITY_MT103 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
                                          + nostref_MT103102 + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Nostro ref no nostref_MT103102 query" + query);
                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              int value103 = 0;
                              if (rs.next()) {
                                    value103 = rs.getInt(1);

                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Nostro ref no nostref_MT103102 count" + value + "value103" + value103);
                              }
                              if ((nostref_MT103102.length() > 0) && (value == 0 && value103 == 0)
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("BRANCHINPUT")
                                                      || step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "MT103 Nostro Reference number is not valid (" + nostref_MT103102 + ") [CM]");

            //                      getPane().setNOSTAMT("");
                                    getPane().setNOSTDAT("");
                                    getPane().setPOOLAMT("");
                                    getPane().setNOSTACC("");
                                    getPane().setMTMESG("");
                                    getPane().setINWMSG("");
                                    getPane().setNOSTOUT("");

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"MT103 Nostro Reference else===>" + value + "value103===>" + value103);
                                    }
                              }

                        } else if (nostref_MT940.length() > 0) {
                              query = "SELECT count(*) FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
                                          + nostref_MT940 + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Nostro ref no nostref_MT940 query" + query);
                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              int val = 0;
                              while (rs.next()) {
                                    val = rs.getInt(1);
                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Nostro ref no nostref_MT940 count" + val);
                              }
                              if ((nostref_MT940.length() > 0) && val == 0 && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("BRANCHINPUT") || step_csm.equalsIgnoreCase("CSM")
                                                      || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "MT940 Nostro Reference number is not valid (" + nostref_MT940 + ") [CM]");
            //                      getPane().setNOSTAMT("");
                                    getPane().setNOSTDAT("");
                                    getPane().setPOOLAMT("");
                                    getPane().setNOSTACC("");
                                    getPane().setMTMESG("");
                                    getPane().setINWMSG("");
                                    getPane().setNOSTOUT("");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"MT940 Nostro Reference else" + val);
                                    }
                              }
                        }

                  } catch (Exception e1) {

                        Loggers.general().info(LOG,"Exception Nostro ref no validation" + e1.getMessage());

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

                  try {

                        // CR 99 starts

                        String tipay_method = getDriverWrapper().getEventFieldAsText(
                        "PYSB", "s", "");

                        String  prev_payAccount = getDriverWrapper().getEventFieldAsText(
                        "cAPH", "s", "");
                        if(prev_payAccount==null)
                            prev_payAccount ="";
                        String  tipayAccount  =  getDriverWrapper().getEventFieldAsText(
                        "PAYA", "a", "u");
                        if(tipayAccount==null)
                        tipayAccount="";
                        String accountType = getDriverWrapper().getEventFieldAsText(
                        "PAYA", "a", "t");

                        Loggers.general () .info(LOG,"Pay funds via account transfer "
                        + tipay_method);
                        Loggers.general () .info(LOG,"Account type onvalidate" + accountType);
                        String rbiPurCode = getPane().getOPURPOS_Name().trim();
                        Loggers.general () .info(LOG,"rbi purpose code onvali---> " + rbiPurCode);

                        if (getMajorCode().equalsIgnoreCase("CPCI")
                        && getMinorCode().equalsIgnoreCase("PCIC")&&step_Input.equalsIgnoreCase("i")) {

                        if (tipay_method.equalsIgnoreCase("T")) {    // set the purpose code if the Pay Funds is  Account Transfer
                        if(!tipayAccount.equalsIgnoreCase(prev_payAccount)&&(!tipayAccount.equalsIgnoreCase(""))) { // condition to check if the account number is changed
                        getPane().setPAYACC(tipayAccount);
                        getWrapper().setPAYACC(tipayAccount);
                        if (accountType.equalsIgnoreCase("SBASBNRE")) {  // condition to check if the account type of the changed account is SBASBNRE
                        getPane().setOPURPOS_Name("P0014");
                        Loggers.general () .info(LOG,"Inside SBASBNRE for account "+tipayAccount);

                        } else {
            //          getPane().setOPURPOS_Name("");
                        Loggers.general () .info(LOG,"Inside NON-NRE account for account " + tipayAccount);
                        }      
                        }
                        }

                        else {  // reset the purpose code if the Pay Funds is not Account Transfer
                        getPane().setPAYACC("");
                        //getPane().setOPURPOS_Name("");
                        }
                           }
                        
                  
                        // CR 99 ends
                  
                  
                        String rbipur = getWrapper().getOPURPOS_Name().trim();
                        String str = rbipur.substring(0, 1);
                        if (((!str.equalsIgnoreCase("P")) || str.equalsIgnoreCase("S")) && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addError(ErrorType.Other, "RBI purpose code should start with P  [CM]");

                        } else {
                              // Loggers.general().info(LOG,"Purpose code is P----> " + str);
                        }


                        // CR 99 Starts---------
                        if (getMajorCode().equalsIgnoreCase("CPCI")
                                    && getMinorCode().equalsIgnoreCase("PCIC")
                                    && (step_Input.equalsIgnoreCase("i"))
                                    && step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Reject")) {

                              if (rbipur.equalsIgnoreCase("P1011")
                                          || rbipur.equalsIgnoreCase("S1011")) {

                                    validationDetails
                                                .addWarning(
                                                            WarningType.Other,
                                                            "Ensure Documents related to LO/PO/BO are taken as Purpose Code selected is P1011/S1011  [CM]");

                              }
                        }
                        // CR 99 Ends---------
                        
                        if (!rbipur.trim().equalsIgnoreCase("") || rbipur != null) {
                              try {
                                    con = getConnection();
                                    String query = "select trim(RDRC) from EXTRBIPOSE where RBICOD ='" + rbipur + "'";
                                    /// //Loggers.general().info(LOG,"EXTRBIPOSE-----> " + query);

                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String rbivalue = rs1.getString(1);
                                          // //Loggers.general().info(LOG,"value of count in while " +
                                          // rbivalue);
                                          getPane().setGPURDES_Name(rbivalue);
                                    }

                              } catch (Exception e1) {
                                    Loggers.general().info(LOG,"Exception RBI purpose code" + e1.getMessage());
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
                        Loggers.general().info(LOG,"Exception RBI purpose code error and population===>" + e1.getMessage());

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

                  if (getMinorCode().equalsIgnoreCase("PCIC")) {
                        try {
                              // //Loggers.general().info(LOG,"get value for LOB");
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }
                  }

                  try {
                        String mercht = getDriverWrapper().getEventFieldAsText("cARQ", "l", "").toString();
                        String masRefNo = getWrapper().getREMERREF().trim();

                        int dmT = 0;

                        String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                                    + masRefNo + "'";
                        // Loggers.general().info(LOG,"Master ref no valid for Import lc" +
                        // dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Master ref no valid" + dms);

                        }
                        Connection con = ConnectionMaster.getConnection();
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
                              validationDetails.addError(ErrorType.Other,
                                          "Related merchanting trade reference number is not valid number [CM]");

                        }

                        else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Master ref no valid in else" + dmT + "mechant trade check box" + mercht);

                              }
                        }

                  } catch (Exception e) {
                        Loggers.general().info(LOG,"Exception in Master ref no validation" + e.getMessage());
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

                  // Preshipemnt
                  try {

                        String Preshipment = getHyperPreshipment();

                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentELCSETTclayHyperlink();
                        dmsh.setUrl(Preshipment);

                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTINWRDCUSPAYCONTclayHyperlink();
                        dmsh2.setUrl(Preshipment);
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());

                  }
                  
                  // Category code populate based on input branch
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                        con = ConnectionMaster.getConnection();
                        if (!(BranchCode.length() == 0)) {
                              String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
                              // //Loggers.general().info(LOG,"BranchCode Query - " + sql6);
                              PreparedStatement ps1 = con.prepareStatement(sql6);
                              ResultSet rs1 = ps1.executeQuery();
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

                  String cust = getDriverWrapper().getEventFieldAsText("BEN", "p", "no").trim();
                  // Loggers.general().info(LOG,"Primary customer taking ----> " + cust);
                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // //Loggers.general().info(LOG,"charge account collected " + chargecol);
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
                                          // Loggers.general().info(LOG,"custoemr number in query" +
                                          // custval);
                                          // Loggers.general().info(LOG,"custoemr number in
                                          // transaction" + cust);
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Account selected in charges does not belong to the customer [CM]");

                                    } else {
                                          // Loggers.general().info(LOG,"charge account collected
                                          // matched");
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

                        String productVal = getDriverWrapper().getEventFieldAsText("BEN", "p", "cAJB").trim();
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

                  // IFSC code validation

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
                                                      "Invalid Beneficiary IFSC code(" + Ifsc + ")  [CM]");
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
                        }
                  }

                  try {
                        int noOfRecord = 0;
                        con = getConnection();
                        String customerId = "";
                        String amount_am = "";
                        String currency = "";
                        String masterRef = "";
                        customerId = getDriverWrapper().getEventFieldAsText("BEN", "p", "no").trim();
                        // //Loggers.general().info(LOG,"Customer Id " + customerId);
                        amount_am = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                        String amount = amount_am.replaceAll("[^0-9]", "");
                        // //Loggers.general().info(LOG,"Currency is " + amount_am);
                        currency = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        // //Loggers.general().info(LOG,"Currency is " + currency);
                        masterRef = getmasRefNo();
                        if (amount_am.length() > 0 && currency.length() > 0) {

                              // //Loggers.general().info(LOG,"Duplicate Record Query is " +
                              // duplicateMaster);

                              String duplicateMaster = "select COUNT(*) as COUNT from ETT_DUPLICAT_WARNING_VIEW where PRICUSTMNM='"
                                          + customerId + "' and AMOUNT='" + amount + "' and CCY='" + currency + "' and MASTER_REF !='"
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
                              //// Loggers.general().info(LOG,"Connection Failed! Check output
                              //// console");
                              e.printStackTrace();
                        }
                  }

                  // Preshipment value populated
                  //
                  // List<ExtEventLoanDetails> preship = (List<ExtEventLoanDetails>)
                  // getWrapper().getExtEventLoanDetails();
                  // String currency = "";
                  // String amt = "";
                  // if (preship.size() < 5) {
                  // getPane().setTOTUSD(0 + " USD");
                  // getPane().setTOTINR(0 + " INR");
                  // getPane().setTOTEUR(0 + " EUR");
                  // getPane().setTOTJPY(0 + " JPY");
                  // getPane().setTOTGBP(0 + " GBP");
                  // }
                  //
                  // for (int l = 0; l < preship.size(); l++) {
                  // // for(ExtEventLoanDetails preshipment : preship){
                  // ExtEventLoanDetails preshipment = preship.get(l);
                  //
                  // // //Loggers.general().info(LOG,"fIRST VALUE ==>" +
                  // // preshipment.toString());
                  //
                  // currency = preshipment.getREAMOUNTCurrency();
                  // // //Loggers.general().info(LOG,"PreShipment value in currency" +
                  // // currency);
                  // amt = preshipment.getREAMOUNT().toString();
                  // // //Loggers.general().info(LOG,"PreShipment value in amount" + amt);
                  // if (currency.equalsIgnoreCase("USD")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency USD" + amt + currency);
                  // getPane().setTOTUSD(amt + " USD");
                  // } else {
                  // // getPane().setTOTUSD(0 + " USD");
                  // //Loggers.general().info(LOG,"USD---------->");
                  // }
                  // if (currency.equalsIgnoreCase("INR")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency INR" + amt + currency);
                  // getPane().setTOTINR(amt + " INR");
                  // } else {
                  // // getPane().setTOTINR(0 + " INR");
                  // //Loggers.general().info(LOG,"INR---------->");
                  // }
                  // if (currency.equalsIgnoreCase("JPY")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency JPY" + amt + currency);
                  // getPane().setTOTJPY(amt + " JPY");
                  // } else {
                  //
                  // // getPane().setTOTJPY(0 + " JPY");
                  // //Loggers.general().info(LOG,"JPY---------->");
                  // }
                  // if (currency.equalsIgnoreCase("EUR")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency EUR" + amt + currency);
                  // getPane().setTOTEUR(amt + " EUR");
                  // } else {
                  // // getPane().setTOTEUR(0 + " EUR");
                  // //Loggers.general().info(LOG,"EUR---------->");
                  // }
                  // if (currency.equalsIgnoreCase("GBP")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency GBP" + amt + currency);
                  // getPane().setTOTGBP(amt + " GBP");
                  // } else {
                  // // getPane().setTOTGBP(0 + " GBP");
                  // //Loggers.general().info(LOG,"GBP---------->");
                  // }
                  //
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

                  try {

                        /*String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, BASEEVENT BEV, ETT_REFERRAL_TRACKING REF WHERE MAS.KEY97 = BEV.MASTER_KEY AND trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO) AND BEV.STATUS <>'a' AND TRIM(REF.MASTER_REF_NO) ='"
                                    + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode + "' AND REF.SUB_PRODUCT_CODE = '"
                                    + subproductCode
                                    + "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID";*/
                        String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, ETT_REFERRAL_TRACKING REF WHERE  trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)  AND TRIM(REF.MASTER_REF_NO) ='"
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
                        Loggers.general().info(LOG,"Entered while referal step" + step + "count" + count);
                              if (count > 0) {
                                    if ((step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Reject"))
                                                && !step_csm.equalsIgnoreCase("CBS Authoriser")) {
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
                                          Loggers.general().info(LOG,"prepared statement"+ps1);
                                          //ArrayList<String> al = new ArrayList<String>();
                                          rs = ps1.executeQuery();
                                          if(rs!=null)
                                          {
                                                Loggers.general().info(LOG,"Result set exsists"+rs);
                                          }
                                          else
                                          {
                                                Loggers.general().info(LOG,"Result set not exsists"+rs);
                                          }
                                          
                                          ArrayList<String> al = new ArrayList<String>();
                                          Loggers.general().info(LOG,"arraylist"+al);
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
                                                Loggers.general().info(LOG,"Referral error"+ref);
                                                al.add(ref);
                                          }
                                          warningMessage = StringUtils.join(al, "\n");
                              Loggers.general().info(LOG,"Single Warning Message " +
                                           warningMessage);

                                          validationDetails.addWarning(WarningType.Other, warningMessage);

                                    } else if (step_csm.equalsIgnoreCase("CBS Checker")
                                                || step_csm.equalsIgnoreCase("CBS Authoriser")) {

                                          String ref = null;
                                          String statusReferral = null;
                                          String warningMessage = null;
                                          String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                                                      + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                                                      + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                                                      + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
                                          Loggers.general().info(LOG,"query for referral----------------->" +query6);

                                          ps1 = con.prepareStatement(query6);
                                          Loggers.general().info(LOG,"prepared statement"+ps1);
                                    //    ArrayList<String> al = new ArrayList<String>();
                                          rs= ps1.executeQuery();
                                          if(rs!=null)
                                          {
                                                Loggers.general().info(LOG,"Result set exsists"+rs);
                                          }
                                          else
                                          {
                                                Loggers.general().info(LOG,"Result set not exsists rs "+rs);
                                          }
                                          
                                          ArrayList<String> al = new ArrayList<String>();
                                          Loggers.general().info(LOG,"arraylist"+al);
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
                                                Loggers.general().info(LOG,"Referral error"+ref);
                                                al.add(ref);
                                          }
                                          warningMessage = StringUtils.join(al, "\n");
                                    Loggers.general().info(LOG,"Single Warning Message " +
                                     warningMessage);

                                          validationDetails.addError(ErrorType.Other, warningMessage);

                                    } else {
                                          // Loggers.general().info(LOG,"referal step in step" +
                                          // step_csm);
                                    }
                              }
                        }
                        
                        
                        
                        
                        // //Loggers.general().info(LOG,"Entered while referal count out of
                        // loop"
                        // + count);
                  } catch (Exception e1) {
                        Loggers.general().info(LOG,"Exception referal count" +
                        e1.getMessage());
                  } finally {
                        try {
                              if (rs!= null)
                                    rs.close();
                              if (rs1!= null)
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
                                    Loggers.general().info(LOG,"ArrayList Single Warning CBS 2nd" + warningMessage);
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
                  finally {
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

                  String productyp = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  String amountcur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c").trim();
                  String rtgspart = getWrapper().getRTGSPART();

                  // unbalanced posting error
                  try {
                        if ((step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise"))
                                    && (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))
                                    && (!amountcur.equalsIgnoreCase("INR")) && rtgspart.equalsIgnoreCase("FULL")) {
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
                                          // Loggers.general().info(LOG,"Unbalanced posting valid");
                                    } else if ((step_csm.equalsIgnoreCase("CBS Authoriser")
                                                || step_csm.equalsIgnoreCase("Authorise"))
                                                && (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))) {

                                          validationDetails.addError(ErrorType.Other, "Please enter Valid RTGS account number [CM]");
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
                        }
                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception Unbalanced posting===>" +
                        // e1.getMessage());
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

                  String paymentType = getWrapper().getPROREMT();
                  try {

                        getutrNoGenerated();
                  } catch (Exception ee) {
                        Loggers.general().info(LOG,"UTR Number getutrNoGenerated" + ee.getMessage());

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
                        double nosrtoAmt = 0;
                        double nosrtoOutAmt = 0;
                        double creditAmt = 0;
                        double creditAmt940 = 0;
                        BigDecimal nosrtoAmount = new BigDecimal("0");
                        BigDecimal nosrtoOutAmount = new BigDecimal("0");
                        BigDecimal creditAmount = new BigDecimal("0");
                        BigDecimal creditAmount940 = new BigDecimal("0");
                        String nostref_MT103102 = getWrapper().getNOSTMT().trim();
                        String nostref_MT940 = getWrapper().getNOSTRM().trim();

                        try {
                              String nosAmt = getDriverWrapper().getEventFieldAsText("cABI", "v", "m");
                              if (!nosAmt.equalsIgnoreCase("") && nosAmt.length() > 0) {
                                    nosrtoAmt = Double.parseDouble(nosAmt);
                              }
                              nosrtoAmount = new BigDecimal(nosAmt);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," Nostro utilizaton amount in inward remittance" + nosrtoAmount);
                              }
                        } catch (Exception e) {
                              nosrtoAmount = new BigDecimal("0");
                        }

                        try {
                              String nosOutAmt = getDriverWrapper().getEventFieldAsText("cBJL", "v", "m");
                              if (!nosOutAmt.equalsIgnoreCase("") && nosOutAmt.length() > 0) {
                                    nosrtoOutAmt = Double.parseDouble(nosOutAmt);
                              }
                              nosrtoOutAmount = new BigDecimal(nosOutAmt);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," Nostro Outstanding amount in inward remittance" + nosrtoOutAmount);
                              }
                        } catch (Exception e) {
                              nosrtoOutAmount = new BigDecimal("0");
                        }

                        try {
                              String credAmt = getDriverWrapper().getEventFieldAsText("cANE", "v", "m");
                              if (!credAmt.equalsIgnoreCase("") && credAmt.length() > 0) {
                                    creditAmt = Double.parseDouble(credAmt);

                              }
                              creditAmount = new BigDecimal(credAmt);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," Nostro credit amount in inward remittance" + creditAmount);
                              }
                        } catch (Exception e) {
                              creditAmount = new BigDecimal("0");
                        }

                        try {
                              String credAmt940 = getDriverWrapper().getEventFieldAsText("cABN", "v", "m");
                              if (!credAmt940.equalsIgnoreCase("") && credAmt940.length() > 0) {
                                    creditAmt940 = Double.parseDouble(credAmt940);

                              }

                              creditAmount940 = new BigDecimal(credAmt940);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," Nostro credit 940 amount in inward remittance" + creditAmount940);
                              }
                        } catch (Exception e) {
                              creditAmount940 = new BigDecimal("0");
                        }

                        if (nosrtoAmt > 0 && nosrtoOutAmt > 0 && (nosrtoOutAmount.compareTo(nosrtoAmount) < 0)

                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1")
                                                || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "Notsro utilization amount is greater than nostro Outstanding amount [CM]");
                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," Notsro Outstanding amount is greater" + nosrtoOutAmount);
                              }
                        }

                        if (nostref_MT103102.length() > 0) {
                              if (nosrtoAmt > 0 && creditAmt > 0 && (creditAmount.compareTo(nosrtoAmount) < 0)
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Notsro utilization amount is greater than nostro credit amount [CM]");
                              }

                              else if (nosrtoAmt > 0 && creditAmt > 0 && (creditAmount.compareTo(nosrtoAmount) > 0)
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "Notsro utilization amount is less than nostro credit amount [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Nostro creditAmt amount else" + creditAmount + "nosrtoAmt===>" + nosrtoAmount);
                                    }
                              }
                        } else if (nostref_MT940.length() > 0) {
                              if (nosrtoAmt > 0 && creditAmt940 > 0 && (creditAmount940.compareTo(nosrtoAmount) < 0)
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Notsro utilization amount is greater than nostro credit amount [CM]");
                              }

                              else if (nosrtoAmt > 0 && creditAmt940 > 0 && (creditAmount940.compareTo(nosrtoAmount) > 0)
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "Notsro utilization amount is less than nostro credit amount [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Nostro creditAmt amount else" + creditAmount940 + "nosrtoAmt===>" + nosrtoAmount);
                                    }
                              }
                        }

                  } catch (Exception ex) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in Nostro amount and creadit amount" + ex.getMessage());
                        }

                  }

      //-------------------------Start of Preshipment--------------
                  
                  if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC")
                              && (step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser")||step_csm.equalsIgnoreCase("CBS Reject"))) {
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
                  // For preshipment subproduct issue
            /*    if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC"))
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
            /*    if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC")
                              && ((step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser"))) ){
                        ConnectionMaster connectionMaster = new ConnectionMaster();
                        Loggers.general().info(LOG,"Preshipment knock off amount check");
                        int val=connectionMaster.getPreshipment();
                        if(val==1)
                        {
                        validationDetails.addError(ErrorType.Other, "Preshipment knock of amount is greater than the outstanding amount[CM]");
                        }     
                        }*/
                  if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC")
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
                                                            validationDetails.addError(ErrorType.Other, "Pre shipment knocked off reference number already fetched in" +master+ ". Kindly check the outstanding amount[CM] ");
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
                  if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC")
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
                                    Loggers.general().info(LOG,"close shp---->");
                                    
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
                              Loggers.general().info(LOG,"Exception in preshipment error1===>" + e.getMessage());
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
                        
                  }
                  
                  try{
                        //String paymentType = getWrapper().getPROREMT();
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
                  //CR 140 starts
                  if(getMajorCode().equalsIgnoreCase("CPCI") && (getMinorCode().equalsIgnoreCase("PCIC")||getMinorCode().equalsIgnoreCase("PCIN")))
                  {
                  try {
                        int cnt = 0;
                        Loggers.general () .info(LOG,"PCR/PCF--");
                        cnt = preshipWar();
                        Loggers.general () .info(LOG,"count" + cnt);
                        String subProductype = getDriverWrapper().getEventFieldAsText("PTP",
                                    "s", "");
                        Loggers.general () .info(LOG,"subProductype in cpcipcic==>" + subProductype);
                        if (cnt == 1&&(subProductype.equalsIgnoreCase("XAR")||
                                    subProductype.equalsIgnoreCase("FIC")||subProductype.equalsIgnoreCase("EEC")
                                    ||subProductype.equalsIgnoreCase("EEF")||subProductype.equalsIgnoreCase("MIS")) ) {
                              //Loggers.general () .info(LOG,"duplicate reference");
                              validationDetails.addWarning(WarningType.Other,"Packing credits are outstanding for the customer. Please check if this payment is to be used to knock off exsisting PCR/PCF [CM]");             
                              }
                        
                  } catch (Exception ee) {
                        //ee.printStackTrace();
                        Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());

                  }
//                try {
//                      String contractReference = getDriverWrapper().getEventFieldAsText("FOCR", "s", "");
//                      String pay = getDriverWrapper().getEventFieldAsText("ACT", "s", "");
//                      System.out.println("contract error "+contractReference+" "+pay);
//                      if((contractReference==null || contractReference.isEmpty() || contractReference.trim().equals("")) && (pay.equalsIgnoreCase("P"))) {
//                            validationDetails.addWarning(WarningType.Other, "Reference Contract Number/Deal ID is not entered in Settlements tab[CM]");
//                      }
//                }
//                catch (Exception ee) {
//                      ee.printStackTrace();
//                      //Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());
//
//                }
//                try {
//                      String lrnDetails = getDriverWrapper().getEventFieldAsText("cBVJ", "s", "").trim();
//                //    String investee = getDriverWrapper().getEventFieldAsText("cBVI", "s", "").trim();
//                      String rbipurposecode = getDriverWrapper().getEventFieldAsText("cBUW", "s", "");
//                      String prodsubtype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
//                      if(rbipurposecode.equalsIgnoreCase("P0012") &&prodsubtype.equalsIgnoreCase("21R") ){
//
//                      if(lrnDetails.length()!=15) {
//                            System.out.println("dfb :  error "+lrnDetails.length());
//                            validationDetails.addError(ErrorType.Other, " Length of LRN details should be of 15  ");
//                      }
//                      }
//                      
//                }
//                catch(Exception e){
//                      e.printStackTrace();
//                }
                  }
                  //CR 140 ends
                  
                  // CR220 Changes Startes here
                  String subProductype = getDriverWrapper().getEventFieldAsText("PTP",
                              "s", "");
                  if (getMajorCode().equalsIgnoreCase("CPCI")&&(subProductype.equalsIgnoreCase("XAR")||subProductype.equalsIgnoreCase("MIS"))) {
                  System.out.println("CR-220 Validation starts here");
                  try {
                        con = getConnection();
                        String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
                                    + MasterReference
                                    + "' and BEV.REFNO_PFIX= '"
                                    + evnt
                                    + "' and BEV.REFNO_SERL = '" + evvcount + "'";
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              count = rs1.getInt(1);
                        }

                        if (count > 0
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm
                                                .equalsIgnoreCase("CBS Maker 1"))) {
                              System.out.println("CR-220 Validation setting up Error");
                              validationDetails.addError(ErrorType.Other,
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

                  if ((getMinorCode().equalsIgnoreCase("PCIP"))
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
                  try
                  {
                  getPostingFxrate();
                  }
                  catch(Exception e)
                  {
                        
                  }
                  if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC")||getMinorCode().equalsIgnoreCase("PCIN")){
                        try {
                              String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                                    String eventref = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                                    String custId = getDriverWrapper().getEventFieldAsText("PRM", "p","cu").trim();
                                    String uniqueId = getDriverWrapper().getEventFieldAsText("cBUB", "s", "").trim();
                                    String branch= getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                                    String eventCode1 = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                              String inputXml = "<?xml version=\"1.0\" standalone=\"yes\"?><ServiceRequest xmlns:m='urn:messages.service.ti.apps.tiplus2.misys.com' xmlns='urn:control.services.tiplus2.misys.com' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>" +
                                          "<RequestHeader>" +
                                          "<Service>Treasury</Service>" +
                                          "<Operation>Validate</Operation>" +
                                          "<Credentials>" +
                                          "<Name>Name</Name>" +
                                          "<Password>Password</Password>" +
                                          "<Certificate>Certificate</Certificate>" +
                                          "<Digest>Digest</Digest>" +
                                          "</Credentials>" +
                                          "<ReplyFormat>FULL</ReplyFormat>" +
                                          "<NoRepair>Y</NoRepair>" +
                                          "<NoOverride>Y</NoOverride>" +
                                          "<CorrelationId>CorrelationId</CorrelationId>" +
                                          "<TransactionControl>NONE</TransactionControl>" +
                                          "</RequestHeader>" +
                                          "<TreasuryValidateRequest>" +
                                          "<MASTERREF>"+masReference+"</MASTERREF>" +
                                          "<EVENTREF>"+eventref+"</EVENTREF>" +
                                          "<CUSTID>"+custId+"</CUSTID>" +
                                          "<UNIQUEID>"+uniqueId+"</UNIQUEID>" +
                                          "<INPUTBRANCH>"+branch+"</INPUTBRANCH>" +
                                          "<EVENTCODE>"+eventCode1+"</EVENTCODE>" +
                                           "<VALIDATIONTYPE>BOVERIFY</VALIDATIONTYPE>"+
                                          "</TreasuryValidateRequest></ServiceRequest>";
                              
                              ThemeTransportClient aClient = new ThemeTransportClient();
                              String resultXml=aClient.invoke("Treasury", "Validate", inputXml);
                              
                              String errMsg=getTagValue(resultXml, "ERRORMSG");
                              String warningMsg=getTagValue(resultXml, "WARNMSG");
                              if(errMsg != null && !errMsg.trim().isEmpty()) {                              
                                    {
                                          validationDetails.addError(ErrorType.Other, errMsg);
                                    }
                              }
                              else if(warningMsg != null && !warningMsg.trim().isEmpty()) {                             
                                    {
                                          validationDetails.addWarning(WarningType.Other, warningMsg);
                                    }
                              }
                        }
                        catch(Exception e){
                              e.printStackTrace();
                        }
//                      try {
//                            //getforwardTotal();
//                            System.out.println("forward total value data:");
//                      }
//                      catch (Exception e) {
//                      e.printStackTrace();
//                      //    System.out.println("outside BUYERS data:"+e);
//
//                      }
                        try {
                              String customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");
                              con = ConnectionMaster.getConnection();
                              String query = "select panno,IECODE from extcust where cust= '" + customer + "'";
                              System.out.println("pan number "+query);
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              while (rs.next()) {
                                    String cust1 = rs.getString(1);
                                    String iecode = rs.getString(2);
                                    // Loggers.general().info(LOG,"Customer" + cust);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Customer" + cust1);
                                    }
                                    getPane().setPANDETAI(cust1);
                                    getPane().setIECODE(iecode);
                              }
                        }
                        catch(Exception e){
                              e.printStackTrace();
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
                                    e.printStackTrace();
                              }
                        }
                  
                  }
                  if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC")){
                        try {
                        getErrorPurposeCode(validationDetails);
                        getCountryRisk();
                      }
                      catch (Exception e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                        try {
                              getForwardReference();
                              
                        //    getipbranchCode();
                              System.out.println("forward contract reference:");
                        }
                        catch (Exception e) {
                        e.printStackTrace();
                        //    System.out.println("outside BUYERS data:"+e);

                        }
                         try{
                   
                     getUidWarning(validationDetails);
                     System.out.println("updaters");
                     }
                  catch (Exception e)
                  {
                     System.out.println("Exception in getAcoount-->" + e.getMessage());
                  }
                        }
            
//CR 215 ends here
                  
            /*    if (amount.length() > 1 && currency.length() > 0) {

                        // //Loggers.general().info(LOG,"Duplicate Record Query is " +
                        // duplicateMaster);

                        String duplicateMaster = "select COUNT(*) as COUNT  from ETT_INVOICE_DUPLICAT_VIEW where PRICUSTMNM='"
                                    + customerId + "' and AMOUNT='" + amount + "' and CCY='" + currency + "' and MASTER_REF !='"
                                    + MasterReference + "'  and benef_num='"+ beneaccnum +"' and invoice_num='"+ invnum +"'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"invoice Duplicate Record Query is " + duplicateMaster);
                        }
                        ps1 = con.prepareStatement(duplicateMaster);
                        rs1 = ps1.executeQuery();
                        if (rs1.next()) {
                              noOfRecord = rs1.getInt("COUNT");
                              count=count+noOfRecord;
                              Loggers.general().info(LOG,"Count "+count);
                        }
                        
                  }
                  }
                  */
                  //-------------------------End of Preshipment--------------
                  // Update value
                  // if (rtgspart.equalsIgnoreCase("FULL")) {
                  // try {
                  // con = getConnection();
                  // String sql = "SELECT * from KMB_RTGS_NEFT_VIEW where MASTER_REF =
                  // '" + MasterReference
                  // + "' AND EVENT_REF = '" + eventCode + "'";
                  // // //Loggers.general().info(LOG,"Query value--->" + sql);
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
                  // part--->" + rtgs + "and" + rtgs_amt);
                  // }
                  // } else {
                  // //Loggers.general().info(LOG,"RTGS NEFT value and type else loop--->" +
                  // rtgs + "and" + rtgs_amt);
                  // }
                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                        }
                  }
                  if (getMajorCode().equalsIgnoreCase("CPCI") && getMinorCode().equalsIgnoreCase("PCIC")||getMinorCode().equalsIgnoreCase("PCIN")) {
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
                              getPostingBranch(validationDetails);
                              }catch (Exception e) {
                                    e.printStackTrace();
                              }
                  }
            }

      }

      private int getString(String string) {
            // TODO Auto-generated method stub
            return 0;
      }

      private int getInt(int i) {
            // TODO Auto-generated method stub
            return 0;
      }
      private double getdraftAmt(String getdraft1Val) {
            double value = 0.0;
            if (getdraft1Val.equals("") || getdraft1Val == null || getdraft1Val.isEmpty()) {
                  getdraft1Val = "0.00";
            }
            value = Double.valueOf(getdraft1Val);
            // Loggers.general().info(LOG,"value Sending" + value);
            return (value);

      }

      private String setValueTOString(double d1) {
            DecimalFormat df = new DecimalFormat("#.##");
            BigDecimal dValue = new BigDecimal(df.format(d1)).setScale(2, RoundingMode.HALF_UP);
            return String.valueOf(dValue);
      }

}