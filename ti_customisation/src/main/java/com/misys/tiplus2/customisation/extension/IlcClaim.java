package com.misys.tiplus2.customisation.extension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
//import org.apache.log4j.Logger;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceData;
import com.misys.tiplus2.customisation.entity.ExtEventBUYERSCREDIT;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;

public class IlcClaim extends ConnectionMaster {
      // private static final Logger logger =
      // Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(IlcClaim.class);
      Connection con = null;

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
                  // GETTING LOB
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

                  if (getMinorCode().equalsIgnoreCase("PBIC")) {
                        try {
                              // //Loggers.general().info(LOG,"get value for LOB");
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG, "Exception getLOBCREATE =====>" + ee.getMessage());
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }
                  }

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in currency value population---->" + e.getMessage());

                        }
                  }

                  // -------------------------------//

                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSINWRMTclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSINWBANKContinueLayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSINWBANKcanpayrequestLayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSINWBANKPaymeRejectionLayHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSINWBANKPaymeResponseLayHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSINWBANKCorrespondanceLayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTIMPLCCLAIMRECclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTINWBANKContinueLayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTINWBANKCorrespondanceLayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTINWBANKPaymeResponseLayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTINWBANKcanpayrequestLayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTINWBANKMANULlayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // BOE
                  try {
                        String val = "BOE";
                        String BoeLink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlBOEIMPLCCLAIMRECclayHyperlink();
                        dmsh.setUrl(BoeLink);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                  }
                  // Get Packing Credit A/c Outstanding //200523 //vishal g
                  try {

                        String HyperInward = getACCOUNT();
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlACCOUNTINWRMTclayHyperlink();
                        dmsh5.setUrl(HyperInward);
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());

                  }
                  if (getMajorCode().equalsIgnoreCase("CPBI") && getMinorCode().equalsIgnoreCase("PBIC")
                              || getMinorCode().equalsIgnoreCase("PBIN")) {
                        try {
                              freezeMttNumber();
                              System.out.println("INSIDE freezeMttNumber OF remmitance");
                        } catch (Exception e) {
                              e.printStackTrace();
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
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

                  if (getMinorCode().equalsIgnoreCase("PBIC")) {
                        try {
                              // //Loggers.general().info(LOG,"get value for LOB");
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG, "Exception getLOBCREATE =====>" + ee.getMessage());
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }
                  }

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in currency value population---->" + e.getMessage());

                        }
                  }
                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSINWRMTclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSINWBANKContinueLayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSINWBANKcanpayrequestLayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSINWBANKPaymeRejectionLayHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSINWBANKPaymeResponseLayHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSINWBANKCorrespondanceLayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001

                  // Update value
                  String rtgspart = getWrapper().getRTGSPART();
                  // if (rtgspart.equalsIgnoreCase("FULL")) {
                  // try {
                  // con = getConnection();
                  // String sql = "SELECT SUM(DECODE(POS.DR_CR_FLG, 'D',
                  // (POS.AMOUNT)*-1, (POS.AMOUNT))) AS AMOUNT, TRIM(POS.CCY) AS
                  // CURRENCY FROM master MAS, BASEEVENT BEV, RELITEM REL, POSTING POS
                  // WHERE MAS.KEY97 = BEV.MASTER_KEY AND BEV.KEY97 = REL.EVENT_KEY
                  // AND REL.KEY97 = POS.KEY97 AND POS.POSTED_AS IS NULL AND
                  // POS.ACC_TYPE = 'RTGS' AND Mas.Master_Ref = '"
                  // + MasterReference + "' AND
                  // (Bev.Refno_Pfix||lpad(Bev.Refno_Serl,3,0)) = '" + eventCode
                  // + "' GROUP BY POS.CCY";
                  // // Loggers.general().info(LOG,"Query value for RTGS amount--->" +
                  // // sql);
                  // ps = con.prepareStatement(sql);
                  // rs = ps.executeQuery();
                  // while (rs.next()) {
                  // String rtgsVal = rs.getString(1);
                  // getPane().setRTGSNEFT(rtgsVal + " INR");
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
                  // String rtgs = getWrapper().getPROREMT();
                  // String rtgs_amt = getWrapper().getRTGSNEFT();
                  // double rtgs_lim = 200000;
                  // // //Loggers.general().info(LOG,"RTGS NEFT value and type--->" + rtgs +
                  // "and"
                  // // + rtgs_amt);
                  // if (rtgs_amt.length() > 0 && rtgs.equalsIgnoreCase("RTG")) {
                  // double rtgs_double = Double.valueOf(rtgs_amt);
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

                  // IFSC code validation

                  String Ifsc = getWrapper().getIFSCCO_Name().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);
                  if (Ifsc.trim().equalsIgnoreCase("") || Ifsc == null) {
                        // validationDetails.addWarning(WarningType.Other, "Receiver
                        // IFSC code should not be blank");
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

                  // NOSTRO VALIDATION

                  try {

                        con = getConnection();

                        String query = "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='C' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPBI') AND pos.ORIGAMTCCY NOT IN 'INR' AND pos.KEY97 IN (SELECT MAX(pos.KEY97) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='C' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPBI') AND pos.ORIGAMTCCY NOT IN 'INR' AND mas.MASTER_REF = '"
                                    + MasterReference + "' ) AND mas.MASTER_REF = '" + MasterReference + "'";
                        // //Loggers.general().info(LOG,"Query for warning message in NOSTRO
                        // VALIDATION " + query);
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // //Loggers.general().info(LOG,"Entered while");
                              String cn = rs1.getString(1).trim();
                              // //Loggers.general().info(LOG,"value of nostro after trim---->" +
                              // cn);

                              if (cn.equalsIgnoreCase("CN") && (step_csm.equalsIgnoreCase("CBS Maker")
                                          || step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise"))) {

                                    validationDetails.addError(ErrorType.Other,
                                                "Pay and Receive should not have Nostro account  [CM]");
                              }

                              else {
                                    // Loggers.general().info(LOG,"value of nostro else--->" + cn);
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

                  // CR 215 starts here

                  if ((getMinorCode().equalsIgnoreCase("PBIP"))
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
                  // Notes Populated in Summary

                  try {
                        con = getConnection();
                        String query = "SELECT mas.MASTER_REF, note.* FROM master mas, TIDATAITEM tid, NOTE WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 and NOTE.TYPE not in (3,129,1089) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
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
                  try {
                        String paymentType = getWrapper().getPROREMT();
                        String utrNum = getWrapper().getUTRNO().trim();
                        String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
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

                  /// error configured for duplicate events
                  try {
                        int cnt = 0;
                        Loggers.general().info(LOG, "duplicate reference");
                        cnt = getduplicate();
                        Loggers.general().info(LOG, "count" + cnt);
                        if (cnt > 1) {
                              Loggers.general().info(LOG, "duplicate reference");
                              validationDetails.addError(ErrorType.Other,
                                          "The event reference no already exsist kindly abort and recreate the event [CM]");
                        }
                  } catch (Exception ee) {
                        ee.printStackTrace();
                        Loggers.general().info(LOG, "duplicate reference" + ee.getMessage());

                  }
                  try {
                        getPostingFxrate();
                  } catch (Exception e) {

                  }

                  if ((getMajorCode().equalsIgnoreCase("CPBI")) && (getMinorCode().equalsIgnoreCase("PBIC"))) {

                        /*
                         * try { System.out.println("BUYERS CREDIT"); con = getConnection(); String
                         * masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                         * String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r",
                         * "").trim(); // String prodType =
                         * getDriverWrapper().getEventFieldAsText("SLN", "s", "").trim();
                         * System.out.println("BOE_Warning: "+masterref+", "+eventREF);
                         *
                         *
                         * String query =
                         * "SELECT TRIM(EXTB.SNUNBER),TRIM(EXTB.BCGREFN),TRIM(EXTB.DOCREFNO) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTBCR EXTB"
                         * + " WHERE MAS.KEY97 = BEV.MASTER_KEY" + " AND BEV.KEY97 = EXT.EVENT" +
                         * " AND EXTB.FK_EVENT = EXT.KEY29" + " AND MAS.MASTER_REF = '"+masterref+"'" +
                         * " AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='"+eventREF+"'";
                         * System.out.println(query); ps1 = con.prepareStatement(query); rs1 =
                         * ps1.executeQuery(); if (!rs1.next()) { validationDetails.addError(ErrorType.
                         * Other," Buyers Credit Details to be entered.."); } else {
                         * System.out.println("Buyers Credit Details HAS DATA"); }
                         *
                         * } catch (Exception e) { e.printStackTrace(); }
                         */
                        try {

                              // String finAmt = getDriverWrapper().getEventFieldAsText("B+FD", "v", "m");
                              // String financeParty = getDriverWrapper().getEventFieldAsText("B+FT", "p",
                              // "cu").trim();

                              List<ExtEventBUYERSCREDIT> shipTable = (List<ExtEventBUYERSCREDIT>) getWrapper()
                                          .getExtEventBUYERSCREDIT();
                              // System.out.println("inside invoice data" );
                              // int size=shipTable.size();
                              System.out.println("invoice data size" + shipTable.size());

                              if (shipTable.size() == 0) {

                                    validationDetails.addWarning(WarningType.Other, " Buyers Credit Details to be entered..");
                                    System.out.println("inside BUYERS warning:");
                              } else {
                                    System.out.println("buyres has data");
                              }

                              // }

                        }

                        catch (Exception e) {
                              e.printStackTrace();
                              System.out.println("outside BUYERS data:" + e);

                        }
                        try {
                              String customer = getDriverWrapper().getEventFieldAsText("DBT", "p", "cu");
                              con = ConnectionMaster.getConnection();
                              String query = "select panno from extcust where cust= '" + customer + "'";
                              System.out.println("pan number " + query);
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              while (rs.next()) {
                                    String cust1 = rs.getString(1);
                                    // Loggers.general().info(LOG,"Customer" + cust);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Customer" + cust1);
                                    }
                                    getPane().setPANDETAI(cust1);
                              }
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
//try {
//    String contractReference = getDriverWrapper().getEventFieldAsText("FOCR", "s", "");
////  String pay = getDriverWrapper().getEventFieldAsText("ACT", "s", "");
//    if(contractReference==null || contractReference.isEmpty() || contractReference.trim().equals("") ) {
//          validationDetails.addWarning(WarningType.Other, "Reference Contract Number/Deal ID is not entered in Settlements tab[CM]");
//    }
//}
//catch (Exception ee) {
//    ee.printStackTrace();
//    //Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());
//
//}
                        try {
                              String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventref = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String custId = getDriverWrapper().getEventFieldAsText("PRM", "p", "cu").trim();
                              String uniqueId = getDriverWrapper().getEventFieldAsText("cBUB", "s", "").trim();
                              String branch = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                              String inputXml = "<?xml version=\"1.0\" standalone=\"yes\"?><ServiceRequest xmlns:m='urn:messages.service.ti.apps.tiplus2.misys.com' xmlns='urn:control.services.tiplus2.misys.com' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                                          + "<RequestHeader>" + "<Service>Treasury</Service>" + "<Operation>Validate</Operation>"
                                          + "<Credentials>" + "<Name>Name</Name>" + "<Password>Password</Password>"
                                          + "<Certificate>Certificate</Certificate>" + "<Digest>Digest</Digest>" + "</Credentials>"
                                          + "<ReplyFormat>FULL</ReplyFormat>" + "<NoRepair>Y</NoRepair>"
                                          + "<NoOverride>Y</NoOverride>" + "<CorrelationId>CorrelationId</CorrelationId>"
                                          + "<TransactionControl>NONE</TransactionControl>" + "</RequestHeader>"
                                          + "<TreasuryValidateRequest>" + "<MASTERREF>" + masReference + "</MASTERREF>" + "<EVENTREF>"
                                          + eventref + "</EVENTREF>" + "<CUSTID>" + custId + "</CUSTID>" + "<UNIQUEID>" + uniqueId
                                          + "</UNIQUEID>" + "<INPUTBRANCH>" + branch + "</INPUTBRANCH>"
                                          + "<VALIDATIONTYPE>BOVERIFY</VALIDATIONTYPE>"
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
//                try {
//                    String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
//                    String branch = getDriverWrapper().getEventFieldAsText("BOB", "b", "c");
//                    con = ConnectionMaster.getConnection();
//                    String account="";
//                    String query="SELECT trim(ACC.BO_ACCTNO) As BO_ACCTNO ,trim(ACC.ext_ACCTNO),acc.category "+
//                             "FROM MASTER MAS, BASEEVENT BEV, RELITEM REL, NETTEDFM NET ,ACCOUNT ACC " +
//                             "WHERE MAS.KEY97   = BEV.MASTER_KEY AND BEV.KEY97 = REL.EVENT_KEY " +
//                             "AND REL.KEY97     = NET.KEY97   AND NET.STTLACCT1 =ACC.ACCT_KEY " +
//                             "AND MAS.MASTER_REF='"+masReference+"' "+
//                                     "and acc.category='28500030'";
//                    
//                    ps = con.prepareStatement(query);
//                      rs = ps.executeQuery();
//                      while (rs.next()) {
//                          account = rs.getString(1);
//                        
//                      }
//                      String acct=account.substring(0,5);
//                      if(!branch.equalsIgnoreCase(acct)) {
//                          System.out.println("inside warning "+acct+" "+branch);
//                          validationDetails.addWarning(WarningType.Other,account+" Account number  should belong to Behalf of Branch "+branch+". The transaction may fail");
//                      }
//                      
//                      }
//                
//                catch(Exception e){
//                      e.printStackTrace();
//                }
//                
//                finally {
//                    ConnectionMaster.surrenderDB(con, ps, rs);
//                }
                  // Merchant Trade 19/12/22
                  if (getMajorCode().equalsIgnoreCase("CPBI") && getMinorCode().equalsIgnoreCase("PBIC")) {
                        try {
                              // getErrorPurposeCode(validationDetails);
                              getCountryRisk();
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                        try {

                              getUidWarning(validationDetails);
                              System.out.println("inside uid warning ");
                        } catch (Exception e) {
                              System.out.println("Exception in getUidWarning-->" + e.getMessage());
                        }
                  }
                  if (getMajorCode().equalsIgnoreCase("CPBI")
                              || getMajorCode().equalsIgnoreCase("CPBI") && getMinorCode().equalsIgnoreCase("PBIC")
                              || getMinorCode().equalsIgnoreCase("PBIN")) {
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
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  }
            }
      }
}