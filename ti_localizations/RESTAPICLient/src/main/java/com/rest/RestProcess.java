package com.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rest.json.model.AvailBalBankReq;
import com.rest.json.model.AvailBalRequest;
import com.rest.json.model.LimitFetchBankReq;
import com.rest.json.model.LimitFetchRequest;
import com.rest.json.model.PostingBankReq;
import com.rest.json.model.PostingRequest;
import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
@WebServlet({"/RestProcess"})
public class RestProcess
  extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  NetClientGet aNetClientGet = new NetClientGet();
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
    try
    {
      String apiService = request.getParameter("service").trim();
      String apiURL = request.getParameter("URL").trim();
      String apiKey = request.getParameter("key").trim();
      System.out.println("Service -->" + apiService + "\nURL --->" + apiURL + "\nKey--->" + apiKey);
      if (apiService.equals("AvailBal"))
      {
        AvailBalRequest req = (AvailBalRequest)aGson.fromJson(request.getParameter("requestXML"), AvailBalRequest.class);
        System.out.println("Entering httpServlet with " + apiService + " request-->\n" + request.getParameter("requestXML").trim());
        String encryptedReq = EncryptionProcess.fetchEncryptRequest(request.getParameter("requestXML").trim(), apiKey);

 
        String decryptedReq = EncryptionProcess.fetchDecryptRequest(encryptedReq, apiKey);
        System.out.println("encryptedReq Request --->" + encryptedReq);
        System.out.println("decryptedReq Request --->" + decryptedReq);
        AvailBalBankReq aAvailBalBankReq = new AvailBalBankReq(encryptedReq, req.getMsgid());
        System.out.println("Encrypted Bank Request --->" + aGson.toJson(aAvailBalBankReq).trim());
        String jsonResponse = this.aNetClientGet.process(aGson.toJson(aAvailBalBankReq).trim(), 
          apiURL);
        String decryptedRes = EncryptionProcess.fetchDecryptRequest(jsonResponse, 
          apiKey);
        System.out.println("Decrypted Bank Response --> " + decryptedRes);
        HttpSession session = request.getSession();
        session.setAttribute("requestXML", request.getParameter("requestXML"));
        session.setAttribute("responseXML", decryptedRes);
        response.sendRedirect("result.jsp");
        System.out.println(" ------Exiting httpServlet------ ");
      }
      else if (apiService.equalsIgnoreCase("LimitFetch"))
      {
        LimitFetchRequest req = (LimitFetchRequest)aGson.fromJson(request.getParameter("requestXML"), LimitFetchRequest.class);
        System.out.println("Entering httpServlet with " + apiService + " request-->\n" + request.getParameter("requestXML").trim());
        String encryptedReq = EncryptionProcess.fetchEncryptRequest(request.getParameter("requestXML").trim(), apiKey);

 
        String decryptedReq = EncryptionProcess.fetchDecryptRequest(encryptedReq, apiKey);
        System.out.println("encryptedReq Request --->" + encryptedReq);
        System.out.println("decryptedReq Request --->" + decryptedReq);
        LimitFetchBankReq aLimitFetchBankReq = new LimitFetchBankReq(encryptedReq, req.getMsgid());
        System.out.println("Encrypted Bank Request --->" + aGson.toJson(aLimitFetchBankReq).trim());
        String jsonResponse = this.aNetClientGet.process(aGson.toJson(aLimitFetchBankReq).trim(), 
          apiURL);
        String decryptedRes = EncryptionProcess.fetchDecryptRequest(jsonResponse, 
          apiKey);
        System.out.println("Decrypted Bank Response --> " + decryptedRes);
        HttpSession session = request.getSession();
        session.setAttribute("requestXML", request.getParameter("requestXML"));
        session.setAttribute("responseXML", decryptedRes);
        response.sendRedirect("result.jsp");
        System.out.println(" ------Exiting httpServlet------ ");
      }
      else
      {
        PostingRequest req = (PostingRequest)aGson.fromJson(request.getParameter("requestXML"), PostingRequest.class);
        System.out.println("Entering httpServlet with " + apiService + " request-->\n" + request.getParameter("requestXML").trim());
        String encryptedReq = EncryptionProcess.fetchEncryptRequest(request.getParameter("requestXML").trim(), apiKey);

 
        String decryptedReq = EncryptionProcess.fetchDecryptRequest(encryptedReq, apiKey);
        System.out.println("encryptedReq Request --->" + encryptedReq);
        System.out.println("decryptedReq Request --->" + decryptedReq);
        PostingBankReq aPostingBankReq = new PostingBankReq(encryptedReq, req.getMsgid());
        System.out.println("Encrypted Bank Request --->" + aGson.toJson(aPostingBankReq).trim());
        String jsonResponse = this.aNetClientGet.process(aGson.toJson(aPostingBankReq).trim(), 
          apiURL);
        String decryptedRes = EncryptionProcess.fetchDecryptRequest(jsonResponse, 
          apiKey);
        System.out.println("Decrypted Bank Response --> " + decryptedRes);
        HttpSession session = request.getSession();
        session.setAttribute("requestXML", request.getParameter("requestXML"));
        session.setAttribute("responseXML", decryptedRes);
        response.sendRedirect("result.jsp");
        System.out.println(" ------Exiting httpServlet------ ");
      }
    }
    catch (Exception e)
    {
      System.out.println("Exception in httpServlet");
      e.printStackTrace();
    }
  }
}
