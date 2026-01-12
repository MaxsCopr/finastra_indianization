<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="mtt" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>MTT Enquiry Search</title>

<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link href="css/datepicker.css" rel="stylesheet"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" /><link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" /><link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" /><link />
<link href="css/font-awesome.css" rel="stylesheet"></link>
<link rel="stylesheet" href="css/datepicker.css"></link>
<link rel="stylesheet" href="css/commonTiplus.css"></link>


<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>

<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>

<script src="js/bootstrap-datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="js/date_search.js"></script>
<script type="text/javascript" src="js/WiseConnect.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>


<script type="text/javascript">

 function checkMTTNumber1() {
      
      var mttNum   = $('#mttListNumber1').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber2() {
      
      var mttNum   = $('#mttListNumber2').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber3() {
      
      var mttNum   = $('#mttListNumber3').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber4() {
      
      var mttNum   = $('#mttListNumber4').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber5() {
      
      var mttNum   = $('#mttListNumber5').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber6() {
      
      var mttNum   = $('#mttListNumber6').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber7() {
      
      var mttNum   = $('#mttListNumber7').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber8() {
      
      var mttNum   = $('#mttListNumber8').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber9() {
      
      var mttNum   = $('#mttListNumber9').val();
      $("#mttListNumberToCheck").val(mttNum);
      if(mttNum != '' || mttNum != 0){
            $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}
function checkMTTNumber10() {
      
      var mttNum   = $('#mttListNumber10').val();
      $("#mttListNumberToCheck").val(mttNum);
    if(mttNum != '' || mttNum != 0){
      $('#submit').attr("disabled", true);
      $('#form1').attr('action', 'mttNumberCheckMTTNum1');
            $('#form1').submit();
      }
}

function checkMasterRef() {
      
      var eveRef   = $('#mttSplitEveRef').val();
      var masRef   = $('#mttSplitMasRef').val();
    if(eveRef == '' || masRef == ''){
      
      alert("Please Enter Master Ref and Event Ref");
      return false;
      }else{
      $('#form1').attr('action', 'mttNumberCheckMasterRef');
      $('#form1').submit();
      }
}

      $(document).ready(function() {
            
            $("#Search").click(function(){
                   //alert("Inside Search");
                   $("#form1").attr("action","fetchMTTNumberOnMasterRef");
                    $("#form1").submit();
            });         
            
            $("#reset").click(function(){
                   $("#form1").attr("action","mttNumberSplitMakerProcess");
                   $("#form1").submit();
            });
            
          $("#submit").click(function(){
                   var remark = $.trim($('#splitRemarks').val());
                  
                   var mttTransAmt0 = $.trim($('#mttListTransAmt0').val());
                   //alert("Inside Search 0 "+mttTransAmt0);
                   var mttTransAmt1 = $.trim($('#mttListTransAmt1').val());
                   var mttTransAmt2 = $.trim($('#mttListTransAmt2').val());
                   var mttTransAmt3 = $.trim($('#mttListTransAmt3').val());
                   var mttTransAmt4 = $.trim($('#mttListTransAmt4').val());
                   var mttTransAmt5 = $.trim($('#mttListTransAmt5').val());
                   var mttTransAmt6 = $.trim($('#mttListTransAmt6').val());
                   var mttTransAmt7 = $.trim($('#mttListTransAmt7').val());
                   var mttTransAmt8 = $.trim($('#mttListTransAmt8').val());
                   var mttTransAmt9 = $.trim($('#mttListTransAmt9').val());
                   var mttTransAmt10 = $.trim($('#mttListTransAmt10').val());
                  
                   var mttListNumber1 = $.trim($('#mttListNumber1').val());
                   var mttListNumber2 = $.trim($('#mttListNumber2').val());
                   var mttListNumber3 = $.trim($('#mttListNumber3').val());
                   var mttListNumber4 = $.trim($('#mttListNumber4').val());
                   var mttListNumber5 = $.trim($('#mttListNumber5').val());
                   var mttListNumber6 = $.trim($('#mttListNumber6').val());
                   var mttListNumber7 = $.trim($('#mttListNumber7').val());
                   var mttListNumber8 = $.trim($('#mttListNumber8').val());
                   var mttListNumber9 = $.trim($('#mttListNumber9').val());
                   var mttListNumber10 = $.trim($('#mttListNumber10').val());
                  
                  
                   var totalMttAmt=Number(mttTransAmt1)+Number(mttTransAmt2)+Number(mttTransAmt3)+Number(mttTransAmt4)+Number(mttTransAmt5)+Number(mttTransAmt6)+Number(mttTransAmt7)+Number(mttTransAmt8)+Number(mttTransAmt9)+Number(mttTransAmt10);
                   //alert("Total "+totalMttAmt);
                   if(remark.length == 0){
                        alert("Remarks for Add must be filled...");
                        return false;
                   } else if (mttTransAmt0 !=totalMttAmt) {
                        alert("Amount is not matching with Transaction amount...");
                        return false;
                   }
                   else{
                         if(mttTransAmt1 !=null && mttTransAmt1 !="")
                         {
                               $("#mttListNumber11").val(mttListNumber1);
                               $("#mttListTransAmt11").val(mttTransAmt1);
                         }
                        
                         if(mttTransAmt2 !=null && mttTransAmt2 !="")
                         {
                               $("#mttListNumber12").val(mttListNumber2);
                               $("#mttListTransAmt12").val(mttTransAmt2);
                         }
                        
                         if(mttTransAmt3 !=null && mttTransAmt3 !="")
                         {
                               $("#mttListNumber13").val(mttListNumber3);
                               $("#mttListTransAmt13").val(mttTransAmt3);
                         }
                        
                         if(mttTransAmt4 !=null && mttTransAmt4 !="")
                         {
                               $("#mttListNumber14").val(mttListNumber4);
                               $("#mttListTransAmt14").val(mttTransAmt4);
                         }
                        
                         if(mttTransAmt5 !=null && mttTransAmt5 !="")
                         {
                               $("#mttListNumber15").val(mttListNumber5);
                               $("#mttListTransAmt15").val(mttTransAmt5);
                         }
                        
                         if(mttTransAmt6 !=null && mttTransAmt6 !="")
                         {
                               $("#mttListNumber16").val(mttListNumber6);
                               $("#mttListTransAmt16").val(mttTransAmt6);
                         }
                        
                         if(mttTransAmt7 !=null && mttTransAmt7 !="")
                         {
                               $("#mttListNumber17").val(mttListNumber7);
                               $("#mttListTransAmt17").val(mttTransAmt7);
                         }
                        
                         if(mttTransAmt8 !=null && mttTransAmt8 !="")
                         {
                               $("#mttListNumber18").val(mttListNumber8);
                               $("#mttListTransAmt18").val(mttTransAmt8);
                         }
                        
                         if(mttTransAmt9 !=null && mttTransAmt9 !="")
                         {
                               $("#mttListNumber19").val(mttListNumber9);
                               $("#mttListTransAmt19").val(mttTransAmt9);
                         }
                        
                         if(mttTransAmt10 !=null && mttTransAmt10 !="")
                         {
                               $("#mttListNumber20").val(mttListNumber10);
                               $("#mttListTransAmt20").val(mttTransAmt10);
                         }
                        
                        $("#form1").attr("action","submitMttNumberSplit");
                        $("#form1").submit();
                   }
            });  

            /* $("#add").click(function(){
                   $("#form1").attr("action","mttNumberAddMasterRef");
                   $("#form1").submit();
            }); */
            /* $("#addEventRefNo").click(function(){
                   $("#form1").attr("action","mttNumberCheckMasterRef");
                   $("#form1").submit();
            });
            $("#addMttNumber").click(function(){
                   $("#form1").attr("action","mttNumberCheckMTTNum");
                   $("#form1").submit();
            }); */
      });
      
      
      function add() {
           var element = document.createElement("input");
           element.setAttribute("type", "text");
           element.setAttribute("name", "mytext");
          var spanvar = document.getElementById("myspan");
          var spanvar1 = document.getElementById("myspan");
          spanvar .appendChild(element);
          spanvar1 .appendChild(element);
         }
      
      
      /* var index = 1;
    function insertRow(){
      
      if(index>10){
            alert("Maximum 10 fields can be added..");
            return false;
      }
      else{
              var table=document.getElementById("myTable");
            var row=table.insertRow(table.rows.length);
            var cell1=row.insertCell(0);
            var t1=document.createElement("textfield");
                t1.id = "mttListNumber"+index;
                t1.name = "mttvo.mttListNumber"+index;
                cell1.appendChild(t1);
            var cell2=row.insertCell(1);
            var t2=document.createElement("textfield");
                t2.id = "mttListTransAmt"+index;
                t2.value = "mttvo.mttListTransAmt"+index;
                cell2.appendChild(t2);
            /* var cell3=row.insertCell(2);
            var t3=document.createElement("button");
                t3.id = "remove"+index;
                cell3.appendChild(t3); */
           
         /* index++;
      }

    }  */
      </script>


<!--[if IE 7]>
   <link rel="stylesheet" type="text/css" href="css/styleie7.css" />
 <![endif]-->
</head>

<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  

  <mtt:form method="post" id="form1" name="form">
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="home">Close</a></li>
                        </ul>
                  </div>
                  
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="reset">Reset</a></li>
                              </ul>
                  </div>
                  <c:if test ="${mttVO.isButtonVisible == 'true'}">
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="submit">Submit</a></li>
                              </ul>
                  </div>
                  </c:if>
                  <br />
                  
            </div>
                  
            <div class="col-md-10 content_box">

            <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MTT Split</h5>
                  <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible " id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;Input Details</h5>
                                    </div>
                                    <div class="row page_content">
                                          <%-- <div class="form-group">
                                                      
                                                      <div class="col-md-4 input-group input-group-md" ">
                                                                  <mtt:if test="hasActionMessage()">
                                                                              <mtt:actionmessage cssStyle="font-weight: bold; font-size: 13px; color:red" />
                                                                  </mtt:if>
                                                      </div>
                                          </div> --%>
                                          <div class="col-md-6">                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Master Reference No</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <mtt:textfield id="mttSplitMasRef" name="mttVO.mttSplitMasRef"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <c:if test ="${mttVO.mttSplitDeleteFlag == 'true'}">
                                                                  <div class="col-md-4 input-group input-group-md" style="padding-left: 15px;">
                                                                        <span style="color: Red;font-weight: bold;">Kindly delete Existing Entry...</span>
                                                                  </div>
                                                      </c:if>
                                                      <c:if test ="${mttVO.mttSplitREPPurchaseFlag == 'true'}">
                                                                  <div class="col-md-4 input-group input-group-md" style="padding-left: 15px;">
                                                                        <span style="color: Red;font-weight: bold;">Report Purchase Sale is not Y...</span>
                                                                  </div>
                                                      </c:if>
                                                      <c:if test ="${mttVO.mttSplitPendingFlag == 'true'}">
                                                                  <div class="col-md-4 input-group input-group-md" style="padding-left: 15px;">
                                                                        <span style="color: Red;font-weight: bold;">Records already in MTT Number Split Process...</span>
                                                                  </div>
                                                      </c:if>
                                                      <c:if test ="${mttVO.mttAmendPendingFlag == 'true'}">
                                                                  <div class="col-md-4 input-group input-group-md" style="padding-left: 15px;">
                                                                        <span style="color: Red;font-weight: bold;">Records already in MTT Number Amend Process...</span>
                                                                  </div>
                                                      </c:if>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <input type="button" value="Search" class="button" id="Search" style="margin-top:5px;"/>
                                                      </div>
                                                </div>
                                          </div>
                                          
                                          <div class="col-md-6">                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Event Reference No</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <mtt:textfield id="mttSplitEveRef" name="mttVO.mttSplitEveRef"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>
                                                
                                          </div>

                                    </div>
                                    
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;MTT Number Details</h5>
                                    </div>
                                    
                                    <div class="form-group">
                                          <div class="col-md-12">
                                                <div class="form-group">
                                                            
                                                            <div class="col-md-4 input-group input-group-md">
                                                                        <mtt:if test="hasActionErrors()">
                                                                                    <mtt:actionerror cssStyle="font-weight: bold; font-size: 13px; color:red" />
                                                                        </mtt:if>
                                                            </div>
                                                </div>
                                          </div>
                                          <div class="form-group">
                                                      <label class="col-md-2 Control-label"
                                                            style="font-weight: bold;padding-left: 0px;">Total Amount</label>
                                                      <div class="col-md-2 input-group input-group-md">
                                                            <mtt:textfield id="mttListTransAmt0" name="mttVO.mttListTransAmt0"
                                                                  cssClass="form-control text_box"  readonly="true" />
                                                      </div>
                                          </div>
                                          <div class="form-group">
                                                <div class="col-md-6">
                                                      <div class="table" style="width:500px;">
                                                            
                                                            <table border="1px" align="left" id="myTable">
                                                                     <tbody>
                                                                         <tr>
                                                                            <th style="text-align: left; width:200px; padding:4px 5px;"><label >MTT Number</label></th>
                                                                              <th style="text-align: left; width:200px; padding:4px 5px;"><label >Amount</label></th>
                                                                              <!-- <th style="text-align: left; width:200px; padding:4px 5px;"><input type="button" id="btnAdd" class="button-add" onClick="insertRow()" value="Add"></input></th> -->
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber1" name="mttVO.mttListNumber1"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber1()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt1" name="mttVO.mttListTransAmt1"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber2" name="mttVO.mttListNumber2"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber2()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt2" name="mttVO.mttListTransAmt2"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber3" name="mttVO.mttListNumber3"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber3()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt3" name="mttVO.mttListTransAmt3"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber4" name="mttVO.mttListNumber4"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber4()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt4" name="mttVO.mttListTransAmt4"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber5" name="mttVO.mttListNumber5"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber5()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt5" name="mttVO.mttListTransAmt5"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber6" name="mttVO.mttListNumber6"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber6()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt6" name="mttVO.mttListTransAmt6"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber7" name="mttVO.mttListNumber7"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber7()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt7" name="mttVO.mttListTransAmt7"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber8" name="mttVO.mttListNumber8"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber8()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt8" name="mttVO.mttListTransAmt8"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber9" name="mttVO.mttListNumber9"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber9()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt9" name="mttVO.mttListTransAmt9"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                         <tr>
                                                                              <td><mtt:textfield id="mttListNumber10" name="mttVO.mttListNumber10"
                                                                                          cssClass="form-control text_box"  readonly="false" onchange="checkMTTNumber10()"/></td>
                                                                              <td><mtt:textfield id="mttListTransAmt10" name="mttVO.mttListTransAmt10"
                                                                                          cssClass="form-control text_box"  readonly="false" /></td>
                                                                         </tr>
                                                                        
                                                                        </tbody>
                                                                        
                                                                  </table>
                                                                  
            
                        
                                                                  
                                                                  <br />
                                                                  <br />
                                                      </div>
                                                </div>
                                                <div class="col-md-3">
                                                            <label style="float: left; margin-left: 15px;">Maker Remarks</label><br></br>
                                                            <div class="form-group" style="float: left; margin-left: 15px;">
                                                                  <mtt:textarea name="splitRemarks" id="splitRemarks" cols="15" rows="5"></mtt:textarea>
                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                            </div>
                                                </div>
                                          </div>
                                    </div>
                              <input type="hidden" id="mttListNumber11" name="mttListNumber11"  />
                              <input type="hidden" id="mttListTransAmt11" name="mttListTransAmt11"  />
                              <input type="hidden" id="mttListNumber12" name="mttListNumber12"  />
                              <input type="hidden" id="mttListTransAmt12" name="mttListTransAmt12"  />
                              <input type="hidden" id="mttListNumber13" name="mttListNumber13"  />
                              <input type="hidden" id="mttListTransAmt13" name="mttListTransAmt13"  />
                              <input type="hidden" id="mttListNumber14" name="mttListNumber14"  />
                              <input type="hidden" id="mttListTransAmt14" name="mttListTransAmt14"  />
                              <input type="hidden" id="mttListNumber15" name="mttListNumber15"  />
                              <input type="hidden" id="mttListTransAmt15" name="mttListTransAmt15"  />
                              <input type="hidden" id="mttListNumber16" name="mttListNumber16"  />
                              <input type="hidden" id="mttListTransAmt16" name="mttListTransAmt16"  />
                              <input type="hidden" id="mttListNumber17" name="mttListNumber17"  />
                              <input type="hidden" id="mttListTransAmt17" name="mttListTransAmt17"  />
                              <input type="hidden" id="mttListNumber18" name="mttListNumber18"  />
                              <input type="hidden" id="mttListTransAmt18" name="mttListTransAmt18"  />
                              <input type="hidden" id="mttListNumber19" name="mttListNumber19"  />
                              <input type="hidden" id="mttListTransAmt19" name="mttListTransAmt19"  />
                              <input type="hidden" id="mttListNumber20" name="mttListNumber20"  />
                              <input type="hidden" id="mttListTransAmt20" name="mttListTransAmt20"  />
                              
                              
                              <input type="hidden" id="mttListNumberToCheck" name="mttListNumberToCheck"  />
                        </div>
                        </div>
                  
                  </div>
            </div>
      </mtt:form>
</body>
</html>