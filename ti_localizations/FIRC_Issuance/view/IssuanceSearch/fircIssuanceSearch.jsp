<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="firc" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- <meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="Expires" content="-1" /> -->

<title>FIRC Issuance Search</title>

<jsp:include page="/view/common/header.jsp" />

<script type="text/javascript">

      $(document).ready(function() {
            
            
             $("#Search").click(function(){
                   $("#form1").attr("action","fircIssuanceSearch");
                    $("#form1").submit();
            });         
            
            $("#reset").click(function(){
                   $("#form1").attr("action","fircIssuance");
                   $("#form1").submit();
            });
            
            
            $("#print").click(function(){
                  
                  var fircVal = $("#fircVal").val();
                  
                if (fircVal == "" ) {
                    $('#selectrow').css('display', 'block');
                    return false;
                }else {  
                  
                  $('#selectrow').css('display', 'none');
                  
                  var tempFirc = fircVal.split(":");
                  
                  if(tempFirc[2] == 'A'){
                        
                        if(tempFirc[3] == 'FIRC'){
                              
                              var comId = tempFirc[0];
                              
                               $.ajaxSetup({
                                     async: false
                               });
                               $.post("printStatus"+"?refNo="+comId,function(data) {
                                    
                                     var check = data.resStatus;
                                    
                                     if(check == 'Y'){                                    
                                          
                                           $("#errorMessage").dialog({autoOpen: false,modal: true});
                                          
                                       $("#errorMessage").html("Do you want Duplicate/Re-issue Download ? ");
                                          
                                       $("#errorMessage").dialog("open");
                                          
                                       $("#errorMessage").dialog({autoOpen: false,modal: true,title: "FIRC",
                                                buttons: {
                                                    'Duplicate': function() {
                                                       $(this).dialog('close');
                                                       $("#form1").attr("action","fircDupPrintAction");
                                                                   $("#form1").submit();
                                                     },
                                                     'Re-issue': function() {
                                                       $(this).dialog('close');
                                                       $("#form1").attr("action","fircPrintAction");
                                                             $("#form1").submit();
                                                     }                    
                                              }
                                          });
                                     }else{
                                           $("#form1").attr("action","fircPrintAction");
                                           $("#form1").submit();
                                     }
                               });
                        }else if(tempFirc[3] == 'FIRA'){
                              var comId = tempFirc[0];
                              
                               $.ajaxSetup({
                                     async: false
                               });
                               $.post("printStatus"+"?refNo="+comId,function(data) {
                                    
                                     var check = data.resStatus;
                                    
                                     if(check == 'Y'){                                    
                                          
                                           $("#errorMessage").dialog({autoOpen: false,modal: true});
                                          
                                       $("#errorMessage").html("Do you want to Download FIRA ? ");
                                          
                                       $("#errorMessage").dialog("open");
                                          
                                       $("#errorMessage").dialog({autoOpen: false,modal: true,title: "FIRA",
                                                buttons: {
                                                    'Yes': function() {
                                                       $(this).dialog('close');
                                                       $("#form1").attr("action","firaPrintAction");
                                                                   $("#form1").submit();
                                                     },
                                                     'No': function() {
                                                       $(this).dialog('close');
                                                     }                    
                                              }
                                          });
                                     }else{
                                           $("#form1").attr("action","firaPrintAction");
                                           $("#form1").submit();
                                     }
                               });
                        
                        <%--   $("#errorMessage").dialog({autoOpen: false,modal: true});
                              
                              $("#errorMessage").html("This transaction type is FIRA.So you could not print this document. ");
                              
                              $("#errorMessage").dialog("open");
                              
                              $("#errorMessage").dialog({autoOpen: false,modal: true,title: "Error Message",
                                    buttons: {
                                      Close: function () {
                                          $("#errorMessage").dialog('close');
                                      }
                                  }
                              }); --%>
                              return false;
                        }           
                        
                  }else{
                        
                        $("#errorMessage").dialog({autoOpen: false,modal: true});
                        
                        $("#errorMessage").html("Transaction has not been approved.So you could not print this document");
                        
                        $("#errorMessage").dialog("open");
                        
                        $("#errorMessage").dialog({autoOpen: false,modal: true,title: "Error Message",
                              buttons: {
                                Close: function () {
                                    $("#errorMessage").dialog('close');
                                }
                            }
                        });
                        return false;
                  }
                }
            });   
            
            $("#reprint").click(function(){
                  var fircVal = $("#fircVal").val();
                if (fircVal == "" ) {
                    $('#selectrow').css('display', 'block');
                    return false;
                }else {       
                  
                  $('#selectrow').css('display', 'none');
                  
                  var tempFirc = fircVal.split(":");
                  
                  if(tempFirc[2] == 'A'){ 
                        
                        if(tempFirc[3] == 'FIRA'){
                              $("#form1").attr("action","fircPrintAdviceAction");
                                    $("#form1").submit();
                        }else{
                              $("#errorMessage").dialog({autoOpen: false,modal: true});
                              
                              $("#errorMessage").html("This transaction type is FIRC.So you could not send this document ");
                              
                              $("#errorMessage").dialog("open");
                              
                              $("#errorMessage").dialog({autoOpen: false,modal: true,title: "Error Message",
                                    buttons: {
                                      Close: function () {
                                          $("#errorMessage").dialog('close');
                                      }
                                  }
                              });
                              return false;
                        }     
                        
                  }else{
                              $("#errorMessage").dialog({autoOpen: false,modal: true});
                        
                        $("#errorMessage").html("Transaction is not approved.So you could not print the document");
                        
                        $("#errorMessage").dialog("open");
                        
                        $("#errorMessage").dialog({autoOpen: false,modal: true,title: "Error Message",
                              buttons: {
                                Close: function () {
                                    $("#errorMessage").dialog('close');
                                }
                            }
                        });
                        return false;
                  }
                }
            });
      });
      
      $(document).ready(function () {     
            
            $("#dateFrom").datepicker({
                  changeMonth: true,
                  changeYear: true,
                  dateFormat : 'dd-mm-y'
            });   
            
            $("#dateTo").datepicker({
                  changeMonth: true,
                  changeYear: true,
                  dateFormat : 'dd-mm-y'
            });   
            
      });
      
      function issData(event){
              
             $(".issList").removeClass("highlighted");
             $(event).addClass("highlighted");  
             var value1 = $(event).find("td").eq(2).text().trim();
             var value2 = $(event).find("td").eq(3).text().trim();
             var value3 = $(event).find("td").eq(16).text().trim();
             var value4 = $(event).find("td").eq(14).text().trim();
            
             var fircValue = value1 + ':' + value2 + ':' + value3 + ':' + value4;   
            
             $("#fircVal").val(fircValue);            
            
       }
      
      
      </script>
      
</head>

<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  

  <firc:form method="post" id="form1" name="form">
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav"  style="width: 200px; ">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="home">Close</a></li>
                        </ul>
                  </div>
                  <br />
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    
                                    <li style="text-align: center;"><a href="#" id="reprint">Send Advice(FIRA)</a></li>
                                    <li style="text-align: center;"><a href="#" id="reset">Reset</a></li>
                                    <li style="text-align: center;"><a href="#" id="print">Download</a></li>
                              </ul>
                  </div>
                  <br />
                  
            </div>
                  
            <div class="col-md-10 content_box">

            <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FIRC Issuance</h5>
                  <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible " id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;Input Details</h5>
                                    </div>
                                    <div class="row page_content">
                                          <div class="col-md-6">
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">FIRC Date From </label>

                                                      <div class="col-md-4 input-group input-group-md" style="float:left;">
                                                            <firc:textfield id="dateFrom" name="issuanceVO.dataFrom" readonly="true"
                                                                   cssClass="datepicker form-control text_box" />
                                                      </div>

                                                      <label class="col-md-1 Control-label"
                                                            style="font-weight: normal;">To</label>

                                                      <div class="col-md-3 input-group input-group-md">
                                                            <firc:textfield id="dateTo" name="issuanceVO.dataTo" readonly="true"
                                                                   cssClass="datepicker1 form-control text_box" />
                                                      </div>                                                      

                                                </div>                                    
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Transaction Reference No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="transRefNo" name="issuanceVO.transrefno"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>                                          
                                                

                                          </div>
                                          
                                          
                                          
                                          <div class="col-md-6">                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">FIRC Serial No</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="serialNo" name="issuanceVO.fircno"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>            
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Transaction Status</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:select list="transStatus" name="issuanceVO.status"  id="status"
                                                                  headerValue = "<----->" headerKey=""      style="width: 180px; height: 25px"/>
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <input type="button" value="Search" class="button" id="Search" style="margin-top:5px;"/>
                                                      </div>
                                                </div>                              

                                          </div>

                                    </div>
                              
                              <div class="col-md-12">
                                    <div class="row page_content">
                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px;">&nbsp;FIRC Issuance</h5>
                                          </div>
                                          <div class="form-group">
                                                <div id="selectrow" style="display: none; color: #FF0000; font-weight: bold; font-size: 16px;">
                                                                Please select a row</div> 
                                                <div class="table">
                                                      <table border="1px" align="left">
                                                               <tbody>
                                                                   <tr>
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Issuing Bank</label></th>

                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Issuing Bank Branch</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Master Reference No</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >FIRC Serial No</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >FIRC Date</label></th>                                                                        
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Beneficiary Address</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Purpose Code</label></th>

                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Order Customer</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Amount</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Currency</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >EX Rate</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Available Amount</label></th>

                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Paid Currency</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Paid Amount</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >IRM Type</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Remarks</label></th>
                                                                                                            
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Status</label></th>
                                                                      </tr>
                                                                      
                                                                      <c:if test="${empty issuanceList}">
                                                                    <tr>
                                                                  <td colspan="17">No records found</td>
                                                                   </tr>
                                                                   </c:if>
                                                                  <c:if test="${not empty issuanceList}">
                                                                  <firc:iterator value="issuanceList" id="issuanceList">
                                                                  <tr class="issList" onclick="issData(this)">
                                                                  
                                                                      <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <firc:property value="issuingBank"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="issunibranch"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="transrefno"/>                                                                               
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="fircno"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>             
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="fircdate"/>                                                                                 
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>                   
                                                                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                                    <div class="form-group" align="left">
                                                                                          <firc:property value="benificiarydetails"/>     
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>

                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="purposecode"/>                                                                              
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="orderingcustomer"/>                                                                               
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="amount"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="currency"/>                                                                           
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                                    <div class="form-group" align="left">
                                                                                          <firc:property value="exchange_rate"/>    
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>

                                                                        </td>                               
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                          <firc:property value="available_amt"/>    
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        
                                                                  
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="paidcurrency"/>                                                                             
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="paidamount"/>                                                                               
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="mode"/>                                                                               
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="remarks"/>                                                                                  
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="status"/>                                                                             
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>                                                                                                                   
                                                                  
                                                                  </tr>
                                                                  </firc:iterator>
                                                                  </c:if>
                                                                  
                                                                  </tbody>
                                                            </table><br /><br />
                                                            <input type="hidden" id="fircVal" name="fircValue"  />
                                                </div>
                                          </div>
                                    </div>
                              </div>
                                    <div id="errorMessage" style="display: none"></div>
                              </div>
                        </div>
                  </div>
            </div>
      </firc:form>
</body>
</html>