package in.co.FIRC.dao;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
 
class FIRCOurBankDAO$1
  extends Authenticator
{
  FIRCOurBankDAO$1(FIRCOurBankDAO paramFIRCOurBankDAO, String paramString) {}
  protected PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(this.val$EmailUser, "");
  }
}
