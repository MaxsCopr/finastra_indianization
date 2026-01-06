package in.co.forwardcontract.bd.basicinfo;

import in.co.forwardcontract.bd.BaseBusinessDelegate;

public class BasicinfoBD
  extends BaseBusinessDelegate
{
  static BasicinfoBD bd;
  public static BasicinfoBD getBD()
  {
    if (bd == null) {
      bd = new BasicinfoBD();
    }
    return bd;
  }
}
