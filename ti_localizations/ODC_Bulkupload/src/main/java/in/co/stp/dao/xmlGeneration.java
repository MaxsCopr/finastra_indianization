package in.co.stp.dao;

import in.co.stp.dao.exception.DAOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
public class xmlGeneration
{
  static Logger logger = LogManager.getLogger(xmlGeneration.class);
  public static void main(String[] args)
    throws TransformerConfigurationException, ParserConfigurationException, DAOException
  {
    xmlGeneration xm = new xmlGeneration();
  }
  public String generatePostingTag(String amt, String custmr)
    throws TransformerConfigurationException, ParserConfigurationException, DAOException
  {
    String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceRequest xmlns=\"urn:control.services.tiplus2.misys.com\" xmlns:ns2=\"urn:messages.service.ti.apps.tiplus2.misys.com\" xmlns:ns3=\"urn:common.service.ti.apps.tiplus2.misys.com\" xmlns:ns4=\"urn:custom.service.ti.apps.tiplus2.misys.com\"><RequestHeader><Service>Account</Service><Operation>AvailBal</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAK</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><TransactionControl>NONE</TransactionControl></RequestHeader>";

 
 
 
    String xml2 = "</ServiceRequest>";

 
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();

 
    Element roothead = document.createElement("ns2:AvailBalRequest");
    document.appendChild(roothead);

 
    Element bakoffcacc = document.createElement("ns2:BackOfficeAccount");
    bakoffcacc.appendChild(document.createTextNode(amt));
    roothead.appendChild(bakoffcacc);
    Element extacc = document.createElement("ns2:ExternalAccount");
    extacc.appendChild(document.createTextNode("10000006133"));
    roothead.appendChild(extacc);
    Element postingVal = document.createElement("ns2:PostingValueDate");
    postingVal.appendChild(document.createTextNode("2016-02-04"));
    roothead.appendChild(postingVal);
    Element postingAmt = document.createElement("ns2:PostingAmount");
    postingAmt.appendChild(document.createTextNode("585963.84"));
    roothead.appendChild(postingAmt);
    Element postingCurrency = document.createElement("ns2:PostingCurrency");
    postingCurrency.appendChild(document.createTextNode("INR"));
    roothead.appendChild(postingCurrency);
    Element dbtcrt = document.createElement("ns2:DebitCredit");
    dbtcrt.appendChild(document.createTextNode("D"));
    roothead.appendChild(dbtcrt);
    Element cus = document.createElement("ns2:Customer");
    cus.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
    cus.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    cus.appendChild(document.createTextNode(custmr));
    roothead.appendChild(cus);

 
 
    String str = convertDocumentToString(document);
    String finalXML = xml1 + str + xml2;
    logger.info("XML File-------->" + finalXML);
    return finalXML;
  }
  private static String convertDocumentToString(Document doc)
  {
    TransformerFactory tf = TransformerFactory.newInstance();
    try
    {
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty("omit-xml-declaration", 
        "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      return writer.getBuffer().toString();
    }
    catch (TransformerException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
