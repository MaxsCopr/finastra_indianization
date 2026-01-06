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

<title>FIRC Our Bank Page</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="js/ourbank/fircOurBank.js"></script>

</head>

<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  

  <firc:form method="post" id="myForm" name="myForm">
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="makerProcess">Close</a></li>
                        </ul>
                  </div>
                  <br />
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li><a href="#" onclick="submitForm()"><firc:text name="label.firc.ok" /></a></li>
                                    <li><a href="#" onclick="validateForm()"><firc:text name="label.firc.validate" /></a></li>
                                    <li><a href="#" onclick="resetForm()"><firc:text name="label.firc.cancel" /></a></li>
                              </ul>
                  </div>
                  <br />
                  
            </div>
                  
            <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FIRC Our Bank</h5>
                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;<firc:text name="label.firc.error" /></h5>
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
                                          <div class="table1">
                                                <table border="1px" align="left" id="errorList">
                                                               <tbody>
                                                                   <tr>
                                                                         <th style="text-align: left; padding:2px 3px; width:200px;"><label ><firc:text name="label.firc.error.severity" /></label></th>

                                                                        <th style="text-align: left;  padding:2px 3px; width:200px;"><label ><firc:text name="label.firc.error.description" /></label></th>

                                                                        <th style="text-align: left;  padding:2px 3px; width:200px;"><label ><firc:text name="label.firc.error.steps" /></label></th>
                                                                        
                                                                        <th style="text-align: left;  padding:2px 3px; width:300px;"><label ><firc:text name="label.firc.error.details" /></label></th>
                                                                        
                                                                      <th style="text-align: left;  padding:2px 3px; width:200px;"><label ><firc:text name="label.firc.error.overridden" /></label></th>

                                                                   </tr>
                                                                
                                                                  <c:if test="${empty errorList}">
                                                                        <tr>
                                                                        <td colspan="5"> No Error found </td>
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
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;<label ><firc:text name="FIRC Our Bank" /></label></h5>
                                    </div>
                                    
                                    <div class="row page_content">
                                          <div class="col-md-6">
                                                <div class="form-group" >
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Transaction Ref No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="transactionrefno" name="ourBankVO.transactionrefno"
                                                                  cssClass="form-control text_box" /><br /> <br />
                                                                  
                                                            <input type="button" id="fetch" value="Fetch Details"
                                                                  class="button" onclick="getFircDetails()" ondblclick="return false"/>                                                                                 
                                                      </div>                                                
                                                      
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Issuing Bank</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="issuingBank" name="ourBankVO.issuingBank"
                                                                  cssClass="form-control text_box" readonly="true" />                                                               
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Ordering Customer</label>
                                                      <div class="col-md-4 input-group input-group-md">                                                                                         
                                                            <firc:textarea id="orderingcustomer" name="ourBankVO.orderingcustomer"
                                                                  style="width: 180px;" cols="20" rows="4"  readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Remitter Country</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="rem_country" name="ourBankVO.rem_country"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Exchange Rate</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="exchange_rate" name="ourBankVO.exchange_rate"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>                                                

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Amount</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="amount" name="ourBankVO.amount"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Beneficiary Customer</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textarea id="benificiarydetails" name="ourBankVO.benificiarydetails"
                                                                  style="width: 180px;" cols="20" rows="4"  readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Paid Amount</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="paidamount" name="ourBankVO.paidamount"
                                                                  cssClass="form-control text_box" readonly="true" />

                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Purpose Code</label>

                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="purposecode" name="ourBankVO.purposecode"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> Value Date</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="value_date" name="ourBankVO.value_date"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Remarks</label>
                                                      <div class="col-md-4 input-group input-group-md">                                                           
                                                            <firc:textarea id="remarks" name="ourBankVO.remarks"
                                                             style="width: 180px;" cols="20" rows="4" ></firc:textarea>
                                                      </div>
                                                </div>
                                                
                                          </div>
                                          <div class="col-md-6">                                      
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">FIRC Date</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="fircdate" name="ourBankVO.fircdate"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">FIRC Serial No</label>

                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="fircno" name="ourBankVO.fircno"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Issuing Branch</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textarea id="issuingbranch" name="ourBankVO.issunibranch"
                                                                  style="width: 180px;" cols="20" rows="4"  readonly="true" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Remitting Bank Details</label>
                                                      <div class="col-md-4 input-group input-group-md">                                                           
                                                            <firc:textarea id="rembank" name="ourBankVO.rembank"
                                                             style="width: 180px;" cols="20" rows="4" readonly="true" ></firc:textarea>
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Currency</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="currency" name="ourBankVO.currency"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Purpose of Remittance</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textarea id="purposedesc" name="ourBankVO.purposedesc"
                                                                  style="width: 180px;" cols="20" rows="4" readonly="true" />
                                                      </div>
                                                </div>
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Paid Currency</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="paidcurrency" name="ourBankVO.paidcurrency"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>                                                

                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">CIF No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="cif_no" name="ourBankVO.cif_no"
                                                                  cssClass="form-control text_box" readonly="true" />
                                                      </div>
                                                </div>                                                

                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">FIRC/FIRA</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:select list="inwardType" id="inwardType" name="ourBankVO.inwardType"  
                                                            headerKey = "" headerValue = "<--->"  style="width: 180px; height: 25px;"/>
                                                      </div>
                                                </div>                                                
                                                
                                                
                                          </div>
                                    </div>
                              </div>            
                        </div>       <br/><br/><br/><br/><br/>
            </div>
      </div>
</firc:form>

</body>
</html>