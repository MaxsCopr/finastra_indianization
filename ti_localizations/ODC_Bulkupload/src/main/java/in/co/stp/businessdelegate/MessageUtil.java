package in.co.stp.businessdelegate;

import com.misys.tiplus2.apps.ti.service.messages.BulkServiceRequest;
import com.misys.tiplus2.apps.ti.service.messages.BulkServiceResponse;
import com.misys.tiplus2.apps.ti.service.messages.ObjectFactory;
import com.misys.tiplus2.apps.ti.service.messages.TFILCAPP;
import com.misys.tiplus2.services.control.ReplyFormatEnum;
import com.misys.tiplus2.services.control.ServiceRequest;
import com.misys.tiplus2.services.control.ServiceRequest.RequestHeader;
import com.misys.tiplus2.services.control.ServiceRequest.RequestHeader.Credentials;
import com.misys.tiplus2.services.control.ServiceResponse;
import com.misys.tiplus2.services.control.ServiceResponse.ResponseHeader;
import com.misys.tiplus2.services.control.YNEnum;
import in.co.stp.dao.DBPropertiesLoader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class MessageUtil
{
  private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);
  public static Marshaller marshaller = null;
  public static JAXBContext jaxbContext;
  public static JAXBContext getJaxbContext()
    throws javax.xml.bind.JAXBException
  {
    try
    {
      if (jaxbContext != null) {
        return jaxbContext;
      }
      jaxbContext = JAXBContext.newInstance(new Class[] { TFILCAPP.class });
    }
    catch (org.eclipse.persistence.exceptions.JAXBException e)
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
  public static String xmlServiceRequest(List<ServiceRequest> sRequestItems, String serviceName)
    throws javax.xml.bind.JAXBException
  {
    Date date = new Date();
    String returnXML = "";
    ServiceRequest serReq = getServiceRequest("TIBulk", "Item", 
      DBPropertiesLoader.MIGRATIONUSER, "FULL", "N", "N", new VMID().toString());
    List<JAXBElement<?>> bulkServiceRequests = serReq.getRequest();
    ObjectFactory of = new ObjectFactory();
    BulkServiceRequest batchRequest = new BulkServiceRequest();
    for (ServiceRequest s : sRequestItems)
    {
      s.setRequestHeader(getRequestHeader("TI", serviceName, 
        DBPropertiesLoader.MIGRATIONUSER, "FULL", "N", "N", s.getRequestHeader()
        .getCorrelationId()));
      batchRequest.getServiceRequest().add(s);
    }
    Date date2 = new Date();

 
    Object itemsServiceRequest = of
      .createItemRequest(batchRequest);
    bulkServiceRequests.add(itemsServiceRequest);
    try
    {
      Date date3 = new Date();
      if (marshaller == null)
      {
        logger.info(" Creating marshaller ... ");
        marshaller = getJaxbContext().createMarshaller();
      }
      marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      marshaller.marshal(serReq, baos);
      returnXML = baos.toString();
      Date localDate1 = new Date();
    }
    catch (org.eclipse.persistence.exceptions.JAXBException ex)
    {
      ex.printStackTrace();
    }
    return returnXML;
  }
  public static List<ServiceResponse> processResponse(String result)
    throws org.eclipse.persistence.exceptions.JAXBException, IOException, javax.xml.bind.JAXBException
  {
    List<ServiceResponse> serviceResponses = new ArrayList();

 
    ObjectFactory factory = new ObjectFactory();
    InputStream inStream = new ByteArrayInputStream(result.getBytes());
    JAXBContext context = JAXBInstanceInitialiser.getBackOfficeBatchRequestContext();
    Unmarshaller unmarshaller = context.createUnmarshaller();
    ServiceResponse response = (ServiceResponse)unmarshaller
      .unmarshal(inStream);
    List<JAXBElement<?>> bulkServiceResponse = response.getResponse();
    JAXBElement<BulkServiceResponse> element = (JAXBElement)bulkServiceResponse.get(0);
    serviceResponses = ((BulkServiceResponse)element.getValue()).getServiceResponse();
    logger.info("Size ->" + serviceResponses.size());
    for (ServiceResponse serviceResponse : serviceResponses)
    {
      logger.info("Service Response CorrID -->" + 
        serviceResponse.getResponseHeader()
        .getCorrelationId());
      logger.info("Service Response Status -->" + 
        serviceResponse.getResponseHeader().getStatus());
      logger.info("Service Response XML -->" + serviceResponse.getXMLString());
    }
    return serviceResponses;
  }
}
