<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>
 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Home Page</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
 
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/datepicker.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link href="css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="css/commonTiplus.css" />
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
 
<script type="text/javascript" src="js/date_search.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/forwardcontract.js"></script>
 
 
<script type="text/javascript">
	$(document).ready(function() {
 
		//syntax highlighter
		hljs.tabReplace = '    ';
		hljs.initHighlightingOnLoad();
 
		//collapsible management
		$('.collapsible').collapsible({
			defaultOpen : 'section1',
			cookieName : 'nav'
		});
		$('.page_collapsible').collapsible({
			defaultOpen : 'body-section1',
			cookieName : 'body'
		});
 
	});
</script>
 
<script type="text/javascript">
	function display_c() {
		var refresh = 1000; // Refresh rate in milli seconds
		mytime = setTimeout('display_ct()', refresh);
	}
 
	function display_ct() {
		var strcount
		var x = new Date();
		document.getElementById('ct').innerHTML = x;
		tt = display_c();
	}
 
	$("#submit").click(function() {
		$("#myForm").submit();
	});
</script>
 
</head>
 
<body class="body_bg" onload="display_ct()">
<%-- <jsp:include page="includes/TITLE.jsp" />   --%>
<img src="images/FTI-UBI.png" width="100%" />
<div class="row">
 
		<div class="col-md-2">
<div class="side_nav" style="width: 215px;">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;">
<!-- <a href="https://10.128.231.197:8001/tiplus2-global">Close</a> UAT  -->
<!-- <a href="https://172.27.5.25:443/tiplus2-global">Close</a> PROD  -->
<a href="closeURL">Close</a>
</li>
</ul>
</div>
<br />
</div>
<div class="col-md-10 content_box">
<h5 class="row fontcol" style="color: #527BB8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Forward
				Contract</h5>
<div id="userIdMessage"></div>
<div class="row cont_colaps">
<div class="col-md-12">
<%-- <gr:if test="%{#session.count == 0 || #session.count == 2 }"> --%>
<div class="btn btn-link" id="GR_Process">
<a href="makerProcess">Forward Contract Maker Process</a>
</div>
<br />
<%-- </gr:if> --%>
<%-- <gr:if test="%{#session.count == 1 || #session.count == 2 }"> --%>
<div class="btn btn-link" id="GR_Process">
<a href="checkerProcess">Forward Contract Checker Process</a>
</div>
<br />
<%-- </gr:if> --%>
<div class="btn btn-link" id="GR_Process">
<a href="enquiryProcess">Forward Contract Enquiry Process</a>
</div>
<br />
 
				</div>
</div>
</div>
</div>
<div id="footer">
<%@ include file="/view/includes/FOOTER.jsp"%>
</div>
</body>
</html>