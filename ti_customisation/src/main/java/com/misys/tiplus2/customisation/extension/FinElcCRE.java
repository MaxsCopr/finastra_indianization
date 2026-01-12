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
import com.misys.tiplus2.customisation.entity.ExtEventFXMULTIPLE;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class FinElcCRE extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(FinElcCRE.class);
      Connection con, con1 = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst, pes = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ress = null;

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
                  
                  // Arunkumar M 14-07-2021
                  if (getMajorCode().equalsIgnoreCase("FOC")) {
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
                  String step_id = getDriverWrapper().getEventFieldAsText("CSID", "s", "");

                  // getSubvention
                  if (!step_id.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        try {

                              getSubvention();
                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"getSubvention");
                        }
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        Loggers.general().info(LOG,"<=============Hyperlink===> "+TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTELCFINANCEclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTFINEXPLCAMDclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTFINEXPLCCREclayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTFINIWCOLADJclayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTFINIWCOLAMDclayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTFINIWCOLCREclayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTFININWCOLREPclayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTFINILCADJclayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTFINILCAMDclayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTFINILCCREclayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshA = getPane().getCtlTSTFINILCREPclayHyperlink();
                        dmshA.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshB = getPane().getCtlTSTFINOUTCOLAMDclayHyperlink();
                        dmshB.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshC = getPane().getCtlTSTFINOUTCOLCREclayHyperlink();
                        dmshC.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshF = getPane().getCtlTSTFINOUTCOLREPclayHyperlink();
                        dmshF.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshG = getPane().getCtlTSTFINOUTCOLADJclayHyperlink();
                        dmshG.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshH = getPane().getCtlTSTFINEXPLCREPclayHyperlink();
                        dmshH.setUrl(TSTHyperlink);
                        Loggers.general().info(LOG,"Hyperlink===> "+TSTHyperlink);
                        Loggers.general().info(LOG,"FINEXPREP===> "+dmshH.getUrl());

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  //For CR-143 Limit Nodes
                  try{
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesFINEXPLCCREclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesFINEXPLCADJclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane().getCtlUnavailablelimitnodesFINILCCREclayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode4 = getPane().getCtlUnavailablelimitnodesFINILCADJclayHyperlink();
                        limitnode4.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode5 = getPane().getCtlUnavailablelimitnodesFINOUTCOLCREclayHyperlink();
                        limitnode5.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode6 = getPane().getCtlUnavailablelimitnodesFINOUTCOLADJclayHyperlink();
                        limitnode6.setUrl(HyperLimitNode);
                  }catch(Exception e){
                        System.out.println("For Limit Node"+e.getMessage());
                  }
                  
                  
                  
                  // Preshipemnt
                  // //Loggers.general().info(LOG,"onPostInitialise called in FinElcCRE
                  // started");
                  try {

                        String Preshipment = getHyperPreshipment();
                        // //Loggers.general().info(LOG,"Preshipment URL - "+Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentFINEXPLCCREclayHyperlink();
                        dmsh.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlPreshipmentFINOUTCOLCREclayHyperlink();
                        dmsh1.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlPreshipmentFINOUTCOLAMDclayHyperlink();
                        dmsh2.setUrl(Preshipment);

                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlPreshipmentFINEXPLCADJclayHyperlink();// FELC
                        dmsh3.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlPreshipmentFINEXPLCREPclayHyperlink();// FELC
                                                                                                                                                                        // //
                                                                                                                                                                        // CREATE
                        dmsh4.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlPreshipmentFINEXPLCADJclayHyperlink();// FELC
                        // CREATE
                        dmsh5.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlPreshipmentFINEXPLCREPclayHyperlink();// FELC
                        // CREATE
                        dmsh6.setUrl(Preshipment);
                        // //Loggers.general().info(LOG,"URL Set done");
                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Catched exception in FinElcCRE - " +
                        // e1.getMessage());
                  }

                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  if (!step_id.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        try {

                              if (subproductCode.equalsIgnoreCase("PCR") || subproductCode.equalsIgnoreCase("INA")
                                          || subproductCode.equalsIgnoreCase("HCA")) {

                                    if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4")) {
                                          getPane().onSUBVENFINEXPLCCREclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("ASA4")) {
                                          getPane().onSUBVENFINEXPLCAMDclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("JSA4")) {
                                          getPane().onSUBVENFINEXPLCADJclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("RSA4")) {
                                          getPane().onSUBVENFINEXPLCREPclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("CSA1")) {
                                          getPane().onSUBVENFINOUTCOLCREclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("ASA1")) {
                                          getPane().onSUBVENFINOUTCOLAMDclayButton();
                                    }

                                    if (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("JSA1")) {
                                          getPane().onSUBVENFINOUTCOLADJclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("RSA1")) {
                                          getPane().onSUBVENFINOUTCOLREPclayButton();
                                    }
                              }

                        } catch (Exception e) {
                              Loggers.general().info(LOG,"Exception Getting subvention value in Postinitialization" + e.getMessage());
                        }

                  }

                  // getSubvention
                  if (!step_id.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        try {
                              getLOBISSUE();

                        } catch (Exception ee) {
                              Loggers.general().info(LOG,ee.getMessage());
                        }

                        if (getMajorCode().equalsIgnoreCase("FOC")) {
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
                              currencyCalc();
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                              }
                        }

                        if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4")) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"LOB code FEL create=======>" + getMinorCode());
                              }
                              String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                              try {
                                    con = getConnection();
                                    String query = "select e.LOB from master m,BASEEVENT b,extevent e where m.KEY97=b.MASTER_KEY and b.KEY97=e.EVENT and b.REFNO_PFIX in ('DPR','POD') and trim(e.LOB) is not null and m.MASTER_REF='"
                                                + mainMasterRef + "' order by b.REFNO_PFIX ";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"LOB code update for FEL create===>" + query);
                                    }
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String lobVal = rs1.getString(1);
                                          getPane().setLOB(lobVal);

                                    }
                              }

                              catch (Exception e) {

                                    Loggers.general().info(LOG,"Exception LOB code update for FEL create" + e.getMessage());

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

                        String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
                        String financeVal = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        String financeCreate = getDriverWrapper().getEventFieldAsText("BFI", "l", "");
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"LOB code financeCreate create=======>" + financeCreate);
                        }
                        if (productcode.equalsIgnoreCase("FOC") && financeVal.equalsIgnoreCase("FEC")
                                    && financeCreate.equalsIgnoreCase("Y")) {

                              String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                              try {
                                    con = getConnection();
                                    String query = "SELECT trim(e.LOB),m.MASTER_REF,b.REFNO_PFIX FROM master m, BASEEVENT b, extevent e WHERE m.KEY97 =b.MASTER_KEY AND b.KEY97 =e.EVENT AND b.REFNO_PFIX IN ('FEC') AND trim(e.LOB) IS NOT NULL AND m.MASTER_REF ='"
                                                + mainMasterRef + "' order by b.REFNO_PFIX ";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"LOB code update for financeCreate===>" + query);
                                    }
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String lobVal = rs1.getString(1);
                                          getPane().setLOB(lobVal);

                                    }
                              }

                              catch (Exception e) {

                                    Loggers.general().info(LOG,"Exception LOB code update for FEL create" + e.getMessage());

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

                  }

                  try {
                        String mas = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                        // String = "0172ELFX0003716";
                        String evt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                        String str = mas.substring(4, 16);
                        // //Loggers.general().info(LOG,"str---->" + str);
                        String strevt = evt.substring(0, 1);
                        // //Loggers.general().info(LOG,"strevt ---->" + strevt);

                        String str11 = evt.substring(3, 6);
                        // //Loggers.general().info(LOG,"str11 ---->" + str11);
                        String val = str + strevt + str11;
                        // //Loggers.general().info(LOG,"Total ---->" + val);
                        if (strevt.equalsIgnoreCase("D")) {
                              getWrapper().setBLLREFNO(val);
                              getPane().setBLLREFNO(val);
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exception" + e);
                  }

                  if (getMajorCode().equalsIgnoreCase("FIL")) {

                        String refNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();

                        String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                        try {
                              con = getConnection();
                              String query = "select e.BLLREFNO from master m,BASEEVENT b,extevent e where m.KEY97=b.MASTER_KEY and b.KEY97=e.EVENT and b.REFNO_PFIX='"
                                          + evnt + "' and b.REFNO_SERL =" + evvcount + " and m.MASTER_REF='" + refNumber + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Bill reference number query" + query);
                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String billNo = rs1.getString(1).trim();
                                    getPane().setBLLREFNO(billNo);
                                    // Loggers.general().info(LOG,"ownlc value=======>" +
                                    // ownlc);

                              }
                        }

                        catch (Exception e) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exeception in Bill reference number" + e.getMessage());
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


                  if (!step_id.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        try {
                              List<ExtEventFXMULTIPLE> fxmultiple = (List<ExtEventFXMULTIPLE>) getWrapper()
                                          .getExtEventFXMULTIPLE();
                              // //Loggers.general().info(LOG,"fxmultiple.size ---->");
                              for (int i = 0; i < fxmultiple.size(); i++) {

                                    ExtEventFXMULTIPLE fxmul = fxmultiple.get(i);

                                    BigDecimal bigamt = fxmul.getAMTCUY();
                                    // //Loggers.general().info(LOG,"Fxmultiple bigamt---->" +
                                    // bigamt);
                                    BigDecimal notion = fxmul.getNOTION();
                                    // //Loggers.general().info(LOG,"Fxmultiple notion---->" +
                                    // notion);
                                    BigDecimal inreq = notion.multiply(bigamt);
                                    // //Loggers.general().info(LOG,"Fxmultiple multiple value---->"
                                    // +
                                    // inreq);
                                    // BigDecimal equ = new BigDecimal(equi_bill);
                                    fxmul.setINREQUL(inreq);
                                    fxmul.setINREQULCurrency("INR");
                                    // //Loggers.general().info(LOG,"Fxmultiple multiple final
                                    // value---->"
                                    // + fxmul.getINREQULCurrency());
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in validating for notional
                              // rate
                              // - " + e.getMessage());
                        }

                        String mastamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                        String mastcur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                                                
                        try {
                              String graceday = getWrapper().getGRACEPER();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Grace period days" + graceday);
                              }
                              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                              Calendar c = Calendar.getInstance();
                              String dueDate = getDriverWrapper().getEventFieldAsText("B+PD", "d", "");

                              String days = "3";
                              int gra = 0;
                              if (graceday.equalsIgnoreCase("") || graceday.equalsIgnoreCase("0")) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"Grace period days in if" + graceday);
                                    }
                                    getPane().setGRACEPER(days);
                                    String grace = getWrapper().getGRACEPER();
                                    gra = Integer.parseInt(grace);
                                    // Loggers.general().info(LOG,"Additional expiry days in if
                                    // loop" +
                                    // grace);
                              } else if (!graceday.equalsIgnoreCase("") && !graceday.equalsIgnoreCase("0")) {
                                    String grace = getWrapper().getGRACEPER();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"Grace period days in else" + graceday);
                                    }
                                    gra = Integer.parseInt(grace);
                              }
                              c.setTime(sdf.parse(dueDate));
                              c.add(Calendar.DATE, gra);
                              // //Loggers.general().info(LOG,"DATE 1"+ c);
                              String output = sdf.format(c.getTime());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Grace period date" + output);
                              }
                              getPane().setGRCPERDT(output);
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception Greace period " + e.getMessage());
                              }
                        }

                        // Drawee name population
                        if (getMajorCode().equalsIgnoreCase("FOC")) {

                              String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();

                              try {
                                    con = getConnection();
                                    String query = "select trim(part.ADDRESS1) from master mas,COLLMASTER col,PARTYDTLS part,BASEEVENT bas where mas.KEY97=col.KEY97 and mas.KEY97=bas.MASTER_KEY and col.DRAWEE_PTY = part.KEY97 and mas.MASTER_REF='"
                                                + subrefNumber + "' and bas.REFNO_PFIX='CRE'";
                                    // Loggers.general().info(LOG,"Drawee name population query " +
                                    // query);
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment due
                                          // date
                                          // query");
                                          String draweeName = rs1.getString(1);
                                          getPane().setDRAWNAM(draweeName);
                                    }

                                    con = getConnection();
                                    String query1 = "SELECT ext.INVOICNO FROM master mas, COLLMASTER col, PARTYDTLS part, BASEEVENT bas, extevent ext WHERE mas.KEY97 =col.KEY97 AND mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND col.DRAWEE_PTY = part.KEY97 AND mas.MASTER_REF ='"
                                                + subrefNumber + "' AND bas.REFNO_PFIX ='CRE'";
                                    // Loggers.general().info(LOG,"invoice no population query " +
                                    // query1);
                                    ps1 = con.prepareStatement(query1);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment due
                                          // date
                                          // query");
                                          String invoice = rs1.getString(1);
                                          getPane().setINVOICNO(invoice);
                                    }

                              }

                              catch (Exception e) {
                                    Loggers.general().info(LOG,"INVOICE NO population exception" + e.getMessage());
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

                        // Ownlc population
                        if (getMajorCode().equalsIgnoreCase("FEL")
                                    && (getMinorCode().equalsIgnoreCase("CSA4") || getMinorCode().equalsIgnoreCase("RSA4"))) {

                              String refNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();

                              String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                              String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                              try {
                                    con = getConnection();
                                    String query = "select ext.OWNLC from master mas,baseevent bas,extevent ext where mas.KEY97=bas.MASTER_KEY and bas.KEY97=ext.EVENT and mas.MASTER_REF ='"
                                                + refNumber + "' and bas.REFNO_PFIX='" + evnt + "' and bas.REFNO_SERL='" + evvcount
                                                + "'";
                                    // Loggers.general().info(LOG,"OWNLC checking query " + query);
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String ownlc = rs1.getString(1);
                                          // Loggers.general().info(LOG,"ownlc value=======>" +
                                          // ownlc);
                                          if (ownlc.equalsIgnoreCase("Y")) {
                                                getPane().setOWNLC(true);
                                                getWrapper().setOWNLC(true);
                                          } else {
                                                getPane().setOWNLC(false);
                                                getWrapper().setOWNLC(false);
                                          }
                                    }
                              }

                              catch (Exception e) {
                                    // Loggers.general().info(LOG,"Payment due date exception" +
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
                              // Loggers.general().info(LOG,"Ownlc major code" + getMajorCode());
                        }

                        // //Loggers.general().info(LOG,"onPostInitialise called in FinElcCRE
                        // end");

                        if (getMajorCode().equalsIgnoreCase("FOC")) {

                              String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                              String mainrefNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                              String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                              String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");
                              String masterCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");

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

                                    // -------------------------------------------------//
                                    try {
                                          con = getConnection();
                                          String query = "select trim(part.ADDRESS1) from master mas,COLLMASTER col,PARTYDTLS part,BASEEVENT bas where mas.KEY97=col.KEY97 and mas.KEY97=bas.MASTER_KEY and col.DRAWEE_PTY = part.KEY97 and mas.MASTER_REF='"
                                                      + subrefNumber + "' and bas.REFNO_PFIX='CRE'";
                                          // Loggers.general().info(LOG,"Drawee name population query
                                          // " +
                                          // query);
                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //Loggers.general().info(LOG,"Entered while Payment
                                                // due
                                                // date
                                                // query");
                                                String draweeName = rs1.getString(1);
                                                getPane().setDRAWNAM(draweeName);
                                          }

                                          con = getConnection();
                                          String query1 = "SELECT ext.INVOICNO FROM master mas, COLLMASTER col, PARTYDTLS part, BASEEVENT bas, extevent ext WHERE mas.KEY97 =col.KEY97 AND mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND col.DRAWEE_PTY = part.KEY97 AND mas.MASTER_REF ='"
                                                      + subrefNumber + "' AND bas.REFNO_PFIX ='CRE'";
                                          // Loggers.general().info(LOG,"invoice no population query "
                                          // +
                                          // query1);
                                          ps1 = con.prepareStatement(query1);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //Loggers.general().info(LOG,"Entered while Payment
                                                // due
                                                // date
                                                // query");
                                                String invoice = rs1.getString(1);
                                                getPane().setINVOICNO(invoice);
                                          }

                                    } catch (Exception ex) {
                                          Loggers.general().info(LOG,"Drawee name population exception" + ex.getMessage());
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

                        if (getMajorCode().equalsIgnoreCase("FEL")) {
                              String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                              String mainrefNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                              String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                              String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                              String balanceAmt = "";
                              String balanceccy = mastcur;
                              BigDecimal balanceBig = new BigDecimal(0);
                              try {

                                    String margin = getWrapper().getPMARGIN();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Margin percentage " + margin);

                                    }

                                    if (margin == null || margin.equalsIgnoreCase("")) {
                                          String finalVal = "0";
                                          try {
                                                con = getConnection();
                                                String queryResult = "SELECT mas.MASTER_REF, lcp.OR_REF_PFX||lpad(lcp.OR_REF_SER,3,0) bill_ref_no, bev.REFNO_PFIX||lpad(bev.REFNO_SERL,3,0) pod_ref_no, mas1.MASTER_REF, bev1.REFNO_PFIX ||lpad(bev1.REFNO_SERL,3,0) fin_ref_no,exte.BALAMT, exte.CCY_16 FROM master mas, BASEEVENT bev, LCPAYMENT lcp, BASEEVENT bev1, master mas1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND mas.MASTER_REF = '"
                                                            + mainrefNumber + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL ="
                                                            + evvcount
                                                            + " AND bev.KEY97 = lcp.KEY97 AND bev1.REFNO_PFIX ='CRE' AND mas.STATUS ='LIV' AND bev1.STATUS in ('i','c') AND exte.MARAMT is not null AND exte.BALAMT is not null AND lcp.OR_PAY_EV = bev1.ATTACHD_EV and mas1.KEY97 = bev1.MASTER_KEY AND bev1.KEY97 = exte.EVENT";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"queryResult for net finance amount FEL" + queryResult);

                                                }
                                                ps1 = con.prepareStatement(queryResult);
                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      balanceBig = rs1.getBigDecimal(6);
                                                      balanceccy = rs1.getString(7);

                                                      ConnectionMaster connectionMaster = new ConnectionMaster();
                                                      double divideByDecimal = connectionMaster.getDecimalforCurrency(balanceccy);
                                                      BigDecimal divideByBig = new BigDecimal(divideByDecimal);

                                                      BigDecimal ValueBig = balanceBig.divide(divideByBig);
                                                      DecimalFormat diff = new DecimalFormat("0.00");
                                                      diff.setMaximumFractionDigits(2);
                                                      finalVal = diff.format(ValueBig);

                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"String value of net finance amount " + finalVal);

                                                      }

                                                }

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Final value of net finance amount " + finalVal);

                                                }
                                                if ((balanceBig.compareTo(BigDecimal.ZERO) > 0)) {
                                                      getPane().setBALAMT(finalVal + "" + balanceccy);
                                                } else {
                                                      finalVal = "0";
                                                      getPane().setBALAMT(finalVal + balanceccy);
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

                        // -------------------------------------------------//
                        if (getMajorCode().equalsIgnoreCase("FEL")) {
                              try {
                                    String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                                    String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");

                                    String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                                    con = getConnection();
                                    String query = "SELECT TRIM(part.ADDRESS1) FROM master mas, BASEEVENT bas, PARTYDTLS part,TIDATAITEM tid WHERE mas.KEY97 =bas.MASTER_KEY AND tid.KEY97 = part.KEY97 and bas.key97=tid.EVENT_KEY AND mas.MASTER_REF ='"
                                                + subrefNumber + "' AND PART.ROLE='APP' AND bas.REFNO_PFIX ='ADV'";
                                    // Loggers.general().info(LOG,"Drawee name population query
                                    // " +
                                    // query);
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment
                                          // due
                                          // date
                                          // query");
                                          String draweeName = rs1.getString(1);
                                          getPane().setDRAWNAM(draweeName);
                                    }

                                    con = getConnection();
                                    String query1 = "SELECT trim(EXT.INVOICNO) FROM master mas, BASEEVENT bas, EXTEVENT EXT WHERE mas.KEY97 =bas.MASTER_KEY and bas.key97=EXT.EVENT AND mas.MASTER_REF ='"
                                                + subrefNumber + "' AND bas.REFNO_PFIX ='DPR' AND bas.REFNO_SERL=" + evvcount + "";
                                    // Loggers.general().info(LOG,"invoice no population query "
                                    // +
                                    // query1);
                                    ps1 = con.prepareStatement(query1);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment
                                          // due
                                          // date
                                          // query");
                                          String invoice = rs1.getString(1);
                                          getPane().setINVOICNO(invoice);
                                    }

                              } catch (Exception ex) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Drawee name population exception" + ex.getMessage());
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

                        // Value date population
                        if (getMajorCode().equalsIgnoreCase("FEL")) {
                              String masterNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                              String refNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                              String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                              String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");
                              try {

                                    con = getConnection();
                                    String sql1 = "SELECT doc.PAYSTSCODE, exte.REPCRY, mas.MASTER_REF, mas1.MASTER_REF, bev1.REFNO_PFIX, bev1.REFNO_SERL FROM master mas, BASEEVENT bev, BASEEVENT bev1, master mas1, extevent exte, DOCSPRE doc WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.MASTER_KEY = mas1.KEY97 AND bev1.KEY97 = exte.EVENT AND bev.KEY97 = doc.KEY97 AND mas.MASTER_REF = '"
                                                + masterNumber + "' AND bev.REFNO_PFIX ='" + evnt + "' AND bev.REFNO_SERL =" + evvcount
                                                + "";
                                    // Loggers.general().info(LOG,"Query value for Repayment and
                                    // paymentOption--->" + sql1);
                                    con = getConnection();
                                    ps = con.prepareStatement(sql1);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String paymentOption = rs.getString(1).trim();
                                          String repayCheck = rs.getString(2).trim();
                                          // Loggers.general().info(LOG,"Repayment value===>" +
                                          // repayCheck);
                                          if (paymentOption.equalsIgnoreCase("CRY") && !repayCheck.equalsIgnoreCase("Y")) {

                                                getPane().setREPCRY(true);
                                          } else {
                                                // Loggers.general().info(LOG,"Repayment value else===>"
                                                // + repayCheck + "paymentOption====>" +
                                                // paymentOption);
                                          }

                                    }

                                    String query = "SELECT TO_CHAR(part.VALUE_DAT,'DD/MM/YY') AS VALUE_DATE, mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL FROM master mas, BASEEVENT bev, LCPAYMENT lcp, PARTPAYMNT part WHERE mas.KEY97 = bev.MASTER_KEY AND mas.STATUS = 'LIV' AND bev.KEY97 = lcp.KEY97 AND part.PAYEV_KEY = lcp.KEY97 AND bev.REFNO_SERL ="
                                                + evvcount + " AND mas.MASTER_REF = '" + refNumber + "' AND bev.REFNO_PFIX ='DPR'";
                                    // Loggers.general().info(LOG,"Value date query " + query);
                                    con = getConnection();
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment due
                                          // date
                                          // query");
                                          String valueDate = rs1.getString(1);
                                          getPane().setVALDAT(valueDate);
                                    }
                              }

                              catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Value date exception" + e.getMessage());
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

                        // payment due date population
                        if (getMajorCode().equalsIgnoreCase("FEL")) {

                              String refNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                              String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                              String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");
                              try {
                                    con = getConnection();
                                    String query = "SELECT TO_CHAR(lcp.PAYDUEDATE,'DD/MM/YY'),mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL FROM master mas, BASEEVENT bev, LCPAYMENT lcp WHERE mas.KEY97 = bev.MASTER_KEY AND mas.STATUS = 'LIV' AND bev.KEY97 = lcp.KEY97 AND bev.REFNO_SERL ="
                                                + evvcount + " AND mas.MASTER_REF = '" + refNumber + "' AND bev.REFNO_PFIX = 'POD'";
                                    // Loggers.general().info(LOG,"Payment due date query " +
                                    // query);
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment due
                                          // date
                                          // query");
                                          String paydt = rs1.getString(1);
                                          getPane().setPAYMNT(paydt);
                                    }
                              }

                              catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Payment due date exception" + e.getMessage());
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
                              // Loggers.general().info(LOG,"Payment due date major code" +
                              // getMajorCode());
                              getPane().setPAYMNT("");
                        }

                        // Notional due date population

                        if (getMajorCode().equalsIgnoreCase("FEL") || getMajorCode().equalsIgnoreCase("FOC")
                                    || getMajorCode().equalsIgnoreCase("FIC") || getMajorCode().equalsIgnoreCase("FIL")) {

                              getfinaceNotionalDate();
                        } else {

                              getPane().setSIGVALDT("");
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

                        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                        String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                        // Update value
                        String rtgspart = getWrapper().getRTGSPART();

                        try {

                              int penal=getpenalRate();
                        } catch (Exception ee) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"get Penal Rate" + ee.getMessage());
                              }

                        }

                        if (getMajorCode().equalsIgnoreCase("FOC") || getMajorCode().equalsIgnoreCase("FEL")) {
                              // PostingCustom post = null;
                              // if(step_csm.equalsIgnoreCase("CSM Maker 1"))
                              // String strPSID =
                              // getDriverWrapper().getPostingFieldAsText("PSID",
                              // "").trim();
                              try {

                                    String Mast = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                                    String Evnt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                                    // String strPSID = post.valueString();

                                    Loggers.general().info(LOG,"MasterReferenceNNum----------->" + Mast);
                                    Loggers.general().info(LOG,"their Reference----------->" + Evnt);
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
                                                + " AND POSR.FX_RATECOD = F86.CODE53" + " AND MAS.CCY  = F86.CURREN49"
                                                + " AND MASTER_REF='" + Mast + "'" + " AND BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) ='" + Evnt + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Query Result for FX_Rate----------> " + query1);

                                    }

                                    Loggers.general().info(LOG,"Query Result for FX_Rate ------->" + query1);

                                    ps1 = con.prepareStatement(query1);

                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Entering While Loop");

                                          }
                                          Loggers.general().info(LOG,"Entering While Loop");

                                          String fx = rs1.getString(1);

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
                        if (getMajorCode().equalsIgnoreCase("FOC")) {
                              
                              
                              getPane().getExtEventLoanDetailsNew().setEnabled(false);
                              getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                              getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                              getPane().getExtEventLoanDetailsUp().setEnabled(false);
                              getPane().getExtEventLoanDetailsDown().setEnabled(false);
                        }
                        if (getMajorCode().equalsIgnoreCase("FEL")) {
                              
                              
                              getPane().getExtEventLoanDetailsNew().setEnabled(false);
                              getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                              getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                              getPane().getExtEventLoanDetailsUp().setEnabled(false);
                              getPane().getExtEventLoanDetailsDown().setEnabled(false);
                        }

                        if((getMajorCode().equalsIgnoreCase("FOC")|| getMajorCode().equalsIgnoreCase("FEL")) && (getMinorCode().equalsIgnoreCase("CSA1") || getMinorCode().equalsIgnoreCase("CSA4"))){
                              String amountStr = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                        //    String amountccy = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");
                        //    String postrate = getDriverWrapper().getEventFieldAsText("cASE", "s", "");
                              String stepid = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                        
                              
                              if (stepid.equalsIgnoreCase("AUTHORISE1")) {
                                    getPane().getCtlTREQAM1().setEnabled(false);
                                    getPane().getCtlTREQAM2().setEnabled(false);
                                    getPane().getCtlTREQAM3().setEnabled(false);
                                    getPane().getCtlTREQAM4().setEnabled(false);
                                    getPane().getCtlTREQAM5().setEnabled(false);
                                    getPane().getCtlFDEQAM1().setEnabled(false);
                                    getPane().getCtlFDEQAM2().setEnabled(false);
                                    getPane().getCtlFDEQAM3().setEnabled(false);
                            getPane().getCtlFDEQAM4().setEnabled(false);
                                    getPane().getCtlFDEQAM5().setEnabled(false);
                                    getPane().getCtlTRFXRT1().setEnabled(false);
                                    getPane().getCtlTRFXRT2().setEnabled(false);
                                    getPane().getCtlTRFXRT3().setEnabled(false);
                                    getPane().getCtlTRFXRT4().setEnabled(false);
                                    getPane().getCtlTRFXRT5().setEnabled(false);
                                    getPane().getCtlFDFXRT1().setEnabled(false);
                                    getPane().getCtlFDFXRT2().setEnabled(false);
                                    getPane().getCtlFDFXRT3().setEnabled(false);
                                    getPane().getCtlFDFXRT4().setEnabled(false);
                                    getPane().getCtlFDFXRT5().setEnabled(false);
                                    getPane().getCtlFWDCTREF1().setEnabled(false);
                                    getPane().getCtlFWDCTREF2().setEnabled(false);
                                    getPane().getCtlFWDCTREF3().setEnabled(false);
                                    getPane().getCtlFWDCTREF4().setEnabled(false);
                                    getPane().getCtlFWDCTREF5().setEnabled(false);
                                    getPane().getCtlTRESREF1().setEnabled(false);
                                    getPane().getCtlTRESREF2().setEnabled(false);
                                    getPane().getCtlTRESREF3().setEnabled(false);
                                    getPane().getCtlTRESREF4().setEnabled(false);
                                    getPane().getCtlTRESREF5().setEnabled(false);
                                    getPane().getCtlTRERFAM1().setEnabled(false);
                                    getPane().getCtlTRERFAM2().setEnabled(false);
                                    getPane().getCtlTRERFAM3().setEnabled(false);
                                    getPane().getCtlTRERFAM4().setEnabled(false);
                                    getPane().getCtlTRERFAM5().setEnabled(false);
                                    getPane().getCtlFWDRFAM1().setEnabled(false);
                                    getPane().getCtlFWDRFAM2().setEnabled(false);
                                    getPane().getCtlFWDRFAM3().setEnabled(false);
                                    getPane().getCtlFWDRFAM4().setEnabled(false);
                                    getPane().getCtlFWDRFAM5().setEnabled(false);
                              }
                        }

                  }

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
                  String eventStep= getDriverWrapper().getEventFieldAsText("CSTD", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  
                  
                  //For CR-143 Limit Nodes
                  try{
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesFINEXPLCCREclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesFINEXPLCADJclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane().getCtlUnavailablelimitnodesFINILCCREclayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode4 = getPane().getCtlUnavailablelimitnodesFINILCADJclayHyperlink();
                        limitnode4.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode5 = getPane().getCtlUnavailablelimitnodesFINOUTCOLCREclayHyperlink();
                        limitnode5.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode6 = getPane().getCtlUnavailablelimitnodesFINOUTCOLADJclayHyperlink();
                        limitnode6.setUrl(HyperLimitNode);
                  }catch(Exception e){
                        System.out.println("For Limit Node"+e.getMessage());
                  }

                  // FCY Tax calculation
                  try {

                        getFCCTCALCULATION();

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }

                  // getSubvention
                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        try {

                              getSubvention();
                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"getSubvention");
                        }
                  }
                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        try {

                              if (subproductCode.equalsIgnoreCase("PCR") || subproductCode.equalsIgnoreCase("INA")
                                          || subproductCode.equalsIgnoreCase("HCA")) {

                                    if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4")) {
                                          getPane().onSUBVENFINEXPLCCREclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("ASA4")) {
                                          getPane().onSUBVENFINEXPLCAMDclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("JSA4")) {
                                          getPane().onSUBVENFINEXPLCADJclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("RSA4")) {
                                          getPane().onSUBVENFINEXPLCREPclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("CSA1")) {
                                          getPane().onSUBVENFINOUTCOLCREclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("ASA1")) {
                                          getPane().onSUBVENFINOUTCOLAMDclayButton();
                                    }

                                    if (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("JSA1")) {
                                          getPane().onSUBVENFINOUTCOLADJclayButton();
                                    }
                                    if (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("RSA1")) {
                                          getPane().onSUBVENFINOUTCOLREPclayButton();
                                    }
                              }

                              getPane().ondisplayvalFINEXPLCCREclayButton();
                              getPane().ondisplayvalFINOUTCOLCREclayButton();

                        } catch (Exception e2) {
                              e2.printStackTrace();
                        }

                        if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4")) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"LOB code FEL create=======>" + getMinorCode());
                              }
                              String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                              try {
                                    con = getConnection();
                                    String query = "select e.LOB from master m,BASEEVENT b,extevent e where m.KEY97=b.MASTER_KEY and b.KEY97=e.EVENT and b.REFNO_PFIX in ('DPR','POD') and trim(e.LOB) is not null and m.MASTER_REF='"
                                                + mainMasterRef + "' order by b.REFNO_PFIX ";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"LOB code update for FEL create===>" + query);
                                    }
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          String lobVal = rs1.getString(1);
                                          getPane().setLOB(lobVal);

                                    }
                              }

                              catch (Exception e) {

                                    Loggers.general().info(LOG,"Exception LOB code update for FEL create" + e.getMessage());

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

                  // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String evnt1 = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount1 = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                        
                  // FCY Tax calculation
                  try {
                        String mas = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                        // String = "0172ELFX0003716";
                        String evt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                        String str = mas.substring(4, 16);
                        // //Loggers.general().info(LOG,"str---->" + str);
                        String strevt = evt.substring(0, 1);
                        // //Loggers.general().info(LOG,"strevt ---->" + strevt);

                        String str11 = evt.substring(3, 6);
                        // //Loggers.general().info(LOG,"str11 ---->" + str11);
                        String val = str + strevt + str11;
                        // //Loggers.general().info(LOG,"Total ---->" + val);
                        if (strevt.equalsIgnoreCase("D")) {
                              getWrapper().setBLLREFNO(val);
                              getPane().setBLLREFNO(val);
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exception" + e);
                  }

                  if (getMajorCode().equalsIgnoreCase("FIL")) {

                        String refNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();

                        String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                        try {
                              con = getConnection();
                              String query = "select e.BLLREFNO from master m,BASEEVENT b,extevent e where m.KEY97=b.MASTER_KEY and b.KEY97=e.EVENT and b.REFNO_PFIX='"
                                          + evnt + "' and b.REFNO_SERL =" + evvcount + " and m.MASTER_REF='" + refNumber + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Bill reference number query" + query);
                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String billNo = rs1.getString(1);
                                    getPane().setBLLREFNO(billNo);

                              }
                        }

                        catch (Exception e) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exeception in Bill reference number" + e.getMessage());
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

                  if (getMajorCode().equalsIgnoreCase("FEL")) {

                        String refNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                        String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");
                        try {
                              con = getConnection();
                              String query = "SELECT TO_CHAR(part.VALUE_DAT,'DD/MM/YY') AS VALUE_DATE, mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL FROM master mas, BASEEVENT bev, LCPAYMENT lcp, PARTPAYMNT part WHERE mas.KEY97 = bev.MASTER_KEY AND mas.STATUS = 'LIV' AND bev.KEY97 = lcp.KEY97 AND part.PAYEV_KEY = lcp.KEY97 AND bev.REFNO_SERL ="
                                          + evvcount + " AND mas.MASTER_REF = '" + refNumber + "' AND bev.REFNO_PFIX ='DPR'";
                              // Loggers.general().info(LOG,"Value date query " + query);
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while Payment due date
                                    // query");
                                    String valueDate = rs1.getString(1);
                                    getPane().setVALDAT(valueDate);
                              }

                              String masterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");

                              String accpt = getDriverWrapper().getEventFieldAsText("PUO3", "s", "");
                              String sql2 = "SELECT exte.LIMBLK,exte.ACCPDT, mas.MASTER_REF,bev.REFNO_PFIX,bev.REFNO_SERL FROM master mas, BASEEVENT bev, extevent exte, DOCSPRE doc WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND bev.KEY97 = doc.KEY97 AND mas.MASTER_REF = '"
                                          + masterRef + "' AND bev.REFNO_PFIX ='" + evnt + "' AND bev.REFNO_SERL =" + evvcount + "";
                              // Loggers.general().info(LOG,"Query value for Limit block--->" +
                              // sql2);
                              con = getConnection();
                              ps1 = con.prepareStatement(sql2);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String limiCheck = rs1.getString(1).trim();
                                    String accptCheck = rs1.getString(2);
                                    // Loggers.general().info(LOG,"Limit check in elc===>" +
                                    // limiCheck + "Accptance date====>" + accptCheck);

                                    if (limiCheck.equalsIgnoreCase("1") && (accptCheck == null || accptCheck.equalsIgnoreCase(""))
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                            || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          if (accpt.equalsIgnoreCase("ACP")) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Please select acceptance field as Acceptance not received [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"Acceptance field 1===>" +
                                                // accpt);
                                          }
                                    } else {

                                          // Loggers.general().info(LOG,"Limit check in elc else
                                          // 1===>" + limiCheck + "Accptance date====>" +
                                          // accptCheck);
                                    }

                                    if (limiCheck.equalsIgnoreCase("3") && (accptCheck != null || !accptCheck.equalsIgnoreCase(""))
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                            || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          if (!accpt.equalsIgnoreCase("ACP")) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Please select acceptance field as Acceptance received [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"Acceptance field 2===>" +
                                                // accpt);
                                          }
                                    } else {

                                          // Loggers.general().info(LOG,"Limit check in elc else
                                          // 2===>" + limiCheck + "Accptance date====>" +
                                          // accptCheck);
                                    }

                              }

                        }

                        catch (Exception e) {
                              Loggers.general().info(LOG,"Acceptance field exception" + e.getMessage());
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
                        // Loggers.general().info(LOG,"Value date population in else");
                  }
                  
                  

                  if (getMajorCode().equalsIgnoreCase("FOC")) {

                        String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                        String mainrefNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                        String mastcur = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");
                        String billAmt = "";
                        String finAmt = "";
                        String marginAmt = "";
                        String balanceccy = "";

                        BigDecimal billAmount = new BigDecimal(0);
                        BigDecimal finAmount = new BigDecimal(0);
                        BigDecimal marginAmount = new BigDecimal(0);
                        try {

                              try {
                                    String margin = getWrapper().getPMARGIN();
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
                                                && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                && getMinorCode().equalsIgnoreCase("CSA1")) {
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

                              // -------------------------------------------------//
                              try {
                                    con = getConnection();
                                    String query = "select trim(part.ADDRESS1) from master mas,COLLMASTER col,PARTYDTLS part,BASEEVENT bas where mas.KEY97=col.KEY97 and mas.KEY97=bas.MASTER_KEY and col.DRAWEE_PTY = part.KEY97 and mas.MASTER_REF='"
                                                + subrefNumber + "' and bas.REFNO_PFIX='CRE'";
                                    // Loggers.general().info(LOG,"Drawee name population query " +
                                    // query);
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment due
                                          // date
                                          // query");
                                          String draweeName = rs1.getString(1);
                                          getPane().setDRAWNAM(draweeName);
                                    }

                                    con = getConnection();
                                    String query1 = "SELECT ext.INVOICNO FROM master mas, COLLMASTER col, PARTYDTLS part, BASEEVENT bas, extevent ext WHERE mas.KEY97 =col.KEY97 AND mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND col.DRAWEE_PTY = part.KEY97 AND mas.MASTER_REF ='"
                                                + subrefNumber + "' AND bas.REFNO_PFIX ='CRE'";
                                    // Loggers.general().info(LOG,"invoice no population query " +
                                    // query1);
                                    ps1 = con.prepareStatement(query1);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          // //Loggers.general().info(LOG,"Entered while Payment due
                                          // date
                                          // query");
                                          String invoice = rs1.getString(1);
                                          getPane().setINVOICNO(invoice);
                                    }

                              } catch (Exception ex) {
                                    Loggers.general().info(LOG,"Drawee name population exception" + ex.getMessage());
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

                  if (getMajorCode().equalsIgnoreCase("FEL")) {

                        String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                        String mainrefNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                        String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                        String mastcur = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");
                        String balanceAmt = "";
                        String balanceccy = mastcur;
                        BigDecimal balanceBig = new BigDecimal(0);
                        try {

                              try {
                                    String margin = getWrapper().getPMARGIN();
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
                                                && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                                && getMinorCode().equalsIgnoreCase("CSA4")) {
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
                                          String queryResult = "SELECT mas.MASTER_REF, lcp.OR_REF_PFX||lpad(lcp.OR_REF_SER,3,0) bill_ref_no, bev.REFNO_PFIX||lpad(bev.REFNO_SERL,3,0) pod_ref_no, mas1.MASTER_REF, bev1.REFNO_PFIX ||lpad(bev1.REFNO_SERL,3,0) fin_ref_no,exte.BALAMT, exte.CCY_16 FROM master mas, BASEEVENT bev, LCPAYMENT lcp, BASEEVENT bev1, master mas1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND mas.MASTER_REF = '"
                                                      + mainrefNumber + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL ="
                                                      + evvcount
                                                      + " AND bev.KEY97 = lcp.KEY97 AND bev1.REFNO_PFIX ='CRE' AND mas.STATUS ='LIV' AND bev1.STATUS in ('i','c') AND exte.MARAMT is not null AND exte.BALAMT is not null AND lcp.OR_PAY_EV = bev1.ATTACHD_EV and mas1.KEY97 = bev1.MASTER_KEY AND bev1.KEY97 = exte.EVENT";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"queryResult for net finance amount FEL" + queryResult);

                                          }
                                          ps1 = con.prepareStatement(queryResult);
                                          rs1 = ps1.executeQuery();
                                          if (rs1.next()) {
                                                balanceBig = rs1.getBigDecimal(6);
                                                balanceccy = rs1.getString(7);

                                                ConnectionMaster connectionMaster = new ConnectionMaster();
                                                double divideByDecimal = connectionMaster.getDecimalforCurrency(balanceccy);
                                                BigDecimal divideByBig = new BigDecimal(divideByDecimal);

                                                BigDecimal ValueBig = balanceBig.divide(divideByBig);
                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);
                                                finalVal = diff.format(ValueBig);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"String value of net finance amount " + finalVal);

                                                }

                                          }

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Final value of net finance amount " + finalVal);

                                          }
                                          if ((balanceBig.compareTo(BigDecimal.ZERO) > 0)) {
                                                getPane().setBALAMT(finalVal + "" + balanceccy);
                                          } else {
                                                finalVal = "0";
                                                getPane().setBALAMT(finalVal + balanceccy);
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
                  if (getMajorCode().equalsIgnoreCase("FEL") || getMajorCode().equalsIgnoreCase("FOC")) {
                        try {

                              String customera = "";
                              if (getMajorCode().equalsIgnoreCase("FEL")) {
                                    customera = getDriverWrapper().getEventFieldAsText("B+FT", "p", "no").trim();
                              } else if (getMajorCode().equalsIgnoreCase("FOC")) {
                                    customera = getDriverWrapper().getEventFieldAsText("B+FT", "p", "no").trim();
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
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Exception Custom tenor value===>" + ex.getMessage());
                                    }
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
                  // -------------------------------------------------//
                  if (getMajorCode().equalsIgnoreCase("FEL")) {
                        try {
                              String subrefNumber = getDriverWrapper().getEventFieldAsText("OMRF", "r", "").trim();
                              String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");

                              String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                              con = getConnection();
                              String query = "SELECT TRIM(part.ADDRESS1) FROM master mas, BASEEVENT bas, PARTYDTLS part,TIDATAITEM tid WHERE mas.KEY97 =bas.MASTER_KEY AND tid.KEY97 = part.KEY97 and bas.key97=tid.EVENT_KEY AND mas.MASTER_REF ='"
                                          + subrefNumber + "' AND PART.ROLE='APP' AND bas.REFNO_PFIX ='ADV'";
                              // Loggers.general().info(LOG,"Drawee name population query
                              // " +
                              // query);
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while Payment
                                    // due
                                    // date
                                    // query");
                                    String draweeName = rs1.getString(1);
                                    getPane().setDRAWNAM(draweeName);
                              }

                              con = getConnection();
                              String query1 = "SELECT trim(EXT.INVOICNO) FROM master mas, BASEEVENT bas, EXTEVENT EXT WHERE mas.KEY97 =bas.MASTER_KEY and bas.key97=EXT.EVENT AND mas.MASTER_REF ='"
                                          + subrefNumber + "' AND bas.REFNO_PFIX ='DPR' AND bas.REFNO_SERL=" + evvcount + "";
                              // Loggers.general().info(LOG,"invoice no population query "
                              // +
                              // query1);
                              ps1 = con.prepareStatement(query1);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while Payment
                                    // due
                                    // date
                                    // query");
                                    String invoice = rs1.getString(1);
                                    getPane().setINVOICNO(invoice);
                              }

                        } catch (Exception ex) {
                              Loggers.general().info(LOG,"Drawee name population exception" + ex.getMessage());
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

                  try {
                        String graceday = getWrapper().getGRACEPER().trim();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"Grace period days" + graceday);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                        Calendar c = Calendar.getInstance();
                        String dueDate = getDriverWrapper().getEventFieldAsText("B+PD", "d", "");

                        String days = "3";
                        int gra = 0;
                        if (graceday.equalsIgnoreCase("") || graceday.equalsIgnoreCase("0")) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Grace period days in if" + graceday);
                              }
                              getPane().setGRACEPER(days);
                              String grace = getWrapper().getGRACEPER();
                              gra = Integer.parseInt(grace);
                              // Loggers.general().info(LOG,"Additional expiry days in if
                              // loop" +
                              // grace);
                        } else if (!graceday.equalsIgnoreCase("") && !graceday.equalsIgnoreCase("0")) {
                              String grace = getWrapper().getGRACEPER();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Grace period days in else" + graceday);
                              }
                              gra = Integer.parseInt(grace);
                        }
                        c.setTime(sdf.parse(dueDate));
                        c.add(Calendar.DATE, gra);
                        // //Loggers.general().info(LOG,"DATE 1"+ c);
                        String output = sdf.format(c.getTime());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"Grace period date" + output);
                        }
                        getPane().setGRCPERDT(output);
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Greace period " + e.getMessage());
                        }
                  }

                  // payment due date population
                  if (getMajorCode().equalsIgnoreCase("FEL")) {
                        // //Loggers.general().info(LOG,"Payment due date major code in if loop"
                        // +
                        // getMajorCode());
                        String refNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                        // String crossNumber =
                        // getDriverWrapper().getEventFieldAsText("EVXR", "r",
                        // "").trim();

                        // //Loggers.general().info(LOG,"refNumber ----->" + refNumber);
                        String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        // //Loggers.general().info(LOG,"event refNumber ----->" + evnt);
                        String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");
                        // //Loggers.general().info(LOG,"Event count for posting----->" +
                        // evvcount);
                        String subProd = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                        String categoryCode = getDriverWrapper().getEventFieldAsText("PUO1", "s", "").trim();

                        try {
                              con = getConnection();
                              String query = "SELECT TO_CHAR(lcp.PAYDUEDATE,'DD/MM/YY'),mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL FROM master mas, BASEEVENT bev, LCPAYMENT lcp WHERE mas.KEY97 = bev.MASTER_KEY AND mas.STATUS = 'LIV' AND bev.KEY97 = lcp.KEY97 AND bev.REFNO_SERL ="
                                          + evvcount + " AND mas.MASTER_REF = '" + refNumber + "' AND bev.REFNO_PFIX = 'POD'";
                              // //Loggers.general().info(LOG,"Payment due date query " + query);
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while Payment due date
                                    // query");
                                    String paydt = rs1.getString(1);
                                    getPane().setPAYMNT(paydt);

                              }

                              String query1 = "SELECT prod.NAME, mas.MASTER_REF, mas1.MASTER_REF, bev1.REFNO_PFIX,bev.REFNO_PFIX, bev.REFNO_SERL FROM master mas, BASEEVENT bev, BASEEVENT bev1, master mas1, DOCSPRE doc, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND mas1.KEY97 = bev1.MASTER_KEY AND bev.KEY97 = doc.KEY97 AND mas1.PRODTYPE = prod.KEY97 AND mas.MASTER_REF = '"
                                          + refNumber + "' AND bev1.REFNO_PFIX='CRE' AND bev.REFNO_SERL=" + evvcount + "";
                              // Loggers.general().info(LOG,"Previous product select query===>" +
                              // query1);
                              ps1 = con.prepareStatement(query1);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while Payment due date
                                    // query");
                                    String prodName = rs1.getString(1).trim();
                                    // Loggers.general().info(LOG,"Previous product select query
                                    // value" + prodName);
                                    // Loggers.general().info(LOG,"categoryCode value" +
                                    // categoryCode);
                                    if (subProd.equalsIgnoreCase("CRY") && prodName.equalsIgnoreCase("FCA")
                                                && !categoryCode.equalsIgnoreCase("FCY") && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                            || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addError(ErrorType.Other,
                                                      "Previous finance is FCA Please select category Prev.Finance in FCY [CM]");
                                    } else if (subProd.equalsIgnoreCase("CRY")
                                                && (prodName.equalsIgnoreCase("INA") || prodName.equalsIgnoreCase("HCA"))
                                                && !categoryCode.equalsIgnoreCase("INA") && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                            || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addError(ErrorType.Other,
                                                      "Previous finance is INR currency Please select category Prev.Finance in INA [CM]");
                                    } else {

                                    }

                              }

                        }

                        catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Payment due date exception" + e.getMessage());
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
                        // Loggers.general().info(LOG,"Payment due date major code" +
                        // getMajorCode());
                        getPane().setPAYMNT("");
                  }

                  // Notional due date population

                  if (getMajorCode().equalsIgnoreCase("FEL") || getMajorCode().equalsIgnoreCase("FOC")
                              || getMajorCode().equalsIgnoreCase("FIC") || getMajorCode().equalsIgnoreCase("FIL")) {

                        getfinaceNotionalDate();
                  } else {

                        getPane().setSIGVALDT("");
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

                  // Notional--------------------------------------------------------------

                  try {
                        List<ExtEventFXMULTIPLE> fxmultiple = (List<ExtEventFXMULTIPLE>) getWrapper().getExtEventFXMULTIPLE();
                        // //Loggers.general().info(LOG,"fxmultiple.size ---->");
                        for (int i = 0; i < fxmultiple.size(); i++) {

                              ExtEventFXMULTIPLE fxmul = fxmultiple.get(i);

                              BigDecimal bigamt = fxmul.getAMTCUY();
                              // //Loggers.general().info(LOG,"Fxmultiple bigamt---->" + bigamt);
                              BigDecimal notion = fxmul.getNOTION();
                              // //Loggers.general().info(LOG,"Fxmultiple notion---->" + notion);
                              BigDecimal inreq = notion.multiply(bigamt);
                              // //Loggers.general().info(LOG,"Fxmultiple multiple value---->" +
                              // inreq);
                              // BigDecimal equ = new BigDecimal(equi_bill);
                              fxmul.setINREQUL(inreq);
                              fxmul.setINREQULCurrency("INR");
                              // //Loggers.general().info(LOG,"Fxmultiple multiple final
                              // value---->"
                              // + fxmul.getINREQULCurrency());
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception in validating for notional rate
                        // - " + e.getMessage());
                  }

                  String mastamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                  String mastcur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");

                  if (getMajorCode().equalsIgnoreCase("FEL")
                              && (getMinorCode().equalsIgnoreCase("CSA4") || getMinorCode().equalsIgnoreCase("RSA4"))) {

                        String refNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                        // String crossNumber =
                        // getDriverWrapper().getEventFieldAsText("EVXR", "r",
                        // "").trim();

                        // //Loggers.general().info(LOG,"refNumber----->" + refNumber);
                        String evnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                        // //Loggers.general().info(LOG,"event refNumber ----->" + evnt);
                        String evvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");
                        // //Loggers.general().info(LOG,"Event count for posting----->" +
                        // evvcount);
                        try {
                              con = getConnection();
                              String query = "select ext.OWNLC from master mas,baseevent bas,extevent ext where mas.KEY97=bas.MASTER_KEY and bas.KEY97=ext.EVENT and mas.MASTER_REF ='"
                                          + refNumber + "' and bas.REFNO_PFIX='" + evnt + "' and bas.REFNO_SERL='" + evvcount + "'";
                              // //Loggers.general().info(LOG,"OWNLC checking query " + query);
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String ownlc = rs1.getString(1);
                                    // Loggers.general().info(LOG,"ownlc value=======>" + ownlc);
                                    if (ownlc.equalsIgnoreCase("Y")) {
                                          getPane().setOWNLC(true);
                                          getWrapper().setOWNLC(true);
                                    } else {
                                          getPane().setOWNLC(false);
                                          getWrapper().setOWNLC(false);
                                    }
                              }
                        }

                        catch (Exception e) {
                              // Loggers.general().info(LOG,"Payment due date exception" +
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
                  } else {
                        // Loggers.general().info(LOG,"Ownlc major code" + getMajorCode());
                  }

                  // PreShipment Link
                  try {

                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentFINEXPLCCREclayHyperlink();
                        dmsh.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlPreshipmentFINOUTCOLCREclayHyperlink();
                        dmsh1.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlPreshipmentFINOUTCOLAMDclayHyperlink();
                        dmsh2.setUrl(Preshipment);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception in setting the PreShipment link
                        // " + e.getMessage());
                  }
                  // grace days and due date calculation
                  SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                  Calendar c2 = Calendar.getInstance();
                  String expdate = getDriverWrapper().getEventFieldAsText("B+PD", "d", ""); // 12/10/16
                  // //Loggers.general().info(LOG,"Due date --->" + expdate);
                  String procode = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
                  // //Loggers.general().info(LOG,"Product code --->" + procode);
                  int gra = 0;
                                    

                  try {
                        getLOBISSUE();

                  } catch (Exception ee) {
                        Loggers.general().info(LOG,ee.getMessage());
                  }

                  String finCol = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                  Loggers.general().info(LOG,"fincol" + finCol);

                  if (getMajorCode().equalsIgnoreCase("FOC") && !finCol.equalsIgnoreCase("FEC")) {
                        try {
                              // //Loggers.general().info(LOG,"get value for LOB");
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                        }

                        String lobCreate = getWrapper().getLOB().trim();
                        Loggers.general().info(LOG,"LOB code in ODC finance screen =====>" + lobCreate);
                        /*if ((lobCreate == null || lobCreate.equalsIgnoreCase(""))
                                    && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                              validationDetails.addWarning(WarningType.Other,
                                          "LOB code is blank in finance screen, Please view the finance screen in maker step to update the LOB [CM]");
                        }
*/
                  }

                  String financeCreate = getDriverWrapper().getEventFieldAsText("BFI", "l", "");
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"LOB code financeCreate create=======>" + financeCreate);
                  }
                  if (procode.equalsIgnoreCase("FOC") && finCol.equalsIgnoreCase("FEC")
                              && financeCreate.equalsIgnoreCase("Y")) {

                        String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                        String lobVal = "";
                        try {
                              con = getConnection();
                              String query = "SELECT trim(e.LOB),m.MASTER_REF,b.REFNO_PFIX FROM master m, BASEEVENT b, extevent e WHERE m.KEY97 =b.MASTER_KEY AND b.KEY97 =e.EVENT AND b.REFNO_PFIX IN ('FEC') AND trim(e.LOB) IS NOT NULL AND m.MASTER_REF ='"
                                          + mainMasterRef + "' order by b.REFNO_PFIX  ";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"LOB code update for financeCreate===>" + query);
                              }
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    lobVal = rs1.getString(1);
                                    getPane().setLOB(lobVal);

                              }
                        }

                        catch (Exception e) {

                              Loggers.general().info(LOG,"Exception LOB code update for FEL create" + e.getMessage());

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

                        String lobCreate = getWrapper().getLOB().trim();
                        Loggers.general().info(LOG,"LOB code in ODC finance screen =====>" + lobCreate);
                        Loggers.general().info(LOG,"LOB Code in lobval"+lobVal);
                        /*if ((lobCreate == null || lobCreate.equalsIgnoreCase(""))
                                    && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                              Loggers.general().info(LOG,"lob first error entered");
                              validationDetails.addWarning(WarningType.Other,
                                          "LOB code is blank in finance screen, Please view the finance screen in maker step to update the LOB [CM]");
                        }*/
                        Loggers.general().info(LOG,"Condition for lob"+!lobVal.equalsIgnoreCase(lobCreate));
/*
                        if (!lobVal.equalsIgnoreCase(lobCreate)
                                    && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                              Loggers.general().info(LOG,"lob second error entered");
                              validationDetails.addWarning(WarningType.Other,
                                          "FEC LOB and Subsidiary event finance LOB codes are mismatched, Please view the finance screen in maker step to update the LOB [CM]");
                        }*/

                  }

                  // ------------------------------------------------//
                  // interest subvention validation
                  String elisub = getWrapper().getELISUB();
                  // //Loggers.general().info(LOG,"Eligible value in string " + elisub);
                  String subv = getWrapper().getINTPERE();
                  // //Loggers.general().info(LOG,"Interest percentage value " + subv);
                  if (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub.trim().equalsIgnoreCase("NO")) {
                        if (!(subv.trim().equalsIgnoreCase("")) && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "Subvention Eligible is Blank or No then Interest percentage should be blank  [CM]");
                        } else {
                              // Loggers.general().info(LOG,"Subvention Eligible is YES");
                        }
                  } else {
                        // Loggers.general().info(LOG,"Subvention Eligible is YES");
                  }

                  try {

                        String Preshipment = getHyperPreshipment();
                        // //Loggers.general().info(LOG,"Preshipment URL - "+Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentFINEXPLCCREclayHyperlink();
                        dmsh.setUrl(Preshipment);
                        // //Loggers.general().info(LOG,"URL Set done");
                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Catched exception in FinElcCRE - " +
                        // e1.getMessage());
                  }

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                        }
                  }

                  // Category code populate based on input branch
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                        con = ConnectionMaster.getConnection();
                        if (!(BranchCode.length() == 0)) {
                              String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
                              // //Loggers.general().info(LOG,"BranchCode Query - " + sql6);
                              ps = con.prepareStatement(sql6);
                              rs = ps.executeQuery();
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

                  // Charge Account Validation
                  String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); // CODE
                  String custval = "";

                  String cust = "";
                  if (prodtype.trim().equalsIgnoreCase("ELC")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");// party
                                                                                                                        // id
                  } else if (prodtype.trim().equalsIgnoreCase("ODC")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        // String eventRefPrefix =
                        // getDriverWrapper().getEventFieldAsText("EPF", "s",
                        // "");//Event reference prefix
                        // //Loggers.general().info(LOG,"eventRefPrefix" + eventRefPrefix);
                        if (prodtype.trim().equalsIgnoreCase("FEC")) {
                              cust = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
                                                                                                                              // id
                        } else {
                              cust = getDriverWrapper().getEventFieldAsText("DRW", "p", "no");// party
                                                                                                                              // id
                        }

                  } else if (prodtype.trim().equalsIgnoreCase("FIC")) {
                        // Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("DBT", "p", "no");// party
                                                                                                                        // id

                  } else if (prodtype.trim().equalsIgnoreCase("FOC")) {
                        // Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");// party
                                                                                                                        // id

                  }

                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // //Loggers.general().info(LOG,"charge account collected " + chargecol);

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
                  
                  // NPA customer

                  try {

                        String productVal = "N";
                        if (prodtype.trim().equalsIgnoreCase("ELC")) {
                              // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              productVal = getDriverWrapper().getEventFieldAsText("BEN", "p", "cAJB");// party
                              // id
                        } else if (prodtype.trim().equalsIgnoreCase("ODC")) {
                              // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              // String eventRefPrefix =
                              // getDriverWrapper().getEventFieldAsText("EPF", "s",
                              // "");//Event reference prefix
                              // //Loggers.general().info(LOG,"eventRefPrefix" + eventRefPrefix);
                              if (prodtype.trim().equalsIgnoreCase("FEC")) {
                                    productVal = getDriverWrapper().getEventFieldAsText("DBT", "p", "cAJB");// party
                                    // id
                              } else {
                                    productVal = getDriverWrapper().getEventFieldAsText("DRW", "p", "cAJB");// party
                                    // id
                              }

                        } else if (prodtype.trim().equalsIgnoreCase("FIC")) {
                              // Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              productVal = getDriverWrapper().getEventFieldAsText("DBT", "p", "cAJB");// party
                              // id

                        } else if (prodtype.trim().equalsIgnoreCase("FOC")) {
                              // Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              productVal = getDriverWrapper().getEventFieldAsText("PRI", "p", "cAJB");// party
                              // id

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

                        /*
                         * String query =
                         * "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, BASEEVENT BEV, ETT_REFERRAL_TRACKING REF WHERE MAS.KEY97 = BEV.MASTER_KEY AND trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)  AND BEV.STATUS <>'a' AND TRIM(REF.MASTER_REF_NO) ='"
                         * + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode
                         * + "' AND REF.SUB_PRODUCT_CODE = '" + subproductCode +
                         * "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID"
                         * ;
                         */
                        String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, ETT_REFERRAL_TRACKING REF WHERE  trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)   AND TRIM(REF.MASTER_REF_NO) ='"
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
                        ps = con.prepareStatement(query);
                        rs1 = ps.executeQuery();
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
                                          ps = con.prepareStatement(query6);
                                          
                                          rs = ps.executeQuery();
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
                                                Loggers.general().info(LOG,"ETT_REFERRAL_TRACKING Warning Message query step2====>" + query6);
                                          }
                                          ps = con.prepareStatement(query6);
                                    
                                          rs = ps.executeQuery();
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
                        // Loggers.general().info(LOG,"Entered while referal count out of loop"
                        // + count);
                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception referal count" +
                        // e1.getMessage());
                  } finally {
                        try {
                              if (rs1 != null)
                                    rs1.close();
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

                        String query5 = "select count(*),trim(mas.PRICUSTMNM),ref.STATUS,ref.REFERRAL_STATUS from MASTER mas,ETT_REFERRAL_TRACKING ref where trim(mas.MASTER_REF)=ref.MASTER_REF_NO and mas.MASTER_REF='"
                                    + MasterReference
                                    + "' and ref.REFERRAL_STATUS='PEND' group by mas.PRICUSTMNM,ref.STATUS,ref.REFERRAL_STATUS";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query REFERRAL_STATUS pending" + query5);
                        }

                        con = getConnection();
                        ps = con.prepareStatement(query5);

                        rs = ps.executeQuery();
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
                  String prd_code = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();

                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                  String rtgs = getWrapper().getPROREMT();
                  String rtgspart = getWrapper().getRTGSPART();
                  
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
                                                      "Over due bill exists for this customer (" + cust + ") [CM]");
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

                        getutrNoGenerated();
                  } catch (Exception ee) {
                        Loggers.general().info(LOG,"UTR Number getutrNoGenerated" + ee.getMessage());

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
                  //End changes

                  Loggers.general().info(LOG,"before notional date fincol" + finCol);
                  if (!finCol.equalsIgnoreCase("FEC")) {

                        try

                        {

                              String mainrefNumber = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                              String mainevnt = getDriverWrapper().getEventFieldAsText("MEPF", "s", "");
                              String mainevvcount = getDriverWrapper().getEventFieldAsText("MESQ", "i", "");

                              con = getConnection();

                              String queryResult = "SELECT col.DOCREL_TYP,col.* FROM master mas, COLLMASTER col, PARTYDTLS part, BASEEVENT bas WHERE mas.KEY97 =col.KEY97 AND mas.KEY97 =bas.MASTER_KEY AND col.DRAWEE_PTY = part.KEY97 AND mas.MASTER_REF ='"
                                          + mainrefNumber + "' AND bas.REFNO_PFIX ='" + mainevnt + "' AND bas.REFNO_SERL="
                                          + mainevvcount + "";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Notional due date is blank queryResult" + queryResult);
                              }
                              dmsp = con.prepareStatement(queryResult);

                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {

                                    String DOCREL_TYP = dmsr.getString(1);

                                    if (DOCREL_TYP.length() > 0 && DOCREL_TYP.equalsIgnoreCase("P")
                                                && step_csm.equalsIgnoreCase("CBS Maker 1")
                                                && (!getMinorCode().equalsIgnoreCase("RSA1"))) {

                                          String notionalDate = getWrapper().getSIGVALDT();
                                          String productCRY = getDriverWrapper().getEventFieldAsText("PTP", "s", "");

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Notional due date in finance screen in loop" + notionalDate
                                                            + "productCRY===>" + productCRY);
                                          }

                                          if ((notionalDate == null || notionalDate.equalsIgnoreCase(""))
                                                      && !productCRY.equalsIgnoreCase("CRY")) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Notional due date is blank please go to finance screen and click validate [CM]");
                                          } else {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Notional due date is blank in finance screen in else");
                                                }
                                          }
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Notional due date is blank and not Sight transaction else");
                                          }
                                    }

                              }

                        } catch (Exception e) {

                              Loggers.general().info(LOG,"Exception in Notional due date is blank in finance screen" + e.getMessage());

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
                  }

                  // String finCol = getDriverWrapper().getEventFieldAsText("MEPF",
                  // "s", "");
                  Loggers.general().info(LOG,"fincol" + finCol);
                  // if((getMajorCode().equalsIgnoreCase("FOC")&&getMinorCode().equalsIgnoreCase("CSA4"))||(((getMajorCode().equalsIgnoreCase("FOC")&&getMinorCode().equalsIgnoreCase("CSA1")))
                  if ((getMajorCode().equalsIgnoreCase("FOC") || getMajorCode().equalsIgnoreCase("FEL"))
                              && subproductCode.equalsIgnoreCase("FCA")) {
                        // PostingCustom post = null;
                        // if(step_csm.equalsIgnoreCase("CSM Maker 1"))
                        // String strPSID =
                        // getDriverWrapper().getPostingFieldAsText("PSID", "").trim();
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
                                          Loggers.general().info(LOG,"Entering While Loop");
                                          Loggers.general().info(LOG,"FxRate" + fx);

                                    }

                                    getPane().setFX_RATE(fx);

                              }

                              String fx_rate = getWrapper().getFX_RATE();
                              Loggers.general().info(LOG,"getfx_rate-------" + fx_rate);
                              String interestType = getDriverWrapper().getEventFieldAsText("B+INTT", "s", "");
                              if ((getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4"))
                                          || (getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("CSA1"))) {
                                    

                                    if ((fx_rate == null || fx_rate.equalsIgnoreCase(""))
                                                && interestType.equalsIgnoreCase("Interest in advance - standard")
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                            || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                                          validationDetails.addError(ErrorType.Other,
                                                      "FX rate should not be blank,Please enquiry finance screen[CM]");
                                    }
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
                  //interest amt for increase or decrease
                  try
                  {
                        String underpaidInt = getDriverWrapper().getEventFieldAsText("FIU", "v", "m").trim();
                        String overpaidInt = getDriverWrapper().getEventFieldAsText("FIO", "v", "m").trim();
                        String intrepamtInt = getDriverWrapper().getEventFieldAsText("FIR", "v", "m").trim();
                        String fxrate = getDriverWrapper().getEventFieldAsText("cALD", "s", "").trim();
                        String postfxrate = getDriverWrapper().getEventFieldAsText("cASE", "s", "").trim();
                        System.out.println("Intrest amount and fxrate values: " + underpaidInt + " " + overpaidInt + " "
                                    + fxrate + " " + intrepamtInt + " " + postfxrate);
//                      Loggers.general().info(LOG,"FIU currency 4/9/19" +overpaidInt);
//                      Loggers.general().info(LOG,"fxrate 4/9/19" + fxrate);

                        // if((getMajorCode().equalsIgnoreCase("FEL")
                        // &&(getMinorCode().equalsIgnoreCase("ASA4")||
                        // getMinorCode().equalsIgnoreCase("RSA4")))||(getMajorCode().equalsIgnoreCase("FOC")
                        // && (getMinorCode().equalsIgnoreCase("ASA1")||
                        // getMinorCode().equalsIgnoreCase("RSA1"))))
                        if (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("ASA4")
                                    || getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("ASA1")) {
                              Loggers.general().info(LOG, "entered if 4/9/19");

                              if ((fxrate != null && !fxrate.equalsIgnoreCase(""))
                                          && (underpaidInt != null && !underpaidInt.equalsIgnoreCase(""))) {
                                    System.out.println("entered if underpaid");
                                    BigDecimal underint = new BigDecimal(underpaidInt);
                                    BigDecimal fxra = new BigDecimal(fxrate);
                                    System.out.println("Fxrate in bigdecimal 4/9/19" + fxra);
//                                                    System.out.println("FIU in bigdec 4/9/19" +underint);
                                    BigDecimal intamtinc = underint.multiply(fxra);
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String val1 = diff.format(intamtinc);
                                    System.out.println("mulitiplied 4/9/19" + intamtinc + " " + val1);
//                                                    System.out.println("String 4/9/19" +val1 );
                                    getPane().setINTAMTIN(val1 + " " + "INR");
                              } else {
                                    getPane().setINTAMTIN("0.00 INR");
                              }
                        }
                        if ((getMajorCode().equalsIgnoreCase("FEL"))
                                    && (getMinorCode().equalsIgnoreCase("ASA4") || getMinorCode().equalsIgnoreCase("RSA4"))
                                    || (getMajorCode().equalsIgnoreCase("FOC")) && (getMinorCode().equalsIgnoreCase("ASA1")
                                                || getMinorCode().equalsIgnoreCase("RSA1"))) {
                              System.out.println("entered if of major minor overpaid");
                              if ((postfxrate != null && !postfxrate.equalsIgnoreCase(""))
                                          && (overpaidInt != null && !overpaidInt.equalsIgnoreCase(""))) {
                                    System.out.println("entered if overpaid");
                                    BigDecimal overint = new BigDecimal(overpaidInt);
                                    BigDecimal postfxra = new BigDecimal(postfxrate);
                                    System.out.println("Fxrate in bigdecimal " + postfxra);
                                    BigDecimal intamtdec = overint.multiply(postfxra);
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String val1 = diff.format(intamtdec);
                                    System.out.println("mulitiplied overpaid" + intamtdec + " " + val1);
//                                                    System.out.println("String 4/9/19" +val1 );
                                    getPane().setINTAMTDE(val1 + " " + "INR");
                                    System.out.println("After getting===> " + getPane().getINTAMTDE());
                              } else {
                                    getPane().setINTAMTDE("0.00 INR");
                              }

                        }

                        // added on 19/09/2022 by Sushmita
                        if ((getMajorCode().equalsIgnoreCase("FEL"))
                                    && (getMinorCode().equalsIgnoreCase("ASA4") || getMinorCode().equalsIgnoreCase("RSA4"))
                                    || (getMajorCode().equalsIgnoreCase("FOC")) && (getMinorCode().equalsIgnoreCase("ASA1")
                                                || getMinorCode().equalsIgnoreCase("RSA1"))) {
                              System.out.println("entered if of major minor underpaid");
                              if ((postfxrate != null && !postfxrate.equalsIgnoreCase(""))
                                          && (intrepamtInt != null && !intrepamtInt.equalsIgnoreCase(""))) {
                                    System.out.println("entered if interest repay underpaid");
                                    BigDecimal intrepamt = new BigDecimal(intrepamtInt);
                                    BigDecimal postfxra = new BigDecimal(postfxrate);
                                    System.out.println("Fxrate in bigdecimal " + postfxra);
                                    BigDecimal amtpaid = intrepamt.multiply(postfxra);
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String val1 = diff.format(amtpaid);
                                    System.out.println("mulitiplied underpaid" + amtpaid + " " + val1);
//                                                    System.out.println("String 4/9/19" +val1 );
                                    getPane().setAMTPAID(val1 + " " + "INR");
                                    System.out.println("After getting===> " + getPane().getAMTPAID());
                              } else {
                                    getPane().setAMTPAID("0.00 INR");
                              }

                        }
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception e " + e.getMessage());
                  }
      //---------------------force debit start----------------------
                  
                  if ((step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Reject")||step_csm.equalsIgnoreCase("CBS Authoriser"))
                              &&( (getMajorCode().equalsIgnoreCase("FEL") && (getMinorCode().equalsIgnoreCase("CSA4")))
                              || (getMajorCode().equalsIgnoreCase("FOC") && (getMinorCode().equalsIgnoreCase("CSA1")))
                              ||(getMajorCode().equalsIgnoreCase("FIL") && (getMinorCode().equalsIgnoreCase("CSA3"))))) {
                        String forEvent = getFORCEDEBIT();
                        // String forFin = getFORCEDEBITFIN();

                        String TransForceDebit = getDriverWrapper().getEventFieldAsText("cAHW", "l", "").trim();
                        // String
                        // TransForceDebitFinance=getDriverWrapper().getEventFieldAsText("cAHW",
                        // "l","").trim();
                        Loggers.general().info(LOG,"Force Debit in main event " + TransForceDebit);
                        Loggers.general().info(LOG,"Force Debit in Finance " + forEvent);

                        /*
                         * if (forFin.equalsIgnoreCase("N")&&TransForceDebit.
                         * equalsIgnoreCase("Y")) {
                         *
                         * validationDetails.addError(ErrorType.Other,
                         * "Force Debit Flag should be ticked in subsidiary event [CM]"
                         * );
                         *
                         * } else
                         */ if (TransForceDebit.equalsIgnoreCase("N") && forEvent.equalsIgnoreCase("Y")) {
                              validationDetails.addError(ErrorType.Other,
                                          "Force Debit Flag should be ticked in subsidiary event [CM]");
                        }

                  }
                  //---------------------force debit end----------------------
                                          
                  
                  
//for third party warning
                  
                  if(getMajorCode().equalsIgnoreCase("FEL")&&getMinorCode().equalsIgnoreCase("CSA4")&&step_csm.equalsIgnoreCase("CBS Maker")&&step_Input.equalsIgnoreCase("i")){
                  try {

                        Loggers.general().info(LOG,"Entered fel ==>");
                        String Mast = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                        //String Evnt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
                String  Dbtpty = getDriverWrapper().getEventFieldAsText("DBT", "p", "1").trim();
                      String party=getDriverWrapper().getEventFieldAsText("DBT", "p", "e").trim();
                      Loggers.general().info(LOG,"debit party 1====>");

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"MasterReference-------->" + Mast);
                              //Loggers.general().info(LOG,"their Reference-------->" + Evnt);
                              Loggers.general().info(LOG,"debit party------->"+Dbtpty);
                              Loggers.general().info(LOG,"party------->"+party);
                        
                        }
                        con = getConnection();
                        String query1 = "select pty.address1,mas.priname_l1,mas.nprname_l1 from master mas, lcmaster lcm ,PARTYDTLS pty "
                                    + " where lcm.key97=mas.key97 and lcm.APP_PTY=pty.key97 "
                                    + "and mas.master_ref= '" + Mast +"'";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query Result for Third party check----------> " + query1);

                        }

                        ps1 = con.prepareStatement(query1);

                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {

                              String app = rs1.getString(1);
                              String ben=rs1.getString(3);
                              String rec=rs1.getString(2);
                              Loggers.general().info(LOG,"app----"+app);
                              Loggers.general().info(LOG,"ben----"+ben);
                              Loggers.general().info(LOG,"rec----"+rec);
                              
                              app=app.trim();
                              ben=ben.trim();
                              rec=rec.trim();
                              
                              int applength=app.length();
                              int benlength=ben.length();
                              int reclength=rec.length();
                              int dpartylength=Dbtpty.length();
                              
                              Loggers.general().info(LOG,"app length----"+applength);
                              Loggers.general().info(LOG,"ben length----"+benlength);
                              Loggers.general().info(LOG,"rec length----"+reclength);
                              Loggers.general().info(LOG,"dparty length--"+dpartylength);
                              
                              Loggers.general().info(LOG,"Ben============================>"+(dpartylength==benlength));
                              Loggers.general().info(LOG,"app============================>"+(dpartylength==applength));
                              Loggers.general().info(LOG,"rec============================>"+(dpartylength==reclength));
                              
                              
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Entering While Loop");
                              }
                              if( party.trim().equalsIgnoreCase("Beneficiary")||party.trim().equalsIgnoreCase("Applicant")
                                          || party.trim().equalsIgnoreCase("Received from bank"))
                              {
                                    Loggers.general().info(LOG,"not a third party");
                              }
                              else{
                                    Loggers.general().info(LOG,"third party---");   
                                    validationDetails.addWarning(WarningType.Other,
                                                "Third party not allowed[CM]");
                              }
                              if( party.trim().equalsIgnoreCase("Beneficiary"))
                              {
                                    Loggers.general().info(LOG,"beneficiary if---");
                                    if((Dbtpty.trim().equalsIgnoreCase(ben.trim())&&(dpartylength==benlength)))
                                    {
                                          Loggers.general().info(LOG,"not third party");
                                    }
                                    else
                                    {
                                          Loggers.general().info(LOG,"third party---");   
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Third party not allowed[CM]");
                                    
                                    }
                              }
                              if( party.trim().equalsIgnoreCase("Applicant"))
                              {
                                    Loggers.general().info(LOG,"applicant  if---");
                                    if((Dbtpty.trim().equalsIgnoreCase(app.trim())&&(dpartylength==applength)))
                                    {
                                          Loggers.general().info(LOG,"not third party");
                                    }
                                    else
                                    {
                                          Loggers.general().info(LOG,"third party---");   
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Third party not allowed[CM]");
                                    
                                    }
                              }
                              if( party.trim().equalsIgnoreCase("Received from bank"))
                              {
                                    Loggers.general().info(LOG,"received from if---");
                                    if((Dbtpty.trim().equalsIgnoreCase(rec.trim())&&(dpartylength==reclength)))
                                    {
                                          Loggers.general().info(LOG,"not third party");
                                    }
                                    else
                                    {
                                          Loggers.general().info(LOG,"third party---");   
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Third party not allowed[CM]");
                                    
                                    }
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
                  
                  //-------------------------Start of Preshipment--------------
                  
                  if ((getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("CSA1")) ||
                              (getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4"))
                              && (step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser")||step_csm.equalsIgnoreCase("CBS Reject"))) {
                        try {
                              
                              
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
                  
                  if (((getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("CSA1")) || (getMajorCode()
                              .equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4")))
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
                  
                  if (((getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("CSA1")) || (getMajorCode()
                              .equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4")))
                              && (step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser")||step_csm.equalsIgnoreCase("CBS Reject")) ){
                  
                        String eventREF1 = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();

                        
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
                              String query1 = "select count(*) from ett_preshipment_apiserver where masref='"+masterref+"'and eventref='"+eventREF1+ "'";

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
                                     String query2 = "select count(*) from ett_preshipment_apiserver where masref='"+masterref+"'and eventref='"+eventREF1+"' and LOAN_REF ='"+loanref+"' and REPAYAMT='"+amt+"' and CURR='"+cur+"'";
                              
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
                  if ((getMajorCode().equalsIgnoreCase("FOC") && getMinorCode().equalsIgnoreCase("RSA1")) || (getMajorCode()
                              .equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("RSA4")) )
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
                                    validationDetails.addError(ErrorType.Other, "Already payment event in process, please complete or abort earlier event to continue this event[CM]");
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
                  if((getMajorCode().equalsIgnoreCase("FEL") && getMinorCode().equalsIgnoreCase("CSA4"))||(getMajorCode().equalsIgnoreCase("FOC") &&getMinorCode().equalsIgnoreCase("CSA1")))
                  {try {
                        int cnt = 0;
                        Loggers.general () .info(LOG,"PCR/PCF--");
                        cnt = preshipWar();
                        Loggers.general () .info(LOG,"count" + cnt);
                        String subProductype = getDriverWrapper().getEventFieldAsText("PTP",
                                    "s", "");
                        Loggers.general () .info(LOG,"subProductype in finelccre==>" + subProductype);
                        if (cnt == 1 &&(subProductype.equalsIgnoreCase("BDO")||
                                    subProductype.equalsIgnoreCase("BDK")||subProductype.equalsIgnoreCase("FCA")
                                    ||subProductype.equalsIgnoreCase("HCA")||subProductype.equalsIgnoreCase("INA")||subProductype.equalsIgnoreCase("CBD")||subProductype.equalsIgnoreCase("IND"))) {
                              //Loggers.general () .info(LOG,"duplicate reference");
                              validationDetails.addWarning(WarningType.Other,"Packing credits are outstanding for the customer. Please check if this payment is to be used to knock off exsisting PCR/PCF [CM]");             
                              }
                  } catch (Exception ee) {
                        //ee.printStackTrace();
                        Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());

                  }
            }
                  //CR 140 ends
                  if (((getMajorCode().equalsIgnoreCase("FOC") && (getMinorCode().equalsIgnoreCase("CSA1")||getMinorCode().equalsIgnoreCase("JSA1"))) || (getMajorCode()
                              .equalsIgnoreCase("FEL") && (getMinorCode().equalsIgnoreCase("CSA4")||getMinorCode().equalsIgnoreCase("JSA4"))))
                              && (step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser")||step_csm.equalsIgnoreCase("CBS Reject")) ){
                        String mainMasterRef = getDriverWrapper().getEventFieldAsText("MMST", "r", "");
                        String mainEvntRef = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                        try{
                              int cnt=0,result=0;
                               String  intcon = getDriverWrapper().getEventFieldAsText("B+AM", "l", " ").trim();
                                 String party=getDriverWrapper().getEventFieldAsText("B+DP", "p", "no").trim();
                              
                                 if(intcon.equalsIgnoreCase("Y"))
                                 {
                               con = getConnection();
                               String query2 = "SELECT count(*) FROM GVPF WHERE  trim(GVCUS1)='"+party+"' "
                                    +" and GVCCY='INR' and GVPRF='R' and GVMVT='I'";
                        
                                          Loggers.general () .info(LOG,"Query Result for Preshipment2----------> " + query2);

                                    ps = con.prepareStatement(query2);

                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                          result=rs.getInt(1);
                                          if(result==1)
                                          cnt=cnt+1;
                                          
                                          Loggers.general () .info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                    }
                                    
                                    if (cnt==0&&(getMinorCode().equalsIgnoreCase("CSA4")||getMinorCode().equalsIgnoreCase("CSA1")))
                                    {
                                           con = getConnection();
                                           String query3 ="SELECT COUNT(*) FROM MASTER MAS, BASEEVENT BEV, BASEEVENT BEV1, MASTER MAS1, UBZONE.TIDATAITEM TID , UBZONE.MSTRSETTLE MSTR,"
                                     +" PARTYDTLS PTY WHERE BEV.MASTER_KEY    = MAS.KEY97(+) AND BEV.ATTACHD_EV  = BEV1.KEY97 AND BEV1.MASTER_KEY = MAS1.KEY97 AND TID.KEY97       = MSTR.KEY97"
                                     +" AND BEV.KEY97       = TID.EVENT_KEY AND MSTR.PAY_REC    = 'R' AND MSTR.CCY        = 'INR' AND MSTR.MOVEMENT   = 'I'"
                                     +" AND MSTR.SETTLE_PTY = PTY.KEY97 AND trim(MAS1.MASTER_REF) = '"+mainMasterRef+"' and trim(BEV1.REFNO_PFIX || lpad(BEV1.REFNO_SERL,3,0))='"+mainEvntRef+"' AND trim(PTY.CUS_MNM)     = '"+party+"'";
                                                      Loggers.general () .info(LOG,"Query Result for Preshipment2----------> " + query3);

                                                ps1 = con.prepareStatement(query3);

                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      result=rs1.getInt(1);
                                                      if(result==1)
                                                      cnt=cnt+1;
                                                      
                                                      Loggers.general () .info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                                }
                                    }
                                    //added for adjust
                                    if (cnt==0&&(getMinorCode().equalsIgnoreCase("JSA4")||getMinorCode().equalsIgnoreCase("JSA1")))
                                    {
                                           con = getConnection();
/*                                         String query3 ="SELECT COUNT(*) FROM UBZONE.MASTER MAS, UBZONE.TIDATAITEM TID , UBZONE.MSTRSETTLE MSTR,"
 +" UBZONE.PARTYDTLS PTY WHERE MAS.KEY97     = TID.MASTER_KEY AND TID.KEY97       = MSTR.KEY97 AND MSTR.SETTLE_PTY = PTY.KEY97 AND MSTR.PAY_REC    = 'R'"
 +" AND MSTR.CCY        = 'INR' AND MSTR.MOVEMENT   = 'I' AND trim(MAS.MASTER_REF)  = '"+mainMasterRef+"' AND trim(PTY.CUS_MNM)     = '"+party+"'";*/
                                           String query3 ="SELECT count(*) FROM UBZONE.MASTER MAS, UBZONE.TIDATAITEM TID , UBZONE.BASEEVENT BEV, UBZONE.MSTRSETTLE MSTR, UBZONE.PARTYDTLS PTY WHERE MAS.KEY97 = BEV.MASTER_KEY"
                                                       +" AND BEV.KEY97 = TID.EVENT_KEY AND TID.KEY97 = MSTR.KEY97 AND MSTR.SETTLE_PTY = PTY.KEY97 AND MSTR.PAY_REC = 'R' AND MSTR.CCY = 'INR' AND MSTR.MOVEMENT = 'I' and TID.DELETED='N'"
                                                       +" AND BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0)='"+mainEvntRef+"' AND trim(MAS.MASTER_REF) = '"+mainMasterRef+"' AND trim(PTY.CUS_MNM) = '"+party+"'";
                                          
                                                Loggers.general () .info(LOG,"Query Result for Preshipment2----------> " + query3);

                                                ps1 = con.prepareStatement(query3);

                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      result=rs1.getInt(1);
                                                      if(result==1)
                                                      cnt=cnt+1;
                                                      
                                                      Loggers.general () .info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                                }
                                    }
                                    //end
                                    if(cnt==0 &&(step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject")))
                                    {
                                          Loggers.general () .info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                          validationDetails.addWarning(WarningType.Other, "Settlement instruction(interest) is mandatory for the interest consolidation.Please pend and reopen the transaction for settlememt instruction to be updated to database[CM]");
                                    
                                    }
                                    if(cnt==0 &&(step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Authoriser")))
                                    {
                                          Loggers.general () .info(LOG,"Query Result for Preshipment2----------> " + cnt);
                                          validationDetails.addError(ErrorType.Other, "Settlement instruction(interest) is mandatory for the interest consolidation.Please pend and reopen the transaction for settlememt instruction to be updated to database[CM]");
                                    
                                    }
                        }
                        }
                        catch(Exception e)
                        {
                              Loggers.general () .info(LOG,"Exception in Interest----------> " + e.getMessage());
                        }
                        finally{
                              surrenderDB(con,ps1,rs1);
                        }
                  }
                  try
                  {
                  getPostingFxrate();
                  }
                  catch(Exception e)
                  {
                        
                  }
                  if((getMajorCode().equalsIgnoreCase("FOC")|| getMajorCode().equalsIgnoreCase("FEL")) && (getMinorCode().equalsIgnoreCase("CSA1") || getMinorCode().equalsIgnoreCase("CSA4"))&&(eventStep.equalsIgnoreCase("Input")||eventStep.equalsIgnoreCase("Review - final")||eventStep.equalsIgnoreCase("Post release"))) {
                  try
                  {  
                        con = getConnection();
                        String masterref1 = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                        String eventref1 = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                        String treasury1 = getDriverWrapper().getEventFieldAsText("cBQA", "s", "");
                        String treasury2 = getDriverWrapper().getEventFieldAsText("cBQB", "s", "");
                        String treasury3 = getDriverWrapper().getEventFieldAsText("cBQC", "s", "");
                        String treasury4 = getDriverWrapper().getEventFieldAsText("cBQD", "s", "");
                        String treasury5 = getDriverWrapper().getEventFieldAsText("cBQE", "s", "");
                        String treasamt1 = getDriverWrapper().getEventFieldAsText("cBQL", "v", "m");
                        String treasamt2 = getDriverWrapper().getEventFieldAsText("cBQN", "v", "m");
                        String treasamt3 = getDriverWrapper().getEventFieldAsText("cBQO", "v", "m");
                        String treasamt4 = getDriverWrapper().getEventFieldAsText("cBQP", "v", "m");
                        String treasamt5 = getDriverWrapper().getEventFieldAsText("cBQQ", "v", "m");
                        String treasccy1 = getDriverWrapper().getEventFieldAsText("cBQL", "v", "c");
                        String treasccy2 = getDriverWrapper().getEventFieldAsText("cBQN", "v", "c");
                        String treasccy3 = getDriverWrapper().getEventFieldAsText("cBQO", "v", "c");
                        String treasccy4 = getDriverWrapper().getEventFieldAsText("cBQP", "v", "c");
                        String treasccy5 = getDriverWrapper().getEventFieldAsText("cBQQ", "v", "c");
                        String forward1 = getDriverWrapper().getEventFieldAsText("cBQF", "s", "");
                        String forward2 = getDriverWrapper().getEventFieldAsText("cBQG", "s", "");
                        String forward3 = getDriverWrapper().getEventFieldAsText("cBQH", "s", "");
                        String forward4 = getDriverWrapper().getEventFieldAsText("cBQI", "s", "");
                        String forward5 = getDriverWrapper().getEventFieldAsText("cBQJ", "s", "");
                        String forwardamt1 = getDriverWrapper().getEventFieldAsText("cBQM", "v", "m");
                        String forwardamt2 = getDriverWrapper().getEventFieldAsText("cBQR", "v", "m");
                        String forwardamt3 = getDriverWrapper().getEventFieldAsText("cBQS", "v", "m");
                        String forwardamt4 = getDriverWrapper().getEventFieldAsText("cBQT", "v", "m");
                        String forwardamt5 = getDriverWrapper().getEventFieldAsText("cBQU", "v", "m");
                        String forwardccy1 = getDriverWrapper().getEventFieldAsText("cBQM", "v", "c");
                        String forwardccy2 = getDriverWrapper().getEventFieldAsText("cBQR", "v", "c");
                        String forwardccy3 = getDriverWrapper().getEventFieldAsText("cBQS", "v", "c");
                        String forwardccy4 = getDriverWrapper().getEventFieldAsText("cBQT", "v", "c");
                        String forwardccy5 = getDriverWrapper().getEventFieldAsText("cBQU", "v", "c");
                        String fincustid = getDriverWrapper().getEventFieldAsText("B+FT", "p", "cu");
                        String treasuryType = getDriverWrapper().getEventFieldAsText("cAZG", "s", "");
                        String prodsubtype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                        String duedate = getDriverWrapper().getEventFieldAsText("B+MD", "d", "");
                        if(!prodsubtype.equalsIgnoreCase("4FR")){
                        System.out.println(" finance forward reference number: "+forward1+","+forwardamt1+" ,"+forwardccy1);
                        
                        BigDecimal tamt1 = new BigDecimal("0");
                        if(treasamt1 != null && !treasamt1.trim().isEmpty()) {                              
                              tamt1 = new BigDecimal(treasamt1);
                        }
                        BigDecimal tamt2 = new BigDecimal("0");
                        if(treasamt2 != null && !treasamt2.trim().isEmpty()) {                              
                              tamt2 = new BigDecimal(treasamt2);
                        }
                        
                        BigDecimal tamt3 = new BigDecimal("0");
                        if(treasamt3 != null && !treasamt3.trim().isEmpty()) {                              
                              tamt3 = new BigDecimal(treasamt3);
                        }
                        
                        BigDecimal tamt4 = new BigDecimal("0");
                        if(treasamt4 != null && !treasamt4.trim().isEmpty()) {                              
                              tamt4 = new BigDecimal(treasamt4);
                        }
                        BigDecimal tamt5 = new BigDecimal("0");
                        if(treasamt5 != null && !treasamt5.trim().isEmpty()) {                              
                              tamt5 = new BigDecimal(treasamt5);
                        }
                        BigDecimal famt1 = new BigDecimal("0");
                        if(forwardamt1 != null && !forwardamt1.trim().isEmpty()) {                          
                              famt1 = new BigDecimal(forwardamt1);
                        }
                        BigDecimal famt2 = new BigDecimal("0");
                        if(forwardamt2 != null && !forwardamt2.trim().isEmpty()) {                          
                              famt2 = new BigDecimal(forwardamt2);
                        }
                        BigDecimal famt3 = new BigDecimal("0");
                        if(forwardamt3 != null && !forwardamt3.trim().isEmpty()) {                          
                              famt3 = new BigDecimal(forwardamt3);
                        }
                        BigDecimal famt4 =new BigDecimal("0");
                        if(forwardamt4 != null && !forwardamt4.trim().isEmpty()) {                          
                              famt4 = new BigDecimal(forwardamt4);
                        }
                        BigDecimal famt5 = new BigDecimal("0");
                        if(forwardamt5 != null && !forwardamt5.trim().isEmpty()) {                          
                              famt5 = new BigDecimal(forwardamt5);
                        }
                        
                        
                        if(masterref1 != null && !masterref1.trim().isEmpty()) {                            
                              masterref1=masterref1;
                        }
                        else {
                              masterref1="";
                        }
                        if(eventref1 != null && !eventref1.trim().isEmpty()) {                              
                              eventref1=eventref1;
                        }
                        else {
                              eventref1="";
                        }
                        if(treasury1 != null && !treasury1.trim().isEmpty()) {                              
                              treasury1=treasury1;
                        }
                        else {
                              treasury1="";
                        }
                        if(treasury2 != null && !treasury2.trim().isEmpty()) {                              
                              treasury2=treasury2;
                        }
                        else {
                              treasury2="";
                        }
                        if(treasury3 != null && !treasury3.trim().isEmpty()) {                              
                              treasury3=treasury3;
                        }
                        else {
                              treasury3="";
                        }
                        if(treasury4 != null && !treasury4.trim().isEmpty()) {                              
                              treasury4=treasury4;
                        }
                        else {
                              treasury4="";
                        }
                        if(treasury5 != null && !treasury5.trim().isEmpty()) {                              
                              treasury5=treasury5;
                        }
                        else {
                              treasury5="";
                        }
                        
                        if(forward1 != null && !forward1.trim().isEmpty()) {                          
                              forward1=forward1;
                        }
                        else {
                              forward1="";
                        }
                        if(forward2 != null && !forward2.trim().isEmpty()) {                          
                              forward2=forward2;
                        }
                        else {
                              forward2="";
                        }
                        if(forward3 != null && !forward3.trim().isEmpty()) {                          
                              forward3=forward3;
                        }
                        else {
                              forward3="";
                        }
                        if(forward4 != null && !forward4.trim().isEmpty()) {                          
                              forward4=forward4;
                        }
                        else {
                              forward4="";
                        }
                        if(forward5 != null && !forward5.trim().isEmpty()) {                          
                              forward5=forward5;
                        }
                        else {
                              forward5="";
                        }
                        if(forwardccy1 != null && !forwardccy1.trim().isEmpty()) {                          
                              forwardccy1=forwardccy1;
                        }
                        else {
                              forwardccy1="";
                        }
                        if(forwardccy2 != null && !forwardccy2.trim().isEmpty()) {                          
                              forwardccy2=forwardccy2;
                        }
                        else {
                              forwardccy2="";
                        }
                        if(forwardccy3 != null && !forwardccy3.trim().isEmpty()) {                          
                              forwardccy3=forwardccy3;
                        }
                        else {
                              forwardccy3="";
                        }
                        if(forwardccy4 != null && !forwardccy4.trim().isEmpty()) {                          
                              forwardccy4=forwardccy4;
                        }
                        else {
                              forwardccy4="";
                        }
                        if(forwardccy5 != null && !forwardccy5.trim().isEmpty()) {                          
                              forwardccy5=forwardccy5;
                        }
                        else {
                              forwardccy5="";
                        }
                        if(treasccy1 != null && !treasccy1.trim().isEmpty()) {                              
                              treasccy1=treasccy1;
                        }
                        else {
                              treasccy1="";
                        }
                        if(treasccy2 != null && !treasccy2.trim().isEmpty()) {                              
                              treasccy2=treasccy2;
                        }
                        else {
                              treasccy2="";
                        }
                        
                        if(treasccy3 != null && !treasccy3.trim().isEmpty()) {                              
                              treasccy3=treasccy3;
                        }
                        else {
                              treasccy3="";
                        }
                        if(treasccy4 != null && !treasccy4.trim().isEmpty()) {                              
                              treasccy4=treasccy4;
                        }
                        else {
                              treasccy4="";
                        }
                        if(treasccy5 != null && !treasccy5.trim().isEmpty()) {                              
                              treasccy5=treasccy5;
                        }
                        else {
                              treasccy5="";
                        }
                        
                        
                        if(treasamt1 != null && !treasamt1.trim().isEmpty()) {                              
                              treasamt1=treasamt1;
                        }
                        else {
                              treasamt1="";
                        }
                        if(treasamt2 != null && !treasamt2.trim().isEmpty()) {                              
                              treasamt2=treasamt2;
                        }
                        else {
                              treasamt2="";
                        }
                        if(treasamt3 != null && !treasamt3.trim().isEmpty()) {                              
                              treasamt3=treasamt3;
                        }
                        else {
                              treasamt3="";
                        }
                        if(treasamt4 != null && !treasamt4.trim().isEmpty()) {                              
                              treasamt4=treasamt4;
                        }
                        else {
                              treasamt4="";
                        }
                        if(treasamt5 != null && !treasamt5.trim().isEmpty()) {                              
                              treasamt5=treasamt5;
                        }
                        else {
                              treasamt5="";
                        }
                        if(forwardamt1 != null && !forwardamt1.trim().isEmpty()) {                          
                              forwardamt1=forwardamt1;
                        }
                        else {
                              forwardamt1="";
                        }
                        if(forwardamt2 != null && !forwardamt2.trim().isEmpty()) {                          
                              forwardamt2=forwardamt2;
                        }
                        else {
                              forwardamt2="";
                        }
                        if(forwardamt3 != null && !forwardamt3.trim().isEmpty()) {                          
                              forwardamt3=forwardamt3;
                        }
                        else {
                              forwardamt3="";
                        }
                        if(forwardamt4 != null && !forwardamt4.trim().isEmpty()) {                          
                              forwardamt4=forwardamt4;
                        }
                        else {
                              forwardamt4="";
                        }
                        if(forwardamt5 != null && !forwardamt5.trim().isEmpty()) {                          
                              forwardamt5=forwardamt5;
                        }
                        else {
                              forwardamt5="";
                        }
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
                                    "<TRESREF1>"+treasury1+"</TRESREF1>" +
                                    "<TRESREF2>"+treasury2+"</TRESREF2>" +
                                    "<TRESREF3>"+treasury3+"</TRESREF3>" +
                                    "<TRESREF4>"+treasury4+"</TRESREF4>" +
                                    "<TRESREF5>"+treasury5+"</TRESREF5>" +
                                    "<FWDCTREF1>"+forward1+"</FWDCTREF1>" +
                                    "<FWDCTREF2>"+forward2+"</FWDCTREF2>" +
                                    "<FWDCTREF3>"+forward3+"</FWDCTREF3>" +
                                    "<FWDCTREF4>"+forward4+"</FWDCTREF4>" +
                                    "<FWDCTREF5>"+forward5+"</FWDCTREF5>" +
                                    "<TRERFAM1>"+treasamt1+"</TRERFAM1>" +
                                    "<TRERFAM2>"+treasamt2+"</TRERFAM2>" +
                                    "<TRERFAM3>"+treasamt3+"</TRERFAM3>" +
                                    "<TRERFAM4>"+treasamt4+"</TRERFAM4>" +
                                    "<TRERFAM5>"+treasamt5+"</TRERFAM5>" +
                                    "<TRERFAM1CCY>"+treasccy1+"</TRERFAM1CCY>" +
                                    "<TRERFAM2CCY>"+treasccy2+"</TRERFAM2CCY>" +
                                    "<TRERFAM3CCY>"+treasccy3+"</TRERFAM3CCY>" +
                                    "<TRERFAM4CCY>"+treasccy4+"</TRERFAM4CCY>" +
                                    "<TRERFAM5CCY>"+treasccy5+"</TRERFAM5CCY>" +
                                    "<FWDRFAM1>"+forwardamt1+"</FWDRFAM1>" +
                                    "<FWDRFAM2>"+forwardamt2+"</FWDRFAM2>" +
                                    "<FWDRFAM3>"+forwardamt3+"</FWDRFAM3>" +
                                    "<FWDRFAM4>"+forwardamt4+"</FWDRFAM4>" +
                                    "<FWDRFAM5>"+forwardamt5+"</FWDRFAM5>" +
                                    "<FWDRFAM1CCY>"+forwardccy1+"</FWDRFAM1CCY>" +
                                    "<FWDRFAM2CCY>"+forwardccy2+"</FWDRFAM2CCY>" +
                                    "<FWDRFAM3CCY>"+forwardccy3+"</FWDRFAM3CCY>" +
                                    "<FWDRFAM4CCY>"+forwardccy4+"</FWDRFAM4CCY>" +
                                    "<FWDRFAM5CCY>"+forwardccy5+"</FWDRFAM5CCY>" +
                                    "<TREATXNTYPE>"+treasuryType+"</TREATXNTYPE>" +
                                    "<SUBFINMASTERREF>"+masterref1+"</SUBFINMASTERREF>" +
                                    "<SUBFINEVENTREF>"+eventref1+"</SUBFINEVENTREF>" +
                                    "<FINCUSTID>"+fincustid+"</FINCUSTID>" +
                                    "<DUEDATE>"+duedate+"</DUEDATE>" +
                                    "</TreasuryValidateRequest></ServiceRequest>";
                        
                        
                        ThemeTransportClient aClient = new ThemeTransportClient();
                        String resultXml=aClient.invoke("Treasury", "Validate", inputXml);
                        
                        System.out.println("Treasury Validate Result XML : "+resultXml);
                        
                        getPane().setTREQAM1(getTagValue(resultXml, "TRESREF1EQAMT"));
                        getPane().setTREQAM2(getTagValue(resultXml, "TRESREF2EQAMT"));
                        getPane().setTREQAM3(getTagValue(resultXml, "TRESREF3EQAMT"));
                        getPane().setTREQAM4(getTagValue(resultXml, "TRESREF4EQAMT"));
                        getPane().setTREQAM5(getTagValue(resultXml, "TRESREF5EQAMT"));
                        getPane().setFDEQAM1(getTagValue(resultXml, "FWDCTREF1EQAMT"));
                        getPane().setFDEQAM2(getTagValue(resultXml, "FWDCTREF2EQAMT"));
                        getPane().setFDEQAM3(getTagValue(resultXml, "FWDCTREF3EQAMT"));
                getPane().setFDEQAM4(getTagValue(resultXml, "FWDCTREF4EQAMT"));
                        getPane().setFDEQAM5(getTagValue(resultXml, "FWDCTREF5EQAMT"));
                        getPane().setTRFXRT1(getTagValue(resultXml, "TRESREF1FXRATE"));
                        getPane().setTRFXRT2(getTagValue(resultXml, "TRESREF2FXRATE"));
                        getPane().setTRFXRT3(getTagValue(resultXml, "TRESREF3FXRATE"));
                        getPane().setTRFXRT4(getTagValue(resultXml, "TRESREF4FXRATE"));
                        getPane().setTRFXRT5(getTagValue(resultXml, "TRESREF5FXRATE"));
                        getPane().setFDFXRT1(getTagValue(resultXml, "FWDCTREF1FXRATE"));
                        getPane().setFDFXRT2(getTagValue(resultXml, "FWDCTREF2FXRATE"));
                        getPane().setFDFXRT3(getTagValue(resultXml, "FWDCTREF3FXRATE"));
                        getPane().setFDFXRT4(getTagValue(resultXml, "FWDCTREF4FXRATE"));
                        getPane().setFDFXRT5(getTagValue(resultXml, "FWDCTREF5FXRATE"));
                        getPane().setTRERFAM1(getTagValue(resultXml, "TRESREF1AMTCCY"));
                        getPane().setTRERFAM2(getTagValue(resultXml, "TRESREF2AMTCCY"));
                        getPane().setTRERFAM3(getTagValue(resultXml, "TRESREF3AMTCCY"));
                        getPane().setTRERFAM4(getTagValue(resultXml, "TRESREF4AMTCCY"));
                        getPane().setTRERFAM5(getTagValue(resultXml, "TRESREF5AMTCCY"));
//                      getPane().setGRNDTOTL(getTagValue(resultXml, "TOTALAMT"));
                        String totalamt = getTagValue(resultXml, "TOTALAMT");
                        if(totalamt != null && !totalamt.trim().isEmpty() && !totalamt.trim().equals("0") ) {
                              System.out.println("inside if of total amount "+ totalamt);
                              getPane().setGRNDTOTL(getTagValue(resultXml, "TOTALAMT"));
                        }
                        String errMsg=getTagValue(resultXml, "ERRORMSG");
                        String warningMsg=getTagValue(resultXml, "WARNMSG");
                        if(errMsg != null && !errMsg.trim().isEmpty()) {                              
                              {
                                    validationDetails.addError(ErrorType.Other, errMsg);
                              }
                        }
                        if(warningMsg != null && !warningMsg.trim().isEmpty()) {                            
                              {
                                    validationDetails.addWarning(WarningType.Other, warningMsg);
                              }
                        }
                        getPane().setEXCHNGRT("");
                        String grandTotalStr = (getTagValue(resultXml, "TOTALAMT"));
                        int spacePosition= grandTotalStr.indexOf(" ");
                        grandTotalStr = grandTotalStr.substring(0,spacePosition);
                        System.out.println("grand total amount :"+grandTotalStr);
                        String amountStr = getDriverWrapper().getEventFieldAsText("B+AF", "v", "m");
                        System.out.println("amount to divide :"+amountStr);
                        BigDecimal  grandTotal= new BigDecimal(grandTotalStr);
                        BigDecimal amount = new BigDecimal(amountStr);
                        BigDecimal netPayAmt = new BigDecimal(0);
                        
                        BigDecimal result = new BigDecimal(0);
                        result= (tamt1).add(tamt2).add(tamt3).add(tamt4).add(tamt5).add(famt1).add(famt2).add(famt3).add(famt4).add(famt5);
                        String result1=result.toString();
                        if(treasccy1 != null && !treasccy1.trim().isEmpty()) {
                            getPane().setFINTOTAL(result1+" "+treasccy1);
                            }else {
                               getPane().setFINTOTAL(result1+" "+forwardccy1);
                            }
                  //    getPane().setFINTOTAL(result1+" "+treasccy1);
                        System.out.println("Treasury insert query "+result1);
                        netPayAmt=grandTotal.divide(amount,6, RoundingMode.HALF_UP);
      
                      System.out.println("total exchange rate "+netPayAmt);
//                    if(totalrate != null){
                        getPane().setEXCHNGRT(netPayAmt.toString());
//                    }
                        System.out.println(" finance forward reference number: "+forward1+","+forwardamt1+" ,"+forwardccy1);
                        System.out.println(" finance Treasury reference number: "+treasury1+","+treasamt1+" ,"+treasccy1);

                  
                        
                        }     
                  }
                  catch(Exception e)
                  {
                        e.printStackTrace();    
                  }
                  finally {
                        try {
                              if (ps1 != null)
                                    ps1.close();
                              if (con != null)
                                    con.close();
                        } catch (SQLException e) {
                              e.printStackTrace();
                        }
                  }

                  }
                  //-------------------------End of Preshipment--------------
                                    
                  if((getMajorCode().equalsIgnoreCase("FOC")|| getMajorCode().equalsIgnoreCase("FEL")) && (getMinorCode().equalsIgnoreCase("CSA1") || getMinorCode().equalsIgnoreCase("CSA4"))){
                        String amountStr = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                  //    String amountccy = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");
                        String postrate = getDriverWrapper().getEventFieldAsText("cASE", "s", "");
                        String stepid = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                        BigDecimal amountStr1= new BigDecimal(amountStr);
                        BigDecimal postrate1= new BigDecimal(postrate);
                        
                        BigDecimal result=amountStr1.multiply(postrate1);
                        result=result.setScale(2,BigDecimal.ROUND_HALF_EVEN);
                        getPane().setINTAMINR(result.toString()+" "+"INR");
                        String tenor = getDriverWrapper().getEventFieldAsText("PTN", "s", "");
                        String importOperating = getDriverWrapper().getEventFieldAsText("SEL", "p", "cBYI");
                  String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  int imptrade=0;
                  int tenorperiod=0;
                  
                  
                        try {
                              
                              // Loggers.general().info(LOG,"query " + query);
                              System.out.println(" EXPTRADE query: " +tenor+" "+subproCode+" "+importOperating);
                              
                              if(!importOperating.equals("")&&!importOperating.isEmpty() &&!tenor.equals("")&& !tenor.isEmpty()) {
                              imptrade=Integer.parseInt(importOperating);
                              tenorperiod=Integer.parseInt(tenor);
                              }
                              if (tenorperiod>imptrade) {
                                    System.out.println(" tenor period and import trade value "+tenorperiod+" "+imptrade);
                                    validationDetails.addWarning(WarningType.Other,"Tenor Period is greater than Customer export postshipment Operating Trade Cycle of "+imptrade+" days");
                              }
                        }catch (Exception e) {
                                    System.out.println("Exception update of tenor" + e.getMessage());
                              }
                        finally {
                         ConnectionMaster.surrenderDB(con, ps, rs);
                            
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
                        
                  }
                  
                  if((getMajorCode().equalsIgnoreCase("FOC")|| getMajorCode().equalsIgnoreCase("FEL"))){
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

      private String setValueTOString(double d1) {
            DecimalFormat df = new DecimalFormat("#.##");
            BigDecimal dValue = new BigDecimal(df.format(d1)).setScale(2, RoundingMode.HALF_UP);
            return String.valueOf(dValue);
      }
      public static boolean isValidString(String checkValue) {
            boolean result = false;
            if (checkValue != null && !checkValue.trim().isEmpty())
                  result = true;
            return result;
      }
      }