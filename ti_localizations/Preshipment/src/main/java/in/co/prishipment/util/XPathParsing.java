package in.co.prishipment.util;
 
import java.io.IOException;

import java.io.StringReader;

import java.util.HashMap;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;

import javax.xml.xpath.XPathConstants;

import javax.xml.xpath.XPathExpression;

import javax.xml.xpath.XPathExpressionException;

import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

import org.xml.sax.SAXException;
 
public class XPathParsing

{

  private static final Logger logger = Logger.getLogger(XPathParsing.class.getName());

  public static String getValue(String requestXML, String xpath)

    throws SAXException, IOException, XPathExpressionException

  {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    dbf.setValidating(false);

    String attributeValue = null;

    try

    {

      DocumentBuilder db = dbf.newDocumentBuilder();

      Document doc = db.parse(new InputSource(new StringReader(requestXML)));

      XPathFactory factory = XPathFactory.newInstance();

      XPath oXPath = factory.newXPath();

      attributeValue = oXPath.compile(xpath).evaluate(doc);

    }

    catch (ParserConfigurationException e)

    {

      logger.debug("ParserConfigurationException:" + e.getMessage());

      return null;

    }

    DocumentBuilder db;

    return attributeValue;

  }

  public static Map<String, String> getMultiTagValue(String requestXML, String xpath)

  {

    Map<String, String> messageMAP = new HashMap();

    try

    {

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

      dbFactory.setValidating(false);

      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      Document doc = dBuilder.parse(new InputSource(new StringReader(requestXML)));

      doc.getDocumentElement().normalize();

      XPath xPath = XPathFactory.newInstance().newXPath();

      String expression = xpath;

      NodeList nodeList = (NodeList)xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

      logger.debug("Message Count--->" + nodeList.getLength());

      for (int i = 0; i < nodeList.getLength(); i++)

      {

        Node nNode = nodeList.item(i);

        logger.debug("\nCurrent Element :" + nNode.getNodeName());

        if (nNode.getNodeType() == 1)

        {

          Element eElement = (Element)nNode;

          logger.debug("Element-" + i + "-Message-->" + eElement.getTextContent());

          messageMAP.put(i, eElement.getTextContent());

        }

      }

    }

    catch (ParserConfigurationException e)

    {

      e.printStackTrace();

    }

    catch (SAXException e)

    {

      e.printStackTrace();

    }

    catch (IOException e)

    {

      e.printStackTrace();

    }

    catch (XPathExpressionException e)

    {

      e.printStackTrace();

    }

    return messageMAP;

  }

  public static void main(String[] args)

    throws Exception

  {

    String xpath = "ServiceRequest/BatchRequest/RequestHeader/Service";

    logger.info(xpath);

  }

}