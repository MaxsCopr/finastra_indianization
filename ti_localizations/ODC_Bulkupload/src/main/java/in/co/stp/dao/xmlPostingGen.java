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

public class xmlPostingGen
{
  static Logger logger = LogManager.getLogger(xmlPostingGen.class);
 
  public static void main(String[] args)
    throws TransformerConfigurationException, ParserConfigurationException, DAOException
  {
    xmlPostingGen xm = new xmlPostingGen();
    xm.generatePostingTag("1000", "test");
  }
 
  public String generatePostingTag(String amt, String custmr)
    throws TransformerConfigurationException, ParserConfigurationException, DAOException
  {
    String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceRequest xmlns=\"urn:control.services.tiplus2.misys.com\" xmlns:ns2=\"urn:messages.service.ti.apps.tiplus2.misys.com\" xmlns:ns4=\"urn:custom.service.ti.apps.tiplus2.misys.com\" xmlns:ns3=\"urn:common.service.ti.apps.tiplus2.misys.com\"><RequestHeader><Service>BackOffice</Service><Operation>Batch</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>IDFC EXT</TargetSystem><TargetSystem>KOTAK</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><TransactionControl>NONE</TransactionControl><CreationDate>2016-02-05+05:30</CreationDate></RequestHeader>";
   





    String xml2 = "</ServiceRequest>";
   

    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();
   

    Element roothead = document.createElement("ns2:BatchRequest");
    document.appendChild(roothead);
   
    Element roothead1 = document.createElement("ServiceRequest");
    roothead.appendChild(roothead1);
   
    Element roothead2 = document.createElement("RequestHeader");
    roothead1.appendChild(roothead2);
   

    Element service = document.createElement("Service");
    service.appendChild(document.createTextNode("BackOffice"));
    roothead2.appendChild(service);
   
    Element operation = document.createElement("Operation");
    operation.appendChild(document.createTextNode("Posting"));
    roothead2.appendChild(operation);
   
    Element roothead3 = document.createElement("Credentials");
    roothead2.appendChild(roothead3);
   
    Element namecred = document.createElement("Name");
    namecred.appendChild(document.createTextNode("SUPERVISOR"));
    roothead3.appendChild(namecred);
   

    Element replyFormat = document.createElement("ReplyFormat");
    replyFormat.appendChild(document.createTextNode("FULL"));
    roothead2.appendChild(replyFormat);
   

    Element targetSys = document.createElement("TargetSystem");
    targetSys.appendChild(document.createTextNode("KOTAK"));
    roothead2.appendChild(targetSys);
   
    Element sourceSys = document.createElement("SourceSystem");
    sourceSys.appendChild(document.createTextNode("ZONE1"));
    roothead2.appendChild(sourceSys);
   
    Element noRepair = document.createElement("NoRepair");
    noRepair.appendChild(document.createTextNode("Y"));
    roothead2.appendChild(noRepair);
   
    Element noOverRide = document.createElement("NoOverride");
    noOverRide.appendChild(document.createTextNode("Y"));
    roothead2.appendChild(noOverRide);
   
    Element corrId = document.createElement("CorrelationId");
    corrId.appendChild(document.createTextNode("273ace37-fd39-465d-a2e7-8ee3903eb739--1"));
    roothead2.appendChild(corrId);
   
    Element tranCtrl = document.createElement("TransactionControl");
    tranCtrl.appendChild(document.createTextNode("NONE"));
    roothead2.appendChild(tranCtrl);
   
    Element creatDat = document.createElement("CreationDate");
    creatDat.appendChild(document.createTextNode("2016-02-05+05:30"));
    roothead2.appendChild(creatDat);
   
    Element roothead4 = document.createElement("ns2:Posting");
    roothead1.appendChild(roothead4);
   
    Element transID = document.createElement("ns2:TransactionId");
    transID.appendChild(document.createTextNode("273ace37-fd39-465d-a2e7-8ee3903eb739"));
    roothead4.appendChild(transID);
   
    Element transNo = document.createElement("ns2:TransactionSeqNo");
    transNo.appendChild(document.createTextNode("1"));
    roothead4.appendChild(transNo);
   
    Element masterKey = document.createElement("ns2:MasterKey");
    masterKey.appendChild(document.createTextNode("85025"));
    roothead4.appendChild(masterKey);
   
    Element postingBranch = document.createElement("ns2:PostingBranch");
    postingBranch.appendChild(document.createTextNode("80201"));
    roothead4.appendChild(postingBranch);
   
    Element inputBranch = document.createElement("ns2:InputBranch");
    inputBranch.appendChild(document.createTextNode("80201"));
    roothead4.appendChild(inputBranch);
   
    Element prdtRef = document.createElement("ns2:ProductReference");
    prdtRef.appendChild(document.createTextNode("IRF"));
    roothead4.appendChild(prdtRef);
   
    Element masterRef = document.createElement("ns2:MasterReference");
    masterRef.appendChild(document.createTextNode("VFVA802011601199"));
    roothead4.appendChild(masterRef);
   
    Element postingSeq = document.createElement("ns2:PostingSeqNo");
    postingSeq.appendChild(document.createTextNode("997"));
    roothead4.appendChild(postingSeq);
   
    Element accNo = document.createElement("ns2:AccountNumber");
    accNo.appendChild(document.createTextNode("1BO-80201-24203-INR"));
    roothead4.appendChild(accNo);
   
    Element bakOff = document.createElement("ns2:BackOfficeAccountNo");
    bakOff.appendChild(document.createTextNode("BO-80201-24203-INR"));
    roothead4.appendChild(bakOff);
   
    Element extAcc = document.createElement("ns2:ExternalAccountNo");
    extAcc.appendChild(document.createTextNode("EXT-80201-24203-INR"));
    roothead4.appendChild(extAcc);
   
    Element accType = document.createElement("ns2:AccountType");
    accType.appendChild(document.createTextNode("YU"));
    roothead4.appendChild(accType);
   
    Element spMneumonic = document.createElement("ns2:SPSKMnemonic");
    spMneumonic.appendChild(document.createTextNode("SP628"));
    roothead4.appendChild(spMneumonic);
   
    Element spCat = document.createElement("ns2:SPSKCategoryCode");
    spCat.appendChild(document.createTextNode("24203"));
    roothead4.appendChild(spCat);
   
    Element dbtCrFlag = document.createElement("ns2:DebitCreditFlag");
    dbtCrFlag.appendChild(document.createTextNode("D"));
    roothead4.appendChild(dbtCrFlag);
   
    Element trCode = document.createElement("ns2:TransactionCode");
    trCode.appendChild(document.createTextNode("010"));
    roothead4.appendChild(trCode);
   
    Element postingAmt = document.createElement("ns2:PostingAmount");
    postingAmt.appendChild(document.createTextNode("24657"));
    roothead4.appendChild(postingAmt);
   
    Element postingCcy = document.createElement("ns2:PostingCcy");
    postingCcy.appendChild(document.createTextNode("INR"));
    roothead4.appendChild(postingCcy);
   
    Element valueDate = document.createElement("ns2:ValueDate");
    valueDate.appendChild(document.createTextNode("2016-02-05"));
    roothead4.appendChild(valueDate);
   
    Element relatedParty = document.createElement("ns2:RelatedParty");
    relatedParty.appendChild(document.createTextNode("1000049958"));
    roothead4.appendChild(relatedParty);
   
    Element settlAcc = document.createElement("ns2:SettlementAccountUsed");
    settlAcc.appendChild(document.createTextNode("N"));
    roothead4.appendChild(settlAcc);
   
    Element swiftMess = document.createElement("ns2:SWIFTmessageType");
    swiftMess.appendChild(document.createTextNode("100"));
    roothead4.appendChild(swiftMess);
   
    Element serviceLevel = document.createElement("ns2:ServiceLevel");
    serviceLevel.appendChild(document.createTextNode("CRED"));
    roothead4.appendChild(serviceLevel);
   
    Element swiftCharges = document.createElement("ns2:SWIFTChargesFor");
    swiftCharges.appendChild(document.createTextNode("BEN"));
    roothead4.appendChild(swiftCharges);
   
    Element addFlag = document.createElement("ns2:AddMntDelFlag");
    addFlag.appendChild(document.createTextNode("A"));
    roothead4.appendChild(addFlag);
   
    Element roothead5 = document.createElement("ns2:ExtraData");
    roothead4.appendChild(roothead5);
   
    Element posta = document.createElement("ns4:POSTA");
    roothead5.appendChild(posta);
   


    String str = convertDocumentToString(document);
   
    String finalXML = xml1 + str + xml2;
    logger.info("XML File-------->" + finalXML);
    return finalXML;
  }
 
  private String convertDocumentToString(Document doc)
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

