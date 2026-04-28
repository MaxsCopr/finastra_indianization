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
            
             $("#Search").click(function(){
                  
                  var frmDateEntered = $('#frmfinished').val();
                  var toPayRefEntered = $('#tofinished').val();
                  
                  if(frmDateEntered == '' && toPayRefEntered == '' ){
                        alert('Please Enter From Date & TO Date');
                } else {
                  $("#form1").attr("action","fetchTIData");
                        $("#form1").submit();
                  }
                   //alert("Inside Search");
                  
            });         
            
            $("#reset").click(function(){
                   $("#form1").attr("action","enquiryProcess");
                   $("#form1").submit();
            });
      });
      </script>


<!--[if IE 7]>
   <link rel="stylesheet" type="text/css" href="css/styleie7.css" />
 <![endif]-->
</head>

<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  

  <mtt:form method="post" id="form1" name="form">
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav">
                        <ul class="nav nav-pills nav-stacked">
                              <!-- <li style="text-align: center;"><a href="https://tradeuatapp106:8003/devglobal">Close</a></li> -->
                              <!-- <li style="text-align: center;"><a href="​https://tiplusdr.idfcbank.com/Dr-global">Close</a></li> -->
                              <%-- <li style="text-align: center;"><a href="https://tradeuatapp107:8003/uatglobal"><mtt:text name="Close" /></a></li> --%>
                              <%-- <li style="text-align: center;"><a href="https://encore:9448/tiplus2-global"><mtt:text name="Close" /></a></li> --%>
                              <li style="text-align: center;"><a href="home">Close</a></li>
                              <!-- <li style="text-align: center;"><a href="https://tipluspreprod.idfcbank.com/preprod-global">Close</a></li> -->
                        </ul>
                  </div>
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="reset">Reset</a></li>
                              </ul>
                  </div>
                  <br />
                  
            </div>
                  
            <div class="col-md-10 content_box">

            <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MTT Enquiry Query Search</h5>
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
                                                            style="font-weight: normal;">Master Reference No</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <mtt:textfield id="masRefNo" name="mttVO.masRefNo"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">MTT Number</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <mtt:textfield id="mttNumber" name="mttVO.mttNumber"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">From Date</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <mtt:textfield id="frmfinished" name="mttVO.frmfinished"
                                                                  cssClass="form-control text_box"  placeholder="DD/MM/YYYY"/>
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">MTT Status</label>       
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <select  id = "mttStatus" name ="mttVO.mttStatus">
                                                                    <option value="B"><--------></option>
                                                                    <option value="OUTSTANDING" >OUTSTANDING</option>
                                                                    <option value="COMPLETED" >COMPLETED</option>
                                                                  </select>
                                                                  
                                                            </div>
                                                </div>
                                                

                                          </div>
                                          
                                          <div class="col-md-6">                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Event Reference No</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <mtt:textfield id="eventRefNo" name="mttVO.eventRefNo"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Customer</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <mtt:textfield id="customer" name="mttVO.customer"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">To Date</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <mtt:textfield id="tofinished" name="mttVO.tofinished"
                                                                  cssClass="form-control text_box"  placeholder="DD/MM/YYYY"/>
                                                      </div>
                                                </div>
                                                      
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <input type="button" value="Search" class="button" id="Search" style="margin-top:5px;"/>
                                                      </div>
                                                </div>

                                          </div>

                                    </div>
                              
                              <div class="col-md-12">
                                    
                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px;">&nbsp;TIPLUS Details</h5>
                                          </div>
                                          <div class="form-group">
                                                <div class="table">
                                                      <table border="1px" align="left">
                                                               <tbody>
                                                                   <tr>
                                                                      <th style="text-align: left; width:300px; padding:4px 5px;"><label >Master Reference Number</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Event Reference Number</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >MTT Number</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Customer Code</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Import_Trans_Amount</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Export_Trans_Amount</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >MTT Status</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Finished Date</label></th>
                                                                        
                                                                   </tr>
                                                                      
                                                                   <c:if test="${empty tiList}">
                                                                    <tr>
                                                                  <td colspan="8">No records found</td>
                                                                   </tr>
                                                                   </c:if>
                                                                  <c:if test="${not empty tiList}">
                                                                  <mtt:iterator value="tiList" id="tiList">
                                                                  <tr>
                                                                      <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <mtt:property value="mttListMasterRef"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="listEventeRefNo"/>                                                                                 
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttListNumber"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <mtt:property value="mttListcustomer"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttListTransAmt"/>                                                                                 
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttListOtstAmt"/>                                                                                  
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <!-- Added By mehul Sharma -->
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttListStatus"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <mtt:property value="mttListfinished"/>                                                                                 
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
                                    
                              </div>
                              
                              </div>
                        </div>
                  </div>
            </div>
      </mtt:form>
</body>
</html>