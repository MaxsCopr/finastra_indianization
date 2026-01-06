<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>Rejected BOE</title>
      <jsp:include page="/view/common/header.jsp" />
      <script type="text/javascript" src="boe/scripts/rejectedRecords/rejectedFiles.js"></script>
</head>

<body class="body_bg">
      <%@ include file="/view/includes/TITLE.jsp"%>
      <boe:form method="post" id="myForm" name="form">
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="makerProcess">Close</a></li>
                        <!--  </ul>
                        </div>
                        <br />
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked"> -->
                              
                                    <!-- <li style="text-align: center;"><a href="#" id="deleteBOE">Delete</a></li> -->
                              <!--  <li style="text-align: center;"><a href="viewRejectedRecords" id="reset">Reset</a></li> -->
                              <li style="text-align: center;"><a href="#" id="deleteRec" onclick="deleteRec();">Delete</a></li>
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
                                                            style="font-weight: normal;">Payment Reference No.</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="refNo" name="boeSearchVO.paymentRefNo"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Payment Serial No.</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="mt940RefNo"
                                                                  name="boeSearchVO.paymentSerialNo"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                          </div>

                                          <div class="col-md-5">

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Payment Currency</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="currency"
                                                                  name="boeSearchVO.paymentCurrency" maxlength="3"
                                                                  style="text-transform:uppercase"
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


                                                <br />



                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <input type="button" value="Refresh" class="button"
                                                                  id="boeRejSearch" style="margin-top: 5px;" />
                                                      </div>
                                                </div>

                                          </div>
                                    </div>
                                    <br /> <br />

                                    <div class="row page_content">
                                          <div class="col-md-12">
                                                <div class="page_collapsible" id="body-section1">
                                                      <span></span>
                                                      <h5 style="font-weight: bold; font-size: 11px;">&nbsp;Payment
                                                            Details</h5>
                                                </div>
                                                <div class="col-md-12">
                                                      <div class="form-group">
                                                            <div class="table" style="float: left;">
                                                                  <table  id="billDetailsList">
                                                                        <tbody>
                                                                              <tr>
                                                                                    <th   style="text-align: left; width: 150px; padding: 4px 5px;"><label>Bill
                                                                                                of Entry No</label></th>

                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Bill
                                                                                                of Entry Date </label></th>
                                                                                    
                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>
                                                                                                Portcode</label></th>

                                                                                    <th   style="text-align: left; width: 150px; padding: 4px 5px;"><label>Payment
                                                                                                Reference No</label></th>

                                                                                    <th   style="text-align: left; width: 150px; padding: 4px 5px;"><label>Payment
                                                                                                Serial No</label></th>

                                                                                    <th style="text-align: left; width: 150px; padding: 4px 5px;"><label>Payment
                                                                                                Currency</label></th>

                                                                                    <th   style="text-align: left; width: 150px; padding: 4px 5px;"><label>Payment
                                                                                                Amount</label></th>

                                                                                    <th   style="text-align: left; width: 150px; padding: 4px 5px;"><label>Endorsed
                                                                                                Amount</label></th>
                                                                                                
                                                                                    <th   style="text-align: left; width: 150px; padding: 4px 5px;"><label>Delete the Records</label></th>
                                                                              </tr>

                                                                              <c:if test="${empty rejectedBoeList}">
                                                                                    <tr>
                                                                                          <td colspan="9">No records found</td>
                                                                                    </tr>
                                                                              </c:if>
                                                                              <c:if test="${not empty rejectedBoeList}">
                                                                              
                                                                              <boe:iterator value="rejectedBoeList" id="rejectedBoeList">
                                                                              
                                                                                    <tr class="boeList" ondblclick="boeDetails(this)" >
                                                                                    
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
                                                                                          <td style="text-align: right; padding: 3px 4px;">
                                                                                                <div class="form-group" align="right">
                                                                                                      <boe:property value="paymentAmount" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="text-align: right; padding: 3px 4px;">
                                                                                                <div class="form-group" align="right">
                                                                                                      <boe:property value="endorseAmt" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          
                                                                                          <td style="text-align: right; padding: 3px 4px; display : none;" >
                                                                                                <div class="form-group" align="right">
                                                                                                      <boe:property value="pageType" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          
                                                                                          <%-- <td style="text-align: right; padding: 3px 4px;">
                                                                                                <div class="form-group" align="right">
                                                                                                      <boe:checkbox name="chkList"
                                                                                                                  fieldValue="%{#rejectedBoeList.boeNo+':'
                                                                                                                  +#rejectedBoeList.boeDate+':'
                                                                                                                  +#rejectedBoeList.portCode}">
                                                                                                            </boe:checkbox>
                                                                                                            
                                                                                                            
                                                                                                            <boe:checkbox name="chkList"
                                                                                                            fieldValue="%{#rejectedBoeList.boeNo+':'+#rejectedBoeList.boeDate+':'+#rejectedBoeList.portCode}">
                                                                                                         </boe:checkbox>
                                                                                                </div>
                                                                                          </td> --%>
                                                                                          
                                                                                          <td style="text-align: center;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:checkbox name="chkList"
                                                                                                                  fieldValue="%{#rejectedBoeList.boeNo+':'+#rejectedBoeList.boeDate+':'+#rejectedBoeList.portCode}">
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
                                                      </div>
                                                      <input type="hidden"  name="check" id="check"/>
                                                      <input type="hidden" id="boeData" name="boeData" />
                                                </div>

                                          </div>
                                    </div>
                              </div>
                              <br />
                        </div>
                  </div>
            </div>
      </boe:form>

</body>
</html>