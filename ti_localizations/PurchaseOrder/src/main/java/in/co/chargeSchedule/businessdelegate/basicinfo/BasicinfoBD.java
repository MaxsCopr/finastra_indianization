package in.co.chargeSchedule.businessdelegate.basicinfo;

import in.co.chargeSchedule.businessdelegate.BaseBusinessDelegate;
import org.apache.log4j.Logger;
 
public class BasicinfoBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BasicinfoBD.class.getName());
  static BasicinfoBD bd;
  public static BasicinfoBD getBD()
  {
    if (bd == null) {
      bd = new BasicinfoBD();
    }
    return bd;
  }
}
