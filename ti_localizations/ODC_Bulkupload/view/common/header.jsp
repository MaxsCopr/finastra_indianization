<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="scf" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>ODC  Bulkupload</title>
 
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link href="css/datepicker.css" rel="stylesheet"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" /><link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" /><link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" /><link />
<link href="css/font-awesome.css" rel="stylesheet"></link>
<link rel="stylesheet" href="css/datepicker.css"></link>
<link rel="stylesheet" href="css/commonTiplus.css"></link>
 
<!-- <script src="js/jquery-3.5.0.js"></script> -->
<script src="js/jquery-1.9.1.js" type="text/javascript"></script>
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
 
<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>
 
 
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="js/date_search.js"></script>
<script type="text/javascript" src="js/Theme.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<!-- <script type="text/javascript" src="js/customhome.js"></script> -->
 
<style>
.welcome {
	color:#009edc;
	width:500px;
	height:20px;
}
.welcome li{ 
	list-style: none; 
}
 
 
.errors {
	background-color:#FFCCCC;
	border:1px solid #CC0000;
	width:500px;
	height:20px;
}
.errors li{ 
	list-style: none; 
}
</style>
 
 
<!--[if IE 7]>
<link rel="stylesheet" type="text/css" href="css/styleie7.css" />
<![endif]-->
</head>
 
<body class="body_bg" onload="display_ct()">
<div class="row header_bg" style="width: 102%;     height: 45px;  margin: 0px:padding:none;">
<div class="col-md-12">
<h3 class="col-md-12 head_text">					
<!--  <b> <img src="images/idfc-tiplus_24.png" alt="" style="WIDTH: 94px;height: 26px;margin-left: -15px;margin-top: -11px;" /></b> --> 
<b><img src="images/Logo_Misys.png" alt="" style="margin-left: 1195px;margin-top: 20px;width: 95px;" /></b>
<!-- &lt;b&gt;f&lt;/b&gt; -->	 
</h3>
</div>
</div>
 
		<div class="row" >
<div class="col-md-4">
<span id="ct" class="col-md-12" style="margin-top: 2px;width: 141%; color: #808080;">System Date: &nbsp;<scf:property value="%{#session.processDate}" /></span>
</div>
</div>