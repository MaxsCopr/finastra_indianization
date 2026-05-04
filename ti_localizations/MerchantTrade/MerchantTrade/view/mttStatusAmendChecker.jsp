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
	$(document).ready(function() {
		$("#refresh").click(function(){
			 $("#form1").attr("action","mttfetchstatusCheckerData");
			 $("#form1").submit();
		});  	 
		$("#reset").click(function(){
			 $("#form1").attr("action","mttstatusCheckerProcess");
			 $("#form1").submit();
		}); 
		$("#ok").click(function() {
			$("#check").val("Approve");
			var remark = $.trim($('#remarks').val());
			if(remark.length == 0){
				alert("Remarks must be filled out");
				return false;
			}
			$('#form1').attr('action','approveStatusCheckerData');
			$('#form1').submit();
 
		});
		$("#reject").click(function() {
			$("#check").val("Reject");
			var remark = $.trim($('#remarks').val());
			if(remark.length == 0){
				alert("Remarks must be filled out");
				return false;
			}		
			$('#form1').attr('action','rejectStatusCheckerData');
			$('#form1').submit();
 
		});
	});
</script>
 
 
<!--[if IE 7]>
<link rel="stylesheet" type="text/css" href="css/styleie7.css" />
<![endif]-->
</head>
 
<body class="body_bg">
<%@ include file="/view/includes/TITLE.jsp" %>  
 
  <mtt:form method="post" id="form1" name="form">
<div class="row">
<div class="col-md-2">
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="checkerProcess">Close</a></li>
</ul>
</div>
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="#" id="reset">Reset</a></li>
</ul>
</div>
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="#" id="ok">Approve</a></li>
</ul>
</div>
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="#" id="reject">Reject</a></li>
</ul>
</div>
<br />
</div>
<div class="col-md-10 content_box">
 
		<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MTT Status Amend</h5>
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
										style="font-weight: normal;">MTT Number</label>
<div class="col-md-3 input-group input-group-md">
<mtt:textfield id="mttStatusCheckerNumber" name="mttVO.mttStatusCheckerNumber"
											cssClass="form-control text_box"/>
</div>
</div>
<div class="form-group">
<label class="col-md-4 Control-label"
											style="font-weight: normal;"></label>
<div class="col-md-3 input-group input-group-md">
<input type="button" value="Refresh" class="button" id="refresh" style="margin-top:5px;"/>
</div>
</div>
</div>
<div class="col-md-6">		
<div class="col-md-8"></div>
<div class="col-md-2">	
<h5 style="font-weight: bold;font-size: 10px;color: red;float: right;">&nbsp;*Y=Closed/N=Active</h5>
</div>
</div>
</div>
</div>
<div class="col-md-12">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;MTT Status Amend Pending List</h5>
</div>
<div class="form-group">
<div class="table"  style="float:left;">
<table border="1px" align="left">
<tbody>
<tr>
<th style="text-align: left; width:300px; padding:4px 5px;"><label >MTT Number</label></th>
<th style="text-align: left; width:300px; padding:4px 5px;"><label >Changed Status</label></th>
<th style="text-align: left; width:300px; padding:4px 5px;"><label >Maker User</label></th>
<th style="text-align: left; width:300px; padding:4px 5px;"><label >Maker Timestamp</label></th>
<th style="text-align: left; width:300px; padding:4px 5px;"><label >Select</label></th>
</tr>
<c:if test="${empty pendingtiList}">
<tr>
<td colspan="3">No records found</td>
</tr>
</c:if>
<c:if test="${not empty pendingtiList}">
<mtt:iterator value="pendingtiList" id="pendingtiList">
<tr>
<td style="padding:4px 5px;">
<div class="form-group" align="left">
<mtt:property value="mttListMTTNumber"/>														
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="padding:4px 5px;">
<div class="form-group" align="left">
<mtt:property value="mttListCurrentStatus"/>
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="padding:4px 5px;">
<div class="form-group" align="left">
<mtt:property value="mttListMakerUserId"/>														
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="padding:4px 5px;">
<div class="form-group" align="left">
<mtt:property value="mttListMakertmstmp"/>														
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="text-align: center;">
<div class="form-group">
<mtt:checkbox name="chkList"
															fieldValue="%{#pendingtiList.mttListKeyId+':'
																			+#pendingtiList.mttListMTTNumber+':'
																			+#pendingtiList.mttListCurrentStatus}">
</mtt:checkbox>
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
</tr>
</mtt:iterator>
</c:if>
</tbody>
</table><br /><br />
</div>
<div>
<label style="float: left; margin-left: 15px;">Remarks</label><br></br>
<div class="form-group" style="float: left; margin-left: 15px;">
<mtt:textarea name="remarks" id="remarks" cols="15" rows="5"></mtt:textarea>
<div class="col-md-8 input-group input-group-md"></div>
</div>
</div>	
</div>
<input type="hidden" id="check" name="check"  />

</div>
</div>
</div>
</div>
</mtt:form>
</body>
</html>