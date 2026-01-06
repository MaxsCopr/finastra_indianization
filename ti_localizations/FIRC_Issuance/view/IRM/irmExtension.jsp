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
<title>IRM Extension Page</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="js/IRM/irmExtension.js"></script>  

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

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IRM Extension</h5>
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
                                                                        <td style="padding:2px 3px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="errorId"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                      <td style="padding:2px 3px;">
                                                                              <div class="form-group" align="left">
                                                                                <firc:property value="errorDesc"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="padding:2px 3px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="errorCode"/>                                                                                
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:2px 3px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="errorDetails"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding:2px 3px;">
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
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;<label ><firc:text name="IRM Extension Screen" /></label></h5>
                                    </div>
                                    
                                    <div class="row page_content">
                                          <div class="col-md-6">
                                                <div class="form-group" >
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Transaction Ref No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="transactionrefno" name="irmExVO.transactionrefno"
                                                                  cssClass="form-control text_box" /><br /> <br />
                                                                  
                                                            <input type="button" id="fetch" value="Fetch Details"
                                                                  class="button" ondblclick="return false"/>                                                                                    
                                                      </div>                                                
                                                      
                                                </div> <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">IEcode</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="iecode" name="irmExVO.iecode"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div> <br />                                               
                                          

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Letter No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="letterNo" name="irmExVO.letterNo"
                                                                  maxlength = "20" cssClass="form-control text_box" onkeypress="return validateSpecialChar(event)" />
                                                      </div>
                                                </div> <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Extension Date</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="extensionDate" name="irmExVO.extensionDate" readonly="true"
                                                                  cssClass="datepicker form-control text_box"  />                               
                                                      </div>
                                                </div>      <br />            
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Extended Date</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="previousExtDate" name="irmExVO.previousExtDate"
                                                            readonly="true" cssClass="form-control text_box" maxlength="10" />
                                                      </div>
                                                </div>      <br />      
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Checker Remarks</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textarea id="remarks" name="irmExVO.remarks"
                                                                  style="width: 180px;" cols="20" rows="5" readonly="true" />
                                                      </div>
                                                </div> <br />                             

                                          </div>
                                          <div class="col-md-6">                                      
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">ADcode</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="adcode" name="irmExVO.adcode"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div> <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Extension Indicator </label>
                                                      <div class="col-md-4 input-group input-group-md">                                                     
                                                            <firc:select list="extensionInd" name="irmExVO.extensionInd"  id="extensionInd"
                                                                  headerKey = "" headerValue = "<---->"     cssClass="form-control text_box"
                                                                  style="width: 180px; height: 25px;"/>
                                                      </div>
                                                </div> <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Letter Date</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="letterDate" name="irmExVO.letterDate"
                                                                  cssClass="datepicker form-control text_box" readonly="true" />
                                                      </div>
                                                </div>      <br />      
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Record Indicator</label>
                                                      <div class="col-md-4 input-group input-group-md">                                                                 
                                                            <firc:textfield id="recordInd" name="irmExVO.recordInd"
                                                                  cssClass="form-control text_box" readonly="true" />           
                                                      </div>
                                                </div> <br />                                   
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Reason For Extension</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textarea id="reason" name="irmExVO.reason"
                                                                  maxlength = "200" style="width: 180px;" cols="20" rows="5" readonly="false" />
                                                      </div>
                                                </div> <br />     
                                                <firc:hidden name="irmExVO.status" id="status"></firc:hidden>     
                                                <firc:hidden name="irmExVO.serialNo" id="serialNo"></firc:hidden>                               
                                          </div>
                                    </div>
                              </div>            
                        </div>       <br/><br/><br/><br/><br/>
            </div>
      </div>
</firc:form>

</body>
</html>