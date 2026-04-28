package com.bs.wiseconnect.migration.loader.data.handler;
 
import com.bs.wiseconnect.migration.loader.tiplus.pojos.InvoiceCustomer;
import com.bs.wiseconnect.migration.loader.utility.CommonUtils;
import com.bs.wiseconnect.migration.loader.utility.MessageUtil;
import com.misys.tiplus2.apps.ti.service.common.EnigmaBoolean;

import com.misys.tiplus2.apps.ti.service.common.GWRCustomer;

import com.misys.tiplus2.apps.ti.service.common.GWRMoney;

import com.misys.tiplus2.apps.ti.service.common.GatewayContext;

import com.misys.tiplus2.apps.ti.service.messages.GWRSCFDefiningMessageBase;

import com.misys.tiplus2.apps.ti.service.messages.TFINVNEW;

import com.misys.tiplus2.services.control.ServiceRequest;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
 
public class TFinvdtoHandler

{

  private static final Logger logger = LoggerFactory.getLogger(TFinvdtoHandler.class);

  public ServiceRequest createTFinvdtoRequest(List<InvoiceCustomer> list, String userName, String userTeam, String product, String financeFlag)

  {

    ServiceRequest sRequestItems = new ServiceRequest();

    for (InvoiceCustomer lst : list)

    {

      ServiceRequest tfinvdtoRequest = MessageUtil.getServiceRequest(

        "TI", "TFINVNEW", userName, "FULL", "N", "N", lst.getTicorrid());

      List<JAXBElement<?>> tfil = tfinvdtoRequest.getRequest();

      com.misys.tiplus2.apps.ti.service.messages.ObjectFactory of = new com.misys.tiplus2.apps.ti.service.messages.ObjectFactory();

      com.misys.tiplus2.apps.ti.service.common.ObjectFactory objFactory = new com.misys.tiplus2.apps.ti.service.common.ObjectFactory();

      TFINVNEW tfnew = new TFINVNEW();

      GWRSCFDefiningMessageBase gwsd = new GWRSCFDefiningMessageBase();

 
 
      tfnew.setTheirRef(lst.getTheirref());

      GatewayContext gc = new GatewayContext();

      gc.setTeam(userTeam);

      gc.setBranch(lst.getBranch());

      logger.info("XML Product Type : " + product);

      gc.setProduct(product);

      gc.setCustomer(lst.getCustomer());

      gc.setBehalfOfBranch(lst.getBehalfofbranch());

      tfnew.setContext(gc);

      tfnew.setProgramme(lst.getProgramme());

      tfnew.setBuyer(lst.getBuyer());

      tfnew.setSeller(lst.getSeller());

      tfnew.setBatchID(lst.getBatchid());

 
      GWRMoney gmon = new GWRMoney();

      gmon.setAmount(lst.getFaceamount());

      gmon.setCurrency(lst.getFaceccy());

      tfnew.setFaceValue(gmon);

      GWRCustomer gwcus = new GWRCustomer();

      gwcus.setMnemonic(lst.getMnemonic());

      gwcus.setSourceBankingBusiness(lst.getSourcebankingbuisness());

      tfnew.setAnchorParty(gwcus);

 
      tfnew.setIssueDate(CommonUtils.nullHandler(of.createGWRINEWIssueDate(CommonUtils.getXmlGregorianDate(lst.getIssueddate()))));

 
 
      tfnew.setInvoiceNumber(lst.getInvoicenumber());

 
 
 
      tfnew.setSettlementDate(CommonUtils.nullHandler(of.createGWRINEWSettlementDate(CommonUtils.getXmlGregorianDate(lst.getSettelementdate()))));

      logger.info("ddd" + financeFlag);

      Boolean fl = Boolean.valueOf(lst.getFlag());

      logger.info("get FLAG " + lst.getFlag());

      tfnew.setInvoiceApproved(EnigmaBoolean.valueOf(lst.getFlag().toUpperCase()));

 
 
 
      logger.info("ddd-->" + tfnew.getInvoiceApproved());

      JAXBElement<TFINVNEW> tfinvjaxb = of.createTFINVNEW(tfnew);

      tfil.add(tfinvjaxb);

 
      sRequestItems.getRequest().add(tfinvjaxb);

      sRequestItems.setRequestHeader(MessageUtil.getRequestHeader("TI", "TFINVNEW", userName, "FULL", "N", "N", lst.getTicorrid()));

    }

    return sRequestItems;

  }

}