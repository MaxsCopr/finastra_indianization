package in.co.localization.utility;
 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
 
public class Security
{
  private static Logger logger = Logger.getLogger(Security.class.getName());
  public static String encrypt(String input, String key)
  {
    byte[] crypted = null;
    try
    {
      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(1, skey);
      crypted = cipher.doFinal(input.getBytes());
    }
    catch (Exception e)
    {
      e.getMessage();
    }
    return new String(Base64.encodeBase64(crypted));
  }
  public static String decrypt(String input, String key)
  {
    byte[] output = null;
    try
    {
      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(2, skey);
      output = cipher.doFinal(Base64.decodeBase64(input.getBytes()));
    }
    catch (Exception e)
    {
      e.getMessage();
    }
    return new String(output);
  }
  public static void main(String[] args)
  {
    String key = "1234567891234567";
    String data = "test";
    logger.info(decrypt(encrypt(data, key), key));
    logger.info(encrypt(data, key));
  }
}