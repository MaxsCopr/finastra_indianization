package com.rest;

import java.io.PrintStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
 
public class NetClientGet
{
  public String process(String jsonEncryptRequest, String endURL)
  {
    String encResponse = "FAILED";
    StringBuffer buffer = new StringBuffer();
    PostMethod post = new PostMethod(endURL);
    try
    {
      System.out.println("------Entering process---");
      StringRequestEntity requestEntity = new StringRequestEntity(jsonEncryptRequest, "application/json", 
        "utf-8");
      post.setRequestEntity(requestEntity);
      HttpClient httpclient = new HttpClient();
      int result = httpclient.executeMethod(post);
      if (result != 200) {
        throw new Exception("Server returned code " + result);
      }
      encResponse = post.getResponseBodyAsString();
      System.out.println("Encrypted Response From Bank-->\n" + encResponse);
      System.out.println("------Exiting process-------");
    }
    catch (Exception e)
    {
      System.out.println("Exception in NetClientGet:- " + e);
      e.printStackTrace();
    }
    finally
    {
      post.releaseConnection();
    }
    return encResponse.trim();
  }
}