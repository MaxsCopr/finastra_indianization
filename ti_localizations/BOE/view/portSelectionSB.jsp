<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Port of Shipment</title>
<jsp:include page="/view/common/header.jsp" />	
<script type="text/javascript" src="boe/scripts/manyBill/manualBOE.js"></script>
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/datepicker.css"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link />
<link href="css/font-awesome.css" rel="stylesheet"></link>
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
<script>
	function portOfShipdetails1() {
 
		var portNum 	 = $('#portId').val();
		var cntName 	 = $('#countryName').val();
		if(portNum == '' && cntName == ''){
	    	alert("Any one field should have values");
	    	$('#portId').focus();
	    	return false;
		}
		$("#formId1").attr("action", "fetchPortCodeListSB");
		$("#formId1").submit();
	}
	function selectPort(event) {

 
		$(".customerlist").removeClass("highlighted");
		$(event).addClass("highlighted");
		var value = $(event).find("td").eq(0).text().trim();
		$("#pos").val(value);
		$("#formId1").attr("action", "portOfShipmentToBOESB");
		$("#formId1").submit();
	}
	function returnHome() {
		$("#formId1").attr("action", "portOfShipmentToBOESB");
		$("#formId1").submit();
	}

 
</script>
</head>
 
<body class="body_bg">
 
	<%@ include file="/view/includes/TITLE.jsp"%>
 
	<boe:form method="post" id="formId1" name="form">
<div class="row">
<div class="col-md-2">
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="#" onclick="returnHome()">Close</a></li>
</ul>
</div>
<br />
<!-- <div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="#" id="okId">OK</a></li>
<li style="text-align: center;"><a href="manualBOE" id="Reset">Reset</a></li>
</ul>
</div>
<br /> -->
 
			</div>
 
			<div class="col-md-10 content_box">
 
				<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Port of Shipment</h5>				
 
				<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
 
 
					<!-- First Block is Started  -->
 
					<div class="col-md-12">
<!-- <div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">Bill Of Entry</h5>
</div> -->
 
						<div class="row page_content">
<div class="col-md-12">
<div class="col-md-5">
<div class="form-group">
<label class="col-md-5 Control-label"
											style="font-weight: normal;">Port of Shipment</label>
<div class="col-md-4 input-group input-group-md">
<boe:textfield id="portId" name="boeVO.portId"
												cssClass="form-control text_box">
</boe:textfield>
</div>
</div>
</div>
<div class="col-md-6">
<div class="form-group">
<label class="col-md-5 Control-label"
											style="font-weight: normal;">Country Name</label>
<div class="col-md-4 input-group input-group-md">
<boe:textfield id="countryName" name="boeVO.countryName"
												cssClass="form-control text_box">
</boe:textfield>
</div>
</div>
</div>
<div class="col-md-2">
<div class="form-group">
<input type="button" value="Refresh" class="button"
											onclick="portOfShipdetails1()" />
</div>
</div>
</div>
</div>

 
						<div style="clear: both; height: 20px;"></div>
 
 
				<div class="row page_content">
 
					<div class="col-md-12">
<div class="row page_content">
 
							<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;
<boe:text name="Port of Shipment List" />
</h5>
</div>
<div class="col-md-12">
<!-- <div class="table"> -->
<table border="1px" align="left">
<tbody>
<tr>
<th align="left" style="width: 400px; padding: 4px 5px;">Shipment Ports</th>
<th align="left" style="width: 400px; padding: 4px 5px;">Port Location</th>
<th align="left" style="width: 400px; padding: 4px 5px;">Country Name</th>
</tr>
 
									
<boe:iterator value="portList" id="portList" status="list">
<tr ondblclick="selectPort(this)">
<td style="padding: 4px 5px;">
<div class="form-group" align="left">
<boe:property value="portId"/>
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="padding: 4px 5px;">
<div class="form-group" align="left">
<boe:property value="portLocation"/>
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="padding: 4px 5px;">
<div class="form-group" align="left">
<boe:property value="countryName" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
</tr>
</boe:iterator>
 
									</tbody>
</table>
<br /> <br />
<!-- </div> -->
</div>
</div>
</div>
 
				</div>
<div>
 
					
<boe:hidden id="billRefNo" name="boeVO.boeNo"></boe:hidden>
<boe:hidden id="portCode" name="boeVO.portCode"></boe:hidden>
<boe:hidden id="cifNoId" name="boeVO.cifNo"></boe:hidden>
<boe:hidden id="adCode" name="boeVO.adCode"></boe:hidden>
<boe:hidden id="ieadd" name="boeVO.ieadd"></boe:hidden>
<boe:hidden id="benefName" name="boeVO.benefName"></boe:hidden>
<boe:hidden id="billEntryCurrId" name="boeVO.billCurrency"></boe:hidden>
<boe:hidden id="endorseAmtId" name="boeVO.endorseAmt"></boe:hidden>
<boe:hidden id="paymentRefNo" name="boeVO.paymentRefNo"></boe:hidden>
<boe:hidden id="outStPayAmtId" name="boeVO.outAmt"></boe:hidden>
<boe:hidden id="pdId" name="boeVO.payDate"></boe:hidden>
<boe:hidden id="boeExRate" name="boeVO.boeExRate"></boe:hidden>
<boe:hidden id="fullyAllocId" name="boeVO.fullyAlloc"></boe:hidden>
<boe:hidden id="igmNo" name="boeVO.igmNo"></boe:hidden>
<boe:hidden id="hblNo" name="boeVO.hblNo"></boe:hidden>
<boe:hidden id="mblNo" name="boeVO.mblNo"></boe:hidden>
<boe:hidden id="imAgency" name="boeVO.imAgency"></boe:hidden>
<boe:hidden id="govprv" name="boeVO.govprv"></boe:hidden>
<boe:hidden id="recInd" name="boeVO.recInd"></boe:hidden>
<boe:hidden id="thrdParty" name="boeVO.thrdParty"></boe:hidden>
<boe:hidden id="billEntryDateId" name="boeVO.boeDate"></boe:hidden>
<boe:hidden id="transType" name="boeVO.transType"></boe:hidden>
<boe:hidden id="custName" name="boeVO.custName"></boe:hidden>
<boe:hidden id="ieCode" name="boeVO.ieCode"></boe:hidden>
<boe:hidden id="ieCodeChange" name="boeVO.ieCodeChange"></boe:hidden>
<boe:hidden id="iepan" name="boeVO.iepan"></boe:hidden>
<boe:hidden id="benefCountry" name="boeVO.benefCountry"></boe:hidden>
<boe:hidden id="bilEAId" name="boeVO.billAmt"></boe:hidden>
<boe:hidden id="actualAmtId" name="boeVO.actualEndorseAmt"></boe:hidden>
<boe:hidden id="paySIIdd" name="boeVO.partPaymentSlNo"></boe:hidden>
<boe:hidden id="payAmtId" name="boeVO.paymentAmount"></boe:hidden>
<boe:hidden id="payId" name="boeVO.paymentCurr"></boe:hidden>
<boe:hidden id="eqPaymentAmount" name="boeVO.eqPaymentAmount"></boe:hidden>
<boe:hidden id="alloAmtId" name="boeVO.allocAmt"></boe:hidden>
<boe:hidden id="igmDate" name="boeVO.igmDate"></boe:hidden>
<boe:hidden id="hblDate" name="boeVO.hblDate"></boe:hidden>
<boe:hidden id="mblDate" name="boeVO.mblDate"></boe:hidden>
<boe:hidden id="pos" name="boeVO.pos"></boe:hidden>
<boe:hidden id="boeBesSBInd" name="boeVO.boeBesSBInd"></boe:hidden>
<boe:hidden id="remarkId" name="boeVO.remarks"></boe:hidden>
</div>
 
					</div>
</div>
</div>
</div>
<div id="dialogBOE1" title="Error Message"></div>
</boe:form>
 
</body>
</html>
 