<%@ taglib prefix="scf" uri="/struts-tags"%>
<jsp:include page="common/header.jsp" />
<script type="text/javascript">
$(document).ready(function(){
	$('.datepicker .datepicker-inline').css('display','none');
	$('input[type="submit"],a,input[type="button"]').click(function(){
		$('body').modal({
			   show: 'false'
			 });
		$('body').removeClass('removePageLoad');
		$('body').addClass('addPageLoad');
	});
});
</script>
<form method="post" id="buyerCust" name="form">
<div class="row">
<div class="col-md-2">
<div class="side_nav" style="width: 200px;">
<ul class="nav nav-pills nav-stacked">
<!-- <li style="text-align: center;"><a href="home">Close</a></li> -->
</ul>
</div>
<br />
<!-- 	<div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li><a href="#">Ok</a></li>
<li><a href="home">Cancel</a></li>
</ul>
</div> -->
<br />
</div>
<div class="col-md-10 content_box">
<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h5>
<div class="row page_content">
<div class="col-md-12">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Branch
							Filters</h5>
</div>
<div class="form-group">
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Source banking business</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield cssClass="form-control text_box" 
										name="makervo.sbb" readonly="true"></scf:textfield>
</div>
</div>
 
						</div>
<%-- <div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Code</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="buyervo.branchCode"
										cssClass="form-control text_box"></scf:textfield>
</div>
</div>
 
						</div> --%>
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Number</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="makervo.branchNumber"
										cssClass="form-control text_box"></scf:textfield>
</div>
</div>
 
						</div>
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Name</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="makervo.branchName"
										cssClass="form-control text_box"></scf:textfield>
</div>
</div>
 
						</div>
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Country</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="makervo.branchCountry"
										cssClass="form-control text_box"></scf:textfield>
</div>
</div>
 
						</div>
<%-- <div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">City</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="buyervo.branchCity"
										cssClass="form-control text_box"></scf:textfield>
</div>
</div>
 
						</div> --%>
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Entity Type</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="makervo.brachEntityType"
										cssClass="form-control text_box"></scf:textfield>
</div>
</div>
 
						</div>
 
						<div class="col-md-11" align="right">
<a href="#" onClick="buyerbranchrefreshList()" class="button-new">Refresh...</a>
</div>
<div style="clear: both; height: 5px;">
<!--  -->
</div>
<div>
<div id="selectrow" style="display: none; color: #FF0000;">Please
								select row</div>
<table width="100%" class="embedded"
								style="BORDER-LEFT: 1px solid #d9d9d9; BORDER-TOP: 1px solid #d9d9d9;"
								frame="box" rules="none">
<colgroup>
<col width="24.5%">
<col width="20%">
<col width="20%">
<col width="20%">
<col width="20%">
</colgroup>
<tbody>
<tr class="custom-size">
<th style="text-align: left;">Branch</th>
<th style="text-align: left;">City</th>
<th style="text-align: left;">Country</th>
<th style="text-align: left;">Branch Number</th>
<th style="text-align: left;">Entity Type</th>
</tr>
</tbody>
</table>
<div style="min-height: 20px; max-height: 220px; overflow: auto">
<table width="100%" class="embedded"
									style="BORDER-LEFT: 1px solid #d9d9d9; BORDER-TOP: 1px solid #d9d9d9;"
									frame="box" id="refreshlist">
<colgroup>
<col width="25%">
<col width="20%">
<col width="20%">
<col width="20%">
<col width="20%">
</colgroup>
<tbody>
<scf:iterator value="branchList" id="branchList" status="status">
<tr class="eventrefresh"
												id='eventlist_<scf:property value="%{#status.count}"/>'
												onClick="selectlist1(this)" ondblclick="checkerbuyerBranchList(this);">
<td style="padding-left:3px;"><scf:property value="branchName" /></td>
<td style="padding-left:3px;"><scf:property value="branchCity" /></td>
<td style="padding-left:3px;"><scf:property value="branchCountry" /></td>
<td style="padding-left:3px;"><scf:property value="branchCode" /></td>
<td style="padding-left:3px;"><scf:property value="brachEntityType" /></td>
<scf:hidden id='%{"name_" + #status.count}'
													cssClass="keyvalue" name="keyvalue" value="%{branchCode}" />
<scf:hidden id='%{"keyvalue_" + #status.count}'
													cssClass="keyvalue" name="keyvalue" value="%{branchCity}" />
 
											</tr>
</scf:iterator>
</tbody>
</table>
</div>
</div>
</div>
</div>
<div>
<scf:hidden name="makervo.accountOfficer" id="fname" />
<scf:hidden name="checkervo.accountOfficer" id="fname1" />
<scf:hidden name="makervo.product" />
<scf:hidden name="makervo.ref_no" />
<scf:hidden name="makervo.receivedDate" />
<scf:hidden id="prgidentifieer" name="makervo.prgrmID" /> 

<scf:hidden name="buyervo.buyer" id="bname" />
<scf:hidden name='buyervo.customer' />
<scf:hidden name="buyervo.key" />
<scf:hidden name="buyervo.prgIdentifier" />
<scf:hidden name="buyervo.status" />
<scf:hidden name="buyervo.state" />
<scf:hidden name="buyervo.city" />
<scf:hidden name="buyervo.flag" />
<scf:hidden name="buyervo.mode" />
<scf:hidden name="buyervo.newkeyvalue" />
<scf:hidden name="buyervo.Officer" />
<scf:hidden name="buyervo.fullName" />
<scf:hidden name="buyervo.customerType" />
<scf:hidden name="buyervo.invoiceConLimit" />
<scf:hidden name="buyervo.availAmount" />
<scf:hidden name="buyervo.country" />
<scf:hidden name="buyervo.salutation" />
<scf:hidden name="buyervo.address" />
<scf:hidden name="buyervo.zip" />
<scf:hidden name="buyervo.phone" />
<scf:hidden name="buyervo.fax" />
<scf:hidden name="buyervo.email" />
<scf:hidden name="buyervo.swiftBIC" />
<scf:hidden name="buyervo.language" />
<scf:hidden name="buyervo.swiftAddr" />
<scf:hidden name="buyervo.telex" />
<scf:hidden name="buyervo.answerBack" />
<scf:hidden name="buyervo.transferMethod" />
<scf:hidden name="buyervo.transSwiftMsg" />
<div class="col-md-12">
<div class="row-page-content">
<div class="col-md-5" align="right">
<a href="#" onClick="checkerbuyerBranchList(this)" class="button-new">Ok</a>
</div>
<div class="col-md-5" align="left">
<a href="#" onClick="checkerbranchListCancel()" class="button-new">Cancel</a>
</div>
<div class="col-md-5" align="left"></div>
<div class="col-md-5" align="left"></div>
</div>
</div>
</div>
</div>
</div>
</form>
<jsp:include page="common/footer.jsp" />