package com.bs.wiseconnect.migration.loader.data.handler;
 
import com.bs.wiseconnect.migration.loader.tiplus.pojos.TFBuyFin;
import com.bs.wiseconnect.migration.loader.utility.CommonUtils;
import com.bs.wiseconnect.migration.loader.utility.MessageUtil;
import com.misys.tiplus2.apps.ti.service.common.GWRCustomer;

import com.misys.tiplus2.apps.ti.service.common.GatewayContext;

import com.misys.tiplus2.apps.ti.service.messages.GWRSCFDiscount.InvoiceNumberss;

import com.misys.tiplus2.apps.ti.service.messages.GWRSCFDiscount.InvoiceNumberss.InvoiceNumbers;

import com.misys.tiplus2.apps.ti.service.messages.GatewayBasePane.DocumentsReceiveds;

import com.misys.tiplus2.apps.ti.service.messages.GatewayBasePane.DocumentsReceiveds.DocumentsReceived;

import com.misys.tiplus2.apps.ti.service.messages.TFBUYFIN;

import com.misys.tiplus2.services.control.ServiceRequest;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
 
public class TFBuyerFinHandler

{

  private static final Logger logger = LoggerFactory.getLogger(TFBuyerFinHandler.class);

  public ServiceRequest createTFBuyFinRequest(List<TFBuyFin> list, String userTeam)

  {

    ServiceRequest sRequestItems = new ServiceRequest();

    for (TFBuyFin lst : list)

    {

      ServiceRequest tfilCdtoRequest = MessageUtil.getServiceRequest(

        "TI", "TFBUYFIN", "SUPERVISOR", "FULL", "N", "N", lst.getTiCorrID());

      List<JAXBElement<?>> tfil = tfilCdtoRequest.getRequest();

      com.misys.tiplus2.apps.ti.service.messages.ObjectFactory of = new com.misys.tiplus2.apps.ti.service.messages.ObjectFactory();

      com.misys.tiplus2.apps.ti.service.common.ObjectFactory objFactory = new com.misys.tiplus2.apps.ti.service.common.ObjectFactory();

      TFBUYFIN tfbuyFin = new TFBUYFIN();

      tfbuyFin.setProductType("VIR");

      tfbuyFin.setProgramme(lst.getProgramme());

      tfbuyFin.setBuyer(lst.getBuyer());

      tfbuyFin.setSeller(lst.getSeller());

      GWRCustomer gwrCus = new GWRCustomer();

      gwrCus.setMnemonic(lst.getBuyer());

      gwrCus.setSourceBankingBusiness(lst.getSourcebankingbuisness());

      tfbuyFin.setAnchorParty(gwrCus);

      tfbuyFin.setReceivedOn(CommonUtils.nullHandler(of.createGWRSCFDefiningMessageBaseReceivedOn(CommonUtils.getXmlGregorianDate(lst.getIssueDate()))));

      tfbuyFin.setMaturityDate(CommonUtils.nullHandler(of.createGWRSCFDiscountMaturityDate(CommonUtils.getXmlGregorianDate(lst.getMaturityDate()))));

      tfbuyFin.setFinanceCurrency(lst.getFinanceCurrency());

      tfbuyFin.setFinancePercent(CommonUtils.StringtoBigDecimal(lst.getFinancePercent()));

      tfbuyFin.setFinanceDate(CommonUtils.nullHandler(of.createGWRSCFDiscountFinanceDate(CommonUtils.getXmlGregorianDate(lst.getFinanceDate()))));

      tfbuyFin.setFinanceInstructions(lst.getFinanceInstructions());

      tfbuyFin.setFinanceOfferOnly(CommonUtils.EnigmaBooleanHandler(lst.getFinanceOfferOnly()));

      tfbuyFin.setEBankMasterRef(lst.getMasterRef());

      GWRSCFDiscount.InvoiceNumberss invNums = new GWRSCFDiscount.InvoiceNumberss();

      GWRSCFDiscount.InvoiceNumberss.InvoiceNumbers invNum = new GWRSCFDiscount.InvoiceNumberss.InvoiceNumbers();

      invNum.setInvoiceNumber(CommonUtils.nullHandler(of.createGWRSCFDiscountInvoiceNumberssInvoiceNumbersInvoiceNumber(lst.getInvoiceNumber())));

      invNum.setIssueDate(CommonUtils.nullHandler(of.createGWRSCFDiscountInvoiceNumberssInvoiceNumbersIssueDate(CommonUtils.getXmlGregorianDate(lst.getIssueDate()))));

      invNum.setOutstandingAmount(CommonUtils.RemoveDecimal(lst.getOutstandingAmount()));

      invNum.setOutstandingAmountCurrency(CommonUtils.nullHandler(of.createGWRSCFDiscountInvoiceNumberssInvoiceNumbersOutstandingAmountCurrency(lst.getOutstandingAmountCurrency())));

      invNums.getInvoiceNumbers().add(invNum);

      tfbuyFin.setInvoiceNumberss(invNums);

      tfbuyFin.setEBankMasterRef(lst.geteBankMasterRef());

      tfbuyFin.setEBankEventRef(lst.geteBankEventRef());

      GatewayBasePane.DocumentsReceiveds docRcvds = new GatewayBasePane.DocumentsReceiveds();

      GatewayBasePane.DocumentsReceiveds.DocumentsReceived docRcvd = new GatewayBasePane.DocumentsReceiveds.DocumentsReceived();

      docRcvd.setDocID(lst.getDocID());

      docRcvd.setDocType(lst.getDocType());

      docRcvd.setDMSID(lst.getDmsID());

      docRcvd.setDescription(lst.getDescription());

      docRcvd.setReceivedDate(CommonUtils.nullHandler(of.createGWRCPOReceiveDate(CommonUtils.getXmlGregorianDate(lst.getReceivedDate()))));

      docRcvd.setReceivedTime(lst.getReceivedTime());

      docRcvds.getDocumentsReceived().add(docRcvd);

      tfbuyFin.setDocumentsReceiveds(docRcvds);

      GatewayContext gateCtx = new GatewayContext();

      gateCtx.setBranch(lst.getBranch());

      gateCtx.setCustomer(lst.getBuyer());

      gateCtx.setBehalfOfBranch(lst.getBehalfofbranch());

      gateCtx.setTeam("REPAIR");

      gateCtx.setProduct(lst.getProduct());

      tfbuyFin.setContext(gateCtx);

 
 
      JAXBElement<TFBUYFIN> tfbuyFinJAXB = of.createTFBUYFIN(tfbuyFin);

      tfil.add(tfbuyFinJAXB);

      sRequestItems.getRequest().add(tfbuyFinJAXB);

      sRequestItems.setRequestHeader(MessageUtil.getRequestHeader("TI", "TFBUYFIN", "SUPERVISOR", "FULL", "N", "N", lst.getTiCorrID()));

    }

    return sRequestItems;

  }

}