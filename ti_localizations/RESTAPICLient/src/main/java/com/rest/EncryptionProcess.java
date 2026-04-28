package com.rest;

import com.infrasoft.kiya.security.EncryptionDecryptionImpl;

public class EncryptionProcess
{
  static EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
  public static String fetchEncryptRequest(String jsonRequest, String key)
    throws Exception
  {
    String encMes = null;
    try
    {
      encMes = obj.encryptMessage(jsonRequest, key);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return encMes;
  }
  public static String fetchDecryptRequest(String jsonRequest, String key)
    throws Exception
  {
    String decMes = null;
    try
    {
      decMes = obj.decryptMessage(jsonRequest, key);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return decMes;
  }
}
