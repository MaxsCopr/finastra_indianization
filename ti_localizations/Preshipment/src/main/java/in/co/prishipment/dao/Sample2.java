package in.co.prishipment.dao;
 
import java.text.SimpleDateFormat;

import java.util.Date;
 
public class Sample2

{

  public static void main(String[] args)

  {

    PreShipmentDAO dao = null;

    try

    {

      dao = PreShipmentDAO.getDAO();

      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

      String val_date = "24/05/2018";

      String proc_date = dao.getValueDate().trim();

 
      Date date1 = format.parse(val_date);

      Date date2 = format.parse(proc_date);

      date1.compareTo(date2);

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

  }

}