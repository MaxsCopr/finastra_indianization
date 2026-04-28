<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>BOE  vs Multiple Bills</title>
      <jsp:include page="/view/common/header.jsp" />  
      <script type="text/javascript" src="boe/scripts/manyPayment/manypayment.js"></script>
      
      <script type="text/javascript" src="boe/scripts/manyPayment/manyPayment1.js"></script>
      
      <script>

            function delMBRec(){
                  
                  var checkBoxCount = $('input[type="checkbox"]:checked').length;   
                  
                  var invCount = $('input[type="radio"]:checked').length;
                  
                  if(checkBoxCount == 0){
                  
                  $("#dialogBOE").html("Please Select Atleast One Bill");
                  
                  $("#dialogBOE").dialog("open");
                  
                  $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                        buttons: {
                          Close: function () {
                              $("#dialogBOE").dialog('close');
                          }
                      }
                  });   
                  
                  return false;
                  
               }else if(invCount == 0){
                  
                  $("#dialogBOE").html("Please Select Atleast One Invoice");
                  
                  $("#dialogBOE").dialog("open");
                  
                  $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                        buttons: {
                          Close: function () {
                              $("#dialogBOE").dialog('close');
                          }
                      }
                  });
                  return false;
              }else{
                  
                        $('#formId').attr('action','deleteBOEMBData');
                        $('#formId').submit();
                  }
            }
      
            function getDataFromPayment(){

                  var boeNum = $('#billRefNo').val();
                  var boeDate = $('#billEntryDateId').val();
                  var portCode = $('#portcode').val();

                  if(boeNum == ''){
                        $("#dialogBOE").html('Please Enter BOE Number');
                  
                      $("#dialogBOE").dialog('open')
                    
                      $("#dialogBOE").dialog({
                            autoOpen: false,
                            modal: true,
                            title: "ERROR MESSAGE",
                            buttons: {                            
                                Close: function () {
                                    $(this).dialog('close');
                                }
                           }
                      });
                  }else if(boeDate == ''){
                        $("#dialogBOE").html('Please Enter BOE Date');
                  
                      $("#dialogBOE").dialog('open')
                    
                      $("#dialogBOE").dialog({
                            autoOpen: false,
                            modal: true,
                            title: "ERROR MESSAGE",
                            buttons: {                            
                                Close: function () {
                                    $(this).dialog('close');
                                }
                           }
                      });
                  }else if(portCode == ''){
                        $("#dialogBOE").html('Please Enter Portcode');
                  
                      $("#dialogBOE").dialog('open')
                    
                      $("#dialogBOE").dialog({
                            autoOpen: false,
                            modal: true,
                            title: "ERROR MESSAGE",
                            buttons: {                            
                                Close: function () {
                                    $(this).dialog('close');
                                }
                           }
                      });
                  }else{
                        $('#btnModify').val('M');
                        $('#formId').attr('action','getBoePaymentMBData');
                        $('#formId').submit();
                  }

            }
      
            function getOutStandingBOE(){
                  $("#formId").attr("action", "getOutstandingBOE");
                  $("#formId").submit();
            }
            
            function windowclose() {      
                  top.close();      
            }
            
      </script>
      
</head>

<body class="body_bg">
  <%@ include file="/view/includes/TITLE.jsp"%>
      <boe:form method="post" id="formId" name="form">
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <!-- <li style="text-align: center;"><a href="makerProcess">Close</a></li> -->
                                    
                                    <boe:if test="%{#session.xEvtRefNum.length() > 0}">
                                          <li style="text-align: center;"><a href="#" onclick="windowclose()">Close</a></li>
                                    </boe:if>         
                                    <boe:else>
                                          <li style="text-align: center;"><a href="#" onclick="pressCloseBtn()">Close</a></li>
                                    </boe:else>
                                    <!-- <li style="text-align: center;"><a href="#" onclick="pressCloseBtn()">Close</a></li> -->
                              </ul>
                        </div>
                        <br />                        
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="okId">Ok</a></li>
                                    <li style="text-align: center;"><a href="billNoToManyPaymentReference" id="reset">Reset</a></li>
                                    <li style="text-align: center;"><a href="#" onclick="delMBRec()">Delete </a></li>
                              </ul>
                        </div>
                        <br/>
                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;BOE   vs Multiple Bills</h5>

                        <table align="center">
                              <tr>
                                    <td><boe:actionmessage id="messages"></boe:actionmessage></td>
                              </tr>
                        </table>

                        <div class="row page_content">                        
                              <div class="col-md-12">                         
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;Error Description</h5>
                                    </div>                                    
                                    <div class="form-group">
                                          <div class="table_mt">
                                                <table border="1px" align="left" id="errorAlert">
                                                      <tbody>
                                                      
                                                            <tr>
                                                                  <th style="text-align: left;padding:4px 5px; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Severity" /></label></th>

                                                                  <th style="text-align: left;padding:4px 5px; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Description" /></label></th>

                                                                  <th style="text-align: left;padding:4px 5px; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Steps" /></label></th>

                                                                  <th style="text-align: left;padding:4px 5px; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Details" /></label></th>

                                                                  <th style="text-align: left;padding:4px 5px; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Overridden" /></label></th>
                                                            </tr>
                                                            
                                                            <boe:iterator value="alertMsgArray" id="alertMsgArray">
                                                                  <tr>
                                                                  
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorId" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorDesc" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorCode" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>

                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group">
                                                                                    <boe:property value="errorDetails" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>

                                                                        <td style="padding:4px 5px;">
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
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.BillOfEntryNo" /><a href="#" onclick="getOutStandingBOE()">
                                                            <img src="images/magnify.png" width="13%" height="13%" /></a></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billRefNo" name="boeVO.boeNo"
                                                                  maxlength="7" onkeypress="return isNumber(event)"
                                                                  cssClass="form-control text_box">
                                                            </boe:textfield>        
                                                      </div>
                                                </div>            
                                                
                                                <div class="form-group">
                                                      <label class="col-md-3 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group-md">                                                           
                                                            <input type="button" value="Endorsed Info" class="button"
                                                                  onclick="fetchBOEDetails()" />
                                                      </div>
                                                      <!-- <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label> -->
                                                      <div class="col-md-1 input-group input-group-md">                                                           
                                                            <input type="button" value="Modify" class="button"
                                                                  onclick="getDataFromPayment()" />
                                                      </div>
                                                </div>                              
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.portcode" />&nbsp;<a href="#" onclick="getDischargePort()">
                                                            <img src="images/magnify.png" width="13%" height="13%" /></a></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="portcode" name="boeVO.portCode"
                                                                  cssClass="form-control text_box" />                                                       
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text name="label.boe.CIFNO" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="cifId" name="boeVO.cifNo"
                                                                  cssClass="form-control text_box" readonly="true" />                                                   
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text name="AD Code" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="adCode" name="boeVO.adCode"
                                                                  cssClass="form-control text_box" readonly="true" />                                                         
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text name="IE Address" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ieadd" name="boeVO.ieadd"
                                                                  cssClass="form-control text_box" readonly="true" />                                                   
                                                      </div>
                                                </div>                                          
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.BillofEntryCurrency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billEACurr" name="boeVO.billCurrency"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div><br/> 
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                        name="BOE Endorsed Amount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="edAmt" name="boeVO.endorseAmt"
                                                             cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div><br/> 
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.igmNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="igmNo" name="boeVO.igmNo"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>                                                
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.hblNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="hblNo" name="boeVO.hblNo"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.mblNo" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="mblNo" name="boeVO.mblNo"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Import Agency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="imAgency" name="boeVO.imAgency"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>      <br/>                                           
                                                                              
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="GP" /></label>
                                                      <div class="col-md-3 input-group input-group-md">                 
                                                            <boe:textfield id="govprv" name="boeVO.govprv"
                                                                  cssClass="form-control text_box" readonly="true" />                                                                                                         
                                                      </div>                                                      
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Record Indicator" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="recInd" name="boeVO.recInd"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div><br/>
                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Third Party Payment" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:checkbox name="boeVO.thrdParty" id="thrdParty"
                                                                  fieldValue="true" />
                                                      </div>
                                                </div>
                                                <br />
                                          
                                          </div>
                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.BillofEntryDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billEntryDateId" name="boeVO.boeDate"
                                                                  cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" readonly="false" placeholder="dd/mm/yyyy" />
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
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.CustomerName" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="customerId" name="boeVO.custName"
                                                                  cssClass="form-control text_box"
                                                                  style="text-align: left;" readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.IECode" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ieCodeId" name="boeVO.ieCode"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Changed IE Code" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="ieCodeIdChange"
                                                                  name="boeVO.ieCodeChange" onkeypress="return alphanumeric(event)"
                                                                  cssClass="form-control text_box" readonly="false" maxlength="10"/>
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text name="IE PAN No" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="iepan" name="boeVO.iepan"
                                                                  cssClass="form-control text_box" readonly="true" />                                                   
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.BillofEntryAmount" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="billEAId" name="boeVO.billAmt"
                                                                  onkeypress="return isNumber(event)"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div><br/> 
                                                
                                                <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;"><boe:text
                                                                        name="label.boe.ActualAmountAvailable" /></label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="actualAmountId"
                                                                        name="boeVO.actualEndorseAmt" cssClass="form-control text_box" readonly="true" />
                                                            </div>
                                                </div><br/> 
                                                                                    
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.igmDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="igmDate" name="boeVO.igmDate"
                                                                  cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.hblDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="hblDate" name="boeVO.hblDate"
                                                                  cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="label.boe.mblDate" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="mblDate" name="boeVO.mblDate"
                                                                  cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Port of Shipment" />&nbsp;<a href="#" onclick="getShipmentPort()">
                                                            <img src="images/magnify.png" width="13%" height="13%" /></a></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="pos" name="boeVO.pos"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />
                                                
<!-- Added on 19/01/2018 for BOE Extension Indicator -->

                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;"><boe:text name="BES Record Indicator" /></label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:select id="boeBesMBInd" list="boeBesMBIndList"
                                                                        listKey="key" listValue="value" headerValue="<---->"
                                                                        headerKey="" name="boeVO.boeBesMBInd"
                                                                        style="width: 135px; height: 25px" cssClass="chosen">
                                                                  </boe:select>
                                                            </div>
                                                      </div><br />
<!-- End -->
                                                <%-- <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><boe:text
                                                                  name="Record Indicator" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="recInd" name="boeVO.recInd"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div><br/>  --%>
                                                
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

                                    </div><br />
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
                                                      <div id="selectrow" style="display: none; color: #FF0000; font-weight: bold; font-size: 16px;">
                                                                Please select a row</div> 
                                                      <div class="row page_content">
                                                            <div class="table">
                                                                  <table border="1px" align="left" id="invoiceListTable">
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
                                                                                          <td colspan="16">No records found</td>
                                                                                    </tr>
                                                                              </c:if>                                                                                         
                                                                              <c:if test="${not empty invoiceList}">                                                                                                                                                                                  
                                                                                    <boe:iterator value="invoiceList"  status="invList" id="listItem" >
                                                                                          <tr class="invList" onclick="selectData(this)">
                                                                                                                                                                                          
                                                                                                      <td style="padding: 4px 5px;">      
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="invoiceSerialNumber"></boe:property>
                                                                                                                  <boe:textfield id="invoiceSerialNumber_%{#invList.count}" name="invoiceSerialNumber"
                                                                                                                        value="%{#listItem.invoiceSerialNumber}" style=" display:none;" />
                                                                                                            </div>
                                                                                                      </td>

                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <boe:property value="invoiceNumber"></boe:property>
                                                                                                                  <boe:textfield id="invoiceNumber_%{#invList.count}" name="invoiceNumber"
                                                                                                                        value="%{#listItem.invoiceNumber}" style=" display:none;" />
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
                                                                                                                  <%-- <boe:property value="realizedAmount" style=" display:none;"></boe:property> --%>
                                                                                                                  <boe:textfield id="realAmt_%{#invList.count}" name="realizedAmount"
                                                                                                                        cssClass="form-control text_box" value="%{#listItem.realizedAmount}"
                                                                                                                        style="width: 110px;" readonly = "true" />                                                                                                                  
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      
                                                                                                      <td style="padding: 4px 5px;">
                                                                                                            <div class="form-group" align="left">
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
                                                                                                                  <input type="radio" name="radioId" id="chkId_<boe:property value="%{#invList.count}"/>"
                                                                                                                        value="<boe:property value="utilityRefNo" />" onclick="getInvoiceList(this)"  />                                                          
                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>                                                                                                                                                                                                 
                                                                                          </tr>                                                                                           
                                                                                    </boe:iterator>   
                                                                              </c:if>                                                                                   
                                                                        </tbody>                                                                            
                                                                  </table>

                                                            </div>
                                                      </div>
                                                      
                                                      <div align="right">                 
                                                            
                                                            <input type="button" id="viewInv" value="View" class="button"
                                                                                    onclick="viewInvoiceData()" style="margin-left:10px;" />
                                                            
                                                            <input type="button" id="exInv" value="Ex-Rate" class="button"
                                                                                    onclick="exRateInvoiceData()" style="margin-left:10px;" />              
                                                            
                                                      </div><br/><br/>

                                                </div>
                                          </div>
                                    </div>
                              </div>
                              
                              <div class="col-md-12">             
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 11px;">
                                                &nbsp;
                                                <boe:text name="label.boe.PaymentDetails" />
                                          </h5>
                                    </div>
                                    <div class="col-md-12">
                                          <div class="form-group">
                                                <div class="table_BillMt">
                                                      <table border="1px" align="left" id="billDetailsList">
                                                            <thead>
                                                                  <tr>
                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label><boe:text name="label.boe.BillOfEntryNo" /></label></th>

                                                                        <th style="text-align: left; width: 250px; padding: 4px 5px;"><label><boe:text name="label.boe.BillofEntryDate" /> </label></th>
                                                                        
                                                                        <th style="text-align: left; width: 250px; padding: 4px 5px;"><label><boe:text name="Port code" /> </label></th>

                                                                        <th style="text-align: left; width: 250px; padding: 4px 5px;"><label><boe:text name="label.boe.PaymentReferenceNo" /></label></th>
                                                                        
                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label><boe:text name="Event Reference No" /></label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label><boe:text name="label.boe.PaymentCurrency" /></label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label><boe:text name="label.boe.PaymentAmount" /></label></th>
                                                            
                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label><boe:text name="Payment Endorsed Amount" /></label></th>                                                 
                                                                  </tr>
                                                              </thead>
                                                              <tbody>
                                                                  <c:if test="${empty boePaymentList}">
                                                                        <tr>
                                                                              <td colspan="8">No records found</td>
                                                                        </tr>
                                                                  </c:if>                                                                                         
                                                                  <c:if test="${not empty boePaymentList}">                                                                                                                                                                               
                                                                        <boe:iterator value="boePaymentList" status ="boePayList" id="boePayListItem">
                                                                              <tr class="boePaymentList">
                                                                                    <td style="padding: 4px 5px;">      
                                                                                          <div class="form-group" align="left">
                                                                                                <boe:property value="boeNo"></boe:property>
                                                                                          </div>
                                                                                    </td>

                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <boe:property value="boeDate"></boe:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <boe:property value="portCode"></boe:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <boe:property value="paymentRefNo"></boe:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <boe:property value="partPaymentSlNo"></boe:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <boe:property value="billCurrency"></boe:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <boe:property value="billAmt"></boe:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <boe:property value="actualEndorseAmt"></boe:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                         </boe:iterator>
                                                                        </c:if>                                                                                                     
                                                              </tbody>
                                                      </table>
                                                </div>
                                          </div>
                                     </div>                       
                              </div>                              
                        
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span> </span>
                                          <h5 style="font-weight: bold; font-size: 13px;">
                                                &nbsp;
                                                <boe:text name="Input Details" />
                                          </h5>
                                    </div>


                                    <div class="row page_content">
                                          
                                          <div class="col-md-7">

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Payment Date From </label>

                                                      <div class="col-md-3 input-group input-group-md" style="float:left;">
                                                            <boe:textfield id="paymentDateFrom" name="searchVO.paymentDateFrom" readonly="true"
                                                                   cssClass="datepicker form-control text_box" />
                                                      </div>

                                                      <label class="col-md-1 Control-label"
                                                            style="font-weight: normal;">To</label>

                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="paymentDateTo" name="searchVO.paymentDateTo" readonly="true"
                                                                   cssClass="datepicker form-control text_box" />
                                                      </div>
                                                      

                                                </div>                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Amount From</label>

                                                      <div class="col-md-3 input-group input-group-md" style="float:left;">
                                                            <boe:textfield id="amountFrom" name="searchVO.amountFrom" onkeypress="return isNumber(event)" onblur="compareValue('amountFrom','amountTo')"
                                                                  cssClass="form-control text_box" />
                                                      </div>

                                                      <label class="col-md-1 Control-label"
                                                            style="font-weight: normal;">To</label>

                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="amountTo" name="searchVO.amountTo" onkeypress="return isNumber(event)" onblur="compareValue('amountFrom','amountTo')"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                              
                                                </div>            
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Payment Reference No</label>

                                                      <div class="col-md-3 input-group input-group-md" style="float:left;">
                                                            <boe:textfield id="paymentRefNo" name="searchVO.paymentRefNo"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                      
                              
                                                </div>                                                      

                                          </div>

                                          <div class="col-md-5">

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Currency</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="currency" name="searchVO.paymentCurrency"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>      <br/>                                           
                                          
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <input type="button" value="Refresh" class="button"
                                                                  id="paymentFetch" style="margin-top: 5px;" />
                                                      </div>
                                                </div>
                                                
                                          </div>
                                          

                                    </div>

                              </div>


                              <!-- invoice details -->

                              <div class="col-md-12">
                                    <br />
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">
                                                &nbsp;
                                                <boe:text name="label.boe.PaymentReferencesDetails" />
                                          </h5>
                                    </div>
                                    <div class="col-md-6">
                                          <div class="row page_content">
                                                <div class="table">
                                                      <table border="1px" align="left" id="paymentListTable" >
                                                            <thead>
                                                                  <tr>
                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.PaymentReferenceNo" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.event.refNo" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.PaymentDate" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.PaymentCurrency" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.PaymentAmount" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.PayEndorsedAmount" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.OutstandingAmt" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.AllocatedAmt" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.exRate" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.boeAllocAmt" /></label></th>

                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="label.boe.FullyAllocated" /></label></th>
                                                                        
                                                                        <th style="text-align: left;  padding: 4px 5px;"><label><boe:text
                                                                                          name="Select" /></label></th>

                                                                  </tr>
                                                            </thead>
                                                                  
                                                            <tbody>
                                                                  <c:if test="${empty paymentList}">
                                                                        <tr>
                                                                              <td colspan="12">No records found</td>
                                                                        </tr>
                                                                  </c:if>                                                                                         
                                                                  <c:if test="${not empty paymentList}">                                                                                                                                  
                                                                  <boe:iterator value="paymentList"  status="payList" id="listPayItem" >  
                                                                                                                              
                                                                                    <tr class="paymentList" >     
                                                                                                                                                                  
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="paymentRefNo"></boe:property>
                                                                                                      
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="partPaymentSlNo"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>                                                             
                                                                                          
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="paymentDate"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="paymentCurr"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="paymentAmount"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                
                                                                                                      <boe:textfield id="endorseAmt_%{#payList.count}" name="endorseAmt"
                                                                                                                  cssClass="form-control text_box" value="%{#listPayItem.endorseAmt}" style="width: 118px;"  readonly = "true" />   
                                                                                                      
                                                                                                      <boe:textfield id="endorseAmtTemp_%{#payList.count}" name="endorseAmt_temp"
                                                                                                                  value="%{#listPayItem.endorseAmt_temp}" style="width: 118px; display:none;" />                  
                                                                                                                        
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>                                                                                     
                                                                                          
                                                                                          
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                
                                                                                                      <boe:textfield id="outstandingAmt_%{#payList.count}" name="outstandingAmt"
                                                                                                            cssClass="form-control text_box" value="%{#listPayItem.outstandingAmt}" style="width: 118px;" readonly = "true" />      
                                                                                                                  
                                                                                                      <boe:textfield id="outstandingAmtTemp_%{#payList.count}" name="outstandingAmt_temp"
                                                                                                                  value="%{#listPayItem.outstandingAmt_temp}" style="width: 118px; display:none;" />  
                                                                                                                  
                                                                                                                  <boe:textfield id="alloc_amtx" name="alloc_amtx"
                                                                                                                   style="width: 118px; display:none;" />         
                                                                                                                                                                                                            
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                <boe:textfield id="allocAmt_%{#payList.count}" name="allocAmt"
                                                                                                            cssClass="form-control text_box" value="%{#listPayItem.allocAmt}" style="width: 118px;" onchange="amountCalculation(this)" /> 
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                
                                                                                                      <boe:textfield id="exRate_%{#payList.count}" name="exRate"
                                                                                                            cssClass="form-control text_box" value="%{#listPayItem.exRate}" style="width: 118px;"  /> 
                                                                                                                  
                                                                                                      <boe:textfield id="exRateTemp_%{#payList.count}" name="exRateTemp"
                                                                                                                  value="%{#listPayItem.exRateTemp}" style="width: 118px; display:none;" />     
                                                                                                                        
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:textfield id="boeAllocAmt_%{#payList.count}" name="boeAllocAmt"
                                                                                                            cssClass="form-control text_box" value="%{#listPayItem.boeAllocAmt}"  style="width: 118px;" readonly = "true" />  
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          
                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:textfield id="fullyAlloc_%{#payList.count}" name="fullyAlloc"
                                                                                                            cssClass="form-control text_box" value="%{#listPayItem.fullyAlloc}" style="width: 118px;" readonly = "true" />    
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td> 
                                                                                          
                                                                                          <td style="text-align: center;">
                                                                                                <div class="form-group">                                                                              
                                                                                                      <boe:checkbox name="chkPayList" id="chkPayId_%{#payList.count}"
                                                                                                            fieldValue="%{#listPayItem.utilityTransNo}" onclick="getPaymentList(this)">
                                                                                                      </boe:checkbox>   
                                                                                                      
                                                                                                      <%-- <boe:checkbox name="chkPayList" id="chkPayList_%{#payList.count}"
                                                                                                            fieldValue="%{#listPayItem.utilityTransNo}" >
                                                                                                      </boe:checkbox>    --%>                                                       
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                                <boe:textfield id="isEditField_%{#payList.count}" name="isEditField"
                                                                                                                  value="%{#listPayItem.isEditField}" style="width: 118px; display:none;" />          
                                                                                          </td>                                                                                                                                                                         
                                                                                    </tr>                                                                               
                                                                  </boe:iterator>
                                                                  </c:if>
                                                            </tbody>
                                                      </table>

                                                </div>
                                          </div>
                                    </div>

                              </div>
                        </div>
                  </div>
            </div>            
                        
            <boe:hidden name="boeVO.actualEndorseAmt_temp" id="actualAmtId1" />
            <boe:hidden name="boeVO.buttonType" id ="buttonType" />
            <boe:hidden name="boeVO.invValue" id ="invValue" />
            <boe:hidden name="boeVO.manualPartialData" id ="mpdValue" />
            <boe:hidden name="boeVO.okIdFlg" id="okIdFlg" />
            <boe:hidden name="boeVO.toRejectedPage" id="toRejectedPage" />
            <boe:hidden name="boeSearchVO.paymentRefNo" id="paymentRefNo" />
            <boe:hidden name="boeSearchVO.paymentSerialNo" id="paymentSerialNo" />
            <boe:hidden name="boeSearchVO.boeNo" id="boeNo" />
            <boe:hidden name="boeSearchVO.paymentCurrency" id="paymentCurrency" />
            <boe:hidden id="btnModify" name = "boeVO.btnModify"></boe:hidden>
            <boe:hidden name="boeVO.invoicVal" id ="invoicVal" />
            <div id="dialogBOE" title="Error Message"></div>
      </boe:form>

</body>
</html>