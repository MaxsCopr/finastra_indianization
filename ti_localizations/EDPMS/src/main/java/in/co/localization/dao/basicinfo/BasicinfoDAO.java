package in.co.localization.dao.basicinfo;
 
import org.apache.log4j.Logger;
 
public class BasicinfoDAO

{

  static BasicinfoDAO dao;

  private static Logger logger = Logger.getLogger(BasicinfoDAO.class

    .getName());

  int userid;

  public static BasicinfoDAO getDAO()

  {

    if (dao == null) {

      dao = new BasicinfoDAO();

    }

    return dao;

  }

}