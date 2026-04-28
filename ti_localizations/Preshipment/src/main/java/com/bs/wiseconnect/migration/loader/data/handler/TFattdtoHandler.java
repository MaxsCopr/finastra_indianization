package com.bs.wiseconnect.migration.loader.data.handler;
 
import com.bs.wiseconnect.migration.loader.tiplus.pojos.TiattdocExtra;
import com.bs.wiseconnect.migration.loader.utility.MessageUtil;
import com.misys.tiplus2.apps.ti.service.common.GatewayContext;

import com.misys.tiplus2.apps.ti.service.custom.Event;

import com.misys.tiplus2.apps.ti.service.messages.TFATTDOC;

import com.misys.tiplus2.services.control.ServiceRequest;

import java.math.BigDecimal;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
 
public class TFattdtoHandler

{

  private static final Logger logger = LoggerFactory.getLogger(TFattdtoHandler.class);

  public ServiceRequest createTFattdtoRequest(List<TiattdocExtra> list, String batchid, BigDecimal repayAmt, String currency, String Masref, String Everef)

  {

    TFATTDOC objTFCPCCRT = new TFATTDOC();

    ServiceRequest sRequestItems = new ServiceRequest();

    for (TiattdocExtra lst : list)

    {

      logger.info("entered xml genereation ------------------------------>");

      ServiceRequest tfinvdtoRequest = MessageUtil.getServiceRequest(

        "TI", "TFATTDOC", "SUPERVISOR", "FULL", "N", "N", lst.getTicorrid());

      List<JAXBElement<?>> tfil = tfinvdtoRequest.getRequest();

      com.misys.tiplus2.apps.ti.service.messages.ObjectFactory of = new com.misys.tiplus2.apps.ti.service.messages.ObjectFactory();

      com.misys.tiplus2.apps.ti.service.common.ObjectFactory objFactory = new com.misys.tiplus2.apps.ti.service.common.ObjectFactory();

      TFATTDOC tfat = new TFATTDOC();

      GatewayContext gc = new GatewayContext();

      gc.setBehalfOfBranch(lst.getBehalfofbranch());

      gc.setBranch(lst.getBranch());

      gc.setCustomer(lst.getCustomer());

      gc.setProduct(lst.getProduct());

      gc.setEvent(lst.getEvent());

      gc.setOurReference(lst.getTheirreference());

      gc.setTeam(lst.getTeam());

      tfat.setContext(gc);

 
 
      Event ilce = new Event();

      String ammt = repayAmt.toString().trim();

      String famt = ammt + currency;

      ilce.setREPAYAMT(famt);

      ilce.setREPMAS(Masref);

      ilce.setREPEVNT(Everef);

 
 
 
 
 
 
 
 
 
      tfat.setExtraData(ilce);

 
 
      JAXBElement<TFATTDOC> tfattdocjaxb = of.createTFATTDOC(tfat);

      tfil.add(tfattdocjaxb);

 
      sRequestItems.getRequest().add(tfattdocjaxb);

      sRequestItems.setRequestHeader(MessageUtil.getRequestHeader("TI", "TFATTDOC", "SUPERVISOR", "FULL", "N", "N", lst.getTicorrid()));

    }

    logger.info("req is " + sRequestItems);

    return sRequestItems;

  }

}