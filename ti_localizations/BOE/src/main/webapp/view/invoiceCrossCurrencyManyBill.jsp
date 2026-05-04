<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>Cross Currency Section</title>
      <jsp:include page="/view/common/header.jsp" />  
      <script type="text/javascript" src="boe/scripts/manyPayment/invoiceCrossCurrencyManyBill.js"></script>      
</head>

<body class="body_bg">
      <%@ include file="/view/includes/TITLE.jsp"%>

      <boe:form method="post" id="formId" name="form">

            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#"
                                          onclick="closeCrossCurrencyButton()">Close</a></li>
                              </ul>
                        </div>
                        <br />
                        <br />
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">                            
                                          <li style="text-align: center;"><a href="#" onclick="crossCurrencyConversion()">OK</a></li>                             
                              </ul>
                        </div>
                        <br />                  
                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Invoice
                              Details</h5>

                        <table align="center">
                              <tr>
                                    <td><boe:actionmessage id="messages"></boe:actionmessage></td>
                              </tr>
                        </table>
                  
                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;Error
                                                Description</h5>
                                    </div>
                                    <div class="form-group">
                                          <div class="table_mt">
                                                <table border="1px" align="left" id="errorAlert">
                                                      <tbody>
                                                            <tr>
                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Severity" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Description" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Steps" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Details" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Overridden" /></label></th>
                                                            </tr>
                                                            <boe:iterator value="alertMsgArray" id="alertMsgArray">
                                                                  <tr>
                                                                        <td style="background-color: #962A2A;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorId" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="background-color: #962A2A;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorDesc" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="background-color: #962A2A;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorCode" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>

                                                                        <td style="background-color: #962A2A;">
                                                                              <div class="form-group">
                                                                                    <boe:property value="errorDetails" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>

                                                                        <td style="background-color: #962A2A;">
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
                              </div>
                        </div>
                        <br />

                        <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">
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
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.BillOfEntryNo" /></label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="crossBoeNo" name="boeVO.boeNo"
                                                                        cssClass="form-control text_box" readonly="true">
                                                                  </boe:textfield>
                                                            </div>                                                      
                                                </div>
                                                
                                                <div class="form-group">                                                      
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Portcode" /></label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="crossBoeDate" name="boeVO.portCode"
                                                                        cssClass="form-control text_box" readonly="true">
                                                                  </boe:textfield>
                                                            </div>                                                      
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.InvoiceNumber" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossInvNo" name="transactionVO.invNo"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Invoice FOB Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossFobAmt" name="transactionVO.crossFobAmt"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Freight Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossFrAmt" name="transactionVO.crossFrAmt"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                         <boe:text name="Insurance Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossInsAmt"     name="transactionVO.crossInsAmt"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Insurance Exchange Rate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossExRate1" onkeypress="return isNumber(event)"
                                                            name="transactionVO.crossExRate1" onchange="amtCalculation1()"
                                                            cssClass="form-control text_box" />
                                                      </div>
                                                </div> <br/>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Freight Exchange Rate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossExRate2" name="transactionVO.crossExRate2" onchange="amtCalculation2()"
                                                                  onkeypress="return isNumber(event)" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                          </div>
                                          
                                    
                                    
                                          <div class="col-md-6">
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="BOE Date" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossBoeDate" name="boeVO.boeDate"
                                                                        cssClass="form-control text_box" readonly="true">
                                                            </boe:textfield>
                                                      </div>
                                                </div>
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.InvoiceSerialNumber" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossinvSno"     name="transactionVO.invSno"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                        <boe:text name="Invoice Total Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossTotInvAmt" name="transactionVO.totalInvAmt"
                                                                  cssClass="form-control text_box" style="text-align:left;" readonly="true">
                                                            </boe:textfield>
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                         <boe:text name="Invoice FOB Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossFobCurr"
                                                                  name="transactionVO.crossFobCurr" maxlength="3"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Freight Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossFrCurr"     name="transactionVO.crossFrCurr"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Insurance Currency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossInsCurr" name="transactionVO.crossInsCurr"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Equivalent Insurance Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossEqInsAmt" name="transactionVO.equivalentInsAmt"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div> <br/>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="Equivalent Freight Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="crossEqFrAmt" name="transactionVO.equivalentFreAmt"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                          </div>                                    
                                          
                                    </div>                        

                              </div>
                              <br />

                        <boe:hidden name="boeVO.transType"                    id="transType" />       
                        <boe:hidden name="boeVO.cifNo"                              id="cifNo" />                 
                        <boe:hidden name="boeVO.billCurrency"                 id="billCurrency" />                      
                        <boe:hidden name="boeVO.igmNo"                        id="igmNo" />                       
                        <boe:hidden name="boeVO.hblNo"                        id="hblNo" />     
                        <boe:hidden name="boeVO.mblNo"                              id="mblNo" />
                        <boe:hidden name="boeVO.imAgency"                     id="imAgency" />
                        <boe:hidden name="boeVO.adCode"                       id="adCode" />
                        <boe:hidden name="boeVO.govprv"                       id="govprv" />
                        <boe:hidden name="boeVO.pos"                          id="pos" />                   
                        <boe:hidden name="boeVO.thrdParty"                    id="thrdParty" />                   
                        <boe:hidden name="boeVO.custName"                     id="custName" />
                        <boe:hidden name="boeVO.ieCode"                       id="ieCode" />
                        <boe:hidden name="boeVO.ieCodeChange"                 id="ieCodeChange" />                                  
                        <boe:hidden name="boeVO.ieadd"                              id="ieadd" />                 
                        <boe:hidden name="boeVO.iepan"                        id="iepan" />                 
                        <boe:hidden name="boeVO.billAmt"                      id="billAmt" />
                        <boe:hidden name="boeVO.actualEndorseAmt"             id="actualEndorseAmt" />                  
                        <boe:hidden name="boeVO.igmDate"                      id="igmDate" />
                        <boe:hidden name="boeVO.hblDate"                      id="hblDate" />
                        <boe:hidden name="boeVO.mblDate"                      id="mblDate" />
                        <boe:hidden name="boeVO.remarks"                      id="remarks" />                     
                        <boe:hidden name="boeVO.recInd"                       id="recInd" />    
                        <boe:hidden name="boeVO.actualEndorseAmt_temp"  id="actualAmtId1" />                
                        <boe:hidden name="boeVO.buttonType"                   id="buttonType" />
                        <boe:hidden name="boeVO.manualPartialData"            id ="mpdValue" />
                        </div>
                  </div>
            </div><br/><br/>
      </boe:form>

</body>
</html>