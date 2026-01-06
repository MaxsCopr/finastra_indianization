package com.swift.in;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.rest.util.CommonMethods;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet({"/SwiftInProcess"})
public class SwiftInProcess
  extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String queueResult = "failed";
    String filePath = "/ftrade/FTI_Repositories/Localisation_logs/Localization/ThemeBridge/logs/WebswiftIn";
    try
    {
      String swiftMessage = request.getParameter("swiftInMsg");
      System.out.println("Push Swift IN Message ---> " + swiftMessage);
      String sequence = CommonMethods.getSqlLocalDateTime().toString();
      sequence = sequence.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
      sequence = sequence.substring(0, sequence.indexOf("."));
      CommonMethods.writeFile(filePath + sequence + ".txt", swiftMessage);
      CommonMethods.getProperties();

 
 
      int port = CommonMethods.StringtoInt((String)CommonMethods.TBProperties.get("Test_OutMQPort")).intValue();
      String hostname = (String)CommonMethods.TBProperties.get("Test_OutMQHostName");
      String channel = (String)CommonMethods.TBProperties.get("Test_OutMQChannelName");
      String qManager = (String)CommonMethods.TBProperties.get("Test_OutMQManagerName");
      String inputQName = (String)CommonMethods.TBProperties.get("Test_InMQName");
      queueResult = writeMQMessage(swiftMessage, port, hostname, channel, qManager, inputQName);
      if (!queueResult.equalsIgnoreCase("failed"))
      {
        System.out.println("Queue Result for SWIFT IN : " + queueResult);
        response.sendRedirect("success.jsp");
      }
      else
      {
        response.sendRedirect("fail.jsp");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("Exception in SwiftOut Process--->" + e.getMessage());
      response.sendRedirect("fail.jsp");
    }
  }
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }
  public static String writeMQMessage(String inputMessage, int port, String hostname, String channel, String qManager, String outputQName)
  {
    MQQueueManager _queueManager = null;
    String errormsg = "";String swiftResp = "";
    String status = "";
    try
    {
      com.ibm.mq.MQEnvironment.hostname = hostname;
      com.ibm.mq.MQEnvironment.channel = channel;
      com.ibm.mq.MQEnvironment.port = port;
      System.out.println("Message read from queue-->" + inputMessage);
      try
      {
        System.out.println("Swift IN Queue manager connection check process");
        _queueManager = new MQQueueManager(qManager);
      }
      catch (MQException e)
      {
        System.out.println("Swift outexception" + e.getMessage());
        e.printStackTrace();
        status = "FAILED";
        errormsg = e.getMessage();
        swiftResp = status + "|" + errormsg;
      }
      int lineNum = 0;
      int openOptions = 8208;
      try
      {
        MQQueue queue = _queueManager.accessQueue(outputQName, openOptions, null, null, null);
        DataInputStream input = new DataInputStream(System.in);
        MQMessage sendmsg = new MQMessage();
        sendmsg.format = "MQSTR   ";
        sendmsg.feedback = 0;
        sendmsg.messageType = 8;
        sendmsg.replyToQueueName = "ROGER.QUEUE";
        sendmsg.replyToQueueManagerName = qManager;
        MQPutMessageOptions pmo = new MQPutMessageOptions();

 
        String line = inputMessage;
        sendmsg.clearMessage();
        sendmsg.messageId = MQC.MQMI_NONE;
        sendmsg.correlationId = MQC.MQCI_NONE;
        sendmsg.writeString(line);

 
 
        queue.put(sendmsg, pmo);

 
        queue.close();
        _queueManager.disconnect();
        System.out.println("MQ connection Disconnted!!!!!!!!!!!!!");
        status = "SUCCEEDED";
        swiftResp = status;
      }
      catch (MQException mqex)
      {
        mqex.printStackTrace();
        System.out.println("MQException______________" + mqex);
        status = "FAILED";
        errormsg = mqex.getMessage();
        swiftResp = status + "|" + errormsg;
      }
      catch (IOException ioex)
      {
        ioex.printStackTrace();
        System.out.println("An MQ IO error occurred : " + ioex);
        status = "FAILED";
        errormsg = ioex.getMessage();
        swiftResp = status + "|" + errormsg;
      }
      catch (Exception e)
      {
        e.printStackTrace();
        System.out.println("An MQ IO error occurred : " + e);
        status = "FAILED";
        errormsg = "MQ Server Connection Error";
        swiftResp = status + "|" + errormsg;
      }
      System.out.println("MQ connect Ended");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("An MQ IO error occurred : " + e);
      status = "FAILED";
      errormsg = "MQ Server Connection Error";
      swiftResp = status + "|" + errormsg;
    }
    return swiftResp;
  }
}
