package com.misys.tiplus2.customisation.extension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class SupplyChain_Repayment extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(SupplyChain_Repayment.class);

      public  void  onValidate(ValidationDetails validationDetails)
      {
            String strPropName = "MigrationDone";
            String dailyval ="";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP>
            queryRCODE = getDriverWrapper().createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'" );
            ////Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {
                        
             dailyval = PROPCode.getPropval();
                  ////Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" + PROPCode.getPropval());
            }
            else
            {
                  //Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
                  
            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
            if (dailyval.equalsIgnoreCase("NO") ) {
            try {

                  //Loggers.general().info(LOG,"Calling Button Action in validate for FCCT calculation");
                                    
//                getPane().onSERVICEOUTWRDCHEQUECREclayButton();
//                getPane().onSERVICEOUTWRDCHQREJclayButton();
//                
//                getPane().onSERVICEINVDISCREclayButton();
//                getPane().onSERVICEREVFACREPclayButton();
                  
      

            } catch (Exception e2) {
                  // TODO Auto-generated catch block
                  e2.printStackTrace();
            }
            
            
            
        String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
            //Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
            String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
            //Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);
            
            //GETTING LOB
            
            
            try{
                  ////Loggers.general().info(LOG,"get value for LOB");
                  getLob();
            }
            catch(Exception ee)
            {
                  //Loggers.general().info(LOG,ee.getMessage() );
               // //Loggers.general().info(LOG,"LOB Catch");
            }
            finally{
                  ////Loggers.general().info(LOG,"finally LOB ");
            }
            
            
            
             //  //Loggers.general().info(LOG,"123456");
               String stepid = getDriverWrapper().getEventFieldAsText("CSID","s", "");
               ////Loggers.general().info(LOG,"step is information"+stepid);
               ////Loggers.general().info(LOG,"Starts Coding for Supply Chain Finance");
             //  String repymentID=getWrapper().getREPAYBID();
               String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  ////Loggers.general().info(LOG,"Master Reference----->" +  masterRefNumber);
//             if(repymentID==null || repymentID=="")
//             {
//                   repymentID="X";
//             }
       // //Loggers.general().info(LOG,"step is information"+stepid);
        ////Loggers.general().info(LOG,"repyment ID"+repymentID);
       /* if((stepid.trim().equalsIgnoreCase("Authorise")))
        
        {
         //Loggers.general().info(LOG,"When Step  is Authorise" );
         try{
         Connection con = ConnectionMaster.getConnection();
         String query="UPDATE MASTER MAS SET AMT_O_S =AMT_O_S - (SELECT (REPAY_AMOUNT*100) FROM ETT_INVOICE_MATCHING ETM WHERE TRIM(REPAYMENT_BATCH_ID)=TRIM('"+repymentID+"') AND TRIM(ETM.BASEEVENT_KEY97)=TRIM(MAS.KEY97) AND TRIM(ETM.MAS_REF)=TRIM('"+masterRefNumber+"') and nvl(repayment_update,'X')!='Y') WHERE MAS.KEY97 IN (SELECT ETM.BASEEVENT_KEY97 FROM ETT_INVOICE_MATCHING ETM WHERE TRIM(REPAYMENT_BATCH_ID)=TRIM('"+repymentID+"') AND TRIM(ETM.MAS_REF)=TRIM('"+masterRefNumber+"')and nvl(repayment_update,'X')!='Y')";
         PreparedStatement dmsp = con.prepareStatement(query);
                        //Loggers.general().info(LOG,"First Query---->"+query);
                        int dmsr = dmsp.executeUpdate();
            //    validationDetails.addWarning(WarningType.Other,"Overrides authorised by maker if any, checked and confirmed");
            if(dmsr>0){
               //Loggers.general().info(LOG,"Supply Chain first query updated");
            }
            else{
               //Loggers.general().info(LOG,"Supply Chain first query not updated");
            }
         }
         catch(Exception ees)
                  {
                        //Loggers.general().info(LOG,"Error in first update Script"+ees.getMessage());
                  }
        
         try{
               Connection con = ConnectionMaster.getConnection();
               String query="UPDATE ETT_INVOICE_MATCHING SET REPAYMENT_UPDATE ='Y' WHERE TRIM(REPAYMENT_BATCH_ID)=TRIM('"+repymentID+"') AND TRIM(MAS_REF)=TRIM('"+masterRefNumber+"')";
                        PreparedStatement dmsp = con.prepareStatement(query);
                        //Loggers.general().info(LOG,"Update Invoice"+query);
                        int dmsr = dmsp.executeUpdate();
                 //    validationDetails.addWarning(WarningType.Other,"Overrides authorised by maker if any, checked and confirmed");
                 if(dmsr>0){
                     //Loggers.general().info(LOG,"Supply Chain Second query updated");
                 }
                 else{
                     //Loggers.general().info(LOG,"Supply Chain Second query not updated");
                 }    
            }
               catch(Exception ees)
                  {
                        //Loggers.general().info(LOG,"Error in second update Script"+ees.getMessage());
                  }
        }
        else{
                //Loggers.general().info(LOG,"Stepid is " + stepid);

        }
            */
      //Category code populate based on input branch
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN","b", "c");
                        con = ConnectionMaster.getConnection();
                        if (!(BranchCode.length() == 0)) {
                              String sql6="select telex from capf where cabrnm='"+BranchCode+"'";
                              ////Loggers.general().info(LOG,"BranchCode Query - " + sql6);
                               ps1 = con.prepareStatement(sql6);
                               rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String inmt = rs1.getString(1);
                                    ////Loggers.general().info(LOG,"category code - " + inmt);
                                    getWrapper().setIMBRCODE(inmt);
                                    getPane().setIMBRCODE(inmt);
                              }
                        
                        }
                        
                  }
                  catch(Exception e){
                        //Loggers.general().info(LOG,"Exception caught on branch code validation......"+e.getMessage());      
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
                              //Loggers.general().info(LOG,"Connection Failed! Check output console");
                              e.printStackTrace();
                        }
                  }
                  
            // Charge Account Validation
                  String cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "no").trim();
            ////Loggers.general().info(LOG,"Primary customer taking ----> " + cust);
            String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
            String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

            String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
            //Loggers.general().info(LOG,"charge account collected " + chargecol);
            String custval = "";

            try {

                  Connection con = getConnection();
                  String dms = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='" + masterref
                              + "' and EVENT_REF = '" + eventREF + "'"; // AND CUS_MNM!='"
                                                                                                // + cust + "'";
                  //Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE QUERY ----> " + dms);
                  PreparedStatement dmsp = con.prepareStatement(dms);

                  ResultSet dmsr = dmsp.executeQuery();
                  while (dmsr.next()) {
                        ////Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE IN WHILE BEFORE----> " + dmsr.getString(1));
                        custval = dmsr.getString(1);
                        ////Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE IN WHILE----> " + custval);
                        ////Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE for compare----> " + custval);

                        if (chargecol.equalsIgnoreCase("Y") && (!chargecol.equalsIgnoreCase("N"))) {

                              //if (!custval.equalsIgnoreCase(cust) && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              if ((step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    //Loggers.general().info(LOG,"custoemr number in query" + custval);
                                    //Loggers.general().info(LOG,"custoemr number in transaction" + cust);
                                    validationDetails.addWarning(WarningType.Other, "Account selected for charges does not belong to the Applicant  [CM]");

                              } else {
                                    //Loggers.general().info(LOG,"charge account collected matched");
                              }
                        }

                  }

                  ////Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE OUT OF WHILE---->");
                  dmsr.close();
                  dmsp.close();
                  con.close();
            } catch (Exception e) {
                  //Loggers.general().info(LOG,"charge account collected----->" + e.getMessage());
            }
            
            // Over due bill exists for this customer
            try {
                  con = getConnection();
                        String query = "select * from ETT_OVERDUE_BILCUS_EOD where CUSTOMER_ID= '" + cust + "'";

                  //Loggers.general().info(LOG,"Over due bill exists for this customer " + query);
                  // int count = 0;
                  ps1 = con.prepareStatement(query);
                  rs1 = ps1.executeQuery();
                  while (rs1.next()) {
                        ////Loggers.general().info(LOG,"Entered while Over due bill exists for this customer");

                        if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              ////Loggers.general().info(LOG,"Over due bill exists for this customer in if loop " + cust);
                              validationDetails.addWarning(WarningType.Other,
                                          "Over due bill exists for this customer (" + cust + ")  [CM]");
                        }

                        else {
                              ////Loggers.general().info(LOG,"Over due bill exists for this customer in else " + cust);
                        }
                  }

            } catch (Exception e1) {
                  //Loggers.general().info(LOG,"Exception Over due bill" + e1.getMessage());
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
                        //Loggers.general().info(LOG,"Connection Failed! Check output console");
                        e.printStackTrace();
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
            
            }
      }
      
      private boolean isChargeAccountDiff(Connection con) {
            boolean isChargeAccountDiff = false;
            PreparedStatement ps = null;
            ResultSet rs = null;

            //Loggers.general().info(LOG,"isChargeAccountDiff method Entered");
            try {
                  String masterRefNumber = getDriverWrapper().getEventFieldAsText(
                              "MST", "r", "");
                  String account = getDriverWrapper().getEventFieldAsText("PRI", "q", "RCA").trim();
                  String ar[]=account.split("-");
                  
                  // //Loggers.general().info(LOG,"Master Reference" + masterRefNumber);
                  String priCustStr = getDriverWrapper().getEventFieldAsText("PRI",
                              "p", "no");
                  if (priCustStr != null) {
                        /*String chargeAccountCheckQuery = "select trim(p.bo_acc_no) from master m, baseevent b, relitem r, posting p where m.key97 = b.master_key and b.key97 = r.event_key and r.key97 = p.key97 and p.acc_type in ('CA', 'RB') and m.master_ref='"
                                    + masterRefNumber
                                    + "' and  trim(p.bo_acc_no) not in (select trim(bo_acctno) from account where trim(cus_mnm)='"
                                    + priCustStr + "')";
                        //Loggers.general().info(LOG,"chargeAccountCheckQuery - "
                                    + chargeAccountCheckQuery);
                        ps = con.prepareStatement(chargeAccountCheckQuery);
                        System.out
                        .println("prepared statement for chargeAccountCheck - "
                                    + ps);
                        rs = ps.executeQuery();
                        if (rs.next()) {*/
                        if(priCustStr!=ar[2].trim()){
                              isChargeAccountDiff = true;
                        }
                  
            }} catch (Exception e) {
                  //Loggers.general().info(LOG,"Exception occured in isChargeAccountDiff - "    + e.getMessage());
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
                        //Loggers.general().info(LOG,"Connection Failed! Check output console");
                        e.printStackTrace();
                  }
            }
            return isChargeAccountDiff;
      }
      

}