package in.co.stp.utility;

import java.util.ArrayList;

public class DataGridVO
{
  int sEcho;
  int iTotalRecords;
  int iTotalDisplayRecords;
  ArrayList<Object> aaData;
  int iDisplayLength;
  int iDisplayStart;
  String iSearch;
  String iSearch_1;
  String iSearch_2;
  public int getsEcho()
  {
    return this.sEcho;
  }
  public void setsEcho(int sEcho)
  {
    this.sEcho = sEcho;
  }
  public int getiTotalRecords()
  {
    return this.iTotalRecords;
  }
  public void setiTotalRecords(int iTotalRecords)
  {
    this.iTotalRecords = iTotalRecords;
  }
  public int getiTotalDisplayRecords()
  {
    return this.iTotalDisplayRecords;
  }
  public void setiTotalDisplayRecords(int iTotalDisplayRecords)
  {
    this.iTotalDisplayRecords = iTotalDisplayRecords;
  }
  public ArrayList<Object> getAaData()
  {
    return this.aaData;
  }
  public void setAaData(ArrayList<Object> aaData)
  {
    this.aaData = aaData;
  }
  public int getiDisplayLength()
  {
    return this.iDisplayLength;
  }
  public void setiDisplayLength(int iDisplayLength)
  {
    this.iDisplayLength = iDisplayLength;
  }
  public int getiDisplayStart()
  {
    return this.iDisplayStart;
  }
  public void setiDisplayStart(int iDisplayStart)
  {
    this.iDisplayStart = iDisplayStart;
  }
  public String getiSearch()
  {
    return this.iSearch;
  }
  public void setiSearch(String iSearch)
  {
    this.iSearch = iSearch;
  }
  public String getiSearch_1()
  {
    return this.iSearch_1;
  }
  public void setiSearch_1(String iSearch_1)
  {
    this.iSearch_1 = iSearch_1;
  }
  public String getiSearch_2()
  {
    return this.iSearch_2;
  }
  public void setiSearch_2(String iSearch_2)
  {
    this.iSearch_2 = iSearch_2;
  }
}
