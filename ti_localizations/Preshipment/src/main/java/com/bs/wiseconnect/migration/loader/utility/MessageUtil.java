package com.bs.wiseconnect.migration.loader.utility;
 
import com.misys.tiplus2.apps.ti.service.messages.BulkServiceRequest;

import com.misys.tiplus2.apps.ti.service.messages.BulkServiceResponse;

import com.misys.tiplus2.apps.ti.service.messages.ObjectFactory;

import com.misys.tiplus2.apps.ti.service.messages.TFATTDOC;

import com.misys.tiplus2.services.control.ReplyFormatEnum;

import com.misys.tiplus2.services.control.ServiceRequest;

import com.misys.tiplus2.services.control.ServiceRequest.RequestHeader;

import com.misys.tiplus2.services.control.ServiceRequest.RequestHeader.Credentials;

import com.misys.tiplus2.services.control.ServiceResponse;

import com.misys.tiplus2.services.control.ServiceResponse.ResponseHeader;

import com.misys.tiplus2.services.control.YNEnum;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.io.FileReader;

import java.io.IOException;

import java.io.InputStream;

import java.rmi.dgc.VMID;

import java.util.ArrayList;

import java.util.List;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBElement;

import javax.xml.bind.JAXBException;

import javax.xml.bind.Marshaller;

import javax.xml.bind.Unmarshaller;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.stream.StreamResult;

import javax.xml.transform.stream.StreamSource;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.xml.sax.SAXException;
 
public class MessageUtil

{

  private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

  public static Marshaller marshaller = null;

  public static JAXBContext jaxbContext;

  public static JAXBContext getJaxbContext()

  {

    try

    {

      if (jaxbContext != null) {

        return jaxbContext;

      }

      jaxbContext = JAXBContext.newInstance(new Class[] { TFATTDOC.class });

    }

    catch (JAXBException e)

    {

      e.printStackTrace();

    }

    return jaxbContext;

  }

  public static ServiceRequest.RequestHeader getRequestHeader(String service, String operation, String credentialsName, String replyFormat, String noRepair, String noOverride, String corrID)

  {

    ServiceRequest.RequestHeader requestHeader = new ServiceRequest.RequestHeader();

    requestHeader.setService(service);

    requestHeader.setOperation(operation);

    ServiceRequest.RequestHeader.Credentials credentials = new ServiceRequest.RequestHeader.Credentials();

    credentials.setName(credentialsName);

    requestHeader.setCredentials(credentials);

    requestHeader.setReplyFormat(ReplyFormatEnum.fromValue(replyFormat));

    requestHeader.setNoRepair(YNEnum.fromValue(noRepair));

    requestHeader.setNoOverride(YNEnum.fromValue(noOverride));

    requestHeader.setCorrelationId(corrID);

    return requestHeader;

  }

  public static ServiceRequest getServiceRequest(String service, String operation, String credentialsName, String replyFormat, String noRepair, String noOverride, String corrID)

  {

    ServiceRequest serviceRequest = new ServiceRequest();

    ServiceRequest.RequestHeader header = getRequestHeader(service, operation, 

      credentialsName, replyFormat, noRepair, noOverride, corrID);

    serviceRequest.setRequestHeader(header);

    return serviceRequest;

  }

  public static String xmlServiceRequest(List<ServiceRequest> sRequestItems, String serviceName, String userName)

  {

    String returnXML = "";

    ServiceRequest serReq = getServiceRequest("TIBulk", "Item", 

      "SUPERVISOR", "FULL", "N", "N", new VMID().toString());

    List<JAXBElement<?>> bulkServiceRequests = serReq.getRequest();

    ObjectFactory of = new ObjectFactory();

    BulkServiceRequest batchRequest = new BulkServiceRequest();

    for (ServiceRequest s : sRequestItems)

    {

      s.setRequestHeader(getRequestHeader("TI", serviceName, 

        userName, "FULL", "N", "N", s.getRequestHeader()

        .getCorrelationId()));

      batchRequest.getServiceRequest().add(s);

    }

    JAXBElement<BulkServiceRequest> itemsServiceRequest = of

      .createItemRequest(batchRequest);

    bulkServiceRequests.add(itemsServiceRequest);

    try

    {

      if (marshaller == null) {

        marshaller = getJaxbContext().createMarshaller();

      }

      marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      marshaller.marshal(serReq, baos);

      return baos.toString();

    }

    catch (JAXBException ex)

    {

      ex.printStackTrace();

    }

    return returnXML;

  }

  public static List<ServiceResponse> processResponse(String result)

    throws JAXBException, IOException

  {

    List<ServiceResponse> serviceResponses = new ArrayList();

    if (result.matches("(.*)ItemResponse>(.*)"))

    {

      ObjectFactory factory = new ObjectFactory();

      InputStream inStream = new ByteArrayInputStream(result.getBytes());

      JAXBContext context = JAXBInstanceInitialiser.getBackOfficeBatchRequestContext();

      Unmarshaller unmarshaller = context.createUnmarshaller();

      ServiceResponse response = (ServiceResponse)unmarshaller

        .unmarshal(inStream);

      List<JAXBElement<?>> bulkServiceResponse = response.getResponse();

      JAXBElement<BulkServiceResponse> element = 

        (JAXBElement)bulkServiceResponse.get(0);

      serviceResponses = ((BulkServiceResponse)element.getValue()).getServiceResponse();

      logger.info("Size ->" + serviceResponses.size());

      for (ServiceResponse serviceResponse : serviceResponses)

      {

        logger.info("Service Response CorrID" + 

          serviceResponse.getResponseHeader()

          .getCorrelationId());

        logger.info("Service Response Status" + 

          serviceResponse.getResponseHeader().getStatus());

      }

    }

    else

    {

      serviceResponses = MigrationUtil.processSingleResponse(result);

      serviceResponses.size();

    }

    return serviceResponses;

  }

  public static String clearEmptyNodes(String xmlString)

    throws XPathExpressionException, ParserConfigurationException, SAXException, IOException

  {

    String formattedOutput = "";

    try

    {

      TransformerFactory tFactory = TransformerFactory.newInstance();

      Transformer transformer = tFactory.newTransformer(new StreamSource(

        "removeemptytag.xsl"));

      StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(

        xmlString.getBytes()));

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      transformer.transform(xmlSource, new StreamResult(baos));

      formattedOutput = baos.toString();

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    logger.debug("Formatted Output ->" + formattedOutput);

    return formattedOutput;

  }

  public static void main(String[] args)

    throws IOException, JAXBException

  {

    try

    {

      clearEmptyNodes("sdsadswdsds");

    }

    catch (XPathExpressionException ex)

    {

      ex.printStackTrace();

    }

    catch (ParserConfigurationException ex)

    {

      ex.printStackTrace();

    }

    catch (SAXException ex)

    {

      ex.printStackTrace();

    }

  }

  public static String readFile(String filePath)

    throws IOException

  {

    BufferedReader reader = new BufferedReader(new FileReader(filePath));

    String line = null;

    StringBuilder stringBuilder = new StringBuilder();

    String ls = System.getProperty("line.separator");

    while ((line = reader.readLine()) != null)

    {

      stringBuilder.append(line);

      stringBuilder.append(ls);

    }

    return stringBuilder.toString();

  }

}