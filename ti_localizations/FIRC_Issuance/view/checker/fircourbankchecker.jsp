<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="firc" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- <meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="Expires" content="-1" /> -->
<title>FIRC Our Bank Page</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="js/checker/fircCheckerOurBank.js"></script>
<!--[if IE 7]>
   <link rel="stylesheet" type="text/css" href="css/styleie7.css" />
<![endif]-->
</head>


<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  

  <firc:form method="post" id="myForm" name="myForm">
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav"  style="width: 200px; ">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="checkerProcess">Close</a></li>
                        </ul>
                  </div>
                  <br />
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="approve">Approve</a></li>
                                    <li style="text-align: center;"><a href="#" id="reject">Reject</a></li>
                                    <li style="text-align: center;"><a href="resetAction" id="reset">Reset</a></li>
                              </ul>
                  </div>
                  <br />
                  
            </div>
                  
            <div class="col-md-10 content_box">

            <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FIRC Our Bank Details</h5>
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
                                                            style="font-weight: normal;">Transaction Reference No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="transRefNo" name="issuanceVO.transrefno"
                                                                  cssClass="form-control text_box"  />
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
                                          
                                          
                                          
                                          <div class="col-md-6">                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">FIRC Serial No</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="serialNo" name="issuanceVO.fircno"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>                                          

                                          </div>

                                    </div>
                              
                              <div class="col-md-12">
                                    <div class="row page_content">
                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px;">&nbsp;FIRC Our Bank Approval</h5>
                                          </div>
                                          <div class="form-group">
                                                <div class="checker_table" style="float:left;">
                                                      <table border="1px" align="left">
                                                               <tbody>
                                                                   <tr>
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Issuing Bank</label></th>

                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Issuing Bank Branch</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Master Reference No</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >FIRC Serial No</label></th>                                                                                                                              
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Beneficiary Address</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Purpose Code</label></th>

                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Order Customer</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Amount</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Currency</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >EX Rate</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label >Available Amount</label></th>

                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Paid Currency</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Paid Amount</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >IRM Type</label></th>
                                                                        
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Remarks</label></th>
                                                                                                            
                                                                        <th style="text-align: left; width:100px; padding:4px 5px;"><label >Action</label></th>
                                                                      </tr>
                                                                      
                                                                      <c:if test="${empty issuanceList}">
                                                                    <tr>
                                                                  <td colspan="16">No records found</td>
                                                                   </tr>
                                                                   </c:if>
                                                                  <c:if test="${not empty issuanceList}">
                                                                  <firc:iterator value="issuanceList" id="issuanceList">
                                                                  <tr>
                                                                      <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <firc:property value="issuingBank"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="issunibranch"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="transrefno"/>                                                                               
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="fircno"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>                               
                                                                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                                    <div class="form-group" align="left">
                                                                                          <firc:property value="benificiarydetails"/>     
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>

                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="purposecode"/>                                                                              
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="orderingcustomer"/>                                                                               
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
                                                                              <firc:property value="currency"/>                                                                           
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                                    <div class="form-group" align="left">
                                                                                          <firc:property value="exchange_rate"/>    
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                        </td>                               
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                          <firc:property value="available_amt"/>    
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>                               
                                                                  
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="paidcurrency"/>                                                                             
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="paidamount"/>                                                                               
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="inwardType"/>                                                                               
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="remarks"/>                                                                                  
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                      <td style="text-align: center; padding:4px 5px;">
                                                                              <div class="form-group">
                                                                                    <firc:checkbox name="chkList"
                                                                                          fieldValue="%{#issuanceList.fircno}"
                                                                                          disabled="%{#issuanceList.checkBoxDisabled}">
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
                                                                  <firc:textarea name="remarks" id="remarks" cols="18" rows="5"></firc:textarea>
                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                            </div>                                                      
                                            </div>
                                          </div>
                                          
                                          <input type="hidden" id="check" name="check"  />
                                    </div>
                              </div>
                              
                              </div>
                        </div>
                  </div>
            </div>
      </firc:form>
</body>
</html>