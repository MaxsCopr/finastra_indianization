<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Home Page</title>
<jsp:include page="/view/common/header.jsp" />
<script>	
 
		function windowclose() {	
			top.close();	
		}
</script>
</head>
 
 
<body class="body_bg">
<jsp:include page="includes/TITLE.jsp" />  
<div class="row">
<div class="col-md-2">
<div class="side_nav"  style="width: 215px; ">
<ul class="nav nav-pills nav-stacked">					
<li style="text-align: center;"><a href="closeBOEWindow">Close</a></li>
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
</div></div>
</div></div>
</body>
</html>