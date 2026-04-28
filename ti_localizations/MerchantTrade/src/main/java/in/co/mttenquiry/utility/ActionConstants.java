package in.co.mttenquiry.utility;

import java.util.Collections;
import java.util.Map;
 
public abstract interface ActionConstants
{
  public static final Map<String, String> REC_IND = Collections.unmodifiableMap(new ActionConstants.1());
  public static final String ENTERING_METHOD = "Entering Method";
  public static final String EXITING_METHOD = "Exiting Method";
  public static final String Y = "Y";
  public static final String N = "N";
  public static final String FALSE = "false";
  public static final CharSequence TABLE_NAME = "TABLE_NAME";
  public static final CharSequence COLUMN_NAME1 = "COLUMN_NAME1";
  public static final CharSequence COLUMN_NAME2 = "COLUMN_NAME2";
  public static final String COLUMN_FILE_NAME = "FILE_NAME";
  public static final String COLUMN_STATUS = "STATUS";
  public static final String COLUMN_REFERENCE_NUMBER = "REFERENCE_NUMBER";
  public static final String FAIL = "fail";
  public static final String AUDIT_ACTION_ONE = "1";
  public static final String AUDIT_ACTION_TWO = "2";
  public static final String AUDIT_ACTION_THREE = "3";
  public static final String AUDIT_ACTION_FOUR = "4";
  public static final String AUDIT_ACTION_FIVE = "5";
  public static final String AUDIT_ACTION_SIX = "6";
  public static final String AUDIT_ACTION_SEVEN = "7";
  public static final String AUDIT_ACTION_EIGHT = "8";
  public static final String AUDIT_ACTION_NINE = "9";
  public static final String AUDIT_ACTION_TEN = "10";
  public static final String AUDIT_ACTION_ELEVAN = "11";
  public static final String AUDIT_ACTION_TEWLE = "12";
  public static final String AUDIT_ACTION_THEIRTEEN = "13";
  public static final String REDIRECT = "redirect";
  public static final String GETCLOSEURL = "SELECT TRIM(VALUE1) AS CLOSEURL FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = ?";
}
