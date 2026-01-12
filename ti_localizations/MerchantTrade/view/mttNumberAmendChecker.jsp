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
                   $("#form1").attr("action","fetchMttNumberCheckerData");
                   $("#form1").submit();
            });   
            
            $("#reset").click(function(){
                   $("#form1").attr("action","mttNumberCheckerProcess");
                   $("#form1").submit();
            });
            
            $("#ok").click(function() {
                  $("#check").val("Approve");
                  var remark = $.trim($('#remarks').val());
                  if(remark.length == 0){
                        alert("Remarks for Add must be filled...");
                        return false;
                  }
                  $('#form1').attr('action','approveMttNumberCheckerData');
                  $('#form1').submit();

            });
            
            $("#reject").click(function() {
                  $("#check").val("Reject");
                  var remark = $.trim($('#remarks').val());
                  if(remark.length == 0){
                        alert("Remarks for Add must be filled...");
                        return false;
                  }           
                  $('#form1').attr('action','rejectMttNumberCheckerData');
                  $('#form1').submit();
            });
            
            $("#ok1").click(function() {
                  $("#check1").val("Approve");
                  var remark1 = $.trim($('#remarks1').val());
                  if(remark1.length == 0){
                        alert("Remarks for Add must be filled...");
                        return false;
                  }
                  $('#form1').attr('action','approveMttNumberAddCheckerData');
                  $('#form1').submit();

            });
            
            $("#reject1").click(function() {
                  $("#check1").val("Reject");
                  var remark1 = $.trim($('#remarks1').val());
                  if(remark1.length == 0){
                        alert("Remarks for Add must be filled...");
                        return false;
                  }           
                  $('#form1').attr('action','rejectMttNumberAddCheckerData');
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
                  <!-- <div class="side_nav">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="#" id="ok">Approve</a></li>
                        </ul>
                  </div>
                  <div class="side_nav">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="#" id="reject">Reject</a></li>
                        </ul>
                  </div> -->
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
                                                
                                                
      
                                    </div>
                               </div>
                               <div class="col-md-12">
                                    
                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px;">&nbsp;MTT Amend Deleted Pending List</h5>
                                          </div>
                                          <div class="form-group">
                                          <div class="col-md-9">
                                                <div class="table"  style="float:left;">
                                                      <table border="1px" align="left">
                                                               <tbody>
                                                                   <tr>
                                                                      <th style="text-align: left; width:300px; padding:4px 5px;"><label >MTT Number</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Master Reference</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Event Reference</label></th>
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
                                                                                    <mtt:property value="mttNumberListMttNumber"/>                                                                                
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <mtt:property value="mttNumberListMasterReference"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <mtt:property value="mttNumberListEventRef"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttNumberListMakerUserId"/>                                                                                    
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttNumberListMakertmstmp"/>                                                                                    
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="text-align: center;">
                                                                              <div class="form-group">
                                                                                    <mtt:checkbox name="chkList"
                                                                                          fieldValue="%{#pendingtiList.mttNumberListKeyId+':'
                                                                                                                  +#pendingtiList.mttNumberListEventKeyId+':'
                                                                                                                  +#pendingtiList.mttNumberListMttNumber+':'
                                                                                                                  +#pendingtiList.mttNumberListMasterReference+':'
                                                                                                                  +#pendingtiList.mttNumberListEventRef}">
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
                                                </div>
                                                <div class="col-md-3">
                                                      <div class="col-md-8">
                                                            <label style="float: left; margin-left: 15px;">Remarks</label><br></br>
                                                            <div class="form-group" style="float: left; margin-left: 15px;">
                                                                  <mtt:textarea name="remarks" id="remarks" cols="15" rows="5"></mtt:textarea>
                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                            </div>
                                                      </div>
                                                      <div class="col-md-4">
                                                      <input type="button" value="Approve" class="button" id="ok" style="margin-top:5px;"/>
                                                      
                                                      <input type="button" value="Reject" class="button" id="reject" style="margin-top:5px;"/>
                                                      </div>
                                                </div>
                                                
                                          </div>
                                          
                                    
                              
                              
                              </div>
                              
                              <div class="col-md-12">
                                    
                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px;">&nbsp;MTT Master Reference Amend Pending List</h5>
                                          </div>
                                          <div class="form-group">
                                                <div class="col-md-9">
                                                <div class="table"  style="float:left;">
                                                      <table border="1px" align="left">
                                                               <tbody>
                                                                   <tr>
                                                                      <th style="text-align: left; width:300px; padding:4px 5px;"><label >MTT Number</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Master Reference</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Event Reference</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Maker User</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Maker Timestamp</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Select</label></th>
                                                                   </tr>
                                                                      
                                                                   <c:if test="${empty pendingAddtiList}">
                                                                    <tr>
                                                                  <td colspan="3">No records found</td>
                                                                   </tr>
                                                                   </c:if>
                                                                  <c:if test="${not empty pendingAddtiList}">
                                                                  <mtt:iterator value="pendingAddtiList" id="pendingAddtiList">
                                                                  <tr>
                                                                      
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttNumberListAddMttNumber"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <mtt:property value="mttNumberListAddMasterReference"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <mtt:property value="mttNumberListAddEventRef"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttNumberListAddMakerUserId"/>                                                                                 
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttNumberListAddMakertmstmp"/>                                                                                 
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="text-align: center;">
                                                                              <div class="form-group">
                                                                                    <mtt:checkbox name="chkList1"
                                                                                          fieldValue="%{#pendingAddtiList.mttNumberListAddKeyId+':'
                                                                                                                  +#pendingAddtiList.mttNumberListAddMttNumber+':'
                                                                                                                  +#pendingAddtiList.mttNumberListAddMasterReference+':'
                                                                                                                  +#pendingAddtiList.mttNumberListAddEventRef}">
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
                                                </div>
                                                <div class="col-md-3">
                                                      <div class="col-md-8">
                                                            <label style="float: left; margin-left: 15px;">Remarks</label><br></br>
                                                            <div class="form-group" style="float: left; margin-left: 15px;">
                                                                  <mtt:textarea name="remarks1" id="remarks1" cols="15" rows="5"></mtt:textarea>
                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                            </div>
                                                      </div>
                                                      <div class="col-md-4">
                                                      <input type="button" value="Approve" class="button" id="ok1" style="margin-top:5px;"/>
                                                      
                                                      <input type="button" value="Reject" class="button" id="reject1" style="margin-top:5px;"/>
                                                      </div>
                                                </div>      
                                          </div>
                                    <input type="hidden" id="check" name="check"  />
                                    <input type="hidden" id="check1" name="check1"  />
                              
                              
                              </div>
                        </div>
                  </div>
            </div>
      </mtt:form>
</body>
</html>