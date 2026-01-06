<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="firc" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- <meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="Expires" content="-1" /> -->
<title>IRM Closure Checker Page</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="js/checker/irmClosureChecker.js"></script>
<!--[if IE 7]>
   <link rel="stylesheet" type="text/css" href="css/styleie7.css" />
<![endif]-->
</head>

<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  

  <firc:form method="post" id="form1" name="form">
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="checkerProcess">Close</a></li>
                        </ul>
                  </div>
                  <br />
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="approve">Approve</a></li>
                                    <li style="text-align: center;"><a href="#" id="reject">Reject</a></li>
                                    <li style="text-align: center;"><a href="#" id="reset">Reset</a></li>
                              </ul>
                  </div>
                  <br />
                  
            </div>
                  
            <div class="col-md-10 content_box">

            <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IRM Adjustment/Closure Checker Screen</h5>
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
                                                            style="font-weight: normal;">Transaction Ref No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="serialNo" name="irmAdjVO.transactionrefno"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <input type="button" value="Search" class="button" id="search" style="margin-top:5px;"/>
                                                      </div>
                                                </div>

                                          </div>

                                    </div>
                              
                              <div class="col-md-12">
                                    <div class="row page_content">
                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px;">&nbsp;IRM Adjustment/Closure Checker</h5>
                                          </div>
                                          <div class="form-group">
                                                <div class="checker_table" style="float:left;">
                                                      <table border="1px" align="left" >
                                                               <tbody>
                                                                   <tr>
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Transaction Ref No</label></th>

                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >ADcode</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >IEcode</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Currency</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Amount</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Approved By</label></th>                                                          
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Letter No</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Adjustment Date</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Reason</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Document No</label></th>

                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Document Date</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Document Port</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Record Indicator</label></th>
                                                                        
                                                                        <!-- <th style="text-align: left; width:100px; padding:4px 5px;"><label >Close of Remittance Indicator</label></th> -->
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Remarks</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label>Status</label></th>                                                                
                                                                        
                                                                    </tr>
                                                                      
                                                                      <c:if test="${empty closureList}">
                                                                    <tr>
                                                                  <td colspan="15">No records found</td>
                                                                   </tr>
                                                                   </c:if>
                                                                  <c:if test="${not empty closureList}">
                                                                  <firc:iterator value="closureList" id="closureList">
                                                                  <tr>
                                                                      <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <firc:property value="transactionrefno"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="adcode"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="iecode"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="currency"/>                                                                                 
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="amount"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="approvedBy"/>                                                                               
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>                               
                                                                                                                        
                                                                  
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="letterNo"/>                                                                           
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="adjDate"/>                                                                            
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="reason"/>                                                                             
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="docNo"/>                                                                              
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="docDate"/>                                                                                  
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="docPort"/>                                                                                  
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="recordInd"/>                                                                                
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <%-- <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="closeInd"/>                                                                                 
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td> --%>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="remarks"/>                                                                            
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td> 
                                                                        
                                                                        <td style="text-align: center; padding:4px 5px;">
                                                                              <div class="form-group">
                                                                                    <firc:checkbox name="closureChkList"
                                                                                                fieldValue="%{#closureList.tempTransRefNo}"
                                                                                                disabled="%{#closureList.checkBoxDisabled}">
                                                                                    </firc:checkbox>
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>                               
                                                                  </tr>
                                                                  </firc:iterator>
                                                                  </c:if>
      
                                                                  </tbody>
                                                            </table><br /><br />
                                                </div>
                                                <div>
                                                      <label style="float: left; margin-left: 15px;">Remarks</label><br></br>
                                                      <div class="form-group" style="float: left; margin-left: 15px;">
                                                            <firc:textarea name="closureRemarks" id="remarks" cols="18" rows="5"></firc:textarea>
                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                      </div>
                                                      <firc:hidden id="check" name="closureCheck"></firc:hidden>
                                                </div>
                                          </div>
                                          
                                    </div>
                              </div>
                              
                              </div>
                        </div>
                  </div>
            </div>
      </firc:form>
</body>
</html>