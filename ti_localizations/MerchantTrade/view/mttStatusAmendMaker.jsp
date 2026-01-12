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
function fetchDataForMTT() {
      var mttNum   = $('#mttNumber').val();
      
    if(mttNum == ''){
      
      alert("Please Enter MTT Number");
      return false;
      }else{
      $('#form1').attr('action', 'fetchStatusForMTT');
      $('#form1').submit();
      }
}
      
      $(document).ready(function() {
            
            $("#chngeSts").click(function(){
                   $("#form1").attr("action","changeStatus");
                   $("#form1").submit();
            });   
            
            $("#reset").click(function(){
                   $("#form1").attr("action","mttstatusMakerProcess");
                   $("#form1").submit();
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
                              <!-- <li style="text-align: center;"><a href="https://tradeuatapp106:8003/devglobal">Close</a></li> -->
                              <!-- <li style="text-align: center;"><a href="​https://tiplusdr.idfcbank.com/Dr-global">Close</a></li> -->
                              <%-- <li style="text-align: center;"><a href="https://tradeuatapp107:8003/uatglobal"><mtt:text name="Close" /></a></li> --%>
                              <%-- <li style="text-align: center;"><a href="https://encore:9448/tiplus2-global"><mtt:text name="Close" /></a></li> --%>
                              <li style="text-align: center;"><a href="makerProcess">Close</a></li>
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

            <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MTT Status Amend</h5>
                  <div id="userIdMessage" style="color: orange"></div>
                        <%-- <div class="row page_content">

                              <div class="col-md-12">
                                    <div class="form-group">

                                          <div align="center" id="div1">
                                                <mtt:if test="hasActionErrors()">
                                                      <div style="font-weight: bold; font-size: 13px; color:red">
                                                            <mtt:actionerror />
                                                      </div>
                                                </mtt:if>
                                                
                                          </div>

                                    </div>
                              </div>
                        </div> --%>
                  
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
                                                            <mtt:textfield id="mttStatusNumber" name="mttVO.mttStatusNumber"
                                                                  cssClass="form-control text_box" onChange="fetchDataForMTT()"/>
                                                      </div>
                                                      
                                                </div>
                                                <div class="form-group">
                                                      
                                                      <div class="col-md-4 input-group input-group-md" >
                                                                  <mtt:if test="hasActionErrors()">
                                                                              <mtt:actionerror cssStyle="font-weight: bold; font-size: 13px; color:red" />
                                                                  </mtt:if>
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                      style="font-weight: normal;">New Status</label>       
                                                                                                      
                                                      <div class="col-md-3 input-group input-group-md">                                                           
                                                            <mtt:textfield id="mttNewStatus" name="mttVO.mttNewStatus"
                                                                  cssClass="form-control text_box"  disabled="true"/>
                                                      </div>
                                                                  <%-- <select  id = "mttNewStatus" name ="mttVO.mttNewStatus" disabled="disabled">
                                                                        <c:if test ="${mttVO.mttCurStatus == 'Closed'}">
                                                                        <option value="N" selected="selected">Active</option>
                                                                        </c:if>
                                                                        <c:if test ="${mttVO.mttCurStatus == 'Active'}">
                                                                        <option value="Y">Closed</option>
                                                                        </c:if>
                                                                   </select> --%>
                                                                  
                                                      
                                                </div>      
                                          </div>
                                          <div class="col-md-6">        
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">MTT Current Status</label>           
                                                      <div class="col-md-3 input-group input-group-md">                                                           
                                                            <mtt:textfield id="mttCurStatus" name="mttVO.mttCurStatus"
                                                                  cssClass="form-control text_box"  disabled="true"/>
                                                      </div>
                                                </div>      
                                                <c:if test ="${mttVO.isButtonVisible == 'true'}">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                                  <input type="button" value="Change Status" class="button" id="chngeSts" style="margin-top:5px;"/>
                                                      </div>
                                                </div>
                                                </c:if>
                                          </div>
      
                                    </div>
                                    
                              <%-- <div class="col-md-12">
                                    
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
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Trans_Amount</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >OutStanding Amount</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >MTT Status</label></th>
                                                                        <th style="text-align: left; width:300px; padding:4px 5px;"><label >Finished Date</label></th>
                                                                        
                                                                   </tr>
                                                                      
                                                                   <c:if test="${empty tiList}">
                                                                    <tr>
                                                                  <td colspan="3">No records found</td>
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
                                                                                <mtt:property value="mttListCustCode"/>
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
                                    
                              </div> --%>
                              
                              </div>
                        </div>
                        <mtt:hidden name="mttVO.mttCurStatus" id="mttCurStatus" />
                        <mtt:hidden name="mttVO.mttNewStatus" id="mttNewStatus" />
                  </div>
            </div>
      </mtt:form>
</body>
</html>