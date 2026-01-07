package in.co.localization.dao.localization;
 
import in.co.localization.vo.localization.XMLFileVO;

import org.apache.log4j.Logger;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;
 
class XMLFileDAO$1

  extends DefaultHandler

{

  XMLFileDAO$1(XMLFileDAO paramXMLFileDAO) {}

  public void startElement(String uri, String localName, String qName, Attributes attributes)

    throws SAXException

  {

    this.this$0.xmlFileVO1 = new XMLFileVO();

    XMLFileDAO.access$0().info("Reading-------------------------Tags");

 
    XMLFileDAO.access$0().info("XML Start Element----------------- :" + qName);

    try

    {

      this.this$0.xmlFileVO1.setTagName(qName);

    }

    catch (Exception ex)

    {

      XMLFileDAO.access$0().info("Exception in Reading Start Tags" + ex);

      XMLFileDAO.access$0().info("Exception in Reading Start Tags" + ex);

      throw new SAXException(qName);

    }

  }

  public void endElement(String uri, String localName, String qName)

    throws SAXException

  {

    this.this$0.xmlFileVO1 = new XMLFileVO();

    XMLFileDAO.access$0().info("XML End Element :" + qName);

    try

    {

      this.this$0.xmlFileVO1.setTagName(qName);

    }

    catch (Exception ex)

    {

      XMLFileDAO.access$0().info("Exception in Reading End Tags" + ex);

      XMLFileDAO.access$0().info("Exception in Reading End Tags" + ex);

      throw new SAXException(qName);

    }

  }

}