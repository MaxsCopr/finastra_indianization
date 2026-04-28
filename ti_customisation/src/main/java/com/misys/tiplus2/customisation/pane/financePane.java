package com.misys.tiplus2.customisation.pane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventPrePurchaseOrderEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTax;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTaxEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.customisation.extension.OdcFEC;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.lang.control.EnigmaControl;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class financePane extends EventPane {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(financePane.class);
      Connection con;
      PreparedStatement peco, ps1, ps, dmsp, ttRatePS = null;
      ResultSet dmsr1, rs1, rs, dmsr, ttRateRS = null;

      public void ontotalcalcFinExColCreLayButton() {

            Connection con = null;
            PreparedStatement prepareStmt = null;
            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";
            // int count = 0;
            // int k = 0;
            int temp_count = 0;
            int sequence = 0;
            int cc = 0;
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
            ArrayList<String> loans = new ArrayList<String>();
            try {

                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  // Loggers.general().info(LOG,"temp value " + temp_count);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"temp value " + temp_count);

                  }
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "'";
                        // Loggers.general().info(LOG,"loan query is " + loan_query);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"loan query is " + loan_query);

                        }
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {
                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              // Loggers.general().info(LOG,"eneteerd while");

                        }

                        for (int i = 0; i < loans.size() / 3; i++) {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              // Loggers.general().info(LOG,"va " + cc);
                        }

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception int preshipment fetch " +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception int preshipment fetch " + e.getMessage());

                  }
            } finally {
                  ConnectionMaster.surrenderDB1(result_loan_emplty, prepareStmt, con);
            }

      }

      public void onRTGSFSAREPclayButton() {
            // Loggers.general().info(LOG,"IFSCFECTH");
            if (IFSCFECTH()) {
                  // Loggers.general().info(LOG," IFSCFECTH BUTTON");
            } else {
                  // Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
            }
      }

      public void ondisplayvalFINEXPLCCREclayButton() {
            // Loggers.general().info(LOG,"IFSCFECTH");
            if (IFSCFECTH()) {
                  // Loggers.general().info(LOG," IFSCFECTH BUTTON");
            } else {
                  // Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
            }
      }

      public void ondisplayvalFINOUTCOLCREclayButton() {
            // Loggers.general().info(LOG,"IFSCFECTH");
            if (IFSCFECTH()) {
                  // Loggers.general().info(LOG," IFSCFECTH BUTTON");
            } else {
                  // Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
            }
      }

      public boolean IFSCFECTH() {
            boolean value = false;
            // Loggers.general().info(LOG,"Exp lc button for POD");
            String Ifsc = getIFSCCO_Name().trim();
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
            if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                        // Loggers.general().info(LOG,"query for IFSC button" + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"query for IFSC button" + query);

                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        dmsr1 = ps1.executeQuery();
                        while (dmsr1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = dmsr1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"value of count in while " + count);

                              }
                        }

                        if (count == 0) {

                              // Loggers.general().info(LOG,"IFSC Code is Not valid ");
                              String ban = getBENBAK();
                              String bran = getBENBRN();
                              String cit = getBENCITY();
                              ban = "";
                              bran = "";
                              bran = "";
                              setBENBAK(ban);
                              setBENBRN(bran);
                              setBENCITY(bran);

                        } else {
                              try {
                                    // Loggers.general().info(LOG,"IFSC Code is valid");
                                    con = ConnectionMaster.getConnection();
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    dmsr1 = ps1.executeQuery();
                                    while (dmsr1.next()) {
                                          setBENBAK((dmsr1.getString(1).trim()));
                                          setBENBRN(dmsr1.getString(2).trim());
                                          setBENCITY((dmsr1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
                  } finally {
                        try {
                              if (dmsr1 != null)
                                    dmsr1.close();
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
                  // Loggers.general().info(LOG,"IFSC code is blak");
            }
            return value;
      }

      public boolean deleteLoanDetails() {
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
            try {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"New PreShipment method is Called");
                  }
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> loanDetails = getExtEventLoanDetailsData();
                  for (ExtEventLoanDetailsEntityWrapper detailsEntityWrapper : loanDetails) {
                        removeExtEventLoanDetails(detailsEntityWrapper);
                  }
                  return true;
            } catch (Exception e) {
                  e.printStackTrace();
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in PreShip Fetch Test");
                  }
                  return false;
            }
      }

      public void onFetchPreshipEXPCOLSETTclayButton() {
            String strLog = "Log";
            String dailyval_Log = "";
      //    String Subproduct="";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            // Loggers.general().info(LOG,"Enetered onFetchPreshipEXPCOLSETTclayButton");
            Connection con = null;
            PreparedStatement prepareStmt = null;

            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";
            // int count = 0;
            // int k = 0;
            // String[] currencies = { "INR", "USD", "EUR", "JPY", "GBP" };
            String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };

            int temp_count = 0;
            String startCurr = "";
            double sumAmount = 0;
            ArrayList<String> sumAmountInAllCurrencies;
            int sequence = 0;
            int count = 0;
            int cc = 0;
            String loanType = "";// This will fetch the Loan Type from the grid
            ArrayList<String> loans = new ArrayList<String>();
            try {

                  deleteLoanDetails();
                  sumAmountInAllCurrencies = new ArrayList<String>();
                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  /*Subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  Loggers.general().info(LOG,"Preshipment subproduct"+Subproduct);*/
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  // Loggers.general().info(LOG,"temp value " + temp_count);
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
                        Loggers.general().info(LOG,"loan query is " + loan_query);
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {
                              Loggers.general().info(LOG,"While loop loan " + ress.getString("LOAN"));
                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              loanType = ress.getString("TYPE");
                              // Loggers.general().info(LOG,"eneteerd while");

                        }
                        // Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Loan Type Value is " + loanType);

                        }
                        for (int i = 0; i < loans.size() / 3; i++) {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              /*setPRESHIP(Subproduct);
                              Loggers.general().info(LOG,"Preshipment subproduct"+ getPRESHIP());*/
                              // Loggers.general().info(LOG,"va " + cc);
                        }
                        //// setLOANTYPE(loanType);
                        try {
                              // Loggers.general().info(LOG,"PreShipment new Changes");
                              sumAmountInAllCurrencies = new ArrayList<String>();

                              for (int a = 0; a < currencies.length; a++) {
                                    try {
                                          String query = "SELECT NVL(SUM(REPAYAMT),'0') FROM ETT_PRESHIPMENT_APISERVER WHERE CURR='"
                                                      + currencies[a] + "' AND masref='" + masref + "' and eventref='" + eventRef
                                                      + "' ORDER BY CURR DESC";
                                          // Loggers.general().info(LOG,"PreShipment Sum query " +
                                          // query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"PreShipment Sum query " + query);

                                          }
                                          PreparedStatement prep = con.prepareStatement(query);
                                          ResultSet result = prep.executeQuery();
                                          if (result.next()) {
                                                double amount = 0;
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount is " + result.getString(1));

                                                if (currencies[a].equalsIgnoreCase("EUR")) {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in if loop" + amount);

                                                } else {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in else loop" + amount);
                                                }
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + amount);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Entered if and the Value of Amount in double" + amount);
                                                }
                                                // -----------DECIMAL
                                                // ISSUE-----------------------------------------------------------

                                                if (currencies[a].equalsIgnoreCase("USD")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {

                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("INR")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("JPY")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 1;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("GBP")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                // -------------------------------------------------------------------------------------
                                                //sumAmountInAllCurrencies.add(amount + " " + currencies[a]);
                                                
                                                //18-aug-19 decimal to bigdecimal for exponential in amount
                                                BigDecimal b_amt=new BigDecimal(amount);
                                                Currency cur=Currency.getInstance(currencies[a]);
                                                int precision=cur.getDefaultFractionDigits();
                                                RoundingMode DEFAULT_ROUNDING=RoundingMode.HALF_EVEN;
                                                BigDecimal roundoffvalue=null;
                                                roundoffvalue=b_amt.setScale(precision,DEFAULT_ROUNDING);
                                                sumAmountInAllCurrencies.add(roundoffvalue + " " + currencies[a]);
                                          } else {
                                                // Loggers.general().info(LOG,"ENtered else since the
                                                // Resultset is empty");
                                          }
                                          result.close();
                                          prep.close();

                                    } catch (SQLException sqlexception) {
                                          sqlexception.printStackTrace();
                                    }

                              }
                              // Loggers.general().info(LOG," sumAmountInAllCurrencies Size " +
                              // sumAmountInAllCurrencies.size());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," sumAmountInAllCurrencies Size " + sumAmountInAllCurrencies.size());
                              }
                              for (int b = 0; b < sumAmountInAllCurrencies.size(); b++) {
                                    // Loggers.general().info(LOG,"Inside For Loop " + b);
                                    if (b == 0) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTUSD(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 1) {
                                          setTOTJPY(sumAmountInAllCurrencies.get(b));
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                    }
                                    if (b == 2) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTINR(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 3) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTGBP(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 4) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTEUR(sumAmountInAllCurrencies.get(b));
                                    }
                              }
                              getExtEventLoanDetailsNew().setEnabled(false);
                              getExtEventLoanDetailsDelete().setEnabled(false);
                              getExtEventLoanDetailsUpdate().setEnabled(false);
                              getExtEventLoanDetailsUp().setEnabled(false);
                              getExtEventLoanDetailsDown().setEnabled(false);
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in try inside try" +
                              // e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in try inside try" + e.getMessage());
                              }
                        }

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception int preshipment fetch " +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception int preshipment fetch " + e.getMessage());
                  }
            } finally {
                  ConnectionMaster.surrenderDB(con, prepareStmt, result_loan_emplty);
            }

      }

      public void onFetchPreShipFINEXPLCCREclayButton() {
            String strLog = "Log";
            String dailyval_Log = "";
            //String Subproduct="";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            Connection con = null;
            PreparedStatement prepareStmt = null;

            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";

            String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };
            int temp_count = 0;
            String startCurr = "";
            double sumAmount = 0;
            ArrayList<String> sumAmountInAllCurrencies;
            int sequence = 0;
            int count = 0;
            int cc = 0;
            String loanType = "";// This will fetch the Loan Type from the grid
            ArrayList<String> loans = new ArrayList<String>();
            try {

                  deleteLoanDetails();

                  sumAmountInAllCurrencies = new ArrayList<String>();
                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  /*Subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  Loggers.general().info(LOG,"Preshipment subproduct"+Subproduct);
                  */
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  Loggers.general().info(LOG,"temp value " + temp_count);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"temp value " + temp_count);
                  }
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
                        Loggers.general().info(LOG,"loan query is=====1 " + loan_query);
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {
                              Loggers.general().info(LOG,"While loop loan query is=====2" + ress.getString("LOAN"));
                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              loanType = ress.getString("TYPE");
                              Loggers.general().info(LOG,"eneteerd while");

                        }
                        Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        for (int i = 0; i < loans.size() / 3; i++) {
                              Loggers.general().info(LOG,"loan value is=====3");
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              /*setPRESHIP(Subproduct);
                              Loggers.general().info(LOG,"Preshipment subproduct"+ getPRESHIP());*/
                        Loggers.general().info(LOG,"va " + cc);
                        }
                        //// setLOANTYPE(loanType);
                        try {
                              Loggers.general().info(LOG,"PreShipment new Changes");
                              sumAmountInAllCurrencies = new ArrayList<String>();

                              for (int a = 0; a < currencies.length; a++) {
                                    try {
                                          String query = "SELECT NVL(SUM(REPAYAMT),'0') FROM ETT_PRESHIPMENT_APISERVER WHERE CURR='"
                                                      + currencies[a] + "' AND masref='" + masref + "' and eventref='" + eventRef
                                                      + "' ORDER BY CURR DESC";
                                          Loggers.general().info(LOG,"PreShipment Sum query " + query);
                                          PreparedStatement prep = con.prepareStatement(query);
                                          ResultSet result = prep.executeQuery();
                                          if (result.next()) {
                                                double amount = 0;
                                                double dob_amt = result.getDouble(1);
                                                 Loggers.general().info(LOG,"Entered if and the Value of Amount in double" + dob_amt);
                                                String amt = result.getString(1);
                                                BigDecimal amt_dec = new BigDecimal(amt);
                                                 Loggers.general().info(LOG,"Entered if and the Value of Amount in String " + amt + " Bigdecimal" + amt_dec);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Entered if and the Value of Amount in String " + amt
                                                                  + " Bigdecimal" + amt_dec);
                                                }
                                                if (currencies[a].equalsIgnoreCase("EUR")) {
                                                      amount = result.getDouble(1) / 100;
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in if loop" + amount);

                                                } else {
                                                      amount = result.getDouble(1) / 100;
                                                      Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in else loop" + amount);
                                                }
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + amount);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Entered if and the Value of Amount in double" + amount);
                                                }
                                                // -----------DECIMAL
                                                // ISSUE-----------------------------------------------------------

                                                if (currencies[a].equalsIgnoreCase("USD")) {
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in afte" + amount);
                                                } else {

                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("INR")) {
                                                      Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of     Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("JPY")) {
                                                      Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 1;
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("GBP")) {
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in afte" + amount);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in afte" + amount);
                                                      }
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                // -------------------------------------------------------------------------------------
                                                //18-aug-19 decimal to bigdecimal for exponential in amount
                                                BigDecimal b_amt=new BigDecimal(amount);
                                                Currency cur=Currency.getInstance(currencies[a]);
                                                int precision=cur.getDefaultFractionDigits();
                                                RoundingMode DEFAULT_ROUNDING=RoundingMode.HALF_EVEN;
                                                BigDecimal roundoffvalue=null;
                                                roundoffvalue=b_amt.setScale(precision,DEFAULT_ROUNDING);
                                                sumAmountInAllCurrencies.add(roundoffvalue + " " + currencies[a]);
//                                              sumAmountInAllCurrencies.add(amount + " " + currencies[a]);
                                          } else {
                                                 Loggers.general().info(LOG,"ENtered else since the    Resultset is empty");
                                          }
                                          result.close();
                                          prep.close();

                                    } catch (SQLException sqlexception) {
                                          sqlexception.printStackTrace();
                                    }

                              }
                              Loggers.general().info(LOG," sumAmountInAllCurrencies Size " + sumAmountInAllCurrencies.size());
                              for (int b = 0; b < sumAmountInAllCurrencies.size(); b++) {
                                    Loggers.general().info(LOG,"Inside For Loop " + b);
                                    if (b == 0) {
                                          Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTUSD(sumAmountInAllCurrencies.get(b));
                                          Loggers.general().info(LOG,"getusd"+getTOTUSD());
                                    }
                                    if (b == 1) {
                                          setTOTJPY(sumAmountInAllCurrencies.get(b));
                                          Loggers.general().info(LOG,"Value of b " + b);
                                          Loggers.general().info(LOG,"get1"+getTOTJPY());
                                    }
                                    if (b == 2) {
                                           Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTINR(sumAmountInAllCurrencies.get(b));
                                          Loggers.general().info(LOG,"get2"+getTOTINR());
                                    }
                                    if (b == 3) {
                                          Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTGBP(sumAmountInAllCurrencies.get(b));
                                          Loggers.general().info(LOG,"get3"+getTOTGBP());
                                    }
                                    if (b == 4) {
                                           Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTEUR(sumAmountInAllCurrencies.get(b));
                                          Loggers.general().info(LOG,"get4"+getTOTEUR());
                                    }
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in try inside try" +
                              // e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in try inside try" + e.getMessage());
                              }
                        }

                  }
                  getExtEventLoanDetailsNew().setEnabled(false);
                  getExtEventLoanDetailsDelete().setEnabled(false);
                  getExtEventLoanDetailsUpdate().setEnabled(false);
                  getExtEventLoanDetailsUp().setEnabled(false);
                  getExtEventLoanDetailsDown().setEnabled(false);

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception int preshipment fetch " +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception int preshipment fetch " + e.getMessage());
                  }
            } finally {
                  ConnectionMaster.surrenderDB(con, prepareStmt, result_loan_emplty);
            }

      }

      public void onFetchPreShipFINOUTCOLCREclayButton() {

            String strLog = "Log";
            String dailyval_Log = "";
            //String Subproduct="";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            // Loggers.general().info(LOG,"Enetered onFetchpreShipEXPCOLFECclayButton");
            Connection con = null;
            PreparedStatement prepareStmt = null;

            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";
            // int count = 0;
            // int k = 0;
            String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };
            int temp_count = 0;
            String startCurr = "";
            double sumAmount = 0;
            ArrayList<String> sumAmountInAllCurrencies;
            int sequence = 0;
            int count = 0;
            int cc = 0;
            String loanType = "";// This will fetch the Loan Type from the grid
            ArrayList<String> loans = new ArrayList<String>();
            try {

                  deleteLoanDetails();

                  sumAmountInAllCurrencies = new ArrayList<String>();
                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  /*Subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  Loggers.general().info(LOG,"Preshipment subproduct"+Subproduct);*/
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  // Loggers.general().info(LOG,"temp value " + temp_count);

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"temp value " + temp_count);
                  }
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
                        Loggers.general().info(LOG,"loan query is " + loan_query);
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {
                              Loggers.general().info(LOG,"While loop loan " + ress.getString("LOAN"));
                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              loanType = ress.getString("TYPE");
                              // Loggers.general().info(LOG,"eneteerd while");

                        }
                        // Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        }
                        for (int i = 0; i < loans.size() / 3; i++) {
                              Loggers.general().info(LOG,"For loop loan ");
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              /*setPRESHIP(Subproduct);
                              Loggers.general().info(LOG,"Preshipment subproduct"+ getPRESHIP());*/
                              // Loggers.general().info(LOG,"va " + cc);
                        }
                        //// setLOANTYPE(loanType);
                        try {
                              // Loggers.general().info(LOG,"PreShipment new Changes");
                              sumAmountInAllCurrencies = new ArrayList<String>();

                              for (int a = 0; a < currencies.length; a++) {
                                    try {
                                          String query = "SELECT NVL(SUM(REPAYAMT),'0') FROM ETT_PRESHIPMENT_APISERVER WHERE CURR='"
                                                      + currencies[a] + "' AND masref='" + masref + "' and eventref='" + eventRef
                                                      + "' ORDER BY CURR DESC";
                                          // Loggers.general().info(LOG,"PreShipment Sum query " +
                                          // query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"PreShipment Sum query " + query);
                                          }
                                          PreparedStatement prep = con.prepareStatement(query);
                                          ResultSet result = prep.executeQuery();
                                          if (result.next()) {
                                                double amount = 0;
                                                double dob_amt = result.getDouble(1);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + dob_amt);
                                                String amt = result.getString(1);
                                                BigDecimal amt_dec = new BigDecimal(amt);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in String " + amt + " Bigdecimal" +
                                                // amt_dec);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Entered if and the Value of Amount in String " + amt
                                                                  + " Bigdecimal" + amt_dec);
                                                }
                                                if (currencies[a].equalsIgnoreCase("EUR")) {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in if loop" + amount);

                                                } else {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in else loop" + amount);
                                                }
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + amount);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Entered if and the Value of Amount in double" + amount);
                                                }
                                                // -----------DECIMAL
                                                // ISSUE-----------------------------------------------------------

                                                if (currencies[a].equalsIgnoreCase("USD")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {

                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("INR")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("JPY")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 1;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("GBP")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                // -------------------------------------------------------------------------------------
                                                //18-aug-19 decimal to bigdecimal for exponential in amount
                                                BigDecimal b_amt=new BigDecimal(amount);
                                                Currency cur=Currency.getInstance(currencies[a]);
                                                int precision=cur.getDefaultFractionDigits();
                                                RoundingMode DEFAULT_ROUNDING=RoundingMode.HALF_EVEN;
                                                BigDecimal roundoffvalue=null;
                                                roundoffvalue=b_amt.setScale(precision,DEFAULT_ROUNDING);
                                                sumAmountInAllCurrencies.add(roundoffvalue + " " + currencies[a]);
//                                              sumAmountInAllCurrencies.add(amount + " " + currencies[a]);
                                          } else {
                                                // Loggers.general().info(LOG,"ENtered else since the
                                                // Resultset is empty");
                                          }
                                          result.close();
                                          prep.close();

                                    } catch (SQLException sqlexception) {
                                          sqlexception.printStackTrace();
                                    }

                              }
                              // Loggers.general().info(LOG," sumAmountInAllCurrencies Size " +
                              // sumAmountInAllCurrencies.size());
                              for (int b = 0; b < sumAmountInAllCurrencies.size(); b++) {
                                    // Loggers.general().info(LOG,"Inside For Loop " + b);
                                    if (b == 0) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTUSD(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 1) {
                                          setTOTJPY(sumAmountInAllCurrencies.get(b));
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                    }
                                    if (b == 2) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTINR(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 3) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTGBP(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 4) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTEUR(sumAmountInAllCurrencies.get(b));
                                    }
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in try inside try" +
                              // e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in try inside try" + e.getMessage());
                              }
                        }

                  }
                  getExtEventLoanDetailsNew().setEnabled(false);
                  getExtEventLoanDetailsDelete().setEnabled(false);
                  getExtEventLoanDetailsUpdate().setEnabled(false);
                  getExtEventLoanDetailsUp().setEnabled(false);
                  getExtEventLoanDetailsDown().setEnabled(false);

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception int preshipment fetch " +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception int preshipment fetch" + e.getMessage());
                  }
            } finally {
                  ConnectionMaster.surrenderDB(con, prepareStmt, result_loan_emplty);
            }

      }

      public void onCIFMARGINFSAAMDclayButton() {

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

            try {
                  String facid = getFACLTYID().trim();

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Facility id initially Finance Amend--->" + facid);

                  }
                  BigDecimal hundred = new BigDecimal(100);
                  DecimalFormat diff = new DecimalFormat("0.00");
                  diff.setMaximumFractionDigits(2);
                  // BigDecimal notionalRate = new BigDecimal(1);
                  BigDecimal margin_Amt = new BigDecimal(0);
                  // String magn = "";
                  String billAmt = "";

                  String curr = "";
                  // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                  try {
                        billAmt = getDriverWrapper().getEventFieldAsText("B+FD", "v", "m");

                        margin_Amt = new BigDecimal(billAmt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Bill amount--->" + margin_Amt);

                        }

                        curr = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");

                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Finance amount" + e.getMessage());
                        }
                  }

                  if (!facid.equalsIgnoreCase("") && facid != null) {
                        String query = "select MARGIN,INTEREST,PENINTEREST,PLR,BASERATE from customermargin where facility='"
                                    + facid + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query resultcustomer margin " + query);
                        }
                        con = ConnectionMaster.getConnection();
                        dmsp = con.prepareStatement(query);
                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {

                              String magVal = dmsr.getString(1);
                              String interest = dmsr.getString(2);
                              String penalInt = dmsr.getString(3);
                              String plr = dmsr.getString(4);
                              String baseRate = dmsr.getString(5);
                              // Loggers.general().info(LOG,"magn--->" + magn);
                              BigDecimal magValue = new BigDecimal(magVal);
                              // Loggers.general().info(LOG,"Margin " + magn);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG,"Final Bill amount--->" + margin_Amt);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total margin amount--->" + totalVal);
                              }
                              setPMARGIN(magVal);

                              setINTERDET(interest);
                              setTENO(penalInt);
                              setOURS(plr);
                              setLIBORAT(baseRate);

                              setMARAMT(totalVal + curr);

                        }

                  } else if (facid.equalsIgnoreCase("") || facid == null) {

                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");

                        String magVal = getPMARGIN();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Margin percentage--->" + magVal);
                        }
                        if ((magVal != null || !magVal.equalsIgnoreCase("")) && magVal.length() > 0) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Facility id blank and Margin percentage====>" + magVal);
                              }
                              BigDecimal magValue = new BigDecimal(magVal);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG,"Final Bill amount--->" + margin_Amt);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total margin amount--->" + totalVal);
                              }
                              setMARAMT(totalVal + curr);

                        } else {
                              setPMARGIN("");
                              setMARAMT("");
                              setINTERDET("");
                              setTENO("");
                              setOURS("");
                              setLIBORAT("");
                        }

                  }

                  else {

                        setPMARGIN("");
                        setMARAMT("");
                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");

                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in margin calcultion FSA Amend" + e.getMessage());
                  }
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

      public void onCIFMARGINFSACREclayButton() {

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

            try {
                  String facid = getFACLTYID().trim();

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Facility id initially Finance create--->" + facid);

                  }
                  BigDecimal hundred = new BigDecimal(100);
                  DecimalFormat diff = new DecimalFormat("0.00");
                  diff.setMaximumFractionDigits(2);
                  // BigDecimal notionalRate = new BigDecimal(1);
                  BigDecimal margin_Amt = new BigDecimal(0);
                  // String magn = "";
                  String billAmt = "";

                  String curr = "";
                  // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                  try {
                        billAmt = getDriverWrapper().getEventFieldAsText("B+FD", "v", "m");

                        margin_Amt = new BigDecimal(billAmt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Bill amount--->" + margin_Amt);

                        }
                        // margin_Amount = margin_Amt.divide(hundred);
                        // String marginAmount = diff.format(margin_Amount);
                        curr = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");

                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Finance amount" + e.getMessage());
                        }
                  }

                  if (!facid.equalsIgnoreCase("") && facid != null) {
                        String query = "select MARGIN,INTEREST,PENINTEREST,PLR,BASERATE from customermargin where facility='"
                                    + facid + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query resultcustomer margin " + query);
                        }
                        con = ConnectionMaster.getConnection();
                        dmsp = con.prepareStatement(query);
                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {

                              String magVal = dmsr.getString(1);
                              String interest = dmsr.getString(2);
                              String penalInt = dmsr.getString(3);
                              String plr = dmsr.getString(4);
                              String baseRate = dmsr.getString(5);
                              // Loggers.general().info(LOG,"magn--->" + magn);
                              BigDecimal magValue = new BigDecimal(magVal);
                              // Loggers.general().info(LOG,"Margin " + magn);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG,"Final Bill amount--->" + margin_Amt);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total margin amount--->" + totalVal);
                              }
                              setPMARGIN(magVal);

                              setINTERDET(interest);
                              setTENO(penalInt);
                              setOURS(plr);
                              setLIBORAT(baseRate);

                              setMARAMT(totalVal + curr);

                        }

                  } else if (facid.equalsIgnoreCase("") || facid == null) {

                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");

                        String magVal = getPMARGIN();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Margin percentage--->" + magVal);
                        }
                        if ((magVal != null || !magVal.equalsIgnoreCase("")) && magVal.length() > 0) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Facility id blank and Margin percentage====>" + magVal);
                              }
                              BigDecimal magValue = new BigDecimal(magVal);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG,"Final Bill amount--->" + margin_Amt);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total margin amount--->" + totalVal);
                              }
                              setMARAMT(totalVal + curr);

                        } else {
                              setPMARGIN("");
                              setMARAMT("");
                              setINTERDET("");
                              setTENO("");
                              setOURS("");
                              setLIBORAT("");
                        }

                  }

                  else {

                        setPMARGIN("");
                        setMARAMT("");
                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");

                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in margin calcultion FSA create" + e.getMessage());
                  }
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

      public void onCIFMARGINFINOUTCOLCREclayButton() {

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

            try {
                  String facid = getFACLTYID().trim();

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Facility id initially Finance collection--->" + facid);

                  }
                  BigDecimal hundred = new BigDecimal(100);
                  DecimalFormat diff = new DecimalFormat("0.00");
                  diff.setMaximumFractionDigits(2);
                  // BigDecimal notionalRate = new BigDecimal(1);
                  BigDecimal financeAmount = new BigDecimal(0);
                  BigDecimal margin_Amt = new BigDecimal(0);
                  // String magn = "";
                  String billAmt = "";
                  String financeAmt = "";
                  String curr = "";
                  // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                  try {
                        billAmt = getDriverWrapper().getEventFieldAsText("B+FD", "v", "m");

                        margin_Amt = new BigDecimal(billAmt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Bill amount--->" + margin_Amt);

                        }
                        // margin_Amount = margin_Amt.divide(hundred);
                        // String marginAmount = diff.format(margin_Amount);
                        curr = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");
                        financeAmt = getDriverWrapper().getEventFieldAsText("B+AF", "v", "m");
                        financeAmount = new BigDecimal(financeAmt);
                        // financeVal = margin_Amt.divide(financeAmount);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Finance amount--->" + financeAmount);
                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Finance amount" + e.getMessage());
                        }
                  }

                  if (!facid.equalsIgnoreCase("") && facid != null) {
                        String query = "select MARGIN,INTEREST,PENINTEREST,PLR,BASERATE from customermargin where facility='"
                                    + facid + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query resultcustomer margin " + query);
                        }
                        con = ConnectionMaster.getConnection();
                        dmsp = con.prepareStatement(query);
                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {

                              String magVal = dmsr.getString(1);
                              String interest = dmsr.getString(2);
                              String penalInt = dmsr.getString(3);
                              String plr = dmsr.getString(4);
                              String baseRate = dmsr.getString(5);
                              // Loggers.general().info(LOG,"magn--->" + magn);
                              BigDecimal magValue = new BigDecimal(magVal);
                              // Loggers.general().info(LOG,"Margin " + magn);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG,"Final Bill amount--->" + margin_Amt);
                                    Loggers.general().info(LOG,"Final Finance amount--->" + financeAmount);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total margin amount--->" + totalVal);
                              }
                              setPMARGIN(magVal);

                              setINTERDET(interest);
                              setTENO(penalInt);
                              setOURS(plr);
                              setLIBORAT(baseRate);

                              setMARAMT(totalVal + curr);
                              // setPRMARAMT(ks + "" + curr);
                              BigDecimal financeValue = financeAmount.add(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total value of finance and margin amount--->" + financeValue);
                              }
                              BigDecimal totalValue = margin_Amt.subtract(financeValue);
                              String finalVal = diff.format(totalValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Final value of finance and margin amount====>" + finalVal);
                              }
                              if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
                                    setBALAMT(finalVal + curr);
                              } else {
                                    finalVal = "0";
                                    setBALAMT(finalVal + curr);
                              }
                        }

                  } else if (facid.equalsIgnoreCase("") || facid == null) {

                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");

                        String magVal = getPMARGIN();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Margin percentage--->" + magVal);
                        }
                        if ((magVal != null || !magVal.equalsIgnoreCase("")) && magVal.length() > 0) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Facility id blank and Margin percentage====>" + magVal);
                              }
                              BigDecimal magValue = new BigDecimal(magVal);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG,"Final Bill amount--->" + margin_Amt);
                                    Loggers.general().info(LOG,"Final Finance amount--->" + financeAmount);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total margin amount--->" + totalVal);
                              }
                              setMARAMT(totalVal + curr);
                              // setPRMARAMT(ks + "" + curr);
                              BigDecimal financeValue = financeAmount.add(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total value of finance and margin amount--->" + financeValue);
                              }
                              BigDecimal totalValue = margin_Amt.subtract(financeValue);
                              String finalVal = diff.format(totalValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Final value of finance and margin amount====>" + finalVal);
                              }
                              if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
                                    setBALAMT(finalVal + curr);
                              } else {
                                    finalVal = "0";
                                    setBALAMT(finalVal + curr);
                              }
                        } else {
                              setPMARGIN("");
                              setMARAMT("");
                              setINTERDET("");
                              setTENO("");
                              setOURS("");
                              setLIBORAT("");
                        }

                  }

                  else {

                        setPMARGIN("");
                        setMARAMT("");
                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");
                        //// setPRMARAMT(0.0 + "" + curr);

                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in margin calcultion " + e.getMessage());
                  }
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

      public void onCIFMARGINFINEXPLCCREclayButton() {

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

            try {
                  String facid = getFACLTYID().trim();

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Facility id initially Finance export lc--->" + facid);

                  }
                  BigDecimal hundred = new BigDecimal(100);
                  DecimalFormat diff = new DecimalFormat("0.00");
                  diff.setMaximumFractionDigits(2);
                  // BigDecimal notionalRate = new BigDecimal(1);
                  BigDecimal financeAmount = new BigDecimal(0);
                  BigDecimal margin_Amt = new BigDecimal(0);
                  // String magn = "";
                  String billAmt = "";
                  String financeAmt = "";
                  String curr = "";
                  // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                  try {
                        billAmt = getDriverWrapper().getEventFieldAsText("B+FD", "v", "m");

                        margin_Amt = new BigDecimal(billAmt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Bill amount--->" + margin_Amt);

                        }
                        // margin_Amount = margin_Amt.divide(hundred);
                        // String marginAmount = diff.format(margin_Amount);
                        curr = getDriverWrapper().getEventFieldAsText("B+AF", "v", "c");
                        financeAmt = getDriverWrapper().getEventFieldAsText("B+AF", "v", "m");
                        financeAmount = new BigDecimal(financeAmt);
                        // financeVal = margin_Amt.divide(financeAmount);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Finance amount--->" + financeAmount);
                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Finance amount" + e.getMessage());
                        }
                  }

                  if (!facid.equalsIgnoreCase("") && facid != null) {
                        String query = "select MARGIN,INTEREST,PENINTEREST,PLR,BASERATE from customermargin where facility='"
                                    + facid + "'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query resultcustomer margin " + query);
                        }
                        con = ConnectionMaster.getConnection();
                        dmsp = con.prepareStatement(query);
                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {

                              String magVal = dmsr.getString(1);
                              String interest = dmsr.getString(2);
                              String penalInt = dmsr.getString(3);
                              String plr = dmsr.getString(4);
                              String baseRate = dmsr.getString(5);
                              // Loggers.general().info(LOG,"magn--->" + magn);
                              BigDecimal magValue = new BigDecimal(magVal);
                              // Loggers.general().info(LOG,"Margin " + magn);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG,"Final Bill amount--->" + margin_Amt);
                                    Loggers.general().info(LOG,"Final Finance amount--->" + financeAmount);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total margin amount--->" + totalVal);
                              }
                              setPMARGIN(magVal);

                              setINTERDET(interest);
                              setTENO(penalInt);
                              setOURS(plr);
                              setLIBORAT(baseRate);

                              setMARAMT(totalVal + curr);
                              // setPRMARAMT(ks + "" + curr);
                              BigDecimal financeValue = financeAmount.add(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total value of finance and margin amount--->" + financeValue);
                              }
                              BigDecimal totalValue = margin_Amt.subtract(financeValue);
                              String finalVal = diff.format(totalValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Final value of finance and margin amount====>" + finalVal);
                              }
                              if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
                                    setBALAMT(finalVal + curr);
                              } else {
                                    finalVal = "0";
                                    setBALAMT(finalVal + curr);
                              }
                        }

                  } else if (facid.equalsIgnoreCase("") || facid == null) {

                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");

                        String magVal = getPMARGIN();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Margin percentage--->" + magVal);
                        }
                        if ((magVal != null || !magVal.equalsIgnoreCase("")) && magVal.length() > 0) {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Facility id blank and Margin percentage====>" + magVal);
                              }
                              BigDecimal magValue = new BigDecimal(magVal);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Notional Rate--->" +
                                    // notionalRate);
                                    Loggers.general().info(LOG,"Final Bill amount--->" + margin_Amt);
                                    Loggers.general().info(LOG,"Final Finance amount--->" + financeAmount);
                              }
                              // BigDecimal marginAmtVal =
                              // margin_Amt.multiply(notionalRate);
                              BigDecimal marginAmtValue = magValue.multiply(margin_Amt);
                              BigDecimal marginValue = marginAmtValue.divide(hundred);
                              String totalVal = diff.format(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total margin amount--->" + totalVal);
                              }
                              setMARAMT(totalVal + curr);
                              // setPRMARAMT(ks + "" + curr);
                              BigDecimal financeValue = financeAmount.add(marginValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Total value of finance and margin amount--->" + financeValue);
                              }
                              BigDecimal totalValue = margin_Amt.subtract(financeValue);
                              String finalVal = diff.format(totalValue);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Final value of finance and margin amount====>" + finalVal);
                              }
                              if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
                                    setBALAMT(finalVal + curr);
                              } else {
                                    finalVal = "0";
                                    setBALAMT(finalVal + curr);
                              }
                        } else {
                              setPMARGIN("");
                              setMARAMT("");
                              setINTERDET("");
                              setTENO("");
                              setOURS("");
                              setLIBORAT("");
                        }

                  }

                  else {

                        setPMARGIN("");
                        setMARAMT("");
                        setINTERDET("");
                        setTENO("");
                        setOURS("");
                        setLIBORAT("");
                        //// setPRMARAMT(0.0 + "" + curr);

                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in margin calcultion " + e.getMessage());
                  }
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

      public void onNOSTROFINEXPLCADJclayButton() {
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
            String elcamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
            String curr = getDriverWrapper().getEventFieldAsText("AMPR", "v", "c");
            // Loggers.general().info(LOG,"elc amount value" + elcamt);
            double elcamt1 = Double.parseDouble(elcamt);
            String nostref = getNOSTMT();// nostro ref number from user
            // Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
            }
            double amount = 0.00;
            double amt202 = 0.00;
            double amtpres = 0.00;
            String Amountval = "";
            String msg = "";
            String MTref = "";
            String Ndat = "";

            String MTref103 = "";

            if (!nostref.equalsIgnoreCase("") || !nostref.isEmpty()) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select trim(mt940.ind_nos_amt),trim(mt103.message_data),trim(mt940.ind_nos_refno),trim(to_char(mt103.value_date,'DD/MM/YY')),trim(mt103.receiver) from ett_nostro_utility_match MAT, ett_nostro_utility_mt103 MT103, ett_nostro_utility_mt940 MT940 where mat.swift_match_reference = mt940.ind_nos_refno and mt103.reference_number = mat.swift_match_reference and mat.swift_match_reference='"
                                    + nostref + "'";
                        // Loggers.general().info(LOG,"query " + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"query " + query);
                        }
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                              // Loggers.general().info(LOG,"Entered 1st while");
                              String amt = rs.getString(1);
                              // Amountval=String.valueOf(amount);
                              setNOSTAMT(amt + " " + curr);// amount value for MT940
                              // Loggers.general().info(LOG,"the amount in MT940 " + amount);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"the amount in MT940 " + amount);
                              }
                              msg = rs.getString(2);
                              setINWMSG(msg);
                              // Loggers.general().info(LOG,"the inward message" + msg);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"the inward message" + msg);
                              }
                              MTref = rs.getString(3);
                              setNOSTRM(MTref);
                              // Loggers.general().info(LOG,"the MT940 reference No" + MTref);
                              Ndat = rs.getString(4);
                              // Loggers.general().info(LOG,"the MT103 value date " + Ndat);
                              setNOSTDAT(Ndat);
                              // Loggers.general().info(LOG," setNOSTDAT----------->" +
                              // getNOSTDAT());
                              MTref103 = rs.getString(5);
                              setNOSTACC(MTref103);
                              // Loggers.general().info(LOG,"setNOSTACC------------>" +
                              // getNOSTACC());
                        }

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,"catch in inward reference" +
                        // ee.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"catch in inward reference" + ee.getMessage());
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
            } else {
                  // Loggers.general().info(LOG,"the Nostro MT103/202 reference number is
                  // empty");
            }

      }

      public void onNOSTROFINEXPLCREPclayButton() {
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
            String elcamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
            String curr = getDriverWrapper().getEventFieldAsText("AMPR", "v", "c");
            // Loggers.general().info(LOG,"elc amount value" + elcamt);
            double elcamt1 = Double.parseDouble(elcamt);
            String nostref = getNOSTMT();// nostro ref number from user
            // Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
            }
            double amount = 0.00;
            double amt202 = 0.00;
            double amtpres = 0.00;
            String Amountval = "";
            String msg = "";
            String MTref = "";
            String Ndat = "";
            String MTref103 = "";
            if (!nostref.equalsIgnoreCase("") || !nostref.isEmpty()) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select trim(mt940.ind_nos_amt),trim(mt103.message_data),trim(mt940.ind_nos_refno),trim(to_char(mt103.value_date,'DD/MM/YY')),trim(mt103.receiver) from ett_nostro_utility_match MAT, ett_nostro_utility_mt103 MT103, ett_nostro_utility_mt940 MT940 where mat.swift_match_reference = mt940.ind_nos_refno and mt103.reference_number = mat.swift_match_reference and mat.swift_match_reference='"
                                    + nostref + "'";
                        // Loggers.general().info(LOG,"query " + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"query is" + query);
                        }
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                              // Loggers.general().info(LOG,"Entered 1st while");
                              String amt = rs.getString(1);
                              // Amountval=String.valueOf(amount);
                              setNOSTAMT(amt + " " + curr);// amount value for MT940
                              // Loggers.general().info(LOG,"the amount in MT940 " + amount);
                              msg = rs.getString(2);
                              setINWMSG(msg);
                              // Loggers.general().info(LOG,"the inward message" + msg);
                              MTref = rs.getString(3);
                              setNOSTRM(MTref);
                              // Loggers.general().info(LOG,"the MT940 reference No" + MTref);
                              Ndat = rs.getString(4);
                              // Loggers.general().info(LOG,"the MT103 value date " + Ndat);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"the MT103 value date " + Ndat);
                              }
                              setNOSTDAT(Ndat);
                              // Loggers.general().info(LOG," setNOSTDAT----------->" +
                              // getNOSTDAT());
                              MTref103 = rs.getString(5);
                              setNOSTACC(MTref103);
                              // Loggers.general().info(LOG,"setNOSTACC------------>" +
                              // getNOSTACC());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"setNOSTACC------------>" + getNOSTACC());
                              }
                        }

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,"catch in inward reference" +
                        // ee.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"catch in inward reference" + ee.getMessage());
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
            } else {
                  // Loggers.general().info(LOG,"the Nostro MT103/202 reference number is
                  // empty");
            }

      }

      public void onNOSTROFINOUTCOLAMDclayButton() {
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
            String elcamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
            String curr = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
            // Loggers.general().info(LOG,"elc amount value" + elcamt);
            double elcamt1 = Double.parseDouble(elcamt);
            String nostref = getNOSTMT();// nostro ref number from user
            // Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
            }
            double amount = 0.00;
            double amt202 = 0.00;
            double amtpres = 0.00;
            String msg = "";
            String MTref = "";
            String Ndat = "";

            String MTref103 = "";

            if (!nostref.equalsIgnoreCase("") || !nostref.isEmpty()) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select trim(mt940.ind_nos_amt),trim(mt103.message_data),trim(mt940.ind_nos_refno),trim(to_char(mt103.value_date,'DD/MM/YY')),trim(mt103.receiver) from ett_nostro_utility_match MAT, ett_nostro_utility_mt103 MT103, ett_nostro_utility_mt940 MT940 where mat.swift_match_reference = mt940.ind_nos_refno and mt103.reference_number = mat.swift_match_reference and mat.swift_match_reference='"
                                    + nostref + "'";
                        // Loggers.general().info(LOG,"query " + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"query is " + query);
                        }
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                              // Loggers.general().info(LOG,"Entered 1st while");
                              String amt = rs.getString(1);
                              // Amountval=String.valueOf(amount);
                              setNOSTAMT(amt + " " + curr);// amount value for MT940
                              // Loggers.general().info(LOG,"the amount in MT940 " + amount);
                              msg = rs.getString(2);
                              setINWMSG(msg);
                              // Loggers.general().info(LOG,"the inward message" + msg);
                              MTref = rs.getString(3);
                              setNOSTRM(MTref);
                              // Loggers.general().info(LOG,"the MT940 reference No" + MTref);
                              Ndat = rs.getString(4);
                              setNOSTDAT(Ndat);
                              // Loggers.general().info(LOG,"setNOSTDAT------------>" +
                              // getNOSTDAT());
                              MTref103 = rs.getString(5);
                              setNOSTACC(MTref103);
                              // Loggers.general().info(LOG,"setNOSTACC------------>" +
                              // getNOSTACC());

                        }

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,"catch in inward reference" +
                        // ee.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"catch in inward reference" + ee.getMessage());
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
            } else {
                  // Loggers.general().info(LOG,"the Nostro MT103/202 reference number is
                  // empty");
            }

      }

      public void onNOSTROFINEXPLCAMDclayButton() {
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
            String elcamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
            String curr = getDriverWrapper().getEventFieldAsText("AMPR", "v", "c");
            // Loggers.general().info(LOG,"elc amount value" + elcamt);
            double elcamt1 = Double.parseDouble(elcamt);
            String nostref = getNOSTMT();// nostro ref number from user
            // Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"the nostro ref no MT202" + nostref);
            }
            double amount = 0.00;
            double amt202 = 0.00;
            double amtpres = 0.00;
            String Amountval = "";
            String msg = "";
            String MTref = "";
            String Ndat = "";
            String MTref103 = "";

            if (!nostref.equalsIgnoreCase("") || !nostref.isEmpty()) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select trim(mt940.ind_nos_amt),trim(mt103.message_data),trim(mt940.ind_nos_refno),trim(to_char(mt103.value_date,'DD/MM/YY')),trim(mt103.receiver) from ett_nostro_utility_match MAT, ett_nostro_utility_mt103 MT103, ett_nostro_utility_mt940 MT940 where mat.swift_match_reference = mt940.ind_nos_refno and mt103.reference_number = mat.swift_match_reference and mat.swift_match_reference='"
                                    + nostref + "'";
                        // Loggers.general().info(LOG,"query " + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"query is " + query);
                        }
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                              // Loggers.general().info(LOG,"Entered 1st while");
                              String amt = rs.getString(1);
                              // Amountval=String.valueOf(amount);
                              setNOSTAMT(amt + " " + curr);// amount value for MT940
                              // Loggers.general().info(LOG,"the amount in MT940 " + amount);
                              msg = rs.getString(2);
                              setINWMSG(msg);
                              // Loggers.general().info(LOG,"the inward message" + msg);
                              MTref = rs.getString(3);
                              setNOSTRM(MTref);
                              // Loggers.general().info(LOG,"the MT940 reference No" + MTref);
                              Ndat = rs.getString(4);
                              // Loggers.general().info(LOG,"the MT103 value date " + Ndat);
                              DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                              // Loggers.general().info(LOG,"dateFormat " + dateFormat);
                              Date extendDate = dateFormat.parse(Ndat);
                              // Loggers.general().info(LOG,"extendDate " + extendDate);
                              String valDate = dateFormat.format(extendDate);
                              // //Loggers.general().info(LOG,"value date-----> " + valDate);
                              setNOSTDAT(valDate);
                              // Loggers.general().info(LOG," setNOSTDAT----------->" +
                              // getNOSTDAT());
                              MTref103 = rs.getString(5);
                              setNOSTACC(MTref103);
                              // Loggers.general().info(LOG,"setNOSTACC------------>" +
                              // getNOSTACC());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"setNOSTACC------------>" + getNOSTACC());
                              }
                        }

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,"catch in inward reference" +
                        // ee.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"catch in inward reference" + ee.getMessage());
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
            } else {
                  // Loggers.general().info(LOG,"the Nostro MT103/202 reference number is
                  // empty");
            }

      }
      // ------------------------------ interest subvention -
      // 25/07/2016---------------------------------------------------------//

      public void onSUBVENFINEXPLCADJclayButton()

      {

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
            // Loggers.general().info(LOG,"SUBVEN for Fin standalone create");
            String dmsstr = "null";
            String customer = "customer";
            String customera = "customerid";
            customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");// party
                                                                                                                  // name

            String interAdv = getDriverWrapper().getEventFieldAsText("B+OT", "s", "");
            interAdv = interAdv.trim();
            // //Loggers.general().info(LOG,"customer value " + customer);
            String masRef = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();// party
            // Loggers.general().info(LOG,"customera value for subvention " + customera);
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"customera value for subvention ELC Finance Adjust" + customera);
            }
            // String ELISUB ="";
            Double available = 0.00;
            String inr = "INR";
            String refund = "0.00";
            // String rofInter ="0.00";
            Double inter = 0.00;
            // String inter1 = "0.00";
            Double subvent = 0.00;
            Double interest = 0.00;
            Double rateinterest = 0.00;
            Double refuntAmt = 0.00;

            try {
                  // String dmsstr="";
                  // Loggers.general().info(LOG,"enter into subvention finexlc create");
                  con = ConnectionMaster.getConnection();
                  String dms = "select SUBELB from extcust where cust ='" + customera + "'";
                  dmsp = con.prepareStatement(dms);
                  Loggers.general().info(LOG,"Subvention eligible value Query===>" + dms);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Query" + dms);
                  }
                  dmsr = dmsp.executeQuery();
                  while (dmsr.next()) {
                        dmsstr = dmsr.getString(1);
                        if (dmsstr != null) {
                              dmsstr = dmsstr.trim();
                        }

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception Subvention eligible" + e.getMessage());
                  }
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
            // setELISUB(dmsstr)

            String elisub = getELISUB();
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"Initial elisub value in string ELC finance Adjust" + elisub);

                  Loggers.general().info(LOG,"Subvention eligible value Query ELC finance Adjust" + dmsstr);
            }
            String subv = getINTPERE();
            // Loggers.general().info(LOG,"Initial subv value in string " + subv);
            if (dmsstr.trim().equalsIgnoreCase("YES")
                        && (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub == null)) {
                  // setELISUB(dmsstr);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention eligible value Query value YES and Transaction level blank");
                  }
                  if (subv.isEmpty() || subv == null || subv.length() == 0) {
                        // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

                        subv = "0.00";
                        subvent = Double.valueOf(subv);

                  }
                  subvent = Double.valueOf(subv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
                  }

                  String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest amount to calculate===>" + inter1);
                  }
                  if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        inter1 = "0.00";
                        interest = Double.valueOf(inter1);

                  }
                  interest = Double.valueOf(inter1);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest value for subvention======>" + interest);
                  }
                  String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                  // Loggers.general().info(LOG,"rofInter value in if stmt" +
                  // rofInter.length());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
                  }
                  if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        rofInter = "0.00";
                        rateinterest = Double.valueOf(rofInter);

                  }
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
                  }
                  setROTINT(rofInter);
                  // Loggers.general().info(LOG,"rateinterest value for subvention " +
                  // rateinterest);
                  rateinterest = Double.valueOf(rofInter);
                  if (rateinterest > 0 || (rateinterest != 0.0)) {
                        Double avail = (interest * subvent) / rateinterest;

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Available amount for Subvention " + avail);
                        }
                        String present = String.format("%.2f", avail);
                        // Loggers.general().info(LOG,"present amount for Subvention " +
                        // present);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"present amount for Subvention final" + present);
                        }

                        setSUBVRBI(present + " " + inr);
                        setSUBVCRD(present + " " + inr);

                  }

                  try {

                        double subCredit = 0;
                        double subRBI = 0;
                        double subvenCredit_Double = 0;
                        double subvenRBI_Double = 0;
                        double tenorOld_val = 0;
                        double tenorNew_val = 0;
                        String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
                        String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
                        if (!tenorOld.equalsIgnoreCase("")) {
                              tenorOld_val = Double.valueOf(tenorOld);
                        }
                        if (!tenorNew.equalsIgnoreCase("")) {
                              tenorNew_val = Double.valueOf(tenorNew);
                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
                        }
                        String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
                        // Loggers.general().info(LOG,"Subvention finexlc adjust
                        // subventionCredit" + subventionCredit);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
                        }
                        if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
                              subvenCredit_Double = Double.valueOf(subventionCredit);
                        }
                        String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
                        // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
                        // + subventionRBI);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
                        }
                        if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
                              subvenRBI_Double = Double.valueOf(subventionRBI);
                        }
                        con = ConnectionMaster.getConnection();
                        String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                                    + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
                        // Loggers.general().info(LOG,"Subvention Query===>" + dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention Query===>" + dms);
                        }
                        dmsp = con.prepareStatement(dms);

                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {
                              subCredit = dmsr.getDouble(1);
                              // Loggers.general().info(LOG,"Subvention credit for create" +
                              // subCredit);
                              subRBI = dmsr.getDouble(2);
                              double totalSubvCredit = subCredit - subvenCredit_Double;
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
                              }
                              double totalSubvRBI = subRBI - subvenRBI_Double;
                              DecimalFormat diff = new DecimalFormat("0.00");
                              diff.setMaximumFractionDigits(2);
                              String FinalSubvCredit = diff.format(totalSubvCredit);
                              // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
                              // + FinalSubvCredit);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
                              }
                              if (tenorOld_val > tenorNew_val) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                                    }
                                    if (totalSubvRBI > 0) {
                                          setSUBVDEB(FinalSubvCredit + " " + inr);
                                          setSUBVPAY(FinalSubvCredit + " " + inr);
                                    } else {
                                          setSUBVDEB(0 + " " + inr);
                                          setSUBVPAY(0 + " " + inr);
                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                                    }

                                    setSUBVPAY(available + " " + inr);
                                    setSUBVDEB(available + " " + inr);
                              }

                        }

                        else {
                              setSUBVPAY(available + " " + inr);
                              setSUBVDEB(available + " " + inr);
                        }

                        if (tenorOld_val < tenorNew_val) {

                              try {
                                    String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                                    double amanedValue = Double.valueOf(amanedVal);
                                    double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                                    double subVal = subvent / 100;

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                                    }

                                    double totalVal = amanedValue * tenorVal * subVal;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                                    }
                                    if (totalVal > 0) {
                                          String totalValue = String.format("%.2f", totalVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                                          setSUBVRBI(totalValue + " " + inr);
                                          setSUBVCRD(totalValue + " " + inr);
                                    } else {
                                          setSUBVRBI(0 + " " + inr);
                                          setSUBVCRD(0 + " " + inr);
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
                              }

                        }

                  } catch (Exception e) {
                        Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

            } else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("YES")) {
                  // setELISUB(dmsstr);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention eligible value Query value YES and Transaction YES");
                  }
                  if (subv.isEmpty() || subv == null || subv.length() == 0) {
                        // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

                        subv = "0.00";
                        subvent = Double.valueOf(subv);

                  }
                  subvent = Double.valueOf(subv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
                  }

                  String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                  // Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
                  }
                  if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        inter1 = "0.00";
                        interest = Double.valueOf(inter1);

                  }
                  interest = Double.valueOf(inter1);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest value for subvention " + interest);
                  }
                  String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                  // Loggers.general().info(LOG,"rofInter value in if stmt" +
                  // rofInter.length());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
                  }
                  if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        rofInter = "0.00";
                        rateinterest = Double.valueOf(rofInter);

                  }
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
                  }
                  setROTINT(rofInter);
                  // Loggers.general().info(LOG,"rateinterest value for subvention " +
                  // rateinterest);
                  rateinterest = Double.valueOf(rofInter);
                  if (rateinterest > 0 || (rateinterest != 0.0)) {
                        Double avail = (interest * subvent) / rateinterest;

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Available amount for Subvention " + avail);
                        }
                        String present = String.format("%.2f", avail);
                        // Loggers.general().info(LOG,"present amount for Subvention " +
                        // present);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"present amount for Subvention " + present);
                        }
                        setSUBVRBI(present + " " + inr);
                        setSUBVCRD(present + " " + inr);
                  }

                  try {

                        double subCredit = 0;
                        double subRBI = 0;
                        double subvenCredit_Double = 0;
                        double subvenRBI_Double = 0;
                        double tenorOld_val = 0;
                        double tenorNew_val = 0;
                        String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
                        String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
                        if (!tenorOld.equalsIgnoreCase("")) {
                              tenorOld_val = Double.valueOf(tenorOld);
                        }
                        if (!tenorNew.equalsIgnoreCase("")) {
                              tenorNew_val = Double.valueOf(tenorNew);
                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
                        }
                        String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
                        // Loggers.general().info(LOG,"Subvention finexlc adjust
                        // subventionCredit" + subventionCredit);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
                        }
                        if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
                              subvenCredit_Double = Double.valueOf(subventionCredit);
                        }
                        String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
                        // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
                        // + subventionRBI);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
                        }
                        if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
                              subvenRBI_Double = Double.valueOf(subventionRBI);
                        }
                        con = ConnectionMaster.getConnection();
                        String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                                    + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
                        // Loggers.general().info(LOG,"Subvention Query===>" + dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention Query===>" + dms);
                        }
                        dmsp = con.prepareStatement(dms);

                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {
                              subCredit = dmsr.getDouble(1);
                              // Loggers.general().info(LOG,"Subvention credit for create" +
                              // subCredit);
                              subRBI = dmsr.getDouble(2);
                              double totalSubvCredit = subCredit - subvenCredit_Double;
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
                              }
                              double totalSubvRBI = subRBI - subvenRBI_Double;
                              DecimalFormat diff = new DecimalFormat("0.00");
                              diff.setMaximumFractionDigits(2);
                              String FinalSubvCredit = diff.format(totalSubvCredit);
                              // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
                              // + FinalSubvCredit);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
                              }
                              if (tenorOld_val > tenorNew_val) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                                    }
                                    if (totalSubvRBI > 0) {
                                          setSUBVDEB(FinalSubvCredit + " " + inr);
                                          setSUBVPAY(FinalSubvCredit + " " + inr);
                                    } else {
                                          setSUBVDEB(0 + " " + inr);
                                          setSUBVPAY(0 + " " + inr);
                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                                    }

                                    setSUBVPAY(available + " " + inr);
                                    setSUBVDEB(available + " " + inr);
                              }

                        }

                        else {
                              setSUBVPAY(available + " " + inr);
                              setSUBVDEB(available + " " + inr);
                        }

                        if (tenorOld_val < tenorNew_val) {

                              try {
                                    String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                                    double amanedValue = Double.valueOf(amanedVal);
                                    double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                                    double subVal = subvent / 100;

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                                    }

                                    double totalVal = amanedValue * tenorVal * subVal;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                                    }
                                    if (totalVal > 0) {
                                          String totalValue = String.format("%.2f", totalVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                                          setSUBVRBI(totalValue + " " + inr);
                                          setSUBVCRD(totalValue + " " + inr);
                                    } else {
                                          setSUBVRBI(0 + " " + inr);
                                          setSUBVCRD(0 + " " + inr);
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
                              }

                        }

                  } catch (Exception e) {
                        Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

            } else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("YES")) {
                  // setELISUB(dmsstr);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention eligible value Query value NO and Transaction YES");
                  }
                  if (subv.isEmpty() || subv == null || subv.length() == 0) {
                        // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

                        subv = "0.00";
                        subvent = Double.valueOf(subv);

                  }
                  subvent = Double.valueOf(subv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
                  }

                  String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                  // Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
                  }
                  if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        inter1 = "0.00";
                        interest = Double.valueOf(inter1);

                  }
                  interest = Double.valueOf(inter1);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest value for subvention " + interest);
                  }
                  String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                  // Loggers.general().info(LOG,"rofInter value in if stmt" +
                  // rofInter.length());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
                  }
                  if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        rofInter = "0.00";
                        rateinterest = Double.valueOf(rofInter);

                  }
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
                  }
                  setROTINT(rofInter);
                  // Loggers.general().info(LOG,"rateinterest value for subvention " +
                  // rateinterest);
                  rateinterest = Double.valueOf(rofInter);
                  if (rateinterest > 0 || (rateinterest != 0.0)) {
                        Double avail = (interest * subvent) / rateinterest;

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Available amount for Subvention " + avail);
                        }
                        String present = String.format("%.2f", avail);
                        // Loggers.general().info(LOG,"present amount for Subvention " +
                        // present);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"present amount for Subvention " + present);
                        }
                        setSUBVRBI(present + " " + inr);
                        setSUBVCRD(present + " " + inr);
                  }

                  try {

                        double subCredit = 0;
                        double subRBI = 0;
                        double subvenCredit_Double = 0;
                        double subvenRBI_Double = 0;
                        double tenorOld_val = 0;
                        double tenorNew_val = 0;
                        String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
                        String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
                        if (!tenorOld.equalsIgnoreCase("")) {
                              tenorOld_val = Double.valueOf(tenorOld);
                        }
                        if (!tenorNew.equalsIgnoreCase("")) {
                              tenorNew_val = Double.valueOf(tenorNew);
                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
                        }
                        String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
                        // Loggers.general().info(LOG,"Subvention finexlc adjust
                        // subventionCredit" + subventionCredit);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
                        }
                        if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
                              subvenCredit_Double = Double.valueOf(subventionCredit);
                        }
                        String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
                        // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
                        // + subventionRBI);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
                        }
                        if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
                              subvenRBI_Double = Double.valueOf(subventionRBI);
                        }
                        con = ConnectionMaster.getConnection();
                        String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                                    + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
                        // Loggers.general().info(LOG,"Subvention Query===>" + dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention Query===>" + dms);
                        }
                        dmsp = con.prepareStatement(dms);

                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {
                              subCredit = dmsr.getDouble(1);
                              // Loggers.general().info(LOG,"Subvention credit for create" +
                              // subCredit);
                              subRBI = dmsr.getDouble(2);
                              double totalSubvCredit = subCredit - subvenCredit_Double;
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
                              }
                              double totalSubvRBI = subRBI - subvenRBI_Double;
                              DecimalFormat diff = new DecimalFormat("0.00");
                              diff.setMaximumFractionDigits(2);
                              String FinalSubvCredit = diff.format(totalSubvCredit);
                              // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
                              // + FinalSubvCredit);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
                              }
                              if (tenorOld_val > tenorNew_val) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                                    }
                                    if (totalSubvRBI > 0) {
                                          setSUBVDEB(FinalSubvCredit + " " + inr);
                                          setSUBVPAY(FinalSubvCredit + " " + inr);
                                    } else {
                                          setSUBVDEB(0 + " " + inr);
                                          setSUBVPAY(0 + " " + inr);
                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                                    }

                                    setSUBVPAY(available + " " + inr);
                                    setSUBVDEB(available + " " + inr);
                              }

                        }

                        else {
                              setSUBVPAY(available + " " + inr);
                              setSUBVDEB(available + " " + inr);
                        }

                        if (tenorOld_val < tenorNew_val) {

                              try {
                                    String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                                    double amanedValue = Double.valueOf(amanedVal);
                                    double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                                    double subVal = subvent / 100;

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                                    }

                                    double totalVal = amanedValue * tenorVal * subVal;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                                    }
                                    if (totalVal > 0) {
                                          String totalValue = String.format("%.2f", totalVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                                          setSUBVRBI(totalValue + " " + inr);
                                          setSUBVCRD(totalValue + " " + inr);
                                    } else {
                                          setSUBVRBI(0 + " " + inr);
                                          setSUBVCRD(0 + " " + inr);
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
                              }

                        }

                  } catch (Exception e) {
                        Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

            else if (elisub.trim().equalsIgnoreCase("YES")
                        && (dmsstr.trim().equalsIgnoreCase("") || dmsstr.isEmpty() || dmsstr == null)) {
                  // setELISUB(dmsstr);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention eligible Transaction level YES and query level blank");
                  }
                  if (subv.isEmpty() || subv == null || subv.length() == 0) {
                        // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

                        subv = "0.00";
                        subvent = Double.valueOf(subv);

                  }
                  subvent = Double.valueOf(subv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
                  }

                  String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                  // Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
                  }
                  if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        inter1 = "0.00";
                        interest = Double.valueOf(inter1);

                  }
                  interest = Double.valueOf(inter1);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest value for subvention " + interest);
                  }
                  String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                  // Loggers.general().info(LOG,"rofInter value in if stmt" +
                  // rofInter.length());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
                  }
                  if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        rofInter = "0.00";
                        rateinterest = Double.valueOf(rofInter);

                  }
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
                  }
                  setROTINT(rofInter);
                  // Loggers.general().info(LOG,"rateinterest value for subvention " +
                  // rateinterest);
                  rateinterest = Double.valueOf(rofInter);
                  if (rateinterest > 0 || (rateinterest != 0.0)) {
                        Double avail = (interest * subvent) / rateinterest;

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Available amount for Subvention " + avail);
                        }
                        String present = String.format("%.2f", avail);
                        // Loggers.general().info(LOG,"present amount for Subvention " +
                        // present);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"present amount for Subvention " + present);
                        }
                        setSUBVRBI(present + " " + inr);
                        setSUBVCRD(present + " " + inr);
                  }

                  try {

                        double subCredit = 0;
                        double subRBI = 0;
                        double subvenCredit_Double = 0;
                        double subvenRBI_Double = 0;
                        double tenorOld_val = 0;
                        double tenorNew_val = 0;
                        String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
                        String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
                        if (!tenorOld.equalsIgnoreCase("")) {
                              tenorOld_val = Double.valueOf(tenorOld);
                        }
                        if (!tenorNew.equalsIgnoreCase("")) {
                              tenorNew_val = Double.valueOf(tenorNew);
                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
                        }
                        String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
                        // Loggers.general().info(LOG,"Subvention finexlc adjust
                        // subventionCredit" + subventionCredit);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
                        }
                        if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
                              subvenCredit_Double = Double.valueOf(subventionCredit);
                        }
                        String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
                        // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
                        // + subventionRBI);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
                        }
                        if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
                              subvenRBI_Double = Double.valueOf(subventionRBI);
                        }
                        con = ConnectionMaster.getConnection();
                        String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                                    + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
                        // Loggers.general().info(LOG,"Subvention Query===>" + dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention Query===>" + dms);
                        }
                        dmsp = con.prepareStatement(dms);

                        dmsr = dmsp.executeQuery();
                        if (dmsr.next()) {
                              subCredit = dmsr.getDouble(1);
                              // Loggers.general().info(LOG,"Subvention credit for create" +
                              // subCredit);
                              subRBI = dmsr.getDouble(2);
                              double totalSubvCredit = subCredit - subvenCredit_Double;
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
                              }
                              double totalSubvRBI = subRBI - subvenRBI_Double;
                              DecimalFormat diff = new DecimalFormat("0.00");
                              diff.setMaximumFractionDigits(2);
                              String FinalSubvCredit = diff.format(totalSubvCredit);
                              // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
                              // + FinalSubvCredit);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
                              }
                              if (tenorOld_val > tenorNew_val) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                                    }
                                    if (totalSubvRBI > 0) {
                                          setSUBVDEB(FinalSubvCredit + " " + inr);
                                          setSUBVPAY(FinalSubvCredit + " " + inr);
                                    } else {
                                          setSUBVDEB(0 + " " + inr);
                                          setSUBVPAY(0 + " " + inr);
                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                                    }

                                    setSUBVPAY(available + " " + inr);
                                    setSUBVDEB(available + " " + inr);
                              }

                        }

                        else {
                              setSUBVPAY(available + " " + inr);
                              setSUBVDEB(available + " " + inr);
                        }

                        if (tenorOld_val < tenorNew_val) {

                              try {
                                    String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                                    double amanedValue = Double.valueOf(amanedVal);
                                    double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                                    double subVal = subvent / 100;

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                                    }

                                    double totalVal = amanedValue * tenorVal * subVal;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                                    }
                                    if (totalVal > 0) {
                                          String totalValue = String.format("%.2f", totalVal);
                                          Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                                          setSUBVRBI(totalValue + " " + inr);
                                          setSUBVCRD(totalValue + " " + inr);
                                    } else {
                                          setSUBVRBI(0 + " " + inr);
                                          setSUBVCRD(0 + " " + inr);
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
                              }

                        }

                  } catch (Exception e) {
                        Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

            else if (dmsstr.trim().equalsIgnoreCase("NO")
                        && (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub == null)) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention eligible query level NO and transaction level blank");
                  }

                  setSUBVRBI(available + " " + inr);
                  setSUBVCRD(available + " " + inr);
                  setSUBVPAY(available + " " + inr);
                  setSUBVDEB(available + " " + inr);
                  String sub = getINTPERE();
                  sub = "";
                  setINTPERE(sub);
            }

            else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("NO")) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention eligible Transaction level NO and query level blank");
                  }
                  setSUBVRBI(available + " " + inr);
                  setSUBVCRD(available + " " + inr);
                  setSUBVPAY(available + " " + inr);
                  setSUBVDEB(available + " " + inr);
                  String sub = getINTPERE();
                  sub = "";
                  setINTPERE(sub);
            }

            else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("NO")) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention eligible query level YES and transaction level blank");
                  }
                  setSUBVRBI(available + " " + inr);
                  setSUBVCRD(available + " " + inr);
                  setSUBVPAY(available + " " + inr);
                  setSUBVDEB(available + " " + inr);
                  String sub = getINTPERE();
                  sub = "";
                  setINTPERE(sub);
            } else if (dmsstr.trim().equalsIgnoreCase("") || dmsstr.trim().isEmpty() && elisub.trim().equalsIgnoreCase("")
                        || elisub.trim().isEmpty()) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention eligible Transaction level blank and query level blank");
                  }
                  setSUBVRBI(available + " " + inr);
                  setSUBVCRD(available + " " + inr);
                  setSUBVPAY(available + " " + inr);
                  setSUBVDEB(available + " " + inr);
                  String sub = getINTPERE();
                  sub = "";
                  setINTPERE(sub);
            }

            String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
            if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
                        || subproCode.equalsIgnoreCase("HCA")) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Subvention subproduct Code===>" + subproCode);

                  }

            } else {

                  setELISUB("");
                  setINTPERE("");
                  setECSECE("");
                  setSUBVRBI("0.00 INR");
                  setSUBVCRD("0.00 INR");
                  setSUBVPAY("0.00 INR");
                  setSUBVDEB("0.00 INR");

            }

      }

      public void onSUBVENFINEXPLCAMDclayButton() {

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
            try {
                  String dmsstr = "null";
                  String customer = "customer";
                  String customera = "customerid";
                  customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");// party
                                                                                                                        // name

                  String interAdv = getDriverWrapper().getEventFieldAsText("B+OT", "s", "");
                  interAdv = interAdv.trim();
                  // //Loggers.general().info(LOG,"customer value " + customer);
                  String masRef = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();// party
                  // Loggers.general().info(LOG,"customera value for subvention " +
                  // customera);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"customera value for subvention ELC Finance Amend" + customera);
                  }
                  // String ELISUB ="";
                  Double available = 0.00;
                  String inr = "INR";
                  String refund = "0.00";
                  // String rofInter ="0.00";
                  Double inter = 0.00;
                  // String inter1 = "0.00";
                  Double subvent = 0.00;
                  Double interest = 0.00;
                  Double rateinterest = 0.00;
                  Double refuntAmt = 0.00;

                  try {
                        // String dmsstr="";
                        // Loggers.general().info(LOG,"enter into subvention finexlc create");
                        con = ConnectionMaster.getConnection();
                        String dms = "select SUBELB from extcust where cust ='" + customera + "'";
                        dmsp = con.prepareStatement(dms);
                        Loggers.general().info(LOG,"Subvention eligible value Query===>" + dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query" + dms);
                        }
                        dmsr = dmsp.executeQuery();
                        while (dmsr.next()) {
                              dmsstr = dmsr.getString(1);
                              if (dmsstr != null) {
                                    dmsstr = dmsstr.trim();
                              }

                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Subvention eligible" + e.getMessage());
                        }
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
                  // setELISUB(dmsstr)

                  String elisub = getELISUB();
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial elisub value in string ELC finance amened" + elisub);

                        Loggers.general().info(LOG,"Subvention eligible value Query ELC finance amened" + dmsstr);
                  }
                  String subv = getINTPERE();
                  // Loggers.general().info(LOG,"Initial subv value in string " + subv);
                  if (dmsstr.trim().equalsIgnoreCase("YES")
                              && (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub == null)) {
                        // setELISUB(dmsstr);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention eligible value Query value YES and Transaction level blank");
                        }
                        if (subv.isEmpty() || subv == null || subv.length() == 0) {
                              // Loggers.general().info(LOG,"subv value in if stmt" +
                              // subv.length());

                              subv = "0.00";
                              subvent = Double.valueOf(subv);

                        }
                        subvent = Double.valueOf(subv);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
                        }

                        String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest amount to calculate===>" + inter1);
                        }
                        if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                              // Loggers.general().info(LOG,"inter1 value in if stmt" +
                              // inter1.length());
                              inter1 = "0.00";
                              interest = Double.valueOf(inter1);

                        }
                        interest = Double.valueOf(inter1);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest value for subvention======>" + interest);
                        }
                        String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
                        }
                        if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                              // Loggers.general().info(LOG,"rofInter value in if stmt" +
                              // rofInter.length());
                              rofInter = "0.00";
                              rateinterest = Double.valueOf(rofInter);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
                        }
                        setROTINT(rofInter);
                        // Loggers.general().info(LOG,"rateinterest value for subvention " +
                        // rateinterest);
                        rateinterest = Double.valueOf(rofInter);
                        if (rateinterest > 0 || (rateinterest != 0.0)) {
                              Double avail = (interest * subvent) / rateinterest;

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Available amount for Subvention " + avail);
                              }
                              String present = String.format("%.2f", avail);
                              // Loggers.general().info(LOG,"present amount for Subvention " +
                              // present);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"present amount for Subvention final" + present);
                              }

                              setSUBVRBI(present + " " + inr);
                              setSUBVCRD(present + " " + inr);

                        }

                        try {

                              double subCredit = 0;
                              double subRBI = 0;
                              double subvenCredit_Double = 0;
                              double subvenRBI_Double = 0;
                              double tenorOld_val = 0;
                              double tenorNew_val = 0;
                              String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
                              String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
                              if (!tenorOld.equalsIgnoreCase("")) {
                                    tenorOld_val = Double.valueOf(tenorOld);
                              }
                              if (!tenorNew.equalsIgnoreCase("")) {
                                    tenorNew_val = Double.valueOf(tenorNew);
                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
                              }
                              String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
                              // Loggers.general().info(LOG,"Subvention finexlc adjust
                              // subventionCredit" + subventionCredit);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
                              }
                              if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
                                    subvenCredit_Double = Double.valueOf(subventionCredit);
                              }
                              String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
                              // Loggers.general().info(LOG,"Subvention finexlc adjust
                              // subventionRBI"
                              // + subventionRBI);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
                              }
                              if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
                                    subvenRBI_Double = Double.valueOf(subventionRBI);
                              }
                              con = ConnectionMaster.getConnection();
                              String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                                          + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
                              // Loggers.general().info(LOG,"Subvention Query===>" + dms);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention Query===>" + dms);
                              }
                              dmsp = con.prepareStatement(dms);

                              dmsr = dmsp.executeQuery();
                              if (dmsr.next()) {
                                    subCredit = dmsr.getDouble(1);
                                    // Loggers.general().info(LOG,"Subvention credit for create" +
                                    // subCredit);
                                    subRBI = dmsr.getDouble(2);
                                    double totalSubvCredit = subCredit - subvenCredit_Double;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
                                    }
                                    double totalSubvRBI = subRBI - subvenRBI_Double;
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String FinalSubvCredit = diff.format(totalSubvCredit);
                                    // Loggers.general().info(LOG,"Subvention credit and RBI
                                    // value--->"
                                    // + FinalSubvCredit);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
                                    }
                                    if (tenorOld_val > tenorNew_val) {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Subvention credit and RBI value old value greater--->" + tenorOld_val);
                                          }
                                          if (totalSubvRBI > 0) {
                                                setSUBVDEB(FinalSubvCredit + " " + inr);
                                                setSUBVPAY(FinalSubvCredit + " " + inr);
                                          } else {
                                                setSUBVDEB(0 + " " + inr);
                                                setSUBVPAY(0 + " " + inr);
                                          }
                                    } else {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                                          }

                                          setSUBVPAY(available + " " + inr);
                                          setSUBVDEB(available + " " + inr);
                                    }

                              }

                              else {
                                    setSUBVPAY(available + " " + inr);
                                    setSUBVDEB(available + " " + inr);
                              }

                              if (tenorOld_val < tenorNew_val) {

                                    try {
                                          String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                                          double amanedValue = Double.valueOf(amanedVal);
                                          double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                                          double subVal = subvent / 100;

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                                          }

                                          double totalVal = amanedValue * tenorVal * subVal;
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                                          }
                                          if (totalVal > 0) {
                                                String totalValue = String.format("%.2f", totalVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                                                setSUBVRBI(totalValue + " " + inr);
                                                setSUBVCRD(totalValue + " " + inr);
                                          } else {
                                                setSUBVRBI(0 + " " + inr);
                                                setSUBVCRD(0 + " " + inr);
                                          }

                                    } catch (Exception e) {
                                          Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
                                    }

                              }

                        } catch (Exception e) {
                              Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

                  } else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("YES")) {
                        // setELISUB(dmsstr);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention eligible value Query value YES and Transaction YES");
                        }
                        if (subv.isEmpty() || subv == null || subv.length() == 0) {
                              // Loggers.general().info(LOG,"subv value in if stmt" +
                              // subv.length());

                              subv = "0.00";
                              subvent = Double.valueOf(subv);

                        }
                        subvent = Double.valueOf(subv);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
                        }

                        String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
                        }
                        if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                              // Loggers.general().info(LOG,"inter1 value in if stmt" +
                              // inter1.length());
                              inter1 = "0.00";
                              interest = Double.valueOf(inter1);

                        }
                        interest = Double.valueOf(inter1);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest value for subvention " + interest);
                        }
                        String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
                        }
                        if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                              // Loggers.general().info(LOG,"rofInter value in if stmt" +
                              // rofInter.length());
                              rofInter = "0.00";
                              rateinterest = Double.valueOf(rofInter);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
                        }
                        setROTINT(rofInter);
                        // Loggers.general().info(LOG,"rateinterest value for subvention " +
                        // rateinterest);
                        rateinterest = Double.valueOf(rofInter);
                        if (rateinterest > 0 || (rateinterest != 0.0)) {
                              Double avail = (interest * subvent) / rateinterest;

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Available amount for Subvention " + avail);
                              }
                              String present = String.format("%.2f", avail);
                              // Loggers.general().info(LOG,"present amount for Subvention " +
                              // present);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"present amount for Subvention " + present);
                              }
                              setSUBVRBI(present + " " + inr);
                              setSUBVCRD(present + " " + inr);
                        }

                        try {

                              double subCredit = 0;
                              double subRBI = 0;
                              double subvenCredit_Double = 0;
                              double subvenRBI_Double = 0;
                              double tenorOld_val = 0;
                              double tenorNew_val = 0;
                              String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
                              String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
                              if (!tenorOld.equalsIgnoreCase("")) {
                                    tenorOld_val = Double.valueOf(tenorOld);
                              }
                              if (!tenorNew.equalsIgnoreCase("")) {
                                    tenorNew_val = Double.valueOf(tenorNew);
                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
                              }
                              String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
                              // Loggers.general().info(LOG,"Subvention finexlc adjust
                              // subventionCredit" + subventionCredit);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
                              }
                              if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
                                    subvenCredit_Double = Double.valueOf(subventionCredit);
                              }
                              String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
                              // Loggers.general().info(LOG,"Subvention finexlc adjust
                              // subventionRBI"
                              // + subventionRBI);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
                              }
                              if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
                                    subvenRBI_Double = Double.valueOf(subventionRBI);
                              }
                              con = ConnectionMaster.getConnection();
                              String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                                          + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
                              // Loggers.general().info(LOG,"Subvention Query===>" + dms);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention Query===>" + dms);
                              }
                              dmsp = con.prepareStatement(dms);

                              dmsr = dmsp.executeQuery();
                              if (dmsr.next()) {
                                    subCredit = dmsr.getDouble(1);
                                    // Loggers.general().info(LOG,"Subvention credit for create" +
                                    // subCredit);
                                    subRBI = dmsr.getDouble(2);
                                    double totalSubvCredit = subCredit - subvenCredit_Double;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
                                    }
                                    double totalSubvRBI = subRBI - subvenRBI_Double;
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String FinalSubvCredit = diff.format(totalSubvCredit);
                                    // Loggers.general().info(LOG,"Subvention credit and RBI
                                    // value--->"
                                    // + FinalSubvCredit);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
                                    }
                                    if (tenorOld_val > tenorNew_val) {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Subvention credit and RBI value old value greater--->" + tenorOld_val);
                                          }
                                          if (totalSubvRBI > 0) {
                                                setSUBVDEB(FinalSubvCredit + " " + inr);
                                                setSUBVPAY(FinalSubvCredit + " " + inr);
                                          } else {
                                                setSUBVDEB(0 + " " + inr);
                                                setSUBVPAY(0 + " " + inr);
                                          }
                                    } else {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                                          }

                                          setSUBVPAY(available + " " + inr);
                                          setSUBVDEB(available + " " + inr);
                                    }

                              }

                              else {
                                    setSUBVPAY(available + " " + inr);
                                    setSUBVDEB(available + " " + inr);
                              }

                              if (tenorOld_val < tenorNew_val) {

                                    try {
                                          String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                                          double amanedValue = Double.valueOf(amanedVal);
                                          double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                                          double subVal = subvent / 100;

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                                          }

                                          double totalVal = amanedValue * tenorVal * subVal;
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                                          }
                                          if (totalVal > 0) {
                                                String totalValue = String.format("%.2f", totalVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                                                setSUBVRBI(totalValue + " " + inr);
                                                setSUBVCRD(totalValue + " " + inr);
                                          } else {
                                                setSUBVRBI(0 + " " + inr);
                                                setSUBVCRD(0 + " " + inr);
                                          }

                                    } catch (Exception e) {
                                          Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
                                    }

                              }

                        } catch (Exception e) {
                              Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

                  } else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("YES")) {
                        // setELISUB(dmsstr);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention eligible value Query value NO and Transaction YES");
                        }
                        if (subv.isEmpty() || subv == null || subv.length() == 0) {
                              // Loggers.general().info(LOG,"subv value in if stmt" +
                              // subv.length());

                              subv = "0.00";
                              subvent = Double.valueOf(subv);

                        }
                        subvent = Double.valueOf(subv);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
                        }

                        String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
                        }
                        if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                              // Loggers.general().info(LOG,"inter1 value in if stmt" +
                              // inter1.length());
                              inter1 = "0.00";
                              interest = Double.valueOf(inter1);

                        }
                        interest = Double.valueOf(inter1);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest value for subvention " + interest);
                        }
                        String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
                        }
                        if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                              // Loggers.general().info(LOG,"rofInter value in if stmt" +
                              // rofInter.length());
                              rofInter = "0.00";
                              rateinterest = Double.valueOf(rofInter);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
                        }
                        setROTINT(rofInter);
                        // Loggers.general().info(LOG,"rateinterest value for subvention " +
                        // rateinterest);
                        rateinterest = Double.valueOf(rofInter);
                        if (rateinterest > 0 || (rateinterest != 0.0)) {
                              Double avail = (interest * subvent) / rateinterest;

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Available amount for Subvention " + avail);
                              }
                              String present = String.format("%.2f", avail);
                              // Loggers.general().info(LOG,"present amount for Subvention " +
                              // present);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"present amount for Subvention " + present);
                              }
                              setSUBVRBI(present + " " + inr);
                              setSUBVCRD(present + " " + inr);
                        }

                        try {

                              double subCredit = 0;
                              double subRBI = 0;
                              double subvenCredit_Double = 0;
                              double subvenRBI_Double = 0;
                              double tenorOld_val = 0;
                              double tenorNew_val = 0;
                              String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
                              String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
                              if (!tenorOld.equalsIgnoreCase("")) {
                                    tenorOld_val = Double.valueOf(tenorOld);
                              }
                              if (!tenorNew.equalsIgnoreCase("")) {
                                    tenorNew_val = Double.valueOf(tenorNew);
                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
                              }
                              String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
                              // Loggers.general().info(LOG,"Subvention finexlc adjust
                              // subventionCredit" + subventionCredit);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
                              }
                              if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
                                    subvenCredit_Double = Double.valueOf(subventionCredit);
                              }
                              String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
                              // Loggers.general().info(LOG,"Subvention finexlc adjust
                              // subventionRBI"
                              // + subventionRBI);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
                              }
                              if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
                                    subvenRBI_Double = Double.valueOf(subventionRBI);
                              }
                              con = ConnectionMaster.getConnection();
                              String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                                          + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
                              // Loggers.general().info(LOG,"Subvention Query===>" + dms);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention Query===>" + dms);
                              }
                              dmsp = con.prepareStatement(dms);

                              dmsr = dmsp.executeQuery();
                              if (dmsr.next()) {
                                    subCredit = dmsr.getDouble(1);
                                    // Loggers.general().info(LOG,"Subvention credit for create" +
                                    // subCredit);
                                    subRBI = dmsr.getDouble(2);
                                    double totalSubvCredit = subCredit - subvenCredit_Double;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
                                    }
                                    double totalSubvRBI = subRBI - subvenRBI_Double;
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String FinalSubvCredit = diff.format(totalSubvCredit);
                                    // Loggers.general().info(LOG,"Subvention credit and RBI
                                    // value--->"
                                    // + FinalSubvCredit);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
                                    }
                                    if (tenorOld_val > tenorNew_val) {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Subvention credit and RBI value old value greater--->" + tenorOld_val);
                                          }
                                          if (totalSubvRBI > 0) {
                                                setSUBVDEB(FinalSubvCredit + " " + inr);
                                                setSUBVPAY(FinalSubvCredit + " " + inr);
                                          } else {
                                                setSUBVDEB(0 + " " + inr);
                                                setSUBVPAY(0 + " " + inr);
                                          }
                                    } else {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                                          }

                                          setSUBVPAY(available + " " + inr);
                                          setSUBVDEB(available + " " + inr);
                                    }

                              }

                              else {
                                    setSUBVPAY(available + " " + inr);
                                    setSUBVDEB(available + " " + inr);
                              }

                              if (tenorOld_val < tenorNew_val) {

                                    try {
                                          String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                                          double amanedValue = Double.valueOf(amanedVal);
                                          double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                                          double subVal = subvent / 100;

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                                          }

                                          double totalVal = amanedValue * tenorVal * subVal;
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                                          }
                                          if (totalVal > 0) {
                                                String totalValue = String.format("%.2f", totalVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                                                setSUBVRBI(totalValue + " " + inr);
                                                setSUBVCRD(totalValue + " " + inr);
                                          } else {
                                                setSUBVRBI(0 + " " + inr);
                                                setSUBVCRD(0 + " " + inr);
                                          }

                                    } catch (Exception e) {
                                          Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
                                    }

                              }

                        } catch (Exception e) {
                              Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

                  else if (elisub.trim().equalsIgnoreCase("YES")
                              && (dmsstr.trim().equalsIgnoreCase("") || dmsstr.isEmpty() || dmsstr == null)) {
                        // setELISUB(dmsstr);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention eligible Transaction level YES and query level blank");
                        }
                        if (subv.isEmpty() || subv == null || subv.length() == 0) {
                              // Loggers.general().info(LOG,"subv value in if stmt" +
                              // subv.length());

                              subv = "0.00";
                              subvent = Double.valueOf(subv);

                        }
                        subvent = Double.valueOf(subv);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
                        }

                        String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
                        }
                        if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                              // Loggers.general().info(LOG,"inter1 value in if stmt" +
                              // inter1.length());
                              inter1 = "0.00";
                              interest = Double.valueOf(inter1);

                        }
                        interest = Double.valueOf(inter1);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest value for subvention " + interest);
                        }
                        String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
                        }
                        if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                              // Loggers.general().info(LOG,"rofInter value in if stmt" +
                              // rofInter.length());
                              rofInter = "0.00";
                              rateinterest = Double.valueOf(rofInter);

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
                        }
                        setROTINT(rofInter);
                        // Loggers.general().info(LOG,"rateinterest value for subvention " +
                        // rateinterest);
                        rateinterest = Double.valueOf(rofInter);
                        if (rateinterest > 0 || (rateinterest != 0.0)) {
                              Double avail = (interest * subvent) / rateinterest;

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Available amount for Subvention " + avail);
                              }
                              String present = String.format("%.2f", avail);
                              // Loggers.general().info(LOG,"present amount for Subvention " +
                              // present);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"present amount for Subvention " + present);
                              }
                              setSUBVRBI(present + " " + inr);
                              setSUBVCRD(present + " " + inr);
                        }

                        try {

                              double subCredit = 0;
                              double subRBI = 0;
                              double subvenCredit_Double = 0;
                              double subvenRBI_Double = 0;
                              double tenorOld_val = 0;
                              double tenorNew_val = 0;
                              String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
                              String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
                              if (!tenorOld.equalsIgnoreCase("")) {
                                    tenorOld_val = Double.valueOf(tenorOld);
                              }
                              if (!tenorNew.equalsIgnoreCase("")) {
                                    tenorNew_val = Double.valueOf(tenorNew);
                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
                              }
                              String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
                              // Loggers.general().info(LOG,"Subvention finexlc adjust
                              // subventionCredit" + subventionCredit);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
                              }
                              if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
                                    subvenCredit_Double = Double.valueOf(subventionCredit);
                              }
                              String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
                              // Loggers.general().info(LOG,"Subvention finexlc adjust
                              // subventionRBI"
                              // + subventionRBI);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
                              }
                              if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
                                    subvenRBI_Double = Double.valueOf(subventionRBI);
                              }
                              con = ConnectionMaster.getConnection();
                              String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                                          + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
                              // Loggers.general().info(LOG,"Subvention Query===>" + dms);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Subvention Query===>" + dms);
                              }
                              dmsp = con.prepareStatement(dms);

                              dmsr = dmsp.executeQuery();
                              if (dmsr.next()) {
                                    subCredit = dmsr.getDouble(1);
                                    // Loggers.general().info(LOG,"Subvention credit for create" +
                                    // subCredit);
                                    subRBI = dmsr.getDouble(2);
                                    double totalSubvCredit = subCredit - subvenCredit_Double;
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
                                    }
                                    double totalSubvRBI = subRBI - subvenRBI_Double;
                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String FinalSubvCredit = diff.format(totalSubvCredit);
                                    // Loggers.general().info(LOG,"Subvention credit and RBI
                                    // value--->"
                                    // + FinalSubvCredit);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
                                    }
                                    if (tenorOld_val > tenorNew_val) {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Subvention credit and RBI value old value greater--->" + tenorOld_val);
                                          }
                                          if (totalSubvRBI > 0) {
                                                setSUBVDEB(FinalSubvCredit + " " + inr);
                                                setSUBVPAY(FinalSubvCredit + " " + inr);
                                          } else {
                                                setSUBVDEB(0 + " " + inr);
                                                setSUBVPAY(0 + " " + inr);
                                          }
                                    } else {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                                          }

                                          setSUBVPAY(available + " " + inr);
                                          setSUBVDEB(available + " " + inr);
                                    }

                              }

                              else {
                                    setSUBVPAY(available + " " + inr);
                                    setSUBVDEB(available + " " + inr);
                              }

                              if (tenorOld_val < tenorNew_val) {

                                    try {
                                          String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                                          double amanedValue = Double.valueOf(amanedVal);
                                          double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                                          double subVal = subvent / 100;

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                                          }

                                          double totalVal = amanedValue * tenorVal * subVal;
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                                          }
                                          if (totalVal > 0) {
                                                String totalValue = String.format("%.2f", totalVal);
                                                Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                                                setSUBVRBI(totalValue + " " + inr);
                                                setSUBVCRD(totalValue + " " + inr);
                                          } else {
                                                setSUBVRBI(0 + " " + inr);
                                                setSUBVCRD(0 + " " + inr);
                                          }

                                    } catch (Exception e) {
                                          Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
                                    }

                              }

                        } catch (Exception e) {
                              Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

                  else if (dmsstr.trim().equalsIgnoreCase("NO")
                              && (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub == null)) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention eligible query level NO and transaction level blank");
                        }

                        setSUBVRBI(available + " " + inr);
                        setSUBVCRD(available + " " + inr);
                        setSUBVPAY(available + " " + inr);
                        setSUBVDEB(available + " " + inr);
                        String sub = getINTPERE();
                        sub = "";
                        setINTPERE(sub);
                  }

                  else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("NO")) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention eligible Transaction level NO and query level blank");
                        }
                        setSUBVRBI(available + " " + inr);
                        setSUBVCRD(available + " " + inr);
                        setSUBVPAY(available + " " + inr);
                        setSUBVDEB(available + " " + inr);
                        String sub = getINTPERE();
                        sub = "";
                        setINTPERE(sub);
                  }

                  else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("NO")) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention eligible query level YES and transaction level blank");
                        }
                        setSUBVRBI(available + " " + inr);
                        setSUBVCRD(available + " " + inr);
                        setSUBVPAY(available + " " + inr);
                        setSUBVDEB(available + " " + inr);
                        String sub = getINTPERE();
                        sub = "";
                        setINTPERE(sub);
                  } else if (dmsstr.trim().equalsIgnoreCase("")
                              || dmsstr.trim().isEmpty() && elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty()) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention eligible Transaction level blank and query level blank");
                        }
                        setSUBVRBI(available + " " + inr);
                        setSUBVCRD(available + " " + inr);
                        setSUBVPAY(available + " " + inr);
                        setSUBVDEB(available + " " + inr);
                        String sub = getINTPERE();
                        sub = "";
                        setINTPERE(sub);
                  }

                  String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                  if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
                              || subproCode.equalsIgnoreCase("HCA")) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention subproduct Code===>" + subproCode);

                        }

                  } else {

                        setELISUB("");
                        setINTPERE("");
                        setECSECE("");
                        setSUBVRBI("0.00 INR");
                        setSUBVCRD("0.00 INR");
                        setSUBVPAY("0.00 INR");
                        setSUBVDEB("0.00 INR");

                  }

            } catch (Exception e) {
                  Loggers.general().info(LOG,"onSUBVENFinExplcCreLayButton is called");
            }
      }

      public void onSUBVENFINEXPLCCREclayButton() {

            try {
                  // Loggers.general().info(LOG,"onSUBVENFinExplcCreLayButton is called");
                  SubventionCalculation();
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"exception in onSUBVENFinExplcCreLayButton " +
                  // e.getMessage());

            }

      }

      public void onSUBVENFINEXPLCREPclayButton() {
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
            try {

                  // Loggers.general().info(LOG,"onSUBVENFINEXLCREPAYButton for Fin Export LC
                  // repay");
                  Double available = 0.00;
                  Double principalRepayed = 0.0;
                  String inr = "INR";
                  String Principcl_Repaid_Amt = "";
                  int subvenPercentage = 0;
                  int tenor = 0;
                  String value_date = "";
                  String start_Date = "";
                  String maturity_date = "";
                  String subvention = getINTPERE();
                  Principcl_Repaid_Amt = getDriverWrapper().getEventFieldAsText("FPR", "v", "m");
                  // Loggers.general().info(LOG,"value of Principal Repayed Amount ");
                  value_date = getDriverWrapper().getEventFieldAsText("RPD", "d", "");
                  // Loggers.general().info(LOG,"value date is " + value_date);
                  start_Date = getDriverWrapper().getEventFieldAsText("B+SD", "d", "");
                  // Loggers.general().info(LOG,"start Date is " + start_Date);
                  maturity_date = getDriverWrapper().getEventFieldAsText("B+MD", "d", "");
                  // Loggers.general().info(LOG,"Maturity date is " + maturity_date);
                  Double subvent = Double.valueOf(subvention);

                  String interAdv = getDriverWrapper().getEventFieldAsText("B+OT", "s", "");
                  interAdv = interAdv.trim();
                  // Loggers.general().info(LOG,"Interest type" + interAdv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Interest type" + interAdv);
                  }

                  Boolean abnormal_recovery = getPOSHCL_Name();

                  if (interAdv.equalsIgnoreCase("Interest in advance - standard")) {
                        // Loggers.general().info(LOG,"Repayment type is Interest in advance -
                        // standard");

                        // case if recovery is not abnormal, scenario is normal advance
                        // repayment
                        if (!abnormal_recovery) {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Date maturityDate = format.parse(maturity_date);
                              Date valueDate = format.parse(value_date);
                              Date startDate = format.parse(start_Date);
                              // case if recovery is not abnormal, scenario is normal
                              // advance repayment before maturity date,refund is present
                              if (valueDate.compareTo(maturityDate) == -1) {
                                    // Loggers.general().info(LOG,"value Date is less than maturity
                                    // date");
                                    long diff = (maturityDate.getTime() - valueDate.getTime());
                                    tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                    // Loggers.general().info(LOG,"tenor val is " + tenor);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"tenor val is " + tenor);
                                    }
                              }
                              // case if recovery is not abnormal, scenario is normal
                              // advance repayment after maturity date ,no refund
                              else {
                                    tenor = 0;
                              }
                        }
                        // case if recovery is abnormal, scenario is normal advance
                        // repayment
                        else {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Date maturityDate = format.parse(maturity_date);
                              Date startDate = format.parse(start_Date);
                              // case if recovery is abnormal, scenario is normal advance
                              // repayment after maturity date,refund is for normal for
                              // full tenor
                              // Loggers.general().info(LOG,"value Date is less than maturity
                              // date");
                              long diff = (maturityDate.getTime() - startDate.getTime());
                              tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                              // Loggers.general().info(LOG,"tenor val is " + tenor);
                        }
                        // Calculation of Refund Amount
                        subvenPercentage = Integer.parseInt(getINTPERE().trim());
                        // Loggers.general().info(LOG,"value of interest percentage " +
                        // subvenPercentage);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"value of interest percentage " + subvenPercentage);
                        }
                        principalRepayed = Double.valueOf(Principcl_Repaid_Amt);

                        String Subvention_Amount = SimpleIntrestCalculation(principalRepayed, subvenPercentage, tenor);

                        setSUBVPAY(Subvention_Amount + " " + inr);
                        setSUBVDEB(Subvention_Amount + " " + inr);

                  } else if (interAdv.equalsIgnoreCase("Interest in arrears - standard") && (!abnormal_recovery)) {

                        // Loggers.general().info(LOG,"enter into Interest in arrears -
                        // standard");
                        // Tenor Calculation
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        Date maturityDate = format.parse(maturity_date);
                        Date valueDate = format.parse(value_date);
                        Date startDate = format.parse(start_Date);
                        if (valueDate.compareTo(maturityDate) == 1) {
                              // Loggers.general().info(LOG,"value Date id Greator than Maturiy
                              // Date ");
                              long diff = maturityDate.getTime() - startDate.getTime();
                              tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                              // Loggers.general().info(LOG,"tenor val is if" + tenor);
                        } else {
                              long diff = valueDate.getTime() - startDate.getTime();
                              tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                              // Loggers.general().info(LOG,"tenor val is else" + tenor);
                        }

                        // Calculation of Refund Amount
                        subvenPercentage = Integer.parseInt(getINTPERE().trim());
                        // Loggers.general().info(LOG,"value of interest percentage " +
                        // subvenPercentage);
                        principalRepayed = Double.valueOf(Principcl_Repaid_Amt);

                        String Subvention_Amount = SimpleIntrestCalculation(principalRepayed, subvenPercentage, tenor);

                        setSUBVRBI(Subvention_Amount + " " + inr);
                        setSUBVCRD(Subvention_Amount + " " + inr);

                  }
                  // case if the interest is monthly consolidation and abnormal
                  // recovery
                  else {
                        setSUBVRBI("0.00 INR");
                        setSUBVCRD("0.00 INR");
                        setSUBVPAY("0.00 INR");
                        setSUBVDEB("0.00 INR");
                        // Loggers.general().info(LOG,"Else Excecuted since it is not Intrest in
                        // advance or intrest in arrears");
                  }

                  String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                  if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
                              || subproCode.equalsIgnoreCase("HCA")) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Subvention subproduct Code===>" + subproCode);

                        }

                  } else {

                        setELISUB("");
                        setINTPERE("");
                        setECSECE("");
                        setSUBVRBI("0.00 INR");
                        setSUBVCRD("0.00 INR");
                        setSUBVPAY("0.00 INR");
                        setSUBVDEB("0.00 INR");

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception in onSUBVENFINEXLCREPAYButton " +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in onSUBVENFINEXLCREPAYButton " + e.getMessage());
                  }
            }

      }

      public void onSUBVENFSAADJclayButton()

      {
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
            // PCR //PTP
            String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
            // Loggers.general().info(LOG,"SubproCode-----> " + subproCode);
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"SubproCode-----> " + subproCode);
            }
            if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
                        || subproCode.equalsIgnoreCase("HCA")) {

                  // Loggers.general().info(LOG,"SUBVEN for Fin standalone create");
                  String dmsstr = "null";
                  String customer = "customer";
                  String customera = "customerid";
                  customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");// party
                                                                                                                        // name
                  // //Loggers.general().info(LOG,"customer value " + customer);
                  customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();// party
                  // Loggers.general().info(LOG,"customera value for subvention " +
                  // customera);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"customera value for subvention " + customera);
                  }
                  // String ELISUB ="";
                  Double available = 0.00;
                  String inr = "INR";
                  String refund = "0.00";
                  // String rofInter ="0.00";
                  Double inter = 0.00;
                  // String inter1 = "0.00";
                  Double subvent = 0.00;
                  Double interest = 0.00;
                  Double rateinterest = 0.00;
                  Double refuntAmt = 0.00;

                  try {
                        // String dmsstr="";
                        // Loggers.general().info(LOG,"enter into subvention finexlc create");
                        con = ConnectionMaster.getConnection();
                        String dms = "select SUBELB from extcust where cust ='" + customera + "'";
                        dmsp = con.prepareStatement(dms);
                        // Loggers.general().info(LOG,"Query" + dms);
                        dmsr = dmsp.executeQuery();
                        while (dmsr.next()) {
                              dmsstr = dmsr.getString(1);
                              if (dmsstr != null) {
                                    dmsstr = dmsstr.trim();
                              }

                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,e.getMessage());
                        }
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
                  // setELISUB(dmsstr)

                  String elisub = getELISUB();
                  // Loggers.general().info(LOG,"Initial elisub value in string " + elisub);
                  String subv = getINTPERE();
                  // Loggers.general().info(LOG,"Initial subv value in string " + subv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Initial subv value in string " + subv);
                  }
                  if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty()
                              || elisub == null) {
                        setELISUB(dmsstr);
                        // Loggers.general().info(LOG,"enter into Subvention Eligible is YES
                        // eligible value set as YES");
                        // Loggers.general().info(LOG," subv value in string " + subv);
                        if (subv.isEmpty() || subv == null || subv.length() == 0) {
                              // Loggers.general().info(LOG,"subv value in if stmt" +
                              // subv.length());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"subv value in if stmt" + subv.length());
                              }
                              subv = "0.00";
                              subvent = Double.valueOf(subv);

                        }
                        subvent = Double.valueOf(subv);
                        // Loggers.general().info(LOG,"subvent value " + subvent);

                        String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
                        }
                        if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                              // Loggers.general().info(LOG,"inter1 value in if stmt" +
                              // inter1.length());
                              inter1 = "0.00";
                              interest = Double.valueOf(inter1);

                        }
                        interest = Double.valueOf(inter1);
                        // Loggers.general().info(LOG,"interest value for subvention " +
                        // interest);

                        String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
                        }
                        if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                              // Loggers.general().info(LOG,"rofInter value in if stmt" +
                              // rofInter.length());
                              rofInter = "0.00";
                              rateinterest = Double.valueOf(rofInter);

                        }
                        setROTINT(rofInter);
                        // Loggers.general().info(LOG,"rateinterest value for subvention " +
                        // rateinterest);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"rateinterest value for subvention " + rateinterest);
                        }
                        rateinterest = Double.valueOf(rofInter);
                        if (rateinterest > 0 || (rateinterest != 0.0)) {
                              Double avail = (interest * subvent) / rateinterest;
                              String present = String.format("%.2f", avail);
                              // Loggers.general().info(LOG,"present amount for Subvention " +
                              // present);
                              setSUBVRBI(present + " " + inr);
                              setSUBVCRD(present + " " + inr);
                        }

                  } else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("YES")) {
                        // Loggers.general().info(LOG,"enter into Subvention Eligible is YES and
                        // eligible also YES------>");
                        // Loggers.general().info(LOG," subv value in string " + subv);
                        if (subv.isEmpty() || subv == null || subv.length() == 0) {
                              // Loggers.general().info(LOG,"subv value in if stmt" +
                              // subv.length());
                              subv = "0.00";
                              subvent = Double.valueOf(subv);

                        }
                        subvent = Double.valueOf(subv);
                        // Loggers.general().info(LOG,"subvent value " + subvent);

                        String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
                        }
                        if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                              // Loggers.general().info(LOG,"inter1 value in if stmt" +
                              // inter1.length());
                              inter1 = "0.00";
                              interest = Double.valueOf(inter1);

                        }
                        interest = Double.valueOf(inter1);
                        // Loggers.general().info(LOG,"interest value for subvention " +
                        // interest);

                        String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
                        }
                        if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                              // Loggers.general().info(LOG,"rofInter value in if stmt" +
                              // rofInter.length());
                              rofInter = "0.00";
                              rateinterest = Double.valueOf(rofInter);

                        }
                        setROTINT(rofInter);
                        // Loggers.general().info(LOG,"rateinterest value for subvention " +
                        // rateinterest);
                        rateinterest = Double.valueOf(rofInter);
                        if (rateinterest > 0 || (rateinterest != 0.0)) {
                              Double avail = (interest * subvent) / rateinterest;
                              String present = String.format("%.2f", avail);
                              // Loggers.general().info(LOG,"present amount for Subvention " +
                              // present);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"present amount for Subvention " + present);
                              }
                              setSUBVRBI(present + " " + inr);
                              setSUBVCRD(present + " " + inr);
                        }

                  } else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("YES")) {
                        // Loggers.general().info(LOG,"enter into Subvention Eligible is NO and
                        // eligible is YES");
                        // Loggers.general().info(LOG," subv value in string " + subv);
                        if (subv.isEmpty() || subv == null || subv.length() == 0) {
                              // Loggers.general().info(LOG,"subv value in if stmt" +
                              // subv.length());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"subv value in if stmt" + subv.length());
                              }
                              subv = "0.00";
                              subvent = Double.valueOf(subv);

                        }
                        subvent = Double.valueOf(subv);
                        // Loggers.general().info(LOG,"subvent value " + subvent);

                        String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
                        }
                        if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                              // Loggers.general().info(LOG,"inter1 value in if stmt" +
                              // inter1.length());
                              inter1 = "0.00";
                              interest = Double.valueOf(inter1);

                        }
                        interest = Double.valueOf(inter1);
                        // Loggers.general().info(LOG,"interest value for subvention " +
                        // interest);

                        String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
                        }
                        if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                              // Loggers.general().info(LOG,"rofInter value in if stmt" +
                              // rofInter.length());
                              rofInter = "0.00";
                              rateinterest = Double.valueOf(rofInter);

                        }
                        setROTINT(rofInter);
                        // Loggers.general().info(LOG,"rateinterest value for subvention " +
                        // rateinterest);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"rateinterest value for subvention " + rateinterest);
                        }
                        rateinterest = Double.valueOf(rofInter);
                        if (rateinterest > 0 || (rateinterest != 0.0)) {
                              Double avail = (interest * subvent) / rateinterest;
                              String present = String.format("%.2f", avail);
                              // Loggers.general().info(LOG,"present amount for Subvention " +
                              // present);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"present amount for Subvention " + present);
                              }
                              setSUBVRBI(present + " " + inr);
                              setSUBVCRD(present + " " + inr);
                        }

                  }

                  else if (elisub.trim().equalsIgnoreCase("YES") && dmsstr.trim().equalsIgnoreCase("") || dmsstr.isEmpty()
                              || dmsstr == null) {
                        // Loggers.general().info(LOG,"Subvention eligible blank and elisub
                        // YES");
                        setELISUB(dmsstr);
                        // Loggers.general().info(LOG,"enter into Subvention Eligible is YES and
                        // eligible also YES------>");
                        // Loggers.general().info(LOG," subv value in string " + subv);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG," subv value in string " + subv);
                        }
                        if (subv.isEmpty() || subv == null || subv.length() == 0) {
                              // Loggers.general().info(LOG,"subv value in if stmt" +
                              // subv.length());
                              subv = "0.00";
                              subvent = Double.valueOf(subv);

                        }
                        subvent = Double.valueOf(subv);
                        // Loggers.general().info(LOG,"subvent value " + subvent);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"subvent value " + subvent);
                        }

                        String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
                        // Loggers.general().info(LOG,"inter1 value in if stmt" +
                        // inter1.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
                        }

                        if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
                              // Loggers.general().info(LOG,"inter1 value in if stmt" +
                              // inter1.length());
                              inter1 = "0.00";
                              interest = Double.valueOf(inter1);

                        }
                        interest = Double.valueOf(inter1);
                        // Loggers.general().info(LOG,"interest value for subvention " +
                        // interest);

                        String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
                        // Loggers.general().info(LOG,"rofInter value in if stmt" +
                        // rofInter.length());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
                        }
                        if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
                              // Loggers.general().info(LOG,"rofInter value in if stmt" +
                              // rofInter.length());
                              rofInter = "0.00";
                              rateinterest = Double.valueOf(rofInter);

                        }
                        // Loggers.general().info(LOG,"rateinterest value for subvention " +
                        // rateinterest);
                        rateinterest = Double.valueOf(rofInter);
                        if (rateinterest > 0 || (rateinterest != 0.0)) {
                              Double avail = (interest * subvent) / rateinterest;
                              String present = String.format("%.2f", avail);
                              // Loggers.general().info(LOG,"present amount for Subvention " +
                              // present);
                              setSUBVRBI(present + " " + inr);
                              setSUBVCRD(present + " " + inr);
                        }

                  }

                  else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("")
                              || elisub.trim().isEmpty() || elisub == null) {
                        // Loggers.general().info(LOG,"Subvention eligible is No and elisub is
                        // blank");
                        setSUBVRBI(available + " " + inr);
                        setSUBVCRD(available + " " + inr);
                        String sub = getINTPERE();
                        sub = "";
                        setINTPERE(sub);
                  }

                  else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("NO")) {
                        // Loggers.general().info(LOG,"Subvention eligible is No and elisub also
                        // NO");
                        setSUBVRBI(available + " " + inr);
                        setSUBVCRD(available + " " + inr);
                        String sub = getINTPERE();
                        sub = "";
                        setINTPERE(sub);
                  }

                  else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("NO")) {
                        // Loggers.general().info(LOG,"Subvention eligible yes and elisub also
                        // no");
                        setSUBVRBI(available + " " + inr);
                        setSUBVCRD(available + " " + inr);
                        String sub = getINTPERE();
                        sub = "";
                        setINTPERE(sub);
                  } else if (dmsstr.trim().equalsIgnoreCase("")
                              || dmsstr.trim().isEmpty() && elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty()) {
                        // Loggers.general().info(LOG,"Subvention eligible is No and elisub is
                        // blank");
                        setSUBVRBI(available + " " + inr);
                        setSUBVCRD(available + " " + inr);
                        String sub = getINTPERE();
                        sub = "";
                        setINTPERE(sub);
                  }
            } else {
                  // Loggers.general().info(LOG,"SubproCode is not PCR-----> " + subproCode);
                  setELISUB("");
                  setROTINT("");
                  setSUBVRBI("");
                  setSUBVCRD("");
                  setINTPERE("");
            }
      }

      public void onSUBVENFSAAMDclayButton() {

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
            String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
            Loggers.general().info(LOG,"SubproCode-----> " + subproCode);
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"SubproCode-----> " + subproCode);
            }
            if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
                        || subproCode.equalsIgnoreCase("HCA")) {

                   Loggers.general().info(LOG,"SUBVEN for Fin standalone create");
                  String dmsstr = "null";
                  String customer = "customer";
                  String customera = "customerid";
                  customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");// party
                                                                                                                        // name
                  Loggers.general().info(LOG,"customer value " + customer);
                  customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();// party
                   Loggers.general().info(LOG,"customera value for subvention " +   customera);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"customera value for subvention " + customera);
                  }
                  // String ELISUB ="";
                  Double available = 0.00;
                  String inr = "INR";
                  String refund = "0.00";
                  // String rofInter ="0.00";
                  Double inter = 0.00;
                  // String inter1 = "0.00";
                  Double subvent = 0.00;
                  Double interest = 0.00;
                  Double rateinterest = 0.00;
                  Double refuntAmt = 0.00;

                  try {
                        // String dmsstr="";
                        // Loggers.general().info(LOG,"enter into subvention finexlc create");
                        con = ConnectionMaster.getConnection();
                        String dms = "select SUBELB from extcust where cust ='" + customera + "'";
                        dmsp = con.prepareStatement(dms);
                        Loggers.general().info(LOG,"Query" + dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Query" + dms);
                        }
                        dmsr = dmsp.executeQuery();
                        while (dmsr.next()) {
                              dmsstr = dmsr.getString(1);
                              if (dmsstr != null) {
                                    dmsstr = dmsstr.trim();
                              }

                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,e.getMessage());
                        }
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
                  // setELISUB(dmsstr)

                  String elisub = getELISUB();
                   Loggers.general().info(LOG,"Initial elisub value in string " + elisub);
                  String subv = getINTPERE();
                   Loggers.general().info(LOG,"Initial subv value in string " + subv);
 if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial subv value in string " + subv);
}
if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty()
          || elisub == null) {
    setELISUB(dmsstr);
    Loggers.general().info(LOG,"enter into Subvention Eligible is YES eligible value set as YES");
    Loggers.general().info(LOG," subv value in string " + subv);
    if (subv.isEmpty() || subv == null || subv.length() == 0) {
           Loggers.general().info(LOG,"subv value in if stmt" + subv.length());
          subv = "0.00";
          subvent = Double.valueOf(subv);

    }
    subvent = Double.valueOf(subv);
     Loggers.general().info(LOG,"subvent value " + subvent);

    String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
    //String inter1 = getDriverWrapper().getEventFieldAsText("B+AT", "v", "m");
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
    }
    if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
           Loggers.general().info(LOG,"inter1 value in if stmt" +
          inter1.length());
          inter1 = "0.00";
          interest = Double.valueOf(inter1);

    }
    interest = Double.valueOf(inter1);
    Loggers.general().info(LOG,"interest value for subvention " +
     interest);

    String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
    Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
    }
    if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
           Loggers.general().info(LOG,"rofInter value in if stmt" +    rofInter.length());
          rofInter = "0.00";
          rateinterest = Double.valueOf(rofInter);

    }
    setROTINT(rofInter);
    // Loggers.general().info(LOG,"rateinterest value for subvention " +
    // rateinterest);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"rateinterest value for subvention " + rateinterest);
    }
    rateinterest = Double.valueOf(rofInter);
    if (rateinterest > 0 || (rateinterest != 0.0)) {
          Double avail = (interest * subvent) / rateinterest;
          String present = String.format("%.2f", avail);
           Loggers.general().info(LOG,"present amount for Subvention " +     present);
          setSUBVRBI(present + " " + inr);
          setSUBVCRD(present + " " + inr);
    }

} else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("YES")) {
     Loggers.general().info(LOG,"enter into Subvention Eligible is YES and   eligible also YES------>");
    // Loggers.general().info(LOG," subv value in string " + subv);
    if (subv.isEmpty() || subv == null || subv.length() == 0) {
          // Loggers.general().info(LOG,"subv value in if stmt" +
          // subv.length());
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"subv value in if stmt" + subv.length());
          }
          subv = "0.00";
          subvent = Double.valueOf(subv);

    }
    subvent = Double.valueOf(subv);
    // Loggers.general().info(LOG,"subvent value " + subvent);

    String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
    }
    if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
          Loggers.general().info(LOG,"inter1 value in if stmt" +      inter1.length());
          inter1 = "0.00";
          interest = Double.valueOf(inter1);

    }
    interest = Double.valueOf(inter1);
    // Loggers.general().info(LOG,"interest value for subvention " +
    // interest);

    String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
    }
    if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
          // Loggers.general().info(LOG,"rofInter value in if stmt" +
          // rofInter.length());
          rofInter = "0.00";
          rateinterest = Double.valueOf(rofInter);

    }
    setROTINT(rofInter);
    // Loggers.general().info(LOG,"rateinterest value for subvention " +
    // rateinterest);
    rateinterest = Double.valueOf(rofInter);
    if (rateinterest > 0 || (rateinterest != 0.0)) {
          Double avail = (interest * subvent) / rateinterest;
          String present = String.format("%.2f", avail);
          // Loggers.general().info(LOG,"present amount for Subvention " +
          // present);
          setSUBVRBI(present + " " + inr);
          setSUBVCRD(present + " " + inr);
    }

} else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("YES")) {
     Loggers.general().info(LOG,"enter into Subvention Eligible is NO andeligible is YES");
    // Loggers.general().info(LOG," subv value in string " + subv);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG," subv value in string " + subv);
    }
    if (subv.isEmpty() || subv == null || subv.length() == 0) {
          // Loggers.general().info(LOG,"subv value in if stmt" +
          // subv.length());
          subv = "0.00";
          subvent = Double.valueOf(subv);

    }
    subvent = Double.valueOf(subv);
    // Loggers.general().info(LOG,"subvent value " + subvent);

    String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
    }
    if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
          // Loggers.general().info(LOG,"inter1 value in if stmt" +
          // inter1.length());
          inter1 = "0.00";
          interest = Double.valueOf(inter1);

    }
    interest = Double.valueOf(inter1);
    // Loggers.general().info(LOG,"interest value for subvention " +
    // interest);

    String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
    }
    if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
           Loggers.general().info(LOG,"rofInter value in if stmt===>" +      rofInter.length());
          rofInter = "0.00";
          rateinterest = Double.valueOf(rofInter);

    }
    setROTINT(rofInter);
    // Loggers.general().info(LOG,"rateinterest value for subvention " +
    // rateinterest);
    rateinterest = Double.valueOf(rofInter);
    if (rateinterest > 0 || (rateinterest != 0.0)) {
          Double avail = (interest * subvent) / rateinterest;
          String present = String.format("%.2f", avail);
          // Loggers.general().info(LOG,"present amount for Subvention " +
          // present);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"present amount for Subvention " + present);
          }
          setSUBVRBI(present + " " + inr);
          setSUBVCRD(present + " " + inr);
    }

}

else if (elisub.trim().equalsIgnoreCase("YES") && dmsstr.trim().equalsIgnoreCase("") || dmsstr.isEmpty()
          || dmsstr == null) {
    Loggers.general().info(LOG,"Subvention eligible blank and elisub YES");
    setELISUB(dmsstr);
    // Loggers.general().info(LOG,"enter into Subvention Eligible is YES and
    // eligible also YES------>");
    // Loggers.general().info(LOG," subv value in string " + subv);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG," subv value in string " + subv);
    }
    if (subv.isEmpty() || subv == null || subv.length() == 0) {
          // Loggers.general().info(LOG,"subv value in if stmt" +
          // subv.length());
          subv = "0.00";
          subvent = Double.valueOf(subv);

    }
    subvent = Double.valueOf(subv);
     Loggers.general().info(LOG,"subvent value===> " + subvent);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"subvent value " + subvent);
    }
    String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
          // Loggers.general().info(LOG,"inter1 value in if stmt" +
          // inter1.length());
          inter1 = "0.00";
          interest = Double.valueOf(inter1);

    }
    interest = Double.valueOf(inter1);
     Loggers.general().info(LOG,"interest value for subvention " +     interest);

    String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"rofInter value in if stmt" + rofInter.length());
    }
    if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
          // Loggers.general().info(LOG,"rofInter value in if stmt" +
          // rofInter.length());
          rofInter = "0.00";
          rateinterest = Double.valueOf(rofInter);

    }
    // Loggers.general().info(LOG,"rateinterest value for subvention " +
    // rateinterest);
    rateinterest = Double.valueOf(rofInter);
    if (rateinterest > 0 || (rateinterest != 0.0)) {
          Double avail = (interest * subvent) / rateinterest;
          String present = String.format("%.2f", avail);
          // Loggers.general().info(LOG,"present amount for Subvention " +
          // present);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"present amount for Subvention " + present);
          }
          setSUBVRBI(present + " " + inr);
          setSUBVCRD(present + " " + inr);
    }

}

else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("")
          || elisub.trim().isEmpty() || elisub == null) {
     Loggers.general().info(LOG,"Subvention eligible is No and elisub is blank");
    setSUBVRBI(available + " " + inr);
    setSUBVCRD(available + " " + inr);
    String sub = getINTPERE();
    sub = "";
    setINTPERE(sub);
}

else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("NO")) {
    Loggers.general().info(LOG,"Subvention eligible is No and elisub also NO");
    setSUBVRBI(available + " " + inr);
    setSUBVCRD(available + " " + inr);
    String sub = getINTPERE();
    sub = "";
    setINTPERE(sub);
}

else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("NO")) {
    Loggers.general().info(LOG,"Subvention eligible yes and elisub also      no");
    setSUBVRBI(available + " " + inr);
    setSUBVCRD(available + " " + inr);
    String sub = getINTPERE();
    sub = "";
    setINTPERE(sub);
} else if (dmsstr.trim().equalsIgnoreCase("")
          || dmsstr.trim().isEmpty() && elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty()) {
     Loggers.general().info(LOG,"Subvention eligible is No and elisub is blank");
    setSUBVRBI(available + " " + inr);
    setSUBVCRD(available + " " + inr);
    String sub = getINTPERE();
    sub = "";
    setINTPERE(sub);
}
} else {
Loggers.general().info(LOG,"SubproCode is not PCR-----> " + subproCode);
setELISUB("");
setROTINT("");
setSUBVRBI("");
setSUBVCRD("");
setINTPERE("");
}
}

public void onSUBVENFSACREclayButton() {
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
// PCR //PTP
String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
// Loggers.general().info(LOG,"SubproCode-----> " + subproCode);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"SubproCode-----> " + subproCode);
}
if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
    || subproCode.equalsIgnoreCase("HCA")) {
try {
    // Loggers.general().info(LOG,"onSUBVENFINSTDALOLAYButton is called");
    SubventionCalculation();
} catch (Exception e) {
    // Loggers.general().info(LOG,"exception in onSUBVENFINSTDALOLAYButton "
    // + e.getMessage());
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"exception in onSUBVENFINSTDALOLAYButton " + e.getMessage());
    }
}
} else {
// Loggers.general().info(LOG,"SubproCode is not PCR-----> " + subproCode);
setELISUB("");
setROTINT("");
setSUBVRBI("");
setSUBVCRD("");
setINTPERE("");
}

}

public Date lastDate(String input) {
Date lastDate = null;
try {
SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
lastDate = format.parse(input);
input = input.substring(input.length() - 4);
// Loggers.general().info(LOG,"val " + input);
String input1 = "31/12/" + input;
// Loggers.general().info(LOG,"val of Date " + input1);

lastDate = format.parse(input1);

} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
return lastDate;
}

public String SimpleIntrestCalculation(double principal, double subven, int tenor) {

String start_Date = "";
String maturity_date = "";
String present = "0.0";
String present1 = "0.0";
String present2 = "0.0";
Double avail1 = 0.0;
Double avail2 = 0.0;
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
try {

// Loggers.general().info(LOG,"FSA Repay entered SimpleIntrestCalculation");
start_Date = getDriverWrapper().getEventFieldAsText("B+SD", "d", "");
// Loggers.general().info(LOG,"start Date is " + start_Date);
maturity_date = getDriverWrapper().getEventFieldAsText("B+MD", "d", "");
// Loggers.general().info(LOG,"Maturity date is " + maturity_date);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Maturity date is " + maturity_date);
}

SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
Date maturityDate = format.parse(maturity_date);
Date startDate = format.parse(start_Date);

// Calculation of Refund Amount
// function to retrieve the last date of the year
Date last_date_year = lastDate(start_Date);
startDate = format.parse(start_Date);
long diff = (last_date_year.getTime() - startDate.getTime());
int diff_val = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Start and maturity date diff_val--->" + diff_val);
}
// case if the tenore for repayment falls within the same year
if (diff_val >= tenor) {
    // Loggers.general().info(LOG,"if the tenor for repayment within the
    // same year");
    // Loggers.general().info(LOG,"entered if");
    int yeardb = 0;
    yeardb = Integer.parseInt(start_Date.substring(start_Date.length() - 4));
    // if the loan start year is leap year
    if (yeardb % 4 == 0) {
          // Loggers.general().info(LOG,"year is leap Year");
          avail1 = (principal * subven * tenor) / (100 * 366);
          // Loggers.general().info(LOG,"val " + avail1);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"val " + avail1);
          }
          present1 = String.format("%.2f", avail1);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"SimpleIntrestCalculation is if 1" + present1);
          }
    }
    // if the loan start year is not leap year
    else {
          // Loggers.general().info(LOG,"Year is not a Leap Year");
          avail1 = (principal * subven * tenor) / (100 * 365);
          // Loggers.general().info(LOG,"val " + avail1);
          present1 = String.format("%.2f", avail1);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"SimpleIntrestCalculation is else 1" + present1);
          }

    }
}
// case if the tenore for repayment falls into next year
else {
    // Loggers.general().info(LOG,"Incase the tenor for repayment into next
    // year else loop");
    // calculation for the current year
    diff = (last_date_year.getTime() - startDate.getTime());
    diff_val = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    // Loggers.general().info(LOG,"Repayment into next year else loop
    // diff_val" + diff_val);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Repayment into next year else loop diff_val" + diff_val);
    }
    int yeardb = 0;
    yeardb = Integer.parseInt(start_Date.substring(start_Date.length() - 4));
    // Loggers.general().info(LOG,"Repayment into next year else loop
    // yeardb" + yeardb);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Repayment into next year else loop yeardb" + yeardb);
    }
    // if the loan start year is leap year
    if (yeardb % 4 == 0) {
          // Loggers.general().info(LOG,"year is leap year");
          avail1 = (principal * subven * diff_val) / (100 * 366);

          present1 = String.format("%.2f", avail1);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"SimpleIntrestCalculation next year else loop val in if loop" + present1);
          }
    }
    // if the loan start year is not leap year
    else {
          // Loggers.general().info(LOG,"year is not a Leap year ");
          avail1 = (principal * subven * diff_val) / (100 * 365);
          // Loggers.general().info(LOG,"next year else loop val in else" +
          // avail1);
          present1 = String.format("%.2f", avail1);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"SimpleIntrestCalculation next year else loop val in else" + present1);
          }

    }

    // calculation for the next year
    diff = (last_date_year.getTime() - startDate.getTime());
    // Loggers.general().info(LOG,"diff val in Milliseconds " + diff);
    // diff_val = (int) TimeUnit.DAYS.convert(diff,
    // TimeUnit.MILLISECONDS);
    Loggers.general().info(LOG,"diff val in Day " + diff_val);
    yeardb = Integer.parseInt(maturity_date.substring(maturity_date.length() - 4));
    // Loggers.general().info(LOG,"year DB- " + yeardb);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"year DB- " + yeardb);
    }
    // if the loan start year is leap year
    if (yeardb % 4 == 0) {
          // Loggers.general().info(LOG,"Year is Leap Year");
          avail2 = (principal * subven * (tenor - diff_val)) / (100 * 366);
          // Loggers.general().info(LOG,"val " + avail2);
          present2 = String.format("%.2f", avail2);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"val2 is present2 if " + present2);
          }

    }
    // if the loan start year is not leap year
    else {
          // Loggers.general().info(LOG,"Not a Leap A Leap Year ");
          avail2 = (principal * subven * (tenor - diff_val)) / (100 * 365);
          // Loggers.general().info(LOG,"val " + avail2);
          present2 = String.format("%.2f", avail2);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"val2 is present2 else" + present2);
          }

    }

}
// Loggers.general().info(LOG,"Available Subvention value is " + avail1 +
// "avail2===>" + avail2);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Available Subvention value is " + avail1 + "avail2===>" + avail2);
}
present = String.format("%.2f", avail1 + avail2);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Final Subvention value is " + present);
}
} catch (Exception e) {
// Loggers.general().info(LOG,"exception in SimpleIntrestCalculation " +
// e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"exception in SimpleIntrestCalculation " + e.getMessage());
}
}
return present;
}

public void SubventionCalculation() {

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
int tenorval = 0;
int yeardb = 0;
String intrest_type = "";
String year = "";
// Loggers.general().info(LOG,"SUBVEN for Outward Collection Finance Event");
String dmsstr = "";
String customer = "customer";
String customera = "customerid";
Double principal = Double.valueOf(getDriverWrapper().getEventFieldAsText("B+DA", "v", "m"));// Finance
                                                                                                                                  // Deal
                                                                                                                                  // Amount

customera = getDriverWrapper().getEventFieldAsText("B+FT", "p", "no").trim();// primary
// customer
// number
// Loggers.general().info(LOG,"customera value for subvention " + customera);
Double available = 0.00;
String inr = "INR";
String elisub = "";
String refund = "0.00";
// String rofInter ="0.00";
Double inter = 0.00;
// String inter1 = "0.00";
Double subvent = 0.00;
Double interest = 0.00;
Double rateinterest = 0.00;
Double refuntAmt = 0.00;

String subv = "";
// Loggers.general().info(LOG,"Initial subv value in string " + subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Initial subv value in string " + subv);
}
try {
elisub = getELISUB();
elisub = elisub.trim();
// Loggers.general().info(LOG,"Initial elisub value in string " + elisub);
// Loggers.general().info(LOG,"Initial elisub value in string " + elisub);
// Checking whether Subvention eligible is empty or not.
// If empty then check in customer master and check for eligibility
if (elisub.equalsIgnoreCase("")) {
    try {
          // Loggers.general().info(LOG,"enter into subvention create");
          con = ConnectionMaster.getConnection();
          String dms = "select nvl(trim(SUBELB),'NO') from extcust where cust ='" + customera + "'";
          // Loggers.general().info(LOG,"subvention create query----" + dms);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"subvention create query----" + dms);
          }
          dmsp = con.prepareStatement(dms);
          // Loggers.general().info(LOG,"Query" + dms);
          dmsr = dmsp.executeQuery();
          while (dmsr.next()) {
                dmsstr = dmsr.getString(1);
                if (dmsstr != null) {
                      dmsstr = dmsstr.trim();
                }
                setINTPERE("0.00");
                setELISUB(dmsstr);
          }

    } catch (Exception e) {
          // Loggers.general().info(LOG,e.getMessage());
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,e.getMessage());
          }
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

// code to calculate subvention if selected yes in transaction or
// defaulted to yes from customer master
// Code is changed on 23/09/2016

intrest_type = getDriverWrapper().getEventFieldAsText("B+IS", "l", "").trim();
// Loggers.general().info(LOG,"Interest type" + intrest_type);
// Loggers.general().info(LOG,"if the intrest type is Y then it is Interest
// in advance - standard");
elisub = getELISUB().trim();
if (elisub.equalsIgnoreCase("YES")) {
    // code to get the subvention percentage
    subv = getINTPERE();

    // Loggers.general().info(LOG,"enter into Subvention Eligible is YES
    // eligible value set as YES");
    // Loggers.general().info(LOG," subv value in string " + subv);

    subvent = Double.valueOf(subv);
    // Loggers.general().info(LOG,"subvent value " + subvent);

    // code to get the tenor of loan
    String tenor = "";
    tenor = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    // Loggers.general().info(LOG," TENOR value if stmt" + tenor);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG," TENOR value if stmt" + tenor);
    }
    if (tenor.equalsIgnoreCase("")) {
          // Loggers.general().info(LOG,"TENOR value INSIDE if stmt" +
          // tenor.length());
          tenor = "0";
          tenorval = Integer.parseInt(tenor);

    }

    tenorval = Integer.parseInt(tenor);
    // Loggers.general().info(LOG,"TENOR IN FINSTANDALONE STMT " +
    // tenorval);
    // Loggers.general().info(LOG,"principal amount" + principal);
    String Subvention_Amount = SimpleIntrestCalculation(principal, subvent, tenorval);
    // Loggers.general().info(LOG,"Subvention_Amount======>" +
    // Subvention_Amount);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention_Amount======>" + Subvention_Amount);
    }
    setSUBVRBI(Subvention_Amount + " " + inr);
    setSUBVCRD(Subvention_Amount + " " + inr);

}

else {
    // Loggers.general().info(LOG,"Subvention eligible is No and elisub is
    // blank");
    setSUBVRBI(available + " " + inr);
    setSUBVCRD(available + " " + inr);
    String sub = getINTPERE();
    sub = "";
    setINTPERE(sub);
}
} catch (Exception e) {
// Loggers.general().info(LOG,"exception e " + e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"exception e " + e.getMessage());
}
}

}

public void onSUBVENFSAREPclayButton() {
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
try {

// Loggers.general().info(LOG,"SUBVEN for Fin standalone repay");
Double available = 0.00;
Double principalRepayed = 0.0;
String inr = "INR";
String Principcl_Repaid_Amt = "";
int subvenPercentage = 0;
int tenor = 0;
String value_date = "";
String start_Date = "";
String maturity_date = "";
String subvention = getINTPERE();
Principcl_Repaid_Amt = getDriverWrapper().getEventFieldAsText("FPR", "v", "m");
// Loggers.general().info(LOG,"value of Principal Repayed Amount ");
value_date = getDriverWrapper().getEventFieldAsText("RPD", "d", "");
// Loggers.general().info(LOG,"value date is " + value_date);
start_Date = getDriverWrapper().getEventFieldAsText("B+SD", "d", "");
// Loggers.general().info(LOG,"start Date is " + start_Date);
maturity_date = getDriverWrapper().getEventFieldAsText("B+MD", "d", "");
// Loggers.general().info(LOG,"Maturity date is " + maturity_date);
Double subvent = Double.valueOf(subvention);

String interAdv = getDriverWrapper().getEventFieldAsText("B+OT", "s", "");
interAdv = interAdv.trim();
// Loggers.general().info(LOG,"Interest type" + interAdv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Interest type" + interAdv);
}

Boolean abnormal_recovery = getPOSHCL_Name();
Loggers.general().info(LOG,"Account closure" + abnormal_recovery);

if (interAdv.equalsIgnoreCase("Interest in advance - standard")) {
    Loggers.general().info(LOG,"Repayment type is Interest in advance -standard");

    // case if recovery is not abnormal, scenario is normal advance
    // repayment
    if (!abnormal_recovery) {
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
          Date maturityDate = format.parse(maturity_date);
          Date valueDate = format.parse(value_date);
          Date startDate = format.parse(start_Date);
          // case if recovery is not abnormal, scenario is normal
          // advance repayment before maturity date,refund is present
          if (valueDate.compareTo(maturityDate) == -1) {
                // Loggers.general().info(LOG,"value Date is less than maturity
                // date");
                long diff = (maturityDate.getTime() - valueDate.getTime());
                tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                Loggers.general().info(LOG,"tenor val in if loop" + tenor);
          }
          // case if recovery is not abnormal, scenario is normal
          // advance repayment after maturity date ,no refund
          else {
                tenor = 0;
          }
    }
    // case if recovery is abnormal, scenario is normal advance
    // repayment
    else {
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
          Date maturityDate = format.parse(maturity_date);
          Date startDate = format.parse(start_Date);
          // case if recovery is abnormal, scenario is normal advance
          // repayment after maturity date,refund is for normal for
          // full tenor
          // Loggers.general().info(LOG,"value Date is less than maturity
          // date");
          long diff = (maturityDate.getTime() - startDate.getTime());
          tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
          Loggers.general().info(LOG,"tenor val in else loop " + tenor);
    }
    // Calculation of Refund Amount
    subvenPercentage = Integer.parseInt(getINTPERE());
    // Loggers.general().info(LOG,"value of interest percentage " +
    // subvenPercentage);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"value of interest percentage " + subvenPercentage);
    }

    principalRepayed = Double.valueOf(Principcl_Repaid_Amt);

    String Subvention_Amount = SimpleIntrestCalculation(principalRepayed, subvenPercentage, tenor);
    Loggers.general().info(LOG,"Interest Subvention_Amount Advance" + Subvention_Amount);
    setSUBVPAY(Subvention_Amount + " " + inr);
    setSUBVDEB(Subvention_Amount + " " + inr);

} else if (interAdv.equalsIgnoreCase("Interest in arrears - standard") && (!abnormal_recovery)) {

    Loggers.general().info(LOG,"enter into Interest in arrears - standard");
    // Tenor Calculation
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    Date maturityDate = format.parse(maturity_date);
    Date valueDate = format.parse(value_date);
    Date startDate = format.parse(start_Date);
    if (valueDate.compareTo(maturityDate) == 1) {
          // Loggers.general().info(LOG,"value Date id Greator than Maturiy
          // Date ");
          long diff = maturityDate.getTime() - startDate.getTime();
          tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
          // Loggers.general().info(LOG,"tenor val is " + tenor);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"tenor val is " + tenor);
          }
    } else {
          long diff = valueDate.getTime() - startDate.getTime();
          tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
          // Loggers.general().info(LOG,"tenor val is " + tenor);
    }

    // Calculation of Refund Amount
    subvenPercentage = Integer.parseInt(getINTPERE());

    principalRepayed = Double.valueOf(Principcl_Repaid_Amt);
    Loggers.general().info(LOG,"Interest principal Repayed amount" + principalRepayed);
    Loggers.general().info(LOG,"Tenor days for calculation" + tenor);
    String Subvention_Amount = SimpleIntrestCalculation(principalRepayed, subvenPercentage, tenor);
    Loggers.general().info(LOG,"Interest Subvention_Amount arrears" + Subvention_Amount);
    setSUBVRBI(Subvention_Amount + " " + inr);
    setSUBVCRD(Subvention_Amount + " " + inr);

}
// case if the interest is monthly consolidation and abnormal
// recovery
else {
    setSUBVRBI("0.00 INR");
    setSUBVCRD("0.00 INR");
    setSUBVPAY("0.00 INR");
    setSUBVDEB("0.00 INR");
    // Loggers.general().info(LOG,"Else Excecuted since it is not Intrest in
    // advance or intrest in arrears");
}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in onSUBVENFINSTDALOREPAYButton " +
// e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Exception in onSUBVENFINSTDALOREPAYButton " + e.getMessage());
}
}

}

public void onSUBVENFINOUTCOLADJclayButton() {

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
// Loggers.general().info(LOG,"SUBVEN for Fin standalone create");
String dmsstr = "null";
String customer = "customer";
String customera = "customerid";
customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");// party
                                                                                              // name

String interAdv = getDriverWrapper().getEventFieldAsText("B+OT", "s", "");
interAdv = interAdv.trim();
// //Loggers.general().info(LOG,"customer value " + customer);
String masRef = getDriverWrapper().getEventFieldAsText("MST", "r", "");
customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();// party
// Loggers.general().info(LOG,"customera value for subvention " + customera);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"customera value for subvention ODC Finance Adjust" + customera);
}
// String ELISUB ="";
Double available = 0.00;
String inr = "INR";
String refund = "0.00";
// String rofInter ="0.00";
Double inter = 0.00;
// String inter1 = "0.00";
Double subvent = 0.00;
Double interest = 0.00;
Double rateinterest = 0.00;
Double refuntAmt = 0.00;

try {
// String dmsstr="";
// Loggers.general().info(LOG,"enter into subvention finexlc create");
con = ConnectionMaster.getConnection();
String dms = "select SUBELB from extcust where cust ='" + customera + "'";
dmsp = con.prepareStatement(dms);
Loggers.general().info(LOG,"Subvention eligible value Query===>" + dms);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Query" + dms);
}
dmsr = dmsp.executeQuery();
while (dmsr.next()) {
    dmsstr = dmsr.getString(1);
    if (dmsstr != null) {
          dmsstr = dmsstr.trim();
    }

}

} catch (Exception e) {
// Loggers.general().info(LOG,e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Exception Subvention eligible" + e.getMessage());
}
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
// setELISUB(dmsstr)

String elisub = getELISUB();
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Initial elisub value in string ODC finance Adjust" + elisub);

Loggers.general().info(LOG,"Subvention eligible value Query ODC finance Adjust" + dmsstr);
}
String subv = getINTPERE();
// Loggers.general().info(LOG,"Initial subv value in string " + subv);
if (dmsstr.trim().equalsIgnoreCase("YES")
    && (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub == null)) {
// setELISUB(dmsstr);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible value Query value YES and Transaction level blank");
}
if (subv.isEmpty() || subv == null || subv.length() == 0) {
    // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

    subv = "0.00";
    subvent = Double.valueOf(subv);

}
subvent = Double.valueOf(subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
}

String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");

if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest amount to calculate===>" + inter1);
}
if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    inter1 = "0.00";
    interest = Double.valueOf(inter1);

}
interest = Double.valueOf(inter1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest value for subvention======>" + interest);
}
String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
// Loggers.general().info(LOG,"rofInter value in if stmt" +
// rofInter.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
}
if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    rofInter = "0.00";
    rateinterest = Double.valueOf(rofInter);

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
}
setROTINT(rofInter);
// Loggers.general().info(LOG,"rateinterest value for subvention " +
// rateinterest);
rateinterest = Double.valueOf(rofInter);
if (rateinterest > 0 || (rateinterest != 0.0)) {
    Double avail = (interest * subvent) / rateinterest;

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Available amount for Subvention " + avail);
    }
    String present = String.format("%.2f", avail);
    // Loggers.general().info(LOG,"present amount for Subvention " +
    // present);

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"present amount for Subvention final" + present);
    }

    setSUBVRBI(present + " " + inr);
    setSUBVCRD(present + " " + inr);

}

try {

    double subCredit = 0;
    double subRBI = 0;
    double subvenCredit_Double = 0;
    double subvenRBI_Double = 0;
    double tenorOld_val = 0;
    double tenorNew_val = 0;
    String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
    if (!tenorOld.equalsIgnoreCase("")) {
          tenorOld_val = Double.valueOf(tenorOld);
    }
    if (!tenorNew.equalsIgnoreCase("")) {
          tenorNew_val = Double.valueOf(tenorNew);
    }
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
    }
    String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust
    // subventionCredit" + subventionCredit);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finodc amend subventionCredit" + subventionCredit);
    }
    if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
          subvenCredit_Double = Double.valueOf(subventionCredit);
    }
    String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
    // + subventionRBI);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finodc amend subventionRBI" + subventionRBI);
    }
    if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
          subvenRBI_Double = Double.valueOf(subventionRBI);
    }
    con = ConnectionMaster.getConnection();
    String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
    // Loggers.general().info(LOG,"Subvention Query===>" + dms);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention Query===>" + dms);
    }
    dmsp = con.prepareStatement(dms);

    dmsr = dmsp.executeQuery();
    if (dmsr.next()) {
          subCredit = dmsr.getDouble(1);
          // Loggers.general().info(LOG,"Subvention credit for create" +
          // subCredit);
          subRBI = dmsr.getDouble(2);
          double totalSubvCredit = subCredit - subvenCredit_Double;
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
          }
          double totalSubvRBI = subRBI - subvenRBI_Double;
          DecimalFormat diff = new DecimalFormat("0.00");
          diff.setMaximumFractionDigits(2);
          String FinalSubvCredit = diff.format(totalSubvCredit);
          // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
          // + FinalSubvCredit);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
          }
          if (tenorOld_val > tenorNew_val) {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                }
                if (totalSubvRBI > 0) {
                      setSUBVDEB(FinalSubvCredit + " " + inr);
                      setSUBVPAY(FinalSubvCredit + " " + inr);
                } else {
                      setSUBVDEB(0 + " " + inr);
                      setSUBVPAY(0 + " " + inr);
                }
          } else {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,
                                  "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                }

                setSUBVPAY(available + " " + inr);
                setSUBVDEB(available + " " + inr);
          }

    }

    else {
          setSUBVPAY(available + " " + inr);
          setSUBVDEB(available + " " + inr);
    }

    if (tenorOld_val < tenorNew_val) {

          try {
                String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                double amanedValue = Double.valueOf(amanedVal);
                double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                double subVal = subvent / 100;

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                }

                double totalVal = amanedValue * tenorVal * subVal;
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                }
                if (totalVal > 0) {
                      String totalValue = String.format("%.2f", totalVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                      setSUBVRBI(totalValue + " " + inr);
                      setSUBVCRD(totalValue + " " + inr);
                } else {
                      setSUBVRBI(0 + " " + inr);
                      setSUBVCRD(0 + " " + inr);
                }

          } catch (Exception e) {
                Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
          }

    }

} catch (Exception e) {
    Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

} else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("YES")) {
// setELISUB(dmsstr);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible value Query value YES and Transaction YES");
}
if (subv.isEmpty() || subv == null || subv.length() == 0) {
    // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

    subv = "0.00";
    subvent = Double.valueOf(subv);

}
subvent = Double.valueOf(subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
}

String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
// Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
}
if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    inter1 = "0.00";
    interest = Double.valueOf(inter1);

}
interest = Double.valueOf(inter1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest value for subvention " + interest);
}
String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
// Loggers.general().info(LOG,"rofInter value in if stmt" +
// rofInter.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
}
if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    rofInter = "0.00";
    rateinterest = Double.valueOf(rofInter);

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
}
setROTINT(rofInter);
// Loggers.general().info(LOG,"rateinterest value for subvention " +
// rateinterest);
rateinterest = Double.valueOf(rofInter);
if (rateinterest > 0 || (rateinterest != 0.0)) {
    Double avail = (interest * subvent) / rateinterest;

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Available amount for Subvention " + avail);
    }
    String present = String.format("%.2f", avail);
    // Loggers.general().info(LOG,"present amount for Subvention " +
    // present);

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"present amount for Subvention " + present);
    }
    setSUBVRBI(present + " " + inr);
    setSUBVCRD(present + " " + inr);
}

try {

    double subCredit = 0;
    double subRBI = 0;
    double subvenCredit_Double = 0;
    double subvenRBI_Double = 0;
    double tenorOld_val = 0;
    double tenorNew_val = 0;
    String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
    if (!tenorOld.equalsIgnoreCase("")) {
          tenorOld_val = Double.valueOf(tenorOld);
    }
    if (!tenorNew.equalsIgnoreCase("")) {
          tenorNew_val = Double.valueOf(tenorNew);
    }
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
    }
    String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust
    // subventionCredit" + subventionCredit);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
    }
    if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
          subvenCredit_Double = Double.valueOf(subventionCredit);
    }
    String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
    // + subventionRBI);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
    }
    if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
          subvenRBI_Double = Double.valueOf(subventionRBI);
    }
    con = ConnectionMaster.getConnection();
    String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
    // Loggers.general().info(LOG,"Subvention Query===>" + dms);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention Query===>" + dms);
    }
    dmsp = con.prepareStatement(dms);

    dmsr = dmsp.executeQuery();
    if (dmsr.next()) {
          subCredit = dmsr.getDouble(1);
          // Loggers.general().info(LOG,"Subvention credit for create" +
          // subCredit);
          subRBI = dmsr.getDouble(2);
          double totalSubvCredit = subCredit - subvenCredit_Double;
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
          }
          double totalSubvRBI = subRBI - subvenRBI_Double;
          DecimalFormat diff = new DecimalFormat("0.00");
          diff.setMaximumFractionDigits(2);
          String FinalSubvCredit = diff.format(totalSubvCredit);
          // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
          // + FinalSubvCredit);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
          }
          if (tenorOld_val > tenorNew_val) {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                }
                if (totalSubvRBI > 0) {
                      setSUBVDEB(FinalSubvCredit + " " + inr);
                      setSUBVPAY(FinalSubvCredit + " " + inr);
                } else {
                      setSUBVDEB(0 + " " + inr);
                      setSUBVPAY(0 + " " + inr);
                }
          } else {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,
                                  "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                }

                setSUBVPAY(available + " " + inr);
                setSUBVDEB(available + " " + inr);
          }

    }

    else {
          setSUBVPAY(available + " " + inr);
          setSUBVDEB(available + " " + inr);
    }

    if (tenorOld_val < tenorNew_val) {

          try {
                String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                double amanedValue = Double.valueOf(amanedVal);
                double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                double subVal = subvent / 100;

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                }

                double totalVal = amanedValue * tenorVal * subVal;
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                }
                if (totalVal > 0) {
                      String totalValue = String.format("%.2f", totalVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                      setSUBVRBI(totalValue + " " + inr);
                      setSUBVCRD(totalValue + " " + inr);
                } else {
                      setSUBVRBI(0 + " " + inr);
                      setSUBVCRD(0 + " " + inr);
                }

          } catch (Exception e) {
                Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
          }

    }

} catch (Exception e) {
    Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

} else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("YES")) {
// setELISUB(dmsstr);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible value Query value NO and Transaction YES");
}
if (subv.isEmpty() || subv == null || subv.length() == 0) {
    // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

    subv = "0.00";
    subvent = Double.valueOf(subv);

}
subvent = Double.valueOf(subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
}

String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
// Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
}
if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    inter1 = "0.00";
    interest = Double.valueOf(inter1);

}
interest = Double.valueOf(inter1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest value for subvention " + interest);
}
String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
// Loggers.general().info(LOG,"rofInter value in if stmt" +
// rofInter.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
}
if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    rofInter = "0.00";
    rateinterest = Double.valueOf(rofInter);

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
}
setROTINT(rofInter);
// Loggers.general().info(LOG,"rateinterest value for subvention " +
// rateinterest);
rateinterest = Double.valueOf(rofInter);
if (rateinterest > 0 || (rateinterest != 0.0)) {
    Double avail = (interest * subvent) / rateinterest;

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Available amount for Subvention " + avail);
    }
    String present = String.format("%.2f", avail);
    // Loggers.general().info(LOG,"present amount for Subvention " +
    // present);

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"present amount for Subvention " + present);
    }
    setSUBVRBI(present + " " + inr);
    setSUBVCRD(present + " " + inr);
}

try {

    double subCredit = 0;
    double subRBI = 0;
    double subvenCredit_Double = 0;
    double subvenRBI_Double = 0;
    double tenorOld_val = 0;
    double tenorNew_val = 0;
    String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
    if (!tenorOld.equalsIgnoreCase("")) {
          tenorOld_val = Double.valueOf(tenorOld);
    }
    if (!tenorNew.equalsIgnoreCase("")) {
          tenorNew_val = Double.valueOf(tenorNew);
    }
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
    }
    String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust
    // subventionCredit" + subventionCredit);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
    }
    if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
          subvenCredit_Double = Double.valueOf(subventionCredit);
    }
    String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
    // + subventionRBI);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
    }
    if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
          subvenRBI_Double = Double.valueOf(subventionRBI);
    }
    con = ConnectionMaster.getConnection();
    String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
    // Loggers.general().info(LOG,"Subvention Query===>" + dms);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention Query===>" + dms);
    }
    dmsp = con.prepareStatement(dms);

    dmsr = dmsp.executeQuery();
    if (dmsr.next()) {
          subCredit = dmsr.getDouble(1);
          // Loggers.general().info(LOG,"Subvention credit for create" +
          // subCredit);
          subRBI = dmsr.getDouble(2);
          double totalSubvCredit = subCredit - subvenCredit_Double;
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
          }
          double totalSubvRBI = subRBI - subvenRBI_Double;
          DecimalFormat diff = new DecimalFormat("0.00");
          diff.setMaximumFractionDigits(2);
          String FinalSubvCredit = diff.format(totalSubvCredit);
          // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
          // + FinalSubvCredit);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
          }
          if (tenorOld_val > tenorNew_val) {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                }
                if (totalSubvRBI > 0) {
                      setSUBVDEB(FinalSubvCredit + " " + inr);
                      setSUBVPAY(FinalSubvCredit + " " + inr);
                } else {
                      setSUBVDEB(0 + " " + inr);
                      setSUBVPAY(0 + " " + inr);
                }
          } else {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,
                                  "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                }

                setSUBVPAY(available + " " + inr);
                setSUBVDEB(available + " " + inr);
          }

    }

    else {
          setSUBVPAY(available + " " + inr);
          setSUBVDEB(available + " " + inr);
    }

    if (tenorOld_val < tenorNew_val) {

          try {
                String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                double amanedValue = Double.valueOf(amanedVal);
                double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                double subVal = subvent / 100;

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                }

                double totalVal = amanedValue * tenorVal * subVal;
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                }
                if (totalVal > 0) {
                      String totalValue = String.format("%.2f", totalVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                      setSUBVRBI(totalValue + " " + inr);
                      setSUBVCRD(totalValue + " " + inr);
                } else {
                      setSUBVRBI(0 + " " + inr);
                      setSUBVCRD(0 + " " + inr);
                }

          } catch (Exception e) {
                Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
          }

    }

} catch (Exception e) {
    Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

else if (elisub.trim().equalsIgnoreCase("YES")
    && (dmsstr.trim().equalsIgnoreCase("") || dmsstr.isEmpty() || dmsstr == null)) {
// setELISUB(dmsstr);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible Transaction level YES and query level blank");
}
if (subv.isEmpty() || subv == null || subv.length() == 0) {
    // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

    subv = "0.00";
    subvent = Double.valueOf(subv);

}
subvent = Double.valueOf(subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
}

String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
// Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
}
if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    inter1 = "0.00";
    interest = Double.valueOf(inter1);

}
interest = Double.valueOf(inter1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest value for subvention " + interest);
}
String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
// Loggers.general().info(LOG,"rofInter value in if stmt" +
// rofInter.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
}
if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    rofInter = "0.00";
    rateinterest = Double.valueOf(rofInter);

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
}
setROTINT(rofInter);
// Loggers.general().info(LOG,"rateinterest value for subvention " +
// rateinterest);
rateinterest = Double.valueOf(rofInter);
if (rateinterest > 0 || (rateinterest != 0.0)) {
    Double avail = (interest * subvent) / rateinterest;

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Available amount for Subvention " + avail);
    }
    String present = String.format("%.2f", avail);
    // Loggers.general().info(LOG,"present amount for Subvention " +
    // present);

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"present amount for Subvention " + present);
    }
    setSUBVRBI(present + " " + inr);
    setSUBVCRD(present + " " + inr);
}

try {

    double subCredit = 0;
    double subRBI = 0;
    double subvenCredit_Double = 0;
    double subvenRBI_Double = 0;
    double tenorOld_val = 0;
    double tenorNew_val = 0;
    String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
    if (!tenorOld.equalsIgnoreCase("")) {
          tenorOld_val = Double.valueOf(tenorOld);
    }
    if (!tenorNew.equalsIgnoreCase("")) {
          tenorNew_val = Double.valueOf(tenorNew);
    }
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
    }
    String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust
    // subventionCredit" + subventionCredit);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
    }
    if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
          subvenCredit_Double = Double.valueOf(subventionCredit);
    }
    String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
    // + subventionRBI);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
    }
    if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
          subvenRBI_Double = Double.valueOf(subventionRBI);
    }
    con = ConnectionMaster.getConnection();
    String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
    // Loggers.general().info(LOG,"Subvention Query===>" + dms);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention Query===>" + dms);
    }
    dmsp = con.prepareStatement(dms);

    dmsr = dmsp.executeQuery();
    if (dmsr.next()) {
          subCredit = dmsr.getDouble(1);
          // Loggers.general().info(LOG,"Subvention credit for create" +
          // subCredit);
          subRBI = dmsr.getDouble(2);
          double totalSubvCredit = subCredit - subvenCredit_Double;
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
          }
          double totalSubvRBI = subRBI - subvenRBI_Double;
          DecimalFormat diff = new DecimalFormat("0.00");
          diff.setMaximumFractionDigits(2);
          String FinalSubvCredit = diff.format(totalSubvCredit);
          // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
          // + FinalSubvCredit);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
          }
          if (tenorOld_val > tenorNew_val) {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                }
                if (totalSubvRBI > 0) {
                      setSUBVDEB(FinalSubvCredit + " " + inr);
                      setSUBVPAY(FinalSubvCredit + " " + inr);
                } else {
                      setSUBVDEB(0 + " " + inr);
                      setSUBVPAY(0 + " " + inr);
                }
          } else {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,
                                  "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                }

                setSUBVPAY(available + " " + inr);
                setSUBVDEB(available + " " + inr);
          }

    }

    else {
          setSUBVPAY(available + " " + inr);
          setSUBVDEB(available + " " + inr);
    }

    if (tenorOld_val < tenorNew_val) {

          try {
                String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                double amanedValue = Double.valueOf(amanedVal);
                double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                double subVal = subvent / 100;

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                }

                double totalVal = amanedValue * tenorVal * subVal;
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                }
                if (totalVal > 0) {
                      String totalValue = String.format("%.2f", totalVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                      setSUBVRBI(totalValue + " " + inr);
                      setSUBVCRD(totalValue + " " + inr);
                } else {
                      setSUBVRBI(0 + " " + inr);
                      setSUBVCRD(0 + " " + inr);
                }

          } catch (Exception e) {
                Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
          }

    }

} catch (Exception e) {
    Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

else if (dmsstr.trim().equalsIgnoreCase("NO")
    && (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub == null)) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible query level NO and transaction level blank");
}

setSUBVRBI(available + " " + inr);
setSUBVCRD(available + " " + inr);
setSUBVPAY(available + " " + inr);
setSUBVDEB(available + " " + inr);
String sub = getINTPERE();
sub = "";
setINTPERE(sub);
}

else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("NO")) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible Transaction level NO and query level blank");
}
setSUBVRBI(available + " " + inr);
setSUBVCRD(available + " " + inr);
setSUBVPAY(available + " " + inr);
setSUBVDEB(available + " " + inr);
String sub = getINTPERE();
sub = "";
setINTPERE(sub);
}

else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("NO")) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible query level YES and transaction level blank");
}
setSUBVRBI(available + " " + inr);
setSUBVCRD(available + " " + inr);
setSUBVPAY(available + " " + inr);
setSUBVDEB(available + " " + inr);
String sub = getINTPERE();
sub = "";
setINTPERE(sub);
} else if (dmsstr.trim().equalsIgnoreCase("") || dmsstr.trim().isEmpty() && elisub.trim().equalsIgnoreCase("")
    || elisub.trim().isEmpty()) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible Transaction level blank and query level blank");
}
setSUBVRBI(available + " " + inr);
setSUBVCRD(available + " " + inr);
setSUBVPAY(available + " " + inr);
setSUBVDEB(available + " " + inr);
String sub = getINTPERE();
sub = "";
setINTPERE(sub);
}

String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
    || subproCode.equalsIgnoreCase("HCA")) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention subproduct Code===>" + subproCode);

}

} else {

setELISUB("");
setINTPERE("");
setECSECE("");
setSUBVRBI("0.00 INR");
setSUBVCRD("0.00 INR");
setSUBVPAY("0.00 INR");
setSUBVDEB("0.00 INR");

}

}

public void onSUBVENFINOUTCOLCREclayButton() {

try {

try {
    // Loggers.general().info(LOG,"onSUBVENOUTDOCFECLButton is called in
    // Finance Outward Bills create ");
    SubventionCalculation();
} catch (Exception e) {
    // Loggers.general().info(LOG,"exception in onSUBVENFINSTDALOLAYButton "
    // + e.getMessage());
}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in onSUBVENFINEXLAYOUTButton" +
// e.getMessage());
}

}

public void onSUBVENFINOUTCOLAMDclayButton() {

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
// Loggers.general().info(LOG,"SUBVEN for Fin standalone create");
String dmsstr = "null";
String customer = "customer";
String customera = "customerid";
customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");// party
                                                                                              // name

String interAdv = getDriverWrapper().getEventFieldAsText("B+OT", "s", "");
interAdv = interAdv.trim();
// //Loggers.general().info(LOG,"customer value " + customer);
String masRef = getDriverWrapper().getEventFieldAsText("MST", "r", "");
customera = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();// party
// Loggers.general().info(LOG,"customera value for subvention " + customera);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"customera value for subvention ODC Finance Amend" + customera);
}
// String ELISUB ="";
Double available = 0.00;
String inr = "INR";
String refund = "0.00";
// String rofInter ="0.00";
Double inter = 0.00;
// String inter1 = "0.00";
Double subvent = 0.00;
Double interest = 0.00;
Double rateinterest = 0.00;
Double refuntAmt = 0.00;

try {
// String dmsstr="";
// Loggers.general().info(LOG,"enter into subvention finexlc create");
con = ConnectionMaster.getConnection();
String dms = "select SUBELB from extcust where cust ='" + customera + "'";
dmsp = con.prepareStatement(dms);
Loggers.general().info(LOG,"Subvention eligible value Query===>" + dms);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Query" + dms);
}
dmsr = dmsp.executeQuery();
while (dmsr.next()) {
    dmsstr = dmsr.getString(1);
    if (dmsstr != null) {
          dmsstr = dmsstr.trim();
    }

}

} catch (Exception e) {
// Loggers.general().info(LOG,e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Exception Subvention eligible" + e.getMessage());
}
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
// setELISUB(dmsstr)

String elisub = getELISUB();
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,"Initial elisub value in string ODC finance amened" + elisub);

Loggers.general().info(LOG,"Subvention eligible value Query ODC finance amened" + dmsstr);
}
String subv = getINTPERE();
// Loggers.general().info(LOG,"Initial subv value in string " + subv);
if (dmsstr.trim().equalsIgnoreCase("YES")
    && (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub == null)) {
// setELISUB(dmsstr);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible value Query value YES and Transaction level blank");
}
if (subv.isEmpty() || subv == null || subv.length() == 0) {
    // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

    subv = "0.00";
    subvent = Double.valueOf(subv);

}
subvent = Double.valueOf(subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
}

String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");

if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest amount to calculate===>" + inter1);
}
if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    inter1 = "0.00";
    interest = Double.valueOf(inter1);

}
interest = Double.valueOf(inter1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest value for subvention======>" + interest);
}
String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
// Loggers.general().info(LOG,"rofInter value in if stmt" +
// rofInter.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
}
if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    rofInter = "0.00";
    rateinterest = Double.valueOf(rofInter);

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
}
setROTINT(rofInter);
// Loggers.general().info(LOG,"rateinterest value for subvention " +
// rateinterest);
rateinterest = Double.valueOf(rofInter);
if (rateinterest > 0 || (rateinterest != 0.0)) {
    Double avail = (interest * subvent) / rateinterest;

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Available amount for Subvention " + avail);
    }
    String present = String.format("%.2f", avail);
    // Loggers.general().info(LOG,"present amount for Subvention " +
    // present);

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"present amount for Subvention final" + present);
    }

    setSUBVRBI(present + " " + inr);
    setSUBVCRD(present + " " + inr);

}

try {

    double subCredit = 0;
    double subRBI = 0;
    double subvenCredit_Double = 0;
    double subvenRBI_Double = 0;
    double tenorOld_val = 0;
    double tenorNew_val = 0;
    String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
    if (!tenorOld.equalsIgnoreCase("")) {
          tenorOld_val = Double.valueOf(tenorOld);
    }
    if (!tenorNew.equalsIgnoreCase("")) {
          tenorNew_val = Double.valueOf(tenorNew);
    }
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
    }
    String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust
    // subventionCredit" + subventionCredit);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finodc amend subventionCredit" + subventionCredit);
    }
    if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
          subvenCredit_Double = Double.valueOf(subventionCredit);
    }
    String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
    // + subventionRBI);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finodc amend subventionRBI" + subventionRBI);
    }
    if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
          subvenRBI_Double = Double.valueOf(subventionRBI);
    }
    con = ConnectionMaster.getConnection();
    String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
    // Loggers.general().info(LOG,"Subvention Query===>" + dms);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention Query===>" + dms);
    }
    dmsp = con.prepareStatement(dms);

    dmsr = dmsp.executeQuery();
    if (dmsr.next()) {
          subCredit = dmsr.getDouble(1);
          // Loggers.general().info(LOG,"Subvention credit for create" +
          // subCredit);
          subRBI = dmsr.getDouble(2);
          double totalSubvCredit = subCredit - subvenCredit_Double;
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
          }
          double totalSubvRBI = subRBI - subvenRBI_Double;
          DecimalFormat diff = new DecimalFormat("0.00");
          diff.setMaximumFractionDigits(2);
          String FinalSubvCredit = diff.format(totalSubvCredit);
          // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
          // + FinalSubvCredit);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
          }
          if (tenorOld_val > tenorNew_val) {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                }
                if (totalSubvRBI > 0) {
                      setSUBVDEB(FinalSubvCredit + " " + inr);
                      setSUBVPAY(FinalSubvCredit + " " + inr);
                } else {
                      setSUBVDEB(0 + " " + inr);
                      setSUBVPAY(0 + " " + inr);
                }
          } else {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,
                                  "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                }

                setSUBVPAY(available + " " + inr);
                setSUBVDEB(available + " " + inr);
          }

    }

    else {
          setSUBVPAY(available + " " + inr);
          setSUBVDEB(available + " " + inr);
    }

    if (tenorOld_val < tenorNew_val) {

          try {
                String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                double amanedValue = Double.valueOf(amanedVal);
                double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                double subVal = subvent / 100;

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                }

                double totalVal = amanedValue * tenorVal * subVal;
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                }
                if (totalVal > 0) {
                      String totalValue = String.format("%.2f", totalVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                      setSUBVRBI(totalValue + " " + inr);
                      setSUBVCRD(totalValue + " " + inr);
                } else {
                      setSUBVRBI(0 + " " + inr);
                      setSUBVCRD(0 + " " + inr);
                }

          } catch (Exception e) {
                Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
          }

    }

} catch (Exception e) {
    Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

} else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("YES")) {
// setELISUB(dmsstr);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible value Query value YES and Transaction YES");
}
if (subv.isEmpty() || subv == null || subv.length() == 0) {
    // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

    subv = "0.00";
    subvent = Double.valueOf(subv);

}
subvent = Double.valueOf(subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
}

String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
// Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
}
if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    inter1 = "0.00";
    interest = Double.valueOf(inter1);

}
interest = Double.valueOf(inter1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest value for subvention " + interest);
}
String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
// Loggers.general().info(LOG,"rofInter value in if stmt" +
// rofInter.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
}
if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    rofInter = "0.00";
    rateinterest = Double.valueOf(rofInter);

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
}
setROTINT(rofInter);
// Loggers.general().info(LOG,"rateinterest value for subvention " +
// rateinterest);
rateinterest = Double.valueOf(rofInter);
if (rateinterest > 0 || (rateinterest != 0.0)) {
    Double avail = (interest * subvent) / rateinterest;

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Available amount for Subvention " + avail);
    }
    String present = String.format("%.2f", avail);
    // Loggers.general().info(LOG,"present amount for Subvention " +
    // present);

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"present amount for Subvention " + present);
    }
    setSUBVRBI(present + " " + inr);
    setSUBVCRD(present + " " + inr);
}

try {

    double subCredit = 0;
    double subRBI = 0;
    double subvenCredit_Double = 0;
    double subvenRBI_Double = 0;
    double tenorOld_val = 0;
    double tenorNew_val = 0;
    String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
    if (!tenorOld.equalsIgnoreCase("")) {
          tenorOld_val = Double.valueOf(tenorOld);
    }
    if (!tenorNew.equalsIgnoreCase("")) {
          tenorNew_val = Double.valueOf(tenorNew);
    }
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
    }
    String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust
    // subventionCredit" + subventionCredit);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
    }
    if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
          subvenCredit_Double = Double.valueOf(subventionCredit);
    }
    String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
    // + subventionRBI);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
    }
    if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
          subvenRBI_Double = Double.valueOf(subventionRBI);
    }
    con = ConnectionMaster.getConnection();
    String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
    // Loggers.general().info(LOG,"Subvention Query===>" + dms);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention Query===>" + dms);
    }
    dmsp = con.prepareStatement(dms);

    dmsr = dmsp.executeQuery();
    if (dmsr.next()) {
          subCredit = dmsr.getDouble(1);
          // Loggers.general().info(LOG,"Subvention credit for create" +
          // subCredit);
          subRBI = dmsr.getDouble(2);
          double totalSubvCredit = subCredit - subvenCredit_Double;
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
          }
          double totalSubvRBI = subRBI - subvenRBI_Double;
          DecimalFormat diff = new DecimalFormat("0.00");
          diff.setMaximumFractionDigits(2);
          String FinalSubvCredit = diff.format(totalSubvCredit);
          // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
          // + FinalSubvCredit);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
          }
          if (tenorOld_val > tenorNew_val) {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                }
                if (totalSubvRBI > 0) {
                      setSUBVDEB(FinalSubvCredit + " " + inr);
                      setSUBVPAY(FinalSubvCredit + " " + inr);
                } else {
                      setSUBVDEB(0 + " " + inr);
                      setSUBVPAY(0 + " " + inr);
                }
          } else {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,
                                  "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                }

                setSUBVPAY(available + " " + inr);
                setSUBVDEB(available + " " + inr);
          }

    }

    else {
          setSUBVPAY(available + " " + inr);
          setSUBVDEB(available + " " + inr);
    }

    if (tenorOld_val < tenorNew_val) {

          try {
                String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                double amanedValue = Double.valueOf(amanedVal);
                double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                double subVal = subvent / 100;

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                }

                double totalVal = amanedValue * tenorVal * subVal;
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                }
                if (totalVal > 0) {
                      String totalValue = String.format("%.2f", totalVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                      setSUBVRBI(totalValue + " " + inr);
                      setSUBVCRD(totalValue + " " + inr);
                } else {
                      setSUBVRBI(0 + " " + inr);
                      setSUBVCRD(0 + " " + inr);
                }

          } catch (Exception e) {
                Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
          }

    }

} catch (Exception e) {
    Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

} else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("YES")) {
// setELISUB(dmsstr);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible value Query value NO and Transaction YES");
}
if (subv.isEmpty() || subv == null || subv.length() == 0) {
    // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

    subv = "0.00";
    subvent = Double.valueOf(subv);

}
subvent = Double.valueOf(subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
}

String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
// Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
}
if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    inter1 = "0.00";
    interest = Double.valueOf(inter1);

}
interest = Double.valueOf(inter1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest value for subvention " + interest);
}
String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
// Loggers.general().info(LOG,"rofInter value in if stmt" +
// rofInter.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
}
if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    rofInter = "0.00";
    rateinterest = Double.valueOf(rofInter);

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
}
setROTINT(rofInter);
// Loggers.general().info(LOG,"rateinterest value for subvention " +
// rateinterest);
rateinterest = Double.valueOf(rofInter);
if (rateinterest > 0 || (rateinterest != 0.0)) {
    Double avail = (interest * subvent) / rateinterest;

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Available amount for Subvention " + avail);
    }
    String present = String.format("%.2f", avail);
    // Loggers.general().info(LOG,"present amount for Subvention " +
    // present);

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"present amount for Subvention " + present);
    }
    setSUBVRBI(present + " " + inr);
    setSUBVCRD(present + " " + inr);
}

try {

    double subCredit = 0;
    double subRBI = 0;
    double subvenCredit_Double = 0;
    double subvenRBI_Double = 0;
    double tenorOld_val = 0;
    double tenorNew_val = 0;
    String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
    if (!tenorOld.equalsIgnoreCase("")) {
          tenorOld_val = Double.valueOf(tenorOld);
    }
    if (!tenorNew.equalsIgnoreCase("")) {
          tenorNew_val = Double.valueOf(tenorNew);
    }
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
    }
    String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust
    // subventionCredit" + subventionCredit);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
    }
    if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
          subvenCredit_Double = Double.valueOf(subventionCredit);
    }
    String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
    // + subventionRBI);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
    }
    if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
          subvenRBI_Double = Double.valueOf(subventionRBI);
    }
    con = ConnectionMaster.getConnection();
    String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
    // Loggers.general().info(LOG,"Subvention Query===>" + dms);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention Query===>" + dms);
    }
    dmsp = con.prepareStatement(dms);

    dmsr = dmsp.executeQuery();
    if (dmsr.next()) {
          subCredit = dmsr.getDouble(1);
          // Loggers.general().info(LOG,"Subvention credit for create" +
          // subCredit);
          subRBI = dmsr.getDouble(2);
          double totalSubvCredit = subCredit - subvenCredit_Double;
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
          }
          double totalSubvRBI = subRBI - subvenRBI_Double;
          DecimalFormat diff = new DecimalFormat("0.00");
          diff.setMaximumFractionDigits(2);
          String FinalSubvCredit = diff.format(totalSubvCredit);
          // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
          // + FinalSubvCredit);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
          }
          if (tenorOld_val > tenorNew_val) {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                }
                if (totalSubvRBI > 0) {
                      setSUBVDEB(FinalSubvCredit + " " + inr);
                      setSUBVPAY(FinalSubvCredit + " " + inr);
                } else {
                      setSUBVDEB(0 + " " + inr);
                      setSUBVPAY(0 + " " + inr);
                }
          } else {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,
                                  "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                }

                setSUBVPAY(available + " " + inr);
                setSUBVDEB(available + " " + inr);
          }

    }

    else {
          setSUBVPAY(available + " " + inr);
          setSUBVDEB(available + " " + inr);
    }

    if (tenorOld_val < tenorNew_val) {

          try {
                String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                double amanedValue = Double.valueOf(amanedVal);
                double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                double subVal = subvent / 100;

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                }

                double totalVal = amanedValue * tenorVal * subVal;
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                }
                if (totalVal > 0) {
                      String totalValue = String.format("%.2f", totalVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                      setSUBVRBI(totalValue + " " + inr);
                      setSUBVCRD(totalValue + " " + inr);
                } else {
                      setSUBVRBI(0 + " " + inr);
                      setSUBVCRD(0 + " " + inr);
                }

          } catch (Exception e) {
                Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
          }

    }

} catch (Exception e) {
    Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

else if (elisub.trim().equalsIgnoreCase("YES")
    && (dmsstr.trim().equalsIgnoreCase("") || dmsstr.isEmpty() || dmsstr == null)) {
// setELISUB(dmsstr);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible Transaction level YES and query level blank");
}
if (subv.isEmpty() || subv == null || subv.length() == 0) {
    // Loggers.general().info(LOG,"subv value in if stmt" + subv.length());

    subv = "0.00";
    subvent = Double.valueOf(subv);

}
subvent = Double.valueOf(subv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention interest value===>" + subvent);
}

String inter1 = getDriverWrapper().getEventFieldAsText("B+IA", "v", "m");
// Loggers.general().info(LOG,"inter1 value in if stmt" + inter1.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial interest value for subvention" + inter1);
}
if (inter1.isEmpty() || inter1 == null || inter1.length() == 4) {
    // Loggers.general().info(LOG,"inter1 value in if stmt" +
    // inter1.length());
    inter1 = "0.00";
    interest = Double.valueOf(inter1);

}
interest = Double.valueOf(inter1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest value for subvention " + interest);
}
String rofInter = getDriverWrapper().getEventFieldAsText("INAR", "s", "");
// Loggers.general().info(LOG,"rofInter value in if stmt" +
// rofInter.length());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Initial rofInter value in if stmt" + rofInter);
}
if (rofInter.isEmpty() || rofInter == null || rofInter.length() == 1) {
    // Loggers.general().info(LOG,"rofInter value in if stmt" +
    // rofInter.length());
    rofInter = "0.00";
    rateinterest = Double.valueOf(rofInter);

}
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"interest Rate of interest value subvention " + rofInter);
}
setROTINT(rofInter);
// Loggers.general().info(LOG,"rateinterest value for subvention " +
// rateinterest);
rateinterest = Double.valueOf(rofInter);
if (rateinterest > 0 || (rateinterest != 0.0)) {
    Double avail = (interest * subvent) / rateinterest;

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Available amount for Subvention " + avail);
    }
    String present = String.format("%.2f", avail);
    // Loggers.general().info(LOG,"present amount for Subvention " +
    // present);

    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"present amount for Subvention " + present);
    }
    setSUBVRBI(present + " " + inr);
    setSUBVCRD(present + " " + inr);
}

try {

    double subCredit = 0;
    double subRBI = 0;
    double subvenCredit_Double = 0;
    double subvenRBI_Double = 0;
    double tenorOld_val = 0;
    double tenorNew_val = 0;
    String tenorOld = getDriverWrapper().getEventFieldAsText("TNR", "i", "");
    String tenorNew = getDriverWrapper().getEventFieldAsText("AMI:B+IT", "i", "");
    if (!tenorOld.equalsIgnoreCase("")) {
          tenorOld_val = Double.valueOf(tenorOld);
    }
    if (!tenorNew.equalsIgnoreCase("")) {
          tenorNew_val = Double.valueOf(tenorNew);
    }
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention amend old tenor" + tenorOld_val + "tenorNew_val===>" + tenorNew_val);
    }
    String subventionCredit = getDriverWrapper().getEventFieldAsText("cBCY", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust
    // subventionCredit" + subventionCredit);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionCredit" + subventionCredit);
    }
    if (!subventionCredit.equalsIgnoreCase("") || subventionCredit != null) {
          subvenCredit_Double = Double.valueOf(subventionCredit);
    }
    String subventionRBI = getDriverWrapper().getEventFieldAsText("cBCW", "v", "m");
    // Loggers.general().info(LOG,"Subvention finexlc adjust subventionRBI"
    // + subventionRBI);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention finexlc amend subventionRBI" + subventionRBI);
    }
    if (!subventionRBI.equalsIgnoreCase("") || subventionRBI != null) {
          subvenRBI_Double = Double.valueOf(subventionRBI);
    }
    con = ConnectionMaster.getConnection();
    String dms = "SELECT ext.SUBVCRD/100,ext.SUBVRBI/100,mas.MASTER_REF,bas.REFNO_PFIX,bas.REFNO_SERL FROM master mas, baseevent bas, extevent ext WHERE mas.KEY97=bas.MASTER_KEY AND bas.KEY97=ext.EVENT AND mas.MASTER_REF = '"
                + masRef + "' AND bas.REFNO_PFIX ='CRE' AND bas.REFNO_SERL =1";
    // Loggers.general().info(LOG,"Subvention Query===>" + dms);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"Subvention Query===>" + dms);
    }
    dmsp = con.prepareStatement(dms);

    dmsr = dmsp.executeQuery();
    if (dmsr.next()) {
          subCredit = dmsr.getDouble(1);
          // Loggers.general().info(LOG,"Subvention credit for create" +
          // subCredit);
          subRBI = dmsr.getDouble(2);
          double totalSubvCredit = subCredit - subvenCredit_Double;
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value--->" + totalSubvCredit);
          }
          double totalSubvRBI = subRBI - subvenRBI_Double;
          DecimalFormat diff = new DecimalFormat("0.00");
          diff.setMaximumFractionDigits(2);
          String FinalSubvCredit = diff.format(totalSubvCredit);
          // Loggers.general().info(LOG,"Subvention credit and RBI value--->"
          // + FinalSubvCredit);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Subvention credit and RBI value final--->" + FinalSubvCredit);
          }
          if (tenorOld_val > tenorNew_val) {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"Subvention credit and RBI value old value greater--->" + tenorOld_val);
                }
                if (totalSubvRBI > 0) {
                      setSUBVDEB(FinalSubvCredit + " " + inr);
                      setSUBVPAY(FinalSubvCredit + " " + inr);
                } else {
                      setSUBVDEB(0 + " " + inr);
                      setSUBVPAY(0 + " " + inr);
                }
          } else {

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,
                                  "Subvention credit and RBI value New value greater in else--->" + tenorNew_val);
                }

                setSUBVPAY(available + " " + inr);
                setSUBVDEB(available + " " + inr);
          }

    }

    else {
          setSUBVPAY(available + " " + inr);
          setSUBVDEB(available + " " + inr);
    }

    if (tenorOld_val < tenorNew_val) {

          try {
                String amanedVal = getDriverWrapper().getEventFieldAsText("AMM:B+DA", "v", "m");
                double amanedValue = Double.valueOf(amanedVal);
                double tenorVal = (tenorNew_val - tenorOld_val) / 365;
                double subVal = subvent / 100;

                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old and value--->" + tenorVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and subvention per--->" + subVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old and amanedValue--->" + amanedValue);
                }

                double totalVal = amanedValue * tenorVal * subVal;
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"New Tenor is greater then old totalVal==>" + totalVal);
                }
                if (totalVal > 0) {
                      String totalValue = String.format("%.2f", totalVal);
                      Loggers.general().info(LOG,"New Tenor is greater then old final value==>" + totalValue);
                      setSUBVRBI(totalValue + " " + inr);
                      setSUBVCRD(totalValue + " " + inr);
                } else {
                      setSUBVRBI(0 + " " + inr);
                      setSUBVCRD(0 + " " + inr);
                }

          } catch (Exception e) {
                Loggers.general().info(LOG,"Exception New Tenor is greater then old--->" + e.getMessage());
          }

    }

} catch (Exception e) {
    Loggers.general().info(LOG,"Exception Subvention to RBI payable===>" + e.getMessage());
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

else if (dmsstr.trim().equalsIgnoreCase("NO")
    && (elisub.trim().equalsIgnoreCase("") || elisub.trim().isEmpty() || elisub == null)) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible query level NO and transaction level blank");
}

setSUBVRBI(available + " " + inr);
setSUBVCRD(available + " " + inr);
setSUBVPAY(available + " " + inr);
setSUBVDEB(available + " " + inr);
String sub = getINTPERE();
sub = "";
setINTPERE(sub);
}

else if (dmsstr.trim().equalsIgnoreCase("NO") && elisub.trim().equalsIgnoreCase("NO")) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible Transaction level NO and query level blank");
}
setSUBVRBI(available + " " + inr);
setSUBVCRD(available + " " + inr);
setSUBVPAY(available + " " + inr);
setSUBVDEB(available + " " + inr);
String sub = getINTPERE();
sub = "";
setINTPERE(sub);
}

else if (dmsstr.trim().equalsIgnoreCase("YES") && elisub.trim().equalsIgnoreCase("NO")) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible query level YES and transaction level blank");
}
setSUBVRBI(available + " " + inr);
setSUBVCRD(available + " " + inr);
setSUBVPAY(available + " " + inr);
setSUBVDEB(available + " " + inr);
String sub = getINTPERE();
sub = "";
setINTPERE(sub);
} else if (dmsstr.trim().equalsIgnoreCase("") || dmsstr.trim().isEmpty() && elisub.trim().equalsIgnoreCase("")
    || elisub.trim().isEmpty()) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention eligible Transaction level blank and query level blank");
}
setSUBVRBI(available + " " + inr);
setSUBVCRD(available + " " + inr);
setSUBVPAY(available + " " + inr);
setSUBVDEB(available + " " + inr);
String sub = getINTPERE();
sub = "";
setINTPERE(sub);
}

String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
    || subproCode.equalsIgnoreCase("HCA")) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention subproduct Code===>" + subproCode);

}

} else {

setELISUB("");
setINTPERE("");
setECSECE("");
setSUBVRBI("0.00 INR");
setSUBVCRD("0.00 INR");
setSUBVPAY("0.00 INR");
setSUBVDEB("0.00 INR");

}

}

public void onSUBVENFINOUTCOLREPclayButton() {

// Loggers.general().info(LOG,"Code is update on 17-09-16");
// Loggers.general().info(LOG,"SUBVEN for Fin Expcol repay");
Double available = 0.00;
String inr = "INR";
String mainMaster = "";
String mainEvent = "";
String dmsstr = "";
String dmsstr1 = "";
String dmsstr2 = "";
String dmsstr3 = "";
String interAdv = getDriverWrapper().getEventFieldAsText("B+OT", "s", "");
// Loggers.general().info(LOG,"Interest type" + interAdv);
mainMaster = getDriverWrapper().getEventFieldAsText("MST", "r", "");
// Loggers.general().info(LOG,"Master type" + mainMaster);
mainEvent = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
// Loggers.general().info(LOG,"event type" + mainEvent);
String preFix = getDriverWrapper().getEventFieldAsText("PFX", "r", "");
// Loggers.general().info(LOG,"preFix" + preFix);
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
try {

// Loggers.general().info(LOG,"SUBVEN for Fin standalone repay");
available = 0.00;
Double principalRepayed = 0.0;
inr = "INR";
String Principcl_Repaid_Amt = "";
int subvenPercentage = 0;
int tenor = 0;
String value_date = "";
String start_Date = "";
String maturity_date = "";
String subvention = getINTPERE();
Principcl_Repaid_Amt = getDriverWrapper().getEventFieldAsText("FPR", "v", "m");
// Loggers.general().info(LOG,"value of Principal Repayed Amount ");
value_date = getDriverWrapper().getEventFieldAsText("RPD", "d", "");
// Loggers.general().info(LOG,"value date is " + value_date);
start_Date = getDriverWrapper().getEventFieldAsText("B+SD", "d", "");
// Loggers.general().info(LOG,"start Date is " + start_Date);
maturity_date = getDriverWrapper().getEventFieldAsText("B+MD", "d", "");
// Loggers.general().info(LOG,"Maturity date is " + maturity_date);
Double subvent = Double.valueOf(subvention);

interAdv = getDriverWrapper().getEventFieldAsText("B+OT", "s", "");
interAdv = interAdv.trim();
// Loggers.general().info(LOG,"Interest type" + interAdv);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Interest type" + interAdv);
}

Boolean abnormal_recovery = getPOSHCL_Name();
Loggers.general().info(LOG,"abnormal_recovery" + abnormal_recovery);

if (interAdv.equalsIgnoreCase("Interest in advance - standard")) {
    // Loggers.general().info(LOG,"Repayment type is Interest in advance -
    // standard");

    // case if recovery is not abnormal, scenario is normal advance
    // repayment
    if (!abnormal_recovery) {
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
          Date maturityDate = format.parse(maturity_date);
          Date valueDate = format.parse(value_date);
          Date startDate = format.parse(start_Date);
          // case if recovery is not abnormal, scenario is normal
          // advance repayment before maturity date,refund is present
          if (valueDate.compareTo(maturityDate) == -1) {
                // Loggers.general().info(LOG,"value Date is less than maturity
                // date");
                long diff = (maturityDate.getTime() - valueDate.getTime());
                tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                // Loggers.general().info(LOG,"tenor val is " + tenor);
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      Loggers.general().info(LOG,"tenor val is " + tenor);
                }
          }
          // case if recovery is not abnormal, scenario is normal
          // advance repayment after maturity date ,no refund
          else {
                tenor = 0;
          }
    }
    // case if recovery is abnormal, scenario is normal advance
    // repayment
    else {
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
          Date maturityDate = format.parse(maturity_date);
          Date startDate = format.parse(start_Date);
          // case if recovery is abnormal, scenario is normal advance
          // repayment after maturity date,refund is for normal for
          // full tenor
          // Loggers.general().info(LOG,"value Date is less than maturity
          // date");
          long diff = (maturityDate.getTime() - startDate.getTime());
          tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
          // Loggers.general().info(LOG,"tenor val is " + tenor);
    }
    // Calculation of Refund Amount
    subvenPercentage = Integer.parseInt(getINTPERE().trim());
    // Loggers.general().info(LOG,"value of interest percentage " +
    // subvenPercentage);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"value of interest percentage " + subvenPercentage);
    }
    principalRepayed = Double.valueOf(Principcl_Repaid_Amt);

    String Subvention_Amount = SimpleIntrestCalculation(principalRepayed, subvenPercentage, tenor);
    Loggers.general().info(LOG,"Interest Subvention_Amount advance" + Subvention_Amount);
    setSUBVPAY(Subvention_Amount + " " + inr);
    setSUBVDEB(Subvention_Amount + " " + inr);

} else if (interAdv.equalsIgnoreCase("Interest in arrears - standard") && (!abnormal_recovery)) {

    // Loggers.general().info(LOG,"enter into Interest in arrears -
    // standard");
    // Tenor Calculation
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    Date maturityDate = format.parse(maturity_date);
    Date valueDate = format.parse(value_date);
    Date startDate = format.parse(start_Date);
    if (valueDate.compareTo(maturityDate) == 1) {
          // Loggers.general().info(LOG,"value Date id Greator than Maturiy
          // Date ");
          long diff = maturityDate.getTime() - startDate.getTime();
          tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
          // Loggers.general().info(LOG,"tenor val is " + tenor);
    } else {
          long diff = valueDate.getTime() - startDate.getTime();
          tenor = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
          // Loggers.general().info(LOG,"tenor val is " + tenor);
    }

    // Calculation of Refund Amount
    subvenPercentage = Integer.parseInt(getINTPERE().trim());
    // Loggers.general().info(LOG,"value of interest percentage " +
    // subvenPercentage);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"value of interest percentage " + subvenPercentage);
    }
    principalRepayed = Double.valueOf(Principcl_Repaid_Amt);

    String Subvention_Amount = SimpleIntrestCalculation(principalRepayed, subvenPercentage, tenor);
    Loggers.general().info(LOG,"Interest Subvention_Amount arrears" + Subvention_Amount);

    setSUBVRBI(Subvention_Amount + " " + inr);
    setSUBVCRD(Subvention_Amount + " " + inr);

}
// case if the interest is monthly consolidation and abnormal
// recovery
else {
    setSUBVRBI("0.00 INR");
    setSUBVCRD("0.00 INR");
    setSUBVPAY("0.00 INR");
    setSUBVDEB("0.00 INR");
    // Loggers.general().info(LOG,"Else Excecuted since it is not Intrest in
    // advance or intrest in arrears");
}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in onSUBVENFINEXCOLREPAYButton " +
// e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Exception in onSUBVENFINEXCOLREPAYButton " + e.getMessage());
}
}

String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
if (subproCode.equalsIgnoreCase("PCR") || subproCode.equalsIgnoreCase("INA")
    || subproCode.equalsIgnoreCase("HCA")) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Subvention subproduct Code===>" + subproCode);

}

} else {

setELISUB("");
setINTPERE("");
setECSECE("");
setSUBVRBI("0.00 INR");
setSUBVCRD("0.00 INR");
setSUBVPAY("0.00 INR");
setSUBVDEB("0.00 INR");

}

}

@Override
public void onpurchasedetailsFSACREclayButton() {
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
String pono = "";
Connection con = null;
PreparedStatement ps = null;
String fincurr = "";
ResultSet res = null;
// String medAmt = "";
String Current_finance_eligible_Amt = "";
String Total_finance_eligible_amt = "";
String currency = "";
String first_time_currfamt = "";
String master = "";
int decimalPlace=0;
try {
master = getDriverWrapper().getEventFieldAsText("MST", "r", "");
BigDecimal Total_finance_eligible = null;
BigDecimal denominator = null;
BigDecimal curval = null;
BigDecimal hundred = new BigDecimal(100);
EnigmaArray<ExtEventPrePurchaseOrderEntityWrapper> list = getExtEventPrePurchaseOrderData();
for (int i = 0; i < list.getSize().intValue(); i++) {
    Iterator<ExtEventPrePurchaseOrderEntityWrapper> iterator1 = list.iterator();
    while (iterator1.hasNext()) {
          ExtEventPrePurchaseOrderEntityWrapper fdwrapper = (ExtEventPrePurchaseOrderEntityWrapper) iterator1
                      .next();
          pono = fdwrapper.getPON().trim();
          if (!pono.equalsIgnoreCase("")) {
                pono = pono.trim();
          }

          con = ConnectionMaster.getConnection();
          String ELIGIBLE_AMOUNT_query = "select TRIM(ELIGIBLE_AMOUNT),TRIM(CURRENCY),TRIM(IMPORTER_NAME),TRIM(CIF_ID) from ett_export_order WHERE  EXPORT_ORDER_NUMBER='"
                      + pono + "' AND STATUS = 'A' AND IMPORTER_NAME IS NOT NULL AND CIF_ID IS NOT NULL";

          System.out.println("ELIGIBLE_AMOUNT_query is--->" + ELIGIBLE_AMOUNT_query);
          ps = con.prepareStatement(ELIGIBLE_AMOUNT_query);
          res = ps.executeQuery();
          while (res.next()) {
                
                fincurr = res.getString(2);
                fdwrapper.setIMPORT(res.getString(3));
                fdwrapper.setCRNO(res.getString(4));
                BigDecimal Total_finance = res.getBigDecimal(1);
                Currency ccyNameCode = Currency.getInstance(fincurr);
                System.out.println("Currency=>"+fincurr);

                decimalPlace = ccyNameCode.getDefaultFractionDigits();

                System.out.println("Integer CCY_decimalPlace \t\t" + decimalPlace + "[" + fincurr + "]");

                double minorValueD = Math.pow(10, decimalPlace);
                System.out.println("minor value "+minorValueD);

                 denominator = BigDecimal.valueOf(minorValueD);

                 System.out.println("BigDecimal CCY_denominatorValue \t" + denominator+" "+Total_finance);

                Loggers.general().info(LOG,"BigDecimal Value from table \t\t" + Total_finance);

                BigDecimal total_finance_eligible_reg = denominator.multiply(Total_finance);

                System.out.println("Multiplied Value Total_finance \t\t" + total_finance_eligible_reg);

                long tifrmt_tot_finc = total_finance_eligible_reg.longValue();

                System.out.println("Long value TIFRMT with CCY \t\t" + tifrmt_tot_finc + " " + fincurr);

                String tifrmtAmount = String.valueOf(tifrmt_tot_finc)+ " " + fincurr;

                System.out.println("String value TIFRMT with CCY \t\t" + tifrmtAmount + " " + fincurr);

                 fdwrapper.setTOFAMT(tifrmtAmount);// set value to TI field
                 System.out.println("getTotalamount "+fdwrapper.getTOFAMT());

                
          /*    BigDecimal Total_finance_eligible_str = hundred.multiply(Total_finance);

                first_time_currfamt = String.valueOf(Total_finance_eligible_str) + " " + res.getString(2);
                fdwrapper.setTOFAMT(first_time_currfamt);
*/
                String query = "select trim(SPOTRATE) as SPOTRATE from spotrate where currency='" + fincurr
                            + "'" +" and BRANCH='MBWW'";
                dmsp = con.prepareStatement(query);

                dmsr = dmsp.executeQuery();
                while (dmsr.next()) {
                      curval = dmsr.getBigDecimal(1);
                      // Loggers.general().info(LOG,"Query value in curval in
                      // while" + curval);
                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                            System.out.println("Query value in curval in while" + curval);
                      }

                }
                
                Total_finance_eligible = curval.multiply(Total_finance);
                
                // Loggers.general().info(LOG,"INR equ amount value in
                // Total_finance_eligible " + Total_finance_eligible);

          }

          String Current_Finance_Eligible_Amount = "select PON,sum(UFINAMT)/100 UFINAMT from (SELECT ext.PON, ext.ccy_2, NVL(ext.UFINAMT,0)*NVL(cy.SPOTRATE,0) UFINAMT FROM EXTEVENTPPO ext, BASEEVENT bev, MASTER mas, SPOTRATE cy WHERE ext.FK_EVENT =bev.EXTFIELD AND mas.KEY97 =bev.MASTER_KEY AND bev.STATUS ='c' AND ext.PON ='"
                      + pono + "' AND mas.ccy =ext.ccy_2 and mas.ccy=cy.currency AND BRANCH='MBWW')  group by PON";

          // Loggers.general().info(LOG,"Current_Finance_Eligible_Amount is "
          // + Current_Finance_Eligible_Amount);
          
          System.out.println("Current_Finance_Eligible_Amount is " + Current_Finance_Eligible_Amount);
          
          ps1 = con.prepareStatement(Current_Finance_Eligible_Amount);
          rs1 = ps1.executeQuery();
          BigDecimal medAmt;
          // Double mAmt = 0.0;
          if (rs1.next()) {

                medAmt = rs1.getBigDecimal(2);

                BigDecimal INRval = Total_finance_eligible.subtract(medAmt);

                BigDecimal INRvaltot = INRval.divide(curval, 2, RoundingMode.HALF_UP);

                BigDecimal INRvalfinal = hundred.multiply(INRvaltot);
                System.out.println("INRval of if "+INRval+" "+INRvaltot+" "+INRvalfinal+" "+medAmt);
                fdwrapper.setCURRFAMT(String.valueOf(INRvaltot + " " + fincurr));

          } else {

                /*BigDecimal INRvalelse = Total_finance_eligible;

                BigDecimal INRvaltot = INRvalelse.divide(curval);

                BigDecimal INRvalfinal = hundred.multiply(INRvaltot);

                fdwrapper.setCURRFAMT(String.valueOf(INRvalfinal + " " + fincurr));
*/
                
                Loggers.general().info(LOG,"iN ELSE"+Total_finance_eligible);
                BigDecimal INRvalelse = Total_finance_eligible;
                System.out.println("INRvalelse "+INRvalelse);

                BigDecimal INRvaltot = INRvalelse.divide(curval);
                Loggers.general().info(LOG,"INRvaltot "+INRvaltot);
                
                

                BigDecimal INRvalfinal = denominator.multiply(INRvaltot);
                Loggers.general().info(LOG,"INRvalfinal "+INRvalfinal);
                
                
                long tifrmt_tot_finc1 = INRvalfinal.longValue();

                System.out.println("Long value TIFRMT with CCY \t\t" + tifrmt_tot_finc1 + " " + fincurr);



                String tifrmtAmount1 = String.valueOf(tifrmt_tot_finc1)+ " " + fincurr;

                System.out.println("String value TIFRMT with CCY \t\t" + tifrmtAmount1 + " " + fincurr);


                fdwrapper.setCURRFAMT(tifrmtAmount1);

          }

    }

}
} catch (Exception e) {
Loggers.general().info(LOG,"Exception is " + e.getMessage());
}

finally {
try {
    if (res != null)
          res.close();
    if (ps != null)
          ps.close();
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

//For CR_209
public void onpurchasedetailsFSACORclayButton(){


String strLog = "Log";
String dailyval_Log = "";
@SuppressWarnings("unchecked")
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
          .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
EXTGENCUSTPROP CodeLog = queryLog.getUnique();
if (CodeLog != null) {

    dailyval_Log = CodeLog.getPropval();
} else {
    // System.out.println("ADDDailyTxnLimit is empty-------->");

}
String pono = "";
Connection con = null;
PreparedStatement ps = null;
String fincurr = "";
ResultSet res = null;
// String medAmt = "";
String Current_finance_eligible_Amt = "";
String Total_finance_eligible_amt = "";
String currency = "";
String first_time_currfamt = "";
String master = "";
int decimalPlace=0;
try {
    master = getDriverWrapper().getEventFieldAsText("MST", "r", "");
    BigDecimal Total_finance_eligible = null;
    BigDecimal denominator = null;
    BigDecimal curval = null;
    BigDecimal hundred = new BigDecimal(100);
    EnigmaArray<ExtEventPrePurchaseOrderEntityWrapper> list = getExtEventPrePurchaseOrderData();
    for (int i = 0; i < list.getSize().intValue(); i++) {
          Iterator<ExtEventPrePurchaseOrderEntityWrapper> iterator1 = list.iterator();
          while (iterator1.hasNext()) {
                ExtEventPrePurchaseOrderEntityWrapper fdwrapper = (ExtEventPrePurchaseOrderEntityWrapper) iterator1
                            .next();
                pono = fdwrapper.getPON().trim();
                if (!pono.equalsIgnoreCase("")) {
                      pono = pono.trim();
                }

                con = ConnectionMaster.getConnection();
                String ELIGIBLE_AMOUNT_query = "select TRIM(ELIGIBLE_AMOUNT),TRIM(CURRENCY),TRIM(IMPORTER_NAME),TRIM(CIF_ID) from ett_export_order WHERE  EXPORT_ORDER_NUMBER='"
                            + pono + "' AND STATUS = 'A' AND IMPORTER_NAME IS NOT NULL AND CIF_ID IS NOT NULL";

                // System.out.println("ELIGIBLE_AMOUNT_query is--->" +
                // ELIGIBLE_AMOUNT_query);
                ps = con.prepareStatement(ELIGIBLE_AMOUNT_query);
                res = ps.executeQuery();
                while (res.next()) {
                      fincurr = res.getString(2);
                      fdwrapper.setIMPORT(res.getString(3));
                      fdwrapper.setCRNO(res.getString(4));

                      BigDecimal Total_finance = res.getBigDecimal(1);
                      
                      Currency ccyNameCode = Currency.getInstance(fincurr);
                      System.out.println("Currency=>"+fincurr);

                      decimalPlace = ccyNameCode.getDefaultFractionDigits();

                      System.out.println("Integer CCY_decimalPlace \t\t" + decimalPlace + "[" + fincurr + "]");

                      double minorValueD = Math.pow(10, decimalPlace);
                      System.out.println("minor value "+minorValueD);

                       denominator = BigDecimal.valueOf(minorValueD);

                      System.out.println("BigDecimal CCY_denominatorValue \t" + denominator);

                      System.out.println("BigDecimal Value from table \t\t" + Total_finance);



                      BigDecimal total_finance_eligible_reg = denominator.multiply(Total_finance);

                      System.out.println("Multiplied Value Total_finance \t\t" + total_finance_eligible_reg);



                      long tifrmt_tot_finc = total_finance_eligible_reg.longValue();

                      System.out.println("Long value TIFRMT with CCY \t\t" + tifrmt_tot_finc + " " + fincurr);



                      String tifrmtAmount = String.valueOf(tifrmt_tot_finc)+ " " + fincurr;

                      System.out.println("String value TIFRMT with CCY \t\t" + tifrmtAmount + " " + fincurr);

                       fdwrapper.setTOFAMT(tifrmtAmount);// set value to TI field
                       System.out.println("getTotalamount "+fdwrapper.getTOFAMT());

                      /*BigDecimal Total_finance_eligible_str = hundred.multiply(Total_finance);

                      first_time_currfamt = String.valueOf(Total_finance_eligible_str) + " " + res.getString(2);
                      fdwrapper.setTOFAMT(first_time_currfamt);
*/
                      String query = "select trim(SPOTRATE) as SPOTRATE from spotrate where currency='" + fincurr
                                  + "'";
                      ps = con.prepareStatement(query);

                      res = ps.executeQuery();
                      while (res.next()) {
                            curval = res.getBigDecimal(1);
                            // System.out.println("Query value in curval in
                            // while" + curval);
                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                  System.out.println("Query value in curval in while" + curval);
                            }

                      }
                      Total_finance_eligible = curval.multiply(Total_finance);
                      // System.out.println("INR equ amount value in
                      // Total_finance_eligible " + Total_finance_eligible);

                }

                String Current_Finance_Eligible_Amount = "select PON,sum(UFINAMT)/100 UFINAMT from (SELECT ext.PON, ext.ccy_2, NVL(ext.UFINAMT,0)*NVL(cy.SPOTRATE,0) UFINAMT FROM EXTEVENTPPO ext, BASEEVENT bev, MASTER mas, SPOTRATE cy WHERE ext.FK_EVENT =bev.EXTFIELD AND mas.KEY97 =bev.MASTER_KEY AND bev.STATUS ='c' AND ext.PON ='"
                            + pono + "' AND mas.ccy =ext.ccy_2 and mas.ccy=cy.currency) group by PON";

                // System.out.println("Current_Finance_Eligible_Amount is "
                // + Current_Finance_Eligible_Amount);
                if (dailyval_Log.equalsIgnoreCase("YES")) {
                      System.out.println("Current_Finance_Eligible_Amount is " + Current_Finance_Eligible_Amount);
                }
                ps = con.prepareStatement(Current_Finance_Eligible_Amount);
                res = ps.executeQuery();
                BigDecimal medAmt;
                // Double mAmt = 0.0;
                if (res.next()) {

                      medAmt = res.getBigDecimal(2);

                      BigDecimal INRval = Total_finance_eligible.subtract(medAmt);

                      BigDecimal INRvaltot = INRval.divide(curval, 2, RoundingMode.HALF_UP);

                      BigDecimal INRvalfinal = hundred.multiply(INRvaltot);

                      fdwrapper.setCURRFAMT(String.valueOf(INRvaltot + " " + fincurr));

                } else {

                      /*BigDecimal INRvalelse = Total_finance_eligible;

                      BigDecimal INRvaltot = INRvalelse.divide(curval);

                      BigDecimal INRvalfinal = hundred.multiply(INRvaltot);

                      fdwrapper.setCURRFAMT(String.valueOf(INRvalfinal + " " + fincurr));
*/
                      
                      System.out.println("iN ELSE"+Total_finance_eligible);
                      BigDecimal INRvalelse = Total_finance_eligible;
                      System.out.println("INRvalelse "+INRvalelse);

                      BigDecimal INRvaltot = INRvalelse.divide(curval);
                      System.out.println("INRvaltot "+INRvaltot);
                      
                      

                      BigDecimal INRvalfinal = denominator.multiply(INRvaltot);
                      System.out.println("INRvalfinal "+INRvalfinal);
                      
                      
                      long tifrmt_tot_finc1 = INRvalfinal.longValue();

                      System.out.println("Long value TIFRMT with CCY \t\t" + tifrmt_tot_finc1 + " " + fincurr);



                      String tifrmtAmount1 = String.valueOf(tifrmt_tot_finc1)+ " " + fincurr;

                      System.out.println("String value TIFRMT with CCY \t\t" + tifrmtAmount1 + " " + fincurr);


                      fdwrapper.setCURRFAMT(tifrmtAmount1);

                }

          }

    }
} catch (Exception e) {
    System.out.println("Exception is " + e.getMessage());
}

finally {
    try {
          if (res != null)
                res.close();
          if (ps != null)
                ps.close();
          if (con != null)
                con.close();
    } catch (SQLException e) {
          // System.out.println("Connection Failed! Check
          // output
          // console");
          e.printStackTrace();
    }
}



}


public void onpurchaseorderFSAADJclayButton() {
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
String pono = "";
Connection con = null;
PreparedStatement ps = null;
String fincurr = "";
ResultSet res = null;
// String medAmt = "";
String Current_finance_eligible_Amt = "";
String Total_finance_eligible_amt = "";
String currency = "";
String first_time_currfamt = "";
String master = "";
int decimalPlace=0;
try {
master = getDriverWrapper().getEventFieldAsText("MST", "r", "");
BigDecimal Total_finance_eligible = null;
BigDecimal denominator = null;
BigDecimal curval = null;
BigDecimal hundred = new BigDecimal(100);
EnigmaArray<ExtEventPrePurchaseOrderEntityWrapper> list = getExtEventPrePurchaseOrderData();
for (int i = 0; i < list.getSize().intValue(); i++) {
    Iterator<ExtEventPrePurchaseOrderEntityWrapper> iterator1 = list.iterator();
    while (iterator1.hasNext()) {
          ExtEventPrePurchaseOrderEntityWrapper fdwrapper = (ExtEventPrePurchaseOrderEntityWrapper) iterator1
                      .next();
          pono = fdwrapper.getPON().trim();
          if (!pono.equalsIgnoreCase("")) {
                pono = pono.trim();
          }

          con = ConnectionMaster.getConnection();
          String ELIGIBLE_AMOUNT_query = "select TRIM(ELIGIBLE_AMOUNT),TRIM(CURRENCY),TRIM(IMPORTER_NAME),TRIM(CIF_ID) from ett_export_order WHERE  EXPORT_ORDER_NUMBER='"
                      + pono + "' AND STATUS = 'A' AND IMPORTER_NAME IS NOT NULL AND CIF_ID IS NOT NULL";

          // Loggers.general().info(LOG,"ELIGIBLE_AMOUNT_query is--->" +
          // ELIGIBLE_AMOUNT_query);
          ps = con.prepareStatement(ELIGIBLE_AMOUNT_query);
          res = ps.executeQuery();
          while (res.next()) {
                fincurr = res.getString(2);
                fdwrapper.setIMPORT(res.getString(3));
                fdwrapper.setCRNO(res.getString(4));

                BigDecimal Total_finance = res.getBigDecimal(1);
                
                Currency ccyNameCode = Currency.getInstance(fincurr);
                Loggers.general().info(LOG,"Currency=>"+fincurr);

                decimalPlace = ccyNameCode.getDefaultFractionDigits();

                Loggers.general().info(LOG,"Integer CCY_decimalPlace \t\t" + decimalPlace + "[" + fincurr + "]");

                double minorValueD = Math.pow(10, decimalPlace);
                Loggers.general().info(LOG,"minor value "+minorValueD);

                 denominator = BigDecimal.valueOf(minorValueD);

                Loggers.general().info(LOG,"BigDecimal CCY_denominatorValue \t" + denominator);

                Loggers.general().info(LOG,"BigDecimal Value from table \t\t" + Total_finance);



                BigDecimal total_finance_eligible_reg = denominator.multiply(Total_finance);

                Loggers.general().info(LOG,"Multiplied Value Total_finance \t\t" + total_finance_eligible_reg);



                long tifrmt_tot_finc = total_finance_eligible_reg.longValue();

                Loggers.general().info(LOG,"Long value TIFRMT with CCY \t\t" + tifrmt_tot_finc + " " + fincurr);



                String tifrmtAmount = String.valueOf(tifrmt_tot_finc)+ " " + fincurr;

                Loggers.general().info(LOG,"String value TIFRMT with CCY \t\t" + tifrmtAmount + " " + fincurr);

                 fdwrapper.setTOFAMT(tifrmtAmount);// set value to TI field
                 Loggers.general().info(LOG,"getTotalamount "+fdwrapper.getTOFAMT());

                /*BigDecimal Total_finance_eligible_str = hundred.multiply(Total_finance);

                first_time_currfamt = String.valueOf(Total_finance_eligible_str) + " " + res.getString(2);
                fdwrapper.setTOFAMT(first_time_currfamt);
*/
                String query = "select trim(SPOTRATE) as SPOTRATE from spotrate where currency='" + fincurr
                            + "'";
                ps = con.prepareStatement(query);

                res = ps.executeQuery();
                while (res.next()) {
                      curval = res.getBigDecimal(1);
                      // Loggers.general().info(LOG,"Query value in curval in
                      // while" + curval);
                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                            Loggers.general().info(LOG,"Query value in curval in while" + curval);
                      }

                }
                Total_finance_eligible = curval.multiply(Total_finance);
                // Loggers.general().info(LOG,"INR equ amount value in
                // Total_finance_eligible " + Total_finance_eligible);

          }

          String Current_Finance_Eligible_Amount = "select PON,sum(UFINAMT)/100 UFINAMT from (SELECT ext.PON, ext.ccy_2, NVL(ext.UFINAMT,0)*NVL(cy.SPOTRATE,0) UFINAMT FROM EXTEVENTPPO ext, BASEEVENT bev, MASTER mas, SPOTRATE cy WHERE ext.FK_EVENT =bev.EXTFIELD AND mas.KEY97 =bev.MASTER_KEY AND bev.STATUS ='c' AND ext.PON ='"
                      + pono + "' AND mas.ccy =ext.ccy_2 and mas.ccy=cy.currency) group by PON";

          // Loggers.general().info(LOG,"Current_Finance_Eligible_Amount is "
          // + Current_Finance_Eligible_Amount);
          if (dailyval_Log.equalsIgnoreCase("YES")) {
                Loggers.general().info(LOG,"Current_Finance_Eligible_Amount is " + Current_Finance_Eligible_Amount);
          }
          ps = con.prepareStatement(Current_Finance_Eligible_Amount);
          res = ps.executeQuery();
          BigDecimal medAmt;
          // Double mAmt = 0.0;
          if (res.next()) {

                medAmt = res.getBigDecimal(2);

                BigDecimal INRval = Total_finance_eligible.subtract(medAmt);

                BigDecimal INRvaltot = INRval.divide(curval, 2, RoundingMode.HALF_UP);

                BigDecimal INRvalfinal = hundred.multiply(INRvaltot);

                fdwrapper.setCURRFAMT(String.valueOf(INRvaltot + " " + fincurr));

          } else {

                /*BigDecimal INRvalelse = Total_finance_eligible;

                BigDecimal INRvaltot = INRvalelse.divide(curval);

                BigDecimal INRvalfinal = hundred.multiply(INRvaltot);

                fdwrapper.setCURRFAMT(String.valueOf(INRvalfinal + " " + fincurr));
*/
                
                Loggers.general().info(LOG,"iN ELSE"+Total_finance_eligible);
                BigDecimal INRvalelse = Total_finance_eligible;
                Loggers.general().info(LOG,"INRvalelse "+INRvalelse);

                BigDecimal INRvaltot = INRvalelse.divide(curval);
                Loggers.general().info(LOG,"INRvaltot "+INRvaltot);
                
                

                BigDecimal INRvalfinal = denominator.multiply(INRvaltot);
                Loggers.general().info(LOG,"INRvalfinal "+INRvalfinal);
                
                
                long tifrmt_tot_finc1 = INRvalfinal.longValue();

                Loggers.general().info(LOG,"Long value TIFRMT with CCY \t\t" + tifrmt_tot_finc1 + " " + fincurr);



                String tifrmtAmount1 = String.valueOf(tifrmt_tot_finc1)+ " " + fincurr;

                Loggers.general().info(LOG,"String value TIFRMT with CCY \t\t" + tifrmtAmount1 + " " + fincurr);


                fdwrapper.setCURRFAMT(tifrmtAmount1);

          }

    }

}
} catch (Exception e) {
Loggers.general().info(LOG,"Exception is " + e.getMessage());
}

finally {
try {
    if (res != null)
          res.close();
    if (ps != null)
          ps.close();
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

public void ondisplayvalFSAAMDclayButton() {

// Loggers.general().info(LOG,"FSA Create button for POD");
String Ifsc = getIFSCCO_Name().trim();
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
if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
try {
    con = ConnectionMaster.getConnection();
    String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
    // Loggers.general().info(LOG,"query for IFSC button" + query);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"query for IFSC button" + query);
    }
    int count = 0;
    ps1 = con.prepareStatement(query);
    dmsr1 = ps1.executeQuery();
    while (dmsr1.next()) {
          // Loggers.general().info(LOG,"Entered while");
          count = dmsr1.getInt(1);
          // Loggers.general().info(LOG,"value of count in while " + count);
    }

    if (count == 0) {

          // Loggers.general().info(LOG,"IFSC Code is Not valid ");
          String ban = getBENBAK();
          String bran = getBENBRN();
          String cit = getBENCITY();
          ban = "";
          bran = "";
          bran = "";
          setBENBAK(ban);
          setBENBRN(bran);
          setBENCITY(bran);

    } else {
          try {
                // Loggers.general().info(LOG,"IFSC Code is valid");
                con = ConnectionMaster.getConnection();
                String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                            + "'";
                // Loggers.general().info(LOG,spq);
                ps1 = con.prepareStatement(spq);
                dmsr1 = ps1.executeQuery();
                while (dmsr1.next()) {
                      setBENBAK((dmsr1.getString(1).trim()));
                      setBENBRN(dmsr1.getString(2).trim());
                      setBENCITY((dmsr1.getString(3).trim()));

                }

          } catch (Exception e) {

                // Loggers.general().info(LOG,"event catch");
          }

    }
} catch (Exception e) {

    // Loggers.general().info(LOG,"event catch");
} finally {
    try {
          if (dmsr != null)
                dmsr.close();
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
// Loggers.general().info(LOG,"IFSC code is blak");
}

}

public void ondisplayvalFSACREclayButton() {

// Loggers.general().info(LOG,"FSA Create button for POD");
String Ifsc = getIFSCCO_Name().trim();
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
if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
try {
    con = ConnectionMaster.getConnection();
    String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
    // Loggers.general().info(LOG,"query for IFSC button" + query);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"query for IFSC button" + query);
    }
    int count = 0;
    ps1 = con.prepareStatement(query);
    dmsr1 = ps1.executeQuery();
    while (dmsr1.next()) {
          // Loggers.general().info(LOG,"Entered while");
          count = dmsr1.getInt(1);
          // Loggers.general().info(LOG,"value of count in while " + count);
    }

    if (count == 0) {

          // Loggers.general().info(LOG,"IFSC Code is Not valid ");
          String ban = getBENBAK();
          String bran = getBENBRN();
          String cit = getBENCITY();
          ban = "";
          bran = "";
          bran = "";
          setBENBAK(ban);
          setBENBRN(bran);
          setBENCITY(bran);

    } else {
          try {
                // Loggers.general().info(LOG,"IFSC Code is valid");
                con = ConnectionMaster.getConnection();
                String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                            + "'";
                // Loggers.general().info(LOG,spq);
                ps1 = con.prepareStatement(spq);
                dmsr1 = ps1.executeQuery();
                while (dmsr1.next()) {
                      setBENBAK((dmsr1.getString(1).trim()));
                      setBENBRN(dmsr1.getString(2).trim());
                      setBENCITY((dmsr1.getString(3).trim()));

                }

          } catch (Exception e) {

                // Loggers.general().info(LOG,"event catch");
          }

    }
} catch (Exception e) {

    // Loggers.general().info(LOG,"event catch");
} finally {
    try {
          if (dmsr != null)
                dmsr.close();
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
// Loggers.general().info(LOG,"IFSC code is blak");
}

}

public void onPreshipFetchINWRMTCUSclayButton() {
// Loggers.general().debug(LOG, "on{}Button:{}",
// "PreshipFetchINWRMTCUSclay", ValidationTexts.METHOD_NOT_IMPLEMENTED);

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

Connection con = null;
PreparedStatement prepareStmt = null;
ResultSet result_loan_emplty = null;
String masref = "";
String eventRef = "";
// int count = 0;
// int k = 0;
int temp_count = 0;
int sequence = 0;
int cc = 0;
ArrayList<String> loans = new ArrayList<String>();
try {

masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
eventRef = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
temp_count = liste.getSize().intValue();
// Loggers.general().info(LOG,"temp value " + temp_count);
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"temp value " + temp_count);
}
if (liste.getSize() == 0) {
    con = ConnectionMaster.getConnection();
    String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE from ett_preshipment_apiserver where masref='"
                + masref + "' and eventref='" + eventRef + "'";
    // Loggers.general().info(LOG,"loan query is " + loan_query);
    if (dailyval_Log.equalsIgnoreCase("YES")) {
          Loggers.general().info(LOG,"loan query is " + loan_query);
    }
    PreparedStatement ps = con.prepareStatement(loan_query);
    ResultSet ress = ps.executeQuery();
    while (ress.next()) {
          loans.add(ress.getString("LOAN"));
          loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                      + ress.getString("CURR").trim());
          loans.add(ress.getString("VDATE"));
          // Loggers.general().info(LOG,"eneteerd while");

    }

    for (int i = 0; i < loans.size() / 3; i++) {
          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
          Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
          ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
          loanvalues.setColumn("DEALREF", loans.get(cc));
          loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
          Date date1 = format.parse(loans.get(cc + 2));
          loanvalues.setVALDATE(date1);

          loanvalues.setNewKey();
          loanvalues.setFk(fkey);
          loanvalues.setSequence(sequence);
          ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                      loanvalues, getDriverWrapper());

          addNewExtEventLoanDetails(loanDetailsWrapper);
          sequence++;
          cc = cc + 3;
          // Loggers.general().info(LOG,"va " + cc);
    }

}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception int preshipment fetch " +
// e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
    Loggers.general().info(LOG,"Exception int preshipment fetch " + e.getMessage());
}
} finally {
ConnectionMaster.surrenderDB(con, prepareStmt, result_loan_emplty);
}

}

}