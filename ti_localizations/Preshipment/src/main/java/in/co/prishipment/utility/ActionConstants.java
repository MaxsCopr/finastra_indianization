package in.co.prishipment.utility;
 
import java.util.Collections;

import java.util.Map;
 
public abstract interface ActionConstants

{

  public static final String SYSTEM = "SYSTEM";

  public static final String APPROVE = "approve";

  public static final String REJECT = "reject";

  public static final String REJECT1 = "REJECTED";

  public static final String ENTERING_METHOD = "Entering Method";

  public static final String EXITING_METHOD = "Exiting Method";

  public static final String Y = "Y";

  public static final String N = "N";

  public static final String FALSE = "false";

  public static final String TRUE = "true";

  public static final String SCREEN_ID = "PS01";

  public static final String ERROR_NAME_CODE = "1";

  public static final String ERROR_MISMATCH_CODE = "0";

  public static final String ERROR_DOCKET_CODE = "2";

  public static final String ERROR_DATE_CODE = "3";

  public static final String URL_QUERY = "SELECT URL FROM CLOSE_URL WHERE NAME=?";

  public static final String URL_HOME = "/home";

  public static final String GET_PRESHIP_QUERY = "select NVL(TRIM(PRESHIPMENT_FIN_BASE_VIEW.MAS_MASTER_REF), ' ') AS MASTER, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE, 'dd-mm-yy'),'dd-mm-yy') as START_DATE, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY, 'dd-mm-yy'),'dd-mm-yy') as DUE_DATE, to_char(PRESHIPMENT_FIN_BASE_VIEW.FNC_FINCE_AMT,'999,999,999,999,999.99') AS LOAN_AMT, to_char(PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S,'999,999,999,999,999.99') AS OUTSTANDING_AMT,MAS_CCY AS CURR,TO_CHAR(PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS) AS STATUS, MAS_KEY97, TRIM(MAS_BHALF_BRN) AS BHALF_BRN , TRIM(MAS_INPUT_BRN) AS INPUT_BRN,FACILITY_TYPE AS FACILITY from PRESHIPMENT_FIN_BASE_VIEW where PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS IN ('LIV')  and PRODUCT_CODE='FSA' AND PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S > 0  AND PRESHIPMENT_FIN_BASE_VIEW.MAS_CCY in ('EUR','INR','GBP','JPY','USD')";

  public static final String INSERT_PRESHIPMENT = "insert into ETT_PRESHIPMENT(CIF_CODE,VALUE_DATE,PAYMENT_AMOUNT,MAS_REF,DISBURSE_DATE,DUE_DATE,LOAN_AMOUNT,OUT_AMOUNT,REPAY_AMOUNT,STATUS,BASEEVENT_KEY97,INPUT_BRANCH,BEHALF_BRANCH,PRODUCT,EVENT,BATCHID,CURRENCY,CUS_MAS_REF,CUS_EVENT_REF) values(?,to_date(?,'DD/MM/YYYY'),TO_NUMBER(?,'999999999999999.99'),?,to_date(?,'DD/MM/YYYY'),to_date(?,'DD/MM/YYYY'),TO_NUMBER(?,'999999999999999.99'),TO_NUMBER(?,'999999999999999.99'),TO_NUMBER(?,'999999999999999.99'),?,?,?,?,?,?,?,?,?,?)";

  public static final String CURRENCY_QUERY = "select C8SCY,C8CUR from C8PF where C8SCY=? order by C8SCY";

  public static final String MASTER = "MASTER";

  public static final String FACILITY = "FACILITY";

  public static final String START_DATE = "START_DATE";

  public static final String DUE_DATE = "DUE_DATE";

  public static final String LOAN_AMT = "LOAN_AMT";

  public static final String OS_AMT = "OUTSTANDING_AMT";

  public static final String STATUS = "STATUS";

  public static final String MAS_KEY97 = "MAS_KEY97";

  public static final String MAS_INPUT_BRN = "INPUT_BRN";

  public static final String MAS_BHALF_BRN = "BHALF_BRN";

  public static final String CURR = "CURR";

  public static final String REP_AMNT = "REPAY_AMNT";

  public static final String PRODUCT = "FSA";

  public static final String EVENT = "RSA";

  public static final String USERTEAM_VAL = "REPAIR";

  public static final String userName = "";

  public static final String SYS_DATE = "SYSDATE";

  public static final String CIF_NULL = "0";

  public static final String AMOUNT_NULL = "1";

  public static final String INVALD_AMOUNT = "2";

  public static final String DATE_NULL = "3";

  public static final String INVALID_DATE = "4";

  public static final String INVALID_OS_AMT = "5";

  public static final String MISMATCH_PAYMENT_REPAYMENT = "6";

  public static final String CURRENCY_NULL = "7";

  public static final String INVALID_CURRENCY = "8";

  public static final String MISMATCH_OS_AMT = "9";

  public static final String PRODUCT_NULL = "10";

  public static final String AMOUNT_MISMATCH = "11";

  public static final String INVALID_VALUE_DATE = "12";

  public static final String INVALID_VALUE_DATE2 = "13";

  public static final String Over_Utilization = "14";

  public static final String CUS_NUM_FILTER_QUERY = "select GFCPNC,GFCUN,GFCUS1 from GFPF where GFCPNC=GFCPNC ";

  public static final String CREATIONDATE = "CreationDate";

  public static final String TRANSACTIONID = "TransactionId";

  public static final String MASTERREFERENCE = "MasterReference";

  public static final String MASREFERENCE = "MasReference";

  public static final String EVENTREFERENCE = "EventReference";

  public static final String FACILITYIDENTIFIER = "FacilityIdentifier";

  public static final String PRODUCTSUBTYPE = "ProductSubType";

  public static final String CUSTOMER = "Customer";

  public static final String INPUTBRANCH = "InputBranch";

  public static final String BEHALFOFBRANCH = "BehalfOfBranch";

  public static final String AMOUNT = "Amount";

  public static final String CURRENCY = "Currency";

  public static final String getOutstanding_amt = "select ETT.MAS_REF, SUM(ETT.REPAY_AMOUNT) AS OUT_AMT  from ETT_PRESHIPMENT ETT ,master MAS,baseevent BEV where trim(ETT.CUS_MAS_REF)=trim(mas.master_ref) and trim(ett.cus_event_ref)=trim(bev.refno_pfix)||lpad(bev.refno_serl,3,0) and mas.key97=bev.master_key  and bev.status in ('c','i')  and ETT.MAS_REF= ? GROUP BY ETT.MAS_REF";

  public static final String getOutstanding_amt_manual = "Select mas.MASTER_REF as MAS_REF,SUM(PAY.PRI_PAID/power(10,C8CED)) AS OUT_AMT from master mas, baseevent bev,FINREPAY pay,C8PF c8  where mas.key97 = bev.MASTER_KEY  AND BEV.KEY97 = pay.KEY97  and bev.status<>'a'  AND C8.C8CCY= BEV.CCY   aND BEV.CREATNMTHD='M'  AND trim(MAS.MASTER_REF)= ?  group by MAS.MASTER_REF ";

  public static final String getLoanAmount = "select fnc.DEAL_AMT/(SELECT power(10,c8.c8ced) as LOAN_AMT  FROM c8pf c8 WHERE c8.c8ccy= fnc.DEAL_CCY) as LOAN_AMOUNT from master mas ,fncemaster fnc where mas.key97=fnc.KEY97 and trim(mas.MASTER_REF) = ?";

  public static final Map<String, String> REC = Collections.unmodifiableMap(new ActionConstants.1());

  public static final Map<String, String> PROREC = Collections.unmodifiableMap(new ActionConstants.2());

}