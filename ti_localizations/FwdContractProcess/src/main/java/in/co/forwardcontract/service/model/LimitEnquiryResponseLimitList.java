package in.co.forwardcontract.service.model;

import java.util.ArrayList;

public class LimitEnquiryResponseLimitList
{
  private ArrayList<LimitEnquiryResponseLimitDetails> UserLimitDetails;
  private LimitEnquiryResponseLimitDetailsUML UML;
  public ArrayList<LimitEnquiryResponseLimitDetails> getUserLimitDetails()
  {
    return this.UserLimitDetails;
  }
  public void setUserLimitDetails(ArrayList<LimitEnquiryResponseLimitDetails> userLimitDetails)
  {
    this.UserLimitDetails = userLimitDetails;
  }
  public LimitEnquiryResponseLimitDetailsUML getUML()
  {
    return this.UML;
  }
  public void setUML(LimitEnquiryResponseLimitDetailsUML uML)
  {
    this.UML = uML;
  }
}
