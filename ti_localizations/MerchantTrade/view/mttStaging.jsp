<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="mtt" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>MTT Staging</title>
 
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
		 $("#insrt").click(function(){
			 //alert("Inside Search");
			 $("#form1").attr("action","processData");
			  $("#form1").submit();
		}); 		 
		/* $("#reset").click(function(){
			 $("#form1").attr("action","home");
			 $("#form1").submit();
		});  */
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
 
		<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MTT Process Data to Staging</h5>
<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
<div class="col-md-12">
<div class="row page_content">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Master Ref.</label>
<div class="col-md-3 input-group input-group-md">
<mtt:textfield id="stgMasRef" name="mttVO.stgMasRef"
										cssClass="form-control text_box"  />
</div>
</div>
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Event Ref.</label>
<div class="col-md-3 input-group input-group-md">
<mtt:textfield id="stgEveRef" name="mttVO.stgEveRef"
										cssClass="form-control text_box" />
</div>
</div>
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">MTT Number</label>
<div class="col-md-3 input-group input-group-md">
<mtt:textfield id="stgMttNumber" name="mttVO.stgMttNumber"
										cssClass="form-control text_box" />
</div>
</div>
</div>
<div class="row page_content">
<div class="col-md-6">						
<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal;"></label>
<div class="col-md-10 input-group input-group-md">
<input type="button" value="Insert Current Data To Staging" class="button" id="insrt" style="margin-top:5px;"/>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</mtt:form>
</body>
</html>