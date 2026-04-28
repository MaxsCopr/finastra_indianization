package in.co.ebrc.utility;

public abstract interface ActionConstantsQuery
{
  public static final String userName = "root";
  public static final String password = "root";
  public static final String dbName = "demohab_myshare";
  public static final String url = "jdbc:mysql://localhost:3306/demohab_myshare";
  public static final String driver = "com.mysql.jdbc.Driver";
  public static final String gettingPartPaymentSINumber = "SELECT PD_PART_PAY_REF FROM ETTV_BOE_PAYMENT_DETAILS WHERE PD_TXN_REF=?";
  public static final String gettingDataFromTIMultiPayments = "SELECT NVL(TRIM(PD_CIF_NAME), ' '), NVL(TRIM(PD_IE_CODE), ' '), NVL(TRIM(PD_TXN_REF), ' '), NVL(TRIM(PD_PART_PAY_REF), ' '), TO_CHAR(TO_DATE(PD_TXN_DT, 'dd-mm-yy'),'dd-mm-yyyy'), NVL(TRIM(PD_TXN_CCY), ' '), NVL(TRIM(to_char(PD_FC_AMT,'999,999,999,999,999.99')), '0'),NVL(TRIM(to_char(PD_ENDORSED_AMT,'999,999,999,999,999.99')), '0'), NVL(TRIM(to_char(PD_OUTSTANDING_AMT,'999,999,999,999,999.99')), '0') FROM ETTV_BOE_PAYMENT_DETAILS WHERE PD_CIF_ID =? and PD_TXN_CCY=?";
}
