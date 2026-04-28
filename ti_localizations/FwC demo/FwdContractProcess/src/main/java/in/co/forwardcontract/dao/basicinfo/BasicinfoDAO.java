package in.co.forwardcontract.dao.basicinfo;

public class BasicinfoDAO
{
  static BasicinfoDAO dao;
  int userid;
  public static BasicinfoDAO getDAO()
  {
    if (dao == null) {
      dao = new BasicinfoDAO();
    }
    return dao;
  }
}
