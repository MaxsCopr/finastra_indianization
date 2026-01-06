package com.swift.in;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.rest.util.CommonMethods;
import com.rest.util.MapTokenResolver;
import com.rest.util.RequestResponseTemplate;
import com.rest.util.TIPlusEJBClient;
import com.rest.util.TokenReplacingReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
 
public class swiftInProcessPull
{
  private MQQueueManager _queueManager = null;
  private MQQueue queue = null;
  private String swiftINMessage = "Failed";
  private String TIRequest = "Failed";
  private String TIResponse = "Failed";
  public HashMap<String, String> fetchSwiftFromQueue()
    throws MQException
  {
    System.out.println(" Entering fetchSwiftFromQueue ");
    CommonMethods.getProperties();
    HashMap<String, String> result = new HashMap();
    try
    {
      String inputQName = (String)CommonMethods.TBProperties.get("Test_InMQName");
      int openOptions = 8226;
      this.queue = this._queueManager.accessQueue(inputQName, openOptions, null, null, null);
      System.out.println("MQRead v1.0 connected.\n");
      int depth = this.queue.getCurrentDepth();
      System.out.println("Current depth: " + depth + "\n");
      if (depth == 0) {
        return result;
      }
      MQGetMessageOptions getOptions = new MQGetMessageOptions();
      getOptions.options = 24576;
      for (;;)
      {
        MQMessage message = new MQMessage();
        try
        {
          this.queue.get(message, getOptions);
          byte[] b = new byte[message.getMessageLength()];
          message.readFully(b);
          System.out.println("Swift Message from Mqueue-->" + new String(b));
          this.swiftINMessage = new String(b);
          pushToTI(new String(b), (String)CommonMethods.TBProperties.get("Test_BootstrapURL"));
          message.clearMessage();
        }
        catch (IOException e)
        {
          System.out.println("IOException during GET: " + e);
        }
        catch (MQException e)
        {
          if ((e.completionCode == 2) && (e.reasonCode == 2033))
          {
            if (depth > 0) {
              System.out.println("All messages read.");
            }
          }
          else {
            System.out.println("GET Exception: " + e);
          }
        }
      }
      this.queue.close();
      this._queueManager.disconnect();
      result.put("swiftINMessage", this.swiftINMessage);
      result.put("TIRequest", this.TIRequest);
      result.put("TIResponse", this.TIResponse);
      System.out.println(" Exiting fetchSwiftFromQueue ");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      if ((this.queue != null) && (this._queueManager != null))
      {
        this.queue.close();
        this._queueManager.disconnect();
      }
    }
    return result;
  }
  public void pushToTI(String swiftMsg, String bootstrapURL)
  {
    Map<String, String> swifinprocMap = new HashMap();
    InputStream anInputStream = null;
    System.out.println("Entering pushToTI ");
    try
    {
      if (CommonMethods.isValidString(swiftMsg))
      {
        anInputStream = 
          swiftInProcessPull.class.getClassLoader().getResourceAsStream(RequestResponseTemplate.TI_REQUEST_SWIFT_IN);
        String templateXml = CommonMethods.readFileInpStream(anInputStream);

 
        Map<String, String> tokens = new HashMap();
        String correlationId = CommonMethods.randomCorrelationId();
        tokens.put("correlationId", correlationId);
        tokens.put("name", (String)CommonMethods.TBProperties.get("SwiftInUser"));
        tokens.put("acknowledged", "true");
        tokens.put("message", swiftMsg);

 
        MapTokenResolver resolver = new MapTokenResolver(tokens);
        Reader fileValue = new StringReader(templateXml);
        Reader reader = new TokenReplacingReader(fileValue, resolver);
        String tiRequestXML = reader.toString();
        reader.close();
        swifinprocMap.put("tiRequest", tiRequestXML);
        System.out.println("SFMSSwiftIn TIRequest to TI: \n" + tiRequestXML);
        this.TIRequest = tiRequestXML;
        this.TIResponse = TIPlusEJBClient.process(tiRequestXML, bootstrapURL);
        System.out.println("Exiting pushToTI ");
      }
    }
    catch (Exception e)
    {
      System.out.println("Exception in pushToTI-->" + e.getMessage());
      e.printStackTrace();
    }
  }
  public static void main(String[] a)
    throws Exception
  {
    String swiftMessage = 
      CommonMethods.readFile("C:\\Users\\EA20188813\\OneDrive - Wipro\\Easwari\\Project\\UBI\\Random\\SwiftInMessage.txt");
    swiftInProcessPull aswiftInProcess = new swiftInProcessPull();
    aswiftInProcess.pushToTI(swiftMessage, "");
  }
}
