<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Fail Page</title>
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
<script>	
 
		function windowclose() {	
			top.close();	
		}
</script>
</head>
 
<body class="body_bg" onload="display_ct()">
<%-- <jsp:include page="includes/TITLE.jsp" />   --%>
<img src="images/FTI-UBI.png" width="100%" />
<div class="row">
 
		<div class="col-md-2">
<div class="side_nav"  style="width: 215px; ">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="home">Close</a></li>					
</ul>
</div>
<div class="side_nav">
</div>
</div>
<div class="col-md-10 content_box">
<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
<div class="col-md-12">
<h3 class="row fontcol">Unauthorized: Access is denied </h3>
</div>
</div>
</div>
</div>
<div id="footer">
<%@ include file="/view/includes/FOOTER.jsp"%>
</div>
</body>
</html>