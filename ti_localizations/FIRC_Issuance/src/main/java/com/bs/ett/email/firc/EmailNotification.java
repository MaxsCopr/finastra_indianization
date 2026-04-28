package com.bs.ett.email.firc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class EmailNotification
{
  private static Logger logger = LogManager.getLogger(EmailNotification.class.getName());
  public static void main(String[] args) {}
  public boolean sendEmailNotification(String service, String operation, String masterRef, String eventRef, String customerId, String emailSubject, String emailBodyText, String attachFileName, String[] toEmailIds, String[] ccEmailIds, String[] bccEmailIds, byte[] attachmentData, String customerCRN)
  {
    logger.info("FIRC email process initiated new");

 
 
    boolean mailStatus = true;
    String errorDesc = "";
    String hostNodeName = "";
    String ccEmailIdsLogList = "";
    String toEmailIdsLogList = "";
    String bccEmailIdsLogList = "";
    Timestamp processTime = null;
    Timestamp tiReqTime = null;
    Timestamp bankReqTime = null;
    Timestamp bankResTime = null;
    Timestamp tiResTime = null;
    try
    {
      hostNodeName = NotificationUtil.getCurrentHost();
      logger.info("HostNodeName: " + hostNodeName);
      tiReqTime = NotificationUtil.getSqlLocalTimestamp();
      HashMap<String, String> bridgePropertiesMap = new HashMap();
      bridgePropertiesMap = NotificationUtil.getBridgePropertiesConfigMap();
      String SMTPHost = (String)bridgePropertiesMap.get(NotificationUtil.SMTP_HOST);
      String SMTPPort = (String)bridgePropertiesMap.get(NotificationUtil.SMTP_PORT);
      String EmailUser = (String)bridgePropertiesMap.get(NotificationUtil.EMAIL_USER);
      String EmailPassword = "";

 
      Properties props = System.getProperties();
      props.put("mail.smtp.host", SMTPHost);
      props.put("mail.smtp.port", SMTPPort);
      props.put("mail.smtp.auth", Boolean.valueOf(false));
      Session session = Session.getInstance(props, new EmailNotification.1(this, EmailUser));

 
 
 
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(EmailUser));
      if (toEmailIds != null)
      {
        InternetAddress[] toAddress = new InternetAddress[toEmailIds.length];
        for (int i = 0; i < toEmailIds.length; i++)
        {
          toAddress[i] = new InternetAddress(toEmailIds[i].trim());
          logger.info("SetTO >-->>" + toAddress[i] + "<<--<");
          message.addRecipient(Message.RecipientType.TO, toAddress[i]);
          if (i == 0) {
            toEmailIdsLogList = toEmailIds[i].toString();
          } else {
            toEmailIdsLogList = toEmailIdsLogList + "," + toAddress[i];
          }
        }
      }
      else
      {
        toEmailIdsLogList = "No recipient email addresses. TO address is empty";
        logger.info(toEmailIdsLogList);
      }
      if (ccEmailIds != null)
      {
        InternetAddress[] ccAddress = new InternetAddress[ccEmailIds.length];
        for (int i = 0; i < ccEmailIds.length; i++)
        {
          ccAddress[i] = new InternetAddress(ccEmailIds[i].trim());
          logger.info("SetCC >-->>" + ccAddress[i] + "<<--<");
          message.addRecipient(Message.RecipientType.CC, ccAddress[i]);
          if (i == 0) {
            ccEmailIdsLogList = ccAddress[i].toString();
          } else {
            ccEmailIdsLogList = ccEmailIdsLogList + "," + ccAddress[i];
          }
        }
      }
      if (bccEmailIds != null)
      {
        InternetAddress[] bccAddress = new InternetAddress[bccEmailIds.length];
        for (int i = 0; i < bccEmailIds.length; i++)
        {
          bccAddress[i] = new InternetAddress(bccEmailIds[i].trim());
          logger.info("SetBCC >-->>" + bccAddress[i] + "<<--<");
          message.addRecipient(Message.RecipientType.BCC, bccAddress[i]);
          bccEmailIdsLogList = bccEmailIdsLogList + "," + bccAddress[i];
        }
      }
      message.setSubject(emailSubject);
      message.setSentDate(new Date());
      Multipart multiPart = new MimeMultipart();
      BodyPart textPart = new MimeBodyPart();

 
 
 
      String emailBody = emailBodyText.replaceAll("(\r\n|\n)", "<br />");
      String htmlBodyContent = "<html><head><style>html,body{height:297mm;width:210mm;}</style></head><body><p style=\"font-size:15px; color:#1f497d; font-family: Calibri;text-align:left\">" + 
        emailBody + 
        "</p></body></html>";
      textPart.setContent(htmlBodyContent, "text/html; charset=utf-8");
      multiPart.addBodyPart(textPart);
      if (attachmentData != null)
      {
        MimeBodyPart attachFiles = new MimeBodyPart();
        attachFiles.setFileName(attachFileName);
        attachFiles.setContent(attachmentData, "application/pdf");
        multiPart.addBodyPart(attachFiles);
        logger.info("FIRC Document attached successfully! " + masterRef + "-" + eventRef);
      }
      else
      {
        logger.info("FIRC Attchment data is null");
      }
      message.setContent(multiPart);
      bankReqTime = NotificationUtil.getSqlLocalTimestamp();
      Transport.send(message);
      mailStatus = true;
      bankResTime = NotificationUtil.getSqlLocalTimestamp();
      logger.info("Milestone 10S FIRC Email Sent successfully! " + masterRef + "-" + eventRef);
    }
    catch (Exception e)
    {
      errorDesc = e.getMessage();
      logger.error("Milestone 10F FIRC EMail sending failed (" + masterRef + "-" + eventRef + ") " + errorDesc);
      e.printStackTrace();
      mailStatus = false;
    }
    finally
    {
      processTime = NotificationUtil.getSqlLocalTimestamp();
      doServiceLogging(service, operation, "ZONE1", "", "ZONE1", "SMTP", masterRef, eventRef, 
        mailStatus, processTime, toEmailIdsLogList, ccEmailIdsLogList, bccEmailIdsLogList, emailSubject, 
        emailBodyText, attachFileName, tiReqTime, bankReqTime, bankResTime, tiResTime, errorDesc, 
        hostNodeName, customerCRN);
    }
    logger.info("Milestone 10L FIRC Final : " + mailStatus);
    return mailStatus;
  }
  public static int doServiceLogging(String service, String operation, String zone, String branch, String sourceSystem, String targetSystem, String masterRef, String eventRef, boolean sendMailResponse, Timestamp processtime, String toEmailIds, String ccEmailIds, String bccEmailListId, String emailSubject, String emailBodyText, String fileName, Timestamp tiReqTime, Timestamp bankReqTime, Timestamp bankResTime, Timestamp tiResTime, String errDescription, String hostNodeName, String customerCRN)
  {
    logger.info("FIRC ServiceLogging initiated new");
    int result = 0;
    String status = "";
    Connection conn = null;
    PreparedStatement pst = null;
    try
    {
      if (sendMailResponse) {
        status = "SUCCEEDED";
      } else if (!sendMailResponse) {
        status = "FAILED";
      }
      String bankresponse = status;
      String tirequest = "FileName: " + fileName;

 
      String tiresponse = "DummyDmsDocId~" + emailSubject + "~" + emailBodyText;
      String bankrequest = "CustomerCRN: " + customerCRN + "\n\nTOEmailIds: " + toEmailIds + "\nCcMailListId : " + 
        ccEmailIds;
      String serviceLoginsertQuery = "INSERT INTO SERVICELOG (ID, SERVICE, OPERATION, ZONE, BRANCH, SOURCESYSTEM, TARGETSYSTEM, MASTERREFERENCE, EVENTREFERENCE,  TIREQUEST, TIRESPONSE, BANKREQUEST, BANKRESPONSE, STATUS, PROCESSTIME, TIREQTIME, BANKREQTIME, BANKRESTIME, TIRESTIME, DESCRIPTION, NODE, TRANSACTIONKEY1, STATICKEY1 ) VALUES (servicelog_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

 
      logger.info("FIRCServiceLoggingQuery: " + serviceLoginsertQuery);
      conn = DatabaseUtility.getWiseConnection();
      pst = conn.prepareStatement(serviceLoginsertQuery);
      pst.setString(1, service);
      pst.setString(2, operation);
      pst.setString(3, zone);
      pst.setString(4, branch);
      pst.setString(5, sourceSystem);
      pst.setString(6, targetSystem);
      pst.setString(7, masterRef);
      pst.setString(8, eventRef);
      pst.setString(9, tirequest);
      pst.setString(10, tiresponse);
      pst.setString(11, bankrequest);
      pst.setString(12, bankresponse);
      pst.setString(13, status);
      pst.setTimestamp(14, processtime);
      pst.setTimestamp(15, tiReqTime);
      pst.setTimestamp(16, tiResTime);
      pst.setTimestamp(17, bankReqTime);
      pst.setTimestamp(18, bankResTime);
      pst.setString(19, errDescription);
      pst.setString(20, hostNodeName);
      pst.setString(21, null);
      pst.setString(22, null);
      result = pst.executeUpdate();
      logger.info("FIRC ServiceLogging completed " + result);
    }
    catch (Exception e)
    {
      logger.info("FIRC ServiceLogging failed (" + masterRef + "-" + eventRef + ") " + result);
      logger.error("FIRC WiseConnect.Servicelog exception! " + e.getMessage());
      e.printStackTrace();
      int i = result;return i;
    }
    finally
    {
      DatabaseUtility.surrenderConnection(conn, pst, null);
    }
    return result;
  }
}
