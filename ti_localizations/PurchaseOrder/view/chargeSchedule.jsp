<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Home Page</title>
<%@include file="includes/header.jsp" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link href="css/datepicker.css" rel="stylesheet"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link />
<link href="css/font-awesome.css" rel="stylesheet" />

<!-- recently added -->
<link rel="stylesheet" href="css/commonTiplus.css" />

<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css" /> -->


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
<script type="text/javascript" src="js/home.js"></script>
<!-- <script src="js/purchaseorder/purchase_order.js" type="text/javascript"></script> -->


 <script src="js/jquery-ui.js"></script>

<!-- <script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/chargeSchedule.js"></script> -->

<script type="text/javascript">


function margin_cal(event)
{
var po_amt=document.getElementById("povalue").value;
var vals=getNumbers(po_amt);
console.log(vals);
function getNumbers(inputString){
    var regex=/\d+\.\d+|\.\d+|\d+/g,
        results = [],
        n;

    while(n = regex.exec(inputString)) {
        results.push(parseFloat(n[0]));
    }

    return results;
}

var margin_value=document.getElementById("margin").value;
console.log(margin_value);
var insurance = document.getElementById('insurancededuction').value;
console.log(insurance);
var freight = document.getElementById('freightdeduction').value;
console.log(freight);

var eligible_amt=(vals-vals*margin_value/100)*((100-freight-insurance)/100);
console.log(eligible_amt);



$('#eligibleamount').val(eligible_amt)
}



function dueDate_cal()
{
var valLastShipmentDate=document.getElementById("lastShipmentDate").value;
var valNTPeriod=document.getElementById("ntPeriod").value;
var dd=valLastShipmentDate.substring(0,2);
var mm=valLastShipmentDate.substring(3,5);
var yyyy=valLastShipmentDate.substring(6,10);
var formattedDate=yyyy+"-"+mm+"-"+dd;
var newDate = addDays(valLastShipmentDate,valNTPeriod);
function addDays(date,days){
      const currentDate = new Date(formattedDate);
      currentDate.setDate(currentDate.getDate() + Number(valNTPeriod));
      var newDateReturned=("0" + currentDate.getDate()).slice(-2) + "/" + ("0"+(currentDate.getMonth()+1)).slice(-2) + "/" + currentDate.getFullYear();
      
      return newDateReturned;
}
$('#dueDate').val(newDate)
}

function dueDate1_cal()
{
var valLastShipmentDate=document.getElementById("lastShipmentDate").value;
var valNTPeriod=document.getElementById("ntPeriod").value;
var dd=valLastShipmentDate.substring(0,2);
var mm=valLastShipmentDate.substring(3,5);
var yyyy=valLastShipmentDate.substring(6,10);
var formattedDate=yyyy+"-"+mm+"-"+dd;
var newDate = addDays(valLastShipmentDate,valNTPeriod);
function addDays(date,d){
      const currentDate = new Date(formattedDate);
      currentDate.setDate(currentDate.getDate() + Number(d));
      var newDateReturned=("0" + currentDate.getDate()).slice(-2) + "/" + ("0"+(currentDate.getMonth()+1)).slice(-2) + "/" + currentDate.getFullYear();
      
      return newDateReturned;
}
$('#dueDate').val(newDate)
}

$(document).ready(function() {
$("#exportexpiryDate").datepicker({
changeMonth: true,
changeYear: true,
dateFormat: 'dd/mm/yy'
});
$("#lastShipmentDate").datepicker({
changeMonth: true,
changeYear: true,
dateFormat: 'dd/mm/yy'
});
$("#datepicker2").datepicker({
changeMonth: true,
changeYear: true,
dateFormat: 'dd/mm/yy'
});  
 
 
$('input[type="submit"],a').click(function() {
$('body').modal({
show : 'false'
});
$('body').removeClass('removePageLoad');
$('body').addClass('addPageLoad');

});

});
function sync() {
var a = document.getElementById('insurancededuction');
var twogiga = document.getElementById('insurancededuction').value;
var fourgiga = document.getElementById('freightdeduction').value;

var sum = parseInt(twogiga) + parseInt(fourgiga);
var sum1 = parseInt(sum);

if (parseInt(sum) >= 100) {
alert("Addition of InsuranceDeduction and FreightDeduction should not be more or equal to 100");
document.getElementById('insurancededuction').value = "";
document.getElementById('freightdeduction').value = "";
}

else if (a.value >= 100) {

alert("Percentage Doesnot Exceed or Equal to 100");
document.getElementById('insurancededuction').value = "";
} else {
$("#myForm").attr("action", "calculate");
$("#myForm").submit();
}
}
function validate() {
var b = document.getElementById('freightdeduction');

if (b.value >= 100) {
alert("Percentage Doesnot Exceed  or Equal to 100");
document.getElementById('freightdeduction').value = "";
}
sync();
}
if (window.addEventListener) {
$('body').modal({
show : 'false'
});
$('body').addClass('addPageLoad');
window.addEventListener("load", doLoad, false);
}

function reset() {
$("#myForm").attr("action", "reset");
$("#myForm").submit();
}

function getValue() {
var v = document.getElementById('povalue');

$("#eligibleamount").val(v.value);
document.getElementById("insurancededuction").value = 0;
document.getElementById("freightdeduction").value = 0;
parseAlphaNumeric();
fetchMargin();

}

function parseAlphaNumeric() {
var vAlphaNum = document.getElementById("povalue").value; //"125sdf5jk67lk98";

var vAlpha = vAlphaNum.split(/[a-zA-Z]+/);
var vNum = vAlphaNum.split(/[.0-9]+/);
$('#povalue').val(vAlpha.join("") + " " + vNum.join(""));

}

function fetchBenName() {

$("#myForm").attr("action", "fetchBenName");
$("#myForm").submit();
}

function fetchMargin() {

$("#myForm").attr("action", "calculate");
$("#myForm").submit();
}

function copyPurchaseDetails() {
if (confirm('Do you want to Copy this Data?')) {
$("#myForm").attr("action", "copyPurchaseOrderDetails");
$("#myForm").submit();
} else {
}
}



</script>

</head>


<body class="body_bg" onload="margin_cal()">
<%-- <%@ include file="/view/includes/TITLE.jsp"%> --%>
<img src="images/FusionBanking.png" width="100%" />
<gr:form method="post" id="myForm" name="icForm">
<div class="row">
      <div class="col-md-2">
            <div class="side_nav" style="width: 215px;">
                  <ul class="nav nav-pills nav-stacked">
                  <li style="text-align: center;"><a href="home">Close</a></li>
                  </ul>
            </div><br />
            <div class="side_nav" style="width: 215px;">
                  <ul class="nav nav-pills nav-stacked">
                  <li style="text-align: center;"><a href="#" id="submit"
                  onclick="exportsum()">submit</a></li>
                  </ul>
            </div><br />      
            <div class="side_nav" style="width: 215px;">
                  <ul class="nav nav-pills nav-stacked">
                  <li style="text-align: center;"><a href="#" id="reset"
                  onclick="reset()">Reset</a></li>
                  </ul>
            </div><br />
            <div class="side_nav" style="width: 215px;">
                  <ul class="nav nav-pills nav-stacked">
                  <li style="text-align: center;"><a href="#" id="validate">Validate</a></li>
                  </ul>
            </div>
      </div>

      <div class="col-md-10 content_box">

            <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Purchase Order - Maker Process</h5><br /> <br />
            <div class="row page_content">
                  <div class="col-md-12">
                        <div class="page_collapsible" id="body-section1"><span></span>
                        <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Error Description</h5>
                        </div>

                        <div class="form-group">
                                    <div align="center" id="div1">
                                          <gr:if test="hasActionErrors()">
                                                <div class="errors">
                                                      <gr:actionerror />
                                                </div>
                                          </gr:if>
                                          <gr:if test="hasActionMessages()">
                                                <div class="welcome">
                                                      <gr:actionmessage />
                                                </div>
                                          </gr:if>
                                    </div><br />
                              <div class="table">
                                    <table border="1px" align="left" id="errorList">
                                    <tbody>
                                          <tr>
                                                <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Severity</label></th>
      
                                                <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Description</label></th>
      
                                                <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Steps</label></th>
      
                                                <th style="text-align: left; width: 450px"><label>&nbsp;&nbsp;Details</label></th>
      
                                                <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Overridden</label></th>
                                          </tr>
      
                                          <gr:iterator value="chargeVO.errorList" id="errorList">
                                          <tr>
                                                <td>
                                                      <div class="form-group" align="left">
                                                            <gr:property value="errorId" />
                                                            <div class="col-md-8 input-group input-group-md">
                                                            </div>
                                                      </div>
      
                                                </td>
                                                <td>
                                                      <div class="form-group" align="left">
                                                            <gr:property value="errorDesc" />
                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                      </div>
                                                </td>
                                                <td>
                                                      <div class="form-group" align="left">
                                                            <gr:property value="errorCode" />
                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                      </div>
                                                </td>
                                                <td>
                                                      <div class="form-group" align="left">
                                                            <gr:property value="errorDetails" />
                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                      </div>
                                                </td>
                                                <td>
                                                      <div class="form-group" align="left">
                                                            <gr:property value="errorMsg" />
                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                      </div>
                                                </td>
      
                                          </tr>
                                          </gr:iterator>
                                    </tbody>
                                    </table>
                              </div>
                        </div>
                  </div>
                        <c:if test="${not empty msg}">
                        <h5 style="font-weight: lighter; font-size: 13px; color: #FF0000;">
                        <gr:property value="msg" />
                        </h5>
                        </c:if>
            </div><br />
                  <div id="userIdMessage" style="color: orange"></div><br />
                  
            <div class="row cont_colaps">
                  <div class="col-md-12">
                        <div class="page_collapsible" id="body-section1"><span></span>
                              <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Input Details</h5>
                        </div>
                        <div class="row page_content">
                              <div class="col-md-6">
                                    <div class="form-group required">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Export Order/PO Number/LC Number &nbsp;&nbsp;</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="amountFrom" name="chargeVO.exportOrderNumber" cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">CIF ID&nbsp;<span class="searchBtn"
                                                style="color: #579EDC; font-weight: bold; cursor: pointer;" onclick="customerCifCode()">
                                                <img src="images/magnify.png" width="13%" height="13%" /></span></label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="transRefNo" name="chargeVO.cifID" readonly="false" cssClass="form-control text_box" onBlur="fetchBenName()"/>
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Inco Terms&nbsp;
                                                <span class="searchBtn"
                                                style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                onclick="incoTerms()"><img src="images/magnify.png"
                                                width="13%" height="13%" /></span></label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="amountTo" name="chargeVO.incoTerms" readonly="false" cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">PO Value</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="povalue" placeholder="Eg:1000 USD"
                                                name="chargeVO.poValue" cssClass="form-control text_box"
                                                onblur="getValue()" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Expiry Date &nbsp;&nbsp;</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="exportexpiryDate"
                                                name="chargeVO.exportexpiryDate"
                                                cssClass="datepicker form-control text_box" readonly="true"
                                                style="text-align: left;" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">% Freight Deduction</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="freightdeduction"
                                                name="chargeVO.freightDeduction" onblur="validate()"
                                                cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Margin % &nbsp;&nbsp;</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <!-- The fields true earlier due to jira 5514 now it is marked as true -->
                                                <gr:textfield id="margin" name="chargeVO.marginPercentage"
                                                readonly="false" cssClass="form-control text_box"  onchange="margin_cal(this)"/>
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Goods Code <span class="searchBtn"
                                                style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                onclick="fetchGoodsCode()"><img     src="images/magnify.png" width="13%" height="13%" /></span></label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="goodsCode" name="chargeVO.goodsCode"
                                                cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Normal Transit Period &nbsp;&nbsp;</span></label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="ntPeriod" name="chargeVO.ntPeriod" value="21"
                                                cssClass="form-control text_box" onchange="dueDate1_cal()"/>
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Due Date &nbsp;&nbsp;</span></label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="dueDate" name="chargeVO.dueDate" readonly="true"
                                                cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <!-- <i class="fa fa-globe fa-1x " style="color:lightblue;"></i> -->
                                    
                                    
                                    
                                    
                              </div>
                              <div class="col-md-6">
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Export Order/PO date/LC Date &nbsp;&nbsp;</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <%-- <gr:textfield id="amountTo" name="chargeVO.exporterOrderDate"
                                                cssClass="form-control text_box" /> --%>
                                                <gr:textfield id="datepicker2"
                                                name="chargeVO.exporterOrderDate"
                                                cssClass="datepicker form-control text_box" readonly="true"
                                                style="text-align: left;" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;"> Beneficiary Name</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="refNo" name="chargeVO.beneficiaryName"
                                                readonly="true" cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;"> Importer Name</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="importerName" name="chargeVO.importerName"
                                                cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Export Letter Credit No<span class="searchBtn"style="color: #579EDC; font-weight: bold; cursor: pointer;"onclick="goToInwardPage()"><img src="images/magnify.png" width="13%" height="13%" /></span></label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="inwardNo" name="chargeVO.inwardNo"
                                                cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Last Shipment Date&nbsp;&nbsp;</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="lastShipmentDate"
                                                name="chargeVO.lastShipmentDate" readonly="true"
                                                cssClass="datepicker form-control text_box"
                                                style="text-align: left;" onchange="dueDate_cal()"/>
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">% Insurance Deduction</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="insurancededuction"
                                                name="chargeVO.insuranceDeduction"
                                                cssClass="form-control text_box" onBlur="sync()" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Eligible Amount&nbsp;&nbsp;</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="eligibleamount"
                                                name="chargeVO.eligibleAmount" readonly="true"
                                                cssClass="form-control text_box" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Goods Description&nbsp;&nbsp;</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textarea id="goodsDescription"
                                                name="chargeVO.goodsDescription" cols="18" rows="4"
                                                style="margin: 0px; width: 200px; height: 60px;" />
                                          </div>
                                    </div>
                                    <div class="form-group">
                                          <div class="col-md-6 Control-label">
                                                <label style="font-weight: normal;">Shipment Mode&nbsp;&nbsp;</label>
                                          </div>
                                          <div class="col-md-3 input-group input-group-md">
                                                <gr:textfield id="shpMode"
                                                name="chargeVO.shpMode" readonly="true"
                                                cssClass="form-control text_box" />
                                          </div>
                                    </div>
                              </div>
                              <div style="clear: both; height: 10px;"></div>
                              <div class="form-group">
                                    <div class="col-md-4"></div>
                                    <div class="col-md-4">
                                    <div class="col-md-3" style="margin-left: 25%;">
                                                <input style="width: 80px;height: 25px;" type="button" value="Search"
                                                class="button"  onclick="fetchPurchaseOrder()" />
                                          </div>
                                    </div>
                                    <div class="col-md-4"></div>
                              </div>
                              <div style="clear: both; height: 10px;"></div>
                        </div>
                        


                              <gr:hidden name="chargeVO.chargeDesc"></gr:hidden>
                              <gr:hidden name="chargeVO.chargeId"></gr:hidden>
                              <gr:hidden name="chargeVO.chargeKey97"></gr:hidden>
                              <gr:hidden name="chargeVO.productDesc"></gr:hidden>
                              <gr:hidden name="chargeVO.productId"></gr:hidden>
                              <gr:hidden name="chargeVO.productKey97"></gr:hidden>
                              <gr:hidden id="status" name="chargeVO.status" />
                              
                              <gr:hidden id="updateCIFId" name="chargeVO.updateCusCif"></gr:hidden>
                              <gr:hidden id="updateChargeId" name="chargeVO.updateChargeId"></gr:hidden>
                              <gr:hidden id="updateKey97Id" name="chargeVO.updateChargeKey97"></gr:hidden>
                              <gr:hidden id="updateKey97Id" name="chargeVO.updateChargeKey97"></gr:hidden>
                              <gr:hidden id="updateKey97Id" name="chargeVO.updateChargeKey97"></gr:hidden>
                              <gr:hidden id="updateKey97Id" name="chargeVO.updateChargeKey97"></gr:hidden>


                  </div>
            </div>



            <div class="row cont_colaps">
                  <div class="col-md-12">
                        <div class="page_collapsible" id="body-section1"><span></span>
                              <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Purchase Order Details</h5>
                        </div>
                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="form-group">
                                          <div class="table">
                                                <table border="1px" align="left" id="fxList">
                                                      <tbody>
                                                      <tr>
                                                            <th style="text-align: left; width: 120px;"><label>Purchase
                                                            Order No </label></th>
                                                      
                                                            <th style="text-align: left; width: 120px;"><label>Purchase
                                                            Order Date</label></th>
                                                      
                                                            <th style="text-align: left; width: 120px;"><label>CIF
                                                            ID </label></th>
                                                      
                                                            <th style="text-align: left; width: 120px;"><label>Beneficiary
                                                            Name </label></th>
                                                      
                                                            <th style="text-align: left; width: 120px;"><label>Inco
                                                            Terms </label></th>
                                                      
                                                            <th style="text-align: left; width: 120px;"><label>Goods
                                                            Description </label></th>
                                                      
                                                            <th style="text-align: left; width: 120px;"><label>Purchase
                                                            Order Value </label></th>
                                                      
                                                            <th style="text-align: left; width: 120px;"><label>Importer
                                                            Name</label></th>
                                                            <th style="text-align: left; width: 120px;"><label>Eligible
                                                            Amount </label></th>
                                                            <th style="text-align: left; width: 120px;"><label>Select
                                                            </label></th>
                                                      </tr>
                                                <gr:iterator value="chargeVO.purchaseOrderList"
                                                id="fxConvListId" status="list">
                                                      <tr>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poNo"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                      
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poDate"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poCif"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poBen"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poInco"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poGoodDesc"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poAmtValue"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poImpName"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:property value="poEligAmt"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td>
                                                                  <div class="form-group trData" align="center"
                                                                  style="width: 120px">
                                                                        <gr:radio label="Select" name="chargeVO.copyVal"
                                                                        list="#{poNo:''}" onclick="copyPurchaseDetails()"/>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                      
                                                      </tr>
                                                </gr:iterator>
                                                      </tbody>
                                                </table>
                                          </div>
                                    </div>
                                    <div class="col-md-6">
                                          <div style="margin-left: 42%; float: left">
                                                <div style="float: left">
                                                      <input type="button" id="search" value="Load All"
                                                      class="button" onclick="searchPurchaseOrder()"
                                                      style="width: 130px" />
                                                </div>
                                          </div>
                                    </div>
                              </div>
                              <div style="clear: both; height: 10px;"></div>
                        </div>
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="col-md-12">
                                          <div class="row page_content">
                                                <div id="selectRow" style="display: none">
                                                      <h5 id="GreenTextID">Please Select the Row...</h5>
                                                </div>
                                                <div id="selectRow1" style="display: none">
                                                      <h5 id="GreenTextID">Please Select All Feilds...</h5>
                                                </div>
                                                <div class="form-group"></div>
                                          </div>
                                    </div>
                              </div>
                        </div>
                        <div style="clear: both; height: 20px;"></div>
                        <div class="row page_content"></div>
                        <div style="clear: both; height: 10px;"></div>
                  </div>
            </div>
            <div style="clear: both; height: 20px;"></div>
      </div>
</div>
</gr:form>
<div id="footer">
<%@ include file="/view/includes/FOOTER.jsp"%>
</div>
</body>
</html>