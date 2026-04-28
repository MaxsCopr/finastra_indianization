package com.bs.ett.email.firc;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
 
class EmailNotification$1
  extends Authenticator
{
  EmailNotification$1(EmailNotification paramEmailNotification, String paramString) {}
  protected PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(this.val$EmailUser, "");
  }
}
