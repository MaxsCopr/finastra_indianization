<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>OBB Bulk Upload</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="js/OBBBulkUpload_Maker.js"></script>
</head>
 
<body class="body_bg">
<jsp:include page="includes/TITLE.jsp" />
<boe:form method="post" action="csvAction">
<div class="row">
<div class="col-md-2">
<div class="side_nav" style="width: 215px;">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="OBBBulkUpload"><boe:text name="label.boe.close" /></a></li>
</ul>
</div>
<div class="side_nav"></div>
</div>
 
			<div class="col-md-10 content_box">
<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OBB Bulk Upload</h5>
<boe:if test="hasActionMessages()">
<div class="welcome">
<boe:actionmessage/>
</div>
</boe:if>
<boe:hidden id="csvFileGenFlagId" name="csvFileGenFlag" />
<div class="col-md-10">
<boe:if test="%{csvFileGenFlag.length() > 0}">
<div class="col-md-2 form-group">
							Batch ID&nbsp;&nbsp;
</div>
<div class="col-md-2 form-group">
<boe:textfield id="batchID" name="batchId" readonly="true" cssClass="form-control text_box" />
<br/><br/>
<boe:submit id="submit" class="button" value="Generate csv" />
</div>
</boe:if>
</div>
</div>
</div>	
</boe:form>
<jsp:include page="common/footer.jsp" />
</body>
</html>