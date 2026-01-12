package com.misys.tiplus2.customisation.extension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;

//com.misys.tiplus2.customisation.extension.SupplyChainFinance

public class SupplyChainFinance extends ConnectionMaster {
//    private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(ConnectionMaster.class);

      Connection con, con1 = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst = null;
      ResultSet rs1, rs, rs2, dmsr, rst = null;

      // @Override
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
                  try {
                        // //Loggers.general().info(LOG,"get value for LOB");
                        getLob();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
                        // //Loggers.general().info(LOG,"LOB Catch");
                  } finally {
                        // //Loggers.general().info(LOG,"finally LOB ");
                  }

                  /*
                   * try{ String DMSHyperlink = getDMSHyperLINK();
                   * ExtendedHyperlinkControlWrapper dmsh8 =
                   * getPane().getCtlDMSREVFACCREclayHyperlink();
                   * dmsh8.setUrl(DMSHyperlink); ExtendedHyperlinkControlWrapper
                   * dmsh8s = getPane().getCtlDMSINVDISCREclayHyperlink();
                   * dmsh8s.setUrl(DMSHyperlink);
                   *
                   * } catch(Exception esd) { //Loggers.general().info(LOG,"DMSplace" +
                   * esd.getMessage()); }
                   */
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        // String TSTHyperlink = getTSTHyperLINK();
                        ExtendedHyperlinkControlWrapper TSTh8 = getPane().getCtlTSTREVFACCREclayHyperlink();
                        TSTh8.setUrl(TSTHyperlink);
                        // ExtendedHyperlinkControlWrapper TSTh8s =
                        // getPane().getCtlTSTINVDISCREclayHyperlink();
                        // TSTh8s.setUrl(TSTHyperlink);

                  } catch (Exception esd) {
                        // Loggers.general().info(LOG,"TSTplace" + esd.getMessage());
                  }
                  /*
                   * try{ String val = "ESB"; String ESBHyperlink = Link(val);
                   * //String ESBHyperlink = getESBHyperLINK();
                   * //ExtendedHyperlinkControlWrapper ESBh8 =
                   * getPane().getCtlESBREVFACCREclayHyperlink();
                   * //ESBh8.setUrl(ESBHyperlink); ExtendedHyperlinkControlWrapper
                   * ESBh8s = getPane().getCtlESBINVDISCREclayHyperlink();
                   * ESBh8s.setUrl(ESBHyperlink);
                   *
                   * } catch(Exception esd) { //Loggers.general().info(LOG,"ESBplace" +
                   * esd.getMessage()); }
                   */

                  /*
                   * String tradepro = getWrapper().getTRADEPRO_Name(); try{
                   *
                   * Connection con = ConnectionMaster.getConnection(); String dms=
                   * "select value1 from ett_parameter_tbl where parameter_id='DMS' and active = 'Y'"
                   * ; PreparedStatement dmsp=con.prepareStatement(dms);
                   * //Loggers.general().info(LOG,dms); ResultSet dmsr=dmsp.executeQuery();
                   * while(dmsr.next()) { String dmsstr = dmsr.getString(1); String
                   * dmsurl = dmsstr + tradepro; //Loggers.general().info(LOG,dmsurl);
                   * ExtendedHyperlinkControlWrapper dmsh =
                   * getPane().getCtlDMSOUTLAYOUTHyperlink(); dmsh.setUrl(dmsurl);
                   * ExtendedHyperlinkControlWrapper dmsh1 =
                   * getPane().getCtlDMSAdjustGUA_Group_LHyperlink();
                   * dmsh1.setUrl(dmsurl); ExtendedHyperlinkControlWrapper dmsh2 =
                   * getPane().getCtlDMSOUTGUR_AMEND_LHyperlink();
                   * dmsh2.setUrl(dmsurl); ExtendedHyperlinkControlWrapper dmsh3 =
                   * getPane().getCtlDMSOUTLAYISSHyperlink(); dmsh3.setUrl(dmsurl);
                   * ExtendedHyperlinkControlWrapper dmsh4 =
                   * getPane().getCtlDMSOUTGUR_CLARECHyperlink();
                   * dmsh4.setUrl(dmsurl); ExtendedHyperlinkControlWrapper dmsh5 =
                   * getPane().getCtlDMSMaintainHyperlink(); dmsh5.setUrl(dmsurl);
                   * ExtendedHyperlinkControlWrapper dmsh6 =
                   * getPane().getCtlDMSOrigGurLayoutHyperlink();
                   * dmsh6.setUrl(dmsurl); ExtendedHyperlinkControlWrapper dmsh7 =
                   * getPane().getCtlDMSExiprydateHyperlink(); dmsh7.setUrl(dmsurl);
                   *
                   * ExtendedHyperlinkControlWrapper dmsh8 =
                   * getPane().getCtlDMSSupplychain_LHyperlink();
                   * dmsh8.setUrl(dmsurl); ExtendedHyperlinkControlWrapper dmsh8s =
                   * getPane().getCtlDMSSupplychainsellHyperlink();
                   * dmsh8s.setUrl(dmsurl);
                   *
                   * } dmsr.close(); dmsp.close(); con.commit();con.close(); }
                   * catch(Exception e) { //Loggers.general().info(LOG,e.getMessage()); }
                   */

            }

            return false;
      }

      @Override
      public void onValidate(ValidationDetails validationDetails) {

            String eventStep= getDriverWrapper().getEventFieldAsText("CSTD", "s", "");
            String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
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
            if (dailyval.equalsIgnoreCase("NO") && !eventStatus.equalsIgnoreCase("Completed")) {
                  // try {
                  //
                  // // //Loggers.general().info(LOG,"Calling Button Action in validate");
                  // getPane().onSERVICEINVDISREPclayButton();
                  // getPane().onSERVICEINOVICECREATElayButton();
                  // getPane().onSERVICEREVFACCREclayButton();
                  // getPane().onSERVICEIRFCREATElayButton();
                  //
                  // } catch (Exception e2) {
                  // // TODO Auto-generated catch block
                  // e2.printStackTrace();
                  // }

                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                   step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

                  // try {
                  // String rtnf = "rtnf";
                  // String rtnfs = getDriverWrapper().getEventFieldAsText("PUO1",
                  // "s", "");
                  // if (((rtnfs.trim().equalsIgnoreCase("RTGS")) ||
                  // (rtnfs.trim().equalsIgnoreCase("RTG"))
                  // || (rtnfs.trim().equalsIgnoreCase("RTS")) ||
                  // (rtnfs.trim().equalsIgnoreCase("IBP")))
                  // && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  //
                  // rtnf = "RTGS";
                  // if (getIFSC().trim().equalsIgnoreCase("NEFT")) {
                  // validationDetails.addWarning(WarningType.Other, "Method of
                  // Proceeds Remitted (" + rtnf
                  // + ")does not match with IFSC VALUE(" + getIFSC() + ") [CM]");
                  // }
                  // } else if ((rtnfs.trim().equalsIgnoreCase("NEFT") ||
                  // (rtnfs.trim().equalsIgnoreCase("NFT"))
                  // || (rtnfs.trim().equalsIgnoreCase("NEF"))) &&
                  // (step_Input.equalsIgnoreCase("i"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // rtnf = "NEFT";
                  // if (getIFSC().trim().equalsIgnoreCase("RTGS")) {
                  // validationDetails.addWarning(WarningType.Other, "Method of
                  // Proceeds Remitted (" + rtnf
                  // + ")does not match with IFSC VALUE(" + getIFSC() + ") [CM]");
                  // }
                  // } else {
                  // //Loggers.general().info(LOG,"RTNFS" + rtnfs + "RTNF" + rtnf);
                  // }
                  //
                  // /*
                  // * if(!rtnf.trim().equalsIgnoreCase(getIFSC())) {
                  // * validationDetails.addWarning(WarningType.Other,
                  // * "IFSC code of the Beneficiary Bank Branch is "+getIFSC() +
                  // * " not equal to " + rtnf); } else{ //Loggers.general().info(LOG,
                  // * "Check the rtgs and neft"); }
                  // */
                  //
                  // } catch (Exception edf) {
                  // //Loggers.general().info(LOG,edf.getMessage());
                  // edf.getStackTrace();
                  // edf.printStackTrace();
                  // }
                  // //Loggers.general().info(LOG,"Service Called validate");
                  /*
                   * String tradepro = getWrapper().getTRADEPRO_Name(); try{
                   *
                   * Connection con = ConnectionMaster.getConnection(); String dms=
                   * "select value1 from ett_parameter_tbl where parameter_id='DMS' and active = 'Y'"
                   * ; PreparedStatement dmsp=con.prepareStatement(dms);
                   * //Loggers.general().info(LOG,dms); ResultSet dmsr=dmsp.executeQuery();
                   * while(dmsr.next()) { String dmsstr = dmsr.getString(1); String
                   * dmsurl = dmsstr + tradepro; //Loggers.general().info(LOG,dmsurl);
                   * ExtendedHyperlinkControlWrapper dmsh8 =
                   * getPane().getCtlDMSSupplychain_LHyperlink();
                   * dmsh8.setUrl(dmsurl);
                   *
                   * } dmsr.close(); dmsp.close(); con.commit();con.close(); }
                   * catch(Exception e) { //Loggers.general().info(LOG,e.getMessage()); }
                   */
                  /*
                   * try{ String DMSHyperlink = getDMSHyperLINK();
                   * ExtendedHyperlinkControlWrapper dmsh8 =
                   * getPane().getCtlDMSREVFACCREclayHyperlink();
                   * dmsh8.setUrl(DMSHyperlink); ExtendedHyperlinkControlWrapper
                   * dmsh8s = getPane().getCtlDMSINVDISCREclayHyperlink();
                   * dmsh8s.setUrl(DMSHyperlink);
                   *
                   * } catch(Exception esd) { //Loggers.general().info(LOG,"DMSplace" +
                   * esd.getMessage()); }
                   */
//                try {
//
//                      String TSTHyperlink = getTSTHyperLINK();
//                      ExtendedHyperlinkControlWrapper TSTh8 = getPane().getCtlTSTREVFACCREclayHyperlink();
//                      TSTh8.setUrl(TSTHyperlink);
//                      // ExtendedHyperlinkControlWrapper TSTh8s =
//                      // getPane().getCtlTSTINVDISCREclayHyperlink();
//                      // TSTh8s.setUrl(TSTHyperlink);
//
//                } catch (Exception esd) {
//                      // Loggers.general().info(LOG,"TSTplace" + esd.getMessage());
//                }

                  //// ifsc code validation for RTGS/NEFT
                  // try
                  // { //checking Proceed Remittant(Code)
                  // String
                  //// procrem=getDriverWrapper().getEventFieldAsText("PUL1","s","");
                  // String procrem2=procrem.substring(0,4);
                  // //Loggers.general().info(LOG,"checking Proceed Remittant(Code)"+procrem);
                  // //Loggers.general().info(LOG,"checking Proceed Remittant(Code)
                  //// substring"+procrem2);
                  // String ifsccode=getWrapper().getIFSCCO_Name();
                  // //int ifsclen=ifsccode.length();
                  // //Loggers.general().info(LOG,"Value of the IFSC Code entered "+ifsccode);
                  // String ifsccode1=ifsccode.toUpperCase().substring(0, 4);
                  // //Loggers.general().info(LOG,"Checking RTGS or NEFT "+ifsccode1);
                  // String ifsccode2=ifsccode.toUpperCase().substring(4,
                  //// ifsccode.length());
                  // //Loggers.general().info(LOG,"Checking RTGS or NEFT "+ifsccode2);
                  //
                  // if(ifsccode1.equalsIgnoreCase(procrem2)){
                  // try{
                  // Connection con=ConnectionMaster.getConnection();
                  // //Loggers.general().info(LOG,"trim value of ifsccode2"+ifsccode2.trim());
                  // String query="select count(*) from ETT_IFSC_TBL where
                  //// IFSC='"+ifsccode2.trim()+"'";
                  // int count=0;
                  // //Loggers.general().info(LOG,"query - "+query);
                  // ps1 = con.prepareStatement(query);
                  // rs1 = ps1.executeQuery();
                  // ps2 = con.prepareStatement(query);
                  // rs2 = ps2.executeQuery();
                  // //Loggers.general().info(LOG,"rs2.next() - "+rs2.next());
                  // //Loggers.general().info(LOG,"Before entering into while");
                  // while(rs1.next()){
                  // //Loggers.general().info(LOG,"Entered while");
                  // count=rs1.getInt(1);
                  // //Loggers.general().info(LOG,"value of count in while "+count);
                  // }
                  //// rs1.close();
                  //// rs2.close();
                  //// ps1.close();
                  //// ps2.close();
                  //// con.close();
                  // if(count==0 && (step_Input.equalsIgnoreCase("i")) &&
                  //// (step_csm.equalsIgnoreCase("CBS Maker"))){
                  // validationDetails.addError(ErrorType.Other, "Invalid IFSC
                  //// Code("+ifsccode2+") [CM]" );
                  // }
                  // }
                  // catch(Exception ee){
                  // //Loggers.general().info(LOG,"Error in try in side try"+ee.getMessage());
                  // }
                  // finally {
                  // try {
                  // if (con != null) {
                  // con.close();
                  // if (ps1 != null)
                  // ps1.close();
                  // if (rs1 != null)
                  // rs1.close();
                  // if (ps2 != null)
                  // ps2.close();
                  // if (rs2 != null)
                  // rs2.close();
                  // }
                  // } catch (SQLException e) {
                  // //Loggers.general().info(LOG,"Connection Failed! Check output console");
                  // e.printStackTrace();
                  // }
                  // }
                  // }
                  // else{
                  // //validationDetails.addError(ErrorType.Other, "Invalid IFSC
                  //// Code"+ifsccode2.trim());
                  //
                  // }
                  // }
                  //
                  // catch(Exception e){
                  // //Loggers.general().info(LOG,"Error catched in IFSC
                  //// Validation"+e.getMessage());
                  // }

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

                  // Charge Account Validation

                  String cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "no").trim();
                  // //Loggers.general().info(LOG,"Primary customer taking ----> " + cust);
                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // Loggers.general().info(LOG,"charge account collected " + chargecol);
                  String custval = "";

                  try {

                        con = getConnection();
                        String dms = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='" + masterref
                                    + "' and EVENT_REF = '" + eventREF + "'"; // AND
                                                                                                      // CUS_MNM!='"
                                                                                                      // + cust +
                                                                                                      // "'";
                        // Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE QUERY ----> " + dms);
                        PreparedStatement dmsp = con.prepareStatement(dms);

                        ResultSet dmsr = dmsp.executeQuery();
                        while (dmsr.next()) {
                              // //Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE IN WHILE
                              // BEFORE----> " + dmsr.getString(1));
                              custval = dmsr.getString(1);
                              // //Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE IN WHILE----> "
                              // +
                              // custval);
                              // //Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE for
                              // compare---->
                              // " + custval);

                              if (chargecol.equalsIgnoreCase("Y") && (!chargecol.equalsIgnoreCase("N"))) {

                                    // if (!custval.equalsIgnoreCase(cust) &&
                                    // (step_Input.equalsIgnoreCase("i")) &&
                                    // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    if ((step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          // Loggers.general().info(LOG,"custoemr number in query" +
                                          // custval);
                                          // Loggers.general().info(LOG,"custoemr number in
                                          // transaction" + cust);
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Account selected for charges does not belong to the Applicant  [CM]");

                                    } else {
                                          // Loggers.general().info(LOG,"charge account collected
                                          // matched");
                                    }
                              }

                        }

                        // //Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE OUT OF
                        // WHILE---->");

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"charge account collected----->" +
                        // e.getMessage());
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
                  // Over due bill exists for this customer
                  try {
                        con = getConnection();
                        String query = "select * from ETT_OVERDUE_BILCUS_EOD where CUSTOMER_ID= '" + cust + "'";

                        // Loggers.general().info(LOG,"Over due bill exists for this customer "
                        // + query);
                        // int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // //Loggers.general().info(LOG,"Entered while Over due bill exists
                              // for this customer");

                              if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    // //Loggers.general().info(LOG,"Over due bill exists for this
                                    // customer in if loop " + cust);
                                    validationDetails.addWarning(WarningType.Other,
                                                "Over due bill exists for this customer (" + cust + ")  [CM]");
                              }

                              else {
                                    // //Loggers.general().info(LOG,"Over due bill exists for this
                                    // customer in else " + cust);
                              }
                        }

                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception Over due bill" +
                        // e1.getMessage());
                  }

                  finally {
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
                  
                  ///error configured for duplicate events
                  try {
                   int cnt=0;
               //    Loggers.general().info(LOG,"duplicate reference");
                              cnt= getduplicate();
                              // Loggers.general().info(LOG,"count"+cnt);
                              if(cnt>1)
                              {
                                    // Loggers.general().info(LOG,"duplicate reference");
                                    validationDetails.addError(ErrorType.Other,
                                                "The event reference no already exsist kindly abort and recreate the event [CM]");
                              }
                        } catch (Exception ee) {
                              ee.printStackTrace();
                              //Loggers.general().info(LOG,"duplicate reference" + ee.getMessage());

                        }
                  
                  // Validation For EOD Status Signal......By Yogesh Jadhav-30-07-2022
                  String brnCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                  System.out.println("EOD Status Check brnCode:"+brnCode);
      //          String eodStatus=getStatusBasedOnBranchCode(brnCode);
      //          System.out.println("EOD Status Check eodStatus Signal: "+eodStatus);
//                if(eodStatus.equalsIgnoreCase("Y")) {
//                    validationDetails.addError(ErrorType.Other, "EOD Signal Status Submitted.");
//                }
            }
            
            //Added for Due Date Calculation - 01-09-2021
            String programme = "";
            String anchorParty = "";
            String counterParty = "";
            String issueDate = "";
            String stepId="";
            String invoiceType = "";
            String buyer = "";
            String seller = "";
            int tenorDays = 0;
            String dueDate = "";
            try{
                  stepId = getNullSafeString(getDriverWrapper().getEventFieldAsText("CSID", "s", "")).trim();
                  programme = getNullSafeString(getDriverWrapper().getEventFieldAsText("PROGC", "s", "")).trim();
                  anchorParty = getNullSafeString(getDriverWrapper().getEventFieldAsText("APTCMN", "s", "")).trim();
                  buyer = getNullSafeString(getDriverWrapper().getEventFieldAsText("INVBC", "s", "")).trim();
                  seller = getNullSafeString(getDriverWrapper().getEventFieldAsText("INVSC", "s", "")).trim();
                  issueDate = getNullSafeString(getDriverWrapper().getEventFieldAsText("ISS", "d", "")).trim();
                  invoiceType = getNullSafeString(getDriverWrapper().getEventFieldAsText("INTP", "s", "")).trim();
                  
                  if (getMajorCode().equalsIgnoreCase("INV")&& getMinorCode().equalsIgnoreCase("ICRE")&& stepId.equalsIgnoreCase("Input")) {
                        if(invoiceType.equalsIgnoreCase("Seller centric"))
                        {
                              counterParty = buyer;
                        }
                        else if(invoiceType.equalsIgnoreCase("Buyer centric")){
                              counterParty = seller;
                        }
                        tenorDays = getTenorDays(anchorParty, counterParty, programme);
                        if(tenorDays==0){
                              getPane().setINVDUDAT(issueDate);
                        }
                        else{
                              dueDate = getDueDate(tenorDays, issueDate);
                              getPane().setINVDUDAT(dueDate);
                        }
                  }
            }
            catch(Exception e){
                  e.printStackTrace();
            }
            
            //auto population of fields by Vishal G --28032022
            if (getMajorCode().equalsIgnoreCase("IDS")||getMajorCode().equalsIgnoreCase("IRF")&& getMinorCode().equalsIgnoreCase("IDCR")||getMinorCode().equalsIgnoreCase("IRCR")){
                  try {
                                          
                                          String cust = getDriverWrapper().getEventFieldAsText("INVSC", "s", "");
                                          String benname= getDriverWrapper().getEventFieldAsText("INVSN", "s", "");
                                          String rtgAmt= getDriverWrapper().getEventFieldAsText("B+DA", "v", "m");
                                          String rtgCcy= getDriverWrapper().getEventFieldAsText("B+DA", "v", "c");
                                          String rtgsType= getDriverWrapper().getEventFieldAsText("cAOA", "s", "");
                                          String branch= getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                                          String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                                          String ifsc="";
                                          String benacc="";
                                          if (!cust.trim().equalsIgnoreCase("") || cust != null) {
                                                con = ConnectionMaster.getConnection();
                                                  String query="SELECT BENCITY, BENBRN , BENBANK, IFSCCO , BENACC , BENTYP  FROM EXTCUST where CUST = '"+cust+"'";
                                                 System.out.println(query);
                                                 ps1 = con.prepareStatement(query);
                                                 rs1 = ps1.executeQuery();
                                                 while(rs1.next()) {

                                                       getPane().setBENCITY(rs1.getString(1));
                                                       getPane().setBENBRN(rs1.getString(2));
                                                       getPane().setBENBAK(rs1.getString(3));
                                                        ifsc=rs1.getString(4).trim();
                                                        benacc=rs1.getString(5).trim();
                                                      //  System.out.println(ifsc);
//                                                     getPane().setIFSCCO_Name(rs1.getString(4));
                                                        getPane().setIFSCCO_Name(ifsc);
                                                       getPane().setBENACC_Name(benacc);
                                                       getPane().setBENTYP(rs1.getString(6));
                                                      // getPane().setRECBNKCD_Name(ifsc.substring(0, 4));
                                                      //    setBENNAME_Name(rs1.getString(7));
                                                      //    System.out.println("INSIDE BENEFICIARY DETAILS");
                                                 }
                                                 getPane().setBENNAME_Name(benname);
                                                 getPane().setRTGSNEFT(rtgAmt+" "+rtgCcy);
                                                 getPane().setNARRTVE(masterref);
                                                 getPane().setSENDBNCD("026");
                                                 String query1="SELECT SXCUS1, SVNAFF  FROM SX20LF where SXCUS1 = '"+cust+"'";
                                                 System.out.println(query1);
                                                 ps1 = con.prepareStatement(query1);
                                                 rs1 = ps1.executeQuery();
                                                 while(rs1.next()) {
                                 String beneAddress=rs1.getString(2);
                                 if(beneAddress.length()>70) {
                                                       getPane().setBADDRE_Name(beneAddress.substring(0, 70));
                                 }
                                 else {
                                     System.out.println("INSIDE BENEFICIARY ADDRESS :"+beneAddress.length());
                                     getPane().setBADDRE_Name(beneAddress);
                                 }
                                                      //    setBENNAME_Name(rs1.getString(7));
                                                            
                                                 }
                                    /*           String ifscsub= ifsc.substring(0, 4);
                                                 String query2="SELECT BANK_CODE  FROM UBI_BANK_LIST where IFSC_BANK_CODE ='"+ifscsub+"'";
                                                 ps1 = con.prepareStatement(query2);
                                                 rs1 = ps1.executeQuery();
                                                 while(rs1.next()) {
                                                       getPane().setRECBNKCD_Name(ifsc.substring(0, 4));
                                                      
                                                 }
                                                 */
                                          }
                                          
                                          if (rtgsType.trim().equalsIgnoreCase("RTG"))  {
                              //          String      debtacc="1980050000";
                              //    String query="select BRCH_MNM,BO_ACCTNO from account where CATEGORY='"+debtacc+"' and brch_mnm='"+branch+"' "+
                           //               "AND CURRENCY='INR'";
                                    
                              //    ps1 = con.prepareStatement(query);
                              //     rs1 = ps1.executeQuery();
                              //     System.out.println("RTGS QUERY"+query);
                              //     while(rs1.next()) {
                              //           getPane().setDRINTACC(rs1.getString(2));
                                          
                              //     }  
                                                getPane().setCRPOOLAC("504505120004000");
                                                
                                          }
                                          
                                          if (rtgsType.trim().equalsIgnoreCase("NEF"))  {
                                                
                                          //    String      debtacc="1980050000";
                                                getPane().setCRPOOLAC("473802480013000");
                                                
                                          }
                                           BigDecimal tranAmt=new BigDecimal(rtgAmt);
                                           if((benacc.equalsIgnoreCase("")||benacc == null) || (ifsc.equalsIgnoreCase("")||ifsc == null)||ifsc.trim().isEmpty()) {
                                                 getPane().setPROREMT("IBP");
                                                 System.out.println("IBP");
                                           }
                                            if ((!benacc.equalsIgnoreCase("")&&benacc != null )&& (!ifsc.equalsIgnoreCase("") && ifsc != null) && (tranAmt.compareTo(new BigDecimal(200000))==1)) {
                                                 getPane().setPROREMT("RTG");
                                                 System.out.println("RTG");
                                           }
                                           if ((!benacc.equalsIgnoreCase("")&&benacc != null )&& (!ifsc.equalsIgnoreCase("") && ifsc != null) && (tranAmt.compareTo(new BigDecimal(200000))!=1)) {
                                                 getPane().setPROREMT("NEF");
                                                 System.out.println("NEF");
                                           }
                                           if (!ifsc.equalsIgnoreCase("") && ifsc != null &&!ifsc.trim().isEmpty()) {
                                                      String ifscsub= ifsc.substring(0, 4);
                                                      System.out.println("ifsc bank code"+ifscsub);
                                                       String query2="SELECT BANK_CODE  FROM UBI_BANK_LIST where IFSC_BANK_CODE ='"+ifscsub+"'";
                                                       ps1 = con.prepareStatement(query2);
                                                       rs1 = ps1.executeQuery();
                                                       while(rs1.next()) {
                                                             getPane().setRECBNKCD_Name(ifsc.substring(0, 4));
                                                            
                                                       }
                                                       }
                                    }
                  
                  catch (Exception e) {
                                                e.printStackTrace();
                                    }finally {
                                          try {
                                                if (con != null) {
                                                      con.close();
                                                if (ps1 != null)
                                                      ps1.close();
                                                if (rs1 != null)
                                                      rs1.close();
                                                }
                                          } catch (SQLException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                          }
                                          }
                  }
            
            
            if (getMajorCode().equalsIgnoreCase("IDS")&& getMinorCode().equalsIgnoreCase("IDCR")||(eventStep.equalsIgnoreCase("Input")||eventStep.equalsIgnoreCase("Review - final")||eventStep.equalsIgnoreCase("Post release")) ||
                        (step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CSM")
                                    || step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))){
                  try {
                  String cust = getDriverWrapper().getEventFieldAsText("INVBC", "s", "");
                  String branch= getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                  String acctype="SL";
                  System.out.println("result QUERY "+cust+" "+branch);
                  con = ConnectionMaster.getConnection();
                  String query="select BRCH_MNM,BO_ACCTNO from account where CUS_MNM='"+cust+"' and brch_mnm='"+branch+"' "+
                    "AND ACC_TYPE='ODA'";
            ps1 = con.prepareStatement(query);
             rs1 = ps1.executeQuery();
//           System.out.println("NEFT QUERY"+query);
             System.out.println("result QUERY "+rs1+" "+query);
             while(rs1.next()) {
                   System.out.println("RESULT SELLER"+query);
                   getPane().setDRINTACC(rs1.getString(2));
             }  

                  }
                  catch (Exception e) {
                        e.printStackTrace();
            }finally {
                  try {
                        if (con != null) {
                              con.close();
                        if (ps1 != null)
                              ps1.close();
                        if (rs1 != null)
                              rs1.close();
                        }
                  } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                  }}
            //Sagar
                  try{
                        String cust = getDriverWrapper().getEventFieldAsText("INVBC", "s", "");
                  if(pastDue(cust)) {
                        System.out.print("CHECK ERROR");
                        validationDetails.addWarning(WarningType.Other,"One or more finance transactions with the same parties as this transactions are past due.");
                  } }catch (Exception e) {
                        e.printStackTrace();
            }
                  // Validation For EOD Status Signal......By Yogesh Jadhav-30-07-2022
                  String brnCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                  System.out.println("EOD Status Check brnCode:"+brnCode);
//                String eodStatus=getStatusBasedOnBranchCode(brnCode);
//                System.out.println("EOD Status Check eodStatus Signal: "+eodStatus);
//                if(eodStatus.equalsIgnoreCase("Y")) {
//                    validationDetails.addError(ErrorType.Other, "EOD Signal Status Submitted.");
//                }
            }
            
            
            if (getMajorCode().equalsIgnoreCase("IRF")&& getMinorCode().equalsIgnoreCase("IRCR")||(eventStep.equalsIgnoreCase("Input")||eventStep.equalsIgnoreCase("Review - final")||eventStep.equalsIgnoreCase("Post release")) ||
                        (step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CSM")
                                    || step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))){
                  try {
                        
                  String cust = getDriverWrapper().getEventFieldAsText("INVSC", "s", "");
                  String branch= getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                  String acctype="SL";
                  System.out.println("result QUERY "+cust+" "+branch);
                  con = ConnectionMaster.getConnection();
                  String query="select BRCH_MNM,BO_ACCTNO from account where CUS_MNM='"+cust+"' and brch_mnm='"+branch+"' "+
                    "AND ACC_TYPE='ODA'";
            ps1 = con.prepareStatement(query);
             rs1 = ps1.executeQuery();
             System.out.println("result QUERY "+rs1+" "+query);
//           System.out.println("NEFT QUERY"+query);
             while(rs1.next()) {
                   System.out.println("RESULT BUYER"+query);
                   getPane().setDRINTACC(rs1.getString(2));
             }

                  }
                  catch (Exception e) {
                        e.printStackTrace();
            }finally {
                  try {
                        if (con != null) {
                              con.close();
                        if (ps1 != null)
                              ps1.close();
                        if (rs1 != null)
                              rs1.close();
                        }
                  } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                  }
            }
            //Sagar
            try {
                  String cust = getDriverWrapper().getEventFieldAsText("INVBC", "s", "");
       
                  if (pastDue(cust)) {
                      System.out.print("CHECK ERROR");
                      validationDetails.addWarning(WarningType.Other,
                            "One or more finance transactions with the same parties as this transactions are past due.");
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
                
            // Validation For EOD Status Signal......By Yogesh Jadhav-30-07-2022
            String brnCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
            System.out.println("EOD Status Check brnCode:"+brnCode);
//          String eodStatus=getStatusBasedOnBranchCode(brnCode);
//          System.out.println("EOD Status Check eodStatus Signal: "+eodStatus);
//          if(eodStatus.equalsIgnoreCase("Y")) {
//              validationDetails.addError(ErrorType.Other, "EOD Signal Status Submitted.");
//          }

      }
            
            
//          if (getMajorCode().equalsIgnoreCase("INV") || getMajorCode().equalsIgnoreCase("IBF") || getMajorCode().equalsIgnoreCase("IBP")){
//                try {
//                 // Validation For EOD Status Signal......By Yogesh Jadhav-30-07-2022
//                      String brnCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
//                      System.out.println("EOD Status Check brnCode:"+brnCode);
////                    String eodStatus=getStatusBasedOnBranchCode(brnCode);
////                    System.out.println("EOD Status Check eodStatus Signal: "+eodStatus);
////                    if(eodStatus.equalsIgnoreCase("Y")) {
////                        validationDetails.addError(ErrorType.Other, "EOD Signal Status Submitted.");
////                    }
//                } catch (Exception e) {
//                      e.printStackTrace();
//                }
//          }
      }

      private boolean isChargeAccountDiff(Connection con) {
            boolean isChargeAccountDiff = false;
            PreparedStatement ps = null;
            ResultSet rs = null;

            // //Loggers.general().info(LOG,"isChargeAccountDiff method Entered");
            try {
                  String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String account = getDriverWrapper().getEventFieldAsText("PRI", "q", "RCA").trim();
                  String ar[] = account.split("-");

                  // //Loggers.general().info(LOG,"Master Reference" + masterRefNumber);
                  String priCustStr = getDriverWrapper().getEventFieldAsText("PRI", "p", "no");
                  if (priCustStr != null) {
                        /*
                         * String chargeAccountCheckQuery =
                         * "select trim(p.bo_acc_no) from master m, baseevent b, relitem r, posting p where m.key97 = b.master_key and b.key97 = r.event_key and r.key97 = p.key97 and p.acc_type in ('CA', 'RB') and m.master_ref='"
                         * + masterRefNumber +
                         * "' and  trim(p.bo_acc_no) not in (select trim(bo_acctno) from account where trim(cus_mnm)='"
                         * + priCustStr + "')"; //Loggers.general().info(LOG,
                         * "chargeAccountCheckQuery - " + chargeAccountCheckQuery); ps =
                         * con.prepareStatement(chargeAccountCheckQuery); System.out
                         * .println("prepared statement for chargeAccountCheck - " +
                         * ps); rs = ps.executeQuery(); if (rs.next()) {
                         */
                        if (priCustStr != ar[2].trim()) {
                              isChargeAccountDiff = true;
                        }

                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception occured in isChargeAccountDiff - "
                  // + e.getMessage());
            } finally {
                  try {
                        if (con != null) {
                              con.close();
                              if (ps != null)
                                    ps.close();
                              if (rs != null)
                                    rs.close();
                        }
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
            return isChargeAccountDiff;
      }
      
      public int getTenorDays(String anchorParty,String counterParty,String programme){
            int tenorDays = 0;
            String query="";
            try{
            con1 = ConnectionMaster.getConnection();
            query="SELECT SCFM.MAXPERNUM FROM SCFMAP SCFM,SCFPROGRAM SCFP,SCFCPARTY SCFCP,SCFCPARTY SCFCP1 WHERE SCFP.KEY97 = SCFM.PROGRAMME AND SCFM.CPARTY = SCFCP.KEY97 AND SCFM.PARTY = SCFCP1.KEY97 AND trim(SCFP.CUSTOMER) = ? AND trim(SCFCP.CPARTY) = ? AND trim(SCFP.ID)=?";
            pst = con1.prepareStatement(query);
            pst.setString(1, anchorParty);
            pst.setString(2, counterParty);
            pst.setString(3, programme);
            rst = pst.executeQuery();
            while(rst.next()){
                  tenorDays=rst.getInt(1);
            }
            LOG.info("Tenor days"+tenorDays);
            }
            catch(Exception e){
                  e.printStackTrace();
            }
            finally{
                  try{
                        surrenderDB(con1, pst, rst);
                  }
                  catch(Exception e){
                        e.printStackTrace();
                  }
            }
            return tenorDays;
      }
      
      public String getDueDate(int tenor, String issueDate){
            String dueDate = "";
            String query="";
            try{
                  con1 = ConnectionMaster.getConnection();
                  query="select to_char(to_date(?,'DD/MM/YY')+?, 'DD/MM/YY') FROM DUAL";
                  pst = con1.prepareStatement(query);
                  pst.setString(1, issueDate);
                  pst.setInt(2,tenor);
                  rst = pst.executeQuery();
                  while(rst.next()){
                        dueDate = rst.getString(1);
                  }
                  LOG.info("Duedate: "+dueDate);
            }
            catch(Exception e){
                  e.printStackTrace();
            }
            finally{
                  try{
                        surrenderDB(con1, pst, rst);
                  }
                  catch(Exception e){
                        e.printStackTrace();
                  }
            }
            
            return dueDate;
      }
      
      //Sagar
      public boolean pastDue(String counterParty) {
            boolean flag=false;
            String query="";
            try{
                  con1 = ConnectionMaster.getConnection();
                  query="select mas.expiry_dat as fin_maturity_date, THEME_GENIUS_PKG.CONVAMT(MAS.CCY,MAS.AMT_O_S) AS FIN_OUTS_AMT, MAS.STATUS AS FIN_MASTER_STATUS, current_date from master mas, FNCEMASTER FIN where TRIM(mas.PRICUSTMNM)= ?  and MAS.STATUS IN ('LIV') and mas.amt_o_s > '0' and mas.expiry_dat < ( SELECT PROCDATE FROM DLYPRCCYCL)  AND FIN.KEY97 = MAS.KEY97 ";
                  System.out.println("query entered"+query);
                  pst = con1.prepareStatement(query);
                  pst.setString(1, counterParty);
                  System.out.print(counterParty+" CIF check");
                  rst=pst.executeQuery();
                  while(rst.next()) {
                        flag=true;
                        System.out.println("Flag changed");
                  }
                  System.out.println("Result set "+rst);
            }

            catch(Exception e){
            e.printStackTrace();
            }finally{
                  try{
                        surrenderDB(con1, pst, rst);
                  }
                  catch(Exception e){
                        e.printStackTrace();
                  }
            }

            
            System.out.println("Checking Code:SAGAR "+flag);
            
            return flag;
            }
}