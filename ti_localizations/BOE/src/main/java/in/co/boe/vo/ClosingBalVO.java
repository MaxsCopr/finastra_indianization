package in.co.boe.vo;

import java.io.File;

public class ClosingBalVO
{
  private String date;
  private String accNo;
  private String closBal;
  private File inputFile;
  public String getDate()
  {
    return this.date;
  }
  public void setDate(String date)
  {
    this.date = date;
  }
  public String getAccNo()
  {
    return this.accNo;
  }
  public void setAccNo(String accNo)
  {
    this.accNo = accNo;
  }
  public String getClosBal()
  {
    return this.closBal;
  }
  public void setClosBal(String closBal)
  {
    this.closBal = closBal;
  }
  public File getInputFile()
  {
    return this.inputFile;
  }
  public void setInputFile(File inputFile)
  {
    this.inputFile = inputFile;
  }
}