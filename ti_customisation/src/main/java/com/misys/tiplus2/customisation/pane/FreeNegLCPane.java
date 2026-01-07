package com.misys.tiplus2.customisation.pane;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicedetailsLC;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicedetailsLCEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetailslc;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetailslcEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import com.misys.tiplus2.foundations.lang.logging.Loggers;

public class FreeNegLCPane extends EventPane {
      /**
       *
       */
      private static final long serialVersionUID = 1L;
      private static final Logger LOG = LoggerFactory
                  .getLogger(FreeNegLCPane.class);
      Connection con, con1 = null;
      PreparedStatement dmsp, ps, ps1, ps2, prepare1, prepare2, dmsp1, psd, pst,
                  ps3, ps4 = null;
      ResultSet dmsr, rs, rs1, rs2, rs3, rs4, result, result2 = null;
      ResultSet dmsr1, rst = null;

      public void onRTGSFREENEGLCButton() {
            Loggers.general().info(LOG,
                        "Inside onRTGSFreelyNegotiableLCButton Method ");
            if (IFSCFECTH()) {
                  Loggers.general().info(LOG, " IFSCFECTH BUTTON");
            } else {
                  Loggers.general().info(LOG, "IFSCFECTH Else systemOutput");
            }
      }

      public void onIFSCFREENEGLCOUTPclayButton() {
            Loggers.general().info(LOG,
                        "Inside onRTGSFREENEGLCOUTPclayButton Method ");

            if (IFSCFECTH()) {
                  Loggers.general().info(LOG, " IFSCFECTH BUTTON");
            } else {
                  Loggers.general().info(LOG, "IFSCFECTH Else systemOutput");
            }
      }

      public void onFetchpreshipFREENEGLCOUTPclayButton() {

            System.out
                        .println("Inside onFetchpreshipFREENEGLCOUTPclayButton method");

            String MasterReference = getDriverWrapper().getEventFieldAsText("MST",
                        "r", "").trim(); //
            String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
            String evvcount = getDriverWrapper()
                        .getEventFieldAsText("ESQ", "i", "");

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
                  con = ConnectionMaster.getConnection();
                  try {

                        String dms = "SELECT exte.MARAMT, exte.CCY_1 FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT and exte.MARAMT is not null and trim(exte.CCY_1) is not null AND mas.MASTER_REF = '"
                                    + MasterReference
                                    + "' AND bev.REFNO_PFIX = '"
                                    + evnt
                                    + "' AND bev.REFNO_SERL =" + evvcount + "";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,
                                          "Finance  margin query---->" + dms);
                        }
                        psd = con.prepareStatement(dms);
                        rst = psd.executeQuery();
                        if (rst.next()) {
                              String margin = rst.getString(1);
                              String ccy = rst.getString(2);
                              if (margin != null && margin.length() > 0
                                          && ccy.length() > 1) {
                                    setMARAMT(margin + "" + ccy);
                              } else {
                                    setMARAMT("");
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,
                                          "Exception Margin finance amount" + e.getMessage());
                        }
                  }

                  try {
                        getExtEventLoanDetailsNew().setEnabled(false);
                        getExtEventLoanDetailsDelete().setEnabled(false);
                        getExtEventLoanDetailsUpdate().setEnabled(false);
                        getExtEventLoanDetailsUp().setEnabled(false);
                        getExtEventLoanDetailsDown().setEnabled(false);
                        EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                        int count = 0;
                        // Iterator<ExtEventLoanDetailsEntityWrapper> iterator =
                        // liste.iterator();

                        if (liste.getSize() < 1) {
                              String query = "SELECT trim(exte.DEALREF),exte.REAMOUNT,exte.CCY,TO_CHAR(exte.VALDATE,'yyyy-mm-dd') FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENTLRT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.EXTFIELD = exte.FK_EVENT AND mas.MASTER_REF = '"
                                          + MasterReference
                                          + "' AND bev.REFNO_PFIX = '"
                                          + evnt + "' AND bev.REFNO_SERL =" + evvcount + "";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "DPR Loan Details" + query);
                              }

                              psd = con.prepareStatement(query);
                              rst = psd.executeQuery();
                              // // Loggers.general().info(LOG,"executeQuery statement ");
                              while (rst.next()) {
                                    // // Loggers.general().info(LOG,"Enter into while");
                                    Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                    ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                                    loanvalues.setColumn("DEALREF", rst.getString(1));
                                    loanvalues.setColumn("REAMOUNT", rst.getString(2) + ""
                                                + rst.getString(3));
                                    // loanvalues.setColumn("CCY", rst.getString(3));
                                    loanvalues.setColumn("VALDATE", rst.getString(4));
                                    loanvalues.setNewKey();
                                    loanvalues.setFk(fkey);
                                    loanvalues.setSequence(count);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Loan details" + rst.getString(1));
                                          Loggers.general().info(LOG,
                                                      "Repayment amount" + rst.getString(2));
                                          Loggers.general().info(LOG,
                                                      "Value date" + rst.getString(3));
                                    }
                                    getExtEventLoanDetailsNew().setEnabled(false);
                                    getExtEventLoanDetailsDelete().setEnabled(false);
                                    getExtEventLoanDetailsUpdate().setEnabled(false);
                                    getExtEventLoanDetailsUp().setEnabled(false);
                                    getExtEventLoanDetailsDown().setEnabled(false);
                                    getBtnPRESHIPFINELCSETTclay().setEnabled(false);

                                    ExtEventLoanDetailsEntityWrapper projectdetchk = new ExtEventLoanDetailsEntityWrapper(
                                                loanvalues, getDriverWrapper());
                                    addNewExtEventLoanDetails(projectdetchk);

                                    count++;
                              }
                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general()
                                          .info(LOG,
                                                      "Exception LoanDetails population"
                                                                  + e.getMessage());
                        }

                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,
                                    "Exception LoanDetails" + e.getMessage());
                  }

            }

            finally {

                  try {
                        if (rst != null)
                              rst.close();
                        if (psd != null)
                              psd.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // // console");
                        e.printStackTrace();
                  }

            }

      }
      
      //EXTEVENTSLC  Shipping DetailsLc
      public void onFetchShipdetFREENEGLCButton(){

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
            try {
                  EnigmaArray<ExtEventShippingTableEntityWrapper> liste = getExtEventShippingTableData();
                  EnigmaArray<ExtEventShippingdetailslcEntityWrapper> listef = getExtEventShippingdetailslcData();
                  Iterator<ExtEventShippingTableEntityWrapper> iterator = liste.iterator();
                  Iterator<ExtEventShippingdetailslcEntityWrapper> iterator1 = listef.iterator();
                  System.out.println("Iterator before==>");
                  for(ExtEventShippingdetailslcEntityWrapper extship:listef)
                  {
                        System.out.println("Entered into for all");
                        removeExtEventShippingdetailslc(extship);
                  }
                  

                  String iecode = getDriverWrapper().getEventFieldAsText("BEN", "p", "cBBF").trim();
                  String query = "";
                  int count = 0;
                  System.out.println("size==>"+liste.getSize().intValue());

                  Connection connection = null;
            /*    ResultSet rst = null;
                  PreparedStatement ps = null;*/
                  for (int i = 0; i < liste.getSize().intValue(); i++) {
                        if (connection == null) {
                              connection = ConnectionMaster.getConnection();
                        }
                        //Iterator<ExtEventShippingTableEntityWrapper> iterator = liste.iterator();
                        //Iterator<ExtEventShippingdetailslcEntityWrapper> iterator1 = listef.iterator();
                        System.out.println("entered in for");

                        while (iterator.hasNext()) {
                              
                              ExtEventShippingTableEntityWrapper sdwrapper = (ExtEventShippingTableEntityWrapper) iterator.next();
                              ExtEventShippingdetailslcEntityWrapper sdwrapper1 = (ExtEventShippingdetailslcEntityWrapper) iterator1
                                          .next();
                              //// System.out.println("bill number" +
                              //// sdwrapper.getBILLNUM());
                              String billnum = sdwrapper.getBILLNUM().trim();
                              String shipamt = sdwrapper.getSHPAMT();
                              String add_lie = shipamt.replaceAll("[^0-9]", "");
                              //// System.out.println("Shipping bill amount number only" +
                              //// add_lie);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out.println("Shipping bill amount number only" + add_lie);
                              }
                              String portcode = sdwrapper.getPORTCODDD();
                              String shidate = sdwrapper.getBILLDAT();

                              sdwrapper.setIECOD(iecode);
                              if (add_lie.length() > 0) {
                                    String shipcur = sdwrapper.getSHPAMTCurrency();
                                    BigDecimal priceDecimal = new BigDecimal(add_lie);

                                    String ship_final = String.valueOf(priceDecimal);

                                    sdwrapper.setEQUBILL(ship_final + "" + shipcur);
                                    //// System.out.println("Shipping bill amount for set
                                    //// outstanding amount" + ship_final);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Shipping bill amount for set outstanding amount" + ship_final);
                                    }
                                    sdwrapper.setLOUTSAMT(ship_final + "" + shipcur);
                                    sdwrapper.setSHCOLAM(0 + "" + shipcur);

                              } else {
                                    //// System.out.println("Shipping bill amount is
                                    //// empty");
                              }

                              String formNO = sdwrapper.getFORMNUM().trim();
                              //// System.out.println("Shipping form no" + formNO);
                              System.out.println("Shipping bill num" + billnum);
                              System.out.println("Shipping form num" + formNO);
                              System.out.println("Shipping bill num LENGTH" + billnum.length());
                              System.out.println("Shipping form num LENGTH" + formNO.length());


                              int count2 = 0;
                               if (billnum.length() > 0 && formNO.length() > 0) {
                                    /*String query2 = "SELECT DISTINCT CASE WHEN LCFORMNO IN (SELECT elm.LCFORMNO FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.LCFORMNO ='"
                                                + formNO + "' and TO_CHAR(elm.lcbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " AND elm.LCPRTCDE='" + portcode
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.LCFORMNO='"
                                                + formNO + "'  and TO_CHAR(elm.lcbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " AND elm.LCPRTCDE='" + portcode + "'";*/
                                    
                                    

                                     String query2 ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                         + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcBILLNO)    ='"+ billnum +"'"
                                         + " and trim(elm.LCFORMNO)='"+ formNO +"'  and TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.lcprtcde) ='"+ portcode +"'";
                                    
                                    
                                    
                                    // ////System.out.println("Query2 -------->" + query2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 -------->" + query2);
                                    }
                                    //con = ConnectionMaster.getConnection();
                                    try{
                                          if(ps !=null)
                                                ps.close();
                                          if(rst !=null)
                                                rst.close();
                                    }catch(Exception e){
                                          System.out.println("close shp---->"+e.getMessage());
                                    }
                                    ps= connection.prepareStatement(query2);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count2 = rst.getInt(1);
                                          // ////System.out.println("Count value for Invoice
                                          // bill---------->" + count2);
                                    }
                                    // ODC bill no checking
                                    int count_ODC = 0;
                              /*    String query_ODC = "SELECT DISTINCT CASE WHEN SDBILLNO IN (SELECT elm.SDBILLNO FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.SDBILLNO ='"
                                                + billnum
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.SDBILLNO ='"
                                                + billnum + "'";*/
                                    
                                    
                                     String query_ODC ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDBILLNO) ='"+ billnum +"'"
                                  + " and trim(elm.sdformno)='"+ formNO +"'  AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.sdprtcde) ='"+ portcode +"'";
                        
                                    // ////System.out.println("Query2 billnum---------->" +
                                    // query_ODC);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 billnum---------->" + query_ODC);
                                    }
                                    try{
                                          if(ps !=null)
                                                ps.close();
                                          if(rst !=null)
                                                rst.close();
                                    }catch(Exception e){
                                          System.out.println("close shp---->"+e.getMessage());
                                    }
                                    ps = connection.prepareStatement(query_ODC);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count_ODC = rst.getInt(1);
                                          // ////System.out.println("count_ODC value for
                                          // shipping
                                          // bill---------->" + count_ODC);
                                    }

                                    if (count_ODC < 1 && count2 < 1) {
                                    
                          System.out.println("Start of export 1-----");
                                          String queryexport1 = "select DISTINCT shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS SDATE,portcode,TO_CHAR(leodate,'yyyy-mm-dd') AS LDATE,custno,exportagency,exporttype,countrydest,iecode,adcode,formno,RECIND from ett_edpms_shp where shipbillno='"
                                                      + billnum + "' AND trim(IECODE)='" + iecode
                                                      + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "' AND FORMNO='"+formNO+"'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Shipping bill number result SEZ export 1-------->" + query);

                                          }
                                          try{
                                                if(ps !=null)
                                                      ps.close();
                                                if(rst !=null)
                                                      rst.close();
                                          }catch(Exception e){
                                                System.out.println("close shp---->"+e.getMessage());
                                          }
                                          ps = connection.prepareStatement(queryexport1);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventShippingdetailslc shippingdetails = new ExtEventShippingdetailslc();
                                                if (billnum.length() > 0) {
                                                      shippingdetails.setColumn("LCBILLNO", rst.getString(1));
                                                } else {
                                                      // ////System.out.println("LCBILLNO number
                                                      // else");
                                                      shippingdetails.setColumn("LCBILLNO", "");
                                                }
                                                shippingdetails.setColumn("LCBILDAT", rst.getString(2));
                                                System.out.println("lcbildate---" + shippingdetails.getColumn("LCBILDAT"));
                                                System.out.println("date" + rst.getString(2));
                                                shippingdetails.setColumn("LCPRTCDE", rst.getString(3));
                                                // ////System.out.println("PORTCODE" +
                                                // rst.getString(3));
                                                shippingdetails.setColumn("LCLEODAT", rst.getString(4));
                                                shippingdetails.setColumn("LCCUSTNO", rst.getString(5));
                                                shippingdetails.setColumn("LCEXAGNC", rst.getString(6));
                                                shippingdetails.setColumn("LCEXPTYP", rst.getString(7));
                                                shippingdetails.setColumn("LCDESCON", rst.getString(8));
                                                shippingdetails.setColumn("LCIECOD", rst.getString(9));
                                                shippingdetails.setColumn("LCADCOD", rst.getString(10));
                                                shippingdetails.setColumn("LCFORMNO", rst.getString(11));
                                                shippingdetails.setColumn("RECIN", rst.getString(12));
                                                getBtnFetchInvdetELCDPclay().setEnabled(true);
                                                // ////System.out.println("getBtnFetchInvdetELCDPclay
                                                // is enabled in if loop");
                                                shippingdetails.setNewKey();
                                                shippingdetails.setFk(fkey);
                                                shippingdetails.setSequence(count);

                                                ExtEventShippingdetailslcEntityWrapper shippingdwrapper1 = new ExtEventShippingdetailslcEntityWrapper(
                                                            shippingdetails, getDriverWrapper());
                                                addNewExtEventShippingdetailslc(shippingdwrapper1);

                                                count++;

                                          }
                                          
                          System.out.println("Start of export 2");
                                          String queryexport2 = "select DISTINCT shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS SDATE,portcode,TO_CHAR(leodate,'yyyy-mm-dd') AS LDATE,custno,exportagency,exporttype,countrydest,iecode,adcode,formno,RECIND from ETT_EDPMS_SHP_SOFTEX where shipbillno='"
                                                      + billnum + "' AND trim(IECODE)='" + iecode
                                                      + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "' AND FORMNO='"+formNO+"'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Shipping bill number result SEZ export 2-------->" + query);

                                          }
                                          try{
                                                if(ps !=null)
                                                      ps.close();
                                                if(rst !=null)
                                                      rst.close();
                                          }catch(Exception e){
                                                System.out.println("close shp---->"+e.getMessage());
                                          }
                                          ps = connection.prepareStatement(queryexport2);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventShippingdetailslc shippingdetails = new ExtEventShippingdetailslc();
                                                
                                                      shippingdetails.setColumn("LCBILLNO", rst.getString(1));
                                    
                                                shippingdetails.setColumn("LCBILDAT", rst.getString(2));
                                                shippingdetails.setColumn("LCPRTCDE", rst.getString(3));
                                                // ////System.out.println("PORTCODE" +
                                                // rst.getString(3));
                                                shippingdetails.setColumn("LCLEODAT", rst.getString(4));
                                                shippingdetails.setColumn("LCCUSTNO", rst.getString(5));
                                                shippingdetails.setColumn("LCEXAGNC", rst.getString(6));
                                                shippingdetails.setColumn("LCEXPTYP", rst.getString(7));
                                                shippingdetails.setColumn("LCDESCON", rst.getString(8));
                                                shippingdetails.setColumn("LCIECOD", rst.getString(9));
                                                shippingdetails.setColumn("LCADCOD", rst.getString(10));
                                                shippingdetails.setColumn("LCFORMNO", rst.getString(11));
                                                shippingdetails.setColumn("RECIN", rst.getString(12));
                                                getBtnFetchInvdetELCDPclay().setEnabled(true);
                                                // ////System.out.println("getBtnFetchInvdetELCDPclay
                                                // is enabled in if loop");
                                                shippingdetails.setNewKey();
                                                shippingdetails.setFk(fkey);
                                                shippingdetails.setSequence(count);

                                                ExtEventShippingdetailslcEntityWrapper shippingdwrapper1 = new ExtEventShippingdetailslcEntityWrapper(
                                                            shippingdetails, getDriverWrapper());
                                                addNewExtEventShippingdetailslc(shippingdwrapper1);

                                                count++;

                                          }


                                    } else {
                                          System.out
                                                      .println("count_ODC value for shipping bill---------->" + count2 + " " + count_ODC);
                                    }

                                    if (listef.getSize().intValue() == 0) {
                                          getBtnFetchInvdetELCDPclay().setEnabled(false);
                                    } else if (listef.getSize().intValue() > 0) {

                                          getBtnFetchInvdetELCDPclay().setEnabled(true);
                                    }
                              }
                              
                              else if (billnum.length() > 0 && formNO.length()<=0) {
                              /*    String query2 = "SELECT DISTINCT CASE WHEN SDBILLNO IN (SELECT elm.SDBILLNO FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.SDBILLNO ='"
                                                + billnum + "' and TO_CHAR(elm.sdbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " and elm.sdprtcde ='" + portcode
                                                + "' ) THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND elm.SDBILLNO ='"
                                                + billnum + "' and TO_CHAR(elm.sdbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " and elm.sdprtcde ='" + portcode + "'";*/
                                    
                                     String query2 ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDBILLNO) ='"+ billnum +"'"
                                  + " AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.sdprtcde) ='"+ portcode +"'";
                                    
                                    // ////System.out.println("Query2 -------->" + query2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 -------->" + query2);
                                    }
                                    //con = ConnectionMaster.getConnection();
                                    try{
                                          if(ps !=null)
                                                ps.close();
                                          if(rst !=null)
                                                rst.close();
                                    }catch(Exception e){
                                          System.out.println("close shp---->"+e.getMessage());
                                    }
                                    ps = connection.prepareStatement(query2);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count2 = rst.getInt(1);
                                          // ////System.out.println("Count value for Invoice
                                          // bill---------->" + count2);
                                    }
                                    // ODC bill no checking
                                    int count_ODC = 0;
                              /*    String query_ODC = "SELECT DISTINCT CASE WHEN SDBILLNO IN (SELECT elm.SDBILLNO FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.SDBILLNO ='"
                                                + billnum
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.SDBILLNO ='"
                                                + billnum + "'";*/
                                     String query_ODC ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcBILLNO) ='"+ billnum +"'"
                                 + " AND TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('"+shidate+"','DD-MM-YY')) AND trim(elm.lcprtcde) ='"+ portcode +"'";
                        
                                    
                                    /*

*/                                  // ////System.out.println("Query2 billnum---------->" +
                                    // query_ODC);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 billnum---------->" + query_ODC);
                                    }
                                    try{
                                          if(ps !=null)
                                                ps.close();
                                          if(rst !=null)
                                                rst.close();
                                    }catch(Exception e){
                                          System.out.println("close shp---->"+e.getMessage());
                                    }
                                    ps = connection.prepareStatement(query_ODC);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count_ODC = rst.getInt(1);
                                          // ////System.out.println("count_ODC value for
                                          // shipping
                                          // bill---------->" + count_ODC);
                                    }

                                    if (count_ODC < 1 && count2 < 1) {

                                          query = "select DISTINCT shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS SDATE,portcode,TO_CHAR(leodate,'yyyy-mm-dd') AS LDATE,custno,exportagency,exporttype,countrydest,iecode,adcode,formno,RECIND from ett_edpms_shp where shipbillno='"
                                                      + billnum + "' AND trim(IECODE)='" + iecode
                                                      + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Shipping bill number result-------->" + query);

                                          }
                                          try{
                                                if(ps !=null)
                                                      ps.close();
                                                if(rst !=null)
                                                      rst.close();
                                          }catch(Exception e){
                                                System.out.println("close shp---->"+e.getMessage());
                                          }
                                          ps = connection.prepareStatement(query);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventShippingdetailslc shippingdetails = new ExtEventShippingdetailslc();
                                                if (billnum.length() > 0) {
                                                      shippingdetails.setColumn("LCBILLNO", rst.getString(1));
                                                } else {
                                                      // ////System.out.println("LCBILLNO number
                                                      // else");
                                                      shippingdetails.setColumn("LCBILLNO", "");
                                                }
                                                shippingdetails.setColumn("LCBILDAT", rst.getString(2));
                                                shippingdetails.setColumn("LCPRTCDE", rst.getString(3));
                                                // ////System.out.println("PORTCODE" +
                                                // rst.getString(3));
                                                shippingdetails.setColumn("LCLEODAT", rst.getString(4));
                                                shippingdetails.setColumn("LCCUSTNO", rst.getString(5));
                                                shippingdetails.setColumn("LCEXAGNC", rst.getString(6));
                                                shippingdetails.setColumn("LCEXPTYP", rst.getString(7));
                                                shippingdetails.setColumn("LCDESCON", rst.getString(8));
                                                shippingdetails.setColumn("LCIECOD", rst.getString(9));
                                                shippingdetails.setColumn("LCADCOD", rst.getString(10));
                                                shippingdetails.setColumn("LCFORMNO", rst.getString(11));
                                                shippingdetails.setColumn("RECIN", rst.getString(12));
                                                getBtnFetchInvdetELCDPclay().setEnabled(true);
                                                // ////System.out.println("getBtnFetchInvdetELCDPclay
                                                // is enabled in if loop");
                                                shippingdetails.setNewKey();
                                                shippingdetails.setFk(fkey);
                                                shippingdetails.setSequence(count);

                                                ExtEventShippingdetailslcEntityWrapper shippingdwrapper1 = new ExtEventShippingdetailslcEntityWrapper(
                                                            shippingdetails, getDriverWrapper());
                                                addNewExtEventShippingdetailslc(shippingdwrapper1);

                                                count++;

                                          }

                                    } else {
                                          System.out
                                                      .println("count_ODC value for shipping bill---------->" + count2 + " " + count_ODC);
                                    }

                                    if (listef.getSize().intValue() == 0) {
                                          getBtnFetchInvdetELCDPclay().setEnabled(false);
                                    } else if (listef.getSize().intValue() > 0) {

                                          getBtnFetchInvdetELCDPclay().setEnabled(true);
                                    }
                              } else if (formNO.length() > 0&&billnum.length()<=0) {

                              /*    String query2 = "SELECT DISTINCT CASE WHEN LCFORMNO IN (SELECT elm.LCFORMNO FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.LCFORMNO ='"
                                                + formNO + "' and TO_CHAR(elm.lcbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " AND elm.LCPRTCDE='" + portcode
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.LCFORMNO='"
                                                + formNO + "'  and TO_CHAR(elm.lcbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " AND elm.LCPRTCDE='" + portcode + "'";*/
                                    
                                     String query2 ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcformno)  ='"+ formNO +"'"
                                 + " AND TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.lcprtcde) ='"+ portcode +"'";
                        
                                    
                                    // ////System.out.println("Query2 form no-------->" +
                                    // query2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 form no-------->" + query2);
                                    }
                                    try{
                                          if(ps !=null)
                                                ps.close();
                                          if(rst !=null)
                                                rst.close();
                                    }catch(Exception e){
                                          System.out.println("close shp---->"+e.getMessage());
                                    }
                                    //con = ConnectionMaster.getConnection();
                                    ps = connection.prepareStatement(query2);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count2 = rst.getInt(1);
                                          // ////System.out.println("Count value for Invoice
                                          // form
                                          // no---------->" + count2);
                                    }

                                    // ODC FORM NO CHECKING
                                    int count_ODC = 0;
                                    /*String query_ODC = "SELECT DISTINCT CASE WHEN SDFORMNO IN (SELECT elm.SDFORMNO FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.SDFORMNO ='"
                                                + formNO + "' and TO_CHAR(elm.sdbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " and elm.sdprtcde ='" + portcode
                                                + "' ) THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND elm.SDFORMNO ='"
                                                + formNO + "' and TO_CHAR(elm.sdbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " and elm.sdprtcde ='" + portcode + "'";*/
                                    
                                     String query_ODC ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29  "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDFORMNO) ='"+ formNO +"'"
                                  + " AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.sdprtcde) ='"+ portcode +"'";
                                    
                                    // ////System.out.println("Query2 formNO---------->" +
                                    // query_ODC);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 formNO---------->" + query_ODC);
                                    }
                                    try{
                                          if(ps !=null)
                                                ps.close();
                                          if(rst !=null)
                                                rst.close();
                                    }catch(Exception e){
                                          System.out.println("close shp---->"+e.getMessage());
                                    }
                                    ps = connection.prepareStatement(query_ODC);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count_ODC = rst.getInt(1);
                                          // ////System.out.println("count_ODC value for
                                          // shipping
                                          // formNO---------->" + count_ODC);
                                    }

                                    if (count_ODC < 1 && count2 < 1) {
                                          query = "SELECT DISTINCT fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS sofdate, portcode, TO_CHAR(leodate,'yyyy-mm-dd') AS LEODATE, custno, exportagency, exporttype, countrydest, iecode, adcode, formno,RECIND FROM ett_edpms_shp_softex WHERE formno ='"
                                                      + formNO + "' AND trim(IECODE)='" + iecode
                                                      + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Form number result-------->" + query);

                                          }
                                          try{
                                                if(ps !=null)
                                                      ps.close();
                                                if(rst !=null)
                                                      rst.close();
                                          }catch(Exception e){
                                                System.out.println("close shp---->"+e.getMessage());
                                          }

                                          ps = connection.prepareStatement(query);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventShippingdetailslc shippingdetails = new ExtEventShippingdetailslc();
                                                if (billnum.length() > 0) {
                                                      shippingdetails.setColumn("LCBILLNO", rst.getString(1));
                                                } else {
                                                      //// System.out.println("LCBILLNO number
                                                      //// else");
                                                      shippingdetails.setColumn("LCBILLNO", "");
                                                }
                                                shippingdetails.setColumn("LCBILDAT", rst.getString(2));
                                                shippingdetails.setColumn("LCPRTCDE", rst.getString(3));
                                                // ////System.out.println("PORTCODE" +
                                                // rst.getString(3));
                                                shippingdetails.setColumn("LCLEODAT", rst.getString(4));
                                                shippingdetails.setColumn("LCCUSTNO", rst.getString(5));
                                                shippingdetails.setColumn("LCEXAGNC", rst.getString(6));
                                                shippingdetails.setColumn("LCEXPTYP", rst.getString(7));
                                                shippingdetails.setColumn("LCDESCON", rst.getString(8));
                                                shippingdetails.setColumn("LCIECOD", rst.getString(9));
                                                shippingdetails.setColumn("LCADCOD", rst.getString(10));
                                                shippingdetails.setColumn("LCFORMNO", rst.getString(11));
                                                shippingdetails.setColumn("RECIN", rst.getString(12));
                                                getBtnFetchInvdetELCDPclay().setEnabled(true);
                                                // ////System.out.println("getBtnFetchInvdetELCDPclay
                                                // is enabled in if loop");
                                                shippingdetails.setNewKey();
                                                shippingdetails.setFk(fkey);
                                                shippingdetails.setSequence(count);

                                                ExtEventShippingdetailslcEntityWrapper shippingdwrapper1 = new ExtEventShippingdetailslcEntityWrapper(
                                                            shippingdetails, getDriverWrapper());
                                                addNewExtEventShippingdetailslc(shippingdwrapper1);

                                                count++;

                                          }

                                    } else {
                                          //// System.out.println("count_ODC value for form
                                          //// no---------->" + count2 + " " + count_ODC);
                                    }
                                    if (listef.getSize().intValue() == 0) {
                                          getBtnFetchInvdetELCDPclay().setEnabled(false);
                                    } else if (listef.getSize().intValue() > 0) {

                                          getBtnFetchInvdetELCDPclay().setEnabled(true);
                                    }
                              }
                              else
                              {
                                    System.out.println("Not in any if elcpane===");
                              }
                                    
                        }
                        try {
                              if (connection != null)
                                    connection.close();
                        } catch (Exception e) {
                              e.getMessage();
                        }

                  }
                  if (liste.getSize().intValue() > 0) {
                        getBtnFetchShipdetELCDPclay().setEnabled(false);
                  } else {
                        getBtnFetchShipdetELCDPclay().setEnabled(true);
                  }

            } catch (Exception e) {
                  //// System.out.println("Exception for shipping population " +
                  //// e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Exception for shipping population " + e.getMessage());
                  }

            }

            finally {
                  try {

                        if (rst != null)
                              rst.close();
                        if (psd != null)
                              psd.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        //// System.out.println("Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }
      
      }
      
      //Fetch Invoice Details
      public void onFetchInvdetFREENEGLCButton(){
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

            try {

                  EnigmaArray<ExtEventShippingTableEntityWrapper> liste = getExtEventShippingTableData();
                  EnigmaArray<ExtEventShippingdetailslcEntityWrapper> listes = getExtEventShippingdetailslcData();
                  EnigmaArray<ExtEventInvoicedetailsLCEntityWrapper> listef = getExtEventInvoicedetailsLCData();
                  Iterator<ExtEventShippingTableEntityWrapper> iterator = liste.iterator();
                  Iterator<ExtEventInvoicedetailsLCEntityWrapper> iterator1 = listef.iterator();
                  Iterator<ExtEventShippingdetailslcEntityWrapper> iterator2 = listes.iterator();
                  for(ExtEventInvoicedetailsLCEntityWrapper extship:listef)
                  {
                        System.out.println("Entered into for all");
                        removeExtEventInvoicedetailsLC(extship);
                  }
                  int count = 0;
                  String query = "";
                  String formNO = "";
                  String billnum = "";
                  Connection connection = null;
            /*    ResultSet rst = null;
                  PreparedStatement ps = null;*/

                  for (int i = 0; i < listes.getSize().intValue(); i++) {
                        if (connection == null) {
                              connection = ConnectionMaster.getConnection();
                        }

                        while (iterator.hasNext()) {
                              
                  
                              ExtEventShippingTableEntityWrapper sdwrapper = (ExtEventShippingTableEntityWrapper) iterator.next();
                              ExtEventInvoicedetailsLCEntityWrapper sdwrapper1 = (ExtEventInvoicedetailsLCEntityWrapper) iterator1
                                          .next();
                              ExtEventShippingdetailslcEntityWrapper sdwrapper2 = (ExtEventShippingdetailslcEntityWrapper) iterator2
                                          .next();
                              // ////System.out.println("bill number" +
                              // sdwrapper.getBILLNUM());
                              billnum = sdwrapper.getBILLNUM().trim();
                              String portcode = sdwrapper.getPORTCODDD();
                              String shidate = sdwrapper.getBILLDAT();
                              formNO = sdwrapper.getFORMNUM().trim();
                              String billdate = null;
                               if (billnum.length() > 0 && formNO.length()>0) {
                                          System.out.println("Inside sez invoice---");
            /*                                  String query2 = "SELECT DISTINCT CASE WHEN ISHPBILL IN (SELECT elm.ISHPBILL FROM EXTEVENTINL elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND elm.ISHPBILL ='"
                                                            + billnum + "' AND elm.INVPRTCD='" + portcode
                                                            + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTINL elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 and elm.ISHPBILL='"
                                                            + billnum + "' AND elm.INVPRTCD='" + portcode + "' ";
                                                // ////System.out.println("Query2 bill no---------->" +
                                                // query2);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      System.out.println("Query2 bill no---------->" + query2);
                                                }
                                                //con = ConnectionMaster.getConnection();
                                                try{
                                                      if(ps !=null)
                                                            ps.close();
                                                      if(rst !=null)
                                                            rst.close();
                                                }catch(Exception e){
                                                      System.out.println("close inv------>"+e.getMessage());
                                                }
                                                ps = connection.prepareStatement(query2);
                                                rst = ps.executeQuery();
                                                int count2 = 0;
                                                while (rst.next()) {
                                                      count2 = rst.getInt(1);

                                                }
                                                //// System.out.println("ELC Count value for Invoice
                                                //// bill---------->" + count2);

                                                // ODC INVOICE NO CHECKING
                                                int count_ODC = 0;
                                                String query_ODC = "SELECT DISTINCT CASE WHEN ISHPBILL IN (SELECT elm.ISHPBILL FROM EXTEVENTINV elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.ISHPBILL ='"
                                                            + billnum + "' AND elm.INVPRTCD='" + portcode
                                                            + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTINV elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.ISHPBILL='"
                                                            + billnum + "' AND elm.INVPRTCD='" + portcode + "' ";
                                                // ////System.out.println("query_ODC Invoice bill
                                                // no---------->" + query_ODC);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      System.out.println("query_ODC Invoice bill no---------->" + query_ODC);
                                                }
                                                try{
                                                      if(ps !=null)
                                                            ps.close();
                                                      if(rst !=null)
                                                            rst.close();
                                                }catch(Exception e){
                                                      System.out.println("close inv------>"+e.getMessage());
                                                }
                                                ps = connection.prepareStatement(query_ODC);
                                                rst = ps.executeQuery();
                                                while (rst.next()) {
                                                      count_ODC = rst.getInt(1);

                                                }
                                                //// System.out.println("ELC Count value for Invoice
                                                //// bill---------->" + count_ODC);
                                                // Invoice bill getting populate without sp bill
                                                int count3 = 0;
                                                String query3 = "SELECT DISTINCT CASE WHEN LCBILLNO IN (SELECT elm.LCBILLNO FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.LCBILLNO ='"
                                                            + billnum
                                                            + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.LCBILLNO ='"
                                                            + billnum + "'";

                                                // ////System.out.println("Invoice bill getting populate
                                                // without sp bill====>" + query3);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      System.out.println("Invoice bill getting populate without sp bill====>" + query3);
                                                }
                                                try{
                                                      if(ps !=null)
                                                            ps.close();
                                                      if(rst !=null)
                                                            rst.close();
                                                }catch(Exception e){
                                                      System.out.println("close inv------>"+e.getMessage());
                                                }
                                                ps= connection.prepareStatement(query3);
                                                rst = ps.executeQuery();
                                                while (rst.next()) {
                                                      count3 = rst.getInt(1);
                                                      // ////System.out.println("Invoice bill populate
                                                      // without
                                                      // sp bill=====>" + query3);
                                                }

                                                if ((count_ODC < 1 || count_ODC == 0) && (count2 < 1 || count2 == 0)) {
*/
                                                      // query = "select DISTINCT
                                                      // invserialno,invno,TO_CHAR(TO_DATE(invdate),'DD/MM/YY')
                                                      // AS
                                                      // invdate,fobamt,fobcurrcode,frieghtamt,frieghtcurrcode,insamt,inscurrcode,commamt,commcurrcode,disamt,discurrcode,dedamt,dedcurrcode,pkgamt,pkgcurrcode,shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY'),portcode,shipbillno,fileno
                                                      // from ett_edpms_shp_inv where shipbillno='"
                                                      System.out.println("inside sez invoice export 1blaaa----");
                                                      String formNO_b = sdwrapper2.getLCFORMNO().trim();
                                                       String billnum_b = sdwrapper2.getLCBILLNO().trim();
                                                       String portcode_b = sdwrapper2.getLCPORTCODE();
                                                       System.out.println("portcode"+portcode_b);
                                                      System.out.println("billnum_b"+billnum_b);
                                                      
                                                      String queryexport1 = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate,'dd-mm-yyyy'),'yyyy-mm-dd') AS INDATE, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), shipbillno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY'), portcode, shipbillno,formno FROM ett_edpms_shp_inv WHERE shipbillno='"
                                                                  + billnum_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                                  + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "' and formno='" + formNO_b +"'";
                                                      //// System.out.println("Invoice bill fecting==>" +
                                                      //// query);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            System.out.println("Invoice bill fecting sez export 1==>" + queryexport1);
                                                      }
                                                      try{
                                                            if(ps !=null)
                                                                  ps.close();
                                                            if(rst !=null)
                                                                  rst.close();
                                                      }catch(Exception e){
                                                            System.out.println("close inv------>"+e.getMessage());
                                                      }
                                                      ps = connection.prepareStatement(queryexport1);
                                                      rst = ps.executeQuery();
                                                      while (rst.next()) {
                                                            Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                            ExtEventInvoicedetailsLC iinvoicedetails = new ExtEventInvoicedetailsLC();
                                                            iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
                                                            iinvoicedetails.setColumn("INVNO", rst.getString(2));
                                                            iinvoicedetails.setColumn("INVDATE", rst.getString(3));
                                                            iinvoicedetails.setColumn("IFOBAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5),
                                                                                    "T") + rst.getString(5));
                                                            iinvoicedetails.setColumn("CCY", rst.getString(5));
                                                            iinvoicedetails.setColumn("INVFRAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7),
                                                                                    "T") + rst.getString(7));
                                                            iinvoicedetails.setColumn("CCY_1", rst.getString(7));
                                                            iinvoicedetails.setColumn("INSUAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9),
                                                                                    "T") + rst.getString(9));
                                                            iinvoicedetails.setColumn("CCY_2", rst.getString(9));
                                                            iinvoicedetails.setColumn("ICOMMAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11),
                                                                                    "T") + rst.getString(11));
                                                            iinvoicedetails.setColumn("CCY_3", rst.getString(11));
                                                            iinvoicedetails.setColumn("IDISCAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13),
                                                                                    "T") + rst.getString(13));
                                                            iinvoicedetails.setColumn("CCY_4", rst.getString(13));
                                                            iinvoicedetails.setColumn("IDEDUAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15),
                                                                                    "T") + rst.getString(15));
                                                            //// System.out.println("PORTCODE" +
                                                            //// rst.getString(3));
                                                            iinvoicedetails.setColumn("CCY_5", rst.getString(15));
                                                            iinvoicedetails.setColumn("IPKGAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17),
                                                                                    "T") + rst.getString(17));
                                                            iinvoicedetails.setColumn("CCY_6", rst.getString(17));
                                                            iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));
                                                            if (billnum.length() > 0) {
                                                                  // ////System.out.println("ISHPBILL number
                                                                  // else");
                                                                  iinvoicedetails.setColumn("ISHPBILL", rst.getString(21));
                                                            } else {
                                                                  iinvoicedetails.setColumn("ISHPBILL", "");
                                                            }
                                                            iinvoicedetails.setColumn("IFORNO", rst.getString(22));
                                                            iinvoicedetails.setNewKey();
                                                            iinvoicedetails.setFk(fkey);
                                                            iinvoicedetails.setSequence(count);

                                                            ExtEventInvoicedetailsLCEntityWrapper invoicewrapper = new ExtEventInvoicedetailsLCEntityWrapper(
                                                                        iinvoicedetails, getDriverWrapper());
                                                            addNewExtEventInvoicedetailsLC(invoicewrapper);

                                                            count++;

                                                      }
                                                      System.out.println("inside sez invoice expoert 2----");
                                                      String queryexport2 = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate),'yyyy-mm-dd') AS invdate, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY') AS SHIPBILLDATE, portcode, shipbillno, formno FROM ett_edpms_shp_inv_softex WHERE formno='"
                                                                  + formNO_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                                  + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "' and shipbillno='" + billnum_b + "'";
                                                      //// System.out.println("Invoice bill fecting==>" +
                                                      //// query);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            System.out.println("Invoice bill fecting sez export 2==>" + queryexport2);
                                                      }
                                                      try{
                                                            if(ps !=null)
                                                                  ps.close();
                                                            if(rst !=null)
                                                                  rst.close();
                                                      }catch(Exception e){
                                                            System.out.println("close inv------>"+e.getMessage());
                                                      }
                                                      ps = connection.prepareStatement(queryexport2);
                                                      rst = ps.executeQuery();
                                                      while (rst.next()) {
                                                            Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                            ExtEventInvoicedetailsLC iinvoicedetails = new ExtEventInvoicedetailsLC();
                                                            iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
                                                            iinvoicedetails.setColumn("INVNO", rst.getString(2));
                                                            iinvoicedetails.setColumn("INVDATE", rst.getString(3));
                                                            iinvoicedetails.setColumn("IFOBAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5),
                                                                                    "T") + rst.getString(5));
                                                            iinvoicedetails.setColumn("CCY", rst.getString(5));
                                                            iinvoicedetails.setColumn("INVFRAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7),
                                                                                    "T") + rst.getString(7));
                                                            iinvoicedetails.setColumn("CCY_1", rst.getString(7));
                                                            iinvoicedetails.setColumn("INSUAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9),
                                                                                    "T") + rst.getString(9));
                                                            iinvoicedetails.setColumn("CCY_2", rst.getString(9));
                                                            iinvoicedetails.setColumn("ICOMMAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11),
                                                                                    "T") + rst.getString(11));
                                                            iinvoicedetails.setColumn("CCY_3", rst.getString(11));
                                                            iinvoicedetails.setColumn("IDISCAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13),
                                                                                    "T") + rst.getString(13));
                                                            iinvoicedetails.setColumn("CCY_4", rst.getString(13));
                                                            iinvoicedetails.setColumn("IDEDUAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15),
                                                                                    "T") + rst.getString(15));
                                                            //// System.out.println("PORTCODE" +
                                                            //// rst.getString(3));
                                                            iinvoicedetails.setColumn("CCY_5", rst.getString(15));
                                                            iinvoicedetails.setColumn("IPKGAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17),
                                                                                    "T") + rst.getString(17));
                                                            iinvoicedetails.setColumn("CCY_6", rst.getString(17));
                                                            iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));
                                                            if (billnum.length() > 0) {
                                                                  // ////System.out.println("ISHPBILL number
                                                                  // else");
                                                                  iinvoicedetails.setColumn("ISHPBILL", rst.getString(21));
                                                            } else {
                                                                  iinvoicedetails.setColumn("ISHPBILL", "");
                                                            }
                                                            iinvoicedetails.setColumn("IFORNO", rst.getString(22));
                                                            iinvoicedetails.setNewKey();
                                                            iinvoicedetails.setFk(fkey);
                                                            iinvoicedetails.setSequence(count);

                                                            ExtEventInvoicedetailsLCEntityWrapper invoicewrapper = new ExtEventInvoicedetailsLCEntityWrapper(
                                                                        iinvoicedetails, getDriverWrapper());
                                                            addNewExtEventInvoicedetailsLC(invoicewrapper);

                                                            count++;

                                                      }



                                          /*    } else {
                                                      //// System.out.println("Count value Invoice bill
                                                      //// else<===count_ODC===>" + count_ODC + "<>" +
                                                      //// count2);
                                                }*/
                                          }
                              else if (billnum.length() > 0&&formNO.length()<=0) {
                                    /*String query2 = "SELECT DISTINCT CASE WHEN ISHPBILL IN (SELECT elm.ISHPBILL FROM EXTEVENTINL elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND elm.ISHPBILL ='"
                                                + billnum + "' AND elm.INVPRTCD='" + portcode
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTINL elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 and elm.ISHPBILL='"
                                                + billnum + "' AND elm.INVPRTCD='" + portcode + "'";
                                    // ////System.out.println("Query2 bill no---------->" +
                                    // query2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 bill no---------->" + query2);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    psd = con.prepareStatement(query2);
                                    rst = psd.executeQuery();
                                    int count2 = 0;
                                    while (rst.next()) {
                                          count2 = rst.getInt(1);

                                    }
                                    //// System.out.println("ELC Count value for Invoice
                                    //// bill---------->" + count2);

                                    // ODC INVOICE NO CHECKING
                                    int count_ODC = 0;
                                    String query_ODC = "SELECT DISTINCT CASE WHEN ISHPBILL IN (SELECT elm.ISHPBILL FROM EXTEVENTINV elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.ISHPBILL ='"
                                                + billnum + "' AND elm.INVPRTCD='" + portcode
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTINV elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.ISHPBILL='"
                                                + billnum + "' AND elm.INVPRTCD='" + portcode + "'";
                                    // ////System.out.println("query_ODC Invoice bill
                                    // no---------->" + query_ODC);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("query_ODC Invoice bill no---------->" + query_ODC);
                                    }
                                    psd = con.prepareStatement(query_ODC);
                                    rst = psd.executeQuery();
                                    while (rst.next()) {
                                          count_ODC = rst.getInt(1);

                                    }
                                    //// System.out.println("ELC Count value for Invoice
                                    //// bill---------->" + count_ODC);
                                    // Invoice bill getting populate without sp bill
                                    int count3 = 0;
                                    String query3 = "SELECT DISTINCT CASE WHEN LCBILLNO IN (SELECT elm.LCBILLNO FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.LCBILLNO ='"
                                                + billnum
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.LCBILLNO ='"
                                                + billnum + "'";

                                    // ////System.out.println("Invoice bill getting populate
                                    // without sp bill====>" + query3);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Invoice bill getting populate without sp bill====>" + query3);
                                    }
                                    psd = con.prepareStatement(query3);
                                    rst = psd.executeQuery();
                                    while (rst.next()) {
                                          count3 = rst.getInt(1);
                                          // ////System.out.println("Invoice bill populate
                                          // without
                                          // sp bill=====>" + query3);
                                    }

                                    if ((count_ODC < 1 || count_ODC == 0) && (count2 < 1 || count2 == 0)) {
*/
                                     String billnum_b = sdwrapper2.getLCBILLNO().trim();
                                     String portcode_b = sdwrapper2.getLCPORTCODE();
                                     System.out.println("portcode"+portcode_b);
                                    System.out.println("billnum_b"+billnum_b);
                                          // query = "select DISTINCT
                                          // invserialno,invno,TO_CHAR(TO_DATE(invdate),'DD/MM/YY')
                                          // AS
                                          // invdate,fobamt,fobcurrcode,frieghtamt,frieghtcurrcode,insamt,inscurrcode,commamt,commcurrcode,disamt,discurrcode,dedamt,dedcurrcode,pkgamt,pkgcurrcode,shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY'),portcode,shipbillno,fileno
                                          // from ett_edpms_shp_inv where shipbillno='"
                                          query = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate,'dd-mm-yyyy'),'yyyy-mm-dd') AS INDATE, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), shipbillno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY'), portcode, shipbillno, fileno FROM ett_edpms_shp_inv WHERE shipbillno='"
                                                      + billnum_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "'";
                                          //// System.out.println("Invoice bill fecting==>" +
                                          //// query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Invoice bill fecting==>" + query);
                                          }
                                          try{
                                                if(ps !=null)
                                                      ps.close();
                                                if(rst !=null)
                                                      rst.close();
                                          }catch(Exception e){
                                                System.out.println("close inv------>"+e.getMessage());
                                          }
                                          ps= connection.prepareStatement(query);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventInvoicedetailsLC iinvoicedetails = new ExtEventInvoicedetailsLC();
                                                iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
                                                iinvoicedetails.setColumn("INVNO", rst.getString(2));
                                                iinvoicedetails.setColumn("INVDATE", rst.getString(3));
                                                iinvoicedetails.setColumn("IFOBAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5),
                                                                        "T") + rst.getString(5));
                                                iinvoicedetails.setColumn("CCY", rst.getString(5));
                                                iinvoicedetails.setColumn("INVFRAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7),
                                                                        "T") + rst.getString(7));
                                                iinvoicedetails.setColumn("CCY_1", rst.getString(7));
                                                iinvoicedetails.setColumn("INSUAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9),
                                                                        "T") + rst.getString(9));
                                                iinvoicedetails.setColumn("CCY_2", rst.getString(9));
                                                iinvoicedetails.setColumn("ICOMMAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11),
                                                                        "T") + rst.getString(11));
                                                iinvoicedetails.setColumn("CCY_3", rst.getString(11));
                                                iinvoicedetails.setColumn("IDISCAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13),
                                                                        "T") + rst.getString(13));
                                                iinvoicedetails.setColumn("CCY_4", rst.getString(13));
                                                iinvoicedetails.setColumn("IDEDUAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15),
                                                                        "T") + rst.getString(15));
                                                //// System.out.println("PORTCODE" +
                                                //// rst.getString(3));
                                                iinvoicedetails.setColumn("CCY_5", rst.getString(15));
                                                iinvoicedetails.setColumn("IPKGAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17),
                                                                        "T") + rst.getString(17));
                                                iinvoicedetails.setColumn("CCY_6", rst.getString(17));
                                                iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));
                                                if (billnum.length() > 0) {
                                                      // ////System.out.println("ISHPBILL number
                                                      // else");
                                                      iinvoicedetails.setColumn("ISHPBILL", rst.getString(21));
                                                } else {
                                                      iinvoicedetails.setColumn("ISHPBILL", "");
                                                }

                                                iinvoicedetails.setNewKey();
                                                iinvoicedetails.setFk(fkey);
                                                iinvoicedetails.setSequence(count);

                                                ExtEventInvoicedetailsLCEntityWrapper invoicewrapper = new ExtEventInvoicedetailsLCEntityWrapper(
                                                            iinvoicedetails, getDriverWrapper());
                                                addNewExtEventInvoicedetailsLC(invoicewrapper);

                                                count++;

                                          }

                              /*    } else {
                                          //// System.out.println("Count value Invoice bill
                                          //// else<===count_ODC===>" + count_ODC + "<>" +
                                          //// count2);
                                    }*/
                              } else if (formNO.length() > 0 && billnum.length()<=0) {

/*                                  String query2 = "SELECT DISTINCT CASE WHEN IFORNO IN (SELECT elm.IFORNO FROM EXTEVENTINL elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND elm.IFORNO ='"
                                                + formNO + "' AND elm.INVPRTCD='" + portcode
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTINL elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 and elm.IFORNO='"
                                                + formNO + "' AND elm.INVPRTCD='" + portcode + "'";
                                    // ////System.out.println("Invoice form no---------->" +
                                    // query2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Invoice form no==>" + query2);
                                    }
                                    con = ConnectionMaster.getConnection();
                                    psd = con.prepareStatement(query2);
                                    rst = psd.executeQuery();
                                    int count3 = 0;
                                    while (rst.next()) {
                                          count3 = rst.getInt(1);

                                    }
                                    //// System.out.println("ELC Count value for Invoice
                                    //// form no----->" + count3);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("ELC Count value for Invoice form no----->" + count3);
                                    }

                                    // ODC form no checking
                                    int count_ODC = 0;
                                    String query_ODC = "SELECT DISTINCT CASE WHEN IFORNO IN (SELECT elm.IFORNO FROM EXTEVENTINV elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.IFORNO ='"
                                                + formNO + "' AND elm.INVPRTCD='" + portcode
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTINV elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND elm.IFORNO='"
                                                + formNO + "' AND elm.INVPRTCD='" + portcode + "'";
                                    // ////System.out.println("Query2 Invoice form
                                    // no---------->" + query_ODC);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 Invoice form no---------->" + query_ODC);
                                    }
                                    psd = con.prepareStatement(query_ODC);
                                    rst = psd.executeQuery();
                                    while (rst.next()) {
                                          count_ODC = rst.getInt(1);
                                          // ////System.out.println("count_ODC value for
                                          // Invoice
                                          // form no---------->" + count_ODC);
                                    }

                                    //// System.out.println("count_ODC value for Invoice
                                    //// form no---------->" + count_ODC);
                                    // Invoice form getting populate without sp form
                                    int count4 = 0;
                                    String query3 = "SELECT DISTINCT CASE WHEN LCFORMNO IN (SELECT elm.LCFORMNO FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.LCFORMNO ='"
                                                + formNO
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.LCFORMNO='"
                                                + formNO + "'";
                                    // ////System.out.println("Invoice form getting populate
                                    // without sp form===>" + query3);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Invoice form getting populate without sp form===>" + query3);
                                    }
                                    psd = con.prepareStatement(query3);
                                    rst = psd.executeQuery();
                                    while (rst.next()) {
                                          count4 = rst.getInt(1);
                                          // ////System.out.println("Invoice form getting
                                          // populate
                                          // without sp form===>" + count4);
                                    }

                                    if ((count3 < 1 || count3 == 0) && (count_ODC < 1 || count_ODC == 0)) {
*/
                                     String portcode_f = sdwrapper2.getLCPORTCODE();
                                          String formNO_f = sdwrapper2.getLCFORMNO().trim();
                                          System.out.println("portcode"+portcode_f);
                                          System.out.println("formNO_f"+formNO_f);
                                          query = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate),'yyyy-mm-dd') AS invdate, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY') AS SHIPBILLDATE, portcode, shipbillno, formno FROM ett_edpms_shp_inv_softex WHERE formno='"
                                                      + formNO_f + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_f + "'";
                                          //// System.out.println("Softex in Invoice form no
                                          //// for fetching" + query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Softex in Invoice form no for fetching" + query);
                                          }
                                          try{
                                                if(ps !=null)
                                                      ps.close();
                                                if(rst !=null)
                                                      rst.close();
                                          }catch(Exception e){
                                                System.out.println("close inv------>"+e.getMessage());
                                          }

                                          ps = connection.prepareStatement(query);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventInvoicedetailsLC iinvoicedetails = new ExtEventInvoicedetailsLC();
                                                iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
                                                iinvoicedetails.setColumn("INVNO", rst.getString(2));
                                                iinvoicedetails.setColumn("INVDATE", rst.getString(3));
                                                //// System.out.println("INVDATE--->" +
                                                //// rst.getString(3));
                                                iinvoicedetails.setColumn("IFOBAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5),
                                                                        "T") + rst.getString(5));
                                                iinvoicedetails.setColumn("CCY", rst.getString(5));
                                                iinvoicedetails.setColumn("INVFRAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7),
                                                                        "T") + rst.getString(7));
                                                iinvoicedetails.setColumn("CCY_1", rst.getString(7));
                                                iinvoicedetails.setColumn("INSUAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9),
                                                                        "T") + rst.getString(9));
                                                iinvoicedetails.setColumn("CCY_2", rst.getString(9));
                                                iinvoicedetails.setColumn("ICOMMAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11),
                                                                        "T") + rst.getString(11));
                                                iinvoicedetails.setColumn("CCY_3", rst.getString(11));
                                                iinvoicedetails.setColumn("IDISCAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13),
                                                                        "T") + rst.getString(13));
                                                iinvoicedetails.setColumn("CCY_4", rst.getString(13));
                                                iinvoicedetails.setColumn("IDEDUAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15),
                                                                        "T") + rst.getString(15));
                                                iinvoicedetails.setColumn("CCY_5", rst.getString(15));
                                                iinvoicedetails.setColumn("IPKGAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17),
                                                                        "T") + rst.getString(17));
                                                iinvoicedetails.setColumn("CCY_6", rst.getString(17));
                                                iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));

                                                iinvoicedetails.setColumn("ISHPBILL", "");

                                                iinvoicedetails.setColumn("IFORNO", rst.getString(22));
                                                iinvoicedetails.setNewKey();
                                                iinvoicedetails.setFk(fkey);
                                                iinvoicedetails.setSequence(count);

                                                ExtEventInvoicedetailsLCEntityWrapper invoicewrapper = new ExtEventInvoicedetailsLCEntityWrapper(
                                                            iinvoicedetails, getDriverWrapper());
                                                addNewExtEventInvoicedetailsLC(invoicewrapper);

                                                count++;

                                          }

                        /*          } else {
                                          //// System.out.println("Count value for Invoice
                                          //// form no---------->" + count3 + "" + count_ODC);
                                    }*/

                              }
                              else
                              {
                                    System.out.println("Not in any else==>");
                              }
                              
                        }
                        try {
                              if (connection != null)
                                    connection.close();
                        } catch (Exception e) {
                              e.getMessage();
                        }
                  
                  }
                  if (liste.getSize().intValue() > 0)
                        getBtnFetchInvdetELCDPclay().setEnabled(false);

            } catch (Exception e) {
                  //// System.out.println("ELC invoice value exception " +
                  //// e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("ELC invoice value exception " + e.getMessage());
                  }
            } finally {
                  try {
                        if (rst != null)
                              rst.close();
                        if (psd != null)
                              psd.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        //// System.out.println("Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }

      }
      
      //Nostro Fetch in POF
      public void ononfetchFREENEGLCOUTPclayButton(){

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
            try {

                  String inwnum = "";
                  double totalAmt = 0;
                  String balanceValCurrency = ""; // String
                  String balance = "0.0";
                  String creditcur = "";
                  String bank_ADCODE = "";
                  long creditAmount = 0;
                  long balanceAmt = 0;
                  String cif_no = "";
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");

                  try {
                        con = ConnectionMaster.getConnection();
                        EnigmaArray<ExtEventAdvanceTableEntityWrapper> liste = getExtEventAdvanceTableData();
                        for (int i = 0; i < liste.getSize().intValue(); i++) {
                              Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
                              while (iterator1.hasNext()) {
                                    ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
                                                .next();
                                    inwnum = fdwarapper1.getINWARD().trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Inward Remittance Number " + inwnum);
                                    }

                                    if (!inwnum.equalsIgnoreCase("")) {
                                          String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'DD/MM/YY')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
                                                      + inwnum + "'";
                                          System.out
                                                      .println("query for getting all fields in inward Remittance grid " + inwardDetails);
                                          pst = con.prepareStatement(inwardDetails);
                                          rs1 = pst.executeQuery();
                                          if (rs1.next()) {
                                                fdwarapper1.setNAMREM(rs1.getString(1));
                                                fdwarapper1.setDATREM(rs1.getString(2));
                                                fdwarapper1.setCOUNREM(rs1.getString(3));
                                                creditAmount = rs1.getLong(4);
                                                creditcur = rs1.getString(5);
                                                cif_no = rs1.getString(6);

                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                bank_ADCODE = rs1.getString(7);
                                                fdwarapper1.setADVRECB(bank_ADCODE);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      System.out.println("Credit AMount " + creditAmount);
                                                }
                                          } else {
                                                //// System.out.println("entered else since
                                                //// result set value returned nothing");
                                          }

                                          // String BalAmtQuery = "SELECT
                                          // ext.ccy_1,NVL(SUM(ext.amtutil),0) AS TOTAL_AMT"
                                          // + " FROM exteventadv ext, BASEEVENT bev, MASTER
                                          // mas WHERE ext.FK_EVENT = bev.EXTFIELD "
                                          // + " AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS
                                          // IN ('i','c') AND ext.inward ='"
                                          // + inwnum + "' AND mas.MASTER_REF !='" +
                                          // masReference + "' GROUP BY ext.ccy_1 ";

                                          String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF ='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Query for getting Inward Utilized Amount===>" + BalAmtQuery);
                                          }
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
                                                      System.out.println("IRM Clourse Query ---" + closureQuery);
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
                                                      System.out.println("Balance Credit Amount-->" + balanceAmt);
                                                }
                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                if (balanceAmt > 0) {

                                                      balance = String.valueOf(balanceAmt);
                                                      fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + balanceValCurrency);
                                                }
                                          } else {

                                                double irmAmt = 0;
                                                String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      System.out.println("IRM Clourse Query --->" + closureQuery);
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
                                                      System.out.println("Balance Credit Amount-->" + balan_cret);
                                                }

                                                if (balan_cret > 0) {
                                                      String balan_Str = String.valueOf(balan_cret);
                                                      fdwarapper1.setBALANCE(balan_Str + " " + creditcur);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + creditcur);
                                                }

                                                fdwarapper1.setCUSCIFNO(cif_no);

                                          }
                                    } else {
                                          //// System.out.println("entered else since there is
                                          //// no Inward remittance no ");

                                    }

                              }
                        }

                  } catch (Exception e) {

                        //// System.out.println("Inward remittance excepton" +
                        //// e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              System.out.println("Exception Inward remittance" + e.getMessage());
                        }

                  }

            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Exception in Inward remittance ELCPAYLAYOUTButton" + e.getMessage());
                  }
            }

            finally {
                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (pst != null)
                              pst.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        //// System.out.println("Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }
      
            
      }
      //Nostro Fetch in FNF
      
      public void ononfetchFREENEGLCButton(){

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
            try {

                  String inwnum = "";
                  double totalAmt = 0;
                  String balanceValCurrency = ""; // String
                  String balance = "0.0";
                  String creditcur = "";
                  String bank_ADCODE = "";
                  long creditAmount = 0;
                  long balanceAmt = 0;
                  String cif_no = "";
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");

                  try {
                        con = ConnectionMaster.getConnection();
                        EnigmaArray<ExtEventAdvanceTableEntityWrapper> liste = getExtEventAdvanceTableData();
                        for (int i = 0; i < liste.getSize().intValue(); i++) {
                              Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
                              while (iterator1.hasNext()) {
                                    ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
                                                .next();
                                    inwnum = fdwarapper1.getINWARD().trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Inward Remittance Number " + inwnum);
                                    }

                                    if (!inwnum.equalsIgnoreCase("")) {
                                          String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'yyyy-mm-dd')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
                                                      + inwnum + "'";
                                          System.out
                                                      .println("query for getting all fields in inward Remittance grid " + inwardDetails);
                                          pst = con.prepareStatement(inwardDetails);
                                          rs1 = pst.executeQuery();
                                          if (rs1.next()) {
                                                fdwarapper1.setNAMREM(rs1.getString(1));
                                                fdwarapper1.setDATREM(rs1.getString(2));
                                                
                                                System.out.println("date==>"+fdwarapper1.getDATREM());
                                                fdwarapper1.setCOUNREM(rs1.getString(3));
                                                creditAmount = rs1.getLong(4);
                                                creditcur = rs1.getString(5);
                                                cif_no = rs1.getString(6);

                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                bank_ADCODE = rs1.getString(7);
                                                fdwarapper1.setADVRECB(bank_ADCODE);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      System.out.println("Credit AMount " + creditAmount);
                                                }
                                          } else {
                                                //// System.out.println("entered else since
                                                //// result set value returned nothing");
                                          }

                                          // String BalAmtQuery = "SELECT
                                          // ext.ccy_1,NVL(SUM(ext.amtutil),0) AS TOTAL_AMT"
                                          // + " FROM exteventadv ext, BASEEVENT bev, MASTER
                                          // mas WHERE ext.FK_EVENT = bev.EXTFIELD "
                                          // + " AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS
                                          // IN ('i','c') AND ext.inward ='"
                                          // + inwnum + "' AND mas.MASTER_REF !='" +
                                          // masReference + "' GROUP BY ext.ccy_1 ";

                                          String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF ='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Query for getting Inward Utilized Amount===>" + BalAmtQuery);
                                          }
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
                                                      System.out.println("IRM Clourse Query ---" + closureQuery);
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
                                                      System.out.println("Balance Credit Amount-->" + balanceAmt);
                                                }
                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                if (balanceAmt > 0) {

                                                      balance = String.valueOf(balanceAmt);
                                                      fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + balanceValCurrency);
                                                }
                                          } else {

                                                double irmAmt = 0;
                                                String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      System.out.println("IRM Clourse Query --->" + closureQuery);
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
                                                      System.out.println("Balance Credit Amount-->" + balan_cret);
                                                }

                                                if (balan_cret > 0) {
                                                      String balan_Str = String.valueOf(balan_cret);
                                                      fdwarapper1.setBALANCE(balan_Str + " " + creditcur);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + creditcur);
                                                }

                                                fdwarapper1.setCUSCIFNO(cif_no);

                                          }
                                    } else {
                                          //// System.out.println("entered else since there is
                                          //// no Inward remittance no ");

                                    }

                              }
                        }

                  } catch (Exception e) {

                        //// System.out.println("Inward remittance excepton" +
                        //// e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              System.out.println("Exception Inward remittance" + e.getMessage());
                        }

                  }

            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Exception in Inward remittance ELCDOCLAYOUTButton" + e.getMessage());
                  }
            }

            finally {
                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (pst != null)
                              pst.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        //// System.out.println("Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }
      
            
      }
      
      public void onMERCHANTFREENEGADJButton(){

      // String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r",
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
      try {
            con = ConnectionMaster.getConnection();
            String mercht = getDriverWrapper().getEventFieldAsText("cARQ", "l", "").toString();
            String relrefno = getREMERREF().trim();
            String adremno = getINWREMNO();

            int dmT = 0;

            try {

                  String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                              + relrefno + "'";
                  //// System.out.println("Master ref no valid for Export lc" +
                  //// dms);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Master ref no valid for Export lc" + dms);

                  }

                  ps = con.prepareStatement(dms);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        //// System.out.println("while--->");
                        dmT = rs.getInt(1);
                        //// System.out.println("AFTER GET THE VALUE " + dmT);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              System.out.println("AFTER GET THE VALUE " + dmT);

                        }
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {
                                    // ////System.out.println("enter into try");

                                    String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                + relrefno + "'";
                                    //// System.out.println("values fetching Export lc"
                                    //// +
                                    //// query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("values fetching Export lc" + query_dms);

                                    }
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String merdate = rs.getString(1);
                  //                      setMERDUET(merdate);
                                          //// System.out.println("AFTER GET THE VALUE " +
                                          //// dmT);
                                    }

                              } catch (Exception e) {
                                    //// System.out.println(e.getMessage());
                              }

                        } else {
                              //// System.out.println("Merchant trade is not tickec or
                              //// master not valide lc or collection--->" + dmT +
                              //// mercht);
                        }

                  }

            } catch (Exception e) {
                  //// System.out.println("Merchanting details in lc--->" +
                  //// e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Merchanting details in lc--->" + e.getMessage());

                  }
            }

            try {
                  // inward renittance
                  String query_adv = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS InwardNo FROM master mas, BASEEVENT bev, EXTEVENT exte, master mas1, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas1.PRODTYPE = prod.KEY97 AND prod.CODE ='XAR' AND mas1.MASTER_REF ='"
                              + adremno + "'";
                  //// System.out.println("Advance rem no valid for Export lc" +
                  //// query_adv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Advance rem no valid for Export lc" + query_adv);

                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(query_adv);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        //// System.out.println("query_adv AFTER GET THE VALUE " +
                        //// dmT);

                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {

                                    String query_dms = "SELECT TO_CHAR(cpm.RCV_DATE,'DD/MM/YY') AS REV_DATE FROM master mas, CPAYMASTER cpm, PARTYDTLS prt, TIDATAITEM tid WHERE mas.key97 = tid.MASTER_KEY AND tid.KEY97 = prt.KEY97 AND mas.KEY97 = cpm.KEY97 AND prt.ROLE = 'BEN' AND mas.master_ref ='"
                                                + adremno + "'";
                                    //// System.out.println("Advance rem no values
                                    //// fetching
                                    //// Export lc" + query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Advance rem no values fetching Export lc" + query_dms);

                                    }
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String recdate = rs.getString(1);
                                          setADRECDT(recdate);
                                          //// System.out.println("Outward received date
                                          //// fetching " + recdate);

                                          //// System.out.println("systemDate date --->" +
                                          //// recdate);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Outward received date fetching " + recdate);
                                                System.out.println("systemDate date --->" + recdate);

                                          }
                                          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                          Calendar cal = Calendar.getInstance();
                                          int gra = 270;

                                          try {
                                                cal.setTime(sdf.parse(recdate));
                                                // ////System.out.println("expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                String output = sdf.format(cal.getTime());
                                                //// System.out.println("output----->" +
                                                //// output);
                  //                            setMERDUET(output);

                                          } catch (Exception e) {
                                                //// System.out.println("Sight value date
                                                //// --->"
                                                //// + e.getMessage());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      System.out.println("Sight value date --->" + e.getMessage());
                                                }

                                          }

                                    }

                              } catch (Exception e) {
                                    //// System.out.println(e.getMessage());
                              }

                        } else {
                              //// System.out.println("Merchant trade is not ticked or
                              //// master not valide outward --->" + dmT + mercht);
                        }

                  }

            } catch (Exception e) {
                  //// System.out.println("Merchanting details outward--->" +
                  //// e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Merchanting details outward--->" + e.getMessage());
                  }
            }

      } catch (Exception e) {
            //// System.out.println("Merchanting details outward--->" +
            //// e.getMessage());
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  System.out.println("Exception Merchanting details--->" + e.getMessage());
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
                  //// System.out.println("Connection Failed! Check output
                  //// console");
                  e.printStackTrace();
            }}
      }


      public void onPRESHIPFINFREENEGLCOUTPclayButton() {
            
            System.out
            .println("Inside onPRESHIPFINFREENEGLCOUTPclayButton method");

            String MasterReference = getDriverWrapper().getEventFieldAsText("MST",
                        "r", "").trim(); //
            String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
            String evvcount = getDriverWrapper()
                        .getEventFieldAsText("ESQ", "i", "");

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
                  con = ConnectionMaster.getConnection();
                  try {

                        String dms = "SELECT exte.MARAMT, exte.CCY_1 FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT and exte.MARAMT is not null and trim(exte.CCY_1) is not null AND mas.MASTER_REF = '"
                                    + MasterReference
                                    + "' AND bev.REFNO_PFIX = '"
                                    + evnt
                                    + "' AND bev.REFNO_SERL =" + evvcount + "";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,
                                          "Finance  margin query---->" + dms);
                        }
                        psd = con.prepareStatement(dms);
                        rst = psd.executeQuery();
                        if (rst.next()) {
                              String margin = rst.getString(1);
                              String ccy = rst.getString(2);
                              if (margin != null && margin.length() > 0
                                          && ccy.length() > 1) {
                                    setMARAMT(margin + "" + ccy);
                              } else {
                                    setMARAMT("");
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,
                                          "Exception Margin finance amount" + e.getMessage());
                        }
                  }

                  try {
                        getExtEventLoanDetailsNew().setEnabled(false);
                        getExtEventLoanDetailsDelete().setEnabled(false);
                        getExtEventLoanDetailsUpdate().setEnabled(false);
                        getExtEventLoanDetailsUp().setEnabled(false);
                        getExtEventLoanDetailsDown().setEnabled(false);
                        EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                        int count = 0;
                        // Iterator<ExtEventLoanDetailsEntityWrapper> iterator =
                        // liste.iterator();

                        if (liste.getSize() < 1) {
                              String query = "SELECT trim(exte.DEALREF),exte.REAMOUNT,exte.CCY,TO_CHAR(exte.VALDATE,'yyyy-mm-dd') FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENTLRT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.EXTFIELD = exte.FK_EVENT AND mas.MASTER_REF = '"
                                          + MasterReference
                                          + "' AND bev.REFNO_PFIX = '"
                                          + evnt + "' AND bev.REFNO_SERL =" + evvcount + "";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "DPR Loan Details" + query);
                              }

                              psd = con.prepareStatement(query);
                              rst = psd.executeQuery();
                              // // Loggers.general().info(LOG,"executeQuery statement ");
                              while (rst.next()) {
                                    // // Loggers.general().info(LOG,"Enter into while");
                                    Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                    ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                                    loanvalues.setColumn("DEALREF", rst.getString(1));
                                    loanvalues.setColumn("REAMOUNT", rst.getString(2) + ""
                                                + rst.getString(3));
                                    // loanvalues.setColumn("CCY", rst.getString(3));
                                    loanvalues.setColumn("VALDATE", rst.getString(4));
                                    loanvalues.setNewKey();
                                    loanvalues.setFk(fkey);
                                    loanvalues.setSequence(count);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Loan details" + rst.getString(1));
                                          Loggers.general().info(LOG,
                                                      "Repayment amount" + rst.getString(2));
                                          Loggers.general().info(LOG,
                                                      "Value date" + rst.getString(3));
                                    }
                                    getExtEventLoanDetailsNew().setEnabled(false);
                                    getExtEventLoanDetailsDelete().setEnabled(false);
                                    getExtEventLoanDetailsUpdate().setEnabled(false);
                                    getExtEventLoanDetailsUp().setEnabled(false);
                                    getExtEventLoanDetailsDown().setEnabled(false);
                                    getBtnPRESHIPFINELCSETTclay().setEnabled(false);

                                    ExtEventLoanDetailsEntityWrapper projectdetchk = new ExtEventLoanDetailsEntityWrapper(
                                                loanvalues, getDriverWrapper());
                                    addNewExtEventLoanDetails(projectdetchk);

                                    count++;
                              }
                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general()
                                          .info(LOG,
                                                      "Exception LoanDetails population"
                                                                  + e.getMessage());
                        }

                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,
                                    "Exception LoanDetails" + e.getMessage());
                  }

            }

            finally {

                  try {
                        if (rst != null)
                              rst.close();
                        if (psd != null)
                              psd.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        // // Loggers.general().info(LOG,"Connection Failed! Check
                        // output
                        // // console");
                        e.printStackTrace();
                  }

            }

      }

      public boolean IFSCFECTH() {
            boolean value = false;
            // // Loggers.general().info(LOG,"Exp lc button for POD");
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
                        String query = "select count(*) from EXTIFSCC where IFSC='"
                                    + Ifsc + "'";
                        Loggers.general().info(LOG, "query for IFSC button" + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general()
                                          .info(LOG, "query for IFSC button" + query);
                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              Loggers.general().info(LOG, "Entered while");
                              count = rs1.getInt(1);
                              Loggers.general().info(LOG,
                                          "value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "value of count in while " + count);
                              }
                        }

                        if (count == 0) {

                              Loggers.general().info(LOG, "IFSC Code is Not valid ");
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

                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='"
                                                + Ifsc + "'";
                                    // // Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          setBENBAK((rs1.getString(1).trim()));
                                          setBENBRN(rs1.getString(2).trim());
                                          setBENCITY((rs1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    Loggers.general().info(LOG, "event catch");
                              }

                        }
                  } catch (Exception e) {

                        Loggers.general().info(LOG, "event catch in IFSC Fetch");
                  } finally {
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
            }
            return value;

      }

}