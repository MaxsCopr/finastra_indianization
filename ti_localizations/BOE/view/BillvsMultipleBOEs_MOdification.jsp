<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>Bill vs Multiple BOEs_Modification</title>
      <jsp:include page="/view/common/header.jsp" />  
      <script type="text/javascript" src="boe/scripts/manyBill/manybill.js"></script>
      <script type="text/javascript" src="boe/scripts/manyBill/manybill1.js"></script>    
      
</head>

<body class="body_bg">

      <%@ include file="/view/includes/TITLE.jsp"%>

      <boe:form method="post" id="formId" name="form">
      
            <div class="row">
            
                  <div class="col-md-2">
                  
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <!-- <li style="text-align: center;"><a href="BillEnteryForm">Close</a></li> -->
                                    <li style="text-align: center;"><a href="#" onclick="pressCloseBtn()">Close</a></li>
                              </ul>
                        </div>
                        <br />
                        
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="modify">Modify</a></li>
                              </ul>
                        </div>
                        <br />

                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill Of Entry</h5>                        

                        <div id="userIdMessage" style="color: orange"></div>
                        
                        <div class="row cont_colaps">

                              <!-- Error table begin -->                            

                              <div class="col-md-12">

                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">Error Description</h5>
                                    </div>
                                    
                                    <div class="form-group">
                                    
                                          <div class="error_table">
                                          
                                                <table border="1px" align="left" id="errorAlert">
                                                      <tbody>
                                                            <tr>
                                                                  <th style="text-align: left; width: 200px; padding:4px 5px;" >
                                                                        <label><boe:text name="label.boe.Severity"/></label></th>

                                                                  <th style="text-align: left; width: 200px; padding:4px 5px;">
                                                                        <label><boe:text name="label.boe.Description"/></label></th>

                                                                  <th style="text-align: left; width: 200px; padding:4px 5px;">
                                                                        <label><boe:text name="label.boe.Steps"/></label></th>

                                                                  <th style="text-align: left; width: 300px; padding:4px 5px;">
                                                                        <label><boe:text name="label.boe.Details"/></label></th>

                                                                  <th style="text-align: left; width: 200px; padding:4px 5px;">
                                                                        <label><boe:text name="label.boe.Overridden"/></label></th>
                                                            </tr>
                                                            
                                                            <boe:iterator value="alertMsgArray" id="alertMsgArray">                                                           
                                                                  <tr class="boeErrorMsg" ondblclick ="overRide(this)" >
                                                                        <td style="text-align: left; padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorId" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="text-align: left; padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorDesc" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="text-align: left; padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorCode" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="text-align: left; padding:4px 5px;">
                                                                              <div class="form-group">
                                                                                    <boe:property value="errorDetails" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="text-align: left; padding:4px 5px;" id="">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorMsg"/>
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                  </tr>
                                                            </boe:iterator>
                                                      </tbody>
                                                </table>
                                          </div>
                                    </div>
                                    <br />
                              </div>

                              <br />
                              <!-- Error table End -->


                              <!-- First Block is Started  -->

                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">Bill Of
                                                Entry</h5>
                                    </div>

                                    <div class="row page_content">
                                    
                                          <div class="col-md-6">
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.BillOfEntryNo" /></label>
                                                       <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billRefNo" name="boeVO.boeNo" maxlength="7"
                                                                  onkeypress="return isNumber(event)" cssClass="form-control text_box">
                                                            </boe:textfield>                                                        
                                                      </div>
                                                </div>
                                                
                                                <%-- <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;"></label>
                                                       <div class="col-md-3 input-group input-group-md">
                                                       <boe:textfield> </boe:textfield>                                                         
                                                            <input type="button" value="<boe:text name="label.boe.Endorse" />"
                                                                  onclick="retriveDataBasedOnBillNO()" class="button" id="endorseButton" />
                                                      </div>
                                                </div> --%>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.portcode"/>&nbsp;<!-- <a href="#" onclick="getDischargePort()">
                                                            <img src="images/magnify.png" width="13%" height="13%" /> --></a></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="portcode" name="boeVO.portCode"  
                                                              readonly="false"      cssClass="form-control text_box" />
                                                      </div>
                                                </div>                              

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.CIFNO" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="cifNoId" name="boeVO.cifNo"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="AD Code" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="adCode" name="boeVO.adCode" maxlength="7"
                                                                  onkeypress="return isNumber(event)" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="IE Address" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ieadd" name="boeVO.ieadd" maxlength="500"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.benefName"/></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="benefName" name="boeVO.benefName"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>                  


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.BillofEntryCurrency"/></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billEntryCurrId" name="boeVO.billCurrency"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />            
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.EndorsedAmount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="endorseAmtId" name="boeVO.endorseAmt" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />      
                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.PaymentReferenceNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="paymentRefNo" name="boeVO.paymentRefNo"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div><br />      
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.OutstandingPayment" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="outStPayAmtId" name="boeVO.outAmt"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.PaymentDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="pdId" name="boeVO.payDate" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Exchange Rate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="boeExRate" name="boeVO.boeExRate"
                                                                  onchange="amountCalculation()" onkeypress="return isNumber(event)"
                                                                  maxlength="50" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br/>             
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.FullyAllocated" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="fullyAllocId" name="boeVO.fullyAlloc"
                                                                  readonly="true" cssClass="form-control text_box" style="text-align:left;"/>
                                                      </div>
                                                </div><br />                                                      
                                                

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.igmNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="igmNo" name="boeVO.igmNo" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br/>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.hblNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="hblNo" name="boeVO.hblNo" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.mblNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="mblNo" name="boeVO.mblNo" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />      
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Import Agency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="imAgency" name="boeVO.imAgency"
                                                            readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="" />G-P</label>
                                                      <div class="col-md-3 input-group input-group-md">                                                           
                                                            <boe:textfield id="govprv" name="boeVO.govprv"
                                                                  readonly="true" cssClass="form-control text_box" />                                                                                                   
                                                      </div>                                                      
                                                </div><br />
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="BOE Record Indicator" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="recInd" name="boeVO.recInd" maxlength="1"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                                                    
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Third Party Payment" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:checkbox name="boeVO.thrdParty" fieldValue="true" id="thrdParty"/>
                                                      </div>
                                                </div>
                                                <br />
                                          </div>                                          
                                          
                                          <div class="col-md-6">
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.BillofEntryDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md input-append date" id="dp3">
                                                            <boe:textfield id="billEntryDateId" name="boeVO.boeDate"
                                                             readonly="false" cssClass="datepicker form-control text_box" style="text-align:left;" />
                                                      </div>
                                                </div>      
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Transaction Type" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="transType" name="boeVO.transType"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>      

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.CustomerName" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="custName" name="boeVO.custName"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.IECode" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ieCode" name="boeVO.ieCode" maxlength="10"
                                                                  onkeypress="return isNumber(event)"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Changed IE Code" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ieCodeChange" name="boeVO.ieCodeChange"
                                                                  maxlength="10" onkeypress="return alphanumeric(event)"
                                                                  cssClass="form-control text_box" readonly="false" />
                                                      </div>
                                                </div>                                          
                                                
                                                
                                                <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                  <boe:text name="IE PAN Number" /></label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="iepan" name="boeVO.iepan" maxlength="10"
                                                                        cssClass="form-control text_box" readonly="true" />
                                                            </div>
                                                </div>                                                      
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.benefCountry" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="benefCountry" name="boeVO.benefCountry"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.BillofEntryAmount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="bilEAId" name="boeVO.billAmt"
                                                                  readonly="true" onkeypress="return isNumber(event)"   cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.ActualAmountAvailable" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="actualAmtId"     name="boeVO.actualEndorseAmt"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.event.refNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="paySIIdd" name="boeVO.partPaymentSlNo"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.PaymentAmount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="payAmtId" name="boeVO.paymentAmount"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />                                    
                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.PaymentCurrency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="payId" name="boeVO.paymentCurr"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Endorse Amount in BOE Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="eqPaymentAmount" name="boeVO.eqPaymentAmount"
                                                            readonly="true" onkeypress="return isNumber(event)"
                                                            cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="BOE Allocated Amount in Payment currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md" style="float: left;">
                                                            <boe:textfield id="alloAmtId" name="boeVO.allocAmt"
                                                                  onkeypress="return isNumber(event)" cssClass="form-control text_box" />
                                                      </div>                                                      
                                                      <div class="col-md-2 input-group input-group-md" style="padding:2px;" >
                                                            <input type="button" value="<boe:text name="Allocate" />"
                                                                  onclick="fetchAllocateInvoice1()" class="button" id="calcInvButton" />
                                                      </div>
                                                </div>
                                                <br />                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.igmDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="igmDate" name="boeVO.igmDate"
                                                                  cssClass="datepicker form-control text_box"
                                                                  style="text-align:left;" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.hblDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="hblDate" name="boeVO.hblDate"
                                                                  readonly="true" cssClass="datepicker form-control text_box" style="text-align: left;"/>
                                                      </div>
                                                </div>
                                                <br />


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.mblDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="mblDate" name="boeVO.mblDate"
                                                                  readonly="true" cssClass="datepicker form-control text_box" style="text-align: left;" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Port of Shipment" />&nbsp;<a href="#" onclick="getShipmentPort()">
                                                            <img src="images/magnify.png" width="13%" height="13%" /></a></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="pos" name="boeVO.pos" readonly="true"
                                                                  maxlength="50" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />
                                                
<!-- Added on 19/01/2018 for BOE Extension Indicator -->

                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;"><boe:text name="BES Record Indicator" /></label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:if test="%{boeVO.okIdFlg == 'reject'}">
                                                                        <boe:textfield id="boeBesSBInd" name="boeVO.boeBesSBInd" readonly="true" cssClass="form-control text_box" />
                                                                  </boe:if>
                                                                  <boe:else>
                                                                        <boe:select id="boeBesSBInd" list="boeBesSBIndList"
                                                                        listKey="key" listValue="value" headerValue="<---->"
                                                                        headerKey="" name="boeVO.boeBesSBInd"
                                                                        style="width: 135px; height: 25px" cssClass="chosen">
                                                                        </boe:select>
                                                                  </boe:else>
                                                            </div>
                                                      </div><br />
<!-- End -->
                                                
<%--                                            <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="BES Record Indicator" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="recBesInd" name="boeVO.recBESInd" maxlength="1"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div> --%>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.Remarks" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textarea id="remarkId" name="boeVO.remarks" cols="18" rows="4"
                                                            style="margin: 0px; width: 135px; height: 74px;"/>                                              
                                                      </div>
                                                </div>
                                                <br />            
                                          
                                                </div>      
                                           </div>                       
                                          
                                    </div>

                                          <!-- First Block is Ended  -->

                                          <!-- Invoice Block is started -->

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
                                                                  <div id="selectrow" style="display: none; color: #FF0000; font-weight: bold; font-size: 16px;">
                                                                Please select a row</div> 
                                                                  <div class="row page_content">
                                                                        <div class="table">
                                                                              <table border="1px" align="left" id="invTable">
                                                                                    <thead>
                                                                                          <tr>                                                                                      
                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="label.boe.InvoiceSerialNumber" /></label></th>

                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="label.boe.InvoiceNumber" /></label></th>
                                                                                                      
                                                                                                <th style="text-align: left; width: 100px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Terms Of Invoice" /></label></th>           
                                                                                                
                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Total Invoice Amount" /></label></th>       

                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="label.boe.InvoiceAmount" /></label></th>
                                                                                                      
                                                                                                <th style="text-align: left; width: 100px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Invoice Currency" /></label></th>                                                                                                                                                                 
                                                                                                
                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Freight Amount" /></label></th> 
                                                                                                
                                                                                                <th style="text-align: left; width: 100px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Freight Currency" /></label></th>     
                                                                                                
                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Insurance Amount" /></label></th>     
                                                                                                
                                                                                                <th style="text-align: left; width: 100px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Insurance Currency" /></label></th>         
                                                                                                      
                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="label.boe.RealizedAmount" /></label></th>

                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Realized AmountIC" /></label></th>
                                                                                                      
                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Paid Realized Amount" /></label></th>

                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Paid Realized AmountIC" /></label></th>           
                                                                                                
                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="BOE Closure Amount / <br/> Endorsed By Other AD" /></label></th>          
                                                                                                
                                                                                                <th style="text-align: left; width: 200px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="OutStanding AmountIC" /></label></th>       
                                                                                                
                                                                                                <th style="text-align: left; width: 100px; padding:4px 5px; !important"><label>
                                                                                                      <boe:text name="Select" /></label></th>                                                                                             

                                                                                          </tr>                                                                                     
                                                                                          </thead>
                                                                                          
                                                                                          <tbody>
                                                                                          <c:if test="${empty invoiceList}">
                                                                                                <tr>
                                                                                                      <td colspan="17">No records found</td>
                                                                                                </tr>
                                                                                          </c:if>                                                                                         
                                                                                          <c:if test="${not empty invoiceList}">
                                                                                                                                                                                    
                                                                                          <boe:iterator value="invoiceList" id="invoiceList" status="invList" var="listItem" >
                                                                                                                                                                  
                                                                                                <tr class="invList" onclick="selectData(this)">
                                                                                                
                                                                                                      <td style="padding: 4px 5px;">      
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="invoiceSerialNumber"></boe:property>
                                                                                                            </div>
                                                                                                      </td>

                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="invoiceNumber"></boe:property>
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
                                                                                                            <div class="form-group" align="right">
                                                                                                                  <boe:property value="totalInvAmt"></boe:property>
                                                                                                                  <boe:textfield id="totalInvAmt_%{#invList.count}" name="totalInvAmount"
                                                                                                                        value="%{#listItem.totalInvAmt}" style=" display:none;" />  
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td> 

                                                                                                      <td style="padding: 4px 5px;">      
                                                                                                            <div class="form-group" align="right">
                                                                                                                  <boe:property value="invoiceAmount"></boe:property>                                                                                                               
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
                                                                                                            <div class="form-group" align="right">
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
                                                                                                            <div class="form-group" align="right">
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
<!-- Property Code Added for BOE Modification on 20/04/2018 -->
                                                                                                                  <boe:textfield id="realAmt_%{#invList.count}" name="realizedAmount"
                                                                                                                        value="%{#listItem.realizedAmount}" style="width: 110px;"
                                                                                                                        cssClass="form-control text_box" onchange = "setRealizedIC(this)"/>                                                                                                                 
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
<!-- Property Code Added for BOE Modification on 20/04/2018 -->
                                                                                                                  <boe:textfield id="realAmtIC_%{#invList.count}" name="realizedAmountIC"
                                                                                                                        cssClass="form-control text_box" value="%{#listItem.realizedAmountIC}"
                                                                                                                        style="width: 110px;" readonly = "true" />
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td> 
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="right">
                                                                                                                  <boe:property value="alreadyRealizedAmt"></boe:property>
                                                                                                                  <boe:textfield id="alRealiedAmt_%{#invList.count}" name="alreadyRealizedAmt"
                                                                                                                        value="%{#listItem.alreadyRealizedAmt}" style=" display:none;" /> 
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="right">
                                                                                                                  <boe:property value="alreadyRealizedAmtIC"></boe:property>
                                                                                                                  <boe:textfield id="alRealiedAmtIC_%{#invList.count}" name="alreadyRealizedAmtIC"
                                                                                                                        value="%{#listItem.alreadyRealizedAmtIC}" style=" display:none;" />     
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="right">
                                                                                                                  <boe:property value="closureAmt"></boe:property>                                                                                                                  
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td> 
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="right">
                                                                                                                  <boe:property value="outAmtIC"></boe:property>
                                                                                                                  <boe:textfield id="totalOutAmtIC_%{#invList.count}" name="outAmtIC"
                                                                                                                        value="%{#listItem.outAmtIC}" style=" display:none;" />     
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>       

                                                                                                      <td style="text-align: center;">
                                                                                                            <div class="form-group">                                                                              
                                                                                                                  <boe:checkbox name="chkInvlist" id="chkId_%{#invList.count}"
                                                                                                                              fieldValue="%{#listItem.utilityRefNo}"    onclick="getInvoiceList(this)">
                                                                                                                  </boe:checkbox>                                                               
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td> 

                                                                                                </tr>                                                                                           
                                                                                          </boe:iterator>   
                                                                                          </c:if>                                                                                   
                                                                                    </tbody>
                                                                              </table>                                                                            
                                                                              <br></br>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                                <br />                                          
                                          </div><br/>
                                    
                         <div id="dialog" title="Error Message"></div>
                                    <!-- Invoice Block is Ended -->
                                    
                                    <div align="center">                                  
                                          
                                          <input type="button" id="viewInv" value="View" class="button"
                                                                  onclick="viewInvoiceData()" style="margin-left:10px;" />
                                          
                                          <input type="button" id="exInv" value="Ex-Rate" class="button"
                                                                  onclick="exRateInvoiceData()" style="margin-left:10px;" />              
                                          
                                    </div><br/><br/>

                              </div>
                        </div>
                  </div>
                        
                  <boe:hidden name="boeVO.initEndorseAmt" id ="initEndorseAmt" />   
                  <boe:hidden name="boeVO.endorseAmt_temp" id ="endorseAmtId1" />
                  <boe:hidden name="boeVO.outAmt_temp" id ="outStPayAmtId_temp" />
                  <boe:hidden name="boeVO.actualEndorseAmt_temp" id ="actualAmtId1" />
                  <boe:hidden name="boeVO.fullyAlloc_temp" id ="fullyAllocId1" />               
                  <boe:hidden name="boeVO.exRate" id ="exchangeRate" />
                  <boe:hidden name="boeVO.buttonType" id ="buttonType" />
                  <boe:hidden name="boeVO.pageType" id ="pageType" />
                  <boe:hidden name="boeVO.invValue" id ="invValue" />
                  <boe:hidden name="boeVO.manualPartialData" id ="mpdValue" />
                  <boe:hidden name="boeVO.overridStatus" id="overridStatus" />
                  <boe:hidden name="boeVO.hideFromWarning" id="hideFromWarning" />
                  <boe:hidden name="boeVO.okIdFlg" id="okIdFlg" />
                  <boe:hidden name="boeVO.toRejectedPage" id="toRejectedPage" />
                  <boe:hidden name="boeSearchVO.paymentRefNo" id="paymentRefNo" />
                  <boe:hidden name="boeSearchVO.paymentSerialNo" id="paymentSerialNo" />
                  <boe:hidden name="boeSearchVO.boeNo" id="boeNo" />
                  <boe:hidden name="boeSearchVO.paymentCurrency" id="paymentCurrency" />
                  <boe:hidden id="btnModify" name = "boeVO.btnModify"></boe:hidden>
                  <div id="dialogBOE" title="Error Message"></div>
      </boe:form>

</body>
</html>