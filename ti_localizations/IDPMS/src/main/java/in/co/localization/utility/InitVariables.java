package in.co.localization.utility;
 
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
public class InitVariables
  implements ServletContextListener
{
  public void contextDestroyed(ServletContextEvent event) {}
  public void contextInitialized(ServletContextEvent event)
  {
    String props = "E:\\TIplus\\localisation\\conf\\config.properties";
    Properties propsFromFile = new Properties();
    try
    {
      propsFromFile.load(getClass().getResourceAsStream("E:\\TIplus\\localisation\\conf\\config.properties"));
    }
    catch (IOException localIOException) {}
    for (String prop : propsFromFile.stringPropertyNames()) {
      if (System.getProperty(prop) == null) {
        System.setProperty(prop, propsFromFile.getProperty(prop));
      }
    }
  }
}