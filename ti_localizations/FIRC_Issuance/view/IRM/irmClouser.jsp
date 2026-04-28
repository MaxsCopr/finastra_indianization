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
<title>IRM Closure Page</title>
  <jsp:include page="/view/common/header.jsp" />
  <script type="text/javascript" src="js/IRM/irmClosure.js"></script>  
</head>

<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  

  <firc:form method="post" id="myForm" name="myForm">
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="makerProcess"><firc:text name="Close" /></a></li>
                        </ul>
                  </div>
                  <br />
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li><a href="#" id = "submit"><firc:text name="OK" /></a></li>
                                    <li><a href="#" id = "validate"><firc:text name="Validate" /></a></li>
                                    <li><a href="#" id = "reset"><firc:text name="Reset" /></a></li>
                              </ul>
                  </div>
                  <br />
                  
            </div>
                  
            <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IRM Adjustment/Closure</h5>
                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;<firc:text name="Error Description" /></h5>
                                    </div>
                                    
                                    <div class="form-group">
                                          <div align="center" id="div1">
                                                <firc:if test="hasActionErrors()">
                                                             <div class="errors">
                                                             <firc:actionerror />
                                                              </div>
                                                </firc:if>
                                                <firc:if test="hasActionMessages()">
                                                            <div class="welcome">
                                                             <firc:actionmessage />
                                                            </div>
                                                </firc:if>                                                  
                                          </div><br />
                                          <div class="errorTab">
                                                <table border="1px" align="left" id="errorList">
                                                               <tbody>
                                                                   <tr>
                                                                         <th style="text-align: left; padding:2px 3px; width:200px;"><label ><firc:text name="Severity" /></label></th>

                                                                        <th style="text-align: left;  padding:2px 3px; width:200px;"><label ><firc:text name="Description" /></label></th>

                                                                        <th style="text-align: left;  padding:2px 3px; width:200px;"><label ><firc:text name="Steps" /></label></th>
                                                                        
                                                                        <th style="text-align: left;  padding:2px 3px; width:300px;"><label ><firc:text name="Details" /></label></th>
                                                                        
                                                                      <th style="text-align: left;  padding:2px 3px; width:200px;"><label ><firc:text name="Overridden" /></label></th>

                                                                   </tr>
                                                                
                                                                  <c:if test="${empty errorList}">
                                                                        <tr>
                                                                        <td colspan="5">  No records found </td>
                                                                        </tr>
                                                                   </c:if>
                                                               <c:if test="${not empty errorList}">   
                                                                  <firc:iterator value="errorList" id="errorList">
                                                                  <tr>
                                                                        <td style="background-color:#FF0000; padding:2px 3px; color:#FFFFFF;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="errorId"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                      <td style="background-color:#FF0000; padding:2px 3px; color:#FFFFFF;">
                                                                              <div class="form-group" align="left">
                                                                                <firc:property value="errorDesc"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="background-color:#FF0000; padding:2px 3px; color:#FFFFFF;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="errorCode"/>                                                                                
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="background-color:#FF0000; padding:2px 3px; color:#FFFFFF;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="errorDetails"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="background-color:#FF0000; padding:2px 3px; color:#FFFFFF;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="errorMsg"/>                                                                           
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>                                                                                                                   
                                                                  
                                                                 </tr>
                                                                 </firc:iterator>
                                                                 </c:if>
                                                            </tbody>
                                                      </table>
                                          </div>
                                    </div>
                              </div>
                        </div> <br/>
                        
                   <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">       
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span> </span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;<label ><firc:text name="IRM Adjustment/Closure Screen" /></label></h5>
                                    </div>
                                    
                                    <div class="row page_content">
                                          <div class="col-md-6">
                                                <div class="form-group" >
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Transaction Ref No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="transactionrefno" name="irmAdjVO.transactionrefno"
                                                                  cssClass="form-control text_box" /><br /> <br />
                                                                  
                                                            <input type="button" id="fetch" value="Fetch Details"
                                                                  class="button" ondblclick="return false"/>                                                                                    
                                                      </div>                                                
                                                      
                                                </div> <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">IEcode</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="iecode" name="irmAdjVO.iecode"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div> <br />           
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Amount Adjusted</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="amount" name="irmAdjVO.amount"
                                                                  cssClass="form-control text_box" readonly="true" onkeypress="return isNumber(event)" />
                                                      </div>
                                                </div> <br />                                               
                                          

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Letter No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="letterNo" name="irmAdjVO.letterNo"
                                                                  maxlength = "50" cssClass="form-control text_box" onkeypress="return validateSpecialChar(event)" />
                                                      </div>
                                                </div> <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Document No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="docNo" name="irmAdjVO.docNo"
                                                                  maxlength = "20" cssClass="form-control text_box" onkeypress="return validateSpecialChar(event)" />
                                                      </div>
                                                </div> <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Document Date</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="docDate" name="irmAdjVO.docDate"
                                                                  cssClass="datepicker1 form-control text_box" readonly="true" />
                                                      </div>
                                                </div> <br />                                         
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Ack Status</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="ackStatus" name="irmAdjVO.ackStatus" readonly="true"
                                                                  cssClass="form-control text_box"  />                                    
                                                      </div>
                                                </div>      <br />      
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Checker Remarks</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textarea id="checkerRemarks" name="irmAdjVO.checkerRemarks"
                                                             maxlength = "200"      style="width: 180px;" cols="20" rows="5" readonly="true" />
                                                      </div>
                                                </div> <br />                             

                                          </div>
                                          <div class="col-md-6">                                      
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">ADcode</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="adcode" name="irmAdjVO.adcode"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div> <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Currency</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="currency" name="irmAdjVO.currency"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div> <br />     

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Approved by</label>
                                                      <div class="col-md-4 input-group input-group-md">                                                     
                                                            <firc:select list="extensionInd" name="irmAdjVO.approvedBy"  id="approvedBy"
                                                                  headerKey = "" headerValue = "<---->"     cssClass="form-control text_box"
                                                                  style="width: 180px; height: 25px;"/>
                                                      </div>
                                                </div> <br />
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Reason For Adjustment</label>
                                                      <div class="col-md-4 input-group input-group-md">                                                           
                                                            <firc:select list="reasonAdj" name="irmAdjVO.reason"  id="reason"
                                                                  headerKey = "" headerValue = "<---->"     cssClass="form-control text_box"
                                                                  style="width: 180px; height: 25px;"/>
                                                      </div>
                                                </div> <br />     

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Adjustment Date</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="adjDate" name="irmAdjVO.adjDate"
                                                                  cssClass="datepicker form-control text_box" readonly="true" />
                                                      </div>
                                                </div>      <br />                                                      
                                          
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Port of Receiving</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="docPort" name="irmAdjVO.docPort"
                                                                  maxlength = "20" cssClass="form-control text_box" />
                                                      </div>
                                                </div>      <br />      
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Record Indicator</label>
                                                      <div class="col-md-4 input-group input-group-md">                                                       
                                                        <firc:select list="recordInd" name="irmAdjVO.recordInd"  id="recordInd"
                                                                  headerKey = "" headerValue = "<---->"     cssClass="form-control text_box"
                                                                  style="width: 180px; height: 25px;"/>                                                                                     
                                                      </div>
                                                </div> <br />                                   
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Remarks</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textarea id="remarks" name="irmAdjVO.remarks"
                                                                  maxlength = "200" style="width: 180px;" cols="20" rows="5" readonly="false" />
                                                      </div>
                                                </div> <br />     
                                                <firc:hidden name="irmAdjVO.status" id="status"></firc:hidden>    
                                                <firc:hidden name="irmAdjVO.serialNo" id="serialNo"></firc:hidden>                                                      
                                          </div>
                                    </div>
                              </div>            
                        </div>       <br/><br/><br/><br/><br/>
            </div>
      </div>
</firc:form>

</body>
</html>