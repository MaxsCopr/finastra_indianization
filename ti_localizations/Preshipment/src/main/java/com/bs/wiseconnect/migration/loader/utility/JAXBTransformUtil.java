package com.bs.wiseconnect.migration.loader.utility;
 
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
 
public class JAXBTransformUtil

{

  public static Logger logger = Logger.getLogger(JAXBTransformUtil.class);

  public static String doMarshalling(JAXBContext context, Object toDoObject)

  {

    String result = "";

    if (WiseconnectUtil.isValidObject(toDoObject))

    {

      ByteArrayOutputStream outStream = new ByteArrayOutputStream();

      try

      {

        Marshaller jaxbMarshaller = context.createMarshaller();

        jaxbMarshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));

        jaxbMarshaller.marshal(toDoObject, outStream);

        result = outStream.toString();

      }

      catch (Exception exp)

      {

        logger.error(exp.getMessage(), exp);

      }

    }

    else

    {

      logger.debug("Marshalling object is not valid");

    }

    return result;

  }

}