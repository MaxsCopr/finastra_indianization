package com.rest.util;

import com.misys.tiplus2.service.access.UnrecognisedServiceName;
import com.misys.tiplus2.service.access.ejb.EnigmaServiceAccess;
import com.misys.tiplus2.service.access.ejb.EnigmaServiceAccessHome;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
 
public class TIPlusEJBClient
{
  public static String process(String message, String bootstrapURL)
    throws Exception
  {
    String result = "Unable to Process";
    Hashtable<String, String> env = new Hashtable();
    System.out.println("Entering EJB Process ");

 
 
 
    System.out.println("bootstrapURL --->" + bootstrapURL);
    env.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
    env.put("java.naming.provider.url", bootstrapURL);
    try
    {
      System.out.println("EJB Process 1");
      Context ctx = new InitialContext(env);
      System.out.println("EJB Process 2");
      EnigmaServiceAccessHome accessBeanHome = (EnigmaServiceAccessHome)ctx.lookup("ejb/EnigmaServiceAccess");
      System.out.println("EJB Process 3");
      EnigmaServiceAccess accessB = accessBeanHome.create();
      System.out.println("EJB Process 4");
      result = accessB.process(message);
      System.out.println("TI Response --->" + result);
      System.out.println("Exiting EJB Process ");
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
      System.out.println("RemoteException -->" + e.getMessage());
    }
    catch (NamingException e)
    {
      System.out.println("NamingException -->" + e.getMessage());
      e.printStackTrace();
    }
    catch (UnrecognisedServiceName e)
    {
      System.out.println("UnrecognisedServiceName -->" + e.getMessage());
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
}
