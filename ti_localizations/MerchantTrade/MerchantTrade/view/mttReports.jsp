<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="mtt" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>MTT Staging</title>
 
<!-- <link rel="stylesheet" href="css/jquery-ui.css" />
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
<script type="text/javascript" src="js/commonTiplus.js"></script> -->
 
 
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<!-- <link href="css/datepicker.css" rel="stylesheet"></link> -->
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link href="css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="css/commonTiplus.css" />
<script  type="text/javascript" src="js/jquery-1.9.1.js"></script>
<script  type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
<script  type="text/javascript" src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>
<!-- <script src="js/bootstrap-datepicker.js" type="text/javascript"></script> -->
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
 
 
<script type="text/javascript">
	function fileDownload(){
		var FileName = $.trim($('#fileUtilFileName').val());
		var frmDate = $.trim($('#fileUtilFromDate').val());
		var toDate = $.trim($('#fileUtilToDate').val());
		//alert("idpmsFileName"+idpmsFileName);
		if(frmDate == ''){
	    	alert("Please Enter From Date");
	    	return false;
		}
		if(toDate==''){
	    	alert("Please Enter To Date");
	    	return false;
		}
		$("#fileUtilFileName").val(FileName);	
		$("#fileUtilFromDate").val(frmDate);	
		$("#fileUtilToDate").val(toDate);
		if(FileName == "MTTANNEXII"){
			$("#form1").attr("action","mttAnnexIIDownload");
			$("#form1").submit();
		}
		if(FileName == "MTTANNEXIII"){
			$("#form1").attr("action","mttAnnexIIIDownload");
			$("#form1").submit();
		}
		if(FileName == "MTTRBI"){
			$("#form1").attr("action","mttRBIDownload");
			$("#form1").submit();
		}
	}

	$(document).ready(function() {
		$("#fileUtilFromDate").datepicker({
			changeMonth: true,
			changeYear: true,
			dateFormat: 'dd/mm/yy'
		});
		$("#fileUtilToDate").datepicker({
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
</script>
 
 
<!--[if IE 7]>
<link rel="stylesheet" type="text/css" href="css/styleie7.css" />
<![endif]-->
</head>
 
<body class="body_bg" onload="display_ct()">
<%-- <%@ include file="/view/includes/TITLE.jsp" %>   --%>
<img src="images/FTI-UBI.png" width="100%" />
<mtt:form method="post" id="form1" name="form">
<div class="row">
<div class="col-md-2">
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="home">Close</a></li>
</ul>
</div>
<br />
</div>
<div class="col-md-10 content_box">
 
		<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MTT Reports</h5>
<div id="userIdMessage" style="color: orange"></div>
<div class="row page_content">
<div class="col-md-12">
<div class="col-md-5">
<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal;">File Name</label>
<div class="col-md-4 input-group input-group-md">
<select  id = "fileUtilFileName" name ="fileUtilFileName">
<option value="N">-------</option>
<!-- <option value="MTTANNEXII">MTT ANNEXURE-II REPORT</option>
<option value="MTTANNEXIII">MTT ANNEXURE-III REPORT</option> -->
<option value="MTTRBI">MTT RBI REPORT</option>
</select>
</div>
</div>
</div>
<div class="col-md-5">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">MTT Number</label>		
<div class="col-md-4 input-group input-group-md">	
<mtt:textfield id="mttNumber" name="mttNumber"
											Autocomplete="off">
</mtt:textfield> 	
</div>
</div>
</div>	 
</div>
<div class="col-md-12">
<div class="col-md-5">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Customer ID</label>		
<div class="col-md-4 input-group input-group-md">	
<mtt:textfield id="custId" name="custId"
											Autocomplete="off">
</mtt:textfield> 	
</div>
</div>
</div>	 
<div class="col-md-5">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Branch Code</label>		
<div class="col-md-4 input-group input-group-md">										
<mtt:textfield id="brnCode" name="brnCode"
											Autocomplete="off">
</mtt:textfield>	
</div>
</div>
</div>	 
</div>
<div class="col-md-12">
<div class="col-md-5">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">From_Date</label>		
<div class="col-md-4 input-group input-group-md">	
<mtt:textfield id="fileUtilFromDate" name="fileUtilFromDate"
											cssClass="datepicker form-control text_box" placeholder="dd/mm/yyyy"
											Autocomplete="off">
</mtt:textfield> 	
</div>
</div>
</div>	 
<div class="col-md-5">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">To_Date</label>		
<div class="col-md-4 input-group input-group-md">										
<mtt:textfield id="fileUtilToDate" name="fileUtilToDate"
											cssClass="datepicker form-control text_box" placeholder="dd/mm/yyyy" 
											Autocomplete="off">
</mtt:textfield>	
</div>
</div>
</div>	 
</div>
<div class="col-md-12">
<div class="col-md-4">&nbsp;</div>
<div class="col-md-3">						
<div class="form-group">
<input type="button" value="Download File" class="button" id="Download" style="background: url('../img/but.gif'); height: 25px; width: 100px;" onclick="fileDownload()" />
</div>
</div>
<div class="col-md-3">&nbsp;</div>
</div>
<div class="col-md-12">
<div class="form-group">
<div align="center">
<mtt:if test="hasActionErrors()">
<div class="errors"><mtt:actionerror /></div>
</mtt:if>
<mtt:if test="hasActionMessages()">
<div class="welcome"><mtt:actionmessage /></div>
</mtt:if>
</div>
<br />
</div>
</div>
</div>
</div>
</div>
</mtt:form>
</body>
</html>