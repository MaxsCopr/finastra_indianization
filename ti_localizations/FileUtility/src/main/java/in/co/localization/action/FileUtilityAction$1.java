package in.co.localization.action;
 
import javax.mail.Authenticator;

import javax.mail.PasswordAuthentication;
 
class FileUtilityAction$1

  extends Authenticator

{

  FileUtilityAction$1(FileUtilityAction paramFileUtilityAction, String paramString1, String paramString2) {}

  protected PasswordAuthentication getPasswordAuthentication()

  {

    return new PasswordAuthentication(this.val$EmailUser, 

      this.val$EmailPassword);

  }

}