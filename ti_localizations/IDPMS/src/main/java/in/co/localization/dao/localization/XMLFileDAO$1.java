package in.co.localization.dao.localization;
 
import in.co.localization.vo.localization.XMLFileVO;
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
    try
    {
      this.this$0.xmlFileVO1.setTagName(qName);
    }
    catch (Exception ex)
    {
      throw new SAXException(qName);
    }
  }
  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    this.this$0.xmlFileVO1 = new XMLFileVO();
    try
    {
      this.this$0.xmlFileVO1.setTagName(qName);
    }
    catch (Exception ex)
    {
      throw new SAXException(qName);
    }
  }
}