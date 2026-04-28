<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>Checker Page</title>
      <jsp:include page="/view/common/header.jsp" />  
      <script type="text/javascript" src="boe/scripts/checker/checker_forms.js"></script>
      <script>
      function windowclose()
      {     
            top.close();      
      }
      
      function selectAll()
      {
            if(document.forms[0].selectal.checked==true)
            {
                  for(var i=0;i<document.forms["myForm"].chkList.length;i++)
                  {
                        document.forms["myForm"].chkList[i].checked=true;
                  }
            }else{
                  for(var i=0;i<document.forms[0].chkList.length;i++)
                  {
                        document.forms["myForm"].chkList[i].checked=false;
                  }
            }
      }
      </script>
</head>
<body class="body_bg">
      <%@ include file="/view/includes/TITLE.jsp"%>

      <boe:form method="post" id="myForm" name="myForm">


            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                              
                              <boe:if test="%{#session.xEvtRefNum.length() > 0}">
                                          <li style="text-align: center;"><a href="#" onclick="windowclose()">Close</a></li>
                                    </boe:if>         
                                    <boe:else>
                                          <li style="text-align: center;"><a href="checkerProcess">Close</a></li>
                                    </boe:else>
                                    <!-- <li style="text-align: center;"><a href="checkerProcess">Close</a></li> -->
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="ok">Approve</a></li>
                                    <li style="text-align: center;"><a href="#" id="reject">Reject</a></li>
                                    <li style="text-align: center;"><a href="resetBOEDetails" id="reset">Reset</a></li>
                              </ul>
                        </div>
                        <br />

                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill
                              of Entry</h5>

                        <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">
                          <div class="col-md-12">
                                          <div class="page_collapsible " id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px;">&nbsp;Input
                                                            Details</h5>
                                          </div>
                                          <div class="row page_content">
                                                <div class="col-md-7">
      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;">BOE Date From</label>
      
                                                            <div class="col-md-3 input-group input-group-md" style="float:left;">
                                                                  <boe:textfield id="boeDateFrom" name="boeSearchVO.paymentDateFrom" readonly="true"
                                                                         cssClass="datepicker form-control text_box" />
                                                            </div>
      
                                                            <label class="col-md-1 Control-label"
                                                                  style="font-weight: normal;">To</label>
      
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="boeDateTo" name="boeSearchVO.paymentDateTo" readonly="true"
                                                                         cssClass="datepicker1 form-control text_box" />
                                                            </div>
                                                            
      
                                                      </div>                                                
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;">Endorsed Amount From</label>
      
                                                            <div class="col-md-3 input-group input-group-md" style="float:left;">
                                                                  <boe:textfield id="amountFrom" name="boeSearchVO.amountFrom" onkeypress="return isNumber(event)" onblur="compareValue('amountFrom','amountTo')"
                                                                        cssClass="form-control text_box" />
                                                            </div>
      
                                                            <label class="col-md-1 Control-label"
                                                                  style="font-weight: normal;">To</label>
      
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="amountTo" name="boeSearchVO.amountTo" onkeypress="return isNumber(event)" onblur="compareValue('amountFrom','amountTo')"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                    
                                                      </div>
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;">Payment Reference No.</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                            
<!-- Added on 03032018 -->
                  <boe:if test="%{#session.loginType == 'Yes'}">
                        <boe:textfield id="refNo" value="%{#session.xMstRefNum}" cssClass="form-control text_box"  readonly="true"/>
                  </boe:if>
                  <boe:else>                          
                        <boe:textfield id="refNo" name="boeSearchVO.paymentRefNo" cssClass="form-control text_box" />
                  </boe:else>
                                                            </div>
                                                      </div>                                                
      
                                                </div>
      
                                                <div class="col-md-5">
      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;">Payment Currency</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="currency" name="boeSearchVO.paymentCurrency"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;">Bill of Entry No.</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="bicName" name="boeSearchVO.boeNo"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;">Payment Serial No.</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <boe:textfield id="mt940RefNo" name="boeSearchVO.paymentSerialNo"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>      <br/>
                                                      
                                                
                                                      
                                                      <div class="form-group">
                                                            <label class="col-md-4 Control-label"
                                                                  style="font-weight: normal;"></label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <input type="button" value="Refresh" class="button"
                                                                        id="boeSearch" style="margin-top: 5px;" />
                                                            </div>
                                                      </div>
      
                                                </div>
                                          </div><br /><br/>

                              <div class="row page_content">
                                    <div class="col-md-12">
                                                <div class="page_collapsible" id="body-section1">
                                                      <span></span>
                                                      <h5 style="font-weight: bold; font-size: 11px;">&nbsp;Payment
                                                            Details</h5>
                                                </div>
                                                <div class="col-md-12">
                                                      <div class="form-group">
                                                            <div class="table_Checker" style="float:left;">
                                                                  <table border="1px" align="left" id="billDetailsList">
                                                                        <tbody>
                                                                              <tr>
                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Bill of Entry No</label></th>

                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Bill of Entry Date </label></th>
                                                                                    
                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Port code </label></th>

                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Payment Reference No</label></th>

                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Payment Serial No</label></th>
                                                                                                
                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Payment Currency</label></th>

                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Payment Amount</label></th>
                                                                                                
                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Endorsed Amount</label></th>
                                                                                                
                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><boe:checkbox name="selectal" id="selectal" style="width: 12px;height: 12px" onclick="selectAll();"></boe:checkbox><label>Status</label></th>

                                                                              </tr>
                                                                              
                                                                              <c:if test="${empty multiPaymentReferenceList}">
                                                                                    <tr>
                                                                                          <td colspan="9">No records found</td>
                                                                                    </tr>
                                                                              </c:if>
                                                                              <c:if test="${not empty multiPaymentReferenceList}">
                                                                                    <boe:iterator value="multiPaymentReferenceList" id="multiPaymentReferenceList">
                                                                                          <tr class="boeList" ondblclick="boeDetails(this)">
                                                                                                <td style="text-align: left; padding: 4px 5px;">
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="boeNo" />

                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td style="text-align: left; padding: 4px 5px;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:property value="boeDate" />
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                
                                                                                                <td style="text-align: left; padding: 4px 5px;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:property value="portCode" />
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                
                                                                                                <td style="text-align: left; padding: 4px 5px;">
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="paymentRefNo" />

                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td style="text-align: left; padding: 4px 5px;">
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="partPaymentSlNo" />
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td style="text-align: left; padding: 4px 5px;">
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="paymentCurr" />
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td style="text-align: right; padding: 4px 5px;">
                                                                                                      <div class="form-group" align="right">
                                                                                                            <boe:property value="paymentAmount" />
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td style="text-align: right; padding: 4px 5px;">
                                                                                                      <div class="form-group" align="right">
                                                                                                            <boe:property value="endorseAmt" />
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                
                                                                                                <td style="text-align: center;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:checkbox name="chkList"
                                                                                                                  fieldValue="%{#multiPaymentReferenceList.boeNo+':'
                                                                                                                  +#multiPaymentReferenceList.boeDate+':'
                                                                                                                  +#multiPaymentReferenceList.portCode+':'
                                                                                                                  +#multiPaymentReferenceList.paymentRefNo+':'
                                                                                                                  +#multiPaymentReferenceList.partPaymentSlNo}">
                                                                                                            </boe:checkbox>
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                          </tr>
                                                                                    </boe:iterator>
                                                                              </c:if>
                                                                        </tbody>
                                                                  </table>                                                                                              
                                                            </div>      
                                                            <div>
                                                                  <label style="float: left; margin-left: 15px;">Remarks</label><br></br>
                                                                  <div class="form-group" style="float: left; margin-left: 15px;">
                                                                        <boe:textarea name="remarks" id="remarks" cols="15" rows="5"></boe:textarea>
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </div>                                                                              
                                                      </div>                                                                        
                                                      <input type="hidden" id="check" name="check"  />            
                                                      <input type="hidden" id="boeData" name="boeData"  />                                      
                                                </div>
                                                <span></span>&nbsp;
                                                <h4 style=" font-size: 13px;">&nbsp; *Note- Kindly double click on record to view details</h4>  
                                          </div>
                                    </div><br /><br />
                              </div>
                              <br />
                        </div>
                  </div>
             </div>
      </boe:form>

</body>
</html>