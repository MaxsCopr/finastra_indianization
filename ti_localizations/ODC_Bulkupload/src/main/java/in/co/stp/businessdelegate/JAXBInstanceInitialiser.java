package in.co.stp.businessdelegate;

import com.misys.tiplus2.apps.ti.service.messages.AvailBalResponseType;
import com.misys.tiplus2.apps.ti.service.messages.AvailBalType;
import com.misys.tiplus2.apps.ti.service.messages.Customer;
import com.misys.tiplus2.apps.ti.service.messages.FacilityRequestType;
import com.misys.tiplus2.apps.ti.service.messages.FacilityResponseType;
import com.misys.tiplus2.apps.ti.service.messages.ReservationRequestType;
import com.misys.tiplus2.apps.ti.service.messages.ReservationResponseType;
import com.misys.tiplus2.apps.ti.service.messages.ReservationReversalResponseType;
import com.misys.tiplus2.apps.ti.service.messages.ReservationReversalType;
import com.misys.tiplus2.apps.ti.service.messages.STRAccountOfficerType;
import com.misys.tiplus2.apps.ti.service.messages.STRAccountType;
import com.misys.tiplus2.apps.ti.service.messages.STRBICPlusIBANType;
import com.misys.tiplus2.apps.ti.service.messages.STRBaseRateCodeType;
import com.misys.tiplus2.apps.ti.service.messages.STRBaseRateType;
import com.misys.tiplus2.apps.ti.service.messages.STRCountryCalendarType;
import com.misys.tiplus2.apps.ti.service.messages.STRCurrencyCalendarType;
import com.misys.tiplus2.apps.ti.service.messages.STRCurrencySpotRateType;
import com.misys.tiplus2.apps.ti.service.messages.STRFXRateType;
import com.misys.tiplus2.apps.ti.service.messages.STRSwiftAddressType;
import com.misys.tiplus2.apps.ti.service.messages.SWOPFType;
import com.misys.tiplus2.apps.ti.service.messages.SearchMastersRequestType;
import com.misys.tiplus2.services.control.ServiceResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JAXBInstanceInitialiser
{
  private static final Logger logger = LogManager.getLogger(JAXBInstanceInitialiser.class);
  private static JAXBContext availBalContext_Req = null;
  private static JAXBContext availBalContext_Res = null;
  private static JAXBContext limitFacilityContext_Req = null;
  private static JAXBContext limitFacilityContext_Res = null;
  private static JAXBContext limitReservationContext_Req = null;
  private static JAXBContext limitReservationContext_Res = null;
  private static JAXBContext limitReservationReversalContext_Req = null;
  private static JAXBContext limitReservationReversalContext_Res = null;
  private static JAXBContext backOfficeBatchContext_Req = null;
  private static JAXBContext backOfficeBatchContext_Res = null;
  private static JAXBContext backOfficeVerification_Req = null;
  private static JAXBContext backOfficeVerification_Res = null;
  private static JAXBContext backOfficeFXUtilization_Req = null;
  private static JAXBContext backOfficeFXUtilization_Res = null;
  private static JAXBContext businessSupportFXContracts_Req = null;
  private static JAXBContext businessSupportFXContracts_Res = null;
  private static JAXBContext swiftOutContext_Req = null;
  private static JAXBContext swiftOutContext_Res = null;
  private static JAXBContext serviceResponse = null;
  private static JAXBContext swiftInContext_Req = null;
  private static JAXBContext swiftInContext_Res = null;
  private static JAXBContext backOfficeBulkService_Req = null;
  private static JAXBContext backOfficeBulkService_Res = null;
  private static JAXBContext fxRate_StaticData = null;
  private static JAXBContext spotRate_StaticData = null;
  private static JAXBContext biRate_StaticData = null;
  private static JAXBContext biRateCode_StaticData = null;
  private static JAXBContext relationManager_StaticData = null;
  private static JAXBContext customer_StaticData = null;
  private static JAXBContext account_StaticData = null;
  private static JAXBContext searchMasters_RequestData = null;
  private static JAXBContext strBicIbanContext_Req = null;
  private static JAXBContext strSwiftAddressContext_Req = null;
  private static JAXBContext strCountryCalendarContext_Req = null;
  private static JAXBContext strCurrencyCalendarContext_Req = null;
  private static JAXBContext strHolidaysCurrencyCalendar_req = null;
 
  public static JAXBContext getAvailBalRequestContext()
  {
    if (availBalContext_Req == null)
    {
      logger.debug("availBalContext_Req is null, so creating new context");
      synchronized (JAXBContext.class)
      {
        if (availBalContext_Req == null)
        {
          logger.debug("Context is null, so creating new context inside SYNCHRONISED");
          try
          {
            availBalContext_Req = JAXBContext.newInstance(new Class[] { AvailBalType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return availBalContext_Req;
  }
 
  public static JAXBContext getAvailBalResponseContext()
  {
    if (availBalContext_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (availBalContext_Res == null)
        {
          logger.debug("availBalContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            availBalContext_Res = JAXBContext.newInstance(new Class[] { AvailBalResponseType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return availBalContext_Res;
  }
 
  public static JAXBContext getlimitFacilityRequestContext()
  {
    if (limitFacilityContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (limitFacilityContext_Req == null)
        {
          logger.debug("limitFacilityContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            limitFacilityContext_Req = JAXBContext.newInstance(new Class[] { FacilityRequestType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return limitFacilityContext_Req;
  }
 
  public static JAXBContext getLimitFacilityResponseContext()
  {
    if (limitFacilityContext_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (limitFacilityContext_Res == null)
        {
          logger.debug("limitFacilityContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            limitFacilityContext_Res = JAXBContext.newInstance(new Class[] { FacilityResponseType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return limitFacilityContext_Res;
  }
 
  public static JAXBContext getLimitReservationRequestContext()
  {
    if (limitReservationContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (limitReservationContext_Req == null)
        {
          logger.debug("limitReservationContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            limitReservationContext_Req = JAXBContext.newInstance(new Class[] { ReservationRequestType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return limitReservationContext_Req;
  }
 
  public static JAXBContext getLimitReservationResponseContext()
  {
    if (limitReservationContext_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (limitReservationContext_Res == null)
        {
          logger.debug("limitReservationContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            limitReservationContext_Res = JAXBContext.newInstance(new Class[] { ReservationResponseType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return limitReservationContext_Res;
  }
 
  public static JAXBContext getLimitReservationReversalRequestContext()
  {
    if (limitReservationReversalContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (limitReservationReversalContext_Req == null)
        {
          logger.debug("limitReservationReversalContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            limitReservationReversalContext_Req = JAXBContext.newInstance(new Class[] { ReservationReversalType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return limitReservationReversalContext_Req;
  }
 
  public static JAXBContext getLimitReservationReversalResponseContext()
  {
    if (limitReservationReversalContext_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (limitReservationReversalContext_Res == null)
        {
          logger.debug("limitReservationReversalContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            limitReservationReversalContext_Res = JAXBContext.newInstance(new Class[] { ReservationReversalResponseType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return limitReservationReversalContext_Res;
  }
 
  public static JAXBContext getBackOfficeBatchRequestContext()
  {
    if (backOfficeBatchContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (backOfficeBatchContext_Req == null)
        {
          logger.debug("backOfficeBatchContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            backOfficeBatchContext_Req =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return backOfficeBatchContext_Req;
  }
 
  public static JAXBContext getBackOfficeBatchResponseContext()
  {
    if (backOfficeBatchContext_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (backOfficeBatchContext_Res == null)
        {
          logger.debug("backOfficeBatchContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            backOfficeBatchContext_Req =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return backOfficeBatchContext_Res;
  }
 
  public static JAXBContext getBackOfficeRequestVerification()
  {
    if (backOfficeVerification_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (backOfficeVerification_Req == null)
        {
          logger.debug("backOfficeVerification_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            backOfficeVerification_Req =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return backOfficeVerification_Req;
  }
 
  public static JAXBContext getBackOfficeResponseVerification()
  {
    if (backOfficeVerification_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (backOfficeVerification_Res == null)
        {
          logger.debug("backOfficeVerification_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            backOfficeVerification_Res =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return backOfficeVerification_Res;
  }
 
  public static JAXBContext getSwiftOutRequestContext()
  {
    if (swiftOutContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (swiftOutContext_Req == null)
        {
          logger.debug("swiftOutContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            swiftOutContext_Req = JAXBContext.newInstance(new Class[] { SWOPFType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return swiftOutContext_Req;
  }
 
  public static JAXBContext getSwiftOutResponseContext()
  {
    if (swiftOutContext_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (swiftOutContext_Res == null)
        {
          logger.debug("swiftOutContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            swiftOutContext_Res = JAXBContext.newInstance(new Class[] { ServiceResponse.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return swiftOutContext_Res;
  }
 
  public static JAXBContext getServiceResponseContext()
  {
    if (serviceResponse == null)
    {
      logger.debug("serviceResponse is null, so creating new context");
      synchronized (JAXBContext.class)
      {
        if (serviceResponse == null)
        {
          logger.debug("Context is null, so creating new context inside SYNCHRONISED");
          try
          {
            serviceResponse = JAXBContext.newInstance(new Class[] { ServiceResponse.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return serviceResponse;
  }
 
  public static JAXBContext getFXRateRequestContext()
  {
    if (fxRate_StaticData == null) {
      synchronized (JAXBContext.class)
      {
        if (fxRate_StaticData == null)
        {
          logger.debug("fxRate_StaticData is null, so creating new context inside SYNCHRONISED");
          try
          {
            fxRate_StaticData = JAXBContext.newInstance(new Class[] { STRFXRateType.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return fxRate_StaticData;
  }
 
  public static JAXBContext getSpotRateRequestContext()
  {
    if (spotRate_StaticData == null) {
      synchronized (JAXBContext.class)
      {
        if (spotRate_StaticData == null)
        {
          logger.debug("spotRate_StaticData is null, so creating new context inside SYNCHRONISED");
          try
          {
            spotRate_StaticData = JAXBContext.newInstance(new Class[] { STRCurrencySpotRateType.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return spotRate_StaticData;
  }
 
  public static JAXBContext getBaseRateRequestContext()
  {
    if (biRate_StaticData == null) {
      synchronized (JAXBContext.class)
      {
        if (biRate_StaticData == null)
        {
          logger.debug("biRate_StaticData is null, so creating new context inside SYNCHRONISED");
          try
          {
            biRate_StaticData = JAXBContext.newInstance(new Class[] { STRBaseRateType.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return biRate_StaticData;
  }
 
  public static JAXBContext getBaseRateCodeRequestContext()
  {
    if (biRateCode_StaticData == null) {
      synchronized (JAXBContext.class)
      {
        if (biRateCode_StaticData == null)
        {
          logger.debug("biRateCode_StaticData is null, so creating new context inside SYNCHRONISED");
          try
          {
            biRateCode_StaticData = JAXBContext.newInstance(new Class[] { STRBaseRateCodeType.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return biRateCode_StaticData;
  }
 
  public static JAXBContext getAccountOfficerRequestContext()
  {
    if (relationManager_StaticData == null) {
      synchronized (JAXBContext.class)
      {
        if (relationManager_StaticData == null)
        {
          logger.debug("relationManager_StaticData is null, so creating new context inside SYNCHRONISED");
          try
          {
            relationManager_StaticData = JAXBContext.newInstance(new Class[] { STRAccountOfficerType.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return relationManager_StaticData;
  }
 
  public static JAXBContext getCustomerRequestContext()
  {
    if (customer_StaticData == null) {
      synchronized (JAXBContext.class)
      {
        if (customer_StaticData == null)
        {
          logger.debug("customer_StaticData is null, so creating new context inside SYNCHRONISED");
          try
          {
            customer_StaticData = JAXBContext.newInstance(new Class[] { Customer.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return customer_StaticData;
  }
 
  public static JAXBContext getAccountRequestContext()
  {
    if (account_StaticData == null) {
      synchronized (JAXBContext.class)
      {
        if (account_StaticData == null)
        {
          logger.debug("account_StaticData is null, so creating new context inside SYNCHRONISED");
          try
          {
            account_StaticData = JAXBContext.newInstance(new Class[] { STRAccountType.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return account_StaticData;
  }
 
  public static JAXBContext getHolidaysRequestContext()
  {
    if (strHolidaysCurrencyCalendar_req == null) {
      synchronized (JAXBContext.class)
      {
        if (strHolidaysCurrencyCalendar_req == null)
        {
          logger.debug("strHolidaysCurrencyCalendar_req is null, so creating new context inside SYNCHRONISED");
          try
          {
            strHolidaysCurrencyCalendar_req = JAXBContext.newInstance(new Class[] { STRCurrencyCalendarType.class });
          }
          catch (JAXBException jaxbExp)
          {
            logger.error(jaxbExp);
          }
        }
      }
    }
    return strHolidaysCurrencyCalendar_req;
  }
 
  public static JAXBContext getSwiftInRequestContext()
  {
    if (swiftInContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (swiftInContext_Req == null)
        {
          logger.debug("swiftInContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            swiftInContext_Req =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return swiftInContext_Req;
  }
 
  public static JAXBContext getSwiftInResponseContext()
  {
    if (swiftInContext_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (swiftInContext_Res == null)
        {
          logger.debug("swiftInContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            swiftInContext_Res =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return swiftInContext_Res;
  }
 
  public static JAXBContext getSearchMasterRequestContext()
  {
    if (searchMasters_RequestData == null) {
      synchronized (JAXBContext.class)
      {
        if (searchMasters_RequestData == null)
        {
          logger.debug("searchMasters_RequestData is null, so creating new context inside SYNCHRONISED");
          try
          {
            searchMasters_RequestData = JAXBContext.newInstance(new Class[] { SearchMastersRequestType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return searchMasters_RequestData;
  }
 
  public static JAXBContext getBackOfficeFXUtilizationRequestContext()
  {
    if (backOfficeFXUtilization_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (backOfficeFXUtilization_Req == null)
        {
          logger.debug("backOfficeFXUtilization_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            backOfficeFXUtilization_Req =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return backOfficeFXUtilization_Req;
  }
 
  public static JAXBContext getBackOfficeFXUtilizationResponseContext()
  {
    if (backOfficeFXUtilization_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (backOfficeBatchContext_Res == null)
        {
          logger.debug("backOfficeBatchContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            backOfficeBatchContext_Req =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return backOfficeFXUtilization_Res;
  }
 
  public static JAXBContext getBusinessSupportFXRateRequestContext()
  {
    if (businessSupportFXContracts_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (businessSupportFXContracts_Req == null)
        {
          logger.debug("businessSupportFXContracts_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            businessSupportFXContracts_Req =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return businessSupportFXContracts_Req;
  }
 
  public static JAXBContext getBusinessSupportFXRateResponseContext()
  {
    if (businessSupportFXContracts_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (businessSupportFXContracts_Res == null)
        {
          logger.debug("businessSupportFXContracts_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            businessSupportFXContracts_Res =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return businessSupportFXContracts_Res;
  }
 
  public static JAXBContext getSTRBicIbanRequestContext()
  {
    if (strBicIbanContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (strBicIbanContext_Req == null)
        {
          logger.debug("swiftInContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            strBicIbanContext_Req = JAXBContext.newInstance(new Class[] { STRBICPlusIBANType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return strBicIbanContext_Req;
  }
 
  public static JAXBContext getSTRSwiftAddressRequestContext()
  {
    if (strSwiftAddressContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (strSwiftAddressContext_Req == null)
        {
          logger.debug("strSwiftAddressContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            strSwiftAddressContext_Req = JAXBContext.newInstance(new Class[] { STRSwiftAddressType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return strSwiftAddressContext_Req;
  }
 
  public static JAXBContext getSTRCountryCalendarRequestContext()
  {
    if (strCountryCalendarContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (strCountryCalendarContext_Req == null)
        {
          logger.debug("strCountryCalendarContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            strCountryCalendarContext_Req = JAXBContext.newInstance(new Class[] { STRCountryCalendarType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return strCountryCalendarContext_Req;
  }
 
  public static JAXBContext getSTRCurrencyCalendarRequestContext()
  {
    if (strCurrencyCalendarContext_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (strCurrencyCalendarContext_Req == null)
        {
          logger.debug("strCurrencyCalendarContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            strCurrencyCalendarContext_Req = JAXBContext.newInstance(new Class[] { STRCurrencyCalendarType.class });
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return strCurrencyCalendarContext_Req;
  }
 
  public static JAXBContext getBackOfficeBulkRequestContext()
  {
    if (backOfficeBulkService_Req == null) {
      synchronized (JAXBContext.class)
      {
        if (backOfficeBulkService_Req == null)
        {
          logger.debug("backOfficeBatchContext_Req is null, so creating new context inside SYNCHRONISED");
          try
          {
            backOfficeBulkService_Req =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return backOfficeBulkService_Req;
  }
 
  public static JAXBContext getBackOfficeBulkResponseContext()
  {
    if (backOfficeBulkService_Res == null) {
      synchronized (JAXBContext.class)
      {
        if (backOfficeBulkService_Res == null)
        {
          logger.debug("backOfficeBatchContext_Res is null, so creating new context inside SYNCHRONISED");
          try
          {
            backOfficeBulkService_Res =
              JAXBContext.newInstance("com.misys.tiplus2.apps.ti.service.messages");
          }
          catch (JAXBException e)
          {
            logger.error(e.getMessage(), e);
          }
        }
      }
    }
    return backOfficeBulkService_Res;
  }
 
  public static void initializeAllContext()
  {
    logger.debug("Starting initialising Context");
    getAvailBalRequestContext();
    getAvailBalResponseContext();
    getlimitFacilityRequestContext();
    getLimitFacilityResponseContext();
    getLimitReservationRequestContext();
    getLimitReservationResponseContext();
    getLimitReservationReversalRequestContext();
    getLimitReservationReversalResponseContext();
    getSwiftOutRequestContext();
    getSwiftOutResponseContext();
    getBackOfficeBatchRequestContext();
    getBackOfficeBatchResponseContext();
    getBackOfficeRequestVerification();
    getBackOfficeResponseVerification();
    getBackOfficeFXUtilizationRequestContext();
    getBackOfficeFXUtilizationResponseContext();
    getBusinessSupportFXRateRequestContext();
    getBusinessSupportFXRateResponseContext();
    getServiceResponseContext();
    getFXRateRequestContext();
    getBaseRateRequestContext();
    getAccountRequestContext();
    getCustomerRequestContext();
    getAccountOfficerRequestContext();
    getSwiftInRequestContext();
    getSwiftInResponseContext();
    getSTRBicIbanRequestContext();
    getSTRCountryCalendarRequestContext();
    getSTRCurrencyCalendarRequestContext();
    getHolidaysRequestContext();
  }
}
