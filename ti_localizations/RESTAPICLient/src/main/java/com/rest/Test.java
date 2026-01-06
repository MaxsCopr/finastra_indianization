package com.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infrasoft.kiya.security.EncryptionDecryptionImpl;
import java.io.PrintStream;
import org.json.JSONObject;
 
public class Test
{
  private static String jsonReq = "";
  private static long value;
  private static String bannkEncryptionKey = "4359dbcdb30a44a08aebabbeb971cb4e";
  private static String encryptedReq = "fe70618d63b5ae04784ee1f4551c04f5 19f0fc58c051d6196edb8516f3b78553 ZiJusu2jOcRSRhxhMabOPpW9zKSMC+gsiJxmVBjjAbMhLk/6S3Y8HmQ8H0ozUzZEhgtMy5w9slHs5q1Tb+vvq0Ke+PRRXNQAq1gLgzbef15c0EJ5AUIoVgP5DDs48oRu0xEduWYMQ94H8vF3R0gQ6dLeBZYSQKFmu91O6vd2B2kF/gz3S80wcca4HZaGXe6OUUMh0Jq+/iC704GFzI3YOS7AtxqbDbwrrnnNxql5dfrdFKtDOH6rgKRiJ/k0hZN8/znw/gE3e2gEihGT5MCQq+j5IGHr4lch7C+eugRSBG0=";
  static Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
  public static void main(String[] args)
    throws Exception
  {
    String decrypCtedReq = EncryptionProcess.fetchDecryptRequest(encryptedReq, bannkEncryptionKey);
    JSONObject a = new JSONObject(decrypCtedReq);
    System.out.println("decryptedReq Request --->" + aGson.toJson(decrypCtedReq));
  }
  private static String fetchDecryptRequest(String encryptedReq2, String bannkEncryptionKey2)
  {
    EncryptionDecryptionImpl a = new EncryptionDecryptionImpl();
    return a.decryptMessage(encryptedReq2, bannkEncryptionKey2);
  }
}
