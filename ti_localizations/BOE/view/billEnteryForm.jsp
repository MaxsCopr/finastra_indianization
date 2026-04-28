<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>Bill vs Multiple BOEs</title>
      <jsp:include page="/view/common/header.jsp" />
      <script type="text/javascript" src="boe/scripts/billForm/billform.js"></script>     
      <script>
            function windowclose() {      
                  top.close();      
            }
            
            function delRec(){
                  $('#formId').attr('action','deleteBOEData');
                  $('#formId').submit();
            }
            
            
            function getDataFromPayment(){
                  
                  var payRefNo       = $('#payRefId').val();
                var eventRefNo = $('#eventRefNo').val();
              
                if(payRefNo == ''){
                  
                  $("#dialogBOE").html('Please Enter Payment Reference Number');
                  
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
                  }else if(eventRefNo == ''){
                        
                  $("#dialogBOE").html('Please Select Event Reference Number');
                  
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
                      
// Added on 21/04/2018 Start

                      var tbl_len = document.getElementById('boeChkValue').rows.length - 1;     
                        var chkBoxPosition;
                        var boeStatus = 0;
                  if (tbl_len > 0) {
                        
                        var vCount = 0;
                         for ( var j = 1; j <= tbl_len; j++) {
                              var isEditVal = $("#bill_chkList_"+j).prop('checked');

                              if(isEditVal == true){

                                    boeStatus = $("#invPendingCount_"+j).val();
                                    chkBoxPosition = j;
                                    vCount += 1;
                              }
                        }
                  }

                  if(vCount == 0){
                        
                        $("#dialogBOE").html('Select Modification Bill');
                  
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
                  }else if(vCount > 1){
                  
                              $("#dialogBOE").html('Select only one Bill to Modify');
                  
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
                        
                  }/* else if(boeStatus == 0){
                  
                              $("#dialogBOE").html('Cannot Modify Approved BOE');
                  
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
                        
                  } */else{
// Added on 21/04/2018 Ends                   
                              /* $('#btnModify').val('M');
                              $('#formId').attr('action','getBoePaymentData');
                              $('#formId').submit(); */
                              
                              $('#btnModify').val('M');
                              $('#formId').attr('action','boe_modification');
                              $('#formId').submit();
                  }
                  }
            }
            
            function selectData(event){     

                   $(".boeList").removeClass("highlighted");
                   $(event).addClass("highlighted");  
                  
                   var value1 = $(event).find("td").eq(0).text().trim();
                   var value2 = $(event).find("td").eq(1).text().trim();
                   var value3 = $(event).find("td").eq(2).text().trim();
                   var value4 = $(event).find("td").eq(3).text().trim();
                   var value5 = $(event).find("td").eq(4).text().trim();

                   var boeValue = value1 + ':' + value2 + ':' +value3 + ':' + value4+ ':' + value5;   
                  
                   $("#boeValue").val(boeValue);            
                  
            }
            
            function getBOEDetails(event){

                        var boeDetails = $(event).attr('id');

                        var vBoeDetails = boeDetails.split('_');
                        var vCount = vBoeDetails[2];
                        
                        $(".boeList").removeClass("highlighted");
                         $(event).addClass("highlighted");
                        
                        var paymentRefNo = document.getElementById("paymentRefNo_"+vCount).value;
                        var eventRefNo = document.getElementById("partPaymentSlNo_"+vCount).value;
                        var boeNo = document.getElementById("boeNo_"+vCount).value;
                        var boeDate = document.getElementById("boeDate_"+vCount).value;
                        var portCode = document.getElementById("portCode_"+vCount).value;

                        var condVal = $(event).prop('checked');

                        if(condVal == true){

                              var tempVal = paymentRefNo + ':' + eventRefNo + ':' + boeNo + ':' +boeDate+ ':'+portCode; 

                              document.getElementById("bill_chkList_"+cVal).value = tempVal;
                              
                              /* $('#formId').attr('action','deleteBOEData');
                              $('#formId').submit(); */
                        }
                  }
            
                  function getOutStandingORM(){
                        $("#formId").attr("action", "outstandingORM");
                        $("#formId").submit();
                  }
      </script>
</head>

<body class="body_bg">

<%-- <%
String master=request.getParameter("xMstRefNum");
out.println(master);%> --%>
      <jsp:include page="includes/TITLE.jsp" />
      
      <boe:form method="post" id="formId" name="form">
      
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <boe:if test="%{#session.xEvtRefNum.length() > 0}">
                                          <li style="text-align: center;"><a href="#" onclick="windowclose()">Close</a></li>
                                    </boe:if>         
                                    <boe:else>
                                          <li style="text-align: center;"><a href="makerProcess"><boe:text name="label.boe.close" /></a></li>
                                    </boe:else>
                                    
                                    <%-- <li style="text-align: center;"><a href="makerProcess"><boe:text name="label.boe.close" /></a></li> --%>
                                          
                                    <li style="text-align: center;"><a href="BillEnteryForm"><boe:text name="Reset" /></a></li>
                                    
                                    
                                    <c:if test="${not empty tiList}">
                                    <boe:set var='loops' value="{%false}"></boe:set>
                                    <boe:iterator value="tiList"  status="boeList" id="listItem">                                   
                                                                              
                                    <boe:if test="!#loops">                                     
                                    <boe:if test='#listItem.status.equals("R")'>
                                    <li style="text-align: center;"><a href="#" onclick="delRec()">Delete </a></li>
                                    <boe:set var="loops" value="{%true}"/>
                                    </boe:if>
                                    </boe:if>
                                    </boe:iterator>
                                    </c:if>
                              </ul>
                        </div>
                        <div class="side_nav"></div>
                  </div>

                  <div class="col-md-10 content_box">
                  
                  
                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill vs Multiple BOEs</h5>
                        
                        <div id="userIdMessage" style="color: orange"></div>
                        
                        <div class="row cont_colaps">
                        
                              <div class="row page_content"></div>
                              
                              <div class="col-md-12">
                              
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">Payment     Details</h5>
                                    </div>                                    

                                          <div class="row page_content">
                                          
                                                <div class="col-md-6">
                                                

                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;"><boe:text
                                                                        name="label.boe.PaymentReferenceNo" /><a href="#" onclick="getOutStandingORM()">
                                                            <img src="images/magnify.png" width="13%" height="13%" /></a></label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                            
                                                            
                                                            
                                                            
                                                            <boe:textfield id="payRefId" name="boeVO.paymentRefNo"
                                                                        onblur="getPayRefNo()" cssClass="form-control text_box" />
                                                                  
                                                            </div>
                                                      </div><br/>
                                                      
                                                      <p style="text-indent: 180px">
                                                      <boe:if test="%{#session.xEvtRefNum.length() > 0}">                                                         
                                                            <input type="button" value="<boe:text name="label.boe.Search"/>" class="button"
                                                                        id="searchButton" style="background: url('../img/but.gif'); height: 25px; width: 80px;"
                                                                        onclick="fetchDataFromView()" />
                                                            </boe:if>                                                                                                                     
                                                            <boe:elseif test='searchButton.trim().equals("Y")'>
                                                            
                                                                  <input type="button" value="<boe:text name="label.boe.Search"/>" class="button"
                                                                        id="searchButton" style="background: url('../img/but.gif'); height: 25px; width: 80px;"
                                                                        onclick="fetchDataFromView()" />
                                                            </boe:elseif>                                                     
                                                      
                                                            <%-- <boe:if test='searchButton.trim().equals("Y")'>
                                                                  <input type="button" value="<boe:text name="label.boe.Search"/>" class="button"
                                                                        id="searchButton" style="background: url('../img/but.gif'); height: 25px; width: 80px;"
                                                                        onclick="fetchDataFromView()" />
                                                            </boe:if> --%>
                                                            <br/> <br/>
                                                      </p>
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="label.boe.PaymentDate" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="paymentDatIdd" name="boeVO.payDate"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/>                                                 
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="label.boe.CustomerName" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="customerIdd" name="boeVO.custName"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/>
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="label.boe.benefName" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="benefName" name="boeVO.benefName"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/> 
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="label.boe.PaymentCurrency" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="payCurrIdd" name="boeVO.paymentCurr"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/> 
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="Outstanding Payment Amount" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="osAmt" name="boeVO.outAmt"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/> 

                                                </div>                                                                  
                                    
                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                  <boe:text name="label.boe.event.refNo" /></label>
                                                             <div class="col-md-3 input-group input-group-md">
                                                            
                                                                  <%-- <boe:select list="eventList" listKey="key" listValue="value"
                                                                        style="width: 137px; height: 26px" headerKey = "" headerValue = "<--->"
                                                                        name="boeVO.partPaymentSlNo" id="eventRefNo">
                                                                  </boe:select> --%>
                                                            
                                                            <boe:if test="%{#session.xEvtRefNum.length() > 0}">                                                         
                                                            <boe:textfield  name="boeVO.partPaymentSlNo" id="eventRefNo" value="%{#session.xEvtRefNum}"
                                                                         readonly="true"/>
                                                            </boe:if>                                                                                                                     
                                                            <boe:else>
                                                            <boe:select list="eventList" listKey="key" listValue="value"
                                                                        style="width: 137px; height: 26px" headerKey = "" headerValue = "<--->"
                                                                        name="boeVO.partPaymentSlNo" id="eventRefNo">
                                                                  </boe:select>
                                                            </boe:else>
                                                            
                                                                  
                                                            </div>
                                                      </div><br/><br/><br/>
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="label.boe.CIFNO" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="cifNoIdd" name="boeVO.cifNo"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/>
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="label.boe.IECode" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="iecodeIdd" name="boeVO.ieCode"
                                                                              readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/>
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="label.boe.benefCountry" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="benefCountry" name="boeVO.benefCountry"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/>
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="label.boe.PaymentAmount" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="payAmountIdd" name="boeVO.paymentAmount"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/>       
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                                        <boe:text name="Fully Allocated" /></label>
                                                                  <div class="col-md-3 input-group input-group-md">
                                                                        <boe:textfield id="fullyAlloc" name="boeVO.fullyAlloc"
                                                                                    readonly="true" cssClass="form-control text_box" />
                                                                  </div>
                                                      </div><br/> 
                                                                                                
                                                </div>
                                    </div>
                              </div>
                        </div>
      
                        <br />
                        <div class="row cont_colaps">
                              <div class="row page_content"></div>
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">Bill Of     Entry Details</h5>
                                    </div>
                                    <div>
                                          <div align="left" class="col-md-6"
                                                style="height: 250px; overflow: auto; width: 900px">
                                                <table border="1px" align="left"
                                                      style="height: 55px; width: 800px;" id="boeChkValue">
                                                      <thead>
                                                            <tr>
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="label.boe.PaymentReferenceNo" /></label></th>
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="label.boe.event.refNo" /></label></th>
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="label.boe.BillOfEntryNo" /> </label></th>
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="label.boe.BillofEntryDate" /></label></th>
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="Portcode" /></label></th>
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="label.boe.BillofEntryAmount" /> </label></th>   
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="BOE Currency" /> </label></th>                                    
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="label.boe.EndorsedByOtherAD" /> </label></th>
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="label.boe.ActualAmountAvailable" /> </label></th>
                                                                  <th><label style=" padding:4px 5px; !important"><boe:text
                                                                                    name="label.boe.EndorsedAmount" /> </label></th>
                                                                  <th><label style="padding: 4px 5px; !important"><boe:text
                                                                                    name="Select" /> </label></th>                                                                                    
                                                            </tr>
                                                      </thead>
                                                      <tbody>
                                                            <c:if test="${empty tiList}">
                                                                  <tr>
                                                                        <td colspan="10">No records found</td>
                                                                  </tr>
                                                            </c:if>
                                                            <c:if test="${not empty tiList}">
                                                                  <boe:iterator value="tiList"  status="boeList" id="listItem">
                                                                        <tr class="boeList" onclick="selectData(this)">
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="paymentRefNo" />
                                                                                          <boe:textfield id="paymentRefNo_%{#boeList.count}" name="paymentRefNo"
                                                                                                                        value="%{#listItem.paymentRefNo}" style=" display:none;" /> 
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="partPaymentSlNo" />
                                                                                          <boe:textfield id="partPaymentSlNo_%{#boeList.count}" name="partPaymentSlNo"
                                                                                                                        value="%{#listItem.partPaymentSlNo}" style=" display:none;" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeNo" />
                                                                                          <boe:textfield id="boeNo_%{#boeList.count}" name="boeNo"
                                                                                                                        value="%{#listItem.boeNo}" style=" display:none;" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: right; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeDate" />
                                                                                          <boe:textfield id="boeDate_%{#boeList.count}" name="boeDate"
                                                                                                                        value="%{#listItem.boeDate}" style=" display:none;" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: right; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="portCode" />
                                                                                          <boe:textfield id="portCode_%{#boeList.count}" name="portCode"
                                                                                                                        value="%{#listItem.portCode}" style=" display:none;" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="billAmt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeCurr" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>                                                                   
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="adEndorseAmt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="actualEndorseAmt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="balEndorseAmt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              
                                                                              
                                                                              <%-- <td style="text-align: center;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:checkbox name="chkList"
                                                                                                                  fieldValue="%{#rejectedBoeList.boeNo+':'+#rejectedBoeList.boeDate+':'+#rejectedBoeList.portCode}">
                                                                                                            </boe:checkbox>
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                              </td>
                                                                                           --%>




                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:checkbox name="bill_chkList"  id="bill_chkList_%{#boeList.count}"
                                                                                                      fieldValue="%{#listItem.delBoeDetails}" onclick="getBOEDetails(this)">
                                                                                                            </boe:checkbox>
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                        </tr>
                                                                        <boe:textfield id="invPendingCount_%{#boeList.count}" name="invPendingCount"
                                                                                                                        value="%{#listItem.invPendingCount}" style=" display:none;" />
                                                                  </boe:iterator>
                                                            </c:if>
                                                      </tbody>
                                                </table>
                                          </div>

                                          <div>
                                          <boe:if test="%{#session.xEvtRefNum.length() > 0}">
                                          <input type="button" value="<boe:text name="label.boe.New"/>"
                                                            class="button" style="background: url('../img/but.gif'); height: 25px; width: 80px;"
                                                            onclick="getDataFromView()" /> &ensp;
                                          </boe:if>                                       
                                                <boe:elseif test='newButton.trim().equals("N")'>
                                                      <input type="button" value="<boe:text name="label.boe.New"/>"
                                                            class="button" style="background: url('../img/but.gif'); height: 25px; width: 80px;"
                                                            onclick="getDataFromView()" /> &ensp;
                                                </boe:elseif>
                                                
                                                <c:if test="${not empty tiList}">
                                                <boe:set var='loops' value="{%false}"></boe:set>
                                                <boe:iterator value="tiList"  status="boeList" id="listItem">     
                                                <boe:if test="!#loops">                                     
                                                <boe:if test='#listItem.status.equals("R")'>                                                          
                                              <boe:set var="loops" value="{%true}"/>
                                                      <input type="button" value="<boe:text name="label.boe.Modify"/>"
                                                            class="button" style="background: url('../img/but.gif'); height: 25px; width: 80px;"
                                                            onclick="getDataFromPayment()" />
                                                      <br />
                                                      <br />
                                                </boe:if>
                                                </boe:if>
                                                </boe:iterator>
                                                </c:if>
                                                
                                                      
                                                
                                          </div>
                                          
                                          <div id="dialogBOE" title="Error Message"></div>
                                          <boe:hidden id="btnModify" name = "boeVO.btnModify" value = ""></boe:hidden>
                                          <boe:hidden name="boeVO.boeValue" id ="boeValue" />
                                    </div>
                              </div>
                        </div>
                  </div>
            </div>
      </boe:form>

</body>
</html>