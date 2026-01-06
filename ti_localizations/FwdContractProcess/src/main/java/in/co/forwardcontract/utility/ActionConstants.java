package in.co.forwardcontract.utility;

import java.util.Collections;
import java.util.Map;
 
public abstract interface ActionConstants
{
  public static final String ENTERING_METHOD = "Entering Method";
  public static final String EXITING_METHOD = "Exiting Method";
  public static final String USERID_QUERY = "select skey80 from secage88 where name85 =?";
  public static final String USER_ID = "skey80";
  public static final String USERID = "USERID";
  public static final String FWC_MAKER = "FWCMAKER";
  public static final String FWC_SP_MAKER = "FWC_SP_MAKER";
  public static final String FWC_CHECKER = "FWCCHECKER";
  public static final String FWC_SP_CHECKER = "FWC_SP_CHECKER";
  public static final String FWC_VIEWER = "FWCVIEWER";
  public static final String MAKER = "maker";
  public static final String CHECKER = "checker";
  public static final String VIEWER = "viewer";
  public static final String MAKER_CHECKER = "both";
  public static final String HOME = "1";
  public static final String ZERO = "0";
  public static final String ONE = "1";
  public static final String TWO = "2";
  public static final String THREE = "3";
  public static final String FOUR = "4";
  public static final String COL_COUNT = "Count";
  public static final String TEAMKEY = "teamkey";
  public static final String FWCBOOK = "FWCBOOK";
  public static final String FWCCANCEL = "FWCCANCEL";
  public static final String BOOK = "Booked";
  public static final String MODIFY = "Modified";
  public static final String SAVE = "Saved";
  public static final String CANCEL = "Cancelled";
  public static final String APPROVE = "Approve";
  public static final String REJECT = "Rejected";
  public static final String FAIL = "Failed";
  public static final String PENDING_APPROVAL = "PENDING FOR APPROVAL";
  public static final String PENDING_SUBMIT = "PENDING TO SUBMIT";
  public static final String APPROVED = "APPROVED";
  public static final String REJECTED = "REJECTED";
  public static final String CANCELLED = "CANCELLED";
  public static final String FAILED = "FAILED";
  public static final String DELETED = "DELETED";
  public static final String userName = "root";
  public static final String password = "root";
  public static final String dbName = "demohab_myshare";
  public static final String url = "jdbc:mysql://localhost:3306/demohab_myshare";
  public static final String driver = "com.mysql.jdbc.Driver";
  public static final Map<String, String> SUBPRODUCT = Collections.unmodifiableMap(new ActionConstants.1());
  public static final Map<String, String> STATUSLIST = Collections.unmodifiableMap(new ActionConstants.2());
}
