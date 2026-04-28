<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>Invoice Section</title>
      <jsp:include page="/view/common/header.jsp" />  
      <script type="text/javascript" src="boe/scripts/manyBill/invoiceData.js"></script>  
</head>

<body class="body_bg">
      <%@ include file="/view/includes/TITLE.jsp"%>

      <boe:form method="post" id="formId" name="form">
            
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" onclick="closeButton()">Close</a></li>
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav" id="okButton">
                              <ul class="nav nav-pills nav-stacked">                            
                                          <li style="text-align: center;"><a href="#" onclick="insertInvoiceDatatoTable()">OK</a></li>                            
                              </ul>
                        </div>
                        <br />
                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Invoice Details</h5>
                        
                        <div id="userIdMessage" style="color: orange"></div>

                        <table align="center">
                              <tr>
                                    <td><boe:actionmessage id="messages"></boe:actionmessage></td>
                              </tr>
                        </table>
                        
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
                                                                  <th style="text-align: left; width: 200px;">
                                                                        <label><boe:text name="label.boe.Severity"/></label></th>

                                                                  <th style="text-align: left; width: 200px;">
                                                                        <label><boe:text name="label.boe.Description"/></label></th>

                                                                  <th style="text-align: left; width: 200px;">
                                                                        <label><boe:text name="label.boe.Steps"/></label></th>

                                                                  <th style="text-align: left; width: 300px;">
                                                                        <label><boe:text name="label.boe.Details"/></label></th>

                                                                  <th style="text-align: left; width: 200px;">
                                                                        <label><boe:text name="label.boe.Overridden"/></label></th>
                                                            </tr>
                                                            
                                                            <boe:iterator value="alertMsgArray" id="alertMsgArray">                                                           
                                                                  <tr>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorId" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorDesc" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorCode" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group">
                                                                                    <boe:property value="errorDetails" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorMsg" />
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
                        
                        
                              <div class="col-md-12">
                              
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">
                                                &nbsp;
                                                <boe:text name="label.boe.BillofEntry" />
                                          </h5>
                                    </div>

                                    <div class="row page_content">

                                          <div class="col-md-6">
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Bill Of Entry No *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billRefNo" name="boeVO.boeNo"
                                                                  cssClass="form-control text_box" readonly="true">
                                                            </boe:textfield>
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Port Code *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="portcode" name="boeVO.portCode"
                                                                  cssClass="form-control text_box" readonly="true">
                                                            </boe:textfield>
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Invoice Serial Number *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="invSno" name="transactionVO.invSno"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Invoice Amount *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="invAmt" name="transactionVO.invAmt"
                                                            maxlength="20"    onkeypress="return isNumber(event)" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Supplier Name *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="supname" name="transactionVO.supplierName"
                                                                   maxlength="200" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Supplier Country *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="supcon" name="transactionVO.supplierCountry"
                                                                   maxlength="50" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Freight Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="freamt" name="transactionVO.freightAmount"
                                                                  onkeypress="return isNumber(event)"  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Insurance Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="inamt" name="transactionVO.insuranceAmount"
                                                                  onkeypress="return isNumber(event)"  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                        
                                                 <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Agency Commission" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="agecom" name="transactionVO.agencyCommission"
                                                                  onkeypress="return isNumber(event)"  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Discount Charges" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="dischar" name="transactionVO.discountCharges"
                                                                  onkeypress="return isNumber(event)"  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Miscellaneous charges" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="mischar" name="transactionVO.miscellaneousCharges"
                                                                  onkeypress="return isNumber(event)"  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Seller Name" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="selname" name="transactionVO.sellerName"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Seller Country" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="selcty" name="transactionVO.sellerCountry"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Third Party Address" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ptyaddr" name="transactionVO.thirdPartyAddress"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>      
                                          
                                                
                                          </div>
                                          
                                          <div class="col-md-6">
                                                
                                                <div class="form-group">                                                
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text name="BOE Date" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billEntryDateId" name="boeVO.boeDate"
                                                                  cssClass="datepicker form-control text_box"
                                                                  style="text-align:left;" readonly="true">
                                                            </boe:textfield>
                                                      </div>                                                
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Invoice Number *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="invNo" name="transactionVO.invoiceNumber"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Terms of Service *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="tos" name="transactionVO.termsofInvoice"
                                                                  maxlength="3" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Invoice Currency *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="tos" name="transactionVO.invoiceCurr"
                                                                  maxlength="3" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Supplier Address *" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="supadd" name="transactionVO.supplierAddress"
                                                                   maxlength="500" cssClass="form-control text_box" />
                                                      </div>
                                                </div><br/><br/>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Freight Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="frecuc" name="transactionVO.freightCurrencyCode"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Insurance Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="insucod" name="transactionVO.insuranceCurrencyCode"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Agency Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="agencuc" name="transactionVO.agencyCurrency"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Discount Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="discucy" name="transactionVO.discountCurrency"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Miscellaneous Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="miscucy" name="transactionVO.miscellaneousCurrency"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Seller Address" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="seladdr" name="transactionVO.sellerAddress"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Third Party Name" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="thrdname" name="transactionVO.thirdPartyName"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Third Party Country" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ptycty" name="transactionVO.thirdPartyCountry"
                                                                    cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                                                                                                                                                          
                                          </div>                                    
                                    </div>                                          
                                    <br />
                                    <br />
                              </div>
                        <br />
                        <!-- invoice details -->

                        </div>
                  </div>                  

                  <boe:hidden name="boeVO.cifNo"                              id="cifNo" />
                  <boe:hidden name="boeVO.benefName"                    id="benefName" />                   
                  <boe:hidden name="boeVO.billCurrency"                 id="billCurrency" />                
                  <boe:hidden name="boeVO.paymentCurr"                  id="paymentCurr" />
                  <boe:hidden name="boeVO.paymentRefNo"                 id="paymentRefNo" />
                  <boe:hidden name="boeVO.endorseAmt"                   id="endorseAmt" />
                  <boe:hidden name="boeVO.outAmt"                       id="outAmt" />                      
                  <boe:hidden name="boeVO.boeExRate"                    id="boeExRate" />             
                  <boe:hidden name="boeVO.igmNo"                        id="igmNo" />                       
                  <boe:hidden name="boeVO.hblNo"                        id="hblNo" />     
                  <boe:hidden name="boeVO.mblNo"                              id="mblNo" />
                  <boe:hidden name="boeVO.imAgency"                     id="imAgency" />
                  <boe:hidden name="boeVO.adCode"                       id="adCode" />
                  <boe:hidden name="boeVO.govprv"                       id="govprv" />
                  <boe:hidden name="boeVO.pos"                          id="pos" />                   
                  <boe:hidden name="boeVO.thrdParty"                    id="thrdParty" /> 
                  <boe:hidden name="boeVO.transType"                    id="transType" />
                  <boe:hidden name="boeVO.custName"                     id="custName" />
                  <boe:hidden name="boeVO.ieCode"                       id="ieCode" />
                  <boe:hidden name="boeVO.ieCodeChange"                 id="ieCodeChange" />                      
                  <boe:hidden name="boeVO.ieadd"                              id="ieadd" />                 
                  <boe:hidden name="boeVO.iepan"                        id="iepan" />                       
                  <boe:hidden name="boeVO.benefCountry"           id="benefCountry" />    
                  <boe:hidden name="boeVO.billAmt"                      id="billAmt" />
                  <boe:hidden name="boeVO.actualEndorseAmt"             id="actualEndorseAmt" />
                  <boe:hidden name="boeVO.partPaymentSlNo"        id="partPaymentSlNo" />
                  <boe:hidden name="boeVO.paymentAmount"                id="paymentAmount" />
                  <boe:hidden name="boeVO.payDate"                      id="payDate" />                     
                  <boe:hidden name="boeVO.eqPaymentAmount"        id="eqPaymentAmount" />             
                  <boe:hidden name="boeVO.allocAmt"                     id="allocAmt" />  
                  <boe:hidden name="boeVO.fullyAlloc"                   id="fullyAlloc" />
                  <boe:hidden name="boeVO.igmDate"                      id="igmDate" />
                  <boe:hidden name="boeVO.hblDate"                      id="hblDate" />
                  <boe:hidden name="boeVO.mblDate"                      id="mblDate" />
                  <boe:hidden name="boeVO.remarks"                      id="remarks" />                     
                  <boe:hidden name="boeVO.recInd"                       id="recInd" />
                  <boe:hidden name="boeVO.initEndorseAmt"         id="initEndorseAmt" />  
                  <boe:hidden name="boeVO.endorseAmt_temp"        id="endorseAmtId1" />
                  <boe:hidden name="boeVO.outAmt_temp"                  id="outStPayAmtId_temp" />
                  <boe:hidden name="boeVO.actualEndorseAmt_temp"  id="actualAmtId1" />
                  <boe:hidden name="boeVO.fullyAlloc_temp"        id="fullyAllocId1" />               
                  <boe:hidden name="boeVO.exchangeRate"                 id="exchangeRate" />                
                  <boe:hidden name="boeVO.buttonType"                   id="buttonType" />
                  <boe:hidden name="boeVO.manualPartialData"            id ="mpdValue" />
            </div>
      </boe:form>
</body>
</html>