package in.co.FIRC.vo;

public class UserDetailsVO
{
  String userTitle = null;
  String firstName = null;
  String lastName = null;
  String address = null;
  String country = null;
  String mobileNumber = null;
  String emailId = null;
  String sessionUserName = null;
  int userid;
  public int getUserid()
  {
    return this.userid;
  }
  public void setUserid(int userid)
  {
    this.userid = userid;
  }
  public String getUserTitle()
  {
    return this.userTitle;
  }
  public void setUserTitle(String userTitle)
  {
    this.userTitle = userTitle;
  }
  public String getFirstName()
  {
    return this.firstName;
  }
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }
  public String getLastName()
  {
    return this.lastName;
  }
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }
  public String getAddress()
  {
    return this.address;
  }
  public void setAddress(String address)
  {
    this.address = address;
  }
  public String getCountry()
  {
    return this.country;
  }
  public void setCountry(String country)
  {
    this.country = country;
  }
  public String getMobileNumber()
  {
    return this.mobileNumber;
  }
  public void setMobileNumber(String mobileNumber)
  {
    this.mobileNumber = mobileNumber;
  }
  public String getEmailId()
  {
    return this.emailId;
  }
  public void setEmailId(String emailId)
  {
    this.emailId = emailId;
  }
  public String getSessionUserName()
  {
    return this.sessionUserName;
  }
  public void setSessionUserName(String sessionUserName)
  {
    this.sessionUserName = sessionUserName;
  }
}