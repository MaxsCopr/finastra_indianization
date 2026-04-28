<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>Viewer Payment</title>
      <jsp:include page="/view/common/header.jsp" />  
<script type="text/javascript">

      $(document).ready(function() {
            
            $("#formId :input").attr("readonly", true);
            
            $("#closeButton").click(function() {
                  $('#formId').attr('action', 'checker_multi_paymentReferences');
                  $('#formId').submit();
            });
            
      });   
      
</script>

</head>

<body class="body_bg">
      <%@ include file="/view/includes/TITLE.jsp"%>

      <boe:form method="post" id="formId" name="form">
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="closeButton">Close</a></li>
                              </ul>
                        </div>
                        <br />

                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill
                              Of Entry</h5>

                        <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">

                              <!-- First Block is Started  -->

                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">Bill Of
                                                Entry Details</h5>
                                    </div>

                                    <div class="row page_content">
                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.BillOfEntryNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billRefNo" name="boeVO.boeNo"
                                                                  readonly="true" cssClass="form-control text_box">
                                                            </boe:textfield>
                                                      </div>
                                                </div>
                                                <br />      <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.portcode" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="portcode" name="boeVO.portCode"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>                        


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.CIFNO" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="cifNoId" name="boeVO.cifNo" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text name="AD Code" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="adCode" name="boeVO.adCode"
                                                                  readonly="false" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.IEAddress" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="custName" name="boeVO.ieadd"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.benefName" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="benefName" name="boeVO.benefName"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.BillofEntryCurrency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billEntryCurrId" name="boeVO.billCurrency"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>                        
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.EndorsedAmount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="endorseAmtId" name="boeVO.endorseAmt"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />                  

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.PaymentReferenceNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="currencyId" name="boeVO.paymentRefNo"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>                                          

                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.OutstandingPayment" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="outStPayAmtId" name="boeVO.outAmt"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.PaymentDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="pdId" name="boeVO.payDate" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Exchange Rate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="boeExRate" name="boeVO.boeExRate"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.FullyAllocated" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="fullyAllocId" name="boeVO.fullyAlloc"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.igmNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="igmNo" name="boeVO.igmNo" readonly="false"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.hblNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="hblNo" name="boeVO.hblNo" readonly="false"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.mblNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="mblNo" name="boeVO.mblNo" readonly="false"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Import Agency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="imAgency" name="boeVO.imAgency"
                                                                  readonly="false" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="GP" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="govprv" name="boeVO.govprv"
                                                                  readonly="false" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Third Party Payment" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:checkbox name="boeVO.thrdParty" fieldValue="true" />
                                                      </div>
                                                </div>
                                                
                                          </div>
                                          
                                          <div class="col-md-6">
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.BillofEntryDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billEntryDateId" readonly="true"
                                                                  name="boeVO.boeDate" cssClass="form-control text_box"
                                                                  style="text-align:left;" />
                                                      </div>
                                                </div>                              
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Transaction Type" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="transType" name="boeVO.boeType"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.CustomerName" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="custName" name="boeVO.custName"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.IECode" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ieCode" name="boeVO.ieCode"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Changed IE Code" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ieCodeChange" name="boeVO.ieCodeChange"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.IEPanNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="custName" name="boeVO.iepan"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.benefCountry" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="benefCountry" name="boeVO.benefCountry"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>                                    
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.BillofEntryAmount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="bilEAId" name="boeVO.billAmt"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>                                    

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.ActualAmountAvailable" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="actualAmtId" name="boeVO.actualEndorseAmt"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.event.refNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="paySIIdd" name="boeVO.partPaymentSlNo"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.PaymentAmount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="payAmtId" name="boeVO.paymentAmount"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.PaymentCurrency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="payId" name="boeVO.paymentCurr"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>                  
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Payment Amount in BOE currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="eqPaymentAmount" name="boeVO.eqPaymentAmount" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.AllocAmtPayCcy" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="benefCountry" name="boeVO.allocAmt"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.igmDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="igmDate" name="boeVO.igmDate"
                                                                  readonly="false" cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" />
                                                      </div>
                                                </div>
                                                <br />
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.hblDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="hblDate" name="boeVO.hblDate"
                                                                  readonly="false" cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" />
                                                      </div>
                                                </div>
                                                <br />


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.mblDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="mblDate" name="boeVO.mblDate"
                                                                  readonly="false" cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Port of Shipment" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="pos" name="boeVO.pos" readonly="false"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Record Indicator" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="recInd" name="boeVO.recInd"
                                                                  readonly="false" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                      
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Maker Remarks" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textarea id="remarkId" name="boeVO.remarks"
                                                                  readonly="true" cols="20" rows="5" />
                                                      </div>
                                                </div>                        
                  
                                                <br />
                                                
                                          </div>
                                    </div>

                                    <boe:hidden id="boe_DateFrom" name="boeSearchVO.boe_DateFrom" />
                                    <boe:hidden id="boe_DateTo" name="boeSearchVO.boe_DateTo" />
                                    <boe:hidden id="amountFrom" name="boeSearchVO.amountFrom" />
                                    <boe:hidden id="amountTo" name="boeSearchVO.amountTo" />
                                    <boe:hidden id="paymentRefNo" name="boeSearchVO.paymentRefNo" />
                                    <boe:hidden id="paymentSerialNo" name="boeSearchVO.paymentSerialNo" />
                                    <boe:hidden id="paymentCurrency" name="boeSearchVO.paymentCurrency" />
                                    <boe:hidden id="boeNo" name="boeSearchVO.boeNo" />
                                    <!-- First Block is Ended  -->

                              </div>
                              <div class="row page_content">
                                                <div class="col-md-12">
                                                      <div class="col-md-12">
                                                            <div class="page_collapsible" id="body-section1">
                                                                  <span></span>
                                                                  <h5 style="font-weight: bold; font-size: 11px;">
                                                                        &nbsp;
                                                                        <boe:text name="label.boe.InvoiceDetails" />
                                                                  </h5>
                                                            </div>
                                                            <div class="col-md-6">
                                                                  <div class="row page_content">
                                                                        <div class="table">
                                                                              <table border="1px" align="left">
                                                                                    <tbody>
                                                                                          <tr>
                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="label.boe.InvoiceSerialNumber" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="label.boe.InvoiceNumber" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="label.boe.InvoiceAmount" /></label></th>
                                                                                                                  
                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Invoice Currency" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="label.boe.RealizedAmount" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Realized AmountIC" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Terms Of Invoice" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Supplier Name" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Supplier Address" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Supplier Country" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Seller Name" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Seller Address" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Seller Country" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Freight Amount" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Freight Currency" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Insurance Amount" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Insurance Currency" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Agency Commission" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Agency Currency" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Discount Charges" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Discount Currency" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Miscellaneous CHARGES" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Miscellaneous Currency" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Thirdparty Name" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Thirdparty Address" /></label></th>

                                                                                                <th style="text-align: left; width: 100px; padding: 4px 5px;"><label><boe:text
                                                                                                                  name="Thirdparty Country" /></label></th>

                                                                                          </tr>
                                                                                                                                                                                    
                                                                                          <boe:iterator value="invoiceList1"  id="invoiceList2">
                                                                                                <tr>
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="invoiceSerialNumber1"></boe:property>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="invoiceNumber1"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>

                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="invoiceAmount1"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>                                                                                           
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="invoiceCurr"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="realizedAmount1"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="realizedAmountIC"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>

                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="termsofInvoice"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="supplierName"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="supplierAddress"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="supplierCountry"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="sellerName"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="sellerAddress"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="sellerCountry"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="freightAmount"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="freightCurrencyCode"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="insuranceAmount"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="insuranceCurrencyCode"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="agencyCommission"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="agencyCurrency"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="discountCharges"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="discountCurrency"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="miscellaneousCharges"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="miscellaneousCurrency"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="thirdPartyName"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="thirdPartyAddress"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="thirdPartyCountry"></boe:property>
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                </tr>
                                                                                          </boe:iterator>

                                                                                    </tbody>
                                                                              </table>
                                                                              <br></br>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                                <br />
                                          </div>
                        </div>
                  </div>
                  </div>
                  
      </boe:form>

</body>
</html>
