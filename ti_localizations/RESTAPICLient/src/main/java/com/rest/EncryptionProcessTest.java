package com.rest;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
 
public class EncryptionProcessTest
{
  private static String ENC_STANDARD = "AES";
  private static String ENC_CHARSET = "UTF-8";
  private static String ENC_ALGORITHM = "SHA-256";
  public String fetchEncryptRequest(String jsonRequest, String key)
    throws Exception
  {
    System.out.println("----Entering fetchEncryptRequest---");
    Cipher cipher = Cipher.getInstance(ENC_STANDARD);
    SecretKey originalKey = generateKey(key);
    cipher.init(1, originalKey);
    byte[] cipherText = cipher.doFinal(jsonRequest.getBytes(ENC_CHARSET));
    System.out.println("------Exiting fetchEncryptRequest---");
    return Base64.getEncoder().encodeToString(cipherText);
  }
  private static SecretKeySpec generateKey(String bannkEncryptionKey)
    throws Exception
  {
    MessageDigest digest = MessageDigest.getInstance(ENC_ALGORITHM);
    byte[] bytes = bannkEncryptionKey.getBytes(ENC_CHARSET);
    digest.update(bytes, 0, bytes.length);
    byte[] key = digest.digest();
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, ENC_STANDARD);
    return secretKeySpec;
  }
  public String fetchDecryptRequest(String jsonRequest, String key)
    throws Exception
  {
    Cipher cipher = Cipher.getInstance(ENC_STANDARD);
    SecretKey originalKey = generateKey(key);
    cipher.init(2, originalKey);
    byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(jsonRequest.getBytes(ENC_CHARSET)));
    return new String(cipherText);
  }
}